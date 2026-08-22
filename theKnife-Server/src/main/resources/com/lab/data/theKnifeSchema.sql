CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name character varying(50),
    surname character varying(50),
    birth_date date,
    address character varying(255),
    latitude double precision,
    longitude double precision,
    username character varying(50) NOT NULL UNIQUE,
    password character varying(255) NOT NULL,
    role character varying(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS restaurants (
    id SERIAL PRIMARY KEY,
    owner_id integer REFERENCES users(id) ON DELETE CASCADE,
    name character varying(255) NOT NULL,
    address character varying(255),
    location character varying(255),
    price character varying(10),
    cuisine character varying(255),
    latitude double precision,
    longitude double precision,
    delivery character varying(50),
    booking character varying(255),
    award character varying(50)
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

