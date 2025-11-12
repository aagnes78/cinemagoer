SET names utf8mb4;
SET charset utf8mb4;

DROP DATABASE IF EXISTS filmtracker;
CREATE DATABASE filmtracker charset utf8mb4;
USE filmtracker;

CREATE TABLE countries (
  id INT unsigned NOT NULL auto_increment PRIMARY KEY,
  countryName VARCHAR(100) NOT NULL
) ;

CREATE TABLE films (
  id INT unsigned NOT NULL auto_increment PRIMARY KEY,
  title VARCHAR(100) NOT NULL,
  imdb VARCHAR(20) UNIQUE
) ;

CREATE TABLE alternativeTitles (
-- id INT unsigned NOT NULL auto_increment PRIMARY KEY,
  filmId INT unsigned NOT NULL,
  title VARCHAR(100) NOT NULL,
  CONSTRAINT titleFilms FOREIGN KEY (filmId) REFERENCES films (id),
  UNIQUE (filmId, title)
) ;

CREATE TABLE filmOrigins (
  filmId INT unsigned NOT NULL,
  countryId INT unsigned NOT NULL,
  CONSTRAINT originFilms FOREIGN KEY (filmId) REFERENCES films (id),
  CONSTRAINT originCountries FOREIGN KEY (countryId) REFERENCES countries (id),
  UNIQUE (filmId, countryId)
) ;

CREATE TABLE cities (
  id INT unsigned NOT NULL auto_increment PRIMARY KEY,
  cityName VARCHAR(100) NOT NULL,
  countryId INT unsigned NOT NULL,
  CONSTRAINT cityCountries FOREIGN KEY (countryId) REFERENCES countries (id),
  UNIQUE (cityName, countryId)
) AUTO_INCREMENT=4 ;

CREATE TABLE cinemas (
  id INT unsigned NOT NULL auto_increment PRIMARY KEY,
  cinemaName VARCHAR(100) NOT NULL,
  cityId INT unsigned NOT NULL,
  identifier VARCHAR(100),
  CONSTRAINT cinemaCities FOREIGN KEY (cityId) REFERENCES cities (id),
  UNIQUE (cinemaName, identifier)
) ;

CREATE TABLE screenings (
  id INT unsigned NOT NULL auto_increment PRIMARY KEY,
  filmId INT unsigned NOT NULL,
  cinemaId INT unsigned NOT NULL,
  startDate DATETIME NOT NULL,
  endDate DATETIME NOT NULL,
  CONSTRAINT visitCinemas FOREIGN KEY (cinemaId) REFERENCES cinemas (id),
  CONSTRAINT visitFilms FOREIGN KEY (filmId) REFERENCES films (id),
  UNIQUE (filmId, cinemaId, startDate)
) ;

INSERT INTO countries(countryName)
VALUES ('Belgium'),
  ('Czech Rep'),
  ('Germany'),
  ('Hungary'),
  ('Ireland'),
  ('UK'),
  ('US'),
  ('Czechoslovakia');

INSERT INTO cities (id, cityName, countryId)
VALUES (1, 'Gent', 1),
  (2, 'Budapest', 4),
  (3, 'Dublin', 5);
  
INSERT INTO cinemas (cinemaName, cityId, identifier)
VALUES ('Sphinx', 1, 'Korenmarkt'),
  ('Studio Skoop', 1, 'Zuid'),
  ('Kinepolis', 1, ''),
  ('KASK', 1, 'Bijloke'),
  ('Savoy', 3, 'O\'Connell Str'),
  ('Screen', 3, 'Southside'),
  ('Parnell', 3, 'Parnell St'),
  ('Puskin', 2, 'Astoria'),
  ('Örökmozgó', 2, 'Nagykörút'),
  ('Művész', 2, 'Nagykörút');

INSERT INTO films (title, imdb)
VALUES ('Notting Hill', 'tt0125439'),
  ('The Last Spy', 'tt30486139');

INSERT INTO screenings (filmId, cinemaId, startDate, endDate)
VALUES (1, 5, '1999-08-01 00:00:00', '1999-08-31 23:59:59'),
  (2, 4, '2025-10-12 14:30:00', '2025-10-12 14:30:00');

CREATE USER IF NOT EXISTS cinemagoer IDENTIFIED BY 'cinemagoer';
GRANT SELECT,INSERT,UPDATE,DELETE ON cinemas TO cinemagoer;
GRANT SELECT,INSERT,UPDATE,DELETE ON cities TO cinemagoer;
GRANT SELECT ON countries TO cinemagoer;
GRANT SELECT,INSERT,UPDATE,DELETE ON films TO cinemagoer;
GRANT SELECT,INSERT,UPDATE,DELETE ON alternativeTitles TO cinemagoer;
GRANT SELECT,INSERT,UPDATE,DELETE ON filmOrigins TO cinemagoer;
GRANT SELECT,INSERT,UPDATE,DELETE ON screenings TO cinemagoer;

