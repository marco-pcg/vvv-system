-- db/migration/V1__initial_schema.sql

CREATE TABLE cidade (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    uf VARCHAR(2) NOT NULL
);

CREATE TABLE modal (
    codigo VARCHAR(50) PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL,
    capacidade INT NOT NULL,
    status_operacional BOOLEAN NOT NULL
);

-- Add your other tables (Viagem, Reserva, Pessoa, etc.) here