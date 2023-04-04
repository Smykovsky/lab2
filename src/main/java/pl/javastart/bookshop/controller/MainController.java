package pl.javastart.bookshop.controller;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Window;
import pl.javastart.bookshop.MySql.MySql;
import pl.javastart.bookshop.model.Book;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Button btnadd;

    @FXML
    private TableColumn<Book, Integer> idColumn;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, Integer> priceColumn;

    @FXML
    private TableView<Book> tableView;

    @FXML
    private TextField idField;

    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField delField;

    final MySql mySql = new MySql("localhost", 3306, "books", "root", "admin", 4, 4);


    public ObservableList<Book> getBooksList() throws SQLException {
        ObservableList<Book> booksList = FXCollections.observableArrayList();
        Connection connection = mySql.getHikari().getConnection();
        String query = "SELECT * FROM book";
        Statement statement;
        ResultSet rs;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            Book book;
            while (rs.next()) {
                book = new Book(rs.getInt("Id"), rs.getString("Title"), rs.getString("Author"), rs.getInt("Price"));
                booksList.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return booksList;
    }



    public void insertButton() throws SQLException {
        String query = "insert into book values("+idField.getText()+",'"+titleField.getText()+"','"+authorField.getText()+"',"+priceField.getText()+")";

        if (idField.getText().isEmpty() || titleField.getText().isEmpty() || authorField.getText().isEmpty()) {
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("FAILED :(");
            alertError.setContentText("TextField like id, title and author can not be null!");
            alertError.show();
        } else {
            mySql.executeQuery(query);
            idField.setText("");
            titleField.setText("");
            authorField.setText("");
            priceField.setText("");
            showBooks();
        }
    }

    public void updateButton() throws SQLException {
        String query = "UPDATE book SET Title='"+titleField.getText()+"',Author='"+authorField.getText()+"',Price="+priceField.getText()+" WHERE ID="+idField.getText()+"";
        mySql.executeQuery(query);
        showBooks();
    }

    public void deleteButton() throws SQLException {
        String query = "DELETE FROM book WHERE ID="+delField.getText()+"";

        if (delField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("TextField can not be null. Please type us ID of book what you want delete!");
            alert.show();
        } else {
            mySql.executeQuery(query);
            delField.setText("");
            showBooks();
            Alert alertSuccess = new Alert(Alert.AlertType.CONFIRMATION);
            alertSuccess.setTitle("SUCCESFUL :)");
            alertSuccess.setContentText("Succesful! The Book has been deleted from list :)");
            alertSuccess.show();
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            showBooks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    public void showBooks() throws SQLException {
        ObservableList<Book> booksList = getBooksList();
        idColumn.setCellValueFactory(new PropertyValueFactory<Book, Integer>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Book, Integer>("price"));

        tableView.setItems(booksList);
    }
}

