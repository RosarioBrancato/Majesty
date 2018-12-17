package ch.fhnw.projectbois.time;

import java.util.Timer;
import java.util.TimerTask;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * 
 * Some concepts were inspired by
 * https://stackoverflow.com/questions/6118922/convert-seconds-value-to-hours-minutes-seconds
 *
 *@author Rosario Brancato & Dario Stoeckli
 *
 */

public class Time {

	private Timer timer = null;
	private TimerTask task = null;
	
	private int counter = 0;
	private IntegerProperty periodCounter = null;

	/**
	 * Instantiates a new time.
	 */
	public Time() {
		this.periodCounter = new SimpleIntegerProperty(0);
	}

	/**
	 * Start timer.
	 *
	 * @param period the period
	 */
	public void startTimer(long period) {
		this.timer = new Timer();

		this.task = new TimerTask() {

			@Override
			public void run() {
				counter++;
				periodCounter.setValue(counter);
			}
		};

		this.timer.schedule(this.task, period, period);
	}

	/**
	 * Start countdown.
	 *
	 * @param seconds the seconds
	 */
	public void startCountdown(int seconds) {
		this.counter = seconds;
		this.timer = new Timer();

		this.task = new TimerTask() {

			@Override
			public void run() {
				counter--;
				periodCounter.setValue(counter);
			}
		};

		this.timer.schedule(this.task, 1000, 1000);
	}

	/**
	 * Stop.
	 */
	public void stop() {
		if (this.timer != null) {
			this.task.cancel();
			this.timer.cancel();

			this.task = null;
			this.timer = null;
		}
	}

	public IntegerProperty getPeriodCounterProperty() {
		return this.periodCounter;
	}

	// Get the counter in a nice format MINUTES:SECONDS
	public String getCounterAsString() {
		int mins = (counter % 3600) / 60;
		int seconds = counter % 60;

		return getTwoDigitValues(mins) + ":" + getTwoDigitValues(seconds);
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int seconds) {
		this.counter = seconds;
	}

	/**
	 * Gets the two digit values.
	 *
	 * @param number the number
	 * @return the two digit values
	 */
	// Double digit formatting
	private String getTwoDigitValues(int number) {
		if (number == 0) {
			return "00";
		}
		if (number / 10 == 0) {
			return "0" + number;
		}
		return String.valueOf(number);
	}

}
