package repository;

import model.Book;
import model.builder.BookBuilder;

import java.sql.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMySQL implements BookRepository{
    private Connection connection;
    public BookRepositoryMySQL(Connection connection){
        this.connection = connection;   //dependency injection
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();

        String sql = " SELECT * FROM book;";
        try{
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){    //primul element din resultSet pointeaza spre metadate
                books.add(getBookFromResultSet(resultSet));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        Book book = new Book();

        String sql = " SELECT * FROM book" +
                " WHERE id = ?";

        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            book = getBookFromResultSet(resultSet);


        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.ofNullable(book);
    }

    @Override
    public boolean save(Book book) {
        String sql = " INSERT INTO book (title, author, publishedDate)" +
                " VALUES (?, ?, ?)";
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setDate(3, (java.sql.Date) Date.from(book.getPublishedDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void removeAll() {
        String sql = " DELETE FROM book;";
        try{
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException{
        return new BookBuilder()
                .setId(resultSet.getLong("id"))
                .setTitle(resultSet.getString("title"))
                .setAuthor(resultSet.getString("author"))
                .setPublishedDate(new java.sql.Date(resultSet.getDate("publishedDate").getTime()).toLocalDate())
                .build();
    }
}
