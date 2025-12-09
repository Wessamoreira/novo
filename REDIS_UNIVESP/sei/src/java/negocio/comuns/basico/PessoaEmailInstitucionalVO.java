package negocio.comuns.basico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;

public class PessoaEmailInstitucionalVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1345235049316117127L;
	private Integer codigo;
	private PessoaVO pessoaVO;
	private String email;
	private String nome;
	private StatusAtivoInativoEnum statusAtivoInativoEnum;	
	@ExcluirJsonAnnotation
	private boolean pessoaEmailInstitucional = false;
	@ExcluirJsonAnnotation
	private boolean executarStatusAtivoInativoEnum = false;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public PessoaVO getPessoaVO() {
		if (pessoaVO == null) {
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	public String getEmail() {
		if (email == null) {
			email = "";
		}
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public StatusAtivoInativoEnum getStatusAtivoInativoEnum() {
		if (statusAtivoInativoEnum == null) {
			statusAtivoInativoEnum = StatusAtivoInativoEnum.NENHUM;
		}
		return statusAtivoInativoEnum;
	}
	
	public Boolean getApresentarBotaoAtivar() {
		if (getStatusAtivoInativoEnum().equals(StatusAtivoInativoEnum.ATIVO)) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	public void setStatusAtivoInativoEnum(StatusAtivoInativoEnum statusAtivoInativoEnum) {
		this.statusAtivoInativoEnum = statusAtivoInativoEnum;
	}

	public boolean equalsCampoSelecaoLista(PessoaEmailInstitucionalVO obj) {
		return getPessoaVO().getCodigo().equals(obj.getPessoaVO().getCodigo()) && getEmail().equals(obj.getEmail());

	}	

	public boolean isPessoaEmailInstitucional() {
		return pessoaEmailInstitucional;
	}

	public void setPessoaEmailInstitucional(boolean pessoaEmailInstitucional) {
		this.pessoaEmailInstitucional = pessoaEmailInstitucional;
	}

	public String getPessoaEmailInstitucionalTexto() {
		return isPessoaEmailInstitucional() ? "Sim":"Não";
	}

	public String getNome() {
		if(nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isExecutarStatusAtivoInativoEnum() {
		return executarStatusAtivoInativoEnum;
	}

	public void setExecutarStatusAtivoInativoEnum(boolean executarStatusAtivoInativoEnum) {
		this.executarStatusAtivoInativoEnum = executarStatusAtivoInativoEnum;
	}
	
	
	
	

}
