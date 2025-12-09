package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoFinanceiroCursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.AgenteNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.enumerador.TipoAgenteNegativacaoCobrancaContaReceberEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.financeiro.RecebimentoRel;

@Controller("RecebimentoRelControle")
@Scope("viewScope")
@Lazy
public class RecebimentoRelControle extends SuperControleRelatorio {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5196434823693261459L;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private AgenteNegativacaoCobrancaContaReceberVO agente;
	private String campoConsultaAgente;
	private String valorConsultaAgente;
	List<SelectItem> tipoConsultaComboAgente;
	private List<AgenteNegativacaoCobrancaContaReceberVO> listaConsultaAgente;	
	private TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente;	
    private Date dataInicio;
    private Date dataFim;
    private Integer codigoUnidadeEnsino;
    private String tipoPessoa;
    private Integer codigoContaCorrente;
    private String tipoOrdenacao;
    private Integer codigoPessoa;
    private Integer codigoParceiro;
    private Integer fornecedorCodigo;
    private String fornecedorNome;
    private String campoDescritivo;
    private String valorDescritivo;
    private Boolean mostrarCamposDescritivos;
    private List listaSelectItemMatriculaAluno;
    private List listaSelectItemFuncionario;
    private List listaConsultaFuncionario;
    private String valorConsultaFuncionario;
    private String campoConsultaFuncionario;
    private List listaConsultaAluno;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private List listaConsultaCandidato;
    private String valorConsultaCandidato;
    private String campoConsultaCandidato;
    private List listaConsultaParceiro;
    private String valorConsultaParceiro;
    private String campoConsultaParceiro;
    private List listaConsultaCentroReceita;
    private String valorConsultaCentroReceita;
    private String campoConsultaCentroReceita;
    private List listaConsultaPlanoFinanceiroCurso;
    private String valorConsultaPlanoFinanceiroCurso;
    private String campoConsultaPlanoFinanceiroCurso;
    private PlanoFinanceiroCursoVO planoFinanceiroCursoVO;
    private CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCurso;
    private List listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso;
    private Integer centroReceita;
    private String centroReceitaDescricao;
    private List listaSelectItemContaCorrente;
    private Boolean aluno;
    private Boolean candidato;
    private Boolean funcionario;
    private Boolean responsavelFinanceiro;
    private Boolean parceiro;
    private Boolean fornecedor;
    private List listaSelectItemUnidadeEnsino;
    private List<SelectItem> listaSelectItemLayout;
    protected List<PessoaVO> listaConsultaResponsavelFinanceiro;
    protected String valorConsultaResponsavelFinanceiro;
    protected String campoConsultaResponsavelFinanceiro;
    protected List listaSelectItemParcelas;
    protected String parcela;
    protected List<FornecedorVO> listaConsultaFornecedor;
    protected String valorConsultaFornecedor;
    protected String campoConsultaFornecedor;
    protected String layout;
    private String campoConsultaTurma;
    private String valorConsultaTurma;
    private List listaConsultaTurma;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List listaConsultaCurso;
    private TurmaVO turmaVO;
    private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;	
    private List<SelectItem> listaSelectItemTurno;
    private Boolean utilizarDataBaseFaturamento;
    private Date dataBaseFaturamento;
    private Date dataInicioCompetencia;
    private Date dataFimCompetencia;
    private Boolean apresentarQuadroResumoCalouroVeterano;
    private Boolean considerarUnidadeEnsinoFinanceira;
    private List<SelectItem> listaSelectItemFiltrarPeriodoPor;
    private String filtrarPeriodoPor;
    private MatriculaVO matriculaVO;

    public RecebimentoRelControle() throws Exception {
        setAluno(Boolean.FALSE);
        setFuncionario(Boolean.FALSE);
        setCandidato(Boolean.FALSE);
        setListaConsultaAluno(null);
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_entre_prmrelatorio");
    }

