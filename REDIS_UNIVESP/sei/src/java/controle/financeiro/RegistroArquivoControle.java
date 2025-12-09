package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas registroArquivoForm.jsp
 * registroArquivoCons.jsp) com as funcionalidades da classe <code>RegistroArquivo</code>. Implemtação da camada
 * controle (Backing Bean).
 * 
 * @see SuperControle
 * @see RegistroArquivo
 * @see RegistroArquivoVO
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
import negocio.comuns.financeiro.RegistroDetalheVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis; @Controller("RegistroArquivoControle")
@Scope("viewScope")
@Lazy
public class RegistroArquivoControle extends SuperControle implements Serializable {
	
	private RegistroArquivoVO registroArquivoVO;
	private RegistroDetalheVO registroDetalheVO;
	private String valorAtualFiltroNossoNumero;

	public RegistroArquivoControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>RegistroArquivo</code> para edição pelo
	 * usuário da aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setRegistroArquivoVO(new RegistroArquivoVO());
		setRegistroDetalheVO(new RegistroDetalheVO());
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("registroArquivoForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>RegistroArquivo</code> para
	 * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
	 * possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		RegistroArquivoVO obj = (RegistroArquivoVO) context().getExternalContext().getRequestMap().get("registroArquivoItens");
		obj = getFacadeFactory().getRegistroArquivoFacade().consultarPorChavePrimaria(obj.getCodigo(), getValorAtualFiltroNossoNumero(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		obj.setNovoObj(Boolean.FALSE);
		setRegistroArquivoVO(obj);
		setRegistroDetalheVO(new RegistroDetalheVO());
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("registroArquivoForm.xhtml");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>RegistroArquivo</code>.
	 * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
	 * para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (registroArquivoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getRegistroArquivoFacade().incluir(registroArquivoVO,true, getUsuarioLogado());
			} else {
				getFacadeFactory().getRegistroArquivoFacade().alterar(registroArquivoVO,true, getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroArquivoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroArquivoForm.xhtml");
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP RegistroArquivoCons.jsp. Define o tipo de
	 * consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("identificacaoTituloEmpresa")) {
				objs = getFacadeFactory().getRegistroArquivoFacade().consultarPorIdentificacaoTituloEmpresa(getValorAtualFiltroNossoNumero(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataGeracaoArquivo")) {
				objs = getFacadeFactory().getRegistroArquivoFacade().consultarPorDataGeracaoArquivo(getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), "", false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroArquivoCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroArquivoCons.xhtml");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>RegistroArquivoVO</code> Após a exclusão
	 * ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getRegistroArquivoFacade().excluir(registroArquivoVO,true, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroArquivoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroArquivoForm.xhtml");
		}
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe <code>RegistroDetalhe</code> para o objeto
	 * <code>registroArquivoVO</code> da classe <code>RegistroArquivo</code>
	 */
	public String adicionarRegistroDetalhe() throws Exception {
		try {
			if (!getRegistroArquivoVO().getCodigo().equals(0)) {
				registroDetalheVO.setRegistroArquivo(getRegistroArquivoVO());
			}
			getRegistroArquivoVO().adicionarObjRegistroDetalheVOs(getRegistroDetalheVO());
			this.setRegistroDetalheVO(new RegistroDetalheVO());
			setMensagemID("msg_dados_adicionados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroArquivoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroArquivoForm.xhtml");
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe <code>RegistroDetalhe</code> para edição pelo
	 * usuário.
	 */
	public String editarRegistroDetalhe() throws Exception {
		RegistroDetalheVO obj = (RegistroDetalheVO) context().getExternalContext().getRequestMap().get("registroDetalheItens");
		setRegistroDetalheVO(obj);
		return Uteis.getCaminhoRedirecionamentoNavegacao("registroArquivoForm.xhtml");
	}

	/*
	 * Método responsável por remover um novo objeto da classe <code>RegistroDetalhe</code> do objeto
	 * <code>registroArquivoVO</code> da classe <code>RegistroArquivo</code>
	 */
	public String removerRegistroDetalhe() throws Exception {
		RegistroDetalheVO obj = (RegistroDetalheVO) context().getExternalContext().getRequestMap().get("registroDetalheItens");
		getRegistroArquivoVO().excluirObjRegistroDetalheVOs(obj.getCodigo());
		setMensagemID("msg_dados_excluidos");
		return Uteis.getCaminhoRedirecionamentoNavegacao("registroArquivoForm.xhtml");
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
		itens.add(new SelectItem("identificacaoTituloEmpresa", "Nosso Número"));
		itens.add(new SelectItem("dataGeracaoArquivo", "Data Criação Arquivo"));
		return itens;
	}

	public boolean isTipoConsultaData() {
		if (getControleConsulta().getCampoConsulta().equals("dataGeracaoArquivo")) {
			return true;
		}
		return false;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {         removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("registroArquivoCons.xhtml");
	}

	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean. Garantindo uma melhor atuação
	 * do Garbage Coletor do Java. A mesma é automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		registroArquivoVO = null;
		registroDetalheVO = null;
	}

	public RegistroDetalheVO getRegistroDetalheVO() {
		return registroDetalheVO;
	}

	public void setRegistroDetalheVO(RegistroDetalheVO registroDetalheVO) {
		this.registroDetalheVO = registroDetalheVO;
	}

	public RegistroArquivoVO getRegistroArquivoVO() {
		return registroArquivoVO;
	}

	public void setRegistroArquivoVO(RegistroArquivoVO registroArquivoVO) {
		this.registroArquivoVO = registroArquivoVO;
	}

	public String getValorAtualFiltroNossoNumero() {
		return valorAtualFiltroNossoNumero;
	}

	public void setValorAtualFiltroNossoNumero(String valorAtualFiltroNossoNumero) {
		this.valorAtualFiltroNossoNumero = valorAtualFiltroNossoNumero;
	}
	
	public String navegarParaControleCobranca(){
		return Uteis.getCaminhoRedirecionamentoNavegacao("controleCobrancaCons");
	}
}