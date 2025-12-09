package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.FechamentoMesVO;

public class FechamentoFinanceiroVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4090034281299000361L;
	private Integer codigo;
	private Date dataFechamento;
	private FechamentoMesVO fechamentoMesVO;
	private UsuarioVO usuarioFechamento;
	private String descricaoFechamento;
	private List<FechamentoFinanceiroCentroResultadoVO> fechamentoFinanceiroCentroResultadoVOs;
	private List<FechamentoFinanceiroContaVO> fechamentoFinanceiroContaVOs;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Date getDataFechamento() {
		if (dataFechamento == null) {
			dataFechamento = new Date();
		}
		return dataFechamento;
	}

	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public UsuarioVO getUsuarioFechamento() {
		if (usuarioFechamento == null) {
			usuarioFechamento = new UsuarioVO();
		}
		return usuarioFechamento;
	}

	public void setUsuarioFechamento(UsuarioVO usuarioFechamento) {
		this.usuarioFechamento = usuarioFechamento;
	}

	public String getDescricaoFechamento() {
		if (descricaoFechamento == null) {
			descricaoFechamento = "";
		}
		return descricaoFechamento;
	}

	public void setDescricaoFechamento(String descricaoFechamento) {
		this.descricaoFechamento = descricaoFechamento;
	}

	public List<FechamentoFinanceiroCentroResultadoVO> getFechamentoFinanceiroCentroResultadoVOs() {
		if (fechamentoFinanceiroCentroResultadoVOs == null) {
			fechamentoFinanceiroCentroResultadoVOs = new ArrayList<FechamentoFinanceiroCentroResultadoVO>(0);
		}
		return fechamentoFinanceiroCentroResultadoVOs;
	}

	public void setFechamentoFinanceiroCentroResultadoVOs(
			List<FechamentoFinanceiroCentroResultadoVO> fechamentoFinanceiroCentroResultadoVOs) {
		this.fechamentoFinanceiroCentroResultadoVOs = fechamentoFinanceiroCentroResultadoVOs;
	}

	public List<FechamentoFinanceiroContaVO> getFechamentoFinanceiroContaVOs() {
		if (fechamentoFinanceiroContaVOs == null) {
			fechamentoFinanceiroContaVOs = new ArrayList<FechamentoFinanceiroContaVO>(0);
		}
		return fechamentoFinanceiroContaVOs;
	}

	public void setFechamentoFinanceiroContaVOs(List<FechamentoFinanceiroContaVO> fechamentoFinanceiroContaVOs) {
		this.fechamentoFinanceiroContaVOs = fechamentoFinanceiroContaVOs;
	}

	public FechamentoMesVO getFechamentoMesVO() {
		if (fechamentoMesVO == null) {
			fechamentoMesVO = new FechamentoMesVO();
		}
		return fechamentoMesVO;
	}

	public void setFechamentoMesVO(FechamentoMesVO fechamentoMesVO) {
		this.fechamentoMesVO = fechamentoMesVO;
	}

	

	

}
