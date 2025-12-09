package negocio.interfaces.academico;

import java.util.List;
import java.util.Map;

import negocio.comuns.academico.GradeCurricularEstagioVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface GradeCurricularEstagioInterfaceFacade {
	
	public void validarDados(GradeCurricularEstagioVO gre);

	void persistir(List<GradeCurricularEstagioVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO);

	List<GradeCurricularEstagioVO> consultarPorGradeCurricularVO(GradeCurricularVO obj, int nivelMontarDados, UsuarioVO usuario);
	
	Integer consultarCargahorariaDeferidaEstagio(String matricula, Integer gradeCurricularEstagio) throws Exception;
	
	Boolean consultarSeExisteGradeCurricularEstagioPorGradeCurricular(Integer gradeCurricular) throws Exception;
	
	GradeCurricularEstagioVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario);
	
	public List<GradeCurricularEstagioVO> consultarPorGradeCurricularMatriculaHistoricoRel(Integer gradeCurricular, String matricula, int nivelMontarDados, UsuarioVO usuario);

	List<GradeCurricularEstagioVO> consultarGradeCurricularEstagioRelatorioSeiDecidir(UsuarioVO usuario);

	List<GradeCurricularEstagioVO> consultaRapidaPorNome(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<GradeCurricularEstagioVO> consultaRapidaPorCurso(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	Map<Integer, Integer> consultarCargahorariaDeferidaEstagio(String matricula) throws Exception;

}
