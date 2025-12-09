/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

/**
 *
 * @author Philippe
 */
public class TitulacaoProfessorCursoVO extends SuperVO {

    private Integer codigo;
    private TurmaVO turma;
    private List<TitulacaoQuantidadeFuncionariosVO> listaTitulacaoQuantidadeFuncionarios;
    private Integer quantidadeTecnico;
    private Integer quantidadeGraduacao;
    private Integer quantidadeEspecializacao;
    private Integer quantidadeMestrado;
    private Integer quantidadeDoutorado;
    private Integer quantidadePosDoutorado;

    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return turma;
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
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

    public List<TitulacaoQuantidadeFuncionariosVO> getListaTitulacaoQuantidadeFuncionarios() {
        if (listaTitulacaoQuantidadeFuncionarios == null) {
            listaTitulacaoQuantidadeFuncionarios = new ArrayList(0);
        }
        return listaTitulacaoQuantidadeFuncionarios;
    }

    public void setListaTitulacaoQuantidadeFuncionarios(List<TitulacaoQuantidadeFuncionariosVO> listaTitulacaoQuantidadeFuncionarios) {
        this.listaTitulacaoQuantidadeFuncionarios = listaTitulacaoQuantidadeFuncionarios;
    }

    public Integer getQuantidadeTecnico() {
        if (quantidadeTecnico == null) {
            quantidadeTecnico = 0;
        }
        return quantidadeTecnico;
    }

    public void setQuantidadeTecnico(Integer quantidadeTecnico) {
        this.quantidadeTecnico = quantidadeTecnico;
    }

    public Integer getQuantidadeGraduacao() {
        if (quantidadeGraduacao == null) {
            quantidadeGraduacao = 0;
        }
        return quantidadeGraduacao;
    }

    public void setQuantidadeGraduacao(Integer quantidadeGraduacao) {
        this.quantidadeGraduacao = quantidadeGraduacao;
    }

    public Integer getQuantidadeEspecializacao() {
        if (quantidadeEspecializacao == null) {
            quantidadeEspecializacao = 0;
        }
        return quantidadeEspecializacao;
    }

    public void setQuantidadeEspecializacao(Integer quantidadeEspecializacao) {
        this.quantidadeEspecializacao = quantidadeEspecializacao;
    }

    public Integer getQuantidadeMestrado() {
        if (quantidadeMestrado == null) {
            quantidadeMestrado = 0;
        }
        return quantidadeMestrado;
    }

    public void setQuantidadeMestrado(Integer quantidadeMestrado) {
        this.quantidadeMestrado = quantidadeMestrado;
    }

    public Integer getQuantidadeDoutorado() {
        if (quantidadeDoutorado == null) {
            quantidadeDoutorado = 0;
        }
        return quantidadeDoutorado;
    }

    public void setQuantidadeDoutorado(Integer quantidadeDoutorado) {
        this.quantidadeDoutorado = quantidadeDoutorado;
    }

    public Integer getQuantidadePosDoutorado() {
        if (quantidadePosDoutorado == null) {
            quantidadePosDoutorado = 0;
        }
        return quantidadePosDoutorado;
    }

    public void setQuantidadePosDoutorado(Integer quantidadePosDoutorado) {
        this.quantidadePosDoutorado = quantidadePosDoutorado;
    }
}
