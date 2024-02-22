
-- Usersテーブルの作成
CREATE TABLE Users (
user_id SERIAL PRIMARY KEY,
first_name VARCHAR(50) NOT NULL,
last_name VARCHAR(50) NOT NULL,
email VARCHAR(100) NOT NULL,
password VARCHAR(100) NOT NULL,
is_admin BOOLEAN DEFAULT FALSE, -- 管理者かどうかを判断する列
birth_date DATE,
master BOOLEAN DEFAULT FALSE, -- 100冊以上借りたかどうかを判断する列
registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Booksテーブルの作成
CREATE TABLE Books (
book_id SERIAL PRIMARY KEY,
title VARCHAR(255) NOT NULL,
author VARCHAR(100) NOT NULL,
genre VARCHAR(50),
publication_date DATE,
status VARCHAR(20) NOT NULL
);

-- Loansテーブルの作成
CREATE TABLE Loans (
loan_id SERIAL PRIMARY KEY,
user_id INT NOT NULL,
book_id INT NOT NULL,
loan_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
due_date DATE,
returned BOOLEAN DEFAULT FALSE,
FOREIGN KEY (user_id) REFERENCES Users(user_id),
FOREIGN KEY (book_id) REFERENCES Books(book_id)
);

-- Reservationsテーブルの作成
CREATE TABLE Reservations (
reservation_id SERIAL PRIMARY KEY,
user_id INT NOT NULL,
book_id INT NOT NULL,
reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
status VARCHAR(20) NOT NULL,
position INT,
FOREIGN KEY (user_id) REFERENCES Users(user_id),
FOREIGN KEY (book_id) REFERENCES Books(book_id)
);

-- 人気商品を追跡するためのテーブル
CREATE TABLE PopularBooks (
book_id INT PRIMARY KEY,
loan_count INT DEFAULT 0,
FOREIGN KEY (book_id) REFERENCES Books(book_id)
);

CREATE TABLE Reviews (
review_id SERIAL PRIMARY KEY,
user_id INT NOT NULL,
loan_id INT NOT NULL, -- 過去の貸出履歴を参照
rating INT NOT NULL,
comment TEXT,
review_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (user_id) REFERENCES Users(user_id),
FOREIGN KEY (loan_id) REFERENCES Loans(loan_id) -- 過去の貸出履歴に関連付け
);

-- Booksテーブルに情報を格納するクエリ
-- 1. ハリー・ポッターシリーズ
INSERT INTO Books (title, author, genre, publication_date, status)
VALUES ('ハリー・ポッターと賢者の石', 'J.K. Rowling', 'ファンタジー', '1997-06-26', '貸出可'),
       ('ハリー・ポッターと秘密の部屋', 'J.K. Rowling', 'ファンタジー', '1998-07-02', '貸出可'),
       ('ハリー・ポッターとアズカバンの囚人', 'J.K. Rowling', 'ファンタジー', '1999-07-08', '貸出可'),
       ('ハリー・ポッターと炎のゴブレット', 'J.K. Rowling', 'ファンタジー', '2000-07-08', '貸出可'),
       ('ハリー・ポッターと不死鳥の騎士団', 'J.K. Rowling', 'ファンタジー', '2003-06-21', '貸出可'),
       ('ハリー・ポッターと謎のプリンス', 'J.K. Rowling', 'ファンタジー', '2005-07-16', '貸出可'),
       ('ハリー・ポッターと死の秘宝', 'J.K. Rowling', 'ファンタジー', '2007-07-21', '貸出可');

-- 2. 名作小説
INSERT INTO Books (title, author, genre, publication_date, status)
VALUES ('1984', 'George Orwell', 'SF', '1949-06-08', '貸出可'),
       ('動物農場', 'George Orwell', 'SF', '1945-08-17', '貸出可'),
       ('魔法使いの嫁', 'Yamazaki Kore', 'ファンタジー', '2008-11-11', '貸出可'),
       ('ロード・オブ・ザ・リング', 'J.R.R. Tolkien', 'ファンタジー', '1954-07-29', '貸出可'),
       ('ミステリー・ハウス', 'Agatha Christie', 'ミステリー', '1939-01-01', '貸出可');

-- 3. 科学書籍
INSERT INTO Books (title, author, genre, publication_date, status)
VALUES ('宇宙の始まりからブラックホールまで', 'Stephen Hawking', '科学', '1988-01-01', '貸出可'),
       ('未来の技術', 'Michio Kaku', '科学', '2018-09-25', '貸出可'),
       ('人体の不思議', 'James Fixx', '科学', '2000-03-07', '貸出可'),
       ('エボリューション', 'Stephen Baxter', '科学', '2002-01-07', '貸出可'),
       ('進化の秘密', 'Richard Dawkins', '科学', '1995-06-15', '貸出可');

-- 4. ミステリー小説
INSERT INTO Books (title, author, genre, publication_date, status)
VALUES ('シャーロック・ホームズの冒険', 'Arthur Conan Doyle', 'ミステリー', '1892-10-14', '貸出可'),
       ('アンド・ゼン・ゼア・ワー・ノーン', 'Agatha Christie', 'ミステリー', '1920-01-01', '貸出可'),
       ('狼たちの挽歌', 'James Ellroy', 'ミステリー', '1986-01-01', '貸出可'),
       ('切り裂きジャックの告白', 'Kerri Maniscalco', 'ミステリー', '2018-09-20', '貸出可'),
       ('逆転裁判', 'Capcom', 'ミステリー', '2001-10-12', '貸出可');

-- 5. SF小説
INSERT INTO Books (title, author, genre, publication_date, status)
VALUES ('アリス・イン・ワンダーランド', 'Lewis Carroll', 'ファンタジー', '1865-11-26', '貸出可'),
       ('レディ・オア・ザ・タイガー', 'Frank R. Stockton', 'ファンタジー', '1882-01-01', '貸出可'),
       ('クリムゾン・キング', 'Stephen King', 'ホラー', '1977-01-01', '貸出可'),
       ('宇宙の彼方から来たもの', 'H.P. Lovecraft', 'ホラー', '1936-08-01', '貸出可'),
       ('エンダーズ・ゲーム', 'Orson Scott Card', 'SF', '1985-12-01', '貸出可');

-- 6. ドラマ
INSERT INTO Books (title, author, genre, publication_date, status)
VALUES ('ロミオとジュリエット', 'William Shakespeare', 'ドラマ', '1597-01-01', '貸出可'),
       ('アラビアン・ナイト', 'Anonymous', 'ファンタジー', '1700-01-01', '貸出可'),
       ('夏の夜の夢', 'William Shakespeare', 'ドラマ', '1595-01-01', '貸出可'),
       ('キング・リアー', 'William Shakespeare', 'ドラマ', '1606-01-01', '貸出可'),
       ('マクベス', 'William Shakespeare', 'ドラマ', '1606-01-01', '貸出可');

-- 7. 日本の小説
INSERT INTO Books (title, author, genre, publication_date, status)
VALUES ('銀河鉄道の夜', '宮沢賢治', '小説', '1934-01-01', '貸出可'),
       ('ノルウェイの森', '村上春樹', '小説', '1987-08-01', '貸出可'),
       ('舞姫', '森鴎外', '小説', '1890-01-01', '貸出可'),
       ('こゝろ', '夏目漱石', '小説', '1914-01-01', '貸出可'),
       ('雪国', '川端康成', '小説', '1947-01-01', '貸出可');

-- 8. 伝記・自叙伝
INSERT INTO Books (title, author, genre, publication_date, status)
VALUES ('わたしの幸福論', '石原慎太郎', '伝記・自叙伝', '2006-04-01', '貸出可'),
       ('飛ぶがよい、はずがない', '佐藤勝彦', '伝記・自叙伝', '2018-01-01', '貸出可'),
       ('グレート・リーダーズ・ハビット', '奥田英朗', '伝記・自叙伝', '2009-03-01', '貸出可'),
       ('強者の流儀', '柳原陽一郎', '伝記・自叙伝', '2015-08-01', '貸出可'),
       ('私の青空', '細川俊夫', '伝記・自叙伝', '1996-12-01', '貸出可');

-- 9. ビジネス・経済書
INSERT INTO Books (title, author, genre, publication_date, status)
VALUES ('ルールブック・オブ・ライフ', 'リチャード・テンプラー', 'ビジネス・経済書', '1998-01-01', '貸出可'),
       ('豊かさの法則', 'ウォレス・D・ワトルズ', 'ビジネス・経済書', '1910-01-01', '貸出可'),
       ('アート・オブ・ウォー', '孫子', 'ビジネス・経済書', '400-01-01', '貸出可'),
       ('影響力の武器', 'ロバート・B・チャルディーニ', 'ビジネス・経済書', '1984-01-01', '貸出可'),
       ('アウトライアーズ', 'マルコム・グラッドウェル', 'ビジネス・経済書', '2008-11-01', '貸出可');
-- 10. ロマンス小説
INSERT INTO Books (title, author, genre, publication_date, status)
VALUES ('プライドと偏見', 'ジェーン・オースティン', 'ロマンス小説', '1813-01-28', '貸出可'),
       ('タイタニック', 'ジェームズ・キャメロン', 'ロマンス小説', '1997-11-18', '貸出可'),
       ('君の名は。', '新海誠', 'ロマンス小説', '2016-08-26', '貸出可'),
       ('くちづけを、今', '乙川 拓斗', 'ロマンス小説', '2019-09-20', '貸出可'),
       ('愛と宿命のサラブレッド', 'ノーラ・ロバーツ', 'ロマンス小説', '2009-06-30', '貸出可');

-- 11. ミステリー・サスペンス小説
INSERT INTO Books (title, author, genre, publication_date, status)
VALUES ('シャーロック・ホームズ', 'アーサー・コナン・ドイル', 'ミステリー・サスペンス小説', '1887-01-01', '貸出可'),
       ('ダ・ヴィンチ・コード', 'ダン・ブラウン', 'ミステリー・サスペンス小説', '2003-03-18', '貸出可'),
       ('ゴーン・ガール', 'ギリアン・フリン', 'ミステリー・サスペンス小説', '2012-05-24', '貸出可'),
       ('リトル・ライズ', 'リアン・モリアーティ', 'ミステリー・サスペンス小説', '2014-07-29', '貸出可'),
       ('雪国', '川端康成', 'ミステリー・サスペンス小説', '1947-01-01', '貸出可');

-- 12. ファンタジー小説
INSERT INTO Books (title, author, genre, publication_date, status)
VALUES ('指輪物語', 'J.R.R. トールキン', 'ファンタジー小説', '1954-07-29', '貸出可'),
       ('アースシーカー', 'オーソン・スコット・カード', 'ファンタジー小説', '1987-12-01', '貸出可'),
       ('ニュームーン', 'ステファニー・メイヤー', 'ファンタジー小説', '2006-08-21', '貸出可'),
       ('妖精の森', '高田崇史', 'ファンタジー小説', '2002-10-01', '貸出可');

-- 13. SF小説
INSERT INTO Books (title, author, genre, publication_date, status)
VALUES ('アイ,ロボット', 'アイザック・アシモフ', 'SF小説', '1950-12-02', '貸出可'),
       ('ネクサス', 'リン・ノーランド', 'SF小説', '2012-12-18', '貸出可'),
       ('ディストピア', 'ジョージ・オーウェル', 'SF小説', '1949-06-08', '貸出可'),
       ('スノーピアサー', 'ジャック・オコナー', 'SF小説', '2013-01-20', '貸出可'),
       ('鋼の錬金術師', '荒川弘', 'SF小説', '2001-07-12', '貸出可');

-- 14. 歴史小説
INSERT INTO Books (title, author, genre, publication_date, status)
VALUES ('源氏物語', '紫式部', '歴史小説', '1000-01-01', '貸出可'),
       ('水滸伝', '施耐庵', '歴史小説', '1300-01-01', '貸出可'),
       ('西遊記', '吴承恩', '歴史小説', '1500-01-01', '貸出可'),
       ('檸檬', '梶井基次郎', '歴史小説', '1925-01-01', '貸出可'),
       ('ロミオとジュリエット', 'ウィリアム・シェイクスピア', '歴史小説', '1597-01-01', '貸出可');



