package controle.compras;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas formaPagamentoForm.jsp
 * formaPagamentoCons.jsp) com as funcionalidades da classe <code>FormaPagamento</code>. Implemtação da camada controle
 * (Backing Bean).
 * 
 * @see SuperControle
 * @see FormaPagamento
 * @see FormaPagamentoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;

@Controller("FormaPagamentoControle")
@Scope("viewScope")
@Lazy
public class FormaPagamentoControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private FormaPagamentoVO formaPagamentoVO;
	private Boolean permitirEditarTipoFormaPagamento;

	public FormaPagamentoControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>FormaPagamento</code> para edição pelo usuário da aplicação.
	 */
	public String novo() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "FormaPagamentoControle", "Novo Forma Pagamento", "Novo");
		removerObjetoMemoria(this);
		setFormaPagamentoVO(new FormaPagamentoVO());
		setPermitirEditarTipoFormaPagamento(true);
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("formaPagamentoForm");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>FormaPagamento</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		setFormaPagamentoVO((FormaPagamentoVO) context().getExternalContext().getRequestMap().get("formaPagamentoItem"));
		setPermitirEditarTipoFormaPagamento(!getFacadeFactory().getFormaPagamentoFacade().executarVerificacaoFormaPagamentoVinculadoNegociacaoRecebimento(getFormaPagamentoVO().getCodigo()));
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("formaPagamentoForm");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>FormaPagamento</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (getFormaPagamentoVO().isNovoObj().booleanValue()) {
				registrarAtividadeUsuario(getUsuarioLogado(), "FormaPagamentoControle", "Inicializando Incluir Forma Pagamento", "Incluindo");
				getFacadeFactory().getFormaPagamentoFacade().incluir(getFormaPagamentoVO(), getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "FormaPagamentoControle", "Finalizando Incluir Forma Pagamento", "Incluindo");
			} else {
				registrarAtividadeUsuario(getUsuarioLogado(), "FormaPagamentoControle", "Inicializando Alterar Forma Pagamento", "Alterando");
				getFacadeFactory().getFormaPagamentoFacade().alterar(getFormaPagamentoVO(), getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "FormaPagamentoControle", "Finalizando Alterar Forma Pagamento", "Alterando");
			}
			setMensagemID("msg_dados_gravados");
			getAplicacaoControle().removerFormaPagamento(getFormaPagamentoVO().getCodigo());
			getAplicacaoControle().removerOperadoraCartao(null);
			return Uteis.getCaminhoRedirecionamentoNavegacao("formaPagamentoForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("formaPagamentoForm");
		}
	}
	
    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
    }

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * FormaPagamentoCons.jsp. Define o tipo de consulta a ser executada, por
	 * meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
	 * Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 */
	@SuppressWarnings("rawtypes")
	public String consultar() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "FormaPagamentoControle", "Inicializando Consultar Forma Pagamento", "Consultando");
			super.consultar();
			List<FormaPagamentoVO> objs = new ArrayList<FormaPagamentoVO>(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				int valorInt = 0;
				if (!getControleConsulta().getValorConsulta().equals("")) {
					valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				}
				objs = getFacadeFactory().getFormaPagamentoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getFormaPagamentoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			registrarAtividadeUsuario(getUsuarioLogado(), "FormaPagamentoControle", "Finalizando Consultar Forma Pagamento", "Consultando");
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("formaPagamentoCons");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("formaPagamentoCons");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>FormaPagamentoVO</code> Após a exclusão ela automaticamente aciona
	 * a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "FormaPagamentoControle", "Inicializando Excluir Forma Pagamento", "Excluindo");
			getFacadeFactory().getFormaPagamentoFacade().excluir(formaPagamentoVO, getUsuarioLogado());
			setFormaPagamentoVO(new FormaPagamentoVO());
			registrarAtividadeUsuario(getUsuarioLogado(), "FormaPagamentoControle", "Finalizando Excluir Forma Pagamento", "Excluindo");
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("formaPagamentoForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("formaPagamentoForm");
		}
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

	public List<SelectItem> getListaSelectItemTipoFormaPagamento() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoFormaPagamento.class, false);
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	@SuppressWarnings("rawtypes")
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("formaPagamentoCons");
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

	/**
	 * @return the permitirEditarTipoFormaPagamento
	 */
	public Boolean getPermitirEditarTipoFormaPagamento() {
		if (permitirEditarTipoFormaPagamento == null) {
			permitirEditarTipoFormaPagamento = false;
		}
		return permitirEditarTipoFormaPagamento;
	}

	/**
	 * @param permitirEditarTipoFormaPagamento
	 *            the permitirEditarTipoFormaPagamento to set
	 */
	public void setPermitirEditarTipoFormaPagamento(Boolean permitirEditarTipoFormaPagamento) {
		this.permitirEditarTipoFormaPagamento = permitirEditarTipoFormaPagamento;
	}

}
