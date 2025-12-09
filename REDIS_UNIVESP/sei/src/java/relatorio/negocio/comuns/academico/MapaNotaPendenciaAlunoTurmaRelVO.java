package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

/**
 * @author Wellington Rodrigues - 22 de jul de 2015
 *
 */
public class MapaNotaPendenciaAlunoTurmaRelVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private String identificadorTurma;
	private List<MapaNotaPendenciaAlunoDisciplinaRelVO> mapaNotaPendenciaAlunoDisciplinaRelVOs;

	public String getIdentificadorTurma() {
		if (identificadorTurma == null) {
			identificadorTurma = "";
		}
		return identificadorTurma;
	}

	public void setIdentificadorTurma(String identificadorTurma) {
		this.identificadorTurma = identificadorTurma;
	}

	public List<MapaNotaPendenciaAlunoDisciplinaRelVO> getMapaNotaPendenciaAlunoDisciplinaRelVOs() {
		if (mapaNotaPendenciaAlunoDisciplinaRelVOs == null) {
			mapaNotaPendenciaAlunoDisciplinaRelVOs = new ArrayList<MapaNotaPendenciaAlunoDisciplinaRelVO>(0);
		}
		return mapaNotaPendenciaAlunoDisciplinaRelVOs;
	}

	public void setMapaNotaPendenciaAlunoDisciplinaRelVOs(List<MapaNotaPendenciaAlunoDisciplinaRelVO> mapaNotaPendenciaAlunoDisciplinaRelVOs) {
		this.mapaNotaPendenciaAlunoDisciplinaRelVOs = mapaNotaPendenciaAlunoDisciplinaRelVOs;
	}
}
