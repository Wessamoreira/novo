package controle.secretaria;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.FilterFactory;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.TipoTrancamentoEnum;

/**
 * @author Wellington - 11 de jan de 2016
 *
 */
@Controller("MapaRegistroAbandonoCursoTrancamentoControle")
@Scope("viewScope")
public class MapaRegistroAbandonoCursoTrancamentoControle extends SuperControle {

	private static final long serialVersionUID = 9103613726027353023L;
	private List<MatriculaPeriodoVO> matriculaPeriodoVOs;
	private MatriculaPeriodoVO matriculaPeriodoVO;
	private String periodicidade;
	private String unidadeEnsinoApresentar;
	private String cursoApresentar;
	private String turnoApresentar;
	private String ano;
	private String semestre;
	private List<SelectItem> listaSelectItemPeriodicidade;
	private boolean trazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte = false;
	private boolean trazerAlunoTrancadoAbandonadoAnoSemestreBase = false;
	private boolean trancamentoRelativoAbondonoCurso = false;
	private boolean matriculaSelecionada = true;
	private boolean abrirPainelConfirmarGravacao = false;
	private Integer qtdeMatriculasSelecionados = 0;
	private ProgressBarVO progressBarVO;
	private String filtroMatricula;
	private String filtroNomeAluno;
	private String filtroNomeUnidadeEnsino;
	private String filtroNomeCurso;
	private String filtroNomeTurma;
	private List<SelectItem> listaSelectItemTipoTrancamento;
	private String tipoTrancamento;

	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("");
			verificarTodasUnidadesSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String consultar() {
		try {
			boolean selecionado = false;
			for (UnidadeEnsinoVO ueVO : getUnidadeEnsinoVOs()) {
				if (ueVO.getFiltrarUnidadeEnsino()) {
					selecionado = true;
					break;
				}
			}
			if (!selecionado) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_AlunosPorUnidadeCursoTurnoTurmaRel_unidadeEnsino"));
			}
			if (!Uteis.isAtributoPreenchido(getAno())) {
				throw new Exception(UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_ano"));
			}
			if (getApresentarCampoSemestre() && !Uteis.isAtributoPreenchido(getSemestre())) {
				throw new Exception(UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_semestre"));
			}
			//setMatriculaPeriodoVOs(getFacadeFactory().getMatriculaPeriodoFacade().consultarPorUnidadeEnsinoCursoTurnoMapaRegistroAbandonoCursoTrancamento(getUnidadeEnsinoVOs(), getCursoVOs(), getTurnoVOs(), getAno(), getSemestre(), getPeriodicidade(), isTrazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte(), isTrazerAlunoTrancadoAbandonadoAnoSemestreBase(), false, getUsuarioLogado()));
			inicializarDadosMarcacaoLista();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			unidade.setFiltrarUnidadeEnsino(getMarcarTodasUnidadeEnsino());
		}
		verificarTodasUnidadesSelecionadas();
	}

	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
			if (obj.getFiltrarUnidadeEnsino()) {
				unidade.append(obj.getNome().trim());
				if (getUnidadeEnsinoVOs().size() > 1) {
					unidade.append("; ");
				}
			}
		}
		setUnidadeEnsinoApresentar(unidade.toString());
		consultarCursoFiltroRelatorio(getPeriodicidade());
		consultarTurnoFiltroRelatorio();
	}

	public void marcarTodosCursosAction() throws Exception {
		for (CursoVO cursoVO : getCursoVOs()) {
			cursoVO.setFiltrarCursoVO(getMarcarTodosCursos());
		}
		verificarTodosCursosSelecionados();
	}

	public void verificarTodosCursosSelecionados() {
		StringBuilder curso = new StringBuilder();
		for (CursoVO obj : getCursoVOs()) {
			if (obj.getFiltrarCursoVO()) {
				curso.append(obj.getCodigo()).append(" - ");
				curso.append(obj.getNome());
				if (getCursoVOs().size() > 1) {
					curso.append("; ");
				}
			}
		}
		setCursoApresentar(curso.toString());
	}

	public void marcarTodosTurnosAction() throws Exception {
		for (TurnoVO turnoVO : getTurnoVOs()) {
			turnoVO.setFiltrarTurnoVO(getMarcarTodosTurnos());
		}
		verificarTodosTurnosSelecionados();
	}

	public void verificarTodosTurnosSelecionados() {
		StringBuilder turno = new StringBuilder();
		for (TurnoVO obj : getTurnoVOs()) {
			if (obj.getFiltrarTurnoVO()) {
				turno.append(obj.getNome());
				if (getTurnoVOs().size() > 1) {
					turno.append("; ");
				}
			}
		}
		setTurnoApresentar(turno.toString());
	}

	public void consultarCurso() {
		try {
			consultarCursoFiltroRelatorio(getPeriodicidade());
			verificarTodosCursosSelecionados();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inicializarDadosMarcacaoLista() {
		for (MatriculaPeriodoVO matriculaPeriodoVO : getMatriculaPeriodoVOs()) {
			matriculaPeriodoVO.getMatriculaVO().setAlunoSelecionado(isMatriculaSelecionada());
		}
	}
	public void marcarDesmarcarTodos(FilterFactory filterFactory) {		
		for (MatriculaPeriodoVO matriculaPeriodoVO : getMatriculaPeriodoVOs()) {
			marcarDesmarcarTodosFiltroTabela(matriculaPeriodoVO, filterFactory);
		}
	}

	public void executarVerificacaoQuantidadeMatriculasSelecionadas() {
		try {
			setQtdeMatriculasSelecionados(0);
			for (MatriculaPeriodoVO matriculaPeriodoVO : getMatriculaPeriodoVOs()) {
				if (matriculaPeriodoVO.getMatriculaVO().getAlunoSelecionado()) {
					setQtdeMatriculasSelecionados(getQtdeMatriculasSelecionados() + 1);
				}
			}
			if (!Uteis.isAtributoPreenchido(getQtdeMatriculasSelecionados())) {
				setAbrirPainelConfirmarGravacao(false);
				throw new Exception(UteisJSF.internacionalizar("msg_RenovarTurma_selecionarMatricula"));
			}
			setAbrirPainelConfirmarGravacao(true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void persistir() {
		try {
			if (!getProgressBarVO().getAtivado()) {
				executarInicializacaoProgressBar();
			}
			getFacadeFactory().getTrancamentoFacade().persistirTrancamentoMapaRegistroAbandonoCursoTrancamento(getMatriculaPeriodoVOs(), getTipoTrancamento(), getProgressBarVO(), getUsuarioLogado());
			for (Iterator<MatriculaPeriodoVO> iterator = getMatriculaPeriodoVOs().iterator(); iterator.hasNext();) {
				MatriculaPeriodoVO matriculaPeriodoVO = (MatriculaPeriodoVO) iterator.next();
				if (matriculaPeriodoVO.getMatriculaVO().getAlunoSelecionado()) {
					iterator.remove();
				}
			}
			setAbrirPainelConfirmarGravacao(false);
			setTrancamentoRelativoAbondonoCurso(false);
			setProgressBarVO(new ProgressBarVO());
			setMensagemID("msg_dados_gravados_trancamento");
		} catch (Exception e) {
			setProgressBarVO(new ProgressBarVO());
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}

	public void selecionarMatricula() {
		try {
			setMatriculaPeriodoVO((MatriculaPeriodoVO) getRequestMap().get("matriculaPeriodoItem"));
			getMatriculaPeriodoVO().getMatriculaVO().setAlunoSelecionado(true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void persistirIndividualmente() {
		try {
			validarDados();
			Integer unidadeEnsino = getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo();
			//getFacadeFactory().getTrancamentoFacade().persistirTrancamentoMapaRegistroAbandonoCursoTrancamentoIndividualmente(getMatriculaPeriodoVO(), getTipoTrancamento(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(unidadeEnsino), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unidadeEnsino), getUsuarioLogado());
			for (Iterator<MatriculaPeriodoVO> iterator = getMatriculaPeriodoVOs().iterator(); iterator.hasNext();) {
				MatriculaPeriodoVO matriculaPeriodoVO = (MatriculaPeriodoVO) iterator.next();
				if (matriculaPeriodoVO.getCodigo().equals(getMatriculaPeriodoVO().getCodigo())) {
					iterator.remove();
					break;
				}
			}
			setTrancamentoRelativoAbondonoCurso(false); 
			setOncompleteModal("RichFaces.$('panelMatricula').hide()");
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			getOncompleteModal();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void validarDados() throws Exception {
		if (getTipoTrancamento() == null || getTipoTrancamento().equals("")) {
			setOncompleteModal("RichFaces.$('panelMatricula').show()");
			throw new ConsistirException("O campo Tipo Trancamento deve ser informado.");
		}
	}

	public void executarInicializacaoProgressBar() {
		setProgressBarVO(new ProgressBarVO());
		getProgressBarVO().setMaxValue(getQtdeMatriculasSelecionados());
		getProgressBarVO().setAtivado(true);
		getProgressBarVO().setStatus("Iniciando Abandono Curso/Trancamento das Matrículas selecionadas.");
	}

	public String getUnidadeEnsinoApresentar() {
		if (unidadeEnsinoApresentar == null) {
			unidadeEnsinoApresentar = "";
		}
		return unidadeEnsinoApresentar;
	}

	public void setUnidadeEnsinoApresentar(String unidadeEnsinoApresentar) {
		this.unidadeEnsinoApresentar = unidadeEnsinoApresentar;
	}

	public String getCursoApresentar() {
		if (cursoApresentar == null) {
			cursoApresentar = "";
		}
		return cursoApresentar;
	}

	public void setCursoApresentar(String cursoApresentar) {
		this.cursoApresentar = cursoApresentar;
	}

	public String getTurnoApresentar() {
		if (turnoApresentar == null) {
			turnoApresentar = "";
		}
		return turnoApresentar;
	}

	public void setTurnoApresentar(String turnoApresentar) {
		this.turnoApresentar = turnoApresentar;
	}

	public String getPeriodicidade() {
		if (periodicidade == null) {
			periodicidade = PeriodicidadeEnum.ANUAL.getValor();
		}
		return periodicidade;
	}

	public void setPeriodicidade(String periodicidade) {
		this.periodicidade = periodicidade;
	}

	public List<SelectItem> getListaSelectItemPeriodicidade() {
		if (listaSelectItemPeriodicidade == null) {
			listaSelectItemPeriodicidade = new ArrayList<SelectItem>(0);
			listaSelectItemPeriodicidade.add(new SelectItem(PeriodicidadeEnum.ANUAL.getValor(), PeriodicidadeEnum.ANUAL.getDescricao()));
			listaSelectItemPeriodicidade.add(new SelectItem(PeriodicidadeEnum.SEMESTRAL.getValor(), PeriodicidadeEnum.SEMESTRAL.getDescricao()));
		}
		return listaSelectItemPeriodicidade;
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

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public boolean getApresentarCampoSemestre() {
		return PeriodicidadeEnum.SEMESTRAL.getValor().equals(getPeriodicidade());
	}

	public boolean isTrazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte() {
		return trazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte;
	}

	public void setTrazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte(boolean trazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte) {
		this.trazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte = trazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte;
	}

	public boolean isTrazerAlunoTrancadoAbandonadoAnoSemestreBase() {
		return trazerAlunoTrancadoAbandonadoAnoSemestreBase;
	}

	public void setTrazerAlunoTrancadoAbandonadoAnoSemestreBase(boolean trazerAlunoTrancadoAbandonadoAnoSemestreBase) {
		this.trazerAlunoTrancadoAbandonadoAnoSemestreBase = trazerAlunoTrancadoAbandonadoAnoSemestreBase;
	}

	public List<MatriculaPeriodoVO> getMatriculaPeriodoVOs() {
		if (matriculaPeriodoVOs == null) {
			matriculaPeriodoVOs = new ArrayList<MatriculaPeriodoVO>(0);
		}
		return matriculaPeriodoVOs;
	}

	public void setMatriculaPeriodoVOs(List<MatriculaPeriodoVO> matriculaPeriodoVOs) {
		this.matriculaPeriodoVOs = matriculaPeriodoVOs;
	}

	public boolean isTrancamentoRelativoAbondonoCurso() {
		return trancamentoRelativoAbondonoCurso;
	}

	public void setTrancamentoRelativoAbondonoCurso(boolean trancamentoRelativoAbondonoCurso) {
		this.trancamentoRelativoAbondonoCurso = trancamentoRelativoAbondonoCurso;
	}

	public boolean isMatriculaSelecionada() {
		return matriculaSelecionada;
	}

	public void setMatriculaSelecionada(boolean matriculaSelecionada) {
		this.matriculaSelecionada = matriculaSelecionada;
	}

	public Integer getQtdeMatriculasSelecionados() {
		return qtdeMatriculasSelecionados;
	}

	public void setQtdeMatriculasSelecionados(Integer qtdeMatriculasSelecionados) {
		this.qtdeMatriculasSelecionados = qtdeMatriculasSelecionados;
	}

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	public boolean isAbrirPainelConfirmarGravacao() {
		return abrirPainelConfirmarGravacao;
	}

	public void setAbrirPainelConfirmarGravacao(boolean abrirPainelConfirmarGravacao) {
		this.abrirPainelConfirmarGravacao = abrirPainelConfirmarGravacao;
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
	
	public void limparUnidadeEnsino() {
		try {
			setUnidadeEnsinoApresentar(null);
			setMarcarTodasUnidadeEnsino(false);
			marcarTodasUnidadesEnsinoAction();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparCurso() {
		try {
			setCursoApresentar(null);
			setMarcarTodosCursos(false);
			marcarTodosCursosAction();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurno() {
		try {
			setTurnoApresentar(null);
			setMarcarTodosTurnos(false);
			marcarTodosTurnosAction();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Integer getQuantidadeTotalAlunoListaMatriculaPeriodo() {
		return getMatriculaPeriodoVOs().size();
	}

	public String getFiltroMatricula() {
		if (filtroMatricula == null) {
			filtroMatricula = "";
		}
		return filtroMatricula;
	}

	public void setFiltroMatricula(String filtroMatricula) {
		this.filtroMatricula = filtroMatricula;
	}
	

	public void marcarDesmarcarTodosFiltroTabela(MatriculaPeriodoVO obj, FilterFactory filterFactory) {
		if (filterFactory != null) {
			setFiltroMatricula(filterFactory.getMapFilter().get("filtroMatricula").getFiltro());
			setFiltroNomeAluno(filterFactory.getMapFilter().get("filtroNomeAluno").getFiltro());
			setFiltroNomeUnidadeEnsino(filterFactory.getMapFilter().get("filtroNomeUnidadeEnsino").getFiltro());
			setFiltroNomeCurso(filterFactory.getMapFilter().get("filtroNomeCurso").getFiltro());
			setFiltroNomeTurma(filterFactory.getMapFilter().get("filtroNomeTurma").getFiltro());
		}
		
		if (!getFiltroMatricula().trim().isEmpty() || !getFiltroNomeAluno().trim().isEmpty() || !getFiltroNomeUnidadeEnsino().trim().isEmpty() 
				|| !getFiltroNomeCurso().trim().isEmpty() || !getFiltroNomeTurma().trim().isEmpty()) {
			
			boolean filtrarMatricula = filtrarMatricula(obj);
			boolean filtrarAluno = filtrarNomeAluno(obj);
			boolean filtrarUnidadeEnsino = filtrarNomeUnidadeEnsino(obj);
			boolean filtrarCurso = filtrarNomeCurso(obj);
			boolean filtrarTurma = filtrarNomeTurma(obj);
			if (filtrarMatricula && filtrarAluno && filtrarUnidadeEnsino && filtrarCurso && filtrarTurma) {
				obj.getMatriculaVO().setAlunoSelecionado(isMatriculaSelecionada());
			}
		} else {
			obj.getMatriculaVO().setAlunoSelecionado(isMatriculaSelecionada());
		}
		
	}
	
	public boolean filtrarMatricula(Object obj) {
		if (!getFiltroMatricula().trim().isEmpty()) {
			if (obj instanceof MatriculaPeriodoVO) {
				if(((MatriculaPeriodoVO) obj).getMatricula().toUpperCase().contains(getFiltroMatricula().toUpperCase().trim())){
					return true;
				}
				return false;
			}else{
				return false;
			}
		}
		return true;		
	}
	
	public Integer getQuantidadeAlunosSelecionados() {
		Integer qtde = 0;
		for (MatriculaPeriodoVO matriculaPeriodoVO : getMatriculaPeriodoVOs()) {
			if (matriculaPeriodoVO.getMatriculaVO().getAlunoSelecionado()) {
				qtde++;
			}
		}
		return qtde;
	}
	
	public boolean filtrarNomeAluno(Object obj) {
		if (!getFiltroNomeAluno().trim().isEmpty()) {
			if (obj instanceof MatriculaPeriodoVO) {
				if(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(((MatriculaPeriodoVO) obj).getMatriculaVO().getAluno().getNome())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltroNomeAluno().toUpperCase().trim())))){
					return true;
				}
				return false;
			}else{
				return false;
			}
		}
		return true;		
	}
	
	public boolean filtrarNomeUnidadeEnsino(Object obj) {
		if (!getFiltroNomeUnidadeEnsino().trim().isEmpty()) {
			if (obj instanceof MatriculaPeriodoVO) {
				if(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(((MatriculaPeriodoVO) obj).getMatriculaVO().getUnidadeEnsino().getNome())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltroNomeUnidadeEnsino().toUpperCase().trim())))){
					return true;
				}
				return false;
			}else{
				return false;
			}
		}
		return true;		
	}
	
	public boolean filtrarNomeCurso(Object obj) {
		if (!getFiltroNomeCurso().trim().isEmpty()) {
			if (obj instanceof MatriculaPeriodoVO) {
				if(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(((MatriculaPeriodoVO) obj).getMatriculaVO().getCurso().getNome())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltroNomeCurso().toUpperCase().trim())))){
					return true;
				}
				return false;
			}else{
				return false;
			}
		}
		return true;		
	}
	
	public boolean filtrarNomeTurma(Object obj) {
		if (!getFiltroNomeTurma().trim().isEmpty()) {
			if (obj instanceof MatriculaPeriodoVO) {
				if(((MatriculaPeriodoVO) obj).getTurma().getIdentificadorTurma().toUpperCase().contains(getFiltroNomeTurma().toUpperCase().trim())){
					return true;
				}
				return false;
			}else{
				return false;
			}
		}
		return true;		
	}

	public String getFiltroNomeAluno() {
		if (filtroNomeAluno == null) {
			filtroNomeAluno = "";
		}
		return filtroNomeAluno;
	}

	public void setFiltroNomeAluno(String filtroNomeAluno) {
		this.filtroNomeAluno = filtroNomeAluno;
	}

	public String getFiltroNomeUnidadeEnsino() {
		if (filtroNomeUnidadeEnsino == null) {
			filtroNomeUnidadeEnsino = "";
		}
		return filtroNomeUnidadeEnsino;
	}

	public void setFiltroNomeUnidadeEnsino(String filtroNomeUnidadeEnsino) {
		this.filtroNomeUnidadeEnsino = filtroNomeUnidadeEnsino;
	}

	public String getFiltroNomeCurso() {
		if (filtroNomeCurso == null) {
			filtroNomeCurso = "";
		}
		return filtroNomeCurso;
	}

	public void setFiltroNomeCurso(String filtroNomeCurso) {
		this.filtroNomeCurso = filtroNomeCurso;
	}

	public String getFiltroNomeTurma() {
		if (filtroNomeTurma == null) {
			filtroNomeTurma = "";
		}
		return filtroNomeTurma;
	}

	public void setFiltroNomeTurma(String filtroNomeTurma) {
		this.filtroNomeTurma = filtroNomeTurma;
	}
	
	public List<SelectItem> getListaSelectItemTipoTrancamento() {
		if(listaSelectItemTipoTrancamento == null) {
			listaSelectItemTipoTrancamento = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoTrancamentoEnum.class, true);
		}
		return listaSelectItemTipoTrancamento;
	}

	public String getTipoTrancamento() {
		if(tipoTrancamento == null) {
			tipoTrancamento = "";
		}
		return tipoTrancamento;
	}

	public void setTipoTrancamento(String tipoTrancamento) {
		this.tipoTrancamento = tipoTrancamento;
	}
	
}
