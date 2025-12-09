package negocio.comuns.protocolo;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;

public class TipoRequerimentoDepartamentoFuncionarioVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2667450197392329766L;
	private Integer codigo;
	private FuncionarioVO funcionarioVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO;
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
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
	public TipoRequerimentoDepartamentoVO getTipoRequerimentoDepartamentoVO() {
		if (tipoRequerimentoDepartamentoVO == null) {
			tipoRequerimentoDepartamentoVO = new TipoRequerimentoDepartamentoVO();
		}
		return tipoRequerimentoDepartamentoVO;
	}
	public void setTipoRequerimentoDepartamentoVO(TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO) {
		this.tipoRequerimentoDepartamentoVO = tipoRequerimentoDepartamentoVO;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((funcionarioVO == null) ? 0 : funcionarioVO.getCodigo().hashCode());
		result = prime * result + ((tipoRequerimentoDepartamentoVO == null) ? 0 : tipoRequerimentoDepartamentoVO.getCodigo().hashCode());
		result = prime * result + ((unidadeEnsinoVO == null) ? 0 : unidadeEnsinoVO.getCodigo().hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoRequerimentoDepartamentoFuncionarioVO other = (TipoRequerimentoDepartamentoFuncionarioVO) obj;
		if (funcionarioVO == null) {
			if (other.funcionarioVO != null)
				return false;
		} else if (!funcionarioVO.getCodigo().equals(other.funcionarioVO.getCodigo()))
			return false;
		if (tipoRequerimentoDepartamentoVO == null) {
			if (other.tipoRequerimentoDepartamentoVO != null)
				return false;
		} else if (!tipoRequerimentoDepartamentoVO.getCodigo().equals(other.tipoRequerimentoDepartamentoVO.getCodigo()))
			return false;
		if (unidadeEnsinoVO == null) {
			if (other.unidadeEnsinoVO != null)
				return false;
		} else if (!unidadeEnsinoVO.getCodigo().equals(other.unidadeEnsinoVO.getCodigo()))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "TipoRequerimentoDepartamentoFuncionarioVO [TipoRequerimentoDepartamentoVO = "+tipoRequerimentoDepartamentoVO.getCodigo()+", funcionarioVO=" + funcionarioVO.getPessoa().getNome() + ", unidadeEnsinoVO=" + unidadeEnsinoVO.getNome() + "]";
	}

	public String getOrdenacao() {
		return getUnidadeEnsinoVO().getNome()+getFuncionarioVO().getPessoa().getNome();
	}
	
}
