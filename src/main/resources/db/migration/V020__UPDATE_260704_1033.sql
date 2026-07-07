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

ALTER TABLE item ADD CONSTRAINT fk_item_item_category_id FOREIGN KEY (item_category_id) REFERENCES item_category (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
