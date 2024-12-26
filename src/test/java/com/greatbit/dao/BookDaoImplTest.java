package com.greatbit.dao;

import com.greatbit.models.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(
    properties = {"jdbcUrl=jdbc:h2:mem:db;DB_CLOSE_DELAY=-1"}
)

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookDaoImplTest {
    @Autowired
    private BookDao bookDao;

    @BeforeEach
    public void beforeEach (){
        bookDao.deleteAll();
    }

    @Test
    public void saveSaveDataToDoAndReturnsEntityWithId(){
        Book book = bookDao.save(new Book("book name", "book author", 25));
        assertThat(book.getId()).isNotBlank();
        assertThat(bookDao.findAll()).extracting("id").containsExactly(book.getId());
    }

    @Test
    void deleteAllDeletesAllData() {
        bookDao.save(new Book("book name", "book author", 25));
        assertThat(bookDao.findAll()).isNotEmpty(); // Проверяем, что список не пустой
        bookDao.deleteAll();
        assertThat(bookDao.findAll()).isEmpty(); // Проверяем, что список пустой
    }
    @Test
    void findAllRuturnsAllBooks(){
        assertThat(bookDao.findAll()).isEmpty(); // Проверяем, что список пустой
        bookDao.save(new Book("book name", "book author", 25));
        assertThat(bookDao.findAll()).isNotEmpty(); // Проверяем, что список не пустой
    }

    @Test
    void getByIdThrowsRuntimeExceptionIfNoBookFound(){
        assertThatThrownBy(() -> bookDao.getById("1")).isInstanceOf(RuntimeException.class);
    }

    @Test
    void getByIdReturnsCorrectBooks() {
        // Сохраняем первую книгу и получаем её идентификатор
        Book book = bookDao.save(new Book("book name 1", "book author 1", 15));

        // Сохраняем другую книгу (она не будет использована в утверждении)
        bookDao.save(new Book("other book name 2", "other book author 2", 25));

        // Получаем книгу по ID и проверяем, что это та же книга, что и выше
        assertThat(bookDao.getById(book.getId()))
                .isNotNull() // ожидать, что книга не null
                .extracting("name")
                .isEqualTo(book.getName()); // удостоверяемся, что имя соответствует
    }

    @Test
    void updateUpdatesDataInDb(){
        Book book = bookDao.save(new Book("book name 1", "book author 1", 15));
        book.setName("new name");
        bookDao.update(book);
        assertThat(bookDao.getById(book.getId()).getName()).isEqualTo("new name");
    }
    @Test
    void updateThrowsExceptionUpdatingNotSaveBook(){
        assertThatThrownBy(() -> bookDao.update(new Book("book name 1", "book author 1", 15)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void deleteDeletesCorrectData() {
        Book bookToKeep = bookDao.save(new Book("book name 1", "book author 1", 15));
        Book bookToDelete = bookDao.save(new Book("other book name 2", "other book author 2", 25));
        bookDao.delete(bookToDelete);
        assertThat(bookDao.getById(bookToKeep.getId())).isNotNull();
        assertThatThrownBy(() -> bookDao.getById(bookToDelete.getId())).isInstanceOf(RuntimeException.class);
    }
    @Test
    void updateDoesNotChangeDataIfNoChangeIsMade() {
        Book book = bookDao.save(new Book("book name 1", "book author 1", 15));
        String originalName = book.getName();
        bookDao.update(book);
        assertThat(bookDao.getById(book.getId()).getName()).isEqualTo(originalName);
    }

    @Test
    void saveNullBookThrowsException() {
        assertThatThrownBy(() -> bookDao.save(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Book cannot be null.");
    }

    @Test
    void deleteNullBookThrowsException() {
        assertThatThrownBy(() -> bookDao.delete(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Cannot delete null book");
    }

    @Test
    void getByIdHandlesInvalidIdGracefully() {
        Book book = bookDao.save(new Book("book name", "book author", 25));
        assertThatThrownBy(() -> bookDao.getById("invalid-id"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error while retrieving the book");
    }
}