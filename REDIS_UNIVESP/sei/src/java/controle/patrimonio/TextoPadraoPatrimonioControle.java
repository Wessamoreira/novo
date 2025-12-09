package controle.patrimonio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itextpdf.text.PageSize;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.basico.enumeradores.EntidadeTextoPadraoEnum;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.patrimonio.TextoPadraoPatrimonioVO;
import negocio.comuns.patrimonio.enumeradores.TipoUsoTextoPadraoPatrimonioEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisHTML;
import negocio.comuns.utilitarias.UteisTextoPadrao;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.OrientacaoPaginaEnum;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.interfaces.basico.TagTextoPadraoInterfaceEnum;
import relatorio.controle.arquitetura.SuperControleRelatorio;

@Controller("TextoPadraoPatrimonioControle")
@Scope("viewScope")
public class TextoPadraoPatrimonioControle extends SuperControleRelatorio {

	private static final long serialVersionUID = -5891950509211555361L;
	private TextoPadraoPatrimonioVO textoPadraoPatrimonioVO;
	private List<ArquivoVO> arquivoVOs;
	private ArquivoVO arquivoVO;
	private StatusAtivoInativoEnum situacaoCons;
	private List<SelectItem> listaSelectItemEntidadeTag;
	private List<SelectItem> listaSelectItemTipoUso;
	private List<SelectItem> comboboxOrientacaoPaginaEnum;

	private List<Enum<? extends TagTextoPadraoInterfaceEnum>> listaTag;
	private EntidadeTextoPadraoEnum entidadeTextoPadrao;

