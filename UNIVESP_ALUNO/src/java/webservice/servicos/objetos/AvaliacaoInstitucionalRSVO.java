package webservice.servicos.objetos;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.processosel.PerguntaQuestionarioVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.RespostaPerguntaVO;

@XmlRootElement(name = "avaliacaoInstitucionalRSVO")
public class AvaliacaoInstitucionalRSVO {
	
	private AvaliacaoInstitucionalVO avaliacaoInstitucionalVO;
	private Integer codigoPerfilAcesso;
	private QuestionarioVO questionarioVO;
	private PerguntaQuestionarioVO perguntaQuestionarioVO;
	private RespostaPerguntaVO respostaPerguntaVO;
	private Integer pesoSelecionado;
	private List<AvaliacaoInstitucionalVO> listaAvaliacaoInstitucional;
	
	public AvaliacaoInstitucionalVO getAvaliacaoInstitucionalVO() {
		if (avaliacaoInstitucionalVO == null) {
			avaliacaoInstitucionalVO = new AvaliacaoInstitucionalVO();
		}
		return avaliacaoInstitucionalVO;
	}
	
	public void setAvaliacaoInstitucionalVO(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO) {
		this.avaliacaoInstitucionalVO = avaliacaoInstitucionalVO;
	}
	
	public Integer getCodigoPerfilAcesso() {
		if (codigoPerfilAcesso == null) {
			codigoPerfilAcesso = 0;
		}
		return codigoPerfilAcesso;
	}
	
	public void setCodigoPerfilAcesso(Integer codigoPerfilAcesso) {
		this.codigoPerfilAcesso = codigoPerfilAcesso;
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
	
	public PerguntaQuestionarioVO getPerguntaQuestionarioVO() {
		if (perguntaQuestionarioVO == null) {
			perguntaQuestionarioVO = new PerguntaQuestionarioVO();
		}
		return perguntaQuestionarioVO;
	}
	
	public void setPerguntaQuestionarioVO(PerguntaQuestionarioVO perguntaQuestionarioVO) {
		this.perguntaQuestionarioVO = perguntaQuestionarioVO;
	}
	
	public RespostaPerguntaVO getRespostaPerguntaVO() {
		if (respostaPerguntaVO == null) {
			respostaPerguntaVO = new RespostaPerguntaVO();
		}
		return respostaPerguntaVO;
	}
	
	public void setRespostaPerguntaVO(RespostaPerguntaVO respostaPerguntaVO) {
		this.respostaPerguntaVO = respostaPerguntaVO;
	}
	
	public Integer getPesoSelecionado() {
		if (pesoSelecionado == null) {
			pesoSelecionado = 0;
		}
		return pesoSelecionado;
	}
	
	public void setPesoSelecionado(Integer pesoSelecionado) {
		this.pesoSelecionado = pesoSelecionado;
	}

	public List<AvaliacaoInstitucionalVO> getListaAvaliacaoInstitucional() {
		if (listaAvaliacaoInstitucional == null) {
			listaAvaliacaoInstitucional = new ArrayList<AvaliacaoInstitucionalVO>();
		}
		return listaAvaliacaoInstitucional;
	}

	public void setListaAvaliacaoInstitucional(List<AvaliacaoInstitucionalVO> listaAvaliacaoInstitucional) {
		this.listaAvaliacaoInstitucional = listaAvaliacaoInstitucional;
	}

	
	
	
}