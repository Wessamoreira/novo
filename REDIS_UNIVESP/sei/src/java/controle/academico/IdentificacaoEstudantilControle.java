package controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CarteirinhaEstudantilVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.IdentificacaoEstudantilVO;
import negocio.comuns.academico.LocalPeriodoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.basico.enumeradores.ModuloLayoutEtiquetaEnum;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.SituacaoRequerimento;
import negocio.comuns.utilitarias.dominios.TiposRequerimento;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Controller("IdentificacaoEstudantilControle")
@Scope("viewScope")
public class IdentificacaoEstudantilControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private String tipoPessoa;
	private RequerimentoVO requerimento;
	private List<CursoVO> listaConsultaCurso;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private CursoVO cursoVO;
	private List<TurmaVO> listaConsultaTurma;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private TurmaVO turmaVO;
	private List<DisciplinaVO> listaConsultaDisciplina;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private DisciplinaVO disciplina;
	private String ano;
	private String semestre;
	private Date validadeCarteirinha;
	private List<FuncionarioVO> listaConsultaProfessor;
	private String campoConsultaProfessor;
	private String valorConsultaProfessor;
	private FuncionarioVO funcionarioVO;
	private List<SelectItem> tipoConsultaComboProfessor;
	private List<SelectItem> tipoLayoutCarteirinha;
	private String layoutCarteirinha;
	private List<IdentificacaoEstudantilVO> listaIdentificacaoEstudantilVOs;
	private IdentificacaoEstudantilVO identificacaoEstudantilVO;
	private CarteirinhaEstudantilVO carteirinhaEstudantilVO;
	private LocalPeriodoVO localPeriodoVO;
	private LocalPeriodoVO localPeriodoHeaderVO;
	private String orgaoResponsavelEmissaoCarteirinha;
	private Boolean apenasRequerimentoPago;
	private Boolean apresentarDataValidadeCarteirinha;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private List<MatriculaVO> listaConsultaAluno;
	private MatriculaVO matriculaVO;
	private String utilizarFotoAluno;
	private String tituloCracha;
	private LayoutEtiquetaVO layoutEtiqueta;

	@PostConstruct
	public void novo() {
		try {
			verificarUnidadeEnsinoLogada();
			setListaIdentificacaoEstudantilVOs(null);
			setCursoVO(null);
			setTituloCracha(null);
			setTurmaVO(null);
			setDisciplina(null);
			setAno("");
			setSemestre("");
			setOrgaoResponsavelEmissaoCarteirinha("");
			setUtilizarFotoAluno("");
			setMatriculaVO(null);
			setUnidadeEnsinoVO(null);
			setCarteirinhaEstudantilVO(null);
			setTipoPessoa("AL");
			setRequerimento(new RequerimentoVO());
			setLocalPeriodoHeaderVO(new LocalPeriodoVO());
			String requerimento = (String) ((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("requerimento");
			setValidadeCarteirinha(null);
			setApresentarDataValidadeCarteirinha(null);
			setTipoLayoutCarteirinha(null);
			setOrgaoResponsavelEmissaoCarteirinha(null);
			setApenasRequerimentoPago(null);
			if (requerimento != null && !requerimento.trim().isEmpty()) {
				getRequerimento().setCodigo(Integer.valueOf(requerimento));
				consultarRequerimentoPorChavePrimaria();
			}
			montarListaSelectItemLayoutEtiquetaIdentificacaoEstudantil();
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setListaConsultaCurso(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarUnidadeEnsinoLogada() throws Exception {
		if (getUnidadeEnsinoLogado().getCodigo() != 0l) {
			getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
			getUnidadeEnsinoVO().setNome(getUnidadeEnsinoLogado().getNome());
			montarListaSelectItemUnidadeEnsino(getUnidadeEnsinoVO().getNome());
		} else {
			montarListaSelectItemUnidadeEnsino();
		}
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
		setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome", !Uteis.isAtributoPreenchido(getUnidadeEnsinoLogado().getCodigo())));
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, getUsuarioLogado());
	}

	public void consultarCurso() {
		try {
			if (!Uteis.isAtributoPreenchido(getUnidadeEnsinoVO())) {
				throw new Exception(UteisJSF.internacionalizar("msg_campoUnidadeEnsinoDeveSerInformado"));
			}
			setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultar(getCampoConsultaCurso(), getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() {
		try {
			setCursoVO((CursoVO) context().getExternalContext().getRequestMap().get("cursoItens"));
			getListaConsultaCurso().clear();
			setTurmaVO(new TurmaVO());
			setListaIdentificacaoEstudantilVOs(null);
			setValorConsultaCurso("");
			setCampoConsultaCurso("");
			setListaConsultaCurso(null);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Requerimento</code> por meio de sua respectiva chave primária. Esta rotina é
	 * utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária da entidade montando automaticamente o resultado da
	 * consulta para apresentação.
	 */
	public void consultarRequerimentoPorChavePrimaria() {
		try {
			if (Uteis.isAtributoPreenchido(getRequerimento().getCodigo())) {
				setRequerimento(getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimariaFiltrandoPorUnidadeEnsino(getRequerimento().getCodigo(), "", getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema()));
				getUnidadeEnsinoVO().setCodigo(requerimento.getUnidadeEnsino().getCodigo());
				validarSituacaoRequerimento();
				setListaIdentificacaoEstudantilVOs(getFacadeFactory().getIdentificacaoEstudantilFacade().consultarDadosIdentificacaoEstudantilAluno(getUnidadeEnsinoVO().getCodigo(), getRequerimento().getCodigo(), 0, 0, 0, "", "", getApenasRequerimentoPago(), getMatriculaVO().getMatricula(), getUtilizarFotoAluno().equals("fotoPerfil"), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema()));
				setMensagemID("msg_dados_consultados");
			} else {
				limparMensagem();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarRequerimento() {
		try {

			if (getCampoConsultaRequerimento().equals("codigo")) {
				int valorInt = Uteis.getValorInteiro(getValorConsultaRequerimento());
				setListaConsultaRequerimento(getFacadeFactory().getRequerimentoFacade().consultarPorCodigo(new Integer(valorInt), getTipoRequerimento(), getUnidadeEnsinoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema()));
			}
			if (getCampoConsultaRequerimento().equals("data")) {
				Date valorData = Uteis.getDate(getValorConsultaRequerimento());
				setListaConsultaRequerimento(getFacadeFactory().getRequerimentoFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getTipoRequerimento(), getUnidadeEnsinoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema()));
			}
			if (getCampoConsultaRequerimento().equals("nomeTipoRequerimento")) {
				setListaConsultaRequerimento(getFacadeFactory().getRequerimentoFacade().consultarPorNomeTipoRequerimento(getValorConsultaRequerimento(), getUnidadeEnsinoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema()));
			}
			if (getCampoConsultaRequerimento().equals("situacao")) {
				setListaConsultaRequerimento(getFacadeFactory().getRequerimentoFacade().consultarPorSituacao(getValorConsultaRequerimento(), getTipoRequerimento(), getUnidadeEnsinoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema()));
			}
			if (getCampoConsultaRequerimento().equals("situacaoFinanceira")) {
				setListaConsultaRequerimento(getFacadeFactory().getRequerimentoFacade().consultarPorSituacaoFinanceira(getValorConsultaRequerimento(), getTipoRequerimento(), getUnidadeEnsinoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema()));
			}
			if (getCampoConsultaRequerimento().equals("nomePessoa")) {
				setListaConsultaRequerimento(getFacadeFactory().getRequerimentoFacade().consultarPorNomePessoa(getValorConsultaRequerimento(), getTipoRequerimento(), getUnidadeEnsinoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema()));
			}
			if (getCampoConsultaRequerimento().equals("cpfPessoa")) {
				setListaConsultaRequerimento(getFacadeFactory().getRequerimentoFacade().consultarPorNomeCPFPessoa(getValorConsultaRequerimento(), getTipoRequerimento(), getUnidadeEnsinoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema()));
			}
			if (getCampoConsultaRequerimento().equals("matriculaMatricula")) {
				setListaConsultaRequerimento(getFacadeFactory().getRequerimentoFacade().consultarPorMatriculaMatricula(getValorConsultaRequerimento(), getTipoRequerimento(), getUnidadeEnsinoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaRequerimento(new ArrayList<RequerimentoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void validarSituacaoRequerimento() throws Exception {
		if (!getRequerimento().getTipoRequerimento().getTipo().equals(TiposRequerimento.CARTEIRINHA_ESTUDANTIL.getValor())) {
			throw new Exception("Requerimento especificado deve ser do tipo CARTEIRINHA ESTUDANTIL.");
		}
		if (getRequerimento().getSituacao().equals(SituacaoRequerimento.AGUARDANDO_PAGAMENTO.getValor())) {
			throw new Exception("Requerimento especificado está aguardando pagamento.");
		}
		if (getRequerimento().getMatricula().getSituacao().equals("CA")) {
			throw new Exception("Matrícula especificada já está cancelada.");
		}
		if (getRequerimento().getSituacao().equals(SituacaoRequerimento.FINALIZADO_INDEFERIDO.getValor()) || getRequerimento().getSituacao().equals(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor())) {
			throw new Exception("Requerimento especificado já está finalizado.");
		}
	}

	public void selecionarRequerimento() {
		try {
			getListaIdentificacaoEstudantilVOs().clear();
			setRequerimento((RequerimentoVO) context().getExternalContext().getRequestMap().get("requerimentoItens"));
			validarSituacaoRequerimento();
			setCursoVO(getRequerimento().getMatricula().getCurso());
			setTurmaVO(getRequerimento().getTurma());
			setUnidadeEnsinoVO(getRequerimento().getUnidadeEnsino());
			setMatriculaVO(getRequerimento().getMatricula());
			setTurmaVO(getRequerimento().getTurma());
            getRequerimento().getMatricula(); 
			setListaIdentificacaoEstudantilVOs(getFacadeFactory().getIdentificacaoEstudantilFacade().consultarDadosIdentificacaoEstudantilAluno(getUnidadeEnsinoVO().getCodigo(), getRequerimento().getCodigo(), null, null, null, "", "", getApenasRequerimentoPago(), getMatriculaVO().getMatricula(), getUtilizarFotoAluno().equals("fotoPerfil"), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema()));
			setListaConsultaRequerimento(null);
			setValorConsultaRequerimento(null);
			setCampoConsultaRequerimento(null);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setRequerimento(new RequerimentoVO());
		}
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemTipoPessoa() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("AL", "Aluno"));
		itens.add(new SelectItem("PR", "Professor"));
		return itens;
	}

	public List<SelectItem> getComboOrgaoResponsavelEmissaoCarteirinha() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("CARIMBO CODEP", "CARIMBO CODEP"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public void consultarTurma() {
		try {
			if (!Uteis.isAtributoPreenchido(getUnidadeEnsinoVO())) {
				throw new Exception(UteisJSF.internacionalizar("msg_campoUnidadeEnsinoDeveSerInformado"));
			}
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnidadeEnsinoCurso(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultarPorNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() throws Exception {
		setTurmaVO((TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens"));
		setCursoVO(getTurmaVO().getCurso());
		setListaIdentificacaoEstudantilVOs(null);
		setListaConsultaTurma(null);
		valorConsultaTurma = "";
		campoConsultaTurma = "";
	}

	public void consultarProfessor() {
		try {
		 	if (!Uteis.isAtributoPreenchido(getUnidadeEnsinoVO())) {
				throw new Exception(UteisJSF.internacionalizar("msg_campoUnidadeEnsinoDeveSerInformado"));
			}
			if (!Uteis.isAtributoPreenchido(getCursoVO())) {
				throw new Exception(UteisJSF.internacionalizar("msg_SolicitacaoAberturaTurma_unidadeEnsinoCurso"));
			}

			if (isApresentarCampoAno() && getAno().trim().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_ano"));
			}
			if (isApresentarCampoSemestre() && getSemestre().trim().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_semestre"));
			} 

			if (getCampoConsultaProfessor().equals("nome")) {
				setListaConsultaProfessor(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeProfessorTitularDisciplinaTurma(getValorConsultaProfessor(), getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), null, null, getSemestre(), getAno(), null, true, false, getUsuarioLogado()));
			}
			if (getCampoConsultaProfessor().equals("matricula")) {
				setListaConsultaProfessor(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatriculaProfessorTitularDisciplinaTurma(getValorConsultaProfessor(), getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), getSemestre(), getAno(), null, true, false, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaProfessor(new ArrayList<FuncionarioVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarProfessor() throws Exception {
		setFuncionarioVO((FuncionarioVO) context().getExternalContext().getRequestMap().get("professorItens"));
		getFacadeFactory().getFuncionarioFacade().carregarDados(getFuncionarioVO(), getUsuarioLogado());
		montarListaSelectItemUnidadeEnsino();
		setValorConsultaProfessor("");
		setCampoConsultaProfessor("");
		getListaConsultaProfessor().clear();
	}

	public Boolean getIsApresentarDataValidadeCarteirinha() throws Exception {
		return getConfiguracaoGeralPadraoSistema().getControlarValidadeCarteirinhaEstudantil();
	}

	public void consultarAlunos() {
		try {
			if (!Uteis.isAtributoPreenchido(getUnidadeEnsinoVO())) {
				throw new Exception(UteisJSF.internacionalizar("msg_campoUnidadeEnsinoDeveSerInformado"));
			}
			if (!Uteis.isAtributoPreenchido(getCursoVO())) {
				throw new Exception(UteisJSF.internacionalizar("msg_SolicitacaoAberturaTurma_unidadeEnsinoCurso"));
			}
			if (!Uteis.isAtributoPreenchido(getTurmaVO())) {
				throw new Exception(UteisJSF.internacionalizar("msg_SolicitacaoAberturaTurma_turma"));
			}
			if (isApresentarCampoAno() && getAno().trim().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_ano"));
			}
			if (isApresentarCampoSemestre() && getSemestre().trim().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_semestre"));
			}
			if (getApresentarDataValidadeCarteirinha() && !Uteis.isAtributoPreenchido(getValidadeCarteirinha())) {
				throw new Exception(UteisJSF.internacionalizar("msg_campoDataValidadeCarteirinhaDeveSerInformado"));
			}
			if (!Uteis.isAtributoPreenchido(getLayoutCarteirinha())) {
				throw new Exception(UteisJSF.internacionalizar("msg_campoLayoutCarteirinhaDeveSerInformado"));
			}
			setListaIdentificacaoEstudantilVOs(getFacadeFactory().getIdentificacaoEstudantilFacade().consultarDadosIdentificacaoEstudantilAluno(getUnidadeEnsinoVO().getCodigo(), getRequerimento().getCodigo(), getCursoVO().getCodigo(), getTurmaVO().getCodigo(), getDisciplina().getCodigo(), getAno(), getSemestre(), getApenasRequerimentoPago(), getMatriculaVO().getMatricula(), getUtilizarFotoAluno().equals("fotoPerfil"), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
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

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public Boolean getIsAprensetarCamposAluno() {
		return getTipoPessoa().equals("AL");
	}

	public List<SelectItem> getTipoConsultaComboProfessor() {
		if (tipoConsultaComboProfessor == null) {
			tipoConsultaComboProfessor = new ArrayList<SelectItem>();
			tipoConsultaComboProfessor.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboProfessor.add(new SelectItem("matricula", "Matrícula"));
		}
		return tipoConsultaComboProfessor;
	}


	public void consultarDisciplina() {
		try {
			if (getCampoConsultaDisciplina().equals("codigo")) {
				if (getValorConsultaDisciplina().equals("")) {
					setValorConsultaDisciplina("0");
				}
				setListaConsultaDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorCodigoDisciplinaUnidadeEnsinoCodigoCursoCodigoTurma(Integer.parseInt(getValorConsultaDisciplina()), getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				setListaConsultaDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorNomeDisciplinaUnidadeEnsinoCodigoCursoCodigoTurma(getValorConsultaDisciplina(), getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<DisciplinaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarDisciplina() {
		try {
			setDisciplina((DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens"));
			setListaIdentificacaoEstudantilVOs(null);
			setValorConsultaDisciplina(null);
			setCampoConsultaDisciplina(null);
			setListaConsultaDisciplina(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDisciplina() {
		setDisciplina(null);
		setValorConsultaDisciplina(null);
		setCampoConsultaDisciplina(null);
		setListaIdentificacaoEstudantilVOs(null);
	}

	public void limparDadosTurma() throws Exception {
		try {
			setTurmaVO(null);
			setListaConsultaTurma(null);
			setListaConsultaCurso(null);
		} catch (Exception e) {
		}
	}
	
	public void limparDadosCurso() throws Exception {
		try {
			setCursoVO(null);
		} catch (Exception e) {
		}
	}
	
	public void imprimirPDFLayout1() {
		try {
			List<IdentificacaoEstudantilVO> identificacaoEstudantilVOs = realizarSeparacaoCarteirinhasRelatorio();	
            validarPessoaSelecionadaListaGeracaoCrachaCarteirinha(identificacaoEstudantilVOs);
			// montarListaObjetosRelatorio();
			if (getIsAprensetarCamposAluno()) {
				if (!identificacaoEstudantilVOs.isEmpty()) {
					getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getIdentificacaoEstudantilFacade().designIReportRelatorioLayout1());
					getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
					getSuperParametroRelVO().setTituloRelatorio("ALUNO");
					getSuperParametroRelVO().adicionarParametro("logoPadraoRelatorio", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorioCracha.png");
					getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getIdentificacaoEstudantilFacade().caminhoBaseRelatorio());
					getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
					getSuperParametroRelVO().setListaObjetos(identificacaoEstudantilVOs);
					getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getIdentificacaoEstudantilFacade().caminhoBaseRelatorio());
					getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
					getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
					getSuperParametroRelVO().setCurso(getCursoVO().getNome());
					getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
					getSuperParametroRelVO().adicionarParametro("tituloCracha", getTituloCracha());
					realizarImpressaoRelatorio();
					setMensagemID("msg_relatorio_ok");
				} else {
					setMensagemID("msg_relatorio_sem_dados");
				}
			} else {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getIdentificacaoEstudantilFacade().designIReportRelatorioLayout1());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().adicionarParametro("logoPadraoRelatorio", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorioCracha.png");
				getSuperParametroRelVO().setTituloRelatorio("PROFESSOR");
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getIdentificacaoEstudantilFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setListaObjetos(identificacaoEstudantilVOs);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getIdentificacaoEstudantilFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
				getSuperParametroRelVO().setCurso(getCursoVO().getNome());
				getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
				getSuperParametroRelVO().adicionarParametro("tituloCracha", getTituloCracha());
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			getSuperParametroRelVO().adicionarParametro("logoPadraoRelatorio", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png");
		}
	}

	public void imprimirPDFLayout2() {
		try {
			List<IdentificacaoEstudantilVO> identificacaoEstudantilVOs = realizarSeparacaoCarteirinhasRelatorio();
			validarPessoaSelecionadaListaGeracaoCrachaCarteirinha(identificacaoEstudantilVOs);			
			if (!identificacaoEstudantilVOs.isEmpty()) {
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				if (getLayoutCarteirinha().equals("Layout3")) {
					getSuperParametroRelVO().adicionarParametro("logoPadraoRelatorioIdentificacaoEstudantil", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "layoutCarteiraDeEstudante.png");
					getSuperParametroRelVO().adicionarParametro("logoPadraoRelatorio", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png");
					getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getIdentificacaoEstudantilFacade().designIReportRelatorioLayout3());
				} else if (getLayoutCarteirinha().equals("Layout2")) {
					getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getIdentificacaoEstudantilFacade().designIReportRelatorioLayout2());
					getSuperParametroRelVO().adicionarParametro("logoPadraoRelatorioIdentificacaoEstudantil", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png");
					getSuperParametroRelVO().adicionarParametro("logoPadraoRelatorio", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png");
				}
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getIdentificacaoEstudantilFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setListaObjetos(identificacaoEstudantilVOs);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getIdentificacaoEstudantilFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
				getSuperParametroRelVO().setCurso(getCursoVO().getNome());
				getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
				getSuperParametroRelVO().adicionarParametro("apresentarDataValidadeCarteirinha", getApresentarDataValidadeCarteirinha());
				realizarImpressaoRelatorio();
				// removerObjetoMemoria(this);
				// verificarUnidadeEnsinoLogada();
				// getListaIdentificacaoEstudantilVOs().clear();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			getSuperParametroRelVO().adicionarParametro("logoPadraoRelatorio", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png");
		}
	}

	public void imprimirPDFLayoutPersonalizado() {
		List<IdentificacaoEstudantilVO> identificacaoEstudantilVOs;
		try {
			identificacaoEstudantilVOs = realizarSeparacaoCarteirinhasRelatorio();
			validarPessoaSelecionadaListaGeracaoCrachaCarteirinha(identificacaoEstudantilVOs);
			if (!identificacaoEstudantilVOs.isEmpty()) {
				setLayoutEtiqueta(getFacadeFactory().getLayoutEtiquetaFacade().consultarPorChavePrimaria(Integer.parseInt(layoutCarteirinha), Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
				if(getLayoutEtiqueta().getCodigo() != null && getLayoutEtiqueta().getCodigo() != 0){
					getFacadeFactory().getIdentificacaoEstudantilFacade().realizarMotagemIdentificacaoPDF(getLayoutEtiqueta(), identificacaoEstudantilVOs, getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb());
					
					setCaminhoRelatorio(layoutEtiqueta.getModuloLayoutEtiqueta().name() +".pdf");
					setFazerDownload(true);
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setFazerDownload(false);
		}
	}

	public List<IdentificacaoEstudantilVO> realizarSeparacaoCarteirinhasRelatorio() throws Exception {
		List<IdentificacaoEstudantilVO> identificacaoEstudantilFinalVOs = new ArrayList<IdentificacaoEstudantilVO>(0);
		if (getTipoPessoa().equals("AL") && Uteis.isAtributoPreenchido(getListaIdentificacaoEstudantilVOs())) {
			for (IdentificacaoEstudantilVO obj : getListaIdentificacaoEstudantilVOs()) {
				if (obj.getSelecionar()) {
					obj.setTipoPessoa(getTipoPessoa());
					obj.setOrgaoResponsavelEmissaoCarteirinha(getOrgaoResponsavelEmissaoCarteirinha());
					obj.setTituloDaIdentificacao(getTituloCracha());
					if (getValidadeCarteirinha() != null) {
						if (getLayoutCarteirinha().equals("Layout3") && getApresentarDataValidadeCarteirinha()) {
							obj.setDataVencimento(MesAnoEnum.getMesData(getValidadeCarteirinha()).getMesAbreviado().toUpperCase() + "/" + Uteis.getData(getValidadeCarteirinha(), "yyyy"));
						} else {
							obj.setDataVencimento(Uteis.getDataAno4Digitos(getValidadeCarteirinha()));
						}
					}
					inicializarLocalPeriodoHeader(obj);
					identificacaoEstudantilFinalVOs.add(obj);
				}
			}
		} else {
			if (getTipoPessoa().equals("PR") && Uteis.isAtributoPreenchido(getListaIdentificacaoEstudantilVOs())) {
				for (IdentificacaoEstudantilVO obj : getListaIdentificacaoEstudantilVOs()) {
					if (obj.getSelecionar()) {
						obj.setOrgaoResponsavelEmissaoCarteirinha(getOrgaoResponsavelEmissaoCarteirinha());
						obj.setTituloDaIdentificacao(getTituloCracha());
						obj.setTipoPessoa(getTipoPessoa());
						if (getValidadeCarteirinha() != null) {
							if (getLayoutCarteirinha().equals("Layout3")) {
								obj.setDataVencimento(MesAnoEnum.getMesData(getValidadeCarteirinha()).getMesAbreviado().toUpperCase() + "/" + Uteis.getData(getValidadeCarteirinha(), "yy"));
							} else {
								obj.setDataVencimento(Uteis.getDataAno4Digitos(getValidadeCarteirinha()));
							}
						}
						inicializarLocalPeriodoHeader(obj);
						identificacaoEstudantilFinalVOs.add(obj);
					}
				}
			}
		}
		return identificacaoEstudantilFinalVOs;
	}

	public void inicializarLocalPeriodoHeader(IdentificacaoEstudantilVO obj) {
		if (obj.getListaLocalPeriodo().isEmpty()) {
			int i = 0;
			while (i < 10) {
				LocalPeriodoVO localPeriodoVO = new LocalPeriodoVO();
				localPeriodoVO.setLocal(getLocalPeriodoHeaderVO().getLocal());
				localPeriodoVO.setPeriodoInicio(getLocalPeriodoHeaderVO().getPeriodoInicio());
				localPeriodoVO.setPeriodoFim(getLocalPeriodoHeaderVO().getPeriodoFim());
				obj.getListaLocalPeriodo().add(localPeriodoVO);
				i++;
			}
		}
	}

	public void adicionarLocalPeriodo() {
		try {
			if (getIdentificacaoEstudantilVO().getListaLocalPeriodo().size() == 10) {
				throw new Exception("Não é possível adicionar mais de 10 locais para o mesmo crachá");
			}
			LocalPeriodoVO obj = new LocalPeriodoVO();
			obj.setLocal(getLocalPeriodoVO().getLocal());
			obj.setPeriodoFim(getLocalPeriodoVO().getPeriodoFim());
			obj.setPeriodoInicio(getLocalPeriodoVO().getPeriodoInicio());
			getIdentificacaoEstudantilVO().getListaLocalPeriodo().add(obj);
			setLocalPeriodoVO(new LocalPeriodoVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void abrirModalAdicaoLocalPeriodo() {
		setIdentificacaoEstudantilVO((IdentificacaoEstudantilVO) context().getExternalContext().getRequestMap().get("identificacaoEstudantilItens"));
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

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
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

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
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

	public Date getValidadeCarteirinha() {
		return validadeCarteirinha;
	}

	public void setValidadeCarteirinha(Date validadeCarteirinha) {
		this.validadeCarteirinha = validadeCarteirinha;
	}

	public List<IdentificacaoEstudantilVO> getListaIdentificacaoEstudantilVOs() {
		if (listaIdentificacaoEstudantilVOs == null) {
			listaIdentificacaoEstudantilVOs = new ArrayList<IdentificacaoEstudantilVO>(0);
		}
		return listaIdentificacaoEstudantilVOs;
	}

	public void setListaIdentificacaoEstudantilVOs(List<IdentificacaoEstudantilVO> listaIdentificacaoEstudantilVOs) {
		this.listaIdentificacaoEstudantilVOs = listaIdentificacaoEstudantilVOs;
	}

	public CarteirinhaEstudantilVO getCarteirinhaEstudantilVO() {
		if (carteirinhaEstudantilVO == null) {
			carteirinhaEstudantilVO = new CarteirinhaEstudantilVO();
		}
		return carteirinhaEstudantilVO;
	}

	public void setCarteirinhaEstudantilVO(CarteirinhaEstudantilVO carteirinhaEstudantilVO) {
		this.carteirinhaEstudantilVO = carteirinhaEstudantilVO;
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

	/**
	 * @return the tipoPessoa
	 */
	public String getTipoPessoa() {
		if (tipoPessoa == null) {
			tipoPessoa = "AL";
		}
		return tipoPessoa;
	}

	/**
	 * @param tipoPessoa
	 *            the tipoPessoa to set
	 */
	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	/**
	 * @return the listaConsultaProfessor
	 */
	public List<FuncionarioVO> getListaConsultaProfessor() {
		if (listaConsultaProfessor == null) {
			listaConsultaProfessor = new ArrayList<FuncionarioVO>(0);
		}
		return listaConsultaProfessor;
	}

	/**
	 * @param listaConsultaProfessor
	 *            the listaConsultaProfessor to set
	 */
	public void setListaConsultaProfessor(List<FuncionarioVO> listaConsultaProfessor) {
		this.listaConsultaProfessor = listaConsultaProfessor;
	}

	/**
	 * @return the campoConsultaProfessor
	 */
	public String getCampoConsultaProfessor() {
		if (campoConsultaProfessor == null) {
			campoConsultaProfessor = "";
		}
		return campoConsultaProfessor;
	}

	/**
	 * @param campoConsultaProfessor
	 *            the campoConsultaProfessor to set
	 */
	public void setCampoConsultaProfessor(String campoConsultaProfessor) {
		this.campoConsultaProfessor = campoConsultaProfessor;
	}

	/**
	 * @return the valorConsultaProfessor
	 */
	public String getValorConsultaProfessor() {
		if (valorConsultaProfessor == null) {
			valorConsultaProfessor = "";
		}
		return valorConsultaProfessor;
	}

	/**
	 * @param valorConsultaProfessor
	 *            the valorConsultaProfessor to set
	 */
	public void setValorConsultaProfessor(String valorConsultaProfessor) {
		this.valorConsultaProfessor = valorConsultaProfessor;
	}

	public Boolean getApresentarBotaoImprimir() {
		return Uteis.isAtributoPreenchido(getListaIdentificacaoEstudantilVOs());
	}

	/**
	 * @return the funcionarioVO
	 */
	public FuncionarioVO getFuncionarioVO() {
		if (funcionarioVO == null) {
			funcionarioVO = new FuncionarioVO();
		}
		return funcionarioVO;
	}

	/**
	 * @param funcionarioVO
	 *            the funcionarioVO to set
	 */
	public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
		this.funcionarioVO = funcionarioVO;
	}

	public LocalPeriodoVO getLocalPeriodoVO() {
		if (localPeriodoVO == null) {
			localPeriodoVO = new LocalPeriodoVO();
		}
		return localPeriodoVO;
	}

	public void setLocalPeriodoVO(LocalPeriodoVO localPeriodoVO) {
		this.localPeriodoVO = localPeriodoVO;
	}

	public String getOrgaoResponsavelEmissaoCarteirinha() {
		if (orgaoResponsavelEmissaoCarteirinha == null) {
			orgaoResponsavelEmissaoCarteirinha = "";
		}
		return orgaoResponsavelEmissaoCarteirinha;
	}

	public void setOrgaoResponsavelEmissaoCarteirinha(String orgaoResponsavelEmissaoCarteirinha) {
		this.orgaoResponsavelEmissaoCarteirinha = orgaoResponsavelEmissaoCarteirinha;
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

	private List<SelectItem> tipoConsultaComboDisciplina;

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if (tipoConsultaComboDisciplina == null) {
			tipoConsultaComboDisciplina = new ArrayList<SelectItem>(0);
			tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboDisciplina;
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

	public LocalPeriodoVO getLocalPeriodoHeaderVO() {
		if (localPeriodoHeaderVO == null) {
			localPeriodoHeaderVO = new LocalPeriodoVO();
		}
		return localPeriodoHeaderVO;
	}

	public void setLocalPeriodoHeaderVO(LocalPeriodoVO localPeriodoHeaderVO) {
		this.localPeriodoHeaderVO = localPeriodoHeaderVO;
	}

	/**
	 * @return the requerimento
	 */
	public RequerimentoVO getRequerimento() {
		if (requerimento == null) {
			requerimento = new RequerimentoVO();
		}
		return requerimento;
	}

	/**
	 * @param requerimento
	 *            the requerimento to set
	 */
	public void setRequerimento(RequerimentoVO requerimento) {
		this.requerimento = requerimento;
	}

	public List<SelectItem> getTipoLayoutCarteirinha() {
		if (tipoLayoutCarteirinha == null) {
			tipoLayoutCarteirinha = new ArrayList<SelectItem>(0);
		}
		return tipoLayoutCarteirinha;
	}

	public void setTipoLayoutCarteirinha(List<SelectItem> tipoLayoutCarteirinha) {
		this.tipoLayoutCarteirinha = tipoLayoutCarteirinha;
	}

	public String getLayoutCarteirinha() {
		if (layoutCarteirinha == null) {
			layoutCarteirinha = "Layout1";
		}
		return layoutCarteirinha;
	}

	public void setLayoutCarteirinha(String layoutCarteirinha) {
		this.layoutCarteirinha = layoutCarteirinha;
	}

	public IdentificacaoEstudantilVO getIdentificacaoEstudantilVO() {
		if (identificacaoEstudantilVO == null) {
			identificacaoEstudantilVO = new IdentificacaoEstudantilVO();
		}
		return identificacaoEstudantilVO;
	}

	public void setIdentificacaoEstudantilVO(IdentificacaoEstudantilVO identificacaoEstudantilVO) {
		this.identificacaoEstudantilVO = identificacaoEstudantilVO;
	}

	/**
	 * @return the apenasRequerimentoPago
	 */
	public Boolean getApenasRequerimentoPago() {
		if (apenasRequerimentoPago == null) {
			apenasRequerimentoPago = false;
		}
		return apenasRequerimentoPago;
	}

	/**
	 * @param apenasRequerimentoPago
	 *            the apenasRequerimentoPago to set
	 */
	public void setApenasRequerimentoPago(Boolean apenasRequerimentoPago) {
		this.apenasRequerimentoPago = apenasRequerimentoPago;
	}

	public Boolean getApresentarDataValidadeCarteirinha() {
		if (apresentarDataValidadeCarteirinha == null) {
			apresentarDataValidadeCarteirinha = false;
		}
		return apresentarDataValidadeCarteirinha;
	}

	public void setApresentarDataValidadeCarteirinha(Boolean apresentarDataValidadeCarteirinha) {
		this.apresentarDataValidadeCarteirinha = apresentarDataValidadeCarteirinha;
	}

	public void consultarAluno() {
		try {
			if (!Uteis.isAtributoPreenchido(getUnidadeEnsinoVO())) {
				throw new Exception(UteisJSF.internacionalizar("msg_campoUnidadeEnsinoDeveSerInformado"));
			}
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getValorConsultaAluno(), getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				if (!obj.getMatricula().equals("")) {
					getListaConsultaAluno().add(obj);
				}
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoaCursoTurma(getValorConsultaAluno(), getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), getTurmaVO().getCodigo(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), getUnidadeEnsinoVO().getCodigo(), 0, false, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			setMatriculaVO(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatriculaVO().getMatricula(), getUnidadeEnsinoVO().getCodigo(), 0, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			if (getMatriculaVO().getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			MatriculaPeriodoVO obj = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoAtivaPreMatriculaPorMatricula(getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			setTurmaVO(obj.getTurma());
			setCursoVO(obj.getTurma().getCurso());
			setUnidadeEnsinoVO(getMatriculaVO().getUnidadeEnsino());
			setListaIdentificacaoEstudantilVOs(null);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() throws Exception {
		try {
			setMatriculaVO((MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens"));
			MatriculaPeriodoVO obj = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoAtivaPreMatriculaPorMatricula(getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			setTurmaVO(obj.getTurma());
			setCursoVO(obj.getTurma().getCurso());
			setListaConsultaAluno(null);
			setMensagemID("msg_dados_consultados");
			setValorConsultaAluno(null);
			setCampoConsultaAluno(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
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

	public void limparDadosAluno() {
		setMatriculaVO(new MatriculaVO());
		setListaIdentificacaoEstudantilVOs(null);
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public List<SelectItem> getListaSelectItemUtilizarFotoAluno() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("fotoPerfil", "Utilizar Foto do Perfil do Aluno"));
		objs.add(new SelectItem("fotoRequerimento", "Utilizar Foto Requerimento"));
		return objs;
	}

	public String getUtilizarFotoAluno() {
		if (utilizarFotoAluno == null) {
			utilizarFotoAluno = "";
		}
		return utilizarFotoAluno;
	}

	public void setUtilizarFotoAluno(String utilizarFotoAluno) {
		this.utilizarFotoAluno = utilizarFotoAluno;
	}

	public void montarListaSelectItemLayoutEtiquetaIdentificacaoEstudantil() throws Exception {
		setTipoLayoutCarteirinha(null);
		List<LayoutEtiquetaVO> layoutEtiquetaVOs = getFacadeFactory().getLayoutEtiquetaFacade().consultarRapidaPorModulo(ModuloLayoutEtiquetaEnum.CARTEIRA_ESTUDANTIL, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
		if(getTipoPessoa().equals("AL")){
			getTipoLayoutCarteirinha().addAll(UtilSelectItem.getListaSelectItem(layoutEtiquetaVOs, "codigo", "descricao"));
			getTipoLayoutCarteirinha().add(new SelectItem("Layout1", "Layout Cracha Padrão"));
			getTipoLayoutCarteirinha().add(new SelectItem("Layout2", "Layout Frente e Verso"));
			getTipoLayoutCarteirinha().add(new SelectItem("Layout3", "Layout Frente (PVC)"));
		}else if (getTipoPessoa().equals("PR")) {
			getTipoLayoutCarteirinha().addAll(UtilSelectItem.getListaSelectItem(layoutEtiquetaVOs, "codigo", "descricao"));
			getTipoLayoutCarteirinha().add(new SelectItem("Layout1", "Layout Cracha Padrão"));
		}
	}
	
	public void imprimir(){
		if(!layoutCarteirinha.equals("")){
			if(getLayoutCarteirinha().equals("Layout1")){
				imprimirPDFLayout1();
			}else if (getLayoutCarteirinha().equals("Layout2") || getLayoutCarteirinha().equals("Layout3")) {
				imprimirPDFLayout2();
			}else{
				imprimirPDFLayoutPersonalizado();
			}
		}
	}

	public boolean isApresentarCampoAno() {
		return getCursoVO().getAnual() || getCursoVO().getSemestral();
	}

	public boolean isApresentarCampoSemestre() {
		return getCursoVO().getSemestral();
	}
    
	public Boolean aprensetarCursoUnidadeEnsino() {
		if (getUnidadeEnsinoVO().getCodigo() != 0l) {
			setCursoVO(new CursoVO());
			setTurmaVO(new TurmaVO());
			setMatriculaVO(new MatriculaVO());
			setListaIdentificacaoEstudantilVOs(null);
			return Boolean.TRUE;
		}
		setListaIdentificacaoEstudantilVOs(null);
		setCursoVO(new CursoVO());
		setTurmaVO(new TurmaVO());
		setMatriculaVO(new MatriculaVO());
		setSemestre("");
		setAno("");
		return Boolean.FALSE;

	}
	public String getIsApresentarColunaNome() {
		if (getTipoPessoa().equals("AL")) {
			return UteisJSF.internacionalizar("prt_IdentificacaoEstudantil_aluno");
		} else {
			return UteisJSF.internacionalizar("prt_IdentificacaoEstudantil_professor");
		}
	}
	
	public void limparDadosProfessor(){
      setFuncionarioVO(null);
      setCampoConsultaProfessor(null);
      setValorConsultaProfessor(null);
	}

	public void consultarProfessores() {
		try {

			if (!Uteis.isAtributoPreenchido(getUnidadeEnsinoVO())) {
				throw new Exception(UteisJSF.internacionalizar("msg_campoUnidadeEnsinoDeveSerInformado"));
			}
			if (!Uteis.isAtributoPreenchido(getCursoVO())) {
				throw new Exception(UteisJSF.internacionalizar("msg_SolicitacaoAberturaTurma_unidadeEnsinoCurso"));
			}
			if (isApresentarCampoAno() && getAno().trim().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_ano"));
			}
			if (isApresentarCampoSemestre() && getSemestre().trim().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_semestre"));
			}
			if (getApresentarDataValidadeCarteirinha() && !Uteis.isAtributoPreenchido(getValidadeCarteirinha())) {
				throw new Exception(UteisJSF.internacionalizar("msg_campoDataValidadeCarteirinhaDeveSerInformado"));
			}
			if (!Uteis.isAtributoPreenchido(getLayoutCarteirinha())) {
				throw new Exception(UteisJSF.internacionalizar("msg_campoLayoutCarteirinhaDeveSerInformado"));
			}
			setListaIdentificacaoEstudantilVOs(getFacadeFactory().getIdentificacaoEstudantilFacade().consultarDadosIdentificacaoEstudantilProfessor(getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), getSemestre(), getAno(),  getFuncionarioVO().getMatricula(), true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public Boolean getIsApresentarResultadoConsulta() {
		return Uteis.isAtributoPreenchido(getListaIdentificacaoEstudantilVOs());
	}

	public void limparDadosPagina() {
		try {
			setListaIdentificacaoEstudantilVOs(null);
			setCursoVO(null);
			setTurmaVO(null);
			setDisciplina(null);
			setAno(null);
			setSemestre(null);
			setUtilizarFotoAluno(null);
			setMatriculaVO(null);
//			setUnidadeEnsinoVO(null);
			montarListaSelectItemLayoutEtiquetaIdentificacaoEstudantil();
		} catch (Exception e) {
			setListaConsultaCurso(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
 
	public void validarPessoaSelecionadaListaGeracaoCrachaCarteirinha(List<IdentificacaoEstudantilVO> identificacaoEstudantilVOs) throws Exception {
		if (getTipoPessoa().equals("AL") && !Uteis.isAtributoPreenchido(identificacaoEstudantilVOs)) {
			throw new Exception(UteisJSF.internacionalizar("msg_IdentificacaoEstudantil_AlunoNaoSelecionadoGeracaoCrachaCarteirinha"));
		} else if (getTipoPessoa().equals("PR") && !Uteis.isAtributoPreenchido(identificacaoEstudantilVOs)) {
			throw new Exception(UteisJSF.internacionalizar("msg_IdentificacaoEstudantil_ProfessorNaoSelecionadoGeracaoCrachaCarteirinha"));
		}
	}

	public String getTituloCracha() {
		if (tituloCracha == null) {
			tituloCracha = "ESTAGIÁRIO";
		}
		return tituloCracha;
	}

	public void setTituloCracha(String tituloCracha) {
		this.tituloCracha = tituloCracha;
	}

	public String getApresentarModalTituloCracha() {
		return "RichFaces.$('panelTituloCracha').show()";
	}

	public LayoutEtiquetaVO getLayoutEtiqueta() {
		if(layoutEtiqueta == null){
			layoutEtiqueta = new LayoutEtiquetaVO();
		}
		return layoutEtiqueta;
	}

	public void setLayoutEtiqueta(LayoutEtiquetaVO layoutEtiqueta) {
		this.layoutEtiqueta = layoutEtiqueta;
	}
	
	public Boolean getIsApresentarImprimirLayoutPersonalizado() throws NumberFormatException, Exception {
		try {
			setLayoutEtiqueta(getFacadeFactory().getLayoutEtiquetaFacade().consultarPorChavePrimaria(Integer.parseInt(layoutCarteirinha), Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
			if (getLayoutEtiqueta().getCodigo() != 0) {
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

}
