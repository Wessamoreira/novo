alter table configuracaogeralsistema
    add if not exists urlLoginAD varchar(255);
alter table configuracaogeralsistema
    add if not exists urlIdentificadorAD varchar(255);