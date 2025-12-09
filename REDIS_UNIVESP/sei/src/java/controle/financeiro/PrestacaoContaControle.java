package controle.financeiro;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.ItemPrestacaoContaCategoriaDespesaVO;
import negocio.comuns.financeiro.ItemPrestacaoContaOrigemContaReceberVO;
import negocio.comuns.financeiro.ItemPrestacaoContaPagarVO;
import negocio.comuns.financeiro.ItemPrestacaoContaReceberVO;
import negocio.comuns.financeiro.ItemPrestacaoContaTurmaVO;
import negocio.comuns.financeiro.PrestacaoContaVO;
import negocio.comuns.financeiro.enumerador.TipoPrestacaoContaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;


@Controller("PrestacaoContaControle")
@Scope("viewScope")
@Lazy
public class PrestacaoContaControle extends SuperControleRelatorio implements Serializable {

	private static final long serialVersionUID = 761718062891822609L;
	private PrestacaoContaVO prestacaoConta;
	private ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesa;
	private ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesaEditar;
	
	private ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceber;
	private ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberEditar;
	
	private List<SelectItem> listaSelectItemCategoriaDespesa;
	private List<SelectItem> listaSelectItemFiltroDataContaReceber;
	private List<SelectItem> listaSelectItemFiltroDataContaPagar;
	private List<SelectItem> listaSelectItemTipoOrigemContaReceber;
	private List<SelectItem> listaSelectItemTipoOrigemContaReceberIncluir;
	private String filtroDataContaReceber = "DATA_RECEBIMENTO";
	private String filtroDataContaPagar = "DATA_PAGAMENTO";
	private String filtroTipoOrigemContaReceber;
	private Date dataInicioContaReceber;
	private Date dataInicioContaPagar;
	private Date dataFimContaReceber;
	private Date dataInicioTurma;
	private Date dataFimTurma;
	private String filtroIdentificadorTurma;
	private Date dataFimContaPagar;
	private String filtroNomeFavorecidoContaReceber;
	private String filtroNomeFavorecidoContaPagar;
	private TipoPrestacaoContaEnum tipoPrestacaoContaEnum;
	private List<ItemPrestacaoContaPagarVO> listaConsultaItemPrestacaoContaPagarVOs;
	private List<ItemPrestacaoContaTurmaVO> listaConsultaItemPrestacaoContaTurmaVOs;
	private List<ItemPrestacaoContaReceberVO> listaConsultaItemPrestacaoContaReceberVOs;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<TurmaVO> listaConsultaTurma;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private Boolean visualizacao = false;
	private Date dataInicioCompetencia;
	private Date dataFimCompetencia;
	private Date dataInicioConsulta;
	
	private String tipoOrigemContaReceber;

	private Double totalInformadoManualApresentacao;
	private Double totalApresentacao;
	
	public PrestacaoContaControle() {
		HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
		if (request.getParameter("TPC") != null) {
			setTipoPrestacaoContaEnum(TipoPrestacaoContaEnum.valueOf(request.getParameter("TPC")));
		}
		if (request.getParameter("COD") != null) {
			visualizar(Integer.valueOf(request.getParameter("COD")));
		}		
		setIdControlador(PrestacaoContaControle.class.getSimpleName()+"TPC"+""+getTipoPrestacaoContaEnum().name());
		setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
	}

	public String novo() {
		setPrestacaoConta(null);
		setItemPrestacaoContaCategoriaDespesa(null);
		getPrestacaoConta().setTipoPrestacaoConta(getTipoPrestacaoContaEnum());
		getListaConsultaItemPrestacaoContaPagarVOs().clear();
		getListaConsultaItemPrestacaoContaReceberVOs().clear();
		getListaConsultaItemPrestacaoContaTurmaVOs().clear();
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
		return Uteis.getCaminhoRedirecionamentoNavegacao("prestacaoContaForm.xhtml", getIdControlador());
	}

