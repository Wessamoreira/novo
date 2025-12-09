package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas
 * liberacaoFinanceiroCancelamentoTrancamentoForm.jsp liberacaoFinanceiroCancelamentoTrancamentoCons.jsp) com as
 * funcionalidades da classe <code>LiberacaoFinanceiroCancelamentoTrancamento</code>. Implemtação da camada controle
 * (Backing Bean).
 * 
 * @see SuperControle
 * @see LiberacaoFinanceiroCancelamentoTrancamento
 * @see LiberacaoFinanceiroCancelamentoTrancamentoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO;
import negocio.comuns.financeiro.LiberacaoFinanceiroCancelamentoTrancamentoVO;
import negocio.comuns.financeiro.enumerador.MotivoLiberacaoFinanceiroEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("LiberacaoFinanceiroCancelamentoTrancamentoControle")
@Scope("viewScope")
@Lazy
public class LiberacaoFinanceiroCancelamentoTrancamentoControle extends SuperControle implements Serializable {

	private LiberacaoFinanceiroCancelamentoTrancamentoVO liberacaoFinanceiroCancelamentoTrancamentoVO;
	private String campoConsultarMatriculaAluno;
	private String valorConsultarMatriculaAluno;
	private List listaConsultarMatriculaAluno;
	private String campoConsultarUnidadeEnsino;
	private String valorConsultarUnidadeEnsino;
	private List listaConsultarUnidadeEnsino;
	private String responsavel_Erro;
	private HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO historicoLiberacaoFinanceiroCancelamentoTrancamentoVO;
	protected List listaSelectItemLiberacaoFinanceiroCancelamentoTrancamento;
	private List listaConsultaAluno;
	private List<ContaReceberVO> listaPendencias;
	private List<ContaReceberVO> listaPendenciasPersistirBanco;
	private List<ContaReceberVO> listaContaReceber;
	private List<ContaReceberVO> listaContaReceberPersistirBanco;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	// Esse campo controla se os botoes de Novo e consulta deverá ser
	// apresentados na tela pois quando a tela e inciada pelo Servelt não
	// consigo realizar uma nagegacao.
	private Boolean apresentarBotoesParaNavegacaoPagina;
	private Boolean possuiPermissaoRemoverContaReceber;
	private Boolean possuiPermissaoRemoverContaReceberVencida;
	private Boolean possuiPermissaoCancelarContaReceberVencida;
	private Boolean marcarTodasContarReceber;
	private Boolean marcarTodasPendecias;
	private String motivoCancelamento;
	private List<ContaReceberVO> listaContaReceberCanceladaEstornar;	
	private String modalOncomplete;	

	public LiberacaoFinanceiroCancelamentoTrancamentoControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	@PostConstruct
	public void realizarInicializacaoPeloServlet() {
		try {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String matricula = (String) request.getAttribute("codigo");
			if (matricula != null && !matricula.isEmpty()) {
				inicializarNovaLiberacaoAutomaticaPorMapaSuspensaoCancelamento(matricula);
			} else {
				matricula = (String) request.getSession().getAttribute("codigo");
				if (matricula != null && !matricula.isEmpty()) {
					inicializarNovaLiberacaoAutomaticaPorMapaSuspensaoCancelamento(matricula);
				}
			}
			setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>LiberacaoFinanceiroCancelamentoTrancamento</code> para edição pelo
	 * usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setLiberacaoFinanceiroCancelamentoTrancamentoVO(new LiberacaoFinanceiroCancelamentoTrancamentoVO());
		inicializarListasSelectItemTodosComboBox();
		setListaPendencias(new ArrayList(0));
		setListaContaReceber(new ArrayList(0));
		setListaContaReceberPersistirBanco(new ArrayList(0));
		setHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO(new HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO());
		inicializarUsuarioLogado();
		setApresentarBotoesParaNavegacaoPagina(true);
		verificarUsuarioPossuiPermissaoCancelarContaReceberVencida();
		verificarUsuarioPossuiPermissaoRemoverContaReceber();
		verificarUsuarioPossuiPermissaoRemoverContaReceberVencida();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("liberacaoFinanceiroCancelamentoTrancamentoForm.xhtml");
	}

	public void verificarUsuarioPossuiPermissaoRemoverContaReceber() {
		Boolean liberar = false;
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("LiberacaoFinanceiroCancelamento_PermitiRemoverContaReceber", getUsuarioLogado());
			liberar = true;
		} catch (Exception e) {
			liberar = false;
		}
		this.setPossuiPermissaoRemoverContaReceber(liberar);
	}

