package negocio.comuns.administrativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import controle.administrativo.ConfiguracaoAparenciaSistemaVO;
import jobs.enumeradores.FornecedorSmsEnum;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.MotivoCancelamentoTrancamentoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.basico.ConfiguracoesVO;
import negocio.comuns.basico.PaizVO;
import negocio.comuns.basico.PessoaVO;

import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import webservice.nfse.generic.AmbienteEnum;

/**
 * Reponsável por manter os dados da entidade ConfiguracaoGeralSistema. Classe
 * do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class ConfiguracaoGeralSistemaVO extends SuperVO {

    private Integer codigo;
    private Boolean validarCadastroCpf;
    private String smptPadrao;
    private String idAutenticacaoServOtimize;
    private String portaSmtpPadrao;
    private String emailRemetente;
    private String login;
    private String senha;
    private String mensagemPadrao;
    private String ipServidor;
    private Integer qtdEmailEnvio;
    private Integer qtdeLimiteMsg;
    private Integer departamentoRespServidorNotificacao;
    private Integer qtdeMsgLimiteServidorNotificacao;
    private Integer nrMaximoFolhaRecibo;
    private Integer diasParaRemoverPreMatricula;
    private PessoaVO responsavelPadraoComunicadoInterno;
    private String ipServidorBiometria;
    private Integer nrDiasNotifVencCand;	
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Visao </code>.
     */
    private Boolean primeiroAcessoAlunoCairMeusDados;
    private Boolean primeiroAcessoAlunoResetarSenha;
    private Boolean primeiroAcessoProfessorResetarSenha;    
    private VisaoVO visaoPadraoAluno;
    private VisaoVO visaoPadraoPais;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Visao </code>.
     */
    private VisaoVO visaoPadraoProfessor;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Visao </code>.
     */
    private VisaoVO visaoPadraoCandidato;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Visao </code>.
     */
    private VisaoVO visaoPadraoCoordenador;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>PerfilAcesso </code>.
     */
    private PerfilAcessoVO perfilPadraoAluno;
    private PerfilAcessoVO perfilPadraoPais;
    private PerfilAcessoVO perfilPadraoOuvidoria;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>PerfilAcesso </code>.
     */
    private PerfilAcessoVO perfilPadraoProfessorGraduacao;
    private PerfilAcessoVO perfilPadraoProfessorPosGraduacao;
    private PerfilAcessoVO perfilPadraoProfessorEducacaoInfantil;
    private PerfilAcessoVO perfilPadraoProfessorEnsinoFundamental;
    private PerfilAcessoVO perfilPadraoProfessorEnsinoMedio;
    private PerfilAcessoVO perfilPadraoProfessorTecnicoProfissionalizante;
    private PerfilAcessoVO perfilPadraoProfessorSequencial;
    private PerfilAcessoVO perfilPadraoProfessorMestrado;
    private PerfilAcessoVO perfilPadraoProfessorGraduacaoTecnologica;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>PerfilAcesso </code>.
     */
    private PerfilAcessoVO perfilPadraoCandidato;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>PerfilAcesso </code>.
     */
    private PerfilAcessoVO perfilPadraoCoordenador;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>PerfilAcesso </code>.
     */
    private PerfilAcessoVO perfilPadraoParceiro;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>PerfilAcesso </code>.
     */
    private PerfilAcessoVO perfilPadraoProfessorExtensao;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>UnidadeEnsino </code>.
     */
    private Integer UnidadeEnsino;
    private QuestionarioVO questionarioPerfilSocioEconomico;
    
    private ConfiguracoesVO configuracoesVO;
    private Boolean permiteCancelamentoSemRequerimento;
    private Boolean permiteTrancamentoSemRequerimento;
    private Boolean permiteTransferenciaSemRequerimento;
    private Boolean permiteTransferenciaInternaSemRequerimento;
    private Boolean permiteTransferenciaSaidaSemRequerimento;
    private Boolean permiteAproveitamentoDiscSemRequerimento;
    private Boolean permiteProgramacaoFormaturaSemRequerimento;
    private Boolean permitePortadorDiplomaSemRequerimento;
    private Boolean permiteAlunoVerContasConvenio;
    private Boolean naoPermitirExpedicaoDiplomaDocumentacaoPendente;
    private Boolean naoPermitirAlterarUsernameUsuario;
    private String prefixoMatriculaFuncionario;
    private String prefixoMatriculaProfessor;
    private String urlExternoDownloadArquivo;
    private String urlWebserviceNFe;
    private String urlWebserviceNFSe;
    
    /* Servidor de arquivo online*/
    private Boolean integracaoServidorOnline;
    private String servidorArquivoOnline;
    private String usuarioAutenticacao;
    private String senhaAutenticacao;
    private String nomeRepositorio;
    
    private String mascaraSubRede;
    private String localUploadArquivoFixo;
    private String localUploadArquivoTemp;
	private String localUploadArquivoGED;
    private Integer tamanhoMaximoUpload;
    private String enderecoServidorArquivo;
    private String descricaooVersoCarteirinhaEstudantil;
    private String mensagemTelaLogin;
    private Boolean apresentarMensagemTelaLogin;
    private Boolean apresentarHomeCandidato;
    private Boolean apresentarEsqueceuMinhaSenha;
    private Boolean controlarValidadeCarteirinhaEstudantil;
    private Boolean gerarSenhaCpfAluno;
    private Boolean utilizarCaixaAltaNomePessoa;
    private String linkFacebook;
    private String linkLinkdIn;
    private String linkTwitter;
    private String codigoTwitts;
    private Integer qtdeDiasAlertaRequerimento;
    private Boolean servidorEmailUtilizaSSL;
    private Boolean servidorEmailUtilizaTSL;
    private String textoComunicacaoInterna;
    private String mensagemErroSenha;
    private Boolean monitorarMensagensProfessor;
    private String emailConfirmacaoEnvioComunicado;
    public static final long serialVersionUID = 1L;
    private String mensagemTelaBancoCurriculum;
    private String tituloTelaBancoCurriculum;
    private String tituloTelaBuscaCandidato;
    private Integer qtdDiasExpiracaoVagaBancoCurriculum;
    private FuncionarioVO funcionarioRespAlteracaoDados;
    private Integer qtdDiasNotificacaoExpiracaoVagaBancoCurriculum;
    private String usernameSMS;
    private String senhaSMS;
	private FornecedorSmsEnum fornecedorSMSEnum;
    private Integer qtdeDiasNotificacaoDataProva;
    // CRM
    private Integer qtdAceitavelContatosPendentesNaoIniciados;
    private Integer qtdLimiteContatosPendentesNaoIniciados;
    private Integer qtaAceitavelContatosPendentesNaoFinalizados;
    private Integer qtaLimiteContatosPendentesNaoFinalizados;
    private Boolean associarProspectSemConsultorResponsavelComPrimeiroConsultorInteragir;
    private Boolean associarNovoProspectComConsultorResponsavelCadastro;
    private Boolean permiteInclusaoForaPrazoMatriculaPeriodoAtiva;
    private Boolean permiteRenovarComParcelaVencida;
    private Boolean permiteRenovarAlunoPreMatriculadoParaNovaMatriculaPreMatricula;
    private String mensagemPadraoRenovacaoMatriculaComParcelaVencida;
    private Integer qtdeAntecedenciaDiasNotificarAlunoDownloadMaterial;
    private Integer qtdeCaractereLimiteDownloadMaterial;
    private Integer avaliacaoInstitucionalFinalModuloAluno;
    private Integer avaliacaoInstitucionalFinalModuloProfessor;
    private Boolean controlaQtdDisciplinaExtensao = Boolean.FALSE;
    private Boolean controlaQtdDisciplinaRealizadaAteMatricula = Boolean.FALSE;
    private Integer qtdDisciplinaExtensao;
    private Integer qtdDisciplinaRealizadaAteMatricula;
    private Integer qtdDiasAcessoAlunoFormado;
    private Integer qtdDiasAcessoAlunoExtensao;
    private Integer qtdDiasAcessoAlunoAtivo;
    protected TipoRequerimentoVO tipoRequerimentoVO;
    private Boolean naoApresentarProfessorVisaoAluno;    
    private Boolean todosCamposObrigatoriosPreInscricao;
    private Boolean ocultarCPFPreInscricao;
    private Boolean ocultarEmailPreInscricao;
    private Boolean ocultarRG;
    private Boolean ocultarDataNasc;
    private Boolean ocultarSexo;
    private Boolean ocultarTelefone;
    private Boolean ocultarEstadoCivil;
    private Boolean ocultarNaturalidade;
    private Boolean ocultarFormacaoAcademica;
    private Boolean ocultarEnderecoPreInscricao;
    private Boolean matricularAlunoReprovFaltaEmDisciplinaJaRealizadaPosGraduacaoExtensao;
    private PaizVO paisPadrao;
    private Integer qtdeDiasNotificarProfessorPostarMaterial;
    private Integer nrDiasLimiteEntregaDocumento;
    private GrupoDestinatariosVO grupoDestinatarioMapaLocalAula;
    private Boolean maeFiliacao;
    private Boolean notificarAlunoAniversariante;
    private Boolean notificarExAlunoAniversariante;
    private Boolean notificarProfessorAniversariante;
    private Boolean notificarFuncionarioAniversariante;
    private Boolean notificarPaiAniversariante;
    private Boolean obrigarTipoMidiaProspect;
    private Boolean controlaAprovacaoDocEntregue;
    
    private Boolean bloquearMatriculaPosSemGraduacao;
	private Integer diasParaNotificarCoordenadorInicioTurma;
	
	private String headerBarIntegracaoSistemasProvaMestreGR;
	private String integracaoMestreGRURLBaseAPI;
	private String actionIntegracaoSistemasProvaMestreGR;
	private String tokenIntegracaoSistemasProvaMestreGR;
	private Boolean habilitarIntegracaoSistemaProvas;
	private Boolean habilitarOperacoesTempoRealIntegracaoMestreGR;
	private Boolean habilitarRecursoInativacaoCredenciasAlunosFormados;
    
    /**
     * Regras de calculo de comissão do ranking do tcc
     *
     */
    
