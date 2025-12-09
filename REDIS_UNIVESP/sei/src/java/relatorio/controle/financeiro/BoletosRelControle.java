package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.financeiro.BoletoBancarioRelVO;
import webservice.boletoonline.RegistroOnlineBoleto;

@Controller("BoletosRelControle")
@Scope("viewScope")
@Lazy
public class BoletosRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private String valorConsultaTipoContas;
	private String valorConsultaFiltros;
	private TurmaVO turmaVO;
	private List<TurmaVO> listaConsultaTurma;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private MatriculaVO matriculaVO;
	private List<MatriculaVO> listaConsultaAluno;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private CursoVO cursoVO;
	private List<SelectItem> listaSelectItemCurso;
	private String ano;
	private String semestre;
	private String parcela;
	private Date dataInicio;
	private Date dataFim;
	private String valorModelo;
	private String tipoImpressaoBoleto;
	private Integer codigoRenegociacao;
	private Boolean trazerApenasAlunosAtivos;
	private List<SelectItem> listaSelectItemCentroReceita;
	private Integer valorConsultaCentroReceita;
	private List<PessoaVO> listaConsultaResponsavelFinanceiro;
	private String valorConsultaResponsavelFinanceiro;
	private String campoConsultaResponsavelFinanceiro;

	public BoletosRelControle() throws Exception {
		inicializarTodasListaSelectItem();
		setMensagemID("msg_entre_prmrelatorio");
	}

	public List<SelectItem> getListaSelectItemFiltros() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("", ""));
		// itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("turma", "Turma"));
		itens.add(new SelectItem("aluno", "Aluno"));
		itens.add(new SelectItem("responsavelFinanceiro", "Responsável Financeiro"));
		// itens.add(new SelectItem("unidadeEnsino", "Unidade Ensino"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemModelos() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("boleto", "Boleto"));
		itens.add(new SelectItem("boletoSegundo", "Boleto (LayOut 2)"));
		itens.add(new SelectItem("carne", "Carnê"));
		return itens;
	}

