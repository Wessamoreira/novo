package negocio.interfaces.ead;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.ParametrosGraficoComparativoAvaliacoesOnlinesVO;

public interface ParametrosGraficoComparativoAvaliacoesOnlinesInterfaceFacade {

	List<ParametrosGraficoComparativoAvaliacoesOnlinesVO> consultarPercentualAcertosComparativoAvaliacoesOnlines(Integer codigoMatriculaPeriodoTurmaDisciplina, Integer codigoTemaAssunto, UsuarioVO usuarioVO) throws Exception;

}
