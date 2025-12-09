package controle.recursoshumanos;

import java.util.ArrayList;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.faturamento.nfe.ConsistirException;
import negocio.comuns.recursoshumanos.TipoTransporteVO;
import negocio.comuns.recursoshumanos.TipoTransporteVO.EnumCampoConsultaTipoTransporte;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas tipoTransporteForm.xhtml é tipoTransporteFPCons.xhtl com as
 * funcionalidades da classe <code>TipoTransporteVO</code>.
 * Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see TipoTransporteVO
 */
@Controller("TipoTransporteControle")
@Scope("viewScope")
@Lazy
public class TipoTransporteControle extends SuperControle {

	private static final long serialVersionUID = 3217110293827957253L;

	private final String TELA_FORM = "tipoTransporteForm";
	private final String TELA_CONS = "tipoTransporteCons";
	private final String CONTEXT_PARA_EDICAO = "itemTipoTransporte";

	private TipoTransporteVO tipoTransporte;

	public TipoTransporteControle() {
		setControleConsultaOtimizado(new DataModelo());
		inicializarConsultar();
	}

	public String novo() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public String editar() {
		try {
			TipoTransporteVO obj = (TipoTransporteVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			setTipoTransporte(obj);
			setControleConsultaOtimizado(new DataModelo());
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void persistir() {
		try {
			getFacadeFactory().getTipoTransporteInterfaceFacade().persistir(getTipoTransporte(), true, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListener(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultarDados();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	@Override
	public void consultarDados() {
		try {

			if (getControleConsultaOtimizado().getCampoConsulta().equals(EnumCampoConsultaTipoTransporte.CODIGO.name())) {
				if (getControleConsultaOtimizado().getValorConsulta().trim().isEmpty()
						|| !Uteis.getIsValorNumerico(getControleConsultaOtimizado().getValorConsulta())) {
					throw new ConsistirException(UteisJSF.internacionalizar("prt_TextoPadrao_consultaCodigo"));
				}
			}

			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			getFacadeFactory().getTipoTransporteInterfaceFacade().consultarPorEnumCampoConsulta(getControleConsultaOtimizado());

			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getTipoTransporteInterfaceFacade().excluir(getTipoTransporte(), true,
					getUsuarioLogado());
			novo();
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void irPaginaInicial() throws Exception {
		this.consultar();
	}

	public void irPaginaAnterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
		this.consultar();
	}

	public void irPaginaPosterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
		this.consultar();
	}

	public void irPaginaFinal() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de
	 * uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setCampoConsulta(EnumCampoConsultaTipoTransporte.DESCRICAO.name());
		setListaConsulta(new ArrayList<>(0));
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}

	public boolean getApresentarResultadoConsulta() {
		return getControleConsultaOtimizado().getListaConsulta().size() > 0;
	}

	public TipoTransporteVO getTipoTransporte() {
		if (tipoTransporte == null) {
			tipoTransporte = new TipoTransporteVO();
		}
		return tipoTransporte;
	}

	public void setTipoTransporte(TipoTransporteVO tipoTransporte) {
		this.tipoTransporte = tipoTransporte;
	}
}
