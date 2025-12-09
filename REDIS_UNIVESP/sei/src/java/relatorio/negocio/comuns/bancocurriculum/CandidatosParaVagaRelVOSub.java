/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.bancocurriculum;

import negocio.comuns.bancocurriculum.enumeradores.SituacaoReferenteVagaEnum;

/**
 *
 * @author Philippe
 */
public class CandidatosParaVagaRelVOSub {

    private Integer codigoCandidato;
    private String candidato;
    private String estado;
    private String email;
    private String telefone;
    private String situacaoReferenteVaga;

    public String getSituacaoReferenteVaga() {
        if (situacaoReferenteVaga == null) {
            return "";
        } else if (situacaoReferenteVaga.equals(SituacaoReferenteVagaEnum.PROCESSO_SELETIVO.toString())) {
            return "Processo Seletivo";
        } else if (situacaoReferenteVaga.equals(SituacaoReferenteVagaEnum.SELECIONADO.toString())) {
            return "Selecionado";
        } else if (situacaoReferenteVaga.equals(SituacaoReferenteVagaEnum.DESQUALIFICADO.toString())) {
            return "Desclassificado";
        }
        return "";
    }

    public void setSituacaoReferenteVaga(String situacaoReferenteVaga) {
        this.situacaoReferenteVaga = situacaoReferenteVaga;
    }

    public String getCandidato() {
        if (candidato == null) {
            candidato = "";
        }
        return candidato;
    }

    public void setCandidato(String candidato) {
        this.candidato = candidato;
    }

    public String getEstado() {
        if (estado == null) {
            estado = "";
        }
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEmail() {
        if (email == null) {
            email = "";
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        if (telefone == null) {
            telefone = "";
        }
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Integer getCodigoCandidato() {
        if (codigoCandidato == null) {
            codigoCandidato = 0;
        }
        return codigoCandidato;
    }

    public void setCodigoCandidato(Integer codigoCandidato) {
        this.codigoCandidato = codigoCandidato;
    }
}
