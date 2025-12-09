package controle.administrativo;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.LoginControle;
import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.NovidadeSeiVO;
import negocio.comuns.arquitetura.enumeradores.TipoNovidadeEnum;
import negocio.comuns.utilitarias.Uteis;

@Controller("NovidadeSeiControle")
@Scope("session")
public class NovidadeSeiControle extends SuperControle {

	private static final long serialVersionUID = 1L;	
	private Integer contagemNovidadesNaoVisualizadas;
	private DataModelo novidadeNaoVisualizado;
	private DataModelo novidadeLive;
	private DataModelo novidadeComunicado;
	private DataModelo novidadeDestaque;

	public NovidadeSeiControle() {
		super();
		getControleConsultaOtimizado().setLimitePorPagina(4);
		getControleConsultaOtimizado().setPaginaAtual(1);
		getControleConsultaOtimizado().setPage(0);
		// this.selecionarNovidade();
	}

	private void atualizarNotificacaoNovidadeSei() {
		LoginControle loginControle = (LoginControle) getControlador("LoginControle");
		if (loginControle != null && loginControle.getExisteNovidadeSei()) {
			loginControle.setExisteNovidadeSei(null);
		}
	}
	
	public void inicializarConsultaNovidade() {
		getControleConsultaOtimizado().setLimitePorPagina(6);
		getControleConsultaOtimizado().setPaginaAtual(1);
		getControleConsultaOtimizado().setPage(0);
		consultarNovidade(false);
		consultarLive();
		consultarComunicado();
	}
	
	public void consultarLive() {
		try {
			getNovidadeLive().setListaConsulta(getFacadeFactory().getNovidadeSeiFacade().consultarNovidades(getNovidadeLive().getLimitePorPagina(), getNovidadeLive().getOffset(), false, TipoNovidadeEnum.LIVE, getNovidadeLive().getValorConsulta(), false, getUsuarioLogado()));
			getNovidadeLive().setTotalRegistrosEncontrados(getFacadeFactory().getNovidadeSeiFacade().consultarTotalRegistroNovidades(false, TipoNovidadeEnum.LIVE, getNovidadeLive().getValorConsulta(), getUsuarioLogado()));
			((List<NovidadeSeiVO>) getNovidadeLive().getListaConsulta()).forEach(t -> {
				if(!t.getVisualizado()) {
				getFacadeFactory().getNovidadeSeiFacade().registrarVisualizacao(t, getUsuarioLogado());
				}				
			});			
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			e.printStackTrace();
		}
	}
	
	public void consultarComunicado() {
		try {
			getNovidadeComunicado().setListaConsulta(getFacadeFactory().getNovidadeSeiFacade().consultarNovidades(getNovidadeComunicado().getLimitePorPagina(), getNovidadeComunicado().getOffset(), false, TipoNovidadeEnum.COMUNICADO, getNovidadeComunicado().getValorConsulta(), false, getUsuarioLogado()));
			getNovidadeComunicado().setTotalRegistrosEncontrados(getFacadeFactory().getNovidadeSeiFacade().consultarTotalRegistroNovidades(false, TipoNovidadeEnum.COMUNICADO, getNovidadeComunicado().getValorConsulta(), getUsuarioLogado()));
			((List<NovidadeSeiVO>) getNovidadeComunicado().getListaConsulta()).forEach(t -> {
				if(!t.getVisualizado()) {
				getFacadeFactory().getNovidadeSeiFacade().registrarVisualizacao(t, getUsuarioLogado());
				}				
			});			
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			e.printStackTrace();
		}
	}


