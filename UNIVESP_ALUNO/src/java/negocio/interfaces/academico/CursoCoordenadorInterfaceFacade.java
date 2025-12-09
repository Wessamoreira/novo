package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CursoCoordenadorVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

public interface CursoCoordenadorInterfaceFacade {

    public void validarDados(CursoCoordenadorVO obj, UsuarioVO usuario) throws ConsistirException;

    public void incluir(final CursoCoordenadorVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(final CursoCoordenadorVO obj, UsuarioVO usuario) throws Exception;

    public void excluir(CursoCoordenadorVO obj, UsuarioVO usuario) throws Exception;

    public List<CursoCoordenadorVO> consultarPorIdentificadorTurma(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoCoordenadorVO> consultarPorNomeCurso(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public CursoCoordenadorVO consultarPorMatriculaAluno(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public CursoCoordenadorVO consultarPorCursoTurma(Integer curso, Integer turma, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoCoordenadorVO> consultarPorCodigoCurso(Integer valorConsulta, boolean verificarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoCoordenadorVO> consultarPorCodigoCursoUnidadeEnsino(Integer valorConsulta, Integer funcionarioCodigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoCoordenadorVO> consultarPorCodigoCursoFuncionarioNivelEducacional(Integer valorConsulta, Integer funcionarioCodigo, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoCoordenadorVO> consultarPorCodigoCursoUnidadeEnsinoPeriodicidade(Integer valorConsulta, Integer unidadeEnsinoCodigo, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoCoordenadorVO> consultarPorNomeCursoFuncionario(String valorConsulta, Integer unidadeEnsinoCodigo, boolean b, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoCoordenadorVO> consultarPorNomeCursoUnidadeEnsinoNivelEducacional(String valorConsulta, Integer unidadeEnsinoCodigo, String nivelEducacional, boolean b, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void excluirCursoCoordenador(Integer curso, UsuarioVO usuario) throws Exception;

    public void excluirCursoCoordenador(Integer curso, List<CursoCoordenadorVO> objetos, UsuarioVO usuario) throws Exception;

    public void excluir(Integer codigoCursoCoordenador, UsuarioVO usuario) throws Exception;

    public void alterarCursoCoordenador(Integer curso, List<CursoCoordenadorVO> objetos, UsuarioVO usuario) throws Exception;

    public void incluirCursoCoordenador(Integer curso, List<CursoCoordenadorVO> objetos, UsuarioVO usuario) throws Exception;

    public List<CursoCoordenadorVO> consultarCursoCoordenadors(Integer curso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public CursoCoordenadorVO consultarPorChavePrimaria(Integer cursoPrm, Integer unidadeEnsinoPrm, Integer turnoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public CursoCoordenadorVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void carregarDados(CursoCoordenadorVO obj, UsuarioVO usuario) throws Exception;

    public void carregarDados(CursoCoordenadorVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

    public CursoCoordenadorVO consultaRapidaPorCursoFuncionarioTurma(Integer cursoPrm, Integer funcionarioPrm, Integer turmaPrm, UsuarioVO usuario) throws Exception;

    public void gravarCursoCoordenadorVOs(CursoVO obj, UsuarioVO usuario) throws Exception;

    public boolean validarExisteCoordenadorMaisDeUmCurso(Integer codigoPessoa, UsuarioVO usuario) throws Exception;

    public List<TipoNivelEducacional> consultarNivelEducacionalCursosCoordenador(Integer codigoPessoa, UsuarioVO usuario) throws Exception;
	
    public List<CursoCoordenadorVO> consultarPorCodigoMatriculaPeriodoTurmaDisciplina(Integer codMatPerTurDisc, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public boolean consultarPorFuncionarioUnidadeEnsinoTurma (UsuarioVO usuario, MatriculaPeriodoVO matricula) throws Exception;

	List<CursoCoordenadorVO> consultarPorPessoaUnidadeEnsinoNivelEducacionalCurso(Integer pessoa, Integer unidadeEnsino, String nivelEducacional, Integer curso, Integer turma, Boolean coordenadorGealAmbos, UsuarioVO usuario) throws Exception;

	boolean consultarSeExisteCoordenadorPorUsuario(UsuarioVO usuario, CursoVO curso, TurmaVO turma) throws Exception;
	
	public void alterarFuncionarioCursoCoordenadorUnificacaoFuncionario(Integer funcionarioAntigo, Integer funcionarioNovo) throws Exception;
	
	public List<CursoCoordenadorVO> consultarPorCodigoCursoECodigoUnidadeEnsino(int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<CursoCoordenadorVO> consultarPorPessoaUnidadeEnsinoNivelEducacionalCurso(Integer pessoa, Integer unidadeEnsino, String nivelEducacional, Integer curso, Integer turma, Boolean coordenadorGealAmbos, Boolean apresentarCoordenadorSemUnidadeEnsino, UsuarioVO usuario) throws Exception;
}
