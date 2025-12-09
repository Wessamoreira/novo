ALTER TABLE IF EXISTS estagio ADD COLUMN IF NOT EXISTS docenteResponsavelEstagio int;

SELECT
	create_constraint('ALTER TABLE estagio ADD CONSTRAINT fk_estagio_docenteResponsavelEstagio FOREIGN KEY (docenteResponsavelEstagio) REFERENCES pessoa (codigo) ON UPDATE RESTRICT ON DELETE RESTRICT;');

ALTER TABLE IF EXISTS gradecurricularestagio ADD COLUMN IF NOT EXISTS docenteResponsavelEstagio int;

SELECT
	create_constraint('ALTER TABLE gradecurricularestagio ADD CONSTRAINT fk_gradecurricularestagio_docenteResponsavelEstagio FOREIGN KEY (docenteResponsavelEstagio) REFERENCES pessoa (codigo) ON UPDATE RESTRICT ON DELETE RESTRICT;');

ALTER TABLE IF EXISTS layoutpadrao ADD COLUMN IF NOT EXISTS usuario int;

ALTER TABLE IF EXISTS layoutpadrao DROP CONSTRAINT IF EXISTS unique_layoutpadrao_entidade_campo_agrup;

ALTER TABLE layoutpadrao ADD CONSTRAINT unique_layoutpadrao_entidade_campo_agrup UNIQUE (entidade, campo, agrupador, usuario);

SELECT 
	create_constraint('ALTER TABLE layoutpadrao ADD CONSTRAINT unique_layoutpadrao_entidade_campo_agrup UNIQUE (entidade, campo, agrupador, usuario);');

ALTER TABLE IF EXISTS layoutpadrao ADD COLUMN IF NOT EXISTS assinaturaFunc3 int4;

ALTER TABLE IF EXISTS layoutpadrao ADD COLUMN IF NOT EXISTS assinaturaFunc4 int4;

ALTER TABLE IF EXISTS layoutpadrao ADD COLUMN IF NOT EXISTS assinaturaFunc5 int4;

ALTER TABLE IF EXISTS layoutpadrao ADD COLUMN IF NOT EXISTS tituloAssinaturaFunc3 varchar(100);

ALTER TABLE IF EXISTS layoutpadrao ADD COLUMN IF NOT EXISTS tituloAssinaturaFunc4 varchar(100);

ALTER TABLE IF EXISTS layoutpadrao ADD COLUMN IF NOT EXISTS tituloAssinaturaFunc5 varchar(100);

ALTER TABLE IF EXISTS layoutpadrao ADD COLUMN IF NOT EXISTS nomecargo3Apresentar varchar(75);

ALTER TABLE IF EXISTS layoutpadrao ADD COLUMN IF NOT EXISTS nomecargo4Apresentar varchar(75);

ALTER TABLE IF EXISTS layoutpadrao ADD COLUMN IF NOT EXISTS nomecargo5Apresentar varchar(75);

