alter table if exists agentenegativacaocobrancacontareceber  add column if not exists credorunidadeensino int;
alter table agentenegativacaocobrancacontareceber ADD CONSTRAINT fk_agentenegativacaocobrancacontareceber_unidadeensino FOREIGN KEY (credorunidadeensino)   references unidadeensino(codigo) on update restrict on delete restrict;
 