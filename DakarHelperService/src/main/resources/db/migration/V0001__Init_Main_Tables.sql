CREATE TABLE IF NOT EXISTS vendors (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS vendor_files (
    id BIGSERIAL PRIMARY KEY,
    vendor_id BIGINT REFERENCES vendors(id) ON DELETE CASCADE,
    filepath VARCHAR(255) NOT NULL,
    file_status VARCHAR(20) NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS subcategories (
    id BIGSERIAL PRIMARY KEY,
    category VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS header_cells (
    id BIGSERIAL PRIMARY KEY,
    subcategory_id BIGINT DEFAULT NULL,
    original_name VARCHAR(255) NOT NULL,
    normalized_name VARCHAR(255) NULL,
    category VARCHAR(255) NOT NULL,
    cell_status VARCHAR(255) NULL,
    CONSTRAINT fk_subcategory
        FOREIGN KEY (subcategory_id)
        REFERENCES subcategories(id)
        ON DELETE SET NULL
);