	public String visualizar(Integer codigo) {
		try {
			setVisualizacao(true);
			setPrestacaoConta(getFacadeFactory().getPrestacaoContaFacade().consultarPorChavePrimaria(codigo, NivelMontarDados.FORCAR_RECARGATODOSOSDADOS, getUsuarioLogado()));
			setMensagemID("msg_entre_dados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("prestacaoContaForm.xhtml", getIdControlador());
	}

	public String editar() {
		try {
			setPrestacaoConta((PrestacaoContaVO) context().getExternalContext().getRequestMap().get("prestacaoContaItens"));
			setPrestacaoConta(getFacadeFactory().getPrestacaoContaFacade().consultarPorChavePrimaria(getPrestacaoConta().getCodigo(), NivelMontarDados.FORCAR_RECARGATODOSOSDADOS, getUsuarioLogado()));

			realizarCalculoSaldoFinal();
			setMensagemID("msg_entre_dados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("prestacaoContaForm.xhtml", getIdControlador());
	}

	public void persistir() {
		try {
			getFacadeFactory().getPrestacaoContaFacade().persistir(getPrestacaoConta(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getPrestacaoContaFacade().excluir(getPrestacaoConta(), true, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("prestacaoContaForm.xhtml", getIdControlador());
	}

	public String irPaginaConsulta() {
		setControleConsultaOtimizado(new DataModelo());
		setPrestacaoConta(null);
		getPrestacaoConta().setTipoPrestacaoConta(getTipoPrestacaoContaEnum());
		return Uteis.getCaminhoRedirecionamentoNavegacao("prestacaoContaCons.xhtml", getIdControlador());
	}

	public String consultar() {
		try {
			super.consultar();
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getPrestacaoContaFacade().consultar(getControleConsultaOtimizado().getValorConsulta(), getTipoPrestacaoContaEnum(), getPrestacaoConta().getTurma(), getPrestacaoConta().getUnidadeEnsino(),getDataInicioConsulta(), getControleConsultaOtimizado().getDataFim(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, getUsuarioLogado(), getDataInicioCompetencia(), getDataFimCompetencia() ));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getPrestacaoContaFacade().consultarTotalRegistro(getControleConsultaOtimizado().getValorConsulta(), getTipoPrestacaoContaEnum(), getPrestacaoConta().getTurma(), getPrestacaoConta().getUnidadeEnsino(), getDataInicioConsulta(), getControleConsultaOtimizado().getDataFim(), getDataInicioCompetencia(), getDataFimCompetencia()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("prestacaoContaCons.xhtml", getIdControlador());
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultar();
	}
	
	public void realizarPreenchimentoDatasDosFiltroPagamento() {
		try {
			if (this.getPrestacaoConta().getDataCompetencia() == null) {
				 throw new Exception(UteisJSF.internacionalizar("msg_PrestacaoConta_dataCompetencia"));
			}
			setDataInicioContaPagar(UteisData.getPrimeiroDataMes(this.getPrestacaoConta().getDataCompetencia()));
			setDataFimContaPagar(UteisData.getUltimaDataMes(this.getPrestacaoConta().getDataCompetencia()));
			consultarItemPrestacaoContaPagar();
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarItemPrestacaoContaPagar() {
		try {
			if (getDataInicioContaPagar() == null) {
				throw new Exception("O filtro DATA DE INÍCIO deve ser informado.");
			}
			if (getDataFimContaPagar() == null) {
				throw new Exception("O filtro DATA DE TÉRMINO deve ser informado.");
			}
			setListaConsultaItemPrestacaoContaPagarVOs(getFacadeFactory().getItemPrestacaoContaPagarFacade().consultarContaPagarDisponivelPrestacaoConta(getFiltroNomeFavorecidoContaPagar(), getFiltroDataContaPagar(), getDataInicioContaPagar(), getDataFimContaPagar(), getPrestacaoConta()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setListaConsultaItemPrestacaoContaPagarVOs(new ArrayList<>());
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarItemPrestacaoContaPagar() {
		try {
			ItemPrestacaoContaPagarVO itemPrestacaoContaPagarVO = (ItemPrestacaoContaPagarVO) context().getExternalContext().getRequestMap().get("contaPagarItens");
			getFacadeFactory().getPrestacaoContaFacade().preencherItemPrestacaoContaCategoriaDespesa(getPrestacaoConta(), itemPrestacaoContaPagarVO, getUsuarioLogado());
			removerItemPrestacaoContaPagarListaConsulta(itemPrestacaoContaPagarVO);
			realizarCalculoSaldoFinal();
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}	

	public void adicionarTodosItemPrestacaoContaPagar() {
		try {
			getFacadeFactory().getPrestacaoContaFacade().adicionarVariasItensPrestacaoContaPagarVOs(getListaConsultaItemPrestacaoContaPagarVOs(), getPrestacaoConta(), getUsuarioLogado());
			getListaConsultaItemPrestacaoContaPagarVOs().clear();
			realizarCalculoSaldoFinal();
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void removerItemPrestacaoContaPagarListaConsulta(ItemPrestacaoContaPagarVO itemPrestacaoContaPagarVO) {
		int index = 0;
		for (ItemPrestacaoContaPagarVO obj : getListaConsultaItemPrestacaoContaPagarVOs()) {
			if (obj.getContaPagar().getCodigo().intValue() == itemPrestacaoContaPagarVO.getContaPagar().getCodigo().intValue()) {
				getListaConsultaItemPrestacaoContaPagarVOs().remove(index);
				return;
			}
			index++;
		}
		realizarCalculoSaldoFinal();
	}
	
	public void removerItemPrestacaoContaCategoriaDespesaVO() {
		try {
			ItemPrestacaoContaCategoriaDespesaVO obj = (ItemPrestacaoContaCategoriaDespesaVO) context().getExternalContext().getRequestMap().get("itemPrestacaoContaCategoriaDespesaItens");
			getFacadeFactory().getPrestacaoContaFacade().removerItemPrestacaoContaCategoriaDespesaVO(getPrestacaoConta(), obj);
			realizarCalculoSaldoFinal();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarItemPrestacaoContaReceber() {
		try {
			if (getDataInicioContaReceber() == null) {
				throw new Exception("O filtro DATA DE INÍCIO deve ser informado.");
			}
			if (getDataFimContaReceber() == null) {
				throw new Exception("O filtro DATA DE TÉRMINO deve ser informado.");
			}
			setListaConsultaItemPrestacaoContaReceberVOs(getFacadeFactory().getItemPrestacaoContaReceberFacade().consultarContaReceberDisponivelPrestacaoConta(getFiltroNomeFavorecidoContaReceber(), getFiltroDataContaReceber(), getDataInicioContaReceber(), getDataFimContaReceber(), getFiltroTipoOrigemContaReceber(), getPrestacaoConta()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarItemPrestacaoTurma() {
		try {
			if (getDataInicioTurma() == null) {
				throw new Exception("O filtro DATA DE INÍCIO deve ser informado.");
			}
			if (getDataFimTurma() == null) {
				throw new Exception("O filtro DATA DE TÉRMINO deve ser informado.");
			}
			setListaConsultaItemPrestacaoContaTurmaVOs(getFacadeFactory().getPrestacaoContaFacade().consultarPrestacaoContaTurmaDisponivelPrestacaoConta(getFiltroIdentificadorTurma(), getDataInicioTurma(), getDataFimTurma(), getPrestacaoConta(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarItemPrestacaoContaReceber() {
		try {
			ItemPrestacaoContaReceberVO itemPrestacaoContaReceberVO = (ItemPrestacaoContaReceberVO) context().getExternalContext().getRequestMap().get("contaReceberItens");
			ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberNovoVO = new ItemPrestacaoContaOrigemContaReceberVO();
			itemPrestacaoContaOrigemContaReceberNovoVO.setTipoOrigemContaReceber(TipoOrigemContaReceber.getEnum(itemPrestacaoContaReceberVO.getContaReceber().getTipoOrigem()));
			getFacadeFactory().getItemPrestacaoContaOrigemContaReceberFacade().adicionarItemPrestacaoContaReceberVO(itemPrestacaoContaOrigemContaReceberNovoVO, itemPrestacaoContaReceberVO);
			getFacadeFactory().getPrestacaoContaFacade().adicionarItemPrestacaoContaOrigemContaReceberVO(getPrestacaoConta(), itemPrestacaoContaOrigemContaReceberNovoVO);
			removerItemPrestacaoContaReceberListaConsulta(itemPrestacaoContaReceberVO);
			realizarCalculoSaldoFinal();
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerItemPrestacaoContaReceberListaConsulta(ItemPrestacaoContaReceberVO itemPrestacaoContaReceberVO) {
		int index = 0;
		for (ItemPrestacaoContaReceberVO obj : getListaConsultaItemPrestacaoContaReceberVOs()) {
			if (obj.getContaReceber().getCodigo().intValue() == itemPrestacaoContaReceberVO.getContaReceber().getCodigo().intValue()) {
				getListaConsultaItemPrestacaoContaReceberVOs().remove(index);
				return;
			}
			index++;
		}
		realizarCalculoSaldoFinal();
	}

	public void adicionarTodosItemPrestacaoContaReceber() {
		try {
			getFacadeFactory().getItemPrestacaoContaOrigemContaReceberFacade().adicionarVariasItensPrestacaoContaReceberVOs(getListaConsultaItemPrestacaoContaReceberVOs(), getPrestacaoConta());
			getListaConsultaItemPrestacaoContaReceberVOs().clear();
			realizarCalculoSaldoFinal();
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerItemPrestacaoContaTurmaVO() {
		try {
			ItemPrestacaoContaTurmaVO obj = (ItemPrestacaoContaTurmaVO) context().getExternalContext().getRequestMap().get("itemPrestacaoContaTurmaItens");
			getFacadeFactory().getPrestacaoContaFacade().removerItemPrestacaoContaTurmaVO(getPrestacaoConta(), obj);
			realizarCalculoSaldoFinal();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarItemPrestacaoContaTurmaVO() {
		try {
			ItemPrestacaoContaTurmaVO obj = (ItemPrestacaoContaTurmaVO) context().getExternalContext().getRequestMap().get("prestacaoContaTurmaItens");
			getFacadeFactory().getPrestacaoContaFacade().adicionarItemPrestacaoContaTurmaVO(getPrestacaoConta(), obj);
			int index = 0;
			for (ItemPrestacaoContaTurmaVO itemPrestacaoContaTurmaVO : getListaConsultaItemPrestacaoContaTurmaVOs()) {
				if (itemPrestacaoContaTurmaVO.getPrestacaoContaTurma().getCodigo().intValue() == obj.getPrestacaoContaTurma().getCodigo().intValue()) {
					getListaConsultaItemPrestacaoContaTurmaVOs().remove(index);
					break;
				}
				index++;
			}
			realizarCalculoSaldoFinal();
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarTodosItemPrestacaoContaTurmaVO() {
		try {
			for (ItemPrestacaoContaTurmaVO obj : getListaConsultaItemPrestacaoContaTurmaVOs()) {
				getFacadeFactory().getPrestacaoContaFacade().adicionarItemPrestacaoContaTurmaVO(getPrestacaoConta(), obj);
			}
			getListaConsultaItemPrestacaoContaTurmaVOs().clear();
			realizarCalculoSaldoFinal();
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarItemPrestacaoContaCategoriaDespesaVO() {
		try {
			getItemPrestacaoContaCategoriaDespesa().setCategoriaDespesa(getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(getItemPrestacaoContaCategoriaDespesa().getCategoriaDespesa().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			getFacadeFactory().getPrestacaoContaFacade().adicionarItemPrestacaoContaCategoriaDespesaVO(getPrestacaoConta(), getItemPrestacaoContaCategoriaDespesa().getCategoriaDespesa(), getItemPrestacaoContaCategoriaDespesa().getValor(), Boolean.TRUE);
			setItemPrestacaoContaCategoriaDespesa(new ItemPrestacaoContaCategoriaDespesaVO());
			realizarCalculoSaldoFinal();
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	//TODO Adicionar Recebimeno
	public void adicionarItemPrestacaoContaOrigemContraRecebimento() {
		try {
			getItemPrestacaoContaOrigemContaReceber().setValorInformadoManual(Boolean.TRUE);
			if (Uteis.isAtributoPreenchido(getTipoOrigemContaReceber())) {
				getItemPrestacaoContaOrigemContaReceber().setTipoOrigemContaReceber(TipoOrigemContaReceber.getEnum(getTipoOrigemContaReceber()));
			}
			getFacadeFactory().getPrestacaoContaFacade().adicionarItemPrestacaoContaOrigemContaReceberVO(getPrestacaoConta(), getItemPrestacaoContaOrigemContaReceber());

			setItemPrestacaoContaOrigemContaReceber(new ItemPrestacaoContaOrigemContaReceberVO());
			atualizarTotalRecebimento();
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void atualizarTotalRecebimento() {
		double totalRecebimento = 0.0;
		for(ItemPrestacaoContaOrigemContaReceberVO item : getPrestacaoConta().getItemPrestacaoContaOrigemContaReceberVOs()) {
			totalRecebimento = totalRecebimento +  (item.getValor() + item.getValorManual() );
		}
		prestacaoConta.setValorTotalRecebimento(totalRecebimento);
		
		realizarCalculoSaldoFinal();
	}
	
	public void atualizarTotalCategoriaDespesa() {
		double totalCategoriaDespesa = 0.0;
		for(ItemPrestacaoContaCategoriaDespesaVO item : getPrestacaoConta().getItemPrestacaoContaCategoriaDespesaVOs()) {
			totalCategoriaDespesa = totalCategoriaDespesa + item.getValor();
		}
		prestacaoConta.setValorTotalPagamento(totalCategoriaDespesa);
		
		realizarCalculoSaldoFinal();
	}
	
	public void filtrarContaReceber() {
		try {
			if (Uteis.isAtributoPreenchido(getFiltroNomeFavorecidoContaReceber())) {
				List<ItemPrestacaoContaReceberVO> lista =  getItemPrestacaoContaOrigemContaReceberEditar().getItemPrestacaoContaReceberVOs().stream()
					.filter(p -> p.getContaReceber().getDadosPessoaAtiva().toUpperCase().contains(getFiltroNomeFavorecidoContaReceber().toUpperCase())).collect(Collectors.toList());

				getItemPrestacaoContaOrigemContaReceberEditar().getItemPrestacaoContaReceberVOs().clear();
				getItemPrestacaoContaOrigemContaReceberEditar().getItemPrestacaoContaReceberVOs().addAll(lista);
			} else {
					getItemPrestacaoContaOrigemContaReceberEditar().getItemPrestacaoContaReceberVOs().addAll(getFacadeFactory().getItemPrestacaoContaReceberFacade().consultarItemPrestacaoContaReceberPorItemPrestacaoContaOrigemContaReceber(getItemPrestacaoContaOrigemContaReceberEditar(), NivelMontarDados.BASICO, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerItemPrestacaoContaOrigemContaReceberVO() {
		try {
			ItemPrestacaoContaOrigemContaReceberVO obj = (ItemPrestacaoContaOrigemContaReceberVO) context().getExternalContext().getRequestMap().get("itemPrestacaoContaOrigemContaReceberItens");
			getFacadeFactory().getPrestacaoContaFacade().removerItemPrestacaoContaOrigemContaReceberVO(getPrestacaoConta(), obj);
			realizarCalculoSaldoFinal();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void editarItemPrestacaoContaOrigemContaReceberVO() {
		try {
			setItemPrestacaoContaOrigemContaReceberEditar((ItemPrestacaoContaOrigemContaReceberVO) context().getExternalContext().getRequestMap().get("itemPrestacaoContaOrigemContaReceberItens"));
			getItemPrestacaoContaOrigemContaReceberEditar().getItemPrestacaoContaReceberVOs().addAll(getFacadeFactory().getItemPrestacaoContaReceberFacade().consultarItemPrestacaoContaReceberPorItemPrestacaoContaOrigemContaReceber(getItemPrestacaoContaOrigemContaReceberEditar(), NivelMontarDados.BASICO, getUsuarioLogado()));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void editarItemPrestacaoContaCategoriaDespesaVO() {
		try {
			setItemPrestacaoContaCategoriaDespesaEditar((ItemPrestacaoContaCategoriaDespesaVO) context().getExternalContext().getRequestMap().get("itemPrestacaoContaCategoriaDespesaItens"));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerItemPrestacaoContaPagar() {
		try {
			ItemPrestacaoContaPagarVO obj = (ItemPrestacaoContaPagarVO) context().getExternalContext().getRequestMap().get("itemPrestacaoContaPagarItens");
			getFacadeFactory().getItemPrestacaoContaCategoriaDespesaFacade().removerItemPrestacaoContaPagarVO(getPrestacaoConta(), getItemPrestacaoContaCategoriaDespesaEditar(), obj);
			realizarCalculoSaldoFinal();
			setMensagemID("msg_dados_excluidos", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerItemPrestacaoContaReceber() {
		try {
			ItemPrestacaoContaReceberVO obj = (ItemPrestacaoContaReceberVO) context().getExternalContext().getRequestMap().get("itemPrestacaoContaReceberItens");
			getFacadeFactory().getItemPrestacaoContaOrigemContaReceberFacade().removerItemPrestacaoContaReceberVO(getPrestacaoConta(), getItemPrestacaoContaOrigemContaReceberEditar(), obj);
			realizarCalculoSaldoFinal();
			setMensagemID("msg_dados_excluidos", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * TurmaCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultarTurma() {
		try {
			super.consultar();
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}

			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}

			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}

			if (getCampoConsultaTurma().equals("nomeTurno")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
			return "";
		} catch (Exception e) {
			setListaConsulta(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}

	}

	public void selecionarTurma() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
		limparDadosTurma();
		getPrestacaoConta().setTurma(obj);
		setCampoConsultaTurma("");
		setValorConsultaTurma("");
		setListaConsultaTurma(new ArrayList<TurmaVO>(0));
		realizarCalculoSaldoFinal();
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public void limparDadosTurma() {
		getPrestacaoConta().getTurma().setCodigo(0);
		getPrestacaoConta().getTurma().setIdentificadorTurma("");
		getPrestacaoConta().getItemPrestacaoContaCategoriaDespesaVOs().clear();
		getPrestacaoConta().getItemPrestacaoContaOrigemContaReceberVOs().clear();
		getPrestacaoConta().setValorTotalRecebimento(0.0);
		getListaConsultaItemPrestacaoContaPagarVOs().clear();
		getListaConsultaItemPrestacaoContaReceberVOs().clear();
	}

	public void limparUnidadeEnsino() {
		getPrestacaoConta().getItemPrestacaoContaCategoriaDespesaVOs().clear();
		getPrestacaoConta().getItemPrestacaoContaOrigemContaReceberVOs().clear();
		getPrestacaoConta().getItemPrestacaoContaTurmaVOs().clear();
		getPrestacaoConta().setValorTotalRecebimento(0.0);
		getPrestacaoConta().setValorTotalPrestacaoContaTurma(0.0);
		getListaConsultaItemPrestacaoContaPagarVOs().clear();
		getListaConsultaItemPrestacaoContaReceberVOs().clear();
		getListaConsultaItemPrestacaoContaTurmaVOs().clear();
	}
	
	public void realizarPreenchimentoDatasDosFiltro() {
		try {
			if (this.getPrestacaoConta().getDataCompetencia() == null) {
				 throw new Exception(UteisJSF.internacionalizar("msg_PrestacaoConta_dataCompetencia"));
			}

			setDataInicioTurma(UteisData.getPrimeiroDataMes(this.getPrestacaoConta().getDataCompetencia()));
			setDataFimTurma(UteisData.getUltimaDataMes(this.getPrestacaoConta().getDataCompetencia()));			

			setDataInicioContaReceber(UteisData.getPrimeiroDataMes(this.getPrestacaoConta().getDataCompetencia()));
			setDataFimContaReceber(UteisData.getUltimaDataMes(this.getPrestacaoConta().getDataCompetencia()));

			setDataInicioTurma(UteisData.getPrimeiroDataMes(this.getPrestacaoConta().getDataCompetencia()));
			setDataFimTurma(UteisData.getUltimaDataMes(this.getPrestacaoConta().getDataCompetencia()));

			consultarItemPrestacaoTurma();
			consultarItemPrestacaoContaReceber();
			setCampoConsultaTurma("");
			setValorConsultaTurma("");
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
		} catch (Exception e) {
			setMensagem(campoConsultaTurma);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarCalculoSaldoFinal() {
		double saldoFinal = 0.0;

		if (getPrestacaoConta().getTipoPrestacaoConta().equals(TipoPrestacaoContaEnum.TURMA)) {
			saldoFinal = (getPrestacaoConta().getSaldoAnterior() + getPrestacaoConta().getValorTotalRecebimento()) - getPrestacaoConta().getValorTotalPagamento();
		}
		
		if (getPrestacaoConta().getTipoPrestacaoConta().equals(TipoPrestacaoContaEnum.UNIDADE_ENSINO)) {
			saldoFinal = (getPrestacaoConta().getSaldoAnterior() + getPrestacaoConta().getValorTotalPrestacaoContaTurma() + getPrestacaoConta().getValorTotalRecebimento()) - getPrestacaoConta().getValorTotalPagamento();
		}
		
		getPrestacaoConta().setSaldoFinal(saldoFinal);
	}
	
	public void consultaSaldoAnterior() {
		try {
			boolean consultarSaldoAnteriro = Boolean.TRUE;

			if (getPrestacaoConta().getTipoPrestacaoConta().equals(TipoPrestacaoContaEnum.UNIDADE_ENSINO)) {
				if (!Uteis.isAtributoPreenchido(getPrestacaoConta().getUnidadeEnsino())) {
					consultarSaldoAnteriro = Boolean.FALSE;
				}
			}

			if (getPrestacaoConta().getTipoPrestacaoConta().equals(TipoPrestacaoContaEnum.TURMA)) {
				if (!Uteis.isAtributoPreenchido(getPrestacaoConta().getTurma())) {
					consultarSaldoAnteriro = Boolean.FALSE;
				}
			}

			if (consultarSaldoAnteriro && Uteis.isAtributoPreenchido(getPrestacaoConta().getDataCompetencia())) {
				PrestacaoContaVO obj = getFacadeFactory().getPrestacaoContaFacade().consultarSaldoAnteriorPorDataCompetencia(prestacaoConta);
				if (obj != null) {
					prestacaoConta.setSaldoAnterior(obj.getSaldoFinal());
				} else {
					prestacaoConta.setSaldoAnterior(0.0);
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro" , e.getMessage());
		}
	}

	/**
	 * @return the listaConsultaTurma
	 */
	public List<TurmaVO> getListaConsultaTurma() {
		return listaConsultaTurma;
	}

	/**
	 * @param listaConsultaTurma
	 *            the listaConsultaTurma to set
	 */
	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	/**
	 * @return the valorConsultaTurma
	 */
	public String getValorConsultaTurma() {
		return valorConsultaTurma;
	}

	/**
	 * @param valorConsultaTurma
	 *            the valorConsultaTurma to set
	 */
	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	/**
	 * @return the campoConsultaTurma
	 */
	public String getCampoConsultaTurma() {
		return campoConsultaTurma;
	}

	/**
	 * @param campoConsultaTurma
	 *            the campoConsultaTurma to set
	 */
	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public Boolean getApresentarDadosPrestacaoConta() {
		return getPrestacaoConta().getDataCompetencia() != null;
	}

	public PrestacaoContaVO getPrestacaoConta() {
		if (prestacaoConta == null) {
			prestacaoConta = new PrestacaoContaVO();
		}
		return prestacaoConta;
	}

	public void setPrestacaoConta(PrestacaoContaVO prestacaoConta) {
		this.prestacaoConta = prestacaoConta;
	}

	public ItemPrestacaoContaCategoriaDespesaVO getItemPrestacaoContaCategoriaDespesa() {
		if (itemPrestacaoContaCategoriaDespesa == null) {
			itemPrestacaoContaCategoriaDespesa = new ItemPrestacaoContaCategoriaDespesaVO();
		}
		return itemPrestacaoContaCategoriaDespesa;
	}

	public void setItemPrestacaoContaCategoriaDespesa(ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesa) {
		this.itemPrestacaoContaCategoriaDespesa = itemPrestacaoContaCategoriaDespesa;
	}

	public String getFiltroDataContaReceber() {
		if (filtroDataContaReceber == null) {
			filtroDataContaReceber = "DATA_RECEBIMENTO";
		}
		return filtroDataContaReceber;
	}

	public void setFiltroDataContaReceber(String filtroDataContaReceber) {
		this.filtroDataContaReceber = filtroDataContaReceber;
	}

	public String getFiltroDataContaPagar() {
		if (filtroDataContaPagar == null) {
			filtroDataContaPagar = "DATA_PAGAMENTO";
		}
		return filtroDataContaPagar;
	}

	public void setFiltroDataContaPagar(String filtroDataContaPagar) {
		this.filtroDataContaPagar = filtroDataContaPagar;
	}

	public String getFiltroTipoOrigemContaReceber() {
		if (filtroTipoOrigemContaReceber == null) {
			filtroTipoOrigemContaReceber = "TO";
		}
		return filtroTipoOrigemContaReceber;
	}

	public void setFiltroTipoOrigemContaReceber(String filtroTipoOrigemContaReceber) {
		this.filtroTipoOrigemContaReceber = filtroTipoOrigemContaReceber;
	}

	public Date getDataInicioContaReceber() {
		return dataInicioContaReceber;
	}

	public void setDataInicioContaReceber(Date dataInicioContaReceber) {
		this.dataInicioContaReceber = dataInicioContaReceber;
	}

	public Date getDataInicioContaPagar() {
		return dataInicioContaPagar;
	}

	public void setDataInicioContaPagar(Date dataInicioContaPagar) {
		this.dataInicioContaPagar = dataInicioContaPagar;
	}

	public Date getDataFimContaReceber() {
		return dataFimContaReceber;
	}

	public void setDataFimContaReceber(Date dataFimContaReceber) {
		this.dataFimContaReceber = dataFimContaReceber;
	}

	public Date getDataFimContaPagar() {
		return dataFimContaPagar;
	}

	public void setDataFimContaPagar(Date dataFimContaPagar) {
		this.dataFimContaPagar = dataFimContaPagar;
	}

	public String getFiltroNomeFavorecidoContaReceber() {
		if (filtroNomeFavorecidoContaReceber == null) {
			filtroNomeFavorecidoContaReceber = "";
		}
		return filtroNomeFavorecidoContaReceber;
	}

	public void setFiltroNomeFavorecidoContaReceber(String filtroNomeFavorecidoContaReceber) {
		this.filtroNomeFavorecidoContaReceber = filtroNomeFavorecidoContaReceber;
	}

	public String getFiltroNomeFavorecidoContaPagar() {
		if (filtroNomeFavorecidoContaPagar == null) {
			filtroNomeFavorecidoContaPagar = "";
		}
		return filtroNomeFavorecidoContaPagar;
	}

	public void setFiltroNomeFavorecidoContaPagar(String filtroNomeFavorecidoContaPagar) {
		this.filtroNomeFavorecidoContaPagar = filtroNomeFavorecidoContaPagar;
	}

	public TipoPrestacaoContaEnum getTipoPrestacaoContaEnum() {
		if (tipoPrestacaoContaEnum == null) {
			tipoPrestacaoContaEnum = TipoPrestacaoContaEnum.TURMA;
		}
		return tipoPrestacaoContaEnum;
	}

	public void setTipoPrestacaoContaEnum(TipoPrestacaoContaEnum tipoPrestacaoContaEnum) {
		this.tipoPrestacaoContaEnum = tipoPrestacaoContaEnum;
	}

	public List<ItemPrestacaoContaPagarVO> getListaConsultaItemPrestacaoContaPagarVOs() {
		if (listaConsultaItemPrestacaoContaPagarVOs == null) {
			listaConsultaItemPrestacaoContaPagarVOs = new ArrayList<ItemPrestacaoContaPagarVO>(0);
		}
		return listaConsultaItemPrestacaoContaPagarVOs;
	}

	public void setListaConsultaItemPrestacaoContaPagarVOs(List<ItemPrestacaoContaPagarVO> listaConsultaItemPrestacaoContaPagarVOs) {
		this.listaConsultaItemPrestacaoContaPagarVOs = listaConsultaItemPrestacaoContaPagarVOs;
	}

	public List<ItemPrestacaoContaReceberVO> getListaConsultaItemPrestacaoContaReceberVOs() {
		if (listaConsultaItemPrestacaoContaReceberVOs == null) {
			listaConsultaItemPrestacaoContaReceberVOs = new ArrayList<ItemPrestacaoContaReceberVO>(0);
		}
		return listaConsultaItemPrestacaoContaReceberVOs;
	}

	public void setListaConsultaItemPrestacaoContaReceberVOs(List<ItemPrestacaoContaReceberVO> listaConsultaItemPrestacaoContaReceberVOs) {
		this.listaConsultaItemPrestacaoContaReceberVOs = listaConsultaItemPrestacaoContaReceberVOs;
	}

	public void montarListaSeleciItemCategoriaDespesa() {
		Uteis.liberarListaMemoria(listaSelectItemCategoriaDespesa);
		getListaSelectItemCategoriaDespesa();
	}

	public List<SelectItem> getListaSelectItemCategoriaDespesa() {
		try {
			if (listaSelectItemCategoriaDespesa == null) {
				listaSelectItemCategoriaDespesa = new ArrayList<SelectItem>(0);
				List<CategoriaDespesaVO> categoriaDespesaVOs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorCodigo(0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				for (CategoriaDespesaVO categoriaDespesaVO : categoriaDespesaVOs) {
					listaSelectItemCategoriaDespesa.add(new SelectItem(categoriaDespesaVO.getCodigo(), categoriaDespesaVO.getDescricao()));
				}
			}
			return listaSelectItemCategoriaDespesa;
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
		return new ArrayList<SelectItem>(0);
	}

	public void consultarDadosGeracaoPrestacaoContaPorTurma() {
		List<PrestacaoContaVO> listaPrestacaoConta = new ArrayList<>(0);
		try {
			limparMensagem();
			prestacaoConta.setTipoPrestacaoConta(TipoPrestacaoContaEnum.TURMA);
			realizarPreenchimentoDatasDosFiltro();
			listaPrestacaoConta = getFacadeFactory().getPrestacaoContaFacade().consultarDadosGeracaoPrestacaoContaPorTurma(getFiltroNomeFavorecidoContaReceber(), getFiltroDataContaReceber(), getDataInicioContaReceber(), getDataFimContaReceber(), getFiltroTipoOrigemContaReceber(), getPrestacaoConta());
			if (!listaPrestacaoConta.isEmpty()) {
				prestacaoConta.setTurma(listaPrestacaoConta.get(0).getTurma());
				for (PrestacaoContaVO p : listaPrestacaoConta) {
					setListaConsultaItemPrestacaoContaReceberVOs(getFacadeFactory().getItemPrestacaoContaReceberFacade().consultarContaReceberDisponivelPrestacaoConta(getFiltroNomeFavorecidoContaReceber(), getFiltroDataContaReceber(), getDataInicioContaReceber(), getDataFimContaReceber(), getFiltroTipoOrigemContaReceber(), getPrestacaoConta()));
					setListaConsultaItemPrestacaoContaPagarVOs(getFacadeFactory().getItemPrestacaoContaPagarFacade().consultarContaPagarDisponivelPrestacaoConta(getFiltroNomeFavorecidoContaPagar(), getFiltroDataContaPagar(), getDataInicioContaPagar(), getDataFimContaPagar(), getPrestacaoConta()));
					getFacadeFactory().getItemPrestacaoContaOrigemContaReceberFacade().adicionarVariasItensPrestacaoContaReceberVOs(getListaConsultaItemPrestacaoContaReceberVOs(), getPrestacaoConta());
					getFacadeFactory().getPrestacaoContaFacade().adicionarVariasItensPrestacaoContaPagarVOs(getListaConsultaItemPrestacaoContaPagarVOs(), getPrestacaoConta(), getUsuarioLogado());
					p.setItemPrestacaoContaCategoriaDespesaVOs(prestacaoConta.getItemPrestacaoContaCategoriaDespesaVOs());
					p.setItemPrestacaoContaOrigemContaReceberVOs(prestacaoConta.getItemPrestacaoContaOrigemContaReceberVOs());
					p.setItemPrestacaoContaTurmaVOs(prestacaoConta.getItemPrestacaoContaTurmaVOs());
					p.setValorTotalRecebimento(p.getItemPrestacaoContaOrigemContaReceberVOs().stream().map(itemPrestacaoContaOrigemReceber -> itemPrestacaoContaOrigemReceber.getValor()).mapToDouble(Double::doubleValue).sum());
					p.setSaldoFinal(p.getSaldoAnterior() + p.getValorTotalRecebimento() - p.getValorTotalPagamento());

					getFacadeFactory().getPrestacaoContaFacade().persistir(p, Boolean.FALSE, p.getResponsavelCadastro());
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			listaPrestacaoConta = null;
			prestacaoConta.setTurma(null);
			prestacaoConta.setTipoPrestacaoConta(TipoPrestacaoContaEnum.UNIDADE_ENSINO);
		}
	}

	public List<SelectItem> getListaSelectItemFiltroDataContaReceber() {
		if (listaSelectItemFiltroDataContaReceber == null) {
			listaSelectItemFiltroDataContaReceber = new ArrayList<SelectItem>(0);
			listaSelectItemFiltroDataContaReceber.add(new SelectItem("DATA_RECEBIMENTO", "Recebimento de"));
			listaSelectItemFiltroDataContaReceber.add(new SelectItem("DATA_VENCIMENTO", "Vencimento de"));
		}
		return listaSelectItemFiltroDataContaReceber;
	}

	public List<SelectItem> getListaSelectItemFiltroDataContaPagar() {
		if (listaSelectItemFiltroDataContaPagar == null) {
			listaSelectItemFiltroDataContaPagar = new ArrayList<SelectItem>(0);
			listaSelectItemFiltroDataContaPagar.add(new SelectItem("DATA_PAGAMENTO", "Pagamento de"));
			listaSelectItemFiltroDataContaPagar.add(new SelectItem("DATA_VENCIMENTO", "Vencimento de"));
			listaSelectItemFiltroDataContaPagar.add(new SelectItem("DATA_FATO_GERADOR", "Fato Gerador de"));
		}
		return listaSelectItemFiltroDataContaPagar;
	}

	public List<SelectItem> getListaSelectItemTipoOrigemContaReceber() {
		if (listaSelectItemTipoOrigemContaReceber == null) {
			listaSelectItemTipoOrigemContaReceber = new ArrayList<SelectItem>(0);
			for (TipoOrigemContaReceber tipoOrigemContaReceber : TipoOrigemContaReceber.values()) {
				listaSelectItemTipoOrigemContaReceber.add(new SelectItem(tipoOrigemContaReceber.getValor(), tipoOrigemContaReceber.getDescricao()));
			}

		}
		setTipoOrigemContaReceber(null);
		return listaSelectItemTipoOrigemContaReceber;
	}

	public List<SelectItem> getListaSelectItemTipoOrigemContaReceberIncluir() {
		if (listaSelectItemTipoOrigemContaReceberIncluir == null) {
			listaSelectItemTipoOrigemContaReceberIncluir = new ArrayList<>();
			for (TipoOrigemContaReceber tipoOrigemContaReceber : TipoOrigemContaReceber.values()) {
				listaSelectItemTipoOrigemContaReceberIncluir.add(new SelectItem(tipoOrigemContaReceber.getValor(), tipoOrigemContaReceber.getDescricao()));
			}
			
		}
		return listaSelectItemTipoOrigemContaReceberIncluir;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		try {
			if (listaSelectItemUnidadeEnsino == null) {
				listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
				List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				listaSelectItemUnidadeEnsino.add(new SelectItem(0, ""));
				for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
					listaSelectItemUnidadeEnsino.add(new SelectItem(unidadeEnsinoVO.getCodigo(), unidadeEnsinoVO.getNome()));
				}
			}
			return listaSelectItemUnidadeEnsino;
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return new ArrayList<SelectItem>(0);
	}

	/**
	 * Valida se o usuário logado possui permissão para acessar a funcionalidade
	 * de alteração do saldo anterior.
	 * 
	 * @return
	 */
	public boolean permiteAlterarSaldoAnterior() {
		if (getPrestacaoConta().getTipoPrestacaoConta().equals(TipoPrestacaoContaEnum.TURMA)) {
			return getFacadeFactory().getPrestacaoContaFacade().permiteAlterarSaldoAnteriorTurma(getUsuarioLogadoClone());
		} else {
			return getFacadeFactory().getPrestacaoContaFacade().permiteAlterarSaldoAnteriorUnidadeEnsino(getUsuarioLogadoClone());
		}
	}

	/**
	 * Valida se o usuário logado possui permissão para incluir um valor a receber.
	 * 
	 * @return
	 */
	public boolean permiteIncluirSaldoReceber() {
		if (getPrestacaoConta().getTipoPrestacaoConta().equals(TipoPrestacaoContaEnum.TURMA)) {
			return getFacadeFactory().getPrestacaoContaFacade().permiteIncluirSaldoReceberTurma(getUsuarioLogadoClone());
		} else {
			return getFacadeFactory().getPrestacaoContaFacade().permiteIncluirSaldoReceberUnidadeEnsino(getUsuarioLogadoClone());
		}
	}

	public Boolean getTipoPrestacaoContaTurma() {
		return getTipoPrestacaoContaEnum().equals(TipoPrestacaoContaEnum.TURMA);
	}

	public Date getDataInicioTurma() {
		return dataInicioTurma;
	}

	public void setDataInicioTurma(Date dataInicioTurma) {
		this.dataInicioTurma = dataInicioTurma;
	}

	public Date getDataFimTurma() {
		return dataFimTurma;
	}

	public void setDataFimTurma(Date dataFimTurma) {
		this.dataFimTurma = dataFimTurma;
	}

	public String getFiltroIdentificadorTurma() {
		return filtroIdentificadorTurma;
	}

	public void setFiltroIdentificadorTurma(String filtroIdentificadorTurma) {
		this.filtroIdentificadorTurma = filtroIdentificadorTurma;

	}

	public List<ItemPrestacaoContaTurmaVO> getListaConsultaItemPrestacaoContaTurmaVOs() {
		if (listaConsultaItemPrestacaoContaTurmaVOs == null) {
			listaConsultaItemPrestacaoContaTurmaVOs = new ArrayList<ItemPrestacaoContaTurmaVO>(0);
		}
		return listaConsultaItemPrestacaoContaTurmaVOs;
	}

	public void setListaConsultaItemPrestacaoContaTurmaVOs(List<ItemPrestacaoContaTurmaVO> listaConsultaItemPrestacaoContaTurmaVOs) {
		this.listaConsultaItemPrestacaoContaTurmaVOs = listaConsultaItemPrestacaoContaTurmaVOs;
	}

	public ItemPrestacaoContaCategoriaDespesaVO getItemPrestacaoContaCategoriaDespesaEditar() {
		if (itemPrestacaoContaCategoriaDespesaEditar == null) {
			itemPrestacaoContaCategoriaDespesaEditar = new ItemPrestacaoContaCategoriaDespesaVO();
		}
		return itemPrestacaoContaCategoriaDespesaEditar;
	}

	public void setItemPrestacaoContaCategoriaDespesaEditar(ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesaEditar) {
		this.itemPrestacaoContaCategoriaDespesaEditar = itemPrestacaoContaCategoriaDespesaEditar;
	}

	public ItemPrestacaoContaOrigemContaReceberVO getItemPrestacaoContaOrigemContaReceberEditar() {
		if (itemPrestacaoContaOrigemContaReceberEditar == null) {
			itemPrestacaoContaOrigemContaReceberEditar = new ItemPrestacaoContaOrigemContaReceberVO();
		}
		return itemPrestacaoContaOrigemContaReceberEditar;
	}

	public void setItemPrestacaoContaOrigemContaReceberEditar(ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberEditar) {
		this.itemPrestacaoContaOrigemContaReceberEditar = itemPrestacaoContaOrigemContaReceberEditar;
	}

	public Boolean getVisualizacao() {
		if (visualizacao == null) {
			visualizacao = false;
		}
		return visualizacao;
	}

	public void setVisualizacao(Boolean visualizacao) {
		this.visualizacao = visualizacao;
	}

	public void imprimirPDF() {
		try {
			List<PrestacaoContaVO> prestacaoContaVOs = new ArrayList<PrestacaoContaVO>();
			prestacaoContaVOs.add(getPrestacaoConta());
			getSuperParametroRelVO().setNomeDesignIreport(designIReportRelatorio());
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(caminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			if (getTipoPrestacaoContaTurma()) {
				getSuperParametroRelVO().setTituloRelatorio("Prestação Conta Turma");
			} else {
				getSuperParametroRelVO().setTituloRelatorio("Prestação Conta Unidade Ensino");
			}
			getSuperParametroRelVO().setListaObjetos(prestacaoContaVOs);
			getSuperParametroRelVO().setCaminhoBaseRelatorio(caminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			realizarImpressaoRelatorio();
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} 
	}

	public String designIReportRelatorio() {
		return (caminhoBaseRelatorio() + "prestacaoContaTurmaUnidadeEnsinoRel.jrxml");
	}

	public String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}
	
	public Date getDataInicioCompetencia() {
		return dataInicioCompetencia;
	}

	public void setDataInicioCompetencia(Date dataInicioCompetencia) {
		this.dataInicioCompetencia = dataInicioCompetencia;
	}

	public Date getDataFimCompetencia() {
		return dataFimCompetencia;
	}

	public void setDataFimCompetencia(Date dataFimCompetencia) {
		this.dataFimCompetencia = dataFimCompetencia;
	}

	public Date getDataInicioConsulta() {
		return dataInicioConsulta;
	}

	public void setDataInicioConsulta(Date dataInicioConsulta) {
		this.dataInicioConsulta = dataInicioConsulta;
	}

	public ItemPrestacaoContaOrigemContaReceberVO getItemPrestacaoContaOrigemContaReceber() {
		if (itemPrestacaoContaOrigemContaReceber == null) {
			itemPrestacaoContaOrigemContaReceber = new ItemPrestacaoContaOrigemContaReceberVO();
		}
		return itemPrestacaoContaOrigemContaReceber;
	}

	public void setItemPrestacaoContaOrigemContaReceber(
			ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceber) {
		this.itemPrestacaoContaOrigemContaReceber = itemPrestacaoContaOrigemContaReceber;
	}

	public String getTipoOrigemContaReceber() {
		if (tipoOrigemContaReceber == null) {
			tipoOrigemContaReceber = "";
		}
		return tipoOrigemContaReceber;
	}

	public void setTipoOrigemContaReceber(String tipoOrigemContaReceber) {
		this.tipoOrigemContaReceber = tipoOrigemContaReceber;
	}

	public Double getTotalInformadoManualApresentacao() {
		if (totalInformadoManualApresentacao == null) {
			totalInformadoManualApresentacao = 0.0;
		}
		return totalInformadoManualApresentacao;
	}

	public void setTotalInformadoManualApresentacao(double totalInformadoManualApresentacao) {
		this.totalInformadoManualApresentacao = totalInformadoManualApresentacao;
	}

	public double getTotalApresentacao() {
		if (totalApresentacao == null) {
			totalApresentacao = 0.0;
		}
		return totalApresentacao;
	}

	public void setTotalApresentacao(double totalApresentacao) {
		this.totalApresentacao = totalApresentacao;
	}
	
	public boolean getAdicionarNovoItemPrestacaoContaCategoriaDespesa() {
	  return getFacadeFactory().getPrestacaoContaFacade().permitirAdicionarNovoItemPrestacaoContaCategoriaDespesaVO(getPrestacaoConta() ,getUsuarioLogadoClone());
	}
	
}
