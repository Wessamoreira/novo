package negocio.comuns.financeiro;

import java.io.Serializable;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;

public class CondicaoRenegociacaoFuncionarioVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private FuncionarioVO funcionarioVO;
	private CondicaoRenegociacaoVO condicaoRenegociacaoVO;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public CondicaoRenegociacaoVO getCondicaoRenegociacaoVO() {
		if (condicaoRenegociacaoVO == null) {
			condicaoRenegociacaoVO = new CondicaoRenegociacaoVO();
		}
		return condicaoRenegociacaoVO;
	}

	public void setCondicaoRenegociacaoVO(CondicaoRenegociacaoVO condicaoRenegociacaoVO) {
		this.condicaoRenegociacaoVO = condicaoRenegociacaoVO;
	}

	public FuncionarioVO getFuncionarioVO() {
		if (funcionarioVO == null) {
			funcionarioVO = new FuncionarioVO();
		}
		return funcionarioVO;
	}

	public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
		this.funcionarioVO = funcionarioVO;
	}

}
