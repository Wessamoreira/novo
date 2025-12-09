package negocio.comuns.administrativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade CampanhaMarketing. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class CampanhaMarketingVO extends SuperVO {

    private Integer codigo;
    private String descricao;
    private String situacao;
    private String objetivo;
    private String publicoAlvo;
    private Date dataRequisicao;
    private Date dataFinalizacaoCampanha;
    private Date dataInicioVinculacao;
    private Date dataFimVinculacao;
    private Integer nrPessoasImpactadas;
    private String resultados;
    private Double custoEstimado;
    private Double custoEfetivado;
    private Date dataAutorizacao;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>TipoMidiaCaptacao </code>.
     */
    // private TipoMidiaCaptacaoVO tipoMidiaCaptacao;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private FuncionarioVO requisitante;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private UsuarioVO responsavelAutorizacao;
    private UsuarioVO responsavelFinalizacao;
    private List campanhaMarketingMidiaVOs;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>CampanhaMarketing</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public CampanhaMarketingVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>CampanhaMarketingVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(CampanhaMarketingVO obj) throws ConsistirException {
        if (obj.getDescricao().equals("")) {
            throw new ConsistirException("O campo DESCRIÇÃO (Dados da Campanha) deve ser informado.");
        }
        // if ((obj.getTipoMidiaCaptacao() == null) ||
        // (obj.getTipoMidiaCaptacao().getCodigo().intValue() == 0)) {
        // throw new
        // ConsistirException("O campo MÍDIA CAPTAÇÃO (Campanha Marketing) deve ser informado.");
        // }
        if (obj.getSituacao().equals("")) {
            throw new ConsistirException("O campo SITUAÇÃO (Campanha Marketing) deve ser informado.");
        }
        if (obj.getObjetivo().equals("")) {
            throw new ConsistirException("O campo OBJETIVO (Dados da Campanha) deve ser informado.");
        }
        if (obj.getPublicoAlvo().equals("")) {
            throw new ConsistirException("O campo PÚBLICO ALVO (Dados da Campanha) deve ser informado.");
        }
        if (obj.getDataRequisicao() == null) {
            throw new ConsistirException("O campo DATA REQUISIÇÃO (Gestão da Campanha) deve ser informado.");
        }
        if (obj.getDataInicioVinculacao().after(obj.getDataFimVinculacao())
                || obj.getDataInicioVinculacao().compareTo(obj.getDataFimVinculacao()) == 0) {
            throw new ConsistirException("O campo DATA FIM VINCULAÇÃO (Gestão da Campanha) deve ser maior que DATA INÍCIO VINCULAÇÃO.");
        }
        if (obj.getRequisitante() == null || obj.getRequisitante().getCodigo().equals(0)) {
            throw new ConsistirException("O campo REQUISITANTE (Gestão da Campanha) deve ser informado.");
        }
        if (obj.getCustoEstimado().intValue() == 0) {
            throw new ConsistirException("O campo CUSTO ESTIMADO (Gestão da Campanha) deve ser informado.");
        }
        if ((obj.getSituacao().equals("FI")) && (obj.getCustoEfetivado().intValue() == 0)) {
            throw new ConsistirException("O campo CUSTO EFETIVADO (Gestão da Campanha) deve ser informado.");
        }
        if (obj.getSituacao().equals("FI") || obj.getSituacao().equals("EE") || obj.getSituacao().equals("PR")) {
            if ((obj.getResponsavelAutorizacao() == null)
                    || (obj.getResponsavelAutorizacao().getCodigo().intValue() == 0)) {
                throw new ConsistirException("O campo RESPONSÁVEL  (Gestão da Campanha) deve ser informado.");
            }
        }
    }

    public void adicionarObjCampanhaMarketingMidiaVOs(CampanhaMarketingMidiaVO obj) throws Exception {
        CampanhaMarketingMidiaVO.validarDados(obj);
        Iterator i = getCampanhaMarketingMidiaVOs().iterator();
        int index = 0;
        while (i.hasNext()) {
            CampanhaMarketingMidiaVO objExistente = (CampanhaMarketingMidiaVO) i.next();
            if (objExistente.getMidia().getCodigo().equals(obj.getMidia().getCodigo())) {
                getCampanhaMarketingMidiaVOs().set(index, obj);
                return;
            }
            index++;
        }
        getCampanhaMarketingMidiaVOs().add(obj);
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>UnidadeEnsinoCursoVO</code> no List
     * <code>unidadeEnsinoCursoVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>UnidadeEnsinoCurso</code> - getCurso().getCodigo() - como
     * identificador (key) do objeto no List.
     *
     * @param curso
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjCampanhaMarketingMidiaVOs(Integer midia) throws Exception {
        int index = 0;
        Iterator i = getCampanhaMarketingMidiaVOs().iterator();
        while (i.hasNext()) {
            CampanhaMarketingMidiaVO objExistente = (CampanhaMarketingMidiaVO) i.next();
            if ((objExistente.getMidia().getCodigo().equals(midia))) {
                getCampanhaMarketingMidiaVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>UnidadeEnsinoCursoVO</code> no List
     * <code>unidadeEnsinoCursoVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>UnidadeEnsinoCurso</code> - getCurso().getCodigo() - como
     * identificador (key) do objeto no List.
     *
     * @param curso
     *            Parâmetro para localizar o objeto do List.
     */
    public CampanhaMarketingMidiaVO consultarObjCampanhaMarketingMidiaVOs(Integer midia) throws Exception {
        Iterator i = getCampanhaMarketingMidiaVOs().iterator();
        while (i.hasNext()) {
            CampanhaMarketingMidiaVO objExistente = (CampanhaMarketingMidiaVO) i.next();
            if ((objExistente.getMidia().getCodigo().equals(midia))) {
                return objExistente;
            }
        }
        return null;
    }

    public List getCampanhaMarketingMidiaVOs() {
        if (campanhaMarketingMidiaVOs == null) {
            campanhaMarketingMidiaVOs = new ArrayList();
        }
        return campanhaMarketingMidiaVOs;
    }

    public void setCampanhaMarketingMidiaVOs(List campanhaMarketingMidiaVOs) {
        this.campanhaMarketingMidiaVOs = campanhaMarketingMidiaVOs;
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

    public UsuarioVO getResponsavelFinalizacao() {
        if (responsavelFinalizacao == null) {
            responsavelFinalizacao = new UsuarioVO();
        }
        return responsavelFinalizacao;
    }

    public void setResponsavelFinalizacao(UsuarioVO responsavelFinalizacao) {
        this.responsavelFinalizacao = responsavelFinalizacao;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>CampanhaMarketing</code>).
     */
    public FuncionarioVO getRequisitante() {
        if (requisitante == null) {
            requisitante = new FuncionarioVO();
        }
        return (requisitante);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>CampanhaMarketing</code>).
     */
    public void setRequisitante(FuncionarioVO obj) {
        this.requisitante = obj;
    }

    /**
     * Retorna o objeto da classe <code>TipoMidiaCaptacao</code> relacionado com
     * (<code>CampanhaMarketing</code>).
     */
    // public TipoMidiaCaptacaoVO getTipoMidiaCaptacao() {
    // return (tipoMidiaCaptacao);
    // }
    //
    // /**
    // * Define o objeto da classe <code>TipoMidiaCaptacao</code> relacionado
    // com (<code>CampanhaMarketing</code>).
    // */
    // public void setTipoMidiaCaptacao(TipoMidiaCaptacaoVO obj) {
    // this.tipoMidiaCaptacao = obj;
    // }
    public Date getDataAutorizacao() {
        return (dataAutorizacao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataAutorizacao_Apresentar() {
        return (Uteis.getData(getDataAutorizacao()));
    }

    public void setDataAutorizacao(Date dataAutorizacao) {
        this.dataAutorizacao = dataAutorizacao;
    }

    public Double getCustoEfetivado() {
        if (custoEfetivado == null) {
            custoEfetivado = 0.0;
        }
        return (custoEfetivado);
    }

    public void setCustoEfetivado(Double custoEfetivado) {
        this.custoEfetivado = custoEfetivado;
    }

    public Double getCustoEstimado() {
        if (custoEstimado == null) {
            custoEstimado = 0.0;
        }
        return (custoEstimado);
    }

    public void setCustoEstimado(Double custoEstimado) {
        this.custoEstimado = custoEstimado;
    }

    public String getResultados() {
        if (resultados == null) {
            resultados = "";
        }
        return (resultados);
    }

    public void setResultados(String resultados) {
        this.resultados = resultados;
    }

    public Integer getNrPessoasImpactadas() {
        if (nrPessoasImpactadas == null) {
            nrPessoasImpactadas = 0;
        }
        return (nrPessoasImpactadas);
    }

    public void setNrPessoasImpactadas(Integer nrPessoasImpactadas) {
        this.nrPessoasImpactadas = nrPessoasImpactadas;
    }

    public Date getDataFimVinculacao() {
        if (dataFimVinculacao == null) {
            dataFimVinculacao = new Date();
        }
        return (dataFimVinculacao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataFimVinculacao_Apresentar() {
        return (Uteis.getData(getDataFimVinculacao()));
    }

    public void setDataFimVinculacao(Date dataFimVinculacao) {
        this.dataFimVinculacao = dataFimVinculacao;
    }

    public Date getDataInicioVinculacao() {
        if (dataInicioVinculacao == null) {
            dataInicioVinculacao = new Date();
        }
        return (dataInicioVinculacao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataInicioVinculacao_Apresentar() {
        return (Uteis.getData(getDataInicioVinculacao()));
    }

    public void setDataInicioVinculacao(Date dataInicioVinculacao) {
        this.dataInicioVinculacao = dataInicioVinculacao;
    }

    public Date getDataFinalizacaoCampanha() {
        return (dataFinalizacaoCampanha);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataFinalizacaoCampanha_Apresentar() {
        return (Uteis.getData(getDataFinalizacaoCampanha()));
    }

    public void setDataFinalizacaoCampanha(Date dataFinalizacaoCampanha) {
        this.dataFinalizacaoCampanha = dataFinalizacaoCampanha;
    }

    public Date getDataRequisicao() {
        if (dataRequisicao == null) {
            dataRequisicao = new Date();
        }
        return (dataRequisicao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataRequisicao_Apresentar() {
        return (Uteis.getData(getDataRequisicao()));
    }

    public void setDataRequisicao(Date dataRequisicao) {
        this.dataRequisicao = dataRequisicao;
    }

    public String getPublicoAlvo() {
        if (publicoAlvo == null) {
            publicoAlvo = "";
        }
        return (publicoAlvo);
    }

    public void setPublicoAlvo(String publicoAlvo) {
        this.publicoAlvo = publicoAlvo;
    }

    public String getObjetivo() {
        if (objetivo == null) {
            objetivo = "";
        }
        return (objetivo);
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "AA";
        }
        return (situacao);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getSituacao_Apresentar() {
        if (getSituacao().equals("PR")) {
            return "Em Produção";
        }
        if (getSituacao().equals("FI")) {
            return "Finalizada";
        }
        if (getSituacao().equals("EE")) {
            return "Em Execução";
        }
        if (getSituacao().equals("II")) {
            return "Indeferida";
        }
        if (getSituacao().equals("AA")) {
            return "Aguardando Autorização";
        }
        return (getSituacao());
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return (descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
}
