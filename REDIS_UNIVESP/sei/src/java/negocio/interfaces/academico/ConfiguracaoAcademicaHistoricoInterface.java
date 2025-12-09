package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ConfiguracaoAcademicaHistoricoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface ConfiguracaoAcademicaHistoricoInterface {

	public void persistir(ConfiguracaoAcademicaHistoricoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void alterar(ConfiguracaoAcademicaHistoricoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void excluir(ConfiguracaoAcademicaHistoricoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public ConfiguracaoAcademicaHistoricoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<ConfiguracaoAcademicaHistoricoVO> consultaRapidaAlterarConfiguracaoAcadHistorico(String matricula, Integer unidadeEnsino, Integer curso, Integer turma, Integer disciplina, Integer configuracaoAcademico, String ano, String semestre, UsuarioVO usuario) throws Exception;

	public void alterarConfiguracaoAcademicaHistoricoPorMatricula(ConfiguracaoAcademicaHistoricoVO configuracaoAcademicaHistoricoVO, UsuarioVO usuarioVO) throws Exception;

	public void alterarConfiguracaoAcademicaHistoricoVOs(List<ConfiguracaoAcademicaHistoricoVO> ConfiguracaoAcademicaHistoricoVOs, UsuarioVO usuarioVO) throws Exception;

}
