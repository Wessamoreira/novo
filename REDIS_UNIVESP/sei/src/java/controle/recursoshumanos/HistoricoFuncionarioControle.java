package controle.recursoshumanos;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.FuncionarioDependenteVO;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.CategoriaGEDVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.FaixaSalarialVO;
import negocio.comuns.recursoshumanos.HistoricoAfastamentoVO;
import negocio.comuns.recursoshumanos.HistoricoDependentesVO;
import negocio.comuns.recursoshumanos.HistoricoDependentesVO.EnumCampoConsultaHistoricoDependentes;
import negocio.comuns.recursoshumanos.HistoricoFuncaoVO;
import negocio.comuns.recursoshumanos.HistoricoPensaoVO;
import negocio.comuns.recursoshumanos.HistoricoPensaoVO.EnumCampoConsultaHistoricoPensao;
import negocio.comuns.recursoshumanos.HistoricoSalarialVO;
import negocio.comuns.recursoshumanos.HistoricoSecaoVO;
import negocio.comuns.recursoshumanos.HistoricoSituacaoVO;
import negocio.comuns.recursoshumanos.NivelSalarialVO;
import negocio.comuns.recursoshumanos.SecaoFolhaPagamentoVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * Classe responsavel por implementar a interacao entre os componentes JSF das
 * paginas historicoFuncionarioForm.xhtml e historicoFuncionarioCons.xhtl com as
 * funcionalidades da classe <code>historicoFuncionario</code>.
 * Implemtacao da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see CategoriaGEDVO
 */
@Controller("HistoricoFuncionarioControle")
@Scope("viewScope")
public class HistoricoFuncionarioControle extends SuperControle {
	
	private static final long serialVersionUID = -4972439716448593438L;
	
	private static final String TELA_CONS = "historicoFuncionarioCons";
	private static final String TELA_FORM = "historicoFuncionarioForm";
	private static final String CONTEXT_PARA_EDICAO = "itemHistoricoFuncionario";

	private FuncionarioCargoVO funcionarioCargo;
	private String situacaoFuncionario;

	private DataModelo dataModeloHistoricoDependentes;
	private DataModelo dataModeloHistoricoFuncao;
	private DataModelo dataModeloHistoricoSalarial;
	private DataModelo dataModeloHistoricoSecao;
	private DataModelo dataModeloHistoricoSituacao;
	private DataModelo dataModeloHistoricoAfastamento;
	private DataModelo dataModeloHistoricoPensao;
	
	protected List<SelectItem> listaSelectItemCargo;
	protected List<SelectItem> listaSelectItemNivelSalarial;
	protected List<SelectItem> listaSelectItemFaixaSalarial;
	protected List<SelectItem> listaSelectItemSecaoFolhaPagamento;
	protected List<SelectItem> listaSelectItemFuncionarioDependente;
	
	private String abaSelecionada;
	
	private HistoricoDependentesVO historicoDependentesVO;
	private HistoricoFuncaoVO historicoFuncaoVO;
	private HistoricoSalarialVO historicoSalarialVO;
	private HistoricoSecaoVO historicoSecaoVO;
	private HistoricoSituacaoVO historicoSituacaoVO;
	private HistoricoAfastamentoVO historicoAfastamentoVO;
	private HistoricoPensaoVO historicoPensaoVO;
	
	private CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO;

	public HistoricoFuncionarioControle() {
		setControleConsultaOtimizado(new DataModelo());
		inicializarConsultar();
	}

