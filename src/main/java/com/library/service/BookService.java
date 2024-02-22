package com.library.service;

import com.library.domain.Book;
import com.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    // コンストラクターでBookRepositoryをインジェクトする
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // 書籍を検索するメソッド
    public List<Book> searchBooks(String title, String publishYear, String genre, String author) {
        return bookRepository.searchBooks(title, publishYear, genre, author);
    }

    public List<Book> bookList() {
        return bookRepository.findAll();
    }
}

