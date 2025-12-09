package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

public class CriterioAvaliacaoPeriodoLetivoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7138993016679967697L;
	private Integer codigo;
	private CriterioAvaliacaoVO criterioAvaliacaoVO;
	private PeriodoLetivoVO periodoLetivoVO;
	private List<CriterioAvaliacaoDisciplinaVO> criterioAvaliacaoDisciplinaVOs;
	private List<CriterioAvaliacaoNotaConceitoVO> criterioAvaliacaoNotaConceitoVOs;
	private List<CriterioAvaliacaoNotaConceitoVO> criterioAvaliacaoNotaConceitoVO1s;
	private List<CriterioAvaliacaoNotaConceitoVO> criterioAvaliacaoNotaConceitoVO2s;
	private List<CriterioAvaliacaoNotaConceitoVO> criterioAvaliacaoNotaConceitoVO3s;
	private List<CriterioAvaliacaoIndicadorVO> criterioAvaliacaoIndicadorVOs;
	//	transientes
	private Integer qtdeIndicador1Bimestre;
	private Integer qtdeIndicador2Bimestre;
	private Integer qtdeIndicador3Bimestre;
	private Integer qtdeIndicador4Bimestre;
	private String observacao;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public CriterioAvaliacaoVO getCriterioAvaliacaoVO() {
		if (criterioAvaliacaoVO == null) {
			criterioAvaliacaoVO = new CriterioAvaliacaoVO();
		}
		return criterioAvaliacaoVO;
	}

	public void setCriterioAvaliacaoVO(CriterioAvaliacaoVO criterioAvaliacaoVO) {
		this.criterioAvaliacaoVO = criterioAvaliacaoVO;
	}

	public PeriodoLetivoVO getPeriodoLetivoVO() {
		if (periodoLetivoVO == null) {
			periodoLetivoVO = new PeriodoLetivoVO();
		}
		return periodoLetivoVO;
	}

	public void setPeriodoLetivoVO(PeriodoLetivoVO periodoLetivoVO) {
		this.periodoLetivoVO = periodoLetivoVO;
	}

	public List<CriterioAvaliacaoDisciplinaVO> getCriterioAvaliacaoDisciplinaVOs() {
		if (criterioAvaliacaoDisciplinaVOs == null) {
			criterioAvaliacaoDisciplinaVOs = new ArrayList<CriterioAvaliacaoDisciplinaVO>(0);
		}
		return criterioAvaliacaoDisciplinaVOs;
	}

	public void setCriterioAvaliacaoDisciplinaVOs(List<CriterioAvaliacaoDisciplinaVO> criterioAvaliacaoDisciplinaVOs) {
		this.criterioAvaliacaoDisciplinaVOs = criterioAvaliacaoDisciplinaVOs;
	}

	public List<CriterioAvaliacaoNotaConceitoVO> getCriterioAvaliacaoNotaConceitoVOs() {
		if (criterioAvaliacaoNotaConceitoVOs == null) {
			criterioAvaliacaoNotaConceitoVOs = new ArrayList<CriterioAvaliacaoNotaConceitoVO>(0);
		}
		return criterioAvaliacaoNotaConceitoVOs;
	}

	public void setCriterioAvaliacaoNotaConceitoVOs(List<CriterioAvaliacaoNotaConceitoVO> criterioAvaliacaoNotaConceitoVOs) {
		this.criterioAvaliacaoNotaConceitoVOs = criterioAvaliacaoNotaConceitoVOs;
	}

	public List<CriterioAvaliacaoNotaConceitoVO> getCriterioAvaliacaoNotaConceitoVO1s() {
		if (criterioAvaliacaoNotaConceitoVO1s == null) {
			criterioAvaliacaoNotaConceitoVO1s = new ArrayList<CriterioAvaliacaoNotaConceitoVO>(0);
		}
		return criterioAvaliacaoNotaConceitoVO1s;
	}
	
	public void setCriterioAvaliacaoNotaConceitoVO1s(List<CriterioAvaliacaoNotaConceitoVO> criterioAvaliacaoNotaConceitoVO1s) {
		this.criterioAvaliacaoNotaConceitoVO1s = criterioAvaliacaoNotaConceitoVO1s;
	}
	
	public List<CriterioAvaliacaoNotaConceitoVO> getCriterioAvaliacaoNotaConceitoVO2s() {
		if (criterioAvaliacaoNotaConceitoVO2s == null) {
			criterioAvaliacaoNotaConceitoVO2s = new ArrayList<CriterioAvaliacaoNotaConceitoVO>(0);
		}
		return criterioAvaliacaoNotaConceitoVO2s;
	}
	
	public void setCriterioAvaliacaoNotaConceitoVO2s(List<CriterioAvaliacaoNotaConceitoVO> criterioAvaliacaoNotaConceitoVO2s) {
		this.criterioAvaliacaoNotaConceitoVO2s = criterioAvaliacaoNotaConceitoVO2s;
	}
	
	public List<CriterioAvaliacaoNotaConceitoVO> getCriterioAvaliacaoNotaConceitoVO3s() {
		if (criterioAvaliacaoNotaConceitoVO3s == null) {
			criterioAvaliacaoNotaConceitoVO3s = new ArrayList<CriterioAvaliacaoNotaConceitoVO>(0);
		}
		return criterioAvaliacaoNotaConceitoVO3s;
	}
	
	public void setCriterioAvaliacaoNotaConceitoVO3s(List<CriterioAvaliacaoNotaConceitoVO> criterioAvaliacaoNotaConceitoVO3s) {
		this.criterioAvaliacaoNotaConceitoVO3s = criterioAvaliacaoNotaConceitoVO3s;
	}

	public List<CriterioAvaliacaoIndicadorVO> getCriterioAvaliacaoIndicadorVOs() {
		if (criterioAvaliacaoIndicadorVOs == null) {
			criterioAvaliacaoIndicadorVOs = new ArrayList<CriterioAvaliacaoIndicadorVO>();
		}
		return criterioAvaliacaoIndicadorVOs;
	}

	public void setCriterioAvaliacaoIndicadorVOs(List<CriterioAvaliacaoIndicadorVO> criterioAvaliacaoIndicadorVOs) {
		this.criterioAvaliacaoIndicadorVOs = criterioAvaliacaoIndicadorVOs;
	}
	
	public Integer getOrdenacao(){
		return getPeriodoLetivoVO().getPeriodoLetivo();
	}
	
	public Integer getQtdeNotaConceito(){
		return getCriterioAvaliacaoNotaConceitoVOs().size();
	}
	
	public Integer getQtdeDisciplina(){
		return getCriterioAvaliacaoDisciplinaVOs().size();
	}
	
	public Integer getQtdeIndicador(){
		return getCriterioAvaliacaoIndicadorVOs().size();
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

	
	public void distribuirListagemNotaConceito() {
		if (!getCriterioAvaliacaoNotaConceitoVOs().isEmpty()) {
			int controlador = 1;
			Iterator<CriterioAvaliacaoNotaConceitoVO> i = getCriterioAvaliacaoNotaConceitoVOs().iterator();
			while (i.hasNext()) {
				CriterioAvaliacaoNotaConceitoVO obj = i.next();
				if (controlador == 1) {
					getCriterioAvaliacaoNotaConceitoVO1s().add(obj);
				} else if (controlador == 2) {
					getCriterioAvaliacaoNotaConceitoVO2s().add(obj);
				} else if (controlador == 3) {
					getCriterioAvaliacaoNotaConceitoVO3s().add(obj);
				}
				controlador++;
				if (controlador > 3) {
					controlador = 1;
				} 
			}
			
		}
		
	}

	public String getObservacao() {
		if (observacao == null) {
			observacao = "";
		}
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}


}
