package controle.biblioteca;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas secaoForm.jsp secaoCons.jsp) com
 * as funcionalidades da classe <code>Secao</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Secao
 * @see SecaoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.biblioteca.SecaoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis; @Controller("SecaoControle")
@Scope("viewScope")
@Lazy
public class SecaoControle extends SuperControle implements Serializable {
	private SecaoVO secaoVO;

	public SecaoControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>Secao</code> para edição pelo usuário da
	 * aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setSecaoVO(new SecaoVO());
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("secaoForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Secao</code> para alteração. O objeto
	 * desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
	 * disponibilizá-lo para edição.
	 */
	public String editar() {
		SecaoVO obj = (SecaoVO) context().getExternalContext().getRequestMap().get("secaoItens");
		obj.setNovoObj(Boolean.FALSE);
		setSecaoVO(obj);
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("secaoForm.xhtml");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Secao</code>. Caso o
	 * objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
	 * para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (secaoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getSecaoFacade().incluir(secaoVO, getUsuarioLogado());
			} else {
				getFacadeFactory().getSecaoFacade().alterar(secaoVO, getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("secaoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("secaoForm.xhtml");
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP SecaoCons.jsp. Define o tipo de consulta a ser
	 * executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
	 * disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getSecaoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getSecaoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("secaoCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("secaoCons.xhtml");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>SecaoVO</code> Após a exclusão ela
	 * automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getSecaoFacade().excluir(secaoVO, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("secaoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("secaoForm.xhtml");
		}
	}

	/**
	 * Rotina responsável por atribui um javascript com o método de mascara para campos do tipo Data, CPF, CNPJ, etc.
	 */
	public String getMascaraConsulta() {
		return "";
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {         removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("secaoCons.xhtml");
	}

	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean. Garantindo uma melhor atuação
	 * do Garbage Coletor do Java. A mesma é automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		secaoVO = null;
	}

	public SecaoVO getSecaoVO() {
		return secaoVO;
	}

	public void setSecaoVO(SecaoVO secaoVO) {
		this.secaoVO = secaoVO;
	}
}