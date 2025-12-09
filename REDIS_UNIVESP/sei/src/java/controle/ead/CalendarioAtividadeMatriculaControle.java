package controle.ead;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.ead.enumeradores.SituacaoAtividadeEnum;
import negocio.comuns.ead.enumeradores.TipoCalendarioAtividadeMatriculaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * @author Victor Hugo 02/10/2014
 */
@Controller("CalendarioAtividadeMatriculaControle")
@Scope("viewScope")
public class CalendarioAtividadeMatriculaControle extends SuperControle {
	
	private static final long serialVersionUID = 1L;
	private CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO;
	private List<SelectItem> listaSelectItemDisciplinas;
	private List<CalendarioAtividadeMatriculaVO> listaConsultaCalendario = new ArrayList<CalendarioAtividadeMatriculaVO>();
	private List<SelectItem> listaSelectItemTurma;
	private Boolean buscarTurmasAnteriores;
	private Integer codigoDisciplina;
	private Integer codigoTurma;
	private TurmaVO turmaVO;
	private String ano;
	private String semestre;
	private Date periodoInicio;
	private Date periodoFim;
	private String tipoFiltroConsulta;
	private String situacaoAtividade;
	private TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividade;
	private Boolean selecionarAtividade;
	private Integer nrAtividadeSelecionadas;
	private Integer nrDias;
	private String comecaOuTerminaPeriodo;
	private String tipoOperacao;
	private String tipoContagem;
	private String tipoDataAlteracao;
	private boolean movimentarPeriodoAutomaticamente = false;
	private CursoVO cursoVO;
	private MatriculaVO matriculaVO;
	private String tituloRea;
	private Date dataInicioCalendario;
	private Date dataFimCalendario;
	private Integer codigoAtividadeDiscursiva;

	@PostConstruct
	public void init() {
		setMensagemID("msg_dados_parametroConsulta", Uteis.ALERTA);
	}

