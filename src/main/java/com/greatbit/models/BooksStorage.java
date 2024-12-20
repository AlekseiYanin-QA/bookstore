package com.greatbit.models;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BooksStorage {
    private static final Set<Book> books = new HashSet<>();

    static {
        books.add(new Book(
                UUID.randomUUID().toString(),
                "Чапаев и Пустота",
                "Виктор Пелевин",
                400)
        );
        books.add(new Book(
                UUID.randomUUID().toString(),
                "Колыбель для кошки",
                "Воннегут Курт",
                250)
        );
//        books.add(new Book("Богатый Папа, Бедный Папа", "Роберт Киосаки", 300));
//        books.add(new Book("Мифогенная любовь каст", "Пепперштейн Павел", 576));
    }
    public static Set<Book> getBooks() {
        return books;
    }


}
