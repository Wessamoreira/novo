alter table tipodocumento rename idademinima to idade;
alter table tipodocumento add column if not exists tipoidadeexigida  varchar(6); 