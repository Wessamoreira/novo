package controle.faturamento.nfe;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.faturamento.nfe.IntegracaoGinfesAlunoItemVO;
import negocio.comuns.faturamento.nfe.IntegracaoGinfesAlunoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;

@Controller("IntegracaoGinfesAlunoControle")
@Scope("viewScope")
@Lazy
public class IntegracaoGinfesAlunoControle extends SuperControleRelatorio implements Serializable {

	private static final long serialVersionUID = 1L;
	private IntegracaoGinfesAlunoVO integracaoVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private Integer codigoUnidadeEnsino;
	private Boolean naoImportados;
	private String nomeArquivoImportado;
	private String anoReferencia;
	private String mesReferencia;
	private IntegracaoGinfesAlunoItemVO integracaoGinfesAlunoItemVO;
    private Boolean trazerBotaoTabAlunoComErro;
	public IntegracaoGinfesAlunoControle() throws Exception {
		setControleConsultaOtimizado(new DataModelo());		
		getControleConsultaOtimizado().setCampoConsulta("nome");
		setFiltroRelatorioFinanceiroVO(new FiltroRelatorioFinanceiroVO());
		setMensagemID("msg_entre_prmconsulta");
	}

	public String novo() {
		try {
			setIntegracaoVO(new IntegracaoGinfesAlunoVO());
			getIntegracaoVO().setDataImportacao(null);
			setAnoReferencia("");		
			setMensagemID("msg_entre_dados");
			getIntegracaoVO().setDescontoIncondicional(true);
			getIntegracaoVO().setDescontoCondicional(true);
			getIntegracaoVO().setTrazerValorServicoContaReceber(true);
			verificarLayoutPadrao();	
		} catch (Exception e) {
			setMensagemDetalhada("msgo_erro", e.getMessage());
		}
		
		return Uteis.getCaminhoRedirecionamentoNavegacao("integracaoGinfesAlunoForm");
	}

