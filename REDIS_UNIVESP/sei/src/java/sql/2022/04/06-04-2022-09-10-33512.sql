alter table ConfiguracaoGeralSistema add column if not exists  textoOrientacaoCancelamentoPorOutraMatricula  text NULL;
alter table ConfiguracaoGeralSistema add column if not exists  justificativaCancelamentoPorOutraMatricula text NULL;
ALTER TABLE configuracaogeralsistema add column if not exists motivopadraocancelamentooutramatricula int4 NULL;
ALTER TABLE  configuracaogeralsistema ADD CONSTRAINT fk_configuracaogeralsistema_motivopadraocancelamentooutramatricula 
FOREIGN KEY (motivopadraocancelamentooutramatricula) REFERENCES motivocancelamentotrancamento(codigo)ON UPDATE cascade ON DELETE cascade;





