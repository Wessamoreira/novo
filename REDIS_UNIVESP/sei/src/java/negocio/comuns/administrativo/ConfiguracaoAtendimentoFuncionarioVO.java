package negocio.comuns.administrativo;

import negocio.comuns.arquitetura.SuperVO;

/**
 * 
 * @author Pedro
 */
public class ConfiguracaoAtendimentoFuncionarioVO extends SuperVO {
	private Integer codigo;
	private ConfiguracaoAtendimentoVO configuracaoAtendimentoVO;
	private FuncionarioVO funcionarioVO;
	private Boolean inativoTemporario;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public ConfiguracaoAtendimentoVO getConfiguracaoAtendimentoVO() {
		if (configuracaoAtendimentoVO == null) {
			configuracaoAtendimentoVO = new ConfiguracaoAtendimentoVO();
		}
		return configuracaoAtendimentoVO;
	}

	public void setConfiguracaoAtendimentoVO(ConfiguracaoAtendimentoVO configuracaoAtendimentoVO) {
		this.configuracaoAtendimentoVO = configuracaoAtendimentoVO;
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

	public Boolean getInativoTemporario() {
		if (inativoTemporario == null) {
			inativoTemporario = false;
		}
		return inativoTemporario;
	}

	public void setInativoTemporario(Boolean inativoTemporario) {
		this.inativoTemporario = inativoTemporario;
	}
	
	

}
