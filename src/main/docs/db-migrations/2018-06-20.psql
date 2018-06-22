-- Create a column for IIIFEnabled items, part of migrating this data
-- from the JSON for each item to the DB so it can be easily queried.

ALTER TABLE items
    ADD COLUMN iiifenabled boolean DEFAULT false;
