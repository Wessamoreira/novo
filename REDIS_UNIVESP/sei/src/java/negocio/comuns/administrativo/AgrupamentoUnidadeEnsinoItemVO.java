package negocio.comuns.administrativo;

import negocio.comuns.arquitetura.SuperVO;

public class AgrupamentoUnidadeEnsinoItemVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6628532668994347083L;
	private Integer codigo;
	private AgrupamentoUnidadeEnsinoVO agrupamentoUnidadeEnsinoVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public AgrupamentoUnidadeEnsinoVO getAgrupamentoUnidadeEnsinoVO() {
		if (agrupamentoUnidadeEnsinoVO == null) {
			agrupamentoUnidadeEnsinoVO = new AgrupamentoUnidadeEnsinoVO();
		}
		return agrupamentoUnidadeEnsinoVO;
	}

	public void setAgrupamentoUnidadeEnsinoVO(AgrupamentoUnidadeEnsinoVO agrupamentoUnidadeEnsinoVO) {
		this.agrupamentoUnidadeEnsinoVO = agrupamentoUnidadeEnsinoVO;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

}
