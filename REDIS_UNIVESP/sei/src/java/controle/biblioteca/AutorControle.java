package controle.biblioteca;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas autorForm.jsp autorCons.jsp) com as funcionalidades da classe
 * <code>Autor</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Autor
 * @see AutorVO
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.biblioteca.AutorVO;
import negocio.comuns.biblioteca.AutorVariacaoNomeVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("AutorControle")
@Scope("viewScope")
@Lazy
public class AutorControle extends SuperControle implements Serializable {

	private AutorVO autorVO;
	private AutorVariacaoNomeVO autorVariacaoNomeVO;
	private List<AutorVariacaoNomeVO> listaAutorVariacaoNome;
	private List<AutorVariacaoNomeVO> listaAutorVariacaoNomeRemovido;

	public AutorControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Autor</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setAutorVO(new AutorVO());
		setAutorVariacaoNomeVO(new AutorVariacaoNomeVO());
		getListaAutorVariacaoNome().clear();
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("autorForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>Autor</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			AutorVO obj = (AutorVO) context().getExternalContext().getRequestMap().get("autorItens");
			setAutorVO(getFacadeFactory().getAutorFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
//			obj.setListaAutorVariacaoNome(getFacadeFactory().getAutorVariacaoNomeFacade().consultarPorAutor(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS));
//			setListaAutorVariacaoNome(obj.getListaAutorVariacaoNome());
//			obj.setNovoObj(Boolean.FALSE);
//			setAutorVO(obj);
			setListaAutorVariacaoNome(getAutorVO().getListaAutorVariacaoNome());
			getAutorVO().setNovoObj(Boolean.FALSE);
			inicializarListasSelectItemTodosComboBox();
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("autorForm.xhtml");
	}

	/**
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>AutorVariacaoNome</code> para edição pelo usuário.
	 */
	public String editarAutorVariacaoNome() throws Exception {
		AutorVariacaoNomeVO obj = (AutorVariacaoNomeVO) context().getExternalContext().getRequestMap().get("autorVariacaoNomeItens");
		setAutorVariacaoNomeVO(obj);
		return Uteis.getCaminhoRedirecionamentoNavegacao("autorForm.xhtml");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>Autor</code>. Caso o objeto seja novo (ainda não gravado
	 * no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o
	 * objeto não é gravado, sendo re-apresentado para o usuário juntamente com
	 * uma mensagem de erro.
	 */
	public String gravar() {
		try {
			getAutorVO().setListaAutorVariacaoNome(getListaAutorVariacaoNome());
			if (autorVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getAutorFacade().incluir(autorVO, getUsuarioLogado());
			} else {
				getFacadeFactory().getAutorFacade().alterar(autorVO, listaAutorVariacaoNomeRemovido);
			}
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("autorForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("autorForm.xhtml");
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * AutorCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				if(!Uteis.getIsValorNumerico(getControleConsulta().getValorConsulta())) {
					throw new Exception("Informe apenas valores numéricos.");
	            }
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				setListaConsulta(getFacadeFactory().getAutorFacade().consultaRapidaNivelComboBoxPorCodigo(new Integer(valorInt), getUsuarioLogado()));
				setMensagemID("msg_dados_consultados");
			} else if (getControleConsulta().getCampoConsulta().equals("nome")) {
				if (getControleConsulta().getValorConsulta() != null && !getControleConsulta().getValorConsulta().equals("") && getControleConsulta().getValorConsulta().length() >= 2) {
					setListaConsulta(getFacadeFactory().getAutorFacade().consultaRapidaNivelComboBoxPorNome(getControleConsulta().getValorConsulta(), getUsuarioLogado()));
					setMensagemID("msg_dados_consultados");
				} else {
					setMensagemID("msg_Autor_valorConsultaVazio");
				}
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("autorCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("autorCons.xhtml");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>AutorVO</code> Após a exclusão ela automaticamente aciona a rotina
	 * para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getAutorFacade().excluir(autorVO);
			novo();
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("autorForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("autorForm.xhtml");
		}
	}

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
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
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		limparMensagem();
		getListaAutorVariacaoNome().clear();
		getListaAutorVariacaoNomeRemovido().clear();
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("autorCons.xhtml");
	}

	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do
	 * backing bean. Garantindo uma melhor atuação do Garbage Coletor do Java. A
	 * mesma é automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		autorVO = null;
	}

	/**
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>AutorVariacaoNome</code> na lista de AutorVariacaoNomeVO do objeto
	 * <code>autorVO</code> da classe <code>Autor</code>
	 */
	public void adicionarVariacaoNome() throws Exception {
		Boolean controleLista = true;
		try {
			if (getAutorVariacaoNomeVO() != null && getAutorVariacaoNomeVO().getVariacaoNome() != null && !getAutorVariacaoNomeVO().getVariacaoNome().equals("")) {
				int index = 0;

				Iterator<AutorVariacaoNomeVO> i = getListaAutorVariacaoNome().iterator();
				while (i.hasNext()) {
					AutorVariacaoNomeVO objExistente = (AutorVariacaoNomeVO) i.next();
					if (objExistente.getVariacaoNome().equals(getAutorVariacaoNomeVO().getVariacaoNome())) {
						controleLista = false;
						break;
					}
					index++;
				}
				if (controleLista) {
					getListaAutorVariacaoNome().add(getAutorVariacaoNomeVO());
				}

			} else {
				throw new Exception("O campo VARIAÇÃO NOME deve ser informado");
			}
			setAutorVariacaoNomeVO(new AutorVariacaoNomeVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			controleLista = null;
		}
	}

	/**
	 * Método responsável por remover um novo objeto da classe
	 * <code>AutorVariacaoNome</code> do objeto <code>autorVO</code> da classe
	 * <code>autor</code>
	 */
	public void removerAutorVariacaoNome() throws Exception {
		try {
			AutorVariacaoNomeVO obj = (AutorVariacaoNomeVO) context().getExternalContext().getRequestMap().get("autorVariacaoNomeItens");
			if (obj != null && (obj.getVariacaoNome() != null || !obj.getVariacaoNome().equals(""))) {
				int index = 0;
				for (AutorVariacaoNomeVO autorVariacaoNomeVO : getListaAutorVariacaoNome()) {
					if (autorVariacaoNomeVO.getVariacaoNome().equals(obj.getVariacaoNome())) {
						getListaAutorVariacaoNome().remove(index);
						getListaAutorVariacaoNomeRemovido().add(obj);
						break;
					}
					index++;
				}
			}
		} catch (Exception e) {
			setMensagemID("msg_Autor_falhaNaRemocaoAutorVariacaoNome");
		}
		setMensagemID("msg_dados_excluidos");
	}

	public AutorVO getAutorVO() {
		if(autorVO == null){
			autorVO = new AutorVO();
		}
		return autorVO;
	}

	public void setAutorVO(AutorVO autorVO) {
		this.autorVO = autorVO;
	}

	public AutorVariacaoNomeVO getAutorVariacaoNomeVO() {
		if (autorVariacaoNomeVO == null) {
			autorVariacaoNomeVO = new AutorVariacaoNomeVO();
		}
		return autorVariacaoNomeVO;
	}

	public void setAutorVariacaoNomeVO(AutorVariacaoNomeVO autorVariacaoNomeVO) {
		this.autorVariacaoNomeVO = autorVariacaoNomeVO;
	}

	public List<AutorVariacaoNomeVO> getListaAutorVariacaoNome() {
		if (listaAutorVariacaoNome == null) {
			listaAutorVariacaoNome = new ArrayList<AutorVariacaoNomeVO>(0);
		}
		return listaAutorVariacaoNome;
	}

	public void setListaAutorVariacaoNome(List<AutorVariacaoNomeVO> listaAutorVariacaoNome) {
		this.listaAutorVariacaoNome = listaAutorVariacaoNome;
	}

	public List<AutorVariacaoNomeVO> getListaAutorVariacaoNomeRemovido() {
		if (listaAutorVariacaoNomeRemovido == null) {
			listaAutorVariacaoNomeRemovido = new ArrayList<AutorVariacaoNomeVO>(0);
		}
		return listaAutorVariacaoNomeRemovido;
	}

	public void setListaAutorVariacaoNomeRemovido(List<AutorVariacaoNomeVO> listaAutorVariacaoNomeRemovido) {
		this.listaAutorVariacaoNomeRemovido = listaAutorVariacaoNomeRemovido;
	}

}