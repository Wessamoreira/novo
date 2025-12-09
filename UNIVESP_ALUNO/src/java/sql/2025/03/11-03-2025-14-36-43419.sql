CREATE TABLE bloqueiomatricula (
    codigo SERIAL PRIMARY KEY, 
    matricula VARCHAR(20) NOT NULL,
    CONSTRAINT fk_matricula FOREIGN KEY (matricula) REFERENCES matricula(matricula)
);

