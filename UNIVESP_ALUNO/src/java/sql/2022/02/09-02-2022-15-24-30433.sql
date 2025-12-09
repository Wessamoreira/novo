create table if not exists public.controlelivroregistrodiplomaunidadeensino (
	codigo serial not null,
	controlelivroregistrodiploma int4 null,
	unidadeensino int4 null,
	created timestamp NULL DEFAULT now(),
	codigocreated int4 NULL,
	nomecreated varchar(200) NULL,
	updated timestamp NULL DEFAULT now(),
	codigoupdated int4 NULL,
	nomeupdated varchar(200) NULL,
	constraint pk_controlelivroregistrodiplomaunidadeensino_codigo primary key (codigo),
	constraint unq_controlelivroregistrodiplomaunidadeensino unique (controlelivroregistrodiploma, unidadeensino),
	constraint fk_controlelivroregistrodiplomaunidadeensino_controlelivroregistrodiploma foreign key (controlelivroregistrodiploma) references controlelivroregistrodiploma(codigo),
	constraint fk_controlelivroregistrodiplomaunidadeensino_unidadeensino foreign key (unidadeensino) references unidadeensino(codigo)
);

insert into controlelivroregistrodiplomaunidadeensino (controlelivroregistrodiploma, unidadeensino) (
select codigo, unidadeensino  from controlelivroregistrodiploma where not exists (
select codigo from 	controlelivroregistrodiplomaunidadeensino where controlelivroregistrodiplomaunidadeensino.unidadeensino = controlelivroregistrodiploma.unidadeensino 
and controlelivroregistrodiplomaunidadeensino.controlelivroregistrodiploma = controlelivroregistrodiploma.codigo
));

alter table if exists controlelivroregistrodiploma drop column if exists unidadeensino;
