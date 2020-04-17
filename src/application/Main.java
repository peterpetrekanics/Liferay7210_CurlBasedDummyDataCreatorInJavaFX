package application;

import java.net.Socket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane) FXMLLoader
					.load(getClass().getResource("Liferay7210_CurlBasedDummyDataCreatorInJavaFX.fxml"));
			Scene scene = new Scene(root, 900, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			if(!testIfLocalhost8080IsAvailable()) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("Please check if localhost:8080 is available");
				alert.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	private boolean testIfLocalhost8080IsAvailable() {
		Socket s = null;
		String host = "localhost";
		int port = 8080;

		try	{
			s = new Socket(host, port);
			return true;
		}
		catch (Exception e)	{
			return false;
		}
		finally	{
			if(s != null) try {
				s.close();
			}	catch(Exception e){}
		}
	}
}
