package controle.secretaria;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.AutorizacaoCursoVO;
import negocio.comuns.academico.ControleLivroFolhaReciboVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.EnadeVO;
import negocio.comuns.academico.MatriculaControleLivroRegistroDiplomaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.academico.enumeradores.NomeTurnoCensoEnum;
import negocio.comuns.academico.enumeradores.TipoTrabalhoConclusaoCurso;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.dominios.FinanciamentoEstudantil;
import negocio.comuns.utilitarias.dominios.FormaIngresso;
import negocio.comuns.utilitarias.dominios.JustificativaCensoEnum;
import negocio.comuns.utilitarias.dominios.TipoMobilidadeAcademicaEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("AlteracoesCadastraisMatriculaControle")
@Scope("viewScope")
@Lazy
@SuppressWarnings("unchecked")
public class AlteracoesCadastraisMatriculaControle extends SuperControle implements Serializable {

	private MatriculaControleLivroRegistroDiplomaVO matriculaControleLivroRegistroDiplomaVO;
	private ControleLivroFolhaReciboVO controleLivroFolhaReciboVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private TurmaVO turmaVO;
	private DisciplinaVO disciplinaVO;
	private List listaSelectItemTurma;
	private List listaSelectItemCurso;
	private List listaSelectItemUnidadeEnsino;
	private String semestre;
	private String ano;
	private List alunosTurma;
	private MatriculaVO matriculaVO;
	private String campoConsultaEnade;
	private String valorConsultaEnade;
	private List listaConsultaEnade;
	private List listaMatriculasControleLivroRegistroDiploma;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private List listaConsultaAluno;
	private String campoConsultaDisciplinaMatricula;
	private String valorConsultaDisciplinaMatricula;
	private List listaConsultaDisciplinaMatricula;
	private Date valorConsultaData;
	protected List listaConsultaCandidato;
	private List<SelectItem> listaSelectItemReconhecimentoVOs;
	private List<SelectItem> listaSelectItemRenovacaoReconhecimentoVOs;
	private Boolean temProgramacaoFormaturaAluno;
	private Boolean permitirExclusaoRegistroCobranca;
	private Boolean permitirAlterarInformacoesReajustePreco;
	private Boolean permiteAlterarDataBaseGeracaoParcelas;
	private List<SelectItem> listaSelectItemMesIngreso;
	private List<SelectItem> listaSelectItemFormacaoAcademica;
	private static final List<SelectItem> LISTA_SELECT_ITEM_SEMESTRE = Arrays.asList(new SelectItem("", ""), new SelectItem("1", "1º"), new SelectItem("2", "2º"));
	private static final List<SelectItem> LISTA_SELECT_ITEM_TIPO_CONSULTA_ALUNO = Arrays.asList(new SelectItem("nomePessoa", "Aluno"), new SelectItem("matricula", "Matrícula"),new SelectItem("registroAcademico", "Registro Acadêmico"), new SelectItem("nomeCurso", "Curso"));
	private static final List<SelectItem> LISTA_SELECT_ITEM_TIPO_CONSULTA_DISCIPLINA = Arrays.asList(new SelectItem("nome", "Nome"), new SelectItem("codigo", "Código"));
	private List<SelectItem> listaSelectItemDiaSemanaAula;
	private List<SelectItem> listaSelectItemTurnoAula;
	private Map<String, String> mapFinanciamentoEstudantil;
	private List<SelectItem> listaSelectItemJustificativaCenso;
	private List<SelectItem> listaSelectItemTipoMobilidadeAcademicaEnum;
	private List alunosTurmaAntiga;

	public AlteracoesCadastraisMatriculaControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		incializarDados();
		verificarPermissaoAlterarInformacaoReajustePreco();
		setMensagemID("msg_entre_prmconsulta");
	}
	
	@PostConstruct
	public void realizarCarregamentoMatriculaVindoTelaFichaAluno() {
		MatriculaVO matriculaVO = (MatriculaVO) context().getExternalContext().getSessionMap().get("matriculaFichaAluno");
		if (matriculaVO != null && !matriculaVO.getMatricula().equals("")) {
			try {
				setMatriculaVO(matriculaVO);
				consultarAlunoPorMatricula();
				List<MatriculaVO> matriculas = new ArrayList<>();
				matriculas.add(matriculaVO);
				setAlunosTurmaAntiga(clonarListaMatriculas(matriculas));
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				context().getExternalContext().getSessionMap().remove("matriculaFichaAluno");
			}
			
		}
	}
	
	public void verificarPermissaoAlterarInformacaoReajustePreco() throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirAlterarInformacoesReajustePreco", getUsuarioLogado());
			setPermitirAlterarInformacoesReajustePreco(Boolean.TRUE);
		} catch (Exception e) {
			setPermitirAlterarInformacoesReajustePreco(Boolean.FALSE);
		}
	}

	public String novo() {
		removerObjetoMemoria(this);
		setMatriculaVO(new MatriculaVO());
		setAno(Uteis.getAnoDataAtual4Digitos());
		setListaSelectItemTurma(new ArrayList(0));
		setListaSelectItemCurso(new ArrayList(0));
		setListaSelectItemUnidadeEnsino(new ArrayList(0));
		setAlunosTurma(new ArrayList(0));
		setListaMatriculasControleLivroRegistroDiploma(new ArrayList(0));
		setUnidadeEnsinoVO(new UnidadeEnsinoVO());
		setAno("");
		setSemestre("");
		montarListaSelectItemUnidadeEnsino();
		
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("alteracoesCadastraisMatriculaForm.xhtml");
	}

	public void atualizarMatricula() {
		try {
			getFacadeFactory().getMatriculaFacade().alterarInformacoesCadastrais(getAlunosTurma(), getListaMatriculasControleLivroRegistroDiploma(), getUsuarioLogado());
			Map<String, String> hashMatriculasIncial = matriculasParaHash(getAlunosTurmaAntiga());
			Map<String, String> hashMatriculasFinal = matriculasParaHash(getAlunosTurma());
			List<MatriculaVO> matriculasNovas = getAlunosTurma();
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema();
			matriculasNovas.forEach( matricula -> {
			getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().atualizarAlunoPorMatricula(matricula, hashMatriculasIncial, hashMatriculasFinal, getUsuarioLogado(), configuracaoGeralSistemaVO);
			});
			setAlunosTurmaAntiga(clonarListaMatriculas(getAlunosTurma()));
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private Map<String, String>  matriculasParaHash(List matriculas) {
		Map<String, String> hashAtual = new HashMap<>();
		List<MatriculaVO> matriculaVOS = matriculas;
		matriculaVOS.forEach( matricula -> {
			hashAtual.put(matricula.getMatricula(), String.valueOf(Objects.hash(matricula.getDiaSemanaAula())));
		});
		return hashAtual;
	}

	private List<MatriculaVO> clonarListaMatriculas(List<MatriculaVO> matriculaIncial) {
		List<MatriculaVO> copia = new ArrayList<>();
		for (MatriculaVO m : matriculaIncial) {
			MatriculaVO clone = new MatriculaVO();
			clone.setMatricula(m.getMatricula());
			clone.setDiaSemanaAula(m.getDiaSemanaAula());
			copia.add(clone);
		}
		return copia;
	}

	public void validarDadosBuscaPorUnidadeEnsino() throws Exception {
		if (getUnidadeEnsinoVO().getCodigo().intValue() == 0) {
			throw new Exception("UNIDADE DE ENSINO não informada para a busca.");
		}
		if (getUnidadeEnsinoCursoVO().getCurso().getCodigo().intValue() == 0) {
			throw new Exception("CURSO não informado para a busca.");
		}

	}

	public void consultarAlunosPorTurma() {
		try {
			setAlunosTurma(new ArrayList<>(0));
			validarDadosBuscaPorUnidadeEnsino();
			Map<String, List<? extends SuperVO>> resultado = getFacadeFactory().getMatriculaFacade().consultarDadosAlteracaoCadastral("", getUnidadeEnsinoVO().getCodigo(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), getAno(), getSemestre() ,  getMatriculaVO().getColacaoGrauVO(), getMatriculaVO().getProgramacaoFormaturaAlunoVO(), getUsuarioLogado());
			setAlunosTurma(resultado.get("MATRICULA"));
			setAlunosTurmaAntiga(clonarListaMatriculas((List<MatriculaVO>) resultado.get("MATRICULA")));
			setListaMatriculasControleLivroRegistroDiploma(resultado.get("MATRICULA_CONTROLE"));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			this.setAlunosTurma(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunosPorMatricula() throws Exception {
		setPermiteAlterarDataBaseGeracaoParcelas(true);
		setAlunosTurma(new ArrayList<>(0));
		if (getMatriculaVO().getMatricula().equals("")) {
			throw new Exception("O campo Matrícula Aluno deve ser informado!");
		}

		Map<String, List<? extends SuperVO>> resultado = getFacadeFactory().getMatriculaFacade().consultarDadosAlteracaoCadastral(getMatriculaVO().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), 0, 0, "", "",  getMatriculaVO().getColacaoGrauVO(), getMatriculaVO().getProgramacaoFormaturaAlunoVO(), getUsuarioLogado());
		setAlunosTurma(resultado.get("MATRICULA"));
		setAlunosTurmaAntiga(clonarListaMatriculas((List<MatriculaVO>) resultado.get("MATRICULA")));
		setListaMatriculasControleLivroRegistroDiploma(resultado.get("MATRICULA_CONTROLE"));
		montarListaSelectItemReconhecimentoCurso(getMatriculaVO().getCurso().getCodigo());
		setTemProgramacaoFormaturaAluno(getFacadeFactory().getProgramacaoFormaturaAlunoFacade().consultarSeExisteColacaoGrauParaMatricula(getMatriculaVO().getMatricula()));
		setMensagemID("msg_dados_consultados");
		verificarPermiteAlterarDataBaseGeracaoParcelas();
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemCurso() throws Exception {
		try {
			setListaSelectItemTurma(new ArrayList<>(0));
			setListaSelectItemCurso(new ArrayList<>(0));
			getListaSelectItemCurso().add(new SelectItem(0, ""));
			List<UnidadeEnsinoCursoVO> resultadoConsulta = consultarCursoPorUnidadeEnsino(getUnidadeEnsinoVO().getCodigo());
			for (UnidadeEnsinoCursoVO unidadeEnsinoCursoVO : resultadoConsulta) {
				getListaSelectItemCurso().add(new SelectItem(unidadeEnsinoCursoVO.getCodigo(), unidadeEnsinoCursoVO.getNomeCursoTurno()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<UnidadeEnsinoCursoVO> consultarCursoPorUnidadeEnsino(Integer unidadeEnsino) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoUnidadeEnsino(unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
	}

	public void montarListaSelectItemTurma() throws Exception {
		try {
			List<TurmaVO> resultadoConsulta = consultarTurmasPorCurso();
			setListaSelectItemTurma(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "identificadorTurma"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<TurmaVO> consultarTurmasPorCurso() throws Exception {
		if (Uteis.isAtributoPreenchido(getUnidadeEnsinoCursoVO().getCodigo())) {
			setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			return getFacadeFactory().getTurmaFacade().consultarPorUnidadeEnsinoCursoTurno(getUnidadeEnsinoVO().getCodigo(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoCursoVO().getTurno().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		}
		return Collections.EMPTY_LIST;
	}

	public void incializarDados() {
		montarListaSelectItemUnidadeEnsino();
	}

	public List<SelectItem> getListaSelectItemSemestre() {
		return LISTA_SELECT_ITEM_SEMESTRE;
	}

	public String consultarEnade() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaEnade().equals("codigo")) {
				if (getValorConsultaEnade().equals("")) {
					setValorConsultaEnade("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaEnade());
				objs = getFacadeFactory().getEnadeFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaEnade().equals("tituloEnade")) {
				objs = getFacadeFactory().getEnadeFacade().consultarPorTituloEnade(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaEnade().equals("dataPublicacaoPortariaDOU")) {

				objs = getFacadeFactory().getEnadeFacade().consultarPorDataPublicacaoPortariaDOU(Uteis.getDateTime(getValorConsultaData(), 0, 0, 0), Uteis.getDateTime(getValorConsultaData(), 23, 59, 59), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaEnade(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("alteracoesCadastraisMatriculaForm.xhtml");
		} catch (Exception e) {
			setListaConsultaEnade(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("alteracoesCadastraisMatriculaForm.xhtml");
		}
	}

	public void setarObjetoMatriculaProcuracao() {
		MatriculaControleLivroRegistroDiplomaVO matriculaControleLivroRegistroDiplomaVO = (MatriculaControleLivroRegistroDiplomaVO) context().getExternalContext().getRequestMap().get("matriculaControleLivroRegistroDiplomaItens");
		setMatriculaControleLivroRegistroDiplomaVO(matriculaControleLivroRegistroDiplomaVO);
	}

	public void setarObjetoRecebeuCertificado() {

		MatriculaControleLivroRegistroDiplomaVO matriculaControleLivroRegistroDiplomaVO = (MatriculaControleLivroRegistroDiplomaVO) context().getExternalContext().getRequestMap().get("matriculaControleLivroRegistroDiplomaItens");
		if (matriculaControleLivroRegistroDiplomaVO.isCertificadoRecebido()) {
			matriculaControleLivroRegistroDiplomaVO.setDataEntregaRecibo(new Date());
		} else {
			matriculaControleLivroRegistroDiplomaVO.setDataEntregaRecibo(null);
		}
		setMatriculaControleLivroRegistroDiplomaVO(matriculaControleLivroRegistroDiplomaVO);
	}

	public void setarObjetoMatriculaDaLista() {
		MatriculaVO matriculaVO = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		setMatriculaVO(matriculaVO);
	}

	public String getAbrirModalPanel() {
		if (getMatriculaVO().getFezEnade()) {
			return "RichFaces.$('panelEnade').show()";
		}
		return "";
	}

	public void selecionarEnade() {
		EnadeVO enadeVO = (EnadeVO) context().getExternalContext().getRequestMap().get("enadeItens");
		if (getMatriculaVO() != null) {
			getMatriculaVO().setEnadeVO(enadeVO);
			getMatriculaVO().setFezEnade(true);
			getAlunosTurma().set(getAlunosTurma().indexOf(getMatriculaVO()), getMatriculaVO());
		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatriculaVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			montarListaSelectItemFormacaoAcademica(objAluno);
			this.setMatriculaVO(objAluno);
			consultarAlunosPorMatricula();
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.setMatriculaVO(new MatriculaVO());
			this.setAlunosTurma(new ArrayList(0));
		}
	}

	public void limparInscricaoAluno() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
			obj.setInscricao(new InscricaoVO());
			obj.setDataProcessoSeletivo(null);
		} catch (Exception e) {
		}
	}
	
	public void limparDadosDisciplinaMatricula() throws Exception {
		setDisciplinaVO(new DisciplinaVO());
		setListaSelectItemTurma(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
	}

	public void limparDadosDisciplinaTurma() throws Exception {
		setDisciplinaVO(new DisciplinaVO());
		// setListaSelectItemTurmaDisciplina(new ArrayList(0));
		setAlunosTurma(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
	}

	public void limparDadosAluno() throws Exception {
		// setHistoricoVO(new HistoricoVO());
		setMatriculaVO(new MatriculaVO());
		getMatriculaVO().setMatricula("");
		setAlunosTurma(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
	}

	public void montarListaTurma() throws Exception {
		if (getDisciplinaVO().getCodigo().intValue() == 0) {
			setListaSelectItemTurma(new ArrayList(0));
			return;
		}
		List resultadoConsulta = consultarTurmaPorDisciplinaMatricula();
		setListaSelectItemTurma(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "identificadorTurma", false));
	}

	public List<TurmaVO> consultarTurmaPorDisciplinaMatricula() throws Exception {
		List<TurmaVO> objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorDisciplinaMatricula(getDisciplinaVO().getCodigo(), getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return objs;
	}

	public void montarListaDisciplinasProcessoSeletivo() {
		try {
			MatriculaVO matriculaVO = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");

			if (matriculaVO.getFormaIngresso().equals(FormaIngresso.PROCESSO_SELETIVO.getValor()) && matriculaVO.getDisciplinasProcSeletivo().trim().isEmpty()) {
				matriculaVO.setDisciplinasProcSeletivo(getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarResultadoProcessoSeletivoDescritivoPorMatricula(matriculaVO.getMatricula()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);

			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				// MatriculaVO obj =
				// getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(),
				// this.getUnidadeEnsinoLogado().getCodigo(), true,
				// Uteis.NIVELMONTARDADOS_DADOSBASICOS);
				MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
				if (!obj.getMatricula().equals("")) {
					objs.add(obj);
				}
			}
			if (getCampoConsultaAluno().equals("registroAcademico")) {				
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				// objs =
				// getFacadeFactory().getMatriculaFacade().consultarPorNomePessoa(getValorConsultaAluno(),
				// this.getUnidadeEnsinoLogado().getCodigo(), false,
				// Uteis.NIVELMONTARDADOS_DADOSBASICOS);
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				// objs =
				// getFacadeFactory().getMatriculaFacade().consultarPorNomeCurso(getValorConsultaAluno(),
				// this.getUnidadeEnsinoLogado().getCodigo(), false,
				// Uteis.NIVELMONTARDADOS_DADOSBASICOS);
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}

			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno()  {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		MatriculaVO objCompleto;
		try {
			montarListaSelectItemFormacaoAcademica(obj);
			objCompleto = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			setMatriculaVO(objCompleto);
			setDisciplinaVO(new DisciplinaVO());
			setListaSelectItemTurma(new ArrayList(0));
			obj = null;
			objCompleto = null;
			setValorConsultaAluno("");
			setCampoConsultaAluno("");
			getListaConsultaAluno().clear();
			consultarAlunosPorMatricula();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setMatriculaVO(null);
		}
		
	}

	public void consultarDisciplinaMatriculaRich() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaDisciplinaMatricula().equals("codigo")) {
				if (getValorConsultaDisciplinaMatricula().equals("")) {
					setValorConsultaDisciplinaMatricula("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaDisciplinaMatricula());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo_Matricula(new Integer(valorInt), getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplinaMatricula().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome_Matricula(getValorConsultaDisciplinaMatricula(), getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplinaMatricula().equals("areaConhecimento")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeAreaConhecimento_Matricula(getValorConsultaDisciplinaMatricula(), getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaDisciplinaMatricula(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplinaMatricula(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarDisciplinaMatricula() throws Exception {
		DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaMatriculaItens");
		setDisciplinaVO(obj);
		setTurmaVO(new TurmaVO());
		montarListaTurma();
		setListaConsultaDisciplinaMatricula(new ArrayList(0));
		obj = null;
		setValorConsultaDisciplinaMatricula("");
		setCampoConsultaDisciplinaMatricula("");
		getListaConsultaDisciplinaMatricula().clear();
	}

	public List getTipoConsultaComboEnade() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("tituloEnade", "Titulo Enade"));
		itens.add(new SelectItem("codigo", "Número"));
		itens.add(new SelectItem("dataPublicacaoPortariaDOU", "Data Publicacao no Diário Oficial"));
		return itens;
	}

	public void consultarInscricaoAluno() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
			setMatriculaVO(obj);
			setMensagemID("msg_dados_consultados");
			List<InscricaoVO> objs = new ArrayList(0);
			objs = getFacadeFactory().getInscricaoFacade().consultarCanditadoPorCodigo(matriculaVO.getAluno(), 0, 0, false, getUsuarioLogado());
			if (objs.size() == 1 && objs.stream().allMatch(p-> !Uteis.isAtributoPreenchido(p.getMatriculaVO().getMatricula()))) {
				((InscricaoVO)objs.get(0)).setSelecionar(Boolean.TRUE);
			}
			setListaConsultaCandidato(objs);
		} catch (Exception e) {
			
		}
	}
	
	public void selecionarInscricaoMatricula() throws Exception {
		try {
			if (!getListaConsultaCandidato().isEmpty()) {
				if (getListaConsultaCandidato().size() > 0) {
					Iterator i = getListaConsultaCandidato().iterator();
					while (i.hasNext()) {
						InscricaoVO insc = (InscricaoVO) i.next();
						if (insc.getSelecionar()) {
							if (insc.getResultadoProcessoSeletivoVO().getCodigo().intValue() == 0) {
								throw new Exception(getMensagemInternalizacao("msg_matricula_resultadoProcSeletivoNaoEncontrado"));
							}else if(Uteis.isAtributoPreenchido(insc.getMatriculaVO().getMatricula()) && !insc.getMatriculaVO().getMatricula().equals(getMatriculaVO().getMatricula())) {
								getMatriculaVO().setInscricao(new InscricaoVO());
								throw new Exception("Inscrição utilizada para a matrícula de número "+ insc.getMatriculaVO().getMatricula() );
							} else {
								if (!insc.getResultadoProcessoSeletivoVO().isAprovadoProcessoSeletivo()) {
									throw new Exception(getMensagemInternalizacao("msg_matricula_naoAprovadoResultadoProcSeletivo"));
								} else {
									//getMatriculaVO().setFormaIngresso("PS");
									getMatriculaVO().setInscricao(insc);
									getFacadeFactory().getMatriculaFacade().realizarMontagemDadosProcSeletivoMatricula(getMatriculaVO(), new MatriculaPeriodoVO(), getUsuarioLogado());
									break;
								}
							}
						}
					}
				}
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarInscricao() throws Exception {
		InscricaoVO obj = (InscricaoVO) context().getExternalContext().getRequestMap().get("inscricaoItens");
		if (!getListaConsultaCandidato().isEmpty()) {			
			if (getListaConsultaCandidato().size() > 1) {
				Iterator i = getListaConsultaCandidato().iterator();
				while (i.hasNext()) {
					InscricaoVO insc = (InscricaoVO)i.next();
					if (obj.getCodigo().intValue() != insc.getCodigo().intValue() && obj.getSelecionar() && insc.getSelecionar()) {
						insc.setSelecionar(Boolean.FALSE);
					}
				}
			}
		}
	}

	public void alterarDataInicioConclusaoCursoAluno(List<MatriculaVO> listaMatriculaVO) throws Exception {
		for (MatriculaVO matriculaVO : listaMatriculaVO) {
			getFacadeFactory().getMatriculaFacade().alterarDadosAlteracoesCadastraisMatricula(matriculaVO, getUsuarioLogado());
		}
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
		if (unidadeEnsinoCursoVO == null) {
			unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCursoVO;
	}

	public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
		this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
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

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List getListaSelectItemCurso() {
		if (listaSelectItemCurso == null) {
			listaSelectItemCurso = new ArrayList(0);
		}
		return listaSelectItemCurso;
	}

	public void setListaSelectItemCurso(List listaSelectItemCurso) {
		this.listaSelectItemCurso = listaSelectItemCurso;
	}

	public List getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList(0);
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	public List getAlunosTurma() {
		if (alunosTurma == null) {
			alunosTurma = new ArrayList(0);
		}
		return alunosTurma;
	}

	public void setAlunosTurma(List alunosTurma) {
		this.alunosTurma = alunosTurma;
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

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public String getCampoConsultaEnade() {
		if (campoConsultaEnade == null) {
			campoConsultaEnade = "";
		}
		return campoConsultaEnade;
	}

	public void setCampoConsultaEnade(String campoConsultaEnade) {
		this.campoConsultaEnade = campoConsultaEnade;
	}

	public String getValorConsultaEnade() {
		return valorConsultaEnade;
	}

	public void setValorConsultaEnade(String valorConsultaEnade) {
		this.valorConsultaEnade = valorConsultaEnade;
	}

	public List getListaConsultaEnade() {
		return listaConsultaEnade;
	}

	public void setListaConsultaEnade(List listaConsultaEnade) {
		this.listaConsultaEnade = listaConsultaEnade;
	}

	public ControleLivroFolhaReciboVO getControleLivroFolhaReciboVO() {
		return controleLivroFolhaReciboVO;
	}

	public void setControleLivroFolhaReciboVO(ControleLivroFolhaReciboVO controleLivroFolhaReciboVO) {
		this.controleLivroFolhaReciboVO = controleLivroFolhaReciboVO;
	}

	public MatriculaControleLivroRegistroDiplomaVO getMatriculaControleLivroRegistroDiplomaVO() {
		if (matriculaControleLivroRegistroDiplomaVO == null) {
			matriculaControleLivroRegistroDiplomaVO = new MatriculaControleLivroRegistroDiplomaVO();
		}
		return matriculaControleLivroRegistroDiplomaVO;
	}

	public void setMatriculaControleLivroRegistroDiplomaVO(MatriculaControleLivroRegistroDiplomaVO matriculaControleLivroRegistroDiplomaVO) {
		this.matriculaControleLivroRegistroDiplomaVO = matriculaControleLivroRegistroDiplomaVO;
	}

	public List<MatriculaControleLivroRegistroDiplomaVO> getListaMatriculasControleLivroRegistroDiploma() {
		return listaMatriculasControleLivroRegistroDiploma;
	}

	public void setListaMatriculasControleLivroRegistroDiploma(List listaMatriculasControleLivroRegistroDiploma) {
		this.listaMatriculasControleLivroRegistroDiploma = listaMatriculasControleLivroRegistroDiploma;
	}

	/**
	 * @return the disciplinaVO
	 */
	public DisciplinaVO getDisciplinaVO() {
		return disciplinaVO;
	}

	/**
	 * @param disciplinaVO
	 *            the disciplinaVO to set
	 */
	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	/**
	 * @return the valorConsultaAluno
	 */
	public String getValorConsultaAluno() {
		return valorConsultaAluno;
	}

	/**
	 * @param valorConsultaAluno
	 *            the valorConsultaAluno to set
	 */
	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	/**
	 * @return the campoConsultaAluno
	 */
	public String getCampoConsultaAluno() {
		return campoConsultaAluno;
	}

	/**
	 * @param campoConsultaAluno
	 *            the campoConsultaAluno to set
	 */
	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	/**
	 * @return the listaConsultaAluno
	 */
	public List getListaConsultaAluno() {
		return listaConsultaAluno;
	}

	/**
	 * @param listaConsultaAluno
	 *            the listaConsultaAluno to set
	 */
	public void setListaConsultaAluno(List listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		return LISTA_SELECT_ITEM_TIPO_CONSULTA_ALUNO;
	}

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		return LISTA_SELECT_ITEM_TIPO_CONSULTA_DISCIPLINA;
	}

	public List<SelectItem> getListaSelectItemFormaIngresso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(FormaIngresso.class, true);
		Collections.sort(itens, new SelectItemOrdemValor());
		return itens;
	}

	/**
	 * @return the campoConsultaDisciplinaMatricula
	 */
	public String getCampoConsultaDisciplinaMatricula() {
		return campoConsultaDisciplinaMatricula;
	}

	/**
	 * @param campoConsultaDisciplinaMatricula
	 *            the campoConsultaDisciplinaMatricula to set
	 */
	public void setCampoConsultaDisciplinaMatricula(String campoConsultaDisciplinaMatricula) {
		this.campoConsultaDisciplinaMatricula = campoConsultaDisciplinaMatricula;
	}

	/**
	 * @return the valorConsultaDisciplinaMatricula
	 */
	public String getValorConsultaDisciplinaMatricula() {
		return valorConsultaDisciplinaMatricula;
	}

	/**
	 * @param valorConsultaDisciplinaMatricula
	 *            the valorConsultaDisciplinaMatricula to set
	 */
	public void setValorConsultaDisciplinaMatricula(String valorConsultaDisciplinaMatricula) {
		this.valorConsultaDisciplinaMatricula = valorConsultaDisciplinaMatricula;
	}

	/**
	 * @return the listaConsultaDisciplinaMatricula
	 */
	public List getListaConsultaDisciplinaMatricula() {
		return listaConsultaDisciplinaMatricula;
	}

	/**
	 * @param listaConsultaDisciplinaMatricula
	 *            the listaConsultaDisciplinaMatricula to set
	 */
	public void setListaConsultaDisciplinaMatricula(List listaConsultaDisciplinaMatricula) {
		this.listaConsultaDisciplinaMatricula = listaConsultaDisciplinaMatricula;
	}

	/**
	 * @return the valorConsultaData
	 */
	public Date getValorConsultaData() {
		if (valorConsultaData == null) {
			valorConsultaData = new Date();
		}
		return valorConsultaData;
	}

	/**
	 * @param valorConsultaData
	 *            the valorConsultaData to set
	 */
	public void setValorConsultaData(Date valorConsultaData) {
		this.valorConsultaData = valorConsultaData;
	}

	public Boolean getApresentarCampoData() {
		if (getCampoConsultaEnade().equals("dataPublicacaoPortariaDOU")) {
			return true;
		}
		return false;
	}

	public List<SelectItem> getListaSelectItemMes() {
		return MesAnoEnum.getListaSelectItemKeyMes();
	}
	
	public void montarDadosMatriculaEnade() throws Exception {
		MatriculaVO matriculaVO = (MatriculaVO) getRequestMap().get("matriculaItens");
		removerControleMemoriaFlashTela("MatriculaEnadeControle");
		context().getExternalContext().getSessionMap().put("matricula", matriculaVO);
	}
	
	public List<SelectItem> getListaSelectItemTipoTrabalhoConclusaoCurso() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoTrabalhoConclusaoCurso.class);
	}

	public List getListaConsultaCandidato() {
		if (listaConsultaCandidato == null) {
			listaConsultaCandidato = new ArrayList();
		}
		return listaConsultaCandidato;
	}

	public void setListaConsultaCandidato(List listaConsultaCandidato) {
		this.listaConsultaCandidato = listaConsultaCandidato;
	}
	
	public Boolean getIsApresentarCampoCodigoIntegracaoFinanceira() throws Exception {
		return getConfiguracaoFinanceiroPadraoSistema().getUtilizarIntegracaoFinanceira();
	}

	public void montarListaSelectItemReconhecimentoCurso(Integer curso) throws Exception {
		List<AutorizacaoCursoVO> listaAutorizacaoVOs = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCurso(curso, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		if (!listaAutorizacaoVOs.isEmpty()) {
			getListaSelectItemReconhecimentoVOs().clear();
			getListaSelectItemRenovacaoReconhecimentoVOs().clear();
			List<SelectItem> items = new ArrayList<SelectItem>(0);
			items.add(new SelectItem("", ""));
			for (AutorizacaoCursoVO autorizacaoCursoVO : listaAutorizacaoVOs) {
				items.add(new SelectItem(autorizacaoCursoVO.getCodigo(), autorizacaoCursoVO.getNome()));
			}
			getListaSelectItemReconhecimentoVOs().addAll(items);
			getListaSelectItemRenovacaoReconhecimentoVOs().addAll(items);
			items = null;
		}
		listaAutorizacaoVOs = null;
	} 

	public List<SelectItem> getListaSelectItemReconhecimentoVOs() {
		if (listaSelectItemReconhecimentoVOs == null) {
			listaSelectItemReconhecimentoVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemReconhecimentoVOs;
	}

	public void setListaSelectItemReconhecimentoVOs(List<SelectItem> listaSelectItemReconhecimentoVOs) {
		this.listaSelectItemReconhecimentoVOs = listaSelectItemReconhecimentoVOs;
	}

	public List<SelectItem> getListaSelectItemRenovacaoReconhecimentoVOs() {
		if (listaSelectItemRenovacaoReconhecimentoVOs == null) {
			listaSelectItemRenovacaoReconhecimentoVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemRenovacaoReconhecimentoVOs;
	}

	public void setListaSelectItemRenovacaoReconhecimentoVOs(List<SelectItem> listaSelectItemRenovacaoReconhecimentoVOs) {
		this.listaSelectItemRenovacaoReconhecimentoVOs = listaSelectItemRenovacaoReconhecimentoVOs;
	}
	
	public Boolean getTemProgramacaoFormaturaAluno() {
		if (temProgramacaoFormaturaAluno == null) {
			temProgramacaoFormaturaAluno = Boolean.FALSE;
		}
		return temProgramacaoFormaturaAluno;
	}
	
	public void setTemProgramacaoFormaturaAluno(Boolean temProgramacaoFormaturaAluno) {
		this.temProgramacaoFormaturaAluno = temProgramacaoFormaturaAluno;
	}

	public void excluirProgramacaoFormaturaPorMatricula() {
		try {
			if (!getPermitirExclusaoRegistroCobranca()) {
				return;
			}
			ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO = getFacadeFactory().getProgramacaoFormaturaAlunoFacade().consultarColacaoPorMatricula(getMatriculaVO().getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getFacadeFactory().getProgramacaoFormaturaAlunoFacade().excluir(programacaoFormaturaAlunoVO);
			consultarAlunosPorMatricula();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Boolean getPermitirExclusaoRegistroCobranca() {
		if (permitirExclusaoRegistroCobranca == null) {
			try {
				ControleAcesso.excluir("ProgramacaoFormatura", getUsuarioLogado());
				permitirExclusaoRegistroCobranca = true;
			} catch (Exception e) {
				permitirExclusaoRegistroCobranca = false;
			}
		}
		return permitirExclusaoRegistroCobranca;
	}

	public Boolean getPermitirAlterarInformacoesReajustePreco() {
		if (permitirAlterarInformacoesReajustePreco == null) {
			permitirAlterarInformacoesReajustePreco = Boolean.FALSE;
		}
		return permitirAlterarInformacoesReajustePreco;
	}

	public void setPermitirAlterarInformacoesReajustePreco(Boolean permitirAlterarInformacoesReajustePreco) {
		this.permitirAlterarInformacoesReajustePreco = permitirAlterarInformacoesReajustePreco;
	}
	
	private Boolean abrirModalMotivoCancelamentoReajustePreco;
	
	public void verificarAlteracaoReajustePreco() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		if (!obj.getPermiteExecucaoReajustePreco()) {
			setAbrirModalMotivoCancelamentoReajustePreco(true);
			obj.setResponsavelCancelamentoReajustePreco(getUsuarioLogadoClone());
			obj.setDataCancelamentoReajustePreco(new Date());
			setMatriculaVO(obj);
		} else {
			getMatriculaVO().setDataCancelamentoReajustePreco(null);
			getMatriculaVO().setResponsavelCancelamentoReajustePreco(null);
		}
	}
	
	public String getAbrirModalMotivoCancelamentoReajuste() {
		if (getAbrirModalMotivoCancelamentoReajustePreco()) {
			return "RichFaces.$('panelMotivoCancelamentoReajustePreco').show()";
		}
		return "";
	}
	
	public String getFecharModalMotivoCancelamentoReajuste() {
		if (!getAbrirModalMotivoCancelamentoReajustePreco()) {
			return "RichFaces.$('panelMotivoCancelamentoReajustePreco').hide()";
		}
		return "";
	}

	public Boolean getAbrirModalMotivoCancelamentoReajustePreco() {
		if (abrirModalMotivoCancelamentoReajustePreco == null) {
			abrirModalMotivoCancelamentoReajustePreco = false;
		}
		return abrirModalMotivoCancelamentoReajustePreco;
	}

	public void setAbrirModalMotivoCancelamentoReajustePreco(Boolean abrirModalMotivoCancelamentoReajustePreco) {
		this.abrirModalMotivoCancelamentoReajustePreco = abrirModalMotivoCancelamentoReajustePreco;
	}
	
	public void cancelarIndiceReajustePreco() {
		try {
			getFacadeFactory().getMatriculaFacade().cancelarIndiceReajustePreco(getMatriculaVO(), getUsuarioLogado());
			setAbrirModalMotivoCancelamentoReajustePreco(false);
			setMensagemID("msg_cancelar_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void fecharModalReajustePreco() {
		setAbrirModalMotivoCancelamentoReajustePreco(false);
		getMatriculaVO().setPermiteExecucaoReajustePreco(true);
	}
	
	public void verificarPermiteAlterarDataBaseGeracaoParcelas() {
		try {
			if (getFacadeFactory().getMatriculaPeriodoVencimentoFacade().consultarExisteMatriculaPeriodoVencimentoGeradaPagaOuNegociada(getMatriculaVO().getUltimoMatriculaPeriodoVO().getCodigo(), false, getUsuarioLogado())) {
				permiteAlterarDataBaseGeracaoParcelas = false;
			} else {
				permiteAlterarDataBaseGeracaoParcelas = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Boolean getPermiteAlterarDataBaseGeracaoParcelas() {
		if (permiteAlterarDataBaseGeracaoParcelas == null) {
			permiteAlterarDataBaseGeracaoParcelas = true;
		}
		return permiteAlterarDataBaseGeracaoParcelas;
	}

	public void setPermiteAlterarDataBaseGeracaoParcelas(Boolean permiteAlterarDataBaseGeracaoParcelas) {
		this.permiteAlterarDataBaseGeracaoParcelas = permiteAlterarDataBaseGeracaoParcelas;
	}
	
	public List<SelectItem> getListaSelectItemMesIngreso() {
		if (listaSelectItemMesIngreso == null) {
			listaSelectItemMesIngreso = new ArrayList<>();
			listaSelectItemMesIngreso.addAll(MesAnoEnum.getListaSelectItemDescricaoMes());
		}
		return listaSelectItemMesIngreso;
	}

	
	public void setListaSelectItemMesIngreso(List<SelectItem> listaSelectItemMesIngreso) {
		this.listaSelectItemMesIngreso = listaSelectItemMesIngreso;
	}
	
	/**
	 * @return the listaSelectItemFormacaoAcademica
	 */
	public List<SelectItem> getListaSelectItemFormacaoAcademica() {
		if (listaSelectItemFormacaoAcademica == null) {
			listaSelectItemFormacaoAcademica = new ArrayList<SelectItem>();
		}
		return listaSelectItemFormacaoAcademica;
	}

	/**
	 * @param listaSelectItemFormacaoAcademica the listaSelectItemFormacaoAcademica to set
	 */
	public void setListaSelectItemFormacaoAcademica(List<SelectItem> listaSelectItemFormacaoAcademica) {
		this.listaSelectItemFormacaoAcademica = listaSelectItemFormacaoAcademica;
	}

	public void montarListaSelectItemFormacaoAcademica(MatriculaVO matriculaVO) {
		try {
			List<FormacaoAcademicaVO> lista = getFacadeFactory().getFormacaoAcademicaFacade().consultarFormacaoAcademicaoMaisAtual(matriculaVO.getAluno().getCodigo(), getUsuarioLogado(), 0);
			setListaSelectItemFormacaoAcademica(new ArrayList(0));
			Iterator i = lista.iterator();
			getListaSelectItemFormacaoAcademica().add(new SelectItem(0, ""));
			while (i.hasNext()) {
				FormacaoAcademicaVO formacaoAcademicaVO = (FormacaoAcademicaVO) i.next();
				getListaSelectItemFormacaoAcademica().add(new SelectItem(formacaoAcademicaVO.getCodigo(), formacaoAcademicaVO.getEscolaridade_Apresentar() + " - "  + formacaoAcademicaVO.getCurso() + " - "  + formacaoAcademicaVO.getInstituicao()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	public void alterarCampoAnoSemestreMesIngresso() {		
		MatriculaVO matricula = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");		
		matricula.setAnoIngresso(String.valueOf(Uteis.getAnoData(matricula.getDataInicioCurso())));
		matricula.setSemestreIngresso(String.valueOf(Uteis.getSemestreData(matricula.getDataInicioCurso())));
		matricula.setMesIngresso(String.valueOf(getListaSelectItemMesIngreso().get(Uteis.getMesData(matricula.getDataInicioCurso())).getValue()));
		
	}	
	
	public List<SelectItem> getListaSelectItemDiaSemanaAula() {
		if(listaSelectItemDiaSemanaAula == null) {
			listaSelectItemDiaSemanaAula = UtilSelectItem.getListaSelectItemEnum(DiaSemana.values(), Obrigatorio.SIM);
		}
		return listaSelectItemDiaSemanaAula;
	}

	public void setListaSelectItemDiaSemanaAula(List<SelectItem> listaSelectItemDiaSemanaAula) {
		this.listaSelectItemDiaSemanaAula = listaSelectItemDiaSemanaAula;
	}

	public List<SelectItem> getListaSelectItemTurnoAula() {
		if(listaSelectItemTurnoAula == null) {
			listaSelectItemTurnoAula = UtilSelectItem.getListaSelectItemEnum(NomeTurnoCensoEnum.values(), Obrigatorio.SIM);
		}
		return listaSelectItemTurnoAula;
	}

	public void setListaSelectItemTurnoAula(List<SelectItem> listaSelectItemTurnoAula) {
		this.listaSelectItemTurnoAula = listaSelectItemTurnoAula;
	}

	public Map<String, String> getMapFinanciamentoEstudantil() {
		if (mapFinanciamentoEstudantil == null) {
			montarHashMapFinanciamentoEstudantil();
		}
		return mapFinanciamentoEstudantil;
	}

	public void setMapFinanciamentoEstudantil(Map<String, String> mapFinanciamentoEstudantil) {
		this.mapFinanciamentoEstudantil = mapFinanciamentoEstudantil;
	}

	private void montarHashMapFinanciamentoEstudantil() {
		Stream<FinanciamentoEstudantil> financiamentoEstudantis = Stream.of(FinanciamentoEstudantil.values());
		mapFinanciamentoEstudantil = financiamentoEstudantis
				.sorted(Comparator.comparing(FinanciamentoEstudantil::getDescricao))
				.collect(Collectors.toMap(FinanciamentoEstudantil::getDescricao, FinanciamentoEstudantil::getValor, (a, b) -> a, LinkedHashMap::new));
	}

	public List<SelectItem> getListaSelectItemJustificativaCenso() {
		if (listaSelectItemJustificativaCenso == null) {
			listaSelectItemJustificativaCenso = JustificativaCensoEnum.getListaSelectItemJustificativaCensoEnum();
		}
		return listaSelectItemJustificativaCenso;
	}

	public void setListaSelectItemJustificativaCenso(List<SelectItem> listaSelectItemJustificativaCenso) {
		this.listaSelectItemJustificativaCenso = listaSelectItemJustificativaCenso;
	}

	public List<SelectItem> getListaSelectItemTipoMobilidadeAcademicaEnum() {
		if (listaSelectItemTipoMobilidadeAcademicaEnum == null) {
			listaSelectItemTipoMobilidadeAcademicaEnum = TipoMobilidadeAcademicaEnum.getListaSelectItemTipoMobilidadeAcademicaEnum();
		}
		return listaSelectItemTipoMobilidadeAcademicaEnum;
	}

	public void setListaSelectItemTipoMobilidadeAcademicaEnum(List<SelectItem> listaSelectItemTipoMobilidadeAcademicaEnum) {
		this.listaSelectItemTipoMobilidadeAcademicaEnum = listaSelectItemTipoMobilidadeAcademicaEnum;
	}

	public List getAlunosTurmaAntiga() {
		if (alunosTurmaAntiga == null) {
			alunosTurmaAntiga = new ArrayList();
		}
		return alunosTurmaAntiga;
	}

	public void setAlunosTurmaAntiga(List alunosTurmaAntiga) {
		this.alunosTurmaAntiga = alunosTurmaAntiga;
	}
}
