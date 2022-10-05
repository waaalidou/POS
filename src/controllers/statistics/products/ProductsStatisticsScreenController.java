package controllers.statistics.products;


import dao.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProductsStatisticsScreenController implements Initializable {
    @FXML
    private PieChart ProduitParCategorie;
    @FXML
    private BarChart NbrBarchart;
    private ObservableList<PieChart.Data> productsData = FXCollections.observableArrayList();
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<Integer> quantite = new ArrayList<>();
    private Connection connection;
    private ResultSet rs;
    private XYChart.Series dataseries1 = new XYChart.Series();
    @FXML
    private Label totalLabel;
    private int total;
    @FXML
    CategoryAxis xAxis;
    @FXML
    NumberAxis yAxis;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ProduitCategory();
        ProduitParCategorie.setData(productsData);
        barchart();
        totalLabel();
    }

    private void ProduitCategory() {
        connection = DBConnection.getConnection();

       String sql = "SELECT category.nom AS nom, COUNT(produit.code_category) AS Total\n" +
               "FROM category,produit\n" +
               "WHERE category.code = produit.code_category\n" +
               "GROUP BY category.nom\n";
        try {
            rs = connection.createStatement().executeQuery(sql);

            while (rs.next()) {
                productsData.add(new PieChart.Data(rs.getString(1),rs.getInt(2)));
            }
            rs.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
    private void totalLabel() {
        connection = DBConnection.getConnection();
        String totalSql = "SELECT COUNT(id) AS Total FROM produit";
        try {
            rs = connection.createStatement().executeQuery(totalSql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
            rs.close();
            totalLabel.setText("Total des produits est : " + total);
        }catch (SQLException se) {
            se.printStackTrace();
        }
    }

    @FXML
    void goBackFunction(MouseEvent event) {
        Parent root ;
        Stage stage;
        try {
            root = FXMLLoader.load(getClass().getResource("/resources/views/products/ProductView.fxml"));
            Scene scene = new Scene(root);
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Produits");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void barchart() {
        connection = DBConnection.getConnection();
        String sql = "SELECT category.nom AS nom, COUNT(produit.code_category) AS Total\n" +
                "FROM category,produit\n" +
                "WHERE category.code = produit.code_category\n" +
                "GROUP BY category.nom\n";
        try {
            Statement preparedStatement = connection.createStatement();
            rs = preparedStatement.executeQuery(sql);
            while (rs.next()) {
                categories.add(rs.getString(1));
                quantite.add(rs.getInt(2));
            }
            rs.close();
            dataseries1.setName("Categorie");
            for (int i = 0; i < categories.size(); i++) {
                dataseries1.getData().add(new XYChart.Data(categories.get(i),Math.floor(quantite.get(i))));
            }
            NbrBarchart.getData().add(dataseries1);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