CREATE TABLE IF NOT EXISTS configuracaoDiplomaDigital (
	codigo serial4 NOT NULL,
	dataCadastro timestamp,
	descricao varchar(100) NOT NULL,
	padrao boolean DEFAULT(FALSE),
	utilizarCoordenadorCursoAtividadeComplementar boolean DEFAULT(FALSE),
	coordenadorCursoDisciplinasAproveitadas varchar(25),
	unidadeEnsinoPadrao int4,
	layoutGraduacaoPadrao varchar(50),
	layoutGraduacaoTecnologicaPadrao varchar(50),
	textoPadraoGraduacaoPadrao int4,
	textoPadraoGraduacaoTecnologicaPadrao int4,
	funcionarioPrimario int4,
	cargoFuncionarioPrimario int4,
	tituloFuncionarioPrimario varchar(40),
	funcionarioSecundario int4,
	cargoFuncionarioSecundario int4,
	tituloFuncionarioSecundario varchar(40),
	funcionarioTerceiro int4,
	cargoFuncionarioTerceiro int4,
	tituloFuncionarioTerceiro varchar(40),
	funcionarioQuarto int4,
	cargoFuncionarioQuarto int4,
	tituloFuncionarioQuarto varchar(40),
	funcionarioQuinto int4,
	cargoFuncionarioQuinto int4,
	tituloFuncionarioQuinto varchar(40),
	utilizarCampoPeriodoDisciplina varchar(20),
	created timestamp NULL,
	codigocreated int4 NULL,
	nomecreated varchar(255) NULL,
	updated timestamp NULL,
	codigoupdated int4 NULL,
	nomeupdated varchar(255) NULL,
	CONSTRAINT configuracaoDiplomaDigital_pkey PRIMARY KEY (codigo),
	CONSTRAINT fk_configuracaoDiplomaDigital_funcionarioPrimario FOREIGN KEY (funcionarioPrimario) REFERENCES public.funcionario(codigo) ON
DELETE
	RESTRICT ON
	UPDATE
		RESTRICT,
		CONSTRAINT fk_configuracaoDiplomaDigital_funcionarioSecundario FOREIGN KEY (funcionarioSecundario) REFERENCES public.funcionario(codigo) ON
		DELETE
			RESTRICT ON
			UPDATE
				RESTRICT,
				CONSTRAINT fk_configuracaoDiplomaDigital_funcionarioTerceiro FOREIGN KEY (funcionarioTerceiro) REFERENCES public.funcionario(codigo) ON
				DELETE
					RESTRICT ON
					UPDATE
						RESTRICT,
						CONSTRAINT fk_configuracaoDiplomaDigital_funcionarioQuarto FOREIGN KEY (funcionarioQuarto) REFERENCES public.funcionario(codigo) ON
						DELETE
							RESTRICT ON
							UPDATE
								RESTRICT,
								CONSTRAINT fk_configuracaoDiplomaDigital_funcionarioQuinto FOREIGN KEY (funcionarioQuinto) REFERENCES public.funcionario(codigo) ON
								DELETE
									RESTRICT ON
									UPDATE
										RESTRICT,
										CONSTRAINT fk_configuracaoDiplomaDigital_cargoFuncionarioPrimario FOREIGN KEY (cargoFuncionarioPrimario) REFERENCES public.cargo(codigo) ON
										DELETE
											RESTRICT ON
											UPDATE
												RESTRICT,
												CONSTRAINT fk_configuracaoDiplomaDigital_cargofuncionariosecundario FOREIGN KEY (cargofuncionarioSecundario) REFERENCES public.cargo(codigo) ON
												DELETE
													RESTRICT ON
													UPDATE
														RESTRICT,
														CONSTRAINT fk_configuracaoDiplomaDigital_cargofuncionarioterceiro FOREIGN KEY (cargofuncionarioTerceiro) REFERENCES public.cargo(codigo) ON
														DELETE
															RESTRICT ON
															UPDATE
																RESTRICT,
																CONSTRAINT fk_configuracaoDiplomaDigital_cargoFuncionarioQuarto FOREIGN KEY (cargoFuncionarioQuarto) REFERENCES public.cargo(codigo) ON
																DELETE
																	RESTRICT ON
																	UPDATE
																		RESTRICT,
																		CONSTRAINT fk_configuracaoDiplomaDigital_cargoFuncionarioQuinto FOREIGN KEY (cargoFuncionarioQuinto) REFERENCES public.cargo(codigo) ON
																		DELETE
																			RESTRICT ON
																			UPDATE
																				RESTRICT,
																				CONSTRAINT fk_configuracaoDiplomaDigital_unidadeEnsinoPadrao FOREIGN KEY (unidadeEnsinoPadrao) REFERENCES public.unidadeensino(codigo) ON
																				DELETE
																					RESTRICT ON
																					UPDATE
																						RESTRICT,
																						CONSTRAINT fk_configuracaoDiplomaDigital_textoPadraoGraduacaoPadrao FOREIGN KEY (textoPadraoGraduacaoPadrao) REFERENCES public.textopadraodeclaracao(codigo) ON
																						DELETE
																							RESTRICT ON
																							UPDATE
																								RESTRICT,
																								CONSTRAINT fk_configuracaoDiplomaDigital_textoPadraoGraduacaoTecnologicaPadrao FOREIGN KEY (textoPadraoGraduacaoTecnologicaPadrao) REFERENCES public.textopadraodeclaracao(codigo) ON
																								DELETE
																									RESTRICT ON
																									UPDATE
																										RESTRICT
);

ALTER TABLE IF EXISTS configuracaodiplomadigital ADD COLUMN IF NOT EXISTS apresentarTextoEnade boolean DEFAULT(FALSE);

ALTER TABLE IF EXISTS configuracaodiplomadigital ADD COLUMN IF NOT EXISTS horaRelogio int;

ALTER TABLE IF EXISTS configuracaoDiplomaDigital ADD COLUMN IF NOT EXISTS versao varchar(15);

ALTER TABLE IF EXISTS configuracaodiplomadigital ADD COLUMN IF NOT EXISTS historicoConsiderarAprovado boolean DEFAULT(TRUE);

ALTER TABLE IF EXISTS configuracaodiplomadigital ADD COLUMN IF NOT EXISTS historicoConsiderarReprovado boolean DEFAULT(TRUE);

ALTER TABLE IF EXISTS configuracaodiplomadigital ADD COLUMN IF NOT EXISTS historicoConsiderarCursando boolean DEFAULT(TRUE);

ALTER TABLE IF EXISTS configuracaodiplomadigital ADD COLUMN IF NOT EXISTS historicoConsiderarEvasao boolean DEFAULT(FALSE);

ALTER TABLE IF EXISTS configuracaodiplomadigital ADD COLUMN IF NOT EXISTS historicoConsiderarForaGrade boolean DEFAULT(TRUE);

ALTER TABLE IF EXISTS configuracaodiplomadigital ADD COLUMN IF NOT EXISTS considerarCargaHorariaForaGrade boolean DEFAULT(TRUE);

