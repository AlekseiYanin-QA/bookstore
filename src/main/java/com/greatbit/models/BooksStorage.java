package com.greatbit.models;

import java.util.ArrayList;
import java.util.List;

public class BooksStorage {
    private static  List<Book> books = new ArrayList<>();

    public static  List<Book> getBooks() {
        return books;
    }

}
