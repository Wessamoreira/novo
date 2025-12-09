package negocio.comuns.protocolo;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import jakarta.faces. model.SelectItem;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CidTipoRequerimentoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaCursadaVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.academico.MaterialRequerimentoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.MotivoCancelamentoTrancamentoVO;
import negocio.comuns.academico.PendenciaTipoDocumentoTipoRequerimentoVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.ExcluirJsonStrategy;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.PossuiEndereco;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.protocolo.enumeradores.SituacaoRequerimentoDisciplinasAproveitadasEnum;
import negocio.comuns.protocolo.enumeradores.SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.dominios.SituacaoRequerimento;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.dominios.TiposRequerimento;
import webservice.DateAdapterMobile;

/**
 * Reponsável por manter os dados da entidade Requerimento. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "requerimento")
public class RequerimentoVO extends SuperVO implements PossuiEndereco {

	private Integer codigo;
	private Integer contaReceber;
	private Date data;
	private Double valor;
	private Double valorAdicional;
	private Date dataPrevistaFinalizacao;
	private Date dataFinalizacao;
	private Date dataAfastamentoInicio;
	private Date dataAfastamentoFim;
	private Date dataEmissaoBoleto;
	private String situacao;
	private String nrDocumento;
	private String situacaoFinanceira;
	private String cpfRequerente;
	private String nomeRequerente;
	private String observacao;
	private Date dataRecebimentoDocRequerido;
	private UsuarioVO responsavelRecebimentoDocRequerido;
	private UsuarioVO responsavelEmissaoBoleto;
	
	private UnidadeEnsinoVO unidadeEnsino;
	private Boolean trocouTipoRequerimento;
	private Date dataVencimentoContaReceber;
	private FuncionarioVO funcionarioVO;
	private String sigla;
	private CursoVO curso;
	private TurnoVO turno;
	private Boolean taxaIsentaPorQtdeVia;
	
	private TipoPessoa tipoPessoa;
	private String tipoTrabalhoConclusaoCurso;
	private Double notaMonografia;
	private String tituloMonografia;
	private String orientadorMonografia;
	@Expose(serialize = false, deserialize = false)
	private RequerimentoVO requerimentoAntigo;
	private Boolean existeComunicadoInternoNaoLido;
	private Boolean permitirImpressaoBoleto;
	private Boolean permitirRecebimentoCartaoCreditoOnline;
	private Boolean permitirImpressaoArquivo;
	private Boolean permitirImpressaoComprovante;
	private Boolean permitirImpressaoCertificado;
	private Boolean permitirImpressaoDeclaracao;
	private Boolean edicao;
	private Boolean permitirRequerenteInteragirTramite;
	
	private CidTipoRequerimentoVO cidTipoRequerimentoVO;
//	private List<RequerimentoCidTipoRequerimentoVO> requerimentoCidTipoRequerimentoVOs;
//	private List<CidTipoRequerimentoVO> listaCidTipoRequerimentoVOs;
	private RequerimentoCidTipoRequerimentoVO requerimentoCidTipoRequerimentoVO;
	/**
	 *	mantem o numero de solicitações deste tipo de requerimento este número é contabilizado seguindo a seguinte regra
	 *  1º deve existir uma taxa vinculado ao requerimento
	 *  2ª No cadastro do tipo de requerimento o campo cobrar via a partir de deve ser > 0
	 *  3º Deverá respeitar a regra do campo tipoControleCobrancaViaRequerimento no cadastro tipo de requerimento 
	 *  onde ser estiver definido como PERIODO_MATRICULA este será contabilizado a cada matricula período
	 *  se estiver como CURSO então será contabilizado por matrícula.  
	 */
	private Integer numeroVia;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>TipoRequerimento </code>.
	 */
	private TipoRequerimentoVO tipoRequerimento;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Pessoa </code>.
	 */
	private DepartamentoVO departamentoResponsavel;
	private Integer ordemExecucaoTramiteDepartamento;
	private List<RequerimentoHistoricoVO> requerimentoHistoricoVOs;
	private List<RequerimentoDisciplinasAproveitadasVO> listaRequerimentoDisciplinasAproveitadasVOs;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Matricula </code>.
	 */
	private MatriculaVO matricula;
	private QuestionarioVO questionarioVO;
	private PessoaVO pessoa;
	private Boolean isApresentarIconeEmAlerta;
	private Double percDesconto;
	private Double valorDesconto;
	private String tipoDesconto;
	private String endereco;
	private String setor;
	private String numero;
	private String CEP;
	private String complemento;
	private CidadeVO cidade;
	private ArquivoVO arquivoVO;
	//private ArquivoVO arquivoAssinadoDigitalmente;
	private DocumentoAssinadoVO documentoAssinado;

	private Boolean excluirArquivo;
	private UsuarioVO responsavel;
	private String visaoGerado;
	private String motivoIndeferimento;
	private TurmaVO turma;
	private DisciplinaVO disciplina;
	// Usado apenas em tela ( mapa solicitacao reposição )
	private TurmaVO turmaReposicao;
	// Usado apenas em tela ( mapa solicitacao reposição )
	private Integer qtdAlunosReposicaoTurma;
	// Usado apenas em tela ( mapa solicitacao reposição )
	private Date dataAulaReprovado;
	// Usado apenas em tela ( mapa solicitacao reposição )
	private String documentosPendentes;
	public static final long serialVersionUID = 1L;
	private Boolean notificado;

	
	// Atributo criado para geração do relatório
	private String ano;
	private String semestre;
	private String situacaoMatriculaPeriodo;
	private List<MaterialRequerimentoVO> materialRequerimentoVOs;
	private Date dataUltimaAlteracao;
	
	
	
	//Boolean que controla se é possível imprimir ou não o requerimento na visao aluno
	private Boolean aptoImpressaoVisaoAluno;
	private Date dataUltimaImpressao;
	private String motivoDeferimento;
	
	// Campos usado apenas para controle de tela
    private Boolean selecionado;
    private UnidadeEnsinoVO unidadeEnsinoTransferenciaInternaVO;
    private CursoVO cursoTransferenciaInternaVO;
    private TurnoVO turnoTransferenciaInternaVO;
    
 // Atributo utilizado apenas no relatório Requerimento por Responsável Sintético
    private Integer qtde;
    
    
    private SituacaoRequerimentoDepartamentoVO situacaoRequerimentoDepartamentoVO;
    private SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum situacaoIsencaoTaxa;
    private ArquivoVO comprovanteSolicitacaoIsencao;
    private String justificativaSolicitacaoIsencao;
    private String motivoDeferimentoIndeferimentoIsencaoTaxa;
    private UsuarioVO responsavelDeferimentoIndeferimentoIsencaoTaxa;
    private Date dataDeferimentoIndeferimentoIsencaoTaxa;
     /**
      * Campos Usados Para Requerimento de Reposicao
      */    
    private MatriculaPeriodoVO matriculaPeriodoVO;
    private Boolean disciplinaPorEquivalencia;
    private MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO;
    private MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO;
    private Date dataInicioAula;
    private Date dataTerminoAula;
    private Integer cargaHorariaDisciplina;
    private String mensagemChoqueHorario;
    /**
     * Transiente
     */
    @Expose(serialize = false, deserialize = false)
    private Boolean realizarCobrancaBaseNaUltimaAula;
       
    /**
     * Transiente, usado no relatorio de requerimento.
     */

    private String coordenador;
    private String nomeResponsavel;
    private Boolean possuiInteracaoAtendenteNaoLida;
    private Boolean possuiInteracaoRequerenteNaoLida;
    private Boolean departamentoPodeInserirNota;

    
    private Date dataEntradaDepartamento;    
    private String responsavelTramite;    
    private String departamentoResponsavelTramite;
    private String motivoNaoAceiteCertificado;
    private String formatoCertificadoSelecionado;
    private List<EstadoVO> listaEstado;
    private List<DisciplinaVO> listaDisciplina;
    private List<UnidadeEnsinoVO> listaUnidadeEnsino;
    private Boolean requerimentoPermitirRequerenteAnexarArquivo;
    private Boolean exigePagamento;
    private Boolean somenteAluno;
    private String justificativaTrancamento;
    private MotivoCancelamentoTrancamentoVO motivoCancelamentoTrancamento;
    private List<RequerimentoDisciplinaVO> requerimentoDisciplinaVOs;
    private RequerimentoDisciplinasAproveitadasVO requerimentoDisciplinasAproveitadasVO;
    private SalaAulaBlackboardVO grupoFacilitador;
    private String temaTccFacilitador;
    private String assuntoTccFacilitador;
    private String avaliadorExternoFacilitador;
    private String cid;
    private Integer vagaPorCursoDiaSemana;
    private Integer vagaPorUnidadeEnsinoDiaSemana;
	/**
	 * Construtor padrão da classe <code>Requerimento</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public RequerimentoVO() {
		super();
		inicializarDados();
	}

	public static void validarRecebimentoDoc(RequerimentoVO obj) throws ConsistirException {
		if (obj.getCodigo().equals(0)) {
			throw new ConsistirException("Requerimento deve estar gravado.");
		}
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>RequerimentoVO</code>. Todos os tipos de consistência de dados são
	 * e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(RequerimentoVO obj) throws ConsistirException {
		if ((obj.getUnidadeEnsino() == null) || (obj.getUnidadeEnsino().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo UNIDADE ENSINO (Requerimento) deve ser informado.");
		}
		if ((obj.getTipoRequerimento() == null) || (obj.getTipoRequerimento().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo TIPO REQUERIMENTO (Requerimento) deve ser informado.");
		}
		if(obj.getTipoPessoaAluno()){
			if(obj.getMatricula().getMatricula().trim().isEmpty()|| (obj.getPessoa() == null || obj.getPessoa().getCodigo().intValue() == 0)){
				throw new ConsistirException("O campo MATRÍCULA (Requerimento) deve ser informado.");
			} 			 			
		}else if (!obj.getTipoPessoaAluno() && (obj.getPessoa() == null || obj.getPessoa().getCodigo().intValue() == 0) && (obj.getCpfRequerente().equals("") || obj.getNomeRequerente().equals(""))) {
			throw new ConsistirException("O campo REQUISITANTE OU CPF REQUERENTE/NOME REQUERENTE (Requerimento) deve ser informado.");
		}
		if ((obj.getTipoRequerimento().getTipo().equals("FO") || obj.getTipoRequerimento().getTipo().equals("CA") || obj.getTipoRequerimento().getTipo().equals("TR") || obj.getTipoRequerimento().getTipo().equals("TS") || obj.getTipoRequerimento().getTipo().equals("TI")) && ((obj.getMatricula() == null) || (obj.getMatricula().getMatricula().equals("")))) {
			throw new ConsistirException("O campo Matricula (Requerimento) deve ser informado para este tipo de requerimento.");
		}
		if (obj.getData() == null) {
			throw new ConsistirException("O campo DATA (Requerimento) deve ser informado.");
		}
		if (obj.getDataPrevistaFinalizacao() == null) {
			throw new ConsistirException("O campo DATA PREVISTA FINALIZAÇÃO (Requerimento) deve ser informado.");
		}
		if (obj.getSituacao().equals("")) {
			throw new ConsistirException("O campo SITUAÇÃO (Requerimento) deve ser informado.");
		}
		if (obj.getSituacaoFinanceira().equals("")) {
			throw new ConsistirException("O campo SITUAÇÃO FINANCEIRA (Requerimento) deve ser informado.");
		}
		if ((obj.getDepartamentoResponsavel() == null) || (obj.getDepartamentoResponsavel().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo DEPARTAMENTO RESPONSÁVEL (Requerimento) deve ser informado.");
		}
		if(obj.getTipoRequerimento().getTipo().equals("TC")) {
			if(!Uteis.isAtributoPreenchido(obj.getTipoTrabalhoConclusaoCurso())){
				throw new ConsistirException("O campo TIPO TCC (Requerimento) deve ser informado.");
			}
			if(!Uteis.isAtributoPreenchido(obj.getTituloMonografia())){
				if(obj.getTipoTrabalhoConclusaoCurso().equals("AR")) {
					throw new ConsistirException("O campo TÍTULO ARTIGO  (Requerimento) deve ser informado.");
				}else {
					throw new ConsistirException("O campo TÍTULO MONOGRAFIA  (Requerimento) deve ser informado.");
				}
			}
		}
		/*
		if (obj.getTipoRequerimento().getPermitirUploadArquivo() && obj.getArquivoVO().getNome().trim().isEmpty()) {
			throw new ConsistirException("Deve ser realizado o Upload da Foto!");
		}*/
		if (obj.getTipoRequerimento().getPermitirInformarEnderecoEntrega() && obj.getCEP().trim().equals("")) {
			throw new ConsistirException("O campo CEP (Requerimento) deve ser informado.");
		}
		if (obj.getTipoRequerimento().getPermitirInformarEnderecoEntrega() && obj.getEndereco().trim().equals("")) {
			throw new ConsistirException("O campo ENDEREÇO (Requerimento) deve ser informado.");
		}
		if (obj.getTipoRequerimento().getPermitirInformarEnderecoEntrega() && obj.getSetor().trim().equals("")) {
			throw new ConsistirException("O campo SETOR (Requerimento) deve ser informado.");
		}
		if (obj.getTipoRequerimento().getPermitirInformarEnderecoEntrega() && obj.getCidade().getCodigo() == 0) {
			throw new ConsistirException("O campo CIDADE (Requerimento) deve ser informado.");
		}
		if (TiposRequerimento.TRANSF_INTERNA.getValor().equals(obj.getTipoRequerimento().getTipo())) {
			if (!Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoTransferenciaInternaVO())) {
				throw new ConsistirException("O campo UNIDADE DE ENSINO TRANSFERÊNCIA INTERNA (Requerimento) deve ser informado.");
			}
			if (!Uteis.isAtributoPreenchido(obj.getCursoTransferenciaInternaVO())) {
				throw new ConsistirException("O campo CURSO TRANSFERÊNCIA INTERNA (Requerimento) deve ser informado.");
			}
			if (!Uteis.isAtributoPreenchido(obj.getTurnoTransferenciaInternaVO())) {
				throw new ConsistirException("O campo TURNO TRANSFERÊNCIA INTERNA (Requerimento) deve ser informado.");
			}
			if(obj.getMatricula().getUnidadeEnsino().getCodigo().equals(obj.getUnidadeEnsinoTransferenciaInternaVO().getCodigo())
					&& obj.getMatricula().getCurso().getCodigo().equals(obj.getCursoTransferenciaInternaVO().getCodigo())) {
				throw new ConsistirException("O campo UNIDADE DE ENSINO TRANSFERÊNCIA INTERNA ou CURSO TRANSFERÊNCIA INTERNA (Requerimento) deve ser diferente da matrícula.");
			}
		}
		if(obj.getTipoRequerimento().getIsPermiteInformarDisciplina() && !Uteis.isAtributoPreenchido(obj.getDisciplina()) && !obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.SEGUNDA_CHAMADA.getValor())) {
			throw new ConsistirException("O campo DISCIPLINA (Requerimento) deve ser informado.");
		}
		if(!Uteis.isAtributoPreenchido(obj.getRequerimentoDisciplinaVOs()) && obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.SEGUNDA_CHAMADA.getValor())) {
			throw new ConsistirException("Deve ser adicionado ao menos uma DISCIPLINA.");
		}
		if(obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.SEGUNDA_CHAMADA.getValor())) {
			for(RequerimentoDisciplinaVO t :  obj.getRequerimentoDisciplinaVOs()) {
				if(t.getSituacao().equals(SituacaoRequerimentoDisciplinasAproveitadasEnum.INDEFERIDO) && t.getMotivoIndeferimento().trim().isEmpty()) {
					throw new ConsistirException("O campo MOTIVO INDEFERIMENTO da disciplina "+t.getDisciplina().getNome().toUpperCase()+" deve ser informado.");
				}
			};			
		}
		if(obj.getTipoRequerimento().getIsPermiteInformarDisciplina() && obj.getTipoRequerimento().getIsTipoReposicao() && obj.getTipoRequerimento().getPermiteIncluirDisciplinaPorEquivalencia() && obj.getDisciplinaPorEquivalencia() && !Uteis.isAtributoPreenchido(obj.getMapaEquivalenciaDisciplinaCursadaVO())) {
			throw new ConsistirException("O campo DISCIPLINA EQUIVALENTE (Requerimento) deve ser informado.");
		}
		if(obj.getTipoRequerimento().getIsTipoReposicao() && obj.getTipoRequerimento().getDeferirAutomaticamente() && !Uteis.isAtributoPreenchido(obj.getTurmaReposicao())) {
			throw new ConsistirException("O campo TURMA REPOSIÇÃO (Requerimento) deve ser informado.");
		}
		if (obj.getTipoRequerimento().getPermitirUploadArquivo() && obj.getTipoRequerimento().getUploadArquivoObrigatorio() && obj.getArquivoVO().getNome().trim().isEmpty()) {
			throw new ConsistirException("Deve ser informado o ANEXO ao requerimento!");
		}
		if (obj.getTipoRequerimento().getIsTipoAproveitamentoDisciplina() && obj.getListaRequerimentoDisciplinasAproveitadasVOs().isEmpty()) {
			throw new ConsistirException("ERRO: Ainda há campos obrigatórios que não foram preenchidos.");
		}
		if (obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.TRANCAMENTO.getValor()) && !Uteis.isAtributoPreenchido(obj.getJustificativaTrancamento())) {
			throw new ConsistirException("O campo JUSTIFICATIVA TRANCAMENTO deve ser informado.");
		}
		if (obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.TRANCAMENTO.getValor()) && !Uteis.isAtributoPreenchido(obj.getMotivoCancelamentoTrancamento())) {
			throw new ConsistirException("O campo MOTIVO TRANCAMENTO deve ser informado.");
		}
		
		if (obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.CANCELAMENTO.getValor()) && !Uteis.isAtributoPreenchido(obj.getJustificativaTrancamento())) {
			throw new ConsistirException("O campo JUSTIFICATIVA CANCELAMENTO deve ser informado.");
		}
		if (obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.CANCELAMENTO.getValor()) && !Uteis.isAtributoPreenchido(obj.getMotivoCancelamentoTrancamento())) {
			throw new ConsistirException("O campo MOTIVO CANCELAMENTO deve ser informado.");
		}
		if (obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.CERTIFICADO_PARTICIPACAO_TCC.getValor())) {
			if (!Uteis.isAtributoPreenchido(obj.getGrupoFacilitador())) {
				throw new ConsistirException("O campo GRUPO DE TCC deve ser informado.");
			}
			if (!Uteis.isAtributoPreenchido(obj.getTemaTccFacilitador())) {
				throw new ConsistirException("O campo TEMA DO TCC deve ser informado.");
			}
			if (!Uteis.isAtributoPreenchido(obj.getAssuntoTccFacilitador())) {
				throw new ConsistirException("O campo ASSUNTO DO TCC deve ser informado.");
			}
			if (!Uteis.isAtributoPreenchido(obj.getAvaliadorExternoFacilitador())) {
				throw new ConsistirException("O campo AVALIADOR EXTERNO deve ser informado.");
			}
		}
		if (obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.OUTROS.getValor()) && obj.getTipoRequerimento().getCampoAfastamento()) {
			if (obj.getTipoRequerimento().getCampoAfastamento() && (!Uteis.isAtributoPreenchido(obj.getDataAfastamentoInicio()) && !Uteis.isAtributoPreenchido(obj.getDataAfastamentoFim()))) {
				throw new ConsistirException("O campo Período Afastamento - Início e/ou Período Afastamento - Fim deve ser informado.");
			}
			if (Uteis.isAtributoPreenchido(obj.getDataAfastamentoFim())) {
				if( obj.getDataAfastamentoInicio().after(obj.getDataAfastamentoFim())) {
					throw new ConsistirException("Período Afastamento - FIM deve ser maior que Período Afastamento - Início.");
				}
			} else {
				throw new ConsistirException("Período Afastamento - FIM deve ser infoarmado.");
			}
		}
			
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(0);
		setData(new Date());
		setValor(0.0);
		setSituacao("PE");
		setSituacaoFinanceira("IS");
		setNomeRequerente("");
		setCpfRequerente("");
		setObservacao("");
		setContaReceber(0);
		setNrDocumento("");
		setTrocouTipoRequerimento(Boolean.TRUE);
	}

	// public ContaReceberVO emitirBoletoParcela(Integer contaCorrente) throws
	// Exception {
	// ContaReceberVO contaReceberVO = new ContaReceberVO();
	// contaReceberVO.setCentroReceita(getCentroReceita());
	// contaReceberVO.setContaCorrente(contaCorrente);
	// contaReceberVO.getUnidadeEnsino().setCodigo(getUnidadeEnsino().getCodigo());
	// contaReceberVO.setData(new Date());
	// contaReceberVO.setValor(getTipoRequerimento().getValor());
	// contaReceberVO.setDataVencimento(new Date());
	// contaReceberVO.setMatriculaAluno(getMatricula());
	// contaReceberVO.setSituacao("AR");
	// if (getMatricula().getMatricula().equals("")) {
	// contaReceberVO.setTipoPessoa("RE");
	// } else {
	// contaReceberVO.setTipoPessoa("AL");
	// contaReceberVO.getMatriculaAluno().setAluno(getPessoa());
	// }
	// contaReceberVO.setParcela("1/1");
	// contaReceberVO.setCodOrigem(String.valueOf(getCodigo()));
	// contaReceberVO.setTipoOrigem("REQ");
	// contaReceberVO.setPessoa(getPessoa());
	// contaReceberVO.setDescricaoPagamento("Taxa do Requerimento (" +
	// getTipoRequerimento().getNome() + ")");
	// contaReceberVO.setNrDocumento(String.valueOf(getCodigo()));
	// contaReceberVO.setTipoBoleto(TipoBoletoBancario.REQUERIMENTO.getValor());
	// contaReceberVO.setUnidadeEnsino(getUnidadeEnsino());
	// return contaReceberVO;
	// }
	

	public Boolean getExisteTipoRequerimento() {
		if (getTipoRequerimento().getCodigo().intValue() != 0) {
			return true;
		}
		return false;
	}

	public Boolean getExisteMatricula() {
		if (getMatricula().getMatricula().equals("")) {
			return false;
		}
		return true;
	}

	@XmlElement(name = "codigoContaReceber")
	public Integer getContaReceber() {
		return contaReceber;
	}

	public void setContaReceber(Integer contaReceber) {
		this.contaReceber = contaReceber;
	}

	public String getNrDocumento() {
		return nrDocumento;
	}

	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}

	public Boolean getTrocouTipoRequerimento() {
		return trocouTipoRequerimento;
	}

	public void setTrocouTipoRequerimento(Boolean trocouTipoRequerimento) {
		this.trocouTipoRequerimento = trocouTipoRequerimento;
	}

	/**
	 * Retorna o objeto da classe <code>Matricula</code> relacionado com (
	 * <code>Requerimento</code>).
	 */
	@XmlElement(name = "matricula")
	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return (matricula);
	}

	/**
	 * Define o objeto da classe <code>Matricula</code> relacionado com (
	 * <code>Requerimento</code>).
	 */
	public void setMatricula(MatriculaVO obj) {
		this.matricula = obj;
	}



	
	/**
	 * Retorna o objeto da classe <code>Departamento</code> relacionado com (
	 * <code>TipoRequerimento</code>).
	 */
	@XmlElement(name = "departamentoResponsavel")
	public DepartamentoVO getDepartamentoResponsavel() {
		if (departamentoResponsavel == null) {
			departamentoResponsavel = new DepartamentoVO();
		}
		return (departamentoResponsavel);
	}

	/**
	 * Define o objeto da classe <code>Departamento</code> relacionado com (
	 * <code>TipoRequerimento</code>).
	 */
	public void setDepartamentoResponsavel(DepartamentoVO obj) {
		this.departamentoResponsavel = obj;
	}

	/**
	 * Retorna o objeto da classe <code>TipoRequerimento</code> relacionado com
	 * (<code>Requerimento</code>).
	 */
	@XmlElement(name = "tipoRequerimento")
	public TipoRequerimentoVO getTipoRequerimento() {
		if (tipoRequerimento == null) {
			tipoRequerimento = new TipoRequerimentoVO();
		}
		return (tipoRequerimento);
	}

	/**
	 * Define o objeto da classe <code>TipoRequerimento</code> relacionado com (
	 * <code>Requerimento</code>).
	 */
	public void setTipoRequerimento(TipoRequerimentoVO obj) {
		this.tipoRequerimento = obj;
	}

	@XmlElement(name = "situacaoFinanceira")
	public String getSituacaoFinanceira() {
		return (situacaoFinanceira);
	}

	public Boolean getIsRequerimentoPago() {
		return (getSituacaoFinanceira().equals("PG") || getSituacaoFinanceira().equals("IS"));
	}

	public Boolean getIsIndeferido() {
		return getSituacao().equals("FI");
	}
	
	public Boolean getIsApresentarModalDeCobranca() {
		return getTipoRequerimento().getTipo().equals("TC") && getTipoRequerimento().getQtdDiasCobrarTaxa() !=0  && getRealizarCobrancaBaseNaUltimaAula() && !getIsRequerimentoPago();
	}

	/**
	 * Operação responsável por retornar o valor de apresentação de um atributo
	 * com um domínio específico. Com base no valor de armazenamento do atributo
	 * esta função é capaz de retornar o de apresentação correspondente. Útil
	 * para campos como sexo, escolaridade, etc.
	 */
	@XmlElement(name = "situacaoFinanceira_Apresentar")
	public String getSituacaoFinanceira_Apresentar() {
		String situacaoApp = situacaoFinanceira;
		if (situacaoFinanceira.equals("PG")) {
			situacaoApp = "Pago";
		}else if (situacaoFinanceira.equals("CA")) {
				situacaoApp = "Cancelado Financeiramente";
		}else		if (situacaoFinanceira.equals("AP")) {
			situacaoApp =  "Aguardando Autorização Pagamento";
		}else		if (situacaoFinanceira.equals("PE")) {
			situacaoApp =  "Pendente";
		}else		if (situacaoFinanceira.equals("IS")) {
			situacaoApp =  "Isento";
		}
		if(getSituacaoIsencaoTaxa().equals(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.DEFERIDO)) {
			situacaoApp += " Por Solicitação";
		}else if(getAguardandoDeferimentoIndeferimentoSolicitacaoIsencaoTaxa()) { 
			situacaoApp += " (Com Solicitação Isenção)";
		}				
		return (situacaoApp);
	}

	public void setSituacaoFinanceira(String situacaoFinanceira) {
		this.situacaoFinanceira = situacaoFinanceira;
	}

	@XmlElement(name = "situacao")
	public String getSituacao() {
		return (situacao);
	}

	/**
	 * Operação responsável por retornar o valor de apresentação de um atributo
	 * com um domínio específico. Com base no valor de armazenamento do atributo
	 * esta função é capaz de retornar o de apresentação correspondente. Útil
	 * para campos como sexo, escolaridade, etc.
	 */
	@XmlElement(name = "situacaoApresentar")
	public String getSituacao_Apresentar() {
		return SituacaoRequerimento.getDescricao(getSituacao());
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>situacao</code>
	 */
	public List getListaSelectItemSituacaoRequerimento() throws Exception {
		List objs = new ArrayList();
		objs.add(new SelectItem("", ""));
		Hashtable situacaoProtocolos = (Hashtable) Dominios.getSituacaoProtocolo();
		Enumeration keys = situacaoProtocolos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoProtocolos.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	public List getListaSelectItemSituacaoFinalRequerimento() throws Exception {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoRequerimento.class);
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	@XmlElement(name = "dataFinalizacao")
	public Date getDataFinalizacao() {
	    return dataFinalizacao;
	}
	
	
	
	@XmlElement(name = "dataVencimentoContaReceber")
	public Date getDataVencimentoContaReceber() {		
		return dataVencimentoContaReceber;
	}


	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	public String getDataFinalizacao_Apresentar() {
		return (Uteis.getData(dataFinalizacao));
	}

	public void setDataFinalizacao(Date dataFinalizacao) {
		this.dataFinalizacao = dataFinalizacao;
	}

	@XmlElement(name = "dataPrevistaFinalizacao")
	public Date getDataPrevistaFinalizacao() {
		return (dataPrevistaFinalizacao);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	@XmlElement(name = "dataPrevistaFinalizacao_Apresentar")
	public String getDataPrevistaFinalizacao_Apresentar() {
		return (Uteis.getData(dataPrevistaFinalizacao));
	}

	public void setDataPrevistaFinalizacao(Date dataPrevistaFinalizacao) {
		this.dataPrevistaFinalizacao = dataPrevistaFinalizacao;
	}
	
	@XmlElement(name = "questionarioVO")
	public QuestionarioVO getQuestionarioVO() {
		if (questionarioVO == null) {
			questionarioVO = new QuestionarioVO();
		}
		return questionarioVO;
	}

	public void setQuestionarioVO(QuestionarioVO questionarioVO) {
		this.questionarioVO = questionarioVO;
	}


	@XmlElement(name = "valor")
	public Double getValor() {
		return (valor);
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	@XmlElement(name = "data")	
	public Date getData() {
		return (data);
	}

	

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	@XmlElement(name = "dataApresentar")
	public String getData_Apresentar() {
		return (Uteis.getDataComHora(data));
	}

	public void setData(Date data) {
		this.data = data;
	}

	@XmlElement(name = "codigo") 
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}	
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getCpfRequerente() {
		return cpfRequerente;
	}

	public void setCpfRequerente(String cpfRequerente) {
		this.cpfRequerente = cpfRequerente;
	}

	public String getNomeRequerente() {
		return nomeRequerente;
	}

	public void setNomeRequerente(String nomeRequerente) {
		this.nomeRequerente = nomeRequerente;
	}

	@XmlElement(name = "observacao")
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Date getDataRecebimentoDocRequerido() {
		return dataRecebimentoDocRequerido;
	}

	public void setDataRecebimentoDocRequerido(Date dataRecebimentoDocRequerido) {
		this.dataRecebimentoDocRequerido = dataRecebimentoDocRequerido;
	}

	public Date getDataEmissaoBoleto() {
		return dataEmissaoBoleto;
	}

	public void setDataEmissaoBoleto(Date dataEmissaoBoleto) {
		this.dataEmissaoBoleto = dataEmissaoBoleto;
	}

	public UsuarioVO getResponsavelEmissaoBoleto() {
		if (responsavelEmissaoBoleto == null) {
			responsavelEmissaoBoleto = new UsuarioVO();
		}
		return responsavelEmissaoBoleto;
	}

	public void setResponsavelEmissaoBoleto(UsuarioVO responsavelEmissaoBoleto) {
		this.responsavelEmissaoBoleto = responsavelEmissaoBoleto;
	}

	public UsuarioVO getResponsavelRecebimentoDocRequerido() {
		if (responsavelRecebimentoDocRequerido == null) {
			responsavelRecebimentoDocRequerido = new UsuarioVO();
		}
		return responsavelRecebimentoDocRequerido;
	}

	public void setResponsavelRecebimentoDocRequerido(UsuarioVO responsavelRecebimentoDocRequerido) {
		this.responsavelRecebimentoDocRequerido = responsavelRecebimentoDocRequerido;
	}

	@XmlElement(name = "unidadeEnsino")
	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	@XmlElement(name = "pessoa")
	public PessoaVO getPessoa() {
		if (pessoa == null) {
			pessoa = new PessoaVO();
		}
		return pessoa;
	}

	public void setPessoa(PessoaVO pessoa) {
		this.pessoa = pessoa;
	}

	public boolean getIsApresentarIconeDentroPrazo() {
		return ((getSituacao().equals("EX") || getSituacao().equals("AP") || getSituacao().equals("PR") || getSituacao().equals("PE")) && (getDataPrevistaFinalizacao().after(new Date())) ||  Uteis.getData(getDataPrevistaFinalizacao()).equals(Uteis.getData(new Date())));
	}

	public boolean getIsApresentarIconeAtrasado() {
		return ((getSituacao().equals("EX") || getSituacao().equals("AP") || getSituacao().equals("PR") || getSituacao().equals("PE")) && getDataPrevistaFinalizacao().before(new Date())) && !Uteis.getData(getDataPrevistaFinalizacao()).equals(Uteis.getData(new Date()));
	}

	public boolean getIsApresentarIconeFinalizado() {
		return (getSituacao().equals("FI") || getSituacao().equals("FD"));
	}

	/**
	 * @return the isApresentarIconeEmAlerta
	 */
	public Boolean getIsApresentarIconeEmAlerta() {
		if (isApresentarIconeEmAlerta == null) {
			isApresentarIconeEmAlerta = Boolean.FALSE;
		}
		return isApresentarIconeEmAlerta;
	}

	/**
	 * @param isApresentarIconeEmAlerta
	 *            the isApresentarIconeEmAlerta to set
	 */
	public void setIsApresentarIconeEmAlerta(Boolean isApresentarIconeEmAlerta) {
		this.isApresentarIconeEmAlerta = isApresentarIconeEmAlerta;
	}

	@XmlElement(name = "percDesconto")
	public Double getPercDesconto() {
		if (percDesconto == null) {
			percDesconto = 0.0;
		}
		return percDesconto;
	}

	public void setPercDesconto(Double percDesconto) {
		this.percDesconto = percDesconto;
	}

	@XmlElement(name = "valorDesconto")
	public Double getValorDesconto() {
		if (valorDesconto == null) {
			valorDesconto = 0.0;
		}
		return valorDesconto;
	}

	public void setValorDesconto(Double valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	@XmlElement(name = "tipoDesconto")
	public String getTipoDesconto() {
		if (tipoDesconto == null) {
			tipoDesconto = "PO";
		}
		return tipoDesconto;
	}

	public void setTipoDesconto(String tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}

	public boolean getApresentarCampoDesconto() {
		return getTipoDesconto().equals("PO");
	}



	public void setDataVencimentoContaReceber(Date dataVencimentoContaReceber) {
		this.dataVencimentoContaReceber = dataVencimentoContaReceber;
	}

	@XmlElement(name = "complemento")
	public String getComplemento() {
		if (complemento == null) {
			complemento = "";
		}
		return (complemento);
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	@XmlElement(name = "CEP")
	public String getCEP() {
		if (CEP == null) {
			CEP = "";
		}
		return (CEP);
	}

	public void setCEP(String CEP) {
		this.CEP = CEP;
	}

	@XmlElement(name = "numero")
	public String getNumero() {
		if (numero == null) {
			numero = "";
		}
		return (numero);
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@XmlElement(name = "setor")
	public String getSetor() {
		if (setor == null) {
			setor = "";
		}
		return (setor);
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	@XmlElement(name = "endereco")
	public String getEndereco() {
		if (endereco == null) {
			endereco = "";
		}
		return (endereco);
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	@XmlElement(name = "cidade")
	public CidadeVO getCidade() {
		if (cidade == null) {
			cidade = new CidadeVO();
		}
		return (cidade);
	}

	public void setCidade(CidadeVO obj) {
		this.cidade = obj;
	}

	@XmlElement(name = "arquivoVO")
	public ArquivoVO getArquivoVO() {
		if (arquivoVO == null) {
			arquivoVO = new ArquivoVO();
		}
		return arquivoVO;
	}

	public void setArquivoVO(ArquivoVO arquivoVO) {
		this.arquivoVO = arquivoVO;
	}

	public Boolean getExcluirArquivo() {
		if (excluirArquivo == null) {
			excluirArquivo = false;
		}
		return excluirArquivo;
	}

	public void setExcluirArquivo(Boolean excluirArquivo) {
		this.excluirArquivo = excluirArquivo;
	}

	public boolean getIsPossuiArquivo() {
		return !getArquivoVO().getNome().equals("");
	}

	public boolean getIsPossuiArquivoBanco() {
		return !getArquivoVO().getCodigo().equals(0);
	}

	public String getStylePorSituacao() {
		if (getSituacao().equals("PE")) {
			return "font-weight:bold; color: #040402;";
		}
		return "";
	}

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}

	public String getVisaoGerado() {
		if (visaoGerado == null) {
			visaoGerado = "";
		}
		return visaoGerado;
	}

	public void setVisaoGerado(String visaoGerado) {
		this.visaoGerado = visaoGerado;
	}

	@XmlElement(name = "valorAdicional")
	public Double getValorAdicional() {
		if (valorAdicional == null) {
			valorAdicional = 0.0;
		}
		return valorAdicional;
	}

	public void setValorAdicional(Double valorAdicional) {
		this.valorAdicional = valorAdicional;
	}

	@XmlElement(name = "motivoIndeferimento")
	public String getMotivoIndeferimento() {
		if (motivoIndeferimento == null) {
			motivoIndeferimento = "";
		}
		return motivoIndeferimento;
	}

	public void setMotivoIndeferimento(String motivoIndeferimento) {
		this.motivoIndeferimento = motivoIndeferimento;
	}

	/**
	 * @return the turma
	 */
	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return turma;
	}

	/**
	 * @param turma
	 *            the turma to set
	 */
	public void setTurma(TurmaVO turma) {
		this.turma = turma;
	}

	/**
	 * @return the disciplina
	 */
	@XmlElement(name = "disciplina")
	public DisciplinaVO getDisciplina() {
		if (disciplina == null) {
			disciplina = new DisciplinaVO();
		}
		return disciplina;
	}

	/**
	 * @param disciplina
	 *            the disciplina to set
	 */
	public void setDisciplina(DisciplinaVO disciplina) {
		this.disciplina = disciplina;
	}

	/**
	 * @return the turmaReposicao
	 */
	@XmlElement(name = "turmaReposicao")
	public TurmaVO getTurmaReposicao() {
		if (turmaReposicao == null) {
			turmaReposicao = new TurmaVO();
		}
		return turmaReposicao;
	}

	/**
	 * @param turmaReposicao
	 *            the turmaReposicao to set
	 */
	public void setTurmaReposicao(TurmaVO turmaReposicao) {
		this.turmaReposicao = turmaReposicao;
	}

	/**
	 * @return the qtdAlunosReposicaoTurma
	 */
	public Integer getQtdAlunosReposicaoTurma() {
		if (qtdAlunosReposicaoTurma == null) {
			qtdAlunosReposicaoTurma = 0;
		}
		return qtdAlunosReposicaoTurma;
	}

	/**
	 * @param qtdAlunosReposicaoTurma
	 *            the qtdAlunosReposicaoTurma to set
	 */
	public void setQtdAlunosReposicaoTurma(Integer qtdAlunosReposicaoTurma) {
		this.qtdAlunosReposicaoTurma = qtdAlunosReposicaoTurma;
	}

	/**
	 * @return the dataAulaReprovado
	 */
	public Date getDataAulaReprovado() {
		if (dataAulaReprovado == null) {
			dataAulaReprovado = new Date();
		}
		return dataAulaReprovado;
	}

	/**
	 * @param dataAulaReprovado
	 *            the dataAulaReprovado to set
	 */
	public void setDataAulaReprovado(Date dataAulaReprovado) {
		this.dataAulaReprovado = dataAulaReprovado;
	}

	public boolean getIsPermiteDeferir() {
		if ((!this.getNovoObj()) && (this.getSituacaoFinanceira().equals("PG") || this.getSituacaoFinanceira().equals("IS")) && (!getSituacao().equals("FD"))) {
			return true;
		}
		return false;
	}

	public boolean getIsPermiteInDeferir() {
		if ((!this.getNovoObj()) && (this.getSituacaoFinanceira().equals("PG") || this.getSituacaoFinanceira().equals("IS")) && (this.getSituacao().equals("EX") || this.getSituacao().equals("PE"))) {
			return true;
		}
		return false;
	}

	public String getDataAulaReprovado_Apresentar() {
		return (Uteis.getData(dataAulaReprovado));
	}

	/**
	 * @return the documentosPendentes
	 */
	public String getDocumentosPendentes() {
		if (documentosPendentes == null) {
			documentosPendentes = "";
		}
		return documentosPendentes;
	}

	/**
	 * @param documentosPendentes
	 *            the documentosPendentes to set
	 */
	public void setDocumentosPendentes(String documentosPendentes) {
		this.documentosPendentes = documentosPendentes;
	}

	@XmlElement(name = "notificado")
	public Boolean getNotificado() {
		if (notificado == null) {
			notificado = false;
		}
		return notificado;
	}

	public void setNotificado(Boolean notificado) {
		this.notificado = notificado;
	}

	public Double getCalcularValorDesconto() {

		if (getTipoDesconto().equals("PO")) {
			setValorDesconto(0.0);
			return Uteis.arrendondarForcando2CadasDecimais(getValor() * (getPercDesconto() / 100.0));
		} else {
			setPercDesconto(0.0);
			if (getValorDesconto() > (getValor() + getValorAdicional())) {
				setValorDesconto((getValor() + getValorAdicional()));
			}
			return getValorDesconto();
		}
	}

	@XmlElement(name = "valorTotalFinal")
	public Double getValorTotalFinal() {
		if (getTipoDesconto().equals("PO") && getPercDesconto() >= 100.0) {
			setPercDesconto(100.0);
			return 0.0;
		}

		return getValor() + getValorAdicional() - getCalcularValorDesconto();
	}

	/**
	 * Este método indica se o requerimento como um todo pode ser iniciado,
	 * independetemente do departamento ou se mesmo tramita entre os
	 * departamentos.
	 * 
	 * @return
	 */
	public boolean getPodeSerIniciadoExecucaoRequerimento() {
		if (this.getCodigo().equals(0)) {
			return false;
		}
		if ((!getSituacaoFinanceira().equals("PE")) && this.getSituacao().equals("PE")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean getPodeSerGravado() {
		if (this.getCodigo().equals(0)) {
			return true;
		}
		if (this.getSituacaoFinanceira().equals("PG")) { // || this.getSituacaoFinanceira().equals("IS")
			return false;
		} else {
			return true;
		}
	}

	public Boolean podeSerIniciadoExecucaoDepartamentoAtual;
	public void setPodeSerIniciadoExecucaoDepartamentoAtual(Boolean podeSerIniciadoExecucaoDepartamentoAtual) {
		this.podeSerIniciadoExecucaoDepartamentoAtual = podeSerIniciadoExecucaoDepartamentoAtual;
	}
	public Boolean getPodeSerIniciadoExecucaoDepartamentoAtual() {
		if(podeSerIniciadoExecucaoDepartamentoAtual == null) {
		if (this.getCodigo().equals(0)) {
			podeSerIniciadoExecucaoDepartamentoAtual = false;
		}else if (this.getOrdemExecucaoTramiteDepartamento().compareTo(1) <= 0) {
			// Se ordem de excucao nao for maior que 2 (segundo depto)
			// entao também este botao nao deve ser apresentado.
			podeSerIniciadoExecucaoDepartamentoAtual = false;
		}else {		
			RequerimentoHistoricoVO ultimoHistorico;
			try {
				ultimoHistorico = this.consultarUltimoRequerimentoHistoricoDepartamentoAtualVOs();
				if (ultimoHistorico != null && ultimoHistorico.getDataInicioExecucaoDepartamento() != null) {
					// Se o departamento no qual o tramite de execução está atualmente
					// já tiver iniciado a execução, também nao faz sentido apresentar
					// este botaõ. Somente o botão para enviar para o proximo depto.
					podeSerIniciadoExecucaoDepartamentoAtual = false;
				}else {
					podeSerIniciadoExecucaoDepartamentoAtual = true;
				}
			} catch (Exception e) {
				podeSerIniciadoExecucaoDepartamentoAtual = false;
			}
		}
		}
		return podeSerIniciadoExecucaoDepartamentoAtual;
	}

	public boolean getPodeSerDevolvidoDepartamentoAnterior() {
		if (this.getCodigo().equals(0)) {
			return false;
		}
		if (!getTipoRequerimento().getTramitaEntreDepartamentos()) {
			// Se nao utiliza o conceito de tramiar entre departamentos,
			// entao nao existe o conceito de encaminhamento
			return false;
		}
		if (this.getOrdemExecucaoTramiteDepartamento().compareTo(1) > 0) {
			// Se o departamento que estamos, nao é o primeiro departamento do
			// tramite
			// entao, temos que podemos devolver ou retornar no fluxo de tramite
			return true;
		}
		return false;
	}

	public boolean getPodeSerEncaminhadoProximoDepartamento() {
		if (this.getCodigo().equals(0)) {
			return false;
		}
		if (!getTipoRequerimento().getTramitaEntreDepartamentos()) {
			// Se nao utiliza o conceito de tramiar entre departamentos,
			// entao nao existe o conceito de encaminhamento
			return false;
		}
		int nrTramitesDepartamento = getTipoRequerimento().getTipoRequerimentoDepartamentoVOs().size();
		if ((this.getOrdemExecucaoTramiteDepartamento().compareTo(nrTramitesDepartamento) < 0) && (this.getSituacao().equals("EX"))) {
			// Se o departamento que estamos atualmente (indicado pelo campo
			// OrdemExecucaoTramiteDepartamento)
			// não é o ultimo que iremos tramitar, significa que ainda é
			// possível encaminha para frente
			// (ou seja, para o proximo departamento na ordem). Contudo, só
			// habilitamos para ir para o próximo
			// departamento, caso o requerimento já tenha sido iniciado (ou
			// seja, esteja em execuação).
			// Forçando assim o usuário a iniciar o requerimento antes de
			// encaminhá-lo para frente.
			return true;
		}
		return false;
	}

	public boolean getPodeSerDeferidoDepartamentoAtual() {
		if (this.getCodigo().equals(0)) {
			return false;
		}
		if ((!this.getSituacaoFinanceira().equals("PG") && !this.getSituacaoFinanceira().equals("IS")) && ((getAguardandoAutorizacaoPagamento() && !getTipoRequerimento().getPermiteDeferirAguardandoAutorizacaoPagamento()) || !getAguardandoAutorizacaoPagamento())) {
			return false;
		}
		if ((this.getSituacao().equals("FD")) || (this.getSituacao().equals("FI"))) {
			return false;
		}
		if (!getTipoRequerimento().getTramitaEntreDepartamentos()) {
			// Se o requerimento não tramita entre departamento e está pago ou
			// isento
			// o mesmo pode ser deferido / finalizado com sucesso.
			if (this.getSituacao().equals("PE")) {
				return false;
			} else {
				return true;
			}
		} else {
			// Se o requerimento tramita entre vários departamentos, somente o
			// último departamento
			// poderá autorizar/finalizar. Os demais departamentos, na verdade
			// encaminham para o
			// proximo departamento no tramite.
			int nrTramitesDepartamento = getTipoRequerimento().getTipoRequerimentoDepartamentoVOs().size();
			if (this.getOrdemExecucaoTramiteDepartamento().equals(nrTramitesDepartamento)) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getPodeSerIndeferidoDepartamentoAtual() {
		if (this.getCodigo().equals(0)) {
			return false;
		}
		if ((!this.getSituacaoFinanceira().equals("PG")) && (!this.getSituacaoFinanceira().equals("IS"))  && (!this.getSituacaoFinanceira().equals("AP"))) {
			return false;
		}
		if ((this.getSituacao().equals("FD")) || (this.getSituacao().equals("FI"))) {
			return false;
		}
		if (!getTipoRequerimento().getTramitaEntreDepartamentos()) {
			if (this.getSituacao().equals("PE")) {
				return false;
			} else {
				return true;
			}
		} else {
			// Se tramita entre departamento, temos que avaliar se o
			// departamento no qual
			// requerimento está atualmente, tem permissao para indeferir o
			// mesmo.
			if (getTipoRequerimento().getTipoRequerimentoDepartamentoVOs().size() < (this.getOrdemExecucaoTramiteDepartamento())) {
				this.setOrdemExecucaoTramiteDepartamento(getTipoRequerimento().getTipoRequerimentoDepartamentoVOs().size());
			}
			TipoRequerimentoDepartamentoVO dtpoAtualTramite = getTipoRequerimento().getTipoRequerimentoDepartamentoVOs().get(this.getOrdemExecucaoTramiteDepartamento() - 1);
			if (this.getSituacao().equals("PE")) {
				return false;
			} else {
				return dtpoAtualTramite.getPodeIndeferirRequerimento();
			}
		}
	}

	/**
	 * @return the ordemExecucaoTramiteDepartamento
	 */
	
	@XmlElement(name = "ordemExecucaoTramiteDepartamento")
	public Integer getOrdemExecucaoTramiteDepartamento() {
		// Este controle parte da posicao 1, caso esteja zero assumimos 1, para
		// que requerimento ja
		// existentes na base de dados dos clientes possam funcionar.
		if ((ordemExecucaoTramiteDepartamento == null) || (ordemExecucaoTramiteDepartamento == 0)) {
			ordemExecucaoTramiteDepartamento = 1;
		}
		return ordemExecucaoTramiteDepartamento;
	}

	/**
	 * @param ordemExecucaoTramiteDepartamento
	 *            the ordemExecucaoTramiteDepartamento to set
	 */
	public void setOrdemExecucaoTramiteDepartamento(Integer ordemExecucaoTramiteDepartamento) {
		this.ordemExecucaoTramiteDepartamento = ordemExecucaoTramiteDepartamento;
	}

	/**
	 * @return the requerimentoHistoricoVOs
	 */
	@XmlElement(name = "requerimentoHistoricoVOs")
	public List<RequerimentoHistoricoVO> getRequerimentoHistoricoVOs() {
		if (requerimentoHistoricoVOs == null) {
			requerimentoHistoricoVOs = new ArrayList<RequerimentoHistoricoVO>(0);
		}
		return requerimentoHistoricoVOs;
	}

	/**
	 * @param requerimentoHistoricoVOs
	 *            the requerimentoHistoricoVOs to set
	 */
	public void setRequerimentoHistoricoVOs(List<RequerimentoHistoricoVO> requerimentoHistoricoVOs) {
		this.requerimentoHistoricoVOs = requerimentoHistoricoVOs;
	}

	public RequerimentoHistoricoVO consultarUltimoRequerimentoHistoricoDepartamentoAtualVOs() throws Exception {
		if (this.getRequerimentoHistoricoVOs().isEmpty()) {
			return new RequerimentoHistoricoVO();
		}
		Ordenacao.ordenarLista(getRequerimentoHistoricoVOs(), "codigo");
		RequerimentoHistoricoVO ultimoObjExistente = getRequerimentoHistoricoVOs().get(getRequerimentoHistoricoVOs().size() - 1);
		if (!ultimoObjExistente.getDepartamento().getCodigo().equals(getDepartamentoResponsavel().getCodigo())) { // AQUI_LEMBRAR
			throw new Exception("O último histórico de trâmite do departamento está registrado para um departamento diferente do departamento atual do Requerimento.");
		}
		return ultimoObjExistente;
	}
	
	public boolean isExisteRequerimentoDisciplinaDeferido() {
		return getListaRequerimentoDisciplinasAproveitadasVOs().stream().anyMatch(p-> p.getSituacaoRequerimentoDisciplinasAproveitadasEnum().isDeferido());
	}

	public List<RequerimentoDisciplinasAproveitadasVO> getListaRequerimentoDisciplinasAproveitadasVOs() {
		if (listaRequerimentoDisciplinasAproveitadasVOs == null) {
			listaRequerimentoDisciplinasAproveitadasVOs = new ArrayList<>();
		}
		return listaRequerimentoDisciplinasAproveitadasVOs;
	}

	public void setListaRequerimentoDisciplinasAproveitadasVOs(List<RequerimentoDisciplinasAproveitadasVO> listaRequerimentoDisciplinasAproveitadasVOs) {
		this.listaRequerimentoDisciplinasAproveitadasVOs = listaRequerimentoDisciplinasAproveitadasVOs;
	}

	private Boolean existeQuestionarioDepartamentoAtual;

//	public Boolean getExisteQuestionarioDepartamentoAtual() throws Exception {
//		if (existeQuestionarioDepartamentoAtual == null) {
//			existeQuestionarioDepartamentoAtual = false;
//			for (RequerimentoHistoricoVO reqHis : getRequerimentoHistoricoVOs()) {
//				if (reqHis.getDepartamento().getCodigo().equals(getDepartamentoResponsavel().getCodigo()) && reqHis.getOrdemExecucaoTramite().equals(getOrdemExecucaoTramiteDepartamento())) {
//					existeQuestionarioDepartamentoAtual = reqHis.getQuestionario().getCodigo() > 0;
//					break;
//				}
//			}
//		}
//		return existeQuestionarioDepartamentoAtual;
//	}

	public void setExisteQuestionarioDepartamentoAtual(Boolean existeQuestionarioDepartamentoAtual) {
		this.existeQuestionarioDepartamentoAtual = existeQuestionarioDepartamentoAtual;
	}

	public void gerarNovoRequerimentoHistoricoVODepartamentoAtualTramite(UsuarioVO usuarioLogado, Boolean iniciarExecucao, FuncionarioVO funcionarioVO, TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO, Boolean retorno, String motivoRetorno) throws Exception {

		RequerimentoHistoricoVO historicoVO = new RequerimentoHistoricoVO();
		historicoVO.setRequerimento(this.getCodigo());
		historicoVO.setDataEntradaDepartamento(new Date());
		if (iniciarExecucao) {
			historicoVO.setDataInicioExecucaoDepartamento(historicoVO.getDataEntradaDepartamento());
		}
		historicoVO.setDepartamento(tipoRequerimentoDepartamentoVO.getDepartamento());
		historicoVO.setResponsavelRequerimentoDepartamento(funcionarioVO.getPessoa());
//		historicoVO.setQuestionario(tipoRequerimentoDepartamentoVO.getQuestionario());
		historicoVO.setOrdemExecucaoTramite(tipoRequerimentoDepartamentoVO.getOrdemExecucao());
		historicoVO.setRetorno(retorno);
		historicoVO.setMotivoRetorno(motivoRetorno);
		historicoVO.setPodeInserirNota(tipoRequerimentoDepartamentoVO.getPodeInserirNota());
		this.adicionarRequerimentoHistoricoVOs(historicoVO);
	}

	public String getNomeProximoDepartamentoTramite() {
		try {
			if (this.getTipoRequerimento().getTramitaEntreDepartamentos()) {
				int nrProximo = this.getOrdemExecucaoTramiteDepartamento() + 1;
				TipoRequerimentoDepartamentoVO objProx = this.getTipoRequerimento().consultarTipoRequerimentoDepartamentoVOs(nrProximo);
				if (objProx != null) {
					return objProx.getDepartamento().getNome();
				}
			}
		} catch (Exception e) {
		}
		return "";
	}

	public void adicionarRequerimentoHistoricoVOs(RequerimentoHistoricoVO obj) throws Exception {
		RequerimentoHistoricoVO.validarDados(obj);
		int index = 0;
		Iterator i = getRequerimentoHistoricoVOs().iterator();
		while (i.hasNext()) {
			RequerimentoHistoricoVO objExistente = (RequerimentoHistoricoVO) i.next();
			if (objExistente.getCodigo().equals(obj.getCodigo())) {
				getRequerimentoHistoricoVOs().set(index, obj);
				return;
			}
			index++;
		}
		getRequerimentoHistoricoVOs().add(obj);
	}

	public void excluirRequerimentoHistoricoVOs(Integer codigoRemover) throws Exception {
		int index = 0;
		Iterator i = getRequerimentoHistoricoVOs().iterator();
		while (i.hasNext()) {
			RequerimentoHistoricoVO objExistente = (RequerimentoHistoricoVO) i.next();
			if (objExistente.getCodigo().equals(codigoRemover)) {
				getRequerimentoHistoricoVOs().remove(index);
				return;
			}
			index++;
		}
	}

	@XmlElement(name = "funcionarioVO") 
	public FuncionarioVO getFuncionarioVO() {
		if (funcionarioVO == null) {
			funcionarioVO = new FuncionarioVO();
		}
		return funcionarioVO;
	}

	public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
		this.funcionarioVO = funcionarioVO;
	}



	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public String getSituacaoMatriculaPeriodo() {
		return situacaoMatriculaPeriodo;
	}

	public void setSituacaoMatriculaPeriodo(String situacaoMatriculaPeriodo) {
		this.situacaoMatriculaPeriodo = situacaoMatriculaPeriodo;
	}

	@XmlElement(name = "curso") 
	public CursoVO getCurso() {
		if (curso == null) {
			curso = new CursoVO();
		}
		return curso;
	}

	public void setCurso(CursoVO curso) {
		this.curso = curso;
	}

	@XmlElement(name = "turno") 
	public TurnoVO getTurno() {
		if (turno == null) {
			turno = new TurnoVO();
		}
		return turno;
	}

	public void setTurno(TurnoVO turno) {
		this.turno = turno;
	}

	@XmlElement(name = "materialRequerimentoVOs")
	public List<MaterialRequerimentoVO> getMaterialRequerimentoVOs() {
		if (materialRequerimentoVOs == null) {
			materialRequerimentoVOs = new ArrayList<MaterialRequerimentoVO>(0);
		}
		return materialRequerimentoVOs;
	}

	public void setMaterialRequerimentoVOs(List materialRequerimentoVOs) {
		this.materialRequerimentoVOs = materialRequerimentoVOs;
	}

	public String getSigla() {
		if (sigla == null) {
			sigla = "";
		}
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public Boolean getApresentarCampoSigla() {
		if (getSigla() != null && !getSigla().equals("")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	@XmlElement(name = "taxaIsentaPorQtdeVia")
	public Boolean getTaxaIsentaPorQtdeVia() {
		if (taxaIsentaPorQtdeVia == null) {
			taxaIsentaPorQtdeVia = false;
		}
		return taxaIsentaPorQtdeVia;
	}

	public void setTaxaIsentaPorQtdeVia(Boolean taxaIsentaPorQtdeVia) {
		this.taxaIsentaPorQtdeVia = taxaIsentaPorQtdeVia;
	}

	

	@XmlElement(name = "numeroVia")
	public Integer getNumeroVia() {
		if (numeroVia == null) {
			numeroVia = 1;
		}
		return numeroVia;
	}

	public void setNumeroVia(Integer numeroVia) {
		this.numeroVia = numeroVia;
	}

	public Date getDataUltimaAlteracao() {
		if (dataUltimaAlteracao == null) {
			dataUltimaAlteracao = new Date();
		}
		return dataUltimaAlteracao;
	}

	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}

	public Boolean getAptoImpressaoVisaoAluno() {
		if(aptoImpressaoVisaoAluno == null){
			aptoImpressaoVisaoAluno = Boolean.FALSE;
		}
		return aptoImpressaoVisaoAluno;
	}

	public void setAptoImpressaoVisaoAluno(Boolean aptoImpressaoVisaoAluno) {
		this.aptoImpressaoVisaoAluno = aptoImpressaoVisaoAluno;
	}

	public Date getDataUltimaImpressao() {
		return dataUltimaImpressao;
	}

	public void setDataUltimaImpressao(Date dataUltimaImpressao) {
		this.dataUltimaImpressao = dataUltimaImpressao;
	}

	@XmlElement(name = "motivoDeferimento")
	public String getMotivoDeferimento() {
		if (motivoDeferimento == null) {
			motivoDeferimento = "";
		}
		return motivoDeferimento;
	}

	public void setMotivoDeferimento(String motivoDeferimento) {
		this.motivoDeferimento = motivoDeferimento;
	}

	/**
	 * @return the tipoPessoa
	 */
	@XmlElement(name = "tipoPessoa")
	public TipoPessoa getTipoPessoa() {
		if (tipoPessoa == null) {
			tipoPessoa = TipoPessoa.ALUNO;
		}
		return tipoPessoa;
	}

	/**
	 * @param tipoPessoa the tipoPessoa to set
	 */
	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}
	
	public boolean getTipoPessoaAluno(){
		return getTipoPessoa().equals(TipoPessoa.ALUNO);
	} 
	
    public Boolean getSelecionado() {
        if (selecionado == null) {
            selecionado = false;
        }
        return selecionado;
    }

    public void setSelecionado(Boolean selecionado) {
        this.selecionado = selecionado;
    }

    @XmlElement(name = "unidadeEnsinoTransferenciaInternaVO")
	public UnidadeEnsinoVO getUnidadeEnsinoTransferenciaInternaVO() {
		if (unidadeEnsinoTransferenciaInternaVO == null) {
			unidadeEnsinoTransferenciaInternaVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoTransferenciaInternaVO;
	}

	public void setUnidadeEnsinoTransferenciaInternaVO(UnidadeEnsinoVO unidadeEnsinoTransferenciaInternaVO) {
		this.unidadeEnsinoTransferenciaInternaVO = unidadeEnsinoTransferenciaInternaVO;
	}

	@XmlElement(name = "cursoTransferenciaInternaVO")
	public CursoVO getCursoTransferenciaInternaVO() {
		if (cursoTransferenciaInternaVO == null) {
			cursoTransferenciaInternaVO = new CursoVO();
		}
		return cursoTransferenciaInternaVO;
	}

	public void setCursoTransferenciaInternaVO(CursoVO cursoTransferenciaInternaVO) {
		this.cursoTransferenciaInternaVO = cursoTransferenciaInternaVO;
	}

	@XmlElement(name = "turnoTransferenciaInternaVO")
	public TurnoVO getTurnoTransferenciaInternaVO() {
		if (turnoTransferenciaInternaVO == null) {
			turnoTransferenciaInternaVO = new TurnoVO();
		}
		return turnoTransferenciaInternaVO;
	}

	public void setTurnoTransferenciaInternaVO(TurnoVO turnoTransferenciaInternaVO) {
		this.turnoTransferenciaInternaVO = turnoTransferenciaInternaVO;
	}
	
	/*public ArquivoVO getArquivoAssinadoDigitalmente() {
		if(arquivoAssinadoDigitalmente == null){
			arquivoAssinadoDigitalmente = new ArquivoVO();
		}
		return arquivoAssinadoDigitalmente;
	}

	public void setArquivoAssinadoDigitalmente(ArquivoVO arquivoAssinadoDigitalmente) {
		this.arquivoAssinadoDigitalmente = arquivoAssinadoDigitalmente;
	}*/
	
	
	
	public Integer getQtde() {
		if (qtde == null) {
			qtde = 0;
		}
		return qtde;
	}

	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}
	
	public Boolean getIsDeferido() {
		return getSituacao().equals("FD");
	}

	public boolean getApresentarBotaoEmitirBoleto() {
		if (getValor() != null && getValor() > 0) {
			return true;
		} else {
			return false;
		}
	}
	@XmlElement(name = "contaReceberVO")
//	public boolean getIsApresentarEmitirBoleto() {
//		try {
//			if ((!getCodigo().equals(0)) && (getContaReceberVO().getSituacao().equals("AR")) && (!getContaReceberVO().getCodigo().equals(0) && (getContaReceberVO().getPermiteImprimirBoleto() || getContaReceberVO().getPermiteImprimirBoletoLinkBanco()))) {
//				return true;
//			}
//			return false;
//		} catch (Exception e) {
//			return false;
//		}
//	}
	
	
	public boolean getIsPermiteExcluir() throws Exception {
		if ((!getCodigo().equals(0)) && ((getSituacaoFinanceira().equals("PE") || getSituacaoFinanceira().equals("PG") || getSituacaoFinanceira().equals("IS")) && ((getValorTotalFinal().equals(0.0) && getSituacao().equals("EX")) || (getValorTotalFinal().equals(0.0) && getSituacao().equals("PE")) || (!getValorTotalFinal().equals(0.0) && getSituacao().equals("AP"))))) {
			return true;
		}
		return false;
	}
	
	public String getCssTimeLineRequerimento() {
//		if (getSituacao().equals(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor())) {
//			return "timelineRequerimentoDeferido";
//		}
		return "timelineRequerimento";
	}
	
	public Boolean getApresentarBotaoAutorizarPagamento() {		
		return !isNovoObj() && Uteis.isAtributoPreenchido(getSituacaoFinanceira()) && getSituacaoFinanceira().equals("AP") && getValorTotalFinal() > 0.0 
				&& ((!getSituacao().equals("FI")  && !getSituacao().equals("FD") && !getTipoRequerimento().getPermiteDeferirAguardandoAutorizacaoPagamento()) 
				|| (getSituacao().equals("FD") && getAguardandoAutorizacaoPagamento()));
	}
	
	public Boolean getPago() {
		return Uteis.isAtributoPreenchido(getSituacaoFinanceira()) && getSituacaoFinanceira().equals("PG");
	}
	
	public Boolean getCancelado() {
		return Uteis.isAtributoPreenchido(getSituacaoFinanceira()) && getSituacaoFinanceira().equals("CA");
	}
	
	public Boolean getAguardandoPagamento() {
		return Uteis.isAtributoPreenchido(getSituacaoFinanceira()) && getSituacaoFinanceira().equals("PE");
	}
	
	public Boolean getAguardandoAutorizacaoPagamento() {
		return Uteis.isAtributoPreenchido(getSituacaoFinanceira()) && getSituacaoFinanceira().equals("AP");
	}
	
	public Boolean getIsento() {
		return Uteis.isAtributoPreenchido(getSituacaoFinanceira()) && getSituacaoFinanceira().equals("IS");
	}

	public SituacaoRequerimentoDepartamentoVO getSituacaoRequerimentoDepartamentoVO() {
		if (situacaoRequerimentoDepartamentoVO == null) {
			situacaoRequerimentoDepartamentoVO = new SituacaoRequerimentoDepartamentoVO();
		}
		return situacaoRequerimentoDepartamentoVO;
	}

	public void setSituacaoRequerimentoDepartamentoVO(SituacaoRequerimentoDepartamentoVO situacaoRequerimentoDepartamentoVO) {
		this.situacaoRequerimentoDepartamentoVO = situacaoRequerimentoDepartamentoVO;
	}

	@XmlElement(name = "situacaoIsencaoTaxa")
	public SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum getSituacaoIsencaoTaxa() {
		if (situacaoIsencaoTaxa == null) {
			situacaoIsencaoTaxa = SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.NAO_REQUERIDA;
		}
		return situacaoIsencaoTaxa;
	}
	
	@XmlElement(name = "situacaoIsencaoTaxaApresentar")
	public String getSituacaoIsencaoTaxaApresentar() {
		if (!Uteis.isAtributoPreenchido(situacaoIsencaoTaxa.getValorApresentar())) {
			return "";
		} else {
			return situacaoIsencaoTaxa.getValorApresentar();
		}
	}

	public void setSituacaoIsencaoTaxa(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum situacaoIsencaoTaxa) {
		this.situacaoIsencaoTaxa = situacaoIsencaoTaxa;
	}

	@XmlElement(name = "comprovanteSolicitacaoIsencao")
	public ArquivoVO getComprovanteSolicitacaoIsencao() {
		if (comprovanteSolicitacaoIsencao == null) {
			comprovanteSolicitacaoIsencao = new ArquivoVO();
		}
		return comprovanteSolicitacaoIsencao;
	}

	public void setComprovanteSolicitacaoIsencao(ArquivoVO comprovanteSolicitacaoIsencao) {
		this.comprovanteSolicitacaoIsencao = comprovanteSolicitacaoIsencao;
	}

	@XmlElement(name = "justificativaSolicitacaoIsencao")
	public String getJustificativaSolicitacaoIsencao() {
		if (justificativaSolicitacaoIsencao == null) {
			justificativaSolicitacaoIsencao = "";
		}
		return justificativaSolicitacaoIsencao;
	}

	public void setJustificativaSolicitacaoIsencao(String justificativaSolicitacaoIsencao) {
		this.justificativaSolicitacaoIsencao = justificativaSolicitacaoIsencao;
	}

	@XmlElement(name = "motivoDeferimentoIndeferimentoIsencaoTaxa")
	public String getMotivoDeferimentoIndeferimentoIsencaoTaxa() {
		if (motivoDeferimentoIndeferimentoIsencaoTaxa == null) {
			motivoDeferimentoIndeferimentoIsencaoTaxa = "";
		}
		return motivoDeferimentoIndeferimentoIsencaoTaxa;
	}

	public void setMotivoDeferimentoIndeferimentoIsencaoTaxa(String motivoDeferimentoIndeferimentoIsencaoTaxa) {
		this.motivoDeferimentoIndeferimentoIsencaoTaxa = motivoDeferimentoIndeferimentoIsencaoTaxa;
	}

	@XmlElement(name = "responsavelDeferimentoIndeferimentoIsencaoTaxa")
	public UsuarioVO getResponsavelDeferimentoIndeferimentoIsencaoTaxa() {
		if (responsavelDeferimentoIndeferimentoIsencaoTaxa == null) {
			responsavelDeferimentoIndeferimentoIsencaoTaxa = new UsuarioVO();
		}
		return responsavelDeferimentoIndeferimentoIsencaoTaxa;
	}

	public void setResponsavelDeferimentoIndeferimentoIsencaoTaxa(UsuarioVO responsavelDeferimentoIndeferimentoIsencaoTaxa) {
		this.responsavelDeferimentoIndeferimentoIsencaoTaxa = responsavelDeferimentoIndeferimentoIsencaoTaxa;
	}

	@XmlElement(name = "dataDeferimentoIndeferimentoIsencaoTaxa")
	public Date getDataDeferimentoIndeferimentoIsencaoTaxa() {
		return dataDeferimentoIndeferimentoIsencaoTaxa;
	}

	public void setDataDeferimentoIndeferimentoIsencaoTaxa(Date dataDeferimentoIndeferimentoIsencaoTaxa) {
		this.dataDeferimentoIndeferimentoIsencaoTaxa = dataDeferimentoIndeferimentoIsencaoTaxa;
	}

	public Boolean getPermiteSolicitarIsencaoTaxa() {
		return getTipoRequerimento().getPermitirSolicitacaoIsencaoTaxaRequerimento() && getValorTotalFinal() > 0 
				&& getSituacaoIsencaoTaxa().equals(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.NAO_REQUERIDA)
				&& !getIsento()
				&& !getPago()
				&& !getCancelado();
	}
	
	public Boolean getAguardandoDeferimentoIndeferimentoSolicitacaoIsencaoTaxa() {
		return getTipoRequerimento().getPermitirSolicitacaoIsencaoTaxaRequerimento() && getValorTotalFinal() > 0 
				&& getSituacaoIsencaoTaxa().equals(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.AGUARDANDO_RESPOSTA)
				&& !getIsento()
				&& !getPago()
				&& !getCancelado();
	}
	
	public Boolean getApresentarDadosSolicitacaoIsencaoTaxa() {
		return getSituacaoIsencaoTaxa().equals(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.DEFERIDO)
			   || getSituacaoIsencaoTaxa().equals(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.INDEFERIDO)
			   || getAguardandoDeferimentoIndeferimentoSolicitacaoIsencaoTaxa();
	}
	
	public Boolean getApresentarDadosConfirmacaoSolicitacaoIsencaoTaxa() {
		return (getSituacaoIsencaoTaxa().equals(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.DEFERIDO)
			   || getSituacaoIsencaoTaxa().equals(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.INDEFERIDO))
			   && !getMotivoDeferimentoIndeferimentoIsencaoTaxa().trim().isEmpty();
	}
	
	public String getLabelConfirmacaoSolicitacaoIsencaoTaxa() {
		if(getSituacaoIsencaoTaxa().equals(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.DEFERIDO)) {
			return UteisJSF.internacionalizar("prt_Requerimento_motivoDeferimentoTaxa");
		}
		if(getSituacaoIsencaoTaxa().equals(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.INDEFERIDO)) {
			return UteisJSF.internacionalizar("prt_Requerimento_motivoIndeferimentoTaxa");
		}
		return "";
	}
	
	public String getLabelResponsavelConfirmacaoSolicitacaoIsencaoTaxa() {
		if(getSituacaoIsencaoTaxa().equals(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.DEFERIDO)) {
			return UteisJSF.internacionalizar("prt_Requerimento_responsavelDeferimentoTaxa");
		}
		if(getSituacaoIsencaoTaxa().equals(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.INDEFERIDO)) {
			return UteisJSF.internacionalizar("prt_Requerimento_responsavelIndeferimentoTaxa");
		}
		return "";
	}
	public String getLabelDataConfirmacaoSolicitacaoIsencaoTaxa() {
		if(getSituacaoIsencaoTaxa().equals(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.DEFERIDO)) {
			return UteisJSF.internacionalizar("prt_Requerimento_dataDeferimentoTaxa");
		}
		if(getSituacaoIsencaoTaxa().equals(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.INDEFERIDO)) {
			return UteisJSF.internacionalizar("prt_Requerimento_dataIndeferimentoTaxa");
		}
		return "";
	}

	@XmlElement(name = "disciplinaPorEquivalencia")
	public Boolean getDisciplinaPorEquivalencia() {
		if (disciplinaPorEquivalencia == null) {
			disciplinaPorEquivalencia = false;
		}
		return disciplinaPorEquivalencia;
	}

	public void setDisciplinaPorEquivalencia(Boolean disciplinaPorEquivalencia) {
		this.disciplinaPorEquivalencia = disciplinaPorEquivalencia;
	}

	@XmlElement(name = "mapaEquivalenciaDisciplinaVO")
	public MapaEquivalenciaDisciplinaVO getMapaEquivalenciaDisciplinaVO() {
		if (mapaEquivalenciaDisciplinaVO == null) {
			mapaEquivalenciaDisciplinaVO = new MapaEquivalenciaDisciplinaVO();
		}
		return mapaEquivalenciaDisciplinaVO;
	}

	public void setMapaEquivalenciaDisciplinaVO(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO) {
		this.mapaEquivalenciaDisciplinaVO = mapaEquivalenciaDisciplinaVO;
	}

	@XmlElement(name = "dataInicioAula")
	public Date getDataInicioAula() {		
		return dataInicioAula;
	}

	public void setDataInicioAula(Date dataInicioAula) {
		this.dataInicioAula = dataInicioAula;
	}

	@XmlElement(name = "dataTerminoAula")
	public Date getDataTerminoAula() {		
		return dataTerminoAula;
	}

	public void setDataTerminoAula(Date dataTerminoAula) {
		this.dataTerminoAula = dataTerminoAula;
	}

//	@XmlElement(name = "salaLocalAulaVO")
//	public SalaLocalAulaVO getSalaLocalAulaVO() {
//		if (salaLocalAulaVO == null) {
//			salaLocalAulaVO = new SalaLocalAulaVO();
//		}
//		return salaLocalAulaVO;
//	}
//
//	public void setSalaLocalAulaVO(SalaLocalAulaVO salaLocalAulaVO) {
//		this.salaLocalAulaVO = salaLocalAulaVO;
//	}

	@XmlElement(name = "matriculaPeriodoVO")
	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	@XmlElement(name = "cargaHorariaDisciplina")
	public Integer getCargaHorariaDisciplina() {
		if (cargaHorariaDisciplina == null) {
			cargaHorariaDisciplina = 0;
		}
		return cargaHorariaDisciplina;
	}

	public void setCargaHorariaDisciplina(Integer cargaHorariaDisciplina) {
		this.cargaHorariaDisciplina = cargaHorariaDisciplina;
	}

	@XmlElement(name = "mapaEquivalenciaDisciplinaCursadaVO")
	public MapaEquivalenciaDisciplinaCursadaVO getMapaEquivalenciaDisciplinaCursadaVO() {
		if (mapaEquivalenciaDisciplinaCursadaVO == null) {
			mapaEquivalenciaDisciplinaCursadaVO = new MapaEquivalenciaDisciplinaCursadaVO();
		}
		return mapaEquivalenciaDisciplinaCursadaVO;
	}

	public void setMapaEquivalenciaDisciplinaCursadaVO(MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO) {
		this.mapaEquivalenciaDisciplinaCursadaVO = mapaEquivalenciaDisciplinaCursadaVO;
	}

//	public List<HorarioAlunoTurnoVO> getHorarioAlunoTurnoVOs() {
//		if (horarioAlunoTurnoVOs == null) {
//			horarioAlunoTurnoVOs = new ArrayList<HorarioAlunoTurnoVO>(0);
//		}
//		return horarioAlunoTurnoVOs;
//	}
//
//	public void setHorarioAlunoTurnoVOs(List<HorarioAlunoTurnoVO> horarioAlunoTurnoVOs) {
//		this.horarioAlunoTurnoVOs = horarioAlunoTurnoVOs;
//	}
	
	
	@XmlElement(name = "realizarCobrancaBaseNaUltimaAula")
	public Boolean getRealizarCobrancaBaseNaUltimaAula() {
		if(realizarCobrancaBaseNaUltimaAula == null) {
			realizarCobrancaBaseNaUltimaAula = false;
		}
		return realizarCobrancaBaseNaUltimaAula;
	}

	public void setRealizarCobrancaBaseNaUltimaAula(Boolean realizarCobrancaBaseNaUltimaAula) {
		this.realizarCobrancaBaseNaUltimaAula = realizarCobrancaBaseNaUltimaAula;
	}

	@XmlElement(name = "mensagemChoqueHorario")
	public String getMensagemChoqueHorario() {
		if (mensagemChoqueHorario == null) {
			mensagemChoqueHorario = "";
		}
		return mensagemChoqueHorario;
	}

	public void setMensagemChoqueHorario(String mensagemChoqueHorario) {
		this.mensagemChoqueHorario = mensagemChoqueHorario;
	}

	public String getCoordenador() {
		if (coordenador == null) {
			coordenador = "";
		}
		return coordenador;
	}

	public void setCoordenador(String coordenador) {
		this.coordenador = coordenador;
	}

	public Date getDataEntradaDepartamento() {
		return dataEntradaDepartamento;
	}

	public void setDataEntradaDepartamento(Date dataEntradaDepartamento) {
		this.dataEntradaDepartamento = dataEntradaDepartamento;
	}

	public String getResponsavelTramite() {
		return responsavelTramite;
	}

	public void setResponsavelTramite(String responsavelTramite) {
		this.responsavelTramite = responsavelTramite;
	}

	public String getDepartamentoResponsavelTramite() {
		return departamentoResponsavelTramite;
	}

	public void setDepartamentoResponsavelTramite(String departamentoResponsavelTramite) {
		this.departamentoResponsavelTramite = departamentoResponsavelTramite;
	}
	
	@XmlElement(name = "motivoNaoAceiteCertificado")
	public String getMotivoNaoAceiteCertificado() {
		if(motivoNaoAceiteCertificado == null) {
			motivoNaoAceiteCertificado = "";
		}
		return motivoNaoAceiteCertificado;
	}

	public void setMotivoNaoAceiteCertificado(String motivoNaoAceiteCertificado) {
		this.motivoNaoAceiteCertificado = motivoNaoAceiteCertificado;
	}

	@XmlElement(name = "formatoCertificadoSelecionado")
	public String getFormatoCertificadoSelecionado() {
		if(formatoCertificadoSelecionado == null) {
			formatoCertificadoSelecionado = "";
		}
		return formatoCertificadoSelecionado;
	}

	public void setFormatoCertificadoSelecionado(String formatoCertificadoSelecionado) {
		this.formatoCertificadoSelecionado = formatoCertificadoSelecionado;
	}
	
	public Boolean getIsFormatoCertificadoSelecionadoDigital() {
		return getFormatoCertificadoSelecionado().equals("DIGITAL");
	}
	
	public Boolean getIsFormatoCertificadoSelecionadoImpresso() {
		return getFormatoCertificadoSelecionado().equals("IMPRESSO");
	}
			

	public String getNomeResponsavel() {
		if (nomeResponsavel == null) {
			nomeResponsavel = "";
		}
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}
			
	public Boolean getApresentarBotaoImprimirHistoricoVisaoAluno() {
		return getTipoRequerimento().getPermitirImpressaoHistoricoVisaoAluno() 
				&& getTipoRequerimento().getTipo().equals(TiposRequerimento.HISTORICO.getValor()) 
				&& getSituacao().equals(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor());
	}


	@XmlElement(name = "tipoTrabalhoConclusaoCurso")
	public String getTipoTrabalhoConclusaoCurso() {
		if (tipoTrabalhoConclusaoCurso == null) {
			tipoTrabalhoConclusaoCurso = "";
		}
		return tipoTrabalhoConclusaoCurso;
	}

	public void setTipoTrabalhoConclusaoCurso(String tipoTrabalhoConclusaoCurso) {
		this.tipoTrabalhoConclusaoCurso = tipoTrabalhoConclusaoCurso;
	}

	@XmlElement(name = "notaMonografia")
	public Double getNotaMonografia() {
		return notaMonografia;
	}

	public void setNotaMonografia(Double notaMonografia) {
		this.notaMonografia = notaMonografia;
	}

	@XmlElement(name = "tituloMonografia")
	public String getTituloMonografia() {
		if (tituloMonografia == null) {
			tituloMonografia = "";
		}
		return tituloMonografia;
	}

	public void setTituloMonografia(String tituloMonografia) {
		this.tituloMonografia = tituloMonografia;
	}

	@XmlElement(name = "orientadorMonografia")
	public String getOrientadorMonografia() {
		if (orientadorMonografia == null) {
			orientadorMonografia = "";
		}
		return orientadorMonografia;
	}

	public void setOrientadorMonografia(String orientadorMonografia) {
		this.orientadorMonografia = orientadorMonografia;
	}

	public RequerimentoVO getRequerimentoAntigo() {
		if (requerimentoAntigo == null) {
			requerimentoAntigo = new RequerimentoVO();
		}
		return requerimentoAntigo;
	}

	public void setRequerimentoAntigo(RequerimentoVO requerimentoAntigo) {
		this.requerimentoAntigo = requerimentoAntigo;
	}

	public Boolean getExisteComunicadoInternoNaoLido() {
		if (existeComunicadoInternoNaoLido == null) {
			existeComunicadoInternoNaoLido = false;
		}
		return existeComunicadoInternoNaoLido;
	}

	public void setExisteComunicadoInternoNaoLido(Boolean existeComunicadoInternoNaoLido) {
		this.existeComunicadoInternoNaoLido = existeComunicadoInternoNaoLido;
	}

	public Boolean getPossuiInteracaoAtendenteNaoLida() {
		if (possuiInteracaoAtendenteNaoLida == null) {
			possuiInteracaoAtendenteNaoLida = false;
		}
		return possuiInteracaoAtendenteNaoLida;
	}

	public void setPossuiInteracaoAtendenteNaoLida(Boolean possuiInteracaoAtendenteNaoLida) {
		this.possuiInteracaoAtendenteNaoLida = possuiInteracaoAtendenteNaoLida;
	}

	public Boolean getPossuiInteracaoRequerenteNaoLida() {
		if (possuiInteracaoRequerenteNaoLida == null) {
			possuiInteracaoRequerenteNaoLida = false;
		}
		return possuiInteracaoRequerenteNaoLida;
	}

	public void setPossuiInteracaoRequerenteNaoLida(Boolean possuiInteracaoRequerenteNaoLida) {
		this.possuiInteracaoRequerenteNaoLida = possuiInteracaoRequerenteNaoLida;
	}
	
	public Boolean getDepartamentoPodeInserirNota() {
		if(departamentoPodeInserirNota == null) {
			departamentoPodeInserirNota = false;
		}
		return departamentoPodeInserirNota;
	}

	public void setDepartamentoPodeInserirNota(Boolean departamentoPodeInserirNota) {
		this.departamentoPodeInserirNota = departamentoPodeInserirNota;
	}

	public String getEntreguePrazo(){
		if(this.valor > 0.0) {
	    	return "NÃO";
	    }else {
	    	return "SIM";
	    }
	 }
	
	public String getFinalizadoDentroPrazo() {
		if(Uteis.isAtributoPreenchido(this.dataPrevistaFinalizacao) && Uteis.isAtributoPreenchido(this.dataFinalizacao) && dataFinalizacao.compareTo(dataPrevistaFinalizacao) <= 0) {
			return "SIM";
		}else if(!Uteis.isAtributoPreenchido(this.dataFinalizacao)) {
			return "";
		}
		return "NÃO";
	}
	
	public Long getQuantidadeDiasExecucao() {
		Long diasDeExecucao = new Long(0L);
		if(this.situacao.equals("FD") || this.situacao.equals("FI")) {
			LocalDate dataInicial = Instant.ofEpochMilli(this.data.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate dataFinalizacao = Instant.ofEpochMilli(this.dataFinalizacao.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
			diasDeExecucao = ChronoUnit.DAYS.between(dataInicial,dataFinalizacao);
		}
		return diasDeExecucao;
	}
	
	public Long getQtdDiasAtraso() {
		Long diferencaEmDias = new Long(0L);
		if(getFinalizadoDentroPrazo().equals("NÃO")) {
			LocalDate dataPrevistaFinalizacao = Instant.ofEpochMilli(this.dataPrevistaFinalizacao.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
			diferencaEmDias = ChronoUnit.DAYS.between(dataPrevistaFinalizacao, LocalDate.now());
		}
		return diferencaEmDias;
	}
	public String getSituacaoApresentar() {
		return SituacaoRequerimento.getDescricao(this.situacao);
	}
	
	
	@XmlElement(name = "permitirRecebimentoCartaoCreditoOnline")
	public Boolean getPermitirRecebimentoCartaoCreditoOnline() {
		if (permitirRecebimentoCartaoCreditoOnline == null) {
			permitirRecebimentoCartaoCreditoOnline = false;
		}
		return permitirRecebimentoCartaoCreditoOnline;
	}

	public void setPermitirRecebimentoCartaoCreditoOnline(Boolean permitirRecebimentoCartaoCreditoOnline) {
		this.permitirRecebimentoCartaoCreditoOnline = permitirRecebimentoCartaoCreditoOnline;
	}

	
	@XmlElement(name = "listaEstado")
	public List<EstadoVO> getListaEstado() {
		if (listaEstado == null) {
			listaEstado = new ArrayList<EstadoVO>();
		}
		return listaEstado;
	}

	public void setListaEstado(List<EstadoVO> listaEstado) {
		this.listaEstado = listaEstado;
	}

	@XmlElement(name = "listaDisciplina")
	public List<DisciplinaVO> getListaDisciplina() {
		if (listaDisciplina == null) {
			listaDisciplina = new ArrayList<DisciplinaVO>();
		}
		return listaDisciplina;
	}

	public void setListaDisciplina(List<DisciplinaVO> listaDisciplina) {
		this.listaDisciplina = listaDisciplina;
	}

	@XmlElement(name = "listaUnidadeEnsino")
	public List<UnidadeEnsinoVO> getListaUnidadeEnsino() {
		if (listaUnidadeEnsino == null) {
			listaUnidadeEnsino = new ArrayList<UnidadeEnsinoVO>();
		}
		return listaUnidadeEnsino;
	}

	public void setListaUnidadeEnsino(List<UnidadeEnsinoVO> listaUnidadeEnsino) {
		this.listaUnidadeEnsino = listaUnidadeEnsino;
	}

	@XmlElement(name = "requerimentoPermitirRequerenteAnexarArquivo")
	public Boolean getRequerimentoPermitirRequerenteAnexarArquivo() {
		if (requerimentoPermitirRequerenteAnexarArquivo == null) {
			requerimentoPermitirRequerenteAnexarArquivo = false;
		}
		return requerimentoPermitirRequerenteAnexarArquivo;
	}

	public void setRequerimentoPermitirRequerenteAnexarArquivo(Boolean requerimentoPermitirRequerenteAnexarArquivo) {
		this.requerimentoPermitirRequerenteAnexarArquivo = requerimentoPermitirRequerenteAnexarArquivo;
	}

	@XmlElement(name = "exigePagamento")
	public Boolean getExigePagamento() {
		if (exigePagamento == null) {
			exigePagamento = false;
		}
		return exigePagamento;
	}

	public void setExigePagamento(Boolean exigePagamento) {
		this.exigePagamento = exigePagamento;
	}

	public Boolean getSomenteAluno() {
		if (somenteAluno == null) {
			somenteAluno = false;
		}
		return somenteAluno;
	}

	public void setSomenteAluno(Boolean somenteAluno) {
		this.somenteAluno = somenteAluno;
	}

	public Boolean getPermitirImpressaoBoleto() {
		return permitirImpressaoBoleto;
	}

	public void setPermitirImpressaoBoleto(Boolean permitirImpressaoBoleto) {
		this.permitirImpressaoBoleto = permitirImpressaoBoleto;
	}
	
	@XmlElement(name = "permitirImpressaoArquivo")
	public Boolean getPermitirImpressaoArquivo() {
		if (getTipoRequerimento().getTipo().equals("EC")) {
			return getPermitirImpressaoCertificado();
		} else {
			return getPermitirImpressaoDeclaracao();
		}
		//apresentarBotaoImprimirHistoricoVisaoAluno
	}

	public void setPermitirImpressaoArquivo(Boolean permitirImpressaoArquivo) {
		this.permitirImpressaoArquivo = permitirImpressaoArquivo;
	}

	@XmlElement(name = "permitirImpressaoComprovante")
	public Boolean getPermitirImpressaoComprovante() {
		return permitirImpressaoComprovante;
	}

	public void setPermitirImpressaoComprovante(Boolean permitirImpressaoComprovante) {
		this.permitirImpressaoComprovante = permitirImpressaoComprovante;
	}

	@XmlElement(name = "permitirImpressaoCertificado")
	public Boolean getPermitirImpressaoCertificado() {
		return getTipoRequerimento().getTipo().equals("EC") && !getNovoObj() && getEdicao() && getIsDeferido() && getIsFormatoCertificadoSelecionadoDigital() && getAptoImpressaoVisaoAluno() && getTipoRequerimento().getRequerimentoVisaoAlunoApresImprimirDeclaracao();
	}

	public void setPermitirImpressaoCertificado(Boolean permitirImpressaoCertificado) {
		this.permitirImpressaoCertificado = permitirImpressaoCertificado;
	}
	
	public Boolean getEdicao() {
		if (edicao == null) {
			edicao = Boolean.FALSE;
		}
		return edicao;
	}

	public void setEdicao(Boolean edicao) {
		this.edicao = edicao;
	}

	@XmlElement(name = "permitirImpressaoDeclaracao")
	public Boolean getPermitirImpressaoDeclaracao() {
		return !getTipoRequerimento().getTipo().equals("EC") && !getNovoObj() && getEdicao() && getTipoRequerimento().getRequerimentoVisaoAlunoApresImprimirDeclaracao() && getSituacao().equals("FD") && getTipoRequerimento().getIsUtilizaTextoPadrao();
	}

	public void setPermitirImpressaoDeclaracao(Boolean permitirImpressaoDeclaracao) {
		this.permitirImpressaoDeclaracao = permitirImpressaoDeclaracao;
	}
	
	@XmlElement(name = "permitirRequerenteInteragirTramite")
	public Boolean getPermitirRequerenteInteragirTramite() {
		if(permitirRequerenteInteragirTramite == null) {
			permitirRequerenteInteragirTramite = Boolean.FALSE;
		}
		return permitirRequerenteInteragirTramite;
	}

	public void setPermitirRequerenteInteragirTramite(Boolean permitirRequerenteInteragirTramite) {
		this.permitirRequerenteInteragirTramite = permitirRequerenteInteragirTramite;
	}
	
	@XmlElement(name = "dataInicioAulaApresentar")
	public String getDataInicioAulaApresentar() {		
		return (Uteis.getData(dataInicioAula));
	}

	@XmlElement(name = "dataTerminoAulaApresentar")
	public String getDataTerminoAulaApresentar() {		
		return (Uteis.getData(dataTerminoAula));
	}
	
	@XmlElement(name = "justificativaTrancamento")
	public String getJustificativaTrancamento() {
		if (justificativaTrancamento == null) {
			justificativaTrancamento = "";
		}
		return justificativaTrancamento;
	}
	
	public void setJustificativaTrancamento(String justificativaTrancamento) {
		this.justificativaTrancamento = justificativaTrancamento;
	}
	
	public MotivoCancelamentoTrancamentoVO getMotivoCancelamentoTrancamento() {
		if (motivoCancelamentoTrancamento == null) {
			motivoCancelamentoTrancamento = new MotivoCancelamentoTrancamentoVO();
		}
		return motivoCancelamentoTrancamento;
	}
	
	/**
	 * @param motivoCancelamentoTrancamento
	 *            the motivoCancelamentoTrancamento to set
	 */
	public void setMotivoCancelamentoTrancamento(MotivoCancelamentoTrancamentoVO motivoCancelamentoTrancamento) {
		this.motivoCancelamentoTrancamento = motivoCancelamentoTrancamento;
	}

	public List<RequerimentoDisciplinaVO> getRequerimentoDisciplinaVOs() {
		if(requerimentoDisciplinaVOs == null) {
			requerimentoDisciplinaVOs =  new ArrayList<RequerimentoDisciplinaVO>(0);
		}
		return requerimentoDisciplinaVOs;
	}

	public void setRequerimentoDisciplinaVOs(List<RequerimentoDisciplinaVO> requerimentoDisciplinaVOs) {
		this.requerimentoDisciplinaVOs = requerimentoDisciplinaVOs;
	}
	
	public String getDisciplina_Apresentar() {
		if(getTipoRequerimento().getTipo().equals(TiposRequerimento.APROVEITAMENTO_DISCIPLINA.getValor())) {
			StringBuilder dis =  new StringBuilder();
			getListaRequerimentoDisciplinasAproveitadasVOs().forEach(t -> {
				if(dis.length()>0) {
					dis.append(", ");
				}
				dis.append(t.getDisciplina().getAbreviatura()+"-"+t.getDisciplina().getNome());
			});
			return dis.toString();
		}else if(getTipoRequerimento().getTipo().equals(TiposRequerimento.SEGUNDA_CHAMADA.getValor())) {
			StringBuilder dis =  new StringBuilder();
			getRequerimentoDisciplinaVOs().forEach(t -> {
				if(dis.length()>0) {
					dis.append(", ");
				}
				dis.append(t.getDisciplina().getAbreviatura()+"-"+t.getDisciplina().getNome());
			});
			return dis.toString();
		}else if(getTipoRequerimento().getIsPermiteInformarDisciplina()) {
			return getDisciplina().getAbreviatura()+"-"+getDisciplina().getNome();
		}
		return "";
	}

	public Boolean getIsAnexoPDF() {
		return Uteis.isAtributoPreenchido(getArquivoVO().getNome()) && getArquivoVO().getIsPdF() ;	
	}
	
	public Boolean getIsAnexoImagem() {
		return Uteis.isAtributoPreenchido(getArquivoVO().getNome()) && getArquivoVO().getIsImagem() ;	
	}
	
	/*
	 * ATRIBUTO TRANSIENTE!
	 */
	@XmlElement(name = "requerimentoDisciplinasAproveitadasVO")
	public RequerimentoDisciplinasAproveitadasVO getRequerimentoDisciplinasAproveitadasVO() {
		if(requerimentoDisciplinasAproveitadasVO == null) {
			requerimentoDisciplinasAproveitadasVO =  new RequerimentoDisciplinasAproveitadasVO();
		}
		return requerimentoDisciplinasAproveitadasVO;
	}

	public void setRequerimentoDisciplinasAproveitadasVO(
			RequerimentoDisciplinasAproveitadasVO requerimentoDisciplinasAproveitadasVO) {
		this.requerimentoDisciplinasAproveitadasVO = requerimentoDisciplinasAproveitadasVO;
	}
	
	public static void removerCamposNaoUsadoAPI(RequerimentoVO requerimentoVO) throws Exception{
		 UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getArquivoVO(), "codigo", "nome", "descricao", "cpfRequerimento", "pastaBaseArquivo", "pastaBaseArquivoEnum", "dataUpload", "responsavelUpload", "origem", "codOrigem", "servidorArquivoOnline");
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getArquivoVO().getResponsavelUpload(), "codigo", "nome");
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getComprovanteSolicitacaoIsencao(), "codigo", "nome", "descricao", "cpfRequerimento", "pastaBaseArquivo", "pastaBaseArquivoEnum", "dataUpload", "responsavelUpload", "origem", "codOrigem", "servidorArquivoOnline");
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getComprovanteSolicitacaoIsencao().getResponsavelUpload(), "codigo", "nome");
		  for(MaterialRequerimentoVO t: requerimentoVO.getMaterialRequerimentoVOs()) {				  
			  UtilReflexao.removerCamposChamadaAPI(t.getArquivoVO(), "codigo", "nome", "descricao", "cpfRequerimento", "pastaBaseArquivo", "pastaBaseArquivoEnum", "dataUpload", "responsavelUpload", "origem", "codOrigem", "servidorArquivoOnline");
			  UtilReflexao.removerCamposChamadaAPI(t.getRequerimentoHistorico(), "codigo");
			  UtilReflexao.removerCamposChamadaAPI(t.getUsuarioDisponibilizouArquivo(), "codigo", "nome");
		  };
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getCidade(), "codigo", "nome", "estado");			  
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getDepartamentoResponsavel(), "codigo", "nome");			  
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getTurmaReposicao(), "codigo", "identificadorTurma", "anual", "semestral", "curso", "identificadorTurmaBase", "dataPrimeiraAula", "dataUltimaAula", "professor", "salaLocalAulaVO");			  
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getTurmaReposicao().getProfessor(), "codigo", "nome");			  
//		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getTurmaReposicao().getSalaLocalAulaVO(), "codigo", "sala", "localAula", "localSala");			  
//		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getTurmaReposicao().getSalaLocalAulaVO().getLocalAula(), "codigo", "local");			  
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getTurmaReposicao().getCurso(), "codigo", "nome", "nivelEducacional", "periodicidade");			  
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getMatricula(), "matricula", "gradeCurricularAtual", "curso", "turno", "unidadeEnsino", "aluno");			  
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getPessoa(), "codigo", "nome", "email", "celular", "endereco", "setor", "numero", "CEP", "complemento", "cidade");			  
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getPessoa().getCidade(), "codigo", "nome", "estado");			  
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getMatricula().getAluno(), "codigo", "nome", "email", "celular", "endereco", "setor", "numero", "CEP", "complemento", "cidade");			  
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getMatricula().getCurso(), "codigo", "nome", "nivelEducacional", "periodicidade");			  
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getMatricula().getTurno(), "codigo", "nome");			  
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getMatricula().getUnidadeEnsino(), "codigo", "nome");			  
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getMatricula().getGradeCurricularAtual(), "codigo", "nome");			  
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getCurso(), "codigo", "nome", "nivelEducacional", "periodicidade");			  
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getCursoTransferenciaInternaVO(), "codigo", "nome", "nivelEducacional", "periodicidade");			  
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getTurno(), "codigo", "nome");			  
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getTurnoTransferenciaInternaVO(), "codigo", "nome");			  
//		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getCentroReceita(), "codigo");			  
//		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getContaCorrenteVO(), "codigo");
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getUnidadeEnsino(), "codigo", "nome");
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getUnidadeEnsinoTransferenciaInternaVO(), "codigo", "nome");
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getFuncionarioVO(), "codigo", "pessoa");
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getFuncionarioVO().getPessoa(), "codigo", "nome", "email", "celular");
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getTipoRequerimento().getTextoPadrao(), "codigo", "nome");
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getTipoRequerimento().getCertificadoImpresso(), "codigo", "nome");			  
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getTipoRequerimento().getDepartamentoResponsavel(), "codigo", "nome");			  
//		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getTipoRequerimento().getQuestionario(), "codigo", "descricao");			  
//		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getQuestionarioVO(), "codigo", "descricao", "perguntaQuestionarioVOs");			  			  			 
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getResponsavelDeferimentoIndeferimentoIsencaoTaxa(), "codigo", "nome");			  			  			 
		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getMatriculaPeriodoVO(), "codigo", "ano", "semestre", "matricula", "turma", "situacaoMatriculaPeriodo", "situacao");			  			  			 
		  			  			  			
