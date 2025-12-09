package negocio.comuns.academico;

import negocio.comuns.academico.enumeradores.TipoTextoEnadeEnum;
import negocio.comuns.arquitetura.SuperVO;

public class TextoEnadeVO extends SuperVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 392981532314350268L;
	private Integer codigo; 
//	private EnadeVO enade;
	private TipoTextoEnadeEnum tipoTextoEnade;
	private String texto;
	
	//Transiente
	private String textoEdicao;
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
//	
	public TipoTextoEnadeEnum getTipoTextoEnade() {
		if (tipoTextoEnade == null) {
			tipoTextoEnade = TipoTextoEnadeEnum.REALIZACAO;
		}
		return tipoTextoEnade;
	}
	public void setTipoTextoEnade(TipoTextoEnadeEnum tipoTextoEnade) {
		this.tipoTextoEnade = tipoTextoEnade;
	}
	public String getTexto() {
		if (texto == null) {
			texto = "";
		}
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public String getTextoEdicao() {
		if (textoEdicao == null) {
			textoEdicao = getTexto();
		}
		return textoEdicao;
	}
	public void setTextoEdicao(String textoEdicao) {
		this.textoEdicao = textoEdicao;
	}
	
	

}
