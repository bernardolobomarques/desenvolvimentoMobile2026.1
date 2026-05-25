CREATE DATABASE IF NOT EXISTS energy_monitor
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE energy_monitor;

CREATE TABLE IF NOT EXISTS devices (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  type VARCHAR(50) NOT NULL,
  is_active BOOLEAN NOT NULL DEFAULT FALSE
);

INSERT INTO devices (name, type, is_active) VALUES
  ('Ar Condicionado Sala', 'Climatizacao', TRUE),
  ('Lampada Cozinha', 'Iluminacao', FALSE),
  ('Geladeira', 'Eletrodomestico', TRUE),
  ('Televisao', 'Entretenimento', FALSE),
  ('Computador Escritório', 'Informatica', TRUE);
