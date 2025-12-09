package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;

/**
 * @author Wellington - 2 de fev de 2016
 *
 */
public class RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO;
	private GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO;

	public RenovacaoMatriculaTurmaVO getRenovacaoMatriculaTurmaVO() {
		if (renovacaoMatriculaTurmaVO == null) {
			renovacaoMatriculaTurmaVO = new RenovacaoMatriculaTurmaVO();
		}
		return renovacaoMatriculaTurmaVO;
	}

	public void setRenovacaoMatriculaTurmaVO(RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO) {
		this.renovacaoMatriculaTurmaVO = renovacaoMatriculaTurmaVO;
	}

	public GradeDisciplinaCompostaVO getGradeDisciplinaCompostaVO() {
		if (gradeDisciplinaCompostaVO == null) {
			gradeDisciplinaCompostaVO = new GradeDisciplinaCompostaVO();
		}
		return gradeDisciplinaCompostaVO;
	}

	public void setGradeDisciplinaCompostaVO(GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO) {
		this.gradeDisciplinaCompostaVO = gradeDisciplinaCompostaVO;
	}

}
