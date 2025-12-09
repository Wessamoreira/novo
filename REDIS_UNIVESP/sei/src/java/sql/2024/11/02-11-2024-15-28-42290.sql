ALTER TABLE IF EXISTS tiporequerimento ADD COLUMN IF NOT EXISTS utilizarMensagemDeferimentoExclusivo boolean DEFAULT (FALSE);
ALTER TABLE IF EXISTS tiporequerimento ADD COLUMN IF NOT EXISTS utilizarMensagemIndeferimentoExclusivo boolean DEFAULT (FALSE);

ALTER TABLE IF EXISTS personalizacaomensagemautomatica ADD COLUMN IF NOT EXISTS tipoRequerimento int;
SELECT create_constraint('ALTER TABLE personalizacaomensagemautomatica ADD CONSTRAINT fk_personalizacaomensagemautomatica_tiporequerimento FOREIGN KEY (tipoRequerimento) REFERENCES tipoRequerimento (codigo) ON UPDATE RESTRICT ON DELETE RESTRICT;');