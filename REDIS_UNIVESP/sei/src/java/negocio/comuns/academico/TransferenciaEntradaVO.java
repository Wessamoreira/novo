package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.TipoMidiaCaptacaoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoTransferenciaEntrada;
import negocio.comuns.utilitarias.dominios.TipoTransferenciaEntrada;

/**
 * Reponsável por manter os dados da entidade TransferenciaEntrada. Classe do tipo VO - Value Object composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes
 * atributos. Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class TransferenciaEntradaVO extends SuperVO {

    private Integer codigo;
    private Date data;
    private String descricao;
    private String situacao;
    private String cursoOrigem;
    private String instituicaoOrigem;
    private CidadeVO cidade;
    private String parecerLegalInstituicaoOrigem;
    private String justificativa;
    private String tipoJustificativa;
    private RequerimentoVO codigoRequerimento;
    private TipoMidiaCaptacaoVO tipoMidiaCaptacao;
    
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>Matricula </code>.
     */
    private MatriculaVO matricula;
    /**
     * Atributo responsável por manter os objetos da classe <code>TransferenciaEntradaDisciplinasAproveitadas</code>.
     */
    private List<TransferenciaEntradaDisciplinasAproveitadasVO> transferenciaEntradaDisciplinasAproveitadasVOs;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>Pessoa </code>.
     */
    private UsuarioVO responsavelAutorizacao;
    private UnidadeEnsinoVO unidadeEnsino;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>Curso </code>.
     */
    private CursoVO curso;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>Turno </code>.
     */
    private TurnoVO turno;
    private TurmaVO turma;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>gradeCurricular </code>.
     */
    private GradeCurricularVO gradeCurricular;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>matriculaPeriodo </code>.
     */
    private PeriodoLetivoVO peridoLetivo;
    private Boolean matriculado;
    private PessoaVO pessoa;
    private Integer unidadeEnsinoCurso;
    private MatriculaPeriodoVO matriculaPeriodo;
    private String tipoTransferenciaEntrada;
    private List<TransferenciaEntradaRegistroAulaFrequenciaVO> transferenciaEntradaRegistroAulaFrequenciaVOs;
    
    private Date dataEstorno;
    private UsuarioVO responsavelEstorno;
    private String motivoEstorno;
    private Boolean carregarDisciplinasAproveitadas;
    private ProcessoMatriculaVO processoMatriculaVO;
    
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>TransferenciaEntrada</code>. Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public TransferenciaEntradaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>TransferenciaEntradaVO</code>. Todos os tipos de consistência de dados são e devem ser implementadas neste método. São
     * validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
     */
    public static void validarDados(TransferenciaEntradaVO obj, ConfiguracaoGeralSistemaVO c) throws ConsistirException {
    	if(obj.getData() == null){
    		throw new ConsistirException("O campo DATA deve ser informado.");
    	}
        if (obj.getPessoa() == null || obj.getPessoa().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo ALUNO deve ser informado.");
        }
        if (obj.getSituacao().equals("")) {
            throw new ConsistirException("O campo SITUAÇÃO (Transferência Entrada) deve ser informado.");
        }
        if (obj.getSituacao().equals("EF")) {
            // throw new
            // ConsistirException("Esta Transferência Entrada já foi Efetivada");
        }
        if (obj.getSituacao().equals("IN")) {
            // throw new
            // ConsistirException("Esta Transferência Entrada já foi Indeferida");
        }
        if ((!c.getPermiteTransferenciaSemRequerimento().booleanValue() && obj.getTipoTransferenciaEntrada().equals(TipoTransferenciaEntrada.EXTERNA.getValor()))
                || (!c.getPermiteTransferenciaInternaSemRequerimento().booleanValue() && obj.getTipoTransferenciaEntrada().equals(TipoTransferenciaEntrada.INTERNA.getValor()))) {
            if (obj.getCodigoRequerimento().getCodigo().intValue() == 0) {
                throw new ConsistirException("O campo REQUERIMENTO  deve ser informado.");
            }
        }
        if (obj.getTipoTransferenciaEntrada().equals(TipoTransferenciaEntrada.EXTERNA.getValor())
                && ((obj.getMatricula().getMatricula() == null && obj.getMatriculado()) || (obj.getMatricula().getMatricula().equals("") && obj.getMatriculado()))) {
            throw new ConsistirException("O campo MATRÍCULA deve ser informado.");
        }
        if (obj.getTipoTransferenciaEntrada().equals(TipoTransferenciaEntrada.EXTERNA.getValor()) && obj.getCursoOrigem().equals("")) {
            throw new ConsistirException("O campo CURSO ORIGEM  deve ser informado.");
        }
        if (obj.getTipoTransferenciaEntrada().equals(TipoTransferenciaEntrada.EXTERNA.getValor()) && obj.getInstituicaoOrigem().equals("")) {
            throw new ConsistirException("O campo INSTITUIÇÃO ORIGEM deve ser informado.");
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA deve ser informado.");
        }
        if ((obj.getResponsavelAutorizacao() == null) || (obj.getResponsavelAutorizacao().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo RESPONSÁVEL AUTORIZAÇÃO deve ser informado.");
        }
        if (obj.getCodigoRequerimento().getCodigo().intValue() != 0) {
            if (obj.getCodigoRequerimento().getSituacao().equals("AP")) {
                throw new ConsistirException("Requerimento especificado está aguardando pagamento.");
            }
        }
    }

    public static void validarDadosParaMatricula(TransferenciaEntradaVO obj, ConfiguracaoGeralSistemaVO c) throws ConsistirException {
        validarDados(obj, c);
        if (obj.getUnidadeEnsino() == null || obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo UNIDADE ENSINO deve ser informado.");
        }
        if (obj.getCurso() == null || obj.getCurso().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo CURSO deve ser informado.");
        }
        if ((obj.getTurno() == null) || (obj.getTurno().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo TURNO deve ser informado.");
        }
        if (obj.getGradeCurricular() == null || obj.getGradeCurricular().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo GRADE CURRICULAR deve ser informado.");
        }
        if (obj.getPeridoLetivo() == null || obj.getPeridoLetivo().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo PERIODO LETIVO deve ser informado.");
        }
//		if (obj.getTurma() == null || obj.getTurma().getCodigo().intValue() == 0) {
//			throw new ConsistirException("O campo TURMA deve ser informado.");
//		}
    }
    
    public static void validarDadosParaTransferenciaEntradaNaoPodeSerAlterada(TransferenciaEntradaVO obj) throws ConsistirException {

        if (obj.getTipoTransferenciaEntrada().equals(TipoTransferenciaEntrada.EXTERNA.getValor()) && obj.getCursoOrigem().equals("")) {
            throw new ConsistirException("O campo CURSO ORIGEM  deve ser informado.");
        }
        if (obj.getTipoTransferenciaEntrada().equals(TipoTransferenciaEntrada.EXTERNA.getValor()) && obj.getInstituicaoOrigem().equals("")) {
            throw new ConsistirException("O campo INSTITUIÇÃO ORIGEM deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setData(new Date());
        setDescricao("");
        setSituacao(SituacaoTransferenciaEntrada.EM_AVALIACAO.getValor());
        setCursoOrigem("");
        setInstituicaoOrigem("");
        setParecerLegalInstituicaoOrigem("");
        setJustificativa("");
        setTipoJustificativa("");
        setMatriculado(false);
        setUnidadeEnsinoCurso(0);
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe <code>TransferenciaEntradaDisciplinasAproveitadasVO</code> ao List <code>transferenciaEntradaDisciplinasAproveitadasVOs</code>.
     * Utiliza o atributo padrão de consulta da classe <code>TransferenciaEntradaDisciplinasAproveitadas</code> - getDisciplina() - como identificador (key) do objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>TransferenciaEntradaDisciplinasAproveitadasVO</code> que será adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjTransferenciaEntradaDisciplinasAproveitadasVOs(TransferenciaEntradaDisciplinasAproveitadasVO obj, String periodicidadeCurso) throws Exception {

        TransferenciaEntradaDisciplinasAproveitadasVO.validarDados(obj, periodicidadeCurso);
        int index = 0;
        Iterator i = getTransferenciaEntradaDisciplinasAproveitadasVOs().iterator();
        while (i.hasNext()) {
            TransferenciaEntradaDisciplinasAproveitadasVO objExistente = (TransferenciaEntradaDisciplinasAproveitadasVO) i.next();
            if (objExistente.getDisciplina().getCodigo().equals(obj.getDisciplina().getCodigo())) {
                getTransferenciaEntradaDisciplinasAproveitadasVOs().set(index, obj);
                return;
            }
            index++;
        }
        getTransferenciaEntradaDisciplinasAproveitadasVOs().add(obj);
    }

    /**
     * Operação responsável por excluir um objeto da classe <code>TransferenciaEntradaDisciplinasAproveitadasVO</code> no List <code>transferenciaEntradaDisciplinasAproveitadasVOs</code>. Utiliza o
     * atributo padrão de consulta da classe <code>TransferenciaEntradaDisciplinasAproveitadas</code> - getDisciplina() - como identificador (key) do objeto no List.
     *
     * @param disciplina
     *            Parâmetro para localizar e remover o objeto do List.
     */
    
    public void excluirObjTransferenciaEntradaDisciplinasAproveitadasVOs(Integer disciplina) throws Exception {
        int index = 0;
        Iterator i = getTransferenciaEntradaDisciplinasAproveitadasVOs().iterator();
        while (i.hasNext()) {
            TransferenciaEntradaDisciplinasAproveitadasVO objExistente = (TransferenciaEntradaDisciplinasAproveitadasVO) i.next();
            if (objExistente.getDisciplina().getCodigo().equals(disciplina)) {
                getTransferenciaEntradaDisciplinasAproveitadasVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe <code>TransferenciaEntradaDisciplinasAproveitadasVO</code> no List <code>transferenciaEntradaDisciplinasAproveitadasVOs</code>. Utiliza o
     * atributo padrão de consulta da classe <code>TransferenciaEntradaDisciplinasAproveitadas</code> - getDisciplina() - como identificador (key) do objeto no List.
     *
     * @param disciplina
     *            Parâmetro para localizar o objeto do List.
     */
    public TransferenciaEntradaDisciplinasAproveitadasVO consultarObjTransferenciaEntradaDisciplinasAproveitadasVO(Integer disciplina) throws Exception {
        Iterator i = getTransferenciaEntradaDisciplinasAproveitadasVOs().iterator();
        while (i.hasNext()) {
            TransferenciaEntradaDisciplinasAproveitadasVO objExistente = (TransferenciaEntradaDisciplinasAproveitadasVO) i.next();
            if (objExistente.getDisciplina().getCodigo().equals(disciplina)) {
                return objExistente;
            }
        }
        return null;
    }

    public Boolean getApresentarBotoes() {
        if (getSituacao().equals("AV") && !getNovoObj()) {
            return true;
        }
        return false;
    }

    public Boolean getExisteMatricula() {
        if (!getMatricula().getMatricula().equals("")) {
            return true;
        }
        return false;
    }

    public UsuarioVO getResponsavelAutorizacao() {
        if (responsavelAutorizacao == null) {
            responsavelAutorizacao = new UsuarioVO();
        }
        return responsavelAutorizacao;
    }

    public void setResponsavelAutorizacao(UsuarioVO responsavelAutorizacao) {
        this.responsavelAutorizacao = responsavelAutorizacao;
    }

    /**
     * Retorna o objeto da classe <code>Matricula</code> relacionado com ( <code>TransferenciaEntrada</code>).
     */
    public MatriculaVO getMatricula() {
        if (matricula == null) {
            matricula = new MatriculaVO();
        }
        return (matricula);
    }

    /**
     * Define o objeto da classe <code>Matricula</code> relacionado com ( <code>TransferenciaEntrada</code>).
     */
    public void setMatricula(MatriculaVO obj) {
        this.matricula = obj;
    }

    public String getTipoJustificativa() {
        return (tipoJustificativa);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo com um domínio específico. Com base no valor de armazenamento do atributo esta função é capaz de retornar o de
     * apresentação correspondente. Útil para campos como sexo, escolaridade, etc.
     */
    public String getTipoJustificativa_Apresentar() {
        if (tipoJustificativa.equals("FT")) {
            return "Falta Tempo";
        }
        if (tipoJustificativa.equals("FI")) {
            return "Infra-estrutura Fraca";
        }
        if (tipoJustificativa.equals("BA")) {
            return "Baixa Qualidade Acadêmica";
        }
        if (tipoJustificativa.equals("DD")) {
            return "Deficiência Administração";
        }
        if (tipoJustificativa.equals("DC")) {
            return "Desmotivação Carreira";
        }
        if (tipoJustificativa.equals("DA")) {
            return "Deficiência Atendimento";
        }
        if (tipoJustificativa.equals("IP")) {
            return "Insatisfação Professores";
        }
        if (tipoJustificativa.equals("DF")) {
            return "Dificuldade Financeira";
        }
        if (tipoJustificativa.equals("NI")) {
            return "Não Informado";
        }
        return (tipoJustificativa);
    }

    public void setTipoJustificativa(String tipoJustificativa) {
        this.tipoJustificativa = tipoJustificativa;
    }

    public String getJustificativa() {
        return (justificativa);
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public String getParecerLegalInstituicaoOrigem() {
        return (parecerLegalInstituicaoOrigem);
    }

    public void setParecerLegalInstituicaoOrigem(String parecerLegalInstituicaoOrigem) {
        this.parecerLegalInstituicaoOrigem = parecerLegalInstituicaoOrigem;
    }

    public String getInstituicaoOrigem() {
        return (instituicaoOrigem);
    }

    public void setInstituicaoOrigem(String instituicaoOrigem) {
        this.instituicaoOrigem = instituicaoOrigem;
    }

    public String getCursoOrigem() {
        return (cursoOrigem);
    }

    public void setCursoOrigem(String cursoOrigem) {
        this.cursoOrigem = cursoOrigem;
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "";
        }
        return (situacao);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo com um domínio específico. Com base no valor de armazenamento do atributo esta função é capaz de retornar o de
     * apresentação correspondente. Útil para campos como sexo, escolaridade, etc.
     */
    public String getSituacao_Apresentar() {
    	 return SituacaoTransferenciaEntrada.getEnum(getSituacao()).getDescricao();
    }

    public Boolean getGradeCurricularDefina() {
        if (getGradeCurricular().getCodigo() != null && getGradeCurricular().getCodigo().intValue() > 0) {
            return true;
        }
        return false;
    }

    public Boolean getExisteRequerimento() {
        if (getCodigoRequerimento().getCodigo() != null && getCodigoRequerimento().getCodigo().intValue() > 0) {
            return true;
        }
        return false;
    }

    public Boolean getAguardandoRealizacaoMatricula() {
        if (!getMatriculado() && !getNovoObj()) {
            return true;
        }
        return false;
    }

    public Boolean getSituacaoEfetivado() {
        if (getSituacao().equals(SituacaoTransferenciaEntrada.EFETIVADO.getValor())) {
            return true;
        }
        return false;
    }

    public Boolean getSituacaoEmAvaliacao() {
        if (getSituacao().equals(SituacaoTransferenciaEntrada.EM_AVALIACAO.getValor())) {
            return true;
        }
        return false;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getDescricao() {
        return (descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getData() {
        return (data);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
     */
    public String getData_Apresentar() {
        return (Uteis.getData(data));
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public TipoMidiaCaptacaoVO getTipoMidiaCaptacao() {
        if (tipoMidiaCaptacao == null) {
            tipoMidiaCaptacao = new TipoMidiaCaptacaoVO();
        }
        return tipoMidiaCaptacao;
    }

    public void setTipoMidiaCaptacao(TipoMidiaCaptacaoVO tipoMidiaCaptacao) {
        this.tipoMidiaCaptacao = tipoMidiaCaptacao;
    }

    public RequerimentoVO getCodigoRequerimento() {
        if (codigoRequerimento == null) {
            codigoRequerimento = new RequerimentoVO();
        }
        return codigoRequerimento;
    }

    public void setCodigoRequerimento(RequerimentoVO codigoRequerimento) {
        this.codigoRequerimento = codigoRequerimento;
    }

    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return curso;
    }

    public void setCurso(CursoVO curso) {
        this.curso = curso;
    }

    public TurnoVO getTurno() {
        if (turno == null) {
            turno = new TurnoVO();
        }
        return turno;
    }

    public void setTurno(TurnoVO turno) {
        this.turno = turno;
    }

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public GradeCurricularVO getGradeCurricular() {
        if (gradeCurricular == null) {
            gradeCurricular = new GradeCurricularVO();
        }
        return gradeCurricular;
    }

    public void setGradeCurricular(GradeCurricularVO gradeCurricular) {
        this.gradeCurricular = gradeCurricular;
    }

    public Boolean getExisteUnidadeEnsino() {
        if (getUnidadeEnsino().getCodigo() != 0) {
            return true;
        }
        return false;
    }

    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return turma;
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
    }

    public PeriodoLetivoVO getPeridoLetivo() {
        if (peridoLetivo == null) {
            peridoLetivo = new PeriodoLetivoVO();
        }
        return peridoLetivo;
    }

    public void setPeridoLetivo(PeriodoLetivoVO peridoLetivo) {
        this.peridoLetivo = peridoLetivo;
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe <code>TransferenciaEntradaDisciplinasAproveitadas</code>.
     */
    public List<TransferenciaEntradaDisciplinasAproveitadasVO> getTransferenciaEntradaDisciplinasAproveitadasVOs() {
        if (transferenciaEntradaDisciplinasAproveitadasVOs == null) {
            transferenciaEntradaDisciplinasAproveitadasVOs = new ArrayList();
        }
        return (transferenciaEntradaDisciplinasAproveitadasVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe <code>TransferenciaEntradaDisciplinasAproveitadas</code>.
     */
    public void setTransferenciaEntradaDisciplinasAproveitadasVOs(List<TransferenciaEntradaDisciplinasAproveitadasVO> transferenciaEntradaDisciplinasAproveitadasVOs) {
        this.transferenciaEntradaDisciplinasAproveitadasVOs = transferenciaEntradaDisciplinasAproveitadasVOs;
    }

    public Boolean getMatriculado() {
        if (matriculado == null) {
            matriculado = Boolean.FALSE;
        }
        return matriculado;
    }

    public void setMatriculado(Boolean matriculado) {
        this.matriculado = matriculado;
    }

    public PessoaVO getPessoa() {
        if (pessoa == null) {
            pessoa = new PessoaVO();
        }
        return pessoa;
    }

    public void setPessoa(PessoaVO pessoa) {
        this.pessoa = pessoa;
    }

    public Integer getUnidadeEnsinoCurso() {
        return unidadeEnsinoCurso;
    }

    public void setUnidadeEnsinoCurso(Integer unidadeEnsinoCurso) {
        this.unidadeEnsinoCurso = unidadeEnsinoCurso;
    }

    public MatriculaPeriodoVO getMatriculaPeriodo() {
        if (matriculaPeriodo == null) {
            matriculaPeriodo = new MatriculaPeriodoVO();
        }
        return matriculaPeriodo;
    }

    public void setMatriculaPeriodo(MatriculaPeriodoVO matriculaPeriodo) {
        this.matriculaPeriodo = matriculaPeriodo;
    }

    public String getTipoTransferenciaEntrada() {
        if (tipoTransferenciaEntrada == null) {
            tipoTransferenciaEntrada = "";
        }
        return tipoTransferenciaEntrada;
    }

    public void setTipoTransferenciaEntrada(String tipoTransferenciaEntrada) {
        this.tipoTransferenciaEntrada = tipoTransferenciaEntrada;
    }

    public Boolean getIsTransferenciaEntradaPodeSerAlterada() {
        if (this.getSituacao().equals("AV")) {
            return true;
        }
        //if (!this.getMatricula().getMatricula().equals("")) {
        //    return true;
        //}
        return false;
    }

    public Boolean getIsPossuiRequerimento() {
        return (getCodigoRequerimento().getCodigo() != null && getCodigoRequerimento().getCodigo() != 0);
    }

    public Boolean getIsNaoPossuiRequerimento() {
        return !getIsPossuiRequerimento();
    }

	public CidadeVO getCidade() {
		if(cidade == null){
			cidade = new CidadeVO();
		}
		return cidade;
	}

	public void setCidade(CidadeVO cidade) {
		this.cidade = cidade;
	}

	public List<TransferenciaEntradaRegistroAulaFrequenciaVO> getTransferenciaEntradaRegistroAulaFrequenciaVOs() {
		if (transferenciaEntradaRegistroAulaFrequenciaVOs == null) {
			transferenciaEntradaRegistroAulaFrequenciaVOs = new ArrayList<TransferenciaEntradaRegistroAulaFrequenciaVO>(0);
		}
		return transferenciaEntradaRegistroAulaFrequenciaVOs;
	}

	public void setTransferenciaEntradaRegistroAulaFrequenciaVOs(List<TransferenciaEntradaRegistroAulaFrequenciaVO> transferenciaEntradaRegistroAulaFrequenciaVOs) {
		this.transferenciaEntradaRegistroAulaFrequenciaVOs = transferenciaEntradaRegistroAulaFrequenciaVOs;
	}
    
	public Boolean getSituacaoEstornado() {
        return getSituacao().equals(SituacaoTransferenciaEntrada.ESTORNADO.getValor());	            
	}
	
	public Date getDataEstorno() {
		if(dataEstorno == null){
			dataEstorno = new Date();
		}
		return dataEstorno;
	}
	
	public void setDataEstorno(Date dataEstorno) {
		this.dataEstorno = dataEstorno;
	}
	
	public UsuarioVO getResponsavelEstorno() {
		if(responsavelEstorno == null){
			responsavelEstorno = new UsuarioVO();
		}
		return responsavelEstorno;
	}
	
	public void setResponsavelEstorno(UsuarioVO responsavelEstorno) {
		this.responsavelEstorno = responsavelEstorno;
	}
	
	public String getMotivoEstorno() {
		if(motivoEstorno == null){
			motivoEstorno = "";
		}
		return motivoEstorno;
	}
	
	public void setMotivoEstorno(String motivoEstorno) {
		this.motivoEstorno = motivoEstorno;
	}

	public Boolean getCarregarDisciplinasAproveitadas() {
		if (carregarDisciplinasAproveitadas == null) {
			carregarDisciplinasAproveitadas = false;
		}
		return carregarDisciplinasAproveitadas;
	}

	public void setCarregarDisciplinasAproveitadas(Boolean carregarDisciplinasAproveitadas) {
		this.carregarDisciplinasAproveitadas = carregarDisciplinasAproveitadas;
	}

	public ProcessoMatriculaVO getProcessoMatriculaVO() {
		if(processoMatriculaVO == null) {
			processoMatriculaVO =  new ProcessoMatriculaVO();
		}
		return processoMatriculaVO;
	}

	public void setProcessoMatriculaVO(ProcessoMatriculaVO processoMatriculaVO) {
		this.processoMatriculaVO = processoMatriculaVO;
	}
	
	
}
