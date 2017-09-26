package ru.home.diaryfx.controllers;

import java.util.Optional;

import ru.home.diaryfx.dao.DAOService;
import ru.home.diaryfx.dao.ServiceImpl;
import ru.home.diaryfx.model.Tags;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;

public class TagsDialogController implements Controller {
	@FXML
	private ListView<Tags> tagsList;
	@FXML
	private Button btnAddTag;
	@FXML
	private Button btnEditTag;
	@FXML
	private Button btnDelTag;
	private DAOService service;
	private Tags tagCurrent;
	
	@FXML
    public void initialize() {
		tagCurrent = null;
		tagsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tags>() {
             public void changed(ObservableValue<? extends Tags> observable, Tags oldValue, Tags newValue) {
            	 tagCurrent = newValue;
            }
        });
	}
	
	public void refresh() {
		if(service != null) {
			tagsList.getItems().addAll(service.findAll(Tags.class));
		}
	}
	
	public void dispose() {
		if(service != null) {
			service.close();
		}
	}
	
	@FXML
	private void onClickAddTag() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Добавить тег");
		dialog.setHeaderText("Добавляйте новые теги");
		Optional<String> response = dialog.showAndWait();

		if (response.isPresent()) {
			Tags tag = new Tags();
			tag.setTitle(response.get());
		    service.insert(tag);
		    tagsList.getItems().add(tag);
		}		
	}

	@FXML
	private void onClickEditTag() {
		if(tagCurrent == null) {
		 return;	
		}
		
		TextInputDialog dialog = new TextInputDialog(tagCurrent.getTitle());
		dialog.setTitle("Редактировать тег");
		dialog.setHeaderText("Изменить тег: " + tagCurrent.getTitle() + "?");
		Optional<String> response = dialog.showAndWait();

		if (response.isPresent()) {
			Tags tag = new Tags();
			tag.setId(tagCurrent.getId());
			tag.setTitle(response.get());
			tagCurrent = null;
		    tagCurrent = service.update(tag);
		    tagsList.getItems().set(tagsList.getSelectionModel().getSelectedIndex(), tagCurrent);
		}		
	}

	@FXML
	private void onClickDelTag() {
		if(tagCurrent == null) {
			 return;	
		}		

		Alert dialog = new Alert(AlertType.CONFIRMATION);
		dialog.setTitle("Удалить тег");
		dialog.setHeaderText("Удалить тег: " + tagCurrent.getTitle() + "?");
		Optional<ButtonType> response = dialog.showAndWait();

		if (response.isPresent()) {
		    service.delete(tagCurrent);
		    tagsList.getItems().remove(tagsList.getSelectionModel().getSelectedIndex());
		}		
	}

	public DAOService getService() {
		return service;
	}

	public void setService(DAOService service) {
		this.service = service;
	}
}
