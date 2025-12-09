/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.processosel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

/**
 * 
 * @author Otimize-TI
 */
public class PerguntaPerfilSocioEconomicoVO extends SuperVO {

    private String titulo;
    private String variavel;
    private int codigo;
    private List listaResposta;
    public static final long serialVersionUID = 1L;

    public PerguntaPerfilSocioEconomicoVO() {
        incializarDados();
    }

    public void validarDadosSelecionado(RespostaPerguntaPerfilSocioEconomicoVO obj) {
        if (obj.getSelecionado()) {
            Iterator i = getListaResposta().iterator();
            while (i.hasNext()) {
                RespostaPerguntaPerfilSocioEconomicoVO objExistente = (RespostaPerguntaPerfilSocioEconomicoVO) i.next();
                if (!objExistente.getKey().equals(obj.getKey())) {
                    objExistente.setSelecionado(Boolean.FALSE);
                }
            }
        }
    }

    public void marcarValoresSelecionadosReposta(PerfilSocioEconomicoVO perfilSocioEconcomico) throws Exception {
        Iterator j = getListaResposta().iterator();

        while (j.hasNext()) {
            try {
                RespostaPerguntaPerfilSocioEconomicoVO resposta = (RespostaPerguntaPerfilSocioEconomicoVO) j.next();

                if (resposta.getSelecionado()) {
                    Class perfil = Class.forName(perfilSocioEconcomico.getClass().getName());
                    Method metodoSet = perfil.getMethod("set" + getVariavel().toString(), String.class);
                    metodoSet.invoke(perfilSocioEconcomico, resposta.getKey().toString());
                    return;
                }

            } catch (Exception e) {
                throw e;
            }
        }

    }

    public void incializarDados() {
        setTitulo("");
        setVariavel("");
        setCodigo(0);
    }

    public String getTitulo() {
        if (titulo == null) {
            titulo = "";
        }
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List getListaResposta() {
        if (listaResposta == null) {
            listaResposta = new ArrayList();
        }
        return listaResposta;
    }

    public void setListaResposta(List listaResposta) {
        this.listaResposta = listaResposta;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getVariavel() {
        if (variavel == null) {
            variavel = "";
        }
        return variavel;
    }

    public void setVariavel(String variavel) {
        this.variavel = variavel;
    }
}
