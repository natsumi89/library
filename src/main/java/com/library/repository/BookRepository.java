package com.library.repository;

import com.library.domain.Book;
import com.library.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepository {
    @Autowired
    private NamedParameterJdbcTemplate template;

    public static final RowMapper<Book> BOOK_ROW_MAPPER = (rs, i) -> {
        Book book = new Book();

        book.setBookId(rs.getInt("book_id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setGenre(rs.getString("genre"));
        book.setPublicationDate(rs.getDate("publication_date"));
        book.setStatus(rs.getString("status"));
        return book;
    };

    public List<Book> findAll() {
        String sql = "SELECT book_id, title,author,genre,publication_date,status FROM books ORDER BY book_id";
        List<Book> bookList = template.query(sql, BOOK_ROW_MAPPER);
        return bookList;
    }

    public void insert(Book book) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("title", book.getTitle())
                .addValue("author", book.getAuthor())
                .addValue("genre", book.getGenre()).addValue("publication_date", book.getPublicationDate())
                .addValue("status", book.getStatus());
        String sql = "INSERT into books(title,author,genre,publication_date, status" +
                "VALUES(:title,:author,:genre,:publicationDate,:status)";
        template.update(sql, param);
    }

    // コンストラクターでNamedParameterJdbcTemplateをインジェクトする
    public BookRepository(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    // 書籍を検索するメソッド
    public List<Book> searchBooks(String title, String publishYear, String genre, String author) {
        String sql = "SELECT book_id, title, author, genre, publication_date, status FROM books WHERE 1 = 1";
        MapSqlParameterSource params = new MapSqlParameterSource();

        if (title != null && !title.isEmpty()) {
            sql += " AND title LIKE :title";
            params.addValue("title", "%" + title + "%");
        }
        if (publishYear != null && !publishYear.isEmpty()) {
            sql += " AND publication_date LIKE :publishYear";
            params.addValue("publishYear", "%" + publishYear + "%");
        }
        if (genre != null && !genre.isEmpty()) {
            sql += " AND genre = :genre";
            params.addValue("genre", genre);
        }
        if (author != null && !author.isEmpty()) {
            sql += " AND author = :author";
            params.addValue("author", author);
        }
        return template.query(sql, params, new BeanPropertyRowMapper<>(Book.class));
    }
}
