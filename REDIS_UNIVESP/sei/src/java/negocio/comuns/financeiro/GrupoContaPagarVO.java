package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoSacado;

/**
 * Reponsável por manter os dados da entidade ContaPagar. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class GrupoContaPagarVO extends SuperVO {

    private Integer codigo;
    private String tipoSacado;
    private Date dataVencimento;
    private Date dataEmissao;
    private Date dataFatoGerador;
    private UnidadeEnsinoVO unidadeEnsino;
    private UsuarioVO responsavel;
    private Double valor;
    private List<ContaPagarVO> contaPagarVOs;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Fornecedor </code>.
     */
    private PessoaVO pessoa;
    private ParceiroVO parceiro;
    private PessoaVO responsavelFinanceiro;
    private FornecedorVO fornecedor;
    private FuncionarioVO funcionario;
    private BancoVO banco;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ContaPagar</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public GrupoContaPagarVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ContaPagarVO</code>. Todos os tipos de consistência de dados são e
     * devem ser implementadas neste método. São validações típicas: verificação
     * de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(GrupoContaPagarVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getTipoSacado().equals("")) {
            throw new ConsistirException("O campo TIPO SACADO (Conta à Pagar) deve ser informado.");
        }
        if (obj.getTipoSacado().equals("FO")) {
            if (obj.getFornecedor() == null || obj.getFornecedor().getCodigo().intValue() == 0) {                
                throw new ConsistirException("O campo FORNECEDOR (Conta à Pagar) deve ser informado.");
            }
        } else if (obj.getTipoSacado().equals("FU")) {
            if (obj.getFuncionario() == null || obj.getFuncionario().getCodigo().intValue() == 0) {                
                throw new ConsistirException("O campo FUNCIONARIO (Conta à Pagar) deve ser informado.");
            }
        } else if (obj.getTipoSacado().equals("AL")) {
            if (obj.getPessoa() == null || obj.getPessoa().getCodigo().intValue() == 0) {                
                throw new ConsistirException("O campo ALUNO (Conta à Pagar) deve ser informado.");
            }
        } else if (obj.getTipoSacado().equals("BA")) {
            if (obj.getBanco() == null || obj.getBanco().getCodigo().intValue() == 0) {                
                throw new ConsistirException("O campo BANCO (Conta à Pagar) deve ser informado.");
            }
        } else if (obj.getTipoSacado().equals("RF")) {
            if (obj.getResponsavelFinanceiro() == null || obj.getResponsavelFinanceiro().getCodigo().intValue() == 0) {                
                throw new ConsistirException("O campo RESPONSÁVEL FINANCEIRO (Conta à Pagar) deve ser informado.");
            }
        } else if (obj.getTipoSacado().equals("PA")) {
            if (obj.getParceiro() == null || obj.getParceiro().getCodigo().intValue() == 0) {                
                throw new ConsistirException("O campo PARCEIRO (Conta à Pagar) deve ser informado.");
            }
        }
        obj.limparDadosTipoSacadoDeAcordoComTipoSacado();
        if (obj.getDataEmissao() == null) {
            throw new ConsistirException("O campo DATA (Conta à Pagar) deve ser informado.");
        }
        if (obj.getDataVencimento() == null) {
            throw new ConsistirException("O campo DATA DE VENCIMENTO (Conta à Pagar) deve ser informado.");
        }
        if (obj.getContaPagarVOs().size() == 0 || obj.getContaPagarVOs() == null) {
        	throw new ConsistirException("Deve ser informado pelo menos uma CONTA A PAGAR (Contas a Pagar)");
        }
    }
    
    public void limparDadosTipoSacadoDeAcordoComTipoSacado(){
        if (getTipoSacado().equals("FO")) {
            getBanco().setCodigo(0);
            getFuncionario().setCodigo(0);
            getPessoa().setCodigo(0);
            getResponsavelFinanceiro().setCodigo(0);
            getParceiro().setCodigo(0);
        } else if (getTipoSacado().equals("FU")) {
            getBanco().setCodigo(0);
            getFornecedor().setCodigo(0);            
            getResponsavelFinanceiro().setCodigo(0);
            getParceiro().setCodigo(0);
        } else if (getTipoSacado().equals("BA")) {
            getFornecedor().setCodigo(0);
            getFuncionario().setCodigo(0);
            getPessoa().setCodigo(0);
            getResponsavelFinanceiro().setCodigo(0);
            getParceiro().setCodigo(0);
        }else if (getTipoSacado().equals("AL")) {
            getFornecedor().setCodigo(0);
            getFuncionario().setCodigo(0);
            getBanco().setCodigo(0);
            getResponsavelFinanceiro().setCodigo(0);
            getParceiro().setCodigo(0);
        }else if (getTipoSacado().equals("RF")) {
            getFornecedor().setCodigo(0);
            getFuncionario().setCodigo(0);
            getBanco().setCodigo(0);            
            getParceiro().setCodigo(0);
        }else if (getTipoSacado().equals("PA")) {            
            getFornecedor().setCodigo(0);
            getFuncionario().setCodigo(0);
            getBanco().setCodigo(0);
            getPessoa().setCodigo(0);
            getResponsavelFinanceiro().setCodigo(0);
        }
        
    }

    public boolean isTipoSacadoFuncionario() {
        return getTipoSacado().equals(TipoSacado.FUNCIONARIO_PROFESSOR.getValor());
    }

    public boolean isTipoSacadoAluno() {
        return getTipoSacado().equals(TipoSacado.ALUNO.getValor());
    }

    public boolean isTipoSacadoFornecedor() {
        return getTipoSacado().equals(TipoSacado.FORNECEDOR.getValor());
    }

    public boolean isTipoSacadoBanco() {
        return getTipoSacado().equals(TipoSacado.BANCO.getValor());
    }
    
    public boolean isTipoSacadoParceiro() {
        return getTipoSacado().equals(TipoSacado.PARCEIRO.getValor());
    }
    
    public boolean isTipoSacadoResponsavelFinanceiro() {
        return getTipoSacado().equals(TipoSacado.RESPONSAVEL_FINANCEIRO.getValor());
    }

    public boolean isApresentarUnidadeEnsinoFornecedor() {
        return !getTipoSacado().equals(TipoSacado.FUNCIONARIO_PROFESSOR.getValor());
    }

    public String getFavorecido() {
       if(isTipoSacadoAluno()){
           return "Aluno(a) - "+ getPessoa().getNome();
       }
       if(isTipoSacadoBanco()){
           return "Banco(a) - "+getBanco().getNome();
       }
       if(isTipoSacadoFornecedor()){
           return "Fornecedor(a) - "+getFornecedor().getNome();
       }
       if(isTipoSacadoFuncionario()){
           return "Funcionário(a)/Professor(a) - "+getFuncionario().getPessoa().getNome();
       }
       if(isTipoSacadoParceiro()){
           return "Parceiro(a) - "+getParceiro().getNome();
       }
       if(isTipoSacadoResponsavelFinanceiro()){
           if(getPessoa().getCodigo().intValue() > 0){
               return "Resp. Finan. - "+getResponsavel().getNome() + "(Aluno(a) - "+getPessoa().getNome()+") ";
           }
           return "Resp. Finan. - "+getResponsavel().getNome();
       }
       
       return "";
    }

    /**
     * Retorna o objeto da classe <code>Fornecedor</code> relacionado com (
     * <code>ContaPagar</code>).
     */
    public FornecedorVO getFornecedor() {
        if (fornecedor == null) {
            fornecedor = new FornecedorVO();
        }
        return (fornecedor);
    }

    /**
     * Define o objeto da classe <code>Fornecedor</code> relacionado com (
     * <code>ContaPagar</code>).
     */
    public void setFornecedor(FornecedorVO obj) {
        this.fornecedor = obj;
    }

    public Double getValor() {
        if (valor == null) {
            valor = 0.0;
        }
        return (valor);
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getDataVencimento() {
        if (dataVencimento == null) {
            dataVencimento = new Date();
        }
        return (dataVencimento);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataVencimento_Apresentar() {
        return (Uteis.getData(dataVencimento));
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Date getDataEmissao() {
        if (dataEmissao == null) {
            dataEmissao = new Date();
        }
        return (dataEmissao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataEmissao_Apresentar() {
        return (Uteis.getData(dataEmissao));
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
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

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public Boolean getHabilitarComboBoxUnidadeEnsino(Integer unidadeEnsino) {
        if (unidadeEnsino == 0) {
            return false;
        }
        return true;
    }

    public FuncionarioVO getFuncionario() {
        if (funcionario == null) {
            funcionario = new FuncionarioVO();
        }
        return funcionario;
    }

    public void setFuncionario(FuncionarioVO funcionario) {
        this.funcionario = funcionario;
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

    public String getTipoSacado() {
        if (tipoSacado == null) {
            tipoSacado = TipoSacado.FORNECEDOR.getValor();
        }
        return tipoSacado;
    }

    public void setTipoSacado(String tipoSacado) {
        this.tipoSacado = tipoSacado;
    }

    /**
     * @return the banco
     */
    public BancoVO getBanco() {
        if (banco == null) {
            banco = new BancoVO();
        }
        return banco;
    }

    /**
     * @param banco
     *            the banco to set
     */
    public void setBanco(BancoVO banco) {
        this.banco = banco;
    }

    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return responsavel;
    }

    public void setResponsavel(UsuarioVO responsavel) {
        this.responsavel = responsavel;
    }

    public Date getDataFatoGerador() {
        if (dataFatoGerador == null) {
            dataFatoGerador = new Date();
        }
        return dataFatoGerador;
    }

    public void setDataFatoGerador(Date dataFatoGerador) {
        this.dataFatoGerador = dataFatoGerador;
    }

    public String getDataFatoGerador_Apresentar() {
        return (Uteis.getData(dataFatoGerador));
    }

    public List<ContaPagarVO> getContaPagarVOs() {
        if (contaPagarVOs == null) {
            contaPagarVOs = new ArrayList(0);
        }
        return contaPagarVOs;
    }

    public void setContaPagarVOs(List<ContaPagarVO> contaPagarVOs) {
        this.contaPagarVOs = contaPagarVOs;
    }

    
    public ParceiroVO getParceiro() {
        if(parceiro == null){
            parceiro = new ParceiroVO();
        }
        return parceiro;
    }

    
    public void setParceiro(ParceiroVO parceiro) {
        this.parceiro = parceiro;
    }

    
    public PessoaVO getResponsavelFinanceiro() {
        if(responsavelFinanceiro == null){
            responsavelFinanceiro = new PessoaVO();
        }
        return responsavelFinanceiro;
    }

    
    public void setResponsavelFinanceiro(PessoaVO responsavelFinanceiro) {
        this.responsavelFinanceiro = responsavelFinanceiro;
    }
    
    
}
