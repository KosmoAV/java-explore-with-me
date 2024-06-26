drop table if exists stats;

CREATE TABLE IF NOT EXISTS stats (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
  app VARCHAR(128) NOT NULL,
  uri VARCHAR(128) NOT NULL,
  ip VARCHAR(16) NOT NULL,
  time_stamp TIMESTAMP WITHOUT TIME ZONE NOT NULL
);