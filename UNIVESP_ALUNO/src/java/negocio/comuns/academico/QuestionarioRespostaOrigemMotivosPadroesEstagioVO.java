package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.estagio.MotivosPadroesEstagioVO;

public class QuestionarioRespostaOrigemMotivosPadroesEstagioVO extends SuperVO implements Cloneable {

	private static final long serialVersionUID = -393578052852307253L;

	private QuestionarioRespostaOrigemVO questionarioRespostaOrigemVO;
	private MotivosPadroesEstagioVO motivosPadroesEstagioVO;
	private Integer codigo;
	private  Boolean selecionado;

	public QuestionarioRespostaOrigemVO getQuestionarioRespostaOrigemVO() {
		if (questionarioRespostaOrigemVO == null) {
			questionarioRespostaOrigemVO = new QuestionarioRespostaOrigemVO();
		}
		return questionarioRespostaOrigemVO;
	}

	public void setQuestionarioRespostaOrigemVO(QuestionarioRespostaOrigemVO questionarioRespostaOrigemVO) {
		this.questionarioRespostaOrigemVO = questionarioRespostaOrigemVO;
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

	public MotivosPadroesEstagioVO getMotivosPadroesEstagioVO() {
		if (motivosPadroesEstagioVO == null) {
			motivosPadroesEstagioVO = new MotivosPadroesEstagioVO();
		}
		return motivosPadroesEstagioVO;
	}

	public void setMotivosPadroesEstagioVO(MotivosPadroesEstagioVO motivosPadroesEstagioVO) {
		this.motivosPadroesEstagioVO = motivosPadroesEstagioVO;
	}
	
	
	public Boolean getSelecionado() {
	if(selecionado == null) {
		selecionado =  false;
	}
	return selecionado;
	}
	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}
	
}
