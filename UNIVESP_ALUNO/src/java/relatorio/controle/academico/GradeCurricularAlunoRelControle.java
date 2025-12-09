package relatorio.controle.academico;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import jakarta.faces. model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

//import controle.academico.ExpedicaoDiplomaControle;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.GradeCurricularAlunoRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
//import relatorio.negocio.jdbc.academico.GradeCurricularAlunoRel;

@SuppressWarnings("unchecked")
@Controller("GradeCurricularAlunoRelControle")
@Scope("viewScope")
@Lazy
public class GradeCurricularAlunoRelControle extends SuperControleRelatorio {

	private MatriculaVO matriculaVO;
	protected List listaConsultaAluno;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	protected Boolean existeUnidadeEnsino;
	private String valorConsultaFiltros;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private String campoFiltroPor;
	private List listaSelectItemUnidadeEnsino;
	private TurmaVO turmaVO;
	private List<TurmaVO> listaConsultaTurma;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private List<MatriculaVO> listaMatriculas;
	private String ano;
	private String semestre;
	private Boolean apresentarTodasDisciplinasGrade;
	private String ordenacao;
	private List<SelectItem> selectItemsCargoFuncionarioPrincipal;
	private List<SelectItem> selectItemsCargoFuncionarioSecundario;
	private List<FuncionarioVO> listaConsultaFuncionario;
	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;
	private GradeCurricularAlunoRelVO gradeCurricularAlunoRelVO;
	private String layout;
	private List<SelectItem> listaSelectItemLayout;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO;
	private Boolean visaoAluno;
	private Boolean filtrarPorRegistroAcademico;

	public GradeCurricularAlunoRelControle() throws Exception {
		// obterUsuarioLogado();
		setMensagemID("msg_entre_prmrelatorio");
	}
	
	@PostConstruct
	public void realizarCarregamentoGradeCurricularVindoTelaFichaAluno() {
		MatriculaVO matriculaVO = (MatriculaVO) context().getExternalContext().getSessionMap().get("matriculaFichaAluno");
		if (matriculaVO != null && !matriculaVO.getMatricula().equals("")) {
			try {
				setMatriculaVO(matriculaVO);
				consultarAlunoPorMatricula();
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				context().getExternalContext().getSessionMap().remove("matriculaFichaAluno");
			}
			
		}
	}

