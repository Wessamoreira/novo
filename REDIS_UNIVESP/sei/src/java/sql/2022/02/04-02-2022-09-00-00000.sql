create table if not exists agrupamentounidadeensino (
	 codigo serial,
	 descricao varchar(250),
	 abreviatura varchar(50),
	 statusAtivoInativoEnum varchar(50) default 'ATIVO',
	 created timestamp,
	 codigocreated int,
	 nomecreated varchar(250),
	 updated timestamp,
	 codigoupdated int,
	 nomeupdated varchar(250),
	 constraint pk_agrupamentounidadeensino primary key (codigo)
);

create table if not exists agrupamentounidadeensinoitem (
	 codigo serial,
	 agrupamentounidadeensino int,
	 unidadeensino int,
	 created timestamp,
	 codigocreated int,
	 nomecreated varchar(250),
	 updated timestamp,
	 codigoupdated int,
	 nomeupdated varchar(250),
	 constraint pk_agrupamentounidadeensinoitem primary key (codigo),
	 constraint fk_agrupamentounidadeensinoitem_agrupamentounidadeensino FOREIGN KEY (agrupamentounidadeensino) REFERENCES agrupamentounidadeensino(codigo) ON UPDATE CASCADE ON DELETE CASCADE,
	 constraint fk_agrupamentounidadeensinoitem_unidadeensino FOREIGN KEY (unidadeensino) REFERENCES unidadeensino(codigo) ON UPDATE RESTRICT ON DELETE RESTRICT
);

alter table if exists salaaulablackboard add column if not exists agrupamentounidadeensino int;
select create_constraint('alter table salaaulablackboard add constraint fk_salaaulablackboard_agrupamentounidadeensino foreign key (agrupamentounidadeensino) references agrupamentounidadeensino (codigo) on update restrict on delete restrict');
