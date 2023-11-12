package repository;

import model.Book;

import java.util.List;
import java.util.Optional;

public abstract class BookRepositoryDecorator implements BookRepository{

    protected BookRepository decoratedRepository;
    public BookRepositoryDecorator(BookRepository bookRepository){
        this.decoratedRepository = bookRepository;
    }
}
