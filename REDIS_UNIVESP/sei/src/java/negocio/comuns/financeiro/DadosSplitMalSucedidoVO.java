package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;

public class DadosSplitMalSucedidoVO extends SuperVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String finacialMovementKey;
	private Date dataTentativa;
	private String descricaoErro;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getFinacialMovementKey() {
		if(finacialMovementKey == null) {
			finacialMovementKey = "";
		}
		return finacialMovementKey;
	}

	public void setFinacialMovementKey(String finacialMovementKey) {
		this.finacialMovementKey = finacialMovementKey;
	}

	public Date getDataTentativa() {
		if(dataTentativa == null) {
			dataTentativa = new Date();
		}
		return dataTentativa;
	}

	public void setDataTentativa(Date dataTentativa) {
		this.dataTentativa = dataTentativa;
	}

	public String getDescricaoErro() {
		if(descricaoErro == null) {
			descricaoErro = "";
		}
		return descricaoErro;
	}

	public void setDescricaoErro(String descricaoErro) {
		this.descricaoErro = descricaoErro;
	}
}