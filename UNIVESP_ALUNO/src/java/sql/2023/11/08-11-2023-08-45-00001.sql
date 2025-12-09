ALTER TABLE IF EXISTS ConfiguracaoSeiBlackboard ADD COLUMN IF NOT EXISTS ativaroperacaoensalamentoestagio  boolean default false;
ALTER TABLE IF EXISTS ConfiguracaoSeiBlackboard ADD COLUMN IF NOT EXISTS ativaroperacaoensalamentotcc  boolean default false;
