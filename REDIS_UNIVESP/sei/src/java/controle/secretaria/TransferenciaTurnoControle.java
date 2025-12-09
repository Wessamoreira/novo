package controle.secretaria;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoFinanceiroCursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.secretaria.TransferenciaTurnoDisciplinaVO;
import negocio.comuns.secretaria.TransferenciaTurnoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("TransferenciaTurnoControle")
@Scope("viewScope")
@Lazy
public class TransferenciaTurnoControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	private TransferenciaTurnoVO transferenciaTurnoVO;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private List<MatriculaVO> listaConsultaAluno;
	private List<SelectItem> listaSelectItemNovoTurno;
	private List<SelectItem> listaSelectItemNovoPlanoFinanceiroCurso;
	private List<SelectItem> listaSelectItemNovaTurma;
	private List<SelectItem> listaSelectItemNovaCondicaoPagamento;
	private Boolean abrirModalRegistrarAbonoDisciplina;
	private Boolean realizarMarcacaoDesmarcacaoTodosTransferenciaTurnoDisciplina;

	public TransferenciaTurnoControle() {
	}

	public String novo() {
		try {
			setTransferenciaTurnoVO(new TransferenciaTurnoVO());
			getTransferenciaTurnoVO().setResponsavel(getUsuarioLogadoClone());
			limparMensagem();
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaTurnoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		getControleConsultaOtimizado().getListaConsulta().clear();
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaTurnoCons.xhtml");
	}

	public String consultar() {
		getControleConsultaOtimizado().getListaConsulta().clear();
		getControleConsultaOtimizado().setLimitePorPagina(10);
		try {
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getTransferenciaTurnoFacade().consultar(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getTransferenciaTurnoFacade().consultarQuantidadeTotalRegistros(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta()));
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaTurnoCons.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultar();
	}

	public String editar() throws Exception {
		try {
			TransferenciaTurnoVO obj = (TransferenciaTurnoVO) context().getExternalContext().getRequestMap().get("transferenciaTurnoItens");
			setTransferenciaTurnoVO(getFacadeFactory().getTransferenciaTurnoFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			montarListaSelectItemNovoTurno();
			montarListaSelectItemNovaTurma();
			montarListaSelectItemNovoPlanoFinanceiro();
			montarListaSelectItemCondicoesPagamentoPlanoFinanceiroCurso();
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaTurnoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaTurnoForm.xhtml");
		}
	}

	/**
	 * Método responsável por preencher e mostrar a lista de disciplinas
	 * cursadas pelo aluno em sua última Matricula Período.
	 */
	public void montarListaDisciplinas() {
		try {
			getFacadeFactory().getTransferenciaTurnoFacade().realizarPreenchimentoObjetosParaPersistencia(getTransferenciaTurnoVO().getMatriculaPeriodoOrigem(), getTransferenciaTurnoVO().getMatriculaPeriodoDestino(), getUsuarioLogado());
			getFacadeFactory().getTransferenciaTurnoDisciplinaFacade().montarDadosTransferenciaTurnoDisciplina(getTransferenciaTurnoVO(), getUsuarioLogado());
			if (getTransferenciaTurnoVO().getTransferenciaTurnoDisciplinaVOs().isEmpty()) {
				throw new Exception("Não existe nenhum disciplina adicionada a matricula do aluno, para realização da transferência!");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método que irá chamar a rotina de transferência de turno, fazendo todas
	 * as operações necessárias para o seu funcionamento.
	 */
	public void gravar() {
		try {
			getFacadeFactory().getTransferenciaTurnoFacade().persistir(getTransferenciaTurnoVO(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			getTransferenciaTurnoVO().setTransferenciaTurnoDisciplinaVOs(getFacadeFactory().getTransferenciaTurnoDisciplinaFacade().consultarPorTransferenciaTurno(getTransferenciaTurnoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void executarVerificacaoExisteRegistroAulaParaAbono() {
		try {
			setAbrirModalRegistrarAbonoDisciplina(getFacadeFactory().getTransferenciaTurnoFacade().verificarRegistroAulaParaAbono(getTransferenciaTurnoVO(), false, getUsuarioLogado()));
			if (!getAbrirModalRegistrarAbonoDisciplina()) {
				gravar();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void executarVerificacaoQtdeMaximaAlunosTurmaChoqueHorarioRegistroAula() {
		try {
			TransferenciaTurnoDisciplinaVO obj = (TransferenciaTurnoDisciplinaVO) context().getExternalContext().getRequestMap().get("transferenciaTurnoDisciplinaVOItens");
			if (obj != null) {
				getFacadeFactory().getTransferenciaTurnoDisciplinaFacade().executarGeracaoListaSelectItemTurmaTeoricaPraticaTransferenciaTurnoDisciplinaVO(obj, getUsuarioLogado());
				obj.setExisteRegistroAula(getFacadeFactory().getDistribuicaoSubturmaFacade().executarVerificarQtdeMaximaAlunosTurmaChoqueHorarioRegistroAula(getTransferenciaTurnoVO().getMatriculaPeriodoOrigem().getTurma(), obj.getMatriculaPeriodoTurmaDisciplinaVO().getTurma(), obj.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina(), getTransferenciaTurnoVO().getMatriculaPeriodoOrigem(), getUsuarioLogado(), TipoSubTurmaEnum.GERAL, getTransferenciaTurnoVO().getMatriculaPeriodoOrigem().getMatriculaVO().getCurso().getConfiguracaoAcademico().getValidarChoqueHorarioOutraMatriculaAluno()));
				if (Uteis.isAtributoPreenchido(obj.getTurmaPraticaAntiga())) {
					obj.setExisteRegistroAulaPratica(getFacadeFactory().getDistribuicaoSubturmaFacade().executarVerificarQtdeMaximaAlunosTurmaChoqueHorarioRegistroAula(obj.getTurmaPraticaAntiga(), obj.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaPratica(), obj.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina(), getTransferenciaTurnoVO().getMatriculaPeriodoOrigem(), getUsuarioLogado(), TipoSubTurmaEnum.PRATICA, getTransferenciaTurnoVO().getMatriculaPeriodoOrigem().getMatriculaVO().getCurso().getConfiguracaoAcademico().getValidarChoqueHorarioOutraMatriculaAluno()));
				}
				if (Uteis.isAtributoPreenchido(obj.getTurmaTeoricaAntiga())) {
					obj.setExisteRegistroAulaTeorica(getFacadeFactory().getDistribuicaoSubturmaFacade().executarVerificarQtdeMaximaAlunosTurmaChoqueHorarioRegistroAula(obj.getTurmaTeoricaAntiga(), obj.getMatriculaPeriodoTurmaDisciplinaVO().getTurmaTeorica(), obj.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina(), getTransferenciaTurnoVO().getMatriculaPeriodoOrigem(), getUsuarioLogado(), TipoSubTurmaEnum.TEORICA, getTransferenciaTurnoVO().getMatriculaPeriodoOrigem().getMatriculaVO().getCurso().getConfiguracaoAcademico().getValidarChoqueHorarioOutraMatriculaAluno()));
				}
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	}

	public String getApresentarModalConfirmacaoAbonoRegistroAula() {
		if (getAbrirModalRegistrarAbonoDisciplina()) {
			return "RichFaces.$('panelAbono').show();";
		}
		return "RichFaces.$('panelDisciplinasTurma').hide();";
	}

	/**
	 * Método que controla a renderização ou não do ModalPanel das disciplinas
	 * do aluno.
	 *
	 * @return String que mostra o ModalPanel
	 */
	public String getMostrarModalDisciplinasTurma() {
		if (Uteis.isAtributoPreenchido(getTransferenciaTurnoVO().getTransferenciaTurnoDisciplinaVOs())) {
			return "RichFaces.$('panelDisciplinasTurma').show()";
		}
		return "";
	}

	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("O campo Consulta (Tranferência de Turno) deve ser informado.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(obj.getMatricula())) {
					objs.add(obj);
				}
			} else if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			} else if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			if (!Uteis.isAtributoPreenchido(getTransferenciaTurnoVO().getMatriculaPeriodoOrigem().getMatriculaVO().getMatricula())) {
				throw new Exception("O campo Matrícula (Tranferência de Turno) deve ser informado.");
			}
			MatriculaPeriodoVO objCompleto = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatriculaOrdenandoPorAnoSemestrePeriodoLetivo(getTransferenciaTurnoVO().getMatriculaPeriodoOrigem().getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(objCompleto.getMatricula(), objCompleto.getMatriculaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
			objCompleto.setMatriculaPeriodoTumaDisciplinaVOs(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorMatriculaPeriodo(objCompleto.getCodigo(), false, getUsuarioLogado()));
			getFacadeFactory().getTransferenciaTurnoFacade().validarDadosPermiteTransferenciaTurno(obj, objCompleto, getUsuarioLogado());
			getTransferenciaTurnoVO().setMatriculaPeriodoOrigem(objCompleto);
			montarListaSelectItemNovoTurno();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			novo();		
			setMensagemDetalhada("msg_erro", e.getMessage());
			getTransferenciaTurnoVO().getMatriculaPeriodoOrigem().setMatriculaVO(new MatriculaVO());
		}
	}

	public void limparDadosAluno() throws Exception {
		setTransferenciaTurnoVO(new TransferenciaTurnoVO());
		setListaSelectItemNovaTurma(null);
		setListaSelectItemNovoPlanoFinanceiroCurso(null);
		setListaSelectItemNovoTurno(null);
		setListaSelectItemNovaCondicaoPagamento(null);
		setMensagemID("msg_entre_prmconsulta");
	}

	public void selecionarAluno() throws Exception {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
			MatriculaPeriodoVO objCompleto = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatriculaOrdenandoPorAnoSemestrePeriodoLetivo(obj.getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			objCompleto.setMatriculaPeriodoTumaDisciplinaVOs(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorMatriculaPeriodo(objCompleto.getCodigo(), false, getUsuarioLogado()));
			getFacadeFactory().getTransferenciaTurnoFacade().validarDadosPermiteTransferenciaTurno(obj, objCompleto, getUsuarioLogado());
			getTransferenciaTurnoVO().setMatriculaPeriodoOrigem(objCompleto);
			obj = null;
			objCompleto = null;
			setValorConsultaAluno("");
			setCampoConsultaAluno("");
			getListaConsultaAluno().clear();
			montarListaSelectItemNovoTurno();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void montarListaSelectItemNovoTurno() throws Exception {
		List<TurnoVO> listaConsultaSelectItem = getFacadeFactory().getTurnoFacade().consultarPorUnidadeEnsinoCurso(getTransferenciaTurnoVO().getMatriculaPeriodoOrigem().getMatriculaVO().getUnidadeEnsino().getCodigo(), getTransferenciaTurnoVO().getMatriculaPeriodoOrigem().getMatriculaVO().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		setListaSelectItemNovoTurno(UtilSelectItem.getListaSelectItem(listaConsultaSelectItem, "codigo", "nome"));
	}

	public void montarListaSelectItemNovoPlanoFinanceiro() throws Exception {
		List<PlanoFinanceiroCursoVO> listaConsultaSelectItem = new ArrayList<PlanoFinanceiroCursoVO>(0);
		try {
			if (Uteis.isAtributoPreenchido(getTransferenciaTurnoVO().getMatriculaPeriodoDestino().getTurma())) {
				getTransferenciaTurnoVO().getMatriculaPeriodoOrigem().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTransferenciaTurnoVO().getMatriculaPeriodoOrigem().getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				if (getTransferenciaTurnoVO().getMatriculaPeriodoOrigem().getTurma().getPlanoFinanceiroCurso().getCodigo() != 0) {
					listaConsultaSelectItem = getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPorCursoETurnoVinculadoTurma(getTransferenciaTurnoVO().getMatriculaPeriodoOrigem().getMatriculaVO().getCurso().getCodigo(), getTransferenciaTurnoVO().getMatriculaPeriodoDestino().getMatriculaVO().getTurno().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				} else {
					listaConsultaSelectItem = getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPorCursoETurnoVinculadoUnidadeEnsinoCurso(getTransferenciaTurnoVO().getMatriculaPeriodoOrigem().getMatriculaVO().getCurso().getCodigo(), getTransferenciaTurnoVO().getMatriculaPeriodoDestino().getMatriculaVO().getTurno().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				}
				setListaSelectItemNovoPlanoFinanceiroCurso(UtilSelectItem.getListaSelectItem(listaConsultaSelectItem, "codigo", "descricao"));
				for(SelectItem selectItem : getListaSelectItemNovoPlanoFinanceiroCurso()){
					if(getTransferenciaTurnoVO().getMatriculaPeriodoOrigem().getPlanoFinanceiroCurso().getCodigo().equals((Integer)selectItem.getValue())){						
						getTransferenciaTurnoVO().getMatriculaPeriodoDestino().getPlanoFinanceiroCurso().setCodigo(getTransferenciaTurnoVO().getMatriculaPeriodoOrigem().getPlanoFinanceiroCurso().getCodigo());
						montarListaSelectItemCondicoesPagamentoPlanoFinanceiroCurso();						
						getTransferenciaTurnoVO().getMatriculaPeriodoDestino().getCondicaoPagamentoPlanoFinanceiroCurso().setCodigo(getTransferenciaTurnoVO().getMatriculaPeriodoOrigem().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo());
					}
				}				
			} else {
				setListaSelectItemNovoPlanoFinanceiroCurso(new ArrayList<SelectItem>(0));
				setListaSelectItemNovaCondicaoPagamento(new ArrayList<SelectItem>(0));
			}
			setMensagemID("msg_dados_consultados");
		} finally {
			listaConsultaSelectItem = null;
		}
	}

	public void montarListaSelectItemCondicoesPagamentoPlanoFinanceiroCurso() throws Exception {
		List<CondicaoPagamentoPlanoFinanceiroCursoVO> listaConsultaSelectItem = getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorCodigoPlanoFinanceiroCurso(getTransferenciaTurnoVO().getMatriculaPeriodoDestino().getPlanoFinanceiroCurso().getCodigo(), "AT", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		setListaSelectItemNovaCondicaoPagamento(UtilSelectItem.getListaSelectItem(listaConsultaSelectItem, "codigo", "descricao"));
		setMensagemID("msg_dados_consultados");
	}

	public Boolean getIsMostrarListaCondicoes() {
		return Uteis.isAtributoPreenchido(getListaSelectItemNovaCondicaoPagamento());
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public void montarListaSelectItemNovaTurma() {
		try {
			List<TurmaVO> turmaVOs = getFacadeFactory().getTurmaFacade().consultaRapidaPorNrPeriodoLetivoUnidadeEnsinoCursoTurno(getTransferenciaTurnoVO().getMatriculaPeriodoOrigem().getPeridoLetivo().getPeriodoLetivo(), getTransferenciaTurnoVO().getMatriculaPeriodoOrigem().getMatriculaVO().getUnidadeEnsino().getCodigo(), getTransferenciaTurnoVO().getMatriculaPeriodoOrigem().getMatriculaVO().getCurso().getCodigo(), getTransferenciaTurnoVO().getMatriculaPeriodoDestino().getMatriculaVO().getTurno().getCodigo(), getTransferenciaTurnoVO().getMatriculaPeriodoOrigem().getMatriculaVO().getGradeCurricularAtual().getCodigo(), false, false, true, getTransferenciaTurnoVO().getMatriculaPeriodoOrigem().getMatriculaVO().getCurso().getConfiguracaoAcademico().getMatricularApenasDisciplinaAulaProgramada(), getTransferenciaTurnoVO().getMatriculaPeriodoOrigem().getAno(), getTransferenciaTurnoVO().getMatriculaPeriodoOrigem().getSemestre(),  false, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema(), false);
			setListaSelectItemNovaTurma(UtilSelectItem.getListaSelectItem(turmaVOs, "codigo", "identificadorTurma"));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> objs = new ArrayList<SelectItem>();
		objs.add(new SelectItem("matricula", "Matrícula"));
		objs.add(new SelectItem("aluno", "Aluno"));
		return objs;
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

	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	public List<MatriculaVO> getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<MatriculaVO>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public List<SelectItem> getListaSelectItemNovoTurno() {
		if (listaSelectItemNovoTurno == null) {
			listaSelectItemNovoTurno = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNovoTurno;
	}

	public void setListaSelectItemNovoTurno(List<SelectItem> listaSelectItemNovoTurno) {
		this.listaSelectItemNovoTurno = listaSelectItemNovoTurno;
	}

	public List<SelectItem> getListaSelectItemNovoPlanoFinanceiroCurso() {
		if (listaSelectItemNovoPlanoFinanceiroCurso == null) {
			listaSelectItemNovoPlanoFinanceiroCurso = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNovoPlanoFinanceiroCurso;
	}

	public void setListaSelectItemNovoPlanoFinanceiroCurso(List<SelectItem> listaSelectItemNovoPlanoFinanceiroCurso) {
		this.listaSelectItemNovoPlanoFinanceiroCurso = listaSelectItemNovoPlanoFinanceiroCurso;
	}

	public List<SelectItem> getListaSelectItemNovaTurma() {
		if (listaSelectItemNovaTurma == null) {
			listaSelectItemNovaTurma = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNovaTurma;
	}

	public void setListaSelectItemNovaTurma(List<SelectItem> listaSelectItemNovaTurma) {
		this.listaSelectItemNovaTurma = listaSelectItemNovaTurma;
	}

	public void setListaSelectItemNovaCondicaoPagamento(List<SelectItem> listaSelectItemNovaCondicaoPagamento) {
		this.listaSelectItemNovaCondicaoPagamento = listaSelectItemNovaCondicaoPagamento;
	}

	public List<SelectItem> getListaSelectItemNovaCondicaoPagamento() {
		if (listaSelectItemNovaCondicaoPagamento == null) {
			listaSelectItemNovaCondicaoPagamento = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNovaCondicaoPagamento;
	}

	public TransferenciaTurnoVO getTransferenciaTurnoVO() {
		if (transferenciaTurnoVO == null) {
			transferenciaTurnoVO = new TransferenciaTurnoVO();
		}
		return transferenciaTurnoVO;
	}

	public void setTransferenciaTurnoVO(TransferenciaTurnoVO transferenciaTurnoVO) {
		this.transferenciaTurnoVO = transferenciaTurnoVO;
	}

	public Boolean getAbrirModalRegistrarAbonoDisciplina() {
		if (abrirModalRegistrarAbonoDisciplina == null) {
			abrirModalRegistrarAbonoDisciplina = false;
		}
		return abrirModalRegistrarAbonoDisciplina;
	}

	public void setAbrirModalRegistrarAbonoDisciplina(Boolean abrirModalRegistrarAbonoDisciplina) {
		this.abrirModalRegistrarAbonoDisciplina = abrirModalRegistrarAbonoDisciplina;
	}

	public void executarMarcacaoDesmarcacaoTodosTransferenciaTurnoDisciplinaVOs() {
		for (TransferenciaTurnoDisciplinaVO obj : getTransferenciaTurnoVO().getTransferenciaTurnoDisciplinaVOs()) {
			obj.setRealizarAbonoRegistroAula(getRealizarMarcacaoDesmarcacaoTodosTransferenciaTurnoDisciplina());
		}
	}

	public Boolean getRealizarMarcacaoDesmarcacaoTodosTransferenciaTurnoDisciplina() {
		if (realizarMarcacaoDesmarcacaoTodosTransferenciaTurnoDisciplina == null) {
			realizarMarcacaoDesmarcacaoTodosTransferenciaTurnoDisciplina = false;
		}
		return realizarMarcacaoDesmarcacaoTodosTransferenciaTurnoDisciplina;
	}

	public void setRealizarMarcacaoDesmarcacaoTodosTransferenciaTurnoDisciplina(Boolean realizarMarcacaoDesmarcacaoTodosTransferenciaTurnoDisciplina) {
		this.realizarMarcacaoDesmarcacaoTodosTransferenciaTurnoDisciplina = realizarMarcacaoDesmarcacaoTodosTransferenciaTurnoDisciplina;
	}

}
