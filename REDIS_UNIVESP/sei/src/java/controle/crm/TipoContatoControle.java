package controle.crm;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.crm.TipoContatoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

@Controller("TipoContatoControle")
@Scope("viewScope")
@Lazy
public class TipoContatoControle extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5838530453419784644L;
	private TipoContatoVO tipoContatoVO;
	private List<SelectItem> opcaoConsulta;
	
	public String novo(){
		setTipoContatoVO(null);
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
		return Uteis.getCaminhoRedirecionamentoNavegacao("tipoContatoForm.xhtml");
	}
	public String editar(){
		setTipoContatoVO((TipoContatoVO) getRequestMap().get("tipoContatoItens"));
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
		return Uteis.getCaminhoRedirecionamentoNavegacao("tipoContatoForm.xhtml");
	}
	
	public void persistir(){
		try{
			getFacadeFactory().getTipoContatoFacade().persistir(getTipoContatoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro",e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void ativar(){
		try{
			getFacadeFactory().getTipoContatoFacade().ativar(getTipoContatoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro",e.getMessage(), Uteis.ERRO);
		}
	}
	public void inativar(){
		try{
			getFacadeFactory().getTipoContatoFacade().inativar(getTipoContatoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro",e.getMessage(), Uteis.ERRO);
		}
	}
	
	public String irPaginaConsulta(){
		setMensagemID("msg_entre_prmconsulta",Uteis.ALERTA );
		return Uteis.getCaminhoRedirecionamentoNavegacao("tipoContatoCons.xhtml");
	}
	
	public String consulta(){
		try{
			getControleConsulta().setListaConsulta(getFacadeFactory().getTipoContatoFacade().consultar(getControleConsulta().getValorConsulta(), null, true, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		}catch(Exception e){
			getControleConsulta().getListaConsulta().clear();
			setMensagemDetalhada("msg_erro",e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("tipoContatoCons.xhtml");
	}
	
	public List<SelectItem> getOpcaoConsulta() {
		if(opcaoConsulta == null){
			opcaoConsulta = new ArrayList<SelectItem>(0);
			opcaoConsulta.add(new SelectItem("descricao", UteisJSF.internacionalizar("prt_TipoContato_descricao")));
		}
		return opcaoConsulta;
	}
	public void setOpcaoConsulta(List<SelectItem> opcaoConsulta) {
		this.opcaoConsulta = opcaoConsulta;
	}
	public TipoContatoVO getTipoContatoVO() {
		if(tipoContatoVO == null){
			tipoContatoVO = new TipoContatoVO();
		}
		return tipoContatoVO;
	}
	public void setTipoContatoVO(TipoContatoVO tipoContatoVO) {
		this.tipoContatoVO = tipoContatoVO;
	}
	
	

}
