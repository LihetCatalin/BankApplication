package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Book;
import repository.book.BookRepositoryCacheDecorator;
import service.book.BookService;
import view.CustomerView;

import java.util.List;

public class CustomerController {
    private final CustomerView customerView;
    private final BookService bookService;
    public CustomerController(CustomerView customerView, BookService bookService){
        this.customerView = customerView;
        this.bookService = bookService;

        this.customerView.addViewBooksButtonListener(new ViewBooksButtonListener());
        this.customerView.addBuyBooksButtonListener(new BuyBooksButtonListener());
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
            
        }
    }
}
