

import database.DatabaseConnectionFactory;
import model.AudioBook;
import model.Book;
import model.EBook;
import model.PhysicalBook;
import model.builder.BookBuilder;
import repository.book.BookRepository;
import repository.book.BookRepositoryCacheDecorator;
import repository.book.BookRepositoryMySQL;
import repository.book.Cache;
import service.BookService;
import service.BookServiceImpl;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        BookRepository bookRepository = new BookRepositoryCacheDecorator(
                new BookRepositoryMySQL(DatabaseConnectionFactory.getConnectionWrapper(true).getConnection()),
                new Cache<>()
        );

        BookService bookService = new BookServiceImpl(bookRepository);

        Book book = new BookBuilder(new PhysicalBook())
                .setTitle("Harry Potter")
                .setAuthor("J.K. Rowling")
                .setPublishedDate(LocalDate.of(2010, 7, 3))
                .setFormat("pdf")
                .setRunTime(200)
                .build();

        Book eBook = new BookBuilder(new EBook())
                .setTitle("Harry Potter")
                .setAuthor("J.K. Rowling")
                .setPublishedDate(LocalDate.of(2010, 7, 3))
                .setFormat("pdf")
                .setRunTime(200)
                .build();

        Book audioBook = new BookBuilder(new AudioBook())
                .setTitle("Harry Potter")
                .setAuthor("J.K. Rowling")
                .setPublishedDate(LocalDate.of(2010, 7, 3))
                .setFormat("pdf")
                .setRunTime(200)
                .build();

        System.out.println(book);
        bookRepository.removeAll();  //nu am removeAll in bookService, nu stiu daca trebuie adaugat

        bookService.save(book);
        bookService.save(eBook);
        bookService.save(audioBook);

        System.out.println(bookService.findAll());
        System.out.println(bookService.findById(1L));
        System.out.println(bookService.getAgeOfBook(1L));

        //System.out.println(bookService.findAll());
        //System.out.println(bookService.findAll());
    }
}