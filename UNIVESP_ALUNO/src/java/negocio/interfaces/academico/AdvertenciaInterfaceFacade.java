package negocio.interfaces.academico;

import java.util.List;
import java.util.Map;

import negocio.comuns.academico.AdvertenciaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface AdvertenciaInterfaceFacade {

	public Map<String, Integer> consultarPorMatriculaNrAdvertenciasPeriodo(String matricula, String ano, String semestre, UsuarioVO usuarioVO) throws Exception;

	public List<AdvertenciaVO> consultarAdvertenciaPorNomeAluno(String nome, Boolean controlarAcesso, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;

	void persistir(AdvertenciaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(AdvertenciaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<AdvertenciaVO> consultarPorDescricaoTipoAdvertencia(String descricao, Boolean controlarAcesso, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;

	public List<AdvertenciaVO> consultarAdvertenciaPorMatricula(String matricula, String ano, Boolean controlarAcesso, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;

	public List<AdvertenciaVO> consultaAdvertenciaVisaoAluno(String matricula, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;

	public List<AdvertenciaVO> consultarAdvertenciaPorTurmaVisaoProfessor(String matricula, String nomeAluno, Integer professorLogado, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;

	public List<AdvertenciaVO> consultarAdvertenciaVisaoCoordenador(String matricula, String nomeAluno, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;

	public void executarEnvioMensagenAdvertencia(AdvertenciaVO advertenciaVO, UsuarioVO usuarioVO) throws Exception;
	
	public AdvertenciaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

}
