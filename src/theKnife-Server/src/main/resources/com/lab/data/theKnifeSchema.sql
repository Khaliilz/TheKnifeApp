CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name varchar(50) NOT NULL,
    surname varchar(50) NOT NULL,
    birth_date date NOT NULL,
    address varchar(255) NOT NULL,
    latitude double precision,
    longitude double precision,
    username varchar(50) NOT NULL UNIQUE,
    password varchar(255) NOT NULL,
    role varchar(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS restaurants (
    id SERIAL PRIMARY KEY,
    owner_id integer REFERENCES users(id) ON DELETE CASCADE,
    name varchar(255) NOT NULL,
    address varchar(255) NOT NULL,
    location varchar(255),
    price varchar(10) NOT NULL,
    cuisine varchar(255) NOT NULL ,
    latitude double precision,
    longitude double precision,
    delivery varchar(50),
    booking varchar(255)
);

CREATE TABLE IF NOT EXISTS bookmarks (
    user_id integer NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    restaurant_id integer NOT NULL REFERENCES restaurants(id) ON DELETE CASCADE,
    PRIMARY KEY(user_id, restaurant_id)
);

CREATE TABLE IF NOT EXISTS reviews (
    id SERIAL PRIMARY KEY,
    user_id integer REFERENCES users(id) ON DELETE CASCADE,
    restaurant_id integer REFERENCES restaurants(id) ON DELETE CASCADE,
    stars integer,
    comment text,
    answer text,
    CONSTRAINT reviews_stars_check CHECK (((stars >= 1) AND (stars <= 3)))
);

