package webservice.servicos.objetos;

import negocio.comuns.processosel.InscricaoVO;
//import negocio.comuns.processosel.QuestaoProvaProcessoSeletivoVO;

public class ProcessoSeletivoRSVO {

	private InscricaoVO inscricaoVO;
	private String tipoPergunta;
//	private QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivo;	
	private String codigoAutenticacaoProvaNavegador;
	
	public InscricaoVO getInscricaoVO() {
		if (inscricaoVO == null) {
			inscricaoVO = new InscricaoVO();
		}
		return inscricaoVO;
	}
	
	public void setInscricaoVO(InscricaoVO inscricaoVO) {
		this.inscricaoVO = inscricaoVO;
	}
	
	public String getTipoPergunta() {
		if (tipoPergunta == null) {
			tipoPergunta = "";
		}
		return tipoPergunta;
	}
	
	public void setTipoPergunta(String tipoPergunta) {
		this.tipoPergunta = tipoPergunta;
	}
	
	

	public String getCodigoAutenticacaoProvaNavegador() {
		if(codigoAutenticacaoProvaNavegador == null) {
			codigoAutenticacaoProvaNavegador ="";
		}
		return codigoAutenticacaoProvaNavegador;
	}

	public void setCodigoAutenticacaoProvaNavegador(String codigoAutenticacaoProvaNavegador) {
		this.codigoAutenticacaoProvaNavegador = codigoAutenticacaoProvaNavegador;
	}

}