	@Override
	public void consultarDados() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getFacadeFactory().getHistoricoFuncionarioInterfaceFacade().consultarPorEnumCampoConsulta(getControleConsultaOtimizado(), situacaoFuncionario);
			
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public String editar() {
		FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
		setFuncionarioCargo(obj);
		setControleConsultaOtimizado(new DataModelo());
		setMensagemID(MSG_TELA.msg_dados_editar.name());
		consultarDadosHistoricoDependentes();
		inicializarValoresHistoricos();
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void consultarDadosHistoricoDependentes() {
		try {
			preencherDadosParaConsulta(getDataModeloHistoricoDependentes());
			getFacadeFactory().getHistoricoDependentesInterfaceFacade().consultarPorEnumCampoConsulta(dataModeloHistoricoDependentes);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarDadosHistoricoFuncao() {
		try {
			preencherDadosParaConsulta(getDataModeloHistoricoFuncao());
			getFacadeFactory().getHistoricoFuncaoInterfaceFacade().consultarPorEnumCampoConsulta(dataModeloHistoricoFuncao);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarDadosHistoricoSalarial() {
		try {
			preencherDadosParaConsulta(getDataModeloHistoricoSalarial());
			getFacadeFactory().getHistoricoSalarialInterfaceFacade().consultarPorEnumCampoConsulta(dataModeloHistoricoSalarial);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarDadosHistoricoSecao() {
		try {
			preencherDadosParaConsulta(getDataModeloHistoricoSecao());
			getFacadeFactory().getHistoricoSecaoInterfaceFacade().consultarPorEnumCampoConsulta(dataModeloHistoricoSecao);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarDadosHistoricoSituacao() {
		try {
			preencherDadosParaConsulta(getDataModeloHistoricoSituacao());
			getFacadeFactory().getHistoricoSituacaoInterfaceFacade().consultarPorEnumCampoConsulta(dataModeloHistoricoSituacao);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarDadosHistoricoAfastamento() {
		try {
			preencherDadosParaConsulta(getDataModeloHistoricoAfastamento());
			getFacadeFactory().getHistoricoAfastamentoInterfaceFacade().consultarPorEnumCampoConsulta(dataModeloHistoricoAfastamento);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarDadosHistoricoPensao() {
		try {
			getDataModeloHistoricoPensao().setCampoConsulta(EnumCampoConsultaHistoricoPensao.MATRICULA_FUNCIONARIO.name());
			getDataModeloHistoricoPensao().setValorConsulta(getFuncionarioCargo().getFuncionarioVO().getMatricula());
			getDataModeloHistoricoPensao().setLimitePorPagina(10);
			getDataModeloHistoricoPensao().getListaFiltros().clear();

			getFacadeFactory().getHistoricoPensaoInterfaceFacade().consultarPorEnumCampoConsulta(getDataModeloHistoricoPensao());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarFuncionarioPelaMatriculaCargo() {
		try {
			setFuncionarioCargo(getFacadeFactory().getFuncionarioCargoFacade().consultarPorMatriculaCargo(getFuncionarioCargo().getMatriculaCargo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			inicializarValoresHistoricos();
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}

		limparHistoricos();

		if (Uteis.isAtributoPreenchido(getFuncionarioCargo())) {
			consultarDadosHistoricoDependentes();
		}

		setAbaSelecionada("primeiraTab");
	}
	
	public void novoHistoricoDependente() {
		setHistoricoDependentesVO(new HistoricoDependentesVO());
	}
	
	public void novoHistoricoFuncao() {
		setHistoricoFuncaoVO(new HistoricoFuncaoVO());
	}

	public void novoHistoricoSalarial() {
		setHistoricoSalarialVO(new HistoricoSalarialVO());
	}

	public void novoHistoricoSecao() {
		setHistoricoSecaoVO(new HistoricoSecaoVO());
	}

	public void novoHistoricoSituacao() {
		setHistoricoSituacaoVO(new HistoricoSituacaoVO());
	}

	public void novoHistoricoAfastamento() {
		setHistoricoAfastamentoVO(new HistoricoAfastamentoVO());
	}

	public void novoHistoricoPensao() {
		setHistoricoPensaoVO(new HistoricoPensaoVO());
	}

	//Editar
	public void editarHistoricoDependente() {
		HistoricoDependentesVO objeto = (HistoricoDependentesVO) context().getExternalContext().getRequestMap().get("dependentes");
		setHistoricoDependentesVO(objeto);
	}

	public void editarHistoricoFuncao() {
		HistoricoFuncaoVO objeto = (HistoricoFuncaoVO) context().getExternalContext().getRequestMap().get("funcao");
		setHistoricoFuncaoVO(objeto);
	}

	public void editarHistoricoSalarial() {
		HistoricoSalarialVO objeto = (HistoricoSalarialVO) context().getExternalContext().getRequestMap().get("salarial");
		setHistoricoSalarialVO(objeto);
	}

	public void editarHistoricoSecao() {
		HistoricoSecaoVO objeto = (HistoricoSecaoVO) context().getExternalContext().getRequestMap().get("secaofolha");
		setHistoricoSecaoVO(objeto);
	}

	public void editarHistoricoSituacao() {
		HistoricoSituacaoVO objeto = (HistoricoSituacaoVO) context().getExternalContext().getRequestMap().get("situacao");
		setHistoricoSituacaoVO(objeto);
	}

	public void editarHistoricoAfastamento() {
		HistoricoAfastamentoVO objeto = (HistoricoAfastamentoVO) context().getExternalContext().getRequestMap().get("tableAfastamento");
		setHistoricoAfastamentoVO(objeto);
	}

	public void editarHistoricoPensao() {
		HistoricoPensaoVO objeto = (HistoricoPensaoVO) context().getExternalContext().getRequestMap().get("tablePensao");
		setHistoricoPensaoVO(objeto);
	}

	/**
	 * Grava o {@link HistoricoDependentesVO}.
	 */
	public void persistirHistoricoDependente() {
		try {
			getHistoricoDependentesVO().setFuncionarioVO(getFuncionarioCargo().getFuncionarioVO());
			getFacadeFactory().getHistoricoDependentesInterfaceFacade().persistir(getHistoricoDependentesVO(), false, getUsuarioLogado());
			consultarDadosHistoricoDependentes();
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	}

	/**
	 * Grava o {@link HistoricoFuncaoVO}.
	 */
	public void persistirHistoricoFuncao() {
		try {
			getHistoricoFuncaoVO().setFuncionarioCargo(getFuncionarioCargo());
			getFacadeFactory().getHistoricoFuncaoInterfaceFacade().persistir(getHistoricoFuncaoVO(), false, getUsuarioLogado());
			consultarDadosHistoricoFuncao();
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	}
	
	/**
	 * Grava o {@link HistoricoSalarialVO}.
	 */
	public void persistirHistoricoSalarial() {
		try {
			getHistoricoSalarialVO().setFuncionarioCargo(getFuncionarioCargo());
			getFacadeFactory().getHistoricoSalarialInterfaceFacade().persistir(getHistoricoSalarialVO(), false, getUsuarioLogado());
			consultarDadosHistoricoSalarial();
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	}
	
	/**
	 * Grava o {@link HistoricoSecaoVO}.
	 */
	public void persistirHistoricoSecao() {
		try {
			getHistoricoSecaoVO().setFuncionarioCargo(getFuncionarioCargo());
			getFacadeFactory().getHistoricoSecaoInterfaceFacade().persistir(getHistoricoSecaoVO(), false, getUsuarioLogado());
			consultarDadosHistoricoSecao();
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	}
	
	/**
	 * Grava o {@link HistoricoSituacaoVO}.
	 */
	public void persistirHistoricoSituacao() {
		try {
			getHistoricoSituacaoVO().setFuncionarioCargo(getFuncionarioCargo());
			getFacadeFactory().getHistoricoSituacaoInterfaceFacade().persistir(getHistoricoSituacaoVO(), false, getUsuarioLogado());
			consultarDadosHistoricoSituacao();
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	}
	
	/**
	 * Grava o {@link HistoricoAfastamentoVO}.
	 */
	public void persistirHistoricoAfastamento() {
		try {
			getHistoricoAfastamentoVO().setFuncionarioCargo(getFuncionarioCargo());
			getFacadeFactory().getHistoricoAfastamentoInterfaceFacade().persistir(getHistoricoAfastamentoVO(), false, getUsuarioLogado());
			consultarDadosHistoricoAfastamento();
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	}
	
	/**
	 * Grava o {@link HistoricoPensaoVO}.
	 */
	public void persistirHistoricoPensao() {
		try {
			getHistoricoPensaoVO().setCompetenciaFolhaPagamento(getCompetenciaFolhaPagamentoVO());
			getFacadeFactory().getHistoricoPensaoInterfaceFacade().persistir(getHistoricoPensaoVO(), false, getUsuarioLogado());
			consultarDadosHistoricoPensao();
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	}
	
	public void montarListaSelectItemCargo() {
        try {
            List<CargoVO> resultadoConsulta = getFacadeFactory().getCargoFacade().consultaRapidaPorNome("", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            List<SelectItem> objs = new ArrayList<>();
            for (CargoVO cargoVO : resultadoConsulta) {
            	 objs.add(new SelectItem(cargoVO.getCodigo(), cargoVO.getNome()));
			}
            setListaSelectItemCargo(objs);
        } catch (Exception e) {
        	setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
	
	public void montarNivelSalarial() {
		try {
            List<NivelSalarialVO> lista = getFacadeFactory().getNivelSalarialInterfaceFacade().consultarListaDeNivelSalarial();
            List<SelectItem> objs = new ArrayList<>();
            for (NivelSalarialVO nivelSalarialVO : lista) {
            	 objs.add(new SelectItem(nivelSalarialVO.getCodigo(), nivelSalarialVO.getDescricao()));
			}
            setListaSelectItemNivelSalarial(objs);
        } catch (Exception e) {
        	setMensagemDetalhada("msg_erro", e.getMessage());
        }
	}

	public void montarSecaoFolhaPagamento() {
		try {
			List<SecaoFolhaPagamentoVO> lista = getFacadeFactory().getSecaoFolhaPagamentoInterfaceFacade().consultar("DESCRICAO", "%%", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			List<SelectItem> objs = new ArrayList<>();
			for (SecaoFolhaPagamentoVO secaoFolhaPagamento : lista) {
				objs.add(new SelectItem(secaoFolhaPagamento.getCodigo(), secaoFolhaPagamento.getDescricao()));
			}
			setListaSelectItemSecaoFolhaPagamento(objs);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void montarFuncionarioDependente() throws Exception {
		try {
			List<FuncionarioDependenteVO> lista = getFacadeFactory().getFuncionarioDependenteInterfaceFacade().consultarPorFuncionario(getFuncionarioCargo().getFuncionarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			List<SelectItem> objs = new ArrayList<>();
			for (FuncionarioDependenteVO funcionarioDependente : lista) {
				objs.add(new SelectItem(funcionarioDependente.getCodigo(), funcionarioDependente.getNome()));
			}
			setListaSelectItemFuncionarioDependente(objs);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarFaixaSalarial() {
		try {
			List<FaixaSalarialVO> lista = getFacadeFactory().getFaixaSalarialInterfaceFacade().consultarListaDeFaixaSalarial();
			List<SelectItem> objs = new ArrayList<>();
			for (FaixaSalarialVO faixaSalarialVO : lista) {
				objs.add(new SelectItem(faixaSalarialVO.getCodigo(), faixaSalarialVO.getDescricao()));
			}
			setListaSelectItemFaixaSalarial(objs);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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
	
	public boolean getApresentarResultadoConsulta() {
		return getControleConsultaOtimizado().getListaConsulta().size() > 0;
	}
	
	public boolean getApresentarPaginadorResultadoConsulta() {
		return getControleConsultaOtimizado().getTotalRegistrosEncontrados() > 10;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de
	 * uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setCampoConsulta(EnumCampoConsultaHistoricoDependentes.FUNCIONARIO.name());
		setListaConsulta(new ArrayList<>(0));
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}
	
	public void inicializarValoresHistoricos() {
		try {
			montarListaSelectItemCargo();
			montarFaixaSalarial();
			montarNivelSalarial();
			montarSecaoFolhaPagamento();
			montarFuncionarioDependente();
			setCompetenciaFolhaPagamentoVO(getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarCompetenciaAtiva(true));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Evento de consulta paginada da tela pesquisa do Funcionario Cargo da tela de historicoFuncionarioCons
	 * 
	 * @param dataScrollerEvent
	 */
	public void scrollerListener(DataScrollEvent dataScrollerEvent) {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		this.consultarDados();
	}

	/**
	 * Evento de consulta paginada do historico dependentes
	 * 
	 * @param dataScrollerEvent
	 */
	public void scrollerListenerHistoricoDependentes(DataScrollEvent dataScrollerEvent) {
		getDataModeloHistoricoDependentes().setPaginaAtual(dataScrollerEvent.getPage());
		getDataModeloHistoricoDependentes().setPage(dataScrollerEvent.getPage());
		this.consultarDadosHistoricoDependentes();
	}

	/**
	 * Evento de consulta paginada do historico funcao
	 * 
	 * @param dataScrollerEvent
	 */
	public void scrollerListenerHistoricoFuncao(DataScrollEvent dataScrollerEvent) {
		getDataModeloHistoricoFuncao().setPaginaAtual(dataScrollerEvent.getPage());
		getDataModeloHistoricoFuncao().setPage(dataScrollerEvent.getPage());
		this.consultarDadosHistoricoFuncao();
	}

	/**
	 * Evento de consulta paginada do historico salarial
	 * 
	 * @param dataScrollerEvent
	 */
	public void scrollerListenerHistoricoSalarial(DataScrollEvent dataScrollerEvent) {
		getDataModeloHistoricoSalarial().setPaginaAtual(dataScrollerEvent.getPage());
		getDataModeloHistoricoSalarial().setPage(dataScrollerEvent.getPage());
		this.consultarDadosHistoricoSalarial();
	}

	/**
	 * Evento de consulta paginada do historico secao
	 * 
	 * @param dataScrollerEvent
	 */
	public void scrollerListenerHistoricoSecao(DataScrollEvent dataScrollerEvent) {
		getDataModeloHistoricoSecao().setPaginaAtual(dataScrollerEvent.getPage());
		getDataModeloHistoricoSecao().setPage(dataScrollerEvent.getPage());
		this.consultarDadosHistoricoSecao();
	}

	/**
	 * Evento de consulta paginada do historico situacao
	 * 
	 * @param dataScrollerEvent
	 */
	public void scrollerListenerHistoricoSituacao(DataScrollEvent dataScrollerEvent) {
		getDataModeloHistoricoSituacao().setPaginaAtual(dataScrollerEvent.getPage());
		getDataModeloHistoricoSituacao().setPage(dataScrollerEvent.getPage());
		this.consultarDadosHistoricoSituacao();
	}

	/**
	 * Evento de consulta paginada do historico afastamento
	 * 
	 * @param dataScrollerEvent
	 */
	public void scrollerListenerHistoricoAfastamento(DataScrollEvent dataScrollerEvent) {
		getDataModeloHistoricoAfastamento().setPaginaAtual(dataScrollerEvent.getPage());
		getDataModeloHistoricoAfastamento().setPage(dataScrollerEvent.getPage());
		this.consultarDadosHistoricoAfastamento();
	}

	/**
	 * Evento de consulta paginada do historico pensao
	 * 
	 * @param dataScrollerEvent
	 */
	public void scrollerListenerHistoricoPensao(DataScrollEvent dataScrollerEvent) {
		getDataModeloHistoricoPensao().setPaginaAtual(dataScrollerEvent.getPage());
		getDataModeloHistoricoPensao().setPage(dataScrollerEvent.getPage());
		this.consultarDadosHistoricoPensao();
	}

	public void deletarHistoricoFuncao(HistoricoFuncaoVO obj) {
		try {
			getFacadeFactory().getHistoricoFuncaoInterfaceFacade().excluir(obj, false, getUsuarioLogado());
			this.consultarDadosHistoricoFuncao();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void deletarHistoricoSalarial(HistoricoSalarialVO obj) {
		try {
			getFacadeFactory().getHistoricoSalarialInterfaceFacade().excluir(obj, false, getUsuarioLogado());
			this.consultarDadosHistoricoSalarial();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void deletarHistoricoSituacao(HistoricoSituacaoVO obj) {
		try {
			getFacadeFactory().getHistoricoSituacaoInterfaceFacade().excluir(obj, false, getUsuarioLogado());
			this.consultarDadosHistoricoSituacao();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void deletarHistoricoPensao(HistoricoPensaoVO obj) {
		try {
			getFacadeFactory().getHistoricoPensaoInterfaceFacade().excluir(obj, false, getUsuarioLogado());
			this.consultarDadosHistoricoPensao();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void deletarHistoricoSecao(HistoricoSecaoVO obj) {
		try {
			getFacadeFactory().getHistoricoSecaoInterfaceFacade().excluir(obj, false, getUsuarioLogado());
			this.consultarDadosHistoricoSecao();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void deletarHistoricoDependentes(HistoricoDependentesVO obj) {
		try {
			getFacadeFactory().getHistoricoDependentesInterfaceFacade().excluir(obj, false, getUsuarioLogado());
			this.consultarDadosHistoricoDependentes();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void deletarHistoricoAfastamento(HistoricoAfastamentoVO obj) {
		try {
			getFacadeFactory().getHistoricoAfastamentoInterfaceFacade().excluir(obj, false, getUsuarioLogado());
			this.consultarDadosHistoricoAfastamento();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparHistoricos() {
		dataModeloHistoricoDependentes = new DataModelo();
		dataModeloHistoricoFuncao = new DataModelo();
		dataModeloHistoricoSalarial = new DataModelo();
		dataModeloHistoricoSecao = new DataModelo();
		dataModeloHistoricoSituacao = new DataModelo();
		dataModeloHistoricoAfastamento = new DataModelo();
		dataModeloHistoricoSecao = new DataModelo();
	}

	public void preencherDadosParaConsulta(DataModelo modelo) {
		if (modelo == null) {
			modelo = new DataModelo();
		}

		modelo.setCampoConsulta("MATRICULA_CARGO");
		modelo.setValorConsulta(getFuncionarioCargo().getMatriculaCargo());
	}

	// GETTER AND SETTER
	public FuncionarioCargoVO getFuncionarioCargo() {
		if (funcionarioCargo == null) {
			funcionarioCargo = new FuncionarioCargoVO();
		}
		return funcionarioCargo;
	}

	public void setFuncionarioCargo(FuncionarioCargoVO funcionarioCargo) {
		this.funcionarioCargo = funcionarioCargo;
	}
	
	public String getSituacaoFuncionario() {
		if (situacaoFuncionario == null) {
			situacaoFuncionario = "TODOS";
		}
		return situacaoFuncionario;
	}

	public void setSituacaoFuncionario(String situacaoFuncionario) {
		this.situacaoFuncionario = situacaoFuncionario;
	}

	public DataModelo getDataModeloHistoricoDependentes() {
		if (dataModeloHistoricoDependentes == null) {
			dataModeloHistoricoDependentes = new DataModelo();
		}
		return dataModeloHistoricoDependentes;
	}

	public void setDataModeloHistoricoDependentes(DataModelo dataModeloHistoricoDependentes) {
		this.dataModeloHistoricoDependentes = dataModeloHistoricoDependentes;
	}

	public DataModelo getDataModeloHistoricoFuncao() {
		if (dataModeloHistoricoFuncao == null) {
			dataModeloHistoricoFuncao = new DataModelo();
		}
		return dataModeloHistoricoFuncao;
	}

	public void setDataModeloHistoricoFuncao(DataModelo dataModeloHistoricoFuncao) {
		this.dataModeloHistoricoFuncao = dataModeloHistoricoFuncao;
	}

	public DataModelo getDataModeloHistoricoSalarial() {
		if (dataModeloHistoricoSalarial == null) {
			dataModeloHistoricoSalarial = new DataModelo();
		}
		return dataModeloHistoricoSalarial;
	}

	public void setDataModeloHistoricoSalarial(DataModelo dataModeloHistoricoSalarial) {
		this.dataModeloHistoricoSalarial = dataModeloHistoricoSalarial;
	}

	public DataModelo getDataModeloHistoricoSecao() {
		if (dataModeloHistoricoSecao == null) {
			dataModeloHistoricoSecao = new DataModelo();
		}
		return dataModeloHistoricoSecao;
	}

	public void setDataModeloHistoricoSecao(DataModelo dataModeloHistoricoSecao) {
		this.dataModeloHistoricoSecao = dataModeloHistoricoSecao;
	}

	public DataModelo getDataModeloHistoricoSituacao() {
		if (dataModeloHistoricoSituacao == null) {
			dataModeloHistoricoSituacao = new DataModelo();
		}
		return dataModeloHistoricoSituacao;
	}

	public void setDataModeloHistoricoSituacao(DataModelo dataModeloHistoricoSituacao) {
		this.dataModeloHistoricoSituacao = dataModeloHistoricoSituacao;
	}

	public DataModelo getDataModeloHistoricoAfastamento() {
		if (dataModeloHistoricoAfastamento == null) {
			dataModeloHistoricoAfastamento = new DataModelo();
		}
		return dataModeloHistoricoAfastamento;
	}

	public void setDataModeloHistoricoAfastamento(DataModelo dataModeloHistoricoAfastamento) {
		this.dataModeloHistoricoAfastamento = dataModeloHistoricoAfastamento;
	}

	public String getAbaSelecionada() {
		if (abaSelecionada == null) {
			abaSelecionada = "primeiraTab";
		}
		return abaSelecionada;
	}

	public void setAbaSelecionada(String abaSelecionada) {
		this.abaSelecionada = abaSelecionada;
	}

	public DataModelo getDataModeloHistoricoPensao() {
		if (dataModeloHistoricoPensao == null) {
			dataModeloHistoricoPensao = new DataModelo();
		}
		return dataModeloHistoricoPensao;
	}

	public void setDataModeloHistoricoPensao(DataModelo dataModeloHistoricoPensao) {
		this.dataModeloHistoricoPensao = dataModeloHistoricoPensao;
	}

	public HistoricoDependentesVO getHistoricoDependentesVO() {
		if (historicoDependentesVO == null) {
			historicoDependentesVO = new HistoricoDependentesVO();
		}
		return historicoDependentesVO;
	}

	public void setHistoricoDependentesVO(HistoricoDependentesVO historicoDependentesVO) {
		this.historicoDependentesVO = historicoDependentesVO;
	}

	public HistoricoFuncaoVO getHistoricoFuncaoVO() {
		if (historicoFuncaoVO == null) {
			historicoFuncaoVO = new HistoricoFuncaoVO();
		}
		return historicoFuncaoVO;
	}

	public void setHistoricoFuncaoVO(HistoricoFuncaoVO historicoFuncaoVO) {
		this.historicoFuncaoVO = historicoFuncaoVO;
	}

	public HistoricoSalarialVO getHistoricoSalarialVO() {
		if (historicoSalarialVO == null) {
			historicoSalarialVO = new HistoricoSalarialVO();
		}
		return historicoSalarialVO;
	}

	public void setHistoricoSalarialVO(HistoricoSalarialVO historicoSalarialVO) {
		this.historicoSalarialVO = historicoSalarialVO;
	}

	public HistoricoSecaoVO getHistoricoSecaoVO() {
		if (historicoSecaoVO == null) {
			historicoSecaoVO = new HistoricoSecaoVO();
		}
		return historicoSecaoVO;
	}

	public void setHistoricoSecaoVO(HistoricoSecaoVO historicoSecaoVO) {
		this.historicoSecaoVO = historicoSecaoVO;
	}

	public HistoricoSituacaoVO getHistoricoSituacaoVO() {
		if (historicoSituacaoVO == null) {
			historicoSituacaoVO = new HistoricoSituacaoVO();
		}
		return historicoSituacaoVO;
	}

	public void setHistoricoSituacaoVO(HistoricoSituacaoVO historicoSituacaoVO) {
		this.historicoSituacaoVO = historicoSituacaoVO;
	}

	public HistoricoAfastamentoVO getHistoricoAfastamentoVO() {
		if (historicoAfastamentoVO == null) {
			historicoAfastamentoVO = new HistoricoAfastamentoVO();
		}
		return historicoAfastamentoVO;
	}

	public void setHistoricoAfastamentoVO(HistoricoAfastamentoVO historicoAfastamentoVO) {
		this.historicoAfastamentoVO = historicoAfastamentoVO;
	}

	public HistoricoPensaoVO getHistoricoPensaoVO() {
		if (historicoPensaoVO == null) {
			historicoPensaoVO = new HistoricoPensaoVO();
		}
		return historicoPensaoVO;
	}

	public void setHistoricoPensaoVO(HistoricoPensaoVO historicoPensaoVO) {
		this.historicoPensaoVO = historicoPensaoVO;
	}

	public List<SelectItem> getListaSelectItemCargo() {
		if (listaSelectItemCargo == null) {
			listaSelectItemCargo = new ArrayList<>();
		}
		return listaSelectItemCargo;
	}

	public void setListaSelectItemCargo(List<SelectItem> listaSelectItemCargo) {
		this.listaSelectItemCargo = listaSelectItemCargo;
	}

	public List<SelectItem> getListaSelectItemNivelSalarial() {
		if (listaSelectItemNivelSalarial == null) {
			listaSelectItemNivelSalarial = new ArrayList<>();
		}
		return listaSelectItemNivelSalarial;
	}

	public void setListaSelectItemNivelSalarial(List<SelectItem> listaSelectItemNivelSalarial) {
		this.listaSelectItemNivelSalarial = listaSelectItemNivelSalarial;
	}

	public List<SelectItem> getListaSelectItemFaixaSalarial() {
		if (listaSelectItemFaixaSalarial == null) {
			listaSelectItemFaixaSalarial = new ArrayList<>();
		}
		return listaSelectItemFaixaSalarial;
	}

	public void setListaSelectItemFaixaSalarial(List<SelectItem> listaSelectItemFaixaSalarial) {
		this.listaSelectItemFaixaSalarial = listaSelectItemFaixaSalarial;
	}

	public List<SelectItem> getListaSelectItemSecaoFolhaPagamento() {
		if (listaSelectItemSecaoFolhaPagamento == null) {
			listaSelectItemSecaoFolhaPagamento = new ArrayList<>();
		}
		return listaSelectItemSecaoFolhaPagamento;
	}

	public void setListaSelectItemSecaoFolhaPagamento(List<SelectItem> listaSelectItemSecaoFolhaPagamento) {
		this.listaSelectItemSecaoFolhaPagamento = listaSelectItemSecaoFolhaPagamento;
	}

	public List<SelectItem> getListaSelectItemFuncionarioDependente() {
		if (listaSelectItemFuncionarioDependente == null) {
			listaSelectItemFuncionarioDependente = new ArrayList<>();
		}
		return listaSelectItemFuncionarioDependente;
	}

	public void setListaSelectItemFuncionarioDependente(List<SelectItem> listaSelectItemFuncionarioDependente) {
		this.listaSelectItemFuncionarioDependente = listaSelectItemFuncionarioDependente;
	}

	public CompetenciaFolhaPagamentoVO getCompetenciaFolhaPagamentoVO() {
		if (competenciaFolhaPagamentoVO == null) {
			competenciaFolhaPagamentoVO = new CompetenciaFolhaPagamentoVO();
		}
		return competenciaFolhaPagamentoVO;
	}

	public void setCompetenciaFolhaPagamentoVO(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) {
		this.competenciaFolhaPagamentoVO = competenciaFolhaPagamentoVO;
	}
}