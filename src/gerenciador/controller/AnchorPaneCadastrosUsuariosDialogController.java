package gerenciador.controller;

import java.net.URL;
import java.util.ResourceBundle;

import gerenciador.model.domain.Cliente;
import gerenciador.model.domain.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AnchorPaneCadastrosUsuariosDialogController implements Initializable {
	
	@FXML
    private TextField textFieldCadastrosUsuariosDialogNome;

    @FXML
    private TextField textFieldCadastrosUsuariosDialogLogin;

    @FXML
    private TextField textFieldCadastrosUsuariosDialogSenha;

    @FXML
    private TextField textFieldCadastrosUsuariosDialogEmail;
    
    private Stage dialogStage;
	private boolean buttonConfirmarClicked = false;
	private Usuario usuario;
    
	
	
	public Stage getDialogStage() {
		return dialogStage;
	}


	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}


	public boolean isButtonConfirmarClicked() {
		return buttonConfirmarClicked;
	}


	public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
		this.buttonConfirmarClicked = buttonConfirmarClicked;
	}


	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;		
		this.textFieldCadastrosUsuariosDialogNome.setText(usuario.getNome());
		this.textFieldCadastrosUsuariosDialogLogin.setText(usuario.getLogin());
		this.textFieldCadastrosUsuariosDialogSenha.setText(usuario.getSenha());
		this.textFieldCadastrosUsuariosDialogEmail.setText(usuario.getEmail());
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}


    @FXML
    void handleButtonCancelar() {
    	dialogStage.close();
    }

    @FXML
    void handleButtonConfirmar() {
    	if(validarEntradaDeDados()) {
			usuario.setNome(textFieldCadastrosUsuariosDialogNome.getText());
			usuario.setLogin(textFieldCadastrosUsuariosDialogLogin.getText());
			usuario.setSenha(textFieldCadastrosUsuariosDialogSenha.getText());
			usuario.setEmail(textFieldCadastrosUsuariosDialogEmail.getText());

			buttonConfirmarClicked = true;
			dialogStage.close();
		}	

    }
    

 	private boolean validarEntradaDeDados() {
 		String errorMessage = "";

 		if (textFieldCadastrosUsuariosDialogNome.getText() == null || textFieldCadastrosUsuariosDialogNome.getText().length() == 0) {
 			errorMessage += "Nome inválido!\n";
 		}
 		if (textFieldCadastrosUsuariosDialogLogin.getText() == null || textFieldCadastrosUsuariosDialogLogin.getText().length() == 0) {
 			errorMessage += "Login inválido!\n";
 		}
 		if (textFieldCadastrosUsuariosDialogSenha.getText() == null || textFieldCadastrosUsuariosDialogSenha.getText().length() == 0) {
 			errorMessage += "Senha inválido!\n";
 		}
 		if (textFieldCadastrosUsuariosDialogEmail.getText() == null || textFieldCadastrosUsuariosDialogEmail.getText().length() == 0) {
 			errorMessage += "Email inválido!\n";
 		}

 		if (errorMessage.length() == 0) {
 			return true;
 		} else {
 			Alert alert = new Alert(Alert.AlertType.ERROR);
 			alert.setTitle("Erro no cadastro");
 			alert.setHeaderText("Campos inválidos, por favor, corrija...");
 			alert.setContentText(errorMessage);
 			alert.show();
 			return false;
 		}
 	}


}
