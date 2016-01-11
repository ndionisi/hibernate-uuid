DROP TABLE IF EXISTS "order_item";
DROP TABLE IF EXISTS "order";
DROP TABLE IF EXISTS "customer";

CREATE TABLE "customer" (
  id   UUID,
  name TEXT,
  PRIMARY KEY (id)
);

CREATE TABLE "order" (
  id          UUID,
  customer_id UUID,
  PRIMARY KEY (id),
  FOREIGN KEY (customer_id) REFERENCES "customer" (id)
);

CREATE TABLE "order_item" (
  id           UUID,
  order_id  UUID,
  product_name TEXT,
  PRIMARY KEY (id),
  FOREIGN KEY (order_id) REFERENCES "order" (id)
);