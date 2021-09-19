INSERT INTO gremium (version, name, abbr, description) VALUES
  (0, 'Das Test Gremium', 'DTG', 'Das erste Test Gremium der Welt!'),
  (0, 'Kein Test Gremium', 'KTG', 'Das hier ist wirklich kein Test.'),
  (0, 'Vier Fragen Gremium','VFT', 'Das VFT hat 4 Fragen.'),
  (0, 'CSV Query Upload Test','CQUT', 'Ein Gremium zum Testen der Query-CSV Upload Funktionen.');

INSERT INTO query (version, text) VALUES
  (0,'Dieser Aussage soll zugestimmt werden.'),
  (0,'Dieser Aussage soll nicht zugestimmt werden.'),
  (0,'Gegenüber dieser Frage sollte man neutral eingestimmt sein.'),
  (0,'Griechenland ist besetzt von Griechen.'),
  (0,'Bier schmeckt nicht.'),
  (0,'Diese Aussage ist dir komplett egal.'),
  (0,'Tomaten sind lila.'),
  (0,'1. Frage!'),
  (0,'2. Frage!'),
  (0,'3. Frage!'),
  (0,'4. Frage!');

INSERT INTO query_contain (gremium_id, query_id) VALUES
('DTG','Dieser Aussage soll zugestimmt werden.'),
('DTG','Dieser Aussage soll nicht zugestimmt werden.'),
('DTG','Gegenüber dieser Frage sollte man neutral eingestimmt sein.'),
('KTG','Griechenland ist besetzt von Griechen.'),
('KTG','Bier schmeckt nicht.'),
('KTG','Diese Aussage ist dir komplett egal.'),
('KTG','Tomaten sind lila.'),
('VFT','1. Frage!'),
('VFT','2. Frage!'),
('VFT','3. Frage!'),
('VFT','4. Frage!');


