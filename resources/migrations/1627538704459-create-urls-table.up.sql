CREATE TABLE urls
(
    id  VARCHAR(255) NOT NULL UNIQUE PRIMARY KEY,
    url VARCHAR(255) NOT NULL
);
--;;
INSERT INTO urls (id, url)
VALUES (1, 'http://abc.com/1');
--;;
INSERT INTO urls (id, url)
VALUES (2, 'http://abc.com/2');
