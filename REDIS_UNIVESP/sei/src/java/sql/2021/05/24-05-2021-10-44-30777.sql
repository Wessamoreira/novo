create table configuracaohistorico (
	codigo serial,
	nivelEducacional varchar(100) not null unique,
	created timestamp,
	codigoCreated int,
	nomeCreated varchar(200),
	updated timestamp,
	codigoupdated int,
	nomeupdated varchar(200),
	
	constraint pk_configuracaohistorico_codigo primary key (codigo)
);

create index idx_configuracaohistorico_nivelEducacional on configuracaohistorico(nivelEducacional);


create table configuracaolayouthistorico (
	codigo serial,
	descricao varchar(200) not null,
	chave varchar(100),
	layoutFixoSistema boolean default false,
	ocultarLayout boolean default false,
	layoutPadrao  boolean default false,
	configuracaoHistorico int not null,
	created timestamp,
	codigoCreated int,
	nomeCreated varchar(200),
	updated timestamp,
	codigoupdated int,
	nomeupdated varchar(200),
	
	constraint pk_configuracaolayouthistorico_codigo primary key (codigo),
	
	constraint fk_configuracaolayouthistorico_configuracaoHistorico foreign key (configuracaoHistorico) references configuracaoHistorico(codigo) on delete cascade on update  cascade 
);

create index idx_configuracaolayouthistorico_configuracaoHistorico on configuracaolayouthistorico(configuracaoHistorico);


create table configuracaoobservacaohistorico (
	codigo serial,
	observacao text not null,
	ocultar boolean default false,
	padrao  boolean default false,
	configuracaoHistorico int not null,
	tipoObservacaoHistorico varchar(50) default 'INTEGRALIZACAO',
	created timestamp,
	codigoCreated int,
	nomeCreated varchar(200),
	updated timestamp,
	codigoupdated int,
	nomeupdated varchar(200),
	
	constraint pk_configuracaoobservacaohistorico_codigo primary key (codigo),
	
	constraint fk_configuracaoobservacaohistorico_configuracaoHistorico foreign key (configuracaoHistorico) references configuracaoHistorico(codigo) on delete cascade on update  cascade 
);

create index idx_configuracaoobservacaohistorico_configuracaoHistorico on configuracaoobservacaohistorico(configuracaoHistorico);


alter table arquivo alter column extensao type varchar(8);
alter table arquivo alter column origem type varchar(50);
alter table arquivo add column tipoRelatorio varchar(10) default 'PDF';