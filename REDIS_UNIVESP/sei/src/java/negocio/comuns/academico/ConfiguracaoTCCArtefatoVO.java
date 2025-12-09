package negocio.comuns.academico;

import negocio.comuns.administrativo.ConfiguracaoTCCVO;
import negocio.comuns.arquitetura.SuperVO;

public class ConfiguracaoTCCArtefatoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 792846440787046580L;
	private ConfiguracaoTCCVO configuracaoTCC;
	private String artefato;
	private Integer codigo;

	public ConfiguracaoTCCVO getConfiguracaoTCC() {
		if (configuracaoTCC == null) {
			configuracaoTCC = new ConfiguracaoTCCVO();
		}
		return configuracaoTCC;
	}

	public void setConfiguracaoTCC(ConfiguracaoTCCVO configuracaoTCC) {
		this.configuracaoTCC = configuracaoTCC;
	}

	public String getArtefato() {
		if (artefato == null) {
			artefato = "";
		}
		return artefato;
	}

	public void setArtefato(String artefato) {
		this.artefato = artefato;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

}
