drop table if exists categories, users, events, requests, compilations, compilations_data;

CREATE TABLE IF NOT EXISTS categories (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  email VARCHAR(254) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS events (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
  title VARCHAR(120) NOT NULL,
  annotation VARCHAR(2000) NOT NULL,
  description VARCHAR(7000) NOT NULL,
  event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  lat REAL NOT NULL,
  lon REAL NOT NULL,
  participant_limit INTEGER NOT NULL,
  paid BOOLEAN NOT NULL,
  request_moderation BOOLEAN NOT NULL,
  state VARCHAR(16) NOT NULL,
  created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  published_on TIMESTAMP WITHOUT TIME ZONE,
  views INTEGER,
  category_id INTEGER NOT NULL,
  user_id INTEGER NOT NULL,
  FOREIGN KEY(category_id) REFERENCES categories(id) ON DELETE CASCADE,
  FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS requests (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
  event_id INTEGER NOT NULL,
  user_id INTEGER NOT NULL,
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  status VARCHAR(16) NOT NULL,
  FOREIGN KEY(event_id) REFERENCES events(id) ON DELETE CASCADE,
  FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
  UNIQUE(event_id, user_id)
);

CREATE TABLE IF NOT EXISTS compilations (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
  title VARCHAR(50) NOT NULL,
  pinned BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations_data (
  compilation_id INTEGER NOT NULL,
  event_id INTEGER NOT NULL,
  PRIMARY KEY(compilation_id, event_id),
  FOREIGN KEY(event_id) REFERENCES events(id) ON DELETE CASCADE,
  FOREIGN KEY(compilation_id) REFERENCES compilations(id) ON DELETE CASCADE
);