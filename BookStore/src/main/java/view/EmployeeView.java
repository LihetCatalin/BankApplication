package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Book;

public class EmployeeView {

    private Button createButton;
    private Button retrieveButton;
    private Button updateButton;
    private TextField updateField;
    private Button deleteButton;
    private Button sellBookButton;

    private TextField authorTextField;
    private TextField titleTextField;
    private TextField publishedDateTextField;
    private TextField priceTextField;
    private TextField stockTextField;

    private TableView tableView;

    public EmployeeView(Stage employeeStage){
        employeeStage.setTitle("Welcome, employee!");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 720, 480);
        employeeStage.setScene(scene);

        initializeSceneTitle(gridPane);

        initializeFields(gridPane);

        employeeStage.show();
    }

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
    }

    private void initializeSceneTitle(GridPane gridPane) {
        Text title = new Text("Welcome, employee!");
        title.setFont(Font.font("Tahome", FontWeight.EXTRA_BOLD, 20));
        gridPane.add(title, 0, 0, 2, 1);
    }

    private void initializeFields(GridPane gridPane){
        createButton = new Button("create book");
        gridPane.add(createButton, 1, 11, 1, 1);

        retrieveButton = new Button("retrieve books");
        gridPane.add(retrieveButton, 0, 2, 1, 1);

        updateButton = new Button("update book");
        gridPane.add(updateButton, 0, 3, 1, 1);

        deleteButton = new Button("delete book");
        gridPane.add(deleteButton, 0, 4, 1, 1);

        sellBookButton = new Button("sell book");
        gridPane.add(sellBookButton, 0, 5, 1, 1);

        initializeTable();
        gridPane.add(tableView, 2, 0, 5, 10);

        initializeTextFields(gridPane);
    }

    private void initializeTextFields(GridPane gridPane){
        authorTextField = new TextField();
        authorTextField.setPromptText("author");
        gridPane.add(authorTextField, 2, 11, 1, 1);
        titleTextField = new TextField();
        titleTextField.setPromptText("title");
        gridPane.add(titleTextField, 3, 11, 1, 1);
        publishedDateTextField = new TextField();
        publishedDateTextField.setPromptText("date");
        gridPane.add(publishedDateTextField, 4, 11, 1, 1);
        priceTextField = new TextField();
        priceTextField.setPromptText("price");
        gridPane.add(priceTextField, 5, 11, 1, 1);
        stockTextField = new TextField();
        stockTextField.setPromptText("stock");
        gridPane.add(stockTextField, 6, 11, 1, 1);

        updateField = new TextField();
        updateField.setPromptText("id");
        gridPane.add(updateField, 1, 3, 1, 1);
    }

    private void initializeTable(){
        TableColumn authorCol = new TableColumn("author");
        authorCol.setMinWidth(125);
        authorCol.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));

        TableColumn titleCol = new TableColumn("title");
        titleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));

        TableColumn publishedDateCol = new TableColumn("publishedDate");
        publishedDateCol.setCellValueFactory(new PropertyValueFactory<Book, String>("publishedDate"));

        TableColumn priceCol = new TableColumn("price");
        priceCol.setCellValueFactory(new PropertyValueFactory<Book, String>("price"));

        TableColumn stockCol = new TableColumn("stock");
        stockCol.setCellValueFactory(new PropertyValueFactory<Book, String>("stock"));

        tableView = new TableView<>();
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableView.getColumns().addAll(authorCol, titleCol, publishedDateCol,
                priceCol, stockCol);
    }

    public String getAuthorText() {
        return authorTextField.getText();
    }

    public String getTitleText() {
        return titleTextField.getText();
    }

    public String getPublishedDateText() {
        return publishedDateTextField.getText();
    }

    public String getPriceText() {
        return priceTextField.getText();
    }

    public String getStockText() {
        return stockTextField.getText();
    }

    public TableView getTableView(){
        return tableView;
    }

    public String getUpdateText(){
        return updateField.getText();
    }

    public void addCreateBookButtonListener(EventHandler<ActionEvent> createBookButtonListener){
        createButton.setOnAction(createBookButtonListener);
    }
    public void addRetrieveBooksButtonListener(EventHandler<ActionEvent> retrieveBooksButtonListener){
        retrieveButton.setOnAction(retrieveBooksButtonListener);
    }
    public void addUpdateBookButtonListener(EventHandler<ActionEvent> updateBookButtonListener){
        updateButton.setOnAction(updateBookButtonListener);
    }
    public void addDeleteBookButtonListener(EventHandler<ActionEvent> deleteBookButtonListener){
        deleteButton.setOnAction(deleteBookButtonListener);
    }
    public void addSellBookButtonListener(EventHandler<ActionEvent> sellBookButtonListener){
        sellBookButton.setOnAction(sellBookButtonListener);
    }
}
