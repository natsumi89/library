package com.library.repository;

import com.library.domain.Book;
//import com.library.domain.User;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    public Book load(Integer bookId) {
        String sql = "SELECT book_id, title,author,genre,publication_date,status FROM books WHERE book_id = :book_id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("book_id",bookId);
        Book book = template.queryForObject(sql,param,BOOK_ROW_MAPPER);
        return book;
    }

    public List<String> findAllGenres() {
        String sql = "SELECT DISTINCT genre FROM books"; // ジャンルの一覧を取得するSQLクエリ
        return template.queryForList(sql, new MapSqlParameterSource(), String.class);
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
            // 年のみの場合でも検索可能に修正
            sql += " AND EXTRACT(YEAR FROM publication_date) = :publishYear";
            params.addValue("publishYear", Integer.parseInt(publishYear));
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

    public void update(Integer userId, Integer bookId) {
        String updateSql = "UPDATE books SET status = '貸出中' WHERE book_id = :book_id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("book_id", bookId);
        template.update(updateSql, param);

        String insertSql = "INSERT INTO loans (user_id, book_id, loan_date, due_date, returned) " +
                "VALUES (:user_id, :book_id, :loan_date, :due_date, :returned)";
        LocalDate today = LocalDate.now();
        LocalDate dueDate = today.plus(2, ChronoUnit.WEEKS);
        SqlParameterSource param2 = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("book_id", bookId)
                .addValue("loan_date", today) // 今日の日付を設定
                .addValue("due_date", dueDate)
                .addValue("returned",false);

        template.update(insertSql, param2);
    }
    public void returnBook(Integer userId, Integer bookId) {
        // books テーブルの status を更新
        String updateBookSql = "UPDATE Books SET status = '貸出可' WHERE book_id = :book_id";
        SqlParameterSource bookParam = new MapSqlParameterSource().addValue("book_id", bookId);
        template.update(updateBookSql, bookParam);

        // loans テーブルの returned を更新
        String updateLoanSql = "UPDATE Loans SET returned = true WHERE book_id = :book_id AND user_id = :user_id";
        SqlParameterSource loanParam = new MapSqlParameterSource()
                .addValue("book_id", bookId)
                .addValue("user_id", userId);
        template.update(updateLoanSql, loanParam);
    }

    public List<Book> bookFindByUserId(Integer userId) {
        String sql = "SELECT books.book_id, books.title, books.author, books.genre, books.publication_date,status" +
                " FROM books" +
                " INNER JOIN loans AS l ON books.book_id = l.book_id" +  // LEFT JOIN を使用する
                " WHERE user_id = :userId AND books.status = '貸出中'";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        List<Book> loanList = template.query(sql, params, BOOK_ROW_MAPPER);
        return loanList;
    }
}
