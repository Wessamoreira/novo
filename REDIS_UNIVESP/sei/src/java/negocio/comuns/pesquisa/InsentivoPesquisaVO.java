package negocio.comuns.pesquisa;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade InsentivoPesquisa. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class InsentivoPesquisaVO extends SuperVO {

    protected Integer codigo;
    protected Date data;
    protected String nome;
    protected String descricao;
    protected String orgaoPromotor;
    protected Date dataInicialInscricao;
    protected Date dataFinalInscricao;
    private String prerequisitos;
    private String documentacao;
    private String areasConhecimento;
    private String sitePrograma;
    private String tipoInsentivo;
    private String contatosOrgaoPromotor;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>InsentivoPesquisa</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public InsentivoPesquisaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>InsentivoPesquisaVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(InsentivoPesquisaVO obj) throws ConsistirException {
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Insentivo Pesquisa) deve ser informado.");
        }
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Insentivo Pesquisa) deve ser informado.");
        }
        if (obj.getOrgaoPromotor().equals("")) {
            throw new ConsistirException("O campo ORGÃO PROMOTOR (Insentivo Pesquisa) deve ser informado.");
        }
        if (obj.getDataInicialInscricao() == null) {
            throw new ConsistirException("O campo DATA INÍCIAL INSCRIÇÃO (Insentivo Pesquisa) deve ser informado.");
        }
        if (obj.getDataFinalInscricao() == null) {
            throw new ConsistirException("O campo DATA FINAL INSCRIÇÃO (Insentivo Pesquisa) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setData(new Date());
        setNome("");
        setDescricao("");
        setOrgaoPromotor("");
        setDataInicialInscricao(new Date());
        setDataFinalInscricao(new Date());
        setPrerequisitos("");
        setDocumentacao("");
        setAreasConhecimento("");
        setSitePrograma("");
        setTipoInsentivo("");
        setContatosOrgaoPromotor("");
    }

    public String getContatosOrgaoPromotor() {
        return (contatosOrgaoPromotor);
    }

    public void setContatosOrgaoPromotor(String contatosOrgaoPromotor) {
        this.contatosOrgaoPromotor = contatosOrgaoPromotor;
    }

    public String getTipoInsentivo() {
        return (tipoInsentivo);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoInsentivo_Apresentar() {
        if (tipoInsentivo.equals("MA")) {
            return "Materiais";
        }
        if (tipoInsentivo.equals("PA")) {
            return "Passagens";
        }
        if (tipoInsentivo.equals("BO")) {
            return "Bolsas";
        }
        if (tipoInsentivo.equals("DG")) {
            return "Despesas Gerais";
        }
        return (tipoInsentivo);
    }

    public void setTipoInsentivo(String tipoInsentivo) {
        this.tipoInsentivo = tipoInsentivo;
    }

    public String getSitePrograma() {
        return (sitePrograma);
    }

    public void setSitePrograma(String sitePrograma) {
        this.sitePrograma = sitePrograma;
    }

    public String getAreasConhecimento() {
        return (areasConhecimento);
    }

    public void setAreasConhecimento(String areasConhecimento) {
        this.areasConhecimento = areasConhecimento;
    }

    public String getDocumentacao() {
        return (documentacao);
    }

    public void setDocumentacao(String documentacao) {
        this.documentacao = documentacao;
    }

    public String getPrerequisitos() {
        return (prerequisitos);
    }

    public void setPrerequisitos(String prerequisitos) {
        this.prerequisitos = prerequisitos;
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

    public String getOrgaoPromotor() {
        return (orgaoPromotor);
    }

    public void setOrgaoPromotor(String orgaoPromotor) {
        this.orgaoPromotor = orgaoPromotor;
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

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
