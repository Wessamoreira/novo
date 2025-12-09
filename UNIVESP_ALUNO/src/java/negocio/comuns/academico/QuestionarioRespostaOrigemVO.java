package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.enumeradores.SituacaoQuestionarioRespostaOrigemEnum;
import negocio.comuns.arquitetura.SuperVO;

import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.processosel.enumeradores.EscopoPerguntaEnum;
import negocio.comuns.utilitarias.Uteis;

public class QuestionarioRespostaOrigemVO extends SuperVO {

	private static final long serialVersionUID = -6623227807297653937L;

	private QuestionarioVO questionarioVO;
	private Integer codigo;
	private EscopoPerguntaEnum escopo;
	private PlanoEnsinoVO planoEnsinoVO;
//	private RequisicaoVO requisicaoVO;
	private EstagioVO estagioVO;
	private Integer nrVersao;
	private SituacaoQuestionarioRespostaOrigemEnum situacaoQuestionarioRespostaOrigemEnum;
	private Date dataEnvioAnalise;
	private Date dataLimiteAnalise;
	private Date dataEnvioCorrecao;
	private Date dataLimiteCorrecao;
	private List<PerguntaRespostaOrigemVO> perguntaRespostaOrigemVOs;
	private String observacaoFinal;
	private String motivo;
	private List<QuestionarioRespostaOrigemMotivosPadroesEstagioVO> questionarioRespostaOrigemMotivosPadroesEstagioVOs;	
	
	
	public QuestionarioRespostaOrigemVO getClonePorEstagio() {
		QuestionarioRespostaOrigemVO clone = new QuestionarioRespostaOrigemVO();
		clone = (QuestionarioRespostaOrigemVO) Uteis.clonar(this);
		clone.setCodigo(0);
		clone.setNrVersao(getNrVersao()+1);
		clone.setSituacaoQuestionarioRespostaOrigemEnum(SituacaoQuestionarioRespostaOrigemEnum.EM_PREENCHIMENTO);
		clone.getPerguntaRespostaOrigemVOs().clear();
		for (PerguntaRespostaOrigemVO proExistente : getPerguntaRespostaOrigemVOs()) {
			PerguntaRespostaOrigemVO proClone = proExistente.getClonePorEstagio();
			proClone.setCodigo(0);
			proClone.setQuestionarioRespostaOrigemVO(clone);
			clone.getPerguntaRespostaOrigemVOs().add(proClone);
		}
		return clone;
	}
	

	public PlanoEnsinoVO getPlanoEnsinoVO() {
		if (planoEnsinoVO == null) {
			planoEnsinoVO = new PlanoEnsinoVO();
		}
		return planoEnsinoVO;
	}

