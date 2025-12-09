package controle.recursoshumanos;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.CategoriaGEDVO;
import negocio.comuns.faturamento.nfe.ImpostoVO;
import negocio.comuns.recursoshumanos.IncidenciaFolhaPagamentoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas tabelaReferenciaFPForm.xhtml é tabelaReferenciaFPCons.xhtl com as
 * funcionalidades da classe <code>IncidenciaFolhaPagamentoVO</code>.
 * Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see CategoriaGEDVO
 */
@Controller("IncidenciaFolhaPagamentoControle")
@Scope("viewScope")
@Lazy
public class IncidenciaFolhaPagamentoControle extends SuperControle {

	private static final long serialVersionUID = 1455114507405935993L;

	private IncidenciaFolhaPagamentoVO incidenciaFolhaPagamentoVO;
	protected List<SelectItem> listaSelectItemImposto;
	
	 @PostConstruct
	 public void init() {
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }
	 
	public IncidenciaFolhaPagamentoControle() {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsavel por disponibilizar um novo objeto da classe
	 * <code>IncidenciaFolhaPagamentoVO</code> para edicao pelo usuario da aplicacao.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setIncidenciaFolhaPagamentoVO(new IncidenciaFolhaPagamentoVO());
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("incidenciaFolhaPagamentoForm");
	}

	/**
	 * Rotina responsavel por disponibilizar os dados de um objeto da classe
	 * <code>IncidenciaFolhaPagamentoControle</code> para alteracao. O objeto desta classe e
	 * disponibilizado na session da pagina (request) para que o JSP correspondente
	 * possa disponibiliza-lo para edicao.
	 */
	public String editar() {
		IncidenciaFolhaPagamentoVO obj = (IncidenciaFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("incidenciaFP");
		obj.setNovoObj(Boolean.FALSE);
		setIncidenciaFolhaPagamentoVO(obj);
		setMensagemID("msg_dados_editar");
		montarListagemDasCombobox();
		return Uteis.getCaminhoRedirecionamentoNavegacao("incidenciaFolhaPagamentoForm");
	}

	private void montarListagemDasCombobox() {
		getListaSelectItemImposto();
	}

	/**
	 * Rotina responsavel por organizar a paginacao entre as paginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<IncidenciaFolhaPagamentoVO>(0));
		setMensagemID("msg_entre_prmconsulta");
		getControleConsulta().setValorConsulta("");
		return Uteis.getCaminhoRedirecionamentoNavegacao("incidenciaFolhaPagamentoCons");
	}

	/**
	 * Rotina responsavel por gravar no BD os dados editados de um novo objeto da
	 * classe <code>IncidenciaFolhaPagamentoVO</code>. Caso o objeto seja novo (ainda nao gravado
	 * no BD) e acionado a operacao <code>incluir()</code>. Caso contrario e
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistencia o objeto
	 * nao e gravado, sendo re-apresentado para o usuario juntamente com uma
	 * mensagem de erro.
	 */
	public String gravar() {
		try {
			getFacadeFactory().getIncidenciaFolhaPagamentoInterfaceFacade().persistir(incidenciaFolhaPagamentoVO, Boolean.TRUE, getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "";
	}

	/**
	 * Operaçao responsavel por processar a exclusao um objeto da classe
	 * <code>IncidenciaFolhaPagamento</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusao.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getIncidenciaFolhaPagamentoInterfaceFacade().excluir(incidenciaFolhaPagamentoVO, Boolean.TRUE, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("incidenciaFolhaPagamentoForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("incidenciaFolhaPagamentoForm");
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis do
	 * incidenciaFolhaPagamentoCons.xhtml. Define o tipo de consulta a ser executada, por meio
	 * de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List<IncidenciaFolhaPagamentoVO> objs = new ArrayList<IncidenciaFolhaPagamentoVO>(0);

			if(getControleConsulta().getCampoConsulta().equals("codigo")) {
				Uteis.validarSomenteNumeroString(getControleConsulta().getValorConsulta());
			} 
			
			objs = getFacadeFactory().getIncidenciaFolhaPagamentoInterfaceFacade().consultarPorFiltro(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());

			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}
	
	/**
	 * Rotina responsavel por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	/**
	 * Rotina responsavel por realizar a paginacao da pagina de incidenciaFPCons.xhtml
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
	 * Operacao que libera todos os recursos (atributos, listas, objetos) do backing
	 * bean. Garantindo uma melhor atuacao do Garbage Coletor do Java. A mesma e
	 * automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		incidenciaFolhaPagamentoVO = null;
	}

	public List<SelectItem> getListaSelectItemImposto() {
		try {
 			if (listaSelectItemImposto == null) {
				listaSelectItemImposto = new ArrayList<SelectItem>(0);
				List<ImpostoVO> impostos = getFacadeFactory().getImpostoFacade().consultarImpostoComboBox(false, getUsuarioLogado());
				listaSelectItemImposto.add(new SelectItem(0, ""));
				for (ImpostoVO imposto : impostos) {
					listaSelectItemImposto.add(new SelectItem(imposto.getCodigo(), imposto.getNome()));
				}
			}
			return listaSelectItemImposto;
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return new ArrayList<SelectItem>(0);
	}

	public void setListaSelectItemImposto(List<SelectItem> listaSelectItemImposto) {
		this.listaSelectItemImposto = listaSelectItemImposto;
	}

	public IncidenciaFolhaPagamentoVO getIncidenciaFolhaPagamentoVO() {
		if (incidenciaFolhaPagamentoVO == null)
			incidenciaFolhaPagamentoVO = new IncidenciaFolhaPagamentoVO();
		return incidenciaFolhaPagamentoVO;
	}

	public void setIncidenciaFolhaPagamentoVO(IncidenciaFolhaPagamentoVO incidenciaFolhaPagamentoVO) {
		this.incidenciaFolhaPagamentoVO = incidenciaFolhaPagamentoVO;
	}

}