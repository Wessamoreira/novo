package controle.secretaria;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.EnadeCursoVO;
import negocio.comuns.academico.EnadeVO;
import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.secretaria.MapaConvocacaoEnadeMatriculaVO;
import negocio.comuns.secretaria.MapaConvocacaoEnadeVO;
import negocio.comuns.secretaria.enumeradores.SituacaoMapaConvocacaoEnadeEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.academico.MapaConvocacaoEnadeRelControle;
import relatorio.controle.arquitetura.SuperControleRelatorio;

@Controller("MapaConvocacaoEnadeControle")
@Scope("viewScope")
public class MapaConvocacaoEnadeControle extends SuperControleRelatorio {

	private MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO;
	private MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemAno;
	private List<SelectItem> listaSelectItemSemestre;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<UnidadeEnsinoCursoVO> listaConsultaCurso;
	private String campoConsultaEnade;
	private String campoConsultaRelEnade;
	private String valorConsultaEnade;
	private List<EnadeCursoVO> listaConsultaEnadeCurso;
	private Date valorConsultaData;
	private Date dataInicial;
	private Date dataFinal;
	private ProgressBarVO progressBar;
	private String campoOrdernarRel;
	private MapaConvocacaoEnadeMatriculaVO matriConvocacaoEnadeMatriculaVO;
	private String metodoSerExecutado;
	private Boolean apresentarBotaoFinalizarEtapaTransferenciaAluno;
	private Boolean apresentarPainelObservacao;
	private Boolean situacaoIngressante;
	private Boolean situacaoConcluinte;
	private Boolean situacaoDispensado;
	private EnadeVO enadeVO;
	public List<MapaConvocacaoEnadeVO> listaMapaConvocacaoEnadeVOErros;
	private List<EnadeVO> listaConsultaEnadeVO;
	private Boolean apresentarEnade;
	private Boolean apresentarListaErros;
	private List<MapaConvocacaoEnadeVO> listaMapaConvocacaoEnadeVOs;
	private Boolean apresentarResultadoConsultaEnade;
	private List<MapaConvocacaoEnadeVO> listaMapaConvocacaoEnadeVOsRel;
	
	private String onCompleteErroGeracaoArquivo;
	private String onCompleteRegerarArquivo;
	private List<EnadeVO> listaConsultaEnade;
	
	public List<MapaConvocacaoEnadeVO> listaSituacaoEnade;
	private String onCompleteDownloadMapaconvocacaoEnade;
	public List<MapaConvocacaoEnadeVO> listaExportarMapaConvocacaoEnadeVOs;
	private ArquivoVO arquivoVO;
	List<MatriculaVO> matriculaVOs;	
	private ProgressBarVO progressBarTxt;
	
	private static final long serialVersionUID = 1L;

	public MapaConvocacaoEnadeControle() {
		super();
		realizarConsultaPreferenciasUsuario();
	}

	public String novo() {
		removerObjetoMemoria(this);
		setMapaConvocacaoEnadeVO(null);
		getMapaConvocacaoEnadeVO().setResponsavel(getUsuarioLogadoClone());
		getMapaConvocacaoEnadeVO().setNovoObj(Boolean.TRUE);
		consultarUnidadeEnsino();
		limparMensagem();
		return Uteis.getCaminhoRedirecionamentoNavegacao("mapaConvocacaoEnadeForm.xhtml");
	}