ALTER TABLE IF EXISTS configuracaodiplomadigital ADD COLUMN IF NOT EXISTS formatoCargaHorariaXML varchar(15) DEFAULT('HORA_AULA');

ALTER TABLE IF EXISTS configuracaogeralsistema ADD COLUMN IF NOT EXISTS databasevalidacaodiplomadigital date;

ALTER TABLE IF EXISTS unidadeEnsino ADD COLUMN IF NOT EXISTS configuracaoDiplomaDigital int;

ALTER TABLE IF EXISTS unidadeEnsino ADD COLUMN IF NOT EXISTS dataCredenciamento timestamp;

ALTER TABLE IF EXISTS unidadeEnsino ADD COLUMN IF NOT EXISTS veiculoPublicacaoCredenciamento varchar(100);

ALTER TABLE IF EXISTS unidadeEnsino ADD COLUMN IF NOT EXISTS secaoPublicacaoCredenciamento int;

ALTER TABLE IF EXISTS unidadeEnsino ADD COLUMN IF NOT EXISTS paginaPublicacaoCredenciamento int;

ALTER TABLE IF EXISTS unidadeEnsino ADD COLUMN IF NOT EXISTS numeroDOUCredenciamento int;

ALTER TABLE IF EXISTS unidadeEnsino ADD COLUMN IF NOT EXISTS tipoAutorizacaoEnum varchar(30);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS informarDadosRegistradora boolean DEFAULT(TRUE);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS utilizarEnderecoUnidadeEnsinoRegistradora boolean DEFAULT(FALSE);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS cepRegistradora varchar(10);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS cidadeRegistradora int;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS complementoRegistradora varchar(100);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS bairroRegistradora varchar(100);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS enderecoRegistradora varchar(100);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS numeroRegistradora varchar(20);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS utilizarCredenciamentoUnidadeEnsino boolean DEFAULT(FALSE);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS numeroCredenciamentoRegistradora varchar(99);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataCredenciamentoRegistradora timestamp;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataPublicacaoDORegistradora timestamp;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS veiculoPublicacaoCredenciamentoRegistradora varchar(99);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS secaoPublicacaoCredenciamentoRegistradora int;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS paginaPublicacaoCredenciamentoRegistradora int;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS numeroPublicacaoCredenciamentoRegistradora int;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS utilizarMantenedoraUnidadeEnsino boolean DEFAULT(FALSE);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS mantenedoraRegistradora varchar(99);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS cnpjMantenedoraRegistradora varchar(25);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS cepMantenedoraRegistradora varchar(10);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS enderecoMantenedoraRegistradora varchar(99);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS numeroMantenedoraRegistradora varchar(99);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS cidadeMantenedoraRegistradora int;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS complementoMantenedoraRegistradora varchar(99);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS bairroMantenedoraRegistradora varchar(99);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS utilizarEnderecoUnidadeEnsinoMantenedora boolean DEFAULT(FALSE);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS cepMantenedora varchar(10);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS enderecoMantenedora varchar(99);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS numeroMantenedora varchar(99);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS cidadeMantenedora int;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS complementoMantenedora varchar(99);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS bairroMantenedora varchar(99);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS numeroRecredenciamento varchar(99);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataRecredenciamento timestamp;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataPublicacaoRecredenciamento timestamp;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS veiculoPublicacaoRecredenciamento varchar(99);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS secaoPublicacaoRecredenciamento int;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS paginaPublicacaoRecredenciamento int;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS numeroDOURecredenciamento int;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS tipoAutorizacaoRecredenciamento varchar(25);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS numeroRenovacaoRecredenciamento varchar(99);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataRenovacaoRecredenciamento timestamp;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataPublicacaoRenovacaoRecredenciamento timestamp;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS veiculoPublicacaoRenovacaoRecredenciamento varchar(99);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS secaoPublicacaoRenovacaoRecredenciamento int;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS paginaPublicacaoRenovacaoRecredenciamento int;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS numeroDOURenovacaoRecredenciamento int;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS tipoAutorizacaoRenovacaoRecredenciamento varchar(25);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS tipoautorizacaocredenciamentoregistradora varchar(20);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS numeroCredenciamentoEAD varchar(100);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS credenciamentoEAD varchar(100);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataCredenciamentoEAD date;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataPublicacaoDOEAD date;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS credenciamentoPortariaEAD TEXT;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS veiculoPublicacaoCredenciamentoEAD varchar(100);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS secaoPublicacaoCredenciamentoEAD int;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS paginaPublicacaoCredenciamentoEAD int;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS numeroDOUCredenciamentoEAD int;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS tipoAutorizacaoEAD varchar(25);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS numeroRecredenciamentoEAD varchar(100);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataRecredenciamentoEAD date;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataPublicacaoRecredenciamentoEAD date;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS veiculoPublicacaoRecredenciamentoEAD varchar(100);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS secaoPublicacaoRecredenciamentoEAD int;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS paginaPublicacaoRecredenciamentoEAD int;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS numeroDOURecredenciamentoEAD int;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS tipoAutorizacaoRecredenciamentoEAD varchar(25);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS numeroRenovacaoRecredenciamentoEAD varchar(100);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataRenovacaoRecredenciamentoEAD date;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataPublicacaoRenovacaoRecredenciamentoEAD date;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS veiculoPublicacaoRenovacaoRecredenciamentoEAD varchar(100);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS secaoPublicacaoRenovacaoRecredenciamentoEAD int;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS paginaPublicacaoRenovacaoRecredenciamentoEAD int;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS numeroDOURenovacaoRecredenciamentoEAD int;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS tipoAutorizacaoRenovacaoRecredenciamentoEAD varchar(25);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS credenciamentoEmTramitacao boolean;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS numeroProcessoCredenciamento varchar(40);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS tipoProcessoCredenciamento varchar(255);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataCadastroCredenciamento date;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataProtocoloCredenciamento date;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS recredenciamentoEmTramitacao boolean;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS numeroProcessoRecredenciamento varchar(40);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS tipoProcessoRecredenciamento varchar(255);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataCadastroRecredenciamento date;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataProtocoloRecredenciamento date;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS renovacaoRecredenciamentoEmTramitacao boolean;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS numeroProcessoRenovacaoRecredenciamento varchar(40);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS tipoProcessoRenovacaoRecredenciamento varchar(255);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataCadastroRenovacaoRecredenciamento date;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataProtocoloRenovacaoRecredenciamento date;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS credenciamentoEadEmTramitacao boolean;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS numeroProcessoCredenciamentoEad varchar(40);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS tipoProcessoCredenciamentoEad varchar(255);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataCadastroCredenciamentoEad date;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataProtocoloCredenciamentoEad date;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS recredenciamentoEmTramitacaoEad boolean;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS numeroProcessoRecredenciamentoEad varchar(40);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS tipoProcessoRecredenciamentoEad varchar(255);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataCadastroRecredenciamentoEad date;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataProtocoloRecredenciamentoEad date;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS renovacaoRecredenciamentoEmTramitacaoEad boolean;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS numeroProcessoRenovacaoRecredenciamentoEad varchar(40);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS tipoProcessoRenovacaoRecredenciamentoEad varchar(255);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataCadastroRenovacaoRecredenciamentoEad date;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataProtocoloRenovacaoRecredenciamentoEad date;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS credenciamentoRegistradoraEmTramitacao boolean;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS numeroProcessoCredenciamentoRegistradora varchar(40);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS tipoProcessoCredenciamentoRegistradora varchar(255);

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataCadastroCredenciamentoRegistradora date;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS dataProtocoloCredenciamentoRegistradora date;

