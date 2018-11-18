package ch.fhnw.projectbois.time;

import java.util.Timer;
import java.util.TimerTask;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Time {

	private Timer timer = null;
	private TimerTask task = null;
	private int counter = 0;
	private IntegerProperty periodCounter = null;

	public Time() {
		this.periodCounter = new SimpleIntegerProperty(0);
	}

	public void start(long period) {
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

	public void stop() {
		if (this.timer != null) {
			this.task.cancel();
			this.timer.cancel();
			
			this.task = null;
			this.timer = null;
			this.periodCounter = null;
		}
	}

	public IntegerProperty getPeriodCounterProperty() {
		return this.periodCounter;
	}

}
