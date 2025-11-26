UPDATE users SET is_deleted = false WHERE id = 2;
INSERT INTO shopping_carts (id, user_id) VALUES (1, 2);