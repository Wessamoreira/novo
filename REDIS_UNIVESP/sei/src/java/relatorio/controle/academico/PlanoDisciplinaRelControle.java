package relatorio.controle.academico;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.SituacaoPlanoEnsinoEnum;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import relatorio.arquitetura.GeradorRelatorio;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.PlanoDisciplinaRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.PlanoDisciplinaRel;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;

@SuppressWarnings("unchecked")
@Controller("PlanoDisciplinaRelControle")
@Scope("viewScope")
@Lazy
public class PlanoDisciplinaRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = -2573843298794316986L;

	private CursoVO cursoVO;
	private TurmaVO turmaVO;
	private DisciplinaVO disciplinaVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private GradeCurricularVO gradeCurricularVO;
	private PessoaVO professorResponsavel;

	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private List<DisciplinaVO> listaConsultaDisciplina;

	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<CursoVO> listaConsultaCurso;

	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;

	private List<SelectItem> tipoConsultaComboCurso;
	private List<SelectItem> listaSelectItemSemestre;
	private List<SelectItem> listaSelectItemSituacaoCons;
	private List<SelectItem> listaSelectItemPeriodicidade;	
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemGradeCurricular;

	private String tipoLayout;
	private String ano;
	private String semestre;
	private String situacao;
	private PeriodicidadeEnum periodicidade;

	private String valorConsultaProfessor;
	private String campoConsultaProfessor;
	private List<FuncionarioVO> listaConsultaProfessor;

	public PlanoDisciplinaRelControle() {
		inicializarDadosListaSelectItemUnidadelEnsino();
	}
	
	public enum tipoLayoutEnumPlanoDisciplina {
		ANALITICO("ANALITICO", "Analítico"), 
		SINTETICO("SINTETICO", "Sintético");
		
		private tipoLayoutEnumPlanoDisciplina(String valor, String descricao) {
			this.valor = valor;
			this.descricao = descricao;
		}
		
		public static tipoLayoutEnumPlanoDisciplina getEnum(String valor) {
			tipoLayoutEnumPlanoDisciplina[] valores = values();
			for (tipoLayoutEnumPlanoDisciplina obj : valores) {
				if (obj.getValor().equals(valor)) {
					return obj;
				}
			}
			return null;
		}

		String valor;
		String descricao;

		public String getValor() {
			if (valor == null) {
				valor = "";
			}
			return valor;
		}

		public void setValor(String valor) {
			this.valor = valor;
		}

		public String getDescricao() {
			if (descricao == null) {
				descricao = "";
			}
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
	}

	public void imprimirPDF() {
		List<PlanoEnsinoVO> listaPlanoEnsino = new ArrayList<PlanoEnsinoVO>(0);
		List<File> files = new ArrayList<File>(0);
		List<PlanoEnsinoVO> listaPlanoEnsinoComLink =  new ArrayList<PlanoEnsinoVO>(0);

		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "PlanoDisciplinaRelControle", "Inicializando Geração de Relatório Plano da Disciplina", "Emitindo Relatório");
			listaPlanoEnsino = getFacadeFactory().getPlanoDisciplinaRelFacade().criarObjeto(getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), getTurmaVO().getCodigo(),
					getGradeCurricularVO().getCodigo(), getDisciplinaVO().getCodigo(), getAno(), getSemestre(), getUsuarioLogado(),
					getProfessorResponsavel(), getPeriodicidade(), getSituacao(), getConfiguracaoGeralPadraoSistema().getQuestionarioPlanoEnsino().getCodigo());
			
			if (!Uteis.isAtributoPreenchido(listaPlanoEnsino)) {
				setMensagemID("msg_relatorio_sem_dados");
				return;
			}
			if (getTipoLayout().equals(tipoLayoutEnumPlanoDisciplina.ANALITICO.name())) {
				gerarRelatorioAnalitico(listaPlanoEnsino, files, listaPlanoEnsinoComLink);
			} else {
				gerarRelatorioSintetico(listaPlanoEnsino);
			}
			
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private void gerarRelatorioSintetico(List<PlanoEnsinoVO> listaPlanoEnsino) throws Exception, IOException {
		List<PlanoDisciplinaRelVO> listaPlanoDisciplinaRelVO = new ArrayList<>(0);
		for (PlanoEnsinoVO planoEnsinoVO : listaPlanoEnsino) {
			planoEnsinoVO = getFacadeFactory().getPlanoEnsinoFacade().consultarPorChavePrimaria(planoEnsinoVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
			getSuperParametroRelVO().setUnidadeEnsino(planoEnsinoVO.getUnidadeEnsino().getNome());
			
			getSuperParametroRelVO().setCurso(planoEnsinoVO.getCurso().getNome());
			listaPlanoDisciplinaRelVO.add(getFacadeFactory().getPlanoDisciplinaRelFacade().realizarGeracaoRelatorioPlanoEnsinoSintetico(planoEnsinoVO, getUsuarioLogado()));
		}
		if(!listaPlanoDisciplinaRelVO.isEmpty()) {
			String caminho = PlanoDisciplinaRel.getCaminhoBaseRelatorio();
			String design = PlanoDisciplinaRel.getDesignIReportRelatorioSintetico();

			if (Uteis.isAtributoPreenchido(getGradeCurricularVO())) {
				setGradeCurricularVO(getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(getGradeCurricularVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				getSuperParametroRelVO().setGradeCurricular(getGradeCurricularVO().getNome());
			}

			if (Uteis.isAtributoPreenchido(getDisciplinaVO().getCodigo())) {
				getSuperParametroRelVO().setDisciplina(getDisciplinaVO().getNome());
			}
			if (Uteis.isAtributoPreenchido(getProfessorResponsavel())) {
				getSuperParametroRelVO().adicionarParametro("professorResponsavel", getProfessorResponsavel().getNome());
			}
			if (Uteis.isAtributoPreenchido(getPeriodicidade())) {
				getSuperParametroRelVO().adicionarParametro("periodicidade", getPeriodicidade().getDescricao());
			}
			if (Uteis.isAtributoPreenchido(getSituacao())) {
				getSuperParametroRelVO().adicionarParametro("situacao", SituacaoPlanoEnsinoEnum.getEnum(getSituacao()).getDescricao());
			}
			
			getSuperParametroRelVO().setNomeDesignIreport(design);
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(caminho);
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio("Plano de Ensino");
			getSuperParametroRelVO().setListaObjetos(listaPlanoDisciplinaRelVO);
			getSuperParametroRelVO().setCaminhoBaseRelatorio(caminho);
			getSuperParametroRelVO().setNomeEmpresa("");
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setFiltros("");
			realizarImpressaoRelatorio();	
		}
	}

	private void gerarRelatorioAnalitico(List<PlanoEnsinoVO> listaPlanoEnsino, List<File> files,
			List<PlanoEnsinoVO> listaPlanoEnsinoComLink) throws Exception, IOException {

		List<PlanoDisciplinaRelVO> listaPlanoDisciplinaRelVO = new ArrayList<>(0);
		for (PlanoEnsinoVO planoEnsinoVO : listaPlanoEnsino) {
			planoEnsinoVO = getFacadeFactory().getPlanoEnsinoFacade().consultarPorChavePrimaria(planoEnsinoVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
			getFacadeFactory().getPlanoEnsinoFacade().preencherDadosPlanoEnsinoQuestionarioRespostaOrigem(planoEnsinoVO, getConfiguracaoGeralPadraoSistema().getQuestionarioPlanoEnsino().getCodigo(), getUsuarioLogado());
			planoEnsinoVO.setGradeCurricular(getGradeCurricularVO());
			listaPlanoDisciplinaRelVO = getFacadeFactory().getPlanoDisciplinaRelFacade().realizarGeracaoRelatorioPlanoEnsino(planoEnsinoVO, getUsuarioLogado());
			if(!listaPlanoDisciplinaRelVO.isEmpty()) {
				String caminho = PlanoDisciplinaRel.getCaminhoBaseRelatorio();
				String design = "";
				
				if (Uteis.isAtributoPreenchido(planoEnsinoVO.getQuestionarioRespostaOrigemVO())) {
					design = PlanoDisciplinaRel.getDesignIReportRelatorio("PlanoDisciplinaFormularioQuestionarioRel");
				} else {
					design = PlanoDisciplinaRel.getDesignIReportRelatorio();
				}

				getSuperParametroRelVO().setUnidadeEnsino(planoEnsinoVO.getUnidadeEnsino().getNome());
				getSuperParametroRelVO().setGradeCurricular(planoEnsinoVO.getGradeCurricular().getNome());
				getSuperParametroRelVO().setCurso(planoEnsinoVO.getCurso().getNome());
				getSuperParametroRelVO().setTurma("");
				getSuperParametroRelVO().setDisciplina(planoEnsinoVO.getDisciplina().getNome());
				if (Uteis.isAtributoPreenchido(getProfessorResponsavel())) {
					getSuperParametroRelVO().adicionarParametro("professorResponsavel", getProfessorResponsavel().getNome());
				}
				if (Uteis.isAtributoPreenchido(getPeriodicidade())) {
					getSuperParametroRelVO().adicionarParametro("periodicidade", getPeriodicidade().getDescricao());
				}
				if (Uteis.isAtributoPreenchido(getSituacao())) {
					getSuperParametroRelVO().adicionarParametro("situacao", SituacaoPlanoEnsinoEnum.getEnum(getSituacao()).getDescricao());
				}

				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(caminho);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Plano de Ensino");
				getSuperParametroRelVO().setListaObjetos(listaPlanoDisciplinaRelVO);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(caminho);
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");
				getSuperParametroRelVO().setNomeEspecificoRelatorio("Codigo_" + planoEnsinoVO.getCodigo() +"_" + Uteis.removerCaracteresEspeciais3(planoEnsinoVO.getDescricao()));
				realizarImpressaoRelatorio();	

				String link = getCaminhoRelatorio();
				planoEnsinoVO.setLink(link);				
				planoEnsinoVO.setNomeLink("Codigo_" + planoEnsinoVO.getCodigo() +"_" + Uteis.removerCaracteresEspeciais3(planoEnsinoVO.getDescricao()));
				listaPlanoEnsinoComLink.add(planoEnsinoVO);
				files.add(new File(getCaminhoPastaWeb() + "relatorio" + File.separator +getCaminhoRelatorio()));
			}
		}

		if(files.size() > 1) {
			
			getSuperParametroRelVO().setListaObjetos(listaPlanoEnsinoComLink);
			getSuperParametroRelVO().setNomeDesignIreport(PlanoDisciplinaRel.getDesignIReportRelatorioLink());				
			GeradorRelatorio.realizarExportacaoPDF(getSuperParametroRelVO(), getCaminhoPastaWeb().replaceAll("|", ""), "Indice.pdf");
			files.add(new File(getCaminhoPastaWeb()+File.separator+"Indice.pdf"));	
			
			String nomeNovoArquivo = "Relatorio_Plano_Ensino";
			
			nomeNovoArquivo += ".zip";
			File[] filesArray = new File[files.size()];
			getFacadeFactory().getArquivoHelper().zip(files.toArray(filesArray), new File( getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + nomeNovoArquivo));
			for(File file : files) {
			   file.delete();
		    }
			Uteis.liberarListaMemoria(files);
			setCaminhoRelatorio(nomeNovoArquivo);					
		}
	}

	public void inicializarDadosListaSelectItemUnidadelEnsino() {
		try {
			List<UnidadeEnsinoVO> listaResultado = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome("", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(listaResultado, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCurso() {
		try {
			List<CursoVO> objs = new ArrayList<>(0);
			if (getCampoConsultaCurso().equals("nome") && !getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeEUnidadeDeEnsino(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			} else if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				objs = getFacadeFactory().getCursoFacade().consultarPorNomeCoordenador(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), getUsuarioLogado().getPessoa().getCodigo());				
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarProfessor() {
		try {
			super.consultar();
			List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
			if (getCampoConsultaProfessor().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaProfessor(), 
						TipoPessoa.PROFESSOR.getValor(), getUnidadeEnsinoVO().getCodigo(), 
						false, 0, getUsuarioLogado());
			}
			if (getCampoConsultaProfessor().equals("cpf")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaProfessor(), 
						TipoPessoa.PROFESSOR.getValor(), getUnidadeEnsinoVO().getCodigo(), 
						false, 0, getUsuarioLogado());
			}
			setListaConsultaProfessor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaProfessor(new ArrayList<FuncionarioVO>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			setCursoVO(obj);
			montarListaSelectItemGradeCurricular();
			setDisciplinaVO(null);
			setTurmaVO(null);
			setListaConsultaDisciplina(null);
			setPeriodicidade(PeriodicidadeEnum.getEnumPorValor(getCursoVO().getPeriodicidade()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparCurso() throws Exception {
		try {
			setCursoVO(null);
			montarListaSelectItemGradeCurricular();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboCurso;
	}

	public void consultarTurma() {
		try {
			List<TurmaVO> objs = new ArrayList<>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnidadeEnsinoCursoTurno(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), 0, false, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarTurma() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
		setTurmaVO(obj);
		setCursoVO(getTurmaVO().getCurso());
		montarListaSelectItemGradeCurricular();
		removerObjetoMemoria(getDisciplinaVO());
		obj = null;
		setValorConsultaTurma(null);
		setCampoConsultaTurma(null);
		Uteis.liberarListaMemoria(getListaConsultaTurma());
	}

	public void limparTurma() throws Exception {
		try {
			removerObjetoMemoria(getTurmaVO());
			montarListaSelectItemGradeCurricular();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private List<SelectItem> tipoConsultaComboTurma;

	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
		}
		return tipoConsultaComboTurma;
	}

	private List<SelectItem> tipoConsultaComboDisciplina;

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if (tipoConsultaComboDisciplina == null) {
			tipoConsultaComboDisciplina = new ArrayList<>(0);
			tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboDisciplina;
	}

	public void consultarDisciplina() {
		try {
			setListaConsultaDisciplina(getFacadeFactory().getPlanoDisciplinaRelFacade().consultarDisciplina(getGradeCurricularVO().getCodigo(), getCampoConsultaDisciplina(), getValorConsultaDisciplina(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

		}
	}

	public void selecionarDisciplina() throws Exception {
		try {
			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
			setDisciplinaVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparDisciplina() throws Exception {
		try {
			setDisciplinaVO(null);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparDadosProfessor() {
		setProfessorResponsavel(new PessoaVO());
		setListaConsultaProfessor(new ArrayList<>(0));
		setCampoConsultaProfessor("");
		setValorConsultaProfessor("");
	}
	
	public void selecionarProfessor() {
		try {
			FuncionarioVO professorSelecionado = (FuncionarioVO) context().getExternalContext().getRequestMap().get("professorVOItens");
			if (Uteis.isAtributoPreenchido(professorSelecionado)) {
				setProfessorResponsavel(professorSelecionado.getPessoa());
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private void montarListaSelectItemGradeCurricular() throws Exception {
		List<GradeCurricularVO> gradeCurricularVOs = new ArrayList<GradeCurricularVO>(0);
		if (!getCursoVO().getCodigo().equals(0) && getTurmaVO().getCodigo().equals(0)) {
			gradeCurricularVOs = getFacadeFactory().getGradeCurricularFacade().consultarGradeCurriculars(getCursoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		} else {
			gradeCurricularVOs.add(getFacadeFactory().getGradeCurricularFacade().consultarPorTurmaNivelComboBox(getTurmaVO().getCodigo(), getUsuarioLogado()));
		}
		setListaSelectItemGradeCurricular(UtilSelectItem.getListaSelectItem(gradeCurricularVOs, "codigo", "nome"));
	}
	
	public List<SelectItem> getTipoConsultaComboProfessorBusca() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("cpf", "CPF"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemSituacao() throws Exception {
		if(listaSelectItemSituacaoCons == null){
			listaSelectItemSituacaoCons = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoCons.add(new SelectItem(SituacaoPlanoEnsinoEnum.TODOS.getValor(), SituacaoPlanoEnsinoEnum.TODOS.getDescricao()));			
			listaSelectItemSituacaoCons.add(new SelectItem(SituacaoPlanoEnsinoEnum.PENDENTE.getValor(), SituacaoPlanoEnsinoEnum.PENDENTE.getDescricao()));
			listaSelectItemSituacaoCons.add(new SelectItem(SituacaoPlanoEnsinoEnum.AUTORIZADO.getValor(), SituacaoPlanoEnsinoEnum.AUTORIZADO.getDescricao()));
			listaSelectItemSituacaoCons.add(new SelectItem(SituacaoPlanoEnsinoEnum.AGUARDANDO_APROVACAO.getValor(), SituacaoPlanoEnsinoEnum.AGUARDANDO_APROVACAO.getDescricao()));
			listaSelectItemSituacaoCons.add(new SelectItem(SituacaoPlanoEnsinoEnum.EM_REVISAO.getValor(), SituacaoPlanoEnsinoEnum.EM_REVISAO.getDescricao()));
		}
		return listaSelectItemSituacaoCons;
	}

	public List<SelectItem> getListaSelectItemPeriodicidade() {
		if (listaSelectItemPeriodicidade == null) {
			listaSelectItemPeriodicidade = Uteis.montarComboboxSemOpcaoDeNenhum(PeriodicidadeEnum.values(), Obrigatorio.SIM);
		}
		return listaSelectItemPeriodicidade;
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

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
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

	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
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

	public String getValorConsultaDisciplina() {
		if (valorConsultaDisciplina == null) {
			valorConsultaDisciplina = "";
		}
		return valorConsultaDisciplina;
	}

	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}

	public List<DisciplinaVO> getListaConsultaDisciplina() {
		if (listaConsultaDisciplina == null) {
			listaConsultaDisciplina = new ArrayList<>(0);
		}
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(List<DisciplinaVO> listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
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

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
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

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<SelectItem> getListaSelectItemGradeCurricular() {
		if (listaSelectItemGradeCurricular == null) {
			listaSelectItemGradeCurricular = new ArrayList<>(0);
		}
		return listaSelectItemGradeCurricular;
	}

	public void setListaSelectItemGradeCurricular(List<SelectItem> listaSelectItemGradeCurricular) {
		this.listaSelectItemGradeCurricular = listaSelectItemGradeCurricular;
	}

	public GradeCurricularVO getGradeCurricularVO() {
		if (gradeCurricularVO == null) {
			gradeCurricularVO = new GradeCurricularVO();
		}
		return gradeCurricularVO;
	}

	public void setGradeCurricularVO(GradeCurricularVO gradeCurricularVO) {
		this.gradeCurricularVO = gradeCurricularVO;
	}

	public boolean getApresentarDadosDisciplina() {
		return !getGradeCurricularVO().getCodigo().equals(0);
	}

	public boolean getApresentarGradeCurricular() {
		return !getCursoVO().getCodigo().equals(0) || !getTurmaVO().getCodigo().equals(0);
	}

	public List<SelectItem> getListaSelectItemSemestre() {
		if(listaSelectItemSemestre == null){
			listaSelectItemSemestre = new ArrayList<SelectItem>(0);
			listaSelectItemSemestre.add(new SelectItem("1", "1º"));
			listaSelectItemSemestre.add(new SelectItem("2", "2º"));
		}
		return listaSelectItemSemestre;
	}
	
	public List<SelectItem> getListaSelectItemTipoLayout() {
		List<SelectItem> lista = new ArrayList<>(0);
		lista.add(new SelectItem(tipoLayoutEnumPlanoDisciplina.ANALITICO.name(), tipoLayoutEnumPlanoDisciplina.ANALITICO.getDescricao()));
		lista.add(new SelectItem(tipoLayoutEnumPlanoDisciplina.SINTETICO.name(), tipoLayoutEnumPlanoDisciplina.SINTETICO.getDescricao()));
		return lista;
	}

	public void setListaSelectItemSemestre(List<SelectItem> listaSelectItemSemestre) {
		this.listaSelectItemSemestre = listaSelectItemSemestre;
	}

	public String getAno() {
		if(ano == null){
			ano = Uteis.getAnoDataAtual();
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if(semestre == null){
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public Boolean getMostrarCampoAno() {
		if (getCursoVO().getCodigo() != null && getCursoVO().getCodigo() != 0) {
			if (getCursoVO().getPeriodicidade().equals("SE") || getCursoVO().getPeriodicidade().equals("AN")) {
				return Boolean.TRUE;
			} else {
				setAno("");
				setSemestre("");
				return Boolean.FALSE;
			}
		}
		if (getTurmaVO().getCodigo() != null && getTurmaVO().getCodigo() != 0) {
			if (getTurmaVO().getCurso().getPeriodicidade().equals("SE") || getTurmaVO().getCurso().getPeriodicidade().equals("AN")) {
				return Boolean.TRUE;
			} else {
				setAno("");
				setSemestre("");
				return Boolean.FALSE;
			}
		}
		setAno("");
		setSemestre("");
		return Boolean.FALSE;
	}
	
	public Boolean getMostrarCampoSemestre() {
		if (getCursoVO().getCodigo() != null && getCursoVO().getCodigo() != 0) {
			if (getCursoVO().getPeriodicidade().equals("SE")) {
				return Boolean.TRUE;
			} else {
				setSemestre("");
				return Boolean.FALSE;
			}
		}
		if (getTurmaVO().getCodigo() != null && getTurmaVO().getCodigo() != 0) {
			if (getTurmaVO().getCurso().getPeriodicidade().equals("SE")) {
				return Boolean.TRUE;
			} else {
				setSemestre("");
				return Boolean.FALSE;
			}
		}
		setSemestre("");
		return Boolean.FALSE;
	}
	
	public String getValorConsultaProfessor() {
		if (valorConsultaProfessor == null) {
			valorConsultaProfessor = "";
		}
		return valorConsultaProfessor;
	}

	public String getCampoConsultaProfessor() {
		if (campoConsultaProfessor == null) {
			campoConsultaProfessor = "";
		}
		return campoConsultaProfessor;
	}

	public List<FuncionarioVO> getListaConsultaProfessor() {
		if (listaConsultaProfessor == null) {
			listaConsultaProfessor = new ArrayList<>(0);
		}
		return listaConsultaProfessor;
	}

	public void setValorConsultaProfessor(String valorConsultaProfessor) {
		this.valorConsultaProfessor = valorConsultaProfessor;
	}

	public void setCampoConsultaProfessor(String campoConsultaProfessor) {
		this.campoConsultaProfessor = campoConsultaProfessor;
	}

	public void setListaConsultaProfessor(List<FuncionarioVO> listaConsultaProfessor) {
		this.listaConsultaProfessor = listaConsultaProfessor;
	}

	public PessoaVO getProfessorResponsavel() {
		if (professorResponsavel == null) {
			professorResponsavel = new PessoaVO();
		}
		return professorResponsavel;
	}

	public void setProfessorResponsavel(PessoaVO professorResponsavel) {
		this.professorResponsavel = professorResponsavel;
	}

	public PeriodicidadeEnum getPeriodicidade() {
		if (periodicidade == null) {
			periodicidade = PeriodicidadeEnum.SEMESTRAL;
		}
		return periodicidade;
	}

	public void setPeriodicidade(PeriodicidadeEnum periodicidade) {
		this.periodicidade = periodicidade;
	}
	
	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = tipoLayoutEnumPlanoDisciplina.ANALITICO.name();
		}
		return tipoLayout;
	}

	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
	}
}
