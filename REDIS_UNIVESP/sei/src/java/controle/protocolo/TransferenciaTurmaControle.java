package controle.protocolo;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoAcademicoEnum;
import negocio.comuns.protocolo.TransferenciaTurmaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Controller("TransferenciaTurmaControle")
@Scope("viewScope")
public class TransferenciaTurmaControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	private TransferenciaTurmaVO transferenciaTurmaVO;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private List<MatriculaVO> listaConsultaAluno;
	private List<SelectItem> listaSelectItemDisciplina;
	private List<SelectItem> listaSelectItemTurmaDestino;
	private Boolean imprimirContrato;
	private Integer textoPadraoDeclaracao;
	private List<SelectItem> listaSelectItemTipoTextoPadrao;
	private List<TransferenciaTurmaVO> transferenciaTurmaVOs;
	private Boolean abrirModalRegistrarAbonoDisciplina;
	private Boolean realizarMarcacaoDesmarcacaoTodosTransferenciaTurnoDisciplina;
	private String mensagemAlterarTurmaOrigem;
	private Boolean alterarTurmaOrigem;
	private Boolean permitirIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais;
	
	public TransferenciaTurmaControle() {
		verificarPermissaoIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais();
	}

	public String novo() {
		try {
			setTransferenciaTurmaVO(new TransferenciaTurmaVO());
			setTransferenciaTurmaVOs(null);
			setImprimirContrato(false);
			getTransferenciaTurmaVO().setNovoObj(true);
			setMensagemID("msg_entre_dados");
			verificarPermissaoIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais();
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaTurmaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void gravar() {
		try {
			for (TransferenciaTurmaVO obj : getTransferenciaTurmaVOs()) {
				obj.setObservacao(getTransferenciaTurmaVO().getObservacao());
			}
			getFacadeFactory().getTransferenciaTurmaFacade().persistirTransferenciaTurmaVOs(getTransferenciaTurmaVOs(), getAlterarTurmaOrigem(), getTransferenciaTurmaVO().getTurmaDestino().getCodigo(), false, getUsuarioLogado());
			getTransferenciaTurmaVO().setNovoObj(false);
			consultarListaSelectItemTipoTextoPadrao(0);
			setMensagemID("msg_dados_gravados");
		} catch (ConsistirException ex) {
			getTransferenciaTurmaVO().setNovoObj(true);
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {
			getTransferenciaTurmaVO().setNovoObj(true);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String editar() {
		try {
			setTransferenciaTurmaVO((TransferenciaTurmaVO) context().getExternalContext().getRequestMap().get("transferenciaTurmaItens"));
			setTransferenciaTurmaVO(getFacadeFactory().getTransferenciaTurmaFacade().consultaRapidaPorChavePrimaria(getTransferenciaTurmaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			consultarListaSelectItemTipoTextoPadrao(0);
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaTurmaForm.xhtml");
	}

	public void executarVerificacaoExisteRegistroAulaParaAbono() {
		try {
			setAbrirModalRegistrarAbonoDisciplina(getFacadeFactory().getTransferenciaTurmaFacade().verificarRegistroAulaParaAbono(getTransferenciaTurmaVOs(), false, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void executarVerificacaoQtdeMaximaAlunosTurmaChoqueHorarioRegistroAula() {
		try {
			TransferenciaTurmaVO obj = (TransferenciaTurmaVO) context().getExternalContext().getRequestMap().get("transferenciaTurmaVOItens");
			getFacadeFactory().getTransferenciaTurmaFacade().executarVerificarQtdeMaximaAlunosTurmaChoqueHorario(getTransferenciaTurmaVOs(), obj.getTurmaDestino(), obj.getDisciplinaVO(), obj.getUltimaMatriculaPeriodoAtiva(), TipoSubTurmaEnum.GERAL, getUsuarioLogado());
			getFacadeFactory().getTransferenciaTurmaFacade().executarMontagemTurmaTeoricaPraticaDestino(obj, getTransferenciaTurmaVO(), obj.getMatriculaPeriodoTurmaDisciplinaVO(), getUsuarioLogado());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void executarVerificacaoQtdeMaximaAlunosTurmaTeoricaChoqueHorarioRegistroAula() {
		try {
			TransferenciaTurmaVO obj = (TransferenciaTurmaVO) context().getExternalContext().getRequestMap().get("transferenciaTurmaVOItens");
			getFacadeFactory().getTransferenciaTurmaFacade().executarVerificarQtdeMaximaAlunosTurmaChoqueHorario(getTransferenciaTurmaVOs(), obj.getTurmaTeoricaDestino(), obj.getDisciplinaVO(), obj.getUltimaMatriculaPeriodoAtiva(), TipoSubTurmaEnum.TEORICA, getUsuarioLogado());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void executarVerificacaoQtdeMaximaAlunosTurmaPraticaChoqueHorarioRegistroAula() {
		try {
			TransferenciaTurmaVO obj = (TransferenciaTurmaVO) context().getExternalContext().getRequestMap().get("transferenciaTurmaVOItens");
			getFacadeFactory().getTransferenciaTurmaFacade().executarVerificarQtdeMaximaAlunosTurmaChoqueHorario(getTransferenciaTurmaVOs(), obj.getTurmaPraticaDestino(), obj.getDisciplinaVO(), obj.getUltimaMatriculaPeriodoAtiva(), TipoSubTurmaEnum.PRATICA, getUsuarioLogado());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getApresentarModalConfirmacaoAbonoRegistroAula() {
		if (getAbrirModalRegistrarAbonoDisciplina()) {
			return "RichFaces.$('panelAbono').show();";
		}
		executarVerificacaoApresentarMensagemAlterarTurmaOrigem();
		return "RichFaces.$('panelAlterarTurmaOrigem').show();";
	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		getControleConsultaOtimizado().getListaConsulta().clear();
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaTurmaCons.xhtml");
	}

	public String consultar() {
		getControleConsultaOtimizado().getListaConsulta().clear();
		getControleConsultaOtimizado().setLimitePorPagina(10);
		try {
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getTransferenciaTurmaFacade().consultar(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getTransferenciaTurmaFacade().consultarQuantidadeRegistros(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta()));
			if (Uteis.isAtributoPreenchido(getControleConsultaOtimizado().getListaConsulta())) {
				setMensagemID("msg_dados_consultados");
			} else {
				setMensagemID("msg_entre_prmconsulta");
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaTurmaCons.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaTurmaCons.xhtml");
		}
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultar();
	}

	public void consultarAluno() {
		try {
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), 0, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				if (!obj.getMatricula().equals("")) {
					getListaConsultaAluno().add(obj);
				}
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), 0, false, getUsuarioLogado()));
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), 0, false, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getTransferenciaTurmaVO().getMatriculaVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), 0, Uteis.NIVELMONTARDADOS_COMBOBOX, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				setTransferenciaTurmaVO(new TransferenciaTurmaVO());
				throw new Exception("Aluno de matrícula " + objAluno.getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			verificarSituacaoUltimaMatriculaPeriodo();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() {
		try {
			setTransferenciaTurmaVO(new TransferenciaTurmaVO());
			getTransferenciaTurmaVO().setMatriculaVO((MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens"));
			verificarSituacaoUltimaMatriculaPeriodo();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosAluno() {
		setTransferenciaTurmaVO(new TransferenciaTurmaVO());
		setTransferenciaTurmaVOs(null);
	}

	/**
	 * Método responsável por montar a lista de turmas de destino de acordo com a disciplina trazendo todas as turmas que sejam diferente da turma de
	 * origem.
	 * 
	 * @throws Exception
	 */
	public void montarListaSelectItemTurmaDestino() throws Exception {
		List<TurmaVO> turmaVOs = getFacadeFactory().getTurmaFacade().consultaRapidaPorNrPeriodoLetivoUnidadeEnsinoCursoTurno(getTransferenciaTurmaVO().getUltimaMatriculaPeriodoAtiva().getPeriodoLetivo().getPeriodoLetivo(), getTransferenciaTurmaVO().getUltimaMatriculaPeriodoAtiva().getUnidadeEnsinoCursoVO().getUnidadeEnsino(), getTransferenciaTurmaVO().getUltimaMatriculaPeriodoAtiva().getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTransferenciaTurmaVO().getUltimaMatriculaPeriodoAtiva().getUnidadeEnsinoCursoVO().getTurno().getCodigo(), getTransferenciaTurmaVO().getMatriculaVO().getGradeCurricularAtual().getCodigo(), false, false, true, getTransferenciaTurmaVO().getUltimaMatriculaPeriodoAtiva().getMatriculaVO().getCurso().getConfiguracaoAcademico().getMatricularApenasDisciplinaAulaProgramada(), getTransferenciaTurmaVO().getUltimaMatriculaPeriodoAtiva().getAno(), getTransferenciaTurmaVO().getUltimaMatriculaPeriodoAtiva().getSemestre(),   false, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema(), false);
		setListaSelectItemTurmaDestino(UtilSelectItem.getListaSelectItem(turmaVOs, "codigo", "identificadorTurma", true));
	}

	/**
	 * Método responsável por validar a ultima matrícula período do aluno para que somente seja possível realizar a transferência de turma de alunos
	 * cuja situação da matrícula período seja ATIVO ou PRÉ-MATRÍCULA.
	 * 
	 * @throws Exception
	 */
	public void verificarSituacaoUltimaMatriculaPeriodo() throws Exception {
		MatriculaPeriodoVO obj = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoAtivaPreMatriculaPorMatricula(getTransferenciaTurmaVO().getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
		obj.setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsinoCurso(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
		if (!obj.getSituacaoMatriculaPeriodo().equals("AT") && !obj.getSituacaoMatriculaPeriodo().equals("PR")) {
			getTransferenciaTurmaVO().setMatriculaVO(new MatriculaVO());
			throw new Exception("Situação Matrícula Período diferente de ATIVA ou PRÉ-MATRÍCULA.");
		}
		getTransferenciaTurmaVO().setUltimaMatriculaPeriodoAtiva(obj);
		getTransferenciaTurmaVO().setMatriculaVO(obj.getMatriculaVO());
		setTransferenciaTurmaVOs(null);
		montarListaSelectItemTurmaDestino();
		getTransferenciaTurmaVO().setTurmaOrigem(obj.getTurma());
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> objs = new ArrayList<SelectItem>();
		objs.add(new SelectItem("matricula", "Matrícula"));
		objs.add(new SelectItem("aluno", "Aluno"));
		objs.add(new SelectItem("disciplina", "Disciplina"));
		objs.add(new SelectItem("turmaOrigem", "Turma de Origem"));
		objs.add(new SelectItem("turmaDestino", "Turma de Destino"));
		return objs;
	}

	public void imprimirPDF2() {
		try {
			setImprimirContrato(getFacadeFactory().getTransferenciaTurmaFacade().imprimirDeclaracaoTransferenciaTurma(getTransferenciaTurmaVO(), getTextoPadraoDeclaracao(), getUsuarioLogado()));
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarListaSelectItemTipoTextoPadrao(Integer unidadeEnsino) {
		try {
			getListaSelectItemTipoTextoPadrao().clear();
			List<TextoPadraoDeclaracaoVO> textoPadraoDeclaracaoVOs = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("TT", unidadeEnsino, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			setListaSelectItemTipoTextoPadrao(UtilSelectItem.getListaSelectItem(textoPadraoDeclaracaoVOs, "codigo", "descricao"));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemTipoTextoPadrao() {
		if (listaSelectItemTipoTextoPadrao == null) {
			listaSelectItemTipoTextoPadrao = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoTextoPadrao;
	}

	public void setListaSelectItemTipoTextoPadrao(List<SelectItem> listaSelectItemTipoTextoPadrao) {
		this.listaSelectItemTipoTextoPadrao = listaSelectItemTipoTextoPadrao;
	}

	public Integer getTextoPadraoDeclaracao() {
		if (textoPadraoDeclaracao == null) {
			textoPadraoDeclaracao = 0;
		}
		return textoPadraoDeclaracao;
	}

	public void setTextoPadraoDeclaracao(Integer textoPadraoDeclaracao) {
		this.textoPadraoDeclaracao = textoPadraoDeclaracao;
	}

	public String getContrato() {
		if (getImprimirContrato()) {
			return "abrirPopup('../../VisualizarContrato', 'RelatorioContrato', 730, 545); RichFaces.$('panelTextoPadraoDeclaracao').hide()";
		}
		return "";
	}

	public Boolean getImprimirContrato() {
		if (imprimirContrato == null) {
			imprimirContrato = false;
		}
		return imprimirContrato;
	}

	public void setImprimirContrato(Boolean imprimirContrato) {
		this.imprimirContrato = imprimirContrato;
	}

	public TransferenciaTurmaVO getTransferenciaTurmaVO() {
		if (transferenciaTurmaVO == null) {
			transferenciaTurmaVO = new TransferenciaTurmaVO();
		}
		return transferenciaTurmaVO;
	}

	public void setTransferenciaTurmaVO(TransferenciaTurmaVO transferenciaTurmaVO) {
		this.transferenciaTurmaVO = transferenciaTurmaVO;
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

	public List<MatriculaVO> getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<MatriculaVO>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
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

	public List<SelectItem> getListaSelectItemTurmaDestino() {
		if (listaSelectItemTurmaDestino == null) {
			listaSelectItemTurmaDestino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTurmaDestino;
	}

	public void setListaSelectItemTurmaDestino(List<SelectItem> listaSelectItemTurmaDestino) {
		this.listaSelectItemTurmaDestino = listaSelectItemTurmaDestino;
	}

	public List<TransferenciaTurmaVO> getTransferenciaTurmaVOs() {
		if (transferenciaTurmaVOs == null) {
			transferenciaTurmaVOs = new ArrayList<TransferenciaTurmaVO>(0);
		}
		return transferenciaTurmaVOs;
	}

	public void setTransferenciaTurmaVOs(List<TransferenciaTurmaVO> transferenciaTurmaVOs) {
		this.transferenciaTurmaVOs = transferenciaTurmaVOs;
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

	public Boolean getRealizarMarcacaoDesmarcacaoTodosTransferenciaTurnoDisciplina() {
		if (realizarMarcacaoDesmarcacaoTodosTransferenciaTurnoDisciplina == null) {
			realizarMarcacaoDesmarcacaoTodosTransferenciaTurnoDisciplina = false;
		}
		return realizarMarcacaoDesmarcacaoTodosTransferenciaTurnoDisciplina;
	}

	public void setRealizarMarcacaoDesmarcacaoTodosTransferenciaTurnoDisciplina(Boolean realizarMarcacaoDesmarcacaoTodosTransferenciaTurnoDisciplina) {
		this.realizarMarcacaoDesmarcacaoTodosTransferenciaTurnoDisciplina = realizarMarcacaoDesmarcacaoTodosTransferenciaTurnoDisciplina;
	}

	public void executarMarcacaoDesmarcacaoTodosTransferenciaTurnoDisciplinaVOs() {
		for (TransferenciaTurmaVO obj : getTransferenciaTurmaVOs()) {
			if (obj.getExisteRegistroAula() || obj.isExisteRegistroAulaTurmaPratica() || obj.isExisteRegistroAulaTurmaTeorica()) {
				obj.setRealizarAbonoRegistroAula(getRealizarMarcacaoDesmarcacaoTodosTransferenciaTurnoDisciplina());
			}
		}
	}

	public void executarMontagemDisciplinas() {
		try {
			setTransferenciaTurmaVOs(getFacadeFactory().getTransferenciaTurmaFacade().executarMontagemDisciplinasParaTransferencia(getTransferenciaTurmaVO(), false, getUsuarioLogado(), !getPermitirIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getMensagemAlterarTurmaOrigem() {
		if (mensagemAlterarTurmaOrigem == null) {
			mensagemAlterarTurmaOrigem = "";
		}
		return mensagemAlterarTurmaOrigem;
	}

	public void setMensagemAlterarTurmaOrigem(String mensagemAlterarTurmaOrigem) {
		this.mensagemAlterarTurmaOrigem = mensagemAlterarTurmaOrigem;
	}

	public Boolean getAlterarTurmaOrigem() {
		if (alterarTurmaOrigem == null) {
			alterarTurmaOrigem = false;
		}
		return alterarTurmaOrigem;
	}

	public void setAlterarTurmaOrigem(Boolean alterarTurmaOrigem) {
		this.alterarTurmaOrigem = alterarTurmaOrigem;
	}

	public void executarVerificacaoApresentarMensagemAlterarTurmaOrigem() {
		try {
			getTransferenciaTurmaVO().setTurmaDestino(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTransferenciaTurmaVO().getTurmaDestino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			if (getTransferenciaTurmaVO().getTurmaOrigem().getPlanoFinanceiroCurso().getCodigo().equals(getTransferenciaTurmaVO().getTurmaDestino().getPlanoFinanceiroCurso().getCodigo())) {
				setMensagemAlterarTurmaOrigem(UteisJSF.internacionalizar("msg_TransferenciaTurma_planoFinanceiroTurmaIgual").replace("{0}", getTransferenciaTurmaVO().getTurmaOrigem().getIdentificadorTurma()).replace("{1}", getTransferenciaTurmaVO().getTurmaDestino().getIdentificadorTurma()));
			} else {
				setMensagemAlterarTurmaOrigem(UteisJSF.internacionalizar("msg_TransferenciaTurma_planoFinanceiroTurmaDiferente"));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public boolean getIsApresentarBotaoCancelarSemGravar() {
		return !getTransferenciaTurmaVO().getTurmaOrigem().getPlanoFinanceiroCurso().getCodigo().equals(getTransferenciaTurmaVO().getTurmaDestino().getPlanoFinanceiroCurso().getCodigo());
	}

	public void limparDadosTransferenciaTurmaVOs() {
		setTransferenciaTurmaVOs(null);
	}

	public void removerTransferenciaTurmaVOs() {
		try {
			TransferenciaTurmaVO objRemover = (TransferenciaTurmaVO) context().getExternalContext().getRequestMap().get("transferenciaTurmaVOItens");
			getFacadeFactory().getTransferenciaTurmaFacade().removerTransferenciaTurmaVOs(getTransferenciaTurmaVOs(), objRemover);
			setMensagemID("msg_dados_removidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void verificarPermissaoIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.TRANSFERENCIA_TURMA_PERMITIR_INCLUIR_DISCIPLINA_SEM_AULA_PROGRAMADA_EM_CURSOS_INTEGRAIS.getValor(), getUsuarioLogado());
			setPermitirIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais(true);
		} catch (Exception e) {
			setPermitirIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais(false);
		}
	}

	public Boolean getPermitirIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais() {
		if (permitirIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais == null) {
			permitirIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais = false;
		}
		return permitirIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais;
	}

	public void setPermitirIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais(Boolean permitirIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais) {
		this.permitirIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais = permitirIncluirDisciplinaSemAulaProgramadaEmCursosIntegrais;
	}
	
}
