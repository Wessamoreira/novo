package negocio.interfaces.administrativo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.GrupoLancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.LancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.TemplateLancamentoFolhaPagamentoVO;

@SuppressWarnings("rawtypes")
public interface FuncionarioCargoInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>FuncionarioCargoVO</code>.
	 */
	public FuncionarioCargoVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>FuncionarioCargoVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * @param obj  Objeto da classe <code>FuncionarioCargoVO</code> que será gravado no banco de dados.
	 * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void incluir(FuncionarioCargoVO obj) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>FuncionarioCargoVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>alterar</code> da superclasse.
	 * @param obj    Objeto da classe <code>FuncionarioCargoVO</code> que será alterada no banco de dados.
	 * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void alterar(FuncionarioCargoVO obj) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>FuncionarioCargoVO</code>.
	 * Sempre localiza o registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>excluir</code> da superclasse.
	 * @param obj    Objeto da classe <code>FuncionarioCargoVO</code> que será removido no banco de dados.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(FuncionarioCargoVO obj) throws Exception;

	public List consultarPorFuncionario(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCargo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>FuncionarioCargo</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>FuncionarioCargoVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;


        public List consultarPorNomeCargoUnico(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir todos os objetos da <code>FuncionarioCargoVO</code> no BD.
	 * Faz uso da operação <code>excluir</code> disponível na classe <code>FuncionarioCargo</code>.
	 * @param <code>funcionario</code> campo chave para exclusão dos objetos no BD.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void excluirFuncionarioCargos(Integer funcionario) throws Exception;

	/**
	 * Operação responsável por alterar todos os objetos da <code>FuncionarioCargoVO</code> contidos em um Hashtable no BD.
	 * Faz uso da operação <code>excluirFuncionarioCargos</code> e <code>incluirFuncionarioCargos</code> disponíveis na classe <code>FuncionarioCargo</code>.
	 * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void alterarFuncionarioCargos(FuncionarioVO funcionario, List objetos) throws Exception;

	/**
	 * Operação responsável por incluir objetos da <code>FuncionarioCargoVO</code> no BD.
	 * Garantindo o relacionamento com a entidade principal <code>administrativo.funcionario</code> através do atributo de vínculo.
	 * @param objetos List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void incluirFuncionarioCargos(Integer funcionarioPrm, List objetos) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe <code>FuncionarioCargoVO</code>
	 * através de sua chave primária.
	 * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public FuncionarioCargoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe.
	 * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
	 * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade);

        public FuncionarioCargoVO consultarPorCodigo(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

        public List<FuncionarioCargoVO> consultarPorNomeCargo(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

        public List<FuncionarioCargoVO> consultarPorNomeFuncionario(DataModelo dataModelo, String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

        public List<FuncionarioCargoVO> consultarPorNomeFuncionarioUnidadeEnsinoSituacao(String valorConsulta, Integer unidadeEnsino, Boolean ativo, Boolean consultor, int nivelMontarDados, UsuarioVO usuario) throws Exception;

        public List consultarPorNomeCargoUnicoUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;

        public List<FuncionarioCargoVO> consultarPorNomeCargoUnidadeEnsinoSituacao(String valorConsulta, Integer unidadeEnsino, Boolean ativo, Boolean consultor, int nivelMontarDados, UsuarioVO usuario) throws Exception;

        public Boolean verificaUsuarioDepartamento(Integer codigoDepartamento, Integer usuario) throws Exception;

		List consultarFuncionarioCargos(Integer funcionario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

		void alterarAtivo(FuncionarioCargoVO funcionarioCargoVO, Integer funcionario, UsuarioVO usuarioVO) throws Exception;

		List<FuncionarioCargoVO> consultarCargoPorCodigoFuncionario(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
		
		public List<FuncionarioCargoVO> consultarFuncionarioCargoAtivoParaRH(DataModelo dataModelo, String situacaoFuncionario) throws Exception;
		public List consultarFuncionarioCargoAtivoParaRH(DataModelo dataModel, String situacaoFuncionario, boolean consultarSomenteProfessores) throws Exception;
		
		public StringBuilder getSQLFuncionarioCargo();
		public StringBuilder getSQLTotalizadorFuncionarioCargo();
		
		public FuncionarioCargoVO consultarPorMatriculaCargo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
		
		public List<FuncionarioCargoVO> consultarPorMatriculaCargo(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;
		
		public FuncionarioCargoVO consultarPorChavePrimariaUnica(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
		
		public String consultarMatriculaPorFuncionarioCargo(Integer codigo, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;

		public List<FuncionarioCargoVO> consultarCargoFuncionarioPorFiltrosLancamentoFolhaPagamento(LancamentoFolhaPagamentoVO lancamento);

		public List<FuncionarioCargoVO> consultarCargoFuncionarioPorFiltrosGrupoLancamentoFolhaPagamento(GrupoLancamentoFolhaPagamentoVO grupoLancamento);
		
		public List<FuncionarioCargoVO> consultarCargoFuncionarioPorFiltrosTemplateFolhaPagamento(TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamento, Integer nivelMontarDados) throws Exception;
		
		public void montarClausulaWhereComSituacaoFuncionarioCargo(String situacaoFuncionario, StringBuilder where);
		
		public Integer consultarTotalPorFuncionarioCargo(DataModelo dataModelo, String situacaoFuncionario);
		public Integer consultarTotalPorFuncionarioCargo(DataModelo dataModelo, String situacaoFuncionario, boolean consultarSomenteProfessores);
		
		public StringBuilder filtroFuncionarioCargoAtivoParaRH(DataModelo dataModelo, String situacaoFuncionario, boolean totalizador);
		public StringBuilder filtroFuncionarioCargoAtivoParaRH(DataModelo dataModelo, String situacaoFuncionario, boolean totalizador, boolean consultarSomenteProfessores);
		
		/**
		 * Altera a situacao do funcionario e o status caso a situacao seja marcada como Demitido ou Ferias
		 * 
		 * @param funcionarioCargoVO
		 * @param funcionario
		 * @param usuarioVO
		 * @throws Exception
		 */
		void alterarSituacaoFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO, UsuarioVO usuarioVO) throws Exception;

		void alterarSituacaoFuncionario(Integer codigoFuncionarioCargo, String situacao, UsuarioVO usuarioVO) throws Exception;
		

		/**
		 * Consulta lista com FuncionarioCargoVO pelos filtros
		 *  
		 * @param situacao
		 * @param utilizaRH
		 * @return
		 * @throws Exception
		 */
		public List<FuncionarioCargoVO> consultarListaFuncionarioPorSituacao(SituacaoFuncionarioEnum situacao, Boolean utilizaRH, int nivelMontarDados) throws Exception;
		
		public List<FuncionarioCargoVO> consultarPorMatriculaCargoAtivo(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;
		
		public List<FuncionarioCargoVO> consultarPorNomeFuncionarioAtivo(DataModelo dataModelo, int nivelMontarDados, UsuarioVO usuario) throws Exception ;

		public Integer consultarTotalPorNomeFuncionarioAtivo(DataModelo dataModeloFuncionarioCargo, int nivelmontardadosCombobox, UsuarioVO usuarioLogado);

		public void alterarSituacaoFuncionarioDataDemissao(Integer codigoFuncionarioCargo, Date dataDemisssao, String situacaoFuncionario, UsuarioVO usuarioVO) throws Exception;

		public List<Map<String, Object>> consultarPorSecao() throws Exception;
		public List<Map<String, Object>> consultarPorFormaContratacao() throws Exception;
		public List<Map<String, Object>> consultarPorSituacaoFuncionarioCargo() throws Exception ;
		public List<FuncionarioCargoVO> consultarTodosFuncionariosCargoAtivo(int nivelMontarDados, UsuarioVO usuario) throws Exception;

		public List<FuncionarioCargoVO> consultarPorPessoaESituacaoFuncionario(Integer codigo, SituacaoFuncionarioEnum ativo, int nivelmontardadosDadosbasicos, UsuarioVO usuarioLogado) throws Exception;

		public FuncionarioCargoVO consultarPorMatriculaCargoProfessor(String valorConsulta, boolean controlarAcesso,
				int nivelMontarDados, UsuarioVO usuario) throws Exception;

		public List<FuncionarioCargoVO> consultarPorNomeFuncionario(DataModelo dataModelo, String valorConsulta,
				int nivelMontarDados, UsuarioVO usuario, boolean consultarApenasProfessor) throws Exception;

		public List<FuncionarioCargoVO> consultarProfessoresCoordenador(String valorConsulta, String campoConsultaFuncionario, UsuarioVO usuarioLogado) throws Exception;

		Integer consultarTotalPorNomeFuncionarioAtivoProfessor(DataModelo dataModelo, int nivelMontarDados,
				UsuarioVO usuario, boolean professor);

		List<FuncionarioCargoVO> consultarPorNomeFuncionarioAtivoProfessor(DataModelo dataModelo, int nivelMontarDados,
				UsuarioVO usuario, boolean professor) throws Exception;
		
		List<FuncionarioCargoVO> consultarListaFuncionarioPorSituacaoDiferenteDeDemitidoLicencaSemVencimentoOutros(
				Boolean utilizaRH, int nivelMontarDados) throws Exception;
}