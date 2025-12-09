/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.processosel;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 *
 * @author Rodrigo Araújo
 */
public class ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO {

    protected Integer unidadeEnsinoId;
    protected String unidadeEnsino;
    protected String curso;
    protected String turno;
    protected Integer unidadeEnsinoCursoId;
    protected List<ProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO> processoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO;
    private Integer qtdeInscritos;
    private Integer qtdeVagas;
    private Integer qtdeConfirmados;
    private Double qtdeCandPorVaga;
    private Integer qtdeInscNaoConfirmados;
    private Integer qtdeInscNaoCompareceu;
    private Integer qtdeInscCancelada;
    private Integer qtdeInscCanceladaOutraInscricao;

    public ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO() {
        inicializarDados();
    }

    public String getUnidadeEnsinoCursoTurno(){
        return unidadeEnsino+curso+turno;
    }

    public void inicializarDados() {
        unidadeEnsino = "";
        curso = "";
        turno = "";
        processoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO = new ArrayList(0);
    }

    public JRDataSource getListaProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO() {
        JRDataSource jr = new JRBeanArrayDataSource(getProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO().toArray());
        return jr;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public List<ProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO> getProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO() {
        return processoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO;
    }

    public void setProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO(List<ProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO> processoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO) {
        this.processoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO = processoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getUnidadeEnsino() {
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(String unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public Integer getUnidadeEnsinoId() {
        return unidadeEnsinoId;
    }

    public void setUnidadeEnsinoId(Integer unidadeEnsinoId) {
        this.unidadeEnsinoId = unidadeEnsinoId;
    }

    public Integer getUnidadeEnsinoCursoId() {
        return unidadeEnsinoCursoId;
    }

    public void setUnidadeEnsinoCursoId(Integer unidadeEnsinoCursoId) {
        this.unidadeEnsinoCursoId = unidadeEnsinoCursoId;
    }

    /**
     * @return the qtdeInscritos
     */
    public Integer getQtdeInscritos() {
        if(qtdeInscritos == null){
            qtdeInscritos = 0;
        }
        return qtdeInscritos;
    }

    /**
     * @param qtdeInscritos the qtdeInscritos to set
     */
    public void setQtdeInscritos(Integer qtdeInscritos) {
        this.qtdeInscritos = qtdeInscritos;
    }

	public Integer getQtdeVagas() {
		if (qtdeVagas == null) {
			qtdeVagas = 0;
		}
		return qtdeVagas;
	}

	public void setQtdeVagas(Integer qtdeVagas) {
		this.qtdeVagas = qtdeVagas;
	}

	public Integer getQtdeConfirmados() {
		if (qtdeConfirmados == null) {
			qtdeConfirmados = 0;
		}
		return qtdeConfirmados;
	}

	public void setQtdeConfirmados(Integer qtdeConfirmados) {
		this.qtdeConfirmados = qtdeConfirmados;
	}

	public Double getQtdeCandPorVaga() {
		if (qtdeCandPorVaga == null) {
			qtdeCandPorVaga = 0.0;
		}
		return qtdeCandPorVaga;
	}

	public void setQtdeCandPorVaga(Double qtdeCandPorVaga) {
		this.qtdeCandPorVaga = qtdeCandPorVaga;
	}

	public Integer getQtdeInscNaoConfirmados() {
		if (qtdeInscNaoConfirmados == null) {
			qtdeInscNaoConfirmados = 0;
		}
		return qtdeInscNaoConfirmados;
	}

	public void setQtdeInscNaoConfirmados(Integer qtdeInscNaoConfirmados) {
		this.qtdeInscNaoConfirmados = qtdeInscNaoConfirmados;
	}

	public Integer getQtdeInscNaoCompareceu() {
		if (qtdeInscNaoCompareceu == null) {
			qtdeInscNaoCompareceu = 0;
		}
		return qtdeInscNaoCompareceu;
	}

	public void setQtdeInscNaoCompareceu(Integer qtdeInscNaoCompareceu) {
		this.qtdeInscNaoCompareceu = qtdeInscNaoCompareceu;
	}

	public Integer getQtdeInscCancelada() {
		if (qtdeInscCancelada == null) {
			qtdeInscCancelada = 0;
		}
		return qtdeInscCancelada;
	}

	public void setQtdeInscCancelada(Integer qtdeInscCancelada) {
		this.qtdeInscCancelada = qtdeInscCancelada;
	}

	public Integer getQtdeInscCanceladaOutraInscricao() {
		return qtdeInscCanceladaOutraInscricao;
	}

	public void setQtdeInscCanceladaOutraInscricao(Integer qtdeInscCanceladaOutraInscricao) {
		this.qtdeInscCanceladaOutraInscricao = qtdeInscCanceladaOutraInscricao;
	}
	
	
    
}