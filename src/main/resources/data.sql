-- выполнить для добавления юзеров в БД

BEGIN;

INSERT INTO users (username, password, enabled) VALUES ('reader', crypt('reader', gen_salt('bf')), '1');
INSERT INTO users (username, password, enabled) VALUES ('writer', crypt('writer', gen_salt('bf')), '1');
INSERT INTO users (username, password, enabled) VALUES ('admin', crypt('admin', gen_salt('bf')), '1');
INSERT INTO users (username, password, enabled) VALUES ('kaban', crypt('kaban', gen_salt('bf')), '1');

INSERT INTO authorities (username, authority) values ('reader', 'ROLE_READER');
INSERT INTO authorities (username, authority) values ('writer', 'ROLE_WRITER');
INSERT INTO authorities (username, authority) values ('admin', 'ROLE_ADMIN');
INSERT INTO authorities (username, authority) values ('kaban', 'ROLE_ADMIN');

COMMIT;