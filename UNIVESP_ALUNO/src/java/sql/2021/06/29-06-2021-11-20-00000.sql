CREATE TABLE public.questionarioRespostaOrigemMotivosPadroesEstagio (
	codigo serial NOT NULL,
	questionarioRespostaOrigem int NOT NULL,
	motivosPadroesEstagio int NOT NULL,
	CONSTRAINT pkey_questionarioRespostaOrigemMotivosPadroesEstagio_codigo PRIMARY KEY (codigo),
	CONSTRAINT fk_questionarioRespostaOrigemMotivosPadroesEstagio_questionarioRespostaOrigem FOREIGN KEY (questionarioRespostaOrigem) REFERENCES questionarioRespostaOrigem(codigo) ON UPDATE RESTRICT ON DELETE RESTRICT,
	CONSTRAINT fk_questionarioRespostaOrigemMotivosPadroesEstagio_motivosPadroesEstagio FOREIGN KEY (motivosPadroesEstagio) REFERENCES motivosPadroesEstagio(codigo) ON UPDATE CASCADE ON DELETE CASCADE
);
