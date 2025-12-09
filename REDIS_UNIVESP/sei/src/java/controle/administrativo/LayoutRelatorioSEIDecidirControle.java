package controle.administrativo;

/**
 * @author Leonardo Riciolle 19/11/2014
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.SerializationUtils;
import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.DropEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle.MSG_TELA;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.PerguntaRespostaOrigemVO;
import negocio.comuns.academico.RespostaPerguntaRespostaOrigemVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FiltroPersonalizadoOpcaoVO;
import negocio.comuns.administrativo.FiltroPersonalizadoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirArquivoVO;
import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirFuncionarioVO;
import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirPerfilAcessoVO;
import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirVO;
import negocio.comuns.administrativo.LayoutRelatorioSeiDecidirCampoVO;
import negocio.comuns.administrativo.enumeradores.OrigemFiltroPersonalizadoEnum;
import negocio.comuns.administrativo.enumeradores.RelatorioSEIDecidirModuloEnum;
import negocio.comuns.administrativo.enumeradores.RelatorioSEIDecidirNivelDetalhamentoEnum;
import negocio.comuns.administrativo.enumeradores.RelatorioSEIDecidirTipoTotalizadorEnum;
import negocio.comuns.administrativo.enumeradores.TagSEIDecidirEntidadeEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoFiltroEnum;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.financeiro.RelatorioSEIDecidirVO;
import negocio.comuns.financeiro.enumerador.TipoFiltroPeriodoSeiDecidirEnum;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Controller("LayoutRelatorioSEIDecidirControle")
@Scope("viewScope")
@Lazy
public class LayoutRelatorioSEIDecidirControle extends SuperControleRelatorio implements Serializable {

	private static final long serialVersionUID = 1L;

	private LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO;
	private LayoutRelatorioSeiDecidirCampoVO layoutRelatorioSeiDecidirCampoVO;
	private TagSEIDecidirEntidadeEnum entidade;
	private List<PerfilTagSEIDecidirEnum> listaTags;
	
	private Boolean addTagAgrupamento;
	private Boolean addTagCondicaoJoin;
	private Boolean addTagCondicaoWhere;
	private Boolean addTagGroupBY;
	private Boolean addTagOrderBY;
	private List<SelectItem> tipoConsultaComboEntidade;
	private List<SelectItem> listaSelectItemFormatoGeracaoRelatorio;
	private List<SelectItem> listaSelectItemNivelDetalhamento;
	private List<SelectItem> listaSelectItemLayoutRelatorioSEIDecidir;

	private Boolean marcarTodosPerfilAcesso;
	private PerfilAcessoVO perfilAcessoVO;
	private List<PerfilAcessoVO> perfilAcessoVOs;

	private Boolean marcarTodosFuncionario;
	private FuncionarioVO funcionarioVO;
	private List<FuncionarioVO> funcionarioVOs;
	private String campoConsultaFuncionario;
	private String valorConsultaFuncionario;
	
	private Boolean habilitarGravarImagem;
	private String onCompletePanelUpload;
	
	private List<PerfilAcessoVO> listaIdsPerfilAcesso = new ArrayList<>();
	private List<FuncionarioVO> listaIdsFuncionario = new ArrayList<>();
	
	private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;
	
	private LayoutRelatorioSEIDecidirArquivoVO layoutRelatorioSEIDecidirArquivoVO;
	
	private String modulo;
	
	
	
	private FiltroPersonalizadoVO filtroPersonalizadoVO;
	private FiltroPersonalizadoOpcaoVO filtroPersonalizadoOpcaoVO;
	
	public LayoutRelatorioSEIDecidirControle() {
		try {
			setConfiguracaoGeralSistemaVO(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema());
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
		setLayoutRelatorioSEIDecidirVO(new LayoutRelatorioSEIDecidirVO());
	}
	
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<Object>(0));
		getControleConsultaOtimizado().getListaConsulta().clear();
		getControleConsulta().setValorConsulta("");
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("layoutRelatorioSEIDecidirCons");
	}

	public void inicializarDadosEditar(LayoutRelatorioSEIDecidirVO obj) {
		try {
			inicializarLayoutRelatorioPerfilAcesso(obj);

			inicializarLayoutRelatorioFuncionario(obj);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void inicializarLayoutRelatorioFuncionario(LayoutRelatorioSEIDecidirVO obj) throws Exception {
		List<LayoutRelatorioSEIDecidirFuncionarioVO> listaLayoutPerfilAcesso  = getFacadeFactory().getLayoutRelatorioSEIDecidirFuncionarioInterfaceFacade().consultarPorLayoutRelatorioSeiDecidir(obj.getCodigo());

		for(LayoutRelatorioSEIDecidirFuncionarioVO funcionarioVO : listaLayoutPerfilAcesso) {
			getListaIdsFuncionario().add(funcionarioVO.getFuncionarioVO());
		}
	}

	/**
	 * Carrega os dados referente ao {@link PerfilAcessoVO} vinculado com o {@link LayoutRelatorioSEIDecidirVO}
	 * 
	 * @param obj
	 * @throws Exception
	 */
	private void inicializarLayoutRelatorioPerfilAcesso(LayoutRelatorioSEIDecidirVO obj) throws Exception {
		List<LayoutRelatorioSEIDecidirPerfilAcessoVO> lista  = getFacadeFactory().getLayoutRelatorioSEIDecidirPerfilAcessoInterfaceFacade().consultarPorLayoutRelatorioSeiDecidir(obj.getCodigo());
		verificarTodasPerfilAcessosSelecionadas();
		getListaIdsPerfilAcesso().clear();

		for(LayoutRelatorioSEIDecidirPerfilAcessoVO layoutRelatorioSEIDecidirPerfilAcesso : lista) {
			getListaIdsPerfilAcesso().add(layoutRelatorioSEIDecidirPerfilAcesso.getPerfilAcessoVO());
		}
	}

	public void inicializarDados() {
		try {
			verificarTodasPerfilAcessosSelecionadas();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String novo() {
		try {
			removerObjetoMemoria(this);
			setLayoutRelatorioSEIDecidirVO(new LayoutRelatorioSEIDecidirVO());			
			setControleConsulta(new ControleConsulta());
			setMensagemID("msg_entre_dados");
			inicializarDados();
			getLayoutRelatorioSEIDecidirVO().setTelaLayout(true);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("layoutRelatorioSEIDecidirForm");
	}

	public void gravar() {
		try {
			getFacadeFactory().getLayoutRelatorioSEIDecidirInterfaceFacade().persistirTodos(getLayoutRelatorioSEIDecidirVO(), false, getUsuarioLogado(), getListaIdsPerfilAcesso(), getListaIdsFuncionario());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void excluir() {
		try {
			getFacadeFactory().getLayoutRelatorioSEIDecidirInterfaceFacade().excluir(getLayoutRelatorioSEIDecidirVO(), false, getUsuarioLogado());
			setLayoutRelatorioSEIDecidirVO(new LayoutRelatorioSEIDecidirVO());
			setLayoutRelatorioSeiDecidirCampoVO(new LayoutRelatorioSeiDecidirCampoVO());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String editar() throws Exception {
		try {
			LayoutRelatorioSEIDecidirVO obj = ((LayoutRelatorioSEIDecidirVO) context().getExternalContext().getRequestMap().get("layoutRelatorioSEIDecidirItem"));
			setLayoutRelatorioSEIDecidirVO(getFacadeFactory().getLayoutRelatorioSEIDecidirInterfaceFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, false, getUsuarioLogado()));
			inicializarDadosEditar(obj);
			getLayoutRelatorioSEIDecidirVO().setListaRelatorioSEIDecidirArquivo(getFacadeFactory().getLayoutRelatorioSEIDecidirArquivoInterfaceFacade().consultarPorLayoutRelatorioSeiDecidir(obj.getCodigo()));

			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("layoutRelatorioSEIDecidirForm");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
	}

	public void adicionarFuncionarioSelecionadosNoEditar() {
		if (Uteis.isAtributoPreenchido(getLayoutRelatorioSEIDecidirVO())) {
			getFuncionarioVOs().stream().forEach(obj -> {
				boolean existeFuncionario = getListaIdsFuncionario().stream().anyMatch(p -> p.getCodigo().equals(obj.getCodigo()));
				if (existeFuncionario) {
					obj.setFiltrarFuncionarioVO(Boolean.TRUE);
				}
			});
		}
	}

	public void adicionarPerfilAcessoSelecionadosNoEditar() {
		if (Uteis.isAtributoPreenchido(getLayoutRelatorioSEIDecidirVO())) {

			getPerfilAcessoVOs().stream().forEach(obj -> {
				boolean existePerfilAcesso = getListaIdsPerfilAcesso().stream().anyMatch(p -> p.getCodigo().equals(obj.getCodigo()));
				if (existePerfilAcesso) {
					obj.setFiltrarPerfilAcesso(Boolean.TRUE);
				}
			});
		}
	}

	public void editarLayoutRelatorioSeiDecidirCampoVOs() {
		try {
			setLayoutRelatorioSeiDecidirCampoVO(new LayoutRelatorioSeiDecidirCampoVO());
			setLayoutRelatorioSeiDecidirCampoVO((LayoutRelatorioSeiDecidirCampoVO) context().getExternalContext().getRequestMap().get("layoutRelatorioSeiDecidirCampoItem"));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void editarLayoutRelatorioSEIDecidirArquivo() {
		LayoutRelatorioSEIDecidirArquivoVO obj = (LayoutRelatorioSEIDecidirArquivoVO) context().getExternalContext().getRequestMap().get("layoutArquivo");
		obj.setItemEdicao(true);
		setLayoutRelatorioSEIDecidirArquivoVO( (LayoutRelatorioSEIDecidirArquivoVO) SerializationUtils.clone(obj));
		setOnCompletePanelUpload("RichFaces.$('panelUploadArquivo').show()");
	}

	public String consultar() throws Exception {
		try {
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getLayoutRelatorioSEIDecidirInterfaceFacade().consultar(
					getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), true, getUsuarioLogado(),
					getLayoutRelatorioSEIDecidirVO(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getModulo()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return "";
	}
	
	public void scrollerListener(DataScrollEvent dataScrollerEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
        getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());        
        consultar();
    }

	public void validarDados() throws Exception {
		if (getIsApresentarQtdCasasDecimais() == true) {
			if (getLayoutRelatorioSeiDecidirCampoVO().getQtdCasasDecimais().equals(0)) {
				throw new Exception(UteisJSF.internacionalizar("msg_LayoutRelatorioSeiDecidirCampo_QtdCasasDecimais"));
			}
		}
		if (getIsApresentarTextoTotalizador() == true) {
			if (getLayoutRelatorioSeiDecidirCampoVO().getTextoTotalizador().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("msg_LayoutRelatorioSeiDecidirCampo_TextoTotalizador"));
			}
		}
		if (getLayoutRelatorioSeiDecidirCampoVO().getTamanhoColuna().equals(0)) {
			throw new Exception(UteisJSF.internacionalizar("msg_LayoutRelatorioSeiDecidirCampo_TamanhoColuna"));
		}
	}

	public void adicionarLayoutRelatorioSeiDecidirCampoVO() {
		try {
			validarDados();
			getFacadeFactory().getLayoutRelatorioSEIDecidirInterfaceFacade().adicionarLayoutRelatorioSeiDecidirCampoVOs(getLayoutRelatorioSEIDecidirVO(), getLayoutRelatorioSeiDecidirCampoVO());
			setLayoutRelatorioSeiDecidirCampoVO(new LayoutRelatorioSeiDecidirCampoVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void removerLayoutRelatorioSeiDecidirCampoVO() {
		try {
			LayoutRelatorioSeiDecidirCampoVO obj = ((LayoutRelatorioSeiDecidirCampoVO) context().getExternalContext().getRequestMap().get("layoutRelatorioSeiDecidirCampoItem"));
			getFacadeFactory().getLayoutRelatorioSEIDecidirInterfaceFacade().removerLayoutRelatorioSeiDecidirCampoVOs(layoutRelatorioSEIDecidirVO, obj);
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	/**
	 * Remove {@link LayoutRelatorioSEIDecidirArquivoVO} pelo codigo do {@link ArquivoVO} selecionado.
	 * 
	 * @param obj - {@link LayoutRelatorioSEIDecidirArquivoVO}
	 */
	public void removerLayoutRelatorioArquivo(LayoutRelatorioSEIDecidirArquivoVO obj) {
		getLayoutRelatorioSEIDecidirVO().getListaRelatorioSEIDecidirArquivo().removeIf(p -> p.getArquivoVO().getCodigo().equals(obj.getArquivoVO().getCodigo()));
	}

	public void montarListaTagDisponivelEntidade() {
		getListaTags().clear();
		for (Enum<? extends PerfilTagSEIDecidirEnum> tag : getEntidade().getTagSeiDecidirEnum()) {
			getListaTags().add((PerfilTagSEIDecidirEnum) tag);
		}
		if (getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.HISTORICO)) {
			montarListaSelectItemComboEntidade();
		}
	}

	public void clonarLayoutRelatorio() {
		getLayoutRelatorioSEIDecidirVO().setCodigo(0);
		getLayoutRelatorioSEIDecidirVO().setNovoObj(true);
		for (LayoutRelatorioSeiDecidirCampoVO campo : getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs()) {
			campo.setCodigo(0);
			campo.setNovoObj(true);
			campo.getLayoutRelatorioSEIDecidirVO().setCodigo(0);
			campo.getLayoutRelatorioSEIDecidirVO().setNovoObj(true);
		}
		for (FiltroPersonalizadoVO filtroPersonalizadoVO : getLayoutRelatorioSEIDecidirVO().getListaFiltroPersonalizadoVOs()) {
			filtroPersonalizadoVO.setCodigo(0);
			filtroPersonalizadoVO.setNovoObj(true);
			filtroPersonalizadoVO.setCodigoOrigem(0);
			for (FiltroPersonalizadoOpcaoVO filtroPersonalizadoOpcaoVO : filtroPersonalizadoVO.getListaFiltroPersonalizadoOpcaoVOs()) {
				filtroPersonalizadoOpcaoVO.setCodigo(0);
				filtroPersonalizadoOpcaoVO.setNovoObj(true);
				filtroPersonalizadoOpcaoVO.getFiltroPersonalizadoVO().setCodigo(0);
				filtroPersonalizadoOpcaoVO.getFiltroPersonalizadoVO().setNovoObj(true);
			}
		}
	}
	
	 public void alterarOrdemApresentacaoCampo(DropEvent dropEvent) {
        try {
            if (dropEvent.getDragValue() instanceof LayoutRelatorioSeiDecidirCampoVO && dropEvent.getDropValue() instanceof LayoutRelatorioSeiDecidirCampoVO) {
                getFacadeFactory().getLayoutRelatorioSEIDecidirInterfaceFacade().alterarOrdemApresentacaoCampo(getLayoutRelatorioSEIDecidirVO(),
                        (LayoutRelatorioSeiDecidirCampoVO) dropEvent.getDragValue(), (LayoutRelatorioSeiDecidirCampoVO) dropEvent.getDropValue());
                limparMensagem();
            }
        } catch (Exception e) {
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
        }
    }
	
	public void adicionarTag(){
		PerfilTagSEIDecidirEnum tag = (PerfilTagSEIDecidirEnum) getRequestMap().get("tagItem");
		if(getAddTagCondicaoWhere()){
			if(!getLayoutRelatorioSEIDecidirVO().getCondicaoWhereAdicional().trim().isEmpty()){
				getLayoutRelatorioSEIDecidirVO().setCondicaoWhereAdicional(getLayoutRelatorioSEIDecidirVO().getCondicaoWhereAdicional() + " and ");
			}
			getLayoutRelatorioSEIDecidirVO().setCondicaoWhereAdicional(getLayoutRelatorioSEIDecidirVO().getCondicaoWhereAdicional() + tag.getTag()+ " = ");
		}else if(getAddTagAgrupamento() && !getLayoutRelatorioSEIDecidirVO().getAgruparRelatorioPor().contains(tag.getTag())){
			getLayoutRelatorioSEIDecidirVO().setAgruparRelatorioPor(getLayoutRelatorioSEIDecidirVO().getAgruparRelatorioPor().trim().isEmpty()?tag.getTag():getLayoutRelatorioSEIDecidirVO().getAgruparRelatorioPor()+" "+tag.getTag());
		}else if(getAddTagGroupBY() && !getLayoutRelatorioSEIDecidirVO().getGroupByAdicional().contains(tag.getTag())){
			getLayoutRelatorioSEIDecidirVO().setGroupByAdicional(getLayoutRelatorioSEIDecidirVO().getGroupByAdicional().trim().isEmpty()?tag.getTag():getLayoutRelatorioSEIDecidirVO().getGroupByAdicional()+", "+tag.getTag());
		}else if(getAddTagOrderBY() && !getLayoutRelatorioSEIDecidirVO().getOrderByAdicional().contains(tag.getTag())){
			getLayoutRelatorioSEIDecidirVO().setOrderByAdicional(getLayoutRelatorioSEIDecidirVO().getOrderByAdicional().trim().isEmpty()?tag.getTag():getLayoutRelatorioSEIDecidirVO().getOrderByAdicional()+", "+tag.getTag());
		}else if(!getAddTagAgrupamento() && !getLayoutRelatorioSeiDecidirCampoVO().getCampo().contains(tag.getTag())){
			getLayoutRelatorioSeiDecidirCampoVO().setCampo(getLayoutRelatorioSeiDecidirCampoVO().getCampo().trim().isEmpty()?tag.getTag():getLayoutRelatorioSeiDecidirCampoVO().getCampo()+" + "+tag.getTag());
			getLayoutRelatorioSeiDecidirCampoVO().setTipoCampo(tag.getTipoCampo());
			getLayoutRelatorioSeiDecidirCampoVO().setTitulo(tag.getAtributo());
			
			if(getLayoutRelatorioSeiDecidirCampoVO().getTamanhoColuna().equals(0)){				
					getLayoutRelatorioSeiDecidirCampoVO().setTamanhoColuna(tag.getTamanhoCampo());				
			}
		}
	}

	/**
	 * Adicionar {@link LayoutRelatorioSEIDecidirArquivoVO} a lista .
	 */
	public void adicionarArquivoLista() {
		try {

			if (Uteis.isAtributoPreenchido(getLayoutRelatorioSEIDecidirVO().getCodigo())) {
				if (getLayoutRelatorioSEIDecidirVO().getCodigo().equals(getLayoutRelatorioSEIDecidirArquivoVO().getLayoutRelatorioSEIDecidirSuperiorVO().getCodigo())) {
					setMensagemDetalhada("O campo LAYOUT RELATÓRIO SUPERIOR não deve ser o mesmo que está sendo editado.");
					return;
				}
			}
			
			if (!Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO)) {
				setConfiguracaoGeralSistemaVO(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema());
			}

			persistirArquivo();

			adicionarLayoutRelatorioSeiDecidirArquivo();
			
			setOnCompletePanelUpload("RichFaces.$('panelUploadArquivo').hide()");
			setMensagemID(MSG_TELA.msg_dados_editar.name());
			setMensagemDetalhada("");
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	}
	
	/**
	 * remove o {@link LayoutRelatorioSEIDecidirArquivoVO} da lista 
	 * 
	 * @param layoutRelatorioSEIDecidirArquivoVO
	 */
	public void removerLayoutRelaentidadetorioArquivo(LayoutRelatorioSEIDecidirArquivoVO layoutRelatorioSEIDecidirArquivoVO) {
		getLayoutRelatorioSEIDecidirVO().getListaRelatorioSEIDecidirArquivo().removeIf(p -> p.getCodigo().equals(layoutRelatorioSEIDecidirArquivoVO.getCodigo()));
	}

	private void adicionarLayoutRelatorioSeiDecidirArquivo() throws Exception {

		getLayoutRelatorioSEIDecidirArquivoVO().setLayoutRelatorioSEIDecidirSuperiorVO(
				getFacadeFactory().getLayoutRelatorioSEIDecidirInterfaceFacade().consultarPorChavePrimaria(
				getLayoutRelatorioSEIDecidirArquivoVO().getLayoutRelatorioSEIDecidirSuperiorVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));

		if (getLayoutRelatorioSEIDecidirArquivoVO().getItemEdicao()) {
			Iterator<LayoutRelatorioSEIDecidirArquivoVO> i = getLayoutRelatorioSEIDecidirVO().getListaRelatorioSEIDecidirArquivo().iterator();
			int index = 0;
			int aux = -1;
			LayoutRelatorioSEIDecidirArquivoVO objAux = new LayoutRelatorioSEIDecidirArquivoVO();
			while(i.hasNext()) {
				LayoutRelatorioSEIDecidirArquivoVO objExistente = i.next();
				
				if (objExistente.getCodigo().equals(getLayoutRelatorioSEIDecidirArquivoVO().getCodigo()) && objExistente.getItemEdicao()){
					getLayoutRelatorioSEIDecidirArquivoVO().setItemEdicao(false);
			       	aux = index;
			       	objAux = getLayoutRelatorioSEIDecidirArquivoVO();
				}
		        index++;
			}
			
			if(aux >= 0) {
				getLayoutRelatorioSEIDecidirVO().getListaRelatorioSEIDecidirArquivo().set(aux, objAux);
			}
			
		} else {
			getLayoutRelatorioSEIDecidirVO().getListaRelatorioSEIDecidirArquivo().add(getLayoutRelatorioSEIDecidirArquivoVO());
		}
		setLayoutRelatorioSEIDecidirArquivoVO(new LayoutRelatorioSEIDecidirArquivoVO());
	}

	private void persistirArquivo() throws Exception {
		getLayoutRelatorioSEIDecidirArquivoVO().getArquivoVO().setValidarDisciplina(Boolean.FALSE);
		getLayoutRelatorioSEIDecidirArquivoVO().getArquivoVO().setResponsavelUpload(getUsuarioLogado());
		getLayoutRelatorioSEIDecidirArquivoVO().getArquivoVO().setDataUpload(new Date());
		getLayoutRelatorioSEIDecidirArquivoVO().getArquivoVO().setDataDisponibilizacao(new Date());
		getLayoutRelatorioSEIDecidirArquivoVO().getArquivoVO().setManterDisponibilizacao(false);
		getLayoutRelatorioSEIDecidirArquivoVO().getArquivoVO().setOrigem(OrigemArquivo.LAYOUT_RELATORIO_SEI_DECIDIDIR.getValor());
		getLayoutRelatorioSEIDecidirArquivoVO().getArquivoVO().setCodOrigem(getLayoutRelatorioSEIDecidirVO().getCodigo());
		getLayoutRelatorioSEIDecidirArquivoVO().getArquivoVO().setSituacao(SituacaoArquivo.ATIVO.getValor());
		getLayoutRelatorioSEIDecidirArquivoVO().getArquivoVO().setDescricaoArquivo(OrigemArquivo.LAYOUT_RELATORIO_SEI_DECIDIDIR.getDescricao());

		getFacadeFactory().getArquivoFacade().persistir(getLayoutRelatorioSEIDecidirArquivoVO().getArquivoVO(), false, getUsuarioLogado(), configuracaoGeralSistemaVO);
	}

	/**
	 * Metodo para alternar as posições de cada celula inserida
	 * 
	 * @return
	 */
	public void ladoDireitoOpcaoLayoutRelatorioSeiDecidirCampoVO() {
		try {
			LayoutRelatorioSeiDecidirCampoVO objOpcao1 = (LayoutRelatorioSeiDecidirCampoVO) (context().getExternalContext().getRequestMap().get("layoutRelatorioSeiDecidirCampoItem"));
			getFacadeFactory().getLayoutRelatorioSEIDecidirInterfaceFacade().realizarAlteracaoOrdemLayoutRelatorioSeiDecidirCampoVOs(getLayoutRelatorioSEIDecidirVO(), objOpcao1, false);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void ladoEsquerdoLayoutRelatorioSeiDecidirCampoVO() {
		try {
			LayoutRelatorioSeiDecidirCampoVO objOpcao1 = (LayoutRelatorioSeiDecidirCampoVO) (context().getExternalContext().getRequestMap().get("layoutRelatorioSeiDecidirCampoItem"));
			getFacadeFactory().getLayoutRelatorioSEIDecidirInterfaceFacade().realizarAlteracaoOrdemLayoutRelatorioSeiDecidirCampoVOs(getLayoutRelatorioSEIDecidirVO(), objOpcao1, true);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void verificarTodasPerfilAcessosSelecionadas() {
		StringBuilder perfilAcesso = new StringBuilder();
		getListaIdsPerfilAcesso().clear();
		if (getPerfilAcessoVOs().size() > 1) {
			for (PerfilAcessoVO obj : getPerfilAcessoVOs()) {
				if (obj.getFiltrarPerfilAcesso()) {
					getListaIdsPerfilAcesso().add(obj);
				}
			}
			getPerfilAcessoVO().setNome(perfilAcesso.toString());
		} else {
			if (!getPerfilAcessoVOs().isEmpty()) {
				if (getPerfilAcessoVOs().get(0).getFiltrarPerfilAcesso()) {
					getPerfilAcessoVO().setNome(getPerfilAcessoVOs().get(0).getNome());
					getListaIdsPerfilAcesso().add(getPerfilAcessoVOs().get(0));
				}
			}
		}
	}

	public void marcarTodosPerfilAcessosAction() {
		for (PerfilAcessoVO perfilAcesso : getPerfilAcessoVOs()) {
			if (getMarcarTodosPerfilAcesso()) {
				perfilAcesso.setFiltrarPerfilAcesso(Boolean.TRUE);
			} else {
				perfilAcesso.setFiltrarPerfilAcesso(Boolean.FALSE);
			}
		}
		verificarTodasPerfilAcessosSelecionadas();
	}

	public void marcarTodosFuncionariosAction () {
		for (FuncionarioVO funcionarioVO : getFuncionarioVOs()) {
			if (getMarcarTodosFuncionario()) {
				funcionarioVO.setFiltrarFuncionarioVO(Boolean.TRUE);
			} else {
				funcionarioVO.setFiltrarFuncionarioVO(Boolean.FALSE);
			}
		}
		verificarTodasFuncionariosSelecionadas();
	}
	
	public void verificarTodasFuncionariosSelecionadas() {
		StringBuilder stringBuilder = new StringBuilder();
		getListaIdsFuncionario().clear();
		if (getFuncionarioVOs().size() > 1) {
			for (FuncionarioVO obj : getFuncionarioVOs()) {
				if (obj.getFiltrarFuncionarioVO()) {
					stringBuilder.append(obj.getPessoa().getNome()).append("; ");
					getListaIdsFuncionario().add(obj);
				}
			}
			getFuncionarioVO().getPessoa().setNome(stringBuilder.toString());
		} else {
			if (!getFuncionarioVOs().isEmpty()) {
				if (getFuncionarioVOs().get(0).getFiltrarFuncionarioVO()) {
					getFuncionarioVO().getPessoa().setNome(getFuncionarioVOs().get(0).getPessoa().getNome());
					getListaIdsFuncionario().add(getFuncionarioVOs().get(0));
				}
			} else {
				getFuncionarioVO().getPessoa().setNome(stringBuilder.toString());
			}
		}
	}
	
	public void carregarDadosPanelUpload() {
		try {
			setHabilitarGravarImagem(true);
			setLayoutRelatorioSEIDecidirArquivoVO(new LayoutRelatorioSEIDecidirArquivoVO());
			setMensagemID(MSG_TELA.msg_entre_dados.name());
			setOnCompletePanelUpload("RichFaces.$('panelUploadArquivo').show()");
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
			setOnCompletePanelUpload("");
		}
	}

	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			if (uploadEvent.getUploadedFile() != null && uploadEvent.getUploadedFile().getSize() > 15360000) {
				setMensagemDetalhada("Prezado Usuário, seu arquivo excede o tamanho máximo para upload de 15Mb, por favor reduza o arquivo ou divida em partes antes de efetuar a postagem. Obrigado.");
				setHabilitarGravarImagem(false);
			} else {
				getLayoutRelatorioSEIDecidirArquivoVO().getArquivoVO().setOrigem(OrigemArquivo.LAYOUT_RELATORIO_SEI_DECIDIDIR.getValor());
				getLayoutRelatorioSEIDecidirArquivoVO().getArquivoVO().setCodOrigem(getLayoutRelatorioSEIDecidirVO().getCodigo());
				getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getLayoutRelatorioSEIDecidirArquivoVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.IREPORT_TMP, getUsuarioLogado());
				setMensagemID(MSG_TELA.msg_entre_dados.name());
				setHabilitarGravarImagem(true);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}
	
	public void selecionarArquivoParaDownload() {
		try {
			LayoutRelatorioSEIDecidirArquivoVO obj = (LayoutRelatorioSEIDecidirArquivoVO) context().getExternalContext().getRequestMap().get("layoutArquivo"); 
			context().getExternalContext().getSessionMap().put("arquivoVO", obj.getArquivoVO());
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	//GETTER AND SETTER
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("descricao", "Descrição"));
		return objs;
	}

	public List<SelectItem> getListaSelectItemModulo() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(RelatorioSEIDecidirModuloEnum.class, "name", "valorApresentar", false);
	}

	public void montarListaSelectItemNivelDetalhamento() {
		listaSelectItemNivelDetalhamento =  null;
	}
	
	public List<SelectItem> getListaSelectItemNivelDetalhamento() {
		if(listaSelectItemNivelDetalhamento == null) {
			listaSelectItemNivelDetalhamento = new ArrayList<SelectItem>();
			for(RelatorioSEIDecidirNivelDetalhamentoEnum item : getLayoutRelatorioSEIDecidirVO().getModulo().getRelatorioSEIDecidirNivelDetalhamentoEnums()) {
				listaSelectItemNivelDetalhamento.add(new SelectItem(item, item.getValorApresentar()));
			}
		}
		return listaSelectItemNivelDetalhamento;
	}

	public List<SelectItem> getListaSelectTipoCampo() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoCampoEnum.class, "name", "valorApresentar", false);
	}

	public List<SelectItem> getListaSelectItemLayoutRelatorioSEIDecidir() {
		if (listaSelectItemLayoutRelatorioSEIDecidir == null) {
			listaSelectItemLayoutRelatorioSEIDecidir = new ArrayList<>();
			listaSelectItemLayoutRelatorioSEIDecidir.add(new SelectItem("", ""));
			try {
				List<LayoutRelatorioSEIDecidirVO> lista = getFacadeFactory().getLayoutRelatorioSEIDecidirInterfaceFacade().consultarTodos(false, getUsuarioLogado(), Uteis.NIVELMONTARDADOS_DADOSBASICOS);
				for (LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO : lista) {
					listaSelectItemLayoutRelatorioSEIDecidir.add(new SelectItem(layoutRelatorioSEIDecidirVO.getCodigo(), layoutRelatorioSEIDecidirVO.getDescricao()));
				}
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
		return listaSelectItemLayoutRelatorioSEIDecidir;
	}

	public void setListaSelectItemLayoutRelatorioSEIDecidir(List<SelectItem> listaSelectItemLayoutRelatorioSEIDecidir) {
		this.listaSelectItemLayoutRelatorioSEIDecidir = listaSelectItemLayoutRelatorioSEIDecidir;
	}

	public List<SelectItem> getListaSelectTipoTotalizador() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(RelatorioSEIDecidirTipoTotalizadorEnum.class, "name", "valorApresentar", false);
	}

	public boolean getIsApresentarQtdCasasDecimais() {
		return getLayoutRelatorioSeiDecidirCampoVO().getTipoCampo().equals(TipoCampoEnum.DOUBLE);
	}

	public boolean getIsApresentarTextoTotalizador() {
		return getLayoutRelatorioSeiDecidirCampoVO().getTipoTotalizador().equals(RelatorioSEIDecidirTipoTotalizadorEnum.TEXTO);
	}

	public LayoutRelatorioSEIDecidirVO getLayoutRelatorioSEIDecidirVO() {
		if (layoutRelatorioSEIDecidirVO == null) {
			layoutRelatorioSEIDecidirVO = new LayoutRelatorioSEIDecidirVO();
		}
		return layoutRelatorioSEIDecidirVO;
	}

	public void setLayoutRelatorioSEIDecidirVO(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO) {
		this.layoutRelatorioSEIDecidirVO = layoutRelatorioSEIDecidirVO;
	}

	public LayoutRelatorioSeiDecidirCampoVO getLayoutRelatorioSeiDecidirCampoVO() {
		if (layoutRelatorioSeiDecidirCampoVO == null) {
			layoutRelatorioSeiDecidirCampoVO = new LayoutRelatorioSeiDecidirCampoVO();
		}
		return layoutRelatorioSeiDecidirCampoVO;
	}

	public void setLayoutRelatorioSeiDecidirCampoVO(LayoutRelatorioSeiDecidirCampoVO layoutRelatorioSeiDecidirCampoVO) {
		this.layoutRelatorioSeiDecidirCampoVO = layoutRelatorioSeiDecidirCampoVO;
	}

	public Integer getColumnLayoutRelatorioSeiDecidirCampoVO() throws Exception {
		if(getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs().size() > 10) {
			return 10;
		}
		return getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs().size();
	}

	public Integer getElementLayoutRelatorioSeiDecidirCampoVO() throws Exception {
		return getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs().size();
	}

	public TagSEIDecidirEntidadeEnum getEntidade() {
		if (entidade == null) {
			entidade = TagSEIDecidirEntidadeEnum.UNIDADE_ENSINO;
			montarListaTagDisponivelEntidade();
		}
		return entidade;
	}

	public void setEntidade(TagSEIDecidirEntidadeEnum entidade) {
		this.entidade = entidade;
	}
	
	public void montarListaSelectItemComboEntidade(){
		tipoConsultaComboEntidade = UtilSelectItem.getListaSelectItemEnum(TagSEIDecidirEntidadeEnum.getValues(getLayoutRelatorioSEIDecidirVO().getModulo(), getLayoutRelatorioSEIDecidirVO().getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.HISTORICO) ? true : false),  Obrigatorio.SIM);
		montarListaSelectItemNivelDetalhamento();
	}

	public List<SelectItem> getTipoConsultaComboEntidade() {
		if (tipoConsultaComboEntidade == null) {
			montarListaSelectItemComboEntidade();
		}
		return tipoConsultaComboEntidade;
	}

	public void setTipoConsultaComboEntidade(List<SelectItem> tipoConsultaComboEntidade) {
		this.tipoConsultaComboEntidade = tipoConsultaComboEntidade;
	}

	public List<PerfilTagSEIDecidirEnum> getListaTags() {
		if (listaTags == null) {
			listaTags = new ArrayList<PerfilTagSEIDecidirEnum>(0);
		}
		return listaTags;
	}

	public void setListaTags(List<PerfilTagSEIDecidirEnum> listaTags) {
		this.listaTags = listaTags;
	}

	public Boolean getAddTagAgrupamento() {
		if (addTagAgrupamento == null) {
			addTagAgrupamento = false;
		}
		return addTagAgrupamento;
	}

	public void setAddTagAgrupamento(Boolean addTagAgrupamento) {
		this.addTagAgrupamento = addTagAgrupamento;
	}
	
	public void realizarGeracaoRelatorio() {
		try {
			setFazerDownload(false);
			RelatorioSEIDecidirVO relatorioSEIDecidirVO = new RelatorioSEIDecidirVO();
			relatorioSEIDecidirVO.setDataInicio(Uteis.getDataPrimeiroDiaMes(new Date()));
			relatorioSEIDecidirVO.setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
			relatorioSEIDecidirVO.setLayoutRelatorioSEIDecidirVO(getLayoutRelatorioSEIDecidirVO());
			if(getLayoutRelatorioSEIDecidirVO().getModulo().equals(RelatorioSEIDecidirModuloEnum.ACADEMICO)){
				relatorioSEIDecidirVO.setTipoFiltroPeriodo(TipoFiltroPeriodoSeiDecidirEnum.ANO_SEMESTRE);
				relatorioSEIDecidirVO.setAno(Uteis.getAnoDataAtual4Digitos());
				relatorioSEIDecidirVO.setSemestre(Uteis.getSemestreAtual());
			}else{
				relatorioSEIDecidirVO.setTipoFiltroPeriodo(TipoFiltroPeriodoSeiDecidirEnum.DATA_VENCIMENTO);
			}
			relatorioSEIDecidirVO.setUnidadeEnsinoVOs(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoFaltandoLista(getUnidadeEnsinoVOs(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			this.setCaminhoRelatorio(getFacadeFactory().getRelatorioSeiDecidirFacade().realizarGeracaoRelatorioSeiDecidir(relatorioSEIDecidirVO, relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getTipoGeracaoRelatorio(), getLogoPadraoRelatorio(), false, "", "", true, getUsuarioLogado(), getConfiguracaoGeralSistemaVO(), null));
			setFazerDownload(true);
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarExportacaoLayout() {
		try {
			setFazerDownload(false);			
			this.setCaminhoRelatorio(getFacadeFactory().getLayoutRelatorioSEIDecidirInterfaceFacade().realizarExportacaoLayout(getLayoutRelatorioSEIDecidirVO()));
			setFazerDownload(true);
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarImportacaoLayout(FileUploadEvent event) {
		try {
			setLayoutRelatorioSEIDecidirVO(getFacadeFactory().getLayoutRelatorioSEIDecidirInterfaceFacade().realizarImportacaoLayout(event));
			montarListaSelectItemNivelDetalhamento();
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarPerfisAcessos() {
		try {
			if (!Uteis.isAtributoPreenchido(getPerfilAcessoVOs())) {
				setPerfilAcessoVOs(getFacadeFactory().getPerfilAcessoFacade().consultarPorNome("%", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				adicionarPerfilAcessoSelecionadosNoEditar();
				verificarTodasPerfilAcessosSelecionadas();
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * Consulta responsavel por retornar os usuarios do popup de pesquisa 
	 * do funcionario
	 */
	public void consultarFuncionario() {
		try {
			if (!Uteis.isAtributoPreenchido(getFuncionarioVOs())) {
				setFuncionarioVOs(getFacadeFactory().getFuncionarioFacade().consultarFuncionarioPorNomeAtivo("", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				
				adicionarFuncionarioSelecionadosNoEditar();
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparDadosFuncionario () {
		getListaIdsFuncionario().clear();
		setFuncionarioVO(new FuncionarioVO());
		setFuncionarioVOs(new ArrayList<>());
		getControleConsulta().setValorConsulta("");
	}

	public void selecionarFuncionario() throws Exception {
		try {

			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItem");
			StringBuilder nome = new StringBuilder();
			for (FuncionarioVO funcionario : getListaIdsFuncionario()) {
				if (obj.getCodigo().equals(funcionario.getCodigo())) {
					throw new Exception("Funcionário Já adicionado a lista.");
				}
				nome.append(funcionario.getPessoa().getNome() + ";");
			}
			getFuncionarioVO().getPessoa().setNome(nome.toString());

			getListaIdsFuncionario().add(obj);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	/**
	 * @return the addTagCondicaoWhere
	 */
	public Boolean getAddTagCondicaoWhere() {
		if (addTagCondicaoWhere == null) {
			addTagCondicaoWhere = false;
		}
		return addTagCondicaoWhere;
	}

	/**
	 * @param addTagCondicaoWhere the addTagCondicaoWhere to set
	 */
	public void setAddTagCondicaoWhere(Boolean addTagCondicaoWhere) {
		this.addTagCondicaoWhere = addTagCondicaoWhere;
	}
	


	public Boolean getAddTagGroupBY() {
		if(addTagGroupBY == null){
			addTagGroupBY = false;
		}
		return addTagGroupBY;
	}

	public void setAddTagGroupBY(Boolean addTagGroupBY) {
		this.addTagGroupBY = addTagGroupBY;
	}

	public List<SelectItem> getListaSelectItemFormatoGeracaoRelatorio() {
		if(listaSelectItemFormatoGeracaoRelatorio == null){
			listaSelectItemFormatoGeracaoRelatorio = new ArrayList<SelectItem>(0);
			listaSelectItemFormatoGeracaoRelatorio.add(new SelectItem(TipoRelatorioEnum.EXCEL, TipoRelatorioEnum.EXCEL.name()));
			listaSelectItemFormatoGeracaoRelatorio.add(new SelectItem(TipoRelatorioEnum.XML, TipoRelatorioEnum.XML.name()));
			listaSelectItemFormatoGeracaoRelatorio.add(new SelectItem(TipoRelatorioEnum.PDF, TipoRelatorioEnum.PDF.name()));
		}
		return listaSelectItemFormatoGeracaoRelatorio;
	}

	public void setListaSelectItemFormatoGeracaoRelatorio(List<SelectItem> listaSelectItemFormatoGeracaoRelatorio) {
		this.listaSelectItemFormatoGeracaoRelatorio = listaSelectItemFormatoGeracaoRelatorio;
	}

	public List<PerfilAcessoVO> getPerfilAcessoVOs() {
		if (perfilAcessoVOs == null) {
			perfilAcessoVOs = new ArrayList<>();
		}
		return perfilAcessoVOs;
	}

	public void setPerfilAcessoVOs(List<PerfilAcessoVO> perfilAcessoVOs) {
		this.perfilAcessoVOs = perfilAcessoVOs;
	}

	public PerfilAcessoVO getPerfilAcessoVO() {
		if (perfilAcessoVO == null) {
			perfilAcessoVO = new PerfilAcessoVO();
		}
		return perfilAcessoVO;
	}

	public void setPerfilAcessoVO(PerfilAcessoVO perfilAcessoVO) {
		this.perfilAcessoVO = perfilAcessoVO;
	}

	public Boolean getMarcarTodosPerfilAcesso() {
		if (marcarTodosPerfilAcesso == null) {
			marcarTodosPerfilAcesso = Boolean.FALSE;
		}
		return marcarTodosPerfilAcesso;
	}

	public void setMarcarTodosPerfilAcesso(Boolean marcarTodosPerfilAcesso) {
		if (marcarTodosPerfilAcesso == null) {
			marcarTodosPerfilAcesso = Boolean.FALSE;
		}
		this.marcarTodosPerfilAcesso = marcarTodosPerfilAcesso;
	}

	public Boolean getMarcarTodosFuncionario() {
		return marcarTodosFuncionario;
	}

	public void setMarcarTodosFuncionario(Boolean marcarTodosFuncionario) {
		this.marcarTodosFuncionario = marcarTodosFuncionario;
	}

	public FuncionarioVO getFuncionarioVO() {
		if (funcionarioVO == null) {
			funcionarioVO = new FuncionarioVO();
		}
		return funcionarioVO;
	}

	public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
		this.funcionarioVO = funcionarioVO;
	}

	public List<FuncionarioVO> getFuncionarioVOs() {
		if (funcionarioVOs == null) {
			funcionarioVOs = new ArrayList<>();
		}
		return funcionarioVOs;
	}

	public void setFuncionarioVOs(List<FuncionarioVO> funcionarioVOs) {
		this.funcionarioVOs = funcionarioVOs;
	}

	public List<PerfilAcessoVO> getListaIdsPerfilAcesso() {
		if (listaIdsPerfilAcesso == null) {
			listaIdsPerfilAcesso = new ArrayList<>();
		}
		return listaIdsPerfilAcesso;
	}

	public void setListaIdsPerfilAcesso(List<PerfilAcessoVO> listaIdsPerfilAcesso) {
		this.listaIdsPerfilAcesso = listaIdsPerfilAcesso;
	}

	public List<FuncionarioVO> getListaIdsFuncionario() {
		if (listaIdsFuncionario== null) {
			listaIdsFuncionario = new ArrayList<>();
		}
		return listaIdsFuncionario;
	}

	public void setListaIdsFuncionario(List<FuncionarioVO> listaIdsFuncionario) {
		this.listaIdsFuncionario = listaIdsFuncionario;
	}

	public String getCampoConsultaFuncionario() {
		if (campoConsultaFuncionario == null) {
			campoConsultaFuncionario = "";
		}
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		if (valorConsultaFuncionario == null ) {
			valorConsultaFuncionario = "";
		}
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}
	
	public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}
	

	public Boolean getHabilitarGravarImagem() {
		if (habilitarGravarImagem == null) {
			habilitarGravarImagem = Boolean.TRUE;
		}
		return habilitarGravarImagem;
	}

	public void setHabilitarGravarImagem(Boolean habilitarGravarImagem) {
		this.habilitarGravarImagem = habilitarGravarImagem;
	}

	public String getOnCompletePanelUpload() {
		if (onCompletePanelUpload == null) {
			onCompletePanelUpload = "";
		}
		return onCompletePanelUpload;
	}

	public void setOnCompletePanelUpload(String onCompletePanelUpload) {
		this.onCompletePanelUpload = onCompletePanelUpload;
	}
	
	public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaVO() {
		if (configuracaoGeralSistemaVO == null) {
			configuracaoGeralSistemaVO = new ConfiguracaoGeralSistemaVO();
		}
		return configuracaoGeralSistemaVO;
	}

	public void setConfiguracaoGeralSistemaVO(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		this.configuracaoGeralSistemaVO = configuracaoGeralSistemaVO;
	}

	public LayoutRelatorioSEIDecidirArquivoVO getLayoutRelatorioSEIDecidirArquivoVO() {
		if (layoutRelatorioSEIDecidirArquivoVO == null) {
			layoutRelatorioSEIDecidirArquivoVO = new LayoutRelatorioSEIDecidirArquivoVO();
		}
		return layoutRelatorioSEIDecidirArquivoVO;
	}

	public void setLayoutRelatorioSEIDecidirArquivoVO(
			LayoutRelatorioSEIDecidirArquivoVO layoutRelatorioSEIDecidirArquivoVO) {
		this.layoutRelatorioSEIDecidirArquivoVO = layoutRelatorioSEIDecidirArquivoVO;
	}

	public String getModulo() {
		if (modulo == null) {
			modulo = "TODOS";
		}
		return modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	public List<SelectItem> getModulosRelatorioSEIDecidir() {
		List<SelectItem> lista = new ArrayList<>();
		lista.add(new SelectItem("TODOS", "Todos"));
		for (RelatorioSEIDecidirModuloEnum modulo : RelatorioSEIDecidirModuloEnum.values()) {
			lista.add(new SelectItem(modulo.name(), modulo.getValorApresentar()));
		}
		return lista;
	}
	
	public Boolean getAddTagCondicaoJoin() {
		if (addTagCondicaoJoin == null) {
			addTagCondicaoJoin = false;
		}
		return addTagCondicaoJoin;
	}

	public void setAddTagCondicaoJoin(Boolean addTagCondicaoJoin) {
		this.addTagCondicaoJoin = addTagCondicaoJoin;
	}

	public Boolean getAddTagOrderBY() {
		if(addTagOrderBY == null) {
			addTagOrderBY =  false;
}
		return addTagOrderBY;
	}

	public void setAddTagOrderBY(Boolean addTagOrderBY) {
		this.addTagOrderBY = addTagOrderBY;
	}

	public FiltroPersonalizadoVO getFiltroPersonalizadoVO() {
		if (filtroPersonalizadoVO == null) {
			filtroPersonalizadoVO = new FiltroPersonalizadoVO();
		}
		return filtroPersonalizadoVO;
	}

	public void setFiltroPersonalizadoVO(FiltroPersonalizadoVO filtroPersonalizadoVO) {
		this.filtroPersonalizadoVO = filtroPersonalizadoVO;
	}
	
	public void consultarFiltroPersonalizado() {
//		getLayoutRelatorioSEIDecidirVO().setListaFiltroPersonalizadoVOs(getFacadeFactory().getFiltroPersonalizadoFacade().consultarPorOrigemCodigoOrigem(getLayoutRelatorioSEIDecidirVO().getCodigo(), OrigemFiltroPersonalizadoEnum.SEI_DECIDIR, getUsuarioLogado()));
		inicializarDadosComboboxCustomizavel();
	}
	
	public void adicionarFiltroPersonalizado() {
		try {
			getFacadeFactory().getFiltroPersonalizadoFacade().adicionarFiltroPersonalizado(getFiltroPersonalizadoVO(), getLayoutRelatorioSEIDecidirVO().getListaFiltroPersonalizadoVOs(), getUsuarioLogado());
			setFiltroPersonalizadoVO(new FiltroPersonalizadoVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void editarFiltroPersonalizado() {
		FiltroPersonalizadoVO obj = (FiltroPersonalizadoVO) context().getExternalContext().getRequestMap().get("filtroPersonalizadoItem");
		obj.setEditando(true);
		setFiltroPersonalizadoVO(obj);
	}
	
	public void removerFiltroPersonalizado() {
		FiltroPersonalizadoVO obj = (FiltroPersonalizadoVO) context().getExternalContext().getRequestMap().get("filtroPersonalizadoItem");
		getFacadeFactory().getFiltroPersonalizadoFacade().removerFiltroPersonalizado(obj, getLayoutRelatorioSEIDecidirVO().getListaFiltroPersonalizadoVOs(), getUsuarioLogado());
		setMensagem("msg_dados_excluidos");
	}
	
	public void inicializarDadosComboboxCustomizavel() {
		for (FiltroPersonalizadoVO obj : getLayoutRelatorioSEIDecidirVO().getListaFiltroPersonalizadoVOs()) {
			getFacadeFactory().getFiltroPersonalizadoFacade().inicializarDadosComboBoxCustomizavel(obj, true, getUsuarioLogado());
			getFacadeFactory().getFiltroPersonalizadoFacade().inicializarDadosCombobox(obj, getUsuarioLogado());
		}
	}
	
	public List<SelectItem> getListaSelectItemTipoCampoFiltroVOs() {
		return TipoCampoFiltroEnum.getListaSelectItemTipoCampoFiltroVOs();
	}
	
	public void adicionarFiltroPersonalizadoOpcao() {
		try {
			getFacadeFactory().getFiltroPersonalizadoOpcaoInterfaceFacade().adicionarFiltroPersonalizadoOpcao(getFiltroPersonalizadoOpcaoVO(), getFiltroPersonalizadoVO(), getFiltroPersonalizadoVO().getListaFiltroPersonalizadoOpcaoVOs(), getUsuarioLogado());
			setFiltroPersonalizadoOpcaoVO(new FiltroPersonalizadoOpcaoVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void editarFiltroPersonalizadoOpcao() {
		FiltroPersonalizadoOpcaoVO obj = (FiltroPersonalizadoOpcaoVO) context().getExternalContext().getRequestMap().get("filtroOpcaoItem");
		setFiltroPersonalizadoOpcaoVO(obj);
	}
	
	public void removerFiltroPersonalizadoOpcao() {
		FiltroPersonalizadoOpcaoVO obj = (FiltroPersonalizadoOpcaoVO) context().getExternalContext().getRequestMap().get("filtroOpcaoItem");
		getFacadeFactory().getFiltroPersonalizadoOpcaoInterfaceFacade().removerFiltroPersonalizadoOpcao(obj, getFiltroPersonalizadoVO().getListaFiltroPersonalizadoOpcaoVOs(), getUsuarioLogado());
		setMensagem("msg_dados_excluidos");
	}

	public FiltroPersonalizadoOpcaoVO getFiltroPersonalizadoOpcaoVO() {
		if (filtroPersonalizadoOpcaoVO == null) {
			filtroPersonalizadoOpcaoVO = new FiltroPersonalizadoOpcaoVO();
		}
		return filtroPersonalizadoOpcaoVO;
	}

	public void setFiltroPersonalizadoOpcaoVO(FiltroPersonalizadoOpcaoVO filtroPersonalizadoOpcaoVO) {
		this.filtroPersonalizadoOpcaoVO = filtroPersonalizadoOpcaoVO;
	}
	
	public void subirFiltroPersonalizado() {
        try {
        	FiltroPersonalizadoVO filtro1 = (FiltroPersonalizadoVO) context().getExternalContext().getRequestMap().get("filtroPersonalizadoItem");
            if (filtro1.getOrdem() > 1) {
            	FiltroPersonalizadoVO filtro2 = getLayoutRelatorioSEIDecidirVO().getListaFiltroPersonalizadoVOs().get(filtro1.getOrdem() - 2);
            	getFacadeFactory().getLayoutRelatorioSEIDecidirInterfaceFacade().alterarOrdemFiltroPersonalizado(getLayoutRelatorioSEIDecidirVO(), filtro1, filtro2);
            }
            limparMensagem();

        } catch (Exception e) {
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
        }
    }

    public void descerFiltroPersonalizado() {
        try {
        	FiltroPersonalizadoVO filtro1 = (FiltroPersonalizadoVO) context().getExternalContext().getRequestMap().get("filtroPersonalizadoItem");
            if (getLayoutRelatorioSEIDecidirVO().getListaFiltroPersonalizadoVOs().size() >= filtro1.getOrdem()) {
            	FiltroPersonalizadoVO filtro2 = getLayoutRelatorioSEIDecidirVO().getListaFiltroPersonalizadoVOs().get(filtro1.getOrdem());
            	getFacadeFactory().getLayoutRelatorioSEIDecidirInterfaceFacade().alterarOrdemFiltroPersonalizado(getLayoutRelatorioSEIDecidirVO(), filtro1, filtro2);
            }
            limparMensagem();
        } catch (Exception e) {
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
        }
    }
	
	
	public void subirFiltroOpcao() {
        try {
        	FiltroPersonalizadoOpcaoVO opc1 = (FiltroPersonalizadoOpcaoVO) context().getExternalContext().getRequestMap().get("filtroOpcaoItem");
            if (opc1.getOrdem() > 1) {
            	FiltroPersonalizadoOpcaoVO opc2 = getFiltroPersonalizadoVO().getListaFiltroPersonalizadoOpcaoVOs().get(opc1.getOrdem() - 2);
                getFacadeFactory().getFiltroPersonalizadoFacade().alterarOrdemFiltroOpcao(getFiltroPersonalizadoVO(), opc1, opc2);
            }
            limparMensagem();

        } catch (Exception e) {
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
        }
    }

    public void descerFiltroOpcao() {
        try {
        	FiltroPersonalizadoOpcaoVO opc1 = (FiltroPersonalizadoOpcaoVO) context().getExternalContext().getRequestMap().get("filtroOpcaoItem");
            if (getFiltroPersonalizadoVO().getListaFiltroPersonalizadoOpcaoVOs().size() >= opc1.getOrdem()) {
            	FiltroPersonalizadoOpcaoVO opc2 = getFiltroPersonalizadoVO().getListaFiltroPersonalizadoOpcaoVOs().get(opc1.getOrdem());
            	getFacadeFactory().getFiltroPersonalizadoFacade().alterarOrdemFiltroOpcao(getFiltroPersonalizadoVO(), opc1, opc2);
            }
            limparMensagem();
        } catch (Exception e) {
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
        }
    }

    
    public List<SelectItem> getListaSelectItemColunaVOs() {
    	List<SelectItem> itens = new ArrayList<SelectItem>(0);
    	itens.add(new SelectItem(1,  "1"));
    	itens.add(new SelectItem(2,  "2"));
    	itens.add(new SelectItem(3,  "3"));
    	itens.add(new SelectItem(4,  "4"));
    	itens.add(new SelectItem(5,  "5"));
    	itens.add(new SelectItem(6,  "6"));
    	itens.add(new SelectItem(7,  "7"));
    	itens.add(new SelectItem(8,  "8"));
    	itens.add(new SelectItem(9,  "9"));
    	itens.add(new SelectItem(10, "10"));
    	itens.add(new SelectItem(11, "11"));
    	itens.add(new SelectItem(12, "12"));
    	return itens;
    }
    
    public void limparListaFiltroOpcao() {
    	getFiltroPersonalizadoVO().getListaFiltroPersonalizadoOpcaoVOs().clear();
    }

    
	
}
