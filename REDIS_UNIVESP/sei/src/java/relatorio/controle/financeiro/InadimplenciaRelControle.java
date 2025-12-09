package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TagsMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TipoOrigemComunicacaoInternaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.AgenteNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.RegistroNegativacaoCobrancaContaReceberItemVO;
import negocio.comuns.financeiro.enumerador.TipoAgenteNegativacaoCobrancaContaReceberEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.comuns.financeiro.InadimplenciaRelVO;
import relatorio.negocio.jdbc.financeiro.InadimplenciaRel;

@SuppressWarnings({ "serial" , "deprecation"})
@Controller("InadimplenciaRelControle")
@Scope("viewScope")
@Lazy
public class InadimplenciaRelControle extends SuperControleRelatorio {

	
	private Boolean filtrarPorDataCompetencia;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<MatriculaVO> listaConsultaAluno;
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
	private Integer quantidadeAlunoInadimplentes;
	private String email;
	private String ordenarPor;
	private Boolean manterModalAberto;
	private Boolean trazerAlunosSerasa;
	private FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO;
	private Boolean enviarSMS;
	private Boolean imprimirApenasTipoOrigemMatricula;
	private Boolean marcarTodosTipoOrigem;
	private ComunicacaoInternaVO comunicacaoEnviar;	
	private CentroReceitaVO centroReceitaVO;
	private List<CentroReceitaVO> listaCentroReceitaVOs;
	private String campoConsultaCentroReceita;
	private String valorConsultaCentroReceita;
	private AgenteNegativacaoCobrancaContaReceberVO agente;
	private String campoConsultaAgente;
	private String valorConsultaAgente;
	List<SelectItem> tipoConsultaComboAgente;
	private List<AgenteNegativacaoCobrancaContaReceberVO> listaConsultaAgente;	
	private List<SelectItem> listaSelectItemFiltroRegistroCobranca;
	private TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente;	
	private Boolean considerarUnidadeEnsinoFinanceira;
	private String situacaoRegistroCobranca;
	private String tagsUtilizarMensagemCobranca;
	
	public InadimplenciaRelControle() {		
		if (!Uteis.isAtributoPreenchido(getFiltroRelatorioFinanceiroVO())) {
			setFiltroRelatorioFinanceiroVO(new FiltroRelatorioFinanceiroVO(getLoginControle().getPermissaoAcessoMenuVO().getPermitirApenasContasDaBiblioteca()));
		}
		montarDadosSalvosEmissaoUltimoRelatorio();
		
		setMensagemID("msg_entre_prmrelatorio");
	}

