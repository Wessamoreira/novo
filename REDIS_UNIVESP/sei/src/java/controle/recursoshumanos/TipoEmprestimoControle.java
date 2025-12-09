package controle.recursoshumanos;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.faturamento.nfe.ConsistirException;
import negocio.comuns.financeiro.AgenciaVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.recursoshumanos.TipoEmprestimoVO;
import negocio.comuns.recursoshumanos.TipoEmprestimoVO.EnumCampoConsultaTipoEmprestimo;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

@Controller("TipoEmprestimoControle")
@Scope("viewScope")
@Lazy
public class TipoEmprestimoControle extends SuperControle {

	private static final long serialVersionUID = -6499489579795786309L;

	private final String TELA_FORM = "tipoEmprestimoForm";
	private final String TELA_CONS = "tipoEmprestimoCons";
	private final String CONTEXT_PARA_EDICAO = "itemTipoEmprestimo";

	private TipoEmprestimoVO tipoEmprestimo;

	private List<SelectItem> bancos;
	private List<SelectItem> agencias;
	private List<SelectItem> tipoEmprestimos;

	public TipoEmprestimoControle() {
		setControleConsultaOtimizado(new DataModelo());
		inicializarConsultar();
	}

	public String novo() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		inicializarBancos();
		inicializarTipos();
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public String editar() {
		try {
			TipoEmprestimoVO obj = (TipoEmprestimoVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			setTipoEmprestimo(obj);
			setControleConsultaOtimizado(new DataModelo());
			inicializarBancos();
			inicializarAgencias();
			inicializarTipos();
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void persistir() {
		try {
			getFacadeFactory().getTipoEmprestimoInterfaceFacade().persistir(getTipoEmprestimo(), true, getUsuarioLogado());
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

			if (getControleConsultaOtimizado().getCampoConsulta().equals(EnumCampoConsultaTipoEmprestimo.CODIGO.name())) {
				if (getControleConsultaOtimizado().getValorConsulta().trim().isEmpty()
						|| !Uteis.getIsValorNumerico(getControleConsultaOtimizado().getValorConsulta())) {
					throw new ConsistirException(UteisJSF.internacionalizar("prt_TextoPadrao_consultaCodigo"));
				}
			}

			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			getFacadeFactory().getTipoEmprestimoInterfaceFacade().consultarPorEnumCampoConsulta(getControleConsultaOtimizado());

			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getTipoEmprestimoInterfaceFacade().excluir(getTipoEmprestimo(), true, getUsuarioLogado());
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
		getControleConsultaOtimizado().setCampoConsulta(EnumCampoConsultaTipoEmprestimo.DESCRICAO.name());
		setListaConsulta(new ArrayList<>(0));
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}

	/**
	 * Consulta Todos os bancos cadastrados.
	 */
	private void inicializarBancos() {
		try {
			List<BancoVO> list = getFacadeFactory().getBancoFacade().consultarPorBancoNivelComboBox(false, getUsuarioLogado());
			bancos = new ArrayList<>();

			bancos.add(new SelectItem(0, ""));
			for (BancoVO bancoVO : list) {
				bancos.add(new SelectItem(bancoVO.getCodigo(), bancoVO.getNome()));
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void inicializarAgencias() {
		try {
			if (Uteis.isAtributoPreenchido(getTipoEmprestimo().getBanco().getNome())) {
				List<AgenciaVO> list = getFacadeFactory().getAgenciaFacade().consultarPorNomeBanco(getTipoEmprestimo().getBanco().getNome(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				agencias = new ArrayList<>();

				agencias.add(new SelectItem("", ""));
				for (AgenciaVO agenciaVO : list) {
					agencias.add(new SelectItem(agenciaVO.getCodigo(), agenciaVO.getNome()));
				}
			} else {
				agencias.clear();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	@SuppressWarnings("static-access")
	private void inicializarTipos() {
		tipoEmprestimos = new ArrayList<>();
		setTipoEmprestimos(getTipoEmprestimo().getTipoEmprestimo().getValorTipoEmprestimoEnum());
	}

	public void montarListaAgencias() {
		try {
			if (Uteis.isAtributoPreenchido(getTipoEmprestimo().getBanco().getCodigo())) {
				List<AgenciaVO> list = getFacadeFactory().getAgenciaFacade().consultarPorBanco(getTipoEmprestimo().getBanco().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				agencias = new ArrayList<>();

				agencias.add(new SelectItem("", ""));
				for (AgenciaVO agenciaVO : list) {
					agencias.add(new SelectItem(agenciaVO.getCodigo(), agenciaVO.getNome()));
				}
			} else {
				agencias.clear();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public boolean getApresentarResultadoConsulta() {
		return getControleConsultaOtimizado().getListaConsulta().size() > 0;
	}

	public TipoEmprestimoVO getTipoEmprestimo() {
		if (tipoEmprestimo == null) {
			tipoEmprestimo = new TipoEmprestimoVO();
		}
		return tipoEmprestimo;
	}

	public void setTipoEmprestimo(TipoEmprestimoVO tipoEmprestimo) {
		this.tipoEmprestimo = tipoEmprestimo;
	}

	public List<SelectItem> getBancos() {
		if (bancos == null) {
			bancos = new ArrayList<>();
		}
		return bancos;
	}

	public void setBancos(List<SelectItem> bancos) {
		this.bancos = bancos;
	}

	public List<SelectItem> getAgencias() {
		if (agencias == null) {
			agencias = new ArrayList<>();
		}
		return agencias;
	}

	public void setAgencias(List<SelectItem> agencias) {
		this.agencias = agencias;
	}

	public List<SelectItem> getTipoEmprestimos() {
		if (tipoEmprestimos == null) {
			tipoEmprestimos = new ArrayList<>();
		}
		return tipoEmprestimos;
	}

	public void setTipoEmprestimos(List<SelectItem> tipoEmprestimos) {
		this.tipoEmprestimos = tipoEmprestimos;
	}
}
