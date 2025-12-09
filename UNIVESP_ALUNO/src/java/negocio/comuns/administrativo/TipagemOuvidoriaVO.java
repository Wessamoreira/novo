package negocio.comuns.administrativo;

import negocio.comuns.administrativo.enumeradores.TipoOuvidoriaEnum;
import negocio.comuns.arquitetura.SuperVO;


/**
 * 
 * @author Pedro
 */
public class TipagemOuvidoriaVO  extends SuperVO {
	
	private Integer codigo;
	private String descricao;
	private TipoOuvidoriaEnum tipoOuvidoriaEnum;
	
	
	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getDescricao() {
		if(codigo == null){
			codigo = 0;
		}
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public TipoOuvidoriaEnum getTipoOuvidoriaEnum() {
		if(tipoOuvidoriaEnum == null){
			tipoOuvidoriaEnum = TipoOuvidoriaEnum.DUVIDA;
		}
		return tipoOuvidoriaEnum;
	}
	public void setTipoOuvidoriaEnum(TipoOuvidoriaEnum tipoOuvidoriaEnum) {
		this.tipoOuvidoriaEnum = tipoOuvidoriaEnum;
	}
	
	

}
