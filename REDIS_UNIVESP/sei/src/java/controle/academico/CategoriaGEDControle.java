package controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.administrativo.CategoriaGEDVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas categoriaGEDForm.xhtml é categoriaGEDCons.xhtl com as funcionalidades
 * da classe <code>CategoriaGEDVO</code>. Implemtação da camada controle
 * (Backing Bean).
 * 
 * @see SuperControle
 * @see CategoriaGEDVO
 */
@Controller("CategoriaGEDControle")
@Scope("viewScope")
@Lazy
public class CategoriaGEDControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 2850426279438964071L;

	public CategoriaGEDControle() {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	private CategoriaGEDVO categoriaGEDVO;

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>CategoriaGED</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setCategoriaGEDVO(new CategoriaGEDVO());
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaGEDForm");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>CategoriaGED</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP correspondente
	 * possa disponibilizá-lo para edição.
	 */
	public String editar() {
		CategoriaGEDVO obj = (CategoriaGEDVO) context().getExternalContext().getRequestMap().get("tipo");
		obj.setNovoObj(Boolean.FALSE);
		setCategoriaGEDVO(obj);
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaGEDForm");
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
		getControleConsulta().setValorConsulta("");
		return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaGEDCons");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da
	 * classe <code>CategoriaGED</code>. Caso o objeto seja novo (ainda não gravado
	 * no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto
	 * não é gravado, sendo re-apresentado para o usuário juntamente com uma
	 * mensagem de erro.
	 */
	public String persistir() {
		try {
			getFacadeFactory().getCategoriaGEDInterfaceFacade().persistir(categoriaGEDVO, Boolean.TRUE,
					getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaGEDForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaGEDForm");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>TipoDocumentoVO</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getCategoriaGEDInterfaceFacade().excluir(categoriaGEDVO, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaGEDForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaGEDForm");
		}
	}

	/**
	 * Rotina responsável por executar as consultas disponiveis do
	 * CategoriaGEDCons.xhtml. Define o tipo de consulta a ser executada, por meio
	 * de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessão da
	 * página.
	 */
	public String consultar() {
		try {
			super.consultar();
			List<CategoriaGEDVO> objs = new ArrayList<CategoriaGEDVO>(0);

			objs = getFacadeFactory().getCategoriaGEDInterfaceFacade().consultarPorFiltro(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());

			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return "";
		} catch (Exception e) {
			setListaConsulta(new ArrayList<TipoDocumentoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificador", "Identificador"));
		return itens;
	}

	/**
	 * Rotina responsavel por realizar a paginação da pagina de categoriaGEDCons.xhtml
	 * 
	 * @param DataScrollEvent
	 * @throws Exception
	 */
	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
    }

	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do backing
	 * bean. Garantindo uma melhor atuação do Garbage Coletor do Java. A mesma é
	 * automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		categoriaGEDVO = null;
	}

	public CategoriaGEDVO getCategoriaGEDVO() {
		return categoriaGEDVO;
	}

	public void setCategoriaGEDVO(CategoriaGEDVO categoriaGEDVO) {
		this.categoriaGEDVO = categoriaGEDVO;
	}

}
