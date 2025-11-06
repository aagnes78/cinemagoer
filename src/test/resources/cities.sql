INSERT INTO cities(cityName, countryId)
VALUES ('testCity1', (SELECT id FROM countries WHERE countryName = 'Belgium')),
       ('testCity2', (SELECT id FROM countries WHERE countryName = 'Belgium'));
