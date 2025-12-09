package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas parceiroForm.jsp
 * parceiroCons.jsp) com as funcionalidades da classe <code>Parceiro</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see Parceiro
 * @see ParceiroVO
 */
import java.io.Serializable;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import controle.bancocurriculum.VagasControle;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("VisaoParceiroControle")
@Scope("session")
@Lazy
public class VisaoParceiroControle extends SuperControle implements Serializable {

    private Boolean possuiVagaAtivaCadastrada;

    public VisaoParceiroControle() throws Exception {
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    public String consultarVagas() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "VisaoParceiroControle", "Inicializando Consultar Vagas", "Consultando");
        } catch (Exception e) {
            setMensagemID("msg_erro", e.getMessage());
        } finally {
            return Uteis.getCaminhoRedirecionamentoNavegacao("vagasVisaoParceiroCons.xhtml");
        }
    }

    public String buscaCandidatoVaga() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "VisaoParceiroControle", "Inicializando Consultar Vagas", "Consultando");
            setPossuiVagaAtivaCadastrada(getFacadeFactory().getParceiroFacade().consultarExisteVagaAtivaCadastrada(getUsuarioLogado().getParceiro().getCodigo(), false, getUsuarioLogado()));
        } catch (Exception e) {
            setMensagemID("msg_erro", e.getMessage());
        } finally {
            if (getPossuiVagaAtivaCadastrada()) {
                return Uteis.getCaminhoRedirecionamentoNavegacao("buscaCandidatoVagaCons.xhtml");
            } else {
                return Uteis.getCaminhoRedirecionamentoNavegacao("naoPossuiVagaAtivaCons.xhtml");
            }
        }
    }

    public String novaVaga() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "VisaoParceiroControle", "Inicializando Consultar Vagas", "Consultando");
            //setPossuiVagaAtivaCadastrada(getFacadeFactory().getParceiroFacade().consultarExisteVagaAtivaCadastrada(getUsuarioLogado().getParceiro().getCodigo(), false, getUsuarioLogado()));
            VagasControle vc = (VagasControle) getControlador("VagasControle");
            vc.novoVisaoParceiro();
        } catch (Exception e) {
            setMensagemID("msg_erro", e.getMessage());
        } finally {
        	return Uteis.getCaminhoRedirecionamentoNavegacao("vagasVisaoParceiroCons.xhtml");
        }
    }

    public String buscaCandidatoVagaExpirada() {
        try {
            //setPossuiVagaAtivaCadastrada(getFacadeFactory().getParceiroFacade().consultarExisteVagaAtivaCadastrada(getUsuarioLogado().getParceiro().getCodigo(), false, getUsuarioLogado()));
            VagasControle vc = (VagasControle) getControlador("VagasControle");
            List objs = getFacadeFactory().getVagasFacade().consultarPorSituacao("EX", "data", getUsuarioLogado().getParceiro().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            vc.setListaConsulta(objs);
            vc.getControleConsulta().setValorConsulta("EX");
        } catch (Exception e) {
            setMensagemID("msg_erro", e.getMessage());
        } finally {
        	return Uteis.getCaminhoRedirecionamentoNavegacao("vagasVisaoParceiroCons.xhtml");
        }
    }

    public String buscaCandidatoVagaCancelada() {
        try {
            //setPossuiVagaAtivaCadastrada(getFacadeFactory().getParceiroFacade().consultarExisteVagaAtivaCadastrada(getUsuarioLogado().getParceiro().getCodigo(), false, getUsuarioLogado()));
            VagasControle vc = (VagasControle) getControlador("VagasControle");
            List objs = getFacadeFactory().getVagasFacade().consultarPorSituacao("EN", "data", getUsuarioLogado().getParceiro().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            vc.setListaConsulta(objs);
            vc.getControleConsulta().setValorConsulta("EN");
        } catch (Exception e) {
            setMensagemID("msg_erro", e.getMessage());
        } finally {
        	return Uteis.getCaminhoRedirecionamentoNavegacao("vagasVisaoParceiroCons.xhtml");
        }
    }

    public String buscaCandidatoVagaAberta() {
        try {
            //setPossuiVagaAtivaCadastrada(getFacadeFactory().getParceiroFacade().consultarExisteVagaAtivaCadastrada(getUsuarioLogado().getParceiro().getCodigo(), false, getUsuarioLogado()));
            VagasControle vc = (VagasControle) getControlador("VagasControle");
            List objs = getFacadeFactory().getVagasFacade().consultarPorSituacao("AT", "data", getUsuarioLogado().getParceiro().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            vc.setListaConsulta(objs);
            vc.getControleConsulta().setValorConsulta("AT");
        } catch (Exception e) {
            setMensagemID("msg_erro", e.getMessage());
        } finally {
        	return Uteis.getCaminhoRedirecionamentoNavegacao("vagasVisaoParceiroCons.xhtml");
        }
    }

    public Boolean getPossuiVagaAtivaCadastrada() {
        if (possuiVagaAtivaCadastrada == null) {
            possuiVagaAtivaCadastrada = Boolean.FALSE;
        }
        return possuiVagaAtivaCadastrada;
    }

    public void setPossuiVagaAtivaCadastrada(Boolean possuiVagaAtivaCadastrada) {
        this.possuiVagaAtivaCadastrada = possuiVagaAtivaCadastrada;
    }
}
