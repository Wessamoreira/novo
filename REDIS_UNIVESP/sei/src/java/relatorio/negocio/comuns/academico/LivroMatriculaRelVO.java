package relatorio.negocio.comuns.academico;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.basico.PessoaVO;

public class LivroMatriculaRelVO {

	private MatriculaPeriodoVO matriculaPeriodo;
	private PessoaVO pessoaVO;
	private String situacaoMatricula;

	
	public MatriculaPeriodoVO getMatriculaPeriodo() {
		if(matriculaPeriodo == null){
			matriculaPeriodo = new MatriculaPeriodoVO();
		}
		return matriculaPeriodo;
	}

	public void setMatriculaPeriodo(MatriculaPeriodoVO matriculaPeriodo) {
		this.matriculaPeriodo = matriculaPeriodo;
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

	public String getSituacaoMatricula() {
		if (situacaoMatricula == null) {
			situacaoMatricula = "";
		}
		return situacaoMatricula;
	}

	public void setSituacaoMatricula(String situacaoMatricula) {
		this.situacaoMatricula = situacaoMatricula;
	}

}
