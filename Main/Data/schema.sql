CREATE TABLE users IF NOT EXISTS(
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  password TEXT NOT NULL,
);

CREATE TABLE gamesaves IF NOT EXISTS(
  id SERIAL PRIMARY KEY,
  userID INT NOT NULL,
  fileName TEXT NOT NULL
);