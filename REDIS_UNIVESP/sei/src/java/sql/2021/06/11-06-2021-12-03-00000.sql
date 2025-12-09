alter table configuracaogeralsistema drop column if exists urlLoginAD;
alter table configuracaogeralsistema drop column if exists urlIdentificadorAD;

alter table configuracaoldap add if not exists urlLoginAD varchar(255);
alter table configuracaoldap add if not exists urlIdentificadorAD varchar(255);