ALTER TABLE IF EXISTS unidadeensino ADD COLUMN IF NOT EXISTS numerocredenciamento VARCHAR(255);

SELECT
	create_constraint('ALTER TABLE unidadeensino ADD CONSTRAINT fk_unidadeensino_cidadeRegistradora FOREIGN KEY (cidadeRegistradora) REFERENCES cidade (codigo) ON UPDATE RESTRICT ON DELETE RESTRICT;');

SELECT
	create_constraint('ALTER TABLE unidadeensino ADD CONSTRAINT fk_unidadeensino_cidadeMantenedoraRegistradora FOREIGN KEY (cidadeMantenedoraRegistradora) REFERENCES cidade (codigo) ON UPDATE RESTRICT ON DELETE RESTRICT;');

SELECT
	create_constraint('ALTER TABLE unidadeensino ADD CONSTRAINT fk_unidadeensino_cidadeMantenedora FOREIGN KEY (cidadeMantenedora) REFERENCES cidade (codigo) ON UPDATE RESTRICT ON DELETE RESTRICT;');

SELECT
	create_constraint('ALTER TABLE unidadeEnsino ADD CONSTRAINT fk_unidadeEnsino_configuracaoDiplomaDigital FOREIGN KEY (configuracaoDiplomaDigital) REFERENCES configuracaoDiplomaDigital(codigo) ON UPDATE RESTRICT ON DELETE RESTRICT;');

