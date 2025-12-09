package negocio.comuns.processosel;

import java.util.ArrayList;
import java.util.List;


public class ResultadoProcessamentoArquivoRespostaVO {
    
    private List<InscricaoRespostaNaoProcessadaVO> inscricaoRespostaNaoProcessadaVOs;
    private List<ResultadoProcessoSeletivoVO> resultadoProcessoSeletivoVOs;
    private List<InscricaoVO> listaInscricaoNaoComparecidosVOs;
    
    public List<InscricaoRespostaNaoProcessadaVO> getInscricaoRespostaNaoProcessadaVOs() {
        if(inscricaoRespostaNaoProcessadaVOs == null){
            inscricaoRespostaNaoProcessadaVOs = new ArrayList<InscricaoRespostaNaoProcessadaVO>(0);
        }
        return inscricaoRespostaNaoProcessadaVOs;
    }

    
    public void setInscricaoRespostaNaoProcessadaVOs(List<InscricaoRespostaNaoProcessadaVO> inscricaoRespostaNaoProcessadaVOs) {
        this.inscricaoRespostaNaoProcessadaVOs = inscricaoRespostaNaoProcessadaVOs;
    }

    public List<ResultadoProcessoSeletivoVO> getResultadoProcessoSeletivoVOs() {
        if(resultadoProcessoSeletivoVOs == null){
            resultadoProcessoSeletivoVOs = new ArrayList<ResultadoProcessoSeletivoVO>(0);
        }
        return resultadoProcessoSeletivoVOs;
    }
    
    public void setResultadoProcessoSeletivoVOs(List<ResultadoProcessoSeletivoVO> resultadoProcessoSeletivoVOs) {
        this.resultadoProcessoSeletivoVOs = resultadoProcessoSeletivoVOs;
    }
    
    public Integer getQtdeResultado(){
        return getResultadoProcessoSeletivoVOs().size();
    }
    
    public Integer getQtdeErro(){
        return getInscricaoRespostaNaoProcessadaVOs().size();
    }

    public List<InscricaoVO> getListaInscricaoNaoComparecidosVOs() {
		if (listaInscricaoNaoComparecidosVOs == null) {
			listaInscricaoNaoComparecidosVOs = new ArrayList<InscricaoVO>(0);
		}
		return listaInscricaoNaoComparecidosVOs;
	}


	public void setListaInscricaoNaoComparecidosVOs(List<InscricaoVO> listaInscricaoNaoComparecidosVOs) {
		this.listaInscricaoNaoComparecidosVOs = listaInscricaoNaoComparecidosVOs;
	}

	public Integer getQtdeNaoCompareceu() {
		return getListaInscricaoNaoComparecidosVOs().size();
	}
}
