ALTER TABLE unit ALTER COLUMN width TYPE numeric(10, 2) USING (width::numeric(10, 2));

ALTER TABLE unit RENAME COLUMN width TO capacity_m3;

ALTER TABLE unit ALTER COLUMN max_weight_limit TYPE numeric(10, 2) USING (max_weight_limit::numeric(10, 2));