CREATE TABLE IF NOT EXISTS public.declaracaoAcercaProcessoJudicial (
	codigo serial4 NOT NULL,
	expedicaodiploma int NOT NULL,
	declaracao varchar(150),
	created timestamp NULL,
	codigocreated int4 NULL,
	nomecreated varchar(255) NULL,
	updated timestamp NULL,
	codigoupdated int4 NULL,
	nomeupdated varchar(255) NULL,
	CONSTRAINT declaracoesAcercaProcessoJudicial_pkey PRIMARY KEY (codigo),
	CONSTRAINT fk_declaracoesAcercaProcessoJudicial_expedicaoDiploma FOREIGN KEY (expedicaodiploma) REFERENCES public.expedicaodiploma(codigo) ON DELETE RESTRICT ON UPDATE RESTRICT
);
	
ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS dataRegistroDiploma date;
	
ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS funcionarioQuarto int;

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS funcionarioQuinto int;

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS emitidoPorDecisaoJudicial boolean DEFAULT(FALSE);

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS nomeJuizDecisaoJudicial varchar(100);

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS numeroProcessoDecisaoJudicial varchar(35);

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS decisaoJudicial varchar(100);

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS informacoesAdicionaisDecisaoJudicial varchar(100);

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS emitidoPorProcessoTransferenciaAssistida boolean DEFAULT(FALSE);

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS nomeIesPTA varchar(255);

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS cnpjPTA varchar(18);

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS codigoMecPTA int;

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS cepPTA varchar(10);

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS cidadePTA int;

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS logradouroPTA varchar(150);

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS numeroPTA varchar(60);

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS complementoPTA varchar(60);

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS bairroPTA varchar(60);

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS tipoDescredenciamentoPTA varchar(20);

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS numeroDescredenciamentoPTA varchar(20);

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS dataDescredenciamentoPTA date;

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS dataPublicacaoDescredenciamentoPTA date;

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS veiculoPublicacaoDescredenciamentoPTA varchar(20);

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS secaoPublicacaoDescredenciamentoPTA int;

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS paginaPublicacaoDescredenciamentoPTA int;

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS numeroDOUDescredenciamentoPTA int;

ALTER TABLE IF EXISTS expedicaoDiploma ADD COLUMN IF NOT EXISTS versaoDiploma varchar(15);

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS dataAnulacao date DEFAULT(NULL);

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS motivoAnulacao varchar(250) DEFAULT(NULL);

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS anotacaoAnulacao varchar(250) DEFAULT(NULL);

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS dataCadastro date DEFAULT(NULL);

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS responsavelCadastro int DEFAULT(NULL);

ALTER TABLE IF EXISTS expedicaodiploma ALTER COLUMN veiculopublicacaodescredenciamentopta TYPE varchar(100);

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS codigoValidacaoDiplomaDigital TEXT;

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS anulado boolean;

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS textopadrao int;

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS cargofuncionarioquarto int;

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS cargofuncionarioquinto int;

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS informarCamposLivroRegistradora boolean;

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS nrLivroRegistradora TEXT;

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS nrFolhaReciboRegistradora TEXT;

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS numeroRegistroRegistradora TEXT;

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS gerarxmldiploma boolean;

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS tituloFuncionarioQuarto varchar(100);

ALTER TABLE IF EXISTS expedicaodiploma ADD COLUMN IF NOT EXISTS tituloFuncionarioQuinto varchar(100);

SELECT
	create_constraint('ALTER TABLE expedicaodiploma ADD CONSTRAINT fk_expedicaodiploma_textopadrao FOREIGN KEY (textopadrao) REFERENCES textopadraodeclaracao(codigo);');

UPDATE
	expedicaodiploma
SET
	funcionarioprimario = NULL
WHERE
	expedicaodiploma.funcionarioprimario IS NOT NULL
	AND NOT EXISTS (
	SELECT
	FROM
		funcionario
	WHERE
		funcionario.codigo = expedicaodiploma.funcionarioprimario);

UPDATE
	expedicaodiploma
SET
	funcionariosecundario = NULL
WHERE
	expedicaodiploma.funcionariosecundario IS NOT NULL
	AND NOT EXISTS (
	SELECT
	FROM
		funcionario
	WHERE
		funcionario.codigo = expedicaodiploma.funcionariosecundario);

UPDATE
	expedicaodiploma
SET
	funcionarioterceiro = NULL
WHERE
	expedicaodiploma.funcionarioterceiro IS NOT NULL
	AND NOT EXISTS (
	SELECT
	FROM
		funcionario
	WHERE
		funcionario.codigo = expedicaodiploma.funcionarioterceiro);

UPDATE
	expedicaodiploma
SET
	funcionarioquarto = NULL
WHERE
	expedicaodiploma.funcionarioquarto IS NOT NULL
	AND NOT EXISTS (
	SELECT
	FROM
		funcionario
	WHERE
		funcionario.codigo = expedicaodiploma.funcionarioquarto);

UPDATE
	expedicaodiploma
SET
	funcionarioquinto = NULL
WHERE
	expedicaodiploma.funcionarioquinto IS NOT NULL
	AND NOT EXISTS (
	SELECT
	FROM
		funcionario
	WHERE
		funcionario.codigo = expedicaodiploma.funcionarioquinto);

UPDATE
	expedicaodiploma
SET
	cargofuncionarioprincipal = NULL
WHERE
	expedicaodiploma.cargofuncionarioprincipal IS NOT NULL
	AND NOT EXISTS (
	SELECT
	FROM
		cargo
	WHERE
		cargo.codigo = expedicaodiploma.cargofuncionarioprincipal);

UPDATE
	expedicaodiploma
SET
	cargofuncionariosecundario = NULL
