package relatorio.negocio.comuns.processosel;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.processosel.InscricaoRespostaNaoProcessadaVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class ProcessarResultadoProcessoSeletivoRelVO {
	
    private List<InscricaoRespostaNaoProcessadaVO> inscricaoRespostaNaoProcessadaVOs;
    private List<ResultadoProcessoSeletivoVO> resultadoProcessoSeletivoVOs;
    
    public JRDataSource getInscricaoRespostaNaoProcessadaVOsJRDataSource() {
        return new JRBeanArrayDataSource(getInscricaoRespostaNaoProcessadaVOs().toArray());
    }
    
    public JRDataSource getResultadoProcessoSeletivoVOsJRDataSource() {
        return new JRBeanArrayDataSource(getResultadoProcessoSeletivoVOs().toArray());
    }
    
	public List<InscricaoRespostaNaoProcessadaVO> getInscricaoRespostaNaoProcessadaVOs() {
		if (resultadoProcessoSeletivoVOs == null) {
			resultadoProcessoSeletivoVOs = new ArrayList<ResultadoProcessoSeletivoVO>(0);
		}
		return inscricaoRespostaNaoProcessadaVOs;
	}
	public void setInscricaoRespostaNaoProcessadaVOs(List<InscricaoRespostaNaoProcessadaVO> inscricaoRespostaNaoProcessadaVOs) {
		this.inscricaoRespostaNaoProcessadaVOs = inscricaoRespostaNaoProcessadaVOs;
	}
	public List<ResultadoProcessoSeletivoVO> getResultadoProcessoSeletivoVOs() {
		if (resultadoProcessoSeletivoVOs == null) {
			resultadoProcessoSeletivoVOs = new ArrayList<ResultadoProcessoSeletivoVO>(0);
		}
		return resultadoProcessoSeletivoVOs;
	}
	public void setResultadoProcessoSeletivoVOs(List<ResultadoProcessoSeletivoVO> resultadoProcessoSeletivoVOs) {
		this.resultadoProcessoSeletivoVOs = resultadoProcessoSeletivoVOs;
	}

}
