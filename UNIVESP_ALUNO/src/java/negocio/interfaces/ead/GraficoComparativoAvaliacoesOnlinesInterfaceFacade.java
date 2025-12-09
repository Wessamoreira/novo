package negocio.interfaces.ead;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.GraficoComparativoAvaliacoesOnlinesVO;

public interface GraficoComparativoAvaliacoesOnlinesInterfaceFacade {

	List<GraficoComparativoAvaliacoesOnlinesVO> consultarPercentualAcertosComparativoAvaliacoesOnlines(Integer codigoMatriculaPeriodoTurmaDisciplina, Integer codigoTemaAssunto, UsuarioVO usuarioVO) throws Exception;

}
