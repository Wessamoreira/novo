alter table if exists processomatricula add column if not exists tipoaluno varchar(30);
update processomatricula set tipoaluno = 'AMBOS' where tipoaluno is null;
alter table if exists processomatricula add column if not exists tipousoprocessomatriculaenum varchar(30);