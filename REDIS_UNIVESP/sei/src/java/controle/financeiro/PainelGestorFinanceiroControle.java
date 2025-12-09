package controle.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoModuloEnum;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.EstatisticaContaPagarReceberVO;
import negocio.comuns.financeiro.EstatisticasLancamentosFuturosVO;
import negocio.comuns.financeiro.MapaLancamentoFuturoVO;
import negocio.comuns.utilitarias.DashboardVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoDashboardEnum;
import negocio.comuns.utilitarias.dominios.TipoMapaLancamentoFuturo;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
/**
 * 
 * @author Rodrigo
 */

@Controller("PainelGestorFinanceiroControle")
@Scope("session")
@Lazy
public class PainelGestorFinanceiroControle extends SuperControle implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8775651062404518436L;
	private List<EstatisticaContaPagarReceberVO> estatisticaContaPagarReceberVOs;
    private List<EstatisticasLancamentosFuturosVO> estatisticasLancamentosFuturosVOs;
    private DashboardVO dashBoardPainelFinanceiro;
    
    

    public PainelGestorFinanceiroControle() {
       

    }
    
    

    public DashboardVO getDashBoardPainelFinanceiro() {
    
		return dashBoardPainelFinanceiro;
	}



	public void setDashBoardPainelFinanceiro(DashboardVO dashBoardPainelFinanceiro) {
		this.dashBoardPainelFinanceiro = dashBoardPainelFinanceiro;
	}



	@PostConstruct
    public void inicializarDadosPainelGestorFinanceiro() {
//    	 consultarContas();
//         consultarMapasLancamentosFuturos();
    	if(getPerfilAcessoPainelLancamentosPendentes()) {
    		if (!getLoginControle().getMapDashboards().containsKey(TipoDashboardEnum.LANCAMENTOS_FINANCEIROS_PENDENTE.name())) {
    			dashBoardPainelFinanceiro = new DashboardVO(TipoDashboardEnum.LANCAMENTOS_FINANCEIROS_PENDENTE, false, 4,
						TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.ADMINISTRATIVO, getUsuarioLogado());
				getLoginControle().getMapDashboards().put(TipoDashboardEnum.LANCAMENTOS_FINANCEIROS_PENDENTE.name(),
						dashBoardPainelFinanceiro);
			} else {
				dashBoardPainelFinanceiro = getLoginControle().getMapDashboards()
						.get(TipoDashboardEnum.LANCAMENTOS_FINANCEIROS_PENDENTE.name());
			}
			getDashBoardPainelFinanceiro().setUsuarioVO(getUsuarioLogadoClone());
			getDashBoardPainelFinanceiro().setUnidadeEnsinoVO(getUnidadeEnsinoLogadoClone());
			
			getDashBoardPainelFinanceiro().iniciar(1l, 2, "Carregando..", true, this, "consultarMapasLancamentosFuturos");
			getDashBoardPainelFinanceiro().iniciarAssincrono();
		} else {
			if (getLoginControle().getMapDashboards().containsKey(TipoDashboardEnum.LANCAMENTOS_FINANCEIROS_PENDENTE.name())) {
				getLoginControle().getMapDashboards().remove(TipoDashboardEnum.LANCAMENTOS_FINANCEIROS_PENDENTE.name());
			}
		}
    }
    
    
    public void consultarMapasLancamentosFuturos() {
        try {
        	setEstatisticasLancamentosFuturosVOs(getFacadeFactory().getMapaLancamentoFuturoFacade().consultarEstatisticasLancamentosFuturosVO(getDashBoardPainelFinanceiro().getUnidadeEnsinoVO(), getDashBoardPainelFinanceiro().getUsuarioVO()));            
        } catch (Exception e) {
        	
        }finally {
        	getDashBoardPainelFinanceiro().incrementar();
        	getDashBoardPainelFinanceiro().encerrar();
        }
    }

    public boolean getPerfilAcessoPainelContasPagar() {
        try {
            ControleAcesso.consultar("PainelGestorContasPagar", true, getUsuarioLogado());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean getPerfilAcessoPainelContasReceber() {
        try {
            ControleAcesso.consultar("PainelGestorContasReceber", true, getUsuarioLogado());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean perfilAcessoPainelLancamentosPendentes;
    public Boolean getPerfilAcessoPainelLancamentosPendentes() {
    	if(perfilAcessoPainelLancamentosPendentes == null) {
        try {
            ControleAcesso.consultar("PainelGestorLancamentosPendentes", true, getUsuarioLogado());
            perfilAcessoPainelLancamentosPendentes = true;
        } catch (Exception e) {
        	perfilAcessoPainelLancamentosPendentes = false;
        }
    	}
    	return perfilAcessoPainelLancamentosPendentes;
    }

    
    public void consultarLancamentosPendentesAnterior() {
        try {
            EstatisticasLancamentosFuturosVO estatisticas = (EstatisticasLancamentosFuturosVO) context().getExternalContext().getRequestMap().get("linhaEstatisticas");
            consultarLancamentosPendentes(estatisticas, null, new Date(), null, Uteis.getDataPassada(new Date(), 0));
        } catch (Exception e) {
            //System.out.println(" Painel Gestor Financeiro Erro consultarContaPagarVencidas:" + e.getMessage());
        }
    }

    public void consultarLancamentosPendentesHoje() {
        try {
            EstatisticasLancamentosFuturosVO estatisticas = (EstatisticasLancamentosFuturosVO) context().getExternalContext().getRequestMap().get("linhaEstatisticas");
            consultarLancamentosPendentes(estatisticas, null, new Date(), new Date(), new Date());
        } catch (Exception e) {
            //System.out.println(" Painel Gestor Financeiro Erro consultarContaPagarVencer:" + e.getMessage());
        }
    }

    public void consultarLancamentosPendentesFuturo() {
        try {
            EstatisticasLancamentosFuturosVO estatisticas = (EstatisticasLancamentosFuturosVO) context().getExternalContext().getRequestMap().get("linhaEstatisticas");
            consultarLancamentosPendentes(estatisticas, null, new Date(), Uteis.obterDataFutura(new Date(), 2), null);
        } catch (Exception e) {
            //System.out.println(" Painel Gestor Financeiro Erro consultarContaPagar:" + e.getMessage());
        }
    }

    public void consultarLancamentosPendentes(EstatisticasLancamentosFuturosVO estatisticas, Date dataEmissao, Date dataEmissaoFinal, Date dataPrevisao, Date dataPrevisaoFinal) throws Exception {
        MapaLancamentoFuturoControle mapaLancamentoFuturoControle = (MapaLancamentoFuturoControle) context().getExternalContext().getSessionMap().get("MapaLancamentoFuturoControle");
        if (mapaLancamentoFuturoControle == null) {
            mapaLancamentoFuturoControle = new MapaLancamentoFuturoControle();
            mapaLancamentoFuturoControle.setMapaLancamentoFuturoVO(new MapaLancamentoFuturoVO());
            mapaLancamentoFuturoControle.getMapaLancamentoFuturoVO().setDataEmissao(dataEmissao);
            mapaLancamentoFuturoControle.getMapaLancamentoFuturoVO().setDataEmissaoFinal(dataEmissaoFinal);
            mapaLancamentoFuturoControle.getMapaLancamentoFuturoVO().setDataPrevisao(dataPrevisao);
            mapaLancamentoFuturoControle.getMapaLancamentoFuturoVO().setDataPrevisaoFinal(dataPrevisaoFinal);
            if (estatisticas.getTipoMapaLancamento().getMetodoReferenteAoTipo().equals("abaChequesAPagar")) {
                mapaLancamentoFuturoControle.abaChequesAPagar();
            } else {
                mapaLancamentoFuturoControle.abaChequesAReceber();
            }
            context().getExternalContext().getSessionMap().put("MapaLancamentoFuturoControle", mapaLancamentoFuturoControle);
        } else {
            mapaLancamentoFuturoControle.setMapaLancamentoFuturoVO(new MapaLancamentoFuturoVO());
            mapaLancamentoFuturoControle.getMapaLancamentoFuturoVO().setDataEmissao(dataEmissao);
            mapaLancamentoFuturoControle.getMapaLancamentoFuturoVO().setDataEmissaoFinal(dataEmissaoFinal);
            mapaLancamentoFuturoControle.getMapaLancamentoFuturoVO().setDataPrevisao(dataPrevisao);
            mapaLancamentoFuturoControle.getMapaLancamentoFuturoVO().setDataPrevisaoFinal(dataPrevisaoFinal);
            if (estatisticas.getTipoMapaLancamento().getMetodoReferenteAoTipo().equals("abaChequesAPagar")) {
                mapaLancamentoFuturoControle.abaChequesAPagar();
            } else {
                mapaLancamentoFuturoControle.abaChequesAReceber();
            }
            context().getExternalContext().getSessionMap().put("MapaLancamentoFuturoControle", mapaLancamentoFuturoControle);
        }
    }

//    public void consultarPendenciaFinanceira() {
//        try {
//            EstatisticasLancamentosFuturosVO estatisticas = (EstatisticasLancamentosFuturosVO) context().getExternalContext().getRequestMap().get("linhaEstatisticas");
//            executarMetodoControle(MapaLancamentoFuturoControle.class.getSimpleName(), estatisticas.getTipoMapaLancamento().getMetodoReferenteAoTipo());
//        } catch (Exception e) {
//        }
//    }
    public boolean isMostrarPainelLancamentosPendentes() {
        return getEstatisticasLancamentosFuturosVOs() != null && getEstatisticasLancamentosFuturosVOs().size() != 0;
    }

    public EstatisticasLancamentosFuturosVO somarEstatisticaNoTempoCerto(EstatisticasLancamentosFuturosVO estatistica, MapaLancamentoFuturoVO mapa) {
        //int comparacao = Uteis.getData(mapa.getDataPrevisao()) Uteis.getDataJDBC(new Date()));

        if (Uteis.getData(mapa.getDataPrevisao(), "dd-MM-yyyy").equals(Uteis.getData(new Date(), "dd-MM-yyyy"))) {
            estatistica.setQtdHoje(estatistica.getQtdHoje() + 1);
        } else if (Uteis.getDataJDBC(mapa.getDataPrevisao()).before(Uteis.getDataJDBC(new Date()))) {
            estatistica.setQtdAnterior(estatistica.getQtdAnterior() + 1);
        } else if (Uteis.getDataJDBC(mapa.getDataPrevisao()).after(Uteis.getDataJDBC(new Date()))) {
            estatistica.setQtdFuturos(estatistica.getQtdFuturos() + 1);
        }

        return estatistica;
    }

    public void consultarContas() {
        try {
            //setEstatisticaContaPagarReceberVOs(getFacadeFactory().getContaPagarFacade().consultarValoresEstatisticaContaPagarReceber(new Date(), getUnidadeEnsinoLogado().getCodigo()));
            setEstatisticaContaPagarReceberVOs(null);
        } catch (Exception e) {
            //System.out.println(" Painel Gestor Financeiro Erro:" + e.getMessage());
        }
    }

    public void consultarContaPagarVencidas() {
        try {
            EstatisticaContaPagarReceberVO obj = (EstatisticaContaPagarReceberVO) context().getExternalContext().getRequestMap().get("estatisticaContaPagarReceber");
            consultarContaPagar(obj.getUnidadeEnsino(), "VE");
        } catch (Exception e) {
            //System.out.println(" Painel Gestor Financeiro Erro consultarContaPagarVencidas:" + e.getMessage());
        }
    }

    public void consultarContaPagarVencer() {
        try {
            EstatisticaContaPagarReceberVO obj = (EstatisticaContaPagarReceberVO) context().getExternalContext().getRequestMap().get("estatisticaContaPagarReceber");
            consultarContaPagar(obj.getUnidadeEnsino(), "AV");
        } catch (Exception e) {
            //System.out.println(" Painel Gestor Financeiro Erro consultarContaPagarVencer:" + e.getMessage());
        }
    }

    public void consultarContaPagar() {
        try {
            EstatisticaContaPagarReceberVO obj = (EstatisticaContaPagarReceberVO) context().getExternalContext().getRequestMap().get("estatisticaContaPagarReceber");
            consultarContaPagar(obj.getUnidadeEnsino(), "VH");
        } catch (Exception e) {
            //System.out.println(" Painel Gestor Financeiro Erro consultarContaPagar:" + e.getMessage());
        }
    }

    public void consultarContaPagar(Integer unidadeEnsino, String situacao) throws Exception {
//        List listaConsultas = getFacadeFactory().getContaPagarFacade().consultarContaPagarPorSituacaoUnidadeEnsino(unidadeEnsino, situacao, new Date(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
        List listaConsultas = getFacadeFactory().getContaPagarFacade().consultaRapidaContaPagarPorSituacaoUnidadeEnsino(unidadeEnsino, situacao, new Date(), 10, 0, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
        Integer totalRegistros = getFacadeFactory().getContaPagarFacade().consultaRapidaContaPagarPorSituacaoUnidadeEnsinoTotalRegistros(unidadeEnsino, situacao, new Date());
        ContaPagarControle contaPagarControle = (ContaPagarControle) context().getExternalContext().getSessionMap().get("ContaPagarControle");
        if (contaPagarControle == null) {
            contaPagarControle = new ContaPagarControle();
            contaPagarControle.getControleConsultaOtimizado().setTotalRegistrosEncontrados(totalRegistros);
            contaPagarControle.getControleConsultaOtimizado().setListaConsulta(listaConsultas);
            contaPagarControle.getControleConsultaOtimizado().setLimitePorPagina(10);
            contaPagarControle.getControleConsultaOtimizado().setPaginaAtual(1);
            contaPagarControle.getControleConsultaOtimizado().setPage(1);
            contaPagarControle.setConsultaDataScroller(false);
            contaPagarControle.setConsultaPainelGestorFinanceiro(true);
            contaPagarControle.setValorConsultaUnidadeEnsino(unidadeEnsino);
            contaPagarControle.setSituacaoConsultaPainelGestorFinanceiro(situacao);
            contaPagarControle.calcularTotalPagarTotalPago();
            context().getExternalContext().getSessionMap().put("ContaPagarControle", contaPagarControle);
        } else {
            contaPagarControle.getControleConsultaOtimizado().setTotalRegistrosEncontrados(totalRegistros);
            contaPagarControle.getControleConsultaOtimizado().setListaConsulta(listaConsultas);
            contaPagarControle.getControleConsultaOtimizado().setLimitePorPagina(10);
            contaPagarControle.getControleConsultaOtimizado().setPaginaAtual(1);
            contaPagarControle.getControleConsultaOtimizado().setPage(1);
            contaPagarControle.setConsultaDataScroller(false);
            contaPagarControle.setConsultaPainelGestorFinanceiro(true);
            contaPagarControle.setValorConsultaUnidadeEnsino(unidadeEnsino);
            contaPagarControle.setSituacaoConsultaPainelGestorFinanceiro(situacao);
            contaPagarControle.calcularTotalPagarTotalPago();
            context().getExternalContext().getSessionMap().put("ContaPagarControle", contaPagarControle);
        }
    }

    public void consultarContaReceberVencidas() {
        try {
            EstatisticaContaPagarReceberVO obj = (EstatisticaContaPagarReceberVO) context().getExternalContext().getRequestMap().get("estatisticaContaPagarReceber");
            consultarContaReceber(obj.getUnidadeEnsino(), "VE");
        } catch (Exception e) {
        }
    }

    public void consultarContaReceberVencer() {
        try {
            EstatisticaContaPagarReceberVO obj = (EstatisticaContaPagarReceberVO) context().getExternalContext().getRequestMap().get("estatisticaContaPagarReceber");
            consultarContaReceber(obj.getUnidadeEnsino(), "AV");
        } catch (Exception e) {
        }
    }

    public void consultarContaReceber() {
        try {
            EstatisticaContaPagarReceberVO obj = (EstatisticaContaPagarReceberVO) context().getExternalContext().getRequestMap().get("estatisticaContaPagarReceber");
            consultarContaReceber(obj.getUnidadeEnsino(), "VH");
        } catch (Exception e) {
        }
    }

    public void consultarContaReceber(Integer unidadeEnsino, String situacao) throws Exception {
//        List listaConsultas = getFacadeFactory().getContaReceberFacade().consultarContaReceberPorSituacaoUnidadeEnsino(unidadeEnsino, situacao, new Date(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
        List listaConsultas = getFacadeFactory().getContaReceberFacade().consultaRapidaPorSituacaoUnidadeEnsino(unidadeEnsino, situacao, new Date(), 10, 0, "", false, getUsuarioLogado(), new ArrayList<String>());
        Integer totalRegistros = getFacadeFactory().getContaReceberFacade().consultaRapidaPorSituacaoUnidadeEnsinoTotalRegistros(unidadeEnsino, situacao, new Date(), "", false, getUsuarioLogado(), new ArrayList<String>());
        ContaReceberControle contaReceberControle = (ContaReceberControle) context().getExternalContext().getSessionMap().get("ContaReceberControle");
        if (contaReceberControle == null) {
            contaReceberControle = new ContaReceberControle();
            contaReceberControle.getControleConsultaOtimizado().setTotalRegistrosEncontrados(totalRegistros);
            contaReceberControle.getControleConsultaOtimizado().setListaConsulta(listaConsultas);
            contaReceberControle.getControleConsultaOtimizado().setLimitePorPagina(10);
            contaReceberControle.getControleConsultaOtimizado().setPaginaAtual(1);
            contaReceberControle.getControleConsultaOtimizado().setPage(1);
            contaReceberControle.setConsultaDataScroller(false);
            contaReceberControle.setConsultaPainelGestorFinanceiro(true);
            contaReceberControle.setValorConsultaUnidadeEnsino(unidadeEnsino);
            contaReceberControle.setSituacaoConsultaPainelGestorFinanceiro(situacao);
            contaReceberControle.calcularTotalReceberTotalRecebido();
            context().getExternalContext().getSessionMap().put("ContaReceberControle", contaReceberControle);
        } else {
            contaReceberControle.getControleConsultaOtimizado().setTotalRegistrosEncontrados(totalRegistros);
            contaReceberControle.getControleConsultaOtimizado().setListaConsulta(listaConsultas);
            contaReceberControle.getControleConsultaOtimizado().setLimitePorPagina(10);
            contaReceberControle.getControleConsultaOtimizado().setPaginaAtual(1);
            contaReceberControle.getControleConsultaOtimizado().setPage(1);
            contaReceberControle.setConsultaDataScroller(false);
            contaReceberControle.setConsultaPainelGestorFinanceiro(true);
            contaReceberControle.setValorConsultaUnidadeEnsino(unidadeEnsino);
            contaReceberControle.setSituacaoConsultaPainelGestorFinanceiro(situacao);
            contaReceberControle.calcularTotalReceberTotalRecebido();
            context().getExternalContext().getSessionMap().put("ContaReceberControle", contaReceberControle);
        }
    }

    public List<EstatisticaContaPagarReceberVO> getEstatisticaContaPagarReceberVOs() {
        if (estatisticaContaPagarReceberVOs == null) {
            estatisticaContaPagarReceberVOs = new ArrayList<EstatisticaContaPagarReceberVO>(0);
        }
        return estatisticaContaPagarReceberVOs;
    }

    public void setEstatisticaContaPagarReceberVOs(List<EstatisticaContaPagarReceberVO> estatisticaContaPagarReceberVOs) {
        this.estatisticaContaPagarReceberVOs = estatisticaContaPagarReceberVOs;
    }

    public List<EstatisticasLancamentosFuturosVO> getEstatisticasLancamentosFuturosVOs() {
        return estatisticasLancamentosFuturosVOs;
    }

    public void setEstatisticasLancamentosFuturosVOs(List<EstatisticasLancamentosFuturosVO> estatisticasLancamentosFuturosVOs) {
        this.estatisticasLancamentosFuturosVOs = estatisticasLancamentosFuturosVOs;
    }
}
