select  
exec ('alter table ' ||  relname || ' add column if not exists created timestamp;'),
exec ('alter table ' ||  relname || ' add column if not exists codigoCreated int;'),
exec ('alter table ' ||  relname || ' add column if not exists nomeCreated varchar(255);'),
exec ('alter table ' ||  relname || ' add column if not exists updated timestamp;'),
exec ('alter table ' ||  relname || ' add column if not exists codigoUpdated int;'),
exec ('alter table ' ||  relname || ' add column if not exists nomeUpdated varchar(255);')
from pg_catalog.pg_stat_user_tables
where schemaname = 'public' order by relname 