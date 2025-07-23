package GameMode;

import Board.Board;
import Board.Cell;
import GameMode.gameutils.GameTimer;
import Player.Player;
import Player.Worker;

import GodCard.GodCard;
import GodCard.PowerPhase;
import artifacts.Artifact;
import artifacts.ArtifactCellCondition;
import frontend.SelectedStatus;
import artifacts.shops.ShopPopup;
import Board.BoardHighlighter;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.Vector;

/**
 * Handles two-player game logic, including turn management, game rules,
 * GodCard integration, and win condition checking.
 */
public class TwoPlayerConfig extends Config {
    private Board board;
    private Player[] players;
    private int currentPlayerIndex;
    private BoardHighlighter boardHighlighter;
    private Worker activeWorker;
    private int turnTime = 900; //15 minutes
    private GameTimer player1Timer = new GameTimer(turnTime);
    private GameTimer player2Timer = new GameTimer(turnTime);
    private Artifact currentArtifactInUse;


    public TwoPlayerConfig(Vector<String> playerNames, Vector<GodCard> gods) {
        super(playerNames, gods);
        // Change the board 5x5 size
        this.boardHeight = 5;
        this.boardWidth = 5;
        this.numPlayers = 2;
    }

    /**
     * Initialises the board and assigns player instances and GodCards.
     */
    @Override
    public void setup() {
        this.board = new Board(boardWidth, boardHeight);
        this.boardHighlighter = new BoardHighlighter(this.getBoard());

        players = new Player[2];
        players[0] = new Player(playerNames.get(0), gods.get(0), Color.BLUE);
        players[1] = new Player(playerNames.get(1), gods.get(1), Color.RED);

        currentPlayerIndex = new Random().nextInt(2);
        numPlayers = 2;

        // Add listener for both timers
        addPlayerTimer(player1Timer);
        addPlayerTimer(player2Timer);

        player1Timer.addListener(this);
        player2Timer.addListener(this);

    }
    @Override
    public void startTurn(Player current){
        if (currentPlayerIndex == 0) {
            player2Timer.pause();
            player1Timer.start(true);
        } else {
            player1Timer.pause();
            player2Timer.start(true);
        }

        // Display shop during buy phase
        if(!turnState.hasCompletedBuyPhase()){
            // Delay pop up by 1 second
            javax.swing.Timer delay = new javax.swing.Timer(800, e -> {
                currentShopPopup = new ShopPopup(current, shopManager, turnState);
                currentShopPopup.setVisible(true);
            });
            delay.setRepeats(false);  // make sure it only fires once
            delay.start();
        }
    }

    /**
     * Ends the current player's turn and resets flags for the next.
     */
    @Override
    public void endTurn() {

        if (!turnState.isTurnComplete()) {
            System.out.println("You must move, build, and use or skip god power before ending the turn.");
            return;
        }

        // Pause the timer for the current player
        if (currentPlayerIndex == 0) {
            player1Timer.pause();
        } else {
            player2Timer.pause();
        }

        currentPlayerIndex = (currentPlayerIndex + 1) % 2;
        boardHighlighter.clearHighlights();
        turnState.resetTurn(); // Set all flags to false
        currentShopPopup.dispose();
        startTurn(players[currentPlayerIndex]); // Start the next player's timer
    }

    /**
     * Retrieves the duration of the timer
     * @return duration for each turn
     */
    @Override
    public int getTurnTime(){
        return turnTime;
    }


    /**
     * Is called when the Game Timer expires.
     * Handles the actions to be done when the countdown is at 0:00.
     * Automatically crown the other player (not current)  as the winner
     */
    @Override
    public void onTimeExpired() {
        System.out.println("Time ENDS");
        shopManager.closeShop(turnState);
        currentShopPopup.dispose();
        turnState.completeTurn(); // consider the turn has been completed when timer ends
        board.clearMarkings(); // Clear any cell highlights


        // Automatically set the winner as the other player
        if (player1Timer.getSecondsLeft() <= 0) {
            setWinner(players[1]);
        } else {
            setWinner(players[0]);
        }
    }


    /**
     * Gets the current player
     * @return the current player
     */
    @Override
    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    /**
     * Get the current active worker
     * @return the current selected worker
     */
    public Worker getActiveWorker(){
        return this.activeWorker;
    }

    /**
     * Sets the active worker to the selected worker
     * @param worker the worker to set
     */
    public void setActiveWorker(Worker worker){
        this.activeWorker = worker;
    }

    /**
     * Resets the current active worker to null
     */
    public void resetActiveWorker(){
        this.activeWorker = null;
    }

    /**
     * Gets all the players in this game
     * @return the players
     */
    @Override
    public Player[] getPlayers() {
        return players;
    }

    /**
     * Retrieves this current board
     * @return hte current game board
     */
    @Override
    public Board getBoard() {
        return board;
    }

