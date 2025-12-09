package controle.academico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoVisaoAlunoEnum;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.academico.GradeCurricularAlunoRelControle;
import relatorio.controle.arquitetura.SuperControleRelatorio;

/**
 * 
 * @author Marco Túlio
 *
 */
@Controller("MinhasNotasControle")
@Scope("viewScope")
@Lazy
public class MinhasNotasControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -844511328398164080L;
	
	private UnidadeEnsinoVO unidadeEnsino;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private MatriculaVO matricula;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private List listaConsultaAluno;
	private List listaSelectItemPeriodoLetivo;
	private List<List<HistoricoVO>> listaPrincipalHistoricos;
	private Integer periodoLetivo;
	private List historicoVOs;
	private CursoVO cursoVO;
	private ConfiguracaoAcademicoVO configuracaoAcademico;
	private List<SelectItem> listaSelectItemAnoSemestre;
	private String filterAnoSemestre;
	private Boolean apresentarCampoProfessor;
	private Boolean apresentarBotaoLancamentoNota;
	private List<RegistroAulaVO> listaDetalhesMinhasFaltasVOs;
	private MatriculaVO matriculaDetalhesMinhasFaltas;
	private Integer totalCargaHorariaPeriodoLetivoExigida;
	private Integer totalCargaHorariaPeriodoLetivoCumprida;
	private Integer totalDisciplinasCurso;
	private Integer totalDisciplinasCursadaCurso;

	@PostConstruct
	public void init() {
		inicializarMinhasNotasAdminsitrativo();
		inicializarMatriculaFollowUp();			
	}
	
	
	public void inicializarMinhasNotasAdminsitrativo(){
		montarListaSelectItemUnidadeEnsino();
	}
	
	
	public void inicializarMatriculaFollowUp() {
		try {
			if(context() != null && !getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) {
			String matricula = (String) context().getExternalContext().getSessionMap().get("matricula");
			if (matricula != null && !matricula.trim().equals("")) {
				getMatricula().setMatricula(matricula);
				consultarAlunoPorMatricula();
				realizarMontagemListaSelectItemPeriodoLetivo();
			}
			Integer unidadeEnsino = (Integer) context().getExternalContext().getSessionMap().get("unidadeEnsino");
			if(unidadeEnsino != null && unidadeEnsino != 0){
				getUnidadeEnsino().setCodigo(unidadeEnsino);
			}
			}
		
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			context().getExternalContext().getSessionMap().remove("matricula");
			context().getExternalContext().getSessionMap().remove("unidadeEnsino");
		}
		
	}
	
	
	
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			if(getUnidadeEnsinoLogado().getCodigo() != null && getUnidadeEnsinoLogado().getCodigo() != 0){
				getListaSelectItemUnidadeEnsino().add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(),getUnidadeEnsinoLogado().getNome()));
			}else {
			List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}
	

	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}
	
	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
		setMatricula(obj);
		setUnidadeEnsino(obj.getUnidadeEnsino());
		valorConsultaAluno = "";
		campoConsultaAluno = "";
		getListaConsultaAluno().clear();
		getListaPrincipalHistoricos().clear();
	}
	
	
	public void limparDadosAluno(){
		setMatricula(new MatriculaVO());
		setHistoricoVOs(new ArrayList(0));
		setPeriodoLetivo(null);
		getListaPrincipalHistoricos().clear();
	}
	
	
	public void imprimirMatrizCurricularAluno() {
		try {
			GradeCurricularAlunoRelControle gradeCurricularAlunoRelControle = (GradeCurricularAlunoRelControle) context().getExternalContext().getSessionMap().get(GradeCurricularAlunoRelControle.class.getSimpleName());
			if (gradeCurricularAlunoRelControle == null) {
				gradeCurricularAlunoRelControle = new GradeCurricularAlunoRelControle();
				context().getExternalContext().getSessionMap().put(GradeCurricularAlunoRelControle.class.getSimpleName(), gradeCurricularAlunoRelControle);
			}
			gradeCurricularAlunoRelControle.setMatriculaVO(getMatricula());
			gradeCurricularAlunoRelControle.setLayout("layout2");
			gradeCurricularAlunoRelControle.setVisaoAluno(false);
			gradeCurricularAlunoRelControle.imprimirPDF();
			setFazerDownload(gradeCurricularAlunoRelControle.getFazerDownload());
			setCaminhoRelatorio(gradeCurricularAlunoRelControle.getCaminhoRelatorio());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	
	public void realizarMontagemListaSelectItemPeriodoLetivo() {
		try {
			if(getMatricula().getMatricula() == null || getMatricula().getMatricula().trim().equals("")){
				throw new Exception("Informe a MATRÍCULA para poder efetuar a consulta.");
			}
			List<PeriodoLetivoVO> periodoLetivoVOs = new ArrayList<PeriodoLetivoVO>(0);
			getListaSelectItemPeriodoLetivo().clear();
			getListaPrincipalHistoricos().clear();
			setPeriodoLetivo(0);
//			CursoVO curso = getFacadeFactory().getCursoFacade().consultarCursoPorMatriculaParaInicializarNotaRapida(getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			montarConfiguracaoAcademico();
//			if (curso.getIntegral()) {
//				periodoLetivoVOs = getFacadeFactory().getPeriodoLetivoFacade().consultarPorMatriculaCurso(getMatricula().getMatricula(), curso.getCodigo(), curso.getPeriodicidade(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
//				if (!periodoLetivoVOs.isEmpty()) {
//					setPeriodoLetivo(((PeriodoLetivoVO) periodoLetivoVOs.get(0)).getCodigo());
//					montarListaHistoricoAluno(curso);
//				}
				
//			} else {
//				periodoLetivoVOs = getFacadeFactory().getPeriodoLetivoFacade().consultarPorMatriculaCurso(getMatricula().getMatricula(), curso.getCodigo(), curso.getPeriodicidade(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
//				if (!periodoLetivoVOs.isEmpty()) {
//					setListaSelectItemPeriodoLetivo(UtilSelectItem.getListaSelectItem(periodoLetivoVOs, "codigo", "descricao"));
//					montarListaHistoricoAluno(curso);
//				}
//			}
			
			setApresentarCampoProfessor(verificarPermissaoApresentarProfessor());
			this.verificarUsuarioPossuiPermissaoLancamentoNota();
			if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			montarListaHistoricoAluno(getCursoVO());
			}
//			curso = null;
			periodoLetivoVOs.clear();
			periodoLetivoVOs = null;
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
		}
	}
	
	public void montarConfiguracaoAcademico() throws Exception {
		setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getMatricula().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
		Integer codigoConfiguracaoAcademico = getCursoVO().getConfiguracaoAcademico().getCodigo();
		setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(codigoConfiguracaoAcademico, getUsuarioLogado()));
	}
	
	public void montarListaHistoricoAluno(CursoVO curso) throws Exception {
		try {
			setHistoricoVOs(new ArrayList<HistoricoVO>(0));
			getListaPrincipalHistoricos().clear();
			setHistoricoVOs(getFacadeFactory().getHistoricoFacade().executarMontagemListaHistoricoAluno(getMatricula(), getPeriodoLetivo(), getConfiguracaoAcademico(), curso, getConfiguracaoFinanceiroPadraoSistema(), "", true, true, true , getUsuarioLogado()));
			realizarMontagemListaHistoricoPrincipal();
			realizarMontagemTotalizadoresCargasHorarias();
			realizarMontagemTotalizadoresDisciplinasCursadasCurso();
			if (getHistoricoVOs().isEmpty() || getHistoricoVOs().get(0) == null) {
				throw new Exception("");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setHistoricoVOs(new ArrayList<HistoricoVO>(0));
			setListaPrincipalHistoricos(new ArrayList<List<HistoricoVO>>(0));
			throw e;
		}
	}
	
	
	private void realizarMontagemListaHistoricoPrincipal() throws Exception {
		setListaPrincipalHistoricos(getFacadeFactory().getHistoricoFacade().realizarSeparacaoHistoricoPorPeriodicidade(getHistoricoVOs(), getUsuarioLogado()));
		if(getIsApresentarCampoAno()){
			getListaSelectItemAnoSemestre().clear();
			getListaSelectItemAnoSemestre().add(new SelectItem("", ""));
		}
		for(List<HistoricoVO> historicoVOs: getListaPrincipalHistoricos()){			
			for(HistoricoVO historicoVO: historicoVOs){
				getListaSelectItemAnoSemestre().add(new SelectItem(historicoVO.getAnoSemestreApresentar(), historicoVO.getAnoSemestreApresentar()));
				break;
			}
		}
		if (getMatricula().getCurso().getIntegral()) {
			for (List<HistoricoVO> listas : getListaPrincipalHistoricos()) {
				List<HistoricoVO> listaAux = new ArrayList<>();
				Map<String, List<HistoricoVO>> mapFilhasComposicao = new HashMap<>(), mapFilhasComposicaoOptativas = new HashMap<>();
				for (HistoricoVO historicoVO : listas) {
					if (Uteis.isAtributoPreenchido(historicoVO.getGradeDisciplinaVO())) {
						listaAux.add(historicoVO);
						if (historicoVO.getHistoricoDisciplinaComposta()) {
							mapFilhasComposicao.put(historicoVO.getChaveDisciplinaMaeComposicao(), new ArrayList<>());
						}
					} else if (Uteis.isAtributoPreenchido(historicoVO.getGradeCurricularGrupoOptativaDisciplinaVO())) {
						listaAux.add(historicoVO);
						if (historicoVO.getHistoricoDisciplinaComposta()) {
							mapFilhasComposicaoOptativas.put(historicoVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo() + "-" + historicoVO.getAnoHistorico() + "-" +historicoVO.getSemestreHistorico(), new ArrayList<>());
						}
					} 
				}
				for (HistoricoVO historicoVO : listas) {
					if (Uteis.isAtributoPreenchido(historicoVO.getGradeDisciplinaComposta())) {
						if (Uteis.isAtributoPreenchido(historicoVO.getGradeDisciplinaComposta().getGradeDisciplina()) && mapFilhasComposicao.containsKey(historicoVO.getChaveDisciplinaFilhaComposicao())) {
							mapFilhasComposicao.get(historicoVO.getChaveDisciplinaFilhaComposicao()).add(historicoVO);
						} else if (Uteis.isAtributoPreenchido(historicoVO.getGradeDisciplinaComposta().getGradeCurricularGrupoOptativaDisciplina()) && mapFilhasComposicaoOptativas.containsKey(historicoVO.getChaveDisciplinaFilhaComposicaoOptativa())) {
							mapFilhasComposicaoOptativas.get(historicoVO.getChaveDisciplinaFilhaComposicaoOptativa()).add(historicoVO);
						}
					}
				}
				Ordenacao.ordenarLista(listaAux, "data");
				listas.clear();
				for (HistoricoVO historicoVO : listaAux) {
					if (Uteis.isAtributoPreenchido(historicoVO.getGradeDisciplinaVO())) {
						listas.add(historicoVO);
						if (historicoVO.getHistoricoDisciplinaComposta() && Uteis.isAtributoPreenchido(mapFilhasComposicao.get(historicoVO.getChaveDisciplinaMaeComposicao()))) {
							listas.addAll(mapFilhasComposicao.get(historicoVO.getChaveDisciplinaMaeComposicao()));
						}
					} else if (Uteis.isAtributoPreenchido(historicoVO.getGradeCurricularGrupoOptativaDisciplinaVO())) {
						listas.add(historicoVO);
						if (historicoVO.getHistoricoDisciplinaComposta() && Uteis.isAtributoPreenchido(mapFilhasComposicaoOptativas.get(historicoVO.getChaveDisciplinaMaeComposicaoOptativa()))) {
							listas.addAll(mapFilhasComposicaoOptativas.get(historicoVO.getChaveDisciplinaMaeComposicaoOptativa()));
						}
					} 
				}
			}
		}	
	}
	
	private boolean verificarPermissaoApresentarProfessor() {
	    	try {
	    		ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoVisaoAlunoEnum.PERMITIR_APRESENTAR_PROFESSOR, getUsuarioLogado());
	    		return true;
			} catch (Exception e) {
				return false;
			}
	    }
	
	/**
	 * @return the listaSelectItemAnoSemestre
	 */
	public List<SelectItem> getListaSelectItemAnoSemestre() {
		if (listaSelectItemAnoSemestre == null) {
			listaSelectItemAnoSemestre = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemAnoSemestre;
	}

	/**
	 * @param listaSelectItemAnoSemestre
	 *            the listaSelectItemAnoSemestre to set
	 */
	public void setListaSelectItemAnoSemestre(List<SelectItem> listaSelectItemAnoSemestre) {
		this.listaSelectItemAnoSemestre = listaSelectItemAnoSemestre;
	}
	
	public boolean getIsApresentarCampoAno() {
		return getMatricula().getCurso().getPeriodicidade().equals("AN") || getMatricula().getCurso().getPeriodicidade().equals("SE");
	}

	public boolean getIsApresentarCampoSemestre() {
		return getMatricula().getCurso().getPeriodicidade().equals("SE");
	}
	
	public void consultarAluno() {
		try {
			if(getUnidadeEnsino().getCodigo() == null || getUnidadeEnsino().getCodigo() == 0){
				throw new Exception("Deve ser informado pelo menos uma unidade de ensino para realizar a consulta.");
			}
			List objs = new ArrayList(0);
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("registroAcademico")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(getValorConsultaAluno(),  getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			getListaConsultaAluno().clear();
			getListaPrincipalHistoricos().clear();
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatricula().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatricula().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setUnidadeEnsino(objAluno.getUnidadeEnsino());
			setMatricula(objAluno);
			setPeriodoLetivo(null);
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setMatricula(new MatriculaVO());
		}
	}
	
	
	
	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}
	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}
	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}
	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}
	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return matricula;
	}
	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
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



	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}



	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}



	public List getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList();
		}
		return listaConsultaAluno;
	}



	public void setListaConsultaAluno(List listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public List getListaSelectItemPeriodoLetivo() {
		if (listaSelectItemPeriodoLetivo == null) {
			listaSelectItemPeriodoLetivo = new ArrayList();
		}
		return listaSelectItemPeriodoLetivo;
	}


	public void setListaSelectItemPeriodoLetivo(List listaSelectItemPeriodoLetivo) {
		this.listaSelectItemPeriodoLetivo = listaSelectItemPeriodoLetivo;
	}


	public List<List<HistoricoVO>> getListaPrincipalHistoricos() {
		if (listaPrincipalHistoricos == null) {
			listaPrincipalHistoricos = new ArrayList<List<HistoricoVO>>(0);
		}
		return listaPrincipalHistoricos;
	}


	public void setListaPrincipalHistoricos(List<List<HistoricoVO>> listaPrincipalHistoricos) {
		this.listaPrincipalHistoricos = listaPrincipalHistoricos;
	}


	public Integer getPeriodoLetivo() {
		if (periodoLetivo == null) {
			periodoLetivo = 0;
		}
		return periodoLetivo;
	}


	public void setPeriodoLetivo(Integer periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}


	public List getHistoricoVOs() {
		if (historicoVOs == null) {
			historicoVOs = new ArrayList(0);
		}
		return historicoVOs;
	}


	public void setHistoricoVOs(List historicoVOs) {
		this.historicoVOs = historicoVOs;
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


	public ConfiguracaoAcademicoVO getConfiguracaoAcademico() {
		if (configuracaoAcademico == null) {
			configuracaoAcademico = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademico;
	}


	public void setConfiguracaoAcademico(ConfiguracaoAcademicoVO configuracaoAcademico) {
		this.configuracaoAcademico = configuracaoAcademico;
	}

	public String getFilterAnoSemestre() {
		if(filterAnoSemestre == null){
			filterAnoSemestre = "";
		}
		return filterAnoSemestre;
	}

	public void setFilterAnoSemestre(String filterAnoSemestre) {
		this.filterAnoSemestre = filterAnoSemestre;
	}

	public Boolean getApresentarCampoProfessor() {
		if(apresentarCampoProfessor == null) {
			apresentarCampoProfessor = false;
		}
		return apresentarCampoProfessor;
	}

	public void setApresentarCampoProfessor(Boolean apresentarCampoProfessor) {
		this.apresentarCampoProfessor = apresentarCampoProfessor;
	}
	

	public Boolean getApresentarBotaoLancamentoNota() {
	if(apresentarBotaoLancamentoNota == null){
		apresentarBotaoLancamentoNota = false;
	}
	return apresentarBotaoLancamentoNota;
}

   public void setApresentarBotaoLancamentoNota(Boolean apresentarBotaoLancamentoNota) {
	  this.apresentarBotaoLancamentoNota = apresentarBotaoLancamentoNota;
   }
	
   
	public void verificarUsuarioPossuiPermissaoLancamentoNota(){
		try {
			if(ControleAcesso.verificarPermissaoOperacao("LancamentoNota", Uteis.INCLUIR, getUsuarioLogado())){
				setApresentarBotaoLancamentoNota(true);
			}else{
				setApresentarBotaoLancamentoNota(false);
			}
		} catch (Exception e) {
			setApresentarBotaoLancamentoNota(false);
		}
	}
	
	
	public void realizarNavegacaoTelaLancamentoNota() {
		HistoricoVO obj = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoVOItens");
		if (obj != null && !obj.getCodigo().equals(0)) {
			removerControleMemoriaFlashTela("LancamentoNotaControle");
			context().getExternalContext().getSessionMap().put("historicoFichaAluno", obj);
		}
	}
	
	
	public void consultarFaltasAluno() {
		try {
			setListaDetalhesMinhasFaltasVOs(new ArrayList<RegistroAulaVO>(0));
			HistoricoVO historicoVO = (HistoricoVO)getRequestMap().get("historicoVOItens");
			setMatriculaDetalhesMinhasFaltas(historicoVO.getMatricula());			
			if(!Uteis.isAtributoPreenchido(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurmaPratica()) && !Uteis.isAtributoPreenchido(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurmaTeorica())) {
				setListaDetalhesMinhasFaltasVOs(getFacadeFactory().getRegistroAulaFacade().consultaRapidaFaltasAlunoTurma(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo(), getMatriculaDetalhesMinhasFaltas().getMatricula(), historicoVO.getDisciplina().getCodigo(), historicoVO.getSemestreHistorico(), historicoVO.getAnoHistorico(), false, getUsuarioLogado()));
			}
			if(Uteis.isAtributoPreenchido(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurmaPratica())) {
				getListaDetalhesMinhasFaltasVOs().addAll(getFacadeFactory().getRegistroAulaFacade().consultaRapidaFaltasAlunoTurma(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurmaPratica().getCodigo(), getMatriculaDetalhesMinhasFaltas().getMatricula(), historicoVO.getDisciplina().getCodigo(), historicoVO.getSemestreHistorico(), historicoVO.getAnoHistorico(), false, getUsuarioLogado()));
			}
			if(Uteis.isAtributoPreenchido(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurmaTeorica())) {
				getListaDetalhesMinhasFaltasVOs().addAll(getFacadeFactory().getRegistroAulaFacade().consultaRapidaFaltasAlunoTurma(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurmaTeorica().getCodigo(), getMatriculaDetalhesMinhasFaltas().getMatricula(), historicoVO.getDisciplina().getCodigo(), historicoVO.getSemestreHistorico(), historicoVO.getAnoHistorico(), false, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	
	/**
	 * @return the listaDetalhesMinhasFaltasVOs
	 */
	public List<RegistroAulaVO> getListaDetalhesMinhasFaltasVOs() {
		if (listaDetalhesMinhasFaltasVOs == null) {
			listaDetalhesMinhasFaltasVOs = new ArrayList<RegistroAulaVO>();
		}
		return listaDetalhesMinhasFaltasVOs;
	}

	/**
	 * @param listaDetalhesMinhasFaltasVOs
	 *            the listaDetalhesMinhasFaltasVOs to set
	 */
	public void setListaDetalhesMinhasFaltasVOs(List<RegistroAulaVO> listaDetalhesMinhasFaltasVOs) {
		this.listaDetalhesMinhasFaltasVOs = listaDetalhesMinhasFaltasVOs;
	}

	public MatriculaVO getMatriculaDetalhesMinhasFaltas() {
		if(matriculaDetalhesMinhasFaltas == null) {
			matriculaDetalhesMinhasFaltas = new MatriculaVO();
		}
		return matriculaDetalhesMinhasFaltas;
	}

	public void setMatriculaDetalhesMinhasFaltas(MatriculaVO matriculaDetalhesMinhasFaltas) {
		this.matriculaDetalhesMinhasFaltas = matriculaDetalhesMinhasFaltas;
	}
	
	public Integer getTotalCargaHorariaPeriodoLetivoExigida() {
		if(totalCargaHorariaPeriodoLetivoExigida == null) {
			totalCargaHorariaPeriodoLetivoExigida =0;
		}
		return totalCargaHorariaPeriodoLetivoExigida;
	}

	public void setTotalCargaHorariaPeriodoLetivoExigida(Integer totalCargaHorariaPeriodoLetivoExigida) {
		this.totalCargaHorariaPeriodoLetivoExigida = totalCargaHorariaPeriodoLetivoExigida;
	}

	public Integer getTotalCargaHorariaPeriodoLetivoCumprida() {
		if(totalCargaHorariaPeriodoLetivoCumprida == null ) {
			totalCargaHorariaPeriodoLetivoCumprida =0 ;
		}
		return totalCargaHorariaPeriodoLetivoCumprida;
	}

	public void setTotalCargaHorariaPeriodoLetivoCumprida(Integer totalCargaHorariaPeriodoLetivoCumprida) {
		this.totalCargaHorariaPeriodoLetivoCumprida = totalCargaHorariaPeriodoLetivoCumprida;
	}
	
	public Integer getTotalDisciplinasCurso() {
		if(totalDisciplinasCurso == null) {
			totalDisciplinasCurso =0;
		}
		return totalDisciplinasCurso;
	}

	public void setTotalDisciplinasCurso(Integer totalDisciplinasCurso) {
		this.totalDisciplinasCurso = totalDisciplinasCurso;
	}

	public Integer getTotalDisciplinasCursadaCurso() {
	    if(totalDisciplinasCursadaCurso == null ) {
	    	totalDisciplinasCursadaCurso = 0;
	    }
	   return totalDisciplinasCursadaCurso;
	}

	public void setTotalDisciplinasCursadaCurso(Integer totalDisciplinasCursadaCurso) {
		this.totalDisciplinasCursadaCurso = totalDisciplinasCursadaCurso;
	}
	
	public  void realizarMontagemTotalizadoresCargasHorarias() {		
		try {
			HashMap<String, Integer> mapTotalCargasHorarias = new HashMap<String, Integer>(0);			
			getFacadeFactory().getHistoricoFacade().realizarMontagemTotalizadoresCargasHorarias(getFilterAnoSemestre() ,mapTotalCargasHorarias, getListaPrincipalHistoricos()  );
			setTotalCargaHorariaPeriodoLetivoExigida(mapTotalCargasHorarias.get("totalCargaHorariaPeriodoLetivoExigida"));
			setTotalCargaHorariaPeriodoLetivoCumprida(mapTotalCargasHorarias.get("totalCargaHorariaPeriodoLetivoCumprida"));
		} catch (Exception e) {			
			e.printStackTrace();
		}	
		
	}
	
	
	public void realizarMontagemTotalizadoresDisciplinasCursadasCurso() {	
		if(getMatricula().getCurso().getIntegral()) {
			 Integer  totalDisciplinasCurso = 0;
			 Integer totalDisciplinasCursadaCurso =0;
			for(List<HistoricoVO> historicoVOs: getListaPrincipalHistoricos()){	
				totalDisciplinasCurso +=   historicoVOs.get(0).getTotalDisciplinasPeriodoLetivo();
				totalDisciplinasCursadaCurso += historicoVOs.get(0).getTotalDisciplinasCursadasPeriodoLetivo();		
			}
			setTotalDisciplinasCurso(totalDisciplinasCurso);
			setTotalDisciplinasCursadaCurso(totalDisciplinasCursadaCurso);
			
		}
		
	}

	

	
	
}
