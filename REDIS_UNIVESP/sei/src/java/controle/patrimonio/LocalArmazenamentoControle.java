package controle.patrimonio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoConsultaLocalArmazenamentoEnum;
import negocio.comuns.patrimonio.LocalArmazenamentoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;

@Controller("LocalArmazenamentoControle")
@Scope("viewScope")
@Lazy

public class LocalArmazenamentoControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4777110413096605815L;
	private LocalArmazenamentoVO localArmazenamentoVO;
	private DataModelo controleConsultaLocalArmazenamento;
	private List<SelectItem> listaSelectItemConsultarPor;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemLocalSuperiorConsultarPor;
	private TipoConsultaLocalArmazenamentoEnum consultarPor;

	public void persistir(){
		try{
			getFacadeFactory().getLocalArmazenamentoFacade().persistir(getLocalArmazenamentoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void excluir(){
		try{
			getFacadeFactory().getLocalArmazenamentoFacade().excluir(getLocalArmazenamentoVO(), true, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public String novo(){
		try{
			setLocalArmazenamentoVO(null);
			montarListaSelectItemUnidadeEnsino();
			setMensagemID("msg_entre_dados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("localArmazenamentoForm");
	}
	
	public String editar(){
		try{
			setLocalArmazenamentoVO((LocalArmazenamentoVO)getRequestMap().get("localArmazenamentoLista"));
			montarListaSelectItemUnidadeEnsino();
			setMensagemID("msg_entre_dados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("localArmazenamentoForm");
	}
	
	public void consultarDados(){
		try{
		    	getControleConsultaOtimizado().setLimitePorPagina(10);			
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getLocalArmazenamentoFacade().consultar(getConsultarPor(), getControleConsultaOtimizado().getValorConsulta(), true, getUsuarioLogado(), getUnidadeEnsinoLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getLocalArmazenamentoFacade().consultarTotalRegistro(getConsultarPor(), getControleConsultaOtimizado().getValorConsulta(), getUnidadeEnsinoLogado(), false));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
	}
	
	public void consultarDadosPaginador(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());        
        consultarDados();		
	}
	
	public void consultarLocalSuperior(){
		try{
		    	getControleConsultaLocalArmazenamento().setLimitePorPagina(5);			
			getControleConsultaLocalArmazenamento().setListaConsulta(getFacadeFactory().getLocalArmazenamentoFacade().consultar(getConsultarPor(),  getControleConsultaLocalArmazenamento().getValorConsulta(), true, getUsuarioLogado(), getLocalArmazenamentoVO().getUnidadeEnsinoVO(), getControleConsultaLocalArmazenamento().getLimitePorPagina(), getControleConsultaLocalArmazenamento().getOffset(), false));
			getControleConsultaLocalArmazenamento().setTotalRegistrosEncontrados(getFacadeFactory().getLocalArmazenamentoFacade().consultarTotalRegistro(getConsultarPor(),  getControleConsultaLocalArmazenamento().getValorConsulta(), getLocalArmazenamentoVO().getUnidadeEnsinoVO(), false));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
	}
	
	public void consultarDadosPaginadorLocalSuperior(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaLocalArmazenamento().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaLocalArmazenamento().setPage(DataScrollEvent.getPage());        
		consultarLocalSuperior();		
	}
	
	public void selecionarLocalSuperior(){
		try{
			 LocalArmazenamentoVO obj = (LocalArmazenamentoVO)getRequestMap().get("localArmazenamentoLista");
			 getFacadeFactory().getLocalArmazenamentoFacade().validarDadosLocalSuperior(getLocalArmazenamentoVO(), obj);
			 getLocalArmazenamentoVO().setLocalArmazenamentoSuperiorVO(obj);
			 getLocalArmazenamentoVO().getUnidadeEnsinoVO().setCodigo(obj.getUnidadeEnsinoVO().getCodigo());
			 getLocalArmazenamentoVO().getUnidadeEnsinoVO().setNome(obj.getUnidadeEnsinoVO().getNome());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}	
	}
	
	public void inicializarListaConsultaLocalSuperior(){
		setControleConsultaLocalArmazenamento(null);
	}
	
	public void limparLocalSuperior(){
		getLocalArmazenamentoVO().setLocalArmazenamentoSuperiorVO(null);
	}
	
	public String irPaginaConsulta(){
		setControleConsultaOtimizado(null);
		return Uteis.getCaminhoRedirecionamentoNavegacao("localArmazenamentoCons");
	}
	
	public LocalArmazenamentoVO getLocalArmazenamentoVO() {
		if (localArmazenamentoVO == null) {
			localArmazenamentoVO = new LocalArmazenamentoVO();
		}
		return localArmazenamentoVO;
	}

	public void setLocalArmazenamentoVO(LocalArmazenamentoVO localArmazenamentoVO) {
		this.localArmazenamentoVO = localArmazenamentoVO;
	}

	public DataModelo getControleConsultaLocalArmazenamento() {
		if (controleConsultaLocalArmazenamento == null) {
			controleConsultaLocalArmazenamento = new DataModelo();
		}
		return controleConsultaLocalArmazenamento;
	}

	public void setControleConsultaLocalArmazenamento(DataModelo controleConsultaLocalArmazenamento) {
		this.controleConsultaLocalArmazenamento = controleConsultaLocalArmazenamento;
	}

	public List<SelectItem> getListaSelectItemConsultarPor() {
		if (listaSelectItemConsultarPor == null) {
			listaSelectItemConsultarPor = new ArrayList<SelectItem>(0);
			listaSelectItemConsultarPor.add(new SelectItem(TipoConsultaLocalArmazenamentoEnum.LOCAL, TipoConsultaLocalArmazenamentoEnum.LOCAL.getValorApresentar()));
			listaSelectItemConsultarPor.add(new SelectItem(TipoConsultaLocalArmazenamentoEnum.LOCAL_SUPERIOR, TipoConsultaLocalArmazenamentoEnum.LOCAL_SUPERIOR.getValorApresentar()));
			if(!Uteis.isAtributoPreenchido(getUsuarioLogado().getUnidadeEnsinoLogado())){
				listaSelectItemConsultarPor.add(new SelectItem(TipoConsultaLocalArmazenamentoEnum.UNIDADE_ENSINO, TipoConsultaLocalArmazenamentoEnum.UNIDADE_ENSINO.getValorApresentar()));
			}
		}
		return listaSelectItemConsultarPor;
	}

	public void setListaSelectItemConsultarPor(List<SelectItem> listaSelectItemConsultarPor) {
		this.listaSelectItemConsultarPor = listaSelectItemConsultarPor;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);		
		}
		return listaSelectItemUnidadeEnsino;
	}
	
	public void montarListaSelectItemUnidadeEnsino(){
		try{
			getListaSelectItemUnidadeEnsino().clear();
			if(Uteis.isAtributoPreenchido(getUsuarioLogado().getUnidadeEnsinoLogado())){
				getListaSelectItemUnidadeEnsino().add(new SelectItem(getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado().getUnidadeEnsinoLogado().getNome()));
			}else{
				List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarTodasUnidades(false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(unidadeEnsinoVOs, "codigo", "nome", false));
			}
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<SelectItem> getListaSelectItemLocalSuperiorConsultarPor() {
		if (listaSelectItemLocalSuperiorConsultarPor == null) {
			listaSelectItemLocalSuperiorConsultarPor = new ArrayList<SelectItem>(0);
			listaSelectItemLocalSuperiorConsultarPor.add(new SelectItem(TipoConsultaLocalArmazenamentoEnum.LOCAL, TipoConsultaLocalArmazenamentoEnum.LOCAL.getValorApresentar()));
			listaSelectItemLocalSuperiorConsultarPor.add(new SelectItem(TipoConsultaLocalArmazenamentoEnum.LOCAL_SUPERIOR, TipoConsultaLocalArmazenamentoEnum.LOCAL_SUPERIOR.getValorApresentar()));
		}
		return listaSelectItemLocalSuperiorConsultarPor;
	}

	public void setListaSelectItemLocalSuperiorConsultarPor(List<SelectItem> listaSelectItemLocalSuperiorConsultarPor) {
		this.listaSelectItemLocalSuperiorConsultarPor = listaSelectItemLocalSuperiorConsultarPor;
	}

	public TipoConsultaLocalArmazenamentoEnum getConsultarPor() {
		if (consultarPor == null) {
			consultarPor = TipoConsultaLocalArmazenamentoEnum.LOCAL;
		}
		return consultarPor;
	}

	public void setConsultarPor(TipoConsultaLocalArmazenamentoEnum consultarPor) {
		this.consultarPor = consultarPor;
	}
	
	public void consultarArvoreLocalArmazenamentoSuperiorCons(){
		editar();
		consultarArvoreLocalArmazenamentoSuperior();
	}		
	
	public void consultarArvoreLocalArmazenamentoInferiorCons(){
		editar();
		consultarArvoreLocalArmazenamentoSuperior();
	}	
	
	public void consultarArvoreLocalArmazenamentoSuperior(){		
		try {
			getLocalArmazenamentoVO().setArvoreLocalArmazenamento(getFacadeFactory().getLocalArmazenamentoFacade().consultarArvoreLocalArmazenamentoSuperior(getLocalArmazenamentoVO(), false, getUsuarioLogado()));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarArvoreLocalArmazenamentoInferior(){
		try {
			getLocalArmazenamentoVO().setArvoreLocalArmazenamento(getFacadeFactory().getLocalArmazenamentoFacade().consultarArvoreLocalArmazenamentoInferior(getLocalArmazenamentoVO(), false, getUsuarioLogado()));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

}
