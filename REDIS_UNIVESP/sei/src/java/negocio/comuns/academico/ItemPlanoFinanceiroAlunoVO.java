package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.ConvenioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.academico.PlanoFinanceiroAluno;

/**
 * Reponsável por manter os dados da entidade ItemPlanoFinanceiroAluno. Classe
 * do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see PlanoFinanceiroAluno
 */
public class ItemPlanoFinanceiroAlunoVO extends SuperVO {

    protected Integer codigo;
    protected Integer planoFinanceiroAluno;
    protected String tipoItemPlanoFinanceiro;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>PlanoDesconto </code>.
     */
    protected PlanoDescontoVO planoDesconto;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Convenio </code>.
     */
    protected ConvenioVO convenio;
    /**
     * Atributo transiente para ajudar.
     */
    private Boolean regerarConta;
    
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ItemPlanoFinanceiroAluno</code>. Cria
     * uma nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public ItemPlanoFinanceiroAlunoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ItemPlanoFinanceiroAlunoVO</code>. Todos os tipos de consistência
     * de dados são e devem ser implementadas neste método. São validações
     * típicas: verificação de campos obrigatórios, verificação de valores
     * válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ItemPlanoFinanceiroAlunoVO obj) throws ConsistirException {
        if (obj.getTipoItemPlanoFinanceiro().equals("")) {
            throw new ConsistirException("O campo TIPOITEMPLANOFINANCEIRO (Item Plano Financeiro Aluno) deve ser informado.");
        }
        if (obj.getTipoItemPlanoFinanceiro().equals("PD")) {
            if ((obj.getPlanoDesconto() == null) || (obj.getPlanoDesconto().getCodigo().intValue() == 0)) {
                throw new ConsistirException("O campo PLANO DE DESCONTO (Item Plano Financeiro Aluno) deve ser informado.");
            }
        }
        if (obj.getTipoItemPlanoFinanceiro().equals("CO")) {
            if ((obj.getConvenio() == null) || (obj.getConvenio().getCodigo().intValue() == 0)) {
                throw new ConsistirException("O campo CONVÊNIO (Item Plano Financeiro Aluno) deve ser informado.");
            }
        }

    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setTipoItemPlanoFinanceiro("");
    }

    public Boolean getTipoPlanoFinanceiroDescontoInstitucional() {
        if (tipoItemPlanoFinanceiro.equals("PD")) {
            return true;
        }
        return false;
    }

    public Boolean getTipoPlanoFinanceiroConvenio() {

        if (getTipoItemPlanoFinanceiro().equals("CO")) {
            return true;
        }
        return false;
    }

    /**
     * Retorna o objeto da classe <code>Convenio</code> relacionado com (
     * <code>ItemPlanoFinanceiroAluno</code>).
     */
    public ConvenioVO getConvenio() {
        if (convenio == null) {
            convenio = new ConvenioVO();
        }
        return (convenio);
    }

    /**
     * Define o objeto da classe <code>Convenio</code> relacionado com (
     * <code>ItemPlanoFinanceiroAluno</code>).
     */
    public void setConvenio(ConvenioVO obj) {
        this.convenio = obj;
    }

    /**
     * Retorna o objeto da classe <code>PlanoDesconto</code> relacionado com (
     * <code>ItemPlanoFinanceiroAluno</code>).
     */
    public PlanoDescontoVO getPlanoDesconto() {
        if (planoDesconto == null) {
            planoDesconto = new PlanoDescontoVO();
        }
        return (planoDesconto);
    }

    /**
     * Define o objeto da classe <code>PlanoDesconto</code> relacionado com (
     * <code>ItemPlanoFinanceiroAluno</code>).
     */
    public void setPlanoDesconto(PlanoDescontoVO obj) {
        this.planoDesconto = obj;
    }

    public String getTipoItemPlanoFinanceiro() {
        if (tipoItemPlanoFinanceiro == null) {
            tipoItemPlanoFinanceiro = "";
        }
        return (tipoItemPlanoFinanceiro);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoItemPlanoFinanceiro_Apresentar() {
        if (tipoItemPlanoFinanceiro.equals("PD")) {
            return "Plano de Desconto";
        }
        if (tipoItemPlanoFinanceiro.equals("CO")) {
            return "Convênio";
        }
        return (tipoItemPlanoFinanceiro);
    }

    public void setTipoItemPlanoFinanceiro(String tipoItemPlanoFinanceiro) {
        this.tipoItemPlanoFinanceiro = tipoItemPlanoFinanceiro;
    }

    public Integer getPlanoFinanceiroAluno() {
        return (planoFinanceiroAluno);
    }

    public void setPlanoFinanceiroAluno(Integer planoFinanceiroAluno) {
        this.planoFinanceiroAluno = planoFinanceiroAluno;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public void setRegerarConta(Boolean regerarConta) {
        this.regerarConta = regerarConta;
    }

    public Boolean getRegerarConta() {
        if (regerarConta == null) {
            regerarConta = Boolean.TRUE;
        }
        return regerarConta;
    }



    public String getOrdenacao(){
    	if(getTipoPlanoFinanceiroDescontoInstitucional()){
    		return getPlanoDesconto().getOrdemPrioridadeParaCalculo()+"_"+getPlanoDesconto().getCodigo();
    	}
    	return getConvenio().getOrdemPrioridadeParaCalculo()+"_"+getConvenio().getCodigo();    	
    }
}
