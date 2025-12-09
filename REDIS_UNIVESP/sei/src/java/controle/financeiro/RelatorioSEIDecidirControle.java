package controle.financeiro;

import java.text.ParseException;
/**
 * @author Leonardo Riciolle
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularEstagioVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FiltroPersonalizadoOpcaoVO;
import negocio.comuns.administrativo.FiltroPersonalizadoVO;
import negocio.comuns.administrativo.DepartamentoVO.EnumCampoConsultaDepartamento;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.FormaContratacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.RelatorioSEIDecidirModuloEnum;
import negocio.comuns.administrativo.enumeradores.RelatorioSEIDecidirNivelDetalhamentoEnum;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoFiltroEnum;
import negocio.comuns.administrativo.enumeradores.TipoRecebimentoEnum;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.RelatorioSEIDecidirVO;
import negocio.comuns.financeiro.enumerador.TipoFiltroPeriodoSeiDecidirEnum;
import negocio.comuns.planoorcamentario.PlanoOrcamentarioVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.enumeradores.SituacaoResultadoProcessoSeletivoEnum;
import negocio.comuns.protocolo.SituacaoRequerimentoDepartamentoVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.CompetenciaPeriodoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.SecaoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.SecaoFolhaPagamentoVO.EnumCampoConsultaSecaoFolhaPagamento;
import negocio.comuns.recursoshumanos.enumeradores.MotivoMudancaCargoEnum;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoHoraAtividadeExtraClasseEnum;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoPeriodoAquisitivoEnum;
import negocio.comuns.utilitarias.AcessoException;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.processosel.ProcSeletivoUnidadeEnsino;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.avaliacaoInst.enumeradores.NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum;

@Controller("RelatorioSEIDecidirControle")
@Scope("viewScope")
@Lazy
public class RelatorioSEIDecidirControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 6804789116502843608L;

	private RelatorioSEIDecidirVO relatorioSEIDecidirVO;
	private List<SelectItem> listaSelectItemNivelEducacional;
	private List<PessoaVO> listaConsultaResponsavelFinanceiro;
	private List<TurmaVO> listaConsultaTurma;
	private List<SelectItem> listaSelectItemContaCorrente;
	private List<MatriculaVO> listaConsultaAluno;
	private List<FuncionarioVO> listaConsultaFuncionario;	
	private List<PessoaVO> listaConsultaCandidato;
	private List<ParceiroVO> listaConsultaParceiro;
	private List<FornecedorVO> listaConsultaFornecedor;
	private List<CampanhaVO> listaConsultaCampanha;
	private List<SelectItem> tipoConsultaComboResponsavelFinanceiro;
	private List<SelectItem> listaSelectItemBanco;
	private List<SelectItem> tipoConsultaComboFornecedor;
	private List<SelectItem> listaSelectItemOperadoraCartao;
	private String campoConsultaFornecedor;
	private String valorConsultaFornecedor;
	private String campoConsultaResponsavelFinanceiro;
	private String valorConsultaResponsavelFinanceiro;
	private String campoConsultaParceiro;
	private String valorConsultaParceiro;
	private String valorConsultaTurma;
	private String valorConsultaAluno;
	private String campoConsultaCandidato;
	private String valorConsultaCandidato;
	private String valorConsultaFuncionario;
	private String campoConsultaTurma;
	private String campoConsultaAluno;
	private String campoConsultaFuncionario;
	private String campoConsultaCampanha;	
	private String valorConsultaCampanha;
	private Boolean marcarTodasCampanhas;
	private Boolean marcarTodosFuncionarios;

	private String campoDescritivo;
	private String valorDescritivo;
	// Variaveis Responsaveis Para Marcar GradeCurricularEstagioVOs Selecionados
		private Boolean marcarTodosGradeCurricularEstagios;
	// Variaveis Responsaveis Para Marcar CursoVOs Selecionados
	private Boolean marcarTodosCursos;
	// Variaveis Reponsaveis para marcar TurnosVOs selecionados
	private Boolean marcarTodosTurnos;
	// Variaveis Reponsaveis para marcar CentroReceitaVO selecionados
	private Boolean marcarTodosCentroReceitas;
	// Variaveis Reponsaveis para marcar CategoriaDespesaVO selecionados
	private Boolean marcarTodosCategoriaDespesa;
	private List<SelectItem> listaSelectItemLayoutRelatorioSeiDecidir;
	private List<SelectItem> listaSelectItemTipoFiltroPeriodo;
	private RelatorioSEIDecidirModuloEnum modulo;
	
	private List<SelectItem> listaSelectItemFormacontratacao;
	private List<SelectItem> listaSelectItemRecebimento;
	private List<SelectItem> listaSelectItemSituacao;

	private List<SelectItem> listaSelectItemSituacaoPeriodoAquisitivo;
	private List<SelectItem> listaSelectItemMotivoProgressaoSalarial;
	
	private String[] formaContratacao = {};
	private String[] recebimento = {};
	private String[] situacao = {};
	
	private Boolean marcarTodosFormaContratacao;
	private Boolean marcarTodosRecebimento;
	private Boolean marcarTodosSituacao;
	
	private List<SelectItem> listaSelectItemCompetenciaPeriodo;
	
	private String campoConsultaSecaoFolhaPagamento;
    private String valorConsultaSecaoFolhaPagamento;
	private List<SecaoFolhaPagamentoVO> listaConsultaSecaoFolhaPagamento;
	
	private String campoConsultaEvento;
	private String valorConsultaEvento;
	private List<EventoFolhaPagamentoVO> listaEventosFolhaPagamento;
	
    private UnidadeEnsinoVO unidadeEnsino;
    
    private List<FuncionarioCargoVO> funcionarioCargoVOs;
    
    private List<SelectItem> tipoConsultaComboCargo;
    private DataModelo dataModeloCargo;
    
   
    private List<SelectItem> listaSelectItemNivelCentroResultado;
    
    private String campoConsultaDisciplina;
    private String valorConsultaDisciplina;
    private List listaConsultaDisciplina;
    
    private String campoConsultaTurmaEstudouDisciplina;
    private String valorConsultaTurmaEstudouDisciplina;
    private List<TurmaVO> listaConsultaTurmaEstudouDisciplinaVOs;   
    
    private Boolean marcarTodasSituacoesInscricao;    

	/**
	 * Com este marcado não será apresentado filtros, topo, nome de colunas, totais e subtotais
	 */
	private Boolean gerarFormatoExportacaoDados;
	private String campoSeparador;
	private String extensaoArquivo;
	
	private String campoConsultaDepartamento;
    private String valorConsultaDepartamento;
    protected List<DepartamentoVO> listaConsultaDepartamento;
    
    private String campoConsultaCategoriaDespesa;
	private String valorConsultaCategoriaDespesa;
	protected List<CategoriaDespesaVO> listaConsultaCategoriaDespesa;
	
	private String campoConsultaCategoriaProduto;
	private String valorConsultaCategoriaProduto;
	private List<CategoriaProdutoVO> listaConsultaCategoriaProduto;

	private DataModelo dataModeloCentroResultado;
	private SituacaoEnum situacaoEnumFiltro;
	
	private List<PlanoOrcamentarioVO> planosOrcamentarios;
	
	/*private List<CentroResultadoVO> listaConsultaCentroResultado;*/
	
	private Boolean marcarTodosPlanoOrcamentario;
	private Boolean marcarTodosDepartamento;
	private Boolean marcarTodasCategoriaDespesa;
	private Boolean marcarTodasCategoriaProduto;
	private Boolean marcarTodosCentroResultado;
	
	private String planoOrcamentarioApresentar;
	private String categoriaProdutoApresentar;
	/*private String centroResultadoApresentar;*/
	private String departamentoApresentar;
	private DataModelo dataModeloPlanoOrcamentario;
	
	private List<SelectItem> tipoConsultaComboDisciplina;
	private Boolean marcarTodasSituacoesFinanceirasRequerimento;
	private Boolean marcarTodasSituacoesRequerimento;
	private String campoConsultaTurmaReposicao;
	private String valorConsultaTurmaReposicao;
	private List<TurmaVO> listaConsultaTurmaReposicao;
	private String campoConsultaRequerente;
	private String valorConsultaRequerente;
	private List<PessoaVO> listaConsultaRequerente;
	private String campoConsultaCoordenador;
	private String valorConsultaCoordenador;
	private List<PessoaVO> listaConsultaCoordenador;
	private List<SelectItem> listaSelectItemDepartamentoResponsavel;
	private List<SelectItem> listaSelectItemSituacaoRequerimentoDepartamentoCons;
	private String campoConsultaTipoRequerimento;
	private String valorConsultaTipoRequerimento;
	private List<TipoRequerimentoVO> listaConsultaTipoRequerimento;
	
	protected String valorConsultaProcSeletivo;
	protected String campoConsultaProcSeletivo;
	protected List<ProcSeletivoVO> listaConsultaProcSeletivo;
	protected List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemDataProva;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List listaConsultaCurso;
	private List<SelectItem> listaSelectItemSituacaoFinanceiraInscricao;
	private ProgressBarVO progressBarVO;

	public RelatorioSEIDecidirControle() {
		String origem = (String) context().getExternalContext().getSessionMap().get("modulo");
		if (Uteis.isAtributoPreenchido(origem)) {
			modulo = RelatorioSEIDecidirModuloEnum.valueOf(origem);
		} else {
			origem = ((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("ORIGEM");
			modulo = RelatorioSEIDecidirModuloEnum.getModuloAcessoMenu(origem);
		}
		if (modulo == null) {
			throw new AcessoException(UteisJSF.internacionalizar("msg_RelatorioSeiDecidir_moduloNaoInformado"));
		}
		switch (modulo) {
		case ACADEMICO:
			if(!getLoginControle().getPermissaoAcessoMenuVO().getRelatorioSEIDecidirAcademico()){
				throw new AcessoException(UteisJSF.internacionalizar("msg_RelatorioSeiDecidir_moduloAcademicoSemPerfil"));
			}
			break;
		case FINANCEIRO_RECEITA:		
			if(!getLoginControle().getPermissaoAcessoMenuVO().getRelatorioSEIDecidirReceita()){
				throw new AcessoException(UteisJSF.internacionalizar("msg_RelatorioSeiDecidir_moduloReceitaSemPerfil"));
			}
			break;
		case FINANCEIRO_CENTRO_RESULTADO:
			if(!getLoginControle().getPermissaoAcessoMenuVO().getRelatorioSEIDecidirReceita()){
				throw new AcessoException(UteisJSF.internacionalizar("msg_RelatorioSeiDecidir_moduloCentroResultadoSemPerfil"));
			}
			break;
		case FINANCEIRO_DESPESA:
			if(!getLoginControle().getPermissaoAcessoMenuVO().getRelatorioSEIDecidirDespesa()){
				throw new AcessoException(UteisJSF.internacionalizar("msg_RelatorioSeiDecidir_moduloDespesaSemPerfil"));
			}
			break;
		case RECURSOS_HUMANOS:
			if(!getLoginControle().getPermissaoAcessoMenuVO().getRelatorioSEIDecidirRecursosHumanos()){
				throw new AcessoException(UteisJSF.internacionalizar("msg_RelatorioSeiDecidir_moduloRhSemPerfil"));
			}
			break;
		case PLANO_ORCAMENTARIO:
			if(!getLoginControle().getPermissaoAcessoMenuVO().getRelatorioSEIDecidirPlanoOrcamentario()){
				throw new AcessoException(UteisJSF.internacionalizar("msg_RelatorioSeiDecidir_moduloPlanoOrcamentarioSemPerfil"));
			}
			carregarPesquisasPlanoOrcamentario();		
			break;
		case CRM:
			if(!getLoginControle().getPermissaoAcessoMenuVO().getRelatorioSEIDecidirCrm()){
				throw new AcessoException(UteisJSF.internacionalizar("msg_RelatorioSeiDecidir_moduloCrmSemPerfil"));
			}
			break;
		case ESTAGIO:
			if(!getLoginControle().getPermissaoAcessoMenuVO().getRelatorioSEIDecidirEstagio()){
				throw new AcessoException(UteisJSF.internacionalizar("msg_RelatorioSeiDecidir_moduloEstagioSemPerfil"));
			}			
			consultarGradeCurricularEstagioFiltroRelatorio();
			break;
		case PROCESSO_SELETIVO:
			if(!getLoginControle().getPermissaoAcessoMenuVO().getRelatorioSEIDecidirProcessoSeletivo()){
				throw new AcessoException(UteisJSF.internacionalizar("msg_RelatorioSeiDecidir_moduloProcessoSeletivoSemPerfil"));
			}
			break;
		case FINANCEIRO_ARVORE_FECHAMENTO_MES:
			if(!getLoginControle().getPermissaoAcessoMenuVO().getRelatorioSEIDecidirReceita()){
				throw new AcessoException(UteisJSF.internacionalizar("msg_RelatorioSeiDecidir_moduloReceitaSemPerfil"));
			}
			break;
		case FINANCEIRO_FECHAMENTO_MES_RECEITA:
			if(!getLoginControle().getPermissaoAcessoMenuVO().getRelatorioSEIDecidirReceita()){
				throw new AcessoException(UteisJSF.internacionalizar("msg_RelatorioSeiDecidir_moduloReceitaSemPerfil"));
			}
			break;
		case REQUERIMENTO:
			if(!getLoginControle().getPermissaoAcessoMenuVO().getRelatorioSEIDecidirRequerimento()){
				throw new AcessoException(UteisJSF.internacionalizar("msg_RelatorioSeiDecidir_moduloRequerimentoSemPerfil"));
			}
			break;
		
		case ADMINISTRATIVO:
			if(!getLoginControle().getPermissaoAcessoMenuVO().getRelatorioSEIDecidirAdministrativo()){
				throw new AcessoException(UteisJSF.internacionalizar("msg_RelatorioSeiDecidir_moduloAdministrativoSemPerfil"));
			}
			break;
		case AVALIACAO_INSTITUCIONAL:
			if(!getLoginControle().getPermissaoAcessoMenuVO().getRelatorioSEIDecidirAvaliacaoInstitucional()){
				throw new AcessoException(UteisJSF.internacionalizar("msg_RelatorioSeiDecidir_moduloAvaliacaoInstitucionalSemPerfil"));
			}
			break;
		case COMPRA:
			if(!getLoginControle().getPermissaoAcessoMenuVO().getRelatorioSEIDecidirCompra()){
				throw new AcessoException(UteisJSF.internacionalizar("msg_RelatorioSeiDecidir_moduloComprasSemPerfil"));
			}
			break;
		case EAD:
			if(!getLoginControle().getPermissaoAcessoMenuVO().getRelatorioSEIDecidirEAD()){
				throw new AcessoException(UteisJSF.internacionalizar("msg_RelatorioSeiDecidir_moduloEADSemPerfil"));
			}
			break;
		case NOTA_FISCAL:
			if(!getLoginControle().getPermissaoAcessoMenuVO().getRelatorioSEIDecidirNotaFiscal()){
				throw new AcessoException(UteisJSF.internacionalizar("msg_RelatorioSeiDecidir_moduloNotaFiscalSemPerfil"));
			}
			break;
		case PATRIMONIO:
			if(!getLoginControle().getPermissaoAcessoMenuVO().getRelatorioSEIDecidirPatrimonio()){
				throw new AcessoException(UteisJSF.internacionalizar("msg_RelatorioSeiDecidir_moduloPatrimonioSemPerfil"));
			}
			break;
		default:
			break;
		}

		setIdControlador(RelatorioSEIDecidirControle.class.getSimpleName()+origem);
//		consultarTurnoFiltroRelatorio();
//		consultarUnidadeEnsino();
		setMensagemID("msg_entre_prmrelatorio");
	}

	private void carregarPesquisasPlanoOrcamentario() {
		consultarPlanoOrcamentario();
		
	}
	
	@PostConstruct
	public void consultarFormaPagamento() {
		try {
			consultarTurnoFiltroRelatorio();
			consultarUnidadeEnsino();
			consultarFormaPagamentoFiltroRelatorio();
			inicializarDadosFormaPagamento();
			verificarTodasFormaPagamentosSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void inicializarDadosFormaPagamento() {
		for (FormaPagamentoVO formaPagamentoVO : getRelatorioSEIDecidirVO().getFormaPagamentoVOs()) {
			formaPagamentoVO.setFiltrarFormaPagamento(false);
		}
	}
	
	public Boolean apresentarCamposRequisicao() {
		return getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.REQUISICAO);
	}
	
	public boolean apresentarBotaoPlanilha() {
		return getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getTipoGeracaoRelatorio().equals(TipoRelatorioEnum.EXCEL) || apresentarBotaoPDF() ;
	}

	public boolean apresentarBotaoPDF() {
		return getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getTipoGeracaoRelatorio().equals(TipoRelatorioEnum.PDF);
	}
	
	public void consultarCategoriaDespesa() {
		try {
			List<CategoriaDespesaVO> objs = new ArrayList<>(0);
			if (this.campoConsultaCategoriaDespesa.equals("codigo")) {
				if (this.valorConsultaCategoriaDespesa.equals("")) {
					this.valorConsultaCategoriaDespesa = "0";
				}
				int valorInt = Integer.parseInt(this.valorConsultaCategoriaDespesa);
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (this.campoConsultaCategoriaDespesa.equals("identificador")) {
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa(this.valorConsultaCategoriaDespesa, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (this.campoConsultaCategoriaDespesa.equals("nome")) {
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricao(this.valorConsultaCategoriaDespesa, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCategoriaDespesa(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCategoriaDespesa(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarCategoriaProduto() {
		try {
			List<CategoriaProdutoVO> objs = new ArrayList<>(0);
			if (getCampoConsultaCategoriaProduto().equals("codigo")) {
				if (getValorConsultaCategoriaProduto().equals("")) {
					setValorConsultaCategoriaProduto("0");
				}
				int valorInt = Uteis.getValorInteiro(getValorConsultaCategoriaProduto());
				objs = getFacadeFactory().getCategoriaProdutoFacade().consultarPorCodigo((valorInt), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getCampoConsultaCategoriaProduto().equals("nome")) {
				objs = getFacadeFactory().getCategoriaProdutoFacade().consultarPorNome(getValorConsultaCategoriaProduto(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			setListaConsultaCategoriaProduto(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaCategoriaProduto(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboCategoriaProduto() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public Boolean apresentarCamposCentroResultado() {
		RelatorioSEIDecidirNivelDetalhamentoEnum nivelDetalhamento = getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento();
		
		return nivelDetalhamento.equals(RelatorioSEIDecidirNivelDetalhamentoEnum.CENTRORESULTADO_CATEGORIADESPESA) ? Boolean.TRUE : Boolean.FALSE;
	}
	
	public void consultarFormaPagamentoFiltroRelatorio() {
		try {
			getRelatorioSEIDecidirVO().getFormaPagamentoVOs().clear();
			getRelatorioSEIDecidirVO().setFormaPagamentoVOs(getFacadeFactory().getFormaPagamentoFacade().consultarFormaPagamentoFaltandoLista(getRelatorioSEIDecidirVO().getFormaPagamentoVOs(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			//setListaConsultarFormaPagamento(new ArrayList<FormaPagamentoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarPlanoOrcamentario() {
		try {
			getDataModeloPlanoOrcamentario().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			setPlanosOrcamentarios(getFacadeFactory().getPlanoOrcamentarioFacade().consultarPorNome("%%",getUnidadeEnsinoLogado().getCodigo(),  false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getDataModeloPlanoOrcamentario().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarDepartamento() {
		try {
			super.consultar();
			List<DepartamentoVO> objs = new ArrayList<DepartamentoVO>(0);
			if (getCampoConsultaDepartamento().equals(EnumCampoConsultaDepartamento.CODIGO.toString())) {
				int valorInt = 0;
				if (!getValorConsultaDepartamento().equals("")) {
					valorInt = Integer.parseInt(getValorConsultaDepartamento());
				}
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaDepartamento().equals(EnumCampoConsultaDepartamento.NOME.toString())) {
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorNome(getValorConsultaDepartamento(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}

			if (getCampoConsultaDepartamento().equals(EnumCampoConsultaDepartamento.NOME_PESSOA.toString())) {
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorNomePessoa(getValorConsultaDepartamento(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaDepartamento(objs);
			setMensagemID("msg_dados_consultados"); 
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	//TODO
	public void montarDadosPlanosOrcamentarios() throws Exception {
		limparCamposPlanoOrcamentario();
		if (Uteis.isAtributoPreenchido(getRelatorioSEIDecidirVO().getPlanosOrcamentarioVOs())) {
			setListaConsultaDepartamento(getFacadeFactory().getDetalhamentoPlanoOrcamentarioFacade().consultarDepartantoPorPlanoOrcamentario(getRelatorioSEIDecidirVO().getPlanosOrcamentarioVOs(), getUsuarioLogado()));
			getRelatorioSEIDecidirVO().setCategoriaDespesaVOs(getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().consultarCategoriaDespesaPorPlanoOrcamentario(getRelatorioSEIDecidirVO().getPlanosOrcamentarioVOs(), getUsuarioLogado()));
			setListaConsultaCategoriaProduto(getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().consultarCategoriaProdutoPorPlanoOrcamentario(getRelatorioSEIDecidirVO().getPlanosOrcamentarioVOs(), getUsuarioLogado()));
			getRelatorioSEIDecidirVO().setListaCentroResultadoVO(getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().consultarCentroResultadoPorPlanoOrcamentario(getRelatorioSEIDecidirVO().getPlanosOrcamentarioVOs(), getUsuarioLogado()));
		}
	}
	
	public void limparCamposPlanoOrcamentario() {
		getRelatorioSEIDecidirVO().setCategoriaDespesaVOs(new ArrayList<>());
		setListaConsultaCategoriaProduto(new ArrayList<>());
		setListaConsultaDepartamento(new ArrayList<>());
		getRelatorioSEIDecidirVO().setListaCentroResultadoVO(new ArrayList<>());
		getRelatorioSEIDecidirVO().setCategoriaDespesaApresentar("");
		setCategoriaProdutoApresentar("");
		setDepartamentoApresentar("");
		getRelatorioSEIDecidirVO().setCentroResultadoApresentar("");
	}

	public void marcarTodosPlanoOrcamentarioAction() {
		for (PlanoOrcamentarioVO plano : getPlanosOrcamentarios()) {
			plano.setFiltrarPlanoOrcamentarioVO(getMarcarTodosPlanoOrcamentario());
		}
		verificarTodosPlanoOrcamentarioSelecionados();
	}

	public void verificarTodosPlanoOrcamentarioSelecionados(){
		StringBuilder planoOrcamentario = new StringBuilder();
		getRelatorioSEIDecidirVO().setPlanosOrcamentarioVOs(new ArrayList<>());
		if (getPlanosOrcamentarios().size() > 1) {
			for (PlanoOrcamentarioVO obj : getPlanosOrcamentarios()) {
				if (obj.getFiltrarPlanoOrcamentarioVO()) {
					planoOrcamentario.append(obj.getCodigo()).append(" - ");
					planoOrcamentario.append(obj.getNome()).append("; ");
					getRelatorioSEIDecidirVO().getPlanosOrcamentarioVOs().add(obj);
				}
			}
			setPlanoOrcamentarioApresentar(planoOrcamentario.toString());
		} else {
			if (!getPlanosOrcamentarios().isEmpty()) {
				if (getPlanosOrcamentarios().get(0).getFiltrarPlanoOrcamentarioVO()) {
					setPlanoOrcamentarioApresentar(getPlanosOrcamentarios().get(0).getNome());
					getRelatorioSEIDecidirVO().getPlanosOrcamentarioVOs().add(getPlanosOrcamentarios().get(0));
				}
			} else {
				setPlanoOrcamentarioApresentar(planoOrcamentario.toString());
			}
		}
	}
	
	public void marcarTodosDepartamentoAction() {
		for (DepartamentoVO departamento : getListaConsultaDepartamento()) {
			departamento.setFiltrarDepartamento(getMarcarTodosDepartamento());
		}
		verificarTodosDepartamentoSelecionados();
	}

	public void verificarTodosDepartamentoSelecionados(){
		StringBuilder departamento = new StringBuilder();
		getRelatorioSEIDecidirVO().setDepartamentosVOs(new ArrayList<>());
		if (getListaConsultaDepartamento().size() > 1) {
			for (DepartamentoVO obj : getListaConsultaDepartamento()) {
				if (obj.isFiltrarDepartamento()) {
					departamento.append(obj.getCodigo()).append(" - ");
					departamento.append(obj.getNome()).append("; ");
					getRelatorioSEIDecidirVO().getDepartamentosVOs().add(obj);
				}
			}
			setDepartamentoApresentar(departamento.toString());
		} else {
			if (!getListaConsultaDepartamento().isEmpty()) {
				if (getListaConsultaDepartamento().get(0).isFiltrarDepartamento()) {
					setDepartamentoApresentar(getListaConsultaDepartamento().get(0).getNome());
					getRelatorioSEIDecidirVO().getDepartamentosVOs().add(getListaConsultaDepartamento().get(0));
				}
			} else {
				setDepartamentoApresentar(departamento.toString());
			}
		}
	}

	public void marcarTodasCategoriaProdutoAction() {
		for (CategoriaProdutoVO categoriaProduto : getListaConsultaCategoriaProduto()) {
			categoriaProduto.setFiltrarCategoriaProduto(getMarcarTodasCategoriaProduto());
		}
		verificarTodasCategoriaProdutoSelecionados();
	}

	public void verificarTodasCategoriaProdutoSelecionados(){
		StringBuilder categoriaProduto = new StringBuilder();
		getRelatorioSEIDecidirVO().setCategoriasProdutoVOs(new ArrayList<>());
		if (getListaConsultaCategoriaProduto().size() > 1) {
			for (CategoriaProdutoVO obj : getListaConsultaCategoriaProduto()) {
				if (obj.isFiltrarCategoriaProduto()) {
					categoriaProduto.append(obj.getCodigo()).append(" - ");
					categoriaProduto.append(obj.getNome()).append("; ");
					getRelatorioSEIDecidirVO().getCategoriasProdutoVOs().add(obj);
				}
			}
			setCategoriaProdutoApresentar(categoriaProduto.toString());
		} else {
			if (!getListaConsultaCategoriaProduto().isEmpty()) {
				if (getListaConsultaCategoriaProduto().get(0).isFiltrarCategoriaProduto()) {
					setCategoriaProdutoApresentar(getListaConsultaCategoriaProduto().get(0).getNome());
					getRelatorioSEIDecidirVO().getCategoriasProdutoVOs().add(getListaConsultaCategoriaProduto().get(0));
				}
			} else {
				setCategoriaProdutoApresentar(categoriaProduto.toString());
			}
		}
	}
	
	
	public void limparDadosPlanoOrcamentario() {
		getRelatorioSEIDecidirVO().setPlanosOrcamentarioVOs(new ArrayList<>());
		setPlanoOrcamentarioApresentar("");
		limparCamposPlanoOrcamentario();
	}

	public void limparDadosCategoriaDespesa() {
		getRelatorioSEIDecidirVO().setCategoriasDespesaVOs(new ArrayList<>());
		getRelatorioSEIDecidirVO().setCategoriaDespesaApresentar("");
	}

	public void limparDadosCategoriaProduto() {
		getRelatorioSEIDecidirVO().setCategoriasProdutoVOs(new ArrayList<>());
		setCategoriaProdutoApresentar("");
	}

	public void limparDadosDepartamento() {
		getRelatorioSEIDecidirVO().setDepartamentosVOs(new ArrayList<>());
		setDepartamentoApresentar("");
	}

	public void limparDadosDepartamento2() {
		getRelatorioSEIDecidirVO().setDepartamentosVOs(new ArrayList<>());
		setDepartamentoApresentar("");
	}
	
	public boolean getApresentarResultadoConsultaDepartamento() {
		return getListaConsultaDepartamento().size() > 0;
	}
	
	public void scrollerListenerPlanoOrcamentario(DataScrollEvent dataScrollerEvent) {
		getDataModeloCargo().setPaginaAtual(dataScrollerEvent.getPage());
		getDataModeloCargo().setPage(dataScrollerEvent.getPage());
		this.consultarPlanoOrcamentario();
	}
	
	public List<SelectItem> getTipoConsultaComboCategoriaDespesa() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("identificador", "Identificador"));
		return itens;
	}
	
	public void limparCentroResultadoSuperior() {
		getRelatorioSEIDecidirVO().setCentrosResultadoVOs(new ArrayList<>());
		getRelatorioSEIDecidirVO().setCentroResultadoApresentar("");
	}
	
	public void atualizarLayout() {
		montarListaSelectItemLayoutRelatorioSeiDecidir();
	}
	
	public void inicializarListasSelectItemTodosComboBox() throws Exception {
		montarListaSelectItemContaCorrente();
	}

	public void carregarDadosLayoutRelatorio() throws Exception {
		setListaSelectItemTipoFiltroPeriodo(null);
		getRelatorioSEIDecidirVO().setTipoFiltroPeriodo(null);
		getRelatorioSEIDecidirVO().setMotivoProgressaoSalarial("");
		getRelatorioSEIDecidirVO().setSituacaoPeriodoAquisitivo("");
		getRelatorioSEIDecidirVO().getMatriculaVO().setMatricula("");
		if (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getCodigo() > 0) {
			try {
				getRelatorioSEIDecidirVO().setLayoutRelatorioSEIDecidirVO(getFacadeFactory().getLayoutRelatorioSEIDecidirInterfaceFacade().consultarPorChavePrimaria(getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
				getListaSelectItemTipoFiltroPeriodo();
				if(getRelatorioSEIDecidirVO().getCentroReceitaVOs().isEmpty()) {
					consultarCentroReceitaFiltroRelatorio();
				}
				if(getRelatorioSEIDecidirVO().getCategoriaDespesaVOs().isEmpty()) {
					consultarCategoriaDespesaFiltroRelatorio();	
				}
				if(getListaSelectItemContaCorrente().isEmpty()) {
					montarListaSelectItemContaCorrente();
				}
				montarListaSelectItemNivelCentroResultado();
				preencherTipoFiltroPeriodo();				

				Map<String, String> camposPadroes = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[]{"tipoFiltroPeriodo", "considerarUnidadeEnsinoFinanceira", "dataInicio", "dataFim", "dataBase", "dataCompetencia", "dataDocumentoFim","dataDocumentoInicio", "dataFim2", "dataFimPeriodo", "dataFimPeriodoAutorizacaoRequisicao", "dataFimPeriodoEntregaRequisicao", "dataInicio2", "dataInicioPeriodo", "dataInicioPeriodoAutorizacaoRequisicao", "dataInicioPeriodoEntregaRequisicao"}, getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getDescricao());
				camposPadroes.entrySet().stream().forEach(p->{
					if(p.getKey().equals("tipoFiltroPeriodo") && Uteis.isAtributoPreenchido(p.getValue())){
						getRelatorioSEIDecidirVO().setTipoFiltroPeriodo(TipoFiltroPeriodoSeiDecidirEnum.valueOf(p.getValue()));
					}else if(getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosReceita() && p.getKey().equals("considerarUnidadeEnsinoFinanceira") && Uteis.isAtributoPreenchido(p.getValue())){
						getRelatorioSEIDecidirVO().setConsiderarUnidadeEnsinoFinanceira(Boolean.parseBoolean(p.getValue()));
					}else if(p.getKey().startsWith("data") && Uteis.isAtributoPreenchido(p.getValue())){
						try {
							UtilReflexao.invocarMetodoSet1Parametro(getRelatorioSEIDecidirVO(), p.getKey(), Date.class, UteisData.getConverterStringParaDataToString(p.getValue()));
						} catch (ParseException e) {							
							e.printStackTrace();
					}
					}

				});
				
				if(getPermiteGerarFormatoExportacaoDados()) {
					camposPadroes = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[]{"gerarFormatoExportacaoDados", "extensaoArquivo", "campoSeparador"}, getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getDescricao());
					if(camposPadroes.get("gerarFormatoExportacaoDados") != null){
						setGerarFormatoExportacaoDados(Boolean.valueOf(camposPadroes.get("gerarFormatoExportacaoDados")));
					}
					if(camposPadroes.get("extensaoArquivo") != null){
						setExtensaoArquivo(camposPadroes.get("extensaoArquivo"));
					}
					if(camposPadroes.get("campoSeparador") != null){
						setCampoSeparador(camposPadroes.get("campoSeparador"));
					}
				}
				if (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.RECURSOS_HUMANOS)) {
					carregarDadosPeriodoAquisitivo();
					carregarMotivoProgressaoSalarial();
				}
				
				montarListaSelectItemFiltroCustomizavel();
				montarListaSelectItemCombobox();
				setMensagemID("msg_entre_prmrelatorio");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			getRelatorioSEIDecidirVO().setLayoutRelatorioSEIDecidirVO(null);
		}
	}

	
	private void preencherTipoFiltroPeriodo() {
		if (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA) || getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA)) {
			getRelatorioSEIDecidirVO().setTipoFiltroPeriodo(TipoFiltroPeriodoSeiDecidirEnum.DATA_VENCIMENTO);
			getRelatorioSEIDecidirVO().setConsiderarUnidadeEnsinoFinanceira(true);
		}else if (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.ACADEMICO)) {
			getRelatorioSEIDecidirVO().setTipoFiltroPeriodo(TipoFiltroPeriodoSeiDecidirEnum.ANO_SEMESTRE);
		}else if (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.CRM) && getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.FOLLOW_UP)) {
			getRelatorioSEIDecidirVO().setTipoFiltroPeriodo(TipoFiltroPeriodoSeiDecidirEnum.PERIODO_FOLLOW_UP);
			getRelatorioSEIDecidirVO().setConsiderarUnidadeEnsinoFinanceira(true);
		}else if (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.CRM) && getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.COMPROMISSO)) {
			getRelatorioSEIDecidirVO().setTipoFiltroPeriodo(TipoFiltroPeriodoSeiDecidirEnum.PERIODO_COMPROMISSO);
			getRelatorioSEIDecidirVO().setConsiderarUnidadeEnsinoFinanceira(true);
		}else if (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.CRM) && getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.INTERACOES_WORKFLOW)) {
			getRelatorioSEIDecidirVO().setTipoFiltroPeriodo(TipoFiltroPeriodoSeiDecidirEnum.PERIODO_INTERACAO_WORKFLOW);
			getRelatorioSEIDecidirVO().setConsiderarUnidadeEnsinoFinanceira(true);
		}else if (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.CRM) && getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.PRE_INSCRICAO)) {
			getRelatorioSEIDecidirVO().setTipoFiltroPeriodo(TipoFiltroPeriodoSeiDecidirEnum.PERIODO_PRE_INSCRICAO);
			getRelatorioSEIDecidirVO().setConsiderarUnidadeEnsinoFinanceira(true);
		}else if (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.CRM) && getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.MATRICULA)) {
			getRelatorioSEIDecidirVO().setTipoFiltroPeriodo(TipoFiltroPeriodoSeiDecidirEnum.DATA_MATRICULA_E_ANO_SEMESTRE);
			getRelatorioSEIDecidirVO().setConsiderarUnidadeEnsinoFinanceira(true);
		}else if (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_CENTRO_RESULTADO)) {
			getRelatorioSEIDecidirVO().setTipoFiltroPeriodo(TipoFiltroPeriodoSeiDecidirEnum.PERIODO_DATA_MOVIMENTACAO_CENTRO_ORIGEM);
		}
	}

	public void carregarDadosPeriodoAquisitivo() {
		try {
			listaSelectItemSituacaoPeriodoAquisitivo = montarComboboxSemOpcaoDeNenhum(SituacaoPeriodoAquisitivoEnum.values(), Obrigatorio.NAO);
		
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void carregarMotivoProgressaoSalarial() {
		try {
			listaSelectItemMotivoProgressaoSalarial = montarComboboxSemOpcaoDeNenhum(MotivoMudancaCargoEnum.values(), Obrigatorio.NAO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public Boolean apresentarEventoFolha() {
		RelatorioSEIDecidirNivelDetalhamentoEnum nivelDetalhamento = getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento();

		return nivelDetalhamento.equals(RelatorioSEIDecidirNivelDetalhamentoEnum.EVENTO_FOLHA_PAGAMENTO) ? Boolean.TRUE : Boolean.FALSE; 
	}

	public Boolean apresentarPeriodoAquisitivo() {
		RelatorioSEIDecidirNivelDetalhamentoEnum nivelDetalhamento = getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento();
		return nivelDetalhamento.equals(RelatorioSEIDecidirNivelDetalhamentoEnum.PERIODO_AQUISITIVO) ? Boolean.TRUE : Boolean.FALSE; 
	}

	public Boolean apresentarMotivoProgressao() {
		RelatorioSEIDecidirNivelDetalhamentoEnum nivelDetalhamento = getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento();
		
		return nivelDetalhamento.equals(RelatorioSEIDecidirNivelDetalhamentoEnum.PROGRESSAO_SALARIAL) ? Boolean.TRUE : Boolean.FALSE; 
	}

	public void realizarGeracaoRelatorioHtml() {
		realizarGeracaoRelatorio(TipoRelatorioEnum.HTML);
	}

	public void realizarGeracaoRelatorioExcel() {
		realizarGeracaoRelatorio(TipoRelatorioEnum.EXCEL);
	}
	
	public void realizarGeracaoRelatorioPdf() {
		realizarGeracaoRelatorio(TipoRelatorioEnum.PDF);
	}

	
	
	public void realizarGeracaoRelatorio(TipoRelatorioEnum tipoRelatorio) {
		try {
			setFazerDownload(false);
			setCaminhoRelatorio("");
			getRelatorioSEIDecidirVO().setFiltro(new StringBuilder());			
			String caminhoRelatorio = getFacadeFactory().getRelatorioSeiDecidirFacade().realizarGeracaoRelatorioSeiDecidir(getRelatorioSEIDecidirVO(), tipoRelatorio, getProgressBarVO().getUrlLogoUnidadeEnsino(), getGerarFormatoExportacaoDados(), getCampoSeparador(), getExtensaoArquivo(), true, getProgressBarVO().getUsuarioVO(), getProgressBarVO().getConfiguracaoGeralSistemaVO(), getProgressBarVO());
			setCaminhoRelatorio(caminhoRelatorio);
			setFazerDownload(true);
			getFacadeFactory().getLayoutPadraoFacade().persistirFiltroSituacaoAcademica(getRelatorioSEIDecidirVO().getRelatorioAcademicoVO(), getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getDescricao(), getProgressBarVO().getUsuarioVO());
			getFacadeFactory().getLayoutPadraoFacade().persistirFiltroTipoOrigemContaReceber(getRelatorioSEIDecidirVO().getRelatorioFinanceiroVO(), getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getDescricao(), getProgressBarVO().getUsuarioVO());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().name(), getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getDescricao(), "tipoFiltroPeriodo", getProgressBarVO().getUsuarioVO());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getRelatorioSEIDecidirVO().getDataInicio() == null ? "" : Uteis.getData(getRelatorioSEIDecidirVO().getDataInicio(), "dd/MM/yyyy"), getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getDescricao(), "dataInicio", getProgressBarVO().getUsuarioVO());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getRelatorioSEIDecidirVO().getDataFim() == null ? "" : Uteis.getData(getRelatorioSEIDecidirVO().getDataFim(), "dd/MM/yyyy"), getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getDescricao(), "dataFim", getProgressBarVO().getUsuarioVO());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getRelatorioSEIDecidirVO().getDataBase() == null ? "" : Uteis.getData(getRelatorioSEIDecidirVO().getDataBase(), "dd/MM/yyyy"), getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getDescricao(), "dataBase", getProgressBarVO().getUsuarioVO());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getRelatorioSEIDecidirVO().getDataCompetencia() == null ? "" : Uteis.getData(getRelatorioSEIDecidirVO().getDataCompetencia(), "dd/MM/yyyy"), getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getDescricao(), "dataCompetencia", getProgressBarVO().getUsuarioVO());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getRelatorioSEIDecidirVO().getDataDocumentoFim() == null ? "" : Uteis.getData(getRelatorioSEIDecidirVO().getDataDocumentoFim(), "dd/MM/yyyy"), getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getDescricao(), "dataDocumentoFim",getProgressBarVO().getUsuarioVO());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getRelatorioSEIDecidirVO().getDataDocumentoInicio() == null ? "" : Uteis.getData(getRelatorioSEIDecidirVO().getDataDocumentoInicio(), "dd/MM/yyyy"), getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getDescricao(), "dataDocumentoInicio", getProgressBarVO().getUsuarioVO());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getRelatorioSEIDecidirVO().getDataFim2() == null ? "" : Uteis.getData(getRelatorioSEIDecidirVO().getDataFim2(), "dd/MM/yyyy"), getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getDescricao(), "dataFim2", getProgressBarVO().getUsuarioVO());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getRelatorioSEIDecidirVO().getDataFimPeriodo() == null ? "" : Uteis.getData(getRelatorioSEIDecidirVO().getDataFimPeriodo(), "dd/MM/yyyy"), getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getDescricao(), "dataFimPeriodo", getProgressBarVO().getUsuarioVO());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getRelatorioSEIDecidirVO().getDataFimPeriodoAutorizacaoRequisicao() == null ? "" : Uteis.getData(getRelatorioSEIDecidirVO().getDataFimPeriodoAutorizacaoRequisicao(), "dd/MM/yyyy"), getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getDescricao(), "dataFimPeriodoAutorizacaoRequisicao", getProgressBarVO().getUsuarioVO());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getRelatorioSEIDecidirVO().getDataFimPeriodoEntregaRequisicao() == null ? "" : Uteis.getData(getRelatorioSEIDecidirVO().getDataFimPeriodoEntregaRequisicao(), "dd/MM/yyyy"), getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getDescricao(), "dataFimPeriodoEntregaRequisicao", getProgressBarVO().getUsuarioVO());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getRelatorioSEIDecidirVO().getDataInicio2() == null ? "" : Uteis.getData(getRelatorioSEIDecidirVO().getDataInicio2(), "dd/MM/yyyy"), getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getDescricao(), "dataInicio2", getProgressBarVO().getUsuarioVO());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getRelatorioSEIDecidirVO().getDataInicioPeriodo() == null ? "" : Uteis.getData(getRelatorioSEIDecidirVO().getDataInicioPeriodo(), "dd/MM/yyyy"), getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getDescricao(), "dataInicioPeriodo", getProgressBarVO().getUsuarioVO());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getRelatorioSEIDecidirVO().getDataInicioPeriodoAutorizacaoRequisicao() == null ? "" : Uteis.getData(getRelatorioSEIDecidirVO().getDataInicioPeriodoAutorizacaoRequisicao(), "dd/MM/yyyy"), getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getDescricao(), "dataInicioPeriodoAutorizacaoRequisicao", getProgressBarVO().getUsuarioVO());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getRelatorioSEIDecidirVO().getDataInicioPeriodoEntregaRequisicao() == null ? "" : Uteis.getData(getRelatorioSEIDecidirVO().getDataInicioPeriodoEntregaRequisicao(), "dd/MM/yyyy"), getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getDescricao(), "dataInicioPeriodoEntregaRequisicao", getProgressBarVO().getUsuarioVO());
			if(getProgressBarVO().getParams().get("PermiteGerarFormatoExportacaoDados") != null && getProgressBarVO().getParams().get("PermiteGerarFormatoExportacaoDados")  instanceof Boolean
					&& (Boolean)getProgressBarVO().getParams().get("PermiteGerarFormatoExportacaoDados")  
				) {
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getGerarFormatoExportacaoDados().toString(), getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getDescricao(), "gerarFormatoExportacaoDados", getProgressBarVO().getUsuarioVO());
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getExtensaoArquivo(), getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getDescricao(), "extensaoArquivo", getProgressBarVO().getUsuarioVO());
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getCampoSeparador(), getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getDescricao(), "campoSeparador", getProgressBarVO().getUsuarioVO());
			}
			
			if(getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosReceita()){
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(String.valueOf(getRelatorioSEIDecidirVO().isConsiderarUnidadeEnsinoFinanceira()), getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getDescricao(), "considerarUnidadeEnsinoFinanceira", getProgressBarVO().getUsuarioVO());
			}
			getProgressBarVO().setProgresso(getProgressBarVO().getMaxValue().longValue());
			limparMensagem();
		} catch (Exception e) {
			getProgressBarVO().setForcarEncerramento(true);
			
			e.printStackTrace();
			getProgressBarVO().getSuperControle().setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}finally {
			getProgressBarVO().encerrar();
		}
	}
	


	public String getIdEntidade(){
		if(getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA)){
			return "RelatorioSEIDecidirReceita";
		}else if(getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA)){
			return "RelatorioSEIDecidirDespesa";			
		}		
		return "RelatorioSEIDecidirAcademico";		
	}

	/**
	 * Apresenta os campos dos filtros para o Nivel de detalhamento 'ATIVIDADE_EXTRA_CLASSE_PROFESSOR' 
	 */
	public boolean apresentarAtividadeExtraClasse() {
		return getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.ATIVIDADE_EXTRA_CLASSE_PROFESSOR);
	}

	public void consultarUnidadeEnsino() {
		try {			
			consultarUnidadeEnsinoFiltroRelatorio(getIdEntidade());
			getRelatorioSEIDecidirVO().setUnidadeEnsinoVOs(getUnidadeEnsinoVOs());
			verificarTodasUnidadesSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemContaCorrente() {
		try {
			getListaSelectItemContaCorrente().clear();
			if (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosReceita()) {
				List<ContaCorrenteVO> contaCorrenteVOs = getFacadeFactory().getContaCorrenteFacade().consultarPorNumero("", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());				
				getListaSelectItemContaCorrente().add(new SelectItem(0, ""));
				for(ContaCorrenteVO obj: contaCorrenteVOs) {										
					getListaSelectItemContaCorrente().add(new SelectItem(obj.getCodigo(), obj.getDescricaoParaComboBox()));					
				}				
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultar(getCampoConsultaTurma(), getValorConsultaTurma(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsultaTurma().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarTurmaEstudouDisciplina() {
		try {
			setListaConsultaTurmaEstudouDisciplinaVOs(getFacadeFactory().getTurmaFacade().consultar(getCampoConsultaTurmaEstudouDisciplina(), getValorConsultaTurmaEstudouDisciplina(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsultaTurma().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAluno() {
		List<MatriculaVO> objs = null;
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
			if (getCampoConsultaAluno().equals("registroAcademico")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
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

	public void consultarFuncionario() {
		List<FuncionarioVO> objs = null;
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
	
	/**
	 * Consulta responsavel por retornar os usuarios do popup de pesquisa 
	 * do funcionario
	 */
	public void consultarFuncionarioCargo() {

		try {
			getControleConsultaOtimizado().setLimitePorPagina(10);
			List<FuncionarioCargoVO> objs = new ArrayList<>(0);

			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				setListaConsulta(new ArrayList<FuncionarioCargoVO>(0));
				return;
			}
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioCargoFacade().consultarPorNomeFuncionario(getControleConsultaOtimizado(), getValorConsultaFuncionario(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getFuncionarioCargoFacade().consultarTotalPorNomeFuncionarioAtivo(getControleConsultaOtimizado(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioCargoFacade().consultarPorMatriculaCargo(getValorConsultaFuncionario(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setFuncionarioCargoVOs(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Consulta os cargos paginado por 10 registros.
	 */
	public void consultarCargos() {
		try {
			getDataModeloCargo().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getCargoFacade().consultarPorEnumCampoConsulta(getDataModeloCargo());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getDataModeloCargo().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCandidato() {
		List<PessoaVO> objs = null;
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

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * relatorioSEIDecidirRel.jsp. Define o tipo de consulta a ser executada,
	 * por meio de ComboBox denominado campoConsulta, disponivel neste mesmo
	 * JSP. Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 */
	public void consultarParceiro() {
		List<ParceiroVO> objs = null;
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
			getListaConsultaParceiro().clear();
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

	public void consultarFornecedor() {
		try {
			super.consultar();
			List<FornecedorVO> objs = null;
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
			getListaConsultaFornecedor().clear();
			;
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarCursoFiltroRelatorio(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
		try {
			if (unidadeEnsinoVOs.isEmpty()) {
				getRelatorioSEIDecidirVO().getCursoVOs().clear();
				verificarTodosCursosSelecionados();
				return;
			}
			List<CursoVO> cursoVOs = getFacadeFactory().getCursoFacade().consultarCursoPorNomePeriodicidadeEUnidadeEnsinoVOs("", "", null, unidadeEnsinoVOs, getUsuarioLogado());
			if (getRelatorioSEIDecidirVO().getCursoVOs().isEmpty()) {
				getRelatorioSEIDecidirVO().setCursoVOs(cursoVOs);
			} else {
				for (CursoVO obj : cursoVOs) {
					if (!getRelatorioSEIDecidirVO().getCursoVOs().contains(obj)) {
						getRelatorioSEIDecidirVO().getCursoVOs().add(obj);
					}
				}
				for (Iterator<CursoVO> iterator = getRelatorioSEIDecidirVO().getCursoVOs().iterator(); iterator.hasNext();) {
					CursoVO cursoVO = (CursoVO) iterator.next();
					if (!cursoVOs.contains(cursoVO)) {
						iterator.remove();
					}
				}
			}
			verificarTodosCursosSelecionados();
		} catch (Exception e) {
			getRelatorioSEIDecidirVO().getCursoVOs().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarGradeCurricularEstagioFiltroRelatorio() {
		try {
			getRelatorioSEIDecidirVO().setGradeCurricularEstagioVOs(getFacadeFactory().getGradeCurricularEstagioFacade().consultarGradeCurricularEstagioRelatorioSeiDecidir(getUsuarioLogadoClone()));
			verificarTodosGradeCurricularEstagiosSelecionados();
		} catch (Exception e) {
			getRelatorioSEIDecidirVO().getCursoVOs().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurnoFiltroRelatorio() {
		try {
			getRelatorioSEIDecidirVO().setTurnoVOs(getFacadeFactory().getTurnoFacade().consultarTurnoUsadoMatricula(null, Uteis.NIVELMONTARDADOS_COMBOBOX));
		} catch (Exception e) {
			getRelatorioSEIDecidirVO().getTurnoVOs().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarCentroReceitaFiltroRelatorio() {
		try {
			if (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosCentroReceita()) {
				getRelatorioSEIDecidirVO().setCentroReceitaVOs(getFacadeFactory().getCentroReceitaFacade().consultarCentroReceitaVinculadoContaReceber(null, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarCategoriaDespesaFiltroRelatorio() {
		try {
			if (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosCategoriaDespesa()) {
				getRelatorioSEIDecidirVO().setCategoriaDespesaVOs(getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricao("", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarTodosCentroReceitaSelecionados() {
		StringBuilder centroReceita = new StringBuilder();
		if (getRelatorioSEIDecidirVO().getCentroReceitaVOs().size() > 1) {
			for (CentroReceitaVO obj : getRelatorioSEIDecidirVO().getCentroReceitaVOs()) {
				if (obj.getFiltrarCentroReceitaVO()) {
					centroReceita.append(obj.getDescricao().trim()).append("; ");
				}
			}
			getRelatorioSEIDecidirVO().setCentroReceitaApresentar(centroReceita.toString());
		} else {
			if (!getRelatorioSEIDecidirVO().getCentroReceitaVOs().isEmpty()) {
				if (getRelatorioSEIDecidirVO().getCentroReceitaVOs().get(0).getFiltrarCentroReceitaVO()) {
					getRelatorioSEIDecidirVO().setCentroReceitaApresentar(getRelatorioSEIDecidirVO().getCentroReceitaVOs().get(0).getDescricao().trim());
				}
			} else {
				getRelatorioSEIDecidirVO().setCentroReceitaApresentar(centroReceita.toString());
			}
		}
	}

	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<UnidadeEnsinoVO>(0);
		if (getRelatorioSEIDecidirVO().getUnidadeEnsinoVOs().size() > 1) {
			for (UnidadeEnsinoVO obj : getRelatorioSEIDecidirVO().getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getNome().trim()).append("; ");
					unidadeEnsinoVOs.add(obj);
				}
			}
			consultarCursoFiltroRelatorio(unidadeEnsinoVOs);
			getRelatorioSEIDecidirVO().setUnidadeEnsinoApresentar(unidade.toString());
		} else {
			if (!getRelatorioSEIDecidirVO().getUnidadeEnsinoVOs().isEmpty()) {
				if (getRelatorioSEIDecidirVO().getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					getRelatorioSEIDecidirVO().setUnidadeEnsinoApresentar(getRelatorioSEIDecidirVO().getUnidadeEnsinoVOs().get(0).getNome());
					unidadeEnsinoVOs.add(getRelatorioSEIDecidirVO().getUnidadeEnsinoVOs().get(0));
				} else {
					getRelatorioSEIDecidirVO().setUnidadeEnsinoApresentar(unidade.toString());					
				}
				consultarCursoFiltroRelatorio(unidadeEnsinoVOs);
			} else {
				getRelatorioSEIDecidirVO().setUnidadeEnsinoApresentar(unidade.toString());
			}
		}
	}

	public void verificarTodasCategoriaDespesaSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		for (CategoriaDespesaVO obj : getRelatorioSEIDecidirVO().getCategoriaDespesaVOs()) {
			if (obj.getSelecionar()) {
				unidade.append(obj.getIdentificadorCategoriaDespesa().trim() + " - " + obj.getDescricao()).append("; ");
			}
		}
		getRelatorioSEIDecidirVO().setCategoriaDespesaApresentar(unidade.toString());
	}

	public void verificarTodosCursosSelecionados(){
		StringBuilder curso = new StringBuilder();
		if (getRelatorioSEIDecidirVO().getCursoVOs().size() > 1) {
			for (CursoVO obj : getRelatorioSEIDecidirVO().getCursoVOs()) {
				if (obj.getFiltrarCursoVO()) {
					curso.append(obj.getCodigo()).append(" - ");
					curso.append(obj.getNome()).append("; ");
				}
			}
			getRelatorioSEIDecidirVO().setCursoApresentar(curso.toString());
		} else {
			if (!getRelatorioSEIDecidirVO().getCursoVOs().isEmpty()) {
				if (getRelatorioSEIDecidirVO().getCursoVOs().get(0).getFiltrarCursoVO()) {
					getRelatorioSEIDecidirVO().setCursoApresentar(getRelatorioSEIDecidirVO().getCursoVOs().get(0).getNome());
				}
			} else {
				getRelatorioSEIDecidirVO().setCursoApresentar(curso.toString());
			}
		}
	}
	
	public void verificarTodosGradeCurricularEstagiosSelecionados(){
		StringBuilder gradeCurricularEstagio = new StringBuilder();
	    StringJoiner codigosgradeCurricularEstagio = new StringJoiner(", ");
		if (getRelatorioSEIDecidirVO().getGradeCurricularEstagioVOs().size() > 1) {
			for (GradeCurricularEstagioVO obj : getRelatorioSEIDecidirVO().getGradeCurricularEstagioVOs()) {
				if (obj.getFiltrarGradeCurricularEstagioVO()) {
					gradeCurricularEstagio.append(obj.getCodigo()).append(" - ");
					gradeCurricularEstagio.append(obj.getNome()).append("; ");
					codigosgradeCurricularEstagio.add(String.valueOf(obj.getCodigo()));
				}
			}
			getRelatorioSEIDecidirVO().setGradeCurricularEstagioApresentar(gradeCurricularEstagio.toString());
	        getRelatorioSEIDecidirVO().setCodigosGradeCurricularEstagio(codigosgradeCurricularEstagio.toString());
			
		} else {
			if (!getRelatorioSEIDecidirVO().getGradeCurricularEstagioVOs().isEmpty()) {
				if (getRelatorioSEIDecidirVO().getGradeCurricularEstagioVOs().get(0).getFiltrarGradeCurricularEstagioVO()) {
					getRelatorioSEIDecidirVO().setGradeCurricularEstagioApresentar(getRelatorioSEIDecidirVO().getGradeCurricularEstagioVOs().get(0).getNome());
					getRelatorioSEIDecidirVO().setCodigosGradeCurricularEstagio(getRelatorioSEIDecidirVO().getGradeCurricularEstagioVOs().get(0).getCodigo().toString());
				}
			} else {
				getRelatorioSEIDecidirVO().setGradeCurricularEstagioApresentar(gradeCurricularEstagio.toString());
			}
		}
	}

	public void verificarTodosTurnosSelecionados() {
		StringBuilder turno = new StringBuilder();
		if (getRelatorioSEIDecidirVO().getTurnoVOs().size() > 1) {
			for (TurnoVO obj : getRelatorioSEIDecidirVO().getTurnoVOs()) {
				if (obj.getFiltrarTurnoVO()) {
					turno.append(obj.getNome()).append("; ");
				}
			}
			getRelatorioSEIDecidirVO().setTurnoApresentar(turno.toString());
		} else {
			if (!getRelatorioSEIDecidirVO().getTurnoVOs().isEmpty()) {
				if (getRelatorioSEIDecidirVO().getTurnoVOs().get(0).getFiltrarTurnoVO()) {
					getRelatorioSEIDecidirVO().setTurnoApresentar(getRelatorioSEIDecidirVO().getTurnoVOs().get(0).getNome());
				}
			} else {
				getRelatorioSEIDecidirVO().setTurnoApresentar(turno.toString());
			}
		}
	}

	public void marcarTodasCategoriaDespesaAction() {
		for (CategoriaDespesaVO categoriaDespesaVO : getRelatorioSEIDecidirVO().getCategoriaDespesaVOs()) {
			categoriaDespesaVO.setSelecionar(getMarcarTodosCategoriaDespesa());
		}
		verificarTodasCategoriaDespesaSelecionadas();
	}

	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getRelatorioSEIDecidirVO().getUnidadeEnsinoVOs()) {
			unidade.setFiltrarUnidadeEnsino(getMarcarTodasUnidadeEnsino());
		}
		verificarTodasUnidadesSelecionadas();
	}

	public void marcarTodosCursosAction() throws Exception {
		for (CursoVO cursoVO : getRelatorioSEIDecidirVO().getCursoVOs()) {
			cursoVO.setFiltrarCursoVO(getMarcarTodosCursos());
		}
		verificarTodosCursosSelecionados();
	}
	
	public void marcarTodosGradeCurricularEstagioAction() throws Exception {
		for (GradeCurricularEstagioVO gradeCurricularEstagioVO : getRelatorioSEIDecidirVO().getGradeCurricularEstagioVOs()) {
			gradeCurricularEstagioVO.setFiltrarGradeCurricularEstagioVO(getMarcarTodosGradeCurricularEstagios());
		}
		verificarTodosGradeCurricularEstagiosSelecionados();
	}

	public void marcarTodosTurnosAction() throws Exception {
		for (TurnoVO turnoVO : getRelatorioSEIDecidirVO().getTurnoVOs()) {
			turnoVO.setFiltrarTurnoVO(getMarcarTodosTurnos());
		}
		verificarTodosTurnosSelecionados();
	}

	public void marcarTodosCentroReceitasAction() {
		for (CentroReceitaVO centroReceitaVO : getRelatorioSEIDecidirVO().getCentroReceitaVOs()) {
			centroReceitaVO.setFiltrarCentroReceitaVO(getMarcarTodosCentroReceitas());
		}
		verificarTodosCentroReceitaSelecionados();
	}
	
	public void marcarTodosCentroResultadoAction() throws Exception {
		for (CentroResultadoVO centroResultadoVO : getRelatorioSEIDecidirVO().getListaCentroResultadoVO()) {
			centroResultadoVO.setFiltrarCentroResultado(getMarcarTodosCentroResultado());
		}
		verificarTodosCentroResultadoSelecionados();
	}
	
	public void verificarTodosCentroResultadoSelecionados(){
		StringBuilder centroResultado = new StringBuilder();
		if (getRelatorioSEIDecidirVO().getListaCentroResultadoVO().size() > 1) {
			for (CentroResultadoVO obj : getRelatorioSEIDecidirVO().getListaCentroResultadoVO()) {
				if (obj.getFiltrarCentroResultado()) {
					centroResultado.append(obj.getCodigo()).append(" - ");
					centroResultado.append(obj.getDescricao()).append("; ");
				}
			}
			getRelatorioSEIDecidirVO().setCentroResultadoApresentar(centroResultado.toString());
		} else {
			if (!getRelatorioSEIDecidirVO().getListaCentroResultadoVO().isEmpty()) {
				if (getRelatorioSEIDecidirVO().getListaCentroResultadoVO().get(0).getFiltrarCentroResultado()) {
					getRelatorioSEIDecidirVO().setCentroResultadoApresentar(getRelatorioSEIDecidirVO().getListaCentroResultadoVO().get(0).getDescricao());
				}
			} else {
				getRelatorioSEIDecidirVO().setCentroResultadoApresentar(centroResultado.toString());
			}
		}
	}
	
	public void consultarCentroResultado() {
		try {
			if (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosCentroResultado()
					&& !Uteis.isAtributoPreenchido(getRelatorioSEIDecidirVO().getListaCentroResultadoVO())) {
				getRelatorioSEIDecidirVO().setListaCentroResultadoVO(getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorArvoreCompleta(null, SituacaoEnum.ATIVO, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogadoClone()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparCentroResultado()  {
		try {
			getRelatorioSEIDecidirVO().setCentroResultadoApresentar(null);
			getRelatorioSEIDecidirVO().getListaCentroResultadoVO().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	
	public void consultarCampanhaFiltroRelatorio() {
		try {
			getRelatorioSEIDecidirVO().setListaCampanhaVOs(getFacadeFactory().getCampanhaFacade().consultarPorListaUnidadeEnsinoPorListaCurso(getRelatorioSEIDecidirVO().getUnidadeEnsinoVOs().stream().filter(p-> p.getFiltrarUnidadeEnsino()).collect(Collectors.toList()), getRelatorioSEIDecidirVO().getCursoVOs().stream().filter(p-> p.getFiltrarCursoVO()).collect(Collectors.toList()), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarFuncionarioFiltroRelatorio() {
		try {
			getRelatorioSEIDecidirVO().setListaFuncionarioVOs(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getRelatorioSEIDecidirVO().getUnidadeEnsinoVOs().stream().filter(p-> p.getFiltrarUnidadeEnsino()).collect(Collectors.toList()),false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void marcarTodasCampanhaAction()  {
		try {
			getRelatorioSEIDecidirVO().getListaCampanhaVOs().stream().forEach(p-> p.setFiltrarCampanhaVO(getMarcarTodasCampanhas()));
			verificarTodasCampanhaSelecionados();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void marcarTodosFuncionarioAction() throws Exception {
		try {
			getRelatorioSEIDecidirVO().getListaFuncionarioVOs().stream().forEach(p-> p.setFiltrarFuncionarioVO(getMarcarTodosFuncionarios()));
			verificarTodosFuncionarioSelecionados();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	
	public void verificarTodasCampanhaSelecionados(){
		getRelatorioSEIDecidirVO().setCampanhaApresentar("");
		StringBuilder listaCampanhas = new StringBuilder();
		getRelatorioSEIDecidirVO().getListaCampanhaVOs().stream().filter(p-> p.getFiltrarCampanhaVO()).forEach(p->{
			listaCampanhas.append(p.getCodigo()).append(" - ");
			listaCampanhas.append(p.getDescricao()).append("; ");
		});
		if (Uteis.isAtributoPreenchido(listaCampanhas)) {
			getRelatorioSEIDecidirVO().setCampanhaApresentar(listaCampanhas.toString());
		}
	}
	
	public void verificarTodosFuncionarioSelecionados(){
		getRelatorioSEIDecidirVO().setFuncionarioApresentar("");
		StringBuilder listaFuncionarios = new StringBuilder();
		getRelatorioSEIDecidirVO().getListaFuncionarioVOs().stream().filter(p-> p.getFiltrarFuncionarioVO()).forEach(p->{
			listaFuncionarios.append(p.getCodigo()).append(" - ");
			listaFuncionarios.append(p.getPessoa().getNome()).append("; ");
		});
		if (Uteis.isAtributoPreenchido(listaFuncionarios)) {
			getRelatorioSEIDecidirVO().setFuncionarioApresentar(listaFuncionarios.toString());
		}
	}	
	

	public void selecionarTurma() throws Exception {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			getRelatorioSEIDecidirVO().setTurmaVO(obj);
			obj = null;
			valorConsultaTurma = "";
			campoConsultaTurma = "";
			listaConsultaTurma.clear();
		} catch (Exception e) {
			getRelatorioSEIDecidirVO().getTurmaVO().setCodigo(0);
			getRelatorioSEIDecidirVO().getTurmaVO().setIdentificadorTurma("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarTurmaEstudouDisciplina() throws Exception {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			getRelatorioSEIDecidirVO().setTurmaEstudouDisciplinaVO(obj);
			obj = null;
			valorConsultaTurmaEstudouDisciplina = "";
			campoConsultaTurmaEstudouDisciplina = "";
			listaConsultaTurmaEstudouDisciplinaVOs.clear();
		} catch (Exception e) {
			getRelatorioSEIDecidirVO().getTurmaEstudouDisciplinaVO().setCodigo(0);
			getRelatorioSEIDecidirVO().getTurmaEstudouDisciplinaVO().setIdentificadorTurma("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarFornecedor() {
		try {
			FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
			this.getRelatorioSEIDecidirVO().getFornecedorVO().setCodigo(obj.getCodigo());
			this.getRelatorioSEIDecidirVO().getFornecedorVO().setNome(obj.getNome());
			this.setCampoDescritivo(obj.getCPF() + obj.getCNPJ());
			this.setValorDescritivo(obj.getNome());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarResponsavelFinanceiro() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("responsavelFinanceiroItens");
			getListaConsultaResponsavelFinanceiro().clear();
			setCampoDescritivo(obj.getCPF());
			setValorDescritivo(obj.getNome());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarTurma() throws Exception {
		try {
			if (!getRelatorioSEIDecidirVO().getTurmaVO().getIdentificadorTurma().equals("")) {
				getRelatorioSEIDecidirVO().setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getRelatorioSEIDecidirVO().getTurmaVO(), getRelatorioSEIDecidirVO().getTurmaVO().getIdentificadorTurma(), 0, 0, false, getUsuarioLogado()));
			} else {
				throw new Exception("Informe a Turma.");
			}
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			getRelatorioSEIDecidirVO().setTurmaVO(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void montarTurmaEstudouDisciplina() throws Exception {
		try {
			if (!getRelatorioSEIDecidirVO().getTurmaVO().getIdentificadorTurma().equals("")) {
				getRelatorioSEIDecidirVO().setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getRelatorioSEIDecidirVO().getTurmaVO(), getRelatorioSEIDecidirVO().getTurmaVO().getIdentificadorTurma(), 0, 0, false, getUsuarioLogado()));
			} else {
				throw new Exception("Informe a Turma.");
			}
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			getRelatorioSEIDecidirVO().setTurmaVO(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoItens");
		try {
			if(!getRelatorioSEIDecidirVO().getIsPermiteInformarAlunoJuntoOutroSacado()) {				
				setCampoDescritivo(obj.getMatricula());
				setValorDescritivo(obj.getAluno().getNome());
			}
			getRelatorioSEIDecidirVO().getPessoaVO().setCodigo(obj.getAluno().getCodigo());
			getRelatorioSEIDecidirVO().setMatriculaVO(obj);
			setCampoConsultaAluno("");
			setValorConsultaAluno("");
			getListaConsultaAluno().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			obj = null;
		}

	}

	public void selecionarFuncionario() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		try {
			setCampoDescritivo(obj.getMatricula());
			setValorDescritivo(obj.getPessoa().getNome());
			getRelatorioSEIDecidirVO().getFuncionarioVO().setCodigo(obj.getPessoa().getCodigo());
			getRelatorioSEIDecidirVO().getFuncionarioVO().getPessoa().setNome(obj.getPessoa().getNome());
			setCampoConsultaFuncionario("");
			setValorConsultaFuncionario("");
			getListaConsultaFuncionario().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			obj = null;
		}
	}

	public void selecionarCandidato() throws Exception {
		PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("candidatoItens");
		try {
			setCampoDescritivo(obj.getCPF());
			setValorDescritivo(obj.getNome());
			getRelatorioSEIDecidirVO().getCandidatosVagasVO().setCodigo(obj.getCodigo());
			setCampoConsultaCandidato("");
			setValorConsultaCandidato("");
			getListaConsultaCandidato().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
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
			getRelatorioSEIDecidirVO().getParceiroVO().setCodigo(obj.getCodigo());
			setValorConsultaCandidato("");
			setCampoConsultaCandidato("");
			getListaConsultaCandidato().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			obj = null;
		}
	}

	public RelatorioSEIDecidirVO getRelatorioSEIDecidirVO() {
		if (relatorioSEIDecidirVO == null) {
			relatorioSEIDecidirVO = new RelatorioSEIDecidirVO();
		}
		return relatorioSEIDecidirVO;
	}

	public void limparCurso()  {
		try {
			getRelatorioSEIDecidirVO().setCursoApresentar(null);
			setMarcarTodosCursos(false);
			marcarTodosCursosAction();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparGradeCurricularEstagio()  {
		try {
			getRelatorioSEIDecidirVO().setGradeCurricularEstagioApresentar(null);
			setMarcarTodosGradeCurricularEstagios(false);
			marcarTodosGradeCurricularEstagioAction();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparTurno()  {
		try {
			getRelatorioSEIDecidirVO().setTurnoApresentar("");
			setMarcarTodosTurnos(false);
			marcarTodosTurnosAction();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurma() {
		try {
			getRelatorioSEIDecidirVO().setTurmaVO(null);
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparTurmaEstudouDisciplina() {
		try {
			getRelatorioSEIDecidirVO().setTurmaEstudouDisciplinaVO(null);
			setListaConsultaTurmaEstudouDisciplinaVOs(new ArrayList<TurmaVO>(0));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCentroReceita() {
		getRelatorioSEIDecidirVO().setCentroReceitaApresentar("");
		setMarcarTodosCentroReceitas(false);
		marcarTodosCentroReceitasAction();
	}
	
	public void limparCategoriaDespesa() {
		getRelatorioSEIDecidirVO().setCategoriaDespesaApresentar("");
		setMarcarTodosCategoriaDespesa(false);
		marcarTodasCategoriaDespesaAction();
	}

	public void limparDadosPessoa() {
		if(!getRelatorioSEIDecidirVO().getIsPermiteInformarAlunoJuntoOutroSacado()) {
			getRelatorioSEIDecidirVO().setMatriculaVO(null);
			getRelatorioSEIDecidirVO().setPessoaVO(null);
		}
		getRelatorioSEIDecidirVO().setFuncionarioVO(null);
		getRelatorioSEIDecidirVO().setParceiroVO(null);
		getRelatorioSEIDecidirVO().setFornecedorVO(null);
		getRelatorioSEIDecidirVO().setCandidatosVagasVO(null);
		setCampoDescritivo("");
		setValorDescritivo("");
	}
	
	public void limparDadosCargo() {
		setDataModeloCargo(new DataModelo());
		getRelatorioSEIDecidirVO().setCargoVO(new CargoVO());
	}

	private List<SelectItem> tipoConsultaComboCurso;

	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboCurso;
	}

	private List<SelectItem> tipoConsultaComboTurma;

	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador Turma"));
			tipoConsultaComboTurma.add(new SelectItem("nomeCurso", "Nome Curso"));
			tipoConsultaComboTurma.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboTurma;
	}

	private List<SelectItem> tipoConsultaComboCentroReceita;

	public List<SelectItem> getTipoConsultaComboCentroReceita() {
		if (tipoConsultaComboCentroReceita == null) {
			tipoConsultaComboCentroReceita = new ArrayList<SelectItem>(0);
			tipoConsultaComboCentroReceita.add(new SelectItem("descricao", "Descrição"));
			tipoConsultaComboCentroReceita.add(new SelectItem("identificadorCentroReceita", "Identificador Centro Receita"));
			tipoConsultaComboCentroReceita.add(new SelectItem("nomeDepartamento", "Departamento"));
		}
		return tipoConsultaComboCentroReceita;
	}

	private List<SelectItem> tipoConsultaComboAluno;

	public List<SelectItem> getTipoConsultaComboAluno() {
		if (tipoConsultaComboAluno == null) {
			tipoConsultaComboAluno = new ArrayList<SelectItem>(0);
			tipoConsultaComboAluno.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultaComboAluno.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
			tipoConsultaComboAluno.add(new SelectItem("nomePessoa", "Nome Aluno"));
			tipoConsultaComboAluno.add(new SelectItem("nomeCurso", "Nome Curso"));
		}
		return tipoConsultaComboAluno;
	}

	private List<SelectItem> tipoConsultaComboFuncionario;

	public List<SelectItem> getTipoConsultaComboFuncionario() {
		if (tipoConsultaComboFuncionario == null) {
			tipoConsultaComboFuncionario = new ArrayList<SelectItem>(0);
			tipoConsultaComboFuncionario.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultaComboFuncionario.add(new SelectItem("nome", "Nome"));
			// tipoConsultaComboFuncionario.add(new SelectItem("CPF", "CPF"));
		}
		return tipoConsultaComboFuncionario;
	}

	private List<SelectItem> tipoConsultaComboCandidato;

	public List<SelectItem> getTipoConsultaComboCandidato() {
		if (tipoConsultaComboCandidato == null) {
			tipoConsultaComboCandidato = new ArrayList<SelectItem>(0);
			tipoConsultaComboCandidato.add(new SelectItem("codigo", "Código"));
			tipoConsultaComboCandidato.add(new SelectItem("nome", "Nome"));
			// tipoConsultaComboCandidato.add(new SelectItem("CPF", "CPF"));
			// tipoConsultaComboCandidato.add(new SelectItem("RG", "RG"));
		}
		return tipoConsultaComboCandidato;
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
			// tipoConsultaComboParceiro.add(new SelectItem("RG", "RG"));
			// tipoConsultaComboParceiro.add(new SelectItem("CPF", "CPF"));
			// tipoConsultaComboParceiro.add(new SelectItem("tipoParceiro",
			// "Tipo Parceiro"));
		}
		return tipoConsultaComboParceiro;
	}

	public List<SelectItem> getTipoConsultaComboResponsavelFinanceiro() {
		if (tipoConsultaComboResponsavelFinanceiro == null) {
			tipoConsultaComboResponsavelFinanceiro = new ArrayList<SelectItem>(0);
			tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("nomeAluno", "Aluno"));
			tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("CPF", "CPF"));
		}
		return tipoConsultaComboResponsavelFinanceiro;
	}

	public List<SelectItem> getTipoConsultaComboFornecedor() {
		if (tipoConsultaComboFornecedor == null) {
			tipoConsultaComboFornecedor = new ArrayList<SelectItem>(0);
			tipoConsultaComboFornecedor.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboFornecedor.add(new SelectItem("razaoSocial", "Razão Social"));
			tipoConsultaComboFornecedor.add(new SelectItem("CNPJ", "CNPJ"));
			tipoConsultaComboFornecedor.add(new SelectItem("CPF", "CPF"));
			tipoConsultaComboFornecedor.add(new SelectItem("RG", "RG"));
			tipoConsultaComboFornecedor.add(new SelectItem("codigo", "Codigo"));
		}
		return tipoConsultaComboFornecedor;
	}

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

	public String getModalTipoPessoa() {
		String tipo = "";
		if (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosReceita()) {
			if (getRelatorioSEIDecidirVO().getTipoPessoaEnum() != null && !getRelatorioSEIDecidirVO().getTipoPessoaEnum().equals(TipoPessoa.NENHUM)) {
				tipo = getRelatorioSEIDecidirVO().getTipoPessoaEnum().getValor();
			}
		} else if (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosDespesa() && getRelatorioSEIDecidirVO().getTipoSacadoEnum() != null) {
			tipo = getRelatorioSEIDecidirVO().getTipoSacadoEnum().getValor();
		}		
		if (!tipo.trim().isEmpty()) {
			if (tipo.equals(TipoPessoa.ALUNO.getValor()) || tipo.equals(TipoSacado.ALUNO.getValor())) {
				return "RichFaces.$('panelAluno').show()";
			}
			if (tipo.equals(TipoPessoa.FUNCIONARIO.getValor()) || tipo.equals(TipoSacado.FUNCIONARIO_PROFESSOR.getValor())) {
				return "RichFaces.$('panelFuncionario').show()";
			}
			if (tipo.equals(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor()) || tipo.equals(TipoSacado.RESPONSAVEL_FINANCEIRO.getValor())) {
				return "RichFaces.$('panelResponsavelFinanceiro').show()";
			}
			if (tipo.equals(TipoPessoa.FORNECEDOR.getValor()) || tipo.equals(TipoSacado.FORNECEDOR.getValor())) {
				return "RichFaces.$('panelFornecedor').show()";
			}
			if (tipo.equals(TipoPessoa.PARCEIRO.getValor()) || tipo.equals(TipoSacado.PARCEIRO.getValor())) {
				return "RichFaces.$('panelParceiro').show()";
			}
			if (tipo.equals(TipoPessoa.CANDIDATO.getValor())) {
				return "RichFaces.$('panelCandidato').show()";
			}
		}
		return "";
	}

	public Boolean getApresentarTipoPessoa() {
		return (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosReceita() && getRelatorioSEIDecidirVO().getTipoPessoaEnum() != null && !getRelatorioSEIDecidirVO().getTipoPessoaEnum().equals(TipoPessoa.NENHUM)) || (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosDespesa() && getRelatorioSEIDecidirVO().getTipoSacadoEnum() != null && !getRelatorioSEIDecidirVO().getTipoSacadoEnum().equals(TipoSacado.BANCO) && !getRelatorioSEIDecidirVO().getTipoSacadoEnum().equals(TipoSacado.OPERADORA_CARTAO));
	}

	public String getDescricaoTipoPessoa() {
		if (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosReceita()) {
			return getRelatorioSEIDecidirVO().getTipoPessoaEnum() == null || getRelatorioSEIDecidirVO().getTipoPessoaEnum().equals(TipoPessoa.NENHUM) ? "" : getRelatorioSEIDecidirVO().getTipoPessoaEnum().getDescricao();
		} else if (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosDespesa()) {
			return getRelatorioSEIDecidirVO().getTipoSacadoEnum() == null ? "" : getRelatorioSEIDecidirVO().getTipoSacadoEnum().getDescricao();
		} else {
			return "";
		}
	}

	public void setRelatorioSEIDecidirVO(RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
		this.relatorioSEIDecidirVO = relatorioSEIDecidirVO;
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

	public List<SelectItem> getListaSelectItemNivelEducacional() {
		if (listaSelectItemNivelEducacional == null) {
			listaSelectItemNivelEducacional = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelEducacional.class, "valor", "descricao", true);
		}
		return listaSelectItemNivelEducacional;
	}

	public void setListaSelectItemNivelEducacional(List<SelectItem> listaSelectItemNivelEducacional) {
		this.listaSelectItemNivelEducacional = listaSelectItemNivelEducacional;
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

	public List<SelectItem> getListaSelectItemContaCorrente() {
		if (listaSelectItemContaCorrente == null) {
			listaSelectItemContaCorrente = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemContaCorrente;
	}

	public void setListaSelectItemContaCorrente(List<SelectItem> listaSelectItemContaCorrente) {
		this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
	}

	public List<SelectItem> listaSelectItemTipoPessoa;

	public List<SelectItem> getListaSelectItemTipoPessoa() {
		if (listaSelectItemTipoPessoa == null) {
			listaSelectItemTipoPessoa = new ArrayList<SelectItem>(0);
			listaSelectItemTipoPessoa.add(new SelectItem(TipoPessoa.NENHUM, ""));
			listaSelectItemTipoPessoa.add(new SelectItem(TipoPessoa.ALUNO, TipoPessoa.ALUNO.getDescricao()));
			listaSelectItemTipoPessoa.add(new SelectItem(TipoPessoa.RESPONSAVEL_FINANCEIRO, TipoPessoa.RESPONSAVEL_FINANCEIRO.getDescricao()));
			listaSelectItemTipoPessoa.add(new SelectItem(TipoPessoa.FUNCIONARIO, TipoPessoa.FUNCIONARIO.getDescricao()));
			listaSelectItemTipoPessoa.add(new SelectItem(TipoPessoa.CANDIDATO, TipoPessoa.CANDIDATO.getDescricao()));
			listaSelectItemTipoPessoa.add(new SelectItem(TipoPessoa.PARCEIRO, TipoPessoa.PARCEIRO.getDescricao()));
			listaSelectItemTipoPessoa.add(new SelectItem(TipoPessoa.FORNECEDOR, TipoPessoa.FORNECEDOR.getDescricao()));
			listaSelectItemTipoPessoa.add(new SelectItem(TipoPessoa.REQUERENTE, TipoPessoa.REQUERENTE.getDescricao()));
		}
		return listaSelectItemTipoPessoa;
	}
	
	public List<SelectItem> listaSelectItemTipoSacado;

	public List<SelectItem> getListaSelectItemTipoSacado() {
		if (listaSelectItemTipoSacado == null) {
			listaSelectItemTipoSacado = new ArrayList<SelectItem>(0);
			listaSelectItemTipoSacado.add(new SelectItem(null, ""));
			listaSelectItemTipoSacado.add(new SelectItem(TipoSacado.ALUNO, TipoSacado.ALUNO.getDescricao() + "/" + TipoSacado.CANDIDATO.getDescricao()));
			listaSelectItemTipoSacado.add(new SelectItem(TipoSacado.RESPONSAVEL_FINANCEIRO, TipoSacado.RESPONSAVEL_FINANCEIRO.getDescricao()));
			listaSelectItemTipoSacado.add(new SelectItem(TipoSacado.FUNCIONARIO_PROFESSOR, TipoSacado.FUNCIONARIO_PROFESSOR.getDescricao()));
			listaSelectItemTipoSacado.add(new SelectItem(TipoSacado.BANCO, TipoSacado.BANCO.getDescricao()));
			listaSelectItemTipoSacado.add(new SelectItem(TipoSacado.PARCEIRO, TipoSacado.PARCEIRO.getDescricao()));
			listaSelectItemTipoSacado.add(new SelectItem(TipoSacado.FORNECEDOR, TipoSacado.FORNECEDOR.getDescricao()));
			listaSelectItemTipoSacado.add(new SelectItem(TipoSacado.OPERADORA_CARTAO, TipoSacado.OPERADORA_CARTAO.getDescricao()));
		}
		return listaSelectItemTipoSacado;
	}

	public List<SelectItem> getListaSelectItemFiltroConta() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("todos", "Todos"));
		itens.add(new SelectItem("recebidas", "Recebidas"));
		itens.add(new SelectItem("receber", "A Receber"));
		itens.add(new SelectItem("renegociadas", "Renegociadas"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemFiltrarPeriodo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("dataRecebimento", "Data Recebimento"));
		itens.add(new SelectItem("dataVencimento", "Data Vencimento"));
		itens.add(new SelectItem("dataCompentencia", "Data Competência"));
		return itens;
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

	public List<PessoaVO> getListaConsultaResponsavelFinanceiro() {
		if (listaConsultaResponsavelFinanceiro == null) {
			listaConsultaResponsavelFinanceiro = new ArrayList<PessoaVO>(0);
		}
		return listaConsultaResponsavelFinanceiro;
	}

	public void setListaConsultaResponsavelFinanceiro(List<PessoaVO> listaConsultaResponsavelFinanceiro) {
		this.listaConsultaResponsavelFinanceiro = listaConsultaResponsavelFinanceiro;
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

	public List<MatriculaVO> getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<MatriculaVO>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList<FuncionarioVO>(0);
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public String getCampoConsultaFuncionario() {
		if (campoConsultaFuncionario == null) {
			campoConsultaFuncionario = "";
		}
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		if (valorConsultaFuncionario == null) {
			valorConsultaFuncionario = "";
		}
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public List<PessoaVO> getListaConsultaCandidato() {
		if (listaConsultaCandidato == null) {
			listaConsultaCandidato = new ArrayList<PessoaVO>(0);
		}
		return listaConsultaCandidato;
	}

	public void setListaConsultaCandidato(List<PessoaVO> listaConsultaCandidato) {
		this.listaConsultaCandidato = listaConsultaCandidato;
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

	public String getValorConsultaCandidato() {
		if (valorConsultaCandidato == null) {
			valorConsultaCandidato = "";
		}
		return valorConsultaCandidato;
	}

	public void setValorConsultaCandidato(String valorConsultaCandidato) {
		this.valorConsultaCandidato = valorConsultaCandidato;
	}

	public List<ParceiroVO> getListaConsultaParceiro() {
		if (listaConsultaParceiro == null) {
			listaConsultaParceiro = new ArrayList<ParceiroVO>(0);
		}
		return listaConsultaParceiro;
	}

	public void setListaConsultaParceiro(List<ParceiroVO> listaConsultaParceiro) {
		this.listaConsultaParceiro = listaConsultaParceiro;
	}

	public String getCampoConsultaParceiro() {
		if (campoConsultaParceiro == null) {
			campoConsultaParceiro = "";
		}
		return campoConsultaParceiro;
	}

	public void setCampoConsultaParceiro(String campoConsultaParceiro) {
		this.campoConsultaParceiro = campoConsultaParceiro;
	}

	public String getValorConsultaParceiro() {
		if (valorConsultaParceiro == null) {
			valorConsultaParceiro = "";
		}
		return valorConsultaParceiro;
	}

	public void setValorConsultaParceiro(String valorConsultaParceiro) {
		this.valorConsultaParceiro = valorConsultaParceiro;
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

	public String getValorConsultaResponsavelFinanceiro() {
		if (valorConsultaResponsavelFinanceiro == null) {
			valorConsultaResponsavelFinanceiro = "";
		}
		return valorConsultaResponsavelFinanceiro;
	}

	public void setValorConsultaResponsavelFinanceiro(String valorConsultaResponsavelFinanceiro) {
		this.valorConsultaResponsavelFinanceiro = valorConsultaResponsavelFinanceiro;
	}

	public void setTipoConsultaComboResponsavelFinanceiro(List<SelectItem> tipoConsultaComboResponsavelFinanceiro) {
		this.tipoConsultaComboResponsavelFinanceiro = tipoConsultaComboResponsavelFinanceiro;
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

	public String getCampoConsultaFornecedor() {
		if (campoConsultaFornecedor == null) {
			campoConsultaFornecedor = "";
		}
		return campoConsultaFornecedor;
	}

	public void setCampoConsultaFornecedor(String campoConsultaFornecedor) {
		this.campoConsultaFornecedor = campoConsultaFornecedor;
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

	public void setTipoConsultaComboFornecedor(List<SelectItem> tipoConsultaComboFornecedor) {
		this.tipoConsultaComboFornecedor = tipoConsultaComboFornecedor;
	}

	public Boolean getMarcarTodosCursos() {
		if (marcarTodosCursos == null) {
			marcarTodosCursos = false;
		}
		return marcarTodosCursos;
	}

	public void setMarcarTodosCursos(Boolean marcarTodosCursos) {
		this.marcarTodosCursos = marcarTodosCursos;
	}

	public Boolean getMarcarTodosGradeCurricularEstagios() {
		if (marcarTodosGradeCurricularEstagios == null) {
			marcarTodosGradeCurricularEstagios = false;
		}
		return marcarTodosGradeCurricularEstagios;
	}

	public void setMarcarTodosGradeCurricularEstagios(Boolean marcarTodosGradeCurricularEstagios) {
		this.marcarTodosGradeCurricularEstagios = marcarTodosGradeCurricularEstagios;
	}

	public Integer getColumnCursoVO() throws Exception {
		if (getRelatorioSEIDecidirVO().getCursoVOs().size() > 4) {
			return 4;
		}
		return getRelatorioSEIDecidirVO().getCursoVOs().size();
	}

	public Integer getElementCursoVO() throws Exception {
		return getRelatorioSEIDecidirVO().getCursoVOs().size();
	}

	public Integer getColumnTurnoVO() throws Exception {
		if (getRelatorioSEIDecidirVO().getTurnoVOs().size() > 4) {
			return 4;
		}
		return getRelatorioSEIDecidirVO().getTurnoVOs().size();
	}

	public Integer getElementTurnoVO() throws Exception {
		return getRelatorioSEIDecidirVO().getTurnoVOs().size();
	}

	public Integer getColumnCentroReceitaVO() throws Exception {
		if (getRelatorioSEIDecidirVO().getCentroReceitaVOs().size() > 4) {
			return 4;
		}
		return getRelatorioSEIDecidirVO().getCentroReceitaVOs().size();
	}

	public Integer getElementCentroReceitaVO() throws Exception {
		return getRelatorioSEIDecidirVO().getCentroReceitaVOs().size();
	}
	public Integer getColumnCategoriaDespesaVO() throws Exception {
		if (getRelatorioSEIDecidirVO().getCategoriaDespesaVOs().size() > 4) {
			return 4;
		}
		return getRelatorioSEIDecidirVO().getCategoriaDespesaVOs().size();
	}
	
	public Integer getElementCategoriaDespesaVO() throws Exception {
		return getRelatorioSEIDecidirVO().getCategoriaDespesaVOs().size();
	}

	public Boolean getMarcarTodosTurnos() {
		if (marcarTodosTurnos == null) {
			marcarTodosTurnos = false;
		}
		return marcarTodosTurnos;
	}

	public void setMarcarTodosTurnos(Boolean marcarTodosTurnos) {
		this.marcarTodosTurnos = marcarTodosTurnos;
	}

	public Boolean getMarcarTodosCentroReceitas() {
		if (marcarTodosCentroReceitas == null) {
			marcarTodosCentroReceitas = false;
		}
		return marcarTodosCentroReceitas;
	}

	public void setMarcarTodosCentroReceitas(Boolean marcarTodosCentroReceitas) {
		this.marcarTodosCentroReceitas = marcarTodosCentroReceitas;
	}

	public void montarListaSelectItemLayoutRelatorioSeiDecidir() {
		try {

			/*if (((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("ORIGEM") == null) {
				return;
			}*/
			getListaSelectItemLayoutRelatorioSeiDecidir().clear();
			getListaSelectItemLayoutRelatorioSeiDecidir().add(new SelectItem(0, " "));
			List<LayoutRelatorioSEIDecidirVO> layoutRelatorioSEIDecidirVOs = getFacadeFactory().getLayoutRelatorioSEIDecidirInterfaceFacade().consultarPorModulo(getModulo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());

			for (LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO : layoutRelatorioSEIDecidirVOs) {
//				List<LayoutRelatorioSEIDecidirPerfilAcessoVO> listaPerfisAcessos  = getFacadeFactory().getLayoutRelatorioSEIDecidirPerfilAcessoInterfaceFacade().consultarPorLayoutRelatorioSeiDecidir(layoutRelatorioSEIDecidirVO.getCodigo());
//				List<LayoutRelatorioSEIDecidirFuncionarioVO> listaLayoutFuncionarioCargo  = getFacadeFactory().getLayoutRelatorioSEIDecidirFuncionarioInterfaceFacade().consultarPorLayoutRelatorioSeiDecidir(layoutRelatorioSEIDecidirVO.getCodigo());
//
//				if (listaPerfisAcessos.isEmpty() && listaLayoutFuncionarioCargo.isEmpty()) {
					getListaSelectItemLayoutRelatorioSeiDecidir().add(new SelectItem(layoutRelatorioSEIDecidirVO.getCodigo(), layoutRelatorioSEIDecidirVO.getDescricao()));
//				} else {
//					boolean existePerfil = listaPerfisAcessos.stream().anyMatch(p -> p.getPerfilAcessoVO().getCodigo().equals(getUsuarioLogado().getPerfilAcesso().getCodigo()));
//					boolean existeUsuario = listaLayoutFuncionarioCargo.stream().anyMatch(p -> 
//						p.getFuncionarioVO().getPessoa().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo()));
//
//					if ( (existePerfil || existeUsuario) && !layoutRelatorioSEIDecidirVO.getSubRelatorio()) {
//						getListaSelectItemLayoutRelatorioSeiDecidir().add(new SelectItem(layoutRelatorioSEIDecidirVO.getCodigo(), layoutRelatorioSEIDecidirVO.getDescricao()));
//					}
//				}
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.SUCESSO);
		}
	}

	public List<SelectItem> getListaSelectItemLayoutRelatorioSeiDecidir() {
		if (listaSelectItemLayoutRelatorioSeiDecidir == null) {
			listaSelectItemLayoutRelatorioSeiDecidir = new ArrayList<SelectItem>(0);
			montarListaSelectItemLayoutRelatorioSeiDecidir();
		}
		return listaSelectItemLayoutRelatorioSeiDecidir;
	}

	public void setListaSelectItemLayoutRelatorioSeiDecidir(List<SelectItem> listaSelectItemLayoutRelatorioSeiDecidir) {
		this.listaSelectItemLayoutRelatorioSeiDecidir = listaSelectItemLayoutRelatorioSeiDecidir;
	}

	public Boolean getApresentarComboBanco() {
		return getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosDespesa() && getRelatorioSEIDecidirVO().getTipoSacadoEnum() != null && getRelatorioSEIDecidirVO().getTipoSacadoEnum().equals(TipoSacado.BANCO);
	}

	public Boolean getApresentarComboOperadoraCartao() {
		return getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosDespesa() && getRelatorioSEIDecidirVO().getTipoSacadoEnum() != null && getRelatorioSEIDecidirVO().getTipoSacadoEnum().equals(TipoSacado.OPERADORA_CARTAO);
	}

	public String getLabelPeriodo() {
		if (getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_RECEBIMENTO_E_DATA_COMPETENCIA)) {
			return TipoFiltroPeriodoSeiDecidirEnum.DATA_RECEBIMENTO.getValorApresentar();
		} else if (getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_VENCIMENTO_E_DATA_COMPETENCIA)) {
			return TipoFiltroPeriodoSeiDecidirEnum.DATA_VENCIMENTO.getValorApresentar();
		} else if (getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_VENCIMENTO_E_DATA_RECEBIMENTO)) {
			return TipoFiltroPeriodoSeiDecidirEnum.DATA_VENCIMENTO.getValorApresentar();
		} else if (getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_PAGAMENTO_E_DATA_COMPETENCIA)) {
			return TipoFiltroPeriodoSeiDecidirEnum.DATA_PAGAMENTO.getValorApresentar();
		} else if (getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_VENCIMENTO_E_DATA_PAGAMENTO)) {
			return TipoFiltroPeriodoSeiDecidirEnum.DATA_VENCIMENTO.getValorApresentar();
		} else if (getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_VENCIMENTO_E_DATA_COMPENSACAO)) {
			return TipoFiltroPeriodoSeiDecidirEnum.DATA_VENCIMENTO.getValorApresentar();
		} else if (getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_RECEBIMENTO_E_DATA_COMPENSACAO)) {
			return TipoFiltroPeriodoSeiDecidirEnum.DATA_RECEBIMENTO.getValorApresentar();
		} else if (getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_PAGAMENTO_E_DATA_COMPENSACAO)) {
			return TipoFiltroPeriodoSeiDecidirEnum.DATA_PAGAMENTO.getValorApresentar();
		} else if (getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_COMPETENCIA_DATA_COMPENSACAO)) {
			return TipoFiltroPeriodoSeiDecidirEnum.DATA_COMPETENCIA.getValorApresentar();
		} else if (getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.NENHUM)) {
			return TipoFiltroPeriodoSeiDecidirEnum.NENHUM.getValorApresentar();
		} else {
			return getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().getValorApresentar();
		}

	}

	public String getLabelPeriodo2() {
		if (getApresentaPeriodo2()) {
			if (getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_RECEBIMENTO_E_DATA_COMPETENCIA)) {
				return TipoFiltroPeriodoSeiDecidirEnum.DATA_COMPETENCIA.getValorApresentar();
			} else if (getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_VENCIMENTO_E_DATA_COMPETENCIA)) {
				return TipoFiltroPeriodoSeiDecidirEnum.DATA_COMPETENCIA.getValorApresentar();
			} else if (getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_VENCIMENTO_E_DATA_RECEBIMENTO)) {
				return TipoFiltroPeriodoSeiDecidirEnum.DATA_RECEBIMENTO.getValorApresentar();
			} else if (getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_VENCIMENTO_E_DATA_PAGAMENTO)) {
				return TipoFiltroPeriodoSeiDecidirEnum.DATA_PAGAMENTO.getValorApresentar();
			} else if (getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_PAGAMENTO_E_DATA_COMPETENCIA)) {
				return TipoFiltroPeriodoSeiDecidirEnum.DATA_COMPETENCIA.getValorApresentar();
			} else if (getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_VENCIMENTO_E_DATA_COMPENSACAO)) {
				return TipoFiltroPeriodoSeiDecidirEnum.DATA_COMPENSACAO.getValorApresentar();			
			} else if (getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_RECEBIMENTO_E_DATA_COMPENSACAO)) {
				return TipoFiltroPeriodoSeiDecidirEnum.DATA_COMPENSACAO.getValorApresentar();
			} else if (getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_PAGAMENTO_E_DATA_COMPENSACAO)) {
				return TipoFiltroPeriodoSeiDecidirEnum.DATA_COMPENSACAO.getValorApresentar();
			} else if (getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_COMPETENCIA_DATA_COMPENSACAO)) {
				return TipoFiltroPeriodoSeiDecidirEnum.DATA_COMPENSACAO.getValorApresentar();
			} else {
				return getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().getValorApresentar();
			}
		}
		return "";
	}

	public Boolean getApresentaPeriodo2() {
		return getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_RECEBIMENTO_E_DATA_COMPETENCIA) 
				|| getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_VENCIMENTO_E_DATA_COMPETENCIA) 
				|| getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_VENCIMENTO_E_DATA_RECEBIMENTO) 
				|| getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_PAGAMENTO_E_DATA_COMPETENCIA) 
				|| getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_VENCIMENTO_E_DATA_COMPENSACAO) 
				|| getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_VENCIMENTO_E_DATA_PAGAMENTO)
				|| getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_PAGAMENTO_E_DATA_COMPENSACAO)
				|| getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_COMPETENCIA_DATA_COMPENSACAO)
				|| getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_RECEBIMENTO_E_DATA_COMPENSACAO);
		
	}

	public Boolean getApresentaPeriodo() {
		return !getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.NENHUM) && !getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.ANO_SEMESTRE);
	}

	public Boolean getApresentaAnoSemestre() {
		return !getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.NENHUM) && 
				(getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.ANO_SEMESTRE) || getRelatorioSEIDecidirVO().getTipoFiltroPeriodo().equals(TipoFiltroPeriodoSeiDecidirEnum.DATA_MATRICULA_E_ANO_SEMESTRE));
	}

	public List<SelectItem> getListaSelectItemTipoFiltroPeriodo() {
		if ((listaSelectItemTipoFiltroPeriodo == null || listaSelectItemTipoFiltroPeriodo.isEmpty()) && getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getCodigo() > 0) {
			listaSelectItemTipoFiltroPeriodo = UtilSelectItem.getListaSelectItemEnum(TipoFiltroPeriodoSeiDecidirEnum.getValues(getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getModulo()), Obrigatorio.SIM);
			/*if(getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosAcademicos()){
				getRelatorioSEIDecidirVO().setTipoFiltroPeriodo(TipoFiltroPeriodoSeiDecidirEnum.ANO_SEMESTRE);
			}else {
				getRelatorioSEIDecidirVO().setTipoFiltroPeriodo(TipoFiltroPeriodoSeiDecidirEnum.DATA_COMPETENCIA);
			}*/
		}
		return listaSelectItemTipoFiltroPeriodo;
	}

	public void setListaSelectItemTipoFiltroPeriodo(List<SelectItem> listaSelectItemTipoFiltroPeriodo) {
		this.listaSelectItemTipoFiltroPeriodo = listaSelectItemTipoFiltroPeriodo;
	}

	public List<SelectItem> getListaSelectItemBanco() {
		if (listaSelectItemBanco == null) {
			listaSelectItemBanco = new ArrayList<SelectItem>(0);
			try {
				List<BancoVO> bancoVOs = getFacadeFactory().getBancoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				listaSelectItemBanco.add(new SelectItem(0, ""));
				for (BancoVO bancoVO : bancoVOs) {
					listaSelectItemBanco.add(new SelectItem(bancoVO.getCodigo(), bancoVO.getNome()));
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		return listaSelectItemBanco;
	}

	public void setListaSelectItemBanco(List<SelectItem> listaSelectItemBanco) {
		this.listaSelectItemBanco = listaSelectItemBanco;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getListaSelectItemOperadoraCartao() {
		if (listaSelectItemOperadoraCartao == null) {
			listaSelectItemOperadoraCartao = new ArrayList<SelectItem>(0);
			try {
				List<OperadoraCartaoVO> operadoraCartaoVOs = getFacadeFactory().getOperadoraCartaoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				listaSelectItemOperadoraCartao.add(new SelectItem(0, ""));
				for (OperadoraCartaoVO operadoraCartaoVO : operadoraCartaoVOs) {
					listaSelectItemOperadoraCartao.add(new SelectItem(operadoraCartaoVO.getCodigo(), operadoraCartaoVO.getNome()));
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		return listaSelectItemOperadoraCartao;
	}

	public void setListaSelectItemOperadoraCartao(List<SelectItem> listaSelectItemOperadoraCartao) {
		this.listaSelectItemOperadoraCartao = listaSelectItemOperadoraCartao;
	}

	public Boolean getMarcarTodosCategoriaDespesa() {
		if (marcarTodosCategoriaDespesa == null) {
			marcarTodosCategoriaDespesa = false;
		}
		return marcarTodosCategoriaDespesa;
	}

	public void setMarcarTodosCategoriaDespesa(Boolean marcarTodosCategoriaDespesa) {
		this.marcarTodosCategoriaDespesa = marcarTodosCategoriaDespesa;
	}

	public RelatorioSEIDecidirModuloEnum getModulo() {
		if (modulo == null) {
			modulo = RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA;
		}
 		return modulo;
	}

	public void setModulo(RelatorioSEIDecidirModuloEnum modulo) {
		this.modulo = modulo;
	}
	
	/**
	 * @return the gerarFormatoExportacaoDados
	 */
	public Boolean getGerarFormatoExportacaoDados() {
		if (gerarFormatoExportacaoDados == null) {
			gerarFormatoExportacaoDados = false;
		}
		return gerarFormatoExportacaoDados;
	}

	/**
	 * @param gerarFormatoExportacaoDados the gerarFormatoExportacaoDados to set
	 */
	public void setGerarFormatoExportacaoDados(Boolean gerarFormatoExportacaoDados) {
		this.gerarFormatoExportacaoDados = gerarFormatoExportacaoDados;
	}

	public Boolean getPermiteGerarFormatoExportacaoDados(){
		return Uteis.isAtributoPreenchido(getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO())
				&& ((getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.REQUERIMENTO) && getLoginControle().getPermissaoAcessoMenuVO().getPermitirGerarRelatorioSeiDecidirRequerimentoApenasDados())
						|| (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.PROCESSO_SELETIVO) && getLoginControle().getPermissaoAcessoMenuVO().getPermitirGerarRelatorioSeiDecidirProcessoSeletivoApenasDados())
						|| (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.RECURSOS_HUMANOS) && getLoginControle().getPermissaoAcessoMenuVO().getPermitirGerarRelatorioSeiDecidirRHApenasDados())
						|| (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.PLANO_ORCAMENTARIO) && getLoginControle().getPermissaoAcessoMenuVO().getPermitirGerarRelatorioSeiDecidirPlanoOrcamentarioApenasDados())
						|| (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosAcademicos() && getLoginControle().getPermissaoAcessoMenuVO().getPermitirGerarRelatorioSeiDecidirAcademicoApenasDados())
						|| (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosReceita() && getLoginControle().getPermissaoAcessoMenuVO().getPermitirGerarRelatorioSeiDecidirReceitaApenasDados())						
						|| (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosFinanceirosDespesa() && getLoginControle().getPermissaoAcessoMenuVO().getPermitirGerarRelatorioSeiDecidirDespesaApenasDados()));
	}
	
	/**
	 * Atualiza a {@link CompetenciaPeriodoFolhaPagamentoVO} pela data de competencia selecionda.
	 */
	public void atualizarCompetenciaPeriodo() {
		try {
			if (Uteis.isAtributoPreenchido(getRelatorioSEIDecidirVO().getDataCompetencia())) {
				CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO = getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarCompetenciaFolhaPagamentoPorMesAno(
						UteisData.getMesData(getRelatorioSEIDecidirVO().getDataCompetencia()), UteisData.getAnoData(getRelatorioSEIDecidirVO().getDataCompetencia()), false, getUsuarioLogado());
				List<CompetenciaPeriodoFolhaPagamentoVO> periodos = getFacadeFactory().getCompetenciaPeriodoFolhaPagamentoInterfaceFacade().consultarPorCompetenciaFolhaPagamento(competenciaFolhaPagamentoVO);
				setListaSelectItemCompetenciaPeriodo(UtilSelectItem.getListaSelectItem(periodos, "codigo", "periodoApresentacao", true));
			} else {
				setListaSelectItemCompetenciaPeriodo(new ArrayList<>());
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	/**
	 * Consultar {@link FuncionarioCargoVO} por matricula informada.
	 */
	public void consultarFuncionarioPorMatricula() {
		try {
			if (Uteis.isAtributoPreenchido(getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().getFuncionarioCargoVO().getMatriculaCargo())) {
				FuncionarioCargoVO funcionarioCargo = getFacadeFactory().getFuncionarioCargoFacade().consultarPorMatriculaCargo(getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().getFuncionarioCargoVO().getMatriculaCargo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuario());
				getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().setFuncionarioCargoVO(funcionarioCargo);
						
				setMensagemID("msg_entre_dados");
			} else {
				getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().setFuncionarioCargoVO(new FuncionarioCargoVO());
			}
		} catch (Exception e) {
			getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().setFuncionarioCargoVO(new FuncionarioCargoVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Consulta a {@link SecaoFolhaPagamentoVO} pelo identificador informado.
	 */
	public void consultarSecaoPorIdentificador() {
		try {
			String identificadorSecao = getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().getSecaoFolhaPagamento().getIdentificador();
			if (Uteis.isAtributoPreenchido(identificadorSecao)) {
				getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().setSecaoFolhaPagamento(
						getFacadeFactory().getSecaoFolhaPagamentoInterfaceFacade().consultarPorIdentificador(identificadorSecao));
			} else {
				getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().setSecaoFolhaPagamento(new SecaoFolhaPagamentoVO());
			}
		} catch (Exception e) {
			getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().setSecaoFolhaPagamento(new SecaoFolhaPagamentoVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarEventoPorIdentificador() {
		try {
			if (Uteis.isAtributoPreenchido(this.getRelatorioSEIDecidirVO().getEventoFolhaPagamentoVO().getIdentificador())) {
				this.getRelatorioSEIDecidirVO().setEventoFolhaPagamentoVO(getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChaveIdentificador(this.getRelatorioSEIDecidirVO().getEventoFolhaPagamentoVO().getIdentificador(), false, getUsuarioLogado()));
			} else {
				this.getRelatorioSEIDecidirVO().setEventoFolhaPagamentoVO(new EventoFolhaPagamentoVO());
			}
		} catch (Exception e) {
			this.getRelatorioSEIDecidirVO().setEventoFolhaPagamentoVO(new EventoFolhaPagamentoVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarCargoPorCodigo() {
		try {
			if (Uteis.isAtributoPreenchido(getRelatorioSEIDecidirVO().getCargoVO())) {
				getRelatorioSEIDecidirVO().setCargoVO(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(getRelatorioSEIDecidirVO().getCargoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			} else {
				getRelatorioSEIDecidirVO().setCargoVO(new CargoVO());
			}
		} catch (Exception e) {
			getRelatorioSEIDecidirVO().setCargoVO(new CargoVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarSecaoFolhaPagamento() {
		try {

			if (getCampoConsultaSecaoFolhaPagamento().equals(EnumCampoConsultaSecaoFolhaPagamento.CODIGO.name()) && 
					getValorConsultaSecaoFolhaPagamento().trim().isEmpty() || !Uteis.getIsValorNumerico(getValorConsultaSecaoFolhaPagamento())) {
				throw new ConsistirException(UteisJSF.internacionalizar("prt_SecaoFolhaPagamento_ConsultaCampoCodigoInvalido"));
			}

			setListaConsultaSecaoFolhaPagamento(getFacadeFactory().getSecaoFolhaPagamentoInterfaceFacade().consultar(getCampoConsultaSecaoFolhaPagamento(), getValorConsultaSecaoFolhaPagamento(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));

			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarEvento() {
		try {
			if(getCampoConsultaEvento().equals("codigo")) {
				Uteis.validarSomenteNumeroString(getControleConsulta().getValorConsulta());
			} 
			
			setListaEventosFolhaPagamento(getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorFiltro(campoConsultaEvento, valorConsultaEvento, "ATIVO", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por realizar a paginacao da pesquisa secao folha de pagamento.
	 * 
	 * @param dataScrollerEvent
	 */
	public void scrollerListenerSecaoFolhaPagamento(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultarDados();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void scrollerListenerCargo(DataScrollEvent dataScrollerEvent) {
		getDataModeloCargo().setPaginaAtual(dataScrollerEvent.getPage());
		getDataModeloCargo().setPage(dataScrollerEvent.getPage());
		this.consultarCargos();
	}
	
	public List<SelectItem> getTipoConsultaComboEvento() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("descricao", UteisJSF.internacionalizar("prt_TextoPadrao_descricao")));
		itens.add(new SelectItem("identificador", UteisJSF.internacionalizar("prt_TextoPadrao_identificador")));
		return itens;
	}
	
	public boolean getApresentarResultadoConsultaSecao() {
		return getListaConsultaSecaoFolhaPagamento().size() > 0;
	}
	
	public boolean getApresentarPaginadorConsultaSecao() {
		return getListaConsultaSecaoFolhaPagamento().size() > 5;
	}
	
	public void selecionarFormaContratacao() {
		setMarcarTodosFormaContratacao(FormaContratacaoFuncionarioEnum.values().length == this.formaContratacao.length ? Boolean.TRUE : Boolean.FALSE); 
		
		getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().setFormaContratacaoFuncionario("");
		for(String formaContratacao : this.formaContratacao ) {
			getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().setFormaContratacaoFuncionario(
					getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().getFormaContratacaoFuncionario().concat(formaContratacao).concat(";"));
		}
	}

	public void selecionarRecebimento() {
		setMarcarTodosRecebimento(TipoRecebimentoEnum.values().length == this.recebimento.length ? Boolean.TRUE : Boolean.FALSE); 

		getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().setTipoRecebimento("");
		for(String recebimento : this.recebimento ) {
			getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().setTipoRecebimento(
					getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().getTipoRecebimento().concat(recebimento).concat(";"));
		}
	}

	public void selecionarSituacao() {
		setMarcarTodosSituacao(SituacaoFuncionarioEnum.values().length == this.situacao.length ? Boolean.TRUE : Boolean.FALSE); 
		
		getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().setSituacaoFuncionario("");
		for(String situacao : this.situacao ) {
			getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().setSituacaoFuncionario(
					getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().getSituacaoFuncionario().concat(situacao).concat(";"));
		}
	}

	public void selecionarSecaoFolhaPagamento() {
    	SecaoFolhaPagamentoVO obj = (SecaoFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("itemSecaoFolhaPagamento");
    	getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().setSecaoFolhaPagamento(obj);
    	this.getListaConsultaSecaoFolhaPagamento().clear();
    	setMensagemID(MSG_TELA.msg_dados_selecionados.name());
    }

	public void selecionarEvento() {
		EventoFolhaPagamentoVO obj = (EventoFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("eventoItem");
		getRelatorioSEIDecidirVO().setEventoFolhaPagamentoVO(obj);

		valorConsultaEvento = "";
		campoConsultaEvento = "";
		getListaEventosFolhaPagamento().clear();
		setMensagemID(MSG_TELA.msg_dados_selecionados.name());
	}

	public void selecionarFuncionarioCargo() {
		FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap().get("funcionarioCargoItem");
		getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().setFuncionarioCargoVO(obj);

		valorConsultaFuncionario = "";
		campoConsultaFuncionario = "";
		setMensagemID(MSG_TELA.msg_dados_selecionados.name());
	}

	public void selecionarCargo() {
		CargoVO obj = (CargoVO) context().getExternalContext().getRequestMap().get("CargoItem");
		getRelatorioSEIDecidirVO().setCargoVO(obj);

		limparDadosCargo();
	}

	public void limparDadosFuncionario () {
		getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().setFuncionarioCargoVO(new FuncionarioCargoVO());
		getControleConsulta().setValorConsulta("");
	}

    public void limparDadosSecaoFolhaPagamento() {
    	getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().setSecaoFolhaPagamento(new SecaoFolhaPagamentoVO());
    }

    public void limparDadosEvento() {
    	getRelatorioSEIDecidirVO().setEventoFolhaPagamentoVO(new EventoFolhaPagamentoVO());
		setMensagemID("msg_entre_dados");
	}
    
    public void selecionarTodosFormaContratacao() {
    	getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().setFormaContratacaoFuncionario("");
		if(getMarcarTodosFormaContratacao()) {
			for (FormaContratacaoFuncionarioEnum formaContratacaoFuncionarioEnum : FormaContratacaoFuncionarioEnum.values()) {
				getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().setFormaContratacaoFuncionario(
						getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().getFormaContratacaoFuncionario().concat(formaContratacaoFuncionarioEnum.toString()).concat(";"));
			}
		}

		formaContratacao = getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().getFormaContratacaoFuncionario().split(";");
	}
	
	public void selecionarTodosRecebimento() {
		getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().setTipoRecebimento("");
		if(getMarcarTodosRecebimento()) {
			for (TipoRecebimentoEnum formaRecebimentoEnum : TipoRecebimentoEnum.values()) {
				getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().setTipoRecebimento(
						getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().getTipoRecebimento().concat(formaRecebimentoEnum.toString()).concat(";"));
			}
		}

		recebimento = getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().getTipoRecebimento().split(";");
	}
	
	public void selecionarTodosSituacao() {
		getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().setSituacaoFuncionario("");
		if(getMarcarTodosSituacao()) {
			for (SituacaoFuncionarioEnum situacaoFuncionarioEnum : SituacaoFuncionarioEnum.values()) {
				getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().setSituacaoFuncionario(
						getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().getSituacaoFuncionario().concat(situacaoFuncionarioEnum.toString()).concat(";"));
			}
		}

		situacao = getRelatorioSEIDecidirVO().getTemplateLancamentoFolhaPagamento().getSituacaoFuncionario().split(";");
	}
	
	public Map<String, String> getSituacoes() {
		Map<String, String> situacoes = new LinkedHashMap<String, String>();
		for (SituacaoHoraAtividadeExtraClasseEnum situacao : SituacaoHoraAtividadeExtraClasseEnum.values()) {
			situacoes.put(situacao.getDescricao(), situacao.getValor());
		}
		return situacoes;
	}
	
	public void selecionarPlanoOrcamentario() {
		PlanoOrcamentarioVO obj = (PlanoOrcamentarioVO) context().getExternalContext().getRequestMap().get("itemPlanoOrcamentario");
		setPlanoOrcamentarioApresentar("");
		StringBuilder sb = new StringBuilder();
		getRelatorioSEIDecidirVO().getPlanosOrcamentarioVOs().add(obj);
		int contador = 0;
		boolean primeiro = true;

		for (PlanoOrcamentarioVO plano : getRelatorioSEIDecidirVO().getPlanosOrcamentarioVOs()) {
			if (obj.getCodigo() == plano.getCodigo() && !primeiro) {
				getRelatorioSEIDecidirVO().getPlanosOrcamentarioVOs().remove(contador);
				primeiro = false;
			} else if (obj.getCodigo() == plano.getCodigo() && primeiro) {
				primeiro = false;
			}
			contador++;
			sb.append(plano.getNome()).append(";");
		}
		setPlanoOrcamentarioApresentar(sb.toString());

		setMensagemID("msg_adicionarPlanoOrcamentarioRElaotiorSEIDecidir");
	}

	public void selecionarDepartamento() {
    	DepartamentoVO obj = (DepartamentoVO) context().getExternalContext().getRequestMap().get("itemDepartamento");
    	setDepartamentoApresentar("");
    	getRelatorioSEIDecidirVO().getDepartamentosVOs().add(obj);
		int contador = 0;
		boolean primeiro = true;

		StringBuilder sb = new StringBuilder();
		for (DepartamentoVO departamento : getRelatorioSEIDecidirVO().getDepartamentosVOs()) {
			if (obj.getCodigo() == departamento.getCodigo() && !primeiro) {
				getRelatorioSEIDecidirVO().getDepartamentosVOs().remove(contador);
				primeiro = false;
			} else if (obj.getCodigo() == departamento.getCodigo() && primeiro) {
				primeiro = false;
			}
			contador++;
			sb.append(departamento.getNome()).append(";");
		}
		setDepartamentoApresentar(sb.toString());
		setMensagemID(MSG_TELA.msg_dados_selecionados.name());
    }

	public void selecionarDepartamento2() {
		DepartamentoVO obj = (DepartamentoVO) context().getExternalContext().getRequestMap().get("itemDepartamento2");
		setDepartamentoApresentar("");
		getRelatorioSEIDecidirVO().getDepartamentosVOs().add(obj);
		int contador = 0;
		boolean primeiro = true;
		
		StringBuilder sb = new StringBuilder();
		for (DepartamentoVO departamento : getRelatorioSEIDecidirVO().getDepartamentosVOs()) {
			if (obj.getCodigo() == departamento.getCodigo() && !primeiro) {
				getRelatorioSEIDecidirVO().getDepartamentosVOs().remove(contador);
				primeiro = false;
			} else if (obj.getCodigo() == departamento.getCodigo() && primeiro) {
				primeiro = false;
			}
			contador++;
			sb.append(departamento.getNome()).append(";");
		}
		setDepartamentoApresentar(sb.toString());
		setMensagemID("msg_adicionarDepartamentoRElaotiorSEIDecidir");
	}
	
	public void selecionarCategoriaProduto() {
		CategoriaProdutoVO obj = (CategoriaProdutoVO) context().getExternalContext().getRequestMap().get("categoriaProdutoItem");
		setCategoriaProdutoApresentar("");
		StringBuilder sb = new StringBuilder();
		getRelatorioSEIDecidirVO().getCategoriasProdutoVOs().add(obj);
		int contador = 0;
		boolean primeiro = true;

		for (CategoriaProdutoVO categoriaProduto : getRelatorioSEIDecidirVO().getCategoriasProdutoVOs()) {
			if (obj.getCodigo() == categoriaProduto.getCodigo() && !primeiro) {
				getRelatorioSEIDecidirVO().getCategoriasProdutoVOs().remove(contador);
				primeiro = false;
			} else if (obj.getCodigo() == categoriaProduto.getCodigo() && primeiro) {
				primeiro = false;
			}
			sb.append(categoriaProduto.getNome()).append(";");
		}
		setCategoriaProdutoApresentar(sb.toString());
		setMensagemID("msg_adicionarCategoriaProdutoRElaotiorSEIDecidir");
	}

	//GETTER AND SETTER
	public List<SelectItem> getListaSelectItemCompetenciaPeriodo() {
		if (listaSelectItemCompetenciaPeriodo == null)
			listaSelectItemCompetenciaPeriodo = new ArrayList<>();
		return listaSelectItemCompetenciaPeriodo;
	}

	public void setListaSelectItemCompetenciaPeriodo(List<SelectItem> listaSelectItemCompetenciaPeriodo) {
		this.listaSelectItemCompetenciaPeriodo = listaSelectItemCompetenciaPeriodo;
	}
	
	public String getCampoConsultaSecaoFolhaPagamento() {
		return campoConsultaSecaoFolhaPagamento;
	}

	public void setCampoConsultaSecaoFolhaPagamento(String campoConsultaSecaoFolhaPagamento) {
		this.campoConsultaSecaoFolhaPagamento = campoConsultaSecaoFolhaPagamento;
	}

	public String getValorConsultaSecaoFolhaPagamento() {
		return valorConsultaSecaoFolhaPagamento;
	}

	public void setValorConsultaSecaoFolhaPagamento(String valorConsultaSecaoFolhaPagamento) {
		this.valorConsultaSecaoFolhaPagamento = valorConsultaSecaoFolhaPagamento;
	}

	public List<SecaoFolhaPagamentoVO> getListaConsultaSecaoFolhaPagamento() {
		if (listaConsultaSecaoFolhaPagamento == null) {
			listaConsultaSecaoFolhaPagamento = new ArrayList<>();
		}
		return listaConsultaSecaoFolhaPagamento;
	}

	public void setListaConsultaSecaoFolhaPagamento(List<SecaoFolhaPagamentoVO> listaConsultaSecaoFolhaPagamento) {
		this.listaConsultaSecaoFolhaPagamento = listaConsultaSecaoFolhaPagamento;
	}
	
	public String[] getFormaContratacao() {
		return formaContratacao;
	}

	public void setFormaContratacao(String[] formaContratacao) {
		this.formaContratacao = formaContratacao;
	}

	public String[] getRecebimento() {
		return recebimento;
	}

	public void setRecebimento(String[] recebimento) {
		this.recebimento = recebimento;
	}

	public String[] getSituacao() {
		return situacao;
	}

	public void setSituacao(String[] situacao) {
		this.situacao = situacao;
	}
	
	public Boolean getMarcarTodosFormaContratacao() {
		if (marcarTodosFormaContratacao == null)
			marcarTodosFormaContratacao = false;
		return marcarTodosFormaContratacao;
	}

	public void setMarcarTodosFormaContratacao(Boolean marcarTodosFormaContratacao) {
		this.marcarTodosFormaContratacao = marcarTodosFormaContratacao;
	}

	public Boolean getMarcarTodosRecebimento() {
		if (marcarTodosRecebimento == null)
			marcarTodosRecebimento = false;
		return marcarTodosRecebimento;
	}

	public void setMarcarTodosRecebimento(Boolean marcarTodosRecebimento) {
		this.marcarTodosRecebimento = marcarTodosRecebimento;
	}

	public Boolean getMarcarTodosSituacao() {
		if (marcarTodosSituacao == null)
			marcarTodosSituacao = false;
		return marcarTodosSituacao;
	}

	public void setMarcarTodosSituacao(Boolean marcarTodosSituacao) {
		this.marcarTodosSituacao = marcarTodosSituacao;
	}
	
	public List<SelectItem> getListaSelectItemFormacontratacao() {
		if (listaSelectItemFormacontratacao == null || listaSelectItemFormacontratacao.isEmpty()) {
			listaSelectItemFormacontratacao = new ArrayList<>();
			try {
				for (FormaContratacaoFuncionarioEnum formaContratacaoFuncionarioEnum : FormaContratacaoFuncionarioEnum.values()) {
					listaSelectItemFormacontratacao.add(new SelectItem(formaContratacaoFuncionarioEnum, formaContratacaoFuncionarioEnum.getDescricao()));
				}
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
		return listaSelectItemFormacontratacao;
	}

	public void setListaSelectItemFormacontratacao(List<SelectItem> listaSelectItemFormacontratacao) {
		this.listaSelectItemFormacontratacao = listaSelectItemFormacontratacao;
	}

	public List<SelectItem> getListaSelectItemRecebimento() {
		if (listaSelectItemRecebimento == null || listaSelectItemRecebimento.isEmpty()) {
			listaSelectItemRecebimento = new ArrayList<>(0);
			try {
				for (TipoRecebimentoEnum tipoRecebimentoEnum : TipoRecebimentoEnum.values()) {
					listaSelectItemRecebimento.add(new SelectItem(tipoRecebimentoEnum, tipoRecebimentoEnum.getDescricao()));
				}
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
		return listaSelectItemRecebimento;
	}

	public void setListaSelectItemRecebimento(List<SelectItem> listaSelectItemRecebimento) {
		this.listaSelectItemRecebimento = listaSelectItemRecebimento;
	}

	public List<SelectItem> getListaSelectItemSituacao() {
		if (listaSelectItemSituacao == null || listaSelectItemSituacao.isEmpty()) {
			listaSelectItemSituacao = new ArrayList<>(0);
			try {
				for (SituacaoFuncionarioEnum situacaoFuncionarioEnum : SituacaoFuncionarioEnum.values()) {
					listaSelectItemSituacao.add(new SelectItem(situacaoFuncionarioEnum, situacaoFuncionarioEnum.getDescricao()));
				}
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
		return listaSelectItemSituacao;
	}

	public void setListaSelectItemSituacao(List<SelectItem> listaSelectItemSituacao) {
		this.listaSelectItemSituacao = listaSelectItemSituacao;
	}

	public String getCampoConsultaEvento() {
		if (campoConsultaEvento == null) {
			campoConsultaEvento = "";
		}
		return campoConsultaEvento;
	}

	public void setCampoConsultaEvento(String campoConsultaEvento) {
		this.campoConsultaEvento = campoConsultaEvento;
	}

	public String getValorConsultaEvento() {
		if (valorConsultaEvento == null) {
			valorConsultaEvento = "";
		}
		return valorConsultaEvento;
	}

	public void setValorConsultaEvento(String valorConsultaEvento) {
		this.valorConsultaEvento = valorConsultaEvento;
	}

	public List<EventoFolhaPagamentoVO> getListaEventosFolhaPagamento() {
		if (listaEventosFolhaPagamento == null) {
			listaEventosFolhaPagamento = new ArrayList<>();
		}
		return listaEventosFolhaPagamento;
	}

	public void setListaEventosFolhaPagamento(List<EventoFolhaPagamentoVO> listaEventosFolhaPagamento) {
		this.listaEventosFolhaPagamento = listaEventosFolhaPagamento;
	}

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public List<FuncionarioCargoVO> getFuncionarioCargoVOs() {
		if (funcionarioCargoVOs == null) {
			funcionarioCargoVOs = new ArrayList<>();
		}
		return funcionarioCargoVOs;
	}

	public void setFuncionarioCargoVOs(List<FuncionarioCargoVO> funcionarioCargoVOs) {
		this.funcionarioCargoVOs = funcionarioCargoVOs;
	}

	public DataModelo getDataModeloCargo() {
		if (dataModeloCargo == null) {
			dataModeloCargo = new DataModelo();
		}
		return dataModeloCargo;
	}

	public void setDataModeloCargo(DataModelo dataModeloCargo) {
		this.dataModeloCargo = dataModeloCargo;
	}
	
	public List<SelectItem> getTipoConsultaComboCargo() {
		if (tipoConsultaComboCargo == null) {
			tipoConsultaComboCargo = new ArrayList<>();
			tipoConsultaComboCargo.add(new SelectItem("NOME", "Nome"));
		}
		return tipoConsultaComboCargo;
	}

	public void setTipoConsultaComboCargo(List<SelectItem> tipoConsultaComboCargo) {
		this.tipoConsultaComboCargo = tipoConsultaComboCargo;
	}
	
	public void montarListaSelectItemNivelCentroResultado() {
		try {
			getListaSelectItemNivelCentroResultado().clear();
			if (getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getApresentarFiltrosCentroResultado()) {
				Integer maiorNivel = getFacadeFactory().getCentroResultadoFacade().consultarMaiorNivelCentroResultado(getUsuarioLogado());				
				maiorNivel++;
				getListaSelectItemNivelCentroResultado().add(new SelectItem(0, ""));
				for (int i = 1; i <= maiorNivel; i++) {
					getListaSelectItemNivelCentroResultado().add(new SelectItem(i, "Nível - "+i));
				}				
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	

	public List<SelectItem> getListaSelectItemNivelCentroResultado() {
		if (listaSelectItemNivelCentroResultado == null) {
			listaSelectItemNivelCentroResultado = new ArrayList<>();
		}
		return listaSelectItemNivelCentroResultado;
	}

	public void setListaSelectItemNivelCentroResultado(List<SelectItem> listaSelectItemNivelCentroResultado) {
		this.listaSelectItemNivelCentroResultado = listaSelectItemNivelCentroResultado;
	}

	public List<SelectItem> getListaSelectItemSituacaoPeriodoAquisitivo() {
		if (listaSelectItemSituacaoPeriodoAquisitivo == null) {
			listaSelectItemSituacaoPeriodoAquisitivo = new ArrayList<>();
		}
		return listaSelectItemSituacaoPeriodoAquisitivo;
	}

	public void setListaSelectItemSituacaoPeriodoAquisitivo(List<SelectItem> listaSelectItemSituacaoPeriodoAquisitivo) {
		this.listaSelectItemSituacaoPeriodoAquisitivo = listaSelectItemSituacaoPeriodoAquisitivo;
	}

	public List<SelectItem> getListaSelectItemMotivoProgressaoSalarial() {
		if (listaSelectItemMotivoProgressaoSalarial == null) {
			listaSelectItemMotivoProgressaoSalarial = new ArrayList<SelectItem>();
		}
		return listaSelectItemMotivoProgressaoSalarial;
	}

	public void setListaSelectItemMotivoProgressaoSalarial(List<SelectItem> listaSelectItemMotivoProgressaoSalarial) {
		this.listaSelectItemMotivoProgressaoSalarial = listaSelectItemMotivoProgressaoSalarial;
	}
	
	public void limparDadosMatricula() {
		getRelatorioSEIDecidirVO().setMatriculaVO(null); 
	}
	
	public String getCampoSeparador() {
		if(campoSeparador == null){
			campoSeparador = ";";
		}
		return campoSeparador;
	}
	
	public void setCampoSeparador(String campoSeparador) {
		this.campoSeparador = campoSeparador;
}

	public String getExtensaoArquivo() {
		if(extensaoArquivo == null){
			extensaoArquivo = "xls";
		}
		return extensaoArquivo;
	}

	public void setExtensaoArquivo(String extensaoArquivo) {
		this.extensaoArquivo = extensaoArquivo;
	}
	
	public List<CampanhaVO> getListaConsultaCampanha() {
		if(listaConsultaCampanha == null){
			listaConsultaCampanha = new ArrayList<>();
		}
		return listaConsultaCampanha;
	}

	public void setListaConsultaCampanha(List<CampanhaVO> listaConsultaCampanha) {
		this.listaConsultaCampanha = listaConsultaCampanha;
	}

	public String getCampoConsultaCampanha() {
		if(campoConsultaCampanha == null){
			campoConsultaCampanha = "";
		}
		return campoConsultaCampanha;
	}

	public void setCampoConsultaCampanha(String campoConsultaCampanha) {
		this.campoConsultaCampanha = campoConsultaCampanha;
	}

	public String getValorConsultaCampanha() {
		if(valorConsultaCampanha == null){
			valorConsultaCampanha = "";
		}
		return valorConsultaCampanha;
	}

	public void setValorConsultaCampanha(String valorConsultaCampanha) {
		this.valorConsultaCampanha = valorConsultaCampanha;
	}

	public Boolean getMarcarTodasCampanhas() {
		return marcarTodasCampanhas;
	}

	public void setMarcarTodasCampanhas(Boolean marcarTodasCampanhas) {
		this.marcarTodasCampanhas = marcarTodasCampanhas;
	}

	public Boolean getMarcarTodosFuncionarios() {
		return marcarTodosFuncionarios;
	}

	public void setMarcarTodosFuncionarios(Boolean marcarTodosFuncionarios) {
		this.marcarTodosFuncionarios = marcarTodosFuncionarios;
	}
	
	public void verificarTodasFormaPagamentosSelecionadas() {
		StringBuilder formaPagamento = new StringBuilder();
		if (getRelatorioSEIDecidirVO().getFormaPagamentoVOs().size() > 1) {
			for (FormaPagamentoVO obj : getRelatorioSEIDecidirVO().getFormaPagamentoVOs()) {
				if (obj.getFiltrarFormaPagamento()) {
					formaPagamento.append(obj.getNome()).append("; ");
				} 
			}
			getRelatorioSEIDecidirVO().getFormaPagamentoVO().setNome(formaPagamento.toString());
		} else {
			if (!getRelatorioSEIDecidirVO().getFormaPagamentoVOs().isEmpty()) {
				if (getRelatorioSEIDecidirVO().getFormaPagamentoVOs().get(0).getFiltrarFormaPagamento()) {
					getRelatorioSEIDecidirVO().getFormaPagamentoVO().setNome(getRelatorioSEIDecidirVO().getFormaPagamentoVOs().get(0).getNome());
				}
			} else {
				getRelatorioSEIDecidirVO().getFormaPagamentoVO().setNome(formaPagamento.toString());
			}
		}		
	}
	
	public void marcarTodasFormaPagamentosRelatorioAction() {
		for (FormaPagamentoVO unidade : getRelatorioSEIDecidirVO().getFormaPagamentoVOs()) {
			if (getMarcarTodasFormaPagamento()) {
				unidade.setFiltrarFormaPagamento(Boolean.TRUE);
			} else {
				unidade.setFiltrarFormaPagamento(Boolean.FALSE);
			}
		}
		verificarTodasFormaPagamentosSelecionadas();
	}
	
	public DataModelo getDataModeloPlanoOrcamentario() {
		if (dataModeloPlanoOrcamentario == null) {
			dataModeloPlanoOrcamentario = new DataModelo();
		}
 		return dataModeloPlanoOrcamentario;
	}

	public void setDataModeloPlanoOrcamentario(DataModelo dataModeloPlanoOrcamentario) {
		this.dataModeloPlanoOrcamentario = dataModeloPlanoOrcamentario;
	}

	public String getCampoConsultaDepartamento() {
		if (campoConsultaDepartamento == null) {
			campoConsultaDepartamento = "";
		}
		return campoConsultaDepartamento;
	}

	public void setCampoConsultaDepartamento(String campoConsultaDepartamento) {
		this.campoConsultaDepartamento = campoConsultaDepartamento;
	}

	public String getValorConsultaDepartamento() {
		if (valorConsultaDepartamento == null) {
			valorConsultaDepartamento = "";
		}
		return valorConsultaDepartamento;
	}

	public void setValorConsultaDepartamento(String valorConsultaDepartamento) {
		this.valorConsultaDepartamento = valorConsultaDepartamento;
	}

	public List<DepartamentoVO> getListaConsultaDepartamento() {
		if (listaConsultaDepartamento == null) {
			listaConsultaDepartamento = new ArrayList<DepartamentoVO>(0);
		}
		return listaConsultaDepartamento;
	}

	public void setListaConsultaDepartamento(List<DepartamentoVO> listaConsultaDepartamento) {
		this.listaConsultaDepartamento = listaConsultaDepartamento;
	}

	public String getCampoConsultaCategoriaDespesa() {
		if (campoConsultaCategoriaDespesa == null) {
			campoConsultaCategoriaDespesa = "";
		}
		return campoConsultaCategoriaDespesa;
	}

	public void setCampoConsultaCategoriaDespesa(String campoConsultaCategoriaDespesa) {
		this.campoConsultaCategoriaDespesa = campoConsultaCategoriaDespesa;
	}

	public String getValorConsultaCategoriaDespesa() {
		if (valorConsultaCategoriaDespesa == null) {
			valorConsultaCategoriaDespesa = "";
		}
		return valorConsultaCategoriaDespesa;
	}

	public void setValorConsultaCategoriaDespesa(String valorConsultaCategoriaDespesa) {
		this.valorConsultaCategoriaDespesa = valorConsultaCategoriaDespesa;
	}

	public List<CategoriaDespesaVO> getListaConsultaCategoriaDespesa() {
		if (listaConsultaCategoriaDespesa == null) {
			listaConsultaCategoriaDespesa = new ArrayList<>();
		}
		return listaConsultaCategoriaDespesa;
	}

	public void setListaConsultaCategoriaDespesa(List<CategoriaDespesaVO> listaConsultaCategoriaDespesa) {
		this.listaConsultaCategoriaDespesa = listaConsultaCategoriaDespesa;
	}
	
	public String getCampoConsultaCategoriaProduto() {
		if (campoConsultaCategoriaProduto == null) {
			campoConsultaCategoriaProduto = "";
		}
		return campoConsultaCategoriaProduto;
	}

	public void setCampoConsultaCategoriaProduto(String campoConsultaCategoriaProduto) {
		this.campoConsultaCategoriaProduto = campoConsultaCategoriaProduto;
	}

	public List<CategoriaProdutoVO> getListaConsultaCategoriaProduto() {
		if (listaConsultaCategoriaProduto == null) {
			listaConsultaCategoriaProduto = new ArrayList<>();
		}
		return listaConsultaCategoriaProduto;
	}

	public void setListaConsultaCategoriaProduto(List<CategoriaProdutoVO> listaConsultaCategoriaProduto) {
		this.listaConsultaCategoriaProduto = listaConsultaCategoriaProduto;
	}

	public String getValorConsultaCategoriaProduto() {
		if (valorConsultaCategoriaProduto == null) {
			valorConsultaCategoriaProduto = "";
		}
		return valorConsultaCategoriaProduto;
	}

	public void setValorConsultaCategoriaProduto(String valorConsultaCategoriaProduto) {
		this.valorConsultaCategoriaProduto = valorConsultaCategoriaProduto;
	}

	public SituacaoEnum getSituacaoEnumFiltro() {
		if (situacaoEnumFiltro == null) {
			situacaoEnumFiltro = SituacaoEnum.ATIVO;
		}
		return situacaoEnumFiltro;
	}

	public void setSituacaoEnumFiltro(SituacaoEnum situacaoEnumFiltro) {
		this.situacaoEnumFiltro = situacaoEnumFiltro;
	}

	public DataModelo getDataModeloCentroResultado() {
		if (dataModeloCentroResultado == null) {
			dataModeloCentroResultado = new DataModelo();
		}
		return dataModeloCentroResultado;
	}

	public void setDataModeloCentroResultado(DataModelo dataModeloCentroCusto) {
		this.dataModeloCentroResultado = dataModeloCentroCusto;
	}

	public String getPlanoOrcamentarioApresentar() {
		if (planoOrcamentarioApresentar == null) {
			planoOrcamentarioApresentar = "";
		}
		return planoOrcamentarioApresentar;
	}

	public void setPlanoOrcamentarioApresentar(String planoOrcamentarioApresentar) {
		this.planoOrcamentarioApresentar = planoOrcamentarioApresentar;
	}

	public String getDepartamentoApresentar() {
		if (departamentoApresentar == null) {
			departamentoApresentar = "";
		}
		return departamentoApresentar;
	}

	public void setDepartamentoApresentar(String departamentoApresentar) {
		this.departamentoApresentar = departamentoApresentar;
	}

	public String getCategoriaProdutoApresentar() {
		if (categoriaProdutoApresentar == null) {
			categoriaProdutoApresentar = "";
		}
		return categoriaProdutoApresentar;
	}

	public void setCategoriaProdutoApresentar(String categoriaProdutoApresentar) {
		this.categoriaProdutoApresentar = categoriaProdutoApresentar;
	}

	/*public String getCentroResultadoApresentar() {
		if (centroResultadoApresentar == null) { 
			centroResultadoApresentar = "";
		}
		return centroResultadoApresentar;
	}

	public void setCentroResultadoApresentar(String centroResultadoApresentar) {
		this.centroResultadoApresentar = centroResultadoApresentar;
	}*/

	public List<PlanoOrcamentarioVO> getPlanosOrcamentarios() {
		if (planosOrcamentarios == null) {
			planosOrcamentarios = new ArrayList<>();
		}
		return planosOrcamentarios;
	}

	public void setPlanosOrcamentarios(List<PlanoOrcamentarioVO> planosOrcamentarios) {
		this.planosOrcamentarios = planosOrcamentarios;
	}

	public Boolean getMarcarTodosPlanoOrcamentario() {
		if (marcarTodosPlanoOrcamentario == null) {
			marcarTodosPlanoOrcamentario = Boolean.TRUE;
		}
		return marcarTodosPlanoOrcamentario;
	}

	public void setMarcarTodosPlanoOrcamentario(Boolean marcarTodosPlanoOrcamentario) {
		this.marcarTodosPlanoOrcamentario = marcarTodosPlanoOrcamentario;
	}

	public Boolean getMarcarTodosDepartamento() {
		if (marcarTodosDepartamento == null) {
			marcarTodosDepartamento = Boolean.FALSE;
		}
		return marcarTodosDepartamento;
	}

	public void setMarcarTodosDepartamento(Boolean marcarTodosDepartamento) {
		this.marcarTodosDepartamento = marcarTodosDepartamento;
	}

	public Boolean getMarcarTodasCategoriaDespesa() {
		if (marcarTodasCategoriaDespesa == null) {
			marcarTodasCategoriaDespesa = Boolean.FALSE;
		}
		return marcarTodasCategoriaDespesa;
	}

	public void setMarcarTodasCategoriaDespesa(Boolean marcarTodasCategoriaDespesa) {
		this.marcarTodasCategoriaDespesa = marcarTodasCategoriaDespesa;
	}

	public Boolean getMarcarTodasCategoriaProduto() {
		if (marcarTodasCategoriaProduto == null) {
			marcarTodasCategoriaProduto = Boolean.FALSE;
		}
		return marcarTodasCategoriaProduto;
	}

	public void setMarcarTodasCategoriaProduto(Boolean marcarTodasCategoriaProduto) {
		this.marcarTodasCategoriaProduto = marcarTodasCategoriaProduto;
	}

	public Boolean getMarcarTodosCentroResultado() {
		if (marcarTodosCentroResultado == null) {
			marcarTodosCentroResultado = Boolean.FALSE;
		}
		return marcarTodosCentroResultado;
	}

	public void setMarcarTodosCentroResultado(Boolean marcarTodosCentroResultado) {
		this.marcarTodosCentroResultado = marcarTodosCentroResultado;
	}

	/*public List<CentroResultadoVO> getListaConsultaCentroResultado() {
		if (listaConsultaCentroResultado == null) {
			listaConsultaCentroResultado = new ArrayList<>();
		}
		return listaConsultaCentroResultado;
	}

	public void setListaConsultaCentroResultado(List<CentroResultadoVO> listaConsultaCentroResultado) {
		this.listaConsultaCentroResultado = listaConsultaCentroResultado;
	}*/

	public void consultarDisciplina() {
        try {
            List objs = new ArrayList<>(0);
            if (getCampoConsultaDisciplina().equals("codigo")) {
                if (getValorConsultaDisciplina().equals("")) {
                    setValorConsultaDisciplina("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaDisciplina());
                objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(valorInt, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaDisciplina().equals("nome")) {
                objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaDisciplina().equals("abreviatura")) {
                objs = getFacadeFactory().getDisciplinaFacade().consultarPorAbreviatura(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaDisciplina(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaDisciplina(new ArrayList<>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void selecionarDisciplina() throws Exception {
        DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
        getRelatorioSEIDecidirVO().setDisciplinaVO(obj);
    }
    
    public void limparDisciplina() {
    	getRelatorioSEIDecidirVO().setDisciplinaVO(null);
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

    public List getListaConsultaDisciplina() {
        if (listaConsultaDisciplina == null) {
            listaConsultaDisciplina = new ArrayList<>(0);
        }
        return listaConsultaDisciplina;
    }

    public void setListaConsultaDisciplina(List listaConsultaDisciplina) {
        this.listaConsultaDisciplina = listaConsultaDisciplina;
    }

	public String getCampoConsultaTurmaEstudouDisciplina() {
		if (campoConsultaTurmaEstudouDisciplina == null) {
			campoConsultaTurmaEstudouDisciplina = "";
		}
		return campoConsultaTurmaEstudouDisciplina;
	}

	public void setCampoConsultaTurmaEstudouDisciplina(String campoConsultaTurmaEstudouDisciplina) {
		this.campoConsultaTurmaEstudouDisciplina = campoConsultaTurmaEstudouDisciplina;
	}

	public String getValorConsultaTurmaEstudouDisciplina() {
		if (valorConsultaTurmaEstudouDisciplina == null) {
			valorConsultaTurmaEstudouDisciplina = "";
		}
		return valorConsultaTurmaEstudouDisciplina;
	}

	public void setValorConsultaTurmaEstudouDisciplina(String valorConsultaTurmaEstudouDisciplina) {
		this.valorConsultaTurmaEstudouDisciplina = valorConsultaTurmaEstudouDisciplina;
	}

	public List<TurmaVO> getListaConsultaTurmaEstudouDisciplinaVOs() {
		if (listaConsultaTurmaEstudouDisciplinaVOs == null) {
			listaConsultaTurmaEstudouDisciplinaVOs = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurmaEstudouDisciplinaVOs;
	}

	public void setListaConsultaTurmaEstudouDisciplinaVOs(List<TurmaVO> listaConsultaTurmaEstudouDisciplinaVOs) {
		this.listaConsultaTurmaEstudouDisciplinaVOs = listaConsultaTurmaEstudouDisciplinaVOs;
	}
	
	public List<SelectItem> getTipoConsultaComboDisciplina() {
        if (tipoConsultaComboDisciplina == null) {
            tipoConsultaComboDisciplina = new ArrayList<SelectItem>(0);
            tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
            tipoConsultaComboDisciplina.add(new SelectItem("abreviatura", "Abreviatura"));
            tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
        }
        return tipoConsultaComboDisciplina;
    }
	
	public Boolean getMarcarTodasSituacoesRequerimento() {
		if (marcarTodasSituacoesRequerimento == null) {
			marcarTodasSituacoesRequerimento = false;
		}
		return marcarTodasSituacoesRequerimento;
	}

	public void setMarcarTodasSituacoesRequerimento(Boolean marcarTodasSituacoesRequerimento) {
		this.marcarTodasSituacoesRequerimento = marcarTodasSituacoesRequerimento;
	}

	public void realizarSelecaoCheckboxMarcarDesmarcarTodasSituacoesRequerimento() {
		realizarSelecionarTodosSituacoesRequerimento(getMarcarTodasSituacoesRequerimento());
	}
	
	public void realizarSelecionarTodosSituacoesRequerimento(boolean selecionado){
		if(getRelatorioSEIDecidirVO().getFiltrarPeriodoPor().equals("dtConclusao")) {
			getRelatorioSEIDecidirVO().setFinalizadoDeferido(selecionado);
			getRelatorioSEIDecidirVO().setFinalizadoIndeferido(selecionado);
		}
		else if(getRelatorioSEIDecidirVO().getFiltrarPeriodoPor().equals("dtPrevisaoConclusao")) {
			getRelatorioSEIDecidirVO().setEmExecucao(selecionado);
			getRelatorioSEIDecidirVO().setPendente(selecionado);
			getRelatorioSEIDecidirVO().setProntoRetirada(selecionado);
			getRelatorioSEIDecidirVO().setAtrasado(selecionado);	
		}
		else {
			getRelatorioSEIDecidirVO().setFinalizadoDeferido(selecionado);
			getRelatorioSEIDecidirVO().setFinalizadoIndeferido(selecionado);
			getRelatorioSEIDecidirVO().setEmExecucao(selecionado);
			getRelatorioSEIDecidirVO().setPendente(selecionado);
			getRelatorioSEIDecidirVO().setProntoRetirada(selecionado);
			getRelatorioSEIDecidirVO().setAtrasado(selecionado);	
		}

	}
	
	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodasSituacoesRequerimento() {
		if (getMarcarTodasSituacoesRequerimento()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}
	
	public Boolean getFiltrarPeriodoPorDataPrevisaoConclusao() {
		if(getRelatorioSEIDecidirVO().getFiltrarPeriodoPor().equals("dtPrevisaoConclusao")) {
			getRelatorioSEIDecidirVO().setFinalizadoDeferido(false);
			getRelatorioSEIDecidirVO().setFinalizadoIndeferido(false);
			return true;
		}
		return false;
	}
	
	public void limparFuncionario() throws Exception {
		try {
			getRelatorioSEIDecidirVO().setFuncionarioVO(null);
			setValorConsultaFuncionario(null);
			setListaConsultaFuncionario(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparTurmaReposicao() {
		try {
			getRelatorioSEIDecidirVO().setTurmaReposicao(new TurmaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarTurmaReposicao() {
		try {			
			setListaConsultaTurmaReposicao(getFacadeFactory().getTurmaFacade().consultar(getCampoConsultaTurmaReposicao(), getValorConsultaTurmaReposicao(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurmaReposicao(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarTurmaReposicao() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			getRelatorioSEIDecidirVO().setTurmaReposicao(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String getCampoConsultaTurmaReposicao() {
		if(campoConsultaTurmaReposicao == null) {
			campoConsultaTurmaReposicao = "";
		}
		return campoConsultaTurmaReposicao;
	}

	public void setCampoConsultaTurmaReposicao(String campoConsultaTurmaReposicao) {
		this.campoConsultaTurmaReposicao = campoConsultaTurmaReposicao;
	}

	public String getValorConsultaTurmaReposicao() {
		if(valorConsultaTurmaReposicao == null) {
			valorConsultaTurmaReposicao = "";
		}
		return valorConsultaTurmaReposicao;
	}

	public void setValorConsultaTurmaReposicao(String valorConsultaTurmaReposicao) {
		this.valorConsultaTurmaReposicao = valorConsultaTurmaReposicao;
	}
	
	public List<TurmaVO> getListaConsultaTurmaReposicao() {
		if(listaConsultaTurmaReposicao == null) {
			listaConsultaTurmaReposicao = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurmaReposicao;
	}

	public void setListaConsultaTurmaReposicao(List<TurmaVO> listaConsultaTurmaReposicao) {
		this.listaConsultaTurmaReposicao = listaConsultaTurmaReposicao;
	}

	public void limparRequerente() throws Exception {
		try {
			getRelatorioSEIDecidirVO().setRequerenteVO(null);
			setValorConsultaRequerente(null);
			setListaConsultaRequerente(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarRequerente() {
		try {
			super.consultar();
			List<PessoaVO> objs = new ArrayList<PessoaVO>(0);
			if (getCampoConsultaRequerente().equals("nome")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaRequerente(), "", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaRequerente().equals("cpf")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaRequerente(), "", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaRequerente(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarRequerente() {
		getRelatorioSEIDecidirVO().setRequerenteVO((PessoaVO) context().getExternalContext().getRequestMap().get("RequerenteItens"));
	}
	
	public String getCampoConsultaRequerente() {
		if (campoConsultaRequerente == null) {
			campoConsultaRequerente = "";
		}
		return campoConsultaRequerente;
	}

	public void setCampoConsultaRequerente(String campoConsultaRequerente) {
		this.campoConsultaRequerente = campoConsultaRequerente;
	}

	public String getValorConsultaRequerente() {
		if (valorConsultaRequerente == null) {
			valorConsultaRequerente = "";
		}
		return valorConsultaRequerente;
	}

	public void setValorConsultaRequerente(String valorConsultaRequerente) {
		this.valorConsultaRequerente = valorConsultaRequerente;
	}

	public List<PessoaVO> getListaConsultaRequerente() {
		if (listaConsultaRequerente == null) {
			listaConsultaRequerente = new ArrayList<>();
		}
		return listaConsultaRequerente;
	}

	public void setListaConsultaRequerente(List<PessoaVO> listaConsultaRequerente) {
		this.listaConsultaRequerente = listaConsultaRequerente;
	}
	
	public void consultarCoordenador() {
		try {
			super.consultar();
			List<PessoaVO> objs = new ArrayList<PessoaVO>(0);
			if (getCampoConsultaCoordenador().equals("nome")) {
				objs = getFacadeFactory().getPessoaFacade().consultaCoordenadorPorNome(getValorConsultaCoordenador(), getUnidadeEnsinoVOs(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaCoordenador(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarCoordenador() {
		getRelatorioSEIDecidirVO().setCoordenadorVO((PessoaVO) context().getExternalContext().getRequestMap().get("coordenadorItens"));
	}
	
	public List<SelectItem> getTipoConsultaComboCoordenador() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}
	
	
	public void limparCoordenador() throws Exception {
		try {
			getRelatorioSEIDecidirVO().setCoordenadorVO(null);
			setValorConsultaCoordenador(null);
			setListaConsultaCoordenador(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String getCampoConsultaCoordenador() {
		if (campoConsultaCoordenador == null) {
			campoConsultaCoordenador = "";
		}
		return campoConsultaCoordenador;
	}

	public void setCampoConsultaCoordenador(String campoConsultaCoordenador) {
		this.campoConsultaCoordenador = campoConsultaCoordenador;
	}

	public String getValorConsultaCoordenador() {
		if (valorConsultaCoordenador == null) {
			valorConsultaCoordenador = "";
		}
		return valorConsultaCoordenador;
	}

	public void setValorConsultaCoordenador(String valorConsultaCoordenador) {
		this.valorConsultaCoordenador = valorConsultaCoordenador;
	}

	public List<PessoaVO> getListaConsultaCoordenador() {
		if (listaConsultaCoordenador == null) {
			listaConsultaCoordenador = new ArrayList<>();
		}
		return listaConsultaCoordenador;
	}

	public void setListaConsultaCoordenador(List<PessoaVO> listaConsultaCoordenador) {
		this.listaConsultaCoordenador = listaConsultaCoordenador;
	}
	
	public void inicializarListaSelectItemDepartamento() {
		try {
			getListaSelectItemDepartamentoResponsavel().clear();
			List<DepartamentoVO> departamentoVOs = getFacadeFactory().getDepartamentoFacade().consultarDepartamentoRequerimento(getRelatorioSEIDecidirVO().getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getListaSelectItemDepartamentoResponsavel().add(new SelectItem(0, ""));
			for (DepartamentoVO departamentoVO : departamentoVOs) {
				getListaSelectItemDepartamentoResponsavel().add(new SelectItem(departamentoVO.getCodigo(), departamentoVO.getNome()));
			}
		} catch (Exception e) {

		}
	}

	public List<SelectItem> getListaSelectItemDepartamentoResponsavel() {
		if (listaSelectItemDepartamentoResponsavel == null) {
			listaSelectItemDepartamentoResponsavel = new ArrayList<SelectItem>(0);
			inicializarListaSelectItemDepartamento();
		}
		return listaSelectItemDepartamentoResponsavel;
	}

	public void setListaSelectItemDepartamentoResponsavel(List<SelectItem> listaSelectItemDepartamentoResponsavel) {
		this.listaSelectItemDepartamentoResponsavel = listaSelectItemDepartamentoResponsavel;
	}
	
	public List<SelectItem> getListaSelectItemSituacaoRequerimentoDepartamentoCons() {
		if (listaSelectItemSituacaoRequerimentoDepartamentoCons == null) {
			listaSelectItemSituacaoRequerimentoDepartamentoCons = new ArrayList<SelectItem>();
			try {
				List<SituacaoRequerimentoDepartamentoVO> situacaoRequerimentoDepartamentoVOs =  getFacadeFactory().getSituacaoRequerimentoDepartamentoFacade().consultarPorSituacao("", getUsuarioLogado());
				listaSelectItemSituacaoRequerimentoDepartamentoCons = UtilSelectItem.getListaSelectItem(situacaoRequerimentoDepartamentoVOs, "codigo", "situacao");
			} catch (Exception e) {				
				e.printStackTrace();
			}
		}
		return listaSelectItemSituacaoRequerimentoDepartamentoCons;
	}

	public void setListaSelectItemSituacaoRequerimentoDepartamentoCons(List<SelectItem> listaSelectItemSituacaoRequerimentoDepartamentoCons) {
		this.listaSelectItemSituacaoRequerimentoDepartamentoCons = listaSelectItemSituacaoRequerimentoDepartamentoCons;
	}
	
	public List<SelectItem> getFiltrarPeriodoPorCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("dtAbertura", "Data Abertura"));
		itens.add(new SelectItem("dtConclusao", "Data Conclusão"));
		itens.add(new SelectItem("dtAtendimento", "Data Atendimento"));
		itens.add(new SelectItem("dtPrevisaoConclusao", "Data Previsão Conclusão"));
		return itens;
	}
	
	public Boolean getFiltrarPeriodoPorDataConclusao() {
		if(getRelatorioSEIDecidirVO().getFiltrarPeriodoPor().equals("dtConclusao")) {
			getRelatorioSEIDecidirVO().setEmExecucao(false);
			getRelatorioSEIDecidirVO().setPendente(false);
			getRelatorioSEIDecidirVO().setProntoRetirada(false);
			getRelatorioSEIDecidirVO().setAtrasado(false);	
			return true;
		}
		return false;
	}
	
	public List<SelectItem> getTipoConsultaComboRequerente() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("cpf", "CPF"));
		return itens;
	}
	
	public Boolean getApresentarCampoCpf() {
		return getCampoConsultaRequerente().equals("cpf");
	}

	public String getCampoConsultaTipoRequerimento() {
		if (campoConsultaTipoRequerimento == null) {
			campoConsultaTipoRequerimento = "";
		}
		return campoConsultaTipoRequerimento;
	}

	public void setCampoConsultaTipoRequerimento(String campoConsultaTipoRequerimento) {
		this.campoConsultaTipoRequerimento = campoConsultaTipoRequerimento;
	}

	public List<TipoRequerimentoVO> getListaConsultaTipoRequerimento() {
		if (listaConsultaTipoRequerimento == null) {
			listaConsultaTipoRequerimento = new ArrayList<TipoRequerimentoVO>(0);
		}
		return listaConsultaTipoRequerimento;
	}

	public void setListaConsultaTipoRequerimento(List<TipoRequerimentoVO> listaConsultaTipoRequerimento) {
		this.listaConsultaTipoRequerimento = listaConsultaTipoRequerimento;
	}

	public String getValorConsultaTipoRequerimento() {
		if (valorConsultaTipoRequerimento == null) {
			valorConsultaTipoRequerimento = "";
		}
		return valorConsultaTipoRequerimento;
	}

	public void setValorConsultaTipoRequerimento(String valorConsultaTipoRequerimento) {
		this.valorConsultaTipoRequerimento = valorConsultaTipoRequerimento;
	}

	public String consultarTipoRequerimento() {
		try {
			super.consultar();
			List<TipoRequerimentoVO> objs = new ArrayList<TipoRequerimentoVO>(0);
			if (getCampoConsultaTipoRequerimento().equals("codigo")) {
				if (getValorConsultaTipoRequerimento().equals("")) {
					setValorConsultaTipoRequerimento("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaTipoRequerimento());
				objs = getFacadeFactory().getTipoRequerimentoFacade().consultarPorCodigo(new Integer(valorInt) , "TO", getUnidadeEnsinoLogado().getCodigo(),  Uteis.NIVELMONTARDADOS_COMBOBOX, false,  getUsuarioLogado());
			}
			if (getCampoConsultaTipoRequerimento().equals("nome")) {
				objs = getFacadeFactory().getTipoRequerimentoFacade().consultarPorNome(getValorConsultaTipoRequerimento(), "TO", getUnidadeEnsinoLogado().getCodigo(),  Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
			}
			if (getCampoConsultaTipoRequerimento().equals("valor")) {
				if (getValorConsultaTipoRequerimento().equals("")) {
					setValorConsultaTipoRequerimento("0");
				}
				double valorDouble = Double.parseDouble(getValorConsultaTipoRequerimento());
				objs = getFacadeFactory().getTipoRequerimentoFacade().consultarPorValor(new Double(valorDouble),  Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
			}
			if (getCampoConsultaTipoRequerimento().equals("prazoExecucao")) {
				if (getValorConsultaTipoRequerimento().equals("")) {
					setValorConsultaTipoRequerimento("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaTipoRequerimento());
				objs = getFacadeFactory().getTipoRequerimentoFacade().consultarPorPrazoExecucao(new Integer(valorInt), "TO",  getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
			}
			if (getCampoConsultaTipoRequerimento().equals("nomeDepartamento")) {
				objs = getFacadeFactory().getTipoRequerimentoFacade().consultarPorNomeDepartamento(getValorConsultaTipoRequerimento(), "TO", getUnidadeEnsinoLogado().getCodigo(),  Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
			}
			setListaConsultaTipoRequerimento(objs);
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			if (e instanceof NumberFormatException) {
				setMensagemDetalhada("msg_erro", "Informe apenas números.");
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
			setListaConsultaTipoRequerimento(new ArrayList<TipoRequerimentoVO>(0));
			return "consultar";
		}
	}

	public void selecionarTipoRequerimento() throws Exception {
		TipoRequerimentoVO obj = (TipoRequerimentoVO) context().getExternalContext().getRequestMap().get("tipoRequerimentoItens");
		getRelatorioSEIDecidirVO().setTipoRequerimentoVO(obj);
		obj = null;
		setValorConsultaTipoRequerimento("");
		setCampoConsultaTipoRequerimento("");
		getListaConsultaTipoRequerimento().clear();
	}

	public void limparConsultaTipoRequerimento() {
		setValorConsultaTipoRequerimento(null);
		setCampoConsultaTipoRequerimento("");
		setListaConsultaTipoRequerimento(null);
		getRelatorioSEIDecidirVO().setTipoRequerimentoVO(null);
		setMensagemID("msg_entre_prmconsulta");
	}
	
	public List<SelectItem> getTipoConsultaComboTipoRequerimento() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Codigo"));
		itens.add(new SelectItem("prazoExecucao", "Prazo Execução"));
		return itens;
	}

	public List<SelectItem> getTipoSituacaoRequerimento() {
		List<SelectItem> objs = new ArrayList<SelectItem>();
		objs.add(new SelectItem("", ""));
		objs.add(new SelectItem("FD", "Finalizado - Deferido"));
		objs.add(new SelectItem("FI", "Finalizado - Indeferido"));
		objs.add(new SelectItem("EX", "Em Execução"));
		objs.add(new SelectItem("PE", "Pendente"));
		objs.add(new SelectItem("AP", "Aguardando Pagamento"));
		objs.add(new SelectItem("IS", "Isento"));
		objs.add(new SelectItem("PR", "Pronto para Retirada"));
		return objs;
	}
	
	public boolean getIsApresentraUnidadeEnsinoCurso() {
		return getRelatorioSEIDecidirVO().getProcSeletivoVO().getCodigo() != 0 && 
				(getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.INSCRICAO)
				|| getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.PROCESSO_SELETIVO_CURSO));
	}
	
	public void realizarSelecaoCheckboxMarcarDesmarcarTodosSituacaoInscricao() {
		if (getMarcarTodasSituacoesInscricao()) {
			getRelatorioSEIDecidirVO().getFiltroRelatorioProcessoSeletivoVO().realizarMarcarTodasSituacoes();
		} else {
			getRelatorioSEIDecidirVO().getFiltroRelatorioProcessoSeletivoVO().realizarDesmarcarTodasSituacoes();
		}
	}
    
    public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodos() {
		if (getRelatorioSEIDecidirVO().getMarcarTodasSituacoesInscricao()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}

    public void montarListaUltimosProcSeletivos(){
		try {
			setListaConsultaProcSeletivo(getFacadeFactory().getProcSeletivoFacade().consultarUltimosProcessosSeletivos(5, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
		} catch (Exception e) {
			setListaConsultaProcSeletivo(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<ProcSeletivoVO> getListaConsultaProcSeletivo() {
		if (listaConsultaProcSeletivo == null) {
			listaConsultaProcSeletivo = new ArrayList<ProcSeletivoVO>(0);
		}
		return listaConsultaProcSeletivo;
	}

	public void setListaConsultaProcSeletivo(List<ProcSeletivoVO> listaConsultaProcSeletivo) {
		this.listaConsultaProcSeletivo = listaConsultaProcSeletivo;
	}
	
	public void limparDados() {
		getRelatorioSEIDecidirVO().getProcSeletivoVO().setCodigo(0);
		getRelatorioSEIDecidirVO().getProcSeletivoVO().setDescricao("");
		
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

	public List<SelectItem> getListaSelectItemDataProva() {
		if (listaSelectItemDataProva == null) {
			listaSelectItemDataProva = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDataProva;
	}

	public void setListaSelectItemDataProva(List<SelectItem> listaSelectItemDataProva) {
		this.listaSelectItemDataProva = listaSelectItemDataProva;
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
			listaConsultaCurso = new ArrayList<>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public void consultarCurso() {
		try {
			setListaConsultaCurso(getFacadeFactory().getProcSeletivoInscricoesRelFacade().consultarCurso(getCampoConsultaCurso(), getValorConsultaCurso(), getRelatorioSEIDecidirVO().getUnidadeEnsinoCursoVO().getUnidadeEnsino(), getRelatorioSEIDecidirVO().getProcSeletivoVO().getCodigo(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}
	
	public void selecionarCurso() {
		try {
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			getRelatorioSEIDecidirVO().setUnidadeEnsinoCursoVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparDadosCurso() {
		getRelatorioSEIDecidirVO().getUnidadeEnsinoCursoVO().setCodigo(0);
		getRelatorioSEIDecidirVO().getUnidadeEnsinoCursoVO().getCurso().setCodigo(0);
		getRelatorioSEIDecidirVO().getUnidadeEnsinoCursoVO().getCurso().setNome("");
		getRelatorioSEIDecidirVO().getUnidadeEnsinoCursoVO().getTurno().setCodigo(0);
		getRelatorioSEIDecidirVO().getUnidadeEnsinoCursoVO().getTurno().setNome("");
	}

	public String getValorConsultaProcSeletivo() {
		if (valorConsultaProcSeletivo == null) {
			valorConsultaProcSeletivo = "";
		}
		return valorConsultaProcSeletivo;
	}

	public void setValorConsultaProcSeletivo(String valorConsultaProcSeletivo) {
		this.valorConsultaProcSeletivo = valorConsultaProcSeletivo;
	}

	public String getCampoConsultaProcSeletivo() {
		if (campoConsultaProcSeletivo == null) {
			campoConsultaProcSeletivo = "";
		}
		return campoConsultaProcSeletivo;
	}

	public void setCampoConsultaProcSeletivo(String campoConsultaProcSeletivo) {
		this.campoConsultaProcSeletivo = campoConsultaProcSeletivo;
	}

	public List getTipoConsultaComboProcSeletivo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("dataInicio", "Data Início"));
		itens.add(new SelectItem("dataFim", "Data Fim"));
		itens.add(new SelectItem("dataProva", "Data Prova"));
		return itens;
	}
	
	public String getMascaraConsultaProcSeletivo() {
		if (getCampoConsultaProcSeletivo().equals("dataInicio") || getCampoConsultaProcSeletivo().equals("dataFim") || getCampoConsultaProcSeletivo().equals("dataProva")) {
			return "return mascara(this.form,'this.id','99/99/9999',event);";
		}
		return "";
	}

	public void consultarProcSeletivo() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaProcSeletivo().equals("descricao")) {
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino(getValorConsultaProcSeletivo(), getUnidadeEnsinoLogado().getCodigo(),false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcSeletivo().equals("dataInicio")) {
				Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataInicioUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(),false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcSeletivo().equals("dataFim")) {
				Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataFimUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(),false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcSeletivo().equals("dataProva")) {
				Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataProvaUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(),false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaProcSeletivo(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaProcSeletivo(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	
	public void selecionarProcSeletivo() {
		ProcSeletivoVO obj = (ProcSeletivoVO) context().getExternalContext().getRequestMap().get("procSeletivoItens");
		getRelatorioSEIDecidirVO().setProcSeletivoVO(obj);
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemSituacaoFinanceiraCandidato();
		montarListaSelectItemDataProva();
		getRelatorioSEIDecidirVO().getInscricaoVO().setSituacao("AM");
		getRelatorioSEIDecidirVO().setSituacaoResultadoProcessoSeletivo(SituacaoResultadoProcessoSeletivoEnum.TODOS);
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}
	
	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List<ProcSeletivoUnidadeEnsinoVO>  resultadoConsulta = null;
		Iterator<ProcSeletivoUnidadeEnsinoVO>  i = null;
		try {
			if (getRelatorioSEIDecidirVO().getProcSeletivoVO().getCodigo().intValue() == 0) {
				setListaSelectItemUnidadeEnsino(new ArrayList<SelectItem>(0));
				return;
			}			
			if(!Uteis.isAtributoPreenchido(getUnidadeEnsinoLogado())) {
				resultadoConsulta = consultarProcessoSeletivoUnidadeEnsino();
				List<SelectItem> objs = new ArrayList<SelectItem>(0);
				if(!Uteis.isAtributoPreenchido(getUnidadeEnsinoLogado())) {
					objs.add(new SelectItem(0, ""));
				}				
				i = resultadoConsulta.iterator();
				// Colocar no mapa o filtro por chamada
				while (i.hasNext()) {
					ProcSeletivoUnidadeEnsinoVO proc = (ProcSeletivoUnidadeEnsinoVO) i.next();
					ProcSeletivoUnidadeEnsino.montarDadosUnidadeEnsino(proc, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
					objs.add(new SelectItem(proc.getUnidadeEnsino().getCodigo(), proc.getUnidadeEnsino().getNome()));
				}					
				setListaSelectItemUnidadeEnsino(objs);
				Ordenacao.ordenarLista(getListaSelectItemUnidadeEnsino(), "label");
			}else {
				getListaSelectItemUnidadeEnsino().clear();
				getListaSelectItemUnidadeEnsino().add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getNome()));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}
	
	public List<ProcSeletivoUnidadeEnsinoVO> consultarProcessoSeletivoUnidadeEnsino() throws Exception {
		List<ProcSeletivoUnidadeEnsinoVO>  lista = ProcSeletivoUnidadeEnsino.consultarProcSeletivoUnidadeEnsinos(getRelatorioSEIDecidirVO().getProcSeletivoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());		
		return lista;
	}
	
	public void montarListaSelectItemSituacaoFinanceiraCandidato() {
		getListaSelectItemSituacaoFinanceiraInscricao().clear();
		getListaSelectItemSituacaoFinanceiraInscricao().add(new SelectItem("AM", "Ambos"));
		getListaSelectItemSituacaoFinanceiraInscricao().add(new SelectItem("CO", "Confirmado"));
		getListaSelectItemSituacaoFinanceiraInscricao().add(new SelectItem("PF", "Pendente Financeiramente"));

	}
	
	public void montarListaSelectItemDataProva() {
		try {
			List<ItemProcSeletivoDataProvaVO> itemProcSeletivoDataProvaVOs = getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorCodigoProcessoSeletivo(getRelatorioSEIDecidirVO().getProcSeletivoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getListaSelectItemDataProva().clear();
			getListaSelectItemDataProva().add(new SelectItem(0, ""));
			for (ItemProcSeletivoDataProvaVO obj : itemProcSeletivoDataProvaVOs) {
				getListaSelectItemDataProva().add(new SelectItem(obj.getCodigo(), obj.getDataProva_Apresentar()));
			}
		} catch (Exception e) {

		}
	}

	public List<SelectItem> getListaSelectItemSituacaoFinanceiraInscricao() {
		if (listaSelectItemSituacaoFinanceiraInscricao == null) {
			listaSelectItemSituacaoFinanceiraInscricao = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemSituacaoFinanceiraInscricao;
	}

	public void setListaSelectItemSituacaoFinanceiraInscricao(List<SelectItem> listaSelectItemSituacaoFinanceiraInscricao) {
		this.listaSelectItemSituacaoFinanceiraInscricao = listaSelectItemSituacaoFinanceiraInscricao;
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
	
	public List<SelectItem> getListaSelectItemSituacaoResultadoProcessoSeletivo() {
		return SituacaoResultadoProcessoSeletivoEnum.getSituacaoResultadoProcessoSeletivo();
	}

	public List<SelectItem> listaSelectItemTipoFiltroDocumentoAssinado;
	public List<SelectItem> getListaSelectItemTipoFiltroDocumentoAssinado() {
		if(listaSelectItemTipoFiltroDocumentoAssinado == null) {
		listaSelectItemTipoFiltroDocumentoAssinado = new ArrayList<SelectItem>(0);
		listaSelectItemTipoFiltroDocumentoAssinado.add(new SelectItem("NAO_CONTROLA", "Não Controla"));
		listaSelectItemTipoFiltroDocumentoAssinado.add(new SelectItem("TODOS", "Todos"));
		listaSelectItemTipoFiltroDocumentoAssinado.add(new SelectItem("PENDENTE", "Pendente"));
		listaSelectItemTipoFiltroDocumentoAssinado.add(new SelectItem("ASSINADO", "Assinado"));
		listaSelectItemTipoFiltroDocumentoAssinado.add(new SelectItem("REJEITADO", "Rejeitado"));
		}
			return listaSelectItemTipoFiltroDocumentoAssinado;
	}

	public ProgressBarVO getProgressBarVO() {
		if(progressBarVO == null) {
			progressBarVO = new ProgressBarVO();
		}
		return progressBarVO;
	}

	public void setProgressBarVO(ProgressBarVO progressBarVO) {
		this.progressBarVO = progressBarVO;
	}

	public void iniciarProgressBarExcel() {
		getProgressBarVO().setUsuarioVO(getUsuarioLogado());
		getProgressBarVO().setUrlLogoUnidadeEnsino(getLogoPadraoRelatorio());
		getProgressBarVO().getParams().put("PermiteGerarFormatoExportacaoDados", getPermiteGerarFormatoExportacaoDados());
		try {
			getProgressBarVO().setCaminhoWebRelatorio(UteisJSF.getCaminhoWeb());
			getProgressBarVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralSistemaVO());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getProgressBarVO().iniciar(0l, 3, "Consultando dados...", true, this, "realizarGeracaoRelatorioExcel");		
	}
	
	public void iniciarProgressBarPdf() {
		getProgressBarVO().setUsuarioVO(getUsuarioLogado());			
		getProgressBarVO().setUrlLogoUnidadeEnsino(getLogoPadraoRelatorio());
		getProgressBarVO().getParams().put("PermiteGerarFormatoExportacaoDados", getPermiteGerarFormatoExportacaoDados());
		try {
			getProgressBarVO().setCaminhoWebRelatorio(UteisJSF.getCaminhoWeb());
			getProgressBarVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralSistemaVO());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getProgressBarVO().iniciar(0l, 3, "Consultando dados...", true, this, "realizarGeracaoRelatorioPdf");		
	}
	

	public Boolean getMarcarTodasSituacoesFinanceirasRequerimento() {
		if(marcarTodasSituacoesFinanceirasRequerimento == null) {
			marcarTodasSituacoesFinanceirasRequerimento = false;
		}
		return marcarTodasSituacoesFinanceirasRequerimento;
	}

	public void setMarcarTodasSituacoesFinanceirasRequerimento(Boolean marcarTodasSituacoesFinanceirasRequerimento) {
		this.marcarTodasSituacoesFinanceirasRequerimento = marcarTodasSituacoesFinanceirasRequerimento;
	}

	public void realizarSelecaoCheckboxMarcarDesmarcarTodasSituacoesFinanceirasRequerimento() {
		realizarSelecionarTodosSituacoesFinanceirasRequerimento(getMarcarTodasSituacoesFinanceirasRequerimento());
	}
	
	public void realizarSelecionarTodosSituacoesFinanceirasRequerimento(boolean selecionado){
		getRelatorioSEIDecidirVO().setAguardandoAutorizacaoPagamento(selecionado);
		getRelatorioSEIDecidirVO().setAguardandoPagamento(selecionado);
		getRelatorioSEIDecidirVO().setIsento(selecionado);
		getRelatorioSEIDecidirVO().setPago(selecionado);
		getRelatorioSEIDecidirVO().setCanceladoFinanceiramente(selecionado);
		getRelatorioSEIDecidirVO().setSolicitacaoIsencao(selecionado);
		getRelatorioSEIDecidirVO().setSolicitacaoIsencaoDeferido(selecionado);
		getRelatorioSEIDecidirVO().setSolicitacaoIsencaoIndeferido(selecionado);
		
	}
	
	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodasSituacoesFinanceirasRequerimento() {
		if (getMarcarTodasSituacoesFinanceirasRequerimento()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}
	public Boolean getApresentarPeriodoAceiteContrato() {
		return !getRelatorioSEIDecidirVO().getTipoFiltroDocumentoAssinado().equals("NAO_CONTROLA");
	}
	
	
	public void validarDadosListaResposta(FiltroPersonalizadoVO filtroPersonalizadoVO, FiltroPersonalizadoOpcaoVO filtroPersonalizadoOpcaoVO) {
		if (filtroPersonalizadoVO.getTipoCampoSimplesEscolha()) {
			filtroPersonalizadoOpcaoVO.setSelecionado(!filtroPersonalizadoOpcaoVO.getSelecionado());
			for (FiltroPersonalizadoOpcaoVO obj : filtroPersonalizadoVO.getListaFiltroPersonalizadoOpcaoVOs()) {
				if (!obj.getCodigo().equals(filtroPersonalizadoOpcaoVO.getCodigo())) {
					obj.setSelecionado(false);
				}
	    	}
		}
    }
	
	public void montarListaSelectItemFiltroCustomizavel() {
		for(FiltroPersonalizadoVO obj : getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getListaFiltroPersonalizadoVOs()) {
			if (obj.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.COMBOBOX_CUSTOMIZAVEL)) {
				List<SelectItem> itens = new ArrayList<SelectItem>(0);
				itens.add(new SelectItem("", ""));
				for (FiltroPersonalizadoOpcaoVO filtroPersonalizadoOpcaoVO : obj.getListaFiltroPersonalizadoOpcaoVOs()) {
					itens.add(new SelectItem(filtroPersonalizadoOpcaoVO.getCodigo(), filtroPersonalizadoOpcaoVO.getDescricaoOpcao()));
				}
				obj.setListaSelectItemComboboxCustomizavelVOs(itens);
			}
		}
	}
	
	public void montarListaSelectItemCombobox() {
		for(FiltroPersonalizadoVO obj : getRelatorioSEIDecidirVO().getLayoutRelatorioSEIDecidirVO().getListaFiltroPersonalizadoVOs()) {
			if (obj.getTipoCampoFiltro().equals(TipoCampoFiltroEnum.COMBOBOX)) {
				getFacadeFactory().getFiltroPersonalizadoFacade().inicializarDadosCombobox(obj, getUsuarioLogado());
			} 
		}
	}
}
