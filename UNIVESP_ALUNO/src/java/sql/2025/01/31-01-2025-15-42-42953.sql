ALTER table if exists configuracaogeralsistema ADD COLUMN IF NOT EXISTS habilitarintegracaosistemasymplicty BOOLEAN DEFAULT false;
alter table if exists configuracaogeralsistema add column if not exists hostintegracaosistemasymplicty varchar(255) default '';
alter table if exists configuracaogeralsistema add column if not exists userintegracaosistemasymplicty varchar(255)  default '';
alter table if exists configuracaogeralsistema add column if not exists passintegracaosistemasymplicty varchar(255) default '';
alter table if exists configuracaogeralsistema add column if not exists portintegracaosistemasymplicty integer;
alter table if exists configuracaogeralsistema add column if not exists protocolintegracaosistemasymplicty varchar(255) default '';
alter table if exists configuracaogeralsistema add column if not exists pastadestinoremotasymplicty varchar(255) default '';


CREATE TABLE if not exists logjobsymplicty (
    codigo SERIAL PRIMARY KEY,   
    datahora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  
    descricao_erro TEXT,   
    sucesso BOOLEAN NOT NULL  
);
