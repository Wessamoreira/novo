package controle.avaliacaoinst;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas avaliacaoInstitucionalForm.jsp
 * avaliacaoInstitucionalCons.jsp) com as funcionalidades da classe <code>AvaliacaoInstitucional</code>. Implemtação da
 * camada controle (Backing Bean).
 *
 * @see SuperControle
 * @see AvaliacaoInstitucional
 * @see AvaliacaoInstitucionalVO
 */

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoCoordenadorCursoEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalCursoVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalPessoaAvaliadaVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalUnidadeEnsinoVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.enumeradores.TipoEscopoQuestionarioPerguntaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.PublicoAlvoAvaliacaoInstitucional;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
//import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import relatorio.negocio.comuns.avaliacaoInst.enumeradores.NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum;

import jakarta.faces.model.SelectItem;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Controller("AvaliacaoInstitucionalControle")
@Scope("viewScope")
@Lazy
public class AvaliacaoInstitucionalControle extends SuperControle implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -8698175116027979021L;
	private AvaliacaoInstitucionalVO avaliacaoInstitucionalVO;
	private AvaliacaoInstitucionalPessoaAvaliadaVO avaliacaoInstitucionalPessoaAvaliadaVO;
	protected List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemQuestionario;
	private List<SelectItem> listaSelectItemPublicoAlvoAvaliacaoInstitucional;
	private String valorConsultaCurso;
	private String campoConsultaCurso;
	private List<CursoVO> listaConsultaCurso;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	private String valorConsultaDisciplina;
	private String campoConsultaDisciplina;
	private List<DisciplinaVO> listaConsultaDisciplina;
	private List<SelectItem> listaNivelEducacional;
	private Boolean apresentarSemestre = Boolean.FALSE;
	private Boolean apresentarAno = Boolean.FALSE;
	private Boolean consultaDataScroller;
	private List<SelectItem> listaSelectItemDepartamento;
	private List<SelectItem> listaSelectItemCargo;
	private List<SelectItem> listaSelectItemCargoAvaliado;

	private List<PessoaVO> listaConsultaPessoa;
	private String campoConsultaPessoa;
	private String valorConsultaPessoa;
	private List<SelectItem> listaSelectItemNivelDetalhamento;

	private String valorConsultaUnidadeEnsino;
	private String campoConsultaUnidadeEnsino;
	private List<UnidadeEnsinoVO> listaConsultaUnidadeEnsino;


	public AvaliacaoInstitucionalControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsultaOtimizado(new DataModelo());
		montarListaSelectItemPublicAlvoAvaliacaoInstitucional();
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>AvaliacaoInstitucional</code> para edição pelo usuário da aplicação.
	 */
	public String novo() throws Exception {
		removerObjetoMemoria(this);
		setAvaliacaoInstitucionalVO(new AvaliacaoInstitucionalVO());
		inicializarListasSelectItemTodosComboBox();
		incializarResponsavel();
		getCursoVOs().clear();
		getUnidadeEnsinoVOs().clear();
		inicializarDadosUnidadeEnsino();
		verificarTodasUnidadeEnsinoSelecionados();
		getAvaliacaoInstitucionalVO().setSituacao("EC");
		getAvaliacaoInstitucionalVO().getDisciplina().setNome("Todas Disciplinas");
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoInstitucionalForm.xhtml");
	}

	public void incializarResponsavel() {
		try {
			getAvaliacaoInstitucionalVO().getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
			getAvaliacaoInstitucionalVO().getResponsavel().setNome(getUsuarioLogado().getNome());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>AvaliacaoInstitucional</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP correspondente
	 * possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			AvaliacaoInstitucionalVO obj = (AvaliacaoInstitucionalVO) context().getExternalContext().getRequestMap()
					.get("avaliacaoInstitucionalItens");
			obj.setNovoObj(Boolean.FALSE);
			getFacadeFactory().getAvaliacaoInstitucionalFacade().carregarDados(obj, getUsuarioLogado());
			setAvaliacaoInstitucionalVO(obj);
//			if (getAvaliacaoInstitucionalVO().getPublicoAlvo_Professor()) {
//				verificarPublicoAlvoProfessor();
//			}
			inicializarListasSelectItemTodosComboBox();
			inicializarListaNivelDetalhamento();
			incializarResponsavel();
			getCursoVOs().clear();
			getUnidadeEnsinoVOs().clear();
			consultarCursoFiltroRelatorio();
			consultarUnidadeEnsinoFiltroRelatorio();
			verificarTodasUnidadeEnsinoSelecionados();
			verificarTodosCursosSelecionados();
			montarAnoSemestre();
			if (getAvaliacaoInstitucionalVO().getDisciplina().getCodigo().equals(0)) {
				getAvaliacaoInstitucionalVO().getDisciplina().setNome("Todas Disciplinas");
			}
			setMensagemID("msg_dados_editar");
		} catch (Exception ex) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoInstitucionalForm.xhtml");
	}

	public void verificarPublicoAlvoProfessor() {
		if (!getAvaliacaoInstitucionalVO().getAvaliacaoInstitucionalCursoVOs().isEmpty()) {
			getAvaliacaoInstitucionalVO().setTipoFiltroProfessor("curso");
		} else if (!getAvaliacaoInstitucionalVO().getTurma().getCodigo().equals(0)) {
			getAvaliacaoInstitucionalVO().setTipoFiltroProfessor("turma");
		} else {
			getAvaliacaoInstitucionalVO().setTipoFiltroProfessor("todos");
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da
	 * classe <code>AvaliacaoInstitucional</code>. Caso o objeto seja novo (ainda
	 * não gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o usuário
	 * juntamente com uma mensagem de erro.
	 */
	public void gravar() {
		try {
			if (!getIsPublicoAlvoTodosCursoOuCursoOuTurmaSelecionado()) {
				getAvaliacaoInstitucionalVO().getListaAvaliacaoInstitucionalPessoaAvaliadaVOs().clear();
			}
			if (avaliacaoInstitucionalVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getAvaliacaoInstitucionalFacade().incluir(avaliacaoInstitucionalVO,
						getUsuarioLogado());
				setMensagemID("msg_dados_gravados");
			} else {
				Boolean avaliacaoRespondida = getFacadeFactory().getAvaliacaoInstitucionalFacade()
						.verificarPermissaoAlteracaoExclusao(getAvaliacaoInstitucionalVO().getCodigo(),
								getUsuarioLogado());
				if (avaliacaoRespondida) {
					getFacadeFactory().getAvaliacaoInstitucionalFacade()
							.alterarDataInicioDataFimAvaliacao(getAvaliacaoInstitucionalVO(), getUsuarioLogado());
					setMensagemID("msg_dados_gravadosAvalicaoRespondida");
				} else {
					getFacadeFactory().getAvaliacaoInstitucionalFacade().alterar(avaliacaoInstitucionalVO,
							getUsuarioLogado());
					setMensagemID("msg_dados_gravados");
				}
			}
		}catch (DuplicateKeyException e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), "Já existe uma Avaliação Institucional com o mesmo Nome.");
			setMensagemID("Já existe uma Avaliação Institucional com o mesmo Nome." , Uteis.ERRO );
		}
		catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * AvaliacaoInstitucionalCons.jsp. Define o tipo de consulta a ser executada,
	 * por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
	 * Como resultado, disponibiliza um List com os objetos selecionados na sessao
	 * da pagina.
	 */
	@Override
	public String consultar() {
		try {
			super.consultar();
			getControleConsulta().getListaConsulta().clear();
			List<AvaliacaoInstitucionalVO> objs = new ArrayList<AvaliacaoInstitucionalVO>(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getAvaliacaoInstitucionalFacade().consultarPorCodigo(new Integer(valorInt),
						null, null, null, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("data")) {
				// Date valorData =
				// Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getAvaliacaoInstitucionalFacade().consultarPorData(
						Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0),
						Uteis.getDateTime(getControleConsulta().getDataIni(), 23, 59, 59), null, null, null, true,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getAvaliacaoInstitucionalFacade().consultaRapidaPorNome(
						getControleConsulta().getValorConsulta(), null, null, null, true,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("situacao")) {
				objs = getFacadeFactory().getAvaliacaoInstitucionalFacade().consultaRapidaPorSituacao(
						getControleConsulta().getValorConsulta(), null, null, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("publicoAlvo")) {
				objs = getFacadeFactory().getAvaliacaoInstitucionalFacade().consultaRapidaPorPublicoAlvo(
						getControleConsulta().getValorConsulta(), null, null, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataInicio")) {
				// Date valorData =
				// Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getAvaliacaoInstitucionalFacade().consultaRapidaPorDataInicio(
						Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0),
						Uteis.getDateTime(getControleConsulta().getDataIni(), 23, 59, 59), null, null, true,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataFinal")) {
				// Date valorData =
				// Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getAvaliacaoInstitucionalFacade().consultaRapidaPorDataFinal(
						Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0),
						Uteis.getDateTime(getControleConsulta().getDataIni(), 23, 59, 59), null, null, true,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getAvaliacaoInstitucionalFacade().consultaRapidaPorUnidadeEnsino(
						getControleConsulta().getValorConsulta(), null, null, null, true,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("questionario")) {
				objs = getFacadeFactory().getAvaliacaoInstitucionalFacade().consultaRapidaPorQuestionario(
						getControleConsulta().getValorConsulta(), null, null, null, true,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			getControleConsultaOtimizado().setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoInstitucionalCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoInstitucionalCons.xhtml");
		}
	}

	public void scrollerListener( ) throws Exception {
//		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
//		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
//		setConsultaDataScroller(true);
//		consultar();
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>AvaliacaoInstitucionalVO</code> Após a exclusão ela automaticamente
	 * aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getAvaliacaoInstitucionalFacade().excluir(avaliacaoInstitucionalVO, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
			return "";
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public void montarListaSelectItemPublicAlvoAvaliacaoInstitucional() {
		List<SelectItem> obj = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(PublicoAlvoAvaliacaoInstitucional.class, false);
		setListaSelectItemPublicoAlvoAvaliacaoInstitucional(obj);
	}

	public void consultarCurso() {
		try {
			setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultar(getCampoConsultaCurso(),
					getValorConsultaCurso(), getAvaliacaoInstitucionalVO().getUnidadeEnsino().getCodigo(), true,
					Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(null);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void selecionarCurso() {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			getAvaliacaoInstitucionalVO().setCurso(obj);
			getAvaliacaoInstitucionalVO().setNomeCurso(obj.getNome());
			getListaConsultaCurso().clear();
			this.setValorConsultaCurso("");
			this.setCampoConsultaCurso("");
			limparDadosDisciplina();
		} catch (Exception ex) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
		}
	}

	public void limparDadosCurso() {
		try {
			getAvaliacaoInstitucionalVO().getAvaliacaoInstitucionalCursoVOs().clear();
			getAvaliacaoInstitucionalVO().setNomeCurso("");
			getCursoVOs().clear();
			limparDadosDisciplina();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			super.consultar();
			List<TurmaVO> objs = new ArrayList<>(0);
			List<UnidadeEnsinoVO> listaUnidadeEnsino = Optional.ofNullable(avaliacaoInstitucionalVO.getAvaliacaoInstitucionalUnidadeEnsinoVOs())
					.orElseGet(Collections::emptyList)
					.stream()
					.map(AvaliacaoInstitucionalUnidadeEnsinoVO::getUnidadeEnsinoVO)
					.filter(Objects::nonNull)
					.collect(Collectors.toList());

			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCursoPorUnidadeEnsino(
						getValorConsultaTurma(),  0 , listaUnidadeEnsino,false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		return itens;
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			getAvaliacaoInstitucionalVO().setTurma(obj);
			getAvaliacaoInstitucionalVO().setCurso(obj.getCurso());
			getAvaliacaoInstitucionalVO().getAvaliacaoInstitucionalCursoVOs().clear();
			getFacadeFactory().getAvaliacaoInstitucionalFacade()
					.adicionarAvaliacaoInstitucionalCurso(getAvaliacaoInstitucionalVO(), obj.getCurso());
			getAvaliacaoInstitucionalVO().setNomeCurso(obj.getCurso().getCodigo() + " - " + obj.getCurso().getNome());
			limparDadosDisciplina();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparDadosTurma() {
		try {
			getListaConsultaTurma().clear();
			setValorConsultaTurma(null);
			setCampoConsultaTurma(null);
			getAvaliacaoInstitucionalVO().setTurma(null);
			getAvaliacaoInstitucionalVO().setCurso(null);
			limparDadosDisciplina();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarDisciplina() {
		try {
 			List<DisciplinaVO> objs = new ArrayList<DisciplinaVO>(0);
			List<CursoVO> listaCursos = Optional.ofNullable(avaliacaoInstitucionalVO.getAvaliacaoInstitucionalCursoVOs())
					.orElseGet(Collections::emptyList)
					.stream()
					.map(AvaliacaoInstitucionalCursoVO::getCursoVO)
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
			if (getCampoConsultaDisciplina().equals("codigo")) {
				if (getValorConsultaDisciplina().equals("")) {
					setValorConsultaDisciplina("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigoCursoTurma(new Integer(valorInt), 0,
						getAvaliacaoInstitucionalVO().getTurma().getCodigo(),
						getAvaliacaoInstitucionalVO().getUnidadeEnsino().getCodigo(), false,
						Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorListaCursoTurma(getValorConsultaDisciplina(),
						0, getAvaliacaoInstitucionalVO().getTurma().getCodigo(),
						0, listaCursos, false,
						Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}

			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<>());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarPublicoAlvo() {
		if (getAvaliacaoInstitucionalVO().getAvaliacaoUltimoModulo()) {
			List<SelectItem> itens = new ArrayList<SelectItem>(0);
			itens.add(new SelectItem(PublicoAlvoAvaliacaoInstitucional.TODOS_CURSOS.getValor(),
					PublicoAlvoAvaliacaoInstitucional.TODOS_CURSOS.getDescricao()));
			itens.add(new SelectItem(PublicoAlvoAvaliacaoInstitucional.CURSO.getValor(),
					PublicoAlvoAvaliacaoInstitucional.CURSO.getDescricao()));
			itens.add(new SelectItem(PublicoAlvoAvaliacaoInstitucional.TURMA.getValor(),
					PublicoAlvoAvaliacaoInstitucional.TURMA.getDescricao()));
			itens.add(new SelectItem(PublicoAlvoAvaliacaoInstitucional.PROFESSORES.getValor(),
					PublicoAlvoAvaliacaoInstitucional.PROFESSORES.getDescricao()));
			setListaSelectItemPublicoAlvoAvaliacaoInstitucional(itens);
			montarListaSelectItemQuestionarioUnidadeEnsino();
		} else {
			montarListaSelectItemPublicAlvoAvaliacaoInstitucional();
			montarListaSelectItemQuestionarioUnidadeEnsino();
		}
	}

	public void montarAnoSemestre() {
		if (getAvaliacaoInstitucionalVO().getPublicoAlvo_CargoCoordenador()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_DepartamentoCoordenador()) {
			getAvaliacaoInstitucionalVO().setAno("");
			getAvaliacaoInstitucionalVO().setSemestre("");
			setApresentarAno(Boolean.FALSE);
			setApresentarSemestre(Boolean.FALSE);
		} else if (getAvaliacaoInstitucionalVO().getNivelEducacional().equals(TipoNivelEducacional.SUPERIOR.getValor())
				|| getAvaliacaoInstitucionalVO().getNivelEducacional()
				.equals(TipoNivelEducacional.GRADUACAO_TECNOLOGICA.getValor())) {
			if (getAvaliacaoInstitucionalVO().getCodigo().equals(0)) {
				getAvaliacaoInstitucionalVO().setAno(Uteis.getAnoDataAtual());
				getAvaliacaoInstitucionalVO().setSemestre(Uteis.getSemestreAtual());
			}
			setApresentarAno(Boolean.TRUE);
			setApresentarSemestre(Boolean.TRUE);
		} else if (getAvaliacaoInstitucionalVO().getNivelEducacional().equals(TipoNivelEducacional.BASICO.getValor())
				|| getAvaliacaoInstitucionalVO().getNivelEducacional().equals(TipoNivelEducacional.INFANTIL.getValor())
				|| getAvaliacaoInstitucionalVO().getNivelEducacional().equals(TipoNivelEducacional.MEDIO.getValor())) {
			if (getAvaliacaoInstitucionalVO().getCodigo().equals(0)) {
				getAvaliacaoInstitucionalVO().setAno(Uteis.getAnoDataAtual());
				getAvaliacaoInstitucionalVO().setSemestre("");
			}
			setApresentarAno(Boolean.TRUE);
		} else {
			setApresentarAno(Boolean.FALSE);
			setApresentarSemestre(Boolean.FALSE);
			if (getAvaliacaoInstitucionalVO().getCodigo().equals(0)) {
				getAvaliacaoInstitucionalVO().setAno("");
				getAvaliacaoInstitucionalVO().setSemestre("");
			}
		}
		if (!getAvaliacaoInstitucionalVO().getNivelEducacional()
				.equals(TipoNivelEducacional.POS_GRADUACAO.getValor())) {
			getAvaliacaoInstitucionalVO().setAvaliacaoUltimoModulo(false);
		}
		if (!getAvaliacaoInstitucionalVO().getAvaliacaoInstitucionalCursoVOs().isEmpty()) {
			consultarCursoFiltroRelatorio();
		}
	}

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		// itens.add(new SelectItem("areaConhecimento",
		// "Área de Conhecimento"));
		return itens;
	}

	public String validarTamanhoColunaNivelEducacional() {
		if (getApresentarSemestre() || getApresentarAno() || getAvaliacaoInstitucionalVO().getIsApresentarOpcaoUltimoModulo()) {
			return "col-md-6";
		}
		return "col-md-12";
	}

	public String validarTamanhoColunaPorNivelEducacional() {
		if (getApresentarSemestre() && getApresentarAno() && getAvaliacaoInstitucionalVO().getIsApresentarOpcaoUltimoModulo()) {
			return "col-md-2";
		} else if ( (getApresentarSemestre() && getApresentarAno()) ||
				(getAvaliacaoInstitucionalVO().getIsApresentarOpcaoUltimoModulo()) && getApresentarAno() ||
				(getAvaliacaoInstitucionalVO().getIsApresentarOpcaoUltimoModulo()) && getApresentarSemestre()) {
			return "col-md-3";
		}
		return "col-md-6";
	}

	public void selecionarDisciplina() {
		try {
			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
			if(avaliacaoInstitucionalVO.getAvaliacaoInstitucionalCursoVOs().isEmpty()){
				getCursoVOs().clear();
				getCursoVOs().addAll(getFacadeFactory().getCursoFacade().consultarCursoPorDisciplina(obj.getCodigo(),false,
						Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				setMarcarTodosCursos(true);
				realizarMarcacaoCursos();
			}
			getAvaliacaoInstitucionalVO().setDisciplina(obj);
			getListaConsultaDisciplina().clear();
			setValorConsultaDisciplina(null);
			setCampoConsultaDisciplina(null);
		} catch (Exception ex) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
		}
	}

	public void limparDadosDisciplina() {
		try {
			getListaConsultaDisciplina().clear();
			setValorConsultaDisciplina(null);
			setCampoConsultaDisciplina(null);
			getAvaliacaoInstitucionalVO().setDisciplina(null);
			getAvaliacaoInstitucionalVO().getDisciplina().setNome("Todas Disciplinas");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void inicializarDadosPublicoAlvo() {
		limparDadosCursoTurmaDisciplina();
		getAvaliacaoInstitucionalVO().getCargo().setCodigo(0);
		getAvaliacaoInstitucionalVO().getCargo().setNome("");
		getAvaliacaoInstitucionalVO().getDepartamento().setCodigo(0);
		getAvaliacaoInstitucionalVO().getDepartamento().setNome("");
		getAvaliacaoInstitucionalVO().setQuestionarioVO(new QuestionarioVO());
		if (getAvaliacaoInstitucionalVO().getIsApresentarDepartamento()) {
			montarListaSelectItemDepartamento();
		}
		if (getAvaliacaoInstitucionalVO().getIsApresentarCargo()) {
			montarListaSelectItemCargo();
		}
		if (!getIsPublicoAlvoTodosCursoOuCursoOuTurmaSelecionado()) {
			getAvaliacaoInstitucionalVO().getListaAvaliacaoInstitucionalPessoaAvaliadaVOs().clear();
		}
		inicializarListaNivelDetalhamento();
		montarListaSelectItemQuestionarioUnidadeEnsino();
	}

	public void montarListaSelectItemCargo() {
		try {
			getListaSelectItemCargo().clear();
			if (getAvaliacaoInstitucionalVO().getIsApresentarCargo()) {
				List<CargoVO> cargoVOs = getFacadeFactory().getCargoFacade().consultarPorDepartamento(
						getAvaliacaoInstitucionalVO().getDepartamento().getCodigo(), false,
						Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				getListaSelectItemCargo().add(new SelectItem(0, ""));
				for (CargoVO cargoVO : cargoVOs) {
					getListaSelectItemCargo().add(new SelectItem(cargoVO.getCodigo(), cargoVO.getNome()));
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarListaSelectItemCargoAvaliado() {
		try {
			getListaSelectItemCargoAvaliado().clear();
			if (getAvaliacaoInstitucionalVO().getIsApresentarCargoAvaliado()) {
				List<CargoVO> cargoVOs = getFacadeFactory().getCargoFacade().consultarPorDepartamento(
						getAvaliacaoInstitucionalVO().getDepartamentoAvaliado().getCodigo(), false,
						Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				getListaSelectItemCargoAvaliado().add(new SelectItem(0, ""));
				for (CargoVO cargoVO : cargoVOs) {
					getListaSelectItemCargoAvaliado().add(new SelectItem(cargoVO.getCodigo(), cargoVO.getNome()));
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarListaSelectItemDepartamento() {
		try {
			getListaSelectItemDepartamento().clear();
			if (getAvaliacaoInstitucionalVO().getIsApresentarDepartamento()) {
				List<DepartamentoVO> departamentoVOs = getFacadeFactory().getDepartamentoFacade()
						.consultarPorCodigoUnidadeEnsinoESemUE(
								getAvaliacaoInstitucionalVO().getUnidadeEnsino().getCodigo(), false,
								Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				getListaSelectItemDepartamento().add(new SelectItem(0, ""));
				for (DepartamentoVO departamentoVO : departamentoVOs) {
					getListaSelectItemDepartamento()
							.add(new SelectItem(departamentoVO.getCodigo(), departamentoVO.getNome()));
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparDadosCursoTurmaDisciplina() {
		limparDadosCurso();
		limparDadosTurma();
		getAvaliacaoInstitucionalVO().getDisciplina().setCodigo(0);
		getAvaliacaoInstitucionalVO().getDisciplina().setNome("Todas Disciplinas");
	}

	public List<SelectItem> getTipoFiltroComboProfessor() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("todos", "Todos"));
		itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("turma", "Turma"));
		return itens;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>QuestionarioUnidadeEnsino</code>.
	 */
	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List<UnidadeEnsinoVO> resultadoConsulta = null;
		Iterator<UnidadeEnsinoVO> i = null;
		try {
			if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				getListaSelectItemUnidadeEnsino().add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(),
						getUnidadeEnsinoLogado().getNome().toString()));
				getAvaliacaoInstitucionalVO().getUnidadeEnsino().setCodigo(getUnidadeEnsinoLogado().getCodigo());
				return;
			}
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				if (getAvaliacaoInstitucionalVO().getUnidadeEnsino().getCodigo() == 0) {
					getAvaliacaoInstitucionalVO().getUnidadeEnsino().setCodigo(obj.getCodigo());
				}
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			Uteis.liberarListaMemoria(resultadoConsulta);
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>QuestionarioUnidadeEnsino</code>. Buscando todos os objetos
	 * correspondentes a entidade <code>Questionario</code>. Esta rotina não recebe
	 * parâmetros para filtragem de dados, isto é importante para a inicialização
	 * dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String descricaoPrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(descricaoPrm,
				this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>QuestionarioUnidadeEnsino</code>.
	 */
	public void montarListaSelectItemQuestionario(String prm) throws Exception {
		List<QuestionarioVO> resultadoConsulta = null;
		try {
			getListaSelectItemQuestionario().clear();
			if (getAvaliacaoInstitucionalVO().getCodigo() > 0
					&& getAvaliacaoInstitucionalVO().getSituacao().equals("AT")) {
				getListaSelectItemQuestionario()
						.add(new SelectItem(getAvaliacaoInstitucionalVO().getQuestionarioVO().getCodigo(),
								getAvaliacaoInstitucionalVO().getQuestionarioVO().getDescricao().toString()));
			} else {
				resultadoConsulta = consultarQuestionarioPorDescricao(prm);
				getListaSelectItemQuestionario().add(new SelectItem(0, ""));
				boolean possuiQuestionario = !Uteis
						.isAtributoPreenchido(getAvaliacaoInstitucionalVO().getQuestionarioVO().getCodigo());
				for (QuestionarioVO obj : resultadoConsulta) {
					getListaSelectItemQuestionario()
							.add(new SelectItem(obj.getCodigo(), obj.getDescricao().toString()));
					if (obj.getCodigo().equals(getAvaliacaoInstitucionalVO().getQuestionarioVO().getCodigo())) {
						possuiQuestionario = true;
					}
				}
				if (!possuiQuestionario) {
					getListaSelectItemQuestionario()
							.add(new SelectItem(getAvaliacaoInstitucionalVO().getQuestionarioVO().getCodigo(),
									getAvaliacaoInstitucionalVO().getQuestionarioVO().getDescricao().toString()));
				}
				Uteis.liberarListaMemoria(resultadoConsulta);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>QuestionarioUnidadeEnsino</code>. Buscando todos os objetos
	 * correspondentes a entidade <code>Questionario</code>. Esta rotina não recebe
	 * parâmetros para filtragem de dados, isto é importante para a inicialização
	 * dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemQuestionarioUnidadeEnsino() {
		try {
			montarListaSelectItemQuestionario("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>descricao</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List<QuestionarioVO> consultarQuestionarioPorDescricao(String descricaoPrm) throws Exception {
		if (getAvaliacaoInstitucionalVO().getPublicoAlvo().equals("")) {
			return null;
		}

		TipoEscopoQuestionarioPerguntaEnum escopo = getAvaliacaoInstitucionalVO().getAvaliacaoUltimoModulo()
				? TipoEscopoQuestionarioPerguntaEnum.ULTIMO_MODULO
				: PublicoAlvoAvaliacaoInstitucional.getEnum(getAvaliacaoInstitucionalVO().getPublicoAlvo())
				.getEscopoQuestionario();
		return getFacadeFactory().getQuestionarioFacade().consultarPorEscopoSituacao(escopo, "AT",
				!escopo.equals(TipoEscopoQuestionarioPerguntaEnum.ULTIMO_MODULO), false,
				Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());

	}

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() throws Exception {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemQuestionarioUnidadeEnsino();
		montarListaSelectItemPublicAlvoAvaliacaoInstitucional();
		montarListaSelectItemNivelEducacionalCurso();
		montarListaSelectItemDepartamento();
		montarListaSelectItemCargo();
	}

	/**
	 * Rotina responsável por atribui um javascript com o método de mascara para
	 * campos do tipo Data, CPF, CNPJ, etc.
	 */
	public String getMascaraConsulta() {
		if (getControleConsulta().getCampoConsulta().equals("data")
				|| getControleConsulta().getCampoConsulta().equals("dataInicio")
				|| getControleConsulta().getCampoConsulta().equals("dataFinal")) {
			return "return mascara(this.form,'form:valorConsulta','99/99/9999',event);";
		}
		return "";
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("situacao", "Situação"));
		itens.add(new SelectItem("data", "Data"));
		itens.add(new SelectItem("publicoAlvo", "Público Alvo"));
		itens.add(new SelectItem("dataInicio", "Data Início"));
		itens.add(new SelectItem("dataFinal", "Data Final"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("questionario", "Questinário"));

		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de
	 * uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<>(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoInstitucionalCons.xhtml");
	}

	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do backing
	 * bean. Garantindo uma melhor atuação do Garbage Coletor do Java. A mesma é
	 * automaticamente quando realiza o logout.
	 */
	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		avaliacaoInstitucionalVO = null;
		Uteis.liberarListaMemoria(getListaSelectItemQuestionario());

	}

	public Boolean getApresentarBotaoAtivar() {
		return !getAvaliacaoInstitucionalVO().getSituacao().equals("AT") && !getAvaliacaoInstitucionalVO().getNovoObj();
	}

	public Boolean getApresentarBotaoFinalizar() {
		return getAvaliacaoInstitucionalVO().getSituacao().equals("AT") && !getAvaliacaoInstitucionalVO().getNovoObj();
	}

	public void realizarAtivacaoSituacaoAvaliacao() {
		try {
			getFacadeFactory().getAvaliacaoInstitucionalFacade().alterarSituacaoAvaliacao(
					getAvaliacaoInstitucionalVO().getCodigo(), "AT", getAvaliacaoInstitucionalVO(), getUsuarioLogado());
			getAvaliacaoInstitucionalVO().setSituacao("AT");
			setMensagemID("msg_dados_ativado");
		} catch (Exception ex) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
		}
	}

	public void montarListaSelectItemNivelEducacionalCurso() throws Exception {
		setListaNivelEducacional(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelEducacional.class, true));
	}

	public void realizarFinalizacaoSituacaoAvaliacao() {
		try {
			getFacadeFactory().getAvaliacaoInstitucionalFacade().alterarSituacaoAvaliacao(
					getAvaliacaoInstitucionalVO().getCodigo(), "FI", null, getUsuarioLogado());
			getAvaliacaoInstitucionalVO().setSituacao("FI");
			setMensagemID("msg_dados_finalizado");
		} catch (Exception ex) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
		}
	}

	public void addAvaliacaoInstitucionalPessoaAvaliada() {
		try {
			getAvaliacaoInstitucionalPessoaAvaliadaVO()
					.setPessoaVO((PessoaVO) context().getExternalContext().getRequestMap().get("professor"));
			getFacadeFactory().getAvaliacaoInstitucionalFacade().addAvaliacaoInstitucionalPessoaAvaliadaVO(
					getAvaliacaoInstitucionalVO(), getAvaliacaoInstitucionalPessoaAvaliadaVO());
			setAvaliacaoInstitucionalPessoaAvaliadaVO(new AvaliacaoInstitucionalPessoaAvaliadaVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception ex) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
		}
	}

	public void removerAvaliacaoInstitucionalPessoaAvaliada() {
		try {
			AvaliacaoInstitucionalPessoaAvaliadaVO obj = (AvaliacaoInstitucionalPessoaAvaliadaVO) context()
					.getExternalContext().getRequestMap().get("avaliacaoInstitucionalPessoaAvaliadaVOItens");
			getFacadeFactory().getAvaliacaoInstitucionalFacade()
					.removerAvaliacaoInstitucionalPessoaAvaliadaVO(getAvaliacaoInstitucionalVO(), obj);
			setMensagemID("msg_dados_excluidos");
		} catch (Exception ex) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
		}
	}

	public void consultarPessoa() {
		try {
			super.consultar();
			List<PessoaVO> objs = new ArrayList<>(0);
			if (getCampoConsultaPessoa().equals("nome")) {
				if (getValorConsultaPessoa().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaPessoa(),
						getTipoPessoaAvaliar().getValor(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN,
						getUsuarioLogado());
			}
			if (getCampoConsultaPessoa().equals("cpf")) {
				if (getValorConsultaPessoa().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaPessoa(),
						getTipoPessoaAvaliar().getValor(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN,
						getUsuarioLogado());
			}
			setListaConsultaPessoa(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public Boolean getApresentarCampoCpf() {
		return getCampoConsultaPessoa().equals("cpf") ? true : false;
	}

	public List<SelectItem> getTipoConsultaComboPessoa() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("cpf", "CPF"));
		return itens;
	}

	public List<PessoaVO> getListaConsultaPessoa() {
		if (listaConsultaPessoa == null) {
			listaConsultaPessoa = new ArrayList<PessoaVO>();
		}
		return listaConsultaPessoa;
	}

	public void setListaConsultaPessoa(List<PessoaVO> listaConsultaPessoa) {
		this.listaConsultaPessoa = listaConsultaPessoa;
	}

	public String getCampoConsultaPessoa() {
		if (campoConsultaPessoa == null) {
			campoConsultaPessoa = "";
		}
		return campoConsultaPessoa;
	}

	public void setCampoConsultaPessoa(String campoConsultaPessoa) {
		this.campoConsultaPessoa = campoConsultaPessoa;
	}

	public String getValorConsultaPessoa() {
		if (valorConsultaPessoa == null) {
			valorConsultaPessoa = "";
		}
		return valorConsultaPessoa;
	}

	public void setValorConsultaPessoa(String valorConsultaPessoa) {
		this.valorConsultaPessoa = valorConsultaPessoa;
	}

	/**
	 * Operação que inicializa as Interfaces Façades com os respectivos objetos de
	 * persistência dos dados no banco de dados.
	 */
	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public AvaliacaoInstitucionalVO getAvaliacaoInstitucionalVO() {
		if (avaliacaoInstitucionalVO == null) {
			avaliacaoInstitucionalVO = new AvaliacaoInstitucionalVO();
		}
		return avaliacaoInstitucionalVO;
	}

	public void setAvaliacaoInstitucionalVO(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO) {
		this.avaliacaoInstitucionalVO = avaliacaoInstitucionalVO;
	}

	public boolean getIsFiltroData() {
		if (getControleConsulta().getCampoConsulta().equals("data")
				|| getControleConsulta().getCampoConsulta().equals("dataInicio")
				|| getControleConsulta().getCampoConsulta().equals("dataFinal")) {
			return true;
		}
		return false;
	}

	public boolean getIsFiltroNaoData() {
		if (!getControleConsulta().getCampoConsulta().equals("data")
				&& !getControleConsulta().getCampoConsulta().equals("dataInicio")
				&& !getControleConsulta().getCampoConsulta().equals("dataFinal")) {
			return true;
		}
		return false;
	}

	public List<SelectItem> getListaSelectItemQuestionario() {
		if (listaSelectItemQuestionario == null) {
			listaSelectItemQuestionario = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemQuestionario;
	}

	public void setListaSelectItemQuestionario(List<SelectItem> listaSelectItemQuestionario) {
		this.listaSelectItemQuestionario = listaSelectItemQuestionario;
	}

	public List<SelectItem> getListaSelectItemPublicoAlvoAvaliacaoInstitucional() {
		if (listaSelectItemPublicoAlvoAvaliacaoInstitucional == null) {
			listaSelectItemPublicoAlvoAvaliacaoInstitucional = new ArrayList<>(0);
		}
		return listaSelectItemPublicoAlvoAvaliacaoInstitucional;
	}

	public void setListaSelectItemPublicoAlvoAvaliacaoInstitucional(
			List<SelectItem> listaSelectItemPublicoAlvoAvaliacaoInstitucional) {
		this.listaSelectItemPublicoAlvoAvaliacaoInstitucional = listaSelectItemPublicoAlvoAvaliacaoInstitucional;
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

	public String getCampoConsultaDisciplina() {
		if (campoConsultaDisciplina == null) {
			campoConsultaDisciplina = "";
		}
		return campoConsultaDisciplina;
	}

	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
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

	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public List<DisciplinaVO> getListaConsultaDisciplina() {
		if (listaConsultaDisciplina == null) {
			listaConsultaDisciplina = new ArrayList<>(0);
		}
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(List<DisciplinaVO> listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
	}

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
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

	public String getValorConsultaDisciplina() {
		if (valorConsultaDisciplina == null) {
			valorConsultaDisciplina = "";
		}
		return valorConsultaDisciplina;
	}

	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
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

	public boolean getIsPublicoAlvoTodosCursoOuCursoOuTurmaSelecionado() {
		return (getAvaliacaoInstitucionalVO().getPublicoAlvo_TodosCursos()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_AlunoCoordenador()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_Curso()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_Turma()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_CargoCoordenador()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_DepartamentoCoordenador());
	}

	public boolean getIsPublicoAlvoCursoTurmaSelecionado() {
		return (getAvaliacaoInstitucionalVO().getPublicoAlvo_TodosCursos()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_ProfessorTurma()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_ProfessorCurso()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_AlunoCoordenador()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_Curso()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_Turma());
	}

	public boolean getIsPublicoAlvoCursoSelecionado() {
		return (getAvaliacaoInstitucionalVO().getPublicoAlvo_Curso()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_ProfessorTurma()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_ProfessorCurso()
				|| (getAvaliacaoInstitucionalVO().getPublicoAlvo_Professor()
				&& getAvaliacaoInstitucionalVO().getTipoFiltroProfessor().equals("curso")));
	}

	public boolean getIsPublicoAlvoTurmaSelecionada() {
		return (getAvaliacaoInstitucionalVO().getPublicoAlvo_Turma()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_AlunoCoordenador()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_ProfessorTurma()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_ProfessorCurso()
				|| (getAvaliacaoInstitucionalVO().getPublicoAlvo_Professor()
				&& getAvaliacaoInstitucionalVO().getTipoFiltroProfessor().equals("turma")));
	}

	public boolean getIsPublicoAlvoProfessorSelecionado() {
		return (getAvaliacaoInstitucionalVO().getPublicoAlvo_Professor());
	}

	public void setListaNivelEducacional(List<SelectItem> listaNivelEducacional) {
		this.listaNivelEducacional = listaNivelEducacional;
	}

	public List<SelectItem> getListaNivelEducacional() {
		if (listaNivelEducacional == null) {
			listaNivelEducacional = new ArrayList<>(0);
		}
		return listaNivelEducacional;
	}

	public Boolean getApresentarSemestre() {
		if (apresentarSemestre == null) {
			apresentarSemestre = false;
		}
		return apresentarSemestre;
	}

	public void setApresentarSemestre(Boolean apresentarSemestre) {
		this.apresentarSemestre = apresentarSemestre;
	}

	public Boolean getApresentarAno() {
		if (apresentarAno == null) {
			apresentarAno = false;
		}
		return apresentarAno;
	}

	public void setApresentarAno(Boolean apresentarAno) {
		this.apresentarAno = apresentarAno;
	}

	public List<SelectItem> getTipoConsultaComboSemestre() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("1", "1º"));
		itens.add(new SelectItem("2", "2º"));
		return itens;
	}

	public Boolean getConsultaDataScroller() {
		if (consultaDataScroller == null) {
			consultaDataScroller = false;
		}
		return consultaDataScroller;
	}

	public void setConsultaDataScroller(Boolean consultaDataScroller) {
		this.consultaDataScroller = consultaDataScroller;
	}

	public List<SelectItem> getListaSelectItemDepartamento() {
		if (listaSelectItemDepartamento == null) {
			listaSelectItemDepartamento = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDepartamento;
	}

	public void setListaSelectItemDepartamento(List<SelectItem> listaSelectItemDepartamento) {
		this.listaSelectItemDepartamento = listaSelectItemDepartamento;
	}

	public List<SelectItem> getListaSelectItemCargo() {
		if (listaSelectItemCargo == null) {
			listaSelectItemCargo = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemCargo;
	}

	public void setListaSelectItemCargo(List<SelectItem> listaSelectItemCargo) {
		this.listaSelectItemCargo = listaSelectItemCargo;
	}

	public List<SelectItem> getListaSelectItemCargoAvaliado() {
		if (listaSelectItemCargoAvaliado == null) {
			listaSelectItemCargoAvaliado = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemCargoAvaliado;
	}

	public void setListaSelectItemCargoAvaliado(List<SelectItem> listaSelectItemCargoAvaliado) {
		this.listaSelectItemCargoAvaliado = listaSelectItemCargoAvaliado;
	}

	public AvaliacaoInstitucionalPessoaAvaliadaVO getAvaliacaoInstitucionalPessoaAvaliadaVO() {
		if (avaliacaoInstitucionalPessoaAvaliadaVO == null) {
			avaliacaoInstitucionalPessoaAvaliadaVO = new AvaliacaoInstitucionalPessoaAvaliadaVO();
		}
		return avaliacaoInstitucionalPessoaAvaliadaVO;
	}

	public void setAvaliacaoInstitucionalPessoaAvaliadaVO(
			AvaliacaoInstitucionalPessoaAvaliadaVO avaliacaoInstitucionalPessoaAvaliadaVO) {
		this.avaliacaoInstitucionalPessoaAvaliadaVO = avaliacaoInstitucionalPessoaAvaliadaVO;
	}

	public TipoPessoa getTipoPessoaAvaliar() {
		return getAvaliacaoInstitucionalVO().getPublicoAlvo_AlunoCoordenador()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_ProfessorCoordenador()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_DepartamentoCoordenador()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_CargoCoordenador()
				? TipoPessoa.COORDENADOR_CURSO
				: getAvaliacaoInstitucionalVO().getPublicoAlvo_TodosCursos()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_Turma()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_Curso()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_CoordenadorProfessor()
				? TipoPessoa.PROFESSOR
				: TipoPessoa.FUNCIONARIO;
	}

	public Boolean getHabilitarPublicacaoVisaoAvaliado() {
		return !getAvaliacaoInstitucionalVO().getPublicoAlvo_Professor()
				&& !getAvaliacaoInstitucionalVO().getPublicoAlvo_TodosCoordenadores()
				&& !getAvaliacaoInstitucionalVO().getPublicoAlvo_ProfessorCurso()
				&& !getAvaliacaoInstitucionalVO().getPublicoAlvo_ProfessorTurma()
				&& !getAvaliacaoInstitucionalVO().getPublicoAlvo_CoordenadorAvaliacaoCurso()
				&& !((getAvaliacaoInstitucionalVO().getPublicoAlvo_Curso()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_TodosCursos()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_Turma())
				&& getAvaliacaoInstitucionalVO().getQuestionarioVO().getEscopo().equals("GE"));
	}

	public Boolean getHabilitarPublicacaoVisaoAluno() {
		return getAvaliacaoInstitucionalVO().getPublicoAlvo_AlunoCoordenador()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_Curso()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_TodosCursos()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_Turma();
	}

	public Boolean getHabilitarPublicacaoVisaoCoordenador() {
		return getAvaliacaoInstitucionalVO().getPublicoAlvo_Curso()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_TodosCursos()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_Turma()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_Professor()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_FuncionarioGestor()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_ProfessorCurso()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_CoordenadorAvaliacaoCurso()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_ProfessorTurma()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_TodosCoordenadores();
	}

	public Boolean getHabilitarPublicacaoVisaoProfessor() {
		return getAvaliacaoInstitucionalVO().getPublicoAlvo_ProfessorCoordenador()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_FuncionarioGestor()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_Professor()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_CoordenadorAvaliacaoCurso()
				|| (getAvaliacaoInstitucionalVO().getQuestionarioVO().getEscopo().equals("GE")
				&& (getAvaliacaoInstitucionalVO().getPublicoAlvo_Curso()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_TodosCursos()
				|| getAvaliacaoInstitucionalVO().getPublicoAlvo_Turma()));
	}

	public Boolean getHabilitarPeriodoPublicacao() {
		return getAvaliacaoInstitucionalVO().getPublicarResultadoRespondente()
				|| getAvaliacaoInstitucionalVO().getPublicarResultadoAluno()
				|| getAvaliacaoInstitucionalVO().getPublicarResultadoProfessor()
				|| getAvaliacaoInstitucionalVO().getPublicarResultadoCoordenador();
	}

	public void inicializarDadosQuestionario() {
		if (Uteis.isAtributoPreenchido(getAvaliacaoInstitucionalVO().getQuestionarioVO())) {
			try {
				getAvaliacaoInstitucionalVO().setQuestionarioVO(getFacadeFactory().getQuestionarioFacade()
						.consultarPorChavePrimaria(getAvaliacaoInstitucionalVO().getQuestionarioVO().getCodigo(),
								Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				inicializarListaNivelDetalhamento();
				limparMensagem();
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
			}
		} else {
			getAvaliacaoInstitucionalVO().setQuestionarioVO(null);
		}
	}

	public String getNomeAvaliadoApresentar() {
		if (getHabilitarPublicacaoVisaoAvaliado()) {
			if (getAvaliacaoInstitucionalVO().getPublicoAlvo_CargoCoordenador()
					|| getAvaliacaoInstitucionalVO().getPublicoAlvo_ProfessorCoordenador()
					|| getAvaliacaoInstitucionalVO().getPublicoAlvo_DepartamentoCoordenador()
					|| getAvaliacaoInstitucionalVO().getPublicoAlvo_AlunoCoordenador()) {

				return "Coordenador";
			} else if (getAvaliacaoInstitucionalVO().getPublicoAlvo_CoordenadorProfessor()
					|| ((getAvaliacaoInstitucionalVO().getPublicoAlvo_Curso()
					|| getAvaliacaoInstitucionalVO().getPublicoAlvo_TodosCursos()
					|| getAvaliacaoInstitucionalVO().getPublicoAlvo_Turma())
					&& !getAvaliacaoInstitucionalVO().getQuestionarioVO().getEscopo().equals("GE"))) {
				return "Professor";
			} else if (getAvaliacaoInstitucionalVO().getPublicarAlvoEnum()
					.equals(PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_CARGO)
					|| getAvaliacaoInstitucionalVO().getPublicarAlvoEnum()
					.equals(PublicoAlvoAvaliacaoInstitucional.CARGO_CARGO)
					|| getAvaliacaoInstitucionalVO().getPublicarAlvoEnum()
					.equals(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_CARGO)) {
				return "Funcionários do Cargo Avaliado";
			} else if (getAvaliacaoInstitucionalVO().getPublicarAlvoEnum()
					.equals(PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_DEPARTAMENTO)
					|| getAvaliacaoInstitucionalVO().getPublicarAlvoEnum()
					.equals(PublicoAlvoAvaliacaoInstitucional.CARGO_DEPARTAMENTO)
					|| getAvaliacaoInstitucionalVO().getPublicarAlvoEnum()
					.equals(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_DEPARTAMENTO)) {
				return "Funcionários do Departamento Avaliado";
			} else if (getAvaliacaoInstitucionalVO().getPublicarAlvoEnum()
					.equals(PublicoAlvoAvaliacaoInstitucional.FUNCIONARIO_GESTOR)
					|| getAvaliacaoInstitucionalVO().getPublicarAlvoEnum()
					.equals(PublicoAlvoAvaliacaoInstitucional.COLABORADORES_INSTITUICAO)) {
				return "Funcionários da Unidade de Ensino";
			}
		}
		return "Avaliado";
	}

	public void inicializarListaNivelDetalhamento() {
		listaSelectItemNivelDetalhamento = new ArrayList<SelectItem>(0);
		for (NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamentoResultadoAvaliacaoInstitucionalEnum : NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum
				.values()) {
			if (nivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.getIsPermiteDatalhar(
					PublicoAlvoAvaliacaoInstitucional.getEnum(getAvaliacaoInstitucionalVO().getPublicoAlvo()),
					getAvaliacaoInstitucionalVO().getQuestionarioVO().getEscopo().equals("GE"))) {
				listaSelectItemNivelDetalhamento
						.add(new SelectItem(nivelDetalhamentoResultadoAvaliacaoInstitucionalEnum,
								nivelDetalhamentoResultadoAvaliacaoInstitucionalEnum
										.getDescricao(getAvaliacaoInstitucionalVO())));
			}
		}
		if (getAvaliacaoInstitucionalVO().isNovoObj()) {
			getAvaliacaoInstitucionalVO().setNivelDetalhamentoPublicarResultado(
					NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.AVALIADO);
		}
	}

	public List<SelectItem> getListaSelectItemNivelDetalhamento() {
		if (listaSelectItemNivelDetalhamento == null) {
			inicializarListaNivelDetalhamento();
		}
		return listaSelectItemNivelDetalhamento;
	}

	public void setListaSelectItemNivelDetalhamento(List<SelectItem> listaSelectItemNivelDetalhamento) {
		this.listaSelectItemNivelDetalhamento = listaSelectItemNivelDetalhamento;
	}

	private List<SelectItem> listaSelectItemTipoCoordenadorCurso;

	public List<SelectItem> getListaSelectItemTipoCoordenadorCurso() {
		if (listaSelectItemTipoCoordenadorCurso == null) {
			listaSelectItemTipoCoordenadorCurso = UtilSelectItem
					.getListaSelectItemEnum(TipoCoordenadorCursoEnum.values(), Obrigatorio.SIM);
		}
		return listaSelectItemTipoCoordenadorCurso;
	}

	public void realizarCloneAvaliacaoInstitucional() {
		getFacadeFactory().getAvaliacaoInstitucionalFacade()
				.realizarCloneAvaliacaoInstitucional(getAvaliacaoInstitucionalVO(), getUsuarioLogado());
	}

	public void consultarCursoFiltroRelatorio() {
		try {
			setCursosApresentar("");
			setTurnosApresentar("");
			if(!getAvaliacaoInstitucionalVO().getAvaliacaoInstitucionalUnidadeEnsinoVOs().isEmpty()){
				setCursoVOs(getFacadeFactory().getCursoFacade().consultaRapidaPorNomePorListaUnidadeEnsinoPorNivelEducacional("",
						getAvaliacaoInstitucionalVO().getAvaliacaoInstitucionalUnidadeEnsinoVOs()
								.stream()
								.map(AvaliacaoInstitucionalUnidadeEnsinoVO::getUnidadeEnsinoVO)
								.collect(Collectors.toList())
						,
						TipoNivelEducacional.getEnum(getAvaliacaoInstitucionalVO().getNivelEducacional()),
						new DataModelo()));
			}else{
				setCursoVOs(null);
			}
			getFacadeFactory().getAvaliacaoInstitucionalFacade().realizarMarcacaoCursoSelecionado(getAvaliacaoInstitucionalVO(), getCursoVOs());
			verificarTodosCursosSelecionados();
		} catch (Exception e) {
			setCursoVOs(null);
		}
	}

	public void inicializarDadosUnidadeEnsino() {
			consultarUnidadeEnsinoFiltroRelatorio("AvaliacaoInstitucional");
			getUnidadeEnsinoVOs().forEach(unidadeEnsino -> {
				AvaliacaoInstitucionalUnidadeEnsinoVO novoItem = new AvaliacaoInstitucionalUnidadeEnsinoVO();
				//unidadeEnsino.setFiltrarUnidadeEnsino(true);
				novoItem.setUnidadeEnsinoVO(unidadeEnsino);
				avaliacaoInstitucionalVO.getAvaliacaoInstitucionalUnidadeEnsinoVOs().add(novoItem);
			});
		montarListaSelectItemDepartamento();
	}

	public void realizarMarcacaoCursos() {
		for (CursoVO cursoVO : getCursoVOs()) {
			cursoVO.setFiltrarCursoVO(getMarcarTodosCursos());
		}
		verificarTodosCursosSelecionados();
	}

	public void verificarTodosCursosSelecionados() {
		StringBuilder curso = new StringBuilder();
		StringBuilder codigosCurso = new StringBuilder();
		for (CursoVO cursoVO : getCursoVOs()) {
			try {
				if (cursoVO.getFiltrarCursoVO()) {
					getFacadeFactory().getAvaliacaoInstitucionalFacade()
							.adicionarAvaliacaoInstitucionalCurso(getAvaliacaoInstitucionalVO(), cursoVO);
					curso.append(cursoVO.getCodigo()).append(" - ");
					curso.append(cursoVO.getNome()).append("; ");
					codigosCurso.append(cursoVO.getCodigo()).append(" - ");
				} else {
					getFacadeFactory().getAvaliacaoInstitucionalFacade()
							.removerAvaliacaoInstitucionalCurso(getAvaliacaoInstitucionalVO(), cursoVO);
					getAvaliacaoInstitucionalVO().setNomeCurso("");
				}
				getAvaliacaoInstitucionalVO().setNomeCurso(curso.toString());
				limparMensagem();
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
			}
		}
		if(getCursoVOs().isEmpty()) {
			getAvaliacaoInstitucionalVO().setNomeCurso("");
		}
	}

	public boolean getApresentarPublicoAlvoAvaliacaoInstitucional() {
		if (getAvaliacaoInstitucionalVO().getQuestionarioVO().getEscopo().equals("DI")) {
			if (getAvaliacaoInstitucionalVO().isNovoObj()) {
				getAvaliacaoInstitucionalVO().setAvaliarDisciplinasReposicao(Boolean.TRUE);
			}
			return true;
		}
		getAvaliacaoInstitucionalVO().setAvaliarDisciplinasReposicao(Boolean.FALSE);
		return false;
	}

	public String getValorConsultaUnidadeEnsino() {
		if(valorConsultaUnidadeEnsino == null){
			valorConsultaUnidadeEnsino = "";
		}
		return valorConsultaUnidadeEnsino;
	}

	public void setValorConsultaUnidadeEnsino(String valorConsultaUnidadeEnsino) {
		this.valorConsultaUnidadeEnsino = valorConsultaUnidadeEnsino;
	}

	public String getCampoConsultaUnidadeEnsino() {
		if(campoConsultaUnidadeEnsino == null){
			campoConsultaUnidadeEnsino = "";
		}
		return campoConsultaUnidadeEnsino;
	}

	public void setCampoConsultaUnidadeEnsino(String campoConsultaUnidadeEnsino) {
		this.campoConsultaUnidadeEnsino = campoConsultaUnidadeEnsino;
	}

	public List<UnidadeEnsinoVO> getListaConsultaUnidadeEnsino() {
		if(listaConsultaUnidadeEnsino == null){
			listaConsultaUnidadeEnsino = new ArrayList<UnidadeEnsinoVO>(0);
		}
		return listaConsultaUnidadeEnsino;
	}

	public void setListaConsultaUnidadeEnsino(List<UnidadeEnsinoVO> listaConsultaUnidadeEnsino) {
		this.listaConsultaUnidadeEnsino = listaConsultaUnidadeEnsino;
	}

	public void consultarUnidadeEnsino() {
		try {
			setListaConsultaUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUsuarioNomeEntidadePermissao(getIdEntidade(), "", Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaUnidadeEnsino(null);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarUnidadeEnsino() {
		try {
			UnidadeEnsinoVO obj = (UnidadeEnsinoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoItens");
			getAvaliacaoInstitucionalVO().setUnidadeEnsino(obj);
			getListaConsultaUnidadeEnsino().clear();
			this.setValorConsultaUnidadeEnsino("");
			this.setCampoConsultaUnidadeEnsino("");
			limparDadosDisciplina();
		} catch (Exception ex) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
		}
	}

	public void limparDadosUnidadeEnsino() {
		try {
			getAvaliacaoInstitucionalVO().getAvaliacaoInstitucionalUnidadeEnsinoVOs().clear();
			getAvaliacaoInstitucionalVO().setNomeUnidadeEnsino("");
			getUnidadeEnsinoVOs().clear();
			//limparDadosDisciplina();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarUnidadeEnsinoFiltroRelatorio() {
		try {
			setUnidadeEnsinosApresentar("");
			setTurnosApresentar("");
			setUnidadeEnsinoVOs(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome("",null ,false,Uteis.NIVELMONTARDADOS_DADOSBASICOS,getUsuarioLogado()));
			getFacadeFactory().getAvaliacaoInstitucionalFacade().realizarMarcacaoUnidadeEnsinoSelecionado(getAvaliacaoInstitucionalVO(), getUnidadeEnsinoVOs());
			verificarTodosCursosSelecionados();
		} catch (Exception e) {
			setUnidadeEnsinoVOs(null);
		}
	}

	public void verificarTodasUnidadeEnsinoSelecionados() {
		StringBuilder unidade = new StringBuilder();
		for (UnidadeEnsinoVO unidadeEnsinoVO : getUnidadeEnsinoVOs()) {
			try {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					getFacadeFactory().getAvaliacaoInstitucionalFacade()
							.adicionarAvaliacaoInstitucionalUnidadeEnsino(getAvaliacaoInstitucionalVO(), unidadeEnsinoVO);
					unidade.append(unidadeEnsinoVO.getNome().trim()).append("; ");
				} else {
					getFacadeFactory().getAvaliacaoInstitucionalFacade()
							.removerAvaliacaoInstitucionalUnidadeEnsino(getAvaliacaoInstitucionalVO(), unidadeEnsinoVO, getUsuarioLogado(), getMarcarTodasUnidadeEnsino());
				}
				getAvaliacaoInstitucionalVO().setNomeUnidadeEnsino(unidade.toString());
				limparMensagem();
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
			}
		}
		consultarCursoFiltroRelatorio();
	}

	public String getIdEntidade(){
		return "AvaliacaoInstitucional";
	}

}
