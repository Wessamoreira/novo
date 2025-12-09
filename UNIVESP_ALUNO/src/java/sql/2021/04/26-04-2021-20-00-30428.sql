CREATE TABLE public.motivospadroesestagio (
	codigo serial NOT NULL,
	descricao varchar(255) NOT NULL,
	statusAtivoInativoEnum varchar(20) DEFAULT 'NENHUM',
	retornoSolicitacaoAproveitamento boolean DEFAULT false,
	indeferimentoSolicitacaoAproveitamento boolean DEFAULT false,
	retornoSolicitacaoEquivalencia boolean DEFAULT false,
	indeferimentoSolicitacaoEquivalencia boolean DEFAULT false,
	retornoAvaliacaoTermo boolean DEFAULT false,
	indeferimentoAvaliacaoTermo boolean DEFAULT false,
	retornoAvaliacaoRelatorioFinal boolean DEFAULT false,
	indeferimentoAvaliacaoRelatorioFinal boolean DEFAULT false,
	retornoAditivos boolean DEFAULT false,
	indeferimentoAditivos boolean DEFAULT false,
	retornoRecisao boolean DEFAULT false,
	indeferimentoRecisao boolean DEFAULT false,
	CONSTRAINT pkey_motivospadroesestagio_codigo PRIMARY KEY (codigo)
);

