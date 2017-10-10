package ru.home.diaryfx.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ru.home.diaryfx.dao.DAOService;
import ru.home.diaryfx.dao.ServiceImpl;
import ru.home.diaryfx.model.Diary;
import ru.home.diaryfx.model.Tags;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.web.HTMLEditor;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebView;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class DiaryController implements Controller
{
	@FXML 
	private Button btnAdd;
	@FXML 
	private Button btnDel;
	@FXML 
	private Button btnEdit;
	@FXML 
	private Button btnFind;
	@FXML 
	private Button btnTags;
	@FXML
	private HTMLEditor htmlEditor;
	@FXML
	private TextArea textEditor;
	@FXML
	private WebView webView;
	@FXML
	private TabPane tabPane;
	@FXML
	private ListView<Diary> datesList;
	@FXML
	private ComboBox<Tags> tagsCombo;
	@FXML
	private ListView<Tags> tagsList;
	@FXML
	private Label statusLabel;
	private DAOService service;
	private Diary diaryCurrent;
	private Tags tagCurrent;
	
	private final static String DATE_FORMAT = "yyyy-MM-dd";
	private final static String TIME_FORMAT = "HH:mm:ss";
	
	@FXML
    public void initialize() {
		webView.setCursor(Cursor.NONE);
		WebView webViewHTML = (WebView) htmlEditor.lookup("WebView");
		GridPane.setHgrow(webViewHTML, Priority.ALWAYS);
		GridPane.setVgrow(webViewHTML, Priority.ALWAYS);
		btnDel.setDisable(true);
		btnEdit.setDisable(true);
		service = new ServiceImpl();
		diaryCurrent = null;
		refresh();
		datesList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Diary>() {
             public void changed(ObservableValue<? extends Diary> observable, Diary oldValue, Diary newValue) {
            	 if(newValue != null) {
            		 webView.getEngine().loadContent(newValue.getDescript());
            		 htmlEditor.setHtmlText(newValue.getDescript());
            		 textEditor.setText(newValue.getDescript());
            		 statusLabel.setText(newValue.getTitle());
            		 tagsList.getItems().clear();
            		 tagsList.getItems().addAll(newValue.getListTags());
            		 diaryCurrent = newValue;
            		 if(btnDel.isDisabled()) {
            			 btnDel.setDisable(false);
            		 }
            		 if(btnEdit.isDisabled()) {
            			 btnEdit.setDisable(false);
            		 }
            	 } else {
            		 btnDel.setDisable(true);
            		 btnEdit.setDisable(true);
            	 }
            }
        });
		tagCurrent = null;
		tagsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tags>() {
            public void changed(ObservableValue<? extends Tags> observable, Tags oldValue, Tags newValue) {
           	 tagCurrent = newValue;
           }
       });
    }
	
	public void refresh() {
		tagsCombo.getItems().clear();
		datesList.getItems().clear();
		Tags t = new Tags();
		t.setTitle("Все записи");
		tagsCombo.getItems().add(t);
		if(service != null) {
			datesList.getItems().addAll(service.findAll(Diary.class));
			tagsCombo.getItems().addAll(service.findAll(Tags.class));
		}
	}
	
	public void dispose() {
		if(service != null) {
			service.close();
		}
	}
	
	@FXML
	private void onClickAdd() {
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("diarydialog.fxml"));;
		Alert dialog = new Alert(AlertType.CONFIRMATION);
	    dialog.setTitle("Добавить запись");
	    dialog.setHeaderText("Добавляйте новые записи");

		try {
			dialog.getDialogPane().setContent((BorderPane) loader.load());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		DiaryDialogController addController = (DiaryDialogController) loader.getController(); 
	    Optional<ButtonType> result = dialog.showAndWait();

		if(result.isPresent() && result.get() == ButtonType.OK)	{
			String textHTML = "<html><head><meta content=\"text/html;charset=UTF-8\"/></head><body><h1>" + addController.getTextInput().getText() + "</h1><hr/></body></html>";
			Diary diary = new Diary();
			diary.setDate(addController.getDateInput().getValue().format(DateTimeFormatter.ofPattern(DATE_FORMAT)) + " " +
				addController.getTimeInput().getValue().format(DateTimeFormatter.ofPattern(TIME_FORMAT)));
			diary.setTitle(addController.getTextInput().getText());
			diary.setDescript(textHTML);
			service.insert(diary);
			textEditor.setText(textHTML);
			webView.getEngine().loadContent(textHTML);
			htmlEditor.setHtmlText(textHTML);
			datesList.getItems().add(0, diary);
			datesList.getSelectionModel().select(0);
		}		
	}

	@FXML
	private void onClickEdit() {
		if(diaryCurrent == null) {
			return;
		}
		
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("diarydialog.fxml"));;
		Alert dialog = new Alert(AlertType.CONFIRMATION);
	    dialog.setTitle("Изменить запись");
	    dialog.setHeaderText("Изменить запись: " + diaryCurrent.getTitle() + "?");

		try {
			dialog.getDialogPane().setContent((BorderPane) loader.load());
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		System.out.println(diaryCurrent.getDate());
		DiaryDialogController editController = (DiaryDialogController) loader.getController();
		String datetime[] = diaryCurrent.getDate().split(" ");
		editController.getDateInput().setValue(LocalDate.parse(datetime[0], DateTimeFormatter.ofPattern(DATE_FORMAT)));
		editController.getTimeInput().setValue(LocalTime.parse(datetime[1], DateTimeFormatter.ofPattern(TIME_FORMAT)));
	    editController.getTextInput().setText(diaryCurrent.getTitle());
		Optional<ButtonType> result = dialog.showAndWait();
			
		if(result.isPresent() && result.get() == ButtonType.OK)	{
			switch(tabPane.getSelectionModel().getSelectedIndex())
			{
			case 1:
				diaryCurrent.setDescript(htmlEditor.getHtmlText());
				break;		
			case 2:
				diaryCurrent.setDescript(textEditor.getText());
				break;
			default:
				break;
			}
			diaryCurrent.setTitle(editController.getTextInput().getText());
			diaryCurrent.setDate(editController.getDateInput().getValue().format(DateTimeFormatter.ofPattern(DATE_FORMAT)) + " " +
				editController.getTimeInput().getValue().format(DateTimeFormatter.ofPattern(TIME_FORMAT)));
			
			service.update(diaryCurrent);
			textEditor.setText(diaryCurrent.getDescript());
			htmlEditor.setHtmlText(diaryCurrent.getDescript());
			webView.getEngine().loadContent(diaryCurrent.getDescript());
			datesList.getItems().set(datesList.getSelectionModel().getSelectedIndex(), diaryCurrent);
			statusLabel.setText(diaryCurrent.getTitle());
		}    
	}

	@FXML
	private void onClickDel() {
		if(diaryCurrent == null) {
			return;
		}
		
		Alert dialog = new Alert(AlertType.CONFIRMATION);
	    dialog.setTitle("Удалить запись");
	    dialog.setHeaderText("Удалить запись: " + diaryCurrent.getTitle() + "?");
	    Optional<ButtonType> result = dialog.showAndWait();

		if(result.isPresent() && result.get() == ButtonType.OK)	{
			service.delete(diaryCurrent);
			datesList.getItems().remove(datesList.getSelectionModel().getSelectedIndex());
			cleanView();
		}
	}
	
	@FXML
	private void onClickFind() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Строка для поиска");
		dialog.setHeaderText("Введите строку:");
		Optional<String> response = dialog.showAndWait();

		if (response.isPresent()) {
			String param = "%" + response.get().toUpperCase() + "%";
			datesList.getItems().clear();
			datesList.getItems().addAll(service.find(Diary.class, param));
			cleanView();
		}		
	}
	
	@FXML
	private void onClickTags() {
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("tagsdialog.fxml"));;
		Alert dialog = new Alert(AlertType.INFORMATION);
	    dialog.setTitle("Редактирование списка тегов");
	    dialog.setHeaderText("Здесь вы можете управлять всеми тегами,\nкоторые использует приложение.");

		try {
			dialog.getDialogPane().setContent((BorderPane) loader.load());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		TagsDialogController tagsController = (TagsDialogController) loader.getController();
		tagsController.setService(service);
		tagsController.refresh();
		Optional<ButtonType> result = dialog.showAndWait();
		
		if(result.isPresent() && result.get() == ButtonType.OK) {
		}
	}
	
	@FXML
	private void onClickTagAdd() {
		if(diaryCurrent == null) {
			return;
		}
		
		List<Tags> tags = service.findAll(Tags.class);
		if(tags.size() > 0) {
			ChoiceDialog<Tags> dialog = new ChoiceDialog<Tags>(tags.get(0), tags);
			dialog.setTitle("Список тегов");
			dialog.setHeaderText("Выберите тег");
			Optional<Tags> result = dialog.showAndWait();
			if(result.isPresent())
			{
				diaryCurrent.getListTags().add(result.get());
				service.update(diaryCurrent);
				tagsList.getItems().clear();
				tagsList.getItems().addAll(diaryCurrent.getListTags());
			}
		}
	}

	@FXML
	private void onClickTagDelete() {
		if(diaryCurrent == null || tagCurrent == null) {
			return;
		}
		
		Alert dialog = new Alert(AlertType.CONFIRMATION);
		dialog.setTitle("Удалить тег");
		dialog.setHeaderText("Удалить тег: " + tagCurrent.getTitle() + "?");
		Optional<ButtonType> response = dialog.showAndWait();

		if (response.isPresent()) {
			diaryCurrent.getListTags().remove(tagCurrent);
			service.update(diaryCurrent);
			tagsList.getItems().remove(tagCurrent);			
		}
	}
	
	@FXML
	private void onClickFilter() {
		Tags tag = tagsCombo.getSelectionModel().getSelectedItem();
		if(tagsCombo.getSelectionModel().getSelectedIndex() == 0) {
			refresh();
		}
		else {
			datesList.getItems().clear();
			datesList.getItems().addAll(tag.getListDiaries());
		}
		cleanView();
	}
	
	public DAOService getService() {
		return service;
	}

	public void setService(DAOService service) {
		this.service = service;
	}
	
	private void cleanView() {
		String textHTML = "<html><body></body></html>";
		textEditor.setText(textHTML);
		webView.getEngine().loadContent(textHTML);
		htmlEditor.setHtmlText(textHTML);
		statusLabel.setText("");
		datesList.getSelectionModel().clearSelection();		
	}
}
