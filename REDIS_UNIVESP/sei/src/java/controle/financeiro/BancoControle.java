package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas bancoForm.jsp bancoCons.jsp) com
 * as funcionalidades da classe <code>Banco</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Banco
 * @see BancoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.ModeloGeracaoBoleto;
import negocio.comuns.utilitarias.dominios.ModeloGeracaoBoletoSicoob;

@Controller("BancoControle")
@Scope("viewScope")
@Lazy
public class BancoControle extends SuperControle implements Serializable {

	private BancoVO bancoVO;
	private List listaSelectItemModeloBoleto;
	private List listaSelectItemModeloGeracaoBoleto;
	private List listaSelectItemModeloGeracaoBoletoSicoob;
	private Boolean apresentarBanco;

	public BancoControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setApresentarBanco(false);
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Banco</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setBancoVO(new BancoVO());
		montarListaSelectItemModeloBoleto();
		montarListaSelectItemModeloGeracaoBoleto();
		montarListaSelectItemModeloGeracaoBoletoSicoob();		
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("bancoForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>Banco</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		BancoVO obj = (BancoVO) context().getExternalContext().getRequestMap().get("bancoItens");
		try {
			obj = getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		} catch (Exception r) {
		}
		montarListaSelectItemModeloBoleto();
		montarListaSelectItemModeloGeracaoBoleto();
		montarListaSelectItemModeloGeracaoBoletoSicoob();		
		obj.setNovoObj(Boolean.FALSE);
		setBancoVO(obj);
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("bancoForm.xhtml");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>Banco</code>. Caso o objeto seja novo (ainda não gravado
	 * no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o
	 * objeto não é gravado, sendo re-apresentado para o usuário juntamente com
	 * uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (bancoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getBancoFacade().incluir(bancoVO, getUsuarioLogado());
			} else {
				getFacadeFactory().getBancoFacade().alterar(bancoVO, getUsuarioLogado());
				getAplicacaoControle().atualizarBancoAgenciaContaCorrente(bancoVO);
			}
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("bancoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("bancoForm.xhtml");
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * BancoCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
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
				objs = getFacadeFactory().getBancoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getBancoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nrBanco")) {
				objs = getFacadeFactory().getBancoFacade().consultarPorNrBanco(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("bancoCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("bancoCons.xhtml");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>BancoVO</code> Após a exclusão ela automaticamente aciona a rotina
	 * para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getBancoFacade().excluir(bancoVO, getUsuarioLogado());
			setBancoVO(new BancoVO());
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("bancoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("bancoForm.xhtml");
		}
	}

	public void irPaginaInicial() throws Exception {
		this.consultar();
	}

	public void montarListaSelectItemModeloBoleto() {
		try {
			List modeloBoletoVOs = getFacadeFactory().getModeloBoletoFacade().listarTodos(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			List<SelectItem> lista = UtilSelectItem.getListaSelectItem(modeloBoletoVOs, "codigo", "nome");
			setListaSelectItemModeloBoleto(lista);
		} catch (Exception e) {
			this.setMensagemDetalhada("msg_erro", e.getMessage());
		}
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
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("nrBanco", "Número do Banco"));
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
		return Uteis.getCaminhoRedirecionamentoNavegacao("bancoCons.xhtml");
	}

	public BancoVO getBancoVO() {
		if(bancoVO == null) {
			setBancoVO(new BancoVO());
		}
		return bancoVO;
	}

	public void setBancoVO(BancoVO bancoVO) {
		this.bancoVO = bancoVO;
	}

	/**
	 * @return the listaSelectItemModeloBoleto
	 */
	public List getListaSelectItemModeloBoleto() {
		if (listaSelectItemModeloBoleto == null) {
			listaSelectItemModeloBoleto = new ArrayList(0);
		}
		return listaSelectItemModeloBoleto;
	}

	/**
	 * @param listaSelectItemModeloBoleto
	 *            the listaSelectItemModeloBoleto to set
	 */
	public void setListaSelectItemModeloBoleto(List listaSelectItemModeloBoleto) {
		this.listaSelectItemModeloBoleto = listaSelectItemModeloBoleto;
	}

    public void montarListaSelectItemModeloGeracaoBoleto() {
        List obj = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(ModeloGeracaoBoleto.class);
        obj.remove (0);
        setListaSelectItemModeloGeracaoBoleto(obj);
    }

    public void montarListaSelectItemModeloGeracaoBoletoSicoob() {
    	List obj = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(ModeloGeracaoBoletoSicoob.class);
    	obj.remove (0);
    	setListaSelectItemModeloGeracaoBoletoSicoob(obj);
    }
	
	public List getListaSelectItemModeloGeracaoBoleto() {
		if (listaSelectItemModeloGeracaoBoleto == null) {
			listaSelectItemModeloGeracaoBoleto = new ArrayList(0);
		}
		return listaSelectItemModeloGeracaoBoleto;
	}

	public void setListaSelectItemModeloGeracaoBoleto(List listaSelectItemModeloGeracaoBoleto) {
		this.listaSelectItemModeloGeracaoBoleto = listaSelectItemModeloGeracaoBoleto;
	}

	public List getListaSelectItemModeloGeracaoBoletoSicoob() {
		if (listaSelectItemModeloGeracaoBoletoSicoob == null) {
			listaSelectItemModeloGeracaoBoletoSicoob = new ArrayList(0);
		}
		return listaSelectItemModeloGeracaoBoletoSicoob;
	}
	
	public void setListaSelectItemModeloGeracaoBoletoSicoob(List listaSelectItemModeloGeracaoBoletoSicoob) {
		this.listaSelectItemModeloGeracaoBoletoSicoob = listaSelectItemModeloGeracaoBoletoSicoob;
	}
	
	public Boolean getApresentarBanco() {
		if (apresentarBanco == null) {
			apresentarBanco = false;
		}
		return apresentarBanco;
	}
	
	public void setApresentarBanco(Boolean apresentarBanco) {
		this.apresentarBanco = apresentarBanco;
	}
}
