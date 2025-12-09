package negocio.comuns.estagio;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;

public class ConfiguracaoEstagioObrigatorioFuncionarioVO extends SuperVO {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 5451769198070926008L;
	private Integer codigo;
	private ConfiguracaoEstagioObrigatorioVO configuracaoEstagioObrigatorioVO;
	private FuncionarioVO funcionarioVO;
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public ConfiguracaoEstagioObrigatorioVO getConfiguracaoEstagioObrigatorioVO() {
		if (configuracaoEstagioObrigatorioVO == null) {
			configuracaoEstagioObrigatorioVO = new ConfiguracaoEstagioObrigatorioVO();
		}
		return configuracaoEstagioObrigatorioVO;
	}

	public void setConfiguracaoEstagioObrigatorioVO(ConfiguracaoEstagioObrigatorioVO configuracaoEstagioObrigatorioVO) {
		this.configuracaoEstagioObrigatorioVO = configuracaoEstagioObrigatorioVO;
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
