package repository;

import model.AudioBook;
import model.Book;
import model.EBook;
import model.PhysicalBook;
import model.builder.BookBuilder;

import java.sql.*;
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
        //Book book = new Book();

        String sql = " SELECT * FROM book" +
                " WHERE id = ?";

        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
                return Optional.ofNullable(getBookFromResultSet(resultSet));


        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean save(Book book) {
        String sql = " INSERT INTO book (title, author, publishedDate, bookType, format, runTime)" +
                " VALUES (?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setDate(3, Date.valueOf(book.getPublishedDate()));

            if (book instanceof AudioBook) {
                statement.setString(4, "audioBook");
                statement.setNull(5, Types.VARCHAR);
                statement.setInt(6, ((AudioBook) book).getRunTime());
            } else if (book instanceof EBook) {
                statement.setString(4, "eBook");
                statement.setString(5, ((EBook) book).getFormat());
                statement.setNull(6, Types.INTEGER);
            } else {
                statement.setString(4, "normal book");
                statement.setNull(5, Types.VARCHAR);
                statement.setNull(6, Types.INTEGER);
            }
            statement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void removeAll() {
        String sql = " DELETE FROM book;";
        String rst_inc = " ALTER TABLE book AUTO_INCREMENT = 1";
        try{
            Statement statement = connection.createStatement();

            statement.executeUpdate(sql);
            statement.executeUpdate(rst_inc);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException{
        String type = resultSet.getString("bookType");
        Book book = null;
        switch (type){
            case "eBook":
                book = new EBook();
                break;
            case "audioBook":
                book = new AudioBook();
                break;
            default:
                book = new PhysicalBook();
        }
        return new BookBuilder(book)
                .setId(resultSet.getLong("id"))
                .setTitle(resultSet.getString("title"))
                .setAuthor(resultSet.getString("author"))
                .setPublishedDate(new java.sql.Date(resultSet.getDate("publishedDate").getTime()).toLocalDate())
                .setFormat(resultSet.getString("format"))
                .setRunTime(resultSet.getInt("runTime"))
                .build();
    }
}
