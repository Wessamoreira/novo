package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import relatorio.negocio.comuns.arquitetura.CrosstabVO;

public class MapaNotasDisciplinaAlunosRelVO {

	private List<CrosstabVO> crosstabVOs;
	private String dataInicioModulo;
	
	public List<CrosstabVO> getCrosstabVOs() {
		if (crosstabVOs == null) {
			crosstabVOs = new ArrayList<CrosstabVO>(0);
		}
		return crosstabVOs;
	}

	public void setCrosstabVOs(List<CrosstabVO> crosstabVOs) {
		this.crosstabVOs = crosstabVOs;
	}

	public String getDataInicioModulo() {
		if(dataInicioModulo == null) {
			dataInicioModulo = "";
		}
		return dataInicioModulo;
	}

	public void setDataInicioModulo(String dataInicioModulo) {
		this.dataInicioModulo = dataInicioModulo;
	}
	
	

}
