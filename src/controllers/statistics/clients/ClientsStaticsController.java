package controllers.statistics.clients;


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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ClientsStaticsController implements Initializable {
    @FXML
    private PieChart ClientParAbonement;
    @FXML
    private BarChart ClientAbonement;
    @FXML
    private PieChart ClientParWilaya;
    @FXML
    private BarChart ClientWilaya;
    private ObservableList<PieChart.Data> AbonementData = FXCollections.observableArrayList();
    private ObservableList<PieChart.Data> WilayaData = FXCollections.observableArrayList();
    private ArrayList<String> client = new ArrayList<>();
    private ArrayList<Integer> nomber = new ArrayList<>();
    private ArrayList<String> Wilaya = new ArrayList<>();
    private ArrayList<Integer> nomber2 = new ArrayList<>();
    private Connection connection;
    private Statement preparedStatement;
    private ResultSet rs;
    private XYChart.Series dataseries1 = new XYChart.Series();
    private XYChart.Series dataseries2 = new XYChart.Series();

    @FXML
    void goBackFunction(MouseEvent event) {
        Parent root ;
        Stage stage;
        try {
            root = FXMLLoader.load(getClass().getResource("/resources/views/clients/ClientScreen.fxml"));
            Scene scene = new Scene(root);
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Clients");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void ClientAbonement() {
        connection = DBConnection.getConnection();

        String sql = "SELECT abonement.nom AS nom, COUNT(CLIENT.code_abonement) AS Total\n" +
                "FROM abonement,CLIENT\n" +
                "WHERE abonement.code = CLIENT.code_abonement\n" +
                "GROUP BY abonement.nom\n";
        try {
            rs = connection.createStatement().executeQuery(sql);
            while (rs.next()) {
                AbonementData.add(new PieChart.Data(rs.getString(1),rs.getInt(2)));
            }
            rs.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
    private void ClientWilaya() {
        connection = DBConnection.getConnection();

        String sql = "SELECT CLIENT\n" +
                "    .Adress AS Wilaya,\n" +
                "    COUNT(CLIENT.Adress) AS Nombre\n" +
                "FROM CLIENT\n" +
                "GROUP BY CLIENT\n" +
                "    .Adress";
        try {
            rs = connection.createStatement().executeQuery(sql);
            while (rs.next()) {
                WilayaData.add(new PieChart.Data(rs.getString(1),rs.getInt(2)));
            }
            rs.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
    private void barchart() {
        connection = DBConnection.getConnection();
        String sql = "SELECT abonement.nom AS nom, COUNT(CLIENT.code_abonement) AS Total\n" +
                "FROM abonement,CLIENT\n" +
                "WHERE abonement.code = CLIENT.code_abonement\n" +
                "GROUP BY abonement.nom\n";
        try {
            preparedStatement = connection.createStatement();
            rs = preparedStatement.executeQuery(sql);
            while (rs.next()) {
                client.add(rs.getString(1));
                nomber.add(rs.getInt(2));
            }
            rs.close();
            dataseries1.setName("Abonement");
            for (int i = 0; i < client.size(); i++) {
                dataseries1.getData().add(new XYChart.Data(client.get(i),Math.floor(nomber.get(i))));
            }
            ClientAbonement.getData().add(dataseries1);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
    private void barchartWilaya() {
        connection = DBConnection.getConnection();
        String sql = "SELECT CLIENT\n" +
                "    .Adress AS Wilaya,\n" +
                "    COUNT(CLIENT.Adress) AS Nombre\n" +
                "FROM CLIENT\n" +
                "GROUP BY CLIENT\n" +
                "    .Adress";
        try {
            preparedStatement = connection.createStatement();
            rs = preparedStatement.executeQuery(sql);
            while (rs.next()) {
                Wilaya.add(rs.getString(1));
                nomber2.add(rs.getInt(2));
            }
            rs.close();
            dataseries2.setName("Wilaya");
            for (int i = 0; i < Wilaya.size(); i++) {
                dataseries2.getData().add(new XYChart.Data(Wilaya.get(i),nomber2.get(i)));
            }
            ClientWilaya.getData().add(dataseries2);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ClientAbonement();
        ClientParAbonement.setData(AbonementData);
        ClientWilaya();
        ClientParWilaya.setData(WilayaData);
        barchart();
        barchartWilaya();
    }


}
