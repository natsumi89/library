package com.library.controller;

import com.library.domain.Book;
import com.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("")
public class BookController {
    @Autowired
    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/back")
    public String back() {
        return "top";
    }

    @GetMapping("/search")
    public String searchBooks(@RequestParam(value = "title", required = false) String title,
                              @RequestParam(value = "publish_year", required = false) String publishYear,
                              @RequestParam(value = "genre", required = false) String genre,
                              @RequestParam(value = "author", required = false) String author,
                              Model model) {
        // title、publishYear、genre、authorの値を使用してデータベースから検索を行う処理
        List<Book> searchResult = bookService.searchBooks(title, publishYear, genre, author);
        model.addAttribute("books", searchResult);
        return "search";
    }


}
