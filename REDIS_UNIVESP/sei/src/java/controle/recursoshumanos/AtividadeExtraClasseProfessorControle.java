package controle.recursoshumanos;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.SerializationUtils;
import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.recursoshumanos.AtividadeExtraClasseProfessorCursoVO;
import negocio.comuns.recursoshumanos.AtividadeExtraClasseProfessorPostadoVO;
import negocio.comuns.recursoshumanos.AtividadeExtraClasseProfessorVO;
import negocio.comuns.recursoshumanos.HistoricoDependentesVO.EnumCampoConsultaHistoricoDependentes;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoHoraAtividadeExtraClasseEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

/**
 * Classe responsavel por implementar a interacao entre os componentes JSF das
 * paginas historicoFuncionarioForm.xhtml e historicoFuncionarioCons.xhtl com as
 * funcionalidades da classe <code>historicoFuncionario</code>. Implemtacao da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 */
@Controller("AtividadeExtraClasseProfessorControle")
@Scope("viewScope")
public class AtividadeExtraClasseProfessorControle extends SuperControle {

	private static final long serialVersionUID = -9145134888233724110L;

	private static final String TELA_CONS = "atividadeExtraClasseProfessorCons";
	private static final String TELA_FORM = "atividadeExtraClasseProfessorForm";
	private static final String CONTEXT_PARA_EDICAO = "itemAtividadeExtraClasseProfessor";

	private AtividadeExtraClasseProfessorVO atividadeExtraClasseProfessorSelecionadoVO;
	private AtividadeExtraClasseProfessorPostadoVO atividadeExtraClasseProfessorPostadoSelecionadoVO;
	private AtividadeExtraClasseProfessorCursoVO atividadeExtraClasseProfessorCursoSelecionadoVO;

	private AtividadeExtraClasseProfessorCursoVO atividadeExtraClasseProfessorCursoVO;
	
	private List<AtividadeExtraClasseProfessorVO> listaAtividadeExtraClasseProfessor;
	private List<AtividadeExtraClasseProfessorPostadoVO> listaAtividadeExtraClasseProfessorPostado;

	private SituacaoHoraAtividadeExtraClasseEnum situacaoHoraAtividadeExtraClasseEnum;

	private Date dataInicio;
	private Date dataFinal;
	private Date dataInicioPeriodo;
	private Date dataFinalPerido;
	private Integer horasPrevistas;

	private String situacaoFuncionario;
	private FuncionarioCargoVO funcionarioCargo;

	private DataModelo dataModeloCurso;
	private DataModelo dataModeloFuncionarioCargo;

	public AtividadeExtraClasseProfessorControle() {
		setControleConsultaOtimizado(new DataModelo());
		inicializarConsultar();
	}

