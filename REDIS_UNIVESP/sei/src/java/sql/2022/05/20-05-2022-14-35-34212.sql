create table if not exists  censounidadeensino(
	codigo serial not null,
	unidadeensino int not null,
	censo int not null,
	created timestamp,
	codigocreated int,
	nomeCreated varchar(500),
	updated timestamp,
	codigoupdated int,
	nomeupdated varchar(500),
	
	constraint pk_censounidadeensino primary key (codigo),
	constraint fk_censounidadeensino_unidadeensino foreign key (unidadeensino) references unidadeensino(codigo),
	constraint fk_censounidadeensino_censo foreign key (censo) references censo(codigo) on update cascade on delete cascade,
	constraint unq_censounidadeensino_censo unique (censo, unidadeensino)
 );