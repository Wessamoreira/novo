package controle.administrativo;

import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.RegistroLdapVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Controller("RegistroLdapControle")
@Scope("viewScope")
public class RegistroLdapControle extends SuperControle implements Serializable {

	public RegistroLdapControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name(), Uteis.ALERTA);
	}

	public List<SelectItem> tipoConsultaCombo;
	public List<SelectItem> getTipoConsultaCombo() {
		if(tipoConsultaCombo == null) {
		tipoConsultaCombo = new ArrayList<>(0);
		tipoConsultaCombo.add(new SelectItem("codigo", "Código"));
		tipoConsultaCombo.add(new SelectItem("username", "Username"));
		tipoConsultaCombo.add(new SelectItem("nome", "Nome"));
		tipoConsultaCombo.add(new SelectItem("matricula", "Matrícula"));
		}
		return tipoConsultaCombo;
	}

	public void consultar(Integer pagina) {
		try {
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setPage(pagina);
			getControleConsultaOtimizado().setPaginaAtual(pagina);
			getFacadeFactory().getLdapFacade().consultar(getControleConsultaOtimizado(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {		
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}		
	}
	
	public void scrollListener(DataScrollEvent DataScrollEvent) {
		consultar(DataScrollEvent.getPage());		
	}

	public String regerar() {
		try {
			RegistroLdapVO registroLdapVO = (RegistroLdapVO) context().getExternalContext().getRequestMap().get("registroLdapItem");
			getFacadeFactory().getLdapFacade().reexecutarSincronismoComLdap(registroLdapVO, getUsuarioLogado());
			consultar(1);
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return "";
	}

	public void irPaginaInicial() throws Exception {
		getControleConsulta().setPaginaAtual(1);
		this.consultar();
	}

	public void irPaginaAnterior() throws Exception {
		getControleConsulta().setPaginaAtual(getControleConsulta().getPaginaAtual() - 1);
		this.consultar();
	}

	public void irPaginaPosterior() throws Exception {
		getControleConsulta().setPaginaAtual(getControleConsulta().getPaginaAtual() + 1);
		this.consultar();
	}

	public void irPaginaFinal() throws Exception {
		getControleConsulta().setPaginaAtual(getControleConsulta().getNrTotalPaginas());
		this.consultar();
	}

}

