package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.LivroMatriculaRelVO;
import relatorio.negocio.comuns.academico.enumeradores.TipoFiltroPeriodoAcademicoEnum;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.academico.LivroMatriculaRel;

@Controller("LivroMatriculaRelControle")
@Scope("viewScope")
public class LivroMatriculaRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = -2699862212261963961L;
	private Date dataInicio;
	private Date dataFim;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademico;
	private List<SelectItem> listaSelectItemAno;
	private String ano;
	private String semestre;
	private TurmaVO turmaVO;
	
	private String tipoCurso;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	private List<SelectItem> listaSelectItemTipoAluno;
	private String tipoAluno;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO;

	public void imprimirPDF() {
		getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
		imprimir();
	}

	public LivroMatriculaRelControle(){
		consultarUnidadeEnsinoFiltroRelatorio("");	
		verificarTodasUnidadeEnsinoSelecionados();
		verificarTodosTurnosSelecionados();
		consultarTurnoFiltroRelatorio();
		setMensagemID("msg_dados_parametroConsulta");
	}
	
	private void validarDados() throws Exception {
		if(getUnidadeEnsinosApresentar().length() == 0){
			throw new ConsistirException("O campo UNIDADE DE ENSINO deve ser informado.");
		}
		if (getIsApresentarAno()) {
			if (!Uteis.isAtributoPreenchido(getAno()) || getAno().length() != 4) {
				throw new ConsistirException("O campo ANO deve ser informado.");
			}
		}
		if (getIsApresentarSemestre()) {
			if (!Uteis.isAtributoPreenchido(getSemestre())) {
				throw new ConsistirException("O campo SEMESTRE deve ser informado.");
			}
		}
		if (getIsApresentarPeriodo()) {
			if (filtroRelatorioAcademicoVO.getDataInicio() == null) {
				throw new Exception("A DATA DE INÍCIO deve ser informado!");
			}
			if (filtroRelatorioAcademicoVO.getDataTermino() == null) {
				throw new Exception("A DATA FINAL deve ser informado!");
			}
			if ((filtroRelatorioAcademicoVO.getDataInicio().compareTo(filtroRelatorioAcademicoVO.getDataTermino()) >= 1)) {
				throw new Exception("A DATA DE INÍCIO não pode ser maior que a DATA FINAL!");
			}
		}
	}
	
	public void imprimir() {
		try {
			validarDados();
			registrarAtividadeUsuario(getUsuarioLogado(), "LivroMatriculaRelControle", "Inicializando Geração de Relatório Livro Matrícula", "Emitindo Relatório");
			List<LivroMatriculaRelVO> livroMatriculaRelVOs = getFacadeFactory().getLivroMatriculaRelInterfaceFacade().criarObjeto(filtroRelatorioAcademicoVO, getCursoVOs(), 
					getUnidadeEnsinoVOs(), getTurnoVOs(), getTurmaVO(), getAno(), getSemestre(),getTipoCurso(), getTipoAluno());
			if (!livroMatriculaRelVOs.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(LivroMatriculaRel.getDesignIReportRelatorio());
				getSuperParametroRelVO().setSubReport_Dir(LivroMatriculaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Livro de Matrículas");
				getSuperParametroRelVO().setListaObjetos(livroMatriculaRelVOs);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(LivroMatriculaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().adicionarParametro("unidadesFiltradas", getUnidadeEnsinosApresentar());
				getSuperParametroRelVO().adicionarParametro("cursosFiltrados", getCursosApresentar());
				getSuperParametroRelVO().adicionarParametro("turnosFiltrados", getTurnosApresentar());
				getSuperParametroRelVO().adicionarParametro("turmaFiltrada", getTurmaVO().getIdentificadorTurma());
				adicionarFiltroSituacaoAcademica(getSuperParametroRelVO(), getFiltroRelatorioAcademicoVO());
				getSuperParametroRelVO().adicionarParametro("ano", getAno());
				getSuperParametroRelVO().adicionarParametro("semestre", getSemestre());
				if(tipoCurso.equals("IN")){
				getSuperParametroRelVO().adicionarParametro("periodo", Uteis.getData(getFiltroRelatorioAcademicoVO().getDataInicio()) + " à " + Uteis.getData(getFiltroRelatorioAcademicoVO().getDataTermino()));
				}else{
					getSuperParametroRelVO().adicionarParametro("periodo","");
				}
				realizarImpressaoRelatorio();
				getFacadeFactory().getLayoutPadraoFacade().persistirFiltroSituacaoAcademica(getFiltroRelatorioAcademico(), "estatisticaMatricula", getUsuarioLogado());
	
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "EstatisticaMatriculaRelControle", "Finalizando Geração de Relatório Livro Matrícula", "Emitindo Relatório");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = new Date();
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = new Date();
		}
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public List<SelectItem> getListaSelectItemTipoFiltroPeriodoAcademico() {
		return TipoFiltroPeriodoAcademicoEnum.getListaSelectItemSemAmbos();
	}

	public FiltroRelatorioAcademicoVO getFiltroRelatorioAcademico() {
		if (filtroRelatorioAcademico == null) {
			filtroRelatorioAcademico = new FiltroRelatorioAcademicoVO();
		}
		return filtroRelatorioAcademico;
	}

	public void setFiltroRelatorioAcademico(FiltroRelatorioAcademicoVO filtroRelatorioAcademico) {
		this.filtroRelatorioAcademico = filtroRelatorioAcademico;
	}

	public List<SelectItem> getListaSelectItemAno() {
		try {
			
			if (listaSelectItemAno == null) {
				listaSelectItemAno = new ArrayList<SelectItem>(0);
				List<String> anos = getFacadeFactory().getMatriculaPeriodoFacade().consultarAnosMatriculaPeriodo();
				for (String ano : anos) {
					listaSelectItemAno.add(new SelectItem(ano, ano));
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return listaSelectItemAno;
	}

	public void setListaSelectItemAno(List<SelectItem> listaSelectItemAno) {
		this.listaSelectItemAno = listaSelectItemAno;
	}



	
	public String getAno() {
		if(ano == null){
			ano= "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if(semestre == null){
			semestre ="";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public TurmaVO getTurmaVO() {
		if(turmaVO == null){
			turmaVO = new  TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}
	
	public String getTipoCurso() {
		if (tipoCurso == null) {
			tipoCurso = "SE";
		}
		return tipoCurso;
	}

	public void setTipoCurso(String tipoCurso) {
		this.tipoCurso = tipoCurso;
	}
	
	public boolean getIsApresentarAno() {
		if (getTipoCurso().equals("AN") || getTipoCurso().equals("SE")) {
			return true;
		}
		
		return false;
	}

	public boolean getIsApresentarSemestre() {
		if (getTipoCurso().equals("SE")) {
			return true;
		}
		setSemestre("");
		return false;
	}
	
	public boolean getIsApresentarPeriodo(){
		if (getTipoCurso().equals("IN")){
		
			 return true;
		}
		
		return false;
	}

	public void consultarCursoFiltroRelatorio(){
		if(getCursoVOs().isEmpty() || !getCursoVOs().get(0).getPeriodicidade().equals(getTipoCurso())){
			consultarCursoFiltroRelatorio(getTipoCurso());
		}
	}
	
	public void limparDadosTurma() throws Exception {
		removerObjetoMemoria(getTurmaVO());	
	}
	
	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			if(obj.getSubturma() || obj.getTurmaAgrupada()) {
				throw new Exception("Não é possível gerar este relatório para uma turma "+obj.getTipoTurmaApresentar()+", o mesmo só considera turma base da matrícula periodo.");
			}
			obj.setTurmaSelecionada(Boolean.TRUE);
			setTurmaVO(obj);		
			limparUnidadeEnsinos();
			limparCursos();
			limparTurnos();		
			
			setTipoCurso(obj.getPeriodicidade());
			for(UnidadeEnsinoVO unidadeEnsinoVO: getUnidadeEnsinoVOs()) {
				if(unidadeEnsinoVO.getCodigo().equals(obj.getUnidadeEnsino().getCodigo())) {
					unidadeEnsinoVO.setFiltrarUnidadeEnsino(true);
					consultarCursoFiltroRelatorio(obj.getPeriodicidade());
					break;
				}
			}
			setUnidadeEnsinosApresentar(obj.getUnidadeEnsino().getNome());
			
			
			for(CursoVO cursoVO: getCursoVOs()) {
				if(cursoVO.getCodigo().equals(obj.getCurso().getCodigo())) {
					cursoVO.setFiltrarCursoVO(true);
					break;
				}
			}
			setCursosApresentar(obj.getCurso().getNome());
			
			for(TurnoVO turnoVO: getTurnoVOs()) {
				if(turnoVO.getCodigo().equals(obj.getTurno().getCodigo())) {
					turnoVO.setFiltrarTurnoVO(true);
					break;
				}
			}
			setTurnosApresentar(obj.getTurno().getNome());

			getListaConsultaTurma().clear();


		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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
		if(listaConsultaTurma == null){
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}
	
	public List<SelectItem> getListaSelectItemTipoAluno() {
		if (listaSelectItemTipoAluno == null) {
			listaSelectItemTipoAluno = new ArrayList<SelectItem>(0);
			listaSelectItemTipoAluno.add(new SelectItem("todos", "Todos"));
			listaSelectItemTipoAluno.add(new SelectItem("normal", "Alunos (Turma Origem)"));
			listaSelectItemTipoAluno.add(new SelectItem("reposicao", "Alunos (Reposição/ Inclusão)"));
		}
		return listaSelectItemTipoAluno;
	}

	public String getTipoAluno() {
		if (tipoAluno == null) {
			tipoAluno = "todos";
		}
		return tipoAluno;
	}

	public void setTipoAluno(String tipoAluno) {
		this.tipoAluno = tipoAluno;
	}
	
	@SuppressWarnings("rawtypes")
	public void consultarTurma() {
		try {
			super.consultar();
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public void limparConsultaTurma(){
		try {
			setListaConsultaTurma(null);
		} catch (Exception e) {
		}
	}
	
	/**
	 * @return the filtroRelatorioAcademicoVO
	 */
	public FiltroRelatorioAcademicoVO getFiltroRelatorioAcademicoVO() {
		if (filtroRelatorioAcademicoVO == null) {
			filtroRelatorioAcademicoVO = new FiltroRelatorioAcademicoVO();
		}
		return filtroRelatorioAcademicoVO;
	}

	/**
	 * @param filtroRelatorioAcademicoVO
	 *            the filtroRelatorioAcademicoVO to set
	 */
	public void setFiltroRelatorioAcademicoVO(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) {
		this.filtroRelatorioAcademicoVO = filtroRelatorioAcademicoVO;
	}
}
