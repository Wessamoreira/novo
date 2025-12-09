package negocio.comuns.financeiro;

import negocio.comuns.arquitetura.SuperVO;

public class ControleRemessaMXVO extends SuperVO{
	
	private Integer codigo;
	private ContaCorrenteVO contaCorrenteVO;
	private Integer incremental;
	private Integer incrementalCP;
	
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public ContaCorrenteVO getContaCorrenteVO() {
		if (contaCorrenteVO == null) {
			contaCorrenteVO = new ContaCorrenteVO();
		}
		return contaCorrenteVO;
	}
	public void setContaCorrenteVO(ContaCorrenteVO contaCorrenteVO) {
		this.contaCorrenteVO = contaCorrenteVO;
	}
	public Integer getIncremental() {
		if (incremental == null) {
			incremental = 0;
		}
		return incremental;
	}
	public void setIncremental(Integer incremental) {
		this.incremental = incremental;
	}

	public Integer getIncrementalCP() {
		if (incrementalCP == null) {
			incrementalCP = 0;
		}
		return incrementalCP;
	}
	
	public void setIncrementalCP(Integer incrementalCP) {
		this.incrementalCP = incrementalCP;
	}
}
