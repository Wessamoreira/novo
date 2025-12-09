package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.utilitarias.Uteis;

public class RespostaPerguntaRespostaOrigemVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 255620907040925881L;
	private Integer codigo;
	private RespostaPerguntaVO respostaPerguntaVO;
	private PerguntaRespostaOrigemVO perguntaRespostaOrigemVO;
	private Boolean selecionado;
	private String respostaAdicional;
	
	public RespostaPerguntaRespostaOrigemVO getClone() {
		RespostaPerguntaRespostaOrigemVO clone = new RespostaPerguntaRespostaOrigemVO();
		clone = (RespostaPerguntaRespostaOrigemVO) Uteis.clonar(this);
		clone.setCodigo(0);
		return clone;
	}

	public Integer getCodigo() {
		if(codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public RespostaPerguntaVO getRespostaPerguntaVO() {
		if(respostaPerguntaVO == null) {
			respostaPerguntaVO = new RespostaPerguntaVO();
		}
		return respostaPerguntaVO;
	}

	public void setRespostaPerguntaVO(RespostaPerguntaVO respostaPerguntaVO) {
		this.respostaPerguntaVO = respostaPerguntaVO;
	}

	public PerguntaRespostaOrigemVO getPerguntaRespostaOrigemVO() {
		if(perguntaRespostaOrigemVO == null) {
			perguntaRespostaOrigemVO = new PerguntaRespostaOrigemVO();
		}
		return perguntaRespostaOrigemVO;
	}

	public void setPerguntaRespostaOrigemVO(PerguntaRespostaOrigemVO perguntaRespostaOrigemVO) {
		this.perguntaRespostaOrigemVO = perguntaRespostaOrigemVO;
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

	public String getRespostaAdicional() {
		if(respostaAdicional == null){
			respostaAdicional = "";
		}
		return respostaAdicional;
	}

	public void setRespostaAdicional(String respostaAdicional) {
		this.respostaAdicional = respostaAdicional;
	}

}
