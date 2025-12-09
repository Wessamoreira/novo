package negocio.comuns.biblioteca;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.biblioteca.enumeradores.FormatoImpressaoEnum;

public class PoolImpressaoVO extends SuperVO  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4806960883340305894L;
	private Integer codigo;
	private String imprimir;
	private FormatoImpressaoEnum formatoImpressao;
	private Date data;
	private Boolean erroImpressao;
	private String motivoErroImpressao;
	private ImpressoraVO impressoraVO;
	
	public String getImprimir() {
		if(imprimir == null){
			imprimir = "";
		}
		return imprimir;
	}
	
	public void setImprimir(String imprimir) {
		this.imprimir = imprimir;
	}
	
	public FormatoImpressaoEnum getFormatoImpressao() {
		if(formatoImpressao == null){
			formatoImpressao = FormatoImpressaoEnum.TEXTO;
		}
		return formatoImpressao;
	}
	
	public void setFormatoImpressao(FormatoImpressaoEnum formatoImpressao) {
		this.formatoImpressao = formatoImpressao;
	}
	
	public Date getData() {
		if(data == null){
			data = new Date();
		}
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}

	public Boolean getErroImpressao() {
		if(erroImpressao == null){
			erroImpressao = false;
		}
		return erroImpressao;
	}

	public void setErroImpressao(Boolean erroImpressao) {
		this.erroImpressao = erroImpressao;
	}

	public String getMotivoErroImpressao() {
		if(motivoErroImpressao == null){
			motivoErroImpressao = "";
		}
		return motivoErroImpressao;
	}

	public void setMotivoErroImpressao(String motivoErroImpressao) {
		this.motivoErroImpressao = motivoErroImpressao;
	}

	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public ImpressoraVO getImpressoraVO() {
		if(impressoraVO == null){
			impressoraVO = new ImpressoraVO();
		}
		return impressoraVO;
	}

	public void setImpressoraVO(ImpressoraVO impressoraVO) {
		this.impressoraVO = impressoraVO;
	}
	
	
	
}