//    private Boolean considerarRankingCrmSomenteMatriculAtivo;
//    private Boolean desconsiderarRankingCrmAlunoBolsista;
//    private Boolean considerarRankingCrmPrimeiraMensalidade;
//    private Boolean considerarContratoAssinadoRankingCrm;
//    private Integer qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm;
//    private Boolean desconsiderarMatriculaContratoNaoAssinado4Meses;
//    private Boolean desconsiderarParcelaEFaltaApartir3Meses;
//    private Integer desconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm;
//    private Boolean considerarAlunoAdimplenteSemContratoAssinadoRankingCrm;
//    private Integer qtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm;
//    private String descricaoRegraComissionamentoCRM;
    private String urlConfirmacaoPreInscricao;
    private Integer qtdDiasMaximoAntecedenciaRemarcarAulaReposicao;
    private Boolean calcularMediaAoGravar;
    private Boolean realizarCalculoMediaFinalFechPeriodo;
	private Boolean permitiAlunoPreMatriculaSolicitarRequerimento;
    private Boolean validarQtdAlunoLocalSalaAulaInclusaoReposicaoDisciplina;
    private Boolean desconsiderarAlunoReposicaoVagasTurma;
    private Integer qtdeDiaNotificarAbandonoCurso;
    private Integer qtdeDiaRegistrarAbandonoCurso;
    private Integer percentualBaixaFrequencia;
    private MotivoCancelamentoTrancamentoVO motivoPadraoAbandonoCurso;
    private MotivoCancelamentoTrancamentoVO motivoPadraoCancelamentoPreMatriculaCalouro;

    //    private Integer validadeCarteirinhaEstudantil;
    private Boolean bloquearRealizarTrancamentoComEmprestimoBiblioteca;
    private Boolean bloquearRealizarAbandonoCursoComEmprestimoBiblioteca;
    private Boolean bloquearRealizarCancelamentoComEmprestimoBiblioteca;
    private Boolean bloquearRealizarTransferenciaSaidaComEmprestimoBiblioteca;
    private Boolean bloquearRealizarTransferenciaInternaComEmprestimoBiblioteca;
    private Boolean bloquearRealizarRenovacaoComEmprestimoBiblioteca;
    private Boolean verificarAulaProgramadaIncluirDisciplina;
    private Boolean apresentarAlunoPendenteFinanceiroVisaoProfessor;
    private Boolean apresentarAlunoPendenteFinanceiroVisaoCoordenador;
    
    /**
     * Inicio Dados Perfil Acesso por nível educacional visão aluno e pai
     */
    private PerfilAcessoVO perfilPadraoAlunoEducacaoInfantil;
    private PerfilAcessoVO perfilPadraoAlunoEducacaoFundamental;
    private PerfilAcessoVO perfilPadraoAlunoEducacaoMedio;
    private PerfilAcessoVO perfilPadraoAlunoEducacaoGraduacao;
    private PerfilAcessoVO perfilPadraoAlunoEducacaoGraduacaoTecnologica;
    private PerfilAcessoVO perfilPadraoAlunoEducacaoSequencial;
    private PerfilAcessoVO perfilPadraoAlunoEducacaoExtensao;
    private PerfilAcessoVO perfilPadraoAlunoEducacaoPosGraduacao;
    private PerfilAcessoVO perfilPadraoAlunoEducacaoTecnicoProfissionalizante;
    private PerfilAcessoVO perfilPadraoPaisEducacaoInfantil;
    private PerfilAcessoVO perfilPadraoPaisEducacaoFundamental;
    private PerfilAcessoVO perfilPadraoPaisEducacaoMedio;
    private PerfilAcessoVO perfilPadraoPaisEducacaoGraduacao;
    private PerfilAcessoVO perfilPadraoPaisEducacaoGraduacaoTecnologica;
    private PerfilAcessoVO perfilPadraoPaisEducacaoSequencial;
    private PerfilAcessoVO perfilPadraoPaisEducacaoExtensao;
    private PerfilAcessoVO perfilPadraoPaisEducacaoPosGraduacao;
    private PerfilAcessoVO perfilPadraoPaisEducacaoTecnicoProfissionalizante;
    private PerfilAcessoVO perfilPadraoAlunoPreMatricula;
    private PerfilAcessoVO perfilPadraoAlunoEvasao;
    private PerfilAcessoVO perfilPadraoAlunoFormado;
    
    private Boolean controlarNumeroAulaProgramadoProfessorPorDia;
    private Integer quantidadeAulaMaximaProgramarProfessorDia;
	private Boolean incrementarNumeroExemplarPorBiblioteca;    
    private Boolean zerarNumeroRegistroPorCurso;    
    
    private Boolean criarProspectFiliacao;
    private Boolean criarProspectAluno;
    private Boolean criarProspectCandidato;
    private Boolean criarProspectFuncionario;
    private Boolean ocultarMediaProcSeletivo;
    private Boolean ocultarChamadaCandidatoProcSeletivo;		
    private Boolean ocultarClassificacaoProcSeletivo;
	
    private Boolean controlarCargaHorariaMaximaEstagioObrigatorio;
    private Integer cargaHorariaMaximaSemanal;
    private Integer cargaHorariaMaximaDiaria;
    private Boolean forcarSeguradoraEApoliceParaEstagioObrigatorio;
    private String seguradoraPadraoEstagioObrigatorio;
    private String apolicePadraoEstagioObrigatorio;
    private ArquivoVO certificadoParaDocumento;
    
    private String senhaCertificadoParaDocumento;
    
    private Integer quantidadeCasaDecimalConsiderarNotaProcessoSeletivo;
    
    private String urlAcessoExternoAplicacao;

    private ConfiguracaoAtualizacaoCadastralVO configuracaoAtualizacaoCadastralVO;    
    
    private ConfiguracaoCandidatoProcessoSeletivoVO configuracaoCandidatoProcessoSeletivoVO;
    
    private Boolean bloquearLancamentosNotasAulasFeriadosFinaisSemana;
    
    private Boolean permitirProfessorRealizarLancamentoAlunosPreMatriculados;
    /**
     * Fim Dados Perfil Acesso por nível educacional visão aluno e pai
     */
    
    private Integer quantidadeCaracteresMinimoSenhaUsuario;
    private Boolean nivelSegurancaNumero;
    private Boolean nivelSegurancaLetra;
    private Boolean nivelSegurancaLetraMaiuscula;
    private Boolean nivelSegurancaCaracterEspecial;
    private Integer nivelcontrolealteracaosenha;
    
    
    //Integracao RD Station
    private String idClienteRdStation;
	private String senhaClienteRdStation;
	private String tokenRdStation;
	private String tokenPrivadoRdStation;
	private Boolean ativarIntegracaoRdStation;
	private String identificadorRdStation;

	//Integracao Web Service
	private String tokenWebService;
	private String loginIntegracaoSofFin;
	private String senhaIntegracaoSofFin;
	private String tokenIntegracaoSofFin;
	private AmbienteEnum ambienteEnumIntegracaoSoftFin;
	/**
	 * 
	 */
	private String linkAcessoVisoesMoodle;
	private Integer qtdTentativasFalhaLogin;
	private Integer tempoBloqTentativasFalhaLogin;
   
	/**
    * Controle de acesso por situação de matrícula
    */
   private Boolean permitirAcessoAlunoPreMatricula;
   private Boolean permitirAcessoAlunoEvasao;
    private Boolean permitirAcessoAlunoFormado;

    private String siglaAbonoFalta;
    private String descricaoAbonoFalta;

    private Boolean permiteReativacaoMatriculaSemRequerimento;

    /**
     * LDAP
     */
    private List<ConfiguracaoLdapVO> configuracaoLdapVOs;

    /**
     * Inicio Dados controlar o perfil de acesso de alunos que não assinaram o contrato
     */
    private Boolean definirPerfilAcessoAlunoNaoAssinouContratoMatricula;
    private PerfilAcessoVO perfilAcessoAlunoNaoAssinouContratoMatricula;
    private Boolean apresentarMensagemAlertaAlunoNaoAssinouContrato;
    private String mensagemAlertaAlunoNaoAssinouContratoMatricula;
    /**
     * Fim Dados controlar o perfil de acesso de alunos que não assinaram o contrato
     */

   private Boolean apresentarBotoesAcoesRequerimentoApenasAbaInicial;
   private String textoParaOrientacaoTcc;
   private String textoParaOrientacaoProjetoIntegrador;
   
   private ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistema;
   
   private Boolean logUploadProfessor;
   private String linkNormasMatricula;
   private String textoFinalizacaoMatriculaOnline;
   private QuestionarioVO questionarioPlanoEnsino;
   
   
	//Integracao LGPD
   private Boolean ativarIntegracaoLGPD;
   private String nomeDestinatarioEmailIntegracaoLGPD;
   private String emailDestinatarioIntegracaoLGPD;
   private String mensagemLGPD;
   private String assuntoMensagemLGPD;
   
   private TextoPadraoDeclaracaoVO textoPadraoDadosSensiveisLGPD;  


	private String textoOrientacaoCancelamentoPorOutraMatricula;
	private MotivoCancelamentoTrancamentoVO motivoPadraoCancelamentoOutraMatricula;
	private String justificativaCancelamentoPorOutraMatricula;
	private Boolean apresentarDocumentoPortalTransparenciaComPendenciaAssinatura;
	private Integer tamanhoMaximoCorpoMensagem;
	private Integer limiteDestinatariosPorEmail;
	private Integer tamanhoLimiteAnexoEmail;
	private String versaoSeiSignature;
	private Date dataBaseValidacaoDiplomaDigital;
    private Boolean habilitarIntegracaoSistemaSymplicty;
    private String hostIntegracaoSistemaSymplicty;
    private String userIntegracaoSistemaSymplicty;
    private String passIntegracaoSistemaSymplicty;
    private Integer portIntegracaoSistemaSymplicty;
    private String protocolIntegracaoSistemaSymplicty;
    private String pastaDestinoRemotaSymplicty;
    private Boolean habilitarIntegracaoSistemaTechCert;
    private String tokenSentry;
    private Boolean habilitarMonitoramentoSentry;
    private Boolean habilitarRecursosAcademicosVisaoAluno;
    private Boolean habilitarMonitoramentoBlackboardSentry;
    private String tokenBlackboardSentry;
    private Boolean habilitarEnvioEmailAssincrono;
    private Boolean ativarDebugEmail;
    private Long timeOutFilaEmail;
	/**
     * Construtor padrão da classe <code>ConfiguracaoGeralSistema</code>. Cria
     * uma nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public ConfiguracaoGeralSistemaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ConfiguracaoGeralSistemaVO</code>. Todos os tipos de consistência
     * de dados são e devem ser implementadas neste método. São validações
     * típicas: verificação de campos obrigatórios, verificação de valores
     * válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ConfiguracaoGeralSistemaVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getQtdAceitavelContatosPendentesNaoIniciados() > obj.getQtdLimiteContatosPendentesNaoIniciados()) {
            throw new ConsistirException("O campo (Número aceitável de contatos pendentes não iniciados ) não pode ser maior que o campo (Número limite de contatos pendentes não iniciados ). ");
        }
        if (obj.getQtaAceitavelContatosPendentesNaoFinalizados() > obj.getQtaLimiteContatosPendentesNaoFinalizados()) {
            throw new ConsistirException("O campo (Número aceitável de contatos pendentes não finalizados) não pode ser maior que o campo (Número limite de contatos pendentes não finalizados). ");
        }
        if (!obj.getPermiteRenovarComParcelaVencida() && obj.getMensagemPadraoRenovacaoMatriculaComParcelaVencida().trim().isEmpty()) {
            throw new ConsistirException("O campos (Mensagem Renovar Matrícula Visão Aluno Com Parcela Vencida) deve ser informado.");
        }
        if (obj.getServidorEmailUtilizaSSL().booleanValue() && obj.getServidorEmailUtilizaTSL().booleanValue()) {
            throw new ConsistirException("Deve ser definido apenas um modelo de criptografia para o envio do email ( SSL ou TSL ).");
        }
        if(obj.getQtdeDiaRegistrarAbandonoCurso() > 0 && obj.getMotivoPadraoAbandonoCurso().getCodigo() == 0){
        	   throw new ConsistirException("O campo MOTIVO PADRÃO ABANDONO DE CURSO deve ser informado.");
        }
        if(obj.getControlarNumeroAulaProgramadoProfessorPorDia() && !Uteis.isAtributoPreenchido(obj.getQuantidadeAulaMaximaProgramarProfessorDia())){
        	throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoGeralSistema_quantidadeAulaMaximaProgramarProfessorDia"));
        }
        if(obj.getUrlAcessoExternoAplicacao().endsWith("/")) {
        	obj.setUrlAcessoExternoAplicacao(obj.getUrlAcessoExternoAplicacao().substring(0, obj.getUrlAcessoExternoAplicacao().length() - 1));
        }
        
        if (Uteis.isAtributoPreenchido(obj.getSiglaAbonoFalta()) && !Uteis.isAtributoPreenchido(obj.getDescricaoAbonoFalta())) {
        	throw new ConsistirException("O campo DESCRIÇÃO ABONO DE FALTAS deve ser informado.");
        }
        
        if (!Uteis.isAtributoPreenchido(obj.getSiglaAbonoFalta()) && Uteis.isAtributoPreenchido(obj.getDescricaoAbonoFalta())) {
        	throw new ConsistirException("O campo SIGLA PARA ABONO DE FALTA deve ser informado.");
        }
        for(ConfiguracaoLdapVO configuracaoLdapVO: obj.getConfiguracaoLdapVOs()) {
        	if(obj.getConfiguracaoLdapVOs().stream().filter(t -> t.getDominio().equals(configuracaoLdapVO.getDominio())).count() > 1l) {
            	throw new ConsistirException("Existem "+obj.getConfiguracaoLdapVOs().stream().filter(t -> t.getDominio().equals(configuracaoLdapVO.getDominio())).count()+" INTEGRAÇÕES LDAP para o DOMÍNIO "+configuracaoLdapVO.getDominio()+", exclua/altere um deles para prosseguir.");
        	}
        }
        
        if (obj.getHabilitarIntegracaoSistemaProvas()) {
			if(!Uteis.isAtributoPreenchido(obj.getTokenIntegracaoSistemasProvaMestreGR())) {
	            throw new ConsistirException("O campos (Token de Integração Sistema de Provas Aba API's de Integração SEI) deve ser informado.");		
			}
			if(!Uteis.isAtributoPreenchido(obj.getActionIntegracaoSistemasProvaMestreGR())) {
				 throw new ConsistirException("O campos (Action de Integração Sistema de Provas Aba API's de Integração SEI) deve ser informado.");		           
			}
			if(!Uteis.isAtributoPreenchido(obj.getHeaderBarIntegracaoSistemasProvaMestreGR())) {
				 throw new ConsistirException("O campos (HeaderBar de Integração Sistema de Provas Aba API's de Integração SEI) deve ser informado.");
			}
		}
        
        if(obj.getAtivarIntegracaoLGPD()) {
        	if(!Uteis.isAtributoPreenchido(obj.getNomeDestinatarioEmailIntegracaoLGPD())) {
        		throw new ConsistirException("O campo (Nome Destinatário Email Integração) deve ser informado.");
        	}
        	if(!Uteis.isAtributoPreenchido(obj.getEmailDestinatarioIntegracaoLGPD())) {
        		throw new ConsistirException("O campo (Email Destinatário Integração) deve ser informado.");
        	}
        	if(!Uteis.isAtributoPreenchido(obj.getAssuntoMensagemLGPD())) {
        		throw new ConsistirException("O campo (Assunto da Mensagem) deve ser informado.");
        	}  	
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setValidarCadastroCpf(Boolean.FALSE);
        setSmptPadrao("");
        setPortaSmtpPadrao("");
        setEmailRemetente("");
        setLogin("");
        setSenha("");
        setMensagemPadrao("");
        setQtdeLimiteMsg(0);
        setPermiteCancelamentoSemRequerimento(Boolean.FALSE);
        setPermiteTrancamentoSemRequerimento(Boolean.FALSE);
        setPermiteTransferenciaSemRequerimento(Boolean.FALSE);
        setPermiteTransferenciaInternaSemRequerimento(Boolean.FALSE);
        setPermiteTransferenciaSaidaSemRequerimento(Boolean.FALSE);
        setPermiteAproveitamentoDiscSemRequerimento(Boolean.FALSE);
        setPermiteProgramacaoFormaturaSemRequerimento(Boolean.FALSE);
        setPermitePortadorDiplomaSemRequerimento(Boolean.FALSE);
    }

    /**
     * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>ConfiguracaoGeralSistema</code>).
     */
    public Integer getUnidadeEnsino() {
        if (UnidadeEnsino == null) {
            UnidadeEnsino = 0;
        }
        return (UnidadeEnsino);
    }

    /**
     * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>ConfiguracaoGeralSistema</code>).
     */
    public void setUnidadeEnsino(Integer obj) {
        this.UnidadeEnsino = obj;
    }

    /**
     * Retorna o objeto da classe <code>PerfilAcesso</code> relacionado com (
     * <code>ConfiguracaoGeralSistema</code>).
     */
    public PerfilAcessoVO getPerfilPadraoCandidato() {
        if (perfilPadraoCandidato == null) {
            perfilPadraoCandidato = new PerfilAcessoVO();
        }
        return (perfilPadraoCandidato);
    }

    /**
     * Define o objeto da classe <code>PerfilAcesso</code> relacionado com (
     * <code>ConfiguracaoGeralSistema</code>).
     */
    public void setPerfilPadraoCandidato(PerfilAcessoVO obj) {
        this.perfilPadraoCandidato = obj;
    }

    /**
     * Retorna o objeto da classe <code>PerfilAcesso</code> relacionado com (
     * <code>ConfiguracaoGeralSistema</code>).
     */
    public PerfilAcessoVO getPerfilPadraoProfessorGraduacao() {
        if (perfilPadraoProfessorGraduacao == null) {
            perfilPadraoProfessorGraduacao = new PerfilAcessoVO();
        }
        return (perfilPadraoProfessorGraduacao);
    }

    /**
     * Define o objeto da classe <code>PerfilAcesso</code> relacionado com (
     * <code>ConfiguracaoGeralSistema</code>).
     */
    public void setPerfilPadraoProfessorGraduacao(PerfilAcessoVO obj) {
        this.perfilPadraoProfessorGraduacao = obj;
    }

    /**
     * Retorna o objeto da classe <code>PerfilAcesso</code> relacionado com (
     * <code>ConfiguracaoGeralSistema</code>).
     */
    public PerfilAcessoVO getPerfilPadraoAluno() {
        if (perfilPadraoAluno == null) {
            perfilPadraoAluno = new PerfilAcessoVO();
        }
        return (perfilPadraoAluno);
    }

    /**
     * Define o objeto da classe <code>PerfilAcesso</code> relacionado com (
     * <code>ConfiguracaoGeralSistema</code>).
     */
    public void setPerfilPadraoPais(PerfilAcessoVO obj) {
        this.perfilPadraoPais = obj;
    }

    public PerfilAcessoVO getPerfilPadraoPais() {
        if (perfilPadraoPais == null) {
            perfilPadraoPais = new PerfilAcessoVO();
        }
        return (perfilPadraoPais);
    }

    /**
     * Define o objeto da classe <code>PerfilAcesso</code> relacionado com (
     * <code>ConfiguracaoGeralSistema</code>).
     */
    public void setPerfilPadraoAluno(PerfilAcessoVO obj) {
        this.perfilPadraoAluno = obj;
    }

    public VisaoVO getVisaoPadraoPais() {
        if (visaoPadraoPais == null) {
            visaoPadraoPais = new VisaoVO();
        }
        return (visaoPadraoPais);
    }

    /**
     * Define o objeto da classe <code>Visao</code> relacionado com (
     * <code>ConfiguracaoGeralSistema</code>).
     */
    public void setVisaoPadraoPais(VisaoVO obj) {
        this.visaoPadraoPais = obj;
    }

    /**
     * Retorna o objeto da classe <code>Visao</code> relacionado com (
     * <code>ConfiguracaoGeralSistema</code>).
     */
    public VisaoVO getVisaoPadraoCandidato() {
        if (visaoPadraoCandidato == null) {
            visaoPadraoCandidato = new VisaoVO();
        }
        return (visaoPadraoCandidato);
    }

    /**
     * Define o objeto da classe <code>Visao</code> relacionado com (
     * <code>ConfiguracaoGeralSistema</code>).
     */
    public void setVisaoPadraoCandidato(VisaoVO obj) {
        this.visaoPadraoCandidato = obj;
    }

    /**
     * Retorna o objeto da classe <code>Visao</code> relacionado com (
     * <code>ConfiguracaoGeralSistema</code>).
     */
    public VisaoVO getVisaoPadraoProfessor() {
        if (visaoPadraoProfessor == null) {
            visaoPadraoProfessor = new VisaoVO();
        }
        return (visaoPadraoProfessor);
    }

    /**
     * Define o objeto da classe <code>Visao</code> relacionado com (
     * <code>ConfiguracaoGeralSistema</code>).
     */
    public void setVisaoPadraoProfessor(VisaoVO obj) {
        this.visaoPadraoProfessor = obj;
    }

    /**
     * Retorna o objeto da classe <code>Visao</code> relacionado com (
     * <code>ConfiguracaoGeralSistema</code>).
     */
    public VisaoVO getVisaoPadraoAluno() {
        if (visaoPadraoAluno == null) {
            visaoPadraoAluno = new VisaoVO();
        }
        return (visaoPadraoAluno);
    }

    /**
     * Define o objeto da classe <code>Visao</code> relacionado com (
     * <code>ConfiguracaoGeralSistema</code>).
     */
    public void setVisaoPadraoAluno(VisaoVO obj) {
        this.visaoPadraoAluno = obj;
    }

    public Integer getQtdeLimiteMsg() {
        if (qtdeLimiteMsg == null) {
            qtdeLimiteMsg = 0;
        }
        return (qtdeLimiteMsg);
    }

    public void setQtdeLimiteMsg(Integer qtdeLimiteMsg) {
        this.qtdeLimiteMsg = qtdeLimiteMsg;
    }

    public String getSenha() {
        if (senha == null) {
            senha = "";
        }
        return (senha);
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getMensagemPadrao() {
        if (mensagemPadrao == null) {
            mensagemPadrao = "";
        }
        return (mensagemPadrao);
    }

    public void setMensagemPadrao(String mensagemPadrao) {
        this.mensagemPadrao = mensagemPadrao;
    }

    public String getLogin() {
        if (login == null) {
            login = "";
        }
        return (login);
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPortaSmtpPadrao() {
        if (portaSmtpPadrao == null) {
            portaSmtpPadrao = "";
        }
        return (portaSmtpPadrao);
    }

    public void setPortaSmtpPadrao(String portaSmtpPadrao) {
        this.portaSmtpPadrao = portaSmtpPadrao;
    }

    public String getSmptPadrao() {
        if (smptPadrao == null) {
            smptPadrao = "";
        }
        return (smptPadrao);
    }

    public void setSmptPadrao(String smptPadrao) {
        this.smptPadrao = smptPadrao;
    }

    public Boolean getValidarCadastroCpf() {
    	if (validarCadastroCpf == null) {
    		return true;
    	}
        return (validarCadastroCpf);
    }

    public Boolean isValidarCadastroCpf() {
        return (validarCadastroCpf);
    }

    public void setValidarCadastroCpf(Boolean validarCadastroCpf) {
        this.validarCadastroCpf = validarCadastroCpf;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public QuestionarioVO getQuestionarioPerfilSocioEconomico() {
        if (questionarioPerfilSocioEconomico == null) {
            questionarioPerfilSocioEconomico = new QuestionarioVO();
        }
        return questionarioPerfilSocioEconomico;
    }

    public void setQuestionarioPerfilSocioEconomico(QuestionarioVO questionarioPerfilSocioEconomico) {
        this.questionarioPerfilSocioEconomico = questionarioPerfilSocioEconomico;
    }

    

    /**
     * @return the configuracoesVO
     */
    public ConfiguracoesVO getConfiguracoesVO() {
        if (configuracoesVO == null) {
            configuracoesVO = new ConfiguracoesVO();
        }
        return configuracoesVO;
    }

    /**
     * @param configuracoesVO
     *            the configuracoesVO to set
     */
    public void setConfiguracoesVO(ConfiguracoesVO configuracoesVO) {
        this.configuracoesVO = configuracoesVO;
    }

    /**
     * @return the permiteCancelamentoSemRequerimento
     */
    public Boolean getPermiteCancelamentoSemRequerimento() {
        if (permiteCancelamentoSemRequerimento == null) {
            permiteCancelamentoSemRequerimento = Boolean.FALSE;
        }
        return permiteCancelamentoSemRequerimento;
    }

    /**
     * @param permiteCancelamentoSemRequerimento the permiteCancelamentoSemRequerimento to set
     */
    public void setPermiteCancelamentoSemRequerimento(Boolean permiteCancelamentoSemRequerimento) {
        this.permiteCancelamentoSemRequerimento = permiteCancelamentoSemRequerimento;
    }

    /**
     * @return the permiteTrancamentoSemRequerimento
     */
    public Boolean getPermiteTrancamentoSemRequerimento() {
        if (permiteTrancamentoSemRequerimento == null) {
            permiteTrancamentoSemRequerimento = Boolean.FALSE;
        }
        return permiteTrancamentoSemRequerimento;
    }

    /**
     * @param permiteTrancamentoSemRequerimento the permiteTrancamentoSemRequerimento to set
     */
    public void setPermiteTrancamentoSemRequerimento(Boolean permiteTrancamentoSemRequerimento) {
        this.permiteTrancamentoSemRequerimento = permiteTrancamentoSemRequerimento;
    }

    /**
     * @return the permiteTransferenciaSemRequerimento
     */
    public Boolean getPermiteTransferenciaSemRequerimento() {
        if (permiteTransferenciaSemRequerimento == null) {
            permiteTransferenciaSemRequerimento = Boolean.FALSE;
        }
        return permiteTransferenciaSemRequerimento;
    }

    /**
     * @param permiteTransferenciaSemRequerimento the permiteTransferenciaSemRequerimento to set
     */
    public void setPermiteTransferenciaSemRequerimento(Boolean permiteTransferenciaSemRequerimento) {
        this.permiteTransferenciaSemRequerimento = permiteTransferenciaSemRequerimento;
    }

    /**
     * @return the permiteAproveitamentoDiscSemRequerimento
     */
    public Boolean getPermiteAproveitamentoDiscSemRequerimento() {
        if (permiteAproveitamentoDiscSemRequerimento == null) {
            permiteAproveitamentoDiscSemRequerimento = Boolean.FALSE;
        }
        return permiteAproveitamentoDiscSemRequerimento;
    }

    /**
     * @param permiteAproveitamentoDiscSemRequerimento the permiteAproveitamentoDiscSemRequerimento to set
     */
    public void setPermiteAproveitamentoDiscSemRequerimento(Boolean permiteAproveitamentoDiscSemRequerimento) {
        this.permiteAproveitamentoDiscSemRequerimento = permiteAproveitamentoDiscSemRequerimento;
    }

    public Integer getNrMaximoFolhaRecibo() {
        if (nrMaximoFolhaRecibo == null) {
            nrMaximoFolhaRecibo = 0;
        }
        return nrMaximoFolhaRecibo;
    }

    public void setNrMaximoFolhaRecibo(Integer nrMaximoFolhaRecibo) {
        this.nrMaximoFolhaRecibo = nrMaximoFolhaRecibo;
    }

    /**
     * @return the permiteProgramacaoFormaturaSemRequerimento
     */
    public Boolean getPermiteProgramacaoFormaturaSemRequerimento() {
        if (permiteProgramacaoFormaturaSemRequerimento == null) {
            permiteProgramacaoFormaturaSemRequerimento = Boolean.FALSE;
        }
        return permiteProgramacaoFormaturaSemRequerimento;
    }

    /**
     * @param permiteProgramacaoFormaturaSemRequerimento the permiteProgramacaoFormaturaSemRequerimento to set
     */
    public void setPermiteProgramacaoFormaturaSemRequerimento(Boolean permiteProgramacaoFormaturaSemRequerimento) {
        this.permiteProgramacaoFormaturaSemRequerimento = permiteProgramacaoFormaturaSemRequerimento;
    }

    /**
     * @return the permiteTransferenciaInternaSemRequerimento
     */
    public Boolean getPermiteTransferenciaInternaSemRequerimento() {
        return permiteTransferenciaInternaSemRequerimento;
    }

    /**
     * @param permiteTransferenciaInternaSemRequerimento the permiteTransferenciaInternaSemRequerimento to set
     */
    public void setPermiteTransferenciaInternaSemRequerimento(Boolean permiteTransferenciaInternaSemRequerimento) {
        this.permiteTransferenciaInternaSemRequerimento = permiteTransferenciaInternaSemRequerimento;
    }

    public Boolean getPermiteTransferenciaSaidaSemRequerimento() {
    	return permiteTransferenciaSaidaSemRequerimento;
    }
    
    public void setPermiteTransferenciaSaidaSemRequerimento(Boolean permiteTransferenciaSaidaSemRequerimento) {
    	this.permiteTransferenciaSaidaSemRequerimento = permiteTransferenciaSaidaSemRequerimento;
    }
    
    /**
     * @return the permitePortadorDiplomaSemRequerimento
     */
    public Boolean getPermitePortadorDiplomaSemRequerimento() {
        if (permitePortadorDiplomaSemRequerimento == null) {
            permitePortadorDiplomaSemRequerimento = Boolean.FALSE;
        }
        return permitePortadorDiplomaSemRequerimento;
    }

    /**
     * @param permitePortadorDiplomaSemRequerimento the permitePortadorDiplomaSemRequerimento to set
     */
    public void setPermitePortadorDiplomaSemRequerimento(Boolean permitePortadorDiplomaSemRequerimento) {
        this.permitePortadorDiplomaSemRequerimento = permitePortadorDiplomaSemRequerimento;
    }

    public Boolean getPermiteAlunoVerContasConvenio() {
        if (permiteAlunoVerContasConvenio == null) {
            permiteAlunoVerContasConvenio = false;
        }
        return permiteAlunoVerContasConvenio;
    }

    public void setPermiteAlunoVerContasConvenio(Boolean permiteAlunoVerContasConvenio) {
        this.permiteAlunoVerContasConvenio = permiteAlunoVerContasConvenio;
    }

    public String getPrefixoMatriculaFuncionario() {
        if (prefixoMatriculaFuncionario == null) {
            prefixoMatriculaFuncionario = "";
        }
        return prefixoMatriculaFuncionario;
    }

    public void setPrefixoMatriculaFuncionario(String prefixoMatriculaFuncionario) {
        this.prefixoMatriculaFuncionario = prefixoMatriculaFuncionario;
    }

    public String getPrefixoMatriculaProfessor() {
        if (prefixoMatriculaProfessor == null) {
            prefixoMatriculaProfessor = "";
        }
        return prefixoMatriculaProfessor;
    }

    public void setPrefixoMatriculaProfessor(String prefixoMatriculaProfessor) {
        this.prefixoMatriculaProfessor = prefixoMatriculaProfessor;
    }

    public void setLocalUploadArquivoFixo(String localUploadArquivoFixo) {
        this.localUploadArquivoFixo = localUploadArquivoFixo;
    }

    public String getLocalUploadArquivoFixo() {
        if (localUploadArquivoFixo == null) {
            localUploadArquivoFixo = "";
        }
        return localUploadArquivoFixo;
    }

    public void setLocalUploadArquivoTemp(String localUploadArquivoTemp) {
        this.localUploadArquivoTemp = localUploadArquivoTemp;
    }

    public String getLocalUploadArquivoTemp() {
        if (localUploadArquivoTemp == null) {
            localUploadArquivoTemp = "";
        }
        return localUploadArquivoTemp;
    }
	
	public String getLocalUploadArquivoGED() {
    	if (localUploadArquivoGED == null) {
    		localUploadArquivoGED = "";
    	}
		return localUploadArquivoGED;
	}

	public void setLocalUploadArquivoGED(String localUploadArquivoGED) {
		this.localUploadArquivoGED = localUploadArquivoGED;
	}

    public void setEnderecoServidorArquivo(String enderecoServidorArquivo) {
        this.enderecoServidorArquivo = enderecoServidorArquivo;
    }

    public String getEnderecoServidorArquivo() {
        if (enderecoServidorArquivo == null) {
            enderecoServidorArquivo = "";
        }
        return enderecoServidorArquivo;
    }

    public String getDescricaooVersoCarteirinhaEstudantil() {
        if (descricaooVersoCarteirinhaEstudantil == null) {
            descricaooVersoCarteirinhaEstudantil = "";
        }
        return descricaooVersoCarteirinhaEstudantil;
    }

    public void setDescricaooVersoCarteirinhaEstudantil(String descricaooVersoCarteirinhaEstudantil) {
        this.descricaooVersoCarteirinhaEstudantil = descricaooVersoCarteirinhaEstudantil;
    }

    public Boolean getControlarValidadeCarteirinhaEstudantil() {
        if (controlarValidadeCarteirinhaEstudantil == null) {
            controlarValidadeCarteirinhaEstudantil = Boolean.FALSE;
        }
        return controlarValidadeCarteirinhaEstudantil;
    }

    public void setControlarValidadeCarteirinhaEstudantil(Boolean controlarValidadeCarteirinhaEstudantil) {
        this.controlarValidadeCarteirinhaEstudantil = controlarValidadeCarteirinhaEstudantil;
    }

    /**
     * @return the mensagemTelaLogin
     */
    public String getMensagemTelaLogin() {
        if (mensagemTelaLogin == null) {
            mensagemTelaLogin = "";
        }
        return mensagemTelaLogin;
    }

    /**
     * @param mensagemTelaLogin the mensagemTelaLogin to set
     */
    public void setMensagemTelaLogin(String mensagemTelaLogin) {
        this.mensagemTelaLogin = mensagemTelaLogin;
    }

    /**
     * @return the apresentarMensagemTelaLogin
     */
    public Boolean getApresentarMensagemTelaLogin() {
        if (apresentarMensagemTelaLogin == null) {
            apresentarMensagemTelaLogin = false;
        }
        return apresentarMensagemTelaLogin;
    }

    /**
     * @param apresentarMensagemTelaLogin the apresentarMensagemTelaLogin to set
     */
    public void setApresentarMensagemTelaLogin(Boolean apresentarMensagemTelaLogin) {
        this.apresentarMensagemTelaLogin = apresentarMensagemTelaLogin;
    }

    /**
     * @return the urlExternoDownloadArquivo
     */
    public String getUrlExternoDownloadArquivo() {
        if (urlExternoDownloadArquivo == null) {
            urlExternoDownloadArquivo = "";
        }
        return urlExternoDownloadArquivo;
    }

    /**
     * @param urlExternoDownloadArquivo the urlExternoDownloadArquivo to set
     */
    public void setUrlExternoDownloadArquivo(String urlExternoDownloadArquivo) {
        this.urlExternoDownloadArquivo = urlExternoDownloadArquivo;
    }

    /**
     * @return the mascaraSubRede
     */
    public String getMascaraSubRede() {
        if (mascaraSubRede == null) {
            mascaraSubRede = "";
        }
        return mascaraSubRede;
    }

    /**
     * @param mascaraSubRede the mascaraSubRede to set
     */
    public void setMascaraSubRede(String mascaraSubRede) {
        this.mascaraSubRede = mascaraSubRede;
    }

    /**
     * @return the gerarSenhaCpfAluno
     */
    public Boolean getGerarSenhaCpfAluno() {
        if (gerarSenhaCpfAluno == null) {
            gerarSenhaCpfAluno = Boolean.FALSE;
        }
        return gerarSenhaCpfAluno;
    }

    /**
     * @param gerarSenhaCpfAluno the gerarSenhaCpfAluno to set
     */
    public void setGerarSenhaCpfAluno(Boolean gerarSenhaCpfAluno) {
        this.gerarSenhaCpfAluno = gerarSenhaCpfAluno;
    }

    /**
     * @return the linkFacebook
     */
    public String getLinkFacebook() {
        if (linkFacebook == null) {
            linkFacebook = "";
        }
        return linkFacebook;
    }

    public Boolean getApresentarLinkFacebook() {
        if (getLinkFacebook().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param linkFacebook the linkFacebook to set
     */
    public void setLinkFacebook(String linkFacebook) {
        this.linkFacebook = linkFacebook;
    }

    /**
     * @return the linkLinkdIn
     */
    public String getLinkLinkdIn() {
        if (linkLinkdIn == null) {
            linkLinkdIn = "";
        }
        return linkLinkdIn;
    }

    public Boolean getApresentarLinkLinkdIn() {
        if (getLinkLinkdIn().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param linkLinkdIn the linkLinkdIn to set
     */
    public void setLinkLinkdIn(String linkLinkdIn) {
        this.linkLinkdIn = linkLinkdIn;
    }

    /**
     * @return the linkTwitter
     */
    public String getLinkTwitter() {
        if (linkTwitter == null) {
            linkTwitter = "";
        }
        return linkTwitter;
    }

    public Boolean getApresentarTwitter() {
        if (getLinkTwitter().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param linkTwitter the linkTwitter to set
     */
    public void setLinkTwitter(String linkTwitter) {
        this.linkTwitter = linkTwitter;
    }

    /**
     * @return the codigoTwitts
     */
    public String getCodigoTwitts() {
        if (codigoTwitts == null) {
            codigoTwitts = "";
        }
        return codigoTwitts;
    }

    public Boolean getApresentarCodigoTwitts() {
        if (getCodigoTwitts().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param codigoTwitts the codigoTwitts to set
     */
    public void setCodigoTwitts(String codigoTwitts) {
        this.codigoTwitts = codigoTwitts;
    }

    public PerfilAcessoVO getPerfilPadraoCoordenador() {
        if (perfilPadraoCoordenador == null) {
            perfilPadraoCoordenador = new PerfilAcessoVO();
        }
        return perfilPadraoCoordenador;
    }

    public void setPerfilPadraoCoordenador(PerfilAcessoVO perfilPadraoCoordenador) {
        this.perfilPadraoCoordenador = perfilPadraoCoordenador;
    }

    public VisaoVO getVisaoPadraoCoordenador() {
        if (visaoPadraoCoordenador == null) {
            visaoPadraoCoordenador = new VisaoVO();
        }
        return visaoPadraoCoordenador;
    }

    public void setVisaoPadraoCoordenador(VisaoVO visaoPadraoCoordenador) {
        this.visaoPadraoCoordenador = visaoPadraoCoordenador;
    }
//    public Integer getValidadeCarteirinhaEstudantil() {
//        if(validadeCarteirinhaEstudantil == null){
//            validadeCarteirinhaEstudantil = 0;
//        }
//        return validadeCarteirinhaEstudantil;
//    }
//
//    
//    public void setValidadeCarteirinhaEstudantil(Integer validadeCarteirinhaEstudantil) {
//        this.validadeCarteirinhaEstudantil = validadeCarteirinhaEstudantil;
//    }

    public PerfilAcessoVO getPerfilPadraoProfessorPosGraduacao() {
        if (perfilPadraoProfessorPosGraduacao == null) {
            perfilPadraoProfessorPosGraduacao = new PerfilAcessoVO();
        }
        return perfilPadraoProfessorPosGraduacao;
    }

    public void setPerfilPadraoProfessorPosGraduacao(PerfilAcessoVO perfilPadraoProfessorPosGraduacao) {
        this.perfilPadraoProfessorPosGraduacao = perfilPadraoProfessorPosGraduacao;
    }
    
    public PerfilAcessoVO getPerfilPadraoProfessorEducacaoInfantil() {
    	if (perfilPadraoProfessorEducacaoInfantil == null) {
    		perfilPadraoProfessorEducacaoInfantil = new PerfilAcessoVO();
    	}
		return perfilPadraoProfessorEducacaoInfantil;
	}

	public void setPerfilPadraoProfessorEducacaoInfantil(PerfilAcessoVO perfilPadraoProfessorEducacaoInfantil) {
		this.perfilPadraoProfessorEducacaoInfantil = perfilPadraoProfessorEducacaoInfantil;
	}

	public PerfilAcessoVO getPerfilPadraoProfessorEnsinoFundamental() {
		if (perfilPadraoProfessorEnsinoFundamental == null) {
			perfilPadraoProfessorEnsinoFundamental = new PerfilAcessoVO();
		}
		return perfilPadraoProfessorEnsinoFundamental;
	}

	public void setPerfilPadraoProfessorEnsinoFundamental(PerfilAcessoVO perfilPadraoProfessorEnsinoFundamental) {
		this.perfilPadraoProfessorEnsinoFundamental = perfilPadraoProfessorEnsinoFundamental;
	}

	public PerfilAcessoVO getPerfilPadraoProfessorEnsinoMedio() {
		if (perfilPadraoProfessorEnsinoMedio == null) {
			perfilPadraoProfessorEnsinoMedio = new PerfilAcessoVO();
		}
		return perfilPadraoProfessorEnsinoMedio;
	}

	public void setPerfilPadraoProfessorEnsinoMedio(PerfilAcessoVO perfilPadraoProfessorEnsinoMedio) {
		this.perfilPadraoProfessorEnsinoMedio = perfilPadraoProfessorEnsinoMedio;
	}

	public PerfilAcessoVO getPerfilPadraoProfessorTecnicoProfissionalizante() {
		if (perfilPadraoProfessorTecnicoProfissionalizante == null) {
			perfilPadraoProfessorTecnicoProfissionalizante = new PerfilAcessoVO();
		}
		return perfilPadraoProfessorTecnicoProfissionalizante;
	}

	public void setPerfilPadraoProfessorTecnicoProfissionalizante(
			PerfilAcessoVO perfilPadraoProfessorTecnicoProfissionalizante) {
		this.perfilPadraoProfessorTecnicoProfissionalizante = perfilPadraoProfessorTecnicoProfissionalizante;
	}

	public PerfilAcessoVO getPerfilPadraoProfessorSequencial() {
		if (perfilPadraoProfessorSequencial == null) {
			perfilPadraoProfessorSequencial = new PerfilAcessoVO();
		}
		return perfilPadraoProfessorSequencial;
	}

	public void setPerfilPadraoProfessorSequencial(PerfilAcessoVO perfilPadraoProfessorSequencial) {
		this.perfilPadraoProfessorSequencial = perfilPadraoProfessorSequencial;
	}

	public PerfilAcessoVO getPerfilPadraoProfessorMestrado() {
		if (perfilPadraoProfessorMestrado == null) {
			perfilPadraoProfessorMestrado = new PerfilAcessoVO();
		}
		return perfilPadraoProfessorMestrado;
	}

	public void setPerfilPadraoProfessorMestrado(PerfilAcessoVO perfilPadraoProfessorMestrado) {
		this.perfilPadraoProfessorMestrado = perfilPadraoProfessorMestrado;
	}

	public PerfilAcessoVO getPerfilPadraoProfessorGraduacaoTecnologica() {
		if (perfilPadraoProfessorGraduacaoTecnologica == null) {
			perfilPadraoProfessorGraduacaoTecnologica = new PerfilAcessoVO();
		}
		return perfilPadraoProfessorGraduacaoTecnologica;
	}

	public void setPerfilPadraoProfessorGraduacaoTecnologica(PerfilAcessoVO perfilPadraoProfessorGraduacaoTecnologica) {
		this.perfilPadraoProfessorGraduacaoTecnologica = perfilPadraoProfessorGraduacaoTecnologica;
	}

	/**
     * @return the qtdeDiasAlerta
     */
    public Integer getQtdeDiasAlertaRequerimento() {
        if (qtdeDiasAlertaRequerimento == null) {
            qtdeDiasAlertaRequerimento = 0;
        }
        return qtdeDiasAlertaRequerimento;
    }

    /**
     * @param qtdeDiasAlerta the qtdeDiasAlerta to set
     */
    public void setQtdeDiasAlertaRequerimento(Integer qtdeDiasAlertaRequerimento) {
        this.qtdeDiasAlertaRequerimento = qtdeDiasAlertaRequerimento;
    }

    public Boolean getServidorEmailUtilizaSSL() {
        if (servidorEmailUtilizaSSL == null) {
            servidorEmailUtilizaSSL = false;
        }
        return servidorEmailUtilizaSSL;
    }

    public void setServidorEmailUtilizaSSL(Boolean servidorEmailUtilizaSSL) {
        this.servidorEmailUtilizaSSL = servidorEmailUtilizaSSL;
    }

    public Boolean getServidorEmailUtilizaTSL() {
        if (servidorEmailUtilizaTSL == null) {
            servidorEmailUtilizaTSL = false;
        }
        return servidorEmailUtilizaTSL;
    }

    public void setServidorEmailUtilizaTSL(Boolean servidorEmailUtilizaTSL) {
        this.servidorEmailUtilizaTSL = servidorEmailUtilizaTSL;
    }

    public String getTextoComunicacaoInterna() {
        if (textoComunicacaoInterna == null) {
            textoComunicacaoInterna = "";
        }
        return textoComunicacaoInterna;
    }

    public void setTextoComunicacaoInterna(String textoComunicacaoInterna) {
        this.textoComunicacaoInterna = textoComunicacaoInterna;
    }

    /**
     * @return the apresentarHomeCandidato
     */
    public Boolean getApresentarHomeCandidato() {
        if (apresentarHomeCandidato == null) {
            return true;
        }
        return apresentarHomeCandidato;
    }

    /**
     * @param apresentarHomeCandidato the apresentarHomeCandidato to set
     */
    public void setApresentarHomeCandidato(Boolean apresentarHomeCandidato) {
        this.apresentarHomeCandidato = apresentarHomeCandidato;
    }

    public String getMensagemErroSenha() {
        if (mensagemErroSenha == null) {
            mensagemErroSenha = "";
        }
        return mensagemErroSenha;
    }

    public void setMensagemErroSenha(String mensagemErroSenha) {
        this.mensagemErroSenha = mensagemErroSenha;
    }



    public Boolean getMonitorarMensagensProfessor() {
        if (monitorarMensagensProfessor == null) {
            monitorarMensagensProfessor = false;
        }
        return monitorarMensagensProfessor;
    }

    public void setMonitorarMensagensProfessor(Boolean monitorarMensagensProfessor) {
        this.monitorarMensagensProfessor = monitorarMensagensProfessor;
    }

    public String getEmailConfirmacaoEnvioComunicado() {
        if (emailConfirmacaoEnvioComunicado == null) {
            emailConfirmacaoEnvioComunicado = "";
        }
        return emailConfirmacaoEnvioComunicado;
    }

    public void setEmailConfirmacaoEnvioComunicado(String emailConfirmacaoEnvioComunicado) {
        this.emailConfirmacaoEnvioComunicado = emailConfirmacaoEnvioComunicado;
    }

    public PerfilAcessoVO getPerfilPadraoParceiro() {
        if (perfilPadraoParceiro == null) {
            perfilPadraoParceiro = new PerfilAcessoVO();
        }
        return perfilPadraoParceiro;
    }

    public void setPerfilPadraoParceiro(PerfilAcessoVO perfilPadraoParceiro) {
        this.perfilPadraoParceiro = perfilPadraoParceiro;
    }

    public String getMensagemTelaBancoCurriculum() {
        if (mensagemTelaBancoCurriculum == null || mensagemTelaBancoCurriculum.equals("")) {
            mensagemTelaBancoCurriculum = "<P ALIGN=\"LEFT\">Seja bem vindo (a) Empreendedor(a)<br/> "
                    + "<P ALIGN=\"LEFT\">Será uma honra ter a sua empresa como parceira do Programa Banco de Talentos! Através do preenchimento deste cadastro, você terá acesso a um capital humano altamente qualificado para atuar com excelência em sua organização.<br/> "
                    + "<P ALIGN=\"LEFT\">Todos perfis que se encontram em nosso banco de dados são profissionais que buscam investir em sua carreira, tornando-se cada vez mais competitivos em suas áreas de atuação.<br/> "
                    + "<P ALIGN=\"LEFT\">Além dos aspecto técnico, o também oferece treinamentos de gestão, liderança, trabalho em equipe e outros itens tão necessários para a formação de um profissional preparado para as exigências do atual cenário.<br/> "
                    + "<P ALIGN=\"LEFT\">Em contrapartida, o quer que nossos alunos trabalhem em empresas sérias, comprometidas e capazes de estabelecer um relacionamento duradouro e feliz com seus colaboradores.<br/> "
                    + "<P ALIGN=\"LEFT\">Cadastre-se agora mesmo e publique suas vagas. Logo você terá os melhores talentos dentro de sua empresa.";
        }
        return mensagemTelaBancoCurriculum;
    }

    public void setMensagemTelaBancoCurriculum(String mensagemTelaBancoCurriculum) {
        this.mensagemTelaBancoCurriculum = mensagemTelaBancoCurriculum;
    }

    /**
     * @return the qtdDiasExpiracaoVagaBancoCurriculum
     */
    public Integer getQtdDiasExpiracaoVagaBancoCurriculum() {
        if (qtdDiasExpiracaoVagaBancoCurriculum == null) {
            qtdDiasExpiracaoVagaBancoCurriculum = 0;
        }
        return qtdDiasExpiracaoVagaBancoCurriculum;
    }

    /**
     * @param qtdDiasExpiracaoVagaBancoCurriculum the qtdDiasExpiracaoVagaBancoCurriculum to set
     */
    public void setQtdDiasExpiracaoVagaBancoCurriculum(Integer qtdDiasExpiracaoVagaBancoCurriculum) {
        this.qtdDiasExpiracaoVagaBancoCurriculum = qtdDiasExpiracaoVagaBancoCurriculum;
    }

    /**
     * @return the qtdDiasNotificacaoExpiracaoVagaBancoCurriculum
     */
    public Integer getQtdDiasNotificacaoExpiracaoVagaBancoCurriculum() {
        if (qtdDiasNotificacaoExpiracaoVagaBancoCurriculum == null) {
            qtdDiasNotificacaoExpiracaoVagaBancoCurriculum = 0;
        }
        return qtdDiasNotificacaoExpiracaoVagaBancoCurriculum;
    }

    /**
     * @param qtdDiasNotificacaoExpiracaoVagaBancoCurriculum the qtdDiasNotificacaoExpiracaoVagaBancoCurriculum to set
     */
    public void setQtdDiasNotificacaoExpiracaoVagaBancoCurriculum(Integer qtdDiasNotificacaoExpiracaoVagaBancoCurriculum) {
        this.qtdDiasNotificacaoExpiracaoVagaBancoCurriculum = qtdDiasNotificacaoExpiracaoVagaBancoCurriculum;
    }

    public String getTituloTelaBancoCurriculum() {
        if (tituloTelaBancoCurriculum == null) {
            tituloTelaBancoCurriculum = "Banco de Curriculuns";
        }
        return tituloTelaBancoCurriculum;
    }

    public void setTituloTelaBancoCurriculum(String tituloTelaBancoCurriculum) {
        this.tituloTelaBancoCurriculum = tituloTelaBancoCurriculum;
    }

    /**
     * @return the tituloTelaBuscaCandidato
     */
    public String getTituloTelaBuscaCandidato() {
        if (tituloTelaBuscaCandidato == null) {
            tituloTelaBuscaCandidato = "Busca de Candidato";
        }
        return tituloTelaBuscaCandidato;
    }

    /**
     * @param tituloTelaBuscaCandidato the tituloTelaBuscaCandidato to set
     */
    public void setTituloTelaBuscaCandidato(String tituloTelaBuscaCandidato) {
        this.tituloTelaBuscaCandidato = tituloTelaBuscaCandidato;
    }

    public Integer getQtaAceitavelContatosPendentesNaoFinalizados() {
        if (qtaAceitavelContatosPendentesNaoFinalizados == null) {
            qtaAceitavelContatosPendentesNaoFinalizados = 0;
        }
        return qtaAceitavelContatosPendentesNaoFinalizados;
    }

    public void setQtaAceitavelContatosPendentesNaoFinalizados(Integer qtaAceitavelContatosPendentesNaoFinalizados) {
        this.qtaAceitavelContatosPendentesNaoFinalizados = qtaAceitavelContatosPendentesNaoFinalizados;
    }

    public Integer getQtaLimiteContatosPendentesNaoFinalizados() {
        if (qtaLimiteContatosPendentesNaoFinalizados == null) {
            qtaLimiteContatosPendentesNaoFinalizados = 0;
        }
        return qtaLimiteContatosPendentesNaoFinalizados;
    }

    public void setQtaLimiteContatosPendentesNaoFinalizados(Integer qtaLimiteContatosPendentesNaoFinalizados) {
        this.qtaLimiteContatosPendentesNaoFinalizados = qtaLimiteContatosPendentesNaoFinalizados;
    }

    public Integer getQtdAceitavelContatosPendentesNaoIniciados() {
        if (qtdAceitavelContatosPendentesNaoIniciados == null) {
            qtdAceitavelContatosPendentesNaoIniciados = 0;
        }
        return qtdAceitavelContatosPendentesNaoIniciados;
    }

    public void setQtdAceitavelContatosPendentesNaoIniciados(Integer qtdAceitavelContatosPendentesNaoIniciados) {
        this.qtdAceitavelContatosPendentesNaoIniciados = qtdAceitavelContatosPendentesNaoIniciados;
    }

    public Integer getQtdLimiteContatosPendentesNaoIniciados() {
        if (qtdLimiteContatosPendentesNaoIniciados == null) {
            qtdLimiteContatosPendentesNaoIniciados = 0;
        }
        return qtdLimiteContatosPendentesNaoIniciados;
    }

    public void setQtdLimiteContatosPendentesNaoIniciados(Integer qtdLimiteContatosPendentesNaoIniciados) {
        this.qtdLimiteContatosPendentesNaoIniciados = qtdLimiteContatosPendentesNaoIniciados;
    }

    public PessoaVO getResponsavelPadraoComunicadoInterno() {
        if (responsavelPadraoComunicadoInterno == null) {
            responsavelPadraoComunicadoInterno = new PessoaVO();
        }
        return responsavelPadraoComunicadoInterno;
    }

    public void setResponsavelPadraoComunicadoInterno(PessoaVO responsavelPadraoComunicadoInterno) {
        this.responsavelPadraoComunicadoInterno = responsavelPadraoComunicadoInterno;
    }

    public Boolean getPermiteInclusaoForaPrazoMatriculaPeriodoAtiva() {
        if (permiteInclusaoForaPrazoMatriculaPeriodoAtiva == null) {
            permiteInclusaoForaPrazoMatriculaPeriodoAtiva = Boolean.FALSE;
        }
        return permiteInclusaoForaPrazoMatriculaPeriodoAtiva;
    }

    public void setPermiteInclusaoForaPrazoMatriculaPeriodoAtiva(Boolean permiteInclusaoForaPrazoMatriculaPeriodoAtiva) {
        this.permiteInclusaoForaPrazoMatriculaPeriodoAtiva = permiteInclusaoForaPrazoMatriculaPeriodoAtiva;
    }

    public Boolean getNaoPermitirExpedicaoDiplomaDocumentacaoPendente() {
        if (naoPermitirExpedicaoDiplomaDocumentacaoPendente == null) {
            naoPermitirExpedicaoDiplomaDocumentacaoPendente = Boolean.FALSE;
        }
        return naoPermitirExpedicaoDiplomaDocumentacaoPendente;
    }

    public void setNaoPermitirExpedicaoDiplomaDocumentacaoPendente(Boolean naoPermitirExpedicaoDiplomaDocumentacaoPendente) {
        this.naoPermitirExpedicaoDiplomaDocumentacaoPendente = naoPermitirExpedicaoDiplomaDocumentacaoPendente;
    }

    public Boolean getNaoPermitirAlterarUsernameUsuario() {
        if (naoPermitirAlterarUsernameUsuario == null) {
            naoPermitirAlterarUsernameUsuario = Boolean.FALSE;
        }
        return naoPermitirAlterarUsernameUsuario;
    }

    public void setNaoPermitirAlterarUsernameUsuario(Boolean naoPermitirAlterarUsernameUsuario) {
        this.naoPermitirAlterarUsernameUsuario = naoPermitirAlterarUsernameUsuario;
    }

    public Boolean getPermiteRenovarComParcelaVencida() {
        if (permiteRenovarComParcelaVencida == null) {
            permiteRenovarComParcelaVencida = false;
        }
        return permiteRenovarComParcelaVencida;
    }

    public void setPermiteRenovarComParcelaVencida(Boolean permiteRenovarComParcelaVencida) {
        this.permiteRenovarComParcelaVencida = permiteRenovarComParcelaVencida;
    }

    public String getMensagemPadraoRenovacaoMatriculaComParcelaVencida() {
        if (mensagemPadraoRenovacaoMatriculaComParcelaVencida == null || mensagemPadraoRenovacaoMatriculaComParcelaVencida.equals("")) {
            mensagemPadraoRenovacaoMatriculaComParcelaVencida = "Verificamos em nosso sistema uma pendência financeira. Para mais informações entre em contato com o Departamento Financeiro";
        }
        return mensagemPadraoRenovacaoMatriculaComParcelaVencida;
    }

    public void setMensagemPadraoRenovacaoMatriculaComParcelaVencida(String mensagemPadraoRenovacaoMatriculaComParcelaVencida) {
        this.mensagemPadraoRenovacaoMatriculaComParcelaVencida = mensagemPadraoRenovacaoMatriculaComParcelaVencida;
    }

    public Integer getQtdeAntecedenciaDiasNotificarAlunoDownloadMaterial() {
        if (qtdeAntecedenciaDiasNotificarAlunoDownloadMaterial == null) {
            qtdeAntecedenciaDiasNotificarAlunoDownloadMaterial = 0;
        }
        return qtdeAntecedenciaDiasNotificarAlunoDownloadMaterial;
    }

    public void setQtdeAntecedenciaDiasNotificarAlunoDownloadMaterial(Integer qtdeAntecedenciaDiasNotificarAlunoDownloadMaterial) {
        this.qtdeAntecedenciaDiasNotificarAlunoDownloadMaterial = qtdeAntecedenciaDiasNotificarAlunoDownloadMaterial;
    }

    public Integer getQtdeCaractereLimiteDownloadMaterial() {
    	  if (qtdeCaractereLimiteDownloadMaterial == null) {
    		  qtdeCaractereLimiteDownloadMaterial = 0;
          }
		return qtdeCaractereLimiteDownloadMaterial;
	}

	public void setQtdeCaractereLimiteDownloadMaterial(Integer qtdeCaractereLimiteDownloadMaterial) {
		this.qtdeCaractereLimiteDownloadMaterial = qtdeCaractereLimiteDownloadMaterial;
	}

	/**
     * @return the ipServidor
     */
    public String getIpServidor() {
        if (ipServidor == null) {
            ipServidor = "";
        }
        return ipServidor;
    }

    /**
     * @param ipServidor the ipServidor to set
     */
    public void setIpServidor(String ipServidor) {
        this.ipServidor = ipServidor;
    }

    /**
     * @return the diasParaRemoverPreMatricula
     */
    public Integer getDiasParaRemoverPreMatricula() {
        if (diasParaRemoverPreMatricula == null) {
            diasParaRemoverPreMatricula = 0;
        }
        return diasParaRemoverPreMatricula;
    }

    /**
     * @param diasParaRemoverPreMatricula the diasParaRemoverPreMatricula to set
     */
    public void setDiasParaRemoverPreMatricula(Integer diasParaRemoverPreMatricula) {
        this.diasParaRemoverPreMatricula = diasParaRemoverPreMatricula;
    }

    /**
     * @return the qtdeMsgLimiteServidorNotificacao
     */
    public Integer getQtdeMsgLimiteServidorNotificacao() {
        if (qtdeMsgLimiteServidorNotificacao == null) {
            qtdeMsgLimiteServidorNotificacao = 0;
        }
        return qtdeMsgLimiteServidorNotificacao;
    }

    /**
     * @param qtdeMsgLimiteServidorNotificacao the qtdeMsgLimiteServidorNotificacao to set
     */
    public void setQtdeMsgLimiteServidorNotificacao(Integer qtdeMsgLimiteServidorNotificacao) {
        this.qtdeMsgLimiteServidorNotificacao = qtdeMsgLimiteServidorNotificacao;
    }

    /**
     * @return the permiteRenovarAlunoPreMatriculadoParaNovaMatriculaPreMatricula
     */
    public Boolean getPermiteRenovarAlunoPreMatriculadoParaNovaMatriculaPreMatricula() {
        if (permiteRenovarAlunoPreMatriculadoParaNovaMatriculaPreMatricula == null) {
            permiteRenovarAlunoPreMatriculadoParaNovaMatriculaPreMatricula = Boolean.TRUE;
        }
        return permiteRenovarAlunoPreMatriculadoParaNovaMatriculaPreMatricula;
    }

    /**
     * @param permiteRenovarAlunoPreMatriculadoParaNovaMatriculaPreMatricula the permiteRenovarAlunoPreMatriculadoParaNovaMatriculaPreMatricula to set
     */
    public void setPermiteRenovarAlunoPreMatriculadoParaNovaMatriculaPreMatricula(Boolean permiteRenovarAlunoPreMatriculadoParaNovaMatriculaPreMatricula) {
        this.permiteRenovarAlunoPreMatriculadoParaNovaMatriculaPreMatricula = permiteRenovarAlunoPreMatriculadoParaNovaMatriculaPreMatricula;
    }

    /**
     * @return the avaliacaoInstitucionalFinalModuloAluno
     */
    public Integer getAvaliacaoInstitucionalFinalModuloAluno() {
        if (avaliacaoInstitucionalFinalModuloAluno == null) {
            avaliacaoInstitucionalFinalModuloAluno = 0;
        }
        return avaliacaoInstitucionalFinalModuloAluno;
    }

    /**
     * @param avaliacaoInstitucionalFinalModuloAluno the avaliacaoInstitucionalFinalModuloAluno to set
     */
    public void setAvaliacaoInstitucionalFinalModuloAluno(Integer avaliacaoInstitucionalFinalModuloAluno) {
        this.avaliacaoInstitucionalFinalModuloAluno = avaliacaoInstitucionalFinalModuloAluno;
    }

    /**
     * @return the avaliacaoInstitucionalFinalModuloProfessor
     */
    public Integer getAvaliacaoInstitucionalFinalModuloProfessor() {
        if (avaliacaoInstitucionalFinalModuloProfessor == null) {
            avaliacaoInstitucionalFinalModuloProfessor = 0;
        }
        return avaliacaoInstitucionalFinalModuloProfessor;
    }

    /**
     * @param avaliacaoInstitucionalFinalModuloProfessor the avaliacaoInstitucionalFinalModuloProfessor to set
     */
    public void setAvaliacaoInstitucionalFinalModuloProfessor(Integer avaliacaoInstitucionalFinalModuloProfessor) {
        this.avaliacaoInstitucionalFinalModuloProfessor = avaliacaoInstitucionalFinalModuloProfessor;
    }

    public Boolean getUtilizarCaixaAltaNomePessoa() {
        if (utilizarCaixaAltaNomePessoa == null) {
            utilizarCaixaAltaNomePessoa = Boolean.FALSE;
        }
        return utilizarCaixaAltaNomePessoa;
    }

    public void setUtilizarCaixaAltaNomePessoa(Boolean utilizarCaixaAltaNomePessoa) {
        this.utilizarCaixaAltaNomePessoa = utilizarCaixaAltaNomePessoa;
    }

    /**
     * @return the controlaQtdDisciplinaExtensao
     */
    public Boolean getControlaQtdDisciplinaExtensao() {
        return controlaQtdDisciplinaExtensao;
    }

    /**
     * @param controlaQtdDisciplinaExtensao the controlaQtdDisciplinaExtensao to set
     */
    public void setControlaQtdDisciplinaExtensao(Boolean controlaQtdDisciplinaExtensao) {
        this.controlaQtdDisciplinaExtensao = controlaQtdDisciplinaExtensao;
    }

    /**
     * @return the qtdDisciplinaExtensao
     */
    public Integer getQtdDisciplinaExtensao() {
        if (qtdDisciplinaExtensao == null) {
            qtdDisciplinaExtensao = 0;
        }
        return qtdDisciplinaExtensao;
    }

    /**
     * @param qtdDisciplinaExtensao the qtdDisciplinaExtensao to set
     */
    public void setQtdDisciplinaExtensao(Integer qtdDisciplinaExtensao) {
        this.qtdDisciplinaExtensao = qtdDisciplinaExtensao;
    }

    /**
     * @return the departamentoRespServidorNotificacao
     */
    public Integer getDepartamentoRespServidorNotificacao() {
        if (departamentoRespServidorNotificacao == null) {
            departamentoRespServidorNotificacao = 0;
        }
        return departamentoRespServidorNotificacao;
    }

    /**
     * @param departamentoRespServidorNotificacao the departamentoRespServidorNotificacao to set
     */
    public void setDepartamentoRespServidorNotificacao(Integer departamentoRespServidorNotificacao) {
        this.departamentoRespServidorNotificacao = departamentoRespServidorNotificacao;
    }

    public TipoRequerimentoVO getTipoRequerimentoVO() {
        if (tipoRequerimentoVO == null) {
            tipoRequerimentoVO = new TipoRequerimentoVO();
        }
        return tipoRequerimentoVO;
    }

    public void setTipoRequerimentoVO(TipoRequerimentoVO tipoRequerimentoVO) {
        this.tipoRequerimentoVO = tipoRequerimentoVO;
    }

    /**
     * @return the idAutenticacaoServOtimize
     */
    public String getIdAutenticacaoServOtimize() {
        if (idAutenticacaoServOtimize == null) {
            idAutenticacaoServOtimize = "";
        }
        return idAutenticacaoServOtimize;
    }

    /**
     * @param idAutenticacaoServOtimize the idAutenticacaoServOtimize to set
     */
    public void setIdAutenticacaoServOtimize(String idAutenticacaoServOtimize) {
        this.idAutenticacaoServOtimize = idAutenticacaoServOtimize;
    }

    public PerfilAcessoVO getPerfilPadraoOuvidoria() {
        if (perfilPadraoOuvidoria == null) {
            perfilPadraoOuvidoria = new PerfilAcessoVO();
        }
        return perfilPadraoOuvidoria;
    }

    public void setPerfilPadraoOuvidoria(PerfilAcessoVO perfilPadraoOuvidoria) {
        this.perfilPadraoOuvidoria = perfilPadraoOuvidoria;
    }

    public Integer getTamanhoMaximoUpload() {
        if (tamanhoMaximoUpload == null) {
            tamanhoMaximoUpload = 15;
        }
        return tamanhoMaximoUpload;
    }

    public void setTamanhoMaximoUpload(Integer tamanhoMaximoUpload) {
        this.tamanhoMaximoUpload = tamanhoMaximoUpload;
    }

    public Boolean getNaoApresentarProfessorVisaoAluno() {
        if (naoApresentarProfessorVisaoAluno == null) {
            naoApresentarProfessorVisaoAluno = false;
        }
        return naoApresentarProfessorVisaoAluno;
    }

    public void setNaoApresentarProfessorVisaoAluno(Boolean naoApresentarProfessorVisaoAluno) {
        this.naoApresentarProfessorVisaoAluno = naoApresentarProfessorVisaoAluno;
    }

    public Boolean getOcultarCPFPreInscricao() {
        if (ocultarCPFPreInscricao == null) {
            ocultarCPFPreInscricao = false;
        }
        return ocultarCPFPreInscricao;
    }

    public void setOcultarCPFPreInscricao(Boolean ocultarCPFPreInscricao) {
        this.ocultarCPFPreInscricao = ocultarCPFPreInscricao;
    }

    public Boolean getOcultarEnderecoPreInscricao() {
        if (ocultarEnderecoPreInscricao == null) {
            ocultarEnderecoPreInscricao = false;
        }
        return ocultarEnderecoPreInscricao;
    }

    public void setOcultarEnderecoPreInscricao(Boolean ocultarEnderecoPreInscricao) {
        this.ocultarEnderecoPreInscricao = ocultarEnderecoPreInscricao;
    }

    /**
     * @return the associarProspectSemConsultorResponsavelComPrimeiroConsultorInteragir
     */
    public Boolean getAssociarProspectSemConsultorResponsavelComPrimeiroConsultorInteragir() {
        if (associarProspectSemConsultorResponsavelComPrimeiroConsultorInteragir == null) {
            associarProspectSemConsultorResponsavelComPrimeiroConsultorInteragir = Boolean.FALSE;
        }
        return associarProspectSemConsultorResponsavelComPrimeiroConsultorInteragir;
    }

    /**
     * @param associarProspectSemConsultorResponsavelComPrimeiroConsultorInteragir the associarProspectSemConsultorResponsavelComPrimeiroConsultorInteragir to set
     */
    public void setAssociarProspectSemConsultorResponsavelComPrimeiroConsultorInteragir(Boolean associarProspectSemConsultorResponsavelComPrimeiroConsultorInteragir) {
        this.associarProspectSemConsultorResponsavelComPrimeiroConsultorInteragir = associarProspectSemConsultorResponsavelComPrimeiroConsultorInteragir;
    }

    public PaizVO getPaisPadrao() {
        if (paisPadrao == null) {
            paisPadrao = new PaizVO();
        }
        return paisPadrao;
    }

    public void setPaisPadrao(PaizVO paisPadrao) {
        this.paisPadrao = paisPadrao;
    }

    /***
     * @return the associarNovoProspectComConsultorResponsavelCadastro
     */
    public Boolean getAssociarNovoProspectComConsultorResponsavelCadastro() {
        if (associarNovoProspectComConsultorResponsavelCadastro == null) {
            associarNovoProspectComConsultorResponsavelCadastro = Boolean.FALSE;
        }
        return associarNovoProspectComConsultorResponsavelCadastro;
    }

    /**
     * @param associarNovoProspectComConsultorResponsavelCadastro the associarNovoProspectComConsultorResponsavelCadastro to set
     */
    public void setAssociarNovoProspectComConsultorResponsavelCadastro(Boolean associarNovoProspectComConsultorResponsavelCadastro) {
        this.associarNovoProspectComConsultorResponsavelCadastro = associarNovoProspectComConsultorResponsavelCadastro;
    }

    /**
     * @return the controlaQtdDisciplinaRealizadaAteMatricula
     */
    public Boolean getControlaQtdDisciplinaRealizadaAteMatricula() {
        return controlaQtdDisciplinaRealizadaAteMatricula;
    }

    /**
     * @param controlaQtdDisciplinaRealizadaAteMatricula the controlaQtdDisciplinaRealizadaAteMatricula to set
     */
    public void setControlaQtdDisciplinaRealizadaAteMatricula(Boolean controlaQtdDisciplinaRealizadaAteMatricula) {
        this.controlaQtdDisciplinaRealizadaAteMatricula = controlaQtdDisciplinaRealizadaAteMatricula;
    }

    /**
     * @return the qtdDisciplinaRealizadaAteMatricula
     */
    public Integer getQtdDisciplinaRealizadaAteMatricula() {
        if (qtdDisciplinaRealizadaAteMatricula == null) {
            qtdDisciplinaRealizadaAteMatricula = 0;
        }
        return qtdDisciplinaRealizadaAteMatricula;
    }

    /**
     * @param qtdDisciplinaRealizadaAteMatricula the qtdDisciplinaRealizadaAteMatricula to set
     */
    public void setQtdDisciplinaRealizadaAteMatricula(Integer qtdDisciplinaRealizadaAteMatricula) {
        this.qtdDisciplinaRealizadaAteMatricula = qtdDisciplinaRealizadaAteMatricula;
    }

    /**
     * @return the primeiroAcessoAlunoCairMeusDados
     */
    public Boolean getPrimeiroAcessoAlunoCairMeusDados() {
        if (primeiroAcessoAlunoCairMeusDados == null) {
            primeiroAcessoAlunoCairMeusDados = Boolean.FALSE;
        }
        return primeiroAcessoAlunoCairMeusDados;
    }

    /**
     * @param primeiroAcessoAlunoCairMeusDados the primeiroAcessoAlunoCairMeusDados to set
     */
    public void setPrimeiroAcessoAlunoCairMeusDados(Boolean primeiroAcessoAlunoCairMeusDados) {
        this.primeiroAcessoAlunoCairMeusDados = primeiroAcessoAlunoCairMeusDados;
    }

    /**
     * @return the qtdDiasAcessoAlunoFormado
     */
    public Integer getQtdDiasAcessoAlunoFormado() {
        if (qtdDiasAcessoAlunoFormado == null) {
            qtdDiasAcessoAlunoFormado = 0;
        }
        return qtdDiasAcessoAlunoFormado;
    }

    /**
     * @param qtdDiasAcessoAlunoFormado the qtdDiasAcessoAlunoFormado to set
     */
    public void setQtdDiasAcessoAlunoFormado(Integer qtdDiasAcessoAlunoFormado) {
        this.qtdDiasAcessoAlunoFormado = qtdDiasAcessoAlunoFormado;
    }

    /**
     * @return the qtdDiasAcessoAlunoExtensao
     */
    public Integer getQtdDiasAcessoAlunoExtensao() {
        if (qtdDiasAcessoAlunoExtensao == null) {
            qtdDiasAcessoAlunoExtensao = 0;
        }
        return qtdDiasAcessoAlunoExtensao;
    }

    /**
     * @param qtdDiasAcessoAlunoExtensao the qtdDiasAcessoAlunoExtensao to set
     */
    public void setQtdDiasAcessoAlunoExtensao(Integer qtdDiasAcessoAlunoExtensao) {
        this.qtdDiasAcessoAlunoExtensao = qtdDiasAcessoAlunoExtensao;
    }

	public Integer getQtdeDiasNotificarProfessorPostarMaterial() {
		if(qtdeDiasNotificarProfessorPostarMaterial == null){
			qtdeDiasNotificarProfessorPostarMaterial = 0;
		}
		return qtdeDiasNotificarProfessorPostarMaterial;
	}

	public void setQtdeDiasNotificarProfessorPostarMaterial(Integer qtdeDiasNotificarProfessorPostarMaterial) {
		this.qtdeDiasNotificarProfessorPostarMaterial = qtdeDiasNotificarProfessorPostarMaterial;
	}

    /**
     * @return the apresentarEsqueceuMinhaSenha
     */
    public Boolean getApresentarEsqueceuMinhaSenha() {
        if (apresentarEsqueceuMinhaSenha == null) {
            apresentarEsqueceuMinhaSenha = true;
        }
        return apresentarEsqueceuMinhaSenha;
    }

    /**
     * @param apresentarEsqueceuMinhaSenha the apresentarEsqueceuMinhaSenha to set
     */
    public void setApresentarEsqueceuMinhaSenha(Boolean apresentarEsqueceuMinhaSenha) {
        this.apresentarEsqueceuMinhaSenha = apresentarEsqueceuMinhaSenha;
    }

    /**
     * @return the qtdDiasAcessoAlunoAtivo
     */
    public Integer getQtdDiasAcessoAlunoAtivo() {
        if (qtdDiasAcessoAlunoAtivo == null) {
            qtdDiasAcessoAlunoAtivo = 0;
        }
        return qtdDiasAcessoAlunoAtivo;
    }

    /**
     * @param qtdDiasAcessoAlunoAtivo the qtdDiasAcessoAlunoAtivo to set
     */
    public void setQtdDiasAcessoAlunoAtivo(Integer qtdDiasAcessoAlunoAtivo) {
        this.qtdDiasAcessoAlunoAtivo = qtdDiasAcessoAlunoAtivo;
    }

    /**
     * @return the matricularAlunoReprovFaltaEmDisciplinaJaRealizadaPosGraduacaoExtensao
     */
    public Boolean getMatricularAlunoReprovFaltaEmDisciplinaJaRealizadaPosGraduacaoExtensao() {
        if (matricularAlunoReprovFaltaEmDisciplinaJaRealizadaPosGraduacaoExtensao == null) {
            matricularAlunoReprovFaltaEmDisciplinaJaRealizadaPosGraduacaoExtensao = Boolean.FALSE;
        }
        return matricularAlunoReprovFaltaEmDisciplinaJaRealizadaPosGraduacaoExtensao;
    }

    /**
     * @param matricularAlunoReprovFaltaEmDisciplinaJaRealizadaPosGraduacaoExtensao the matricularAlunoReprovFaltaEmDisciplinaJaRealizadaPosGraduacaoExtensao to set
     */
    public void setMatricularAlunoReprovFaltaEmDisciplinaJaRealizadaPosGraduacaoExtensao(Boolean matricularAlunoReprovFaltaEmDisciplinaJaRealizadaPosGraduacaoExtensao) {
        this.matricularAlunoReprovFaltaEmDisciplinaJaRealizadaPosGraduacaoExtensao = matricularAlunoReprovFaltaEmDisciplinaJaRealizadaPosGraduacaoExtensao;
    }

    /**
     * @return the funcionarioRespAlteracaoDados
     */
    public FuncionarioVO getFuncionarioRespAlteracaoDados() {
        if (funcionarioRespAlteracaoDados == null) {
            funcionarioRespAlteracaoDados = new FuncionarioVO();
        }
        return funcionarioRespAlteracaoDados;
    }

    /**
     * @param funcionarioRespAlteracaoDados the funcionarioRespAlteracaoDados to set
     */
    public void setFuncionarioRespAlteracaoDados(FuncionarioVO funcionarioRespAlteracaoDados) {
        this.funcionarioRespAlteracaoDados = funcionarioRespAlteracaoDados;
    }

	public Integer getNrDiasLimiteEntregaDocumento() {
		if(nrDiasLimiteEntregaDocumento == null){
			nrDiasLimiteEntregaDocumento = 30;
		}
		return nrDiasLimiteEntregaDocumento;
	}

	public void setNrDiasLimiteEntregaDocumento(Integer nrDiasLimiteEntregaDocumento) {
		this.nrDiasLimiteEntregaDocumento = nrDiasLimiteEntregaDocumento;
	}

    /**
     * @return the grupoDestinatarioMapaLocalAula
     */
    public GrupoDestinatariosVO getGrupoDestinatarioMapaLocalAula() {
        if (grupoDestinatarioMapaLocalAula == null) {
            grupoDestinatarioMapaLocalAula = new GrupoDestinatariosVO();
        }
        return grupoDestinatarioMapaLocalAula;
    }

    /**
     * @param grupoDestinatarioMapaLocalAula the grupoDestinatarioMapaLocalAula to set
     */
    public void setGrupoDestinatarioMapaLocalAula(GrupoDestinatariosVO grupoDestinatarioMapaLocalAula) {
        this.grupoDestinatarioMapaLocalAula = grupoDestinatarioMapaLocalAula;
    }

    /**
     * @return the todosCamposObrigatoriosPreInscricao
     */
    public Boolean getTodosCamposObrigatoriosPreInscricao() {
        if (todosCamposObrigatoriosPreInscricao == null) {
            todosCamposObrigatoriosPreInscricao = Boolean.FALSE;
        }
        return todosCamposObrigatoriosPreInscricao;
    }

    /**
     * @param todosCamposObrigatoriosPreInscricao the todosCamposObrigatoriosPreInscricao to set
     */
    public void setTodosCamposObrigatoriosPreInscricao(Boolean todosCamposObrigatoriosPreInscricao) {
        this.todosCamposObrigatoriosPreInscricao = todosCamposObrigatoriosPreInscricao;
    }

    /**
     * @return the maeFiliacao
     */
    public Boolean getMaeFiliacao() {
        if (maeFiliacao == null) {
            maeFiliacao = Boolean.FALSE;
        }
        return maeFiliacao;
    }

    /**
     * @param maeFiliacao the maeFiliacao to set
     */
    public void setMaeFiliacao(Boolean maeFiliacao) {
        this.maeFiliacao = maeFiliacao;
    }

    /**
     * @return the notificarAlunoAniversariante
     */
    public Boolean getNotificarAlunoAniversariante() {
        if (notificarAlunoAniversariante == null) {
            notificarAlunoAniversariante = Boolean.FALSE;
        }
        return notificarAlunoAniversariante;
    }

    /**
     * @param notificarAlunoAniversariante the notificarAlunoAniversariante to set
     */
    public void setNotificarAlunoAniversariante(Boolean notificarAlunoAniversariante) {
        this.notificarAlunoAniversariante = notificarAlunoAniversariante;
    }

    public Boolean getNotificarExAlunoAniversariante() {
        if (notificarExAlunoAniversariante == null) {
            notificarExAlunoAniversariante = Boolean.FALSE;
        }
        return notificarExAlunoAniversariante;
    }

    public void setNotificarExAlunoAniversariante(Boolean notificarExAlunoAniversariante) {
        this.notificarExAlunoAniversariante = notificarExAlunoAniversariante;
    }

    /**
     * @return the notificarProfessorAniversariante
     */
    public Boolean getNotificarProfessorAniversariante() {
        if (notificarProfessorAniversariante == null) {
            notificarProfessorAniversariante = Boolean.FALSE;
        }
        return notificarProfessorAniversariante;
    }

    /**
     * @param notificarProfessorAniversariante the notificarProfessorAniversariante to set
     */
    public void setNotificarProfessorAniversariante(Boolean notificarProfessorAniversariante) {
        this.notificarProfessorAniversariante = notificarProfessorAniversariante;
    }

    /**
     * @return the notificarFuncionarioAniversariante
     */
    public Boolean getNotificarFuncionarioAniversariante() {
        if (notificarFuncionarioAniversariante == null) {
            notificarFuncionarioAniversariante = Boolean.FALSE;
        }
        return notificarFuncionarioAniversariante;
    }

    /**
     * @param notificarFuncionarioAniversariante the notificarFuncionarioAniversariante to set
     */
    public void setNotificarFuncionarioAniversariante(Boolean notificarFuncionarioAniversariante) {
        this.notificarFuncionarioAniversariante = notificarFuncionarioAniversariante;
    }

    /**
     * @return the notificarPaiAniversariante
     */
    public Boolean getNotificarPaiAniversariante() {
        if (notificarPaiAniversariante == null) {
            notificarPaiAniversariante = Boolean.FALSE;
        }
        return notificarPaiAniversariante;
    }

    /**
     * @param notificarPaiAniversariante the notificarPaiAniversariante to set
     */
    public void setNotificarPaiAniversariante(Boolean notificarPaiAniversariante) {
        this.notificarPaiAniversariante = notificarPaiAniversariante;
    }

    /**
     * @return the controlaAprovacaoDocEntregue
     */
    public Boolean getControlaAprovacaoDocEntregue() {
        if (controlaAprovacaoDocEntregue == null) {
            controlaAprovacaoDocEntregue = Boolean.FALSE;
        }
        return controlaAprovacaoDocEntregue;
    }

    /**
     * @param controlaAprovacaoDocEntregue the controlaAprovacaoDocEntregue to set
     */
    public void setControlaAprovacaoDocEntregue(Boolean controlaAprovacaoDocEntregue) {
        this.controlaAprovacaoDocEntregue = controlaAprovacaoDocEntregue;
    }

    /**
     * @return the obrigarTipoMidiaProspect
     */
    public Boolean getObrigarTipoMidiaProspect() {
        if (obrigarTipoMidiaProspect == null) {
            obrigarTipoMidiaProspect = Boolean.FALSE;
        }
        return obrigarTipoMidiaProspect;
    }

    /**
     * @param obrigarTipoMidiaProspect the obrigarTipoMidiaProspect to set
     */
    public void setObrigarTipoMidiaProspect(Boolean obrigarTipoMidiaProspect) {
        this.obrigarTipoMidiaProspect = obrigarTipoMidiaProspect;
    }

//	public Boolean getConsiderarRankingCrmSomenteMatriculAtivo() {
//		if(considerarRankingCrmSomenteMatriculAtivo == null){
//			considerarRankingCrmSomenteMatriculAtivo = true;
//		}
//		return considerarRankingCrmSomenteMatriculAtivo;
//	}
//
//	public void setConsiderarRankingCrmSomenteMatriculAtivo(Boolean considerarRankingCrmSomenteMatriculAtivo) {
//		this.considerarRankingCrmSomenteMatriculAtivo = considerarRankingCrmSomenteMatriculAtivo;
//	}
//
//	public Boolean getDesconsiderarRankingCrmAlunoBolsista() {
//		if(desconsiderarRankingCrmAlunoBolsista == null){
//			desconsiderarRankingCrmAlunoBolsista = true;
//		}
//		return desconsiderarRankingCrmAlunoBolsista;
//	}
//
//	public void setDesconsiderarRankingCrmAlunoBolsista(Boolean desconsiderarRankingCrmAlunoBolsista) {
//		this.desconsiderarRankingCrmAlunoBolsista = desconsiderarRankingCrmAlunoBolsista;
//	}
//
//	public Boolean getConsiderarRankingCrmPrimeiraMensalidade() {
//		if(considerarRankingCrmPrimeiraMensalidade == null){
//			considerarRankingCrmPrimeiraMensalidade = true;
//		}
//		return considerarRankingCrmPrimeiraMensalidade;
//	}
//
//	public void setConsiderarRankingCrmPrimeiraMensalidade(Boolean considerarRankingCrmPrimeiraMensalidade) {
//		this.considerarRankingCrmPrimeiraMensalidade = considerarRankingCrmPrimeiraMensalidade;
//	}
//
//	public Boolean getConsiderarContratoAssinadoRankingCrm() {
//		if(considerarContratoAssinadoRankingCrm == null){
//			considerarContratoAssinadoRankingCrm = true;
//		}
//		return considerarContratoAssinadoRankingCrm;
//	}
//
//	public void setConsiderarContratoAssinadoRankingCrm(Boolean considerarContratoAssinadoRankingCrm) {
//		this.considerarContratoAssinadoRankingCrm = considerarContratoAssinadoRankingCrm;
//	}
//
//	public Integer getQtdeMatriculaConsultorPorTurmaConsiderarRankingCrm() {
//		if(qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm == null){
//			qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm = 2;
//		}
//		return qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm;
//	}
//
//	public void setQtdeMatriculaConsultorPorTurmaConsiderarRankingCrm(Integer qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm) {
//		this.qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm = qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm;
//	}
//
//	public Integer getDesconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm() {
//		if(desconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm == null){
//			desconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm = 4;
//		}
//		return desconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm;
//	}
//
//	public void setDesconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm(Integer desconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm) {
//		this.desconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm = desconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm;
//	}
//
//	public Boolean getConsiderarAlunoAdimplenteSemContratoAssinadoRankingCrm() {
//		if(considerarAlunoAdimplenteSemContratoAssinadoRankingCrm == null){
//			considerarAlunoAdimplenteSemContratoAssinadoRankingCrm = true;
//		}
//		return considerarAlunoAdimplenteSemContratoAssinadoRankingCrm;
//	}
//
//	public void setConsiderarAlunoAdimplenteSemContratoAssinadoRankingCrm(Boolean considerarAlunoAdimplenteSemContratoAssinadoRankingCrm) {
//		this.considerarAlunoAdimplenteSemContratoAssinadoRankingCrm = considerarAlunoAdimplenteSemContratoAssinadoRankingCrm;
//	}
//
//	public Integer getQtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm() {
//		if(qtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm == null){
//			qtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm = 2;
//		}
//		return qtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm;
//	}
//
//	public void setQtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm(Integer qtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm) {
//		this.qtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm = qtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm;
//	}

	public String getUrlConfirmacaoPreInscricao() {
		if(urlConfirmacaoPreInscricao == null){
			urlConfirmacaoPreInscricao = "";
		}
		return urlConfirmacaoPreInscricao;
	}

	public void setUrlConfirmacaoPreInscricao(String urlConfirmacaoPreInscricao) {
		this.urlConfirmacaoPreInscricao = urlConfirmacaoPreInscricao;
	}

//	public String getDescricaoRegraComissionamentoCRM() {
//		if(descricaoRegraComissionamentoCRM == null){
//			descricaoRegraComissionamentoCRM = "";
//		}
//		return descricaoRegraComissionamentoCRM;
//	}
//
//	public void setDescricaoRegraComissionamentoCRM(String descricaoRegraComissionamentoCRM) {
//		this.descricaoRegraComissionamentoCRM = descricaoRegraComissionamentoCRM;
//	}
//
	public Integer getQtdDiasMaximoAntecedenciaRemarcarAulaReposicao() {
		if (qtdDiasMaximoAntecedenciaRemarcarAulaReposicao == null) {
			qtdDiasMaximoAntecedenciaRemarcarAulaReposicao = 0;
		}
		return qtdDiasMaximoAntecedenciaRemarcarAulaReposicao;
	}

	public void setQtdDiasMaximoAntecedenciaRemarcarAulaReposicao(Integer qtdDiasMaximoAntecedenciaRemarcarAulaReposicao) {
		this.qtdDiasMaximoAntecedenciaRemarcarAulaReposicao = qtdDiasMaximoAntecedenciaRemarcarAulaReposicao;
	}

	public Boolean getCalcularMediaAoGravar() {
		if (calcularMediaAoGravar == null) {
			calcularMediaAoGravar = Boolean.FALSE;
		}
		return calcularMediaAoGravar;
	}

	public void setCalcularMediaAoGravar(Boolean calcularMediaAoGravar) {
		this.calcularMediaAoGravar = calcularMediaAoGravar;
	}

	public Boolean getPermitiAlunoPreMatriculaSolicitarRequerimento() {
		if (permitiAlunoPreMatriculaSolicitarRequerimento == null) {
			permitiAlunoPreMatriculaSolicitarRequerimento = Boolean.FALSE;
		}
		return permitiAlunoPreMatriculaSolicitarRequerimento;
	}

	public void setPermitiAlunoPreMatriculaSolicitarRequerimento(Boolean permitiAlunoPreMatriculaSolicitarRequerimento) {
		this.permitiAlunoPreMatriculaSolicitarRequerimento = permitiAlunoPreMatriculaSolicitarRequerimento;
	}

	public String getEmailRemetente() {
        if (emailRemetente == null) {
        	emailRemetente = "";
        }
		return emailRemetente;
	}

	public void setEmailRemetente(String emailRemetente) {
		this.emailRemetente = emailRemetente;
	}

	public Integer getQtdeDiasNotificacaoDataProva() {
		if(qtdeDiasNotificacaoDataProva == null) {
			qtdeDiasNotificacaoDataProva = 0;
		}
		return qtdeDiasNotificacaoDataProva;
	}

	public void setQtdeDiasNotificacaoDataProva(Integer qtdeDiasNotificacaoDataProva) {
		this.qtdeDiasNotificacaoDataProva = qtdeDiasNotificacaoDataProva;
	}

	public Boolean getValidarQtdAlunoLocalSalaAulaInclusaoReposicaoDisciplina() {
		if (validarQtdAlunoLocalSalaAulaInclusaoReposicaoDisciplina == null) {
			validarQtdAlunoLocalSalaAulaInclusaoReposicaoDisciplina = Boolean.FALSE;
		}
		return validarQtdAlunoLocalSalaAulaInclusaoReposicaoDisciplina;
	}

	public void setValidarQtdAlunoLocalSalaAulaInclusaoReposicaoDisciplina(Boolean validarQtdAlunoLocalSalaAulaInclusaoReposicaoDisciplina) {
		this.validarQtdAlunoLocalSalaAulaInclusaoReposicaoDisciplina = validarQtdAlunoLocalSalaAulaInclusaoReposicaoDisciplina;
	}

	public Integer getQtdeDiaNotificarAbandonoCurso() {
		if(qtdeDiaNotificarAbandonoCurso == null){
			qtdeDiaNotificarAbandonoCurso = 0;
		}
		return qtdeDiaNotificarAbandonoCurso;
	}

	public void setQtdeDiaNotificarAbandonoCurso(Integer qtdeDiaNotificarAbandonoCurso) {
		this.qtdeDiaNotificarAbandonoCurso = qtdeDiaNotificarAbandonoCurso;
	}

	public Integer getQtdeDiaRegistrarAbandonoCurso() {
		if(qtdeDiaRegistrarAbandonoCurso == null){
			qtdeDiaRegistrarAbandonoCurso = 0;
		}
		return qtdeDiaRegistrarAbandonoCurso;
	}

	public void setQtdeDiaRegistrarAbandonoCurso(Integer qtdeDiaRegistrarAbandonoCurso) {
		this.qtdeDiaRegistrarAbandonoCurso = qtdeDiaRegistrarAbandonoCurso;
	}

	public MotivoCancelamentoTrancamentoVO getMotivoPadraoAbandonoCurso() {
		if(motivoPadraoAbandonoCurso == null){
			motivoPadraoAbandonoCurso = new MotivoCancelamentoTrancamentoVO();
		}
		return motivoPadraoAbandonoCurso;
	}

	public void setMotivoPadraoAbandonoCurso(MotivoCancelamentoTrancamentoVO motivoPadraoAbandonoCurso) {
		this.motivoPadraoAbandonoCurso = motivoPadraoAbandonoCurso;
	}

	public Boolean getOcultarEmailPreInscricao() {
		if (ocultarEmailPreInscricao == null) {
			ocultarEmailPreInscricao = Boolean.FALSE;
		}
		return ocultarEmailPreInscricao;
	}

	public void setOcultarEmailPreInscricao(Boolean ocultarEmailPreInscricao) {
		this.ocultarEmailPreInscricao = ocultarEmailPreInscricao;
	}

	public Boolean getOcultarRG() {
		if (ocultarRG == null) {
			ocultarRG = Boolean.FALSE;
		}
		return ocultarRG;
	}

	public void setOcultarRG(Boolean ocultarRG) {
		this.ocultarRG = ocultarRG;
	}

	public Boolean getOcultarSexo() {
		if (ocultarSexo == null) {
			ocultarSexo = Boolean.FALSE;
		}
		return ocultarSexo;
	}

	public void setOcultarSexo(Boolean ocultarSexo) {
		this.ocultarSexo = ocultarSexo;
	}

	public Boolean getOcultarTelefone() {
		if (ocultarTelefone == null) {
			ocultarTelefone = Boolean.FALSE;
		}
		return ocultarTelefone;
	}

	public void setOcultarTelefone(Boolean ocultarTelefone) {
		this.ocultarTelefone = ocultarTelefone;
	}

	public Boolean getOcultarEstadoCivil() {
		if (ocultarEstadoCivil == null) {
			ocultarEstadoCivil = Boolean.FALSE;
		}
		return ocultarEstadoCivil;
	}

	public void setOcultarEstadoCivil(Boolean ocultarEstadoCivil) {
		this.ocultarEstadoCivil = ocultarEstadoCivil;
	}

	public Boolean getOcultarNaturalidade() {
		if (ocultarNaturalidade == null) {
			ocultarNaturalidade = Boolean.FALSE;
		}
		return ocultarNaturalidade;
	}

	public void setOcultarNaturalidade(Boolean ocultarNaturalidade) {
		this.ocultarNaturalidade = ocultarNaturalidade;
	}

	public Boolean getOcultarFormacaoAcademica() {
		if (ocultarFormacaoAcademica == null) {
			ocultarFormacaoAcademica = Boolean.FALSE;
		}
		return ocultarFormacaoAcademica;
	}

	public void setOcultarFormacaoAcademica(Boolean ocultarFormacaoAcademica) {
		this.ocultarFormacaoAcademica = ocultarFormacaoAcademica;
	}

	public Boolean getOcultarDataNasc() {
		if (ocultarDataNasc == null) {
			ocultarDataNasc = Boolean.FALSE;
		}
		return ocultarDataNasc;
	}

	public void setOcultarDataNasc(Boolean ocultarDataNasc) {
		this.ocultarDataNasc = ocultarDataNasc;
	}
//
//	public Boolean getDesconsiderarMatriculaContratoNaoAssinado4Meses() {
//		if (desconsiderarMatriculaContratoNaoAssinado4Meses == null) {
//			desconsiderarMatriculaContratoNaoAssinado4Meses = Boolean.FALSE;
//		}
//		return desconsiderarMatriculaContratoNaoAssinado4Meses;
//	}
//
//	public void setDesconsiderarMatriculaContratoNaoAssinado4Meses(Boolean desconsiderarMatriculaContratoNaoAssinado4Meses) {
//		this.desconsiderarMatriculaContratoNaoAssinado4Meses = desconsiderarMatriculaContratoNaoAssinado4Meses;
//	}
//
//	public Boolean getDesconsiderarParcelaEFaltaApartir3Meses() {
//		if (desconsiderarParcelaEFaltaApartir3Meses == null) {
//			desconsiderarParcelaEFaltaApartir3Meses = Boolean.FALSE;
//		}
//		return desconsiderarParcelaEFaltaApartir3Meses;
//	}
//
//	public void setDesconsiderarParcelaEFaltaApartir3Meses(Boolean desconsiderarParcelaEFaltaApartir3Meses) {
//		this.desconsiderarParcelaEFaltaApartir3Meses = desconsiderarParcelaEFaltaApartir3Meses;
//	}

	public Integer getDiasParaNotificarCoordenadorInicioTurma() {
		if(diasParaNotificarCoordenadorInicioTurma == null){
			diasParaNotificarCoordenadorInicioTurma = 0;
		}
		return diasParaNotificarCoordenadorInicioTurma;
	}

	public void setDiasParaNotificarCoordenadorInicioTurma(Integer diasParaNotificarCoordenadorInicioTurma) {
		this.diasParaNotificarCoordenadorInicioTurma = diasParaNotificarCoordenadorInicioTurma;
	}
	
	public Boolean getBloquearMatriculaPosSemGraduacao() {
		if (bloquearMatriculaPosSemGraduacao == null) {
			bloquearMatriculaPosSemGraduacao = Boolean.FALSE;
		}
		return bloquearMatriculaPosSemGraduacao;
	}

	public void setBloquearMatriculaPosSemGraduacao(Boolean bloquearMatriculaPosSemGraduacao) {
		this.bloquearMatriculaPosSemGraduacao = bloquearMatriculaPosSemGraduacao;
	}

	public Integer getQtdEmailEnvio() {
		if (qtdEmailEnvio == null) {
			qtdEmailEnvio = 50;
		}
		return qtdEmailEnvio;
	}

	public void setQtdEmailEnvio(Integer qtdEmailEnvio) {
		this.qtdEmailEnvio = qtdEmailEnvio;
	}

	public Boolean getBloquearRealizarTrancamentoComEmprestimoBiblioteca() {
		if (bloquearRealizarTrancamentoComEmprestimoBiblioteca == null) {
			bloquearRealizarTrancamentoComEmprestimoBiblioteca = true;
		}
		return bloquearRealizarTrancamentoComEmprestimoBiblioteca;
	}

	public void setBloquearRealizarTrancamentoComEmprestimoBiblioteca(Boolean bloquearRealizarTrancamentoComEmprestimoBiblioteca) {
		this.bloquearRealizarTrancamentoComEmprestimoBiblioteca = bloquearRealizarTrancamentoComEmprestimoBiblioteca;
	}

	public Boolean getBloquearRealizarAbandonoCursoComEmprestimoBiblioteca() {
		if (bloquearRealizarAbandonoCursoComEmprestimoBiblioteca == null) {
			bloquearRealizarAbandonoCursoComEmprestimoBiblioteca = true;
		}
		return bloquearRealizarAbandonoCursoComEmprestimoBiblioteca;
	}

	public void setBloquearRealizarAbandonoCursoComEmprestimoBiblioteca(Boolean bloquearRealizarAbandonoCursoComEmprestimoBiblioteca) {
		this.bloquearRealizarAbandonoCursoComEmprestimoBiblioteca = bloquearRealizarAbandonoCursoComEmprestimoBiblioteca;
	}

	public Boolean getBloquearRealizarCancelamentoComEmprestimoBiblioteca() {
		if (bloquearRealizarCancelamentoComEmprestimoBiblioteca == null) {
			bloquearRealizarCancelamentoComEmprestimoBiblioteca = true;
		}
		return bloquearRealizarCancelamentoComEmprestimoBiblioteca;
	}

	public void setBloquearRealizarCancelamentoComEmprestimoBiblioteca(Boolean bloquearRealizarCancelamentoComEmprestimoBiblioteca) {
		this.bloquearRealizarCancelamentoComEmprestimoBiblioteca = bloquearRealizarCancelamentoComEmprestimoBiblioteca;
	}

	public Boolean getBloquearRealizarTransferenciaSaidaComEmprestimoBiblioteca() {
		if (bloquearRealizarTransferenciaSaidaComEmprestimoBiblioteca == null) {
			bloquearRealizarTransferenciaSaidaComEmprestimoBiblioteca = true;
		}
		return bloquearRealizarTransferenciaSaidaComEmprestimoBiblioteca;
	}

	public void setBloquearRealizarTransferenciaSaidaComEmprestimoBiblioteca(Boolean bloquearRealizarTransferenciaSaidaComEmprestimoBiblioteca) {
		this.bloquearRealizarTransferenciaSaidaComEmprestimoBiblioteca = bloquearRealizarTransferenciaSaidaComEmprestimoBiblioteca;
	}

	public Boolean getBloquearRealizarTransferenciaInternaComEmprestimoBiblioteca() {
		if (bloquearRealizarTransferenciaInternaComEmprestimoBiblioteca == null) {
			bloquearRealizarTransferenciaInternaComEmprestimoBiblioteca = true;
		}
		return bloquearRealizarTransferenciaInternaComEmprestimoBiblioteca;
	}

	public void setBloquearRealizarTransferenciaInternaComEmprestimoBiblioteca(Boolean bloquearRealizarTransferenciaInternaComEmprestimoBiblioteca) {
		this.bloquearRealizarTransferenciaInternaComEmprestimoBiblioteca = bloquearRealizarTransferenciaInternaComEmprestimoBiblioteca;
	}

	public Boolean getBloquearRealizarRenovacaoComEmprestimoBiblioteca() {
		if (bloquearRealizarRenovacaoComEmprestimoBiblioteca == null) {
			bloquearRealizarRenovacaoComEmprestimoBiblioteca = true;
		}
		return bloquearRealizarRenovacaoComEmprestimoBiblioteca;
	}

	public void setBloquearRealizarRenovacaoComEmprestimoBiblioteca(Boolean bloquearRealizarRenovacaoComEmprestimoBiblioteca) {
		this.bloquearRealizarRenovacaoComEmprestimoBiblioteca = bloquearRealizarRenovacaoComEmprestimoBiblioteca;
	}

	public Boolean getVerificarAulaProgramadaIncluirDisciplina() {
		if (verificarAulaProgramadaIncluirDisciplina == null) {
			verificarAulaProgramadaIncluirDisciplina = false;
		}
		return verificarAulaProgramadaIncluirDisciplina;
	}

	public void setVerificarAulaProgramadaIncluirDisciplina(Boolean verificarAulaProgramadaIncluirDisciplina) {
		this.verificarAulaProgramadaIncluirDisciplina = verificarAulaProgramadaIncluirDisciplina;
	}

	public PerfilAcessoVO getPerfilPadraoProfessorExtensao() {
		if (perfilPadraoProfessorExtensao == null) {
			perfilPadraoProfessorExtensao = new PerfilAcessoVO();
		}
		return perfilPadraoProfessorExtensao;
	}

	public void setPerfilPadraoProfessorExtensao(PerfilAcessoVO perfilPadraoProfessorExtensao) {
		this.perfilPadraoProfessorExtensao = perfilPadraoProfessorExtensao;
	}

	public Boolean getDesconsiderarAlunoReposicaoVagasTurma() {
		if (desconsiderarAlunoReposicaoVagasTurma == null) {
			desconsiderarAlunoReposicaoVagasTurma = Boolean.FALSE;
		}
		return desconsiderarAlunoReposicaoVagasTurma;
	}

	public void setDesconsiderarAlunoReposicaoVagasTurma(Boolean desconsiderarAlunoReposicaoVagasTurma) {
		this.desconsiderarAlunoReposicaoVagasTurma = desconsiderarAlunoReposicaoVagasTurma;
	}

	/**
	 * @return the apresentarAlunoPendenteFinanceiroVisaoProfessor
	 */
	public Boolean getApresentarAlunoPendenteFinanceiroVisaoProfessor() {
		if (apresentarAlunoPendenteFinanceiroVisaoProfessor == null) {
			apresentarAlunoPendenteFinanceiroVisaoProfessor = true;
		}
		return apresentarAlunoPendenteFinanceiroVisaoProfessor;
	}

	/**
	 * @param apresentarAlunoPendenteFinanceiroVisaoProfessor the apresentarAlunoPendenteFinanceiroVisaoProfessor to set
	 */
	public void setApresentarAlunoPendenteFinanceiroVisaoProfessor(Boolean apresentarAlunoPendenteFinanceiroVisaoProfessor) {
		this.apresentarAlunoPendenteFinanceiroVisaoProfessor = apresentarAlunoPendenteFinanceiroVisaoProfessor;
	}

	/**
	 * @return the apresentarAlunoPendenteFinanceiroVisaoCoordenador
	 */
	public Boolean getApresentarAlunoPendenteFinanceiroVisaoCoordenador() {
		if (apresentarAlunoPendenteFinanceiroVisaoCoordenador == null) {
			apresentarAlunoPendenteFinanceiroVisaoCoordenador = true;
		}
		return apresentarAlunoPendenteFinanceiroVisaoCoordenador;
	}

	/**
	 * @param apresentarAlunoPendenteFinanceiroVisaoCoordenador the apresentarAlunoPendenteFinanceiroVisaoCoordenador to set
	 */
	public void setApresentarAlunoPendenteFinanceiroVisaoCoordenador(Boolean apresentarAlunoPendenteFinanceiroVisaoCoordenador) {
		this.apresentarAlunoPendenteFinanceiroVisaoCoordenador = apresentarAlunoPendenteFinanceiroVisaoCoordenador;
	}

	public String getIpServidorBiometria() {
		if (ipServidorBiometria == null) {
			ipServidorBiometria = "localhost";
		}
		return ipServidorBiometria;
	}

	public void setIpServidorBiometria(String ipServidorBiometria) {
		this.ipServidorBiometria = ipServidorBiometria;
	}

	public PerfilAcessoVO getPerfilPadraoAlunoEducacaoInfantil() {
		if (perfilPadraoAlunoEducacaoInfantil == null) {
			perfilPadraoAlunoEducacaoInfantil = new PerfilAcessoVO();
		}
		return perfilPadraoAlunoEducacaoInfantil;
	}

	public void setPerfilPadraoAlunoEducacaoInfantil(PerfilAcessoVO perfilPadraoAlunoEducacaoInfantil) {
		this.perfilPadraoAlunoEducacaoInfantil = perfilPadraoAlunoEducacaoInfantil;
	}

	public PerfilAcessoVO getPerfilPadraoAlunoEducacaoFundamental() {
		if (perfilPadraoAlunoEducacaoFundamental == null) {
			perfilPadraoAlunoEducacaoFundamental = new PerfilAcessoVO();
		}
		return perfilPadraoAlunoEducacaoFundamental;
	}

	public void setPerfilPadraoAlunoEducacaoFundamental(PerfilAcessoVO perfilPadraoAlunoEducacaoFundamental) {
		this.perfilPadraoAlunoEducacaoFundamental = perfilPadraoAlunoEducacaoFundamental;
	}

	public PerfilAcessoVO getPerfilPadraoAlunoEducacaoMedio() {
		if (perfilPadraoAlunoEducacaoMedio == null) {
			perfilPadraoAlunoEducacaoMedio = new PerfilAcessoVO();
		}
		return perfilPadraoAlunoEducacaoMedio;
	}

	public void setPerfilPadraoAlunoEducacaoMedio(PerfilAcessoVO perfilPadraoAlunoEducacaoMedio) {
		this.perfilPadraoAlunoEducacaoMedio = perfilPadraoAlunoEducacaoMedio;
	}

	public PerfilAcessoVO getPerfilPadraoAlunoEducacaoGraduacao() {
		if (perfilPadraoAlunoEducacaoGraduacao == null) {
			perfilPadraoAlunoEducacaoGraduacao = new PerfilAcessoVO();
		}
		return perfilPadraoAlunoEducacaoGraduacao;
	}

	public void setPerfilPadraoAlunoEducacaoGraduacao(PerfilAcessoVO perfilPadraoAlunoEducacaoGraduacao) {
		this.perfilPadraoAlunoEducacaoGraduacao = perfilPadraoAlunoEducacaoGraduacao;
	}

	public PerfilAcessoVO getPerfilPadraoAlunoEducacaoGraduacaoTecnologica() {
		if (perfilPadraoAlunoEducacaoGraduacaoTecnologica == null) {
			perfilPadraoAlunoEducacaoGraduacaoTecnologica = new PerfilAcessoVO();
		}
		return perfilPadraoAlunoEducacaoGraduacaoTecnologica;
	}

	public void setPerfilPadraoAlunoEducacaoGraduacaoTecnologica(PerfilAcessoVO perfilPadraoAlunoEducacaoGraduacaoTecnologica) {
		this.perfilPadraoAlunoEducacaoGraduacaoTecnologica = perfilPadraoAlunoEducacaoGraduacaoTecnologica;
	}

	public PerfilAcessoVO getPerfilPadraoAlunoEducacaoSequencial() {
		if (perfilPadraoAlunoEducacaoSequencial == null) {
			perfilPadraoAlunoEducacaoSequencial = new PerfilAcessoVO();
		}
		return perfilPadraoAlunoEducacaoSequencial;
	}

	public void setPerfilPadraoAlunoEducacaoSequencial(PerfilAcessoVO perfilPadraoAlunoEducacaoSequencial) {
		this.perfilPadraoAlunoEducacaoSequencial = perfilPadraoAlunoEducacaoSequencial;
	}

	public PerfilAcessoVO getPerfilPadraoAlunoEducacaoExtensao() {
		if (perfilPadraoAlunoEducacaoExtensao == null) {
			perfilPadraoAlunoEducacaoExtensao = new PerfilAcessoVO();
		}
		return perfilPadraoAlunoEducacaoExtensao;
	}

	public void setPerfilPadraoAlunoEducacaoExtensao(PerfilAcessoVO perfilPadraoAlunoEducacaoExtensao) {
		this.perfilPadraoAlunoEducacaoExtensao = perfilPadraoAlunoEducacaoExtensao;
	}

	public PerfilAcessoVO getPerfilPadraoAlunoEducacaoPosGraduacao() {
		if (perfilPadraoAlunoEducacaoPosGraduacao == null) {
			perfilPadraoAlunoEducacaoPosGraduacao = new PerfilAcessoVO();
		}
		return perfilPadraoAlunoEducacaoPosGraduacao;
	}

	public void setPerfilPadraoAlunoEducacaoPosGraduacao(PerfilAcessoVO perfilPadraoAlunoEducacaoPosGraduacao) {
		this.perfilPadraoAlunoEducacaoPosGraduacao = perfilPadraoAlunoEducacaoPosGraduacao;
	}

	public PerfilAcessoVO getPerfilPadraoAlunoEducacaoTecnicoProfissionalizante() {
		if (perfilPadraoAlunoEducacaoTecnicoProfissionalizante == null) {
			perfilPadraoAlunoEducacaoTecnicoProfissionalizante = new PerfilAcessoVO();
		}
		return perfilPadraoAlunoEducacaoTecnicoProfissionalizante;
	}

	public void setPerfilPadraoAlunoEducacaoTecnicoProfissionalizante(PerfilAcessoVO perfilPadraoAlunoEducacaoTecnicoProfissionalizante) {
		this.perfilPadraoAlunoEducacaoTecnicoProfissionalizante = perfilPadraoAlunoEducacaoTecnicoProfissionalizante;
	}

	public PerfilAcessoVO getPerfilPadraoPaisEducacaoInfantil() {
		if (perfilPadraoPaisEducacaoInfantil == null) {
			perfilPadraoPaisEducacaoInfantil = new PerfilAcessoVO();
		}
		return perfilPadraoPaisEducacaoInfantil;
	}

	public void setPerfilPadraoPaisEducacaoInfantil(PerfilAcessoVO perfilPadraoPaisEducacaoInfantil) {
		this.perfilPadraoPaisEducacaoInfantil = perfilPadraoPaisEducacaoInfantil;
	}

	public PerfilAcessoVO getPerfilPadraoPaisEducacaoFundamental() {
		if (perfilPadraoPaisEducacaoFundamental == null) {
			perfilPadraoPaisEducacaoFundamental = new PerfilAcessoVO();
		}
		return perfilPadraoPaisEducacaoFundamental;
	}

	public void setPerfilPadraoPaisEducacaoFundamental(PerfilAcessoVO perfilPadraoPaisEducacaoFundamental) {
		this.perfilPadraoPaisEducacaoFundamental = perfilPadraoPaisEducacaoFundamental;
	}

	public PerfilAcessoVO getPerfilPadraoPaisEducacaoMedio() {
		if (perfilPadraoPaisEducacaoMedio == null) {
			perfilPadraoPaisEducacaoMedio = new PerfilAcessoVO();
		}
		return perfilPadraoPaisEducacaoMedio;
	}

	public void setPerfilPadraoPaisEducacaoMedio(PerfilAcessoVO perfilPadraoPaisEducacaoMedio) {
		this.perfilPadraoPaisEducacaoMedio = perfilPadraoPaisEducacaoMedio;
	}

	public PerfilAcessoVO getPerfilPadraoPaisEducacaoGraduacao() {
		if (perfilPadraoPaisEducacaoGraduacao == null) {
			perfilPadraoPaisEducacaoGraduacao = new PerfilAcessoVO();
		}
		return perfilPadraoPaisEducacaoGraduacao;
	}

	public void setPerfilPadraoPaisEducacaoGraduacao(PerfilAcessoVO perfilPadraoPaisEducacaoGraduacao) {
		this.perfilPadraoPaisEducacaoGraduacao = perfilPadraoPaisEducacaoGraduacao;
	}

	public PerfilAcessoVO getPerfilPadraoPaisEducacaoGraduacaoTecnologica() {
		if (perfilPadraoPaisEducacaoGraduacaoTecnologica == null) {
			perfilPadraoPaisEducacaoGraduacaoTecnologica = new PerfilAcessoVO();
		}
		return perfilPadraoPaisEducacaoGraduacaoTecnologica;
	}

	public void setPerfilPadraoPaisEducacaoGraduacaoTecnologica(PerfilAcessoVO perfilPadraoPaisEducacaoGraduacaoTecnologica) {
		this.perfilPadraoPaisEducacaoGraduacaoTecnologica = perfilPadraoPaisEducacaoGraduacaoTecnologica;
	}

	public PerfilAcessoVO getPerfilPadraoPaisEducacaoSequencial() {
		if (perfilPadraoPaisEducacaoSequencial == null) {
			perfilPadraoPaisEducacaoSequencial = new PerfilAcessoVO();
		}
		return perfilPadraoPaisEducacaoSequencial;
	}

	public void setPerfilPadraoPaisEducacaoSequencial(PerfilAcessoVO perfilPadraoPaisEducacaoSequencial) {
		this.perfilPadraoPaisEducacaoSequencial = perfilPadraoPaisEducacaoSequencial;
	}

	public PerfilAcessoVO getPerfilPadraoPaisEducacaoExtensao() {
		if (perfilPadraoPaisEducacaoExtensao == null) {
			perfilPadraoPaisEducacaoExtensao = new PerfilAcessoVO();
		}
		return perfilPadraoPaisEducacaoExtensao;
	}

	public void setPerfilPadraoPaisEducacaoExtensao(PerfilAcessoVO perfilPadraoPaisEducacaoExtensao) {
		this.perfilPadraoPaisEducacaoExtensao = perfilPadraoPaisEducacaoExtensao;
	}

	public PerfilAcessoVO getPerfilPadraoPaisEducacaoPosGraduacao() {
		if (perfilPadraoPaisEducacaoPosGraduacao == null) {
			perfilPadraoPaisEducacaoPosGraduacao = new PerfilAcessoVO();
		}
		return perfilPadraoPaisEducacaoPosGraduacao;
	}

	public void setPerfilPadraoPaisEducacaoPosGraduacao(PerfilAcessoVO perfilPadraoPaisEducacaoPosGraduacao) {
		this.perfilPadraoPaisEducacaoPosGraduacao = perfilPadraoPaisEducacaoPosGraduacao;
	}

	public PerfilAcessoVO getPerfilPadraoPaisEducacaoTecnicoProfissionalizante() {
		if (perfilPadraoPaisEducacaoTecnicoProfissionalizante == null) {
			perfilPadraoPaisEducacaoTecnicoProfissionalizante = new PerfilAcessoVO();
		}
		return perfilPadraoPaisEducacaoTecnicoProfissionalizante;
	}

	public void setPerfilPadraoPaisEducacaoTecnicoProfissionalizante(PerfilAcessoVO perfilPadraoPaisEducacaoTecnicoProfissionalizante) {
		this.perfilPadraoPaisEducacaoTecnicoProfissionalizante = perfilPadraoPaisEducacaoTecnicoProfissionalizante;
	}
	
	public boolean getIsClienteUNIRV() {
		return getIdAutenticacaoServOtimize().equals("UNIRV");
	}

	/**
	 * @return the controlarNumeroAulaProgramadoProfessorPorDia
	 */
	public Boolean getControlarNumeroAulaProgramadoProfessorPorDia() {
		if (controlarNumeroAulaProgramadoProfessorPorDia == null) {
			controlarNumeroAulaProgramadoProfessorPorDia = false;
		}
		return controlarNumeroAulaProgramadoProfessorPorDia;
	}

	/**
	 * @param controlarNumeroAulaProgramadoProfessorPorDia the controlarNumeroAulaProgramadoProfessorPorDia to set
	 */
	public void setControlarNumeroAulaProgramadoProfessorPorDia(Boolean controlarNumeroAulaProgramadoProfessorPorDia) {
		this.controlarNumeroAulaProgramadoProfessorPorDia = controlarNumeroAulaProgramadoProfessorPorDia;
	}

	/**
	 * @return the quantidadeAulaMaximaProgramarProfessorDia
	 */
	public Integer getQuantidadeAulaMaximaProgramarProfessorDia() {
		if (quantidadeAulaMaximaProgramarProfessorDia == null) {
			quantidadeAulaMaximaProgramarProfessorDia = 0;
		}
		return quantidadeAulaMaximaProgramarProfessorDia;
	}

	/**
	 * @param quantidadeAulaMaximaProgramarProfessorDia the quantidadeAulaMaximaProgramarProfessorDia to set
	 */
	public void setQuantidadeAulaMaximaProgramarProfessorDia(Integer quantidadeAulaMaximaProgramarProfessorDia) {
		this.quantidadeAulaMaximaProgramarProfessorDia = quantidadeAulaMaximaProgramarProfessorDia;
	}
	
	/**
	 * @author Victor Hugo de Paula Costa 22/09/2015
	 */
	private PessoaVO usuarioResponsavelOperacoesExternas;

	public PessoaVO getUsuarioResponsavelOperacoesExternas() {
		if(usuarioResponsavelOperacoesExternas == null) {
			usuarioResponsavelOperacoesExternas = new PessoaVO();
		}
		return usuarioResponsavelOperacoesExternas;
	}

	public void setUsuarioResponsavelOperacoesExternas(PessoaVO usuarioResponsavelOperacoesExternas) {
		this.usuarioResponsavelOperacoesExternas = usuarioResponsavelOperacoesExternas;
	}
	
	public Boolean getIncrementarNumeroExemplarPorBiblioteca() {
		if (incrementarNumeroExemplarPorBiblioteca == null) {
			incrementarNumeroExemplarPorBiblioteca = Boolean.FALSE;
		}
		return incrementarNumeroExemplarPorBiblioteca;
	}

	public void setIncrementarNumeroExemplarPorBiblioteca(Boolean incrementarNumeroExemplarPorBiblioteca) {
		this.incrementarNumeroExemplarPorBiblioteca = incrementarNumeroExemplarPorBiblioteca;
	}

	public Boolean getZerarNumeroRegistroPorCurso() {
		if (zerarNumeroRegistroPorCurso == null) {
			zerarNumeroRegistroPorCurso = Boolean.TRUE;
		}
		return zerarNumeroRegistroPorCurso;
	}

	public void setZerarNumeroRegistroPorCurso(Boolean zerarNumeroRegistroPorCurso) {
		this.zerarNumeroRegistroPorCurso = zerarNumeroRegistroPorCurso;
	}
	
	public FornecedorSmsEnum getFornecedorSMSEnum() {
		if (fornecedorSMSEnum == null)
			fornecedorSMSEnum = FornecedorSmsEnum.FACILITASMS;
		return fornecedorSMSEnum;
	}
	
	public void setFornecedorSMSEnum(FornecedorSmsEnum fornecedorSMSEnum) {
		this.fornecedorSMSEnum = fornecedorSMSEnum;
	}
	

	public Boolean getCriarProspectFiliacao() {
		if (criarProspectFiliacao == null) {
			criarProspectFiliacao = Boolean.FALSE;
		}
		return criarProspectFiliacao;
	}

	public void setCriarProspectFiliacao(Boolean criarProspectFiliacao) {
		this.criarProspectFiliacao = criarProspectFiliacao;
	}

	public Boolean getCriarProspectAluno() {
		if (criarProspectAluno == null) {
			criarProspectAluno = Boolean.FALSE;
		}
		return criarProspectAluno;
	}

	public void setCriarProspectAluno(Boolean criarProspectAluno) {
		this.criarProspectAluno = criarProspectAluno;
	}

	public Boolean getCriarProspectCandidato() {
		if (criarProspectCandidato == null) {
			criarProspectCandidato = Boolean.FALSE;
		}
		return criarProspectCandidato;
	}

	public void setCriarProspectCandidato(Boolean criarProspectCandidato) {
		this.criarProspectCandidato = criarProspectCandidato;
	}

	public Boolean getCriarProspectFuncionario() {
		if (criarProspectFuncionario == null) {
			criarProspectFuncionario = Boolean.FALSE;
		}
		return criarProspectFuncionario;
	}

	public void setCriarProspectFuncionario(Boolean criarProspectFuncionario) {
		this.criarProspectFuncionario = criarProspectFuncionario;
	}

	public Boolean getOcultarMediaProcSeletivo() {
		if (ocultarMediaProcSeletivo == null) {
			ocultarMediaProcSeletivo = Boolean.FALSE;
		}
		return ocultarMediaProcSeletivo;
	}

	public void setOcultarMediaProcSeletivo(Boolean ocultarMediaProcSeletivo) {
		this.ocultarMediaProcSeletivo = ocultarMediaProcSeletivo;
	}

	public Boolean getOcultarClassificacaoProcSeletivo() {
		if (ocultarClassificacaoProcSeletivo == null) {
			ocultarClassificacaoProcSeletivo = Boolean.FALSE;
		}
		return ocultarClassificacaoProcSeletivo;
	}

	public void setOcultarClassificacaoProcSeletivo(Boolean ocultarClassificacaoProcSeletivo) {
		this.ocultarClassificacaoProcSeletivo = ocultarClassificacaoProcSeletivo;
	}
	

    public Boolean getControlarCargaHorariaMaximaEstagioObrigatorio() {
        if (controlarCargaHorariaMaximaEstagioObrigatorio == null) { 
            controlarCargaHorariaMaximaEstagioObrigatorio = Boolean.FALSE;
        }
        return controlarCargaHorariaMaximaEstagioObrigatorio;
    }

    public void setControlarCargaHorariaMaximaEstagioObrigatorio(Boolean controlarCargaHorariaMaximaEstagioObrigatorio) {
        this.controlarCargaHorariaMaximaEstagioObrigatorio = controlarCargaHorariaMaximaEstagioObrigatorio;
    }

    public Integer getCargaHorariaMaximaSemanal() {
        if (cargaHorariaMaximaSemanal == null) { 
            cargaHorariaMaximaSemanal = 0;
        }
        return cargaHorariaMaximaSemanal;
    }

    public void setCargaHorariaMaximaSemanal(Integer cargaHorariaMaximaSemanal) {
        this.cargaHorariaMaximaSemanal = cargaHorariaMaximaSemanal;
    }

    public Integer getCargaHorariaMaximaDiaria() {
        if (cargaHorariaMaximaDiaria == null) { 
            cargaHorariaMaximaDiaria = 0;
        }
        return cargaHorariaMaximaDiaria;
    }

    public void setCargaHorariaMaximaDiaria(Integer cargaHorariaMaximaDiaria) {
        this.cargaHorariaMaximaDiaria = cargaHorariaMaximaDiaria;
    }

    public String getSeguradoraPadraoEstagioObrigatorio() {
        if (seguradoraPadraoEstagioObrigatorio == null) { 
            seguradoraPadraoEstagioObrigatorio = "";
        }
        return seguradoraPadraoEstagioObrigatorio;
    }

    public void setSeguradoraPadraoEstagioObrigatorio(String seguradoraPadraoEstagioObrigatorio) {
        this.seguradoraPadraoEstagioObrigatorio = seguradoraPadraoEstagioObrigatorio;
    }

    public String getApolicePadraoEstagioObrigatorio() {
        if (apolicePadraoEstagioObrigatorio == null) { 
            apolicePadraoEstagioObrigatorio = "";
        }
        return apolicePadraoEstagioObrigatorio;
    }

    public void setApolicePadraoEstagioObrigatorio(String apolicePadraoEstagioObrigatorio) {
        this.apolicePadraoEstagioObrigatorio = apolicePadraoEstagioObrigatorio;
    }

    public Boolean getForcarSeguradoraEApoliceParaEstagioObrigatorio() {
        if (forcarSeguradoraEApoliceParaEstagioObrigatorio == null) { 
            forcarSeguradoraEApoliceParaEstagioObrigatorio = Boolean.FALSE;
        }
        return forcarSeguradoraEApoliceParaEstagioObrigatorio;
    }

    public void setForcarSeguradoraEApoliceParaEstagioObrigatorio(Boolean forcarSeguradoraEApoliceParaEstagioObrigatorio) {
        this.forcarSeguradoraEApoliceParaEstagioObrigatorio = forcarSeguradoraEApoliceParaEstagioObrigatorio;
    }
    
    public Boolean getRealizarCalculoMediaFinalFechPeriodo() {
    	if (realizarCalculoMediaFinalFechPeriodo == null) {
    		realizarCalculoMediaFinalFechPeriodo = Boolean.FALSE;
    	}
		return realizarCalculoMediaFinalFechPeriodo;
	}

	public void setRealizarCalculoMediaFinalFechPeriodo(Boolean realizarCalculoMediaFinalFechPeriodo) {
		this.realizarCalculoMediaFinalFechPeriodo = realizarCalculoMediaFinalFechPeriodo;
	}
	
	public Boolean getOcultarChamadaCandidatoProcSeletivo() {
		if (ocultarChamadaCandidatoProcSeletivo == null) {
			ocultarChamadaCandidatoProcSeletivo = Boolean.FALSE;
		}
		return ocultarChamadaCandidatoProcSeletivo;
	}
	
	public void setOcultarChamadaCandidatoProcSeletivo(Boolean ocultarChamadaCandidatoProcSeletivo) {
		this.ocultarChamadaCandidatoProcSeletivo = ocultarChamadaCandidatoProcSeletivo;
	}

	public Integer getNrDiasNotifVencCand() {
		if (nrDiasNotifVencCand == null) {
			nrDiasNotifVencCand = 0;
		}
		return nrDiasNotifVencCand;
	}

	public void setNrDiasNotifVencCand(Integer nrDiasNotifVencCand) {
		this.nrDiasNotifVencCand = nrDiasNotifVencCand;
	}
	
	public ArquivoVO getCertificadoParaDocumento() {
		if(certificadoParaDocumento == null	){
			certificadoParaDocumento = new ArquivoVO();
		}
		return certificadoParaDocumento;
	}

	public void setCertificadoParaDocumento(ArquivoVO certificadoParaDocumento) {
		this.certificadoParaDocumento = certificadoParaDocumento;
	}

	public String getSenhaCertificadoParaDocumento() {
		return senhaCertificadoParaDocumento;
	}

	public void setSenhaCertificadoParaDocumento(String senhaCertificadoParaDocumento) {
		this.senhaCertificadoParaDocumento = senhaCertificadoParaDocumento;
	}
	
	public Integer getQuantidadeCasaDecimalConsiderarNotaProcessoSeletivo() {
		if (quantidadeCasaDecimalConsiderarNotaProcessoSeletivo == null || quantidadeCasaDecimalConsiderarNotaProcessoSeletivo == 0 ) {
			quantidadeCasaDecimalConsiderarNotaProcessoSeletivo = 2;
		}
		return quantidadeCasaDecimalConsiderarNotaProcessoSeletivo;
	}

	public void setQuantidadeCasaDecimalConsiderarNotaProcessoSeletivo(Integer quantidadeCasaDecimalConsiderarNotaProcessoSeletivo) {
		this.quantidadeCasaDecimalConsiderarNotaProcessoSeletivo = quantidadeCasaDecimalConsiderarNotaProcessoSeletivo;
	}

	public Boolean getPrimeiroAcessoAlunoResetarSenha() {
		if (primeiroAcessoAlunoResetarSenha == null) {
			primeiroAcessoAlunoResetarSenha = Boolean.FALSE;
		}
		return primeiroAcessoAlunoResetarSenha;
	}

	public void setPrimeiroAcessoAlunoResetarSenha(Boolean primeiroAcessoAlunoResetarSenha) {
		this.primeiroAcessoAlunoResetarSenha = primeiroAcessoAlunoResetarSenha;
	}

	public Boolean getPrimeiroAcessoProfessorResetarSenha() {
		if (primeiroAcessoProfessorResetarSenha == null) {
			primeiroAcessoProfessorResetarSenha = Boolean.FALSE;
		}
		return primeiroAcessoProfessorResetarSenha;
	}

	public void setPrimeiroAcessoProfessorResetarSenha(Boolean primeiroAcessoProfessorResetarSenha) {
		this.primeiroAcessoProfessorResetarSenha = primeiroAcessoProfessorResetarSenha;
	}
	
	public String getUrlAcessoExternoAplicacao() {
		if (urlAcessoExternoAplicacao == null) {
			urlAcessoExternoAplicacao = "";
		}
		return urlAcessoExternoAplicacao;
	}

	public void setUrlAcessoExternoAplicacao(String urlAcessoExternoAplicacao) {
		this.urlAcessoExternoAplicacao = urlAcessoExternoAplicacao;
	}
	
	public ConfiguracaoAtualizacaoCadastralVO getConfiguracaoAtualizacaoCadastralVO() {
		if (configuracaoAtualizacaoCadastralVO == null) {
			configuracaoAtualizacaoCadastralVO = new ConfiguracaoAtualizacaoCadastralVO();
		}
		return configuracaoAtualizacaoCadastralVO;
	}

	public void setConfiguracaoAtualizacaoCadastralVO(ConfiguracaoAtualizacaoCadastralVO configuracaoAtualizacaoCadastralVO) {
		this.configuracaoAtualizacaoCadastralVO = configuracaoAtualizacaoCadastralVO;
	}
	
	public ConfiguracaoCandidatoProcessoSeletivoVO getConfiguracaoCandidatoProcessoSeletivoVO() {
		if(configuracaoCandidatoProcessoSeletivoVO == null) {
			configuracaoCandidatoProcessoSeletivoVO = new ConfiguracaoCandidatoProcessoSeletivoVO();
		}
		return configuracaoCandidatoProcessoSeletivoVO;
	}
	
	public void setConfiguracaoCandidatoProcessoSeletivoVO(ConfiguracaoCandidatoProcessoSeletivoVO configuracaoCandidatoProcessoSeletivo) {
		this.configuracaoCandidatoProcessoSeletivoVO = configuracaoCandidatoProcessoSeletivo;
	}

	public Boolean getBloquearLancamentosNotasAulasFeriadosFinaisSemana() {
		if (bloquearLancamentosNotasAulasFeriadosFinaisSemana == null) {
			bloquearLancamentosNotasAulasFeriadosFinaisSemana = false;
		}
		return bloquearLancamentosNotasAulasFeriadosFinaisSemana;
	}

	public void setBloquearLancamentosNotasAulasFeriadosFinaisSemana(Boolean bloquearLancamentosNotasAulasFeriadosFinaisSemana) {
		this.bloquearLancamentosNotasAulasFeriadosFinaisSemana = bloquearLancamentosNotasAulasFeriadosFinaisSemana;
	}

	public Boolean getPermitirProfessorRealizarLancamentoAlunosPreMatriculados() {
		if (permitirProfessorRealizarLancamentoAlunosPreMatriculados == null) {
			permitirProfessorRealizarLancamentoAlunosPreMatriculados = true;
		}
		return permitirProfessorRealizarLancamentoAlunosPreMatriculados;
	}

	public void setPermitirProfessorRealizarLancamentoAlunosPreMatriculados(Boolean permitirProfessorRealizarLancamentoAlunosPreMatriculados) {
		this.permitirProfessorRealizarLancamentoAlunosPreMatriculados = permitirProfessorRealizarLancamentoAlunosPreMatriculados;
	}
	
	public Integer getQuantidadeCaracteresMinimoSenhaUsuario() {
	    if(quantidadeCaracteresMinimoSenhaUsuario == null){
		quantidadeCaracteresMinimoSenhaUsuario = 0;
	    }
	    return quantidadeCaracteresMinimoSenhaUsuario;
	}

	public void setQuantidadeCaracteresMinimoSenhaUsuario(Integer quantidadeCaracteresMinimoSenhaUsuario) {
	    this.quantidadeCaracteresMinimoSenhaUsuario = quantidadeCaracteresMinimoSenhaUsuario;
	}
	
	public Boolean getNivelSegurancaNumero() {
	    if(nivelSegurancaNumero == null ){
		nivelSegurancaNumero = false;
	    }
	    return nivelSegurancaNumero;
	}

	public void setNivelSegurancaNumero(Boolean nivelSegurancaNumero) {
	    this.nivelSegurancaNumero = nivelSegurancaNumero;
	}

	public Boolean getNivelSegurancaLetra() {
	    if(nivelSegurancaLetra == null){
		nivelSegurancaLetra = false;
	    }
	    return nivelSegurancaLetra;
	}

	public void setNivelSegurancaLetra(Boolean nivelSegurancaLetra) {
	    this.nivelSegurancaLetra = nivelSegurancaLetra;
	}

	public Boolean getNivelSegurancaLetraMaiuscula() {
	    if(nivelSegurancaLetraMaiuscula == null){
		nivelSegurancaLetraMaiuscula = false;
	    }
	    return nivelSegurancaLetraMaiuscula;
	}

	public void setNivelSegurancaLetraMaiuscula(Boolean nivelSegurancaLetraMaiuscula) {
	    this.nivelSegurancaLetraMaiuscula = nivelSegurancaLetraMaiuscula;
	}

	public Boolean getNivelSegurancaCaracterEspecial() {
	    if(nivelSegurancaCaracterEspecial == null){
		nivelSegurancaCaracterEspecial = false;
	    }
	    return nivelSegurancaCaracterEspecial;
	}

	public void setNivelSegurancaCaracterEspecial(Boolean nivelSegurancaCaracterEspecial) {
	    this.nivelSegurancaCaracterEspecial = nivelSegurancaCaracterEspecial;
	}

	public Integer getNivelcontrolealteracaosenha() {
	    if(nivelcontrolealteracaosenha == null){
		nivelcontrolealteracaosenha = 0;
	    }
	    return nivelcontrolealteracaosenha;
	}

	public void setNivelcontrolealteracaosenha(Integer nivelcontrolealteracaosenha) {
	    this.nivelcontrolealteracaosenha = nivelcontrolealteracaosenha;
	}

	public String getIdClienteRdStation() {
		if(idClienteRdStation == null){
			idClienteRdStation = "";
		}
		return idClienteRdStation;
	}

	public void setIdClienteRdStation(String idClienteRdStation) {
		this.idClienteRdStation = idClienteRdStation;
	}

	public String getSenhaClienteRdStation() {
		if(senhaClienteRdStation == null){
			senhaClienteRdStation = "";
		}
		return senhaClienteRdStation;
	}

	public void setSenhaClienteRdStation(String senhaClienteRdStation) {
		this.senhaClienteRdStation = senhaClienteRdStation;
	}

	public String getTokenRdStation() {
		if(tokenRdStation == null){
			tokenRdStation = "";
		}
		return tokenRdStation;
	}

	public void setTokenRdStation(String tokenRdStation) {
		this.tokenRdStation = tokenRdStation;
	}

	public String getTokenPrivadoRdStation() {
		if(tokenPrivadoRdStation == null){
			tokenPrivadoRdStation = "";
		}
		return tokenPrivadoRdStation;
	}

	public void setTokenPrivadoRdStation(String tokenPrivadoRdStation) {
		this.tokenPrivadoRdStation = tokenPrivadoRdStation;
	}

	public Boolean getAtivarIntegracaoRdStation() {
		if(ativarIntegracaoRdStation == null) {
			ativarIntegracaoRdStation = false;
		}
		return ativarIntegracaoRdStation;
	}

	public void setAtivarIntegracaoRdStation(Boolean ativarIntegracaoRdStation) {
		this.ativarIntegracaoRdStation = ativarIntegracaoRdStation;
	}

	public String getIdentificadorRdStation() {
		if(identificadorRdStation == null){
			identificadorRdStation = "";
		}
		return identificadorRdStation;
	}

	public void setIdentificadorRdStation(String identificadorRdStation) {
		this.identificadorRdStation = identificadorRdStation;
	}
	
	
	
	
	public String getNomeDestinatarioEmailIntegracaoLGPD() {
		if(nomeDestinatarioEmailIntegracaoLGPD == null) {
			nomeDestinatarioEmailIntegracaoLGPD = "";
		}
		return nomeDestinatarioEmailIntegracaoLGPD;
	}

	public void setNomeDestinatarioEmailIntegracaoLGPD(String nomeDestinatarioEmailIntegracaoLGPD) {
		this.nomeDestinatarioEmailIntegracaoLGPD = nomeDestinatarioEmailIntegracaoLGPD;
	}

	public String getEmailDestinatarioIntegracaoLGPD() {
		if(emailDestinatarioIntegracaoLGPD == null) {
			emailDestinatarioIntegracaoLGPD = "";
		}
		return emailDestinatarioIntegracaoLGPD;
	}

	public void setEmailDestinatarioIntegracaoLGPD(String emailDestinatarioIntegracaoLGPD) {
		this.emailDestinatarioIntegracaoLGPD = emailDestinatarioIntegracaoLGPD;
	}

	public String getMensagemLGPD() {
		if(mensagemLGPD == null) {
			mensagemLGPD = "";
		}
		return mensagemLGPD;
	}

	public void setMensagemLGPD(String mensagemLGPD) {
		this.mensagemLGPD = mensagemLGPD;
	}

	public String getAssuntoMensagemLGPD() {
		if(assuntoMensagemLGPD == null) {
			assuntoMensagemLGPD = "";
		}
		return assuntoMensagemLGPD;
	}

	public void setAssuntoMensagemLGPD(String assuntoMensagemLGPD) {
		this.assuntoMensagemLGPD = assuntoMensagemLGPD;
	}

	public Boolean getAtivarIntegracaoLGPD() {
		if(ativarIntegracaoLGPD == null) {
			ativarIntegracaoLGPD = Boolean.FALSE;
		}
		return ativarIntegracaoLGPD;
	}

	public void setAtivarIntegracaoLGPD(Boolean ativarIntegracaoLGPD) {
		this.ativarIntegracaoLGPD = ativarIntegracaoLGPD;
		
	}
	
	
	

	public TextoPadraoDeclaracaoVO getTextoPadraoDadosSensiveisLGPD() {
		if(textoPadraoDadosSensiveisLGPD == null) {
			textoPadraoDadosSensiveisLGPD = new TextoPadraoDeclaracaoVO();
		}
		return textoPadraoDadosSensiveisLGPD;
	}

	public void setTextoPadraoDadosSensiveisLGPD(TextoPadraoDeclaracaoVO textoPadraoDadosSensiveisLGPD) {
		this.textoPadraoDadosSensiveisLGPD = textoPadraoDadosSensiveisLGPD;
	}

	public PerfilAcessoVO getPerfilAcessoProfessor(TipoNivelEducacional tipoNivelEducacional) {
		switch (tipoNivelEducacional) {
		case BASICO:
			return getPerfilPadraoProfessorEnsinoFundamental();		
		case EXTENSAO:
			return getPerfilPadraoProfessorExtensao();		
		case GRADUACAO_TECNOLOGICA:
			return getPerfilPadraoProfessorGraduacaoTecnologica();		
		case INFANTIL:
			return getPerfilPadraoProfessorEducacaoInfantil();		
		case MEDIO:
			return getPerfilPadraoProfessorEnsinoMedio();		
		case MESTRADO:
			return getPerfilPadraoProfessorMestrado();		
		case POS_GRADUACAO:
			return getPerfilPadraoProfessorPosGraduacao();		
		case PROFISSIONALIZANTE:
			return getPerfilPadraoProfessorTecnicoProfissionalizante();		
		case SEQUENCIAL:
			return getPerfilPadraoProfessorSequencial();		
		case SUPERIOR:
			return getPerfilPadraoProfessorGraduacao();		

		default:
			return null;
		}
	}

	public String getTokenWebService() {
		if (tokenWebService == null)
			tokenWebService = "";
		return tokenWebService;
	}

	public void setTokenWebService(String tokenWebService) {
		this.tokenWebService = tokenWebService;
	}
	
	

	public String getLoginIntegracaoSofFin() {
		if (loginIntegracaoSofFin == null)
			loginIntegracaoSofFin = "";
		return loginIntegracaoSofFin;
	}

	public void setLoginIntegracaoSofFin(String loginIntegracaoSofFin) {
		this.loginIntegracaoSofFin = loginIntegracaoSofFin;
	}

	public String getSenhaIntegracaoSofFin() {
		if (senhaIntegracaoSofFin == null)
			senhaIntegracaoSofFin = "";
		return senhaIntegracaoSofFin;
	}

	public void setSenhaIntegracaoSofFin(String senhaIntegracaoSofFin) {
		this.senhaIntegracaoSofFin = senhaIntegracaoSofFin;
	}

	public String getTokenIntegracaoSofFin() {
		if (tokenIntegracaoSofFin == null)
			tokenIntegracaoSofFin = "";
		return tokenIntegracaoSofFin;
	}

	public void setTokenIntegracaoSofFin(String tokenIntegracaoSofFin) {
		this.tokenIntegracaoSofFin = tokenIntegracaoSofFin;
	}

	public AmbienteEnum getAmbienteEnumIntegracaoSoftFin() {
		if (ambienteEnumIntegracaoSoftFin == null)
			ambienteEnumIntegracaoSoftFin = AmbienteEnum.HOMOLOGACAO;
		return ambienteEnumIntegracaoSoftFin;
	}

	public void setAmbienteEnumIntegracaoSoftFin(AmbienteEnum ambienteEnumIntegracaoSoftFin) {
		this.ambienteEnumIntegracaoSoftFin = ambienteEnumIntegracaoSoftFin;
	}

	public Boolean getIntegracaoServidorOnline() {
		if (integracaoServidorOnline == null)
			integracaoServidorOnline = false;
		return integracaoServidorOnline;
	}

	public void setIntegracaoServidorOnline(Boolean integracaoServidorOnline) {
		this.integracaoServidorOnline = integracaoServidorOnline;
	}

	public String getServidorArquivoOnline() {
		if (servidorArquivoOnline == null)
			servidorArquivoOnline = "APACHE";
		return servidorArquivoOnline;
	}

	public void setServidorArquivoOnline(String servidorArquivoOnline) {
		this.servidorArquivoOnline = servidorArquivoOnline;
	}

	public String getUsuarioAutenticacao() {
		if (usuarioAutenticacao == null)
			usuarioAutenticacao = "";
		return usuarioAutenticacao;
	}

	public void setUsuarioAutenticacao(String usuarioAutenticacao) {
		this.usuarioAutenticacao = usuarioAutenticacao;
	}

	public String getSenhaAutenticacao() {
		if (senhaAutenticacao == null)
			senhaAutenticacao = "";
		return senhaAutenticacao;
	}

	public void setSenhaAutenticacao(String senhaAutenticacao) {
		this.senhaAutenticacao = senhaAutenticacao;
	}
	
	public Boolean getPermitirAcessoAlunoPreMatricula() {
		if (permitirAcessoAlunoPreMatricula == null) {
			permitirAcessoAlunoPreMatricula = true;
		}
		return permitirAcessoAlunoPreMatricula;
	}

	public void setPermitirAcessoAlunoPreMatricula(Boolean permitirAcessoAlunoPreMatricula) {
		this.permitirAcessoAlunoPreMatricula = permitirAcessoAlunoPreMatricula;
	}

	public Boolean getPermitirAcessoAlunoEvasao() {
		if (permitirAcessoAlunoEvasao == null) {
			permitirAcessoAlunoEvasao = false;
		}
		return permitirAcessoAlunoEvasao;
	}

	public void setPermitirAcessoAlunoEvasao(Boolean permitirAcessoAlunoEvasao) {
		this.permitirAcessoAlunoEvasao = permitirAcessoAlunoEvasao;
	}

	public Boolean getPermitirAcessoAlunoFormado() {
		if (permitirAcessoAlunoFormado == null) {
			permitirAcessoAlunoFormado = false;
		}
		return permitirAcessoAlunoFormado;
	}

	public void setPermitirAcessoAlunoFormado(Boolean permitirAcessoAlunoFormado) {
		this.permitirAcessoAlunoFormado = permitirAcessoAlunoFormado;
	}

	public PerfilAcessoVO getPerfilPadraoAlunoPreMatricula() {
		if (perfilPadraoAlunoPreMatricula == null) {
			perfilPadraoAlunoPreMatricula = new PerfilAcessoVO();
		}
		return perfilPadraoAlunoPreMatricula;
	}

	public void setPerfilPadraoAlunoPreMatricula(PerfilAcessoVO perfilPadraoAlunoPreMatricula) {
		this.perfilPadraoAlunoPreMatricula = perfilPadraoAlunoPreMatricula;
	}

	public PerfilAcessoVO getPerfilPadraoAlunoEvasao() {
		if (perfilPadraoAlunoEvasao == null) {
			perfilPadraoAlunoEvasao = new PerfilAcessoVO();
		}
		return perfilPadraoAlunoEvasao;
	}

	public void setPerfilPadraoAlunoEvasao(PerfilAcessoVO perfilPadraoAlunoEvasao) {
		this.perfilPadraoAlunoEvasao = perfilPadraoAlunoEvasao;
	}

	public PerfilAcessoVO getPerfilPadraoAlunoFormado() {
		if (perfilPadraoAlunoFormado == null) {
			perfilPadraoAlunoFormado = new PerfilAcessoVO();
		}
		return perfilPadraoAlunoFormado;
	}

	public void setPerfilPadraoAlunoFormado(PerfilAcessoVO perfilPadraoAlunoFormado) {
		this.perfilPadraoAlunoFormado = perfilPadraoAlunoFormado;
	}

	public String getLinkAcessoVisoesMoodle() {
		if (linkAcessoVisoesMoodle == null) {
			linkAcessoVisoesMoodle = "";
		}
		return linkAcessoVisoesMoodle;
	}

	public void setLinkAcessoVisoesMoodle(String linkAcessoVisoesMoodle) {
		this.linkAcessoVisoesMoodle = linkAcessoVisoesMoodle;
	}

	public String getUrlWebserviceNFe() {
		if (urlWebserviceNFe == null) {
			urlWebserviceNFe = "";
		}
		return urlWebserviceNFe;
	}

	public void setUrlWebserviceNFe(String urlWebserviceNFe) {
		this.urlWebserviceNFe = urlWebserviceNFe;
	}
	
	public String getUrlWebserviceNFSe() {
		if (urlWebserviceNFSe == null) {
			urlWebserviceNFSe = "";
		}
		return urlWebserviceNFSe;
	}

	public void setUrlWebserviceNFSe(String urlWebserviceNFSe) {
		this.urlWebserviceNFSe = urlWebserviceNFSe;
	}

	public String getNomeRepositorio() {
		if(nomeRepositorio == null) {
			nomeRepositorio = "sei-"+getIdAutenticacaoServOtimize();
		}
		return nomeRepositorio;
	}

	public void setNomeRepositorio(String nomeRepositorio) {
		this.nomeRepositorio = nomeRepositorio;
	}

	public static void validarDadosIntegracaoAmazon(ConfiguracaoGeralSistemaVO obj) throws ConsistirException {
		if (obj.getIntegracaoServidorOnline() && obj.getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3.getValor()) && !Uteis.isAtributoPreenchido(obj.getIdAutenticacaoServOtimize())) {
            throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoAmazonS3CampoObrigatorio"));
        }		
		if (obj.getIntegracaoServidorOnline() && obj.getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3.getValor()) && !Uteis.isAtributoPreenchido(obj.getUsuarioAutenticacao())) {
            throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoAmazonS3"));
        }
		if (obj.getIntegracaoServidorOnline() && obj.getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3.getValor()) && !Uteis.isAtributoPreenchido(obj.getSenhaAutenticacao())) {
            throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoAmazonS3"));
        }
	}
	
	
	

	public String getSiglaAbonoFalta() {
		if (siglaAbonoFalta == null) {
			siglaAbonoFalta = "";
		}
		return siglaAbonoFalta;
	}

	public void setSiglaAbonoFalta(String siglaAbonoFalta) {
		this.siglaAbonoFalta = siglaAbonoFalta;
	}

	public String getDescricaoAbonoFalta() {
		if (descricaoAbonoFalta == null) {
			descricaoAbonoFalta = "";
		}
		return descricaoAbonoFalta;
	}

	public void setDescricaoAbonoFalta(String descricaoAbonoFalta) {
		this.descricaoAbonoFalta = descricaoAbonoFalta;
	}

	public Boolean getPermiteReativacaoMatriculaSemRequerimento() {
        if (permiteReativacaoMatriculaSemRequerimento == null) {
        	permiteReativacaoMatriculaSemRequerimento = Boolean.FALSE;
        }
		return permiteReativacaoMatriculaSemRequerimento;
	}

	public void setPermiteReativacaoMatriculaSemRequerimento(Boolean permiteReativacaoMatriculaSemRequerimento) {
		this.permiteReativacaoMatriculaSemRequerimento = permiteReativacaoMatriculaSemRequerimento;
	}

	public Boolean getDefinirPerfilAcessoAlunoNaoAssinouContratoMatricula() {
		if(definirPerfilAcessoAlunoNaoAssinouContratoMatricula == null) {
			definirPerfilAcessoAlunoNaoAssinouContratoMatricula = Boolean.FALSE;
		}
		return definirPerfilAcessoAlunoNaoAssinouContratoMatricula;
	}

	public void setDefinirPerfilAcessoAlunoNaoAssinouContratoMatricula(
			Boolean definirPerfilAcessoAlunoNaoAssinouContratoMatricula) {
		this.definirPerfilAcessoAlunoNaoAssinouContratoMatricula = definirPerfilAcessoAlunoNaoAssinouContratoMatricula;
	}

	public PerfilAcessoVO getPerfilAcessoAlunoNaoAssinouContratoMatricula() {
		if(perfilAcessoAlunoNaoAssinouContratoMatricula == null) {
			perfilAcessoAlunoNaoAssinouContratoMatricula = new PerfilAcessoVO();
		}
		return perfilAcessoAlunoNaoAssinouContratoMatricula;
	}

	public void setPerfilAcessoAlunoNaoAssinouContratoMatricula(
			PerfilAcessoVO perfilAcessoAlunoNaoAssinouContratoMatricula) {
		this.perfilAcessoAlunoNaoAssinouContratoMatricula = perfilAcessoAlunoNaoAssinouContratoMatricula;
	}

	public Boolean getApresentarMensagemAlertaAlunoNaoAssinouContrato() {
		if(apresentarMensagemAlertaAlunoNaoAssinouContrato == null) {
			apresentarMensagemAlertaAlunoNaoAssinouContrato = Boolean.FALSE;
		}
		return apresentarMensagemAlertaAlunoNaoAssinouContrato;
	}

	public void setApresentarMensagemAlertaAlunoNaoAssinouContrato(
			Boolean apresentarMensagemAlertaAlunoNaoAssinouContrato) {
		this.apresentarMensagemAlertaAlunoNaoAssinouContrato = apresentarMensagemAlertaAlunoNaoAssinouContrato;
	}

	public String getMensagemAlertaAlunoNaoAssinouContratoMatricula() {
		if(mensagemAlertaAlunoNaoAssinouContratoMatricula == null) {
			mensagemAlertaAlunoNaoAssinouContratoMatricula = "";
		}
		return mensagemAlertaAlunoNaoAssinouContratoMatricula;
	}

	public void setMensagemAlertaAlunoNaoAssinouContratoMatricula(String mensagemAlertaAlunoNaoAssinouContratoMatricula) {
		this.mensagemAlertaAlunoNaoAssinouContratoMatricula = mensagemAlertaAlunoNaoAssinouContratoMatricula;
	}
	
	

	public Integer getQtdTentativasFalhaLogin() {
		if (qtdTentativasFalhaLogin == null) {
			qtdTentativasFalhaLogin = 10;
		}
		return qtdTentativasFalhaLogin;
	}

	public void setQtdTentativasFalhaLogin(Integer qtdTentativasFalhaLogin) {
		this.qtdTentativasFalhaLogin = qtdTentativasFalhaLogin;
	}

	public Integer getTempoBloqTentativasFalhaLogin() {
		if (tempoBloqTentativasFalhaLogin == null) {
			tempoBloqTentativasFalhaLogin = 5;
		}
		return tempoBloqTentativasFalhaLogin;
	}

	public void setTempoBloqTentativasFalhaLogin(Integer tempoBloqTentativasFalhaLogin) {
		this.tempoBloqTentativasFalhaLogin = tempoBloqTentativasFalhaLogin;
	}

	public Integer getPercentualBaixaFrequencia() {
		if(percentualBaixaFrequencia == null) {
			percentualBaixaFrequencia = 0;
		}
		return percentualBaixaFrequencia;
	}

	public void setPercentualBaixaFrequencia(Integer percentualBaixaFrequencia) {
		this.percentualBaixaFrequencia = percentualBaixaFrequencia;
	}
		
	public Boolean getApresentarBotoesAcoesRequerimentoApenasAbaInicial() {
		if(apresentarBotoesAcoesRequerimentoApenasAbaInicial == null) {
			apresentarBotoesAcoesRequerimentoApenasAbaInicial = Boolean.FALSE;
		}
		return apresentarBotoesAcoesRequerimentoApenasAbaInicial;
	}

	public void setApresentarBotoesAcoesRequerimentoApenasAbaInicial(
			Boolean apresentarBotoesAcoesRequerimentoApenasAbaInicial) {
		this.apresentarBotoesAcoesRequerimentoApenasAbaInicial = apresentarBotoesAcoesRequerimentoApenasAbaInicial;
	}
	
	private String urlHomeCandidato;
	
	public String getUrlHomeCandidato() {
		if(urlHomeCandidato == null) {
			urlHomeCandidato = "/visaoCandidato/homeCandidato.xhtml";
		}
		return urlHomeCandidato;
	}
	
	public void setUrlHomeCandidato(String urlHomeCandidato) {
		this.urlHomeCandidato = urlHomeCandidato;
	}

	public String getUsernameSMS() {
		if(usernameSMS == null ) {
			usernameSMS ="";
		}
		return usernameSMS;
	}

	public void setUsernameSMS(String usernameSMS) {
		this.usernameSMS = usernameSMS;
	}

	public String getSenhaSMS() {
		if(senhaSMS == null) {
			senhaSMS = "" ;
		}
		return senhaSMS;
	}

	public void setSenhaSMS(String senhaSMS) {
		this.senhaSMS = senhaSMS;
	}

	public ConfiguracaoAparenciaSistemaVO getConfiguracaoAparenciaSistema() {
		if(configuracaoAparenciaSistema == null) {
			configuracaoAparenciaSistema =  new ConfiguracaoAparenciaSistemaVO();
		}
		return configuracaoAparenciaSistema;
	}

	public void setConfiguracaoAparenciaSistema(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistema) {
		this.configuracaoAparenciaSistema = configuracaoAparenciaSistema;
    }

    public Boolean getLogUploadProfessor() {
        if (logUploadProfessor == null) {
            logUploadProfessor = false;
        }
        return logUploadProfessor;
    }

    public void setLogUploadProfessor(Boolean logUploadProfessor) {
        this.logUploadProfessor = logUploadProfessor;
    }

    public List<ConfiguracaoLdapVO> getConfiguracaoLdapVOs() {
        if (configuracaoLdapVOs == null) {
            configuracaoLdapVOs = new ArrayList<>();
        }
        return configuracaoLdapVOs;
    }

    public void setConfiguracaoLdapVOs(List<ConfiguracaoLdapVO> configuracaoLdapVOs) {
        this.configuracaoLdapVOs = configuracaoLdapVOs;
    }



	public String getTextoParaOrientacaoTcc() {
		if (textoParaOrientacaoTcc == null) {
			textoParaOrientacaoTcc = "";
		}
		return textoParaOrientacaoTcc;
	}

	public void setTextoParaOrientacaoTcc(String textoParaOrientacaoTcc) {
		this.textoParaOrientacaoTcc = textoParaOrientacaoTcc;
	}
 
	public String getTextoParaOrientacaoProjetoIntegrador() {
		if (textoParaOrientacaoProjetoIntegrador == null) {
			textoParaOrientacaoProjetoIntegrador = "";
		}
		return textoParaOrientacaoProjetoIntegrador;
	}

	public void setTextoParaOrientacaoProjetoIntegrador(String textoParaOrientacaoProjetoIntegrador) {
		this.textoParaOrientacaoProjetoIntegrador = textoParaOrientacaoProjetoIntegrador;
	}

	private UsuarioVO usuarioOperacaoExternas;


	public UsuarioVO getUsuarioOperacaoExternas() {
		if(usuarioOperacaoExternas == null) {
			usuarioOperacaoExternas =  new UsuarioVO();
		}
		return usuarioOperacaoExternas;
	}

	public void setUsuarioOperacaoExternas(UsuarioVO usuarioOperacaoExternas) {
		this.usuarioOperacaoExternas = usuarioOperacaoExternas;
	}

	public MotivoCancelamentoTrancamentoVO getMotivoPadraoCancelamentoPreMatriculaCalouro() {
		if(motivoPadraoCancelamentoPreMatriculaCalouro == null) {
			motivoPadraoCancelamentoPreMatriculaCalouro = new MotivoCancelamentoTrancamentoVO();
		}
		return motivoPadraoCancelamentoPreMatriculaCalouro;
	}

	public void setMotivoPadraoCancelamentoPreMatriculaCalouro(
			MotivoCancelamentoTrancamentoVO motivoPadraoCancelamentoPreMatriculaCalouro) {
		this.motivoPadraoCancelamentoPreMatriculaCalouro = motivoPadraoCancelamentoPreMatriculaCalouro;
	}


	public String getActionIntegracaoSistemasProvaMestreGR() {
		if(actionIntegracaoSistemasProvaMestreGR == null ) {
			actionIntegracaoSistemasProvaMestreGR ="";
		}
		return actionIntegracaoSistemasProvaMestreGR;
	}

	public void setActionIntegracaoSistemasProvaMestreGR(String actionIntegracaoSistemasProvaMestreGR) {
		this.actionIntegracaoSistemasProvaMestreGR = actionIntegracaoSistemasProvaMestreGR;
	}

	public String getTokenIntegracaoSistemasProvaMestreGR() {
		if(tokenIntegracaoSistemasProvaMestreGR == null) {
			tokenIntegracaoSistemasProvaMestreGR = "";
		}
		return tokenIntegracaoSistemasProvaMestreGR;
	}

	public void setTokenIntegracaoSistemasProvaMestreGR(String tokenIntegracaoSistemasProvaMestreGR) {
		this.tokenIntegracaoSistemasProvaMestreGR = tokenIntegracaoSistemasProvaMestreGR;
	}

	public String getHeaderBarIntegracaoSistemasProvaMestreGR() {
		if(headerBarIntegracaoSistemasProvaMestreGR == null ) {
			headerBarIntegracaoSistemasProvaMestreGR="";
		}
		return headerBarIntegracaoSistemasProvaMestreGR;
	}

	public void setHeaderBarIntegracaoSistemasProvaMestreGR(String headerBarIntegracaoSistemasProvaMestreGR) {
		this.headerBarIntegracaoSistemasProvaMestreGR = headerBarIntegracaoSistemasProvaMestreGR;
	}

	public Boolean getHabilitarIntegracaoSistemaProvas() {
		if(habilitarIntegracaoSistemaProvas== null ) {
			habilitarIntegracaoSistemaProvas =Boolean.FALSE;
		}
		return habilitarIntegracaoSistemaProvas;
	}

	public void setHabilitarIntegracaoSistemaProvas(Boolean habilitarIntegracaoSistemaProvas) {
		this.habilitarIntegracaoSistemaProvas = habilitarIntegracaoSistemaProvas;
	}

	public QuestionarioVO getQuestionarioPlanoEnsino() {
		if(questionarioPlanoEnsino == null) {
			questionarioPlanoEnsino = new QuestionarioVO();
		}
		return questionarioPlanoEnsino;
	}

	public void setQuestionarioPlanoEnsino(QuestionarioVO questionarioPlanoEnsino) {
		this.questionarioPlanoEnsino = questionarioPlanoEnsino;
	}	
	
	
	   public String getTextoOrientacaoCancelamentoPorOutraMatricula() {
		   if(textoOrientacaoCancelamentoPorOutraMatricula == null) {
			   textoOrientacaoCancelamentoPorOutraMatricula = "";
		   }
			return textoOrientacaoCancelamentoPorOutraMatricula;
		}

		public void setTextoOrientacaoCancelamentoPorOutraMatricula(String textoOrientacaoCancelamentoPorOutraMatricula) {
			this.textoOrientacaoCancelamentoPorOutraMatricula = textoOrientacaoCancelamentoPorOutraMatricula;
		}

		public MotivoCancelamentoTrancamentoVO getMotivoPadraoCancelamentoOutraMatricula() {
			if(motivoPadraoCancelamentoOutraMatricula == null ) {
				motivoPadraoCancelamentoOutraMatricula = new MotivoCancelamentoTrancamentoVO();
			}
			return motivoPadraoCancelamentoOutraMatricula;
		}

		public void setMotivoPadraoCancelamentoOutraMatricula(MotivoCancelamentoTrancamentoVO motivoPadraoCancelamentoOutraMatricula) {
			this.motivoPadraoCancelamentoOutraMatricula = motivoPadraoCancelamentoOutraMatricula;
		}

	

		public String getJustificativaCancelamentoPorOutraMatricula() {
			if(justificativaCancelamentoPorOutraMatricula == null) {
				justificativaCancelamentoPorOutraMatricula ="";
			}
			return justificativaCancelamentoPorOutraMatricula;
		}

		public void setJustificativaCancelamentoPorOutraMatricula(String justificativaCancelamentoPorOutraMatricula) {
			this.justificativaCancelamentoPorOutraMatricula = justificativaCancelamentoPorOutraMatricula;
		}

		public Boolean getHabilitarRecursoInativacaoCredenciasAlunosFormados() {
			if(habilitarRecursoInativacaoCredenciasAlunosFormados == null) {
				habilitarRecursoInativacaoCredenciasAlunosFormados = Boolean.FALSE;
			}
			return habilitarRecursoInativacaoCredenciasAlunosFormados;
		}

		public void setHabilitarRecursoInativacaoCredenciasAlunosFormados(
				Boolean habilitarRecursoInativacaoCredenciasAlunosFormados) {
			this.habilitarRecursoInativacaoCredenciasAlunosFormados = habilitarRecursoInativacaoCredenciasAlunosFormados;
		}
		
		public Boolean getApresentarDocumentoPortalTransparenciaComPendenciaAssinatura() {
			if (apresentarDocumentoPortalTransparenciaComPendenciaAssinatura == null) {
				apresentarDocumentoPortalTransparenciaComPendenciaAssinatura = false;
			}
			return apresentarDocumentoPortalTransparenciaComPendenciaAssinatura;
		}

		public void setApresentarDocumentoPortalTransparenciaComPendenciaAssinatura(
				Boolean apresentarDocumentoPortalTransparenciaComPendenciaAssinatura) {
			this.apresentarDocumentoPortalTransparenciaComPendenciaAssinatura = apresentarDocumentoPortalTransparenciaComPendenciaAssinatura;
		}

		public Integer getTamanhoMaximoCorpoMensagem() {
			if(tamanhoMaximoCorpoMensagem == null) {
				tamanhoMaximoCorpoMensagem = 0;
			}
			return tamanhoMaximoCorpoMensagem;
		}

		public void setTamanhoMaximoCorpoMensagem(Integer tamanhoMaximoCorpoMensagem) {
			this.tamanhoMaximoCorpoMensagem = tamanhoMaximoCorpoMensagem;
		}

	public Integer getLimiteDestinatariosPorEmail() {
		if (limiteDestinatariosPorEmail == null) {
			limiteDestinatariosPorEmail = 0;
		}
		return limiteDestinatariosPorEmail;
	}

	public void setLimiteDestinatariosPorEmail(Integer limiteDestinatariosPorEmail) {
		this.limiteDestinatariosPorEmail = limiteDestinatariosPorEmail;
	}

	public Integer getTamanhoLimiteAnexoEmail() {
		if (tamanhoLimiteAnexoEmail == null) {
			tamanhoLimiteAnexoEmail = 0;
		}
		return tamanhoLimiteAnexoEmail;
	}

	public void setTamanhoLimiteAnexoEmail(Integer tamanhoLimiteAnexoEmail) {
		this.tamanhoLimiteAnexoEmail = tamanhoLimiteAnexoEmail;
	}
	
	public String getVersaoSeiSignature() {
		if (versaoSeiSignature == null) {
			versaoSeiSignature = Constantes.EMPTY;
		}
		return versaoSeiSignature;
	}

	public void setVersaoSeiSignature(String versaoSeiSignature) {
		this.versaoSeiSignature = versaoSeiSignature;
	}
	
	public Date getDataBaseValidacaoDiplomaDigital() {
		return dataBaseValidacaoDiplomaDigital;
	}
	
	public void setDataBaseValidacaoDiplomaDigital(Date dataBaseValidacaoDiplomaDigital) {
		this.dataBaseValidacaoDiplomaDigital = dataBaseValidacaoDiplomaDigital;
	}

	public Boolean getHabilitarIntegracaoSistemaSymplicty() {
		if (habilitarIntegracaoSistemaSymplicty == null) {
			habilitarIntegracaoSistemaSymplicty = false;
		}
		return habilitarIntegracaoSistemaSymplicty;
	}

	public void setHabilitarIntegracaoSistemaSymplicty(Boolean habilitarIntegracaoSistemaSymplicty) {
		this.habilitarIntegracaoSistemaSymplicty = habilitarIntegracaoSistemaSymplicty;
	}

	public String getHostIntegracaoSistemaSymplicty() {
		if (hostIntegracaoSistemaSymplicty == null) {
			hostIntegracaoSistemaSymplicty = "";
		}
		return hostIntegracaoSistemaSymplicty;
	}

	public void setHostIntegracaoSistemaSymplicty(String hostIntegracaoSistemaSymplicty) {
		this.hostIntegracaoSistemaSymplicty = hostIntegracaoSistemaSymplicty;
	}

	public String getUserIntegracaoSistemaSymplicty() {
		if (userIntegracaoSistemaSymplicty == null) {
			userIntegracaoSistemaSymplicty = "";
		}
		return userIntegracaoSistemaSymplicty;
	}

	public void setUserIntegracaoSistemaSymplicty(String userIntegracaoSistemaSymplicty) {
		this.userIntegracaoSistemaSymplicty = userIntegracaoSistemaSymplicty;
	}

	public String getPassIntegracaoSistemaSymplicty() {
		if (passIntegracaoSistemaSymplicty == null) {
			passIntegracaoSistemaSymplicty = "";
		}
		return passIntegracaoSistemaSymplicty;
	}

	public void setPassIntegracaoSistemaSymplicty(String passIntegracaoSistemaSymplicty) {
		this.passIntegracaoSistemaSymplicty = passIntegracaoSistemaSymplicty;
	}

	public Integer getPortIntegracaoSistemaSymplicty() {
		if (portIntegracaoSistemaSymplicty == null) {
			portIntegracaoSistemaSymplicty = 0;
		}
		return portIntegracaoSistemaSymplicty;
	}

	public void setPortIntegracaoSistemaSymplicty(Integer portIntegracaoSistemaSymplicty) {
		this.portIntegracaoSistemaSymplicty = portIntegracaoSistemaSymplicty;
	}

	public String getProtocolIntegracaoSistemaSymplicty() {
		if (protocolIntegracaoSistemaSymplicty == null) {
			protocolIntegracaoSistemaSymplicty = "";
		}
		return protocolIntegracaoSistemaSymplicty;
	}

	public void setProtocolIntegracaoSistemaSymplicty(String protocolIntegracaoSistemaSymplicty) {
		this.protocolIntegracaoSistemaSymplicty = protocolIntegracaoSistemaSymplicty;
	}

	public String getPastaDestinoRemotaSymplicty() {
		if (pastaDestinoRemotaSymplicty == null) {
			pastaDestinoRemotaSymplicty = "";
		}
		return pastaDestinoRemotaSymplicty;
	}

	public void setPastaDestinoRemotaSymplicty(String pastaDestinoRemotaSymplicty) {
		this.pastaDestinoRemotaSymplicty = pastaDestinoRemotaSymplicty;
	}

    public Boolean getHabilitarIntegracaoSistemaTechCert() {
        if (habilitarIntegracaoSistemaTechCert == null) {
            habilitarIntegracaoSistemaTechCert = false;
        }
        return habilitarIntegracaoSistemaTechCert;
    }

    public void setHabilitarIntegracaoSistemaTechCert(Boolean habilitarIntegracaoSistemaTechCert) {
        this.habilitarIntegracaoSistemaTechCert = habilitarIntegracaoSistemaTechCert;
    }

    public String getTokenSentry() {
        if (tokenSentry == null){
            tokenSentry = "";
        }
        return tokenSentry;
    }

    public void setTokenSentry(String tokenSentry) {
        this.tokenSentry = tokenSentry;
    }

    public Boolean getHabilitarMonitoramentoSentry() {
        if (habilitarMonitoramentoSentry == null){
            habilitarMonitoramentoSentry = Boolean.FALSE;
        }
        return habilitarMonitoramentoSentry;
    }

    public void setHabilitarMonitoramentoSentry(Boolean habilitarMonitoramentoSentry) {
        this.habilitarMonitoramentoSentry = habilitarMonitoramentoSentry;
    }

    public Boolean getHabilitarOperacoesTempoRealIntegracaoMestreGR() {
        if (habilitarOperacoesTempoRealIntegracaoMestreGR == null) {
            habilitarOperacoesTempoRealIntegracaoMestreGR = Boolean.FALSE;
        }
        return habilitarOperacoesTempoRealIntegracaoMestreGR;
    }

    public void setHabilitarOperacoesTempoRealIntegracaoMestreGR(Boolean habilitarOperacoesTempoRealIntegracaoMestreGR) {
        this.habilitarOperacoesTempoRealIntegracaoMestreGR = habilitarOperacoesTempoRealIntegracaoMestreGR;
    }

    public String getIntegracaoMestreGRURLBaseAPI() {
        if (integracaoMestreGRURLBaseAPI == null) {
            integracaoMestreGRURLBaseAPI = "";
        }
        return integracaoMestreGRURLBaseAPI;
    }

    public void setIntegracaoMestreGRURLBaseAPI(String integracaoMestreGRURLBaseAPI) {
        this.integracaoMestreGRURLBaseAPI = integracaoMestreGRURLBaseAPI;
    }

	public Boolean getHabilitarRecursosAcademicosVisaoAluno() {
		if (habilitarRecursosAcademicosVisaoAluno == null) {
			habilitarRecursosAcademicosVisaoAluno = true;
		}
		return habilitarRecursosAcademicosVisaoAluno;
	}

	public void setHabilitarRecursosAcademicosVisaoAluno(Boolean habilitarRecursosAcademicosVisaoAluno) {
		this.habilitarRecursosAcademicosVisaoAluno = habilitarRecursosAcademicosVisaoAluno;
	}
	
	public Boolean getHabilitarMonitoramentoBlackboardSentry() {
		if (habilitarMonitoramentoBlackboardSentry == null) {
			habilitarMonitoramentoBlackboardSentry = Boolean.FALSE;
		}
		return habilitarMonitoramentoBlackboardSentry;
	}

	public void setHabilitarMonitoramentoBlackboardSentry(Boolean habilitarMonitoramentoBlackboardSentry) {
		this.habilitarMonitoramentoBlackboardSentry = habilitarMonitoramentoBlackboardSentry;
	}

	public String getTokenBlackboardSentry() {
		if (tokenBlackboardSentry == null) {
			tokenBlackboardSentry = Constantes.EMPTY;
		}
		return tokenBlackboardSentry;
	}

	public void setTokenBlackboardSentry(String tokenBlackboardSentry) {
		this.tokenBlackboardSentry = tokenBlackboardSentry;
	}

	public Boolean getHabilitarEnvioEmailAssincrono() {
		if (habilitarEnvioEmailAssincrono == null) {
			habilitarEnvioEmailAssincrono = false;
		}
		return habilitarEnvioEmailAssincrono;
	}

	public void setHabilitarEnvioEmailAssincrono(Boolean habilitarEnvioEmailAssincrono) {
		this.habilitarEnvioEmailAssincrono = habilitarEnvioEmailAssincrono;
	}

	public Boolean getAtivarDebugEmail() {
		if(ativarDebugEmail == null) {
			ativarDebugEmail =  false;
		}
		return ativarDebugEmail;
	}

	public void setAtivarDebugEmail(Boolean ativarDebugEmail) {
		this.ativarDebugEmail = ativarDebugEmail;
	}

	public Long getTimeOutFilaEmail() {
		if(timeOutFilaEmail == null) {
			timeOutFilaEmail = 0l;
		}
		return timeOutFilaEmail;
	}

	public void setTimeOutFilaEmail(Long timeOutFilaEmail) {
		this.timeOutFilaEmail = timeOutFilaEmail;
	}
	
	
}