    /**
     * Executes god power logic when button is clicked; depending on current phase and validity.
     */
    @Override
    public void useGodPower() {
        Player currentPlayer = getCurrentPlayer();
        GodCard god = currentPlayer.getGod();
        Worker activeWorker = getActiveWorker();

        if (!turnState.hasMoved() && god.getPowerPhase() == PowerPhase.MOVE) {
            System.out.println("You must move before using god power.");
            return;
        }

        if (!turnState.hasBuilt() && god.getPowerPhase() == PowerPhase.BUILD) {
            System.out.println("You must build before using god power.");
            return;
        }

        if (turnState.hasUsedOrSkippedGodPower()) {
            System.out.println("God power already used or skipped.");
            return;
        }

        if (god.godPowerAvailable(this.getBoard(), currentPlayer, turnState, activeWorker)) {
            god.usingGodPower(this.getBoard(), boardHighlighter,currentPlayer, activeWorker);
            System.out.println("Use your god power now by clicking a cell.");

            // Automatically end turn if everything is complete
            if (turnState.hasMoved() && turnState.hasBuilt() && turnState.hasUsedOrSkippedGodPower() && god.getPowerPhase() == PowerPhase.BUILD) {
                endTurn();
            }
        } else {
            System.out.println("God power not available.");
        }
    }


    /**
     * Skips god power and optionally ends the turn.
     */
    @Override
    public void skipGodPower() {
        if (turnState.hasBuilt() && !turnState.hasUsedOrSkippedGodPower()) {
            turnState.setGodPowerUsedOrSkipped(true);

            System.out.println("God power skipped.");

            if (turnState.hasMoved() && turnState.hasBuilt()) {
                endTurn();
                return;
            }
        }

        if (!turnState.hasBuilt() && turnState.hasMoved() && !turnState.hasUsedOrSkippedGodPower()) {
            // player skipped Artemis's move power
            Cell selected = board.getSelected();
            if (selected != null && selected.getOccupiedBy() != null) {
                boardHighlighter.clearHighlights(); // Clear previous yellow MOVE highlights
                boardHighlighter.highlightBuildable(selected.getRow(), selected.getCol());
            }

            turnState.setGodPowerUsedOrSkipped(true);

        }
    }

    /**
     * Executes god power logic when button is clicked; depending on current phase and validity.
     */
    @Override
    public void useArtifact(Artifact artifact) {
        boardHighlighter.clearHighlights(); // clear all markings to avoid confusion
        Player currentPlayer = getCurrentPlayer();

        if(turnState.hasUsedOrSkippedArtifact()){
            System.out.println("You can only use an artifact once per turn");
            return;
        }

        if(turnState.hasMoved() || turnState.hasBuilt() || turnState.hasUsedOrSkippedGodPower()){
            System.out.println("You can only use an artifact in the beginning of each turn.");
            return;
        }

        // Look for artifact instance in player's inventory
        for(Artifact playerArtifact : currentPlayer.getArtifacts()){
            if (playerArtifact == artifact && playerArtifact.canUse(this.getBoard())){

                this.currentArtifactInUse = playerArtifact;
                ArtifactCellCondition condition = currentArtifactInUse.getArtifactCondition();
                boardHighlighter.clearHighlights();
                boardHighlighter.highlightUsableArtifactCells(condition);
                return;
            }
        }

        System.out.println("Player uses artifact");
    }


    /**
     * Checks if a winner has been set.
     */
    @Override
    public Player getWinner() {
        for (Player p : players) {
            if (p.isWinner()) return p;
        }
        return null;
    }

