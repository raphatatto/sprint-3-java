INSERT INTO usuario (username, password, role, enabled)
VALUES ('operator', '<<BCRYPT_OPERATOR>>', 'ROLE_OPERATOR', 'S');

INSERT INTO usuario (username, password, role, enabled)
VALUES ('manager',  '<<BCRYPT_MANAGER>>',  'ROLE_MANAGER',  'S');
