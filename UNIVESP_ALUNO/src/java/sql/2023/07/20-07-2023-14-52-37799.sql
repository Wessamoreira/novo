ALTER TABLE IF EXISTS requerimento ADD COLUMN IF NOT EXISTS grupoFacilitador int DEFAULT(NULL);
ALTER TABLE IF EXISTS requerimento ADD COLUMN IF NOT EXISTS temaTccFacilitador varchar(255);
ALTER TABLE IF EXISTS requerimento ADD COLUMN IF NOT EXISTS assuntoTccFacilitador varchar(255);
ALTER TABLE IF EXISTS requerimento ADD COLUMN IF NOT EXISTS avaliadorExternoFacilitador varchar(255);

SELECT create_constraint('ALTER TABLE requerimento ADD CONSTRAINT fk_requerimento_grupoFacilitador FOREIGN KEY (grupoFacilitador) REFERENCES salaaulablackboard (codigo) ON UPDATE RESTRICT ON DELETE RESTRICT;');