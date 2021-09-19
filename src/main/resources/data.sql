INSERT INTO mgmt_user (version, username, password, role) VALUES
(0,'user','user','USER'),
(0,'admin','admin', 'ADMIN');

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

INSERT INTO candidate (version, firstname, lastname, email) VALUES
(0,'Darth','Vader','1@mail.de'),
(0,'Kim','Kardashian','2@mail.de'),
(0,'Robert','McClanahan','3@mail.de'),
(0,'Virginia','Davis','4@mail.de'),
(0,'Christine','Powell','5@mail.de'),
(0,'Peggy','Kaufman','6@mail.de'),
(0,'Charlotte L','Galvan','7@mail.de'),
(0,'Charles K','Galvan','8@mail.de'),
(0,'Hector','Toro','9@mail.de'),
(0,'Tracy L','Stack','10@mail.de'),
(0,'Linda J','Alvarado','11@mail.de');

INSERT INTO candidate_join (gremium_id, candidate_id) VALUES
('DTG','1@mail.de'),
('DTG','2@mail.de'),
('DTG','3@mail.de'),
('DTG','4@mail.de'),
('KTG','1@mail.de'),
('KTG','5@mail.de'),
('KTG','6@mail.de'),
('KTG','7@mail.de'),
('KTG','8@mail.de'),
('VFT','1@mail.de'),
('VFT','10@mail.de'),
('VFT','11@mail.de'),
('VFT','9@mail.de');

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


