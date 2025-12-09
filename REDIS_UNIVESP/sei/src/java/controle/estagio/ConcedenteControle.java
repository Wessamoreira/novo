package controle.estagio;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.enumeradores.AtivoInativoEnum;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.estagio.ConcedenteVO;
import negocio.comuns.estagio.TipoConcedenteVO;
import negocio.comuns.utilitarias.Uteis;

@Controller("ConcedenteControle")
@Scope("viewScope")
@Lazy
public class ConcedenteControle extends SuperControle {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4827351082602899936L;
	private static final String TELA_FORM = "concedenteForm.xhtml";
	private static final String TELA_CONS = "concedenteCons.xhtml";
	private static final String CONTEXT_PARA_EDICAO = "concedenteItens";
	private ConcedenteVO concedenteVO;
	private List<SelectItem>listaSelectItemTipoConcedente;
	protected String campoConsultaCidade;
	protected String valorConsultaCidade;
	protected List<CidadeVO> listaConsultaCidade;
	private List<SelectItem> listaSelectItemSituacaoConcedente;

	
	public ConcedenteControle() {
		inicializarListasSelectItemTodosComboBox();
	}
	
	public String novo() {
		removerObjetoMemoria(this);
		inicializarListasSelectItemTodosComboBox();
		setListaSelectItemSituacaoConcedente(Uteis.montarComboboxSemOpcaoDeNenhum(AtivoInativoEnum.values(), Obrigatorio.SIM));
		setConcedenteVO(new ConcedenteVO());
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public String editar() {
		try {
			ConcedenteVO obj = (ConcedenteVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			setConcedenteVO(getFacadeFactory().getConcedenteFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			inicializarListasSelectItemTodosComboBox();
			setListaSelectItemSituacaoConcedente(Uteis.montarComboboxSemOpcaoDeNenhum(AtivoInativoEnum.values(), Obrigatorio.SIM));
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void persistir() {
		try {
			getFacadeFactory().getConcedenteFacade().persistir(getConcedenteVO(), true, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getConcedenteFacade().excluir(getConcedenteVO(), true, getUsuarioLogado());
			novo();
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public String consultar() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getConcedenteFacade().consultar(getControleConsultaOtimizado(), getConcedenteVO());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return "";
	}
	
	public void scrollerListener(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultar();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		inicializarListasSelectItemTodosComboBox();
		setControleConsultaOtimizado(new DataModelo());
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}
	
	public void realizarConsultaEndereco() {
		try {
			getFacadeFactory().getEnderecoFacade().carregarEnderecoEstagio(getConcedenteVO(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarCidade() {
		try {
			getListaConsultaCidade().clear();
			Uteis.checkState(!Uteis.isAtributoPreenchido(getValorConsultaCidade()), getMensagemInternalizacao("msg_ParametroConsulta_informeUmParametro"));
			if (getCampoConsultaCidade().equals("codigo")) {
				if (getValorConsultaCidade().equals("")) {
					setValorConsultaCidade("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCidade());
				getListaConsultaCidade().addAll(getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado()));
			}
			if (getCampoConsultaCidade().equals("nome")) {
				Uteis.checkState(getValorConsultaCidade().length() < 2, getMensagemInternalizacao("msg_Autor_valorConsultaVazio"));
				getListaConsultaCidade().addAll(getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaCidade(), false, getUsuarioLogado()));
			}			
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getListaConsultaCidade().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Método responsável por selecionar o objeto CidadeVO <code>Cidade/code>.
	 */

	public void selecionarCidade() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItens");
		getConcedenteVO().setCidade(obj.getNome());
		getListaConsultaCidade().clear();
		this.setValorConsultaCidade("");
		this.setCampoConsultaCidade("");
	}
	
	public void inicializarConsultarCidade() {
		try {
			getListaConsultaCidade().clear();
			setValorConsultaCidade("");
			setCampoConsultaCidade("nome");
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void limparConsultaCidade() {
		getConcedenteVO().setCidade("");
	}
	
	public boolean isCampoConsultaCidadeSomenteNumero(){
		return getCampoConsultaCidade().equals("codigo") || getCampoConsultaCidade().equals("CODIGO") ? true: false;
	}
	
	public String getMontarScriptParaCampoConsultaCidadeSomenteNumero(){
		return isCampoConsultaCidadeSomenteNumero() ? "return somenteNumero1(event);":"";
	}
	
	private void inicializarListasSelectItemTodosComboBox() {
		setListaSelectItemSituacaoConcedente(null);
		montarListaSelectItemTipoConcedente();
	}
	
	@SuppressWarnings("unchecked")
	public void montarListaSelectItemTipoConcedente() {
		try {
			DataModelo dataModelo = (new DataModelo(false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			dataModelo.setLimitePorPagina(0);
			getFacadeFactory().getTipoConcedenteFacade().consultar(dataModelo, new TipoConcedenteVO());
			getListaSelectItemTipoConcedente().clear();
			getListaSelectItemTipoConcedente().add(new SelectItem(0, ""));
			((List<TipoConcedenteVO>)dataModelo.getListaConsulta()).stream().forEach(p -> getListaSelectItemTipoConcedente().add(new SelectItem(p.getCodigo(), p.getNome())));
		} catch (Exception e) {
			getListaSelectItemTipoConcedente().clear();
		}
	}
	
	public void selecionarTipoConcedente() {
		try {
			if(Uteis.isAtributoPreenchido(getConcedenteVO().getTipoConcedenteVO())) {
				getConcedenteVO().setTipoConcedenteVO(getFacadeFactory().getTipoConcedenteFacade().consultarPorChavePrimaria(getConcedenteVO().getTipoConcedenteVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogadoClone()));	
			}else {
				getConcedenteVO().setTipoConcedenteVO(new TipoConcedenteVO());
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public ConcedenteVO getConcedenteVO() {
		if (concedenteVO == null) {
			concedenteVO = new ConcedenteVO();
		}
		return concedenteVO;
	}

	public void setConcedenteVO(ConcedenteVO concedenteVO) {
		this.concedenteVO = concedenteVO;
	}

	public List<SelectItem> getListaSelectItemTipoConcedente() {
		if (listaSelectItemTipoConcedente == null) {
			listaSelectItemTipoConcedente = new ArrayList<>();
		}
		return listaSelectItemTipoConcedente;
	}

	public void setListaSelectItemTipoConcedente(List<SelectItem> listaSelectItemTipoConcedente) {
		this.listaSelectItemTipoConcedente = listaSelectItemTipoConcedente;
	}
	
	
	public String getCampoConsultaCidade() {
		if(campoConsultaCidade == null) {
			campoConsultaCidade = "";
		}
		return campoConsultaCidade;
	}
	
	public void setCampoConsultaCidade(String campoConsultaCidade) {
		this.campoConsultaCidade = campoConsultaCidade;
	}
	
	public String getValorConsultaCidade() {
		return valorConsultaCidade;
	}


	public void setValorConsultaCidade(String valorConsultaCidade) {
		this.valorConsultaCidade = valorConsultaCidade;
	}

	
	public List<CidadeVO> getListaConsultaCidade() {
		if(listaConsultaCidade == null) {
			listaConsultaCidade = new ArrayList<>();
		}
		return listaConsultaCidade;
	}

	public void setListaConsultaCidade(List<CidadeVO> listaConsultaCidade) {
		this.listaConsultaCidade = listaConsultaCidade;
	}
	
	
	
	public List<SelectItem> getListaSelectItemSituacaoConcedente() {
		if (listaSelectItemSituacaoConcedente == null) {
			listaSelectItemSituacaoConcedente = Uteis.montarComboboxSemOpcaoDeNenhum(AtivoInativoEnum.values(), Obrigatorio.NAO);
		}
		return listaSelectItemSituacaoConcedente;
	}

	public void setListaSelectItemSituacaoConcedente(List<SelectItem> listaSelectItemSituacaoConcedente) {
		this.listaSelectItemSituacaoConcedente = listaSelectItemSituacaoConcedente;
	}
}
