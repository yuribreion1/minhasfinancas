-- Create database
CREATE DATABASE minhasfinancas;

-- Create main schema
CREATE SCHEMA financas;

-- table responsible to create new user
CREATE TABLE financas.usuario (
  id bigserial NOT NULL PRIMARY KEY,
  nome character varying(150),
  email character varying(100),
  senha character varying(20),
  data_cadastro date default now()
);

-- table responsible to receive to receive new transaction
CREATE TABLE financas.lancamento (
  id bigserial NOT NULL PRIMARY KEY ,
  descricao character varying(100) NOT NULL,
  mes integer NOT NULL,
  ano integer NOT NULL,
  valor numeric(16,2) NOT NULL,
  tipo character varying(20) CHECK (tipo IN ('RECEITA','DESPESA')) NOT NULL,
  status character varying(20) CHECK (status IN ('PENDENTE','CANCELADO','EFETIVADO')) NOT NULL,
  id_usuario bigint REFERENCES financas.usuario (id) NOT NULL,
  data_cadastro date default now()
);