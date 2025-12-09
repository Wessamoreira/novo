package negocio.interfaces.faturamento.nfe;

import java.math.BigDecimal;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.UnidadeEnsinoCursoValoresGinfesVO;

public interface UnidadeEnsinoCursoValoresGinfesInterfaceFacade {

	UnidadeEnsinoCursoValoresGinfesVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

	List<UnidadeEnsinoCursoValoresGinfesVO> consultaRapidaPorUnidadeEnsinoCurso(Integer unidadeEnsinoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

	void excluir(UnidadeEnsinoCursoValoresGinfesVO obj, boolean verificarAcesso, UsuarioVO usuario);	

	void adicionarUnidadeEnsinoCursoValoresGinfes(List<UnidadeEnsinoCursoValoresGinfesVO> listaUnidadeEnsinoCursoValoresGinfesVOs, UnidadeEnsinoCursoValoresGinfesVO obj, UsuarioVO usuario);

	void removerUnidadeEnsinoCursoValoresGinfes(List<UnidadeEnsinoCursoValoresGinfesVO> listaUnidadeEnsinoCursoValoresGinfesVOs, UnidadeEnsinoCursoValoresGinfesVO obj, UsuarioVO usuario);

	void persistir(UnidadeEnsinoCursoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void reajustarValoresCursoGinfes(UnidadeEnsinoCursoVO obj, Integer anoCompetenciaAtual, Integer semestreCompetenciaAtual, BigDecimal percentualReajustePorCompetencia, String periodicidadeCurso, boolean reajustePorCompetenciaGeral, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

}
