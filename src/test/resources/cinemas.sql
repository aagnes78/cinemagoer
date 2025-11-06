INSERT INTO cinemas (cinemaName, cityId, identifier)
VALUES
('testCinema1', (SELECT id FROM cities WHERE cityName='testCity1'), 'testStreet1'),
('testCinema12', (SELECT id FROM cities WHERE cityName='testCity1'), 'testStreet12'),
('testCinema21', (SELECT id FROM cities WHERE cityName='testCity2'), 'testStreet21'),
('testCinema22', (SELECT id FROM cities WHERE cityName='testCity2'), 'testStreet22'),
('cinemaTestUK', (SELECT id FROM cities WHERE cityName='testCityUK'), 'testStreetUK');
