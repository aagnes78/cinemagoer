INSERT INTO screenings (filmId, cinemaId, startDate, endDate)
VALUES
((SELECT id FROM films WHERE title = 'testFilm1'),
        (SELECT id FROM cinemas WHERE cinemaName = 'testCinema1'),
        '1900-10-31 20:30:00', '1900-10-31 20:30:00'),
((SELECT id FROM films WHERE title = 'testFilm1'),
        (SELECT id FROM cinemas WHERE cinemaName = 'testCinema12'),
        '1900-10-21 20:30:00', '1900-10-21 20:30:00'),
((SELECT id FROM films WHERE title = 'testFilm2'),
        (SELECT id FROM cinemas WHERE cinemaName = 'testCinema1'),
        '1900-09-01 00:00:00', '1900-09-30 23:59:59');