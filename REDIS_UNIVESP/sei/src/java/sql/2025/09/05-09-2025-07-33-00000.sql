ALTER TABLE IF EXISTS configuracaogeralsistema ADD COLUMN IF NOT EXISTS habilitarMonitoramentoSentry boolean DEFAULT (FALSE);
ALTER TABLE IF EXISTS configuracaogeralsistema ADD COLUMN IF NOT EXISTS tokenSentry text;
ALTER TABLE IF EXISTS configuracaogeralsistema ADD COLUMN IF NOT EXISTS habilitarMonitoramentoBlackboardSentry boolean DEFAULT (FALSE);
ALTER TABLE IF EXISTS configuracaogeralsistema ADD COLUMN IF NOT EXISTS tokenBlackboardSentry text;