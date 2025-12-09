create table if not exists public.OcorrenciaLGPD(
	codigo serial not null,
	dataCadastro timestamp,
	tipoOcorrencia varchar,
	pessoa int,
	created timestamp,
	updated timestamp,
	codigoCreated int,
	codigoUpdated int,
	nomeCreated varchar,
	nomeUpdated varchar,
	constraint ocorrenciaLGPD_pk primary key (codigo),
	constraint fk_pessoa foreign key (pessoa) references pessoa(codigo)
);


