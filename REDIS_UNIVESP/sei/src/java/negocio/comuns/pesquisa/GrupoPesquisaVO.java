package negocio.comuns.pesquisa;

import java.util.Date;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade GrupoPesquisa. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class GrupoPesquisaVO extends SuperVO {

    protected Integer codigo;
    protected String nome;
    protected String sigla;
    protected Date dataCriacao;
    private String descricao;
    private String objetivos;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Funcionario </code>.
     */
    private FuncionarioVO liderPrincipal;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Funcionario </code>.
     */
    private FuncionarioVO liderSecundario;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>AreaConhecimento </code>.
     */
    private AreaConhecimentoVO areaConhecimento;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>UnidadeEnsino </code>.
     */
    private UnidadeEnsinoVO unidadeEnsino;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Curso </code>.
     */
    private CursoVO curso;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>GrupoPesquisa</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public GrupoPesquisaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>GrupoPesquisaVO</code>. Todos os tipos de consistência de dados são
     * e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(GrupoPesquisaVO obj) throws ConsistirException {
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Grupo de Pesquisa) deve ser informado.");
        }
        if ((obj.getLiderPrincipal() == null) || (obj.getLiderPrincipal().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo LIDER PRINCIPAL (Grupo de Pesquisa) deve ser informado.");
        }
        if ((obj.getAreaConhecimento() == null) || (obj.getAreaConhecimento().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo ÁREA DE CONHECIMENTO (Grupo de Pesquisa) deve ser informado.");
        }
        if ((obj.getUnidadeEnsino() == null) || (obj.getUnidadeEnsino().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo UNIDADE DE ENSINO (Grupo de Pesquisa) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setNome("");
        setSigla("");
        setDataCriacao(new Date());
        setDescricao("");
        setObjetivos("");
    }

    /**
     * Retorna o objeto da classe <code>Curso</code> relacionado com (
     * <code>GrupoPesquisa</code>).
     */
    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return (curso);
    }

    /**
     * Define o objeto da classe <code>Curso</code> relacionado com (
     * <code>GrupoPesquisa</code>).
     */
    public void setCurso(CursoVO obj) {
        this.curso = obj;
    }

    /**
     * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>GrupoPesquisa</code>).
     */
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return (unidadeEnsino);
    }

    /**
     * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>GrupoPesquisa</code>).
     */
    public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
        this.unidadeEnsino = obj;
    }

    /**
     * Retorna o objeto da classe <code>AreaConhecimento</code> relacionado com
     * (<code>GrupoPesquisa</code>).
     */
    public AreaConhecimentoVO getAreaConhecimento() {
        if (areaConhecimento == null) {
            areaConhecimento = new AreaConhecimentoVO();
        }
        return (areaConhecimento);
    }

    /**
     * Define o objeto da classe <code>AreaConhecimento</code> relacionado com (
     * <code>GrupoPesquisa</code>).
     */
    public void setAreaConhecimento(AreaConhecimentoVO obj) {
        this.areaConhecimento = obj;
    }

    /**
     * Retorna o objeto da classe <code>Funcionario</code> relacionado com (
     * <code>GrupoPesquisa</code>).
     */
    public FuncionarioVO getLiderSecundario() {
        if (liderSecundario == null) {
            liderSecundario = new FuncionarioVO();
        }
        return (liderSecundario);
    }

    /**
     * Define o objeto da classe <code>Funcionario</code> relacionado com (
     * <code>GrupoPesquisa</code>).
     */
    public void setLiderSecundario(FuncionarioVO obj) {
        this.liderSecundario = obj;
    }

    /**
     * Retorna o objeto da classe <code>Funcionario</code> relacionado com (
     * <code>GrupoPesquisa</code>).
     */
    public FuncionarioVO getLiderPrincipal() {
        if (liderPrincipal == null) {
            liderPrincipal = new FuncionarioVO();
        }
        return (liderPrincipal);
    }

    /**
     * Define o objeto da classe <code>Funcionario</code> relacionado com (
     * <code>GrupoPesquisa</code>).
     */
    public void setLiderPrincipal(FuncionarioVO obj) {
        this.liderPrincipal = obj;
    }

    public String getObjetivos() {
        return (objetivos);
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public String getDescricao() {
        return (descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataCriacao() {
        return (dataCriacao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataCriacao_Apresentar() {
        return (Uteis.getData(dataCriacao));
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getSigla() {
        return (sigla);
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getNome() {
        return (nome);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
