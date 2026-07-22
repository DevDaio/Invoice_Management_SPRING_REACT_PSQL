CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    mail VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'user'
);

CREATE TABLE IF NOT EXISTS suppliers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS invoices (
    id SERIAL PRIMARY KEY,
    payed BOOLEAN NOT NULL DEFAULT false,
    number VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    supplier_id INT NOT NULL REFERENCES suppliers(id)
);

CREATE TABLE IF NOT EXISTS articles (
    id SERIAL PRIMARY KEY,
    article_number INT NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    price_net DOUBLE PRECISION NOT NULL,
    tax_type VARCHAR(10) NOT NULL,
    unit_type VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    supplier_id INT NOT NULL REFERENCES suppliers(id),
    invoice_id INT NOT NULL REFERENCES invoices(id)
);
