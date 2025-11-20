UPDATE books SET is_deleted = false WHERE id IN (1, 2, 3, 4);
INSERT INTO books_categories (book_id, category_id) VALUES (1, 1);
INSERT INTO books_categories (book_id, category_id) VALUES (2, 2);
INSERT INTO books_categories (book_id, category_id) VALUES (3, 3);
INSERT INTO books_categories (book_id, category_id) VALUES (4, 1);