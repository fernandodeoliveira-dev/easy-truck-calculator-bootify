CREATE TABLE unit (
    id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    width DOUBLE PRECISION NOT NULL,
    max_weight_limit DOUBLE PRECISION,
    date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT unit_pkey PRIMARY KEY (id)
);

CREATE TABLE item (
    id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    volume_m3 numeric(10, 2) NOT NULL,
    weight_kg numeric(10, 2),
    stackable BOOLEAN NOT NULL,
    fragile BOOLEAN NOT NULL,
    heavy BOOLEAN NOT NULL,
    active BOOLEAN NOT NULL,
    date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT item_pkey PRIMARY KEY (id)
);