	public void montarDadosSalvosEmissaoUltimoRelatorio() {
		try {
			
			Map<String, String> retorno = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(null, InadimplenciaRel.class.getSimpleName());
			for (String key : retorno.keySet()) {
				try {
					
					if (key.equals("dataInicio") && retorno.get(key) != null
						&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
						setDataInicio(Uteis.getData(retorno.get(key), "dd/MM/yyyy"));
					} else if (key.equals("dataFim") && retorno.get(key) != null
							&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
						setDataFim(Uteis.getData(retorno.get(key), "dd/MM/yyyy"));					
					} else if (key.equals("serasa") && retorno.get(key) != null 	&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
						setTrazerAlunosSerasa(Boolean.valueOf(retorno.get(key)));
					} else if (key.equals("considerarUnidadeFinanceira") && retorno.get(key) != null && !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
						setConsiderarUnidadeEnsinoFinanceira(Boolean.valueOf(retorno.get(key)));
					}else if (key.equals("ordenarPor") && retorno.get(key) != null	&& !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
						setOrdenarPor(retorno.get(key));
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), InadimplenciaRel.class.getSimpleName(), getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroTipoOrigemContaReceber(getFiltroRelatorioFinanceiroVO(), InadimplenciaRel.class.getSimpleName(), getUsuarioLogado());
			
		} catch (Exception e) {

		}
	}
	
	@PostConstruct
	public void inicializarMatriculaFollowUp() {
		try {
			String matricula = (String) context().getExternalContext().getSessionMap().get("matricula");
			if (matricula != null && !matricula.trim().equals("")) {
				getMatricula().setMatricula(matricula);
				consultarAlunoPorMatricula();
				context().getExternalContext().getSessionMap().remove("matricula");
			}
			Integer unidadeEnsino = (Integer) context().getExternalContext().getSessionMap().get("unidadeEnsino");
			if(unidadeEnsino != null && unidadeEnsino != 0){
				getUnidadeEnsinoVO().setCodigo(unidadeEnsino);
				context().getExternalContext().getSessionMap().remove("unidadeEnsino");
			}
			PessoaVO responsavelFinanceiroVO = (PessoaVO) context().getExternalContext().getSessionMap().get("responsavelFinanceiroInteracaoWorkFlow");
			if (responsavelFinanceiroVO != null && !responsavelFinanceiroVO.getCodigo().equals(0)) {
				setResponsavelFinanceiro(responsavelFinanceiroVO);
				context().getExternalContext().getSessionMap().remove("responsavelFinanceiroInteracaoWorkFlow");
			}
			
		} catch (Exception e) {
			
		}
		
	}

	private void imprimirPDF() {
		List<InadimplenciaRelVO> listaObjetos = null;		
		String retornoTipoRelatorio = null;
		try {
			if (getFiltrarPorDataCompetencia()) {
				setDataInicio(Uteis.getDataPrimeiroDiaMes(getDataInicio()));
				setDataFim(Uteis.getDataUltimoDiaMes(getDataFim()));
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "InadimplenciaRelControle", "Inicializando Geração de Relatório Inadimplencia" + this.getUnidadeEnsinoVO().getNome() + " - " + getUsuarioLogado().getCodigo() + " - " + getUsuarioLogado().getPerfilAcesso().getCodigo(), "Emitindo Relatório");
			validarImpressaoRelatorio();
			getFacadeFactory().getInadimplenciaRelFacade().validarDados(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getDataInicio(), getDataFim(), getTipoRelatorio());//			
			listaObjetos = getFacadeFactory().getInadimplenciaRelFacade().gerarListaComDesconto(getCursoVOs(), obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getFiltrarPorDataCompetencia() , getTurma(), getMatricula(), getOrdenarPor() , getDataInicio(), getDataFim(), getUsuarioLogado(), getResponsavelFinanceiro(), false, getFiltroRelatorioAcademicoVO(), getTrazerAlunosSerasa() , getConsiderarUnidadeEnsinoFinanceira(), getImprimirApenasTipoOrigemMatricula(), getFiltroRelatorioFinanceiroVO(),getCentroReceitaVO(), getAgente(), getTipoAgente(),getSituacaoRegistroCobranca());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getInadimplenciaRelFacade().designIReportRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getInadimplenciaRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Inadimplência da Instituição");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getInadimplenciaRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");
				getSuperParametroRelVO().adicionarParametro("considerarUnidadeEnsinoFinanceira", getConsiderarUnidadeEnsinoFinanceira());
				getSuperParametroRelVO().adicionarParametro("filtroRelatorioFinanceiro", getFiltroRelatorioFinanceiroVO().getItensFiltroTipoOrigem());
				getSuperParametroRelVO().adicionarParametro("agente", getAgente().getNome());
				if(Uteis.isAtributoPreenchido(getSituacaoRegistroCobranca())) {
					getSuperParametroRelVO().adicionarParametro("filtrarSituacaoNegativacaoCobranca", FiltroRelatorioFinanceiroVO.FiltroRegistroCobranca.valueOf(getSituacaoRegistroCobranca()).getDescricao());
				}
				if (getDataInicio() != null) {
					getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(getDataInicio())) + "  a  " + String.valueOf(Uteis.getData(getDataFim())));
				} else {
					getSuperParametroRelVO().setPeriodo(String.valueOf("Datas anteriores à " + String.valueOf(Uteis.getData(getDataFim()))));
				}
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
				
				if (!getCursosApresentar().trim().isEmpty()) {
					getSuperParametroRelVO().setCurso(getCursosApresentar());
				} else {
					getSuperParametroRelVO().setCurso("TODOS");
				}
				
				retornoTipoRelatorio = "";

				if (getTipoRelatorio().equals("relatorioSimplificado")) {
					retornoTipoRelatorio = "Relatório Simplificado";
				} else {
					retornoTipoRelatorio = "Relatório Detalhado";
				}
				getSuperParametroRelVO().setTipoRelatorio(retornoTipoRelatorio);
				adicionarFiltroSituacaoAcademica(getSuperParametroRelVO());

				if (Uteis.isAtributoPreenchido(getTurma().getCodigo())) {
					getSuperParametroRelVO().setTurma(getTurma().getIdentificadorTurma());
				} else {
					getSuperParametroRelVO().setTurma("TODAS");
				}
				this.adicionarParametroCentroReceita();
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
	            registrarAtividadeUsuario(getUsuarioLogado(), "InadimplenciaRel", "Finalizando Geração de Relatório Inadimplencia", "Emitindo Relatório");	            
	            gravarDadosPadroesEmissaoRelatorio();
	            //removerObjetoMemoria(this);				
	            
			} else {				
				setMensagemID("msg_relatorio_sem_dados");
				setFazerDownload(false);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);			
			retornoTipoRelatorio = null;			
		}
	}

	public void adicionarFiltroSituacaoAcademica(SuperParametroRelVO superParametroRelVO) {
		superParametroRelVO.adicionarParametro("filtroAcademicoAtivo", getFiltroRelatorioAcademicoVO().getAtivo());
		superParametroRelVO.adicionarParametro("filtroAcademicoTrancado", getFiltroRelatorioAcademicoVO().getTrancado());
		superParametroRelVO.adicionarParametro("filtroAcademicoCancelado", getFiltroRelatorioAcademicoVO().getCancelado());
		superParametroRelVO.adicionarParametro("filtroAcademicoPreMatricula", getFiltroRelatorioAcademicoVO().getPreMatricula());
		superParametroRelVO.adicionarParametro("filtroAcademicoPreMatriculaCancelada", getFiltroRelatorioAcademicoVO().getPreMatriculaCancelada());
		superParametroRelVO.adicionarParametro("filtroAcademicoConcluido", getFiltroRelatorioAcademicoVO().getConcluido());
		superParametroRelVO.adicionarParametro("filtroAcademicoPendenteFinanceiro", getFiltroRelatorioAcademicoVO().getPendenteFinanceiro());
		superParametroRelVO.adicionarParametro("filtroAcademicoAbandonado", getFiltroRelatorioAcademicoVO().getAbandonado());
		superParametroRelVO.adicionarParametro("filtroAcademicoTransferenciaInterna", getFiltroRelatorioAcademicoVO().getTransferenciaInterna());
		superParametroRelVO.adicionarParametro("filtroAcademicoTransferenciaSaida", getFiltroRelatorioAcademicoVO().getTransferenciaExterna());
		superParametroRelVO.adicionarParametro("filtroAcademicoFormado", getFiltroRelatorioAcademicoVO().getFormado());
		superParametroRelVO.adicionarParametro("filtroAcademicoJubilado", getFiltroRelatorioAcademicoVO().getJubilado());
	} 

	public void imprimirRelatorioExcel() {
		List<InadimplenciaRelVO> listaObjetos = null;		
		String retornoTipoRelatorio = null;
		try {
			if (getFiltrarPorDataCompetencia()) {
				setDataInicio(Uteis.getDataPrimeiroDiaMes(getDataInicio()));
				setDataFim(Uteis.getDataUltimoDiaMes(getDataFim()));
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "InadimplenciaRelControle", "Inicializando Geração de Relatório Inadimplencia" + this.getUnidadeEnsinoVO().getNome() + " - " + getUsuarioLogado().getCodigo() + " - " + getUsuarioLogado().getPerfilAcesso().getCodigo(), "Emitindo Relatório");
			validarImpressaoRelatorio();
			getFacadeFactory().getInadimplenciaRelFacade().validarDados(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getDataInicio(), getDataFim(), getTipoRelatorio());//			
			listaObjetos = getFacadeFactory().getInadimplenciaRelFacade().gerarListaComDesconto(getCursoVOs(), obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getFiltrarPorDataCompetencia() , getTurma(), getMatricula(), getOrdenarPor() , getDataInicio(), getDataFim(), getUsuarioLogado(), getResponsavelFinanceiro(), false, getFiltroRelatorioAcademicoVO(), getTrazerAlunosSerasa(), getConsiderarUnidadeEnsinoFinanceira(), getImprimirApenasTipoOrigemMatricula(), getFiltroRelatorioFinanceiroVO(),getCentroReceitaVO(), getAgente(), getTipoAgente(),getSituacaoRegistroCobranca());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getInadimplenciaRelFacade().designIReportRelatorioExcel());
		        getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getInadimplenciaRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Inadimplência da Instituição");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getInadimplenciaRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");
				getSuperParametroRelVO().adicionarParametro("filtroRelatorioFinanceiro", getFiltroRelatorioFinanceiroVO().getItensFiltroTipoOrigem());
				getSuperParametroRelVO().adicionarParametro("agente", getAgente().getNome());
				if (getDataInicio() != null) {
					getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(getDataInicio())) + "  a  " + String.valueOf(Uteis.getData(getDataFim())));
				} else {
					getSuperParametroRelVO().setPeriodo(String.valueOf("Datas anteriores à " + String.valueOf(Uteis.getData(getDataFim()))));
				}
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
				
				if (!getCursosApresentar().trim().isEmpty()) {
					getSuperParametroRelVO().setCurso(getCursosApresentar());
				} else {
					getSuperParametroRelVO().setCurso("TODOS");
				}
				
				retornoTipoRelatorio = "";

				if (getTipoRelatorio().equals("relatorioSimplificado")) {
					retornoTipoRelatorio = "Relatório Simplificado";
				} else {
					retornoTipoRelatorio = "Relatório Detalhado";
				}
				getSuperParametroRelVO().setTipoRelatorio(retornoTipoRelatorio);
				adicionarFiltroSituacaoAcademica(getSuperParametroRelVO());

				if (Uteis.isAtributoPreenchido(getTurma().getCodigo())) {
					getSuperParametroRelVO().setTurma(getTurma().getIdentificadorTurma());
				} else {
					getSuperParametroRelVO().setTurma("TODAS");
				}
				this.adicionarParametroCentroReceita();
				realizarImpressaoRelatorio();
				//removerObjetoMemoria(this);				
				setMensagemID("msg_relatorio_ok");
	            registrarAtividadeUsuario(getUsuarioLogado(), "InadimplenciaRel", "Finalizando Geração de Relatório Inadimplencia", "Emitindo Relatório");	
			} 
			
			gravarDadosPadroesEmissaoRelatorio();
			
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);			
		}
	}
	
	public void gravarDadosPadroesEmissaoRelatorio(){
		try{
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(Uteis.getData(getDataInicio(), "dd/MM/yyyy"), InadimplenciaRel.class.getSimpleName(), "dataInicio", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(Uteis.getData(getDataFim(), "dd/MM/yyyy"), InadimplenciaRel.class.getSimpleName(), "dataFim", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getUnidadeEnsinosApresentar(), InadimplenciaRel.class.getSimpleName(), "unidadeEnsino", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTrazerAlunosSerasa().toString(), InadimplenciaRel.class.getSimpleName(), "serasa", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getConsiderarUnidadeEnsinoFinanceira().toString(), InadimplenciaRel.class.getSimpleName(), "considerarUnidadeFinanceira", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getConsiderarUnidadeEnsinoFinanceira().toString(), InadimplenciaRel.class.getSimpleName(), "considerarUnidadeFinanceira", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirFiltroTipoOrigemContaReceber(getFiltroRelatorioFinanceiroVO(), InadimplenciaRel.class.getSimpleName(), getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), InadimplenciaRel.class.getSimpleName(), getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getOrdenarPor(), InadimplenciaRel.class.getSimpleName(), "ordenarPor", getUsuarioLogado());
			
		}catch(Exception e){
		}
	}

	public void imprimirPDFSimplificado() {
		InadimplenciaRel.setIdEntidade("InadimplenciaRel_Simplificado");
		imprimirPDF();
	}

	public void imprimirPDFDetalhado() {
		InadimplenciaRel.setIdEntidade("InadimplenciaRel_DetalhadoNovo");
		imprimirPDF();
	}

	public void contarNumeroAlunosInadimplentes() {
		try {
			setQuantidadeAlunoInadimplentes(getFacadeFactory().getInadimplenciaRelFacade().gerarQuantidadeAlunosInadimplentes(getCursoVOs(), obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getFiltrarPorDataCompetencia(), getTurma(), getMatricula(), getDataInicio(), getDataFim(), getUsuarioLogado(), getResponsavelFinanceiro(), false, getFiltroRelatorioAcademicoVO(), getTrazerAlunosSerasa(), getConsiderarUnidadeEnsinoFinanceira(), getImprimirApenasTipoOrigemMatricula(), getFiltroRelatorioFinanceiroVO(),getCentroReceitaVO() ,getSituacaoRegistroCobranca()));
			setManterModalAberto(Boolean.TRUE);			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void enviarEmailAlunosInadimplentes() {
		try {
			if (getEnviarSMS() && getComunicacaoEnviar().getMensagemSMS().length() > 150) {
				throw new Exception(getMensagemInternalizacao("msg_LimiteCampoTextoSms"));
			}
			if (getEnviarSMS() && getComunicacaoEnviar().getMensagemSMS().isEmpty()) {
				throw new Exception(getMensagemInternalizacao("msg_EnviarSmsTextoVazio"));
			}
			
			validarImpressaoRelatorio();
			Map<Integer, UnidadeEnsinoVO> mapUnidadeEnsino = new HashMap<Integer, UnidadeEnsinoVO>(0);
			final List<InadimplenciaRelVO> listaObjetos = getFacadeFactory().getInadimplenciaRelFacade().gerarListaInadimplenteEnvioNotificacao(getCursoVOs(), obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getFiltrarPorDataCompetencia() , getTurma(), getMatricula(), getOrdenarPor(),  getDataInicio(), getDataFim(), getUsuarioLogado(), getResponsavelFinanceiro(), false, getFiltroRelatorioAcademicoVO(), getTrazerAlunosSerasa(), getConsiderarUnidadeEnsinoFinanceira(), getImprimirApenasTipoOrigemMatricula(), getFiltroRelatorioFinanceiroVO(),getCentroReceitaVO());						
			final ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmailMaisResponsavelPadrao();
			final UsuarioVO usuarioLog =  getUsuarioLogadoClone();
			final ConsistirException consistirException =  new ConsistirException();
			String corpoMensagem = getEmail();
			ProcessarParalelismo.executar(0, listaObjetos.size(), consistirException, new ProcessarParalelismo.Processo() {
				@Override
				public void run(int i) {
					try {
						InadimplenciaRelVO obj =	listaObjetos.get(i);
						String corpoMensagemSMS = getComunicacaoEnviar().getMensagemSMS();
						if ((!obj.getEmail().trim().isEmpty() && obj.getEmail().contains("@") && obj.getEmail().contains(".")) || (!obj.getEmail2().trim().isEmpty() && obj.getEmail2().contains("@")&& obj.getEmail2().contains("."))) {	
							ComunicacaoInternaVO comunicacaoInternaVO = new ComunicacaoInternaVO();
							comunicacaoInternaVO.setAssunto(getComunicacaoEnviar().getAssunto());
							comunicacaoInternaVO.setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum.FINANCEIRO_COBRANCA);
							comunicacaoInternaVO.getUnidadeEnsino().setCodigo(obj.getContaReceberVO().getUnidadeEnsino().getCodigo());
							if(!mapUnidadeEnsino.containsKey(obj.getContaReceberVO().getUnidadeEnsino().getCodigo())) {
								getFacadeFactory().getUnidadeEnsinoFacade().carregarDados(comunicacaoInternaVO.getUnidadeEnsino(), NivelMontarDados.BASICO, usuarioLog);
								mapUnidadeEnsino.put(obj.getContaReceberVO().getUnidadeEnsino().getCodigo(), comunicacaoInternaVO.getUnidadeEnsino());
							}else {
								obj.getContaReceberVO().setUnidadeEnsino(mapUnidadeEnsino.get(obj.getContaReceberVO().getUnidadeEnsino().getCodigo()));
							}
							
							ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
							comunicadoInternoDestinatarioVO.setTipoComunicadoInterno("LE");
							comunicadoInternoDestinatarioVO.setDestinatario(obj.getContaReceberVO().getPessoa());
							comunicacaoInternaVO.setAluno(obj.getContaReceberVO().getPessoa());			
							if ((!obj.getEmail().trim().isEmpty() && obj.getEmail().contains("@") && obj.getEmail().contains("."))) {
								comunicadoInternoDestinatarioVO.setEmail(obj.getEmail());
								comunicadoInternoDestinatarioVO.setNome(obj.getNome());
								comunicadoInternoDestinatarioVO.getDestinatario().setEmail(obj.getEmail());
							} else {
								comunicadoInternoDestinatarioVO.setEmail(obj.getEmail2());
								comunicadoInternoDestinatarioVO.setNome(obj.getNome());
								comunicadoInternoDestinatarioVO.getDestinatario().setEmail(obj.getEmail2());
							}
							comunicadoInternoDestinatarioVO.getDestinatario().setCelular(obj.getTelcelular());				
							
							comunicadoInternoDestinatarioVO.getDestinatario().setNome(obj.getNome());
							
							comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs().add(comunicadoInternoDestinatarioVO);
							comunicacaoInternaVO.setTipoRemetente("FU");
							comunicacaoInternaVO.setTipoDestinatario("AL");
							comunicacaoInternaVO.setTipoComunicadoInterno("LE");			
							PessoaVO responsavel = config.getResponsavelPadraoComunicadoInterno();
							comunicacaoInternaVO.setResponsavel(responsavel);
							comunicacaoInternaVO.setMensagem(obterMensagemFormatadaMensagemCobrancaAlunoInadimplente(obj, corpoMensagem));
							
							comunicacaoInternaVO.setEnviarSMS(getEnviarSMS());
							corpoMensagemSMS = obterMensagemSmsFormatadaMensagemCobrancaAlunoInadimplente(obj, corpoMensagemSMS);
							String msgSMS = corpoMensagemSMS;
							if (msgSMS.length() > 150) {
								msgSMS = msgSMS.substring(0, 150);
							}
							comunicacaoInternaVO.setMensagemSMS(msgSMS);		
							
							
							getFacadeFactory().getComunicacaoInternaFacade().realizarTrocarLogoEmailPorUnidadeEnsino(comunicacaoInternaVO, config, usuarioLog);
							
							getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoInternaVO, false, usuarioLog, config,null);
							
						}
					}catch (Exception e) {
							
						if(!consistirException.getListaMensagemErro().contains(e.getMessage())) {
							consistirException.adicionarListaMensagemErro(e.getMessage());
						}
					}
				}
			});
			if(!consistirException.getListaMensagemErro().isEmpty()) {
				throw new Exception(consistirException.getToStringMensagemErro());
			}
			setManterModalAberto(Boolean.FALSE);
			setMensagemID("msg_msg_emailsEnviados");
		} catch (Exception e) {
			setManterModalAberto(Boolean.TRUE);
			setMensagemDetalhada("msg_erro", e.getMessage());
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
	
	public List<SelectItem> getTipoOrdenacao() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
		itens.add(new SelectItem("matricula", "Matricula"));
		itens.add(new SelectItem("nomeAluno", "Nome Aluno"));
		itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("turma", "Turma"));
		itens.add(new SelectItem("turno", "Turno"));
		itens.add(new SelectItem("origem", "Origem"));
		itens.add(new SelectItem("dataVencimento", "Data Vencimento"));
		return itens;
	}	

	public List<SelectItem> getListaSelectItemTipoRelatorio() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		// itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("relatorioDetalhado", "Relatório Detalhado"));
		// itens.add(new SelectItem("relatorioSimplificado",
		// "Relatório Simplificado"));
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
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), null, getConsiderarUnidadeEnsinoFinanceira() ? new ArrayList<UnidadeEnsinoVO>(0) : obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsinoCurso(getValorConsultaTurma(), null, getConsiderarUnidadeEnsinoFinanceira() ? new ArrayList<UnidadeEnsinoVO>(0) : obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurnoCurso(getValorConsultaTurma(), null, getConsiderarUnidadeEnsinoFinanceira() ? new ArrayList<UnidadeEnsinoVO>(0) : obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), null, 0, getConsiderarUnidadeEnsinoFinanceira() ? new ArrayList<UnidadeEnsinoVO>(0) : obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
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
			limparCurso();
			setCursosApresentar(obj.getCurso().getNome());
			considerarFiltrarUnidadeEnsino(obj.getUnidadeEnsino());
			getListaConsultaTurma().clear();
			this.setValorConsultaTurma("");
			this.setCampoConsultaTurma("");
			setMensagemID("", "");
		} catch (Exception e) {
		}
	}

	public void limparTurma()  {
		try {
			setCursosApresentar("");
			setTurma(null);			
		} catch (Exception e) {
		}
	}

	public void limparAluno()  {
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
			List<CursoVO> cursoVOs = getFacadeFactory().getCursoFacade().consultarTodosCursosPorUnidadeEnsino(getConsiderarUnidadeEnsinoFinanceira() ? new ArrayList<UnidadeEnsinoVO>(0) : obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()));
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
		if (!getConsiderarUnidadeEnsinoFinanceira()) {
			setTurma(null);
			setMatricula(null);					
			consultarCurso();
		} else {
			consultarCurso();
		}
	}

	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getCampoConsultaAluno().equals("nome")) {
				if (Uteis.isAtributoPreenchido(getTurma().getCodigo())) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoaCursoTurma(getValorConsultaAluno(), obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), null, getTurma().getCodigo(), false, getUsuarioLogado());
				} else if (Uteis.isAtributoPreenchido(getCursoVOs())) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), null, false, getUsuarioLogado());
				} else if (Uteis.isAtributoPreenchido(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()))) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), getUnidadeEnsinoLogado().getCodigo(), false, "","", getUsuarioLogado());
				} else {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), 0, false, getUsuarioLogado());
				}
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
			if (getMatricula().getMatricula().equals("")) {
				setMatricula(new MatriculaVO());
				limparCurso();
			} else if(getMatricula().getMatricula().length() > 50){
				throw new Exception("Não Foi Possível Localizar o Aluno de Matrícula " + getMatricula().getMatricula() + ".Verifique Se o Número de Matrícula Está Correto.");
			} else {
				MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getMatricula().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				if (objAluno.getMatricula().equals("")) {
					throw new Exception("Aluno de matrícula " + getMatricula().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
				}
				considerarFiltrarUnidadeEnsino(objAluno.getUnidadeEnsino());
				limparCurso();
				setCursosApresentar(objAluno.getCurso().getNome());
				setMatricula(objAluno);
			}
			setTurma(new TurmaVO());
			setCampoConsultaAluno("");
			setValorConsultaAluno("");
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
		} catch (Exception e) {
			setMatricula(new MatriculaVO());;
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		setMatricula(obj);
		considerarFiltrarUnidadeEnsino(obj.getUnidadeEnsino());
		limparCurso();
		setCursosApresentar(obj.getCurso().getNome());
		setTurma(new TurmaVO());
		valorConsultaAluno = "";
		campoConsultaAluno = "";
		getListaConsultaAluno().clear();
	}

	public List<SelectItem> tipoConsultaComboAluno;
	public List<SelectItem> getTipoConsultaComboAluno() {
		if(tipoConsultaComboAluno == null){
			tipoConsultaComboAluno = new ArrayList<SelectItem>(0);
			tipoConsultaComboAluno.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboAluno;
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

	public List<MatriculaVO> getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<MatriculaVO>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
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
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}	

	public Integer getQuantidadeAlunoInadimplentes() {
		if (quantidadeAlunoInadimplentes == null) {
			quantidadeAlunoInadimplentes = 0;
		}
		return quantidadeAlunoInadimplentes;
	}

	public void setQuantidadeAlunoInadimplentes(Integer quantidadeAlunoInadimplentes) {
		this.quantidadeAlunoInadimplentes = quantidadeAlunoInadimplentes;
	}

	public String getEmail() {
		if (email == null) {
			email = "";
		}
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getShowHideModalEnviarEmail() {
		return getManterModalAberto() ? "RichFaces.$('panelEnvioEmailAlunosInadimplentes').show()" : "RichFaces.$('panelEnvioEmailAlunosInadimplentes').hide()";
	}
	
	public void realizarDefinicoesEnvioPersonalizacaoMensagemAutomaticaInadimplenciaRel() {
		setTagsUtilizarMensagemCobranca(TemplateMensagemAutomaticaEnum.MENSAGEM_RELATORIO_INADIMPLENCIA.getTags_Apresentar());
		ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
		try {
			getFacadeFactory().getInadimplenciaRelFacade().validarDados(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getDataInicio(), getDataFim(), getTipoRelatorio());//
			contarNumeroAlunosInadimplentes();
			PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_RELATORIO_INADIMPLENCIA, false, getUsuarioLogado());
			if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
				setTagsUtilizarMensagemCobranca(mensagemTemplate.getTags());
				String mensagem = realizarAlteracaoCaminhoImagemCimaBaixoMensagemApresentar(mensagemTemplate.getMensagem());
				comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
				comunicacaoEnviar.setMensagem(mensagem);
				comunicacaoEnviar.setMensagemSMS(!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica() ? mensagemTemplate.getMensagemSMS() : "");
				comunicacaoEnviar.setEnviarSMS(!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica());
				setEnviarSMS(!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica());
				setEmail(mensagem);
			} else {
				setEnviarSMS(Boolean.TRUE);				
				setEmail(comunicacaoEnviar.getMensagem());
				comunicacaoEnviar.setAssunto("Inadimplência");
			}
			if (Uteis.isAtributoPreenchido(getUnidadeEnsinoLogado().getCodigo())) {
				comunicacaoEnviar.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
				getFacadeFactory().getComunicacaoInternaFacade().realizarTrocarLogoEmailPorUnidadeEnsino(comunicacaoEnviar, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
				setEmail(comunicacaoEnviar.getMensagem());
			}				
			setComunicacaoEnviar(comunicacaoEnviar);
		} catch(Exception e) {
			setManterModalAberto(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String obterMensagemFormatadaMensagemCobrancaAlunoInadimplente(InadimplenciaRelVO inadimplenciaRelVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), inadimplenciaRelVO.getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_SACADO.name(), inadimplenciaRelVO.getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), inadimplenciaRelVO.getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), inadimplenciaRelVO.getUnidadeEnsino());
		StringBuilder listaContaReceber = new StringBuilder("<ul>");
		if (inadimplenciaRelVO.getListaParcelaNotificacaoInadimplente().contains(";")) {
			for (String contaReceber : inadimplenciaRelVO.getListaParcelaNotificacaoInadimplente().split(";")) {
				listaContaReceber.append("<li>");
				listaContaReceber.append(" <strong>").append(contaReceber).append("</strong>");
				listaContaReceber.append("</li>");
			}
		} else {
			listaContaReceber.append("<li>");
			listaContaReceber.append(" <strong>").append(inadimplenciaRelVO.getListaParcelaNotificacaoInadimplente()).append("</strong>");
			listaContaReceber.append("</li>");
		}
		listaContaReceber.append("</ul>");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.LISTA_CONTARECEBER_DETALHE.name(), Matcher.quoteReplacement(listaContaReceber.toString()));
		return mensagemTexto;
	}
	
	public Boolean getManterModalAberto() {
		if (manterModalAberto == null) {
			manterModalAberto = Boolean.FALSE;
		}
		return manterModalAberto;
	}

	public void setManterModalAberto(Boolean manterModalAberto) {
		this.manterModalAberto = manterModalAberto;
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

	public String getOrdenarPor() {
		if (ordenarPor == null) {
			ordenarPor = "TU";
		}
		return ordenarPor;
	}

	public void setOrdenarPor(String ordenarPor) {
		this.ordenarPor = ordenarPor;
	}

	public void limparDadosResponsavelFinanceiro() {
		getResponsavelFinanceiro().setCodigo(0);
		getResponsavelFinanceiro().setNome("");
	}

	public Boolean getTrazerAlunosSerasa() {
		if (trazerAlunosSerasa == null) {
			trazerAlunosSerasa = Boolean.FALSE;
		}
		return trazerAlunosSerasa;
	}

	public void setTrazerAlunosSerasa(Boolean trazerAlunosSerasa) {
		this.trazerAlunosSerasa = trazerAlunosSerasa;
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

	public Boolean getFiltrarPorDataCompetencia() {
		if (filtrarPorDataCompetencia == null) {
			filtrarPorDataCompetencia = false;
		}
		return filtrarPorDataCompetencia;
	}

	public void setFiltrarPorDataCompetencia(Boolean filtrarPorDataCompetencia) {
		this.filtrarPorDataCompetencia = filtrarPorDataCompetencia;
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

	public void validarImpressaoRelatorio() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("InadimplenciaEmitirApenasMatricula", getUsuarioLogado());
			setImprimirApenasTipoOrigemMatricula(Boolean.TRUE);
		} catch (Exception e) {
			setImprimirApenasTipoOrigemMatricula(Boolean.FALSE);
		}
	}
	

	public Boolean getImprimirApenasTipoOrigemMatricula() {
		if(imprimirApenasTipoOrigemMatricula == null){
			imprimirApenasTipoOrigemMatricula = Boolean.FALSE;
		}
		return imprimirApenasTipoOrigemMatricula;
	}


	public void setImprimirApenasTipoOrigemMatricula(Boolean imprimirApenasTipoOrigemMatricula) {
		this.imprimirApenasTipoOrigemMatricula = imprimirApenasTipoOrigemMatricula;
	}

	public FiltroRelatorioFinanceiroVO getFiltroRelatorioFinanceiroVO() {
		if (filtroRelatorioFinanceiroVO == null) {
			filtroRelatorioFinanceiroVO = new FiltroRelatorioFinanceiroVO(getLoginControle().getPermissaoAcessoMenuVO().getPermitirApenasContasDaBiblioteca());
		}
		return filtroRelatorioFinanceiroVO;
	}

	public void setFiltroRelatorioFinanceiroVO(FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) {
		this.filtroRelatorioFinanceiroVO = filtroRelatorioFinanceiroVO;
	}
	
	public void inicializarDadosFiltrRelatorioFinanceiroVO() {
		getFiltroRelatorioFinanceiroVO().setTipoOrigemBiblioteca(true);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemBolsaCusteadaConvenio(true);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemContratoReceita(true);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemDevolucaoCheque(true);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemInclusaoReposicao(true);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemInscricaoProcessoSeletivo(false);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemMatricula(true);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemMensalidade(true);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemNegociacao(true);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemOutros(true);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemRequerimento(false);
		getFiltroRelatorioFinanceiroVO().setTipoOrigemMaterialDidatico(true);
	}
	
	public Boolean getEnviarSMS() {
		if (enviarSMS == null) {
			enviarSMS = Boolean.TRUE;
		}
		return enviarSMS;
	}


	public void setEnviarSMS(Boolean enviarSMS) {
		this.enviarSMS = enviarSMS;
	}
	
	public Boolean getMarcarTodosTipoOrigem() {
		if (marcarTodosTipoOrigem == null) {
			marcarTodosTipoOrigem = true;
		}
		return marcarTodosTipoOrigem;
	}

	public void setMarcarTodosTipoOrigem(Boolean marcarTodosTipoOrigem) {
		this.marcarTodosTipoOrigem = marcarTodosTipoOrigem;
	}



	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodosTipoOrigem() {
		if (getMarcarTodosTipoOrigem()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}

	public void inicializarDadosFiltrRelatorioAcademicoVO() {
		getFiltroRelatorioAcademicoVO().setAbandonado(true);
		getFiltroRelatorioAcademicoVO().setAtivo(true);
		getFiltroRelatorioAcademicoVO().setCancelado(true);
		getFiltroRelatorioAcademicoVO().setConcluido(true);
		getFiltroRelatorioAcademicoVO().setFormado(true);
		getFiltroRelatorioAcademicoVO().setPreMatricula(true);
		getFiltroRelatorioAcademicoVO().setPreMatriculaCancelada(true);
		getFiltroRelatorioAcademicoVO().setTrancado(true);
		getFiltroRelatorioAcademicoVO().setTransferenciaExterna(true);
		getFiltroRelatorioAcademicoVO().setTransferenciaInterna(true);
		setMarcarTodasSituacoesAcademicas(true);
	}

	public void realizarSelecaoCheckboxMarcarDesmarcarTodosTipoOrigem() {
		if (getMarcarTodosTipoOrigem()) {
			filtroRelatorioFinanceiroVO.realizarMarcarTodasOrigens();
		} else {
			filtroRelatorioFinanceiroVO.realizarDesmarcarTodasOrigens();
		}
	}
	
	public List<UnidadeEnsinoVO> obterListaUnidadeEnsinoSelecionada(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
		List<UnidadeEnsinoVO> objs = new ArrayList<UnidadeEnsinoVO>(0);
		unidadeEnsinoVOs.forEach(obj->{
			if (obj.getFiltrarUnidadeEnsino()) {
				objs.add(obj);
			}
		});
		return objs;
	}
	
	public Boolean getIsApresentarBotoesSelecionarLimparCurso() {
		return !(Uteis.isAtributoPreenchido(getTurma().getIdentificadorTurma()) || Uteis.isAtributoPreenchido(getMatricula().getMatricula()));
	}

	public ComunicacaoInternaVO getComunicacaoEnviar() {
		if (comunicacaoEnviar == null) {
			comunicacaoEnviar = new ComunicacaoInternaVO();
		}
		return comunicacaoEnviar;
	}

	public void setComunicacaoEnviar(ComunicacaoInternaVO comunicacaoEnviar) {
		this.comunicacaoEnviar = comunicacaoEnviar;
	}
	
	public String getIsRenderizarFormularioModalEmailSms() {
		if (!getManterModalAberto()) {
			return "formEnvioEmailAlunosInadimplentes, form";
		}
		return "formEnvioEmailAlunosInadimplentes:mensagemEmissaoTermoReconhecimentoDivida";
	}
	public String obterMensagemSmsFormatadaMensagemCobrancaAlunoInadimplente(InadimplenciaRelVO inadimplenciaRelVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), inadimplenciaRelVO.getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_SACADO.name(), inadimplenciaRelVO.getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), inadimplenciaRelVO.getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), inadimplenciaRelVO.getUnidadeEnsino());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.LISTA_CONTARECEBER_DETALHE.name(), Matcher.quoteReplacement(inadimplenciaRelVO.getListaParcelaNotificacaoInadimplente()));
		return mensagemTexto;
	}
	
	public CentroReceitaVO getCentroReceitaVO() {
		if (centroReceitaVO == null) {
			centroReceitaVO = new CentroReceitaVO();
		}
		return centroReceitaVO;
	}

	public void setCentroReceitaVO(CentroReceitaVO centroReceitaVO) {
		this.centroReceitaVO = centroReceitaVO;
	}

	public List<CentroReceitaVO> getListaCentroReceitaVOs() {
		if (listaCentroReceitaVOs == null) {
			listaCentroReceitaVOs = new ArrayList<CentroReceitaVO>(0);
		}
		return listaCentroReceitaVOs;
	}

	public void setListaCentroReceitaVOs(List<CentroReceitaVO> listaCentroReceitaVOs) {
		this.listaCentroReceitaVOs = listaCentroReceitaVOs;
	}

	public String getCampoConsultaCentroReceita() {
		if (campoConsultaCentroReceita == null) {
			campoConsultaCentroReceita = "";
		}
		return campoConsultaCentroReceita;
	}

	public void setCampoConsultaCentroReceita(String campoConsultaCentroReceita) {
		this.campoConsultaCentroReceita = campoConsultaCentroReceita;
	}

	public String getValorConsultaCentroReceita() {
		if (valorConsultaCentroReceita == null) {
			valorConsultaCentroReceita = "";
		}
		return valorConsultaCentroReceita;
	}

	public void setValorConsultaCentroReceita(String valorConsultaCentroReceita) {
		this.valorConsultaCentroReceita = valorConsultaCentroReceita;
	}
	
	public void consultarCentroReceita() {
		try {
			List<CentroReceitaVO> objs = new ArrayList<CentroReceitaVO>(0);
			if (getCampoConsultaCentroReceita().equals("descricao")) {
				objs = getFacadeFactory().getCentroReceitaFacade().consultarPorDescricao(getValorConsultaCentroReceita(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}else if (getCampoConsultaCentroReceita().equals("identificadorCentroReceita")) {
				objs = getFacadeFactory().getCentroReceitaFacade().consultarPorIdentificadorCentroReceita(getValorConsultaCentroReceita(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}else if (getCampoConsultaCentroReceita().equals("nomeDepartamento")) {
				objs = getFacadeFactory().getCentroReceitaFacade().consultarPorNomeDepartamento(getValorConsultaCentroReceita(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaCentroReceitaVOs(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaCentroReceitaVOs(new ArrayList<CentroReceitaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarCentroReceita() {
		CentroReceitaVO obj = (CentroReceitaVO) context().getExternalContext().getRequestMap().get("centroReceitaItem");
		setCentroReceitaVO(obj);
		this.limparDadosConsultaCentroReceita();
	}

	public List<SelectItem> getTipoConsultaComboCentroReceita() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificadorCentroReceita", "Identificador Centro Receita"));
		itens.add(new SelectItem("nomeDepartamento", "Departamento"));
		return itens;
	}

	public void limparDadosConsultaCentroReceita() {
		setValorConsultaCentroReceita("");
		setCampoConsultaCentroReceita("");
		setListaCentroReceitaVOs(null);
	}

	public void limparDadosCentroReceita() {
		setCentroReceitaVO(null);
		this.limparDadosConsultaCentroReceita();
	}

	public void adicionarParametroCentroReceita() {
		if (Uteis.isAtributoPreenchido(getCentroReceitaVO().getDescricao())) {
			getSuperParametroRelVO().adicionarParametro("centroReceita", getCentroReceitaVO().getDescricao());
		} else {
			getSuperParametroRelVO().adicionarParametro("centroReceita", "TODOS");
		}
	}

	public Boolean getConsiderarUnidadeEnsinoFinanceira() {
		if (considerarUnidadeEnsinoFinanceira == null)
			considerarUnidadeEnsinoFinanceira = Boolean.FALSE;
		return considerarUnidadeEnsinoFinanceira;
	}

	public void setConsiderarUnidadeEnsinoFinanceira(Boolean considerarUnidadeEnsinoFinanceira) {
		this.considerarUnidadeEnsinoFinanceira = considerarUnidadeEnsinoFinanceira;
	}
	
	public Boolean getIsApresentarBotaoSelecionarUnidadeEnsino() {
		return getConsiderarUnidadeEnsinoFinanceira() ? Boolean.TRUE : !(Uteis.isAtributoPreenchido(getTurma().getIdentificadorTurma()) || Uteis.isAtributoPreenchido(getMatricula().getMatricula()));
	}
	
	private void considerarFiltrarUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		if (!getConsiderarUnidadeEnsinoFinanceira()) {
			getUnidadeEnsinoVOs().forEach(unidadeEnsinoVO->{
				unidadeEnsinoVO.setFiltrarUnidadeEnsino(unidadeEnsinoVO.getCodigo().equals(unidadeEnsino.getCodigo()));			
			});
			getUnidadeEnsinoVO().setNome(unidadeEnsino.getNome());
		}
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
	
	public List<SelectItem> getListaSelectItemFiltroRegistroCobranca() {
		if(listaSelectItemFiltroRegistroCobranca == null){
			listaSelectItemFiltroRegistroCobranca = new ArrayList<SelectItem>();
			listaSelectItemFiltroRegistroCobranca = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(FiltroRelatorioFinanceiroVO.FiltroRegistroCobranca.class, true);
		}
		return listaSelectItemFiltroRegistroCobranca;
	}
	public void setListaSelectItemFiltroRegistroCobranca(List<SelectItem> listaSelectItemFiltroRegistroCobranca) {
		this.listaSelectItemFiltroRegistroCobranca = listaSelectItemFiltroRegistroCobranca;
	}
	
	
	public String getSituacaoRegistroCobranca() {
		if(situacaoRegistroCobranca == null) {
			situacaoRegistroCobranca = "";			
		}
		return situacaoRegistroCobranca;
	}
	public void setSituacaoRegistroCobranca(String situacaoRegistroCobranca) {
		this.situacaoRegistroCobranca = situacaoRegistroCobranca;
	}

	private String realizarAlteracaoCaminhoImagemCimaBaixoMensagemApresentar(String mensagem) {
		String mensagemRetorno = mensagem;
		if (Uteis.isAtributoPreenchido(mensagemRetorno)) {
			if (mensagemRetorno.contains("../resources/imagens/email/cima_sei.jpg")) {
				mensagemRetorno = mensagemRetorno.replace("../resources/imagens/email/cima_sei.jpg", "../../resources/imagens/email/cima_sei.jpg");
			}
			if (mensagemRetorno.contains("../resources/imagens/email/baixo_sei.jpg")) {
				mensagemRetorno = mensagemRetorno.replace("../resources/imagens/email/baixo_sei.jpg", "../../resources/imagens/email/baixo_sei.jpg");
			}
		}
		return mensagemRetorno;
	}
	
	public String getTagsUtilizarMensagemCobranca() {
		if (tagsUtilizarMensagemCobranca == null) {
			tagsUtilizarMensagemCobranca = "";
		}
		return tagsUtilizarMensagemCobranca;
	}

	public void setTagsUtilizarMensagemCobranca(String tagsUtilizarMensagemCobranca) {
		this.tagsUtilizarMensagemCobranca = tagsUtilizarMensagemCobranca;
	}
}