ALTER TABLE IF EXISTS salaaulablackboardoperacao ADD COLUMN IF NOT EXISTS situacaoreprovado boolean default false;
ALTER TABLE IF EXISTS salaaulablackboardoperacao ADD COLUMN IF NOT EXISTS situacaoaprovado boolean default false;
ALTER TABLE IF EXISTS salaaulablackboardoperacao ADD COLUMN IF NOT EXISTS situacaocursando boolean default false;
