ALTER TABLE configuracaogeralsistema RENAME COLUMN usernamefacilitasms TO usernamesms;
ALTER TABLE configuracaogeralsistema RENAME COLUMN senhafacilitasms TO senhasms;
ALTER TABLE configuracaogeralsistema add column fornecedorSMS varchar(15);
update configuracaogeralsistema set fornecedorSMS = 'FACILITASMS' where fornecedorSMS is null;


update configuracaogeralsistema set usernamesms = t.usuario, senhasms = t.senha, fornecedorSMS = t.tipo from (
select codigo, 
case when (usernamehumamsms is not null and usernamehumamsms <> '') then usernamehumamsms else (
case when (usernamelocasms is not null and usernamelocasms <> '') then usernamelocasms else '' end) end as usuario,
case when (senhahumamsms is not null and senhahumamsms <> '') then senhahumamsms else (
case when (senhalocasms is not null and senhalocasms <> '') then senhalocasms else '' end
) end as senha,
case when (usernamehumamsms is not null and usernamehumamsms <> '') then 'HUMANSMS' else (
case when (usernamelocasms is not null and usernamelocasms <> '') then 'LOCASMS' else 'FACILITASMS' end) end as tipo
 from configuracaogeralsistema
where (usernamesms is null or usernamesms = '') and (senhasms is null or senhasms = '') 
) as t where t.codigo = configuracaogeralsistema.codigo;


alter table if exists sms add column if not exists codigoDest varchar(15);
alter table if exists sms add column if not exists cpfDest varchar(15);
alter table if exists sms add column if not exists matriculaDest varchar(30);

ALTER TABLE configuracaogeralsistema add COLUMN usernamefacilitasms varchar(30);
ALTER TABLE configuracaogeralsistema add COLUMN senhafacilitasms varchar(30);
