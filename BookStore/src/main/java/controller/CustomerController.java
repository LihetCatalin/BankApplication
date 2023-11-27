package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Book;
import service.book.BookService;
import view.CustomerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerController {
    private final CustomerView customerView;
    private final BookService bookService;
    private HashMap<Book,Integer> shoppingList = new HashMap<>();

    public CustomerController(CustomerView customerView, BookService bookService){
        this.customerView = customerView;
        this.bookService = bookService;

        this.customerView.addViewBooksButtonListener(new ViewBooksButtonListener());
        this.customerView.addBuyBooksButtonListener(new BuyBooksButtonListener());
        this.customerView.addAddBookToCartButtonListener(new AddBooksToCartButtonListener());
        this.customerView.addClearCartButtonListener(new ClearCartButtonListener());
    }

    private class ViewBooksButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            List<Book> books = bookService.findAll();
            customerView.setActionText("Listed all the books we have!");
            final ObservableList<Book> data = FXCollections.observableList(books);
            customerView.getTableView().setItems(data);
            //customerView.getTableView().refresh();
        }
    }

    private class BuyBooksButtonListener implements  EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            /*List<Book> boughtBooks = customerView.getTableView()
                    .getSelectionModel()
                    .getSelectedItems();*/

            StringBuilder msg = new StringBuilder();

            for(Map.Entry<Book, Integer> book : shoppingList.entrySet()){
                if(bookService.updateStock(book.getKey(), book.getValue().intValue())){
                    msg.append("Bought " + book.getValue() + " " + book.getKey().getTitle() + "\n");
                }
                else {
                    msg.append("Invalid action for book " + book.getKey().getTitle() + "\n");
                }
            }
            customerView.setActionText(msg.toString());
            shoppingList.clear();
            customerView.setShoppingCartText("");
            customerView.getTableView().refresh();
        }
    }

    private class AddBooksToCartButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            List<Book> selectedBooks = customerView.getTableView()
                    .getSelectionModel()
                    .getSelectedItems();

            StringBuilder cartStr = new StringBuilder();
            customerView.setShoppingCartText("");

            for(Book b:selectedBooks){
                if(shoppingList.get(b) != null){
                    shoppingList.put(b, shoppingList.get(b) + 1);
                }
                else shoppingList.put(b, 1);
            }

            for(Map.Entry<Book, Integer> book : shoppingList.entrySet()){
                cartStr.append(book.getKey().getTitle() + " " + book.getValue() + "\n");
            }
            customerView.setShoppingCartText(customerView.getShoppingCartText() + cartStr);
        }
    }

    private class ClearCartButtonListener implements  EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            shoppingList.clear();
            customerView.setShoppingCartText("");
        }
    }
}
