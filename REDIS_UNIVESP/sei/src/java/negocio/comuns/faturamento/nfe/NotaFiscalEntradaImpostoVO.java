package negocio.comuns.faturamento.nfe;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author Pedro Otimize
 *
 */
public class NotaFiscalEntradaImpostoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4286320807964544392L;
	private Integer codigo;
	private NotaFiscalEntradaVO notaFiscalEntradaVO;
	private ImpostoVO impostoVO;
	private boolean retido = false;
	private Double porcentagem;
	private Double valor;
	
	
	
	public void calcularPorcentagemNotaFiscalEntradaImposto() {
		if(Uteis.isAtributoPreenchido(getNotaFiscalEntradaVO().getTotalNotaEntrada())){
			setPorcentagem((100 * getValor())/getNotaFiscalEntradaVO().getTotalNotaEntrada());	
		}
	}
	public void calcularValorNotaFiscalEntradaImposto() {
		setValor((getNotaFiscalEntradaVO().getTotalNotaEntrada() * getPorcentagem())/100);
		
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public NotaFiscalEntradaVO getNotaFiscalEntradaVO() {
		if (notaFiscalEntradaVO == null) {
			notaFiscalEntradaVO = new NotaFiscalEntradaVO();
		}
		return notaFiscalEntradaVO;
	}

	public void setNotaFiscalEntradaVO(NotaFiscalEntradaVO notaFiscalEntradaVO) {
		this.notaFiscalEntradaVO = notaFiscalEntradaVO;
	}

	public ImpostoVO getImpostoVO() {
		if (impostoVO == null) {
			impostoVO = new ImpostoVO();
		}
		return impostoVO;
	}

	public void setImpostoVO(ImpostoVO impostoVO) {
		this.impostoVO = impostoVO;
	}

	public boolean isRetido() {
		return retido;
	}

	public void setRetido(boolean retido) {
		this.retido = retido;
	}

	public Double getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(Double porcentagem) {
		this.porcentagem = porcentagem;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}
	
	public boolean equalsCampoSelecaoLista(NotaFiscalEntradaImpostoVO obj){
		return Uteis.isAtributoPreenchido(getImpostoVO()) && Uteis.isAtributoPreenchido(obj.getImpostoVO()) && getImpostoVO().getCodigo().equals(obj.getImpostoVO().getCodigo());
		
	}

}