    /**
     * Returns true if the current player has no valid moves.
     */
    public boolean currentPlayerHasNoValidMoves() {
        Player current = getCurrentPlayer();

        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                Cell cell = board.getCell(row, col);
                if (cell.isOccupied() && cell.getOccupiedBy().getOwner() == current) {
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            if (dx == 0 && dy == 0) continue;
                            int newRow = row + dx;
                            int newCol = col + dy;
                            if (board.isValidPosition(newRow, newCol)) {
                                Cell target = board.getCell(newRow, newCol);
                                if (cell.getOccupiedBy().canMoveTo(target)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Handles all gameplay logic triggered when the player clicks a cell.
     */
    @Override
    public void handleClick(int row, int col) {
        Player current = getCurrentPlayer();
        GodCard god = current.getGod();
        Cell clicked = board.getCell(row, col);
        Cell selected = board.getSelected();

        // Complete buy phase
        if(!turnState.hasCompletedBuyPhase()){
            return;
        }

        // Using Artifact
        if(currentArtifactInUse != null){
            if(clicked.getStatus() == SelectedStatus.HIGHLIGHTED){

                System.out.println("Currently Artifact in Use");

                if(currentArtifactInUse.performAction(clicked, current)){
                    turnState.setUsedOrSkippedArtifact(true);
                    currentArtifactInUse = null; // reset to null
                    boardHighlighter.clearHighlights();
                    return;// skip click logic for this turn

                }
                else{
                    turnState.setUsedOrSkippedArtifact(true); // skipped
                }

            }else{
                System.out.println("Must select valid cell.");
            }
            return;

        }

        if (!turnState.hasMoved() && currentPlayerHasNoValidMoves()) {
            Player opponent = players[(currentPlayerIndex + 1) % players.length];
            setWinner(opponent);
            return;
        }

        // === MOVE Phase ===
        if (!turnState.hasMoved()) {
            if (clicked.isOccupied() && clicked.getOccupiedBy().getOwner() == current) {
                // Worker selection
                boardHighlighter.clearHighlights();
                clicked.setStatus(SelectedStatus.SELECTED);
                board.setLastMovedCell(clicked);
                boardHighlighter.highlightMovable(row, col);
                return;
            }

            if (clicked.getStatus() == SelectedStatus.HIGHLIGHTED &&
                    selected != null &&
                    selected.isOccupied() &&
                    selected.getOccupiedBy().canMoveTo(clicked)) {

                setActiveWorker(selected.getOccupiedBy()); // Set the worker currently being used

                // Perform movement
                boolean reachedLevel3 = selected.getOccupiedBy().move(clicked);
                if (reachedLevel3) { //Check if the worker is on Level 3
                    setWinner(current);
                }

                boardHighlighter.clearHighlights();
                clicked.setStatus(SelectedStatus.SELECTED);
                boardHighlighter.highlightBuildable(clicked.getRow(), clicked.getCol());
                turnState.setMoved(true);

                return;
            }

            return; // prevent invalid clicks during MOVE phase
        }

        // === BUILD Phase (including god power second move) ===
        if (turnState.hasMoved() && !turnState.hasBuilt()) {

            // Handle MOVE-phase god power (e.g., Artemis/Triton second move)
            if (god.getPowerPhase() == PowerPhase.MOVE &&
                    !turnState.hasUsedOrSkippedGodPower() &&
                    clicked.getStatus() == SelectedStatus.HIGHLIGHTED) {

                // Try to use God Power
                boolean useGodPower = god.performExtraAction(this.getBoard(), boardHighlighter, getActiveWorker(), clicked, turnState);

                // If has player clicked "Use God Power" Button
                if (useGodPower) {
                    if (turnState.hasUsedOrSkippedGodPower()) {

                        // God is done (Triton ended on non-perimeter or Artemis used second move)
                        boardHighlighter.clearHighlights();
                        clicked.setStatus(SelectedStatus.SELECTED);
                        System.out.println("Clicked:" + clicked.getRow() + clicked.getCol());
                        boardHighlighter.highlightBuildable(clicked.getRow(), clicked.getCol());
                        turnState.setMoved(true);
                    }

                    return; // if god not done, stay in extra move phase

                }

                // If player has not clicked on "Use God Power" Button but clicked on a cell, assume the player skipped god power
                if(!turnState.hasUsedOrSkippedGodPower()){
                    turnState.setGodPowerUsedOrSkipped(true);
                }
            }

            // Standard build
            if (clicked.getStatus() == SelectedStatus.HIGHLIGHTED &&
                    selected != null &&
                    selected.getOccupiedBy().canBuildOn(clicked)) {

                board.build(clicked.getRow(), clicked.getCol());
                boardHighlighter.clearHighlights();
                selected.setStatus(SelectedStatus.SELECTED);
                turnState.setBuilt(true);
                increasePlayerToken(current, clicked);

                if (god.getPowerPhase() == PowerPhase.MOVE) {
                    resetActiveWorker();
                    endTurn();
                }

                return;
            }

            return; // ignore invalid build clicks
        }

        // === Optional God Power Phase (after Build) ===
        if (turnState.hasMoved() && turnState.hasBuilt() && !turnState.hasUsedOrSkippedGodPower() &&
                god.getPowerPhase() == PowerPhase.BUILD &&
                clicked.getStatus() == SelectedStatus.HIGHLIGHTED) {

            boolean success = god.performExtraAction(this.getBoard(), boardHighlighter, getActiveWorker(), clicked, turnState);
            if (success) {
                increasePlayerToken(current, clicked);
                boardHighlighter.clearHighlights();
                resetActiveWorker();
                endTurn();
            }

            return;
        }

        // === End Turn if All Done ===

        if(turnState.isTurnComplete()){
            resetActiveWorker();
            endTurn();
        }
    }

    /**
     * Player receives a token if they build a level 3 block
     * 2 tokens received if they build a dome.
     */
    private void increasePlayerToken(Player current, Cell clicked) {
        if(clicked.getBlock().hasDome()){
            current.increaseTokens(2);
        }
        else if(clicked.getBlock().getLevel() == 3){
            current.increaseTokens(1);
        }
        System.out.println("Player tokens: " + current.getTokens());
    }

    // === State Getters for UI ===
    public boolean hasMoved() {
        return turnState.hasMoved();
    }

    public boolean hasBuilt() {
        return turnState.hasBuilt();
    }

    public boolean isGodPowerUsedOrSkipped() {
        return turnState.hasUsedOrSkippedGodPower();
    }

    public boolean hasCompletedBuyPhase(){
        return turnState.hasCompletedBuyPhase();
    }
}
