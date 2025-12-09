package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;

public class CondicaoPagamentoPlanoDescontoVO extends SuperVO {
	
	private Integer codigo;
	private PlanoDescontoVO planoDescontoVO;
	private Integer condicaoPagamentoPlanoFinanceiroCurso;
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public PlanoDescontoVO getPlanoDescontoVO() {
		if (planoDescontoVO == null) {
			planoDescontoVO = new PlanoDescontoVO();
		}
		return planoDescontoVO;
	}
	public void setPlanoDescontoVO(PlanoDescontoVO planoDescontoVO) {
		this.planoDescontoVO = planoDescontoVO;
	}
	public Integer getCondicaoPagamentoPlanoFinanceiroCurso() {
		if (condicaoPagamentoPlanoFinanceiroCurso == null) {
			condicaoPagamentoPlanoFinanceiroCurso = 0;
		}
		return condicaoPagamentoPlanoFinanceiroCurso;
	}
	public void setCondicaoPagamentoPlanoFinanceiroCurso(Integer condicaoPagamentoPlanoFinanceiroCurso) {
		this.condicaoPagamentoPlanoFinanceiroCurso = condicaoPagamentoPlanoFinanceiroCurso;
	}


}
