CREATE TABLE users
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(500),
    date_of_birth VARCHAR(255),
    password      VARCHAR(500)
);

CREATE TABLE account
(
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT         NOT NULL,
    balance         NUMERIC(19, 4) NOT NULL,
    initial_deposit NUMERIC(19, 4) NOT NULL,
    CONSTRAINT fk_account_user FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE,
    CONSTRAINT unique_account_user UNIQUE (user_id)
);

CREATE TABLE phone_data
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT      NOT NULL,
    phone   VARCHAR(13) NOT NULL,
    CONSTRAINT fk_phonedata_user FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE,
    CONSTRAINT unique_phone UNIQUE (phone)
    );

CREATE TABLE email_data
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT       NOT NULL,
    email   VARCHAR(200) NOT NULL,
    CONSTRAINT fk_emaildata_user FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE,
    CONSTRAINT unique_email UNIQUE (email)
);