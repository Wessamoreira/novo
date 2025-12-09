package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas registroTrailerForm.jsp
 * registroTrailerCons.jsp) com as funcionalidades da classe <code>RegistroTrailer</code>. Implemtação da camada
 * controle (Backing Bean).
 * 
 * @see SuperControle
 * @see RegistroTrailer
 * @see RegistroTrailerVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.financeiro.RegistroArquivoVO;
import negocio.comuns.financeiro.RegistroTrailerVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis; @Controller("RegistroTrailerControle")
@Scope("request")
@Lazy
public class RegistroTrailerControle extends SuperControle implements Serializable {

	private RegistroTrailerVO registroTrailerVO;
	private String campoConsultarRegistroArquivo;
	private String valorConsultarRegistroArquivo;
	private List listaConsultarRegistroArquivo;

	public RegistroTrailerControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>RegistroTrailer</code> para edição pelo
	 * usuário da aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setRegistroTrailerVO(new RegistroTrailerVO());
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>RegistroTrailer</code> para
	 * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
	 * possa disponibilizá-lo para edição.
	 */
	public String editar() {
		RegistroTrailerVO obj = (RegistroTrailerVO) context().getExternalContext().getRequestMap().get("registroTrailer");
		obj.setNovoObj(Boolean.FALSE);
		setRegistroTrailerVO(obj);
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>RegistroTrailer</code>.
	 * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
	 * para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (registroTrailerVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getRegistroTrailerFacade().incluir(registroTrailerVO, getUsuarioLogado());
			} else {
				getFacadeFactory().getRegistroTrailerFacade().alterar(registroTrailerVO, getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP RegistroTrailerCons.jsp. Define o tipo de
	 * consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
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
				objs = getFacadeFactory().getRegistroTrailerFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>RegistroTrailerVO</code> Após a exclusão
	 * ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getRegistroTrailerFacade().excluir(registroTrailerVO, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	public void selecionarRegistroArquivo() throws Exception {
		RegistroArquivoVO obj = (RegistroArquivoVO) context().getExternalContext().getRequestMap().get("registroArquivo");
		if (getMensagemDetalhada().equals("")) {
			// this.getRegistroTrailerVO().setRegistroArquivo(obj);
		}
		Uteis.liberarListaMemoria(this.getListaConsultarRegistroArquivo());
		this.setValorConsultarRegistroArquivo(null);
		this.setCampoConsultarRegistroArquivo(null);
	}

	public void limparCampoRegistroArquivo() {
		// this.getRegistroTrailerVO().setRegistroArquivo( new RegistroArquivoVO());
	}

	/**
	 * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
	 */
	public List getTipoConsultarComboRegistroArquivo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Número"));
		itens.add(new SelectItem("registroHeader", "Registro Header"));
		return itens;
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
		itens.add(new SelectItem("codigo", "Codigo"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {         removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return "consultar";
	}

	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean. Garantindo uma melhor atuação
	 * do Garbage Coletor do Java. A mesma é automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		registroTrailerVO = null;
	}

	public String getCampoConsultarRegistroArquivo() {
		return campoConsultarRegistroArquivo;
	}

	public void setCampoConsultarRegistroArquivo(String campoConsultarRegistroArquivo) {
		this.campoConsultarRegistroArquivo = campoConsultarRegistroArquivo;
	}

	public String getValorConsultarRegistroArquivo() {
		return valorConsultarRegistroArquivo;
	}

	public void setValorConsultarRegistroArquivo(String valorConsultarRegistroArquivo) {
		this.valorConsultarRegistroArquivo = valorConsultarRegistroArquivo;
	}

	public List getListaConsultarRegistroArquivo() {
		return listaConsultarRegistroArquivo;
	}

	public void setListaConsultarRegistroArquivo(List listaConsultarRegistroArquivo) {
		this.listaConsultarRegistroArquivo = listaConsultarRegistroArquivo;
	}

	public RegistroTrailerVO getRegistroTrailerVO() {
		return registroTrailerVO;
	}

	public void setRegistroTrailerVO(RegistroTrailerVO registroTrailerVO) {
		this.registroTrailerVO = registroTrailerVO;
	}
}