alter table if exists configuracaoged  add column if not exists provedordeassinaturaenum varchar(100) default 'SEI';
alter table if exists configuracaoged  add column if not exists tipoprovedorassinaturaenum varchar(100) default 'ASSINATURA_DIGITAL';
alter table if exists configuracaoged  add column if not exists usuarioprovedordeassinatura varchar(100);
alter table if exists configuracaoged  add column if not exists senhaprovedordeassinatura varchar(100);
alter table if exists configuracaoged  add column if not exists tokenProvedorDeAssinatura varchar(255);
alter table if exists configuracaoged ALTER COLUMN certificadodigitalunidadeensino DROP NOT NULL;

alter table if exists documentoassinado  add column if not exists provedordeassinaturaenum varchar(100) default 'SEI';
alter table if exists documentoassinado  add column if not exists codigoprovedordeassinatura int;
alter table if exists documentoassinado  add column if not exists chaveprovedordeassinatura varchar(100);
alter table if exists documentoassinado  add column if not exists urlprovedordeassinatura varchar(255);
alter table if exists documentoassinadopessoa  add column if not exists codigoassinatura int;
alter table if exists documentoassinadopessoa  add column if not exists acaoassinatura varchar(100);
alter table if exists documentoassinadopessoa  add column if not exists urlassinatura varchar(255);


 