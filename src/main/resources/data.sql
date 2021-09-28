INSERT INTO gremium (version, name, abbr, description) VALUES
  (0, 'Das Test Gremium', 'dtg', 'Das erste Test Gremium der Welt!'),
  (0, 'CSV Query Upload Test','cqut', 'Ein Gremium zum Testen der Query-CSV Upload Funktionen.');

INSERT INTO query (id, version, text) VALUES
  (-1,0,'Dieser Aussage soll zugestimmt werden.'),
  (-2,0,'Dieser Aussage soll nicht zugestimmt werden.'),
  (-3,0,'Gegenüber dieser Frage sollte man neutral eingestimmt sein.');

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

INSERT INTO candidate_answer (id, version, query_id, opinion, reason) VALUES
  (-31,0,-1,1,'Weil ich der erste Kandidat bin!'),
  (-32,0,-2,-1,'Weil ich der erste Kandidat bin!'),
  (-33,0,-3,0,'Weil ich der erste Kandidat bin!'),
  (-34,0,-1,1,'Weil ich der zweite Kandidat bin!'),
  (-35,0,-2,0,'Weil ich der zweite Kandidat bin!'),
  (-36,0,-3,1,'Weil ich der zweite Kandidat bin!'),
  (-37,0,-1,0,'Weil ich der dritte Kandidat bin!'),
  (-38,0,-2,-1,'Weil ich der dritte Kandidat bin!'),
  (-39,0,-3,1,'Weil ich der dritte Kandidat bin!'),
  (-310,0,-1,1,'Weil ich der vierte Kandidat bin!'),
  (-311,0,-2,-1,'Weil ich der vierte Kandidat bin!'),
  (-312,0,-3,0,'Weil ich der vierte Kandidat bin!');

INSERT INTO mgmt_user (version, username, password, role, candidate_details_email) VALUES
(0,'user','{noop}user','USER','1@mail.de'),
(0,'admin','{noop}admin', 'ADMIN', NULL);

INSERT INTO candidate_answers (answers_id, candidate_email) VALUES
(-31,'1@mail.de'),
(-32,'1@mail.de'),
(-33,'1@mail.de'),
(-34,'2@mail.de'),
(-35,'2@mail.de'),
(-36,'2@mail.de'),
(-37,'3@mail.de'),
(-38,'3@mail.de'),
(-39,'3@mail.de'),
(-310,'4@mail.de'),
(-311,'4@mail.de'),
(-312,'4@mail.de');

INSERT INTO candidate_join (gremium_id, candidate_id) VALUES
('dtg','1@mail.de'),
('dtg','2@mail.de'),
('dtg','3@mail.de'),
('dtg','4@mail.de');

INSERT INTO query_contain (gremium_id, query_id) VALUES
('dtg',-1),
('dtg',-2),
('dtg',-3);

