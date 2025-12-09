package negocio.comuns.academico;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;

@XmlRootElement(name = "mapaEquivalenciaDisciplinaMatrizCurricularVO")
public class MapaEquivalenciaDisciplinaMatrizCurricularVO extends SuperVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2953540745934893259L;
	private Integer codigo;
	private DisciplinaVO disciplinaVO;
	private Integer cargaHoraria;
	private Integer numeroCredito;
	private MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplina;

        /**
         * TRANSIENT, SOMENTE PARA DEMONSTRAR A SITUAÇÃO DA DISCIPLINA DENTRO 
         * DO MAPA
         */
        private HistoricoVO historico;
    
    @XmlElement(name = "codigo")
	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public DisciplinaVO getDisciplinaVO() {
		if(disciplinaVO == null){
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}
	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}
	public MapaEquivalenciaDisciplinaVO getMapaEquivalenciaDisciplina() {
		if(mapaEquivalenciaDisciplina == null){
			mapaEquivalenciaDisciplina = new MapaEquivalenciaDisciplinaVO();
		}
		return mapaEquivalenciaDisciplina;
	}
	public void setMapaEquivalenciaDisciplina(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplina) {
		this.mapaEquivalenciaDisciplina = mapaEquivalenciaDisciplina;
	}

    /**
     * @return the historico
     */
    public HistoricoVO getHistorico() {
        if (historico == null) {
            historico = new HistoricoVO();
        }
        return historico;
    }

    /**
     * @param historico the historico to set
     */
    public void setHistorico(HistoricoVO historico) {
        this.historico = historico;
    }
	public Integer getCargaHoraria() {
		if (cargaHoraria == null) {
			cargaHoraria = 0;
		}
		return cargaHoraria;
	}
	public void setCargaHoraria(Integer cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}
	public Integer getNumeroCredito() {
		if (numeroCredito == null) {
			numeroCredito = 0;
		}
		return numeroCredito;
	}
	public void setNumeroCredito(Integer numeroCredito) {
		this.numeroCredito = numeroCredito;
	}
	
	
	public String getOrdenacao(){
    	return getDisciplinaVO().getNome()+"COD:"+getDisciplinaVO().getCodigo()+"CH:"+getCargaHoraria().toString();
    }
	
	public String getDisciplinaApresentar(){
		return getDisciplinaVO().getAbreviatura()+" - "+getDisciplinaVO().getNome()+" - CH: "+getCargaHoraria();
	}
}
