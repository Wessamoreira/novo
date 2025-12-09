package negocio.comuns.administrativo;

import controle.administrativo.ConfiguracaoAparenciaSistemaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

public class PreferenciaSistemaUsuarioVO extends SuperVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4940341881371959693L;
	private Integer codigo;
	private UsuarioVO usuarioVO;
	private Integer tamanhoFonte;
	private ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO;
	
	
	public Integer getCodigo() {
		if(codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public UsuarioVO getUsuarioVO() {
		if(usuarioVO == null) {
			usuarioVO =  new UsuarioVO();
		}
		return usuarioVO;
	}
	
	public void setUsuarioVO(UsuarioVO usuarioVO) {
		this.usuarioVO = usuarioVO;
	}
	
	public Integer getTamanhoFonte() {
		if(tamanhoFonte == null) {
			tamanhoFonte = 13;
		}
		return tamanhoFonte;
	}
	
	public void incrementarFonte() {
		if(getTamanhoFonte() < 20) {
			setTamanhoFonte(tamanhoFonte+1);
		}
	}
	public void decrementarFonte() {
		if(getTamanhoFonte() > 13) {
			setTamanhoFonte(tamanhoFonte-1);
		}
	}
	public void setTamanhoFonte(Integer tamanhoFonte) {
		this.tamanhoFonte = tamanhoFonte;
	}

	public ConfiguracaoAparenciaSistemaVO getConfiguracaoAparenciaSistemaVO() {
		if(configuracaoAparenciaSistemaVO == null) {
			configuracaoAparenciaSistemaVO =  new ConfiguracaoAparenciaSistemaVO();
		}
		return configuracaoAparenciaSistemaVO;
	}

	public void setConfiguracaoAparenciaSistemaVO(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO) {
		this.configuracaoAparenciaSistemaVO = configuracaoAparenciaSistemaVO;
	}
	
	
	
	public String cssFonte;
	public String getCssFonte() {
		if(cssFonte == null) {
			if(getTamanhoFonte() > 13) {
				StringBuilder css = new StringBuilder();
				css.append("input{font-size:").append(getTamanhoFonte()).append("px !important} ");
				css.append("select{font-size:").append(getTamanhoFonte()).append("px !important} ");
				css.append("option{font-size:").append(getTamanhoFonte()).append("px !important} ");
				css.append("buttom{font-size:").append(getTamanhoFonte()).append("px !important} ");
				css.append("a{font-size:").append(getTamanhoFonte()).append("px !important} ");
				css.append("label{font-size:").append(getTamanhoFonte()).append("px !important} ");				
				css.append("span{font-size:").append(getTamanhoFonte()).append("px !important} ");				
				css.append("i{font-size:").append(getTamanhoFonte()).append("px !important} ");
				css.append(".fonteHeaderDashboard{font-size:").append(getTamanhoFonte()).append("px !important} ");
				css.append(".iconeMenu{font-size:").append(getTamanhoFonte()).append("px !important} ");
				css.append("@media (min-width: 768px) {");
				if(getTamanhoFonte().equals(20)) {
					css.append(".sidebar {width:25rem !important} ");
					css.append(".sidebar.toggled {width:13.5rem !important} ");
//					css.append(".cloneMenu {width:12rem !important} ");
//					css.append(".expandMenu {width:24rem !important} ");
					css.append(".panelMenuIncluido {left:13.5rem !important; width: calc(100vw - 14rem) !important} ");
					css.append(".expandMenuIncluido {left:25rem !important; width: calc(100vw - 25.5rem) !important} ");
					
				}else if(getTamanhoFonte().equals(19)) {
					css.append(".sidebar {width:25rem !important} ");
					css.append(".sidebar.toggled {width:13.5rem !important} ");
//					css.append(".cloneMenu {width:12rem !important} ");
//					css.append(".expandMenu {width:24rem !important} ");
					css.append(".panelMenuIncluido {left:13.5rem !important; width: calc(100vw - 14rem) !important} ");
					css.append(".expandMenuIncluido {left:25rem !important; width: calc(100vw - 25.5rem) !important} ");
				}else if(getTamanhoFonte().equals(18)) {
					css.append(".sidebar {width:25rem !important} ");
					css.append(".sidebar.toggled {width:13.5rem !important} ");
//					css.append(".cloneMenu {width:12rem !important} ");
//					css.append(".expandMenu {width:24rem !important} ");
					css.append(".panelMenuIncluido {left:13.5rem !important; width: calc(100vw - 14rem) !important} ");
					css.append(".expandMenuIncluido {left:25rem !important; width: calc(100vw - 25.5rem) !important} ");
				}else if(getTamanhoFonte().equals(17)) {
					css.append(".sidebar {width:24rem !important} ");
					css.append(".sidebar.toggled {width:12.5rem !important} ");
//					css.append(".cloneMenu {width:11rem !important} ");
//					css.append(".expandMenu {width:23rem !important} ");
					css.append(".panelMenuIncluido {left:12.5rem !important; width: calc(100vw - 13rem) !important} ");
					css.append(".expandMenuIncluido {left:24rem !important; width: calc(100vw - 24.5rem) !important} ");
				}else if(getTamanhoFonte().equals(16)) {
					css.append(".sidebar {width:23rem !important} ");
					css.append(".sidebar.toggled {width:11.5rem !important} ");
//					css.append(".cloneMenu {width:10rem !important} ");
//					css.append(".expandMenu {width:22rem !important} ");
					css.append(".panelMenuIncluido {left:11.5rem !important; width: calc(100vw - 12rem) !important} ");
					css.append(".expandMenuIncluido {left:23rem !important; width: calc(100vw - 23.5rem) !important} ");
				}else if(getTamanhoFonte().equals(15)) {
					css.append(".sidebar {width:22rem !important} ");
					css.append(".sidebar.toggled {width:10.5rem !important} ");
//					css.append(".cloneMenu {width:9rem !important} ");
//					css.append(".expandMenu {width:21rem !important} ");
					css.append(".panelMenuIncluido {left:10.5rem !important; width: calc(100vw - 11rem) !important} ");
					css.append(".expandMenuIncluido {left:22rem !important; width: calc(100vw - 22.5rem) !important} ");
				}else if(getTamanhoFonte().equals(14)) {
					css.append(".sidebar {width:22rem !important} ");
					css.append(".sidebar.toggled {width:9.5rem !important} ");
//					css.append(".cloneMenu {width:9rem !important} ");
//					css.append(".expandMenu {width:18.45rem !important} ");
					css.append(".panelMenuIncluido {left:9.5rem !important; width: calc(100vw - 10rem) !important} ");
					css.append(".expandMenuIncluido {left:22rem !important; width: calc(100vw - 22.5rem) !important} ");
				}
				css.append("} ");
				
				cssFonte = css.toString();
			}else {
				cssFonte = "";
			}
		}
		return cssFonte;
	}

	public void setCssFonte(String cssFonte) {
		this.cssFonte = cssFonte;
	}
	

}
