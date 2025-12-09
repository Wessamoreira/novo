package negocio.comuns.processosel;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;

public class PerguntaChecklistVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2618872943856094218L;
	private Integer codigo;
	private String descricao;
	private StatusAtivoInativoEnum statusAtivoInativoEnum;
	private PerguntaVO perguntaVO;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public StatusAtivoInativoEnum getStatusAtivoInativoEnum() {
		if (statusAtivoInativoEnum == null) {
			statusAtivoInativoEnum = StatusAtivoInativoEnum.ATIVO;
		}
		return statusAtivoInativoEnum;
	}

	public void setStatusAtivoInativoEnum(StatusAtivoInativoEnum statusAtivoInativoEnum) {
		this.statusAtivoInativoEnum = statusAtivoInativoEnum;
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

}