	@Override
	public void consultarDados() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getFacadeFactory().getHistoricoFuncionarioInterfaceFacade().consultarPorEnumCampoConsultaSomenteProfessores(getControleConsultaOtimizado(), situacaoFuncionario, Boolean.TRUE);

			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Consulta responsavel por retornar os usuarios do popup de pesquisa 
	 * do funcionario
	 */
	public void consultarFuncionario() {
		try {
			getDataModeloFuncionarioCargo().setLimitePorPagina(10);
			getDataModeloFuncionarioCargo().preencherDadosParaConsulta(false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());

			if (getDataModeloFuncionarioCargo().getValorConsulta().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getDataModeloFuncionarioCargo().getCampoConsulta().equals("nome")) {
				getDataModeloFuncionarioCargo().setListaConsulta(getFacadeFactory().getFuncionarioCargoFacade().consultarPorNomeFuncionario(
						getDataModeloFuncionarioCargo(), getDataModeloFuncionarioCargo().getValorConsulta(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				getDataModeloFuncionarioCargo().setTotalRegistrosEncontrados(getFacadeFactory().getFuncionarioCargoFacade().consultarTotalPorNomeFuncionarioAtivo(getDataModeloFuncionarioCargo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}

			if (getDataModeloFuncionarioCargo().getCampoConsulta().equals("matricula")) {
				getDataModeloFuncionarioCargo().setListaConsulta(getFacadeFactory().getFuncionarioCargoFacade().consultarPorMatriculaCargo(
						getDataModeloFuncionarioCargo().getValorConsulta(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				getDataModeloFuncionarioCargo().setTotalRegistrosEncontrados(getDataModeloFuncionarioCargo().getListaConsulta().size());
			}

			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Consulta as {@link AtividadeExtraClasseProfessorVO} do {@link FuncionarioCargoVO}
	 * selecionado.
	 * 
	 */
	public void consultarHoraAtividadeExtraClassePorFuncionarioCargo() {
		try {
			setListaAtividadeExtraClasseProfessor(
					getFacadeFactory().getAtividadeExtraClasseProfessorInterfaceFacade().consultarAtividadeExtraClassePorFuncionarioCargo(
							getFuncionarioCargo().getCodigo(), getDataInicio(), getDataFinal()));

			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String editar() {
		FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
		setFuncionarioCargo(obj);
		consultarHoraAtividadeExtraClassePorFuncionarioCargo();

		setControleConsultaOtimizado(new DataModelo());
		setMensagemID(MSG_TELA.msg_dados_editar.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void excluir() {
		try {
			getFacadeFactory().getAtividadeExtraClasseProfessorInterfaceFacade().excluirTodos(getListaAtividadeExtraClasseProfessor(), false, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	@Override
	public boolean getApresentarResultadoConsulta() {
		if (Uteis.isAtributoPreenchido(getControleConsultaOtimizado().getListaConsulta())) {
			return !getControleConsultaOtimizado().getListaConsulta().isEmpty();
		}
		return false;
	}

	public boolean getApresentarPaginadorResultadoConsulta() {
		return getControleConsultaOtimizado().getTotalRegistrosEncontrados() > 10;
	}

	public String novo() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void persistir() {
		try {
			getFacadeFactory().getAtividadeExtraClasseProfessorInterfaceFacade().atualizarValorHoraPrevista(getListaAtividadeExtraClasseProfessor());
			getFacadeFactory().getAtividadeExtraClasseProfessorInterfaceFacade()
				.persistirTodos(getListaAtividadeExtraClasseProfessor(), getFuncionarioCargo(), false, getUsuarioLogado());

			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void persistirHorasPrevistaCurso() {
		try {
			//alteraHorasPrevistaCursoTodosMeses();
			persistir();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de
	 * uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setCampoConsulta(EnumCampoConsultaHistoricoDependentes.FUNCIONARIO.name());
		setListaConsulta(new ArrayList<>(0));
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}

	/**
	 * Evento de consulta paginada da tela pesquisa do Funcionario Cargo da tela de
	 * atividadeExtraClasseProfessorCons.xhtml
	 * 
	 * @param dataScrollerEvent
	 */
	public void scrollerListener(DataScrollEvent dataScrollerEvent) {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		this.consultarDados();
	}

	public void scrollerListenerCurso(DataScrollEvent dataScrollerEvent) {
		getDataModeloCurso().setPaginaAtual(dataScrollerEvent.getPage());
		getDataModeloCurso().setPage(dataScrollerEvent.getPage());
		this.consultarCurso();
	}

	/**
	 * Evento de consulta paginada da tela pesquisa do popup Funcionario Cargo.
	 * 
	 * @param dataScrollerEvent
	 */
	public void scrollerListenerFuncionario(DataScrollEvent dataScrollerEvent) {
		try {
			getDataModeloFuncionarioCargo().setPaginaAtual(dataScrollerEvent.getPage());
			getDataModeloFuncionarioCargo().setPage(dataScrollerEvent.getPage());
			consultarFuncionario();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarFuncionarioPelaMatriculaCargo() {
		try {
			setFuncionarioCargo(getFacadeFactory().getFuncionarioCargoFacade().consultarPorMatriculaCargoProfessor(getFuncionarioCargo().getMatriculaCargo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			if (Uteis.isAtributoPreenchido(getFuncionarioCargo())) {
				consultarHoraAtividadeExtraClassePorFuncionarioCargo();
				setMensagemID(MSG_TELA.msg_dados_consultados.name());
			} else {
				setListaAtividadeExtraClasseProfessor(new ArrayList<>(0));
				setMensagemDetalhada("Não existe funcionário com o CARGO DE PROFESSOR para a matrícula informada.");
			}
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	}

	public void adicionar() {
		try {
			setListaAtividadeExtraClasseProfessor(getFacadeFactory().getAtividadeExtraClasseProfessorInterfaceFacade().validarDadosPeriodo(
					getDataInicio(), getDataFinal(), getHorasPrevistas(), getListaAtividadeExtraClasseProfessor()));
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	}
	
	public void remover() {
		try {
			if (Uteis.isAtributoPreenchido(getAtividadeExtraClasseProfessorSelecionadoVO())) {
				getFacadeFactory().getAtividadeExtraClasseProfessorInterfaceFacade().excluir(getAtividadeExtraClasseProfessorSelecionadoVO(), false, getUsuarioLogado());
			}

			getListaAtividadeExtraClasseProfessor().removeIf(p -> p.getData().equals(getAtividadeExtraClasseProfessorSelecionadoVO().getData()));
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	}

	public void remover(AtividadeExtraClasseProfessorVO obj) {
		try {
			getListaAtividadeExtraClasseProfessor().removeIf(p -> p.getData().equals(obj.getData()));
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void adicionarAtividadeExtraClasseCurso() {
		try {
			getFacadeFactory().getAtividadeExtraClasseProfessorCursoInterfaceFacade().adicionarAtividadeExtraClasseCurso(
					getListaAtividadeExtraClasseProfessor(), getAtividadeExtraClasseProfessorCursoVO(),
					getAtividadeExtraClasseProfessorSelecionadoVO());

			setAtividadeExtraClasseProfessorCursoVO(new AtividadeExtraClasseProfessorCursoVO());
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void adicionarAtividadeExtraClasseCursoReplicarTodosMeses() {
		try {
			getFacadeFactory().getAtividadeExtraClasseProfessorCursoInterfaceFacade().adicionarAtividadeExtraClasseCursoReplicarTodosMeses(
					getListaAtividadeExtraClasseProfessor(), getAtividadeExtraClasseProfessorCursoVO(), getAtividadeExtraClasseProfessorSelecionadoVO());

			setAtividadeExtraClasseProfessorCursoVO(new AtividadeExtraClasseProfessorCursoVO());
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void atualizarValorListaAtividadeExtraClasseCurso() {
		for (AtividadeExtraClasseProfessorVO atividadeExtraClasseProfessorVO : getListaAtividadeExtraClasseProfessor()) {
			if (atividadeExtraClasseProfessorVO.getCodigo().equals(getAtividadeExtraClasseProfessorSelecionadoVO().getCodigo())) {					
				atividadeExtraClasseProfessorVO.setAtividadeExtraClasseProfessorCursoVOs(getAtividadeExtraClasseProfessorSelecionadoVO().getAtividadeExtraClasseProfessorCursoVOs());
			}
		}
	}

	public void selecionarAtividadeExtraClasseProfessor() {
		try {
			AtividadeExtraClasseProfessorVO obj = (AtividadeExtraClasseProfessorVO) context().getExternalContext().getRequestMap().get("horaAtividadeExtraClasse");
			setAtividadeExtraClasseProfessorSelecionadoVO(obj);
			if (Uteis.isAtributoPreenchido(obj)) {
				getFacadeFactory().getAtividadeExtraClasseProfessorCursoInterfaceFacade().consultarPorAtividadeExtraClasse(obj, getUsuarioLogado());
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void selecionarAtividadeExtraClasseProfessorPostado() {
		AtividadeExtraClasseProfessorPostadoVO obj = (AtividadeExtraClasseProfessorPostadoVO) context().getExternalContext().getRequestMap().get("itemAtividadeExtraClasseProfessor");
		setAtividadeExtraClasseProfessorPostadoSelecionadoVO(obj);
		setMensagemID(MSG_TELA.msg_dados_consultados.name());
	}

	public void selecionarAtividadeExtraClasseProfessorCurso() {
		AtividadeExtraClasseProfessorCursoVO obj = (AtividadeExtraClasseProfessorCursoVO) context().getExternalContext().getRequestMap().get("itemAtividadeExtraClasseCurso");

		setAtividadeExtraClasseProfessorCursoSelecionadoVO(obj);
		setMensagemID(MSG_TELA.msg_dados_consultados.name());
	}

	public void removerAtividadeExtraClasseCurso() {
		try {
			getAtividadeExtraClasseProfessorSelecionadoVO().getAtividadeExtraClasseProfessorCursoVOs().removeIf(p -> 
				p.getCursoVO().getCodigo().equals(getAtividadeExtraClasseProfessorCursoSelecionadoVO().getCursoVO().getCodigo()) &&
				( UteisData.getMesData(p.getAtividadeExtraClasseProfessorVO().getData()) == UteisData.getMesData(getAtividadeExtraClasseProfessorCursoSelecionadoVO().getAtividadeExtraClasseProfessorVO().getData()) &&
				  UteisData.getAnoData(p.getAtividadeExtraClasseProfessorVO().getData()) == UteisData.getAnoData(getAtividadeExtraClasseProfessorCursoSelecionadoVO().getAtividadeExtraClasseProfessorVO().getData())) );
			
			atualizarValorListaAtividadeExtraClasseCurso();
			persistir();
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void alteraHorasPrevistaCurso() {
		try {
			AtividadeExtraClasseProfessorCursoVO obj = SerializationUtils.clone(getAtividadeExtraClasseProfessorCursoSelecionadoVO());
			getFacadeFactory().getAtividadeExtraClasseProfessorCursoInterfaceFacade().alteraHorasPrevistaCurso(getListaAtividadeExtraClasseProfessor(),
					obj);
			
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void alteraHorasPrevistaCursoTodosMeses() {
		try {
			getFacadeFactory().getAtividadeExtraClasseProfessorCursoInterfaceFacade().alteraHorasPrevistaCursoTodosMeses(
					getListaAtividadeExtraClasseProfessor(), getAtividadeExtraClasseProfessorCursoSelecionadoVO(), getFuncionarioCargo(), getUsuarioLogado() );
			
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Remove todos as {@link AtividadeExtraClasseProfessorCursoVO} do mesmo selecionado.
	 */
	public void removerTodosMesesAtividadeExtraClasseCurso() {
		try {

			getFacadeFactory().getAtividadeExtraClasseProfessorCursoInterfaceFacade().removerTodosMesesAtividadeExtraClasseCurso(
					getAtividadeExtraClasseProfessorCursoSelecionadoVO(), getAtividadeExtraClasseProfessorSelecionadoVO().getAtividadeExtraClasseProfessorCursoVOs(), getListaAtividadeExtraClasseProfessor());
			persistir();
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparDadosFuncionario () {
		setFuncionarioCargo(new FuncionarioCargoVO());
		getDataModeloFuncionarioCargo().setValorConsulta("");
		
		setListaAtividadeExtraClasseProfessor(new ArrayList<>(0));
	}
	
	/**
	 * Limpa os campos do curso e a lista do popup de consulta. 
	 */
	public void limparDadosCurso() {
		getAtividadeExtraClasseProfessorCursoVO().setCursoVO(new CursoVO());
		setDataModeloCurso(new DataModelo());
		getDataModeloCurso().setLimitePorPagina(10);
		getDataModeloCurso().setPage(1);
		getDataModeloCurso().setPaginaAtual(1);
		limparMensagem();
	}

	public void limparDadosAtividadeExtraClasseCurso() {
		setAtividadeExtraClasseProfessorCursoVO(new AtividadeExtraClasseProfessorCursoVO());
		getAtividadeExtraClasseProfessorSelecionadoVO().setAtividadeExtraClasseProfessorCursoVOs(new ArrayList<>(0));
	}
	

	/**
	 * Seleciona o funcionario cargo pesquisado.
	 */
	public void selecionarFuncionarioCargo() {
		FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap().get("funcionarioCargoItem");
		setFuncionarioCargo(obj);

		consultarHoraAtividadeExtraClassePorFuncionarioCargo();
		setDataModeloFuncionarioCargo(new DataModelo());
	}
	
	public void selecionarCurso() {
		CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItem");
		
		boolean cursoAdicionado = getAtividadeExtraClasseProfessorSelecionadoVO().getAtividadeExtraClasseProfessorCursoVOs().stream().anyMatch(p -> p.getCursoVO().getCodigo().equals(obj.getCodigo()));
		if (cursoAdicionado) {
			setMensagemDetalhada("O curso " + obj.getNome() + " já foi adicionado.");
		} else {
			getAtividadeExtraClasseProfessorCursoVO().setCursoVO(obj);
		}
	}

	/**
	 * Consulta os cursos para modal de pesquisa utilizando limite de 10 registros.
	 */
	public void consultarCurso() {
		try {
			getFacadeFactory().getCursoFacade().consultarCursoDataModelo(getDataModeloCurso(), getUsuarioLogado(), null);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarAtivadeExtraClassePorSituacaoData(SituacaoHoraAtividadeExtraClasseEnum situacao, Date dataAtividade) {
		this.consultarAtivadeExtraClassePorSituacaoData(situacao, dataAtividade, null);
	}

	public void consultarAtivadeExtraClassePorSituacaoData(SituacaoHoraAtividadeExtraClasseEnum situacao, Date dataAtividade, CursoVO cursoVO) {
		setSituacaoHoraAtividadeExtraClasseEnum(situacao);
		try {
			setListaAtividadeExtraClasseProfessorPostado(getFacadeFactory().getAtividadeExtraClasseProfessorPostadoInterfaceFacade().
					consultarAtivadeExtraClassePorSituacaoData(situacao, dataAtividade, getFuncionarioCargo().getCodigo(), cursoVO));
			
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarAtivadeExtraClasseCursoPorAtividade() {
		try {
			limparDadosAtividadeExtraClasseCurso();
			AtividadeExtraClasseProfessorVO obj = (AtividadeExtraClasseProfessorVO) context().getExternalContext().getRequestMap().get("horaAtividadeExtraClasse");
			setAtividadeExtraClasseProfessorSelecionadoVO(obj);

			obj.setAtividadeExtraClasseProfessorCursoVOs(getFacadeFactory().getAtividadeExtraClasseProfessorCursoInterfaceFacade()
					.consultarPorAtividadeExtraClasse(obj, getUsuarioLogado()));

			for (AtividadeExtraClasseProfessorCursoVO atividadeExtraClasseProfessorCurso : getAtividadeExtraClasseProfessorSelecionadoVO().getAtividadeExtraClasseProfessorCursoVOs()) {
				atividadeExtraClasseProfessorCurso.setTotalHorasAguardandoAprovacao(getFacadeFactory().getAtividadeExtraClasseProfessorPostadoInterfaceFacade().consultarTotalAtivadeExtraClassePorSituacaoData(
						SituacaoHoraAtividadeExtraClasseEnum.AGUARDANDO_APROVACAO, obj.getData(), getFuncionarioCargo().getCodigo(), atividadeExtraClasseProfessorCurso.getCursoVO()));

				atividadeExtraClasseProfessorCurso.setTotalHorasAprovadas(getFacadeFactory().getAtividadeExtraClasseProfessorPostadoInterfaceFacade().consultarTotalAtivadeExtraClassePorSituacaoData(
						SituacaoHoraAtividadeExtraClasseEnum.APROVADO, obj.getData(), getFuncionarioCargo().getCodigo(), atividadeExtraClasseProfessorCurso.getCursoVO()));

				atividadeExtraClasseProfessorCurso.setTotalHorasIndeferidas(getFacadeFactory().getAtividadeExtraClasseProfessorPostadoInterfaceFacade().consultarTotalAtivadeExtraClassePorSituacaoData(
						SituacaoHoraAtividadeExtraClasseEnum.INDEFERIDO, obj.getData(), getFuncionarioCargo().getCodigo(), atividadeExtraClasseProfessorCurso.getCursoVO()));
			}

			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void chamarRelatorioSEIDecidir() {
		context().getExternalContext().getSessionMap().put("modulo", "RECURSOS_HUMANOS");
	}

	public boolean validarAprovado(SituacaoHoraAtividadeExtraClasseEnum situacaoHoraAtividadeExtraClasseEnum) {
		return situacaoHoraAtividadeExtraClasseEnum != null ? situacaoHoraAtividadeExtraClasseEnum.equals(SituacaoHoraAtividadeExtraClasseEnum.APROVADO) : Boolean.FALSE;
	}

	public boolean validarIndeferido(SituacaoHoraAtividadeExtraClasseEnum situacaoHoraAtividadeExtraClasseEnum) {
		return situacaoHoraAtividadeExtraClasseEnum != null ? situacaoHoraAtividadeExtraClasseEnum.equals(SituacaoHoraAtividadeExtraClasseEnum.INDEFERIDO) : Boolean.FALSE;
	}

	public boolean validarAguardandoAprovacao(SituacaoHoraAtividadeExtraClasseEnum situacaoHoraAtividadeExtraClasseEnum) {
		return situacaoHoraAtividadeExtraClasseEnum != null ? situacaoHoraAtividadeExtraClasseEnum.equals(SituacaoHoraAtividadeExtraClasseEnum.AGUARDANDO_APROVACAO) : Boolean.FALSE;
	}

	public boolean permiteAlterarDataLimiteRegistro() {
    	try {
    		return ControleAcesso.verificarPermissaoFuncionalidadeUsuario("PermiteAlterarDataLimiteRegistro", getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return false;
		}
    }

	public boolean permiteAlterarDataLimiteAprovacao() {
		try {
			return ControleAcesso.verificarPermissaoFuncionalidadeUsuario("PermiteAlterarDataLimiteAprovacao", getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return false;
		}
	}

	public int getTotalHorasPrevistas() {
		return listaAtividadeExtraClasseProfessor.stream().mapToInt(AtividadeExtraClasseProfessorVO::getHoraPrevista).sum();
	}

	public int getTotalHorasAguardandoAprovacao() {
		return listaAtividadeExtraClasseProfessor.stream().mapToInt(AtividadeExtraClasseProfessorVO::getTotalHorasAguardandoAprovacao).sum();
	}

	public int getTotalHorasAprovadas() {
		return listaAtividadeExtraClasseProfessor.stream().mapToInt(AtividadeExtraClasseProfessorVO::getTotalHorasAprovadas).sum();
	}

	public int getTotalHorasIndeferidas() {
		return listaAtividadeExtraClasseProfessor.stream().mapToInt(AtividadeExtraClasseProfessorVO::getTotalHorasIndeferidas).sum();
	}

	public String selecionarArquivoDocumentoParaDownload() {
		try {
			AtividadeExtraClasseProfessorPostadoVO obj = (AtividadeExtraClasseProfessorPostadoVO) context().getExternalContext().getRequestMap().get("itemAtividadeExtraClasseProfessor");
			ArquivoVO arquivoVO = obj.getArquivo();
			arquivoVO.setPastaBaseArquivo(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator +arquivoVO.getPastaBaseArquivo());
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("arquivoVO", arquivoVO);
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}
	
	public boolean getApresentarVisaoCoordenador() {
		return getUsuarioLogado().getIsApresentarVisaoCoordenador();
	}

	// GETTER AND SETTER
	public FuncionarioCargoVO getFuncionarioCargo() {
		if (funcionarioCargo == null) {
			funcionarioCargo = new FuncionarioCargoVO();
		}
		return funcionarioCargo;
	}

	public void setFuncionarioCargo(FuncionarioCargoVO funcionarioCargo) {
		this.funcionarioCargo = funcionarioCargo;
	}

	public String getSituacaoFuncionario() {
		if (situacaoFuncionario == null) {
			situacaoFuncionario = "ATIVO";
		}
		return situacaoFuncionario;
	}

	public void setSituacaoFuncionario(String situacaoFuncionario) {
		this.situacaoFuncionario = situacaoFuncionario;
	}

	public List<AtividadeExtraClasseProfessorVO> getListaAtividadeExtraClasseProfessor() {
		if (listaAtividadeExtraClasseProfessor == null) {
			listaAtividadeExtraClasseProfessor = new ArrayList<>(0);
		}
		return listaAtividadeExtraClasseProfessor;
	}

	public void setListaAtividadeExtraClasseProfessor(
			List<AtividadeExtraClasseProfessorVO> listaAtividadeExtraClasseProfessor) {
		this.listaAtividadeExtraClasseProfessor = listaAtividadeExtraClasseProfessor;
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = Uteis.getDataInicioSemestreAtual();
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFinal() {
		if (dataFinal == null) {
			dataFinal = Uteis.getDataFimSemestreAtual();
		}
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Integer getHorasPrevistas() {
		if (horasPrevistas == null) {
			horasPrevistas = 0;
		}
		return horasPrevistas;
	}

	public void setHorasPrevistas(Integer horasPrevistas) {
		this.horasPrevistas = horasPrevistas;
	}
	
	public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}

	public DataModelo getDataModeloFuncionarioCargo() {
		if (dataModeloFuncionarioCargo == null) {
			dataModeloFuncionarioCargo = new DataModelo();
		}
		return dataModeloFuncionarioCargo;
	}

	public void setDataModeloFuncionarioCargo(DataModelo dataModeloFuncionarioCargo) {
		this.dataModeloFuncionarioCargo = dataModeloFuncionarioCargo;
	}

	public List<AtividadeExtraClasseProfessorPostadoVO> getListaAtividadeExtraClasseProfessorPostado() {
		if (listaAtividadeExtraClasseProfessorPostado == null) {
			listaAtividadeExtraClasseProfessorPostado = new ArrayList<>(0);
		}
		return listaAtividadeExtraClasseProfessorPostado;
	}

	public void setListaAtividadeExtraClasseProfessorPostado(
			List<AtividadeExtraClasseProfessorPostadoVO> listaAtividadeExtraClasseProfessorPostado) {
		this.listaAtividadeExtraClasseProfessorPostado = listaAtividadeExtraClasseProfessorPostado;
	}

	public SituacaoHoraAtividadeExtraClasseEnum getSituacaoHoraAtividadeExtraClasseEnum() {
		if (situacaoHoraAtividadeExtraClasseEnum == null) {
			situacaoHoraAtividadeExtraClasseEnum = SituacaoHoraAtividadeExtraClasseEnum.AGUARDANDO_APROVACAO;
		}
		return situacaoHoraAtividadeExtraClasseEnum;
	}

	public void setSituacaoHoraAtividadeExtraClasseEnum(
			SituacaoHoraAtividadeExtraClasseEnum situacaoHoraAtividadeExtraClasseEnum) {
		this.situacaoHoraAtividadeExtraClasseEnum = situacaoHoraAtividadeExtraClasseEnum;
	}

	public Date getDataInicioPeriodo() {
		if (dataInicioPeriodo == null) {
			dataInicioPeriodo = UteisData.primeiroDiaAnoAtual();
		}
		return dataInicioPeriodo;
	}

	public void setDataInicioPeriodo(Date dataInicioPeriodo) {
		this.dataInicioPeriodo = dataInicioPeriodo;
	}

	public Date getDataFinalPerido() {
		if (dataFinalPerido == null) {
			dataFinalPerido = UteisData.ultimoDiaAnoAtual();
		}
		return dataFinalPerido;
	}

	public void setDataFinalPerido(Date dataFinalPerido) {
		this.dataFinalPerido = dataFinalPerido;
	}

	public DataModelo getDataModeloCurso() {
		if (dataModeloCurso == null) {
			dataModeloCurso = new DataModelo();
			dataModeloCurso.setLimitePorPagina(10);
			dataModeloCurso.setPage(1);
			dataModeloCurso.setPaginaAtual(1);
		}
		return dataModeloCurso;
	}

	public void setDataModeloCurso(DataModelo dataModeloCurso) {
		this.dataModeloCurso = dataModeloCurso;
	}

	public AtividadeExtraClasseProfessorCursoVO getAtividadeExtraClasseProfessorCursoVO() {
		if (atividadeExtraClasseProfessorCursoVO == null) {
			atividadeExtraClasseProfessorCursoVO = new AtividadeExtraClasseProfessorCursoVO();
		}
		return atividadeExtraClasseProfessorCursoVO;
	}

	public void setAtividadeExtraClasseProfessorCursoVO(
			AtividadeExtraClasseProfessorCursoVO atividadeExtraClasseProfessorCursoVO) {
		this.atividadeExtraClasseProfessorCursoVO = atividadeExtraClasseProfessorCursoVO;
	}

	public AtividadeExtraClasseProfessorVO getAtividadeExtraClasseProfessorSelecionadoVO() {
		if (atividadeExtraClasseProfessorSelecionadoVO == null) {
			atividadeExtraClasseProfessorSelecionadoVO = new AtividadeExtraClasseProfessorVO();
		}
		return atividadeExtraClasseProfessorSelecionadoVO;
	}

	public void setAtividadeExtraClasseProfessorSelecionadoVO(
			AtividadeExtraClasseProfessorVO atividadeExtraClasseProfessorSelecionadoVO) {
		this.atividadeExtraClasseProfessorSelecionadoVO = atividadeExtraClasseProfessorSelecionadoVO;
	}

	public AtividadeExtraClasseProfessorCursoVO getAtividadeExtraClasseProfessorCursoSelecionadoVO() {
		if (atividadeExtraClasseProfessorCursoSelecionadoVO == null) {
			atividadeExtraClasseProfessorCursoSelecionadoVO = new AtividadeExtraClasseProfessorCursoVO();
		}
		return atividadeExtraClasseProfessorCursoSelecionadoVO;
	}

	public void setAtividadeExtraClasseProfessorCursoSelecionadoVO(
			AtividadeExtraClasseProfessorCursoVO atividadeExtraClasseProfessorCursoSelecionadoVO) {
		this.atividadeExtraClasseProfessorCursoSelecionadoVO = atividadeExtraClasseProfessorCursoSelecionadoVO;
	}

	public AtividadeExtraClasseProfessorPostadoVO getAtividadeExtraClasseProfessorPostadoSelecionadoVO() {
		if (atividadeExtraClasseProfessorPostadoSelecionadoVO == null) {
			atividadeExtraClasseProfessorPostadoSelecionadoVO = new AtividadeExtraClasseProfessorPostadoVO();
		}
		return atividadeExtraClasseProfessorPostadoSelecionadoVO;
	}

	public void setAtividadeExtraClasseProfessorPostadoSelecionadoVO(
			AtividadeExtraClasseProfessorPostadoVO atividadeExtraClasseProfessorPostadoSelecionadoVO) {
		this.atividadeExtraClasseProfessorPostadoSelecionadoVO = atividadeExtraClasseProfessorPostadoSelecionadoVO;
	}
}
