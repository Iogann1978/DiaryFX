package ru.home.diaryfx.control;

import java.time.LocalTime;

import javafx.scene.control.Spinner;

public class TimeSpinner extends Spinner<LocalTime> {

	public TimeSpinner() {
		setEditable(true);
		TimeSpinnerValueFactory factory = new TimeSpinnerValueFactory();
		super.setValueFactory(factory);
	}
	
	public void setValue(LocalTime time) {
		getValueFactory().setValue(time);
	}
}
