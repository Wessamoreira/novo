package negocio.comuns.protocolo;

import negocio.comuns.arquitetura.SuperVO;

public class SituacaoRequerimentoDepartamentoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7094200587623669418L;
	private Integer codigo;
	private String situacao;
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	
	
	
}
