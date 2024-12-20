package com.greatbit;

import com.greatbit.models.Book;
import com.greatbit.models.BooksStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application {


    public static void main(String[] args) {
        BooksStorage.getBooks().add(
                new Book("Учение Дона Хуана", "Виктор Пелевин", 400)
        );
        BooksStorage.getBooks().add(
                new Book("Богатый Папа, Бедный Папа", "Роберт Киосаки", 300)
        );
        BooksStorage.getBooks().add(
                new Book("Колыбель для кошки", "Воннегут Курт", 250)
        );
        BooksStorage.getBooks().add(
                new Book("Мифогенная любовь каст", "Пепперштейн Павел", 576)
        );

        SpringApplication.run(Application.class, args);
    }
}