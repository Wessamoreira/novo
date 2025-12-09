package webservice.servicos;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bandeira")
public class BandeiraRSVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4998187572643161135L;
	private Integer codigo;
	private String bandeira;
	private String $$hashKey;
	private Double valorMinimoRecebimento;
	private String nomeCartao ;
	private String bandeiraEnum;

	@XmlElement(name = "codigo", required= true)	
	public Integer getCodigo() {
		if(codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	@XmlElement(name = "bandeira")
	public String getBandeira() {
		return bandeira;
	}

	public void setBandeira(String bandeira) {
		this.bandeira = bandeira;
	}

	@XmlElement(name = "valorMinimoRecebimento")
	public Double getValorMinimoRecebimento() {
		if(valorMinimoRecebimento == null) {
			valorMinimoRecebimento = 0.0;
		}
		return valorMinimoRecebimento;
	}

	public void setValorMinimoRecebimento(Double valorMinimoRecebimento) {
		this.valorMinimoRecebimento = valorMinimoRecebimento;
	}

	@XmlElement(name = "$$hashKey")
	public String get$$hashKey() {
		if ($$hashKey == null) {
			$$hashKey = getCodigo().toString();
		}
		return $$hashKey;
	}

	public void set$$hashKey(String $$hashKey) {
		this.$$hashKey = $$hashKey;
	}

	@XmlElement(name = "nomeCartao")
	public String getNomeCartao() {
		return nomeCartao;
	}

	public void setNomeCartao(String nomeCartao) {
		this.nomeCartao = nomeCartao;
	}

	@XmlElement(name = "bandeiraEnum")
	public String getBandeiraEnum() {
		if (bandeiraEnum == null) {
			bandeiraEnum = "";
		}
		return bandeiraEnum;
	}

	public void setBandeiraEnum(String bandeiraEnum) {
		this.bandeiraEnum = bandeiraEnum;
	}
	
	
	
	
}
