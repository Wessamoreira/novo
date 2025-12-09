package negocio.comuns.pesquisa;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade Congressos. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class CongressosVO extends SuperVO {

    private Integer codigo;
    protected String nome;
    protected Date dataInicialRealizacao;
    protected Date dataFinalRealizacao;
    protected String descricao;
    protected Date dataInicialInscricao;
    protected Date dataFinalInscricao;
    private Boolean paraPulbicacao;
    private String promotor;
    private String site;
    private String contatosPromotor;
    private String localRealizacao;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>Congressos</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public CongressosVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>CongressosVO</code>. Todos os tipos de consistência de dados são e
     * devem ser implementadas neste método. São validações típicas: verificação
     * de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(CongressosVO obj) throws ConsistirException {
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Congressos) deve ser informado.");
        }
        if (obj.getDataInicialRealizacao() == null) {
            throw new ConsistirException("O campo DATA INÍCIAL REALIZAÇÃO (Congressos) deve ser informado.");
        }
        if (obj.getDataFinalRealizacao() == null) {
            throw new ConsistirException("O campo DATA FINAL REALIZAÇÃO (Congressos) deve ser informado.");
        }
        if (obj.getDataInicialInscricao() == null) {
            throw new ConsistirException("O campo DATA INÍCIAL INSCRIÇÃO (Congressos) deve ser informado.");
        }
        if (obj.getDataFinalInscricao() == null) {
            throw new ConsistirException("O campo DATA FINAL INSCRIÇÃO (Congressos) deve ser informado.");
        }
        if (obj.getPromotor().equals("")) {
            throw new ConsistirException("O campo PROMOTOR (Congressos) deve ser informado.");
        }
        if (obj.getLocalRealizacao().equals("")) {
            throw new ConsistirException("O campo LOCAL REALIZAÇÃO (Congressos) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setNome("");
        setDataInicialRealizacao(new Date());
        setDataFinalRealizacao(new Date());
        setDescricao("");
        setDataInicialInscricao(new Date());
        setDataFinalInscricao(new Date());
        setParaPulbicacao(Boolean.FALSE);
        setPromotor("");
        setSite("");
        setContatosPromotor("");
        setLocalRealizacao("");
    }

    public String getLocalRealizacao() {
        return (localRealizacao);
    }

    public void setLocalRealizacao(String localRealizacao) {
        this.localRealizacao = localRealizacao;
    }

    public String getContatosPromotor() {
        return (contatosPromotor);
    }

    public void setContatosPromotor(String contatosPromotor) {
        this.contatosPromotor = contatosPromotor;
    }

    public String getSite() {
        return (site);
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getPromotor() {
        return (promotor);
    }

    public void setPromotor(String promotor) {
        this.promotor = promotor;
    }

    public Boolean getParaPulbicacao() {
        return (paraPulbicacao);
    }

    public Boolean isParaPulbicacao() {
        return (paraPulbicacao);
    }

    public void setParaPulbicacao(Boolean paraPulbicacao) {
        this.paraPulbicacao = paraPulbicacao;
    }

    public Date getDataFinalInscricao() {
        return (dataFinalInscricao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataFinalInscricao_Apresentar() {
        return (Uteis.getData(dataFinalInscricao));
    }

    public void setDataFinalInscricao(Date dataFinalInscricao) {
        this.dataFinalInscricao = dataFinalInscricao;
    }

    public Date getDataInicialInscricao() {
        return (dataInicialInscricao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataInicialInscricao_Apresentar() {
        return (Uteis.getData(dataInicialInscricao));
    }

    public void setDataInicialInscricao(Date dataInicialInscricao) {
        this.dataInicialInscricao = dataInicialInscricao;
    }

    public String getDescricao() {
        return (descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataFinalRealizacao() {
        return (dataFinalRealizacao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataFinalRealizacao_Apresentar() {
        return (Uteis.getData(dataFinalRealizacao));
    }

    public void setDataFinalRealizacao(Date dataFinalRealizacao) {
        this.dataFinalRealizacao = dataFinalRealizacao;
    }

    public Date getDataInicialRealizacao() {
        return (dataInicialRealizacao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataInicialRealizacao_Apresentar() {
        return (Uteis.getData(dataInicialRealizacao));
    }

    public void setDataInicialRealizacao(Date dataInicialRealizacao) {
        this.dataInicialRealizacao = dataInicialRealizacao;
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