	public void setPlanoEnsinoVO(PlanoEnsinoVO planoEnsinoVO) {
		this.planoEnsinoVO = planoEnsinoVO;
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

	public QuestionarioVO getQuestionarioVO() {
		if (questionarioVO == null) {
			questionarioVO = new QuestionarioVO();
		}
		return questionarioVO;
	}

	public void setQuestionarioVO(QuestionarioVO questionarioVO) {
		this.questionarioVO = questionarioVO;
	}

	public EscopoPerguntaEnum getEscopo() {
		if (escopo == null) {
			escopo = EscopoPerguntaEnum.AVALIACAO_INSTITUCIONAL;
		}
		return escopo;
	}

	public void setEscopo(EscopoPerguntaEnum escopo) {
		this.escopo = escopo;
	}

	public List<PerguntaRespostaOrigemVO> getPerguntaRespostaOrigemVOs() {
		if (perguntaRespostaOrigemVOs == null) {
			perguntaRespostaOrigemVOs = new ArrayList<PerguntaRespostaOrigemVO>(0);
		}
		return perguntaRespostaOrigemVOs;
	}

	public void setPerguntaRespostaOrigemVOs(List<PerguntaRespostaOrigemVO> perguntaRespostaOrigemVOs) {
		this.perguntaRespostaOrigemVOs = perguntaRespostaOrigemVOs;
	}

//	public RequisicaoVO getRequisicaoVO() {
//		if (requisicaoVO == null) {
//			requisicaoVO = new RequisicaoVO();
//		}
//		return requisicaoVO;
//	}
//
//	public void setRequisicaoVO(RequisicaoVO requisicaoVO) {
//		this.requisicaoVO = requisicaoVO;
//	}

	public EstagioVO getEstagioVO() {
		if (estagioVO == null) {
			estagioVO = new EstagioVO();
		}
		return estagioVO;
	}

	public void setEstagioVO(EstagioVO estagioVO) {
		this.estagioVO = estagioVO;
	}

	public Integer getNrVersao() {
		if (nrVersao == null) {
			nrVersao = 1;
		}
		return nrVersao;
	}

	public void setNrVersao(Integer nrVersao) {
		this.nrVersao = nrVersao;
	}	

	public Date getDataLimiteAnalise() {		
		return dataLimiteAnalise;
	}

	public void setDataLimiteAnalise(Date dataLimiteAnalise) {
		this.dataLimiteAnalise = dataLimiteAnalise;
	}

	public Date getDataLimiteCorrecao() {		
		return dataLimiteCorrecao;
	}

	public void setDataLimiteCorrecao(Date dataLimiteCorrecao) {
		this.dataLimiteCorrecao = dataLimiteCorrecao;
	}

	public Date getDataEnvioAnalise() {		
		return dataEnvioAnalise;
	}

	public void setDataEnvioAnalise(Date dataEnvioAnalise) {
		this.dataEnvioAnalise = dataEnvioAnalise;
	}

	public Date getDataEnvioCorrecao() {		
		return dataEnvioCorrecao;
	}

	public void setDataEnvioCorrecao(Date dataEnvioCorrecao) {
		this.dataEnvioCorrecao = dataEnvioCorrecao;
	}

	public SituacaoQuestionarioRespostaOrigemEnum getSituacaoQuestionarioRespostaOrigemEnum() {
		if (situacaoQuestionarioRespostaOrigemEnum == null) {
			situacaoQuestionarioRespostaOrigemEnum = SituacaoQuestionarioRespostaOrigemEnum.EM_PREENCHIMENTO;
		}
		return situacaoQuestionarioRespostaOrigemEnum;
	}

	public void setSituacaoQuestionarioRespostaOrigemEnum(SituacaoQuestionarioRespostaOrigemEnum situacaoQuestionarioRespostaOrigemEnum) {
		this.situacaoQuestionarioRespostaOrigemEnum = situacaoQuestionarioRespostaOrigemEnum;
	}	

	public String getObservacaoFinal() {
		if (observacaoFinal == null) {
			observacaoFinal = "";
		}
		return observacaoFinal;
	}

	public void setObservacaoFinal(String observacaoFinal) {
		this.observacaoFinal = observacaoFinal;
	}

	public String getMotivo() {
		if (motivo == null) {
			motivo = "";
		}
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}


	public List<QuestionarioRespostaOrigemMotivosPadroesEstagioVO> getQuestionarioRespostaOrigemMotivosPadroesEstagioVOs() {
		if (questionarioRespostaOrigemMotivosPadroesEstagioVOs == null) {
			questionarioRespostaOrigemMotivosPadroesEstagioVOs = new ArrayList<QuestionarioRespostaOrigemMotivosPadroesEstagioVO>(0);
		}
		return questionarioRespostaOrigemMotivosPadroesEstagioVOs;
	}


	public void setQuestionarioRespostaOrigemMotivosPadroesEstagioVOs(List<QuestionarioRespostaOrigemMotivosPadroesEstagioVO> questionarioRespostaOrigemMotivosPadroesEstagioVOs) {
		this.questionarioRespostaOrigemMotivosPadroesEstagioVOs = questionarioRespostaOrigemMotivosPadroesEstagioVOs;
	}
	
	
	
	
	public QuestionarioRespostaOrigemVO getClonePorEstagioAproveitamento() {
		QuestionarioRespostaOrigemVO clone = new QuestionarioRespostaOrigemVO();
		clone = (QuestionarioRespostaOrigemVO) Uteis.clonar(this);
		clone.setCodigo(0);			
		clone.getPerguntaRespostaOrigemVOs().clear();
		for (PerguntaRespostaOrigemVO proExistente : getPerguntaRespostaOrigemVOs()) {
			PerguntaRespostaOrigemVO proClone = proExistente.getClonePorEstagio();
			proClone.setCodigo(0);
			proClone.setQuestionarioRespostaOrigemVO(clone);
			clone.getPerguntaRespostaOrigemVOs().add(proClone);
		}
		for(QuestionarioRespostaOrigemMotivosPadroesEstagioVO questExistente : getQuestionarioRespostaOrigemMotivosPadroesEstagioVOs()) {
			questExistente.setCodigo(0);
			questExistente.setQuestionarioRespostaOrigemVO(clone);			
		}
		return clone;
	}
	
	
	
	
}
