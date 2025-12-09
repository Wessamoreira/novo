create table configuracaoataresultadosfinais (
	codigo serial,
	created timestamp,
	codigoCreated int,
	nomeCreated varchar(200),
	updated timestamp,
	codigoupdated int,
	nomeupdated varchar(200),
	
	constraint pk_configuracaoataresultadosfinais_codigo primary key (codigo)
);

create table configuracaolayoutataresultadosfinais(
	codigo serial,
	titulo varchar(200) not null,
	chave varchar(100),
	layoutFixoSistema boolean default false,
	inativarLayout boolean default false,
	layoutPadrao  boolean default false,
	configuracaoataresultadosfinais int not null,
	created timestamp,
	codigoCreated int,
	nomeCreated varchar(200),
	updated timestamp,
	codigoupdated int,
	nomeupdated varchar(200),
	
	constraint pk_configuracaolayoutataresultadosfinais_codigo primary key (codigo),
	
	constraint fk_configuracaolayoutataresultadosfinais_configuracaoataresultadosfinais foreign key (configuracaoataresultadosfinais) references configuracaoataresultadosfinais(codigo) on delete cascade on update  cascade 
);

create index idx_configuracaolayoutataresultadosfinais_configuracaoataresultadosfinais on configuracaolayoutataresultadosfinais(configuracaoataresultadosfinais);
