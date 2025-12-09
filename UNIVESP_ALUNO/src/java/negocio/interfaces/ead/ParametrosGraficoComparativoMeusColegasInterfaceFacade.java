package negocio.interfaces.ead;

import java.util.List;

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.ParametrosGraficoComparativoMeusColegasVO;

public interface ParametrosGraficoComparativoMeusColegasInterfaceFacade {

	List<ParametrosGraficoComparativoMeusColegasVO> consultarParametrosGraficoComparativoMeusColegasVO(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Integer codigoCurso, Integer codigoTemaAssunto, UsuarioVO usuarioVO) throws Exception;

}
