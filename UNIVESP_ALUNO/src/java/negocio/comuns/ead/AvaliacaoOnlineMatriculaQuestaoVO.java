package negocio.comuns.ead;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.ead.enumeradores.SituacaoAtividadeRespostaEnum;

/**
 * @author Victor Hugo 10/10/2014
 */
public class AvaliacaoOnlineMatriculaQuestaoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO;
	private QuestaoVO questaoVO;
	private Integer ordemApresentacao;
	private SituacaoAtividadeRespostaEnum situacaoAtividadeRespostaEnum;
	private List<AvaliacaoOnlineMatriculaRespostaQuestaoVO> avaliacaoOnlineMatriculaRespostaQuestaoVOs;
	/*
	 * Transient
	 */
	private Integer acertouTurma;
	private Integer errouTurma;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public AvaliacaoOnlineMatriculaVO getAvaliacaoOnlineMatriculaVO() {
		if (avaliacaoOnlineMatriculaVO == null) {
			avaliacaoOnlineMatriculaVO = new AvaliacaoOnlineMatriculaVO();
		}
		return avaliacaoOnlineMatriculaVO;
	}

	public void setAvaliacaoOnlineMatriculaVO(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO) {
		this.avaliacaoOnlineMatriculaVO = avaliacaoOnlineMatriculaVO;
	}

	public QuestaoVO getQuestaoVO() {
		if (questaoVO == null) {
			questaoVO = new QuestaoVO();
		}
		return questaoVO;
	}

	public void setQuestaoVO(QuestaoVO questaoVO) {
		this.questaoVO = questaoVO;
	}

	public Integer getOrdemApresentacao() {
		if (ordemApresentacao == null) {
			ordemApresentacao = 0;
		}
		return ordemApresentacao;
	}

	public void setOrdemApresentacao(Integer ordemApresentacao) {
		this.ordemApresentacao = ordemApresentacao;
	}

	public SituacaoAtividadeRespostaEnum getSituacaoAtividadeRespostaEnum() {
		if (situacaoAtividadeRespostaEnum == null) {
			situacaoAtividadeRespostaEnum = SituacaoAtividadeRespostaEnum.NAO_RESPONDIDA;
		}
		return situacaoAtividadeRespostaEnum;
	}

	public void setSituacaoAtividadeRespostaEnum(SituacaoAtividadeRespostaEnum situacaoAtividadeRespostaEnum) {
		this.situacaoAtividadeRespostaEnum = situacaoAtividadeRespostaEnum;
	}

	public List<AvaliacaoOnlineMatriculaRespostaQuestaoVO> getAvaliacaoOnlineMatriculaRespostaQuestaoVOs() {
		if (avaliacaoOnlineMatriculaRespostaQuestaoVOs == null) {
			avaliacaoOnlineMatriculaRespostaQuestaoVOs = new ArrayList<AvaliacaoOnlineMatriculaRespostaQuestaoVO>(0);
		}
		return avaliacaoOnlineMatriculaRespostaQuestaoVOs;
	}

	public void setAvaliacaoOnlineMatriculaRespostaQuestaoVOs(
			List<AvaliacaoOnlineMatriculaRespostaQuestaoVO> avaliacaoOnlineMatriculaRespostaQuestaoVOs) {
		this.avaliacaoOnlineMatriculaRespostaQuestaoVOs = avaliacaoOnlineMatriculaRespostaQuestaoVOs;
	}

	public Boolean getIsQuestaoEstaRepondida() {
		for (AvaliacaoOnlineMatriculaRespostaQuestaoVO resposta : getAvaliacaoOnlineMatriculaRespostaQuestaoVOs()) {
			if (resposta.getMarcada()) {
				return true;
			}
		}
		return false;
	}

	public Integer getAcertouTurma() {
		if (acertouTurma == null) {
			acertouTurma = 0;
		}
		return acertouTurma;
	}

	public void setAcertouTurma(Integer acertouTurma) {
		this.acertouTurma = acertouTurma;
	}

	public Integer getErrouTurma() {
		if (errouTurma == null) {
			errouTurma = 0;
		}
		return errouTurma;
	}

	public void setErrouTurma(Integer errouTurma) {
		this.errouTurma = errouTurma;
	}

	public String getCssPergunta() {
		if (getAvaliacaoOnlineMatriculaRespostaQuestaoVOs().size() <= 2) {
			return "col-md-6";
		} else if (getAvaliacaoOnlineMatriculaRespostaQuestaoVOs().size() == 3) {
			return "col-md-4";
		}
		return "col-md-3";
	}
}
