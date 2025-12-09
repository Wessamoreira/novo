package negocio.interfaces.academico;
import java.util.List;
import java.util.Map;

import negocio.comuns.academico.ControleLivroFolhaReciboVO;
import negocio.comuns.academico.ControleLivroRegistroDiplomaUnidadeEnsinoVO;
import negocio.comuns.academico.ControleLivroRegistroDiplomaVO;
import negocio.comuns.academico.enumeradores.TipoLivroRegistroDiplomaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface ControleLivroRegistroDiplomaInterfaceFacade {
	

    public ControleLivroRegistroDiplomaVO novo() throws Exception;
    public void incluir(ControleLivroRegistroDiplomaVO obj,List listaControleLivroFolhaReciboVOs, UsuarioVO usuario) throws Exception;
    public void alterar(final ControleLivroRegistroDiplomaVO obj, List listaControleLivroFolhaReciboVOs, UsuarioVO usuario) throws Exception;
    public void excluir(ControleLivroRegistroDiplomaVO obj, UsuarioVO usuario) throws Exception;
    public ControleLivroRegistroDiplomaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados , UsuarioVO usuario) throws Exception;
//    public Integer obterNumeroLivroRegistro(int unidadeEnsino, int curso, UsuarioVO usuario) throws Exception;    
    public List<ControleLivroRegistroDiplomaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados , UsuarioVO usuario) throws Exception;
    public List<ControleLivroRegistroDiplomaVO> consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados , UsuarioVO usuario) throws Exception;
    public List<ControleLivroRegistroDiplomaVO> consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados , UsuarioVO usuario) throws Exception;
    public ControleLivroRegistroDiplomaVO consultarPorChavePrimaria(int valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
//    public ControleLivroRegistroDiplomaVO consultarPorUnidadeEnsinoCurso(int unidadeEnsino, int curso, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
	ControleLivroRegistroDiplomaVO consultarPorCodigoCurso(Integer valorConsulta,  String niveEducacional,  TipoLivroRegistroDiplomaEnum tipoLivroRegistroDiplomaEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	public Integer consultarMaxNumeroRegistro(Integer valorConsulta, Boolean zerarPorCurso) throws Exception;
	public String obterNumeroRegistroMatricula(String matricula) throws Exception;
	public String obterNumeroRegistroMatriculaPrimeiraVia(String matricula) throws Exception;
	/** 
	 * @author Wellington - 2 de fev de 2016 
	 * @param valorConsulta
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	List<ControleLivroRegistroDiplomaVO> consultarPorNomeAluno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	/** 
	 * @author Wellington - 2 de fev de 2016 
	 * @param valorConsulta
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	List<ControleLivroRegistroDiplomaVO> consultarPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	/** 
	 * @author Wellington - 2 de fev de 2016 
	 * @param unidadeEnsino
	 * @param curso
	 * @param livro
	 * @param situacao
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	ControleLivroRegistroDiplomaVO consultarPorUnidadeEnsinoCursoLivro(List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs, int curso, int livro, String situacao, String tipoLivro, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	/** 
	 * @author Wellington - 3 de fev de 2016 
	 * @param unidadeEnsino
	 * @param curso
	 * @param situacao
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	Integer consultarPorUnidadeEnsinoCursoUltimoLivroCadastrado(int unidadeEnsino, int curso, String situacao, boolean controlarAcesso, int nivelMontarDados, String nivelEducacional, UsuarioVO usuario) throws Exception;
	
	List<ControleLivroRegistroDiplomaVO> consultarPorTipoLivroRegistro(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<ControleLivroRegistroDiplomaVO> consultarPorNumeroLivroRegistro(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	public void validarNumeroRegistroMenorUltimo(ControleLivroRegistroDiplomaVO controleLivroRegistroDiplomaVO, List<ControleLivroFolhaReciboVO> listaControleLivroFolhaRecibo) throws ConsistirException;
	public void validarFolhaReciboAtualMenorUltimo(ControleLivroRegistroDiplomaVO controleLivroRegistroDiplomaVO, List<ControleLivroFolhaReciboVO> listaControleLivroFolhaRecibo) throws ConsistirException;
	public List<ControleLivroRegistroDiplomaVO> consultarPorRegistroAcademico(String valorConsulta, boolean b,	int nivelmontardadosTodos, UsuarioVO usuarioLogado) throws Exception;
	
	public String consultarUnidadesVinculadas(Integer codigo) throws Exception;
	
	public Map<String, String> obterDadosLivroPorMatricula(String matricula, String via) throws Exception;
	
}