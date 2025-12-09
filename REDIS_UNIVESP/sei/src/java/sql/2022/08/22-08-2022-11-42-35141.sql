ALTER TABLE IF EXISTS NotaFiscalSaida add COLUMN if not exists jsonEnvio  text ;
ALTER TABLE IF EXISTS NotaFiscalSaida add COLUMN if not exists jsonRetornoEnvio  text ;
ALTER TABLE IF EXISTS configuracaonotafiscal add COLUMN if not exists utilizarServicoWebserviceAuxiliar  BOOLEAN DEFAULT FALSE;



