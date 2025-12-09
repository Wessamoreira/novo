package negocio.comuns.pesquisa;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade ProjetoPesquisa. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ProjetoPesquisaVO extends SuperVO {

    protected Integer codigo;
    protected String nome;
    protected Integer linhaPesquisa;
    protected Date dataCriacao;
    protected String palavrasChave;
    protected String objetivos;
    protected String justificativa;
    protected String impactos;
    protected Integer duracaoPrevista;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>PesquisadorProjetoPesquisa</code>.
     */
    private List pesquisadorProjetoPesquisaVOs;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>AreaConhecimento </code>.
     */
    protected AreaConhecimentoVO areaConhecimento;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ProjetoPesquisa</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public ProjetoPesquisaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ProjetoPesquisaVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ProjetoPesquisaVO obj) throws ConsistirException {
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Projeto de Pesquisa) deve ser informado.");
        }
        if (obj.getLinhaPesquisa().intValue() == 0) {
            throw new ConsistirException("O campo LINHA DE PESQUISA (Projeto de Pesquisa) deve ser informado.");
        }
        if (obj.getDataCriacao() == null) {
            throw new ConsistirException("O campo DATA CRIAÇÃO (Projeto de Pesquisa) deve ser informado.");
        }
        if (obj.getDuracaoPrevista().intValue() == 0) {
            throw new ConsistirException("O campo DURAÇÃO PREVISTA (Projeto de Pesquisa) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setNome("");
        setLinhaPesquisa(0);
        setDataCriacao(new Date());
        setPalavrasChave("");
        setObjetivos("");
        setJustificativa("");
        setImpactos("");
        setDuracaoPrevista(0);
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>PesquisadorProjetoPesquisaVO</code> ao List
     * <code>pesquisadorProjetoPesquisaVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>PesquisadorProjetoPesquisa</code> -
     * getPesquisadorLinhaPesquisa().getCodigo() - como identificador (key) do
     * objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>PesquisadorProjetoPesquisaVO</code> que
     *            será adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjPesquisadorProjetoPesquisaVOs(PesquisadorProjetoPesquisaVO obj) throws Exception {
        PesquisadorProjetoPesquisaVO.validarDados(obj);
        int index = 0;
        Iterator i = getPesquisadorProjetoPesquisaVOs().iterator();
        while (i.hasNext()) {
            PesquisadorProjetoPesquisaVO objExistente = (PesquisadorProjetoPesquisaVO) i.next();
            if (objExistente.getPesquisadorLinhaPesquisa().getCodigo().equals(obj.getPesquisadorLinhaPesquisa().getCodigo())) {
                getPesquisadorProjetoPesquisaVOs().set(index, obj);
                return;
            }
            index++;
        }
        getPesquisadorProjetoPesquisaVOs().add(obj);
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>PesquisadorProjetoPesquisaVO</code> no List
     * <code>pesquisadorProjetoPesquisaVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>PesquisadorProjetoPesquisa</code> -
     * getPesquisadorLinhaPesquisa().getCodigo() - como identificador (key) do
     * objeto no List.
     *
     * @param pesquisadorLinhaPesquisa
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjPesquisadorProjetoPesquisaVOs(Integer pesquisadorLinhaPesquisa) throws Exception {
        int index = 0;
        Iterator i = getPesquisadorProjetoPesquisaVOs().iterator();
        while (i.hasNext()) {
            PesquisadorProjetoPesquisaVO objExistente = (PesquisadorProjetoPesquisaVO) i.next();
            if (objExistente.getPesquisadorLinhaPesquisa().getCodigo().equals(pesquisadorLinhaPesquisa)) {
                getPesquisadorProjetoPesquisaVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>PesquisadorProjetoPesquisaVO</code> no List
     * <code>pesquisadorProjetoPesquisaVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>PesquisadorProjetoPesquisa</code> -
     * getPesquisadorLinhaPesquisa().getCodigo() - como identificador (key) do
     * objeto no List.
     *
     * @param pesquisadorLinhaPesquisa
     *            Parâmetro para localizar o objeto do List.
     */
    public PesquisadorProjetoPesquisaVO consultarObjPesquisadorProjetoPesquisaVO(Integer pesquisadorLinhaPesquisa) throws Exception {
        Iterator i = getPesquisadorProjetoPesquisaVOs().iterator();
        while (i.hasNext()) {
            PesquisadorProjetoPesquisaVO objExistente = (PesquisadorProjetoPesquisaVO) i.next();
            if (objExistente.getPesquisadorLinhaPesquisa().getCodigo().equals(pesquisadorLinhaPesquisa)) {
                return objExistente;
            }
        }
        return null;
    }

    /**
     * Retorna o objeto da classe <code>AreaConhecimento</code> relacionado com
     * (<code>ProjetoPesquisa</code>).
     */
    public AreaConhecimentoVO getAreaConhecimento() {
        if (areaConhecimento == null) {
            areaConhecimento = new AreaConhecimentoVO();
        }
        return (areaConhecimento);
    }

    /**
     * Define o objeto da classe <code>AreaConhecimento</code> relacionado com (
     * <code>ProjetoPesquisa</code>).
     */
    public void setAreaConhecimento(AreaConhecimentoVO obj) {
        this.areaConhecimento = obj;
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>PesquisadorProjetoPesquisa</code>.
     */
    public List getPesquisadorProjetoPesquisaVOs() {
        if (pesquisadorProjetoPesquisaVOs == null) {
            pesquisadorProjetoPesquisaVOs = new ArrayList();
        }
        return (pesquisadorProjetoPesquisaVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>PesquisadorProjetoPesquisa</code>.
     */
    public void setPesquisadorProjetoPesquisaVOs(List pesquisadorProjetoPesquisaVOs) {
        this.pesquisadorProjetoPesquisaVOs = pesquisadorProjetoPesquisaVOs;
    }

    public Integer getDuracaoPrevista() {
        return (duracaoPrevista);
    }

    public void setDuracaoPrevista(Integer duracaoPrevista) {
        this.duracaoPrevista = duracaoPrevista;
    }

    public String getImpactos() {
        return (impactos);
    }

    public void setImpactos(String impactos) {
        this.impactos = impactos;
    }

    public String getJustificativa() {
        return (justificativa);
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public String getObjetivos() {
        return (objetivos);
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public String getPalavrasChave() {
        return (palavrasChave);
    }

    public void setPalavrasChave(String palavrasChave) {
        this.palavrasChave = palavrasChave;
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

    public Integer getLinhaPesquisa() {
        return (linhaPesquisa);
    }

    public void setLinhaPesquisa(Integer linhaPesquisa) {
        this.linhaPesquisa = linhaPesquisa;
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
