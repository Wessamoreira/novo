package negocio.interfaces.academico;

import java.util.List;
import java.util.Map;

import negocio.comuns.academico.MapaRegistroEvasaoCursoMatriculaPeriodoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.secretaria.MapaRegistroEvasaoCursoVO;


public interface MapaRegistroEvasaoCursoMatriculaPeriodoInterfaceFacade {

	void persistir(MapaRegistroEvasaoCursoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO);
	
	void executarOperacaoMapaRegistroEvasaoCurso(MapaRegistroEvasaoCursoMatriculaPeriodoVO mrecmp, boolean verificarAcesso, Map<Integer, ConfiguracaoGeralSistemaVO> configuracaoGeralSistemaVOs, Map<Integer, ConfiguracaoFinanceiroVO> configuracaoFinanceiroVOs, UsuarioVO usuarioVO) throws Exception;
	
	void executarEstornoMapaRegistroEvasaoCursoMatriculaPeriodo(MapaRegistroEvasaoCursoMatriculaPeriodoVO mrecmp, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<MapaRegistroEvasaoCursoMatriculaPeriodoVO> consultarPorMapaRegistroAbandonoCursoTrancamentoVO(MapaRegistroEvasaoCursoVO obj, int nivelMontarDados, UsuarioVO usuario);

}
