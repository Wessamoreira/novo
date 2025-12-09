package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;
import negocio.comuns.academico.DisciplinaVO;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;

import negocio.comuns.utilitarias.dominios.TipoPessoa;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.ProfessoresAnaliticoPorUnidadeCursoTurmaRelVO;
import relatorio.negocio.comuns.academico.ProfessoresPorUnidadeCursoTurmaRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.ProfessorAnaliticoRel;
import relatorio.negocio.jdbc.academico.ProfessorRel;

@SuppressWarnings("unchecked")
@Controller("ProfessorRelControle")
@Scope("viewScope")
@Lazy
public class ProfessorRelControle extends SuperControleRelatorio {

	private PessoaVO professorVO;
	private String ordemRelatorio;
	private AreaConhecimentoVO areaConhecimentoVO;
	private ProfessorRel professorRel;
	private ProfessorAnaliticoRel professorAnaliticoRel;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private TurmaVO turmaVO;
	private DisciplinaVO disciplinaVO;
	private DisciplinaVO disciplinaInteresseVO;
	private String ano;
	private String semestre;
	private List listaModelosRelatorio;
	private List listaOrdemRelatorio;
	private List listaSelectItemUnidadeEnsino;
	private List listaSelectItemCurso;
	private List listaSelectItemTurma;
	private List listaSelectItemAreaConhecimento;
	private Integer modeloRel;
	private Boolean trazerSomenteAlunosAtivos;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List listaConsultaTurma;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private List listaConsultaDisciplina;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List listaConsultaCurso;
	private String situacaoProfessor;
	private String escolaridade;
	private String campoConsultaProfessor;
	private String valorConsultaProfessor;
	private List listaConsultaProfessor;

