-- Create the users table
CREATE TABLE IF NOT EXISTS users
(
    id                   BIGSERIAL
        CONSTRAINT user_pk PRIMARY KEY,
    chat_id              BIGINT,
    first_name           VARCHAR(24),
    phone_number         VARCHAR(14),
    chat_state           VARCHAR(32),
    pre_order_date       DATE,
    pre_order_start_time TIME,
    pre_order_end_time   TIME,
    event_place          VARCHAR(128),
    comment              VARCHAR(255)
);

-- Create the orders table
CREATE TABLE IF NOT EXISTS orders
(
    id           BIGSERIAL
        CONSTRAINT order_pk PRIMARY KEY,
    order_state  VARCHAR(12)  NOT NULL,
    comment      VARCHAR(255),
    location     VARCHAR(128) NOT NULL,
    start_order  TIMESTAMP    NOT NULL,
    finish_order TIMESTAMP    NOT NULL,
    user_id      BIGINT REFERENCES users (id),
    created_at   TIMESTAMP    NOT NULL
);
