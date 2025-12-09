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
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.arquitetura.faturamento.nfe.ConsistirException;
import negocio.comuns.recursoshumanos.NivelSalarialVO.EnumCampoConsultaNivelSalarial;
import negocio.comuns.recursoshumanos.SecaoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.SecaoFolhaPagamentoVO.EnumCampoConsultaSecaoFolhaPagamento;
import negocio.comuns.recursoshumanos.enumeradores.CategoriaRAISEnum;
import negocio.comuns.recursoshumanos.enumeradores.ControlePontoEnum;
import negocio.comuns.recursoshumanos.enumeradores.MotivoMudancaCNPJEnum;
import negocio.comuns.recursoshumanos.enumeradores.OptanteSimpleEnum;
import negocio.comuns.recursoshumanos.enumeradores.PorteEmpresaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

@Controller("SecaoFolhaPagamentoControle")
@Scope("viewScope")
@Lazy
public class SecaoFolhaPagamentoControle extends SuperControle {

	private static final long serialVersionUID = 6971434447453177405L;

	private static final String TELA_FORM = "secaoFolhaPagamentoForm";
	private static final String TELA_CONS = "secaoFolhaPagamentoCons";
	private static final String CONTEXT_PARA_EDICAO = "itemSecaoFolhaPagamento";

	private SecaoFolhaPagamentoVO secaoFolhaPagamento;

	private List<SelectItem> listaSelectItemOptanteSimples;;
	private List<SelectItem> listaSelectItemMotivoMudancaCnpj;
	private List<SelectItem> listaSelectItemCategoria;
	private List<SelectItem> listaSelectItemMesDataBase;
	private List<SelectItem> listaSelectItemPorteEmpresa;
	private List<SelectItem> listaSelectItemControlePonto;

