/**
 * 
 */
package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Carlos Eugênio
 *
 */
public enum NomeTurnoCensoEnum {

	MATUTINO(1, "08:00", "12:00", "M"), VESPERTINO(2, "13:00", "17:00", "T"), NOTURNO(3, "18:00", "22:00", "N"), INTEGRAL(4, "08:00", "22:00", "I") , NENHUM(0, "", "", "");

	NomeTurnoCensoEnum(Integer valorCenso, String horarioInicio, String horarioTermino, String sigla) {
		this.valorCenso = valorCenso;
		this.horarioInicio = horarioInicio;
		this.horarioTermino = horarioTermino;
		this.sigla = sigla;
	}

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_NomeTurnoCensoEnum_" + this.name());
	}

	private Integer valorCenso;
	private String horarioInicio;
	private String horarioTermino;
	private String sigla;

	public Integer getValorCenso() {
		return valorCenso;
	}

	public String getHorarioInicio() {
		return horarioInicio;
	}

	public void setHorarioInicio(String horarioInicio) {
		this.horarioInicio = horarioInicio;
	}

	public String getHorarioTermino() {
		return horarioTermino;
	}

	public void setHorarioTermino(String horarioTermino) {
		this.horarioTermino = horarioTermino;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public void setValorCenso(Integer valorCenso) {
		this.valorCenso = valorCenso;
	}
	
	
}

