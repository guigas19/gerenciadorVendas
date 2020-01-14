package gerenciador.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.ResourceBundle;

import gerenciador.model.dao.VendaDAO;
import gerenciador.model.database.Database;
import gerenciador.model.database.DatabaseFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class AnchorPaneGraficoPorMesController implements Initializable {

	@FXML
    private BarChart<String, Integer> barChart;

    @FXML
    private CategoryAxis categoryAxis;

    @FXML
    private NumberAxis numberAxis;
    
    private ObservableList<String> observableListMeses = FXCollections.observableArrayList();

 
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final VendaDAO vendaDAO = new VendaDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        String[] arrayMeses = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
   
        observableListMeses.addAll(Arrays.asList(arrayMeses));


        categoryAxis.setCategories(observableListMeses);
        
        vendaDAO.setConnection(connection);
        Map<Integer, ArrayList> dados = vendaDAO.listarQuantidadeVendasPorMes();

        for (Map.Entry<Integer, ArrayList> dadosItem : dados.entrySet()) {
            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            series.setName(dadosItem.getKey().toString());

            for (int i = 0; i < dadosItem.getValue().size(); i = i + 2) {
                String mes;
                Integer quantidade;

                mes = retornaNomeMes((int) dadosItem.getValue().get(i));
                quantidade = (Integer) dadosItem.getValue().get(i + 1);

                series.getData().add(new XYChart.Data<>(mes, quantidade));
            }
            barChart.getData().add(series);
        }

    }

    public String retornaNomeMes(int mes) {
        switch (mes) {
            case 1:
                return "Jan";
            case 2:
                return "Fev";
            case 3:
                return "Mar";
            case 4:
                return "Abr";
            case 5:
                return "Mai";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Ago";
            case 9:
                return "Set";
            case 10:
                return "Out";
            case 11:
                return "Nov";
            case 12:
                return "Dez";
            default:
                return "";
        }
    }

}
