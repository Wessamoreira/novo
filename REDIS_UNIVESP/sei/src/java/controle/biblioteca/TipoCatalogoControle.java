package controle.biblioteca;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas TipoCatalogoForm.jsp
 * TipoCatalogoCons.jsp) com as funcionalidades da classe <code>TipoCatalogo</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see TipoCatalogo
 * @see TipoCatalogoVO
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
import negocio.comuns.biblioteca.TipoCatalogoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis; 

@Controller("TipoCatalogoControle")
@Scope("viewScope")
@Lazy
public class TipoCatalogoControle extends SuperControle implements Serializable {

	private TipoCatalogoVO tipoCatalogoVO;
	protected List<SelectItem> listaSelectItemTipoCatalogoSubdivisao;

	public TipoCatalogoControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>TipoCatalogo</code> para edição pelo usuário da
	 * aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setTipoCatalogoVO(new TipoCatalogoVO());
		setMensagemID("msg_entre_dados");
		montarListaSelectItemTipoCatalogoSubdivisao();
		return Uteis.getCaminhoRedirecionamentoNavegacao("tipoCatalogoForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>TipoCatalogo</code> para alteração. O
	 * objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
	 * disponibilizá-lo para edição.
	 */
	public String editar() {
		TipoCatalogoVO obj = (TipoCatalogoVO) context().getExternalContext().getRequestMap().get("tipoCatalogoItens");
		obj.setNovoObj(Boolean.FALSE);
		setTipoCatalogoVO(obj);
		setMensagemID("msg_dados_editar");
		montarListaSelectItemTipoCatalogoSubdivisao();
		return Uteis.getCaminhoRedirecionamentoNavegacao("tipoCatalogoForm.xhtml");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>TipoCatalogo</code>. Caso o
	 * objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
	 * para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (tipoCatalogoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getTipoCatalogoFacade().incluir(tipoCatalogoVO, getUsuarioLogado());
			} else {
				getFacadeFactory().getTipoCatalogoFacade().alterar(tipoCatalogoVO, getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoCatalogoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoCatalogoForm.xhtml");
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP TipoCatalogoCons.jsp. Define o tipo de consulta a ser
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
				objs = getFacadeFactory().getTipoCatalogoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getTipoCatalogoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoCatalogoCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoCatalogoCons.xhtml");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>TipoCatalogoVO</code> Após a exclusão ela
	 * automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getTipoCatalogoFacade().excluir(tipoCatalogoVO, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoCatalogoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoCatalogoForm.xhtml");
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
		return Uteis.getCaminhoRedirecionamentoNavegacao("tipoCatalogoCons.xhtml");
	}

	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean. Garantindo uma melhor atuação
	 * do Garbage Coletor do Java. A mesma é automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		tipoCatalogoVO = null;
	}
	
	public TipoCatalogoVO getTipoCatalogoVO() {
		return tipoCatalogoVO;
	}

	public void setTipoCatalogoVO(TipoCatalogoVO tipoCatalogoVO) {
		this.tipoCatalogoVO = tipoCatalogoVO;
	}
	
	
	public List<SelectItem> getListaSelectItemTipoCatalogoSubdivisao() {
        if (listaSelectItemTipoCatalogoSubdivisao == null) {
        	listaSelectItemTipoCatalogoSubdivisao = new ArrayList<SelectItem>();
        }
        return listaSelectItemTipoCatalogoSubdivisao;
    }

    public void setListaSelectItemTipoCatalogoSubdivisao (List<SelectItem> listaSelectItemTipoCatalogoSubdivisao) {    
        this.listaSelectItemTipoCatalogoSubdivisao = listaSelectItemTipoCatalogoSubdivisao;
    }
	
    
    public void montarListaSelectItemTipoCatalogoSubdivisao() {
        try {
        	montarListaSelectItemTipoCatalogoSubdivisao("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }
    
   
    public void montarListaSelectItemTipoCatalogoSubdivisao(String codigoPrm) throws Exception {
    	int codigo = 0;
    	List<TipoCatalogoVO> resultadoConsulta = null;
    	Iterator<TipoCatalogoVO> i = null;    	    	
    	try {
    		resultadoConsulta = consultarTipoCatalogoSubdivisaoPorIdentificador(codigo);
    		
    		i = resultadoConsulta.iterator();
	 		List<SelectItem> objs = new ArrayList<SelectItem>();
    		objs.add(new SelectItem(0, ""));
    		
    		while (i.hasNext()) {
    			TipoCatalogoVO obj = (TipoCatalogoVO) i.next();
    			if(!obj.getCodigo().equals(getTipoCatalogoVO().getCodigo())) {
    				objs.add(new SelectItem(obj.getCodigo(), obj.getSigla() + " - " + obj.getNome()));
    			}
    		}
    		
    		setListaSelectItemTipoCatalogoSubdivisao(objs);
    	} catch (Exception e) {
    		throw e;
    	} finally {
    		Uteis.liberarListaMemoria(resultadoConsulta);
    		i = null;
    	}

    }
    
    public List<TipoCatalogoVO> consultarTipoCatalogoSubdivisaoPorIdentificador(Integer codigo) throws Exception {
    	List<TipoCatalogoVO> listaTipoCatalogo = getFacadeFactory().getTipoCatalogoFacade().consultarSubdivisaoSuperiorPorIdentificador(codigo, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
    	return listaTipoCatalogo;
    }
	
	public void consultarTipoCatalogoSuperior(){		
		try {
			getTipoCatalogoVO().setArvoreTipoCatalogo(getFacadeFactory().getTipoCatalogoFacade().consultarArvoreTipoCatalogoSuperior(getTipoCatalogoVO(), false, getUsuarioLogado()));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarTipoCatalogoInferior(){
		try {
			getTipoCatalogoVO().setArvoreTipoCatalogo(getFacadeFactory().getTipoCatalogoFacade().consultarArvoreTipoCatalogoInferior(getTipoCatalogoVO(), false, getUsuarioLogado()));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void atualizarTipoCatalogo() {        
        try {
        	if(Uteis.isAtributoPreenchido(getTipoCatalogoVO().getSubdivisao())){
        		getTipoCatalogoVO().setSigla(getTipoCatalogoVO().getSigla());
        		getTipoCatalogoVO().setNome(getTipoCatalogoVO().getNome());
        		getTipoCatalogoVO().setSubdivisao(getTipoCatalogoVO().getSubdivisao());
        	}
        	limparMensagem();
        } catch (Exception e) {
        	setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
}