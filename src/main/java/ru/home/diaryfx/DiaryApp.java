package ru.home.diaryfx;

import java.io.IOException;

import ru.home.diaryfx.controllers.DiaryController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DiaryApp extends Application
{
	private FXMLLoader loader;
	
	public static void main(String args[]) { 
		launch(args);      
	} 
	
	@Override
	public void start(Stage stage) {
		try {
			loader = new FXMLLoader(getClass().getClassLoader().getResource("mainframe.fxml"));
			Parent root = (Parent) loader.load();
		    Scene scene = new Scene(root);
		    stage.setScene(scene);
		    stage.setTitle("Diary FX");
		    stage.setMaximized(true);
		    stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop()
	{
		DiaryController controller = (DiaryController) loader.getController();
		controller.dispose();
		try {
			super.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}