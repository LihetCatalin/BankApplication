package view;

import javafx.scene.control.SelectionMode;
import model.Book;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.text.Text;

public class CustomerView {
    private Button goBackButton;
    private Button viewBooksButton;
    private Button addBooksToCartButton;
    private Button clearCartButton;
    private Button buyBooksButton;
    private TableView tableView;
    private Text shoppingCartText;
    private Text actionText;

    public CustomerView(Stage customerStage){
        customerStage.setTitle("Store for customers");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 720, 480);
        customerStage.setScene(scene);

        initializeSceneTitle(gridPane);

        initializeFields(gridPane);

        customerStage.show();
    }

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
    }

    private void initializeSceneTitle(GridPane gridPane){
        Text title = new Text("Welcome, customer!");
        title.setFont(Font.font("Tahome", FontWeight.EXTRA_BOLD, 20));
        gridPane.add(title, 0, 0, 1, 1);
    }

    private void initializeFields(GridPane gridPane){
        //goBackButton = new Button("Main Page");
        //gridPane.add(goBackButton, 0, 0, 1, 1);

        viewBooksButton = new Button("View Books");
        gridPane.add(viewBooksButton, 0, 1, 1, 1);

        addBooksToCartButton = new Button("Add books to cart");
        gridPane.add(addBooksToCartButton, 0, 2, 1, 1);

        clearCartButton = new Button("Clear shopping cart");
        gridPane.add(clearCartButton, 0, 10, 1, 1);

        buyBooksButton = new Button("Buy Books");
        gridPane.add(buyBooksButton, 0, 9, 1, 1);

        actionText = new Text();
        gridPane.add(actionText, 0, 5);

        Text cartText = new Text("Shopping cart:");
        gridPane.add(cartText, 0, 6);

        shoppingCartText = new Text();
        gridPane.add(shoppingCartText, 0, 7);

        initializeTable();

        gridPane.add(tableView, 1, 1, 5, 10);
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
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.getColumns().addAll(authorCol, titleCol, publishedDateCol,
                priceCol, stockCol);
    }

    public TableView getTableView(){
        return tableView;
    }

    public String getActionText(){
        return actionText.getText();
    }

    public void setActionText(String text){
        actionText.setText(text);
    }

    public void setShoppingCartText(String text){shoppingCartText.setText(text);}
    public String getShoppingCartText(){return shoppingCartText.getText();}
    public void addViewBooksButtonListener(EventHandler<ActionEvent> viewBooksButtonListener){
        viewBooksButton.setOnAction(viewBooksButtonListener);
    }

    public void addBuyBooksButtonListener(EventHandler<ActionEvent> buyBooksButtonListener){
        buyBooksButton.setOnAction(buyBooksButtonListener);
    }

    public void addAddBookToCartButtonListener(EventHandler<ActionEvent> addBookToCartButtonListener){
        addBooksToCartButton.setOnAction(addBookToCartButtonListener);
    }

    public void addClearCartButtonListener(EventHandler<ActionEvent> clearCartButtonListener){
        clearCartButton.setOnAction(clearCartButtonListener);
    }
}
