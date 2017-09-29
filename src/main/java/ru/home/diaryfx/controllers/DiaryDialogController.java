package ru.home.diaryfx.controllers;

import ru.home.diaryfx.control.TimeSpinner;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class DiaryDialogController {
	@FXML
	private DatePicker dateInput;
	@FXML
	private TimeSpinner timeInput;
	@FXML
	private TextField textInput;

	public DatePicker getDateInput() {
		return dateInput;
	}
	public TextField getTextInput() {
		return textInput;
	}
	public TimeSpinner getTimeInput() {
		return timeInput;
	}
}
