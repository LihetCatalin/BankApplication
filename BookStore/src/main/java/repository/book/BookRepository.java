package repository.book;
import model.Book;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> findAll();
    Optional<Book> findById(Long id);
    boolean save(Book book);
    boolean updateStock(Book book);
    void removeAll();
}
