package gerenciador;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

	private static Stage stage;
	private static Scene mainVBox;
	private static Scene login;

	@Override
	public void start(Stage primaryStage) throws Exception {

		stage = primaryStage;
		
		Image icone = new Image("/gerenciador/imagens/icone.jpg");

        stage.getIcons().add(icone);

		primaryStage.setTitle("Gerenciador de Vendas");

		Parent FXMLLogin = FXMLLoader.load(getClass().getResource("/gerenciador/view/FXMLAnchorPaneLogin.fxml"));
		login = new Scene(FXMLLogin);

		Parent FXMLMainVBox = FXMLLoader.load(getClass().getResource("/gerenciador/view/FXMLVBoxMain.fxml"));
		mainVBox = new Scene(FXMLMainVBox);

		primaryStage.setScene(login);
		stage.setResizable(false);
		primaryStage.show();
	}

	//Método para mudar as telas
	public static void changeScreen(String srg) {
		switch (srg) {
		case "VBoxMain":
			stage.setScene(mainVBox);			
			break;	
		case "login":
			stage.setScene(login);			
			break;		
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}