WHERE
	expedicaodiploma.cargofuncionariosecundario IS NOT NULL
	AND NOT EXISTS (
	SELECT
	FROM
		cargo
	WHERE
		cargo.codigo = expedicaodiploma.cargofuncionariosecundario);

UPDATE
	expedicaodiploma
SET
	cargofuncionarioterceiro = NULL
WHERE
	expedicaodiploma.cargofuncionarioterceiro IS NOT NULL
	AND NOT EXISTS (
	SELECT
	FROM
		cargo
	WHERE
		cargo.codigo = expedicaodiploma.cargofuncionarioterceiro);

UPDATE
	expedicaodiploma
SET
	cargofuncionarioquarto = NULL
WHERE
	expedicaodiploma.cargofuncionarioquarto IS NOT NULL
	AND NOT EXISTS (
	SELECT
	FROM
		cargo
	WHERE
		cargo.codigo = expedicaodiploma.cargofuncionarioquarto);

UPDATE
	expedicaodiploma
SET
	cargofuncionarioquinto = NULL
WHERE
	expedicaodiploma.cargofuncionarioquinto IS NOT NULL
	AND NOT EXISTS (
	SELECT
	FROM
		cargo
	WHERE
		cargo.codigo = expedicaodiploma.cargofuncionarioquinto);

UPDATE
	expedicaodiploma
SET
	funcionarioprimario = NULL
WHERE
	funcionarioprimario = 0;

UPDATE
	expedicaodiploma
SET
	funcionariosecundario = NULL
WHERE
	funcionariosecundario = 0;

UPDATE
	expedicaodiploma
SET
	funcionarioterceiro = NULL
WHERE
	funcionarioterceiro = 0;

UPDATE
	expedicaodiploma
SET
	funcionarioquarto = NULL
WHERE
	funcionarioquarto = 0;

UPDATE
	expedicaodiploma
SET
	funcionarioquinto = NULL
WHERE
	funcionarioquinto = 0;

UPDATE
	expedicaodiploma
SET
	cargofuncionarioquarto = NULL
WHERE
	cargofuncionarioquarto = 0;

UPDATE
	expedicaodiploma
SET
	cargofuncionarioquinto = NULL
WHERE
	cargofuncionarioquinto = 0;

UPDATE
	expedicaodiploma
SET
	unidadeensinocertificadora = (
	SELECT
			codigo
	FROM
			unidadeensino
	WHERE
			unidadeensino.matriz
	ORDER BY
		codigo DESC
	LIMIT 1
	)
WHERE
	expedicaodiploma.utilizarunidadematriz
	AND (expedicaodiploma.unidadeensinocertificadora IS NULL);

UPDATE
	expedicaodiploma
SET
	unidadeensinocertificadora = (
	SELECT
			unidadeensino.codigo
	FROM
			matricula
	INNER JOIN unidadeensino ON
			unidadeensino.codigo = matricula.unidadeensino
	WHERE
			matricula.matricula = expedicaodiploma.matricula
	LIMIT 1
	)
WHERE
	(expedicaodiploma.unidadeensinocertificadora IS NULL)
	AND (expedicaodiploma.utilizarunidadematriz = FALSE
		OR expedicaodiploma.utilizarunidadematriz IS NULL);

SELECT
	create_constraint('ALTER TABLE expedicaoDiploma ADD CONSTRAINT fk_expedicaodiploma_funcionarioprimario FOREIGN KEY (funcionarioprimario) REFERENCES funcionario (codigo) ON UPDATE RESTRICT ON DELETE RESTRICT;');

SELECT
	create_constraint('ALTER TABLE expedicaoDiploma ADD CONSTRAINT fk_expedicaodiploma_funcionariosecundario FOREIGN KEY (funcionariosecundario) REFERENCES funcionario (codigo) ON UPDATE RESTRICT ON DELETE RESTRICT;');

SELECT
	create_constraint('ALTER TABLE expedicaoDiploma ADD CONSTRAINT fk_expedicaodiploma_funcionarioterceiro FOREIGN KEY (funcionarioterceiro) REFERENCES funcionario (codigo) ON UPDATE RESTRICT ON DELETE RESTRICT;');

SELECT
	create_constraint('ALTER TABLE expedicaoDiploma ADD CONSTRAINT fk_expedicaodiploma_funcionarioquarto FOREIGN KEY (funcionarioquarto) REFERENCES funcionario (codigo) ON UPDATE RESTRICT ON DELETE RESTRICT;');

SELECT
	create_constraint('ALTER TABLE expedicaoDiploma ADD CONSTRAINT fk_expedicaodiploma_funcionarioquinto FOREIGN KEY (funcionarioquinto) REFERENCES funcionario (codigo) ON UPDATE RESTRICT ON DELETE RESTRICT;');

SELECT
	create_constraint('ALTER TABLE expedicaoDiploma ADD CONSTRAINT fk_expedicaodiploma_cargofuncionarioquarto FOREIGN KEY (cargofuncionarioquarto) REFERENCES cargo (codigo) ON UPDATE RESTRICT ON DELETE RESTRICT;');

