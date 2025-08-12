-- Script to initialice tables

-- =============================================
-- CUSTOMERS DATABASE
-- =============================================
\c customers_db;

-- Main table for customers
CREATE TABLE IF NOT EXISTS customers (
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	balance DECIMAL(10, 2) NOT NULL CHECK(balance >= 0),
	code VARCHAR(15) NOT NULL,
	phone VARCHAR(20) NOT NULL,
	iban VARCHAR(20) UNIQUE NOT NULL,
	surname VARCHAR(100) NOT NULL,
	address TEXT,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table for many-to-many relationship between customers and products
CREATE TABLE IF NOT EXISTS customer_products (
	id BIGSERIAL PRIMARY KEY,
	customer_id BIGINT NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
	product_id BIGINT NOT NULL,
	product_name VARCHAR(100),
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	UNIQUE(customer_id, product_id) -- One customer cannnot own same product twice
);

-- Create index to optimice query
CREATE INDEX idx_customers_iban ON customers(iban);
CREATE INDEX idx_customers_code ON customers(code);
CREATE INDEX idx_customer_products_customer_id ON customer_products(customer_id);
CREATE INDEX idx_customer_products_product_id ON customer_products(product_id);

-- Function to update "update_at" field
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';


-- =============================================
-- PRODUCTS DATABASE  
-- =============================================

\c products_db;

CREATE TABLE IF NOT EXISTS products (
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	code VARCHAR(20) UNIQUE NOT NULL,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index to optimice query
CREATE INDEX idx_products_name ON products(name);

-- Function to update "update_at" field
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';


-- =============================================
-- TRANSACTIONS DATABASE  
-- =============================================

\c transactions_db;

CREATE TABLE IF NOT EXISTS transactions (
	id BIGSERIAL PRIMARY KEY,
	reference VARCHAR(100) NOT NULL,
	iban VARCHAR(20) NOT NULL,
	date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	amount DECIMAL(10,2) NOT NULL,
	fee DECIMAL(4,2) NOT NULL,
	description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE' 
        CHECK (status IN ('PENDIENTE', 'LIQUIDADA', 'RECHAZADA', 'CANCELADA')),
    channel VARCHAR(20) NOT NULL DEFAULT 'WEB' 
        CHECK (channel IN ('WEB', 'CAJERO', 'OFICINA')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index to optimice query
CREATE INDEX idx_transactions_iban ON transactions(iban);
CREATE INDEX idx_transactions_status ON transactions(status);
CREATE INDEX idx_transactions_channel ON transactions(channel);
CREATE INDEX idx_transactions_date ON transactions(date);
CREATE INDEX idx_transactions_reference ON transactions(reference);


-- Function to update "update_at" field
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
	NEW.updated_at = CURRENT_TIMESTAMP;
	RETURN NEW;
END;
$$ language 'plpgsql';

-- Apply triggers to execute function before each update
\c customers_db;
CREATE TRIGGER update_customers_updated_at BEFORE UPDATE ON customers 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

\c products_db;
CREATE TRIGGER update_products_updated_at BEFORE UPDATE ON products 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

\c transactions_db;
CREATE TRIGGER update_transactions_updated_at BEFORE UPDATE ON transactions 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
