package negocio.comuns.elastic;

import java.io.Serializable;

public class IndiceVersaoElasticVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String numero;
	private IndiceElasticVO indice;
	private Boolean ativo;
	private Integer registros;
	
	public String getNumero() {
		if (numero == null) {
			numero = "";
		}
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public IndiceElasticVO getIndice() {
		if (indice == null) {
			indice = new IndiceElasticVO();
		}
		return indice;
	}
	
	public void setIndice(IndiceElasticVO indice) {
		this.indice = indice;
	}
	
	public Boolean getAtivo() {
		if (ativo == null) {
			ativo = false;
		}
		return ativo;
	}
	
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Integer getRegistros() {
		if (registros == null) {
			registros = 0;
		}
		return registros;
	}

	public void setRegistros(Integer registros) {
		this.registros = registros;
	}
	
}
