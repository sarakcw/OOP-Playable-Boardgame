package GameMode.gameutils;
import listeners.TimerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Concrete class representing the countdown timer of every play turn.
 * It handles the starting and stopping a Game Timer.
 */
public class GameTimer {
    private final int initialSeconds;
    private int secondsLeft;
    private Timer timer;
    private final List<TimerListener> listeners = new ArrayList<>();

    /**
     * Constructor for a GameTimer
     * @param seconds the duration of the timer before it expires
     */
    public GameTimer(int seconds){
        this.initialSeconds = seconds;
        this.secondsLeft = seconds;

    }

    /**
     * Adds a listener to the list of TimerListeners
     * @param listener the listener to be added
     */
    public void addListener(TimerListener listener){
        listeners.add(listener);
    }


    /**
     * Starts the Game Timer.
     * Is called in the initial launch of the game and at the start of every play turn.
     */
    public void start(boolean resume){
        stop(); // Stop any existing timer
        if(!resume){
            this.secondsLeft = initialSeconds; // Reset if not resume
        }

        this.timer = new Timer();

        // Reset the tick
        for(TimerListener listener: listeners) {
            listener.onTick(secondsLeft);
        }
        timer.scheduleAtFixedRate(new TimerTask(){

            // Run the timer
            public void run(){
                secondsLeft--;

                // Tell listener of the update
                for(TimerListener listener: listeners){
                    listener.onTick(secondsLeft);

                }
                // Check if the timer has expired
                if(secondsLeft <= 0){
                    for(TimerListener listener: listeners){
                        stop(); // Stop the timer
                        listener.onTimeExpired();
                    }
                    ;
                }


            }
        }, 1000, 1000);
    }

    /**
     * Stops the current timer instance from running.
     */
    public void stop(){
        /*
        Check if there is a timer set up already
        e.g. start of the game = no timer yet
         */
        if(timer!= null){
            timer.cancel();
            timer.purge(); //cleanup
        }

    }

    public void pause() {
        stop(); // Stop the timer without resetting secondsLeft
    }




    /**
     * Retrieves the amount of seconds remaining on the timer countdown
     * @return seconds remaining on the timer
     */
    public int getSecondsLeft(){
        return secondsLeft;
    }
}
