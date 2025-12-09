/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.bancocurriculum;

/**
 *
 * @author Philippe
 */
public class AlunosCandidatadosVagaDadosVagaRelVO {

    private String areaProfissional;
    private String empresa;
    private String cidadeEmpresa;
    private String cargo;
    private String situacao;

    public String getAreaProfissional() {
        if (areaProfissional == null) {
            areaProfissional = "";
        }
        return areaProfissional;
    }

    public void setAreaProfissional(String areaProfissional) {
        this.areaProfissional = areaProfissional;
    }

    public String getEmpresa() {
        if (empresa == null) {
            empresa = "";
        }
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getCidadeEmpresa() {
        if (cidadeEmpresa == null) {
            cidadeEmpresa = "";
        }
        return cidadeEmpresa;
    }

    public void setCidadeEmpresa(String cidadeEmpresa) {
        this.cidadeEmpresa = cidadeEmpresa;
    }

    public String getCargo() {
        if (cargo == null) {
            cargo = "";
        }
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getSituacao() {
        if (situacao == null || situacao.equals("NENHUM")) {
            situacao = "Indefinida";
        } else if (situacao.equals("PROCESSO_SELETIVO")) {
            return "Processo Seletivo";
        } else if (situacao.equals("SELECIONADO")) {
            return "Selecionado";
        } else if (situacao.equals("DESQUALIFICADO")) {
            return "Desclassificado";
        }
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}
