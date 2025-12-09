/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controle.administrativo;

/**
 *
 * @author OTIMIZE 5
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.utilitarias.Uteis; @Controller("MuralControle")
@Scope("request")
@Lazy
public class MuralControle extends SuperControle implements Serializable {
    
    protected List listaSelectItemMuralPrincipal;
    protected List listaSelectItemMuralComunicadoPessoal;
    
    public MuralControle(){
        montarListas();
    }
    
    public void montarListas(){
        montarListaSelectItemMuralPrincipal();
        montarListaSelectItemMensagem();
    }
    
    public List consultarComunicacaoInternaPorTipoComunicadoMural(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getComunicacaoInternaFacade().consultarPorTipoComunicadoInternoMuralDestinatario(nomePrm, getUsuarioLogado().getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }
    
    public List consultarComunicacaoInternaPorTipoComunicado(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getComunicacaoInternaFacade().consultarPorTipoComunicadoInternoNaoLidaRespondidaDestinatario(nomePrm, getUsuarioLogado().getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher
     * o comboBox relativo ao atributo <code>Responsavel</code>.
    */
    public void montarListaSelectItemMuralPrincipal() {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarComunicacaoInternaPorTipoComunicadoMural( "MP" );
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            while (i.hasNext()) {
                objs.add( (ComunicacaoInternaVO)i.next() );
            }
            listaSelectItemMuralPrincipal = objs;
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }
    
    public void montarListaSelectItemMensagem() {
        try {
            List resultadoConsulta = consultarComunicacaoInternaPorTipoComunicado( "RE" );
            Iterator i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            while (i.hasNext()) {
                objs.add( (ComunicacaoInternaVO)i.next() );
            }
            
            List resultadoConsulta2 = consultarComunicacaoInternaPorTipoComunicado( "LE" );
            Iterator i2 = resultadoConsulta2.iterator();
            List objs2 = new ArrayList(0);
            while (i2.hasNext()) {
                objs2.add( (ComunicacaoInternaVO)i2.next() );
            }
            objs.addAll( (Collection) objs2 );
            listaSelectItemMuralComunicadoPessoal = objs;
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }
    

    public List getListaSelectItemMuralPrincipal() {
        return listaSelectItemMuralPrincipal;
    }

    public void setListaSelectItemMuralPrincipal(List listaSelectItemMuralPrincipal) {
        this.listaSelectItemMuralPrincipal = listaSelectItemMuralPrincipal;
    }

    public List getListaSelectItemMuralComunicadoPessoal() {
        return listaSelectItemMuralComunicadoPessoal;
    }

    public void setListaSelectItemMuralComunicadoPessoal(List listaSelectItemMuralComunicadoPessoal) {
        this.listaSelectItemMuralComunicadoPessoal = listaSelectItemMuralComunicadoPessoal;
    }

    public Integer getListaSelectItemMuralComunicadoPessoalTamanho(){
        return new Integer(listaSelectItemMuralComunicadoPessoal.size());
    }

}
