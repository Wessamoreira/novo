package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.CartaoCreditoDebitoRelVO;
import relatorio.negocio.jdbc.financeiro.CartaoCreditoDebitoRel;


@Controller("CartaoCreditoDebitoRelControle")
@Scope("viewScope")
@Lazy
public class CartaoCreditoDebitoRelControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6929906177178148564L;
	private String tipoPessoa;
	private String tipoCurso;
	private Boolean filtrarPorDataCompetencia;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List listaConsultaAluno;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private String valorConsultaFiltros;
	private String tipoRelatorio;
	private String ordenacao;
	private TurmaVO turma;
	private ContaReceberVO contaReceber;
	private MatriculaVO matricula;
	private Date dataInicio;
	private Date dataFim;
	private List<TurmaVO> listaConsultaTurma;
	protected String funcionarioMatricula;
	protected String funcionarioNome;
	protected String candidatoCpf;
	protected String candidatoNome;
	protected String parceiroCPF;
	protected String parceiroCNPJ;
	protected String parceiroNome;
	protected String fornecedorNome;
	protected List<FuncionarioVO> listaConsultaFuncionario;
	protected String valorConsultaFuncionario;
	protected String campoConsultaFuncionario;
	protected List<PessoaVO> listaConsultaCandidato;
	protected String valorConsultaCandidato;
	protected String campoConsultaCandidato;
	protected List<ParceiroVO> listaConsultaParceiro;
	protected String valorConsultaParceiro;
	protected String campoConsultaParceiro;
	protected List<FornecedorVO> listaConsultaFornecedor;
	protected String valorConsultaFornecedor;
	protected String campoConsultaFornecedor;
	private Integer parceiroCodigo;
	private Integer fornecedorCodigo;
	protected Integer pessoa;
	protected String filtrarPor;
	protected String situacaoBaixa;
	protected String layout;
	
	public CartaoCreditoDebitoRelControle()  {
		setMensagemID("msg_entre_prmrelatorio");
	}
	
	private void imprimirPDF(Boolean sintetico) throws Exception{
		List<CartaoCreditoDebitoRelVO> listaObjetos = null;
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "CartaoCreditoDebitoRelControle", "Inicializando Geração de Relatório CartaoCreditoDebitoRel" + this.getUnidadeEnsinoVO().getNome() + " - " + getUsuarioLogado().getCodigo() + " - " + getUsuarioLogado().getPerfilAcesso().getCodigo(), "Emitindo Relatório");
			getFacadeFactory().getCartaoCreditoDebitoRelFacade().validarDados(ObterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getDataInicio(), getDataFim(), getTipoRelatorio());
			if (sintetico) {
				listaObjetos = getFacadeFactory().getCartaoCreditoDebitoRelFacade().gerarListaSintetico(ObterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), ObterListaOperadoraCartaoSelecionada(getOperadoraCartaoVOs()), ObterListaCursoSelecionada(getCursoVOs()), getDataInicio(), getDataFim(), getFiltrarPor(), getSituacaoBaixa(), getMatricula(), getResponsavelFinanceiro(), getFornecedorCodigo(), getPessoa(), getParceiroCodigo(), getTipoPessoa());
			} else {
				listaObjetos = getFacadeFactory().getCartaoCreditoDebitoRelFacade().gerarListaAnalitico(ObterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), ObterListaOperadoraCartaoSelecionada(getOperadoraCartaoVOs()), ObterListaCursoSelecionada(getCursoVOs()), getDataInicio(), getDataFim(), getFiltrarPor(), getSituacaoBaixa(), getMatricula(), getResponsavelFinanceiro(), getFornecedorCodigo(), getPessoa(), getParceiroCodigo(), getTipoPessoa());
			}
			if (!listaObjetos.isEmpty()) {
				
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getCartaoCreditoDebitoRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				if (sintetico) {
					getSuperParametroRelVO().setNomeDesignIreport(CartaoCreditoDebitoRel.getDesignIReportRelatorioSintetico());
					getSuperParametroRelVO().setTituloRelatorio("Rel. Sintético Cartão de Crédito/Débito");
				} else {
					getSuperParametroRelVO().setNomeDesignIreport(CartaoCreditoDebitoRel.getDesignIReportRelatorioAnalitico());
					getSuperParametroRelVO().setTituloRelatorio("Rel. Analítico Cartão de Crédito/Débito");
				}
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getCartaoCreditoDebitoRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");
				if (getDataInicio() != null) {
					getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(getDataInicio())) + "  a  " + String.valueOf(Uteis.getData(getDataFim())));
				} else {
					getSuperParametroRelVO().setPeriodo(String.valueOf("Datas anteriores à " + String.valueOf(Uteis.getData(getDataFim()))));
				}
				if (!getUnidadeEnsinosApresentar().trim().isEmpty()) {
					getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinosApresentar());
				} else {
					getSuperParametroRelVO().setUnidadeEnsino("TODAS");
				}
				if (!getCursosApresentar().trim().isEmpty()) {
					getSuperParametroRelVO().setCurso(getCursosApresentar());
				} else {
					getSuperParametroRelVO().setCurso("TODOS");
				}
				if (Uteis.isAtributoPreenchido(getTurma().getCodigo())) {
					getSuperParametroRelVO().setTurma(getTurma().getIdentificadorTurma());
				} else {
					getSuperParametroRelVO().setTurma("TODAS");
				}
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
	            registrarAtividadeUsuario(getUsuarioLogado(), "CartaoCreditoDebitoRel", "Finalizando Geração de Relatório CartaoCreditoDebito", "Emitindo Relatório");	            				
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} finally {
			limparDadosRelacionadosUnidadeEnsino();
			Uteis.liberarListaMemoria(listaObjetos);
		}
	}

	public void consultarCursoFiltroRelatorio() {
		if (getCursoVOs().isEmpty()) {
			consultarCursoFiltroRelatorio("");
		}
	}
	
	public void imprimirPDFSintetico() {
		try {
			imprimirPDF(Boolean.TRUE);
			setMensagemID("msg_relatorio_ok");
		//	removerObjetoMemoria(this);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		
	}

	public void imprimirPDFAnalitico() {
		try {
			imprimirPDF(Boolean.FALSE);	
			setMensagemID("msg_relatorio_ok");
		//	removerObjetoMemoria(this);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemTipoRelatorio() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("relatorioDetalhado", "Relatório Detalhado"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemOrdenarPor() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("TU", "Turma"));
		itens.add(new SelectItem("AL", "Nome Aluno"));
		return itens;
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
	
	public Boolean getIsApresentarComboOrdenacao() {
		if (getTipoRelatorio().equals("relatorioDetalhadoComDesconto")) {
			return false;
		} else {
			return true;
		}
	}

	public Boolean getIsApresentarBotoesRelatorioComDesconto() {
		if (getTipoRelatorio().equals("relatorioDetalhado")) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsApresentarBotaoRelatorioDetalhadoNormal() {
		if (getTipoRelatorio().equals("relatorioDetalhado")) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsApresentarBotaoRelatorioSinteticoNormal() {
		if (getTipoRelatorio().equals("relatorioSimplificado")) {
			return true;
		} else {
			return false;
		}
	}

	public void consultarTurma() {
		try {
			super.consultar();
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), null, ObterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsinoCurso(getValorConsultaTurma(), null, ObterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurnoCurso(getValorConsultaTurma(), null, ObterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), null, 0, ObterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurma(obj);
			for(UnidadeEnsinoVO unidadeEnsinoVO: getUnidadeEnsinoVOs()){
				unidadeEnsinoVO.setFiltrarUnidadeEnsino(unidadeEnsinoVO.getCodigo().equals(obj.getUnidadeEnsino().getCodigo()));			
			}
			limparCurso();
			setCursosApresentar(obj.getCurso().getNome());
			getUnidadeEnsinoVO().setNome(obj.getUnidadeEnsino().getNome());			
			getListaConsultaTurma().clear();
			this.setValorConsultaTurma("");
			this.setCampoConsultaTurma("");
			setMensagemID("", "");
		} catch (Exception e) {
		}
	}

	public void limparTurma() throws Exception {
//		setCursosApresentar("");
		setTurma(null);			
	}

	public void limparAluno() throws Exception {
		try {
			setCursosApresentar("");
			setMatricula(null);
		} catch (Exception e) {
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

	public void consultarCurso() {
		try {
			List<CursoVO> cursoVOs = getFacadeFactory().getCursoFacade().consultarTodosCursosPorUnidadeEnsino(ObterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()));
			for(CursoVO cursoVO: cursoVOs){
				if(getCursoVOs().contains(cursoVO)){
					cursoVO.setFiltrarCursoVO(getCursoVOs().get(getCursoVOs().indexOf(cursoVO)).getFiltrarCursoVO());					
				}
			}
			setCursoVOs(cursoVOs);
			verificarTodosCursosSelecionados();
		} catch (Exception e) {		
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}	

	public void limparCurso() {
		super.limparCursos();					
	}

	public void limparDadosRelacionadosUnidadeEnsino() {		
		setTurma(null);
		setMatricula(null);
		consultarCurso();
	}

	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("nome")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			if(getMatricula().getMatricula().length() > 50){
				throw new Exception("Não Foi Possível Localizar o Aluno de Matrícula " + getMatricula().getMatricula() + ".Verifique Se o Número de Matrícula Está Correto.");
			}
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatriculaListaUnidadeEnsinoVOs(getMatricula().getMatricula(), ObterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatricula().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			for(UnidadeEnsinoVO unidadeEnsinoVO: getUnidadeEnsinoVOs()){
				unidadeEnsinoVO.setFiltrarUnidadeEnsino(unidadeEnsinoVO.getCodigo().equals(objAluno.getUnidadeEnsino().getCodigo()));			
			}
			limparCurso();
			setCursosApresentar(objAluno.getCurso().getNome());
			getUnidadeEnsinoVO().setNome(objAluno.getUnidadeEnsino().getNome());		
			setMatricula(objAluno);
			setCampoConsultaAluno("");
			setValorConsultaAluno("");
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		setMatricula(obj);
		for(UnidadeEnsinoVO unidadeEnsinoVO: getUnidadeEnsinoVOs()){
			unidadeEnsinoVO.setFiltrarUnidadeEnsino(unidadeEnsinoVO.getCodigo().equals(obj.getUnidadeEnsino().getCodigo()));			
		}
		limparCurso();
		setCursosApresentar(obj.getCurso().getNome());
		getUnidadeEnsinoVO().setNome(obj.getUnidadeEnsino().getNome());		
		valorConsultaAluno = "";
		campoConsultaAluno = "";
		getListaConsultaAluno().clear();
	}

//	public List<SelectItem> tipoConsultaComboAluno;
	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);	
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	
	public String getTipoRelatorio() {
		if (tipoRelatorio == null) {
			tipoRelatorio = "";
		}
		return tipoRelatorio;
	}

	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public void setTurma(TurmaVO turma) {
		this.turma = turma;
	}

	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return turma;
	}

	public void setContaReceber(ContaReceberVO contaReceber) {
		this.contaReceber = contaReceber;
	}

	public ContaReceberVO getContaReceber() {
		if (contaReceber == null) {
			contaReceber = new ContaReceberVO();
		}
		return contaReceber;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = Uteis.getNewDateComMesesAMenos(1);
		}
		return dataInicio;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = new Date();
		}
		return dataFim;
	}

	public void setOrdenacao(String ordenacao) {
		this.ordenacao = ordenacao;
	}

	public String getOrdenacao() {
		if (ordenacao == null) {
			ordenacao = "";
		}
		return ordenacao;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
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

	public List getListaConsultaAluno() {
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return matricula;
	}

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}

	public String getValorConsultaAluno() {
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
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

	protected List<PessoaVO> listaConsultaResponsavelFinanceiro;
	protected String valorConsultaResponsavelFinanceiro;
	protected String campoConsultaResponsavelFinanceiro;

	public void consultarResponsavelFinanceiro() {
		try {

			if (getValorConsultaResponsavelFinanceiro().trim().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			getFacadeFactory().getPessoaFacade().setIdEntidade("ResponsavelFinanceiro");
			getListaConsultaResponsavelFinanceiro().clear();
			if (getCampoConsultaResponsavelFinanceiro().equals("nome")) {
				setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorNomeResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaResponsavelFinanceiro().equals("nomeAluno")) {
				setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorNomeAlunoResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaResponsavelFinanceiro().equals("CPF")) {
				setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorCpfResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
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

	public void selecionarResponsavelFinanceiro() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("responsavelFinanceiro");
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

	public void limparDadosResponsavelFinanceiro() {
		getResponsavelFinanceiro().setCodigo(0);
		getResponsavelFinanceiro().setNome("");
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			if (getUnidadeEnsinoLogado().getCodigo() > 0) {
				unidadeEnsinoVO = getUnidadeEnsinoLogado();
			} else {
				unidadeEnsinoVO = new UnidadeEnsinoVO();
			}
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public OperadoraCartaoVO getOperadoraCartaoVO() {
		if (operadoraCartaoVO == null) {
			operadoraCartaoVO = new OperadoraCartaoVO();
		}
		return operadoraCartaoVO;
	}
	
	public void setOperadoraCartaoVO(OperadoraCartaoVO operadoraCartaoVO) {
		this.operadoraCartaoVO = operadoraCartaoVO;
	}
	
	public Boolean getFiltrarPorDataCompetencia() {
		if (filtrarPorDataCompetencia == null) {
			filtrarPorDataCompetencia = false;
		}
		return filtrarPorDataCompetencia;
	}

	public void setFiltrarPorDataCompetencia(Boolean filtrarPorDataCompetencia) {
		this.filtrarPorDataCompetencia = filtrarPorDataCompetencia;
	}

	public boolean getIsApresentarBotaoSelecionarUnidadeEnsino() throws Exception {
		return getUnidadeEnsinoVOs().size() > 1;
	}


	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("InadimplenciaRel");
			verificarTodasUnidadesSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	@PostConstruct
	public void consultarOperadoraCartao() {
		try {
			consultarOperadoraCartaoFiltroRelatorio();
			verificarTodasOperadoraCartaoSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@PostConstruct
	public void consultarCursos() {
		try {
//			consultarCurso();
//			verificarTodosCursosSelecionados();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if (getUnidadeEnsinoVOs().size() > 1) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getNome()).append("; ");
				} 
			}
			getUnidadeEnsinoVO().setNome(unidade.toString());
		} else {
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				if (getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					getUnidadeEnsinoVO().setNome(getUnidadeEnsinoVOs().get(0).getNome());
				}
			} else {
				getUnidadeEnsinoVO().setNome(unidade.toString());
			}
		}
		limparDadosRelacionadosUnidadeEnsino();
	}

	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			if (getMarcarTodasUnidadeEnsino()) {
				unidade.setFiltrarUnidadeEnsino(Boolean.TRUE);
			} else {
				unidade.setFiltrarUnidadeEnsino(Boolean.FALSE);
			}
		}
		verificarTodasUnidadesSelecionadas();
	}

	public List<UnidadeEnsinoVO> ObterListaUnidadeEnsinoSelecionada(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
		List<UnidadeEnsinoVO> objs = new ArrayList<UnidadeEnsinoVO>(0);
		Iterator<UnidadeEnsinoVO> i = unidadeEnsinoVOs.iterator();
		while (i.hasNext()) {
			UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
			if (obj.getFiltrarUnidadeEnsino()) {
				objs.add(obj);
			}
		}
		return objs;
	}

	public void verificarTodasOperadoraCartaoSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if (getOperadoraCartaoVOs().size() > 1) {
			for (OperadoraCartaoVO obj : getOperadoraCartaoVOs()) {
				if (obj.getFiltrarOperadoraCartaoVO()) {
					unidade.append(obj.getNome()).append("; ");
				} 
			}
			setOperadoraCartaosApresentar(unidade.toString());
		} else {
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				if (getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					setOperadoraCartaosApresentar(getUnidadeEnsinoVOs().get(0).getNome());
				}
			} else {
				setOperadoraCartaosApresentar(unidade.toString());
			}
		}
	}
	
	public void marcarTodosOperadoraCartaosAction() {
		for (OperadoraCartaoVO operadora : getOperadoraCartaoVOs()) {
	 		if (getMarcarTodosOperadoraCartaos()) {
				operadora.setFiltrarOperadoraCartaoVO(Boolean.TRUE);
			} else {
				operadora.setFiltrarOperadoraCartaoVO(Boolean.FALSE);
			}
		}
		verificarTodasOperadoraCartaoSelecionadas();
	}
	
	public List<OperadoraCartaoVO> ObterListaOperadoraCartaoSelecionada(List<OperadoraCartaoVO> operadoraCartaoVOs) {
		List<OperadoraCartaoVO> objs = new ArrayList<OperadoraCartaoVO>(0);
		Iterator<OperadoraCartaoVO> i = operadoraCartaoVOs.iterator();
		while (i.hasNext()) {
			OperadoraCartaoVO obj = (OperadoraCartaoVO) i.next();
			if (obj.getFiltrarOperadoraCartaoVO()) {
				objs.add(obj);
			}
		}
		return objs;
	}

	public void marcarTodosCursoAction() {
		for (CursoVO curso : getCursoVOs()) {
			if (getMarcarTodosCursos()) {
				curso.setFiltrarCursoVO(Boolean.TRUE);
			} else {
				curso.setFiltrarCursoVO(Boolean.FALSE);
			}
		}
		verificarTodosCursosSelecionados();
	}
	
	public List<CursoVO> ObterListaCursoSelecionada(List<CursoVO> cursoVOs) {
		List<CursoVO> objs = new ArrayList<CursoVO>(0);
		Iterator<CursoVO> i = cursoVOs.iterator();
		while (i.hasNext()) {
			CursoVO obj = (CursoVO) i.next();
			if (obj.getFiltrarCursoVO()) {
				objs.add(obj);
			}
		}
		return objs;
	}
		
	
	public Boolean getIsApresentarBotoesSelecionarLimparCurso() {
		return !(Uteis.isAtributoPreenchido(getTurma().getIdentificadorTurma()) || Uteis.isAtributoPreenchido(getMatricula().getMatricula()));
	}	

	public void limparDadosFuncionario() {
		setFuncionarioMatricula("");
		setFuncionarioNome("");
	}
	
	public void limparDadosFornecedor() {
		setFornecedorCodigo(0);
		setFornecedorNome("");
	}

	public void limparDadosCandidato() {
		setCandidatoCpf("");
		setCandidatoNome("");
	}

	public void limparDadosParceiro() {
		setParceiroCPF("");
		setParceiroCNPJ("");
		setParceiroNome("");
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

	private List<SelectItem> tipoConsultaComboFuncionario;

	public List<SelectItem> getTipoConsultaComboFuncionario() {
		if (tipoConsultaComboFuncionario == null) {
			tipoConsultaComboFuncionario = new ArrayList<SelectItem>(0);
			tipoConsultaComboFuncionario.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultaComboFuncionario.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboFuncionario.add(new SelectItem("CPF", "CPF"));
			tipoConsultaComboFuncionario.add(new SelectItem("cargo", "Cargo"));
			tipoConsultaComboFuncionario.add(new SelectItem("departamento", "Departamento"));
		}
		return tipoConsultaComboFuncionario;
	}

	public List<SelectItem> tipoConsultaComboCandidato;

	public List<SelectItem> getTipoConsultaComboCandidato() {
		if (tipoConsultaComboCandidato == null) {
			tipoConsultaComboCandidato = new ArrayList<SelectItem>(0);
			tipoConsultaComboCandidato.add(new SelectItem("codigo", "Código"));
			tipoConsultaComboCandidato.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboCandidato.add(new SelectItem("CPF", "CPF"));
			tipoConsultaComboCandidato.add(new SelectItem("RG", "RG"));
		}
		return tipoConsultaComboCandidato;
	}
	public void selecionarFornecedor() {
		FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
		this.setFornecedorCodigo(obj.getCodigo());
		this.setFornecedorNome(obj.getNome());
	}

	public void consultarCandidato() {
		try {
			getFacadeFactory().getPessoaFacade().setIdEntidade("Candidato");
			List<PessoaVO> objs = new ArrayList<PessoaVO>(0);
			if (getValorConsultaCandidato().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaCandidato().equals("codigo")) {
				int valorInt = Integer.parseInt(getValorConsultaCandidato());
				objs = getFacadeFactory().getPessoaFacade().consultarPorCodigo(new Integer(valorInt), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCandidato().equals("nome")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaCandidato(), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}

			if (getCampoConsultaCandidato().equals("nomeCidade")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNomeCidade(getValorConsultaCandidato(), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCandidato().equals("CPF")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaCandidato(), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCandidato().equals("RG")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorRG(getValorConsultaCandidato(), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCandidato().equals("necessidadesEspeciais")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNecessidadesEspeciais(getValorConsultaCandidato(), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaCandidato(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCandidato(new ArrayList<PessoaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	private List<SelectItem> tipoConsultaComboFornecedor;

	public String getMascaraConsultaFornecedor() {
		if (getCampoConsultaFornecedor().equals("CPF")) {
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '99.999.999/9999-99', event);";
		} else if (getCampoConsultaFornecedor().equals("CNPJ")) {
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '999.999.999-99', event);";
		} else if (getCampoConsultaFornecedor().equals("codigo")) {
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '99999999999999', event);";
		}
		return "";
	}

	public List<SelectItem> getTipoConsultaComboFornecedor() {
		if (tipoConsultaComboFornecedor == null) {
			tipoConsultaComboFornecedor = new ArrayList<SelectItem>(0);
			tipoConsultaComboFornecedor.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboFornecedor.add(new SelectItem("razaoSocial", "Razão Social"));
			tipoConsultaComboFornecedor.add(new SelectItem("CNPJ", "CNPJ"));
			tipoConsultaComboFornecedor.add(new SelectItem("CPF", "CPF"));
			tipoConsultaComboFornecedor.add(new SelectItem("RG", "RG"));
			tipoConsultaComboFornecedor.add(new SelectItem("codigo", "codigo"));
		}
		return tipoConsultaComboFornecedor;
	}

	public void consultarFornecedor() {
		try {
			super.consultar();
			List<FornecedorVO> objs = new ArrayList<FornecedorVO>(0);
			if (getCampoConsultaFornecedor().equals("codigo")) {
				if (getValorConsultaFornecedor().equals("")) {
					setValorConsultaFornecedor("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaFornecedor());
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCodigo(new Integer(valorInt), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("nome")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorNome(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("razaoSocial")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorRazaoSocial(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("RG")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorRG(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("CPF")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCPF(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("CNPJ")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCNPJ(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}

			setListaConsultaFornecedor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFornecedor(new ArrayList<FornecedorVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCandidato() {
		PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("candidato");
		setCandidatoCpf(obj.getCPF());
		setCandidatoNome(obj.getNome());
		setPessoa(obj.getCodigo());
		setCampoConsultaCandidato("");
		setValorConsultaCandidato("");
		setListaConsultaCandidato(new ArrayList<PessoaVO>(0));
	}

	public void selecionarFuncionario() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		setFuncionarioMatricula(obj.getMatricula());
		setFuncionarioNome(obj.getPessoa().getNome());
		setPessoa(obj.getPessoa().getCodigo());
		setCampoConsultaFuncionario("");
		setValorConsultaFuncionario("");
		setListaConsultaFuncionario(new ArrayList<FuncionarioVO>(0));

	}
	
	public void selecionarParceiro() {
		ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
		setParceiroCPF(obj.getCPF());
		setParceiroCNPJ(obj.getCNPJ());
		setParceiroNome(obj.getNome());
		setParceiroCodigo(obj.getCodigo());
		Uteis.liberarListaMemoria(getListaConsultaCandidato());
		setValorConsultaCandidato("");
		setCampoConsultaCandidato("");
	}	
	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	private List<SelectItem> tipoConsultaComboParceiro;

	public List<SelectItem> getTipoConsultaComboParceiro() {
		if (tipoConsultaComboParceiro == null) {
			tipoConsultaComboParceiro = new ArrayList<SelectItem>(0);
			tipoConsultaComboParceiro.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboParceiro.add(new SelectItem("razaoSocial", "Razão Social"));
			tipoConsultaComboParceiro.add(new SelectItem("RG", "RG"));
			tipoConsultaComboParceiro.add(new SelectItem("CPF", "CPF"));
			tipoConsultaComboParceiro.add(new SelectItem("tipoParceiro", "Tipo Parceiro"));
		}
		return tipoConsultaComboParceiro;
	}
	
	public void consultarParceiro() {
		try {
			super.consultar();
			List<ParceiroVO> objs = new ArrayList<ParceiroVO>(0);
			if (getCampoConsultaParceiro().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getParceiroFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("nome")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorNome(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("razaoSocial")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorRazaoSocial(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("RG")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorRG(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("CPF")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorCPF(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("tipoParceiro")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorTipoParceiro(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaParceiro(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaParceiro(new ArrayList<ParceiroVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<SelectItem> listaSelectItemTipoPessoa;

	public List<SelectItem> getListaSelectItemTipoPessoa() {
		if (listaSelectItemTipoPessoa == null) {
			listaSelectItemTipoPessoa = new ArrayList<SelectItem>(0);
			listaSelectItemTipoPessoa.add(new SelectItem("", ""));
			listaSelectItemTipoPessoa.add(new SelectItem(TipoPessoa.ALUNO.getValor(), TipoPessoa.ALUNO.getDescricao()));
			listaSelectItemTipoPessoa.add(new SelectItem(TipoPessoa.CANDIDATO.getValor(), TipoPessoa.CANDIDATO.getDescricao()));
			listaSelectItemTipoPessoa.add(new SelectItem(TipoPessoa.FORNECEDOR.getValor(), TipoPessoa.FORNECEDOR.getDescricao()));
			listaSelectItemTipoPessoa.add(new SelectItem(TipoPessoa.FUNCIONARIO.getValor(), TipoPessoa.FUNCIONARIO.getDescricao()));
			listaSelectItemTipoPessoa.add(new SelectItem(TipoPessoa.PARCEIRO.getValor(), TipoPessoa.PARCEIRO.getDescricao()));
			listaSelectItemTipoPessoa.add(new SelectItem(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor(), TipoPessoa.RESPONSAVEL_FINANCEIRO.getDescricao()));
		}
		return listaSelectItemTipoPessoa;
	}

	public List<SelectItem> listaSelectItemFiltrarPorPeriodo;
	
	public List<SelectItem> getListaSelectItemFiltrarPorPeriodo() {
		if (listaSelectItemFiltrarPorPeriodo == null) {
			listaSelectItemFiltrarPorPeriodo = new ArrayList<SelectItem>(0);
			listaSelectItemFiltrarPorPeriodo.add(new SelectItem("DR", "Data Recebimento"));
			listaSelectItemFiltrarPorPeriodo.add(new SelectItem("DP", "Data Previsão Recebimento"));
		}
		return listaSelectItemFiltrarPorPeriodo;
	}
	
	public List<SelectItem> listaSelectItemSituacaoBaixa;
	
	public List<SelectItem> getListaSelectItemSituacaoBaixa() {
		if (listaSelectItemSituacaoBaixa == null) {
			listaSelectItemSituacaoBaixa = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoBaixa.add(new SelectItem("", "Todas"));
			listaSelectItemSituacaoBaixa.add(new SelectItem("CO", "Compensado"));
			listaSelectItemSituacaoBaixa.add(new SelectItem("NC", "Não Compensado"));
		}
		return listaSelectItemSituacaoBaixa;
	}
	
	public List<SelectItem> listaSelectItemLayout;
	
	public List<SelectItem> getListaSelectItemLayout() {
		if (listaSelectItemLayout == null) {
			listaSelectItemLayout = new ArrayList<SelectItem>(0);
			listaSelectItemLayout.add(new SelectItem("AN", "Analítico"));
			listaSelectItemLayout.add(new SelectItem("SI", "Sintético"));
		}
		return listaSelectItemLayout;
	}
	
	public Boolean getAluno() {
		return getTipoPessoa().equals(TipoPessoa.ALUNO.getValor());
	}

	public Boolean getCandidato() {
		return getTipoPessoa().equals(TipoPessoa.CANDIDATO.getValor());
	}

	public Boolean getFuncionario() {
		return getTipoPessoa().equals(TipoPessoa.FUNCIONARIO.getValor());
	}

	public Boolean getParceiro() {
		return getTipoPessoa().equals(TipoPessoa.PARCEIRO.getValor());
	}

	public Boolean getIsResponsavelFinanceiro() {
		return getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor());
	}

	public String getTipoPessoa() {
		if (tipoPessoa == null) {
			tipoPessoa = "";
		}
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public String getFuncionarioMatricula() {
		if (funcionarioMatricula == null) {
			funcionarioMatricula = "";
		}
		return funcionarioMatricula;
	}

	public void setFuncionarioMatricula(String funcionarioMatricula) {
		this.funcionarioMatricula = funcionarioMatricula;
	}

	public String getFuncionarioNome() {
		if (funcionarioNome == null) {
			funcionarioNome = "";
		}
		return funcionarioNome;
	}

	public void setFuncionarioNome(String funcionarioNome) {
		this.funcionarioNome = funcionarioNome;
	}

	public String getCandidatoCpf() {
		if (candidatoCpf == null) {
			candidatoCpf = "";
		}
		return candidatoCpf;
	}

	public void setCandidatoCpf(String candidatoCpf) {
		this.candidatoCpf = candidatoCpf;
	}

	public String getCandidatoNome() {
		if (candidatoNome == null) {
			candidatoNome = "";
		}
		return candidatoNome;
	}

	public void setCandidatoNome(String candidatoNome) {
		this.candidatoNome = candidatoNome;
	}

	public String getParceiroCPF() {
		if (parceiroCPF == null) {
			parceiroCPF = "";
		}
		return parceiroCPF;
	}

	public void setParceiroCPF(String parceiroCPF) {
		this.parceiroCPF = parceiroCPF;
	}

	public String getParceiroCNPJ() {
		if (parceiroCNPJ == null) {
			parceiroCNPJ = "";
		}
		return parceiroCNPJ;
	}

	public void setParceiroCNPJ(String parceiroCNPJ) {
		this.parceiroCNPJ = parceiroCNPJ;
	}

	public String getParceiroNome() {
		if (parceiroNome == null) {
			parceiroNome = "";
		}
		return parceiroNome;
	}

	public void setParceiroNome(String parceiroNome) {
		this.parceiroNome = parceiroNome;
	}
	
	public List<FornecedorVO> getListaConsultaFornecedor() {
		if (listaConsultaFornecedor == null) {
			listaConsultaFornecedor = new ArrayList<FornecedorVO>(0);
		}
		return listaConsultaFornecedor;
	}

	public void setListaConsultaFornecedor(List<FornecedorVO> listaConsultaFornecedor) {
		this.listaConsultaFornecedor = listaConsultaFornecedor;
	}

	public String getValorConsultaFornecedor() {
		if (valorConsultaFornecedor == null) {
			valorConsultaFornecedor = "";
		}
		return valorConsultaFornecedor;
	}

	public void setValorConsultaFornecedor(String valorConsultaFornecedor) {
		this.valorConsultaFornecedor = valorConsultaFornecedor;
	}

	public String getCampoConsultaFornecedor() {
		if (campoConsultaFornecedor == null) {
			campoConsultaFornecedor = "";
		}
		return campoConsultaFornecedor;
	}

	public void setCampoConsultaFornecedor(String campoConsultaFornecedor) {
		this.campoConsultaFornecedor = campoConsultaFornecedor;
	}

	public Boolean getFornecedor() {
		return getTipoPessoa().equals(TipoPessoa.FORNECEDOR.getValor());
	}

	public String getFornecedorNome() {
		return fornecedorNome;
	}

	public void setFornecedorNome(String fornecedorNome) {
		this.fornecedorNome = fornecedorNome;
	}

	public Integer getFornecedorCodigo() {
		return fornecedorCodigo;
	}

	public void setFornecedorCodigo(Integer fornecedorCodigo) {
		this.fornecedorCodigo = fornecedorCodigo;
	}
	
	public Integer getParceiroCodigo() {
		if (parceiroCodigo == null) {
			parceiroCodigo = 0;
		}
		return parceiroCodigo;
	}

	public void setParceiroCodigo(Integer parceiroCodigo) {
		this.parceiroCodigo = parceiroCodigo;
	}

	public String getCampoConsultaFuncionario() {
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public List<PessoaVO> getListaConsultaCandidato() {
		return listaConsultaCandidato;
	}

	public void setListaConsultaCandidato(List<PessoaVO> listaConsultaCandidato) {
		this.listaConsultaCandidato = listaConsultaCandidato;
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

	public String getCampoConsultaCandidato() {
		return campoConsultaCandidato;
	}

	public void setCampoConsultaCandidato(String campoConsultaCandidato) {
		this.campoConsultaCandidato = campoConsultaCandidato;
	}

	public String getValorConsultaCandidato() {
		return valorConsultaCandidato;
	}

	public void setValorConsultaCandidato(String valorConsultaCandidato) {
		this.valorConsultaCandidato = valorConsultaCandidato;
	}
	
	public Integer getPessoa() {
		if (pessoa == null) {
			pessoa = 0;
		}
		return pessoa;
	}

	public void setPessoa(Integer pessoa) {
		this.pessoa = pessoa;
	}

	public List<ParceiroVO> getListaConsultaParceiro() {
		return listaConsultaParceiro;
	}

	public void setListaConsultaParceiro(List<ParceiroVO> listaConsultaParceiro) {
		this.listaConsultaParceiro = listaConsultaParceiro;
	}

	public String getValorConsultaParceiro() {
		return valorConsultaParceiro;
	}

	public void setValorConsultaParceiro(String valorConsultaParceiro) {
		this.valorConsultaParceiro = valorConsultaParceiro;
	}

	public String getCampoConsultaParceiro() {
		return campoConsultaParceiro;
	}

	public void setCampoConsultaParceiro(String campoConsultaParceiro) {
		this.campoConsultaParceiro = campoConsultaParceiro;
	}

	public String getFiltrarPor() {
		if (filtrarPor == null) {
			filtrarPor = "DP";
		}
		return filtrarPor;
	}

	public void setFiltrarPor(String filtrarPor) {
		this.filtrarPor = filtrarPor;
	}

	public String getSituacaoBaixa() {
		if (situacaoBaixa == null) {
			situacaoBaixa = "";
		}
		return situacaoBaixa;
	}

	public void setSituacaoBaixa(String situacaoBaixa) {
		this.situacaoBaixa = situacaoBaixa;
	}

	public String getLayout() {
		if (layout == null) {
			layout = "SI";
		}
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public void limparOperadoraCartao() {
		try {
			super.limparOperadoraCartao();
			setOperadoraCartaoVO(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	

}
