package gerenciador.controller;

import java.net.URL;
import java.util.ResourceBundle;

import gerenciador.model.domain.Cliente;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AnchorPaneCadastrosClientesDialogController implements Initializable {

	@FXML
	private TextField textFieldClienteNome;

	@FXML
	private TextField textFieldClienteCPF;

	@FXML
	private TextField textFieldClienteTelefone;

	@FXML
	private Button buttonConfirmar;

	@FXML
	private Button buttonCancelar;

	private Stage dialogStage;
	private boolean buttonConfirmarClicked = false;
	private Cliente cliente;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

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

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
		this.textFieldClienteNome.setText(cliente.getNome());
		this.textFieldClienteCPF.setText(cliente.getCpf());
		this.textFieldClienteTelefone.setText(cliente.getTelefone());
	}

	public void handleButtonConfirmar() {
		
		if(validarEntradaDeDados()) {
			cliente.setNome(textFieldClienteNome.getText());
			cliente.setCpf(textFieldClienteCPF.getText());
			cliente.setTelefone(textFieldClienteTelefone.getText());

			buttonConfirmarClicked = true;
			dialogStage.close();
		}		
	}

	public void handleButtonCancelar() {
		dialogStage.close();
	}

	
	private boolean validarEntradaDeDados() {
		String errorMessage = "";

		if (textFieldClienteNome.getText() == null || textFieldClienteNome.getText().length() == 0) {
			errorMessage += "Nome inválido!\n";
		}
		if (textFieldClienteCPF.getText() == null || textFieldClienteCPF.getText().length() == 0) {
			errorMessage += "CPF inválido!\n";
		}
		if (textFieldClienteTelefone.getText() == null || textFieldClienteTelefone.getText().length() == 0) {
			errorMessage += "Telefone inválido!\n";
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
