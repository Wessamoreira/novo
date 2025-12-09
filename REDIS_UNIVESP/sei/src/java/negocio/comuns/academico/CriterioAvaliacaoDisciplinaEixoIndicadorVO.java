package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

public class CriterioAvaliacaoDisciplinaEixoIndicadorVO extends SuperVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -398335685765903092L;
	private Integer codigo;
	private String eixoIndicador;
	private String eixoIndicadorAnt;
	private CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplina;
	private Double nota;
	private List<CriterioAvaliacaoIndicadorVO> criterioAvaliacaoIndicadorVOs;
	private Integer ordem;
	//Transientes
	private Integer qtdeIndicador1Bimestre;
	private Integer qtdeIndicador2Bimestre;
	private Integer qtdeIndicador3Bimestre;
	private Integer qtdeIndicador4Bimestre;
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getEixoIndicador() {
		if (eixoIndicador == null) {
			eixoIndicador = "";
		}
		return eixoIndicador;
	}
	public void setEixoIndicador(String eixoIndicador) {
		this.eixoIndicador = eixoIndicador;
	}
	public CriterioAvaliacaoDisciplinaVO getCriterioAvaliacaoDisciplina() {
		if (criterioAvaliacaoDisciplina == null) {
			criterioAvaliacaoDisciplina = new CriterioAvaliacaoDisciplinaVO();
		}
		return criterioAvaliacaoDisciplina;
	}
	public void setCriterioAvaliacaoDisciplina(CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplina) {
		this.criterioAvaliacaoDisciplina = criterioAvaliacaoDisciplina;
	}
	public Double getNota() {
		if (nota == null) {
			nota = 0.0;
		}
		return nota;
	}
	public void setNota(Double nota) {
		this.nota = nota;
	}
	public List<CriterioAvaliacaoIndicadorVO> getCriterioAvaliacaoIndicadorVOs() {
		if (criterioAvaliacaoIndicadorVOs == null) {
			criterioAvaliacaoIndicadorVOs = new ArrayList<CriterioAvaliacaoIndicadorVO>(0);
		}
		return criterioAvaliacaoIndicadorVOs;
	}
	public void setCriterioAvaliacaoIndicadorVOs(List<CriterioAvaliacaoIndicadorVO> criterioAvaliacaoIndicadorVOs) {
		this.criterioAvaliacaoIndicadorVOs = criterioAvaliacaoIndicadorVOs;
	}
	public Integer getOrdem() {
		if (ordem == null) {
			ordem = 0;
		}
		return ordem;
	}
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	
	public String getEixoIndicadorAnt() {
		if (eixoIndicadorAnt == null) {
			eixoIndicadorAnt = "";
		}
		return eixoIndicadorAnt;
	}
	public void setEixoIndicadorAnt(String eixoIndicadorAnt) {
		this.eixoIndicadorAnt = eixoIndicadorAnt;
	}
	public Integer getQtdeIndicador1Bimestre() {
		if (qtdeIndicador1Bimestre == null) {
			qtdeIndicador1Bimestre = 0;
		}
		return qtdeIndicador1Bimestre;
	}
	public void setQtdeIndicador1Bimestre(Integer qtdeIndicador1Bimestre) {
		this.qtdeIndicador1Bimestre = qtdeIndicador1Bimestre;
	}
	public Integer getQtdeIndicador2Bimestre() {
		if (qtdeIndicador2Bimestre == null) {
			qtdeIndicador2Bimestre = 0;
		}
		return qtdeIndicador2Bimestre;
	}
	public void setQtdeIndicador2Bimestre(Integer qtdeIndicador2Bimestre) {
		this.qtdeIndicador2Bimestre = qtdeIndicador2Bimestre;
	}
	public Integer getQtdeIndicador3Bimestre() {
		if (qtdeIndicador3Bimestre == null) {
			qtdeIndicador3Bimestre = 0;
		}
		return qtdeIndicador3Bimestre;
	}
	public void setQtdeIndicador3Bimestre(Integer qtdeIndicador3Bimestre) {
		this.qtdeIndicador3Bimestre = qtdeIndicador3Bimestre;
	}
	public Integer getQtdeIndicador4Bimestre() {
		if (qtdeIndicador4Bimestre == null) {
			qtdeIndicador4Bimestre = 0;
		}
		return qtdeIndicador4Bimestre;
	}
	
	public void setQtdeIndicador4Bimestre(Integer qtdeIndicador4Bimestre) {
		this.qtdeIndicador4Bimestre = qtdeIndicador4Bimestre;
	}
	
	public Integer getQtdeIndicador(){
		return getCriterioAvaliacaoIndicadorVOs().size();
	}
	
	

}
