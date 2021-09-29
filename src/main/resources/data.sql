INSERT INTO gremium (version, name, abbr, description) VALUES
  (0, 'Das Test Gremium', 'dtg', 'Das erste Test Gremium der Welt!'),
  (0, 'CSV Query Upload Test','cqut', 'Ein Gremium zum Testen der Query-CSV Upload Funktionen.');

INSERT INTO query (id, version, text) VALUES
  (-1,0,'Dieser Aussage soll zugestimmt werden.'),
  (-2,0,'Dieser Aussage soll nicht zugestimmt werden.'),
  (-3,0,'Gegenüber dieser Frage sollte man neutral eingestimmt sein.');

INSERT INTO candidate (id, version, firstname, lastname, email) VALUES
(-20,0,'Darth','Vader','1@mail.de'),
(-21,0,'Kim','Kardashian','2@mail.de'),
(-22,0,'Robert','McClanahan','3@mail.de'),
(-23,0,'Virginia','Davis','4@mail.de');

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

INSERT INTO mgmt_user (version, username, password, role, details_id) VALUES
(0,'user','{noop}user','USER',-20),
(0,'admin','{noop}admin', 'ADMIN', NULL);

INSERT INTO candidate_answers (answers_id, candidate_id) VALUES
(-31,-21),
(-32,-21),
(-33,-21),
(-34,-22),
(-35,-22),
(-36,-22),
(-37,-23),
(-38,-23),
(-39,-23);

INSERT INTO candidate_join (gremium_id, candidate_id) VALUES
('dtg',-20),
('dtg',-21),
('dtg', -22);

INSERT INTO query_contain (gremium_id, query_id) VALUES
('dtg',-1),
('dtg',-2),
('dtg',-3);

