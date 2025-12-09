/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.academico;

/**
 *
 * @author Carlos
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import controle.protocolo.RequerimentoControle;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoModuloEnum;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;
import negocio.comuns.protocolo.EstatisticaRequerimentoVO;
import negocio.comuns.utilitarias.DashboardVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoDashboardEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.arquitetura.Dashboard;

@Controller("PainelGestorAcademicoControle")
@Scope("session")
@Lazy
public class PainelGestorAcademicoControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4863162808806461816L;
	private List<EstatisticaRequerimentoVO> listaRequerimentoPendente;
	private Integer totalListaRequerimento;
	private String situacaoReq;
	private String dadosGrafico;
	private DashboardVO dashBoardRequerimento;

	public DashboardVO getDashBoardRequerimento() {
		return dashBoardRequerimento;
	}

	public void setDashBoardRequerimento(DashboardVO dashBoardRequerimento) {
		this.dashBoardRequerimento = dashBoardRequerimento;
	}

	public PainelGestorAcademicoControle() throws Exception {
		super();
	}

	public void consultarRequerimentosPendententes() {
		try {
			setTotalListaRequerimento(0);
			setListaRequerimentoPendente(getFacadeFactory().getRequerimentoFacade()
					.consultaRapidaRequerimentosPendentes(getUnidadeEnsinoLogado().getCodigo().intValue(), false,
							Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			for (EstatisticaRequerimentoVO estatisticaRequerimentoVO : getListaRequerimentoPendente()) {
				setTotalListaRequerimento(
						getTotalListaRequerimento() + estatisticaRequerimentoVO.getQuantidadeTipoRequerimento());
			}
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		} finally {
			if (getListaRequerimentoPendente().isEmpty()) {
				getListaRequerimentoPendente().clear();
				setListaRequerimentoPendente(null);
			}
		}
	}

	public void editarEstatisticaRequerimento() {
		EstatisticaRequerimentoVO estatisticaRequerimento = (EstatisticaRequerimentoVO) context().getExternalContext()
				.getRequestMap().get("estatisticaRequerimento");
		consultarEstatisticaRequerimento(estatisticaRequerimento);
	}

	public void consultarEstatisticaRequerimento(EstatisticaRequerimentoVO obj) {
		try {
			List lista = getFacadeFactory().getRequerimentoFacade()
					.consultaRapidaRequerimentosPorCodigoTipoRequerimento(
							getUnidadeEnsinoLogado().getCodigo().intValue(), obj.getCodigoTipoRequerimento().intValue(),
							false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(),
							getConfiguracaoGeralPadraoSistema());
			RequerimentoControle requerimentoControle = (RequerimentoControle) context().getExternalContext()
					.getSessionMap().get("RequerimentoControle");
			if (requerimentoControle == null) {
				requerimentoControle = new RequerimentoControle();
				requerimentoControle.setListaConsulta(lista);
				context().getExternalContext().getSessionMap().put("RequerimentoControle", requerimentoControle);
			} else {
				requerimentoControle.setListaConsulta(lista);
				context().getExternalContext().getSessionMap().put("RequerimentoControle", requerimentoControle);
			}
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public Boolean getIsApresentarMensagemNaoPossuiRequerimento() {
		if (getListaRequerimentoPendente().isEmpty()) {
			return true;
		}
		return false;
	}

	public boolean getPerfilAcessoPainelRequerimentos() {
		try {
			ControleAcesso.consultar("PainelGestorRequerimentosAcademico", true, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @return the listaRequerimentoPendente
	 */
	public List<EstatisticaRequerimentoVO> getListaRequerimentoPendente() {
		if (listaRequerimentoPendente == null) {
			listaRequerimentoPendente = new ArrayList(0);
		}
		return listaRequerimentoPendente;
	}

	/**
	 * @param listaRequerimentoPendente the listaRequerimentoPendente to set
	 */
	public void setListaRequerimentoPendente(List<EstatisticaRequerimentoVO> listaRequerimentoPendente) {
		this.listaRequerimentoPendente = listaRequerimentoPendente;
	}

	/**
	 * @return the totalListaRequerimento
	 */
	public Integer getTotalListaRequerimento() {
		if (totalListaRequerimento == null) {
			totalListaRequerimento = 0;
		}
		return totalListaRequerimento;
	}

	/**
	 * @param totalListaRequerimento the totalListaRequerimento to set
	 */
	public void setTotalListaRequerimento(Integer totalListaRequerimento) {
		this.totalListaRequerimento = totalListaRequerimento;
	}

	@PostConstruct
	public void inicializarDadosGraficoRequerimento() {
		try {
			if (getPerfilAcessoPainelRequerimentos()) {
				if (!getLoginControle().getMapDashboards().containsKey(TipoDashboardEnum.REQUERIMENTO.name())) {
					dashBoardRequerimento = new DashboardVO(TipoDashboardEnum.REQUERIMENTO, false, 5,
							TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.ADMINISTRATIVO, getUsuarioLogado());
					getLoginControle().getMapDashboards().put(TipoDashboardEnum.REQUERIMENTO.name(),
							dashBoardRequerimento);
				} else {
					dashBoardRequerimento = getLoginControle().getMapDashboards()
							.get(TipoDashboardEnum.REQUERIMENTO.name());
				}
				getDashBoardRequerimento().setUsuarioVO(getUsuarioLogadoClone());
				getDashBoardRequerimento().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade());
				dashBoardRequerimento.iniciar(1l, 2, "Carregando..", true, this, "consultarDadosGrafico");
				dashBoardRequerimento.iniciarAssincrono();
			} else {
				if (getLoginControle().getMapDashboards().containsKey(TipoDashboardEnum.REQUERIMENTO.name())) {
					getLoginControle().getMapDashboards().remove(TipoDashboardEnum.REQUERIMENTO.name());
				}
			}
		} catch (Exception e) {
			if(getDashBoardRequerimento() != null) {
				getDashBoardRequerimento().incrementar();
				getDashBoardRequerimento().encerrar();
			}
		}
	}

	public void consultarDadosGrafico() {
		try {

			consultarUnidadeEnsinoFiltroRelatorio();
			setDadosGrafico(getFacadeFactory().getRequerimentoFacade().consultarDadosGraficoPainelGestorRequerimento(
					getUnidadeEnsinoVOs(), "", verificarUsuarioPossuiPermissaoConsultaDadosGrafico(), false,
					getDashBoardRequerimento().getUsuarioVO(),
					getDashBoardRequerimento().getConfiguracaoGeralSistemaVO()));
			consultarRequerimentosPendententes();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getDashBoardRequerimento().incrementar();
			getDashBoardRequerimento().encerrar();
		}
	}

	public void consultarUnidadeEnsinoFiltroRelatorio() {
		try {
			getUnidadeEnsinoVOs().clear();
			if (getUnidadeEnsinoLogado().getCodigo() > 0) {
				setUnidadeEnsinoVOs(getFacadeFactory().getUnidadeEnsinoFacade()
						.consultarPorUsuario(getDashBoardRequerimento().getUsuarioVO()));
				for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
					obj.setFiltrarUnidadeEnsino(true);
				}
			} else {
				setUnidadeEnsinoVOs(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoFaltandoLista(
						getUnidadeEnsinoVOs(), Uteis.NIVELMONTARDADOS_COMBOBOX,
						getDashBoardRequerimento().getUsuarioVO()));
			}

		} catch (Exception e) {
			setListaConsultarUnidadeEnsino(new ArrayList<UnidadeEnsinoVO>(0));

		}
	}

	public String getSituacaoReq() {
		if (situacaoReq == null) {
			situacaoReq = "";
		}
		return situacaoReq;
	}

	public void setSituacaoReq(String situacaoReq) {
		this.situacaoReq = situacaoReq;
	}

	public Boolean verificarUsuarioPossuiPermissaoConsultaDadosGrafico() {
		Boolean liberar = Boolean.FALSE;
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(
					"Requerimento_consultarRequerimentoOutrosConsultoresResponsaveis", getDashBoardRequerimento().getUsuarioVO());
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(
					"Requerimento_consultarRequerimentoOutroDepartamentoResponsavel", getDashBoardRequerimento().getUsuarioVO());
			liberar = Boolean.TRUE;
		} catch (Exception e) {
			liberar = Boolean.FALSE;
		}
		return liberar;
	}

	public String getDadosGrafico() {
		if (dadosGrafico == null) {
			dadosGrafico = "";
		}
		return dadosGrafico;
	}

	public void setDadosGrafico(String dadosGrafico) {
		this.dadosGrafico = dadosGrafico;
	}

}