//	public List<SelectItem> getSelectItemTipoImpressaoBoletos() {
//		List<SelectItem> itens = new ArrayList<SelectItem>();
//		itens.add(new SelectItem("boletoAluno", "Imprimir boletos do Aluno"));
//		itens.add(new SelectItem("boletoParceiro", "Imprimir boletos do Parceiro"));
//		itens.add(new SelectItem("boletoAlunoParceiro", "Imprimir boletos do Aluno/Parceiro"));
//		return itens;
//	}

	public void validarDados() throws Exception {
		if (getValorConsultaFiltros().isEmpty()) {
			throw new Exception("O campo FILTRO (Boletos) deve ser preenchido.");
		}
		if (!Uteis.isAtributoPreenchido(getUnidadeEnsinoVO())) {
			throw new Exception("O campo UNIDADE DE ENSINO (Boletos) deve ser informado.");
		}
		if (getValorConsultaFiltros().equals("turma") && !Uteis.isAtributoPreenchido(getTurmaVO())) {
			throw new Exception("O campo TURMA (Boletos) deve ser preenchido.");
		}
		if (getValorConsultaFiltros().equals("aluno") && !Uteis.isAtributoPreenchido(getMatriculaVO().getMatricula())) {
			throw new Exception("O campo MATRÍCULA (Boletos) deve ser preenchido.");
		}
		if (getValorConsultaFiltros().equals("responsavelFinanceiro") && !Uteis.isAtributoPreenchido(getResponsavelFinanceiro())) {
			throw new Exception("O campo RESPONSÁVEL FINANCEIRO (Boletos) deve ser preenchido.");
		}
		if (getValorConsultaFiltros().equals("curso") && !Uteis.isAtributoPreenchido(getCursoVO())) {
			throw new Exception("O campo CURSO (Boletos) deve ser preenchido.");
		}
	}

	public void imprimirPDF() {
		try {
			if (getFacadeFactory().getIntegracaoFinanceiroFacade().realizarVerificacaoProcessamentoIntegracaoFinanceira() && getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getUnidadeEnsinoVO().getCodigo()).getUtilizarIntegracaoFinanceira()) {
				throw new Exception("Prezado, a emissão dos boletos estão indisponíveis temporariamente, tente mais tarde.");
			}
			validarDados();
			registrarAtividadeUsuario(getUsuarioLogado(), "BoletosRelControle", "Inicializando Impressao PDF", "Emitindo Relatorio");
			List<BoletoBancarioRelVO> boletoBancarioRelVOs = getFacadeFactory().getBoletoBancarioRelFacade().emitirRelatorioLista(null, null, getMatriculaVO().getMatricula(), getApresentarAno() ? getAno() : "", getApresentarSemestre() ? getSemestre() : "", getParcela(), getCursoVO().getCodigo(), getTurmaVO().getCodigo(), getDataInicio(), getDataFim(), getUnidadeEnsinoVO().getCodigo(), getValorConsultaFiltros(), getValorConsultaCentroReceita(), getUsuarioLogado(), "", getCodigoRenegociacao(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getUnidadeEnsinoVO().getCodigo()), getResponsavelFinanceiro().getCodigo(), false, getFiltroRelatorioFinanceiroVO(), getValorConsultaFiltros().equals("turma") ? getFiltroRelatorioAcademicoVO() : null);

			getFacadeFactory().getBoletoBancarioRelFacade().realizarImpressaoPDF(boletoBancarioRelVOs, getSuperParametroRelVO(), getVersaoSistema(), getValorModelo(), getUsuarioLogado());
			realizarImpressaoRelatorio();
			gravarLayoutPadrao();
			setMensagemID("msg_relatorio_ok");
			registrarAtividadeUsuario(getUsuarioLogado(), "BoletosRelControle", "Finalizando Impressao PDF", "Emitindo Relatorio");
		} catch (Exception e) {
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	

	public void gravarLayoutPadrao() {
		try {
			getFacadeFactory().getLayoutPadraoFacade().persistirFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), BoletosRelControle.class.getSimpleName(), getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirFiltroTipoOrigemContaReceber(getFiltroRelatorioFinanceiroVO(), BoletosRelControle.class.getSimpleName(), getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getAno(), BoletosRelControle.class.getSimpleName(), "ano", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getSemestre(), BoletosRelControle.class.getSimpleName(), "semestre", getUsuarioLogado());
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
	
	@PostConstruct
	public void consultarLayoutPadrao() {
		try {
			getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), BoletosRelControle.class.getSimpleName(), getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroTipoOrigemContaReceber(getFiltroRelatorioFinanceiroVO(), BoletosRelControle.class.getSimpleName(), getUsuarioLogado());
			Map<String, String> retorno = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[]{"ano", "semestre"}, BoletosRelControle.class.getSimpleName());
			if(retorno.containsKey("ano")) {
				setAno(retorno.get("ano"));
			}
			if(retorno.containsKey("semestre")) {
				setSemestre(retorno.get("semestre"));
			}
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}


	public String consultarTurma() {
		try {
			super.consultar();
			if (getValorConsultaTurma().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return "";
			}
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorNomeUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorNomeTurno(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
			return "consultarTurma";
		} catch (Exception e) {
			setListaConsulta(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultarTurma";
		}

	}

	public void selecionarTurma() throws Exception {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurmaVO(obj);
			setCampoConsultaTurma("");
			setValorConsultaTurma("");
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setUnidadeEnsinoVO(obj.getUnidadeEnsino());			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}
	
	public List<SelectItem> tipoConsultaComboCurso;
	public List<SelectItem> getTipoConsultaComboCurso() {
		if(tipoConsultaComboCurso == null) {
		tipoConsultaComboCurso = new ArrayList<SelectItem>(0);	   
		tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
		tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboCurso;
	}

	public void limparDadosTurma() {
		setTurmaVO(new TurmaVO());
	}

	public void consultarAluno() {
		try {
			limparMensagem();
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getValorConsultaAluno().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				objs.add(getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorNomeCurso(getValorConsultaAluno(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoPorMatricula() throws Exception {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getMatriculaVO().getMatricula(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMatriculaVO(objAluno);
			setUnidadeEnsinoVO(objAluno.getUnidadeEnsino());
			setCampoConsultaAluno("");
			setValorConsultaAluno("");
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() {
		try {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		setMatriculaVO(obj);
		setUnidadeEnsinoVO(obj.getUnidadeEnsino());
		setCampoConsultaAluno("");
		setValorConsultaAluno("");
		setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
		setMensagem("");
		setMensagemID("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	}
	}

	public void limparDadosAluno() {
		setMatriculaVO(null);
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("nomePessoa", "Nome Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}

	public void montarListaSelectItemUnidadeEnsino() throws Exception {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			if(resultadoConsulta.size() == 1) {
				setUnidadeEnsinoVO(resultadoConsulta.get(0));
				setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome", false));
			}else {
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemCurso() throws Exception {
		try {
			List<CursoVO> resultadoConsulta = consultarCursoPorNome("");
			setListaSelectItemCurso(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<CursoVO> consultarCursoPorNome(String nome) throws Exception {
		return getFacadeFactory().getCursoFacade().consultarPorNome(nome, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nome) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nome, this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
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

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
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

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
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

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public List<SelectItem> getListaSelectItemCurso() {
		if (listaSelectItemCurso == null) {
			listaSelectItemCurso = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemCurso;
	}

	public void setListaSelectItemCurso(List<SelectItem> listaSelectItemCurso) {
		this.listaSelectItemCurso = listaSelectItemCurso;
	}

	public String getValorConsultaTipoContas() {
		if (valorConsultaTipoContas == null) {
			valorConsultaTipoContas = "";
		}
		return valorConsultaTipoContas;
	}

	public void setValorConsultaTipoContas(String valorConsultaTipoContas) {
		this.valorConsultaTipoContas = valorConsultaTipoContas;
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

	public void setParcela(String parcela) {
		this.parcela = parcela;
	}

	public String getParcela() {
		if (parcela == null) {
			parcela = "";
		}
		return parcela;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	/**
	 * @return the unidadeEnsinoVO
	 */
	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	/**
	 * @param unidadeEnsinoVO
	 *            the unidadeEnsinoVO to set
	 */
	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	/**
	 * @return the listaSelectItemUnidadeEnsino
	 */
	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	/**
	 * @param listaSelectItemUnidadeEnsino
	 *            the listaSelectItemUnidadeEnsino to set
	 */
	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public String getValorModelo() {
		if (valorModelo == null) {
			valorModelo = "boleto";
		}
		return valorModelo;
	}

	public void setValorModelo(String valorModelo) {
		this.valorModelo = valorModelo;
	}	

	public Integer getCodigoRenegociacao() {
		if (codigoRenegociacao == null) {
			codigoRenegociacao = 0;
		}
		return codigoRenegociacao;
	}

	public void setCodigoRenegociacao(Integer codigoRenegociacao) {
		this.codigoRenegociacao = codigoRenegociacao;
	}

	public Boolean getTrazerApenasAlunosAtivos() {
		if (trazerApenasAlunosAtivos == null) {
			trazerApenasAlunosAtivos = false;
		}
		return trazerApenasAlunosAtivos;
	}

	public void setTrazerApenasAlunosAtivos(Boolean trazerApenasAlunosAtivos) {
		this.trazerApenasAlunosAtivos = trazerApenasAlunosAtivos;
	}

	public void consultarResponsavelFinanceiro() {
		try {

			if (getValorConsultaResponsavelFinanceiro().trim().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			getFacadeFactory().getPessoaFacade().setIdEntidade("ResponsavelFinanceiro");
			getListaConsultaResponsavelFinanceiro().clear();
			if (getCampoConsultaResponsavelFinanceiro().equals("nome")) {
				setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorNomeResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaResponsavelFinanceiro().equals("nomeAluno")) {
				setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorNomeAlunoResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaResponsavelFinanceiro().equals("CPF")) {
				setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorCpfResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaResponsavelFinanceiro(new ArrayList<PessoaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<SelectItem> tipoConsultaComboResponsavelFinanceiro;

	public List<SelectItem> getTipoConsultaComboResponsavelFinanceiro() {
		if (tipoConsultaComboResponsavelFinanceiro == null) {
			tipoConsultaComboResponsavelFinanceiro = new ArrayList<SelectItem>(0);
			tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("nomeAluno", "Aluno"));
			tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("CPF", "CPF"));
		}
		return tipoConsultaComboResponsavelFinanceiro;
	}

	protected PessoaVO responsavelFinanceiro;

	public PessoaVO getResponsavelFinanceiro() {
		if (responsavelFinanceiro == null) {
			responsavelFinanceiro = new PessoaVO();
		}
		return responsavelFinanceiro;
	}

	public void setResponsavelFinanceiro(PessoaVO responsavelFinanceiro) {
		this.responsavelFinanceiro = responsavelFinanceiro;
	}

	public void selecionarResponsavelFinanceiro() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("responsavelFinanceiroItens");
			getListaConsultaResponsavelFinanceiro().clear();
			this.setResponsavelFinanceiro(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<PessoaVO> getListaConsultaResponsavelFinanceiro() {
		if (listaConsultaResponsavelFinanceiro == null) {
			listaConsultaResponsavelFinanceiro = new ArrayList<PessoaVO>(0);
		}
		return listaConsultaResponsavelFinanceiro;
	}

	public void setListaConsultaResponsavelFinanceiro(List<PessoaVO> listaConsultaResponsavelFinanceiro) {
		this.listaConsultaResponsavelFinanceiro = listaConsultaResponsavelFinanceiro;
	}

	public String getValorConsultaResponsavelFinanceiro() {
		if (valorConsultaResponsavelFinanceiro == null) {
			valorConsultaResponsavelFinanceiro = "";
		}
		return valorConsultaResponsavelFinanceiro;
	}

	public void setValorConsultaResponsavelFinanceiro(String valorConsultaResponsavelFinanceiro) {
		this.valorConsultaResponsavelFinanceiro = valorConsultaResponsavelFinanceiro;
	}

	public String getCampoConsultaResponsavelFinanceiro() {
		if (campoConsultaResponsavelFinanceiro == null) {
			campoConsultaResponsavelFinanceiro = "";
		}
		return campoConsultaResponsavelFinanceiro;
	}

	public void setCampoConsultaResponsavelFinanceiro(String campoConsultaResponsavelFinanceiro) {
		this.campoConsultaResponsavelFinanceiro = campoConsultaResponsavelFinanceiro;
	}

	public Integer getValorConsultaCentroReceita() {
		if (valorConsultaCentroReceita == null) {
			valorConsultaCentroReceita = 0;
		}
		return valorConsultaCentroReceita;
	}

	public void setValorConsultaCentroReceita(Integer valorConsultaCentroReceita) {
		this.valorConsultaCentroReceita = valorConsultaCentroReceita;
	}

	public List<SelectItem> getListaSelectItemCentroReceita() {
		if (listaSelectItemCentroReceita == null) {
			listaSelectItemCentroReceita = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemCentroReceita;
	}

	public void setListaSelectItemCentroReceita(List<SelectItem> listaSelectItemCentroReceita) {
		this.listaSelectItemCentroReceita = listaSelectItemCentroReceita;
	}

	public void montarListaSelectItemCentroReceita() throws Exception {
		List<CentroReceitaVO> centroReceitaVOs = getFacadeFactory().getCentroReceitaFacade().consultarPorDescricao("", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		setListaSelectItemCentroReceita(UtilSelectItem.getListaSelectItem(centroReceitaVOs, "codigo", "descricao"));
	}

	public boolean getIsApresentarFiltroCurso() {
		return getValorConsultaFiltros().equals("curso");
	}

	public boolean getIsApresentarFiltroTurma() {
		return getValorConsultaFiltros().equals("turma");
	}

	public boolean getIsApresentarFiltroAluno() {
		return getValorConsultaFiltros().equals("aluno");
	}

	public boolean getIsApresentarFiltroUnidadeEnsino() {
		return getValorConsultaFiltros().equals("unidadeEnsino");
	}

	public boolean getIsApresentarFiltroResponsavelFinanceiro() {
		return getValorConsultaFiltros().equals("responsavelFinanceiro");
	}

	public void inicializarTodasListaSelectItem() throws Exception {
		montarListaSelectItemCentroReceita();
		//montarListaSelectItemCurso();
		montarListaSelectItemUnidadeEnsino();
	}
	
	public void limparResponsavelFinanceiro() {
		setResponsavelFinanceiro(new PessoaVO());
	}
	
	public void limparCamposConformeFiltro(){
		limparDadosAluno();
		limparDadosTurma();	
		limparResponsavelFinanceiro();
		
	}
	
	public Boolean getApresentarAno() {
		return !getValorConsultaFiltros().equals("responsavelFinanceiro") && 
				((getValorConsultaFiltros().equals("aluno") && Uteis.isAtributoPreenchido(getMatriculaVO().getMatricula()) && !getMatriculaVO().getCurso().getIntegral())
						|| (getValorConsultaFiltros().equals("turma") && Uteis.isAtributoPreenchido(getTurmaVO()) && !getTurmaVO().getIntegral()));
	}
	
	public Boolean getApresentarSemestre() {
		return !getValorConsultaFiltros().equals("responsavelFinanceiro") && 
				((getValorConsultaFiltros().equals("aluno") && Uteis.isAtributoPreenchido(getMatriculaVO().getMatricula()) && getMatriculaVO().getCurso().getSemestral())
						|| (getValorConsultaFiltros().equals("turma") && Uteis.isAtributoPreenchido(getTurmaVO()) && getTurmaVO().getSemestral()));
	}
	
	public Boolean getPossuiFiltrarPorSelecionado() {
		if(getValorConsultaFiltros().equals("responsavelFinanceiro") && Uteis.isAtributoPreenchido(getResponsavelFinanceiro())) {
			return true;		
		}
		if(getValorConsultaFiltros().equals("aluno") && Uteis.isAtributoPreenchido(getMatriculaVO().getMatricula())) {
			return true;
		}
		if(getValorConsultaFiltros().equals("turma") && Uteis.isAtributoPreenchido(getTurmaVO())) {
			return true;
		}
		return false;
	}

}