	public void gravar() {
		try {
			getFacadeFactory().getTextoPadraoPatrimonioFacade().persistir(getTextoPadraoPatrimonioVO(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluir() {
		try {
			getFacadeFactory().getTextoPadraoPatrimonioFacade().excluir(getTextoPadraoPatrimonioVO(), getUsuarioLogado());
			setTextoPadraoPatrimonioVO(new TextoPadraoPatrimonioVO());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String novo() {
		try {
			setTextoPadraoPatrimonioVO(new TextoPadraoPatrimonioVO());
			realizarFormatacaoLayoutPagina();
			if (getArquivoVOs().isEmpty()) {
				montarListaArquivosImagem();
			}
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		getTextoPadraoPatrimonioVO().getTextoPadrao();
		return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoPatrimonioForm");
	}

	public void realizarClonagem() {
		try {
			getFacadeFactory().getTextoPadraoPatrimonioFacade().realizarClonagem(getTextoPadraoPatrimonioVO());
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}

	public String editar() {
		try {
			setTextoPadraoPatrimonioVO((TextoPadraoPatrimonioVO) getRequestMap().get("textoPadraoPatrimonioItem"));
			montarListaArquivosImagem();
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoPatrimonioForm");
	}

	public String consultar() {
		try {
			getControleConsultaOtimizado().getListaConsulta().clear();
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getTextoPadraoPatrimonioFacade().consultar(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, getUsuarioLogado()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getTextoPadraoPatrimonioFacade().consultarTotal(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), true, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoPatrimonioCons");
	}

	public TextoPadraoPatrimonioVO getTextoPadraoPatrimonioVO() {
		if (textoPadraoPatrimonioVO == null) {
			textoPadraoPatrimonioVO = new TextoPadraoPatrimonioVO();
		}
		return textoPadraoPatrimonioVO;
	}

	public void setTextoPadraoPatrimonioVO(TextoPadraoPatrimonioVO textoPadraoPatrimonioVO) {
		this.textoPadraoPatrimonioVO = textoPadraoPatrimonioVO;
	}

	public void montarListaArquivosImagem() {
		try {
			setArquivoVOs(getFacadeFactory().getArquivoFacade().consultarArquivosPorPastaBaseArquivo("imagemTextoPadrao"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.IMAGEM_TEXTOPADRAO, getUsuarioLogado());
			if (!getArquivoVO().getNome().equals("")) {
				try {
					getFacadeFactory().getArquivoFacade().incluir(getArquivoVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
					getArquivoVOs().add(getArquivoVO());
				} catch (Exception e) {
					setMensagemDetalhada("msg_erro", e.getMessage());
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public List<ArquivoVO> getArquivoVOs() {
		if (arquivoVOs == null) {
			arquivoVOs = new ArrayList<ArquivoVO>(0);
		}
		return arquivoVOs;
	}

	public void setArquivoVOs(List<ArquivoVO> arquivoVOs) {
		this.arquivoVOs = arquivoVOs;
	}

	public void selecionarTag() {
		getTextoPadraoPatrimonioVO().setTextoPadrao(UteisTextoPadrao.adicionarTag(getTextoPadraoPatrimonioVO().getTextoPadrao(), ((TagTextoPadraoInterfaceEnum) getRequestMap().get("tag"))));
		getTextoPadraoPatrimonioVO().getTextoPadrao();
	}

	public ArquivoVO getArquivoVO() {
		if (arquivoVO == null) {
			arquivoVO = new ArquivoVO();
		}
		return arquivoVO;
	}

	public void setArquivoVO(ArquivoVO arquivoVO) {
		this.arquivoVO = arquivoVO;
	}

	public void realizarCriacaoNovaPaginaTexto() {
		try {
			getTextoPadraoPatrimonioVO().setTextoPadrao(UteisHTML.realizarCriacaoNovaPaginaTexto(getTextoPadraoPatrimonioVO().getTextoPadrao(), getTextoPadraoPatrimonioVO().getOrientacaoDaPagina()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarAlteracaoConfiguracaoPagina() {
		realizarFormatacaoLayoutPagina();
	}

	public void realizarAlteracaoOrientacaoPagina() {
		realizarFormatacaoLayoutPagina();
	}

	public void realizarFormatacaoLayoutPagina() {
		try {
			getTextoPadraoPatrimonioVO().setTextoPadrao(UteisHTML.realizarFormatacaoBody(getTextoPadraoPatrimonioVO().getTextoPadrao(), getTextoPadraoPatrimonioVO().getOrientacaoDaPagina(), PageSize.A4.getHeight(), PageSize.A4.getWidth(), getTextoPadraoPatrimonioVO().getAlturaTopo(), getTextoPadraoPatrimonioVO().getAlturaRodape(), getTextoPadraoPatrimonioVO().getMargemEsquerda(), getTextoPadraoPatrimonioVO().getMargemDireita(), getTextoPadraoPatrimonioVO().getMargemSuperior(), getTextoPadraoPatrimonioVO().getMargemInferior()));
			getTextoPadraoPatrimonioVO().setTextoTopo(UteisHTML.realizarFormatacaoTopo(getTextoPadraoPatrimonioVO().getTextoTopo(), getTextoPadraoPatrimonioVO().getOrientacaoDaPagina(), PageSize.A4.getHeight(), PageSize.A4.getWidth(), getTextoPadraoPatrimonioVO().getAlturaTopo(),getTextoPadraoPatrimonioVO().getMargemEsquerda(), getTextoPadraoPatrimonioVO().getMargemDireita()));
			getTextoPadraoPatrimonioVO().setTextoRodape(UteisHTML.realizarFormatacaoTopo(getTextoPadraoPatrimonioVO().getTextoRodape(), getTextoPadraoPatrimonioVO().getOrientacaoDaPagina(), PageSize.A4.getHeight(), PageSize.A4.getWidth(), getTextoPadraoPatrimonioVO().getAlturaRodape(),getTextoPadraoPatrimonioVO().getMargemEsquerda(), getTextoPadraoPatrimonioVO().getMargemDireita()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarVisualizacaoTextoPadrao() {
		try {
			setFazerDownload(false);
			this.setCaminhoRelatorio("");
			this.setCaminhoRelatorio(getFacadeFactory().getTextoPadraoPatrimonioFacade().realizarImpressaoTextoVisualizacao(getTextoPadraoPatrimonioVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
			setFazerDownload(true);
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			
		}
	}

	public StatusAtivoInativoEnum getSituacaoCons() {
		return situacaoCons;
	}

	public void setSituacaoCons(StatusAtivoInativoEnum situacaoCons) {
		this.situacaoCons = situacaoCons;
	}

	public List<SelectItem> getListaSelectItemEntidadeTag() {
		if (listaSelectItemEntidadeTag == null) {
			listaSelectItemEntidadeTag = new ArrayList<SelectItem>(0);
			listaSelectItemEntidadeTag.add(new SelectItem(EntidadeTextoPadraoEnum.PATRIMONIO, EntidadeTextoPadraoEnum.PATRIMONIO.getValorApresentar()));
			listaSelectItemEntidadeTag.add(new SelectItem(EntidadeTextoPadraoEnum.PATRIMONIO_UNIDADE, EntidadeTextoPadraoEnum.PATRIMONIO_UNIDADE.getValorApresentar()));
			if(getTextoPadraoPatrimonioVO().getTipoUso().equals(TipoUsoTextoPadraoPatrimonioEnum.CADASTRO_PATRIMONIO)){
				listaSelectItemEntidadeTag.add(new SelectItem(EntidadeTextoPadraoEnum.LOCAL_ARMAZENAMENTO, EntidadeTextoPadraoEnum.LOCAL_ARMAZENAMENTO.getValorApresentar()));
			}
			if(!getTextoPadraoPatrimonioVO().getTipoUso().equals(TipoUsoTextoPadraoPatrimonioEnum.CADASTRO_PATRIMONIO)){
				listaSelectItemEntidadeTag.add(new SelectItem(EntidadeTextoPadraoEnum.OCORRENCIA_PATRIMONIO, EntidadeTextoPadraoEnum.OCORRENCIA_PATRIMONIO.getValorApresentar()));
			}
			listaSelectItemEntidadeTag.add(new SelectItem(EntidadeTextoPadraoEnum.TAG_ADICIONAIS, EntidadeTextoPadraoEnum.TAG_ADICIONAIS.getValorApresentar()));
		}
		return listaSelectItemEntidadeTag;
	}
	

	public void inicializarDadosTipoUso(){
		setListaSelectItemEntidadeTag(null);
	}

	public void setListaSelectItemEntidadeTag(List<SelectItem> listaSelectItemEntidadeTag) {
		this.listaSelectItemEntidadeTag = listaSelectItemEntidadeTag;
	}

	public EntidadeTextoPadraoEnum getEntidadeTextoPadrao() {
		if (entidadeTextoPadrao == null) {
			entidadeTextoPadrao = EntidadeTextoPadraoEnum.PATRIMONIO;
			montarListaSelectItemTag();
		}
		return entidadeTextoPadrao;
	}

	public void setEntidadeTextoPadrao(EntidadeTextoPadraoEnum entidadeTextoPadrao) {
		this.entidadeTextoPadrao = entidadeTextoPadrao;
	}

	public List<Enum<? extends TagTextoPadraoInterfaceEnum>> getListaTag() {
		if (listaTag == null) {
			listaTag = new ArrayList<Enum<? extends TagTextoPadraoInterfaceEnum>>(0);
		}
		return listaTag;
	}

	public void setListaTag(List<Enum<? extends TagTextoPadraoInterfaceEnum>> listaTag) {
		this.listaTag = listaTag;
	}

	public void montarListaSelectItemTag() {
		getListaTag().clear();
		for (Enum<? extends TagTextoPadraoInterfaceEnum> tag : getEntidadeTextoPadrao().getTags()) {
			getListaTag().add(tag);
		}
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("descricao", "Descrição"));
		objs.add(new SelectItem("situacao", "Situação"));
		objs.add(new SelectItem("codigo", "Código"));
		return objs;
	}

	public void scrollerListener(DataScrollEvent dataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollEvent.getPage());
		consultar();
	}

	public String inicializarConsulta() {
		removerObjetoMemoria(this);
		getControleConsultaOtimizado().getListaConsulta().clear();
		getControleConsulta().setValorConsulta("");
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoPatrimonioCons");
	}

	public void alterarSituacao() {
		try {
			if (getTextoPadraoPatrimonioVO().getSituacao().equals(StatusAtivoInativoEnum.ATIVO)) {
				getTextoPadraoPatrimonioVO().setSituacao(StatusAtivoInativoEnum.INATIVO);
				getFacadeFactory().getTextoPadraoPatrimonioFacade().alterarSituacaoTextoPadraoPatrimonio(getTextoPadraoPatrimonioVO());
			} else {
				getTextoPadraoPatrimonioVO().setSituacao(StatusAtivoInativoEnum.ATIVO);
				getFacadeFactory().getTextoPadraoPatrimonioFacade().alterarSituacaoTextoPadraoPatrimonio(getTextoPadraoPatrimonioVO());

			}
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public boolean getIsApresentarSituacao() {
		return getTextoPadraoPatrimonioVO().getSituacao().equals(StatusAtivoInativoEnum.ATIVO);
	}
	public boolean getIsApresentarEntidade() {
		return getControleConsulta().getCampoConsulta().equals("situacao");
	}
	
	public List<SelectItem> getListaSelectItemSituacao() {
		if (listaSelectItemEntidadeTag == null) {
			listaSelectItemEntidadeTag = new ArrayList<SelectItem>(0);
			listaSelectItemEntidadeTag.add(new SelectItem(StatusAtivoInativoEnum.ATIVO, StatusAtivoInativoEnum.ATIVO.getValorApresentar()));
			listaSelectItemEntidadeTag.add(new SelectItem(StatusAtivoInativoEnum.INATIVO, StatusAtivoInativoEnum.INATIVO.getValorApresentar()));
		}
		return listaSelectItemEntidadeTag;
	}

	/**
	 * @return the listaSelectItemTipoUso
	 */
	public List<SelectItem> getListaSelectItemTipoUso() {
		if (listaSelectItemTipoUso == null) {
			listaSelectItemTipoUso = new ArrayList<SelectItem>();
			listaSelectItemTipoUso = UtilSelectItem.getListaSelectItemEnum(TipoUsoTextoPadraoPatrimonioEnum.values(), Obrigatorio.SIM);
		}
		return listaSelectItemTipoUso;
	}

	/**
	 * @param listaSelectItemTipoUso the listaSelectItemTipoUso to set
	 */
	public void setListaSelectItemTipoUso(List<SelectItem> listaSelectItemTipoUso) {
		this.listaSelectItemTipoUso = listaSelectItemTipoUso;
	}
	
	public void excluirArquivo() {
		try {
			ArquivoVO arquivoVO = (ArquivoVO)getRequestMap().get("imagem");
			arquivoVO.setDataIndisponibilizacao(new Date());
			arquivoVO.setManterDisponibilizacao(false);
			getFacadeFactory().getArquivoFacade().alterarManterDisponibilizacao(arquivoVO, getUsuarioLogado());
//			getFacadeFactory().getArquivoFacade().excluir(arquivoVO, false, "", getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			montarListaArquivosImagem();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} 
	}
	
	public List<SelectItem> getComboboxOrientacaoPaginaEnum() {
		if (comboboxOrientacaoPaginaEnum == null) {
			comboboxOrientacaoPaginaEnum = new ArrayList<SelectItem>(0);
			comboboxOrientacaoPaginaEnum.add(new SelectItem(OrientacaoPaginaEnum.RETRATO, OrientacaoPaginaEnum.RETRATO.getValue()));
			comboboxOrientacaoPaginaEnum.add(new SelectItem(OrientacaoPaginaEnum.PAISAGEM, OrientacaoPaginaEnum.PAISAGEM.getValue()));
		}
		return comboboxOrientacaoPaginaEnum;
	}

	public void setComboboxOrientacaoPaginaEnum(List<SelectItem> comboboxOrientacaoPaginaEnum) {
		this.comboboxOrientacaoPaginaEnum = comboboxOrientacaoPaginaEnum;
	}
	
	public Integer getColumnArquivos(){
		if(getArquivoVOs().size()>3){
			return 3;
		}
		return getArquivoVOs().size();
	}
	
	public Integer getElementosArquivos(){
		if(getArquivoVOs().size()>9){
			return 9;
		}
		return getArquivoVOs().size();
	}
}
