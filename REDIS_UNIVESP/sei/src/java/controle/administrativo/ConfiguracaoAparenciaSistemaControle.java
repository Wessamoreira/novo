package controle.administrativo;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("ConfiguracaoAparenciaSistemaControle")
@Scope("session")
public class ConfiguracaoAparenciaSistemaControle extends SuperControle {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9200586218195987819L;
	private ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO;
		

	public ConfiguracaoAparenciaSistemaVO getConfiguracaoAparenciaSistemaVO() {
		if(configuracaoAparenciaSistemaVO == null) {
			configuracaoAparenciaSistemaVO =  new ConfiguracaoAparenciaSistemaVO();
		}
		return configuracaoAparenciaSistemaVO;
	}

	public void setConfiguracaoAparenciaSistemaVO(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO) {
		this.configuracaoAparenciaSistemaVO = configuracaoAparenciaSistemaVO;
	}
	
	public void persistir() {
		try {
			getFacadeFactory().getConfiguracaoAparenciaSistemaFacade().persistir(getConfiguracaoAparenciaSistemaVO(),getUsuarioLogado());
			getAplicacaoControle().setConfiguracaoAparenciaSistemaVOs(null);			
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void adicionarBanner(FileUploadEvent upload) {
		try {
			getFacadeFactory().getConfiguracaoAparenciaSistemaFacade().adicionarBanner(getConfiguracaoAparenciaSistemaVO(), upload, getUsuarioLogado());
			setMensagemID("msg_dados_adicionados", Uteis.ALERTA);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void removerBanner() {
		try {
			getFacadeFactory().getConfiguracaoAparenciaSistemaFacade().removerBanner(getConfiguracaoAparenciaSistemaVO(), (ArquivoVO)getRequestMap().get("banner"), getUsuarioLogado());
			setMensagemID("msg_dados_removidos", Uteis.ALERTA);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void excluir() {
		try {
			getFacadeFactory().getConfiguracaoAparenciaSistemaFacade().excluir(getConfiguracaoAparenciaSistemaVO(),getUsuarioLogado());
			setConfiguracaoAparenciaSistemaVO(new ConfiguracaoAparenciaSistemaVO());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public String novo() {
		try {			
			setConfiguracaoAparenciaSistemaVO(new ConfiguracaoAparenciaSistemaVO());
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoAparenciaSistemaForm.xhtml");
	}
	
	public String editar() {
		try {			
			setConfiguracaoAparenciaSistemaVO(getFacadeFactory().getConfiguracaoAparenciaSistemaFacade().consultarPorChavePrimaria(((ConfiguracaoAparenciaSistemaVO)getRequestMap().get("configuracaoAparenciaSistemaItem")).getCodigo(), true, getUsuarioLogado()));
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoAparenciaSistemaForm.xhtml");
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoAparenciaSistemaCons.xhtml");
		}
	}
	
	public String consultar() {
		try {			
			setListaConsulta(getFacadeFactory().getConfiguracaoAparenciaSistemaFacade().consultar(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), NivelMontarDados.COMBOBOX, true, getUsuarioLogado()));
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		}catch (Exception e) {
			Uteis.liberarListaMemoria(getListaConsulta());
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoAparenciaSistemaCons.xhtml");
	}
	
	
	
	public void realizarPreVisualizacaoLogin() {
		try {
			getFacadeFactory().getConfiguracaoAparenciaSistemaFacade().realizarGeracaoScriptBannerLogin(getConfiguracaoAparenciaSistemaVO());
			getFacadeFactory().getConfiguracaoAparenciaSistemaFacade().realizarGeracaoScriptCss(getConfiguracaoAparenciaSistemaVO());
		} catch (Exception e) {
			
		}
	}
	
	public void realizarPreVisualizacaoHome() {
		getFacadeFactory().getConfiguracaoAparenciaSistemaFacade().realizarGeracaoScriptCss(getConfiguracaoAparenciaSistemaVO());
	}

	public void realizarDefinicaoPadraoSistema() {
		getFacadeFactory().getConfiguracaoAparenciaSistemaFacade().realizarDefinicaoPadraoSistema(getConfiguracaoAparenciaSistemaVO());
	}
	
	

	public void uploadLogoTopo(FileUploadEvent event) {
		try {
			getFacadeFactory().getConfiguracaoAparenciaSistemaFacade().uploadLogoTopo(getConfiguracaoAparenciaSistemaVO(), event, getUsuarioLogado());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);		
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
	}
	
	
	public void excluirLogoTopo() {
		try {
			getFacadeFactory().getConfiguracaoAparenciaSistemaFacade().excluirLogoTopo(getConfiguracaoAparenciaSistemaVO(), getUsuarioLogado());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);		
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
	}
	
	public String getUrlLogoUnidadeEnsino() {
		if(Uteis.isAtributoPreenchido(getConfiguracaoAparenciaSistemaVO().getNomeImagemTopo())) {
			try {
				return getConfiguracaoGeralSistemaVO().getUrlAcessoExternoAplicacao()+"/"+getConfiguracaoAparenciaSistemaVO().getCaminhoBaseImagemTopo().getValue()+"/"+getConfiguracaoAparenciaSistemaVO().getNomeImagemTopo();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		return getLoginControle().getUrlLogoUnidadeEnsino();
	}
	
	
}
