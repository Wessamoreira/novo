package controle.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ConfiguracaoAcademicaHistoricoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoLivroRegistroDiplomaEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@SuppressWarnings("unchecked")
@Controller("ConfiguracaoAcademicaHistoricoControle")
@Scope("viewScope")
@Lazy
public class ConfiguracaoAcademicaHistoricoControle extends SuperControle {

	private static final long serialVersionUID = 1L;

	private UnidadeEnsinoVO unidadeEnsino;
	private CursoVO curso;
	private DisciplinaVO disciplina;
	private TurmaVO turma;
	private MatriculaVO matricula;
	private List<DisciplinaVO> listaConsultaDisciplina;
	private List<CursoVO> listaConsultaCurso;
	private List<TurmaVO> listaConsultaTurma;
	private List<MatriculaVO> listaConsultaAluno;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> tipoConsultaComboTurma;
	private List<SelectItem> listaSelectItemConfiguracaoAcadHistorico;
	private List<SelectItem> listaSelectItemDisplinaPorTurma;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private Date dataInicio;
	private Date dataFim;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private String nivelEducacional;
	private String ordenacao;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private String anoMatricula;
	private String campoConsultaTipoFuncionalidade;
	private String semestre;
	private String ano;
	private ConfiguracaoAcademicoVO configuracaoAcademicoVO;
	private Boolean selecionarTudo;
	private Integer configuracaoAcadHistorico;
	private List<ConfiguracaoAcademicaHistoricoVO> configuracaoAcademicaHistoricoVOs;
	private ConfiguracaoAcademicaHistoricoVO configuracaoAcademicaHistoricoVO;
	private Boolean apresentarBotao;

	public ConfiguracaoAcademicaHistoricoControle() throws Exception {
		incializarDados();
		setCampoConsultaTipoFuncionalidade("curso");
		setMensagemID("msg_entre_prmconsulta");
	}

	public void limparListasConsultas() {
		removerObjetoMemoria(getDisciplina());
		removerObjetoMemoria(getCurso());
		removerObjetoMemoria(getTurma());
		removerObjetoMemoria(getMatricula());
		setValorConsultaAluno(null);
		setValorConsultaCurso(null);
		setValorConsultaDisciplina(null);
		setValorConsultaTurma(null);
		setAno(null);
		setListaConsultaCurso(new ArrayList<CursoVO>(0));
		setListaConsultaDisciplina(new ArrayList<DisciplinaVO>(0));
		setListaConsultaTurma(new ArrayList<TurmaVO>(0));
		setListaSelectItemDisplinaPorTurma(new ArrayList<SelectItem>(0));
		setConfiguracaoAcademicaHistoricoVOs(new ArrayList<ConfiguracaoAcademicaHistoricoVO>(0));

	}

	private void incializarDados() {
		montarListaSelectItemUnidadeEnsino();
	}

	public void selecionarNivelEducacional() {
		setUnidadeEnsino(null);
		setCurso(null);
		setTurma(null);
		setDisciplina(null);
	}

