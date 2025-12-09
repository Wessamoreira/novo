package negocio.comuns.administrativo;

import negocio.comuns.arquitetura.SuperVO;

/**
 * 
 * @author Pedro
 */
public class ConfiguracaoAtendimentoUnidadeEnsinoVO extends SuperVO {

	private Integer codigo;
	private ConfiguracaoAtendimentoVO configuracaoAtendimentoVO;
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

	public ConfiguracaoAtendimentoVO getConfiguracaoAtendimentoVO() {
		if (configuracaoAtendimentoVO == null) {
			configuracaoAtendimentoVO = new ConfiguracaoAtendimentoVO();
		}
		return configuracaoAtendimentoVO;
	}

	public void setConfiguracaoAtendimentoVO(ConfiguracaoAtendimentoVO configuracaoAtendimentoVO) {
		this.configuracaoAtendimentoVO = configuracaoAtendimentoVO;
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
