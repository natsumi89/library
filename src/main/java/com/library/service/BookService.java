package com.library.service;

import com.library.domain.Book;
import com.library.domain.User;
//import com.library.repository.BoardRepository;
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

    public Book load(Integer book_id) {
        return bookRepository.load(book_id);
    }

    public void update(Integer userId,Integer bookId) {
        bookRepository.update(userId,bookId);
    }

    public List<String> findAllGenres() {
        return bookRepository.findAllGenres();
    }

    public List<Book> loanList(Integer userId) {
        return bookRepository.bookFindByUserId(userId);
    }

    public void returnBook(Integer userId,Integer bookId) {
        bookRepository.returnBook(userId,bookId);
    }
}

