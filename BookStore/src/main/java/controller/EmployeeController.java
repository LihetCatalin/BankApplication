package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Font;
import model.Book;
import model.PhysicalBook;
import model.builder.BookBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import service.book.BookService;
import view.EmployeeView;
import org.apache.pdfbox.pdmodel.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class EmployeeController {
    private final EmployeeView employeeView;
    private final BookService bookService;

    public EmployeeController(EmployeeView employeeView, BookService bookService){
        this.employeeView = employeeView;
        this.bookService = bookService;

        this.employeeView.addCreateBookButtonListener(new CreateBookButtonListener());
        this.employeeView.addRetrieveBooksButtonListener(new RetrieveBooksButtonListener());
        this.employeeView.addUpdateBookButtonListener(new UpdateBookButtonListener());
        this.employeeView.addDeleteBookButtonListener(new DeleteBookButtonListener());
        this.employeeView.addSellBookButtonListener(new SellBookButtonListener());
    }

    private class CreateBookButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            Book newBook = new BookBuilder(new PhysicalBook())
                    .setAuthor(employeeView.getAuthorText())
                    .setTitle(employeeView.getTitleText())
                    .setPublishedDate(LocalDate.of(2000, Month.JANUARY, 1))
                    .setPrice(Integer.parseInt(employeeView.getPriceText()))
                    .setStock(Integer.parseInt(employeeView.getStockText()))
                    .build();

            bookService.save(newBook);
            //employeeView.getTableView().getItems().add(newBook);
        }
    }

    private class RetrieveBooksButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            //employeeView.getTableView().getItems().clear();
            List<Book> books = bookService.findAll();
            ObservableList<Book> data = FXCollections.observableList(books);
            employeeView.getTableView().setItems(data);
        }
    }

    private class UpdateBookButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            Book bookToUpdate = (Book) employeeView.getTableView().getSelectionModel().getSelectedItem();
            Book updatedBook = new BookBuilder(new PhysicalBook())
                    .setAuthor(employeeView.getAuthorText())
                    .setTitle(employeeView.getTitleText())
                    .setPublishedDate(LocalDate.of(2000, Month.JANUARY, 1))
                    .setPrice(Integer.parseInt(employeeView.getPriceText()))
                    .setStock(Integer.parseInt(employeeView.getStockText()))
                    .build();

            bookService.updateBook(bookToUpdate.getId(), updatedBook);
            List<Book> books = bookService.findAll();
            ObservableList<Book> data = FXCollections.observableList(books);
            employeeView.getTableView().setItems(data);
        }
    }

    private class DeleteBookButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            Book selectedBook = (Book) employeeView.getTableView()
                    .getSelectionModel()
                    .getSelectedItem();

            employeeView.getTableView().getItems().remove(selectedBook);
            bookService.deleteBook(selectedBook);
        }
    }

    private class SellBookButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            Book selectedBook = (Book) employeeView.getTableView().getSelectionModel()
                    .getSelectedItem();
            bookService.updateStock(selectedBook, 1);
            employeeView.getTableView().refresh();

            PDDocument doc = new PDDocument();
            PDPage page1 = new PDPage();
            doc.addPage(page1);
            try {
                PDPageContentStream contentStream = new PDPageContentStream(doc, page1);
                contentStream.beginText();
                contentStream.newLineAtOffset(20, 450);
                contentStream.setFont(PDType1Font.TIMES_ROMAN , 20);
                contentStream.showText("AJUTOR");
                contentStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                doc.save("C:/documente de pe spatiul de lucru/" +
                        "facultate/anul 3/semestrul 1/IS/BookStoreApplication/soldBooks");
                System.out.println("PDF TIME");
                doc.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
