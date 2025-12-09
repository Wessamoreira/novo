package controle.biblioteca;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.ImpressoraVO;
import negocio.comuns.biblioteca.PoolImpressaoVO;
import negocio.comuns.biblioteca.enumeradores.TipoImpressoraEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;

@Controller("ImpressoraControle")
@Lazy
@Scope("viewScope")
public class ImpressoraControle extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4665764810444961560L;
	private ImpressoraVO impressoraVO;
	private List<SelectItem> listaSelectItemBiblioteca;
	private List<SelectItem> listaSelectItemTipoImpressora;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> tipoConsultaCombo;
	
	public String novo(){
		try{
			setImpressoraVO(null);
			montarListaSelectItemBiblioteca();
			return Uteis.getCaminhoRedirecionamentoNavegacao("impressoraForm.xhtml");
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}
	
	public String editar(){
		try{
			setImpressoraVO((ImpressoraVO) getRequestMap().get("impressoraItens"));
			montarListaSelectItemBiblioteca();
			montarListaSelectItemUnidadeEnsino();
			realizarAtualizacaoPool();
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("impressoraForm.xhtml");
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("impresssoraCons.xhtml");
		}
	}
	
	public void persistir(){
		try{
			getFacadeFactory().getImpressoraFacade().persistir(getImpressoraVO(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			
		}
	}
	
	public String excluir(){
		try{
			getFacadeFactory().getImpressoraFacade().excluir(getImpressoraVO(), getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}
	
	public String realizarExclusaoPool(){
		try{
			getFacadeFactory().getPoolImpressaoFacade().excluirPorImpressora(getImpressoraVO(), getUsuarioLogado());
			realizarAtualizacaoPool();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}
	
	public String excluirPool(){
		try{
			getFacadeFactory().getPoolImpressaoFacade().excluir((PoolImpressaoVO) getRequestMap().get("pool"), getUsuarioLogado());
			realizarAtualizacaoPool();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}
	
	public void consultarImpressora(){
		try{
			setListaConsulta(getFacadeFactory().getImpressoraFacade().consultar(getControleConsulta().getCampoConsulta(),getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado(), true, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			
		}
	}
	
	public String irPaginaInicial(){
		getListaConsulta().clear();
		return Uteis.getCaminhoRedirecionamentoNavegacao("impressoraCons.xhtml");
	}
	
	public void realizarAtualizacaoPool(){
		try {
			getImpressoraVO().setPoolImpressaoVOs(getFacadeFactory().getPoolImpressaoFacade().consultarPorImpressora(getImpressoraVO(), false, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);			
		}
	}
	
	public ImpressoraVO getImpressoraVO() {
		if(impressoraVO == null){
			impressoraVO = new ImpressoraVO();
		}
		return impressoraVO;
	}
	
	public void setImpressoraVO(ImpressoraVO impressoraVO) {
		this.impressoraVO = impressoraVO;
	}
	
	public void atualizarListaSelectItemBiblioteca(){
		getListaSelectItemBiblioteca().clear();
		montarListaSelectItemBiblioteca();
	}
	
	public void montarListaSelectItemBiblioteca(){
		try{
			getListaSelectItemBiblioteca().clear();
			List<BibliotecaVO> bibliotecaVOs = null;
			if(getImpressoraVO().getUsarFinanceiro() && Uteis.isAtributoPreenchido(getImpressoraVO().getUnidadeEnsinoVO())){
				bibliotecaVOs = getFacadeFactory().getBibliotecaFacade().consultarPorNome("", getImpressoraVO().getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}else{
				bibliotecaVOs = getFacadeFactory().getBibliotecaFacade().consultarPorNome("", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaSelectItemBiblioteca(UtilSelectItem.getListaSelectItem(bibliotecaVOs, "codigo", "nome", false));
			if(!getListaSelectItemBiblioteca().isEmpty() && !Uteis.isAtributoPreenchido(getImpressoraVO().getBibliotecaVO())){
				getImpressoraVO().getBibliotecaVO().setCodigo((Integer)getListaSelectItemBiblioteca().get(0).getValue());
				montarListaSelectItemUnidadeEnsino();
			}			
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public List<SelectItem> getListaSelectItemBiblioteca() {
		if(listaSelectItemBiblioteca == null){
			listaSelectItemBiblioteca = new ArrayList<SelectItem>(0);			
		}
		return listaSelectItemBiblioteca;
	}
	
	public void setListaSelectItemBiblioteca(List<SelectItem> listaSelectItemBiblioteca) {
		this.listaSelectItemBiblioteca = listaSelectItemBiblioteca;
	}
	
	public List<SelectItem> getListaSelectItemTipoImpressora() {
		if(listaSelectItemTipoImpressora == null){
			listaSelectItemTipoImpressora = UtilSelectItem.getListaSelectItemEnum(TipoImpressoraEnum.values(), Obrigatorio.SIM);
		}
		return listaSelectItemTipoImpressora;
	}
	
	public void setListaSelectItemTipoImpressora(List<SelectItem> listaSelectItemTipoImpressora) {
		this.listaSelectItemTipoImpressora = listaSelectItemTipoImpressora;
	}

	public List<SelectItem> getTipoConsultaCombo() {
		if(tipoConsultaCombo == null){
			tipoConsultaCombo = new ArrayList<SelectItem>(0);
			tipoConsultaCombo.add(new SelectItem("nome", "Nome"));
			tipoConsultaCombo.add(new SelectItem("idIdentificacao", "Id Identificação"));
			tipoConsultaCombo.add(new SelectItem("biblioteca", "Biblioteca"));
		}
		return tipoConsultaCombo;
	}

	public void setTipoConsultaCombo(List<SelectItem> tipoConsultaCombo) {
		this.tipoConsultaCombo = tipoConsultaCombo;
	}
	
	public void realizarImpressaoTeste(){
		try {
			getFacadeFactory().getImpressoraFacade().realizarImpressaoTeste(getImpressoraVO(),  getUsuarioLogado());
			realizarAtualizacaoPool();
			setMensagemID("msg_msg_enviados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if(listaSelectItemUnidadeEnsino == null){
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>();
		}
		return listaSelectItemUnidadeEnsino;
	}
	public void montarListaSelectItemUnidadeEnsino(){		
		getListaSelectItemUnidadeEnsino().clear();
		if (getUnidadeEnsinoLogado().getCodigo() != null && getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
			getListaSelectItemUnidadeEnsino().add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getNome()));				
		} else {
			List<UnidadeEnsinoVO> unidadeEnsinoVOs;
			try {
				if(getImpressoraVO().isNovoObj() && getImpressoraVO().getUsarBiblioteca() && Uteis.isAtributoPreenchido(getImpressoraVO().getBibliotecaVO())){
					unidadeEnsinoVOs  = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUnidadeEnsinoPorBiblioteca(getImpressoraVO().getBibliotecaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				}else{						
					unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				}
				List<SelectItem> lista = UtilSelectItem.getListaSelectItem(unidadeEnsinoVOs, "codigo", "nome");
				setListaSelectItemUnidadeEnsino(lista);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}
	
}
