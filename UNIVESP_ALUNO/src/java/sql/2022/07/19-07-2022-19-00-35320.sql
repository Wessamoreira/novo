create table if not exists  calendarioAgrupamentoDisciplina(
 codigo serial,
 disciplina int not null,
 calendarioAgrupamento int not null,
 created timestamp,
 codigoCreated int,
 nomeCreated varchar(50),
 updated timestamp,
 codigoupdated int,
 nomeupdated varchar(50),
 
 constraint pk_calendarioAgrupamentoDisciplina_codigo  primary key (codigo),
 constraint unq_calendarioAgrupamentoDisciplina unique (disciplina, calendarioAgrupamento),
 constraint fk_calendarioAgrupamentoDisciplina_discplina foreign key (disciplina) references disciplina(codigo) on update cascade,
 constraint fk_calendarioAgrupamentoDisciplina_calendarioAgrupamento foreign key (calendarioAgrupamento) references calendarioAgrupamentoTcc(codigo) on delete cascade
 
);

alter table if exists calendarioAgrupamentoTcc drop constraint if exists unique_calendarioagrupamentotcc_ano_semestre_classificacaoagrup;