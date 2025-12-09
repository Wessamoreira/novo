package negocio.comuns.protocolo;


public class PossivelResponsavelRequerimentoVO {

	private String unidadeEnsino;
	private String curso;	
	private String responsavel;
	private String origemResponsavel;

	public String getUnidadeEnsino() {
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public String getCurso() {
		if(curso == null){
			curso= "";
		}
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public String getOrigemResponsavel() {
		if(origemResponsavel == null){
			origemResponsavel = "";
		}
		return origemResponsavel;
	}

	public void setOrigemResponsavel(String origemResponsavel) {
		this.origemResponsavel = origemResponsavel;
	}
	
	

}
