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
public class PerfilTurmaSuperParametroRelVO extends SuperParametroRelVO {

    public PerfilTurmaSuperParametroRelVO(){
        inicializarDados();
    }

    public void inicializarDados(){
        setQtdeFundamental(0);
        setQtdeMedio(0);
        setQtdeTecnico(0);
        setQtdeGraduacao(0);
        setQtdeEspecializacao(0);
        setQtdeMestrado(0);
        setQtdeDoutorado(0);
        setQtdePosDoutorado(0);
    }
    public Integer getQtdeFundamental() {
        if (getParametros().get("qtdeFundamental") == null) {
            setQtdeFundamental(0);
        }
        return (Integer) getParametros().get("qtdeFundamental");
    }

    public void setQtdeFundamental(Integer qtdeFundamental) {
        getParametros().put("qtdeFundamental", qtdeFundamental);
    }

    public Integer getQtdeMedio() {
        if (getParametros().get("qtdeMedio") == null) {
            setQtdeMedio(0);
        }
        return (Integer) getParametros().get("qtdeMedio");
    }

    public void setQtdeMedio(Integer qtdeMedio) {
        getParametros().put("qtdeMedio", qtdeMedio);
    }

    public Integer getQtdeTecnico() {
        if (getParametros().get("qtdeTecnico") == null) {
            setQtdeTecnico(0);
        }
        return (Integer) getParametros().get("qtdeTecnico");
    }

    public void setQtdeTecnico(Integer qtdeTecnico) {
        getParametros().put("qtdeTecnico", qtdeTecnico);
    }

    public Integer getQtdeGraduacao() {
        if (getParametros().get("qtdeGraduacao") == null) {
            setQtdeGraduacao(0);
        }
        return (Integer) getParametros().get("qtdeGraduacao");
    }

    public void setQtdeGraduacao(Integer qtdeGraduacao) {
        getParametros().put("qtdeGraduacao", qtdeGraduacao);
    }

    public Integer getQtdeEspecializacao() {
        if (getParametros().get("qtdeEspecializacao") == null) {
            setQtdeEspecializacao(0);
        }
        return (Integer) getParametros().get("qtdeEspecializacao");
    }

    public void setQtdeEspecializacao(Integer qtdeEspecializacao) {
        getParametros().put("qtdeEspecializacao", qtdeEspecializacao);
    }

    public Integer getQtdeMestrado() {
        if (getParametros().get("qtdeMestrado") == null) {
            setQtdeMestrado(0);
        }
        return (Integer) getParametros().get("qtdeMestrado");
    }

    public void setQtdeMestrado(Integer qtdeMestrado) {
        getParametros().put("qtdeMestrado", qtdeMestrado);
    }

    public Integer getQtdeDoutorado() {
        if (getParametros().get("qtdeDoutorado") == null) {
            setQtdeDoutorado(0);
        }
        return (Integer) getParametros().get("qtdeDoutorado");
    }

    public void setQtdeDoutorado(Integer qtdeDoutorado) {
        getParametros().put("qtdeDoutorado", qtdeDoutorado);
    }

    public Integer getQtdePosDoutorado() {
        if (getParametros().get("qtdePosDoutorado") == null) {
            setQtdePosDoutorado(0);
        }
        return (Integer) getParametros().get("qtdePosDoutorado");
    }

    public void setQtdePosDoutorado(Integer qtdePosDoutorado) {
        getParametros().put("qtdePosDoutorado", qtdePosDoutorado);
    }

    public Integer getMediaIdade() {
        if (getParametros().get("mediaIdade") == null) {
            setMediaIdade(0);
        }
        return (Integer) getParametros().get("mediaIdade");
    }

    public void setMediaIdade(Integer mediaIdade) {
        getParametros().put("mediaIdade", mediaIdade);
    }

    public Integer getSomaIdade() {
        if (getParametros().get("somaIdade") == null) {
            setSomaIdade(0);
        }
        return (Integer) getParametros().get("somaIdade");
    }

    public void setSomaIdade(Integer somaIdade) {
        getParametros().put("somaIdade", somaIdade);
    }

    public Double getQtdeMasculino() {
        if (getParametros().get("qtdeMasculino") == null) {
            setQtdeMasculino(0.0);
        }
        return (Double) getParametros().get("qtdeMasculino");
    }

    public void setQtdeMasculino(Double qtdeMasculino) {
        getParametros().put("qtdeMasculino", qtdeMasculino);
    }

    public Double getQtdeFeminino() {
        if (getParametros().get("qtdeFeminino") == null) {
            setQtdeFeminino(0.0);
        }
        return (Double) getParametros().get("qtdeFeminino");
    }

    public void setQtdeFeminino(Double qtdeFeminino) {
        getParametros().put("qtdeFeminino", qtdeFeminino);
    }

    public Double getMediaMasculino() {
        if (getParametros().get("mediaMasculino") == null) {
            setMediaMasculino(0.0);
        }
        return (Double) getParametros().get("mediaMasculino");
    }

    public void setMediaMasculino(Double mediaMasculino) {
        getParametros().put("mediaMasculino", mediaMasculino);
    }

    public Double getMediaFeminino() {
        if (getParametros().get("mediaFeminino") == null) {
            setMediaFeminino(0.0);
        }
        return (Double) getParametros().get("mediaFeminino");
    }

    public void setMediaFeminino(Double mediaFeminino) {
        getParametros().put("mediaFeminino", mediaFeminino);
    }
}
