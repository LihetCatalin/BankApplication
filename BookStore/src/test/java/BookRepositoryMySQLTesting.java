import database.DatabaseConnectionFactory;
import org.junit.jupiter.api.Test;
import model.Book;
import model.builder.BookBuilder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import repository.BookRepository;
import repository.BookRepositoryMySQL;

public class BookRepositoryMySQLTesting {
    private Book book = new BookBuilder()
            .setId(1L)
            .setTitle("Harry Potter")
            .setAuthor("J.K. Rowling")
            .setPublishedDate(LocalDate.of(2010, 7, 3))
            .build();
    private BookRepository bookRepository= new BookRepositoryMySQL(DatabaseConnectionFactory.getConnectionWrapper(true).getConnection());
    @Test
    public void findByIdTest(){
        assertEquals(bookRepository.findById(1L).get().toString(), book.toString());
    }

    @Test
    public void saveTest(){
        assertEquals(bookRepository.save(book), true);
    }

}
