package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.enumerador.FormaPadraoDataBaseCartaoRecorrenteEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;

/**
 * Reponsável por manter os dados da entidade NegociacaoRecebimento. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class NegociacaoRecebimentoVO extends SuperVO {

	private Integer codigo;
	private Date data;
	private Date dataRegistro;
	private Double valorTotalRecebimento;
	private Double valorTotal;
	private Double valorTroco;
	private Double valorTrocoAlteracao;
	private Boolean alterouConteudo;
	private String motivoAlteracao;
	private UsuarioVO responsavel;
	private ContaCorrenteVO contaCorrenteCaixa;
	private PessoaVO pessoa;
	private ParceiroVO parceiroVO;
	private FornecedorVO fornecedor;
	private String matricula;
	private String tipoPessoa;
	private UnidadeEnsinoVO unidadeEnsino;
	private List<ChequeVO> chequeVOs;
	private String controleErroDescontoRecebimento;

	/**
	 * Atributo responsável por manter os objetos da classe
	 * <code>ContaReceberNegociacaoRecebimento</code>.
	 */
	private List<ContaReceberNegociacaoRecebimentoVO> contaReceberNegociacaoRecebimentoVOs;
	/**
	 * Atributo responsável por manter os objetos da classe
	 * <code>FormaPagamentoNegociacaoRecebimento</code>.
	 */
	private List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs;
	private String observacao;
	private Boolean recebimentoBoletoAutomatico;

	private Date dataCreditoBoletoBancario;
	private Date dataEstorno;
	private boolean desconsiderarConciliacaoBancaria = false;
	/* TRANSIENT
	 * Campo utilizado no momento do processamento de arquivo de retorno e controle de geração da taxa bancaria no extrato conta corrente.
	 * */
	private Double valorTaxaBancaria;
	private Boolean criarRegistroRecorrenciaDCC;
	private Boolean realizandoPagamentoJobRecorrencia;
	private Boolean jobExecutadaManualmente;
	private CartaoCreditoDebitoRecorrenciaPessoaVO cartaoCreditoDebitoRecorrenciaPessoaVO;
	
	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>NegociacaoRecebimento</code>. Cria uma
	 * nova instância desta entidade, inicializando automaticamente seus
	 * atributos (Classe VO).
	 */
	public NegociacaoRecebimentoVO() {
		super();
	}

	/**
	 * Operação responsável por validar a unicidade dos dados de um objeto da
	 * classe <code>NegociacaoRecebimentoVO</code>.
	 */
	public static void validarUnicidade(List<NegociacaoRecebimentoVO> lista, NegociacaoRecebimentoVO obj) throws ConsistirException {
		for (NegociacaoRecebimentoVO repetido : lista) {
		}
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>NegociacaoRecebimentoVO</code>. Todos os tipos de consistência de
	 * dados são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public static void validarDados(NegociacaoRecebimentoVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo UNIDADE DE ENSINO (Negociação Recebimento) deve ser informado.");
		}
		if (obj.getContaCorrenteCaixa().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo CAIXA (Negociação Recebimento) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getTipoPessoa())) {
			throw new ConsistirException("O campo TIPO PESSOA (Negociação Recebimento) deve ser informado.");
		}
		if (!obj.getTipoFornecedor() && obj.getPessoa().getCodigo().intValue() == 0 && obj.getParceiroVO().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo PESSOA (Negociação Recebimento) deve ser informado.");
		}
		if (obj.getTipoFornecedor() && obj.getFornecedor().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo FORNECEDOR (Negociação Recebimento) deve ser informado.");
		}
		if (Uteis.arrendondarForcando2CadasDecimais(obj.getValorTotal()) > Uteis.arrendondarForcando2CadasDecimais(obj.getValorTotalRecebimento())) {
			throw new ConsistirException("O VALOR RECEBIDO (Negociação Recebimento) deve ser maior ou igual ao VALOR TOTAL.");
		}
		if (obj.getContaReceberNegociacaoRecebimentoVOs().isEmpty()) {
			throw new ConsistirException("Deve ser informados as CONTAS A RECEBER (Negociação Recebimento).");
		}
		if (obj.getCodigo().intValue() != 0) {
			if (obj.getMotivoAlteracao().equals("")) {
				throw new ConsistirException("O campo MOTIVO ALTERAÇÃO ou EXCLUSÃO (Negociação Recebimento) deve ser informado.");
			}
		}
		ContaReceberVO unidadeExistente= null;
		for (ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO : obj.getContaReceberNegociacaoRecebimentoVOs()) {
			if(!Uteis.isAtributoPreenchido(unidadeExistente) || !Uteis.isAtributoPreenchido(unidadeExistente.getUnidadeEnsinoFinanceira())){
				unidadeExistente = contaReceberNegociacaoRecebimentoVO.getContaReceber();
			}else if(!unidadeExistente.getUnidadeEnsinoFinanceira().getCodigo().equals(contaReceberNegociacaoRecebimentoVO.getContaReceber().getUnidadeEnsinoFinanceira().getCodigo())){
				throw new ConsistirException("Não é possivél realizar o recebimento de contas receber de unidade de ensino financeiras diferentes. Contas Receber Nr Documento ("+unidadeExistente.getNrDocumento()+", "+ contaReceberNegociacaoRecebimentoVO.getContaReceber().getNrDocumento() +") ");
			}
		}
		
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo
	 * String.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void realizarUpperCaseDados() {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return;
		}
	}

	public List<ContaReceberVO> getContaReceberVOs() {
		List<ContaReceberVO> contas = new ArrayList<ContaReceberVO>(0);
		for (ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO : contaReceberNegociacaoRecebimentoVOs) {
			contas.add(contaReceberNegociacaoRecebimentoVO.getContaReceber());
		}
		return contas;
	}

	public Boolean getExisteContaReceber() {
		return !getContaReceberNegociacaoRecebimentoVOs().isEmpty();
	}

	public Boolean getTipoResponsavelFinanceiro() {
		return (getTipoPessoa().equals("RF"));
	}

	public Boolean getTipoAluno() {
		if (getTipoPessoa().equals("AL")) {
			return Boolean.TRUE;
		}
		return (Boolean.FALSE);
	}

	public Boolean getTipoFornecedor() {
		if (getTipoPessoa().equals("FO")) {
			return Boolean.TRUE;
		}
		return (Boolean.FALSE);
	}

	public Boolean getTipoRequerente() {
		if (getTipoPessoa().equals("RE")) {
			return Boolean.TRUE;
		}
		return (Boolean.FALSE);
	}

	public Boolean getTipoFuncionario() {
		if (getTipoPessoa().equals("FU")) {
			return (Boolean.TRUE);
		}
		return (Boolean.FALSE);
	}

	public Boolean getTipoCandidato() {
		if (getTipoPessoa().equals("CA")) {
			return (Boolean.TRUE);
		}
		return (Boolean.FALSE);
	}

	public Boolean getTipoParceiro() {
		if (getTipoPessoa().equals("PA")) {
			return (Boolean.TRUE);
		}
		return (Boolean.FALSE);
	}

	public Boolean getExisteUnidadeEnsino() {
		if (getUnidadeEnsino().getCodigo().intValue() == 0) {
			return false;
		}
		return true;
	}

	public Boolean getExistePessoa() {
		return (!getTipoParceiro() && !getTipoFornecedor() && getPessoa().getCodigo().intValue() != 0) || (getTipoParceiro() && getParceiroVO().getCodigo().intValue() != 0) || (getTipoFornecedor() && getFornecedor().getCodigo().intValue() != 0);
	}

	public void atualizarChequeRecebimento() {
		Iterator i = getFormaPagamentoNegociacaoRecebimentoVOs().iterator();
		while (i.hasNext()) {
			FormaPagamentoNegociacaoRecebimentoVO rLogVO = (FormaPagamentoNegociacaoRecebimentoVO) i.next();
			if (rLogVO.getCheque().getCodigo().intValue() != 0) {
				getChequeVOs().add(rLogVO.getCheque());
			}
		}
	}

	

	public Double validarTroco() {
		if (Uteis.arrendondarForcando2CadasDecimais(getValorTotal()) < Uteis.arrendondarForcando2CadasDecimais(getValorTotalRecebimento())) {
			return Uteis.arrendondarForcando2CadasDecimais(getValorTotalRecebimento().doubleValue() - getValorTotal().doubleValue());
		}
		return 0.0;
	}

	public void calcularTotal(ConfiguracaoFinanceiroVO conf, UsuarioVO usuario) throws Exception {
		calcularTotal(null, conf, usuario);
	}

	public void calcularTotal(Date dataOcorrencia, ConfiguracaoFinanceiroVO conf, UsuarioVO usuario) throws Exception {
		Boolean porcentagemMultaAlterada = false;
		setValorTotal(0.0);
		setControleErroDescontoRecebimento("");
		try {
			for (ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO : getContaReceberNegociacaoRecebimentoVOs()) {
				if (!conf.getPercentualMultaPadrao().equals(contaReceberNegociacaoRecebimentoVO.getContaReceber().getMultaPorcentagem())) {
					porcentagemMultaAlterada = true;
				}
				conf.setUsaDescontoCompostoPlanoDesconto(contaReceberNegociacaoRecebimentoVO.getContaReceber().getUsaDescontoCompostoPlanoDesconto());
				contaReceberNegociacaoRecebimentoVO.setValorTotal(contaReceberNegociacaoRecebimentoVO.getContaReceber().getCalcularValorFinal(dataOcorrencia, conf, porcentagemMultaAlterada, Uteis.getDataJDBC(this.getData()), usuario));
				// contaReceberNegociacaoRecebimentoVO.setValorTotal(contaReceberNegociacaoRecebimentoVO.getValorTotal()
				// +
				// contaReceberNegociacaoRecebimentoVO.getContaReceber().getAcrescimo());
				setValorTotal(Uteis.arrendondarForcando2CadasDecimais(getValorTotal() + contaReceberNegociacaoRecebimentoVO.getValorTotal()));
				if (getControleErroDescontoRecebimento().equals("")) {
					setControleErroDescontoRecebimento(contaReceberNegociacaoRecebimentoVO.getContaReceber().getControleErroDescontoRecebimento());
				}
			}
			if (getControleErroDescontoRecebimento().equals("PO")) {
				throw new ConsistirException("O valor do desconto não pode ser maior que 100%.");
			}
			if (getControleErroDescontoRecebimento().equals("VA")) {
				throw new ConsistirException("O valor do desconto não pode ser maior que o valor total.");
			}
			setAlterouConteudo(Boolean.TRUE);
		} finally {
			porcentagemMultaAlterada = null;
		}
	}

	public Double getResiduo() {
		if (getValorTotal() - getValorTotalRecebimento() >= 0) {
			return Uteis.arrendondarForcando2CadasDecimais(getValorTotal() - getValorTotalRecebimento());
		}
		return 0.0;
	}

	public Boolean getEdicao() {
		if (getCodigo().intValue() != 0 && !getNovoObj()) {
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

	/**
	 * Operação responsável por adicionar um novo objeto da classe
	 * <code>FormaPagamentoNegociacaoRecebimentoVO</code> ao List
	 * <code>formaPagamentoNegociacaoRecebimentoVOs</code>. Utiliza o atributo
	 * padrão de consulta da classe
	 * <code>FormaPagamentoNegociacaoRecebimento</code> - getFormaPagamento() -
	 * como identificador (key) do objeto no List.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>FormaPagamentoNegociacaoRecebimentoVO</code> que será
	 *            adiocionado ao Hashtable correspondente.
	 */
	public void adicionarObjFormaPagamentoNegociacaoRecebimentoVOs(FormaPagamentoNegociacaoRecebimentoVO obj) throws Exception {
		FormaPagamentoNegociacaoRecebimentoVO.validarDados(obj);
		obj.setNegociacaoRecebimento(getCodigo());
		int index = 0;
		Iterator i = getFormaPagamentoNegociacaoRecebimentoVOs().iterator();
		while (i.hasNext()) {
			FormaPagamentoNegociacaoRecebimentoVO objExistente = (FormaPagamentoNegociacaoRecebimentoVO) i.next();
			if (obj.getFormaPagamento().getTipo().equals("DI") && objExistente.getFormaPagamento().getTipo().equals("DI") && obj.getFormaPagamento().getCodigo().equals(objExistente.getFormaPagamento().getCodigo())) {
				setValorTotalRecebimento(Uteis.arrendondarForcando2CadasDecimais(getValorTotalRecebimento() - objExistente.getValorRecebimento()));
				setValorTotalRecebimento(Uteis.arrendondarForcando2CadasDecimais(getValorTotalRecebimento() + obj.getValorRecebimento()));
				getFormaPagamentoNegociacaoRecebimentoVOs().set(index, obj);
				setAlterouConteudo(true);
				return;
			}
			if (obj.getFormaPagamento().getTipo().equals("PE") && objExistente.getFormaPagamento().getTipo().equals("PE") && obj.getFormaPagamento().getCodigo().equals(objExistente.getFormaPagamento().getCodigo())) {
				setValorTotalRecebimento(Uteis.arrendondarForcando2CadasDecimais(getValorTotalRecebimento() - objExistente.getValorRecebimento()));
				setValorTotalRecebimento(Uteis.arrendondarForcando2CadasDecimais(getValorTotalRecebimento() + obj.getValorRecebimento()));
				getFormaPagamentoNegociacaoRecebimentoVOs().set(index, obj);
				setAlterouConteudo(true);
				return;
			}
			if ((obj.getFormaPagamento().getTipo().equals("BO") && objExistente.getFormaPagamento().getTipo().equals("BO")) || (obj.getFormaPagamento().getTipo().equals("CD") && objExistente.getFormaPagamento().getTipo().equals("CD") && obj.getOperadoraCartaoVO().getCodigo().equals(objExistente.getOperadoraCartaoVO().getCodigo()) && obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroReciboTransacao().equals(objExistente.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroReciboTransacao())) && obj.getContaCorrente().getCodigo().intValue() == objExistente.getContaCorrente().getCodigo().intValue()) {
				if (obj.getFormaPagamento().getCodigo().equals(objExistente.getFormaPagamento().getCodigo())) {
					setValorTotalRecebimento(Uteis.arrendondarForcando2CadasDecimais(getValorTotalRecebimento() - objExistente.getValorRecebimento()));
					setValorTotalRecebimento(Uteis.arrendondarForcando2CadasDecimais(getValorTotalRecebimento() + obj.getValorRecebimento()));
					getFormaPagamentoNegociacaoRecebimentoVOs().set(index, obj);
					setAlterouConteudo(true);
					return;
				}
			}
			if ((obj.getFormaPagamento().getTipo().equals("CH") && objExistente.getFormaPagamento().getTipo().equals("CH")) && obj.getCheque().getNumero().equals(objExistente.getCheque().getNumero()) && obj.getCheque().getBanco().equals(objExistente.getCheque().getBanco()) && obj.getCheque().getSacado().equals(objExistente.getCheque().getSacado())) {
				throw new Exception("O cheque com o número " + obj.getCheque().getNumero() + " já foi adicionado.");
			}
			index++;
		}
		if (obj.getFormaPagamento().getTipo().equals("CH")) {
			getChequeVOs().add(obj.getCheque());
		}
		setAlterouConteudo(true);
		getFormaPagamentoNegociacaoRecebimentoVOs().add(obj);
		setValorTotalRecebimento(Uteis.arrendondarForcando2CadasDecimais(getValorTotalRecebimento() + obj.getValorRecebimento()));
	}

	/**
	 * Operação responsável por excluir um objeto da classe
	 * <code>FormaPagamentoNegociacaoRecebimentoVO</code> no List
	 * <code>formaPagamentoNegociacaoRecebimentoVOs</code>. Utiliza o atributo
	 * padrão de consulta da classe
	 * <code>FormaPagamentoNegociacaoRecebimento</code> - getFormaPagamento() -
	 * como identificador (key) do objeto no List.
	 * 
	 * @param formaPagamento
	 *            Parâmetro para localizar e remover o objeto do List.
	 */
	public void excluirObjFormaPagamentoNegociacaoRecebimentoVOs(FormaPagamentoNegociacaoRecebimentoVO obj) throws Exception {
		int index = 0;
		for (Iterator<FormaPagamentoNegociacaoRecebimentoVO> iterator = getFormaPagamentoNegociacaoRecebimentoVOs().iterator(); iterator.hasNext();) {
			FormaPagamentoNegociacaoRecebimentoVO objExistente = (FormaPagamentoNegociacaoRecebimentoVO) iterator.next();	
			if (obj.getFormaPagamento().getTipo().equals("DI") && objExistente.getFormaPagamento().getTipo().equals("DI")) {
				setValorTotalRecebimento(Uteis.arrendondarForcando2CadasDecimais(getValorTotalRecebimento() - objExistente.getValorRecebimento()));
				getFormaPagamentoNegociacaoRecebimentoVOs().remove(index);
				setAlterouConteudo(true);
				return;
			}
			if ((obj.getFormaPagamento().getTipo().equals("BO") && objExistente.getFormaPagamento().getTipo().equals("BO")) || (obj.getFormaPagamento().getTipo().equals("CD") && objExistente.getFormaPagamento().getTipo().equals("CD")) && obj.getContaCorrente().getCodigo().intValue() == objExistente.getContaCorrente().getCodigo().intValue()) {
				setValorTotalRecebimento(Uteis.arrendondarForcando2CadasDecimais(getValorTotalRecebimento() - objExistente.getValorRecebimento()));
				getFormaPagamentoNegociacaoRecebimentoVOs().remove(index);
				setAlterouConteudo(true);
				return;
			}
			if ((obj.getFormaPagamento().getTipo().equals("CA") && objExistente.getFormaPagamento().getTipo().equals("CA")) 
					//&& (obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroParcela().equals(objExistente.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroParcela())) 
					&& (obj.getContaCorrente().getCodigo().intValue() == objExistente.getContaCorrente().getCodigo().intValue()) && (obj.getOperadoraCartaoVO().getNome().equals(objExistente.getOperadoraCartaoVO().getNome())) && (obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao().equals(objExistente.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao()))) {
				setValorTotalRecebimento(Uteis.arrendondarForcando2CadasDecimais(getValorTotalRecebimento() - objExistente.getValorRecebimento()));
				iterator.remove();//getFormaPagamentoNegociacaoRecebimentoVOs().remove(objExistente);
				setAlterouConteudo(true);
			}
			if ((obj.getFormaPagamento().getTipo().equals("CH") && objExistente.getFormaPagamento().getTipo().equals("CH")) && obj.getCheque().getNumero().equals(objExistente.getCheque().getNumero()) && obj.getCheque().getBanco().equals(objExistente.getCheque().getBanco())) {
				setValorTotalRecebimento(Uteis.arrendondarForcando2CadasDecimais(getValorTotalRecebimento() - objExistente.getValorRecebimento()));
				getFormaPagamentoNegociacaoRecebimentoVOs().remove(index);
				setAlterouConteudo(true);
				return;
			}
			if ((obj.getFormaPagamento().getTipo().equals("DE") && objExistente.getFormaPagamento().getTipo().equals("DE")) || (obj.getFormaPagamento().getTipo().equals("DC") && objExistente.getFormaPagamento().getTipo().equals("DC")) && obj.getContaCorrente().getCodigo().intValue() == objExistente.getContaCorrente().getCodigo().intValue()) {
				setValorTotalRecebimento(Uteis.arrendondarForcando2CadasDecimais(getValorTotalRecebimento() - objExistente.getValorRecebimento()));
				getFormaPagamentoNegociacaoRecebimentoVOs().remove(index);
				setAlterouConteudo(true);
				return;
			}
			if (obj.getFormaPagamento().getTipo().equals("IS") && objExistente.getFormaPagamento().getTipo().equals("IS")) {
				setValorTotalRecebimento(Uteis.arrendondarForcando2CadasDecimais(getValorTotalRecebimento() - objExistente.getValorRecebimento()));
				getFormaPagamentoNegociacaoRecebimentoVOs().remove(index);
				setAlterouConteudo(true);
				return;
			}
			if (obj.getFormaPagamento().getTipo().equals(TipoFormaPagamento.PERMUTA.getValor()) 
					&& objExistente.getFormaPagamento().getTipo().equals(TipoFormaPagamento.PERMUTA.getValor())) {
				setValorTotalRecebimento(Uteis.arrendondarForcando2CadasDecimais(getValorTotalRecebimento() - objExistente.getValorRecebimento()));
				getFormaPagamentoNegociacaoRecebimentoVOs().remove(index);
				setAlterouConteudo(true);
				return;
			}
			index++;
			
		}
		
//		int index = 0;
//		Iterator i = getFormaPagamentoNegociacaoRecebimentoVOs().iterator();
//		while (i.hasNext()) {
//		}
	}

	public void removerChequeNegociacao(ChequeVO obj) {
		int index = 0;
		for (ChequeVO objExistente : getChequeVOs()) {
			if (obj.getNumero().equals(objExistente.getNumero()) && obj.getBanco().equals(objExistente.getBanco())) {
				getChequeVOs().remove(index);
				return;
			}
			index++;
		}
	}

	public void removerFormaPagamentoCheque(ChequeVO obj) throws Exception {
		int index = 0;
		for (FormaPagamentoNegociacaoRecebimentoVO fp : getFormaPagamentoNegociacaoRecebimentoVOs()) {
			if (fp.getFormaPagamento().getTipo().equals("CH") && obj.getNumero().equals(fp.getCheque().getNumero()) && obj.getBanco().equals(fp.getCheque().getBanco())) {
				setValorTotalRecebimento(Uteis.arrendondarForcando2CadasDecimais(getValorTotalRecebimento() - fp.getValorRecebimento()));
				getFormaPagamentoNegociacaoRecebimentoVOs().remove(index);
				return;
			}
			index++;
		}

	}

	/**
	 * Operação responsável por consultar um objeto da classe
	 * <code>FormaPagamentoNegociacaoRecebimentoVO</code> no List
	 * <code>formaPagamentoNegociacaoRecebimentoVOs</code>. Utiliza o atributo
	 * padrão de consulta da classe
	 * <code>FormaPagamentoNegociacaoRecebimento</code> - getFormaPagamento() -
	 * como identificador (key) do objeto no List.
	 * 
	 * @param formaPagamento
	 *            Parâmetro para localizar o objeto do List.
	 */
	public FormaPagamentoNegociacaoRecebimentoVO consultarObjFormaPagamentoNegociacaoRecebimentoVO(Integer formaPagamento) throws Exception {
		Iterator i = getFormaPagamentoNegociacaoRecebimentoVOs().iterator();
		while (i.hasNext()) {
			FormaPagamentoNegociacaoRecebimentoVO objExistente = (FormaPagamentoNegociacaoRecebimentoVO) i.next();
			if (objExistente.getFormaPagamento().getCodigo().equals(formaPagamento)) {
				return objExistente;
			}
		}
		return null;
	}

	/**
	 * Operação responsável por adicionar um novo objeto da classe
	 * <code>ContaReceberNegociacaoRecebimentoVO</code> ao List
	 * <code>contaReceberNegociacaoRecebimentoVOs</code>. Utiliza o atributo
	 * padrão de consulta da classe
	 * <code>ContaReceberNegociacaoRecebimento</code> - getContaReceber() - como
	 * identificador (key) do objeto no List.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>ContaReceberNegociacaoRecebimentoVO</code> que será
	 *            adiocionado ao Hashtable correspondente.
	 */
	public void adicionarObjContaReceberNegociacaoRecebimentoVOs(ContaReceberNegociacaoRecebimentoVO obj) throws Exception {
		ContaReceberNegociacaoRecebimentoVO.validarDados(obj);
		obj.setNegociacaoRecebimento(getCodigo());
		int index = 0;
		Iterator i = getContaReceberNegociacaoRecebimentoVOs().iterator();
		while (i.hasNext()) {
			ContaReceberNegociacaoRecebimentoVO objExistente = (ContaReceberNegociacaoRecebimentoVO) i.next();
			if (!objExistente.getContaReceber().getUnidadeEnsinoFinanceira().getCodigo().equals(obj.getContaReceber().getUnidadeEnsinoFinanceira().getCodigo())) {
				throw new ConsistirException("Não é possivél realizar o recebimento de contas receber de unidade de ensino financeiras diferentes. Contas Receber Nr Documento ("+objExistente.getContaReceber().getNrDocumento()+", "+ obj.getContaReceber().getNrDocumento() +") ");
			}
			if (objExistente.getContaReceber().getCodigo().equals(obj.getContaReceber().getCodigo())) {
				getContaReceberNegociacaoRecebimentoVOs().set(index, obj);
				setAlterouConteudo(true);
				return;
			}
			index++;
		}
		setAlterouConteudo(true);
		getContaReceberNegociacaoRecebimentoVOs().add(obj);
		// adicionarObjSubordinadoOC
	}

	/**
	 * Operação responsável por excluir um objeto da classe
	 * <code>ContaReceberNegociacaoRecebimentoVO</code> no List
	 * <code>contaReceberNegociacaoRecebimentoVOs</code>. Utiliza o atributo
	 * padrão de consulta da classe
	 * <code>ContaReceberNegociacaoRecebimento</code> - getContaReceber() - como
	 * identificador (key) do objeto no List.
	 * 
	 * @param contaReceber
	 *            Parâmetro para localizar e remover o objeto do List.
	 */
	public void excluirObjContaReceberNegociacaoRecebimentoVOs(Integer contaReceber) throws Exception {
		int index = 0;
		Iterator i = getContaReceberNegociacaoRecebimentoVOs().iterator();
		while (i.hasNext()) {
			ContaReceberNegociacaoRecebimentoVO objExistente = (ContaReceberNegociacaoRecebimentoVO) i.next();
			if (objExistente.getContaReceber().getCodigo().equals(contaReceber)) {
				getContaReceberNegociacaoRecebimentoVOs().remove(index);
				setValorTotal(Uteis.arrendondarForcando2CadasDecimais(getValorTotal() - objExistente.getValorTotal()));
				setAlterouConteudo(true);
				return;
			}
			index++;
		}
		// excluirObjSubordinadoOC
	}

	public void excluirObjContaReceberNegociacaoRecebimentoVOsContaReceberAgrupada(Integer codContaReceberAgrupada) throws Exception {
		int index = 0;
		Iterator i = getContaReceberNegociacaoRecebimentoVOs().iterator();
		while (i.hasNext()) {
			ContaReceberNegociacaoRecebimentoVO objExistente = (ContaReceberNegociacaoRecebimentoVO) i.next();
			if (objExistente.getContaReceber().getContaReceberAgrupada().intValue() == codContaReceberAgrupada) {
				getContaReceberNegociacaoRecebimentoVOs().remove(index);
				setValorTotal(Uteis.arrendondarForcando2CadasDecimais(getValorTotal() - objExistente.getValorTotal()));
				setAlterouConteudo(true);
				return;
			}
			index++;
		}
		// excluirObjSubordinadoOC
	}
	
	/**
	 * Operação responsável por consultar um objeto da classe
	 * <code>ContaReceberNegociacaoRecebimentoVO</code> no List
	 * <code>contaReceberNegociacaoRecebimentoVOs</code>. Utiliza o atributo
	 * padrão de consulta da classe
	 * <code>ContaReceberNegociacaoRecebimento</code> - getContaReceber() - como
	 * identificador (key) do objeto no List.
	 * 
	 * @param contaReceber
	 *            Parâmetro para localizar o objeto do List.
	 */
	public ContaReceberNegociacaoRecebimentoVO consultarObjContaReceberNegociacaoRecebimentoVO(Integer contaReceber) throws Exception {
		Iterator i = getContaReceberNegociacaoRecebimentoVOs().iterator();
		while (i.hasNext()) {
			ContaReceberNegociacaoRecebimentoVO objExistente = (ContaReceberNegociacaoRecebimentoVO) i.next();
			if (objExistente.getContaReceber().getCodigo().equals(contaReceber)) {
				return objExistente;
			}
		}
		return null;
		// consultarObjSubordinadoOC
	}

	public String getNomePessoaParceiro() {
		// if(getTipoFornecedor()){
		// return "FORNECEDOR: "+getFornecedor().getNome();
		// }
		// if(getTipoParceiro()){
		// return "PARCEIRO: "+getParceiroVO().getNome();
		// }
		// if(getTipoAluno()){
		// return "ALUNO: "+getPessoa().getNome() ;
		// }
		// if(getTipoResponsavelFinanceiro()){
		// return "RESP. FINAN.: "+getPessoa().getNome() ;
		// }
		// if(getTipoCandidato()){
		// return "CANDIDATO: "+getPessoa().getNome() ;
		// }
		// if(getTipoFuncionario()){
		// return "FUNCIONÁRIO: "+getPessoa().getNome() ;
		// }
		// if(getTipoRequerente()){
		// return "REQUERENTE: "+getPessoa().getNome() ;
		// }
		return getPessoa().getNome() + getFornecedor().getNome() + getParceiroVO().getNome();
	}

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return (responsavel);
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}

	public Double getValorTotal() {
		if (valorTotal == null) {
			valorTotal = 0.0;
		}
		return (valorTotal);
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Double getValorTotalRecebimento() {
		if (valorTotalRecebimento == null) {
			valorTotalRecebimento = 0.0;
		}
		return (valorTotalRecebimento);
	}

	public void setValorTotalRecebimento(Double valorTotalRecebimento) {
		this.valorTotalRecebimento = valorTotalRecebimento;
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

	/**
	 * @return the contaCorrenteCaixa
	 */
	public ContaCorrenteVO getContaCorrenteCaixa() {
		if (contaCorrenteCaixa == null) {
			contaCorrenteCaixa = new ContaCorrenteVO();
		}
		return contaCorrenteCaixa;
	}

	/**
	 * @param contaCorrenteCaixa
	 *            the contaCorrenteCaixa to set
	 */
	public void setContaCorrenteCaixa(ContaCorrenteVO contaCorrenteCaixa) {
		this.contaCorrenteCaixa = contaCorrenteCaixa;
	}

	/**
	 * @param pessoa
	 *            the pessoa to set
	 */
	public void setPessoa(PessoaVO pessoa) {
		this.pessoa = pessoa;
	}

	/**
	 * @return the pessoa
	 */
	public PessoaVO getPessoa() {
		if (pessoa == null) {
			pessoa = new PessoaVO();
		}
		return pessoa;
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

	public Double getValorTroco() {
		if (valorTroco == null) {
			valorTroco = 0.0;
		}
		return valorTroco;
	}

	public Double getValorTrocoCalculado() {
		setValorTroco(0.0);
		if (Uteis.arrendondarForcando2CadasDecimais(getValorTotalRecebimento()) > Uteis.arrendondarForcando2CadasDecimais(getValorTotal())) {
			valorTroco = Uteis.arrendondarForcando2CadasDecimais(getValorTotalRecebimento().doubleValue() - getValorTotal().doubleValue());
		}
		return valorTroco;
	}

	public void setValorTroco(Double valorTroco) {
		this.valorTroco = valorTroco;
	}

	public Double getValorTrocoAlteracao() {
		if (valorTrocoAlteracao == null) {
			valorTrocoAlteracao = 0.0;
		}
		return valorTrocoAlteracao;
	}

	public void setValorTrocoAlteracao(Double valorTrocoAlteracao) {
		this.valorTrocoAlteracao = valorTrocoAlteracao;
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

	public List<ContaReceberNegociacaoRecebimentoVO> getContaReceberNegociacaoRecebimentoVOs() {
		if (contaReceberNegociacaoRecebimentoVOs == null) {
			contaReceberNegociacaoRecebimentoVOs = new ArrayList<ContaReceberNegociacaoRecebimentoVO>(0);
		}
		return contaReceberNegociacaoRecebimentoVOs;
	}

	public void setContaReceberNegociacaoRecebimentoVOs(List<ContaReceberNegociacaoRecebimentoVO> contaReceberNegociacaoRecebimentoVOs) {
		this.contaReceberNegociacaoRecebimentoVOs = contaReceberNegociacaoRecebimentoVOs;
	}

	public List<FormaPagamentoNegociacaoRecebimentoVO> getFormaPagamentoNegociacaoRecebimentoVOs() {
		if (formaPagamentoNegociacaoRecebimentoVOs == null) {
			formaPagamentoNegociacaoRecebimentoVOs = new ArrayList<FormaPagamentoNegociacaoRecebimentoVO>(0);
		}
		return formaPagamentoNegociacaoRecebimentoVOs;
	}

	public void setFormaPagamentoNegociacaoRecebimentoVOs(List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs) {
		this.formaPagamentoNegociacaoRecebimentoVOs = formaPagamentoNegociacaoRecebimentoVOs;
	}

	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getTipoPessoa() {
		if (tipoPessoa == null) {
			tipoPessoa = "";
		}
		return tipoPessoa;
	}
	
	public String getTipoPessoa_Apresentar() {
		return TipoPessoa.getDescricao(getTipoPessoa());
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
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

	/**
	 * @return the parceiroVO
	 */
	public ParceiroVO getParceiroVO() {
		if (parceiroVO == null) {
			parceiroVO = new ParceiroVO();
		}
		return parceiroVO;
	}

	/**
	 * @param parceiroVO
	 *            the parceiroVO to set
	 */
	public void setParceiroVO(ParceiroVO parceiroVO) {
		this.parceiroVO = parceiroVO;
	}

	public String getControleErroDescontoRecebimento() {
		if (controleErroDescontoRecebimento == null) {
			controleErroDescontoRecebimento = "";
		}
		return controleErroDescontoRecebimento;
	}

	public void setControleErroDescontoRecebimento(String controleErroDescontoRecebimento) {
		this.controleErroDescontoRecebimento = controleErroDescontoRecebimento;
	}

	public String getObservacao() {
		if (observacao == null) {
			observacao = "";
		}
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	/**
	 * @return the recebimentoBoletoAutomatico
	 */
	public Boolean getRecebimentoBoletoAutomatico() {
		if (recebimentoBoletoAutomatico == null) {
			recebimentoBoletoAutomatico = false;
		}
		return recebimentoBoletoAutomatico;
	}

	/**
	 * @param recebimentoBoletoAutomatico
	 *            the recebimentoBoletoAutomatico to set
	 */
	public void setRecebimentoBoletoAutomatico(Boolean recebimentoBoletoAutomatico) {
		this.recebimentoBoletoAutomatico = recebimentoBoletoAutomatico;
	}

	public Date getDataCreditoBoletoBancario() {
		if (dataCreditoBoletoBancario == null) {
			dataCreditoBoletoBancario = getData();
		}
		return dataCreditoBoletoBancario;
	}

	public void setDataCreditoBoletoBancario(Date dataCreditoBoletoBancario) {
		this.dataCreditoBoletoBancario = dataCreditoBoletoBancario;
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

	public FornecedorVO getFornecedor() {
		if (fornecedor == null) {
			fornecedor = new FornecedorVO();
		}
		return fornecedor;
	}

	public void setFornecedor(FornecedorVO fornecedor) {
		this.fornecedor = fornecedor;
	}
	
	public String getNomePessoaRecebimentoTerceirizado() {
		if (getTipoFornecedor()) {
			return getFornecedor().getNome().toUpperCase();
		}
		if (getTipoParceiro()) {
			return getParceiroVO().getNome().toUpperCase();
		}
		return getPessoa().getNome().toUpperCase();
	}
	
	/**
	 * @author Victor Hugo de Paula Costa
	 */
	private Boolean pagamentoCartaoCredito;

	public Boolean getPagamentoCartaoCredito() {
		if(pagamentoCartaoCredito == null) {
			pagamentoCartaoCredito = false;
		}
		return pagamentoCartaoCredito;
	}

	public void setPagamentoCartaoCredito(Boolean pagamentoCartaoCredito) {
		this.pagamentoCartaoCredito = pagamentoCartaoCredito;
	}
	
	//Transient
	private TipoOrigemContaReceber tipoOrigemContaReceber;

	public TipoOrigemContaReceber getTipoOrigemContaReceber() {
		if(tipoOrigemContaReceber == null) {
			tipoOrigemContaReceber = TipoOrigemContaReceber.OUTROS;
		}
		return tipoOrigemContaReceber;
	}

	public void setTipoOrigemContaReceber(TipoOrigemContaReceber tipoOrigemContaReceber) {
		this.tipoOrigemContaReceber = tipoOrigemContaReceber;
	}
	
	public Integer getSizeListaFormaRecebimentoCartaoCreditoVOs() {
		return getFormaPagamentoNegociacaoRecebimentoVOs().size() - 1;
	}
	
	/**
	 * Transient
	 */
	private String mensagemPagamentoCartaoCredito;
	private MatriculaVO matriculaVO;

	public String getMensagemPagamentoCartaoCredito() {
		if(mensagemPagamentoCartaoCredito == null) {
			mensagemPagamentoCartaoCredito = "";
		}
		return mensagemPagamentoCartaoCredito;
	}

	public void setMensagemPagamentoCartaoCredito(String mensagemPagamentoCartaoCredito) {
		this.mensagemPagamentoCartaoCredito = mensagemPagamentoCartaoCredito;
	}

	@Override
	public String toString() {
		return "NegociacaoRecebimentoVO [codigo=" + codigo + ", data=" + data + ", valorTotalRecebimento=" + valorTotalRecebimento + ", valorTotal=" + valorTotal + ", valorTroco=" + valorTroco + ", valorTrocoAlteracao=" + valorTrocoAlteracao + ", alterouConteudo=" + alterouConteudo + ", motivoAlteracao=" + motivoAlteracao + ", responsavel=" + responsavel + ", contaCorrenteCaixa=" + contaCorrenteCaixa + ", pessoa=" + pessoa + ", parceiroVO=" + parceiroVO + ", fornecedor=" + fornecedor + ", matricula=" + matricula + ", tipoPessoa=" + tipoPessoa + ", unidadeEnsino=" + unidadeEnsino + ", chequeVOs=" + chequeVOs + ", controleErroDescontoRecebimento=" + controleErroDescontoRecebimento + ", contaReceberNegociacaoRecebimentoVOs=" + contaReceberNegociacaoRecebimentoVOs + ", formaPagamentoNegociacaoRecebimentoVOs=" + formaPagamentoNegociacaoRecebimentoVOs + ", observacao=" + observacao + ", recebimentoBoletoAutomatico=" + recebimentoBoletoAutomatico + ", dataCreditoBoletoBancario="
				+ dataCreditoBoletoBancario + ", dataEstorno=" + dataEstorno + ", pagamentoCartaoCredito=" + pagamentoCartaoCredito + ", tipoOrigemContaReceber=" + tipoOrigemContaReceber + ", mensagemPagamentoCartaoCredito=" + mensagemPagamentoCartaoCredito + "]";
	}

	public MatriculaVO getMatriculaVO() {
		if(matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}
	
	/**
	 * @author Victor Hugo de Paula Costa 5.0.4.0 17/03/2016
	 */
	private ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO;

	public ConfiguracaoRecebimentoCartaoOnlineVO getConfiguracaoRecebimentoCartaoOnlineVO() {
		if(configuracaoRecebimentoCartaoOnlineVO == null)
			configuracaoRecebimentoCartaoOnlineVO = new ConfiguracaoRecebimentoCartaoOnlineVO();
		return configuracaoRecebimentoCartaoOnlineVO;
	}

	public void setConfiguracaoRecebimentoCartaoOnlineVO(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO) {
		this.configuracaoRecebimentoCartaoOnlineVO = configuracaoRecebimentoCartaoOnlineVO;
	}
	
	
	private Boolean pagamentoComDCC;

	public Boolean getPagamentoComDCC() {
		if(pagamentoComDCC == null) {
			pagamentoComDCC = false;
		}
		return pagamentoComDCC;
	}

	public void setPagamentoComDCC(Boolean pagamentoComDCC) {
		this.pagamentoComDCC = pagamentoComDCC;
	}
	
	public boolean isDesconsiderarConciliacaoBancaria() {
		return desconsiderarConciliacaoBancaria;
	}
	
	public void setDesconsiderarConciliacaoBancaria(boolean desconsiderarConciliacaoBancaria) {
		this.desconsiderarConciliacaoBancaria = desconsiderarConciliacaoBancaria;
	}
	
	public Double getValorTaxaBancaria() {
		if (valorTaxaBancaria == null) {
			valorTaxaBancaria = 0.0;
		}
		return valorTaxaBancaria;
	}

	public void setValorTaxaBancaria(Double valorTaxaBancaria) {
		this.valorTaxaBancaria = valorTaxaBancaria;
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
	
	public Date realizarCalculoMaiorDataVencimento() {
		Date vencimento = null;
		for (ContaReceberNegociacaoRecebimentoVO c : getContaReceberNegociacaoRecebimentoVOs()) {
			if (vencimento == null || c.getContaReceber().getDataVencimento().after(vencimento))
			vencimento = c.getContaReceber().getDataVencimento();
		}
		return vencimento;
	}
	
	public List<TipoOrigemContaReceber> getListaTipoOrigemContaReceber() {
		List<TipoOrigemContaReceber> lista = new ArrayList<>();
		for (ContaReceberNegociacaoRecebimentoVO c : getContaReceberNegociacaoRecebimentoVOs()) {
			if(!lista.contains(c.getContaReceber().getTipoOrigemContaReceber())) {
				lista.add(c.getContaReceber().getTipoOrigemContaReceber());	
			}
		}
		return lista;
	}

	public Boolean getApresentarOrientacaoDCC() {
		for (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO : getFormaPagamentoNegociacaoRecebimentoVOs()) {
			if (formaPagamentoNegociacaoRecebimentoVO.getUtilizarCartaoComoPagamentoRecorrenteProximaParcela()) {
				return true;
			}
		}
		return false;
	}
	
	public Boolean getPossuiRegistroUtilizarCartaoComoRecorrencia() {
		for (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO : getFormaPagamentoNegociacaoRecebimentoVOs()) {
			if (formaPagamentoNegociacaoRecebimentoVO.getUtilizarCartaoComoPagamentoRecorrenteProximaParcela()) {
				return true;
			}
		}
		return false;
	}

	public Boolean getCriarRegistroRecorrenciaDCC() {
		if (criarRegistroRecorrenciaDCC == null) {
			criarRegistroRecorrenciaDCC = false;
		}
		return criarRegistroRecorrenciaDCC;
	}

	public void setCriarRegistroRecorrenciaDCC(Boolean criarRegistroRecorrenciaDCC) {
		this.criarRegistroRecorrenciaDCC = criarRegistroRecorrenciaDCC;
	}
	
	public Boolean getRealizandoPagamentoJobRecorrencia() {
		if (realizandoPagamentoJobRecorrencia == null) {
			realizandoPagamentoJobRecorrencia = false;
		}
		return realizandoPagamentoJobRecorrencia;
	}

	public void setRealizandoPagamentoJobRecorrencia(Boolean realizandoPagamentoJobRecorrencia) {
		this.realizandoPagamentoJobRecorrencia = realizandoPagamentoJobRecorrencia;
	}

	public Boolean getJobExecutadaManualmente() {
		if (jobExecutadaManualmente == null) {
			jobExecutadaManualmente = false;
		}
		return jobExecutadaManualmente;
	}

	public void setJobExecutadaManualmente(Boolean jobExecutadaManualmente) {
		this.jobExecutadaManualmente = jobExecutadaManualmente;
	}

	public CartaoCreditoDebitoRecorrenciaPessoaVO getCartaoCreditoDebitoRecorrenciaPessoaVO() {
		if (cartaoCreditoDebitoRecorrenciaPessoaVO == null) {
			cartaoCreditoDebitoRecorrenciaPessoaVO = new CartaoCreditoDebitoRecorrenciaPessoaVO();
		}
		return cartaoCreditoDebitoRecorrenciaPessoaVO;
	}

	public void setCartaoCreditoDebitoRecorrenciaPessoaVO(CartaoCreditoDebitoRecorrenciaPessoaVO cartaoCreditoDebitoRecorrenciaPessoaVO) {
		this.cartaoCreditoDebitoRecorrenciaPessoaVO = cartaoCreditoDebitoRecorrenciaPessoaVO;
	}

}
