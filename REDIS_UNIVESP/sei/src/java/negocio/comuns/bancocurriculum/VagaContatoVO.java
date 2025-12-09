package negocio.comuns.bancocurriculum;

import negocio.comuns.arquitetura.SuperVO;

public class VagaContatoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9130066957682883349L;
	private Integer codigo;
	private String nome;
	private String email;
	private VagasVO vaga;
	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getNome() {
		if(nome == null){
			nome = "";
		}
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		if(email == null){
			email = "";
		}
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public VagasVO getVaga() {
		if(vaga == null){
			vaga = new VagasVO();
		}
		return vaga;
	}
	public void setVaga(VagasVO vaga) {
		this.vaga = vaga;
	}
	
	
	
	
	
}
