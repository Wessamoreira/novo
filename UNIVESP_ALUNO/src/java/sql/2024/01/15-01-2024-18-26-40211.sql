alter table tiporequerimento add column ano varchar(4);
alter table tiporequerimento add column semestre varchar(1);
update tiporequerimento set ano =  '2024', semestre =  '1' where tipo in ('TR', 'TI');