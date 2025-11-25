CREATE TABLE evidences (

    id UUID PRIMARY KEY,
    fileName VARCHAR(100) NOT NULL,
    content-type VARCHAR(20) NOT NULL,
    content BYTEA,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);