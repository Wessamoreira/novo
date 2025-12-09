/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.utilitarias.Ordenacao;

/**
 *
 * @author EDIGARANTONIO
 */
public class GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1332138719987300423L;
	private GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO;
    /**
     * Registra todos os históricos do aluno para esta determinada disciplina 
     * (definida na classe mãe). Isto, por que um aluno pode cursar uma mesma
     * disciplina diversas vezes, por exemplo, ele pode ser reprovado em um 
     * periodo, abondonado em outro e aprovado em um terceito período.
     */
    private List<HistoricoVO> historicosAluno;
    /**
     * Registra o histórico atual do aluno para esta determinada disciplina 
     * (definida na classe mãe). Isto, por que um aluno pode cursar uma mesma
     * disciplina diversas vezes, por exemplo, ele pode ser reprovado em um 
     * periodo, abondonado em outro e aprovado em um terceito período.
     * Este atributo irá trazer o último histórico do aluno, o que retrata
     * a realidade atual do aluno, com relação a aprovação ou reprovação.
     */
    private HistoricoVO historicoAtualAluno;
    /**
     * Utilizado na tela de inclusão de disciplina do aluno, criado com a finalidade de realizar o registro de historicos sem informar turma, matricula periodo.
     */
    private List<InclusaoHistoricoAlunoDisciplinaVO> inclusaoHistoricoAlunoDisciplinaVOs;
    

    /**
     * @return the historicoAluno
     */
    public List<HistoricoVO> getHistoricosAluno() {
        if (historicosAluno == null) {
            historicosAluno = new ArrayList(0);
        }
        return historicosAluno;
    }

    /**
     * @param historicoAluno the historicoAluno to set
     */
    public void setHistoricosAluno(List<HistoricoVO> historicosAluno) {
        this.historicosAluno = historicosAluno;
    }

    /**
     * @return the historicoAtualAluno
     */
    public HistoricoVO getHistoricoAtualAluno() {
        if (historicoAtualAluno == null) {
            historicoAtualAluno = new HistoricoVO();
        }
        return historicoAtualAluno;
    }

    /**
     * @param historicoAtualAluno the historicoAtualAluno to set
     */
    public void setHistoricoAtualAluno(HistoricoVO historicoAtualAluno) {
        this.historicoAtualAluno = historicoAtualAluno;
    }

    /**
     * @return the gradeCurricularGrupoOptativaDisciplinaVO
     */
    public GradeCurricularGrupoOptativaDisciplinaVO getGradeCurricularGrupoOptativaDisciplinaVO() {
        if (gradeCurricularGrupoOptativaDisciplinaVO == null) {
            gradeCurricularGrupoOptativaDisciplinaVO = new GradeCurricularGrupoOptativaDisciplinaVO();
        }
        return gradeCurricularGrupoOptativaDisciplinaVO;
    }

    /**
     * @param gradeCurricularGrupoOptativaDisciplinaVO the gradeCurricularGrupoOptativaDisciplinaVO to set
     */
    public void setGradeCurricularGrupoOptativaDisciplinaVO(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO) {
        this.gradeCurricularGrupoOptativaDisciplinaVO = gradeCurricularGrupoOptativaDisciplinaVO;
    }

    public void adicionarHistoricoParaGradeDisciplina(HistoricoVO hist) {
        if (hist.getHistoricoDisciplinaFazParteComposicao()) {
            // nao adiciona-se o historico de uma disciplina que participa de uma
            // composicoa, pois o historico da disciplina mae já é adicionada e
            // utilizada para integralizacao e verificacoes afins...
            return;
        }
        this.getHistoricosAluno().add(hist);
        if (this.getHistoricosAluno().size() == 1) {
            this.setHistoricoAtualAluno(hist);
        }
        Ordenacao.ordenarLista(getHistoricosAluno(), "anoSemestreOrdenacao");
//        if ((hist.getAnoHistorico().compareTo(this.getHistoricoAtualAluno().getAnoHistorico()) >= 0)
//                && (hist.getSemestreHistorico().compareTo(this.getHistoricoAtualAluno().getSemestreHistorico()) >= 0)) {
            this.setHistoricoAtualAluno(getHistoricosAluno().get(getHistoricosAluno().size()-1));
//        }
    }
    
    @Override
    public String toString() {
        return "GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO [Disciplina: " + 
               this.getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplina().getCodigo() + 
               " - " + this.getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplina().getNome() +
               " - CH: " + this.getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHoraria() +
               " Histórico Atual: " + this.getHistoricoAtualAluno().toString() + "]";
    }
    
    public List<InclusaoHistoricoAlunoDisciplinaVO> getInclusaoHistoricoAlunoDisciplinaVOs() {
		if(inclusaoHistoricoAlunoDisciplinaVOs == null){
			inclusaoHistoricoAlunoDisciplinaVOs = new ArrayList<InclusaoHistoricoAlunoDisciplinaVO>(0);
		}
		return inclusaoHistoricoAlunoDisciplinaVOs;
	}

	public void setInclusaoHistoricoAlunoDisciplinaVOs(
			List<InclusaoHistoricoAlunoDisciplinaVO> inclusaoHistoricoAlunoDisciplinaVOs) {
		this.inclusaoHistoricoAlunoDisciplinaVOs = inclusaoHistoricoAlunoDisciplinaVOs;
	}	
	
}
