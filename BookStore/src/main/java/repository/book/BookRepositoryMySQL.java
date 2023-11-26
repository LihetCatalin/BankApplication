package repository.book;

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
        String sql = " INSERT INTO book (title, author, publishedDate, price, stock, bookType, format, runTime)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setDate(3, Date.valueOf(book.getPublishedDate()));
            statement.setInt(4, book.getPrice());
            statement.setInt(5, book.getStock());

            if (book instanceof AudioBook) {
                statement.setString(6, "audioBook");
                statement.setNull(7, Types.VARCHAR);
                statement.setInt(8, ((AudioBook) book).getRunTime());
            } else if (book instanceof EBook) {
                statement.setString(6, "eBook");
                statement.setString(7, ((EBook) book).getFormat());
                statement.setNull(8, Types.INTEGER);
            } else {
                statement.setString(6, "normal book");
                statement.setNull(7, Types.VARCHAR);
                statement.setNull(8, Types.INTEGER);
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
                .setPrice(resultSet.getInt("price"))
                .setStock(resultSet.getInt("stock"))
                .setFormat(resultSet.getString("format"))
                .setRunTime(resultSet.getInt("runTime"))
                .build();
    }
}
