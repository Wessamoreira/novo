package negocio.comuns.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

public class HistoricoEquivalenteVO extends SuperVO implements Serializable {

	private Integer codigo;
	private MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO;
	private List<HistoricoVO> historicosPorEquivalencia;
	private List<HistoricoVO> historicosEquivalentes;
	private static final long serialVersionUID = 1L;

	public HistoricoEquivalenteVO() {
		super();
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = new Integer(0);
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public MapaEquivalenciaDisciplinaVO getMapaEquivalenciaDisciplinaVO() {
		if (mapaEquivalenciaDisciplinaVO == null) {
			mapaEquivalenciaDisciplinaVO = new MapaEquivalenciaDisciplinaVO();
		}
		return mapaEquivalenciaDisciplinaVO;
	}

	public void setMapaEquivalenciaDisciplinaVO(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO) {
		this.mapaEquivalenciaDisciplinaVO = mapaEquivalenciaDisciplinaVO;
	}

	public List<HistoricoVO> getHistoricosPorEquivalencia() {
		if (historicosPorEquivalencia == null) {
			historicosPorEquivalencia = new ArrayList<HistoricoVO>(0);
		}
		return historicosPorEquivalencia;
	}

	public void setHistoricosPorEquivalencia(List<HistoricoVO> historicosPorEquivalencia) {
		this.historicosPorEquivalencia = historicosPorEquivalencia;
	}

	public List<HistoricoVO> getHistoricosEquivalentes() {
		if (historicosEquivalentes == null) {
			historicosEquivalentes = new ArrayList<HistoricoVO>(0);
		}
		return historicosEquivalentes;
	}

	public void setHistoricosEquivalentes(List<HistoricoVO> historicosEquivalentes) {
		this.historicosEquivalentes = historicosEquivalentes;
	}

}
