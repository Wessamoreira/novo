package controle.basico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.basico.PaizVO;
import negocio.comuns.basico.enumeradores.RegiaoEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.basico.Estado;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas estadoForm.xhtml estadoCons.xhtml) com as funcionalidades da classe
 * <code>Estado</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Estado
 * @see EstadoVO
 */

@Controller("EstadoControle")
@Scope("viewScope")
@Lazy
public class EstadoControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6129362327635420829L;
	private EstadoVO estadoVO;
	protected List<SelectItem> listaSelectItemPaiz;
	protected List<SelectItem> listaSelectItemRegiao;

	/**
	 * Interface <code>EstadoInterfaceFacade</code> responsável pela
	 * interconexão da camada de controle com a camada de negócio. Criando uma
	 * independência da camada de controle com relação a tenologia de
	 * persistência dos dados (DesignPatter: Façade).
	 */

	public EstadoControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Estado</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setEstadoVO(new EstadoVO());
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("estadoForm");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>Estado</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		EstadoVO obj = (EstadoVO) context().getExternalContext().getRequestMap().get("estadoItem");
		obj.setNovoObj(Boolean.FALSE);
		setEstadoVO(obj);
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("estadoForm");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>Estado</code>. Caso o objeto seja novo (ainda não gravado
	 * no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o
	 * objeto não é gravado, sendo re-apresentado para o usuário juntamente com
	 * uma mensagem de erro.
	 */
	public void gravar() {
		try {
			getFacadeFactory().getEstadoFacade().persistir(getEstadoVO(), getUsuarioLogado());
			getAplicacaoControle().removerEstadoVO(getEstadoVO().getCodigo());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * EstadoCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultar() {
		try {
			List<EstadoVO> objs = new ArrayList<EstadoVO>(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getEstadoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("sigla")) {
				objs = getFacadeFactory().getEstadoFacade().consultarPorSigla(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getEstadoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomePaiz")) {
				objs = getFacadeFactory().getEstadoFacade().consultarPorNomePaiz(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<EstadoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "";
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>EstadoVO</code> Após a exclusão ela automaticamente aciona a rotina
	 * para uma nova inclusão.
	 */
	public void excluir() {
		try {
			getFacadeFactory().getEstadoFacade().excluir(getEstadoVO(), getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Paiz</code>.
	 */
	public void montarListaSelectItemPaiz(String prm) throws Exception {
		List<PaizVO> resultadoConsulta = null;
		Iterator<PaizVO> i = null;
		try {
			resultadoConsulta = consultarPaizPorNome(prm);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				PaizVO obj = (PaizVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			Uteis.liberarListaMemoria(resultadoConsulta);
			setListaSelectItemPaiz(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Paiz</code>. Buscando todos os objetos correspondentes a entidade
	 * <code>Paiz</code>. Esta rotina não recebe parâmetros para filtragem de
	 * dados, isto é importante para a inicialização dos dados da tela para o
	 * acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemPaiz() {
		try {
			montarListaSelectItemPaiz("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List<PaizVO> consultarPaizPorNome(String nomePrm) throws Exception {
		List<PaizVO> lista = getFacadeFactory().getPaizFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemPaiz();
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
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("sigla", "Sigla"));
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nomePaiz", "País"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<EstadoVO>(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("estadoCons");
	}

	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do
	 * backing bean. Garantindo uma melhor atuação do Garbage Coletor do Java. A
	 * mesma é automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		estadoVO = null;
		Uteis.liberarListaMemoria(listaSelectItemPaiz);
	}

	public List<SelectItem> getListaSelectItemPaiz() {
		if (listaSelectItemPaiz == null) {
			listaSelectItemPaiz = new ArrayList<SelectItem>(0);
		}
		return (listaSelectItemPaiz);
	}

	public List<SelectItem> getListaSelectItemRegiao() {
		if (listaSelectItemRegiao == null) {
			listaSelectItemRegiao = new ArrayList<SelectItem>(0);
			listaSelectItemRegiao.add(new SelectItem("", ""));
			for (RegiaoEnum regiaoEnum : RegiaoEnum.values()) {
				listaSelectItemRegiao.add(new SelectItem(regiaoEnum.name(), regiaoEnum.getValorApresentar()));
			}
		}
		return (listaSelectItemRegiao);
	}

	public void setListaSelectItemPaiz(List<SelectItem> listaSelectItemPaiz) {
		this.listaSelectItemPaiz = listaSelectItemPaiz;
	}

	public EstadoVO getEstadoVO() {
		return estadoVO;
	}

	public void setEstadoVO(EstadoVO estadoVO) {
		this.estadoVO = estadoVO;
	}
}