//		  requerimentoVO.getContaReceberVO().setPessoa(null);			  
//		  requerimentoVO.getContaReceberVO().setResponsavelFinanceiro(null);			  
//		  requerimentoVO.getContaReceberVO().setMatriculaAluno(null);			  
//		  requerimentoVO.getContaReceberVO().setMatriculaPeriodo(null);			  
//		  requerimentoVO.getContaReceberVO().setUnidadeEnsino(null);			  
//		  requerimentoVO.getContaReceberVO().setUnidadeEnsinoFinanceira(null);			  
//		  requerimentoVO.getContaReceberVO().setParceiroVO(null);			  
//		  requerimentoVO.getContaReceberVO().setCentroReceita(null);			  
//		  requerimentoVO.getContaReceberVO().setFuncionario(null);			  
//		  requerimentoVO.getContaReceberVO().setTurma(null);			  
//		  UtilReflexao.removerCamposChamadaAPI(requerimentoVO.getTipoRequerimento().getCentroReceitaRequerimentoPadrao(), "codigo");			  
		  if(!requerimentoVO.getTipoRequerimento().getUnidadeEnsinoEspecificaVOs().isEmpty()) {
			  requerimentoVO.getTipoRequerimento().setUnidadeEnsinoEspecificaVOs(new ArrayList<TipoRequerimentoUnidadeEnsinoVO>(0));
		  }
		  requerimentoVO.getTipoRequerimento().setUnidadeEnsinoEspecificaVOs(null);
		  if(!requerimentoVO.getTipoRequerimento().getTipoRequerimentoDepartamentoVOs().isEmpty()) {
			  requerimentoVO.getTipoRequerimento().setTipoRequerimentoDepartamentoVOs(new ArrayList<TipoRequerimentoDepartamentoVO>(0));
		  }
		  requerimentoVO.getTipoRequerimento().setTipoRequerimentoDepartamentoVOs(null);
		  if(!requerimentoVO.getTipoRequerimento().getTipoRequerimentoCursoVOs().isEmpty()) {
			  requerimentoVO.getTipoRequerimento().setTipoRequerimentoCursoVOs(new ArrayList<TipoRequerimentoCursoVO>(0));
		  }
		  requerimentoVO.getTipoRequerimento().setTipoRequerimentoCursoVOs(null);
		  if(!requerimentoVO.getTipoRequerimento().getTipoDocumentoVOs().isEmpty()) {
			  requerimentoVO.getTipoRequerimento().setTipoDocumentoVOs(new ArrayList<TipoDocumentoVO>(0));
		  }
		  requerimentoVO.getTipoRequerimento().setTipoDocumentoVOs(null);
		  requerimentoVO.getTipoRequerimento().setTipoRequerimentoAbrirDeferimento(null);
		  if(!requerimentoVO.getTipoRequerimento().getPendenciaTipoDocumentoTipoRequerimentoVOs().isEmpty()) {
			  requerimentoVO.getTipoRequerimento().setPendenciaTipoDocumentoTipoRequerimentoVOs(new ArrayList<PendenciaTipoDocumentoTipoRequerimentoVO>(0));
		  }
		  requerimentoVO.getTipoRequerimento().setPendenciaTipoDocumentoTipoRequerimentoVOs(null);		  		 
		  for(RequerimentoHistoricoVO t: requerimentoVO.getRequerimentoHistoricoVOs()) {
			  t.setRequerimentoVO(null);
			  UtilReflexao.removerCamposChamadaAPI(t.getDepartamento(), "codigo", "nome");
			  UtilReflexao.removerCamposChamadaAPI(t.getDepartamentoAnterior(), "codigo", "nome");
			  UtilReflexao.removerCamposChamadaAPI(t.getResponsavelRequerimentoDepartamento(), "codigo", "nome");			  
		  }
		  for(RequerimentoDisciplinasAproveitadasVO t: requerimentoVO.getListaRequerimentoDisciplinasAproveitadasVOs()) {
			  t.setRequerimentoVO(null);
			  t.setAproveitamentoDisciplinasEntreMatriculasVO(null);
			  t.setGradeCurricularGrupoOptativaDisciplina(null);
			  t.setGradeDisciplinaCompostaVO(null);
			  t.setGradeDisciplinaVO(null);
			  t.setAproveitamentoDisciplinaVO(null);
			  t.setConfiguracaoAcademicoNotaConceitoVO(null);
			  t.setHistoricoAtual(null);
			  t.setResponsavelAproveitamentoEntreMatriculas(null);
			  t.setPeriodoletivoGrupoOptativaVO(null);
			  t.setConfiguracaoAcademicoVO(null);
			  t.setDisciplinasAproveitadasFazemParteComposicao(null);			  
			  UtilReflexao.removerCamposChamadaAPI(t.getResponsavelDeferimento(), "codigo", "nome");
			  UtilReflexao.removerCamposChamadaAPI(t.getResponsavelIndeferimento(), "codigo", "nome");
			  UtilReflexao.removerCamposChamadaAPI(t.getConfiguracaoAcademicoNotaConceitoVO(), "codigo", "nome");
			  UtilReflexao.removerCamposChamadaAPI(t.getArquivoPlanoEnsino(), "codigo", "nome", "descricao", "cpfRequerimento", "pastaBaseArquivo", "pastaBaseArquivoEnum", "dataUpload", "responsavelUpload", "origem", "codOrigem", "servidorArquivoOnline");
			  UtilReflexao.removerCamposChamadaAPI(t.getArquivoPlanoEnsino().getResponsavelUpload(), "codigo", "nome");
			 
		  }
		 
	}
	public static Response removerCamposChamadaAPI(RequerimentoVO requerimentoVO) throws Exception{
		removerCamposNaoUsadoAPI(requerimentoVO);
		Gson gson = new GsonBuilder().serializeNulls().setExclusionStrategies(new ExcluirJsonStrategy()).setDateFormat("MM-dd-yyyy HH:mm:ss").create();
		String json =	gson.toJson(requerimentoVO);
		return Response.status(Status.OK).entity(json).build();
	}

	public SalaAulaBlackboardVO getGrupoFacilitador() {
		if (grupoFacilitador == null) {
			grupoFacilitador = new SalaAulaBlackboardVO();
		}
		return grupoFacilitador;
	}
	
	public void setGrupoFacilitador(SalaAulaBlackboardVO grupoFacilitador) {
		this.grupoFacilitador = grupoFacilitador;
	}
	
	public String getTemaTccFacilitador() {
		if (temaTccFacilitador == null) {
			temaTccFacilitador = Constantes.EMPTY;
		}
		return temaTccFacilitador;
	}
	
	public void setTemaTccFacilitador(String temaTccFacilitador) {
		this.temaTccFacilitador = temaTccFacilitador;
	}
	
	public String getAssuntoTccFacilitador() {
		if (assuntoTccFacilitador == null) {
			assuntoTccFacilitador = Constantes.EMPTY;
		}
		return assuntoTccFacilitador;
	}
	
	public void setAssuntoTccFacilitador(String assuntoTccFacilitador) {
		this.assuntoTccFacilitador = assuntoTccFacilitador;
	}
	
	public String getAvaliadorExternoFacilitador() {
		if (avaliadorExternoFacilitador == null) {
			avaliadorExternoFacilitador = Constantes.EMPTY;
		}
		return avaliadorExternoFacilitador;
	}
	
	public void setAvaliadorExternoFacilitador(String avaliadorExternoFacilitador) {
		this.avaliadorExternoFacilitador = avaliadorExternoFacilitador;
	}

	public String getCid() {
		if(cid == null) {
			cid = "";
		}
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public Integer getVagaPorCursoDiaSemana() {
		if(vagaPorCursoDiaSemana == null) {
			vagaPorCursoDiaSemana =  0;
		}
		return vagaPorCursoDiaSemana;
	}

	public void setVagaPorCursoDiaSemana(Integer vagaPorCursoDiaSemana) {
		this.vagaPorCursoDiaSemana = vagaPorCursoDiaSemana;
	}

	public Integer getVagaPorUnidadeEnsinoDiaSemana() {
		if(vagaPorUnidadeEnsinoDiaSemana == null) {
			vagaPorUnidadeEnsinoDiaSemana =  0;
		}
		return vagaPorUnidadeEnsinoDiaSemana;
	}

	public void setVagaPorUnidadeEnsinoDiaSemana(Integer vagaPorUnidadeEnsinoDiaSemana) {
		this.vagaPorUnidadeEnsinoDiaSemana = vagaPorUnidadeEnsinoDiaSemana;
	}

	public Date getDataAfastamentoInicio() {
		return dataAfastamentoInicio;
	}

	public void setDataAfastamentoInicio(Date dataAfastamentoInicio) {
		this.dataAfastamentoInicio = dataAfastamentoInicio;
	}

	public Date getDataAfastamentoFim() {
		return dataAfastamentoFim;
	}

	public void setDataAfastamentoFim(Date dataAfastamentoFim) {
		this.dataAfastamentoFim = dataAfastamentoFim;
	}
	
	public Boolean getApresentarListaCids() {
		return getTipoRequerimento().getTipo().equals("SEGUNDA_CHAMADA");
	}
	
	
	public CidTipoRequerimentoVO getCidTipoRequerimentoVO() {
		if (cidTipoRequerimentoVO == null) {
			cidTipoRequerimentoVO = new CidTipoRequerimentoVO();
		}
		return cidTipoRequerimentoVO;
	}

	public void setCidTipoRequerimentoVO(CidTipoRequerimentoVO cidTipoRequerimentoVO) {
		this.cidTipoRequerimentoVO = cidTipoRequerimentoVO;
	}
	
	private String cidDescricao;

	public String getCidDescricao() {
		if(!Uteis.isAtributoPreenchido(cidDescricao) || getNovoObj() ) {
			cidDescricao = "";
			for(CidTipoRequerimentoVO cidTipoRequerimentoVO : getTipoRequerimento().getCidTipoRequerimentoVOs()) {
				if(cidTipoRequerimentoVO.getSelecionado()) {
					if(!cidDescricao.isEmpty()) {
						cidDescricao += ", ";
					}
					cidDescricao += cidTipoRequerimentoVO.getNomeCid_Apresentar();
					
				}
			}
		}
		return cidDescricao;
	}
		

	public void setCidDescricao(String cidDescricao) {
		this.cidDescricao = cidDescricao;
	}

	public RequerimentoCidTipoRequerimentoVO getRequerimentoCidTipoRequerimentoVO() {
		if (requerimentoCidTipoRequerimentoVO == null) {
			requerimentoCidTipoRequerimentoVO = new RequerimentoCidTipoRequerimentoVO();
		}
		return requerimentoCidTipoRequerimentoVO;
	}

	public void setRequerimentoCidTipoRequerimentoVO(RequerimentoCidTipoRequerimentoVO requerimentoCidTipoRequerimentoVO) {
		this.requerimentoCidTipoRequerimentoVO = requerimentoCidTipoRequerimentoVO;
	}
	
	public boolean isUtilizarMensagemDeferimentoTipoRequerimento() {
		return getIsDeferido() && Uteis.isAtributoPreenchido(getTipoRequerimento()) && getTipoRequerimento().getUtilizarMensagemDeferimentoExclusivo();
	}

	public boolean isUtilizarMensagemIndeferimentoTipoRequerimento() {
		return getIsIndeferido() && Uteis.isAtributoPreenchido(getTipoRequerimento()) && getTipoRequerimento().getUtilizarMensagemIndeferimentoExclusivo();
	}

	public boolean isPeriodoIngressoAlunoInformado() {
		return Uteis.isAtributoPreenchido(getMatricula()) 
				&& ((getMatricula().getCurso().getSemestral() && Uteis.isAtributoPreenchido(getMatricula().getAnoIngresso()) && Uteis.isAtributoPreenchido(getMatricula().getSemestreIngresso())) 
						|| (getMatricula().getCurso().getAnual() && Uteis.isAtributoPreenchido(getMatricula().getAnoIngresso())));
	}

	/**
	 * regra que permitisse vai validar a regra de tipo aluno definido no tipo de
	 * requerimento
	 * 
	 * @author Felipi Alves
	 * @chamado 41654
	 */
	public boolean isPermitirValidarTipoAlunoTipoRequerimento() {
		return Uteis.isAtributoPreenchido(getTipoRequerimento()) 
				&& Uteis.isAtributoPreenchido(getTipoRequerimento().getTipoAluno()) 
				&& Uteis.isAtributoPreenchido(getMatricula()) 
				&& Uteis.isAtributoPreenchido(getMatriculaPeriodoVO()) 
				&& !getTipoRequerimento().isTipoAlunoAmbos() 
				&& !getMatricula().getCurso().getIntegral() 
				&& isPeriodoIngressoAlunoInformado();
	}
	
	/**
	 * regra que define se o aluno selecionado para o requerimento é CALOURO ou não,
	 * para ser calouro o ano/semestre ingresso tem que ser iguais ao ano/semestre
	 * da matrícula periodo
	 *
	 * @author Felipi Alves
	 * @chamado 41654
	 */
	public boolean isAlunoCalouro() {
		return Uteis.isAtributoPreenchido(getMatricula()) 
				&& ((getMatricula().getCurso().getSemestral() && getMatricula().getAnoIngresso().equals(getMatriculaPeriodoVO().getAno()) && getMatricula().getSemestreIngresso().equals(getMatriculaPeriodoVO().getSemestre())) 
						|| (getMatricula().getCurso().getAnual() && getMatricula().getAnoIngresso().equals(getMatriculaPeriodoVO().getAno())));
	}
	
	/**
	 * regra que define se o aluno selecionado para o requerimento é VETERANO ou
	 * não, para ser VETERANO o ano/semestre da matrícula periodo tem que ser maior
	 * que o ano/semestre ingresso
	 *
	 * @author Felipi Alves
	 * @chamado 41654
	 */
	public boolean isAlunoVeterano() {
		return Uteis.isAtributoPreenchido(getMatricula())
				&& ((getMatricula().getCurso().getSemestral() && (getMatriculaPeriodoVO().getAno() + getMatriculaPeriodoVO().getSemestre()).compareTo(getMatricula().getAnoIngresso() + getMatricula().getSemestreIngresso()) > 0) 
						|| (getMatricula().getCurso().getAnual() && (getMatriculaPeriodoVO().getAno()).compareTo(getMatricula().getAnoIngresso()) > 0));
	}
	
	public DocumentoAssinadoVO getDocumentoAssinado() {
		if (documentoAssinado == null) {
			documentoAssinado = new DocumentoAssinadoVO();
		}
		return documentoAssinado;
	}

	public void setDocumentoAssinado(DocumentoAssinadoVO documentoAssinado) {
		this.documentoAssinado = documentoAssinado;
	}
	
}
