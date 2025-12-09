/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Ordenacao;

/**
 *
 * @author EDIGARANTONIO
 */
public class GradeDisciplinaComHistoricoAlunoVO extends SuperVO {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GradeDisciplinaVO gradeDisciplinaVO;
    private List<HistoricoVO> historicosAluno;
    private HistoricoVO historicoAtualAluno;
    /**
     * TRANSIENTE UTILIZADO PARA MONTAR DADOS NA TELA DE APROVEITAMENTO DE
     * DISCIPLINA
     */
    private DisciplinasAproveitadasVO disciplinasAproveitadasVO;
    private Boolean selecionadoAproveitamento;
    /**
     * Utilizado na tela de inclusão de disciplina do aluno, criado com a finalidade de realizar o registro de historicos sem informar turma, matricula periodo.
     */
    private InclusaoHistoricoAlunoDisciplinaVO inclusaoHistoricoAlunoDisciplinaVO;
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
        if (historicosAluno == null) {
            historicosAluno = new ArrayList(0);
        }
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
        if (historicoAtualAluno == null) {
            historicoAtualAluno = new HistoricoVO();
        }
        this.historicoAtualAluno = historicoAtualAluno;
    }

    /**
     * @return the gradeDisciplinaVO
     */
    public GradeDisciplinaVO getGradeDisciplinaVO() {
        if (gradeDisciplinaVO == null) {
            gradeDisciplinaVO = new GradeDisciplinaVO();
        }
        return gradeDisciplinaVO;
    }

    /**
     * @param gradeDisciplinaVO the gradeDisciplinaVO to set
     */
    public void setGradeDisciplinaVO(GradeDisciplinaVO gradeDisciplinaVO) {
        this.gradeDisciplinaVO = gradeDisciplinaVO;
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
        if ((this.getHistoricoAtualAluno().getReprovado()) &&
            (hist.getAprovado())) {
            // Este if basicamente determina que se o histórico atual do aluno
            // é reprovação e um novo histórico de aprovação foi encontrado, então 
            // o de aprovação deve prevalecer. Pois, não faz sentido ter um histórico
            // de reprovação para uma disciplina no qual o aluno já foi aprovado.
            // Isto é especialmente importante para a pós, pois no caso de pos-graducacao
            // o ano e o semestre não são gravados (ficam em brano) e assim o código abaixo
            // iria falhar, pois o mesmo utiliza estes dados para determinar o histórico
            // atual (ou seja, o histórico mandante da vida academica do alnuo).
            this.setHistoricoAtualAluno(hist);
        }
        if (this.getHistoricoAtualAluno().getAprovado()) {
            // se já se encontrou um histórico de aprovação para a disciplina
            // então, não faz sentido, substituir o histórico atual por nenhum
            // outro.
            return;
        }
        Ordenacao.ordenarLista(getHistoricosAluno(), "anoSemestreOrdenacao");
//        if ((hist.getAnoHistorico().compareTo(this.getHistoricoAtualAluno().getAnoHistorico()) >= 0)
//                && (hist.getSemestreHistorico().compareTo(this.getHistoricoAtualAluno().getSemestreHistorico()) >= 0)) {
            this.setHistoricoAtualAluno(getHistoricosAluno().get(getHistoricosAluno().size()-1));
//        }
    }

    public DisciplinasAproveitadasVO getDisciplinasAproveitadasVO() {
        if (disciplinasAproveitadasVO == null) {
            disciplinasAproveitadasVO = new DisciplinasAproveitadasVO();
        }
        return disciplinasAproveitadasVO;
    }

    public void setDisciplinasAproveitadasVO(DisciplinasAproveitadasVO disciplinasAproveitadasVO) {
        this.disciplinasAproveitadasVO = disciplinasAproveitadasVO;
    }

    public Boolean getSelecionadoAproveitamento() {
        if (selecionadoAproveitamento == null) {
            selecionadoAproveitamento = Boolean.FALSE;
        }
        return selecionadoAproveitamento;
    }

    public void setSelecionadoAproveitamento(Boolean selecionadoAproveitamento) {
        this.selecionadoAproveitamento = selecionadoAproveitamento;
    }

	@Override
	public String toString() {
		return "GradeDisciplinaComHistoricoAlunoVO [gradeDisciplinaVO=" + gradeDisciplinaVO + ", historicosAluno=" + historicosAluno + ", historicoAtualAluno=" + historicoAtualAluno + ", disciplinasAproveitadasVO=" + disciplinasAproveitadasVO + ", selecionadoAproveitamento=" + selecionadoAproveitamento + "]";
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

	public InclusaoHistoricoAlunoDisciplinaVO getInclusaoHistoricoAlunoDisciplinaVO() {
		if(inclusaoHistoricoAlunoDisciplinaVO == null){
			inclusaoHistoricoAlunoDisciplinaVO = new InclusaoHistoricoAlunoDisciplinaVO();
		}
		return inclusaoHistoricoAlunoDisciplinaVO;
	}

	public void setInclusaoHistoricoAlunoDisciplinaVO(
			InclusaoHistoricoAlunoDisciplinaVO inclusaoHistoricoAlunoDisciplinaVO) {
		this.inclusaoHistoricoAlunoDisciplinaVO = inclusaoHistoricoAlunoDisciplinaVO;
	}	
	
	
}
