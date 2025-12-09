package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.processosel.PerguntaItemVO;
import negocio.comuns.utilitarias.Uteis;

public class PerguntaItemRespostaOrigemVO extends SuperVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1414449594414454309L;
	private Integer codigo;
	private PerguntaRespostaOrigemVO perguntaRespostaOrigemPrincipalVO;
	private PerguntaRespostaOrigemVO perguntaRespostaOrigemVO;
	private PerguntaItemVO perguntaItemVO;
	private Integer ordem;
	
	public PerguntaItemRespostaOrigemVO getClone() {
		PerguntaItemRespostaOrigemVO clone = new PerguntaItemRespostaOrigemVO();
		clone = (PerguntaItemRespostaOrigemVO) Uteis.clonar(this);
		clone.setCodigo(0);
		return clone;
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
	public PerguntaItemVO getPerguntaItemVO() {
		if(perguntaItemVO == null) {
			perguntaItemVO = new PerguntaItemVO();
		}
		return perguntaItemVO;
	}
	public void setPerguntaItemVO(PerguntaItemVO perguntaItemVO) {
		this.perguntaItemVO = perguntaItemVO;
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
	public PerguntaRespostaOrigemVO getPerguntaRespostaOrigemPrincipalVO() {
		if(perguntaRespostaOrigemPrincipalVO == null) {
			perguntaRespostaOrigemPrincipalVO = new PerguntaRespostaOrigemVO();
		}
		return perguntaRespostaOrigemPrincipalVO;
	}
	public void setPerguntaRespostaOrigemPrincipalVO(PerguntaRespostaOrigemVO perguntaRespostaOrigemPrincipalVO) {
		this.perguntaRespostaOrigemPrincipalVO = perguntaRespostaOrigemPrincipalVO;
	}
	public Integer getOrdem() {
		if(ordem == null) {
			ordem = 0;
		}
		return ordem;
	}
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}	

}
