package com.greatbit.controllers;

import com.greatbit.dao.BookDao;
import com.greatbit.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BooksController {

    private final BookDao bookDao;

    @Autowired
    public BooksController(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @GetMapping ("/")
    public String booksList(Model model) {
        model.addAttribute("books", bookDao.findAll());
        return "books-list";
    }

    @GetMapping ("/create-form")
    public  String createForm(){
        return "create-form";
    }

    @PostMapping("/create")
    public String create(Book book) {
        bookDao.save(book);
        return "redirect:/";
    }
    // Это удаление, но для использования DeleteMapping нужно подключать JS,
    // но это не в рамках данного урока
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id) {
        Book bookToDelete = bookDao.getById(id);
        bookDao.delete(bookToDelete);
        return "redirect:/";
    }
    // Эта ручка на передачу в edit-form для update
    @GetMapping("/edit-form/{id}")
    public String editForm(@PathVariable("id") String id, Model model) {
        Book bookToEdit = bookDao.getById(id);
        model.addAttribute("book", bookToEdit);
        return "edit-form";
    }

    @PostMapping("/update")
    public String update(Book book) {
        bookDao.update(book);
        return "redirect:/";
    }

}
