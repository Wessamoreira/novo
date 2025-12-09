CREATE TABLE if not exists linksuteis (
	codigo serial NOT NULL,
	descricao varchar(100) NOT null,
	link varchar(100) not null,
	icone varchar(100)not null,
	aluno boolean default(false),
	professor boolean default(false),
	coordenador boolean default(false),
	administrativo boolean default(false),
	created timestamp,
	updated timestamp,
	codigoCreated int,
	codigoUpdated int,
	nomeCreated varchar,
	nomeUpdated varchar,
	constraint  pk_linksuteis_codigo primary key (codigo)
);



create table if not exists usuariolinksuteis (
	codigo serial,
	usuario integer, 
	linksuteis integer,
	created timestamp,
	updated timestamp,
	codigoCreated int,
	codigoUpdated int,
	nomeCreated varchar,
	nomeUpdated varchar,
	constraint pk_usuariolinksuteis_codigo primary key (codigo),
	constraint fk_usuariolinksuteis_usuario foreign key (usuario) references usuario(codigo) on delete cascade,
	constraint fk_Usuariolinksuteis_linksuteis foreign key (linksuteis) references linksuteis(codigo) on delete cascade
);