	public void imprimirPDF() {
		// MatriculaPeriodoVO mpVO = new MatriculaPeriodoVO();
		// String nomeTurma = "";
		List<GradeCurricularAlunoRelVO> gradeCurricularAlunoRelVOs = new ArrayList<GradeCurricularAlunoRelVO>(0);
		// GradeCurricularAlunoRelVO gradeCurricularAlunoRelVO = null;
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "GradeCurricularAlunoRelControle", "Inicializando Geração de Relatório Grade Curricular Aluno", "Emitindo Relatório");
			// getFacadeFactory().getGradeCurricularAlunoRelFacade().validarDados(getMatriculaVO(),
			// getTurmaVO(), getIsFiltrarPorAluno());
			// if (getIsFiltrarPorAluno()) {
			// setListaMatriculas(new ArrayList<MatriculaVO>(0));
			// getListaMatriculas().add(getMatriculaVO());
			// mpVO =
			// getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(getMatriculaVO().getMatricula(),
			// false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			// nomeTurma = mpVO.getTurma().getIdentificadorTurma();
			// } else {
			// nomeTurma = getTurmaVO().getIdentificadorTurma();
			// if (getTurmaVO().getTurmaAgrupada()) {
			// setListaMatriculas(getFacadeFactory().getMatriculaFacade().consultaRapidaPorTurmaAnoSemestrePeriodoLetivo(getTurmaVO().getTurmaAgrupadaVOs(),
			// 0, getAno(), getSemestre(), getUnidadeEnsinoVO().getCodigo(),
			// getUsuarioLogado()));
			// } else {
			// setListaMatriculas(getFacadeFactory().getMatriculaFacade().consultaRapidaPorTurmaAnoSemestrePeriodoLetivoGradeCurricular(getTurmaVO().getCodigo(),
			// 0, getAno(), getSemestre(), 0, 0,
			// getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado()));
			// }
			// }
			// for (MatriculaVO matricula : getListaMatriculas()) {
			// gradeCurricularAlunoRelVO =
			// getFacadeFactory().getGradeCurricularAlunoRelFacade().criarObjeto(getGradeCurricularAlunoRelVO(),
			// getApresentarTodasDisciplinasGrade(), matricula, nomeTurma,
			// getLayout());
			// if (getOrdenacao().equals("situacao")) {
			// Ordenacao.ordenarLista(gradeCurricularAlunoRelVO.getListaGradeCurricularAlunoDisciplinas(),
			// "situacao");
			// } else if (getOrdenacao().equals("periodoLetivo")) {
			// Ordenacao.ordenarLista(gradeCurricularAlunoRelVO.getListaGradeCurricularAlunoDisciplinas(),
			// "periodoLetivo");
			// } else {
			// Ordenacao.ordenarLista(gradeCurricularAlunoRelVO.getListaGradeCurricularAlunoDisciplinas(),
			// "ordenacaoAnoSemestre");
			// }
			// gradeCurricularAlunoRelVO.setApresentarTodasDisciplinasGrade(getApresentarTodasDisciplinasGrade());
			// gradeCurricularAlunoRelVO.setCampoFiltro(getCampoFiltroPor());
			// if (gradeCurricularAlunoRelVO != null) {
			// gradeCurricularAlunoRelVOs.add(gradeCurricularAlunoRelVO);
			// }
			// }
			if (getVisaoAluno()) {
				if (!getLoginControle().getPermissaoAcessoMenuVO().getPermitirImprimirMatrizCurricularAluno()) {
					throw new Exception("USUÁRIO " + getUsuarioLogado().getNome() + " não possui permissão para EMITIR RELATÓRIO em GradeCurricularAlunoRel");
				}
			}
			if (!getGradeCurricularAlunoRelVO().getApresentarCampoAssinatura()) {
				getGradeCurricularAlunoRelVO().setFuncionarioPrincipalVO(null);
				getGradeCurricularAlunoRelVO().setFuncionarioSecundarioVO(null);
				getGradeCurricularAlunoRelVO().setCargoFuncionarioPrincipal(null);
				getGradeCurricularAlunoRelVO().setCargoFuncionarioSecundario(null);
			}else{
				if(Uteis.isAtributoPreenchido(getGradeCurricularAlunoRelVO().getCargoFuncionarioPrincipal().getCodigo())){
					getGradeCurricularAlunoRelVO().setCargoFuncionarioPrincipal(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(getGradeCurricularAlunoRelVO().getCargoFuncionarioPrincipal().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				}				
				if(Uteis.isAtributoPreenchido(getGradeCurricularAlunoRelVO().getCargoFuncionarioSecundario().getCodigo())){
					getGradeCurricularAlunoRelVO().setCargoFuncionarioSecundario(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(getGradeCurricularAlunoRelVO().getCargoFuncionarioSecundario().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				}				
			}
//			gradeCurricularAlunoRelVOs = (getFacadeFactory().getGradeCurricularAlunoRelFacade().realizarGeracaoRelatorio( getIsFiltrarPorAluno(), getMatriculaVO(), getTurmaVO(), getAno(), getSemestre(), getLayout(), getOrdenacao(), getApresentarTodasDisciplinasGrade(), getGradeCurricularAlunoRelVO().getFuncionarioPrincipalVO(), getGradeCurricularAlunoRelVO().getCargoFuncionarioPrincipal(), getGradeCurricularAlunoRelVO().getFuncionarioSecundarioVO(), getGradeCurricularAlunoRelVO().getCargoFuncionarioSecundario(), getUnidadeEnsinoVO(), !getVisaoAluno(), getUsuarioLogado()));
			if (!gradeCurricularAlunoRelVOs.isEmpty()) {
//				getSuperParametroRelVO().setNomeDesignIreport(getLayout().equals("layout2")?"relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "GradeCurricularAlunoRel2.jrxml":GradeCurricularAlunoRel.getDesignIReportRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
//				getSuperParametroRelVO().setSubReport_Dir(GradeCurricularAlunoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Matriz Curricular do Aluno");
				getSuperParametroRelVO().setListaObjetos(gradeCurricularAlunoRelVOs);
//				getSuperParametroRelVO().setCaminhoBaseRelatorio(GradeCurricularAlunoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setQuantidade(gradeCurricularAlunoRelVOs.size());
				getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
				
				UnidadeEnsinoVO unidadeEnsinoVO = new UnidadeEnsinoVO();
				if (!getMatriculaVO().getUnidadeEnsino().getCodigo().equals(0)) {
					unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getMatriculaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                	if (!unidadeEnsinoVO.getCaminhoBaseLogoRelatorio().equals("") && !unidadeEnsinoVO.getNomeArquivoLogoRelatorio().equals("")) {
                		getSuperParametroRelVO().adicionarParametro("logoPadraoRelatorio", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + unidadeEnsinoVO.getCaminhoBaseLogoRelatorio() + File.separator + unidadeEnsinoVO.getNomeArquivoLogoRelatorio());
                	} else {
                		getSuperParametroRelVO().adicionarParametro("logoPadraoRelatorio", getLogoPadraoRelatorio());
                	}
                }
				
				realizarImpressaoRelatorio();
				persistirDadosPadroesGeracaoRelatorio();
				removerObjetoMemoria(this);
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "GradeCurricularAlunoRelControle", "Finalizando Geração de Relatório Grade Curricular Aluno", "Emitindo Relatório");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			// mpVO = null;
			// nomeTurma = null;
			Uteis.liberarListaMemoria(gradeCurricularAlunoRelVOs);
			removerObjetoMemoria(gradeCurricularAlunoRelVO);
			inicializarDados();
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
				MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				if (!obj.getMatricula().equals("")) {
					objs.add(obj);
				}
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				// objs =
				// getFacadeFactory().getMatriculaFacade().consultarPorNomePessoa(getValorConsultaAluno(),
				// this.getUnidadeEnsinoLogado().getCodigo(), false,
				// Uteis.NIVELMONTARDADOS_DADOSBASICOS);
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("registroAcademico")) {
				objs =  getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}

			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		// MatriculaVO objCompleto =
		// getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(),
		// obj.getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS);
		setMatriculaVO(obj);
		obj = null;
		// objCompleto = null;
		valorConsultaAluno = "";
		campoConsultaAluno = "";
		getListaConsultaAluno().clear();
	}

	public void consultarAlunoPorMatricula() throws Exception {
		try {
			// MatriculaVO objAluno =
			// getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getMatriculaVO().getMatricula(),
			// this.getUnidadeEnsinoLogado().getCodigo(), true,
			// Uteis.NIVELMONTARDADOS_TODOS);
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(getMatriculaVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMatriculaVO(objAluno);
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setMatriculaVO(new MatriculaVO());
		}
	}
	
	
	
	public void consultarAlunoPorRegistroAcademico() {
		try {
			MatriculaVO  objAluno = getFacadeFactory().getMatriculaFacade().consultarMatriculaPorRegistroAcademico(getMatriculaVO().getAluno().getRegistroAcademico(), this.getUnidadeEnsinoLogado().getCodigo(), 0,  Uteis.NIVELMONTARDADOS_COMBOBOX,  getUsuarioLogado());
			if (objAluno == null || objAluno.getMatricula().equals("") ) {
				throw new Exception("Aluno de registro Acadêmico " + getMatriculaVO().getAluno().getRegistroAcademico() + " não encontrado. Verifique se o número de matrícula está correto.");
			}		
				setMatriculaVO(objAluno);
				setMensagemDetalhada("");
				setMensagemID("msg_dados_consultados");
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
				setMatriculaVO(new MatriculaVO());
			}
}

	public Boolean getApresentarCampos() {
		if ((getMatriculaVO().getAluno() != null && getMatriculaVO().getAluno().getCodigo() != 0) || (getTurmaVO() != null && getTurmaVO().getCodigo() != 0)) {
			return true;
		}
		return false;
	}

	public boolean isApresentarCamposAluno() {
		return (getMatriculaVO().getAluno() != null && getMatriculaVO().getAluno().getCodigo() != 0 && getIsFiltrarPorAluno());
	}

	public void limparDadosAluno() throws Exception {
		setMatriculaVO(new MatriculaVO());
	}

	public void limparIdentificador() {
		setMatriculaVO(new MatriculaVO());
		setTurmaVO(new TurmaVO());
		setListaConsultaTurma(new ArrayList(0));
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			limparIdentificador();
			removerObjetoMemoria(getUnidadeEnsinoVO());
			if (getIsExisteUnidadeEnsino()) {
				montarListaSelectItemUnidadeEnsino(getUnidadeEnsinoVO().getNome());
			} else {
				montarListaSelectItemUnidadeEnsino("");
			}
		} catch (Exception e) {
		}
	}

	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				objs.add(new SelectItem(0, ""));
			}
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				removerObjetoMemoria(obj);
			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public boolean getIsExisteUnidadeEnsino() {
		try {
			if (getUnidadeEnsinoLogado().getCodigo().intValue() == 0) {
				return false;
			} else {
				getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
				getUnidadeEnsinoVO().setNome(getUnidadeEnsinoLogado().getNome());
				return true;
			}
		} catch (Exception ex) {
			return false;
		}
	}

	public List getTipoConsultaComboFiltroPor() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("aluno", "Aluno"));
		itens.add(new SelectItem("turma", "Turma"));
		return itens;
	}

	public List getTipoOrdenacao() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("anoSemestre", "Ano/Semestre"));
		itens.add(new SelectItem("periodoLetivo", "Período Letivo"));
		itens.add(new SelectItem("situacao", "Situação"));
		return itens;
	}

	public void consultarTurma() {
		try {
			List objs = new ArrayList(0);
			if (getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
				if (getCampoConsultaTurma().equals("identificadorTurma")) {
					objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo().intValue(), false, false, "", false, 0, getUsuarioLogado());
				}
				if (getCampoConsultaTurma().equals("nomeCurso")) {
					objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo().intValue(), false, false, false, 0, getUsuarioLogado());
				}
				setListaConsultaTurma(objs);
				setMensagemID("msg_dados_consultados");
			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try{
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
		setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
		if(getTurmaVO().getSubturma()){
			throw new Exception("Não é possível emitir este relatório apartir de uma subturma.");
		}
		if(!getTurmaVO().getIntegral()){
			setAno(Uteis.getAnoDataAtual());
		}else{
			setAno("");
		}
		if(getTurmaVO().getSemestral()){
			setSemestre(Uteis.getSemestreAtual());
		}else{
			setSemestre("");
		}
		}catch(Exception e){
			setTurmaVO(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally{
		valorConsultaTurma = "";
		campoConsultaTurma = "";
		listaConsultaTurma.clear();
		}
	}

	public List getTipoConsultaComboTurma() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List getTipoConsultaComboSemestre() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("1", "1º"));
		itens.add(new SelectItem("2", "2º"));
		return itens;
	}

	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemFiltros() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("anosemestre", "Ano/Semestre"));
		itens.add(new SelectItem("periodo", "Periodo Letivo"));
		return itens;
	}

	public void paintConsultaAluno(OutputStream out, Object data) throws IOException {
		PessoaVO pessoaVO = (PessoaVO) getListaConsultaAluno().get((Integer) data);
		try {
			getFacadeFactory().getArquivoHelper().renderizarImagemNaTela(out, pessoaVO.getArquivoImagem(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			pessoaVO = null;
		}
	}

	public Boolean getExisteUnidadeEnsino() {
		return existeUnidadeEnsino;
	}

	public void setExisteUnidadeEnsino(Boolean existeUnidadeEnsino) {
		this.existeUnidadeEnsino = existeUnidadeEnsino;
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

	public String getValorConsultaFiltros() {
		if (valorConsultaFiltros == null) {
			valorConsultaFiltros = "";
		}
		return valorConsultaFiltros;
	}

	public void setValorConsultaFiltros(String valorConsultaFiltros) {
		this.valorConsultaFiltros = valorConsultaFiltros;
	}

	public String getCampoFiltroPor() {
		if (campoFiltroPor == null) {
			campoFiltroPor = "aluno";
		}
		return campoFiltroPor;
	}

	public void setCampoFiltroPor(String campoFiltroPor) {
		this.campoFiltroPor = campoFiltroPor;
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

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
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

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
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

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public List<MatriculaVO> getListaMatriculas() {
		if (listaMatriculas == null) {
			listaMatriculas = new ArrayList<MatriculaVO>(0);
		}
		return listaMatriculas;
	}

	public void setListaMatriculas(List<MatriculaVO> listaMatriculas) {
		this.listaMatriculas = listaMatriculas;
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

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
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

	public Boolean getIsFiltrarPorturma() {
		if (getCampoFiltroPor().equals("turma")) {
			return true;
		}
		return false;
	}

	public Boolean getIsFiltrarPorAluno() {
		if (getCampoFiltroPor().equals("aluno")) {
			return true;
		}
		return false;
	}

	public Boolean getIsFiltrarPorAno() {
		return getIsFiltrarPorturma() && Uteis.isAtributoPreenchido(getTurmaVO().getCodigo()) && !getTurmaVO().getIntegral();		
	}

	public Boolean getIsFiltrarPorSemestre() {
		return getIsFiltrarPorturma() && Uteis.isAtributoPreenchido(getTurmaVO().getCodigo()) && getTurmaVO().getSemestral();
	}

	public Boolean getApresentarTodasDisciplinasGrade() {
		if (apresentarTodasDisciplinasGrade == null) {
			apresentarTodasDisciplinasGrade = Boolean.TRUE;
		}
		return apresentarTodasDisciplinasGrade;
	}

	public void setApresentarTodasDisciplinasGrade(Boolean apresentarTodasDisciplinasGrade) {
		this.apresentarTodasDisciplinasGrade = apresentarTodasDisciplinasGrade;
	}

	public String getOrdenacao() {
		if (ordenacao == null) {
			ordenacao = "anosemestre";
		}
		return ordenacao;
	}

	public void setOrdenacao(String ordenacao) {
		this.ordenacao = ordenacao;
	}

	public List<SelectItem> getSelectItemsCargoFuncionarioPrincipal() {
		return selectItemsCargoFuncionarioPrincipal;
	}

	public void setSelectItemsCargoFuncionarioPrincipal(List<SelectItem> selectItemsCargoFuncionarioPrincipal) {
		this.selectItemsCargoFuncionarioPrincipal = selectItemsCargoFuncionarioPrincipal;
	}

	public List<SelectItem> getSelectItemsCargoFuncionarioSecundario() {
		return selectItemsCargoFuncionarioSecundario;
	}

	public void setSelectItemsCargoFuncionarioSecundario(List<SelectItem> selectItemsCargoFuncionarioSecundario) {
		this.selectItemsCargoFuncionarioSecundario = selectItemsCargoFuncionarioSecundario;
	}

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public String getCampoConsultaFuncionario() {
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public List getTipoConsultaComboFuncionario() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("cargo", "Cargo"));
		itens.add(new SelectItem("departamento", "Departamento"));
		return itens;
	}

	public void selecionarFuncionarioPrincipal() throws Exception {
		try {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioPrincipalItens");
			getGradeCurricularAlunoRelVO().setFuncionarioPrincipalVO(obj);
			consultarFuncionarioPrincipal();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarFuncionarioSecundario() throws Exception {
		try {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioSecundarioItens");
			getGradeCurricularAlunoRelVO().setFuncionarioSecundarioVO(obj);
			consultarFuncionarioSecundario();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarFuncionarioPrincipal() throws Exception {
		try {
			getGradeCurricularAlunoRelVO().setFuncionarioPrincipalVO(consultarFuncionarioPorMatricula(getGradeCurricularAlunoRelVO().getFuncionarioPrincipalVO().getMatricula()));
//			setSelectItemsCargoFuncionarioPrincipal(montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade().consultarCargoPorCodigoFuncionario(getGradeCurricularAlunoRelVO().getFuncionarioPrincipalVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())));
			if (!getSelectItemsCargoFuncionarioPrincipal().isEmpty()) {
				getGradeCurricularAlunoRelVO().setCargoFuncionarioPrincipal(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria((Integer) getSelectItemsCargoFuncionarioPrincipal().get(0).getValue(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			} else {
				getGradeCurricularAlunoRelVO().getCargoFuncionarioPrincipal().setCodigo(0);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarFuncionarioSecundario() throws Exception {
		try {
			getGradeCurricularAlunoRelVO().setFuncionarioSecundarioVO(consultarFuncionarioPorMatricula(getGradeCurricularAlunoRelVO().getFuncionarioSecundarioVO().getMatricula()));
//			setSelectItemsCargoFuncionarioSecundario(montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade().consultarCargoPorCodigoFuncionario(getGradeCurricularAlunoRelVO().getFuncionarioSecundarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())));
			if (!getSelectItemsCargoFuncionarioSecundario().isEmpty()) {
				getGradeCurricularAlunoRelVO().setCargoFuncionarioSecundario(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria((Integer) getSelectItemsCargoFuncionarioSecundario().get(0).getValue(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			} else {
				getGradeCurricularAlunoRelVO().getCargoFuncionarioSecundario().setCodigo(0);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public FuncionarioVO consultarFuncionarioPorMatricula(String matricula) throws Exception {
		FuncionarioVO funcionarioVO = null;
		try {
			funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(matricula, 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(funcionarioVO)) {
				return funcionarioVO;
			} else {
				setMensagemDetalhada("msg_erro", "Funcionário de matrícula " + matricula + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return new FuncionarioVO();
	}

	public List<SelectItem> montarComboCargoFuncionario(List<FuncionarioCargoVO> cargos) throws Exception {
		try {
			if (cargos != null && !cargos.isEmpty()) {
				List<SelectItem> selectItems = new ArrayList<SelectItem>();
				for (FuncionarioCargoVO funcionarioCargoVO : cargos) {
					selectItems.add(new SelectItem(funcionarioCargoVO.getCargo().getCodigo(), funcionarioCargoVO.getCargo().getNome() + " - " + funcionarioCargoVO.getUnidade().getNome()));
				}
				return selectItems;
			} else {
				throw new Exception("O Funcionário selecionado não possui cargo configurado");
			}
		} finally {
			Uteis.liberarListaMemoria(cargos);
		}
	}

	public void consultarFuncionario() {
		try {
			List objs = new ArrayList(0);
			getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "FU", 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), 0, true, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCidade(getValorConsultaFuncionario(), 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), "FU", 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(), 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(getValorConsultaFuncionario(), "FU", 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), "FU", 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
//			executarMetodoControle(ExpedicaoDiplomaControle.class.getSimpleName(), "setMensagemID", "msg_dados_consultados");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public boolean isApresentarCampoCargoFuncionarioSecundario() {
		return getApresentarCampos() && Uteis.isAtributoPreenchido(getGradeCurricularAlunoRelVO().getFuncionarioSecundarioVO());
	}

	public boolean isApresentarCampoCargoFuncionarioPrincipal() {
		return getApresentarCampos() && Uteis.isAtributoPreenchido(getGradeCurricularAlunoRelVO().getFuncionarioPrincipalVO());
	}

	public void limparDadosFuncionarioPrincipal() {
		removerObjetoMemoria(getGradeCurricularAlunoRelVO().getFuncionarioPrincipalVO());
		getGradeCurricularAlunoRelVO().setFuncionarioPrincipalVO(new FuncionarioVO());
	}

	public void limparDadosFuncionarioSecundario() {
		removerObjetoMemoria(getGradeCurricularAlunoRelVO().getFuncionarioSecundarioVO());
		getGradeCurricularAlunoRelVO().setFuncionarioSecundarioVO(new FuncionarioVO());
	}

	public GradeCurricularAlunoRelVO getGradeCurricularAlunoRelVO() {
		if (gradeCurricularAlunoRelVO == null) {
			gradeCurricularAlunoRelVO = new GradeCurricularAlunoRelVO();
		}
		return gradeCurricularAlunoRelVO;
	}

	public void setGradeCurricularAlunoRelVO(GradeCurricularAlunoRelVO gradeCurricularAlunoRelVO) {
		this.gradeCurricularAlunoRelVO = gradeCurricularAlunoRelVO;
	}

	/**
	 * @return the layout
	 */
	public String getLayout() {
		if (layout == null) {
			layout = "";
		}
		return layout;
	}

	/**
	 * @param layout
	 *            the layout to set
	 */
	public void setLayout(String layout) {
		this.layout = layout;
	}

	/**
	 * @return the listaSelectItemLayout
	 */
	public List<SelectItem> getListaSelectItemLayout() {
		if (listaSelectItemLayout == null) {
			listaSelectItemLayout = new ArrayList<SelectItem>(0);
			listaSelectItemLayout.add(new SelectItem("layout1", "Layout 1"));
			listaSelectItemLayout.add(new SelectItem("layout2", "Layout 2"));
		}
		return listaSelectItemLayout;
	}

	/**
	 * @param listaSelectItemLayout
	 *            the listaSelectItemLayout to set
	 */
	public void setListaSelectItemLayout(List<SelectItem> listaSelectItemLayout) {
		this.listaSelectItemLayout = listaSelectItemLayout;
	}

	/**
	 * @return the filtroRelatorioAcademicoVO
	 */
	public FiltroRelatorioAcademicoVO getFiltroRelatorioAcademicoVO() {
		if (filtroRelatorioAcademicoVO == null) {
			filtroRelatorioAcademicoVO = new FiltroRelatorioAcademicoVO();
		}
		return filtroRelatorioAcademicoVO;
	}

	/**
	 * @param filtroRelatorioAcademicoVO
	 *            the filtroRelatorioAcademicoVO to set
	 */
	public void setFiltroRelatorioAcademicoVO(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) {
		this.filtroRelatorioAcademicoVO = filtroRelatorioAcademicoVO;
	}

	public Boolean getIsLayout2() {
		return getLayout().equals("layout2");
	}

	@PostConstruct
	public void inicializarDados() {
		Map<String, String> campos = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(null, "gradeCurricularAluno");
		if(campos != null && !campos.isEmpty()){
			setLayout(campos.containsKey("layout")?campos.get("layout"):"layout1");
			setOrdenacao(campos.containsKey("ordenacao")?campos.get("ordenacao"):"anosemestre");
			getGradeCurricularAlunoRelVO().setApresentarCampoAssinatura(campos.containsKey("apresentarAssinatura") && Uteis.isAtributoPreenchido(campos.get("apresentarAssinatura")) && campos.get("apresentarAssinatura").equals("true"));
			if(getGradeCurricularAlunoRelVO().getApresentarCampoAssinatura()){
				getGradeCurricularAlunoRelVO().getFuncionarioPrincipalVO().setCodigo(campos.containsKey("codigoFuncionarioPrincipal") && Uteis.isAtributoPreenchido(campos.get("codigoFuncionarioPrincipal"))? Integer.valueOf(campos.get("codigoFuncionarioPrincipal")) : 0);
				getGradeCurricularAlunoRelVO().getFuncionarioPrincipalVO().setMatricula(campos.containsKey("matriculaFuncionarioPrincipal") && Uteis.isAtributoPreenchido(campos.get("matriculaFuncionarioPrincipal"))? campos.get("matriculaFuncionarioPrincipal") : "");
			    getGradeCurricularAlunoRelVO().getFuncionarioPrincipalVO().getPessoa().setNome(campos.containsKey("nomeFuncionarioPrincipal") && Uteis.isAtributoPreenchido(campos.get("nomeFuncionarioPrincipal"))? campos.get("nomeFuncionarioPrincipal") : "");
			    getGradeCurricularAlunoRelVO().getFuncionarioPrincipalVO().getPessoa().setCodigo(campos.containsKey("codigoPessoaFuncionarioPrincipal") && Uteis.isAtributoPreenchido(campos.get("codigoPessoaFuncionarioPrincipal"))? Integer.valueOf(campos.get("codigoPessoaFuncionarioPrincipal")) : 0);
			    getGradeCurricularAlunoRelVO().getCargoFuncionarioPrincipal().setCodigo(campos.containsKey("codigoCargoFuncionarioPrincipal") && Uteis.isAtributoPreenchido(campos.get("codigoCargoFuncionarioPrincipal"))? Integer.valueOf(campos.get("codigoCargoFuncionarioPrincipal")) : 0);
			    getGradeCurricularAlunoRelVO().getCargoFuncionarioPrincipal().setNome(campos.containsKey("nomeCargoFuncionarioPrincipal") && Uteis.isAtributoPreenchido(campos.get("nomeCargoFuncionarioPrincipal"))? campos.get("nomeCargoFuncionarioPrincipal") : "");
			    if(Uteis.isAtributoPreenchido(getGradeCurricularAlunoRelVO().getFuncionarioPrincipalVO().getCodigo())){
					try {
						consultarFuncionarioPrincipal();
					} catch (Exception e) { 
						e.printStackTrace();
					}
				}
			    getGradeCurricularAlunoRelVO().getFuncionarioSecundarioVO().setCodigo(campos.containsKey("codigoFuncionarioSecundario") && Uteis.isAtributoPreenchido(campos.get("codigoFuncionarioSecundario"))? Integer.valueOf(campos.get("codigoFuncionarioSecundario")) : 0);
			    getGradeCurricularAlunoRelVO().getFuncionarioSecundarioVO().setMatricula(campos.containsKey("matriculaFuncionarioSecundario") && Uteis.isAtributoPreenchido(campos.get("matriculaFuncionarioSecundario"))? campos.get("matriculaFuncionarioSecundario") : "");
			    getGradeCurricularAlunoRelVO().getFuncionarioSecundarioVO().getPessoa().setNome(campos.containsKey("nomeFuncionarioSecundario") && Uteis.isAtributoPreenchido(campos.get("nomeFuncionarioSecundario"))? campos.get("nomeFuncionarioSecundario") : "");
			    getGradeCurricularAlunoRelVO().getFuncionarioSecundarioVO().getPessoa().setCodigo(campos.containsKey("codigoPessoaFuncionarioSecundario") && Uteis.isAtributoPreenchido(campos.get("codigoPessoaFuncionarioSecundario"))? Integer.valueOf(campos.get("codigoPessoaFuncionarioSecundario")) : 0);
			    getGradeCurricularAlunoRelVO().getCargoFuncionarioSecundario().setCodigo(campos.containsKey("codigoCargoFuncionarioSecundario") && Uteis.isAtributoPreenchido(campos.get("codigoCargoFuncionarioSecundario"))? Integer.valueOf(campos.get("codigoCargoFuncionarioSecundario")) : 0);
			    getGradeCurricularAlunoRelVO().getCargoFuncionarioSecundario().setNome(campos.containsKey("nomeCargoFuncionarioSecundario") && Uteis.isAtributoPreenchido(campos.get("nomeCargoFuncionarioSecundario"))? campos.get("nomeCargoFuncionarioSecundario") : "");
			    if(Uteis.isAtributoPreenchido(getGradeCurricularAlunoRelVO().getFuncionarioSecundarioVO().getCodigo())){
					try {
						consultarFuncionarioSecundario();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void persistirDadosPadroesGeracaoRelatorio() {
		try {
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getLayout(), "gradeCurricularAluno", "layout", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getOrdenacao(), "gradeCurricularAluno", "ordenacao", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getGradeCurricularAlunoRelVO().getApresentarCampoAssinatura().toString(), "gradeCurricularAluno", "apresentarAssinatura", getUsuarioLogado());
			if(getGradeCurricularAlunoRelVO().getApresentarCampoAssinatura()){
				
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getGradeCurricularAlunoRelVO().getFuncionarioPrincipalVO().getCodigo().toString(), "gradeCurricularAluno", "codigoFuncionarioPrincipal", getUsuarioLogado());
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getGradeCurricularAlunoRelVO().getFuncionarioPrincipalVO().getMatricula(), "gradeCurricularAluno", "matriculaFuncionarioPrincipal", getUsuarioLogado());
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getGradeCurricularAlunoRelVO().getFuncionarioPrincipalVO().getPessoa().getNome(), "gradeCurricularAluno", "nomeFuncionarioPrincipal", getUsuarioLogado());
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getGradeCurricularAlunoRelVO().getFuncionarioPrincipalVO().getPessoa().getCodigo().toString(), "gradeCurricularAluno", "codigoPessoaFuncionarioPrincipal", getUsuarioLogado());
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getGradeCurricularAlunoRelVO().getCargoFuncionarioPrincipal().getCodigo().toString(), "gradeCurricularAluno", "codigoCargoFuncionarioPrincipal", getUsuarioLogado());
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getGradeCurricularAlunoRelVO().getCargoFuncionarioPrincipal().getNome(), "gradeCurricularAluno", "nomeCargoFuncionarioPrincipal", getUsuarioLogado());
				
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getGradeCurricularAlunoRelVO().getFuncionarioSecundarioVO().getCodigo().toString(), "gradeCurricularAluno", "codigoFuncionarioSecundario", getUsuarioLogado());
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getGradeCurricularAlunoRelVO().getFuncionarioSecundarioVO().getMatricula(), "gradeCurricularAluno", "matriculaFuncionarioSecundario", getUsuarioLogado());
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getGradeCurricularAlunoRelVO().getFuncionarioSecundarioVO().getPessoa().getNome(), "gradeCurricularAluno", "nomeFuncionarioSecundario", getUsuarioLogado());
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getGradeCurricularAlunoRelVO().getFuncionarioSecundarioVO().getPessoa().getCodigo().toString(), "gradeCurricularAluno", "codigoPessoaFuncionarioSecundario", getUsuarioLogado());
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getGradeCurricularAlunoRelVO().getCargoFuncionarioSecundario().getCodigo().toString(), "gradeCurricularAluno", "codigoCargoFuncionarioSecundario", getUsuarioLogado());
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getGradeCurricularAlunoRelVO().getCargoFuncionarioSecundario().getNome(), "gradeCurricularAluno", "nomeCargoFuncionarioSecundario", getUsuarioLogado());
				
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getVisaoAluno() {
		if (visaoAluno == null) {
			visaoAluno = false;
		}
		return visaoAluno;
	}

	public void setVisaoAluno(Boolean visaoAluno) {
		this.visaoAluno = visaoAluno;
	}
	
	
	public Boolean getFiltrarPorRegistroAcademico() {
		if(filtrarPorRegistroAcademico == null) {
			filtrarPorRegistroAcademico = Boolean.FALSE;
		}
		return filtrarPorRegistroAcademico;
	}

	public void setFiltrarPorRegistroAcademico(Boolean filtrarPorRegistroAcademico) {
		this.filtrarPorRegistroAcademico = filtrarPorRegistroAcademico;
	}
	
	
}
