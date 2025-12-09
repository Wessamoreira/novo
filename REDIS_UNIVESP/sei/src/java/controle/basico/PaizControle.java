package controle.basico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.basico.PaizVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.basico.Paiz;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas paizForm.jsp paizCons.jsp) com as funcionalidades da classe <code>Paiz</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Paiz
 * @see PaizVO
 */

@Controller("PaizControle")
@Scope("viewScope")
@Lazy
public class PaizControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	private PaizVO paizVO;

	public PaizControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>Paiz</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setPaizVO(new PaizVO());
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("paizForm");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Paiz</code> para alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			setPaizVO((PaizVO) context().getExternalContext().getRequestMap().get("paizItem"));
			getPaizVO().setNovoObj(false);
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("paizForm");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Paiz</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (getPaizVO().isNovoObj().booleanValue()) {
				getFacadeFactory().getPaizFacade().incluir(getPaizVO(), getUsuarioLogado());
			} else {
				getFacadeFactory().getPaizFacade().alterar(getPaizVO());
				getAplicacaoControle().removerPaizVO(getPaizVO().getCodigo());
			}
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("paizForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("paizForm");
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP PaizCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	public String consultar() {
		try {
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getPaizFacade().consultarPorCodigo(new Integer(valorInt), true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getPaizFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, getUsuarioLogado());
			}
			getControleConsulta().setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getControleConsulta().setListaConsulta(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "";
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>PaizVO</code> Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getPaizFacade().excluir(getPaizVO());
			setPaizVO(new PaizVO());
			setMensagemID("msg_dados_excluidos");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
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
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<SelectItem>(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("paizCons");
	}

	public PaizVO getPaizVO() {
		if (paizVO == null) {
			paizVO = new PaizVO();
		}
		return paizVO;
	}

	public void setPaizVO(PaizVO paizVO) {
		this.paizVO = paizVO;
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultar();
	}
}