SELECT
	create_constraint('ALTER TABLE expedicaoDiploma ADD CONSTRAINT fk_expedicaodiploma_cargofuncionarioquinto FOREIGN KEY (cargofuncionarioquinto) REFERENCES cargo (codigo) ON UPDATE RESTRICT ON DELETE RESTRICT;');

SELECT
	create_constraint('ALTER TABLE expedicaodiploma add constraint fk_expedicaoDiploma_responsavelCadastro foreign key (responsavelCadastro) references usuario(codigo) on update restrict on delete restrict;');

SELECT
	create_constraint('ALTER TABLE expedicaodiploma ADD CONSTRAINT fk_expedicaodiploma_cidadePTA FOREIGN KEY (cidadePTA) REFERENCES cidade (codigo) ON UPDATE RESTRICT ON DELETE RESTRICT;');

ALTER TABLE IF EXISTS textoenade ALTER COLUMN tipotextoenade TYPE varchar(100);

ALTER TABLE IF EXISTS matriculaenade ADD COLUMN IF NOT EXISTS condicaoEnade TEXT;

ALTER TABLE IF EXISTS curso ADD COLUMN IF NOT EXISTS autorizacaoResolucaoEmTramitacao boolean;

ALTER TABLE IF EXISTS curso ADD COLUMN IF NOT EXISTS numeroProcessoAutorizacaoResolucao varchar(40);

ALTER TABLE IF EXISTS curso ADD COLUMN IF NOT EXISTS tipoProcessoAutorizacaoResolucao varchar(255);

ALTER TABLE IF EXISTS curso ADD COLUMN IF NOT EXISTS dataCadastroAutorizacaoResolucao date;

ALTER TABLE IF EXISTS curso ADD COLUMN IF NOT EXISTS dataProtocoloAutorizacaoResolucao date;

ALTER TABLE IF EXISTS curso ADD COLUMN IF NOT EXISTS dataHabilitacao date;

ALTER TABLE IF EXISTS curso ADD COLUMN IF NOT EXISTS possuiCodigoEMEC boolean DEFAULT TRUE;

ALTER TABLE IF EXISTS curso ADD COLUMN IF NOT EXISTS codigoEMEC integer;

ALTER TABLE IF EXISTS curso ADD COLUMN IF NOT EXISTS numeroProcessoEMEC integer;

ALTER TABLE IF EXISTS curso ADD COLUMN IF NOT EXISTS tipoProcessoEMEC varchar(255);

ALTER TABLE IF EXISTS curso ADD COLUMN IF NOT EXISTS dataCadastroEMEC timestamp WITHOUT time ZONE;

ALTER TABLE IF EXISTS curso ADD COLUMN IF NOT EXISTS dataProtocoloEMEC timestamp WITHOUT time ZONE;

ALTER TABLE IF EXISTS curso ADD COLUMN IF NOT EXISTS tipoAutorizacaoCursoEnum varchar(100);

ALTER TABLE IF EXISTS curso ADD COLUMN IF NOT EXISTS numeroAutorizacao varchar(100);

ALTER TABLE IF EXISTS curso ADD COLUMN IF NOT EXISTS dataCredenciamento timestamp;

ALTER TABLE IF EXISTS curso ADD COLUMN IF NOT EXISTS veiculoPublicacao varchar(100);

ALTER TABLE IF EXISTS curso ADD COLUMN IF NOT EXISTS secaoPublicacao int;

ALTER TABLE IF EXISTS curso ADD COLUMN IF NOT EXISTS paginaPublicacao int;

ALTER TABLE IF EXISTS curso ADD COLUMN IF NOT EXISTS numeroDOU int;

UPDATE
	curso
SET
	dataHabilitacao = datacredenciamento
WHERE
	dataHabilitacao IS NULL;

UPDATE
	curso
SET
	datacredenciamento = datapublicacaodo
WHERE
	(datacredenciamento IS NULL
		AND datapublicacaodo IS NOT NULL);

ALTER TABLE IF EXISTS tipodocumento ADD COLUMN IF NOT EXISTS tipoDocumentacaoEnum varchar(255);

ALTER TABLE IF EXISTS tipodocumento ADD COLUMN IF NOT EXISTS enviarDocumentoXml boolean DEFAULT(TRUE);

ALTER TABLE IF EXISTS documentoassinado ADD COLUMN IF NOT EXISTS decisaoJudicial boolean DEFAULT(FALSE);

ALTER TABLE IF EXISTS documentoAssinado ADD COLUMN IF NOT EXISTS versaoDiploma varchar(15);

ALTER TABLE IF EXISTS documentoassinado ADD COLUMN IF NOT EXISTS codigoValidacaoCurriculoEscolarDigital TEXT;

ALTER TABLE IF EXISTS documentoassinado ADD COLUMN IF NOT EXISTS expedicaoDiploma int DEFAULT(NULL);

