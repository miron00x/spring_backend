CREATE DATABASE IF NOT EXISTS lol;
/*CREATE USER test_user WITH password 'qwerty';*/
GRANT ALL ON DATABASE demo TO lol;
/*CREATE TABLE IF NOT EXISTS users (
  id SERIAL PRIMARY KEY,
  role CHAR(64),
  userName CHAR(64),
  password CHAR(64)
);
CREATE TABLE IF NOT EXISTS documents (
  id SERIAL PRIMARY KEY,
  docName CHAR(64),
  user_id INTEGER REFERENCES users(id),
  uploadDate date,
  updateDate date
);
INSERT INTO users(id, role, userName, password) VALUES
(1, 'USER', 'VasyaPupkin', 'qwer'),
(2, 'USER', 'PetyaPupkin', '123'),
(3, 'USER', 'UnNamed', '1111');
INSERT INTO documents(id, docName, user_id, uploadDate, updateDate) values
(1, 'Book', 1, '2010-06-30', '2010-06-30'),
(2, 'Image', 2, '2012-06-30', '2015-06-30'),
(3, 'Lol', 3, '2013-06-30', '2016-06-30'),
(4, 'Hook', 1, '2017-06-30', '2018-06-30');*/
SELECT * FROM documents;