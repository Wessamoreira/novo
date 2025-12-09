create table LogRegistroOperacoes
(
	codigo serial not null,
	tabelaLogRegistroOperacoes varchar (150) not null,
	operacaoLogRegistroOperacoes varchar (150) not null,
	responsavel integer not null,
	dataOperacao timestamp not null,
	row_data json not null,
	changed_fields json not null,
	observacao text,
	primary key(codigo)
);