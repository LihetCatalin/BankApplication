import database.DatabaseConnectionFactory;
import model.PhysicalBook;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Book;
import model.builder.BookBuilder;

import java.time.LocalDate;

import repository.BookRepository;
import repository.BookRepositoryCacheDecorator;
import repository.BookRepositoryMySQL;
import repository.Cache;

import static org.junit.jupiter.api.Assertions.*;

public class BookRepositoryMySQLTesting {
    private Book book = new BookBuilder(new PhysicalBook())
            .setId(1L)
            .setTitle("Harry Potter")
            .setAuthor("J.K. Rowling")
            .setPublishedDate(LocalDate.of(2010, 7, 3))
            .build();
    private static BookRepository bookRepository;

    @BeforeAll
    public static void setup(){
        bookRepository= new BookRepositoryCacheDecorator(
                new BookRepositoryMySQL(DatabaseConnectionFactory.getConnectionWrapper(true).getConnection()),
                new Cache<>());
    }
    @BeforeEach
    public void cleanUp(){
        bookRepository.removeAll();
    }
    @Test
    public void findAllTest(){
        assertEquals(0, bookRepository.findAll().size());
    }
    @Test
    public void findByIdTest(){
        bookRepository.save(book);
        assertEquals(bookRepository.findById(1L).get().toString(), book.toString());
    }

    @Test
    public void saveTest(){
        assertTrue(bookRepository.save(book));
    }
    @Test
    public void removeAllTest(){

    }

}
