create TABLE usrs (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  login varchar(20) UNIQUE,
  password varchar(10),
  active BOOLEAN

);

INSERT INTO usrs ( login, password ) VALUES ( 'user', 'user');
INSERT INTO usrs ( login, password ) VALUES ( 'admin', 'admin');
INSERT INTO usrs ( login, password ) VALUES ( 'client', 'client');