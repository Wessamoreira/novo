package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

import negocio.comuns.utilitarias.Ordenacao;

@XmlRootElement(name = "gradeDisciplinaCompostaVO")
public class GradeDisciplinaCompostaVO extends GradeDisciplinaVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7250321605645337310L;

	private GradeDisciplinaVO gradeDisciplina;
	private Boolean grupoOptativa;
	private GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplina;
	private transient Integer ordem;
	/**
	 * Este campo define a variavel de nota que será utilizada no calculo da nota da disciplina composta (MAE) quando no campo formulaCalculoNota da
	 * disciplina mae for do tipo FORMULA_CALCULO
	 */
	private String variavelNota;
	private transient List<DisciplinaPreRequisitoVO> disciplinaRequisitoVOs;

	public GradeDisciplinaVO getGradeDisciplina() {
		if (gradeDisciplina == null) {
			gradeDisciplina = new GradeDisciplinaVO();
		}
		return gradeDisciplina;
	}

	public void setGradeDisciplina(GradeDisciplinaVO gradeDisciplina) {
		this.gradeDisciplina = gradeDisciplina;
	}

	public Boolean getGrupoOptativa() {
		if (grupoOptativa == null) {
			grupoOptativa = false;
		}
		return grupoOptativa;
	}

	public void setGrupoOptativa(Boolean grupoOptativa) {
		this.grupoOptativa = grupoOptativa;
	}

	public GradeCurricularGrupoOptativaDisciplinaVO getGradeCurricularGrupoOptativaDisciplina() {
		if (gradeCurricularGrupoOptativaDisciplina == null) {
			gradeCurricularGrupoOptativaDisciplina = new GradeCurricularGrupoOptativaDisciplinaVO();
		}
		return gradeCurricularGrupoOptativaDisciplina;
	}

	public void setGradeCurricularGrupoOptativaDisciplina(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplina) {
		this.gradeCurricularGrupoOptativaDisciplina = gradeCurricularGrupoOptativaDisciplina;
	}

	public Integer getOrdem() {
		if (ordem == null) {
			ordem = 0;
		}
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public String executarVerificacaoNotaAtualizarHistoricoDisciplinaComposta() {
		return "NOTA" + (this.getOrdem().intValue() + 1);
	}

	public DisciplinaVO getDisciplinaMaeComposicao() {
		if (this.getGrupoOptativa()) {
			return this.getGradeCurricularGrupoOptativaDisciplina().getDisciplina();
		} else {
			return this.getGradeDisciplina().getDisciplina();
		}
	}

	public String getVariavelNota() {
		if (variavelNota == null) {
			variavelNota = "";
		}
		return variavelNota;
	}

	public void setVariavelNota(String variavelNota) {
		this.variavelNota = variavelNota;
	}

	@Override
	public List<DisciplinaPreRequisitoVO> getDisciplinaRequisitoVOs() {
		if (disciplinaRequisitoVOs == null) {
			disciplinaRequisitoVOs = new ArrayList<DisciplinaPreRequisitoVO>();
		}
		return disciplinaRequisitoVOs;
	}

	@Override
	public void setDisciplinaRequisitoVOs(List<DisciplinaPreRequisitoVO> disciplinaRequisitoVOs) {
		this.disciplinaRequisitoVOs = disciplinaRequisitoVOs;
	}

	/**
	 * Método elaborado para atender layout 3, que exibe apenas a abreviatura da disciplina
	 * 
	 * @return
	 */
	public String getAbreviaturaPreRequisitoCompostaApresentar() {
		String valor = "";
		Ordenacao.ordenarLista(getDisciplinaRequisitoVOs(), "codigo");
		for (DisciplinaPreRequisitoVO disciplinaPreRequisitoVO : getDisciplinaRequisitoVOs()) {
			if (valor.trim().isEmpty()) {
				valor = disciplinaPreRequisitoVO.getDisciplina().getAbreviatura();
			} else {
				valor += ", " + disciplinaPreRequisitoVO.getDisciplina().getAbreviatura();
			}
		}
		return valor;
	}
	

	private List<HistoricoVO> historicosAluno;
	
	
	 public List<HistoricoVO> getHistoricosAluno() {
		 if(historicosAluno == null){
			 historicosAluno = new ArrayList<HistoricoVO>(0);
		 }
		return historicosAluno;
	}

	public void setHistoricosAluno(List<HistoricoVO> historicosAluno) {
		this.historicosAluno = historicosAluno;
	}

	public void adicionarHistoricoParaGradeDisciplina(HistoricoVO hist) {	      
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
//	        if ((hist.getAnoHistorico().compareTo(this.getHistoricoAtualAluno().getAnoHistorico()) >= 0)
//	                && (hist.getSemestreHistorico().compareTo(this.getHistoricoAtualAluno().getSemestreHistorico()) >= 0)) {
	            this.setHistoricoAtualAluno(getHistoricosAluno().get(getHistoricosAluno().size()-1));
//	        }
	    }


}
