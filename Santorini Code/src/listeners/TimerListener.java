package listeners;

public interface TimerListener {
    /**
     * Handles actions to be done by keeping track of every second of the current turn.
     * @param secondsLeft the seconds remaining on the countdown timer
     */
    void onTick(int secondsLeft);

    /**
     * Handles the actions to be done when the timer expires (0:00)
     */
    void onTimeExpired();
}

