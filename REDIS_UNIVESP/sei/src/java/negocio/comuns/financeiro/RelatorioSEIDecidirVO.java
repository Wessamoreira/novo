package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import controle.arquitetura.TreeNodeCustomizado;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularEstagioVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirArquivoVO;
import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.bancocurriculum.CandidatosVagasVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.SecaoVO;
import negocio.comuns.biblioteca.TipoCatalogoVO;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.enumerador.TipoFiltroPeriodoSeiDecidirEnum;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.planoorcamentario.PlanoOrcamentarioVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.enumeradores.SituacaoResultadoProcessoSeletivoEnum;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.recursoshumanos.CompetenciaPeriodoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.TemplateLancamentoFolhaPagamentoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.comuns.processosel.FiltroRelatorioProcessoSeletivoVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.crm.FiltroRelatorioCompromissoAgendaVO;

/**
 * Responsável por manter os dados de varias outras entidades para emissao de
 * relatorio especifico. Classe do tipo VO - Value Object composta pelos
 * atributos da entidade com visibilidade protegida e os metodos de acesso a
 * estes atributos. Classe utilizada para apresentar e manter em memória os
 * dados desta entidade.
 * 
 * @author Leonardo Riciolle
 */
public class RelatorioSEIDecidirVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	private FiltroRelatorioAcademicoVO relatorioAcademicoVO;
	private FiltroRelatorioFinanceiroVO relatorioFinanceiroVO;
	private FiltroRelatorioCompromissoAgendaVO relatorioCompromissoAgendaVO;
	private String unidadeEnsinoApresentar;
	private List<UnidadeEnsinoVO> unidadeEnsinoVOs;
	private CursoVO cursoVO;
	private String cursoApresentar;
	private List<CursoVO> cursoVOs;
	private String turnoApresentar;
	private List<TurnoVO> turnoVOs;
	private TurmaVO turmaVO;
	private String nivelEducacional;
	private String centroReceitaApresentar;
	private List<CentroReceitaVO> centroReceitaVOs;
	private String categoriaDespesaApresentar;
	private List<CategoriaDespesaVO> categoriaDespesaVOs;
	/**
	 * Estagio
	 */
	private String concedente;
	private String gradeCurricularEstagioApresentar;
	private List<GradeCurricularEstagioVO> gradeCurricularEstagioVOs;
	private Integer valorBuscaInicio;
	private Integer valorBuscaFinal;
	private Integer valorBuscaInicio1;
	private Integer valorBuscaFinal1;
	private boolean filtrarAguardandoAssinatura = false;
	private boolean filtrarEmRealizacao = false;
	private boolean filtrarEmAnalise = false;
	private boolean filtrarEmCorrecao = false;
	private boolean filtrarDeferido = false;
	private boolean filtrarIndeferido = false;
	
	private Integer qtdNivelExistenteCentroResultado;
	private String centroResultadoApresentar;
	private List<CentroResultadoVO> listaCentroResultadoVO;
	private TreeNodeCustomizado arvoreCentroResultado;
	
	/**
	 * Implementar esses cara depois private ConvenioVO convenioVO; private
	 * PlanoDescontoContaReceberVO planoDescontoContaReceberVO; private
	 * PlanoFinanceiroAlunoDescricaoDescontosVO
	 * planoFinanceiroAlunoDescricaoDescontosVO;
	 */
	private ContaCorrenteVO contaCorrenteVO;
	private PessoaVO pessoaVO;
	private TipoPessoa tipoPessoaEnum;
	private TipoSacado tipoSacadoEnum;
	private MatriculaVO matriculaVO;
	private FuncionarioVO funcionarioVO;
	private FuncionarioCargoVO funcionarioCargoVO;

	private CandidatosVagasVO candidatosVagasVO;
	private FornecedorVO fornecedorVO;
	private ParceiroVO parceiroVO;
	private BancoVO bancoVO;
	private OperadoraCartaoVO operadoraCartaoVO;
	private LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO;
	
	
	private List<FuncionarioVO> listaFuncionarioVOs;
	private String funcionarioApresentar;
	private List<CampanhaVO> listaCampanhaVOs;
	private String campanhaApresentar;

	private TipoFiltroPeriodoSeiDecidirEnum tipoFiltroPeriodo;
	private String ano;
	private String semestre;
	private Date dataInicio;
	private Date dataFim;
	private Date dataInicio2;
	private Date dataFim2;
	private Date dataInicio3;
	private Date dataFim3;
	private Date dataInicio4;
	private Date dataFim4;
	private Date dataInicio5;
	private Date dataFim5;
	private Date dataBase;
	private boolean considerarUnidadeEnsinoFinanceira = false;

	private Date dataInicioPeriodo;
	private Date dataFimPeriodo;
	private CompetenciaPeriodoFolhaPagamentoVO periodo;
	private TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamento;

	private EventoFolhaPagamentoVO eventoFolhaPagamentoVO;
	private DepartamentoVO departamentoVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private CargoVO cargoVO;

	private LayoutRelatorioSEIDecidirArquivoVO layoutRelatorioSEIDecidirArquivoVO;

	private String situacaoPeriodoAquisitivo;
	private String motivoProgressaoSalarial;
	
	private FormaPagamentoVO formaPagamentoVO;
	private List<FormaPagamentoVO> formaPagamentoVOs;
	
	private Date periodoFechamentoMesAno;
	
	private String[] situacoes;
	private String[] tipoAutorizacoes;
	private StringBuilder filtro = new StringBuilder();
	
	private List<PlanoOrcamentarioVO> planosOrcamentarioVOs;
	private List<CategoriaDespesaVO> categoriasDespesaVOs;
	private List<CategoriaProdutoVO> categoriasProdutoVOs;
	private List<CentroResultadoVO> centrosResultadoVOs;
	private List<DepartamentoVO> departamentosVOs;

	private boolean considerarCategoriaDespesaArvoreInferior;
	private boolean considerarCategoriaProdutoArvoreInferior;
	private boolean considerarCentroResultadoArvoreInferior;
	

	private Date dataInicioPeriodoAutorizacaoRequisicao;
	private Date dataFimPeriodoAutorizacaoRequisicao;
	private Date dataInicioPeriodoEntregaRequisicao;
	private Date dataFimPeriodoEntregaRequisicao;
	
	private DisciplinaVO disciplinaVO;
	private TurmaVO turmaEstudouDisciplinaVO;
	
	//Requerimento
	private TipoRequerimentoVO tipoRequerimentoVO;
	private TurmaVO turmaReposicao;
	private PessoaVO requerenteVO;
	private PessoaVO coordenadorVO;
	private Integer situacaoRequerimentoDepartamento;
	private String filtrarPeriodoPor;
	private Boolean finalizadoDeferido;
	private Boolean finalizadoIndeferido;
	private Boolean emExecucao;
	private Boolean pendente;
	private Boolean aguardandoPagamento;
	private Boolean canceladoFinanceiramente;
	private Boolean solicitacaoIsencao;
	private Boolean solicitacaoIsencaoDeferido;
	private Boolean solicitacaoIsencaoIndeferido;
	private Boolean aguardandoAutorizacaoPagamento;
	private Boolean isento;
	private Boolean pago;
	private Boolean prontoRetirada;
	private Boolean atrasado;
	private Boolean canceladoFinanceiro;
		
	private ProcSeletivoVO procSeletivoVO;
	private InscricaoVO inscricaoVO;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO;
	private FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivoVO;
    private Boolean marcarTodasSituacoesInscricao;
    private SituacaoResultadoProcessoSeletivoEnum situacaoResultadoProcessoSeletivo;
    
    private String tipoFiltroDocumentoAssinado;
    private Date dataDocumentoInicio;
	private Date dataDocumentoFim;
    
    
    private Boolean considerarTodasMatriculasAluno;
    
  //Biblioteca
    private BibliotecaVO bibliotecaVO;
    private CatalogoVO catalogoVO;
    private TipoCatalogoVO tipoCatalogoVO;
    private String tituloCatalogo;
    private String situacaoEmprestimo;
    private Date dataInicioEmprestimo;
    private Date dataFimEmprestimo;
    private SecaoVO secao;
    private AreaConhecimentoVO areaConhecimentoVO;
    private String tipoEntrada;
    private String classificacaoBibliografica;
    private Date dataInicioAquisicaoExemplar;
    private Date dataFimAquisicaoExemplar;
    private FuncionarioVO professorVO;
    private String tipoCatalogoPeriodico;
    
    private Integer periodoLetivo;
    private Date dataProvaInicio;
    private Date dataProvaFim;
    private String codigosGradeCurricularEstagio;
    
	public RelatorioSEIDecidirVO() {
		super();
		getRelatorioAcademicoVO().setPendenteFinanceiro(false);
		getRelatorioAcademicoVO().setConfirmado(false);
	}

	public Date getDataInicio() {
	return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public FiltroRelatorioAcademicoVO getRelatorioAcademicoVO() {
		if (relatorioAcademicoVO == null) {
			relatorioAcademicoVO = new FiltroRelatorioAcademicoVO();
		}
		return relatorioAcademicoVO;
	}

	public void setRelatorioAcademicoVO(FiltroRelatorioAcademicoVO relatorioAcademicoVO) {
		this.relatorioAcademicoVO = relatorioAcademicoVO;
	}

	public FiltroRelatorioFinanceiroVO getRelatorioFinanceiroVO() {
		if (relatorioFinanceiroVO == null) {
			relatorioFinanceiroVO = new FiltroRelatorioFinanceiroVO(false);
		}
		return relatorioFinanceiroVO;
	}

	public void setRelatorioFinanceiroVO(FiltroRelatorioFinanceiroVO relatorioFinanceiroVO) {
		this.relatorioFinanceiroVO = relatorioFinanceiroVO;
	}

	public FiltroRelatorioCompromissoAgendaVO getRelatorioCompromissoAgendaVO() {
		if (relatorioCompromissoAgendaVO == null) {
			relatorioCompromissoAgendaVO = new FiltroRelatorioCompromissoAgendaVO();
		}
		return relatorioCompromissoAgendaVO;
	}

	public void setRelatorioCompromissoAgendaVO(FiltroRelatorioCompromissoAgendaVO relatorioCompromissoAgendaVO) {
		this.relatorioCompromissoAgendaVO = relatorioCompromissoAgendaVO;
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

	public ContaCorrenteVO getContaCorrenteVO() {
		if (contaCorrenteVO == null) {
			contaCorrenteVO = new ContaCorrenteVO();
		}
		return contaCorrenteVO;
	}

	public void setContaCorrenteVO(ContaCorrenteVO contaCorrenteVO) {
		this.contaCorrenteVO = contaCorrenteVO;
	}

	public PessoaVO getPessoaVO() {
		if (pessoaVO == null) {
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	public TipoPessoa getTipoPessoaEnum() {
		if (tipoPessoaEnum == null) {
			tipoPessoaEnum = TipoPessoa.NENHUM;
		}
		return tipoPessoaEnum;
	}

	public void setTipoPessoaEnum(TipoPessoa tipoPessoaEnum) {
		this.tipoPessoaEnum = tipoPessoaEnum;
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

	public FuncionarioVO getFuncionarioVO() {
		if (funcionarioVO == null) {
			funcionarioVO = new FuncionarioVO();
		}
		return funcionarioVO;
	}

	public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
		this.funcionarioVO = funcionarioVO;
	}

	public CandidatosVagasVO getCandidatosVagasVO() {
		if (candidatosVagasVO == null) {
			candidatosVagasVO = new CandidatosVagasVO();
		}
		return candidatosVagasVO;
	}

	public void setCandidatosVagasVO(CandidatosVagasVO candidatosVagasVO) {
		this.candidatosVagasVO = candidatosVagasVO;
	}

	public FornecedorVO getFornecedorVO() {
		if (fornecedorVO == null) {
			fornecedorVO = new FornecedorVO();
		}
		return fornecedorVO;
	}

	public void setFornecedorVO(FornecedorVO fornecedorVO) {
		this.fornecedorVO = fornecedorVO;
	}

	public ParceiroVO getParceiroVO() {
		if (parceiroVO == null) {
			parceiroVO = new ParceiroVO();
		}
		return parceiroVO;
	}

	public void setParceiroVO(ParceiroVO parceiroVO) {
		this.parceiroVO = parceiroVO;
	}

	public String getNivelEducacional() {
		if (nivelEducacional == null) {
			nivelEducacional = "";
		}
		return nivelEducacional;
	}

	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}

	public LayoutRelatorioSEIDecidirVO getLayoutRelatorioSEIDecidirVO() {
		if (layoutRelatorioSEIDecidirVO == null) {
			layoutRelatorioSEIDecidirVO = new LayoutRelatorioSEIDecidirVO();
		}
		return layoutRelatorioSEIDecidirVO;
	}

	public void setLayoutRelatorioSEIDecidirVO(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO) {
		this.layoutRelatorioSEIDecidirVO = layoutRelatorioSEIDecidirVO;
	}

	public String getUnidadeEnsinoApresentar() {
		if (unidadeEnsinoApresentar == null) {
			unidadeEnsinoApresentar = "";
		}
		return unidadeEnsinoApresentar;
	}

	public void setUnidadeEnsinoApresentar(String unidadeEnsinoApresentar) {
		this.unidadeEnsinoApresentar = unidadeEnsinoApresentar;
	}

	public List<UnidadeEnsinoVO> getUnidadeEnsinoVOs() {
		if (unidadeEnsinoVOs == null) {
			unidadeEnsinoVOs = new ArrayList<UnidadeEnsinoVO>(0);
		}
		return unidadeEnsinoVOs;
	}

	public void setUnidadeEnsinoVOs(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
		this.unidadeEnsinoVOs = unidadeEnsinoVOs;
	}

	public String getCursoApresentar() {
		if (cursoApresentar == null) {
			cursoApresentar = "";
		}
		return cursoApresentar;
	}

	public void setCursoApresentar(String cursoApresentar) {
		this.cursoApresentar = cursoApresentar;
	}

	public List<CursoVO> getCursoVOs() {
		if (cursoVOs == null) {
			cursoVOs = new ArrayList<CursoVO>(0);
		}
		return cursoVOs;
	}

	public void setCursoVOs(List<CursoVO> cursoVOs) {
		this.cursoVOs = cursoVOs;
	}

	public String getTurnoApresentar() {
		if (turnoApresentar == null) {
			turnoApresentar = "";
		}
		return turnoApresentar;
	}

	public void setTurnoApresentar(String turnoApresentar) {
		this.turnoApresentar = turnoApresentar;
	}

	public List<TurnoVO> getTurnoVOs() {
		if (turnoVOs == null) {
			turnoVOs = new ArrayList<TurnoVO>(0);
		}
		return turnoVOs;
	}

	public void setTurnoVOs(List<TurnoVO> turnoVOs) {
		this.turnoVOs = turnoVOs;
	}

	public String getCentroReceitaApresentar() {
		if (centroReceitaApresentar == null) {
			centroReceitaApresentar = "";
		}
		return centroReceitaApresentar;
	}

	public void setCentroReceitaApresentar(String centroReceitaApresentar) {
		this.centroReceitaApresentar = centroReceitaApresentar;
	}

	public List<CentroReceitaVO> getCentroReceitaVOs() {
		if (centroReceitaVOs == null) {
			centroReceitaVOs = new ArrayList<CentroReceitaVO>(0);
		}
		return centroReceitaVOs;
	}

	public void setCentroReceitaVOs(List<CentroReceitaVO> centroReceitaVOs) {
		this.centroReceitaVOs = centroReceitaVOs;
	}
	
	public List<FuncionarioVO> getListaFuncionarioVOs() {
		if (listaFuncionarioVOs == null) {
			listaFuncionarioVOs = new ArrayList<>();
		}
		return listaFuncionarioVOs;
	}

	public void setListaFuncionarioVOs(List<FuncionarioVO> listaFuncionarioVOs) {
		this.listaFuncionarioVOs = listaFuncionarioVOs;
	}

	public List<CampanhaVO> getListaCampanhaVOs() {
		if (listaCampanhaVOs == null) {
			listaCampanhaVOs = new ArrayList<>();
		}
		return listaCampanhaVOs;
	}

	public void setListaCampanhaVOs(List<CampanhaVO> listaCampanhaVOs) {
		this.listaCampanhaVOs = listaCampanhaVOs;
	}

	public String getFuncionarioApresentar() {
		if (funcionarioApresentar == null) {
			funcionarioApresentar = "";
		}
		return funcionarioApresentar;
	}

	public void setFuncionarioApresentar(String funcionarioApresentar) {
		this.funcionarioApresentar = funcionarioApresentar;
	}

	public String getCampanhaApresentar() {
		if (campanhaApresentar == null) {
			campanhaApresentar = "";
		}
		return campanhaApresentar;
	}

	public void setCampanhaApresentar(String campanhaApresentar) {
		this.campanhaApresentar = campanhaApresentar;
	}

	public TipoFiltroPeriodoSeiDecidirEnum getTipoFiltroPeriodo() {
		if (tipoFiltroPeriodo == null) {
			tipoFiltroPeriodo = TipoFiltroPeriodoSeiDecidirEnum.NENHUM;
		}
		return tipoFiltroPeriodo;
	}

	public void setTipoFiltroPeriodo(TipoFiltroPeriodoSeiDecidirEnum tipoFiltroPeriodo) {
		this.tipoFiltroPeriodo = tipoFiltroPeriodo;
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

	public Date getDataInicio2() {

		return dataInicio2;
	}

	public void setDataInicio2(Date dataInicio2) {
		this.dataInicio2 = dataInicio2;
	}

	public Date getDataFim2() {
		return dataFim2;
	}

	public void setDataFim2(Date dataFim2) {
		this.dataFim2 = dataFim2;
	}

	public Date getDataInicio3() {		
		return dataInicio3;
	}

	public void setDataInicio3(Date dataInicio3) {
		this.dataInicio3 = dataInicio3;
	}

	public Date getDataFim3() {		
		return dataFim3;
	}

	public void setDataFim3(Date dataFim3) {
		this.dataFim3 = dataFim3;
	}

	public Date getDataInicio4() {		
		return dataInicio4;
	}

	public void setDataInicio4(Date dataInicio4) {
		this.dataInicio4 = dataInicio4;
	}

	public Date getDataFim4() {		
		return dataFim4;
	}

	public void setDataFim4(Date dataFim4) {
		this.dataFim4 = dataFim4;
	}

	public Date getDataInicio5() {		
		return dataInicio5;
	}

	public void setDataInicio5(Date dataInicio5) {
		this.dataInicio5 = dataInicio5;
	}

	public Date getDataFim5() {		
		return dataFim5;
	}

	public void setDataFim5(Date dataFim5) {
		this.dataFim5 = dataFim5;
	}

	public Date getDataBase() {
		return dataBase;
	}

	public void setDataBase(Date dataBase) {
		this.dataBase = dataBase;
	}

	public TipoSacado getTipoSacadoEnum() {
		return tipoSacadoEnum;
	}

	public void setTipoSacadoEnum(TipoSacado tipoSacadoEnum) {
		this.tipoSacadoEnum = tipoSacadoEnum;
	}

	public List<CategoriaDespesaVO> getCategoriaDespesaVOs() {
		if (categoriaDespesaVOs == null) {
			categoriaDespesaVOs = new ArrayList<CategoriaDespesaVO>(0);
		}
		return categoriaDespesaVOs;
	}

	public void setCategoriaDespesaVOs(List<CategoriaDespesaVO> categoriaDespesaVOs) {
		this.categoriaDespesaVOs = categoriaDespesaVOs;
	}

	public BancoVO getBancoVO() {
		if (bancoVO == null) {
			bancoVO = new BancoVO();
		}
		return bancoVO;
	}

	public void setBancoVO(BancoVO bancoVO) {
		this.bancoVO = bancoVO;
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

	public String getCategoriaDespesaApresentar() {
		if (categoriaDespesaApresentar == null) {
			categoriaDespesaApresentar = "";
		}
		return categoriaDespesaApresentar;
	}

	public void setCategoriaDespesaApresentar(String categoriaDespesaApresentar) {
		this.categoriaDespesaApresentar = categoriaDespesaApresentar;
	}

	public String getConcedente() {
		if (concedente == null) {
			concedente = "";
		}
		return concedente;
	}

	public void setConcedente(String concedente) {
		this.concedente = concedente;
	}

	public String getGradeCurricularEstagioApresentar() {
		if (gradeCurricularEstagioApresentar == null) {
			gradeCurricularEstagioApresentar = "";
		}
		return gradeCurricularEstagioApresentar;
	}

	public void setGradeCurricularEstagioApresentar(String gradeCurricularEstagioApresentar) {
		this.gradeCurricularEstagioApresentar = gradeCurricularEstagioApresentar;
	}

	public List<GradeCurricularEstagioVO> getGradeCurricularEstagioVOs() {
		if (gradeCurricularEstagioVOs == null) {
			gradeCurricularEstagioVOs = new ArrayList<>();
		}
		return gradeCurricularEstagioVOs;
	}

	public void setGradeCurricularEstagioVOs(List<GradeCurricularEstagioVO> gradeCurricularEstagioVOs) {
		this.gradeCurricularEstagioVOs = gradeCurricularEstagioVOs;
	}

	

	public Integer getValorBuscaInicio() {
		if (valorBuscaInicio == null) {
			valorBuscaInicio = 0;
		}
		return valorBuscaInicio;
	}

	public void setValorBuscaInicio(Integer valorBuscaInicio) {
		this.valorBuscaInicio = valorBuscaInicio;
	}

	public Integer getValorBuscaFinal() {
		if (valorBuscaFinal == null) {
			valorBuscaFinal = 100;
		}
		return valorBuscaFinal;
	}

	public void setValorBuscaFinal(Integer valorBuscaFinal) {
		this.valorBuscaFinal = valorBuscaFinal;
	}

	public Integer getValorBuscaInicio1() {
		if (valorBuscaInicio1 == null) {
			valorBuscaInicio1 = 0;
		}
		return valorBuscaInicio1;
	}

	public void setValorBuscaInicio1(Integer valorBuscaInicio1) {
		this.valorBuscaInicio1 = valorBuscaInicio1;
	}

	public Integer getValorBuscaFinal1() {
		if (valorBuscaFinal1 == null) {
			valorBuscaFinal1 = 100;
		}
		return valorBuscaFinal1;
	}

	public void setValorBuscaFinal1(Integer valorBuscaFinal1) {
		this.valorBuscaFinal1 = valorBuscaFinal1;
	}

	public boolean isFiltrarAguardandoAssinatura() {
		return filtrarAguardandoAssinatura;
	}

	public void setFiltrarAguardandoAssinatura(boolean filtrarAguardandoAssinatura) {
		this.filtrarAguardandoAssinatura = filtrarAguardandoAssinatura;
	}

	public boolean isFiltrarEmRealizacao() {		
		return filtrarEmRealizacao;
	}

	public void setFiltrarEmRealizacao(boolean filtrarEmRealizacao) {
		this.filtrarEmRealizacao = filtrarEmRealizacao;
	}	

	public boolean isFiltrarEmAnalise() {		
		return filtrarEmAnalise;
	}

	public void setFiltrarEmAnalise(boolean filtrarEmAnalise) {
		this.filtrarEmAnalise = filtrarEmAnalise;
	}

	public boolean isFiltrarEmCorrecao() {		
		return filtrarEmCorrecao;
	}

	public void setFiltrarEmCorrecao(boolean filtrarEmCorrecao) {
		this.filtrarEmCorrecao = filtrarEmCorrecao;
	}

	public boolean isFiltrarDeferido() {		
		return filtrarDeferido;
	}

	public void setFiltrarDeferido(boolean filtrarDeferido) {
		this.filtrarDeferido = filtrarDeferido;
	}

	public boolean isFiltrarIndeferido() {		
		return filtrarIndeferido;
	}

	public void setFiltrarIndeferido(boolean filtrarIndeferido) {
		this.filtrarIndeferido = filtrarIndeferido;
	}

	public Integer getQtdNivelExistenteCentroResultado() {
		if (qtdNivelExistenteCentroResultado == null) {
			qtdNivelExistenteCentroResultado = 0;
		}
		return qtdNivelExistenteCentroResultado;
	}

	public void setQtdNivelExistenteCentroResultado(Integer qtdNivelExistenteCentroResultado) {
		this.qtdNivelExistenteCentroResultado = qtdNivelExistenteCentroResultado;
	}

	public String getCentroResultadoApresentar() {
		if (centroResultadoApresentar == null) {
			centroResultadoApresentar = "";
		}
		return centroResultadoApresentar;
	}

	public void setCentroResultadoApresentar(String centroResultadoApresentar) {
		this.centroResultadoApresentar = centroResultadoApresentar;
	}

	public TreeNodeCustomizado getArvoreCentroResultado() {
		if (arvoreCentroResultado == null) {
			arvoreCentroResultado = new TreeNodeCustomizado();
		}
		return arvoreCentroResultado;
	}

	public void setArvoreCentroResultado(TreeNodeCustomizado arvoreCentroResultado) {
		this.arvoreCentroResultado = arvoreCentroResultado;
	}
	

	public List<CentroResultadoVO> getListaCentroResultadoVO() {
		if (listaCentroResultadoVO == null) {
			listaCentroResultadoVO = new ArrayList<>(0);
		}
		return listaCentroResultadoVO;
	}

	public void setListaCentroResultadoVO(List<CentroResultadoVO> listaCentroResultadoVO) {
		this.listaCentroResultadoVO = listaCentroResultadoVO;
	}
	

	public boolean isConsiderarUnidadeEnsinoFinanceira() {
		return considerarUnidadeEnsinoFinanceira;
	}

	public void setConsiderarUnidadeEnsinoFinanceira(boolean considerarUnidadeEnsinoFinanceira) {
		this.considerarUnidadeEnsinoFinanceira = considerarUnidadeEnsinoFinanceira;
	}

	public Date getDataCompetencia() {
		return dataInicioPeriodo;
	}

	public void setDataCompetencia(Date dataCompetencia) {
		this.dataInicioPeriodo = dataCompetencia;
	}

	public CompetenciaPeriodoFolhaPagamentoVO getPeriodo() {
		if (periodo == null) {
			periodo = new CompetenciaPeriodoFolhaPagamentoVO();
		}
		return periodo;
	}

	public void setPeriodo(CompetenciaPeriodoFolhaPagamentoVO periodo) {
		this.periodo = periodo;
	}

	public TemplateLancamentoFolhaPagamentoVO getTemplateLancamentoFolhaPagamento() {
		if (templateLancamentoFolhaPagamento == null) {
			templateLancamentoFolhaPagamento = new TemplateLancamentoFolhaPagamentoVO();
		}
		return templateLancamentoFolhaPagamento;
	}

	public void setTemplateLancamentoFolhaPagamento(
			TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamento) {
		this.templateLancamentoFolhaPagamento = templateLancamentoFolhaPagamento;
	}

	public EventoFolhaPagamentoVO getEventoFolhaPagamentoVO() {
		if (eventoFolhaPagamentoVO == null) {
			eventoFolhaPagamentoVO = new EventoFolhaPagamentoVO();
		}
		return eventoFolhaPagamentoVO;
	}

	public void setEventoFolhaPagamentoVO(EventoFolhaPagamentoVO eventoFolhaPagamentoVO) {
		this.eventoFolhaPagamentoVO = eventoFolhaPagamentoVO;
	}

	public DepartamentoVO getDepartamentoVO() {
		if (departamentoVO == null) {
			departamentoVO = new DepartamentoVO();
		}
		return departamentoVO;
	}

	public void setDepartamentoVO(DepartamentoVO departamentoVO) {
		this.departamentoVO = departamentoVO;
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

	public CargoVO getCargoVO() {
		if (cargoVO == null) {
			cargoVO = new CargoVO();
		}
		return cargoVO;
	}

	public void setCargoVO(CargoVO cargoVO) {
		this.cargoVO = cargoVO;
	}

	public LayoutRelatorioSEIDecidirArquivoVO getLayoutRelatorioSEIDecidirArquivoVO() {
		if (layoutRelatorioSEIDecidirArquivoVO == null) {
			layoutRelatorioSEIDecidirArquivoVO = new LayoutRelatorioSEIDecidirArquivoVO();
		}
		return layoutRelatorioSEIDecidirArquivoVO;
	}

	public void setLayoutRelatorioSEIDecidirArquivoVO(
			LayoutRelatorioSEIDecidirArquivoVO layoutRelatorioSEIDecidirArquivoVO) {
		this.layoutRelatorioSEIDecidirArquivoVO = layoutRelatorioSEIDecidirArquivoVO;
	}

	public Date getDataInicioPeriodo() {
		return dataInicioPeriodo;
	}

	public void setDataInicioPeriodo(Date dataInicioPeriodo) {
		this.dataInicioPeriodo = dataInicioPeriodo;
	}

	public Date getDataFimPeriodo() {
		return dataFimPeriodo;
	}

	public void setDataFimPeriodo(Date dataFimPeriodo) {
		this.dataFimPeriodo = dataFimPeriodo;
	}

	public String getSituacaoPeriodoAquisitivo() {
		if (situacaoPeriodoAquisitivo == null) {
			situacaoPeriodoAquisitivo = "";
		}
		return situacaoPeriodoAquisitivo;
	}

	public void setSituacaoPeriodoAquisitivo(String situacaoPeriodoAquisitivo) {
		this.situacaoPeriodoAquisitivo = situacaoPeriodoAquisitivo;
	}

	public String getMotivoProgressaoSalarial() {
		if (motivoProgressaoSalarial == null) {
			motivoProgressaoSalarial = "";
		}
		return motivoProgressaoSalarial;
	}

	public void setMotivoProgressaoSalarial(String motivoProgressaoSalarial) {
		this.motivoProgressaoSalarial = motivoProgressaoSalarial;
	}

	public boolean getIsPermiteInformarAlunoJuntoOutroSacado() {
		return Uteis.isAtributoPreenchido(getLayoutRelatorioSEIDecidirVO()) 
				&& getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosReceita()
				&& getTipoPessoaEnum() != null 
				&& (getTipoPessoaEnum().equals(TipoPessoa.PARCEIRO) || getTipoPessoaEnum().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO));
	}
	
	public FormaPagamentoVO getFormaPagamentoVO() {
		if (formaPagamentoVO == null) {
			formaPagamentoVO = new FormaPagamentoVO();
		}
		return formaPagamentoVO;
	}

	public void setFormaPagamentoVO(FormaPagamentoVO formaPagamentoVO) {
		this.formaPagamentoVO = formaPagamentoVO;
	}
	
	public List<FormaPagamentoVO> getFormaPagamentoVOs() {
		if (formaPagamentoVOs == null) {
			formaPagamentoVOs = new ArrayList<FormaPagamentoVO>(0);
			// consultarUnidadeEnsinoFiltroRelatorio("");
		}
		return formaPagamentoVOs;
	}

	public void setFormaPagamentoVOs(List<FormaPagamentoVO> formaPagamentoVOs) {
		this.formaPagamentoVOs = formaPagamentoVOs;
	}

	public Date getPeriodoFechamentoMesAno() {
		if (periodoFechamentoMesAno == null) {
			periodoFechamentoMesAno = new Date();
		}
		return periodoFechamentoMesAno;
	}

	public void setPeriodoFechamentoMesAno(Date periodoFechamentoMesAno) {
		this.periodoFechamentoMesAno = periodoFechamentoMesAno;
	}

	public String[] getSituacoes() {
		return situacoes;
	}

	public void setSituacoes(String[] situacoes) {
		this.situacoes = situacoes;
	}

	public String[] getTipoAutorizacoes() {
		return tipoAutorizacoes;
	}

	public void setTipoAutorizacoes(String[] tipoAutorizacoes) {
		this.tipoAutorizacoes = tipoAutorizacoes;
	}

	public FuncionarioCargoVO getFuncionarioCargoVO() {
		if (funcionarioCargoVO == null) {
			funcionarioCargoVO = new FuncionarioCargoVO();
		}
		return funcionarioCargoVO;
	}

	public void setFuncionarioCargoVO(FuncionarioCargoVO funcionarioCargoVO) {
		this.funcionarioCargoVO = funcionarioCargoVO;
	}

	public StringBuilder getFiltro() {
		if (filtro == null) {
			filtro = new StringBuilder();
		}
		return filtro;
	}

	public void setFiltro(StringBuilder filtro) {
		this.filtro = filtro;
	}

	

	public boolean getConsiderarCategoriaDespesaArvoreInferior() {
		return considerarCategoriaDespesaArvoreInferior;
	}

	public void setConsiderarCategoriaDespesaArvoreInferior(boolean considerarCategoriaDespesaArvoreInferior) {
		this.considerarCategoriaDespesaArvoreInferior = considerarCategoriaDespesaArvoreInferior;
	}

	public boolean getConsiderarCategoriaProdutoArvoreInferior() {
		return considerarCategoriaProdutoArvoreInferior;
	}

	public void setConsiderarCategoriaProdutoArvoreInferior(boolean considerarCategoriaProdutoArvoreInferior) {
		this.considerarCategoriaProdutoArvoreInferior = considerarCategoriaProdutoArvoreInferior;
	}

	public boolean getConsiderarCentroResultadoArvoreInferior() {
		return considerarCentroResultadoArvoreInferior;
	}

	public void setConsiderarCentroResultadoArvoreInferior(boolean considerarCentroResultadoArvoreInferior) {
		this.considerarCentroResultadoArvoreInferior = considerarCentroResultadoArvoreInferior;
	}

	public Date getDataInicioPeriodoAutorizacaoRequisicao() {
		return dataInicioPeriodoAutorizacaoRequisicao;
	}

	public void setDataInicioPeriodoAutorizacaoRequisicao(Date dataInicioPeriodoAutorizacaoRequisicao) {
		this.dataInicioPeriodoAutorizacaoRequisicao = dataInicioPeriodoAutorizacaoRequisicao;
	}

	public Date getDataFimPeriodoAutorizacaoRequisicao() {
		return dataFimPeriodoAutorizacaoRequisicao;
	}

	public void setDataFimPeriodoAutorizacaoRequisicao(Date dataFimPeriodoAutorizacaoRequisicao) {
		this.dataFimPeriodoAutorizacaoRequisicao = dataFimPeriodoAutorizacaoRequisicao;
	}

	public Date getDataInicioPeriodoEntregaRequisicao() {
		return dataInicioPeriodoEntregaRequisicao;
	}

	public void setDataInicioPeriodoEntregaRequisicao(Date dataInicioPeriodoEntregaRequisicao) {
		this.dataInicioPeriodoEntregaRequisicao = dataInicioPeriodoEntregaRequisicao;
	}

	public Date getDataFimPeriodoEntregaRequisicao() {
		return dataFimPeriodoEntregaRequisicao;
	}

	public void setDataFimPeriodoEntregaRequisicao(Date dataFimPeriodoEntregaRequisicao) {
		this.dataFimPeriodoEntregaRequisicao = dataFimPeriodoEntregaRequisicao;
	}

	public List<PlanoOrcamentarioVO> getPlanosOrcamentarioVOs() {
		if (planosOrcamentarioVOs == null) {
			planosOrcamentarioVOs = new ArrayList<>();
		}
		return planosOrcamentarioVOs;
	}

	public void setPlanosOrcamentarioVOs(List<PlanoOrcamentarioVO> planosOrcamentarioVOs) {
		this.planosOrcamentarioVOs = planosOrcamentarioVOs;
	}

	public List<CategoriaDespesaVO> getCategoriasDespesaVOs() {
		if (categoriasDespesaVOs == null) {
			categoriasDespesaVOs = new ArrayList<>();
		}
		return categoriasDespesaVOs;
	}

	public void setCategoriasDespesaVOs(List<CategoriaDespesaVO> categoriasDespesaVOs) {
		this.categoriasDespesaVOs = categoriasDespesaVOs;
	}

	public List<CategoriaProdutoVO> getCategoriasProdutoVOs() {
		if (categoriasProdutoVOs == null) {
			categoriasProdutoVOs = new ArrayList<>();
		}
		return categoriasProdutoVOs;
	}

	public void setCategoriasProdutoVOs(List<CategoriaProdutoVO> categoriasProdutoVOs) {
		this.categoriasProdutoVOs = categoriasProdutoVOs;
	}

	public List<CentroResultadoVO> getCentrosResultadoVOs() {
		if (centrosResultadoVOs == null) {
			centrosResultadoVOs = new ArrayList<>();
		}
		return centrosResultadoVOs;
	}

	public void setCentrosResultadoVOs(List<CentroResultadoVO> centrosResultadoVOs) {
		this.centrosResultadoVOs = centrosResultadoVOs;
	}

	public List<DepartamentoVO> getDepartamentosVOs() {
		if (departamentosVOs == null) {
			departamentosVOs = new ArrayList<>();
		}
		return departamentosVOs;
	}

	public void setDepartamentosVOs(List<DepartamentoVO> departamentosVOs) {
		this.departamentosVOs = departamentosVOs;
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
	
	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public TurmaVO getTurmaEstudouDisciplinaVO() {
		if (turmaEstudouDisciplinaVO == null) {
			turmaEstudouDisciplinaVO = new TurmaVO();
		}
		return turmaEstudouDisciplinaVO;
	}

	public void setTurmaEstudouDisciplinaVO(TurmaVO turmaEstudouDisciplinaVO) {
		this.turmaEstudouDisciplinaVO = turmaEstudouDisciplinaVO;
	}
	
	public TipoRequerimentoVO getTipoRequerimentoVO() {
		if (tipoRequerimentoVO == null) {
			tipoRequerimentoVO = new TipoRequerimentoVO();
		}
		return tipoRequerimentoVO;
	}

	public void setTipoRequerimentoVO(TipoRequerimentoVO tipoRequerimentoVO) {
		this.tipoRequerimentoVO = tipoRequerimentoVO;
	}

	public TurmaVO getTurmaReposicao() {
		if (turmaReposicao == null) {
			turmaReposicao = new TurmaVO();
		}
		return turmaReposicao;
	}

	public void setTurmaReposicao(TurmaVO turmaReposicao) {
		this.turmaReposicao = turmaReposicao;
	}

	public PessoaVO getRequerenteVO() {
		if (requerenteVO == null) {
			requerenteVO = new PessoaVO();
		}
		return requerenteVO;
	}

	public void setRequerenteVO(PessoaVO requerenteVO) {
		this.requerenteVO = requerenteVO;
	}

	public PessoaVO getCoordenadorVO() {
		if (coordenadorVO == null) {
			coordenadorVO = new PessoaVO();
		}
		return coordenadorVO;
	}

	public void setCoordenadorVO(PessoaVO coordenadorVO) {
		this.coordenadorVO = coordenadorVO;
	}

	public Integer getSituacaoRequerimentoDepartamento() {
		if (situacaoRequerimentoDepartamento == null) {
			situacaoRequerimentoDepartamento = 0;
		}
		return situacaoRequerimentoDepartamento;
	}

	public void setSituacaoRequerimentoDepartamento(Integer situacaoRequerimentoDepartamento) {
		this.situacaoRequerimentoDepartamento = situacaoRequerimentoDepartamento;
	}

	public String getFiltrarPeriodoPor() {
		if (filtrarPeriodoPor == null) {
			filtrarPeriodoPor = "";
		}
		return filtrarPeriodoPor;
	}

	public void setFiltrarPeriodoPor(String filtrarPeriodoPor) {
		this.filtrarPeriodoPor = filtrarPeriodoPor;
	}

	public Boolean getFinalizadoDeferido() {
		if (finalizadoDeferido == null) {
			finalizadoDeferido = false;
		}
		return finalizadoDeferido;
	}

	public void setFinalizadoDeferido(Boolean finalizadoDeferido) {
		this.finalizadoDeferido = finalizadoDeferido;
	}

	public Boolean getFinalizadoIndeferido() {
		if (finalizadoIndeferido == null) {
			finalizadoIndeferido = false;
		}
		return finalizadoIndeferido;
	}

	public void setFinalizadoIndeferido(Boolean finalizadoIndeferido) {
		this.finalizadoIndeferido = finalizadoIndeferido;
	}

	public Boolean getEmExecucao() {
		if (emExecucao == null) {
			emExecucao = true;
		}
		return emExecucao;
	}

	public void setEmExecucao(Boolean emExecucao) {
		this.emExecucao = emExecucao;
	}

	public Boolean getPendente() {
		if (pendente == null) {
			pendente = false;
		}
		return pendente;
	}

	public void setPendente(Boolean pendente) {
		this.pendente = pendente;
	}

	public Boolean getAguardandoPagamento() {
		if (aguardandoPagamento == null) {
			aguardandoPagamento = false;
		}
		return aguardandoPagamento;
	}

	public void setAguardandoPagamento(Boolean aguardandoPagamento) {
		this.aguardandoPagamento = aguardandoPagamento;
	}

	public Boolean getCanceladoFinanceiramente() {
		if (canceladoFinanceiramente == null) {
			canceladoFinanceiramente = false;
		}
		return canceladoFinanceiramente;
	}

	public void setCanceladoFinanceiramente(Boolean canceladoFinanceiramente) {
		this.canceladoFinanceiramente = canceladoFinanceiramente;
	}

	public Boolean getSolicitacaoIsencao() {
		if (solicitacaoIsencao == null) {
			solicitacaoIsencao = false;
		}
		return solicitacaoIsencao;
	}

	public void setSolicitacaoIsencao(Boolean solicitacaoIsencao) {
		this.solicitacaoIsencao = solicitacaoIsencao;
	}

	public Boolean getSolicitacaoIsencaoDeferido() {
		if (solicitacaoIsencaoDeferido == null) {
			solicitacaoIsencaoDeferido = false;
		}
		return solicitacaoIsencaoDeferido;
	}

	public void setSolicitacaoIsencaoDeferido(Boolean solicitacaoIsencaoDeferido) {
		this.solicitacaoIsencaoDeferido = solicitacaoIsencaoDeferido;
	}

	public Boolean getSolicitacaoIsencaoIndeferido() {
		if (solicitacaoIsencaoIndeferido == null) {
			solicitacaoIsencaoIndeferido = false;
		}
		return solicitacaoIsencaoIndeferido;
	}

	public void setSolicitacaoIsencaoIndeferido(Boolean solicitacaoIsencaoIndeferido) {
		this.solicitacaoIsencaoIndeferido = solicitacaoIsencaoIndeferido;
	}

	public Boolean getAguardandoAutorizacaoPagamento() {
		if (aguardandoAutorizacaoPagamento == null) {
			aguardandoAutorizacaoPagamento = false;
		}
		return aguardandoAutorizacaoPagamento;
	}

	public void setAguardandoAutorizacaoPagamento(Boolean aguardandoAutorizacaoPagamento) {
		this.aguardandoAutorizacaoPagamento = aguardandoAutorizacaoPagamento;
	}

	public Boolean getIsento() {
		if (isento == null) {
			isento = false;
		}
		return isento;
	}

	public void setIsento(Boolean isento) {
		this.isento = isento;
	}

	public Boolean getPago() {
		if (pago == null) {
			pago = false;
		}
		return pago;
	}

	public void setPago(Boolean pago) {
		this.pago = pago;
	}

	public Boolean getProntoRetirada() {
		if (prontoRetirada == null) {
			prontoRetirada = false;
		}
		return prontoRetirada;
	}

	public void setProntoRetirada(Boolean prontoRetirada) {
		this.prontoRetirada = prontoRetirada;
	}

	public Boolean getAtrasado() {
		if (atrasado == null) {
			atrasado = false;
		}
		return atrasado;
	}

	public void setAtrasado(Boolean atrasado) {
		this.atrasado = atrasado;
	}

	public Boolean getCanceladoFinanceiro() {
		if (canceladoFinanceiro == null) {
			canceladoFinanceiro = false;
		}
		return canceladoFinanceiro;
	}

	public void setCanceladoFinanceiro(Boolean canceladoFinanceiro) {
		this.canceladoFinanceiro = canceladoFinanceiro;
	}
	
	public FiltroRelatorioProcessoSeletivoVO getFiltroRelatorioProcessoSeletivoVO() {
		if (filtroRelatorioProcessoSeletivoVO == null) {
			filtroRelatorioProcessoSeletivoVO = new FiltroRelatorioProcessoSeletivoVO();
		}
		return filtroRelatorioProcessoSeletivoVO;
	}

	public void setFiltroRelatorioProcessoSeletivoVO(FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivoVO) {
		this.filtroRelatorioProcessoSeletivoVO = filtroRelatorioProcessoSeletivoVO;
	}

	public Boolean getMarcarTodasSituacoesInscricao() {
		if (marcarTodasSituacoesInscricao == null) {
			marcarTodasSituacoesInscricao = false;
		}
		return marcarTodasSituacoesInscricao;
	}

	public void setMarcarTodasSituacoesInscricao(Boolean marcarTodasSituacoesInscricao) {
		this.marcarTodasSituacoesInscricao = marcarTodasSituacoesInscricao;
	}

	public ProcSeletivoVO getProcSeletivoVO() {
		if (procSeletivoVO == null) {
			procSeletivoVO = new ProcSeletivoVO();
		}
		return procSeletivoVO;
	}

	public void setProcSeletivoVO(ProcSeletivoVO procSeletivoVO) {
		this.procSeletivoVO = procSeletivoVO;
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

	public ItemProcSeletivoDataProvaVO getItemProcSeletivoDataProvaVO() {
		if (itemProcSeletivoDataProvaVO == null) {
			itemProcSeletivoDataProvaVO = new ItemProcSeletivoDataProvaVO();
		}
		return itemProcSeletivoDataProvaVO;
	}

	public void setItemProcSeletivoDataProvaVO(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO) {
		this.itemProcSeletivoDataProvaVO = itemProcSeletivoDataProvaVO;
	}

	public InscricaoVO getInscricaoVO() {
		if (inscricaoVO == null) {
			inscricaoVO = new InscricaoVO();
		}
		return inscricaoVO;
	}

	public void setInscricaoVO(InscricaoVO inscricaoVO) {
		this.inscricaoVO = inscricaoVO;
	}

	public SituacaoResultadoProcessoSeletivoEnum getSituacaoResultadoProcessoSeletivo() {
		if (situacaoResultadoProcessoSeletivo == null) {
			situacaoResultadoProcessoSeletivo = SituacaoResultadoProcessoSeletivoEnum.APROVADO;
		}
		return situacaoResultadoProcessoSeletivo;
	}

	public void setSituacaoResultadoProcessoSeletivo(SituacaoResultadoProcessoSeletivoEnum situacaoResultadoProcessoSeletivo) {
		this.situacaoResultadoProcessoSeletivo = situacaoResultadoProcessoSeletivo;
	}


	public Boolean getConsiderarTodasMatriculasAluno() {
		if(considerarTodasMatriculasAluno == null) {
			considerarTodasMatriculasAluno = false;
		}
		return considerarTodasMatriculasAluno;
	}

	public void setConsiderarTodasMatriculasAluno(Boolean considerarTodasMatriculasAluno) {
		this.considerarTodasMatriculasAluno = considerarTodasMatriculasAluno;
	}

	public String getTipoFiltroDocumentoAssinado() {
		if (tipoFiltroDocumentoAssinado == null) {
			tipoFiltroDocumentoAssinado = "NAO_CONTROLA";
		}
		return tipoFiltroDocumentoAssinado;
	}

	public void setTipoFiltroDocumentoAssinado(String tipoFiltroDocumentoAssinado) {
		this.tipoFiltroDocumentoAssinado = tipoFiltroDocumentoAssinado;
	}

	public Date getDataDocumentoInicio() {
		return dataDocumentoInicio;
	}

	public void setDataDocumentoInicio(Date dataDocumentoInicio) {
		this.dataDocumentoInicio = dataDocumentoInicio;
	}

	public Date getDataDocumentoFim() {
		return dataDocumentoFim;
	}

	public void setDataDocumentoFim(Date dataDocumentoFim) {
		this.dataDocumentoFim = dataDocumentoFim;
	}
	
	
	public BibliotecaVO getBibliotecaVO() {
		if (bibliotecaVO == null) {
			bibliotecaVO = new BibliotecaVO();
		}
		return bibliotecaVO;
	}

	public void setBibliotecaVO(BibliotecaVO bibliotecaVO) {
		this.bibliotecaVO = bibliotecaVO;
	}

	public CatalogoVO getCatalogoVO() {
		if (catalogoVO == null) {
			catalogoVO = new CatalogoVO();
		}
		return catalogoVO;
	}

	public void setCatalogoVO(CatalogoVO catalogoVO) {
		this.catalogoVO = catalogoVO;
	}

	public TipoCatalogoVO getTipoCatalogoVO() {
		if (tipoCatalogoVO == null) {
			tipoCatalogoVO = new TipoCatalogoVO();
		}
		return tipoCatalogoVO;
	}

	public void setTipoCatalogoVO(TipoCatalogoVO tipoCatalogoVO) {
		this.tipoCatalogoVO = tipoCatalogoVO;
	}

	public String getTituloCatalogo() {
		if (tituloCatalogo == null) {
			tituloCatalogo = "";
		}
		return tituloCatalogo;
	}

	public void setTituloCatalogo(String tituloCatalogo) {
		this.tituloCatalogo = tituloCatalogo;
	}

	public String getSituacaoEmprestimo() {
		if (situacaoEmprestimo == null) {
			situacaoEmprestimo = "";
		}
		return situacaoEmprestimo;
	}

	public void setSituacaoEmprestimo(String situacaoEmprestimo) {
		this.situacaoEmprestimo = situacaoEmprestimo;
	}

	public Date getDataInicioEmprestimo() {
		if (dataInicioEmprestimo == null) {
			dataInicioEmprestimo = UteisData.getPrimeiroDataMes(new Date());
		}
		return dataInicioEmprestimo;
	}

	public void setDataInicioEmprestimo(Date dataInicioEmprestimo) {
		this.dataInicioEmprestimo = dataInicioEmprestimo;
	}

	public Date getDataFimEmprestimo() {
		if (dataFimEmprestimo == null) {
			dataFimEmprestimo = UteisData.getUltimaDataMes(new Date());
		}
		return dataFimEmprestimo;
	}

	public void setDataFimEmprestimo(Date dataFimEmprestimo) {
		this.dataFimEmprestimo = dataFimEmprestimo;
	}

	public String getClassificacaoBibliografica() {
		if (classificacaoBibliografica == null) {
			classificacaoBibliografica = "";
		}
		return classificacaoBibliografica;
	}

	public void setClassificacaoBibliografica(String classificacaoBibliografica) {
		this.classificacaoBibliografica = classificacaoBibliografica;
	}

	public Date getDataInicioAquisicaoExemplar() {
		return dataInicioAquisicaoExemplar;
	}

	public void setDataInicioAquisicaoExemplar(Date dataInicioAquisicaoExemplar) {
		this.dataInicioAquisicaoExemplar = dataInicioAquisicaoExemplar;
	}

	public Date getDataFimAquisicaoExemplar() {
		return dataFimAquisicaoExemplar;
	}

	public void setDataFimAquisicaoExemplar(Date dataFimAquisicaoExemplar) {
		this.dataFimAquisicaoExemplar = dataFimAquisicaoExemplar;
	}

	public AreaConhecimentoVO getAreaConhecimentoVO() {
		if (areaConhecimentoVO == null) {
			areaConhecimentoVO = new AreaConhecimentoVO();
		}
		return areaConhecimentoVO;
	}

	public void setAreaConhecimentoVO(AreaConhecimentoVO areaConhecimentoVO) {
		this.areaConhecimentoVO = areaConhecimentoVO;
	}

	public String getTipoEntrada() {
		if (tipoEntrada == null) {
			tipoEntrada = "";
		}
		return tipoEntrada;
	}

	public void setTipoEntrada(String tipoEntrada) {
		this.tipoEntrada = tipoEntrada;
	}

	public SecaoVO getSecao() {
		if (secao == null) {
			secao = new SecaoVO();
		}
		return secao;
	}

	public void setSecao(SecaoVO secao) {
		this.secao = secao;
	}

	public FuncionarioVO getProfessorVO() {
		if (professorVO == null) {
			professorVO = new FuncionarioVO();
		}
		return professorVO;
	}

	public void setProfessorVO(FuncionarioVO professorVO) {
		this.professorVO = professorVO;
	}

	public String getTipoCatalogoPeriodico() {
		if (tipoCatalogoPeriodico == null) {
			tipoCatalogoPeriodico = "TODOS";
		}
		return tipoCatalogoPeriodico;
	}

	public void setTipoCatalogoPeriodico(String tipoCatalogoPeriodico) {
		this.tipoCatalogoPeriodico = tipoCatalogoPeriodico;
	}
	
	public Date getDataProvaInicio() {
		return dataProvaInicio;
	}

	public void setDataProvaInicio(Date dataProvaInicio) {
		this.dataProvaInicio = dataProvaInicio;
	}

	public Date getDataProvaFim() {
		return dataProvaFim;
	}

	public void setDataProvaFim(Date dataProvaFim) {
		this.dataProvaFim = dataProvaFim;
	}

	
	public Integer getPeriodoLetivo() {
		if(periodoLetivo == null) {
			periodoLetivo = 0;
		}
		return periodoLetivo;
	}
	
	public void setPeriodoLetivo(Integer periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}

	public String getCodigosGradeCurricularEstagio() {
		if (codigosGradeCurricularEstagio == null) {
			codigosGradeCurricularEstagio = "";
		}
		return codigosGradeCurricularEstagio;
	}

	public void setCodigosGradeCurricularEstagio(String codigosGradeCurricularEstagio) {
		this.codigosGradeCurricularEstagio = codigosGradeCurricularEstagio;
	}
	
	
}