	public void validarDados() throws Exception {
		if (getUnidadeEnsino().getCodigo() == 0 || getUnidadeEnsino().getCodigo().equals(null)) {
			throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoAcademicaHistoricoVO_UnidadeEnsino"));
		}
		
		if (getDisciplina().getCodigo() == 0 || getDisciplina().getCodigo().equals(null)) {
			throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoAcademicaHistoricoVO_Disciplina"));
		}
		
		if (getIsExibirAno() == true) {
			if (getAno().equals(null) || getAno().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoAcademicaHistoricoVO_Ano"));
			}
			if (getAno().length() < 4) {
				throw new Exception(UteisJSF.internacionalizar("O ano deve conter 4 dígitos."));
			}
		}
		if (getIsExibirSemestre() == true) {
			if (getSemestre().equals(null) || getSemestre().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoAcademicaHistoricoVO_Semestre"));
			}
		} 
		
		if(getCampoConsultaTipoFuncionalidade().equals("turma")){
			setCurso(null);
			setMatricula(null);
			if (getTurma().getCodigo() == 0 || getTurma().getCodigo().equals(null)) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoAcademicaHistoricoVO_Turma"));
			}
		}else if(getCampoConsultaTipoFuncionalidade().equals("curso")){
			setTurma(null);
			setMatricula(null);
		}else{
			setTurma(null);
			setCurso(null);
		}
		if (!getIsExibirAno()) {
			setAno(Constantes.EMPTY);
		}
		if (!getIsExibirSemestre()) {
			setSemestre(Constantes.EMPTY);
		}
	}

	public void alterarConfiguracaoAcademicaHistoricoPorMatricula() {
		try {
			getFacadeFactory().getConfiguracaoAcademicaHistoricoInterface().alterarConfiguracaoAcademicaHistoricoPorMatricula(getConfiguracaoAcademicaHistoricoVO(), getUsuarioLogado());
			consultarAlterarConfiguracaoAcadHistorico();
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void alterarConfiguracaoAcademicoHistoricoVOs() {
		try {
			getFacadeFactory().getConfiguracaoAcademicaHistoricoInterface().alterarConfiguracaoAcademicaHistoricoVOs(getConfiguracaoAcademicaHistoricoVOs(), getUsuarioLogado());
			consultarAlterarConfiguracaoAcadHistorico();
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlterarConfiguracaoAcadHistorico() {
		try {
			validarDados();
			setConfiguracaoAcademicaHistoricoVOs(getFacadeFactory().getConfiguracaoAcademicaHistoricoInterface().consultaRapidaAlterarConfiguracaoAcadHistorico(getMatricula().getMatricula(), getUnidadeEnsino().getCodigo(), getCurso().getCodigo(), getTurma().getCodigo(), getDisciplina().getCodigo(), 0, getAno(), getSemestre(), getUsuarioLogado()));
			montarListaSelectItemConfiguracaoAcadHistorico();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemConfiguracaoAcadHistorico() {
		List<ConfiguracaoAcademicoVO> resultadoConsulta = null;
		setListaSelectItemConfiguracaoAcadHistorico(new ArrayList<>(0));
		getListaSelectItemConfiguracaoAcadHistorico().add(new SelectItem(null, Constantes.EMPTY));
		try {
			resultadoConsulta = consultarConfiguracaoAcadHistoricoPorNome();
			if (Uteis.isAtributoPreenchido(resultadoConsulta)) {
				resultadoConsulta.stream().map(configuracao -> new SelectItem(configuracao.getCodigo(), configuracao.getNome())).forEach(getListaSelectItemConfiguracaoAcadHistorico()::add);
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	private List<ConfiguracaoAcademicoVO> consultarConfiguracaoAcadHistoricoPorNome() throws Exception {
		List<ConfiguracaoAcademicoVO> lista = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarConfiguracaoAcademicoDeTodasConfiguracoesNivelCombobox(false, getUsuarioLogado());
		return lista;
	}

	private List<DisciplinaVO> consultarDisciplinaPorTurma() throws Exception {
		List<DisciplinaVO> lista = getFacadeFactory().getDisciplinaFacade().consultarHorarioTurmaDisciplinaProgramadaPorTurma(getTurma().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemDisciplinaPorTurma() {
		List<DisciplinaVO> resultadoConsulta = null;
		try {
			resultadoConsulta = consultarDisciplinaPorTurma();
			setListaSelectItemDisplinaPorTurma((UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome")));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public void consultarAluno() {
		try {
			getListaConsultaAluno().clear();
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				if (!matriculaVO.getMatricula().equals("")) {
					getListaConsultaAluno().add(matriculaVO);
				} else {
					removerObjetoMemoria(matriculaVO);
				}
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaAluno().equals("registroAcademico")) {
				setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatricula().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatricula().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			this.setMatricula(objAluno);
			this.getUnidadeEnsino().setCodigo(objAluno.getUnidadeEnsino().getCodigo());
			setCurso(objAluno.getCurso());
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.setMatricula(new MatriculaVO());
		}
	}
	
	public void consultarAlunoPorRegistroAcademico() {
		try {
			MatriculaVO  objAluno = getFacadeFactory().getMatriculaFacade().consultarMatriculaPorRegistroAcademico(getMatricula().getAluno().getRegistroAcademico(), this.getUnidadeEnsinoLogado().getCodigo(), 0,  Uteis.NIVELMONTARDADOS_COMBOBOX, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(this.getUnidadeEnsinoLogado().getCodigo()), getUsuarioLogado());
				if (objAluno == null || objAluno.getMatricula().equals("") ) {
					throw new Exception("Aluno de registro Acadêmico " + getMatricula().getAluno().getRegistroAcademico() + " não encontrado. Verifique se o número de matrícula está correto.");
				}		
			this.setMatricula(objAluno);
			this.getUnidadeEnsino().setCodigo(objAluno.getUnidadeEnsino().getCodigo());
			setCurso(objAluno.getCurso());
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.setMatricula(new MatriculaVO());
		}
	}

	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		MatriculaVO objCompleto = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
		setMatricula(objCompleto);
		setUnidadeEnsino(objCompleto.getUnidadeEnsino());
		setCurso(objCompleto.getCurso());
		obj = null;
		objCompleto = null;
		setValorConsultaAluno("");
		setCampoConsultaAluno("");
		getListaConsultaAluno().clear();
	}

	public void montarListaSelectItemUnidadeEnsino() {
		List<UnidadeEnsinoVO> resultadoConsulta = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public void consultarDisciplina() {
		try {
			List<DisciplinaVO> objs = new ArrayList<DisciplinaVO>(0);
			if (getCampoConsultaDisciplina().equals("codigo")) {
				if (getValorConsultaDisciplina().equals("")) {
					setValorConsultaDisciplina("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				if (getCampoConsultaTipoFuncionalidade().equals("matricula") || getCampoConsultaTipoFuncionalidade().equals("registroAcademico")) {
					if (Uteis.isAtributoPreenchido(getMatricula().getMatricula())) {
						objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo_Matricula_DisciplinaEquivalenteEDisciplinaComposta(valorInt, getMatricula().getMatricula(), null, true, false,  Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());

					}else{
						throw new Exception("Necessário preencher o campo Matricula/Registro Acadêmico.");
					}
				}
				if (getCampoConsultaTipoFuncionalidade().equals("curso")) {
					if (Uteis.isAtributoPreenchido(getCurso())) {
						objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo_CursoDisciplinaComposta(valorInt, getCurso().getCodigo(), true, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
					}else{
						throw new Exception("Necesário preencher o campo Curso.");
					}
				}

			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				if (getCampoConsultaTipoFuncionalidade().equals("matricula") || getCampoConsultaTipoFuncionalidade().equals("registroAcademico") ) {
					if (Uteis.isAtributoPreenchido(getMatricula().getMatricula())) {
						objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome_Matricula_DisciplinaEquivalenteEDisciplinaComposta(getValorConsultaDisciplina(), getMatricula().getMatricula(), null, getTurma().getCodigo(), true, false,  Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
					}else{
						throw new Exception("Necessário preencher o campo Matricula/Registro Acadêmico.");
					}
				}
				if (getCampoConsultaTipoFuncionalidade().equals("curso")) {
					if (Uteis.isAtributoPreenchido(getCurso())) {
						objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome_CursoDisciplinaComposta(getValorConsultaDisciplina(), getCurso().getCodigo(), getTurma().getCodigo(), true, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
					}else{
						throw new Exception("Necesário preencher o campo Curso.");
					}
				}
			}
			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void selecionarDisciplina() throws Exception {
		try {
			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
			if(getCampoConsultaTipoFuncionalidade().equals("matricula")){
				setAno(obj.getAno());
				setSemestre(obj.getSemestre());
			}
			setDisciplina(obj);
		} catch (Exception e) {
		}
	}

	public void limparDadosAluno() throws Exception {
		setMatricula(new MatriculaVO());
		limparListasConsultas();
		setCurso(new CursoVO());
		setDisciplina(new DisciplinaVO());
		setTurma(new TurmaVO());
		setCampoConsultaAluno(null);
	}

	public void consultarTurma() {
		try {
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnidadeEnsinoCursoTurno(getValorConsultaTurma(), getUnidadeEnsino().getCodigo(), getCurso().getCodigo(), 0, false, getUsuarioLogado()));
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
		setTurma(obj);
		setCurso(obj.getCurso());
		setUnidadeEnsino(obj.getUnidadeEnsino());
		montarListaSelectItemDisciplinaPorTurma();
		removerObjetoMemoria(getDisciplina());
		obj = null;
		setValorConsultaTurma(null);
		setCampoConsultaTurma(null);
		Uteis.liberarListaMemoria(getListaConsultaTurma());
	}

	public void selecionarTudo() {
		Iterator<ConfiguracaoAcademicaHistoricoVO> i = getConfiguracaoAcademicaHistoricoVOs().iterator();
		while (i.hasNext()) {
			ConfiguracaoAcademicaHistoricoVO m = (ConfiguracaoAcademicaHistoricoVO) i.next();
			m.setSelecionarConfiguracaoAcademicaHistorico(getSelecionarTudo());
		}
	}

	public void selecionarSelectOneMenuConfiguracaoAcademico() {
		for (ConfiguracaoAcademicaHistoricoVO obj : getConfiguracaoAcademicaHistoricoVOs()) {
			obj.getConfiguracaoAtualizada().setCodigo(getConfiguracaoAcademicoVO().getCodigo());
		}
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
		}
		return tipoConsultaComboTurma;
	}

	private List<SelectItem> tipoConsultaComboDisciplina;

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if (tipoConsultaComboDisciplina == null) {
			tipoConsultaComboDisciplina = new ArrayList<SelectItem>(0);
			tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboDisciplina;
	}

	public List<SelectItem> getTipoOrdenacaoCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
		itens.add(new SelectItem("data", "Data"));
		return itens;
	}

	public void consultarCurso() {
		try {
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorCodigoCursoUnidadeEnsino(new Integer(valorInt), getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			setCurso(obj);
			setDisciplina(null);
			setTurma(null);
			setListaConsultaDisciplina(null);
		} catch (Exception e) {
		}
	}

	public void limparCurso() throws Exception {
		try {
			setCurso(new CursoVO());
			setCampoConsultaCurso(null);
			setValorConsultaCurso(null);
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			limparListasConsultas();
		} catch (Exception e) {
		}
	}

	public void limparDisciplina() throws Exception {
		try {
			setDisciplina(new DisciplinaVO());
			setValorConsultaDisciplina(null);
			setListaConsultaDisciplina(new ArrayList<DisciplinaVO>(0));
			limparListasConsultas();
		} catch (Exception e) {
		}
	}

	public void limparTurma() throws Exception {
		try {
			setTurma(new TurmaVO());
			setValorConsultaTurma(null);
			removerObjetoMemoria(getTurma());
			limparListasConsultas();
		} catch (Exception e) {
		}
	}

	private List<SelectItem> tipoConsultaComboCurso;

	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboCurso;
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

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
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

	public DisciplinaVO getDisciplina() {
		if (disciplina == null) {
			disciplina = new DisciplinaVO();
		}
		return disciplina;
	}

	public void setDisciplina(DisciplinaVO disciplina) {
		this.disciplina = disciplina;
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
			listaConsultaCurso = new ArrayList<CursoVO>(0);
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
			listaConsultaDisciplina = new ArrayList<DisciplinaVO>(0);
		}
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(List<DisciplinaVO> listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = Uteis.getDataPrimeiroDiaMes(new Date());
		}
		return dataInicio;
	}

	public String periodo_Apresentar() {
		return Uteis.getData(getDataInicio()) + " à " + Uteis.getData(getDataFim());
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

	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return turma;
	}

	public void setTurma(TurmaVO turma) {
		this.turma = turma;
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
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
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

	public String getNivelEducacional() {
		if (nivelEducacional == null) {
			nivelEducacional = "";
		}
		return nivelEducacional;
	}

	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}

	public String getOrdenacao() {
		if (ordenacao == null) {
			ordenacao = "";
		}
		return ordenacao;
	}

	public void setOrdenacao(String ordenacao) {
		this.ordenacao = ordenacao;
	}

	public String getValorConsultaAluno() {
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	public String getCampoConsultaAluno() {
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	public List<MatriculaVO> getListaConsultaAluno() {
		if(listaConsultaAluno== null){
			listaConsultaAluno = new ArrayList<MatriculaVO>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
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

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));		
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public String getAnoMatricula() {
		if (anoMatricula == null) {
			anoMatricula = "";
		}
		return anoMatricula;
	}

	public void setAnoMatricula(String anoMatricula) {
		this.anoMatricula = anoMatricula;
	}

	private List<SelectItem> listaTipoFuncionalidade;

	public List<SelectItem> getListaTipoFuncionalidade() {
		if (listaTipoFuncionalidade == null) {
			listaTipoFuncionalidade = new ArrayList<SelectItem>(0);
			listaTipoFuncionalidade.add(new SelectItem("curso", "Curso"));
			listaTipoFuncionalidade.add(new SelectItem("turma", "Turma"));
			listaTipoFuncionalidade.add(new SelectItem("matricula", "Matrícula"));
			listaTipoFuncionalidade.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
		}
		return listaTipoFuncionalidade;
	}

	public boolean getIsApresentarMatricula() throws Exception {
		return getCampoConsultaTipoFuncionalidade().equals("matricula") && Uteis.isAtributoPreenchido(getUnidadeEnsino().getCodigo());
	}
	
	
	public boolean getIsApresentarRegistroAcademico() throws Exception {
		return getCampoConsultaTipoFuncionalidade().equals("registroAcademico") && Uteis.isAtributoPreenchido(getUnidadeEnsino().getCodigo());
	}

	public boolean getIsApresentarTurma() throws Exception {
		return getCampoConsultaTipoFuncionalidade().equals("turma") && Uteis.isAtributoPreenchido(getUnidadeEnsino().getCodigo());
	}

	public boolean getIsApresentarCurso() throws Exception {
		return getCampoConsultaTipoFuncionalidade().equals("curso") && Uteis.isAtributoPreenchido(getUnidadeEnsino().getCodigo());
	}

	public boolean getIsExibirListaSelectItemDisplinaPorTurma() {
		return getCampoConsultaTipoFuncionalidade().equals("turma") && Uteis.isAtributoPreenchido(getUnidadeEnsino().getCodigo());
	}

	public boolean getIsExibirCamposDisciplina() {
		return Uteis.isAtributoPreenchido(getUnidadeEnsino().getCodigo()) && (getCampoConsultaTipoFuncionalidade().equals("curso") || getCampoConsultaTipoFuncionalidade().equals("matricula") || getCampoConsultaTipoFuncionalidade().equals("registroAcademico")) ;
	}
	public boolean getIsExibirListaSelectItemDisciplinaOuCampoDisciplina() {
		return   Uteis.isAtributoPreenchido(getUnidadeEnsino().getCodigo()) && (Uteis.isAtributoPreenchido(getTurma().getCodigo()) || Uteis.isAtributoPreenchido(getCurso().getCodigo()) || Uteis.isAtributoPreenchido(getMatricula().getMatricula()) ||  Uteis.isAtributoPreenchido(getMatricula().getAluno().getRegistroAcademico()));
	}

	public String getCampoConsultaTipoFuncionalidade() {
		if (campoConsultaTipoFuncionalidade == null) {
			campoConsultaTipoFuncionalidade = "";
		}
		return campoConsultaTipoFuncionalidade;
	}

	public void setCampoConsultaTipoFuncionalidade(String campoConsultaTipoFuncionalidade) {
		this.campoConsultaTipoFuncionalidade = campoConsultaTipoFuncionalidade;
	}

	private List<SelectItem> listaSelectItemSemestre;

	public List<SelectItem> getListaSelectItemSemestre() {
		if (listaSelectItemSemestre == null) {
			listaSelectItemSemestre = new ArrayList<SelectItem>(0);
			listaSelectItemSemestre.add(new SelectItem("", ""));
			listaSelectItemSemestre.add(new SelectItem("1", "1º"));
			listaSelectItemSemestre.add(new SelectItem("2", "2º"));
		}
		return listaSelectItemSemestre;
	}

	public boolean getIsExibirAno() {
		if (getCampoConsultaTipoFuncionalidade().equals("curso") && Uteis.isAtributoPreenchido(getCurso())) {
			return getCurso().getPeriodicidade().equals("AN") || getCurso().getPeriodicidade().equals("SE");
		} else if (getCampoConsultaTipoFuncionalidade().equals("turma") && Uteis.isAtributoPreenchido(getTurma())) {
			return getTurma().getPeriodicidade().equals("AN") || getTurma().getPeriodicidade().equals("SE");
		} else if ((getCampoConsultaTipoFuncionalidade().equals("matricula") || getCampoConsultaTipoFuncionalidade().equals("registroAcademico")) && Uteis.isAtributoPreenchido(getMatricula())) {
			return getMatricula().getCurso().getPeriodicidade().equals("AN") || getMatricula().getCurso().getPeriodicidade().equals("SE");
		}
		return false;
	}

	public boolean getIsExibirSemestre() {
		if (getCampoConsultaTipoFuncionalidade().equals("curso") && Uteis.isAtributoPreenchido(getCurso())) {
			return getCurso().getPeriodicidade().equals("SE");
		} else if (getCampoConsultaTipoFuncionalidade().equals("turma") && Uteis.isAtributoPreenchido(getTurma())) {
			return getTurma().getPeriodicidade().equals("SE");
		} else if (getCampoConsultaTipoFuncionalidade().equals("matricula") && Uteis.isAtributoPreenchido(getMatricula())) {
			return getMatricula().getCurso().getPeriodicidade().equals("SE");
		}
		return false;
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

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public ConfiguracaoAcademicoVO getConfiguracaoAcademicoVO() {
		if (configuracaoAcademicoVO == null) {
			configuracaoAcademicoVO = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademicoVO;
	}

	public void setConfiguracaoAcademicoVO(ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
		this.configuracaoAcademicoVO = configuracaoAcademicoVO;
	}

	public List<SelectItem> getListaSelectItemConfiguracaoAcadHistorico() {
		if (listaSelectItemConfiguracaoAcadHistorico == null) {
			listaSelectItemConfiguracaoAcadHistorico = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemConfiguracaoAcadHistorico;
	}

	public void setListaSelectItemConfiguracaoAcadHistorico(List<SelectItem> listaSelectItemConfiguracaoAcadHistorico) {
		this.listaSelectItemConfiguracaoAcadHistorico = listaSelectItemConfiguracaoAcadHistorico;
	}

	public Boolean getSelecionarTudo() {
		if (selecionarTudo == null) {
			selecionarTudo = Boolean.FALSE;
		}
		return selecionarTudo;
	}

	public void setSelecionarTudo(Boolean selecionarTudo) {
		this.selecionarTudo = selecionarTudo;
	}

	public Integer getConfiguracaoAcadHistorico() {
		if (configuracaoAcadHistorico == null) {
			configuracaoAcadHistorico = 0;
		}
		return configuracaoAcadHistorico;
	}

	public void setConfiguracaoAcadHistorico(Integer configuracaoAcadHistorico) {
		this.configuracaoAcadHistorico = configuracaoAcadHistorico;
	}

	public List<ConfiguracaoAcademicaHistoricoVO> getConfiguracaoAcademicaHistoricoVOs() {
		if (configuracaoAcademicaHistoricoVOs == null) {
			configuracaoAcademicaHistoricoVOs = new ArrayList<ConfiguracaoAcademicaHistoricoVO>(0);
		}
		return configuracaoAcademicaHistoricoVOs;
	}

	public void setConfiguracaoAcademicaHistoricoVOs(List<ConfiguracaoAcademicaHistoricoVO> configuracaoAcademicaHistoricoVOs) {
		this.configuracaoAcademicaHistoricoVOs = configuracaoAcademicaHistoricoVOs;
	}

	public Boolean getApresentarBotao() {
		if (apresentarBotao == null) {
			apresentarBotao = Boolean.FALSE;
		}
		return apresentarBotao;
	}

	public void setApresentarBotao(Boolean apresentarBotao) {
		this.apresentarBotao = apresentarBotao;
	}

	public List<SelectItem> getListaSelectItemDisplinaPorTurma() {
		if (listaSelectItemDisplinaPorTurma == null) {
			listaSelectItemDisplinaPorTurma = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisplinaPorTurma;
	}

	public void setListaSelectItemDisplinaPorTurma(List<SelectItem> listaSelectItemDisplinaPorTurma) {
		this.listaSelectItemDisplinaPorTurma = listaSelectItemDisplinaPorTurma;
	}

	public ConfiguracaoAcademicaHistoricoVO getConfiguracaoAcademicaHistoricoVO() {
		if (configuracaoAcademicaHistoricoVO == null) {
			configuracaoAcademicaHistoricoVO = new ConfiguracaoAcademicaHistoricoVO();
		}
		return configuracaoAcademicaHistoricoVO;
	}

	public void setConfiguracaoAcademicaHistoricoVO(ConfiguracaoAcademicaHistoricoVO configuracaoAcademicaHistoricoVO) {
		this.configuracaoAcademicaHistoricoVO = configuracaoAcademicaHistoricoVO;
	}
	
	public String novo() {
		limparListasConsultas();
		setUnidadeEnsino(null);
		setMatricula(null);
		incializarDados();
		setCampoConsultaTipoFuncionalidade("curso");
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoAcademicaHistorico.xhtml");
	}

}
