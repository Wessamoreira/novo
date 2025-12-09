package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO;

@Controller("RecebimentoPorUnidadeCursoTurmaRelControle")
@Scope("viewScope")
@Lazy
public class RecebimentoPorUnidadeCursoTurmaRelControle extends SuperControleRelatorio {

	private UnidadeEnsinoVO unidadeEnsinoVO;
	private Boolean filtrarPorDataCompetencia;
	private MatriculaVO matriculaVO;
	private ContaCorrenteVO contaCorrenteVO;
	private List<UnidadeEnsinoVO> listaSelectItemUnidadeEnsino;
	private List<ContaCorrenteVO> listaSelectItemContaCorrente;
	private List<SelectItem> listaSelectItemOrdenacao;
	private List<SelectItem> listaSelectItemSituacao;
	private List<SelectItem> listaSelectItemFiltro;
	private List listaConsultaTurma;
	protected List listaSelectItemParcelas;
	private String filtro;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private boolean trazerContasConvenio;
	private TurmaVO turma;
	private CursoVO curso;
	private List listaConsultaCurso;
	private Date dataInicio;
	private Date dataFim;
	private String parcela;
	private Integer ordenacao;
	private String situacao;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private List listaConsultaAluno;
	private String tipoLayout;
	private Boolean utilizarValorCompensado;
	private Boolean marcarTodosTipoOrigem;

