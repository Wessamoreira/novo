/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.secretaria;

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.SuperVO;

/**
 *
 * @author Otimize-04
 */
public class AtividadeComplementarMatriculaVO extends SuperVO {

    private Integer codigo;
    private MatriculaVO matriculaVO;
    private Boolean alunoSelecionado;
    private Integer horaComplementar;
    private AtividadeComplementarVO atividadeComplementarVO;
    private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO;

    public MatriculaVO getMatriculaVO() {
        if (matriculaVO == null) {
            matriculaVO = new MatriculaVO();
        }
        return matriculaVO;
    }

    public void setMatriculaVO(MatriculaVO matriculaVO) {
        this.matriculaVO = matriculaVO;
    }

    public Boolean getAlunoSelecionado() {
        if (alunoSelecionado == null) {
            alunoSelecionado = Boolean.FALSE;
        }
        return alunoSelecionado;
    }

    public void setAlunoSelecionado(Boolean alunoSelecionado) {
        this.alunoSelecionado = alunoSelecionado;
    }

    public Integer getHoraComplementar() {
        if (horaComplementar == null) {
            horaComplementar = 0;
        }
        return horaComplementar;
    }

    public void setHoraComplementar(Integer horaComplementar) {
        this.horaComplementar = horaComplementar;
    }

    public AtividadeComplementarVO getAtividadeComplementarVO() {
        if (atividadeComplementarVO == null) {
            atividadeComplementarVO = new AtividadeComplementarVO();
        }
        return atividadeComplementarVO;
    }

    public void setAtividadeComplementarVO(AtividadeComplementarVO atividadeComplementarVO) {
        this.atividadeComplementarVO = atividadeComplementarVO;
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

	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTurmaDisciplinaVO() {
		if (matriculaPeriodoTurmaDisciplinaVO == null) {
			matriculaPeriodoTurmaDisciplinaVO = new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return matriculaPeriodoTurmaDisciplinaVO;
	}

	public void setMatriculaPeriodoTurmaDisciplinaVO(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) {
		this.matriculaPeriodoTurmaDisciplinaVO = matriculaPeriodoTurmaDisciplinaVO;
	}
    
    
}
