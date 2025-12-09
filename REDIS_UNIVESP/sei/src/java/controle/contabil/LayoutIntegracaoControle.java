package controle.contabil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.contabil.LayoutIntegracaoTagVO;
import negocio.comuns.contabil.LayoutIntegracaoVO;
import negocio.comuns.contabil.enumeradores.TipoCampoTagEnum;
import negocio.comuns.contabil.enumeradores.TipoLayoutIntegracaoEnum;
import negocio.comuns.contabil.enumeradores.TipoLayoutPlanoContaEnum;
import negocio.comuns.contabil.enumeradores.TipoTagEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;

/**
 * 
 * @author PedroOtimize
 *
 */
@Controller("LayoutIntegracaoControle")
@Scope("viewScope")
@Lazy
public class LayoutIntegracaoControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5147988907779503174L;
	private static final String TELA_FORM = "layoutIntegracaoForm.xhtml";
	private static final String TELA_CONS = "layoutIntegracaoCons.xhtml";
	private static final String CONTEXT_PARA_EDICAO = "layoutIntegracaoXMLItens";	                                                   
	private static final String CONTEXT_LAYOUTINTEGRACAOXMLTAGITENS = "layoutIntegracaoXmlTagItens";
	                                                                   
	
	private LayoutIntegracaoVO layoutIntegracaoVO;
	private LayoutIntegracaoTagVO layoutIntegracaoTagVO;
	private List<LayoutIntegracaoTagVO> listaLayoutIntegracaoTagMae;
	private String previewXml;
	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> tipoConsultaCombo;
	public List<SelectItem> tipoLayoutIntegracaoCombo;
	public List<SelectItem> tipoLayoutPlanoContaCombo;
	public List<SelectItem> tipoTagEnumCombo;

	/*
	 * private String valorConsultarTagMae; private String campoConsultarTagMae; private List<LayoutIntegracaoXMLTagVO> listaConsultarTagMae;
	 */

	public LayoutIntegracaoControle() {
		setControleConsulta(new ControleConsulta());
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		setMensagemID(MSG_TELA.msg_entre_dados.name());

	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>PlanoConta</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setLayoutIntegracaoVO(new LayoutIntegracaoVO());
		setLayoutIntegracaoTagVO(new LayoutIntegracaoTagVO());
		getLayoutIntegracaoTagVO().setTipoCampoTagEnum(TipoCampoTagEnum.STRING);
		setMensagemID(MSG_TELA.msg_entre_dados.name());		
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>PlanoConta</code> para alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			LayoutIntegracaoVO obj = (LayoutIntegracaoVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			setLayoutIntegracaoVO(getFacadeFactory().getLayoutIntegracaoFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setPreviewXml(getLayoutIntegracaoVO().gerarApresentacaoLayout());
			setLayoutIntegracaoTagVO(new LayoutIntegracaoTagVO());
			if (getLayoutIntegracaoVO().getTipoLayoutIntegracao().isXml()) {
				getLayoutIntegracaoTagVO().setTipoTagEnum(TipoTagEnum.TAG_ROOT);
			} else if (getLayoutIntegracaoVO().getTipoLayoutIntegracao().isTxt()) {
				getLayoutIntegracaoTagVO().setTipoTagEnum(TipoTagEnum.CAMPO);
			}
			getLayoutIntegracaoTagVO().setTipoCampoTagEnum(TipoCampoTagEnum.STRING);
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>PlanoConta</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public void persistir() {
		try {
			getFacadeFactory().getLayoutIntegracaoFacade().persistir(getLayoutIntegracaoVO(), true, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP PlanoContaCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	public void consultarDados() {
		try {
			super.consultar();
			List<LayoutIntegracaoVO> objs = new ArrayList<>();
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getLayoutIntegracaoFacade().consultaRapidaPorCodigo(valorInt, true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("descricao")) {
				objs = getFacadeFactory().getLayoutIntegracaoFacade().consultaRapidaPorDescricao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>PlanoContaVO</code> Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getLayoutIntegracaoFacade().excluir(getLayoutIntegracaoVO(), true, getUsuarioLogado());
			setLayoutIntegracaoVO(new LayoutIntegracaoVO());
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void subirLayoutIntegracaoXMLTag() {
		try {
			LayoutIntegracaoTagVO obj = (LayoutIntegracaoTagVO) context().getExternalContext().getRequestMap().get(CONTEXT_LAYOUTINTEGRACAOXMLTAGITENS);
			getFacadeFactory().getLayoutIntegracaoFacade().alterarPosicaoLayoutIntegracaoTag(getLayoutIntegracaoVO(), obj, true, getUsuarioLogado());
			setPreviewXml(getLayoutIntegracaoVO().gerarApresentacaoLayout());
			limparMensagem();

		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void descerLayoutIntegracaoXMLTag() {
		try {
			LayoutIntegracaoTagVO obj = (LayoutIntegracaoTagVO) context().getExternalContext().getRequestMap().get(CONTEXT_LAYOUTINTEGRACAOXMLTAGITENS);
			getFacadeFactory().getLayoutIntegracaoFacade().alterarPosicaoLayoutIntegracaoTag(getLayoutIntegracaoVO(), obj, false, getUsuarioLogado());
			setPreviewXml(getLayoutIntegracaoVO().gerarApresentacaoLayout());
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void addLayoutIntegracaoXMLTag() {
		try {
			getFacadeFactory().getLayoutIntegracaoFacade().addLayoutIntegracaoTag(getLayoutIntegracaoVO(), getLayoutIntegracaoTagVO(), getUsuarioLogado());
			setPreviewXml(getLayoutIntegracaoVO().gerarApresentacaoLayout());
			setLayoutIntegracaoTagVO(new LayoutIntegracaoTagVO());
			getLayoutIntegracaoTagVO().setTipoTagEnum(TipoTagEnum.TAG_ROOT);
			getLayoutIntegracaoTagVO().setTipoCampoTagEnum(TipoCampoTagEnum.STRING);
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void editarLayoutIntegracaoXMLTag() {
		try {
			LayoutIntegracaoTagVO obj = (LayoutIntegracaoTagVO) context().getExternalContext().getRequestMap().get(CONTEXT_LAYOUTINTEGRACAOXMLTAGITENS);
			setLayoutIntegracaoTagVO(obj);
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void removeLayoutIntegracaoXMLTag() {
		try {
			LayoutIntegracaoTagVO obj = (LayoutIntegracaoTagVO) context().getExternalContext().getRequestMap().get(CONTEXT_LAYOUTINTEGRACAOXMLTAGITENS);
			getFacadeFactory().getLayoutIntegracaoFacade().removeLayoutIntegracaoTag(getLayoutIntegracaoVO(), obj, getUsuarioLogado());

			setPreviewXml(getLayoutIntegracaoVO().gerarApresentacaoLayout());
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}

	}

	public void limparCampoPorTipoLayouIntegracao() {
		if (getLayoutIntegracaoVO().getTipoLayoutIntegracao().isXml()) {
			getLayoutIntegracaoTagVO().setTipoTagEnum(TipoTagEnum.TAG_ROOT);
			getLayoutIntegracaoVO().setDelimitadorTxt("");
		} else if (getLayoutIntegracaoVO().getTipoLayoutIntegracao().isTxt()) {
			getLayoutIntegracaoTagVO().setTipoTagEnum(TipoTagEnum.CAMPO);
		}
		getLayoutIntegracaoVO().getListaLayoutIntegracaoTag().clear();
		tipoTagEnumCombo = null;
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

	public List<SelectItem> getTipoConsultaCombo() {
		if (tipoConsultaCombo == null) {
			tipoConsultaCombo = new ArrayList<>(0);
			tipoConsultaCombo.add(new SelectItem("descricao", "Descrição"));
			tipoConsultaCombo.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaCombo;
	}

	public List<SelectItem> getTipoLayoutIntegracaoCombo() {
		if (tipoLayoutIntegracaoCombo == null) {
			tipoLayoutIntegracaoCombo = new ArrayList<>(0);
			tipoLayoutIntegracaoCombo.add(new SelectItem(TipoLayoutIntegracaoEnum.XML, UteisJSF.internacionalizar("enum_TipoLayoutIntegracaoEnum_" + TipoLayoutIntegracaoEnum.XML.name())));
			tipoLayoutIntegracaoCombo.add(new SelectItem(TipoLayoutIntegracaoEnum.TXT, UteisJSF.internacionalizar("enum_TipoLayoutIntegracaoEnum_" + TipoLayoutIntegracaoEnum.TXT.name())));
		}
		return tipoLayoutIntegracaoCombo;
	}
	
	public List<SelectItem> getTipoLayoutPlanoContaCombo() {
		if (tipoLayoutPlanoContaCombo == null) {
			tipoLayoutPlanoContaCombo = new ArrayList<SelectItem>(0);
			tipoLayoutPlanoContaCombo.add(new SelectItem(TipoLayoutPlanoContaEnum.NENHUM, UteisJSF.internacionalizar("enum_TipoLayoutPlanoContaEnum_" + TipoLayoutPlanoContaEnum.NENHUM.name())));
			tipoLayoutPlanoContaCombo.add(new SelectItem(TipoLayoutPlanoContaEnum.CREDITO, UteisJSF.internacionalizar("enum_TipoLayoutPlanoContaEnum_" + TipoLayoutPlanoContaEnum.CREDITO.name())));
			tipoLayoutPlanoContaCombo.add(new SelectItem(TipoLayoutPlanoContaEnum.DEBITO, UteisJSF.internacionalizar("enum_TipoLayoutPlanoContaEnum_" + TipoLayoutPlanoContaEnum.DEBITO.name())));
		}
		return tipoLayoutPlanoContaCombo;
	}

	public void carregarListaLayoutIntegracaoXMLTagMae() {
		try {
			getListaLayoutIntegracaoTagMae().clear();
			for (LayoutIntegracaoTagVO objExistente : getLayoutIntegracaoVO().getListaLayoutIntegracaoTag()) {
				if ((objExistente.getTipoTagEnum().isTagRoot() || objExistente.getTipoTagEnum().isTagList() || objExistente.getTipoTagEnum().isTag()) && (!Uteis.isAtributoPreenchido(getLayoutIntegracaoTagVO().getNivel()) || Uteis.isAtributoPreenchido(getLayoutIntegracaoTagVO().getNivel()) && objExistente.getNumeroNivel() < getLayoutIntegracaoTagVO().getNumeroNivel())) {
					getListaLayoutIntegracaoTagMae().add(objExistente);
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarLayoutIntegracaoXMLTag() {
		try {
			LayoutIntegracaoTagVO obj = (LayoutIntegracaoTagVO) context().getExternalContext().getRequestMap().get(CONTEXT_LAYOUTINTEGRACAOXMLTAGITENS);
			if (obj.getTag().equals(getLayoutIntegracaoTagVO().getTag())) {
				throw new StreamSeiException("Não é possível selecionar a mesma tag para ser sua mãe");
			}
			if (Uteis.isAtributoPreenchido(obj.getTagMae().getTag()) && obj.getTagMae().getTag().equals(getLayoutIntegracaoTagVO().getTag())) {
				throw new StreamSeiException("A tag selecionar " + obj.getTag() + " já é filha da tag " + getLayoutIntegracaoTagVO().getTag() + " por isso não pode ser selecionada");
			}
			getLayoutIntegracaoTagVO().setTagMae(obj);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparLayoutIntegracaoXMLTag() {
		try {
			getLayoutIntegracaoTagVO().setTagMae(new LayoutIntegracaoTagVO());
			getLayoutIntegracaoTagVO().setNivel("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		getListaConsulta().clear();
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}

	public List getTipoTagEnumCombo() {
		if (tipoTagEnumCombo == null) {
			tipoTagEnumCombo = new ArrayList<>(0);
			tipoTagEnumCombo.add(new SelectItem(TipoTagEnum.TAG_ROOT, TipoTagEnum.TAG_ROOT.getDescricao()));
			tipoTagEnumCombo.add(new SelectItem(TipoTagEnum.TAG_LIST, TipoTagEnum.TAG_LIST.getDescricao()));
			tipoTagEnumCombo.add(new SelectItem(TipoTagEnum.TAG_FORMULA, TipoTagEnum.TAG_FORMULA.getDescricao()));
			tipoTagEnumCombo.add(new SelectItem(TipoTagEnum.TAG, TipoTagEnum.TAG.getDescricao()));
			tipoTagEnumCombo.add(new SelectItem(TipoTagEnum.CAMPO, TipoTagEnum.CAMPO.getDescricao()));
			tipoTagEnumCombo.add(new SelectItem(TipoTagEnum.FIXO, TipoTagEnum.FIXO.getDescricao()));
		}
		return tipoTagEnumCombo;
	}

	public List getListaSelectItemTipoCampoTagEnum() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoCampoTagEnum.class);
	}

	public LayoutIntegracaoVO getLayoutIntegracaoVO() {
		if (layoutIntegracaoVO == null) {
			layoutIntegracaoVO = new LayoutIntegracaoVO();
		}
		return layoutIntegracaoVO;
	}

	public void setLayoutIntegracaoVO(LayoutIntegracaoVO layoutIntegracaoXMLVO) {
		this.layoutIntegracaoVO = layoutIntegracaoXMLVO;
	}

	public LayoutIntegracaoTagVO getLayoutIntegracaoTagVO() {
		if (layoutIntegracaoTagVO == null) {
			layoutIntegracaoTagVO = new LayoutIntegracaoTagVO();
		}
		return layoutIntegracaoTagVO;
	}

	public void setLayoutIntegracaoTagVO(LayoutIntegracaoTagVO layoutIntegracaoXMLTagVO) {
		this.layoutIntegracaoTagVO = layoutIntegracaoXMLTagVO;
	}

	public String getPreviewXml() {
		return previewXml;
	}

	public void setPreviewXml(String previewXml) {
		this.previewXml = previewXml;
	}

	public List<LayoutIntegracaoTagVO> getListaLayoutIntegracaoTagMae() {
		if (listaLayoutIntegracaoTagMae == null) {
			listaLayoutIntegracaoTagMae = new ArrayList<>();
		}
		return listaLayoutIntegracaoTagMae;
	}

	public void setListaLayoutIntegracaoTagMae(List<LayoutIntegracaoTagVO> listaLayoutIntegracaoXMLTagMae) {
		this.listaLayoutIntegracaoTagMae = listaLayoutIntegracaoXMLTagMae;
	}

}
