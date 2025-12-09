package negocio.comuns.arquitetura;

/**
 *
 * @author Kennedy
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.enumeradores.BloqueioFuncaoEnum;

public class BloqueioFuncaoVO implements Serializable {
   
    public static final long serialVersionUID = 1L;

    private BloqueioFuncaoEnum bloqueioFuncaoEnum;
    private List<FuncaoBloqueadaVO> listaFuncaoBloqueadaVO;
    

	public List<FuncaoBloqueadaVO> getListaFuncaoBloqueadaVO() {
		if(listaFuncaoBloqueadaVO == null){
			listaFuncaoBloqueadaVO = new ArrayList<FuncaoBloqueadaVO>();
		}
		return listaFuncaoBloqueadaVO;
	}

	public void setListaFuncaoBloqueadaVO(List<FuncaoBloqueadaVO> listaFuncaoBloqueadaVO) {
		this.listaFuncaoBloqueadaVO = listaFuncaoBloqueadaVO;
	}

	public BloqueioFuncaoEnum getBloqueioFuncaoEnum() {
		return bloqueioFuncaoEnum;
	}

	public void setBloqueioFuncaoEnum(BloqueioFuncaoEnum bloqueioFuncaoEnum) {
		this.bloqueioFuncaoEnum = bloqueioFuncaoEnum;
	}
    
    
}
