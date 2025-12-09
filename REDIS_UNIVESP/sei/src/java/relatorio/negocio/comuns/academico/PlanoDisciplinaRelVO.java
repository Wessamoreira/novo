package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.PerguntaRespostaOrigemVO;
import negocio.comuns.academico.PlanoEnsinoHorarioAulaVO;
import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class PlanoDisciplinaRelVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigoDisciplina;
	private String disciplina;
	private String professor;
	private String tipoDisciplina;
	private Integer cargaHoraria;
	private String ementa;
	//private String competencia;
	private String objetivosGerais;
	private String objetivosEspecificos;
	private String estrategiasAvaliacao;
	private String procedimentoDidatico;
	private String periodoLetivo;
	private String anoSemestre;
	private Boolean preRequisito;
	private String turno;
	private List<PlanoDisciplinaReferenciaBibliograficaRelVO> listaPlanoReferenciaBibliograficaVOs;
	private List<PlanoDisciplinaConteudoPlanejamentoRelVO> listaPlanoConteudoPlanejamentoVOs;
	private List<PlanoEnsinoHorarioAulaVO> planoEnsinoHorarioAulaVOs;
	private String perfilEgresso;
	private List<PerguntaRespostaOrigemVO>PerguntaRespostaOrigemVOs;
	private List<PlanoEnsinoVO> planoEnsinoVOs;
	private String situacao;
	private CursoVO curso;
	private UnidadeEnsinoVO unidadeEnsino;

	public JRDataSource getPlanoReferenciaBibliograficaJRDataSource() {
        return new JRBeanArrayDataSource(listaPlanoReferenciaBibliograficaVOs.toArray());
    }
	
	public JRDataSource getPlanoConteudoPlanejamentoJRDataSource() {
		return new JRBeanArrayDataSource(listaPlanoConteudoPlanejamentoVOs.toArray());
	}
	
	public String getDisciplina() {
		if (disciplina == null) {
			disciplina = "";
		}
		return disciplina;
	}

	public void setDisciplina(String disciplina) {
		this.disciplina = disciplina;
	}

	public String getTipoDisciplina() {
		if (tipoDisciplina == null) {
			tipoDisciplina = "";
		}
		return tipoDisciplina;
	}

	public void setTipoDisciplina(String tipoDisciplina) {
		this.tipoDisciplina = tipoDisciplina;
	}

	public Integer getCargaHoraria() {
		if (cargaHoraria == null) {
			cargaHoraria = 0;
		}
		return cargaHoraria;
	}

	public void setCargaHoraria(Integer cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public String getEmenta() {
		if (ementa == null) {
			ementa = "";
		}
		return ementa;
	}

	public void setEmenta(String ementa) {
		this.ementa = ementa;
	}

//	public String getCompetencia() {
//		if (competencia == null) {
//			competencia = "";
//		}
//		return competencia;
//	}
//
//	public void setCompetencia(String competencia) {
//		this.competencia = competencia;
//	}

	public String getObjetivosGerais() {
		if (objetivosGerais == null) {
			objetivosGerais = "";
		}
		return objetivosGerais;
	}

	public void setObjetivosGerais(String objetivosGerais) {
		this.objetivosGerais = objetivosGerais;
	}

	public String getObjetivosEspecificos() {
		if (objetivosEspecificos == null) {
			objetivosEspecificos = "";
		}
		return objetivosEspecificos;
	}

	public void setObjetivosEspecificos(String objetivosEspecificos) {
		this.objetivosEspecificos = objetivosEspecificos;
	}

	public String getEstrategiasAvaliacao() {
		if (estrategiasAvaliacao == null) {
			estrategiasAvaliacao = "";
		}
		return estrategiasAvaliacao;
	}

	public void setEstrategiasAvaliacao(String estrategiasAvaliacao) {
		this.estrategiasAvaliacao = estrategiasAvaliacao;
	}

	public String getPeriodoLetivo() {
		if (periodoLetivo == null) {
			periodoLetivo = "";
		}
		return periodoLetivo;
	}

	public void setPeriodoLetivo(String periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}

	public Boolean getPreRequisito() {
		if (preRequisito == null) {
			preRequisito = Boolean.FALSE;
		}
		return preRequisito;
	}

	public void setPreRequisito(Boolean preRequisito) {
		this.preRequisito = preRequisito;
	}

	public List<PlanoDisciplinaReferenciaBibliograficaRelVO> getListaPlanoReferenciaBibliograficaVOs() {
		if (listaPlanoReferenciaBibliograficaVOs == null) {
			listaPlanoReferenciaBibliograficaVOs = new ArrayList<PlanoDisciplinaReferenciaBibliograficaRelVO>(0);
		}
		return listaPlanoReferenciaBibliograficaVOs;
	}

	public void setListaPlanoReferenciaBibliograficaVOs(List<PlanoDisciplinaReferenciaBibliograficaRelVO> listaPlanoReferenciaBibliograficaVOs) {
		this.listaPlanoReferenciaBibliograficaVOs = listaPlanoReferenciaBibliograficaVOs;
	}

	public List<PlanoDisciplinaConteudoPlanejamentoRelVO> getListaPlanoConteudoPlanejamentoVOs() {
		if (listaPlanoConteudoPlanejamentoVOs == null) {
			listaPlanoConteudoPlanejamentoVOs = new ArrayList<PlanoDisciplinaConteudoPlanejamentoRelVO>(0);
		}
		return listaPlanoConteudoPlanejamentoVOs;
	}

	public void setListaPlanoConteudoPlanejamentoVOs(List<PlanoDisciplinaConteudoPlanejamentoRelVO> listaPlanoConteudoPlanejamentoVOs) {
		this.listaPlanoConteudoPlanejamentoVOs = listaPlanoConteudoPlanejamentoVOs;
	}

	public Integer getCodigoDisciplina() {
		if (codigoDisciplina == null) {
			codigoDisciplina = 0;
		}
		return codigoDisciplina;
	}

	public void setCodigoDisciplina(Integer codigoDisciplina) {
		this.codigoDisciplina = codigoDisciplina;
	}

	public String getProcedimentoDidatico() {
		if(procedimentoDidatico == null){
			procedimentoDidatico = "";
		}
		return procedimentoDidatico;
	}

	public void setProcedimentoDidatico(String procedimentoDidatico) {
		this.procedimentoDidatico = procedimentoDidatico;
	}

	public String getAnoSemestre() {
		if(anoSemestre == null){
			anoSemestre = "";
		}
		return anoSemestre;
	}

	public void setAnoSemestre(String anoSemestre) {
		this.anoSemestre = anoSemestre;
	}
	

	public String getProfessor() {
		if(professor == null){
			professor = "";
		}
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}
	
	public List<PlanoEnsinoHorarioAulaVO> getPlanoEnsinoHorarioAulaVOs() {
		if(planoEnsinoHorarioAulaVOs == null){
			planoEnsinoHorarioAulaVOs =  new ArrayList<PlanoEnsinoHorarioAulaVO>(0);
		}
		return planoEnsinoHorarioAulaVOs;
	}

	public void setPlanoEnsinoHorarioAulaVOs(List<PlanoEnsinoHorarioAulaVO> planoEnsinoHorarioAulaVOs) {
		this.planoEnsinoHorarioAulaVOs = planoEnsinoHorarioAulaVOs;
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

	public String getPerfilEgresso() {
		if(perfilEgresso == null){
			perfilEgresso ="";
		}
		return perfilEgresso;
	}

	public void setPerfilEgresso(String perfilEgresso) {
		this.perfilEgresso = perfilEgresso;
	}

	public List<PerguntaRespostaOrigemVO> getPerguntaRespostaOrigemVOs() {
		if(PerguntaRespostaOrigemVOs == null) {
			PerguntaRespostaOrigemVOs = new ArrayList<PerguntaRespostaOrigemVO>(0);
		}
		return PerguntaRespostaOrigemVOs;
	}

	public void setPerguntaRespostaOrigemVOs(List<PerguntaRespostaOrigemVO> perguntaRespostaOrigemVOs) {
		PerguntaRespostaOrigemVOs = perguntaRespostaOrigemVOs;
	}
	
	public JRDataSource getQuestionarioPerguntaRespostaOrigemJRDataSource() {
		return new JRBeanArrayDataSource(getPerguntaRespostaOrigemVOs().toArray());
	}

	public List<PlanoEnsinoVO> getPlanoEnsinoVOs() {
		if(planoEnsinoVOs == null) {
			planoEnsinoVOs = new ArrayList<PlanoEnsinoVO>(0);
		}
		return planoEnsinoVOs;
	}

	public void setPlanoEnsinoVOs(List<PlanoEnsinoVO> planoEnsinoVOs) {
		this.planoEnsinoVOs = planoEnsinoVOs;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public CursoVO getCurso() {
		if (curso == null) {
			curso = new CursoVO();
		}
		return curso;
	}

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setCurso(CursoVO curso) {
		this.curso = curso;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}
}
