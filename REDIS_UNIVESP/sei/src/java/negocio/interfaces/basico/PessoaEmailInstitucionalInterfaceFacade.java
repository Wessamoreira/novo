package negocio.interfaces.basico;

import java.util.List;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.ConfiguracaoLdapVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineVO;

public interface PessoaEmailInstitucionalInterfaceFacade {

	void persistir(PessoaVO pessoa, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	PessoaEmailInstitucionalVO incluirPessoaEmailInstitucional(ConfiguracaoLdapVO conf, PessoaEmailInstitucionalVO pessoaEmailInstitucionalExistente , Boolean criarNovoPessoaEmailInstitucional, UsuarioVO usuario ,UsuarioVO usuarioLogado) throws Exception;

	List<PessoaEmailInstitucionalVO> consultarAlunosDoEadTurmaDisciplinaDisponivel(TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre, Integer bimestre, ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, boolean isSomentePessoaGsuite, UsuarioVO usuarioVO) throws Exception;

	List<PessoaEmailInstitucionalVO> consultarAlunosDoHorarioTurmaDisciplinaDisponivel(Integer unidadeEnsino, Integer curso, TurmaVO turma, Integer disciplina, String ano, String semestre, Integer codigoProfessor, boolean isSomentePessoaGsuite, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

	PessoaEmailInstitucionalVO consultarPorPessoa(Integer pessoa, int nivelMontarDados, UsuarioVO usuario);
	
	List<PessoaEmailInstitucionalVO> consultarListaPessoaEmailInstitucionalPorPessoa(Integer pessoa, int nivelMontarDados, UsuarioVO usuario);

	List<PessoaEmailInstitucionalVO> consultar(String campoConsulta, String valorConsulta, int nivelMontarDados,
			UsuarioVO usuarioLogado);

	PessoaEmailInstitucionalVO consultarPorEmail(String email, int nivelMontarDados, UsuarioVO usuario);

	PessoaEmailInstitucionalVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario);
	
	List<PessoaEmailInstitucionalVO> consultarPorPessoaFuncionario(Integer pessoa, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<PessoaEmailInstitucionalVO> consultarPorPessoaFuncionarioteste(int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void alterarSituacaoStatusAtivoInativoEnum(PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO, UsuarioVO usuarioVO) throws Exception;

	PessoaEmailInstitucionalVO consultarPorMatricula(String matricula, int nivelMontarDados, UsuarioVO usuario);

	PessoaEmailInstitucionalVO consultarPorPessoaPrivilegiandoRegistroAcademicoDominio(Integer pessoa,Integer configuracaoLdap, Boolean validarEmailInstitucionalAtivo ,int nivelMontarDados, UsuarioVO usuario);
	
	void preencherPessoaEmailInstitucional(PessoaVO pessoa, UsuarioVO usuarioVO) throws Exception;

	void persistirPessoaEmailInstitucional(PessoaVO pessoa, PessoaEmailInstitucionalVO obj, boolean verificarAcesso,
			UsuarioVO usuarioVO)throws Exception;
	
	void persistirListaPessoaEmailInstitucional(PessoaVO pessoa, List<PessoaEmailInstitucionalVO> lista,
			boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
}