	public String consultar() throws Exception {
		try {
			if (getSituacaoAtividade().equals("todas")) {
				setSituacaoAtividade("");
			}
			String ano = getIsApresentarAno() ? getAno() : "";
			String semestre = getIsApresentarSemestre() ? getSemestre() : "";
			getListaConsultaCalendario().clear();
			setListaConsultaCalendario(getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarCalendarioAtividadeMatricula(getMatriculaVO().getMatricula(), getCursoVO().getCodigo(), getTurmaVO(), getCodigoDisciplina(), ano, semestre, false, getPeriodoInicio(), getPeriodoFim(), getSituacaoAtividade(), getTipoCalendarioAtividade(), getComecaOuTerminaPeriodo(), getTipoFiltroConsulta(), getTituloRea(), getCodigoAtividadeDiscursiva(), getUsuarioLogado()));
			if (getListaConsultaCalendario().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_relatorio_vazio"));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public void limparParametrosConsulta() {
		limparDadosAluno();
		limparDadosCurso();
		limparDadosTurma();
		getListaSelectItemDisciplinas().clear();
		getListaConsultaCalendario().clear();
		setMensagemID("msg_dados_parametroConsulta", Uteis.ALERTA);
	}

	public void executarAntecipacaoOuProrrogacaoCalendarioAtividadeMatricula() {
		try {
			getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().executarAntecipacaoOuProrrogacaoCalendarioAtividadeMatricula(getListaConsultaCalendario(), getTipoOperacao(), getTipoDataAlteracao(), getTipoContagem(), getNrDias(), isMovimentarPeriodoAutomaticamente(), getUnidadeEnsinoLogado(), getDataInicioCalendario(), getDataFimCalendario(), getUsuarioLogado());
			setNrDias(0);
			if(getInformarPeriodoEspecifico()) {
				setMensagemID("msg_dados_gravados");
			}else if (getTipoOperacao().equals("antecipar")) {
				setMensagemID("msg_CalendarioMatriculaAtividade_antecipadosComSucesso");
			} else {
				setMensagemID("msg_CalendarioMatriculaAtividade_prorrogadosComSucesso");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarTodasAtividades() {
		int cont = 0;
		for (CalendarioAtividadeMatriculaVO object : getListaConsultaCalendario()) {
			if ((object.getTipoCalendarioAtividade().equals(TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_ESTUDO) || object.getTipoCalendarioAtividade().equals(TipoCalendarioAtividadeMatriculaEnum.PERIODO_MAXIMO_CONCLUSAO_CURSO) || object.getTipoCalendarioAtividade().equals(TipoCalendarioAtividadeMatriculaEnum.PERIODO_MAXIMO_CONCLUSAO_DISCIPLINAS)) || !(object.getTipoCalendarioAtividade().equals(TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_ESTUDO) || object.getTipoCalendarioAtividade().equals(TipoCalendarioAtividadeMatriculaEnum.PERIODO_MAXIMO_CONCLUSAO_CURSO) || object.getTipoCalendarioAtividade().equals(TipoCalendarioAtividadeMatriculaEnum.PERIODO_MAXIMO_CONCLUSAO_DISCIPLINAS) && object.getSituacaoAtividade().equals(SituacaoAtividadeEnum.NAO_CONCLUIDA))) {
				if (object.isBloquearAntecipacaoOuProrrogacao()) {
					continue;
				}
				object.setSelecionarAtividade(getSelecionarAtividade());
				cont++;
			}
		}
		if (!getSelecionarAtividade()) {
			setNrAtividadeSelecionadas(0);
		} else {
			setNrAtividadeSelecionadas(cont);
		}
	}

	public void selecionarAtividade() {
		int cont = 0;
		CalendarioAtividadeMatriculaVO obj = (CalendarioAtividadeMatriculaVO) context().getExternalContext().getRequestMap().get("calendario");
		if (obj.getSelecionarAtividade()) {
			cont++;
		} else {
			cont--;
		}
		setNrAtividadeSelecionadas(getNrAtividadeSelecionadas() + cont);
	}

	public String inicializarCalendarioAtividadeDiscurisva() {
		this.init();
		return "calendarioAtividadeMatriculaCons";
	}

	public void consultarAlunoPorMatriculaOuNomeAluno() {
		try {
			if (getControleConsulta().getValorConsulta().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getControleConsulta().getCampoConsulta().equals("matricula")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			getControleConsulta().getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarAluno() throws Exception {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
			obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			setMatriculaVO(obj);
			montarListaDisciplinas();
			getControleConsulta().getListaConsulta().clear();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItem");
			obj = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
			setCursoVO(obj);
			montarListaDisciplinas();
			getControleConsulta().getListaConsulta().clear();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarTurma() throws Exception {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItem");
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			montarListaDisciplinas();
			getControleConsulta().getListaConsulta().clear();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			if(getMatriculaVO().getMatricula().trim().isEmpty()) {
				setMatriculaVO(new MatriculaVO());
				return;
			}
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatriculaVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMatriculaVO(objAluno);
			montarListaDisciplinas();
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			setMatriculaVO(new MatriculaVO());
		}
	}

	public void limparDadosAluno() {
		removerObjetoMemoria(getMatriculaVO());
		getListaSelectItemDisciplinas().clear();
		getListaConsultaCalendario().clear();
	}

	public void limparDadosCurso() {
		removerObjetoMemoria(getCursoVO());
		getListaSelectItemDisciplinas().clear();
		getListaConsultaCalendario().clear();
	}

	public void limparDadosTurma() {
		removerObjetoMemoria(getTurmaVO());
		getListaSelectItemDisciplinas().clear();
		getListaConsultaCalendario().clear();
	}

	public void consultarDadosCurso() {
		try {
			super.consultar();
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getCursoFacade().consultaRapidaPorNome(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, Boolean.TRUE, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nrRegistroInterno")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getCursoFacade().consultaRapidaPorNrRegistroInterno(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeAreaConhecimento")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getCursoFacade().consultaRapidaPorNomeAreaConhecimento(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nivelEducacional")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getCursoFacade().consultaRapidaPorNrNivelEducacional(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("unidadeEnsino")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getCursoFacade().consultaRapidaPorNomeUnidadeEnsino(getControleConsulta().getValorConsulta(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setListaConsulta(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarListaDisciplinas() {
		try {
			getListaSelectItemDisciplinas().clear();
			setListaSelectItemDisciplinas(UtilSelectItem.getListaSelectItem(consultarDisciplinas(), "codigo", "descricaoParaCombobox", true, false));
		} catch (Exception e) {
			setListaSelectItemDisciplinas(new ArrayList<SelectItem>(0));
		}
	}

	public List<DisciplinaVO> consultarDisciplinas() throws Exception {
		String ano = getIsApresentarAno() ? getAno() : "";
		String semestre = getIsApresentarSemestre() ? getSemestre() : "";
		if (!getMatriculaVO().getMatricula().equals("")) {
			return getFacadeFactory().getDisciplinaFacade().consultarPorMatriculaPeriodoAnoSemestre(getMatriculaVO().getMatricula(), ano, semestre, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		} else if (getCursoVO().getCodigo() != 0) {
			return getFacadeFactory().getDisciplinaFacade().consultarPorCodigoAreaConhecimentoCursoTurmaVisaoCoordenador(0, 0, 0, getCursoVO().getCodigo(), 0, ano, semestre, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		} else if (getTurmaVO().getCodigo() != 0) {
			return getFacadeFactory().getDisciplinaFacade().consultarPorCodigoAreaConhecimentoCursoTurmaVisaoCoordenador(0, 0, getTurmaVO().getCodigo(), 0, 0, ano, semestre, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		}
		return new ArrayList<DisciplinaVO>();
	}

	public void montarTurma() throws Exception {
		try {
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorCodigoUnidadeEnsinoIdentificadorTurma(getUnidadeEnsinoLogado().getCodigo().intValue(), getTurmaVO().getIdentificadorTurma(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			montarListaDisciplinas();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			removerObjetoMemoria(getTurmaVO());
			setTurmaVO(new TurmaVO());
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
		}
	}

	public void consultarTurma() {
		try {
			if (getControleConsulta().getCampoConsulta().equals("identificadorTurma")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, false, "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeTurno")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeCurso")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			getControleConsulta().getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	// Getters and Setters
	public CalendarioAtividadeMatriculaVO getCalendarioAtividadeMatriculaVO() {
		if (calendarioAtividadeMatriculaVO == null) {
			calendarioAtividadeMatriculaVO = new CalendarioAtividadeMatriculaVO();
		}
		return calendarioAtividadeMatriculaVO;
	}

	public void setCalendarioAtividadeMatriculaVO(CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO) {
		this.calendarioAtividadeMatriculaVO = calendarioAtividadeMatriculaVO;
	}

	public List<SelectItem> getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList<SelectItem>();
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	public Boolean getBuscarTurmasAnteriores() {
		if (buscarTurmasAnteriores == null) {
			buscarTurmasAnteriores = false;
		}
		return buscarTurmasAnteriores;
	}

	public void setBuscarTurmasAnteriores(Boolean buscarTurmasAnteriores) {
		this.buscarTurmasAnteriores = buscarTurmasAnteriores;
	}

	public Integer getCodigoTurma() {
		if (codigoTurma == null) {
			codigoTurma = 0;
		}
		return codigoTurma;
	}

	public void setCodigoTurma(Integer codigoTurma) {
		this.codigoTurma = codigoTurma;
	}

	public Integer getCodigoDisciplina() {
		if (codigoDisciplina == null) {
			codigoDisciplina = 0;
		}
		return codigoDisciplina;
	}

	public void setCodigoDisciplina(Integer codigoDisciplina) {
		this.codigoDisciplina = codigoDisciplina;
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

	public List<SelectItem> getListaSelectItemDisciplinas() {
		if (listaSelectItemDisciplinas == null) {
			listaSelectItemDisciplinas = new ArrayList<SelectItem>();
		}
		return listaSelectItemDisciplinas;
	}

	public void setListaSelectItemDisciplinas(List<SelectItem> listaSelectItemDisciplinas) {
		this.listaSelectItemDisciplinas = listaSelectItemDisciplinas;
	}

	public String getAno() {
		if (ano == null) {
			ano = Uteis.getAnoDataAtual4Digitos();
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "1";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public String getTipoFiltroConsulta() {
		if (tipoFiltroConsulta == null) {
			tipoFiltroConsulta = "turma";
		}
		return tipoFiltroConsulta;
	}

	public void setTipoFiltroConsulta(String tipoFiltroConsulta) {
		this.tipoFiltroConsulta = tipoFiltroConsulta;
	}

	public Date getPeriodoInicio() {
		if (periodoInicio == null) {
			periodoInicio = new Date();
		}
		return periodoInicio;
	}

	public void setPeriodoInicio(Date periodoInicio) {
		this.periodoInicio = periodoInicio;
	}

	public Date getPeriodoFim() {
		if (periodoFim == null) {
			periodoFim = new Date();
		}
		return periodoFim;
	}

	public void setPeriodoFim(Date periodoFim) {
		this.periodoFim = periodoFim;
	}

	public List<CalendarioAtividadeMatriculaVO> getListaConsultaCalendario() {
		if (listaConsultaCalendario == null) {
			listaConsultaCalendario = new ArrayList<CalendarioAtividadeMatriculaVO>();
		}
		return listaConsultaCalendario;
	}

	public void setListaConsultaCalendario(List<CalendarioAtividadeMatriculaVO> listaConsultaCalendario) {
		this.listaConsultaCalendario = listaConsultaCalendario;
	}

	public String getSituacaoAtividade() {
		if (situacaoAtividade == null) {
			situacaoAtividade = "";
		}
		return situacaoAtividade;
	}

	public void setSituacaoAtividade(String situacaoAtividade) {
		this.situacaoAtividade = situacaoAtividade;
	}

	public TipoCalendarioAtividadeMatriculaEnum getTipoCalendarioAtividade() {
		if (tipoCalendarioAtividade == null) {
			tipoCalendarioAtividade = TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_ESTUDO;
		}
		return tipoCalendarioAtividade;
	}

	public void setTipoCalendarioAtividade(TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividade) {
		this.tipoCalendarioAtividade = tipoCalendarioAtividade;
	}

	public Boolean getSelecionarAtividade() {
		if (selecionarAtividade == null) {
			selecionarAtividade = false;
		}
		return selecionarAtividade;
	}

	public void setSelecionarAtividade(Boolean selecionarAtividade) {
		this.selecionarAtividade = selecionarAtividade;
	}

	public Integer getNrAtividadeSelecionadas() {
		if (nrAtividadeSelecionadas == null) {
			nrAtividadeSelecionadas = 0;
		}
		return nrAtividadeSelecionadas;
	}

	public void setNrAtividadeSelecionadas(Integer nrAtividadeSelecionadas) {
		this.nrAtividadeSelecionadas = nrAtividadeSelecionadas;
	}

	public Integer getNrDias() {
		if (nrDias == null) {
			nrDias = 0;
		}
		return nrDias;
	}

	public void setNrDias(Integer nrDias) {
		this.nrDias = nrDias;
	}

	public String getComecaOuTerminaPeriodo() {
		if (comecaOuTerminaPeriodo == null) {
			comecaOuTerminaPeriodo = "todos";
		}
		return comecaOuTerminaPeriodo;
	}

	public void setComecaOuTerminaPeriodo(String comecaOuTerminaPeriodo) {
		this.comecaOuTerminaPeriodo = comecaOuTerminaPeriodo;
	}

	public String getTipoOperacao() {
		if (tipoOperacao == null) {
			tipoOperacao = "";
		}
		return tipoOperacao;
	}

	public String getTipoContagem() {
		if (tipoContagem == null) {
			tipoContagem = "";
		}
		return tipoContagem;
	}

	public void setTipoOperacao(String tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

	public void setTipoContagem(String tipoContagem) {
		this.tipoContagem = tipoContagem;
	}

	public Boolean getIsApresentarBotaoAlterarProrrogar() {
		for (CalendarioAtividadeMatriculaVO object : listaConsultaCalendario) {
			if (object.getSelecionarAtividade()) {
				return true;
			}
		}
		return false;
	}

	public boolean getIsApresentarAno() {
		if (getCursoVO().getCodigo() != 0) {
			if (getCursoVO().getPeriodicidade().equals("SE")) {
				return true;
			} else if (getCursoVO().getPeriodicidade().equals("AN")) {
				return true;
			}
		} else if (getTurmaVO().getCodigo() != 0) {
			if (getTurmaVO().getSemestral()) {
				return true;
			} else if (getTurmaVO().getAnual()) {
				return true;
			}
		} else if (!getMatriculaVO().getMatricula().equals("")) {
			if (getMatriculaVO().getCurso().getPeriodicidade().equals("SE")) {
				return true;
			} else if (getMatriculaVO().getCurso().getPeriodicidade().equals("AN")) {
				return true;
			}
		}
		return false;
	}

	public boolean getIsApresentarSemestre() {
		if (getCursoVO().getCodigo() != 0) {
			if (getCursoVO().getPeriodicidade().equals("SE")) {
				return true;
			}
		} else if (getTurmaVO().getCodigo() != 0) {
			if (getTurmaVO().getSemestral()) {
				return true;
			}
		} else if (!getMatriculaVO().getMatricula().equals("")) {
			if (getMatriculaVO().getCurso().getPeriodicidade().equals("SE")) {
				return true;
			}
		}
		return false;
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}

	public List<SelectItem> getComboBoxPeriodo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("todos", "Todos"));
		itens.add(new SelectItem("comecaOuTerminaNoPeriodo", "Começa ou Termina no Período"));
		itens.add(new SelectItem("comecaNoPeriodo", "Começa no Período"));
		itens.add(new SelectItem("terminaNoPeriodo", "Termina no Período"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("nrRegistroInterno", "Número (Registro Interno)"));
		itens.add(new SelectItem("nomeAreaConhecimento", "Área Conhecimento"));
		itens.add(new SelectItem("nivelEducacional", "Nível Educacional"));
		itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
		return itens;
	}

	public List<SelectItem> getTipoFiltroConsultaCalendario() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("turma", "Turma"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemTipoOperacao() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("prorrogar", "Prorrogar"));
		itens.add(new SelectItem("antecipar", "Antecipar"));
		itens.add(new SelectItem("periodoEspecifico", "Período Específico"));

		return itens;
	}
	
	public boolean getInformarPeriodoEspecifico() {
		return getTipoOperacao().equals("periodoEspecifico");
	}

	public List<SelectItem> getListaSelectItemTipoContagem() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("diasCorridos", "Dias Corridos"));
		itens.add(new SelectItem("diasUteis", "Dias Úteis"));
		return itens;
	}

	public List<SelectItem> getComboBoxTipoAtividade() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoCalendarioAtividadeMatriculaEnum.class, "name", "valorApresentar", false);
	}

	public List<SelectItem> getComboBoxSituacaoAtividade() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("todas", "Todas"));
		itens.add(new SelectItem(SituacaoAtividadeEnum.NAO_CONCLUIDA, "Não Concluída"));
		itens.add(new SelectItem(SituacaoAtividadeEnum.CONCLUIDA, "Concluída"));
		return itens;
	}

	public List<SelectItem> getCampoSemestreTurma() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);

		objs.add(new SelectItem("1", "1º"));
		objs.add(new SelectItem("2", "2º"));

		return objs;
	}

	public List<SelectItem> getComboBoxTipoDataAlteracao() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);

		objs.add(new SelectItem("inicio", "Início"));
		objs.add(new SelectItem("fim", "Fim"));

		return objs;
	}

	public Boolean getIsApresentarCamposDatasPeriodos() {
		return !getComecaOuTerminaPeriodo().equals("todos");
	}

	public String getTipoDataAlteracao() {
		if (tipoDataAlteracao == null) {
			tipoDataAlteracao = "";
		}
		return tipoDataAlteracao;
	}

	public void setTipoDataAlteracao(String tipoDataAlteracao) {
		this.tipoDataAlteracao = tipoDataAlteracao;
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

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public Boolean getIsApresentarFiltroMatricula() {
		return (tipoFiltroConsulta.equals("matricula"));
	}

	public Boolean getIsApresentarFiltroCurso() {
		return (tipoFiltroConsulta.equals("curso"));
	}

	public Boolean getIsApresentarFiltroTurma() {
		return (tipoFiltroConsulta.equals("turma"));
	}
	
	public boolean isMovimentarPeriodoAutomaticamente() {
		return movimentarPeriodoAutomaticamente;
	}

	public void setMovimentarPeriodoAutomaticamente(boolean movimentarPeriodoAutomaticamente) {
		this.movimentarPeriodoAutomaticamente = movimentarPeriodoAutomaticamente;
	}

	public String getTituloRea() {
		if (tituloRea == null) {
			tituloRea = "";
		}
		return tituloRea;
	}

	public void setTituloRea(String tituloRea) {
		this.tituloRea = tituloRea;
	}
	

	public Date getDataInicioCalendario() {
		if (dataInicioCalendario == null) {
			dataInicioCalendario = new Date();
		}
		return dataInicioCalendario;
	}

	public void setDataInicioCalendario(Date dataInicioCalendario) {
		this.dataInicioCalendario = dataInicioCalendario;
	}

	public Date getDataFimCalendario() {
		if (dataFimCalendario == null) {
			dataFimCalendario = new Date();
		}
		return dataFimCalendario;
	}

	public void setDataFimCalendario(Date dataFimCalendario) {
		this.dataFimCalendario = dataFimCalendario;
	}
	
	public void limparDadosParaConsulta() {
		getListaConsultaCalendario().clear();
		setTituloRea("");
		setSelecionarAtividade(false);
		setCodigoAtividadeDiscursiva(0);
	}

	public Integer getCodigoAtividadeDiscursiva() {
		if (codigoAtividadeDiscursiva == null) {
			codigoAtividadeDiscursiva = 0;
		}
		return codigoAtividadeDiscursiva;
	}

	public void setCodigoAtividadeDiscursiva(Integer codigoAtividadeDiscursiva) {
		this.codigoAtividadeDiscursiva = codigoAtividadeDiscursiva;
	}
}
