alter table salaaulablackboard add column nome varchar(400) default '';
alter table salaaulablackboard add column idGrupo varchar(400);
alter table salaaulablackboard add column grupoSetId varchar(200);
alter table salaaulablackboard add column grupoExternalId varchar(200);
alter table salaaulablackboard add column nomeGrupo varchar(400);
alter table salaaulablackboard add column importado boolean default false;
alter table configuracaoseiblackboard add column importacaoEmRealizacao boolean default false;
alter table pessoaemailinstitucional add column nome varchar(250) default '';

ALTER TABLE public.salaaulablackboard drop CONSTRAINT salaaulablackboard_unique_idsalaaulablackboard;

ALTER TABLE public.salaaulablackboard ADD CONSTRAINT salaaulablackboard_unique_id_idgrupo UNIQUE (id, idgrupo);