package com.library.controller;

import com.library.domain.Book;
//import com.library.entity.Board;
//import com.library.repository.BoardRepository;
import com.library.repository.BookRepository;
import com.library.service.BookService;
//import com.library.util.Pagination;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("")
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/back")
    public String back() {
        return "redirect:/top";
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

    @GetMapping("/top")
    public String toTopPage(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size) {
        List<Book> allBooks = bookService.bookList();
        List<Book> pagedBooks = getPagedList(allBooks, page, size);

        List<String> genreList = bookService.findAllGenres();

        model.addAttribute("bookList", pagedBooks);
        model.addAttribute("currentPage", page + 1); // ページ番号は0からではなく1から表示する

        // ページングに使用するデータを追加
        int totalPages = (int) Math.ceil((double) allBooks.size() / size);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", size);
        model.addAttribute("hasNext", page < totalPages - 1); // 次のページがあるかどうか
        model.addAttribute("hasPrevious", page > 0); // 前のページがあるかどうか
        model.addAttribute("genreList",genreList);

        return "top";
    }

    private List<Book> getPagedList(List<Book> allBooks, int page, int size) {
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, allBooks.size());
        return allBooks.subList(fromIndex, toIndex);
    }

    @GetMapping("/detail/{book_id}")
    public String detail(@PathVariable("book_id") Integer book_id, Model model) {
        Book book = bookService.load(book_id);
        model.addAttribute("book", book);
        return "book-detail";
    }
}
