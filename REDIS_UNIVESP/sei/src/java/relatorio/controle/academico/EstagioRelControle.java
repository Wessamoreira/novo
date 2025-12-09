package relatorio.controle.academico;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.TipoEstagioEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.bancocurriculum.AreaProfissionalVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.EstagioRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.EstagioRel;

@Controller("EstagioRelControle")
@Scope("viewScope")
@Lazy
public class EstagioRelControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6952660098614557068L;
	private String ano;
	private String semestre;
	private String situacao;
	private String tipoCurso;
	private String tipoLayout;
	private List<SelectItem> listaSelectItemLayout;
	private List<AreaProfissionalVO> areaProfissionalVOs;
	private String areaProfissionalApresentar;

	private List<MatriculaVO> listaConsultaAluno;
	private String valorConsultaAluno;
	private String campoConsultaAluno;

	private List<TurmaVO> listaConsultaTurma;
	private String valorConsultaTurma;
	private String campoConsultaTurma;

	private MatriculaVO matriculaVO;
	private TurmaVO turmaVO;
	private String disciplinaApresentar;	
	private List<DisciplinaVO> listaConsultaDisciplina;
	private ParceiroVO empresaVO;
	private String campoConsultaEmpresa;
	private String valorConsultaEmpresa;
	private List<ParceiroVO> listaConsultaEmpresa;
	private List<SelectItem> listaSelectItemTipoEstagio;
	private Boolean apresentarNotaDisciplina;
	private TipoEstagioEnum tipoEstagioEnum;
	private Boolean trazerTodosEstagiosAluno;
	private Integer periodoLetivoDe;
	private Integer periodoLetivoAte;
	private String estagioRegistradoSeguradora;
	private List<SelectItem> listaSelectItemEstagioRegistradoSeguradora;
	private Date dataInicioEnvioSeguradora; 
	private Date dataTerminoEnvioSeguradora;
	private Date dataInicioCadastro;
	private Date dataFimCadastro;

	public EstagioRelControle() {
		setAno(Uteis.getAnoDataAtual4Digitos());
		setSemestre(Uteis.getSemestreAtual());
		consultarUnidadeEnsinoFiltroRelatorio("");	
		verificarTodasUnidadeEnsinoSelecionados();
		setMensagemID("msg_dados_parametroConsulta");
	}

	public String getTipoCurso() {
		if (tipoCurso == null) {
			tipoCurso = "SE";
		}
		return tipoCurso;
	}

	public void setTipoCurso(String tipoCurso) {
		this.tipoCurso = tipoCurso;
	}

	public List<SelectItem> tipoConsultaComboAluno;

	public List<SelectItem> getTipoConsultaComboAluno() {
		if (tipoConsultaComboAluno == null) {
			tipoConsultaComboAluno = new ArrayList<SelectItem>(0);
			tipoConsultaComboAluno.add(new SelectItem("nomePessoa", "Aluno"));
			tipoConsultaComboAluno.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultaComboAluno.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
			tipoConsultaComboAluno.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultaComboAluno;
	}

	public List<SelectItem> tipoConsultaCombo;

	public List<SelectItem> getTipoConsultaCombo() {
		if (tipoConsultaCombo == null) {
			tipoConsultaCombo = new ArrayList<SelectItem>(0);
			tipoConsultaCombo.add(new SelectItem("", ""));
			tipoConsultaCombo.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultaCombo.add(new SelectItem("curso", "Curso"));
			tipoConsultaCombo.add(new SelectItem("turma", "Turma"));
		}
		return tipoConsultaCombo;
	}

	public Boolean getApresentarCampoConsultaMatricula() {
		if (getControleConsulta().getCampoConsulta().equals("matricula")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getApresentarCampoConsultaCurso() {
		if (getControleConsulta().getCampoConsulta().equals("curso")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getApresentarCampoConsultaTurma() {
		if (getControleConsulta().getCampoConsulta().equals("turma")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public List<SelectItem> tipoConsultaSituacao;

	public List<SelectItem> getTipoConsultaSituacao() {
		if (tipoConsultaSituacao == null) {
			tipoConsultaSituacao = new ArrayList<SelectItem>(0);
			tipoConsultaSituacao.add(new SelectItem("todos", "Todos"));
			tipoConsultaSituacao.add(new SelectItem("pendencia", "Deve Estágio"));
			tipoConsultaSituacao.add(new SelectItem("concluido", "Cumpriu Estágio"));
			tipoConsultaSituacao.add(new SelectItem("estagioRegistrado", "Possui Cadastro no Estágio"));
			tipoConsultaSituacao.add(new SelectItem("estagioNaoRegistrado", "Não Possui Cadastro no Estágio"));
		}
		return tipoConsultaSituacao;
	}

	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(),
						this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(),
						this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(),
						this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("registroAcademico")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(getValorConsultaAluno(),
						this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
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

			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatriculaAtivaOuTrancada(
					getMatriculaVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), true,
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula()
						+ " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMatriculaVO(objAluno);
			getUnidadeEnsinoVO().setCodigo(getMatriculaVO().getUnidadeEnsino().getCodigo());
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> tipoConsultaComboTurma;

	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
			tipoConsultaComboTurma.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultaComboTurma;
	}

	public void selecionarAluno() throws Exception {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matricula");
			MatriculaVO objCompleto = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(
					obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
			setMatriculaVO(objCompleto);
			getUnidadeEnsinoVO().setCodigo(getMatriculaVO().getUnidadeEnsino().getCodigo());
			setMensagemDetalhada("");
			limparUnidadeEnsinos();
			limparCursos();
			limparTurnos();
			setCursosApresentar(obj.getCurso().getNome());
			setTurnosApresentar(obj.getTurno().getNome());
			setUnidadeEnsinosApresentar(obj.getUnidadeEnsino().getNome());
			setTurmaVO(new TurmaVO());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			
				if (getCampoConsultaTurma().equals("identificadorTurma")) {
					objs = getFacadeFactory().getTurmaFacade()
							.consultaRapidaPorIdentificadorTurmaUnidadeEnsinoCursoTurno(getValorConsultaTurma(),
									this.getUnidadeEnsinoLogado().getCodigo(), 0, 0, false, getUsuarioLogado());
				}
				if (getCampoConsultaTurma().equals("nomeCurso")) {
					objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCurso(getValorConsultaTurma(),
							this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
							getUsuarioLogado());
				}
				setListaConsultaTurma(objs);
				setMensagemID("msg_dados_consultados");
			
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() throws Exception {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			obj = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(),
					Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			setTurmaVO(obj);
			limparUnidadeEnsinos();
			limparCursos();
			limparTurnos();
			setCursosApresentar(obj.getCurso().getNome());
			setTurnosApresentar(obj.getTurno().getNome());
			setUnidadeEnsinosApresentar(obj.getUnidadeEnsino().getNome());
			setMatriculaVO(new MatriculaVO());
			getListaConsultaTurma().clear();
		} catch (Exception e) {
			setTurmaVO(new TurmaVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void imprimirPDFRegistrarEnvioSeguradora() throws Exception {
		realizarGerarPDF(true);
	}
	
	public void realizarGerarPDF(Boolean registrarSeguradora) throws Exception {
		List<EstagioRelVO> listEstagio = new ArrayList<EstagioRelVO>();
		try {
			if (!getMostrarCampoAno()) {
				setAno("");
			}
			if (!getMostrarCampoSemestre()) {
				setSemestre("");
			}			
			if(getTipoLayout().equals("EstagioRelAnalitico") || getTipoLayout().equals("EstagioRelSintetivo")){
				setApresentarNotaDisciplina(false);
			}
			this.validarDataFiltroRelatorio();
			if (getTipoLayout().contains("Sintetico")) {
				listEstagio = getFacadeFactory().getEstagioRelFacade().consultarListaEstagioSintetico(
						getUnidadeEnsinoVOs(), getCursoVOs(), getTurnoVOs(), getTurmaVO(), getListaConsultaDisciplina(),
						getEmpresaVO(), getAreaProfissionalVOs(), getMatriculaVO(), getSituacao(), getTipoEstagioEnum(),
						getTipoCurso(), getAno(), getSemestre(), getFiltroRelatorioAcademicoVO(), getTrazerTodosEstagiosAluno(), getTipoLayout(), getPeriodoLetivoDe(), getPeriodoLetivoAte(),
						getUsuarioLogado(),getDataInicioCadastro(),getDataFimCadastro());				
			} else {
				listEstagio = getFacadeFactory().getEstagioRelFacade().consultarListaEstagioAnalitico(
						getUnidadeEnsinoVOs(), getCursoVOs(), getTurnoVOs(), getTurmaVO(), getListaConsultaDisciplina(),
						getEmpresaVO(), getAreaProfissionalVOs(), getMatriculaVO(), getSituacao(), getTipoEstagioEnum(),
						getTipoCurso(), getAno(), getSemestre(), getFiltroRelatorioAcademicoVO(),
						getApresentarNotaDisciplina(), getTrazerTodosEstagiosAluno(), getTipoLayout(), getPeriodoLetivoDe(), getPeriodoLetivoAte(), getEstagioRegistradoSeguradora(), getApresentarFiltroEstagioRegistradoSeguradora(), getDataInicioEnvioSeguradora(), getDataTerminoEnvioSeguradora(),  getUsuarioLogado(),getDataInicioCadastro(),getDataFimCadastro());
			}

			if (listEstagio.isEmpty()) {
				throw new Exception("Não existem dados para este Relatório nestes filtros");
			}
			
			getSuperParametroRelVO().setTituloRelatorio("Relatório de Estágio");
			getSuperParametroRelVO().setNomeEmpresa(getUnidadeEnsinoVO().getNome());
			registrarAtividadeUsuario(getUsuarioLogado(), "EstagioRelControle","Inicializando Geração de Relatório Estágio  Aluno", "Emitindo Relatório");
			getSuperParametroRelVO().setNomeDesignIreport(getDesign());
			getSuperParametroRelVO().setSubReport_Dir(getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setListaObjetos(listEstagio);
			getSuperParametroRelVO().setQuantidade(listEstagio.size());
			getSuperParametroRelVO().setCaminhoBaseRelatorio(getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinosApresentar());
			getSuperParametroRelVO().setCurso(getCursosApresentar());
			getSuperParametroRelVO().setTurno(getTurnosApresentar());
			getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
			getSuperParametroRelVO().setDisciplina(getDisciplinaApresentar());
			getSuperParametroRelVO().adicionarParametro("empresa", getEmpresaVO().getNome());
			getSuperParametroRelVO().adicionarParametro("areaProfissional", getAreaProfissionalApresentar());
			getSuperParametroRelVO().adicionarParametro("apresentarNotaDisciplina", getApresentarNotaDisciplina());
			getSuperParametroRelVO().setAno(getAno());
			getSuperParametroRelVO().setSemestre(getSemestre());
			if(getTipoCurso().equals(PeriodicidadeEnum.INTEGRAL.getValor())){
				getSuperParametroRelVO().setPeriodo(Uteis.getData(getFiltroRelatorioAcademicoVO().getDataInicio()) +" à "+Uteis.getData(getFiltroRelatorioAcademicoVO().getDataTermino()));
			}
			getSuperParametroRelVO().adicionarParametro("filtrarCurso", PeriodicidadeEnum.getEnumPorValor(getTipoCurso()).getDescricao());
			getSuperParametroRelVO().adicionarParametro("periodoLetivoDe", getPeriodoLetivoDe());
			getSuperParametroRelVO().adicionarParametro("periodoLetivoAte", getPeriodoLetivoAte());
			if (Uteis.isAtributoPreenchido(getDataInicioCadastro()) && Uteis.isAtributoPreenchido(getDataFimCadastro())) {
				getSuperParametroRelVO().adicionarParametro("filtroDataCadastro", String.valueOf(Uteis.getData(getDataInicioCadastro())) + "  à  " + String.valueOf(Uteis.getData(getDataFimCadastro())));
			}else{
				getSuperParametroRelVO().adicionarParametro("filtroDataCadastro", "Todos");
			}
			if(getApresentarFiltroEstagioRegistradoSeguradora()){
				getSuperParametroRelVO().adicionarParametro("registradoSeguradora",  getEstagioRegistradoSeguradora().equals("Nao") ? "Não" : getEstagioRegistradoSeguradora().isEmpty()? "Todos" : "Sim" );
			}else{
				getSuperParametroRelVO().adicionarParametro("registradoSeguradora",  "");
			}
			String situacaoEstagio = "";
			for(SelectItem sit: getTipoConsultaSituacao()){
				if(((String)sit.getValue()).equals(getSituacao())){
					situacaoEstagio = sit.getLabel();
					break;
				}
			}
			getSuperParametroRelVO().adicionarParametro("situacaoEstagio", situacaoEstagio);
			String tipoEstagio = getTipoEstagioEnum() == null?"":getTipoEstagioEnum().getValorApresentar();			
			getSuperParametroRelVO().adicionarParametro("tipoEstagio", tipoEstagio);
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			adicionarFiltroSituacaoAcademica(getSuperParametroRelVO(), getFiltroRelatorioAcademicoVO());
			realizarImpressaoRelatorio();
			gravarDadosPadroesEmissaoRelatorio();
			registrarAtividadeUsuario(getUsuarioLogado(), "EstagioRelControle",
					"Finalizando Geração de Relatório Estágio Aluno", "Emitindo Relatório");
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally {
			Uteis.liberarListaMemoria(listEstagio);			
		}
	}

	public void imprimirPDF() throws Exception {
		realizarGerarPDF(false);
	}	

	public void gravarDadosPadroesEmissaoRelatorio(){
		try{
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoCurso(), EstagioRel.class.getSimpleName(), "tipoCurso", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getAno(), EstagioRel.class.getSimpleName(), "ano", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getSemestre(), EstagioRel.class.getSimpleName(), "semestre", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(Uteis.getData(getFiltroRelatorioAcademicoVO().getDataInicio()), EstagioRel.class.getSimpleName(), "dataInicio", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(Uteis.getData(getFiltroRelatorioAcademicoVO().getDataTermino()), EstagioRel.class.getSimpleName(), "dataTermino", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getSituacao(), EstagioRel.class.getSimpleName(), "situacao", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoEstagioEnum() == null ? "" : getTipoEstagioEnum().name(), EstagioRel.class.getSimpleName(), "tipoEstagioEnum", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getApresentarNotaDisciplina().toString(), EstagioRel.class.getSimpleName(), "apresentarNotaDisciplina", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTrazerTodosEstagiosAluno().toString(), EstagioRel.class.getSimpleName(), "trazerTodosEstagiosAluno", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), EstagioRel.class.getSimpleName(), getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoLayout(), EstagioRel.class.getSimpleName(), "tipoLayout", getUsuarioLogado());
			if(getPeriodoLetivoDe() != null){
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getPeriodoLetivoDe().toString(), EstagioRel.class.getSimpleName(), "periodoLetivoDe", getUsuarioLogado());
			}else{
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2("", EstagioRel.class.getSimpleName(), "periodoLetivoDe", getUsuarioLogado());
			}
			if(getPeriodoLetivoAte() != null){
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getPeriodoLetivoAte().toString(), EstagioRel.class.getSimpleName(), "periodoLetivoAte", getUsuarioLogado());
			}else{
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2("", EstagioRel.class.getSimpleName(), "periodoLetivoDe", getUsuarioLogado());
			}
		}catch(Exception e){
			
		}
	}
	
	@PostConstruct
	public void montarDadosPadroesEmissaoRelatorio() {
		try {
			Map<String, String> retorno = getFacadeFactory().getLayoutPadraoFacade()
					.consultarValoresPadroes(
							new String[] { "tipoCurso", "ano", "semestre", "dataInicio", "dataTermino", "situacao",
									"tipoEstagioEnum", "apresentarNotaDisciplina", "trazerTodosEstagiosAluno",
									"tipoLayout", "periodoLetivoDe", "periodoLetivoAte" },
							EstagioRel.class.getSimpleName());
			for (String key : retorno.keySet()) {
				try {
					if (key.equals("tipoCurso")) {
						setTipoCurso(retorno.get(key));
					} else if (key.equals("ano")) {
						setAno(retorno.get(key));
					} else if (key.equals("semestre")) {
						setSemestre(retorno.get(key));
					} else if (key.equals("periodoLetivoDe") && retorno.get(key) != null
							&& !retorno.get(key).trim().isEmpty()) {
						setPeriodoLetivoDe(Integer.valueOf(retorno.get(key)));
					} else if (key.equals("periodoLetivoAte") && retorno.get(key) != null
							&& !retorno.get(key).trim().isEmpty()) {
						setPeriodoLetivoAte(Integer.valueOf(retorno.get(key)));
					} else if (key.equals("situacao")) {
						setSituacao(retorno.get(key));
					} else if (key.equals("tipoLayout")) {
						setTipoLayout(retorno.get(key));
					} else if (key.equals("dataInicio") && retorno.get(key) != null
							&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
						getFiltroRelatorioAcademicoVO().setDataInicio(Uteis.getData(retorno.get(key), "dd/MM/YYYY"));
					} else if (key.equals("dataTermino") && retorno.get(key) != null
							&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
						getFiltroRelatorioAcademicoVO().setDataInicio(Uteis.getData(retorno.get(key), "dd/MM/YYYY"));
					} else if (key.equals("tipoEstagioEnum") && retorno.get(key) != null
							&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
						setTipoEstagioEnum(TipoEstagioEnum.getEnum(retorno.get(key)));
					} else if (key.equals("apresentarNotaDisciplina") && retorno.get(key) != null
							&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
						setApresentarNotaDisciplina(Boolean.valueOf(retorno.get(key)));
					} else if (key.equals("trazerTodosEstagiosAluno") && retorno.get(key) != null
							&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
						setTrazerTodosEstagiosAluno(Boolean.valueOf(retorno.get(key)));
					}
				} catch (Exception e) {

				}
			}
			getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroSituacaoAcademica(
					getFiltroRelatorioAcademicoVO(), EstagioRel.class.getName(), getUsuarioLogado());
		} catch (Exception e) {

		}
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public String getDesign() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator
				+ getTipoLayout() + ".jrxml");
	}

	public void montarTurma() throws Exception {
		try {
			if (!getTurmaVO().getIdentificadorTurma().equals("")) {
				setTurmaVO(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurmaEspecifico(
						getTurmaVO().getIdentificadorTurma(), this.getUnidadeEnsinoLogado().getCodigo(), false, false, false,
						Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
				limparUnidadeEnsinos();
				limparCursos();
				limparTurnos();
				setCursosApresentar(getTurmaVO().getCurso().getNome());
				setTurnosApresentar(getTurmaVO().getTurno().getNome());
				setUnidadeEnsinosApresentar(getTurmaVO().getUnidadeEnsino().getNome());
				setMatriculaVO(new MatriculaVO());
				getListaConsultaTurma().clear();
			} else {
				throw new Exception("Informe a Turma.");
			}
			if (getTurmaVO().getCodigo() != 0) {
				setMensagemID("msg_dados_consultados");
			} else {
				setMensagemID("Dados não encontratos");
			}

		} catch (Exception e) {
			limparDadosTurma();
			setMensagemDetalhada("msg_erro", e.getMessage());
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

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
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

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
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

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
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

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
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

	public Boolean getMostrarCampoAno() {
		return getTipoCurso().equals("SE") || getTipoCurso().equals("AN");
	}

	public Boolean getMostrarCampoSemestre() {
		return getTipoCurso().equals("SE");
	}

	public void limparDadosAluno() throws Exception {
		removerObjetoMemoria(getMatriculaVO());
		setCursosApresentar("");
		setTurnosApresentar("");
		setUnidadeEnsinosApresentar("");
	}

	public void limparDadosTurma() throws Exception {
		removerObjetoMemoria(getTurmaVO());
		setCursosApresentar("");
		setTurnosApresentar("");
		setUnidadeEnsinosApresentar("");
	}

	@PostConstruct
	public void consultarDisciplina() {
		try {
			setListaConsultaDisciplina(getFacadeFactory().getDisciplinaFacade()
					.consultarDisciplinasEstagio("nome", ""));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<DisciplinaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarMarcacaoTodasDisciplinas() {
		for (DisciplinaVO disciplinaVO : getListaConsultaDisciplina()) {
			disciplinaVO.setSelecionado(true);
		}
		verificarDisciplinaSelecionado();
	}

	public void realizarDesmarcacaoTodasDisciplinas() {
		for (DisciplinaVO disciplinaVO : getListaConsultaDisciplina()) {
			disciplinaVO.setSelecionado(false);
		}
		verificarDisciplinaSelecionado();
	}

	public void verificarDisciplinaSelecionado() {
		setDisciplinaApresentar("");
		for (DisciplinaVO disciplinaVO : getListaConsultaDisciplina()) {
			if (disciplinaVO.getSelecionado()) {
				setDisciplinaApresentar(
						getDisciplinaApresentar() + (getDisciplinaApresentar().isEmpty() ? "" : "; ")
								+ disciplinaVO.getNome());
			}
		}
	}

	public List<SelectItem> tipoConsultaComboDisciplina;

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if (tipoConsultaComboDisciplina == null) {
			tipoConsultaComboDisciplina = new ArrayList<SelectItem>(0);
			tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboDisciplina;
	}

	public void limparDisciplina() throws Exception {
		try {
			setDisciplinaApresentar("");
			realizarDesmarcacaoTodasDisciplinas();
		} catch (Exception e) {
		}
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

	
	public String getTipoLayout() {
		if(tipoLayout == null){
			tipoLayout = "";
		}
		return tipoLayout;
	}

	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
	}

	public List<SelectItem> getListaSelectItemLayout() {
		if (listaSelectItemLayout == null) {
			listaSelectItemLayout = new ArrayList<SelectItem>(0);
			listaSelectItemLayout.add(new SelectItem("EstagioRelAnalitico", "Analítico Por Matrícula"));
			listaSelectItemLayout.add(
					new SelectItem("EstagioAnaliticoUnidadeCursoRel", "Analítico Por Unidade Ensino e Curso"));
			listaSelectItemLayout.add(new SelectItem("EstagioAnaliticoUnidadeCursoTipoEstagioRel",
					"Analítico Por Unidade Ensino, Curso e Tipo Estágio"));
			listaSelectItemLayout.add(new SelectItem("EstagioAnaliticoUnidadeCursoEmpresaRel",
					"Analítico Por Unidade Ensino, Curso e Empresa"));
			listaSelectItemLayout.add(new SelectItem("EstagioAnaliticoUnidadeAreaProfissionalRel",
					"Analítico Por Unidade Ensino, Curso e Área Profissional"));
			listaSelectItemLayout.add(new SelectItem("EstagioRelSintetico", "Sintético"));
		}
		return listaSelectItemLayout;
	}

	public void setListaSelectItemLayout(List<SelectItem> listaSelectItemLayout) {
		this.listaSelectItemLayout = listaSelectItemLayout;
	}

	public void realizarMarcacaoTodasAreaProfissionais() {
		for (AreaProfissionalVO areaProfissionalVO : getAreaProfissionalVOs()) {
			areaProfissionalVO.setSelecionado(true);
		}
		verificarAreaProfissionalSelecionado();
	}

	public void realizarDesmarcacaoTodasAreaProfissionais() {
		for (AreaProfissionalVO areaProfissionalVO : getAreaProfissionalVOs()) {
			areaProfissionalVO.setSelecionado(false);
		}
		verificarAreaProfissionalSelecionado();
	}

	public void verificarAreaProfissionalSelecionado() {
		setAreaProfissionalApresentar("");
		for (AreaProfissionalVO areaProfissionalVO : getAreaProfissionalVOs()) {
			if (areaProfissionalVO.getSelecionado()) {
				setAreaProfissionalApresentar(
						getAreaProfissionalApresentar() + (getAreaProfissionalApresentar().isEmpty() ? "" : "; ")
								+ areaProfissionalVO.getDescricaoAreaProfissional());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void consultarAreaProfissional() {
		try {
			setAreaProfissionalVOs(
					getFacadeFactory().getAreaProfissionalFacade().consultarPorDescricaoAreaProfissionalAtivo("", false,
							Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.SUCESSO);
		}
	}

	public List<AreaProfissionalVO> getAreaProfissionalVOs() {
		if (areaProfissionalVOs == null) {
			areaProfissionalVOs = new ArrayList<AreaProfissionalVO>(0);
			consultarAreaProfissional();
		}
		return areaProfissionalVOs;
	}

	public void setAreaProfissionalVOs(List<AreaProfissionalVO> areaProfissionalVOs) {
		this.areaProfissionalVOs = areaProfissionalVOs;
	}

	public String getAreaProfissionalApresentar() {
		if (areaProfissionalApresentar == null) {
			areaProfissionalApresentar = "";
		}
		return areaProfissionalApresentar;
	}

	public void setAreaProfissionalApresentar(String areaProfissionalApresentar) {
		this.areaProfissionalApresentar = areaProfissionalApresentar;
	}

	public ParceiroVO getEmpresaVO() {
		if (empresaVO == null) {
			empresaVO = new ParceiroVO();
		}
		return empresaVO;
	}

	public void setEmpresaVO(ParceiroVO empresaVO) {
		this.empresaVO = empresaVO;
	}

	public String getCampoConsultaEmpresa() {
		if (campoConsultaEmpresa == null) {
			campoConsultaEmpresa = "nome";
		}
		return campoConsultaEmpresa;
	}

	public void setCampoConsultaEmpresa(String campoConsultaEmpresa) {
		this.campoConsultaEmpresa = campoConsultaEmpresa;
	}

	public List<SelectItem> tipoConsultaComboEmpresa;

	public List<SelectItem> getTipoConsultaComboEmpresa() {
		if (tipoConsultaComboEmpresa == null) {
			tipoConsultaComboEmpresa = new ArrayList<SelectItem>(0);
			tipoConsultaComboEmpresa.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboEmpresa.add(new SelectItem("razaoSocial", "Razão Social"));
		}
		return tipoConsultaComboEmpresa;
	}

	public String getValorConsultaEmpresa() {
		if (valorConsultaEmpresa == null) {
			valorConsultaEmpresa = "";
		}
		return valorConsultaEmpresa;
	}

	public void setValorConsultaEmpresa(String valorConsultaEmpresa) {
		this.valorConsultaEmpresa = valorConsultaEmpresa;
	}

	public List<ParceiroVO> getListaConsultaEmpresa() {
		if (listaConsultaEmpresa == null) {
			listaConsultaEmpresa = new ArrayList<ParceiroVO>(0);
		}
		return listaConsultaEmpresa;
	}	

	public void setListaConsultaEmpresa(List<ParceiroVO> listaConsultaEmpresa) {
		this.listaConsultaEmpresa = listaConsultaEmpresa;
	}

	public List<SelectItem> getListaSelectItemTipoEstagio() {
		if (listaSelectItemTipoEstagio == null) {
			listaSelectItemTipoEstagio = new ArrayList<SelectItem>(0);
			listaSelectItemTipoEstagio = UtilSelectItem.getListaSelectItemEnum(TipoEstagioEnum.values(),
					Obrigatorio.NAO);
		}
		return listaSelectItemTipoEstagio;
	}

	public void setListaSelectItemTipoEstagio(List<SelectItem> listaSelectItemTipoEstagio) {
		this.listaSelectItemTipoEstagio = listaSelectItemTipoEstagio;
	}

	public Boolean getApresentarNotaDisciplina() {
		if (apresentarNotaDisciplina == null) {
			apresentarNotaDisciplina = false;
		}
		return apresentarNotaDisciplina;
	}

	public void setApresentarNotaDisciplina(Boolean apresentarNotaDisciplina) {
		this.apresentarNotaDisciplina = apresentarNotaDisciplina;
	}

	public void setTipoConsultaComboAluno(List<SelectItem> tipoConsultaComboAluno) {
		this.tipoConsultaComboAluno = tipoConsultaComboAluno;
	}

	public void setTipoConsultaCombo(List<SelectItem> tipoConsultaCombo) {
		this.tipoConsultaCombo = tipoConsultaCombo;
	}

	public void setTipoConsultaSituacao(List<SelectItem> tipoConsultaSituacao) {
		this.tipoConsultaSituacao = tipoConsultaSituacao;
	}

	public void setTipoConsultaComboTurma(List<SelectItem> tipoConsultaComboTurma) {
		this.tipoConsultaComboTurma = tipoConsultaComboTurma;
	}

	public void setTipoConsultaComboDisciplina(List<SelectItem> tipoConsultaComboDisciplina) {
		this.tipoConsultaComboDisciplina = tipoConsultaComboDisciplina;
	}

	public TipoEstagioEnum getTipoEstagioEnum() {
		return tipoEstagioEnum;
	}

	public void setTipoEstagioEnum(TipoEstagioEnum tipoEstagioEnum) {
		this.tipoEstagioEnum = tipoEstagioEnum;
	}

	public void limparParceiro() {
		setEmpresaVO(null);
		limparMensagem();
	}

	public void consultarParceiro() {
		try {
			super.consultar();
			List<ParceiroVO> objs = new ArrayList<ParceiroVO>(0);
			if (getCampoConsultaEmpresa().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getParceiroFacade().consultarPorCodigo(new Integer(valorInt), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaEmpresa().equals("nome")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorNome(getValorConsultaEmpresa(), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaEmpresa().equals("razaoSocial")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorRazaoSocial(getValorConsultaEmpresa(), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaEmpresa().equals("RG")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorRG(getValorConsultaEmpresa(), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaEmpresa().equals("CPF")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorCPF(getValorConsultaEmpresa(), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaEmpresa().equals("tipoParceiro")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorTipoParceiro(getValorConsultaEmpresa(), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaEmpresa(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaEmpresa(new ArrayList<ParceiroVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarParceiro() {
		try {
			ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiro");
			obj = getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(obj.getCodigo(), false,
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			if (!obj.getConveniadaParaVagasEstagio()) {
				throw new Exception(
						"Esta empresa selecionada não está definida como convêniada para estágio. É necessário alterar o cadastro da Empresa/Parceiro habilitando-a como Conveniada para Estágio.");
			}
			setEmpresaVO(obj);
			setListaConsultaEmpresa(null);
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getTrazerTodosEstagiosAluno() {
		if(trazerTodosEstagiosAluno == null){
			trazerTodosEstagiosAluno =  false;
		}
		return trazerTodosEstagiosAluno;
	}

	public void setTrazerTodosEstagiosAluno(Boolean trazerTodosEstagiosAluno) {
		this.trazerTodosEstagiosAluno = trazerTodosEstagiosAluno;
	}
	
	
	public void consultarCursoFiltroRelatorio(){
		if(getCursoVOs().isEmpty() || !getCursoVOs().get(0).getPeriodicidade().equals(getTipoCurso())){
			consultarCursoFiltroRelatorio(getTipoCurso());
		}
	}

	public Integer getPeriodoLetivoDe() {
		return periodoLetivoDe;
	}

	public void setPeriodoLetivoDe(Integer periodoLetivoDe) {
		this.periodoLetivoDe = periodoLetivoDe;
	}

	public Integer getPeriodoLetivoAte() {
		return periodoLetivoAte;
	}

	public void setPeriodoLetivoAte(Integer periodoLetivoAte) {
		this.periodoLetivoAte = periodoLetivoAte;
	}

	public String getDisciplinaApresentar() {
		if(disciplinaApresentar == null){
			disciplinaApresentar = "";
		}
		return disciplinaApresentar;
	}

	public void setDisciplinaApresentar(String disciplinaApresentar) {
		this.disciplinaApresentar = disciplinaApresentar;
	}

	public String getEstagioRegistradoSeguradora() {
		if(estagioRegistradoSeguradora == null){
			estagioRegistradoSeguradora = "";
		}
		return estagioRegistradoSeguradora;
	}

	public void setEstagioRegistradoSeguradora(String estagioRegistradoSeguradora) {
		this.estagioRegistradoSeguradora = estagioRegistradoSeguradora;
	}

	public List<SelectItem> getListaSelectItemEstagioRegistradoSeguradora() {
		if(listaSelectItemEstagioRegistradoSeguradora == null){
			listaSelectItemEstagioRegistradoSeguradora = new ArrayList<SelectItem>(0);
			listaSelectItemEstagioRegistradoSeguradora.add(new SelectItem("", "Todos"));
			listaSelectItemEstagioRegistradoSeguradora.add(new SelectItem("Sim", "Sim"));
			listaSelectItemEstagioRegistradoSeguradora.add(new SelectItem("Nao", "Não"));
		}
		return listaSelectItemEstagioRegistradoSeguradora;
	}

	public void setListaSelectItemEstagioRegistradoSeguradora(List<SelectItem> listaSelectItemEstagioRegistradoSeguradora) {
		this.listaSelectItemEstagioRegistradoSeguradora = listaSelectItemEstagioRegistradoSeguradora;
	}
	
	public boolean getApresentarFiltroPeriodoEstagioRegistradoSeguradora(){
		return !getTipoLayout().equals("EstagioRelSintetico") && (getSituacao().equals("concluido") || getSituacao().equals("estagioRegistrado")) && getEstagioRegistradoSeguradora().equals("Sim");
	}
	public boolean getApresentarFiltroEstagioRegistradoSeguradora(){
		return !getTipoLayout().equals("EstagioRelSintetico") && (getSituacao().equals("concluido") || getSituacao().equals("estagioRegistrado"));
	}
	public boolean getApresentarBotaoRegistrarEnvioSeguradora(){
		return !getTipoLayout().equals("EstagioRelSintetico") && (getSituacao().equals("concluido") || getSituacao().equals("estagioRegistrado")) && getLoginControle().getPermissaoAcessoMenuVO().getPermiteRegistrarEstagioSeguradora() && !getEstagioRegistradoSeguradora().equals("Sim");		
	}

	public Date getDataInicioEnvioSeguradora() {
		return dataInicioEnvioSeguradora;
	}

	public void setDataInicioEnvioSeguradora(Date dataInicioEnvioSeguradora) {
		this.dataInicioEnvioSeguradora = dataInicioEnvioSeguradora;
	}

	public Date getDataTerminoEnvioSeguradora() {
		return dataTerminoEnvioSeguradora;
	}

	public void setDataTerminoEnvioSeguradora(Date dataTerminoEnvioSeguradora) {
		this.dataTerminoEnvioSeguradora = dataTerminoEnvioSeguradora;
	}
	
	public Date getDataInicioCadastro() {
		return dataInicioCadastro;
	}

	public void setDataInicioCadastro(Date dataInicioCadastro) {
		this.dataInicioCadastro = dataInicioCadastro;
	}

	public Date getDataFimCadastro() {
		return dataFimCadastro;
	}

	public void setDataFimCadastro(Date dataFimCadastro) {
		this.dataFimCadastro = dataFimCadastro;
	}
	
	public void validarDataFiltroRelatorio() throws Exception{
		if (Uteis.isAtributoPreenchido(getDataFimCadastro()) || Uteis.isAtributoPreenchido(getDataInicioCadastro())) {
			
			if (!Uteis.isAtributoPreenchido(getDataFimCadastro()) && Uteis.isAtributoPreenchido(getDataInicioCadastro())) {
				throw new Exception("A Data Final (Período Cadastro Estágio) Deve ser Informada!");
			}
			if (Uteis.isAtributoPreenchido(getDataFimCadastro()) && !Uteis.isAtributoPreenchido(getDataInicioCadastro())) {
				throw new Exception("A Data Inicial (Período Cadastro Estágio) Deve ser Informada!");
			}
			if (UteisData.validarDataInicialMaiorFinal(getDataInicioCadastro(), getDataFimCadastro())) {
				throw new Exception("A Data Final (Período Cadastro Estágio) Deve ser Maior que a Data Inicial");
			}
		}
	}
	
	
}