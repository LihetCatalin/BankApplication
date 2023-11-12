package model.builder;

import java.time.LocalDate;
import model.Book;

public interface BookBuilder_I {
    public BookBuilder_I setId(Long id);
    public BookBuilder_I setAuthor(String author);
    public BookBuilder_I setTitle(String title);
    public BookBuilder_I setPublishedDate(LocalDate publishedDate);
    public Book build();
}
