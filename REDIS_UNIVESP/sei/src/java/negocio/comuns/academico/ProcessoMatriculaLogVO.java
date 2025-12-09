package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

public class ProcessoMatriculaLogVO extends SuperVO  {

	private Integer codigo;
	private Integer codigoProcessoMatricula;
    private String descricao;
    private Date data;
    private Date dataInicio;
    private Date dataFinal;
    private Boolean validoPelaInternet;
    private Boolean exigeConfirmacaoPresencial;
    private String situacao;
    private String nivelProcessoMatricula;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>ProcessoMatriculaCalendario</code>.
     */
    private List<ProcessoMatriculaCalendarioVO> processoMatriculaCalendarioVOs;
    private ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>UnidadeEnsino </code>.
     */
    private UnidadeEnsinoVO unidadeEnsino;
    private Boolean apresentarProcessoVisaoAluno;
    private Boolean permiteIncluirExcluirDisciplinaVisaoAluno;
    private Date dataInicioMatriculaOnline;
    private Date dataFimMatriculaOnline;
    public static final long serialVersionUID = 1L;
    private String mensagemApresentarVisaoAluno;
    
    private Integer responsavel;
    private String operacao;

    /**
     * Construtor padrão da classe <code>ProcessoMatricula</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public ProcessoMatriculaLogVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ProcessoMatriculaVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ProcessoMatriculaVO obj) throws ConsistirException {
        if (obj.getDescricao().equals("")) {
            throw new ConsistirException("O campo DESCRIÇÃO (Processo de Matrícula) deve ser informado.");
        }
        if (obj.getUnidadeEnsinoDescricao().isEmpty()) {
            throw new ConsistirException("O campo UNIDADE ENSINO (Processo de Matrícula) deve ser informado.");
        }
        if (obj.getNivelProcessoMatricula().equals("")) {
            throw new ConsistirException("O campo NÍVEL PROCESSO MATRÍCULA (Processo de Matrícula) deve ser informado.");
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA CADASTRO (Processo de Matrícula) deve ser informado.");
        }
        if (obj.getDataInicio() == null) {
            throw new ConsistirException("O campo DATA INÍCIO MATRÍCULA (Processo de Matrícula) deve ser informado.");
        }
        if (obj.getDataFinal() == null) {
            throw new ConsistirException("O campo DATA FINAL MATRÍCULA (Processo de Matrícula) deve ser informado.");
        }
        if (obj.getDataInicio().compareTo(obj.getDataFinal()) > 0) {
            throw new ConsistirException("O campo DATA INÍCIO MATRÍCULA (Processo de Matrícula) deve ser menor do que o campo DATA FINAL MATRÍCULA (Processo Matrícula).");
        }
        if (obj.getSituacao().equals("")) {
            throw new ConsistirException("O campo SITUAÇÃO (Processo de Matrícula) deve ser informado.");
        }
        if (obj.getProcessoMatriculaCalendarioVOs().isEmpty()) {
            throw new ConsistirException("Pelo menos um CALENDÁRIO PROCESSO DE MATRÍCULA deve ser informado.");
        }
        if (obj.getApresentarProcessoVisaoAluno()) {
            if (obj.getDataInicioMatriculaOnline() == null || obj.getDataFimMatriculaOnline() == null) {
                throw  new ConsistirException("O campo PERÍODO MATRÍCULA ONLINE deve ser informado");
            }
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setDescricao("");
        setData(new Date());
        setDataInicio(new Date());
        setDataFinal(new Date());
        setValidoPelaInternet(Boolean.TRUE);
        setExigeConfirmacaoPresencial(Boolean.FALSE);
        setSituacao(new String("AT"));
    }



    /**
     * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>ProcessoMatricula</code>).
     */
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return (unidadeEnsino);
    }

    /**
     * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>ProcessoMatricula</code>).
     */
    public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
        this.unidadeEnsino = obj;
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>ProcessoMatriculaCalendario</code>.
     */
    public List<ProcessoMatriculaCalendarioVO> getProcessoMatriculaCalendarioVOs() {
        if (processoMatriculaCalendarioVOs == null) {
            processoMatriculaCalendarioVOs = new ArrayList();
        }
        return (processoMatriculaCalendarioVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>ProcessoMatriculaCalendario</code>.
     */
    public void setProcessoMatriculaCalendarioVOs(List<ProcessoMatriculaCalendarioVO> processoMatriculaCalendarioVOs) {
        this.processoMatriculaCalendarioVOs = processoMatriculaCalendarioVOs;
    }

    public String getSituacao() {
        return (situacao);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getSituacao_Apresentar() {
        if (situacao.equals("FI")) {
            return "Finalizado";
        }
        if (situacao.equals("AT")) {
            return "Ativo - Matrícula";
        }
        if (situacao.equals("PR")) {
            return "Ativo - Pré-Matrícula";
        }
        return (situacao);
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Boolean getExigeConfirmacaoPresencial() {
        return (exigeConfirmacaoPresencial);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getExigeConfirmacaoPresencial_Apresentar() {
        if (exigeConfirmacaoPresencial) {
            return "Sim";
        } else {
            return "Não";
        }
    }

    public Boolean isExigeConfirmacaoPresencial() {
        return (exigeConfirmacaoPresencial);
    }

    public void setExigeConfirmacaoPresencial(Boolean exigeConfirmacaoPresencial) {
        this.exigeConfirmacaoPresencial = exigeConfirmacaoPresencial;
    }

    public Boolean getValidoPelaInternet() {
        return (validoPelaInternet);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getValidoPelaInternet_Apresentar() {
        if (validoPelaInternet) {
            return "Sim";
        } else {
            return "Não";
        }
    }

    public Boolean isValidoPelaInternet() {
        return (validoPelaInternet);
    }

    public void setValidoPelaInternet(Boolean validoPelaInternet) {
        this.validoPelaInternet = validoPelaInternet;
    }

    public Date getDataFinal() {
        return (dataFinal);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataFinal_Apresentar() {
        return (Uteis.getData(dataFinal));
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Date getDataInicio() {
        return (dataInicio);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataInicio_Apresentar() {
        return (Uteis.getData(dataInicio));
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getData() {
        return (data);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getData_Apresentar() {
        return (Uteis.getData(data));
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescricao() {
        return (descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Boolean ativoParaMatriculaEPreMatricula(Date dataBase) {
        // if this.getDataInicio().compareTo(dataBase) >=
        return true;
    }

    public ProcessoMatriculaCalendarioVO getProcessoMatriculaCalendarioVO() {
        if (processoMatriculaCalendarioVO == null) {
            processoMatriculaCalendarioVO = new ProcessoMatriculaCalendarioVO();
        }
        return processoMatriculaCalendarioVO;
    }

    public void setProcessoMatriculaCalendarioVO(ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO) {
        this.processoMatriculaCalendarioVO = processoMatriculaCalendarioVO;
    }

    /**
     * @return the nivelProcessoMatricula
     */
    public String getNivelProcessoMatricula() {
        if (nivelProcessoMatricula == null) {
            nivelProcessoMatricula = "";
        }
        return nivelProcessoMatricula;
    }

    /**
     * @param nivelProcessoMatricula the nivelProcessoMatricula to set
     */
    public void setNivelProcessoMatricula(String nivelProcessoMatricula) {
        this.nivelProcessoMatricula = nivelProcessoMatricula;
    }

    public Boolean getApresentarProcessoVisaoAluno() {
        if (apresentarProcessoVisaoAluno == null) {
            apresentarProcessoVisaoAluno = Boolean.FALSE;
        }
        return apresentarProcessoVisaoAluno;
    }

    public void setApresentarProcessoVisaoAluno(Boolean apresentarProcessoVisaoAluno) {
        this.apresentarProcessoVisaoAluno = apresentarProcessoVisaoAluno;
    }

    public Date getDataInicioMatriculaOnline() {
        return dataInicioMatriculaOnline;
    }

    public void setDataInicioMatriculaOnline(Date dataInicioMatriculaOnline) {
        this.dataInicioMatriculaOnline = dataInicioMatriculaOnline;
    }

    public Date getDataFimMatriculaOnline() {
        return dataFimMatriculaOnline;
    }

    public void setDataFimMatriculaOnline(Date dataFimMatriculaOnline) {
        this.dataFimMatriculaOnline = dataFimMatriculaOnline;
    }

    
    public String getMensagemApresentarVisaoAluno() {
        if(mensagemApresentarVisaoAluno==null){
            mensagemApresentarVisaoAluno = "";
        }
        return mensagemApresentarVisaoAluno;
    }

    
    public void setMensagemApresentarVisaoAluno(String mensagemApresentarVisaoAluno) {
        this.mensagemApresentarVisaoAluno = mensagemApresentarVisaoAluno;
    }

    
    public Boolean getPermiteIncluirExcluirDisciplinaVisaoAluno() {
        if(permiteIncluirExcluirDisciplinaVisaoAluno == null){
            permiteIncluirExcluirDisciplinaVisaoAluno = false;
        }
        return permiteIncluirExcluirDisciplinaVisaoAluno;
    }

    
    public void setPermiteIncluirExcluirDisciplinaVisaoAluno(Boolean permiteIncluirExcluirDisciplinaVisaoAluno) {
        this.permiteIncluirExcluirDisciplinaVisaoAluno = permiteIncluirExcluirDisciplinaVisaoAluno;
    }

	public Integer getResponsavel() {
		if (responsavel == null) {
			responsavel = 0;
		}
		return responsavel;
	}

	public void setResponsavel(Integer responsavel) {
		this.responsavel = responsavel;
	}

	public String getOperacao() {
		if (operacao == null) {
			operacao = "";
		}
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}

	public Integer getCodigoProcessoMatricula() {
		if (codigoProcessoMatricula == null) {
			codigoProcessoMatricula  = 0;
		}
		return codigoProcessoMatricula;
	}

	public void setCodigoProcessoMatricula(Integer codigoProcessoMatricula) {
		this.codigoProcessoMatricula = codigoProcessoMatricula;
	}
}
