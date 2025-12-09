CREATE TABLE public.configuracaoestagioobrigatorio (
	codigo serial NOT NULL,
	textoOrientacaoAberturaTermo text,
	textoOrientacaoEntregaRelatorioFinal text,
	funcionarioTestemunhaAssinatura1 int,
	funcionarioTestemunhaAssinatura2 int,
	qtdMinimaMantidoPorFacilitador int,
	qtdDiasMaximoParaAnaliseRelatoriofinal int,
	periodicidadeParaNotificacaoEntregaRelatorioFinal int,
	textoOrientacaoAnaliseRelatorioFinal text,
	qtdDiasMaximoParaCorrecaoRelatorioFinal int,
	periodicidadeParaNotificacaoEntregaNovoRelatorioFinal int,
	textoOrientacaoSolicitacaoAproveitamento text,
	qtdDiasMaximoParaRespostaAnaliseAproveitamento int,
	periodicidadeParaNotificacaoAtrasoRespostaAnaliseAproveitamento int,
	qtdDiasMaximoRespostaRetornoAnaliseAproveitamento int,
	periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseAproveitamento int,
	textoOrientacaoSolicitacaoEquivalencia text,
	qtdDiasMaximoParaRespostaAnaliseEquivalencia int,
	periodicidadeParaNotificacaoAtrasoRespostaAnaliseEquivalencia int,
	qtdDiasMaximoRespostaRetornoAnaliseEquivalencia int,
	periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseEquivalencia int,
	CONSTRAINT pkey_configuracaoestagioobrigatorio_codigo PRIMARY KEY (codigo),
	CONSTRAINT fk_ceo_funcionarioTestemunhaAssinatura1 FOREIGN KEY (funcionarioTestemunhaAssinatura1) REFERENCES funcionario(codigo) ON UPDATE RESTRICT ON DELETE RESTRICT,
	CONSTRAINT fk_ceo_funcionarioTestemunhaAssinatura2 FOREIGN KEY (funcionarioTestemunhaAssinatura2) REFERENCES funcionario(codigo) ON UPDATE RESTRICT ON DELETE RESTRICT
);

CREATE TABLE public.configuracaoestagioobrigatoriofuncionario (
	codigo serial NOT NULL,
	configuracaoestagioobrigatorio int NOT NULL,
	funcionario int NOT NULL,
	CONSTRAINT pkey_configuracaoestagioobrigatoriofuncionario_codigo PRIMARY KEY (codigo),
	CONSTRAINT fk_ceof_configuracaoestagioobrigatorio FOREIGN KEY (configuracaoestagioobrigatorio) REFERENCES configuracaoestagioobrigatorio(codigo) ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT fk_ceof_funcionario FOREIGN KEY (funcionario) REFERENCES funcionario(codigo) ON UPDATE RESTRICT ON DELETE RESTRICT
);