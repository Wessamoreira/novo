package negocio.comuns.gsuite;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;


public class PessoaGsuiteVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5823059257943369741L;
	private Integer codigo;
	private PessoaVO pessoaVO;
	//private UnidadeEnsinoVO unidadeEnsinoVO;
	private String email;
	private StatusAtivoInativoEnum statusAtivoInativoEnum;
	private String matricula;
	@ExcluirJsonAnnotation
	private boolean pessoaGsuiteClassroom = false;
	
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

	
//	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
//		if (unidadeEnsinoVO == null) {
//			unidadeEnsinoVO = new UnidadeEnsinoVO();
//		}
//		return unidadeEnsinoVO;
//	}
//
//	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
//		this.unidadeEnsinoVO = unidadeEnsinoVO;
//	}

	
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

	public void setStatusAtivoInativoEnum(StatusAtivoInativoEnum statusAtivoInativoEnum) {
		this.statusAtivoInativoEnum = statusAtivoInativoEnum;
	}
	
	public boolean equalsCampoSelecaoLista(PessoaGsuiteVO obj) {
		return getPessoaVO().getCodigo().equals(obj.getPessoaVO().getCodigo()) && getEmail().equals(obj.getEmail());
		
	}

	public String getMatricula() {
		if(matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public boolean isPessoaGsuiteClassroom() {
		return pessoaGsuiteClassroom;
	}

	public void setPessoaGsuiteClassroom(boolean pessoaGsuiteClassroom) {
		this.pessoaGsuiteClassroom = pessoaGsuiteClassroom;
	}
	
	public String getPessoaGuiteClassroom() {
		return isPessoaGsuiteClassroom() ? "Sim":"Não";
	}
	
	
	
	
	
	

}
