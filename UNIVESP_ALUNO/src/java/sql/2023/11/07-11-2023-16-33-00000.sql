create table filtroPersonalizado(
	codigo serial not null,
	tituloCampo varchar(150) not null,
	campoQuery text not null,
	campoQueryListaCombobox text,
	tipoCampoFiltro varchar(100) not null,
	origem varchar(70) not null,
	codigoOrigem integer not null,
	ordem integer,
	coluna integer,
	comboboxManual Boolean, 
	created timestamp,
	codigoCreated integer,
	nomeCreated varchar(150),
	updated timestamp,
	codigoUpdated integer,
	nomeUpdated varchar(150),
	primary key(codigo)
);


create table filtroPersonalizadoOpcao(
	codigo serial not null,
	filtroPersonalizado integer not null,
	descricaoOpcao varchar(150),
	keyOpcao varchar(50),
	selecionado boolean default false,
	ordem integer,
	campoQuery text,
	created timestamp,
	codigoCreated integer,
	nomeCreated varchar(150),
	updated timestamp,
	codigoUpdated integer,
	nomeUpdated varchar(150),
	primary key(codigo),
	constraint fk_filtroPersonalizado foreign key (filtroPersonalizado) references filtroPersonalizado(codigo) on update cascade on delete cascade
);
