create table if not exists  periodoChamadaProcSeletivo(
	codigo serial not null,
	nrChamada int,
	procseletivo int not null,
	periodoInicialChamada timestamp not NULL,
	periodoFinalChamada timestamp not NULL,
	created timestamp NULL,
	codigocreated int4 NULL,
	nomecreated varchar(255) NULL,
	updated timestamp NULL,
	codigoupdated int4 NULL,
	nomeupdated varchar(255) NULL,
	constraint pcps_pkey primary key (codigo),
	constraint pcps_procseletivo_fk foreign key (procseletivo) references procseletivo(codigo) on update cascade on delete cascade
 );