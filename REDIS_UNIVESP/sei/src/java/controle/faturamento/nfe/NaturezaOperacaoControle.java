package controle.faturamento.nfe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.faturamento.nfe.NaturezaOperacaoVO;
import negocio.comuns.faturamento.nfe.enumeradores.TipoNaturezaOperacaoEnum;
import negocio.comuns.faturamento.nfe.enumeradores.TipoOrigemDestinoNaturezaOperacaoEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author PedroOtimize
 *
 */
@Controller("NaturezaOperacaoControle")
@Scope("viewScope")
@Lazy
public class NaturezaOperacaoControle extends SuperControle implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2246164183852678348L;
	private NaturezaOperacaoVO naturezaOperacaoVO;

	public NaturezaOperacaoControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		getControleConsulta().setCampoConsulta("nome");
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>PlanoConta</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setNaturezaOperacaoVO(new NaturezaOperacaoVO());
		getNaturezaOperacaoVO().setTipoNaturezaOperacaoEnum(TipoNaturezaOperacaoEnum.ENTRADA);
		getNaturezaOperacaoVO().setTipoOrigemDestinoNaturezaOperacaoEnum(TipoOrigemDestinoNaturezaOperacaoEnum.MESMO_ESTADO);
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("naturezaOperacaoForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>PlanoConta</code> para alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			NaturezaOperacaoVO obj = (NaturezaOperacaoVO) context().getExternalContext().getRequestMap().get("naturezaOperacaoItens");
			setNaturezaOperacaoVO(getFacadeFactory().getNaturezaOperacaoFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("naturezaOperacaoForm.xhtml");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>PlanoConta</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public void persistir() {
		try {
			getFacadeFactory().getNaturezaOperacaoFacade().persistir(getNaturezaOperacaoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP PlanoContaCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	public void consultarDados() {
		try {
			super.consultar();
			List<NaturezaOperacaoVO> objs = new ArrayList<NaturezaOperacaoVO>();
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getNaturezaOperacaoFacade().consultaRapidaPorCodigo(valorInt, true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}else if (getControleConsulta().getCampoConsulta().equals("codigoNaturezaOperacao")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getNaturezaOperacaoFacade().consultaRapidaPorCodigoNaturezaOperacao(valorInt, true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}else if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getNaturezaOperacaoFacade().consultaRapidaPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}else if (getControleConsulta().getCampoConsulta().equals("tipoNaturezaOperacao")) {
				objs = getFacadeFactory().getNaturezaOperacaoFacade().consultaRapidaPorTipoNaturezaOperacaoEnum(getNaturezaOperacaoVO().getTipoNaturezaOperacaoEnum(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>PlanoContaVO</code> Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getNaturezaOperacaoFacade().excluir(getNaturezaOperacaoVO(), true, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("naturezaOperacaoForm.xhtml");
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
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> tipoConsultaCombo;

	public List<SelectItem> getTipoConsultaCombo() {
		if (tipoConsultaCombo == null) {
			tipoConsultaCombo = new ArrayList<SelectItem>(0);
			tipoConsultaCombo.add(new SelectItem("nome", "Nome"));
			tipoConsultaCombo.add(new SelectItem("codigoNaturezaOperacao", "Código Natureza Operação"));
			tipoConsultaCombo.add(new SelectItem("tipoNaturezaOperacao", "Tipo Natureza Operação"));
			tipoConsultaCombo.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaCombo;
	}
	
	public boolean isCampoConsultaSomenteNumero(){
		return !isCampoConsultaTipoNaturezaOperacao() && (super.isCampoConsultaSomenteNumero()|| getControleConsulta().getCampoConsulta().equals("codigoNaturezaOperacao"));
	}
	
	public boolean isCampoConsultaTexto(){
		return !isCampoConsultaTipoNaturezaOperacao() && !isCampoConsultaSomenteNumero();
	}
	
	public boolean isCampoConsultaTipoNaturezaOperacao(){
		return getControleConsulta().getCampoConsulta().equals("tipoNaturezaOperacao");
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		getListaConsulta().clear();
		getControleConsulta().setCampoConsulta("nome");
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("naturezaOperacaoCons.xhtml");
	}

	public NaturezaOperacaoVO getNaturezaOperacaoVO() {
		if (naturezaOperacaoVO == null) {
			naturezaOperacaoVO = new NaturezaOperacaoVO();
		}
		return naturezaOperacaoVO;
	}

	public void setNaturezaOperacaoVO(NaturezaOperacaoVO naturezaOperacaoVO) {
		this.naturezaOperacaoVO = naturezaOperacaoVO;
	}

}
