package controle.academico;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import controle.arquitetura.*;
import controle.arquitetura.SuperControle.MSG_TELA;
import negocio.comuns.estagio.GrupoPessoaVO;
import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.DropEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.ConteudoPlanejamentoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaCompostaVO;
import negocio.comuns.academico.DisciplinaEquivalenteVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PerguntaRespostaOrigemVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.PlanoEnsinoHorarioAulaVO;
import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.academico.ReferenciaBibliograficaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.ClassificacaoDisciplinaEnum;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.academico.enumeradores.SituacaoPlanoEnsinoEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.AutorVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.blackboard.FonteDeDadosBlackboardVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.academico.Disciplina;
import negocio.facade.jdbc.academico.Turno;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.PlanoDisciplinaRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.PlanoDisciplinaRel;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas disciplinaForm.jsp disciplinaCons.jsp) com as funcionalidades da
 * classe <code>Disciplina</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see Disciplina
 * @see DisciplinaVO
 */
@Controller("DisciplinaControle")
@Scope("viewScope")
public class DisciplinaControle extends QuestionarioRespostaControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private DisciplinaVO disciplinaVO;
	private DisciplinaEquivalenteVO disciplinaEquivalenteVO;
	private DisciplinaCompostaVO disciplinaCompostaVO;
	private String equivalente_Erro;
	private ReferenciaBibliograficaVO referenciaBibliograficaVO;
	private String publicacaoBiblioteca_Erro;
	private ConteudoPlanejamentoVO conteudoPlanejamentoVO;
	private String valorConsultaReferencia;
	private String campoConsultaReferencia;
	private List<CatalogoVO> listaConsultaReferencia;
	private List<SelectItem> listaSelectItemAreaConhecimento;
	private String matricula;
	private Integer periodoLetivo;
	private Integer disciplina;
	private List<SelectItem> listaSelectItemPeriodosLetivos;
	private List<SelectItem> listaSelectDisciplina;
	private List<SelectItem> listaSelectItemMatriculaCursoTurno;
	private List<SelectItem> listaSelectItemConfiguracaoAcademico;
	private Boolean utilizarConfiguracaoAcademicoEspecifica;
	private Integer codigoReferencia;
	private Integer disciplinaDefasada;
	private Integer disciplinaCorreta;
	private String autores;
	private PlanoEnsinoVO planoEnsinoVO;
	private List<PlanoEnsinoVO> planoEnsinoVOs;
	private List<PlanoEnsinoVO> planoEnsinoAnteriorVOs;
	private String tabActive;
	private Boolean permitirAlterarUltimoPlanoEnsino;
	public List<SelectItem> tipoConsultaComboCurso;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<CursoVO> listaConsultaCurso;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private Boolean disciplinaEditar;
	private Boolean disciplinaConsultar;
	private List<SelectItem> tipoConsultaComboCatalogo;
	private Boolean permitirVisualizarBotoes;	
	private String ano;
	private String semestre;
	private HistoricoVO historicoVO;
	private SituacaoPlanoEnsinoEnum situacaoPlanoEnsino;
	private List<SelectItem> listaSelectItemSituacaoPlanoEnsinoCons;
	private String valorConsultaProfessor;
	private List<FuncionarioVO> listaConsultaProfessor;
	private String campoConsultaProfessor;
	private TurmaVO turmaVO;
	private CursoVO cursoVO;
	private List<SelectItem> listaSelectItemTurno;
	private List<SelectItem> listaSelectItemTurma;
	private List<SelectItem> listaSelectItemPeriodoLetivo;
	private List<SelectItem> listaSelectItemCurso;
	
	private PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO;
	private List<SelectItem> listaSelectItemDiaSemana;
	
	private Boolean habilitarAbas;
	private Boolean abrirModalRevisao;
	private List<String> listaMensagemAlertaVinculosDisciplinaAlteracaoNome;
	private String abrirModalAlertaVinculosDisciplinaAlteracaoNome;
	private List<SelectItem> listaSelectItemClassificacaoDisciplina;

	private List<SelectItem> listaSelectItemGrupoPessoa;
	private List<SelectItem> listaSelectFonteDeDadosBlackboard;
	private List<SelectItem> listaSelectConteudoMasterBlackboard;
	private List<SelectItem> listaSelectItemPeriodicidade;
	private Boolean gerarPlanoEnsinoAssinado;
	
	public DisciplinaControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		setListaSelectItemPeriodosLetivos(new ArrayList<SelectItem>(0));
		setListaSelectItemMatriculaCursoTurno(new ArrayList<SelectItem>(0));
		setPeriodoLetivo(0);
		setDisciplina(0);
		setListaSelectDisciplina(new ArrayList<SelectItem>(0));
		setMatricula(Constantes.EMPTY);
		getControleConsulta().setCampoConsulta("nome");
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name(), Uteis.ALERTA);
		montarListaSelectItemTurno();
		montarListaSelectItemPeriodicidade();
		
	}
	
	/**
	 * Monta os dados do {@link SelectItem} do {@link Turno}.
	 */
	@SuppressWarnings("unchecked")
	public void montarListaSelectItemTurno() {
		try {
			getListaSelectItemTurno().clear();
			List<TurnoVO> turnos = getFacadeFactory().getTurnoFacade().consultarPorUnidadeEnsinoCurso(
					getPlanoEnsinoVO().getUnidadeEnsino().getCodigo(), getPlanoEnsinoVO().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getListaSelectItemTurno().add(new SelectItem(Constantes.EMPTY,Constantes.EMPTY));
			for (TurnoVO turnoVO : turnos) {
				SelectItem selectItem = new SelectItem();
				selectItem.setValue(turnoVO.getCodigo());
				selectItem.setLabel(turnoVO.getNome());
				getListaSelectItemTurno().add(selectItem);
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Disciplina</code> para edição pelo usuário da aplicação.
	 */
	public String novo() throws Exception {
		removerObjetoMemoria(this);
		setDisciplinaVO(new DisciplinaVO());
		// utilizarConfiguracaoAcademicoEspecifica =
		// this.gradeDisciplinaVO.getUtilizarConfiguracaoAcademicoEspecifica();
		setReferenciaBibliograficaVO(new ReferenciaBibliograficaVO());
		setConteudoPlanejamentoVO(new ConteudoPlanejamentoVO());
		setDisciplinaEquivalenteVO(new DisciplinaEquivalenteVO());
		inicializarListasSelectItemTodosComboBox();
		setHabilitarAbas(!getHabilitarControlePorCalendarioLancamentoPlanoEnsino());
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao("disciplinaForm");
	}

	public void clonar() throws Exception {
		// setDisciplinaVO(new DisciplinaVO());
		getDisciplinaVO().setNovoObj(true);
		getDisciplinaVO().setCodigo(0);
		// utilizarConfiguracaoAcademicoEspecifica =
		// this.gradeDisciplinaVO.getUtilizarConfiguracaoAcademicoEspecifica();
		// setReferenciaBibliograficaVO(new ReferenciaBibliograficaVO());
		// setConteudoPlanejamentoVO(new ConteudoPlanejamentoVO());
		// setDisciplinaEquivalenteVO(new DisciplinaEquivalenteVO());
		inicializarListasSelectItemTodosComboBox();
		setMensagemID(MSG_TELA.msg_entre_dados.name());
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>Disciplina</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItem");
		try {
		obj = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimariaUnica(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		obj.setNovoObj(Boolean.FALSE);
		listaConsulta.clear();
		obj = inicializaConteudoPlanejamento(obj);
		setDisciplinaVO(obj);
		inicializarListasSelectItemTodosComboBox();
		setReferenciaBibliograficaVO(new ReferenciaBibliograficaVO());
		setConteudoPlanejamentoVO(new ConteudoPlanejamentoVO());
		setDisciplinaEquivalenteVO(new DisciplinaEquivalenteVO());
		VisaoProfessorControle visaoProfessor = (VisaoProfessorControle) context().getExternalContext().getSessionMap().get("VisaoProfessorControle");
		if (visaoProfessor != null) {
			visaoProfessor.inicializarDisciplinaEditar();
			}
			VisaoCoordenadorControle visaoCoordenador = (VisaoCoordenadorControle) context().getExternalContext().getSessionMap().get("VisaoCoordenadorControle");
			if (visaoCoordenador != null) {
				visaoCoordenador.inicializarDisciplinaEditar();
			}
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("disciplinaForm");
	}

	public void editarProfessorCoordenador() {
		try {
			PlanoEnsinoVO obj = (PlanoEnsinoVO) context().getExternalContext().getRequestMap().get("planoEnsinoItens");
			if (Uteis.isAtributoPreenchido(obj) || getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				setHabilitarAbas(Boolean.TRUE);
			} else {
				setHabilitarAbas(Boolean.FALSE);
			}
			setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimariaUnica(obj.getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			getDisciplinaVO().setDisciplinaEquivalenteVOs(getFacadeFactory().getDisciplinaEquivalenteFacade().consultarDisciplinaEquivalentes(obj.getDisciplina().getCodigo(), false, getUsuarioLogado()));
			
			if(obj.getCodigo() > 0){
				
				setPlanoEnsinoVO(getFacadeFactory().getPlanoEnsinoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
				
				Integer codigoQuestionario = 0;
				if(Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getCurso())){
					//getPlanoEnsinoVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getPlanoEnsinoVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
					codigoQuestionario = getPlanoEnsinoVO().getCurso().getQuestionarioVO().getCodigo();
				}else {
					codigoQuestionario = getConfiguracaoGeralPadraoSistema().getQuestionarioPlanoEnsino().getCodigo();
				}
				
				getPlanoEnsinoVO().setQuestionarioRespostaOrigemVO(getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().consultarPorQuestionarioPlanoEnsino(codigoQuestionario, getPlanoEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
				if(Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getQuestionarioRespostaOrigemVO())) {
					getPlanoEnsinoVO().getQuestionarioRespostaOrigemVO().setPerguntaRespostaOrigemVOs(getFacadeFactory().getPerguntaRespostaOrigemInterfaceFacade().consultarPorQuestionarioPlanoEnsino(codigoQuestionario, getPlanoEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
					 
					for (PerguntaRespostaOrigemVO perguntaRespostaOrigemPrincipalVO : getPlanoEnsinoVO().getQuestionarioRespostaOrigemVO().getPerguntaRespostaOrigemVOs()) {
						getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().editarListaPerguntaItemRespostaOrigemAdicionadasVO(perguntaRespostaOrigemPrincipalVO, perguntaRespostaOrigemPrincipalVO.getPerguntaItemRespostaOrigemVOs(), getUsuarioLogado());
					}		
				}
			}else{
				novoPlanoEnsino();
				//getPlanoEnsinoVO().getUnidadeEnsino().setCodigo(obj.getUnidadeEnsino().getCodigo());
				//getPlanoEnsinoVO().getUnidadeEnsino().setNome(obj.getUnidadeEnsino().getNome());

				QuestionarioVO questionarioVO = new QuestionarioVO();
				
				if(Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getCurso())){
					//getPlanoEnsinoVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado()));
					questionarioVO = getPlanoEnsinoVO().getCurso().getQuestionarioVO();
				}else {
					questionarioVO = getConfiguracaoGeralPadraoSistema().getQuestionarioPlanoEnsino();
				}
				
				getFacadeFactory().getPlanoEnsinoFacade().realizarCriacaoQuestionarioRespostaOrigem(getPlanoEnsinoVO(), questionarioVO, getUsuarioLogado());	
			}
			inicializarDisciplinaEditar();
			setPermitirAlterarUltimoPlanoEnsino(Boolean.FALSE);
			verificarPermissaoAlteracaoPlanoEnsino();
			montarListaSelectItemTurno();
			getFacadeFactory().getPlanoEnsinoFacade().atualizarTotalCargaHoraria(getPlanoEnsinoVO());
			setTabActive("planoDisciplina");
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarPlanoAnterior() {
		try {
			PlanoEnsinoVO obj = (PlanoEnsinoVO) context().getExternalContext().getRequestMap().get("planoEnsinoVOs");
			setPlanoEnsinoVO(getFacadeFactory().getPlanoEnsinoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
			setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimariaUnica(obj.getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			getDisciplinaVO().setDisciplinaEquivalenteVOs(getFacadeFactory().getDisciplinaEquivalenteFacade().consultarDisciplinaEquivalentes(obj.getDisciplina().getCodigo(), false, getUsuarioLogado()));					
			setPermitirAlterarUltimoPlanoEnsino(Boolean.FALSE);
			setPermitirVisualizarBotoes(Boolean.FALSE);
			inicializarDisciplinaEditar();
			verificarPermissaoAlteracaoPlanoEnsino();			
			setTabActive("planoDisciplina");
			getPlanoEnsinoVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getPlanoEnsinoVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
			getPlanoEnsinoVO().setQuestionarioRespostaOrigemVO(getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().consultarPorQuestionarioPlanoEnsino(getPlanoEnsinoVO().getCurso().getQuestionarioVO().getCodigo(), getPlanoEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			if(Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getQuestionarioRespostaOrigemVO())) {
				getPlanoEnsinoVO().getQuestionarioRespostaOrigemVO().setPerguntaRespostaOrigemVOs(getFacadeFactory().getPerguntaRespostaOrigemInterfaceFacade().consultarPorQuestionarioPlanoEnsino(getPlanoEnsinoVO().getCurso().getQuestionarioVO().getCodigo(), getPlanoEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
				 
				for (PerguntaRespostaOrigemVO perguntaRespostaOrigemPrincipalVO : getPlanoEnsinoVO().getQuestionarioRespostaOrigemVO().getPerguntaRespostaOrigemVOs()) {
					getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().editarListaPerguntaItemRespostaOrigemAdicionadasVO(perguntaRespostaOrigemPrincipalVO, perguntaRespostaOrigemPrincipalVO.getPerguntaItemRespostaOrigemVOs(), getUsuarioLogado());
				}		
			}
			setHabilitarAbas(true);			
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarPlanoEnsinoAnteriores() {
		try {
			PlanoEnsinoVO obj = (PlanoEnsinoVO) context().getExternalContext().getRequestMap().get("planoEnsinoItens");
			setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			List<PlanoEnsinoVO> listaPlanos = getFacadeFactory().getPlanoEnsinoFacade().consultar(obj.getUnidadeEnsino().getCodigo(), obj.getCurso().getCodigo(), getDisciplinaVO().getCodigo(), Constantes.EMPTY, Constantes.EMPTY, Constantes.EMPTY, Constantes.EMPTY, 0, 0, false, 0, 0, getUsuarioLogado());
			if (!permiteClonarVisualizarPlanoEnsinoOutrosProfessores()) {
				setPlanoEnsinoAnteriorVOs(listaPlanos.stream().filter(p -> {
					return p.getProfessorResponsavel().getCodigo().equals(0) 
							|| (!p.getProfessorResponsavel().getCodigo().equals(0) 
									&& p.getProfessorResponsavel().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo()));
				}).collect(Collectors.toList()));
			} else {
				setPlanoEnsinoAnteriorVOs(listaPlanos);
			}
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public Boolean getApresentarAbaTodosPlanosEnsino() {
		return getPlanoEnsinoVOs().size() > 1;
	}

	public void editarPlanoEnsino() {
		try {
			setPlanoEnsinoVO((PlanoEnsinoVO) getRequestMap().get("planoEnsinoItens"));
			verificarPermissaoAlteracaoPlanoEnsino();
			setPlanoEnsinoVO(getFacadeFactory().getPlanoEnsinoFacade().consultarPorChavePrimaria(getPlanoEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
			setPermitirAlterarUltimoPlanoEnsino(false);
			setTabActive("planoDisciplina");
			setHabilitarAbas(true);	
			setMensagemID("msg_informe_dados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada(MSG_TELA.msg_erro.name(), e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>Disciplina</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public void gravar() throws Exception {
		try {
			getFacadeFactory().getDisciplinaFacade().persistir(getDisciplinaVO(), false, getUsuarioLogado());
			fecharModalAlertaVinculosDisciplinaAlteracaoNome();
			setMensagemID("msg_dados_gravados");
		} catch (ConsistirException e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		} catch (Exception e) {
			if (e.getMessage().contains("unq_disciplina_nome_nivel_educacional")) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), "Já existe cadastro desta disciplina neste nível educacional");
			} else {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * DisciplinaCons.jsp. Define o tipo de consulta a ser executada, por meio
	 * de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List<DisciplinaVO> objs = new ArrayList<>(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals(Constantes.EMPTY)) {
					getControleConsulta().setValorConsulta("0");
				}
				if (getControleConsulta().getValorConsulta().trim() != null || !getControleConsulta().getValorConsulta().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getControleConsulta().getValorConsulta().trim());
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("abreviatura")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorAbreviatura(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Constantes.EMPTY;
	}
	
	

	@PostConstruct
	public void init() {
		if(getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			consultarVisaoProfessor();
		}else if(getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			consultarVisaoCoordenador();
		}else if(getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) {
			montarListaSelectItemCursoAluno();
		}
	}
	
	public void consultarVisaoProfessor() {
		try {
			if(getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				setDisciplinaEditar(Boolean.FALSE);
				setDisciplinaConsultar(Boolean.TRUE);
				getPlanoEnsinoVOs().clear();
				setPlanoEnsinoVOs(getFacadeFactory().getPlanoEnsinoFacade().consultarPlanoEnsinoProfessor(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), getAno(), getSemestre(), getSituacaoPlanoEnsino(), false, false, !permiteClonarVisualizarPlanoEnsinoOutrosProfessores(), getUsuarioLogado().getPessoa().getCodigo(), getTurmaVO().getCodigo(), getCursoVO().getCodigo(), getPeriodoLetivo(), getUsuarioLogado()));
				setPermitirAlterarUltimoPlanoEnsino(null);			
				setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	
	public void consultarVisaoCoordenador() {
		try {
			if(getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				setDisciplinaEditar(Boolean.FALSE);
				setDisciplinaConsultar(Boolean.TRUE);
				getPlanoEnsinoVOs().clear();
				setPlanoEnsinoVOs(getFacadeFactory().getPlanoEnsinoFacade().consultarPlanoEnsinoCoordenador(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), getAno(), getSemestre(), situacaoPlanoEnsino, false, false, getTurmaVO().getCodigo(), getCursoVO().getCodigo(), getPeriodoLetivo(), getUsuarioLogado()));
				setPermitirAlterarUltimoPlanoEnsino(null);			
				setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarObraBiblioteca() {
		try {
			super.consultar();
			List<CatalogoVO> objs = new ArrayList<CatalogoVO>(0);
			if (getCampoConsultaReferencia().equals("codigo")) {
				if (getValorConsultaReferencia().equals(Constantes.EMPTY)) {
					setValorConsultaReferencia("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaReferencia());
				CatalogoVO catalogo = getFacadeFactory().getCatalogoFacade().consultarPorChavePrimaria(new Integer(valorInt), Uteis.NIVELMONTARDADOS_COMBOBOX, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
				objs.add(catalogo);
				// objs =
				// getFacadeFactory().getCatalogoFacade().consultarPorCodigo(new
				// Integer(valorInt), false, Uteis.NIVELMONTARDADOS_COMBOBOX,
				// getUsuarioLogado());
			} else if (getCampoConsultaReferencia().equals("titulo")) {
				objs = getFacadeFactory().getCatalogoFacade().consultarPorTitulo(getValorConsultaReferencia(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, 0, getUnidadeEnsinoLogado().getCodigo(), 0, getUsuarioLogado());
			} else if (getCampoConsultaReferencia().equals("tipoExemplar")) {				
				objs = getFacadeFactory().getCatalogoFacade().consultarPorTipoCatalogo(getValorConsultaReferencia(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
			} else if (getCampoConsultaReferencia().equals("nomeEditora")) {
				objs = getFacadeFactory().getCatalogoFacade().consultarPorNomeEditora(getValorConsultaReferencia(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, 0, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
			} else if (getCampoConsultaReferencia().equals("nomeClassificacaoBibliografica")) {
				objs = getFacadeFactory().getCatalogoFacade().consultarPorNomeClassificacaoBibliografica(getValorConsultaReferencia(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
			} else if (getCampoConsultaReferencia().equals("biblioteca")) {
				objs = getFacadeFactory().getCatalogoFacade().consultarPorBiblioteca(getValorConsultaReferencia(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
			}

			setListaConsultaReferencia(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaReferencia(new ArrayList<CatalogoVO>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

		}
	}

	public void consultarDisciplina() {
		try {
			List<DisciplinaVO> objs = new ArrayList<>(0);
			if (getControleConsulta().getValorConsulta().equals(Constantes.EMPTY)) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals(Constantes.EMPTY)) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("areaConhecimento")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeAreaConhecimento(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>DisciplinaVO</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusão.
	 */
	public void excluir() {
		try {
			getFacadeFactory().getDisciplinaFacade().excluir(disciplinaVO, getUsuarioLogado());
			getFacadeFactory().getPlanoCursoFacade().excluir(disciplinaVO.getPlanoCursoVO());
			novo();
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void inicializarListasSelectItemTodosComboBox() throws Exception {
		montarListaSelectItemAreaConhecimento();
		montarListaSelectItemConfiguracaoAcademico();
		montarListaSelectItemGrupoPessoa();
		montarListaSelectFonteDeDadosBlackboard();
		montarListaSelectConteudoMasterBlackboard();
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>ConfiguracaoAcademico</code>. Buscando todos os objetos
	 * correspondentes a entidade <code>ConfiguracaoAcademico</code>. Esta
	 * rotina não recebe parâmetros para filtragem de dados, isto é importante
	 * para a inicialização dos dados da tela para o acionamento por meio
	 * requisições Ajax.
	 */
	public void montarListaSelectItemConfiguracaoAcademico() {
		try {
			// List resultadoConsulta =
			// getFacadeFactory().getConfiguracaoAcademicoFacade().consultarConfiguracoesASeremUsadas(false,
			// Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			List<ConfiguracaoAcademicoVO> resultadoConsulta = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarConfiguracaoAcademicoDeTodasConfiguracoesNivelCombobox(false, getUsuarioLogado());
			setListaSelectItemConfiguracaoAcademico(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	public void montarListaSelectItemGrupoPessoa() {
		try {
			List<GrupoPessoaVO> resultadoConsulta = getFacadeFactory().getGrupoPessoaFacade().consultaGrupoPessoaCombobox(false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemGrupoPessoa(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void montarListaSelectFonteDeDadosBlackboard() {
		try {
			List<FonteDeDadosBlackboardVO> resultadoConsulta = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().realizarConsultaFonteDeDadosBlackboardVO(getUsuarioLogado());
			setListaSelectFonteDeDadosBlackboard(UtilSelectItem.getListaSelectItem(resultadoConsulta, "id", "externalId"));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void montarListaSelectConteudoMasterBlackboard() {
		try {
			List<SalaAulaBlackboardVO> resultadoConsulta = getFacadeFactory().getSalaAulaBlackboardFacade().realizarConsultaSalaAulaBlackboardConteudoMaster(getUsuarioLogado());
			setListaSelectConteudoMasterBlackboard(UtilSelectItem.getListaSelectItem(resultadoConsulta, "idSalaAulaBlackboard", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>AreaConhecimento</code>.
	 */
	@SuppressWarnings("unchecked")
	public void montarListaSelectItemAreaConhecimento(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List<AreaConhecimentoVO> resultadoConsulta = null;
		Iterator<AreaConhecimentoVO> i = null;
		try {
			resultadoConsulta = consultarAreaConhecimentoPorNome(prm);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<>(0);
			objs.add(new SelectItem(0, Constantes.EMPTY));
			while (i.hasNext()) {
				AreaConhecimentoVO obj = (AreaConhecimentoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List<SelectItem>) objs, ordenador);
			setListaSelectItemAreaConhecimento(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>AreaConhecimento</code>. Buscando todos os objetos correspondentes
	 * a entidade <code>AreaConhecimento</code>. Esta rotina não recebe
	 * parâmetros para filtragem de dados, isto é importante para a
	 * inicialização dos dados da tela para o acionamento por meio requisições
	 * Ajax.
	 */
	public void montarListaSelectItemAreaConhecimento() {
		try {
			montarListaSelectItemAreaConhecimento(Constantes.EMPTY);
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List<AreaConhecimentoVO> consultarAreaConhecimentoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getAreaConhecimentoFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
	}

	public void selecionarDisciplinaEquivalente() {
		try {
			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaEquivalenteItem");
			getFacadeFactory().getDisciplinaEquivalenteFacade().adicionarDisciplinaEquivalente(getDisciplinaVO(), obj, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarDisciplinaComposta() {
		DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaComposta");
		getDisciplinaCompostaVO().setCompostaVO(obj);
		listaConsulta.clear();
		this.getControleConsulta().setValorConsulta(Constantes.EMPTY);
		this.getControleConsulta().setCampoConsulta(Constantes.EMPTY);
		obj = null;
	}

	public void selecionarPublicacaoBiblioteca() {
		try {
			CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("referencia");
			verificaPublicacaoExemplar(obj);
			getReferenciaBibliograficaVO().setCodigo(obj.getCodigo().intValue());
			getReferenciaBibliograficaVO().setCatalogo(getFacadeFactory().getCatalogoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			montarListaConcatenadaAutores();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void verificaPublicacaoExemplar(CatalogoVO obj) throws Exception {
		List<ExemplarVO> exemplarVOs = getFacadeFactory().getExemplarFacade().consultarPorCatalogo(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
		if (exemplarVOs.isEmpty()) {
			throw new Exception("Nenhum exemplar foi encontrado para esse catálogo na Biblioteca");
		}
	}

	public void adicionarConteudoPlanejamento() throws Exception {
		try {
			if (!getDisciplinaVO().getCodigo().equals(0)) {
				conteudoPlanejamentoVO.setDisciplina(getDisciplinaVO().getCodigo());
			}
			getDisciplinaVO().adicionarConteudoPlanejamentoVOs(getConteudoPlanejamentoVO());

			this.setConteudoPlanejamentoVO(new ConteudoPlanejamentoVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>ConteudoPlanejamento</code> para edição pelo usuário.
	 */
	public void editarPlanejamento() throws Exception {
		ConteudoPlanejamentoVO obj = (ConteudoPlanejamentoVO) context().getExternalContext().getRequestMap().get("planejamentoItens");
		setConteudoPlanejamentoVO(obj);
		disciplinaVO.setCont(0);
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>ConteudoPlanejamento</code> do objeto <code>disciplinaVO</code> da
	 * classe <code>Disciplina</code>
	 */
	public void removerPlanejamento() throws Exception {
		ConteudoPlanejamentoVO obj = (ConteudoPlanejamentoVO) context().getExternalContext().getRequestMap().get("planejamentoItens");
		setConteudoPlanejamentoVO(obj);
		getDisciplinaVO().ecluirConteudoPlanejamentoVOs(getConteudoPlanejamentoVO(), obj.getCodigo());
		this.setConteudoPlanejamentoVO(new ConteudoPlanejamentoVO());
		setMensagemID("msg_dados_excluidos");
	}

	public void editarConteudo() throws Exception {
		ConteudoPlanejamentoVO obj = (ConteudoPlanejamentoVO) context().getExternalContext().getRequestMap().get("conteudo");
		setConteudoPlanejamentoVO(obj);
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>ConteudoPlanejamento</code> do objeto <code>disciplinaVO</code> da
	 * classe <code>Disciplina</code>
	 */
	public void removerConteudo() throws Exception {
		ConteudoPlanejamentoVO obj = (ConteudoPlanejamentoVO) context().getExternalContext().getRequestMap().get("conteudo");
		getDisciplinaVO().excluirObjConteudoPlanejamentoVOs(obj.getCodigo());
		setMensagemID("msg_dados_excluidos");
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>ReferenciaBibliografica</code> para o objeto
	 * <code>disciplinaVO</code> da classe <code>Disciplina</code>
	 */
	public void adicionarReferenciaBibliografica() throws Exception {
		try {
			if (getReferenciaBibliograficaVO() != null && getReferenciaBibliograficaVO().getPublicacaoExistenteBiblioteca()) {

				if (!getDisciplinaVO().getCodigo().equals(0)) {
					getReferenciaBibliograficaVO().setDisciplina(getDisciplinaVO().getCodigo());
				}
				getDisciplinaVO().adicionarObjReferenciaBibliograficaVOs(getReferenciaBibliograficaVO());
				this.setReferenciaBibliograficaVO(new ReferenciaBibliograficaVO());
				setAutores(Constantes.EMPTY);
				setMensagemID("msg_dados_adicionados");
			} else {
				setMensagemDetalhada("Para adicionar a Referência Bibliográfica primeiro selecione se Existe na Biblioteca!");
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>ReferenciaBibliografica</code> para edição pelo usuário.
	 */
	public void editarReferenciaBibliografica() throws Exception {
		ReferenciaBibliograficaVO obj = (ReferenciaBibliograficaVO) context().getExternalContext().getRequestMap().get("referenciaBibliograficaItens");
		setAutores(obj.getCatalogo().getApresentarListaConcatenadaAutores());
		setReferenciaBibliograficaVO(obj);
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>ReferenciaBibliografica</code> do objeto <code>disciplinaVO</code>
	 * da classe <code>Disciplina</code>
	 */
	public void removerReferenciaBibliografica() throws Exception {
		ReferenciaBibliograficaVO obj = (ReferenciaBibliograficaVO) context().getExternalContext().getRequestMap().get("referenciaBibliograficaItens");
		getDisciplinaVO().excluirObjReferenciaBibliograficaVOs(obj.getCatalogo().getTitulo());
		setReferenciaBibliograficaVO(new ReferenciaBibliograficaVO());
		setAutores(Constantes.EMPTY);
		setMensagemID("msg_dados_excluidos");
	}

	public void adicionarDisciplinaComposta() throws Exception {
		try {
			if (!getDisciplinaVO().getCodigo().equals(0)) {
				getDisciplinaCompostaVO().getDisciplinaVO().setCodigo(getDisciplinaVO().getCodigo());
			}
			getFacadeFactory().getDisciplinaFacade().adicionarObjDisciplinaCompostaVOs(getDisciplinaCompostaVO(), getDisciplinaVO());
			this.setDisciplinaCompostaVO(new DisciplinaCompostaVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>DisciplinaEquivalente</code> para edição pelo usuário.
	 */
	public void editarDisciplinaEquivalente() throws Exception {
		DisciplinaEquivalenteVO obj = (DisciplinaEquivalenteVO) context().getExternalContext().getRequestMap().get("disciplinaEquivalenteItem");
		setDisciplinaEquivalenteVO(obj);
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>DisciplinaEquivalente</code> do objeto <code>disciplinaVO</code> da
	 * classe <code>Disciplina</code>
	 */
	public void removerDisciplinaEquivalente() throws Exception {
		DisciplinaEquivalenteVO obj = (DisciplinaEquivalenteVO) context().getExternalContext().getRequestMap().get("disciplinaEquivalenteItem");
		getDisciplinaVO().excluirObjDisciplinaEquivalenteVOs(obj.getEquivalente().getCodigo());
		setMensagemID("msg_dados_excluidos");
	}

	public void removerDisciplinaComposta() {
		DisciplinaCompostaVO obj = (DisciplinaCompostaVO) context().getExternalContext().getRequestMap().get("disciplinaComposta");
		try {
			if (!getDisciplinaVO().getCodigo().equals(0)) {
				getFacadeFactory().getDisciplinaFacade().validarDadosRemoverDisciplinaComposta(getDisciplinaVO().getCodigo(), getUsuarioLogado());
			}
			getFacadeFactory().getDisciplinaFacade().excluirObjDisciplinaCompostaVOs(obj.getCompostaVO().getCodigo(), getDisciplinaVO());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void moverParaBaixo() {
		getFacadeFactory().getDisciplinaFacade().moverParaBaixo((DisciplinaCompostaVO) context().getExternalContext().getRequestMap().get("disciplinaComposta"), this.getDisciplinaVO().getDisciplinaCompostaVOs());
	}

	public void moverParaCima() {
		getFacadeFactory().getDisciplinaFacade().moverParaCima((DisciplinaCompostaVO) context().getExternalContext().getRequestMap().get("disciplinaComposta"), this.getDisciplinaVO().getDisciplinaCompostaVOs());
	}

	public void irPaginaInicial() throws Exception {
		controleConsulta.setPaginaAtual(1);
		this.consultar();
	}

	public void irPaginaAnterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
		this.consultar();
	}

	public void irPaginaPosterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
		this.consultar();
	}

	public void irPaginaFinal() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Exemplar</code> por meio de sua respectiva chave primária. Esta
	 * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
	 * busca pela chave primária da entidade montando automaticamente o
	 * resultado da consulta para apresentação.
	 */
	public void consultarPublicacaoPorChavePrimaria() {
		try {
			Integer campoConsulta = referenciaBibliograficaVO.getCatalogo().getCodigo();
			CatalogoVO catalogo = getFacadeFactory().getCatalogoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
			referenciaBibliograficaVO.getCatalogo().setTitulo(catalogo.getTitulo());
			this.setPublicacaoBiblioteca_Erro(Constantes.EMPTY);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			referenciaBibliograficaVO.getCatalogo().setTitulo(Constantes.EMPTY);
			referenciaBibliograficaVO.getCatalogo().setCodigo(0);
			setMensagemID("msg_erro_dadosnaoencontrados");
			this.setPublicacaoBiblioteca_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Operação responsável por processar a consulta pelo parâmetro informado
	 * pelo usuário. <code>ExemplarBibliotecaVO</code> Após a consulta ela
	 * automaticamente adciona o código e o nome da cidade na tela.
	 */
	public List<CatalogoVO> consultarExemplarSuggestionbox(Object event) {
		try {
			String valor = event.toString();
			List<CatalogoVO> lista = getFacadeFactory().getCatalogoFacade().consultarPorTitulo(valor, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, 0, getUnidadeEnsinoLogado().getCodigo(), 0, getUsuarioLogado());
			this.setPublicacaoBiblioteca_Erro(Constantes.EMPTY);
			referenciaBibliograficaVO.getCatalogo().setTitulo(Constantes.EMPTY);
			return lista;
		} catch (Exception e) {
			return new ArrayList<CatalogoVO>(0);
		}
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>tipoReferencia</code>
	 */
	public List getListaSelectItemTipoReferenciaReferenciaBibliografica() throws Exception {
		List objs = new ArrayList<>(0);
		objs.add(new SelectItem(Constantes.EMPTY, Constantes.EMPTY));
		Hashtable tipoReferenciareferenciaBibliograficas = (Hashtable) Dominios.getTipoReferenciareferenciaBibliografica();
		Enumeration keys = tipoReferenciareferenciaBibliograficas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoReferenciareferenciaBibliograficas.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>tipoPublicacao</code>
	 */
	public List getListaSelectItemTipoPublicacaoReferenciaBibliografica() throws Exception {
		List objs = new ArrayList<>(0);
		objs.add(new SelectItem(Constantes.EMPTY, Constantes.EMPTY));
		Hashtable tipoPublicacaoReferenciaBibliograficas = (Hashtable) Dominios.getTipoPublicacaoReferenciaBibliografica();
		Enumeration keys = tipoPublicacaoReferenciaBibliograficas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoPublicacaoReferenciaBibliograficas.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	public void montarListaPeriodoLetivo() {
		try {
			if (!getMatricula().equals(Constantes.EMPTY)) {
				montarListaSelectPeriodosLetivos(Constantes.EMPTY);
			} else {
				setPeriodoLetivo(0);
				setListaSelectItemPeriodosLetivos(new ArrayList<SelectItem>(0));
				setListaSelectDisciplina(new ArrayList<SelectItem>(0));
				setDisciplinaVO(new DisciplinaVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			setListaSelectItemPeriodosLetivos(new ArrayList<SelectItem>(0));
			setListaSelectDisciplina(new ArrayList<SelectItem>(0));
			setDisciplinaVO(new DisciplinaVO());
		}
	}

	public void montarListaSelectPeriodosLetivos(String prm) throws Exception {
		MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatricula(), getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, null);
		if (matriculaVO.getCurso().getNivelEducacionalPosGraduacao()) {
			MatriculaPeriodoVO mat = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorMatricula(getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, null);
			List<PeriodoLetivoVO> periodo = consultarPeriodoLetivo(mat.getGradeCurricular().getCodigo());
			if (periodo.size() == 1) {
				setPeriodoLetivo(((PeriodoLetivoVO) periodo.get(0)).getCodigo());
			}
			montarListaDisciplina();
			setListaSelectItemPeriodosLetivos(new ArrayList<SelectItem>(0));
		} else {
			List<PeriodoLetivoVO> periodoLetivoVOs = getFacadeFactory().getPeriodoLetivoFacade().consultarPorGradeCurricular(matriculaVO.getGradeCurricularAtual().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemPeriodosLetivos(UtilSelectItem.getListaSelectItem(periodoLetivoVOs, "codigo", "descricao"));
			setPeriodoLetivo(0);
		}
	}

	public List<PeriodoLetivoVO> consultarPeriodoLetivo(Integer codigoPeriodo) throws Exception {
		return getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivos(codigoPeriodo, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}

	public List<MatriculaPeriodoVO> consultarMatriculaPeriodo(String matricula) throws Exception {
		return getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaPeriodos(matricula, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
	}

	
	public void montarListaSelectItemCursoAluno() {
		if(getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "DisciplinaControle", "Inicializando montar Lista Select Item Curso Aluno", "Montando");			
			if (getVisaoAlunoControle() != null) {
				getVisaoAlunoControle().inicializarMenuDisciplina();
				setMatricula(getVisaoAlunoControle().getMatricula().getMatricula());
				if(!getVisaoAlunoControle().getMatricula().getMatriculaComHistoricoAlunoVO().getInicializado()){					
					getVisaoAlunoControle().getMatricula().setMatriculaComHistoricoAlunoVO(getFacadeFactory().getHistoricoFacade().carregarDadosMatriculaComHistoricoAlunoVO(getVisaoAlunoControle().getMatricula(), getVisaoAlunoControle().getMatricula().getGradeCurricularAtual().getCodigo(), !getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getUnidadeEnsinoLogado().getCodigo()).getNaoApresentarProfessorVisaoAluno(), getVisaoAlunoControle().getMatricula().getCurso().getConfiguracaoAcademico(), getUsuarioLogado()));
					getVisaoAlunoControle().getMatricula().getMatriculaComHistoricoAlunoVO().getGraficoAtividadeComplementar();
					getVisaoAlunoControle().getMatricula().getMatriculaComHistoricoAlunoVO().getGraficoEstagio();
					getVisaoAlunoControle().getMatricula().getMatriculaComHistoricoAlunoVO().getGraficoSituacaoAcademicaAluno();
				}
				getVisaoAlunoControle().getMatricula().getMatriculaComHistoricoAlunoVO().gerarDadosGraficoEvolucaoAcademicaAluno();
				getVisaoAlunoControle().getMatricula().setGradeCurricularAtual(getVisaoAlunoControle().getMatricula().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO());
				getListaSelectItemPeriodosLetivos().clear();
				getListaSelectItemPeriodosLetivos().add(new SelectItem("TODOS", Constantes.EMPTY));
				setTabActive("TODOS");
				for(PeriodoLetivoVO periodoLetivoVO:getVisaoAlunoControle().getMatricula().getGradeCurricularAtual().getPeriodoLetivosVOs()){
					getListaSelectItemPeriodosLetivos().add(new SelectItem(periodoLetivoVO.getDescricao(), periodoLetivoVO.getDescricao()));
				}
				for(GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO:getVisaoAlunoControle().getMatricula().getGradeCurricularAtual().getGradeCurricularGrupoOptativaVOs()){
					getListaSelectItemPeriodosLetivos().add(new SelectItem(gradeCurricularGrupoOptativaVO.getDescricao(), gradeCurricularGrupoOptativaVO.getDescricao()));
				}

				getFacadeFactory().getPlanoEnsinoFacade().realizarVerificacaoDisciplinaAlunoQuePossuiPlanoEnsino(getVisaoAlunoControle().getMatricula(), getUsuarioLogado());
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "DisciplinaControle", "Finalizando montar Lista Select Item Curso Aluno", "Montando");

		} catch (Exception e) {
			e.printStackTrace();
			
			
		}
		}
//		return Uteis.getCaminhoRedirecionamentoNavegacao("minhasDisciplinasAluno");
	}

	public List<MatriculaVO> consultarMatriculaPorCodigoPessoa() throws Exception {
		return getFacadeFactory().getMatriculaFacade().consultaRapidaPorCodigoPessoaNaoCancelada(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
	}

	public void montarListaDisciplina() {
		try {
			setListaSelectDisciplina(new ArrayList<SelectItem>(0));
			setDisciplinaVO(new DisciplinaVO());
			if (getPeriodoLetivo().intValue() != 0) {
				List<SelectItem> objs = new ArrayList<SelectItem>(0);
				List<GradeDisciplinaVO> listaConsulta = consultarDisciplinaCursoPeridoLetivo();
				Iterator<GradeDisciplinaVO> i = listaConsulta.iterator();
				objs.add(new SelectItem(0, Constantes.EMPTY));
				while (i.hasNext()) {
					GradeDisciplinaVO obj = (GradeDisciplinaVO) i.next();
					objs.add(new SelectItem(obj.getDisciplina().getCodigo(), obj.getDisciplina().getNome()));
				}
				setListaSelectDisciplina(objs);
			}
		} catch (Exception e) {
			setDisciplinaVO(new DisciplinaVO());
			setListaSelectDisciplina(new ArrayList<SelectItem>(0));
		}

	}

	public void consultarDisciplinaAluno() {
		try {
			limparMensagem();
			if (Uteis.isAtributoPreenchido(getDisciplina())) {
				setPlanoEnsinoVO(getFacadeFactory().getPlanoEnsinoFacade().consultarPorDisciplinaMatriculaAluno(getDisciplina().intValue(), getMatricula(), true, getHistoricoVO(), getUsuarioLogado()));
				if(getPlanoEnsinoVO().isNovoObj()){
					setDisciplina(0);
					throw new Exception("Não Está Disponível o Plano de Ensino para a Disciplina Selecionada.");
				}
				getPlanoEnsinoVO().setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getDisciplina(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				getPlanoEnsinoVO().getDisciplina().setDisciplinaEquivalenteVOs(getFacadeFactory().getDisciplinaEquivalenteFacade().consultarDisciplinaEquivalentes(getDisciplina(), false, getUsuarioLogado()));
				getFacadeFactory().getPlanoEnsinoFacade().atualizarTotalCargaHoraria(getPlanoEnsinoVO());
				if(Uteis.isAtributoPreenchido(getHistoricoVO()) && !getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getUnidadeEnsinoLogado().getCodigo()).getNaoApresentarProfessorVisaoAluno()){					
					getPlanoEnsinoVO().setProfessor(getHistoricoVO().getNomeProfessor());
				}
				Integer codigoQuestionario = 0;
				if(Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getCurso())) {
					codigoQuestionario = getPlanoEnsinoVO().getCurso().getQuestionarioVO().getCodigo();
				}else {
					codigoQuestionario = getConfiguracaoGeralPadraoSistema().getQuestionarioPlanoEnsino().getCodigo();
				}
				
				getPlanoEnsinoVO().setQuestionarioRespostaOrigemVO(getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().consultarPorQuestionarioPlanoEnsino(codigoQuestionario, getPlanoEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
				if(Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getQuestionarioRespostaOrigemVO())) {
					getPlanoEnsinoVO().getQuestionarioRespostaOrigemVO().setPerguntaRespostaOrigemVOs(getFacadeFactory().getPerguntaRespostaOrigemInterfaceFacade().consultarPorQuestionarioPlanoEnsino(codigoQuestionario, getPlanoEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
					 
					for (PerguntaRespostaOrigemVO perguntaRespostaOrigemPrincipalVO : getPlanoEnsinoVO().getQuestionarioRespostaOrigemVO().getPerguntaRespostaOrigemVOs()) {
						getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().editarListaPerguntaItemRespostaOrigemAdicionadasVO(perguntaRespostaOrigemPrincipalVO, perguntaRespostaOrigemPrincipalVO.getPerguntaItemRespostaOrigemVOs(), getUsuarioLogado());
					}		
				}
			} else {
				
				setPlanoEnsinoVO(null);
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ALERTA);
			setPlanoEnsinoVO(null);
		}
	}

	public List<GradeDisciplinaVO> consultarDisciplinaCursoPeridoLetivo() throws Exception {
		return getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinas(getPeriodoLetivo(), false, getUsuarioLogado(), null);
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Disciplina</code> por meio de sua respectiva chave primária. Esta
	 * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
	 * busca pela chave primária da entidade montando automaticamente o
	 * resultado da consulta para apresentação.
	 */
	public void consultarDisciplinaPorChavePrimaria() {
		try {
			Integer campoConsulta = disciplinaEquivalenteVO.getEquivalente().getCodigo();
			DisciplinaVO disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			disciplinaEquivalenteVO.getEquivalente().setNome(disciplina.getNome());
			this.setEquivalente_Erro(Constantes.EMPTY);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			disciplinaEquivalenteVO.getEquivalente().setNome(Constantes.EMPTY);
			disciplinaEquivalenteVO.getEquivalente().setCodigo(0);
			this.setEquivalente_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Disciplina</code> por meio de sua respectiva chave primária. Esta
	 * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
	 * busca pela chave primária da entidade montando automaticamente o
	 * resultado da consulta para apresentação.
	 */
	/**
	 * Operação responsável por processar a consulta pelo parâmetro informado
	 * pelo usuário.<code>EquivalenteVO</code> Após a consulta ela
	 * automaticamente adciona o código e o nome da cidade na tela.
	 */
	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>tipoDisciplina</code>
	 */
	public List getListaSelectItemTipoDisciplinaDisciplina() throws Exception {
		List objs = new ArrayList<>(0);
		objs.add(new SelectItem(Constantes.EMPTY, Constantes.EMPTY));
		Hashtable tipoDisciplinaDisciplinas = (Hashtable) Dominios.getTipoDisciplinaDisciplina();
		Enumeration keys = tipoDisciplinaDisciplinas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoDisciplinaDisciplinas.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	public List getListaSelectItemClassificacaoConteudoPlanejamento() throws Exception {
		List objs = new ArrayList<>(0);
		objs.add(new SelectItem(Constantes.EMPTY, Constantes.EMPTY));
		Hashtable conteudoPlanejamento = (Hashtable) Dominios.getConteudoPlanejamento();
		Enumeration keys = conteudoPlanejamento.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) conteudoPlanejamento.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("abreviatura", "Abreviatura"));
		// itens.add(new SelectItem("areaConhecimento",
		// "Área de Conhecimento"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboReferencia() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("titulo", "Título"));
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("biblioteca", "Biblioteca"));
		itens.add(new SelectItem("tipoExemplar", "Tipo Exemplar"));
		itens.add(new SelectItem("nomeEditora", "Nome Editora"));
		itens.add(new SelectItem("nomeClassificacaoBibliografica", "Nome Classificacao Bibliografica"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<>(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("disciplinaCons");
	}

	public void limarReferenciaBibliograficaVO() {
		// getReferenciaBibliograficaVO().setAnoPublicacao(Constantes.EMPTY);
		// getReferenciaBibliograficaVO().setAutores(Constantes.EMPTY);
		// getReferenciaBibliograficaVO().setEdicao(Constantes.EMPTY);
		// getReferenciaBibliograficaVO().setISBN(Constantes.EMPTY);
		// getReferenciaBibliograficaVO().setLocalPublicacao(Constantes.EMPTY);
		getReferenciaBibliograficaVO().setCatalogo(null);
		// getReferenciaBibliograficaVO().setTipoPublicacao(null);
		getReferenciaBibliograficaVO().setTipoReferencia(null);
		// getReferenciaBibliograficaVO().setTitulo(Constantes.EMPTY);
		getReferenciaBibliograficaVO().setCatalogo(new CatalogoVO());
	}
	
	public void realizarLimpezaCamposPorClassificacaoDisciplina() {
		try {
			if(!getDisciplinaVO().getClassificacaoDisciplina().isTcc()) {
				getDisciplinaVO().setNrMinimoAlunosPorGrupo(0);
				getDisciplinaVO().setNrMinimoAlunosPorSala(0);
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarLimpezaCamposPorDividirSalaEmGrupos() {
		try {
			if(!getDisciplinaVO().getDividirSalaEmGrupo()) {
				getDisciplinaVO().setNrMinimoAlunosPorGrupo(0);
				getDisciplinaVO().setNrMaximoAulosPorGrupo(0);
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String getPublicacaoBiblioteca_Erro() {
		return publicacaoBiblioteca_Erro;
	}

	public void setPublicacaoBiblioteca_Erro(String publicacaoBiblioteca_Erro) {
		this.publicacaoBiblioteca_Erro = publicacaoBiblioteca_Erro;
	}

	public ReferenciaBibliograficaVO getReferenciaBibliograficaVO() {
		if (referenciaBibliograficaVO == null) {
			referenciaBibliograficaVO = new ReferenciaBibliograficaVO();
		}
		return referenciaBibliograficaVO;
	}

	public void setReferenciaBibliograficaVO(ReferenciaBibliograficaVO referenciaBibliograficaVO) {
		this.referenciaBibliograficaVO = referenciaBibliograficaVO;
	}

	public String getEquivalente_Erro() {
		return equivalente_Erro;
	}

	public void setEquivalente_Erro(String equivalente_Erro) {
		this.equivalente_Erro = equivalente_Erro;
	}

	public DisciplinaEquivalenteVO getDisciplinaEquivalenteVO() {
		return disciplinaEquivalenteVO;
	}

	public void setDisciplinaEquivalenteVO(DisciplinaEquivalenteVO disciplinaEquivalenteVO) {
		this.disciplinaEquivalenteVO = disciplinaEquivalenteVO;
	}

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public String getCampoConsultaReferencia() {
		return campoConsultaReferencia;
	}

	public void setCampoConsultaReferencia(String campoConsultaReferencia) {
		this.campoConsultaReferencia = campoConsultaReferencia;
	}

	public List<CatalogoVO> getListaConsultaReferencia() {
		return listaConsultaReferencia;
	}

	public void setListaConsultaReferencia(List<CatalogoVO> listaConsultaReferencia) {
		this.listaConsultaReferencia = listaConsultaReferencia;
	}

	public String getValorConsultaReferencia() {
		return valorConsultaReferencia;
	}

	public void setValorConsultaReferencia(String valorConsultaReferencia) {
		this.valorConsultaReferencia = valorConsultaReferencia;
	}

	public ConteudoPlanejamentoVO getConteudoPlanejamentoVO() {
		if (conteudoPlanejamentoVO == null) {
			conteudoPlanejamentoVO = new ConteudoPlanejamentoVO();
		}
		return conteudoPlanejamentoVO;
	}

	public void setConteudoPlanejamentoVO(ConteudoPlanejamentoVO conteudoPlanejamentoVO) {
		this.conteudoPlanejamentoVO = conteudoPlanejamentoVO;
	}

	private DisciplinaVO inicializaConteudoPlanejamento(DisciplinaVO obj1) {
		Iterator i = obj1.getConteudoPlanejamentoVOs().iterator();
		while (i.hasNext()) {
			ConteudoPlanejamentoVO obj = (ConteudoPlanejamentoVO) i.next();
			if (obj.getClassificacao().equals("CO")) {
				obj1.getConteudoVOs().add(obj);
			}
		}
		return obj1;
	}

	public List<SelectItem> getListaSelectItemAreaConhecimento() {
		return listaSelectItemAreaConhecimento;
	}

	public void setListaSelectItemAreaConhecimento(List<SelectItem> listaSelectItemAreaConhecimento) {
		this.listaSelectItemAreaConhecimento = listaSelectItemAreaConhecimento;
	}

	public List<SelectItem> getListaSelectItemMatriculaCursoTurno() {
		return listaSelectItemMatriculaCursoTurno;
	}

	public void setListaSelectItemMatriculaCursoTurno(List<SelectItem> listaSelectItemMatriculaCursoTurno) {
		this.listaSelectItemMatriculaCursoTurno = listaSelectItemMatriculaCursoTurno;
	}

	public List<SelectItem> getListaSelectItemPeriodosLetivos() {
		if (listaSelectItemPeriodosLetivos == null) {
			listaSelectItemPeriodosLetivos = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemPeriodosLetivos;
	}

	public void setListaSelectItemPeriodosLetivos(List<SelectItem> listaSelectItemPeriodosLetivos) {
		this.listaSelectItemPeriodosLetivos = listaSelectItemPeriodosLetivos;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public Integer getPeriodoLetivo() {
		return periodoLetivo;
	}

	public void setPeriodoLetivo(Integer periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}

	public List<SelectItem> getListaSelectDisciplina() {
		return listaSelectDisciplina;
	}

	public void setListaSelectDisciplina(List<SelectItem> listaSelectDisciplina) {
		this.listaSelectDisciplina = listaSelectDisciplina;
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

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		disciplinaVO = null;
		/**
		 * Interface <code>DisciplinaInterfaceFacade</code> responsável pela
		 * interconexão da camada de controle com a camada de negócio. Criando
		 * uma independência da camada de controle com relação a tenologia de
		 * persistência dos dados (DesignPatter: Façade).
		 */
		disciplinaEquivalenteVO = null;
		equivalente_Erro = null;
		referenciaBibliograficaVO = null;
		publicacaoBiblioteca_Erro = null;
		conteudoPlanejamentoVO = null;
		valorConsultaReferencia = null;
		campoConsultaReferencia = null;
		Uteis.liberarListaMemoria(listaConsultaReferencia);

	}

	/**
	 * @return the listaSelectItemConfiguracaoAcademico
	 */
	public List<SelectItem> getListaSelectItemConfiguracaoAcademico() {
		return listaSelectItemConfiguracaoAcademico;
	}

	/**
	 * @param listaSelectItemConfiguracaoAcademico
	 *            the listaSelectItemConfiguracaoAcademico to set
	 */
	public void setListaSelectItemConfiguracaoAcademico(List<SelectItem> listaSelectItemConfiguracaoAcademico) {
		this.listaSelectItemConfiguracaoAcademico = listaSelectItemConfiguracaoAcademico;
	}

	/**
	 * @return the utilizarConfiguracaoAcademicoEspecifica
	 */
	public Boolean getUtilizarConfiguracaoAcademicoEspecifica() {
		return utilizarConfiguracaoAcademicoEspecifica;
	}

	/**
	 * @param utilizarConfiguracaoAcademicoEspecifica
	 *            the utilizarConfiguracaoAcademicoEspecifica to set
	 */
	public void setUtilizarConfiguracaoAcademicoEspecifica(Boolean utilizarConfiguracaoAcademicoEspecifica) {
		this.utilizarConfiguracaoAcademicoEspecifica = utilizarConfiguracaoAcademicoEspecifica;
	}

	// public void altualizarCfgAcademico() {
	// if (!this.getUtilizarConfiguracaoAcademicoEspecifica()) {
	// this.getGradeDisciplinaVO().setConfiguracaoAcademico(new
	// ConfiguracaoAcademicoVO());
	// }
	// }

	public void executarUnificacaoDeDisciplinasIdenticadas() {
		try {
			getFacadeFactory().getDisciplinaFacade().executarUnificacaoDeDisciplinasIdenticadas(getConfiguracaoFinanceiroPadraoSistema(), getDisciplinaDefasada(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			setMensagemID("msg_dados_gravados");
		} catch (Exception ex) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
		}
	}

	/**
	 * @return the codigoReferencia
	 */
	public Integer getCodigoReferencia() {
		return codigoReferencia;
	}

	/**
	 * @param codigoReferencia
	 *            the codigoReferencia to set
	 */
	public void setCodigoReferencia(Integer codigoReferencia) {
		this.codigoReferencia = codigoReferencia;
	}

	/**
	 * @return the disciplinaDefasada
	 */
	public Integer getDisciplinaDefasada() {
		return disciplinaDefasada;
	}

	/**
	 * @param disciplinaDefasada
	 *            the disciplinaDefasada to set
	 */
	public void setDisciplinaDefasada(Integer disciplinaDefasada) {
		this.disciplinaDefasada = disciplinaDefasada;
	}

	/**
	 * @return the disciplinaCorreta
	 */
	public Integer getDisciplinaCorreta() {
		return disciplinaCorreta;
	}

	/**
	 * @param disciplinaCorreta
	 *            the disciplinaCorreta to set
	 */
	public void setDisciplinaCorreta(Integer disciplinaCorreta) {
		this.disciplinaCorreta = disciplinaCorreta;
	}

	public void executarTransferenciaDisciplinasHistoricoAltigoGradeCorreta() {
		try {
			getFacadeFactory().getDisciplinaFacade().executarTransferenciaDisciplinasHistoricoAltigoGradeCorreta_Direito(this.codigoReferencia, getUsuarioLogado());
		} catch (Exception ex) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
		}
	}

	public void executarTransferenciaDisciplinasEspecifica() {
		try {
			getFacadeFactory().getDisciplinaFacade().processarTrocaDisciplinaHistoricoDiretamente(this.codigoReferencia, this.disciplinaDefasada, this.disciplinaCorreta, getUsuarioLogado());
		} catch (Exception ex) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
		}
	}

	public boolean getIsApresentarDadosDisciplina() {
		return getDisciplina() != 0;
	}

	public DisciplinaCompostaVO getDisciplinaCompostaVO() {
		if (disciplinaCompostaVO == null) {
			disciplinaCompostaVO = new DisciplinaCompostaVO();
		}
		return disciplinaCompostaVO;
	}

	public void setDisciplinaCompostaVO(DisciplinaCompostaVO disciplinaCompostaVO) {
		this.disciplinaCompostaVO = disciplinaCompostaVO;
	}

	public boolean getApresentarTabDisciplinaComposta() {
		return getDisciplinaVO().getDisciplinaComposta();
	}

	public void montarListaConcatenadaAutores() {
		String autores = Constantes.EMPTY;
		String virgula = Constantes.EMPTY;
		if (getReferenciaBibliograficaVO().getCatalogo() != null && !getReferenciaBibliograficaVO().getCatalogo().isNovoObj()) {
			for (AutorVO autor : getReferenciaBibliograficaVO().getCatalogo().getAutorVOs()) {
				autores += autor.getNome();
				autores += virgula;
				virgula = ",";
			}
		}
		setAutores(autores);
	}

	public String getAutores() {
		return autores;
	}

	public void setAutores(String autores) {
		this.autores = autores;
	}

	public PlanoEnsinoVO getPlanoEnsinoVO() {
		if (planoEnsinoVO == null) {
			planoEnsinoVO = new PlanoEnsinoVO();
		}
		return planoEnsinoVO;
	}

	public void setPlanoEnsinoVO(PlanoEnsinoVO planoEnsinoVO) {
		this.planoEnsinoVO = planoEnsinoVO;
	}

	public List<PlanoEnsinoVO> getPlanoEnsinoVOs() {
		if (planoEnsinoVOs == null) {
			planoEnsinoVOs = new ArrayList<PlanoEnsinoVO>(0);
		}
		return planoEnsinoVOs;
	}

	public void setPlanoEnsinoVOs(List<PlanoEnsinoVO> planoEnsinoVOs) {
		this.planoEnsinoVOs = planoEnsinoVOs;
	}

	public String getTabActive() {
		if (tabActive == null) {
			tabActive = "planoDisciplina";
		}
		return tabActive;
	}

	public void setTabActive(String tabActive) {
		this.tabActive = tabActive;
	}

	/**
	 * Se for false desabilita o botão gravar, clonar, novo e todos os
	 * inputText.
	 */
	public boolean getIsPermitirAlterarPlanoEnsino() {
		return (!Uteis.isAtributoPreenchido(getPlanoEnsinoVO()) || (Uteis.isAtributoPreenchido(getPlanoEnsinoVO()) &&  (getLoginControle().getPermissaoAcessoMenuVO().getAlterarTodosPlanoEnsino() || getPermitirAlterarUltimoPlanoEnsino() || getPermitirVisualizarBotoes())));
	}

	public Boolean getPermitirAlterarUltimoPlanoEnsino() {
		if (permitirAlterarUltimoPlanoEnsino == null) {
			permitirAlterarUltimoPlanoEnsino = Boolean.FALSE;
		}
		return permitirAlterarUltimoPlanoEnsino;
	}

	public void setPermitirAlterarUltimoPlanoEnsino(Boolean permitirAlterarUltimoPlanoEnsino) {
		this.permitirAlterarUltimoPlanoEnsino = permitirAlterarUltimoPlanoEnsino;
	}

	/*
	 * Método responsável por verificar se o Professor ou Coordenador tem
	 * permissão para alterar o plano de ensino selecionado de acordo com o ano
	 * e semestre, caso contrário ele seta o boleano
	 * <code>setPermitirAlterarUltimoPlanoEnsino</code> como falso.
	 */
	public void verificarPermissaoAlteracaoPlanoEnsino() {
		setPermitirVisualizarBotoes(false);
		if (Uteis.isAtributoPreenchido(getPlanoEnsinoVO()) && getLoginControle().getPermissaoAcessoMenuVO().getAlterarApenasUltimoPlanoEnsino() && !getLoginControle().getPermissaoAcessoMenuVO().getAlterarTodosPlanoEnsino()) {
			PlanoEnsinoVO ultimoPlanoEnsino;
			try {
				ultimoPlanoEnsino = getFacadeFactory().getPlanoEnsinoFacade().consultarPorUnidadeEnsinoCursoDisciplinaAnoSemestre(getPlanoEnsinoVO().getUnidadeEnsino().getCodigo(), getPlanoEnsinoVO().getCurso().getCodigo(), getPlanoEnsinoVO().getDisciplina().getCodigo(),null, null, null, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if(Uteis.isAtributoPreenchido(ultimoPlanoEnsino)){
					if(ultimoPlanoEnsino.getCodigo().equals(getPlanoEnsinoVO().getCodigo())) {
						setPermitirAlterarUltimoPlanoEnsino(true);
						setPermitirVisualizarBotoes(true);
					}
					else {
						setPermitirAlterarUltimoPlanoEnsino(false);
						setPermitirVisualizarBotoes(false);
					}
					
				}
				
				
			} catch (Exception e) {			
				e.printStackTrace();
			}
		}else if(getLoginControle().getPermissaoAcessoMenuVO().getAlterarTodosPlanoEnsino() || (getLoginControle().getPermissaoAcessoMenuVO().getCriarNovoPlanoEnsino() && getPlanoEnsinoVO().getCodigo().equals(0))){
			setPermitirVisualizarBotoes(true);
		}
		else if(Uteis.isAtributoPreenchido(getPlanoEnsinoVO()) && !getLoginControle().getPermissaoAcessoMenuVO().getAlterarApenasUltimoPlanoEnsino() && !getLoginControle().getPermissaoAcessoMenuVO().getAlterarTodosPlanoEnsino()){
			if(getUsuarioLogado().getPessoa().equals(getPlanoEnsinoVO().getProfessorResponsavel())) {
				setPermitirVisualizarBotoes(true);
			}
			
		}
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboCurso;
	}

	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "nome";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public String getValorConsultaCurso() {
		if (valorConsultaCurso == null) {
			valorConsultaCurso = Constantes.EMPTY;
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public void consultarCurso() {
		try {
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getCampoConsultaCurso().equals("nome")) {
				if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
					objs = getFacadeFactory().getCursoFacade().consultarPorNomeCursoProfessor(getValorConsultaCurso(), getUsuarioLogado().getPessoa().getCodigo(), getPlanoEnsinoVO().getUnidadeEnsino().getCodigo(), getPlanoEnsinoVO().getDisciplina().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				} else {
					objs = getFacadeFactory().getCursoFacade().consultarListaCursoPorNomeCursoCodigoPessoaCoordenador(getValorConsultaCurso(), getUsuarioLogado().getPessoa().getCodigo(), getPlanoEnsinoVO().getUnidadeEnsino().getCodigo(), getPlanoEnsinoVO().getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				}
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
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

	public void selecionarCurso() throws Exception {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			getPlanoEnsinoVO().setCurso(obj);

			getFacadeFactory().getPlanoEnsinoFacade().realizarCriacaoQuestionarioRespostaOrigem(getPlanoEnsinoVO(), obj.getQuestionarioVO(), getUsuarioLogado());	
			
			montarListaSelectItemTurno();
			
			/*if (getHabilitarControlePorCalendarioLancamentoPlanoEnsino() && getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				liberarCalendarioLancamentoPlanoEnsino();
			}
			setHabilitarAbas(Boolean.TRUE);*/
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada(MSG_TELA.msg_erro.name(), e, Uteis.ERRO);
			setHabilitarAbas(Boolean.FALSE);
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	}

	public void selecionarDisciplina() throws Exception {
		try {
			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
			setDisciplinaVO(obj);
			// getPlanoEnsinoVO().setDisciplina(null);
		} catch (Exception e) {
		}
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
			listaSelectItemUnidadeEnsino.add(new SelectItem(0, Constantes.EMPTY));
			try {
				List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
					listaSelectItemUnidadeEnsino.add(new SelectItem(unidadeEnsinoVO.getCodigo(), unidadeEnsinoVO.getNome()));
				}
			} catch (Exception e) {

			}
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public void adicionarConteudoPlanejamentoVOs() throws Exception {
		try {
			getFacadeFactory().getPlanoEnsinoFacade().adicionarConteudoPlanejamentoVOs(getPlanoEnsinoVO(), getConteudoPlanejamentoVO());
			setConteudoPlanejamentoVO(new ConteudoPlanejamentoVO());
			getFacadeFactory().getPlanoEnsinoFacade().atualizarTotalCargaHoraria(getPlanoEnsinoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void editarConteudoPlanejamentoVOs() {
		try {
			setConteudoPlanejamentoVO(new ConteudoPlanejamentoVO());
			setConteudoPlanejamentoVO((ConteudoPlanejamentoVO) getRequestMap().get("planejamento"));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerConteudoPlanejamentoVOs() {
		try {
			ConteudoPlanejamentoVO obj = (ConteudoPlanejamentoVO) getRequestMap().get("planejamento");
			getFacadeFactory().getPlanoEnsinoFacade().removerConteudoPlanejamentoVOs(getPlanoEnsinoVO(), obj);
			getFacadeFactory().getPlanoEnsinoFacade().atualizarTotalCargaHoraria(getPlanoEnsinoVO());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	
	public void alterarOrdemApresentacaoConteudoPlanejado(DropEvent dropEvent) {
		try {
			if (dropEvent.getDragValue() instanceof ConteudoPlanejamentoVO && dropEvent.getDropValue() instanceof ConteudoPlanejamentoVO) {
				getFacadeFactory().getPlanoEnsinoFacade().alterarOrdenacaoConteudoPlanejamentoVO(getPlanoEnsinoVO(), (ConteudoPlanejamentoVO) dropEvent.getDragValue(), (ConteudoPlanejamentoVO) dropEvent.getDropValue());
				limparMensagem();
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarAguardandoAprovacao() {
		try {
			getFacadeFactory().getPlanoEnsinoFacade().persistir(getPlanoEnsinoVO(), getUsuarioLogado());
			getPlanoEnsinoVO().setSituacao(SituacaoPlanoEnsinoEnum.AGUARDANDO_APROVACAO.getValor());
			getPlanoEnsinoVO().setResponsavelAutorizacao((getUsuarioLogado()));
			getFacadeFactory().getPlanoEnsinoFacade().alterarSituacao(getPlanoEnsinoVO().getCodigo(), 
					SituacaoPlanoEnsinoEnum.AGUARDANDO_APROVACAO.getValor(), getPlanoEnsinoVO().getMotivo(), getUsuarioLogado());
			setMensagemID("msg_PlanoEnsinoAguardandoAprovacao");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarEmRevisao() {
		try {
			getFacadeFactory().getPlanoEnsinoFacade().persistir(getPlanoEnsinoVO(), getUsuarioLogado());
			getPlanoEnsinoVO().setSituacao(SituacaoPlanoEnsinoEnum.EM_REVISAO.getValor());
			getFacadeFactory().getPlanoEnsinoFacade().alterarSituacao(getPlanoEnsinoVO().getCodigo(), SituacaoPlanoEnsinoEnum.EM_REVISAO.getValor(), 
					getPlanoEnsinoVO().getMotivo(), getUsuarioLogado());
			setMensagemID("msg_PlanoEnsinoEmRevisao");
			setAbrirModalRevisao(Boolean.FALSE);
		} catch (Exception e) {
			setAbrirModalRevisao(Boolean.TRUE);
			getPlanoEnsinoVO().setSituacao(SituacaoPlanoEnsinoEnum.AGUARDANDO_APROVACAO.getValor());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void alterarAutorizado() {
		try {
			getPlanoEnsinoVO().setSituacao(SituacaoPlanoEnsinoEnum.AUTORIZADO.getValor());
			getPlanoEnsinoVO().setMotivo(Constantes.EMPTY);
			getFacadeFactory().getPlanoEnsinoFacade().alterarSituacao(getPlanoEnsinoVO().getCodigo(), 
					SituacaoPlanoEnsinoEnum.AUTORIZADO.getValor(), getPlanoEnsinoVO().getMotivo(),  getUsuarioLogado());
			setMensagemID("msg_PlanoEnsinoAutorizado");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public boolean apresentarGravar() {
		return getPlanoEnsinoVO().getSituacao().equals(SituacaoPlanoEnsinoEnum.EM_REVISAO.getValor()) ||
						getPlanoEnsinoVO().getSituacao().equals(SituacaoPlanoEnsinoEnum.PENDENTE.getValor());
	}

	public boolean apresentarSolicitarAprovacao() {
		return Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getCodigo()) &&
				(getPlanoEnsinoVO().getSituacao().equals(SituacaoPlanoEnsinoEnum.EM_REVISAO.getValor()) ||
				 getPlanoEnsinoVO().getSituacao().equals(SituacaoPlanoEnsinoEnum.PENDENTE.getValor()));
	}

	public boolean apresentarVoltarRevisao() {
		return Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getCodigo()) &&
				getPlanoEnsinoVO().getSituacao().equals(SituacaoPlanoEnsinoEnum.AGUARDANDO_APROVACAO.getValor());
	}
	
	public boolean apresentarAutorizar() {
		return Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getCodigo()) &&
				(getPlanoEnsinoVO().getSituacao().equals(SituacaoPlanoEnsinoEnum.AGUARDANDO_APROVACAO.getValor()));
	}
	
	public boolean apresentarEmRevisao() {
		return Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getCodigo()) &&
				(getPlanoEnsinoVO().getSituacao().equals(SituacaoPlanoEnsinoEnum.AGUARDANDO_APROVACAO.getValor()) || 
						getPlanoEnsinoVO().getSituacao().equals(SituacaoPlanoEnsinoEnum.AUTORIZADO.getValor()));
	}
	
	public boolean desabilitarCampos() {
		return !getPlanoEnsinoVO().getSituacao().equals(SituacaoPlanoEnsinoEnum.AUTORIZADO.getValor()) 
				&& getUsuarioLogado().getIsApresentarVisaoCoordenador() ? true : getHabilitarControlePorCalendarioLancamentoPlanoEnsino();
	}

	public void liberarCalendarioLancamentoPlanoEnsino() throws ConsistirException {
		try {
			getFacadeFactory().getPlanoEnsinoFacade().validarCalendarioLancamentoPlanoEnsino(getPlanoEnsinoVO(), getUsuarioLogado().getIsApresentarVisaoProfessor());
			setHabilitarAbas(Boolean.TRUE);
			limparMensagem();
		} catch (ConsistirException e) {
			setHabilitarAbas(Boolean.FALSE);
			setConsistirExceptionMensagemDetalhada(MSG_TELA.msg_erro.name(), e, Uteis.ERRO);			
		}
	}

	public void subirConteudoPlanejado() {
		try {
			ConteudoPlanejamentoVO opc1 = (ConteudoPlanejamentoVO) context().getExternalContext().getRequestMap().get("planejamento");
			if (opc1.getOrdem() > 0) {
				ConteudoPlanejamentoVO opc2 = getPlanoEnsinoVO().getConteudoPlanejamentoVOs().get(opc1.getOrdem() - 2);
				getFacadeFactory().getPlanoEnsinoFacade().alterarOrdenacaoConteudoPlanejamentoVO(getPlanoEnsinoVO(), opc1, opc2);
			}
			limparMensagem();

		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void descerConteudoPlanejado() {
		try {
			ConteudoPlanejamentoVO opc1 = (ConteudoPlanejamentoVO) context().getExternalContext().getRequestMap().get("planejamento");
			if (getPlanoEnsinoVO().getConteudoPlanejamentoVOs().size() >= opc1.getOrdem()) {
				ConteudoPlanejamentoVO opc2 = getPlanoEnsinoVO().getConteudoPlanejamentoVOs().get(opc1.getOrdem());
				getFacadeFactory().getPlanoEnsinoFacade().alterarOrdenacaoConteudoPlanejamentoVO(getPlanoEnsinoVO(), opc1, opc2);
			}
			limparMensagem();

		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparReferenciaBibliograficaVO() {
		getReferenciaBibliograficaVO().setCatalogo(null);
		getReferenciaBibliograficaVO().setTipoReferencia(null);
		getReferenciaBibliograficaVO().setCatalogo(new CatalogoVO());
	}

	public void adicionarReferenciaBibliografiaVOs() throws Exception {
		try {
			getFacadeFactory().getPlanoEnsinoFacade().adicionarReferenciaBibliografiaVOs(getPlanoEnsinoVO(), getReferenciaBibliograficaVO());
			setReferenciaBibliograficaVO(new ReferenciaBibliograficaVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerReferenciaBibliografiaVOs() {
		try {
			ReferenciaBibliograficaVO obj = (ReferenciaBibliograficaVO) getRequestMap().get("referenciaBibliograficaItens");
			getFacadeFactory().getPlanoEnsinoFacade().removerReferenciaBibliografiaVOs(getPlanoEnsinoVO(), obj);
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void editarReferenciaBibliografiaVOs() {
		try {
			ReferenciaBibliograficaVO obj = (ReferenciaBibliograficaVO) getRequestMap().get("referenciaBibliograficaItens");
			setReferenciaBibliograficaVO(obj);
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void novoPlanoEnsino() {
		setPlanoEnsinoVO(null);
		setConteudoPlanejamentoVO(null);
		setReferenciaBibliograficaVO(null);
		setPermitirAlterarUltimoPlanoEnsino(Boolean.TRUE);
		setTabActive("planoDisciplina");
		getPlanoEnsinoVO().setDisciplina(getDisciplinaVO());
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			getPlanoEnsinoVO().setProfessorResponsavel(getUsuarioLogado().getPessoa());
			getPlanoEnsinoVO().getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
			getPlanoEnsinoVO().getResponsavel().setPessoa(getUsuarioLogado().getPessoa());
			setHabilitarAbas(Boolean.FALSE);
		}
		getPlanoEnsinoVO().setSituacao(SituacaoPlanoEnsinoEnum.PENDENTE.getValor());
		inicializarDisciplinaEditar();
		setHabilitarAbas(!getHabilitarControlePorCalendarioLancamentoPlanoEnsino());
	}

	public void persistirPlanoEnsino() {
		try {
			if (getHabilitarControlePorCalendarioLancamentoPlanoEnsino()) {
				getFacadeFactory().getPlanoEnsinoFacade().validarCalendarioLancamentoPlanoEnsino(getPlanoEnsinoVO(), getUsuarioLogado().getIsApresentarVisaoProfessor());				
			}
			executarValidacaoSimulacaoVisaoProfessor();
			executarValidacaoSimulacaoVisaoCoordenador();
			
			if (getLoginControle().getPermissaoAcessoMenuVO().getAlterarApenasUltimoPlanoEnsino() && !getLoginControle().getPermissaoAcessoMenuVO().getAlterarTodosPlanoEnsino()) {
				PlanoEnsinoVO ultimoPlanoEnsino;

				ultimoPlanoEnsino = getFacadeFactory().getPlanoEnsinoFacade().consultarPorUnidadeEnsinoCursoDisciplinaAnoSemestre(getPlanoEnsinoVO().getUnidadeEnsino().getCodigo(), getPlanoEnsinoVO().getCurso().getCodigo(), getPlanoEnsinoVO().getDisciplina().getCodigo(),null, null, null, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if(Uteis.isAtributoPreenchido(ultimoPlanoEnsino)){
					if(Integer.parseInt(ultimoPlanoEnsino.getAno()) > Integer.parseInt(getPlanoEnsinoVO().getAno())) {
						throw new Exception("Você não tem permissão para criar Plano de Ensino retroativo, pois só tem permissão para alterar o último Plano de Ensino. Portanto o Ano não pode ser menor que " + ultimoPlanoEnsino.getAno() + "!");
					}
					else if(Integer.parseInt(ultimoPlanoEnsino.getSemestre()) > Integer.parseInt(getPlanoEnsinoVO().getSemestre())) {
						throw new Exception("Você não tem permissão para criar Plano de Ensino retroativo, pois só tem permissão para alterar o último Plano de Ensino. Portanto o Semestre não pode ser que " + ultimoPlanoEnsino.getSemestre() + "º!");
					}						
				}
				
			}
						
			getFacadeFactory().getPlanoEnsinoFacade().persistir(getPlanoEnsinoVO(), getUsuarioLogado());
			if(Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getQuestionarioRespostaOrigemVO())) {
				Integer codigoQuestionario = 0;
				if(Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getCurso())) {
					codigoQuestionario = getPlanoEnsinoVO().getCurso().getQuestionarioVO().getCodigo();
				}else {
					codigoQuestionario = getConfiguracaoGeralPadraoSistema().getQuestionarioPlanoEnsino().getCodigo();
				}
				getPlanoEnsinoVO().getQuestionarioRespostaOrigemVO().setPerguntaRespostaOrigemVOs(getFacadeFactory().getPerguntaRespostaOrigemInterfaceFacade().consultarPorQuestionarioPlanoEnsino(codigoQuestionario, getPlanoEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
				 
				for (PerguntaRespostaOrigemVO perguntaRespostaOrigemPrincipalVO : getPlanoEnsinoVO().getQuestionarioRespostaOrigemVO().getPerguntaRespostaOrigemVOs()) {
					getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().editarListaPerguntaItemRespostaOrigemAdicionadasVO(perguntaRespostaOrigemPrincipalVO, perguntaRespostaOrigemPrincipalVO.getPerguntaItemRespostaOrigemVOs(), getUsuarioLogado());
				}
			}
			setPermitirAlterarUltimoPlanoEnsino(Boolean.FALSE);
			verificarPermissaoAlteracaoPlanoEnsino();
			setHabilitarAbas(Boolean.TRUE);
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada(MSG_TELA.msg_erro.name(), e, Uteis.ERRO);
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void clonarPlanoEnsino() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			getFacadeFactory().getPlanoEnsinoFacade().realizarClonagem(getPlanoEnsinoVO(), getUsuarioLogado().getIsApresentarVisaoProfessor(), getUsuarioLogado().getIsApresentarVisaoCoordenador(), getUsuarioLogado());
			setPermitirAlterarUltimoPlanoEnsino(Boolean.TRUE);
			inicializarDisciplinaEditar();
			setPermitirVisualizarBotoes(true);
			setHabilitarAbas(!getHabilitarControlePorCalendarioLancamentoPlanoEnsino());
			setTabActive("planoDisciplina");
			setMensagemID("msg_dados_clonados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public Boolean getDisciplinaEditar() {
		return disciplinaEditar;
	}

	public void setDisciplinaEditar(Boolean disciplinaEditar) {
		this.disciplinaEditar = disciplinaEditar;
	}

	public Boolean getDisciplinaConsultar() {
		return disciplinaConsultar;
	}

	public void setDisciplinaConsultar(Boolean disciplinaConsultar) {
		this.disciplinaConsultar = disciplinaConsultar;
	}

	public String inicializarMenuDisciplina() {
		inicializarDisciplinaConsultar();
		setMensagemDetalhada(Constantes.EMPTY, Constantes.EMPTY);
		return Uteis.getCaminhoRedirecionamentoNavegacao("disciplinaProfessor.xhtml");
	}

	public String inicializarMenuDisciplinaVisaoCoordenador() {
		inicializarDisciplinaConsultar();
		setMensagemDetalhada(Constantes.EMPTY, Constantes.EMPTY);
		return Uteis.getCaminhoRedirecionamentoNavegacao("disciplinaCoordenador.xhtml");
	}

	public void inicializarDisciplinaConsultar() {
		setDisciplinaEditar(Boolean.FALSE);
		setDisciplinaConsultar(Boolean.TRUE);
		getControleConsulta().setCampoConsulta("nome");
		consultarVisaoProfessor();
		consultarVisaoCoordenador();
	}

	public void inicializarDisciplinaEditar() {
		setDisciplinaEditar(Boolean.TRUE);
		setDisciplinaConsultar(Boolean.FALSE);
	}

	public List<SelectItem> getTipoConsultaComboCatalogo() {
		if (tipoConsultaComboCatalogo == null) {
			tipoConsultaComboCatalogo = new ArrayList<SelectItem>(0);
			tipoConsultaComboCatalogo.add(new SelectItem("tituloTitulo", "Título"));
			tipoConsultaComboCatalogo.add(new SelectItem("nomeEditora", "Editora"));
			tipoConsultaComboCatalogo.add(new SelectItem("autor", "Autor"));
			tipoConsultaComboCatalogo.add(new SelectItem("tombo", "Tombo"));
			tipoConsultaComboCatalogo.add(new SelectItem("assunto", "Assunto"));
			tipoConsultaComboCatalogo.add(new SelectItem("classificacao", "Classificação"));
			tipoConsultaComboCatalogo.add(new SelectItem("tipoCatalogo", "Tipo Catálogo"));
			tipoConsultaComboCatalogo.add(new SelectItem("cutterPha", "Cutter/PHA"));
		}
		return tipoConsultaComboCatalogo;
	}

	public void consultarCatalogo() {
		try {
			super.consultar();
			List<CatalogoVO> objs = new ArrayList<CatalogoVO>(0);
			getControleConsultaOtimizado().setLimitePorPagina(7);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals(Constantes.EMPTY)) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorCodigo(new Integer(valorInt), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorCodigoCatalogo(new Integer(valorInt), false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("tituloTitulo")) {
				objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorTituloCatalogo(getControleConsulta().getValorConsulta(), getControleConsulta().getOrdenarPor(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorTituloCatalogo(getControleConsulta().getValorConsulta(), false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeEditora")) {
				objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorNomeEditora(getControleConsulta().getValorConsulta(), getControleConsulta().getOrdenarPor(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorNomeEditora(getControleConsulta().getValorConsulta(), false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("autor")) {
				objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorNomeAutor(getControleConsulta().getValorConsulta(), getControleConsulta().getOrdenarPor(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorNomeAutor(getControleConsulta().getValorConsulta(), false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("tombo")) {
				if (getControleConsulta().getValorConsulta().equals(Constantes.EMPTY)) {
					getControleConsulta().setValorConsulta("0");
				}
				objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorTombo(getControleConsulta().getValorConsulta(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorTombo(getControleConsulta().getValorConsulta(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("assunto")) {
				objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorAssunto(getControleConsulta().getValorConsulta(), getControleConsulta().getOrdenarPor(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorAssunto(getControleConsulta().getValorConsulta(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("classificacao")) {
				objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorClassificacao(getControleConsulta().getValorConsulta(), getControleConsulta().getOrdenarPor(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorClassificacao(getControleConsulta().getValorConsulta(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("tipoCatalogo")) {
				objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorTipoCatalogo(getControleConsulta().getValorConsulta(), getControleConsulta().getOrdenarPor(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorTipoCatalogo(getControleConsulta().getValorConsulta(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("cutterPha")) {
				objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorCutterPha(getControleConsulta().getValorConsulta(), getControleConsulta().getOrdenarPor(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorCutterPha(getControleConsulta().getValorConsulta(), false, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			}
			getControleConsultaOtimizado().setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarCatalogo() {
		try {
			CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("catalogoItens");
			verificaPublicacaoExemplar(obj);
			getReferenciaBibliograficaVO().setCodigo(obj.getCodigo());
			getReferenciaBibliograficaVO().setCatalogo(getFacadeFactory().getCatalogoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			setControleConsultaOtimizado(new DataModelo());
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemSemestre() {
		List<SelectItem> listaSelectItemSemestre = new ArrayList<SelectItem>(0);
		listaSelectItemSemestre = new ArrayList<SelectItem>(0);
		listaSelectItemSemestre.add(new SelectItem(" ", Constantes.EMPTY));
		listaSelectItemSemestre.add(new SelectItem("1", "1º"));
		listaSelectItemSemestre.add(new SelectItem("2", "2º"));
		return listaSelectItemSemestre;
	}

	public String getAno() {
		if (ano == null) {
			ano = Constantes.EMPTY;
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = Constantes.EMPTY;
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public boolean getIsBloquearSemestreAno() throws Exception {
		if (getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
			return !getLoginControle().getPermissaoAcessoMenuVO().getPermitirConsultarPlanoEnsinoAnterior();
		} else {
			return false;
		}
	}

	
	public List<SelectItem> listaSelectItemSituacaoPlanoEnsino;
	public List<SelectItem> getListaSelectItemSituacaoPlanoEnsino() {
		if(listaSelectItemSituacaoPlanoEnsino == null){
			listaSelectItemSituacaoPlanoEnsino = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoPlanoEnsino.add(new SelectItem(SituacaoPlanoEnsinoEnum.PENDENTE.getValor(), SituacaoPlanoEnsinoEnum.PENDENTE.getDescricao()));
			listaSelectItemSituacaoPlanoEnsino.add(new SelectItem(SituacaoPlanoEnsinoEnum.AUTORIZADO.getValor(), SituacaoPlanoEnsinoEnum.AUTORIZADO.getDescricao()));
			listaSelectItemSituacaoPlanoEnsino.add(new SelectItem(SituacaoPlanoEnsinoEnum.AGUARDANDO_APROVACAO.getValor(), SituacaoPlanoEnsinoEnum.AGUARDANDO_APROVACAO.getDescricao()));
			listaSelectItemSituacaoPlanoEnsino.add(new SelectItem(SituacaoPlanoEnsinoEnum.EM_REVISAO.getValor(), SituacaoPlanoEnsinoEnum.EM_REVISAO.getDescricao()));
		}
		return listaSelectItemSituacaoPlanoEnsino;
	}

	public boolean getIsPermitirEditarSituacao() {
		return getLoginControle().getPermissaoAcessoMenuVO().getAutorizarPublicarPlanoEnsinoVisaoProfessorCoordenador();
	}

	public boolean getIsApresentarBotaoEnviarMensagemProfessor() {
		return getIsPermitirEditarSituacao() && !getPlanoEnsinoVO().getNovoObj();
	}

	/**
	 * Médodo responsável por preencher os dados básicos de acordo com o
	 * professor responsável por criar o plano de ensino, e passa via sessão os
	 * dados para o controlador ComunicacaoInternaControle
	 * 
	 * @return
	 */
	public String novaMensagemCoordenador() {
		try {
			ComunicacaoInternaVO obj = new ComunicacaoInternaVO();
			PessoaVO professor = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(getPlanoEnsinoVO().getResponsavel().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()).getPessoa();
			obj.setTipoDestinatario("PR");
			obj.setProfessor(professor);
			obj.setProfessorNome(professor.getNome());
			obj.setEnviarEmail(true);
			obj.setAssunto("Plano da Disciplina " + getPlanoEnsinoVO().getDisciplina().getNome());
			ComunicadoInternoDestinatarioVO destinatario = new ComunicadoInternoDestinatarioVO();
			destinatario.setDestinatario(professor);
			destinatario.setTipoComunicadoInterno(obj.getTipoComunicadoInterno());
			obj.adicionarObjComunicadoInternoDestinatarioVOs(destinatario);
			destinatario = new ComunicadoInternoDestinatarioVO();
			context().getExternalContext().getSessionMap().put("comunicacaoInternaVO", obj);
			return Uteis.getCaminhoRedirecionamentoNavegacao("recadosCoordenador");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Constantes.EMPTY;
		}
	}

	/**
	 * 
	 * @return
	 */
	public Boolean getIsDesativarComboBoxNivelEducacional() {
		try {
			return getFacadeFactory().getDisciplinaFacade().verificarDisciplinaIncluidaGradeDisciplinaGradeCompostaGradeCurricularGrupoOptativaDisciplinaEquivalente(getDisciplinaVO().getCodigo(), getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void scrollerListenerConsultaCatalogo(DataScrollEvent dataScrollerEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		consultarCatalogo();
	}
	
	public List<SelectItem> getTipoOrdenarPorCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("edicao", "Edição"));
		itens.add(new SelectItem("titulo", "Título"));
		itens.add(new SelectItem("ano", "Ano Publicação"));
		itens.add(new SelectItem("crescente", "Ordem Crescente"));
		itens.add(new SelectItem("decrescente", "Ordem Decrescente"));
		return itens;
	}
		
	public void realizarImpressaoPlanoEnsino() {
		List<PlanoDisciplinaRelVO> planoEnsinoVOs = null;
		try {
			if (getPlanoEnsinoVO().isNovoObj()) {
				throw new Exception("Só é possível emitir o PLANO DE ENSINO após ter sido gravado.");
			}
			if(getGerarPlanoEnsinoAssinado() && getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) {
				DataModelo listaPlanoEnsino =  new DataModelo();
				getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentoAssinadoPlanoEnsino(listaPlanoEnsino, planoEnsinoVO, disciplinaVO, TipoOrigemDocumentoAssinadoEnum.PLANO_DE_ENSINO, SituacaoDocumentoAssinadoPessoaEnum.ASSINADO, getVisaoAlunoControle().getMatricula().getGradeCurricularAtual(), getVisaoAlunoControle().getUsuario(), 1, 0);
				if(!listaPlanoEnsino.getListaConsulta().isEmpty()) {
					DocumentoAssinadoVO obj = (DocumentoAssinadoVO)listaPlanoEnsino.getListaConsulta().get(0);
					setCaminhoRelatorio(getFacadeFactory().getArquivoHelper().disponibilizarArquivoAssinadoParaDowload(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + obj.getArquivo().getPastaBaseArquivo() + File.separator + obj.getArquivo().getNome(), obj.getArquivo().getNome()));											
					setFazerDownload(true);
//					return;
				}
				
			}
			planoEnsinoVOs = getFacadeFactory().getPlanoDisciplinaRelFacade().realizarGeracaoRelatorioPlanoEnsino(getPlanoEnsinoVO(), getUsuarioLogado());
			String caminho = PlanoDisciplinaRel.getCaminhoBaseRelatorio();
			String design = Constantes.EMPTY;
			if(Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getQuestionarioRespostaOrigemVO())) {
				design = PlanoDisciplinaRel.getDesignIReportRelatorio("PlanoDisciplinaFormularioQuestionarioRel");
			}else {
				design = PlanoDisciplinaRel.getDesignIReportRelatorio();
			}
			getSuperParametroRelVO().setUnidadeEnsino(getPlanoEnsinoVO().getUnidadeEnsino().getNome());
			getSuperParametroRelVO().setGradeCurricular(getPlanoEnsinoVO().getGradeCurricular().getNome());
			getSuperParametroRelVO().setCurso(getPlanoEnsinoVO().getCurso().getNome());
			getSuperParametroRelVO().setTurma(Constantes.EMPTY);
			getSuperParametroRelVO().setDisciplina(getPlanoEnsinoVO().getDisciplina().getNome());

			getSuperParametroRelVO().setNomeDesignIreport(design);
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(caminho);
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio("Plano de Ensino");
			getSuperParametroRelVO().setListaObjetos(planoEnsinoVOs);
			getSuperParametroRelVO().setCaminhoBaseRelatorio(caminho);
			getSuperParametroRelVO().setNomeEmpresa(Constantes.EMPTY);
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setFiltros(Constantes.EMPTY);
			realizarImpressaoRelatorio();
			setMensagemID("msg_relatorio_ok", Uteis.SUCESSO);
			if (getGerarPlanoEnsinoAssinado()  && getPlanoEnsinoVO().getSituacao().equals(SituacaoPlanoEnsinoEnum.AUTORIZADO.getValor())) {
				setCaminhoRelatorio(getFacadeFactory().getDocumentoAssinadoFacade().realizarInclusaoDocumentoAssinadoPorPlanoDeEnsino(getCaminhoRelatorio(), getPlanoEnsinoVO(), getPlanoEnsinoVO().getAno(), getPlanoEnsinoVO().getSemestre(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
				getListaDocumentoAsssinados().clear();
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}

	}
	
	public List<SelectItem> listaSelectItemGradeCurricular;
	public List<SelectItem> getListaSelectItemGradeCurricular() {
		if (listaSelectItemGradeCurricular == null) {
			listaSelectItemGradeCurricular = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemGradeCurricular;
	}

	public void setListaSelectItemGradeCurricular(List<SelectItem> listaSelectItemGradeCurricular) {
		this.listaSelectItemGradeCurricular = listaSelectItemGradeCurricular;
	}

	public void inicializarImpressaoPlanoEnsino() {
		try {
			List<GradeCurricularVO> gradeCurricularVOs = getFacadeFactory().getGradeCurricularFacade().consultarPorCodigoCursoCodigoDisciplina(getPlanoEnsinoVO().getCurso().getCodigo(), getPlanoEnsinoVO().getDisciplina().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getListaSelectItemGradeCurricular().clear();
			for (GradeCurricularVO gradeCurricularVO : gradeCurricularVOs) {
				getListaSelectItemGradeCurricular().add(new SelectItem(gradeCurricularVO.getCodigo(), gradeCurricularVO.getNome()));
			}
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarImpressaoPlanoEnsinoAluno(){
		getPlanoEnsinoVO().getGradeCurricular().setCodigo(getVisaoAlunoControle().getMatricula().getGradeCurricularAtual().getCodigo());
		realizarImpressaoPlanoEnsino();
	}


	public HistoricoVO getHistoricoVO() {
		if(historicoVO == null){
			historicoVO = new HistoricoVO();
		}
		return historicoVO;
	}

	public void setHistoricoVO(HistoricoVO historicoVO) {
		this.historicoVO = historicoVO;
	}


	public SituacaoPlanoEnsinoEnum getSituacaoPlanoEnsino() {
		if(situacaoPlanoEnsino == null){
			situacaoPlanoEnsino = SituacaoPlanoEnsinoEnum.TODOS;
		}
		return situacaoPlanoEnsino;
	}

	public void setSituacaoPlanoEnsino(SituacaoPlanoEnsinoEnum situacaoPlanoEnsino) {
		this.situacaoPlanoEnsino = situacaoPlanoEnsino;
	}

	public void setListaSelectItemSituacaoPlanoEnsinoCons(List<SelectItem> listaSelectItemSituacaoPlanoEnsino) {
		this.listaSelectItemSituacaoPlanoEnsinoCons = listaSelectItemSituacaoPlanoEnsino;
	}
	
	public String abrirModalRevisao() {
		if (getAbrirModalRevisao()) {
			return "RichFaces.$('panelVoltarRevisao').show();";			
		} else {
			return "RichFaces.$('panelVoltarRevisao').hide();";
		}
	}

	public List<SelectItem> getListaSelectItemSituacaoPlanoEnsinoCons() {
		if(listaSelectItemSituacaoPlanoEnsinoCons == null){
			listaSelectItemSituacaoPlanoEnsinoCons = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoPlanoEnsinoCons.add(new SelectItem(SituacaoPlanoEnsinoEnum.TODOS, SituacaoPlanoEnsinoEnum.TODOS.getDescricao()));
			listaSelectItemSituacaoPlanoEnsinoCons.add(new SelectItem(SituacaoPlanoEnsinoEnum.NAO_CADASTRADO, SituacaoPlanoEnsinoEnum.NAO_CADASTRADO.getDescricao()));
			listaSelectItemSituacaoPlanoEnsinoCons.add(new SelectItem(SituacaoPlanoEnsinoEnum.PENDENTE, SituacaoPlanoEnsinoEnum.PENDENTE.getDescricao()));
			listaSelectItemSituacaoPlanoEnsinoCons.add(new SelectItem(SituacaoPlanoEnsinoEnum.AUTORIZADO, SituacaoPlanoEnsinoEnum.AUTORIZADO.getDescricao()));
			listaSelectItemSituacaoPlanoEnsinoCons.add(new SelectItem(SituacaoPlanoEnsinoEnum.EM_REVISAO, SituacaoPlanoEnsinoEnum.EM_REVISAO.getDescricao()));
			listaSelectItemSituacaoPlanoEnsinoCons.add(new SelectItem(SituacaoPlanoEnsinoEnum.AGUARDANDO_APROVACAO, SituacaoPlanoEnsinoEnum.AGUARDANDO_APROVACAO.getDescricao()));
			
		}
		return listaSelectItemSituacaoPlanoEnsinoCons;
	}

	public List<PlanoEnsinoVO> getPlanoEnsinoAnteriorVOs() {
		if(planoEnsinoAnteriorVOs == null){
			planoEnsinoAnteriorVOs =   new ArrayList<PlanoEnsinoVO>(0);
		}
		return planoEnsinoAnteriorVOs;
	}

	public void setPlanoEnsinoAnteriorVOs(List<PlanoEnsinoVO> planoEnsinoAnteriorVOs) {
		this.planoEnsinoAnteriorVOs = planoEnsinoAnteriorVOs;
	}
	

	public List<SelectItem> tipoConsultaComboPlanoEnsino;
	public List<SelectItem> getTipoConsultaComboPlanoEnsino() {
		if(tipoConsultaComboPlanoEnsino == null){
			tipoConsultaComboPlanoEnsino = new ArrayList<>(0);
			tipoConsultaComboPlanoEnsino.add(new SelectItem("nome", "Disciplina"));
			tipoConsultaComboPlanoEnsino.add(new SelectItem("codigo", "Código Disciplina"));
			tipoConsultaComboPlanoEnsino.add(new SelectItem("curso", "Curso"));
			tipoConsultaComboPlanoEnsino.add(new SelectItem("turma", "Turma"));
		}
		return tipoConsultaComboPlanoEnsino;
	}

	public String excluirPlanoEnsino(){
		try{
			executarValidacaoSimulacaoVisaoProfessor();
			getFacadeFactory().getPlanoEnsinoFacade().excluir(getPlanoEnsinoVO(), getUsuarioLogado());		
			setPlanoEnsinoVO(new PlanoEnsinoVO());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
		return Constantes.EMPTY;
	}

	public void adicionarPlanoEnsinoHorarioAulaVO(){
		try{
			getFacadeFactory().getPlanoEnsinoFacade().adicionarPlanoEnsinoHorarioAula(getPlanoEnsinoVO(), getPlanoEnsinoHorarioAulaVO());
			setPlanoEnsinoHorarioAulaVO(new PlanoEnsinoHorarioAulaVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void removerPlanoEnsinoHorarioAulaVO(){
		try{
			getFacadeFactory().getPlanoEnsinoFacade().removerPlanoEnsinoHorarioAula(getPlanoEnsinoVO(), (PlanoEnsinoHorarioAulaVO) getRequestMap().get("planoEnsinoHorarioAulaItem"));			
			setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	public PlanoEnsinoHorarioAulaVO getPlanoEnsinoHorarioAulaVO() {
		if(planoEnsinoHorarioAulaVO == null){
			planoEnsinoHorarioAulaVO = new PlanoEnsinoHorarioAulaVO();
		}
		return planoEnsinoHorarioAulaVO;
	}

	public void setPlanoEnsinoHorarioAulaVO(PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO) {
		this.planoEnsinoHorarioAulaVO = planoEnsinoHorarioAulaVO;
	}

	public List<SelectItem> getListaSelectItemDiaSemana() {
		if(listaSelectItemDiaSemana == null){
			listaSelectItemDiaSemana =  new ArrayList<SelectItem>(0);
			listaSelectItemDiaSemana.add(new SelectItem(DiaSemana.DOMINGO, DiaSemana.DOMINGO.getDescricao()));
			listaSelectItemDiaSemana.add(new SelectItem(DiaSemana.SEGUNGA, DiaSemana.SEGUNGA.getDescricao()));
			listaSelectItemDiaSemana.add(new SelectItem(DiaSemana.TERCA, DiaSemana.TERCA.getDescricao()));
			listaSelectItemDiaSemana.add(new SelectItem(DiaSemana.QUARTA, DiaSemana.QUARTA.getDescricao()));
			listaSelectItemDiaSemana.add(new SelectItem(DiaSemana.QUINTA, DiaSemana.QUINTA.getDescricao()));
			listaSelectItemDiaSemana.add(new SelectItem(DiaSemana.SEXTA, DiaSemana.SEXTA.getDescricao()));
			listaSelectItemDiaSemana.add(new SelectItem(DiaSemana.SABADO, DiaSemana.SABADO.getDescricao()));
		}
		return listaSelectItemDiaSemana;
	}

	public void setListaSelectItemDiaSemana(List<SelectItem> listaSelectItemDiaSemana) {
		this.listaSelectItemDiaSemana = listaSelectItemDiaSemana;
	}
	

	public void consultarTurma() {
		try {
			if(!Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getUnidadeEnsino())){
				throw new ConsistirException(UteisJSF.internacionalizar("msg_PlanoEnsino_unidadeEnsino"));
			}
			if(!Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getCurso())){
				throw new ConsistirException(UteisJSF.internacionalizar("msg_PlanoEnsino_curso"));
			}			
			super.consultarTurma(getControleConsultaTurma().getValorConsulta(), getPlanoEnsinoVO().getCurso().getCodigo(),  getPlanoEnsinoVO().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {			
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void scrollerListenerTurma(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaTurma().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaTurma().setPage(DataScrollEvent.getPage());
        consultarTurma();
    }
	

	public List<SelectItem> tipoConsultaComboTurma;

	public List<SelectItem> getTipoConsultaComboTurma() {
		if(tipoConsultaComboTurma == null){
			tipoConsultaComboTurma = new ArrayList<>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
		}
		return tipoConsultaComboTurma;
	}
	
	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItem");
			if(getIndiceHorarioAula().equals(0)){
				getPlanoEnsinoHorarioAulaVO().setTurmaVO(obj.clone());
			}else if(!getPlanoEnsinoVO().getPlanoEnsinoHorarioAulaVOs().isEmpty() && getPlanoEnsinoVO().getPlanoEnsinoHorarioAulaVOs().size() >= getIndiceHorarioAula()){
					getPlanoEnsinoVO().getPlanoEnsinoHorarioAulaVOs().get(getIndiceHorarioAula()-1).setTurmaVO(obj.clone());				
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarTurmaPorChavePrimaria() {
		try {
			if(!Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getUnidadeEnsino())){
				throw new ConsistirException(UteisJSF.internacionalizar("msg_PlanoEnsino_unidadeEnsino"));
			}
			if(!Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getCurso())){
				throw new ConsistirException(UteisJSF.internacionalizar("msg_PlanoEnsino_curso"));
			}
			String campoConsulta = getPlanoEnsinoHorarioAulaVO().getTurmaVO().getIdentificadorTurma();
			if (campoConsulta != null && !campoConsulta.trim().equals(Constantes.EMPTY)) {
				getPlanoEnsinoHorarioAulaVO().setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnicoCursoTurno( campoConsulta, getPlanoEnsinoVO().getCurso().getCodigo(), 0, getPlanoEnsinoVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));				
				setMensagemID("msg_dados_consultados");
			}else{
				limparMensagem();
			}			
		} catch (Exception e) {
			getPlanoEnsinoHorarioAulaVO().setTurmaVO(new TurmaVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	public void consultarTurmaPorChavePrimariaListaHorario() {
		PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO =  (PlanoEnsinoHorarioAulaVO) getRequestMap().get("planoEnsinoHorarioAulaItem");
		try {
			if(!Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getUnidadeEnsino())){
				throw new ConsistirException(UteisJSF.internacionalizar("msg_PlanoEnsino_unidadeEnsino"));
			}
			if(!Uteis.isAtributoPreenchido(getPlanoEnsinoVO().getCurso())){
				throw new ConsistirException(UteisJSF.internacionalizar("msg_PlanoEnsino_curso"));
			}
			String campoConsulta = planoEnsinoHorarioAulaVO.getTurmaVO().getIdentificadorTurma();
			if (campoConsulta != null && !campoConsulta.trim().equals(Constantes.EMPTY)) {				
				planoEnsinoHorarioAulaVO.setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnicoCursoTurno( campoConsulta, getPlanoEnsinoVO().getCurso().getCodigo(), 0, getPlanoEnsinoVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));			
				setMensagemID("msg_dados_consultados");
			}else{
				planoEnsinoHorarioAulaVO.setTurmaVO(new TurmaVO());
			}			
		} catch (Exception e) {
			planoEnsinoHorarioAulaVO.setTurmaVO(new TurmaVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void limparDadosTurma(){
		getPlanoEnsinoHorarioAulaVO().setTurmaVO(new TurmaVO());
	}
	
	public void limparDadosTurmaListaHorario(){
		PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO =  (PlanoEnsinoHorarioAulaVO) getRequestMap().get("planoEnsinoHorarioAulaItem");
		planoEnsinoHorarioAulaVO.setTurmaVO(new TurmaVO());
	}
	
	private Integer indiceHorarioAula;

	public Integer getIndiceHorarioAula() {
		if(indiceHorarioAula == null){
			indiceHorarioAula = 0;
		}
		return indiceHorarioAula;
	}

	public void setIndiceHorarioAula(Integer indiceHorarioAula) {
		this.indiceHorarioAula = indiceHorarioAula;
	}

	public Boolean getPermitirVisualizarBotoes() {
		if (permitirVisualizarBotoes == null) {
			permitirVisualizarBotoes = true;
		}
		return permitirVisualizarBotoes;
	}

	public void setPermitirVisualizarBotoes(Boolean permitirVisualizarBotoes) {
		this.permitirVisualizarBotoes = permitirVisualizarBotoes;
	}
	
	private boolean permiteClonarVisualizarPlanoEnsinoOutrosProfessores() {
		try {
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidade("ClonarPlanoEnsinoProfessor", getUsuarioLogado());
			}
		} catch (Exception e) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	public String getValorConsultaProfessor() {
		if (valorConsultaProfessor == null) {
			valorConsultaProfessor = Constantes.EMPTY;
		}
		return valorConsultaProfessor;
	}

	public void setValorConsultaProfessor(String valorConsultaProfessor) {
		this.valorConsultaProfessor = valorConsultaProfessor;
	}

	public List<FuncionarioVO> getListaConsultaProfessor() {
		if (listaConsultaProfessor == null) {
			listaConsultaProfessor = new ArrayList<FuncionarioVO>(0);
		}
		return listaConsultaProfessor;
	}

	public void setListaConsultaProfessor(List<FuncionarioVO> listaConsultaProfessor) {
		this.listaConsultaProfessor = listaConsultaProfessor;
	}

	public String getCampoConsultaProfessor() {
		if (campoConsultaProfessor == null) {
			campoConsultaProfessor = Constantes.EMPTY;
		}
		return campoConsultaProfessor;
	}

	public void setCampoConsultaProfessor(String campoConsultaProfessor) {
		this.campoConsultaProfessor = campoConsultaProfessor;
	}
	
	public TurmaVO getTurmaVO() {
		if(turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}
	
	public CursoVO getCursoVO() {
		if(cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public List<SelectItem> getTipoConsultaComboProfessorBusca() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("cpf", "CPF"));
		return itens;
	}

	public void consultarProfessor() {
		try {
			super.consultar();
			List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
			if (getCampoConsultaProfessor().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaProfessor(), 
						TipoPessoa.PROFESSOR.getValor(), getPlanoEnsinoVO().getUnidadeEnsino().getCodigo(), 
						false, 0, getUsuarioLogado());
			}
			if (getCampoConsultaProfessor().equals("cpf")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaProfessor(), 
						TipoPessoa.PROFESSOR.getValor(), getPlanoEnsinoVO().getUnidadeEnsino().getCodigo(), 
						false, 0, getUsuarioLogado());
			}
			setListaConsultaProfessor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaProfessor(new ArrayList<FuncionarioVO>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void selecionarProfessor() {
		try {
			FuncionarioVO professorSelecionado = (FuncionarioVO) context().getExternalContext().getRequestMap().get("professorVOItens");
			if (Uteis.isAtributoPreenchido(professorSelecionado)) {
				getPlanoEnsinoVO().setProfessorResponsavel(professorSelecionado.getPessoa());
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			setListaConsultaProfessor(new ArrayList<FuncionarioVO>(0));
			setValorConsultaProfessor(Constantes.EMPTY);
		}
	}
	
	public boolean getHabilitarControlePorCalendarioLancamentoPlanoEnsino() {
    	try {
    		return ControleAcesso.verificarPermissaoFuncionalidadeUsuario("HabilitarControlePorCalendarioLancamentoPlanoEnsinoVisaoProfessor", getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return false;
		}
    }

	public void limparDadosProfessor() {
		getPlanoEnsinoVO().setProfessorResponsavel(new PessoaVO());
	}
	

	public List<SelectItem> getListaSelectItemTurno() {
		if (listaSelectItemTurno == null) {
			listaSelectItemTurno = new ArrayList<>();
		}
		return listaSelectItemTurno;
	}

	public void setListaSelectItemTurno(List<SelectItem> listaSelectItemTurno) {
		this.listaSelectItemTurno = listaSelectItemTurno;
	}

	public List<SelectItem> getListaSelectItemTurma() {
		if(listaSelectItemTurma == null) {
			try {
				listaSelectItemTurma = new ArrayList<SelectItem>();
				List<TurmaVO> turmas = new ArrayList<TurmaVO>();
				
				if(getUsuarioLogado().getIsApresentarVisaoProfessor()) {
					turmas = getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorCursoNivelDadosCombobox(
							getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), false, "AT", getUnidadeEnsinoLogado().getCodigo(), 0, getUsuarioLogado().getVisaoLogar().equals("professor"), false, false, true, null,false, null);
				} else if(getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
					turmas = getFacadeFactory().getTurmaFacade().consultaRapidaPorCoordenadorAnoSemestre(getUsuarioLogado().getPessoa().getCodigo(), false, false, true, false, getAno(), getSemestre(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				}
				
				listaSelectItemTurma.add(new SelectItem(Constantes.EMPTY, Constantes.EMPTY));
				
				for (TurmaVO turmaVO : turmas) {
					SelectItem selectItem = new SelectItem(turmaVO.getCodigo(), turmaVO.getIdentificadorTurma());
					listaSelectItemTurma.add(selectItem);
				}
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	public List<SelectItem> getListaSelectItemPeriodoLetivo() {
		if (listaSelectItemPeriodoLetivo == null) {
			listaSelectItemPeriodoLetivo = new ArrayList<SelectItem>(0);
			listaSelectItemPeriodoLetivo.add(new SelectItem(" ", " "));
			listaSelectItemPeriodoLetivo.add(new SelectItem("1", "1º"));
			listaSelectItemPeriodoLetivo.add(new SelectItem("2", "2º"));
			listaSelectItemPeriodoLetivo.add(new SelectItem("3", "3º"));
			listaSelectItemPeriodoLetivo.add(new SelectItem("4", "4º"));
			listaSelectItemPeriodoLetivo.add(new SelectItem("5", "5º"));
			listaSelectItemPeriodoLetivo.add(new SelectItem("6", "6º"));
			listaSelectItemPeriodoLetivo.add(new SelectItem("7", "7º"));
			listaSelectItemPeriodoLetivo.add(new SelectItem("8", "8º"));
			listaSelectItemPeriodoLetivo.add(new SelectItem("9", "9º"));
			listaSelectItemPeriodoLetivo.add(new SelectItem("10", "10º"));
			listaSelectItemPeriodoLetivo.add(new SelectItem("11", "11º"));
			listaSelectItemPeriodoLetivo.add(new SelectItem("12", "12º"));
		}
		
		return listaSelectItemPeriodoLetivo;
	}

	public void setListaSelectItemPeriodoLetivo(List<SelectItem> listaSelectItemPeriodoLetivo) {
		this.listaSelectItemPeriodoLetivo = listaSelectItemPeriodoLetivo;
	}

	public List<SelectItem> getListaSelectItemCurso() {
		if(listaSelectItemCurso == null) {
			try {
				listaSelectItemCurso = new ArrayList<SelectItem>();
				List<CursoVO> cursos = new ArrayList<CursoVO>();
				
				if(getUsuarioLogado().getIsApresentarVisaoProfessor()) {
					cursos = getFacadeFactory().getCursoFacade().consultarPorProfessor(Constantes.EMPTY, getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
							// consultarPorCodigoProfessor(getUsuarioLogado().getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				}else {
					cursos = getFacadeFactory().getCursoFacade().consultarListaCursoPorCodigoPessoaCoordenador(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
				}
						
				listaSelectItemCurso.add(new SelectItem(Constantes.EMPTY, Constantes.EMPTY));
				
				for (CursoVO cursoVO : cursos) {
					SelectItem selectItem = new SelectItem(cursoVO.getCodigo(), cursoVO.getNome());
					listaSelectItemCurso.add(selectItem);
				}
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
		
		return listaSelectItemCurso;
	}

	public void setListaSelectItemCurso(List<SelectItem> listaSelectItemCurso) {
		this.listaSelectItemCurso = listaSelectItemCurso;
	}
	
	public boolean getIsConsultaPorDisciplina() {
		return getControleConsulta().getCampoConsulta().equals("nome") || getControleConsulta().getCampoConsulta().equals("codigo");
	}
	
	public boolean getIsConsultaPorCurso() {
		return getControleConsulta().getCampoConsulta().equals("curso");
	}
	
	public boolean getIsConsultaPorTurma() {
		return getControleConsulta().getCampoConsulta().equals("turma");
	}
	
	public Boolean getHabilitarAbas() {
		if (habilitarAbas == null) {
			habilitarAbas = Boolean.FALSE;
		}
		return habilitarAbas;
	}

	public void setHabilitarAbas(Boolean habilitarAbas) {
		this.habilitarAbas = habilitarAbas;
	}
	
	public Boolean getAbrirModalRevisao() {
		if (abrirModalRevisao == null) {
			abrirModalRevisao = Boolean.FALSE;
		}
		return abrirModalRevisao;
	}

	public void setAbrirModalRevisao(Boolean abrirModalRevisao) {
		this.abrirModalRevisao = abrirModalRevisao;
	}
	
	public void adicionarListaPerguntaItemRespostaOrigemVO() {		
		super.adicionarListaPerguntaItemRespostaOrigemVO();
	}

	public List<String> getListaMensagemAlertaVinculosDisciplinaAlteracaoNome() {
		if (listaMensagemAlertaVinculosDisciplinaAlteracaoNome == null) {
			listaMensagemAlertaVinculosDisciplinaAlteracaoNome = new ArrayList<>();
		}
		return listaMensagemAlertaVinculosDisciplinaAlteracaoNome;
	}

	public void setListaMensagemAlertaVinculosDisciplinaAlteracaoNome(List<String> listaMensagemAlertaVinculosDisciplinaAlteracaoNome) {
		this.listaMensagemAlertaVinculosDisciplinaAlteracaoNome = listaMensagemAlertaVinculosDisciplinaAlteracaoNome;
	}

	public String getAbrirModalAlertaVinculosDisciplinaAlteracaoNome() {
		if (abrirModalAlertaVinculosDisciplinaAlteracaoNome == null) {
			abrirModalAlertaVinculosDisciplinaAlteracaoNome = Constantes.EMPTY;
		}
		return abrirModalAlertaVinculosDisciplinaAlteracaoNome;
	}

	public void setAbrirModalAlertaVinculosDisciplinaAlteracaoNome(String abrirModalAlertaVinculosDisciplinaAlteracaoNome) {
		this.abrirModalAlertaVinculosDisciplinaAlteracaoNome = abrirModalAlertaVinculosDisciplinaAlteracaoNome;
	}

	public void gravarValidandoAlteracaoNome() {
		try {
			setAbrirModalAlertaVinculosDisciplinaAlteracaoNome(Constantes.EMPTY);
			setListaMensagemAlertaVinculosDisciplinaAlteracaoNome(new ArrayList<>());
			if (Uteis.isAtributoPreenchido(getDisciplinaVO())) {
				Map<String, Integer> verificarVinculosDisciplinaAlteracaoNome = getFacadeFactory().getDisciplinaFacade().consultarVinculosDisciplinaAlteracaoNome(getDisciplinaVO().getCodigo(), getDisciplinaVO().getNome(), true);
				if (Uteis.isAtributoPreenchido(verificarVinculosDisciplinaAlteracaoNome)) {
					verificarVinculosDisciplinaAlteracaoNome.entrySet().stream().map(this::realizarMontagemDescricaoVinculosDisciplina).forEach(getListaMensagemAlertaVinculosDisciplinaAlteracaoNome()::add);
					setAbrirModalAlertaVinculosDisciplinaAlteracaoNome("RichFaces.$('panelAlertaVinculosDisciplina').show();");
				} else {
					gravar();
				}
			} else {
				gravar();
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private String realizarMontagemDescricaoVinculosDisciplina(Entry<String, Integer> entry) {
		return entry.getKey() + " - quantidade: " + entry.getValue() + ".";
	}
	
	public void fecharModalAlertaVinculosDisciplinaAlteracaoNome() {
		setAbrirModalAlertaVinculosDisciplinaAlteracaoNome("RichFaces.$('panelAlertaVinculosDisciplina').hide();");
	}

	public List<SelectItem> getListaSelectItemClassificacaoDisciplina() {
		if(listaSelectItemClassificacaoDisciplina == null) {
			listaSelectItemClassificacaoDisciplina =  UtilSelectItem.getListaSelectItemEnum(ClassificacaoDisciplinaEnum.values(), Obrigatorio.SIM);
		}
		return listaSelectItemClassificacaoDisciplina;
	}

	public void setListaSelectItemClassificacaoDisciplina(List<SelectItem> listaSelectItemClassificacaoDisciplina) {
		this.listaSelectItemClassificacaoDisciplina = listaSelectItemClassificacaoDisciplina;
	}

	public List<SelectItem> getListaSelectItemGrupoPessoa() {
		if(listaSelectItemGrupoPessoa == null) {
			listaSelectItemGrupoPessoa = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemGrupoPessoa;
	}

	public void setListaSelectItemGrupoPessoa(List<SelectItem> listaSelectItemGrupoPessoa) {
		this.listaSelectItemGrupoPessoa = listaSelectItemGrupoPessoa;
	}	

	public List<SelectItem> getListaSelectConteudoMasterBlackboard() {
		if (listaSelectConteudoMasterBlackboard == null) {
			listaSelectConteudoMasterBlackboard = new ArrayList<SelectItem>(0);
		}
		return listaSelectConteudoMasterBlackboard;
	}

	public void setListaSelectConteudoMasterBlackboard(List<SelectItem> listaSelectConteudoMasterBlackboard) {
		this.listaSelectConteudoMasterBlackboard = listaSelectConteudoMasterBlackboard;
	}

	public List<SelectItem> getListaSelectFonteDeDadosBlackboard() {
		if (listaSelectFonteDeDadosBlackboard == null) {
			listaSelectFonteDeDadosBlackboard = new ArrayList<SelectItem>(0);
		}
		return listaSelectFonteDeDadosBlackboard;
	}

	public void setListaSelectFonteDeDadosBlackboard(List<SelectItem> listaSelectFonteDeDadosBlackboard) {
		this.listaSelectFonteDeDadosBlackboard = listaSelectFonteDeDadosBlackboard;
	}
	
	public List<SelectItem> getListaSelectItemPeriodicidade() {
		if (listaSelectItemPeriodicidade == null) {
			listaSelectItemPeriodicidade = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemPeriodicidade;
	}

	public void setListaSelectItemPeriodicidade(List<SelectItem> listaSelectItemPeriodicidade) {
		this.listaSelectItemPeriodicidade = listaSelectItemPeriodicidade;
	}
	
	public void montarListaSelectItemPeriodicidade() {
		getListaSelectItemPeriodicidade().clear();
		getListaSelectItemPeriodicidade().add(new SelectItem("IN", "Integral"));	
		getListaSelectItemPeriodicidade().add(new SelectItem("SE", "Semestral"));		
		getListaSelectItemPeriodicidade().add(new SelectItem("AN", "Anual"));
		
	}
	
	
	public void atualizarTotalCargaHoraria() {
		try {
			getFacadeFactory().getPlanoEnsinoFacade().atualizarTotalCargaHoraria(getPlanoEnsinoVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public Boolean getGerarPlanoEnsinoAssinado() {
		if (gerarPlanoEnsinoAssinado == null) {
			try {
				gerarPlanoEnsinoAssinado = ControleAcesso.verificarPermissaoFuncionalidadeUsuario("AssinarDigitalmentePlanoEnsino", getUsuarioLogado());
			} catch (Exception e) {
				gerarPlanoEnsinoAssinado = false;
			}
		}
		return gerarPlanoEnsinoAssinado;
	}
	
	public void setGerarPlanoEnsinoAssinado(Boolean gerarPlanoEnsinoAssinado) {
		this.gerarPlanoEnsinoAssinado = gerarPlanoEnsinoAssinado;
	}
}

