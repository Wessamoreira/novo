CREATE TABLE public.gradecurricularestagio (
	codigo serial NOT NULL,
	gradecurricular int NOT NULL,
	textoPadraoDeclaracao int ,
	questionarioRelatorioFinal int ,
	questionarioAproveitamentoPorDocenteRegular int ,
	questionarioAproveitamentoPorLicenciatura int ,
	questionarioEquivalencia int ,
	nome varchar(255),
	gradeCurricularEstagioAreaEnum varchar(20) NOT NULL DEFAULT 'DOCENCIA',
	gradeCurricularEstagioQuestionarioEnum varchar(20) NOT NULL DEFAULT 'NENHUM',
	cargaHorarioObrigatorio int,
	horaMaximaAproveitamentoOuEquivalencia int,
	CONSTRAINT pkey_gradecurricularestagio_codigo PRIMARY KEY (codigo),
	CONSTRAINT fk_gradecurricularestagio_textoPadraoDeclaracao FOREIGN KEY (textoPadraoDeclaracao) REFERENCES textoPadraoDeclaracao(codigo) ON UPDATE RESTRICT ON DELETE RESTRICT,
	CONSTRAINT fk_gradecurricularestagio_questionarioRelatorioFinal FOREIGN KEY (questionarioRelatorioFinal) REFERENCES questionario(codigo) ON UPDATE RESTRICT ON DELETE RESTRICT,
	CONSTRAINT fk_gradecurricularestagio_questionarioAproveitamentoPorDocenteRegular FOREIGN KEY (questionarioAproveitamentoPorDocenteRegular) REFERENCES questionario(codigo) ON UPDATE RESTRICT ON DELETE RESTRICT,
	CONSTRAINT fk_gradecurricularestagio_questionarioAproveitamentoPorLicenciatura FOREIGN KEY (questionarioAproveitamentoPorLicenciatura) REFERENCES questionario(codigo) ON UPDATE RESTRICT ON DELETE RESTRICT,
	CONSTRAINT fk_gradecurricularestagio_questionarioEquivalencia FOREIGN KEY (questionarioEquivalencia) REFERENCES questionario(codigo) ON UPDATE RESTRICT ON DELETE RESTRICT,
	CONSTRAINT fk_gradecurricularestagio_gradecurricular FOREIGN KEY (gradecurricular) REFERENCES gradecurricular(codigo) ON UPDATE CASCADE ON DELETE CASCADE
);

alter table gradecurricular add column if not exists percentualPermitirIniciarEstagio int;