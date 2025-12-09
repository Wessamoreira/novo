/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.processosel;

import negocio.comuns.basico.PessoaVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoVO;

/**
 *
 * @author Paulo Victor
 */
public class ProcSeletivoAprovadoReprovadoRelVO {

    private PessoaVO pessoa;
    private ResultadoProcessoSeletivoVO resultadoProcessoSeletivo;
    private String nomeCurso1;
    private String nomeCurso2;
    private String nomeCurso3;
    private InscricaoVO inscricao;
    private String nomeCursoFiltro;
    private String turno;

    public ProcSeletivoAprovadoReprovadoRelVO() {
    }

    public InscricaoVO getInscricao() {
        if (inscricao == null) {
            inscricao = new InscricaoVO();
        }
        return inscricao;
    }

    public void setInscricao(InscricaoVO inscricao) {
        this.inscricao = inscricao;
    }

    public String getNomeCurso1() {
        return nomeCurso1;
    }

    public void setNomeCurso1(String nomeCurso1) {
        this.nomeCurso1 = nomeCurso1;
    }

    public String getNomeCurso2() {
        return nomeCurso2;
    }

    public void setNomeCurso2(String nomeCurso2) {
        this.nomeCurso2 = nomeCurso2;
    }

    public String getNomeCurso3() {
        return nomeCurso3;
    }

    public void setNomeCurso3(String nomeCurso3) {
        this.nomeCurso3 = nomeCurso3;
    }

    public PessoaVO getPessoa() {
        if (pessoa == null) {
            pessoa = new PessoaVO();
        }
        return pessoa;
    }

    public void setPessoa(PessoaVO pessoa) {
        this.pessoa = pessoa;
    }

    public ResultadoProcessoSeletivoVO getResultadoProcessoSeletivo() {
        if (resultadoProcessoSeletivo == null) {
            resultadoProcessoSeletivo = new ResultadoProcessoSeletivoVO();
        }
        return resultadoProcessoSeletivo;
    }

    public void setResultadoProcessoSeletivo(ResultadoProcessoSeletivoVO resultadoProcessoSeletivo) {
        this.resultadoProcessoSeletivo = resultadoProcessoSeletivo;
    }

    public String getNomeCursoFiltro() {
        if (nomeCursoFiltro == null) {
            nomeCursoFiltro = "";
        }
        return nomeCursoFiltro;
    }

    public void setNomeCursoFiltro(String nomeCursoFiltro) {
        this.nomeCursoFiltro = nomeCursoFiltro;
    }

    public String getTurno() {
        if (turno == null) {
            turno = "";
        }
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }
}
