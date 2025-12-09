ALTER TABLE IF EXISTS documetacaomatricula 
    ADD COLUMN IF NOT EXISTS motivoindeferimentodocumentoaluno int4 NULL,
    ADD CONSTRAINT fk_documetacaomatricula_motivoindeferimentodocumento FOREIGN KEY (motivoindeferimentodocumentoaluno) REFERENCES motivoindeferimentodocumentoaluno (codigo) ON UPDATE RESTRICT ON DELETE RESTRICT;