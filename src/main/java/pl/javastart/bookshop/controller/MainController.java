package pl.javastart.bookshop.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pl.javastart.bookshop.model.Book;

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
    private TableColumn<Book, Integer> categoryColumn;
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
    private TextField categoryField;
    @FXML
    private TextField delField;

    public ObservableList<Book> getBooksList() throws SQLException {
        ObservableList<Book> booksList = FXCollections.observableArrayList();
        Connection connection = getConnection();
        String query = "SELECT * FROM book";
        Statement statement;
        ResultSet rs;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            Book book;
            while (rs.next()) {
                book = new Book(rs.getInt("Id"),
                                rs.getString("Title"),
                                rs.getString("Author"),
                                rs.getInt("Price"),
                                rs.getInt("Category_id"));
                booksList.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return booksList;
    }



    public void insertButton() {
        String sql = "INSERT INTO book VALUES (?, ?, ?, ?, ?);";
        PreparedStatement statement = null;
        try {
            statement = getConnection().prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(idField.getText()));
            statement.setString(2, titleField.getText());
            statement.setString(3, authorField.getText());
            statement.setDouble(4, Double.parseDouble(priceField.getText()));
            statement.setInt(5, Integer.parseInt(categoryField.getText()));

            statement.execute();
            idField.setText("");
            titleField.setText("");
            authorField.setText("");
            priceField.setText("");
            categoryField.setText("");
            showBooks();
        } catch (SQLException e) {
            System.out.println("ERROR");
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("FAILED :(");
            alertError.setContentText(String.valueOf(e));
            alertError.show();

        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

    }

    public void updateButton() {
        String sql = "UPDATE book SET Title='"+titleField.getText()+"',Author='"+authorField.getText()+"',Price="+priceField.getText()+" WHERE ID="+idField.getText()+"";
        PreparedStatement statement = null;
        try {
            statement = getConnection().prepareStatement(sql);
            statement.execute(sql);
            showBooks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteButton() {
        String sql = "DELETE FROM book WHERE ID="+delField.getText()+"";
        PreparedStatement statement = null;
        try {
            statement = getConnection().prepareStatement(sql);
            statement.execute(sql);

            delField.setText("");
            showBooks();

            Alert alertSuccess = new Alert(Alert.AlertType.CONFIRMATION);
            alertSuccess.setTitle("SUCCESFUL :)");
            alertSuccess.setContentText("Succesful! The Book has been deleted from list :)");
            alertSuccess.show();
        } catch (SQLException e) {
            System.out.println("ERROR!");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(String.valueOf(e));
            alert.show();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            showBooks();
//            CREATE TABLE Category (
//                    id INT(11) PRIMARY KEY AUTO_INCREMENT,
//                    name VARCHAR(255)
//            );
//
//            CREATE TABLE Book (
//                    id INT(11) PRIMARY KEY AUTO_INCREMENT,
//                    title VARCHAR(255),
//                    author VARCHAR(255),
//                    price DECIMAL(10, 2),
//                    category_id INT,
//                    FOREIGN KEY (category_id) REFERENCES Category(id)
//            );
        } catch (SQLException e) {
            System.out.println("ERROR");
        }

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
    }

    public void showBooks() throws SQLException {
        ObservableList<Book> booksList = getBooksList();
        idColumn.setCellValueFactory(new PropertyValueFactory<Book, Integer>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Book, Integer>("price"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<Book, Integer>("categoryId"));

        tableView.setItems(booksList);
    }

    public void executeQuery(String query) throws SQLException {
        Connection connection = getConnection();
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/books?useSSL=false&serverTimezone=Europe/Warsaw", "root", "admin");
            return connection;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

