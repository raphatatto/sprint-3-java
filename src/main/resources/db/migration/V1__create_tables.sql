CREATE TABLE moto (
  id SERIAL PRIMARY KEY,
  placa VARCHAR(10) NOT NULL UNIQUE,
  modelo VARCHAR(60),
  cor VARCHAR(30),
  status VARCHAR(20) NOT NULL
);

CREATE INDEX idx_moto_placa ON moto(placa);

CREATE TABLE vaga (
  id SERIAL PRIMARY KEY,
  codigo VARCHAR(10) NOT NULL UNIQUE,
  setor VARCHAR(20),
  status VARCHAR(20) NOT NULL
);

CREATE INDEX idx_vaga_status ON vaga(status);
CREATE INDEX idx_vaga_codigo ON vaga(codigo);

CREATE TABLE alocacao (
  id SERIAL PRIMARY KEY,
  moto_id BIGINT NOT NULL REFERENCES moto(id),
  vaga_id BIGINT NOT NULL REFERENCES vaga(id),
  inicio TIMESTAMP NOT NULL,
  fim TIMESTAMP NULL,
  ativa BOOLEAN NOT NULL
);

CREATE INDEX idx_aloc_moto ON alocacao(moto_id);
CREATE INDEX idx_aloc_vaga ON alocacao(vaga_id);
CREATE INDEX idx_aloc_ativa ON alocacao(ativa);

-- Usuários e perfis (Spring Security)
CREATE TABLE usuario (
  id SERIAL PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  role VARCHAR(20) NOT NULL,
  enabled BOOLEAN NOT NULL
);

CREATE INDEX idx_usuario_username ON usuario(username);