	public void verificarUsuarioPossuiPermissaoCancelarContaReceberVencida() {
		Boolean liberar = false;
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("LiberacaoFinanceiroCancelamento_PermitiCancelarContaReceberVencida", getUsuarioLogado());
			liberar = true;
		} catch (Exception e) {
			liberar = false;
		}
		this.setPossuiPermissaoCancelarContaReceberVencida(liberar);
	}

	public void verificarUsuarioPossuiPermissaoRemoverContaReceberVencida() {
		Boolean liberar = false;
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("LiberacaoFinanceiroCancelamento_PermitiRemoverContaReceberVencida", getUsuarioLogado());
			liberar = true;
		} catch (Exception e) {
			liberar = false;
		}
		this.setPossuiPermissaoRemoverContaReceberVencida(liberar);
	}

	public void inicializarUsuarioLogado() {
		try {
			getLiberacaoFinanceiroCancelamentoTrancamentoVO().setResponsavel(getUsuarioLogadoClone());
		} catch (Exception e) {
		}
	}

	public void inicializarNovaLiberacaoAutomaticaPorMapaSuspensaoCancelamento(String matricula) {
		try {
			novo();
			getLiberacaoFinanceiroCancelamentoTrancamentoVO().getMatriculaAluno().setMatricula(matricula);
			consultarAlunoPorMatricula();
			setApresentarBotoesParaNavegacaoPagina(false);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>LiberacaoFinanceiroCancelamentoTrancamento</code> para alteração. O
	 * objeto desta classe é disponibilizado na session da página (request) para
	 * que o JSP correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		LiberacaoFinanceiroCancelamentoTrancamentoVO obj = (LiberacaoFinanceiroCancelamentoTrancamentoVO) context().getExternalContext().getRequestMap().get("liberacaoFinanceiroCancelamentoTrancamentoItens");
		obj.setNovoObj(Boolean.FALSE);
		setLiberacaoFinanceiroCancelamentoTrancamentoVO(obj);
		consultarAlunoPorMatricula();
		setHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO(new HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO());
		setApresentarBotoesParaNavegacaoPagina(true);
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("liberacaoFinanceiroCancelamentoTrancamentoForm.xhtml");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>LiberacaoFinanceiroCancelamentoTrancamento</code>. Caso o
	 * objeto seja novo (ainda não gravado no BD) é acionado a operação
	 * <code>incluir()</code>. Caso contrário é acionado o
	 * <code>alterar()</code>. Se houver alguma inconsistência o objeto não é
	 * gravado, sendo re-apresentado para o usuário juntamente com uma mensagem
	 * de erro.
	 */
	public void gravar() {
		try {
			if (liberacaoFinanceiroCancelamentoTrancamentoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getLiberacaoFinanceiroCancelamentoTrancamentoFacade().incluir(liberacaoFinanceiroCancelamentoTrancamentoVO, getListaPendencias(), getListaContaReceber(), getListaContaReceberPersistirBanco(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			} else {
				throw new Exception(getMensagemInternalizacao("msg_LiberacaoFinanceiroCancelamentoTrancamento_alteracao"));
			}
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			// return "editar";
		}
	}

	public void verificarEstornar() {
		try {
			MatriculaVO obj = getLiberacaoFinanceiroCancelamentoTrancamentoVO().getMatriculaAluno();
			setListaContaReceberCanceladaEstornar(getFacadeFactory().getContaReceberFacade().consultarPorAlunoEMatriculaContasReceberMensalidadeEMatriculaCancelado(obj.getAluno().getCodigo(), obj.getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			if (!getListaContaReceberCanceladaEstornar().isEmpty()) {
				realizarMarcacaoTodasContaReceberCancelada();
				setModalOncomplete("RichFaces.$('panelContasCanceladasEstornar').show(); RichFaces.$('panelMensagemEstornar').hide();");
			} else {
				estornar();
			}			
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void estornar() {
		try {
			getFacadeFactory().getLiberacaoFinanceiroCancelamentoTrancamentoFacade().realizarEstorno(getLiberacaoFinanceiroCancelamentoTrancamentoVO(), getListaContaReceberCanceladaEstornar(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			consultarAlunoPorMatricula();			
			setMensagemID("msg_dados_estornados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void isentar() {
		try {
			if (!verificarListaPossuiItemSelecionado(getListaContaReceber()) && !verificarListaPossuiItemSelecionado(getListaPendencias())) {
				throw new Exception("Selecione ao menos um item!");
			}
			int nrContasIsentas = 0;
			nrContasIsentas = this.isentarContasReceber(getListaContaReceber());
			nrContasIsentas += this.isentarContasReceber(getListaPendencias());
			if (nrContasIsentas <= 0) {
				throw new Exception("Nenhuma das contas selecionadas pode ser isentada. Verifique se as mesmas estão vencidas e você possuir permissão para isentar este tipo de conta.");
			}
			setMensagemID("msg_contareCeber_dadosIsentados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			// return "editar";
		}
	}

	public int isentarContasReceber(List lista) throws Exception {
		int nrContasIsentas = 0;
		Iterator i = lista.iterator();
		while (i.hasNext()) {
			ContaReceberVO c = (ContaReceberVO) i.next();
			if (c.getSelecionado()) {
				if (c.getDataVencimento().compareTo(new Date()) <= 0) {
					c.setSituacao("RE");
					c.setIsentar(true);
					c.setValorDesconto(100.0);
					c.setTipoDesconto("PO");
					c.setValorDescontoAlunoJaCalculado(c.getValor());
					nrContasIsentas++;
				}
			} else {
				c.setIsentar(false);
			}
			// if (c.getDataVencimento().compareTo(new Date()) < 0) {
			// c.setSituacao("RE");
			// c.setIsentar(true);
			// c.setValorDesconto(100.0);
			// c.setTipoDesconto("PO");
			// c.setValorDescontoAlunoJaCalculado(c.getValor());
			// } else {
			// c.setIsentar(false);
			// }
		}
		return nrContasIsentas;
	}

	public void cancelarContasReceberSelecionadas() {
		try {
			if (!verificarListaPossuiItemSelecionado(getListaContaReceber()) && !verificarListaPossuiItemSelecionado(getListaPendencias())) {
				throw new Exception("Selecione ao menos um item!");
			}
			if (getMotivoCancelamento().equals("")) {
				throw new Exception("O campo MOTIVO DE CANCELAMENTO deve ser informado!");
			}
			setListaContaReceberPersistirBanco(new ArrayList(0));
			int nrContasCanceladas = 0;
			nrContasCanceladas = this.cancelarContasReceber(getListaContaReceber(), getMotivoCancelamento(), getUsuarioLogado());
			nrContasCanceladas += this.cancelarContasReceber(getListaPendencias(), getMotivoCancelamento(), getUsuarioLogado());
			if (nrContasCanceladas <= 0) {
				throw new Exception("Nenhuma das contas selecionadas pode ser cancelada. Verifique se as mesmas estão vencidas e você possuir permissão para cancelar este tipo de conta.");
			}
			setMensagemID("msg_contareCeber_dadosCanceladas");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
		}
	}

	public int cancelarContasReceber(List lista, String motivoCancelamento, UsuarioVO usuarioVO) throws Exception {
		int nrCanceladas = 0;
		Iterator i = lista.iterator();
		while (i.hasNext()) {
			ContaReceberVO c = (ContaReceberVO) i.next();
			if (c.getSelecionado()) {
				if (getPossuiPermissaoCancelarContaReceberVencida()) {
					c.setCancelar(true);
					c.setSituacao(SituacaoContaReceber.CANCELADO_FINANCEIRO.getValor());
					getListaContaReceberPersistirBanco().add(c);
					getHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO().setDescricao(c.getCodigo().toString() + " - " + c.getNrDocumento() + " - " + c.getValor_Apresentar() + " - " + c.getTipoOrigem_apresentar());
					adicionarHistoricoLiberacaoFinanceiroCancelamentoTrancamento();
					nrCanceladas++;
					c.setMotivoCancelamento(motivoCancelamento);
					c.setDataCancelamento(new Date());
					c.getResponsavelCancelamentoVO().setCodigo(usuarioVO.getCodigo());
				} else {
					if (c.getDataVencimento().compareTo(new Date()) >= 0) {
						c.setCancelar(true);
						c.setSituacao(SituacaoContaReceber.CANCELADO_FINANCEIRO.getValor());
						getListaContaReceberPersistirBanco().add(c);
						getHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO().setDescricao(c.getCodigo().toString() + " - " + c.getNrDocumento() + " - " + c.getValor_Apresentar() + " - " + c.getTipoOrigem_apresentar());
						adicionarHistoricoLiberacaoFinanceiroCancelamentoTrancamento();
						nrCanceladas++;
						c.setMotivoCancelamento(motivoCancelamento);
						c.setDataCancelamento(new Date());
						c.getResponsavelCancelamentoVO().setCodigo(usuarioVO.getCodigo());
					}
				}
			}
		}
		return nrCanceladas;
	}

	public void remover() {
		try {
			if (!getPossuiPermissaoRemoverContaReceber()) {
				throw new Exception("Usuário não possui permissão para excluir Contas a Receber");
			}
			if (!verificarListaPossuiItemSelecionado(getListaContaReceber()) && !verificarListaPossuiItemSelecionado(getListaPendencias())) {
				throw new Exception("Selecione ao menos um item!");
			}
			setListaContaReceberPersistirBanco(new ArrayList(0));
			int nrContasRemovidas = 0;
			nrContasRemovidas = this.removerContasReceber(getListaContaReceber());
			nrContasRemovidas += this.removerContasReceber(getListaPendencias());
			if (nrContasRemovidas <= 0) {
				throw new Exception("Nenhuma das contas selecionadas pode ser removida. Verifique se as mesmas estão vencidas e você possuir permissão para remover este tipo de conta.");
			}
			setMensagemID("msg_contareCeber_dadosRemovidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			// return "editar";
		}
	}

	public int removerContasReceber(List lista) throws Exception {
		int nrContaRemovida = 0;
		Iterator i = lista.iterator();
		while (i.hasNext()) {
			ContaReceberVO c = (ContaReceberVO) i.next();
			if (c.getSelecionado()) {
				if(Uteis.isAtributoPreenchido(getFacadeFactory().getContaReceberFacade().consultarNumeroNotaFiscalSaidaServicoPorContaReceber(c.getCodigo()))){
					throw new Exception("Não foi possível remover a(s) conta(s) selecionado(s) que possui(em) Nota Fiscal vinculada.");
				}
				if (getPossuiPermissaoRemoverContaReceberVencida() && !c.getIsentar()) {
					c.setRemover(true);
					c.setSituacao("RM");
					getListaContaReceberPersistirBanco().add(c);
					getHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO().setDescricao(c.getCodigo().toString() + " - " + c.getNrDocumento() + " - " + c.getValor_Apresentar() + " - " + c.getTipoOrigem_apresentar());
					adicionarHistoricoLiberacaoFinanceiroCancelamentoTrancamento();
					nrContaRemovida++;
				} else {
					if (c.getDataVencimento().compareTo(new Date()) >= 0) {
						c.setRemover(true);
						c.setSituacao("RM");
						getListaContaReceberPersistirBanco().add(c);
						getHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO().setDescricao(c.getCodigo().toString() + " - " + c.getNrDocumento() + " - " + c.getValor_Apresentar() + " - " + c.getTipoOrigem_apresentar());
						adicionarHistoricoLiberacaoFinanceiroCancelamentoTrancamento();
						nrContaRemovida++;
					}
				}
			}
		}
		return nrContaRemovida;
	}

	public boolean verificarListaPossuiItemSelecionado(List lista) {
		Iterator i = lista.iterator();
		while (i.hasNext()) {
			ContaReceberVO c = (ContaReceberVO) i.next();
			if (c.getSelecionado()) {
				return true;
			}
		}
		return false;
	}

	public void atualizarDados() {
		ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getRequestMap().get("contaReceberItens");
		if (obj.getIsentar()) {
			obj.setIsentar(false);
		} else {
			obj.setIsentar(true);
		}
	}

	public boolean getApresentarListaContaReceber() {
		if (getListaContaReceber().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public boolean getApresentarListaPendencias() {
		if (getListaPendencias().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * LiberacaoFinanceiroCancelamentoTrancamentoCons.jsp. Define o tipo de
	 * consulta a ser executada, por meio de ComboBox denominado campoConsulta,
	 * disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os
	 * objetos selecionados na sessao da pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeAluno")) {
				objs = getFacadeFactory().getLiberacaoFinanceiroCancelamentoTrancamentoFacade().consultaRapidaPorNomeAluno(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("matriculaMatricula")) {
				objs = getFacadeFactory().getLiberacaoFinanceiroCancelamentoTrancamentoFacade().consultaRapidaPorMatricula(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getLiberacaoFinanceiroCancelamentoTrancamentoFacade().consultaRapidaPorNomeUnidadeEnsino(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataCadastro")) {
				objs = getFacadeFactory().getLiberacaoFinanceiroCancelamentoTrancamentoFacade().consultaRapidaPorDataCadastro(Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0), Uteis.getDateTime(getControleConsulta().getDataIni(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), true, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeUsuario")) {
				objs = getFacadeFactory().getLiberacaoFinanceiroCancelamentoTrancamentoFacade().consultaRapidaPorResponsavel(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			return Uteis.getCaminhoRedirecionamentoNavegacao("liberacaoFinanceiroCancelamentoTrancamentoCons.xhtml");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>LiberacaoFinanceiroCancelamentoTrancamentoVO</code> Após a exclusão
	 * ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getLiberacaoFinanceiroCancelamentoTrancamentoFacade().excluir(liberacaoFinanceiroCancelamentoTrancamentoVO, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			return Uteis.getCaminhoRedirecionamentoNavegacao("liberacaoFinanceiroCancelamentoTrancamentoForm.xhtml");
		}
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamento</code> para o
	 * objeto <code>liberacaoFinanceiroCancelamentoTrancamentoVO</code> da
	 * classe <code>LiberacaoFinanceiroCancelamentoTrancamento</code>
	 */
	public void adicionarHistoricoLiberacaoFinanceiroCancelamentoTrancamento() throws Exception {
		if (!getLiberacaoFinanceiroCancelamentoTrancamentoVO().getCodigo().equals(0)) {
			historicoLiberacaoFinanceiroCancelamentoTrancamentoVO.setLiberacaoFinanceiroCancelamentoTrancamento(getLiberacaoFinanceiroCancelamentoTrancamentoVO());
		}
		if (getHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO().getLiberacaoFinanceiroCancelamentoTrancamento().getCodigo().intValue() != 0) {
			Integer campoConsulta = getHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO().getLiberacaoFinanceiroCancelamentoTrancamento().getCodigo();
			LiberacaoFinanceiroCancelamentoTrancamentoVO liberacaoFinanceiroCancelamentoTrancamento = getFacadeFactory().getLiberacaoFinanceiroCancelamentoTrancamentoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			getHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO().setLiberacaoFinanceiroCancelamentoTrancamento(liberacaoFinanceiroCancelamentoTrancamento);
		}
		getLiberacaoFinanceiroCancelamentoTrancamentoVO().adicionarObjHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVOs(getHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO());
		this.setHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO(new HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO());
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamento</code> para
	 * edição pelo usuário.
	 */
	public String editarHistoricoLiberacaoFinanceiroCancelamentoTrancamento() throws Exception {
		HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO obj = (HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO) context().getExternalContext().getRequestMap().get("historicoLiberacaoFinanceiroCancelamentoTrancamentoItens");
		setHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO(obj);
		return Uteis.getCaminhoRedirecionamentoNavegacao("liberacaoFinanceiroCancelamentoTrancamentoForm.xhtml");
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamento</code> do
	 * objeto <code>liberacaoFinanceiroCancelamentoTrancamentoVO</code> da
	 * classe <code>LiberacaoFinanceiroCancelamentoTrancamento</code>
	 */
	public String removerHistoricoLiberacaoFinanceiroCancelamentoTrancamento() throws Exception {
		HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO obj = (HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO) context().getExternalContext().getRequestMap().get("historicoLiberacaoFinanceiroCancelamentoTrancamentoItens");
		getLiberacaoFinanceiroCancelamentoTrancamentoVO().excluirObjHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVOs(obj.getLiberacaoFinanceiroCancelamentoTrancamento().getCodigo());
		setMensagemID("msg_dados_excluidos");
		return Uteis.getCaminhoRedirecionamentoNavegacao("liberacaoFinanceiroCancelamentoTrancamentoForm.xhtml");
	}

	public void removerContaReceberVOs() throws Exception {
		try {
			ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getRequestMap().get("contaReceberItens");
			if(Uteis.isAtributoPreenchido(getFacadeFactory().getContaReceberFacade().consultarNumeroNotaFiscalSaidaServicoPorContaReceber(obj.getCodigo()))){
				throw new Exception("Não é possiveis excluir a Conta a Receber da lista, pois a mesma possui uma Nota Fiscal vinculada .");
			}
			getFacadeFactory().getLiberacaoFinanceiroCancelamentoTrancamentoFacade().excluirObjListaContaReceberVOs(obj.getCodigo(), getListaContaReceber(), getUsuarioLogado());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>LiberacaoFinanceiroCancelamentoTrancamento</code>.
	 */
	public void montarListaSelectItemLiberacaoFinanceiroCancelamentoTrancamento(Integer prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarLiberacaoFinanceiroCancelamentoTrancamentoPorCodigo(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				LiberacaoFinanceiroCancelamentoTrancamentoVO obj = (LiberacaoFinanceiroCancelamentoTrancamentoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getCodigo().toString()));
			}
			setListaSelectItemLiberacaoFinanceiroCancelamentoTrancamento(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>LiberacaoFinanceiroCancelamentoTrancamento</code>. Buscando todos
	 * os objetos correspondentes a entidade
	 * <code>LiberacaoFinanceiroCancelamentoTrancamento</code>. Esta rotina não
	 * recebe parâmetros para filtragem de dados, isto é importante para a
	 * inicialização dos dados da tela para o acionamento por meio requisições
	 * Ajax.
	 */
	public void montarListaSelectItemLiberacaoFinanceiroCancelamentoTrancamento() {
		try {
			montarListaSelectItemLiberacaoFinanceiroCancelamentoTrancamento(0);
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>codigo</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarLiberacaoFinanceiroCancelamentoTrancamentoPorCodigo(Integer codigoPrm) throws Exception {
		List lista = getFacadeFactory().getLiberacaoFinanceiroCancelamentoTrancamentoFacade().consultarPorCodigo(codigoPrm, false, Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		// montarListaSelectItemLiberacaoFinanceiroCancelamentoTrancamento();
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Usuario</code> por meio de sua respectiva chave primária. Esta
	 * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
	 * busca pela chave primária da entidade montando automaticamente o
	 * resultado da consulta para apresentação.
	 */
	public void consultarUsuarioPorChavePrimaria() {
		try {
			Integer campoConsulta = liberacaoFinanceiroCancelamentoTrancamentoVO.getResponsavel().getCodigo();
			UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			liberacaoFinanceiroCancelamentoTrancamentoVO.getResponsavel().setNome(usuario.getNome());
			this.setResponsavel_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			liberacaoFinanceiroCancelamentoTrancamentoVO.getResponsavel().setNome("");
			liberacaoFinanceiroCancelamentoTrancamentoVO.getResponsavel().setCodigo(0);
			this.setResponsavel_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>UnidadeEnsino</code> por meio dos parametros informados no
	 * richmodal. Esta rotina é utilizada fundamentalmente por requisições Ajax,
	 * que realizam busca pelos parâmentros informados no richModal montando
	 * automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarUnidadeEnsino() {
		try {
			List objs = new ArrayList(0);
			setListaConsultarUnidadeEnsino(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultarUnidadeEnsino(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarUnidadeEnsino() throws Exception {
		UnidadeEnsinoVO obj = (UnidadeEnsinoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoItens");
		if (getMensagemDetalhada().equals("")) {
			this.getLiberacaoFinanceiroCancelamentoTrancamentoVO().setUnidadeEnsino(obj);
		}
		Uteis.liberarListaMemoria(this.getListaConsultarUnidadeEnsino());
		this.setValorConsultarUnidadeEnsino(null);
		this.setCampoConsultarUnidadeEnsino(null);
	}

	public void limparCampoUnidadeEnsino() {
		this.getLiberacaoFinanceiroCancelamentoTrancamentoVO().setUnidadeEnsino(new UnidadeEnsinoVO());
	}

	/**
	 * Rotina responsável por preencher a combo de consulta dos RichModal da
	 * telas.
	 */
	public List getTipoConsultarComboUnidadeEnsino() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		itens.add(new SelectItem("RG", "RG"));
		itens.add(new SelectItem("CPF", "CPF"));
		return itens;
	}

	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);

			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
				if (!obj.getMatricula().equals("")) {
					objs.add(obj);
				}
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
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getLiberacaoFinanceiroCancelamentoTrancamentoVO().getMatriculaAluno().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			String motivo = getLiberacaoFinanceiroCancelamentoTrancamentoVO().getMotivoLiberacaoFinanceiro();
			getFacadeFactory().getLiberacaoFinanceiroCancelamentoTrancamentoFacade().validarDadosPendenciaEmprestimoBiblioteca(objAluno, motivo.equals("TR"), motivo.equals("AC"), motivo.equals("CA"), motivo.equals("TS"), motivo.equals("TI"), false, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getLiberacaoFinanceiroCancelamentoTrancamentoVO().getMatriculaAluno().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			this.getLiberacaoFinanceiroCancelamentoTrancamentoVO().setMatriculaAluno(objAluno);
			consultarListaPendenciasAluno(objAluno);
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.getLiberacaoFinanceiroCancelamentoTrancamentoVO().setMatriculaAluno(new MatriculaVO());
		}
	}

	public void limparDadosAluno() throws Exception {
		setLiberacaoFinanceiroCancelamentoTrancamentoVO(new LiberacaoFinanceiroCancelamentoTrancamentoVO());
		setListaContaReceber(new ArrayList(0));
		setListaPendencias(new ArrayList(0));
		setListaContaReceberPersistirBanco(new ArrayList(0));
		inicializarUsuarioLogado();
		setMotivoCancelamento("");
	}

	public void selecionarAluno() throws Exception {
		try {

			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
			String motivo = getLiberacaoFinanceiroCancelamentoTrancamentoVO().getMotivoLiberacaoFinanceiro();
			getFacadeFactory().getLiberacaoFinanceiroCancelamentoTrancamentoFacade().validarDadosPendenciaEmprestimoBiblioteca(obj, motivo.equals("TR"), motivo.equals("AC"), motivo.equals("CA"), motivo.equals("TS"), motivo.equals("TI"), false, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			// MatriculaVO objCompleto =
			// getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(),
			// obj.getUnidadeEnsino().getCodigo(),
			// Uteis.NIVELMONTARDADOS_TODOS,
			// getConfiguracaoFinanceiroPadraoSistema(),
			// getUsuarioLogado());
			getFacadeFactory().getMatriculaFacade().carregarDados(obj, NivelMontarDados.BASICO, getUsuarioLogado());
			getLiberacaoFinanceiroCancelamentoTrancamentoVO().setMatriculaAluno(obj);
			consultarListaPendenciasAluno(obj);
			obj = null;
			valorConsultaAluno = "";
			campoConsultaAluno = "";
			getListaConsultaAluno().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public void consultarListaPendenciasAluno(MatriculaVO obj) throws Exception {
		setListaContaReceber(getFacadeFactory().getContaReceberFacade().consultarPorAlunoEMatriculaContasReceberMensalidadeEMatricula(obj.getAluno().getCodigo(), obj.getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
		setListaPendencias(getFacadeFactory().getContaReceberFacade().consultarPorAlunoEMatriculaContasReceberBiblioteca(obj.getAluno().getCodigo(), obj.getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
		getLiberacaoFinanceiroCancelamentoTrancamentoVO().setUnidadeEnsino(obj.getUnidadeEnsino());
	}

	public void realizarMarcacaoTodasContaReceber() {
		for (ContaReceberVO contaReceberVO : getListaContaReceber()) {
			if (contaReceberVO.getSituacao_ApresentarIsentos()) {
				contaReceberVO.setSelecionado(getMarcarTodasContarReceber());
			}
		}
	}

	public void realizarMarcacaoTodasContaReceberCancelada() {
		for (ContaReceberVO contaReceberVO : getListaContaReceberCanceladaEstornar()) {
			if (contaReceberVO.getSituacao_ApresentarIsentos()) {
				contaReceberVO.setSelecionado(getMarcarTodasContarReceber());
			}
		}
	}
	
	public void realizarMarcacaoTodasPendencias() {
		for (ContaReceberVO contaReceberVO : getListaPendencias()) {
			if (contaReceberVO.getSituacao_ApresentarIsentos()) {
				contaReceberVO.setSelecionado(getMarcarTodasPendecias());
			}
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta dos RichModal da
	 * telas.
	 */
	public List getTipoConsultarComboMatriculaAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("data", "Data"));
		itens.add(new SelectItem("situacao", "Situação"));
		itens.add(new SelectItem("nomePessoa", "Responsável Matrícula"));
		return itens;
	}

	/**
	 * Rotina responsável por atribui um javascript com o método de mascara para
	 * campos do tipo Data, CPF, CNPJ, etc.
	 */
	public String getMascaraConsulta() {
		return "";
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomeAluno", "Aluno"));
		itens.add(new SelectItem("matriculaMatricula", "Mátricula"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade de Ensino"));
		itens.add(new SelectItem("dataCadastro", "Data de Cadastro"));
		itens.add(new SelectItem("nomeUsuario", "Responsável"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("liberacaoFinanceiroCancelamentoTrancamentoCons.xhtml");
	}

	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do
	 * backing bean. Garantindo uma melhor atuação do Garbage Coletor do Java. A
	 * mesma é automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		liberacaoFinanceiroCancelamentoTrancamentoVO = null;
		responsavel_Erro = null;
		historicoLiberacaoFinanceiroCancelamentoTrancamentoVO = null;
		Uteis.liberarListaMemoria(listaSelectItemLiberacaoFinanceiroCancelamentoTrancamento);
	}

	public List getListaSelectItemLiberacaoFinanceiroCancelamentoTrancamento() {
		if (listaSelectItemLiberacaoFinanceiroCancelamentoTrancamento == null) {
			listaSelectItemLiberacaoFinanceiroCancelamentoTrancamento = new ArrayList(0);
		}
		return (listaSelectItemLiberacaoFinanceiroCancelamentoTrancamento);
	}

	public void setListaSelectItemLiberacaoFinanceiroCancelamentoTrancamento(List listaSelectItemLiberacaoFinanceiroCancelamentoTrancamento) {
		this.listaSelectItemLiberacaoFinanceiroCancelamentoTrancamento = listaSelectItemLiberacaoFinanceiroCancelamentoTrancamento;
	}

	public HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO getHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO() {
		return historicoLiberacaoFinanceiroCancelamentoTrancamentoVO;
	}

	public void setHistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO(HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO historicoLiberacaoFinanceiroCancelamentoTrancamentoVO) {
		this.historicoLiberacaoFinanceiroCancelamentoTrancamentoVO = historicoLiberacaoFinanceiroCancelamentoTrancamentoVO;
	}

	public String getResponsavel_Erro() {
		return responsavel_Erro;
	}

	public void setResponsavel_Erro(String responsavel_Erro) {
		this.responsavel_Erro = responsavel_Erro;
	}

	public String getCampoConsultarUnidadeEnsino() {
		return campoConsultarUnidadeEnsino;
	}

	public void setCampoConsultarUnidadeEnsino(String campoConsultarUnidadeEnsino) {
		this.campoConsultarUnidadeEnsino = campoConsultarUnidadeEnsino;
	}

	public String getValorConsultarUnidadeEnsino() {
		return valorConsultarUnidadeEnsino;
	}

	public void setValorConsultarUnidadeEnsino(String valorConsultarUnidadeEnsino) {
		this.valorConsultarUnidadeEnsino = valorConsultarUnidadeEnsino;
	}

	public List getListaConsultarUnidadeEnsino() {
		return listaConsultarUnidadeEnsino;
	}

	public void setListaConsultarUnidadeEnsino(List listaConsultarUnidadeEnsino) {
		this.listaConsultarUnidadeEnsino = listaConsultarUnidadeEnsino;
	}

	public String getCampoConsultarMatriculaAluno() {
		return campoConsultarMatriculaAluno;
	}

	public void setCampoConsultarMatriculaAluno(String campoConsultarMatriculaAluno) {
		this.campoConsultarMatriculaAluno = campoConsultarMatriculaAluno;
	}

	public String getValorConsultarMatriculaAluno() {
		return valorConsultarMatriculaAluno;
	}

	public void setValorConsultarMatriculaAluno(String valorConsultarMatriculaAluno) {
		this.valorConsultarMatriculaAluno = valorConsultarMatriculaAluno;
	}

	public List getListaConsultarMatriculaAluno() {
		return listaConsultarMatriculaAluno;
	}

	public void setListaConsultarMatriculaAluno(List listaConsultarMatriculaAluno) {
		this.listaConsultarMatriculaAluno = listaConsultarMatriculaAluno;
	}

	public LiberacaoFinanceiroCancelamentoTrancamentoVO getLiberacaoFinanceiroCancelamentoTrancamentoVO() {
		return liberacaoFinanceiroCancelamentoTrancamentoVO;
	}

	public void setLiberacaoFinanceiroCancelamentoTrancamentoVO(LiberacaoFinanceiroCancelamentoTrancamentoVO liberacaoFinanceiroCancelamentoTrancamentoVO) {
		this.liberacaoFinanceiroCancelamentoTrancamentoVO = liberacaoFinanceiroCancelamentoTrancamentoVO;
	}

	/**
	 * @return the listaConsultaAluno
	 */
	public List getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList(0);
		}
		return listaConsultaAluno;
	}

	/**
	 * @param listaConsultaAluno
	 *            the listaConsultaAluno to set
	 */
	public void setListaConsultaAluno(List listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	/**
	 * @return the valorConsultaAluno
	 */
	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	/**
	 * @param valorConsultaAluno
	 *            the valorConsultaAluno to set
	 */
	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	/**
	 * @return the campoConsultaAluno
	 */
	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	/**
	 * @param campoConsultaAluno
	 *            the campoConsultaAluno to set
	 */
	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	/**
	 * @return the listaContaReceber
	 */
	public List<ContaReceberVO> getListaContaReceber() {
		if (listaContaReceber == null) {
			listaContaReceber = new ArrayList<ContaReceberVO>(0);
		}
		return listaContaReceber;
	}

	/**
	 * @param listaContaReceber
	 *            the listaContaReceber to set
	 */
	public void setListaContaReceber(List<ContaReceberVO> listaContaReceber) {
		this.listaContaReceber = listaContaReceber;
	}

	/**
	 * @return the listaPendencias
	 */
	public List<ContaReceberVO> getListaPendencias() {
		if (listaPendencias == null) {
			listaPendencias = new ArrayList<ContaReceberVO>(0);
		}
		return listaPendencias;
	}

	/**
	 * @param listaPendencias
	 *            the listaPendencias to set
	 */
	public void setListaPendencias(List<ContaReceberVO> listaPendencias) {
		this.listaPendencias = listaPendencias;
	}

	/**
	 * @return the listaPendenciasPersistirBanco
	 */
	public List<ContaReceberVO> getListaPendenciasPersistirBanco() {
		if (listaPendenciasPersistirBanco == null) {
			listaPendenciasPersistirBanco = new ArrayList<ContaReceberVO>(0);
		}
		return listaPendenciasPersistirBanco;
	}

	/**
	 * @param listaPendenciasPersistirBanco
	 *            the listaPendenciasPersistirBanco to set
	 */
	public void setListaPendenciasPersistirBanco(List<ContaReceberVO> listaPendenciasPersistirBanco) {
		this.listaPendenciasPersistirBanco = listaPendenciasPersistirBanco;
	}

	/**
	 * @return the listaContaReceberPersistirBanco
	 */
	public List<ContaReceberVO> getListaContaReceberPersistirBanco() {
		if (listaContaReceberPersistirBanco == null) {
			listaContaReceberPersistirBanco = new ArrayList<ContaReceberVO>(0);
		}
		return listaContaReceberPersistirBanco;
	}

	/**
	 * @param listaContaReceberPersistirBanco
	 *            the listaContaReceberPersistirBanco to set
	 */
	public void setListaContaReceberPersistirBanco(List<ContaReceberVO> listaContaReceberPersistirBanco) {
		this.listaContaReceberPersistirBanco = listaContaReceberPersistirBanco;
	}

	public boolean isCampoData() {
		if (getControleConsulta().getCampoConsulta().equals("dataCadastro")) {
			return true;
		}
		return false;
	}

	public Boolean getApresentarBotoesParaNavegacaoPagina() {
		if (apresentarBotoesParaNavegacaoPagina == null) {
			apresentarBotoesParaNavegacaoPagina = false;
		}
		return apresentarBotoesParaNavegacaoPagina;
	}

	public void setApresentarBotoesParaNavegacaoPagina(Boolean apresentarBotoesParaNavegacaoPagina) {
		this.apresentarBotoesParaNavegacaoPagina = apresentarBotoesParaNavegacaoPagina;
	}

	/**
	 * @return the possuiPermissaoRemoverContaReceberVencida
	 */
	public Boolean getPossuiPermissaoRemoverContaReceberVencida() {
		if (possuiPermissaoRemoverContaReceberVencida == null) {
			possuiPermissaoRemoverContaReceberVencida = Boolean.FALSE;
		}
		return possuiPermissaoRemoverContaReceberVencida;
	}

	/**
	 * @param possuiPermissaoRemoverContaReceberVencida
	 *            the possuiPermissaoRemoverContaReceberVencida to set
	 */
	public void setPossuiPermissaoRemoverContaReceberVencida(Boolean possuiPermissaoRemoverContaReceberVencida) {
		this.possuiPermissaoRemoverContaReceberVencida = possuiPermissaoRemoverContaReceberVencida;
	}

	public Boolean getMarcarTodasContarReceber() {
		if (marcarTodasContarReceber == null) {
			marcarTodasContarReceber = false;
		}
		return marcarTodasContarReceber;
	}

	public void setMarcarTodasContarReceber(Boolean marcarTodasContarReceber) {
		this.marcarTodasContarReceber = marcarTodasContarReceber;
	}

	public Boolean getMarcarTodasPendecias() {
		if (marcarTodasPendecias == null) {
			marcarTodasPendecias = false;
		}
		return marcarTodasPendecias;
	}

	public void setMarcarTodasPendecias(Boolean marcarTodasPendecias) {
		this.marcarTodasPendecias = marcarTodasPendecias;
	}

	public List<SelectItem> getListaSelectItemMotivoLiberacaoFinanceiro() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(MotivoLiberacaoFinanceiroEnum.class, false);
	}

	/**
	 * @return the possuiPermissaoCancelarContaReceberVencida
	 */
	public Boolean getPossuiPermissaoCancelarContaReceberVencida() {
		if (possuiPermissaoCancelarContaReceberVencida == null) {
			possuiPermissaoCancelarContaReceberVencida = Boolean.FALSE;
		}
		return possuiPermissaoCancelarContaReceberVencida;
	}

	/**
	 * @param possuiPermissaoCancelarContaReceberVencida
	 *            the possuiPermissaoCancelarContaReceberVencida to set
	 */
	public void setPossuiPermissaoCancelarContaReceberVencida(Boolean possuiPermissaoCancelarContaReceberVencida) {
		this.possuiPermissaoCancelarContaReceberVencida = possuiPermissaoCancelarContaReceberVencida;
	}

	/**
	 * @return the possuiPermissaoRemoverContaReceber
	 */
	public Boolean getPossuiPermissaoRemoverContaReceber() {
		if (possuiPermissaoRemoverContaReceber == null) {
			possuiPermissaoRemoverContaReceber = Boolean.FALSE;
		}
		return possuiPermissaoRemoverContaReceber;
	}

	/**
	 * @param possuiPermissaoRemoverContaReceber
	 *            the possuiPermissaoRemoverContaReceber to set
	 */
	public void setPossuiPermissaoRemoverContaReceber(Boolean possuiPermissaoRemoverContaReceber) {
		this.possuiPermissaoRemoverContaReceber = possuiPermissaoRemoverContaReceber;
	}
	
	public String getMotivoCancelamento() {
		if (motivoCancelamento == null) {
			motivoCancelamento = "";
		}
		return motivoCancelamento;
	}

	public void setMotivoCancelamento(String motivoCancelamento) {
		this.motivoCancelamento = motivoCancelamento;
	}

	public String getModalOncomplete() {
		if (modalOncomplete == null) {
			modalOncomplete = "";
		}
		return modalOncomplete;
	}

	public void setModalOncomplete(String modalOncomplete) {
		this.modalOncomplete = modalOncomplete;
	}
	
	public List<ContaReceberVO> getListaContaReceberCanceladaEstornar() {
		if (listaContaReceberCanceladaEstornar == null) {
			listaContaReceberCanceladaEstornar = new ArrayList<ContaReceberVO>();
		}
		return listaContaReceberCanceladaEstornar;
	}

	public void setListaContaReceberCanceladaEstornar(List<ContaReceberVO> listaContaReceberCanceladaEstornar) {
		this.listaContaReceberCanceladaEstornar = listaContaReceberCanceladaEstornar;
	}

}
