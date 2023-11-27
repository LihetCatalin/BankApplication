package model.builder;
import model.AudioBook;
import model.Book;
import model.EBook;
import model.PhysicalBook;

import java.time.LocalDate;

public class BookBuilder implements BookBuilder_I{
    private Book book;

    public BookBuilder(Book book){
        this.book = book;
    }

    public BookBuilder setId(Long id){
        book.setId(id);
        return this;
    }

    public BookBuilder setAuthor(String author){
        book.setAuthor(author);
        return this;
    }

    public BookBuilder setTitle(String title){
        book.setTitle(title);
        return this;
    }

    public BookBuilder setPublishedDate(LocalDate publishedDate){
        book.setPublishedDate(publishedDate);
        return this;
    }

    public BookBuilder setPrice(int price){
        book.setPrice(price);
        return this;
    }

    public BookBuilder setStock(int stock){
        book.setStock(stock);
        return this;
    }

    public BookBuilder setFormat(String format){
        if(book instanceof EBook){
            ((EBook) book).setFormat(format);
        }
        return this;
    }

    public BookBuilder setRunTime(int runTime){
        if(book instanceof AudioBook){
            ((AudioBook) book).setRunTime(runTime);
        }
        return this;
    }

    public Book build(){
        return book;
    }
}
