alter table salaaulablackboard add column if not exists tipoSalaAulaBlackboardEnum varchar(255) default 'DISCIPLINA';
alter table salaaulablackboard add column if not exists nrSala int;
alter table salaaulablackboard add column if not exists curso int;
alter table salaaulablackboard add column if not exists gradeCurricularEstagio int;

alter table salaaulablackboard add constraint fk_salaaulablackboard_curso foreign key (curso) references curso(codigo) on delete restrict on update restrict;
alter table salaaulablackboard add constraint fk_salaaulablackboard_gradeCurricularEstagio foreign key (gradeCurricularEstagio) references gradeCurricularEstagio(codigo) on delete restrict on update restrict;
 

CREATE TABLE public.salaaulablackboardpessoa (
	codigo serial NOT NULL,
	created timestamp,
	codigoCreated int,
	nomeCreated varchar(255),
	updated timestamp,
	codigoUpdated int,
	nomeUpdated varchar(255),
	salaaulablackboard int NOT NULL,
	pessoaemailinstitucional int ,
	matricula varchar(50),
	matriculaperiodoturmadisciplina int ,
	tiposalaaulablackboardpessoaenum varchar(50) NOT NULL DEFAULT 'NENHUM',
	CONSTRAINT pkey_salaaulablackboardpessoa_codigo PRIMARY KEY (codigo),
	CONSTRAINT fk_salaaulablackboardpessoa_pessoaemailinstitucional FOREIGN KEY (pessoaemailinstitucional) REFERENCES pessoaemailinstitucional(codigo) ON UPDATE RESTRICT ON DELETE RESTRICT,
	CONSTRAINT fk_salaaulablackboardpessoa_matriculaperiodoturmadisciplina FOREIGN KEY (matriculaperiodoturmadisciplina) REFERENCES matriculaperiodoturmadisciplina(codigo) ON UPDATE RESTRICT ON DELETE RESTRICT,
	CONSTRAINT fk_salaaulablackboardpessoa_salaaulablackboard FOREIGN KEY (salaaulablackboard) REFERENCES salaaulablackboard(codigo) ON UPDATE CASCADE ON DELETE CASCADE
);