CREATE TABLE edge (
    from_id INTEGER NOT NULL,
    to_id INTEGER NOT NULL,
    CONSTRAINT edge_pk PRIMARY KEY (from_id, to_id),
    CONSTRAINT edge_to_id_uk UNIQUE (to_id)
);
