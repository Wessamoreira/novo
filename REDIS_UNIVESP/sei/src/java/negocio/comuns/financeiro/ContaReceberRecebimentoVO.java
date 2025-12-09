package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaServicoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade CrRLog. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ContaReceberRecebimentoVO extends SuperVO {

	private Integer codigo;
	private FormaPagamentoVO formaPagamento;
	private Integer contaReceber;
	private Integer formaPagamentoNegociacaoRecebimento;
	private Double valorRecebimento;
	private Integer negociacaoRecebimento;
	private String tipoRecebimento;
	private Date dataRecebimeto;
	private UsuarioVO responsavel;
	private String motivo;
	private ContaReceberVO contaReceberVO;
	private NegociacaoRecebimentoVO negociacaoRecebimentoVO;
	private Boolean recebimentoTerceirizado;
	public static final long serialVersionUID = 1L;

	public NotaFiscalSaidaServicoVO notaFiscalSaidaServicoVO;
	
	/**
	 * transietn
	 */
	private FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO;

	/**
	 * Construtor padrão da classe <code>CrRLog</code>. Cria uma nova instância
	 * desta entidade, inicializando automaticamente seus atributos (Classe VO).
	 */
	public ContaReceberRecebimentoVO() {
		super();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>CrRLogVO</code>. Todos os tipos de consistência de dados são e
	 * devem ser implementadas neste método. São validações típicas: verificação
	 * de campos obrigatórios, verificação de valores válidos para os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(ContaReceberRecebimentoVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if ((obj.getFormaPagamento() == null) || (obj.getFormaPagamento().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo FORMA DE PAGAMENTO (Conta Receber) deve ser informado.");
		}

	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo
	 * String.
	 */
	public void realizarUpperCaseDados() {
	}

	public String getTipoRecebimento_Apresentar() {
		if (tipoRecebimento.equals("CR")) {
			return "Crédito";
		}
		if (tipoRecebimento.equals("DE")) {
			return "Débito";
		}
		if (tipoRecebimento.equals("ES")) {
			return "Estorno";
		}
		return tipoRecebimento;
	}

	public Boolean getApresentarMotivo() {
		if (getMotivo().equals("")) {
			return false;
		}
		return true;
	}

	public Integer getContaReceber() {
		if (contaReceber == null) {
			contaReceber = 0;
		}
		return contaReceber;
	}

	public void setContaReceber(Integer contaReceber) {
		this.contaReceber = contaReceber;
	}

	public String getDataRecebimeto_Apresentar() {
		return Uteis.getDataComHora(dataRecebimeto);
	}

	public Date getDataRecebimeto() {
		if (dataRecebimeto == null) {
			dataRecebimeto = new Date();
		}
		return dataRecebimeto;
	}

	public void setDataRecebimeto(Date dataRecebimeto) {
		this.dataRecebimeto = dataRecebimeto;
	}

	public Integer getFormaPagamentoNegociacaoRecebimento() {
		if (formaPagamentoNegociacaoRecebimento == null) {
			formaPagamentoNegociacaoRecebimento = 0;
		}
		return formaPagamentoNegociacaoRecebimento;
	}

	public void setFormaPagamentoNegociacaoRecebimento(Integer formaPagamentoNegociacaoRecebimento) {
		this.formaPagamentoNegociacaoRecebimento = formaPagamentoNegociacaoRecebimento;
	}

	public String getMotivo() {
		if (motivo == null) {
			motivo = "";
		}
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Integer getNegociacaoRecebimento() {
		if (negociacaoRecebimento == null) {
			negociacaoRecebimento = 0;
		}
		return negociacaoRecebimento;
	}

	public void setNegociacaoRecebimento(Integer negociacaoRecebimento) {
		this.negociacaoRecebimento = negociacaoRecebimento;
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

	public String getTipoRecebimento() {
		if (tipoRecebimento == null) {
			tipoRecebimento = "";
		}
		return tipoRecebimento;
	}

	public void setTipoRecebimento(String tipoRecebimento) {
		this.tipoRecebimento = tipoRecebimento;
	}

	public Double getValorRecebimento() {
		if (valorRecebimento == null) {
			valorRecebimento = 0.0;
		}
		return valorRecebimento;
	}

	public void setValorRecebimento(Double valorRecebimento) {
		this.valorRecebimento = valorRecebimento;
	}

	/**
	 * Retorna o objeto da classe <code>FormaPagamento</code> relacionado com (
	 * <code>CrRLog</code>).
	 */
	public FormaPagamentoVO getFormaPagamento() {
		if (formaPagamento == null) {
			formaPagamento = new FormaPagamentoVO();
		}
		return (formaPagamento);
	}

	/**
	 * Define o objeto da classe <code>FormaPagamento</code> relacionado com (
	 * <code>CrRLog</code>).
	 */
	public void setFormaPagamento(FormaPagamentoVO obj) {
		this.formaPagamento = obj;
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

	public ContaReceberVO getContaReceberVO() {
		if (contaReceberVO == null) {
			contaReceberVO = new ContaReceberVO();
		}
		return contaReceberVO;
	}

	public void setContaReceberVO(ContaReceberVO contaReceberVO) {
		this.contaReceberVO = contaReceberVO;
	}

	public NegociacaoRecebimentoVO getNegociacaoRecebimentoVO() {
		if (negociacaoRecebimentoVO == null) {
			negociacaoRecebimentoVO = new NegociacaoRecebimentoVO();
		}
		return negociacaoRecebimentoVO;
	}

	public void setNegociacaoRecebimentoVO(NegociacaoRecebimentoVO negociacaoRecebimentoVO) {
		this.negociacaoRecebimentoVO = negociacaoRecebimentoVO;
	}

	public Boolean getRecebimentoTerceirizado() {
		if (recebimentoTerceirizado == null) {
			recebimentoTerceirizado = Boolean.FALSE;
		}
		return recebimentoTerceirizado;
	}

	public void setRecebimentoTerceirizado(Boolean recebimentoTerceirizado) {
		this.recebimentoTerceirizado = recebimentoTerceirizado;
	}
	
	public String getRecebimentoTerceirizado_Apresentar() {
		if (getRecebimentoTerceirizado()) {
			return "SIM";
		}
		return "NÃO";
	}
	
	/**
	 * @author Victor Hugo de Paula Costa 5.0.4.0 07/04/2016
	 */
	private List<TransacaoCartaoOnlineVO> transacaoCartaoOnlineVOs;

	public List<TransacaoCartaoOnlineVO> getTransacaoCartaoOnlineVOs() {
		if(transacaoCartaoOnlineVOs == null) {
			transacaoCartaoOnlineVOs = new ArrayList<TransacaoCartaoOnlineVO>();
		}
		return transacaoCartaoOnlineVOs;
	}

	public void setTransacaoCartaoOnlineCreditoVOs(List<TransacaoCartaoOnlineVO> transacaoCartaoOnlineVOs) {
		this.transacaoCartaoOnlineVOs = transacaoCartaoOnlineVOs;
	}
	
	public NotaFiscalSaidaServicoVO getNotaFiscalSaidaServicoVO() {
		if (notaFiscalSaidaServicoVO == null) {
			notaFiscalSaidaServicoVO = new NotaFiscalSaidaServicoVO();
		}
		return notaFiscalSaidaServicoVO;
	}

	public void setNotaFiscalSaidaServicoVO(NotaFiscalSaidaServicoVO notaFiscalSaidaServicoVO) {
		this.notaFiscalSaidaServicoVO = notaFiscalSaidaServicoVO;
	}
	
	public FormaPagamentoNegociacaoRecebimentoVO getFormaPagamentoNegociacaoRecebimentoVO() {
		if(formaPagamentoNegociacaoRecebimentoVO == null){
			formaPagamentoNegociacaoRecebimentoVO= new FormaPagamentoNegociacaoRecebimentoVO();
		}
		return formaPagamentoNegociacaoRecebimentoVO;
	}

	public void setFormaPagamentoNegociacaoRecebimentoVO(FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO) {
		this.formaPagamentoNegociacaoRecebimentoVO = formaPagamentoNegociacaoRecebimentoVO;
	}

}