	public String excluir() {
		try {
			getFacadeFactory().getMapaConvocacaoEnadeFacade().excluir(getMapaConvocacaoEnadeVO(), getMapaConvocacaoEnadeMatriculaVO(), getUsuarioLogado());
			getMapaConvocacaoEnadeVO().setIngressantes(Boolean.TRUE);
			getMapaConvocacaoEnadeVO().setConcluintes(Boolean.TRUE);
			novo();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("mapaConvocacaoEnadeForm.xhtml");
	}

	public String editar() {
		try {
			setMapaConvocacaoEnadeVO((MapaConvocacaoEnadeVO) getRequestMap().get("mapaConvocacaoItens"));
			getFacadeFactory().getMapaConvocacaoEnadeFacade().carregarDados(getMapaConvocacaoEnadeVO(), NivelMontarDados.TODOS, getUsuarioLogado(), false);
			montarUnidadesEnsino();
			getMapaConvocacaoEnadeVO().getEnadeCursoVO().setEnadeVO(getFacadeFactory().getEnadeFacade().consultarPorChavePrimaria(getMapaConvocacaoEnadeVO().getEnadeCursoVO().getEnadeVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setListaExportarMapaConvocacaoEnadeVOs(getFacadeFactory().getMapaConvocacaoEnadeFacade().consultaRapidaPorEnadeCurso(getMapaConvocacaoEnadeVO().getEnadeCursoVO().getEnadeVO().getCodigo(), getMapaConvocacaoEnadeVO().getEnadeCursoVO().getCodigo(), getMapaConvocacaoEnadeVO().getCodigo(), false, false, getUsuarioLogado()));
			getFacadeFactory().getMapaConvocacaoEnadeFacade().inicializarDadosTotalizadorLista(getMapaConvocacaoEnadeVO());
			getMapaConvocacaoEnadeVO().setNovoObj(Boolean.FALSE);
			setApresentarPainelObservacao(false);
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("mapaConvocacaoEnadeForm.xhtml");
	}

	public void persitir() {
		try {
			getMapaConvocacaoEnadeVO().setUnidadeEnsino(getUnidadeEnsinoVOs().stream().filter(ue -> ue.getFiltrarUnidadeEnsino()).map(UnidadeEnsinoVO::getCodigo).collect(Collectors.toList()).toString());
			getFacadeFactory().getMapaConvocacaoEnadeFacade().persistir(getMapaConvocacaoEnadeVO(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarAlunosMapaConvocacaoEnade() {
		try {
			getFacadeFactory().getMapaConvocacaoEnadeFacade().consultarAlunosMapaConvocacaoEnade(getMapaConvocacaoEnadeVO(), getMapaConvocacaoEnadeVO().getEnadeCursoVO().getCursoVO().getConfiguracaoAcademico(), getProgressBar(), getUsuarioLogado(), getUnidadeEnsinoVOs());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			if (e instanceof ConsistirException) {
				setConsistirExceptionMensagemDetalhada("msg_erro", (ConsistirException) e, Uteis.ERRO);
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("curso", "Curso"));
//		itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
		itens.add(new SelectItem("enade", "Enade"));
		itens.add(new SelectItem("dataAbertura", "Data Abertura"));
		itens.add(new SelectItem("dataFechamento", "Data Fechamento"));
		return itens;
	}

	public void finalizarMapa() {
		try {
			getMapaConvocacaoEnadeVO().setSituacaoMapaConvocacaoEnade(SituacaoMapaConvocacaoEnadeEnum.MAPA_FINALIZADO);
			getFacadeFactory().getMapaConvocacaoEnadeFacade().alterarSituacaoMApaConvocacaoPorCodigo(getMapaConvocacaoEnadeVO().getCodigo(), SituacaoMapaConvocacaoEnadeEnum.MAPA_FINALIZADO, getUsuarioLogado());
			setMensagemID("msg_dados_finalizado", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void enviarComunicadoInternoAlunosConvocados() {
		try {
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemConvocacaoEnade(getMapaConvocacaoEnadeVO().getMapaConvocacaoEnadeMatriculaAlunosIngressantesVOs(), getMapaConvocacaoEnadeVO().getMapaConvocacaoEnadeMatriculaAlunosConcluintesVOs(), getUnidadeEnsinoVOs(), getMapaConvocacaoEnadeVO(), getUsuarioLogado());
			setMensagemID("msg_msg_enviados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getIsApresentarBotaoGravar() {
		return getMapaConvocacaoEnadeVO().getSituacaoMapaConvocacaoEnade().name().equals(SituacaoMapaConvocacaoEnadeEnum.MAPA_EM_CONSTRUCAO.name());
	}

	public Boolean getIsApresentarBotaoFinalizar() {
		return getMapaConvocacaoEnadeVO().getSituacaoMapaConvocacaoEnade().name().equals(SituacaoMapaConvocacaoEnadeEnum.MAPA_EM_CONSTRUCAO.name()) && !getMapaConvocacaoEnadeVO().getCodigo().equals(0);
	}

	public Boolean getIsApresentarData() {
		return getControleConsulta().getCampoConsulta().equals("dataAbertura") || getControleConsulta().getCampoConsulta().equals("dataFechamento");
	}

	public void imprimirMensagemControladorMapaConvocacaoEnadeRelComDados() {
		setMensagemID("msg_relatorio_ok");
	}

	public void imprimirMensagemControladorMapaConvocacaoEnadeRelSemDados() {
		setMensagemID("msg_relatorio_sem_dados");
	}

	public void imprimirMensagemControladorMapaConvocacaoEnadeRelErro(String erro) {
		setMensagemDetalhada(erro);
	}

	public void imprimirPDF(Boolean impressao) {
		getMapaConvocacaoEnadeVO().setImprimirPDF(impressao);
		realizarPersistenciaPreferenciasUsuario();
		getMapaConvocacaoEnadeVO().setUnidadeEnsino(getUnidadeEnsinosApresentar());
//		for (SelectItem item : listaSelectItemUnidadeEnsino) {
//			if (item.getValue().equals(getMapaConvocacaoEnadeVO().getUnidadeEnsinoVO().getCodigo())) {
//				getMapaConvocacaoEnadeVO().getUnidadeEnsinoVO().setNome(item.getLabel());
//				break;
//			}
//		}
//		consultarAlunosMapaConvocacaoEnade();
		executarMetodoControle(MapaConvocacaoEnadeRelControle.class.getSimpleName(), "inicializaMapaConvocacaoEnadeVO", getMapaConvocacaoEnadeVO(), getCampoConsultaRelEnade(), getSituacaoIngressante(), getSituacaoConcluinte(), getSituacaoDispensado(), getCampoOrdernarRel());
	}

	public String consultar() {
		try {
			setListaConsulta(getFacadeFactory().getMapaConvocacaoEnadeFacade().consultar(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), getDataInicial(), getDataFinal(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado(), NivelMontarDados.BASICO));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("mapaConvocacaoEnadeCons.xhtml");
	}

	private List<SelectItem> opcaoConsultaCurso;

	public List<SelectItem> getOpcaoConsultaCurso() {
		if (opcaoConsultaCurso == null) {
			opcaoConsultaCurso = new ArrayList<SelectItem>(0);
			opcaoConsultaCurso.add(new SelectItem("curso", "Curso"));
		}
		return opcaoConsultaCurso;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
			try {
				List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				listaSelectItemUnidadeEnsino.add(new SelectItem(0, ""));
				for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
					listaSelectItemUnidadeEnsino.add(new SelectItem(unidadeEnsinoVO.getCodigo(), unidadeEnsinoVO.getNome()));
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}

		}
		return listaSelectItemUnidadeEnsino;
	}

	public void consultarEnadeCurso() {
		try {
			super.consultar();
			List<EnadeCursoVO> objs = new ArrayList<>(0);
			if (getCampoConsultaEnade().equals("codigo")) {
				if (getValorConsultaEnade().equals("")) {
					setValorConsultaEnade("0");
				}
				if (getValorConsultaEnade().trim() != null || !getValorConsultaEnade().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getValorConsultaEnade().trim());
				}
				int valorInt = Integer.parseInt(getValorConsultaEnade());
				objs = getFacadeFactory().getEnadeCursoFacade().consultarPorCodigoEnade(new Integer(valorInt), getUsuarioLogado());
			}
			if (getCampoConsultaEnade().equals("tituloEnade")) {
				objs = getFacadeFactory().getEnadeCursoFacade().consultarPorTituloEnade(getValorConsultaEnade(), getUsuarioLogado());
			}
			setListaConsultaEnadeCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaEnadeCurso(new ArrayList<EnadeCursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void consultarEnade() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaEnade().equals("codigo")) {
				if (getValorConsultaEnade().equals("")) {
					setValorConsultaEnade("0");
				}
				if (getValorConsultaEnade().trim() != null || !getValorConsultaEnade().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getValorConsultaEnade().trim());
				}
				int valorInt = Integer.parseInt(getValorConsultaEnade());
				objs = getFacadeFactory().getEnadeCursoFacade().consultarPorCodigoEnade(new Integer(valorInt), getUsuarioLogado());
			}
			if (getCampoConsultaEnade().equals("tituloEnade")) {
				objs = getFacadeFactory().getEnadeCursoFacade().consultarPorTituloEnade(getValorConsultaEnade(), getUsuarioLogado());
			}
			setListaConsultaEnade(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaEnade(new ArrayList<EnadeVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}
	
	public String selecionarEnade() {
		EnadeCursoVO obj = (EnadeCursoVO) context().getExternalContext().getRequestMap().get("enadeCursoItens");
		getMapaConvocacaoEnadeVO().setEnadeCursoVO(obj);
		obj.setNovoObj(Boolean.FALSE);
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("mapaConvocacaoEnadeForm.xhtml");
	}

	public void limparEnade() {
		getMapaConvocacaoEnadeVO().setEnadeCursoVO(null);
		getMapaConvocacaoEnadeVO().getMapaConvocacaoEnadeMatriculaAlunosIngressantesVOs().clear();
		getMapaConvocacaoEnadeVO().getMapaConvocacaoEnadeMatriculaAlunosDispensadosVOs().clear();
		getMapaConvocacaoEnadeVO().getMapaConvocacaoEnadeMatriculaAlunosConcluintesVOs().clear();
		getListaConsultaEnadeCurso().clear();
		getListaConsultaEnade().clear();
	}

	public void realizarMudancaoMapa() {
		try {
			if (getMetodoSerExecutado().equals("INGRESSANTE_DISPENSADO")) {
				realizarMudancaAlunoIngressanteParaDispensados();
			} else if (getMetodoSerExecutado().equals("DISPENSADO_INGRESSANTE")) {
				realizarMudancaAlunoDispensadoParaIngressante();
			} else if (getMetodoSerExecutado().equals("DISPENSADO_CONCLUINTE")) {
				realizarMudancaAlunoDispensadoParaConcluinte();
			} else if (getMetodoSerExecutado().equals("CONCLUINTE_DISPENSADO")) {
				realizarMudancaAlunoConcluinteParadispensados();
			}
			setApresentarPainelObservacao(false);
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private void realizarMudancaAlunoIngressanteParaDispensados() throws Exception {
		getFacadeFactory().getMapaConvocacaoEnadeFacade().realizarMudancaAlunoIngressanteParaDispensados(getMapaConvocacaoEnadeVO(), getMapaConvocacaoEnadeMatriculaVO(), getUsuarioLogado());
	}

	private void realizarMudancaAlunoDispensadoParaIngressante() throws Exception {
		getFacadeFactory().getMapaConvocacaoEnadeFacade().realizarMudancaAlunoDispensadoParaIngressante(getMapaConvocacaoEnadeVO(), getMapaConvocacaoEnadeMatriculaVO(), getUsuarioLogado());
	}

	private void realizarMudancaAlunoDispensadoParaConcluinte() throws Exception {
		getFacadeFactory().getMapaConvocacaoEnadeFacade().realizarMudancaAlunoDispensadoParaConcluinte(getMapaConvocacaoEnadeVO(), getMapaConvocacaoEnadeMatriculaVO(), getUsuarioLogado());
	}

	private void realizarMudancaAlunoConcluinteParadispensados() throws Exception {
		getFacadeFactory().getMapaConvocacaoEnadeFacade().realizarMudancaAlunoConcluinteParadispensados(getMapaConvocacaoEnadeVO(), getMapaConvocacaoEnadeMatriculaVO(), getUsuarioLogado());
	}

	public void iniciliazarDadosTransferenciaAlunoMapa() {
		if (getMetodoSerExecutado().equals("INGRESSANTE_DISPENSADO")) {
			setMapaConvocacaoEnadeMatriculaVO((MapaConvocacaoEnadeMatriculaVO) context().getExternalContext().getRequestMap().get("alunosIngressantesItens"));
		} else if (getMetodoSerExecutado().equals("DISPENSADO_INGRESSANTE") || getMetodoSerExecutado().equals("DISPENSADO_CONCLUINTE")) {
			setMapaConvocacaoEnadeMatriculaVO((MapaConvocacaoEnadeMatriculaVO) context().getExternalContext().getRequestMap().get("alunosDispensadosItens"));
		} else if (getMetodoSerExecutado().equals("CONCLUINTE_DISPENSADO")) {
			setMapaConvocacaoEnadeMatriculaVO((MapaConvocacaoEnadeMatriculaVO) context().getExternalContext().getRequestMap().get("alunosConcluintesItens"));
		}
		setApresentarBotaoFinalizarEtapaTransferenciaAluno(true);
		setApresentarPainelObservacao(true);
		setMensagemID("msg_dados_editar");
	}

	public void inicializarDadosObservacao() {
		if (getMetodoSerExecutado().equals("INGRESSANTE_DISPENSADO")) {
			setMapaConvocacaoEnadeMatriculaVO((MapaConvocacaoEnadeMatriculaVO) context().getExternalContext().getRequestMap().get("alunosIngressantesItens"));
		} else if (getMetodoSerExecutado().equals("DISPENSADO_INGRESSANTE") || getMetodoSerExecutado().equals("DISPENSADO_CONCLUINTE")) {
			setMapaConvocacaoEnadeMatriculaVO((MapaConvocacaoEnadeMatriculaVO) context().getExternalContext().getRequestMap().get("alunosDispensadosItens"));
		} else if (getMetodoSerExecutado().equals("CONCLUINTE_DISPENSADO")) {
			setMapaConvocacaoEnadeMatriculaVO((MapaConvocacaoEnadeMatriculaVO) context().getExternalContext().getRequestMap().get("alunosConcluintesItens"));
		}
		setApresentarBotaoFinalizarEtapaTransferenciaAluno(false);
		setApresentarPainelObservacao(true);
		setMensagemID("msg_dados_editar");
	}

	public List<SelectItem> getTipoConsultaComboEnade() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("tituloEnade", "Título Enade"));
		itens.add(new SelectItem("codigo", "Número"));
		// itens.add(new SelectItem("dataPublicacaoPortariaDOU",
		// "Data Publicação no Diário Oficial"));
		return itens;
	}

	public List<SelectItem> getOrdenacaoRelatorioEnade() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboRelEnade() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("sem-assinatura", "Relatório sem Assinatura"));
		itens.add(new SelectItem("com-assinatura", "Relatório com Assinatura"));
		itens.add(new SelectItem("listagemDadosCompletosAluno", "Listagem Dados Completos Aluno"));
		return itens;
	}

	public String inicializarConsulta() {
		setMapaConvocacaoEnadeVO(null);
		setControleConsultaOtimizado(null);
		return consultar();
	}

	public List<SelectItem> getListaSelectItemAno() {
		if (listaSelectItemAno == null) {
			listaSelectItemAno = new ArrayList<SelectItem>(0);
			listaSelectItemAno.add(new SelectItem("", ""));
			for (int x = 2012; x <= Uteis.getAnoData(new Date()) + 2; x++) {
				listaSelectItemAno.add(new SelectItem(String.valueOf(x), String.valueOf(x)));
			}
		}
		return listaSelectItemAno;
	}

	public void setListaSelectItemAno(List<SelectItem> listaSelectItemAno) {
		this.listaSelectItemAno = listaSelectItemAno;
	}

	public List<SelectItem> getListaSelectItemSemestre() {
		if (listaSelectItemSemestre == null) {
			listaSelectItemSemestre = new ArrayList<SelectItem>(0);
			listaSelectItemSemestre.add(new SelectItem("", ""));
			listaSelectItemSemestre.add(new SelectItem("1", "1ª"));
			listaSelectItemSemestre.add(new SelectItem("2", "2ª"));
		}
		return listaSelectItemSemestre;
	}

	public void setListaSelectItemSemestre(List<SelectItem> listaSelectItemSemestre) {
		this.listaSelectItemSemestre = listaSelectItemSemestre;
	}

	public MapaConvocacaoEnadeVO getMapaConvocacaoEnadeVO() {
		if (mapaConvocacaoEnadeVO == null) {
			mapaConvocacaoEnadeVO = new MapaConvocacaoEnadeVO();
		}
		return mapaConvocacaoEnadeVO;
	}

	public void setMapaConvocacaoEnadeVO(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO) {
		this.mapaConvocacaoEnadeVO = mapaConvocacaoEnadeVO;
	}

	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public String getValorConsultaCurso() {
		if (valorConsultaCurso == null) {
			valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public List<UnidadeEnsinoCursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<UnidadeEnsinoCursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<UnidadeEnsinoCursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public String getCampoConsultaEnade() {
		if (campoConsultaEnade == null) {
			campoConsultaEnade = "";
		}
		return campoConsultaEnade;
	}

	public void setCampoConsultaEnade(String campoConsultaEnade) {
		this.campoConsultaEnade = campoConsultaEnade;
	}

	public String getCampoConsultaRelEnade() {
		if (campoConsultaRelEnade == null) {
			campoConsultaRelEnade = "";
		}
		return campoConsultaRelEnade;
	}

	public void setCampoConsultaRelEnade(String campoConsultaRelEnade) {
		this.campoConsultaRelEnade = campoConsultaRelEnade;
	}

	public String getValorConsultaEnade() {
		if (valorConsultaEnade == null) {
			valorConsultaEnade = "";
		}
		return valorConsultaEnade;
	}

	public void setValorConsultaEnade(String valorConsultaEnade) {
		this.valorConsultaEnade = valorConsultaEnade;
	}

	public List<EnadeVO> getListaConsultaEnadeVO() {
		if (listaConsultaEnadeVO == null) {
			listaConsultaEnadeVO = new ArrayList<EnadeVO>(0);
		}
		return listaConsultaEnadeVO;
	}

	public void setListaConsultaEnadeVO(List<EnadeVO> listaConsultaEnadeVO) {
		this.listaConsultaEnadeVO = listaConsultaEnadeVO;
	}

	public Date getValorConsultaData() {
		if (valorConsultaData == null) {
			valorConsultaData = new Date();
		}
		return valorConsultaData;
	}

	public void setValorConsultaData(Date valorConsultaData) {
		this.valorConsultaData = valorConsultaData;
	}

	public Date getDataInicial() {
		if (dataInicial == null) {
			dataInicial = new Date();
		}
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		if (dataFinal == null) {
			dataFinal = new Date();
		}
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public ProgressBarVO getProgressBar() {
		if (progressBar == null) {
			progressBar = new ProgressBarVO();
		}
		return progressBar;
	}

	public void setProgressBar(ProgressBarVO progressBar) {
		this.progressBar = progressBar;
	}

	public String getCampoOrdernarRel() {
		if (campoOrdernarRel == null) {
			campoOrdernarRel = "";
		}
		return campoOrdernarRel;
	}

	public void setCampoOrdernarRel(String campoOrdernarRel) {
		this.campoOrdernarRel = campoOrdernarRel;
	}

	public MapaConvocacaoEnadeMatriculaVO getMatriConvocacaoEnadeMatriculaVO() {
		if (matriConvocacaoEnadeMatriculaVO == null) {
			matriConvocacaoEnadeMatriculaVO = new MapaConvocacaoEnadeMatriculaVO();
		}
		return matriConvocacaoEnadeMatriculaVO;
	}

	public void setMatriConvocacaoEnadeMatriculaVO(MapaConvocacaoEnadeMatriculaVO matriConvocacaoEnadeMatriculaVO) {
		this.matriConvocacaoEnadeMatriculaVO = matriConvocacaoEnadeMatriculaVO;
	}

	public String getMetodoSerExecutado() {
		if (metodoSerExecutado == null) {
			metodoSerExecutado = "";
		}
		return metodoSerExecutado;
	}

	public void setMetodoSerExecutado(String metodoSerExecutado) {
		this.metodoSerExecutado = metodoSerExecutado;
	}

	public Boolean getApresentarBotaoFinalizarEtapaTransferenciaAluno() {
		if (apresentarBotaoFinalizarEtapaTransferenciaAluno == null) {
			apresentarBotaoFinalizarEtapaTransferenciaAluno = true;
		}
		return apresentarBotaoFinalizarEtapaTransferenciaAluno;
	}

	public void setApresentarBotaoFinalizarEtapaTransferenciaAluno(Boolean apresentarBotaoFinalizarEtapaTransferenciaAluno) {
		this.apresentarBotaoFinalizarEtapaTransferenciaAluno = apresentarBotaoFinalizarEtapaTransferenciaAluno;
	}

	public MapaConvocacaoEnadeMatriculaVO getMapaConvocacaoEnadeMatriculaVO() {
		if (mapaConvocacaoEnadeMatriculaVO == null) {
			mapaConvocacaoEnadeMatriculaVO = new MapaConvocacaoEnadeMatriculaVO();
		}
		return mapaConvocacaoEnadeMatriculaVO;
	}

	public void setMapaConvocacaoEnadeMatriculaVO(MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO) {
		this.mapaConvocacaoEnadeMatriculaVO = mapaConvocacaoEnadeMatriculaVO;
	}

	public Boolean getApresentarPainelObservacao() {
		if (apresentarPainelObservacao == null) {
			apresentarPainelObservacao = false;
		}
		return apresentarPainelObservacao;
	}

	public void setApresentarPainelObservacao(Boolean apresentarPainelObservacao) {
		this.apresentarPainelObservacao = apresentarPainelObservacao;
	}
	
	public Boolean getSituacaoIngressante() {
		if (situacaoIngressante == null) {
			situacaoIngressante = true;
		}
		return situacaoIngressante;
	}

	public void setSituacaoIngressante(Boolean situacaoIngressante) {
		this.situacaoIngressante = situacaoIngressante;
	}

	public Boolean getSituacaoConcluinte() {
		if (situacaoConcluinte == null) {
			situacaoConcluinte = true;
		}
		return situacaoConcluinte;
	}

	public void setSituacaoConcluinte(Boolean situacaoConcluinte) {
		this.situacaoConcluinte = situacaoConcluinte;
	}

	public Boolean getSituacaoDispensado() {
		if (situacaoDispensado == null) {
			situacaoDispensado = false;
		}
		return situacaoDispensado;
	}

	public void setSituacaoDispensado(Boolean situacaoDispensado) {
		this.situacaoDispensado = situacaoDispensado;
	}
	
	public void realizarPersistenciaPreferenciasUsuario() {
		try {
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getCampoConsultaRelEnade(), "mapaConvocacaoEnadeRel", "campoConsultaRelEnade", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getSituacaoIngressante().toString(), "mapaConvocacaoEnadeRel", "ingressante", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getSituacaoConcluinte().toString(), "mapaConvocacaoEnadeRel", "concluinte", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getSituacaoDispensado().toString(), "mapaConvocacaoEnadeRel", "dispensado", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getCampoOrdernarRel(), "mapaConvocacaoEnadeRel", "ordenarPor", getUsuarioLogado());
		} catch (Exception e) {
	}
}
	
	public void realizarConsultaPreferenciasUsuario() {
		try {
			LayoutPadraoVO tipoRelatorio = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("mapaConvocacaoEnadeRel", "campoConsultaRelEnade", false, getUsuarioLogado());
			if (!tipoRelatorio.getValor().equals("")) {
				setCampoConsultaRelEnade(tipoRelatorio.getValor());
			}
			LayoutPadraoVO ingressante = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("mapaConvocacaoEnadeRel", "ingressante", false, getUsuarioLogado());
			if (!ingressante.getValor().equals("")) {
				setSituacaoIngressante(Boolean.parseBoolean(ingressante.getValor()));
			}
			LayoutPadraoVO concluinte = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("mapaConvocacaoEnadeRel", "concluinte", false, getUsuarioLogado());
			if (!concluinte.getValor().equals("")) {
				setSituacaoConcluinte(Boolean.parseBoolean(concluinte.getValor()));
			}
			LayoutPadraoVO dispensado = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("mapaConvocacaoEnadeRel", "dispensado", false, getUsuarioLogado());
			if (!dispensado.getValor().equals("")) {
				setSituacaoDispensado(Boolean.parseBoolean(dispensado.getValor()));
			}
			LayoutPadraoVO ordenarPor = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("mapaConvocacaoEnadeRel", "ordenarPor", false, getUsuarioLogado());
			if (!ordenarPor.getValor().equals("")) {
				setCampoOrdernarRel(ordenarPor.getValor());
			}
		} catch (Exception e) {
		}
	}
	
	
	public EnadeVO getEnadeVO() {
		if (enadeVO == null) {
			enadeVO = new EnadeVO();
		}
		return enadeVO;
	}

	public void setEnadeVO(EnadeVO enadeVO) {
		this.enadeVO = enadeVO;
	}
	public List<MapaConvocacaoEnadeVO> getListaMapaConvocacaoEnadeVOErros() {
		if(listaMapaConvocacaoEnadeVOErros == null ) {
			listaMapaConvocacaoEnadeVOErros = new ArrayList<MapaConvocacaoEnadeVO>(0);
		}
		return listaMapaConvocacaoEnadeVOErros;
	}

	public void setListaMapaConvocacaoEnadeVOErros(List<MapaConvocacaoEnadeVO> listaMapaConvocacaoEnadeVOErros) {
		this.listaMapaConvocacaoEnadeVOErros = listaMapaConvocacaoEnadeVOErros;
	}
	public List<EnadeCursoVO> getListaConsultaEnadeCurso() {
		if (listaConsultaEnadeCurso == null) {
			listaConsultaEnadeCurso = new ArrayList<EnadeCursoVO>(0);
		}
		return listaConsultaEnadeCurso;
	}

	public void setListaConsultaEnadeCurso(List<EnadeCursoVO> listaConsultaEnadeCurso) {
		this.listaConsultaEnadeCurso = listaConsultaEnadeCurso;
	} 	
	public Boolean getApresentarEnade() {
		if(apresentarEnade == null ) {
			apresentarEnade = Boolean.FALSE;
		}
		return apresentarEnade;
	}

	public void setApresentarEnade(Boolean apresentarEnade) {
		this.apresentarEnade = apresentarEnade;
	}

	public Boolean getApresentarListaErros() {
		if(apresentarListaErros == null ) {
			apresentarListaErros = Boolean.FALSE;
		}
		return apresentarListaErros;
	}

	public void setApresentarListaErros(Boolean apresentarListaErros) {
		this.apresentarListaErros = apresentarListaErros;
	} 

	public List<MapaConvocacaoEnadeVO> getListaMapaConvocacaoEnadeVOs() {
		if(listaMapaConvocacaoEnadeVOs == null ) {
			listaMapaConvocacaoEnadeVOs = new ArrayList<MapaConvocacaoEnadeVO>(0);
		}
		return listaMapaConvocacaoEnadeVOs;
	}

	public void setListaMapaConvocacaoEnadeVOs(List<MapaConvocacaoEnadeVO> listaMapaConvocacaoEnadeVOs) {
		this.listaMapaConvocacaoEnadeVOs = listaMapaConvocacaoEnadeVOs;
	}
	
	public Boolean getApresentarResultadoConsultaEnade() {
		if(apresentarResultadoConsultaEnade == null ) {
			apresentarResultadoConsultaEnade = Boolean.FALSE;
		}
		return apresentarResultadoConsultaEnade;
	}

	public void setApresentarResultadoConsultaEnade(Boolean apresentarResultadoConsultaEnade) {
		this.apresentarResultadoConsultaEnade = apresentarResultadoConsultaEnade;
	}
		
	public void obterCaminhoServidorDownloadArquivoAlunoIngressante() {
		try {
			MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO = getMapaConvocacaoEnadeVO();
			setArquivoVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(mapaConvocacaoEnadeVO.getArquivoAlunoIngressante().getCodigo(), Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, getUsuarioLogado()));
			fazerDownloadArquivo(getArquivoVO());
			mapaConvocacaoEnadeVO = new MapaConvocacaoEnadeVO();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}
	
	public void obterCaminhoServidorDownloadArquivoAlunoConcluinte() {
		try {
			MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO = getMapaConvocacaoEnadeVO();
			setArquivoVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(mapaConvocacaoEnadeVO.getArquivoAlunoConcluinte().getCodigo(), Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, getUsuarioLogado()));
			fazerDownloadArquivo(getArquivoVO());
			mapaConvocacaoEnadeVO = new MapaConvocacaoEnadeVO();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}
	
	private void fazerDownloadArquivo(ArquivoVO arquivoVO) throws Exception {
		context().getExternalContext().getSessionMap().put("arquivoVO", arquivoVO);
		context().getExternalContext().dispatch("/DownloadSV");
		FacesContext.getCurrentInstance().responseComplete();
	}	
	
	public void consultarEnadeParaRelatorio() {
		try {
			super.consultar();
			Integer codigoCurso = getMapaConvocacaoEnadeVO().getEnadeCursoVO().getCursoVO().getCodigo();
			Integer codigoEnade = getMapaConvocacaoEnadeVO().getEnadeCursoVO().getEnadeVO().getCodigo();
			getMapaConvocacaoEnadeVO().setMapaConvocacaoEnadeVORelTxtIngressantes(getFacadeFactory().getMapaConvocacaoEnadeFacade().consultarEnadeCursoParaTXT("enade", codigoEnade, null, null, null, getUsuarioLogado(), NivelMontarDados.TODOS, codigoCurso));
			getMapaConvocacaoEnadeVO().setMapaConvocacaoEnadeVORelTxtConcluintes(getFacadeFactory().getMapaConvocacaoEnadeFacade().consultarEnadeCursoParaTXT("enade", codigoEnade, null, null, null, getUsuarioLogado(), NivelMontarDados.TODOS, codigoCurso));
			if(Uteis.isAtributoPreenchido(getMapaConvocacaoEnadeVO().getArquivoAlunoConcluinte())){
				getMapaConvocacaoEnadeVO().getArquivoAlunoConcluinte().setNome(getMapaConvocacaoEnadeVO().getArquivoAlunoConcluinte().getNome());
			}
			if(Uteis.isAtributoPreenchido(getMapaConvocacaoEnadeVO().getArquivoAlunoIngressante())){
				getMapaConvocacaoEnadeVO().getArquivoAlunoIngressante().setNome(getMapaConvocacaoEnadeVO().getArquivoAlunoIngressante().getNome());
			}
			if(Uteis.isAtributoPreenchido( getMapaConvocacaoEnadeVO().getMapaConvocacaoEnadeVORelTxtIngressantes().getArquivoAlunoIngressante().getCodigo()) || ! getMapaConvocacaoEnadeVO().getMapaConvocacaoEnadeVORelTxtIngressantes().getArquivoAlunoIngressante().getCodigo().equals(0)){
				getMapaConvocacaoEnadeVO().setIsMostrarDownloadArquivoIngressante(Boolean.TRUE);
			}else { 
				getMapaConvocacaoEnadeVO().setIsMostrarDownloadArquivoIngressante(Boolean.FALSE);
			}
			if(Uteis.isAtributoPreenchido( getMapaConvocacaoEnadeVO().getMapaConvocacaoEnadeVORelTxtConcluintes().getArquivoAlunoConcluinte().getCodigo()) || ! getMapaConvocacaoEnadeVO().getMapaConvocacaoEnadeVORelTxtConcluintes().getArquivoAlunoConcluinte().getCodigo().equals(0)){
				getMapaConvocacaoEnadeVO().setIsMostrarDownloadArquivoConcluinte(Boolean.TRUE);
			}else { 
				getMapaConvocacaoEnadeVO().setIsMostrarDownloadArquivoConcluinte(Boolean.FALSE);
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaMapaConvocacaoEnadeVOsRel(new ArrayList<MapaConvocacaoEnadeVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<MapaConvocacaoEnadeVO> getListaMapaConvocacaoEnadeVOsRel() {
		return listaMapaConvocacaoEnadeVOsRel;
	}

	public void setListaMapaConvocacaoEnadeVOsRel(List<MapaConvocacaoEnadeVO> listaMapaConvocacaoEnadeVOsRel) {
		this.listaMapaConvocacaoEnadeVOsRel = listaMapaConvocacaoEnadeVOsRel;
	}

	
	public void gerarArquivoMapaConvocacaoEnadeIngressantes() {
		getMapaConvocacaoEnadeVO().getListaAlunoErroGeracaoArquivoEnade().clear();
		getMapaConvocacaoEnadeVO().setArquivoAlunoIngressante(null);
		EnadeVO enadeVO = new EnadeVO();
		try {
			getMapaConvocacaoEnadeVO().setConcluintes(Boolean.FALSE);
			getMapaConvocacaoEnadeVO().setIngressantes(Boolean.TRUE);
			enadeVO = getFacadeFactory().getEnadeFacade().consultarPorChavePrimaria(getMapaConvocacaoEnadeVO().getEnadeCursoVO().getEnadeVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuario());
			getFacadeFactory().getMapaConvocacaoEnadeFacade().realizarGeracaoArquivoMapaConvocacaoEnade(getMapaConvocacaoEnadeVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), enadeVO,  getUsuarioLogado());
			getMapaConvocacaoEnadeVO().setIsMostrarDownloadArquivoIngressante(Boolean.TRUE);
			setOnCompleteErroGeracaoArquivo("RichFaces.$('panelErrosGeracaoArquivoTXT').hide();");
			setMensagemID("Arquivo Gerado Com Sucesso", Uteis.SUCESSO);
		} catch (ConsistirException ce) {
			if (!ce.getListaMensagemErro().isEmpty()) {
				setListaMensagemErro(ce.getListaMensagemErro());
			} else {
				getListaMensagemErro().add(ce.getMessage());
			}
		} catch (Exception e) {
			if (!getMapaConvocacaoEnadeVO().getListaAlunoErroGeracaoArquivoEnade().isEmpty()) {
				getMapaConvocacaoEnadeVO().setListaAlunoErroGeracaoArquivoEnade(getMapaConvocacaoEnadeVO().getListaAlunoErroGeracaoArquivoEnade());
				setOnCompleteErroGeracaoArquivo("RichFaces.$('panelErrosGeracaoArquivoTXT').show();");
			}
			if (!getMapaConvocacaoEnadeVO().getlistaCursoErroGeracaoArquivoEnade().isEmpty()) {
				getMapaConvocacaoEnadeVO().setListaCursoErroGeracaoArquivoEnade(getMapaConvocacaoEnadeVO().getlistaCursoErroGeracaoArquivoEnade());
				setOnCompleteErroGeracaoArquivo("RichFaces.$('panelErrosGeracaoArquivoTXT').show();");
			}
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void gerarArquivoMapaConvocacaoEnadeConcluintes() {
		setOnCompleteErroGeracaoArquivo("");
		getListaMensagemErro().clear();
		getMapaConvocacaoEnadeVO().getListaAlunoErroGeracaoArquivoEnade().clear();
		getMapaConvocacaoEnadeVO().setArquivoAlunoConcluinte(null);
		EnadeVO enadeVO = new EnadeVO();
		try {
			getMapaConvocacaoEnadeVO().setIngressantes(Boolean.FALSE);
			getMapaConvocacaoEnadeVO().setConcluintes(Boolean.TRUE);
			enadeVO = getFacadeFactory().getEnadeFacade().consultarPorChavePrimaria(getMapaConvocacaoEnadeVO().getEnadeCursoVO().getEnadeVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuario());
			getFacadeFactory().getMapaConvocacaoEnadeFacade().realizarGeracaoArquivoMapaConvocacaoEnade(getMapaConvocacaoEnadeVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), enadeVO,  getUsuarioLogado());
			getMapaConvocacaoEnadeVO().setIsMostrarDownloadArquivoConcluinte(Boolean.TRUE);
			setMensagemID("Arquivo Gerado Com Sucesso", Uteis.SUCESSO);
		} catch (ConsistirException ce) {
			if (!ce.getListaMensagemErro().isEmpty()) {
				setListaMensagemErro(ce.getListaMensagemErro());
			} else {
				getListaMensagemErro().add(ce.getMessage());
			}
		} catch (Exception e) {
			if (!getMapaConvocacaoEnadeVO().getListaAlunoErroGeracaoArquivoEnade().isEmpty()) {
				getMapaConvocacaoEnadeVO().setListaAlunoErroGeracaoArquivoEnade(getMapaConvocacaoEnadeVO().getListaAlunoErroGeracaoArquivoEnade());
				setOnCompleteErroGeracaoArquivo("RichFaces.$('panelErrosGeracaoArquivoTXT').show();");
			}
			if (!getMapaConvocacaoEnadeVO().getlistaCursoErroGeracaoArquivoEnade().isEmpty()) {
				getMapaConvocacaoEnadeVO().setListaCursoErroGeracaoArquivoEnade(getMapaConvocacaoEnadeVO().getlistaCursoErroGeracaoArquivoEnade());
				setOnCompleteErroGeracaoArquivo("RichFaces.$('panelErrosGeracaoArquivoTXT').show();");
			}
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void gravarCodigoProjeto() {
		try {
			EnadeVO enadeVO = getFacadeFactory().getEnadeFacade().consultarPorChavePrimaria(getMapaConvocacaoEnadeVO().getEnadeCursoVO().getEnadeVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			enadeVO.setCodigoProjeto(getMapaConvocacaoEnadeVO().getEnadeCursoVO().getEnadeVO().getCodigoProjeto());
			Integer quantidadeCaracteres = enadeVO.getCodigoProjeto().replace(" ", "").length();
			if(quantidadeCaracteres.equals(7)) {
				getFacadeFactory().getEnadeFacade().alterar(enadeVO, getUsuarioLogado());
				setMensagemID("msg_dados_gravados");
			} else {
				throw new Exception("O Código do projeto deve conter 7 caracteres.");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	
	}
	public List<EnadeVO> getListaConsultaEnade() {
		if (listaConsultaEnade == null) {
			listaConsultaEnade = new ArrayList<EnadeVO>(0);
		}
		return listaConsultaEnade;
	}

	public void setListaConsultaEnade(List<EnadeVO> listaConsultaEnade) {
		this.listaConsultaEnade = listaConsultaEnade;
	}

	public List<MapaConvocacaoEnadeVO> getListaSituacaoEnade() {
		if (listaSituacaoEnade == null) {
			listaSituacaoEnade = new ArrayList<MapaConvocacaoEnadeVO>(0);
		}
		return listaSituacaoEnade;
	}

	public void setListaSituacaoEnade(List<MapaConvocacaoEnadeVO> listaSituacaoEnade) {
		this.listaSituacaoEnade = listaSituacaoEnade;
	}

	
	public void excluirArquivoTxt() {
		try {
			getFacadeFactory().getMapaConvocacaoEnadeFacade().excluirArquivoTxt(getMapaConvocacaoEnadeVO(), getMapaConvocacaoEnadeMatriculaVO(), getUsuarioLogado());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public ArquivoVO getArquivoVO() {
		if (arquivoVO == null) {
			arquivoVO = new ArquivoVO();
		}
		return arquivoVO;
	}
	
	public void setArquivoVO(ArquivoVO arquivoVO) {
		this.arquivoVO = arquivoVO;
	}	

	public String getOnCompleteRegerarArquivo() {
		if (onCompleteRegerarArquivo == null) {
			onCompleteRegerarArquivo = "";
		}
		return onCompleteRegerarArquivo;
	}

	public void setOnCompleteRegerarArquivo(String onCompleteRegerarArquivo) {
		this.onCompleteRegerarArquivo = onCompleteRegerarArquivo;
	}

	public String getOnCompleteErroGeracaoArquivo() {
		if (onCompleteErroGeracaoArquivo == null) {
			onCompleteErroGeracaoArquivo = "";
		}
		return onCompleteErroGeracaoArquivo;
	}

	public void setOnCompleteErroGeracaoArquivo(String onCompleteErroGeracaoArquivo) {
		this.onCompleteErroGeracaoArquivo = onCompleteErroGeracaoArquivo;
	}
	
	//ROTINA RESPONSAVEL POR COMPACTAR OS ARQUIVOS
		public String obterCaminhoServidorDownloadArquivoAlunoIngressanteConcluinte() {
			try {
				List<File> files = new ArrayList<File>();
				setListaExportarMapaConvocacaoEnadeVOs(getFacadeFactory().getMapaConvocacaoEnadeFacade().consultaRapidaPorEnadeCurso(getMapaConvocacaoEnadeVO().getEnadeCursoVO().getEnadeVO().getCodigo(), getMapaConvocacaoEnadeVO().getEnadeCursoVO().getCodigo(), getMapaConvocacaoEnadeVO().getCodigo(), false, false, getUsuarioLogado()));
				List<MapaConvocacaoEnadeVO> mapaConvocacaoEnadeVOs  = getListaExportarMapaConvocacaoEnadeVOs();
				for(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO : mapaConvocacaoEnadeVOs) {
				ArquivoVO arquivoIngressanteVO = getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(mapaConvocacaoEnadeVO.getArquivoAlunoIngressante().getCodigo(), Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, getUsuarioLogado());
				ArquivoVO arquivoConcluinteVO = getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(mapaConvocacaoEnadeVO.getArquivoAlunoConcluinte().getCodigo(), Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, getUsuarioLogado());
				 File fileIngressante = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + arquivoIngressanteVO.getPastaBaseArquivo() + File.separator + arquivoIngressanteVO.getNome());
				 files.add(fileIngressante);
				 File fileConcluinte = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + arquivoConcluinteVO.getPastaBaseArquivo() + File.separator + arquivoConcluinteVO.getNome());
				 files.add(fileConcluinte);
				 if(files.size() >= 1) {
					setCaminhoRelatorio("");
					String nomeNovoArquivo = "arquivoIngressantesConcluintes"+new Date().getTime()+".zip";
					File[] filesArray = new File[files.size()];
					getFacadeFactory().getArquivoHelper().zip(files.toArray(filesArray), new File( getCaminhoPastaWeb() +  "relatorio" + File.separator + nomeNovoArquivo));
					setCaminhoRelatorio(nomeNovoArquivo);				
					setFazerDownload(true);				
				}else {
					setFazerDownload(false);	
				}
			}
			} catch (Exception ex) {
				setMensagemDetalhada("msg_erro", ex.getMessage());
			}
			return "";
		}
		
		public String getOnCompleteDownloadMapaconvocacaoEnade() {
			if(onCompleteDownloadMapaconvocacaoEnade == null ) {
				onCompleteDownloadMapaconvocacaoEnade ="";
			}
			return onCompleteDownloadMapaconvocacaoEnade;
		}

		public void setOnCompleteDownloadMapaconvocacaoEnade(String onCompleteDownloadMapaconvocacaoEnade) {
			this.onCompleteDownloadMapaconvocacaoEnade = onCompleteDownloadMapaconvocacaoEnade;
		}
		public List<MapaConvocacaoEnadeVO> getListaExportarMapaConvocacaoEnadeVOs() {
			if (listaExportarMapaConvocacaoEnadeVOs == null) {
				listaExportarMapaConvocacaoEnadeVOs = new ArrayList<MapaConvocacaoEnadeVO>();
			}
			return listaExportarMapaConvocacaoEnadeVOs;
		}

		public void setListaExportarMapaConvocacaoEnadeVOs(List<MapaConvocacaoEnadeVO> listaExportarMapaConvocacaoEnadeVOs) {
			this.listaExportarMapaConvocacaoEnadeVOs = listaExportarMapaConvocacaoEnadeVOs;
		}
		
	//ROTINA RESPONSAVEL POR FAZER O DOWNLOAD DOS ARQUIVOS COMPACTADOS REFERENTE AO ENADE QUE FORAM GERADOS NO MAPA CONVOCACAO ENADE. 	
	public String getDownloadMapaconvocacaoEnade() {
			if (getFazerDownload()) {
				String download = super.getDownload();
				setOnCompleteDownloadMapaconvocacaoEnade(download);
				return download;
			}
				try {
					realizarDownloadArquivo(getArquivoVO());
					return "";
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (getFazerDownload()) {
				String download = super.getDownload();
				setOnCompleteDownloadMapaconvocacaoEnade(download);
				return download;
			}
			
			return "";
		}
	
	public void ingressante() {
		getMapaConvocacaoEnadeVO().setConcluintes(Boolean.FALSE);
		getMapaConvocacaoEnadeVO().setIngressantes(Boolean.TRUE);
	}
	public void concluinte() {
		getMapaConvocacaoEnadeVO().setIngressantes(Boolean.FALSE);
		getMapaConvocacaoEnadeVO().setConcluintes(Boolean.TRUE);
	}
	public void ingressanteConcluinte() {
		getMapaConvocacaoEnadeVO().setIngressantes(Boolean.TRUE);
		getMapaConvocacaoEnadeVO().setConcluintes(Boolean.TRUE);
	}
	
	public void regerarArquivoTxt() {
		try {
			getFacadeFactory().getMapaConvocacaoEnadeFacade().excluirArquivoTxt(getMapaConvocacaoEnadeVO(), getMapaConvocacaoEnadeMatriculaVO(), getUsuarioLogado());
			if(getMapaConvocacaoEnadeVO().getIngressantes()) {
				gerarArquivoMapaConvocacaoEnadeIngressantes();	
			} 
			else if (getMapaConvocacaoEnadeVO().getConcluintes()) {
				gerarArquivoMapaConvocacaoEnadeConcluintes();
			}
			setMensagemID("Arquivo Regerado Com Sucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("");
			verificarTodasUnidadeEnsinoSelecionados();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<MatriculaVO> getMatriculaVOs() {
		if (matriculaVOs == null) {
			matriculaVOs = new ArrayList<MatriculaVO>();
		}
		return matriculaVOs;
	}

	public void setMatriculaVOs(List<MatriculaVO> matriculaVOs) {
		this.matriculaVOs = matriculaVOs;
	}

	public void executarInicioProgressBarConsultaAlunoEnade() {
		try {
			limparDadosNovaConsulta();
			getMatriculaVOs().addAll(getFacadeFactory().getMapaConvocacaoEnadeFacade().consultarAlunosMapaConvocacaoEnade(getMapaConvocacaoEnadeVO(), getMapaConvocacaoEnadeVO().getEnadeCursoVO().getCursoVO().getConfiguracaoAcademico(), getProgressBar(), getUsuarioLogado(), getUnidadeEnsinoVOs()));
		    if (!getMatriculaVOs().isEmpty()) {
				setProgressBar(new ProgressBarVO());			
				getProgressBar().setUsuarioVO(getUsuarioLogadoClone());
				getProgressBar().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
				getProgressBar().setUnidadeEnsinoVO(getUnidadeEnsinoLogado());
				getProgressBar().setCaminhoWebRelatorio(getCaminhoPastaWeb());			
				getProgressBar().resetar();
				getProgressBar().iniciar(0l, (getMatriculaVOs().size() ), "Carregando Alunos Enade", true, this, "processarDefinicaoMapaConvocacaoEnadePorPercentualIntegralizacao");					
			} else {
				throw new ConsistirException("Não existem dados a serem gerados .");
			}	
		} catch (Exception e) {
			if (e instanceof ConsistirException) {
				setConsistirExceptionMensagemDetalhada("msg_erro", (ConsistirException) e, Uteis.ERRO);
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}
	
	public void processarDefinicaoMapaConvocacaoEnadePorPercentualIntegralizacao() {
		try {
			if (!getMatriculaVOs().isEmpty()) {
				getFacadeFactory().getMapaConvocacaoEnadeFacade().processarDefinicaoMapaConvocacaoEnadePorPercentualIntegralizacao(getMatriculaVOs(), getMapaConvocacaoEnadeVO(), getMapaConvocacaoEnadeVO().getEnadeCursoVO().getCursoVO().getConfiguracaoAcademico(), getProgressBar(), getUsuarioLogado());
			}
		} catch (Exception e) {
			if (e instanceof ConsistirException) {
				setConsistirExceptionMensagemDetalhada("msg_erro", (ConsistirException) e, Uteis.ERRO);
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		} finally {
			getProgressBar().setForcarEncerramento(true);
		}

	}
	
	public void limparDadosNovaConsulta() {
		getMapaConvocacaoEnadeVO().getMapaConvocacaoEnadeMatriculaAlunosIngressantesVOs().clear();
		getMapaConvocacaoEnadeVO().getMapaConvocacaoEnadeMatriculaAlunosDispensadosVOs().clear();
		getMapaConvocacaoEnadeVO().getMapaConvocacaoEnadeMatriculaAlunosConcluintesVOs().clear();
		getListaConsultaEnadeCurso().clear();
		getListaConsultaEnade().clear();
		getMatriculaVOs().clear();
		getMapaConvocacaoEnadeVO().setTotalAlunosIngressantes(0);
		getMapaConvocacaoEnadeVO().setTotalAlunosDispensados(0);
		getMapaConvocacaoEnadeVO().setTotalAlunosConcluintes(0);
	}

	public ProgressBarVO getProgressBarTxt() {
		if (progressBarTxt == null) {
			progressBarTxt = new ProgressBarVO();
		}
		return progressBarTxt;
	}

	public void setProgressBarTxt(ProgressBarVO progressBarTxt) {
		this.progressBarTxt = progressBarTxt;
	}
	
	public void montarUnidadesEnsino() {
		consultarUnidadeEnsinoFiltroRelatorio("");
		String unidades = getMapaConvocacaoEnadeVO().getUnidadeEnsino().replaceAll("[\\[\\](){}]","").replaceAll(" ", "");
		if (Uteis.isAtributoPreenchido(unidades)) {
			List<Integer> unidadeEnsinoVOs = Arrays.stream(unidades.split(",")).map(Integer::parseInt).collect(Collectors.toList());
			for (UnidadeEnsinoVO unidadeEnsinoVO : getUnidadeEnsinoVOs()) {
				for (Integer codigoUnidadeEnsino : unidadeEnsinoVOs) {
					if (unidadeEnsinoVO.getCodigo().equals(codigoUnidadeEnsino)) {
						unidadeEnsinoVO.setFiltrarUnidadeEnsino(true);
						break;
					} else {
						unidadeEnsinoVO.setFiltrarUnidadeEnsino(false);
					}
				}
			}
			verificarTodasUnidadeEnsinoSelecionados();
		} else {
			setUnidadeEnsinosApresentar("");
		}
	}
}