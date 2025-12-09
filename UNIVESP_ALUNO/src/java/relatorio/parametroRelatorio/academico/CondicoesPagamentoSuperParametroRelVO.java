/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.parametroRelatorio.academico;

import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;

/**
 *
 * @author Carlos
 */
public class CondicoesPagamentoSuperParametroRelVO extends SuperParametroRelVO {

    public CondicoesPagamentoSuperParametroRelVO(){
        inicializarDados();
    }

    public void inicializarDados(){
        setQtdeMatriculados(0);
        setQtdeNaoAtivos(0);
        setQtdeBolsas(0);
        setQtdePagantes(0);
        
    }
    public Integer getQtdeMatriculados() {
        if (getParametros().get("qtdeMatriculados") == null) {
            setQtdeMatriculados(0);
        }
        return (Integer) getParametros().get("qtdeMatriculados");
    }

    public void setQtdeMatriculados(Integer qtdeMatriculados) {
        getParametros().put("qtdeMatriculados", qtdeMatriculados);
    }

    public Integer getQtdeNaoAtivos() {
        if (getParametros().get("qtdeNaoAtivos") == null) {
            setQtdeNaoAtivos(0);
        }
        return (Integer) getParametros().get("qtdeNaoAtivos");
    }

    public void setQtdeNaoAtivos(Integer qtdeNaoAtivos) {
        getParametros().put("qtdeNaoAtivos", qtdeNaoAtivos);
    }

    
    public Integer getQtdeBolsas() {
        if (getParametros().get("qtdeBolsas") == null) {
            setQtdeBolsas(0);
        }
        return (Integer) getParametros().get("qtdeBolsas");
    }

    public void setQtdeBolsas(Integer qtdeBolsas) {
        getParametros().put("qtdeBolsas", qtdeBolsas);
    }

    public Integer getQtdePagantes() {
        if (getParametros().get("qtdePagantes") == null) {
            setQtdePagantes(0);
        }
        return (Integer) getParametros().get("qtdePagantes");
    }

    public void setQtdePagantes(Integer qtdePagantes) {
        getParametros().put("qtdePagantes", qtdePagantes);
    }

    
}
