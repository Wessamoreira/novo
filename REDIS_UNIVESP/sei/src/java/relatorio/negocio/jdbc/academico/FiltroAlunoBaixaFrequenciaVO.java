package relatorio.negocio.jdbc.academico;


import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.negocio.comuns.academico.enumeradores.TipoFiltroPeriodoAcademicoEnum;

public class FiltroAlunoBaixaFrequenciaVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	private String ano;
	private String semestre;
	private TipoFiltroPeriodoAcademicoEnum tipoFiltroPeriodoAcademico;

	
	public String getAno() {
		if (ano == null) {
			ano = Uteis.getAnoDataAtual();
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = Uteis.getSemestreAtual();
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public TipoFiltroPeriodoAcademicoEnum getTipoFiltroPeriodoAcademico() {
		if (tipoFiltroPeriodoAcademico == null) {
			tipoFiltroPeriodoAcademico = TipoFiltroPeriodoAcademicoEnum.ANO_SEMESTRE;
		}
		return tipoFiltroPeriodoAcademico;
	}

	public void setTipoFiltroPeriodoAcademico(TipoFiltroPeriodoAcademicoEnum tipoFiltroPeriodoAcademico) {
		this.tipoFiltroPeriodoAcademico = tipoFiltroPeriodoAcademico;
	}

	public boolean getFiltrarPorAnoSemestre() {
		return getTipoFiltroPeriodoAcademico().equals(TipoFiltroPeriodoAcademicoEnum.ANO_SEMESTRE);
	}
	
	public boolean getFiltrarPorAno() {
		return getTipoFiltroPeriodoAcademico().equals(TipoFiltroPeriodoAcademicoEnum.ANO);
	}
	
}
