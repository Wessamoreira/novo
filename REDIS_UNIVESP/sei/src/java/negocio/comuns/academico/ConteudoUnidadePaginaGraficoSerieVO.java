package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;


public class ConteudoUnidadePaginaGraficoSerieVO extends SuperVO {
    
    private Integer sequencia;
    private String serie;
    private List<ConteudoUnidadePaginaGraficoSerieValorVO> conteudoUnidadePaginaGraficoSerieValorVOs;
    
    
    
    public ConteudoUnidadePaginaGraficoSerieVO(Integer sequencia, String serie, List<ConteudoUnidadePaginaGraficoSerieValorVO> conteudoUnidadePaginaGraficoSerieValorVOs) {
        super();
        this.sequencia = sequencia;
        this.serie = serie;
        this.conteudoUnidadePaginaGraficoSerieValorVOs = conteudoUnidadePaginaGraficoSerieValorVOs;
    }
    
    public ConteudoUnidadePaginaGraficoSerieVO(Integer sequencia, String serie) {
        super();        
        this.sequencia = sequencia;
        this.serie = serie;        
    }
    
    public ConteudoUnidadePaginaGraficoSerieVO clone() throws CloneNotSupportedException {
    	ConteudoUnidadePaginaGraficoSerieVO clone = (ConteudoUnidadePaginaGraficoSerieVO) super.clone();
    	clone.setNovoObj(true);
    	for (ConteudoUnidadePaginaGraficoSerieValorVO conteudoUnidadePaginaGraficoSerieValorVO : this.conteudoUnidadePaginaGraficoSerieValorVOs) {
			ConteudoUnidadePaginaGraficoSerieValorVO cloneConteudoUnidadePaginaGraficoSerieValorVO = conteudoUnidadePaginaGraficoSerieValorVO.clone();
			clone.getConteudoUnidadePaginaGraficoSerieValorVOs().add(cloneConteudoUnidadePaginaGraficoSerieValorVO);
		}
    	
    	return clone;
    }

    public String getSerie() {
        if(serie == null){
            serie = "";
        }
        return serie;
    }
    
    public void setSerie(String serie) {
        this.serie = serie;
    }
    
    public Integer getSequencia() {
        if(sequencia == null){
            sequencia = 0;
        }
        return sequencia;
    }

    
    public void setSequencia(Integer sequencia) {
        this.sequencia = sequencia;
    }

    
    public List<ConteudoUnidadePaginaGraficoSerieValorVO> getConteudoUnidadePaginaGraficoSerieValorVOs() {
        if(conteudoUnidadePaginaGraficoSerieValorVOs ==null){
            conteudoUnidadePaginaGraficoSerieValorVOs = new ArrayList<ConteudoUnidadePaginaGraficoSerieValorVO>(0);
        }
        return conteudoUnidadePaginaGraficoSerieValorVOs;
    }

    
    public void setConteudoUnidadePaginaGraficoSerieValorVOs(List<ConteudoUnidadePaginaGraficoSerieValorVO> conteudoUnidadePaginaGraficoSerieValorVOs) {
        this.conteudoUnidadePaginaGraficoSerieValorVOs = conteudoUnidadePaginaGraficoSerieValorVOs;
    }

    
    public String getSerieApresentar() {
        if(getSerie().length() > 16){
            return getSerie().substring(0, 14)+"...";
        }
        
        return getSerie();
    }
   
    
    

}