ALTER TABLE IF EXISTS documentoassinado ADD COLUMN IF NOT EXISTS arquivovisual integer;

ALTER TABLE IF EXISTS documentoassinado ALTER COLUMN tipoOrigemDocumentoAssinado TYPE varchar(50);

ALTER TABLE IF EXISTS documentoassinado ADD COLUMN IF NOT EXISTS idDiplomaDigital TEXT;

ALTER TABLE IF EXISTS documentoassinado ADD COLUMN IF NOT EXISTS idDadosRegistrosDiplomaDigital TEXT;

ALTER TABLE IF EXISTS documentoassinado ADD COLUMN IF NOT EXISTS codigovalidacaohistoricodigital TEXT;

ALTER TABLE IF EXISTS documentoassinado ADD CONSTRAINT fk_documentoassinado_arquivovisual FOREIGN KEY (arquivovisual) REFERENCES arquivo(codigo) ON
UPDATE
	RESTRICT ON
	DELETE
	RESTRICT;

ALTER TABLE documentoassinadopessoa ADD COLUMN IF NOT EXISTS assinarPorCNPJ boolean DEFAULT FALSE;

ALTER TABLE documentoassinadopessoa ADD COLUMN IF NOT EXISTS nome TEXT;

ALTER TABLE documentoassinadopessoa ADD COLUMN IF NOT EXISTS provedorAssinatura TEXT;

SELECT
	create_constraint('ALTER TABLE documentoassinado add constraint fk_documentoAssinado_expedicaoDiploma foreign key (expedicaoDiploma) references expedicaodiploma(codigo) on update restrict on delete restrict;');

UPDATE
	documentoassinado
SET
	expedicaodiploma = x.ex_codigo
FROM
	(
	SELECT
		*
	FROM
		(
		SELECT
			documentoassinado.codigo codigo_documento_alterar,
			documentoassinado.matricula,
			documentoassinado.dataregistro,
			expedicaodiploma.dataexpedicao ,
			expedicaodiploma.codigo ex_codigo
		FROM
			documentoassinado
		INNER JOIN expedicaodiploma ON
			expedicaodiploma.codigo = (
			SELECT
				ed.codigo
			FROM
				expedicaodiploma ed
			WHERE
				ed.matricula = documentoAssinado.matricula
			ORDER BY
				@ (EXTRACT(epoch
			FROM
				documentoassinado.dataregistro) - EXTRACT(epoch
			FROM
				ed.dataexpedicao))
			LIMIT 1)
		WHERE
			tipoorigemdocumentoassinado IN ('DIPLOMA_DIGITAL', 'DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL')) AS t
	WHERE
		1 = 1) x
WHERE
	documentoassinado.codigo = x.codigo_documento_alterar;

ALTER TABLE IF EXISTS autorizacaocurso ADD COLUMN IF NOT EXISTS emTramitacao boolean DEFAULT(FALSE);

ALTER TABLE IF EXISTS autorizacaocurso ADD COLUMN IF NOT EXISTS tipoProcesso varchar(255) DEFAULT('');

ALTER TABLE IF EXISTS autorizacaocurso ADD COLUMN IF NOT EXISTS dataCadastro date;

ALTER TABLE IF EXISTS autorizacaocurso ADD COLUMN IF NOT EXISTS dataProtocolo date;

ALTER TABLE IF EXISTS autorizacaocurso ADD COLUMN IF NOT EXISTS numero varchar(50);

ALTER TABLE IF EXISTS autorizacaocurso ADD COLUMN IF NOT EXISTS dataCredenciamento timestamp;

ALTER TABLE IF EXISTS autorizacaocurso ADD COLUMN IF NOT EXISTS veiculoPublicacao varchar(100);

ALTER TABLE IF EXISTS autorizacaocurso ADD COLUMN IF NOT EXISTS secaoPublicacao int;

ALTER TABLE IF EXISTS autorizacaocurso ADD COLUMN IF NOT EXISTS paginaPublicacao int;

ALTER TABLE IF EXISTS autorizacaocurso ADD COLUMN IF NOT EXISTS numeroDOU int;

ALTER TABLE IF EXISTS autorizacaocurso ADD COLUMN IF NOT EXISTS tipoAutorizacaoCursoEnum varchar(100);

UPDATE
	autorizacaocurso
SET
	datacredenciamento = DATA
WHERE
	(datacredenciamento IS NULL
		AND DATA IS NOT NULL);

ALTER TABLE IF EXISTS gradedisciplina ADD COLUMN IF NOT EXISTS utilizarEmissaoXmlDiploma boolean DEFAULT(TRUE);

ALTER TABLE IF EXISTS gradecurriculargrupooptativadisciplina ADD COLUMN IF NOT EXISTS utilizarEmissaoXmlDiploma boolean DEFAULT(TRUE);

ALTER TABLE IF EXISTS arquivo ADD COLUMN IF NOT EXISTS arquivoConvertidoPDFA boolean DEFAULT(FALSE);