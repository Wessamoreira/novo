package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
/**
 * Reponsável por manter os dados da entidade NegociacaoPagamento. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.comuns.utilitarias.dominios.TipoSacado;

public class NegociacaoPagamentoVO extends SuperVO {

    private Integer codigo;
    private Date data;
    private Date dataRegistro;
    private Double valorTotal;
    private Double valorTotalPagamento;
    private Double valorTroco;
    private Double valorTrocoEdicao;
   
    
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>ContaPagarNegociacaoPagamento</code>.
     */
    private List<ContaPagarNegociacaoPagamentoVO> contaPagarNegociacaoPagamentoVOs;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>Pagamento</code>.
     */
    private List<FormaPagamentoNegociacaoPagamentoVO> formaPagamentoNegociacaoPagamentoVOs;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Usuario </code>.
     */
    private UsuarioVO responsavel;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>ContaCorrente </code>.
     */
    private ContaCorrenteVO caixa;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Fornecedor </code>.
     */
    private PessoaVO aluno;
    private PessoaVO responsavelFinanceiro;
    private FornecedorVO fornecedor;
    private FuncionarioVO funcionario;
    private DepartamentoVO departamento;
    private BancoVO banco;
    private ParceiroVO parceiro;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>NPtRLog </code>.
     */
    private UnidadeEnsinoVO unidadeEnsino;
    private List<ChequeVO> chequeVOs;
    private Boolean alterouConteudo;
    private String motivoAlteracao;
    private String tipoSacado;
    private FormaPagamentoVO formaPagamentoTrocoVO;
    private ContaCorrenteVO contaCorrenteTrocoVO;
    private Date dataEstorno;
    private boolean desconsiderarConciliacaoBancaria = false;
    public static final long serialVersionUID = 1L;
    
    private OperadoraCartaoVO operadoraCartao;

    /**
     * Construtor padrão da classe <code>NegociacaoPagamento</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public NegociacaoPagamentoVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>NegociacaoPagamentoVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(NegociacaoPagamentoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Negociação Pagamento) deve ser informado.");
        }
        if ((obj.getResponsavel() == null) || (obj.getResponsavel().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo RESPONSÁVEL (Negociação Pagamento) deve ser informado.");
        }
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo UNIDADE ENSINO (Negociação Pagamento) deve ser informado.");
        }

        // if ((obj.getCaixa() == null) ||
        // (obj.getCaixa().getCodigo().intValue() == 0)) {
        // throw new
        // ConsistirException("O campo CAIXA (Negociação Pagamento) deve ser informado.");
        // }
        if (obj.getTipoSacado() == null || obj.getTipoSacado().equals("")) {
            throw new ConsistirException("O campo PAGAMENTO PARA (Negociação Pagamento) deve ser informado.");
        } else if (obj.getTipoSacado().equals("FO")) {
            if ((obj.getFornecedor() == null) || (obj.getFornecedor().getCodigo().intValue() == 0)) {
                throw new ConsistirException("O campo FORNECEDOR (Negociação Pagamento) deve ser informado.");
            }                       
        } else if (obj.getTipoSacado().equals("FU")) {
            if ((obj.getFuncionario() == null) || (obj.getFuncionario().getCodigo().intValue() == 0)) {
                throw new ConsistirException("O campo FUNCIONARIO (Negociação Pagamento) deve ser informado.");
            }
            
        } else if (obj.getTipoSacado().equals("BA")) {
            if ((obj.getBanco() == null) || (obj.getBanco().getCodigo().intValue() == 0)) {
                throw new ConsistirException("O campo BANCO (Negociação Pagamento) deve ser informado.");
            }
            
        }else if (obj.getTipoSacado().equals("AL")) {
            if ((obj.getAluno() == null) || (obj.getAluno().getCodigo().intValue() == 0)) {
                throw new ConsistirException("O campo ALUNO (Negociação Pagamento) deve ser informado.");
            }
            
        }else if (obj.getTipoSacado().equals("RF")) {
            if ((obj.getResponsavelFinanceiro() == null) || (obj.getResponsavelFinanceiro().getCodigo().intValue() == 0)) {
                throw new ConsistirException("O campo RESPONSÁVEL FINANCEIRO (Negociação Pagamento) deve ser informado.");
            }            
        }else if (obj.getTipoSacado().equals("PA")) {
            if ((obj.getParceiro() == null) || (obj.getParceiro().getCodigo().intValue() == 0)) {
                throw new ConsistirException("O campo PARCEIRO (Negociação Pagamento) deve ser informado.");
            }            
        }else if (obj.getTipoSacado().equals(TipoSacado.OPERADORA_CARTAO.getValor())) {
        	if ((obj.getOperadoraCartao() == null) || (obj.getOperadoraCartao().getCodigo().intValue() == 0)) {
        		throw new ConsistirException("O campo OPERADORA CARTÃO (Negociação Pagamento) deve ser informado.");
        	}            
        }
        if (obj.getContaPagarNegociacaoPagamentoVOs().isEmpty()) {
            throw new ConsistirException("Deve ser informado pelo menos uma CONTA A PAGAR (Negociação Pagamento).");
        }
        obj.limparDadosTipoSacadoDeAcordoComTipoSacado();
        if (!obj.getNegociacaoPagamentoComValorAPagarZerado()) {
            if (obj.getValorTotal().doubleValue() > obj.getValorTotalPagamento().doubleValue()) {
                throw new ConsistirException("Não é possível fazer um pagamento parcial (Negociação Pagamento).");
            }
            if (obj.getFormaPagamentoNegociacaoPagamentoVOs().isEmpty()) {
                throw new ConsistirException("Deve ser informado pelo menos um PAGAMENTO (Negociação Pagamento).");
            }
        }
    }
    
    public void limparDadosTipoSacadoDeAcordoComTipoSacado(){
        if (getTipoSacado().equals("FO")) {
            getBanco().setCodigo(0);
            getFuncionario().setCodigo(0);
            getAluno().setCodigo(0);
            getResponsavelFinanceiro().setCodigo(0);
            getParceiro().setCodigo(0);
            getOperadoraCartao().setCodigo(0);
        } else if (getTipoSacado().equals("FU")) {
            getBanco().setCodigo(0);
            getFornecedor().setCodigo(0);
            getAluno().setCodigo(0);
            getResponsavelFinanceiro().setCodigo(0);
            getParceiro().setCodigo(0);
            getOperadoraCartao().setCodigo(0);
        } else if (getTipoSacado().equals("BA")) {
            getFornecedor().setCodigo(0);
            getFuncionario().setCodigo(0);
            getAluno().setCodigo(0);
            getResponsavelFinanceiro().setCodigo(0);
            getParceiro().setCodigo(0);
            getOperadoraCartao().setCodigo(0);
        }else if (getTipoSacado().equals("AL")) {
            getFornecedor().setCodigo(0);
            getFuncionario().setCodigo(0);
            getBanco().setCodigo(0);
            getResponsavelFinanceiro().setCodigo(0);
            getParceiro().setCodigo(0);
            getOperadoraCartao().setCodigo(0);
        }else if (getTipoSacado().equals("RF")) {
            getFornecedor().setCodigo(0);
            getFuncionario().setCodigo(0);
            getBanco().setCodigo(0);
            getAluno().setCodigo(0);
            getParceiro().setCodigo(0);
            getOperadoraCartao().setCodigo(0);
        }else if (getTipoSacado().equals("PA")) {            
            getFornecedor().setCodigo(0);
            getFuncionario().setCodigo(0);
            getBanco().setCodigo(0);
            getAluno().setCodigo(0);
            getResponsavelFinanceiro().setCodigo(0);
            getOperadoraCartao().setCodigo(0);
        }else if (getTipoSacado().equals(TipoSacado.OPERADORA_CARTAO.getValor())) {            
        	getFornecedor().setCodigo(0);
        	getFuncionario().setCodigo(0);
        	getBanco().setCodigo(0);
        	getAluno().setCodigo(0);
        	getParceiro().setCodigo(0);
        	getResponsavelFinanceiro().setCodigo(0);
        }
        
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
        return;
    }

    

    public void calcularTotal() {
        setValorTotal(0.0);
        Iterator i = getContaPagarNegociacaoPagamentoVOs().iterator();
        while (i.hasNext()) {
            ContaPagarNegociacaoPagamentoVO obj = (ContaPagarNegociacaoPagamentoVO) i.next();
            obj.setValorContaPagar(obj.getValorPrevisaoPagamento());
            setValorTotal(getValorTotal() + obj.getValorContaPagar());
            setAlterouConteudo(true);
        }
        setValorTotal(Uteis.arrendondarForcando2CadasDecimais(valorTotal));

    }

    public void calcularTotalPago() {
        setValorTotalPagamento(0.0);
        Iterator i = getFormaPagamentoNegociacaoPagamentoVOs().iterator();
        while (i.hasNext()) {
            FormaPagamentoNegociacaoPagamentoVO obj = (FormaPagamentoNegociacaoPagamentoVO) i.next();
            setValorTotalPagamento(getValorTotalPagamento() + obj.getValor());
        }
        setAlterouConteudo(true);
        setValorTotalPagamento(Uteis.arrendondarForcando2CadasDecimais(valorTotalPagamento));

    }

    public Boolean getExisteUnidadeEnsino() {
        if (getUnidadeEnsino().getCodigo().intValue() == 0) {
            return false;
        }
        return true;
    }

    public Boolean getApresentarCamposFornecedor() {
        try {
            return getTipoSacado().equals("FO");
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean getApresentarCamposFuncionario() {
        try {
            return getTipoSacado().equals("FU");
        } catch (Exception e) {
            return false;
        }
    }
    
    public Boolean getApresentarCamposResponsavelFinanceiro() {
        try {
            return getTipoSacado().equals("RF");
        } catch (Exception e) {
            return false;
        }
    }
    
    public Boolean getApresentarCamposAluno() {
        try {
            return getTipoSacado().equals("AL");
        } catch (Exception e) {
            return false;
        }
    }
    
    public Boolean getApresentarCamposParceiro() {
        try {
            return getTipoSacado().equals("PA");
        } catch (Exception e) {
            return false;
        }
    }
    
    public Boolean getApresentarCamposOperadoraCartao() {
        try {
            return getTipoSacado().equals(TipoSacado.OPERADORA_CARTAO.getValor());
        } catch (Exception e) {
            return false;
        }
    }


    public void atualizarChequePagamento() {
        setChequeVOs(new ArrayList());
        Iterator i = getFormaPagamentoNegociacaoPagamentoVOs().iterator();
        while (i.hasNext()) {
            FormaPagamentoNegociacaoPagamentoVO pagamentos = (FormaPagamentoNegociacaoPagamentoVO) i.next();
            if (pagamentos.getCheque().getCodigo().intValue() != 0) {
                getChequeVOs().add(pagamentos.getCheque());
            }
        }
    }

    public void zerarValoresPagoContaPagar() {
        for (ContaPagarNegociacaoPagamentoVO obj : getContaPagarNegociacaoPagamentoVOs()) {
            obj.getContaPagar().setSituacao("AP");
            obj.getContaPagar().setValorPagamento(0.0);
            obj.getContaPagar().setValorPago(0.0);
            obj.getContaPagar().setContaPagarPagamentoVOs(new ArrayList<ContaPagarPagamentoVO>(0));
        }
    }

    public Integer getCodigoFornecedor() {
        if (getContaPagarNegociacaoPagamentoVOs().isEmpty()) {
            return 0;
        }
        ContaPagarNegociacaoPagamentoVO obj = (ContaPagarNegociacaoPagamentoVO) getContaPagarNegociacaoPagamentoVOs().get(0);
        return obj.getContaPagar().getFornecedor().getCodigo();
    }

    public Boolean getExisteContaPagar() {
        if (!getContaPagarNegociacaoPagamentoVOs().isEmpty()) {
            return true;
        }
        return false;
    }

    public Boolean getEdicao() {
        if (getCodigo().intValue() != 0) {
            return false;
        }
        return true;
    }

    public Boolean getApresentarModalMotivo() {
        if (!getEdicao() && getAlterouConteudo() && getMotivoAlteracao().equals("")) {
            return true;
        }
        return false;
    }

    public Boolean getApresentarCamposTroco() {
        return getValorTroco().doubleValue() > 0;
    }

    public Double getResiduo() {
        if (getValorTotal() - getValorTotalPagamento() >= 0) {
            return Uteis.arrendondarForcando2CadasDecimais(getValorTotal() - getValorTotalPagamento());
        }
        return 0.0;
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>PagamentoVO</code> ao List <code>pagamentoVOs</code>. Utiliza o
     * atributo padrão de consulta da classe <code>Pagamento</code> - getValor()
     * - como identificador (key) do objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>PagamentoVO</code> que será adiocionado
     *            ao Hashtable correspondente.
     */
    public void adicionarObjFormaPagamentoNegociacaoPagamentoVOs(FormaPagamentoNegociacaoPagamentoVO obj) throws Exception {

        FormaPagamentoNegociacaoPagamentoVO.validarDados(obj);
        obj.setNegociacaoContaPagar(getCodigo());
        int index = 0;
        Iterator i = getFormaPagamentoNegociacaoPagamentoVOs().iterator();
        while (i.hasNext()) {

            FormaPagamentoNegociacaoPagamentoVO objExistente = (FormaPagamentoNegociacaoPagamentoVO) i.next();
            // if (obj.getFormaPagamento().isDinheiro() &&
            // objExistente.getFormaPagamento().isDinheiro()) {
            // getFormaPagamentoNegociacaoPagamentoVOs().set(index, obj);
            // setAlterouConteudo(true);
            // calcularTotalPago();
            // return;
            // }
            if (((obj.getFormaPagamento().isDinheiro() && objExistente.getFormaPagamento().isDinheiro())
                    || (obj.getFormaPagamento().isDebitoEmConta() && objExistente.getFormaPagamento().isDebitoEmConta())
                    || (obj.getFormaPagamento().isPermuta() && objExistente.getFormaPagamento().isPermuta())
                    || (obj.getFormaPagamento().isBoletoBancario() && objExistente.getFormaPagamento().isBoletoBancario()) || (obj.getFormaPagamento().isCartaoCredito() && objExistente.getFormaPagamento().isCartaoCredito()))
                    && (obj.getContaCorrente().getCodigo().intValue() == objExistente.getContaCorrente().getCodigo().intValue())) {
                getFormaPagamentoNegociacaoPagamentoVOs().set(index, obj);
                setAlterouConteudo(true);
                calcularTotalPago();
                return;
            }
            if ((obj.getFormaPagamento().isCheque() && objExistente.getFormaPagamento().isCheque())
                    && obj.getCheque().getNumero().equals(objExistente.getCheque().getNumero())
                    && obj.getCheque().getBanco().equals(objExistente.getCheque().getBanco())) {
                throw new Exception("O cheque com o número " + obj.getCheque().getNumero() + " já foi adicionado.");
            }
            index++;
        }
        if (obj.getFormaPagamento().isCheque()) {
            getChequeVOs().add(obj.getCheque());
        }
        
        if (obj.getFormaPagamento().getTipo().equals("IS")) {
        	obj.setValor(0.0);
        }
        
        setAlterouConteudo(true);
        getFormaPagamentoNegociacaoPagamentoVOs().add(obj);
        calcularTotalPago();
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>PagamentoVO</code> no List <code>pagamentoVOs</code>. Utiliza o
     * atributo padrão de consulta da classe <code>Pagamento</code> - getValor()
     * - como identificador (key) do objeto no List.
     *
     * @param valor
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjFormaPagamentoNegociacaoPagamentoVOs(FormaPagamentoNegociacaoPagamentoVO obj) throws Exception {
        int index = 0;
        Iterator i = getFormaPagamentoNegociacaoPagamentoVOs().iterator();
        while (i.hasNext()) {
            FormaPagamentoNegociacaoPagamentoVO objExistente = (FormaPagamentoNegociacaoPagamentoVO) i.next();
            if (obj.getFormaPagamento().isDinheiro() && objExistente.getFormaPagamento().isDinheiro()) {
                setValorTotalPagamento(Uteis.arrendondarForcando2CadasDecimais(getValorTotalPagamento()
                        - objExistente.getValor()));
                getFormaPagamentoNegociacaoPagamentoVOs().remove(index);
                setAlterouConteudo(true);
                return;
            }
            if ((obj.getFormaPagamento().isBoletoBancario() && objExistente.getFormaPagamento().isBoletoBancario())
                    || (obj.getFormaPagamento().isDebitoEmConta() && objExistente.getFormaPagamento().isDebitoEmConta())
                    || (obj.getFormaPagamento().isCartaoCredito() && objExistente.getFormaPagamento().isCartaoCredito())
                    || (obj.getFormaPagamento().isCartaoDebito() && objExistente.getFormaPagamento().isCartaoDebito())
                    || (obj.getFormaPagamento().getTipo().equals("IS") && objExistente.getFormaPagamento().getTipo().equals("IS"))
                    && obj.getContaCorrente().getCodigo().intValue() == objExistente.getContaCorrente().getCodigo().intValue()) {
                setValorTotalPagamento(Uteis.arrendondarForcando2CadasDecimais(getValorTotalPagamento()
                        - objExistente.getValor()));
                getFormaPagamentoNegociacaoPagamentoVOs().remove(index);
                setAlterouConteudo(true);
                return;
            }
            if ((obj.getFormaPagamento().isCheque() && objExistente.getFormaPagamento().isCheque())
                    && obj.getCheque().getNumero().equals(objExistente.getCheque().getNumero())
                    && obj.getCheque().getBanco().equals(objExistente.getCheque().getBanco())) {
                setValorTotalPagamento(Uteis.arrendondarForcando2CadasDecimais(getValorTotalPagamento()
                        - objExistente.getValor()));
                getFormaPagamentoNegociacaoPagamentoVOs().remove(index);
                removerChequeNegociacao(obj.getCheque());
                setAlterouConteudo(true);
                return;
            }
            index++;
        }
    }

    private void removerChequeNegociacao(ChequeVO obj) {
        int index = 0;
        for (ChequeVO objExistente : getChequeVOs()) {
            if (obj.getNumero().equals(objExistente.getNumero()) && obj.getBanco().equals(objExistente.getBanco())) {
                getChequeVOs().remove(index);
                setAlterouConteudo(true);
                return;
            }
            index++;
        }
    }

    public void removerFormaPagamentoCheque(ChequeVO obj) throws Exception {
        int index = 0;
        for (FormaPagamentoNegociacaoPagamentoVO fp : getFormaPagamentoNegociacaoPagamentoVOs()) {
            if (fp.getFormaPagamento().getTipo().equals("CH") && obj.getNumero().equals(fp.getCheque().getNumero())
                    && obj.getBanco().equals(fp.getCheque().getBanco())) {
                setValorTotalPagamento(Uteis.arrendondarForcando2CadasDecimais(getValorTotalPagamento() - fp.getValor()));
                getFormaPagamentoNegociacaoPagamentoVOs().remove(index);
                removerChequeNegociacao(obj);
                setAlterouConteudo(true);
                return;
            }
            index++;
        }

    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>ContaPagarNegociacaoPagamentoVO</code> ao List
     * <code>contaPagarNegociacaoPagamentoVOs</code>. Utiliza o atributo padrão
     * de consulta da classe <code>ContaPagarNegociacaoPagamento</code> -
     * getContaPagar().getCodigo() - como identificador (key) do objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>ContaPagarNegociacaoPagamentoVO</code>
     *            que será adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjContaPagarNegociacaoPagamentoVOs(ContaPagarNegociacaoPagamentoVO obj) throws Exception {
        ContaPagarNegociacaoPagamentoVO.validarDados(obj);

        if (getTipoSacado().equals("FO") && getFornecedor().getCodigo().intValue() != 0
                && !getFornecedor().getCodigo().equals(obj.getContaPagar().getFornecedor().getCodigo())) {
            throw new Exception("Não é possivel adicionar conta a pagar de fornecedores diferentes.");
        } else if (getTipoSacado().equals("FU") && getFuncionario().getCodigo().intValue() != 0
                && !getFuncionario().getCodigo().equals(obj.getContaPagar().getFuncionario().getCodigo())) {
            throw new Exception("Não é possivel adicionar conta a pagar de funcionarios diferentes.");
        } else if (getTipoSacado().equals("BA") && getBanco().getCodigo().intValue() != 0
                && !getBanco().getCodigo().equals(obj.getContaPagar().getBanco().getCodigo())) {
            throw new Exception("Não é possivel adicionar conta a pagar de bancos diferentes.");
        } else if (getTipoSacado().equals("AL") && getAluno().getCodigo().intValue() != 0
                && !getAluno().getCodigo().equals(obj.getContaPagar().getPessoa().getCodigo())) {
            throw new Exception("Não é possivel adicionar conta a pagar de alunos diferentes.");
        } else if (getTipoSacado().equals("RF") && getResponsavelFinanceiro().getCodigo().intValue() != 0                
                && !getResponsavelFinanceiro().getCodigo().equals(obj.getContaPagar().getResponsavelFinanceiro().getCodigo())) {
            throw new Exception("Não é possivel adicionar conta a pagar de responsáveis financeiros diferentes.");
        }else if (getTipoSacado().equals("PA") && getParceiro().getCodigo().intValue() != 0                
                && !getParceiro().getCodigo().equals(obj.getContaPagar().getParceiro().getCodigo())) {
            throw new Exception("Não é possivel adicionar conta a pagar de parceiro diferentes.");
        }else if (getTipoSacado().equals(TipoSacado.OPERADORA_CARTAO.getValor()) && getOperadoraCartao().getCodigo().intValue() != 0                
        		&& !getOperadoraCartao().getCodigo().equals(obj.getContaPagar().getOperadoraCartao().getCodigo())) {
        	throw new Exception("Não é possivel adicionar conta a pagar de operadora de cartão diferentes.");
        }
        if(getTipoSacado().equals("FO")){
            getFornecedor().setCodigo(obj.getContaPagar().getFornecedor().getCodigo());
            getFornecedor().setNome(obj.getContaPagar().getFornecedor().getNome());
        }
        if(getTipoSacado().equals("FU")){
            getFuncionario().setCodigo(obj.getContaPagar().getFuncionario().getCodigo());
            getFuncionario().getPessoa().setNome(obj.getContaPagar().getFuncionario().getPessoa().getNome());
        }
        if(getTipoSacado().equals("BA")){
            getBanco().setCodigo(obj.getContaPagar().getBanco().getCodigo());
            getBanco().setNome(obj.getContaPagar().getBanco().getNome());
        }
        if(getTipoSacado().equals("RF")){
            getResponsavelFinanceiro().setCodigo(obj.getContaPagar().getResponsavelFinanceiro().getCodigo());
            getResponsavelFinanceiro().setNome(obj.getContaPagar().getResponsavelFinanceiro().getNome());
        }
        if(getTipoSacado().equals("AL")){
            getAluno().setCodigo(obj.getContaPagar().getPessoa().getCodigo());
            getAluno().setNome(obj.getContaPagar().getPessoa().getNome());
        }
        if(getTipoSacado().equals("PA")){
            getParceiro().setCodigo(obj.getContaPagar().getParceiro().getCodigo());
            getParceiro().setNome(obj.getContaPagar().getParceiro().getNome());
        }
        if(getTipoSacado().equals(TipoSacado.OPERADORA_CARTAO.getValor())){
        	getOperadoraCartao().setCodigo(obj.getContaPagar().getOperadoraCartao().getCodigo());
        	getOperadoraCartao().setNome(obj.getContaPagar().getOperadoraCartao().getNome());
        }
        limparDadosTipoSacadoDeAcordoComTipoSacado();
        int index = 0;
        Iterator i = getContaPagarNegociacaoPagamentoVOs().iterator();
        while (i.hasNext()) {
            ContaPagarNegociacaoPagamentoVO objExistente = (ContaPagarNegociacaoPagamentoVO) i.next();
            if (objExistente.getContaPagar().getCodigo().equals(obj.getContaPagar().getCodigo())) {
                setAlterouConteudo(Boolean.TRUE);
                // getContaPagarNegociacaoPagamentoVOs().set( index , obj );
                return;
            }
            index++;
        }
        obj.setValorContaPagar(obj.getContaPagar().getValorPrevisaoPagamento() - obj.getContaPagar().getValorPago());
        setValorTotal(getValorTotal() + obj.getValorContaPagar());
        setAlterouConteudo(Boolean.TRUE);
        getContaPagarNegociacaoPagamentoVOs().add(obj);
        // adicionarObjSubordinadoOC
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>ContaPagarNegociacaoPagamentoVO</code> no List
     * <code>contaPagarNegociacaoPagamentoVOs</code>. Utiliza o atributo padrão
     * de consulta da classe <code>ContaPagarNegociacaoPagamento</code> -
     * getContaPagar().getCodigo() - como identificador (key) do objeto no List.
     *
     * @param contaPagar
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjContaPagarNegociacaoPagamentoVOs(Integer contaPagar) throws Exception {
        int index = 0;
        Iterator i = getContaPagarNegociacaoPagamentoVOs().iterator();
        while (i.hasNext()) {
            ContaPagarNegociacaoPagamentoVO objExistente = (ContaPagarNegociacaoPagamentoVO) i.next();
            if (objExistente.getContaPagar().getCodigo().equals(contaPagar)) {
                setValorTotal(getValorTotal() - objExistente.getValorContaPagar());
                getContaPagarNegociacaoPagamentoVOs().remove(index);
                setAlterouConteudo(Boolean.TRUE);
                return;
            }
            index++;
        }
        // excluirObjSubordinadoOC
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>ContaPagarNegociacaoPagamentoVO</code> no List
     * <code>contaPagarNegociacaoPagamentoVOs</code>. Utiliza o atributo padrão
     * de consulta da classe <code>ContaPagarNegociacaoPagamento</code> -
     * getContaPagar().getCodigo() - como identificador (key) do objeto no List.
     *
     * @param contaPagar
     *            Parâmetro para localizar o objeto do List.
     */
    public ContaPagarNegociacaoPagamentoVO consultarObjContaPagarNegociacaoPagamentoVO(Integer contaPagar) throws Exception {
        Iterator i = getContaPagarNegociacaoPagamentoVOs().iterator();
        while (i.hasNext()) {
            ContaPagarNegociacaoPagamentoVO objExistente = (ContaPagarNegociacaoPagamentoVO) i.next();
            if (objExistente.getContaPagar().getCodigo().equals(contaPagar)) {
                return objExistente;
            }
        }
        return null;
        // consultarObjSubordinadoOC
    }

    public String getNomeFavorecido() {
        if(getTipoSacado().equals(TipoSacado.FORNECEDOR.getValor())){
            return TipoSacado.FORNECEDOR.getDescricao() + " - " + getFornecedor().getNome();
        }
        if(getTipoSacado().equals(TipoSacado.BANCO.getValor())){
            return TipoSacado.BANCO.getDescricao() + " - " + getBanco().getNome();
        }
        if(getTipoSacado().equals(TipoSacado.ALUNO.getValor())){
            return TipoSacado.ALUNO.getDescricao() + " - " + getAluno().getNome();
        }
        if(getTipoSacado().equals(TipoSacado.FUNCIONARIO_PROFESSOR.getValor())){
            return TipoSacado.FUNCIONARIO_PROFESSOR.getDescricao() + " - " + getFuncionario().getPessoa().getNome();
        }
        if(getTipoSacado().equals(TipoSacado.PARCEIRO.getValor())){
            return TipoSacado.PARCEIRO.getDescricao() + " - " + getParceiro().getNome();
        }
        if(getTipoSacado().equals(TipoSacado.OPERADORA_CARTAO.getValor())){
        	return TipoSacado.OPERADORA_CARTAO.getDescricao() + " - " + getOperadoraCartao().getNome();
        }
        if(getTipoSacado().equals(TipoSacado.RESPONSAVEL_FINANCEIRO.getValor())){
            return TipoSacado.RESPONSAVEL_FINANCEIRO.getDescricao() + " - " + getResponsavelFinanceiro().getNome();
        }
        return "";
    }

    /**
     * Retorna o objeto da classe <code>Fornecedor</code> relacionado com (
     * <code>NegociacaoPagamento</code>).
     */
    public FornecedorVO getFornecedor() {
        if (fornecedor == null) {
            fornecedor = new FornecedorVO();
        }
        return (fornecedor);
    }

    /**
     * Define o objeto da classe <code>Fornecedor</code> relacionado com (
     * <code>NegociacaoPagamento</code>).
     */
    public void setFornecedor(FornecedorVO obj) {
        this.fornecedor = obj;
    }

    /**
     * Retorna o objeto da classe <code>ContaCorrente</code> relacionado com (
     * <code>NegociacaoPagamento</code>).
     */
    public ContaCorrenteVO getCaixa() {
        if (caixa == null) {
            caixa = new ContaCorrenteVO();
        }
        return (caixa);
    }

    /**
     * Define o objeto da classe <code>ContaCorrente</code> relacionado com (
     * <code>NegociacaoPagamento</code>).
     */
    public void setCaixa(ContaCorrenteVO obj) {
        this.caixa = obj;
    }

    /**
     * Retorna o objeto da classe <code>Usuario</code> relacionado com (
     * <code>NegociacaoPagamento</code>).
     */
    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return (responsavel);
    }

    /**
     * Define o objeto da classe <code>Usuario</code> relacionado com (
     * <code>NegociacaoPagamento</code>).
     */
    public void setResponsavel(UsuarioVO obj) {
        this.responsavel = obj;
    }

    public List<FormaPagamentoNegociacaoPagamentoVO> getFormaPagamentoNegociacaoPagamentoVOs() {
        if (formaPagamentoNegociacaoPagamentoVOs == null) {
            formaPagamentoNegociacaoPagamentoVOs = new ArrayList<FormaPagamentoNegociacaoPagamentoVO>(0);
        }
        return formaPagamentoNegociacaoPagamentoVOs;
    }

    public void setFormaPagamentoNegociacaoPagamentoVOs(List<FormaPagamentoNegociacaoPagamentoVO> formaPagamentoNegociacaoPagamentoVOs) {
        this.formaPagamentoNegociacaoPagamentoVOs = formaPagamentoNegociacaoPagamentoVOs;
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>ContaPagarNegociacaoPagamento</code>.
     */
    public List<ContaPagarNegociacaoPagamentoVO> getContaPagarNegociacaoPagamentoVOs() {
        if (contaPagarNegociacaoPagamentoVOs == null) {
            contaPagarNegociacaoPagamentoVOs = new ArrayList<ContaPagarNegociacaoPagamentoVO>(0);
        }
        return (contaPagarNegociacaoPagamentoVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>ContaPagarNegociacaoPagamento</code>.
     */
    public void setContaPagarNegociacaoPagamentoVOs(List<ContaPagarNegociacaoPagamentoVO> contaPagarNegociacaoPagamentoVOs) {
        this.contaPagarNegociacaoPagamentoVOs = contaPagarNegociacaoPagamentoVOs;
    }

    public Double getValorTroco() {
        setValorTroco(0.0);
        if (getValorTotal().doubleValue() < getValorTotalPagamento().doubleValue()) {
            setValorTroco(Uteis.arrendondarForcando2CadasDecimais(getValorTotalPagamento().doubleValue()
                    - getValorTotal().doubleValue()));
        }
        return (valorTroco);
    }

    public void setValorTroco(Double valorTroco) {
        this.valorTroco = valorTroco;
    }

    public Double getValorTotal() {
        if (valorTotal == null) {
            valorTotal = 0.0;
        }
        valorTotal = Uteis.arrendondarForcando2CadasDecimais(valorTotal);
        return (valorTotal);
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Date getData() {
        if (data == null) {
            data = new Date();
        }
        return (data);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getData_Apresentar() {
        return (Uteis.getData(getData()));
    }

    public void setData(Date data) {
        this.data = data;
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

    public Boolean getAlterouConteudo() {
        if (alterouConteudo == null) {
            alterouConteudo = Boolean.FALSE;
        }
        return alterouConteudo;
    }

    public void setAlterouConteudo(Boolean alterouConteudo) {
        this.alterouConteudo = alterouConteudo;
    }

    public String getMotivoAlteracao() {
        if (motivoAlteracao == null) {
            motivoAlteracao = "";
        }
        return motivoAlteracao;
    }

    public void setMotivoAlteracao(String motivoAlteracao) {
        this.motivoAlteracao = motivoAlteracao;
    }

    public Double getValorTotalPagamento() {
        if (valorTotalPagamento == null) {
            valorTotalPagamento = 0.0;
        }
        setValorTotalPagamento(Uteis.arrendondarForcando2CadasDecimais(valorTotalPagamento));
        return valorTotalPagamento;
    }

    public void setValorTotalPagamento(Double valorTotalPagamento) {
        this.valorTotalPagamento = valorTotalPagamento;
    }

    public Double getValorTrocoEdicao() {
        if (valorTrocoEdicao == null) {
            valorTrocoEdicao = 0.0;
        }
        return valorTrocoEdicao;
    }

    public void setValorTrocoEdicao(Double valorTrocoEdicao) {
        this.valorTrocoEdicao = valorTrocoEdicao;
    }

    public List<ChequeVO> getChequeVOs() {
        if (chequeVOs == null) {
            chequeVOs = new ArrayList<ChequeVO>(0);
        }
        return chequeVOs;
    }

    public void setChequeVOs(List<ChequeVO> chequeVOs) {
        this.chequeVOs = chequeVOs;
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

    public String getTipoSacado() {
        if (tipoSacado == null) {
            tipoSacado = "FO";
        }
        return tipoSacado;
    }

    public void setTipoSacado(String tipoSacado) {
        this.tipoSacado = tipoSacado;
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

    /**
     * @return the departamento
     */
    public DepartamentoVO getDepartamento() {
        if (departamento == null) {
            departamento = new DepartamentoVO();
        }
        return departamento;
    }

    /**
     * @param departamento
     *            the departamento to set
     */
    public void setDepartamento(DepartamentoVO departamento) {
        this.departamento = departamento;
    }

    /**
     * @return the formaPagamentoTrocoVO
     */
    public FormaPagamentoVO getFormaPagamentoTrocoVO() {
        if (formaPagamentoTrocoVO == null) {
            formaPagamentoTrocoVO = new FormaPagamentoVO();
        }
        return formaPagamentoTrocoVO;
    }

    /**
     * @param formaPagamentoTrocoVO
     *            the formaPagamentoTrocoVO to set
     */
    public void setFormaPagamentoTrocoVO(FormaPagamentoVO formaPagamentoTrocoVO) {
        this.formaPagamentoTrocoVO = formaPagamentoTrocoVO;
    }

    /**
     * @return the contaCorrenteTrocoVO
     */
    public ContaCorrenteVO getContaCorrenteTrocoVO() {
        if (contaCorrenteTrocoVO == null) {
            contaCorrenteTrocoVO = new ContaCorrenteVO();
        }
        return contaCorrenteTrocoVO;
    }

    /**
     * @param contaCorrenteTrocoVO
     *            the contaCorrenteTrocoVO to set
     */
    public void setContaCorrenteTrocoVO(ContaCorrenteVO contaCorrenteTrocoVO) {
        this.contaCorrenteTrocoVO = contaCorrenteTrocoVO;
    }

    public PessoaVO getAluno() {
        if (aluno == null) {
            aluno = new PessoaVO();
        }
        return aluno;
    }

    public void setAluno(PessoaVO aluno) {
        this.aluno = aluno;
    }

    public Date getDataRegistro() {
        if (dataRegistro == null) {
            dataRegistro = new Date();
        }
        return dataRegistro;
    }

    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
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

    
    public ParceiroVO getParceiro() {
        if(parceiro ==null){
            parceiro = new ParceiroVO();
        }
        return parceiro;
    }

    
    public void setParceiro(ParceiroVO parceiro) {
        this.parceiro = parceiro;
    }

	public Date getDataEstorno() {
		if (dataEstorno == null) {
			dataEstorno = new Date();
		}
		return dataEstorno;
	}

	public void setDataEstorno(Date dataEstorno) {
		this.dataEstorno = dataEstorno;
	}

	public OperadoraCartaoVO getOperadoraCartao() {
		if (operadoraCartao == null) {
			operadoraCartao = new OperadoraCartaoVO();
		}
		return operadoraCartao;
	}

	public void setOperadoraCartao(OperadoraCartaoVO operadoraCartao) {
		this.operadoraCartao = operadoraCartao;
	}
	
	public boolean isDesconsiderarConciliacaoBancaria() {
		return desconsiderarConciliacaoBancaria;
	}
	
	public void setDesconsiderarConciliacaoBancaria(boolean desconsiderarConciliacaoBancaria) {
		this.desconsiderarConciliacaoBancaria = desconsiderarConciliacaoBancaria;
	}
	
	/**
	 * INICIO MERGE EDIGAR 24/05/18
	 */
	private List<ContaPagarVO> listaAdiantamentosUtilizadosAbaterContasPagar;
	private Double valorTotalAtiantamentosAbaterContasPagar;
	
	public Boolean getNegociacaoPagamentoComValorAPagarZerado() {
		if (!this.getContaPagarNegociacaoPagamentoVOs().isEmpty()) {
			// caso tenha contas a pagar adicionadas para a negociacao.
			if (this.getValorTotal().doubleValue() <= 0.0) {
				return Boolean.TRUE;
			} 
		}
		return Boolean.FALSE;
	}

	public List<ContaPagarVO> getListaAdiantamentosUtilizadosAbaterContasPagar() {
		if (listaAdiantamentosUtilizadosAbaterContasPagar == null) {
			listaAdiantamentosUtilizadosAbaterContasPagar = new ArrayList<ContaPagarVO>();
		}
		return listaAdiantamentosUtilizadosAbaterContasPagar;
	}

	public void setListaAdiantamentosUtilizadosAbaterContasPagar(List<ContaPagarVO> listaAdiantamentosUtilizadosAbaterContasPagar) {
		this.listaAdiantamentosUtilizadosAbaterContasPagar = listaAdiantamentosUtilizadosAbaterContasPagar;
	}

	public Double getValorTotalAtiantamentosAbaterContasPagar() {
		if (valorTotalAtiantamentosAbaterContasPagar == null) {
			valorTotalAtiantamentosAbaterContasPagar = 0.0;
		}
		return valorTotalAtiantamentosAbaterContasPagar;
	}

	public void setValorTotalAtiantamentosAbaterContasPagar(Double valorTotalAtiantamentosAbaterContasPagar) {
		this.valorTotalAtiantamentosAbaterContasPagar = valorTotalAtiantamentosAbaterContasPagar;
	}

	public Integer getCodigoSacado() {
		if (getTipoSacado().equals(TipoSacado.FORNECEDOR.getValor())) {
			return this.getFornecedor().getCodigo();
		}
		if (getTipoSacado().equals(TipoSacado.FUNCIONARIO_PROFESSOR.getValor())) {
			return this.getFuncionario().getCodigo();
		}
		if (getTipoSacado().equals(TipoSacado.BANCO.getValor())) {
			return this.getBanco().getCodigo();
		}
		if (getTipoSacado().equals(TipoSacado.ALUNO.getValor())) {
			return this.getAluno().getCodigo();
		}
		if (getTipoSacado().equals(TipoSacado.RESPONSAVEL_FINANCEIRO.getValor())) {
			return this.getResponsavelFinanceiro().getCodigo();
		}
		if (getTipoSacado().equals(TipoSacado.PARCEIRO.getValor())) {
			return this.getParceiro().getCodigo();
		}
		if (getTipoSacado().equals(TipoSacado.OPERADORA_CARTAO.getValor())) {
			return this.getOperadoraCartao().getCodigo();
		}
		return 0;
	}
	
	public Boolean getExisteContaPagarAdiantamentoNaNegociacaoPagamento() {
		for (ContaPagarNegociacaoPagamentoVO contaPagarNegociacaoPagamentoVO : this.getContaPagarNegociacaoPagamentoVOs()) {
			if (contaPagarNegociacaoPagamentoVO.getContaPagar().getTipoOrigem().equals(OrigemContaPagar.ADIANTAMENTO.getValor())) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	@Override
	public String toString() {
		return "NegociacaoPagamentoVO [codigo=" + getCodigo() + ", data=" + getData_Apresentar() + ", valorTotalPagamento=" + getValorTotalPagamento() + ", valorTotal=" + valorTotal + ", valorTroco=" + getValorTroco() + ", responsavel=" + getResponsavel().getNome() + ", favorecido=" + getNomeFavorecido() + ", tipoPessoa=" + 
	           getTipoSacado() + ", unidadeEnsino=" + getUnidadeEnsino().getNome() + "dataEstorno=" + getDataEstorno() + "]";
	}	
	public String getTipoPessoaApresentar() {		
			return TipoSacado.getDescricao(getTipoSacado());		
	}	
	
}

