package webservice.servicos.objetos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

/**
 * @author Victor Hugo de Paula Costa - 31 de out de 2016
 *
 */
@XmlRootElement(name = "dataEvento")
public class DataEventosRSVO {
	/**
	 * @author Victor Hugo de Paula Costa - 31 de out de 2016
	 */

	private Integer ano;
	private Integer mes;
	private Integer dia;
	private String color;
	private String textColor;
	private Date data;
	private List<AgendaAlunoRSVO> agendaAlunoRSVOs;
	private String styleClass;
	
	
	public DataEventosRSVO() {
		super();
	}

	public DataEventosRSVO(Date data, String color, String textColor, String styleClass) {
		super();		
		this.color = color;
		this.textColor = textColor;
		this.styleClass = styleClass;
		this.data = data;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		this.ano = calendar.get(Calendar.YEAR);
		this.mes = calendar.get(Calendar.MONTH);
		this.dia = calendar.get(Calendar.DAY_OF_MONTH);
	}

	@XmlElement(name = "ano")
	public Integer getAno() {
		if (ano == null) {
			ano = 0;
		}
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	@XmlElement(name = "mes")
	public Integer getMes() {
		if (mes == null) {
			mes = 0;
		}
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	@XmlElement(name = "dia")
	public Integer getDia() {
		if (dia == null) {
			dia = 0;
		}
		return dia;
	}

	public void setDia(Integer dia) {
		this.dia = dia;
	}

	@XmlElement(name = "color")
	public String getColor() {
		if (color == null) {
			color = "";
		}
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@XmlElement(name = "textColor")
	public String getTextColor() {
		if (textColor == null) {
			textColor = "";
		}
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	@XmlElement(name = "data")
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}

	public String getStyleClass() {
		if (styleClass == null) {
			styleClass = "";
		}
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	
	public List<AgendaAlunoRSVO> getAgendaAlunoRSVOs() {
		if (agendaAlunoRSVOs == null) {
			agendaAlunoRSVOs = new ArrayList<AgendaAlunoRSVO>();
		}
		return agendaAlunoRSVOs;
	}

	public void setAgendaAlunoRSVOs(List<AgendaAlunoRSVO> agendaAlunoRSVOs) {
		this.agendaAlunoRSVOs = agendaAlunoRSVOs;
	}
	
	public String toString() {
        return "DataEventosRSVO [dia=" + dia + ", mes=" + mes + ", ano=" + ano +"]";
    }
	
	public boolean isCssHorarioRegistroLancado() {
		return getStyleClass().equals("horarioRegistroLancado");
	}
	
	public boolean isCssHorarioFeriado() {
		return getStyleClass().equals("horarioFeriado");
	}
	
	public String getNenhumEventoRegistrado() {
		return "Nenhum evento para o dia "+ UteisData.getData(getData()) + ".";
	}
	
	public String getTitleDia() {
		if(getStyleClass().equals("colunaHorarioLivre")) {
			return "Dia Livre";
		}
		if(getStyleClass().equals("colunaHorarioSelecionada")) {
			return "Dia Selecionado";
		}
		if(getStyleClass().equals("horarioRegistroLancado")) {
			return "Aula Lançada";
		}
		return "";
	}
	
	public boolean isDesabilitarDia() {
		return getStyleClass().equals("colunaOutroMes");
	}
	
	public void executarAtualizacaoAgendaAlunoRSVO (AgendaAlunoRSVO obj) {
		int index = 0;
		for (AgendaAlunoRSVO agendaAlunoRSVO : getAgendaAlunoRSVOs()) {
			if(agendaAlunoRSVO.equalsCampoSelecao(obj)) {
				getAgendaAlunoRSVOs().set(index, obj);
			}
			index++;
		}
		getAgendaAlunoRSVOs().add(obj);
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ano == null) ? 0 : ano.hashCode());
		result = prime * result + ((dia == null) ? 0 : dia.hashCode());
		result = prime * result + ((mes == null) ? 0 : mes.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataEventosRSVO other = (DataEventosRSVO) obj;
		if (ano == null) {
			if (other.ano != null)
				return false;
		} else if (!ano.equals(other.ano))
			return false;
		if (dia == null) {
			if (other.dia != null)
				return false;
		} else if (!dia.equals(other.dia))
			return false;
		if (mes == null) {
			if (other.mes != null)
				return false;
		} else if (!mes.equals(other.mes))
			return false;
		return true;
	}
	
	
	
	
	
	
	
}