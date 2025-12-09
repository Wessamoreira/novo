create table if not exists  procseletivounidadeensinoeixocurso(
	codigo serial not null,
	procseletivounidadeensino int not null,
	eixocurso  int not null,
	nrvagaseixocurso int,
	constraint psueec_pkey primary key (codigo),
	constraint psueec_procseletivounidadeensino_fk foreign key (procseletivounidadeensino) references procseletivounidadeensino(codigo) on update cascade on delete cascade,
	constraint psueec_eixocurso_fk foreign key (eixocurso) references eixocurso(codigo) on update cascade on delete cascade
 );