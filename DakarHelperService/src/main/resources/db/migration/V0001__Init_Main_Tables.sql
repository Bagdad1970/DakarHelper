CREATE TABLE IF NOT EXISTS vendors (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS vendor_files (
    id BIGSERIAL PRIMARY KEY,
    vendor_id BIGINT REFERENCES vendors(id) ON DELETE CASCADE,
    filepath VARCHAR(255) NOT NULL,
    file_status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS excel_header_subcategories (
    id BIGSERIAL PRIMARY KEY,
    subcategory_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS excel_header_cells (
    id BIGSERIAL PRIMARY KEY,
    excel_header_subcategory_id BIGINT DEFAULT NULL,
    origin_name VARCHAR(255) NOT NULL,
    normalized_name VARCHAR(255) NULL,
    category VARCHAR(255) NOT NULL,
    cell_status VARCHAR(255) NULL,
    CONSTRAINT fk_excel_header_subcategory
        FOREIGN KEY (excel_header_subcategory_id)
        REFERENCES excel_header_subcategories(id)
        ON DELETE SET NULL
);