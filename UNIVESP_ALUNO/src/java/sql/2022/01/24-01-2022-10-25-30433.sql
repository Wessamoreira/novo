create table if not exists programacaoformaturaunidaensino (
	codigo serial,
	programacaoformatura int,
	unidadeensino int,
	created timestamp NULL DEFAULT now(),
	codigocreated int4 NULL,
	nomecreated varchar(200) NULL,
	updated timestamp NULL DEFAULT now(),
	codigoupdated int4 NULL,
	nomeupdated varchar(200) NULL,
	constraint pk_programacaoformaturaunidaensino_codigo primary key (codigo), 
	constraint fk_programacaoformaturaunidaensino_programacaoformatura foreign key(programacaoformatura) references programacaoformatura(codigo),	
	constraint fk_programacaoformaturaunidaensino_unidadeensino foreign key(unidadeensino) references unidadeensino(codigo),
	constraint unq_programacaoformaturaunidaensino unique (programacaoformatura, unidadeensino)
);

insert into programacaoformaturaunidaensino (programacaoformatura, unidadeensino) (
select codigo, unidadeensino  from programacaoformatura where not exists (
select codigo from 	programacaoformaturaunidaensino where programacaoformaturaunidaensino.unidadeensino = programacaoformatura.unidadeensino 
and programacaoformaturaunidaensino.programacaoformatura = programacaoformatura.codigo
));

alter table if exists programacaoformatura drop column if exists unidadeensino;

alter table if exists programacaoformatura add column if not exists colacaograu int4 null;
alter table if exists programacaoformatura add FOREIGN KEY (colacaograu) REFERENCES colacaograu(codigo) ON UPDATE RESTRICT ON DELETE restrict;

alter table if exists programacaoformatura add column if not exists niveleducacional varchar(2);

alter table if exists programacaoformaturaaluno drop column if exists situacaofinanceira;

alter table if exists programacaoformaturaaluno drop column if exists situacaoacademica;

alter table if exists programacaoformaturaaluno drop column if exists situacaodocumentacao;

alter table if exists programacaoformaturaaluno drop column if exists dataconclusaocurso;

update programacaoformatura set niveleducacional = programacaoformaturaaluno.niveleducacional, colacaograu = programacaoformaturaaluno.colacaograu 
from (select distinct pfa.programacaoformatura, pfa.colacaograu, c.niveleducacional from programacaoformaturaaluno as pfa
inner join matricula m on m.matricula = pfa.matricula
inner join curso c on c.codigo = m.curso
) as programacaoformaturaaluno where programacaoformaturaaluno.programacaoformatura = programacaoformatura.codigo;

alter table if exists documentoassinado add column if not exists curso int;
alter table if exists documentoassinado add FOREIGN KEY (curso) REFERENCES curso(codigo) ON UPDATE RESTRICT ON DELETE restrict;

alter table if exists documentoassinado add column if not exists programacaoformatura int;	
alter table if exists documentoassinado add FOREIGN KEY (programacaoformatura) REFERENCES programacaoformatura(codigo) ON UPDATE RESTRICT ON DELETE restrict;

alter table if exists configuracaogedorigem add column if not exists permitiralunoassinardigitalmente boolean default(false);