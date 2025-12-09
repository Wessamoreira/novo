package negocio.comuns.financeiro;

import negocio.comuns.arquitetura.SuperVO;

public class RegistroTrailerLotePagarVO extends SuperVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6215106057999904097L;
	private Integer codigo;
	private String codigoBanco;
	private String loteServico;
	private Integer tipoRegistro;
	private Integer quantidadeRegistroLote;
	private Double somatoraRegistroLote;
	private String numeroAvisoDebito;
	private RegistroHeaderLotePagarVO registroHeaderLotePagarVO;
	
	

	public RegistroHeaderLotePagarVO getRegistroHeaderLotePagarVO() {
		if (registroHeaderLotePagarVO == null) {
			registroHeaderLotePagarVO = new RegistroHeaderLotePagarVO();
		}
		return registroHeaderLotePagarVO;
	}

	public void setRegistroHeaderLotePagarVO(RegistroHeaderLotePagarVO registroHeaderLotePagarVO) {
		this.registroHeaderLotePagarVO = registroHeaderLotePagarVO;
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

	public String getCodigoBanco() {
		if (codigoBanco == null) {
			codigoBanco = "";
		}
		return codigoBanco;
	}

	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = codigoBanco;
	}

	public String getLoteServico() {
		if (loteServico == null) {
			loteServico = "";
		}
		return loteServico;
	}

	public void setLoteServico(String loteServico) {
		this.loteServico = loteServico;
	}

	public Integer getTipoRegistro() {
		if (tipoRegistro == null) {
			tipoRegistro = 0;
		}
		return tipoRegistro;
	}

	public void setTipoRegistro(Integer tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public Integer getQuantidadeRegistroLote() {
		if (quantidadeRegistroLote == null) {
			quantidadeRegistroLote = 0;
		}
		return quantidadeRegistroLote;
	}

	public void setQuantidadeRegistroLote(Integer quantidadeRegistroLote) {
		this.quantidadeRegistroLote = quantidadeRegistroLote;
	}

	public Double getSomatoraRegistroLote() {
		if (somatoraRegistroLote == null) {
			somatoraRegistroLote = 0.0;
		}
		return somatoraRegistroLote;
	}

	public void setSomatoraRegistroLote(Double somatoraRegistroLote) {
		this.somatoraRegistroLote = somatoraRegistroLote;
	}

	public String getNumeroAvisoDebito() {
		if (numeroAvisoDebito == null) {
			numeroAvisoDebito = "";
		}
		return numeroAvisoDebito;
	}

	public void setNumeroAvisoDebito(String numeroAvisoDebito) {
		this.numeroAvisoDebito = numeroAvisoDebito;
	}

}
