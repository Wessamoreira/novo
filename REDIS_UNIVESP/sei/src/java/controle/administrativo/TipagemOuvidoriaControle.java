package controle.administrativo;


import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.TipagemOuvidoriaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author Pedro
 */

@Controller("TipagemOuvidoriaControle")
@Scope("viewScope")
@Lazy
public class TipagemOuvidoriaControle extends SuperControle {

	private TipagemOuvidoriaVO tipagemOuvidoriaVO;

	public TipagemOuvidoriaControle() throws Exception {
		setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>TitulacaoCurso</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		setTipagemOuvidoriaVO(new TipagemOuvidoriaVO());
		getControleConsulta().setListaConsulta(null);
		getControleConsulta().setValorConsulta("");
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("tipagemOuvidoriaForm");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>TitulacaoCurso</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			TipagemOuvidoriaVO obj = (TipagemOuvidoriaVO) context().getExternalContext().getRequestMap().get("tipagemOuvidoriaItem");
			obj.setNovoObj(new Boolean(false));
			setTipagemOuvidoriaVO(obj);
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("tipagemOuvidoriaForm");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>TitulacaoCurso</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public void persistir() {
		try {
			getFacadeFactory().getTipagemOuvidoriaFacade().persistir(getTipagemOuvidoriaVO(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * TitulacaoCursoCons.jsp. Define o tipo de consulta a ser executada, por
	 * meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
	 * Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 */
	public String consultar() {
		try {
			setListaConsulta(getFacadeFactory().getTipagemOuvidoriaFacade().consultar(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		} 
		return "";
	}

	public boolean getApresentarResultadoConsulta() {
		if (getListaConsulta() == null || getListaConsulta().isEmpty()) {
			return false;
		}
		return true;
	}

	public void limparConsultaRichModal() {
		getListaConsulta().clear();
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>TitulacaoCursoVO</code> Após a exclusão ela automaticamente aciona
	 * a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getTipagemOuvidoriaFacade().excluir(getTipagemOuvidoriaVO(), getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			return "editar";
		}
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		getListaConsulta().clear();
		setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
		return Uteis.getCaminhoRedirecionamentoNavegacao("tipagemOuvidoriaCons");
	}

	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do
	 * backing bean. Garantindo uma melhor atuação do Garbage Coletor do Java. A
	 * mesma é automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		tipagemOuvidoriaVO = null;
	}

	public TipagemOuvidoriaVO getTipagemOuvidoriaVO() {
		if (tipagemOuvidoriaVO == null) {
			tipagemOuvidoriaVO = new TipagemOuvidoriaVO();
		}
		return tipagemOuvidoriaVO;
	}

	public void setTipagemOuvidoriaVO(TipagemOuvidoriaVO tipagemOuvidoriaVO) {
		this.tipagemOuvidoriaVO = tipagemOuvidoriaVO;
	}
	
	public void limparValorConsulta() {
		getControleConsulta().setValorConsulta("");
	}
}
