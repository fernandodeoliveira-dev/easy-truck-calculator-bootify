CREATE TABLE pre_set (
    id UUID NOT NULL,
    name VARCHAR(255),
    active BOOLEAN,
    date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pre_set_pkey PRIMARY KEY (id)
);

CREATE TABLE pre_set_item (
    pre_set_id UUID NOT NULL,
    item_id UUID NOT NULL
);

ALTER TABLE pre_set_item ADD CONSTRAINT pk_pre_set_item PRIMARY KEY (pre_set_id, item_id);

ALTER TABLE pre_set_item ADD CONSTRAINT fk_pre_set_item_pre_set_id FOREIGN KEY (pre_set_id) REFERENCES pre_set (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE pre_set_item ADD CONSTRAINT fk_pre_set_item_item_id FOREIGN KEY (item_id) REFERENCES item (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
