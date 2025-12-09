package negocio.comuns.pesquisa;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade LinhaPesquisa. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class LinhaPesquisaVO extends SuperVO {

    private Integer codigo;
    private String nome;
    private String descricao;
    private String objetivos;
    private String palavrasChaves;
    private String setoresAplicacao;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Funcionario </code>.
     */
    private FuncionarioVO lider;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>GrupoPesquisa </code>.
     */
    private GrupoPesquisaVO grupoPesquisa;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>AreaConhecimento </code>.
     */
    private AreaConhecimentoVO areaConhecimento;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>LinhaPesquisa</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public LinhaPesquisaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>LinhaPesquisaVO</code>. Todos os tipos de consistência de dados são
     * e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(LinhaPesquisaVO obj) throws ConsistirException {
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Linha de Pesquisa) deve ser informado.");
        }
        if ((obj.getLider() == null) || (obj.getLider().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo LIDER (Linha de Pesquisa) deve ser informado.");
        }
        if ((obj.getGrupoPesquisa() == null) || (obj.getGrupoPesquisa().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo GRUPO DE PESQUISA (Linha de Pesquisa) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setNome("");
        setDescricao("");
        setObjetivos("");
        setPalavrasChaves("");
        setSetoresAplicacao("");
    }

    /**
     * Retorna o objeto da classe <code>AreaConhecimento</code> relacionado com
     * (<code>LinhaPesquisa</code>).
     */
    public AreaConhecimentoVO getAreaConhecimento() {
        if (areaConhecimento == null) {
            areaConhecimento = new AreaConhecimentoVO();
        }
        return (areaConhecimento);
    }

    /**
     * Define o objeto da classe <code>AreaConhecimento</code> relacionado com (
     * <code>LinhaPesquisa</code>).
     */
    public void setAreaConhecimento(AreaConhecimentoVO obj) {
        this.areaConhecimento = obj;
    }

    /**
     * Retorna o objeto da classe <code>GrupoPesquisa</code> relacionado com (
     * <code>LinhaPesquisa</code>).
     */
    public GrupoPesquisaVO getGrupoPesquisa() {
        if (grupoPesquisa == null) {
            grupoPesquisa = new GrupoPesquisaVO();
        }
        return (grupoPesquisa);
    }

    /**
     * Define o objeto da classe <code>GrupoPesquisa</code> relacionado com (
     * <code>LinhaPesquisa</code>).
     */
    public void setGrupoPesquisa(GrupoPesquisaVO obj) {
        this.grupoPesquisa = obj;
    }

    /**
     * Retorna o objeto da classe <code>Funcionario</code> relacionado com (
     * <code>LinhaPesquisa</code>).
     */
    public FuncionarioVO getLider() {
        if (lider == null) {
            lider = new FuncionarioVO();
        }
        return (lider);
    }

    /**
     * Define o objeto da classe <code>Funcionario</code> relacionado com (
     * <code>LinhaPesquisa</code>).
     */
    public void setLider(FuncionarioVO obj) {
        this.lider = obj;
    }

    public String getSetoresAplicacao() {
        return (setoresAplicacao);
    }

    public void setSetoresAplicacao(String setoresAplicacao) {
        this.setoresAplicacao = setoresAplicacao;
    }

    public String getPalavrasChaves() {
        return (palavrasChaves);
    }

    public void setPalavrasChaves(String palavrasChaves) {
        this.palavrasChaves = palavrasChaves;
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
