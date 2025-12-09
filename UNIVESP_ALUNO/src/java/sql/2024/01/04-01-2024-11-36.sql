create table IF NOT EXISTS cidtiporequerimento (
	codigo serial PRIMARY key,
	codcid varchar(50),
	descricao varchar (200),
	tiporequerimento integer
	);
	
ALTER TABLE cidtiporequerimento ADD CONSTRAINT fk_tiporequerimento FOREIGN KEY (tiporequerimento) REFERENCES tiporequerimento(codigo) ON UPDATE RESTRICT ON DELETE restrict;

alter table requerimento add column codigocid integer;
ALTER table requerimento ADD CONSTRAINT fk_codigocid FOREIGN KEY (codigocid) REFERENCES cidtiporequerimento(codigo) ON UPDATE RESTRICT ON DELETE restrict;
	
create table IF NOT EXISTS requerimentocidtiporequerimento (
	codigo serial PRIMARY key,
	cidtiporequerimento integer,
	requerimento integer,
	tiporequerimento integer
);

ALTER TABLE requerimentocidtiporequerimento ADD CONSTRAINT fk_tiporequerimento FOREIGN KEY (tiporequerimento) REFERENCES tiporequerimento(codigo) ON UPDATE RESTRICT ON DELETE restrict;

ALTER TABLE requerimentocidtiporequerimento ADD CONSTRAINT fk_requerimento FOREIGN KEY (requerimento) REFERENCES requerimento(codigo) ON UPDATE RESTRICT ON DELETE restrict;

ALTER TABLE requerimentocidtiporequerimento ADD CONSTRAINT fk_cidtiporequerimento FOREIGN KEY (cidtiporequerimento) REFERENCES cidtiporequerimento(codigo) ON UPDATE RESTRICT ON DELETE restrict;


