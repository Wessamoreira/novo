alter table if exists configuracaogeralsistema add column if not exists loginintegracaosoffin varchar(100);
alter table if exists configuracaogeralsistema add column if not exists senhaintegracaosoffin varchar(100);
alter table if exists configuracaogeralsistema add column if not exists tokenintegracaosoffin varchar(100);
alter table if exists configuracaogeralsistema add column if not exists ambienteenumintegracaosoftfin varchar(100) default 'HOMOLOGACAO';



create table if not exists public.loteintegracaosoftfin (
	codigo serial not null,
	"data" timestamp null default now(),
	datainiciorecebimento date,
	datafinalrecebimento date,
	usuario int4 null,
	qtdecontasreceber int4 null,
	nomeusuario varchar null,
	situacao varchar(10) null default 'sucesso'::character varying,
	mensagemerro text null,
	constraint pk_loteintegracaosoftfin_codigo primary key (codigo)
);

create table if not exists public.contarecebersoftfin (
	codigo serial not null,
	estabelecimentoid int,
	cnpjUnidadeEnsino varchar(20),
	cpfCliente varchar(20),
	codigocliente varchar(20),
	dataemissao varchar(10),
	valorbruto numeric(20,2),
	valorliquido numeric(20,2),
	numerodocumento int,
	banco_id int,
	vencimento varchar(10),
	vencimentoprevisto varchar(10),
	observacao varchar(150),
	tipofaturamento int,
	tipolancamento varchar(1),
	referenciaexterna varchar(150),
	descricaounidadenegocio varchar(150),
	datarecebimento varchar(10),
	valorrecebimento numeric(20,2),
	valoradiantamento numeric(20,2),
	historico varchar(50),	
	tipodocumento_id int,
	valorpis numeric(20,2),
	valorcofins numeric(20,2),
	valorcsll numeric(20,2),
	valorirrf numeric(20,2),
	valorinss numeric(20,2),
	valoriss numeric(20,2),
	loteintegracaosoftfin integer not null,
	constraint pk_contarecebersoftfin_codigo primary key (codigo),
	constraint fk_contarecebersoftfin_loteintegracaosoftfin foreign key (loteintegracaosoftfin) references loteintegracaosoftfin(codigo) on update cascade on delete cascade
);

create table if not exists public.centroResultadoOrigemSoftFin (
	codigosei serial not null,
	codigocentroresultado int,
	descricaocentroresultado varchar(250),
	idcentrocustos int,
	historico varchar(50),
	valor numeric(20,2),
	contaReceberSoftFin integer not null,
	constraint pk_centroResultadoOrigemSoftFin_codigoSei primary key (codigoSei),
	constraint fk_centroResultadoOrigemSoftFin_contaReceberSoftFin foreign key (contaReceberSoftFin) references contaReceberSoftFin(codigo) on update cascade on delete cascade
);