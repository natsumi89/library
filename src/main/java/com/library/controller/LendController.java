package com.library.controller;

import com.library.domain.Book;
import com.library.service.BookService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("")
public class LendController {

    @Autowired
    private BookService bookService;

    @Autowired
    private HttpSession session;

    @GetMapping("/to-lend-page")
    public String toLendPage(@RequestParam("bookId") int bookId, Model model) {
        Book book = bookService.load(bookId);
        model.addAttribute("book", book);
        return "lend.html";
    }

    @PostMapping("/lend")
    public String lendBook(@RequestParam int userId, @RequestParam int bookId, HttpSession session, Model model) {
        Object userIdAttribute = session.getAttribute("userId");
        if (userIdAttribute == null) {
            // ログインしていない場合はログインページにリダイレクト
            return "redirect:/login";
        } else {
            // ログインしている場合は貸出処理を行う
            int loggedInUserId = (int) userIdAttribute;
            Book book = bookService.load(bookId);
            model.addAttribute("book", book);
            bookService.update(loggedInUserId, bookId); // book_idの値を追加
            return "redirect:/to-my-page";
        }
    }

    @PostMapping("/return")
    public String returnBook(@RequestParam int userId, @RequestParam int bookId, HttpSession session, Model model) {
        Object userReturnedId = session.getAttribute("userId");
        if (userReturnedId == null) {
            return "redirect:/login";
        } else {
            int loggedInUserId = (int) userReturnedId;
            Book book = bookService.load(bookId);
            model.addAttribute("book", book);
            bookService.returnBook(loggedInUserId, bookId);
            return "redirect:/to-my-page";
        }
    }
}