	public void consultarNovidade(boolean trazerApenasNaoVisualizadas) {
		try {
			if(!trazerApenasNaoVisualizadas) {
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getNovidadeSeiFacade().consultarNovidades(getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), trazerApenasNaoVisualizadas, TipoNovidadeEnum.NEWS, getControleConsultaOtimizado().getValorConsulta(), false, getUsuarioLogado()));
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getNovidadeSeiFacade().consultarTotalRegistroNovidades(trazerApenasNaoVisualizadas, TipoNovidadeEnum.NEWS, getControleConsultaOtimizado().getValorConsulta(), getUsuarioLogado()));
				((List<NovidadeSeiVO>) getControleConsultaOtimizado().getListaConsulta()).forEach(t -> {
					if(!t.getVisualizado()) {
					getFacadeFactory().getNovidadeSeiFacade().registrarVisualizacao(t, getUsuarioLogado());
					}				
				});
			}else {
				getNovidadeNaoVisualizado().setListaConsulta(getFacadeFactory().getNovidadeSeiFacade().consultarNovidades(1, 0, trazerApenasNaoVisualizadas, null, null, false, getUsuarioLogado()));
				getNovidadeNaoVisualizado().setTotalRegistrosEncontrados(getFacadeFactory().getNovidadeSeiFacade().consultarTotalRegistroNovidades(trazerApenasNaoVisualizadas, null, null, getUsuarioLogado()));
				((List<NovidadeSeiVO>) getNovidadeNaoVisualizado().getListaConsulta()).forEach(t -> {
					if(!t.getVisualizado()) {
					getFacadeFactory().getNovidadeSeiFacade().registrarVisualizacao(t, getUsuarioLogado());
					}				
				});
			}
			atualizarContagemNovidades();				
			atualizarNotificacaoNovidadeSei();
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			e.printStackTrace();
		}
	}

	public void paginarLista(DataScrollEvent event) {
		getControleConsultaOtimizado().setPage(event.getPage());
		getControleConsultaOtimizado().setPaginaAtual(event.getPage());
		consultarNovidade(false);
	}
	
	public void paginarLive(DataScrollEvent event) {
		getNovidadeLive().setPage(event.getPage());
		getNovidadeLive().setPaginaAtual(event.getPage());
		consultarLive();
	}
	
	public void paginarComunicado(DataScrollEvent event) {
		getNovidadeComunicado().setPage(event.getPage());
		getNovidadeComunicado().setPaginaAtual(event.getPage());
		consultarComunicado();
	}
	
	public void paginarListaNaoVisualizado(DataScrollEvent event) {
		getNovidadeNaoVisualizado().setPage(event.getPage());
		getNovidadeNaoVisualizado().setPaginaAtual(event.getPage());
		consultarNovidade(true);
	}
	
    public Integer getContagemNovidadesNaoVisualizadas() {
    	if (contagemNovidadesNaoVisualizadas == null) {
    		contagemNovidadesNaoVisualizadas = 0;
    	}
    	return contagemNovidadesNaoVisualizadas;
    }
    
	public void setContagemNovidadesNaoVisualizadas(Integer contagemNovidadesNaoVisualizadas) {
		this.contagemNovidadesNaoVisualizadas = contagemNovidadesNaoVisualizadas;
	}
	
	@PostConstruct
	public void atualizarContagemNovidades() {		
		setContagemNovidadesNaoVisualizadas(getFacadeFactory().getNovidadeSeiFacade().realizarContagemNovidadeSemVisualizacaoUsuario(getUsuarioLogado().getCodigo()));
		
	}

	public DataModelo getNovidadeNaoVisualizado() {
		if(novidadeNaoVisualizado == null) {
			novidadeNaoVisualizado =  new DataModelo();
			novidadeNaoVisualizado.setLimitePorPagina(1);
			novidadeNaoVisualizado.setPaginaAtual(1);
			novidadeNaoVisualizado.setPage(0);
		}
		return novidadeNaoVisualizado;
	}

	public void setNovidadeNaoVisualizado(DataModelo novidadeNaoVisualizado) {
		this.novidadeNaoVisualizado = novidadeNaoVisualizado;
	}

	public DataModelo getNovidadeLive() {
		if(novidadeLive == null) {
			novidadeLive =  new DataModelo();
			novidadeLive.setLimitePorPagina(6);
			novidadeLive.setPaginaAtual(1);
			novidadeLive.setPage(0);
		}
		return novidadeLive;
	}

	public void setNovidadeLive(DataModelo novidadeLive) {
		this.novidadeLive = novidadeLive;
	}

	public DataModelo getNovidadeComunicado() {
		if(novidadeComunicado == null) {
			novidadeComunicado =  new DataModelo();
			novidadeComunicado.setLimitePorPagina(6);
			novidadeComunicado.setPaginaAtual(1);
			novidadeComunicado.setPage(0);
		}
		return novidadeComunicado;
	}

	public void setNovidadeComunicado(DataModelo novidadeComunicado) {
		this.novidadeComunicado = novidadeComunicado;
	}
	
	
	public void consultarNovidadeDestaque() {
		try {
			if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				getNovidadeDestaque().setListaConsulta(getFacadeFactory().getNovidadeSeiFacade().consultarNovidades(0, 0, false, null, null, true, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			e.printStackTrace();
		}
	}

	public DataModelo getNovidadeDestaque() {
		if(novidadeDestaque == null) {
			novidadeDestaque =  new DataModelo();
			consultarNovidadeDestaque();
		}
		return novidadeDestaque;
	}

	public void setNovidadeDestaque(DataModelo novidadeDestaque) {
		this.novidadeDestaque = novidadeDestaque;
	}
	
	public List<NovidadeSeiVO> getListaNovidadeDestaque(){
		return ((List<NovidadeSeiVO>)getNovidadeDestaque().getListaConsulta()).stream().filter(t -> (t.getDataInicioDisponibilidade() == null || t.getDataInicioDisponibilidade().compareTo(new Date()) <= 0) && (t.getDataLimiteDisponibilidade() == null || t.getDataLimiteDisponibilidade().compareTo(new Date()) >= 0)).collect(Collectors.toList());
	}
	

}
