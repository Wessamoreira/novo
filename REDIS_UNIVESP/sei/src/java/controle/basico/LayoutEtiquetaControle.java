package controle.basico;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.basico.LayoutEtiquetaTagVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.basico.enumeradores.ModuloLayoutEtiquetaEnum;
import negocio.comuns.basico.enumeradores.TagEtiquetaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import relatorio.controle.arquitetura.SuperControleRelatorio;

/**
 * Classe responsável por implementar a interação entre os componentes JSF da
 * página (layoutEtiquetaCons.jsp) com as funcionalidades da classe
 * <code>LayoutEtiqueta</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see MetaProducaoVO
 */

@Controller("LayoutEtiquetaControle")
@Scope("viewScope")
@Lazy
public class LayoutEtiquetaControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1056429934024337123L;
	private LayoutEtiquetaVO layoutEtiquetaVO;
	private List<SelectItem> listaSelectItemLayoutEtiquetaTag;
	private TagEtiquetaEnum tagEtiqueta;
	private ModuloLayoutEtiquetaEnum moduloLayoutEtiquetaEnum;

	public LayoutEtiquetaControle() {
		setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
		consultar();
	}

	public LayoutEtiquetaVO getLayoutEtiquetaVO() {
		if (layoutEtiquetaVO == null) {
			layoutEtiquetaVO = new LayoutEtiquetaVO();
		}
		return layoutEtiquetaVO;
	}

	public void setLayoutEtiquetaVO(LayoutEtiquetaVO layoutEtiquetaVO) {
		this.layoutEtiquetaVO = layoutEtiquetaVO;
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Agenda</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		setLayoutEtiquetaVO(new LayoutEtiquetaVO());
		montarListaSelectItemLayoutEtiquetaTag();
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
		return Uteis.getCaminhoRedirecionamentoNavegacao("layoutEtiquetaForm");
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * UnidadeMedidaCons.jsp. Define o tipo de consulta a ser executada, por
	 * meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
	 * Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 * 
	 * @throws Exception
	 */
	public String consultar() {
		try {
			getListaConsulta().clear();
			setListaConsulta(getFacadeFactory().getLayoutEtiquetaFacade().consultar(true, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("layoutEtiquetaCons");
	}

	public String inicializarConsultar() {
		consultar();
		setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
		return Uteis.getCaminhoRedirecionamentoNavegacao("layoutEtiquetaCons");
	}

	public String editar() {
		try {
			getListaConsulta().clear();
			LayoutEtiquetaVO obj = (LayoutEtiquetaVO) context().getExternalContext().getRequestMap().get("layoutEtiquetaLista");
			obj = getFacadeFactory().getLayoutEtiquetaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			obj.setNovoObj(new Boolean(false));
			setLayoutEtiquetaVO(obj);
			montarListaSelectItemLayoutEtiquetaTag();
			setMensagemID("msg_dados_editar", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("layoutEtiquetaForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("layoutEtiquetaCons");
		}
	}

	public void adicionarTagEtiqueta() {
		try {
			getFacadeFactory().getLayoutEtiquetaFacade().adicionarObjLayoutEtiquetaTagVOs(getLayoutEtiquetaVO(), getTagEtiqueta());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemID("msg_erro", Uteis.ERRO);
		}
	}

	public void removerTagEtiqueta() {
		try {
			LayoutEtiquetaTagVO layoutEtiquetaTagVO = (LayoutEtiquetaTagVO) context().getExternalContext().getRequestMap().get("obj");
			getFacadeFactory().getLayoutEtiquetaFacade().excluirObjLayoutEtiquetaTagVOs(getLayoutEtiquetaVO(), layoutEtiquetaTagVO);
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemID("msg_erro",  Uteis.ERRO);
		}
	}

	public Boolean getValidarLista() {
		return !getLayoutEtiquetaVO().getLayoutEtiquetaTagVOs().isEmpty();
	}

	public void persistir() {
		try {
			setListaConsulta(new ArrayList<LayoutEtiquetaVO>(0));
			getFacadeFactory().getLayoutEtiquetaFacade().persistir(getLayoutEtiquetaVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getLayoutEtiquetaFacade().excluir(getLayoutEtiquetaVO(), getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("layoutEtiquetaForm");
	}
	
	public void clonarLayoutRelatorio(){
		getLayoutEtiquetaVO().setNovoObj(true);
		getLayoutEtiquetaVO().setCodigo(0);
		getLayoutEtiquetaVO().setDescricao(getLayoutEtiquetaVO().getDescricao() + " - Clone");
		for(LayoutEtiquetaTagVO tag : getLayoutEtiquetaVO().getLayoutEtiquetaTagVO()){
			tag.setCodigo(0);
			tag.setNovoObj(true);
			tag.getLayoutEtiquetaVO().setCodigo(0);
			tag.getLayoutEtiquetaVO().setNovoObj(true);
		}
		setMensagemID("msg_dados_clonados", Uteis.SUCESSO);
	}

	public void realizarMontagemPreviewEtiqueta() {
		try {
			getFacadeFactory().getLayoutEtiquetaFacade().realizarMontagemPreviewEtiqueta(getLayoutEtiquetaVO(), getConfiguracaoGeralPadraoSistema());
			context().getExternalContext().getSessionMap().put("nomeArquivo", getLayoutEtiquetaVO().getModuloLayoutEtiqueta().name() + ".pdf");
			context().getExternalContext().getSessionMap().put("nomeReal", getLayoutEtiquetaVO().getModuloLayoutEtiqueta().name() + ".pdf");
			context().getExternalContext().getSessionMap().put("pastaBaseArquivo", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + PastaBaseArquivoEnum.LAYOUT_ETIQUETA);
			setCaminhoRelatorio(getLayoutEtiquetaVO().getModuloLayoutEtiqueta().name() +".pdf");
			setFazerDownload(true);
			
			limparMensagem();
			setMensagemID("msg_relatorio_ok", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarModuloEtiqueta() {
		montarListaSelectItemLayoutEtiquetaTag();
		q:
		for (Iterator<LayoutEtiquetaTagVO> iterator = getLayoutEtiquetaVO().getLayoutEtiquetaTagVOs().iterator(); iterator.hasNext();) {
			LayoutEtiquetaTagVO layoutEtiquetaTagVO = (LayoutEtiquetaTagVO) iterator.next();			
			for(SelectItem item: getListaSelectItemLayoutEtiquetaTag()){
				if(item.getValue().equals(layoutEtiquetaTagVO.getTagEtiqueta())){			
					continue q;
				}
			}
			iterator.remove();
		}
	}

	public void montarListaSelectItemLayoutEtiquetaTag() {
		setListaSelectItemLayoutEtiquetaTag(TagEtiquetaEnum.getTagEtiquetaPorModulo(getLayoutEtiquetaVO().getModuloLayoutEtiqueta()));
	}

	public List<SelectItem> getListaSelectItemModuloLayoutEtiqueta() throws Exception {
		return ModuloLayoutEtiquetaEnum.getModuloLayoutEtiqueta();
	}

	public List<SelectItem> getListaSelectItemLayoutEtiquetaTag() {
		if (listaSelectItemLayoutEtiquetaTag == null) {
			listaSelectItemLayoutEtiquetaTag = new ArrayList<SelectItem>();
		}
		return listaSelectItemLayoutEtiquetaTag;
	}

	public void setListaSelectItemLayoutEtiquetaTag(List<SelectItem> listaSelectItemLayoutEtiquetaTag) {
		this.listaSelectItemLayoutEtiquetaTag = listaSelectItemLayoutEtiquetaTag;
	}

	public TagEtiquetaEnum getTagEtiqueta() {
		return tagEtiqueta;
	}

	
	public void setTagEtiqueta(TagEtiquetaEnum tagEtiqueta) {
		this.tagEtiqueta = tagEtiqueta;
	}

	public ModuloLayoutEtiquetaEnum getModuloLayoutEtiquetaEnum() {
		if (moduloLayoutEtiquetaEnum == null) {
			moduloLayoutEtiquetaEnum = ModuloLayoutEtiquetaEnum.INSCRICAO_SELETIVO;
		}
		return moduloLayoutEtiquetaEnum;
	}

	public void setModuloLayoutEtiquetaEnum(ModuloLayoutEtiquetaEnum moduloLayoutEtiquetaEnum) {
		this.moduloLayoutEtiquetaEnum = moduloLayoutEtiquetaEnum;
	}

	public boolean getApresentarTags() {
		return getLayoutEtiquetaVO().getModuloLayoutEtiqueta().name().equals(ModuloLayoutEtiquetaEnum.INSCRICAO_SELETIVO) || getLayoutEtiquetaVO().getModuloLayoutEtiqueta().name().equals(ModuloLayoutEtiquetaEnum.MATRICULA);
	}

	public void realizarSubirOrdemLayoutEtiquetaTag() {
		getFacadeFactory().getLayoutEtiquetaFacade().realizarAlteracaoOrdemLayoutEtiquetaTag(getLayoutEtiquetaVO(), (LayoutEtiquetaTagVO) context().getExternalContext().getRequestMap().get("obj"), true);
	}

	public void realizarDescerOrdemLayoutEtiquetaTag() {
		getFacadeFactory().getLayoutEtiquetaFacade().realizarAlteracaoOrdemLayoutEtiquetaTag(getLayoutEtiquetaVO(), (LayoutEtiquetaTagVO) context().getExternalContext().getRequestMap().get("obj"), false);
	}

	public Integer getTamanhoListaTag() {
		return getLayoutEtiquetaVO().getLayoutEtiquetaTagVOs().size();
	}

	public void uploadImagem(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getLayoutEtiquetaFacade().upLoadArquivo(uploadEvent, getLayoutEtiquetaVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public boolean getIsApresentarCampoImagemFundo() {
		if (getTagEtiqueta() == null) {
			return false;
		}
		return getTagEtiqueta().equals(TagEtiquetaEnum.IMAGEM_FUNDO);
	}
	
	public void realizarExportacaoLayout() {
		try {
			setFazerDownload(false);			
			this.setCaminhoRelatorio(getFacadeFactory().getLayoutEtiquetaFacade().realizarExportacaoLayout(getLayoutEtiquetaVO()));
			setFazerDownload(true);
			limparMensagem();
			setMensagemID("msg_LayoutEtiqueta_dadosExportados",  Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	
	
	public void changeAlterarModuloEtiqueta(ValueChangeEvent event) {
	       alterarModuloEtiqueta();
	}
	
	public void realizarImportacaoLayout(FileUploadEvent event) {
		try {
			setLayoutEtiquetaVO(getFacadeFactory().getLayoutEtiquetaFacade().realizarImportacaoLayout(event));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}


}
