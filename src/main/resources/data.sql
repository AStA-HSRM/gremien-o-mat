INSERT INTO mgmt_user (version, username, password, role) VALUES
(0,'user','user','USER'),
(0,'admin','admin', 'ADMIN');

INSERT INTO gremium (version, name, abbr, description) VALUES
  (0, 'Das Test Gremium', 'dtg', 'Das erste Test Gremium der Welt!'),
  (0, 'Kein Test Gremium', 'ktg', 'Das hier ist wirklich kein Test.'),
  (0, 'Vier Fragen Gremium','vft', 'Das vft hat 4 Fragen.'),
  (0, 'CSV Query Upload Test','cqut', 'Ein Gremium zum Testen der Query-CSV Upload Funktionen.');

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

INSERT INTO candidate_answer (id, version, question_text, choice, reason) VALUES
  (1,0,'Dieser Aussage soll zugestimmt werden.',0,'Weil ich der erste Kandidat bin!'),
  (2,0,'Dieser Aussage soll nicht zugestimmt werden.',0,'Weil ich der erste Kandidat bin!'),
  (3,0,'Gegenüber dieser Frage sollte man neutral eingestimmt sein.',0,'Weil ich der erste Kandidat bin!'),
  (4,0,'Dieser Aussage soll zugestimmt werden.',1,'Weil ich der zweite Kandidat bin!'),
  (5,0,'Dieser Aussage soll nicht zugestimmt werden.',0,'Weil ich der zweite Kandidat bin!'),
  (6,0,'Gegenüber dieser Frage sollte man neutral eingestimmt sein.',1,'Weil ich der zweite Kandidat bin!'),
  (7,0,'Dieser Aussage soll zugestimmt werden.',-1,'Weil ich der dritte Kandidat bin!'),
  (8,0,'Dieser Aussage soll nicht zugestimmt werden.',1,'Weil ich der dritte Kandidat bin!'),
  (9,0,'Gegenüber dieser Frage sollte man neutral eingestimmt sein.',1,'Weil ich der dritte Kandidat bin!'),
  (10,0,'Dieser Aussage soll zugestimmt werden.',1,'Weil ich der vierte Kandidat bin!'),
  (11,0,'Dieser Aussage soll nicht zugestimmt werden.',-1,'Weil ich der vierte Kandidat bin!'),
  (12,0,'Gegenüber dieser Frage sollte man neutral eingestimmt sein.',0,'Weil ich der vierte Kandidat bin!');

INSERT INTO candidate_answers (answers_id, candidate_email) VALUES
(1,'1@mail.de'),
(2,'1@mail.de'),
(3,'1@mail.de'),
(4,'2@mail.de'),
(5,'2@mail.de'),
(6,'2@mail.de'),
(7,'3@mail.de'),
(8,'3@mail.de'),
(9,'3@mail.de'),
(10,'4@mail.de'),
(11,'4@mail.de'),
(12,'4@mail.de');

INSERT INTO candidate_join (gremium_id, candidate_id) VALUES
('dtg','1@mail.de'),
('dtg','2@mail.de'),
('dtg','3@mail.de'),
('dtg','4@mail.de'),
('ktg','1@mail.de'),
('ktg','5@mail.de'),
('ktg','6@mail.de'),
('ktg','7@mail.de'),
('ktg','8@mail.de'),
('vft','1@mail.de'),
('vft','10@mail.de'),
('vft','11@mail.de'),
('vft','9@mail.de');

INSERT INTO query_contain (gremium_id, query_id) VALUES
('dtg','Dieser Aussage soll zugestimmt werden.'),
('dtg','Dieser Aussage soll nicht zugestimmt werden.'),
('dtg','Gegenüber dieser Frage sollte man neutral eingestimmt sein.'),
('ktg','Griechenland ist besetzt von Griechen.'),
('ktg','Bier schmeckt nicht.'),
('ktg','Diese Aussage ist dir komplett egal.'),
('ktg','Tomaten sind lila.'),
('vft','1. Frage!'),
('vft','2. Frage!'),
('vft','3. Frage!'),
('vft','4. Frage!');


