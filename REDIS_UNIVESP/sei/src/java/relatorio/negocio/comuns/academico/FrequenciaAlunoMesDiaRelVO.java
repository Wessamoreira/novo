package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

/**
 * @author Wellington - 29 de out de 2015
 *
 */
public class FrequenciaAlunoMesDiaRelVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer ordemMes;
	private String mes;
	private Integer presencas;
	private Integer faltas;
	private Integer abonos;
	private List<Integer> diaPresenca;
	private List<Integer> diaFalta;
	private Integer porcentagemFaltas;

	public String getMes() {
		if (mes == null) {
			mes = "";
		}
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public Integer getPresencas() {
		if (presencas == null) {
			presencas = 0;
		}
		return presencas;
	}

	public void setPresencas(Integer presencas) {
		this.presencas = presencas;
	}

	public Integer getFaltas() {
		if (faltas == null) {
			faltas = 0;
		}
		return faltas;
	}

	public void setFaltas(Integer faltas) {
		this.faltas = faltas;
	}

	public Integer getAbonos() {
		if (abonos == null) {
			abonos = 0;
		}
		return abonos;
	}

	public void setAbonos(Integer abonos) {
		this.abonos = abonos;
	}

	public Integer getOrdemMes() {
		if (ordemMes == null) {
			ordemMes = 0;
		}
		return ordemMes;
	}

	public void setOrdemMes(Integer ordemMes) {
		this.ordemMes = ordemMes;
	}

	public List<Integer> getDiaPresenca() {
		if (diaPresenca == null) {
			diaPresenca = new ArrayList<Integer>(0);
		}
		return diaPresenca;
	}

	public void setDiaPresenca(List<Integer> diaPresenca) {
		this.diaPresenca = diaPresenca;
	}

	public List<Integer> getDiaFalta() {
		if (diaFalta == null) {
			diaFalta = new ArrayList<Integer>(0);
		}
		return diaFalta;
	}

	public void setDiaFalta(List<Integer> diaFalta) {
		this.diaFalta = diaFalta;
	}

	public Integer getPorcentagemFaltas() {
		if (porcentagemFaltas == null) {
			porcentagemFaltas = 0;
		}
		return porcentagemFaltas;
	}

	public void setPorcentagemFaltas(Integer porcentagemFaltas) {
		this.porcentagemFaltas = porcentagemFaltas;
	}		
	
}