    @PostConstruct
    public void consultarUnidadeEnsino() {
        try {
            consultarUnidadeEnsinoFiltroRelatorio("RecebimentoRel");
            verificarTodasUnidadesSelecionadas();
            inicializarMesAtual();
            carregarDadosLayoutRelatorio();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    
    public void inicializarMesAtual() {
        this.setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
        this.setDataInicio(Uteis.getDataPrimeiroDiaMes(new Date()));
    }
    
    public void atualizarDataFinal() {
        this.setDataFim(Uteis.getDataUltimoDiaMes(this.getDataInicio()));
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
    
    public void carregarDadosLayoutRelatorio() {
		try {
			Map<String, String> camposPadroes = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[] { "layout", "utilizarDataBaseFaturamento","dataBaseFaturamento", "tipoOrdenacao", "apresentarQuadroResumoCalouroVeterano","considerarUnidadeEnsinoFinanceira", "filtrarPeriodoPor" }, "RecebimentoRel");
			camposPadroes.entrySet().stream().forEach(p -> {
				try {
					if (p.getKey().equals("layout") && Uteis.isAtributoPreenchido(p.getValue())) {
						setLayout(p.getValue());
					} else if (p.getKey().equals("utilizarDataBaseFaturamento") && Uteis.isAtributoPreenchido(p.getValue())) {
						setUtilizarDataBaseFaturamento(Boolean.parseBoolean(p.getValue()));
					} else if (p.getKey().equals("dataBaseFaturamento") && Uteis.isAtributoPreenchido(p.getValue())) {
						setDataBaseFaturamento(UteisData.getData(p.getValue()));
					} else if (p.getKey().equals("tipoOrdenacao") && Uteis.isAtributoPreenchido(p.getValue())) {
						setTipoOrdenacao(p.getValue());
					} else if (p.getKey().equals("apresentarQuadroResumoCalouroVeterano") && Uteis.isAtributoPreenchido(p.getValue())) {
						setApresentarQuadroResumoCalouroVeterano(Boolean.parseBoolean(p.getValue()));
					} else if (p.getKey().equals("considerarUnidadeEnsinoFinanceira") && Uteis.isAtributoPreenchido(p.getValue())) {
						setConsiderarUnidadeEnsinoFinanceira(Boolean.parseBoolean(p.getValue()));
					} else if (p.getKey().equals("filtrarPeriodoPor") && Uteis.isAtributoPreenchido(p.getValue())) {
						setFiltrarPeriodoPor(p.getValue());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			if(!getIsPermitirApenasRelatorioContaReceberRecebimentoBiblioteca()){
				getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroTipoOrigemContaReceber(getFiltroRelatorioFinanceiroVO(), "RecebimentoRel", getUsuarioLogado());	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public void selecionarFiltro() throws Exception {
        setarFalseNosFiltros();
        setMatriculaVO(null);
        if (getTipoPessoa().equals("aluno")) {
            setAluno(true);
            setMostrarCamposDescritivos(true);
        } else if (getTipoPessoa().equals("funcionario")) {
            setFuncionario(true);
            setMostrarCamposDescritivos(true);
        } else if (getTipoPessoa().equals("candidato")) {
            setCandidato(true);
            setMostrarCamposDescritivos(true);
        } else if (getTipoPessoa().equals("parceiro")) {
            setParceiro(true);
            setMostrarCamposDescritivos(true);
        } else if (getTipoPessoa().equals("responsavelFinanceiro")) {
            setResponsavelFinanceiro(true);
            setMostrarCamposDescritivos(true);
        } else if (getTipoPessoa().equals("fornecedor")) {
            setFornecedor(true);
            setMostrarCamposDescritivos(true);
        }
    }

    private void setarFalseNosFiltros() {
        setAluno(false);
        setFuncionario(false);
        setCandidato(false);
        setParceiro(false);
        setMostrarCamposDescritivos(false);
        setResponsavelFinanceiro(false);
    }

    public String getDescricao() {
        if (getTipoPessoa().equals("aluno")) {
            return "Aluno";
        } else if (getTipoPessoa().equals("funcionario")) {
            return "Funcionário";
        } else if (getTipoPessoa().equals("candidato")) {
            return "Candidato";
        } else if (getTipoPessoa().equals("parceiro")) {
            return "Parceiro";
        } else if (getTipoPessoa().equals("responsavelFinanceiro")) {
            return "Responsável Financeiro";
        } else if (getTipoPessoa().equals("fornecedor")) {
            return "Fornecedor";
        }
        return "";
    }

    public void imprimirRelatorioExcel() {
        try {
            imprimirRelatorio(TipoRelatorioEnum.EXCEL);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void imprimirRelatorio(TipoRelatorioEnum tipoRelatorioEnum) {
        List listaObjetos = null;
        String titulo = " Relatório de Recebimento";
        try {
            listaObjetos = new ArrayList(0);
            if (getDataInicio() == null && getDataFim() == null && getDataInicioCompetencia() == null && getDataFimCompetencia() == null) {
                throw new Exception("Informe o período desejado!");
            }
            
            if (getDataInicioCompetencia() != null) {
            	setDataInicioCompetencia(Uteis.getDataPrimeiroDiaMes(getDataInicioCompetencia()));
            }
            if (getDataFimCompetencia() != null) {
            	setDataFimCompetencia(Uteis.getDataUltimoDiaMes(getDataFimCompetencia()));
            }
            if (getLayout().equals("sinteticoFormaRecebimento")) {
                listaObjetos = getFacadeFactory().getRecebimentoRelFacade().criarRelatorioSinteticoPorFormaRecebimento(getUnidadeEnsinoVOs(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), getUnidadeEnsinoCursoVO().getTurno().getCodigo(), getFiltroRelatorioFinanceiroVO(), getTipoPessoa(), getCodigoPessoa(), getCodigoParceiro(), getDataInicio(), getDataFim(), getCodigoContaCorrente(), getTipoOrdenacao(), getParcela(), getUsuarioLogado(), getCentroReceita(), getPlanoFinanceiroCursoVO().getCodigo(),  getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), getFornecedorCodigo(), getLayout(), getDataInicioCompetencia(), getDataFimCompetencia(), getApresentarQuadroResumoCalouroVeterano(), getConsiderarUnidadeEnsinoFinanceira(), getAgente(), getTipoAgente(), getFiltrarPeriodoPor(), getMatriculaVO());
                titulo = " Relatório de Recebimento Sintético por Forma de Recebimento";
            } else {
                if (getLayout().equals("faturamentoSinteticoCursoCentroReceita")) {
                    listaObjetos = getFacadeFactory().getRecebimentoRelFacade().criarRelatorioFaturamentoSinteticoPorCursoTipoOrigem(getUnidadeEnsinoVOs(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), getUnidadeEnsinoCursoVO().getTurno().getCodigo(), getFiltroRelatorioFinanceiroVO(), getTipoPessoa(), getCodigoPessoa(), getCodigoParceiro(), getDataInicio(), getDataFim(), getCodigoContaCorrente(), getTipoOrdenacao(), getParcela(), getUsuarioLogado(), getCentroReceita(), getPlanoFinanceiroCursoVO().getCodigo(),  getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), getFornecedorCodigo(), getLayout(), getUtilizarDataBaseFaturamento(), getDataBaseFaturamento(), getDataInicioCompetencia(), getDataFimCompetencia(), getApresentarQuadroResumoCalouroVeterano(), getConsiderarUnidadeEnsinoFinanceira(), getAgente(), getTipoAgente(), getFiltrarPeriodoPor(), getMatriculaVO());
                    titulo = " Relatório de Faturamento Sintético Por Curso / Tipo Origem";
                } else {
                    if (getLayout().equals("faturamentoSinteticoCurso")) {
                        listaObjetos = getFacadeFactory().getRecebimentoRelFacade().criarRelatorioFaturamentoSinteticoPorCursoTipoOrigem(getUnidadeEnsinoVOs(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), getUnidadeEnsinoCursoVO().getTurno().getCodigo(), getFiltroRelatorioFinanceiroVO(), getTipoPessoa(), getCodigoPessoa(), getCodigoParceiro(), getDataInicio(), getDataFim(), getCodigoContaCorrente(), getTipoOrdenacao(), getParcela(), getUsuarioLogado(), getCentroReceita(), getPlanoFinanceiroCursoVO().getCodigo(),  getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), getFornecedorCodigo(), getLayout(), getUtilizarDataBaseFaturamento(), getDataBaseFaturamento(), getDataInicioCompetencia(), getDataFimCompetencia(), getApresentarQuadroResumoCalouroVeterano(), getConsiderarUnidadeEnsinoFinanceira(), getAgente(), getTipoAgente(), getFiltrarPeriodoPor(), getMatriculaVO());
                        titulo = " Relatório de Faturamento Sintético Por Curso";
                    } else {
                        listaObjetos = getFacadeFactory().getRecebimentoRelFacade().criarObjeto(getUnidadeEnsinoVOs(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), getUnidadeEnsinoCursoVO().getTurno().getCodigo(), getFiltroRelatorioFinanceiroVO(), getTipoPessoa(), getCodigoPessoa(), getCodigoParceiro(), getDataInicio(), getDataFim(), getCodigoContaCorrente(), getTipoOrdenacao(), getParcela(), getUsuarioLogado(), getCentroReceita(), getPlanoFinanceiroCursoVO().getCodigo(),  getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), getFornecedorCodigo(), getLayout(), getDataInicioCompetencia(), getDataFimCompetencia(), getApresentarQuadroResumoCalouroVeterano(), getConsiderarUnidadeEnsinoFinanceira(), getAgente(), getTipoAgente(), getFiltrarPeriodoPor(), getMatriculaVO());
                    }
                }
            }
            if (!listaObjetos.isEmpty()) {
                String design;
                if (getLayout().equals("padrao")) {
                    if (tipoRelatorioEnum.equals(TipoRelatorioEnum.EXCEL)) {
                        design = RecebimentoRel.getDesignIReportRelatorioExcel();
                    } else {
                        design = RecebimentoRel.getDesignIReportRelatorio();
                    }
                } else if (getLayout().equals("diadia")) {
                    if (tipoRelatorioEnum.equals(TipoRelatorioEnum.EXCEL)) {
                    	design = RecebimentoRel.getDesignIReportRelatorioDiaDia().replace("Rel.", "ExcelRel.");
                    }else{
                    	design = RecebimentoRel.getDesignIReportRelatorioDiaDia();
                    }
                } else if (getLayout().equals("faturamentoSinteticoCursoCentroReceita")) {
                	if (tipoRelatorioEnum.equals(TipoRelatorioEnum.EXCEL)) {
                    	design = RecebimentoRel.getDesignIReportRelatorioFaturamentoPorCursoTipoOrigem().replace("Rel.", "ExcelRel.");
                    }else{
                    	design = RecebimentoRel.getDesignIReportRelatorioFaturamentoPorCursoTipoOrigem();
                    }
                } else if (getLayout().equals("faturamentoSinteticoCurso")) {                    
                    if (tipoRelatorioEnum.equals(TipoRelatorioEnum.EXCEL)) {
                    	design = RecebimentoRel.getDesignIReportRelatorioFaturamentoPorCurso().replace("Rel.", "ExcelRel.");
                    }else{
                    	design = RecebimentoRel.getDesignIReportRelatorioFaturamentoPorCurso();
                    }
                } else if (getLayout().equals("sinteticoFormaRecebimento")) {                                    	
                	design = RecebimentoRel.getDesignIReportRelatorioSinteticoPorFormaPagamento();                	
                } else {
                    if (getLayout().equals("sinteticoCentroReceita")) {                    	
                        design = RecebimentoRel.getCaminhoBaseDesignIReportRelatorio() + "RecebimentoSinteticoPorCentroReceitaRel.jrxml";
                    } else {
                    	getSuperParametroRelVO().adicionarParametro("centroReceitas", getFacadeFactory().getRecebimentoRelFacade().realizarGeracaoListaRecebimentoCentroReceita(listaObjetos));
                        if (tipoRelatorioEnum.equals(TipoRelatorioEnum.EXCEL)) {
                            design = RecebimentoRel.getDesignIReportRelatorioPorCentroReceitaExcel();
                        } else {
                            design = RecebimentoRel.getDesignIReportRelatorioPorCentroReceita();
                        }
                    }
                }
                getSuperParametroRelVO().setNomeDesignIreport(design);
                getSuperParametroRelVO().setTipoRelatorioEnum(tipoRelatorioEnum);
                getSuperParametroRelVO().setSubReport_Dir(RecebimentoRel.getCaminhoBaseDesignIReportRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio(titulo);
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(RecebimentoRel.getCaminhoBaseDesignIReportRelatorio());
                getSuperParametroRelVO().setNomeEmpresa("");
                getSuperParametroRelVO().setVersaoSoftware("");
                getSuperParametroRelVO().setFiltros("");
                if (getUtilizarDataBaseFaturamento()) {
                    getSuperParametroRelVO().setDataPrevisao(Uteis.getData(getDataBaseFaturamento()));
                } else {
                    // caso nao controle a data base deve ser igual a datafinal do relatório
                    getSuperParametroRelVO().setDataPrevisao(Uteis.getData(getDataFim()));
                }
                
                getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
                getSuperParametroRelVO().setPeriodo("Período");
                getSuperParametroRelVO().adicionarParametro("filtrarPeriodoPor", getFiltrarPeriodoPor().equals("DATA_RECEBIMENTO") ? "Data Recebimento" : getFiltrarPeriodoPor().equals("DATA_VENCIMENTO") ? "Data Vencimento" : "Data Compensação" );
                if (this.getFiltroRelatorioFinanceiroVO().getFiltrarPorDataCompetencia()) {
                    getSuperParametroRelVO().setPeriodo("Competência");
                }
                if (getDataInicio() != null) {
                    getSuperParametroRelVO().setDataInicio(Uteis.getData(getDataInicio()));
                } else {
                    getSuperParametroRelVO().setDataInicio("");
                }
                if (getDataFim() != null) {
                    getSuperParametroRelVO().setDataFim(Uteis.getData(getDataFim()));
                    
                } else {
                    getSuperParametroRelVO().setDataFim("");
                }
                if (getDataInicioCompetencia() != null) {
                	getSuperParametroRelVO().setDataInicioCompetencia(Uteis.getMesData(getDataInicioCompetencia()) + "/" + Uteis.getAnoData(getDataInicioCompetencia()));
                } else {
                	getSuperParametroRelVO().setDataInicioCompetencia("");
                }
                if (getDataFimCompetencia() != null) {
                	getSuperParametroRelVO().setDataFimCompetencia(Uteis.getMesData(getDataFimCompetencia()) + "/" + Uteis.getAnoData(getDataFimCompetencia()));
                } else {
                	getSuperParametroRelVO().setDataFimCompetencia("");
                }
                if (getTipoPessoa().equals("")) {
                    getSuperParametroRelVO().adicionarParametro("tipoPessoa", "TODAS");
                } else {
                    if (getTipoPessoa().equals("aluno")) {
                        getSuperParametroRelVO().adicionarParametro("tipoPessoa", "Aluno");

                    } else if (getTipoPessoa().equals("responsavelFinanceiro")) {
                        getSuperParametroRelVO().adicionarParametro("tipoPessoa", "Responsável Financeiro");
                    } else if (getTipoPessoa().equals("funcionario")) {
                        getSuperParametroRelVO().adicionarParametro("tipoPessoa", "Funcionário");
                    } else if (getTipoPessoa().equals("candidato")) {
                        getSuperParametroRelVO().adicionarParametro("tipoPessoa", "Candidato");
                    } else if (getTipoPessoa().equals("parceiro")) {
                        getSuperParametroRelVO().adicionarParametro("tipoPessoa", "Parceiro");
                    } else if (getTipoPessoa().equals("fornecedor")) {
                        getSuperParametroRelVO().adicionarParametro("tipoPessoa", "Fornecedor");
                    }
                }
                if (!getValorDescritivo().trim().isEmpty() && (getCodigoParceiro() > 0 || getCodigoPessoa() > 0 || getFornecedorCodigo() > 0)) {
                    getSuperParametroRelVO().adicionarParametro("sacado", getValorDescritivo());
                }
                if (getAgente().getCodigo().intValue() > 0) {
                	getSuperParametroRelVO().adicionarParametro("agente", getAgente().getNome());
                }
                getSuperParametroRelVO().adicionarParametro("tipoOrigem", getFiltroRelatorioFinanceiroVO().getItensFiltroTipoOrigem());
                getSuperParametroRelVO().adicionarParametro("apresentarQuadroResumoCalouroVeterano", getApresentarQuadroResumoCalouroVeterano());
                if (getCodigoContaCorrente() > 0) {
                    getSuperParametroRelVO().setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getCodigoContaCorrente(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()).getBancoAgenciaContaCorrente());
                } else {
                    getSuperParametroRelVO().setContaCorrente("TODAS");
                }
                if (getCentroReceita() > 0) {
                    getSuperParametroRelVO().adicionarParametro("centroReceita", getCentroReceitaDescricao());
                } else {
                    getSuperParametroRelVO().adicionarParametro("centroReceita", "TODOS");
                }
                if (getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo() > 0) {
                    setCondicaoPagamentoPlanoFinanceiroCurso(getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
                    getSuperParametroRelVO().adicionarParametro("condicaoPagamentoPlanoFinanceiroCurso", getCondicaoPagamentoPlanoFinanceiroCurso().getDescricao());
                } else {
                    getSuperParametroRelVO().adicionarParametro("condicaoPagamentoPlanoFinanceiroCurso", "TODOS");
                }
                if (getPlanoFinanceiroCursoVO().getCodigo() > 0) {
                    getSuperParametroRelVO().adicionarParametro("planoFinanceiroCurso", getPlanoFinanceiroCursoVO().getDescricao());
                } else {
                    getSuperParametroRelVO().adicionarParametro("planoFinanceiroCurso", "TODOS");
                }
                if (getParcela().trim().isEmpty()) {
                    getSuperParametroRelVO().adicionarParametro("parcela", "TODAS");
                } else {
                    getSuperParametroRelVO().adicionarParametro("parcela", getParcela());
                }
                if (getUnidadeEnsinoCursoVO().getCurso().getCodigo() > 0) {
                    getSuperParametroRelVO().adicionarParametro("curso", getUnidadeEnsinoCursoVO().getCurso().getNome());
                } else {
                    getSuperParametroRelVO().adicionarParametro("curso", "TODOS");
                }
                if (getTurmaVO().getCodigo() > 0) {
                    getSuperParametroRelVO().adicionarParametro("turma", getTurmaVO().getIdentificadorTurma());
                } else {
                    getSuperParametroRelVO().adicionarParametro("turma", "TODAS");
                }
                realizarImpressaoRelatorio();
                persistirLayoutPadrao();
                inicializarListasSelectItemTodosComboBox();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            listaObjetos.clear();
            listaObjetos = null;

        }
    }

    public void imprimirPDF() {
        imprimirRelatorio(TipoRelatorioEnum.PDF);
    }
    
    private void persistirLayoutPadrao() throws Exception{
    	if(!getIsPermitirApenasRelatorioContaReceberRecebimentoBiblioteca()){
    		getFacadeFactory().getLayoutPadraoFacade().persistirFiltroTipoOrigemContaReceber(getFiltroRelatorioFinanceiroVO(), "RecebimentoRel", getUsuarioLogado());	
    	}
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getLayout(), "RecebimentoRel", "layout", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getUtilizarDataBaseFaturamento().toString(), "RecebimentoRel", "utilizarDataBaseFaturamento", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(UteisData.getData(getDataBaseFaturamento()), "RecebimentoRel", "dataBaseFaturamento", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoOrdenacao(), "RecebimentoRel", "tipoOrdenacao", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getApresentarQuadroResumoCalouroVeterano().toString(), "RecebimentoRel", "apresentarQuadroResumoCalouroVeterano", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getConsiderarUnidadeEnsinoFinanceira().toString(), "RecebimentoRel", "considerarUnidadeEnsinoFinanceira", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getFiltrarPeriodoPor().toString(), "RecebimentoRel", "filtrarPeriodoPor", getUsuarioLogado());
	}

    public void consultarFuncionario() {
        List objs = new ArrayList(0);
        try {
            if (getValorConsultaFuncionario().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");
            if (getCampoConsultaFuncionario().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), TipoPessoa.FUNCIONARIO.getValor(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("CPF")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), TipoPessoa.FUNCIONARIO.getValor(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), TipoPessoa.FUNCIONARIO.getValor(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsultaFuncionario(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            getListaConsultaFuncionario().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            objs = null;
        }
    }

    public void consultarAluno() {
        List objs = new ArrayList(0);
        try {
            if (getValorConsultaAluno().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            getListaConsultaAluno().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            objs = null;
        }
    }

    public void consultarCandidato() {
        List objs = new ArrayList(0);
        try {
            getFacadeFactory().getPessoaFacade().setIdEntidade("Candidato");
            if (getValorConsultaCandidato().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaCandidato().equals("codigo")) {
                int valorInt = Integer.parseInt(getValorConsultaCandidato());
                objs = getFacadeFactory().getPessoaFacade().consultarPorCodigo(new Integer(valorInt), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaCandidato().equals("nome")) {
                objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorNome(getValorConsultaCandidato(), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaCandidato().equals("CPF")) {
                objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorCPF(getValorConsultaCandidato(), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaCandidato().equals("RG")) {
                objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorRG(getValorConsultaCandidato(), "CA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsultaCandidato(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            getListaConsultaCandidato().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            objs = null;
        }
    }

    public void selecionarFornecedor() {
        FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
        this.setFornecedorCodigo(obj.getCodigo());
        this.setFornecedorNome(obj.getNome());
        this.setCampoDescritivo(obj.getCPF() + obj.getCNPJ());
        this.setValorDescritivo(obj.getNome());
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
            List objs = new ArrayList(0);
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
            setListaConsultaFornecedor(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void buscarParcelasParaImpressao() throws Exception {
//        try {
//            List<String> parcelas = new ArrayList<String>(0);
//            if (getCodigoPessoa() != 0) {
//                parcelas = getFacadeFactory().getBoletoBancarioRelFacade().executarConsultaParcelasFiltro(getCodigoPessoa(), getDataInicio(), getDataFim(), getCodigoUnidadeEnsino());
//            } else if (getCodigoParceiro() != 0) {
//                parcelas = getFacadeFactory().getBoletoBancarioRelFacade().executarConsultaParcelasFiltro(getCodigoParceiro(), getDataInicio(), getDataFim(), getCodigoUnidadeEnsino());
//            } else {
//                throw new Exception("A PESSOA/PARCEIRO deve ser informado para selecionar a parcela.");
//            }
//            List<SelectItem> selectItemParcelas = new ArrayList<SelectItem>();
//            selectItemParcelas.add(new SelectItem("", ""));
//            if (parcelas != null && !parcelas.isEmpty()) {
//                for (String parcela : parcelas) {
//                    selectItemParcelas.add(new SelectItem(parcela, parcela));
//                }
//                setListaSelectItemParcelas(selectItemParcelas);
//                setMensagemDetalhada("");
//            } else {
//                setListaSelectItemParcelas(selectItemParcelas);
//                throw new ConsistirException("Não há nenhuma parcela para esse(a) determinado(a) pessoa/parceiro nesse período");
//            }
//        } catch (Exception e) {
//            setMensagemDetalhada("msg_erro", e.getMessage());
//        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP
     * ParceiroCons.jsp. Define o tipo de consulta a ser executada, por meio de
     * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
     * resultado, disponibiliza um List com os objetos selecionados na sessao da
     * pagina.
     */
    public void consultarParceiro() {
        List objs = new ArrayList(0);
        try {
            super.consultar();
            if (getCampoConsultaParceiro().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getParceiroFacade().consultarPorCodigo(valorInt, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
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
            getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            objs = null;
        }
    }

    public void consultarCentroReceita() {
        List objs = new ArrayList(0);
        try {
            if (getCampoConsultaCentroReceita().equals("descricao")) {
                objs = getFacadeFactory().getCentroReceitaFacade().consultarPorDescricao(getValorConsultaCentroReceita(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaCentroReceita().equals("identificadorCentroReceita")) {
                objs = getFacadeFactory().getCentroReceitaFacade().consultarPorIdentificadorCentroReceita(getValorConsultaCentroReceita(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaCentroReceita().equals("nomeDepartamento")) {
                objs = getFacadeFactory().getCentroReceitaFacade().consultarPorNomeDepartamento(getValorConsultaCentroReceita(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaCentroReceita(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            objs = null;
        }
    }

    public void consultarPlanoFinanceiroCurso() {
        List objs = new ArrayList(0);
        try {
            if (getCampoConsultaPlanoFinanceiroCurso().equals("descricao")) {
                objs = getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPorDescricao(getValorConsultaPlanoFinanceiroCurso(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaPlanoFinanceiroCurso().equals("codigo")) {
                Integer codigoBuscar = 0;
                try {
                    codigoBuscar = Integer.valueOf(getValorConsultaPlanoFinanceiroCurso());
                } catch (Exception e) {
                    codigoBuscar = 0;
                }
                objs = getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPorCodigo(codigoBuscar, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaPlanoFinanceiroCurso(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            objs = null;
        }
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

    public void selecionarResponsavelFinanceiro() {
        try {
            PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("responsavelFinanceiroItens");
            getListaConsultaResponsavelFinanceiro().clear();
            setCampoDescritivo(obj.getCPF());
            setValorDescritivo(obj.getNome());
            setCodigoPessoa(obj.getCodigo());
            buscarParcelasParaImpressao();
        } catch (Exception e) {
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

    public List getTipoConsultaComboPlanoFinanceiroCurso() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("descricao", "Descrição"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public List getTipoConsultaComboCentroReceita() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("descricao", "Descrição"));
        itens.add(new SelectItem("identificadorCentroReceita", "Identificador Centro Receita"));
        itens.add(new SelectItem("nomeDepartamento", "Departamento"));
        return itens;
    }

    public List getTipoConsultaComboAluno() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nomePessoa", "Nome Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("nomeCurso", "Nome Curso"));

        return itens;
    }

    public List getTipoConsultaComboFuncionario() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("CPF", "CPF"));
        return itens;
    }

    public List getTipoConsultaComboCandidato() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("RG", "RG"));
        return itens;
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaComboParceiro() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("razaoSocial", "Razão Social"));
        itens.add(new SelectItem("RG", "RG"));
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("tipoParceiro", "Tipo Parceiro"));
        return itens;
    }

    public void selecionarFuncionario() throws Exception {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
        try {
            setCampoDescritivo(obj.getMatricula());
            setValorDescritivo(obj.getPessoa().getNome());
            setCodigoPessoa(obj.getPessoa().getCodigo());
            setCampoConsultaFuncionario("");
            setValorConsultaFuncionario("");
            getListaConsultaFuncionario().clear();
            buscarParcelasParaImpressao();
        } finally {
            obj = null;
        }
    }

    public void selecionarAluno() throws Exception {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoItens");
        try {
        	if(getAluno()) {
        		setCampoDescritivo(obj.getMatricula());            
        		setValorDescritivo(obj.getAluno().getNome());
        		setCodigoPessoa(obj.getAluno().getCodigo());
        		buscarParcelasParaImpressao();
        	}else {
        		setMatriculaVO(obj);
        	}
            setCampoConsultaAluno("");
            setValorConsultaAluno("");
            getListaConsultaAluno().clear();
            
        } finally {
            obj = null;
        }

    }

    public void selecionarCandidato() throws Exception {
        PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("candidatoItens");
        try {
            setCampoDescritivo(obj.getCPF());
            setValorDescritivo(obj.getNome());
            setCodigoPessoa(obj.getCodigo());
            setCampoConsultaCandidato("");
            setValorConsultaCandidato("");
            getListaConsultaCandidato().clear();
            buscarParcelasParaImpressao();
        } finally {
            obj = null;
        }
    }

    public void selecionarParceiro() throws Exception {
        ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
        try {
            if (obj.getCPF().equals("")) {
                setCampoDescritivo(obj.getCNPJ());
            } else {
                setCampoDescritivo(obj.getCPF());
            }
            setValorDescritivo(obj.getNome());
            setCodigoParceiro(obj.getCodigo());
            setValorConsultaCandidato("");
            setCampoConsultaCandidato("");
            getListaConsultaCandidato().clear();
            
            buscarParcelasParaImpressao();
        } finally {
            obj = null;
        }
    }

    public void limparDados() {
        setCampoDescritivo(null);
        setValorDescritivo(null);
        setCodigoParceiro(0);
        setFornecedorCodigo(0);
        setFornecedorNome("");
        setCodigoPessoa(0);
        getListaSelectItemParcelas().clear();
    }

    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemContaCorrente();
        montarListaSelectItemUnidadeEnsino();
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
            if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
                setListaSelectItemUnidadeEnsino(new ArrayList<SelectItem>());
                getListaSelectItemUnidadeEnsino().add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getNome()));
                setCodigoUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo());
                return;
            }
            setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public List<SelectItem> getListaSelectItemTipoPessoa() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("", ""));
        if (getIsPermitirApenasRelatorioContaReceberRecebimentoBiblioteca()) {
            itens.add(new SelectItem("aluno", "Aluno"));
            itens.add(new SelectItem("funcionario", "Funcionário"));
        } else {
            itens.add(new SelectItem("aluno", "Aluno"));
            itens.add(new SelectItem("responsavelFinanceiro", "Responsável Financeiro"));
            itens.add(new SelectItem("funcionario", "Funcionário"));
            itens.add(new SelectItem("candidato", "Candidato"));
            itens.add(new SelectItem("parceiro", "Parceiro"));
            itens.add(new SelectItem("fornecedor", "Fornecedor"));
        }
        return itens;
    }

    public List getListaSelectItemTipoOrdenacaoRelatorio() throws Exception {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("nomeAluno", "Nome do Aluno"));
        itens.add(new SelectItem("nomeResponsavelFinanceiro", "Nome do Responsável Financeiro"));
        itens.add(new SelectItem("data", "Data do recebimento"));
        return itens;
    }

    public List getListaSelectItemSituacaoContaReceber() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem(0, ""));
        Hashtable receberRecebidoNegociado = (Hashtable) Dominios.getReceberRecebidoNegociado();
        Enumeration keys = receberRecebidoNegociado.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) receberRecebidoNegociado.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    public List getListaSelectItemTipoOrigemContaReceber() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoOrigemContaReceber = (Hashtable) Dominios.getTipoOrigemContaReceber();
        Enumeration keys = tipoOrigemContaReceber.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoOrigemContaReceber.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    public void montarListaSelectItemContaCorrente(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarContaCorrentePorNumero(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDescricaoCompletaConta().toString()));
            }
            setListaSelectItemContaCorrente(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public Boolean getApresentarCondicaoPagamento() {
        if (getPlanoFinanceiroCursoVO().getCodigo().equals(0)) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    public void montarListaSelectItemCondicaoPagamentoPlanoFinanceiroCurso() {
        try {
            montarListaSelectItemCondicaoPagamentoPlanoFinanceiroCurso("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());
            ;
        }
    }

    public void montarListaSelectItemCondicaoPagamentoPlanoFinanceiroCurso(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorCodigoPlanoFinanceiroCurso(getPlanoFinanceiroCursoVO().getCodigo(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                CondicaoPagamentoPlanoFinanceiroCursoVO obj = (CondicaoPagamentoPlanoFinanceiroCursoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao().toString()));
            }
            setListaSelectItemCondicaoPagamentoPlanoFinanceiroCurso(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public void montarListaSelectItemContaCorrente() {
        try {
            montarListaSelectItemContaCorrente("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());
            ;
        }
    }

    public List consultarContaCorrentePorNumero(String numeroPrm) throws Exception {
        List lista = getFacadeFactory().getContaCorrenteFacade().consultarPorNumero(numeroPrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
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

    public String getCampoConsultaCandidato() {
        if (campoConsultaCandidato == null) {
            campoConsultaCandidato = "";
        }
        return campoConsultaCandidato;
    }

    public void setCampoConsultaCandidato(String campoConsultaCandidato) {
        this.campoConsultaCandidato = campoConsultaCandidato;
    }

    public String getCampoConsultaCentroReceita() {
        return campoConsultaCentroReceita;
    }

    public void setCampoConsultaCentroReceita(String campoConsultaCentroReceita) {
        this.campoConsultaCentroReceita = campoConsultaCentroReceita;
    }

    public String getCampoConsultaFuncionario() {
        return campoConsultaFuncionario;
    }

    public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
        this.campoConsultaFuncionario = campoConsultaFuncionario;
    }

    public List getListaConsultaAluno() {
        return listaConsultaAluno;
    }

    public void setListaConsultaAluno(List listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

    public List getListaConsultaCandidato() {
        if (listaConsultaCandidato == null) {
            listaConsultaCandidato = new ArrayList(0);
        }
        return listaConsultaCandidato;
    }

    public void setListaConsultaCandidato(List listaConsultaCandidato) {
        this.listaConsultaCandidato = listaConsultaCandidato;
    }

    public List getListaConsultaCentroReceita() {
        return listaConsultaCentroReceita;
    }

    public void setListaConsultaCentroReceita(List listaConsultaCentroReceita) {
        this.listaConsultaCentroReceita = listaConsultaCentroReceita;
    }

    public List getListaConsultaFuncionario() {
        return listaConsultaFuncionario;
    }

    public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
        this.listaConsultaFuncionario = listaConsultaFuncionario;
    }

    public List getListaSelectItemFuncionario() {
        return listaSelectItemFuncionario;
    }

    public void setListaSelectItemFuncionario(List listaSelectItemFuncionario) {
        this.listaSelectItemFuncionario = listaSelectItemFuncionario;
    }

    public List getListaSelectItemMatriculaAluno() {
        return listaSelectItemMatriculaAluno;
    }

    public void setListaSelectItemMatriculaAluno(List listaSelectItemMatriculaAluno) {
        this.listaSelectItemMatriculaAluno = listaSelectItemMatriculaAluno;
    }

    public String getValorConsultaAluno() {
        return valorConsultaAluno;
    }

    public void setValorConsultaAluno(String valorConsultaAluno) {
        this.valorConsultaAluno = valorConsultaAluno;
    }

    public String getValorConsultaCandidato() {
        return valorConsultaCandidato;
    }

    public void setValorConsultaCandidato(String valorConsultaCandidato) {
        this.valorConsultaCandidato = valorConsultaCandidato;
    }

    public String getValorConsultaCentroReceita() {
        return valorConsultaCentroReceita;
    }

    public void setValorConsultaCentroReceita(String valorConsultaCentroReceita) {
        this.valorConsultaCentroReceita = valorConsultaCentroReceita;
    }

    public String getValorConsultaFuncionario() {
        return valorConsultaFuncionario;
    }

    public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
        this.valorConsultaFuncionario = valorConsultaFuncionario;
    }

    public Boolean getAluno() {
        return aluno;
    }

    public void setAluno(Boolean aluno) {
        this.aluno = aluno;
    }

    public Boolean getCandidato() {
        return candidato;
    }

    public void setCandidato(Boolean candidato) {
        this.candidato = candidato;
    }

    public Boolean getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Boolean funcionario) {
        this.funcionario = funcionario;
    }

    public List getListaSelectItemContaCorrente() {
        if (listaSelectItemContaCorrente == null) {
            listaSelectItemContaCorrente = new ArrayList(0);
        }
        return listaSelectItemContaCorrente;
    }

    public void setListaSelectItemContaCorrente(List listaSelectItemContaCorrente) {
        this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
    }

    public Boolean getParceiro() {
        return parceiro;
    }

    public void setParceiro(Boolean parceiro) {
        this.parceiro = parceiro;
    }

    public List getListaConsultaParceiro() {
        return listaConsultaParceiro;
    }

    public void setListaConsultaParceiro(List listaConsultaParceiro) {
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

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return listaSelectItemUnidadeEnsino;
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

    public Integer getCodigoUnidadeEnsino() {
        if (codigoUnidadeEnsino == null) {
            codigoUnidadeEnsino = 0;
        }
        return codigoUnidadeEnsino;
    }

    public void setCodigoUnidadeEnsino(Integer codigoUnidadeEnsino) {
        this.codigoUnidadeEnsino = codigoUnidadeEnsino;
    }

    public Integer getCodigoContaCorrente() {
        if (codigoContaCorrente == null) {
            codigoContaCorrente = 0;
        }
        return codigoContaCorrente;
    }

    public void setCodigoContaCorrente(Integer codigoContaCorrente) {
        this.codigoContaCorrente = codigoContaCorrente;
    }

    public void setTipoOrdenacao(String tipoOrdenacao) {
        this.tipoOrdenacao = tipoOrdenacao;
    }

    public String getTipoOrdenacao() {
        if (tipoOrdenacao == null) {
            tipoOrdenacao = "";
        }
        return tipoOrdenacao;
    }

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public String getTipoPessoa() {
        if (tipoPessoa == null) {
            tipoPessoa = "";
        }
        return tipoPessoa;
    }

    public String getCampoDescritivo() {
        if (campoDescritivo == null) {
            campoDescritivo = "";
        }
        return campoDescritivo;
    }

    public void setCampoDescritivo(String campoDescritivo) {
        this.campoDescritivo = campoDescritivo;
    }

    public String getValorDescritivo() {
        if (valorDescritivo == null) {
            valorDescritivo = "";
        }
        return valorDescritivo;
    }

    public void setValorDescritivo(String valorDescritivo) {
        this.valorDescritivo = valorDescritivo;
    }

    public void setMostrarCamposDescritivos(Boolean mostrarCamposDescritivos) {
        this.mostrarCamposDescritivos = mostrarCamposDescritivos;
    }

    public Boolean getMostrarCamposDescritivos() {
        if (mostrarCamposDescritivos == null) {
            mostrarCamposDescritivos = Boolean.FALSE;
        }
        return mostrarCamposDescritivos;
    }

    public Integer getCodigoPessoa() {
        if (codigoPessoa == null) {
            codigoPessoa = 0;
        }
        return codigoPessoa;
    }

    public void setCodigoPessoa(Integer codigoPessoa) {
        this.codigoPessoa = codigoPessoa;
    }

    public Integer getCodigoParceiro() {
        if (codigoParceiro == null) {
            codigoParceiro = 0;
        }
        return codigoParceiro;
    }

    public void setCodigoParceiro(Integer codigoParceiro) {
        this.codigoParceiro = codigoParceiro;
    }

    public Boolean getResponsavelFinanceiro() {
        if (responsavelFinanceiro == null) {
            responsavelFinanceiro = false;
        }
        return responsavelFinanceiro;
    }

    public void setResponsavelFinanceiro(Boolean responsavelFinanceiro) {
        this.responsavelFinanceiro = responsavelFinanceiro;
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

    public List getListaSelectItemParcelas() {
        if (listaSelectItemParcelas == null) {
            listaSelectItemParcelas = new ArrayList(0);
        }
        return listaSelectItemParcelas;
    }

    public void setListaSelectItemParcelas(List listaSelectItemParcelas) {
        this.listaSelectItemParcelas = listaSelectItemParcelas;
    }

    public String getParcela() {
        if (parcela == null) {
            parcela = "";
        }
        return parcela;
    }

    public void setParcela(String parcela) {
        this.parcela = parcela;
    }

    /**
     * @return the centroReceita
     */
    public Integer getCentroReceita() {
        if (centroReceita == null) {
            centroReceita = new Integer(0);
        }
        return centroReceita;
    }

    public void limparCentroReceita() {
        setCentroReceita(0);
        setCentroReceitaDescricao("");
    }

    public void limparPlanoFinanceiroCurso() {
        setPlanoFinanceiroCursoVO(new PlanoFinanceiroCursoVO());
    }

    public void selecionarPlanoFinanceiroCurso() {
        PlanoFinanceiroCursoVO obj = (PlanoFinanceiroCursoVO) context().getExternalContext().getRequestMap().get("planoFinanceiroCursoItens");
        setPlanoFinanceiroCursoVO(obj);
        montarListaSelectItemCondicaoPagamentoPlanoFinanceiroCurso();
        setCampoConsultaPlanoFinanceiroCurso("");
        setValorConsultaPlanoFinanceiroCurso("");
        setListaConsultaPlanoFinanceiroCurso(new ArrayList(0));
    }

    public void selecionarCentroReceita() {
        CentroReceitaVO obj = (CentroReceitaVO) context().getExternalContext().getRequestMap().get("centroReceitaItens");
        setCentroReceita(obj.getCodigo());
        setCentroReceitaDescricao(obj.getDescricao());
        setCampoConsultaCentroReceita("");
        setValorConsultaCentroReceita("");
        setListaConsultaCentroReceita(new ArrayList(0));
    }

    /**
     * @param centroReceita
     *            the centroReceita to set
     */
    public void setCentroReceita(Integer centroReceita) {
        this.centroReceita = centroReceita;
    }

    /**
     * @return the centroReceitaDescricao
     */
    public String getCentroReceitaDescricao() {
        if (centroReceitaDescricao == null) {
            centroReceitaDescricao = "";
        }
        return centroReceitaDescricao;
    }

    /**
     * @param centroReceitaDescricao
     *            the centroReceitaDescricao to set
     */
    public void setCentroReceitaDescricao(String centroReceitaDescricao) {
        this.centroReceitaDescricao = centroReceitaDescricao;
    }

    /**
     * @return the listaConsultaPlanoFinanceiroCurso
     */
    public List getListaConsultaPlanoFinanceiroCurso() {
        return listaConsultaPlanoFinanceiroCurso;
    }

    /**
     * @param listaConsultaPlanoFinanceiroCurso
     *            the listaConsultaPlanoFinanceiroCurso to set
     */
    public void setListaConsultaPlanoFinanceiroCurso(List listaConsultaPlanoFinanceiroCurso) {
        this.listaConsultaPlanoFinanceiroCurso = listaConsultaPlanoFinanceiroCurso;
    }

    /**
     * @return the valorConsultaPlanoFinanceiroCurso
     */
    public String getValorConsultaPlanoFinanceiroCurso() {
        return valorConsultaPlanoFinanceiroCurso;
    }

    /**
     * @param valorConsultaPlanoFinanceiroCurso
     *            the valorConsultaPlanoFinanceiroCurso to set
     */
    public void setValorConsultaPlanoFinanceiroCurso(String valorConsultaPlanoFinanceiroCurso) {
        this.valorConsultaPlanoFinanceiroCurso = valorConsultaPlanoFinanceiroCurso;
    }

    /**
     * @return the campoConsultaPlanoFinanceiroCurso
     */
    public String getCampoConsultaPlanoFinanceiroCurso() {
        return campoConsultaPlanoFinanceiroCurso;
    }

    /**
     * @param campoConsultaPlanoFinanceiroCurso
     *            the campoConsultaPlanoFinanceiroCurso to set
     */
    public void setCampoConsultaPlanoFinanceiroCurso(String campoConsultaPlanoFinanceiroCurso) {
        this.campoConsultaPlanoFinanceiroCurso = campoConsultaPlanoFinanceiroCurso;
    }

    /**
     * @return the planoFinanceiroCursoVO
     */
    public PlanoFinanceiroCursoVO getPlanoFinanceiroCursoVO() {
        if (planoFinanceiroCursoVO == null) {
            planoFinanceiroCursoVO = new PlanoFinanceiroCursoVO();
        }
        return planoFinanceiroCursoVO;
    }

    /**
     * @param planoFinanceiroCursoVO
     *            the planoFinanceiroCursoVO to set
     */
    public void setPlanoFinanceiroCursoVO(PlanoFinanceiroCursoVO planoFinanceiroCursoVO) {
        this.planoFinanceiroCursoVO = planoFinanceiroCursoVO;
    }

    /**
     * @return the condicaoPagamentoPlanoFinanceiroCurso
     */
    public CondicaoPagamentoPlanoFinanceiroCursoVO getCondicaoPagamentoPlanoFinanceiroCurso() {
        if (condicaoPagamentoPlanoFinanceiroCurso == null) {
            condicaoPagamentoPlanoFinanceiroCurso = new CondicaoPagamentoPlanoFinanceiroCursoVO();
        }
        return condicaoPagamentoPlanoFinanceiroCurso;
    }

    /**
     * @param condicaoPagamentoPlanoFinanceiroCurso
     *            the condicaoPagamentoPlanoFinanceiroCurso to set
     */
    public void setCondicaoPagamentoPlanoFinanceiroCurso(CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCurso) {
        this.condicaoPagamentoPlanoFinanceiroCurso = condicaoPagamentoPlanoFinanceiroCurso;
    }

    /**
     * @return the listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso
     */
    public List getListaSelectItemCondicaoPagamentoPlanoFinanceiroCurso() {
        if (listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso == null) {
            listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso = new ArrayList();
        }
        return listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso;
    }

    /**
     * @param listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso
     *            the listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso to
     *            set
     */
    public void setListaSelectItemCondicaoPagamentoPlanoFinanceiroCurso(List listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso) {
        this.listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso = listaSelectItemCondicaoPagamentoPlanoFinanceiroCurso;
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

    public Boolean getFornecedor() {
        return getTipoPessoa().equals("fornecedor");
    }

    public void setFornecedor(Boolean fornecedor) {
        this.fornecedor = fornecedor;
    }

    public List<SelectItem> getListaSelectItemLayout() {
        if (listaSelectItemLayout == null) {
            listaSelectItemLayout = new ArrayList<SelectItem>(0);
            listaSelectItemLayout.add(new SelectItem("padrao", "Analítico Padrão"));
            listaSelectItemLayout.add(new SelectItem("centroReceita", "Analítico por Centro de Receita"));
            listaSelectItemLayout.add(new SelectItem("diadia", "Analítico Dia a Dia"));
            listaSelectItemLayout.add(new SelectItem("sinteticoCentroReceita", "Sintético por Centro de Receita"));
            listaSelectItemLayout.add(new SelectItem("sinteticoFormaRecebimento", "Sintético Forma Recebimento"));
            listaSelectItemLayout.add(new SelectItem("faturamentoSinteticoCurso", "Faturamento - Sintético por Curso"));
            listaSelectItemLayout.add(new SelectItem("faturamentoSinteticoCursoCentroReceita", "Faturamento - Sintético por Curso / Tipo Origem"));
        }
        return listaSelectItemLayout;
    }

    public void setListaSelectItemLayout(List<SelectItem> listaSelectItemLayout) {
        this.listaSelectItemLayout = listaSelectItemLayout;
    }

    public String getLayout() {
        if (layout == null) {
            layout = "";
        }
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public void limparCurso() throws Exception {
        try {
            setUnidadeEnsinoCursoVO(null);
            setTurmaVO(null);
        } catch (Exception e) {
        }
    }

    public void limparTurma() {
        try {
            setTurmaVO(null);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaComboTurma() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        return itens;
    }

    public void consultarTurma() {
        try {
            // if (getUnidadeEnsinoVO().getCodigo() == 0) {
            // throw new Exception("Informe a Unidade de Ensino.");
            // }
            super.consultar();
            List objs = new ArrayList(0);
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getCodigoUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarTurma() {
        try {
            TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
            setTurmaVO(obj);
            if (getUnidadeEnsinoCursoVO().getCurso().getCodigo() == 0) {
                setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidadeTurno(getTurmaVO().getCurso().getCodigo(), getCodigoUnidadeEnsino(), getTurmaVO().getTurno().getCodigo(), getUsuarioLogado()));
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarCurso() throws Exception {
        try {
            CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
            getUnidadeEnsinoCursoVO().setCurso(obj);
            montarListaSelectItemTurno(obj.getCodigo());
            limparTurma();
            getListaConsultaTurma().clear();
        } catch (Exception e) {
        }
    }

	public List<SelectItem> getListaSelectItensTipoAgente() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem(TipoAgenteNegativacaoCobrancaContaReceberEnum.NEGATIVACAO, "Negativação"));
		itens.add(new SelectItem(TipoAgenteNegativacaoCobrancaContaReceberEnum.COBRANCA, "Cobrança"));
		return itens;
	}
	
	public void selecionarAgente() throws Exception {
		try {
			AgenteNegativacaoCobrancaContaReceberVO obj = (AgenteNegativacaoCobrancaContaReceberVO) context().getExternalContext().getRequestMap().get("agenteItens");
			setAgente(getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().consultarPorChavePrimaria(obj.getCodigo(), false,Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));			
			if (!getAgente().getTipoAmbos()) {
				setTipoAgente(getAgente().getTipo());
			} else {
				setTipoAgente(TipoAgenteNegativacaoCobrancaContaReceberEnum.NEGATIVACAO);
			}
			setCampoConsultaAgente("");
			setValorConsultaAgente("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparAgente() throws Exception {
		try {
			setAgente(new AgenteNegativacaoCobrancaContaReceberVO());
			setListaConsultaAgente(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAgente() {
		try {
			List<AgenteNegativacaoCobrancaContaReceberVO> objs = new ArrayList<AgenteNegativacaoCobrancaContaReceberVO>(0);
			if (getCampoConsultaAgente().equals("nome")) {
				objs = getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().consultarPorNome(getValorConsultaAgente(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAgente().equals("tipo")) {
            	objs = getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().consultarPorTipo(getValorConsultaAgente(), false, getUsuarioLogado());
            }
			setListaConsultaAgente(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAgente(new ArrayList<AgenteNegativacaoCobrancaContaReceberVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboAgente() {
		if (tipoConsultaComboAgente == null) {
			tipoConsultaComboAgente = new ArrayList<SelectItem>(0);
			tipoConsultaComboAgente.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboAgente.add(new SelectItem("tipo", "Tipo Agente"));
		}
		return tipoConsultaComboAgente;
	}

    public void montarListaSelectItemTurno(Integer curso) {
        try {
            List<TurnoVO> resultadoConsulta = getFacadeFactory().getTurnoFacade().consultarPorCodigoCurso(curso, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            setListaSelectItemTurno(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarCurso() {
        try {
            // if (getProcessoMatriculaVO().getCodigo() == 0) {
            // throw new Exception("Informe o Processo de Matrícula.");
            // }
            List objs = new ArrayList(0);
            if (getCampoConsultaCurso().equals("nome")) {
                objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNome(getValorConsultaCurso(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, false, getUsuarioLogado());
            }
            setListaConsultaCurso(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCurso(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaComboCurso() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
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

    public List getListaConsultaTurma() {
        if (listaConsultaTurma == null) {
            listaConsultaTurma = new ArrayList();
        }
        return listaConsultaTurma;
    }

    public void setListaConsultaTurma(List listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
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

    public List getListaConsultaCurso() {
        if (listaConsultaCurso == null) {
            listaConsultaCurso = new ArrayList();
        }
        return listaConsultaCurso;
    }

    public void setListaConsultaCurso(List listaConsultaCurso) {
        this.listaConsultaCurso = listaConsultaCurso;
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

    public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
        if (unidadeEnsinoCursoVO == null) {
            unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
        }
        return unidadeEnsinoCursoVO;
    }

    public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
        this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
    }

    public Boolean getIsPermitirApenasRelatorioContaReceberRecebimentoBiblioteca() {
        if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirApenasRelatorioRecebimentoBiblioteca()) {
            getFiltroRelatorioFinanceiroVO().setTipoOrigemBiblioteca(true);
            getFiltroRelatorioFinanceiroVO().setTipoOrigemMensalidade(false);
            getFiltroRelatorioFinanceiroVO().setTipoOrigemMatricula(false);
            return true;
        }
        return false;
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

    public List<SelectItem> getListaSelectItemTurno() {
        if (listaSelectItemTurno == null) {
            listaSelectItemTurno = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemTurno;
    }

    public void setListaSelectItemTurno(List<SelectItem> listaSelectItemTurno) {
        this.listaSelectItemTurno = listaSelectItemTurno;
    }

    /**
     * @return the utilizarDataBaseFaturamento
     */
    public Boolean getUtilizarDataBaseFaturamento() {
        if (utilizarDataBaseFaturamento == null) {
            utilizarDataBaseFaturamento = Boolean.FALSE;
        }
        return utilizarDataBaseFaturamento;
    }
    
   /**
     * @param utilizarDataBaseFaturamento the utilizarDataBaseFaturamento to set
     */
    public void setUtilizarDataBaseFaturamento(Boolean utilizarDataBaseFaturamento) {
        this.utilizarDataBaseFaturamento = utilizarDataBaseFaturamento;
    }

    /**
     * @return the dataBaseFaturamento
     */
    public Date getDataBaseFaturamento() {
        if (dataBaseFaturamento == null) {
            dataBaseFaturamento = this.getDataFim();
        }
        return dataBaseFaturamento;
    }

    /**
     * @param dataBaseFaturamento the dataBaseFaturamento to set
     */
    public void setDataBaseFaturamento(Date dataBaseFaturamento) {
        this.dataBaseFaturamento = dataBaseFaturamento;
    }

    public void prepararRelatorioDataBaseFaturamento() {
        if ((this.getLayout().equals("faturamentoSinteticoCurso")) ||
            (this.getLayout().equals("faturamentoSinteticoCursoCentroReceita"))) {
            this.dataBaseFaturamento = this.getDataFim();
            this.utilizarDataBaseFaturamento = Boolean.TRUE;
            this.getFiltroRelatorioFinanceiroVO().setFiltrarPorDataCompetencia(Boolean.TRUE);
        }
    }
    
    public Boolean getApresentarOpcaoDataBaseFaturamento() {
        if ((this.getLayout().equals("faturamentoSinteticoCurso")) ||
            (this.getLayout().equals("faturamentoSinteticoCursoCentroReceita"))) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    public Date getDataInicioCompetencia() {
		return dataInicioCompetencia;
	}

	public void setDataInicioCompetencia(Date dataInicioCompetencia) {
		this.dataInicioCompetencia = dataInicioCompetencia;
	}

	public Date getDataFimCompetencia() {
		return dataFimCompetencia;
	}

	public void setDataFimCompetencia(Date dataFimCompetencia) {
		this.dataFimCompetencia = dataFimCompetencia;
	}

	public Boolean getApresentarQuadroResumoCalouroVeterano() {
		if (apresentarQuadroResumoCalouroVeterano == null) {
			apresentarQuadroResumoCalouroVeterano = Boolean.FALSE;
		}
		return apresentarQuadroResumoCalouroVeterano;
	}

	public void setApresentarQuadroResumoCalouroVeterano(Boolean apresentarQuadroResumoCalouroVeterano) {
		this.apresentarQuadroResumoCalouroVeterano = apresentarQuadroResumoCalouroVeterano;
	}
	
	public Boolean getConsiderarUnidadeEnsinoFinanceira() {
		if (considerarUnidadeEnsinoFinanceira == null) {
			considerarUnidadeEnsinoFinanceira = false;
		}
		return considerarUnidadeEnsinoFinanceira;
	}

	public void setConsiderarUnidadeEnsinoFinanceira(Boolean considerarUnidadeEnsinoFinanceira) {
		this.considerarUnidadeEnsinoFinanceira = considerarUnidadeEnsinoFinanceira;
	}


	public AgenteNegativacaoCobrancaContaReceberVO getAgente() {
		if (agente == null) {
			agente = new AgenteNegativacaoCobrancaContaReceberVO();
		}
		return agente;
	}

	public void setAgente(AgenteNegativacaoCobrancaContaReceberVO agente) {
		this.agente = agente;
	}
	
	public String getCampoConsultaAgente() {
		if (campoConsultaAgente == null) {
			campoConsultaAgente = "nome";
		}
		return campoConsultaAgente;
	}

	public void setCampoConsultaAgente(String campoConsultaAgente) {
		this.campoConsultaAgente = campoConsultaAgente;
	}

	public String getValorConsultaAgente() {
		if (valorConsultaAgente == null) {
			valorConsultaAgente = "";
		}
		return valorConsultaAgente;
	}

	public void setValorConsultaAgente(String valorConsultaAgente) {
		this.valorConsultaAgente = valorConsultaAgente;
	}

	public List<AgenteNegativacaoCobrancaContaReceberVO> getListaConsultaAgente() {
		if (listaConsultaAgente == null) {
			listaConsultaAgente = new ArrayList<AgenteNegativacaoCobrancaContaReceberVO>();
		}
		return listaConsultaAgente;
	}

	public void setListaConsultaAgente(List<AgenteNegativacaoCobrancaContaReceberVO> listaConsultaAgente) {
		this.listaConsultaAgente = listaConsultaAgente;
	}

	public TipoAgenteNegativacaoCobrancaContaReceberEnum getTipoAgente() {
		return tipoAgente;
	}

	public void setTipoAgente(TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente) {
		this.tipoAgente = tipoAgente;
	}

	public List<SelectItem> getListaSelectItemFiltrarPeriodoPor() {
		if(listaSelectItemFiltrarPeriodoPor == null) {
			listaSelectItemFiltrarPeriodoPor =  new ArrayList<SelectItem>(3);
			listaSelectItemFiltrarPeriodoPor.add(new SelectItem("DATA_RECEBIMENTO", "Data Recebimento"));
			listaSelectItemFiltrarPeriodoPor.add(new SelectItem("DATA_VENCIMENTO", "Data Vencimento"));
			listaSelectItemFiltrarPeriodoPor.add(new SelectItem("DATA_COMPENSACAO", "Data Compensação"));
		}
		return listaSelectItemFiltrarPeriodoPor;
	}

	public void setListaSelectItemFiltrarPeriodoPor(List<SelectItem> listaSelectItemFiltrarPeriodoPor) {
		this.listaSelectItemFiltrarPeriodoPor = listaSelectItemFiltrarPeriodoPor;
	}

	public String getFiltrarPeriodoPor() {
		if(filtrarPeriodoPor == null) {
			filtrarPeriodoPor = "DATA_RECEBIMENTO";
		}
		return filtrarPeriodoPor;
	}

	public void setFiltrarPeriodoPor(String filtrarPeriodoPor) {
		this.filtrarPeriodoPor = filtrarPeriodoPor;
	}

	public MatriculaVO getMatriculaVO() {
		if(matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public void limparMatricula() {
		setMatriculaVO(null);
	}
}
