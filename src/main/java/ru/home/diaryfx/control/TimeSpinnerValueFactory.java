package ru.home.diaryfx.control;

import java.time.LocalTime;
import java.time.format.FormatStyle;

import javafx.scene.control.SpinnerValueFactory;
import javafx.util.converter.LocalTimeStringConverter;

public class TimeSpinnerValueFactory extends SpinnerValueFactory<LocalTime> {

	public TimeSpinnerValueFactory() {
		super();
		setValue(LocalTime.now());
		setConverter(new LocalTimeStringConverter(FormatStyle.MEDIUM));
	}

	@Override
	public void decrement(int step) {
		if (getValue() == null)
            setValue(LocalTime.now());
        else {
            LocalTime time = (LocalTime) getValue();
            setValue(time.minusMinutes(step));
        }
	}

	@Override
	public void increment(int step) {
		if (this.getValue() == null)
            setValue(LocalTime.now());
        else {
            LocalTime time = (LocalTime) getValue();
            setValue(time.plusMinutes(step));
        }
	}
}
