CREATE SEQUENCE  IF NOT EXISTS primary_sequence START WITH 10000 INCREMENT BY 1;

ALTER TABLE unit ADD active BOOLEAN;

UPDATE unit SET active = 'TRUE' WHERE active IS NULL;

ALTER TABLE unit ALTER COLUMN  active SET NOT NULL;

ALTER TABLE item ADD item_category_id BIGINT;

CREATE TABLE item_category (
    id BIGINT NOT NULL,
    name VARCHAR(255),
    active BOOLEAN,
    date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT item_category_pkey PRIMARY KEY (id)
);

CREATE TABLE account_config (
    id BIGINT NOT NULL,
    max_units INTEGER NOT NULL,
    non_stackable_factor numeric(10, 2) NOT NULL,
    fragile_factor numeric(10, 2) NOT NULL,
    heavy_factor numeric(10, 2) NOT NULL,
    tenant_id UUID NOT NULL,
    date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT account_config_pkey PRIMARY KEY (id)
);

CREATE TABLE tenant (
    id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL,
    date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT tenant_pkey PRIMARY KEY (id)
);

ALTER TABLE item ADD CONSTRAINT fk_item_item_category_id FOREIGN KEY (item_category_id) REFERENCES item_category (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE account_config ADD CONSTRAINT fk_account_config_tenant_id FOREIGN KEY (tenant_id) REFERENCES tenant (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE account_config ADD CONSTRAINT unique_account_config_tenant_id UNIQUE (tenant_id);
