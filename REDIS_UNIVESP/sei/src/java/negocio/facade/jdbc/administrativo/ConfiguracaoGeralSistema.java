package negocio.facade.jdbc.administrativo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import jobs.enumeradores.FornecedorSmsEnum;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.MotivoCancelamentoTrancamentoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.VisaoVO;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracoesVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.PerfilEconomicoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.ConfiguracaoGeralSistemaInterfaceFacade;
import org.springframework.util.StringUtils;
import webservice.nfse.generic.AmbienteEnum;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>ConfiguracaoGeralSistemaVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>ConfiguracaoGeralSistemaVO</code>. Encapsula toda a interação
 * com o banco de dados.
 * 
 * @see ConfiguracaoGeralSistemaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
public class ConfiguracaoGeralSistema extends ControleAcesso implements ConfiguracaoGeralSistemaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public ConfiguracaoGeralSistema() throws Exception {
		super();
		setIdEntidade("ConfiguracaoGeralSistema");
	}

	/**
	 * Operacao responsavel por retornar um novo objeto da classe
	 * <code>ConfiguracaoGeralSistemaVO</code>.
	 */
	public ConfiguracaoGeralSistemaVO novo() throws Exception {
		ConfiguracaoGeralSistemaVO obj = new ConfiguracaoGeralSistemaVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ConfiguracaoGeralSistemaVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ConfiguracaoGeralSistemaVO</code> que
	 *            será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ConfiguracaoGeralSistemaVO obj, final UsuarioVO usuarioLogado) throws Exception {
		try {
			ConfiguracaoGeralSistemaVO.validarDados(obj);
			
			if (Uteis.isAtributoPreenchido(obj.getCertificadoParaDocumento().getDescricao()) && obj.getCertificadoParaDocumento().getPastaBaseArquivoEnum() != null && obj.getCertificadoParaDocumento().getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.CERTIFICADO_DOCUMENTOS_TMP)) {
				obj.getCertificadoParaDocumento().setValidarDados(false);
				getFacadeFactory().getArquivoFacade().persistir(obj.getCertificadoParaDocumento(), false, usuarioLogado, obj);
			}
			
			// 5 em 5 campos sao adicionados aqui
			final StringBuilder sql = new StringBuilder("INSERT INTO ConfiguracaoGeralSistema( ")
					.append(" validarCadastroCpf, smptPadrao, portaSmtpPadrao, emailRemetente, login, ")
					.append(" senha, visaoPadraoAluno, visaoPadraoProfessor, visaoPadraoCandidato, perfilPadraoAluno, ")
					.append(" perfilPadraoProfessorGraduacao, perfilPadraoCandidato, qtdeLimiteMsg, questionario, perfilEconomicoPadrao, ")
					.append(" configuracoes, permiteCancelamentoSemRequerimento, permiteTrancamentoSemRequerimento, permiteTransferenciaSemRequerimento, permiteAproveitamentoDisciplinaSemRequerimento, ")
					.append(" permiteProgramacaoFormaturaSemRequerimento, nrmaximofolharecibo, permiteTransferenciaInternaSemRequerimento, permitePortadorDiplomaSemRequerimento, permiteAlunoVerContasConvenio, ")
					.append(" prefixoMatriculaFuncionario, prefixoMatriculaProfessor, localUploadArquivoFixo, localUploadArquivoTemp, enderecoServidorArquivo, ")
					.append(" descricaooVersoCarteirinhaEstudantil, controlarValidadeCarteirinhaEstudantil, mensagemTelaLogin, apresentarMensagemTelaLogin, urlExternoDownloadArquivo, ")
					.append(" urlWebserviceNFe, urlWebserviceNFSe, mascaraSubRede, gerarSenhaCpfAluno, linkFacebook, linkLinkdIn, ")
					.append(" linkTwitter, codigoTwitts, perfilPadraoCoordenador, visaoPadraoCoordenador, perfilPadraoProfessorPosGraduacao, ")
					.append(" qtdeDiasAlertaRequerimento, servidorEmailUtilizaSSL, textoComunicacaoInterna, apresentarHomeCandidato, mensagemErroSenha, ")
					.append(" monitorarmensagensprofessor, emailConfirmacaoEnvioComunicado, perfilPadraoParceiro, qtdDiasExpiracaoVagaBancoCurriculum, qtdDiasNotificacaoExpiracaoVagaBancoCurriculum, ")
					.append(" tituloTelaBancoCurriculum, mensagemTelaBancoCurriculos, tituloTelaBuscaCandidato, usernameSMS, senhaSMS, fornecedorSMS,  ")
					.append(" qtdAceitavelContatosPendentesNaoIniciados, qtdLimiteContatosPendentesNaoIniciados, qtaAceitavelContatosPendentesNaoFinalizados, qtaLimiteContatosPendentesNaoFinalizados, responsavelPadraoComunicadoInterno, ")
					.append(" permiteInclusaoForaPrazoMatriculaPeriodoAtiva, naoPermitirExpedicaoDiplomaDocumentacaoPendente, naoPermitirAlterarUsernameUsuario, visaoPadraoPais, perfilPadraoPais,  ")
					.append(" permiteRenovarComParcelaVencida, mensagemPadraoRenovacaoMatriculaComParcelaVencida, qtdeAntecedenciaDiasNotificarAlunoDownloadMaterial, ipServidor, diasParaRemoverPreMatricula, ")
					.append(" qtdeMsgLimiteServidorNotificacao, permiteRenovarAlunoPreMatriculadoParaNovaMatriculaPreMatricula, avaliacaoInstitucionalFinalModuloAluno, avaliacaoInstitucionalFinalModuloProfessor, servidorEmailUtilizaTSL, ")
					.append(" utilizarCaixaAltaNomePessoa, controlaQtdDisciplinaExtensao, qtdDisciplinaExtensao, departamentoRespServidorNotificacao, tipoRequerimento, ")
					.append(" idAutenticacaoServOtimize, perfilPadraoOuvidoria, tamanhoMaximoUpload, naoApresentarProfessorVisaoAluno, ocultarCPFPreInscricao, ")
					.append(" ocultarEnderecoPreInscricao, controlaQtdDisciplinaRealizadaAteMatricula, qtdDisciplinaRealizadaAteMatricula, primeiroAcessoAlunoCairMeusDados, qtdDiasAcessoAlunoFormado, ")
					.append(" qtdDiasAcessoAlunoExtensao, qtdeDiasNotificarProfessorPostarMaterial, apresentarEsqueceuMinhaSenha, qtdDiasAcessoAlunoAtivo, matricularalunoreprovfaltaemdisciplinajarealizadaposgradext, ")
					.append(" funcionarioRespAlteracaoDados, nrDiasLimiteEntregaDocumento, grupoDestinatarioMapaLocalAula, paisPadrao, todosCamposObrigatoriosPreInscricao, ")
					.append(" maeFiliacao, notificarAlunoAniversariante, notificarProfessorAniversariante, notificarFuncionarioAniversariante, notificarPaiAniversariante, ")
					.append(" controlaAprovacaoDocEntregue, notificarExAlunoAniversariante, obrigarTipoMidiaProspect, urlConfirmacaoPreInscricao, qtdDiasMaximoAntecedenciaRemarcarAulaReposicao, ")
					.append(" calcularMediaAoGravar, permitiAlunoPreMatriculaSolicitarRequerimento, qtdeDiasNotificacaoDataProva, validarQtdAlunoLocalSalaAulaInclusaoReposicaoDisciplina, qtdeDiaNotificarAbandonoCurso, ")
					.append(" qtdeDiaRegistrarAbandonoCurso, motivoPadraoAbandonoCurso, ocultarEmailPreInscricao, ocultarRG, ocultarDataNasc, ")
					.append(" ocultarSexo, ocultarTelefone, ocultarEstadoCivil, ocultarNaturalidade, ocultarFormacaoAcademica,")
					.append(" diasParaNotificarCoordenadorInicioTurma,bloquearMatriculaPosSemGraduacao, qtdEmailEnvio, bloquearRealizarTrancamentoComEmprestimoBiblioteca, bloquearRealizarAbandonoCursoComEmprestimoBiblioteca, ")
					.append(" bloquearRealizarCancelamentoComEmprestimoBiblioteca, bloquearRealizarTransferenciaSaidaComEmprestimoBiblioteca, bloquearRealizarTransferenciaInternaComEmprestimoBiblioteca, bloquearRealizarRenovacaoComEmprestimoBiblioteca,verificarAulaProgramadaIncluirDisciplina,")
					.append(" perfilPadraoProfessorExtensao, desconsiderarAlunoReposicaoVagasTurma, permiteTransferenciaSaidaSemRequerimento, apresentarAlunoPendenteFinanceiroVisaoProfessor, apresentarAlunoPendenteFinanceiroVisaoCoordenador, ")
					.append(" ipServidorBiometria, perfilPadraoAlunoEducacaoInfantil, perfilPadraoAlunoEducacaoFundamental, perfilPadraoAlunoEducacaoMedio, perfilPadraoAlunoEducacaoGraduacao, ")
					.append(" perfilPadraoAlunoEducacaoGraduacaoTecnologica, perfilPadraoAlunoEducacaoSequencial, perfilPadraoAlunoEducacaoExtensao, perfilPadraoAlunoEducacaoPosGraduacao, perfilPadraoAlunoEducacaoTecnicoProfissionalizante, ")
					.append(" perfilPadraoPaisEducacaoInfantil, perfilPadraoPaisEducacaoFundamental, perfilPadraoPaisEducacaoMedio, perfilPadraoPaisEducacaoGraduacao, perfilPadraoPaisEducacaoGraduacaoTecnologica, ")
					.append(" perfilPadraoPaisEducacaoSequencial, perfilPadraoPaisEducacaoExtensao, perfilPadraoPaisEducacaoPosGraduacao, perfilPadraoPaisEducacaoTecnicoProfissionalizante, controlarNumeroAulaProgramadoProfessorPorDia, ")
					.append(" quantidadeAulaMaximaProgramarProfessorDia, usuarioResponsavelOperacoesExternas, incrementarNumeroExemplarPorBiblioteca, zerarNumeroRegistroPorCurso, ")
					.append(" criarProspectFiliacao, criarProspectAluno, criarProspectCandidato, criarProspectFuncionario, ")
					.append(" ocultarMediaProcSeletivo, ocultarClassificacaoProcSeletivo,controlarCargaHorariaMaximaEstagioObrigatorio, cargaHorariaMaximaSemanal, cargaHorariaMaximaDiaria, ")
					.append(" seguradoraPadraoEstagioObrigatorio, apolicePadraoEstagioObrigatorio, forcarSeguradoraEApoliceParaEstagioObrigatorio, realizarCalculoMediaFinalFechPeriodo , ocultarChamadaCandidatoProcSeletivo, ")
					.append(" nrDiasNotifVencCand,  certificadoparadocumento, senhacertificadoparadocumento,  quantidadeCasaDecimalConsiderarNotaProcessoSeletivo, associarprospectsemconsultorresponsavelcomprimeiroconsultor, ")
					.append(" associarnovoprospectcomconsultorresponsavelcadastro, primeiroAcessoAlunoResetarSenha, primeiroAcessoProfessorResetarSenha, urlAcessoExternoAplicacao, bloquearLancamentosNotasAulasFeriadosFinaisSemana, ")
					.append(" permitirProfessorRealizarLancamentoAlunosPreMatriculados, quantidadeCaracteresMinimoSenhaUsuario, nivelSegurancaNumero, nivelSegurancaLetra, nivelSegurancaLetraMaiuscula, ")
					.append(" nivelSegurancaCaracterEspecial, nivelcontrolealteracaosenha, idClienteRdStation, senhaClienteRdStation, tokenRdStation, ")
					.append(" tokenPrivadoRdStation, ativarIntegracaoRdStation, identificadorRdStation, tokenWebService, ")
					.append(" loginIntegracaoSofFin, senhaIntegracaoSofFin, tokenIntegracaoSofFin, ambienteEnumIntegracaoSoftFin, ")
					.append(" integracaoServidorOnline, servidorArquivoOnline, usuarioAutenticacao, senhaAutenticacao, localUploadArquivoGED, permitirAcessoAlunoPreMatricula, ")
					.append(" permitirAcessoAlunoFormado, permitirAcessoAlunoEvasao, perfilPadraoAlunoPreMatricula, perfilPadraoAlunoEvasao, perfilPadraoAlunoFormado, ")
					.append(" linkAcessoVisoesMoodle, perfilPadraoProfessorEducacaoInfantil, perfilPadraoProfessorEnsinoFundamental, perfilPadraoProfessorEnsinoMedio, perfilPadraoProfessorTecnicoProfissionalizante, ")
					.append(" perfilPadraoProfessorSequencial, perfilPadraoProfessorMestrado, perfilPadraoProfessorGraduacaoTecnologica, nomeRepositorio, siglaAbonoFalta, descricaoAbonoFalta, permiteReativacaoMatriculaSemRequerimento, qtdTentativasFalhaLogin, tempoBloqTentativasFalhaLogin, ")
					.append(" definirPerfilAcessoAlunoNaoAssinouContratoMatricula, perfilAcessoAlunoNaoAssinouContratoMatricula, apresentarMensagemAlertaAlunoNaoAssinouContrato, mensagemAlertaAlunoNaoAssinouContratoMatricula, percentualBaixaFrequencia, qtdeCaractereLimiteDownloadMaterial , ")
					.append(" logUploadProfessor, apresentarBotoesAcoesRequerimentoApenasAbaInicial, urlHomeCandidato, configuracaoAparenciaSistema , ")
					.append(" textoParaOrientacaoTcc, textoParaOrientacaoProjetoIntegrador, motivoPadraoCancelamentoPreMatriculaCalouro  , headerBarIntegracaoSistemasProvaMestreGR , actionIntegracaoSistemasProvaMestreGR   , tokenIntegracaoSistemasProvaMestreGR , habilitarIntegracaoSistemaProvas, ")
					.append(" ativarIntegracaoLGPD, nomeDestinatarioEmailIntegracaoLGPD , emailDestinatarioIntegracaoLGPD, assuntoMensagemLGPD, mensagemLGPD, textopadraodeclaracao, questionarioplanoensino ,")
					.append(" textoOrientacaoCancelamentoPorOutraMatricula , motivopadraocancelamentooutramatricula , justificativaCancelamentoPorOutraMatricula  , habilitarRecursoInativacaoCredenciasAlunosFormados, apresentarDocumentoPortalTransparenciaComPendenciaAssinatura, tamanhoMaximoCorpoMensagem, ")
					.append(" limitedestinatariosporemail, tamanholimiteanexoemail, databasevalidacaodiplomadigital, habilitarintegracaosistemasymplicty, hostintegracaosistemasymplicty, userintegracaosistemasymplicty, passintegracaosistemasymplicty, portintegracaosistemasymplicty, ")
					.append(" protocolintegracaosistemasymplicty, pastadestinoremotasymplicty,habilitaroperacoestemporealintegracaomestregr, integracaomestregrurlbaseapi, habilitarrecursosacademicosvisaoaluno, habilitarmonitoramentosentry, tokensentry, habilitarmonitoramentoblackboardsentry, tokenblackboardsentry, habilitarEnvioEmailAssincrono, ativarDebugEmail, timeOutFilaEmail) ")
					.append("VALUES (") //5
					.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
					.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
					.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
					.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
					.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
					.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
					.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
					.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
					.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
					.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ")
					.append(") ")
					.append(" returning codigo");

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int x = 1;
					
					sqlInserir.setBoolean(x++, obj.isValidarCadastroCpf().booleanValue());
					sqlInserir.setString(x++, obj.getSmptPadrao());
					sqlInserir.setString(x++, obj.getPortaSmtpPadrao());
					sqlInserir.setString(x++, obj.getEmailRemetente());
					sqlInserir.setString(x++, obj.getLogin());
					
					sqlInserir.setString(x++, obj.getSenha());
					if (obj.getVisaoPadraoAluno().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getVisaoPadraoAluno().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (obj.getVisaoPadraoProfessor().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getVisaoPadraoProfessor().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (obj.getVisaoPadraoCandidato().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getVisaoPadraoCandidato().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (obj.getPerfilPadraoAluno().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getPerfilPadraoAluno().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (obj.getPerfilPadraoProfessorGraduacao().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getPerfilPadraoProfessorGraduacao().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (obj.getPerfilPadraoCandidato().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getPerfilPadraoCandidato().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setInt(x++, obj.getQtdeLimiteMsg().intValue());
					if (obj.getQuestionarioPerfilSocioEconomico().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getQuestionarioPerfilSocioEconomico().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (obj.getPerfilEconomicoPadrao().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getPerfilEconomicoPadrao().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (obj.getConfiguracoesVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getConfiguracoesVO().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setBoolean(x++, obj.getPermiteCancelamentoSemRequerimento().booleanValue());
					sqlInserir.setBoolean(x++, obj.getPermiteTrancamentoSemRequerimento().booleanValue());
					sqlInserir.setBoolean(x++, obj.getPermiteTransferenciaSemRequerimento().booleanValue());
					sqlInserir.setBoolean(x++, obj.getPermiteAproveitamentoDiscSemRequerimento().booleanValue());
					sqlInserir.setBoolean(x++, obj.getPermiteProgramacaoFormaturaSemRequerimento().booleanValue());
					sqlInserir.setInt(x++, obj.getNrMaximoFolhaRecibo().intValue());
					sqlInserir.setBoolean(x++, obj.getPermiteTransferenciaInternaSemRequerimento().booleanValue());
					sqlInserir.setBoolean(x++, obj.getPermitePortadorDiplomaSemRequerimento().booleanValue());
					sqlInserir.setBoolean(x++, obj.getPermiteAlunoVerContasConvenio().booleanValue());
					sqlInserir.setString(x++, obj.getPrefixoMatriculaFuncionario());
					sqlInserir.setString(x++, obj.getPrefixoMatriculaProfessor());
					sqlInserir.setString(x++, obj.getLocalUploadArquivoFixo());
					sqlInserir.setString(x++, obj.getLocalUploadArquivoTemp());
					sqlInserir.setString(x++, obj.getEnderecoServidorArquivo());
					sqlInserir.setString(x++, obj.getDescricaooVersoCarteirinhaEstudantil());
					sqlInserir.setBoolean(x++, obj.getControlarValidadeCarteirinhaEstudantil());
					sqlInserir.setString(x++, obj.getMensagemTelaLogin());
					sqlInserir.setBoolean(x++, obj.getApresentarMensagemTelaLogin());
					sqlInserir.setString(x++, obj.getUrlExternoDownloadArquivo());
					sqlInserir.setString(x++, obj.getUrlWebserviceNFe());
					sqlInserir.setString(x++, obj.getUrlWebserviceNFSe());
					sqlInserir.setString(x++, obj.getMascaraSubRede());
					sqlInserir.setBoolean(x++, obj.getGerarSenhaCpfAluno());
					sqlInserir.setString(x++, obj.getLinkFacebook());
					sqlInserir.setString(x++, obj.getLinkLinkdIn());
					sqlInserir.setString(x++, obj.getLinkTwitter());
					sqlInserir.setString(x++, obj.getCodigoTwitts());
					if (obj.getPerfilPadraoCoordenador().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getPerfilPadraoCoordenador().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (obj.getVisaoPadraoCoordenador().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getVisaoPadraoCoordenador().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (obj.getPerfilPadraoProfessorPosGraduacao().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getPerfilPadraoProfessorPosGraduacao().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setInt(x++, obj.getQtdeDiasAlertaRequerimento());
					sqlInserir.setBoolean(x++, obj.getServidorEmailUtilizaSSL());
					sqlInserir.setString(x++, obj.getTextoComunicacaoInterna());
					sqlInserir.setBoolean(x++, obj.getApresentarHomeCandidato());
					sqlInserir.setString(x++, obj.getMensagemErroSenha());

					sqlInserir.setBoolean(x++, obj.getMonitorarMensagensProfessor());
					sqlInserir.setString(x++, obj.getEmailConfirmacaoEnvioComunicado());
					if (obj.getPerfilPadraoParceiro().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getPerfilPadraoParceiro().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setInt(x++, obj.getQtdDiasExpiracaoVagaBancoCurriculum());
					sqlInserir.setInt(x++, obj.getQtdDiasNotificacaoExpiracaoVagaBancoCurriculum());
					sqlInserir.setString(x++, obj.getTituloTelaBancoCurriculum());
					sqlInserir.setString(x++, obj.getMensagemTelaBancoCurriculum());
					sqlInserir.setString(x++, obj.getTituloTelaBuscaCandidato());
					sqlInserir.setString(x++, obj.getUsernameSMS());
					sqlInserir.setString(x++, obj.getSenhaSMS());
					sqlInserir.setString(x++, obj.getFornecedorSMSEnum().name());
					sqlInserir.setInt(x++, obj.getQtdAceitavelContatosPendentesNaoIniciados());
					sqlInserir.setInt(x++, obj.getQtdLimiteContatosPendentesNaoIniciados());
					sqlInserir.setInt(x++, obj.getQtaAceitavelContatosPendentesNaoFinalizados());
					sqlInserir.setInt(x++, obj.getQtaLimiteContatosPendentesNaoFinalizados());
					sqlInserir.setInt(x++, obj.getResponsavelPadraoComunicadoInterno().getCodigo());
					sqlInserir.setBoolean(x++, obj.getPermiteInclusaoForaPrazoMatriculaPeriodoAtiva());
					sqlInserir.setBoolean(x++, obj.getNaoPermitirExpedicaoDiplomaDocumentacaoPendente());
					sqlInserir.setBoolean(x++, obj.getNaoPermitirAlterarUsernameUsuario());
					if (obj.getVisaoPadraoPais().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getVisaoPadraoPais().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (obj.getPerfilPadraoPais().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getPerfilPadraoPais().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setBoolean(x++, obj.getPermiteRenovarComParcelaVencida());
					sqlInserir.setString(x++, obj.getMensagemPadraoRenovacaoMatriculaComParcelaVencida());
					sqlInserir.setInt(x++, obj.getQtdeAntecedenciaDiasNotificarAlunoDownloadMaterial());
					sqlInserir.setString(x++, obj.getIpServidor());
					sqlInserir.setInt(x++, obj.getDiasParaRemoverPreMatricula());
					sqlInserir.setInt(x++, obj.getQtdeMsgLimiteServidorNotificacao());
					sqlInserir.setBoolean(x++, obj.getPermiteRenovarAlunoPreMatriculadoParaNovaMatriculaPreMatricula());
					sqlInserir.setInt(x++, obj.getAvaliacaoInstitucionalFinalModuloAluno());
					sqlInserir.setInt(x++, obj.getAvaliacaoInstitucionalFinalModuloProfessor());
					sqlInserir.setBoolean(x++, obj.getServidorEmailUtilizaTSL());
					sqlInserir.setBoolean(x++, obj.getUtilizarCaixaAltaNomePessoa());
					sqlInserir.setBoolean(x++, obj.getControlaQtdDisciplinaExtensao());
					sqlInserir.setInt(x++, obj.getQtdDisciplinaExtensao());
					sqlInserir.setInt(x++, obj.getDepartamentoRespServidorNotificacao());
					sqlInserir.setInt(x++, obj.getTipoRequerimentoVO().getCodigo());
					sqlInserir.setString(x++, obj.getIdAutenticacaoServOtimize());
					if (obj.getPerfilPadraoOuvidoria().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getPerfilPadraoOuvidoria().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setInt(x++, obj.getTamanhoMaximoUpload());
					sqlInserir.setBoolean(x++, obj.getNaoApresentarProfessorVisaoAluno());
					sqlInserir.setBoolean(x++, obj.getOcultarCPFPreInscricao());
					sqlInserir.setBoolean(x++, obj.getOcultarEnderecoPreInscricao());
					sqlInserir.setBoolean(x++, obj.getControlaQtdDisciplinaRealizadaAteMatricula());
					sqlInserir.setInt(x++, obj.getQtdDisciplinaRealizadaAteMatricula());
					sqlInserir.setBoolean(x++, obj.getPrimeiroAcessoAlunoCairMeusDados());
					sqlInserir.setInt(x++, obj.getQtdDiasAcessoAlunoFormado());
					sqlInserir.setInt(x++, obj.getQtdDiasAcessoAlunoExtensao());
					sqlInserir.setInt(x++, obj.getQtdeDiasNotificarProfessorPostarMaterial());
					sqlInserir.setBoolean(x++, obj.getApresentarEsqueceuMinhaSenha());
					sqlInserir.setInt(x++, obj.getQtdDiasAcessoAlunoAtivo());
					sqlInserir.setBoolean(x++, obj.getMatricularAlunoReprovFaltaEmDisciplinaJaRealizadaPosGraduacaoExtensao());
					if (obj.getFuncionarioRespAlteracaoDados().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getFuncionarioRespAlteracaoDados().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setInt(x++, obj.getNrDiasLimiteEntregaDocumento());
					if (obj.getGrupoDestinatarioMapaLocalAula().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getGrupoDestinatarioMapaLocalAula().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (obj.getPaisPadrao().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getPaisPadrao().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setBoolean(x++, obj.getTodosCamposObrigatoriosPreInscricao().booleanValue());
					sqlInserir.setBoolean(x++, obj.getMaeFiliacao().booleanValue());
					sqlInserir.setBoolean(x++, obj.getNotificarAlunoAniversariante().booleanValue());
					sqlInserir.setBoolean(x++, obj.getNotificarProfessorAniversariante().booleanValue());
					sqlInserir.setBoolean(x++, obj.getNotificarFuncionarioAniversariante().booleanValue());
					sqlInserir.setBoolean(x++, obj.getNotificarPaiAniversariante().booleanValue());
					sqlInserir.setBoolean(x++, obj.getControlaAprovacaoDocEntregue().booleanValue());
					sqlInserir.setBoolean(x++, obj.getNotificarExAlunoAniversariante().booleanValue());
					sqlInserir.setBoolean(x++, obj.getObrigarTipoMidiaProspect().booleanValue());
					sqlInserir.setString(x++, obj.getUrlConfirmacaoPreInscricao());

					sqlInserir.setInt(x++, obj.getQtdDiasMaximoAntecedenciaRemarcarAulaReposicao());
					sqlInserir.setBoolean(x++, obj.getCalcularMediaAoGravar());
					sqlInserir.setBoolean(x++, obj.getPermitiAlunoPreMatriculaSolicitarRequerimento());
					sqlInserir.setInt(x++, obj.getQtdeDiasNotificacaoDataProva());
					sqlInserir.setBoolean(x++, obj.getValidarQtdAlunoLocalSalaAulaInclusaoReposicaoDisciplina());
					sqlInserir.setInt(x++, obj.getQtdeDiaNotificarAbandonoCurso());
					sqlInserir.setInt(x++, obj.getQtdeDiaRegistrarAbandonoCurso());
					if (obj.getMotivoPadraoAbandonoCurso().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getMotivoPadraoAbandonoCurso().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setBoolean(x++, obj.getOcultarEmailPreInscricao());
					sqlInserir.setBoolean(x++, obj.getOcultarRG());
					sqlInserir.setBoolean(x++, obj.getOcultarDataNasc());
					sqlInserir.setBoolean(x++, obj.getOcultarSexo());
					sqlInserir.setBoolean(x++, obj.getOcultarTelefone());
					sqlInserir.setBoolean(x++, obj.getOcultarEstadoCivil());
					sqlInserir.setBoolean(x++, obj.getOcultarNaturalidade());
					sqlInserir.setBoolean(x++, obj.getOcultarFormacaoAcademica());

					sqlInserir.setInt(x++, obj.getDiasParaNotificarCoordenadorInicioTurma());
					sqlInserir.setBoolean(x++, obj.getBloquearMatriculaPosSemGraduacao());
					sqlInserir.setInt(x++, obj.getQtdEmailEnvio());
					sqlInserir.setBoolean(x++, obj.getBloquearRealizarTrancamentoComEmprestimoBiblioteca());
					sqlInserir.setBoolean(x++, obj.getBloquearRealizarAbandonoCursoComEmprestimoBiblioteca());
					sqlInserir.setBoolean(x++, obj.getBloquearRealizarCancelamentoComEmprestimoBiblioteca());
					sqlInserir.setBoolean(x++, obj.getBloquearRealizarTransferenciaSaidaComEmprestimoBiblioteca());
					sqlInserir.setBoolean(x++, obj.getBloquearRealizarTransferenciaInternaComEmprestimoBiblioteca());
					sqlInserir.setBoolean(x++, obj.getBloquearRealizarRenovacaoComEmprestimoBiblioteca());
					sqlInserir.setBoolean(x++, obj.getVerificarAulaProgramadaIncluirDisciplina());

					if (obj.getPerfilPadraoProfessorExtensao().getCodigo().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getPerfilPadraoProfessorExtensao().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setBoolean(x++, obj.getDesconsiderarAlunoReposicaoVagasTurma());
					sqlInserir.setBoolean(x++, obj.getPermiteTransferenciaSaidaSemRequerimento().booleanValue());
					sqlInserir.setBoolean(x++, obj.getApresentarAlunoPendenteFinanceiroVisaoProfessor());
					sqlInserir.setBoolean(x++, obj.getApresentarAlunoPendenteFinanceiroVisaoCoordenador());
					sqlInserir.setString(x++, obj.getIpServidorBiometria());
					sqlInserir.setInt(x++, obj.getPerfilPadraoAlunoEducacaoInfantil().getCodigo());
					sqlInserir.setInt(x++, obj.getPerfilPadraoAlunoEducacaoFundamental().getCodigo());
					sqlInserir.setInt(x++, obj.getPerfilPadraoAlunoEducacaoMedio().getCodigo());
					sqlInserir.setInt(x++, obj.getPerfilPadraoAlunoEducacaoGraduacao().getCodigo());
					sqlInserir.setInt(x++, obj.getPerfilPadraoAlunoEducacaoGraduacaoTecnologica().getCodigo());
					sqlInserir.setInt(x++, obj.getPerfilPadraoAlunoEducacaoSequencial().getCodigo());
					sqlInserir.setInt(x++, obj.getPerfilPadraoAlunoEducacaoExtensao().getCodigo());
					sqlInserir.setInt(x++, obj.getPerfilPadraoAlunoEducacaoPosGraduacao().getCodigo());
					sqlInserir.setInt(x++, obj.getPerfilPadraoAlunoEducacaoTecnicoProfissionalizante().getCodigo());
					sqlInserir.setInt(x++, obj.getPerfilPadraoPaisEducacaoInfantil().getCodigo());
					sqlInserir.setInt(x++, obj.getPerfilPadraoPaisEducacaoFundamental().getCodigo());
					sqlInserir.setInt(x++, obj.getPerfilPadraoPaisEducacaoMedio().getCodigo());
					sqlInserir.setInt(x++, obj.getPerfilPadraoPaisEducacaoGraduacao().getCodigo());
					sqlInserir.setInt(x++, obj.getPerfilPadraoPaisEducacaoGraduacaoTecnologica().getCodigo());
					sqlInserir.setInt(x++, obj.getPerfilPadraoPaisEducacaoSequencial().getCodigo());
					sqlInserir.setInt(x++, obj.getPerfilPadraoPaisEducacaoExtensao().getCodigo());
					sqlInserir.setInt(x++, obj.getPerfilPadraoPaisEducacaoPosGraduacao().getCodigo());
					sqlInserir.setInt(x++, obj.getPerfilPadraoPaisEducacaoTecnicoProfissionalizante().getCodigo());
					sqlInserir.setBoolean(x++, obj.getControlarNumeroAulaProgramadoProfessorPorDia());
					sqlInserir.setInt(x++, obj.getQuantidadeAulaMaximaProgramarProfessorDia());
					if(!obj.getUsuarioResponsavelOperacoesExternas().getCodigo().equals(0)) {
						sqlInserir.setInt(x++, obj.getUsuarioResponsavelOperacoesExternas().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setBoolean(x++, obj.getIncrementarNumeroExemplarPorBiblioteca().booleanValue());
					sqlInserir.setBoolean(x++, obj.getZerarNumeroRegistroPorCurso().booleanValue());
                    sqlInserir.setBoolean(x++, obj.getCriarProspectFiliacao());
					sqlInserir.setBoolean(x++, obj.getCriarProspectAluno());
					sqlInserir.setBoolean(x++, obj.getCriarProspectCandidato());
					sqlInserir.setBoolean(x++, obj.getCriarProspectFuncionario());
					sqlInserir.setBoolean(x++, obj.getOcultarMediaProcSeletivo());
					sqlInserir.setBoolean(x++, obj.getOcultarClassificacaoProcSeletivo());
					sqlInserir.setBoolean(x++, obj.getControlarCargaHorariaMaximaEstagioObrigatorio());
					sqlInserir.setInt(x++, obj.getCargaHorariaMaximaSemanal());
					sqlInserir.setInt(x++, obj.getCargaHorariaMaximaDiaria());
					sqlInserir.setString(x++, obj.getSeguradoraPadraoEstagioObrigatorio());
					sqlInserir.setString(x++, obj.getApolicePadraoEstagioObrigatorio());
					sqlInserir.setBoolean(x++, obj.getForcarSeguradoraEApoliceParaEstagioObrigatorio());
					sqlInserir.setBoolean(x++, obj.getRealizarCalculoMediaFinalFechPeriodo());		
					sqlInserir.setBoolean(x++, obj.getOcultarChamadaCandidatoProcSeletivo());					
					sqlInserir.setInt(x++, obj.getNrDiasNotifVencCand());	
					if(!obj.getCertificadoParaDocumento().getCodigo().equals(0)) {
						sqlInserir.setInt(x++, obj.getCertificadoParaDocumento().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setString(x++, obj.getSenhaCertificadoParaDocumento());
					sqlInserir.setInt(x++, obj.getQuantidadeCasaDecimalConsiderarNotaProcessoSeletivo());
					sqlInserir.setBoolean(x++, obj.getAssociarProspectSemConsultorResponsavelComPrimeiroConsultorInteragir());
					sqlInserir.setBoolean(x++, obj.getAssociarNovoProspectComConsultorResponsavelCadastro());
					sqlInserir.setBoolean(x++, obj.getPrimeiroAcessoAlunoResetarSenha());
					sqlInserir.setBoolean(x++, obj.getPrimeiroAcessoProfessorResetarSenha());		
					sqlInserir.setString(x++, obj.getUrlAcessoExternoAplicacao());
					sqlInserir.setBoolean(x++, obj.getBloquearLancamentosNotasAulasFeriadosFinaisSemana());
					sqlInserir.setBoolean(x++, obj.getPermitirProfessorRealizarLancamentoAlunosPreMatriculados());
					
					sqlInserir.setInt(x++, obj.getQuantidadeCaracteresMinimoSenhaUsuario());
					sqlInserir.setBoolean(x++, obj.getNivelSegurancaNumero());
					sqlInserir.setBoolean(x++, obj.getNivelSegurancaLetra());
					sqlInserir.setBoolean(x++, obj.getNivelSegurancaLetraMaiuscula());
					sqlInserir.setBoolean(x++, obj.getNivelSegurancaCaracterEspecial());
					sqlInserir.setInt(x++, obj.getNivelcontrolealteracaosenha());
					
					sqlInserir.setString(x++, obj.getIdClienteRdStation());
					sqlInserir.setString(x++, obj.getSenhaClienteRdStation());
					
					sqlInserir.setString(x++, obj.getTokenRdStation());
					sqlInserir.setString(x++, obj.getTokenPrivadoRdStation());
					sqlInserir.setBoolean(x++, obj.getAtivarIntegracaoRdStation());
					sqlInserir.setString(x++, obj.getIdentificadorRdStation());
					sqlInserir.setString(x++, obj.getTokenWebService());
					sqlInserir.setString(x++, obj.getLoginIntegracaoSofFin());
					sqlInserir.setString(x++, obj.getSenhaIntegracaoSofFin());
					sqlInserir.setString(x++, obj.getTokenIntegracaoSofFin());
					sqlInserir.setString(x++, obj.getAmbienteEnumIntegracaoSoftFin().name());
					
					// Servidor aqruivo online
					sqlInserir.setBoolean(x++, obj.getIntegracaoServidorOnline());					
					sqlInserir.setString(x++, obj.getServidorArquivoOnline());
					sqlInserir.setString(x++, obj.getUsuarioAutenticacao());
					sqlInserir.setString(x++, obj.getSenhaAutenticacao());
					sqlInserir.setString(x++, obj.getLocalUploadArquivoGED());
					
					sqlInserir.setBoolean(x++, obj.getPermitirAcessoAlunoPreMatricula());
					sqlInserir.setBoolean(x++, obj.getPermitirAcessoAlunoFormado());
					sqlInserir.setBoolean(x++, obj.getPermitirAcessoAlunoEvasao());
					sqlInserir.setInt(x++, obj.getPerfilPadraoAlunoPreMatricula().getCodigo());
					sqlInserir.setInt(x++, obj.getPerfilPadraoAlunoEvasao().getCodigo());
					sqlInserir.setInt(x++, obj.getPerfilPadraoAlunoFormado().getCodigo());
					sqlInserir.setString(x++, obj.getLinkAcessoVisoesMoodle());
					
					if (Uteis.isAtributoPreenchido(obj.getPerfilPadraoProfessorEducacaoInfantil().getCodigo().intValue())) {
						sqlInserir.setInt(x++, obj.getPerfilPadraoProfessorEducacaoInfantil().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}

					if (Uteis.isAtributoPreenchido(obj.getPerfilPadraoProfessorEnsinoFundamental().getCodigo().intValue())) {
						sqlInserir.setInt(x++, obj.getPerfilPadraoProfessorEnsinoFundamental().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					
					if (Uteis.isAtributoPreenchido(obj.getPerfilPadraoProfessorEnsinoMedio().getCodigo().intValue())) {
						sqlInserir.setInt(x++, obj.getPerfilPadraoProfessorEnsinoMedio().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					
					if (Uteis.isAtributoPreenchido(obj.getPerfilPadraoProfessorTecnicoProfissionalizante().getCodigo().intValue())) {
						sqlInserir.setInt(x++, obj.getPerfilPadraoProfessorTecnicoProfissionalizante().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					
					if (Uteis.isAtributoPreenchido(obj.getPerfilPadraoProfessorSequencial().getCodigo().intValue())) {
						sqlInserir.setInt(x++, obj.getPerfilPadraoProfessorSequencial().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					
					if (Uteis.isAtributoPreenchido(obj.getPerfilPadraoProfessorMestrado().getCodigo().intValue())) {
						sqlInserir.setInt(x++, obj.getPerfilPadraoProfessorMestrado().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}

					if (Uteis.isAtributoPreenchido(obj.getPerfilPadraoProfessorGraduacaoTecnologica().getCodigo().intValue())) {
						sqlInserir.setInt(x++, obj.getPerfilPadraoProfessorGraduacaoTecnologica().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}

					//Nome do Repositorio do Servidor Arquivo Online deve ser nulo pq ainda nao tem preenchido o campo ID Autenticacao
					if (Uteis.isAtributoPreenchido(obj.getIdAutenticacaoServOtimize())) {
						sqlInserir.setString(x++, obj.getNomeRepositorio());
			        } else {
			        	sqlInserir.setString(x++, null);
			        }
					
					sqlInserir.setString(x++, obj.getSiglaAbonoFalta());
					sqlInserir.setString(x++, obj.getDescricaoAbonoFalta());
					sqlInserir.setBoolean(x++, obj.getPermiteReativacaoMatriculaSemRequerimento().booleanValue());
					sqlInserir.setInt(x++, obj.getQtdTentativasFalhaLogin());
					sqlInserir.setInt(x++, obj.getTempoBloqTentativasFalhaLogin());
					sqlInserir.setBoolean(x++, obj.getDefinirPerfilAcessoAlunoNaoAssinouContratoMatricula());
					if (Uteis.isAtributoPreenchido(obj.getPerfilAcessoAlunoNaoAssinouContratoMatricula().getCodigo().intValue())) {
						sqlInserir.setInt(x++, obj.getPerfilAcessoAlunoNaoAssinouContratoMatricula().getCodigo().intValue());
					}
					else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setBoolean(x++, obj.getApresentarMensagemAlertaAlunoNaoAssinouContrato());
					sqlInserir.setString(x++, obj.getMensagemAlertaAlunoNaoAssinouContratoMatricula());
					sqlInserir.setInt(x++, obj.getPercentualBaixaFrequencia());
					sqlInserir.setInt(x++, obj.getQtdeCaractereLimiteDownloadMaterial());			
					sqlInserir.setBoolean(x++, obj.getLogUploadProfessor());
					sqlInserir.setBoolean(x++, obj.getApresentarBotoesAcoesRequerimentoApenasAbaInicial());
					sqlInserir.setString(x++, obj.getUrlHomeCandidato());
					if (Uteis.isAtributoPreenchido(obj.getConfiguracaoAparenciaSistema().getCodigo().intValue())) {
						sqlInserir.setInt(x++, obj.getConfiguracaoAparenciaSistema().getCodigo().intValue());
					}
					else {
						sqlInserir.setNull(x++, 0);
					}					
					sqlInserir.setString(x++, obj.getTextoParaOrientacaoTcc());
					sqlInserir.setString(x++, obj.getTextoParaOrientacaoProjetoIntegrador());
					if (Uteis.isAtributoPreenchido(obj.getMotivoPadraoCancelamentoPreMatriculaCalouro().getCodigo().intValue())) {
						sqlInserir.setInt(x++, obj.getMotivoPadraoCancelamentoPreMatriculaCalouro().getCodigo().intValue());
					}
					else {
						sqlInserir.setNull(x++, 0);
					}	
					sqlInserir.setString(x++, obj.getHeaderBarIntegracaoSistemasProvaMestreGR());
					sqlInserir.setString(x++, obj.getActionIntegracaoSistemasProvaMestreGR());
					sqlInserir.setString(x++, obj.getTokenIntegracaoSistemasProvaMestreGR());
					sqlInserir.setBoolean(x++, obj.getHabilitarIntegracaoSistemaProvas());
					
					sqlInserir.setBoolean(x++, obj.getAtivarIntegracaoLGPD());
					sqlInserir.setString(x++, obj.getNomeDestinatarioEmailIntegracaoLGPD());
					sqlInserir.setString(x++, obj.getEmailDestinatarioIntegracaoLGPD());
					sqlInserir.setString(x++, obj.getAssuntoMensagemLGPD());
					sqlInserir.setString(x++, obj.getMensagemLGPD());
					
					if(Uteis.isAtributoPreenchido(obj.getTextoPadraoDadosSensiveisLGPD().getCodigo().intValue())) {
						sqlInserir.setInt(x++, obj.getTextoPadraoDadosSensiveisLGPD().getCodigo().intValue());
							
					}else {
						sqlInserir.setNull(x++, 0);
					}

					if (Uteis.isAtributoPreenchido(obj.getQuestionarioPlanoEnsino().getCodigo().intValue())) {
						sqlInserir.setInt(x++, obj.getQuestionarioPlanoEnsino().getCodigo().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setString(x++, obj.getTextoOrientacaoCancelamentoPorOutraMatricula());
					if (Uteis.isAtributoPreenchido(obj.getMotivoPadraoCancelamentoOutraMatricula().getCodigo().intValue())) {
						sqlInserir.setInt(x++, obj.getMotivoPadraoCancelamentoOutraMatricula().getCodigo().intValue());
					}
					else {
						sqlInserir.setNull(x++, 0);
					}					
					sqlInserir.setString(x++, obj.getJustificativaCancelamentoPorOutraMatricula());
					sqlInserir.setBoolean(x++, obj.getHabilitarRecursoInativacaoCredenciasAlunosFormados());
					sqlInserir.setBoolean(x++, obj.getApresentarDocumentoPortalTransparenciaComPendenciaAssinatura());
					sqlInserir.setInt(x++, obj.getTamanhoMaximoCorpoMensagem());
					sqlInserir.setInt(x++, obj.getLimiteDestinatariosPorEmail());
					sqlInserir.setInt(x++, obj.getTamanhoLimiteAnexoEmail());
					sqlInserir.setDate(x++, Uteis.getDataJDBC(obj.getDataBaseValidacaoDiplomaDigital()));
					sqlInserir.setBoolean(x++, obj.getHabilitarIntegracaoSistemaSymplicty());
					sqlInserir.setString(x++, obj.getHostIntegracaoSistemaSymplicty());
					sqlInserir.setString(x++, obj.getUserIntegracaoSistemaSymplicty());
					sqlInserir.setString(x++, obj.getPassIntegracaoSistemaSymplicty());
					sqlInserir.setInt(x++, obj.getPortIntegracaoSistemaSymplicty());
					sqlInserir.setString(x++, obj.getProtocolIntegracaoSistemaSymplicty());
					sqlInserir.setString(x++, obj.getPastaDestinoRemotaSymplicty());
					sqlInserir.setBoolean(x++, obj.getHabilitarOperacoesTempoRealIntegracaoMestreGR());
					sqlInserir.setString(x++, obj.getIntegracaoMestreGRURLBaseAPI());
					sqlInserir.setBoolean(x++, obj.getHabilitarRecursosAcademicosVisaoAluno());
					sqlInserir.setBoolean(x++, obj.getHabilitarMonitoramentoSentry());
					sqlInserir.setString(x++, obj.getTokenSentry());
					sqlInserir.setBoolean(x++, obj.getHabilitarMonitoramentoBlackboardSentry());
					sqlInserir.setString(x++, obj.getTokenBlackboardSentry());
					sqlInserir.setBoolean(x++, obj.getHabilitarEnvioEmailAssincrono());
					sqlInserir.setBoolean(x++, obj.getAtivarDebugEmail());
					sqlInserir.setLong(x++, obj.getTimeOutFilaEmail());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
			persistirConfiguracaoAtualizacaoCadastral(obj, usuarioLogado);
			persistirConfiguracaoCandidatoProcessoSeletivo(obj, usuarioLogado);
			getFacadeFactory().getConfiguracaoLdapInterfaceFacade().incluirConfiguracaoLdaps(obj.getCodigo(), obj.getConfiguracaoLdapVOs());
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirConfiguracaoAtualizacaoCadastral(final ConfiguracaoGeralSistemaVO obj, final UsuarioVO usuarioLogado) throws Exception {
		obj.getConfiguracaoAtualizacaoCadastralVO().setConfiguracaoGeralSistema(obj.getCodigo());
		if (obj.getConfiguracaoAtualizacaoCadastralVO().getCodigo().intValue() > 0) {
			getFacadeFactory().getConfiguracaoAtualizacaoCadastralFacade().alterar(obj.getConfiguracaoAtualizacaoCadastralVO(), usuarioLogado);
		} else {
			getFacadeFactory().getConfiguracaoAtualizacaoCadastralFacade().incluir(obj.getConfiguracaoAtualizacaoCadastralVO(), usuarioLogado);
		}
	}
	
	/**
	 * Método para a persistencia do objeto ConfiguracaoCandidatoProcessoSeletivoVO
	 * @param obj
	 * @param usuarioLogado
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirConfiguracaoCandidatoProcessoSeletivo(final ConfiguracaoGeralSistemaVO obj, final UsuarioVO usuarioLogado) throws Exception {
		obj.getConfiguracaoCandidatoProcessoSeletivoVO().setConfiguracaoGeralSistema(obj.getCodigo());
		if (obj.getConfiguracaoCandidatoProcessoSeletivoVO().getCodigo().intValue() > 0) {
			getFacadeFactory().getConfiguracaoCandidatoProcessoSeletivoInterfaceFacade().alterar(obj.getConfiguracaoCandidatoProcessoSeletivoVO(), usuarioLogado);
		} else {
			getFacadeFactory().getConfiguracaoCandidatoProcessoSeletivoInterfaceFacade().incluir(obj.getConfiguracaoCandidatoProcessoSeletivoVO(), usuarioLogado);
		}
	}
	
	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ConfiguracaoGeralSistemaVO</code>. Sempre utiliza a chave primária
	 * da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ConfiguracaoGeralSistemaVO</code> que
	 *            será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ConfiguracaoGeralSistemaVO obj,  final UsuarioVO usuarioLogado) throws Exception {
		try {
			ConfiguracaoGeralSistemaVO.validarDados(obj);
			ConfiguracaoGeralSistemaVO.validarDadosIntegracaoAmazon(obj);			
		   if (Uteis.isAtributoPreenchido(obj.getCertificadoParaDocumento().getDescricao()) && obj.getCertificadoParaDocumento().getPastaBaseArquivoEnum() != null && obj.getCertificadoParaDocumento().getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.CERTIFICADO_DOCUMENTOS_TMP)) {
				obj.getCertificadoParaDocumento().setValidarDados(false);
				getFacadeFactory().getArquivoFacade().persistir(obj.getCertificadoParaDocumento(), false, usuarioLogado, obj);
			}
			final String sql = "UPDATE ConfiguracaoGeralSistema set validarCadastroCpf=?, smptPadrao=?, portaSmtpPadrao=?, emailRemetente=?, login=?, senha=?, visaoPadraoAluno=?, " // 1-6
					+ "visaoPadraoProfessor=?, visaoPadraoCandidato=?, perfilPadraoAluno=?, perfilPadraoProfessorGraduacao=?, perfilPadraoCandidato=?, qtdeLimiteMsg=?, " // 7-12
					+ "questionario=?, perfilEconomicoPadrao=?, configuracoes=?, permiteCancelamentoSemRequerimento=?, permiteTrancamentoSemRequerimento=?, " // 13-17
					+ "permiteTransferenciaSemRequerimento=?, permiteAproveitamentoDisciplinaSemRequerimento=?, permiteProgramacaoFormaturaSemRequerimento=?, " // 18-20
					+ "nrmaximofolharecibo=?, permiteTransferenciaInternaSemRequerimento=?, permitePortadorDiplomaSemRequerimento=?, permiteAlunoVerContasConvenio=?, " // 21-24
					+ "prefixoMatriculaFuncionario=?, prefixoMatriculaProfessor=?, localUploadArquivoFixo=?, localUploadArquivoTemp=?, enderecoServidorArquivo=?, " // 25-29
					+ "descricaooVersoCarteirinhaEstudantil=?,controlarValidadeCarteirinhaEstudantil=?, mensagemTelaLogin=?, apresentarMensagemTelaLogin=?, " // 30-33
					+ "urlExternoDownloadArquivo=?, urlWebserviceNFe=?, urlWebserviceNFSe=?, mascaraSubRede=?, gerarSenhaCpfAluno = ?, linkFacebook=?, linkLinkdIn=?, " // 34-39
					+ "linkTwitter=?, codigoTwitts=?, perfilpadraocoordenador=?, visaopadraocoordenador=?, perfilPadraoProfessorPosGraduacao=?, " // 40-44
					+ "qtdeDiasAlertaRequerimento=?, servidorEmailUtilizaSSL=?, textoComunicacaoInterna=?, apresentarHomeCandidato=?, mensagemErroSenha=?, " // 45-49
					+ " monitorarmensagensprofessor=?, emailConfirmacaoEnvioComunicado=?, perfilPadraoParceiro=?," + " qtdDiasExpiracaoVagaBancoCurriculum=?, qtdDiasNotificacaoExpiracaoVagaBancoCurriculum=?, tituloTelaBancoCurriculum=?, mensagemTelaBancoCurriculos=?, tituloTelaBuscaCandidato=?, usernameSMS=?, senhaSMS=?, fornecedorSMS=?, "// 52-54
					+ "  qtdAceitavelContatosPendentesNaoIniciados=?, qtdLimiteContatosPendentesNaoIniciados=?, qtaAceitavelContatosPendentesNaoFinalizados=?, qtaLimiteContatosPendentesNaoFinalizados=?, responsavelPadraoComunicadoInterno=?,  "// 52-54
					+ "permiteInclusaoForaPrazoMatriculaPeriodoAtiva=?, naoPermitirExpedicaoDiplomaDocumentacaoPendente=?, naoPermitirAlterarUsernameUsuario=?, visaoPadraoPais=?, perfilPadraoPais=?, " + " permiteRenovarComParcelaVencida=?, mensagemPadraoRenovacaoMatriculaComParcelaVencida = ?, qtdeAntecedenciaDiasNotificarAlunoDownloadMaterial=?, ipServidor=?, diasParaRemoverPreMatricula=?, " + "qtdeMsgLimiteServidorNotificacao=?, permiteRenovarAlunoPreMatriculadoParaNovaMatriculaPreMatricula=?, avaliacaoInstitucionalFinalModuloAluno=?, avaliacaoInstitucionalFinalModuloProfessor=?, servidorEmailUtilizaTSL=?, utilizarCaixaAltaNomePessoa=?, " + " controlaQtdDisciplinaExtensao=?, qtdDisciplinaExtensao=?, departamentoRespServidorNotificacao=?, tipoRequerimento=?, idAutenticacaoServOtimize=?, "
					+ " perfilPadraoOuvidoria=?, tamanhoMaximoUpload = ?, naoApresentarProfessorVisaoAluno=?, ocultarCPFPreInscricao = ?, ocultarEnderecoPreInscricao=? , controlaQtdDisciplinaRealizadaAteMatricula=?, qtdDisciplinaRealizadaAteMatricula=?, primeiroAcessoAlunoCairMeusDados=?, qtdDiasAcessoAlunoFormado=?, qtdDiasAcessoAlunoExtensao=?, qtdeDiasNotificarProfessorPostarMaterial=?, apresentarEsqueceuMinhaSenha=?, qtdDiasAcessoAlunoAtivo=?, " + " matricularalunoreprovfaltaemdisciplinajarealizadaposgradext=?, funcionarioRespAlteracaoDados=?, nrDiasLimiteEntregaDocumento=?, grupoDestinatarioMapaLocalAula=?, paisPadrao=?, todosCamposObrigatoriosPreInscricao=?, maeFiliacao=?, notificarAlunoAniversariante=?, notificarProfessorAniversariante=?, notificarFuncionarioAniversariante=?, notificarPaiAniversariante=?, controlaAprovacaoDocEntregue=?, notificarExAlunoAniversariante=?, obrigarTipoMidiaProspect=?, urlConfirmacaoPreInscricao=?,  "

					+ " qtdDiasMaximoAntecedenciaRemarcarAulaReposicao=?, calcularMediaAoGravar=?, permitiAlunoPreMatriculaSolicitarRequerimento=?, qtdeDiasNotificacaoDataProva=?, validarQtdAlunoLocalSalaAulaInclusaoReposicaoDisciplina=?, " + " qtdeDiaNotificarAbandonoCurso=?, qtdeDiaRegistrarAbandonoCurso=?, motivoPadraoAbandonoCurso=?, " + " ocultarEmailPreInscricao=?, ocultarRG=?, ocultarDataNasc=?, ocultarSexo=?, ocultarTelefone=?, ocultarEstadoCivil=?, ocultarNaturalidade=?, ocultarFormacaoAcademica=?,diasParaNotificarCoordenadorInicioTurma=? ,bloquearMatriculaPosSemGraduacao=?, qtdEmailEnvio=?, "
					+ "bloquearRealizarTrancamentoComEmprestimoBiblioteca=?, bloquearRealizarAbandonoCursoComEmprestimoBiblioteca=?, bloquearRealizarCancelamentoComEmprestimoBiblioteca=?, bloquearRealizarTransferenciaSaidaComEmprestimoBiblioteca=?, bloquearRealizarTransferenciaInternaComEmprestimoBiblioteca=?, bloquearRealizarRenovacaoComEmprestimoBiblioteca=?, verificarAulaProgramadaIncluirDisciplina=?, perfilPadraoProfessorExtensao=?, desconsiderarAlunoReposicaoVagasTurma=?, permiteTransferenciaSaidaSemRequerimento=?, " // 9;
					+ "apresentarAlunoPendenteFinanceiroVisaoProfessor = ?, apresentarAlunoPendenteFinanceiroVisaoCoordenador = ?, ipServidorBiometria = ?, " 
					+ "perfilPadraoAlunoEducacaoInfantil=?, perfilPadraoAlunoEducacaoFundamental=?, perfilPadraoAlunoEducacaoMedio=?, perfilPadraoAlunoEducacaoGraduacao=?, perfilPadraoAlunoEducacaoGraduacaoTecnologica=?, perfilPadraoAlunoEducacaoSequencial=?, perfilPadraoAlunoEducacaoExtensao=?, perfilPadraoAlunoEducacaoPosGraduacao=?, perfilPadraoAlunoEducacaoTecnicoProfissionalizante=?, perfilPadraoPaisEducacaoInfantil=?, perfilPadraoPaisEducacaoFundamental=?, perfilPadraoPaisEducacaoMedio=?, perfilPadraoPaisEducacaoGraduacao=?, perfilPadraoPaisEducacaoGraduacaoTecnologica=?, perfilPadraoPaisEducacaoSequencial=?, perfilPadraoPaisEducacaoExtensao=?, perfilPadraoPaisEducacaoPosGraduacao=?, perfilPadraoPaisEducacaoTecnicoProfissionalizante=?, "
					+ "controlarNumeroAulaProgramadoProfessorPorDia = ?, quantidadeAulaMaximaProgramarProfessorDia = ?, usuarioResponsavelOperacoesExternas=?, incrementarNumeroExemplarPorBiblioteca=? , zerarNumeroRegistroPorCurso=? , "
					+ "criarProspectFiliacao=?, criarProspectAluno=?, criarProspectCandidato=?, criarProspectFuncionario=?, ocultarMediaProcSeletivo=?, ocultarClassificacaoProcSeletivo=?, "
                    + "controlarCargaHorariaMaximaEstagioObrigatorio=?, cargaHorariaMaximaSemanal=?, cargaHorariaMaximaDiaria=?, seguradoraPadraoEstagioObrigatorio=?, apolicePadraoEstagioObrigatorio=?, forcarSeguradoraEApoliceParaEstagioObrigatorio=?, realizarCalculoMediaFinalFechPeriodo=? , ocultarChamadaCandidatoProcSeletivo=?, nrDiasNotifVencCand=?,  "
                    + "certificadoparadocumento=?, senhacertificadoparadocumento=?, quantidadeCasaDecimalConsiderarNotaProcessoSeletivo=?, associarprospectsemconsultorresponsavelcomprimeiroconsultor=?, associarnovoprospectcomconsultorresponsavelcadastro=? , primeiroAcessoAlunoResetarSenha=?, primeiroAcessoProfessorResetarSenha=?, urlAcessoExternoAplicacao=?, bloquearLancamentosNotasAulasFeriadosFinaisSemana=?, permitirProfessorRealizarLancamentoAlunosPreMatriculados=?, "
                    + "quantidadeCaracteresMinimoSenhaUsuario = ?, nivelSegurancaNumero = ?, nivelSegurancaLetra = ?, nivelSegurancaLetraMaiuscula = ?, nivelSegurancaCaracterEspecial = ?, nivelcontrolealteracaosenha = ?, idClienteRdStation = ?, senhaClienteRdStation = ?, tokenRdStation = ?, tokenPrivadoRdStation = ?, ativarIntegracaoRdStation = ?, identificadorRdStation = ?, tokenWebService = ?, "
                    + "loginIntegracaoSofFin=?, senhaIntegracaoSofFin=?, tokenIntegracaoSofFin=?, ambienteEnumIntegracaoSoftFin=?, "
                    + "integracaoServidorOnline=?, servidorArquivoOnline=?, usuarioAutenticacao=?, senhaAutenticacao=? , localUploadArquivoGED=?, permitirAcessoAlunoPreMatricula=?, permitirAcessoAlunoFormado=?, permitirAcessoAlunoEvasao=?, perfilPadraoAlunoPreMatricula=?, perfilPadraoAlunoEvasao=?, perfilPadraoAlunoFormado=?, linkAcessoVisoesMoodle=? ,"
                    + "perfilPadraoProfessorEducacaoInfantil = ?, perfilPadraoProfessorEnsinoFundamental = ?, perfilPadraoProfessorEnsinoMedio = ?, perfilPadraoProfessorTecnicoProfissionalizante = ?, perfilPadraoProfessorSequencial = ?, perfilPadraoProfessorMestrado = ?, perfilPadraoProfessorGraduacaoTecnologica = ?, "
                    + "nomeRepositorio=?, siglaAbonoFalta=?, descricaoAbonoFalta=?, permiteReativacaoMatriculaSemRequerimento=?, qtdTentativasFalhaLogin=?, tempoBloqTentativasFalhaLogin=?, "
                    + " definirPerfilAcessoAlunoNaoAssinouContratoMatricula=?, perfilAcessoAlunoNaoAssinouContratoMatricula=?, apresentarMensagemAlertaAlunoNaoAssinouContrato=?, mensagemAlertaAlunoNaoAssinouContratoMatricula=?, percentualBaixaFrequencia=?, qtdeCaractereLimiteDownloadMaterial=? , apresentarBotoesAcoesRequerimentoApenasAbaInicial=?, urlHomeCandidato=?, configuracaoAparenciaSistema = ? ,  textoParaOrientacaoTcc=?, textoParaOrientacaoProjetoIntegrador=?, motivoPadraoCancelamentoPreMatriculaCalouro = ? , "
                    + " headerBarIntegracaoSistemasProvaMestreGR=? , actionIntegracaoSistemasProvaMestreGR=? , tokenIntegracaoSistemasProvaMestreGR=?  , habilitarIntegracaoSistemaProvas=? , ativarIntegracaoLGPD=?, nomeDestinatarioEmailIntegracaoLGPD=?, emailDestinatarioIntegracaoLGPD=?, assuntoMensagemLGPD=?, mensagemLGPD=?, textopadraodeclaracao=?, questionarioplanoensino = ? ," 
                    + " textoOrientacaoCancelamentoPorOutraMatricula=? , motivopadraocancelamentooutramatricula=? , justificativaCancelamentoPorOutraMatricula=? , habilitarRecursoInativacaoCredenciasAlunosFormados=?, apresentarDocumentoPortalTransparenciaComPendenciaAssinatura=?, tamanhoMaximoCorpoMensagem=?, limitedestinatariosporemail=?, tamanholimiteanexoemail=?, databasevalidacaodiplomadigital=?, habilitarintegracaosistemasymplicty=?, hostintegracaosistemasymplicty=?, userintegracaosistemasymplicty=?, passintegracaosistemasymplicty=?, portintegracaosistemasymplicty=?, protocolintegracaosistemasymplicty=?, pastadestinoremotasymplicty=?, habilitaroperacoestemporealintegracaomestregr=?, integracaomestregrurlbaseapi=?, habilitarrecursosacademicosvisaoaluno=?, habilitarmonitoramentosentry=?, tokensentry=?, habilitarmonitoramentoblackboardsentry=?, tokenblackboardsentry=?,  "
					+ " habilitarEnvioEmailAssincrono =?, ativarDebugEmail = ?, timeOutFilaEmail = ? "                    + " WHERE ((codigo = ?))";

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					int x = 1;
					sqlAlterar.setBoolean(x++, obj.isValidarCadastroCpf().booleanValue());
					sqlAlterar.setString(x++, obj.getSmptPadrao());
					sqlAlterar.setString(x++, obj.getPortaSmtpPadrao());
					sqlAlterar.setString(x++, obj.getEmailRemetente());
					sqlAlterar.setString(x++, obj.getLogin());
					sqlAlterar.setString(x++, obj.getSenha());
					if (obj.getVisaoPadraoAluno().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getVisaoPadraoAluno().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (obj.getVisaoPadraoProfessor().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getVisaoPadraoProfessor().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (obj.getVisaoPadraoCandidato().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getVisaoPadraoCandidato().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (obj.getPerfilPadraoAluno().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getPerfilPadraoAluno().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (obj.getPerfilPadraoProfessorGraduacao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getPerfilPadraoProfessorGraduacao().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (obj.getPerfilPadraoCandidato().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getPerfilPadraoCandidato().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setInt(x++, obj.getQtdeLimiteMsg().intValue());
					if (obj.getQuestionarioPerfilSocioEconomico().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getQuestionarioPerfilSocioEconomico().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (obj.getPerfilEconomicoPadrao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getPerfilEconomicoPadrao().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (obj.getConfiguracoesVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getConfiguracoesVO().getCodigo());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setBoolean(x++, obj.getPermiteCancelamentoSemRequerimento().booleanValue());
					sqlAlterar.setBoolean(x++, obj.getPermiteTrancamentoSemRequerimento().booleanValue());
					sqlAlterar.setBoolean(x++, obj.getPermiteTransferenciaSemRequerimento().booleanValue());
					sqlAlterar.setBoolean(x++, obj.getPermiteAproveitamentoDiscSemRequerimento().booleanValue());
					sqlAlterar.setBoolean(x++, obj.getPermiteProgramacaoFormaturaSemRequerimento().booleanValue());
					sqlAlterar.setInt(x++, obj.getNrMaximoFolhaRecibo().intValue());
					sqlAlterar.setBoolean(x++, obj.getPermiteTransferenciaInternaSemRequerimento().booleanValue());
					sqlAlterar.setBoolean(x++, obj.getPermitePortadorDiplomaSemRequerimento().booleanValue());
					sqlAlterar.setBoolean(x++, obj.getPermiteAlunoVerContasConvenio().booleanValue());
					sqlAlterar.setString(x++, obj.getPrefixoMatriculaFuncionario());
					sqlAlterar.setString(x++, obj.getPrefixoMatriculaProfessor());
					sqlAlterar.setString(x++, obj.getLocalUploadArquivoFixo());
					sqlAlterar.setString(x++, obj.getLocalUploadArquivoTemp());
					sqlAlterar.setString(x++, obj.getEnderecoServidorArquivo());
					sqlAlterar.setString(x++, obj.getDescricaooVersoCarteirinhaEstudantil());
					sqlAlterar.setBoolean(x++, obj.getControlarValidadeCarteirinhaEstudantil());
					sqlAlterar.setString(x++, obj.getMensagemTelaLogin());
					sqlAlterar.setBoolean(x++, obj.getApresentarMensagemTelaLogin());
					sqlAlterar.setString(x++, obj.getUrlExternoDownloadArquivo());
					sqlAlterar.setString(x++, obj.getUrlWebserviceNFe());
					sqlAlterar.setString(x++, obj.getUrlWebserviceNFSe());
					sqlAlterar.setString(x++, obj.getMascaraSubRede());
					sqlAlterar.setBoolean(x++, obj.getGerarSenhaCpfAluno());
					sqlAlterar.setString(x++, obj.getLinkFacebook());
					sqlAlterar.setString(x++, obj.getLinkLinkdIn());
					sqlAlterar.setString(x++, obj.getLinkTwitter());
					sqlAlterar.setString(x++, obj.getCodigoTwitts());
					if (obj.getPerfilPadraoCoordenador().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getPerfilPadraoCoordenador().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (obj.getVisaoPadraoCoordenador().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getVisaoPadraoCoordenador().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (obj.getPerfilPadraoProfessorPosGraduacao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getPerfilPadraoProfessorPosGraduacao().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setInt(x++, obj.getQtdeDiasAlertaRequerimento());
					sqlAlterar.setBoolean(x++, obj.getServidorEmailUtilizaSSL());
					sqlAlterar.setString(x++, obj.getTextoComunicacaoInterna());
					sqlAlterar.setBoolean(x++, obj.getApresentarHomeCandidato());
					sqlAlterar.setString(x++, obj.getMensagemErroSenha());

					sqlAlterar.setBoolean(x++, obj.getMonitorarMensagensProfessor());
					sqlAlterar.setString(x++, obj.getEmailConfirmacaoEnvioComunicado());
					if (obj.getPerfilPadraoParceiro().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getPerfilPadraoParceiro().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setInt(x++, obj.getQtdDiasExpiracaoVagaBancoCurriculum());
					sqlAlterar.setInt(x++, obj.getQtdDiasNotificacaoExpiracaoVagaBancoCurriculum());
					sqlAlterar.setString(x++, obj.getTituloTelaBancoCurriculum());
					sqlAlterar.setString(x++, obj.getMensagemTelaBancoCurriculum());
					sqlAlterar.setString(x++, obj.getTituloTelaBuscaCandidato());
					sqlAlterar.setString(x++, obj.getUsernameSMS());
					sqlAlterar.setString(x++, obj.getSenhaSMS());
					sqlAlterar.setString(x++, obj.getFornecedorSMSEnum().name());
					sqlAlterar.setInt(x++, obj.getQtdAceitavelContatosPendentesNaoIniciados());
					sqlAlterar.setInt(x++, obj.getQtdLimiteContatosPendentesNaoIniciados());
					sqlAlterar.setInt(x++, obj.getQtaAceitavelContatosPendentesNaoFinalizados());
					sqlAlterar.setInt(x++, obj.getQtaLimiteContatosPendentesNaoFinalizados());
					sqlAlterar.setInt(x++, obj.getResponsavelPadraoComunicadoInterno().getCodigo());
					sqlAlterar.setBoolean(x++, obj.getPermiteInclusaoForaPrazoMatriculaPeriodoAtiva());
					sqlAlterar.setBoolean(x++, obj.getNaoPermitirExpedicaoDiplomaDocumentacaoPendente());
					sqlAlterar.setBoolean(x++, obj.getNaoPermitirAlterarUsernameUsuario());
					if (obj.getVisaoPadraoPais().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getVisaoPadraoPais().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (obj.getPerfilPadraoPais().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getPerfilPadraoPais().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setBoolean(x++, obj.getPermiteRenovarComParcelaVencida());
					sqlAlterar.setString(x++, obj.getMensagemPadraoRenovacaoMatriculaComParcelaVencida());
					sqlAlterar.setInt(x++, obj.getQtdeAntecedenciaDiasNotificarAlunoDownloadMaterial());
					sqlAlterar.setString(x++, obj.getIpServidor());
					sqlAlterar.setInt(x++, obj.getDiasParaRemoverPreMatricula());
					sqlAlterar.setInt(x++, obj.getQtdeMsgLimiteServidorNotificacao());
					sqlAlterar.setBoolean(x++, obj.getPermiteRenovarAlunoPreMatriculadoParaNovaMatriculaPreMatricula());
					sqlAlterar.setInt(x++, obj.getAvaliacaoInstitucionalFinalModuloAluno());
					sqlAlterar.setInt(x++, obj.getAvaliacaoInstitucionalFinalModuloProfessor());
					sqlAlterar.setBoolean(x++, obj.getServidorEmailUtilizaTSL());
					sqlAlterar.setBoolean(x++, obj.getUtilizarCaixaAltaNomePessoa());
					sqlAlterar.setBoolean(x++, obj.getControlaQtdDisciplinaExtensao());
					sqlAlterar.setInt(x++, obj.getQtdDisciplinaExtensao());
					sqlAlterar.setInt(x++, obj.getDepartamentoRespServidorNotificacao());
					sqlAlterar.setInt(x++, obj.getTipoRequerimentoVO().getCodigo());
					sqlAlterar.setString(x++, obj.getIdAutenticacaoServOtimize());
					if (obj.getPerfilPadraoOuvidoria().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getPerfilPadraoOuvidoria().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setInt(x++, obj.getTamanhoMaximoUpload());
					sqlAlterar.setBoolean(x++, obj.getNaoApresentarProfessorVisaoAluno());
					sqlAlterar.setBoolean(x++, obj.getOcultarCPFPreInscricao());
					sqlAlterar.setBoolean(x++, obj.getOcultarEnderecoPreInscricao());
					sqlAlterar.setBoolean(x++, obj.getControlaQtdDisciplinaRealizadaAteMatricula());
					sqlAlterar.setInt(x++, obj.getQtdDisciplinaRealizadaAteMatricula());
					sqlAlterar.setBoolean(x++, obj.getPrimeiroAcessoAlunoCairMeusDados());
					sqlAlterar.setInt(x++, obj.getQtdDiasAcessoAlunoFormado());
					sqlAlterar.setInt(x++, obj.getQtdDiasAcessoAlunoExtensao());
					sqlAlterar.setInt(x++, obj.getQtdeDiasNotificarProfessorPostarMaterial());
					sqlAlterar.setBoolean(x++, obj.getApresentarEsqueceuMinhaSenha());
					sqlAlterar.setInt(x++, obj.getQtdDiasAcessoAlunoAtivo());
					sqlAlterar.setBoolean(x++, obj.getMatricularAlunoReprovFaltaEmDisciplinaJaRealizadaPosGraduacaoExtensao());
					if (obj.getFuncionarioRespAlteracaoDados().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getFuncionarioRespAlteracaoDados().getCodigo());
					} else {
						sqlAlterar.setNull(x++, 0);
					}

					sqlAlterar.setInt(x++, obj.getNrDiasLimiteEntregaDocumento());
					if (obj.getGrupoDestinatarioMapaLocalAula().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getGrupoDestinatarioMapaLocalAula().getCodigo());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (obj.getPaisPadrao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getPaisPadrao().getCodigo());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setBoolean(x++, obj.getTodosCamposObrigatoriosPreInscricao().booleanValue());
					sqlAlterar.setBoolean(x++, obj.getMaeFiliacao().booleanValue());
					sqlAlterar.setBoolean(x++, obj.getNotificarAlunoAniversariante().booleanValue());
					sqlAlterar.setBoolean(x++, obj.getNotificarProfessorAniversariante().booleanValue());
					sqlAlterar.setBoolean(x++, obj.getNotificarFuncionarioAniversariante().booleanValue());
					sqlAlterar.setBoolean(x++, obj.getNotificarPaiAniversariante().booleanValue());
					sqlAlterar.setBoolean(x++, obj.getControlaAprovacaoDocEntregue().booleanValue());
					sqlAlterar.setBoolean(x++, obj.getNotificarExAlunoAniversariante().booleanValue());
					sqlAlterar.setBoolean(x++, obj.getObrigarTipoMidiaProspect().booleanValue());
					sqlAlterar.setString(x++, obj.getUrlConfirmacaoPreInscricao());
					
					sqlAlterar.setInt(x++, obj.getQtdDiasMaximoAntecedenciaRemarcarAulaReposicao());
					sqlAlterar.setBoolean(x++, obj.getCalcularMediaAoGravar());
					sqlAlterar.setBoolean(x++, obj.getPermitiAlunoPreMatriculaSolicitarRequerimento());
					sqlAlterar.setInt(x++, obj.getQtdeDiasNotificacaoDataProva());
					sqlAlterar.setBoolean(x++, obj.getValidarQtdAlunoLocalSalaAulaInclusaoReposicaoDisciplina());
					sqlAlterar.setInt(x++, obj.getQtdeDiaNotificarAbandonoCurso());
					sqlAlterar.setInt(x++, obj.getQtdeDiaRegistrarAbandonoCurso());
					if (obj.getMotivoPadraoAbandonoCurso().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getMotivoPadraoAbandonoCurso().getCodigo());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setBoolean(x++, obj.getOcultarEmailPreInscricao());
					sqlAlterar.setBoolean(x++, obj.getOcultarRG());
					sqlAlterar.setBoolean(x++, obj.getOcultarDataNasc());
					sqlAlterar.setBoolean(x++, obj.getOcultarSexo());
					sqlAlterar.setBoolean(x++, obj.getOcultarTelefone());
					sqlAlterar.setBoolean(x++, obj.getOcultarEstadoCivil());
					sqlAlterar.setBoolean(x++, obj.getOcultarNaturalidade());
					sqlAlterar.setBoolean(x++, obj.getOcultarFormacaoAcademica());

					sqlAlterar.setInt(x++, obj.getDiasParaNotificarCoordenadorInicioTurma());
					sqlAlterar.setBoolean(x++, obj.getBloquearMatriculaPosSemGraduacao());
					sqlAlterar.setInt(x++, obj.getQtdEmailEnvio());
					sqlAlterar.setBoolean(x++, obj.getBloquearRealizarTrancamentoComEmprestimoBiblioteca());
					sqlAlterar.setBoolean(x++, obj.getBloquearRealizarAbandonoCursoComEmprestimoBiblioteca());
					sqlAlterar.setBoolean(x++, obj.getBloquearRealizarCancelamentoComEmprestimoBiblioteca());
					sqlAlterar.setBoolean(x++, obj.getBloquearRealizarTransferenciaSaidaComEmprestimoBiblioteca());
					sqlAlterar.setBoolean(x++, obj.getBloquearRealizarTransferenciaInternaComEmprestimoBiblioteca());
					sqlAlterar.setBoolean(x++, obj.getBloquearRealizarRenovacaoComEmprestimoBiblioteca());
					sqlAlterar.setBoolean(x++, obj.getVerificarAulaProgramadaIncluirDisciplina());
					if (obj.getPerfilPadraoProfessorExtensao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getPerfilPadraoProfessorExtensao().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setBoolean(x++, obj.getDesconsiderarAlunoReposicaoVagasTurma());
					sqlAlterar.setBoolean(x++, obj.getPermiteTransferenciaSaidaSemRequerimento().booleanValue());
					sqlAlterar.setBoolean(x++, obj.getApresentarAlunoPendenteFinanceiroVisaoProfessor());
					sqlAlterar.setBoolean(x++, obj.getApresentarAlunoPendenteFinanceiroVisaoCoordenador());
					sqlAlterar.setString(x++, obj.getIpServidorBiometria());
					sqlAlterar.setInt(x++, obj.getPerfilPadraoAlunoEducacaoInfantil().getCodigo());
					sqlAlterar.setInt(x++, obj.getPerfilPadraoAlunoEducacaoFundamental().getCodigo());
					sqlAlterar.setInt(x++, obj.getPerfilPadraoAlunoEducacaoMedio().getCodigo());
					sqlAlterar.setInt(x++, obj.getPerfilPadraoAlunoEducacaoGraduacao().getCodigo());
					sqlAlterar.setInt(x++, obj.getPerfilPadraoAlunoEducacaoGraduacaoTecnologica().getCodigo());
					sqlAlterar.setInt(x++, obj.getPerfilPadraoAlunoEducacaoSequencial().getCodigo());
					sqlAlterar.setInt(x++, obj.getPerfilPadraoAlunoEducacaoExtensao().getCodigo());
					sqlAlterar.setInt(x++, obj.getPerfilPadraoAlunoEducacaoPosGraduacao().getCodigo());
					sqlAlterar.setInt(x++, obj.getPerfilPadraoAlunoEducacaoTecnicoProfissionalizante().getCodigo());
					sqlAlterar.setInt(x++, obj.getPerfilPadraoPaisEducacaoInfantil().getCodigo());
					sqlAlterar.setInt(x++, obj.getPerfilPadraoPaisEducacaoFundamental().getCodigo());
					sqlAlterar.setInt(x++, obj.getPerfilPadraoPaisEducacaoMedio().getCodigo());
					sqlAlterar.setInt(x++, obj.getPerfilPadraoPaisEducacaoGraduacao().getCodigo());
					sqlAlterar.setInt(x++, obj.getPerfilPadraoPaisEducacaoGraduacaoTecnologica().getCodigo());
					sqlAlterar.setInt(x++, obj.getPerfilPadraoPaisEducacaoSequencial().getCodigo());
					sqlAlterar.setInt(x++, obj.getPerfilPadraoPaisEducacaoExtensao().getCodigo());
					sqlAlterar.setInt(x++, obj.getPerfilPadraoPaisEducacaoPosGraduacao().getCodigo());
					sqlAlterar.setInt(x++, obj.getPerfilPadraoPaisEducacaoTecnicoProfissionalizante().getCodigo());
					sqlAlterar.setBoolean(x++, obj.getControlarNumeroAulaProgramadoProfessorPorDia());
					sqlAlterar.setInt(x++, obj.getQuantidadeAulaMaximaProgramarProfessorDia());
					if(!obj.getUsuarioResponsavelOperacoesExternas().getCodigo().equals(0)) {
						sqlAlterar.setInt(x++, obj.getUsuarioResponsavelOperacoesExternas().getCodigo());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setBoolean(x++, obj.getIncrementarNumeroExemplarPorBiblioteca().booleanValue());					
					sqlAlterar.setBoolean(x++, obj.getZerarNumeroRegistroPorCurso().booleanValue());
                    sqlAlterar.setBoolean(x++, obj.getCriarProspectFiliacao());
					sqlAlterar.setBoolean(x++, obj.getCriarProspectAluno());
					sqlAlterar.setBoolean(x++, obj.getCriarProspectCandidato());
					sqlAlterar.setBoolean(x++, obj.getCriarProspectFuncionario());
					sqlAlterar.setBoolean(x++, obj.getOcultarMediaProcSeletivo());
					sqlAlterar.setBoolean(x++, obj.getOcultarClassificacaoProcSeletivo());
					sqlAlterar.setBoolean(x++, obj.getControlarCargaHorariaMaximaEstagioObrigatorio());
					sqlAlterar.setInt(x++, obj.getCargaHorariaMaximaSemanal());
					sqlAlterar.setInt(x++, obj.getCargaHorariaMaximaDiaria());
					sqlAlterar.setString(x++, obj.getSeguradoraPadraoEstagioObrigatorio());
					sqlAlterar.setString(x++, obj.getApolicePadraoEstagioObrigatorio());
					sqlAlterar.setBoolean(x++, obj.getForcarSeguradoraEApoliceParaEstagioObrigatorio());
					sqlAlterar.setBoolean(x++, obj.getRealizarCalculoMediaFinalFechPeriodo());		
					sqlAlterar.setBoolean(x++, obj.getOcultarChamadaCandidatoProcSeletivo());	
					sqlAlterar.setInt(x++, obj.getNrDiasNotifVencCand());
					if(!obj.getCertificadoParaDocumento().getCodigo().equals(0)) {
						sqlAlterar.setInt(x++, obj.getCertificadoParaDocumento().getCodigo());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setString(x++, obj.getSenhaCertificadoParaDocumento());
					sqlAlterar.setInt(x++, obj.getQuantidadeCasaDecimalConsiderarNotaProcessoSeletivo());
					sqlAlterar.setBoolean(x++, obj.getAssociarProspectSemConsultorResponsavelComPrimeiroConsultorInteragir());
					sqlAlterar.setBoolean(x++, obj.getAssociarNovoProspectComConsultorResponsavelCadastro());
					sqlAlterar.setBoolean(x++, obj.getPrimeiroAcessoAlunoResetarSenha());
					sqlAlterar.setBoolean(x++, obj.getPrimeiroAcessoProfessorResetarSenha());		
					sqlAlterar.setString(x++, obj.getUrlAcessoExternoAplicacao());
					sqlAlterar.setBoolean(x++, obj.getBloquearLancamentosNotasAulasFeriadosFinaisSemana());
					sqlAlterar.setBoolean(x++, obj.getPermitirProfessorRealizarLancamentoAlunosPreMatriculados());
					
					sqlAlterar.setInt(x++, obj.getQuantidadeCaracteresMinimoSenhaUsuario());
					sqlAlterar.setBoolean(x++, obj.getNivelSegurancaNumero());
					sqlAlterar.setBoolean(x++, obj.getNivelSegurancaLetra());
					sqlAlterar.setBoolean(x++, obj.getNivelSegurancaLetraMaiuscula());
					sqlAlterar.setBoolean(x++, obj.getNivelSegurancaCaracterEspecial());
					sqlAlterar.setInt(x++, obj.getNivelcontrolealteracaosenha());
					
					sqlAlterar.setString(x++, obj.getIdClienteRdStation());
					sqlAlterar.setString(x++, obj.getSenhaClienteRdStation());
					
					sqlAlterar.setString(x++, obj.getTokenRdStation());
					sqlAlterar.setString(x++, obj.getTokenPrivadoRdStation());
					sqlAlterar.setBoolean(x++, obj.getAtivarIntegracaoRdStation());
					sqlAlterar.setString(x++, obj.getIdentificadorRdStation());
					sqlAlterar.setString(x++, obj.getTokenWebService());
					sqlAlterar.setString(x++, obj.getLoginIntegracaoSofFin());
					sqlAlterar.setString(x++, obj.getSenhaIntegracaoSofFin());
					sqlAlterar.setString(x++, obj.getTokenIntegracaoSofFin());
					sqlAlterar.setString(x++, obj.getAmbienteEnumIntegracaoSoftFin().name());
					//Servidor arquivo online
					sqlAlterar.setBoolean(x++, obj.getIntegracaoServidorOnline());
					sqlAlterar.setString(x++, obj.getServidorArquivoOnline());
					sqlAlterar.setString(x++, obj.getUsuarioAutenticacao());
					sqlAlterar.setString(x++, obj.getSenhaAutenticacao());
					sqlAlterar.setString(x++, obj.getLocalUploadArquivoGED());
					
					sqlAlterar.setBoolean(x++, obj.getPermitirAcessoAlunoPreMatricula());
					sqlAlterar.setBoolean(x++, obj.getPermitirAcessoAlunoFormado());
					sqlAlterar.setBoolean(x++, obj.getPermitirAcessoAlunoEvasao());
					
					sqlAlterar.setInt(x++, obj.getPerfilPadraoAlunoPreMatricula().getCodigo());
					sqlAlterar.setInt(x++, obj.getPerfilPadraoAlunoEvasao().getCodigo());
					sqlAlterar.setInt(x++, obj.getPerfilPadraoAlunoFormado().getCodigo());
					sqlAlterar.setString(x++, obj.getLinkAcessoVisoesMoodle());
					
					if (Uteis.isAtributoPreenchido(obj.getPerfilPadraoProfessorEducacaoInfantil().getCodigo().intValue())) {
						sqlAlterar.setInt(x++, obj.getPerfilPadraoProfessorEducacaoInfantil().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);						
					}
					
					if (Uteis.isAtributoPreenchido(obj.getPerfilPadraoProfessorEnsinoFundamental().getCodigo().intValue())) {
						sqlAlterar.setInt(x++, obj.getPerfilPadraoProfessorEnsinoFundamental().getCodigo().intValue());			
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					
					if (Uteis.isAtributoPreenchido(obj.getPerfilPadraoProfessorEnsinoMedio().getCodigo().intValue())) {
						sqlAlterar.setInt(x++, obj.getPerfilPadraoProfessorEnsinoMedio().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					
					if (Uteis.isAtributoPreenchido(obj.getPerfilPadraoProfessorTecnicoProfissionalizante().getCodigo().intValue())) {
						sqlAlterar.setInt(x++, obj.getPerfilPadraoProfessorTecnicoProfissionalizante().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					
					if (Uteis.isAtributoPreenchido(obj.getPerfilPadraoProfessorSequencial().getCodigo().intValue())) {
						sqlAlterar.setInt(x++, obj.getPerfilPadraoProfessorSequencial().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					
					if (Uteis.isAtributoPreenchido(obj.getPerfilPadraoProfessorMestrado().getCodigo().intValue())) {
						sqlAlterar.setInt(x++, obj.getPerfilPadraoProfessorMestrado().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}

					if (Uteis.isAtributoPreenchido(obj.getPerfilPadraoProfessorGraduacaoTecnologica().getCodigo().intValue())) {
						sqlAlterar.setInt(x++, obj.getPerfilPadraoProfessorGraduacaoTecnologica().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}

					//nome do repositorio na AmazonS3
					if (Uteis.isAtributoPreenchido(obj.getIdAutenticacaoServOtimize())) {
						sqlAlterar.setString(x++, obj.getNomeRepositorio());
			        } else {
			        	sqlAlterar.setString(x++, null);
			        }
					
					sqlAlterar.setString(x++, obj.getSiglaAbonoFalta());
					sqlAlterar.setString(x++, obj.getDescricaoAbonoFalta());
					sqlAlterar.setBoolean(x++,  obj.getPermiteReativacaoMatriculaSemRequerimento().booleanValue());
					sqlAlterar.setInt(x++, obj.getQtdTentativasFalhaLogin());
					sqlAlterar.setInt(x++, obj.getTempoBloqTentativasFalhaLogin());
					sqlAlterar.setBoolean(x++, obj.getDefinirPerfilAcessoAlunoNaoAssinouContratoMatricula());
					if (Uteis.isAtributoPreenchido(obj.getPerfilAcessoAlunoNaoAssinouContratoMatricula().getCodigo().intValue())) {
						sqlAlterar.setInt(x++, obj.getPerfilAcessoAlunoNaoAssinouContratoMatricula().getCodigo().intValue());
					}
					else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setBoolean(x++,obj.getApresentarMensagemAlertaAlunoNaoAssinouContrato());
					sqlAlterar.setString(x++, obj.getMensagemAlertaAlunoNaoAssinouContratoMatricula());
					sqlAlterar.setInt(x++, obj.getPercentualBaixaFrequencia());
					sqlAlterar.setInt(x++, obj.getQtdeCaractereLimiteDownloadMaterial().intValue());
					sqlAlterar.setBoolean(x++, obj.getApresentarBotoesAcoesRequerimentoApenasAbaInicial());
					sqlAlterar.setString(x++, obj.getUrlHomeCandidato());
					if (Uteis.isAtributoPreenchido(obj.getConfiguracaoAparenciaSistema().getCodigo().intValue())) {
						sqlAlterar.setInt(x++, obj.getConfiguracaoAparenciaSistema().getCodigo().intValue());
					}
					else {
						sqlAlterar.setNull(x++, 0);
					}					
					sqlAlterar.setString(x++, obj.getTextoParaOrientacaoTcc());
					sqlAlterar.setString(x++, obj.getTextoParaOrientacaoProjetoIntegrador());
					if (Uteis.isAtributoPreenchido(obj.getMotivoPadraoCancelamentoPreMatriculaCalouro().getCodigo().intValue())) {
						sqlAlterar.setInt(x++, obj.getMotivoPadraoCancelamentoPreMatriculaCalouro().getCodigo().intValue());
					}
					else {
						sqlAlterar.setNull(x++, 0);
					}	
					sqlAlterar.setString(x++, obj.getHeaderBarIntegracaoSistemasProvaMestreGR());
					sqlAlterar.setString(x++, obj.getActionIntegracaoSistemasProvaMestreGR());
					sqlAlterar.setString(x++, obj.getTokenIntegracaoSistemasProvaMestreGR());					
					sqlAlterar.setBoolean(x++, obj.getHabilitarIntegracaoSistemaProvas());
					
					sqlAlterar.setBoolean(x++, obj.getAtivarIntegracaoLGPD());
					sqlAlterar.setString(x++, obj.getNomeDestinatarioEmailIntegracaoLGPD());
					sqlAlterar.setString(x++, obj.getEmailDestinatarioIntegracaoLGPD());
					sqlAlterar.setString(x++, obj.getAssuntoMensagemLGPD());
					sqlAlterar.setString(x++, obj.getMensagemLGPD());
					
					if(Uteis.isAtributoPreenchido(obj.getTextoPadraoDadosSensiveisLGPD().getCodigo().intValue())) {
						sqlAlterar.setInt(x++, obj.getTextoPadraoDadosSensiveisLGPD().getCodigo().intValue());	
					} else {
						sqlAlterar.setNull(x++, 0);
					}
	
					if (Uteis.isAtributoPreenchido(obj.getQuestionarioPlanoEnsino().getCodigo().intValue())) {
						sqlAlterar.setInt(x++, obj.getQuestionarioPlanoEnsino().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);						
					}
					sqlAlterar.setString(x++, obj.getTextoOrientacaoCancelamentoPorOutraMatricula());
					if (Uteis.isAtributoPreenchido(obj.getMotivoPadraoCancelamentoOutraMatricula().getCodigo().intValue())) {
						sqlAlterar.setInt(x++, obj.getMotivoPadraoCancelamentoOutraMatricula().getCodigo().intValue());
					}
					else {
						sqlAlterar.setNull(x++, 0);
					}					
					sqlAlterar.setString(x++, obj.getJustificativaCancelamentoPorOutraMatricula());
					sqlAlterar.setBoolean(x++, obj.getHabilitarRecursoInativacaoCredenciasAlunosFormados());
					sqlAlterar.setBoolean(x++, obj.getApresentarDocumentoPortalTransparenciaComPendenciaAssinatura());
					sqlAlterar.setInt(x++, obj.getTamanhoMaximoCorpoMensagem());
					sqlAlterar.setInt(x++, obj.getLimiteDestinatariosPorEmail());
					sqlAlterar.setInt(x++, obj.getTamanhoLimiteAnexoEmail());
					sqlAlterar.setDate(x++, Uteis.getDataJDBC(obj.getDataBaseValidacaoDiplomaDigital()));
					sqlAlterar.setBoolean(x++, obj.getHabilitarIntegracaoSistemaSymplicty());
					sqlAlterar.setString(x++, obj.getHostIntegracaoSistemaSymplicty());
					sqlAlterar.setString(x++, obj.getUserIntegracaoSistemaSymplicty());
					sqlAlterar.setString(x++, obj.getPassIntegracaoSistemaSymplicty());
					sqlAlterar.setInt(x++, obj.getPortIntegracaoSistemaSymplicty());
					sqlAlterar.setString(x++, obj.getProtocolIntegracaoSistemaSymplicty());
					sqlAlterar.setString(x++, obj.getPastaDestinoRemotaSymplicty());
					sqlAlterar.setBoolean(x++, obj.getHabilitarOperacoesTempoRealIntegracaoMestreGR());
					sqlAlterar.setString(x++, obj.getIntegracaoMestreGRURLBaseAPI());
					sqlAlterar.setBoolean(x++, obj.getHabilitarRecursosAcademicosVisaoAluno());
					sqlAlterar.setBoolean(x++, obj.getHabilitarMonitoramentoSentry());
					sqlAlterar.setString(x++, obj.getTokenSentry());
					sqlAlterar.setBoolean(x++, obj.getHabilitarMonitoramentoBlackboardSentry());
					sqlAlterar.setString(x++, obj.getTokenBlackboardSentry());
					sqlAlterar.setBoolean(x++, obj.getHabilitarEnvioEmailAssincrono());
					sqlAlterar.setBoolean(x++, obj.getAtivarDebugEmail());
					sqlAlterar.setLong(x++, obj.getTimeOutFilaEmail());
					sqlAlterar.setInt(x++, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			persistirConfiguracaoAtualizacaoCadastral(obj, usuarioLogado);
			persistirConfiguracaoCandidatoProcessoSeletivo(obj, usuarioLogado);
			getFacadeFactory().getConfiguracaoLdapInterfaceFacade().alterarConfiguracaoLdaps(obj.getCodigo(), obj.getConfiguracaoLdapVOs());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ConfiguracaoGeralSistemaVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ConfiguracaoGeralSistemaVO</code> que
	 *            será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ConfiguracaoGeralSistemaVO obj, UsuarioVO usuarioLogado) throws Exception {
		try {
			if(Uteis.isAtributoPreenchido(obj.getCertificadoParaDocumento())){
				getFacadeFactory().getArquivoFacade().excluir(obj.getCertificadoParaDocumento(), false, "",usuarioLogado, obj); 
			}
			String sql = "DELETE FROM ConfiguracaoGeralSistema WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
			getFacadeFactory().getConfiguracaoLdapInterfaceFacade().excluirConfiguracaoLdap(obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	public ConfiguracaoGeralSistemaVO consultarConfiguracaoASerUsada(int nivelMontarDados, UsuarioVO usuario, Integer unidadeEnsino) throws Exception {
		return getAplicacaoControle().getConfiguracaoGeralSistemaVO(unidadeEnsino, usuario);
	}

	public String consultarMensagemErroSenha() throws Exception {
		if(getAplicacaoControle() != null) {
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO =  getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, null);
			if(Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO)) {
				return configuracaoGeralSistemaVO.getMensagemErroSenha();
			}
		}
		String sqlStr = "select mensagemerrosenha from configuracaogeralsistema inner join configuracoes on configuracoes.codigo = configuracaogeralsistema.configuracoes and configuracoes.padrao = true";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return "";
		}
		return tabelaResultado.getString("mensagemerrosenha");
	}

	public ConfiguracaoGeralSistemaVO consultarConfiguracaoASerUsadaUnidadEnsino(Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {		
		return consultarConfiguracaoASerUsada(nivelMontarDados, usuario, unidadeEnsino);
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>ConfiguracaoGeralSistema</code> através do valor do atributo
	 * <code>codigo</code> da classe <code>UnidadeEnsino</code> Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ConfiguracaoGeralSistemaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */	
	public ConfiguracaoGeralSistemaVO consultarPorCodigoUnidadeEnsino(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultarConfiguracaoASerUsada(nivelMontarDados, usuario, valorConsulta);
	}
	
	public Integer consultarCodigoConfiguracaoReferenteUnidadeEnsino(Integer unidadeEnsino) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		SqlRowSet tabelaResultado = null;
		if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append("SELECT configuracaogeralsistema.codigo FROM configuracaogeralsistema inner join configuracoes on configuracaogeralsistema.configuracoes = configuracoes.codigo inner join unidadeensino on unidadeensino.configuracoes = configuracoes.codigo WHERE unidadeEnsino.codigo = ").append(unidadeEnsino);
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (tabelaResultado.next()) {
				return tabelaResultado.getInt("codigo");
			}
		}
		sqlStr  = new StringBuilder();
		sqlStr.append("SELECT configuracaogeralsistema.codigo FROM configuracaogeralsistema inner join configuracoes on configuracaogeralsistema.configuracoes = configuracoes.codigo WHERE configuracoes.padrao");
		tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("codigo");
		}
		return 0;
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>ConfiguracaoGeralSistema</code> através do valor do atributo
	 * <code>nome</code> da classe <code>PerfilAcesso</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ConfiguracaoGeralSistemaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ConfiguracaoGeralSistemaVO> consultarPorNomePerfilAcesso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT ConfiguracaoGeralSistema.* FROM ConfiguracaoGeralSistema, PerfilAcesso WHERE ConfiguracaoGeralSistema.perfilPadraoAluno = PerfilAcesso.codigo and upper( PerfilAcesso.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY PerfilAcesso.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public Boolean realizarVerificacaoValidarCpf(boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		if(getAplicacaoControle() != null) {
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO =  getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, usuario);
			if(Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO)) {
				return configuracaoGeralSistemaVO.getValidarCadastroCpf();
			}
		}
		StringBuilder sqlStr = new StringBuilder("SELECT configuracaoGeralSistema.validarcadastrocpf FROM configuracaoGeralSistema ");
		sqlStr.append("INNER JOIN configuracoes ON (configuracaoGeralSistema.configuracoes = configuracoes.codigo) ");
		sqlStr.append("WHERE configuracoes.padrao = true");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		tabelaResultado.next();
		return tabelaResultado.getBoolean("validarcadastrocpf");
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>ConfiguracaoGeralSistema</code> através do valor do atributo
	 * <code>nome</code> da classe <code>Visao</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ConfiguracaoGeralSistemaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ConfiguracaoGeralSistemaVO> consultarPorNomeVisao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT ConfiguracaoGeralSistema.* FROM ConfiguracaoGeralSistema, Visao WHERE ConfiguracaoGeralSistema.visaoPadraoPais = Visao.codigo and upper( Visao.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Visao.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>ConfiguracaoGeralSistema</code> através do valor do atributo
	 * <code>String smptPadrao</code>. Retorna os objetos, com início do valor
	 * do atributo idêntico ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ConfiguracaoGeralSistemaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ConfiguracaoGeralSistemaVO> consultarPorSmptPadrao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ConfiguracaoGeralSistema WHERE upper( smptPadrao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY smptPadrao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>ConfiguracaoGeralSistema</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou
	 * superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ConfiguracaoGeralSistemaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ConfiguracaoGeralSistemaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ConfiguracaoGeralSistema WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public ConfiguracaoGeralSistemaVO consultarPorCodigoConfiguracoes(Integer codigoConfiguracoes, boolean controlarAcesso, UsuarioVO usuario, int nivelMontarDados) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM configuracaogeralsistema WHERE configuracoes = ? ";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoConfiguracoes });
		if (resultado.next()) {
			return montarDados(resultado, nivelMontarDados, usuario);
		}
		return new ConfiguracaoGeralSistemaVO();
	}

	public PessoaVO consultarResponsavelPadraoComunicadoInternoPorCodigoConfiguracoes(Integer codigoPessoa) throws Exception {
		String sqlStr = "SELECT email, nome, codigo FROM pessoa WHERE codigo = " + codigoPessoa;
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (resultado.next()) {
			PessoaVO obj = new PessoaVO();
			obj.setCodigo(resultado.getInt("codigo"));
			obj.setNome(resultado.getString("nome"));
			obj.setEmail(resultado.getString("email"));
			return obj;
		}
		return new PessoaVO();
	}

	public ConfiguracaoGeralSistemaVO consultarPorCodigoConfiguracaoGeralSistema(Integer codigoConfiguracoes, boolean controlarAcesso, UsuarioVO usuario, int nivelMontarDados) throws Exception {
		String sqlStr = "SELECT * FROM configuracaogeralsistema WHERE codigo = ? ";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoConfiguracoes });
		if (resultado.next()) {
			return montarDados(resultado, nivelMontarDados, usuario);
		}
		return new ConfiguracaoGeralSistemaVO();
	}

	public ConfiguracaoGeralSistemaVO consultarPorMensagemTelaLoginConfiguracaoPadraoSistema() throws Exception {
		if(getAplicacaoControle() != null) {
			return getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null);
		}
		String sqlStr = "SELECT configuracaogeralsistema.mensagemTelaLogin, configuracaogeralsistema.apresentarMensagemTelaLogin FROM configuracaogeralsistema " + " inner join configuracoes on configuracoes.codigo = configuracaogeralsistema.configuracoes " + " WHERE configuracoes.padrao = true and configuracaogeralsistema.apresentarmensagemTelaLogin = true order by configuracaogeralsistema.codigo" + " limit 1";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (resultado.next()) {
			return montarDadosMensagem(resultado);
		}
		return new ConfiguracaoGeralSistemaVO();
	}

	public ConfiguracaoGeralSistemaVO consultarConfiguracaoPadraoSistemaTextoBancoCurriculum() throws Exception {
		if(getAplicacaoControle() != null) {
			return getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null);
		}
		String sqlStr = "SELECT conf.mensagemTelaBancoCurriculos, conf.tituloTelaBancoCurriculum, conf.tituloTelaBuscaCandidato FROM configuracaogeralsistema conf " + " inner join configuracoes on configuracoes.codigo = conf.configuracoes " + " WHERE configuracoes.padrao = true order by conf.codigo" + " limit 1";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (resultado.next()) {
			return montarDadosTelaBancoCurriculos(resultado);
		}
		return new ConfiguracaoGeralSistemaVO();
	}

	public ConfiguracaoGeralSistemaVO consultarPorApresentarHomeCandidato() throws Exception {
		if(getAplicacaoControle() != null) {
			return getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null);
		}
		String sqlStr = "SELECT configuracaogeralsistema.apresentarHomeCandidato FROM configuracaogeralsistema " + " inner join configuracoes on configuracoes.codigo = configuracaogeralsistema.configuracoes " + " WHERE configuracoes.padrao = true order by configuracaogeralsistema.codigo" + " limit 1";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (resultado.next()) {
			return montarDadosHomeCandidato(resultado);
		}
		return new ConfiguracaoGeralSistemaVO();
	}

	public ConfiguracaoGeralSistemaVO consultarPorApresentarEsqueceuMinhaSenha() throws Exception {
		if(getAplicacaoControle() != null) {
			return getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null);
		}
		String sqlStr = "SELECT configuracaogeralsistema.apresentarEsqueceuMinhaSenha FROM configuracaogeralsistema " + " inner join configuracoes on configuracoes.codigo = configuracaogeralsistema.configuracoes " + " WHERE configuracoes.padrao = true order by configuracaogeralsistema.codigo" + " limit 1";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (resultado.next()) {
			return montarDadosEsqueceuMinhaSenha(resultado);
		}
		return new ConfiguracaoGeralSistemaVO();
	}

	public ConfiguracaoGeralSistemaVO consultarConfiguraoesEnvioEmail() throws DataAccessException, Exception {
		if(getAplicacaoControle() != null) {
			return getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null);
		}
        String sqlStr = "SELECT smptpadrao, emailRemetente, login, senha, portaSmtpPadrao, servidorEmailUtilizaSSL, servidorEmailUtilizaTSL, ipServidor, responsavelpadraocomunicadointerno, localUploadArquivoFixo, pessoa.nome as nomeResponsavelpadraocomunicadointerno, qtdEmailEnvio, usernameSMS, senhaSMS, fornecedorSMS, urlAcessoExternoAplicacao  FROM configuracaogeralsistema inner join configuracoes on configuracoes.codigo = configuracaogeralsistema.configuracoes and configuracoes.padrao = true left join Pessoa on Pessoa.codigo = configuracaogeralsistema.responsavelpadraocomunicadointerno  limit 1";
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (dadosSQL.next()) {
			ConfiguracaoGeralSistemaVO obj = new ConfiguracaoGeralSistemaVO();
			obj.setSmptPadrao(dadosSQL.getString("smptpadrao"));
			obj.setEmailRemetente(dadosSQL.getString("emailRemetente"));
			obj.setLogin(dadosSQL.getString("login"));
			obj.setSenha(dadosSQL.getString("senha"));
			obj.setPortaSmtpPadrao(dadosSQL.getString("portaSmtpPadrao"));
			obj.setServidorEmailUtilizaSSL(dadosSQL.getBoolean("servidorEmailUtilizaSSL"));
			obj.setServidorEmailUtilizaTSL(dadosSQL.getBoolean("servidorEmailUtilizaTSL"));
			obj.setIpServidor(dadosSQL.getString("ipServidor"));
			obj.setQtdEmailEnvio(dadosSQL.getInt("qtdEmailEnvio"));
			obj.setUsernameSMS(dadosSQL.getString("usernameSMS"));
			obj.setSenhaSMS(dadosSQL.getString("senhaSMS"));			
			if(Uteis.isAtributoPreenchido(dadosSQL.getString("fornecedorSMS"))){
				obj.setFornecedorSMSEnum(FornecedorSmsEnum.valueOf(dadosSQL.getString("fornecedorSMS")));	
			}
			obj.getResponsavelPadraoComunicadoInterno().setCodigo(dadosSQL.getInt("responsavelpadraocomunicadointerno"));
			obj.getResponsavelPadraoComunicadoInterno().setNome(dadosSQL.getString("nomeResponsavelpadraocomunicadointerno"));
			obj.setUrlAcessoExternoAplicacao(dadosSQL.getString("urlAcessoExternoAplicacao"));
			obj.setLocalUploadArquivoFixo(dadosSQL.getString("localUploadArquivoFixo"));
			return obj;
		}
		return new ConfiguracaoGeralSistemaVO();
	}

	@Override
	public ConfiguracaoGeralSistemaVO consultarConfiguraoesEnvioEmailMaisResponsavelPadrao() throws Exception {
		if(getAplicacaoControle() != null) {
			return getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null);
		}
		String sqlStr = " SELECT smptpadrao, emailRemetente, login, senha, portaSmtpPadrao, servidorEmailUtilizaSSL, servidorEmailUtilizaTSL, ipServidor, " + " responsavelPadraoComunicadoInterno, pessoa.nome, pessoa.email " + " FROM configuracaogeralsistema " + " inner join pessoa on pessoa.codigo = configuracaogeralsistema.ResponsavelPadraoComunicadoInterno" + " limit 1";
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (dadosSQL.next()) {
			ConfiguracaoGeralSistemaVO obj = new ConfiguracaoGeralSistemaVO();
			obj.setSmptPadrao(dadosSQL.getString("smptpadrao"));
			obj.setEmailRemetente(dadosSQL.getString("emailRemetente"));
			obj.setLogin(dadosSQL.getString("login"));
			obj.setSenha(dadosSQL.getString("senha"));
			obj.setPortaSmtpPadrao(dadosSQL.getString("portaSmtpPadrao"));
			obj.setServidorEmailUtilizaSSL(dadosSQL.getBoolean("servidorEmailUtilizaSSL"));
			obj.setServidorEmailUtilizaTSL(dadosSQL.getBoolean("servidorEmailUtilizaTSL"));
			obj.setIpServidor(dadosSQL.getString("ipServidor"));
			obj.getResponsavelPadraoComunicadoInterno().setCodigo(dadosSQL.getInt("ResponsavelPadraoComunicadoInterno"));
			obj.getResponsavelPadraoComunicadoInterno().setNome(dadosSQL.getString("nome"));
			obj.getResponsavelPadraoComunicadoInterno().setEmail(dadosSQL.getString("email"));
			return obj;
		}
		return new ConfiguracaoGeralSistemaVO();
	}
	
	@Override
	public boolean consultarConfiguracaoGeralSeGerarSenhaCpfAluno() throws Exception {		
		String sqlStr = "SELECT gerarSenhaCpfAluno FROM configuracaogeralsistema inner join configuracoes on configuracoes.codigo = configuracaogeralsistema.configuracoes and configuracoes.padrao = true limit 1";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return tabelaResultado.next();
	}

	public ConfiguracaoGeralSistemaVO consultarConfiguraoesLocalUploadArquivoFixo() throws Exception {
		if(getAplicacaoControle() != null) {
			return getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null);
		}
		String sqlStr = "SELECT configuracaogeralsistema.localUploadArquivoFixo, configuracaogeralsistema.urlExternoDownloadArquivo FROM configuracaogeralsistema inner join configuracoes on configuracoes.codigo = configuracaogeralsistema.configuracoes and configuracoes.padrao = true limit 1";
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (dadosSQL.next()) {
			ConfiguracaoGeralSistemaVO obj = new ConfiguracaoGeralSistemaVO();
			obj.setLocalUploadArquivoFixo(dadosSQL.getString("localUploadArquivoFixo"));
			obj.setUrlExternoDownloadArquivo(dadosSQL.getString("urlExternoDownloadArquivo"));
			return obj;
		}
		return new ConfiguracaoGeralSistemaVO();
	}
	
	public ConfiguracaoGeralSistemaVO consultarConfiguraoesWebserviceNFe() throws Exception {
		if(getAplicacaoControle() != null) {
			return getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null);
		}
		String sqlStr = "SELECT configuracaogeralsistema.urlWebserviceNFe, configuracaogeralsistema.urlWebserviceNFSe FROM configuracaogeralsistema inner join configuracoes on configuracoes.codigo = configuracaogeralsistema.configuracoes and configuracoes.padrao = true limit 1";
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (dadosSQL.next()) {
			ConfiguracaoGeralSistemaVO obj = new ConfiguracaoGeralSistemaVO();
			obj.setUrlWebserviceNFe(dadosSQL.getString("urlWebserviceNFe"));
			obj.setUrlWebserviceNFSe(dadosSQL.getString("urlWebserviceNFSe"));
			return obj;
		}
		return new ConfiguracaoGeralSistemaVO();
	}

	@Override
	public ConfiguracaoGeralSistemaVO consultarConfiguraoesLocalUploadArquivoGED() throws Exception {
		if(getAplicacaoControle() != null) {
			return getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null);
		}
		String sqlStr = "SELECT configuracaogeralsistema.codigo, configuracaogeralsistema.usuarioResponsavelOperacoesExternas, configuracaogeralsistema.localUploadArquivoGED, configuracaogeralsistema.localUploadArquivoFixo, configuracaogeralsistema.urlExternoDownloadArquivo FROM configuracaogeralsistema inner join configuracoes on configuracoes.codigo = configuracaogeralsistema.configuracoes and configuracoes.padrao = true limit 1";
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (dadosSQL.next()) {
			ConfiguracaoGeralSistemaVO obj = new ConfiguracaoGeralSistemaVO();
			obj.setLocalUploadArquivoGED(dadosSQL.getString("localUploadArquivoGED"));
			obj.setUrlExternoDownloadArquivo(dadosSQL.getString("urlExternoDownloadArquivo"));
			obj.setLocalUploadArquivoFixo(dadosSQL.getString("localUploadArquivoFixo"));
			obj.setLocalUploadArquivoFixo(dadosSQL.getString("localUploadArquivoFixo"));
			obj.getUsuarioResponsavelOperacoesExternas().setCodigo(dadosSQL.getInt("usuarioResponsavelOperacoesExternas"));
			obj.setCodigo(dadosSQL.getInt("codigo"));
			return obj;
		}
		return new ConfiguracaoGeralSistemaVO();
	}
	
	@Override
	public ConfiguracaoGeralSistemaVO consultarConfiguraoesDiretorioUpload() throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT configuracaogeralsistema.localUploadArquivoFixo, configuracaogeralsistema.urlExternoDownloadArquivo, ");
		sqlStr.append(" configuracaogeralsistema.localUploadArquivoTemp, configuracaogeralsistema.usuarioAutenticacao, ");
		sqlStr.append(" configuracaogeralsistema.senhaAutenticacao, configuracaogeralsistema.nomeRepositorio, ");
		sqlStr.append(" configuracaogeralsistema.localUploadArquivoGED, configuracaogeralsistema.usuarioResponsavelOperacoesExternas, configuracaogeralsistema.urlacessoexternoaplicacao ");
		sqlStr.append(" FROM configuracaogeralsistema ");
		sqlStr.append(" inner join configuracoes on configuracoes.codigo = configuracaogeralsistema.configuracoes and configuracoes.padrao = true limit 1 ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (dadosSQL.next()) {
			ConfiguracaoGeralSistemaVO obj = new ConfiguracaoGeralSistemaVO();
			obj.setLocalUploadArquivoTemp(dadosSQL.getString("localUploadArquivoTemp"));
			obj.setLocalUploadArquivoFixo(dadosSQL.getString("localUploadArquivoFixo"));
			obj.setLocalUploadArquivoGED(dadosSQL.getString("localUploadArquivoGED"));
			obj.getUsuarioResponsavelOperacoesExternas().setCodigo(dadosSQL.getInt("usuarioResponsavelOperacoesExternas"));
			obj.setUrlExternoDownloadArquivo(dadosSQL.getString("urlExternoDownloadArquivo"));
			obj.setUsuarioAutenticacao(dadosSQL.getString("usuarioAutenticacao"));
			obj.setSenhaAutenticacao(dadosSQL.getString("senhaAutenticacao"));
			obj.setNomeRepositorio(dadosSQL.getString("nomeRepositorio"));
			obj.setUrlAcessoExternoAplicacao(dadosSQL.getString("urlacessoexternoaplicacao"));
			return obj;
		}
		return new ConfiguracaoGeralSistemaVO();
	}
	
	@Override
	public ConfiguracaoGeralSistemaVO consultarConfiguraoesMinhaBiblioteca() throws Exception {
		if(getAplicacaoControle() != null) {
			return getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null);
		}
		String sqlStr = "SELECT cfg.urlacessoexternoaplicacao FROM configuracaogeralsistema cfg inner join configuracoes c on c.codigo = cfg.configuracoes and c.padrao = true limit 1";
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (dadosSQL.next()) {
			ConfiguracaoGeralSistemaVO obj = new ConfiguracaoGeralSistemaVO();
			obj.setUrlAcessoExternoAplicacao(dadosSQL.getString("urlacessoexternoaplicacao"));
			return obj;
		}
		return new ConfiguracaoGeralSistemaVO();
	}
	
	@Override
	public Integer consultarConfiguraoesQtdeCaractereLimiteDownloadMaterial() throws Exception {
		String sqlStr = "SELECT cfg.qtdeCaractereLimiteDownloadMaterial FROM configuracaogeralsistema cfg inner join configuracoes c on c.codigo = cfg.configuracoes and c.padrao = true limit 1";
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (dadosSQL.next()) {
			return  dadosSQL.getInt("qtdeCaractereLimiteDownloadMaterial");
		}
		return 0;
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ConfiguracaoGeralSistemaVO</code> resultantes da consulta.
	 */
	public List<ConfiguracaoGeralSistemaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ConfiguracaoGeralSistemaVO> vetResultado = new ArrayList<ConfiguracaoGeralSistemaVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>ConfiguracaoGeralSistemaVO</code>.
	 * 
	 * @return O objeto da classe <code>ConfiguracaoGeralSistemaVO</code> com os
	 *         dados devidamente montados.
	 */
	public static ConfiguracaoGeralSistemaVO montarDadosMensagem(SqlRowSet dadosSQL) throws Exception {
		ConfiguracaoGeralSistemaVO obj = new ConfiguracaoGeralSistemaVO();
		obj.setMensagemTelaLogin(dadosSQL.getString("mensagemTelaLogin"));
		obj.setApresentarMensagemTelaLogin(dadosSQL.getBoolean("apresentarMensagemTelaLogin"));
		return obj;
	}

	public static ConfiguracaoGeralSistemaVO montarDadosTelaBancoCurriculos(SqlRowSet dadosSQL) throws Exception {
		ConfiguracaoGeralSistemaVO obj = new ConfiguracaoGeralSistemaVO();
		obj.setMensagemTelaBancoCurriculum(dadosSQL.getString("mensagemTelaBancoCurriculos"));
		obj.setTituloTelaBancoCurriculum(dadosSQL.getString("tituloTelaBancoCurriculum"));
		obj.setTituloTelaBuscaCandidato(dadosSQL.getString("tituloTelaBuscaCandidato"));
		return obj;
	}

	public static ConfiguracaoGeralSistemaVO montarDadosHomeCandidato(SqlRowSet dadosSQL) throws Exception {
		ConfiguracaoGeralSistemaVO obj = new ConfiguracaoGeralSistemaVO();
		obj.setApresentarHomeCandidato(dadosSQL.getBoolean("apresentarHomeCandidato"));
		return obj;
	}

	public static ConfiguracaoGeralSistemaVO montarDadosEsqueceuMinhaSenha(SqlRowSet dadosSQL) throws Exception {
		ConfiguracaoGeralSistemaVO obj = new ConfiguracaoGeralSistemaVO();
		obj.setApresentarEsqueceuMinhaSenha(dadosSQL.getBoolean("apresentarEsqueceuMinhaSenha"));
		return obj;
	}

	public ConfiguracaoGeralSistemaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ConfiguracaoGeralSistemaVO obj = new ConfiguracaoGeralSistemaVO();
		obj.setUpdated(new Date());
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getConfiguracoesVO().setCodigo(dadosSQL.getInt("configuracoes"));
		obj.setPermitirAcessoAlunoPreMatricula(dadosSQL.getBoolean("permitirAcessoAlunoPreMatricula"));
		obj.setPermitirAcessoAlunoFormado(dadosSQL.getBoolean("permitirAcessoAlunoFormado"));
		obj.setPermitirAcessoAlunoEvasao(dadosSQL.getBoolean("permitirAcessoAlunoEvasao"));
		obj.getPerfilPadraoAlunoPreMatricula().setCodigo(dadosSQL.getInt("perfilPadraoAlunoPreMatricula"));
		obj.getPerfilPadraoAlunoEvasao().setCodigo(dadosSQL.getInt("perfilPadraoAlunoEvasao"));
		obj.getPerfilPadraoAlunoFormado().setCodigo(dadosSQL.getInt("perfilPadraoAlunoFormado"));
		obj.setLinkAcessoVisoesMoodle(dadosSQL.getString("linkAcessoVisoesMoodle"));
		obj.setQtdTentativasFalhaLogin(dadosSQL.getInt("qtdTentativasFalhaLogin"));
		obj.setTempoBloqTentativasFalhaLogin(dadosSQL.getInt("tempoBloqTentativasFalhaLogin"));
		obj.setApresentarDocumentoPortalTransparenciaComPendenciaAssinatura(dadosSQL.getBoolean("apresentarDocumentoPortalTransparenciaComPendenciaAssinatura"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("versaoseisignature"))) {
			obj.setVersaoSeiSignature(dadosSQL.getString("versaoseisignature"));
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		obj.setQtdDiasAcessoAlunoFormado(dadosSQL.getInt("qtdDiasAcessoAlunoFormado"));
		obj.setQtdDiasAcessoAlunoExtensao(dadosSQL.getInt("qtdDiasAcessoAlunoExtensao"));
		obj.setQtdDiasAcessoAlunoAtivo(dadosSQL.getInt("qtdDiasAcessoAlunoAtivo"));
		obj.setVerificarAulaProgramadaIncluirDisciplina(dadosSQL.getBoolean("verificarAulaProgramadaIncluirDisciplina"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSLOGIN) {
			return obj;
		}
		obj.setValidarCadastroCpf(dadosSQL.getBoolean("validarCadastroCpf"));
		obj.setPermiteCancelamentoSemRequerimento(dadosSQL.getBoolean("permiteCancelamentoSemRequerimento"));
		obj.setPermiteTrancamentoSemRequerimento(dadosSQL.getBoolean("permiteTrancamentoSemRequerimento"));
		obj.setPermiteTransferenciaSemRequerimento(dadosSQL.getBoolean("permiteTransferenciaSemRequerimento"));
		obj.getResponsavelPadraoComunicadoInterno().setCodigo(dadosSQL.getInt("responsavelPadraoComunicadoInterno"));
		obj.getFuncionarioRespAlteracaoDados().setCodigo(dadosSQL.getInt("funcionarioRespAlteracaoDados"));
		obj.getGrupoDestinatarioMapaLocalAula().setCodigo(dadosSQL.getInt("grupoDestinatarioMapaLocalAula"));
		obj.getMotivoPadraoAbandonoCurso().setCodigo(dadosSQL.getInt("motivopadraoabandonocurso"));
		obj.setMaeFiliacao(dadosSQL.getBoolean("maeFiliacao"));
		obj.setNotificarAlunoAniversariante(dadosSQL.getBoolean("notificarAlunoAniversariante"));
		obj.setNotificarExAlunoAniversariante(dadosSQL.getBoolean("notificarExAlunoAniversariante"));
		obj.setNotificarProfessorAniversariante(dadosSQL.getBoolean("notificarProfessorAniversariante"));
		obj.setNotificarFuncionarioAniversariante(dadosSQL.getBoolean("notificarFuncionarioAniversariante"));
		obj.setNotificarPaiAniversariante(dadosSQL.getBoolean("notificarPaiAniversariante"));
		obj.setObrigarTipoMidiaProspect(dadosSQL.getBoolean("obrigarTipoMidiaProspect"));
		obj.setControlaAprovacaoDocEntregue(dadosSQL.getBoolean("controlaAprovacaoDocEntregue"));
		obj.getPaisPadrao().setCodigo(dadosSQL.getInt("paisPadrao"));
		obj.setPermiteTransferenciaInternaSemRequerimento(dadosSQL.getBoolean("permiteTransferenciaInternaSemRequerimento"));
		obj.setPermiteTransferenciaSaidaSemRequerimento(dadosSQL.getBoolean("permiteTransferenciaSaidaSemRequerimento"));
		obj.setPermiteAproveitamentoDiscSemRequerimento(dadosSQL.getBoolean("permiteAproveitamentoDisciplinaSemRequerimento"));
		obj.setPermiteProgramacaoFormaturaSemRequerimento(dadosSQL.getBoolean("permiteProgramacaoFormaturaSemRequerimento"));
		obj.setPermitePortadorDiplomaSemRequerimento(dadosSQL.getBoolean("permitePortadorDiplomaSemRequerimento"));
		obj.setUtilizarCaixaAltaNomePessoa(dadosSQL.getBoolean("utilizarCaixaAltaNomePessoa"));
		obj.setNaoApresentarProfessorVisaoAluno(dadosSQL.getBoolean("naoApresentarProfessorVisaoAluno"));
		obj.setControlaQtdDisciplinaExtensao(dadosSQL.getBoolean("controlaQtdDisciplinaExtensao"));
		obj.setControlaQtdDisciplinaRealizadaAteMatricula(dadosSQL.getBoolean("controlaQtdDisciplinaRealizadaAteMatricula"));
		obj.setPrimeiroAcessoAlunoCairMeusDados(dadosSQL.getBoolean("primeiroAcessoAlunoCairMeusDados"));
		obj.setDepartamentoRespServidorNotificacao(dadosSQL.getInt("departamentoRespServidorNotificacao"));
		obj.getTipoRequerimentoVO().setCodigo(dadosSQL.getInt("tipoRequerimento"));
		obj.setIdAutenticacaoServOtimize(dadosSQL.getString("idAutenticacaoServOtimize"));
		obj.setQtdDisciplinaExtensao(dadosSQL.getInt("qtdDisciplinaExtensao"));
		obj.setQtdDisciplinaRealizadaAteMatricula(dadosSQL.getInt("qtdDisciplinaRealizadaAteMatricula"));
		obj.setNrDiasLimiteEntregaDocumento(dadosSQL.getInt("nrDiasLimiteEntregaDocumento"));
		obj.setPermiteAlunoVerContasConvenio(dadosSQL.getBoolean("permiteAlunoVerContasConvenio"));
		obj.setPrefixoMatriculaFuncionario(dadosSQL.getString("prefixoMatriculaFuncionario"));
		obj.setPrefixoMatriculaProfessor(dadosSQL.getString("prefixoMatriculaProfessor"));
		obj.setUrlConfirmacaoPreInscricao(dadosSQL.getString("urlConfirmacaoPreInscricao"));
		obj.setNaoPermitirExpedicaoDiplomaDocumentacaoPendente(dadosSQL.getBoolean("naoPermitirExpedicaoDiplomaDocumentacaoPendente"));
		obj.setNaoPermitirAlterarUsernameUsuario(dadosSQL.getBoolean("naoPermitirAlterarUsernameUsuario"));
		obj.setLinkFacebook(dadosSQL.getString("linkFacebook"));
		obj.setLinkLinkdIn(dadosSQL.getString("linkLinkdIn"));
		obj.setLinkTwitter(dadosSQL.getString("linkTwitter"));
		obj.setCodigoTwitts(dadosSQL.getString("codigoTwitts"));
		obj.setSmptPadrao(dadosSQL.getString("smptPadrao"));
		obj.setPortaSmtpPadrao(dadosSQL.getString("portaSmtpPadrao"));
		obj.setIpServidor(dadosSQL.getString("ipServidor"));
		obj.setQtdEmailEnvio(dadosSQL.getInt("qtdEmailEnvio"));
		obj.setDiasParaRemoverPreMatricula(dadosSQL.getInt("diasParaRemoverPreMatricula"));
		obj.setEmailRemetente(dadosSQL.getString("emailRemetente"));
		obj.setLogin(dadosSQL.getString("login"));
		obj.setSenha(dadosSQL.getString("senha"));
		obj.setMensagemPadrao(dadosSQL.getString("mensagemPadrao"));
//		obj.getVisaoPadraoAluno().setCodigo(dadosSQL.getInt("visaoPadraoAluno"));
//		obj.getVisaoPadraoProfessor().setCodigo(dadosSQL.getInt("visaoPadraoProfessor"));
//		obj.getVisaoPadraoCandidato().setCodigo(dadosSQL.getInt("visaoPadraoCandidato"));
//		obj.getVisaoPadraoCoordenador().setCodigo(dadosSQL.getInt("visaoPadraoCoordenador"));
//		obj.getVisaoPadraoPais().setCodigo(dadosSQL.getInt("visaoPadraoPais"));
		obj.getPerfilPadraoAluno().setCodigo(dadosSQL.getInt("perfilPadraoAluno"));
		obj.getPerfilPadraoProfessorGraduacao().setCodigo(dadosSQL.getInt("perfilPadraoProfessorGraduacao"));
		obj.getPerfilPadraoProfessorExtensao().setCodigo(dadosSQL.getInt("perfilPadraoProfessorExtensao"));
		obj.getPerfilPadraoProfessorPosGraduacao().setCodigo(dadosSQL.getInt("perfilPadraoProfessorPosGraduacao"));
		obj.getPerfilPadraoCandidato().setCodigo(dadosSQL.getInt("perfilPadraoCandidato"));
		obj.getPerfilPadraoCoordenador().setCodigo(dadosSQL.getInt("perfilPadraoCoordenador"));
		obj.getPerfilPadraoParceiro().setCodigo(dadosSQL.getInt("perfilPadraoParceiro"));
		obj.getPerfilPadraoPais().setCodigo(dadosSQL.getInt("perfilPadraoPais"));
		obj.getPerfilPadraoOuvidoria().setCodigo(dadosSQL.getInt("perfilPadraoOuvidoria"));
		obj.setQtdeLimiteMsg(dadosSQL.getInt("qtdeLimiteMsg"));
		obj.setQtdeMsgLimiteServidorNotificacao(dadosSQL.getInt("qtdeMsgLimiteServidorNotificacao"));
		obj.setPermiteRenovarAlunoPreMatriculadoParaNovaMatriculaPreMatricula(dadosSQL.getBoolean("permiteRenovarAlunoPreMatriculadoParaNovaMatriculaPreMatricula"));
		obj.setAvaliacaoInstitucionalFinalModuloAluno(dadosSQL.getInt("avaliacaoInstitucionalFinalModuloAluno"));
		obj.setAvaliacaoInstitucionalFinalModuloProfessor(dadosSQL.getInt("avaliacaoInstitucionalFinalModuloProfessor"));
		obj.getQuestionarioPerfilSocioEconomico().setCodigo(dadosSQL.getInt("questionario"));
		obj.getPerfilEconomicoPadrao().setCodigo(dadosSQL.getInt("perfilEconomicoPadrao"));
		obj.setNrMaximoFolhaRecibo(dadosSQL.getInt("nrmaximofolharecibo"));
		obj.setTamanhoMaximoUpload(dadosSQL.getInt("tamanhoMaximoUpload"));
		obj.setPermiteInclusaoForaPrazoMatriculaPeriodoAtiva(dadosSQL.getBoolean("permiteInclusaoForaPrazoMatriculaPeriodoAtiva"));
		obj.setQtdeDiasNotificacaoDataProva(dadosSQL.getInt("qtdeDiasNotificacaoDataProva"));
		obj.setValidarQtdAlunoLocalSalaAulaInclusaoReposicaoDisciplina(dadosSQL.getBoolean("validarQtdAlunoLocalSalaAulaInclusaoReposicaoDisciplina"));
		obj.setDesconsiderarAlunoReposicaoVagasTurma(dadosSQL.getBoolean("desconsiderarAlunoReposicaoVagasTurma"));
        obj.setDesconsiderarAlunoReposicaoVagasTurma(dadosSQL.getBoolean("desconsiderarAlunoReposicaoVagasTurma"));

		obj.setUrlExternoDownloadArquivo(dadosSQL.getString("urlExternoDownloadArquivo"));
		obj.setUrlWebserviceNFe(dadosSQL.getString("urlWebserviceNFe"));
		obj.setUrlWebserviceNFSe(dadosSQL.getString("urlWebserviceNFSe"));
		obj.setMascaraSubRede(dadosSQL.getString("mascaraSubRede"));
		obj.setGerarSenhaCpfAluno(dadosSQL.getBoolean("gerarSenhaCpfAluno"));
		obj.setLocalUploadArquivoFixo(dadosSQL.getString("localUploadArquivoFixo"));
		obj.setLocalUploadArquivoTemp(dadosSQL.getString("localUploadArquivoTemp"));
		obj.setUrlAcessoExternoAplicacao(dadosSQL.getString("urlAcessoExternoAplicacao"));
		obj.setPermiteReativacaoMatriculaSemRequerimento(dadosSQL.getBoolean("permiteReativacaoMatriculaSemRequerimento"));
		obj.setApresentarMensagemAlertaAlunoNaoAssinouContrato(dadosSQL.getBoolean("apresentarMensagemAlertaAlunoNaoAssinouContrato"));
		obj.setMensagemAlertaAlunoNaoAssinouContratoMatricula(dadosSQL.getString("mensagemAlertaAlunoNaoAssinouContratoMatricula"));
		obj.setDefinirPerfilAcessoAlunoNaoAssinouContratoMatricula(dadosSQL.getBoolean("definirPerfilAcessoAlunoNaoAssinouContratoMatricula"));
		obj.getPerfilAcessoAlunoNaoAssinouContratoMatricula().setCodigo(dadosSQL.getInt("perfilAcessoAlunoNaoAssinouContratoMatricula"));
		
		//Servidor arquivo online
		obj.setIntegracaoServidorOnline(dadosSQL.getBoolean("integracaoServidorOnline"));
		obj.setServidorArquivoOnline(dadosSQL.getString("servidorArquivoOnline"));
		obj.setNomeRepositorio(dadosSQL.getString("nomeRepositorio"));
		obj.setUsuarioAutenticacao(dadosSQL.getString("usuarioAutenticacao"));
		obj.setSenhaAutenticacao(dadosSQL.getString("senhaAutenticacao"));
		obj.setLocalUploadArquivoGED(dadosSQL.getString("localUploadArquivoGED"));
		obj.setUrlHomeCandidato(dadosSQL.getString("urlHomeCandidato"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO) {
			return obj;
		}
		obj.getConfiguracaoAparenciaSistema().setCodigo(dadosSQL.getInt("configuracaoAparenciaSistema"));
		obj.setEnderecoServidorArquivo(dadosSQL.getString("enderecoServidorArquivo"));
		obj.setDescricaooVersoCarteirinhaEstudantil(dadosSQL.getString("descricaooVersoCarteirinhaEstudantil"));
		obj.setControlarValidadeCarteirinhaEstudantil(dadosSQL.getBoolean("controlarValidadeCarteirinhaEstudantil"));
		obj.setTodosCamposObrigatoriosPreInscricao(dadosSQL.getBoolean("todosCamposObrigatoriosPreInscricao"));
		obj.setOcultarCPFPreInscricao(dadosSQL.getBoolean("ocultarCPFPreInscricao"));
		obj.setOcultarEnderecoPreInscricao(dadosSQL.getBoolean("ocultarEnderecoPreInscricao"));
		obj.setOcultarEmailPreInscricao(dadosSQL.getBoolean("ocultarEmailPreInscricao"));
		obj.setOcultarRG(dadosSQL.getBoolean("ocultarRG"));
		obj.setOcultarDataNasc(dadosSQL.getBoolean("ocultarDataNasc"));
		obj.setOcultarSexo(dadosSQL.getBoolean("ocultarSexo"));
		obj.setOcultarTelefone(dadosSQL.getBoolean("ocultarTelefone"));
		obj.setOcultarEstadoCivil(dadosSQL.getBoolean("ocultarEstadoCivil"));
		obj.setOcultarNaturalidade(dadosSQL.getBoolean("ocultarNaturalidade"));
		obj.setOcultarFormacaoAcademica(dadosSQL.getBoolean("ocultarFormacaoAcademica"));
		obj.setMensagemTelaLogin(dadosSQL.getString("mensagemTelaLogin"));
		obj.setApresentarMensagemTelaLogin(dadosSQL.getBoolean("apresentarMensagemTelaLogin"));
		obj.setApresentarHomeCandidato(dadosSQL.getBoolean("apresentarHomeCandidato"));
		obj.setApresentarEsqueceuMinhaSenha(dadosSQL.getBoolean("apresentarEsqueceuMinhaSenha"));
		obj.setQtdeDiasAlertaRequerimento(dadosSQL.getInt("qtdeDiasAlertaRequerimento"));
		obj.setQtdeAntecedenciaDiasNotificarAlunoDownloadMaterial(dadosSQL.getInt("qtdeAntecedenciaDiasNotificarAlunoDownloadMaterial"));
		obj.setQtdeCaractereLimiteDownloadMaterial(dadosSQL.getInt("qtdeCaractereLimiteDownloadMaterial"));
		obj.setServidorEmailUtilizaSSL(dadosSQL.getBoolean("servidorEmailUtilizaSSL"));
		obj.setServidorEmailUtilizaTSL(dadosSQL.getBoolean("servidorEmailUtilizaTSL"));
		obj.setTextoComunicacaoInterna(dadosSQL.getString("textoComunicacaoInterna"));
		obj.setMensagemErroSenha(dadosSQL.getString("mensagemErroSenha"));
		obj.setMonitorarMensagensProfessor(dadosSQL.getBoolean("monitorarmensagensprofessor"));
		obj.setEmailConfirmacaoEnvioComunicado(dadosSQL.getString("emailConfirmacaoEnvioComunicado"));
		obj.setQtdDiasExpiracaoVagaBancoCurriculum(dadosSQL.getInt("qtdDiasExpiracaoVagaBancoCurriculum"));
		obj.setQtdeDiasNotificarProfessorPostarMaterial(dadosSQL.getInt("qtdeDiasNotificarProfessorPostarMaterial"));
		obj.setQtdDiasNotificacaoExpiracaoVagaBancoCurriculum(dadosSQL.getInt("qtdDiasNotificacaoExpiracaoVagaBancoCurriculum"));
		obj.setTituloTelaBancoCurriculum(dadosSQL.getString("tituloTelaBancoCurriculum"));
		obj.setTituloTelaBuscaCandidato(dadosSQL.getString("tituloTelaBuscaCandidato"));
        obj.setUsernameSMS(dadosSQL.getString("usernameSMS"));
        obj.setSenhaSMS(dadosSQL.getString("senhaSMS"));
        if(Uteis.isAtributoPreenchido(dadosSQL.getString("fornecedorSMS"))){
			obj.setFornecedorSMSEnum(FornecedorSmsEnum.valueOf(dadosSQL.getString("fornecedorSMS")));	
		}
		obj.setQtdAceitavelContatosPendentesNaoIniciados(dadosSQL.getInt("qtdAceitavelContatosPendentesNaoIniciados"));
		obj.setQtdLimiteContatosPendentesNaoIniciados(dadosSQL.getInt("qtdLimiteContatosPendentesNaoIniciados"));
		obj.setQtaAceitavelContatosPendentesNaoFinalizados(dadosSQL.getInt("qtaAceitavelContatosPendentesNaoFinalizados"));
		obj.setQtaLimiteContatosPendentesNaoFinalizados(dadosSQL.getInt("qtaLimiteContatosPendentesNaoFinalizados"));
		obj.setMensagemTelaBancoCurriculum(dadosSQL.getString("mensagemTelaBancoCurriculos"));
		obj.setPermiteRenovarComParcelaVencida(dadosSQL.getBoolean("permiteRenovarComParcelaVencida"));
		obj.setPrimeiroAcessoAlunoResetarSenha(dadosSQL.getBoolean("primeiroAcessoAlunoResetarSenha"));
		obj.setPrimeiroAcessoProfessorResetarSenha(dadosSQL.getBoolean("primeiroAcessoProfessorResetarSenha"));
		obj.setMensagemPadraoRenovacaoMatriculaComParcelaVencida(dadosSQL.getString("mensagemPadraoRenovacaoMatriculaComParcelaVencida"));
		obj.setMatricularAlunoReprovFaltaEmDisciplinaJaRealizadaPosGraduacaoExtensao(dadosSQL.getBoolean("matricularalunoreprovfaltaemdisciplinajarealizadaposgradext"));
		obj.setAssociarNovoProspectComConsultorResponsavelCadastro(dadosSQL.getBoolean("associarnovoprospectcomconsultorresponsavelcadastro"));
		obj.setAssociarProspectSemConsultorResponsavelComPrimeiroConsultorInteragir(dadosSQL.getBoolean("associarprospectsemconsultorresponsavelcomprimeiroconsultor"));
		obj.setTextoParaOrientacaoTcc(dadosSQL.getString("textoParaOrientacaoTcc"));
		obj.setTextoParaOrientacaoProjetoIntegrador(dadosSQL.getString("textoParaOrientacaoProjetoIntegrador"));
	
		obj.setQtdDiasMaximoAntecedenciaRemarcarAulaReposicao(dadosSQL.getInt("qtdDiasMaximoAntecedenciaRemarcarAulaReposicao"));
		obj.setCalcularMediaAoGravar(dadosSQL.getBoolean("calcularMediaAoGravar"));
		obj.setRealizarCalculoMediaFinalFechPeriodo(dadosSQL.getBoolean("realizarCalculoMediaFinalFechPeriodo"));				
		obj.setPermitiAlunoPreMatriculaSolicitarRequerimento(dadosSQL.getBoolean("permitiAlunoPreMatriculaSolicitarRequerimento"));
	
		obj.setQtdeDiaNotificarAbandonoCurso(dadosSQL.getInt("qtdeDiaNotificarAbandonoCurso"));
		obj.setQtdeDiaRegistrarAbandonoCurso(dadosSQL.getInt("qtdeDiaRegistrarAbandonoCurso"));
		obj.setDiasParaNotificarCoordenadorInicioTurma(dadosSQL.getInt("diasParaNotificarCoordenadorInicioTurma"));
		obj.setBloquearRealizarAbandonoCursoComEmprestimoBiblioteca(dadosSQL.getBoolean("bloquearRealizarAbandonoCursoComEmprestimoBiblioteca"));
		obj.setBloquearRealizarCancelamentoComEmprestimoBiblioteca(dadosSQL.getBoolean("bloquearRealizarCancelamentoComEmprestimoBiblioteca"));
		obj.setBloquearRealizarRenovacaoComEmprestimoBiblioteca(dadosSQL.getBoolean("bloquearRealizarRenovacaoComEmprestimoBiblioteca"));
		obj.setBloquearRealizarTrancamentoComEmprestimoBiblioteca(dadosSQL.getBoolean("bloquearRealizarTrancamentoComEmprestimoBiblioteca"));
		obj.setBloquearRealizarTransferenciaInternaComEmprestimoBiblioteca(dadosSQL.getBoolean("bloquearRealizarTransferenciaInternaComEmprestimoBiblioteca"));
		obj.setBloquearRealizarTransferenciaSaidaComEmprestimoBiblioteca(dadosSQL.getBoolean("bloquearRealizarTransferenciaSaidaComEmprestimoBiblioteca"));
		obj.setNovoObj(Boolean.FALSE);
		obj.setApresentarAlunoPendenteFinanceiroVisaoProfessor(dadosSQL.getBoolean("apresentarAlunoPendenteFinanceiroVisaoProfessor"));
		obj.setApresentarAlunoPendenteFinanceiroVisaoCoordenador(dadosSQL.getBoolean("apresentarAlunoPendenteFinanceiroVisaoCoordenador"));
		obj.setIpServidorBiometria(dadosSQL.getString("ipServidorBiometria"));
		obj.getPerfilPadraoAlunoEducacaoInfantil().setCodigo(dadosSQL.getInt("perfilPadraoAlunoEducacaoInfantil"));
		obj.getPerfilPadraoAlunoEducacaoFundamental().setCodigo(dadosSQL.getInt("perfilPadraoAlunoEducacaoFundamental"));
		obj.getPerfilPadraoAlunoEducacaoMedio().setCodigo(dadosSQL.getInt("perfilPadraoAlunoEducacaoMedio"));
		obj.getPerfilPadraoAlunoEducacaoGraduacao().setCodigo(dadosSQL.getInt("perfilPadraoAlunoEducacaoGraduacao"));
		obj.getPerfilPadraoAlunoEducacaoGraduacaoTecnologica().setCodigo(dadosSQL.getInt("perfilPadraoAlunoEducacaoGraduacaoTecnologica"));
		obj.getPerfilPadraoAlunoEducacaoSequencial().setCodigo(dadosSQL.getInt("perfilPadraoAlunoEducacaoSequencial"));
		obj.getPerfilPadraoAlunoEducacaoExtensao().setCodigo(dadosSQL.getInt("perfilPadraoAlunoEducacaoExtensao"));
		obj.getPerfilPadraoAlunoEducacaoPosGraduacao().setCodigo(dadosSQL.getInt("perfilPadraoAlunoEducacaoPosGraduacao"));
		obj.getPerfilPadraoAlunoEducacaoTecnicoProfissionalizante().setCodigo(dadosSQL.getInt("perfilPadraoAlunoEducacaoTecnicoProfissionalizante"));
		obj.getPerfilPadraoPaisEducacaoInfantil().setCodigo(dadosSQL.getInt("perfilPadraoPaisEducacaoInfantil"));
		obj.getPerfilPadraoPaisEducacaoFundamental().setCodigo(dadosSQL.getInt("perfilPadraoPaisEducacaoFundamental"));
		obj.getPerfilPadraoPaisEducacaoMedio().setCodigo(dadosSQL.getInt("perfilPadraoPaisEducacaoMedio"));
		obj.getPerfilPadraoPaisEducacaoGraduacao().setCodigo(dadosSQL.getInt("perfilPadraoPaisEducacaoGraduacao"));
		obj.getPerfilPadraoPaisEducacaoGraduacaoTecnologica().setCodigo(dadosSQL.getInt("perfilPadraoPaisEducacaoGraduacaoTecnologica"));
		obj.getPerfilPadraoPaisEducacaoSequencial().setCodigo(dadosSQL.getInt("perfilPadraoPaisEducacaoSequencial"));
		obj.getPerfilPadraoPaisEducacaoExtensao().setCodigo(dadosSQL.getInt("perfilPadraoPaisEducacaoExtensao"));
		obj.getPerfilPadraoPaisEducacaoPosGraduacao().setCodigo(dadosSQL.getInt("perfilPadraoPaisEducacaoPosGraduacao"));
		obj.getPerfilPadraoPaisEducacaoTecnicoProfissionalizante().setCodigo(dadosSQL.getInt("perfilPadraoPaisEducacaoTecnicoProfissionalizante"));
		obj.setControlarNumeroAulaProgramadoProfessorPorDia(dadosSQL.getBoolean("controlarNumeroAulaProgramadoProfessorPorDia"));
		obj.setQuantidadeAulaMaximaProgramarProfessorDia(dadosSQL.getInt("quantidadeAulaMaximaProgramarProfessorDia"));
		obj.setIncrementarNumeroExemplarPorBiblioteca(dadosSQL.getBoolean("incrementarNumeroExemplarPorBiblioteca"));
		obj.setZerarNumeroRegistroPorCurso(dadosSQL.getBoolean("zerarNumeroRegistroPorCurso"));
		obj.setOcultarMediaProcSeletivo(dadosSQL.getBoolean("ocultarMediaProcSeletivo"));
		obj.setOcultarClassificacaoProcSeletivo(dadosSQL.getBoolean("ocultarClassificacaoProcSeletivo"));
		obj.setOcultarChamadaCandidatoProcSeletivo(dadosSQL.getBoolean("ocultarChamadaCandidatoProcSeletivo"));				
		obj.getCertificadoParaDocumento().setCodigo(dadosSQL.getInt("certificadoparadocumento"));
		obj.setSenhaCertificadoParaDocumento(dadosSQL.getString("senhacertificadoparadocumento"));
		obj.setNrDiasNotifVencCand(dadosSQL.getInt("nrDiasNotifVencCand"));		
		obj.setCriarProspectFiliacao(dadosSQL.getBoolean("criarProspectFiliacao"));
		obj.setCriarProspectAluno(dadosSQL.getBoolean("criarProspectAluno"));
		obj.setCriarProspectCandidato(dadosSQL.getBoolean("criarProspectCandidato"));
		obj.setCriarProspectFuncionario(dadosSQL.getBoolean("criarProspectFuncionario"));
		//nivel segurança senha usuario
		obj.setQuantidadeCaracteresMinimoSenhaUsuario(dadosSQL.getInt("quantidadeCaracteresMinimoSenhaUsuario"));
		obj.setNivelSegurancaCaracterEspecial(dadosSQL.getBoolean("nivelSegurancaCaracterEspecial"));
		obj.setNivelSegurancaLetraMaiuscula(dadosSQL.getBoolean("nivelSegurancaLetraMaiuscula"));
		obj.setNivelSegurancaLetra(dadosSQL.getBoolean("nivelSegurancaLetra"));
		obj.setNivelSegurancaNumero(dadosSQL.getBoolean("nivelSegurancaNumero"));
		obj.setNivelcontrolealteracaosenha(dadosSQL.getInt("nivelcontrolealteracaosenha"));
		
		if(Uteis.isAtributoPreenchido(dadosSQL.getInt("usuarioResponsavelOperacoesExternas"))) {
			obj.setUsuarioResponsavelOperacoesExternas(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(dadosSQL.getInt("usuarioResponsavelOperacoesExternas"), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));			
		}
        obj.setControlarCargaHorariaMaximaEstagioObrigatorio(dadosSQL.getBoolean("controlarCargaHorariaMaximaEstagioObrigatorio"));
		obj.setCargaHorariaMaximaSemanal(dadosSQL.getInt("cargaHorariaMaximaSemanal"));
		obj.setCargaHorariaMaximaDiaria(dadosSQL.getInt("cargaHorariaMaximaDiaria"));
		obj.setSeguradoraPadraoEstagioObrigatorio(dadosSQL.getString("seguradoraPadraoEstagioObrigatorio"));
		obj.setApolicePadraoEstagioObrigatorio(dadosSQL.getString("apolicePadraoEstagioObrigatorio"));
		obj.setForcarSeguradoraEApoliceParaEstagioObrigatorio(dadosSQL.getBoolean("forcarSeguradoraEApoliceParaEstagioObrigatorio"));
		obj.setQuantidadeCasaDecimalConsiderarNotaProcessoSeletivo(dadosSQL.getInt("quantidadeCasaDecimalConsiderarNotaProcessoSeletivo"));
		obj.setConfiguracaoAtualizacaoCadastralVO(getFacadeFactory().getConfiguracaoAtualizacaoCadastralFacade().consultarPorConfiguracaoGeralSistema(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		obj.setConfiguracaoCandidatoProcessoSeletivoVO(getFacadeFactory().getConfiguracaoCandidatoProcessoSeletivoInterfaceFacade().consultarPorConfiguracaoGeralSistema(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		obj.setBloquearLancamentosNotasAulasFeriadosFinaisSemana(dadosSQL.getBoolean("bloquearLancamentosNotasAulasFeriadosFinaisSemana"));
		obj.setPermitirProfessorRealizarLancamentoAlunosPreMatriculados(dadosSQL.getBoolean("permitirProfessorRealizarLancamentoAlunosPreMatriculados"));
		
		obj.setIdClienteRdStation(dadosSQL.getString("idClienteRdStation"));
		obj.setSenhaClienteRdStation(dadosSQL.getString("senhaClienteRdStation"));
		obj.setTokenRdStation(dadosSQL.getString("tokenRdStation"));		
		obj.setTokenPrivadoRdStation(dadosSQL.getString("tokenPrivadoRdStation"));
		obj.setAtivarIntegracaoRdStation(dadosSQL.getBoolean("ativarIntegracaoRdStation"));
		obj.setIdentificadorRdStation(dadosSQL.getString("identificadorRdStation"));
		obj.setTokenWebService(dadosSQL.getString("tokenWebService"));
		obj.setLoginIntegracaoSofFin(dadosSQL.getString("loginIntegracaoSofFin"));
		obj.setSenhaIntegracaoSofFin(dadosSQL.getString("senhaIntegracaoSofFin"));
		obj.setTokenIntegracaoSofFin(dadosSQL.getString("tokenIntegracaoSofFin"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("ambienteEnumIntegracaoSoftFin"))) {
			obj.setAmbienteEnumIntegracaoSoftFin(AmbienteEnum.valueOf(dadosSQL.getString("ambienteEnumIntegracaoSoftFin")));	
		}
		
		obj.setHabilitarIntegracaoSistemaSymplicty(dadosSQL.getBoolean("habilitarintegracaosistemasymplicty"));
		obj.setHostIntegracaoSistemaSymplicty(dadosSQL.getString("hostintegracaosistemasymplicty"));
		obj.setUserIntegracaoSistemaSymplicty(dadosSQL.getString("userintegracaosistemasymplicty"));
		obj.setPassIntegracaoSistemaSymplicty(dadosSQL.getString("passintegracaosistemasymplicty"));
		obj.setPortIntegracaoSistemaSymplicty(dadosSQL.getInt("portintegracaosistemasymplicty"));
		obj.setProtocolIntegracaoSistemaSymplicty(dadosSQL.getString("protocolintegracaosistemasymplicty"));
		obj.setPastaDestinoRemotaSymplicty(dadosSQL.getString("pastadestinoremotasymplicty"));
		obj.getPerfilPadraoProfessorEducacaoInfantil().setCodigo(dadosSQL.getInt("perfilPadraoProfessorEducacaoInfantil"));
		obj.getPerfilPadraoProfessorEnsinoFundamental().setCodigo(dadosSQL.getInt("perfilPadraoProfessorEnsinoFundamental"));
		obj.getPerfilPadraoProfessorEnsinoMedio().setCodigo(dadosSQL.getInt("perfilPadraoProfessorEnsinoMedio"));
		obj.getPerfilPadraoProfessorTecnicoProfissionalizante().setCodigo(dadosSQL.getInt("perfilPadraoProfessorTecnicoProfissionalizante"));
		obj.getPerfilPadraoProfessorSequencial().setCodigo(dadosSQL.getInt("perfilPadraoProfessorSequencial"));
		obj.getPerfilPadraoProfessorMestrado().setCodigo(dadosSQL.getInt("perfilPadraoProfessorMestrado"));
		obj.getPerfilPadraoProfessorGraduacaoTecnologica().setCodigo(dadosSQL.getInt("perfilPadraoProfessorGraduacaoTecnologica"));
		
		obj.setSiglaAbonoFalta(dadosSQL.getString("siglaAbonoFalta"));
		obj.setDescricaoAbonoFalta(dadosSQL.getString("descricaoAbonoFalta"));
		
		obj.setPercentualBaixaFrequencia(dadosSQL.getInt("percentualBaixaFrequencia"));
		obj.setApresentarBotoesAcoesRequerimentoApenasAbaInicial(dadosSQL.getBoolean("apresentarBotoesAcoesRequerimentoApenasAbaInicial"));
		obj.getMotivoPadraoCancelamentoPreMatriculaCalouro().setCodigo(dadosSQL.getInt("motivoPadraoCancelamentoPreMatriculaCalouro"));
		
		obj.setHeaderBarIntegracaoSistemasProvaMestreGR(dadosSQL.getString("headerBarIntegracaoSistemasProvaMestreGR"));
		obj.setActionIntegracaoSistemasProvaMestreGR(dadosSQL.getString("actionIntegracaoSistemasProvaMestreGR"));
		obj.setTokenIntegracaoSistemasProvaMestreGR(dadosSQL.getString("tokenIntegracaoSistemasProvaMestreGR"));
		obj.setHabilitarIntegracaoSistemaProvas(dadosSQL.getBoolean("habilitarIntegracaoSistemaProvas"));
		
		obj.setAtivarIntegracaoLGPD(dadosSQL.getBoolean("ativarIntegracaoLGPD"));
		obj.setNomeDestinatarioEmailIntegracaoLGPD(dadosSQL.getString("nomeDestinatarioEmailIntegracaoLGPD"));
		obj.setEmailDestinatarioIntegracaoLGPD(dadosSQL.getString("emailDestinatarioIntegracaoLGPD"));
		obj.setAssuntoMensagemLGPD(dadosSQL.getString("assuntoMensagemLGPD"));
		obj.setMensagemLGPD(dadosSQL.getString("mensagemLGPD"));
		obj.getTextoPadraoDadosSensiveisLGPD().setCodigo(dadosSQL.getInt("textopadraodeclaracao"));
		obj.getQuestionarioPlanoEnsino().setCodigo(dadosSQL.getInt("questionarioplanoensino"));
		obj.setTextoOrientacaoCancelamentoPorOutraMatricula(dadosSQL.getString("textoOrientacaoCancelamentoPorOutraMatricula"));
		obj.getMotivoPadraoCancelamentoOutraMatricula().setCodigo(dadosSQL.getInt("motivopadraocancelamentooutramatricula"));
		obj.setJustificativaCancelamentoPorOutraMatricula(dadosSQL.getString("justificativaCancelamentoPorOutraMatricula"));
		obj.setHabilitarRecursoInativacaoCredenciasAlunosFormados(dadosSQL.getBoolean("habilitarRecursoInativacaoCredenciasAlunosFormados"));
		obj.setTamanhoMaximoCorpoMensagem(dadosSQL.getInt("tamanhoMaximoCorpoMensagem"));
		obj.setLimiteDestinatariosPorEmail(dadosSQL.getInt("limitedestinatariosporemail"));
		obj.setTamanhoLimiteAnexoEmail(dadosSQL.getInt("tamanholimiteanexoemail"));
		obj.setDataBaseValidacaoDiplomaDigital(dadosSQL.getDate("databasevalidacaodiplomadigital"));
		obj.setHabilitarOperacoesTempoRealIntegracaoMestreGR(dadosSQL.getBoolean("habilitaroperacoestemporealintegracaomestregr"));
		obj.setIntegracaoMestreGRURLBaseAPI(dadosSQL.getString("integracaomestregrurlbaseapi"));
		obj.setHabilitarRecursosAcademicosVisaoAluno(dadosSQL.getBoolean("habilitarrecursosacademicosvisaoaluno"));
		obj.setHabilitarMonitoramentoSentry(dadosSQL.getBoolean("habilitarmonitoramentosentry"));
		obj.setTokenSentry(dadosSQL.getString("tokensentry"));
		obj.setHabilitarMonitoramentoBlackboardSentry(dadosSQL.getBoolean("habilitarmonitoramentoblackboardsentry"));
		obj.setTokenBlackboardSentry(dadosSQL.getString("tokenblackboardsentry"));
		obj.setHabilitarEnvioEmailAssincrono(dadosSQL.getBoolean("habilitarEnvioEmailAssincrono"));
		obj.setAtivarDebugEmail(dadosSQL.getBoolean("ativarDebugEmail"));
		obj.setTimeOutFilaEmail(dadosSQL.getLong("timeOutFilaEmail"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj; 
		}
		montarDadosConfiguracoes(obj, nivelMontarDados, usuario);
//		montarDadosVisaoPadraoAluno(obj, nivelMontarDados, usuario);
//		montarDadosVisaoPadraoProfessor(obj, nivelMontarDados, usuario);
//		montarDadosVisaoPadraoCandidato(obj, nivelMontarDados, usuario);
//		montarDadosVisaoPadraoCoordenador(obj, nivelMontarDados, usuario);
//		montarDadosVisaoPadraoPais(obj, nivelMontarDados, usuario);
		montarDadosPerfilPadraoAluno(obj, nivelMontarDados, usuario);
		montarDadosPerfilPadraoProfessor(obj, nivelMontarDados, usuario);
		montarDadosPerfilPadraoProfessorExtensao(obj, nivelMontarDados, usuario);
		montarDadosPerfilPadraoCandidato(obj, nivelMontarDados, usuario);
		montarDadosPerfilPadraoCoordenador(obj, nivelMontarDados, usuario);
		montarDadosPerfilEconomicoPadrao(obj, nivelMontarDados, usuario);
		montarDadosPerfilPadraoParceiro(obj, nivelMontarDados, usuario);
		montarDadosPerfilPadraoPais(obj, nivelMontarDados, usuario);
		montarDadosPerfilPadraoOuvidoria(obj, nivelMontarDados, usuario);
		montarDadosResponsavelPadraoComunicadoInterno(obj, nivelMontarDados, usuario);
		montarDadosFuncionarioRespAlteracaoDados(obj, nivelMontarDados, usuario);
		montarDadosMotivoPadraoAbandonoCurso(obj, nivelMontarDados, usuario);
		montarDadosMotivoPadraoCancelamentoPreMatriculaCalouro(obj, nivelMontarDados, usuario);
		montarDadosCertificadoParaDocumento(obj, nivelMontarDados, usuario);
		montarDadosMotivoPadraoCancelamentoMatriculaPorOutraMatricula(obj, nivelMontarDados, usuario);
		obj.setConfiguracaoLdapVOs(getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarPorConfiguracaoGeralSistema(obj.getCodigo(), false, nivelMontarDados, usuario));
		
		return obj;
	}
	
	public static void montarDadosCertificadoParaDocumento(ConfiguracaoGeralSistemaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCertificadoParaDocumento().getCodigo().intValue() == 0) {
			obj.setCertificadoParaDocumento(new ArquivoVO());
			return;
		}
		obj.setCertificadoParaDocumento(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getCertificadoParaDocumento().getCodigo(), nivelMontarDados,usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PerfilAcessoVO</code> relacionado ao objeto
	 * <code>ConfiguracaoGeralSistemaVO</code>. Faz uso da chave primária da
	 * classe <code>PerfilAcessoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosPerfilPadraoCandidato(ConfiguracaoGeralSistemaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPerfilPadraoCandidato().getCodigo().intValue() == 0) {
			obj.setPerfilPadraoCandidato(new PerfilAcessoVO());
			return;
		}
		obj.setPerfilPadraoCandidato(getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(obj.getPerfilPadraoCandidato().getCodigo(), usuario));
	}

	public static void montarDadosPerfilPadraoPais(ConfiguracaoGeralSistemaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPerfilPadraoPais().getCodigo().intValue() == 0) {
			obj.setPerfilPadraoPais(new PerfilAcessoVO());
			return;
		}
		obj.setPerfilPadraoPais(getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(obj.getPerfilPadraoPais().getCodigo(), usuario));
	}

	public static void montarDadosPerfilPadraoOuvidoria(ConfiguracaoGeralSistemaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPerfilPadraoOuvidoria().getCodigo().intValue() == 0) {
			obj.setPerfilPadraoOuvidoria(new PerfilAcessoVO());
			return;
		}
		obj.setPerfilPadraoOuvidoria(getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(obj.getPerfilPadraoOuvidoria().getCodigo(), usuario));
	}

	public static void montarDadosConfiguracoes(ConfiguracaoGeralSistemaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getConfiguracoesVO().getCodigo().intValue() == 0) {
			obj.setConfiguracoesVO(new ConfiguracoesVO());
			return;
		}
		obj.setConfiguracoesVO(getFacadeFactory().getConfiguracoesFacade().consultarPorChavePrimaria(obj.getConfiguracoesVO().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosPerfilEconomicoPadrao(ConfiguracaoGeralSistemaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPerfilEconomicoPadrao().getCodigo().intValue() == 0) {
			obj.setPerfilEconomicoPadrao(new PerfilEconomicoVO());
			return;
		}
		obj.setPerfilEconomicoPadrao(getFacadeFactory().getPerfilEconomicoFacade().consultarPorChavePrimaria(obj.getPerfilEconomicoPadrao().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PerfilAcessoVO</code> relacionado ao objeto
	 * <code>ConfiguracaoGeralSistemaVO</code>. Faz uso da chave primária da
	 * classe <code>PerfilAcessoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosPerfilPadraoProfessor(ConfiguracaoGeralSistemaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPerfilPadraoProfessorGraduacao().getCodigo().intValue() == 0) {
			obj.setPerfilPadraoProfessorGraduacao(new PerfilAcessoVO());
			return;
		}
		obj.setPerfilPadraoProfessorGraduacao(getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(obj.getPerfilPadraoProfessorGraduacao().getCodigo(), usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PerfilAcessoVO</code> relacionado ao objeto
	 * <code>ConfiguracaoGeralSistemaVO</code>. Faz uso da chave primária da
	 * classe <code>PerfilAcessoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosPerfilPadraoProfessorExtensao(ConfiguracaoGeralSistemaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPerfilPadraoProfessorExtensao().getCodigo().intValue() == 0) {
			obj.setPerfilPadraoProfessorExtensao(new PerfilAcessoVO());
			return;
		}
		obj.setPerfilPadraoProfessorExtensao(getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(obj.getPerfilPadraoProfessorExtensao().getCodigo(), usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PerfilAcessoVO</code> relacionado ao objeto
	 * <code>ConfiguracaoGeralSistemaVO</code>. Faz uso da chave primária da
	 * classe <code>PerfilAcessoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosPerfilPadraoCoordenador(ConfiguracaoGeralSistemaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPerfilPadraoCoordenador().getCodigo().intValue() == 0) {
			obj.setPerfilPadraoCoordenador(new PerfilAcessoVO());
			return;
		}
		obj.setPerfilPadraoCoordenador(getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(obj.getPerfilPadraoCoordenador().getCodigo(), usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PerfilAcessoVO</code> relacionado ao objeto
	 * <code>ConfiguracaoGeralSistemaVO</code>. Faz uso da chave primária da
	 * classe <code>PerfilAcessoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosPerfilPadraoAluno(ConfiguracaoGeralSistemaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPerfilPadraoAluno().getCodigo().intValue() == 0) {
			obj.setPerfilPadraoAluno(new PerfilAcessoVO());
			return;
		}
		obj.setPerfilPadraoAluno(getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(obj.getPerfilPadraoAluno().getCodigo(), usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>VisaoVO</code> relacionado ao objeto
	 * <code>ConfiguracaoGeralSistemaVO</code>. Faz uso da chave primária da
	 * classe <code>VisaoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosVisaoPadraoCandidato(ConfiguracaoGeralSistemaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getVisaoPadraoCandidato().getCodigo().intValue() == 0) {
			obj.setVisaoPadraoCandidato(new VisaoVO());
			return;
		}
		obj.setVisaoPadraoCandidato(getFacadeFactory().getVisaoFacade().consultarPorChavePrimaria(obj.getVisaoPadraoCandidato().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>VisaoVO</code> relacionado ao objeto
	 * <code>ConfiguracaoGeralSistemaVO</code>. Faz uso da chave primária da
	 * classe <code>VisaoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosVisaoPadraoProfessor(ConfiguracaoGeralSistemaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getVisaoPadraoProfessor().getCodigo().intValue() == 0) {
			obj.setVisaoPadraoProfessor(new VisaoVO());
			return;
		}
		obj.setVisaoPadraoProfessor(getFacadeFactory().getVisaoFacade().consultarPorChavePrimaria(obj.getVisaoPadraoProfessor().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>VisaoVO</code> relacionado ao objeto
	 * <code>ConfiguracaoGeralSistemaVO</code>. Faz uso da chave primária da
	 * classe <code>VisaoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosVisaoPadraoCoordenador(ConfiguracaoGeralSistemaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getVisaoPadraoCoordenador().getCodigo().intValue() == 0) {
			obj.setVisaoPadraoCoordenador(new VisaoVO());
			return;
		}
		obj.setVisaoPadraoCoordenador(getFacadeFactory().getVisaoFacade().consultarPorChavePrimaria(obj.getVisaoPadraoCoordenador().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>VisaoVO</code> relacionado ao objeto
	 * <code>ConfiguracaoGeralSistemaVO</code>. Faz uso da chave primária da
	 * classe <code>VisaoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosVisaoPadraoAluno(ConfiguracaoGeralSistemaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getVisaoPadraoAluno().getCodigo().intValue() == 0) {
			obj.setVisaoPadraoAluno(new VisaoVO());
			return;
		}
		obj.setVisaoPadraoAluno(getFacadeFactory().getVisaoFacade().consultarPorChavePrimaria(obj.getVisaoPadraoAluno().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosVisaoPadraoPais(ConfiguracaoGeralSistemaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getVisaoPadraoPais().getCodigo().intValue() == 0) {
			obj.setVisaoPadraoPais(new VisaoVO());
			return;
		}
		obj.setVisaoPadraoPais(getFacadeFactory().getVisaoFacade().consultarPorChavePrimaria(obj.getVisaoPadraoPais().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ConfiguracaoGeralSistema.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ConfiguracaoGeralSistema.idEntidade = idEntidade;
	}


	public void validarConfiguracaoGeralSistema(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		if (configuracaoGeralSistemaVO.getCodigo().intValue() == 0) {
			throw new Exception("N�o Existe uma Configura��o Geral do Sistemas definida.");
		}
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PerfilAcessoVO</code> relacionado ao objeto
	 * <code>ConfiguracaoGeralSistemaVO</code>. Faz uso da chave primária da
	 * classe <code>PerfilAcessoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosPerfilPadraoParceiro(ConfiguracaoGeralSistemaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPerfilPadraoParceiro().getCodigo().intValue() == 0) {
			obj.setPerfilPadraoParceiro(new PerfilAcessoVO());
			return;
		}
		obj.setPerfilPadraoParceiro(getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(obj.getPerfilPadraoParceiro().getCodigo(), usuario));
	}

	public static void montarDadosResponsavelPadraoComunicadoInterno(ConfiguracaoGeralSistemaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelPadraoComunicadoInterno().getCodigo().intValue() == 0) {
			obj.setResponsavelPadraoComunicadoInterno(new PessoaVO());
			return;
		}
		obj.setResponsavelPadraoComunicadoInterno(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarResponsavelPadraoComunicadoInternoPorCodigoConfiguracoes(obj.getResponsavelPadraoComunicadoInterno().getCodigo()));
	}

	public static void montarDadosFuncionarioRespAlteracaoDados(ConfiguracaoGeralSistemaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFuncionarioRespAlteracaoDados().getCodigo().intValue() == 0) {
			obj.setFuncionarioRespAlteracaoDados(new FuncionarioVO());
			return;
		}
		getFacadeFactory().getFuncionarioFacade().carregarDados(obj.getFuncionarioRespAlteracaoDados(), usuario);
	}

	public static void montarDadosMotivoPadraoAbandonoCurso(ConfiguracaoGeralSistemaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getMotivoPadraoAbandonoCurso().getCodigo().intValue() == 0) {
			obj.setMotivoPadraoAbandonoCurso(new MotivoCancelamentoTrancamentoVO());
			return;
		}
		obj.setMotivoPadraoAbandonoCurso(getFacadeFactory().getMotivoCancelamentoTrancamentoFacade().consultarPorChavePrimaria(obj.getMotivoPadraoAbandonoCurso().getCodigo(), nivelMontarDados, usuario));
	}
	
	public static void montarDadosMotivoPadraoCancelamentoPreMatriculaCalouro(ConfiguracaoGeralSistemaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getMotivoPadraoCancelamentoPreMatriculaCalouro().getCodigo().intValue() == 0) {
			obj.setMotivoPadraoCancelamentoPreMatriculaCalouro(new MotivoCancelamentoTrancamentoVO());
			return;
		}
		obj.setMotivoPadraoCancelamentoPreMatriculaCalouro(getFacadeFactory().getMotivoCancelamentoTrancamentoFacade().consultarPorChavePrimaria(obj.getMotivoPadraoCancelamentoPreMatriculaCalouro().getCodigo(), nivelMontarDados, usuario));
	}

	@Override
	public String consultarRegraComissionamentoRanking(Integer unidadeEnsino) {
		return "";
//		if(getAplicacaoControle() != null) {
//			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getAplicacaoControle().obterConfiguracaoGeralSistemaReferenteUsuarioLogado(unidadeEnsino, null);
//			if(Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO)) {
//				return "";
//			}
//		}
//		StringBuilder sql = new StringBuilder("SELECT descricaoRegraComissionamentoCRM from ConfiguracaoGeralSistema ");
//		sql.append(" inner join Configuracoes on Configuracoes.codigo = ConfiguracaoGeralSistema.configuracoes ");
//		if (unidadeEnsino != null && unidadeEnsino > 0) {
//			sql.append(" inner join UnidadeEnsino on Configuracoes.codigo = UnidadeEnsino.configuracoes ");
//			sql.append(" and UnidadeEnsino.codigo = ").append(unidadeEnsino);
//		} else {
//			sql.append(" and Configuracoes.padrao = true ");
//		}
//		sql.append(" limit 1 ");
//		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
//		if (rs.next()) {
//			return rs.getString("descricaoRegraComissionamentoCRM");
//		}
//		return "";
	}

	@Override
	public ConfiguracaoGeralSistemaVO consultarConfiguraoesQtdeDiasNotificacaoDataProcessoSeletivo() throws Exception {
		if(getAplicacaoControle() != null) {
			return getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null);
		}
		String sqlStr = "select qtdeDiasNotificacaoDataProva from configuracaoGeralSistema limit 1";
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (dadosSQL.next()) {
			ConfiguracaoGeralSistemaVO obj = new ConfiguracaoGeralSistemaVO();
			obj.setQtdeDiasNotificacaoDataProva(dadosSQL.getInt("qtdeDiasNotificacaoDataProva"));
			return obj;
		}
		return new ConfiguracaoGeralSistemaVO();
	}
    
	/**
	 * Responsável por executar a verificação se é para apresentar aluno
	 * pendente financeiramente seguindo a regra definida na configuração geral
	 * do sistema. Caso o usuário logado seja professor, é validado o boleano
	 * apresentarAlunoPendenteFinanceiroVisaoProfessor, caso o usuário logado
	 * seja coordenador, é validado o boleano
	 * apresentarAlunoPendenteFinanceiroVisaoCoordenador, caso seja visão da
	 * secretaria e retornado verdadeiro.
	 * 
	 * @author Wellington Rodrigues - 16/04/2015
	 * @param unidadeEnsino
	 * @param usuarioLogado
	 * @return
	 * @throws Exception
	 */
    @Override
	public boolean executarVerificacaoApresentarAlunoPendenteFinanceiramente(Integer unidadeEnsino, UsuarioVO usuarioLogado) throws Exception {
    	if(getAplicacaoControle() != null) {
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getAplicacaoControle().getConfiguracaoGeralSistemaVO(unidadeEnsino, usuarioLogado);
			if(Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO)) {
				if (usuarioLogado.getIsApresentarVisaoProfessor()) {
					return configuracaoGeralSistemaVO.getApresentarAlunoPendenteFinanceiroVisaoProfessor();
				}else if (usuarioLogado.getIsApresentarVisaoCoordenador()) {
					return configuracaoGeralSistemaVO.getApresentarAlunoPendenteFinanceiroVisaoCoordenador();
				}
				return true;
			}
		}
		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = null;
		if (usuarioLogado.getIsApresentarVisaoProfessor()) {
			configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoUnidadeEnsino(unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
			return configuracaoGeralSistemaVO.getApresentarAlunoPendenteFinanceiroVisaoProfessor();
		} else if (usuarioLogado.getIsApresentarVisaoCoordenador()) {
			configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoUnidadeEnsino(unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
			return configuracaoGeralSistemaVO.getApresentarAlunoPendenteFinanceiroVisaoCoordenador();
		}
		return true;
	}

	@Override
	public ConfiguracaoGeralSistemaVO consultarConfiguracaoPadraoSistema() throws Exception {
		if(getAplicacaoControle() != null) {
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null);
			if(Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO)) {
				return configuracaoGeralSistemaVO;
			}
		}
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" select * from configuracaogeralsistema cg " )
		.append(" inner join configuracoes c on c.codigo = cg.configuracoes ")
		.append(" where c.padrao is true limit 1 ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		ConfiguracaoGeralSistemaVO obj = new ConfiguracaoGeralSistemaVO();
		if (dadosSQL.next()) {
			obj = montarDados(dadosSQL, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
		}
		return obj;
	}
	
	@Override
	public String consultarUrlExternoDownloadArquivoPadraoSistema() throws Exception {
		if(getAplicacaoControle() != null) {
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null);
			if(Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO)) {
				return configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo();
			}
		}
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" select urlExternoDownloadArquivo from configuracaogeralsistema cg " )
		.append(" inner join configuracoes c on c.codigo = cg.configuracoes ")
		.append(" where c.padrao limit 1 ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (dadosSQL.next()) {
			return dadosSQL.getString("urlExternoDownloadArquivo");
		}
		return "";
	}
	
	@Override
	public String consultarTokenWebServicePadraoSistema() throws Exception {
		if(getAplicacaoControle() != null) {
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null);
			if(Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO)) {
				return configuracaoGeralSistemaVO.getTokenWebService();
			}
		}
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" select tokenWebService from configuracaogeralsistema cg " )
		.append(" inner join configuracoes c on c.codigo = cg.configuracoes ")
		.append(" where c.padrao limit 1 ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (dadosSQL.next()) {
			return dadosSQL.getString("tokenWebService");
		}
		return "";
	}

    @Override
	public String consultarVesaoSeiSignature() throws Exception {		
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" select versaoSeiSignature from configuracaogeralsistema cg " )
		.append(" inner join configuracoes c on c.codigo = cg.configuracoes ")
		.append(" where c.padrao limit 1 ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (dadosSQL.next()) {
			return dadosSQL.getString("versaoSeiSignature");
		}
		return "";
	}
	
	@Override
	public String consultarUrlAcessoExternoAplicacaoPadraoSistema() throws Exception {
		if(getAplicacaoControle() != null) {
			return getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null).getUrlAcessoExternoAplicacao();
		}
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" select urlacessoexternoaplicacao from configuracaogeralsistema cg " )
		.append(" inner join configuracoes c on c.codigo = cg.configuracoes ")
		.append(" where c.padrao limit 1 ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (dadosSQL.next()) {
			return dadosSQL.getString("urlacessoexternoaplicacao");
		}
		return "";
	}

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirCertificado(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
    	excluir(getIdEntidade(), true, usuarioVO);
    	if(Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO.getCertificadoParaDocumento())) {
    		getConexao().getJdbcTemplate().update("update configuracaoGeralSistema set certificadoParaDocumento = null, senhaCertificadoParaDocumento = '' where codigo = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), configuracaoGeralSistemaVO.getCodigo());
    		getFacadeFactory().getArquivoFacade().excluir(configuracaoGeralSistemaVO.getCertificadoParaDocumento(), usuarioVO, configuracaoGeralSistemaVO);
    		configuracaoGeralSistemaVO.setCertificadoParaDocumento(new ArquivoVO());
    		configuracaoGeralSistemaVO.setSenhaCertificadoParaDocumento("");
    	}
    }
    
   

    public static void montarDadosMotivoPadraoCancelamentoMatriculaPorOutraMatricula(ConfiguracaoGeralSistemaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	if (obj.getMotivoPadraoCancelamentoOutraMatricula().getCodigo().intValue() == 0) {
    		obj.setMotivoPadraoCancelamentoOutraMatricula(new MotivoCancelamentoTrancamentoVO());
    		return;
    	}
    	obj.setMotivoPadraoCancelamentoOutraMatricula(getFacadeFactory().getMotivoCancelamentoTrancamentoFacade().consultarPorChavePrimaria(obj.getMotivoPadraoCancelamentoOutraMatricula().getCodigo(), nivelMontarDados, usuario));
    }
    
    
    @Override
    public void  carrregarDadosConfiguracaoPadraoCancelamentoMatriculaPorOutraMatriculaPorCodigoConfiguracao(ConfiguracaoGeralSistemaVO obj, UsuarioVO usuario) throws  Exception {
		
        String sqlStr = "SELECT textoOrientacaoCancelamentoPorOutraMatricula, justificativaCancelamentoPorOutraMatricula , motivoPadraoCancelamentoOutraMatricula    FROM configuracaogeralsistema where codigo = "+obj.getCodigo() +"";
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (dadosSQL.next()) {			
			obj.setTextoOrientacaoCancelamentoPorOutraMatricula(dadosSQL.getString("textoOrientacaoCancelamentoPorOutraMatricula"));
			obj.setJustificativaCancelamentoPorOutraMatricula(dadosSQL.getString("justificativaCancelamentoPorOutraMatricula"));
			obj.getMotivoPadraoCancelamentoOutraMatricula().setCodigo(dadosSQL.getInt("motivoPadraoCancelamentoOutraMatricula"));			
			montarDadosMotivoPadraoCancelamentoMatriculaPorOutraMatricula(obj , 0 ,usuario);			
		}
		
	}
    
    public void incluirLogJobSymplicty(String erro, Boolean sucesso) throws Exception {
        try {
            final String sql = "INSERT INTO logjobsymplicty (descricao_erro, sucesso, datahora) VALUES (?, ?, ?)";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, erro); 
                    ps.setBoolean(2, sucesso);  
                    ps.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis())); 
                    return ps;
                }
            });
            
        } catch (Exception e) {
            throw e;
        }
    }
}
