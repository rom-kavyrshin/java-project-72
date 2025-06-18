DROP TABLE IF EXISTS url_checks;
DROP TABLE IF EXISTS urls;

CREATE TABLE urls (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP
);

CREATE TABLE url_checks (
    id SERIAL PRIMARY KEY,
    url_id SERIAL REFERENCES urls (id) ON UPDATE CASCADE ON DELETE CASCADE,
    status_code INTEGER,
    h1 VARCHAR(255),
    title VARCHAR(255),
    description TEXT,
    created_at TIMESTAMP
);
