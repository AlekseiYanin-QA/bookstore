package com.greatbit.dao;

import com.greatbit.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class BookDaoImpl implements BookDao {
    private final DataSource dataSource;


    @Autowired
    public BookDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Book> findAll() {
        final String selectSql = "SELECT book_id, name, author, pages FROM book";
        List<Book> books = new ArrayList<>();
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(selectSql);
        ) {
            while (rs.next()) {
                Book book = new Book(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getInt(4));
                books.add(book);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }
        if (Objects.isNull(book.getName()) || Objects.isNull(book.getAuthor())) {
            throw new IllegalArgumentException("Book name or author cannot be null.");
        }
        String insertSql = "INSERT INTO book (name, author, pages) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, book.getName());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setInt(3, book.getPages());
            preparedStatement.executeUpdate();
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    book.setId(rs.getString(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return book;
    }

    @Override
    public Book getById(String bookId) {
        final String getByIdSql = "SELECT book_id, name, author, pages FROM book WHERE book_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getByIdSql)) {
            preparedStatement.setString(1, bookId);  // Set the parameter as a String

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (!rs.next()) {
                    throw new RuntimeException(String.format("Book with id %s was not found", bookId));
                }
                return new Book(rs.getString("book_id"), rs.getString("name"),
                        rs.getString("author"), rs.getInt("pages"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while retrieving the book", e);
        }
    }

    @Override
    public Book update(Book book) {
        if (Objects.isNull(book.getId())) {
            throw new RuntimeException("Обновление не выполнено: книга с ID " + book.getId() + " не найдена.");
        }
        final String updateSql = "UPDATE book SET name = ?, author = ?, pages = ? WHERE book_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {
            preparedStatement.setString(1, book.getName());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setInt(3, book.getPages());
            preparedStatement.setString(4, book.getId());
             preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return book;
    }

    @Override
    public void delete(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Cannot delete null book");
        }

        final String deleteByIdSql = "DELETE FROM book WHERE book_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteByIdSql)) {
            preparedStatement.setString(1, book.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll() {
        String deleteSql = "TRUNCATE TABLE book"; //DELETE * FROM book
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
                statement.executeUpdate(deleteSql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
