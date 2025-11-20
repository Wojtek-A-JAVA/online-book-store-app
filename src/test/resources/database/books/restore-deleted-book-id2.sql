UPDATE books SET is_deleted = false WHERE id = 2;
INSERT INTO books_categories (book_id, category_id) VALUES (2, 2);