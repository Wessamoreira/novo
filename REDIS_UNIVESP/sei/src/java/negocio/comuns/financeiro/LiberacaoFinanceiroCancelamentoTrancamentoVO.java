package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade
 * LiberacaoFinanceiroCancelamentoTrancamento. Classe do tipo VO - Value Object
 * composta pelos atributos da entidade com visibilidade protegida e os métodos
 * de acesso a estes atributos. Classe utilizada para apresentar e manter em
 * memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class LiberacaoFinanceiroCancelamentoTrancamentoVO extends SuperVO {

    private Integer codigo;
    private Date dataCadastro;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamento</code>.
     */
    private List<HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO> historicoLiberacaoFinanceiroCancelamentoTrancamentoVOs;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Matricula </code>.
     */
    private MatriculaVO matriculaAluno;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>UnidadeEnsino </code>.
     */
    private UnidadeEnsinoVO unidadeEnsino;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Usuario </code>.
     */
    private UsuarioVO responsavel;
    private String situacao;
    private Date dataAlteracao;
    private String motivoEstorno;
    private UsuarioVO responsavelEstorno;
    private String motivoLiberacaoFinanceiro;
    
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe
     * <code>LiberacaoFinanceiroCancelamentoTrancamento</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public LiberacaoFinanceiroCancelamentoTrancamentoVO() {
        super();
    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da
     * classe <code>LiberacaoFinanceiroCancelamentoTrancamentoVO</code>.
     */
    public static void validarUnicidade(List<LiberacaoFinanceiroCancelamentoTrancamentoVO> lista, LiberacaoFinanceiroCancelamentoTrancamentoVO obj) throws ConsistirException {
        for (LiberacaoFinanceiroCancelamentoTrancamentoVO repetido : lista) {
        }
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>LiberacaoFinanceiroCancelamentoTrancamentoVO</code>. Todos os tipos
     * de consistência de dados são e devem ser implementadas neste método. São
     * validações típicas: verificação de campos obrigatórios, verificação de
     * valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(LiberacaoFinanceiroCancelamentoTrancamentoVO obj) throws ConsistirException, Exception {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getMatriculaAluno().getMatricula() == null) || (obj.getMatriculaAluno().getMatricula().equals(""))) {
            throw new Exception("O campo MATRÍCULA ALUNO (Dados Básicos) deve ser informado");
        }
        if ((obj.getUnidadeEnsino() == null) || (obj.getUnidadeEnsino().getCodigo().intValue() == 0)) {
            // consistir.adicionarListaMensagemErro(getMensagemInternalizacao("msg_LiberacaoFinanceiroCancelamentoTrancamento_unidadeEnsino"));
        }
        if (obj.getDataCadastro() == null) {
            // consistir.adicionarListaMensagemErro(getMensagemInternalizacao("msg_LiberacaoFinanceiroCancelamentoTrancamento_dataCadastro"));
        }
        if ((obj.getResponsavel() == null) || (obj.getResponsavel().getCodigo().intValue() == 0)) {
            // consistir.adicionarListaMensagemErro(getMensagemInternalizacao("msg_LiberacaoFinanceiroCancelamentoTrancamento_responsavel"));
        }
        // if (consistir.existeErroListaMensagemErro()) {
        // throw consistir;
        // }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code> ao
     * List <code>historicoLiberacaoFinanceiroCancelamentoTrancamentoVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamento</code> -
     * getLiberacaoFinanceiroCancelamentoTrancamento().getCodigo() - como
     * identificador (key) do objeto no List.
     *
     * @param obj
     *            Objeto da classe
     *            <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code>
     *            que será adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVOs(HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO obj) throws Exception {
        HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO.validarDados(obj);
        obj.setLiberacaoFinanceiroCancelamentoTrancamento(this);
        int index = 0;
        Iterator i = getHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVOs().iterator();
        while (i.hasNext()) {
            HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO objExistente = (HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO) i.next();
            if (objExistente.getDescricao().equals(obj.getDescricao())) {
                getHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVOs().set(index, obj);
                return;
            }
            index++;
        }
        getHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVOs().add(obj);
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code> no
     * List <code>historicoLiberacaoFinanceiroCancelamentoTrancamentoVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamento</code> -
     * getLiberacaoFinanceiroCancelamentoTrancamento().getCodigo() - como
     * identificador (key) do objeto no List.
     *
     * @param liberacaoFinanceiroCancelamentoTrancamento
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVOs(Integer liberacaoFinanceiroCancelamentoTrancamento) throws Exception {
        int index = 0;
        Iterator i = getHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVOs().iterator();
        while (i.hasNext()) {
            HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO objExistente = (HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO) i.next();
            if (objExistente.getLiberacaoFinanceiroCancelamentoTrancamento().getCodigo().equals(liberacaoFinanceiroCancelamentoTrancamento)) {
                getHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code> no
     * List <code>historicoLiberacaoFinanceiroCancelamentoTrancamentoVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamento</code> -
     * getLiberacaoFinanceiroCancelamentoTrancamento().getCodigo() - como
     * identificador (key) do objeto no List.
     *
     * @param liberacaoFinanceiroCancelamentoTrancamento
     *            Parâmetro para localizar o objeto do List.
     */
    public HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO consultarObjHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO(Integer liberacaoFinanceiroCancelamentoTrancamento) throws Exception {
        Iterator i = getHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVOs().iterator();
        while (i.hasNext()) {
            HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO objExistente = (HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO) i.next();
            if (objExistente.getLiberacaoFinanceiroCancelamentoTrancamento().getCodigo().equals(liberacaoFinanceiroCancelamentoTrancamento)) {
                return objExistente;
            }
        }
        return null;
    }

    /**
     * Retorna o objeto da classe <code>Usuario</code> relacionado com (
     * <code>LiberacaoFinanceiroCancelamentoTrancamento</code>).
     */
    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return (responsavel);
    }

    /**
     * Define o objeto da classe <code>Usuario</code> relacionado com (
     * <code>LiberacaoFinanceiroCancelamentoTrancamento</code>).
     */
    public void setResponsavel(UsuarioVO obj) {
        this.responsavel = obj;
    }

    /**
     * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>LiberacaoFinanceiroCancelamentoTrancamento</code>).
     */
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return (unidadeEnsino);
    }

    /**
     * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>LiberacaoFinanceiroCancelamentoTrancamento</code>).
     */
    public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
        this.unidadeEnsino = obj;
    }

    /**
     * Retorna o objeto da classe <code>Matricula</code> relacionado com (
     * <code>LiberacaoFinanceiroCancelamentoTrancamento</code>).
     */
    public MatriculaVO getMatriculaAluno() {
        if (matriculaAluno == null) {
            matriculaAluno = new MatriculaVO();
        }
        return (matriculaAluno);
    }

    /**
     * Define o objeto da classe <code>Matricula</code> relacionado com (
     * <code>LiberacaoFinanceiroCancelamentoTrancamento</code>).
     */
    public void setMatriculaAluno(MatriculaVO obj) {
        this.matriculaAluno = obj;
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamento</code>.
     */
    public List getHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVOs() {
        if (historicoLiberacaoFinanceiroCancelamentoTrancamentoVOs == null) {
            historicoLiberacaoFinanceiroCancelamentoTrancamentoVOs = new ArrayList(0);
        }
        return (historicoLiberacaoFinanceiroCancelamentoTrancamentoVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamento</code>.
     */
    public void setHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVOs(List historicoLiberacaoFinanceiroCancelamentoTrancamentoVOs) {
        this.historicoLiberacaoFinanceiroCancelamentoTrancamentoVOs = historicoLiberacaoFinanceiroCancelamentoTrancamentoVOs;
    }

    public Date getDataCadastro() {
        if (dataCadastro == null) {
            dataCadastro = new Date();
        }
        return (dataCadastro);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataCadastro_Apresentar() {
        return (Uteis.getData(dataCadastro));
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
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

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Date getDataAlteracao() {
		if (dataAlteracao == null) {
			dataAlteracao = new Date();
		}
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public String getMotivoEstorno() {
		if (motivoEstorno == null) {
			motivoEstorno = "";
		}
		return motivoEstorno;
	}

	public void setMotivoEstorno(String motivoEstorno) {
		this.motivoEstorno = motivoEstorno;
	}

	public UsuarioVO getResponsavelEstorno() {
		if (responsavelEstorno == null) {
			responsavelEstorno = new UsuarioVO();
		}
		return responsavelEstorno;
	}

	public void setResponsavelEstorno(UsuarioVO responsavelEstorno) {
		this.responsavelEstorno = responsavelEstorno;
	}

	public String getMotivoLiberacaoFinanceiro() {
		if (motivoLiberacaoFinanceiro == null) {
			motivoLiberacaoFinanceiro = "";
		}
		return motivoLiberacaoFinanceiro;
	}

	public void setMotivoLiberacaoFinanceiro(String motivoLiberacaoFinanceiro) {
		this.motivoLiberacaoFinanceiro = motivoLiberacaoFinanceiro;
	}
}
