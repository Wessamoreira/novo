alter table if exists salaaulablackboardoperacao add column if not exists msgnotificacaoexecutada boolean default false;
alter table if exists salaaulablackboardoperacao add column if not exists erromsgnotificacao text;

