package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.VagaTurmaDisciplinaVO;
import negocio.comuns.academico.VagaTurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface VagaTurmaDisciplinaInterfaceFacade {

	public VagaTurmaDisciplinaVO novo() throws Exception;

	public void incluir(VagaTurmaDisciplinaVO obj) throws Exception;

	public void alterar(VagaTurmaDisciplinaVO obj) throws Exception;

	public void excluir(VagaTurmaDisciplinaVO obj) throws Exception;

	public void excluirTurmaDisciplinas(Integer turma) throws Exception;
	
	public void incluirTurmaDisciplinas(VagaTurmaVO turma, List<VagaTurmaDisciplinaVO> objetos) throws Exception;
	
	public void alterarTurmaDisciplinas(VagaTurmaVO vagaturma, List<VagaTurmaDisciplinaVO> objetos) throws Exception;

	public void excluirTurmaDisciplinas(Integer turma, List<VagaTurmaDisciplinaVO> objetos) throws Exception;

	public List<VagaTurmaDisciplinaVO> consultarPorDisciplina(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<VagaTurmaDisciplinaVO> consultarPorCodigoTurma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public VagaTurmaDisciplinaVO consultarPorCodigoTurmaCodigoDisciplina(Integer turma, Integer disciplina, String ano, String semestre,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<VagaTurmaDisciplinaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;	

	public List<VagaTurmaDisciplinaVO> consultarTurmaDisciplinas(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public VagaTurmaDisciplinaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String idEntidade);


	public void excluirPorCodigoDisciplinaTurma(Integer turma, Integer disciplina) throws Exception;
    public List<VagaTurmaDisciplinaVO> consultaRapidaPorTurma(Integer turmaSugerida, Integer turmaPrincipal, UsuarioVO usuarioVO);

	void alterarVagaTurmaDisciplinaPorAlteracaoGradeCurricularCursoIntegral(Integer turma, Integer disciplina, Integer novaDisciplina, UsuarioVO usuario) throws Exception;
	
	Integer consultarQtdeVagaDisciplinaPorGradeDisciplina(Integer gradeDisciplina, UsuarioVO usuarioVO);

}