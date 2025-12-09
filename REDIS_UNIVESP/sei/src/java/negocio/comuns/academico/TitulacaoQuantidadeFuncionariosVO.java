/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;

/**
 *
 * @author Philippe
 */
public class TitulacaoQuantidadeFuncionariosVO extends SuperVO {

    private Integer codigo;
    private String titulacao;
    private Integer quantidadeFuncionarios;
    private TitulacaoProfessorCursoVO titulacaoProfessorCurso;

    public String getTitulacao() {
        if (titulacao == null) {
            titulacao = "";
        }
        return titulacao;
    }

    public void setTitulacao(String titulacao) {
        this.titulacao = titulacao;
    }

    public Integer getQuantidadeFuncionarios() {
        if (quantidadeFuncionarios == null) {
            quantidadeFuncionarios = 0;
        }
        return quantidadeFuncionarios;
    }

    public void setQuantidadeFuncionarios(Integer quantidadeFuncionarios) {
        this.quantidadeFuncionarios = quantidadeFuncionarios;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public TitulacaoProfessorCursoVO getTitulacaoProfessorCurso() {
        if (titulacaoProfessorCurso == null) {
            titulacaoProfessorCurso = new TitulacaoProfessorCursoVO();
        }
        return titulacaoProfessorCurso;
    }

    public void setTitulacaoProfessorCurso(TitulacaoProfessorCursoVO titulacaoProfessorCurso) {
        this.titulacaoProfessorCurso = titulacaoProfessorCurso;
    }

    public String getTitulacao_Apresentar() {
        if (getTitulacao().equals("tecnico")) {
            return "Técnico";
        }
        if (getTitulacao().equals("graduacao")) {
            return "Graduação";
        }
        if (getTitulacao().equals("especializacao")) {
            return "Especialização";
        }
        if (getTitulacao().equals("mestrado")) {
            return "Mestrado";
        }
        if (getTitulacao().equals("doutorado")) {
            return "Doutorado";
        }
        return "Pós-Doutorado";
    }
}
