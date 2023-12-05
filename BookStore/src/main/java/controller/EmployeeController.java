package controller;

import model.Book;
import service.book.BookService;
import view.EmployeeView;

public class EmployeeController {
    private final EmployeeView employeeView;
    private final BookService bookService;

    public EmployeeController(EmployeeView employeeView, BookService bookService){
        this.employeeView = employeeView;
        this.bookService = bookService;
    }
}
