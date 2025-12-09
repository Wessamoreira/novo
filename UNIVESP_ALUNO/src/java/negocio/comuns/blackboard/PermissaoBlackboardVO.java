package negocio.comuns.blackboard;

public class PermissaoBlackboardVO {

	private String id;
	private String roleId;
	private String name;
	private String description;
	private String custom;
	private Boolean selecionado;

	public String getId() {		
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleId() {		
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getName() {		
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {		
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCustom() {		
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}

	public Boolean getSelecionado() {
		if(selecionado == null) {
			selecionado = false;
		}
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}
	
	

}
