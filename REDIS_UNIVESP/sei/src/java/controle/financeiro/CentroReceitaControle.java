package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas centroReceitaForm.jsp
 * centroReceitaCons.jsp) com as funcionalidades da classe <code>CentroReceita</code>. Implemtação da camada controle
 * (Backing Bean).
 * 
 * @see SuperControle
 * @see CentroReceita
 * @see CentroReceitaVO
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
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis; @Controller("CentroReceitaControle")
@Scope("viewScope")
@Lazy
public class CentroReceitaControle extends SuperControle implements Serializable {

	private CentroReceitaVO centroReceitaVO;
	private String centroReceitaPrincipal_Erro;
	protected List listaSelectItemDepartamento;
	protected List<SelectItem> listaSelectItemCentroReceitaPrincipal;
	protected Boolean readonlyDepartamento;
	protected Boolean readonlyCentroReceita;
	private String mascaraPadraoCentroReceita;
	private Integer tamanhoMascaraPadraoCentroReceita;

	public CentroReceitaControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>CentroReceita</code> para edição pelo
	 * usuário da aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setCentroReceitaVO(new CentroReceitaVO());
		inicializarListasSelectItemTodosComboBox();
		this.setReadonlyDepartamento(Boolean.FALSE);
		this.setReadonlyCentroReceita(Boolean.FALSE);
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("centroReceitaForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>CentroReceita</code> para alteração.
	 * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
	 * disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		CentroReceitaVO obj = (CentroReceitaVO) context().getExternalContext().getRequestMap().get("centroReceitaItens");
		obj.setNovoObj(Boolean.FALSE);
		setCentroReceitaVO(getFacadeFactory().getCentroReceitaFacade().consultarPorChavePrimariaUnica(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		setReadonlyDepartamento(Boolean.TRUE);
		setReadonlyCentroReceita(Boolean.TRUE);
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("centroReceitaForm.xhtml");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>CentroReceita</code>.
	 * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
	 * para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (centroReceitaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getCentroReceitaFacade().incluir(centroReceitaVO, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
				this.setReadonlyDepartamento(Boolean.TRUE);
				this.setReadonlyCentroReceita(Boolean.TRUE);
			} else {
				getFacadeFactory().getCentroReceitaFacade().alterar(centroReceitaVO, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
				getAplicacaoControle().removerCentroReceita(centroReceitaVO.getCodigo());
			}
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("centroReceitaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("centroReceitaForm.xhtml");
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP CentroReceitaCons.jsp. Define o tipo de consulta
	 * a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
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
				objs = getFacadeFactory().getCentroReceitaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("identificadorCentroReceitaCentroReceita")) {
				objs = getFacadeFactory().getCentroReceitaFacade().consultarPorIdentificadorCentroReceitaCentroReceita(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
//				if (getControleConsulta().getValorConsulta().equals("")) {
//					setMensagemID("msg_entre_dados");
//					return "consultar";
//				}
			}
			if (getControleConsulta().getCampoConsulta().equals("centroReceitaPrincipal")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getCentroReceitaFacade().consultarPorCentroReceitaPrincipal(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("identificadorCentroReceita")) {
				objs = getFacadeFactory().getCentroReceitaFacade().consultarPorIdentificadorCentroReceita(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeDepartamento")) {
				objs = getFacadeFactory().getCentroReceitaFacade().consultarPorNomeDepartamento(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("centroReceitaCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("centroReceitaCons.xhtml");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>CentroReceitaVO</code> Após a exclusão
	 * ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			if (consultarCentroReceitaPrincipalPorFilho(getCentroReceitaVO().getCodigo()).isEmpty()) {
				getFacadeFactory().getCentroReceitaFacade().excluir(centroReceitaVO, getUsuarioLogado());
				montarListaSelectItemCentroReceitaPrincipal("");
				setCentroReceitaVO(new CentroReceitaVO());
				setReadonlyCentroReceita(Boolean.FALSE);
				setReadonlyDepartamento(Boolean.FALSE);
				setMensagemID("msg_dados_excluidos");
			} else {
				setReadonlyCentroReceita(Boolean.TRUE);
				setReadonlyDepartamento(Boolean.TRUE);
				setMensagemDetalhada("msg_erro", "Existem Dependentes deste Centro de Receita");
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("centroReceitaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("centroReceitaForm.xhtml");
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

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
	 * relativo ao atributo <code>Departamento</code>.
	 */
	public void montarListaSelectItemDepartamento(String prm) throws Exception {
            List resultadoConsulta = null;
            Iterator i = null;
            try {
		resultadoConsulta = consultarDepartamentoPorNome(prm);
		i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			DepartamentoVO obj = (DepartamentoVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
		}
		setListaSelectItemDepartamento(objs);
            } catch (Exception e) {
                throw e;
            } finally {
                Uteis.liberarListaMemoria(resultadoConsulta);
                i = null;
            }
	}

	public void atualizarDepartamento() {
		Integer codigo = this.getCentroReceitaVO().getCentroReceitaPrincipal().getCodigo();
		try {
			CentroReceitaVO cr = (CentroReceitaVO) getFacadeFactory().getCentroReceitaFacade().consultarPorChavePrimaria(codigo, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			List resultadoConsulta = consultarDepartamentoPorDepartamentoSuperior(cr.getDepartamento().getCodigo());
			Iterator i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(cr.getDepartamento().getCodigo(), cr.getDepartamento().getNome().toString()));
			while (i.hasNext()) {
				DepartamentoVO obj = (DepartamentoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			setListaSelectItemDepartamento(objs);
		} catch (Exception e) {
			montarListaSelectItemDepartamento();
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>Departamento</code>. Buscando todos os
	 * objetos correspondentes a entidade <code>Departamento</code>. Esta rotina não recebe parâmetros para filtragem de
	 * dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemDepartamento() {
		try {
			montarListaSelectItemDepartamento("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
	 * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
	 * correspondente
	 */
	public List consultarDepartamentoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getDepartamentoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		return lista;
	}

	private List consultarDepartamentoPorDepartamentoSuperior(Integer codigo) throws Exception {
		List lista = getFacadeFactory().getDepartamentoFacade().consultarPorDepartamentoSuperior(codigo, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}

	public List consultarCentroReceitaPrincipalPorFilho(Integer codigo) throws Exception {
		List lista = getFacadeFactory().getCentroReceitaFacade().consultarPorCentroReceitaPrincipalFilho(codigo, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void montarListaSelectItemCentroReceitaPrincipal(String prm) throws Exception {
		getListaSelectItemCentroReceitaPrincipal().clear();
		List<CentroReceitaVO> centroReceitaVOs = consultarCentroReceitaPrincipalPorDescricao(prm);
		getListaSelectItemCentroReceitaPrincipal().add(new SelectItem(0, ""));
		if (Uteis.isAtributoPreenchido(centroReceitaVOs)) {
			centroReceitaVOs.stream()
				.filter(cr -> !cr.getCodigo().equals(getCentroReceitaVO().getCodigo()))
				.map(cr -> new SelectItem(cr.getCodigo(), cr.getDescricao() + " - " + cr.getIdentificadorCentroReceita()))
				.forEach(getListaSelectItemCentroReceitaPrincipal()::add);
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>Banco</code>. Buscando todos os objetos
	 * correspondentes a entidade <code>Banco</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
	 * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemCentroReceitaPrincipal() {
		try {
			montarListaSelectItemCentroReceitaPrincipal("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	public List<CentroReceitaVO> consultarCentroReceitaPrincipalPorDescricao(String nomePrm) throws Exception {
		return getFacadeFactory().getCentroReceitaFacade().consultarPorIdentificadorCentroReceita(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}

	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemDepartamento();
		montarListaSelectItemCentroReceitaPrincipal();
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>CentroReceita</code> por meio de sua respectiva
	 * chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave
	 * primária da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarCentroReceitaPorChavePrimaria() {
		try {
			Integer campoConsulta = centroReceitaVO.getCentroReceitaPrincipal().getCodigo();
			CentroReceitaVO centroReceita = getFacadeFactory().getCentroReceitaFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			centroReceitaVO.setIdentificadorCentroReceita(centroReceita.getIdentificadorCentroReceita());
			this.setCentroReceitaPrincipal_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			centroReceitaVO.setIdentificadorCentroReceita("");
			centroReceitaVO.getCentroReceitaPrincipal().setCodigo((0));
			this.setCentroReceitaPrincipal_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("identificadorCentroReceita", "Identificador do Centro de Receita"));
		itens.add(new SelectItem("nomeDepartamento", "Departamento"));
		itens.add(new SelectItem("centroReceitaPrincipal", "Centro de Receitas Principal"));                
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {         removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("centroReceitaCons.xhtml");
	}

	public List getListaSelectItemDepartamento() {
		return (listaSelectItemDepartamento);
	}

	public void setListaSelectItemDepartamento(List listaSelectItemDepartamento) {
		this.listaSelectItemDepartamento = listaSelectItemDepartamento;
	}

	public String getCentroReceitaPrincipal_Erro() {
		return centroReceitaPrincipal_Erro;
	}

	public void setCentroReceitaPrincipal_Erro(String centroReceitaPrincipal_Erro) {
		this.centroReceitaPrincipal_Erro = centroReceitaPrincipal_Erro;
	}

	public CentroReceitaVO getCentroReceitaVO() {
		return centroReceitaVO;
	}

	public void setCentroReceitaVO(CentroReceitaVO centroReceitaVO) {
		this.centroReceitaVO = centroReceitaVO;
	}

	public List<SelectItem> getListaSelectItemCentroReceitaPrincipal() {
		if (listaSelectItemCentroReceitaPrincipal == null) {
			listaSelectItemCentroReceitaPrincipal = new ArrayList<>();
		}
		return listaSelectItemCentroReceitaPrincipal;
	}

	public void setListaSelectItemCentroReceitaPrincipal(List<SelectItem> listaSelectItemCentroReceitaPrincipal) {
		this.listaSelectItemCentroReceitaPrincipal = listaSelectItemCentroReceitaPrincipal;
	}

	public Boolean getReadonlyDepartamento() {
		return readonlyDepartamento;
	}

	public void setReadonlyDepartamento(Boolean readonlyDepartamento) {
		this.readonlyDepartamento = readonlyDepartamento;
	}

	public Boolean getReadonlyCentroReceita() {
		return readonlyCentroReceita;
	}

	public void setReadonlyCentroReceita(Boolean readonlyCentroReceita) {
		this.readonlyCentroReceita = readonlyCentroReceita;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		centroReceitaVO = null;
		centroReceitaPrincipal_Erro = null;
		Uteis.liberarListaMemoria(listaSelectItemDepartamento);
		Uteis.liberarListaMemoria(listaSelectItemCentroReceitaPrincipal);
		readonlyDepartamento = null;
		readonlyCentroReceita = null;
	}
	
	public String getMascaraPadraoCentroReceita() {
		if (mascaraPadraoCentroReceita == null) {
			try {
				mascaraPadraoCentroReceita = getConfiguracaoFinanceiroPadraoSistema().getMascaraCentroReceita().replace("x", "9");
			} catch (Exception e) {
				mascaraPadraoCentroReceita = "";
			}
		}
		return mascaraPadraoCentroReceita;
	}

	public Integer getTamanhoMascaraPadraoCentroReceita() {
		if (tamanhoMascaraPadraoCentroReceita == null) {
			tamanhoMascaraPadraoCentroReceita = getMascaraPadraoCentroReceita().length();
		}
		return tamanhoMascaraPadraoCentroReceita;
	}
}