	public SecaoFolhaPagamentoControle() {
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
			secaoFolhaPagamento = (SecaoFolhaPagamentoVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			setSecaoFolhaPagamento(secaoFolhaPagamento);
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
			getFacadeFactory().getSecaoFolhaPagamentoInterfaceFacade().persistir(getSecaoFolhaPagamento(), true, getUsuarioLogado());
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

			if (getControleConsultaOtimizado().getCampoConsulta().equals(EnumCampoConsultaSecaoFolhaPagamento.CODIGO.name())) {
				if (getControleConsultaOtimizado().getValorConsulta().trim().isEmpty() || !Uteis.getIsValorNumerico(getControleConsultaOtimizado().getValorConsulta())) {
					throw new ConsistirException(UteisJSF.internacionalizar("prt_SecaoFolhaPagamento_ConsultaCampoCodigoInvalido"));
				}
			}

			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			getFacadeFactory().getSecaoFolhaPagamentoInterfaceFacade().consultarPorEnumCampoConsulta(getControleConsultaOtimizado());

			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getSecaoFolhaPagamentoInterfaceFacade().excluir(getSecaoFolhaPagamento(), true, getUsuarioLogado());
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
		getControleConsultaOtimizado().setCampoConsulta(EnumCampoConsultaNivelSalarial.DESCRICAO.name());
		setListaConsulta(new ArrayList<SecaoFolhaPagamentoVO>(0));
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}
	
	public boolean getApresentarResultadoConsulta() {
		return getControleConsultaOtimizado().getListaConsulta().size() > 0;
	}

	public boolean getApresentarPaginadorResultadoConsulta() {
		return getControleConsultaOtimizado().getListaConsulta().size() > 10;
	}

	public SecaoFolhaPagamentoVO getSecaoFolhaPagamento() {
		if (secaoFolhaPagamento == null) {
			secaoFolhaPagamento = new SecaoFolhaPagamentoVO();
		}
		return secaoFolhaPagamento;
	}

	public void setSecaoFolhaPagamento(SecaoFolhaPagamentoVO secaoFolhaPagamento) {
		this.secaoFolhaPagamento = secaoFolhaPagamento;
	}

	public List<SelectItem> getListaSelectItemOptanteSimples() {
		try {
			if (listaSelectItemOptanteSimples == null) {
				listaSelectItemOptanteSimples = new ArrayList<>(0);
				List<SelectItem> optanteSimples = OptanteSimpleEnum.getValorOptanteSimpleEnum();
				for (SelectItem selectItem : optanteSimples) {
					listaSelectItemOptanteSimples.add(selectItem);
				}
			}
			return listaSelectItemOptanteSimples;
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
		return new ArrayList<>(0);
	}

	public void setListaSelectItemOptanteSimples(List<SelectItem> listaSelectItemOptanteSimples) {
		this.listaSelectItemOptanteSimples = listaSelectItemOptanteSimples;
	}

	public List<SelectItem> getListaSelectItemMotivoMudancaCnpj() {
		try {
			if (listaSelectItemMotivoMudancaCnpj == null) {
				listaSelectItemMotivoMudancaCnpj = new ArrayList<>(0);
				List<SelectItem> motivoMudancaCpnj = MotivoMudancaCNPJEnum.getValorMotivoMudancaCNPJEnum();
				for (SelectItem selectItem : motivoMudancaCpnj) {
					listaSelectItemMotivoMudancaCnpj.add(selectItem);
				}
			}
			return listaSelectItemMotivoMudancaCnpj;
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return new ArrayList<>(0);
	}

	public void setListaSelectItemMotivoMudancaCnpj(List<SelectItem> listaSelectItemMotivoMudancaCnpj) {
		this.listaSelectItemMotivoMudancaCnpj = listaSelectItemMotivoMudancaCnpj;
	}

	public List<SelectItem> getListaSelectItemCategoria() {
		try {
			if (listaSelectItemCategoria == null) {
				listaSelectItemCategoria = new ArrayList<>(0);
				List<SelectItem> categoriaRais = CategoriaRAISEnum.getValorCategoriaRAISEnum();
				for (SelectItem selectItem : categoriaRais) {
					listaSelectItemCategoria.add(selectItem);
				}
			}
			return listaSelectItemCategoria;
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return new ArrayList<>(0);
	}

	public void setListaSelectItemCategoria(List<SelectItem> listaSelectItemCategoria) {
		this.listaSelectItemCategoria = listaSelectItemCategoria;
	}

	public List<SelectItem> getListaSelectItemMesDataBase() {
		try {
			if (listaSelectItemMesDataBase == null) {
				listaSelectItemMesDataBase = new ArrayList<>(0);
				listaSelectItemMesDataBase = MesAnoEnum.getValorMesAnoEnum();
			}
			return listaSelectItemMesDataBase;
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
		return new ArrayList<>(0);
	}

	public void setListaSelectItemMesDataBase(List<SelectItem> listaSelectItemMesDataBase) {
		this.listaSelectItemMesDataBase = listaSelectItemMesDataBase;
	}

	public List<SelectItem> getListaSelectItemPorteEmpresa() {
		try {
			if (listaSelectItemPorteEmpresa == null) {
				listaSelectItemPorteEmpresa = new ArrayList<>(0);
				List<SelectItem> porteEmpresa = PorteEmpresaEnum.getValorPorteEmpresaEnum();
				for (SelectItem selectItem : porteEmpresa) {
					listaSelectItemPorteEmpresa.add(selectItem);
				}
			}
			return listaSelectItemPorteEmpresa;
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
		return new ArrayList<>(0);
	}

	public void setListaSelectItemPorteEmpresa(List<SelectItem> listaSelectItemPorteEmpresa) {
		this.listaSelectItemPorteEmpresa = listaSelectItemPorteEmpresa;
	}

	public List<SelectItem> getListaSelectItemControlePonto() {
		try {
			if (listaSelectItemControlePonto == null) {
				listaSelectItemControlePonto = new ArrayList<>(0);
				List<SelectItem> controlePonto = ControlePontoEnum.getValorControlePontoEnum();
				for (SelectItem selectItem : controlePonto) {
					listaSelectItemControlePonto.add(selectItem);
				}
			}
			return listaSelectItemControlePonto;
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
		return new ArrayList<>(0);
	}

	public void setListaSelectItemControlePonto(List<SelectItem> listaSelectItemControlePonto) {
		this.listaSelectItemControlePonto = listaSelectItemControlePonto;
	}
	
	
}
