package controle.academico;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.GestaoXmlGradeCurricularVO;
import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.academico.GestaoXmlGradeCurricular;
import relatorio.controle.arquitetura.SuperControleRelatorio;

@Controller("GestaoXmlGradeCurricularControle")
@Scope("viewScope")
@Lazy
public class GestaoXmlGradeCurricularControle extends SuperControleRelatorio implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String LAYOUT_PADRAO = "LAYOUT_PADRAO";
	private static final String INDIVIDUAL = "INDIVIDUAL";
	private static final String LOTE = "LOTE";

	private GestaoXmlGradeCurricularVO gestaoXmlGradeCurricularSelecionado;
	private GestaoXmlGradeCurricularVO gestaoXmlGradeCurricularVO;
	private DocumentoAssinadoVO documentoAssinadoVO;
	private FuncionarioVO funcionarioIesEmissora;
	private CargoVO cargoFuncionarioIesEmissora;
	private ProgressBarVO progressBarVO;
	private String tituloFuncionarioIesEmissora;
	private String campoConsultaFuncionario;
	private String valorConsultaFuncionario;
	private String motivoRejeicao;
	private Boolean apresentarMotivoRejeicaoLote;
	private Boolean versaoLayoutListaNovo;
	private Boolean buscarFuncionario;
	private Boolean selecionarTodos;
	private List<SelectItem> listaSelectItemCargoFuncionarioIesEmissora;
	private List<SelectItem> listaSelectItemSituacaoMatrizCurricular;
	private List<SelectItem> listaSelectItemCargoFuncionarioPrimario;
	private List<SelectItem> tipoConsultaComboFuncionario;
	private List<FuncionarioVO> ListaConsultaFuncionario;
	private List<SelectItem> listaSelectItemLimitePagina;
	private List<SelectItem> listaSelectItemSituacaoXml;
	private List<SelectItem> listaSelectItemFuncionario;
	private List<String> listaMensagensErro;

	@PostConstruct
	public void init() {
		consultarUnidadeEnsinoLista();
		consultarCursoLista();
		consultarLayoutListaDocumentoAssinadoPadrao();
		montarLayoutPadraoFuncionarioIes();
	}

	public GestaoXmlGradeCurricularVO getGestaoXmlGradeCurricularSelecionado() {
		if (gestaoXmlGradeCurricularSelecionado == null) {
			gestaoXmlGradeCurricularSelecionado = new GestaoXmlGradeCurricularVO();
		}
		return gestaoXmlGradeCurricularSelecionado;
	}

	public void setGestaoXmlGradeCurricularSelecionado(GestaoXmlGradeCurricularVO gestaoXmlGradeCurricularSelecionado) {
		this.gestaoXmlGradeCurricularSelecionado = gestaoXmlGradeCurricularSelecionado;
	}

	public GestaoXmlGradeCurricularVO getGestaoXmlGradeCurricularVO() {
		if (gestaoXmlGradeCurricularVO == null) {
			gestaoXmlGradeCurricularVO = new GestaoXmlGradeCurricularVO();
		}
		return gestaoXmlGradeCurricularVO;
	}

	public void setGestaoXmlGradeCurricularVO(GestaoXmlGradeCurricularVO gestaoXmlGradeCurricularVO) {
		this.gestaoXmlGradeCurricularVO = gestaoXmlGradeCurricularVO;
	}

	public String getCampoConsultaFuncionario() {
		if (campoConsultaFuncionario == null) {
			campoConsultaFuncionario = Constantes.EMPTY;
		}
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		if (valorConsultaFuncionario == null) {
			valorConsultaFuncionario = Constantes.EMPTY;
		}
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public List<SelectItem> getTipoConsultaComboFuncionario() {
		if (tipoConsultaComboFuncionario == null) {
			tipoConsultaComboFuncionario = new ArrayList<>(0);
			tipoConsultaComboFuncionario.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboFuncionario.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultaComboFuncionario.add(new SelectItem("CPF", "CPF"));
			tipoConsultaComboFuncionario.add(new SelectItem("cargo", "Cargo"));
			tipoConsultaComboFuncionario.add(new SelectItem("departamento", "Departamento"));
		}
		return tipoConsultaComboFuncionario;
	}

	public DocumentoAssinadoVO getDocumentoAssinadoVO() {
		if (documentoAssinadoVO == null) {
			documentoAssinadoVO = new DocumentoAssinadoVO();
		}
		return documentoAssinadoVO;
	}

	public void setDocumentoAssinadoVO(DocumentoAssinadoVO documentoAssinadoVO) {
		this.documentoAssinadoVO = documentoAssinadoVO;
	}
	
	public FuncionarioVO getFuncionarioIesEmissora() {
		if (funcionarioIesEmissora == null) {
			funcionarioIesEmissora = new FuncionarioVO();
		}
		return funcionarioIesEmissora;
	}

	public void setFuncionarioIesEmissora(FuncionarioVO funcionarioVO) {
		this.funcionarioIesEmissora = funcionarioVO;
	}

	public String getMotivoRejeicao() {
		if (motivoRejeicao == null) {
			motivoRejeicao = Constantes.EMPTY;
		}
		return motivoRejeicao;
	}
	
	public void setMotivoRejeicao(String motivoRejeicao) {
		this.motivoRejeicao = motivoRejeicao;
	}
	
	public Boolean getApresentarMotivoRejeicaoLote() {
		if (apresentarMotivoRejeicaoLote == null) {
			apresentarMotivoRejeicaoLote = Boolean.FALSE;
		}
		return apresentarMotivoRejeicaoLote;
	}
	
	public void setApresentarMotivoRejeicaoLote(Boolean apresentarMotivoRejeicaoLote) {
		this.apresentarMotivoRejeicaoLote = apresentarMotivoRejeicaoLote;
	}
	
	public Boolean getVersaoLayoutListaNovo() {
		if (versaoLayoutListaNovo == null) {
			versaoLayoutListaNovo = Boolean.FALSE;
		}
		return versaoLayoutListaNovo;
	}

	public void setVersaoLayoutListaNovo(Boolean versaoLayoutListaNovo) {
		this.versaoLayoutListaNovo = versaoLayoutListaNovo;
	}

	public Boolean getBuscarFuncionario() {
		if (buscarFuncionario == null) {
			buscarFuncionario = Boolean.FALSE;
		}
		return buscarFuncionario;
	}

	public void setBuscarFuncionario(Boolean buscarFuncionario) {
		this.buscarFuncionario = buscarFuncionario;
	}

	public List<SelectItem> getListaSelectItemLimitePagina() {
		if (listaSelectItemLimitePagina == null) {
			listaSelectItemLimitePagina = new ArrayList<>(0);
			listaSelectItemLimitePagina.add(new SelectItem(10, "10"));
			listaSelectItemLimitePagina.add(new SelectItem(20, "20"));
			listaSelectItemLimitePagina.add(new SelectItem(30, "30"));
			listaSelectItemLimitePagina.add(new SelectItem(40, "40"));
			listaSelectItemLimitePagina.add(new SelectItem(50, "50"));
			listaSelectItemLimitePagina.add(new SelectItem(100, "100"));
		}
		return listaSelectItemLimitePagina;
	}

	public void setListaSelectItemLimitePagina(List<SelectItem> listaSelectItemLimitePagina) {
		this.listaSelectItemLimitePagina = listaSelectItemLimitePagina;
	}

	public List<SelectItem> getListaSelectItemSituacaoMatrizCurricular() {
		if (listaSelectItemSituacaoMatrizCurricular == null) {
			listaSelectItemSituacaoMatrizCurricular = new ArrayList<>(0);
			listaSelectItemSituacaoMatrizCurricular.add(new SelectItem(Constantes.EMPTY, "Todas"));
			listaSelectItemSituacaoMatrizCurricular.add(new SelectItem("AT", "Ativa"));
			listaSelectItemSituacaoMatrizCurricular.add(new SelectItem("IN", "Inativa"));
		}
		return listaSelectItemSituacaoMatrizCurricular;
	}

	public void setListaSelectItemSituacaoMatrizCurricular(List<SelectItem> listaSelectItemSituacaoMatrizCurricular) {
		this.listaSelectItemSituacaoMatrizCurricular = listaSelectItemSituacaoMatrizCurricular;
	}

	public List<SelectItem> getListaSelectItemSituacaoXml() {
		if (listaSelectItemSituacaoXml == null) {
			listaSelectItemSituacaoXml = new ArrayList<>(0);
			listaSelectItemSituacaoXml.add(new SelectItem(Constantes.EMPTY, "Todas"));
			listaSelectItemSituacaoXml.add(new SelectItem("GERACAO_PENDENTE", "Não Gerado"));
			listaSelectItemSituacaoXml.add(new SelectItem("PENDENTE_ASSINATURA", "Gerado Com Pendência de Assinatura"));
			listaSelectItemSituacaoXml.add(new SelectItem("ASSINADO", "Gerado e Assinado"));
		}
		return listaSelectItemSituacaoXml;
	}

	public void setListaSelectItemSituacaoXml(List<SelectItem> listaSelectItemSituacaoXml) {
		this.listaSelectItemSituacaoXml = listaSelectItemSituacaoXml;
	}

	public List<SelectItem> getListaSelectItemFuncionario() {
		if (listaSelectItemFuncionario == null) {
			listaSelectItemFuncionario = new ArrayList<>(0);
		}
		return listaSelectItemFuncionario;
	}

	public void setListaSelectItemFuncionario(List<SelectItem> listaSelectItemFuncionario) {
		this.listaSelectItemFuncionario = listaSelectItemFuncionario;
	}

	public List<String> getListaMensagensErro() {
		if (listaMensagensErro == null) {
			listaMensagensErro = new ArrayList<>(0);
		}
		return listaMensagensErro;
	}

	public void setListaMensagensErro(List<String> listaMensagensErro) {
		this.listaMensagensErro = listaMensagensErro;
	}

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		if (ListaConsultaFuncionario == null) {
			ListaConsultaFuncionario = new ArrayList<>(0);
		}
		return ListaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		ListaConsultaFuncionario = listaConsultaFuncionario;
	}

	public List<SelectItem> getListaSelectItemCargoFuncionarioPrimario() {
		if (listaSelectItemCargoFuncionarioPrimario == null) {
			listaSelectItemCargoFuncionarioPrimario = new ArrayList<>(0);
		}
		return listaSelectItemCargoFuncionarioPrimario;
	}

	public void setListaSelectItemCargoFuncionarioPrimario(List<SelectItem> listaSelectItemCargoFuncionarioPrimario) {
		this.listaSelectItemCargoFuncionarioPrimario = listaSelectItemCargoFuncionarioPrimario;
	}

	public CargoVO getCargoFuncionarioIesEmissora() {
		if (cargoFuncionarioIesEmissora == null) {
			cargoFuncionarioIesEmissora = new CargoVO();
		}
		return cargoFuncionarioIesEmissora;
	}

	public void setCargoFuncionarioIesEmissora(CargoVO cargoFuncionarioIesEmissora) {
		this.cargoFuncionarioIesEmissora = cargoFuncionarioIesEmissora;
	}
	
	public ProgressBarVO getProgressBarVO() {
		if (progressBarVO == null) {
			progressBarVO = new ProgressBarVO();
		}
		return progressBarVO;
	}
	
	public void setProgressBarVO(ProgressBarVO progressBarVO) {
		this.progressBarVO = progressBarVO;
	}
	
	public String getTituloFuncionarioIesEmissora() {
		if (tituloFuncionarioIesEmissora == null) {
			tituloFuncionarioIesEmissora = Constantes.EMPTY;
		}
		return tituloFuncionarioIesEmissora;
	}

	public void setTituloFuncionarioIesEmissora(String tituloFuncionarioIesEmissora) {
		this.tituloFuncionarioIesEmissora = tituloFuncionarioIesEmissora;
	}

	public List<SelectItem> getListaSelectItemCargoFuncionarioIesEmissora() {
		if (listaSelectItemCargoFuncionarioIesEmissora == null) {
			listaSelectItemCargoFuncionarioIesEmissora = new ArrayList<>(0);
		}
		return listaSelectItemCargoFuncionarioIesEmissora;
	}

	public void setListaSelectItemCargoFuncionarioIesEmissora(List<SelectItem> listaSelectItemCargoFuncionarioIesEmissora) {
		this.listaSelectItemCargoFuncionarioIesEmissora = listaSelectItemCargoFuncionarioIesEmissora;
	}
	
	public Boolean getSelecionarTodos() {
		if (selecionarTodos == null) {
			selecionarTodos = Boolean.FALSE;
		}
		return selecionarTodos;
	}
	
	public void setSelecionarTodos(Boolean selecionarTodos) {
		this.selecionarTodos = selecionarTodos;
	}
	
	@SuppressWarnings("unchecked")
	public Boolean getApresentarBotaoLote() {
		if (Uteis.isAtributoPreenchido(getGestaoXmlGradeCurricularVO().getControleConsultaOtimizado().getListaConsulta())) {
			List<GestaoXmlGradeCurricularVO> listaConsulta = (List<GestaoXmlGradeCurricularVO>) getGestaoXmlGradeCurricularVO().getControleConsultaOtimizado().getListaConsulta();
			return listaConsulta.stream().anyMatch(GestaoXmlGradeCurricularVO::getSelecionado);
		} else {
			return Boolean.FALSE;
		}
	}

	public void consultarUnidadeEnsinoLista() {
		try {
			if (!Uteis.isAtributoPreenchido(getUnidadeEnsinoVOs())) {
				setUnidadeEnsinoVOs(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome(Constantes.EMPTY, null, Boolean.FALSE, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				verificarTodasUnidadeEnsinoSelecionados();
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCursoLista() {
		try {
			List<UnidadeEnsinoVO> unidadeEnsinoVOs = Uteis.isAtributoPreenchido(getUnidadeEnsinoVOs()) ? getUnidadeEnsinoVOs().stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).collect(Collectors.toList()) : new ArrayList<>(0);
			if (!Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
				setCursoVOs(getFacadeFactory().getCursoFacade().consultarCursoPorUnidadeEnsinoNivelEducacional(new ArrayList<>(), Arrays.asList(TipoNivelEducacional.GRADUACAO_TECNOLOGICA, TipoNivelEducacional.SUPERIOR), getUsuarioLogado()));
				setCursosApresentar(Constantes.EMPTY);
			} else {
				setCursoVOs(getFacadeFactory().getCursoFacade().consultarCursoPorUnidadeEnsinoNivelEducacional(unidadeEnsinoVOs, Arrays.asList(TipoNivelEducacional.GRADUACAO_TECNOLOGICA, TipoNivelEducacional.SUPERIOR), getUsuarioLogado()));
				verificarTodosCursosSelecionados();
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void marcarTodasUnidadesEnsinoAction() {
		super.marcarTodasUnidadesEnsinoAction();
		consultarCursoLista();
	}

	public void verificarTodasUnidadeEnsinoSelecionados() {
		super.verificarTodasUnidadeEnsinoSelecionados();
		consultarCursoLista();
	}

	public String consultar() {
		try {
			getGestaoXmlGradeCurricularVO().limparDados();
			getGestaoXmlGradeCurricularVO().validarDadosConsulta(getUnidadeEnsinoVOs(), getCursoVOs());
			getFacadeFactory().getGestaoXmlGradeCurricularInterfaceFacade().consultar(getGestaoXmlGradeCurricularVO());
			setSelecionarTodos(Boolean.FALSE);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Constantes.EMPTY;
	}

	public void scrollerListener(DataScrollEvent dataScrollEvent) throws Exception {
		getGestaoXmlGradeCurricularVO().getControleConsultaOtimizado().setPaginaAtual(dataScrollEvent.getPage());
		getGestaoXmlGradeCurricularVO().getControleConsultaOtimizado().setPage(dataScrollEvent.getPage());
		consultar();
	}
	
	private void montarDadosFuncionarioIesEmissora(GestaoXmlGradeCurricularVO obj) throws Exception {
		if (Uteis.isAtributoPreenchido(getFuncionarioIesEmissora())) {
			obj.setFuncionarioSecundario((FuncionarioVO) getFuncionarioIesEmissora().clone());
			obj.setCargoSecundario((CargoVO) getCargoFuncionarioIesEmissora().clone());
			obj.setTituloFuncionarioSecundario(new String(getTituloFuncionarioIesEmissora()));
			montarListaSelectItemCargoFuncionarioIesEmissora(LAYOUT_PADRAO);
		} else {
			obj.setFuncionarioSecundario(new FuncionarioVO());
			obj.setCargoSecundario(new CargoVO());
			obj.setTituloFuncionarioSecundario(Constantes.EMPTY);
		}
	}

	public void selecionarMatrizCurricular() {
		try {
			setBuscarFuncionario(Boolean.FALSE);
			setGestaoXmlGradeCurricularSelecionado(new GestaoXmlGradeCurricularVO());
			GestaoXmlGradeCurricularVO obj = (GestaoXmlGradeCurricularVO) context().getExternalContext().getRequestMap().get("gestaoItens");
			setGestaoXmlGradeCurricularSelecionado(obj);
			montarListaSelectItemCoordenadorCurso();
			montarDadosFuncionarioIesEmissora(obj);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarLayoutPadraoFuncionarioIes() {
		if (!Uteis.isAtributoPreenchido(getFuncionarioIesEmissora())) {
			try {
				LayoutPadraoVO layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo(GestaoXmlGradeCurricular.getIdEntidade(), "FuncionarioIesEmissora", Boolean.FALSE, getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(layoutPadraoVO)) {
					setFuncionarioIesEmissora(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(Integer.valueOf(layoutPadraoVO.getValor()), 0, Boolean.FALSE, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
					montarListaSelectItemCargoFuncionarioIesEmissora(LAYOUT_PADRAO);
					setTituloFuncionarioIesEmissora(layoutPadraoVO.getTituloAssinaturaFunc2());
				}
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
	}

	public void montarListaSelectItemCoordenadorCurso() throws Exception {
		if (Uteis.isAtributoPreenchido(getGestaoXmlGradeCurricularSelecionado().getCursoVO())) {
			List<FuncionarioVO> funcionarioVOs = getFacadeFactory().getFuncionarioFacade().consultaRapidaCoordenadorPorCurso(getGestaoXmlGradeCurricularSelecionado().getCursoVO().getCodigo(), getGestaoXmlGradeCurricularSelecionado().getUnidadeEnsinoVO().getCodigo(), Boolean.FALSE, getUsuarioLogado());
			getListaSelectItemFuncionario().clear();
			if (Uteis.isAtributoPreenchido(funcionarioVOs)) {
				funcionarioVOs.stream().map(f -> new SelectItem(f.getCodigo(), f.getPessoa().getNome())).forEach(getListaSelectItemFuncionario()::add);
				getGestaoXmlGradeCurricularSelecionado().setFuncionarioPrimario(funcionarioVOs.get(0));
				montarListaSelectItemCargoFuncionarioPrimario(INDIVIDUAL);
			}
		}
	}

	public void realizarGeracaoCurriculoEscolarDigital() {
		try {
			setOncompleteModal(Constantes.EMPTY);
			setListaMensagensErro(new ArrayList<>(0));
			getGestaoXmlGradeCurricularVO().setConsistirException(new ConsistirException());
			getGestaoXmlGradeCurricularVO().setListaGestaoXmlGradeCurricularErro(new ArrayList<>(0));
			getFacadeFactory().getGestaoXmlGradeCurricularInterfaceFacade().realizarMontagemCurriculoEscolarDigital(getGestaoXmlGradeCurricularSelecionado(), getSuperParametroRelVO(), getSuperControleRelatorio(), getUsuarioLogado(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade());
			if (Uteis.isAtributoPreenchido(getGestaoXmlGradeCurricularSelecionado().getFuncionarioSecundario())) {
				setFuncionarioIesEmissora((FuncionarioVO) getGestaoXmlGradeCurricularSelecionado().getFuncionarioSecundario().clone());
				setCargoFuncionarioIesEmissora((CargoVO) getGestaoXmlGradeCurricularSelecionado().getCargoSecundario().clone());
				setTituloFuncionarioIesEmissora(new String(getGestaoXmlGradeCurricularSelecionado().getTituloFuncionarioSecundario()));
			}
			setMensagemID("Curriculo escolar gerado com sucesso.", Uteis.SUCESSO, Boolean.TRUE);
			setOncompleteModal("RichFaces.$('panelAviso').hide();");
		} catch (ConsistirException e) {
			setOncompleteModal("RichFaces.$('panelListaErroGeracaoCurriculo').show();RichFaces.$('panelAviso').hide();");
			if (Uteis.isAtributoPreenchido(e.getListaMensagemErro())) {
				setListaMensagensErro(e.getListaMensagemErro());
			} else {
				getListaMensagensErro().add(e.getMessage());
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarFuncionario() {
		List<FuncionarioVO> objs = new ArrayList<>(0);
		try {
			if (getValorConsultaFuncionario().equals(Constantes.EMPTY)) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				FuncionarioVO funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), Boolean.FALSE, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				objs.add(funcionarioVO);
			}
			if (getCampoConsultaFuncionario().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCidade(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCargo(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeDepartamento(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeUnidadeEnsino(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			objs = null;
		}
	}

	public void limparDadosConsultaFuncionario(Boolean lote) {
		try {
			if (lote) {
				getGestaoXmlGradeCurricularVO().setFuncionarioPrimario(new FuncionarioVO());
				getGestaoXmlGradeCurricularVO().setTituloFuncionarioPrimario(Constantes.EMPTY);
				getGestaoXmlGradeCurricularVO().setCargoPrimario(new CargoVO());
				setValorConsultaFuncionario(Constantes.EMPTY);
				getListaSelectItemCargoFuncionarioPrimario().clear();
				setListaConsultaFuncionario(new ArrayList<>(0));
			} else {
				getGestaoXmlGradeCurricularSelecionado().setFuncionarioPrimario(new FuncionarioVO());
				getGestaoXmlGradeCurricularSelecionado().setTituloFuncionarioPrimario(Constantes.EMPTY);
				getGestaoXmlGradeCurricularSelecionado().setCargoPrimario(new CargoVO());
				setValorConsultaFuncionario(Constantes.EMPTY);
				getListaSelectItemCargoFuncionarioPrimario().clear();
				setListaConsultaFuncionario(new ArrayList<>(0));
				if (!getBuscarFuncionario() && Uteis.isAtributoPreenchido(getGestaoXmlGradeCurricularSelecionado().getCursoVO()) && Uteis.isAtributoPreenchido(getListaSelectItemFuncionario())) {
					getGestaoXmlGradeCurricularSelecionado().getFuncionarioPrimario().setCodigo((Integer) getListaSelectItemFuncionario().get(0).getValue());
					montarListaSelectItemCargoFuncionarioPrimario(INDIVIDUAL);
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparDadosConsultaFuncionarioIesEmissora() {
		setListaConsultaFuncionario(new ArrayList<>(0));
		setValorConsultaFuncionario(Constantes.EMPTY);
	}

	public void limparDadosFuncionarioIesEmissora(Boolean lote) {
		if (lote) {
			getGestaoXmlGradeCurricularVO().setFuncionarioSecundario(new FuncionarioVO());
			getGestaoXmlGradeCurricularVO().setCargoSecundario(new CargoVO());
			getGestaoXmlGradeCurricularVO().setTituloFuncionarioSecundario(Constantes.EMPTY);
			setListaSelectItemCargoFuncionarioIesEmissora(new ArrayList<>(0));
		} else {
			getGestaoXmlGradeCurricularSelecionado().setFuncionarioSecundario(new FuncionarioVO());
			getGestaoXmlGradeCurricularSelecionado().setCargoSecundario(new CargoVO());
			getGestaoXmlGradeCurricularSelecionado().setTituloFuncionarioSecundario(Constantes.EMPTY);
			setListaSelectItemCargoFuncionarioIesEmissora(new ArrayList<>(0));
		}
	}

	public void selecionarFuncionario(Boolean lote) {
		try {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
			if (lote) {
				getGestaoXmlGradeCurricularVO().setFuncionarioPrimario(obj);
				montarListaSelectItemCargoFuncionarioPrimario(LOTE);
			} else {
				getGestaoXmlGradeCurricularSelecionado().setFuncionarioPrimario(obj);
				montarListaSelectItemCargoFuncionarioPrimario(INDIVIDUAL);
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarFuncionarioIesEmissora(Boolean lote) {
		try {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("iesEmissoraFuncionarioItens");
			if (lote) {
				getGestaoXmlGradeCurricularVO().setFuncionarioSecundario(obj);
				montarListaSelectItemCargoFuncionarioIesEmissora(LOTE);
			} else {
				getGestaoXmlGradeCurricularSelecionado().setFuncionarioSecundario(obj);
				montarListaSelectItemCargoFuncionarioIesEmissora(INDIVIDUAL);
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarListaSelectItemCargoFuncionarioPrimario(String tipo) throws Exception {
		getListaSelectItemCargoFuncionarioPrimario().clear();
		if (tipo.equals(LOTE)) {
			if (Uteis.isAtributoPreenchido(getGestaoXmlGradeCurricularVO().getFuncionarioPrimario())) {
				getGestaoXmlGradeCurricularVO().setCargoPrimario(new CargoVO());
				getGestaoXmlGradeCurricularVO().setTituloFuncionarioPrimario(Constantes.EMPTY);
				List<CargoVO> cargoVOs = getFacadeFactory().getCargoFacade().consultarCargoPorFuncionario(getGestaoXmlGradeCurricularVO().getFuncionarioPrimario().getCodigo(), Boolean.FALSE, getUsuarioLogado());
				cargoVOs.stream().map(c -> new SelectItem(c.getCodigo(), c.getNome())).forEach(getListaSelectItemCargoFuncionarioPrimario()::add);
			}
		} else {
			if (Uteis.isAtributoPreenchido(getGestaoXmlGradeCurricularSelecionado().getFuncionarioPrimario())) {
				getGestaoXmlGradeCurricularSelecionado().setCargoPrimario(new CargoVO());
				getGestaoXmlGradeCurricularSelecionado().setTituloFuncionarioPrimario(Constantes.EMPTY);
				List<CargoVO> cargoVOs = getFacadeFactory().getCargoFacade().consultarCargoPorFuncionario(getGestaoXmlGradeCurricularSelecionado().getFuncionarioPrimario().getCodigo(), Boolean.FALSE, getUsuarioLogado());
				cargoVOs.stream().map(c -> new SelectItem(c.getCodigo(), c.getNome())).forEach(getListaSelectItemCargoFuncionarioPrimario()::add);
			}
		}
	}

	public void montarListaSelectItemCargoFuncionarioIesEmissora(String tipo) throws Exception {
		getListaSelectItemCargoFuncionarioIesEmissora().clear();
		if (tipo.equals(LAYOUT_PADRAO))  {
			if (Uteis.isAtributoPreenchido(getFuncionarioIesEmissora())) {
				setCargoFuncionarioIesEmissora(new CargoVO());
				setTituloFuncionarioIesEmissora(Constantes.EMPTY);
				List<CargoVO> cargoVOs = getFacadeFactory().getCargoFacade().consultarCargoPorFuncionario(getFuncionarioIesEmissora().getCodigo(), Boolean.FALSE, getUsuarioLogado());
				cargoVOs.stream().map(c -> new SelectItem(c.getCodigo(), c.getNome())).forEach(getListaSelectItemCargoFuncionarioIesEmissora()::add);
			}
		} else if (tipo.equals(LOTE))  {
			if (Uteis.isAtributoPreenchido(getGestaoXmlGradeCurricularVO().getFuncionarioSecundario())) {
				getGestaoXmlGradeCurricularVO().setCargoSecundario(new CargoVO());
				getGestaoXmlGradeCurricularVO().setTituloFuncionarioSecundario(Constantes.EMPTY);
				List<CargoVO> cargoVOs = getFacadeFactory().getCargoFacade().consultarCargoPorFuncionario(getGestaoXmlGradeCurricularVO().getFuncionarioSecundario().getCodigo(), Boolean.FALSE, getUsuarioLogado());
				cargoVOs.stream().map(c -> new SelectItem(c.getCodigo(), c.getNome())).forEach(getListaSelectItemCargoFuncionarioIesEmissora()::add);
			}
		} else {
			if (Uteis.isAtributoPreenchido(getGestaoXmlGradeCurricularSelecionado().getFuncionarioSecundario())) {
				getGestaoXmlGradeCurricularSelecionado().setCargoSecundario(new CargoVO());
				getGestaoXmlGradeCurricularSelecionado().setTituloFuncionarioSecundario(Constantes.EMPTY);
				List<CargoVO> cargoVOs = getFacadeFactory().getCargoFacade().consultarCargoPorFuncionario(getGestaoXmlGradeCurricularSelecionado().getFuncionarioSecundario().getCodigo(), Boolean.FALSE, getUsuarioLogado());
				cargoVOs.stream().map(c -> new SelectItem(c.getCodigo(), c.getNome())).forEach(getListaSelectItemCargoFuncionarioIesEmissora()::add);
			}
		}
	}

	public void consultarFuncionarioIesEmissoraPorMatricula(Boolean lote) {
		try {
			if (lote) {
				getGestaoXmlGradeCurricularVO().setCargoSecundario(new CargoVO());
				getGestaoXmlGradeCurricularVO().setTituloFuncionarioSecundario(Constantes.EMPTY);
				FuncionarioVO objFuncionario = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getGestaoXmlGradeCurricularVO().getFuncionarioSecundario().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				if (!Uteis.isAtributoPreenchido(objFuncionario.getMatricula())) {
					getGestaoXmlGradeCurricularVO().setFuncionarioSecundario(new FuncionarioVO());
					throw new Exception("Funcionário não encontrado.");
				}
				getGestaoXmlGradeCurricularVO().setFuncionarioSecundario(objFuncionario);
				montarListaSelectItemCargoFuncionarioIesEmissora(LOTE);
			} else {
				getGestaoXmlGradeCurricularSelecionado().setCargoSecundario(new CargoVO());
				getGestaoXmlGradeCurricularSelecionado().setTituloFuncionarioSecundario(Constantes.EMPTY);
				FuncionarioVO objFuncionario = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getGestaoXmlGradeCurricularSelecionado().getFuncionarioSecundario().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				if (!Uteis.isAtributoPreenchido(objFuncionario.getMatricula())) {
					throw new Exception("Funcionário não encontrado.");
				}
				getGestaoXmlGradeCurricularSelecionado().setFuncionarioSecundario(objFuncionario);
				montarListaSelectItemCargoFuncionarioIesEmissora(INDIVIDUAL);
			}
		} catch (Exception e) {
			limparDadosFuncionarioIesEmissora(lote);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void fecharPanelPreview() {
		setOncompleteModal(Constantes.EMPTY);
		File file = new File(getCaminhoPreview());
		if (file.exists()) {
			file.delete();
		}
		setCaminhoPreview(Constantes.EMPTY);
	}

	public void consultarLayoutListaDocumentoAssinadoPadrao() {
		try {
			LayoutPadraoVO layout = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("GestaoXmlGradeCurricular", "listaDocumentoAssinado", Boolean.FALSE, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(layout)) {
				setVersaoLayoutListaNovo(Boolean.valueOf(layout.getValor()));
			} else {
				setVersaoLayoutListaNovo(Boolean.TRUE);
			}
		} catch (Exception e) {
			setVersaoLayoutListaNovo(Boolean.TRUE);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void persistirLayoutListaPadrao() {
		try {
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getVersaoLayoutListaNovo().toString(), "GestaoXmlGradeCurricular", "listaDocumentoAssinado", getUsuarioLogado());
			setMensagemID("Layout padrão alterado com sucesso.", Uteis.SUCESSO, Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarDownloadDocumentoAssinado() {
		setOncompleteModal(Constantes.EMPTY);
		setCaminhoPreview(Constantes.EMPTY);
		if (Uteis.isAtributoPreenchido(getDocumentoAssinadoVO())) {
			try {
				realizarDownloadArquivo(getDocumentoAssinadoVO().getArquivo());
				setOncompleteModal(getUrlDownloadSV());
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
	}

	public void realizarDownloadRepresentacaoVisual() {
		setOncompleteModal(Constantes.EMPTY);
		if (Uteis.isAtributoPreenchido(getDocumentoAssinadoVO())) {
			try {
				setCaminhoPreview(getFacadeFactory().getDocumentoAssinadoFacade().realizarGeracaoPreviewRepresentacaoVisual(getDocumentoAssinadoVO(), getConfiguracaoGeralPadraoSistema()));
				setOncompleteModal("RichFaces.$('panelPreviewPdf').show()");
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
	}

	public void realizarMontagemCurriculoEscolarDigital() {
		GestaoXmlGradeCurricularVO obj = (GestaoXmlGradeCurricularVO) context().getExternalContext().getRequestMap().get("gestaoItens");
		try {
			if (Uteis.isAtributoPreenchido(obj.getDocumentoAssinadoVO())) {
				setDocumentoAssinadoVO(getFacadeFactory().getDocumentoAssinadoFacade().consultarPorChavePrimaria(obj.getDocumentoAssinadoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void marcarTodos() {
		List<GestaoXmlGradeCurricularVO> listaConsulta = (List<GestaoXmlGradeCurricularVO>) getGestaoXmlGradeCurricularVO().getControleConsultaOtimizado().getListaConsulta();
		if (Uteis.isAtributoPreenchido(listaConsulta)) {
			listaConsulta.stream().forEach(g -> g.setSelecionado(getSelecionarTodos()));
		}
	}
	
	public void limparDadosGeracaoCurriculoEscolarLote() {
		try {
			getGestaoXmlGradeCurricularVO().setCoordenadorCursoAssinateEcpfLote(Boolean.TRUE);
			limparDadosConsultaFuncionario(Boolean.TRUE);
			montarDadosFuncionarioIesEmissora(getGestaoXmlGradeCurricularVO());
			verificarApresentarMotivoRejeicao();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void verificarApresentarMotivoRejeicao() {
		try {
			if (Uteis.isAtributoPreenchido(getGestaoXmlGradeCurricularVO().getControleConsultaOtimizado().getListaConsulta()) && ((List<GestaoXmlGradeCurricularVO>)getGestaoXmlGradeCurricularVO().getControleConsultaOtimizado().getListaConsulta()).stream().anyMatch(GestaoXmlGradeCurricularVO::getSelecionado)) {
				setApresentarMotivoRejeicaoLote(((List<GestaoXmlGradeCurricularVO>)getGestaoXmlGradeCurricularVO().getControleConsultaOtimizado().getListaConsulta()).stream().filter(GestaoXmlGradeCurricularVO::getSelecionado).anyMatch(g -> Uteis.isAtributoPreenchido(g.getDocumentoAssinadoVO())));
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void executarInicioProgressBarGeracaoCurriculoEscolarDigitalLote() {
		try {
			setOncompleteModal("RichFaces.$('panelAvisoLote').hide();");
			if (getApresentarMotivoRejeicaoLote() && !Uteis.isAtributoPreenchido(getMotivoRejeicao())) {
				throw new Exception("O motivo da rejeição deve ser informado.");
			}
			getGestaoXmlGradeCurricularVO().setMotivoRejeicao(getApresentarMotivoRejeicaoLote() ? getMotivoRejeicao() : Constantes.EMPTY);
			List<GestaoXmlGradeCurricularVO> gestaoXmlGradeCurricularVOs = (List<GestaoXmlGradeCurricularVO>) getGestaoXmlGradeCurricularVO().getControleConsultaOtimizado().getListaConsulta();
			if (gestaoXmlGradeCurricularVOs.stream().anyMatch(GestaoXmlGradeCurricularVO::getSelecionado)) {
				getGestaoXmlGradeCurricularVO().getListaGestaoXmlGradeCurricularErro().clear();
				setProgressBarVO(new ProgressBarVO());
				getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
				getProgressBarVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
				getProgressBarVO().setCaminhoWebRelatorio(getCaminhoPastaWeb());
				getProgressBarVO().resetar();
				getProgressBarVO().iniciar(0l, (gestaoXmlGradeCurricularVOs.stream().filter(GestaoXmlGradeCurricularVO::getSelecionado).collect(Collectors.toList()).size()), "Carregando Matrizes Curriculares", true, this, "realizarGeracaoCurriculoEscolarDigitalLote");
			} else {
				throw new ConsistirException("Deve ser selecionado ao menos uma matriz para a geração do currículo escolar digital.");
			}
		} catch (Exception e) {
			setOncompleteModal(Constantes.EMPTY);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarGeracaoCurriculoEscolarDigitalLote() {
		try {
			getFacadeFactory().getGestaoXmlGradeCurricularInterfaceFacade().realizarMontagemCurriculoEscolarDigitalLote(getGestaoXmlGradeCurricularVO(), getProgressBarVO(), getSuperParametroRelVO(), getSuperControleRelatorio());
			if (Uteis.isAtributoPreenchido(getGestaoXmlGradeCurricularVO().getFuncionarioSecundario())) {
				setFuncionarioIesEmissora((FuncionarioVO) getGestaoXmlGradeCurricularVO().getFuncionarioSecundario().clone());
				setCargoFuncionarioIesEmissora((CargoVO) getGestaoXmlGradeCurricularVO().getCargoSecundario().clone());
				setTituloFuncionarioIesEmissora(new String(getGestaoXmlGradeCurricularVO().getTituloFuncionarioSecundario()));
			}
			if (getGestaoXmlGradeCurricularVO().getListaGestaoXmlGradeCurricularErro().isEmpty()) {
				setMensagemID("Curriculo escolar gerado com sucesso.", Uteis.SUCESSO, Boolean.TRUE);
			} else {
				setOncompleteModal("RichFaces.$('panelAvisoLote').hide();RichFaces.$('panelListaErroGeracaoCurriculoLote').show();");
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			getProgressBarVO().setForcarEncerramento(Boolean.TRUE);
		}
	}
}
