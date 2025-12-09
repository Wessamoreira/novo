package negocio.comuns.blackboard;

import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;



public class SalaAulaBlackboardNotaVO extends SuperVO{
	
	private static final long serialVersionUID = 8499916064536315636L;
	private Integer codigoOperacao;
	private Boolean realizarCalculoMediaApuracaoNotas = false;
	private Integer salaAulaBlackboard;
	private Integer disciplina;
	private String ano;
	private String semestre;
	private String tipoOrigem;
	private List<SalaAulaBlackboardPessoaNotaVO> listaSalaAulaBlackboardPessoaNotaVO;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO;
	
	public Integer getSalaAulaBlackboard() {
		return salaAulaBlackboard;
	}

	public void setSalaAulaBlackboard(Integer salaAulaBlackboard) {
		this.salaAulaBlackboard = salaAulaBlackboard;
	}

	public Integer getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Integer disciplina) {
		this.disciplina = disciplina;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public List<SalaAulaBlackboardPessoaNotaVO> getListaSalaAulaBlackboardPessoaNotaVO() {
		return listaSalaAulaBlackboardPessoaNotaVO;
	}

	public void setListaSalaAulaBlackboardPessoaNotaVO(List<SalaAulaBlackboardPessoaNotaVO> listaSalaAulaBlackboardPessoaNotaVO) {
		this.listaSalaAulaBlackboardPessoaNotaVO = listaSalaAulaBlackboardPessoaNotaVO;
	}

	public Integer getCodigoOperacao() {
		return codigoOperacao;
	}

	public void setCodigoOperacao(Integer codigoOperacao) {
		this.codigoOperacao = codigoOperacao;
	}

	public Boolean getRealizarCalculoMediaApuracaoNotas() {
		return realizarCalculoMediaApuracaoNotas;
	}

	public void setRealizarCalculoMediaApuracaoNotas(Boolean realizarCalculoMediaApuracaoNotas) {
		this.realizarCalculoMediaApuracaoNotas = realizarCalculoMediaApuracaoNotas;
	}

	public String getTipoOrigem() {
		return tipoOrigem;
	}

	public void setTipoOrigem(String tipoOrigem) {
		this.tipoOrigem = tipoOrigem;
	}

	public FiltroRelatorioAcademicoVO getFiltroRelatorioAcademicoVO() {
		return filtroRelatorioAcademicoVO;
	}

	public void setFiltroRelatorioAcademicoVO(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) {
		this.filtroRelatorioAcademicoVO = filtroRelatorioAcademicoVO;
	}
	
	
	

}
