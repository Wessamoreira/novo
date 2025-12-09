package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas fluxoCaixaForm.jsp fluxoCaixaCons.jsp) com as funcionalidades da classe <code>FluxoCaixa</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see FluxoCaixa
 * @see FluxoCaixaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.FechamentoMesHistoricoModificacaoVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.FluxoCaixaVO;
import negocio.comuns.financeiro.MovimentacaoCaixaVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Controller("FluxoCaixaControle")
@Scope("viewScope")
@Lazy
public class FluxoCaixaControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = -5154062256610636191L;

	private FluxoCaixaVO fluxoCaixaVO;
	private List<SelectItem> listaSelectItemContaCaixa;
	private Date dataInicioConsultar;	
	private List<MovimentacaoCaixaVO> movimentacaoCaixaChequeVOs;

	public FluxoCaixaControle() throws Exception {
		// obterUsuarioLogado();
		this.setDataInicioConsultar(new Date());
		setControleConsulta(new ControleConsulta());
		getControleConsulta().setCampoConsulta("A");
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>FluxoCaixa</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setFluxoCaixaVO(new FluxoCaixaVO());
		inicializarListasSelectItemTodosComboBox();
		inicializarResponsavel();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("fluxoCaixaForm.xhtml");
	}

	public void inicializarResponsavel() {
		try {
			getFluxoCaixaVO().setResponsavelAbertura(getUsuarioLogadoClone());
		} catch (Exception e) {
		}
	}

	public void realizarConsultaChequeFluxoCaixa() {
		try {
			setMovimentacaoCaixaChequeVOs(getFacadeFactory().getMovimentacaoCaixaFacade().consultarChequeFluxoCaixaPelaMovimentacaoCaixa(getFluxoCaixaVO().getContaCaixa().getCodigo(), getFluxoCaixaVO().getDataAbertura()));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>FluxoCaixa</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 *
	 * @throws Exception
	 */
	public String editar() {
		FluxoCaixaVO obj = (FluxoCaixaVO) context().getExternalContext().getRequestMap().get("fluxoCaixaItens");
		obj.setNovoObj(Boolean.FALSE);
		try {
			setFluxoCaixaVO(getFacadeFactory().getFluxoCaixaFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));

			getFluxoCaixaVO().setSaldoBoletoBancario(getFacadeFactory().getFluxoCaixaFacade().consultarSaldoBoletoBancario(getFluxoCaixaVO().getCodigo(), getFluxoCaixaVO().getDataAbertura(), getFluxoCaixaVO().getDataFechamento(), getFluxoCaixaVO().getUnidadeEnsino(), false, 0));			
			getListaSelectItemContaCaixa().clear();
			getListaSelectItemContaCaixa().add(new SelectItem(getFluxoCaixaVO().getContaCaixa().getCodigo(), getFluxoCaixaVO().getContaCaixa().getDescricaoCompletaConta()));
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("fluxoCaixaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("fluxoCaixaCons.xhtml");
		}
	}

	public void atualizarDadosSaldoInicial() {
		try {
			getFacadeFactory().getFluxoCaixaFacade().atualizarDadosSaldoInicial(fluxoCaixaVO, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void atualizar() {
		try {
			FluxoCaixaVO obj = getFacadeFactory().getFluxoCaixaFacade().consultarPorChavePrimaria(fluxoCaixaVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			Iterator<MovimentacaoCaixaVO> i = obj.getMovimentacaoCaixaVOs().iterator();
			while (i.hasNext()) {
				MovimentacaoCaixaVO mov = (MovimentacaoCaixaVO) i.next();
				try {
					if (mov.getTipoOrigem().equalsIgnoreCase("MF") && mov.getTipoMovimentacao().equalsIgnoreCase("SA")) {
						MovimentacaoFinanceiraVO movFin = getFacadeFactory().getMovimentacaoFinanceiraFacade().consultarPorChavePrimaria(mov.getCodigoOrigem(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
						mov.setDescricaoMovimentacaoFinanceira("Movimentação financeira realizada da conta origem : " + movFin.getContaCorrenteOrigem().getNumero() + " para a conta destino :" + movFin.getContaCorrenteDestino().getNumero() + ".");
					}
				} catch (Exception e) {
					mov.setDescricaoMovimentacaoFinanceira("");
				}
			}
			Ordenacao.ordenarLista(obj.getMovimentacaoCaixaVOs(), "formaPagamento.tipo");
			setFluxoCaixaVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>FluxoCaixa</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {

			if (getFluxoCaixaVO().getContaCaixa().getContaCaixa() && getFluxoCaixaVO().getContaCaixa().getFuncionarioResponsavel().getCodigo().intValue() != 0) {
				if (getUsuarioLogado().getPessoa().getCodigo().equals(getFluxoCaixaVO().getContaCaixa().getFuncionarioResponsavel().getPessoa().getCodigo())) {
					persistir();
				} else {
					throw new Exception("Você não é o responsável pela abertura ou fechamento desse caixa!");
				}
			} else {
				persistir();
			}

			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("fluxoCaixaForm.xhtml");

		} catch (Exception e) {
			getFluxoCaixaVO().setResponsavelFechamento(new UsuarioVO());
			getFluxoCaixaVO().setDataFechamento(null);
			getFluxoCaixaVO().setSituacao("A");
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("fluxoCaixaForm.xhtml");
		}
	}

	public void persistir() throws Exception {
		if (fluxoCaixaVO.isNovoObj().booleanValue()) {
			getFacadeFactory().getFluxoCaixaFacade().incluir(fluxoCaixaVO, getUsuarioLogado());
		} else {
			fluxoCaixaVO.setResponsavelFechamento(getUsuarioLogadoClone());
			fluxoCaixaVO.setDataFechamento(new Date());
			fluxoCaixaVO.setSituacao("F");
			getFacadeFactory().getFluxoCaixaFacade().fecharCaixa(fluxoCaixaVO, getUsuarioLogado());
		}
	}

	public void imprimirPDF() {
		try {
			executarMetodoControle("FluxoCaixaRelControle", "montarObjetosFluxoCaixaParaRelatorio", getFluxoCaixaVO().getDataAbertura(), new Date(), getFluxoCaixaVO().getUnidadeEnsino(), getFluxoCaixaVO().getContaCaixa().getCodigo(), getFluxoCaixaVO().getResponsavelAbertura().getCodigo());
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * FluxoCaixaCons.jsp. Define o tipo de consulta a ser executada, por meio
	 * de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	@Override
	public void consultarDados() {
		try {
			super.consultar();

			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getControleConsultaOtimizado().setCampoConsulta(getControleConsulta().getCampoConsulta());
			getControleConsultaOtimizado().setValorConsulta(getControleConsulta().getValorConsulta());
			getControleConsultaOtimizado().setDataIni(getControleConsulta().getDataIni());
			getControleConsultaOtimizado().setDataFim(getControleConsulta().getDataFim());
			getFacadeFactory().getFluxoCaixaFacade().consultarPorEnumCampoConsulta(getControleConsultaOtimizado(), getControleConsulta().getSituacao());

			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>FluxoCaixaVO</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getFluxoCaixaFacade().excluir(fluxoCaixaVO, getUsuarioLogado());
			setFluxoCaixaVO(new FluxoCaixaVO());
			inicializarResponsavel();
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("fluxoCaixaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("fluxoCaixaForm.xhtml");
		}
	}

	public void irPaginaInicial() throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(1);
		getControleConsultaOtimizado().setPage(0);
		this.consultarDados();
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

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>ContaCaixa</code>.
	 */
	public void montarListaSelectItemContaCaixa(String prm) throws Exception {
		List<ContaCorrenteVO> resultadoConsulta = null;
		Iterator<ContaCorrenteVO> i = null;
		try {
			resultadoConsulta = consultarContaCorrentePorNumero(prm);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<>(0);
			objs.add(new SelectItem(0, ""));			
			while (i.hasNext()) {
				ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
				
				if (obj.getSituacao().equals("AT")) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getDescricaoCompletaConta()));
				}
			}			
			setListaSelectItemContaCaixa(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>ContaCaixa</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>ContaCorrente</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemContaCaixa() {
		try {
			montarListaSelectItemContaCaixa("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>numero</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	@SuppressWarnings("unchecked")
	public List<ContaCorrenteVO> consultarContaCorrentePorNumero(String numeroPrm) throws Exception {
		return getFacadeFactory().getContaCorrenteFacade().consultarPorContaCaixa(true, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
	}

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {

		montarListaSelectItemContaCaixa();

	}
	
	public void scrollerListener(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultarDados();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public boolean getApresentarResultadoConsulta() {
		return getControleConsultaOtimizado().getListaConsulta().size() > 0;
	}

	public boolean getApresentarPaginador() {
		return getControleConsultaOtimizado().getTotalRegistrosEncontrados() >= 10;
	}

	public List<SelectItem> getTipoComboSituacao() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("A", "Aberto"));
		itens.add(new SelectItem("F", "Fechado"));
		itens.add(new SelectItem("T", "Todas"));
		return itens;
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("CONTA_CAIXA", "Conta Caixa"));
		itens.add(new SelectItem("DATA_ABERTURA", "Data de Abertura"));
		itens.add(new SelectItem("RESPONSAVEL_ABERTURA", "Responsável Abertura"));

		return itens;
	}

	public boolean isCampoData() {
		if (getControleConsulta().getCampoConsulta().equals("DATA_ABERTURA")) {
			return true;
		}
		return false;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		// setPaginaAtualDeTodas("0/0");
		setListaConsulta(new ArrayList<>(0));
		// definirVisibilidadeLinksNavegacao(0, 0);
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("fluxoCaixaCons.xhtml");
	}

	public List<SelectItem> getListaSelectItemContaCaixa() {
		if (listaSelectItemContaCaixa == null) {
			listaSelectItemContaCaixa = new ArrayList<>(0);
		}
		return (listaSelectItemContaCaixa);
	}

	public void setListaSelectItemContaCaixa(List<SelectItem> listaSelectItemContaCaixa) {
		this.listaSelectItemContaCaixa = listaSelectItemContaCaixa;
	}

	public FluxoCaixaVO getFluxoCaixaVO() {
		if (fluxoCaixaVO == null) {
			fluxoCaixaVO = new FluxoCaixaVO();
		}
		return fluxoCaixaVO;
	}

	public void setFluxoCaixaVO(FluxoCaixaVO fluxoCaixaVO) {
		this.fluxoCaixaVO = fluxoCaixaVO;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		fluxoCaixaVO = null;
		Uteis.liberarListaMemoria(listaSelectItemContaCaixa);
	}

	public Date getDataInicioConsultar() {
		return dataInicioConsultar;
	}

	public void setDataInicioConsultar(Date dataInicioConsultar) {
		this.dataInicioConsultar = dataInicioConsultar;
	}

	public boolean getApresentarDataConsulta() throws Exception {
		if (getControleConsulta().getCampoConsulta().equals("dataAbertura")) {
			return true;
		} else {
			return false;
		}
	}

	public List<MovimentacaoCaixaVO> getMovimentacaoCaixaChequeVOs() {
		if (movimentacaoCaixaChequeVOs == null) {
			movimentacaoCaixaChequeVOs = new ArrayList<MovimentacaoCaixaVO>(0);
		}
		return movimentacaoCaixaChequeVOs;
	}

	public void setMovimentacaoCaixaChequeVOs(List<MovimentacaoCaixaVO> movimentacaoCaixaChequeVOs) {
		this.movimentacaoCaixaChequeVOs = movimentacaoCaixaChequeVOs;
	}
	
	public void liberarRegistroCompetenciaFechada() {
		try {
			this.getFluxoCaixaVO().setBloqueioPorFechamentoMesLiberado(Boolean.TRUE);		
			FechamentoMesHistoricoModificacaoVO historico = getFacadeFactory().getFechamentoMesHistoricoModificacaoFacade().gerarNovoHistoricoModificacao(this.getFluxoCaixaVO().getFechamentoMesVOBloqueio(), getUsuarioLogado(), TipoOrigemHistoricoBloqueioEnum.CAIXA, this.getFluxoCaixaVO().getDescricaoBloqueio(), this.getFluxoCaixaVO().toString());
			getFacadeFactory().getFechamentoMesHistoricoModificacaoFacade().incluir(historico, getUsuarioLogado());
			setMensagemID("msg_registro_liberado_mes");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.getFluxoCaixaVO().setBloqueioPorFechamentoMesLiberado(Boolean.FALSE);
		}
	}
	
	public void verificarPermissaoLiberarBloqueioCompetencia() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUsernameLiberacaoBloqueioPorFechamentoMes(), this.getSenhaLiberacaoBloqueioPorFechamentoMes(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("FuncionarioMes_liberarBloqueioAberturaCaixa", usuarioVerif);
			liberarRegistroCompetenciaFechada();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
 	}	
	
    public Boolean getApresentarBotaoLiberarBloqueio() {
    	return this.getFluxoCaixaVO().getApresentarBotaoLiberarBloqueioFechamentoMes();
    }	
}
