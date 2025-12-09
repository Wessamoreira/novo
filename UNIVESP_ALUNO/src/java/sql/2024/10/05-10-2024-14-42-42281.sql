ALTER TABLE IF EXISTS documentoassinado ADD COLUMN IF NOT EXISTS erro boolean DEFAULT (FALSE), ADD COLUMN IF NOT EXISTS motivoErro TEXT DEFAULT (NULL);
ALTER TABLE IF EXISTS configuracaodiplomadigital ADD COLUMN IF NOT EXISTS tipoLayoutHistoricoGraduacao varchar(100);
ALTER TABLE IF EXISTS configuracaodiplomadigital ADD COLUMN IF NOT EXISTS tipoLayoutHistoricoGraduacaoTecnologica varchar(100);
ALTER TABLE IF EXISTS configuracaodiplomadigital ADD COLUMN IF NOT EXISTS apresentarApenasUltimoHistoricoDisciplina boolean;
ALTER TABLE IF EXISTS configuracaodiplomadigital ADD COLUMN IF NOT EXISTS considerarCargaHorariaCursadaIgualCargaHorariaPrevista boolean