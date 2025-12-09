package negocio.interfaces.academico;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.ColacaoGrauVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.academico.ProgramacaoFormaturaCursoVO;
import negocio.comuns.academico.ProgramacaoFormaturaVO;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface ProgramacaoFormaturaAlunoInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>ProgramacaoFormaturaAlunoVO</code>.
	 */
	public ProgramacaoFormaturaAlunoVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ProgramacaoFormaturaAlunoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProgramacaoFormaturaAlunoVO</code> que
	 *            será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void incluir(ProgramacaoFormaturaAlunoVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ProgramacaoFormaturaAlunoVO</code>. Sempre utiliza a chave primária
	 * da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProgramacaoFormaturaAlunoVO</code> que
	 *            será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void alterar(ProgramacaoFormaturaAlunoVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ProgramacaoFormaturaAlunoVO</code>. Sempre localiza o registro a
	 * ser excluído através da chave primária da entidade. Primeiramente
	 * verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProgramacaoFormaturaAlunoVO</code> que
	 *            será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(ProgramacaoFormaturaAlunoVO obj) throws Exception;



	public ProgramacaoFormaturaAlunoVO consultarColacaoPorMatricula(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir todos os objetos da
	 * <code>ProgramacaoFormaturaAlunoVO</code> no BD. Faz uso da operação
	 * <code>excluir</code> disponível na classe
	 * <code>ProgramacaoFormaturaAluno</code>.
	 * 
	 * @param <code>programacaoFormatura</code> campo chave para exclusão dos
	 *        objetos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public void excluirProgramacaoFormaturaAlunos(Integer programacaoFormatura) throws Exception;

	/**
	 * Operação responsável por alterar todos os objetos da
	 * <code>ProgramacaoFormaturaAlunoVO</code> contidos em um Hashtable no BD.
	 * Faz uso da operação <code>excluirProgramacaoFormaturaAlunos</code> e
	 * <code>incluirProgramacaoFormaturaAlunos</code> disponíveis na classe
	 * <code>ProgramacaoFormaturaAluno</code>.
	 * 
	 * @param objetos
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public void alterarProgramacaoFormaturaAlunos(Integer programacaoFormatura, List objetos, UsuarioVO usuario ) throws Exception;

	public void alterarProgramacaoFormaturaAlunosPorRegistroPresencaColacaoGrau(List<ProgramacaoFormaturaAlunoVO> objetos,  UsuarioVO usuario) throws Exception;

	

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>ProgramacaoFormaturaAlunoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public ProgramacaoFormaturaAlunoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade);

	public Boolean consultarSeExisteColacaoGrauParaMatricula(String matricula) throws Exception;

	public void inicializarDadosProgramacaoFormaturaAlunoParaExpedicaoDiploma(MatriculaVO matricula, ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO, ColacaoGrauVO colacaoGrauVO);

    void incluirProgramacaoFormaturaAlunos(Integer programacaoFormaturaPrm, List objetos, UsuarioVO usuario) throws Exception;
    
    public ProgramacaoFormaturaAlunoVO consultarPorMatriculaColacaoGrau(String matricula,Integer colacaoGrau, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public void excluirProgramacaoFormaturaAlunoVinculadaColacaoGrau(Integer colacaoGrau,UsuarioVO usuarioVO)throws Exception;
    
    public void removerVinculoProgramacaoFormatura(final ProgramacaoFormaturaVO programacaoFormaturaVO, UsuarioVO usuarioVO) throws Exception;
    
    public List<ProgramacaoFormaturaAlunoVO> consultarProgramacaoFormaturaAluno(Integer valorConsulta, DataModelo controleConsultaOtimize, String valorConsultaMatricula, String valorConsultaNomeAluno, String valorConsultaNomeCurso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public Boolean consultarExisteMatriculaEmProgramacaoFormatura(String matricula, Integer codigo) throws Exception;
	
	public void excluirAluno(ProgramacaoFormaturaAlunoVO obj, String matricula, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Operação responsável por consultar todos os <code>ProgramacaoFormaturaAlunoVO</code> relacionados a um objeto da classe <code>academico.ProgramacaoFormatura</code>.
	 * 
	 * @param programacaoFormatura
	 *            Atributo de <code>academico.ProgramacaoFormatura</code> a ser utilizado para localizar os objetos da classe <code>ProgramacaoFormaturaAlunoVO</code>.
	 * @return List Contendo todos os objetos da classe <code>ProgramacaoFormaturaAlunoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public List<ProgramacaoFormaturaAlunoVO> consultarProgramacaoFormaturaAlunos(Integer programacaoFormatura, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<ProgramacaoFormaturaAlunoVO> consultarProgramacaoFormaturaAlunoPorCurso(Integer codigoProgramacao, Integer codigoCurso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<ProgramacaoFormaturaAlunoVO> consultarProgramacaoFormaturaAlunoPorSemDocumentoAssinado(Integer codigoProgramacao, Integer codigoCurso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void alterarAlunoColouGrau(ProgramacaoFormaturaAlunoVO obj, UsuarioVO usuario) throws Exception;

	public ProgramacaoFormaturaAlunoVO consultarPorDocumentoAssinadorAluno(Integer codigoDocumentoAssinado, Integer codigoAluno, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void realizarAlteracaoColouGrau(DocumentoAssinadoVO documentoAssinado, Integer codigoAluno, SituacaoDocumentoAssinadoPessoaEnum situacao, UsuarioVO usuario) throws Exception;

	public Boolean consultarPossuiDiploma(String matricula) throws Exception;
	
	public List<MatriculaVO> consultarProgramacaoFormaturaMatriculaEnade(String matricula, Integer programacaoFormatura,FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	public void realizarMontagemProgramacaoFormaturaDuplicada(List<ProgramacaoFormaturaAlunoVO> listaProgramacaoFormatura, List<ProgramacaoFormaturaAlunoVO> listaProgramacaoFormaturaDuplicadas, UsuarioVO usuario) throws Exception;

	public void excluirPorMatriculaEProgramacaoFormatura(ProgramacaoFormaturaAlunoVO obj) throws Exception;
	
	public ProgramacaoFormaturaAlunoVO consultarPorMatriculaProgramacaoFormaturaAluno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void adicionarFiltroValidacaoAlunoColouGrauCondicaoWhere(StringBuilder sql, String aliasTabela, String aliasTabelaMatricula);

}