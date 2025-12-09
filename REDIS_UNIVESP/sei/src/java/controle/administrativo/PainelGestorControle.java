package controle.administrativo;

import java.io.File;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.DropEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.ControlePaginaPainelGestorEnum;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.CampanhaColaboradorCursoVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ContaReceberPainelGestorVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FiltroPainelGestorAcademicoVO;
import negocio.comuns.administrativo.FraseInspiracaoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.PainelGestorContaPagarMesAnoVO;
import negocio.comuns.administrativo.PainelGestorContaReceberMesAnoVO;
import negocio.comuns.administrativo.PainelGestorFinanceiroAcademicoMesAnoVO;
import negocio.comuns.administrativo.PainelGestorFinanceiroAcademicoNivelEducacionalVO;
import negocio.comuns.administrativo.PainelGestorMonitoramentoAcademicoVO;
import negocio.comuns.administrativo.PainelGestorMonitoramentoCRMVO;
import negocio.comuns.administrativo.PainelGestorMonitoramentoConsultorDetalhamentoVO;
import negocio.comuns.administrativo.PainelGestorMonitoramentoConsultorVO;
import negocio.comuns.administrativo.PainelGestorMonitoramentoDescontoNivelEducacionalVO;
import negocio.comuns.administrativo.PainelGestorMonitoramentoDetalheCRMVO;
import negocio.comuns.administrativo.PainelGestorVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum;
import negocio.comuns.administrativo.enumeradores.PainelGestorTipoMonitoramentoCRMEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoModuloEnum;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.enumerador.TipoCampanhaEnum;
import negocio.comuns.crm.enumerador.TipoFiltroMonitamentoCrmProspectEnum;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.planoorcamentario.DetalhamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.PlanoOrcamentarioVO;
import negocio.comuns.processosel.PreInscricaoVO;
import negocio.comuns.sad.NivelGraficoDWVO;
import negocio.comuns.segmentacao.SegmentacaoProspectVO;
import negocio.comuns.utilitarias.DashboardVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.ModeloApresentacaoDashboardEnum;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoDashboardEnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Controller("PainelGestorControle")
@Scope("session")
@Lazy
public class PainelGestorControle extends SuperControleRelatorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1282488458794498592L;
	private PessoaVO pessoa;
	private FraseInspiracaoVO fraseInspiracaoVO;
	private Boolean todos;
	private Boolean cargo;
	private Integer codigoCargo;
	private Boolean departamento;
	private Integer codigoDepartamento;
	protected List<SelectItem> listaSelectItemCargo;
	protected List<SelectItem> listaSelectItemDepartamento;
	private Boolean funcionarios;
	private Boolean professores;
	private Boolean alunos;
	private Boolean enviarEmail;
	private PainelGestorVO painelGestorVO;
	private Date dataInicio;
	private Date dataFinal;
	private Date dataInicioMesAno;
	private Date dataFinalMesAno;
	private Date dataInicioTemporaria;
	private Date dataFinalTemporaria;
	private List<UnidadeEnsinoVO> unidadeEnsinoVOs;

	private String campoConsultarUnidadeEnsino;
	private String valorConsultarUnidadeEnsino;
	private List<UnidadeEnsinoVO> listaConsultarUnidadeEnsino;
	private List<PlanoOrcamentarioVO> listaConsultarPlanoOrcamentario;
	private String tipoNivelEducacional;
	private List<SelectItem> tipoNivelEducacionalEnums;
	private List<SelectItem> tipoNivelEducacionalEnumsNaoObrigatorio;
	private String mesAno;
	private NivelGraficoDWVO nivelGraficoDWVO;
	private CursoVO cursoVO;
	private TurmaVO turmaVO;
	private List<TurmaVO> listaConsultaTurma;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private List<CursoVO> listaConsultaCurso;
	private String campoConsultaCurso = "nome";
	private String valorConsultaCurso;
	private List<SelectItem> tipoConsultaComboCurso;
	private List<SelectItem> tipoConsultaComboTurma;
	private String paginaVoltarDesconto;
	private String paginaVoltarDescontoInstituicao;
	private String paginaVoltarDescontoConvenio;
	private String paginaVoltarDescontoProgressivo;
	private String paginaVoltarDescontoNivelEducacional;
	private Boolean desabilitarPool;
	private Boolean filtrarUnidadeEnsino;
	private Boolean filtrarAcademico;
	private Boolean mostarDetalheAcademico;
	private Boolean mostarProspectCrm;

	private PlanoOrcamentarioVO planoOrcamentarioVO;
	private DetalhamentoPlanoOrcamentarioVO detalhamentoPlanoOrcamentarioVO;
	private PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRM;
	private CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorario;
	private FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico;
	private Boolean permissaoExcluirPreInscricao;
	private String ano;
	private String semestre;
	private List<SelectItem> listaSelectItemSemestre;
	private List<SelectItem> listaSelectItemAno;
	private ControlePaginaPainelGestorEnum telaOrigem;
	private List<DocumetacaoMatriculaVO> listaDoc;
	private Boolean fecharGraficoMonitoramentoProspect;
	private String situacaoProspect;
	private List<SelectItem> listaSelectItemSituacaoProspect;
	private List<SelectItem> listaSelectItemAcaoAlteracaoConsultor;
	private ProspectsVO prospectsVO;
	private String dataAtual;
	private PainelGestorContaReceberMesAnoVO painelGestorContaReceberMesAnoVO;
	private String tipoOrigem;
	private String situacaoContaReceberMesAno;
	private String tipoMapaReceita;
	private String tituloMapaReceitaTipoOrigem;
	private String tituloDetalhamentoDesconto;
	private String tipoDesconto;
	private String tituloMapaReceitaPorTurma;
	private String tituloMapaReceitaPorCurso;
	private FuncionarioVO consultor;
	private String situacaoMatricula;
	private String situacaoProspectAtIn;
	private String htmlRelatorioConsultorPorMatricula;

	private Boolean marcarTodasUnidadeEnsino;

	private Date dataCompetenciaInicial;
	private Date dataCompetenciaFinal;

	private List<SelectItem> listaSelectItemFiltrarDataReceitaPor;
	private String filtarDataReceitaPor;

	private String opcaoSituacaoAcademica;
	

	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			if (marcarTodasUnidadeEnsino) {
				unidade.setFiltrarUnidadeEnsino(Boolean.TRUE);
			} else {
				unidade.setFiltrarUnidadeEnsino(Boolean.FALSE);
			}
		}
	}

	public String getPaginaVoltarDescontoInstituicao() {
		if (paginaVoltarDescontoInstituicao == null) {
			paginaVoltarDescontoInstituicao = "";
		}
		return paginaVoltarDescontoInstituicao;
	}

	public void setPaginaVoltarDescontoInstituicao(String paginaVoltarDescontoInstituicao) {
		this.paginaVoltarDescontoInstituicao = paginaVoltarDescontoInstituicao;
	}

	public String getPaginaVoltarDescontoProgressivo() {
		if (paginaVoltarDescontoProgressivo == null) {
			paginaVoltarDescontoProgressivo = "";
		}
		return paginaVoltarDescontoProgressivo;
	}

	public void setPaginaVoltarDescontoProgressivo(String paginaVoltarDescontoProgressivo) {
		this.paginaVoltarDescontoProgressivo = paginaVoltarDescontoProgressivo;
	}

	public String getPaginaVoltarDescontoConvenio() {
		if (paginaVoltarDescontoConvenio == null) {
			paginaVoltarDescontoConvenio = "";
		}
		return paginaVoltarDescontoConvenio;
	}

	public void setPaginaVoltarDescontoConvenio(String paginaVoltarDescontoConvenio) {
		this.paginaVoltarDescontoConvenio = paginaVoltarDescontoConvenio;
	}

	public String getPaginaVoltarDescontoNivelEducacional() {
		if (paginaVoltarDescontoNivelEducacional == null) {
			paginaVoltarDescontoNivelEducacional = "";
		}
		return paginaVoltarDescontoNivelEducacional;
	}

	public void setPaginaVoltarDescontoNivelEducacional(String paginaVoltarDescontoNivelEducacional) {
		this.paginaVoltarDescontoNivelEducacional = paginaVoltarDescontoNivelEducacional;
	}

	public String getPaginaVoltarDesconto() {
		if (paginaVoltarDesconto == null) {
			paginaVoltarDesconto = "";
		}
		return paginaVoltarDesconto;
	}

	public void setPaginaVoltarDesconto(String paginaVoltarDesconto) {
		this.paginaVoltarDesconto = paginaVoltarDesconto;
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboCurso;
	}

	public void consultarCurso() {
		try {
			getListaConsultaCurso().clear();
			if (getCampoConsultaCurso().equals("nome")) {
				setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultarCursoPorNomePeriodicidadeEUnidadeEnsinoVOs(getValorConsultaCurso(), "", null, getUnidadeEnsinoVOs(), getUsuarioLogado()));
			}

			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsultaCurso().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void montarConsultaCurso() {
		getListaConsultaCurso().clear();
	}

	public void realizarLimparCurso() {
		setCursoVO(new CursoVO());
	}

	public void selecionarCurso() throws Exception {
		try {
			CursoVO curso = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			setCursoVO(curso);
			setMensagemDetalhada("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso2() throws Exception {
		try {
			CursoVO curso = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			setCursoVO(curso);
			realizarLimparTurma();
			setMensagemDetalhada("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Turma"));
		}
		return tipoConsultaComboTurma;
	}

	public void montarConsultaTurma() {
		getListaConsultaTurma().clear();
	}

	public void realizarLimparTurma() {
		setTurmaVO(new TurmaVO());
	}

	public String getCampoConsultaCurso() {
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
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

	public String getValorConsultaCurso() {
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public NivelGraficoDWVO getNivelGraficoDWVO() {
		if (nivelGraficoDWVO == null) {
			nivelGraficoDWVO = new NivelGraficoDWVO();
		}
		return nivelGraficoDWVO;
	}

	public void setNivelGraficoDWVO(NivelGraficoDWVO nivelGraficoDWVO2) {
		this.nivelGraficoDWVO = nivelGraficoDWVO2;
	}

	public Boolean getExisteGraficoCategoriaDespesa() {
		return !getPainelGestorVO().getLegendaGraficoCategoriaDespesaVOs().isEmpty();
	}

	public Boolean getExisteGraficoAcademicoFinanceiro() {
		return !getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().isEmpty() && getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().size() >= 3 && (getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().get(0).getQuantidade() > 0 || getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().get(1).getQuantidade() > 0 || getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().get(2).getQuantidade() > 0);
	}

	public Boolean getExisteGraficoReceitaDespesaLinhaTempo() {
		return !getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().isEmpty() && getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().size() >= 2 && (getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().get(0).getValor() > 0.0 || getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().get(1).getValor() > 0.0);
	}

	public Boolean getExisteGraficoConsumo() {
		return !getNivelGraficoDWVO().getLegendaGraficoVOs().isEmpty();
	}

	public String getMesAno() {
		if (mesAno == null) {
			mesAno = "";
		}
		return mesAno;
	}

	public void setMesAno(String mesAno) {
		this.mesAno = mesAno;
	}

	public List<SelectItem> getTipoNivelEducacionalEnums() {
		if (tipoNivelEducacionalEnums == null) {
			tipoNivelEducacionalEnums = new ArrayList<SelectItem>();
		}
		return tipoNivelEducacionalEnums;
	}

	public void setTipoNivelEducacionalEnums(List<SelectItem> tipoNivelEducacionalEnums) {
		this.tipoNivelEducacionalEnums = tipoNivelEducacionalEnums;
	}

	public String getTipoNivelEducacional() {
		if (tipoNivelEducacional == null) {
			tipoNivelEducacional = "";
		}
		return tipoNivelEducacional;
	}

	public TipoNivelEducacional getTipoNivelEducacional_Enum() {
		if (tipoNivelEducacional == null || tipoNivelEducacional.equals("")) {
			return null;
		}
		return TipoNivelEducacional.getEnum(getTipoNivelEducacional());
	}

	public void setTipoNivelEducacional(String tipoNivelEducacional) {

		this.tipoNivelEducacional = tipoNivelEducacional;
	}

	public PainelGestorControle() throws Exception {
		setPessoa(getUsuarioLogado().getPessoa());
		inicializarDadosListaNivelEducacional2();
		setMensagemID("msg_visualizarDetalhes", Uteis.ALERTA);
	}

	public void gerarGraficos() {
		try {
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@PostConstruct
	public String consultarDadosPaginaInicialPainelGestor() {
		try {
			consultarConfiguracaoDashboard();			
			setTelaOrigem(ControlePaginaPainelGestorEnum.PAGINA_INICIAL);
			setFiltrarUnidadeEnsino(false);
			inicializarGraficoLinhaRecebimentoPagamento();
			inicializarGraficoPainelGestorAcademicoPorNivelEducacional();
			inicializarGraficoPainelGestorConsumoPorCategoria();
			inicializarGraficoPainelGestorConsumoPorDespartamento();
			inicializarGraficoPainelGestorPlanoOrcamentario();
		
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestor.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestor.xhtml");
		}
	}
	
	public void consultarGraficoPainelGestorPlanoOrcamentario() {
		try {
			setListaConsultarPlanoOrcamentario(getFacadeFactory().getPlanoOrcamentarioFacade().consultarDadosPainelGestor(getUnidadeEnsinoVOs()));			
		}catch (Exception e) {
			
		}finally {
			getMapDashboards().get(TipoDashboardEnum.BI_PLANO_ORCAMENTARIO.name()).incrementar();
			getMapDashboards().get(TipoDashboardEnum.BI_PLANO_ORCAMENTARIO.name()).encerrar();			
		}
	}
	
	
	public void inicializarGraficoPainelGestorPlanoOrcamentario() {
		try {
			if (getLoginControle().getPermissaoAcessoMenuVO().getPainelGestorPlanoOrcamentario()) {
				
				if(!getMapDashboards().containsKey(TipoDashboardEnum.BI_PLANO_ORCAMENTARIO.name())){					
					getMapDashboards().put(TipoDashboardEnum.BI_PLANO_ORCAMENTARIO.name(), 
							new DashboardVO(TipoDashboardEnum.BI_PLANO_ORCAMENTARIO, false, 5, TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.SEI_DECIDIR, getUsuarioLogado()));
				}
				
				DashboardVO dashboardVO = getMapDashboards().get(TipoDashboardEnum.BI_PLANO_ORCAMENTARIO.name());
				dashboardVO.setUsuarioVO(getUsuarioLogadoClone());
				dashboardVO.iniciar(1l, 2, "Consultando...", true, this, "consultarGraficoPainelGestorPlanoOrcamentario");
				
				dashboardVO.iniciarAssincrono();
				
			}else {
				if(getMapDashboards().containsKey(TipoDashboardEnum.BI_PLANO_ORCAMENTARIO.name())){
					getMapDashboards().remove(TipoDashboardEnum.BI_PLANO_ORCAMENTARIO.name());
				}
				
			}
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarGraficoPainelGestorConsumoPorCategoria() {
		try {
			getFacadeFactory().getPainelGestorFacade().executarCriacaoGraficoCategoriaDespesa(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), "", null, true, 5, 0);
		}catch (Exception e) {
			
		}finally {
			getMapDashboards().get(TipoDashboardEnum.BI_CONSUMO_CATEGORIA.name()).incrementar();
			getMapDashboards().get(TipoDashboardEnum.BI_CONSUMO_CATEGORIA.name()).encerrar();			
		}
	}
	
	
	public void inicializarGraficoPainelGestorConsumoPorCategoria() {
		try {
			if (getLoginControle().getPermissaoAcessoMenuVO().getPainelGestorDespesaCategoria()) {
				
				if(!getMapDashboards().containsKey(TipoDashboardEnum.BI_CONSUMO_CATEGORIA.name())){					
					getMapDashboards().put(TipoDashboardEnum.BI_CONSUMO_CATEGORIA.name(), 
							new DashboardVO(TipoDashboardEnum.BI_CONSUMO_CATEGORIA, false, 3, TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.SEI_DECIDIR, getUsuarioLogado()));
				}
				
				DashboardVO dashboardVO = getMapDashboards().get(TipoDashboardEnum.BI_CONSUMO_CATEGORIA.name());
				
				dashboardVO.iniciar(1l, 2, "Consultando...", true, this, "consultarGraficoPainelGestorConsumoPorCategoria");
				
				dashboardVO.iniciarAssincrono();
				
			}else {
				if(getMapDashboards().containsKey(TipoDashboardEnum.BI_CONSUMO_CATEGORIA.name())){
					getMapDashboards().remove(TipoDashboardEnum.BI_CONSUMO_CATEGORIA.name());
				}
				
			}
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarGraficoPainelGestorConsumoPorDepartamento() {
		try {
			getFacadeFactory().getPainelGestorFacade().executarCriacaoGraficoConsumo(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), true);
		}catch (Exception e) {
			
		}finally {
			getMapDashboards().get(TipoDashboardEnum.BI_CONSUMO_DEPARTAMENTO.name()).incrementar();
			getMapDashboards().get(TipoDashboardEnum.BI_CONSUMO_DEPARTAMENTO.name()).encerrar();			
		}
	}
	
	
	public void inicializarGraficoPainelGestorConsumoPorDespartamento() {
		try {
			if (getLoginControle().getPermissaoAcessoMenuVO().getPainelGestorConsumo()) {
				
				if(!getMapDashboards().containsKey(TipoDashboardEnum.BI_CONSUMO_DEPARTAMENTO.name())){					
					getMapDashboards().put(TipoDashboardEnum.BI_CONSUMO_DEPARTAMENTO.name(), 
							new DashboardVO(TipoDashboardEnum.BI_CONSUMO_DEPARTAMENTO, false, 4, TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.SEI_DECIDIR, getUsuarioLogado()));
				}
				
				DashboardVO dashboardVO = getMapDashboards().get(TipoDashboardEnum.BI_CONSUMO_DEPARTAMENTO.name());
				
				dashboardVO.iniciar(1l, 2, "Consultando...", true, this, "consultarGraficoPainelGestorConsumoPorDepartamento");
				
				dashboardVO.iniciarAssincrono();
				
			}else {
				if(getMapDashboards().containsKey(TipoDashboardEnum.BI_CONSUMO_DEPARTAMENTO.name())){
					getMapDashboards().remove(TipoDashboardEnum.BI_CONSUMO_DEPARTAMENTO.name());
				}
				
			}
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarGraficoPainelGestorAcademicoPorNivelEducacional() {
		try {
			getPainelGestorVO().setQuantidadeAlunosAtualmente(getFacadeFactory().getPainelGestorFacade().consultarQtdeAlunoAtivoAtualmente(getUnidadeEnsinoVOs()));		
			getFacadeFactory().getPainelGestorFacade().consultarDadosGraficoPainelGestorFinanceiroAcademicoNivelEducacional(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), getAno(), getSemestre(), getUsuarioLogado());
		}catch (Exception e) {
			
		}finally {
			getMapDashboards().get(TipoDashboardEnum.BI_ACADEMICO_POR_NIVEL_EDUCACIONAL.name()).incrementar();
			getMapDashboards().get(TipoDashboardEnum.BI_ACADEMICO_POR_NIVEL_EDUCACIONAL.name()).encerrar();			
		}
	}
	
	
	public void inicializarGraficoPainelGestorAcademicoPorNivelEducacional() {
		try {
			if (getLoginControle().getPermissaoAcessoMenuVO().getPainelGestorAcademico()) {
				
				if(!getMapDashboards().containsKey(TipoDashboardEnum.BI_ACADEMICO_POR_NIVEL_EDUCACIONAL.name())){					
					getMapDashboards().put(TipoDashboardEnum.BI_ACADEMICO_POR_NIVEL_EDUCACIONAL.name(), 
							new DashboardVO(TipoDashboardEnum.BI_ACADEMICO_POR_NIVEL_EDUCACIONAL, false, 2, TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.SEI_DECIDIR, getUsuarioLogado()));
				}
				
				DashboardVO dashboardVO = getMapDashboards().get(TipoDashboardEnum.BI_ACADEMICO_POR_NIVEL_EDUCACIONAL.name());
				
				dashboardVO.iniciar(1l, 2, "Consultando...", true, this, "consultarGraficoPainelGestorAcademicoPorNivelEducacional");
				
				dashboardVO.iniciarAssincrono();
				
			}else {
				if(getMapDashboards().containsKey(TipoDashboardEnum.BI_ACADEMICO_POR_NIVEL_EDUCACIONAL.name())){
					getMapDashboards().remove(TipoDashboardEnum.BI_ACADEMICO_POR_NIVEL_EDUCACIONAL.name());
				}
				
			}
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void inicializarGraficoLinhaRecebimentoPagamento() {
		try {
			if (getLoginControle().getPermissaoAcessoMenuVO().getPainelGestorFinanceiro()) {
				
				if(!getMapDashboards().containsKey(TipoDashboardEnum.BI_RECEITA_DESPESA.name())){					
					getMapDashboards().put(TipoDashboardEnum.BI_RECEITA_DESPESA.name(), 
							new DashboardVO(TipoDashboardEnum.BI_RECEITA_DESPESA, false, 1, TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.SEI_DECIDIR, getUsuarioLogado()));
				}
				
				DashboardVO dashboardVO = getMapDashboards().get(TipoDashboardEnum.BI_RECEITA_DESPESA.name());
				
				dashboardVO.iniciar(1l, 2, "Consultando...", true, this, "consultarDadosGraficoLinhaRecebimentoPagamento");
				
				dashboardVO.iniciarAssincrono();
				
			}else {
				if(getMapDashboards().containsKey(TipoDashboardEnum.BI_RECEITA_DESPESA.name())){
					getMapDashboards().remove(TipoDashboardEnum.BI_RECEITA_DESPESA.name());
				}
				
			}
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarDadosGraficoLinhaRecebimentoPagamento() {
		try {
			getFacadeFactory().getPainelGestorFacade().executarCriacaoGraficoLinhaRecebimentoPagamento(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), false);
		}catch (Exception e) {
			
		}finally {
			getMapDashboards().get(TipoDashboardEnum.BI_RECEITA_DESPESA.name()).incrementar();
			getMapDashboards().get(TipoDashboardEnum.BI_RECEITA_DESPESA.name()).encerrar();
		}
	}

	public String consultarDadosPainelGestorAcademicoFinanceiro() {
		try {
			getFacadeFactory().getPainelGestorFacade().consultarDadosPainelGestorFinanceiroAcademico(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal());
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorAcademicoFinanceiro.xhtml");
		} catch (Exception e) {

			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String consultarDadosPainelGestorConsumo() {
		try {
			// navegarPara(DespesaDWControle.class.getSimpleName(),
			// "selecionarLegendaPainel", "despesaDW", getUnidadeEnsinoVOs(),
			// getDataInicio(), getDataFinal());
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("despesaDWForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String visualizarConsumoContaPagarMesAnoEspecifico() {
		try {
			PainelGestorContaPagarMesAnoVO painelGestorContaPagarMesAnoVO = (PainelGestorContaPagarMesAnoVO) context().getExternalContext().getRequestMap().get("contaPagarMesAnoItens");
			setMesAno(painelGestorContaPagarMesAnoVO.getMesAno());
			inicializarDataMesAno();			
			return consultarDadosPainelGestorCategoriaDespesaPorDepartamento();			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String visualizarCategoriaDespesaContaPagarMesAnoEspecifico() {
		try {
			PainelGestorContaPagarMesAnoVO painelGestorContaPagarMesAnoVO = (PainelGestorContaPagarMesAnoVO) context().getExternalContext().getRequestMap().get("contaPagarMesAnoItens");
			setMesAno(painelGestorContaPagarMesAnoVO.getMesAno());
			inicializarDataMesAno();
			// getPainelGestorVO().setDataInicioPorCategoriaDespesa(Uteis.getData("01/"
			// + painelGestorContaPagarMesAnoVO.getMesAno(), "dd/MM/yyyy"));
			// getPainelGestorVO().setDataFinalPorCategoriaDespesa(Uteis.getDataUltimoDiaMes(Uteis.getData("01/"
			// + painelGestorContaPagarMesAnoVO.getMesAno(), "dd/MM/yyyy")));
			return consultarDadosPainelGestorCategoriaDespesa();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String visualizarConsumoContaPagarMesAno() {
		try {
			setMesAno("GERAL");
			setDataInicioMesAno(getDataInicio());
			setDataFinalMesAno(getDataFinal());
			setDataInicioTemporaria(getDataInicio());
			setDataFinalTemporaria(getDataFinal());
			return consultarDadosPainelGestorCategoriaDespesaPorDepartamento();	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	private CategoriaDespesaVO categoriaDespesaVO;

	public CategoriaDespesaVO getCategoriaDespesaVO() {
		if (categoriaDespesaVO == null) {
			categoriaDespesaVO = new CategoriaDespesaVO();
		}
		return categoriaDespesaVO;
	}

	public void setCategoriaDespesaVO(CategoriaDespesaVO categoriaDespesaVO) {
		this.categoriaDespesaVO = categoriaDespesaVO;
	}

	public String consultarDadosPainelGestorCategoriaDespesaMonitoramentoFinanceiroPorDepartamento() {
		setMesAno("GERAL");
		getPainelGestorVO().getNivelAtualCategoriaDespesa();
		setDataInicioMesAno(getDataInicio());
		setDataFinalMesAno(getDataFinal());
		setDataCompetenciaInicial(null);
		setDataCompetenciaFinal(null);		
		setDataInicioTemporaria(getDataInicio());
		setDataFinalTemporaria(getDataFinal());
		return consultarDadosPainelGestorCategoriaDespesaPorDepartamento();
	}

	public String consultarDadosPainelGestorCategoriaDespesaMonitoramentoFinanceiro() {
		setMesAno("");
		setDataInicioMesAno(getDataInicio());
		setDataFinalMesAno(getDataFinal());
		return consultarDadosPainelGestorCategoriaDespesa();
	}

	public String voltarPagina() {
		if (getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAGINA_INICIAL)) {
			// if(getMesAno().trim().isEmpty()){
			// setDataInicio(getDataInicioMesAno());
			// setDataFinal(getDataFinalMesAno());
			// }
			consultarDadosPaginaInicialPainelGestor();
			return Uteis.getCaminhoRedirecionamentoNavegacao(getTelaOrigem().getNavegacao()) ;
		}
		if (getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO)) {
			if (getMesAno().trim().isEmpty()) {
				setDataInicio(getDataInicioMesAno());
				setDataFinal(getDataFinalMesAno());
			} else {
				setDataInicio(getDataInicioTemporaria());
				setDataFinal(getDataFinalTemporaria());
			}
			executarInicializacaoDadoFinanceiroPainelGestor();
			String navegacao = getTelaOrigem().getNavegacao();
			setTelaOrigem(ControlePaginaPainelGestorEnum.PAGINA_INICIAL);
			return Uteis.getCaminhoRedirecionamentoNavegacao(navegacao) ;
		}
		if (getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO_ACADEMICO)) {
			if (getMesAno().trim().isEmpty()) {
				setDataInicio(getDataInicioMesAno());
				setDataFinal(getDataFinalMesAno());
			}
			consultarDadosPainelGestorAcademicoFinanceiro();
			return Uteis.getCaminhoRedirecionamentoNavegacao(getTelaOrigem().getNavegacao()) ;
		}
		if (getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO_POR_NIVEL_EDUCACIONAL)) {
			executarGeracaoDadosPainelGestorFinanceiroPorNivelEducacional();
			String navegacao = getTelaOrigem().getNavegacao();
			setTelaOrigem(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao(navegacao) ;
		}
		if (getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO_POR_NIVEL_EDUCACIONAL_FLUXO_CAIXA)) {
			executarGeracaoDadosPainelGestorFinanceiroPorNivelEducacionalFluxoCaixa();
			String navegacao = getTelaOrigem().getNavegacao();
			setTelaOrigem(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao(navegacao) ;
		}
		if (getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO_POR_TURMA)) {
			executarGeracaoDadosPainelGestorFinanceiroPorTurma();
			String navegacao = getTelaOrigem().getNavegacao();
			setTelaOrigem(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO_POR_NIVEL_EDUCACIONAL);
			return Uteis.getCaminhoRedirecionamentoNavegacao(navegacao) ;
		}
		if (getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO_POR_TURMA_FLUXO_CAIXA)) {
			executarGeracaoDadosPainelGestorFinanceiroPorTurmaFluxoCaixa();
			String navegacao = getTelaOrigem().getNavegacao();
			setTelaOrigem(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO_POR_NIVEL_EDUCACIONAL_FLUXO_CAIXA);
			return Uteis.getCaminhoRedirecionamentoNavegacao(navegacao) ;
		}
		if (getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_MONITORAMENTO_DESCONTO)) {
			if (getMesAno().trim().isEmpty()) {
				setDataInicio(getDataInicioMesAno());
				setDataFinal(getDataFinalMesAno());
			}
			consultarDadosPainelGestorMonitoramentoDesconto();
			String navegacao = getTelaOrigem().getNavegacao();
			setTelaOrigem(ControlePaginaPainelGestorEnum.PAGINA_INICIAL);
			return Uteis.getCaminhoRedirecionamentoNavegacao(navegacao) ;
		}
		if (getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_MONITORAMENTO_DESCONTO_POR_NIVEL_EDUCACIONAL)) {
			executarGeracaoDadosPainelGestorMonitoramentoDescontoPorNivelEducacional();
			String navegacao = getTelaOrigem().getNavegacao();
			setTelaOrigem(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_MONITORAMENTO_DESCONTO);
			return Uteis.getCaminhoRedirecionamentoNavegacao(navegacao) ;
		}
		if (getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_MONITORAMENTO_DESCONTO_POR_TURMA)) {
			executarGeracaoDadosPainelGestorMonitoramentoDescontoTurma();
			String navegacao = getTelaOrigem().getNavegacao();
			setTelaOrigem(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_MONITORAMENTO_DESCONTO_POR_NIVEL_EDUCACIONAL);
			return Uteis.getCaminhoRedirecionamentoNavegacao(navegacao) ;
		}
		if (getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO_DETALHAMENTO_CONTARECEBER)) {
			setTelaOrigem(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO);
			setDataInicio(getDataInicioTemporaria());
			setDataFinal(getDataFinalTemporaria());
			executarInicializacaoDadoFinanceiroPainelGestor();
			return Uteis.getCaminhoRedirecionamentoNavegacao(getTelaOrigem().getNavegacao()) ;
		}
		if (getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_CATEGORIA_DESPESA_DEPARTAMENTO)) {
			setTelaOrigem(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO);
			setDataInicio(getDataInicioTemporaria());
			setDataFinal(getDataFinalTemporaria());
			setDataCompetenciaInicial(getDataInicioTemporaria());
			setDataCompetenciaFinal(getDataFinalTemporaria());
			getPainelGestorVO().getLegendaGraficoCategoriaDespesaVOs().clear();
			executarInicializacaoDadoFinanceiroPainelGestorConsumoDepartamento();
			return Uteis.getCaminhoRedirecionamentoNavegacao(getTelaOrigem().getNavegacao()) ;
		}

		return Uteis.getCaminhoRedirecionamentoNavegacao(getTelaOrigem().getNavegacao()) ;
	}

	public void executarInicializacaoDadoFinanceiroPainelGestorConsumoDepartamento() {
		try {
			getFacadeFactory().getPainelGestorFacade().executarCriacaoGraficoCategoriaDespesaPorDepartamento(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), false, getPainelGestorVO().getCodigoDepartamento(), getDataCompetenciaInicial(), getDataCompetenciaFinal());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String consultarDadosPainelGestorCategoriaDespesa() {
		try {
			getFacadeFactory().getPainelGestorFacade().executarCriacaoGraficoCategoriaDespesa(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getPainelGestorVO().getNivelAtualCategoriaDespesa(), null, false, 0, 0);
			getControleConsultaOtimizado().setLimitePorPagina(50);
			getControleConsultaOtimizado().setPage(0);
			getControleConsultaOtimizado().setPaginaAtual(1);
			if (getPainelGestorVO().getNivelAtualCategoriaDespesa() != null && !getPainelGestorVO().getNivelAtualCategoriaDespesa().trim().isEmpty()) {
				if (getPainelGestorVO().getNivelAtualCategoriaDespesa().equals("-1")) {
					setCategoriaDespesaVO(null);
					getCategoriaDespesaVO().setDescricao("Categoria Não Informada");
				} else {
					setCategoriaDespesaVO(getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesaUnico(getPainelGestorVO().getNivelAtualCategoriaDespesa(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				}
			} else {
				setCategoriaDespesaVO(null);
				getCategoriaDespesaVO().setDescricao("Todas as categorias de despesas");
			}
			setDataInicioMesAno(Uteis.getDataVencimentoPadrao(1, getDataInicioMesAno(), 0));
			setDataFinalMesAno(Uteis.getDataUltimoDiaMes(getDataFinalMesAno()));
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getContaPagarFacade().consultarContaPagarPorUnidadesCategoriaDespesaPeriodo(getPainelGestorVO().getNivelAtualCategoriaDespesa(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getPainelGestorVO().getTrazerContasPagas(), getPainelGestorVO().getTrazerContasAPagar(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getContaPagarFacade().consultarTotalRegistroContaPagarPorUnidadesCategoriaDespesaPeriodo(getPainelGestorVO().getNivelAtualCategoriaDespesa(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getPainelGestorVO().getTrazerContasPagas(), getPainelGestorVO().getTrazerContasAPagar()));
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);			
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorPorCategoriaDespesaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorPorCategoriaDespesaForm.xhtml");
		}
	}
	
	public String consultarDadosPainelGestorCategoriaDespesaDepartamento() {
		try {
			getFacadeFactory().getPainelGestorFacade().executarCriacaoGraficoCategoriaDespesa(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getPainelGestorVO().getNivelAtualCategoriaDespesa(), getPainelGestorVO().getCodigoDepartamento(), false, 0, 0);
			getControleConsultaOtimizado().setLimitePorPagina(50);
			getControleConsultaOtimizado().setPage(0);
			getControleConsultaOtimizado().setPaginaAtual(1);
			if (getPainelGestorVO().getNivelAtualCategoriaDespesa() != null && !getPainelGestorVO().getNivelAtualCategoriaDespesa().trim().isEmpty()) {
				if (getPainelGestorVO().getNivelAtualCategoriaDespesa().equals("-1")) {
					setCategoriaDespesaVO(null);
					getCategoriaDespesaVO().setDescricao("Categoria Não Informada");
				} else {
					setCategoriaDespesaVO(getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesaUnico(getPainelGestorVO().getNivelAtualCategoriaDespesa(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				}
			} else {
				setCategoriaDespesaVO(null);
				getCategoriaDespesaVO().setDescricao("Todas as categorias de despesas");
			}
			setDataInicioMesAno(Uteis.getDataVencimentoPadrao(1, getDataInicioMesAno(), 0));
			setDataFinalMesAno(Uteis.getDataUltimoDiaMes(getDataFinalMesAno()));
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getContaPagarFacade().consultarContaPagarPorUnidadesCategoriaDespesaPorDepartamento(getPainelGestorVO().getNivelAtualCategoriaDespesa(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getDataCompetenciaInicial(), getDataCompetenciaFinal(), getPainelGestorVO().getTrazerContasPagas(), getPainelGestorVO().getTrazerContasAPagar(), getPainelGestorVO().getCodigoDepartamento(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getContaPagarFacade().consultarTotalRegistroContaPagarPorUnidadesCategoriaDespesaPeriodoDepartamento(getPainelGestorVO().getNivelAtualCategoriaDespesa(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getDataCompetenciaInicial(), getDataCompetenciaFinal(), getPainelGestorVO().getTrazerContasPagas(), getPainelGestorVO().getTrazerContasAPagar(), getPainelGestorVO().getCodigoDepartamento()));
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorPorCategoriaDespesaDepartamentoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorPorCategoriaDespesaDepartamentoForm.xhtml");
		}
	}

	public String consultarDadosPainelGestorCategoriaDespesaPorDepartamento() {
		try {
			getFacadeFactory().getPainelGestorFacade().executarCriacaoGraficoCategoriaDespesaPorDepartamento(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), false, getPainelGestorVO().getCodigoDepartamento(), getDataCompetenciaInicial(), getDataCompetenciaFinal());
			getControleConsultaOtimizado().setLimitePorPagina(50);
			getControleConsultaOtimizado().setPage(0);
			getControleConsultaOtimizado().setPaginaAtual(1);
			if (getPainelGestorVO().getNivelAtualCategoriaDespesa() != null && !getPainelGestorVO().getNivelAtualCategoriaDespesa().trim().isEmpty()) {
				if (getPainelGestorVO().getNivelAtualCategoriaDespesa().equals("-1")) {
					setCategoriaDespesaVO(null);
					getCategoriaDespesaVO().setDescricao("Categoria Não Informada");
				} else {
					setCategoriaDespesaVO(getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesaUnico(getPainelGestorVO().getNivelAtualCategoriaDespesa(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				}
			} else {
				setCategoriaDespesaVO(null);
				getCategoriaDespesaVO().setDescricao("Todas as categorias de despesas");
			}
			setDataInicioMesAno(Uteis.getDataVencimentoPadrao(1, getDataInicioMesAno(), 0));
			setDataFinalMesAno(Uteis.getDataUltimoDiaMes(getDataFinalMesAno()));
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getContaPagarFacade().consultarContaPagarPorUnidadesCategoriaDespesaPorDepartamento(getPainelGestorVO().getNivelAtualCategoriaDespesa(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getDataCompetenciaInicial(), getDataCompetenciaFinal(), getPainelGestorVO().getTrazerContasPagas(), getPainelGestorVO().getTrazerContasAPagar(), getPainelGestorVO().getCodigoDepartamento(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getContaPagarFacade().consultarTotalRegistroContaPagarPorUnidadesCategoriaDespesaPeriodoDepartamento(getPainelGestorVO().getNivelAtualCategoriaDespesa(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getDataCompetenciaInicial(), getDataCompetenciaFinal(), getPainelGestorVO().getTrazerContasPagas(), getPainelGestorVO().getTrazerContasAPagar(), getPainelGestorVO().getCodigoDepartamento()));
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorPorCategoriaDespesaDepartamentoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorPorCategoriaDespesaDepartamentoForm.xhtml");
		}
	}

	public void scrollContaPagarCategoriaDespesa(DataScrollEvent DataScrollEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
			getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
			setDataInicioMesAno(Uteis.getDataVencimentoPadrao(1, getDataInicioMesAno(), 0));
			setDataFinalMesAno(Uteis.getDataUltimoDiaMes(getDataFinalMesAno()));
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getContaPagarFacade().consultarContaPagarPorUnidadesCategoriaDespesaPeriodo(getPainelGestorVO().getNivelAtualCategoriaDespesa(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getPainelGestorVO().getTrazerContasPagas(), getPainelGestorVO().getTrazerContasAPagar(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getContaPagarFacade().consultarTotalRegistroContaPagarPorUnidadesCategoriaDespesaPeriodo(getPainelGestorVO().getNivelAtualCategoriaDespesa(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getPainelGestorVO().getTrazerContasPagas(), getPainelGestorVO().getTrazerContasAPagar()));
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void scrollContaPagarCategoriaDespesaDepartamento(DataScrollEvent DataScrollEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
			getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
			setDataInicioMesAno(Uteis.getDataVencimentoPadrao(1, getDataInicioMesAno(), 0));
			setDataFinalMesAno(Uteis.getDataUltimoDiaMes(getDataFinalMesAno()));
			if (Uteis.isAtributoPreenchido(getDataCompetenciaFinal())) {
				setDataCompetenciaFinal(Uteis.getDataUltimoDiaMes(getDataCompetenciaFinal()));
			}
			if (Uteis.isAtributoPreenchido(getDataCompetenciaInicial())) {
				setDataCompetenciaInicial(Uteis.getDataVencimentoPadrao(1, getDataCompetenciaInicial(), 0));
			}
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getContaPagarFacade().consultarContaPagarPorUnidadesCategoriaDespesaPorDepartamento(getPainelGestorVO().getNivelAtualCategoriaDespesa(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getDataCompetenciaInicial(), getDataCompetenciaFinal(), getPainelGestorVO().getTrazerContasPagas(), getPainelGestorVO().getTrazerContasAPagar(), getPainelGestorVO().getCodigoDepartamento(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getContaPagarFacade().consultarTotalRegistroContaPagarPorUnidadesCategoriaDespesaPeriodoDepartamento(getPainelGestorVO().getNivelAtualCategoriaDespesa(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getDataCompetenciaInicial(), getDataCompetenciaFinal(), getPainelGestorVO().getTrazerContasPagas(), getPainelGestorVO().getTrazerContasAPagar(), getPainelGestorVO().getCodigoDepartamento()));
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String consultarDadosPainelGestorMonitoramentoDescontoInstituicaoPeloPainelMonitoramentoDesconto() {
		try {
			setMesAno("");
			// getPainelGestorVO().setDataInicioPorNivelEducacional(getDataInicio());
			// getPainelGestorVO().setDataFinalPorNivelEducacional(getDataFinal());
			setTipoNivelEducacional(null);
			setCursoVO(null);
			setTurmaVO(null);
			inicializarDadosListaNivelEducacional();
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoInstituicao(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), null, null, null);
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String consultarDadosPainelGestorMonitoramentoDescontoConvenioPeloPainelMonitoramentoDesconto() {
		try {
			setMesAno("");
			// getPainelGestorVO().setDataInicioPorNivelEducacional(getDataInicio());
			// getPainelGestorVO().setDataFinalPorNivelEducacional(getDataFinal());
			setTipoNivelEducacional(null);
			setCursoVO(null);
			setTurmaVO(null);
			inicializarDadosListaNivelEducacional();
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoConvenio(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), null, null, null);
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoConvenioForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	/**
	 * Chamado pela tela de Monitoramento de Desconto Fluxo: Painel Gestor
	 * Inicial --> Clique no Botï¿½o Monotirar desconto --> Clique no Botï¿½o do
	 * Rodapï¿½ Detakhar Desc. Prog.
	 * 
	 * @return
	 */
	public String consultarDadosPainelGestorMonitoramentoDescontoProgressivoPeloPainelMonitoramentoDesconto() {
		try {
			setMesAno("");
			// getPainelGestorVO().setDataInicioPorCurso(getDataInicio());
			// getPainelGestorVO().setDataFinalPorCurso(getDataFinal());
			setTipoNivelEducacional(null);
			setCursoVO(null);
			setTurmaVO(null);
			inicializarDadosListaNivelEducacional();
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoProgressivo(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), null, null, null);
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoProgressivoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String consultarDadosPainelGestorMonitoramentoDescontoInstituicao() {
		try {
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoInstituicao(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getCursoVO(), getTipoNivelEducacional_Enum(), "");
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoInstituicaoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			getPainelGestorVO().getPainelGestorMonitoramentoDescontoInstituicaoVOs().clear();
			return "";
		}
	}

	public String consultarDadosPainelGestorMonitoramentoDescontoConvenio() {
		try {
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoConvenio(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getCursoVO(), getTipoNivelEducacional_Enum(), "");
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoConvenioForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			getPainelGestorVO().getPainelGestorMonitoramentoDescontoConvenioVOs().clear();
			return "";
		}
	}

	/**
	 * Chamado a partir da propria tela de monitoramento de desconto progressivo
	 * 
	 * @return
	 */
	public String consultarDadosPainelGestorMonitoramentoDescontoProgressivo() {
		try {
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoProgressivo(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getCursoVO(), getTipoNivelEducacional_Enum(), getMesAno());
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoProgressivoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			getPainelGestorVO().getPainelGestorMonitoramentoDescontoProgressivoVOs().clear();
			return "";
		}
	}

	public String consultarDadosPainelGestorMonitoramentoDescontoInstituicaoPeloPainelGestorMonitoramentoDescontoPorNivelEducacional() {
		try {
			setMesAno("");
			setCursoVO(null);
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoInstituicao(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), null, getTipoNivelEducacional_Enum(), "");
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoInstituicao.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
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

	public String consultarDadosPainelGestorMonitoramentoDescontoInstituicaoPeloPainelGestorFinanceiroPorNivelEducacionalEPorCurso() {
		try {
			PainelGestorContaReceberMesAnoVO painelGestorContaReceberMesAnoVO = (PainelGestorContaReceberMesAnoVO) context().getExternalContext().getRequestMap().get("contaReceberMesAnoItens");
			getCursoVO().setCodigo(painelGestorContaReceberMesAnoVO.getCodigoCurso());
			getCursoVO().setNome(painelGestorContaReceberMesAnoVO.getCurso());
			// setMesAno("");
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoInstituicao(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataInicioMesAno(), getCursoVO(), getTipoNivelEducacional_Enum(), getMesAno());
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoInstituicaoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String consultarDadosPainelGestorMonitoramentoDescontoConvenioPeloPainelGestorFinanceiroPorNivelEducacionalEPorCurso() {
		try {
			PainelGestorContaReceberMesAnoVO painelGestorContaReceberMesAnoVO = (PainelGestorContaReceberMesAnoVO) context().getExternalContext().getRequestMap().get("contaReceberMesAnoItens");
			getCursoVO().setCodigo(painelGestorContaReceberMesAnoVO.getCodigoCurso());
			getCursoVO().setNome(painelGestorContaReceberMesAnoVO.getCurso());
			// setMesAno("");
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoConvenio(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getCursoVO(), getTipoNivelEducacional_Enum(), getMesAno());
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoConvenioForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	/**
	 * Este mï¿½todo ï¿½ chamado da tela de monitoramento de receitas por nivel
	 * educacional; Fluxo: Painel Gestor Inicial-->Clique no Grafico
	 * Financeiro-->Lista de Receitas Clique no icone grafico pizza-->Na lista
	 * de curso clique no icone do cifrï¿½o de detalhe por desconto progressivo
	 * 
	 * @return
	 */
	public String consultarDadosPainelGestorMonitoramentoDescontoProgressivoPeloPainelGestorFinanceiroPorNivelEducacionalEPorCurso() {
		try {
			PainelGestorContaReceberMesAnoVO painelGestorContaReceberMesAnoVO = (PainelGestorContaReceberMesAnoVO) context().getExternalContext().getRequestMap().get("contaReceberMesAnoItens");
			getCursoVO().setCodigo(painelGestorContaReceberMesAnoVO.getCodigoCurso());
			getCursoVO().setNome(painelGestorContaReceberMesAnoVO.getCurso());
			// getPainelGestorVO().setDataInicioPorCurso(getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getDataInicioPorNivelEducacional());
			// getPainelGestorVO().setDataFinalPorCurso(getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getDataFinalPorNivelEducacional());
			// setMesAno("");
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoProgressivo(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getCursoVO(), getTipoNivelEducacional_Enum(), getMesAno());
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoProgressivoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String consultarDadosPainelGestorMonitoramentoDescontoTurmaProgressivoPeloPainelGestorFinanceiroPorTurmaEPorCurso() {
		try {
			Object item = context().getExternalContext().getRequestMap().get("contaReceberMesAnoItens");
			if (item instanceof PainelGestorContaReceberMesAnoVO) {
				PainelGestorContaReceberMesAnoVO painelGestorContaReceberMesAnoVO = (PainelGestorContaReceberMesAnoVO) item;
				getCursoVO().setCodigo(painelGestorContaReceberMesAnoVO.getCodigoCurso());
				getCursoVO().setNome(painelGestorContaReceberMesAnoVO.getCurso());
				getTurmaVO().setCodigo(painelGestorContaReceberMesAnoVO.getCodigoTurma());
				getTurmaVO().setIdentificadorTurma(painelGestorContaReceberMesAnoVO.getTurma());
			}
			if (item instanceof PainelGestorFinanceiroAcademicoNivelEducacionalVO) {
				PainelGestorFinanceiroAcademicoNivelEducacionalVO painelGestorContaReceberMesAnoVO = (PainelGestorFinanceiroAcademicoNivelEducacionalVO) item;
				getCursoVO().setCodigo(painelGestorContaReceberMesAnoVO.getCodigoCurso());
				getCursoVO().setNome(painelGestorContaReceberMesAnoVO.getCurso());
				getTurmaVO().setCodigo(painelGestorContaReceberMesAnoVO.getCodigoTurma());
				getTurmaVO().setIdentificadorTurma(painelGestorContaReceberMesAnoVO.getTurma());
			}
			if (item instanceof PainelGestorMonitoramentoDescontoNivelEducacionalVO) {
				PainelGestorMonitoramentoDescontoNivelEducacionalVO painelGestorContaReceberMesAnoVO = (PainelGestorMonitoramentoDescontoNivelEducacionalVO) item;
				getCursoVO().setCodigo(painelGestorContaReceberMesAnoVO.getCodigoCurso());
				getCursoVO().setNome(painelGestorContaReceberMesAnoVO.getCurso());
				getTurmaVO().setCodigo(painelGestorContaReceberMesAnoVO.getCodigoTurma());
				getTurmaVO().setIdentificadorTurma(painelGestorContaReceberMesAnoVO.getTurma());
			}
			// setMesAno("");
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoProgressivoTurma(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getCursoVO(), getTurmaVO(), getTipoNivelEducacional_Enum(), getMesAno());
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoTurmaProgressivoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String consultarDadosPainelGestorMonitoramentoDescontoProgressivoPorTurma() {
		try {
			// PainelGestorContaReceberMesAnoVO painelGestorContaReceberMesAnoVO
			// = (PainelGestorContaReceberMesAnoVO)
			// context().getExternalContext().getRequestMap().get("contaReceberMesAno");
			// getCursoVO().setCodigo(painelGestorContaReceberMesAnoVO.getCodigoCurso());
			// getCursoVO().setNome(painelGestorContaReceberMesAnoVO.getCurso());
			// getTurmaVO().setCodigo(painelGestorContaReceberMesAnoVO.getCodigoTurma());
			// getTurmaVO().setIdentificadorTurma(painelGestorContaReceberMesAnoVO.getTurma());
			// setMesAno("");
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoProgressivoTurma(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getCursoVO(), getTurmaVO(), getTipoNivelEducacional_Enum(), getMesAno());
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoTurmaProgressivoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String consultarDadosPainelGestorMonitoramentoDescontoInstituicaoPeloPainelGestorMonitoramentoDescontoPorNivelEducacionalEPorCurso() {
		try {
			PainelGestorMonitoramentoDescontoNivelEducacionalVO painelGestorMonitoramentoDescontoNivelEducacionalVO = (PainelGestorMonitoramentoDescontoNivelEducacionalVO) context().getExternalContext().getRequestMap().get("nivelItens");
			getCursoVO().setCodigo(painelGestorMonitoramentoDescontoNivelEducacionalVO.getCodigoCurso());
			getCursoVO().setNome(painelGestorMonitoramentoDescontoNivelEducacionalVO.getCurso());
			// setMesAno("");
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoInstituicao(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getCursoVO(), getTipoNivelEducacional_Enum(), getMesAno());
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoInstituicaoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String consultarDadosPainelGestorMonitoramentoDescontoTurmaInstituicaoPeloPainelGestorMonitoramentoDescontoPorTurmaEPorCurso() {
		try {
			Object item = context().getExternalContext().getRequestMap().get("contaReceberMesAnoItens");
			if (item instanceof PainelGestorContaReceberMesAnoVO) {
				PainelGestorContaReceberMesAnoVO painelGestorContaReceberMesAnoVO = (PainelGestorContaReceberMesAnoVO) item;
				getCursoVO().setCodigo(painelGestorContaReceberMesAnoVO.getCodigoCurso());
				getCursoVO().setNome(painelGestorContaReceberMesAnoVO.getCurso());
				getTurmaVO().setCodigo(painelGestorContaReceberMesAnoVO.getCodigoTurma());
				getTurmaVO().setIdentificadorTurma(painelGestorContaReceberMesAnoVO.getTurma());
			}
			if (item instanceof PainelGestorFinanceiroAcademicoNivelEducacionalVO) {
				PainelGestorFinanceiroAcademicoNivelEducacionalVO painelGestorContaReceberMesAnoVO = (PainelGestorFinanceiroAcademicoNivelEducacionalVO) item;
				getCursoVO().setCodigo(painelGestorContaReceberMesAnoVO.getCodigoCurso());
				getCursoVO().setNome(painelGestorContaReceberMesAnoVO.getCurso());
				getTurmaVO().setCodigo(painelGestorContaReceberMesAnoVO.getCodigoTurma());
				getTurmaVO().setIdentificadorTurma(painelGestorContaReceberMesAnoVO.getTurma());
			}
			if (item instanceof PainelGestorMonitoramentoDescontoNivelEducacionalVO) {
				PainelGestorMonitoramentoDescontoNivelEducacionalVO painelGestorContaReceberMesAnoVO = (PainelGestorMonitoramentoDescontoNivelEducacionalVO) item;
				getCursoVO().setCodigo(painelGestorContaReceberMesAnoVO.getCodigoCurso());
				getCursoVO().setNome(painelGestorContaReceberMesAnoVO.getCurso());
				getTurmaVO().setCodigo(painelGestorContaReceberMesAnoVO.getCodigoTurma());
				getTurmaVO().setIdentificadorTurma(painelGestorContaReceberMesAnoVO.getTurma());
			}
			// setMesAno("");
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoInstituicaoTurma(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getCursoVO(), getTurmaVO(), getTipoNivelEducacional_Enum(), getMesAno());
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoTurmaInstituicaoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String consultarDadosPainelGestorMonitoramentoDescontoTurmaInstituicao() {
		try {
			// PainelGestorContaReceberMesAnoVO painelGestorContaReceberMesAnoVO
			// = (PainelGestorContaReceberMesAnoVO)
			// context().getExternalContext().getRequestMap().get("contaReceberMesAno");
			// getCursoVO().setCodigo(painelGestorContaReceberMesAnoVO.getCodigoCurso());
			// getCursoVO().setNome(painelGestorContaReceberMesAnoVO.getCurso());
			// getTurmaVO().setCodigo(painelGestorContaReceberMesAnoVO.getCodigoTurma());
			// getTurmaVO().setIdentificadorTurma(painelGestorContaReceberMesAnoVO.getTurma());
			// setMesAno("");
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoInstituicaoTurma(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getCursoVO(), getTurmaVO(), getTipoNivelEducacional_Enum(), getMesAno());
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoTurmaInstituicaoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String consultarDadosPainelGestorMonitoramentoDescontoConvenioPeloPainelGestorMonitoramentoDescontoPorNivelEducacionalEPorCurso() {
		try {
			PainelGestorMonitoramentoDescontoNivelEducacionalVO painelGestorMonitoramentoDescontoNivelEducacionalVO = (PainelGestorMonitoramentoDescontoNivelEducacionalVO) context().getExternalContext().getRequestMap().get("nivelItens");
			getCursoVO().setCodigo(painelGestorMonitoramentoDescontoNivelEducacionalVO.getCodigoCurso());
			getCursoVO().setNome(painelGestorMonitoramentoDescontoNivelEducacionalVO.getCurso());
			// setMesAno("");
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoConvenio(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getCursoVO(), getTipoNivelEducacional_Enum(), getMesAno());
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoConvenioForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String consultarDadosPainelGestorMonitoramentoDescontoTurmaConvenioPeloPainelGestorMonitoramentoDescontoPorTurmaEPorCurso() {
		try {
			Object item = context().getExternalContext().getRequestMap().get("contaReceberMesAnoItens");
			if (item instanceof PainelGestorContaReceberMesAnoVO) {
				PainelGestorContaReceberMesAnoVO painelGestorContaReceberMesAnoVO = (PainelGestorContaReceberMesAnoVO) item;
				getCursoVO().setCodigo(painelGestorContaReceberMesAnoVO.getCodigoCurso());
				getCursoVO().setNome(painelGestorContaReceberMesAnoVO.getCurso());
				getTurmaVO().setCodigo(painelGestorContaReceberMesAnoVO.getCodigoTurma());
				getTurmaVO().setIdentificadorTurma(painelGestorContaReceberMesAnoVO.getTurma());
			}
			if (item instanceof PainelGestorFinanceiroAcademicoNivelEducacionalVO) {
				PainelGestorFinanceiroAcademicoNivelEducacionalVO painelGestorContaReceberMesAnoVO = (PainelGestorFinanceiroAcademicoNivelEducacionalVO) item;
				getCursoVO().setCodigo(painelGestorContaReceberMesAnoVO.getCodigoCurso());
				getCursoVO().setNome(painelGestorContaReceberMesAnoVO.getCurso());
				getTurmaVO().setCodigo(painelGestorContaReceberMesAnoVO.getCodigoTurma());
				getTurmaVO().setIdentificadorTurma(painelGestorContaReceberMesAnoVO.getTurma());
			}
			if (item instanceof PainelGestorMonitoramentoDescontoNivelEducacionalVO) {
				PainelGestorMonitoramentoDescontoNivelEducacionalVO painelGestorContaReceberMesAnoVO = (PainelGestorMonitoramentoDescontoNivelEducacionalVO) item;
				getCursoVO().setCodigo(painelGestorContaReceberMesAnoVO.getCodigoCurso());
				getCursoVO().setNome(painelGestorContaReceberMesAnoVO.getCurso());
				getTurmaVO().setCodigo(painelGestorContaReceberMesAnoVO.getCodigoTurma());
				getTurmaVO().setIdentificadorTurma(painelGestorContaReceberMesAnoVO.getTurma());
			}
			// setMesAno("");
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoConvenioTurma(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getCursoVO(), getTurmaVO(), getTipoNivelEducacional_Enum(), getMesAno());
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoTurmaConvenioForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String consultarDadosPainelGestorMonitoramentoDescontoTurmaConvenio() {
		try {
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoConvenioTurma(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getCursoVO(), getTurmaVO(), getTipoNivelEducacional_Enum(), getMesAno());
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoTurmaConvenioForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String consultarDadosPainelGestorMonitoramentoDescontoProgressivoPeloPainelGestorMonitoramentoDescontoPorNivelEducacionalEPorCurso() {
		try {
			PainelGestorMonitoramentoDescontoNivelEducacionalVO painelGestorMonitoramentoDescontoNivelEducacionalVO = (PainelGestorMonitoramentoDescontoNivelEducacionalVO) context().getExternalContext().getRequestMap().get("nivelItens");
			getCursoVO().setCodigo(painelGestorMonitoramentoDescontoNivelEducacionalVO.getCodigoCurso());
			getCursoVO().setNome(painelGestorMonitoramentoDescontoNivelEducacionalVO.getCurso());
			setTipoNivelEducacional(painelGestorMonitoramentoDescontoNivelEducacionalVO.getNivel());
			// getPainelGestorVO().setDataInicioPorCurso(getPainelGestorVO().getDataInicioPorNivelEducacional());
			// getPainelGestorVO().setDataFinalPorCurso(getPainelGestorVO().getDataFinalPorNivelEducacional());

			// setMesAno("");
			inicializarDadosListaNivelEducacional();
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoProgressivo(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getCursoVO(), getTipoNivelEducacional_Enum(), getMesAno());
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoProgressivoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String consultarDadosPainelGestorMonitoramentoDescontoInstituicaoPeloPainelGestorMonitoramentoDescontoPorNivelEducacionalMesAno() {
		try {

			PainelGestorMonitoramentoDescontoNivelEducacionalVO painelGestorMonitoramentoDescontoNivelEducacionalVO = (PainelGestorMonitoramentoDescontoNivelEducacionalVO) UteisJSF.context().getExternalContext().getRequestMap().get("nivelItens");
			setMesAno(painelGestorMonitoramentoDescontoNivelEducacionalVO.getMesAno());
			inicializarDataMesAno();
			setTipoNivelEducacional(painelGestorMonitoramentoDescontoNivelEducacionalVO.getNivel());
			setCursoVO(null);
			inicializarDadosListaNivelEducacional();
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoInstituicao(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getCursoVO(), getTipoNivelEducacional_Enum(), getMesAno());
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoInstituicaoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String consultarDadosPainelGestorMonitoramentoDescontoConvenioPeloPainelGestorMonitoramentoDescontoPorNivelEducacionalMesAno() {
		try {
			PainelGestorMonitoramentoDescontoNivelEducacionalVO painelGestorMonitoramentoDescontoNivelEducacionalVO = (PainelGestorMonitoramentoDescontoNivelEducacionalVO) UteisJSF.context().getExternalContext().getRequestMap().get("nivelItens");
			setMesAno(painelGestorMonitoramentoDescontoNivelEducacionalVO.getMesAno());
			inicializarDataMesAno();
			setTipoNivelEducacional(painelGestorMonitoramentoDescontoNivelEducacionalVO.getNivel());
			setCursoVO(null);
			inicializarDadosListaNivelEducacional();
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoConvenio(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getCursoVO(), getTipoNivelEducacional_Enum(), getMesAno());
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoConvenioForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String realizarConsultarDadosPainelGestorMonitoramentoDescontoProgressivoPorTurma() {
		try {
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoProgressivoPorTurma(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getTurmaVO().getCodigo(), getTipoNivelEducacional_Enum(), "");
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoProgressivoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			getPainelGestorVO().getPainelGestorMonitoramentoDescontoProgressivoVOs().clear();
			return "";
		}
	}

	public String consultarDadosPainelGestorMonitoramentoDescontoProgressivoPeloPainelGestorMonitoramentoDescontoPorNivelEducacionalMesAno() {
		try {
			PainelGestorMonitoramentoDescontoNivelEducacionalVO painelGestorMonitoramentoDescontoNivelEducacionalVO = (PainelGestorMonitoramentoDescontoNivelEducacionalVO) UteisJSF.context().getExternalContext().getRequestMap().get("nivelItens");
			setMesAno(painelGestorMonitoramentoDescontoNivelEducacionalVO.getMesAno());
			inicializarDataMesAno();
			setTipoNivelEducacional(painelGestorMonitoramentoDescontoNivelEducacionalVO.getNivel());
			setCursoVO(null);
			inicializarDadosListaNivelEducacional();
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoProgressivo(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getCursoVO(), getTipoNivelEducacional_Enum(), getMesAno());
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoProgressivoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	// public String realizarVoltarOrigemPainelGestorMonitoramentoDesconto() {
	// if
	// (getPaginaVoltarDesconto().equals("painelGestorMonitoramentoDesconto")) {
	// consultarDadosPainelGestorMonitoramentoDesconto();
	// } else if
	// (getPaginaVoltarDesconto().equals("painelGestorMonitoramentoDescontoPorNivelEducacional"))
	// {
	// executarGeracaoDadosPainelGestorMonitoramentoDescontoPorNivelEducacional();
	// }
	// return getPaginaVoltarDesconto();
	// }

	// public String
	// realizarVoltarOrigemPainelGestorMonitoramentoDescontoPorNivel() {
	// if
	// (getPaginaVoltarDescontoNivelEducacional().equals("painelGestorMonitoramentoDesconto"))
	// {
	// consultarDadosPainelGestorMonitoramentoDesconto();
	// } else if
	// (getPaginaVoltarDescontoNivelEducacional().equals("painelGestorMonitoramentoDescontoPorNivelEducacional"))
	// {
	// executarGeracaoDadosPainelGestorMonitoramentoDescontoPorNivelEducacional();
	// }
	// return getPaginaVoltarDescontoNivelEducacional();
	// }

	// public String
	// realizarVoltarOrigemPainelGestorMonitoramentoDescontoInstituicao() {
	// if
	// (getPaginaVoltarDescontoInstituicao().equals("painelGestorMonitoramentoDesconto"))
	// {
	// consultarDadosPainelGestorMonitoramentoDesconto();
	// } else if
	// (getPaginaVoltarDescontoInstituicao().equals("painelGestorMonitoramentoDescontoPorNivelEducacional"))
	// {
	// executarGeracaoDadosPainelGestorMonitoramentoDescontoPorNivelEducacional();
	// }
	// return getPaginaVoltarDescontoInstituicao();
	// }
	//
	// public String
	// realizarVoltarOrigemPainelGestorMonitoramentoDescontoConvenio() {
	// if
	// (getPaginaVoltarDescontoConvenio().equals("painelGestorMonitoramentoDesconto"))
	// {
	// consultarDadosPainelGestorMonitoramentoDesconto();
	// } else if
	// (getPaginaVoltarDescontoConvenio().equals("painelGestorMonitoramentoDescontoPorNivelEducacional"))
	// {
	// executarGeracaoDadosPainelGestorMonitoramentoDescontoPorNivelEducacional();
	// }
	// return getPaginaVoltarDescontoConvenio();
	// }
	//
	// public String
	// realizarVoltarOrigemPainelGestorMonitoramentoDescontoProgressivo() {
	// if
	// (getPaginaVoltarDescontoProgressivo().equals("painelGestorMonitoramentoDesconto"))
	// {
	// consultarDadosPainelGestorMonitoramentoDesconto();
	// } else if
	// (getPaginaVoltarDescontoProgressivo().equals("painelGestorMonitoramentoDescontoPorNivelEducacional"))
	// {
	// executarGeracaoDadosPainelGestorMonitoramentoDescontoPorNivelEducacional();
	// } else if
	// (getPaginaVoltarDescontoProgressivo().equals("painelGestorMonitoramentoDescontoPorTurma"))
	// {
	// //
	// executarGeracaoDadosPainelGestorMonitoramentoDescontoPorNivelEducacional();
	// }
	// return getPaginaVoltarDescontoProgressivo();
	// }

	public String consultarDadosPainelGestorMonitoramentoDesconto() {
		try {
			if (!getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAGINA_INICIAL) && !getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO) && !getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO_ACADEMICO)) {
				if (getMesAno().trim().isEmpty()) {
					setDataInicio(getDataInicioMesAno());
					setDataFinal(getDataFinalMesAno());
				}
				// setTelaOrigem(ControlePaginaPainelGestorEnum.PAGINA_INICIAL);
			}
			getFacadeFactory().getPainelGestorFacade().consultarDadosPainelGestorMonitoramentoDesconto(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal());
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String consultarDadosPainelGestorMonitoramentoConsultor() {
		try {
			if (!getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAGINA_INICIAL) && !getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO) && !getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO_ACADEMICO)) {
				if (getMesAno().trim().isEmpty()) {
					setDataInicio(getDataInicioMesAno());
					setDataFinal(getDataFinalMesAno());
				}
			}
			getFacadeFactory().getPainelGestorFacade().consultarDadosPainelGestorMonitoramentoConsultor(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), getCursoVO().getCodigo(), getTurmaVO().getCodigo());
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoConsultorCRM.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String detalharMatRec() {
		try {
			PainelGestorMonitoramentoConsultorVO consultor = (PainelGestorMonitoramentoConsultorVO) context().getExternalContext().getRequestMap().get("consultorItens");
			getPainelGestorVO().getPainelGestorMonitoramentoConsultorDetalhamentoVOs().clear();
			getPainelGestorVO().setApresentarDocPainelGestorMonitoramentoConsultorDetalhamentoVOs(Boolean.FALSE);
			getFacadeFactory().getPainelGestorFacade().consultarDadosPainelGestorMonitoramentoConsultorMatRec(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), consultor.getCodConsultor(), consultor.getCodTurma());
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoConsultorCRMDetalhe.xhtml");
		} catch (Exception e) {
			consultarDadosPainelGestorMonitoramentoConsultor();
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoConsultorCRM.xhtml");
		}
	}

	public String detalharMatARec() {
		try {
			PainelGestorMonitoramentoConsultorVO consultor = (PainelGestorMonitoramentoConsultorVO) context().getExternalContext().getRequestMap().get("consultorItens");
			getPainelGestorVO().getPainelGestorMonitoramentoConsultorDetalhamentoVOs().clear();
			getPainelGestorVO().setApresentarDocPainelGestorMonitoramentoConsultorDetalhamentoVOs(Boolean.FALSE);
			getFacadeFactory().getPainelGestorFacade().consultarDadosPainelGestorMonitoramentoConsultorMatARec(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), consultor.getCodConsultor(), consultor.getCodTurma());
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoConsultorCRMDetalhe.xhtml");
		} catch (Exception e) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoConsultorCRM.xhtml");
		}
	}

	public String detalharMatVencida() {
		try {
			PainelGestorMonitoramentoConsultorVO consultor = (PainelGestorMonitoramentoConsultorVO) context().getExternalContext().getRequestMap().get("consultorItens");
			getPainelGestorVO().getPainelGestorMonitoramentoConsultorDetalhamentoVOs().clear();
			getPainelGestorVO().setApresentarDocPainelGestorMonitoramentoConsultorDetalhamentoVOs(Boolean.FALSE);
			getFacadeFactory().getPainelGestorFacade().consultarDadosPainelGestorMonitoramentoConsultorMatVencida(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), consultor.getCodConsultor(), consultor.getCodTurma());
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoConsultorCRMDetalhe.xhtml");
		} catch (Exception e) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoConsultorCRM.xhtml");
		}
	}

	public String detalharMatAVencer() {
		try {
			PainelGestorMonitoramentoConsultorVO consultor = (PainelGestorMonitoramentoConsultorVO) context().getExternalContext().getRequestMap().get("consultorItens");
			getPainelGestorVO().getPainelGestorMonitoramentoConsultorDetalhamentoVOs().clear();
			getPainelGestorVO().setApresentarDocPainelGestorMonitoramentoConsultorDetalhamentoVOs(Boolean.FALSE);
			getFacadeFactory().getPainelGestorFacade().consultarDadosPainelGestorMonitoramentoConsultorMatAVencer(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), consultor.getCodConsultor(), consultor.getCodTurma());
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoConsultorCRMDetalhe.xhtml");
		} catch (Exception e) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoConsultorCRM.xhtml");
		}
	}

	public String detalharPreMat() {
		try {
			PainelGestorMonitoramentoConsultorVO consultor = (PainelGestorMonitoramentoConsultorVO) context().getExternalContext().getRequestMap().get("consultorItens");
			getPainelGestorVO().getPainelGestorMonitoramentoConsultorDetalhamentoVOs().clear();
			getPainelGestorVO().setApresentarDocPainelGestorMonitoramentoConsultorDetalhamentoVOs(Boolean.FALSE);
			getFacadeFactory().getPainelGestorFacade().consultarDadosPainelGestorMonitoramentoConsultorPreMat(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), consultor.getCodConsultor(), consultor.getCodTurma());
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoConsultorCRMDetalhe.xhtml");
		} catch (Exception e) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoConsultorCRM.xhtml");
		}
	}

	public String detalharMatAT() {
		try {
			PainelGestorMonitoramentoConsultorVO consultor = (PainelGestorMonitoramentoConsultorVO) context().getExternalContext().getRequestMap().get("consultorItens");
			getPainelGestorVO().getPainelGestorMonitoramentoConsultorDetalhamentoVOs().clear();
			getPainelGestorVO().setApresentarDocPainelGestorMonitoramentoConsultorDetalhamentoVOs(Boolean.FALSE);
			getFacadeFactory().getPainelGestorFacade().consultarDadosPainelGestorMonitoramentoConsultorMatAT(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), consultor.getCodConsultor(), consultor.getCodTurma());
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoConsultorCRMDetalhe.xhtml");
		} catch (Exception e) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoConsultorCRM.xhtml");
		}
	}

	public String detalharMatCanc() {
		try {
			PainelGestorMonitoramentoConsultorVO consultor = (PainelGestorMonitoramentoConsultorVO) context().getExternalContext().getRequestMap().get("consultorItens");
			getPainelGestorVO().getPainelGestorMonitoramentoConsultorDetalhamentoVOs().clear();
			getPainelGestorVO().setApresentarDocPainelGestorMonitoramentoConsultorDetalhamentoVOs(Boolean.FALSE);
			getFacadeFactory().getPainelGestorFacade().consultarDadosPainelGestorMonitoramentoConsultorMatCanc(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), consultor.getCodConsultor(), consultor.getCodTurma());
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoConsultorCRMDetalhe.xhtml");
		} catch (Exception e) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoConsultorCRM.xhtml");
		}
	}

	public String detalharMatExt() {
		try {
			PainelGestorMonitoramentoConsultorVO consultor = (PainelGestorMonitoramentoConsultorVO) context().getExternalContext().getRequestMap().get("consultorItens");
			getPainelGestorVO().getPainelGestorMonitoramentoConsultorDetalhamentoVOs().clear();
			getPainelGestorVO().setApresentarDocPainelGestorMonitoramentoConsultorDetalhamentoVOs(Boolean.FALSE);
			getFacadeFactory().getPainelGestorFacade().consultarDadosPainelGestorMonitoramentoConsultorMatExt(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), consultor.getCodConsultor(), consultor.getCodTurma());
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoConsultorCRMDetalhe.xhtml");
		} catch (Exception e) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoConsultorCRM.xhtml");
		}
	}

	public String detalharMatPendDoc() {
		try {
			PainelGestorMonitoramentoConsultorVO consultor = (PainelGestorMonitoramentoConsultorVO) context().getExternalContext().getRequestMap().get("consultorItens");
			getPainelGestorVO().getPainelGestorMonitoramentoConsultorDetalhamentoVOs().clear();
			getPainelGestorVO().setApresentarDocPainelGestorMonitoramentoConsultorDetalhamentoVOs(Boolean.TRUE);
			getFacadeFactory().getPainelGestorFacade().consultarDadosPainelGestorMonitoramentoConsultorMatPendDoc(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), consultor.getCodConsultor(), consultor.getCodTurma());
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoConsultorCRMDetalhe.xhtml");
		} catch (Exception e) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoConsultorCRM.xhtml");
		}
	}

	public void visualizarDoc() {
		try {
			PainelGestorMonitoramentoConsultorDetalhamentoVO aluno = (PainelGestorMonitoramentoConsultorDetalhamentoVO) context().getExternalContext().getRequestMap().get("consultorItens");
			setListaDoc(getFacadeFactory().getDocumetacaoMatriculaFacade().consultarPorSituacaoMatricula("PE", aluno.getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
			// return "painelGestorMonitoramentoConsultorCRMDetalhe";
		} catch (Exception e) {
			// return "painelGestorMonitoramentoConsultor";
		}
	}

	public String consultarDadosPainelGestorMonitoramentoDescontoPeloPainelGestorFinanceiroPorNivel() {
		try {
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoPorNivelEducacional(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getTipoNivelEducacional_Enum(), getMesAno());
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoNivelEducacionalForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoConsultorCRM.xhtml");
		}
	}

	public String selecionalObjPainelGestorMonitoramentoDescontoNivelEducacionalVO() throws Exception {
		PainelGestorMonitoramentoDescontoNivelEducacionalVO painelGestorMonitoramentoDescontoNivelEducacionalVO = (PainelGestorMonitoramentoDescontoNivelEducacionalVO) UteisJSF.context().getExternalContext().getRequestMap().get("nivelItens");
		setMesAno(painelGestorMonitoramentoDescontoNivelEducacionalVO.getMesAno());
		inicializarDataMesAno();
		setTipoNivelEducacional(painelGestorMonitoramentoDescontoNivelEducacionalVO.getNivel());
		inicializarDadosListaNivelEducacional();
		return executarGeracaoDadosPainelGestorMonitoramentoDescontoPorNivelEducacional();
	}

	public String realizarGeracaoPainelGestorMonitoramentoDescontoNivelEducacionalVO() {
		setMesAno("");
		setDataInicioMesAno(getDataInicio());
		setDataFinalMesAno(getDataFinal());
		return executarGeracaoDadosPainelGestorMonitoramentoDescontoPorNivelEducacional();
	}

	public void executarGeracaoDadosPainelGestorFinanceiroPorNivelEducacionalEPeriodo() throws Exception {
		setMesAno("");
		setDataInicioMesAno(getDataInicio());
		setDataFinalMesAno(getDataFinal());
		inicializarDadosListaNivelEducacional();
	}

	public void executarGeracaoDadosPainelGestorMonitoramentoDescontoPorNivelEducacionalEPeriodo() throws Exception {
		setMesAno("");
		setDataInicioMesAno(getDataInicio());
		setDataFinalMesAno(getDataFinal());
		inicializarDadosListaNivelEducacional();
	}

	// public String realizarNavegacaoPainelGestorFinanceiro() {
	// getPainelGestorVO().setPainelGestorPorNivelEducacionalVO(null);
	// return "painelGestorFinanceiro";
	// }

	// public String realizarNavegacaoPainelGestorFinanceiroPorNivel() {
	// getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().setPainelGestorContaReceberMesAnoTurmaVOs(null);
	// getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getPainelGestorContaReceberMesAnoTurmaVOs().clear();
	// getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().setCodigoCurso(0);
	// // getUnidadeEnsinoVOPorNivel().clear();
	// return "painelGestorFinanceiroPorNivel";
	// }

	public String executarGeracaoDadosPainelGestorFinanceiroPorTurma() {
		try {
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosMapaReceitaPorTurma(getPainelGestorVO(), "COMPETENCIA", getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getTipoNivelEducacional_Enum(), getMesAno(), getConfiguracaoFinanceiroPadraoSistema(), getPainelGestorVO().getCodigoCurso(), getFiltarDataReceitaPor());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);

			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorFinanceiroPorTurmaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String executarGeracaoDadosPainelGestorFinanceiroPorTurmaFluxoCaixa() {
		try {
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosMapaReceitaPorTurma(getPainelGestorVO(), "FLUXO_CAIXA", getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getTipoNivelEducacional_Enum(), getMesAno(), getConfiguracaoFinanceiroPadraoSistema(), getPainelGestorVO().getCodigoCurso(), getFiltarDataReceitaPor());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);

			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorFinanceiroPorTurmaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String executarGeracaoDadosPainelGestorMonitoramentoDescontoPorNivelEducacional() {
		try {
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoPorNivelEducacional(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getTipoNivelEducacional_Enum(), getMesAno());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoNivelEducacionalForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public List<SelectItem> getTipoNivelEducacionalEnumsNaoObrigatorio() {
		if (tipoNivelEducacionalEnumsNaoObrigatorio == null) {
			tipoNivelEducacionalEnumsNaoObrigatorio = new ArrayList<SelectItem>();
		}
		return tipoNivelEducacionalEnumsNaoObrigatorio;
	}

	public void setTipoNivelEducacionalEnumsNaoObrigatorio(List<SelectItem> tipoNivelEducacionalEnumsNaoObrigatorio) {
		this.tipoNivelEducacionalEnumsNaoObrigatorio = tipoNivelEducacionalEnumsNaoObrigatorio;
	}

	public void inicializarDadosListaNivelEducacional() throws Exception {
		List<TipoNivelEducacional> tipoNivelEducacionals = getFacadeFactory().getPainelGestorFacade().consultarNivelEducacionalPorUnidadeEnsino(getUnidadeEnsinoVOs());
		setTipoNivelEducacionalEnums(new ArrayList<SelectItem>());
		setTipoNivelEducacionalEnumsNaoObrigatorio(new ArrayList<SelectItem>());
		getTipoNivelEducacionalEnumsNaoObrigatorio().add(new SelectItem("", ""));
		for (TipoNivelEducacional tipoNivelEducacional1 : tipoNivelEducacionals) {
			getTipoNivelEducacionalEnums().add(new SelectItem(tipoNivelEducacional1.getValor(), tipoNivelEducacional1.getDescricao()));
			getTipoNivelEducacionalEnumsNaoObrigatorio().add(new SelectItem(tipoNivelEducacional1.getValor(), tipoNivelEducacional1.getDescricao()));
		}
	}

	public void inicializarDadosListaNivelEducacional2() {
		try {
		List<TipoNivelEducacional> tipoNivelEducacionals = getFacadeFactory().getPainelGestorFacade().consultarNivelEducacionalPorUnidadeEnsino(getUnidadeEnsinoVOs());
		setTipoNivelEducacionalEnums(new ArrayList<SelectItem>());
		getTipoNivelEducacionalEnums().add(new SelectItem("", "TODOS"));
		for (TipoNivelEducacional tipoNivelEducacional1 : tipoNivelEducacionals) {
			getTipoNivelEducacionalEnums().add(new SelectItem(tipoNivelEducacional1.getValor(), tipoNivelEducacional1.getDescricao()));
		}
		} catch(Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Date getDataFinal() {
		if (dataFinal == null) {
			dataFinal = Uteis.obterDataFutura(new Date(), 60);
			dataFinal = Uteis.getDataUltimoDiaMes(dataFinal);
		}
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = Uteis.obterDataAntiga(new Date(), 60);
			try {
				dataInicio = Uteis.gerarDataDiaMesAno(1, Uteis.getMesData(dataInicio), Uteis.getAnoData(dataInicio));
			} catch (Exception ex) {
				dataInicio = Uteis.obterDataAntiga(new Date(), -60);
			}
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public PainelGestorVO getPainelGestorVO() {
		if (painelGestorVO == null) {
			painelGestorVO = new PainelGestorVO();
		}
		return painelGestorVO;
	}

	public void setPainelGestorVO(PainelGestorVO painelGestorVO) {
		this.painelGestorVO = painelGestorVO;
	}

	public void removerUnidadeEnsino() {
		UnidadeEnsinoVO unidadeEnsinoVO = (UnidadeEnsinoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoItens");
		int index = 0;
		for (UnidadeEnsinoVO unidadeEnsinoVO1 : getUnidadeEnsinoVOs()) {
			if (unidadeEnsinoVO1.getCodigo().intValue() == unidadeEnsinoVO.getCodigo().intValue()) {
				getUnidadeEnsinoVOs().remove(index);
				validarDadosUnidadeEnsinoUltimoElementoLista(getUnidadeEnsinoVOs());
				listaSelectItemUnidadeEnsino = null;
				return;
			}
			index++;
		}
	}

	public void validarDadosUnidadeEnsinoUltimoElementoLista(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
		if (unidadeEnsinoVOs.isEmpty()) {

			getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs();
			getPainelGestorVO().getPainelGestorContaPagarMesAnoVOs().clear();
			getPainelGestorVO().getPainelGestorContaReceberMesAnoVOs().clear();
			getPainelGestorVO().getPainelGestorContaReceberFluxoCaixaMesAnoVOs().clear();
			getPainelGestorVO().getPainelGestorFinanceiroAcademicoMesAnoVOs().clear();
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por processar a consulta na entidade
	 * <code>UnidadeEnsino</code> por meio dos parametros informados no
	 * richmodal. Esta rotina ï¿½ utilizada fundamentalmente por requisiï¿½ï¿½es
	 * Ajax, que realizam busca pelos parï¿½mentros informados no richModal
	 * montando automaticamente o resultado da consulta para apresentaï¿½ï¿½o.
	 */
	public void consultarUnidadeEnsino() {
		try {
			setListaConsultarUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoFaltandoLista(getUnidadeEnsinoVOs(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultarUnidadeEnsino(new ArrayList<UnidadeEnsinoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerTodasUnidadeEnsino() {
		getUnidadeEnsinoVOs().clear();
		setUnidadeEnsinoVOs(null);
		setUnidadeEnsinoVOs(new ArrayList<UnidadeEnsinoVO>());

		getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().clear();
		getPainelGestorVO().getPainelGestorContaPagarMesAnoVOs().clear();
		getPainelGestorVO().getPainelGestorContaReceberMesAnoVOs().clear();
		getPainelGestorVO().getPainelGestorContaReceberFluxoCaixaMesAnoVOs().clear();
		getPainelGestorVO().getPainelGestorFinanceiroAcademicoMesAnoVOs().clear();
	}

	public void selecionarUnidadeEnsino() throws Exception {
		UnidadeEnsinoVO unidadeEnsinoVO = (UnidadeEnsinoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoCons");
		for (UnidadeEnsinoVO unidadeEnsinoVO1 : getUnidadeEnsinoVOs()) {
			if (unidadeEnsinoVO1.getCodigo().intValue() == unidadeEnsinoVO.getCodigo().intValue()) {
				return;
			}
		}
		getUnidadeEnsinoVOs().add(unidadeEnsinoVO);
		consultarUnidadeEnsino();
		listaSelectItemUnidadeEnsino = null;
	}

	public String getCampoConsultarUnidadeEnsino() {
		return campoConsultarUnidadeEnsino;
	}

	public void setCampoConsultarUnidadeEnsino(String campoConsultarUnidadeEnsino) {
		this.campoConsultarUnidadeEnsino = campoConsultarUnidadeEnsino;
	}

	public List<UnidadeEnsinoVO> getListaConsultarUnidadeEnsino() {
		if (listaConsultarUnidadeEnsino == null) {
			listaConsultarUnidadeEnsino = new ArrayList<UnidadeEnsinoVO>(0);
		}
		return listaConsultarUnidadeEnsino;
	}

	public void setListaConsultarUnidadeEnsino(List<UnidadeEnsinoVO> listaConsultarUnidadeEnsino) {
		this.listaConsultarUnidadeEnsino = listaConsultarUnidadeEnsino;
	}

	public String getValorConsultarUnidadeEnsino() {
		if (valorConsultarUnidadeEnsino == null) {
			valorConsultarUnidadeEnsino = "";
		}
		return valorConsultarUnidadeEnsino;
	}

	public void setValorConsultarUnidadeEnsino(String valorConsultarUnidadeEnsino) {
		this.valorConsultarUnidadeEnsino = valorConsultarUnidadeEnsino;
	}

	/**
	 * Rotina responsï¿½vel por preencher a combo de consulta dos RichModal da
	 * telas.
	 */
	public List<SelectItem> getTipoConsultarComboUnidadeEnsino() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		return itens;
	}

	public Integer getColumn() {
		if (getUnidadeEnsinoVOs().size() > 4) {
			return 4;
		}
		return getUnidadeEnsinoVOs().size();
	}

	public Integer getElement() {
		return getUnidadeEnsinoVOs().size();
	}

	public List<UnidadeEnsinoVO> getUnidadeEnsinoVOs() {
		if (unidadeEnsinoVOs == null) {
			unidadeEnsinoVOs = new ArrayList<UnidadeEnsinoVO>(0);
			if (getUnidadeEnsinoLogado().getCodigo() > 0) {
				getUnidadeEnsinoLogado().setFiltrarUnidadeEnsino(true);
				unidadeEnsinoVOs.add(getUnidadeEnsinoLogadoClone());
			} else {
				consultarUnidadeEnsino();
				unidadeEnsinoVOs.addAll(getListaConsultarUnidadeEnsino());
				getListaConsultarUnidadeEnsino().clear();
			}
		}
		return unidadeEnsinoVOs;
	}

	public void setUnidadeEnsinoVOs(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
		this.unidadeEnsinoVOs = unidadeEnsinoVOs;
	}

	public void inicializarDadosDataInicioDataFimPorMesAno(PainelGestorContaReceberMesAnoVO obj) throws Exception {
		int mes = Integer.parseInt(MesAnoEnum.getEnum(painelGestorContaReceberMesAnoVO.getMesAno().substring(0, painelGestorContaReceberMesAnoVO.getMesAno().indexOf("/"))).getKey());
		int ano = Integer.parseInt(painelGestorContaReceberMesAnoVO.getMesAno().substring(painelGestorContaReceberMesAnoVO.getMesAno().length() - 4, painelGestorContaReceberMesAnoVO.getMesAno().length()));
		Date dataAnoSemestre = Uteis.getDataVencimentoDiaPorMesAno(1, mes, ano, 0);
		setDataInicioTemporaria(getDataInicio());
		setDataFinalTemporaria(getDataFinal());
		setDataInicio(Uteis.getDataPrimeiroDiaMes(dataAnoSemestre));
		setDataFinal(Uteis.getDataUltimoDiaMes(dataAnoSemestre));
	}

	public void realizarVisualizacaoContaReceberIndividual() {
		ContaReceberPainelGestorVO obj = (ContaReceberPainelGestorVO) context().getExternalContext().getRequestMap().get("contaReceberPainelGestorItens");
		context().getExternalContext().getSessionMap().put("ContaReceberPainelGestor", obj.getContaReceberVO());
	}

	public String selecionarPlanoOrcamentario() {
		PlanoOrcamentarioVO obj = (PlanoOrcamentarioVO) context().getExternalContext().getRequestMap().get("planoOrcamentarioItens");
		
		try {			
			removerControleMemoriaFlashTela("PlanoOrcamentarioControle");
			getSessionAplicacao().setAttribute("planoOrcamentarioItens", obj);
			return Uteis.getCaminhoRedirecionamentoNavegacao("planoOrcamentarioForm.xhtml");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String executarDetalhamentoDepartamentoPorMes() {
		DetalhamentoPlanoOrcamentarioVO obj = (DetalhamentoPlanoOrcamentarioVO) context().getExternalContext().getRequestMap().get("detalhamentoPlanoOrcamentarioItens");
		setDetalhamentoPlanoOrcamentarioVO(obj);
		getFacadeFactory().getPainelGestorFacade().executarMontagemGraficoDetalhamentoPorMes(getDetalhamentoPlanoOrcamentarioVO());
		return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorPlanoOrcamentarioPorMes.xhtml");
	}

	public Boolean getExecutarPool() {
		setDesabilitarPool(false);
		return getDesabilitarPool();
	}

	public void paint(OutputStream out, Object data) throws Exception {

		try {
			getFacadeFactory().getArquivoHelper().renderizarImagemNaTela(out, getPessoa().getArquivoImagem(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
		}
	}

	public void upload(FileUploadEvent uploadEvent) {

		try {
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getPessoa().getArquivoImagem(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.IMAGEM_TMP, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			uploadEvent = null;
		}
	}

	public void inicializarComboBox() {
		if (getListaSelectItemCargo().isEmpty()) {
			montarListaSelectItemCargo();
		}
		if (getListaSelectItemDepartamento().isEmpty()) {
			montarListaSelectItemDepartamento();
		}
	}

	public void paintTemp(OutputStream out, Object data) throws Exception {

		try {
			getFacadeFactory().getArquivoHelper().renderizarImagemNaTela(out, getPessoa().getArquivoImagem(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
		}
	}

	public void confirmarFoto() {
		try {
			getFacadeFactory().getArquivoFacade().alterar(getPessoa().getArquivoImagem(), false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void novoFraseInspiracao() {
		setFraseInspiracaoVO(new FraseInspiracaoVO());
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
		// return "editar";
	}

	public void gravarFraseInspiracao() {
		try {
			getFraseInspiracaoVO().setResponsavelCadastro(getUsuarioLogadoClone());
			getFacadeFactory().getFraseInspiracaoFacade().incluir(fraseInspiracaoVO, false, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
			// return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			// return "editar";
		}
	}

	public void executarCompartilhamentoFraseInspiracao() {
		try {
			// getFraseInspiracaoVO().setResponsavelCadastro(getUsuarioLogadoClone());
			// getFacadeFactory().getFraseInspiracaoFacade().incluir(fraseInspiracaoVO);
			getFacadeFactory().getComunicacaoInternaFacade().executarCompartilhamentoFraseInspiracao(getFraseInspiracaoVO().getFrase(), getFraseInspiracaoVO().getAutor(), getTodos(), getCargo(), getCodigoCargo(), getDepartamento(), getCodigoDepartamento(), getFuncionarios(), getProfessores(), getAlunos(), getEnviarEmail(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			setMensagemID("msg_CompartilharFraseInspiracao_compartilharFrase");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsï¿½vel por disponibilizar um novo objeto da classe
	 * <code>Cargo</code> para ediï¿½ï¿½o pelo usuï¿½rio da aplicaï¿½ï¿½o.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestor.xhtml");
	}

	/**
	 * Rotina responsï¿½vel por disponibilizar os dados de um objeto da classe
	 * <code>Cargo</code> para alteraï¿½ï¿½o. O objeto desta classe ï¿½
	 * disponibilizado na session da pï¿½gina (request) para que o JSP
	 * correspondente possa disponibilizï¿½-lo para ediï¿½ï¿½o.
	 */
	public String editar() {
		// CargoVO obj = (CargoVO)
		// context().getExternalContext().getRequestMap().get("cargo");
		// obj.setNovoObj(Boolean.FALSE);
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestor.xhtml");
	}

	/**
	 * Rotina responsï¿½vel por gravar no BD os dados editados de um novo objeto
	 * da classe <code>Cargo</code>. Caso o objeto seja novo (ainda nï¿½o
	 * gravado no BD) ï¿½ acionado a operaï¿½ï¿½o <code>incluir()</code>. Caso
	 * contrï¿½rio ï¿½ acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistï¿½ncia o objeto nï¿½o ï¿½ gravado, sendo re-apresentado para o
	 * usuï¿½rio juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestor.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestor.xhtml");
		}
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		pessoa = null;
	}

	public PessoaVO getPessoa() {
		if (pessoa == null) {
			return new PessoaVO();
		}
		return pessoa;
	}

	public void setPessoa(PessoaVO pessoa) {
		this.pessoa = pessoa;
	}

	/**
	 * @return the fraseInspiracaoVO
	 */
	public FraseInspiracaoVO getFraseInspiracaoVO() {
		if (fraseInspiracaoVO == null) {
			fraseInspiracaoVO = new FraseInspiracaoVO();
		}
		return fraseInspiracaoVO;
	}

	/**
	 * @param fraseInspiracaoVO
	 *            the fraseInspiracaoVO to set
	 */
	public void setFraseInspiracaoVO(FraseInspiracaoVO fraseInspiracaoVO) {
		this.fraseInspiracaoVO = fraseInspiracaoVO;
	}

	public Boolean getTodos() {
		if (todos == null) {
			todos = Boolean.FALSE;
		}
		return todos;
	}

	public void setTodos(Boolean todos) {
		this.todos = todos;
	}

	public Boolean getCargo() {
		if (cargo == null) {
			cargo = Boolean.FALSE;
		}
		return cargo;
	}

	public void setCargo(Boolean cargo) {
		this.cargo = cargo;
	}

	public Boolean getDepartamento() {
		if (departamento == null) {
			departamento = Boolean.FALSE;
		}
		return departamento;
	}

	public void setDepartamento(Boolean departamento) {
		this.departamento = departamento;
	}

	public Boolean getFuncionarios() {
		if (funcionarios == null) {
			funcionarios = Boolean.FALSE;
		}
		return funcionarios;
	}

	public void setFuncionarios(Boolean funcionarios) {
		this.funcionarios = funcionarios;
	}

	public Boolean getProfessores() {
		if (professores == null) {
			professores = Boolean.FALSE;
		}
		return professores;
	}

	public void setProfessores(Boolean professores) {
		this.professores = professores;
	}

	public Boolean getAlunos() {
		if (alunos == null) {
			alunos = Boolean.FALSE;
		}
		return alunos;
	}

	public void setAlunos(Boolean alunos) {
		this.alunos = alunos;
	}

	public Boolean getEnviarEmail() {
		if (enviarEmail == null) {
			enviarEmail = false;
		}
		return enviarEmail;
	}

	public void setEnviarEmail(Boolean enviarEmail) {
		this.enviarEmail = enviarEmail;
	}

	/**
	 * Mï¿½todo responsï¿½vel por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Departamento</code>.
	 */
	public void montarListaSelectItemDepartamento(String prm) throws Exception {
		List<DepartamentoVO> resultadoConsulta = null;
		Iterator<DepartamentoVO> i = null;
		try {
			resultadoConsulta = consultarDepartamentoPorNome(prm);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				DepartamentoVO obj = (DepartamentoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			setListaSelectItemDepartamento(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por atualizar o ComboBox relativo ao atributo
	 * <code>Departamento</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>Departamento</code>. Esta rotina nï¿½o recebe parï¿½metros
	 * para filtragem de dados, isto ï¿½ importante para a inicializaï¿½ï¿½o dos
	 * dados da tela para o acionamento por meio requisiï¿½ï¿½es Ajax.
	 */
	public void montarListaSelectItemDepartamento() {
		try {
			montarListaSelectItemDepartamento("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
		}

	}

	/**
	 * Mï¿½todo responsï¿½vel por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo ï¿½ uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List<DepartamentoVO> consultarDepartamentoPorNome(String nomePrm) throws Exception {
		List<DepartamentoVO> lista = getFacadeFactory().getDepartamentoFacade().consultarPorNomeFaleConosco(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		return lista;
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

	public List<SelectItem> getListaSelectItemDepartamento() {
		if (listaSelectItemDepartamento == null) {
			listaSelectItemDepartamento = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDepartamento;
	}

	public void setListaSelectItemDepartamento(List<SelectItem> listaSelectItemDepartamento) {
		this.listaSelectItemDepartamento = listaSelectItemDepartamento;
	}

	/**
	 * Mï¿½todo responsï¿½vel por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Cargo</code>.
	 */
	public void montarListaSelectItemCargo(String prm) throws Exception {
		List<CargoVO> resultadoConsulta = null;
		Iterator<CargoVO> i = null;
		try {
			resultadoConsulta = consultarCargoPorNome(prm);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				CargoVO obj = (CargoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			setListaSelectItemCargo(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por atualizar o ComboBox relativo ao atributo
	 * <code>Cargo</code>. Buscando todos os objetos correspondentes a entidade
	 * <code>Cargo</code>. Esta rotina nï¿½o recebe parï¿½metros para filtragem
	 * de dados, isto ï¿½ importante para a inicializaï¿½ï¿½o dos dados da tela
	 * para o acionamento por meio requisiï¿½ï¿½es Ajax.
	 */
	public void montarListaSelectItemCargo() {
		try {
			montarListaSelectItemCargo("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
		}

	}

	/**
	 * Mï¿½todo responsï¿½vel por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo ï¿½ uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List<CargoVO> consultarCargoPorNome(String nomePrm) throws Exception {
		List<CargoVO> lista = getFacadeFactory().getCargoFacade().consultaRapidaPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	public String realizarSelecaolObjPainelGestorFinanceiroAcademicoMesAnoVO() {
		try {
			PainelGestorFinanceiroAcademicoMesAnoVO painelGestorFinanceiroAcademicoMesAnoVO = (PainelGestorFinanceiroAcademicoMesAnoVO) UteisJSF.context().getExternalContext().getRequestMap().get("financeiroAcademicoMesAnoItens");
			PainelGestorFinanceiroAcademicoNivelEducacionalVO nivelEducacional = (PainelGestorFinanceiroAcademicoNivelEducacionalVO) UteisJSF.context().getExternalContext().getRequestMap().get("nivelItens");
			setMesAno(painelGestorFinanceiroAcademicoMesAnoVO.getMesAno());
			inicializarDataMesAno();
			setTipoNivelEducacional(nivelEducacional.getNivel());
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosMapaPainelGestorAcademicoXFinanceiroPorNivelEducacional(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getTipoNivelEducacional_Enum(), getMesAno(), getConfiguracaoFinanceiroPadraoSistema());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorAcademicoFinanceiroPorNivelEducacionalForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getPainelGestorContaReceberMesAnoVOs().clear();
			return "";
		}
	}

	public String realizarGeracaoRelatorioObjPainelGestorFinanceiroAcademicoMesAnoVO() {
		try {
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosMapaPainelGestorAcademicoXFinanceiroPorNivelEducacional(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getTipoNivelEducacional_Enum(), "", getConfiguracaoFinanceiroPadraoSistema());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getPainelGestorContaReceberMesAnoVOs().clear();
			return "";
		}
	}

	public String realizarNavegacaoParaPainelGestorAcademicoFinanceiro() {
		return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorAcademicoFinanceiroForm.xhtml");
	}

	public String realizarNavegacaoParaPainelGestorAcademicoFinanceiroPorNivelEducacional() {
		try {
			// getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().setDataInicioPorNivelEducacional(getDataInicio());
			// getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().setDataFinalPorNivelEducacional(getDataFinal());
			// getUnidadeEnsinoVOPorNivel().clear();
			// getUnidadeEnsinoVOPorNivel().addAll(getUnidadeEnsinoVOs());
			// getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosMapaPainelGestorAcadï¿½micoXFinanceiroPorNivelEducacional(getPainelGestorVO(),
			// getUnidadeEnsinoVOPorNivel(),
			// getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getDataInicioPorNivelEducacional(),
			// getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getDataFinalPorNivelEducacional(),
			// getTipoNivelEducacional_Enum(), "",
			// getConfiguracaoFinanceiroPadraoSistema());
			getPainelGestorVO().setCodigoCurso(0);
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {

		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorAcademicoFinanceiroPorNivelEducacionalForm.xhtml");
	}

	public String realizarSelecaolObjPainelGestorFinanceiroAcademicoMesAnoVOPorTurma() {
		try {
			// PainelGestorFinanceiroAcademicoMesAnoVO
			// painelGestorFinanceiroAcademicoMesAnoVO =
			// (PainelGestorFinanceiroAcademicoMesAnoVO)
			// UteisJSF.context().getExternalContext().getRequestMap().get("financeiroAcademicoMesAno");
			PainelGestorFinanceiroAcademicoNivelEducacionalVO nivelEducacional = (PainelGestorFinanceiroAcademicoNivelEducacionalVO) UteisJSF.context().getExternalContext().getRequestMap().get("nivelItens");
			// getPainelGestorVO().setDataInicioPorTurma(getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getDataInicioPorNivelEducacional());
			// getPainelGestorVO().setDataFinalPorTurma(getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getDataFinalPorNivelEducacional());
			getCursoVO().setCodigo(nivelEducacional.getCodigoCurso());
			getCursoVO().setNome(nivelEducacional.getCurso());
			// setMesAno(painelGestorFinanceiroAcademicoMesAnoVO.getMesAno());
			// setTipoNivelEducacional(nivelEducacional.getNivel());
			getPainelGestorVO().setCodigoCurso(nivelEducacional.getCodigoCurso());

			// getUnidadeEnsinoVOPorNivel().clear();
			// getUnidadeEnsinoVOPorNivel().addAll(getUnidadeEnsinoVOs());
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosMapaPainelGestorAcademicoXFinanceiroPorTurma(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getTipoNivelEducacional_Enum(), getPainelGestorVO().getCodigoCurso(), "");
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorAcademicoFinanceiroPorTurmaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getPainelGestorContaReceberMesAnoVOs().clear();
			return "";
		}
	}

	public String realizarGeracaoRelatorioObjPainelGestorFinanceiroAcademicoMesAnoVOPorTurma() {
		try {
			// getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().setDataInicioPorNivelEducacional(getDataInicio());
			// getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().setDataFinalPorNivelEducacional(getDataFinal());
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosMapaPainelGestorAcademicoXFinanceiroPorTurma(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getTipoNivelEducacional_Enum(), getPainelGestorVO().getCodigoCurso(), "");
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getPainelGestorContaReceberMesAnoVOs().clear();
			return "";
		}
	}

	public String selecionalObjPainelGestorMonitoramentoDescontoTurma() {
		try {
			PainelGestorMonitoramentoDescontoNivelEducacionalVO painelGestorMonitoramentoDescontoNivelEducacionalVO = (PainelGestorMonitoramentoDescontoNivelEducacionalVO) UteisJSF.context().getExternalContext().getRequestMap().get("nivelItens");
			// setMesAno(painelGestorMonitoramentoDescontoNivelEducacionalVO.getMesAno());
			// inicializarDataMesAno();
			// getPainelGestorVO().setDataInicioPorNivelEducacional(getDataInicio());
			// getPainelGestorVO().setDataFinalPorNivelEducacional(getDataFinal());
			setTipoNivelEducacional(painelGestorMonitoramentoDescontoNivelEducacionalVO.getNivel());
			getCursoVO().setCodigo(painelGestorMonitoramentoDescontoNivelEducacionalVO.getCodigoCurso());
			getCursoVO().setNome(painelGestorMonitoramentoDescontoNivelEducacionalVO.getCurso());

			return executarGeracaoDadosPainelGestorMonitoramentoDescontoTurma();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String realizarGeracaoRelatorioObjPainelGestorMonitoramentoDescontoTurma() {
		try {
			setDataFinalMesAno(getDataInicio());
			setDataFinalMesAno(getDataFinal());
			inicializarDadosListaNivelEducacional();
			return executarGeracaoDadosPainelGestorMonitoramentoDescontoTurma();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getPainelGestorContaReceberMesAnoVOs().clear();
			return "";
		}
	}

	public String executarGeracaoDadosPainelGestorMonitoramentoDescontoTurma() {
		try {
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosPainelGestorMonitoramentoDescontoPorTurma(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getTipoNivelEducacional_Enum(), getMesAno(), getCursoVO().getCodigo());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoDescontoPorTurma.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String realizarNavegacaoParaPainelGestorMonitoramentoDescontoPorNivelEducacional() {
		try {
			if (getTipoNivelEducacional() == null || getTipoNivelEducacional().isEmpty()) {
				setTipoNivelEducacional(getPainelGestorVO().getTipoNivelEducacional().getValor());
			}
			setDataFinalMesAno(getDataInicio());
			setDataFinalMesAno(getDataFinal());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			return executarGeracaoDadosPainelGestorMonitoramentoDescontoPorNivelEducacional();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}

	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * TurmaCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultarTurma() {
		try {
			super.consultar();
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoVOs(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}

			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultarRapidaPorNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoVOs(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}

	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurmaVO(obj);
			getListaConsultaTurma().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma2() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurmaVO(obj);
			setCursoVO(obj.getCurso());
			getListaConsultaTurma().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public Integer getCodigoCargo() {
		if (codigoCargo == null) {
			codigoCargo = 0;
		}
		return codigoCargo;
	}

	public void setCodigoCargo(Integer codigoCargo) {
		this.codigoCargo = codigoCargo;
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
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public String getValorConsultaTurma() {
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public String getCampoConsultaTurma() {
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public Integer getCodigoDepartamento() {
		if (codigoDepartamento == null) {
			codigoDepartamento = 0;
		}
		return codigoDepartamento;
	}

	public void setCodigoDepartamento(Integer codigoDepartamento) {
		this.codigoDepartamento = codigoDepartamento;
	}

	/**
	 * @return the desabilitarPool
	 */
	public Boolean getDesabilitarPool() {
		if (desabilitarPool == null) {
			desabilitarPool = Boolean.TRUE;
		}
		return desabilitarPool;
	}

	/**
	 * @param desabilitarPool
	 *            the desabilitarPool to set
	 */
	public void setDesabilitarPool(Boolean desabilitarPool) {
		this.desabilitarPool = desabilitarPool;
	}

	/**
	 * @return the listaConsultarPlanoOrcamentario
	 */
	public List<PlanoOrcamentarioVO> getListaConsultarPlanoOrcamentario() {
		if (listaConsultarPlanoOrcamentario == null) {
			listaConsultarPlanoOrcamentario = new ArrayList<PlanoOrcamentarioVO>(0);
		}
		return listaConsultarPlanoOrcamentario;
	}

	/**
	 * @param listaConsultarPlanoOrcamentario
	 *            the listaConsultarPlanoOrcamentario to set
	 */
	public void setListaConsultarPlanoOrcamentario(List<PlanoOrcamentarioVO> listaConsultarPlanoOrcamentario) {
		this.listaConsultarPlanoOrcamentario = listaConsultarPlanoOrcamentario;
	}

	public PlanoOrcamentarioVO getPlanoOrcamentarioVO() {
		if (planoOrcamentarioVO == null) {
			planoOrcamentarioVO = new PlanoOrcamentarioVO();
		}
		return planoOrcamentarioVO;
	}

	public void setPlanoOrcamentarioVO(PlanoOrcamentarioVO planoOrcamentarioVO) {
		this.planoOrcamentarioVO = planoOrcamentarioVO;
	}

	public DetalhamentoPlanoOrcamentarioVO getDetalhamentoPlanoOrcamentarioVO() {
		if (detalhamentoPlanoOrcamentarioVO == null) {
			detalhamentoPlanoOrcamentarioVO = new DetalhamentoPlanoOrcamentarioVO();
		}
		return detalhamentoPlanoOrcamentarioVO;
	}

	public void setDetalhamentoPlanoOrcamentarioVO(DetalhamentoPlanoOrcamentarioVO detalhamentoPlanoOrcamentarioVO) {
		this.detalhamentoPlanoOrcamentarioVO = detalhamentoPlanoOrcamentarioVO;
	}

	public Boolean getFiltrarUnidadeEnsino() {
		if (filtrarUnidadeEnsino == null) {
			filtrarUnidadeEnsino = false;
		}
		return filtrarUnidadeEnsino;
	}

	public void setFiltrarUnidadeEnsino(Boolean filtrarUnidadeEnsino) {
		this.filtrarUnidadeEnsino = filtrarUnidadeEnsino;
	}

	public FiltroPainelGestorAcademicoVO getFiltroPainelGestorAcademico() {
		if (filtroPainelGestorAcademico == null) {
			filtroPainelGestorAcademico = getFacadeFactory().getFiltroPainelGestorAcademicoFacade().consultarFiltroPorUsuario(getUsuarioLogadoClone());
		}
		return filtroPainelGestorAcademico;
	}

	public void setFiltroPainelGestorAcademico(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico) {
		this.filtroPainelGestorAcademico = filtroPainelGestorAcademico;
	}

	public String consultarDadosMonitoramentoAcademico() {
		try {
			setMostarDetalheAcademico(false);
			setFiltrarAcademico(false);
			setFiltrarUnidadeEnsino(false);
			getFiltroPainelGestorAcademico().setUsuario(getUsuarioLogadoClone());			
			inicializarDadosListaNivelEducacional2();
			if (!getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO) && !getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAGINA_INICIAL) && !getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO_ACADEMICO)) {
				if (getMesAno().trim().isEmpty()) {
					setDataInicio(getDataInicioMesAno());
					setDataFinal(getDataFinalMesAno());
				}
				setTelaOrigem(ControlePaginaPainelGestorEnum.PAGINA_INICIAL);
			}			
			inicializarDashboardMonitorarAcademico();
			getFacadeFactory().getFiltroPainelGestorAcademicoFacade().persistir(getFiltroPainelGestorAcademico());			
			getFacadeFactory().getFiltroPainelGestorAcademicoFacade().consultarDadosMonitoramentoAcademico(getFiltroPainelGestorAcademico(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), getFiltroPainelGestorAcademico().getPeriodicidadeCurso(), getAno(), getSemestre());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoAcademicoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}
	
	public void inicializarDashboardMonitorarAcademico() {
		if(!getMapDashboards().containsKey(TipoDashboardEnum.BI_SITUACOES_ACADEMICAS.name())){
			getMapDashboards().put(TipoDashboardEnum.BI_SITUACOES_ACADEMICAS.name(), new DashboardVO(TipoDashboardEnum.BI_SITUACOES_ACADEMICAS, false, 1 , TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.SEI_DECIDIR, ModeloApresentacaoDashboardEnum.GRAFICO_PIZZA, getUsuarioLogado()));
		}
		if(!getMapDashboards().containsKey(TipoDashboardEnum.BI_EVOLUCOES_ACADEMICAS.name())){
			getMapDashboards().put(TipoDashboardEnum.BI_EVOLUCOES_ACADEMICAS.name(), new DashboardVO(TipoDashboardEnum.BI_EVOLUCOES_ACADEMICAS, false, 2 , TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.SEI_DECIDIR, ModeloApresentacaoDashboardEnum.GRAFICO_COLUNA, getUsuarioLogado()));
		}
		if(!getMapDashboards().containsKey(TipoDashboardEnum.BI_EVASOES_ACADEMICAS.name())){
			getMapDashboards().put(TipoDashboardEnum.BI_EVASOES_ACADEMICAS.name(), new DashboardVO(TipoDashboardEnum.BI_EVASOES_ACADEMICAS, false, 3 , TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.SEI_DECIDIR, ModeloApresentacaoDashboardEnum.GRAFICO_PIZZA, getUsuarioLogado()));
		}
		if(!getMapDashboards().containsKey(TipoDashboardEnum.BI_MONITOR_RENOVACOES.name())){
			getMapDashboards().put(TipoDashboardEnum.BI_MONITOR_RENOVACOES.name(), new DashboardVO(TipoDashboardEnum.BI_MONITOR_RENOVACOES, false, 4 , TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.SEI_DECIDIR, ModeloApresentacaoDashboardEnum.GRAFICO_PIZZA, getUsuarioLogado()));
		}
		if(!getMapDashboards().containsKey(TipoDashboardEnum.BI_MONITOR_ACADEMICO.name())){
			getMapDashboards().put(TipoDashboardEnum.BI_MONITOR_ACADEMICO.name(), new DashboardVO(TipoDashboardEnum.BI_MONITOR_ACADEMICO, false, 5 , TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.SEI_DECIDIR, ModeloApresentacaoDashboardEnum.GRAFICO_PIZZA, getUsuarioLogado()));
		}
		if(!getMapDashboards().containsKey(TipoDashboardEnum.BI_PROCESSO_SELETIVO.name())){
			getMapDashboards().put(TipoDashboardEnum.BI_PROCESSO_SELETIVO.name(), new DashboardVO(TipoDashboardEnum.BI_PROCESSO_SELETIVO, false, 6 , TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.SEI_DECIDIR, ModeloApresentacaoDashboardEnum.GRAFICO_BARRA, getUsuarioLogado()));
		}
		if(!getMapDashboards().containsKey(TipoDashboardEnum.BI_ALUNOS_POR_TURNO.name())){
			getMapDashboards().put(TipoDashboardEnum.BI_ALUNOS_POR_TURNO.name(), new DashboardVO(TipoDashboardEnum.BI_ALUNOS_POR_TURNO, false, 7 , TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.SEI_DECIDIR, ModeloApresentacaoDashboardEnum.GRAFICO_PIZZA, getUsuarioLogado()));
		}
		if(!getMapDashboards().containsKey(TipoDashboardEnum.BI_ALUNO_POR_SEXO.name())){
			getMapDashboards().put(TipoDashboardEnum.BI_ALUNO_POR_SEXO.name(), new DashboardVO(TipoDashboardEnum.BI_ALUNO_POR_SEXO, false, 8 , TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.SEI_DECIDIR, ModeloApresentacaoDashboardEnum.GRAFICO_PIZZA, getUsuarioLogado()));
		}
		if(!getMapDashboards().containsKey(TipoDashboardEnum.BI_ALUNO_POR_TURMA.name())){
			getMapDashboards().put(TipoDashboardEnum.BI_ALUNO_POR_TURMA.name(), new DashboardVO(TipoDashboardEnum.BI_ALUNO_POR_TURMA, false, 9 , TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.SEI_DECIDIR, ModeloApresentacaoDashboardEnum.GRAFICO_PIZZA, getUsuarioLogado()));
		}
		if(!getMapDashboards().containsKey(TipoDashboardEnum.BI_IMPRESSAO_DECLARACOES.name())){
			getMapDashboards().put(TipoDashboardEnum.BI_IMPRESSAO_DECLARACOES.name(), new DashboardVO(TipoDashboardEnum.BI_IMPRESSAO_DECLARACOES, false, 10 , TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.SEI_DECIDIR, ModeloApresentacaoDashboardEnum.GRAFICO_PIZZA, getUsuarioLogado()));
		}
		if(!getMapDashboards().containsKey(TipoDashboardEnum.BI_TURMAS_POR_TURNO.name())){
			getMapDashboards().put(TipoDashboardEnum.BI_TURMAS_POR_TURNO.name(), new DashboardVO(TipoDashboardEnum.BI_TURMAS_POR_TURNO, false, 11 , TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.SEI_DECIDIR, ModeloApresentacaoDashboardEnum.GRAFICO_PIZZA, getUsuarioLogado()));
		}
		if(!getMapDashboards().containsKey(TipoDashboardEnum.BI_PROFESSORES_POR_TURNO.name())){
			getMapDashboards().put(TipoDashboardEnum.BI_PROFESSORES_POR_TURNO.name(), new DashboardVO(TipoDashboardEnum.BI_PROFESSORES_POR_TURNO, false, 12 , TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.SEI_DECIDIR, ModeloApresentacaoDashboardEnum.GRAFICO_PIZZA, getUsuarioLogado()));
		}
	}

	public String consultarDadosMonitoramentoConsultorPorMatricula() {
		try {
			setMostarDetalheAcademico(false);
			setFiltrarAcademico(false);
			setFiltrarUnidadeEnsino(false);
			setHtmlRelatorioConsultorPorMatricula("");
			// getFiltroPainelGestorAcademico().setUsuario(getUsuarioLogadoClone());
			// if
			// (!getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO)
			// &&
			// !getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAGINA_INICIAL)
			// &&
			// !getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO_ACADEMICO))
			// {
			// if (getMesAno().trim().isEmpty()) {
			// setDataInicio(getDataInicioMesAno());
			// setDataFinal(getDataFinalMesAno());
			// }
			// setTelaOrigem(ControlePaginaPainelGestorEnum.PAGINA_INICIAL);
			// }
			//
			// getFacadeFactory().getFiltroPainelGestorAcademicoFacade().persistir(getFiltroPainelGestorAcademico());
			// getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().consultarDadosPainelGestorMonitoramentoConsultorPorMatricula(getUnidadeEnsinoVOs(),
			// getDataInicio(), getDataFinal(), getCursoVO(), getTurmaVO(),
			// getConsultor(), getSituacaoMatricula(),
			// getSituacaoProspectAtIn());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoConsultorPorMatricula.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String consultarDadosMonitoramentoConsultorPorMatriculaHashMap() {
		try {
			setMostarDetalheAcademico(false);
			setFiltrarAcademico(false);
			setFiltrarUnidadeEnsino(false);
			setCursoVO(new CursoVO());
			setTurmaVO(new TurmaVO());
			setConsultor(new FuncionarioVO());
			setHtmlRelatorioConsultorPorMatricula("");
			// getFiltroPainelGestorAcademico().setUsuario(getUsuarioLogadoClone());
			// if
			// (!getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO)
			// &&
			// !getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAGINA_INICIAL)
			// &&
			// !getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO_ACADEMICO))
			// {
			// if (getMesAno().trim().isEmpty()) {
			// setDataInicio(getDataInicioMesAno());
			// setDataFinal(getDataFinalMesAno());
			// }
			// setTelaOrigem(ControlePaginaPainelGestorEnum.PAGINA_INICIAL);
			// }
			//
			// getFacadeFactory().getFiltroPainelGestorAcademicoFacade().persistir(getFiltroPainelGestorAcademico());
			// setHtmlRelatorioConsultorPorMatricula(getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().consultarDadosPainelGestorMonitoramentoConsultorPorMatricula(getUnidadeEnsinoLogado(),
			// getDataInicio(), getDataFinal(), getCursoVO(), getTurmaVO(),
			// getConsultor(), getSituacaoMatricula(),
			// getSituacaoProspectAtIn()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoConsultorPorMatricula.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}	
	
	public void selecionarLegendaGraficoMonitoramentoAcademico(String opcao, Integer qtde, Integer codigo) {
		try {
			getControleConsultaOtimizado().setPage(0);
			getControleConsultaOtimizado().setPaginaAtual(1);
			getControleConsultaOtimizado().setLimitePorPagina(50);
			if (opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PRE_MATRICULA.name())) {
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getFiltroPainelGestorAcademicoFacade().consultarQuantidadeMonitoramentoAcademicoPreMatriculado(getFiltroPainelGestorAcademico(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), getAno(), getSemestre()));
			} else {
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(qtde);
			}
			setOpcaoSituacaoAcademica(opcao);
			getFacadeFactory().getFiltroPainelGestorAcademicoFacade().consultarDadosDetalheMonitoramentoAcademico(getFiltroPainelGestorAcademico(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), getAno(), getSemestre(), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.valueOf(opcao), codigo, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset());
			getControleConsultaOtimizado().setListaConsulta(getFiltroPainelGestorAcademico().getPainelGestorDetalheMonitoramentoAcademicoVOs());
			setMostarDetalheAcademico(true);
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
	}

	// Campos utilizados para seleï¿½ï¿½o generica
	public String opcao;
	private Integer qtde;
	private Integer codigo;

	public void detalharMonitoramentoProcessoSeletivo() {
		getControleConsultaOtimizado().setOffset(0);
		selecionarLegendaGraficoMonitoramentoAcademico(getOpcao(), getQtde(), getCodigo());
	}

	public String getOpcao() {
		if (opcao == null) {
			opcao = "";
		}
		return opcao;
	}

	public void setOpcao(String opcao) {
		this.opcao = opcao;
	}

	public Integer getQtde() {
		if (qtde == null) {
			qtde = 0;
		}
		return qtde;
	}

	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String consultarDadosMonitoramentoProcessoSeletivo() {
		try {
			getFacadeFactory().getFiltroPainelGestorAcademicoFacade().consultarMonitoramentoProcessoSeletivo(getFiltroPainelGestorAcademico(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), getAno(), getSemestre(), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.INICIAL);
			getFacadeFactory().getFiltroPainelGestorAcademicoFacade().consultarMonitoramentoProcessoSeletivo(getFiltroPainelGestorAcademico(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), getAno(), getSemestre(), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PS_INSCRITO_POR_CURSO);
			getFiltroPainelGestorAcademico().setOpcaoAtual(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.PS_INSCRITO_POR_CURSO);
			setMostarDetalheAcademico(false);
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoProcessoSeletivoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String voltarMonitoramentoAcademico() {
		try {
			consultarDadosMonitoramentoAcademico();
			getFiltroPainelGestorAcademico().setOpcaoAtual(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.INICIAL);
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoAcademicoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public void imprimirExcel() {
		String design = null;

		try {
			design = ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "MonitoramentoAcademicoAlunoCanceladoRel" + ".jrxml");

			if (!getControleConsultaOtimizado().getListaConsulta().isEmpty()) {
				getSuperParametroRelVO().limparParametros();
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setSubReport_Dir(("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator));
				getSuperParametroRelVO().setTituloRelatorio(getFiltroPainelGestorAcademico().getTituloApresentar());
				getFacadeFactory().getFiltroPainelGestorAcademicoFacade().consultarDadosDetalheMonitoramentoAcademico(getFiltroPainelGestorAcademico(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), getAno(), getSemestre(), OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.valueOf(getOpcaoSituacaoAcademica()), getFiltroPainelGestorAcademico().getCodigoBase(), 0, getControleConsultaOtimizado().getOffset());
				getSuperParametroRelVO().setListaObjetos(getControleConsultaOtimizado().getListaConsulta());
				if (getControleConsultaOtimizado().getListaConsulta().isEmpty()) {
					throw new Exception("NÃ£o hÃ¡ dados para serem impressos!");
				}
				getSuperParametroRelVO().setCaminhoBaseRelatorio(("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator));
				realizarImpressaoRelatorio();

				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			design = null;
		}
	}

	private PainelGestorMonitoramentoAcademicoVO painelGestorMonitoramentoAcademico;

	public PainelGestorMonitoramentoAcademicoVO getPainelGestorMonitoramentoAcademico() {
		return painelGestorMonitoramentoAcademico;
	}

	public void setPainelGestorMonitoramentoAcademico(PainelGestorMonitoramentoAcademicoVO painelGestorMonitoramentoAcademico) {
		this.painelGestorMonitoramentoAcademico = painelGestorMonitoramentoAcademico;
	}

	public void selecionarLegendaGraficoMonitoramentoAcademico() {
		if (painelGestorMonitoramentoAcademico != null) {
			selecionarLegendaGraficoMonitoramentoAcademico(getPainelGestorMonitoramentoAcademico().getOpcao().toString(), getPainelGestorMonitoramentoAcademico().getQuantidade(), getPainelGestorMonitoramentoAcademico().getCodigo());
		}
	}

	public void scrollerListenerDetalheMonitoramentoAcademico(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());		
		getFacadeFactory().getFiltroPainelGestorAcademicoFacade().consultarDadosDetalheMonitoramentoAcademico(getFiltroPainelGestorAcademico(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), getAno(), getSemestre(), getFiltroPainelGestorAcademico().getOpcaoAtual(), getFiltroPainelGestorAcademico().getCodigoBase(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset());
		getControleConsultaOtimizado().setListaConsulta(getFiltroPainelGestorAcademico().getPainelGestorDetalheMonitoramentoAcademicoVOs());		
	}

	public void voltarPainelGestorMonitoramentoAcademico() {
		setMostarDetalheAcademico(false);
	}

	public Boolean getFiltrarAcademico() {
		if (filtrarAcademico == null) {
			filtrarAcademico = false;
		}
		return filtrarAcademico;
	}

	public void setFiltrarAcademico(Boolean filtrarAcademico) {
		this.filtrarAcademico = filtrarAcademico;
	}

	public Boolean getMostarDetalheAcademico() {
		if (mostarDetalheAcademico == null) {
			mostarDetalheAcademico = false;
		}
		return mostarDetalheAcademico;
	}

	public void setMostarDetalheAcademico(Boolean mostarDetalheAcademico) {
		this.mostarDetalheAcademico = mostarDetalheAcademico;
	}

	public String getDadosGraficoConsumo() {
		return Uteis.realizarMontagemDadosGraficoPizza(getNivelGraficoDWVO().getLegendaGraficoVOs());
	}

	/**
	 * Controle Monitoramento Painel Gestor CRM
	 * 
	 */
	public String consultarDadosIniciaisPainelGestorMonitoramentoCRM() {
		try {
			setMostarProspectCrm(false);
			getPainelGestorMonitoramentoCRM().getProspectsVOs().clear();
			getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().consultarDadosIniciaisPainelGestorMonitoramentoCRM(getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), getPainelGestorMonitoramentoCRM(), getSituacaoProspect());
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoCRM.xhtml");
	}

	public void realizarGeracaoGraficoMonitoramentoProspectComoFicouSabendoInstituicao() {
		try {
			setMostarProspectCrm(false);
			getPainelGestorMonitoramentoCRM().getProspectsVOs().clear();
			getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().realizarGeracaoGraficoMonitoramentoProspectComoFicouSabendoInstituicao(getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), getPainelGestorMonitoramentoCRM(), getSituacaoProspect());
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String consultarDadosComoFicouSabendoInstituicao() {
		try {
			getControleConsultaOtimizado().setLimitePorPagina(50);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().consultarDadosProspectComoFicouSabendoDaInstituicao(getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), getSituacaoProspect(), getPainelGestorMonitoramentoCRM().getTipoMidiaCaptacao().getCodigo(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().consultarTotalRegistroProspectComoFicouSabendoDaInstituicao(getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), getSituacaoProspect(), getPainelGestorMonitoramentoCRM().getTipoMidiaCaptacao().getCodigo()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			limparMensagem();
		} catch (Exception e) {
			setMostarProspectCrm(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoCrmVisualizarProspect.xhtml");
	}

	/**
	 * Controle Monitoramento Painel Gestor CRM
	 * 
	 */
	public String consultarDadosIniciaisPainelGestorMonitoramentoProspectCRM() {
		try {

			getPainelGestorMonitoramentoCRM().getProspectsVOs().clear();
			getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().consultarDadosIniciaisPainelGestorMonitoramentoProspectCRM(getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), getPainelGestorMonitoramentoCRM());
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoCRM.xhtml");
	}

	/**
	 * Controle Monitoramento Painel Gestor CRM
	 * 
	 */
	public String consultarDadosDetalhePainelGestorMonitoramentoProspectCRM() {
		try {

			if (getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_UNIDADE)) {
				getPainelGestorMonitoramentoCRM().setPainelGestorTipoMonitoramentoCRMEnum(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_UNIDADE);
				getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().consultarMonitoramentoProspectPorUnidade(getDataInicio(), getDataFinal(), getPainelGestorMonitoramentoCRM().getUnidadeEnsino().getCodigo(), getPainelGestorMonitoramentoCRM());
			}
			if (getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CONSULTOR)) {
				getPainelGestorMonitoramentoCRM().setPainelGestorTipoMonitoramentoCRMEnum(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CONSULTOR);
				getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().consultarMonitoramentoProspectPorConsultor(getDataInicio(), getDataFinal(), getPainelGestorMonitoramentoCRM().getUnidadeEnsino().getCodigo(), getPainelGestorMonitoramentoCRM().getConsultor().getCodigo(), getPainelGestorMonitoramentoCRM());
			}
			if (getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CURSO)) {
				getPainelGestorMonitoramentoCRM().setPainelGestorTipoMonitoramentoCRMEnum(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CURSO);
				getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().consultarMonitoramentoProspectPorConsultor(getDataInicio(), getDataFinal(), getPainelGestorMonitoramentoCRM().getUnidadeEnsino().getCodigo(), getPainelGestorMonitoramentoCRM().getConsultor().getCodigo(), getPainelGestorMonitoramentoCRM());
			}
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoCrmDetalheProspect.xhtml");
	}

	public void realizarAtualizacaorDadosDetalhePainelGestorMonitoramentoCRM() {
		try {
			setMostarProspectCrm(false);
			getPainelGestorMonitoramentoCRM().getProspectsVOs().clear();
			getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().realizarAtualizacaorDadosDetalhePainelGestorMonitoramentoCRM(getUnidadeEnsinoVOs(), getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().getDataInicio(), getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().getDataTermino(), getPainelGestorMonitoramentoCRM(), getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO());
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String consultarDadosDetalheCursoPainelGestorMonitoramentoCRM() {
		try {

			setMostarProspectCrm(false);
			getPainelGestorMonitoramentoCRM().getProspectsVOs().clear();
			getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO((PainelGestorMonitoramentoDetalheCRMVO) context().getExternalContext().getRequestMap().get("detalheItens"));
			if (getCodigoDepartamento() == -1) {
				if (getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().getConsultor().getCodigo() == 0) {
					getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().getConsultor().setCodigo(-1);
				}
			}
			getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().consultarDadosDetalhePainelGestorMonitoramentoCRMPorCurso(getUnidadeEnsinoVOs(), getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().getDataInicio(), getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().getDataTermino(), getPainelGestorMonitoramentoCRM(), getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO());
			getPainelGestorMonitoramentoCRM().setPainelGestorTipoMonitoramentoCRMEnum(getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().getPainelGestorTipoMonitoramentoCRMEnum());
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoCRMDetalhe.xhtml");
	}

	public String consultarDadosDetalheConsultorPainelGestorMonitoramentoCRM() {
		try {

			setMostarProspectCrm(false);
			getPainelGestorMonitoramentoCRM().getProspectsVOs().clear();
			getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO((PainelGestorMonitoramentoDetalheCRMVO) context().getExternalContext().getRequestMap().get("detalheItens"));
			getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().consultarDadosDetalhePainelGestorMonitoramentoCRMPorConsultor(getUnidadeEnsinoVOs(), getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().getDataInicio(), getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().getDataTermino(), getPainelGestorMonitoramentoCRM(), getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO());
			getPainelGestorMonitoramentoCRM().setPainelGestorTipoMonitoramentoCRMEnum(getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().getPainelGestorTipoMonitoramentoCRMEnum());
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoCRMDetalhe.xhtml");
	}

	public void consultarDadosProspectPainelGestorMonitoramentoCRM() {
		try {
			getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().consultarDadosProspectDetalhePainelGestorMonitoramentoCRM(getUnidadeEnsinoVOs(), getPainelGestorMonitoramentoCRM(), (PainelGestorMonitoramentoDetalheCRMVO) context().getExternalContext().getRequestMap().get("detalheItens"));
			setMostarProspectCrm(true);
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}

	public void ocultarProspectCrm() {
		realizarAtualizacaorDadosDetalhePainelGestorMonitoramentoCRM();
		setMostarProspectCrm(false);
	}

	public PainelGestorMonitoramentoCRMVO getPainelGestorMonitoramentoCRM() {
		if (painelGestorMonitoramentoCRM == null) {
			painelGestorMonitoramentoCRM = new PainelGestorMonitoramentoCRMVO();
		}
		return painelGestorMonitoramentoCRM;
	}

	public void setPainelGestorMonitoramentoCRM(PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRM) {
		this.painelGestorMonitoramentoCRM = painelGestorMonitoramentoCRM;
	}

	public CompromissoAgendaPessoaHorarioVO getCompromissoAgendaPessoaHorario() {
		if (compromissoAgendaPessoaHorario == null) {
			compromissoAgendaPessoaHorario = new CompromissoAgendaPessoaHorarioVO();
		}
		return compromissoAgendaPessoaHorario;
	}

	public void setCompromissoAgendaPessoaHorario(CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorario) {
		this.compromissoAgendaPessoaHorario = compromissoAgendaPessoaHorario;
	}

	private String campoConsultarCampanha;
	private String valorConsultarCampanha;
	private List<CampanhaVO> listaConsultarCampanha;

	public String getCampoConsultarCampanha() {
		if (campoConsultarCampanha == null) {
			campoConsultarCampanha = "";
		}
		return campoConsultarCampanha;
	}

	public void setCampoConsultarCampanha(String campoConsultarCampanha) {
		this.campoConsultarCampanha = campoConsultarCampanha;
	}

	public String getValorConsultarCampanha() {
		if (valorConsultarCampanha == null) {
			valorConsultarCampanha = "";
		}
		return valorConsultarCampanha;
	}

	public void setValorConsultarCampanha(String valorConsultarCampanha) {
		this.valorConsultarCampanha = valorConsultarCampanha;
	}

	public List<CampanhaVO> getListaConsultarCampanha() {
		if (listaConsultarCampanha == null) {
			listaConsultarCampanha = new ArrayList<CampanhaVO>(0);
		}
		return listaConsultarCampanha;
	}

	public void setListaConsultarCampanha(List<CampanhaVO> listaConsultarCampanha) {
		this.listaConsultarCampanha = listaConsultarCampanha;
	}

	public void consultarCampanha() {
		try {
			List<CampanhaVO> objs = new ArrayList<CampanhaVO>(0);
			if (getCampoConsultarCampanha().equals("descricao")) {
				objs = getFacadeFactory().getCampanhaFacade().consultarPorDescricao(getValorConsultarCampanha(), "AT", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultarCampanha().equals("unidadeensino")) {
				objs = getFacadeFactory().getCampanhaFacade().consultarPorUnidadeEnsino(getValorConsultarCampanha(), "AT", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultarCampanha().equals("curso")) {
				objs = getFacadeFactory().getCampanhaFacade().consultarPorCurso(getValorConsultarCampanha(), "AT", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			// if (getCampoConsultarCampanha().equals("situacao")) {
			// objs =
			// getFacadeFactory().getCampanhaFacade().consultarPorSituacao(getValorConsultarCampanha(),
			// false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			// }
			setListaConsultarCampanha(objs);
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			getListaConsultarCampanha().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarCampanha() throws Exception {
		CampanhaVO obj = (CampanhaVO) context().getExternalContext().getRequestMap().get("campanhaItens");
		if (getMensagemDetalhada().equals("")) {
			this.getCompromissoAgendaPessoaHorario().setCampanha(getFacadeFactory().getCampanhaFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		}
		Uteis.liberarListaMemoria(this.getListaConsultarCampanha());
		this.setValorConsultarCampanha("");
		this.setCampoConsultarCampanha("");
	}

	public void limparCampoCampanha() {
		getCompromissoAgendaPessoaHorario().setCampanha(new CampanhaVO());
	}

	public List<SelectItem> getTipoConsultaComboCampanha() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("unidadeensino", "Unidade"));
		itens.add(new SelectItem("curso", "Curso"));
		// itens.add(new SelectItem("situacao", "Situação"));
		return itens;
	}

	private Map<Integer, List<SelectItem>> consultoresPorUnidade = new HashMap<Integer, List<SelectItem>>(0);

	public List<SelectItem> getListaSelectItemConsultor() {
		ProspectsVO prospectsVO = (ProspectsVO) context().getExternalContext().getRequestMap().get("prospectItens");
		if (consultoresPorUnidade.containsKey(prospectsVO.getUnidadeEnsino().getCodigo())) {
			return consultoresPorUnidade.get(prospectsVO.getUnidadeEnsino().getCodigo());
		} else {
			List<FuncionarioVO> funcionarioVOs = getFacadeFactory().getFuncionarioFacade().consultarFuncionarioConsultorPorUnidade(prospectsVO.getUnidadeEnsino().getCodigo());
			List<SelectItem> consultores = new ArrayList<SelectItem>(0);
			consultores.add(new SelectItem(0, ""));
			for (FuncionarioVO funcionarioVO : funcionarioVOs) {
				consultores.add(new SelectItem(funcionarioVO.getCodigo(), funcionarioVO.getPessoa().getNome()));
			}
			consultoresPorUnidade.put(prospectsVO.getUnidadeEnsino().getCodigo(), consultores);
		}
		return consultoresPorUnidade.get(prospectsVO.getUnidadeEnsino().getCodigo());
	}

	public void alterarCompromissoAgendaExistentesParaNovoResponsavel(ProspectsVO prospectsVO) {
		try {
			if (prospectsVO.getPreInscricao().getCodigo() > 0) {
				PreInscricaoVO pre = getFacadeFactory().getPreInscricaoFacade().consultarPorChavePrimaria(prospectsVO.getPreInscricao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());

				CampanhaColaboradorCursoVO campanhaColaboradorCursoVO = getFacadeFactory().getCampanhaColaboradorCursoFacade().consultarCampanhaAndResponsavel(pre.getCurso().getCodigo(), pre.getUnidadeEnsino().getCodigo(), "AT", TipoCampanhaEnum.PRE_INSCRICAO, false, getUsuarioLogado());

				getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().removerCompromissoPreInscricaoPessoaAnteriormenteResponsavelCompromisso(prospectsVO.getConsultorPadrao().getCodigo(), prospectsVO.getCodigo(), campanhaColaboradorCursoVO.getCampanhaColaboradorVO().getCampanha().getCodigo(), false, getUsuarioLogado());

				getFacadeFactory().getPreInscricaoFacade().realizarPersistenciaCompromissoPreInscricao(pre, false, getUsuarioLogado(), prospectsVO.getConsultorPadrao());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void iniciarAlteracaoConsultorUnidadeEnsinoProspect() {
		try {
			setProspectsVO(new ProspectsVO());
			setProspectsVO((ProspectsVO) context().getExternalContext().getRequestMap().get("prospectItens"));
			ProspectsVO obj = getFacadeFactory().getProspectsFacade().consultarPorChavePrimaria(prospectsVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			if (obj.getConsultorPadrao().getCodigo().intValue() == getProspectsVO().getConsultorPadrao().getCodigo().intValue()) {
				getProspectsVO().setConsultorAlterado(false);
				alterarConsultorUnidadeEnsinoProspect();
			} else {
				getProspectsVO().setConsultorAlterado(true);
			}
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}

	public String getAbrirModalDefinicaoCompromissoProspect() {
		if (getProspectsVO().getConsultorAlterado()) {
			return "RichFaces.$('panelAlterarConsultor').show()";
		}
		return "";
	}

	public void alterarConsultorUnidadeEnsinoProspect() {
		try {
			// ProspectsVO prospectsVO = (ProspectsVO)
			// context().getExternalContext().getRequestMap().get("prospect");
			// atualizando os dados do consultor padrao pois iremos precisar da
			// pessoa referente ao funcionario
			// em questaos
			if (getProspectsVO().getConsultorPadrao().getCodigo() > 0) {
				getProspectsVO().setConsultorPadrao(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorChavePrimaria(prospectsVO.getConsultorPadrao().getCodigo(), false, getUsuarioLogado()));
			} else {
				getProspectsVO().setConsultorPadrao(null);
			}
			getFacadeFactory().getProspectsFacade().alterarUnidadeEnsinoEConsultorProspect(getProspectsVO(), getUsuarioLogado());
			alterarCompromissoAgendaExistentesParaNovoResponsavel(getProspectsVO());
			getProspectsVO().setConsultorAlterado(false);
			setMensagemID("msg_consultor_alterado_com_sucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarCadastroCompromisso() {
		try {
			ProspectsVO prospectsVO = (ProspectsVO) context().getExternalContext().getRequestMap().get("prospectItens");
			setCompromissoAgendaPessoaHorario(new CompromissoAgendaPessoaHorarioVO());
			if (prospectsVO.getConsultorPadrao().getCodigo() != null && prospectsVO.getConsultorPadrao().getCodigo() > 0) {
				prospectsVO.setConsultorPadrao(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorChavePrimaria(prospectsVO.getConsultorPadrao().getCodigo(), false, getUsuarioLogado()));
			}
			getCompromissoAgendaPessoaHorario().setProspect(prospectsVO);
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluirPreInscricao() {
		try {
			ProspectsVO prospectsVO = (ProspectsVO) context().getExternalContext().getRequestMap().get("prospectItens");
			// for (ProspectsVO prospectsRemover :
			// this.getPainelGestorMonitoramentoCRM().getProspectsVOs()) {
			// }
			this.getPainelGestorMonitoramentoCRM().getProspectsVOs().remove(prospectsVO);
			getFacadeFactory().getPreInscricaoFacade().excluir(prospectsVO.getPreInscricao(), getUsuarioLogado());
			limparMensagem();
			setMensagemDetalhada("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void gravarCompromissoAgendaPessoaHorario() {
		try {
			getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().setAgendaPessoa(getFacadeFactory().getFollowUpFacade().realizarValidacaoAgendaFollowUp(getCompromissoAgendaPessoaHorario().getProspect().getConsultorPadrao().getPessoa(), getUsuarioLogado()));
			getCompromissoAgendaPessoaHorario().setAgendaPessoaHorario(getFacadeFactory().getFollowUpFacade().realizarValidacaoAgendaPessoaExiste(getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().getAgendaPessoa(), getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario(), getCompromissoAgendaPessoaHorario(), getUsuarioLogado()));
			if (getCompromissoAgendaPessoaHorario().getCampanha().getCodigo() != 0) {
				getCompromissoAgendaPessoaHorario().setEtapaWorkflowVO(getFacadeFactory().getEtapaWorkflowFacade().consultarPorCodigoCampanhaEtapaInicial(getCompromissoAgendaPessoaHorario().getCampanha().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			CompromissoAgendaPessoaHorarioVO.validarDados(getCompromissoAgendaPessoaHorario());
			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().incluirCompromissoPorAgendaHorarioPessoa(getCompromissoAgendaPessoaHorario(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);

			setManterRichModalAberto(false);

		} catch (Exception e) {
			setManterRichModalAberto(true);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}

	public String getAbrirRichModalCompromisso() {
		if (!getManterRichModalAberto()) {
			return "RichFaces.$('panelNovoCompromisso').hide()";
		}
		return "RichFaces.$('panelNovoCompromisso').show()";
	}

	private Boolean manterRichModalAberto;

	public Boolean getManterRichModalAberto() {
		if (manterRichModalAberto == null) {
			manterRichModalAberto = false;
		}
		return manterRichModalAberto;
	}

	public void setManterRichModalAberto(Boolean manterRichModalAberto) {
		this.manterRichModalAberto = manterRichModalAberto;
	}

	private List<SelectItem> listaSelectItemUnidadeEnsino;

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
			listaSelectItemUnidadeEnsino.add(new SelectItem(0, ""));
			for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
				listaSelectItemUnidadeEnsino.add(new SelectItem(unidade.getCodigo(), unidade.getNome()));
			}
		}
		return listaSelectItemUnidadeEnsino;
	}

	public Boolean getMostarProspectCrm() {
		if (mostarProspectCrm == null) {
			mostarProspectCrm = false;
		}
		return mostarProspectCrm;
	}

	public void setMostarProspectCrm(Boolean mostarProspectCrm) {
		this.mostarProspectCrm = mostarProspectCrm;
	}

	/**
	 * @return the permissaoExcluirPreInscricao
	 */
	public Boolean getPermissaoExcluirPreInscricao() {
		if (permissaoExcluirPreInscricao == null) {
			try {
				ControleAcesso.excluir("PreInscricao", getUsuarioLogado());
				permissaoExcluirPreInscricao = Boolean.TRUE;
			} catch (Exception e) {
				permissaoExcluirPreInscricao = Boolean.FALSE;
			}
		}
		return permissaoExcluirPreInscricao;
	}

	/**
	 * @param permissaoExcluirPreInscricao
	 *            the permissaoExcluirPreInscricao to set
	 */
	public void setPermissaoExcluirPreInscricao(Boolean permissaoExcluirPreInscricao) {
		this.permissaoExcluirPreInscricao = permissaoExcluirPreInscricao;
	}

	public String voltarVisualizacaoProspectMonitoramento() {
		if (getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum() == null 
			|| getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.COMO_FICOU_SABENDO_INSTITUICAO)) {
			getPainelGestorMonitoramentoCRM().setCurso(null);
			getPainelGestorMonitoramentoCRM().setConsultor(null);
			getPainelGestorMonitoramentoCRM().setUnidadeEnsino(null);
			getPainelGestorMonitoramentoCRM().setTipoMidiaCaptacao(null);
			getPainelGestorMonitoramentoCRM().setPainelGestorTipoMonitoramentoCRMEnum(null);
			consultarDadosIniciaisPainelGestorMonitoramentoProspectCRM();
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoCRM.xhtml");
		}
		if(getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.SEGMENTACAO_PROSPECT)) {
			getPainelGestorMonitoramentoCRM().setPainelGestorTipoMonitoramentoCRMEnum(null);
			return  Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoSegmentacaoCRM.xhtml");
		}
		if (getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CURSO)) {
			getPainelGestorMonitoramentoCRM().setCurso(null);
			getPainelGestorMonitoramentoCRM().setPainelGestorTipoMonitoramentoCRMEnum(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CONSULTOR);
			consultarDadosDetalhePainelGestorMonitoramentoProspectCRM();
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoCrmDetalheProspect.xhtml");
		}
		consultarDadosDetalhePainelGestorMonitoramentoProspectCRM();
		return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoCrmDetalheProspect.xhtml");
	}

	public String voltarDetalheProspectMonitoramento() {

		if (getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum() == null) {
			getPainelGestorMonitoramentoCRM().setCurso(null);
			getPainelGestorMonitoramentoCRM().setConsultor(null);
			getPainelGestorMonitoramentoCRM().setUnidadeEnsino(null);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoCRM.xhtml");
		}
		if (getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CURSO)) {
			getPainelGestorMonitoramentoCRM().setCurso(null);
			getPainelGestorMonitoramentoCRM().setPainelGestorTipoMonitoramentoCRMEnum(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CONSULTOR);
			consultarDadosDetalhePainelGestorMonitoramentoProspectCRM();
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoCrmDetalheProspect.xhtml");
		} else if (getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO)) {
			getPainelGestorMonitoramentoCRM().setCurso(null);
			getPainelGestorMonitoramentoCRM().setConsultor(null);
			getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().setCurso(null);
			getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().setConsultor(null);
			getPainelGestorMonitoramentoCRM().setPainelGestorTipoMonitoramentoCRMEnum(null);
			consultarDadosIniciaisPainelGestorMonitoramentoCRM();
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoCRM.xhtml");
		} else if (getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CONSULTOR)) {
			getPainelGestorMonitoramentoCRM().setCurso(null);
			getPainelGestorMonitoramentoCRM().setConsultor(null);
			getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().setCurso(null);
			getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().setConsultor(null);
			getPainelGestorMonitoramentoCRM().setPainelGestorTipoMonitoramentoCRMEnum(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO);
			realizarAtualizacaorDadosDetalhePainelGestorMonitoramentoCRM();
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoCRMDetalhe.xhtml");
		} else if (getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CURSO)) {
			getPainelGestorMonitoramentoCRM().setCurso(null);
			getPainelGestorMonitoramentoCRM().setConsultor(null);
			getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().setCurso(null);
			getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().setConsultor(null);
			getPainelGestorMonitoramentoCRM().setPainelGestorTipoMonitoramentoCRMEnum(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO);
			realizarAtualizacaorDadosDetalhePainelGestorMonitoramentoCRM();
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoCRMDetalhe.xhtml");
		} else if (getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CONSULTOR_CURSO)) {
			getPainelGestorMonitoramentoCRM().setCurso(null);
			getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().setCurso(null);
			getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().setPainelGestorTipoMonitoramentoCRMEnum(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CONSULTOR);
			getPainelGestorMonitoramentoCRM().setPainelGestorTipoMonitoramentoCRMEnum(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO);
			realizarAtualizacaorDadosDetalhePainelGestorMonitoramentoCRM();
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoCRMDetalhe.xhtml");
		} else if (getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CURSO_CONSULTOR)) {
			getPainelGestorMonitoramentoCRM().setConsultor(null);
			getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().setConsultor(null);
			getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().setPainelGestorTipoMonitoramentoCRMEnum(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO_DETALHE_CURSO);
			getPainelGestorMonitoramentoCRM().setPainelGestorTipoMonitoramentoCRMEnum(PainelGestorTipoMonitoramentoCRMEnum.PRE_INSCRICAO);
			realizarAtualizacaorDadosDetalhePainelGestorMonitoramentoCRM();
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoCRMDetalhe.xhtml");
		} else if (getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CONSULTOR)) {
			getPainelGestorMonitoramentoCRM().setCurso(null);
			getPainelGestorMonitoramentoCRM().setConsultor(null);
			getPainelGestorMonitoramentoCRM().setPainelGestorTipoMonitoramentoCRMEnum(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_UNIDADE);
			consultarDadosDetalhePainelGestorMonitoramentoProspectCRM();
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoCrmDetalheProspect.xhtml");
		} else if (getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_UNIDADE)) {
			getPainelGestorMonitoramentoCRM().setCurso(null);
			getPainelGestorMonitoramentoCRM().setConsultor(null);
			getPainelGestorMonitoramentoCRM().setUnidadeEnsino(null);
			getPainelGestorMonitoramentoCRM().setPainelGestorTipoMonitoramentoCRMEnum(null);
			consultarDadosIniciaisPainelGestorMonitoramentoProspectCRM();
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoCRM.xhtml");
		}
		consultarDadosDetalhePainelGestorMonitoramentoProspectCRM();
		return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoCrmDetalheProspect.xhtml");
	}

	public String verificarPaginaMonitoramentoProspectNavegar() {
		if (getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CURSO) || getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.COMO_FICOU_SABENDO_INSTITUICAO)) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoCrmVisualizarProspect.xhtml");
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoCrmDetalheProspect.xhtml");
	}
		
	public String selecionarLegendaGraficoProspect() {
		selecionarLegendaGraficoProspect(getCodigo(), getNome(), getOpcao());
		return verificarPaginaMonitoramentoProspectNavegar();
	}
	
	public void selecionarLegendaGraficoProspect(Integer codigo, String nome, String nivel) {
		try {
			if (nivel.equals("PROSPECT_POR_UNIDADE")) {
				getPainelGestorMonitoramentoCRM().setPainelGestorTipoMonitoramentoCRMEnum(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_UNIDADE);
				getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().consultarMonitoramentoProspectPorUnidade(getDataInicio(), getDataFinal(), codigo, getPainelGestorMonitoramentoCRM());
			}
			if (nivel.equals("PROSPECT_POR_CONSULTOR")) {
				getPainelGestorMonitoramentoCRM().setPainelGestorTipoMonitoramentoCRMEnum(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CONSULTOR);
				getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().consultarMonitoramentoProspectPorConsultor(getDataInicio(), getDataFinal(), getPainelGestorMonitoramentoCRM().getUnidadeEnsino().getCodigo(), codigo, getPainelGestorMonitoramentoCRM());
			}
			if (nivel.equals("PROSPECT_POR_CURSO")) {
				if (codigo != null && codigo > 0) {
					getPainelGestorMonitoramentoCRM().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(codigo, Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado()));
				} else {
					getPainelGestorMonitoramentoCRM().getCurso().setCodigo(0);
					getPainelGestorMonitoramentoCRM().getCurso().setNome("Sem Curso de Interesse");
				}
				getPainelGestorMonitoramentoCRM().setPainelGestorTipoMonitoramentoCRMEnum(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CURSO);
				getControleConsultaOtimizado().setPage(0);
				getControleConsultaOtimizado().setPaginaAtual(0);
				getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_CADASTRADOS);
				getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
				consultarProspectsMonitoramento();
			}
			if (nivel.equals(PainelGestorTipoMonitoramentoCRMEnum.COMO_FICOU_SABENDO_INSTITUICAO.name())) {
				getPainelGestorMonitoramentoCRM().setPainelGestorTipoMonitoramentoCRMEnum(PainelGestorTipoMonitoramentoCRMEnum.COMO_FICOU_SABENDO_INSTITUICAO);
				getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.COMO_FICOU_SABENDO_INSTITUICAO);
				getPainelGestorMonitoramentoCRM().getTipoMidiaCaptacao().setCodigo(codigo);
				getPainelGestorMonitoramentoCRM().getTipoMidiaCaptacao().setNomeMidia(nome);
				getControleConsultaOtimizado().setPage(0);
				getControleConsultaOtimizado().setPaginaAtual(0);
				getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
				consultarDadosComoFicouSabendoInstituicao();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String consultarTodosProspectsCadastrados() {
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_CADASTRADOS);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
		getPainelGestorMonitoramentoCRM().setFiltrarMatriculado(null);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsCadastradosNaoMatriculado() {
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_CADASTRADOS);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
		getPainelGestorMonitoramentoCRM().setFiltrarMatriculado(false);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsCadastradosMatriculado() {
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_CADASTRADOS);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
		getPainelGestorMonitoramentoCRM().setFiltrarMatriculado(true);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsComAgenda() {
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_COM_AGENDA);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
		getPainelGestorMonitoramentoCRM().setFiltrarMatriculado(null);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsComAgendaNaoMatriculado() {
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_COM_AGENDA);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
		getPainelGestorMonitoramentoCRM().setFiltrarMatriculado(false);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsComAgendaMatriculado() {
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_COM_AGENDA);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
		getPainelGestorMonitoramentoCRM().setFiltrarMatriculado(true);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsSemAgenda() {
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_AGENDA);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
		getPainelGestorMonitoramentoCRM().setFiltrarMatriculado(null);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsSemAgendaNaoMatriculado() {
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_AGENDA);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
		getPainelGestorMonitoramentoCRM().setFiltrarMatriculado(false);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsSemAgendaMatriculado() {
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_AGENDA);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
		getPainelGestorMonitoramentoCRM().setFiltrarMatriculado(true);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsSemConsultor() {
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_CONSULTOR);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
		getPainelGestorMonitoramentoCRM().setFiltrarMatriculado(null);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsSemConsultorNaoMatriculado() {
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_CONSULTOR);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
		getPainelGestorMonitoramentoCRM().setFiltrarMatriculado(false);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsSemConsultorMatriculado() {
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_CONSULTOR);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
		getPainelGestorMonitoramentoCRM().setFiltrarMatriculado(true);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsComConsultor() {
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_COM_CONSULTOR);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
		getPainelGestorMonitoramentoCRM().setFiltrarMatriculado(null);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsComConsultorNaoMatriculado() {
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_COM_CONSULTOR);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
		getPainelGestorMonitoramentoCRM().setFiltrarMatriculado(false);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsComConsultorMatriculado() {
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_COM_CONSULTOR);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
		getPainelGestorMonitoramentoCRM().setFiltrarMatriculado(true);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsSemContatoPeriodo1() {
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_CONTATO_PERIODO1);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
		getPainelGestorMonitoramentoCRM().setFiltrarMatriculado(null);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsSemContatoPeriodo1NaoMatriculado() {
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_CONTATO_PERIODO1);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
		getPainelGestorMonitoramentoCRM().setFiltrarMatriculado(false);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsSemContatoPeriodo1Matriculado() {
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_CONTATO_PERIODO1);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
		getPainelGestorMonitoramentoCRM().setFiltrarMatriculado(true);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsSemContatoPeriodo2() {
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_CONTATO_PERIODO2);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
		getPainelGestorMonitoramentoCRM().setFiltrarMatriculado(null);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsSemContatoPeriodo2NaoMatriculado() {
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_CONTATO_PERIODO2);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
		getPainelGestorMonitoramentoCRM().setFiltrarMatriculado(false);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsSemContatoPeriodo2Matriculado() {
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_CONTATO_PERIODO2);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
		getPainelGestorMonitoramentoCRM().setFiltrarMatriculado(true);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsSemContatoPeriodo3() {
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_CONTATO_PERIODO3);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
		getPainelGestorMonitoramentoCRM().setFiltrarMatriculado(null);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsSemContatoPeriodo3NaoMatriculado() {
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_CONTATO_PERIODO3);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
		getPainelGestorMonitoramentoCRM().setFiltrarMatriculado(false);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsSemContatoPeriodo3Matriculado() {
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.TODOS_PROSPECTS_SEM_CONTATO_PERIODO3);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(new PainelGestorMonitoramentoDetalheCRMVO());
		getPainelGestorMonitoramentoCRM().setFiltrarMatriculado(true);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsMesAnoAbordado() {
		PainelGestorMonitoramentoDetalheCRMVO painelGestorMonitoramentoDetalheCRMVO = (PainelGestorMonitoramentoDetalheCRMVO) getRequestMap().get("prospectMesItens");
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.MES_ANO_ABORDADO);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(painelGestorMonitoramentoDetalheCRMVO);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsMesFinalizadoSucesso() {
		PainelGestorMonitoramentoDetalheCRMVO painelGestorMonitoramentoDetalheCRMVO = (PainelGestorMonitoramentoDetalheCRMVO) getRequestMap().get("prospectMesItens");
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.MES_ANO_FINALIZADO_SUCESSO);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(painelGestorMonitoramentoDetalheCRMVO);
		return consultarProspectsMonitoramento();
	}

	public String consultarTodosProspectsMesMatriculado() {
		PainelGestorMonitoramentoDetalheCRMVO painelGestorMonitoramentoDetalheCRMVO = (PainelGestorMonitoramentoDetalheCRMVO) getRequestMap().get("prospectMesItens");
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.MES_ANO_MATRICULADO);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(painelGestorMonitoramentoDetalheCRMVO);
		return consultarProspectsMonitoramento();
	}
	
	public String consultarTodosProspectsMesAnoReagendado() {
		PainelGestorMonitoramentoDetalheCRMVO painelGestorMonitoramentoDetalheCRMVO = (PainelGestorMonitoramentoDetalheCRMVO) getRequestMap().get("prospectMesItens");
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.MES_ANO_REAGENDADO);
		getPainelGestorMonitoramentoCRM().setPainelGestorMonitoramentoDetalheCRMVO(painelGestorMonitoramentoDetalheCRMVO);
		return consultarProspectsMonitoramento();
	}

	public String consultarProspectsMonitoramento() {
		try {
			getControleConsultaOtimizado().setLimitePorPagina(50);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().consultarDadosProspectMonitoramento(getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), getPainelGestorMonitoramentoCRM().getCurso().getCodigo(), getPainelGestorMonitoramentoCRM().getConsultor().getCodigo(), getPainelGestorMonitoramentoCRM().getUnidadeEnsino().getCodigo(), getPainelGestorMonitoramentoCRM().getTipoFiltroMonitamentoCrmProspectEnum(), getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().getMesAnoEnum(), getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().getAno(), getPainelGestorMonitoramentoCRM().getPeriodoUltimoContato1(), getPainelGestorMonitoramentoCRM().getPeriodoUltimoContato2(), getPainelGestorMonitoramentoCRM().getPeriodoUltimoContato3(), getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum(), getPainelGestorMonitoramentoCRM().getFiltrarMatriculado(),
					getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getCodigoSegmentacao()));
			getControleConsultaOtimizado()
					.setTotalRegistrosEncontrados(getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().consultarTotalDadosProspectMonitoramento(getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), getPainelGestorMonitoramentoCRM().getCurso().getCodigo(), getPainelGestorMonitoramentoCRM().getConsultor().getCodigo(), getPainelGestorMonitoramentoCRM().getUnidadeEnsino().getCodigo(), getPainelGestorMonitoramentoCRM().getTipoFiltroMonitamentoCrmProspectEnum(), getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().getMesAnoEnum(), getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().getAno(), getPainelGestorMonitoramentoCRM().getPeriodoUltimoContato1(), getPainelGestorMonitoramentoCRM().getPeriodoUltimoContato2(), getPainelGestorMonitoramentoCRM().getPeriodoUltimoContato3(), getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum(), getPainelGestorMonitoramentoCRM().getFiltrarMatriculado(), getCodigoSegmentacao()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoCrmVisualizarProspect.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public void scrollerListenerProspectsMonitoramento(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		if (getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum() != null) {
			if (getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.COMO_FICOU_SABENDO_INSTITUICAO)) {
				consultarDadosComoFicouSabendoInstituicao();
			} else {
				consultarProspectsMonitoramento();
			}
		} else {
			consultarProspectsMonitoramento();
		}
	}

	public Integer getColunasMonitoramentoProspectMes() {
		if (getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVOs().size() > 6) {
			return 6;
		}
		return getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVOs().size();
	}

	public Integer getElementosMonitoramentoProspectMes() {
		return getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVOs().size();
	}

	public String getAno() {
		if (ano == null || ano.isEmpty()) {
			ano = Uteis.getAnoDataAtual4Digitos();
		}
		return ano;
	}

	public void setAno(String ano) {

		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = Uteis.getSemestreAtual();
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public List<SelectItem> getListaSelectItemSemestre() {
		if (listaSelectItemSemestre == null) {
			listaSelectItemSemestre = new ArrayList<SelectItem>(0);
			listaSelectItemSemestre.add(new SelectItem("1", UteisJSF.internacionalizar("prt_Primeiro_Semestre")));
			listaSelectItemSemestre.add(new SelectItem("2", UteisJSF.internacionalizar("prt_Segundo_Semestre")));
		}
		return listaSelectItemSemestre;
	}

	public void setListaSelectItemSemestre(List<SelectItem> listaSelectItemSemestre) {
		this.listaSelectItemSemestre = listaSelectItemSemestre;
	}

	public List<SelectItem> getListaSelectItemAno() {
		try {
			
			if (listaSelectItemAno == null) {
				listaSelectItemAno = new ArrayList<SelectItem>(0);
				List<String> anos = getFacadeFactory().getMatriculaPeriodoFacade().consultarAnosMatriculaPeriodo();
				for (String ano : anos) {
					listaSelectItemAno.add(new SelectItem(ano, ano));
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return listaSelectItemAno;
	}

	public void setListaSelectItemAno(List<SelectItem> listaSelectItemAno) {
		this.listaSelectItemAno = listaSelectItemAno;
	}

	public ControlePaginaPainelGestorEnum getTelaOrigem() {
		if (telaOrigem == null) {
			telaOrigem = ControlePaginaPainelGestorEnum.PAGINA_INICIAL;
		}
		return telaOrigem;
	}

	public void setTelaOrigem(ControlePaginaPainelGestorEnum telaOrigem) {
		this.telaOrigem = telaOrigem;
	}

	public void inicializarDataMesAno() throws NumberFormatException, Exception {
		if (!getMesAno().trim().isEmpty()) {
			String mes = getMesAno().substring(0, getMesAno().indexOf("/"));
			String ano = getMesAno().substring(getMesAno().indexOf("/") + 1, getMesAno().length());
			setDataInicioMesAno(Uteis.gerarDataDiaMesAno(1, Integer.valueOf(mes), Integer.valueOf(ano)));
			setDataFinalMesAno(Uteis.getDataUltimoDiaMes(getDataInicioMesAno()));
			setDataInicioTemporaria(getDataInicio());
			setDataFinalTemporaria(getDataFinal());
		}
	}

	public Date getDataInicioMesAno() {
		if (dataInicioMesAno == null) {
			dataInicioMesAno = getDataInicio();
		}
		return dataInicioMesAno;
	}

	public void setDataInicioMesAno(Date dataInicioMesAno) {
		this.dataInicioMesAno = dataInicioMesAno;
	}

	public Date getDataFinalMesAno() {
		if (dataFinalMesAno == null) {
			dataFinalMesAno = getDataFinal();
		}
		return dataFinalMesAno;
	}

	public void setDataFinalMesAno(Date dataFinalMesAno) {
		this.dataFinalMesAno = dataFinalMesAno;
	}

	public List<DocumetacaoMatriculaVO> getListaDoc() {
		if (listaDoc == null) {
			listaDoc = new ArrayList<DocumetacaoMatriculaVO>();
		}
		return listaDoc;
	}

	public void setListaDoc(List<DocumetacaoMatriculaVO> listaDoc) {
		this.listaDoc = listaDoc;
	}

	public Boolean getFecharGraficoMonitoramentoProspect() {
		if (fecharGraficoMonitoramentoProspect == null) {
			fecharGraficoMonitoramentoProspect = false;
		}
		return fecharGraficoMonitoramentoProspect;
	}

	public void setFecharGraficoMonitoramentoProspect(Boolean fecharGraficoMonitoramentoProspect) {
		this.fecharGraficoMonitoramentoProspect = fecharGraficoMonitoramentoProspect;
	}

	public String getSituacaoProspect() {
		if (situacaoProspect == null) {
			situacaoProspect = "";
		}
		return situacaoProspect;
	}

	public void setSituacaoProspect(String situacaoProspect) {
		this.situacaoProspect = situacaoProspect;
	}

	public List<SelectItem> getListaSelectItemSituacaoProspect() {
		if (listaSelectItemSituacaoProspect == null) {
			listaSelectItemSituacaoProspect = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoProspect.add(new SelectItem("", UteisJSF.internacionalizar("enum_PainelGestor_situacaoProspect_TODAS")));
			listaSelectItemSituacaoProspect.add(new SelectItem("MATRICULADO", UteisJSF.internacionalizar("enum_PainelGestor_situacaoProspect_MATRICULADO")));
			listaSelectItemSituacaoProspect.add(new SelectItem("FINALIZADO_INSUCESSO", UteisJSF.internacionalizar("enum_PainelGestor_situacaoProspect_FINALIZADO_INSUCESSO")));
			listaSelectItemSituacaoProspect.add(new SelectItem("NORMAL", UteisJSF.internacionalizar("enum_PainelGestor_situacaoProspect_NORMAL")));
		}
		return listaSelectItemSituacaoProspect;
	}

	public void setListaSelectItemSituacaoProspect(List<SelectItem> listaSelectItemSituacaoProspect) {
		this.listaSelectItemSituacaoProspect = listaSelectItemSituacaoProspect;
	}

	public void imprimirReceitasFluxoCaixa() {
		String caminho = "";
		String design = "";
		String titulo = "";
		setSuperParametroRelVO(new SuperParametroRelVO());
		try {
			if (getTelaOrigem() != null && getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO_POR_NIVEL_EDUCACIONAL_FLUXO_CAIXA)) {
				titulo = "Receitas por Curso (Fluxo Caixa): " + getCursoVO().getNome();
				design = ("relatorio" + File.separator + "designRelatorio" + File.separator + "painelGestor" + File.separator + "PainelGestorFinanceiroReceitaTurmaFluxo.jrxml");
				getSuperParametroRelVO().setListaObjetos(getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getPainelGestorContaReceberMesAnoTurmaVOs());

				getSuperParametroRelVO().adicionarParametro("totalReceitaNoMes", getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getTotalReceitaNoMes());
				getSuperParametroRelVO().adicionarParametro("totalAcrescimoNoMes", getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getTotalAcrescimoNoMes());
				getSuperParametroRelVO().adicionarParametro("totalDescontoNoMes", getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getTotalDescontoNoMes());
				getSuperParametroRelVO().adicionarParametro("mediaInadimplenciaNoPeriodo", getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getMediaInadimplenciaNoPeriodo());
				getSuperParametroRelVO().setPeriodo(Uteis.periodoEntreDatas(getDataInicioMesAno(), getDataInicioMesAno(), "MM/yyyy"));
			} else if (getTelaOrigem() != null && getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO)) {
				titulo = "Receitas por Nível (Fluxo Caixa): " + getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getTipoNivelEducacional().getDescricao();
				design = ("relatorio" + File.separator + "designRelatorio" + File.separator + "painelGestor" + File.separator + "PainelGestorFinanceiroReceitaCursoFluxo.jrxml");
				getSuperParametroRelVO().setListaObjetos(getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getPainelGestorContaReceberFluxoCaixaMesAnoVOs());

				getSuperParametroRelVO().adicionarParametro("totalReceitaNoMes", getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getTotalReceitaNoMes());
				getSuperParametroRelVO().adicionarParametro("totalAcrescimoNoMes", getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getTotalAcrescimoNoMes());
				getSuperParametroRelVO().adicionarParametro("totalDescontoNoMes", getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getTotalDescontoNoMes());
				getSuperParametroRelVO().adicionarParametro("mediaInadimplenciaNoPeriodo", getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getMediaInadimplenciaNoPeriodo());
				getSuperParametroRelVO().setPeriodo(Uteis.periodoEntreDatas(getDataInicioMesAno(), getDataInicioMesAno(), "MM/yyyy"));
			} else {

				titulo = "Receitas Por Mês - Fluxo Caixa";
				design = ("relatorio" + File.separator + "designRelatorio" + File.separator + "painelGestor" + File.separator + "PainelGestorFinanceiroReceitaMesAMesFluxo.jrxml");

				getSuperParametroRelVO().setListaObjetos(getPainelGestorVO().getPainelGestorContaReceberFluxoCaixaMesAnoVOs());
				getSuperParametroRelVO().adicionarParametro("totalReceitaNoMes", getPainelGestorVO().getTotalReceitaNoMes());
				getSuperParametroRelVO().adicionarParametro("totalAcrescimoNoMes", getPainelGestorVO().getTotalAcrescimoNoMes());
				getSuperParametroRelVO().adicionarParametro("totalDescontoNoMes", getPainelGestorVO().getTotalDescontoNoMes());
				getSuperParametroRelVO().adicionarParametro("mediaInadimplenciaNoPeriodo", getPainelGestorVO().getMediaInadimplenciaNoPeriodo());
				getSuperParametroRelVO().setPeriodo(Uteis.periodoEntreDatas(getDataInicio(), getDataFinal(), "MM/yyyy"));
			}
			caminho = ("relatorio" + File.separator + "designRelatorio" + File.separator + "painelGestor");
			getSuperParametroRelVO().setNomeDesignIreport(design);
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(caminho);
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio(titulo);

			getSuperParametroRelVO().setCaminhoBaseRelatorio(caminho);
			getSuperParametroRelVO().setNomeEmpresa("");
			getSuperParametroRelVO().setVersaoSoftware("");
			getSuperParametroRelVO().setFiltros("");
			
			adicionarLogoUnidadeEnsinoRelatorio(getUnidadeEnsinoVOs(), getFiltrarUnidadeEnsino(), getSuperParametroRelVO());
			adicionarUnidadeEnsinoRelatorio(getUnidadeEnsinoVOs(), getFiltrarUnidadeEnsino(), getSuperParametroRelVO());

			realizarImpressaoRelatorio();
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			caminho = null;
			design = null;
			titulo = null;
		}
	}

	public void imprimirReceitas() {
		String caminho = "";
		String design = "";
		String titulo = "";
		setSuperParametroRelVO(new SuperParametroRelVO());
		try {
			if (getTelaOrigem() != null && getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO_POR_NIVEL_EDUCACIONAL)) {
				titulo = "Receitas por Curso (Competência): " + getCursoVO().getNome();
				design = ("relatorio" + File.separator + "designRelatorio" + File.separator + "painelGestor" + File.separator + "PainelGestorFinanceiroReceitaTurmaCompetencia.jrxml");

				getSuperParametroRelVO().setListaObjetos(getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getPainelGestorContaReceberMesAnoTurmaVOs());
				getSuperParametroRelVO().adicionarParametro("totalReceitaDoMes", getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getTotalReceitaDoMes());
				getSuperParametroRelVO().adicionarParametro("totalAcrescimoDoMes", getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getTotalAcrescimoDoMes());
				getSuperParametroRelVO().adicionarParametro("totalDescontoDoMes", getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getTotalDescontoDoMes());
				getSuperParametroRelVO().adicionarParametro("totalReceitaComDescontoAcrescimoDoMes", getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getTotalReceitaComDescontoAcrescimoDoMes());
				getSuperParametroRelVO().adicionarParametro("totalRecebidoDoMes", getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getTotalRecebidoDoMes());
				getSuperParametroRelVO().adicionarParametro("totalSaldoAReceberDoMes", getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getTotalSaldoAReceberDoMes());
				getSuperParametroRelVO().adicionarParametro("mediaInadimplenciaDoPeriodo", getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getMediaInadimplenciaDoPeriodo());
				getSuperParametroRelVO().setPeriodo(Uteis.periodoEntreDatas(getDataInicioMesAno(), getDataFinalMesAno(), "MM/yyyy"));

			} else if (getTelaOrigem() != null && getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO)) {
				titulo = "Receitas por Nível (Competência): " + getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getTipoNivelEducacional().getDescricao();
				design = ("relatorio" + File.separator + "designRelatorio" + File.separator + "painelGestor" + File.separator + "PainelGestorFinanceiroReceitaCursoCompetencia.jrxml");
				getSuperParametroRelVO().setListaObjetos(getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getPainelGestorContaReceberMesAnoVOs());
				getSuperParametroRelVO().adicionarParametro("totalReceitaDoMes", getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getTotalReceitaDoMes());
				getSuperParametroRelVO().adicionarParametro("totalAcrescimoDoMes", getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getTotalAcrescimoDoMes());
				getSuperParametroRelVO().adicionarParametro("totalDescontoDoMes", getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getTotalDescontoDoMes());
				getSuperParametroRelVO().adicionarParametro("totalReceitaComDescontoAcrescimoDoMes", getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getTotalReceitaComDescontoAcrescimoDoMes());
				getSuperParametroRelVO().adicionarParametro("totalRecebidoDoMes", getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getTotalRecebidoDoMes());
				getSuperParametroRelVO().adicionarParametro("totalSaldoAReceberDoMes", getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getTotalSaldoAReceberDoMes());
				getSuperParametroRelVO().adicionarParametro("mediaInadimplenciaDoPeriodo", getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getMediaInadimplenciaDoPeriodo());
				getSuperParametroRelVO().setPeriodo(Uteis.periodoEntreDatas(getDataInicioMesAno(), getDataFinalMesAno(), "MM/yyyy"));
			} else {

				titulo = "Receitas por Competência";
				design = ("relatorio" + File.separator + "designRelatorio" + File.separator + "painelGestor" + File.separator + "PainelGestorFinanceiroReceitaMesAMes.jrxml");

				getSuperParametroRelVO().setListaObjetos(getPainelGestorVO().getPainelGestorContaReceberMesAnoVOs());
				getSuperParametroRelVO().adicionarParametro("totalReceitaDoMes", getPainelGestorVO().getTotalReceitaDoMes());
				getSuperParametroRelVO().adicionarParametro("totalAcrescimoDoMes", getPainelGestorVO().getTotalAcrescimoDoMes());
				getSuperParametroRelVO().adicionarParametro("totalDescontoDoMes", getPainelGestorVO().getTotalDescontoDoMes());
				getSuperParametroRelVO().adicionarParametro("totalReceitaComDescontoAcrescimoDoMes", getPainelGestorVO().getTotalReceitaComDescontoAcrescimoDoMes());
				getSuperParametroRelVO().adicionarParametro("totalRecebidoDoMes", getPainelGestorVO().getTotalRecebidoDoMes());
				getSuperParametroRelVO().adicionarParametro("totalSaldoAReceberDoMes", getPainelGestorVO().getTotalSaldoAReceberDoMes());
				getSuperParametroRelVO().adicionarParametro("mediaInadimplenciaDoPeriodo", getPainelGestorVO().getMediaInadimplenciaDoPeriodo());
				getSuperParametroRelVO().setPeriodo(Uteis.periodoEntreDatas(getDataInicio(), getDataFinal(), "MM/yyyy"));
			}
			caminho = ("relatorio" + File.separator + "designRelatorio" + File.separator + "painelGestor");
			getSuperParametroRelVO().setNomeDesignIreport(design);
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(caminho);
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio(titulo);

			getSuperParametroRelVO().setCaminhoBaseRelatorio(caminho);
			getSuperParametroRelVO().setNomeEmpresa("");
			getSuperParametroRelVO().setVersaoSoftware("");
			getSuperParametroRelVO().setFiltros("");
			
			adicionarLogoUnidadeEnsinoRelatorio(getUnidadeEnsinoVOs(), getFiltrarUnidadeEnsino(), getSuperParametroRelVO());
			adicionarUnidadeEnsinoRelatorio(getUnidadeEnsinoVOs(), getFiltrarUnidadeEnsino(), getSuperParametroRelVO());

			realizarImpressaoRelatorio();
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			caminho = null;
			design = null;
			titulo = null;
		}
	}

	public void imprimirAcademico() {

	}

	public void imprimirAcademicoPorCurso() {

	}

	public void imprimirAcademicoPorTurma() {

	}

	public void imprimirDetalhamentoContaReceberDescontoInstituicao() {
		String caminho = "";
		String design = "";
		String titulo = "";
		try {
			titulo = "Painel Gestor - Detalhamento Desconto de Instituição";
			design = ("relatorio" + File.separator + "designRelatorio" + File.separator + "painelGestor" + File.separator + "PainelGestorFinanceiroDetalhamentoDesconto.jrxml");
			caminho = ("relatorio" + File.separator + "designRelatorio" + File.separator + "painelGestor" + File.separator);
			setSuperParametroRelVO(new SuperParametroRelVO());
			getSuperParametroRelVO().setNomeDesignIreport(design);
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(caminho);
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());

			getSuperParametroRelVO().setTituloRelatorio(titulo);
			getSuperParametroRelVO().setListaObjetos(getPainelGestorVO().getPainelGestorDetalhamentoDescontoVOs());
			getSuperParametroRelVO().setCaminhoBaseRelatorio(caminho);
			getSuperParametroRelVO().setNomeEmpresa("");
			getSuperParametroRelVO().setVersaoSoftware("");
			getSuperParametroRelVO().setFiltros("");
			getSuperParametroRelVO().setPeriodo(Uteis.periodoEntreDatas(getDataInicio(), getDataFinal(), "MM/yyyy"));

			getSuperParametroRelVO().adicionarParametro("totalDescontoDetalhamento", getPainelGestorVO().getTotalDescontoDetalhamento());
			getSuperParametroRelVO().adicionarParametro("totalNumeroAlunoComDesconto", getPainelGestorVO().getTotalNumeroAlunoComDesconto());

			adicionarLogoUnidadeEnsinoRelatorio(getUnidadeEnsinoVOs(), getFiltrarUnidadeEnsino(), getSuperParametroRelVO());
			adicionarUnidadeEnsinoRelatorio(getUnidadeEnsinoVOs(), getFiltrarUnidadeEnsino(), getSuperParametroRelVO());
			realizarImpressaoRelatorio();
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			caminho = null;
			design = null;
			titulo = null;
		}
	}

	public void imprimirDespesas() {
		String caminho = "";
		String design = "";
		String titulo = "";
		try {
			titulo = "Painel Gestor - Mapa de Despesas";
			design = ("relatorio" + File.separator + "designRelatorio" + File.separator + "painelGestor" + File.separator + "PainelGestorFinanceiroDespesaMesAMes.jrxml");
			caminho = ("relatorio" + File.separator + "designRelatorio" + File.separator + "painelGestor");
			setSuperParametroRelVO(new SuperParametroRelVO());
			getSuperParametroRelVO().setNomeDesignIreport(design);
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(caminho);
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio(titulo);
			getSuperParametroRelVO().setListaObjetos(getPainelGestorVO().getPainelGestorContaPagarMesAnoVOs());
			getSuperParametroRelVO().setCaminhoBaseRelatorio(caminho);
			getSuperParametroRelVO().setNomeEmpresa("");
			getSuperParametroRelVO().setVersaoSoftware("");
			getSuperParametroRelVO().setFiltros("");
			getSuperParametroRelVO().setPeriodo(Uteis.periodoEntreDatas(getDataInicio(), getDataFinal(), "MM/yyyy"));
			adicionarLogoUnidadeEnsinoRelatorio(getUnidadeEnsinoVOs(), getFiltrarUnidadeEnsino(), getSuperParametroRelVO());
			adicionarUnidadeEnsinoRelatorio(getUnidadeEnsinoVOs(), getFiltrarUnidadeEnsino(), getSuperParametroRelVO());			
			getSuperParametroRelVO().adicionarParametro("totalProvisaoContaPagarPeriodo", getPainelGestorVO().getTotalProvisaoContaPagarPeriodo());
			getSuperParametroRelVO().adicionarParametro("totalPagoDoPeriodo", getPainelGestorVO().getTotalPagoDoPeriodo());
			getSuperParametroRelVO().adicionarParametro("saldoPagarDoPeriodo", getPainelGestorVO().getSaldoPagarDoPeriodo());
			getSuperParametroRelVO().adicionarParametro("totalPagoNoPeriodo", getPainelGestorVO().getTotalPagoNoPeriodo());
			getSuperParametroRelVO().adicionarParametro("mediaInadimplenciaDespesaDoPeriodo", getPainelGestorVO().getMediaInadimplenciaDespesaDoPeriodo());
			getSuperParametroRelVO().adicionarParametro("totalPagarAnterior", "Total a Pagar Anterior ao dia " + Uteis.getData(getDataInicio()) + ": R$ " + Uteis.getDoubleFormatado(getPainelGestorVO().getTotalPagarAnteriorPeriodo()));

			realizarImpressaoRelatorio();
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			caminho = null;
			design = null;
			titulo = null;
		}
	}

	public List<SelectItem> getListaSelectItemAcaoAlteracaoConsultor() {
		if (listaSelectItemAcaoAlteracaoConsultor == null) {
			listaSelectItemAcaoAlteracaoConsultor = new ArrayList<SelectItem>(0);
			listaSelectItemAcaoAlteracaoConsultor.add(new SelectItem("EXCLUIR", "Excluir Compromisso"));
			listaSelectItemAcaoAlteracaoConsultor.add(new SelectItem("ALTERAR", "Alterar Para o Novo Consultor"));
			listaSelectItemAcaoAlteracaoConsultor.add(new SelectItem("MANTER", "Manter Compromisso"));
		}
		return listaSelectItemAcaoAlteracaoConsultor;
	}

	public void setListaSelectItemAcaoAlteracaoConsultor(List<SelectItem> listaSelectItemAcaoAlteracaoConsultor) {
		this.listaSelectItemAcaoAlteracaoConsultor = listaSelectItemAcaoAlteracaoConsultor;
	}

	public ProspectsVO getProspectsVO() {
		if (prospectsVO == null) {
			prospectsVO = new ProspectsVO();
		}
		return prospectsVO;
	}

	public void setProspectsVO(ProspectsVO prospectsVO) {
		this.prospectsVO = prospectsVO;
	}

	public void emitirRelatorioListaProspectPreInscricaoPdf() {
		try {
			emitirRelatorioListaProspectPreInscricao(TipoRelatorioEnum.PDF, getPainelGestorMonitoramentoCRM().getProspectsVOs(), getPainelGestorMonitoramentoCRM().getTituloVisualizacaoMonitoramentoProspectPreInscricao());
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {

		}
	}

	public void emitirRelatorioListaProspectPreInscricaoExcel() {
		try {

			emitirRelatorioListaProspectPreInscricao(TipoRelatorioEnum.EXCEL, getPainelGestorMonitoramentoCRM().getProspectsVOs(), getPainelGestorMonitoramentoCRM().getTituloVisualizacaoMonitoramentoProspectPreInscricao());
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {

		}
	}

	public void emitirRelatorioListaProspectPreInscricao(TipoRelatorioEnum tipoRelatorioEnum, List<ProspectsVO> prospectsVOs, String titulo) throws Exception {
		String caminho = "";
		String design = "";
		try {
			if (!prospectsVOs.isEmpty()) {
				setSuperParametroRelVO(new SuperParametroRelVO());
				design = ("relatorio" + File.separator + "designRelatorio" + File.separator + "painelGestor" + File.separator + "PainelGestorProspectPreInscricao.jrxml");
				getSuperParametroRelVO().setListaObjetos(prospectsVOs);
				caminho = ("relatorio" + File.separator + "designRelatorio" + File.separator + "painelGestor");
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(tipoRelatorioEnum);
				getSuperParametroRelVO().setSubReport_Dir(caminho);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(caminho);
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware("");
				getSuperParametroRelVO().setFiltros("");
				getSuperParametroRelVO().setPeriodo(Uteis.periodoEntreDatas(getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().getDataInicio(), getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().getDataTermino(), "dd/MM/yyyy"));
				adicionarLogoUnidadeEnsinoRelatorio(getUnidadeEnsinoVOs(), getFiltrarUnidadeEnsino(), getSuperParametroRelVO());
				adicionarUnidadeEnsinoRelatorio(getUnidadeEnsinoVOs(), getFiltrarUnidadeEnsino(), getSuperParametroRelVO());
				realizarImpressaoRelatorio();
			}
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			throw e;
		} finally {
			caminho = null;
			design = null;

		}
	}

	public void emitirRelatorioListaProspectPdf() {
		try {
			List<ProspectsVO> prospectsVOs = getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().consultarDadosProspectMonitoramento(getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), getPainelGestorMonitoramentoCRM().getCurso().getCodigo(), getPainelGestorMonitoramentoCRM().getConsultor().getCodigo(), getPainelGestorMonitoramentoCRM().getUnidadeEnsino().getCodigo(), getPainelGestorMonitoramentoCRM().getTipoFiltroMonitamentoCrmProspectEnum(), getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().getMesAnoEnum(), getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().getAno(), getPainelGestorMonitoramentoCRM().getPeriodoUltimoContato1(), getPainelGestorMonitoramentoCRM().getPeriodoUltimoContato2(), getPainelGestorMonitoramentoCRM().getPeriodoUltimoContato3(), getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum(), getPainelGestorMonitoramentoCRM().getFiltrarMatriculado(), 0, 0, 0);
			emitirRelatorioListaProspect(TipoRelatorioEnum.PDF, prospectsVOs, getPainelGestorMonitoramentoCRM().getTituloVisualizacaoMonitoramentoProspect());
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {

		}
	}

	public void emitirRelatorioListaProspect(TipoRelatorioEnum tipoRelatorioEnum, List<ProspectsVO> prospectsVOs, String titulo) throws Exception {
		String caminho = "";
		String design = "";
		try {
			if (!prospectsVOs.isEmpty()) {
				setSuperParametroRelVO(new SuperParametroRelVO());
				design = ("relatorio" + File.separator + "designRelatorio" + File.separator + "painelGestor" + File.separator + "PainelGestorProspect.jrxml");
				getSuperParametroRelVO().setListaObjetos(prospectsVOs);
				caminho = ("relatorio" + File.separator + "designRelatorio" + File.separator + "painelGestor");
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(tipoRelatorioEnum);
				getSuperParametroRelVO().setSubReport_Dir(caminho);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(caminho);
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware("");
				getSuperParametroRelVO().setFiltros("");
				getSuperParametroRelVO().setPeriodo(Uteis.periodoEntreDatas(getDataInicio(), getDataFinal(), "MM/yyyy"));
				adicionarLogoUnidadeEnsinoRelatorio(getUnidadeEnsinoVOs(), getFiltrarUnidadeEnsino(), getSuperParametroRelVO());
				adicionarUnidadeEnsinoRelatorio(getUnidadeEnsinoVOs(), getFiltrarUnidadeEnsino(), getSuperParametroRelVO());
				realizarImpressaoRelatorio();
			}
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			throw e;
		} finally {
			caminho = null;
			design = null;

		}
	}

	public void emitirRelatorioListaProspectExcel() {
		try {
			List<ProspectsVO> prospectsVOs = getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().consultarDadosProspectMonitoramento(getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), getPainelGestorMonitoramentoCRM().getCurso().getCodigo(), getPainelGestorMonitoramentoCRM().getConsultor().getCodigo(), getPainelGestorMonitoramentoCRM().getUnidadeEnsino().getCodigo(), getPainelGestorMonitoramentoCRM().getTipoFiltroMonitamentoCrmProspectEnum(), getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().getMesAnoEnum(), getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().getAno(), getPainelGestorMonitoramentoCRM().getPeriodoUltimoContato1(), getPainelGestorMonitoramentoCRM().getPeriodoUltimoContato2(), getPainelGestorMonitoramentoCRM().getPeriodoUltimoContato3(), getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum(), getPainelGestorMonitoramentoCRM().getFiltrarMatriculado(), 0, 0, 0);
			emitirRelatorioListaProspect(TipoRelatorioEnum.EXCEL, prospectsVOs, getPainelGestorMonitoramentoCRM().getTituloVisualizacaoMonitoramentoProspect());
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {

		}
	}

	/**
	 * @return the dataAtual
	 */
	public String getDataAtual() {
		if (dataAtual == null) {
			dataAtual = Uteis.getDataAtual();
		}
		return dataAtual;
	}

	/**
	 * @param dataAtual
	 *            the dataAtual to set
	 */
	public void setDataAtual(String dataAtual) {
		this.dataAtual = dataAtual;
	}

	public PainelGestorContaReceberMesAnoVO getPainelGestorContaReceberMesAnoVO() {
		if (painelGestorContaReceberMesAnoVO == null) {
			painelGestorContaReceberMesAnoVO = new PainelGestorContaReceberMesAnoVO();
		}
		return painelGestorContaReceberMesAnoVO;
	}

	public void setPainelGestorContaReceberMesAnoVO(PainelGestorContaReceberMesAnoVO painelGestorContaReceberMesAnoVO) {
		this.painelGestorContaReceberMesAnoVO = painelGestorContaReceberMesAnoVO;
	}

	public String getTipoOrigem() {
		if (tipoOrigem == null) {
			tipoOrigem = "";
		}
		return tipoOrigem;
	}

	public void setTipoOrigem(String tipoOrigem) {
		this.tipoOrigem = tipoOrigem;
	}

	public String getTituloMapaReceitaTipoOrigem() {
		if (tituloMapaReceitaTipoOrigem == null) {
			tituloMapaReceitaTipoOrigem = "Mapa de Receitas";
		}
		return tituloMapaReceitaTipoOrigem;
	}

	public void setTituloMapaReceitaTipoOrigem(String tituloMapaReceitaTipoOrigem) {
		this.tituloMapaReceitaTipoOrigem = tituloMapaReceitaTipoOrigem;
	}

	public Date getDataInicioTemporaria() {
		if (dataInicioTemporaria == null) {
			dataInicioTemporaria = getDataInicio();
		}
		return dataInicioTemporaria;
	}

	public void setDataInicioTemporaria(Date dataInicioTemporaria) {
		this.dataInicioTemporaria = dataInicioTemporaria;
	}

	public Date getDataFinalTemporaria() {
		if (dataFinalTemporaria == null) {
			dataFinalTemporaria = getDataFinal();
		}
		return dataFinalTemporaria;
	}

	public void setDataFinalTemporaria(Date dataFinalTemporaria) {
		this.dataFinalTemporaria = dataFinalTemporaria;
	}

	public String getSituacaoContaReceberMesAno() {
		if (situacaoContaReceberMesAno == null) {
			situacaoContaReceberMesAno = "";
		}
		return situacaoContaReceberMesAno;
	}

	public void setSituacaoContaReceberMesAno(String situacaoContaReceberMesAno) {
		this.situacaoContaReceberMesAno = situacaoContaReceberMesAno;
	}

	public String getTipoMapaReceita() {
		if (tipoMapaReceita == null) {
			tipoMapaReceita = "";
		}
		return tipoMapaReceita;
	}

	public void setTipoMapaReceita(String tipoMapaReceita) {
		this.tipoMapaReceita = tipoMapaReceita;
	}

	public String getTipoDesconto() {
		if (tipoDesconto == null) {
			tipoDesconto = "";
		}
		return tipoDesconto;
	}

	public void setTipoDesconto(String tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}

	public String getTituloDetalhamentoDesconto() {
		if (tituloDetalhamentoDesconto == null) {
			tituloDetalhamentoDesconto = "";
		}
		return tituloDetalhamentoDesconto;
	}

	public void setTituloDetalhamentoDesconto(String tituloDetalhamentoDesconto) {
		this.tituloDetalhamentoDesconto = tituloDetalhamentoDesconto;
	}

	// PAINEL GESTOR FINANCEIRO

	/**
	 * Mï¿½todo responsï¿½vel por inicializar os dados financeiros do painel
	 * gestor, incluindo grï¿½ficos.
	 * 
	 * @return
	 */
	public String executarInicializacaoDadoFinanceiroPainelGestor() {
		try {
			inicializarDadosDashboardMonitorFinanceiro();
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadoFinanceiroPainelGestor(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), getConfiguracaoFinanceiroPadraoSistema(), getFiltarDataReceitaPor());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorFinanceiroForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}
	
	public void inicializarDadosDashboardMonitorFinanceiro() {
		if(!getMapDashboards().containsKey(TipoDashboardEnum.BI_RECEITA_DESPESA_CONSOLIDADO.name())) {
			getMapDashboards().put(TipoDashboardEnum.BI_RECEITA_DESPESA_CONSOLIDADO.name(), new DashboardVO(TipoDashboardEnum.BI_RECEITA_DESPESA_CONSOLIDADO, false, 1, TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.SEI_DECIDIR,  getUsuarioLogado()));
		}
		if(!getMapDashboards().containsKey(TipoDashboardEnum.BI_RECEITA_POR_COMPETENCIA.name())) {
			getMapDashboards().put(TipoDashboardEnum.BI_RECEITA_POR_COMPETENCIA.name(), new DashboardVO(TipoDashboardEnum.BI_RECEITA_POR_COMPETENCIA, false, 2, TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.SEI_DECIDIR,  getUsuarioLogado()));
		}
		if(!getMapDashboards().containsKey(TipoDashboardEnum.BI_RECEITA_POR_FLUXO_CAIXA.name())) {
			getMapDashboards().put(TipoDashboardEnum.BI_RECEITA_POR_FLUXO_CAIXA.name(), new DashboardVO(TipoDashboardEnum.BI_RECEITA_POR_FLUXO_CAIXA, false, 3, TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.SEI_DECIDIR,  getUsuarioLogado()));
		}
		if(!getMapDashboards().containsKey(TipoDashboardEnum.BI_MAPA_DESPESAS.name())) {
			getMapDashboards().put(TipoDashboardEnum.BI_MAPA_DESPESAS.name(), new DashboardVO(TipoDashboardEnum.BI_MAPA_DESPESAS, false, 4, TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.SEI_DECIDIR,  getUsuarioLogado()));
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por inicializar os dados financeiros do painel
	 * gestor, incluindo grï¿½ficos.
	 * 
	 * @return
	 */
	public void executarInicializacaoDadoFinanceiroPainelGestorCompetencia() {
		try {
			getFacadeFactory().getPainelGestorFacade().consultarMapaReceitaPorCompetenciaPainelGestorFinanceiroPorPeriodo(getPainelGestorVO(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), getFiltarDataReceitaPor());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por realizar o detalhamento das contas a receber
	 * 
	 * @return
	 */
	public String executarDetalhamentoReceitaDoMesPorMesAnoETipoOrigem() {
		try {
			PainelGestorContaReceberMesAnoVO obj = (PainelGestorContaReceberMesAnoVO) context().getExternalContext().getRequestMap().get("contaReceberMesAnoItens");
			if (obj.getMesAno().equals("")) {
				obj.setMesAno(getMesAno());
			}
			getFacadeFactory().getPainelGestorFacade().executarDetalhamentoReceitaDoMesPorMesAnoETipoOrigemSituacaoTipoMapareceitaTipoDesconto(getPainelGestorVO(), obj, getTipoOrigem(), getSituacaoContaReceberMesAno(), getTipoMapaReceita(), getTipoDesconto(), getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), getConfiguracaoFinanceiroPadraoSistema(), obj.getCodigoCurso(), obj.getCodigoTurma(), getFiltarDataReceitaPor());
			setPainelGestorContaReceberMesAnoVO(obj);
			inicializarDadosDataTemporaria();
			inicializarTituloMapaReceita();
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorFinanceiroDetalhamentoContaReceberForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public void inicializarTituloMapaReceita() {
		setTituloMapaReceitaTipoOrigem("");
		setTituloDetalhamentoDesconto("");
		if (!getTipoOrigem().equals("")) {
			setTituloMapaReceitaTipoOrigem("Mapa de Receitas" + " - " + TipoOrigemContaReceber.getDescricao(getTipoOrigem()) + " - Período de " + getPainelGestorContaReceberMesAnoVO().getMesAnoApresentar());
		} else {
			if (getTipoMapaReceita().equals("COMPETENCIA")) {
				setTituloMapaReceitaTipoOrigem("Mapa de Receitas Por Competência" + " - Período de " + getPainelGestorContaReceberMesAnoVO().getMesAnoApresentar());
			} else {
				setTituloMapaReceitaTipoOrigem("Mapa de Receitas Por Fluxo de Caixa" + " - Período de " + getPainelGestorContaReceberMesAnoVO().getMesAnoApresentar());
			}
		}
		if (getTipoDesconto().equals("CONVENIO")) {
			setTituloDetalhamentoDesconto("Desconto de Convênio" + "  -  Período de " + getPainelGestorContaReceberMesAnoVO().getMesAnoApresentar());
		} else if (getTipoDesconto().equals("INSTITUICAO")) {
			setTituloDetalhamentoDesconto("Desconto de Instituição" + " -  Período de " + getPainelGestorContaReceberMesAnoVO().getMesAnoApresentar());
		} else if (getTipoDesconto().equals("PROGRESSIVO")) {
			setTituloDetalhamentoDesconto("Desconto Progressivo" + "  -  Período de " + getPainelGestorContaReceberMesAnoVO().getMesAnoApresentar());
		}
	}

	public void selecionalObjPainelGestorContaReceberMesAnoVO() throws Exception {
		PainelGestorContaReceberMesAnoVO painelGestorContaReceberMesAnoVO = (PainelGestorContaReceberMesAnoVO) UteisJSF.context().getExternalContext().getRequestMap().get("contaReceberMesAnoItens");
		setMesAno(painelGestorContaReceberMesAnoVO.getMesAno());
		inicializarDataMesAno();
		setTelaOrigem(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO);
		inicializarDadosListaNivelEducacional();
	}

	/**
	 * Mï¿½todo responsï¿½vel por consultar os dados do curso por nï¿½vel
	 * educacional
	 * 
	 * @return
	 */
	public String executarGeracaoDadosPainelGestorFinanceiroPorNivelEducacional() {
		try {
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosMapaReceitaPorNivelEducacional(getPainelGestorVO(), "COMPETENCIA", getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getTipoNivelEducacional_Enum(), getMesAno(), getConfiguracaoFinanceiroPadraoSistema(), getFiltarDataReceitaPor());
			inicializarDadosDataTemporaria();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			setTituloMapaReceitaPorCurso("Mapa de Receitas Por Nível Educacional - " + getTipoNivelEducacional_Enum().getDescricao() + " - " + getMesAno());
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorFinanceiroPorNivelEducacionalForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getPainelGestorContaReceberMesAnoVOs().clear();
			return "";
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por consultar os dados do curso por nï¿½vel
	 * educacional
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String executarGeracaoDadosPainelGestorFinanceiroPorNivelEducacionalFluxoCaixa() {
		try {
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosMapaReceitaPorNivelEducacional(getPainelGestorVO(), "FLUXO_CAIXA", getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getTipoNivelEducacional_Enum(), getMesAno(), getConfiguracaoFinanceiroPadraoSistema(), getFiltarDataReceitaPor());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			inicializarDadosDataTemporaria();
			setTituloMapaReceitaPorCurso("Mapa de Receitas Por Nível Educacional - " + getTipoNivelEducacional_Enum().getDescricao() + " - " + getMesAno());
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorFinanceiroPorNivelEducacionalFluxoCaixaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			getPainelGestorVO().getPainelGestorPorNivelEducacionalVO().getPainelGestorContaReceberMesAnoVOs().clear();
			return "";
		}
	}

	public String selecionarDadosGeracaoDadosPainelGestorFinanceiroPorTurma() {
		try {
			PainelGestorContaReceberMesAnoVO obj = (PainelGestorContaReceberMesAnoVO) context().getExternalContext().getRequestMap().get("contaReceberMesAnoItens");
			if (obj != null) {
				getCursoVO().setCodigo(obj.getCodigoCurso());
				getCursoVO().setNome(obj.getCurso());
			}
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosMapaReceitaPorTurma(getPainelGestorVO(), "COMPETENCIA", getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getTipoNivelEducacional_Enum(), getMesAno(), getConfiguracaoFinanceiroPadraoSistema(), getCursoVO().getCodigo(), getFiltarDataReceitaPor());
			setTituloMapaReceitaPorTurma("Mapa de Receitas Por Turma - " + getCursoVO().getNome() + " - " + getMesAno());
			setTelaOrigem(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO_POR_NIVEL_EDUCACIONAL);
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorFinanceiroPorTurmaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String selecionarDadosGeracaoDadosPainelGestorFinanceiroPorTurmaFluxoCaixa() {
		try {
			PainelGestorContaReceberMesAnoVO obj = (PainelGestorContaReceberMesAnoVO) context().getExternalContext().getRequestMap().get("contaReceberMesAnoItens");
			getCursoVO().setCodigo(obj.getCodigoCurso());
			getCursoVO().setNome(obj.getCurso());
			getFacadeFactory().getPainelGestorFacade().executarInicializacaoDadosMapaReceitaPorTurma(getPainelGestorVO(), "FLUXO_CAIXA", getUnidadeEnsinoVOs(), getDataInicioMesAno(), getDataFinalMesAno(), getTipoNivelEducacional_Enum(), getMesAno(), getConfiguracaoFinanceiroPadraoSistema(), obj.getCodigoCurso(), getFiltarDataReceitaPor());
			setTituloMapaReceitaPorTurma("Mapa de Receitas Por Turma - " + obj.getCurso() + " - " + getMesAno());
			setTelaOrigem(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO_POR_NIVEL_EDUCACIONAL_FLUXO_CAIXA);
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorFinanceiroPorTurmaFluxoCaixaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public void inicializarDadosDataTemporaria() {
		if (getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO)) {
			setDataInicioTemporaria(getDataInicio());
			setDataFinalTemporaria(getDataFinal());
		}
	}

	public String getTituloMapaReceitaPorTurma() {
		if (tituloMapaReceitaPorTurma == null) {
			tituloMapaReceitaPorTurma = "";
		}
		return tituloMapaReceitaPorTurma;
	}

	public void setTituloMapaReceitaPorTurma(String tituloMapaReceitaPorTurma) {
		this.tituloMapaReceitaPorTurma = tituloMapaReceitaPorTurma;
	}

	public String getTituloMapaReceitaPorCurso() {
		if (tituloMapaReceitaPorCurso == null) {
			tituloMapaReceitaPorCurso = "";
		}
		return tituloMapaReceitaPorCurso;
	}

	public void setTituloMapaReceitaPorCurso(String tituloMapaReceitaPorCurso) {
		this.tituloMapaReceitaPorCurso = tituloMapaReceitaPorCurso;
	}

	public List<SelectItem> getTipoConsultaComboSituacaoMatricula() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", "Todos"));
		itens.add(new SelectItem("AT", "Ativas"));
		itens.add(new SelectItem("PR", "Pré-Matrícula"));
		itens.add(new SelectItem("CA", "Cancelada"));
		itens.add(new SelectItem("EX", "Excluída"));
		itens.add(new SelectItem("TD", "Transferência de"));
		itens.add(new SelectItem("TP", "Transferência para"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboSituacaoProspect() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", "Todos"));
		itens.add(new SelectItem("AT", "Ativo"));
		itens.add(new SelectItem("IN", "Inativo"));
		return itens;
	}

	public FuncionarioVO getConsultor() {
		if (consultor == null) {
			consultor = new FuncionarioVO();
		}
		return consultor;
	}

	public void setConsultor(FuncionarioVO consultor) {
		this.consultor = consultor;
	}

	public String getSituacaoMatricula() {
		if (situacaoMatricula == null) {
			situacaoMatricula = "";
		}
		return situacaoMatricula;
	}

	public void setSituacaoMatricula(String situacaoMatricula) {
		this.situacaoMatricula = situacaoMatricula;
	}

	public String getSituacaoProspectAtIn() {
		if (situacaoProspectAtIn == null) {
			situacaoProspectAtIn = "";
		}
		return situacaoProspectAtIn;
	}

	public void setSituacaoProspectAtIn(String situacaoProspectAtIn) {
		this.situacaoProspectAtIn = situacaoProspectAtIn;
	}

	public String getHtmlRelatorioConsultorPorMatricula() {
		if (htmlRelatorioConsultorPorMatricula == null) {
			htmlRelatorioConsultorPorMatricula = "";
		}
		return htmlRelatorioConsultorPorMatricula;
	}

	public void setHtmlRelatorioConsultorPorMatricula(String htmlRelatorioConsultorPorMatricula) {
		this.htmlRelatorioConsultorPorMatricula = htmlRelatorioConsultorPorMatricula;
	}

	public Boolean getMarcarTodasUnidadeEnsino() {
		if (marcarTodasUnidadeEnsino == null) {
			marcarTodasUnidadeEnsino = Boolean.FALSE;
		}
		return marcarTodasUnidadeEnsino;
	}

	public void setMarcarTodasUnidadeEnsino(Boolean marcarTodasUnidadeEnsino) {
		this.marcarTodasUnidadeEnsino = marcarTodasUnidadeEnsino;
	}

	public Date getDataCompetenciaInicial() {
//		if (dataCompetenciaInicial == null) {
//			dataInicio = Uteis.obterDataAntiga(new Date(), 60);
//			try {
//				dataCompetenciaInicial = Uteis.gerarDataDiaMesAno(1, Uteis.getMesData(dataCompetenciaInicial), Uteis.getAnoData(dataCompetenciaInicial));
//			} catch (Exception ex) {
//				dataCompetenciaInicial = Uteis.obterDataAntiga(new Date(), -60);
//			}
//		}
		return dataCompetenciaInicial;
	}

	public void setDataCompetenciaInicial(Date dataCompetenciaInicial) {
		this.dataCompetenciaInicial = dataCompetenciaInicial;
	}

	public Date getDataCompetenciaFinal() {
//		if (dataCompetenciaFinal == null) {
//			dataCompetenciaFinal = Uteis.obterDataFutura(new Date(), 60);
//			dataCompetenciaFinal = Uteis.getDataUltimoDiaMes(dataCompetenciaFinal);
//		}
		return dataCompetenciaFinal;
	}

	public void setDataCompetenciaFinal(Date dataCompetenciaFinal) {
		this.dataCompetenciaFinal = dataCompetenciaFinal;
	}

	public List<SelectItem> getListaSelectItemFiltrarDataReceitaPor() {
		if (listaSelectItemFiltrarDataReceitaPor == null) {
			listaSelectItemFiltrarDataReceitaPor = new ArrayList<SelectItem>(0);
			listaSelectItemFiltrarDataReceitaPor.add(new SelectItem("datacompetencia", UteisJSF.internacionalizar("prt_PainelGestor_dataCompetencia")));
			listaSelectItemFiltrarDataReceitaPor.add(new SelectItem("datavencimento", UteisJSF.internacionalizar("prt_PainelGestor_dataVencimento")));

		}
		return listaSelectItemFiltrarDataReceitaPor;
	}

	public void setListaSelectItemFiltrarDataReceitaPor(List<SelectItem> listaSelectItemFiltrarDataReceitaPor) {
		this.listaSelectItemFiltrarDataReceitaPor = listaSelectItemFiltrarDataReceitaPor;
	}

	public String getFiltarDataReceitaPor() {
		if (filtarDataReceitaPor == null) {
			filtarDataReceitaPor = "datacompetencia";
		}
		return filtarDataReceitaPor;
	}

	public void setFiltarDataReceitaPor(String filtarDataReceitaPor) {
		this.filtarDataReceitaPor = filtarDataReceitaPor;
	}

	public String getOpcaoSituacaoAcademica() {
		if (opcaoSituacaoAcademica == null) {
			opcaoSituacaoAcademica = "";
		}
		return opcaoSituacaoAcademica;
	}

	public void setOpcaoSituacaoAcademica(String opcaoSituacaoAcademica) {
		this.opcaoSituacaoAcademica = opcaoSituacaoAcademica;
	}

	public void executarInicializacaoTipoNivelEducacionalSelecionarUnidadeEnsino() {
		try {
			inicializarDadosListaNivelEducacional2();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * @author Victor Hugo de Paula Costa
	 */
	private List<SegmentacaoProspectVO> segmentacaoProspectVOs;
	private Integer codigoSegmentacao;
	private Boolean selecionarProspects;
	private ComunicacaoInternaVO comunicacaoInternaVO;
	private List<ProspectsVO> prospectsVOs;

	public Integer getCodigoSegmentacao() {
		if (codigoSegmentacao == null) {
			codigoSegmentacao = 0;
		}
		return codigoSegmentacao;
	}

	public void setCodigoSegmentacao(Integer codigoSegmentacao) {
		this.codigoSegmentacao = codigoSegmentacao;
	}

	public List<SegmentacaoProspectVO> getSegmentacaoProspectVOs() {
		if (segmentacaoProspectVOs == null) {
			segmentacaoProspectVOs = new ArrayList<SegmentacaoProspectVO>();
		}
		return segmentacaoProspectVOs;
	}

	public void setSegmentacaoProspectVOs(List<SegmentacaoProspectVO> segmentacaoProspectVOs) {
		this.segmentacaoProspectVOs = segmentacaoProspectVOs;
	}

	public String consultarDadosPainelGestorMonitoramentoPorSegmentacao() {
		try {
			if (!getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAGINA_INICIAL) && !getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO) && !getTelaOrigem().equals(ControlePaginaPainelGestorEnum.PAINEL_GESTOR_FINANCEIRO_ACADEMICO)) {
				if (getMesAno().trim().isEmpty()) {
					setDataInicio(getDataInicioMesAno());
					setDataFinal(getDataFinalMesAno());
				}
			}
			setSegmentacaoProspectVOs(getFacadeFactory().getSegmentacaoProspectFacade().consultarSegmentacaoProspect(getUnidadeEnsinoVOs(), getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum(), getPainelGestorMonitoramentoCRM().getUnidadeEnsino().getCodigo()));
			for (SegmentacaoProspectVO obj : getSegmentacaoProspectVOs()) {
				obj.calcularPercentualSegmentacaoOpcao();
			}
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			// return "painelGestorMonitoramentoSegmentacao";
			return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoSegmentacaoCRM.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String detalharSegmentacao(Integer codigo) {
		setCodigoSegmentacao(codigo);
		return detalharSegmentacao();
	}
	
	public String detalharSegmentacao() {
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(0);
		getPainelGestorMonitoramentoCRM().setPainelGestorTipoMonitoramentoCRMEnum(PainelGestorTipoMonitoramentoCRMEnum.SEGMENTACAO_PROSPECT);
		getPainelGestorMonitoramentoCRM().setTipoFiltroMonitamentoCrmProspectEnum(TipoFiltroMonitamentoCrmProspectEnum.SEGMENTACAO_PROSPECT);
		try {
			getPainelGestorMonitoramentoCRM().setSegmentacaoOpcaoVO(getFacadeFactory().getSegmentacaOpcaoFacade().consultarPorChavePrimaria(getCodigoSegmentacao()));
		} catch (Exception e) {
			getPainelGestorMonitoramentoCRM().setSegmentacaoOpcaoVO(null);
		}
		return consultarProspectsMonitoramento();
	}

	public Boolean getSelecionarProspects() {
		if (selecionarProspects == null) {
			selecionarProspects = false;
		}
		return selecionarProspects;
	}

	public void setSelecionarProspects(Boolean selecionarProspects) {
		this.selecionarProspects = selecionarProspects;
	}

	public ComunicacaoInternaVO getComunicacaoInternaVO() {
		if (comunicacaoInternaVO == null) {
			comunicacaoInternaVO = new ComunicacaoInternaVO();
		}
		return comunicacaoInternaVO;
	}

	public void setComunicacaoInternaVO(ComunicacaoInternaVO comunicacaoInternaVO) {
		this.comunicacaoInternaVO = comunicacaoInternaVO;
	}

	public List<ProspectsVO> getProspectsVOs() {
		if (prospectsVOs == null) {
			prospectsVOs = new ArrayList<ProspectsVO>();
		}
		return prospectsVOs;
	}

	public void setProspectsVOs(List<ProspectsVO> prospectsVOs) {
		this.prospectsVOs = prospectsVOs;
	}

	public void selecionarTodosProspects() {
		try {
			setProspectsVOs(getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().consultarDadosProspectMonitoramento(getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), getPainelGestorMonitoramentoCRM().getCurso().getCodigo(), getPainelGestorMonitoramentoCRM().getConsultor().getCodigo(), getPainelGestorMonitoramentoCRM().getUnidadeEnsino().getCodigo(), getPainelGestorMonitoramentoCRM().getTipoFiltroMonitamentoCrmProspectEnum(), getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().getMesAnoEnum(), getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().getAno(), getPainelGestorMonitoramentoCRM().getPeriodoUltimoContato1(), getPainelGestorMonitoramentoCRM().getPeriodoUltimoContato2(), getPainelGestorMonitoramentoCRM().getPeriodoUltimoContato3(), getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum(), getPainelGestorMonitoramentoCRM().getFiltrarMatriculado(), 0, 0, getCodigoSegmentacao()));
			for (ProspectsVO object : getProspectsVOs()) {
				object.setSelecionado(true);
			}
			setMensagemID(prospectsVOs.size() + " Prospects selecionados", Uteis.ALERTA);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void realizarEnvioEmail() {
		try {
			getFacadeFactory().getProspectsFacade().realizarEnvioEmail(getComunicacaoInternaVO(), getProspectsVOs(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setComunicacaoInternaVO(new ComunicacaoInternaVO());
			setMensagemID("msg_msg_enviados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarUpload(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getArquivoHelper().upLoadLista(uploadEvent, getComunicacaoInternaVO().getListaArquivosAnexo(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.ARQUIVO_TMP, false, false, getUsuarioLogado());
			setMensagemID("msg_sucesso_upload", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getMontarGraficoLegendaReceitaVOs(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
		if(pgc.getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().size() >= 1){
			return  pgc.getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().get(0).getLegenda().toString();
		}
		return "Receitas";
	}
	
	public String getMontarGraficoLegendaDespesaVOs(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
		if(pgc.getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().size() >= 2){
			return  pgc.getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().get(1).getLegenda().toString();
		}
		return "Despesas";
	}
	
	public String getMontarGraficoLegendaSaldoVOs(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
		if(pgc.getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().size() >= 3){
			return  pgc.getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().get(2).getLegenda().toString();
		}
		return "Saldo";
	}
	
	public String getMontarGraficoLegendaAcademicoFinanceiroAlunosAtivosVOs(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
		if(pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().size() >= 1){
			return  pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().get(0).getLegenda().toString();
		}
		return "Alunos Ativos";
	}
	
	public String getMontarGraficoLegendaAcademicoFinanceiroAlunosAtivosEstuadaramTodasDisciplinasVOs(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
		if(pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().size() >= 2){
			return  pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().get(1).getLegenda().toString();
		}
		return "Alunos Ativos Estudaram Todas as Disciplinas";
	}
	
	public String getMontarGraficoLegendaAcademicoFinanceiroAlunosNovosVOs(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
		if(pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().size() >= 3){
			return  pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().get(2).getLegenda().toString();
		}
		return "Alunos Novos/Renovados";
	}
	
	public String getMontarGraficoLegendaAcademicoFinanceiroEvasaoAlunosVOs(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
		if(pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().size() >= 4){
			return  pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().get(3).getLegenda().toString();
		}
		return "Evasão de Alunos";
	}
	
	public String getMontarGraficoLegendaAcademicoFinanceiroPreMatriculasVOs(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
		if(pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().size() >= 5){
			return  pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().get(4).getLegenda().toString();
		}
		return "Pré-Matrículas";
	}
		
	public String getMontarGraficoReceitaVisible(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
		if(pgc.getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().size() >= 1 && pgc.getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().get(0).getValor() > 0.0){
			return "true";
		}
		return "false";
	}
	
	public String getMontarGraficoDespesaVisible(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
		if(pgc.getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().size() >= 2 && pgc.getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().get(1).getValor() > 0.0){
			return "true";
		}
		return "false";
	}
	
	public String getMontarGraficoSaldoVisible(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
		if(pgc.getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().size() >= 3){
			return "true";
		}
		return "false";
	}
	
	public String getMontarGraficoAcademicoFinanceiroAlunosAtivosVisible(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
		if(pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().size() >= 1){
			return "true";
		}
		return "false";
	}
	
	public String getMontarGraficoAcademicoFinanceiroAlunosAtivosEstudaramTodasDisciplinasVisible(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
		if(pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().size() >= 2 && pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().get(1).getValor() > 0.0){
			return "true";
		}
		return "false";
	}
	
	public String getMontarGraficoAcademicoFinanceiroAlunosNovosVisible(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
		if(pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().size() >= 3 && pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().get(2).getValor() > 0.0){
			return "true";
		}
		return "false";
	}
	
	public String getMontarGraficoAcademicoFinanceiroEvasaoAlunosVisible(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
		if(pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().size() >= 4 && pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().get(3).getValor() > 0.0){
			return "true";
		}
		return "false";
	}
	
	public String getMontarGraficoAcademicoFinanceiroPreMatriculasVisible(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
		if(pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().size() >= 5 && pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().get(4).getValor() > 0.0){
			return "true";
		}
		return "false";
	}
	
	public String getMontarGraficoConsumoInicialVisible(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
		if (!pgc.getPainelGestorVO().getListaValoresConsumoInicial().isEmpty()){
			return "true";
		}
		return "false";
	}
	
	public String getMontarGraficoDespesaInicialVisible(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
		if (!pgc.getPainelGestorVO().getListaValoresCategoriaDespesaInicial().isEmpty()){
			return "true";
		}
		return "false";
	}
	
	public String getMontarGraficoCategoriaDespesaVisible(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
		if (!pgc.getPainelGestorVO().getListaValoresCategoriaDespesa().isEmpty()){
			return "true";
		}
		return "false";
	}
	
	public String getMontarGraficoMonitoramentoCRMProspectVisible(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
		if (!pgc.getPainelGestorMonitoramentoCRM().getDadosGraficoMonitoramentoProspect().equals("")){
			return "true";
		}
		return "false";
	}
	
	public String getMontarGraficoMonitoramentoCRMPreInscricaoVisible(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
		if (!pgc.getPainelGestorMonitoramentoCRM().getDadosGraficoPreInscricao().trim().equals("")){
			return "true";
		}
		return "false";
	}
	
	public String getMontarGraficoMonitoramentoProspectVisible(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
		if (!pgc.getPainelGestorMonitoramentoCRM().getDadosGraficoMonitoramentoProspect().equals("")){
			return "true";
		}
		return "false";
	}
	
	public String getMontarGraficoReceitaShowInLegend(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
			if(pgc.getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().get(0).getValor()> 0.0 && pgc.getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().size() >= 1){
				return "true";
			}
			return "false";
	}
	
	public String getMontarGraficoDespesaShowInLegend(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
			if(pgc.getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().get(1).getValor()> 0.0 && pgc.getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().size() >= 2){
				return "true";
			}
			return "false";
	}
	
	public String getMontarGraficoSaldoShowInLegend(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
			if(pgc.getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().size() >= 3){
				return "true";
			}
			return "false";
	}
	
	public String getMontarGraficoAcademicoFinanceiroAlunosAtivosShowInLegend(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
			if(pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().size() >= 1 && pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().get(0).getValor() > 0.0) {
				return "true";
			}
			return "false";
	}
	
	public String getMontarGraficoAcademicoFinanceiroAlunosAtivosEstudaramTodasDisciplinasShowInLegend(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
			if(pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().size() >= 2 && pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().get(1).getValor() > 0.0) {
				return "true";
			}
			return "false";
	}
	
	public String getMontarGraficoAcademicoFinanceiroAlunosNovosShowInLegend(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
			if(pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().size() >= 3 && pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().get(2).getValor() > 0.0) {
				return "true";
			}
			return "false";
	}
	
	public String getMontarGraficoAcademicoFinanceiroEvasaoAlunosShowInLegend(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
			if(pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().size() >= 4 && pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().get(3).getValor() > 0.0) {
				return "true";
			}
			return "false";
	}
	
	public String getMontarGraficoAcademicoFinanceiroPreMatriculasShowInLegend(){
		PainelGestorControle pgc = (PainelGestorControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PainelGestorControle");
			if(pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().size() >= 5 && pgc.getPainelGestorVO().getLegendaGraficoAcademicoFinanceiroVOs().get(4).getValor() > 0.0) {
				return "true";
			}
			return "false";
	}
	
	public String getMontarGraficoDataReceita(){
		return getPainelGestorVO().getListaValoresReceitas().toString();
	}
	
	public String getMontarGraficoDataDespesa(){
		return getPainelGestorVO().getListaValoresDespesas().toString();
	}
	
	public String getMontarGraficoDataSaldo(){
		return getPainelGestorVO().getListaValoresReceitaDespesasSaldo().toString();
	}
	
	public String getMontarDataGraficoAcademicoFinanceiroAlunosAtivos(){
		return getPainelGestorVO().getListaValoresAcademicoAtivo().toString();
	}
	
	public String getMontarDataGraficoAcademicoFinanceiroAlunosAtivosEstudaramTodasDisciplinas(){
		return getPainelGestorVO().getListaValoresAcademicoAptoFormar().toString();
	}
	
	public String getMontarDataGraficoAcademicoFinanceiroAlunosNovos(){
		return getPainelGestorVO().getListaValoresAcademicoNovo().toString();
	}
	
	public String getMontarDataGraficoAcademicoFinanceiroEvasaoAlunos(){
		return getPainelGestorVO().getListaValoresAcademicoEvasao().toString();
	}
	
	public String getMontarDataGraficoAcademicoFinanceiroPreMatriculas(){
		return getPainelGestorVO().getListaValoresAcademicoPreMatricula().toString();
	}
	
	public String getMontarDataGraficoConsumoInicial(){
		return getPainelGestorVO().getListaValoresConsumoInicial().toString();
	}
	
	public String getMontarDataGraficoDespesaInicial(){
		return getPainelGestorVO().getListaValoresCategoriaDespesaInicial().toString();
	}
	
	public String getMontarDataGraficoCategoriaDespesa(){
		return getPainelGestorVO().getListaValoresCategoriaDespesa().toString();
	}
	
	public String getMontarDataGraficoMonitoramentoAcademico(){
		return getFiltroPainelGestorAcademico().getResultadoGraficoConsultaMonitoramentoAcademico().toString();
	}
	
	public String getMontarDataGraficoGeralMonitoramentoAcademico(){
		return getFiltroPainelGestorAcademico().getPainelGestorMonitoramentoProcessoSeletivoVO().getGraficoGeral().toString();
	}
	
	public String getMontarDataGraficoMonitoramentoAcademicoAlunoTurno(){
		return getFiltroPainelGestorAcademico().getResultadoGraficoConsultaMonitoramentoAcademicoAlunoTurno().toString();
	}
	
	public String getMontarDataGraficoMonitoramentoAcademicoAlunoPorSexo(){
		return getFiltroPainelGestorAcademico().getResultadoGraficoConsultaMonitoramentoAcademicoSexo().toString();
	}
	
	public String getMontarDataGraficoMonitoramentoAcademicoNumeroTurmaPorTurno(){
		return getFiltroPainelGestorAcademico().getResultadoGraficoConsultaMonitoramentoAcademicoTurmaTurno().toString();
	}
	
	public String getMontarDataGraficoMonitoramentoAcademicoNumeroProfessoresPorTurno(){
		return getFiltroPainelGestorAcademico().getResultadoGraficoConsultaMonitoramentoAcademicoProfessorTurno().toString();
	}
	
	public String getMontarDataGraficoMonitoramentoAcademicoImpressaoDeclaracao(){
		return getFiltroPainelGestorAcademico().getResultadoGraficoConsultaMonitoramentoImpressaoDeclaracao().toString();
	}
	
	public String getMontarDataGraficoMonitoramentoAcademicoCalourosPorFormaIngresso(){
		return getFiltroPainelGestorAcademico().getResultadoGraficoMonitoramentoCalouroPorFormaIngresso().toString();
	}
	
	public String getMontarDataGraficoMonitoramentoAcademicoCalourosPreMatriculadosPorFormaIngresso(){
		return getFiltroPainelGestorAcademico().getResultadoGraficoMonitoramentoPreMatriculadoPorFormaIngresso().toString();
	}
	
	public String getMontarDataGraficoMonitoramentoAcademicoCancelamentoPorTipoJustificativa(){
		return getFiltroPainelGestorAcademico().getResultadoGraficoConsultaMonitoramentoCancelamento().toString();
	}
	
	public String getMontarDataGraficoGeralMonitoramentoProcessoSeletivo(){
		return getFiltroPainelGestorAcademico().getPainelGestorMonitoramentoProcessoSeletivoVO().getGraficoGeral().toString();
	}
	
	public String getMontarDataGraficoMonitoramentoCRMProspect(){
		return getPainelGestorMonitoramentoCRM().getDadosGraficoMonitoramentoProspect().toString();
	}
	
	public String getMontarDataGraficoMonitoramentoCRMPreInscricao(){
		return getPainelGestorMonitoramentoCRM().getDadosGraficoPreInscricao().toString();
	}
	
	public String getMontarDataGraficoMonitoramentoCRMComoficouSabendoInstituicao(){
		return getPainelGestorMonitoramentoCRM().getGraficoComoFicouSabendoInstituicao().toString();
	}
	
	public String getMontarDataGraficoMonitoramentoConsultorCRMDocumentacao(){
		return getPainelGestorVO().getGraficoMonitoramentoDocumentacaoConsultor().toString();
	}
	
	public String getMontarDataGraficoMonitoramentoConsultorCRMFinanceiro(){
		return getPainelGestorVO().getGraficoMonitoramentoFinanceiroConsultor().toString();
	}
	
	public String getMontarDataGraficoMonitoramentoConsultorCRMSituacaoAcademica(){
		return getPainelGestorVO().getGraficoMonitoramentoSituacaoAcademicaConsultor().toString();
	}
	
	public String getMontarDataGraficoMonitoramentoConsultorCrmMatriculaAtiva(){
		return getPainelGestorVO().getGraficoMonitoramentoMatriculaAtivaConsultor().toString();
	}
	
	public String getMontarDataGraficoMonitoramentoProspect(){
		return getPainelGestorMonitoramentoCRM().getDadosGraficoMonitoramentoProspect().toString();
	}

	public List getComboBoxListaPeriodicidadeCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem(PeriodicidadeEnum.SEMESTRAL, "Semestral"));
		itens.add(new SelectItem(PeriodicidadeEnum.ANUAL, "Anual"));
		itens.add(new SelectItem(PeriodicidadeEnum.INTEGRAL, "Integral"));
		return itens;
	}
	
	public Boolean getApresentarAnoFiltroAcademico() {
		return getFiltroPainelGestorAcademico().getPeriodicidadeCurso().equals(PeriodicidadeEnum.ANUAL) || getFiltroPainelGestorAcademico().getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL);
	}
	
	public Boolean getApresentarSemestreFiltroAcademico() {
		return getFiltroPainelGestorAcademico().getPeriodicidadeCurso().equals(PeriodicidadeEnum.SEMESTRAL);
	}
	
	public void selecionarLegendaGraficoMonitoramentoAcademicoNivelEducacional(String nivelEducacional, String periodicidade, Integer quantidade, Integer codigo) {
		this.getFiltroPainelGestorAcademico().setTipoNivelEducacional(nivelEducacional);
		this.getFiltroPainelGestorAcademico().setPeriodicidadeCurso(PeriodicidadeEnum.getEnumPorValor(periodicidade));
		consultarDadosMonitoramentoAcademico();
	}
	
	public String selecionarLegendaGraficoMonitoramentoAcademicoNivelEducacional() {
		this.getFiltroPainelGestorAcademico().setTipoNivelEducacional(getNivelEducacional());
		this.getFiltroPainelGestorAcademico().setPeriodicidadeCurso(PeriodicidadeEnum.getEnumPorValor(getPeriodicidade()));
		consultarDadosMonitoramentoAcademico();
		return Uteis.getCaminhoRedirecionamentoNavegacao("painelGestorMonitoramentoAcademicoForm.xhtml");
	}
	
	public String nivelEducacional;
	private String nome;
	public String periodicidade;

	public String getNivelEducacional() {
		if (nivelEducacional == null) {
			nivelEducacional = "";
		}
		return nivelEducacional;
	}

	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}

	public String getPeriodicidade() {
		if (periodicidade == null) {
			periodicidade = "";
		}
		return periodicidade;
	}

	public void setPeriodicidade(String periodicidade) {
		this.periodicidade = periodicidade;
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	

	public void sincronizarTodosProspectsRdStation() {
    	
		try {
			setProspectsVOs(getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().consultarDadosProspectNaoSincronizadosComRdStation(getUnidadeEnsinoVOs(), getDataInicio(), getDataFinal(), getPainelGestorMonitoramentoCRM().getCurso().getCodigo(), getPainelGestorMonitoramentoCRM().getConsultor().getCodigo(), getPainelGestorMonitoramentoCRM().getUnidadeEnsino().getCodigo(), getPainelGestorMonitoramentoCRM().getTipoFiltroMonitamentoCrmProspectEnum(), getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().getMesAnoEnum(), getPainelGestorMonitoramentoCRM().getPainelGestorMonitoramentoDetalheCRMVO().getAno(), getPainelGestorMonitoramentoCRM().getPeriodoUltimoContato1(), getPainelGestorMonitoramentoCRM().getPeriodoUltimoContato2(), getPainelGestorMonitoramentoCRM().getPeriodoUltimoContato3(), getPainelGestorMonitoramentoCRM().getPainelGestorTipoMonitoramentoCRMEnum(), getPainelGestorMonitoramentoCRM().getFiltrarMatriculado(), 0, 0, getCodigoSegmentacao()));
			
			if(getProspectsVOs().isEmpty()) {
				setMensagemID("msg_dados_nenhum_registro");
				return;
			}
			
			setMensagemID("msg_SolicitacaoProcessadaEmSegundoPlano", Uteis.SUCESSO);
			enviarTodosProspectsRdStation(getProspectsVOs());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
 
    }

    public void enviarTodosProspectsRdStation(List<ProspectsVO> prospectsVos) {
    	getFacadeFactory().getLeadInterfaceFacade().incluirListaDeLeadsNoRdStation(prospectsVos, getConfiguracaoGeralPadraoSistema());
    }
    
    private void adicionarLogoUnidadeEnsinoRelatorio(List<UnidadeEnsinoVO> unidadeEnsinoVOs, boolean filtrarPorUnidadeEspecifica, SuperParametroRelVO parametroRelVO) throws Exception {
    	List<UnidadeEnsinoVO> unidadeEnsinoFiltradas = unidadeEnsinoVOs.stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).collect(Collectors.toList());
    	if (Uteis.isAtributoPreenchido(unidadeEnsinoFiltradas) && unidadeEnsinoFiltradas.size() == 1 && filtrarPorUnidadeEspecifica) {
			UnidadeEnsinoVO ue = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(unidadeEnsinoFiltradas.get(0).getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			if (ue.getExisteLogoRelatorio()) {
				parametroRelVO.adicionarParametro("logoPadraoRelatorio", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + ue.getCaminhoBaseLogoRelatorio().replaceAll("\\\\", "/") + File.separator + ue.getNomeArquivoLogoRelatorio());
			}
		} else if (Uteis.isAtributoPreenchido(getLoginControle().getUrlLogoUnidadeEnsinoRelatorio())){
			parametroRelVO.adicionarParametro("logoPadraoRelatorio", getLoginControle().getUrlLogoUnidadeEnsinoRelatorio());	
		}
    }
    
    private void adicionarUnidadeEnsinoRelatorio(List<UnidadeEnsinoVO> unidadeEnsinoVOs, boolean filtrarPorUnidadeEspecifica, SuperParametroRelVO parametroRelVO) throws Exception {
    	String unidadesStr = filtrarPorUnidadeEspecifica && unidadeEnsinoVOs.stream().anyMatch(UnidadeEnsinoVO::getFiltrarUnidadeEnsino) ? 
    			unidadeEnsinoVOs.stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).map(UnidadeEnsinoVO::getNome).collect(Collectors.joining(", ")) : "Todas";
    	parametroRelVO.setUnidadeEnsino(unidadesStr);
    }
    
    private Map<String, DashboardVO> mapDashboards;
	
	
	public Map<String, DashboardVO> getMapDashboards() {
		if(mapDashboards == null) {
			mapDashboards =  new HashMap<String, DashboardVO>(0);
		}
		return mapDashboards;
	}

	public void setMapDashboards(Map<String, DashboardVO> mapDashboards) {
		this.mapDashboards = mapDashboards;
	}

	public void registrarPosicaoDashboard(DropEvent dropEvent) {
		DashboardVO drag = (DashboardVO) dropEvent.getDragValue();
		DashboardVO drop = (DashboardVO) dropEvent.getDropValue();
		if (drag != null && drop != null) {
			Integer ordemDrag = drag.getOrdem();
			drag.setOrdem(drop.getOrdem());
			drop.setOrdem(ordemDrag);
			persistirDashboard(drag);
			persistirDashboard(drop);
		}
	}


	public void persistirDashboard(DashboardVO dashboardVO) {
		try {
			getFacadeFactory().getDashboardInterfaceFacade().persistir(dashboardVO, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	
	public void consultarConfiguracaoDashboard() {
		try {
			List<DashboardVO> dashboards = getFacadeFactory().getDashboardInterfaceFacade().consultarDashboardPorUsuarioAmbiente(getUsuarioLogado(), TipoVisaoEnum.ADMINISTRATIVA, PerfilAcessoModuloEnum.SEI_DECIDIR);
			getMapDashboards().clear();
			for (Iterator<DashboardVO> iterator = dashboards.iterator(); iterator.hasNext();) {
				DashboardVO dashboardVO = (DashboardVO) iterator.next();
				getMapDashboards().put(dashboardVO.getTipoDashboard().name(), dashboardVO);
			}			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public Collection<DashboardVO> getListaDashboard(){
		return getMapDashboards().values();
	}
}