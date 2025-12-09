package negocio.comuns.academico;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.enumeradores.SituacaoQuestionarioRespostaOrigemEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.processosel.PerguntaQuestionarioVO;
import negocio.comuns.processosel.PerguntaVO;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class PerguntaRespostaOrigemVO extends SuperVO implements Cloneable {

	private static final long serialVersionUID = -393578052852307253L;

	private QuestionarioRespostaOrigemVO questionarioRespostaOrigemVO;
	private PerguntaQuestionarioVO perguntaQuestionarioVO;
	private PerguntaVO perguntaVO;
	private Integer codigo;
	private List<PerguntaItemRespostaOrigemVO> perguntaItemRespostaOrigemVOs;
	private List<RespostaPerguntaRespostaOrigemVO> respostaPerguntaRespostaOrigemVOs;
	private List<List<PerguntaItemRespostaOrigemVO>> perguntaItemRespostaOrigemAdicionadaVOs;
	private String texto;
	private Date data;
	private Boolean verdadeiroFalse;
	private Double numerico;
	private String hora;
	private RespostaPerguntaVO respostaPerguntaVO;
	private ArquivoVO arquivoRespostaVO;
	private Integer ordem;
	private List<PerguntaItemRespostaOrigemVO> listaPerguntaItemRespostaOrigemRel;
	private List<PerguntaChecklistOrigemVO> listaPerguntaChecklistOrigem;
	
	/**
	 * criar lista de arquivo
	 * @return
	 */private List<ArquivoVO> listaArquivoVOs;
	 private boolean apresentarListaArquivoVOs = false;
	
	 public PerguntaRespostaOrigemVO getClonePorEstagio() {
		PerguntaRespostaOrigemVO clone = new PerguntaRespostaOrigemVO();
		clone = (PerguntaRespostaOrigemVO) Uteis.clonar(this);
		clone.setCodigo(0);
		clone.setArquivoRespostaVO((ArquivoVO) Uteis.clonar(getArquivoRespostaVO()));
		clone.getArquivoRespostaVO().setCodigo(0);
		clone.getArquivoRespostaVO().setNovoObj(true);		
		clone.setRespostaPerguntaVO((RespostaPerguntaVO) Uteis.clonar(getRespostaPerguntaVO()));
		clone.getRespostaPerguntaVO().setCodigo(0);
		clone.getPerguntaItemRespostaOrigemVOs().clear();
		clone.getRespostaPerguntaRespostaOrigemVOs().clear();
		clone.getListaPerguntaChecklistOrigem().clear();
		clone.getListaArquivoVOs().clear();
		for (PerguntaItemRespostaOrigemVO piro : getPerguntaItemRespostaOrigemVOs()) {
			PerguntaItemRespostaOrigemVO piroClone = piro.getClone();
			piroClone.setPerguntaRespostaOrigemVO(clone);
			clone.getPerguntaItemRespostaOrigemVOs().add(piroClone);
		}
		for (RespostaPerguntaRespostaOrigemVO rpro : getRespostaPerguntaRespostaOrigemVOs()) {
			RespostaPerguntaRespostaOrigemVO rproClone = rpro.getClone();
			rproClone.setPerguntaRespostaOrigemVO(clone);
			clone.getRespostaPerguntaRespostaOrigemVOs().add(rproClone);
		}
		for (PerguntaChecklistOrigemVO pco : getListaPerguntaChecklistOrigem()) {
			PerguntaChecklistOrigemVO pcoClone = pco.getClone();
			pcoClone.setPerguntaRespostaOrigemVO(clone);
			clone.getListaPerguntaChecklistOrigem().add(pcoClone);
		}
		for (ArquivoVO arquivo : getListaArquivoVOs()) {
			ArquivoVO arquivoClone = (ArquivoVO) Uteis.clonar(arquivo);
			arquivoClone.setCodigo(0);
			arquivoClone.setNovoObj(true);
			clone.getListaArquivoVOs().add(arquivoClone);
		}
		return clone;
	}
	

	public QuestionarioRespostaOrigemVO getQuestionarioRespostaOrigemVO() {
		if (questionarioRespostaOrigemVO == null) {
			questionarioRespostaOrigemVO = new QuestionarioRespostaOrigemVO();
		}
		return questionarioRespostaOrigemVO;
	}

	public void setQuestionarioRespostaOrigemVO(QuestionarioRespostaOrigemVO questionarioRespostaOrigemVO) {
		this.questionarioRespostaOrigemVO = questionarioRespostaOrigemVO;
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

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public List<PerguntaItemRespostaOrigemVO> getPerguntaItemRespostaOrigemVOs() {
		if (perguntaItemRespostaOrigemVOs == null) {
			perguntaItemRespostaOrigemVOs = new ArrayList<PerguntaItemRespostaOrigemVO>(0);
		}
		return perguntaItemRespostaOrigemVOs;
	}

	public void setPerguntaItemRespostaOrigemVOs(List<PerguntaItemRespostaOrigemVO> perguntaItemRespostaOrigemVOs) {
		this.perguntaItemRespostaOrigemVOs = perguntaItemRespostaOrigemVOs;
	}

	public List<RespostaPerguntaRespostaOrigemVO> getRespostaPerguntaRespostaOrigemVOs() {
		if (respostaPerguntaRespostaOrigemVOs == null) {
			respostaPerguntaRespostaOrigemVOs = new ArrayList<RespostaPerguntaRespostaOrigemVO>(0);
		}
		return respostaPerguntaRespostaOrigemVOs;
	}

	public void setRespostaPerguntaRespostaOrigemVOs(
			List<RespostaPerguntaRespostaOrigemVO> respostaPerguntaRespostaOrigemVOs) {
		this.respostaPerguntaRespostaOrigemVOs = respostaPerguntaRespostaOrigemVOs;
	}

	public List<List<PerguntaItemRespostaOrigemVO>> getPerguntaItemRespostaOrigemAdicionadaVOs() {
		if (perguntaItemRespostaOrigemAdicionadaVOs == null) {
			perguntaItemRespostaOrigemAdicionadaVOs = new ArrayList<List<PerguntaItemRespostaOrigemVO>>(0);
		}
		return perguntaItemRespostaOrigemAdicionadaVOs;
	}

	public void setPerguntaItemRespostaOrigemAdicionadaVOs(
			List<List<PerguntaItemRespostaOrigemVO>> perguntaItemRespostaOrigemAdicionadaVOs) {
		this.perguntaItemRespostaOrigemAdicionadaVOs = perguntaItemRespostaOrigemAdicionadaVOs;
	}

	public String getTexto() {
		if (texto == null) {
			texto = "";
		}
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Boolean getVerdadeiroFalse() {
		if (verdadeiroFalse == null) {
			verdadeiroFalse = Boolean.FALSE;
		}
		return verdadeiroFalse;
	}

	public void setVerdadeiroFalse(Boolean verdadeiroFalse) {
		this.verdadeiroFalse = verdadeiroFalse;
	}

	public Double getNumerico() {
		if (numerico == null) {
			numerico = 0.0;
		}
		return numerico;
	}

	public void setNumerico(Double numerico) {
		this.numerico = numerico;
	}

	public String getHora() {
		if (hora == null) {
			hora = "";
		}
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
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

	public PerguntaVO getPerguntaVO() {
		if (perguntaVO == null) {
			perguntaVO = new PerguntaVO();
		}
		return perguntaVO;
	}

	public void setPerguntaVO(PerguntaVO perguntaVO) {
		this.perguntaVO = perguntaVO;
	}

	public Integer getOrdem() {
		if (ordem == null) {
			ordem = 0;
		}
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public ArquivoVO getArquivoRespostaVO() {
		if (arquivoRespostaVO == null) {
			arquivoRespostaVO = new ArquivoVO();
		}
		return arquivoRespostaVO;
	}


	public void setArquivoRespostaVO(ArquivoVO arquivoRespostaVO) {
		this.arquivoRespostaVO = arquivoRespostaVO;
	}
	
	public Integer getListaArquivoVOSize() {
		return getListaArquivoVOs().size();
	}

	public List<ArquivoVO> getListaArquivoVOs() {
		if (listaArquivoVOs == null) {
			listaArquivoVOs = new ArrayList<>();
		}
		return listaArquivoVOs;
	}

	public void setListaArquivoVOs(List<ArquivoVO> listaArquivoVOs) {
		this.listaArquivoVOs = listaArquivoVOs;
	}

	public boolean isApresentarListaArquivoVOs() {		
		return apresentarListaArquivoVOs;
	}


	public void setApresentarListaArquivoVOs(boolean apresentarListaArquivoVOs) {
		this.apresentarListaArquivoVOs = apresentarListaArquivoVOs;
	}


	public JRDataSource getPerguntaItemRespostaOrigemJRDataSource() {
		return new JRBeanArrayDataSource(getListaPerguntaItemRespostaOrigemRel().toArray());
	}
	public List<PerguntaItemRespostaOrigemVO> getListaPerguntaItemRespostaOrigemRel() {
		if(listaPerguntaItemRespostaOrigemRel == null) {
			listaPerguntaItemRespostaOrigemRel = new ArrayList<PerguntaItemRespostaOrigemVO>(0);
		}	
			
		return listaPerguntaItemRespostaOrigemRel;
	}
	public void setListaPerguntaItemRespostaOrigemRel(
			List<PerguntaItemRespostaOrigemVO> listaPerguntaItemRespostaOrigemRel) {
		this.listaPerguntaItemRespostaOrigemRel = listaPerguntaItemRespostaOrigemRel;
	}

	public List<PerguntaChecklistOrigemVO> getListaPerguntaChecklistOrigem() {
		if (listaPerguntaChecklistOrigem == null) {
			listaPerguntaChecklistOrigem = new ArrayList<>();
		}
		return listaPerguntaChecklistOrigem;
	}

	public void setListaPerguntaChecklistOrigem(List<PerguntaChecklistOrigemVO> listaPerguntaChecklistOrigem) {
		this.listaPerguntaChecklistOrigem = listaPerguntaChecklistOrigem;
	}
	
	public String getCssColunaPerguntaChecklistOrigem() {
		
		return getListaPerguntaChecklistOrigem().size() > 0 ? "col-md-"+(12/getListaPerguntaChecklistOrigem().size()) : "col-md-12" ;
	}

	public JRDataSource getRespostaPerguntaRespostaOrigemJRDataSource() {
		return new JRBeanArrayDataSource(getRespostaPerguntaRespostaOrigemVOs().toArray());
	}
	
	public String getDataApresentarString() {		

		if(Uteis.isAtributoPreenchido(getData())) {
			SimpleDateFormat formatarData = new SimpleDateFormat(getPerguntaVO().getMascaraData()); 			   
			String dataFormatada = formatarData.format(getData());
			return dataFormatada;
		}
		return "";
	}
}
