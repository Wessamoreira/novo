package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas fechamentoPeriodoLetivoPrmForm.jsp fechamentoPeriodoLetivoPrmCons.jsp) com as funcionalidades da classe
 * <code>FechamentoPeriodoLetivoPrm</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see FechamentoPeriodoLetivoPrm
 * @see FechamentoPeriodoLetivoPrmVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.PeriodoLetivoAtivoUnidadeEnsinoCursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Controller("FechamentoPeriodoLetivoPrmControle")
@Scope("viewScope")
@Lazy
public class FechamentoPeriodoLetivoPrmControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
//	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<PeriodoLetivoAtivoUnidadeEnsinoCursoVO> periodoLetivoAtivoUnidadeEnsinoCursoVOs;
	private List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs;
	private TurmaVO turmaVO;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivoUnidadeEnsinoCursoVO;
	private Boolean existeUnidadeEnsino;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private String ano;
	private String semestre;
	private Boolean selecionarTodos;
	private String situacaoFechamentoPeriodoLetivo;
	private String nivelEducacionalApresentar;
	private String periodicidade;
	private List<SelectItem> listaSelectItemPeriodicidade;
	private ProgressBarVO progressBar;
	
	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("");
			verificarTodasUnidadeEnsinoSelecionados();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public FechamentoPeriodoLetivoPrmControle() throws Exception {
		novo();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	public String novo() {
		removerObjetoMemoria(this);
		setUnidadeEnsinoCursoVO(null);
		setTurmaVO(null);
		setPeriodoLetivoAtivoUnidadeEnsinoCursoVOs(null);
		setPeriodoLetivoAtivoUnidadeEnsinoCursoVO(null);
//		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemPeriodicidade();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("fechamentoPeriodoLetivoPrmForm.xhtml");
	}

//	public void montarListaSelectItemUnidadeEnsino() {
//		try {
//			List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
//			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
//	}

	public void consultarCurso() {
		try {
			List<UnidadeEnsinoCursoVO> objs = new ArrayList<UnidadeEnsinoCursoVO>(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				if (getValorConsultaCurso().trim() != null || !getValorConsultaCurso().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getValorConsultaCurso().trim());
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoCursoListaUnidadeEnsinoNivelEducacional(valorInt, getUnidadeEnsinoVOs(), getNivelEducacionalApresentar(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoListaUnidadeEnsinoNivelEducacional(getValorConsultaCurso(), getUnidadeEnsinoVOs(),getNivelEducacionalApresentar(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setUnidadeEnsinoCursoVOs(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setUnidadeEnsinoCursoVOs(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarCurso() {
		try {
			setUnidadeEnsinoCursoVO((UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoCursoItens"));
			this.setValorConsultaCurso("");
			this.setCampoConsultaCurso("");
			montarListaSelectItemPeriodicidade();
		} catch (Exception e) {
			this.setValorConsultaCurso("");
			this.setCampoConsultaCurso("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarPeriodoLetivoAtivoUnidadeEnsinoCurso() {
		try {
			limparMensagem();
			getFacadeFactory().getUnidadeEnsinoCursoFacade().validarDadosFechamentoPeriodoLetivo(getPeriodicidade(), getAno(), getSemestre(),getNivelEducacionalApresentar());
			setPeriodoLetivoAtivoUnidadeEnsinoCursoVOs(getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().consultarPorUnidadeEnsinoCursoSituacaoDataFechamentoSemMatriculaPeriodoAtiva(0, 0, getUnidadeEnsinoVOs(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoCursoVO().getTurno().getCodigo(), null, getAno(), getSemestre(), getSituacaoFechamentoPeriodoLetivo(),getNivelEducacionalApresentar(), getPeriodicidade()));
			for (PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivoUnidadeEnsinoCursoVO : getPeriodoLetivoAtivoUnidadeEnsinoCursoVOs()) {
				periodoLetivoAtivoUnidadeEnsinoCursoVO.setDataFechamento(new Date());
				periodoLetivoAtivoUnidadeEnsinoCursoVO.setResponsavelFechamento(getUsuarioLogadoClone());
			}
			if (getPeriodoLetivoAtivoUnidadeEnsinoCursoVOs().isEmpty()) {
				setMensagemID("msg_FechamentoPeriodoLetivo_nenhumPeriodoLetivoAtivoEncontrado");
			} else {
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>FechamentoPeriodoLetivoPrm</code>. Caso o objeto seja
	 * novo (ainda não gravado no BD) é acionado a operação
	 * <code>incluir()</code>. Caso contrário é acionado o
	 * <code>alterar()</code>. Se houver alguma inconsistência o objeto não é
	 * gravado, sendo re-apresentado para o usuário juntamente com uma mensagem
	 * de erro.
	 */
	public void gravar() {
				
		try {
			limparMensagem();
			
			getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().alterarListaFechamento(getPeriodoLetivoAtivoUnidadeEnsinoCursoVOs(), "", getUnidadeEnsinoVOs(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoCursoVO().getTurno().getCodigo(), null, getProgressBar().getConfiguracaoGeralSistemaVO().getRealizarCalculoMediaFinalFechPeriodo(), getProgressBar().getUsuarioVO(),getNivelEducacionalApresentar(), getProgressBar(), getPeriodicidade());
			setSelecionarTodos(null);
			getProgressBar().getSuperControle().setMensagemID("msg_dados_gravados");
			getProgressBar().incrementar();
		} catch (ConsistirException ex) {			
			getProgressBar().getSuperControle().setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {
			getProgressBar().getSuperControle().setMensagemDetalhada("msg_erro", e.getMessage());
		}finally {			
			getProgressBar().setForcarEncerramento(true);
		}
	}
	
	public void realizarInicializacaoProgressBar() {
		try {
			if (getSituacaoFechamentoPeriodoLetivo().equals("FI")) {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("ReabrirPeriodoLetivoAtivoUnidadeEnsinoCurso", getUsuarioLogado());
			}
			getProgressBar().resetar();
			getProgressBar().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getUnidadeEnsinoCursoVO().getUnidadeEnsino()));
			getProgressBar().setUsuarioVO(getUsuarioLogadoClone());
			Integer totalAluno = new Long(getPeriodoLetivoAtivoUnidadeEnsinoCursoVOs().stream().filter(p -> p.getSelecionarPeriodoLetivoAtivoUnidadeEnsinoCursoFinalizar()).count()).intValue();
			
			if(totalAluno.equals(0)) {
				throw new Exception("Deve ser selecionado pelo menos uma turma para realizar esta operação.");
			}
			getProgressBar().iniciar(0l, totalAluno+1, "Realizando Fechamento Periodo Letivo", true, this, "gravar");
			
		} catch (ConsistirException ex) {			
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
//	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
//		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
//	}

	public void selecionarTodosDesmarcarTodos() {
		Iterator<PeriodoLetivoAtivoUnidadeEnsinoCursoVO> i = getPeriodoLetivoAtivoUnidadeEnsinoCursoVOs().iterator();
		while (i.hasNext()) {
			PeriodoLetivoAtivoUnidadeEnsinoCursoVO plauec = (PeriodoLetivoAtivoUnidadeEnsinoCursoVO) i.next();
			if (plauec.getIsPermitirSelecionarPeriodoLetivoAtivoUnidadeEnsinoCursoFinalizar()) {
				plauec.setSelecionarPeriodoLetivoAtivoUnidadeEnsinoCursoFinalizar(getSelecionarTodos());
			}
		}
	}

	public void selecionarPeriodoLetivoAtivoUnidadeEnsinoCurso() {
		try {
			setPeriodoLetivoAtivoUnidadeEnsinoCursoVO((PeriodoLetivoAtivoUnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("periodoLetivoAtivoUnidadeEnsinoCursoItens"));
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void alterarDataFimPeriodoLetivoAtivoUnidadeEnsinoCurso() {
		try {
			getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().alterarDataFimPeriodoLetivoAtivoParaFinalizacao(getPeriodoLetivoAtivoUnidadeEnsinoCursoVO(), getUsuarioLogado());
			for (Iterator<PeriodoLetivoAtivoUnidadeEnsinoCursoVO> iterator = getPeriodoLetivoAtivoUnidadeEnsinoCursoVOs().iterator(); iterator.hasNext();) {
				PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj = (PeriodoLetivoAtivoUnidadeEnsinoCursoVO) iterator.next();
				if (getPeriodoLetivoAtivoUnidadeEnsinoCursoVO().getCodigo().equals(obj.getCodigo())) {
					obj.setDataFimPeriodoLetivo(getPeriodoLetivoAtivoUnidadeEnsinoCursoVO().getDataFimPeriodoLetivo());
				}
			}
			setPeriodoLetivoAtivoUnidadeEnsinoCursoVO(null);
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			consultarPeriodoLetivoAtivoUnidadeEnsinoCurso();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosCursoTurno() {
		getUnidadeEnsinoCursoVO().getCurso().setCodigo(0);
		getUnidadeEnsinoCursoVO().getCurso().setNome("");
		getUnidadeEnsinoCursoVO().getTurno().setNome("");
		getUnidadeEnsinoCursoVO().getTurno().setCodigo(0);
		getUnidadeEnsinoCursoVO().setCodigo(0);
		setUnidadeEnsinoCursoVOs(null);
		setPeriodoLetivoAtivoUnidadeEnsinoCursoVOs(null);
		getListaSelectItemPeriodicidade().clear();
		getListaSelectItemPeriodicidade().add(new SelectItem("SE", "Semestral"));
		getListaSelectItemPeriodicidade().add(new SelectItem("AN", "Anual"));
		getListaSelectItemPeriodicidade().add(new SelectItem("IN", "Integral"));
		setMensagemID("msg_entre_dados");
	}

//	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
//		if (listaSelectItemUnidadeEnsino == null) {
//			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
//		}
//		return (listaSelectItemUnidadeEnsino);
//	}
//
//	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
//		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
//	}

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
		if (unidadeEnsinoCursoVO == null) {
			unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCursoVO;
	}

	public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
		this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
	}

	public Boolean getExisteUnidadeEnsino() {
		return existeUnidadeEnsino;
	}

	public void setExisteUnidadeEnsino(Boolean existeUnidadeEnsino) {
		this.existeUnidadeEnsino = existeUnidadeEnsino;
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

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
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

	public List<PeriodoLetivoAtivoUnidadeEnsinoCursoVO> getPeriodoLetivoAtivoUnidadeEnsinoCursoVOs() {
		if (periodoLetivoAtivoUnidadeEnsinoCursoVOs == null) {
			periodoLetivoAtivoUnidadeEnsinoCursoVOs = new ArrayList<PeriodoLetivoAtivoUnidadeEnsinoCursoVO>();
		}
		return periodoLetivoAtivoUnidadeEnsinoCursoVOs;
	}

	public void setPeriodoLetivoAtivoUnidadeEnsinoCursoVOs(List<PeriodoLetivoAtivoUnidadeEnsinoCursoVO> periodoLetivoAtivoUnidadeEnsinoCursoVOs) {
		this.periodoLetivoAtivoUnidadeEnsinoCursoVOs = periodoLetivoAtivoUnidadeEnsinoCursoVOs;
	}

	public List<UnidadeEnsinoCursoVO> getUnidadeEnsinoCursoVOs() {
		if (unidadeEnsinoCursoVOs == null) {
			unidadeEnsinoCursoVOs = new ArrayList<UnidadeEnsinoCursoVO>();
		}
		return unidadeEnsinoCursoVOs;
	}

	public void setUnidadeEnsinoCursoVOs(List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs) {
		this.unidadeEnsinoCursoVOs = unidadeEnsinoCursoVOs;
	}

	public boolean getIsApresentarCampoAno() {
		if (getUnidadeEnsinoCursoVO().getCurso().getPeriodicidade().equals("AN") || getUnidadeEnsinoCursoVO().getCurso().getPeriodicidade().equals("SE")) {
			return true;
		}
		setAno(null);
		return false;
	}

	public boolean getIsApresentarCampoSemestre() {
		if (getUnidadeEnsinoCursoVO().getCurso().getPeriodicidade().equals("SE")) {
			return true;
		}
		setSemestre(null);
		return false;
	}

	public List<SelectItem> getListaSelectItemSemestre() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("", ""));
		objs.add(new SelectItem("1", "1º"));
		objs.add(new SelectItem("2", "2º"));
		return objs;
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("codigo", "Codigo"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public Boolean getSelecionarTodos() {
		if (selecionarTodos == null) {
			selecionarTodos = false;
		}
		return selecionarTodos;
	}

	public void setSelecionarTodos(Boolean selecionarTodos) {
		this.selecionarTodos = selecionarTodos;
	}

	public PeriodoLetivoAtivoUnidadeEnsinoCursoVO getPeriodoLetivoAtivoUnidadeEnsinoCursoVO() {
		if (periodoLetivoAtivoUnidadeEnsinoCursoVO == null) {
			periodoLetivoAtivoUnidadeEnsinoCursoVO = new PeriodoLetivoAtivoUnidadeEnsinoCursoVO();
		}
		return periodoLetivoAtivoUnidadeEnsinoCursoVO;
	}

	public void setPeriodoLetivoAtivoUnidadeEnsinoCursoVO(PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivoUnidadeEnsinoCursoVO) {
		this.periodoLetivoAtivoUnidadeEnsinoCursoVO = periodoLetivoAtivoUnidadeEnsinoCursoVO;
	}
	
	public String getSituacaoFechamentoPeriodoLetivo() {
		if(situacaoFechamentoPeriodoLetivo == null) {
			situacaoFechamentoPeriodoLetivo = "";
		}
		return situacaoFechamentoPeriodoLetivo;
	}

	public void setSituacaoFechamentoPeriodoLetivo(String situacaoFechamentoPeriodoLetivo) {
		this.situacaoFechamentoPeriodoLetivo = situacaoFechamentoPeriodoLetivo;
	}
	
	public List<SelectItem> getListaSelectItemSituacaoFechamentoPeriodoLetivo() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("AT", "Ativo"));
		objs.add(new SelectItem("FI", "Fechado"));
		return objs;
	}
	
	public boolean getIsApresentarCampoReabrirPeriodoLetivo() {
		if (getSituacaoFechamentoPeriodoLetivo().equals("FI")) {
			return true;
		}
		return false;
	}

	public String getNivelEducacionalApresentar() {
		if(nivelEducacionalApresentar == null){
			nivelEducacionalApresentar = "";
		}
		return nivelEducacionalApresentar;
	}

	public void setNivelEducacionalApresentar(String nivelEducacionalApresentar) {
		this.nivelEducacionalApresentar = nivelEducacionalApresentar;
	}
	
	public String getPeriodicidade() {
		if(periodicidade == null) {
			periodicidade = "";
		}
		return periodicidade;
	}
	
	public void setPeriodicidade(String periodicidade) {
		this.periodicidade = periodicidade;
	}
	
	public List<SelectItem> getListaSelectItemPeriodicidade() {
		if(listaSelectItemPeriodicidade == null) {
			listaSelectItemPeriodicidade = new ArrayList<SelectItem>(0);
			listaSelectItemPeriodicidade.add(new SelectItem("SE", "Semestral"));
			listaSelectItemPeriodicidade.add(new SelectItem("AN", "Anual"));
			listaSelectItemPeriodicidade.add(new SelectItem("IN", "Integral"));
		}
		return listaSelectItemPeriodicidade;
	}
	
	public Boolean getIsApresentarCampoSemestrePeriodicidade() {
		if (!getPeriodicidade().equals("AN") && !getPeriodicidade().equals("IN")) {
			return true;
		}
		return false;
	}
	
	public Boolean getIsApresentarCampoAnoPeriodicidade() {
		if (!getPeriodicidade().equals("IN")) {
			return true;
		}
		return false;
	}
		
	public void montarListaSelectItemPeriodicidade() {
		getListaSelectItemPeriodicidade().clear();
		if (Uteis.isAtributoPreenchido(getUnidadeEnsinoCursoVO().getCurso())) {
			if (getUnidadeEnsinoCursoVO().getCurso().getPeriodicidade().equals("IN")) {
				getListaSelectItemPeriodicidade().add(new SelectItem("IN", "Integral"));
				setPeriodicidade("IN");
				setAno("");
				setSemestre("");
			} else if (getUnidadeEnsinoCursoVO().getCurso().getPeriodicidade().equals("SE")) {
				getListaSelectItemPeriodicidade().add(new SelectItem("SE", "Semestral"));
				setPeriodicidade("SE");
			} else {
				getListaSelectItemPeriodicidade().add(new SelectItem("AN", "Anual"));
				setPeriodicidade("AN");
				setSemestre("");
			}
		} else {
			getListaSelectItemPeriodicidade().add(new SelectItem("SE", "Semestral"));
			getListaSelectItemPeriodicidade().add(new SelectItem("AN", "Anual"));
			getListaSelectItemPeriodicidade().add(new SelectItem("IN", "Integral"));
			setAno(Uteis.getAnoDataAtual4Digitos());
			setSemestre(Uteis.getSemestreAtual());
		}
	}

	public void consultaPeriodicidade(){
		if (getPeriodicidade().equals("AN")) {
			setSemestre("");
		}
		if(getPeriodicidade().equals("IN")) {
			setSemestre("");
			setAno("");
		}
	}
	public ProgressBarVO getProgressBar() {
		if(progressBar == null) {
			progressBar =  new ProgressBarVO();
		}
		return progressBar;
	}

	public void setProgressBar(ProgressBarVO progressBar) {
		this.progressBar = progressBar;
	}
	
	
}