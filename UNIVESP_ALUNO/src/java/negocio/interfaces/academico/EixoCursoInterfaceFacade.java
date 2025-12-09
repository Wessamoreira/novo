package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.EixoCursoVO;

public interface EixoCursoInterfaceFacade {

	public EixoCursoVO novo() throws Exception;

	public void incluir(EixoCursoVO eixoCursoVO) throws Exception;

	public void alterar(EixoCursoVO eixoCursoVO) throws Exception;

	public void excluir(EixoCursoVO eixoCursoVO) throws Exception;
	
	public void persistir(EixoCursoVO eixoCursoVO) throws Exception;
	
	public EixoCursoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados) throws Exception;

    public List<EixoCursoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados) throws Exception;

    public List<EixoCursoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados) throws Exception;
    
    public List<EixoCursoVO> consultarTodos(int nivelMontarDados) throws Exception;

	EixoCursoVO consultarNomePorCodigo(String eixoCurso, int nivelMontarDados) throws Exception;
}
