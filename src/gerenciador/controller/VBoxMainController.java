package gerenciador.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

public class VBoxMainController implements Initializable {

	@FXML
	private MenuItem menuItemCadastrosClientes;

	@FXML
	private MenuItem menuItemProcessosVendas;

	@FXML
	private MenuItem menuItemGraficosVendasPorMes;

	@FXML
	private MenuItem menuItemRelatoriosQuantidadeProdutos;

	@FXML
	private AnchorPane anchorPane;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}

	@FXML
	public void handleMenuItemCadastrosClientes() throws IOException {
		AnchorPane a = (AnchorPane) FXMLLoader
				.load(getClass().getResource("/gerenciador/view/FXMLAnchorPaneCadastrosClientes.fxml"));
		anchorPane.getChildren().setAll(a);
	}

	@FXML
	public void handleMenuItemProcessosVendas() throws IOException {
		AnchorPane a = (AnchorPane) FXMLLoader
				.load(getClass().getResource("/gerenciador/view/FXMLAnchorPaneProcessoVendas.fxml"));
		anchorPane.getChildren().setAll(a);
	}

	@FXML
	public void handleMenuItemGraficosVendasPorMes() throws IOException {
		AnchorPane a = (AnchorPane) FXMLLoader
				.load(getClass().getResource("/gerenciador/view/FXMLAnchorPaneGraficoPorMes.fxml"));
		anchorPane.getChildren().setAll(a);
	}

    
    @FXML
    public void handleMenuItemRelatoriosQuantidadeProdutos() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/gerenciador/view/FXMLAnchorPaneRelatoriosQuantidadeProdutos.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemCadastrosCategoria() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/gerenciador/view/FXMLAnchorPaneCadastrosCategorias.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemCadastrosProduto() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/gerenciador/view/FXMLAnchorPaneCadastrosProdutos.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemCadastrosUsuarios() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/gerenciador/view/FXMLAnchorPaneCadastrosUsuarios.fxml"));
        anchorPane.getChildren().setAll(a);
    }

	
}
