package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import relatorio.negocio.comuns.academico.enumeradores.TipoObservacaoHistoricoEnum;

public class ConfiguracaoObservacaoHistoricoVO extends SuperVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4345734350887876706L;
	private Integer codigo;
	private TipoObservacaoHistoricoEnum tipoObservacaoHistorico;
	private String observacao;
	private Boolean ocultar;
	private Boolean padrao;	
	private ConfiguracaoHistoricoVO configuracaoHistoricoVO;
	
	public Integer getCodigo() {
		if(codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getObservacao() {
		if(observacao == null) {
			observacao = "";
		}
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public ConfiguracaoHistoricoVO getConfiguracaoHistoricoVO() {
		if(configuracaoHistoricoVO == null) {
			configuracaoHistoricoVO = new ConfiguracaoHistoricoVO();
		}
		return configuracaoHistoricoVO;
	}
	public void setConfiguracaoHistoricoVO(ConfiguracaoHistoricoVO configuracaoHistoricoVO) {
		this.configuracaoHistoricoVO = configuracaoHistoricoVO;
	}
	public Boolean getOcultar() {
		if(ocultar == null) {
			ocultar = false;
		}
		return ocultar;
	}
	public void setOcultar(Boolean ocultar) {
		this.ocultar = ocultar;
	}
	public Boolean getPadrao() {
		if(padrao == null) {
			padrao =  false;
		}
		return padrao;
	}
	public void setPadrao(Boolean padrao) {
		this.padrao = padrao;
	}
	
	public TipoObservacaoHistoricoEnum getTipoObservacaoHistorico() {
		if(tipoObservacaoHistorico == null) {
			tipoObservacaoHistorico =  TipoObservacaoHistoricoEnum.INTEGRALIZACAO;
		}
		return tipoObservacaoHistorico;
	}
	
	public void setTipoObservacaoHistorico(TipoObservacaoHistoricoEnum tipoObservacaoHistorico) {
		this.tipoObservacaoHistorico = tipoObservacaoHistorico;
	}
	
	
	
}
