ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS responsavelAnulacao int;

SELECT create_constraint('ALTER TABLE expedicaodiploma ADD CONSTRAINT fk_expedicaodiploma_responsavelAnulacao FOREIGN KEY (responsavelAnulacao) REFERENCES usuario (codigo) ON UPDATE RESTRICT ON DELETE RESTRICT;');