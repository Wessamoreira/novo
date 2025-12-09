package controle.academico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

@Controller("DistribuicaoSubturmaControle")
@Scope("viewScope")
@Lazy
public class DistribuicaoSubturmaControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	private TurmaVO turmaPrincipal;
	private TurmaVO subturma;
	private String ano;
	private String semestre;
	private Integer disciplina;
	private List<SelectItem> listaSelectItemDisciplina;
	private String campoConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	private String valorConsultaTurma;
	private List<SelectItem> listaSelectItemSubturma;
	private List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs;
	private List<TurmaVO> subturmaVOs;
	private MatriculaPeriodoTurmaDisciplinaVO mptdTemp;
	private Boolean existeRegistroAula;
	private String situacaoMatriculaPeriodo;
	private Boolean realizarMarcacaoDesmarcacaoTodos;
	private Boolean apresentarMensagemQtdeHorasAbono;
	private List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaComRegistroAulaVOs;
	private List<String> msgChoqueHorario;
	private List<SelectItem> listaSelectItemDisciplinaFazParteComposicao;
	private Boolean apresentarComboboxDisciplinaFazParteComposicao;
	private DisciplinaVO disciplinaFazParteComposicao;
	private TipoSubTurmaEnum tipoSubTurma;
	private Boolean apresentarPainelAlunoVinculadoSubturmaTeoricaOuPratica;
	private Boolean realizandoDistribuicaoManual;
	private Boolean realizarValidacaoAlunoVinculadoSubturmaTeoricaOuPratica;
	private Boolean removerVinculoSubturmaTeoricaPratica;

	/**
	 * Método responsavel por realizar a consulta de alunos que cujo nao esteja em uma subturma, realizar a montagem das subturmas cadastradas e
	 * montar alunos cuja distribuição de subturmas ja foram realizadas
	 * 
	 * @throws Exception
	 */
	public void buscarAlunoSubturma() {
		try {
			if (Uteis.isAtributoPreenchido(getTurmaPrincipal())) {
				getListaSelectItemSubturma().clear();
				getFacadeFactory().getDistribuicaoSubturmaFacade().buscarAlunosSubturma(getMatriculaPeriodoTurmaDisciplinaVOs(), getSubturmaVOs(), getTurmaPrincipal(), getAno(), getSemestre(), obterDisciplinaUtilizar(), getSituacaoMatriculaPeriodo(), getUsuarioLogado(), getTipoSubTurma());
				montarListaSelectItemSubturma();
				setMensagemID("msg_dados_consultados");
			} else {
				setMensagemID("msg_entre_prmconsulta");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsavel por realizar a distribuição de subturmas de acordo com a quantidade de alunos e o número máximo de matriculas para cada
	 * subturma
	 * 
	 * @throws Exception
	 */
	public void realizarDistribuicaoAlunoTurma() {
		try {
			getFacadeFactory().getDistribuicaoSubturmaFacade().realizarDistribuicaoAlunoTurma(getSubturmaVOs(), getMatriculaPeriodoTurmaDisciplinaVOs(), getDisciplina(), getUsuarioLogado());
			setMensagemID("msg_distribuicao_sucesso");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarSubturma() {
		setSubturma((TurmaVO) context().getExternalContext().getRequestMap().get("subturmaItens"));
	}

	public void removerAlunoSubturma() {
		try {
			getFacadeFactory().getDistribuicaoSubturmaFacade().removerAlunoSubturma(getMatriculaPeriodoTurmaDisciplinaVOs(), getSubturmaVOs(), getSubturma(), getMptdTemp(), obterDisciplinaUtilizar(), getUsuarioLogado(), getDisciplinaFazParteComposicaoSelecinada(), getTipoSubTurma());
			setMensagemID("msg_dados_removidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerTodosAlunosSubturma() {
		try {
			if(Uteis.isAtributoPreenchido(getSubturma())) {				
				while(!getSubturma().getMatriculaPeriodoTurmaDisciplinaVOs().isEmpty()) {
					MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO = getSubturma().getMatriculaPeriodoTurmaDisciplinaVOs().get(0);
					if(matriculaPeriodoTurmaDisciplinaVO != null) {
						getFacadeFactory().getDistribuicaoSubturmaFacade().removerAlunoSubturma(getMatriculaPeriodoTurmaDisciplinaVOs(), getSubturmaVOs(), getSubturma(), matriculaPeriodoTurmaDisciplinaVO, obterDisciplinaUtilizar(), getUsuarioLogado(), getDisciplinaFazParteComposicaoSelecinada(), getTipoSubTurma());
					}
				}
				setSubturma(new TurmaVO());
			}else {
				getFacadeFactory().getDistribuicaoSubturmaFacade().removerTodosAlunosSubturma(getMatriculaPeriodoTurmaDisciplinaVOs(), getSubturmaVOs(), getSubturma(), obterDisciplinaUtilizar(), getUsuarioLogado(), getDisciplinaFazParteComposicaoSelecinada(), getTipoSubTurma());
				buscarAlunoSubturma();
			}
			setMensagemID("msg_dados_removidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsavel por remover a subturma da lista de distribuição das mesmas verificando se subturma nao tem turma anterior, ou seja, a mesma
	 * ainda nao foi distribuida
	 * 
	 * @throws Exception
	 */
	public void removerSubturmaListaDistribuicao() {
//		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("subturmaItens");
//		if (!obj.getMatriculaPeriodoTurmaDisciplinaVOs().isEmpty() && !obj.getUtilizarSubturmaNaDistribuicao()) {
//			for (MatriculaPeriodoTurmaDisciplinaVO mptd : obj.getMatriculaPeriodoTurmaDisciplinaVOs()) {
//				if (mptd.isNovoObj()) {		
//					if(getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
//						mptd.setTurmaTeorica(null);
//					}else if(getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
//						
//					}
//					getMatriculaPeriodoTurmaDisciplinaVOs().add(mptd);
//				}
//			}
//			obj.setQtdeAlunosDistribuir(obj.getMatriculaPeriodoTurmaDisciplinaVOs().size());
//		}
	}

	/**
	 * Método responsavel por executar alteracao da turma de uma lista de matricula periodo ou a distribuicao manual desta verificando se ha registro
	 * de aula, executa confirmacao e geracao de abono equivalente a quantidade de registros de aula
	 * 
	 * @throws Exception
	 */
	public void persistir() {
		try {
			persistirComException();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private void persistirComException() throws Exception {
		setMatriculaPeriodoTurmaDisciplinaComRegistroAulaVOs(null);
		setMsgChoqueHorario(getFacadeFactory().getDistribuicaoSubturmaFacade().executarAlteracaoTurmaAlunoDistribuicaoSubturma(getTurmaPrincipal(), getSubturmaVOs(), getMatriculaPeriodoTurmaDisciplinaComRegistroAulaVOs(), false, getUsuarioLogado(), getDisciplinaFazParteComposicaoSelecinada(), getTipoSubTurma(), getRemoverVinculoSubturmaTeoricaPratica()));
		if (!Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaComRegistroAulaVOs()) && !Uteis.isAtributoPreenchido(getMsgChoqueHorario())) {
			buscarAlunoSubturma();
		}
		setRealizarValidacaoAlunoVinculadoSubturmaTeoricaOuPratica(true);
		setApresentarPainelAlunoVinculadoSubturmaTeoricaOuPratica(false);
		setRemoverVinculoSubturmaTeoricaPratica(false);
		setMensagemID("msg_dados_gravados");
	}

	public void persistirMatriculaPeriodoTurmaDisciplinaComRegistroAulaVOs() {
		try {
			getFacadeFactory().getDistribuicaoSubturmaFacade().executarAlteracaoTurmaPorMatriculaPeriodoTurmaDisciplinaComRegistroAulaVOs(getTurmaPrincipal(), getMatriculaPeriodoTurmaDisciplinaComRegistroAulaVOs(), getUsuarioLogado(), getDisciplinaFazParteComposicaoSelecinada(), getTipoSubTurma());
			buscarAlunoSubturma();
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String novo() {
		setTurmaPrincipal(new TurmaVO());
		setSubturma(new TurmaVO());
		setMatriculaPeriodoTurmaDisciplinaVOs(new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0));
		setSubturmaVOs(new ArrayList<TurmaVO>(0));
		setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
		setAno(null);
		setSemestre(null);
		setDisciplina(null);
		return Uteis.getCaminhoRedirecionamentoNavegacao("distribuicaoSubturmaForm.xhtml");
	}

	/**
	 * Método responsavel por executar alteracao da turma de uma determinada matricula periodo
	 * 
	 * @throws Exception
	 */
	public void executarVerificacaoQtdeMaximaAlunosTurmaChoqueHorarioRegistroAula() {
		try {
			executarVerificacaoQtdeMaximaAlunosTurmaChoqueHorarioRegistroAulaComException();
		} catch (Exception e) {
			if (e instanceof ConsistirException && ((ConsistirException) e).getReferenteChoqueHorario()) {
				msgChoqueHorario.add(UteisJSF.internacionalizar("msg_DistribuicaoSubTurma_choqueHorario").replace("{0}", getMptdTemp().getMatriculaPeriodoObjetoVO().getMatriculaVO().getMatricula()).replace("{1}", getMptdTemp().getMatriculaPeriodoObjetoVO().getMatriculaVO().getAluno().getNome()).replace("{2}", e.getMessage()));
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
	}

	private void executarVerificacaoQtdeMaximaAlunosTurmaChoqueHorarioRegistroAulaComException() throws Exception {
		setApresentarMensagemQtdeHorasAbono(null);		
		if(getTipoSubTurma().equals(TipoSubTurmaEnum.GERAL)){
			getMptdTemp().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getMptdTemp().getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_PROCESSAMENTO, getUsuarioLogado()));
		}else if(getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)){
			getMptdTemp().setTurmaPratica(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getMptdTemp().getTurmaPratica().getCodigo(), Uteis.NIVELMONTARDADOS_PROCESSAMENTO, getUsuarioLogado()));
		}else if(getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)){
			getMptdTemp().setTurmaTeorica(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getMptdTemp().getTurmaTeorica().getCodigo(), Uteis.NIVELMONTARDADOS_PROCESSAMENTO, getUsuarioLogado()));
		}
		if (getMptdTemp().getTurma().getTurmaAgrupada() && getTipoSubTurma().equals(TipoSubTurmaEnum.GERAL)) {
			throw new Exception(UteisJSF.internacionalizar("msg_DistribuicaoSubturma_turmaAgrupadaTipoGeral"));
		}
		getFacadeFactory().getDistribuicaoSubturmaFacade().executarAlteracaoTurmaAlunoDistribuicaoSubturmaManual(getTurmaPrincipal(), getMptdTemp(), getSubturmaVOs(), getMatriculaPeriodoTurmaDisciplinaVOs(), getAno(), getSemestre(), obterDisciplinaUtilizar(), getUsuarioLogado(), getDisciplinaFazParteComposicaoSelecinada(), getTipoSubTurma(), getRemoverVinculoSubturmaTeoricaPratica());
		setExisteRegistroAula(getMptdTemp().getExisteRegistroAula());
		if (getExisteRegistroAula()) {
			verificarRegistroAulaParaAbono();
		} else {
			setMensagemID("msg_dados_gravados");
		}
		setApresentarPainelAlunoVinculadoSubturmaTeoricaOuPratica(false);
		setRealizarValidacaoAlunoVinculadoSubturmaTeoricaOuPratica(true);
		setRemoverVinculoSubturmaTeoricaPratica(false);
	}

	private void verificarRegistroAulaParaAbono() throws Exception {
		try {
			getFacadeFactory().getDistribuicaoSubturmaFacade().verificarRegistroAulaParaAbono(getMptdTemp().getMatriculaObjetoVO().getMatricula(), getTurmaPrincipal(), getMptdTemp().getTurma(), getMptdTemp().getDisciplina().getCodigo(), getMptdTemp().getMatriculaPeriodoObjetoVO(), false, getUsuarioLogado());
			persistirMatriculaPeriodoTurmaDisciplina();
			setApresentarMensagemQtdeHorasAbono(false);
		} catch (Exception e) {
			setApresentarMensagemQtdeHorasAbono(true);
			throw e;
		}
	}

	public void persistirMatriculaPeriodoTurmaDisciplina() {
		try {
			getFacadeFactory().getDistribuicaoSubturmaFacade().executarAlteracaoTurmaPorMatriculaPeriodoTurmaDisciplinaValidandoRegistroAulaAbono(getTurmaPrincipal(), getMptdTemp(), getUsuarioLogado(), getDisciplinaFazParteComposicaoSelecinada(), getTipoSubTurma());
			buscarAlunoSubturma();
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getAbrirModalMensagemRegistroAula() {
		if (getApresentarMensagemQtdeHorasAbono()) {
			return "RichFaces.$('panelRegistroAula').show()";
		}
		return "";
	}

	public String getApresentarModalConfirmacaoAbonoRegistroAula() {
		if (Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaComRegistroAulaVOs())) {
			return "RichFaces.$('panelAbono').show()";
		}
		return "";
	}

	public String getApresentarModalChoqueHorario() {
		if (Uteis.isAtributoPreenchido(getMsgChoqueHorario())) {
			return "RichFaces.$('panelChoqueHorario').show()";
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public void montarListaSelectItemDisciplinaDeAcordoTurmaPrincipal() throws Exception {
		List<DisciplinaVO> disciplinaVOs = getFacadeFactory().getDisciplinaFacade().consultarPorTurmaAnoSemestre(getTurmaPrincipal().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		Iterator<DisciplinaVO> i = disciplinaVOs.iterator();
		while (i.hasNext()) {
			DisciplinaVO obj = (DisciplinaVO) i.next();
			setDisciplina(getDisciplina().equals(0) ? obj.getCodigo() : getDisciplina());
			objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List<SelectItem>) objs, ordenador);
		setListaSelectItemDisciplina(objs);
		montarListaSelectItemDisciplinaComposta();
	}

	@SuppressWarnings("unchecked")
	public void montarListaSelectItemSubturma() {
		try {
			List<TurmaVO> turmaVOs = getFacadeFactory().getTurmaFacade().consultarPorSubTurma(getTurmaPrincipal(), obterDisciplinaUtilizar(), true, false, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, getUsuarioLogado(), getTipoSubTurma(), false, getAno(), getSemestre(), "AB");
			setListaSelectItemSubturma(new ArrayList<SelectItem>(0));
			getListaSelectItemSubturma().add(new SelectItem(0, ""));
			for (TurmaVO turmaVO : turmaVOs) {
				if (turmaVO.getSituacao().equals("AB"))
					getListaSelectItemSubturma().add(new SelectItem(turmaVO.getCodigo(), turmaVO.getIdentificadorTurma()));
			}
			Collections.sort(getListaSelectItemSubturma(), new SelectItemOrdemValor());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosTurmaPrincipal() {
		novo();
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
		tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
		tipoConsultaComboTurma.add(new SelectItem("nomeCurso", "Curso"));
		tipoConsultaComboTurma.add(new SelectItem("nomeTurno", "Turno"));
		tipoConsultaComboTurma.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		return tipoConsultaComboTurma;
	}

	public void consultarTurma() {
		try {
			super.consultar();
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaSubturma(getValorConsultaTurma(), 0, false, getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsinoSubturma(getValorConsultaTurma(), 0, false, getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorTurnoSubturma(getValorConsultaTurma(), 0, false, getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaNomeCursoSubturma(getValorConsultaTurma(), 0, false, getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarTurma() {
		try {
			if (!getTurmaPrincipal().getIdentificadorTurma().equals("")) {
				setTurmaPrincipal(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurmaSubturma(getTurmaPrincipal().getIdentificadorTurma(), 0, false, getUnidadeEnsinoLogado().getCodigo().intValue(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				if(!Uteis.isAtributoPreenchido(getTurmaPrincipal())) {
					throw new Exception("Turma não encontrado ou deve ser informado uma turma principal.");
				}
				if (getTurmaPrincipal().getSemestral()) {
					setSemestre(Uteis.getSemestreAtual());
					setAno(Uteis.getAnoDataAtual4Digitos());
				} else if (getTurmaPrincipal().getAnual()) {
					setSemestre("");
					setAno(Uteis.getAnoDataAtual4Digitos());
				} else {
					setSemestre("");
					setAno("");
				}
				montarListaSelectItemDisciplinaDeAcordoTurmaPrincipal();
			}
			limparDadosLista();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public boolean getIsApresentarAno() {
		return getTurmaPrincipal().getSemestral() || getTurmaPrincipal().getAnual();
	}

	public boolean getIsApresentarSemestre() {
		return getTurmaPrincipal().getSemestral();
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurmaPrincipal(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			if (getTurmaPrincipal().getSemestral()) {
				setSemestre(Uteis.getSemestreAtual());
				setAno(Uteis.getAnoDataAtual4Digitos());
			} else if (getTurmaPrincipal().getAnual()) {
				setSemestre("");
				setAno(Uteis.getAnoDataAtual4Digitos());
			} else {
				setSemestre("");
				setAno("");
			}
			limparDadosLista();
			montarListaSelectItemDisciplinaDeAcordoTurmaPrincipal();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<SelectItem> listaSelectItemSituacaoMatriculaPeriodo;

	public List<SelectItem> getListaSelectItemSituacaoMatriculaPeriodo() {
		if (listaSelectItemSituacaoMatriculaPeriodo == null) {
			listaSelectItemSituacaoMatriculaPeriodo = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoMatriculaPeriodo.add(new SelectItem("AT", "Ativo"));
			listaSelectItemSituacaoMatriculaPeriodo.add(new SelectItem("PR", "Pré-Matricula"));
			listaSelectItemSituacaoMatriculaPeriodo.add(new SelectItem("ATPR", "Ativo e Pré-Matricula"));
		}
		return listaSelectItemSituacaoMatriculaPeriodo;
	}

	public TurmaVO getTurmaPrincipal() {
		if (turmaPrincipal == null) {
			turmaPrincipal = new TurmaVO();
		}
		return turmaPrincipal;
	}

	public void setTurmaPrincipal(TurmaVO turmaPrincipal) {
		this.turmaPrincipal = turmaPrincipal;
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

	public Integer getDisciplina() {
		if (disciplina == null) {
			disciplina = 0;
		}
		return disciplina;
	}

	public void setDisciplina(Integer disciplina) {
		this.disciplina = disciplina;
	}

	public List<SelectItem> getListaSelectItemDisciplina() {
		if (listaSelectItemDisciplina == null) {
			listaSelectItemDisciplina = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplina;
	}

	public void setListaSelectItemDisciplina(List<SelectItem> listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
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

	public TurmaVO getSubturma() {
		if (subturma == null) {
			subturma = new TurmaVO();
		}
		return subturma;
	}

	public void setSubturma(TurmaVO subturma) {
		this.subturma = subturma;
	}

	public List<SelectItem> getListaSelectItemSubturma() {
		if (listaSelectItemSubturma == null) {
			listaSelectItemSubturma = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemSubturma;
	}

	public void setListaSelectItemSubturma(List<SelectItem> listaSelectItemSubturma) {
		this.listaSelectItemSubturma = listaSelectItemSubturma;
	}

	public boolean getIsApresentarFiltros() {
		return Uteis.isAtributoPreenchido(getTurmaPrincipal());
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> getMatriculaPeriodoTurmaDisciplinaVOs() {
		if (matriculaPeriodoTurmaDisciplinaVOs == null) {
			matriculaPeriodoTurmaDisciplinaVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		}
		return matriculaPeriodoTurmaDisciplinaVOs;
	}

	public void setMatriculaPeriodoTurmaDisciplinaVOs(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs) {
		this.matriculaPeriodoTurmaDisciplinaVOs = matriculaPeriodoTurmaDisciplinaVOs;
	}

	public List<TurmaVO> getSubturmaVOs() {
		if (subturmaVOs == null) {
			subturmaVOs = new ArrayList<TurmaVO>(0);
		}
		return subturmaVOs;
	}

	public void setSubturmaVOs(List<TurmaVO> subturmaVOs) {
		this.subturmaVOs = subturmaVOs;
	}

	public MatriculaPeriodoTurmaDisciplinaVO getMptdTemp() {
		if (mptdTemp == null) {
			mptdTemp = new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return mptdTemp;
	}

	public void setMptdTemp(MatriculaPeriodoTurmaDisciplinaVO mptdTemp) {
		this.mptdTemp = mptdTemp;
	}

	public Boolean getExisteRegistroAula() {
		if (existeRegistroAula == null) {
			existeRegistroAula = false;
		}
		return existeRegistroAula;
	}

	public void setExisteRegistroAula(Boolean existeRegistroAula) {
		this.existeRegistroAula = existeRegistroAula;
	}

	public String getSituacaoMatriculaPeriodo() {
		if (situacaoMatriculaPeriodo == null) {
			situacaoMatriculaPeriodo = "";
		}
		return situacaoMatriculaPeriodo;
	}

	public void setSituacaoMatriculaPeriodo(String situacaoMatriculaPeriodo) {
		this.situacaoMatriculaPeriodo = situacaoMatriculaPeriodo;
	}

	public Boolean getRealizarMarcacaoDesmarcacaoTodos() {
		if (realizarMarcacaoDesmarcacaoTodos == null) {
			realizarMarcacaoDesmarcacaoTodos = false;
		}
		return realizarMarcacaoDesmarcacaoTodos;
	}

	public void setRealizarMarcacaoDesmarcacaoTodos(Boolean realizarMarcacaoDesmarcacaoTodos) {
		this.realizarMarcacaoDesmarcacaoTodos = realizarMarcacaoDesmarcacaoTodos;
	}

	public void executarMarcacaoDesmarcacaoTodos() {
		for (MatriculaPeriodoTurmaDisciplinaVO obj : getMatriculaPeriodoTurmaDisciplinaComRegistroAulaVOs()) {
			obj.setRealizarAbonoRegistroAula(getRealizarMarcacaoDesmarcacaoTodos());
		}
	}

	public void executarMarcacaoDesmarcacaoTodosSubturma() {
		for (TurmaVO obj : getSubturmaVOs()) {
			if (obj.getSituacao().equals("AB"))
				obj.setUtilizarSubturmaNaDistribuicao(getRealizarMarcacaoDesmarcacaoTodos());
		}
	}

	public Boolean getApresentarMensagemQtdeHorasAbono() {
		if (apresentarMensagemQtdeHorasAbono == null) {
			apresentarMensagemQtdeHorasAbono = false;
		}
		return apresentarMensagemQtdeHorasAbono;
	}

	public void setApresentarMensagemQtdeHorasAbono(Boolean apresentarMensagemQtdeHorasAbono) {
		this.apresentarMensagemQtdeHorasAbono = apresentarMensagemQtdeHorasAbono;
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> getMatriculaPeriodoTurmaDisciplinaComRegistroAulaVOs() {
		if (matriculaPeriodoTurmaDisciplinaComRegistroAulaVOs == null) {
			matriculaPeriodoTurmaDisciplinaComRegistroAulaVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		}
		return matriculaPeriodoTurmaDisciplinaComRegistroAulaVOs;
	}

	public void setMatriculaPeriodoTurmaDisciplinaComRegistroAulaVOs(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaComRegistroAulaVOs) {
		this.matriculaPeriodoTurmaDisciplinaComRegistroAulaVOs = matriculaPeriodoTurmaDisciplinaComRegistroAulaVOs;
	}

	/**
	 * @return the msgChoqueHorario
	 */
	public List<String> getMsgChoqueHorario() {
		if (msgChoqueHorario == null) {
			msgChoqueHorario = new ArrayList<String>(0);
		}
		return msgChoqueHorario;
	}

	/**
	 * @param msgChoqueHorario
	 *            the msgChoqueHorario to set
	 */
	public void setMsgChoqueHorario(List<String> msgChoqueHorario) {
		this.msgChoqueHorario = msgChoqueHorario;
	}

	public List<SelectItem> getListaSelectItemDisciplinaFazParteComposicao() {
		if (listaSelectItemDisciplinaFazParteComposicao == null) {
			listaSelectItemDisciplinaFazParteComposicao = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplinaFazParteComposicao;
	}

	public void setListaSelectItemDisciplinaFazParteComposicao(List<SelectItem> listaSelectItemDisciplinaFazParteComposicao) {
		this.listaSelectItemDisciplinaFazParteComposicao = listaSelectItemDisciplinaFazParteComposicao;
	}

	public DisciplinaVO getDisciplinaFazParteComposicao() {
		if (disciplinaFazParteComposicao == null) {
			disciplinaFazParteComposicao = new DisciplinaVO();
		}
		return disciplinaFazParteComposicao;
	}

	public void setDisciplinaFazParteComposicao(DisciplinaVO disciplinaFazParteComposicao) {
		this.disciplinaFazParteComposicao = disciplinaFazParteComposicao;
	}

	public Boolean getApresentarComboboxDisciplinaFazParteComposicao() {
		if (apresentarComboboxDisciplinaFazParteComposicao == null) {
			apresentarComboboxDisciplinaFazParteComposicao = false;
		}
		return apresentarComboboxDisciplinaFazParteComposicao;
	}

	public void setApresentarComboboxDisciplinaFazParteComposicao(Boolean apresentarComboboxDisciplinaFazParteComposicao) {
		this.apresentarComboboxDisciplinaFazParteComposicao = apresentarComboboxDisciplinaFazParteComposicao;
	}

	public void executarVerificacaoDisciplinaComposta() {
		try {
			montarListaSelectItemDisciplinaComposta();
			limparDadosLista();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private void montarListaSelectItemDisciplinaComposta() throws Exception {
		List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs = getFacadeFactory().getGradeDisciplinaCompostaFacade().consultarPorTurmaDisciplina(getTurmaPrincipal().getCodigo(), getDisciplina(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		setListaSelectItemDisciplinaFazParteComposicao(null);
		getListaSelectItemDisciplinaFazParteComposicao().add(new SelectItem(0, ""));
		setApresentarComboboxDisciplinaFazParteComposicao(Uteis.isAtributoPreenchido(gradeDisciplinaCompostaVOs));
		for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeDisciplinaCompostaVOs) {
			getListaSelectItemDisciplinaFazParteComposicao().add(new SelectItem(gradeDisciplinaCompostaVO.getDisciplina().getCodigo(), gradeDisciplinaCompostaVO.getDisciplina().getNome()));
		}
		Collections.sort((List<SelectItem>) getListaSelectItemDisciplinaFazParteComposicao(), new SelectItemOrdemValor());
	}

	/**
	 * Responsável por obter a disciplina de acordo com o selecionado quando a disciplina seja composta
	 * 
	 * @author Wellington - 10 de set de 2015
	 * @return
	 */
	private Integer obterDisciplinaUtilizar() {
		if (getDisciplinaFazParteComposicaoSelecinada()) {
			return getDisciplinaFazParteComposicao().getCodigo();
		}
		return getDisciplina();
	}

	private boolean getDisciplinaFazParteComposicaoSelecinada() {
		return Uteis.isAtributoPreenchido(getDisciplinaFazParteComposicao());
	}

	public void limparDadosLista() {
		setMatriculaPeriodoTurmaDisciplinaVOs(new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0));
		setSubturmaVOs(new ArrayList<TurmaVO>(0));
		setMensagemID("msg_entre_dados");
	}

	public TipoSubTurmaEnum getTipoSubTurma() {
		if (tipoSubTurma == null) {
			tipoSubTurma = TipoSubTurmaEnum.GERAL;
		}
		return tipoSubTurma;
	}

	public void setTipoSubTurma(TipoSubTurmaEnum tipoSubTurma) {
		this.tipoSubTurma = tipoSubTurma;
	}

	public void executarVerificacaoAlunoVinculadoTipoSubturmaTeoricaOuPratica() {
		try {
			if (getRealizandoDistribuicaoManual()) {
				setMptdTemp((MatriculaPeriodoTurmaDisciplinaVO) context().getExternalContext().getRequestMap().get("mptdItens"));
				if (getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA) && !Uteis.isAtributoPreenchido(getMptdTemp().getTurmaTeorica())) {
					throw new Exception(UteisJSF.internacionalizar("msg_DistribuicaoSubturma_turma"));
				} else if (getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA) && !Uteis.isAtributoPreenchido(getMptdTemp().getTurmaPratica())) {
					throw new Exception(UteisJSF.internacionalizar("msg_DistribuicaoSubturma_turma"));
				} else if (!Uteis.isAtributoPreenchido(getMptdTemp().getTurma())) {
					throw new Exception(UteisJSF.internacionalizar("msg_DistribuicaoSubturma_turma"));
				}
				if (getRealizarValidacaoAlunoVinculadoSubturmaTeoricaOuPratica()) {
					List<MatriculaPeriodoTurmaDisciplinaVO> mptdVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
					getMptdTemp().setNovoObj(true);
					mptdVOs.add(getMptdTemp());
					executarVerificacaoAlunoVinculadoTipoSubturmaTeoricaOuPratica(mptdVOs);
					if (!getApresentarPainelAlunoVinculadoSubturmaTeoricaOuPratica()) {
						executarVerificacaoQtdeMaximaAlunosTurmaChoqueHorarioRegistroAulaComException();
					}
				} else {
					executarVerificacaoQtdeMaximaAlunosTurmaChoqueHorarioRegistroAulaComException();
				}
			} else {
				if (getRealizarValidacaoAlunoVinculadoSubturmaTeoricaOuPratica()) {
					for (TurmaVO subTurma : getSubturmaVOs()) {
						executarVerificacaoAlunoVinculadoTipoSubturmaTeoricaOuPratica(subTurma.getMatriculaPeriodoTurmaDisciplinaVOs());
					}
					if (!getApresentarPainelAlunoVinculadoSubturmaTeoricaOuPratica()) {
						persistirComException();
					}
				} else {
					persistirComException();
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private void executarVerificacaoAlunoVinculadoTipoSubturmaTeoricaOuPratica(List<MatriculaPeriodoTurmaDisciplinaVO> mptdVOs) throws Exception {
		try {
			getFacadeFactory().getDistribuicaoSubturmaFacade().executarVerificacaoAlunoVinculadoTipoSubturmaTeoricaOuPratica(mptdVOs, getTipoSubTurma());
			setApresentarPainelAlunoVinculadoSubturmaTeoricaOuPratica(false);
		} catch (Exception e) {
			setApresentarPainelAlunoVinculadoSubturmaTeoricaOuPratica(true);
			throw e;
		}
	}

	public Boolean getApresentarPainelAlunoVinculadoSubturmaTeoricaOuPratica() {
		if (apresentarPainelAlunoVinculadoSubturmaTeoricaOuPratica == null) {
			apresentarPainelAlunoVinculadoSubturmaTeoricaOuPratica = false;
		}
		return apresentarPainelAlunoVinculadoSubturmaTeoricaOuPratica;
	}

	public void setApresentarPainelAlunoVinculadoSubturmaTeoricaOuPratica(Boolean apresentarPainelAlunoVinculadoSubturmaTeoricaOuPratica) {
		this.apresentarPainelAlunoVinculadoSubturmaTeoricaOuPratica = apresentarPainelAlunoVinculadoSubturmaTeoricaOuPratica;
	}

	public Boolean getRealizandoDistribuicaoManual() {
		if (realizandoDistribuicaoManual == null) {
			realizandoDistribuicaoManual = false;
		}
		return realizandoDistribuicaoManual;
	}

	public void setRealizandoDistribuicaoManual(Boolean realizandoDistribuicaoManual) {
		this.realizandoDistribuicaoManual = realizandoDistribuicaoManual;
	}

	public Boolean getRealizarValidacaoAlunoVinculadoSubturmaTeoricaOuPratica() {
		if (realizarValidacaoAlunoVinculadoSubturmaTeoricaOuPratica == null) {
			realizarValidacaoAlunoVinculadoSubturmaTeoricaOuPratica = true;
		}
		return realizarValidacaoAlunoVinculadoSubturmaTeoricaOuPratica;
	}

	public void setRealizarValidacaoAlunoVinculadoSubturmaTeoricaOuPratica(Boolean realizarValidacaoAlunoVinculadoSubturmaTeoricaOuPratica) {
		this.realizarValidacaoAlunoVinculadoSubturmaTeoricaOuPratica = realizarValidacaoAlunoVinculadoSubturmaTeoricaOuPratica;
	}

	public Boolean getRemoverVinculoSubturmaTeoricaPratica() {
		if (removerVinculoSubturmaTeoricaPratica == null) {
			removerVinculoSubturmaTeoricaPratica = false;
		}
		return removerVinculoSubturmaTeoricaPratica;
	}

	public void setRemoverVinculoSubturmaTeoricaPratica(Boolean removerVinculoSubturmaTeoricaPratica) {
		this.removerVinculoSubturmaTeoricaPratica = removerVinculoSubturmaTeoricaPratica;
	}

}