	@SuppressWarnings("OverridableMethodCallInConstructor")
	public ProfessorRelControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		incializarDados();
		setMensagemID("msg_entre_prmconsulta");
	}

	public void incializarDados() {
		montarListaSelectItemModeloRelatorio();
		montarListaSelectItemOrdemRelatorio();
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemAreaConhecimento();
	}

	public void limparListasConsultas() {
		setTurmaVO(null);
		setProfessorVO(null);
		setUnidadeEnsinoCursoVO(null);
		setDisciplinaVO(null);
		getListaConsultaCurso().clear();
		getListaConsultaTurma().clear();
		getListaConsultaDisciplina().clear();
	}

	public void imprimirPDFModeloSintetico() throws Exception {
		String design = null;
		List listaObjetos = null;
		getProfessorRel().setDescricaoFiltros("");
		design = ProfessorRel.getDesignIReportRelatorio();
		listaObjetos = getProfessorRel().criarObjeto(getUnidadeEnsinoVO(), getUnidadeEnsinoCursoVO().getCurso(), getTurmaVO(), getDisciplinaVO(), getSemestre(), getAno(), getSituacaoProfessor(), getEscolaridade(), getUsuarioLogado());
		if (!listaObjetos.isEmpty()) {
			getSuperParametroRelVO().setNomeDesignIreport(design);
			// getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(ProfessorRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio("Relação de Professores");
			getSuperParametroRelVO().setListaObjetos(listaObjetos);
			getSuperParametroRelVO().setCaminhoBaseRelatorio(ProfessorRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeEmpresa("");
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setFiltros(getProfessorRel().getDescricaoFiltros());
			getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
			getSuperParametroRelVO().setQuantidade(inicializarDadosQtdeProfessores(listaObjetos));
			if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
				setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
			}
			realizarImpressaoRelatorio();
			removerObjetoMemoria(this);
			incializarDados();
			setMensagemID("msg_relatorio_ok");
		} else {
			setMensagemID("msg_relatorio_sem_dados");
		}
		registrarAtividadeUsuario(getUsuarioLogado(), "ProfessorRelControle", "Finalizando Geração de Relatório Professor", "Emitindo Relatório");
	}

	public void imprimirPDFModeloAnalitico() throws Exception {
		String design = null;
		List listaObjetos = null;
		getProfessorRel().setDescricaoFiltros("");
		Boolean imprimirDadosComplementares = false;
		if (getModeloRel().equals(3)) {
			imprimirDadosComplementares = true;
			design = ProfessorAnaliticoRel.getDesignIReportRelatorioModelo3();
		} else {
			design = ProfessorAnaliticoRel.getDesignIReportRelatorio();
		}
		listaObjetos = getProfessorAnaliticoRel().criarObjeto(getAreaConhecimentoVO(), getDisciplinaInteresseVO(), getSituacaoProfessor(), getEscolaridade(), getProfessorVO(), getUsuarioLogado(), getOrdemRelatorio(), imprimirDadosComplementares);
		if (!listaObjetos.isEmpty()) {
			getSuperParametroRelVO().setNomeDesignIreport(design);
			// getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(ProfessorRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio("Relação de Professores - Interesse Acadêmico");
			getSuperParametroRelVO().setListaObjetos(listaObjetos);
			getSuperParametroRelVO().setCaminhoBaseRelatorio(ProfessorRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeEmpresa("");
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setFiltros(getProfessorRel().getDescricaoFiltros());
			getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
			getSuperParametroRelVO().setQuantidade(inicializarDadosQtdeProfessoresAnalitico(listaObjetos));

			realizarImpressaoRelatorio();
			removerObjetoMemoria(this);
			incializarDados();
			setMensagemID("msg_relatorio_ok");
		} else {
			setMensagemID("msg_relatorio_sem_dados");
		}
		registrarAtividadeUsuario(getUsuarioLogado(), "ProfessorRelControle", "Finalizando Geração de Relatório Professor - Analítico", "Emitindo Relatório");
	}

	public void imprimirPDFNomeAssinatura() throws Exception {
		String design = null;
		List listaObjetos = null;
		getProfessorRel().setDescricaoFiltros("");
		design = ProfessorRel.getDesignIReportRelatorioNomeAssinatura();
		listaObjetos = getProfessorRel().criarObjeto(getUnidadeEnsinoVO(), getUnidadeEnsinoCursoVO().getCurso(), getTurmaVO(), getDisciplinaVO(), getSemestre(), getAno(), getSituacaoProfessor(), getEscolaridade(), getUsuarioLogado());
		if (!listaObjetos.isEmpty()) {
			getSuperParametroRelVO().setNomeDesignIreport(design);
			getSuperParametroRelVO().setSubReport_Dir(ProfessorRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio("Relação de Professores - Interesse Acadêmico");
			getSuperParametroRelVO().setListaObjetos(listaObjetos);
			getSuperParametroRelVO().setCaminhoBaseRelatorio(ProfessorRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeEmpresa("");
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setFiltros(getProfessorRel().getDescricaoFiltros());
			getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
			getSuperParametroRelVO().setQuantidade((Integer) inicializarDadosQtdeProfessores(listaObjetos));
			realizarImpressaoRelatorio();
			removerObjetoMemoria(this);
			incializarDados();
			setMensagemID("msg_relatorio_ok");
		} else {
			setMensagemID("msg_relatorio_sem_dados");
		}
		registrarAtividadeUsuario(getUsuarioLogado(), "ProfessorRelControle", "Finalizando Geração de Relatório Professor - Analítico", "Emitindo Relatório");
	}

	public void imprimirPDF() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ProfessorRelControle", "Inicializando Geração de Relatório Professor", "Emitindo Relatório");
			if (getModeloRel().equals(1)) {
				imprimirPDFModeloSintetico();
			} else if (getModeloRel().equals(2) || getModeloRel().equals(3)) {
				imprimirPDFModeloAnalitico();
			} else {
				imprimirPDFNomeAssinatura();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Integer inicializarDadosQtdeProfessoresAnalitico(List<ProfessoresAnaliticoPorUnidadeCursoTurmaRelVO> listaObjetos) {
		for (ProfessoresAnaliticoPorUnidadeCursoTurmaRelVO obj : listaObjetos) {
			return obj.getQtdeProfessores();
		}
		return 0;
	}

	public Integer inicializarDadosQtdeProfessores(List<ProfessoresPorUnidadeCursoTurmaRelVO> listaObjetos) {
		for (ProfessoresPorUnidadeCursoTurmaRelVO obj : listaObjetos) {
			return obj.getQtdeProfessores();
		}
		return 0;
	}

	@SuppressWarnings("static-access")
	public void imprimirPDFSintetico() {
		String design = null;
		List listaObjetos = null;
		try {
			professorRel.validarDados(getUnidadeEnsinoVO(), getUnidadeEnsinoCursoVO(), getDisciplinaVO());
			getProfessorRel().setDescricaoFiltros("");
			design = professorRel.getDesignIReportRelatorioSintetico();
			listaObjetos = getProfessorRel().criarObjeto(getUnidadeEnsinoVO(), getUnidadeEnsinoCursoVO().getCurso(), getTurmaVO(), getDisciplinaVO(), getSemestre(), getAno(), getSituacaoProfessor(), getEscolaridade(), getUsuarioLogado());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(professorRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relação de Alunos Sintético");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(professorRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros(getProfessorRel().getDescricaoFiltros());
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
				// getSuperParametroRelVO().setQuantidade(inicializarDadosQtdeAlunos(listaObjetos));
				realizarImpressaoRelatorio();
				removerObjetoMemoria(this);
				incializarDados();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			design = null;
		}
	}

	public void montarListaSelectItemModeloRelatorio() {
		try {
			List<SelectItem> listaDeSelectItem = new ArrayList<SelectItem>();
			listaDeSelectItem.add(new SelectItem(1, "Modelo 1 - Professores com Aulas Programadas"));
			listaDeSelectItem.add(new SelectItem(2, "Modelo 2 - Professores com Área Conhecimento e Disciplinas Interesse"));
			listaDeSelectItem.add(new SelectItem(3, "Modelo 3 - Professores com Dados Complementares, Área Conhecimento e Disciplinas Interesse"));
			listaDeSelectItem.add(new SelectItem(4, "Modelo 4 - Nome professor e Assinatura"));
			setListaModelosRelatorio(listaDeSelectItem);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemOrdemRelatorio() {
		try {
			List<SelectItem> listaDeSelectItem = new ArrayList<SelectItem>();
			listaDeSelectItem.add(new SelectItem("Nome Professor", "Nome Professor"));
			listaDeSelectItem.add(new SelectItem("Área Conhecimento", "Área Conhecimento"));
			listaDeSelectItem.add(new SelectItem("Disciplina Interesse", "Disciplina Interesse"));
			setListaOrdemRelatorio(listaDeSelectItem);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
			getListaSelectItemUnidadeEnsino();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemAreaConhecimento() {
		try {
			List<AreaConhecimentoVO> resultadoConsulta = getFacadeFactory().getAreaConhecimentoFacade().consultarPorNome("", false, getUsuarioLogado());
			setListaSelectItemAreaConhecimento(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
			getListaSelectItemAreaConhecimento();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemCurso() throws Exception {
		List<UnidadeEnsinoCursoVO> resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarCursoPorUnidadeEnsino(getUnidadeEnsinoVO().getCodigo());
			setListaSelectItemTurma(new ArrayList(0));
			setListaSelectItemCurso(new ArrayList(0));
			i = resultadoConsulta.iterator();
			getListaSelectItemCurso().add(new SelectItem(0, ""));
			while (i.hasNext()) {
				UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) i.next();
				getListaSelectItemCurso().add(new SelectItem(unidadeEnsinoCurso.getCodigo(), unidadeEnsinoCurso.getNomeCursoTurno()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	private List<UnidadeEnsinoCursoVO> consultarCursoPorUnidadeEnsino(Integer codigoUnidadeEnsino) throws Exception {
		List<UnidadeEnsinoCursoVO> lista = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoUnidadeEnsino(codigoUnidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		return lista;
	}

	public void selecinarCurso() {
		try {
			UnidadeEnsinoCursoVO unidade = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			setUnidadeEnsinoCursoVO(unidade);
			montarListaSelectItemTurma();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemTurma() throws Exception {
		getFacadeFactory().getUnidadeEnsinoCursoFacade().carregarDados(getUnidadeEnsinoCursoVO(), getUsuarioLogado());
		List<TurmaVO> resultadoConsulta = consultarTurmasPorUnidadeEnsinoCurso(getUnidadeEnsinoCursoVO().getCodigo());
		setListaSelectItemTurma(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "identificadorTurma"));
	}

	private List<TurmaVO> consultarTurmasPorUnidadeEnsinoCurso(Integer codigoUnidadeEnsinoCurso) throws Exception {
		List<TurmaVO> lista = getFacadeFactory().getTurmaFacade().consultarPorCodigoUnidadeEnsinoCurso(codigoUnidadeEnsinoCurso, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		return lista;
	}

	public List getListaSelectItemSemestre() {
		List lista = new ArrayList(0);
		lista.add(new SelectItem("", ""));
		lista.add(new SelectItem("1", "1º"));
		lista.add(new SelectItem("2", "2º"));
		return lista;
	}

	public void consultarTurma() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurmaPorChavePrimaria() {
		try {
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getTurmaVO(), getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			if (getTurmaVO().getCodigo() == 0) {
				setTurmaVO(null);
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setTurmaVO(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurmaVO(obj);
			getUnidadeEnsinoCursoVO().setCurso(obj.getCurso());
			limparDisciplina();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparProfessor() {
		try {
			setProfessorVO(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurma() {
		try {
			setTurmaVO(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaComboTurma() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		return itens;
	}

	public void consultarCurso() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocursoItens");
			setUnidadeEnsinoCursoVO(obj);
			limparTurma();
			limparDisciplina();
		} catch (Exception e) {
		}
	}

	public void limparCurso() throws Exception {
		try {
			setUnidadeEnsinoCursoVO(null);
		} catch (Exception e) {
		}
	}

	public List getTipoConsultaComboCurso() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void consultarDisciplina() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaDisciplina().equals("codigo")) {
				if (getValorConsultaDisciplina().equals("")) {
					setValorConsultaDisciplina("0");
				}
				if (getValorConsultaDisciplina().trim() != null || !getValorConsultaDisciplina().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getValorConsultaDisciplina().trim());
				}
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigoCursoTurma(new Integer(valorInt), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeCursoTurma(getValorConsultaDisciplina(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarDisciplina() throws Exception {
		DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
		setDisciplinaVO(obj);
		obj = null;
		setValorConsultaDisciplina("");
		setCampoConsultaDisciplina("");
		getListaConsultaDisciplina().clear();
	}

	public void selecionarDisciplinaInteresse() throws Exception {
		DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
		setDisciplinaInteresseVO(obj);
		obj = null;
		setValorConsultaDisciplina("");
		setCampoConsultaDisciplina("");
		getListaConsultaDisciplina().clear();
	}

	public List getTipoConsultaComboDisciplina() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void limparDisciplina() throws Exception {
		try {
			setDisciplinaVO(null);
		} catch (Exception e) {
		}
	}

	public List getComboBoxTipoProfessor() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("", "TODOS"));
		itens.add(new SelectItem("AT", "Ativo"));
		itens.add(new SelectItem("IN", "Inativo"));
		return itens;
	}

	public List getListaSelectItemEscolaridadeFormacaoAcademica() throws Exception {
		// List objs = new ArrayList(0);
		// objs.add(new SelectItem("", "TODOS"));
		// Hashtable escolaridadeFormacaoAcademicas = (Hashtable)
		// Dominios.getEscolaridadeFormacaoAcademica();
		// Enumeration keys = escolaridadeFormacaoAcademicas.keys();
		// while (keys.hasMoreElements()) {
		// String value = (String) keys.nextElement();
		// String label = (String) escolaridadeFormacaoAcademicas.get(value);
		// objs.add(new SelectItem(value, label));
		// }
		// SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		// Collections.sort((List) objs, ordenador);
		// return objs;

		List objs = UtilPropriedadesDoEnum.getListaSelectItemDoEnumTodos(NivelFormacaoAcademica.class, true);
		return objs;
	}

	public List getListaTipoRelPos() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("normal", "Normal"));
		itens.add(new SelectItem("reposicao", "Reposição/Inclusão"));
		return itens;
	}

	public ProfessorRel getProfessorRel() {
		if (professorRel == null) {
			professorRel = new ProfessorRel();
		}
		return professorRel;
	}

	public void setProfessorRel(ProfessorRel professorRel) {
		this.professorRel = professorRel;
	}

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List getListaSelectItemCurso() {
		if (listaSelectItemCurso == null) {
			listaSelectItemCurso = new ArrayList(0);
		}
		return listaSelectItemCurso;
	}

	public void setListaSelectItemCurso(List listaSelectItemCurso) {
		this.listaSelectItemCurso = listaSelectItemCurso;
	}

	public List getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList(0);
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
		if (unidadeEnsinoCursoVO == null) {
			unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCursoVO;
	}

	public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
		this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
	}

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public void setTrazerSomenteAlunosAtivos(Boolean trazerSomenteAlunosAtivos) {
		this.trazerSomenteAlunosAtivos = trazerSomenteAlunosAtivos;
	}

	public Boolean getTrazerSomenteAlunosAtivos() {
		if (trazerSomenteAlunosAtivos == null) {
			trazerSomenteAlunosAtivos = true;
		}
		return trazerSomenteAlunosAtivos;
	}

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
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

	public List getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public String getSituacaoProfessor() {
		if (situacaoProfessor == null) {
			situacaoProfessor = "";
		}
		return situacaoProfessor;
	}

	public void setSituacaoProfessor(String situacaoProfessor) {
		this.situacaoProfessor = situacaoProfessor;
	}

	public String getCampoConsultaDisciplina() {
		if (campoConsultaDisciplina == null) {
			campoConsultaDisciplina = "";
		}
		return campoConsultaDisciplina;
	}

	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public List getListaConsultaDisciplina() {
		if (listaConsultaDisciplina == null) {
			listaConsultaDisciplina = new ArrayList(0);
		}
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(List listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
	}

	public String getValorConsultaDisciplina() {
		if (valorConsultaDisciplina == null) {
			valorConsultaDisciplina = "";
		}
		return valorConsultaDisciplina;
	}

	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}

	/**
	 * @return the escolaridade
	 */
	public String getEscolaridade() {
		if (escolaridade == null) {
			escolaridade = "";
		}
		return escolaridade;
	}

	/**
	 * @param escolaridade
	 *            the escolaridade to set
	 */
	public void setEscolaridade(String escolaridade) {
		this.escolaridade = escolaridade;
	}

	/**
	 * @return the modeloRel
	 */
	public Integer getModeloRel() {
		if (modeloRel == null) {
			modeloRel = 1;
		}
		return modeloRel;
	}

	/**
	 * @param modeloRel
	 *            the modeloRel to set
	 */
	public void setModeloRel(Integer modeloRel) {
		this.modeloRel = modeloRel;
	}

	/**
	 * @return the listaModelosRelatorio
	 */
	public List getListaModelosRelatorio() {
		if (listaModelosRelatorio == null) {
			listaModelosRelatorio = new ArrayList(0);
		}
		return listaModelosRelatorio;
	}

	/**
	 * @param listaModelosRelatorio
	 *            the listaModelosRelatorio to set
	 */
	public void setListaModelosRelatorio(List listaModelosRelatorio) {
		this.listaModelosRelatorio = listaModelosRelatorio;
	}

	/**
	 * @return the campoConsultaProfessor
	 */
	public String getCampoConsultaProfessor() {
		if (campoConsultaProfessor == null) {
			campoConsultaProfessor = "";
		}
		return campoConsultaProfessor;
	}

	/**
	 * @param campoConsultaProfessor
	 *            the campoConsultaProfessor to set
	 */
	public void setCampoConsultaProfessor(String campoConsultaProfessor) {
		this.campoConsultaProfessor = campoConsultaProfessor;
	}

	public List getTipoConsultaComboProfessor() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("cpf", "CPF"));
		return itens;
	}

	public Boolean getApresentarCampoCpf() {
		if (getCampoConsultaProfessor().equals("cpf")) {
			return true;
		}
		return false;
	}

	/**
	 * @return the valorConsultaProfessor
	 */
	public String getValorConsultaProfessor() {
		if (valorConsultaProfessor == null) {
			valorConsultaProfessor = "";
		}
		return valorConsultaProfessor;
	}

	/**
	 * @param valorConsultaProfessor
	 *            the valorConsultaProfessor to set
	 */
	public void setValorConsultaProfessor(String valorConsultaProfessor) {
		this.valorConsultaProfessor = valorConsultaProfessor;
	}

	/**
	 * @param listaConsultaProfessor
	 *            the listaConsultaProfessor to set
	 */
	public void setListaConsultaProfessor(List listaConsultaProfessor) {
		this.listaConsultaProfessor = listaConsultaProfessor;
	}

	/**
	 * @return the listaConsultaProfessor
	 */
	public List getListaConsultaProfessor() {
		if (listaConsultaProfessor == null) {
			listaConsultaProfessor = new ArrayList(0);
		}
		return listaConsultaProfessor;
	}

	public Boolean getIsApresentarBuscaProfessor() {
		if ((getModeloRel().equals(2)) || (getModeloRel().equals(3))) {
			return true;
		}
		return false;
	}

	public void selecionarProfessor() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("professorItens");
			setProfessorVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarProfessor() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaProfessor().equals("nome")) {
				if (getValorConsultaProfessor().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaProfessor(), TipoPessoa.PROFESSOR.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, getUsuarioLogado());
			}
			if (getCampoConsultaProfessor().equals("cpf")) {
				if (getValorConsultaProfessor().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaProfessor(), TipoPessoa.PROFESSOR.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, getUsuarioLogado());
			}
			setListaConsultaProfessor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * @return the professorVO
	 */
	public PessoaVO getProfessorVO() {
		if (professorVO == null) {
			professorVO = new PessoaVO();
		}
		return professorVO;
	}

	/**
	 * @param professorVO
	 *            the professorVO to set
	 */
	public void setProfessorVO(PessoaVO professorVO) {
		this.professorVO = professorVO;
	}

	/**
	 * @return the professorAnaliticoRel
	 */
	public ProfessorAnaliticoRel getProfessorAnaliticoRel() {
		if (professorAnaliticoRel == null) {
			professorAnaliticoRel = new ProfessorAnaliticoRel();
		}
		return professorAnaliticoRel;
	}

	/**
	 * @param professorAnaliticoRel
	 *            the professorAnaliticoRel to set
	 */
	public void setProfessorAnaliticoRel(ProfessorAnaliticoRel professorAnaliticoRel) {
		this.professorAnaliticoRel = professorAnaliticoRel;
	}

	/**
	 * @return the areaConhecimentoVO
	 */
	public AreaConhecimentoVO getAreaConhecimentoVO() {
		if (areaConhecimentoVO == null) {
			areaConhecimentoVO = new AreaConhecimentoVO();
		}
		return areaConhecimentoVO;
	}

	/**
	 * @param areaConhecimentoVO
	 *            the areaConhecimentoVO to set
	 */
	public void setAreaConhecimentoVO(AreaConhecimentoVO areaConhecimentoVO) {
		this.areaConhecimentoVO = areaConhecimentoVO;
	}

	/**
	 * @return the disciplinaInteresseVO
	 */
	public DisciplinaVO getDisciplinaInteresseVO() {
		if (disciplinaInteresseVO == null) {
			disciplinaInteresseVO = new DisciplinaVO();
		}
		return disciplinaInteresseVO;
	}

	/**
	 * @param disciplinaInteresseVO
	 *            the disciplinaInteresseVO to set
	 */
	public void setDisciplinaInteresseVO(DisciplinaVO disciplinaInteresseVO) {
		this.disciplinaInteresseVO = disciplinaInteresseVO;
	}

	/**
	 * @return the listaSelectItemAreaConhecimento
	 */
	public List getListaSelectItemAreaConhecimento() {
		if (listaSelectItemAreaConhecimento == null) {
			listaSelectItemAreaConhecimento = new ArrayList(0);
		}
		return listaSelectItemAreaConhecimento;
	}

	/**
	 * @param listaSelectItemAreaConhecimento
	 *            the listaSelectItemAreaConhecimento to set
	 */
	public void setListaSelectItemAreaConhecimento(List listaSelectItemAreaConhecimento) {
		this.listaSelectItemAreaConhecimento = listaSelectItemAreaConhecimento;
	}

	/**
	 * @return the ordemRelatorio
	 */
	public String getOrdemRelatorio() {
		if (ordemRelatorio == null) {
			ordemRelatorio = "Nome Professor";
		}
		return ordemRelatorio;
	}

	/**
	 * @param ordemRelatorio
	 *            the ordemRelatorio to set
	 */
	public void setOrdemRelatorio(String ordemRelatorio) {
		this.ordemRelatorio = ordemRelatorio;
	}

	/**
	 * @return the listaOrdemRelatorio
	 */
	public List getListaOrdemRelatorio() {
		if (listaOrdemRelatorio == null) {
			listaOrdemRelatorio = new ArrayList(0);
		}
		return listaOrdemRelatorio;
	}

	/**
	 * @param listaOrdemRelatorio
	 *            the listaOrdemRelatorio to set
	 */
	public void setListaOrdemRelatorio(List listaOrdemRelatorio) {
		this.listaOrdemRelatorio = listaOrdemRelatorio;
	}

}
