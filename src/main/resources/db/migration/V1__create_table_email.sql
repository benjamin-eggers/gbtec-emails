CREATE TABLE email
(
    id        SERIAL PRIMARY KEY,
    created   timestamp NOT NULL,
    updated   timestamp,
    sender    text,
    recipient jsonb,
    cc        jsonb,
    bcc       jsonb,
    subject   text,
    body      text,
    state     text      NOT NULL
);