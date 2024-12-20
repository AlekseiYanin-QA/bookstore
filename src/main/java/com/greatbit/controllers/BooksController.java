package com.greatbit.controllers;

import com.greatbit.models.Book;
import org.springframework.ui.Model;
import com.greatbit.models.BooksStorage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Controller
public class BooksController {
    @GetMapping ("/")
    public String booksList(Model model) {
        model.addAttribute("books", BooksStorage.getBooks());
        return "books-list";
    }

    @GetMapping ("/create-form")
    public  String createForm(){
        return "create-form";
    }

    @PostMapping("/create")
    public String create(Book book) {
        book.setId(UUID.randomUUID().toString());
        BooksStorage.getBooks().add(book);
        return "redirect:/";
    }
    // Это удаление, но для использования DeleteMapping нужно подключать JS,
    // но это не в рамках данного урока
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id) {
        Book bookToDelete = BooksStorage.getBooks().stream().
                filter(book -> book.getId().equals(id)).
                findFirst().
                orElseThrow(RuntimeException::new);
        BooksStorage.getBooks().remove(bookToDelete);
        return "redirect:/";
    }
    // Эта ручка на передачу в edit-form для update
    @GetMapping("/edit-form/{id}")
    public String editForm(@PathVariable("id") String id, Model model) {
        Book bookToEdit = BooksStorage.getBooks().stream().
                filter(book -> book.getId().equals(id)).
                findFirst().
                orElseThrow(RuntimeException::new);
        model.addAttribute("book", bookToEdit);
        return "edit-form";
    }

    @PostMapping("/update")
    public String update(Book book) {
        delete(book.getId());
        BooksStorage.getBooks().add(book);
        return "redirect:/";
    }

}
