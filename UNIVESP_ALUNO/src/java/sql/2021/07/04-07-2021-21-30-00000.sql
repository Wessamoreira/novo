alter table configuracaoged add column if not exists habilitarIntegracaoCertisign boolean default false;
alter table configuracaoged add column if not exists habilitarIntegracaoImprensaOficial boolean default false;
alter table configuracaoged add column if not exists ambienteImpresaOficialEnum varchar(255) default 'HOMOLOGACAO';
alter table configuracaoged add column if not exists tokenImprensaOficial varchar(255);
alter table configuracaoged add column if not exists urlIntegracaoImprensaOficialHomologacao varchar(255);
alter table configuracaoged add column if not exists urlIntegracaoImprensaOficialProducao varchar(255);
alter table configuracaoGedOrigem add column if not exists provedorAssinaturaPadraoEnum varchar(255) default 'SEI';