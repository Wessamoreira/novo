package negocio.comuns.contabil;

import negocio.comuns.arquitetura.SuperVO;

/**
 * 
 * @author PedroOtimize
 *
 */
public class ConfiguracaoContabilRegraPlanoContaVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8916279309864168009L;
	private Integer codigo;
	private ConfiguracaoContabilRegraVO configuracaoContabilRegraVO;
	private PlanoContaVO planoContaCreditoVO;
	private PlanoContaVO planoContaDebitoVO;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public ConfiguracaoContabilRegraVO getConfiguracaoContabilRegraVO() {
		if (configuracaoContabilRegraVO == null) {
			configuracaoContabilRegraVO = new ConfiguracaoContabilRegraVO();
		}
		return configuracaoContabilRegraVO;
	}

	public void setConfiguracaoContabilRegraVO(ConfiguracaoContabilRegraVO configuracaoContabilRegraVO) {
		this.configuracaoContabilRegraVO = configuracaoContabilRegraVO;
	}

	public PlanoContaVO getPlanoContaCreditoVO() {
		if (planoContaCreditoVO == null) {
			planoContaCreditoVO = new PlanoContaVO();
		}
		return planoContaCreditoVO;
	}

	public void setPlanoContaCreditoVO(PlanoContaVO planoContaCreditoVO) {
		this.planoContaCreditoVO = planoContaCreditoVO;
	}

	public PlanoContaVO getPlanoContaDebitoVO() {
		if (planoContaDebitoVO == null) {
			planoContaDebitoVO = new PlanoContaVO();
		}
		return planoContaDebitoVO;
	}

	public void setPlanoContaDebitoVO(PlanoContaVO planoContaDebitoVO) {
		this.planoContaDebitoVO = planoContaDebitoVO;
	}

	public boolean equalsCampoSelecaoLista(ConfiguracaoContabilRegraPlanoContaVO obj) {
		return getPlanoContaCreditoVO().getCodigo().equals(obj.getPlanoContaCreditoVO().getCodigo()) && getPlanoContaDebitoVO().getCodigo().equals(obj.getPlanoContaDebitoVO().getCodigo());
	}

}
