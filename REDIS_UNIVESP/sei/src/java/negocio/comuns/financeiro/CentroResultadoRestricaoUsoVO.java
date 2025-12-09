package negocio.comuns.financeiro;

import java.util.Optional;

import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

public class CentroResultadoRestricaoUsoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2290990579492882914L;
	private Integer codigo;
	private CentroResultadoVO centroResultadoVO;
	private UsuarioVO usuarioVO;
	private PerfilAcessoVO perfilAcessoVO;

	public Integer getCodigo() {
		codigo = Optional.ofNullable(codigo).orElse(0);
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public CentroResultadoVO getCentroResultadoVO() {
		centroResultadoVO = Optional.ofNullable(centroResultadoVO).orElse(new CentroResultadoVO());
		return centroResultadoVO;
	}

	public void setCentroResultadoVO(CentroResultadoVO centroResultadoVO) {
		this.centroResultadoVO = centroResultadoVO;
	}

	public UsuarioVO getUsuarioVO() {
		usuarioVO = Optional.ofNullable(usuarioVO).orElse(new UsuarioVO());
		return usuarioVO;
	}

	public void setUsuarioVO(UsuarioVO usuarioVO) {
		this.usuarioVO = usuarioVO;
	}

	public PerfilAcessoVO getPerfilAcessoVO() {
		perfilAcessoVO = Optional.ofNullable(perfilAcessoVO).orElse(new PerfilAcessoVO());
		return perfilAcessoVO;
	}

	public void setPerfilAcessoVO(PerfilAcessoVO perfilAcessoVO) {
		this.perfilAcessoVO = perfilAcessoVO;
	}
	
	public boolean equalsCampoSelecaoLista(CentroResultadoRestricaoUsoVO obj){
		return (Uteis.isAtributoPreenchido(getUsuarioVO()) && Uteis.isAtributoPreenchido(obj.getUsuarioVO()) && getUsuarioVO().getCodigo().equals(obj.getUsuarioVO().getCodigo()))
				|| Uteis.isAtributoPreenchido(getPerfilAcessoVO()) && Uteis.isAtributoPreenchido(obj.getPerfilAcessoVO()) && getPerfilAcessoVO().getCodigo().equals(obj.getPerfilAcessoVO().getCodigo());
		
	}

}
