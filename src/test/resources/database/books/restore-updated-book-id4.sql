UPDATE books SET title = 'Book 4', author = 'Author 1', isbn = '1234567890126', price = 40.40,
                 description = 'Book 4 description', cover_image = 'http://www.book.com/cover_4' WHERE id = 4;
INSERT INTO books_categories (book_id, category_id) VALUES (4, 1);