	public RecebimentoPorUnidadeCursoTurmaRelControle() throws Exception {
		inicializarListasSelectItemTodosComboBox();
		setTrazerContasConvenio(Boolean.TRUE);
		setMensagemID("msg_entre_prmrelatorio");
	}

	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("RecebimentoPorUnidadeCursoTurmaRel");
			verificarTodasUnidadesSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTipoReq() {
		try {
			boolean umSelecionad = false;
			for (TipoRequerimentoVO obj : getTipoReqVOs()) {
				if (obj.getFiltrarTipoReq()) {				
					umSelecionad = true;
				}
			}
			if (!umSelecionad) {
				getTipoReqVOs().clear();
				if (getUnidadeEnsinoLogado().getCodigo() > 0) {
					setTipoReqVOs(getFacadeFactory().getTipoRequerimentoFacade().consultarPorNome("%", "AT", getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
					for (TipoRequerimentoVO obj : getTipoReqVOs()) {
						obj.setFiltrarTipoReq(true);
					}
				} else {
					setTipoReqVOs(getFacadeFactory().getTipoRequerimentoFacade().consultarPorNome("%", "AT", getUnidadeEnsinoVOs(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
				}
				setMarcarTodosTipoReq(Boolean.TRUE);
				marcarTodosTipoReqAction();				
			}			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void marcarTodosTipoReqAction() {
		for (TipoRequerimentoVO unidade : getTipoReqVOs()) {
			if (getMarcarTodosTipoReq()) {
				unidade.setFiltrarTipoReq(Boolean.TRUE);
			} else {
				unidade.setFiltrarTipoReq(Boolean.FALSE);
			}
		}
		//verificarTodasUnidadesSelecionadas();
	}
	
	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if (getUnidadeEnsinoVOs().size() > 1) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getNome()).append("; ");
				} 
			}
			getUnidadeEnsinoVO().setNome(unidade.toString());
		} else {			
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				if (getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					getUnidadeEnsinoVO().setNome(getUnidadeEnsinoVOs().get(0).getNome());
				} 
			} else {
				getUnidadeEnsinoVO().setNome(unidade.toString());
			}
		}
	}

	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			if (getMarcarTodasUnidadeEnsino()) {
				unidade.setFiltrarUnidadeEnsino(Boolean.TRUE);
			} else {
				unidade.setFiltrarUnidadeEnsino(Boolean.FALSE);
			}
		}
		verificarTodasUnidadesSelecionadas();
	}

	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);

			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				if (!obj.getMatricula().equals("")) {
					objs.add(obj);
				}
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}

			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
			setMatriculaVO(obj);
			getUnidadeEnsinoVO().setCodigo(getMatriculaVO().getUnidadeEnsino().getCodigo());
			obj = null;
			valorConsultaAluno = "";
			campoConsultaAluno = "";
			getListaConsultaAluno().clear();
		} catch (Exception e) {

		}
	}

	public void consultarAlunoPorMatricula() throws Exception {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getMatriculaVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMatriculaVO(objAluno);
			getUnidadeEnsinoVO().setCodigo(getMatriculaVO().getUnidadeEnsino().getCodigo());
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setMatriculaVO(new MatriculaVO());
		}
	}

	public void imprimirPDF() {
		List<RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO> listaRecebimento = new ArrayList<RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO>(0);
		List<ContaReceberVO> listaResumo = new ArrayList<ContaReceberVO>(0);
		Map<String, ContaReceberVO> hashMapCodigoContaCorrente = new HashMap<String, ContaReceberVO>();
		try {
			if (getFiltrarPorDataCompetencia()) {
				setDataInicio(Uteis.getDataPrimeiroDiaMes(getDataInicio()));
				setDataFim(Uteis.getDataUltimoDiaMes(getDataFim()));
			}
			getFacadeFactory().getRecebimentoPorUnidadeCursoTurmaRelInterfaceFacade().validarDados(getTurma().getCodigo(), getUnidadeEnsinoVOs());
			listaRecebimento = getFacadeFactory().getRecebimentoPorUnidadeCursoTurmaRelInterfaceFacade().criarObjeto(getUnidadeEnsinoVOs(), getTipoReqVOs(), getFiltrarPorDataCompetencia(), getDataInicio(), getDataFim(), getTurma(), getCurso(), getSituacao(), getMatriculaVO().getMatricula(), hashMapCodigoContaCorrente, getFiltro(), getContaCorrenteVO().getCodigo(), getUtilizarValorCompensado(), getFiltroRelatorioFinanceiroVO());
			Set<String> keys = hashMapCodigoContaCorrente.keySet();
			for (String chave : keys) {
				ContaReceberVO cc = (ContaReceberVO) hashMapCodigoContaCorrente.get(chave);
				listaResumo.add(cc);
			}
			Iterator i = listaRecebimento.iterator();
			while (i.hasNext()) {
				RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO re = (RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO) i.next();
				re.setListaResumo(listaResumo);
			}
			if (!listaRecebimento.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getRecebimentoPorUnidadeCursoTurmaRelInterfaceFacade().getDesignIReportRelatorio(getTipoLayout()));
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getRecebimentoPorUnidadeCursoTurmaRelInterfaceFacade().caminhoBaseIReportRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório de Recebimento por Unidade/Curso/Turma");
				getSuperParametroRelVO().setListaObjetos(listaRecebimento);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getRecebimentoPorUnidadeCursoTurmaRelInterfaceFacade().caminhoBaseIReportRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setQuantidade(listaRecebimento.size());
				getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(getDataInicio())) + "  a  " + String.valueOf(Uteis.getData(getDataFim())));
				getSuperParametroRelVO().setCurso(getCurso().getNome());
				getSuperParametroRelVO().setLista(listaResumo);
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
				if (getTurma().getCodigo() > 0) {
					getSuperParametroRelVO().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getIdentificadorTurma());
				} else {
					getSuperParametroRelVO().setTurma("TODAS");
				}

				realizarImpressaoRelatorio();
				removerObjetoMemoria(this);
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaRecebimento);
			inicializarListasSelectItemTodosComboBox();
		}
	}

	public void imprimirRelatorioExcel() {
		List<RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO> listaRecebimento = new ArrayList<RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO>(0);
		List<ContaReceberVO> listaResumo = new ArrayList<ContaReceberVO>(0);
		Map<String, ContaReceberVO> hashMapCodigoContaCorrente = new HashMap<String, ContaReceberVO>();
		try {
			getFacadeFactory().getRecebimentoPorUnidadeCursoTurmaRelInterfaceFacade().validarDados(getTurma().getCodigo(), getUnidadeEnsinoVOs());
			listaRecebimento = getFacadeFactory().getRecebimentoPorUnidadeCursoTurmaRelInterfaceFacade().criarObjeto(getUnidadeEnsinoVOs(),getTipoReqVOs(), getFiltrarPorDataCompetencia(), getDataInicio(), getDataFim(), getTurma(), getCurso(), getSituacao(), getMatriculaVO().getMatricula(), hashMapCodigoContaCorrente, getFiltro(), getContaCorrenteVO().getCodigo(), getUtilizarValorCompensado(), getFiltroRelatorioFinanceiroVO());
			Set<String> keys = hashMapCodigoContaCorrente.keySet();
			for (String chave : keys) {
				ContaReceberVO cc = (ContaReceberVO) hashMapCodigoContaCorrente.get(chave);
				listaResumo.add(cc);
			}
			Iterator i = listaRecebimento.iterator();
			while (i.hasNext()) {
				RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO re = (RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO) i.next();
				re.setListaResumo(listaResumo);
			}
			if (!listaRecebimento.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getRecebimentoPorUnidadeCursoTurmaRelInterfaceFacade().getDesignIReportRelatorioExcel(getTipoLayout()));
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getRecebimentoPorUnidadeCursoTurmaRelInterfaceFacade().caminhoBaseIReportRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório de Recebimento por Unidade/Curso/Turma");
				getSuperParametroRelVO().setListaObjetos(listaRecebimento);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getRecebimentoPorUnidadeCursoTurmaRelInterfaceFacade().caminhoBaseIReportRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setQuantidade(listaRecebimento.size());
				getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(getDataInicio())) + "  a  " + String.valueOf(Uteis.getData(getDataFim())));
				getSuperParametroRelVO().setCurso(getCurso().getNome());
				getSuperParametroRelVO().setLista(listaResumo);

				if (getUnidadeEnsinoVO().getCodigo() > 0) {
					getSuperParametroRelVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()).getNome());
				} else {
					getSuperParametroRelVO().setUnidadeEnsino("TODAS");
				}

				if (getTurma().getCodigo() > 0) {
					getSuperParametroRelVO().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getIdentificadorTurma());
				} else {
					getSuperParametroRelVO().setTurma("TODAS");
				}

				realizarImpressaoRelatorio();
				removerObjetoMemoria(this);
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaRecebimento);
			inicializarListasSelectItemTodosComboBox();
		}
	}

	public void selecionarTurma() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
		getTurma().setCodigo(obj.getCodigo());
		getTurma().setIdentificadorTurma(obj.getIdentificadorTurma());

		getCurso().setCodigo(obj.getCurso().getCodigo());
		getCurso().setNome(obj.getCurso().getNome());

		buscarParcelasParaImpressao();
		setCampoConsultaTurma("");
		setValorConsultaTurma("");
		setListaConsultaTurma(new ArrayList<TurmaVO>(0));

	}

	public void selecionarCurso() throws Exception {
		CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
		getCurso().setCodigo(obj.getCodigo());
		getCurso().setNome(obj.getNome());
		setCampoConsultaCurso("");
		setValorConsultaCurso("");
		setListaConsultaCurso(new ArrayList<CursoVO>(0));
	}

	public void buscarParcelasParaImpressao() throws Exception {
		try {
			List<String> parcelas = new ArrayList<String>(0);
			parcelas = getFacadeFactory().getBoletoBancarioRelFacade().executarConsultaParcelasTurma(getTurma().getCodigo(), getDataInicio(), getDataFim());
			List<SelectItem> selectItemParcelas = new ArrayList<SelectItem>();
			selectItemParcelas.add(new SelectItem("", ""));
			if (parcelas != null && !parcelas.isEmpty()) {
				for (String parcela : parcelas) {
					selectItemParcelas.add(new SelectItem(parcela, parcela));
				}
				setListaSelectItemParcelas(selectItemParcelas);
				setMensagemDetalhada("");
			} else {
				setListaSelectItemParcelas(selectItemParcelas);
				throw new ConsistirException("Não há nenhuma parcela para esse(a) determinado(a) pessoa/parceiro nesse período");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemOrdenacao();
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemContaCorrente();
		montarListaSelectItemSituacao();
		montarListaSelectItemFiltro();
	}

	public void montarListaSelectItemOrdenacao() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("0", "Aluno"));
		itens.add(new SelectItem("1", "Data"));
		setListaSelectItemOrdenacao(itens);
	}

	public void montarListaSelectItemContaCorrente() {
		try {
			montarListaSelectItemContaCorrente("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	public List consultarContaCorrente(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getContaCorrenteFacade().consultarPorNumero(nomePrm, getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				objs.add(new SelectItem(0, ""));
			}
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemContaCorrente(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			setListaSelectItemContaCorrente(null);
			resultadoConsulta = consultarContaCorrente(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, "Todas"));
			// if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
			// objs.add(new SelectItem(0, "Todas"));
			// }
			while (i.hasNext()) {
				ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
				if(Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())){
					objs.add(new SelectItem(obj.getCodigo(), obj.getNomeApresentacaoSistema()));
				}else{
					objs.add(new SelectItem(obj.getCodigo(), obj.getNome() + " - " + obj.getNumeroDigito()));
				}
			}
			setListaSelectItemContaCorrente(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void limparDadosTurma() {
		getTurma().setCodigo(0);
		getTurma().setIdentificadorTurma("");
		setParcela("");
	}

	public void limparDadosCurso() {
		getCurso().setCodigo(0);
		getCurso().setNome("");
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public void montarListaSelectItemSituacao() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("receber", "A receber"));
		itens.add(new SelectItem("recebido", "Recebido"));
		itens.add(new SelectItem("todos", "Todos"));
		setListaSelectItemSituacao(itens);
	}

	public void montarListaSelectItemFiltro() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", "Todos"));
		itens.add(new SelectItem("isentos", "Somente Isentos"));
		itens.add(new SelectItem("pagantes", "Somente Pagantes"));
		setListaSelectItemFiltro(itens);
	}

	public String consultarCurso() {
		try {
			super.consultar();
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorCodigo(Integer.parseInt(getValorConsultaCurso()), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNome(getValorConsultaCurso(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, false, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}

	public String consultarTurma() {
		try {
			super.consultar();
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}

			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}

			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}

			if (getCampoConsultaTurma().equals("nomeTurno")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}

	}

	public void selecionarSituacao() {
		if (!getSituacao().equals("recebido")) {
			setFiltro("");
		}
	}

	public Boolean getRecebido() {
		if (!getSituacao().equals("recebido")) {
			return false;
		} else {
			return true;
		}
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = Uteis.getDataPrimeiroDiaMes(new Date());
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = Uteis.getDataUltimoDiaMes(new Date());
		}
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public List<UnidadeEnsinoVO> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<UnidadeEnsinoVO>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<UnidadeEnsinoVO> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<SelectItem> getListaSelectItemOrdenacao() {
		if (listaSelectItemOrdenacao == null) {
			listaSelectItemOrdenacao = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemOrdenacao;
	}

	public void setListaSelectItemOrdenacao(List<SelectItem> listaSelectItemOrdenacao) {
		this.listaSelectItemOrdenacao = listaSelectItemOrdenacao;
	}

	public boolean getTrazerContasConvenio() {
		return trazerContasConvenio;
	}

	public void setTrazerContasConvenio(boolean trazerContasConvenio) {
		this.trazerContasConvenio = trazerContasConvenio;
	}

	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return turma;
	}

	public void setTurma(TurmaVO turma) {
		this.turma = turma;
	}

	public String getParcela() {
		if (parcela == null) {
			parcela = "";
		}
		return parcela;
	}

	public void setParcela(String parcela) {
		this.parcela = parcela;
	}

	public Integer getOrdenacao() {
		if (ordenacao == null) {
			ordenacao = 0;
		}
		return ordenacao;
	}

	public void setOrdenacao(Integer ordenacao) {
		this.ordenacao = ordenacao;
	}

	public List getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public String getValorConsultaTurma() {
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public String getCampoConsultaTurma() {
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public List getListaSelectItemParcelas() {
		if (listaSelectItemParcelas == null) {
			listaSelectItemParcelas = new ArrayList(0);
		}
		return listaSelectItemParcelas;
	}

	public void setListaSelectItemParcelas(List listaSelectItemParcelas) {
		this.listaSelectItemParcelas = listaSelectItemParcelas;
	}

	public CursoVO getCurso() {
		if (curso == null) {
			curso = new CursoVO();
		}
		return curso;
	}

	public void setCurso(CursoVO curso) {
		this.curso = curso;
	}

	public List getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
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
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "recebido";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public List<SelectItem> getListaSelectItemSituacao() {
		if (listaSelectItemSituacao == null) {
			listaSelectItemSituacao = new ArrayList(0);
		}
		return listaSelectItemSituacao;
	}

	public void setListaSelectItemSituacao(List<SelectItem> listaSelectItemSituacao) {
		this.listaSelectItemSituacao = listaSelectItemSituacao;
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	public List getListaConsultaAluno() {
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public void limparDadosAluno() throws Exception {
		setMatriculaVO(new MatriculaVO());
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List getListaTipoLayout() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("Layout1", "Layout 1"));
		itens.add(new SelectItem("Layout2", "Layout 2"));
		return itens;
	}

	public String getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = "Layout2";
		}
		return tipoLayout;
	}

	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
	}

	public String getFiltro() {
		if (filtro == null) {
			filtro = "";
		}
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

	public ContaCorrenteVO getContaCorrenteVO() {
		if (contaCorrenteVO == null) {
			contaCorrenteVO = new ContaCorrenteVO();
		}
		return contaCorrenteVO;
	}

	public void setContaCorrenteVO(ContaCorrenteVO contaCorrenteVO) {
		this.contaCorrenteVO = contaCorrenteVO;
	}

	public List<ContaCorrenteVO> getListaSelectItemContaCorrente() {
		if (listaSelectItemContaCorrente == null) {
			listaSelectItemContaCorrente = new ArrayList();
		}
		return listaSelectItemContaCorrente;
	}

	public void setListaSelectItemContaCorrente(List<ContaCorrenteVO> listaSelectItemContaCorrente) {
		this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
	}

	public List<SelectItem> getListaSelectItemFiltro() {
		if (listaSelectItemFiltro == null) {
			listaSelectItemFiltro = new ArrayList();
		}
		return listaSelectItemFiltro;
	}

	public void setListaSelectItemFiltro(List<SelectItem> listaSelectItemFiltro) {
		this.listaSelectItemFiltro = listaSelectItemFiltro;
	}

	public Boolean getUtilizarValorCompensado() {
		if (utilizarValorCompensado == null) {
			utilizarValorCompensado = false;
		}
		return utilizarValorCompensado;
	}

	public void setUtilizarValorCompensado(Boolean utilizarValorCompensado) {
		this.utilizarValorCompensado = utilizarValorCompensado;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			if (getUnidadeEnsinoLogado().getCodigo() > 0) {
				unidadeEnsinoVO = getUnidadeEnsinoLogado();
			} else {
				unidadeEnsinoVO = new UnidadeEnsinoVO();
			}
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public Boolean getFiltrarPorDataCompetencia() {
		if (filtrarPorDataCompetencia == null) {
			filtrarPorDataCompetencia = false;
		}
		return filtrarPorDataCompetencia;
	}

	public void setFiltrarPorDataCompetencia(Boolean filtrarPorDataCompetencia) {
		this.filtrarPorDataCompetencia = filtrarPorDataCompetencia;
	}

	public boolean getIsApresentarBotaoSelecionarUnidadeEnsino() throws Exception {
		return getUnidadeEnsinoVOs().size() > 1;
	}

	public Boolean getMarcarTodosTipoOrigem() {
		if (marcarTodosTipoOrigem == null) {
			marcarTodosTipoOrigem = Boolean.TRUE;
		}
		return marcarTodosTipoOrigem;
	}

	public void setMarcarTodosTipoOrigem(Boolean marcarTodosTipoOrigem) {
		this.marcarTodosTipoOrigem = marcarTodosTipoOrigem;
	}
	
	public void realizarSelecaoCheckboxMarcarDesmarcarTodosTipoOrigem() {
		if (getMarcarTodosTipoOrigem()) {
			realizarSelecaoTodasOrigens(true);
		} else {
			realizarSelecaoTodasOrigens(false);
		}
	}
	
	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodosTipoOrigem() {
		if (getMarcarTodosTipoOrigem()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}
	
	public void realizarSelecaoTodasOrigens(boolean selecionado){
		getFiltroRelatorioFinanceiroVO().setTipoOrigemBiblioteca(selecionado);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemBolsaCusteadaConvenio(selecionado);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemContratoReceita(selecionado);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemDevolucaoCheque(selecionado);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemInclusaoReposicao(selecionado);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemInscricaoProcessoSeletivo(selecionado);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemMaterialDidatico(selecionado);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemMatricula(selecionado);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemMensalidade(selecionado);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemNegociacao(selecionado);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemOutros(selecionado);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemRequerimento(selecionado);
		
	}

}
