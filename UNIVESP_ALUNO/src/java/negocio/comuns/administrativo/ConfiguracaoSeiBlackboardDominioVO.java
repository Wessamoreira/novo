package negocio.comuns.administrativo;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.blackboard.PermissaoBlackboardVO;
import negocio.comuns.blackboard.enumeradores.CourseRoleIdEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoPessoa;

public class ConfiguracaoSeiBlackboardDominioVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8436492714862203767L;
	private Integer codigo;
	private ConfiguracaoSeiBlackboardVO configuracaoSeiBlackboardVO;
	private CourseRoleIdEnum tipoUsuarioBlackboard;
//	private TipoPessoa tipoPessoaEnum;
//	private String dominioEmail;
	private String permissaoSistema;
	private String roleIdSistema;
	private String permissaoInstitucional;
	private String roleIdInstitucional;
	/**
	 * Transient
	 */
	private List<PermissaoBlackboardVO> listaTempPermissaoBlackboardVO;
	private String listaMatriculaString;
	private boolean tipoPermissaoSistema = false;

	public void realizarSeparacaoListaTempPermissaoBlackboardVO() {
		StringBuilder sbPermissao = new StringBuilder("");
		StringBuilder sbRole = new StringBuilder("");
		if (getListaTempPermissaoBlackboardVO().size() == 1) {
			sbPermissao.append(getListaTempPermissaoBlackboardVO().get(0).getName());
			sbRole.append(getListaTempPermissaoBlackboardVO().get(0).getRoleId());
		} else if (getListaTempPermissaoBlackboardVO().size() > 1) {
			getListaTempPermissaoBlackboardVO().stream().forEach(p -> {
				sbPermissao.append(p.getName()).append(";");
				sbRole.append(p.getRoleId()).append(";");
			});
		}
		if (isTipoPermissaoSistema()) {
			setPermissaoSistema(sbPermissao.toString());
			setRoleIdSistema(sbRole.toString());
		} else {
			setPermissaoInstitucional(sbPermissao.toString());
			setRolerIdInstitucional(sbRole.toString());
		}
	}

	public void realizarMontagemListaListaTempPermissaoBlackboardVO() {
		getListaTempPermissaoBlackboardVO().clear();
		String[] arrayPermissao = isTipoPermissaoSistema() ? getPermissaoSistema().split(";") : getPermissaoInstitucional().split(";");
		String[] arrayRole = isTipoPermissaoSistema() ? getRoleIdSistema().split(";") : getRoleIdInstitucional().split(";");
		for (int i = 0; i < arrayPermissao.length; i++) {
			if (arrayPermissao[i] != null && !arrayPermissao[i].trim().isEmpty()) {
				PermissaoBlackboardVO p = new PermissaoBlackboardVO();
				p.setName(arrayPermissao[i]);
				p.setRoleId(arrayRole[i]);
				getListaTempPermissaoBlackboardVO().add(p);
			}
		}
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

	public ConfiguracaoSeiBlackboardVO getConfiguracaoSeiBlackboardVO() {
		if (configuracaoSeiBlackboardVO == null) {
			configuracaoSeiBlackboardVO = new ConfiguracaoSeiBlackboardVO();
		}
		return configuracaoSeiBlackboardVO;
	}

	public void setConfiguracaoSeiBlackboardVO(ConfiguracaoSeiBlackboardVO configuracaoSeiBlackboardVO) {
		this.configuracaoSeiBlackboardVO = configuracaoSeiBlackboardVO;
	}


	public String getRoleIdSistema() {
		if (roleIdSistema == null) {
			roleIdSistema = "";
		}
		return roleIdSistema;
	}

	public void setRoleIdSistema(String rolerIdSistema) {
		this.roleIdSistema = rolerIdSistema;
	}

	public String getRoleIdInstitucional() {
		if (roleIdInstitucional == null) {
			roleIdInstitucional = "";
		}
		return roleIdInstitucional;
	}

	public void setRolerIdInstitucional(String roleIdInstitucional) {
		this.roleIdInstitucional = roleIdInstitucional;
	}

	public String getPermissaoSistema() {
		if (permissaoSistema == null) {
			permissaoSistema = "";
		}
		return permissaoSistema;
	}

	public void setPermissaoSistema(String permissaoSistema) {
		this.permissaoSistema = permissaoSistema;
	}

	public String getPermissaoInstitucional() {
		if (permissaoInstitucional == null) {
			permissaoInstitucional = "";
		}
		return permissaoInstitucional;
	}

	public void setPermissaoInstitucional(String permissaoInstitucional) {
		this.permissaoInstitucional = permissaoInstitucional;
	}

	public boolean isTipoPermissaoSistema() {
		return tipoPermissaoSistema;
	}

	public void setTipoPermissaoSistema(boolean tipoPermissaoSistema) {
		this.tipoPermissaoSistema = tipoPermissaoSistema;
	}

	public String getListaMatriculaString() {
		if (listaMatriculaString == null) {
			listaMatriculaString = "";
		}
		return listaMatriculaString;
	}

	public void setListaMatriculaString(String listaMatriculaString) {
		this.listaMatriculaString = listaMatriculaString;
	}

	public List<PermissaoBlackboardVO> getListaTempPermissaoBlackboardVO() {
		if (listaTempPermissaoBlackboardVO == null) {
			listaTempPermissaoBlackboardVO = new ArrayList<>();
		}
		return listaTempPermissaoBlackboardVO;
	}

	public void setListaTempPermissaoBlackboardVO(List<PermissaoBlackboardVO> listaTempPermissaoBlackboardVO) {
		this.listaTempPermissaoBlackboardVO = listaTempPermissaoBlackboardVO;
	}
	
	public CourseRoleIdEnum getTipoUsuarioBlackboard() {
		if(tipoUsuarioBlackboard == null) {
			tipoUsuarioBlackboard = CourseRoleIdEnum.Student;
		}
		return tipoUsuarioBlackboard;
	}

	public void setTipoUsuarioBlackboard(CourseRoleIdEnum tipoUsuarioBlackboard) {
		this.tipoUsuarioBlackboard = tipoUsuarioBlackboard;
	}

	public boolean equalsCampoSelecaoLista(ConfiguracaoSeiBlackboardDominioVO obj) {
		return Uteis.isAtributoPreenchido(getTipoUsuarioBlackboard()) && Uteis.isAtributoPreenchido(obj.getTipoUsuarioBlackboard()) && getTipoUsuarioBlackboard().equals(obj.getTipoUsuarioBlackboard());

	}

}
