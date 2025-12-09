package relatorio.controle.avaliacaoInst;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jakarta.faces. model.SelectItem;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CursoCoordenadorVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalCursoVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.processosel.PerguntaVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.PublicoAlvoAvaliacaoInstitucional;
import relatorio.arquitetura.GeradorRelatorio;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.avaliacaoInst.AvaliacaoInstitucionalAnaliticoRelVO;
import relatorio.negocio.comuns.avaliacaoInst.AvaliacaoInstucionalRelVO;
import relatorio.negocio.comuns.avaliacaoInst.PerguntaRelVO;
import relatorio.negocio.comuns.avaliacaoInst.QuestionarioRelVO;
import relatorio.negocio.comuns.avaliacaoInst.enumeradores.NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum;
import relatorio.negocio.jdbc.avaliacaoInst.AvaliacaoInstitucionalRel;

@Controller("AvaliacaoInstitucionalRelControle")
@Scope("viewScope")
public class AvaliacaoInstitucionalRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private AvaliacaoInstucionalRelVO avaliacaoInstucionalRelVO;
	private AvaliacaoInstitucionalVO avaliacao;	
	private Integer questionario;
	private List<SelectItem> listaQuestionarios;
	private List<PerguntaVO> listaPerguntas;
	private List<PerguntaVO> listaPerguntasSelecionadas;
	private List<DisciplinaVO> listaDisciplinas;
	private List<DisciplinaVO> listaDisciplinasSelecionadas;
	private Integer codigoUnidadeEnsino;
	private List<SelectItem> listaUnidadesEnsino;
	private Integer turno;
	private List<SelectItem> listaTurnos;
	private Integer turma;
	private String identificadorTurma;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;	
	private Integer curso;
	private String nomeCurso;
	private String nomeDisciplina;
	private Boolean filtrarQuestionario;
	private Boolean filtrarPergunta;
	private Boolean filtrarUnidadeEnsino;
	private Boolean filtrarCurso;
	private Boolean filtrarTurno;	
	private Boolean filtrarTurma;
	private Boolean filtrarDisciplina;
	private Boolean filtrarProfessor;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<CursoVO> listaConsultaCurso;
	private String campoConsultaPergunta;
	private String valorConsultaPergunta;
	private List<PerguntaVO> listaConsultaPergunta;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private List<DisciplinaVO> listaConsultaDisciplina;
	private List<PessoaVO> listaConsultaProfessor;
	private List<PessoaVO> listaConsultaProfessorSelecionados;
	private QuestionarioRelVO questionarioRelVO;
	private Integer posicao;
	private PerguntaRelVO perguntaRelVO;
	private Boolean abrirFiltroConsulta;
	private Boolean apresentarFiltroProfessor;	
	
	private Boolean apresentarFiltroRespondente;	
	private List<PessoaVO> listaConsultaRespondente;
	private List<PessoaVO> listaConsultaRespondenteSelecionados;

	private String campoConsultaAvaliacao;
	private String valorConsultaAvaliacao;
	
	private String nomeAvaliacao;
	private Date dataInicio;
	private Date dataFim;
	private Boolean selecionarTudo;
	private ProgressBarVO progressBarVO;
	private String tipoRelatorioGerar;
	private List<QuestionarioRelVO> questionarioRelVOs;	
	private Boolean gerarRelatorioQuestionarioSelecionado;
	private QuestionarioRelVO questionarioRelSelecionadoVO;
	private boolean apresentarRespostaPorAgrupamento;
	private NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento;
	private List<SelectItem> listaSelectItemNivelDetalhamento;
	private Boolean gerarRelatorioPublicacao;
	private Boolean utilizarListagemRespondente;
	private Integer totalResposta;


	public AvaliacaoInstitucionalRelControle() {
		try {	
			inicializarDados();
			setMensagemID("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void inicializarDadosPublicacao() throws Exception {
		if (getNomeTelaAtual().endsWith("avaliacaoInstitucionalPublicacaoRel.xhtml")) {
			carregarAvaliacaoPublicada();			
		}
	}
	
	public void inicializarDados() throws Exception {		
		
		setQuestionario(0);
		setListaQuestionarios(new ArrayList<SelectItem>(0));
		setListaPerguntas(new ArrayList<PerguntaVO>(0));
		setListaPerguntasSelecionadas(new ArrayList<PerguntaVO>(0));
		setListaDisciplinas(new ArrayList<DisciplinaVO>(0));
		setListaDisciplinasSelecionadas(new ArrayList<DisciplinaVO>(0));
		setCodigoUnidadeEnsino(0);
		setListaUnidadesEnsino(new ArrayList<SelectItem>(0));
		setTurno(0);
		setListaTurnos(new ArrayList<SelectItem>(0));
		setTurma(0);		
		montarListaUnidadesEnsino();
		montarListaTurnos();
		setPosicao(0);
		setQuestionarioRelVO(new QuestionarioRelVO());
		setCurso(0);
		setNomeCurso("");
		setNomeDisciplina("");
		setFiltrarCurso(false);
		setFiltrarDisciplina(false);
		setFiltrarPergunta(false);
		setFiltrarQuestionario(false);
		setFiltrarTurma(false);
		setFiltrarTurno(false);
		setFiltrarUnidadeEnsino(false);
		setAvaliacaoInstucionalRelVO(new AvaliacaoInstucionalRelVO());
		setCampoConsultaDisciplina("");
		setValorConsultaDisciplina("");
		setListaConsultaDisciplina(new ArrayList<DisciplinaVO>(0));
		setCampoConsultaPergunta("");
		setValorConsultaPergunta("");
		setListaConsultaPergunta(new ArrayList<PerguntaVO>(0));
		setCampoConsultaCurso("");
		setValorConsultaCurso("");
		setListaConsultaCurso(new ArrayList<CursoVO>(0));		
		setPerguntaRelVO(new PerguntaRelVO());
		setDataInicio(Uteis.getDataPrimeiroDiaMes(new Date()));
		setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
		setNivelDetalhamento(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.AVALIADO);
		setMensagemID("");
	}

	public void montarListaQuestionarios() throws Exception {
		limparQuestionario();
		limparCurso();
		limparProfessor();
		List<QuestionarioVO> resultadoConsulta = getFacadeFactory().getQuestionarioFacade().consultarPorCodigoAvaliacao(getAvaliacao().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		for (QuestionarioVO obj : resultadoConsulta) {
			objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao().toString()));
		}
		setListaQuestionarios(objs);
	}

	public void montarListaUnidadesEnsino() throws Exception {
		Integer unidadeEnsino = Uteis.isAtributoPreenchido(getUnidadeEnsinoLogado().getCodigo()) ? getUnidadeEnsinoLogado().getCodigo() : getAvaliacao().getUnidadeEnsino().getCodigo();
		List<UnidadeEnsinoVO> resultadoConsulta = new ArrayList<>();
		if(avaliacaoInstucionalRelVO != null) {
			resultadoConsulta = getFacadeFactory().getAvaliacaoInstitucionalRelFacade().consultarUnidadeEnsinoComboBoxPorAvaliacaoInstitucionalPorQuestionario(getAvaliacao().getCodigo(), getQuestionario(), false, getUsuarioLogado());
		}else {
			resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(unidadeEnsino, false, getUsuarioLogado());
		}
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		if (getUnidadeEnsinoLogado().getCodigo().intValue() == 0 && resultadoConsulta.size() > 1) {
			objs.add(new SelectItem(0, "TODAS"));
		}
		for (UnidadeEnsinoVO obj : resultadoConsulta) {
			objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
		}
		setListaUnidadesEnsino(objs);
	}

	public void montarListaTurnos() throws Exception {
		List<TurnoVO> resultadoConsulta = getFacadeFactory().getTurnoFacade().consultarPorCodigo(0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		Iterator<TurnoVO> i = resultadoConsulta.iterator();
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem(0, "TODOS"));
		while (i.hasNext()) {
			TurnoVO obj = (TurnoVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
		}
		setListaTurnos(objs);
	}
		

	private List<SelectItem> tipoConsultaComboCurso;
	public List<SelectItem> getTipoConsultaComboCurso() {
		if(tipoConsultaComboCurso == null){
		tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
		tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboCurso;
	}

	private List<SelectItem> tipoConsultaComboAvaliacao;
	public List<SelectItem> getTipoConsultaComboAvaliacao() {
		if(tipoConsultaComboAvaliacao == null){
			tipoConsultaComboAvaliacao = new ArrayList<SelectItem>(0);
			tipoConsultaComboAvaliacao.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboAvaliacao;
	}

	public void consultarPergunta() {
		try {
			if (getQuestionario() == 0) {
				return;
			}
			if(getListaConsultaPergunta().isEmpty()){
				List<PerguntaVO> objs = new ArrayList<PerguntaVO>(0);
				objs = getFacadeFactory().getPerguntaFacade().consultarPorCodigoQuestionario(getQuestionario(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
				setListaConsultaPergunta(objs);
				getListaPerguntasSelecionadas().clear();
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaPergunta(new ArrayList<PerguntaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarProfessores() {
		try {
			List<PessoaVO> professores = getFacadeFactory().getAvaliacaoInstitucionalRelFacade().consultarProfessor(getAvaliacao().getCodigo(), questionario, getListaPerguntasSelecionadas(), codigoUnidadeEnsino, curso, turno, turma, getListaDisciplinasSelecionadas());
			if(!getListaConsultaProfessorSelecionados().isEmpty() && !getListaConsultaProfessor().isEmpty()){
				getListaConsultaProfessorSelecionados().clear();
				for (PessoaVO pessoaVO : getListaConsultaProfessor()) {
					if (pessoaVO.getSelecionado() && professores.contains(pessoaVO)) {
						pessoaVO = professores.get(professores.indexOf(pessoaVO));
						pessoaVO.setSelecionado(true);
						getListaConsultaProfessorSelecionados().add(pessoaVO);
					}
				}
			}
			setListaConsultaProfessor(professores);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaProfessor(new ArrayList<PessoaVO>(0));

			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}	

	public void consultarDisciplina() {
		try {
			super.consultar();
			List<DisciplinaVO> disciplinaVOs = (getFacadeFactory().getAvaliacaoInstitucionalRelFacade().consultarDisciplinas(getAvaliacao().getCodigo(), getQuestionario(), getCodigoUnidadeEnsino(), getCurso(), getTurno(), getTurma()));
			if (!getListaDisciplinasSelecionadas().isEmpty() && !getListaDisciplinas().isEmpty()) {
				for (DisciplinaVO disciplinaVO : getListaConsultaDisciplina()) {
					if (disciplinaVO.getSelecionado() && disciplinaVOs.contains(disciplinaVO)) {
						disciplinaVO = disciplinaVOs.get(disciplinaVOs.indexOf(disciplinaVO));
						disciplinaVO.setSelecionado(true);
						getListaDisciplinasSelecionadas().add(disciplinaVO);
					}
				}
			}
			setListaConsultaDisciplina(disciplinaVOs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<DisciplinaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void atualizarTurnoECurso() throws Exception {
		montarListaTurnos();
		setCurso(0);
		setNomeCurso("");
	}

	public void consultarTurma() {
		try {
			setListaConsultaTurma(getFacadeFactory().getAvaliacaoInstitucionalRelFacade().consultarTurmas(getCampoConsultaTurma(), getValorConsultaTurma(), getAvaliacao().getCodigo(), getQuestionario(), getCodigoUnidadeEnsino(), getCurso(), getTurno()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void selecionarTurma() throws Exception {
		try {
			TurmaVO turma = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			TurmaVO turmaVO = new TurmaVO();
			turmaVO = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(turma.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setTurma(turmaVO.getCodigo());
			setIdentificadorTurma(turmaVO.getIdentificadorTurma());
			CursoVO curso = new CursoVO();
			curso = getFacadeFactory().getCursoFacade().consultarCursoPorTurma(turma.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setCurso(curso.getCodigo());
			setNomeCurso(curso.getNome());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurma() {
		try {
			setIdentificadorTurma("");
			setTurma(0);
			setCurso(0);
			setNomeCurso("");
		} catch (Exception e) {
		}
	}

	public void consultarCurso() {
		try {					
			getListaConsultaCurso().clear();
			if (getCampoConsultaCurso().equals("nome")) {
				setListaConsultaCurso(getFacadeFactory().getAvaliacaoInstitucionalRelFacade().consultarCursosComboBoxPorAvaliacaoInstitucionalPorQuestionario("nome", getValorConsultaCurso(), getAvaliacao().getCodigo(), getQuestionario(), getCodigoUnidadeEnsino()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAvaliacao() {
		try {
			getControleConsulta().getListaConsulta().clear();
			List<AvaliacaoInstitucionalVO> objs = new ArrayList<AvaliacaoInstitucionalVO>(0);
			if (getCampoConsultaAvaliacao().equals("nome")) {
				if (!getValorConsultaAvaliacao().equals("")) {
					objs = getFacadeFactory().getAvaliacaoInstitucionalFacade().consultarPorNomeAtivosFinalizados(getValorConsultaAvaliacao(), null, null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				} else {
					throw new Exception(UteisJSF.internacionalizar("msg_entre_prmconsulta"));
				}
			}
			getControleConsulta().setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void scrollerListener() throws Exception {
		
		
		consultarAvaliacao();
	}

	public void limparQuestionario() {
		limparProfessor();		
		limparPerguta();

	}

	public void selecionarPergunta() throws Exception {
		try {
			PerguntaVO obj = (PerguntaVO) context().getExternalContext().getRequestMap().get("perguntaItens");
			if (obj.getSelecionado()) {
				getListaPerguntasSelecionadas().add(obj);
			} else {
				getListaPerguntasSelecionadas().remove(obj);
			}
			if(!getListaConsultaProfessorSelecionados().isEmpty()){
				consultarProfessores();
			}
			if(!getListaConsultaRespondenteSelecionados().isEmpty()){
				consultarRespondentes();
			}
			obj = null;
			this.setValorConsultaPergunta(null);
			this.setCampoConsultaPergunta(null);
		} catch (Exception e) {
		}
	}

	public void limparPerguta() {
		for(PerguntaVO perguntaVO: getListaPerguntasSelecionadas()){
			perguntaVO.setSelecionado(false);
		}
		getListaPerguntasSelecionadas().clear();
	}

	public void selecionarProfessor() throws Exception {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("professor");
			if(obj == null){
				if(getSelecionarTudo()){
					for(PessoaVO pessoaVO:getListaConsultaProfessor()){
						pessoaVO.setSelecionado(true);
						getListaConsultaProfessorSelecionados().add(pessoaVO);
					}
				}else{
					limparProfessor();
				}
			}else{
				if (obj.getSelecionado()) {
					getListaConsultaProfessorSelecionados().add(obj);
				} else {
					getListaConsultaProfessorSelecionados().remove(obj);
				}
			}
		} catch (Exception e) {
		}
	}

	public void limparProfessor() {
		for(PessoaVO pessoaVO: getListaConsultaProfessorSelecionados()){
			pessoaVO.setSelecionado(false);
		}
		getListaConsultaProfessorSelecionados().clear();
	}
	
	public void selecionarDisciplina() throws Exception {
		try {
			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
			if(obj == null){
				if(getSelecionarTudo()){
					for(DisciplinaVO disciplinaVO:getListaConsultaDisciplina()){
						disciplinaVO.setSelecionado(true);
						getListaDisciplinasSelecionadas().add(disciplinaVO);
					}
				}else{
					limparDisciplina();
				}
			}else{
				if (obj.getSelecionado()) {
					getListaDisciplinasSelecionadas().add(obj);			
				}else{	
					getListaDisciplinasSelecionadas().remove(obj);
				}
			}
			if (getListaDisciplinasSelecionadas().size() > 1) {
				if(!getListaConsultaProfessorSelecionados().isEmpty() && obj != null){
					consultarProfessores();
				}
				if(!getListaConsultaRespondenteSelecionados().isEmpty() && obj != null){
					consultarRespondentes();
				}
				setNomeDisciplina(getListaDisciplinasSelecionadas().size() + " Disciplinas Selecionadas");
			} else {
				setNomeDisciplina("");
			}
			obj = null;
			this.setValorConsultaDisciplina(null);
			this.setCampoConsultaDisciplina(null);
		} catch (Exception e) {
		}
	}

	public void selecionarCurso() throws Exception {
		CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
		setCurso(obj.getCodigo());
		setNomeCurso(obj.getNome());
	}

	public void selecionarAvaliacao() throws Exception {		
		AvaliacaoInstitucionalVO obj = (AvaliacaoInstitucionalVO) context().getExternalContext().getRequestMap().get("avaliacaoItens");
		carregarDadosAvaliacaoInstitucional(obj, getGerarRelatorioPublicacao());		
	}

	public void limparCurso() {
		setCurso(0);
		setNomeCurso("");		
		limparDisciplina();
	}

	public void limparAvaliacao() {
		setAvaliacao(null);
		setNomeAvaliacao("");
		setApresentarFiltroProfessor(false);
	}

	public void limparDisciplina() {
		setNomeDisciplina("");
		for(DisciplinaVO disciplinaVO: getListaDisciplinasSelecionadas()){
			disciplinaVO.setSelecionado(false);
		}
		getListaDisciplinasSelecionadas().clear();
	}

	public void gerarRelatorioGrafico() throws Exception {
		try {

			MatriculaVO matriculaVO =  null;
			if(getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()){
				matriculaVO = getVisaoAlunoControle().getMatricula();
			}
			setAvaliacaoInstucionalRelVO(getFacadeFactory().getAvaliacaoInstitucionalRelFacade().emitirRelatorioGrafico(getNivelDetalhamento(), getAvaliacao().getPublicarAlvoEnum(), getAvaliacao(), getQuestionario(), getListaPerguntasSelecionadas(), getCodigoUnidadeEnsino(), getCurso(), getTurno(), getTurma(), getListaDisciplinasSelecionadas(), getListaConsultaProfessorSelecionados(), getListaConsultaRespondenteSelecionados(), getDataInicio(), getDataFim(), isApresentarRespostaPorAgrupamento(), getGerarRelatorioPublicacao(), getUsuarioLogado(), matriculaVO));

			posicao = 1;
			voltar();

			setMensagemID("msg_relatorio_gerado");
			if (!getExisteRelatorio()) {
				setAbrirFiltroConsulta(true);
				setMensagemID("msg_relatorio_vazio", Uteis.ERRO,true);
			} else {
				setAbrirFiltroConsulta(false);
			}
		} catch (Exception e) {
			setAbrirFiltroConsulta(true);
			setMensagemDetalhada("msg_erro", getMensagemInternalizacao("msg_relatorio_erro"));
		}
	}

	public synchronized void gerarRelatorioPorRespondentePDF(Integer unidadeEnsino, Integer curso, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, String caminhoWebRelatorio, String urlAplicacaoExterna) throws Exception {
		List<AvaliacaoInstucionalRelVO> lista = new ArrayList<AvaliacaoInstucionalRelVO>(0);
		AvaliacaoInstucionalRelVO avaliacaoInstucionalRelVOs = null;
		try {			
			avaliacaoInstucionalRelVOs = getFacadeFactory().getAvaliacaoInstitucionalRelFacade().emitirRelatorioPorRespondente(getNivelDetalhamento(), getAvaliacao().getPublicarAlvoEnum(), getAvaliacao().getCodigo(), getQuestionario(), getListaPerguntasSelecionadas(), unidadeEnsino, curso, getTurno(), getTurma(), getListaDisciplinasSelecionadas(), getListaConsultaProfessorSelecionados(), getListaConsultaRespondenteSelecionados(),  getDataInicio(), getDataFim());
			if (avaliacaoInstucionalRelVOs != null && !avaliacaoInstucionalRelVOs.getQuestionarioRelVOs().isEmpty()) {
				lista.add(avaliacaoInstucionalRelVOs);
				getSuperParametroRelVO().adicionarParametro("periodo", Uteis.getData(getDataInicio()) + " " + UteisJSF.internacionalizar("prt_ate") + " " + Uteis.getData(getDataFim()));
				if (getRelatorioGeral()) {
					getSuperParametroRelVO().setNomeDesignIreport(AvaliacaoInstitucionalRel.getDesignIReportRelatorioRespondenteGeral());
					getSuperParametroRelVO().setSubReport_Dir(AvaliacaoInstitucionalRel.getCaminhoBaseRelatorio());
					getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
					getSuperParametroRelVO().setNomeUsuario(usuarioVO.getNome());
					getSuperParametroRelVO().setTituloRelatorio("Avaliação Institucional");
					getSuperParametroRelVO().setListaObjetos(lista);
					getSuperParametroRelVO().setCaminhoBaseRelatorio(AvaliacaoInstitucionalRel.getCaminhoBaseRelatorio());
					getSuperParametroRelVO().setNomeEmpresa(getUnidadeEnsinoLogado().getNome());
					getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
					getSuperParametroRelVO().setFiltros("");
					realizarImpressaoRelatorio();
					setCaminhoRelatorio("location.href='" + urlAplicacaoExterna + "/DownloadRelatorioSV?relatorio=" + getCaminhoRelatorio() + "'");					
				} else {
					getSuperParametroRelVO().setNomeDesignIreport(AvaliacaoInstitucionalRel.getDesignIReportRelatorioRespondente());
					getSuperParametroRelVO().setSubReport_Dir(AvaliacaoInstitucionalRel.getCaminhoBaseRelatorio());
					getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
					getSuperParametroRelVO().setNomeUsuario(usuarioVO.getNome());
					getSuperParametroRelVO().setTituloRelatorio("Avaliação Institucional");					
					getSuperParametroRelVO().setCaminhoBaseRelatorio(AvaliacaoInstitucionalRel.getCaminhoBaseRelatorio());
					getSuperParametroRelVO().setNomeEmpresa(getUnidadeEnsinoLogado().getNome());
					getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
					getSuperParametroRelVO().setFiltros("");					
					if(avaliacaoInstucionalRelVOs.getQuestionarioRelVOs().size() > 10){
						getProgressBarVO().setMaxValue(avaliacaoInstucionalRelVOs.getQuestionarioRelVOs().size()+3);
						getProgressBarVO().setProgresso(1l);
						realizarGeracaoRelatorioFormatoLink(getSuperParametroRelVO(), lista, usuarioVO, configuracaoGeralSistemaVO, caminhoWebRelatorio, urlAplicacaoExterna);
					}else{
						getSuperParametroRelVO().setListaObjetos(lista);
						realizarImpressaoRelatorio();
						setCaminhoRelatorio("location.href='" + urlAplicacaoExterna + "/DownloadRelatorioSV?relatorio=" + getCaminhoRelatorio() + "'");						
						getProgressBarVO().setProgresso(getProgressBarVO().getMaxValue().longValue());
					}
				}
				if(context() != null) {
					setMensagemID("msg_relatorio_gerado", Uteis.SUCESSO,true);
				}
			} else {
				if(context() != null) {
					setMensagemID("msg_relatorio_vazio", Uteis.ERRO,true);
				}
			}
		} catch (Exception e) {
			getProgressBarVO().setForcarEncerramento(true);
			getProgressBarVO().setException(e);			
			if(context() != null) {
				setMensagemDetalhada("msg_erro", getMensagemInternalizacao("msg_relatorio_erro"));
			}
		}finally {			
			Uteis.liberarListaMemoria(lista);
			avaliacaoInstucionalRelVOs=null;
		}
	}

	private void realizarGeracaoRelatorioFormatoLink(SuperParametroRelVO superParametroRelVO, List<AvaliacaoInstucionalRelVO> avaliacaoInstucionalRelVOs, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, String caminhoWebRelatorio, String urlAplicacaoExterna) throws Exception{
		setCaminhoRelatorio("");
		setFazerDownload(false);
		long time = new Date().getTime();
		String caminhoBaseRelatorio = (configuracaoGeralSistemaVO.getLocalUploadArquivoFixo().endsWith(File.separator) ? configuracaoGeralSistemaVO.getLocalUploadArquivoFixo():configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()+File.separator) +
				PastaBaseArquivoEnum.AVALIACAO_INSTITUCIONAL_TMP.getValue()+File.separator+getAvaliacao().getCodigo()+File.separator+time+"_"+usuarioVO.getCodigo();				
		List<QuestionarioRelVO> questionarioRelVOs = new ArrayList<QuestionarioRelVO>(0);
		
			questionarioRelVOs.addAll(avaliacaoInstucionalRelVOs.get(0).getQuestionarioRelVOs());
		
		Map<String, File> mapfile = new HashMap<String, File>(0);
		try{
			getProgressBarVO().setMaxValue(questionarioRelVOs.size()+3);
			int x = 1;
			for(QuestionarioRelVO questionarioRelVO:questionarioRelVOs){
				getProgressBarVO().setStatus("Gerando "+ getSuperParametroRelVO().getTipoRelatorioEnum() + " " + x +" de "+questionarioRelVOs.size());
				getProgressBarVO().setProgresso(Long.parseLong(x+""));
				avaliacaoInstucionalRelVOs.get(0).getQuestionarioRelVOs().clear();
				String caminhoArquivo = "";
				String caminhoWeb = "";
			if(!questionarioRelVO.getNomeUnidadeEnsino().trim().isEmpty()){
					if(!caminhoArquivo.trim().isEmpty()){
						caminhoArquivo += File.separator;
						caminhoWeb +="/";
					}
					caminhoArquivo += Uteis.removerCaracteresEspeciais3(questionarioRelVO.getNomeUnidadeEnsino().trim()).toUpperCase().replaceAll(" ", "_").replaceAll("\\|", "");
					caminhoWeb += Uteis.removerCaracteresEspeciais3(questionarioRelVO.getNomeUnidadeEnsino().trim()).toUpperCase().replaceAll(" ", "_").replaceAll("\\|", "");
			}
			if(!questionarioRelVO.getNomeCurso().trim().isEmpty()){
				if(!caminhoArquivo.trim().isEmpty()){
					caminhoArquivo += File.separator;
					caminhoWeb +="/";
				}
				caminhoArquivo += Uteis.removerCaracteresEspeciais3(questionarioRelVO.getNomeCurso().trim()).toUpperCase().replaceAll(" ", "_").replaceAll("\\|", "");
				caminhoWeb += Uteis.removerCaracteresEspeciais3(questionarioRelVO.getNomeCurso().trim()).toUpperCase().replaceAll(" ", "_").replaceAll("\\|", "");
			}
			if(!questionarioRelVO.getIdentificadorTurma().trim().isEmpty()){
				if(!caminhoArquivo.trim().isEmpty()){
					caminhoArquivo += File.separator;
					caminhoWeb +="/";
				}
				caminhoArquivo += Uteis.removerCaracteresEspeciais3(questionarioRelVO.getIdentificadorTurma().trim()).toUpperCase().replaceAll(" ", "_").replaceAll("\\|", "");
				caminhoWeb += Uteis.removerCaracteresEspeciais3(questionarioRelVO.getIdentificadorTurma().trim()).toUpperCase().replaceAll(" ", "_").replaceAll("\\|", "");
			}
			if(!questionarioRelVO.getNomeProfessor().trim().isEmpty()){
				if(!caminhoArquivo.trim().isEmpty()){
					caminhoArquivo += File.separator;
					caminhoWeb +="/";
				}
				caminhoArquivo += Uteis.removerCaracteresEspeciais3(questionarioRelVO.getNomeProfessor().trim()).toUpperCase().replaceAll(" ", "_").replaceAll("\\|", "");
				caminhoWeb += Uteis.removerCaracteresEspeciais3(questionarioRelVO.getNomeProfessor().trim()).toUpperCase().replaceAll(" ", "_").replaceAll("\\|", "");
			}
			if(!questionarioRelVO.getNomeDisciplina().trim().isEmpty()){
				if(!caminhoArquivo.trim().isEmpty()){
					caminhoArquivo += File.separator;
					caminhoWeb +="/";
				}
				caminhoArquivo += Uteis.removerCaracteresEspeciais3(questionarioRelVO.getNomeDisciplina().trim()).toUpperCase().replaceAll(" ", "_").replaceAll("\\|", "");
				caminhoWeb += Uteis.removerCaracteresEspeciais3(questionarioRelVO.getNomeDisciplina().trim()).toUpperCase().replaceAll(" ", "_").replaceAll("\\|", "");
			}
			if(!questionarioRelVO.getDepartamento().getNome().trim().isEmpty()){
				if(!caminhoArquivo.trim().isEmpty()){
					caminhoArquivo += File.separator;
					caminhoWeb +="/";
				}
				caminhoArquivo += Uteis.removerCaracteresEspeciais3(questionarioRelVO.getDepartamento().getNome().trim()).toUpperCase().replaceAll(" ", "_");
				caminhoWeb += Uteis.removerCaracteresEspeciais3(questionarioRelVO.getDepartamento().getNome().trim()).toUpperCase().replaceAll(" ", "_");
			}
			if(!questionarioRelVO.getCargo().getNome().trim().isEmpty()){
					if(!caminhoArquivo.trim().isEmpty()){
						caminhoArquivo += File.separator;
						caminhoWeb +="/";
					}
					caminhoArquivo += Uteis.removerCaracteresEspeciais3(questionarioRelVO.getCargo().getNome().trim()).toUpperCase().replaceAll(" ", "_");
					caminhoWeb += Uteis.removerCaracteresEspeciais3(questionarioRelVO.getCargo().getNome().trim()).toUpperCase().replaceAll(" ", "_");
				}
				if(!questionarioRelVO.getCoordenador().getNome().trim().isEmpty()){
					if(!caminhoArquivo.trim().isEmpty()){
						caminhoArquivo += File.separator;
						caminhoWeb +="/";
					}
					caminhoArquivo += Uteis.removerCaracteresEspeciais3(questionarioRelVO.getCoordenador().getNome()).toUpperCase().replaceAll(" ", "_");
					caminhoWeb += Uteis.removerCaracteresEspeciais3(questionarioRelVO.getCoordenador().getNome()).toUpperCase().replaceAll(" ", "_");
				}
				caminhoArquivo = caminhoArquivo.replaceAll(":", "").replaceAll("-", "").replaceAll(">", "").replaceAll("<", "").replaceAll(":", "").replace("*", "");
				caminhoWeb = caminhoWeb.replaceAll(":", "").replaceAll("-", "").replaceAll(">", "").replaceAll("<", "").replaceAll(":", "").replace("*", "");
				String nome = "";
				if(getTipoRelatorioGerar().equals("gerarRelatorioExcel")){
					nome = x+"_"+time+".xlsx";
				} else {
					nome = x+"_"+time+".pdf";
				}
						
				if(getTipoRelatorioGerar().equals("gerarRelatorioPorRespondentePDF")){
					nome = Uteis.removerCaracteresEspeciais3(questionarioRelVO.getNomeRespondente()).toUpperCase().replaceAll(" ", "_").replaceAll(":", "").replaceAll("-", "").replaceAll(">", "").replaceAll("<", "").replaceAll(":", "").replace("*", "")+".pdf";
				}
				String link = caminhoArquivo+File.separator+nome;
				questionarioRelVO.setLink(link);
				if(getTipoRelatorioGerar().equals("gerarRelatorioPorRespondentePDF")){
					questionarioRelVO.setNomeLink(questionarioRelVO.getNomeRespondente());
				}else{
					questionarioRelVO.setNomeLink(nome);
				}
				avaliacaoInstucionalRelVOs.get(0).getQuestionarioRelVOs().add(questionarioRelVO);
				getSuperParametroRelVO().setListaObjetos(avaliacaoInstucionalRelVOs);
				
				if(getTipoRelatorioGerar().equals("gerarRelatorioExcel")){					 
					GeradorRelatorio.realizarExportacaoEXCEL(getSuperParametroRelVO(), caminhoBaseRelatorio+File.separator+caminhoArquivo.replaceAll("\\|", ""), nome);
				} else {
					GeradorRelatorio.realizarExportacaoPDF(getSuperParametroRelVO(), caminhoBaseRelatorio+File.separator+caminhoArquivo.replaceAll("\\|", ""), nome);
				}

				String primeiroDiretorio = caminhoArquivo.contains(File.separator) ? caminhoArquivo.substring(0, caminhoArquivo.indexOf(File.separator)) : caminhoArquivo;
				if(!mapfile.containsKey(primeiroDiretorio)){
					mapfile.put(primeiroDiretorio, new File(caminhoBaseRelatorio+File.separator+primeiroDiretorio));
				}
				Uteis.liberarListaMemoria(questionarioRelVO.getPerguntaRelVOs());
				questionarioRelVO.setPerguntasJR(null);
				x++;
			}
			getProgressBarVO().setProgresso(getProgressBarVO().getProgresso()+1);
			getProgressBarVO().setStatus("Gerando Índice dos Arquivos PDF's.");
			/**
			 * Gerando Indice dos PDF's			
			 */
			getSuperParametroRelVO().setListaObjetos(questionarioRelVOs);
			getSuperParametroRelVO().setNomeDesignIreport(AvaliacaoInstitucionalRel.getDesignIReportRelatorioLink());
			getSuperParametroRelVO().adicionarParametro("nomeAvaliacao", avaliacaoInstucionalRelVOs.get(0).getNome());
			GeradorRelatorio.realizarExportacaoPDF(getSuperParametroRelVO(), caminhoBaseRelatorio.replaceAll("|", ""), "Indice.pdf");
			mapfile.put("indice", new File(caminhoBaseRelatorio+File.separator+"Indice.pdf"));			
			/**
			 * Zipando os PDF's			
			 */
			getProgressBarVO().setProgresso(getProgressBarVO().getProgresso()+1);
			getProgressBarVO().setStatus("Compactando Arquivos" + getSuperParametroRelVO().getTipoRelatorioEnum());
			String caminhoZip = caminhoWebRelatorio+File.separator+"AVAL_INST_"+getAvaliacao().getCodigo()+"_"+time+".zip";
			File arquivoZip = new File(caminhoZip);
			File[] files = new File[mapfile.size()];
			int y = 0;
			for(File file : mapfile.values()){
				files[y++] = file; 
			}
			getFacadeFactory().getArquivoHelper().zip(files, arquivoZip);
			setCaminhoRelatorio("location.href='" + urlAplicacaoExterna + "/DownloadRelatorioSV?relatorio=AVAL_INST_"+getAvaliacao().getCodigo()+"_"+time+".zip'");
//			setCaminhoRelatorio("abrirPopup('"+urlAplicacaoExterna+"/relatorio/AVAL_INST_"+getAvaliacao().getCodigo()+"_"+time+".zip', 'AVAL_INST_"+getAvaliacao().getCodigo()+"_"+time+"', 0, 0)");
			getProgressBarVO().setProgresso(getProgressBarVO().getProgresso()+1);
			setFazerDownload(true);
		}catch(Exception e){
			getProgressBarVO().setForcarEncerramento(true);
			throw e;
		}finally {
			
			File fileBase = new File(caminhoBaseRelatorio);
			if(fileBase != null && fileBase.exists()){
				ArquivoHelper.deleteRecursivo(fileBase);
				if(fileBase != null && fileBase.exists()){					
					fileBase.delete();
				}
			}
			Uteis.liberarListaMemoria(questionarioRelVOs);
		}
	}

	public void gerarRelatorioPerguntasTextuais() throws Exception {
		try {
			AvaliacaoInstucionalRelVO avaliacaoInstucionalRelVOs = getFacadeFactory().getAvaliacaoInstitucionalRelFacade().emitirRelatorioPerguntasTextuais(getNivelDetalhamento(), getAvaliacao().getPublicarAlvoEnum(), getAvaliacao(), getQuestionario(), getListaPerguntasSelecionadas(), getCodigoUnidadeEnsino(), getCurso(), getTurno(), getTurma(), getListaDisciplinasSelecionadas(), getListaConsultaProfessorSelecionados(),  getListaConsultaRespondenteSelecionados(), getDataInicio(), getDataFim(), isApresentarRespostaPorAgrupamento());
			gerarRelatorioTextual(TipoRelatorioEnum.PDF, avaliacaoInstucionalRelVOs);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void gerarRelatorioTextual(TipoRelatorioEnum tipoRelatorioEnum, AvaliacaoInstucionalRelVO avaliacaoInstucionalRelVOs) throws Exception {
				getSuperParametroRelVO().adicionarParametro("periodo", Uteis.getData(getDataInicio())+" "+UteisJSF.internacionalizar("prt_ate")+" "+Uteis.getData(getDataFim()));
		if (avaliacaoInstucionalRelVOs != null && !avaliacaoInstucionalRelVOs.getQuestionarioRelVOs().isEmpty()) {
			List<AvaliacaoInstucionalRelVO> lista = new ArrayList<AvaliacaoInstucionalRelVO>(0);
			lista.add(avaliacaoInstucionalRelVOs);
			getSuperParametroRelVO().adicionarParametro("periodo", Uteis.getData(getDataInicio()) + " " + UteisJSF.internacionalizar("prt_ate") + " " + Uteis.getData(getDataFim()));
			if (getRelatorioGeral()) {
				getSuperParametroRelVO().setNomeDesignIreport(AvaliacaoInstitucionalRel.getDesignIReportRelatorioPerguntasTextuais());
				getSuperParametroRelVO().setSubReport_Dir(AvaliacaoInstitucionalRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(tipoRelatorioEnum);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Avaliação Institucional");
				getSuperParametroRelVO().setListaObjetos(lista);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(AvaliacaoInstitucionalRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeEmpresa(getUnidadeEnsinoLogado().getNome());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");
				realizarImpressaoRelatorio();
			} else {
				getSuperParametroRelVO().setNomeDesignIreport(AvaliacaoInstitucionalRel.getDesignIReportRelatorioPerguntasTextuais());
				getSuperParametroRelVO().setSubReport_Dir(AvaliacaoInstitucionalRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(tipoRelatorioEnum);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Avaliação Institucional");
				getSuperParametroRelVO().setListaObjetos(lista);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(AvaliacaoInstitucionalRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeEmpresa(getUnidadeEnsinoLogado().getNome());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");
				realizarImpressaoRelatorio();
			}
			setMensagemID("msg_relatorio_gerado", Uteis.SUCESSO,true);
		} else {
			setMensagemID("msg_relatorio_vazio", Uteis.ERRO,true);
		}

	}

	public void gerarRelatorioPerguntasTextuaisDOC() throws Exception {
		try {
			AvaliacaoInstucionalRelVO avaliacaoInstucionalRelVOs = getFacadeFactory().getAvaliacaoInstitucionalRelFacade().emitirRelatorioPerguntasTextuais(getNivelDetalhamento(), getAvaliacao().getPublicarAlvoEnum(), getAvaliacao(), getQuestionario(), getListaPerguntasSelecionadas(), getCodigoUnidadeEnsino(), getCurso(), getTurno(), getTurma(), getListaDisciplinasSelecionadas(), getListaConsultaProfessorSelecionados(), getListaConsultaRespondenteSelecionados(), getDataInicio(), getDataFim(), isApresentarRespostaPorAgrupamento());
			gerarRelatorioTextual(TipoRelatorioEnum.DOC, avaliacaoInstucionalRelVOs);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void avancar() {
		posicao = posicao + 1;
		int tamList = getAvaliacaoInstucionalRelVO().getQuestionarioRelVOs().size();
		if (posicao <= tamList - 1) {
			setQuestionarioRelVO(getAvaliacaoInstucionalRelVO().getQuestionarioRelVOs().get(posicao));
		}
	}

	public void voltar() {
		posicao = posicao - 1;
		if (getAvaliacaoInstucionalRelVO().getQuestionarioRelVOs().isEmpty()) {
			posicao = -1;
			return;
		}
		if (posicao >= 0) {
			setQuestionarioRelVO(getAvaliacaoInstucionalRelVO().getQuestionarioRelVOs().get(posicao));
		}
	}

	public Boolean getApresentarVoltar() {
		if (posicao <= 0) {
			return false;
		}
		if (getRelatorioGeral()) {
			return false;
		}
		return true;
	}

	public Boolean getApresentarAvancar() {
		int tamList = getAvaliacaoInstucionalRelVO().getQuestionarioRelVOs().size();
		if (posicao == tamList - 1) {
			return false;
		}
		if (getRelatorioGeral()) {
			return false;
		}
		return true;
	}

	public Boolean getExisteRelatorio() {
		if (getAvaliacaoInstucionalRelVO().getNome().equals("")) {
			return false;
		}
		return true;
	}

	public void verAvaliacaoPergunta() {
		PerguntaRelVO obj = (PerguntaRelVO) context().getExternalContext().getRequestMap().get("perguntaItens");
		setPerguntaRelVO(obj);
	}

	public void imprimirRespostasTextuais() {
		try {
			PerguntaRelVO obj = (PerguntaRelVO) context().getExternalContext().getRequestMap().get("perguntaItens");
			if (!obj.getRespostaTexto().isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "AvaliacaoInstitucionalRelImpressaoRespostasTextuais.jrxml");
				getSuperParametroRelVO().setSubReport_Dir("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.DOC);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setListaObjetos(obj.getRespostaTexto());
				getSuperParametroRelVO().setTituloRelatorio(obj.getNome());
				getSuperParametroRelVO().setCaminhoBaseRelatorio("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator);
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok", Uteis.SUCESSO,true);
			} else {
				setUsarTargetBlank("");
				setMensagemID("msg_relatorio_sem_dados", Uteis.ERRO,true);
			}
		} catch (Exception ex) {
			setUsarTargetBlank("");
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	/**
	 * @return the avaliacao
	 */
	public AvaliacaoInstitucionalVO getAvaliacao() {
		if (avaliacao == null) {
			avaliacao = new AvaliacaoInstitucionalVO();
		}
		return avaliacao;
	}

	/**
	 * @param avaliacao
	 *            the avaliacao to set
	 */
	public void setAvaliacao(AvaliacaoInstitucionalVO avaliacao) {
		this.avaliacao = avaliacao;
	}
	
	/**
	 * @return the questionario
	 */
	public Integer getQuestionario() {
		if (questionario == null) {
			questionario = 0;
		}
		return questionario;
	}

	/**
	 * @param questionario
	 *            the questionario to set
	 */
	public void setQuestionario(Integer questionario) {
		this.questionario = questionario;
	}

	/**
	 * @return the listaQuestionarios
	 */
	public List<SelectItem> getListaQuestionarios() {
		return listaQuestionarios;
	}

	/**
	 * @param listaQuestionarios
	 *            the listaQuestionarios to set
	 */
	public void setListaQuestionarios(List<SelectItem> listaQuestionarios) {
		this.listaQuestionarios = listaQuestionarios;
	}

	/**
	 * @param pergunta
	 *            the pergunta to set
	 */
	public String getPergunta() {
		int qtde = getListaPerguntasSelecionadas().size();
		if (qtde > 1) {
			return qtde + " Perguntas Selecionadas";
		} else {
			return qtde + " Pergunta Selecionada";
		}
	}
	

	public String getProfessor() {
		int qtde = getListaConsultaProfessorSelecionados().size();
		if (qtde > 1) {
			return qtde + " Professores Selecionados";
		} else {
			return qtde + " Professor Selecionado";
		}
	}	

	public String getDisciplina() {
		int qtde = getListaDisciplinasSelecionadas().size();
		if (qtde > 1) {
			return qtde + " Disciplinas Selecionadas";
		} else {
			return qtde + " Disciplina Selecionada";
		}
	}

	/**
	 * @return the listaPerguntas
	 */
	public List<PerguntaVO> getListaPerguntas() {
		return listaPerguntas;
	}

	/**
	 * @param listaPerguntas
	 *            the listaPerguntas to set
	 */
	public void setListaPerguntas(List<PerguntaVO> listaPerguntas) {
		this.listaPerguntas = listaPerguntas;
	}

	/**
	 * @return the listaUnidadesEnsino
	 */
	public List<SelectItem> getListaUnidadesEnsino() {
		return listaUnidadesEnsino;
	}

	/**
	 * @param listaUnidadesEnsino
	 *            the listaUnidadesEnsino to set
	 */
	public void setListaUnidadesEnsino(List<SelectItem> listaUnidadesEnsino) {
		this.listaUnidadesEnsino = listaUnidadesEnsino;
	}

	/**
	 * @return the turno
	 */
	public Integer getTurno() {
		return turno;
	}

	/**
	 * @param turno
	 *            the turno to set
	 */
	public void setTurno(Integer turno) {
		this.turno = turno;
	}

	/**
	 * @return the listaTurnos
	 */
	public List<SelectItem> getListaTurnos() {
		return listaTurnos;
	}

	/**
	 * @param listaTurnos
	 *            the listaTurnos to set
	 */
	public void setListaTurnos(List<SelectItem> listaTurnos) {
		this.listaTurnos = listaTurnos;
	}

	/**
	 * @return the turma
	 */
	public Integer getTurma() {
		return turma;
	}

	/**
	 * @param turma
	 *            the turma to set
	 */
	public void setTurma(Integer turma) {
		this.turma = turma;
	}
	
	/**
	 * @return the curso
	 */
	public Integer getCurso() {
		return curso;
	}

	/**
	 * @param curso
	 *            the curso to set
	 */
	public void setCurso(Integer curso) {
		this.curso = curso;
	}

	// /**
	// * @return the disciplina
	// */
	// public Integer getDisciplina() {
	// return disciplina;
	// }
	/**
	 * @param disciplina
	 *            the disciplina to set
	 */
	// public void setDisciplina(Integer disciplina) {
	// this.disciplina = disciplina;
	// }
	/**
	 * @return the filtrarQuestionario
	 */
	public Boolean getFiltrarQuestionario() {
		return filtrarQuestionario;
	}

	/**
	 * @param filtrarQuestionario
	 *            the filtrarQuestionario to set
	 */
	public void setFiltrarQuestionario(Boolean filtrarQuestionario) {
		this.filtrarQuestionario = filtrarQuestionario;
	}

	/**
	 * @return the filtrarPergunta
	 */
	public Boolean getFiltrarPergunta() {
		return filtrarPergunta;
	}

	/**
	 * @param filtrarPergunta
	 *            the filtrarPergunta to set
	 */
	public void setFiltrarPergunta(Boolean filtrarPergunta) {
		this.filtrarPergunta = filtrarPergunta;
	}

	/**
	 * @return the filtrarUnidadeEnsino
	 */
	public Boolean getFiltrarUnidadeEnsino() {
		return filtrarUnidadeEnsino;
	}

	/**
	 * @param filtrarUnidadeEnsino
	 *            the filtrarUnidadeEnsino to set
	 */
	public void setFiltrarUnidadeEnsino(Boolean filtrarUnidadeEnsino) {
		this.filtrarUnidadeEnsino = filtrarUnidadeEnsino;
	}

	/**
	 * @return the filtrarCurso
	 */
	public Boolean getFiltrarCurso() {
		return filtrarCurso;
	}

	/**
	 * @param filtrarCurso
	 *            the filtrarCurso to set
	 */
	public void setFiltrarCurso(Boolean filtrarCurso) {
		this.filtrarCurso = filtrarCurso;
	}

	/**
	 * @return the filtrarTurno
	 */
	public Boolean getFiltrarTurno() {
		return filtrarTurno;
	}

	/**
	 * @param filtrarTurno
	 *            the filtrarTurno to set
	 */
	public void setFiltrarTurno(Boolean filtrarTurno) {
		this.filtrarTurno = filtrarTurno;
	}

	/**
	 * @return the filtrarTurma
	 */
	public Boolean getFiltrarTurma() {
		return filtrarTurma;
	}

	/**
	 * @param filtrarTurma
	 *            the filtrarTurma to set
	 */
	public void setFiltrarTurma(Boolean filtrarTurma) {
		this.filtrarTurma = filtrarTurma;
	}

	/**
	 * @return the filtrarDisciplina
	 */
	public Boolean getFiltrarDisciplina() {
		return filtrarDisciplina;
	}

	/**
	 * @param filtrarDisciplina
	 *            the filtrarDisciplina to set
	 */
	public void setFiltrarDisciplina(Boolean filtrarDisciplina) {
		this.filtrarDisciplina = filtrarDisciplina;
	}

	/**
	 * @return the codigoUnidadeEnsino
	 */
	public Integer getCodigoUnidadeEnsino() {
		return codigoUnidadeEnsino;
	}

	/**
	 * @param codigoUnidadeEnsino
	 *            the codigoUnidadeEnsino to set
	 */
	public void setCodigoUnidadeEnsino(Integer codigoUnidadeEnsino) {
		this.codigoUnidadeEnsino = codigoUnidadeEnsino;
	}

	/**
	 * @return the nomeCurso
	 */
	public String getNomeCurso() {
		return nomeCurso;
	}

	/**
	 * @param nomeCurso
	 *            the nomeCurso to set
	 */
	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}

	/**
	 * @return the nomeDisciplina
	 */
	public String getNomeDisciplina() {
		return nomeDisciplina;
	}

	/**
	 * @param nomeDisciplina
	 *            the nomeDisciplina to set
	 */
	public void setNomeDisciplina(String nomeDisciplina) {
		this.nomeDisciplina = nomeDisciplina;
	}

	/**
	 * @return the campoConsultaPergunta
	 */
	public String getCampoConsultaPergunta() {
		return campoConsultaPergunta;
	}

	/**
	 * @param campoConsultaPergunta
	 *            the campoConsultaPergunta to set
	 */
	public void setCampoConsultaPergunta(String campoConsultaPergunta) {
		this.campoConsultaPergunta = campoConsultaPergunta;
	}

	/**
	 * @return the valorConsultaPergunta
	 */
	public String getValorConsultaPergunta() {
		return valorConsultaPergunta;
	}

	/**
	 * @param valorConsultaPergunta
	 *            the valorConsultaPergunta to set
	 */
	public void setValorConsultaPergunta(String valorConsultaPergunta) {
		this.valorConsultaPergunta = valorConsultaPergunta;
	}

	/**
	 * @return the listaConsultaPergunta
	 */
	public List<PerguntaVO> getListaConsultaPergunta() {
		return listaConsultaPergunta;
	}

	/**
	 * @param listaConsultaPergunta
	 *            the listaConsultaPergunta to set
	 */
	public void setListaConsultaPergunta(List<PerguntaVO> listaConsultaPergunta) {
		this.listaConsultaPergunta = listaConsultaPergunta;
	}

	/**
	 * @return the listaPerguntasSelecionadas
	 */
	public List<PerguntaVO> getListaPerguntasSelecionadas() {
		if (listaPerguntasSelecionadas == null) {
			listaPerguntasSelecionadas = new ArrayList<PerguntaVO>(0);
		}
		return listaPerguntasSelecionadas;
	}

	/**
	 * @param listaPerguntasSelecionadas
	 *            the listaPerguntasSelecionadas to set
	 */
	public void setListaPerguntasSelecionadas(List<PerguntaVO> listaPerguntasSelecionadas) {
		this.listaPerguntasSelecionadas = listaPerguntasSelecionadas;
	}

	/**
	 * @return the campoConsultaCurso
	 */
	public String getCampoConsultaCurso() {
		return campoConsultaCurso;
	}

	/**
	 * @param campoConsultaCurso
	 *            the campoConsultaCurso to set
	 */
	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	/**
	 * @return the valorConsultaCurso
	 */
	public String getValorConsultaCurso() {
		return valorConsultaCurso;
	}

	/**
	 * @param valorConsultaCurso
	 *            the valorConsultaCurso to set
	 */
	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	/**
	 * @return the listaConsultaCurso
	 */
	public List<CursoVO> getListaConsultaCurso() {
		return listaConsultaCurso;
	}

	/**
	 * @param listaConsultaCurso
	 *            the listaConsultaCurso to set
	 */
	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public AvaliacaoInstucionalRelVO getAvaliacaoInstucionalRelVO() {
		return avaliacaoInstucionalRelVO;
	}

	public void setAvaliacaoInstucionalRelVO(AvaliacaoInstucionalRelVO avaliacaoInstucionalRelVO) {
		this.avaliacaoInstucionalRelVO = avaliacaoInstucionalRelVO;
	}

	/**
	 * @return the campoConsultaDisciplina
	 */
	public String getCampoConsultaDisciplina() {
		return campoConsultaDisciplina;
	}

	/**
	 * @param campoConsultaDisciplina
	 *            the campoConsultaDisciplina to set
	 */
	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}

	/**
	 * @return the valorConsultaDisciplina
	 */
	public String getValorConsultaDisciplina() {
		return valorConsultaDisciplina;
	}

	/**
	 * @param valorConsultaDisciplina
	 *            the valorConsultaDisciplina to set
	 */
	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}

	/**
	 * @return the listaConsultaDisciplina
	 */
	public List<DisciplinaVO> getListaConsultaDisciplina() {
		return listaConsultaDisciplina;
	}

	/**
	 * @param listaConsultaDisciplina
	 *            the listaConsultaDisciplina to set
	 */
	public void setListaConsultaDisciplina(List<DisciplinaVO> listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
	}

	/**
	 * @return the listaDisciplinas
	 */
	public List<DisciplinaVO> getListaDisciplinas() {
		if (listaDisciplinas == null) {
			listaDisciplinas = new ArrayList<DisciplinaVO>(0);
		}
		return listaDisciplinas;
	}

	/**
	 * @param listaDisciplinas
	 *            the listaDisciplinas to set
	 */
	public void setListaDisciplinas(List<DisciplinaVO> listaDisciplinas) {
		this.listaDisciplinas = listaDisciplinas;
	}

	/**
	 * @return the listaDisciplinasSelecionadas
	 */
	public List<DisciplinaVO> getListaDisciplinasSelecionadas() {
		if (listaDisciplinasSelecionadas == null) {
			listaDisciplinasSelecionadas = new ArrayList<DisciplinaVO>(0);
		}
		return listaDisciplinasSelecionadas;
	}

	public Integer getPosicao() {
		if (posicao == null) {
			posicao = 0;
		}
		return posicao;
	}

	public void setPosicao(Integer posicao) {
		this.posicao = posicao;
	}

	public QuestionarioRelVO getQuestionarioRelVO() {
		if (questionarioRelVO == null) {
			questionarioRelVO = new QuestionarioRelVO();
		}
		return questionarioRelVO;
	}

	public void setQuestionarioRelVO(QuestionarioRelVO questionarioRelVO) {
		this.questionarioRelVO = questionarioRelVO;
	}

	/**
	 * @param listaDisciplinasSelecionadas
	 *            the listaDisciplinasSelecionadas to set
	 */
	public void setListaDisciplinasSelecionadas(List<DisciplinaVO> listaDisciplinasSelecionadas) {
		this.listaDisciplinasSelecionadas = listaDisciplinasSelecionadas;
	}

	public PerguntaRelVO getPerguntaRelVO() {
		if (perguntaRelVO == null) {
			perguntaRelVO = new PerguntaRelVO();
		}
		return perguntaRelVO;
	}

	public void setPerguntaRelVO(PerguntaRelVO perguntaRelVO) {
		this.perguntaRelVO = perguntaRelVO;
	}

	public Boolean getFiltrarProfessor() {
		if (filtrarProfessor == null) {
			filtrarProfessor = false;
		}
		return filtrarProfessor;
	}

	public void setFiltrarProfessor(Boolean filtrarProfessor) {
		this.filtrarProfessor = filtrarProfessor;
	}

	public List<PessoaVO> getListaConsultaProfessor() {
		if (listaConsultaProfessor == null) {
			listaConsultaProfessor = new ArrayList<PessoaVO>(0);
		}
		return listaConsultaProfessor;
	}

	public void setListaConsultaProfessor(List<PessoaVO> listaConsultaProfessor) {
		this.listaConsultaProfessor = listaConsultaProfessor;
	}

	public List<PessoaVO> getListaConsultaProfessorSelecionados() {
		if (listaConsultaProfessorSelecionados == null) {
			listaConsultaProfessorSelecionados = new ArrayList<PessoaVO>(0);
		}
		return listaConsultaProfessorSelecionados;
	}

	public void setListaConsultaProfessorSelecionados(List<PessoaVO> listaConsultaProfessorSelecionados) {
		this.listaConsultaProfessorSelecionados = listaConsultaProfessorSelecionados;
	}

	public Boolean getRelatorioGeral() {
		return getNivelDetalhamento().equals(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.GERAL);
	}

	public Boolean getAbrirFiltroConsulta() {
		if (abrirFiltroConsulta == null) {
			abrirFiltroConsulta = true;
		}
		return abrirFiltroConsulta;
	}

	public void setAbrirFiltroConsulta(Boolean abrirFiltroConsulta) {
		this.abrirFiltroConsulta = abrirFiltroConsulta;
	}

	public String getCampoConsultaAvaliacao() {
		if (campoConsultaAvaliacao == null) {
			campoConsultaAvaliacao = "";
		}
		return campoConsultaAvaliacao;
	}

	public void setCampoConsultaAvaliacao(String campoConsultaAvaliacao) {
		this.campoConsultaAvaliacao = campoConsultaAvaliacao;
	}

	public String getValorConsultaAvaliacao() {
		if (valorConsultaAvaliacao == null) {
			valorConsultaAvaliacao = "";
		}
		return valorConsultaAvaliacao;
	}

	public void setValorConsultaAvaliacao(String valorConsultaAvaliacao) {
		this.valorConsultaAvaliacao = valorConsultaAvaliacao;
	}

	public String getNomeAvaliacao() {
		if (nomeAvaliacao == null) {
			nomeAvaliacao = "";
		}
		return nomeAvaliacao;
	}

	public void setNomeAvaliacao(String nomeAvaliacao) {
		this.nomeAvaliacao = nomeAvaliacao;
	}

	public Date getDataInicio() {

		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {

		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getIdentificadorTurma() {
		if (identificadorTurma == null) {
			identificadorTurma = "";
		}
		return identificadorTurma;
	}

	public void setIdentificadorTurma(String identificadorTurma) {
		this.identificadorTurma = identificadorTurma;
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

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>();
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public List<SelectItem> tipoConsultaComboTurma;
	public List<SelectItem> getTipoConsultaComboTurma() {
		if(tipoConsultaComboTurma == null){
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("nome", "Identificador Turma"));
			tipoConsultaComboTurma.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboTurma;
	}

	public Boolean getApresentarFiltroProfessor() {
		if (apresentarFiltroProfessor == null) {
			apresentarFiltroProfessor = Boolean.FALSE;
		}
		return apresentarFiltroProfessor;
	}

	public void setApresentarFiltroProfessor(Boolean apresentarFiltroProfessor) {
		this.apresentarFiltroProfessor = apresentarFiltroProfessor;
	}
	
	public boolean getIsApresentarPergunta() {
		return Uteis.isAtributoPreenchido(getAvaliacao());
	}

	public void executarVerificarApresentarFiltroProfessor() throws Exception {
		setAvaliacao(getFacadeFactory().getAvaliacaoInstitucionalFacade().consultarPorChavePrimaria(getAvaliacao().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		String escopo = getAvaliacao().getQuestionarioVO().getEscopo();
		String publicoAlvo = getAvaliacao().getPublicoAlvo();
		if (!getAvaliacao().getAvaliacaoUltimoModulo() 
				&& (escopo.equals("DI") || escopo.equals("UM")) 
				&& (publicoAlvo.equals("CU") || publicoAlvo.equals("TC") || publicoAlvo.equals("TU"))) {
			setApresentarFiltroProfessor(true);
		} else if (!getAvaliacao().getAvaliacaoUltimoModulo() && (publicoAlvo.equals("COORDENADORES_PROFESSOR"))) {
			setApresentarFiltroProfessor(true);
		} else if (!getAvaliacao().getAvaliacaoUltimoModulo() && (publicoAlvo.equals("PR"))) {
			setApresentarFiltroProfessor(true);
		} else {
			setApresentarFiltroProfessor(false);
		}
	}

	public void selecionarTudo() {
		Iterator<PerguntaVO> i = getListaConsultaPergunta().iterator();
		while (i.hasNext()) {
			PerguntaVO m = (PerguntaVO) i.next();
			m.setSelecionado(getSelecionarTudo());
			if (m.getSelecionado()) {
				getListaPerguntasSelecionadas().add(m);
			} else {
				getListaPerguntasSelecionadas().remove(m);
			}
			
		}
		if(!getListaConsultaProfessorSelecionados().isEmpty()){
			consultarProfessores();
		}
		if(!getListaConsultaRespondenteSelecionados().isEmpty()){
			consultarRespondentes();
		}
	}
	
	public Boolean getSelecionarTudo() {
		if (selecionarTudo == null) {
			selecionarTudo = Boolean.TRUE;
		}
		return selecionarTudo;
	}

	public void setSelecionarTudo(Boolean selecionarTudo) {
		this.selecionarTudo = selecionarTudo;
	}

	public ProgressBarVO getProgressBarVO() {
		if(progressBarVO == null){
			progressBarVO = new ProgressBarVO();
		}
		return progressBarVO;
	}

	public void setProgressBarVO(ProgressBarVO progressBarVO) {
		this.progressBarVO = progressBarVO;
	}
	
	public synchronized void realizarGeracaoRelatorioProgressBar() throws Exception{
		if(!getGerarRelatorioQuestionarioSelecionado()){
			if(getTipoRelatorioGerar().equals("gerarRelatorioFormatoGrafico")){
				gerarRelatorioPorTipo(TipoRelatorio.GRAFICO, getCodigoUnidadeEnsino(), getCurso(), getProgressBarVO().getUsuarioVO(), getProgressBarVO().getConfiguracaoGeralSistemaVO(), getProgressBarVO().getCaminhoWebRelatorio(), getProgressBarVO().getUrlAplicacaoExterna());
			}
			if(getTipoRelatorioGerar().equals("gerarRelatorioPorRespondentePDF")){
				gerarRelatorioPorRespondentePDF(getCodigoUnidadeEnsino(), getCurso(), getProgressBarVO().getUsuarioVO(), getProgressBarVO().getConfiguracaoGeralSistemaVO(), getProgressBarVO().getCaminhoWebRelatorio(), getProgressBarVO().getUrlAplicacaoExterna());
			}
			if(getTipoRelatorioGerar().equals("gerarRelatorio")){
				gerarRelatorioPorTipo(TipoRelatorio.NORMAL, getCodigoUnidadeEnsino(),  getCurso(), getProgressBarVO().getUsuarioVO(), getProgressBarVO().getConfiguracaoGeralSistemaVO(), getProgressBarVO().getCaminhoWebRelatorio(), getProgressBarVO().getUrlAplicacaoExterna());
			}

			if(getTipoRelatorioGerar().equals("gerarRelatorioExcel")){
				gerarRelatorioPorTipo(TipoRelatorio.EXCEL, getCodigoUnidadeEnsino(),  getCurso(), getProgressBarVO().getUsuarioVO(), getProgressBarVO().getConfiguracaoGeralSistemaVO(), getProgressBarVO().getCaminhoWebRelatorio(), getProgressBarVO().getUrlAplicacaoExterna());
			}
		}else{
			realizarGeracaoRelatorioPorQuestionario(getProgressBarVO().getUsuarioVO(), getProgressBarVO().getConfiguracaoGeralSistemaVO(), getProgressBarVO().getCaminhoWebRelatorio(), getProgressBarVO().getUrlAplicacaoExterna());
		}
	}

	public String getTipoRelatorioGerar() {
		if(tipoRelatorioGerar == null){
			tipoRelatorioGerar  = "";
		}
		return tipoRelatorioGerar;
	}

	public void setTipoRelatorioGerar(String tipoRelatorioGerar) {
		this.tipoRelatorioGerar = tipoRelatorioGerar;
	}
	
	public void selecionarRelatorioPorQuestionario(){
		setFazerDownload(false);
		setCaminhoRelatorio("");
		setQuestionarioRelSelecionadoVO((QuestionarioRelVO) getRequestMap().get("questionarioItem"));
		setGerarRelatorioQuestionarioSelecionado(true);
		inicializarProgressBar();
	}
	
	
	public synchronized String getAbrirModalQuestionarioSelecionar(){		
		if(getFazerDownload()){
			String caminho = getCaminhoRelatorio();
			if (getProgressBarVO().getPollAtivado()) {
				caminho = "";
			}
			return (getQuestionarioRelVOs().isEmpty()?"":"PF('panelQuestionarioSelecionar').show();")+caminho;
		}
		return getQuestionarioRelVOs().isEmpty()?"":"PF('panelQuestionarioSelecionar').show()";
	}
	
	public void realizarGeracaoRelatorioPorQuestionario(UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, String caminhoWebRelatorio, String urlAplicacaoExterna) throws Exception{
		List<DisciplinaVO> disciplinaVOs = new ArrayList<DisciplinaVO>(0);
		disciplinaVOs.addAll(getListaDisciplinasSelecionadas());
		DisciplinaVO disciplinaVO =  null;
		if(Uteis.isAtributoPreenchido(getQuestionarioRelSelecionadoVO().getCodigoDisciplina())){
			disciplinaVO =  new DisciplinaVO();
			getListaDisciplinasSelecionadas().clear();
			disciplinaVO.setSelecionado(true);
			disciplinaVO.setCodigo(getQuestionarioRelSelecionadoVO().getCodigoDisciplina());
			getListaDisciplinasSelecionadas().add(disciplinaVO);
		}
		if(getTipoRelatorioGerar().equals("gerarRelatorioFormatoGrafico")){
			gerarRelatorioPorTipo(TipoRelatorio.GRAFICO, getQuestionarioRelSelecionadoVO().getCodigoUnidadeEnsino(), getQuestionarioRelSelecionadoVO().getCodigoCurso(), usuarioVO, configuracaoGeralSistemaVO, caminhoWebRelatorio, urlAplicacaoExterna);
		} else if(getTipoRelatorioGerar().equals("gerarRelatorioExcel")){
			gerarRelatorioPorTipo(TipoRelatorio.EXCEL, getCodigoUnidadeEnsino(),  getCurso(), getProgressBarVO().getUsuarioVO(), getProgressBarVO().getConfiguracaoGeralSistemaVO(), getProgressBarVO().getCaminhoWebRelatorio(), getProgressBarVO().getUrlAplicacaoExterna());
		} else  if(getTipoRelatorioGerar().equals("gerarRelatorioPorRespondentePDF")){
			gerarRelatorioPorRespondentePDF(getQuestionarioRelSelecionadoVO().getCodigoUnidadeEnsino(), getQuestionarioRelSelecionadoVO().getCodigoCurso(), usuarioVO, configuracaoGeralSistemaVO, caminhoWebRelatorio, urlAplicacaoExterna);
		} else  if(getTipoRelatorioGerar().equals("gerarRelatorio")){
			gerarRelatorioPorTipo(TipoRelatorio.NORMAL, getQuestionarioRelSelecionadoVO().getCodigoUnidadeEnsino(), getQuestionarioRelSelecionadoVO().getCodigoCurso(), usuarioVO, configuracaoGeralSistemaVO, caminhoWebRelatorio, urlAplicacaoExterna);
		}
		getListaDisciplinasSelecionadas().clear();
		getListaDisciplinasSelecionadas().addAll(disciplinaVOs);
		Uteis.liberarListaMemoria(disciplinaVOs);
		disciplinaVO =  null;
		setGerarRelatorioQuestionarioSelecionado(false);
	}
	
	public synchronized void consultarQuantidadeRelatorioGerar() throws Exception {
		limparMensagem();
		setFazerDownload(false);
		setCaminhoRelatorio("");
		setGerarRelatorioQuestionarioSelecionado(false);
		setQuestionarioRelVOs(null);
		getUsuarioLogado();
		List<QuestionarioRelVO> questionarioRelVOs = null;
		setMensagemID("");
		setTotalResposta(getFacadeFactory().getAvaliacaoInstitucionalRelFacade().consultarQuantidadeResposta(nivelDetalhamento, PublicoAlvoAvaliacaoInstitucional.getEnum(getAvaliacao().getPublicoAlvo()), avaliacao, questionario, listaPerguntas, getCodigoUnidadeEnsino(), curso, turno, turma, listaDisciplinas, null, null, listaConsultaProfessor, listaConsultaRespondente, dataInicio, dataFim, true, gerarRelatorioPublicacao, getUsuarioLogado(), null));
        try {
		boolean iniciarProgressBar = true;
			if(!getRelatorioGeral() && (getAvaliacao().getPublicoAlvo_TodosCursos() || getAvaliacao().getPublicoAlvo_Curso() || getAvaliacao().getPublicoAlvo_Turma())){
				if (getUsuarioLogado().getTipoPessoa().equals("PR")) {
					getListaConsultaProfessorSelecionados().add(getUsuarioLogado().getPessoa());
				}
				if (getUsuarioLogado().getTipoPessoa().equals("CO")) {
					List<CursoCoordenadorVO> CursoCoordenadorVOs = getFacadeFactory().getCursoCoordenadorFacade().consultarPorCodigoCursoECodigoUnidadeEnsino(Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());					
					 questionarioRelVOs = getFacadeFactory().getAvaliacaoInstitucionalRelFacade().consultarQuantidadeRelatorioSerGeradoVisaoCoordenador(getTipoRelatorioGerar().equals("gerarRelatorioPorRespondentePDF"), getCodigoUnidadeEnsino() , getAvaliacao(), getNivelDetalhamento(), CursoCoordenadorVOs, avaliacaoInstucionalRelVO);
					
				}else{
				 questionarioRelVOs = getFacadeFactory().getAvaliacaoInstitucionalRelFacade().consultarQuantidadeRelatorioSerGerado(getTipoRelatorioGerar().equals("gerarRelatorioPorRespondentePDF"), getAvaliacao(), getNivelDetalhamento(), getQuestionario(), getListaPerguntasSelecionadas(), getCodigoUnidadeEnsino(), getCurso(), getTurno(), getTurma(), getListaDisciplinasSelecionadas(), avaliacaoInstucionalRelVO, false, getListaConsultaProfessorSelecionados(), getListaConsultaRespondenteSelecionados(), getDataInicio(), getDataFim());
				}
				int qtde = 0;
				/*if(questionarioRelVOs.isEmpty()){
					throw new IllegalArgumentException();
				}*/
				for(QuestionarioRelVO questionarioRelVO: questionarioRelVOs){
					if(getTipoRelatorioGerar().equals("gerarRelatorioPorRespondentePDF")){
						questionarioRelVO.setQtdeRelatorio(questionarioRelVO.getQtdeRespondentes());
						qtde += questionarioRelVO.getQtdeRespondentes();
					}else{
						qtde += questionarioRelVO.getQtdeRelatorio();
					}
				}
				iniciarProgressBar = qtde <= 10;
				if(!iniciarProgressBar){
					inicializarProgressBarModalQuestionarioSelecionar(questionarioRelVOs);
				}
			}
			if(iniciarProgressBar && getTotalResposta() > 0){
				inicializarProgressBar();
			}
			if (getTotalResposta() == 0){
				throw new IllegalArgumentException();
			}
		} catch(IllegalArgumentException e){
			setMensagemID("msg_relatorio_vazio", Uteis.ERRO,true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void inicializarProgressBar() {
		setFazerDownload(false);
		setCaminhoRelatorio("");
		getProgressBarVO().resetar();
		getProgressBarVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
		try {
			getProgressBarVO().setCaminhoWebRelatorio(UteisJSF.getCaminhoWebRelatorio());
		} catch (Exception e) {
			
		}
		getProgressBarVO().setUrlAplicacaoExterna(UteisJSF.getUrlAplicacaoExterna());
		getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
		getProgressBarVO().setOncomplete("");
		getProgressBarVO().iniciar(0l, getTotalResposta(), "Consultando Dados...", true, this, "realizarGeracaoRelatorioProgressBar");
		
	}
	

	public Boolean getApresentarFiltroRespondente() {
		if (apresentarFiltroRespondente == null) {
			apresentarFiltroRespondente = Boolean.FALSE;			
		}
		return apresentarFiltroRespondente;
	}

	public void setApresentarFiltroRespondente(Boolean apresentarFiltroRespondente) {
		this.apresentarFiltroRespondente = apresentarFiltroRespondente;
	}

	public List<PessoaVO> getListaConsultaRespondente() {
		if (listaConsultaRespondente == null) {
			listaConsultaRespondente = new ArrayList<PessoaVO>(0);
		}
		return listaConsultaRespondente;
	}

	public void setListaConsultaRespondente(List<PessoaVO> listaConsultaRespondente) {
		this.listaConsultaRespondente = listaConsultaRespondente;
	}

	public List<PessoaVO> getListaConsultaRespondenteSelecionados() {
		if (listaConsultaRespondenteSelecionados == null) {
			listaConsultaRespondenteSelecionados = new ArrayList<PessoaVO>(0);
		}
		return listaConsultaRespondenteSelecionados;
	}

	public void setListaConsultaRespondenteSelecionados(List<PessoaVO> listaConsultaRespondenteSelecionados) {
		this.listaConsultaRespondenteSelecionados = listaConsultaRespondenteSelecionados;
	}
	
	public void consultarRespondentes() {
		try {
			Integer questionarioPrm = 0;
			List<PerguntaVO> listaPerguntasSelecionadasPrm = new ArrayList<PerguntaVO>(0);
			Integer turmaPrm = 0;
			List<DisciplinaVO> listaDisciplinasSelecionadasPrm = new ArrayList<DisciplinaVO>(0);			
			Integer turnoPrm = 0;
			if (getAvaliacao().getCodigo().intValue() != 0) {
				if (filtrarQuestionario) {
					questionarioPrm = questionario;
				}
				if (filtrarPergunta && filtrarQuestionario) {
					listaPerguntasSelecionadasPrm = listaPerguntasSelecionadas;
				}
			}
			if (filtrarTurno) {
				turnoPrm = turno;
			}
			if (filtrarCurso) {
				if (curso.intValue() != 0) {
					if (filtrarTurma) {
						turmaPrm = turma;
					}
					if (filtrarDisciplina) {
						listaDisciplinasSelecionadasPrm = listaDisciplinasSelecionadas;
					}
				}
			}
			List<PessoaVO> respondentes = (getFacadeFactory().getAvaliacaoInstitucionalRelFacade().consultarRespondente(getAvaliacao().getCodigo(), questionarioPrm, listaPerguntasSelecionadasPrm, codigoUnidadeEnsino, curso, turnoPrm, turmaPrm, listaDisciplinasSelecionadasPrm));
			if(!getListaConsultaRespondenteSelecionados().isEmpty() && !getListaConsultaRespondente().isEmpty()){
				getListaConsultaRespondenteSelecionados().clear();
				for (PessoaVO pessoaVO : getListaConsultaRespondente()) {
					if (pessoaVO.getSelecionado() && respondentes.contains(pessoaVO)) {
						respondentes.get(respondentes.indexOf(pessoaVO)).setSelecionado(true);
						getListaConsultaRespondenteSelecionados().add(pessoaVO);
					}
				}
			}
			setListaConsultaRespondente(respondentes);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaRespondente(new ArrayList<PessoaVO>(0));

			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarRespondente() throws Exception {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("respondente");
			if(obj == null){
				if(getSelecionarTudo()){
					for(PessoaVO pessoaVO:getListaConsultaRespondente()){
						pessoaVO.setSelecionado(true);
						getListaConsultaRespondenteSelecionados().add(pessoaVO);
					}
				}else{
					limparRespondente();
				}
			}else{			
				if (obj.getSelecionado()) {
					getListaConsultaRespondenteSelecionados().add(obj);				
				} else {
					getListaConsultaRespondenteSelecionados().remove(obj);
				}
			}
			
		} catch (Exception e) {
		}
	}
	
	public void limparRespondente() {
		for(PessoaVO pessoaVO: getListaConsultaRespondenteSelecionados()){
			pessoaVO.setSelecionado(false);
		}
		getListaConsultaRespondenteSelecionados().clear();
	}

	public List<QuestionarioRelVO> getQuestionarioRelVOs() {
		if(questionarioRelVOs == null){
			questionarioRelVOs = new ArrayList<QuestionarioRelVO>(0);
		}
		return questionarioRelVOs;
	}

	public void setQuestionarioRelVOs(List<QuestionarioRelVO> questionarioRelVOs) {
		this.questionarioRelVOs = questionarioRelVOs;
	}

	public Boolean getGerarRelatorioQuestionarioSelecionado() {
		if(gerarRelatorioQuestionarioSelecionado == null){
			gerarRelatorioQuestionarioSelecionado = false;
		}
		return gerarRelatorioQuestionarioSelecionado;
	}

	public void setGerarRelatorioQuestionarioSelecionado(Boolean gerarRelatorioQuestionarioSelecionado) {
		this.gerarRelatorioQuestionarioSelecionado = gerarRelatorioQuestionarioSelecionado;
	}

	public QuestionarioRelVO getQuestionarioRelSelecionadoVO() {
		if(questionarioRelSelecionadoVO == null){
			questionarioRelSelecionadoVO = new QuestionarioRelVO();
		}
		return questionarioRelSelecionadoVO;
	}

	public void setQuestionarioRelSelecionadoVO(QuestionarioRelVO questionarioRelSelecionadoVO) {
		this.questionarioRelSelecionadoVO = questionarioRelSelecionadoVO;
	}
	
	public boolean isApresentarRespostaPorAgrupamento() {
		return apresentarRespostaPorAgrupamento;
	}

	public void setApresentarRespostaPorAgrupamento(boolean apresentarRespostaPorAgrupamento) {
		this.apresentarRespostaPorAgrupamento = apresentarRespostaPorAgrupamento;
	}

	public String getRespondente() {
		int qtde = getListaConsultaRespondenteSelecionados().size();
		if (qtde > 1) {
			return qtde + " Respondentes Selecionados";
		} else {
			return qtde + " Respondente Selecionado";
		}		
	}
	
	public void limparFiltrosGeral(){
		limparCurso();
		setTurno(0);
		limparRespondente();
		limparDisciplina();
		limparTurma();				
	}

	public void realizarImpressaoRelatorioRespondentes() {
		try {
			List<AvaliacaoInstitucionalAnaliticoRelVO> listaResultado = null;
			if(getAvaliacao().getSituacao().equals("AT") || getAvaliacao().getSituacao().equals("FI")) {
				setUtilizarListagemRespondente(Boolean.TRUE);
			}
			else {
				setUtilizarListagemRespondente(Boolean.FALSE);
			}
			listaResultado = getFacadeFactory().getAvaliacaoInstitucionalAnaliticoRelFacade().realizarGeracaoRelatorioAnalitico(getCodigoUnidadeEnsino(), getAvaliacao(), getCurso(), getTurno() ,getTurma(), "TODAS", "unidadeensino.nome", getDataInicio(), getDataFim(), getUtilizarListagemRespondente(), getUsuarioLogado(), false);
			if (!listaResultado.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport("relatorio" + File.separator + "designRelatorio" + File.separator + "avaliacaoInstitucional" + File.separator + "ListagemRespondenteAvaliacaoInstitucionalAnaliticoRel.jrxml");
				getSuperParametroRelVO().setSubReport_Dir("relatorio" + File.separator + "designRelatorio" + File.separator + "avaliacaoInstitucional");
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio(UteisJSF.internacionalizar("prt_AvaliacaoInstitucionalAnaliticoRel_tituloForm"));
				getSuperParametroRelVO().setListaObjetos(listaResultado);
				getSuperParametroRelVO().setCaminhoBaseRelatorio("relatorio" + File.separator + "designRelatorio" + File.separator + "avaliacaoInstitucional");
				getSuperParametroRelVO().setNomeEmpresa(getUnidadeEnsinoLogado().getNome());
				getSuperParametroRelVO().setVersaoSoftware("");
				getSuperParametroRelVO().setFiltros("");
				getSuperParametroRelVO().adicionarParametro("avaliacaoInstitucional", getAvaliacao().getNome());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				if (!getAvaliacao().getPublicoAlvo_FuncionarioGestor()) {
					if (getCodigoUnidadeEnsino() > 0) {
						UnidadeEnsinoVO obj = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getCodigoUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
						getSuperParametroRelVO().adicionarParametro("unidadeEnsino", obj.getNome());
					} else {
						getSuperParametroRelVO().adicionarParametro("unidadeEnsino", "Todas");
					}

					if (getCurso() > 0) {
						CursoVO obj = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getCurso(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
						getSuperParametroRelVO().adicionarParametro("curso", obj.getNome());
					} else {
						getSuperParametroRelVO().adicionarParametro("curso", "Todos");
					}
					if (getTurno() > 0) {
						TurnoVO obj = getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(getTurno(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
						getSuperParametroRelVO().adicionarParametro("turno", obj.getNome());
					} else {
						getSuperParametroRelVO().adicionarParametro("turno", "Todos");
					}
					if (getTurma() > 0) {
						TurmaVO obj = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurma(),Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
						getSuperParametroRelVO().adicionarParametro("turma", obj.getIdentificadorTurma());
					} else {
						getSuperParametroRelVO().adicionarParametro("turma", "Todas");
					}
				}
				getSuperParametroRelVO().adicionarParametro("aluno", getAvaliacao().getPublicoAlvo_Curso() || getAvaliacao().getPublicoAlvo_TodosCursos() || getAvaliacao().getPublicoAlvo_Turma());
				getSuperParametroRelVO().adicionarParametro("funcionario", getAvaliacao().getPublicoAlvo_FuncionarioGestor());
				getSuperParametroRelVO().adicionarParametro("situacaoResposta", "Todas" );
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok", Uteis.SUCESSO,true);
			}else {
				setMensagemID("msg_relatorio_sem_dados", Uteis.ERRO,true);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	

	public NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum getNivelDetalhamento() {
		if(nivelDetalhamento == null){
			nivelDetalhamento = NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.AVALIADO;
		}
		return nivelDetalhamento;
	}

	public void setNivelDetalhamento(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamento) {
		this.nivelDetalhamento = nivelDetalhamento;
	}
	
	public void inicializarListaNivelDetalhamento(){
		listaSelectItemNivelDetalhamento =  new ArrayList<SelectItem>(0);
		if(getAvaliacao().getAvaliacaoUltimoModulo() && getAvaliacao().getPublicoAlvo_Professor()) {
			listaSelectItemNivelDetalhamento.add(new SelectItem(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.GERAL, "Geral"));
			listaSelectItemNivelDetalhamento.add(new SelectItem(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.CURSO, "Curso"));
			listaSelectItemNivelDetalhamento.add(new SelectItem(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.TURMA, "Turma"));
			listaSelectItemNivelDetalhamento.add(new SelectItem(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.AVALIADO, "Disciplina"));
		}else {
			for(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamentoResultadoAvaliacaoInstitucionalEnum: NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.values()){
				if(nivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.getIsPermiteDatalhar(PublicoAlvoAvaliacaoInstitucional.getEnum(getAvaliacao().getPublicoAlvo()), getAvaliacao().getQuestionarioVO().getEscopo().equals("GE"))){
					listaSelectItemNivelDetalhamento.add(new SelectItem(nivelDetalhamentoResultadoAvaliacaoInstitucionalEnum, nivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.getDescricao(getAvaliacao())));
				}				
			}
		}
		if (nivelDetalhamento == null) {
			setNivelDetalhamento(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.AVALIADO);
		}
	}

	public List<SelectItem> getListaSelectItemNivelDetalhamento() {
		if(listaSelectItemNivelDetalhamento == null){
			inicializarListaNivelDetalhamento();
		}
		return listaSelectItemNivelDetalhamento;
	}

	public void setListaSelectItemNivelDetalhamento(List<SelectItem> listaSelectItemNivelDetalhamento) {
		this.listaSelectItemNivelDetalhamento = listaSelectItemNivelDetalhamento;
	}
	

	public Boolean getGerarRelatorioPublicacao() {
		if(gerarRelatorioPublicacao == null){
			gerarRelatorioPublicacao =  false;
		}
		return gerarRelatorioPublicacao;
	}

	public void setGerarRelatorioPublicacao(Boolean gerarRelatorioPublicacao) {
		this.gerarRelatorioPublicacao = gerarRelatorioPublicacao;
	}
	

	public void carregarAvaliacaoPublicada(){
		setGerarRelatorioPublicacao(true);
		MatriculaVO matricula = null;
		if((getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) && getVisaoAlunoControle() != null){
			matricula = getVisaoAlunoControle().getMatricula();
		}
		try {
			setListaConsulta(getFacadeFactory().getAvaliacaoInstitucionalFacade().consultarAvaliacaoInstitucionalHabilitadoApresentacaoAvaliado(getUnidadeEnsinoLogado(), getUsuarioLogado(), matricula));
			if(!getListaConsulta().isEmpty()){
				carregarDadosAvaliacaoInstitucional((AvaliacaoInstitucionalVO)getListaConsulta().get(0), true);
			}
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void carregarDadosAvaliacaoInstitucional(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, Boolean consultarGrafico) throws Exception {
		inicializarDados();		
		setAvaliacao(avaliacaoInstitucionalVO);
		getAvaliacao().setAvaliacaoInstitucionalCursoVOs(getFacadeFactory().getAvaliacaoInstitucionalCurso().consultarPorAvaliacaoInstitucional(avaliacaoInstitucionalVO.getCodigo(), getUsuarioLogado()));
		executarVerificarApresentarFiltroProfessor();		
		montarListaQuestionarios();
		inicializarListaNivelDetalhamento();
		montarListaUnidadesEnsino();
		if(!getAvaliacao().getAvaliacaoUltimoModulo()){
			setDataFim(getAvaliacao().getDataFinal());
			setDataInicio(getAvaliacao().getDataInicio());
		}else {
			setDataInicio(Uteis.obterDataAntiga(new Date(), getAvaliacao().getDiasDisponivel()));
			setDataFim(new Date());
		}
		
		setApresentarFiltroRespondente(true);
		setNomeAvaliacao(getAvaliacao().getNome());				
		this.setValorConsultaAvaliacao(null);
		this.setCampoConsultaAvaliacao(null);
		getListaConsultaCurso().clear();
		if(!getAvaliacao().getAvaliacaoInstitucionalCursoVOs().isEmpty()) {
			for(AvaliacaoInstitucionalCursoVO avaliacaoInstitucionalCursoVOs : getAvaliacao().getAvaliacaoInstitucionalCursoVOs()) {
				getListaConsultaCurso().add(avaliacaoInstitucionalCursoVOs.getCursoVO());
			}
		}
		if(consultarGrafico){
			setNivelDetalhamento(getAvaliacao().getNivelDetalhamentoPublicarResultado());
			gerarRelatorioGrafico();
		}
	}
	
	public Integer getColunas(){
		return getListaConsulta().size() > 3 ? 3 :getListaConsulta().size(); 
	}	
	
	public synchronized void gerarRelatorioPorTipo(TipoRelatorio tipoRelatorio, Integer unidadeEnsino, Integer curso, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, String caminhoWebRelatorio, String urlAplicacaoExterna) throws Exception {
		List<AvaliacaoInstucionalRelVO> lista = new ArrayList<AvaliacaoInstucionalRelVO>(0);
		AvaliacaoInstucionalRelVO avaliacaoInstucionalRelVOs = null;
		try {
			if (tipoRelatorio.equals(TipoRelatorio.GRAFICO)) {
				avaliacaoInstucionalRelVOs = getFacadeFactory().getAvaliacaoInstitucionalRelFacade().emitirRelatorioGrafico(getNivelDetalhamento(), getAvaliacao().getPublicarAlvoEnum(), getAvaliacao(), getQuestionario(), getListaPerguntasSelecionadas(), unidadeEnsino, curso, getTurno(), getTurma(), getListaDisciplinasSelecionadas(), getListaConsultaProfessorSelecionados(),  getListaConsultaRespondenteSelecionados(), getDataInicio(), getDataFim(), isApresentarRespostaPorAgrupamento(), getGerarRelatorioPublicacao(), usuarioVO, null);
			} else {
				avaliacaoInstucionalRelVOs = getFacadeFactory().getAvaliacaoInstitucionalRelFacade().emitirRelatorio(getNivelDetalhamento(), getAvaliacao().getPublicarAlvoEnum(), getAvaliacao(), getQuestionario(), getListaPerguntasSelecionadas(), unidadeEnsino, curso, getTurno(), getTurma(), getListaDisciplinasSelecionadas(), getListaConsultaProfessorSelecionados(), getListaConsultaRespondenteSelecionados(),  getDataInicio(), getDataFim(), isApresentarRespostaPorAgrupamento());
			}
			if (avaliacaoInstucionalRelVOs != null && !avaliacaoInstucionalRelVOs.getQuestionarioRelVOs().isEmpty()) {
				lista.add(avaliacaoInstucionalRelVOs);
				getSuperParametroRelVO().adicionarParametro("periodo", Uteis.periodoEntreDatas(getDataInicio(), getDataFim(), "dd/MM/YY"));
				getSuperParametroRelVO().setSubReport_Dir(AvaliacaoInstitucionalRel.getCaminhoBaseRelatorio());
				if (tipoRelatorio.equals(TipoRelatorio.EXCEL)) {
					getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
				} else {
					getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);					
				}
				getSuperParametroRelVO().setNomeUsuario(usuarioVO.getNome());
				getSuperParametroRelVO().setTituloRelatorio("Avaliação Institucional");
				getSuperParametroRelVO().setCaminhoBaseRelatorio(AvaliacaoInstitucionalRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeEmpresa(getUnidadeEnsinoLogado().getNome());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");
				if (getRelatorioGeral()) {
					if (tipoRelatorio.equals(TipoRelatorio.GRAFICO)) {
						getSuperParametroRelVO().setNomeDesignIreport(AvaliacaoInstitucionalRel.getDesignIReportRelatorioGrafico());
					}else if (tipoRelatorio.equals(TipoRelatorio.EXCEL)) {
						getSuperParametroRelVO().setNomeDesignIreport(AvaliacaoInstitucionalRel.getDesignIReportRelatorioGeralExcel());
					} else {
						getSuperParametroRelVO().setNomeDesignIreport(AvaliacaoInstitucionalRel.getDesignIReportRelatorioGeral());
					}
					
					getSuperParametroRelVO().setListaObjetos(lista);
					realizarImpressaoRelatorio();
					setCaminhoRelatorio("location.href='" + urlAplicacaoExterna + "/DownloadRelatorioSV?relatorio=" + getCaminhoRelatorio() + "'");
					getProgressBarVO().setProgresso(getProgressBarVO().getMaxValue().longValue());
				} else {
					if (tipoRelatorio.equals(TipoRelatorio.GRAFICO)) {
						getSuperParametroRelVO().setNomeDesignIreport(AvaliacaoInstitucionalRel.getDesignIReportRelatorioGrafico());
					}else if (tipoRelatorio.equals(TipoRelatorio.EXCEL)) {
						getSuperParametroRelVO().setNomeDesignIreport(AvaliacaoInstitucionalRel.getDesignIReportRelatorioGeralExcel());
					} else {
						getSuperParametroRelVO().setNomeDesignIreport(AvaliacaoInstitucionalRel.getDesignIReportRelatorio());
					}
					if (avaliacaoInstucionalRelVOs.getQuestionarioRelVOs().size() > 10){
						getProgressBarVO().setMaxValue(avaliacaoInstucionalRelVOs.getQuestionarioRelVOs().size() + 3);
						getProgressBarVO().setProgresso(1l);
						realizarGeracaoRelatorioFormatoLink(getSuperParametroRelVO(), lista, usuarioVO, configuracaoGeralSistemaVO, caminhoWebRelatorio, urlAplicacaoExterna);
					} else{
						getSuperParametroRelVO().setListaObjetos(lista);
						realizarImpressaoRelatorio();						
						setCaminhoRelatorio("location.href='" + urlAplicacaoExterna + "/DownloadRelatorioSV?relatorio=" + getCaminhoRelatorio() + "'");
						getProgressBarVO().setProgresso(getProgressBarVO().getMaxValue().longValue());
					}
				}
				if (context() != null) {
					setMensagemID("msg_relatorio_gerado", Uteis.SUCESSO,true);
				}
			} else {
				getProgressBarVO().setForcarEncerramento(true);
				setMensagemID("msg_relatorio_vazio", Uteis.ERRO,true);
			}
		} catch (Exception e) {
			getProgressBarVO().setForcarEncerramento(true);
			getProgressBarVO().setException(e);						
			if(context() != null) {
				setMensagemDetalhada("msg_erro", getMensagemInternalizacao("msg_relatorio_erro"));
			}
		} finally {
			Uteis.liberarListaMemoria(lista);
			avaliacaoInstucionalRelVOs = null;
		}
	}

	private enum TipoRelatorio{
		GRAFICO, NORMAL, EXCEL;
	}
	
	void inicializarProgressBarModalQuestionarioSelecionar(List<QuestionarioRelVO> questionarioRelVOs) throws Exception {
		setFazerDownload(false);
		setCaminhoRelatorio("");
		getProgressBarVO().resetar();
		getProgressBarVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
		getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
		getProgressBarVO().iniciar(0l, 1000000, "Abrindo modal Questionários...", true, this, "getAbrirModalQuestionarioSelecionar");
		getProgressBarVO().setForcarEncerramento(true);
		setQuestionarioRelVOs(questionarioRelVOs);
	}
	
	public Boolean getUtilizarListagemRespondente() {
		if(utilizarListagemRespondente == null) {
			utilizarListagemRespondente = Boolean.FALSE;
		}
		return utilizarListagemRespondente;
	}

	public void setUtilizarListagemRespondente(Boolean utilizarListagemRespondente) {
		this.utilizarListagemRespondente = utilizarListagemRespondente;
	}

	public Integer getTotalResposta() {
		if(totalResposta == null) {
			totalResposta = 0;
		}
		return totalResposta;
	}

	public void setTotalResposta(Integer totalResposta) {
		this.totalResposta = totalResposta;
	}
}