	public String editar() {
		try {
			IntegracaoGinfesAlunoVO obj = (IntegracaoGinfesAlunoVO) context().getExternalContext().getRequestMap().get("item");
			setIntegracaoVO(getFacadeFactory().getIntegracaoGinfesAlunoFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msgo_erro", e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("integracaoGinfesAlunoForm");
	}

	public void persistir() {
		try {
			if (getIntegracaoVO().getImportado()) {
				throw new Exception("Este cadastro já foi marcado como importado!");
			}
			getFacadeFactory().getIntegracaoGinfesAlunoFacade().persistir(getIntegracaoVO(), true, getUsuarioLogado());
			persistirLayoutPadrao();
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	private void persistirLayoutPadrao() throws Exception {
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getIntegracaoVO().getAnoReferencia(), "IntegracaoGinfesAluno", "anoReferencia", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getIntegracaoVO().getMesReferencia(), "IntegracaoGinfesAluno", "mesReferencia", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getIntegracaoVO().getDescontoIncondicional().toString(), "IntegracaoGinfesAluno", "descontoIncondicional", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getIntegracaoVO().getDescontoCondicional().toString(), "IntegracaoGinfesAluno", "descontoCondicional", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getIntegracaoVO().getTrazerValorServicoContaReceber().toString(), "IntegracaoGinfesAluno", "trazerValorServicoContaReceber", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getIntegracaoVO().getSituacaoContaReceber(), "IntegracaoGinfesAluno", "situacaoContaReceber", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirFiltroTipoOrigemContaReceber(getFiltroRelatorioFinanceiroVO(), "IntegracaoGinfesAluno", getUsuarioLogado());
	}

	private void verificarLayoutPadrao() throws Exception {
		Map<String, String> dadosPadroes = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[] { "anoReferencia", "mesReferencia", "descontoIncondicional", "descontoCondicional", "trazerValorServicoContaReceber", "situacaoContaReceber" }, "IntegracaoGinfesAluno");
		for (String key : dadosPadroes.keySet()) {
			if (key.equals("anoReferencia")) {
				getIntegracaoVO().setAnoReferencia(dadosPadroes.get(key));
			} else if (key.equals("mesReferencia")) {
				getIntegracaoVO().setMesReferencia(dadosPadroes.get(key));
			} else if (key.equals("descontoIncondicional")) {
				getIntegracaoVO().setDescontoIncondicional(Boolean.valueOf(dadosPadroes.get(key)));
			} else if (key.equals("descontoCondicional")) {
				getIntegracaoVO().setDescontoCondicional(Boolean.valueOf(dadosPadroes.get(key)));
			} else if (key.equals("trazerValorServicoContaReceber")) {
				getIntegracaoVO().setTrazerValorServicoContaReceber(Boolean.valueOf(dadosPadroes.get(key)));
			} else if (key.equals("situacaoContaReceber")) {
				getIntegracaoVO().setSituacaoContaReceber(dadosPadroes.get(key));
			}
		}
		dadosPadroes = null;
		getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroTipoOrigemContaReceber(getFiltroRelatorioFinanceiroVO(), "IntegracaoGinfesAluno", getUsuarioLogado());
	}

	public void executarDefinicaoComoImportado() {
		try {
			if (getIntegracaoVO().getImportado()) {
				throw new Exception("Este cadastro já foi marcado como importado!");
			}
			if (getIntegracaoVO().getAlunos().size() < 1) {
				throw new Exception("Importação não possui nenhum aluno!");
			}
			getIntegracaoVO().setImportado(true);
			getIntegracaoVO().setDataImportacao(new Date());
			getFacadeFactory().getIntegracaoGinfesAlunoFacade().importar(getIntegracaoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			getIntegracaoVO().setImportado(false);
			getIntegracaoVO().setDataImportacao(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void executarGeracaoAlunos() {	
		try {
			if (getIntegracaoVO().getUnidadeEnsino().getCodigo().intValue() == 0) {
				throw new Exception("Informe a Unidade de Ensino!");
			}
			if (getIntegracaoVO().getImportado()) {
				throw new Exception("Este cadastro já foi marcado como importado!");
			}
			if (getIntegracaoVO().getAnoReferencia().trim().isEmpty()) {
				throw new Exception("Informe o Ano de Referência");
			}
			Map<String, List<IntegracaoGinfesAlunoItemVO>> resultado = getFacadeFactory().getIntegracaoGinfesAlunoItemFacade().gerarAlunos(getIntegracaoVO(),"",getFiltroRelatorioFinanceiroVO(),getUsuarioLogado());

			getIntegracaoVO().setAlunos(resultado.get(Uteis.SUCESSO));
			getIntegracaoVO().setAlunosErro(resultado.get(Uteis.ERRO));
			setTrazerBotaoTabAlunoComErro(true);			
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getDownloadXML() {
		if (getFazerDownload()) {
			setFazerDownload(false);
			try {
				HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
				String caminho = request.getRequestURL().toString().replace(request.getRequestURI().toString(), "")
						+ request.getContextPath() + "/";
				return "location.href='" + caminho + "DownloadRelatorioSV?relatorio=" + getNomeArquivoImportado() + "'";
			} catch (Exception ex) {
				Logger.getLogger(SuperControleRelatorio.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return "";
	}

	public void executarDownload() {
		try {
			setNomeArquivoImportado(getFacadeFactory().getIntegracaoGinfesAlunoFacade()
					.executarGeracaoArquivoImportacao(getIntegracaoVO(), 50, getUsuarioLogado()));
			setFazerDownload(true);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	
	public void realizarGeracaoRelatorioExcelIntegracaoGinfesAluno()  {
		try {
			File arquivo = getFacadeFactory().getIntegracaoGinfesAlunoFacade().realizarGeracaoRelatorioExcelIntegracaoGinfesAluno(getIntegracaoVO(), getLogoPadraoRelatorio(), false, getUsuarioLogado());
			setCaminhoRelatorio(arquivo.getName());
			setFazerDownload(true);
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
	}
	
	public void realizarGeracaoRelatorioExcelIntegracaoGinfesAlunoErro()  {
		try {
			File arquivo = getFacadeFactory().getIntegracaoGinfesAlunoFacade().realizarGeracaoRelatorioExcelIntegracaoGinfesAluno(getIntegracaoVO(), getLogoPadraoRelatorio(), true, getUsuarioLogado());
			setCaminhoRelatorio(arquivo.getName());
			setFazerDownload(true);
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
	}

	public String excluir() {
		try {
			getFacadeFactory().getIntegracaoGinfesAlunoFacade().excluir(getIntegracaoVO(), true, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("integracaoGinfesAlunoForm");
	}

	public void removerAluno() {
		try {
			IntegracaoGinfesAlunoItemVO obj = (IntegracaoGinfesAlunoItemVO) context().getExternalContext()
					.getRequestMap().get("a");
			getIntegracaoVO().getAlunos().remove(obj);
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerAlunoErro() {
		try {
			IntegracaoGinfesAlunoItemVO obj = (IntegracaoGinfesAlunoItemVO) context().getExternalContext()
					.getRequestMap().get("a");
			getIntegracaoVO().getAlunosErro().remove(obj);
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void editarAluno() {
		try {
			setIntegracaoGinfesAlunoItemVO(
					(IntegracaoGinfesAlunoItemVO) context().getExternalContext().getRequestMap().get("a"));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}

	public void atualizarDadosAluno() {
		try {
			setIntegracaoGinfesAlunoItemVO((IntegracaoGinfesAlunoItemVO) context().getExternalContext().getRequestMap().get("a"));
			getFacadeFactory().getIntegracaoGinfesAlunoItemFacade().realizarAtualizacaoAluno(getIntegracaoVO(),getIntegracaoGinfesAlunoItemVO(), getFiltroRelatorioFinanceiroVO(), getUsuarioLogado());
			setIntegracaoGinfesAlunoItemVO(new IntegracaoGinfesAlunoItemVO());
			setMensagemID("msg_dados_atualizados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void persistirIntegracaoGinfesAlunoItem() {
		try {
			IntegracaoGinfesAlunoItemVO igai = (IntegracaoGinfesAlunoItemVO) context().getExternalContext().getRequestMap().get("a");
			getFacadeFactory().getIntegracaoGinfesAlunoItemFacade().persistirIntegracaoGinfesAlunoItem(igai, false, getUsuarioLogadoClone());		
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String consultar() {
		try {
			if (getAnoReferencia().trim().isEmpty()) {
				throw new Exception("Informe o Ano de Referência");
			}
			super.consultar();
			List<IntegracaoGinfesAlunoVO> objs = getFacadeFactory().getIntegracaoGinfesAlunoFacade().consultar(
					getCodigoUnidadeEnsino(), getAnoReferencia(), getMesReferencia(), getNaoImportados(), true,
					getUsuarioLogado());
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return "";
		} catch (Exception e) {
			setListaConsulta(new ArrayList<IntegracaoGinfesAlunoVO>());
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	@SuppressWarnings("rawtypes")
	public String inicializarConsultar() {
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("integracaoGinfesAlunoCons");
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultar();
	}

	public void irPaginaInicial() throws Exception {
		this.consultar();
	}

	public void irPaginaAnterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
		this.consultar();
	}

	public void irPaginaPosterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
		this.consultar();
	}

	public void irPaginaFinal() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();
	}

	public IntegracaoGinfesAlunoVO getIntegracaoVO() {
		if (integracaoVO == null) {
			integracaoVO = new IntegracaoGinfesAlunoVO();
		}
		return integracaoVO;
	}

	public void setIntegracaoVO(IntegracaoGinfesAlunoVO integracaoVO) {
		this.integracaoVO = integracaoVO;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			montarListaSelectItemUnidadeEnsino();
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public void montarListaSelectItemUnidadeEnsino() {
		List<SelectItem> objs = new ArrayList<>();
		objs.add(new SelectItem(0, ""));
		try {
			List<UnidadeEnsinoVO> lista = consultarUnidadeEnsinoPorNome(""); 
			for (UnidadeEnsinoVO obj : lista) {
				objs.add(new SelectItem(obj.getCodigo().toString(), obj.getNome() + " - " + obj.getAbreviatura()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		setListaSelectItemUnidadeEnsino(objs);
	}

	private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm,
				super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
				getUsuarioLogado());
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

	public Boolean getNaoImportados() {
		if (naoImportados == null) {
			naoImportados = false;
		}
		return naoImportados;
	}

	public void setNaoImportados(Boolean naoImportados) {
		this.naoImportados = naoImportados;
	}	

	public String getNomeArquivoImportado() {
		if (nomeArquivoImportado == null) {
			nomeArquivoImportado = "";
		}
		return nomeArquivoImportado;
	}

	public void setNomeArquivoImportado(String nomeArquivoImportado) {
		this.nomeArquivoImportado = nomeArquivoImportado;
	}

	public String getAnoReferencia() {
		if (anoReferencia == null) {
			anoReferencia = "";
		}
		return anoReferencia;
	}

	public void setAnoReferencia(String anoReferencia) {
		this.anoReferencia = anoReferencia;
	}

	public String getMesReferencia() {
		if (mesReferencia == null) {
			mesReferencia = "";
		}
		return mesReferencia;
	}

	public void setMesReferencia(String mesReferencia) {
		this.mesReferencia = mesReferencia;
	}

	public List<SelectItem> getListaSelectItemMeses() {
		List<SelectItem> lista = new ArrayList<>();
		lista.add(new SelectItem("01", "JANEIRO"));
		lista.add(new SelectItem("02", "FEVEREIRO"));
		lista.add(new SelectItem("03", "MARÇO"));
		lista.add(new SelectItem("04", "ABRIL"));
		lista.add(new SelectItem("05", "MAIO"));
		lista.add(new SelectItem("06", "JUNHO"));
		lista.add(new SelectItem("07", "JULHO"));
		lista.add(new SelectItem("08", "AGOSTO"));
		lista.add(new SelectItem("09", "SETEMBRO"));
		lista.add(new SelectItem("10", "OUTUBRO"));
		lista.add(new SelectItem("11", "NOVEMBRO"));
		lista.add(new SelectItem("12", "DEZEMBRO"));
		return lista;
	}

	public List<SelectItem> getListaSelectSituacaoContaReceber() {
		List<SelectItem> lista = new ArrayList<>();
		lista.add(new SelectItem("AMBAS", "Ambas"));
		lista.add(new SelectItem(SituacaoContaReceber.A_RECEBER.getValor(),
				SituacaoContaReceber.A_RECEBER.getDescricao()));
		lista.add(
				new SelectItem(SituacaoContaReceber.RECEBIDO.getValor(), SituacaoContaReceber.RECEBIDO.getDescricao()));

		return lista;
	}

	public void imprimirPDF() {
		List<IntegracaoGinfesAlunoItemVO> listaObjetos = new ArrayList<>(0);
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "IntegracaoGinfesAlunoControle",
					"Inicializando Geração de Relatório Importação Ginfes - Aluno", "Emitindo Relatório");
			listaObjetos = getIntegracaoVO().getAlunos();
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(getDesignIReportRelatorioPDF());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(getCaminhoBaseRelatorioPDF());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório Ginfes Alunos");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getCaminhoBaseRelatorioPDF());
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");
				getSuperParametroRelVO().adicionarParametro("mesReferencia", getIntegracaoVO().getMesReferencia());
				getSuperParametroRelVO().adicionarParametro("anoReferencia", getIntegracaoVO().getAnoReferencia());
				getSuperParametroRelVO().adicionarParametro("incondicional",
						getIntegracaoVO().getDescontoIncondicional());
				getSuperParametroRelVO().adicionarParametro("condicional", getIntegracaoVO().getDescontoCondicional());
				UnidadeEnsinoVO ue = getFacadeFactory().getUnidadeEnsinoFacade()
						.consultaRapidaPorChavePrimariaDadosBasicosBoleto(
								getIntegracaoVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado());
				getSuperParametroRelVO().setUnidadeEnsino(ue.getNome());

				if (getIntegracaoVO().getSituacaoContaReceber().equals("AMBAS")) {
					getSuperParametroRelVO().adicionarParametro("situacaoContaReceber", "AMBAS");
				}
				if (getIntegracaoVO().getSituacaoContaReceber().equals(SituacaoContaReceber.A_RECEBER.getValor())) {
					getSuperParametroRelVO().adicionarParametro("situacaoContaReceber",
							SituacaoContaReceber.A_RECEBER.getDescricao());
				}
				if (getIntegracaoVO().getSituacaoContaReceber().equals(SituacaoContaReceber.RECEBIDO.getValor())) {
					getSuperParametroRelVO().adicionarParametro("situacaoContaReceber",
							SituacaoContaReceber.RECEBIDO.getDescricao());
				}
				
				
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "IntegracaoGinfesAlunoControle",
					"Finalizando Geração de Relatório Importação Ginfes - Aluno", "Emitindo Relatório");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public static String getCaminhoBaseRelatorioPDF() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "faturamento" + File.separator
				+ "nfe" + File.separator);
	}

	public static String getDesignIReportRelatorioPDF() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "faturamento" + File.separator
				+ "nfe" + File.separator + "relatorioIntegracoGinfesAluno.jrxml");
	}

	public IntegracaoGinfesAlunoItemVO getIntegracaoGinfesAlunoItemVO() {
		if (integracaoGinfesAlunoItemVO == null) {
			integracaoGinfesAlunoItemVO = new IntegracaoGinfesAlunoItemVO();
		}
		return integracaoGinfesAlunoItemVO;
	}

	public void setIntegracaoGinfesAlunoItemVO(IntegracaoGinfesAlunoItemVO integracaoGinfesAlunoItemVO) {
		this.integracaoGinfesAlunoItemVO = integracaoGinfesAlunoItemVO;
	}
	public void esconderBotoesParaTelaComErros() {		
		setTrazerBotaoTabAlunoComErro(false);
	}
	public void mostrarBotoesParaTelaComErros() {		
		setTrazerBotaoTabAlunoComErro(true);
	}

	public Boolean getTrazerBotaoTabAlunoComErro() {
		if(trazerBotaoTabAlunoComErro == null) {
			trazerBotaoTabAlunoComErro = false;			
		}
		return trazerBotaoTabAlunoComErro;
	}

	public void setTrazerBotaoTabAlunoComErro(Boolean trazerBotaoTabAlunoComErro) {
		this.trazerBotaoTabAlunoComErro = trazerBotaoTabAlunoComErro;
	}
	
	

}
