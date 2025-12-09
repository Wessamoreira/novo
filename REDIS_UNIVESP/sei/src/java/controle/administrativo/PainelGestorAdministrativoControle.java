/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.administrativo;

/**
 *
 * @author Carlos
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso; 
@Controller("PainelGestorAdministrativoControle")
@Scope("viewScope")
@Lazy
public class PainelGestorAdministrativoControle extends SuperControle implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7430873590777736904L;
	private List<ComunicacaoInternaVO> ListaComunicacaoInternaNaoLidas;
    private Integer totalListaComunicacaoInternaNaoLidas;


    public PainelGestorAdministrativoControle() {
        super();
     }

    @PostConstruct
    public void consultarComunicacaoInternaNaoLidas() {
        try {
        	DataModelo dataModelo=  new DataModelo();
        	dataModelo.setLimitePorPagina(10);
        	dataModelo.setPaginaAtual(1);
        	getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaPorEntradaLimite(dataModelo, getRealizarValidacaoParaObterQualSeraUsuarioCorrente(getUsuarioLogado()).getPessoa().getCodigo(), getUsuarioLogado().getTipoUsuario(), 999, 0, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), "", "", "", true);
        	setTotalListaComunicacaoInternaNaoLidas(dataModelo.getTotalRegistrosEncontrados());
			setListaComunicacaoInternaNaoLidas((List<ComunicacaoInternaVO>)dataModelo.getListaConsulta());
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        } finally {
            if (getListaComunicacaoInternaNaoLidas().isEmpty()) {
                getListaComunicacaoInternaNaoLidas().clear();
                setListaComunicacaoInternaNaoLidas(null);
            }
        }
    }

    public void editarComunicacaoInternaParaTelaConsulta() {
        try {
        	removerControleMemoriaFlash(ComunicacaoInternaControle.class.getSimpleName());
            ComunicacaoInternaVO obj = (ComunicacaoInternaVO) context().getExternalContext().getRequestMap().get("comunicacaoInternaItem");
            obj.setNovoObj(Boolean.FALSE);
            executarMetodoControle(ComunicacaoInternaControle.class.getSimpleName(), "editarComunicacaoInternaVindoOutraTela", obj);            
//            ComunicacaoInternaControle comunicacaoInternaControle = (ComunicacaoInternaControle) context().getExternalContext().getSessionMap().get("ComunicacaoInternaControle");
//            if (comunicacaoInternaControle == null) {
//                comunicacaoInternaControle = new ComunicacaoInternaControle();
//                comunicacaoInternaControle.setComunicacaoInternaVO(obj);
//                context().getExternalContext().getSessionMap().put("ComunicacaoInternaControle", comunicacaoInternaControle);
//            } else {
//                comunicacaoInternaControle.setComunicacaoInternaVO(obj);
//                context().getExternalContext().getSessionMap().put("ComunicacaoInternaControle", comunicacaoInternaControle);
//            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void abrirComunicacaoInternaParaTelaConsulta() {
        try {
        	removerControleMemoriaFlash(ComunicacaoInternaControle.class.getSimpleName());
            //ComunicacaoInternaVO obj = (ComunicacaoInternaVO) context().getExternalContext().getRequestMap().get("comunicacao");
            //getFacadeFactory().getComunicacaoInternaFacade().carregarDados(obj, getUsuarioLogado());
            //obj.setNovoObj(Boolean.FALSE);
            //executarMetodoControle(ComunicacaoInternaControle.class.getSimpleName(), "consultarTodasEntradaTotal");
//            ComunicacaoInternaControle comunicacaoInternaControle = (ComunicacaoInternaControle) context().getExternalContext().getSessionMap().get("ComunicacaoInternaControle");
//            if (comunicacaoInternaControle == null) {
//                comunicacaoInternaControle = new ComunicacaoInternaControle();
//                comunicacaoInternaControle.setComunicacaoInternaVO(obj);
//                context().getExternalContext().getSessionMap().put("ComunicacaoInternaControle", comunicacaoInternaControle);
//            } else {
//                comunicacaoInternaControle.setComunicacaoInternaVO(obj);
//                context().getExternalContext().getSessionMap().put("ComunicacaoInternaControle", comunicacaoInternaControle);
//            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public Boolean getIsApresentarMensagemNaoPossuiComunicadoInterno() {
        if (getListaComunicacaoInternaNaoLidas().isEmpty()) {
            return true;
        }
        return false;
    }


    public boolean getPerfilAcessoPainelComunicacaoInterna() {
        try {
            ControleAcesso.consultar("PainelGestorComunicacaoInterna", true, getUsuarioLogado());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @return the ListaComunicacaoInternaNaoLidas
     */
    public List<ComunicacaoInternaVO> getListaComunicacaoInternaNaoLidas() {
        if (ListaComunicacaoInternaNaoLidas == null) {
            ListaComunicacaoInternaNaoLidas = new ArrayList<ComunicacaoInternaVO>(0);
        }
        return ListaComunicacaoInternaNaoLidas;
    }

    /**
     * @param ListaComunicacaoInternaNaoLidas the ListaComunicacaoInternaNaoLidas to set
     */
    public void setListaComunicacaoInternaNaoLidas(List<ComunicacaoInternaVO> ListaComunicacaoInternaNaoLidas) {
        this.ListaComunicacaoInternaNaoLidas = ListaComunicacaoInternaNaoLidas;
    }

    /**
     * @return the totalListaComunicacaoInternaNaoLidas
     */
    public Integer getTotalListaComunicacaoInternaNaoLidas() {
        if (totalListaComunicacaoInternaNaoLidas == null) {
            totalListaComunicacaoInternaNaoLidas = 0;
        }
        return totalListaComunicacaoInternaNaoLidas;
    }

    /**
     * @param totalListaComunicacaoInternaNaoLidas the totalListaComunicacaoInternaNaoLidas to set
     */
    public void setTotalListaComunicacaoInternaNaoLidas(Integer totalListaComunicacaoInternaNaoLidas) {
        this.totalListaComunicacaoInternaNaoLidas = totalListaComunicacaoInternaNaoLidas;
    }
}
