package controle.faturamento.nfe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.faturamento.nfe.ImpostoVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author PedroOtimize
 *
 */
@Controller("ImpostoControle")
@Scope("viewScope")
@Lazy
public class ImpostoControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8859650582613750671L;
	private ImpostoVO impostoVO;

	public ImpostoControle() throws Exception {
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setCampoConsulta("nome");
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>Imposto</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setImpostoVO(new ImpostoVO());
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("impostoForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Imposto</code> para alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			ImpostoVO obj = (ImpostoVO) context().getExternalContext().getRequestMap().get("impostoItens");
			setImpostoVO(getFacadeFactory().getImpostoFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("impostoForm.xhtml");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Imposto</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public void persistir() {
		try {
			getFacadeFactory().getImpostoFacade().persistir(getImpostoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP ImpostoCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	public void consultarDados() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getImpostoFacade().consultar(getControleConsultaOtimizado());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>ImpostoVO</code> Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getImpostoFacade().excluir(getImpostoVO(), true, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("impostoForm.xhtml");
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

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> tipoConsultaCombo;

	public List<SelectItem> getTipoConsultaCombo() {
		if (tipoConsultaCombo == null) {
			tipoConsultaCombo = new ArrayList<>(0);
			tipoConsultaCombo.add(new SelectItem("nome", "Nome"));
			tipoConsultaCombo.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaCombo;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setCampoConsulta("nome");
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("impostoCons.xhtml");
	}

	public ImpostoVO getImpostoVO() {
		if (impostoVO == null) {
			impostoVO = new ImpostoVO();
		}
		return impostoVO;
	}

	public void setImpostoVO(ImpostoVO impostoVO) {
		this.impostoVO = impostoVO;
	}

}
