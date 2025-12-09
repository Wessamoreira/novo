package controle.contabil;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoAdministrativoEnum;
import negocio.comuns.contabil.FechamentoMesVO;
import negocio.comuns.contabil.IntegracaoContabilVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.contabil.enumeradores.TipoGeracaoIntegracaoContabilEnum;
import negocio.comuns.financeiro.MovimentacaoFinanceiraVO;
import negocio.comuns.financeiro.NegociacaoContaPagarVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

/**
 * 
 * @author PedroOtimize
 *
 */
@Controller("IntegracaoContabilControle")
@Scope("viewScope")
@Lazy
public class IntegracaoContabilControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4995685103275677774L;
	private IntegracaoContabilVO integracaoContabilVO;
	private LancamentoContabilVO lancamentoContabilVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemCodigoContabil;
	private String campoNavegacaoOrigem;
	private String existePeriodoFechamentoAberto;

	public IntegracaoContabilControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		getControleConsulta().setCampoConsulta("dataGeracao");
		getIntegracaoContabilVO().setDataInicio(new Date());
		getIntegracaoContabilVO().setDataTermino(new Date());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>PlanoConta</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setIntegracaoContabilVO(new IntegracaoContabilVO());
		setLancamentoContabilVO(new LancamentoContabilVO());
		getIntegracaoContabilVO().setDataInicio(new Date());
		getIntegracaoContabilVO().setDataTermino(new Date());
		getIntegracaoContabilVO().setDataGeracao(new Date());
		getIntegracaoContabilVO().setTipoGeracaoIntegracaoContabilEnum(TipoGeracaoIntegracaoContabilEnum.UNIDADE_ENSINO);
		getIntegracaoContabilVO().setResponsavel(getUsuarioLogadoClone());
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemCodigoContabil();
		setExistePeriodoFechamentoAberto("");
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("integracaoContabilForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>PlanoConta</code> para alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			IntegracaoContabilVO obj = (IntegracaoContabilVO) context().getExternalContext().getRequestMap().get("integracaoContabilVOItens");
			setIntegracaoContabilVO(getFacadeFactory().getIntegracaoContabilFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			montarListaSelectItemUnidadeEnsino();
			montarListaSelectItemCodigoContabil();
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("integracaoContabilForm.xhtml");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>PlanoConta</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public void persistir() {
		try {
			getFacadeFactory().getIntegracaoContabilFacade().persistir(getIntegracaoContabilVO(), true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarLancamentoContabilDisponivelParaIntegracao() {
		try {
			getFacadeFactory().getIntegracaoContabilFacade().consultarLancamentoContabilDisponivelParaIntegracao(getIntegracaoContabilVO(), getUsuarioLogado());
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAdministrativoEnum.INTEGRACAO_CONTABIL_PERMITIR_APENAS_COM_PERIODO_FECHAMENTO_MES, getUsuarioLogado());
				List<FechamentoMesVO> lista = getFacadeFactory().getFechamentoMesFacade().consultarCompetenciaEmAbertoPorPeriodoPorUnidadeEnsinoPorCodigoIntegracaoContabil(getIntegracaoContabilVO().getDataInicio(), getIntegracaoContabilVO().getDataTermino(), getIntegracaoContabilVO().getUnidadeEnsinoVO().getCodigo(), getIntegracaoContabilVO().getCodigoIntegracaoContabil());
				setExistePeriodoFechamentoAberto("");
				for (FechamentoMesVO fechamentoMesVO : lista) {
					setExistePeriodoFechamentoAberto("Não foi encontrado o Fechamento da Competência para o Mês/Ano - "+fechamentoMesVO.getMes_Apresentar() + "/"+ fechamentoMesVO.getAno_Apresentar()+". "+ System.lineSeparator() + getExistePeriodoFechamentoAberto());	
				}
			} catch (Exception e) {
				//Caso de sucesso
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP PlanoContaCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	public void consultarDados() {
		try {
			super.consultar();
			List<IntegracaoContabilVO> objs = new ArrayList<IntegracaoContabilVO>();
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getIntegracaoContabilFacade().consultaRapidaPorCodigo(valorInt, getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("lote")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getIntegracaoContabilFacade().consultaRapidaPorLote(valorInt, getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getIntegracaoContabilFacade().consultaRapidaPorNomeUnidadeEnsino(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("responsavel")) {
				objs = getFacadeFactory().getIntegracaoContabilFacade().consultaRapidaPorResponsavel(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataGeracao")) {
				objs = getFacadeFactory().getIntegracaoContabilFacade().consultaRapidaPorDataGeracao(getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("periodoContabil")) {
				objs = getFacadeFactory().getIntegracaoContabilFacade().consultaRapidaPorPeriodoContabil(getIntegracaoContabilVO().getDataInicio(), getIntegracaoContabilVO().getDataTermino(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>PlanoContaVO</code> Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getIntegracaoContabilFacade().excluir(getIntegracaoContabilVO(), true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			novo();
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("integracaoContabilForm.xhtml");
	}

	public void visualizarLancamentoContabil() {
		try {
			LancamentoContabilVO obj = (LancamentoContabilVO) context().getExternalContext().getRequestMap().get("lancamentoContabilItens");
			setLancamentoContabilVO(obj);
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}
	
	
	public void realizarNavegacaoParaOrigem() {
		try {
			setCampoNavegacaoOrigem("");
			LancamentoContabilVO lc = (LancamentoContabilVO) context().getExternalContext().getRequestMap().get("lancamentoContabilItens");
			if (lc.getTipoOrigemLancamentoContabilEnum().isReceber()) {
				carregarDadosContaReceber(lc);
			} else if (lc.getTipoOrigemLancamentoContabilEnum().isPagar()) {
				carregarDadosContaPagar(lc);
			} else if (lc.getTipoOrigemLancamentoContabilEnum().isNotaFiscalEntrada()) {
				carregarDadosNotaFiscalEntrada(lc);
			} else if (lc.getTipoOrigemLancamentoContabilEnum().isMovimentacaoFinanceira()) {
				carregarDadosMovFinanceira(lc);
			} else if (lc.getTipoOrigemLancamentoContabilEnum().isNegocicaoContaPagar()) {
				carregarDadosNegociacaoContaPagar(lc);
			} else if (lc.getTipoOrigemLancamentoContabilEnum().isCartaoCredito()) {
				carregarDadosMapaPendenciaCartao(lc);
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setCampoNavegacaoOrigem("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	private void carregarDadosContaReceber(LancamentoContabilVO lc)  {
		context().getExternalContext().getSessionMap().put("contaReceberFichaAluno", lc.getContaReceberVO());
		setCampoNavegacaoOrigem("popup('../financeiro/contaReceberForm.xhtml', 'contaReceberForm' , 1024, 800)");
		removerControleMemoriaFlash("ContaReceberControle");
		removerControleMemoriaTela("ContaReceberControle");
	}

	private void carregarDadosContaPagar(LancamentoContabilVO lc)  {
		context().getExternalContext().getSessionMap().put("contaPagarLancamentoContabil", lc.getContaPagarVO());
		setCampoNavegacaoOrigem("popup('../financeiro/contaPagarForm.xhtml', 'contaPagarForm' , 1024, 800)");
		removerControleMemoriaFlash("ContaPagarControle");
		removerControleMemoriaTela("ContaPagarControle");
	}

	private void carregarDadosNotaFiscalEntrada(LancamentoContabilVO lc)  {
		context().getExternalContext().getSessionMap().put("notaFiscalEntrada", lc.getNotaFiscalEntradaVO());
		setCampoNavegacaoOrigem("popup('../notaFiscal/notaFiscalEntradaForm.xhtml', 'notaFiscalEntradaForm' , 1024, 800)");
		removerControleMemoriaFlash("NotaFiscalEntradaControle");
		removerControleMemoriaTela("NotaFiscalEntradaControle");
	}

	private void carregarDadosMovFinanceira(LancamentoContabilVO lc)  {
		MovimentacaoFinanceiraVO mf = new MovimentacaoFinanceiraVO();
		mf.setCodigo(Integer.parseInt(lc.getCodOrigem()));
		context().getExternalContext().getSessionMap().put("movFinanceira", mf);
		setCampoNavegacaoOrigem("popup('../financeiro/movimentacaoFinanceiraForm.xhtml', 'movimentacaoFinanceiraForm' , 1024, 800)");
		removerControleMemoriaFlash("MovimentacaoFinanceiraControle");
		removerControleMemoriaTela("MovimentacaoFinanceiraControle");
	}
	
	private void carregarDadosNegociacaoContaPagar(LancamentoContabilVO lc) {
		NegociacaoContaPagarVO ncp = new NegociacaoContaPagarVO();
		ncp.setCodigo(Integer.parseInt(lc.getCodOrigem()));
		context().getExternalContext().getSessionMap().put("negociacaoContaPagar", ncp);
		setCampoNavegacaoOrigem("popup('../financeiro/negociacaoContaPagarForm.xhtml', 'negociacaoContaPagarForm' , 1024, 800)");
		removerControleMemoriaFlash("NegociacaoContaPagarControle");
		removerControleMemoriaTela("NegociacaoContaPagarControle");
	}
	
	private void carregarDadosMapaPendenciaCartao(LancamentoContabilVO lc) {
		context().getExternalContext().getSessionMap().put("formapagamentonegociacaorecebimentocartaocredito", lc.getCodOrigem());
		setCampoNavegacaoOrigem("popup('../financeiro/mapaPendenciaCartaoCreditoCons.xhtml', 'mapaPendenciaCartaoCreditoCons' , 1024, 800)");
		removerControleMemoriaFlash("MapaPendenciaCartaoCreditoControle");
		removerControleMemoriaTela("MapaPendenciaCartaoCreditoControle");
	}

	public void downloadArquivoContabil() {
		try {
			preencherDownloadArquivoContabil(getIntegracaoContabilVO());
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void downloadArquivoContabilLote() {
		try {
			IntegracaoContabilVO obj = (IntegracaoContabilVO) context().getExternalContext().getRequestMap().get("integracaoContabilVOItens");
			obj = getFacadeFactory().getIntegracaoContabilFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			preencherDownloadArquivoContabil(obj);
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private void preencherDownloadArquivoContabil(IntegracaoContabilVO obj) throws Exception {
		HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
		request.getSession().setAttribute("nomeArquivo", obj.getArquivo().getNome());
		request.getSession().setAttribute("pastaBaseArquivo", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + obj.getArquivo().getPastaBaseArquivo());
		request.getSession().setAttribute("deletarArquivo", false);
		context().getExternalContext().dispatch("/DownloadSV");
		FacesContext.getCurrentInstance().responseComplete();
	}

	public void irPaginaInicial() throws Exception {
		this.consultar();
		getControleConsulta().setCampoConsulta("periodoContabil");
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

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				setListaSelectItemUnidadeEnsino(new ArrayList<SelectItem>());
				getListaSelectItemUnidadeEnsino().add(new SelectItem(0, ""));
				UnidadeEnsinoVO obj = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				getIntegracaoContabilVO().setUnidadeEnsinoVO(obj);
				getListaSelectItemUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome()));
				return;
			}
			List resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemCodigoContabil() {
		try {
			List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarRapidaTodosCodigoContabilUnidadeEnsino(false, getUsuarioLogado());
			getListaSelectItemCodigoContabil().clear();
			getListaSelectItemCodigoContabil().add(new SelectItem("", ""));
			Map<String, List<UnidadeEnsinoVO>> mapaCodigoContabil = lista.stream()
					.filter(p -> Uteis.isAtributoPreenchido(p.getCodigoIntegracaoContabil()))
					.collect(Collectors.groupingBy(p -> p.getCodigoIntegracaoContabil()));

			Map<String, List<UnidadeEnsinoVO>> result = mapaCodigoContabil.entrySet().stream()
					.sorted(Map.Entry.comparingByKey())
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

			for (Map.Entry<String, List<UnidadeEnsinoVO>> map : result.entrySet()) {
				StringBuilder sb = new StringBuilder();
				for (UnidadeEnsinoVO unidadeEnsino : map.getValue()) {
					sb.append(unidadeEnsino.getNome()).append("; ");
				}
				getListaSelectItemCodigoContabil().add(new SelectItem(map.getKey(), map.getKey() + " - " + sb.toString().substring(0, sb.toString().length() - 2)));
				sb = null;
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> tipoConsultaCombo;
	public List<SelectItem> tipoCampoPlanoContaFiltroCombo;

	public List<SelectItem> getTipoConsultaCombo() {
		if (tipoConsultaCombo == null) {
			tipoConsultaCombo = new ArrayList<SelectItem>(0);
			tipoConsultaCombo.add(new SelectItem("periodoContabil", "Período Contábil"));
			tipoConsultaCombo.add(new SelectItem("unidadeEnsino", "Unidade Ensino"));
			tipoConsultaCombo.add(new SelectItem("lote", "Lote"));
			tipoConsultaCombo.add(new SelectItem("responsavel", "Responsável"));
			tipoConsultaCombo.add(new SelectItem("dataGeracao", "Data de Geração"));
			tipoConsultaCombo.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaCombo;
	}

	public List<SelectItem> getTipoCampoPlanoContaFiltroCombo() {
		if (tipoCampoPlanoContaFiltroCombo == null) {
			tipoCampoPlanoContaFiltroCombo = new ArrayList<SelectItem>(0);
			tipoCampoPlanoContaFiltroCombo.add(new SelectItem("descricao", "Descrição"));
			tipoCampoPlanoContaFiltroCombo.add(new SelectItem("codigoReduzido", "Código Reduzido"));
			tipoCampoPlanoContaFiltroCombo.add(new SelectItem("identificador", "Identificador"));
		}
		return tipoCampoPlanoContaFiltroCombo;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		getListaConsulta().clear();
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("integracaoContabilCons.xhtml");
	}

	public IntegracaoContabilVO getIntegracaoContabilVO() {
		if (integracaoContabilVO == null) {
			integracaoContabilVO = new IntegracaoContabilVO();
		}
		return integracaoContabilVO;
	}

	public void setIntegracaoContabilVO(IntegracaoContabilVO integracaoContabilVO) {
		this.integracaoContabilVO = integracaoContabilVO;
	}

	public LancamentoContabilVO getLancamentoContabilVO() {
		if (lancamentoContabilVO == null) {
			lancamentoContabilVO = new LancamentoContabilVO();
		}
		return lancamentoContabilVO;
	}

	public void setLancamentoContabilVO(LancamentoContabilVO lancamentoContabilVO) {
		this.lancamentoContabilVO = lancamentoContabilVO;
	}

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<SelectItem> getListaSelectItemCodigoContabil() {
		if (listaSelectItemCodigoContabil == null) {
			listaSelectItemCodigoContabil = new ArrayList(0);
		}
		return listaSelectItemCodigoContabil;
	}

	public void setListaSelectItemCodigoContabil(List<SelectItem> listaSelectItemCodigoContabil) {
		this.listaSelectItemCodigoContabil = listaSelectItemCodigoContabil;
	}

	public String getCampoNavegacaoOrigem() {
		if (campoNavegacaoOrigem == null) {
			campoNavegacaoOrigem = "";
		}
		return campoNavegacaoOrigem;
	}

	public void setCampoNavegacaoOrigem(String campoNavegacaoOrigem) {
		this.campoNavegacaoOrigem = campoNavegacaoOrigem;
	}

	public boolean getIsConsultaPorDataGeracaoSelecionado() {
		return getControleConsulta().getCampoConsulta().equals("dataGeracao");
	}

	public boolean getIsConsultaPorPeriodoContabil() {
		return getControleConsulta().getCampoConsulta().equals("periodoContabil");
	}

	public boolean isApresentarPeriodoFechamentoAberto() {
		return Uteis.isAtributoPreenchido(getExistePeriodoFechamentoAberto());
	}

	public String getExistePeriodoFechamentoAberto() {
		if (existePeriodoFechamentoAberto == null) {
			existePeriodoFechamentoAberto = "";
		}
		return existePeriodoFechamentoAberto;
	}

	public void setExistePeriodoFechamentoAberto(String existePeriodoFechamentoAberto) {
		this.existePeriodoFechamentoAberto = existePeriodoFechamentoAberto;
	}

	
	
	
	
	
	
	

}
