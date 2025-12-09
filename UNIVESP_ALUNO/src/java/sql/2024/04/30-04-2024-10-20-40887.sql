ALTER TABLE IF EXISTS configuracaogeralsistema 
	ADD COLUMN IF NOT EXISTS limitedestinatariosporemail int DEFAULT 0,
	ADD COLUMN IF NOT EXISTS tamanholimiteanexoemail int DEFAULT 0;
ALTER TABLE IF EXISTS email ALTER COLUMN emaildest TYPE TEXT;
ALTER TABLE IF EXISTS email	ADD COLUMN IF NOT EXISTS multiplosdestinatarios bool DEFAULT FALSE;