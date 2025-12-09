package controle.administrativo;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;

public class ConfiguracaoAparenciaSistemaVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1711577700916738650L;
	private Integer codigo;
	private String nome;
	private String corBackIndex;

	private String corFonteIndex;
	private String corBackLogin;

	private String corFonteLogin;
	private String corBackBannerLogin;
	private Integer tempoBanner;
	private String linkVideosBanner;
	private List<ArquivoVO> bannerLoginVOs;
	private List<ArquivoVO> bannerLoginExcluirVOs;
	private String corBackMenu;

	private String corFonteMenu;
	private String corBackHeaderDashboard;

	private String corFontHeaderDashboard;
	private String corBackTopo;

	private String corFonteTopo;
	private String corBackAreaConteudo;

	private String corBackGeral;

	private String scriptCssGerado;
	private String scriptBannerGerado;

	private String corFonteTituloFormulario;
	private String corFonteTituloCampos;
	private String corBackCampos;
	private String corFonteHeaderTabela;
	private String corBackHeaderTabela;

	private String corFonteLinhaTabela;
	private String corBackLinhaTabela;
	private String corFonteHeaderAbas;
	private String corBackHeaderAbas;

	private Boolean disponibilizarUsuario;
	private String css;

	private String nomeImagemTopo;
	private String nomeImagemTopoAnterior;
	private PastaBaseArquivoEnum caminhoBaseImagemTopo;

	public String getScriptBannerGerado() {
		if (scriptBannerGerado == null) {
			scriptBannerGerado = "";
		}
		return scriptBannerGerado;
	}

	public void setScriptBannerGerado(String scriptBannerGerado) {
		this.scriptBannerGerado = scriptBannerGerado;
	}

	public String getCorBackIndex() {
		if (corBackIndex == null) {
			corBackIndex = "ffffff";
		}
		return corBackIndex;
	}

	public void setCorBackIndex(String corBackIndex) {
		this.corBackIndex = corBackIndex;
	}

	public String getCorFonteIndex() {
		if (corFonteIndex == null) {
			corFonteIndex = "000";
		}
		return corFonteIndex;
	}

	public void setCorFonteIndex(String corFonteIndex) {
		this.corFonteIndex = corFonteIndex;
	}

	public String getCorBackLogin() {
		if (corBackLogin == null) {
			corBackLogin = "5a5c69";
		}
		return corBackLogin;
	}

	public void setCorBackLogin(String corBackLogin) {
		this.corBackLogin = corBackLogin;
	}

	public String getCorFonteLogin() {
		if (corFonteLogin == null) {
			corFonteLogin = "ffffff";
		}
		return corFonteLogin;
	}

	public void setCorFonteLogin(String corFonteLogin) {
		this.corFonteLogin = corFonteLogin;
	}

	public String getCorBackBannerLogin() {
		if (corBackBannerLogin == null) {
			corBackBannerLogin = "fff";
		}
		return corBackBannerLogin;
	}

	public void setCorBackBannerLogin(String corBackBannerLogin) {
		this.corBackBannerLogin = corBackBannerLogin;
	}

	public List<ArquivoVO> getBannerLoginVOs() {
		if (bannerLoginVOs == null) {
			bannerLoginVOs = new ArrayList<ArquivoVO>(0);
		}
		return bannerLoginVOs;
	}

	public void setBannerLoginVOs(List<ArquivoVO> bannerLoginVOs) {
		this.bannerLoginVOs = bannerLoginVOs;
	}

	public String getCorBackMenu() {
		if (corBackMenu == null) {
			corBackMenu = "5a5c69";
		}
		return corBackMenu;
	}

	public void setCorBackMenu(String corBackMenu) {
		this.corBackMenu = corBackMenu;
	}

	public String getCorFonteMenu() {
		if (corFonteMenu == null) {
			corFonteMenu = "fff";
		}
		return corFonteMenu;
	}

	public void setCorFonteMenu(String corFonteMenu) {
		this.corFonteMenu = corFonteMenu;
	}

	public String getCorBackHeaderDashboard() {
		if (corBackHeaderDashboard == null) {
			corBackHeaderDashboard = "ededed";
		}
		return corBackHeaderDashboard;
	}

	public void setCorBackHeaderDashboard(String corBackHeaderDashboard) {
		this.corBackHeaderDashboard = corBackHeaderDashboard;
	}

	public String getCorFontHeaderDashboard() {
		if (corFontHeaderDashboard == null) {
			corFontHeaderDashboard = "858796";
		}
		return corFontHeaderDashboard;
	}

	public void setCorFontHeaderDashboard(String corFontHeaderDashboard) {
		this.corFontHeaderDashboard = corFontHeaderDashboard;
	}

	public String getCorBackTopo() {
		if (corBackTopo == null) {
			corBackTopo = "fff";
		}
		return corBackTopo;
	}

	public void setCorBackTopo(String corBackTopo) {
		this.corBackTopo = corBackTopo;
	}

	public String getCorFonteTopo() {
		if (corFonteTopo == null) {
			corFonteTopo = "5a5c69";
		}
		return corFonteTopo;
	}

	public void setCorFonteTopo(String corFonteTopo) {
		this.corFonteTopo = corFonteTopo;
	}

	public String getCorBackAreaConteudo() {
		if (corBackAreaConteudo == null) {
			corBackAreaConteudo = "fff";
		}
		return corBackAreaConteudo;
	}

	public void setCorBackAreaConteudo(String corBackAreaConteudo) {
		this.corBackAreaConteudo = corBackAreaConteudo;
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

	public List<ArquivoVO> getBannerLoginExcluirVOs() {
		if (bannerLoginExcluirVOs == null) {
			bannerLoginExcluirVOs = new ArrayList<ArquivoVO>(0);
		}
		return bannerLoginExcluirVOs;
	}

	public void setBannerLoginExcluirVOs(List<ArquivoVO> bannerLoginExcluirVOs) {
		this.bannerLoginExcluirVOs = bannerLoginExcluirVOs;
	}

	public String getScriptCssGerado() {
		if (scriptCssGerado == null) {
			scriptCssGerado = "";
		}
		return scriptCssGerado;
	}

	public void setScriptCssGerado(String scriptCssGerado) {
		this.scriptCssGerado = scriptCssGerado;
	}

	public String getCorFonteTituloFormulario() {
		if (corFonteTituloFormulario == null) {
			corFonteTituloFormulario = "424242";
		}
		return corFonteTituloFormulario;
	}

	public void setCorFonteTituloFormulario(String corFonteTituloFormulario) {
		this.corFonteTituloFormulario = corFonteTituloFormulario;
	}

	public String getCorFonteTituloCampos() {
		if (corFonteTituloCampos == null) {
			corFonteTituloCampos = "929599";
		}
		return corFonteTituloCampos;
	}

	public void setCorFonteTituloCampos(String corFonteTituloCampos) {
		this.corFonteTituloCampos = corFonteTituloCampos;
	}

	public String getCorBackCampos() {
		if (corBackCampos == null) {
			corBackCampos = "";
		}
		return corBackCampos;
	}

	public void setCorBackCampos(String corBackCampos) {
		this.corBackCampos = corBackCampos;
	}

	public String getCorFonteHeaderTabela() {
		if (corFonteHeaderTabela == null) {
			corFonteHeaderTabela = "343a38";
		}
		return corFonteHeaderTabela;
	}

	public void setCorFonteHeaderTabela(String corFonteHeaderTabela) {
		this.corFonteHeaderTabela = corFonteHeaderTabela;
	}

	public String getCorBackHeaderTabela() {
		if (corBackHeaderTabela == null) {
			corBackHeaderTabela = "f3f3f3";
		}
		return corBackHeaderTabela;
	}

	public void setCorBackHeaderTabela(String corBackHeaderTabela) {
		this.corBackHeaderTabela = corBackHeaderTabela;
	}

	public String getCorFonteLinhaTabela() {
		if (corFonteLinhaTabela == null) {
			corFonteLinhaTabela = "5a5c69";
		}
		return corFonteLinhaTabela;
	}

	public void setCorFonteLinhaTabela(String corFonteLinhaTabela) {
		this.corFonteLinhaTabela = corFonteLinhaTabela;
	}

	public String getCorBackLinhaTabela() {
		if (corBackLinhaTabela == null) {
			corBackLinhaTabela = "";
		}
		return corBackLinhaTabela;
	}

	public void setCorBackLinhaTabela(String corBackLinhaTabela) {
		this.corBackLinhaTabela = corBackLinhaTabela;
	}

	public String getCorFonteHeaderAbas() {
		if(corFonteHeaderAbas == null) {
			corFonteHeaderAbas =  "ffffff";
		}
		return corFonteHeaderAbas;
	}

	public void setCorFonteHeaderAbas(String corFonteHeaderAbas) {
		this.corFonteHeaderAbas = corFonteHeaderAbas;
	}

	public String getCorBackHeaderAbas() {
		if(corBackHeaderAbas == null) {
			corBackHeaderAbas =  "6c757d";
		}
		return corBackHeaderAbas;
	}

	public void setCorBackHeaderAbas(String corBackHeaderAbas) {
		this.corBackHeaderAbas = corBackHeaderAbas;
	}

	public String getCorBackGeral() {
		if (corBackGeral == null) {
			corBackGeral = "ededed";
		}
		return corBackGeral;
	}

	public void setCorBackGeral(String corBackGeral) {
		this.corBackGeral = corBackGeral;
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getTempoBanner() {
		if (tempoBanner == null) {
			tempoBanner = 1000;
		}
		return tempoBanner;
	}

	public void setTempoBanner(Integer tempoBanner) {

		this.tempoBanner = tempoBanner;
	}

	public String getLinkVideosBanner() {
		if (linkVideosBanner == null) {
			linkVideosBanner = "";
		}
		return linkVideosBanner;
	}

	public void setLinkVideosBanner(String linkVideosBanner) {
		this.linkVideosBanner = linkVideosBanner;
	}

	public Boolean getDisponibilizarUsuario() {
		if (disponibilizarUsuario == null) {
			disponibilizarUsuario = false;
		}
		return disponibilizarUsuario;
	}

	public void setDisponibilizarUsuario(Boolean disponibilizarUsuario) {
		this.disponibilizarUsuario = disponibilizarUsuario;
	}

	public String getCss() {
		if (css == null) {
			css = "";
		}
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

	public String getNomeImagemTopo() {
		if (nomeImagemTopo == null) {
			nomeImagemTopo = "";
		}
		return nomeImagemTopo;
	}

	public void setNomeImagemTopo(String nomeImagemTopo) {
		this.nomeImagemTopo = nomeImagemTopo;
	}

	public PastaBaseArquivoEnum getCaminhoBaseImagemTopo() {
		if (caminhoBaseImagemTopo == null) {
			caminhoBaseImagemTopo = PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO_TMP;
		}
		return caminhoBaseImagemTopo;
	}

	public void setCaminhoBaseImagemTopo(PastaBaseArquivoEnum caminhoBaseImagemTopo) {
		this.caminhoBaseImagemTopo = caminhoBaseImagemTopo;
	}

	public String getNomeImagemTopoAnterior() {
		if (getCaminhoBaseImagemTopo().equals(PastaBaseArquivoEnum.LOGO_UNIDADE_ENSINO)
				&& !getNomeImagemTopo().isEmpty() && nomeImagemTopoAnterior == null) {
			nomeImagemTopoAnterior = getNomeImagemTopo();
		}
		return nomeImagemTopoAnterior;
	}

	public void setNomeImagemTopoAnterior(String nomeImagemTopoAnterior) {
		this.nomeImagemTopoAnterior = nomeImagemTopoAnterior;
	}

	public String cssBackMenuSkyn;

	public String getCssBackMenuSkyn() {
		if (cssBackMenuSkyn == null) {
			if (!getCorBackMenu().contains("gradient") && !getCorBackMenu().contains("#")) {
				cssBackMenuSkyn = "background-color:#" + getCorBackMenu() + " !important;";
			}
			if (!getCorBackMenu().contains("gradient")) {
				cssBackMenuSkyn = "background-color:#" + getCorBackMenu() + " !important;";

			} else {
				cssBackMenuSkyn = "background-image:" + getCorBackMenu() + " !important;";
			}
		}
		return cssBackMenuSkyn;
	}

}
