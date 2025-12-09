CREATE TABLE public.calendarioagrupamentotcc (
	codigo serial NOT NULL,
	created timestamp,
	codigoCreated int,
	nomeCreated varchar(255),
	updated timestamp,
	codigoUpdated int,
	nomeUpdated varchar(255),
	ano varchar(4),
	semestre varchar(1),
	datainicial date,
	datafinal date,
	CONSTRAINT pkey_calendarioagrupamentotcc_codigo PRIMARY KEY (codigo)
);