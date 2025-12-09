package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface ComunicadoInternoDestinatarioInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>ComunicadoInternoDestinatarioVO</code>.
	 */
	public ComunicadoInternoDestinatarioVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>ComunicadoInternoDestinatarioVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * @param obj  Objeto da classe <code>ComunicadoInternoDestinatarioVO</code> que será gravado no banco de dados.
	 * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void incluir(ComunicadoInternoDestinatarioVO obj) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>ComunicadoInternoDestinatarioVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>alterar</code> da superclasse.
	 * @param obj    Objeto da classe <code>ComunicadoInternoDestinatarioVO</code> que será alterada no banco de dados.
	 * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void alterar(ComunicadoInternoDestinatarioVO obj) throws Exception;

	public void alterarRemoverCaixaEntrada(final Integer destinatario, final Integer comunicado, final Boolean remover, UsuarioVO usuario) throws Exception;       

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ComunicadoInternoDestinatarioVO</code>.
	 * Sempre localiza o registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>excluir</code> da superclasse.
	 * @param obj    Objeto da classe <code>ComunicadoInternoDestinatarioVO</code> que será removido no banco de dados.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(ComunicadoInternoDestinatarioVO obj) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>ComunicadoInternoDestinatario</code> através do valor do atributo 
	 * <code>nome</code> da classe <code>Pessoa</code>
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @return  List Contendo vários objetos da classe <code>ComunicadoInternoDestinatarioVO</code> resultantes da consulta.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomePessoa(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>ComunicadoInternoDestinatario</code> através do valor do atributo 
	 * <code>String tipoComunicadoInterno</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>ComunicadoInternoDestinatarioVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorTipoComunicadoInterno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>ComunicadoInternoDestinatario</code> através do valor do atributo 
	 * <code>codigo</code> da classe <code>ComunicacaoInterna</code>
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @return  List Contendo vários objetos da classe <code>ComunicadoInternoDestinatarioVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigoComunicacaoInterna(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limit, Integer offset) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>ComunicadoInternoDestinatario</code> através do valor do atributo 
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>ComunicadoInternoDestinatarioVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir todos os objetos da <code>ComunicadoInternoDestinatarioVO</code> no BD.
	 * Faz uso da operação <code>excluir</code> disponível na classe <code>ComunicadoInternoDestinatario</code>.
	 * @param <code>comunicadoInterno</code> campo chave para exclusão dos objetos no BD.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void excluirComunicadoInternoDestinatarios(Integer comunicadoInterno) throws Exception;

	/**
	 * Operação responsável por alterar todos os objetos da <code>ComunicadoInternoDestinatarioVO</code> contidos em um Hashtable no BD.
	 * Faz uso da operação <code>excluirComunicadoInternoDestinatarios</code> e <code>incluirComunicadoInternoDestinatarios</code> disponíveis na classe <code>ComunicadoInternoDestinatario</code>.
	 * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void alterarComunicadoInternoDestinatarios(ComunicacaoInternaVO comunicacaoInternaVO, List objetos, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Operação responsável por incluir objetos da <code>ComunicadoInternoDestinatarioVO</code> no BD.
	 * Garantindo o relacionamento com a entidade principal <code>administrativo.ComunicacaoInterna</code> através do atributo de vínculo.
	 * @param objetos List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void incluirComunicadoInternoDestinatarios(ComunicacaoInternaVO comunicacaoInternaVO, List objetos, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe <code>ComunicadoInternoDestinatarioVO</code>
	 * através de sua chave primária. 
	 * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public ComunicadoInternoDestinatarioVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe.
	 * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
	 * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
	 */
	public void setIdEntidade(String idEntidade);

        public void alterarComunicadoInternoDestinatarioLeituraObrigatoria(Integer codComunicacaoInterna, Integer codUsuarioLogado) throws Exception;

        public void alterarComunicadoInternoDestinatarioMarketingLida(Integer codComunicacaoInterna, Integer codUsuarioLogado) throws Exception;
        public void alterarMarcarComoLida(final Integer destinatario, final Integer comunicado, final Boolean remover, UsuarioVO usuarioLogado) throws Exception;


		void alterarComunicadoDestinatarioMensagemComoLidaERespondida(Integer codComunicacaoInterna, Integer codPessoaLogado) throws Exception;

		/** 
		 * @author Wellington Rodrigues - 11/06/2015 
		 * @param pessoaAntigo
		 * @param pessoaNova
		 * @throws Exception 
		 */
		void alterarPessoaUnificacaoFuncionario(Integer pessoaAntigo, Integer pessoaNova) throws Exception;

		Integer consultarTotalRegistroPorCodigoComunicacaoInterna(Integer valorConsulta) throws Exception;

		ComunicadoInternoDestinatarioVO consultarPorComunicadoInterno(Integer comunicadoInterno,
				Integer nivelMontarDados, UsuarioVO usuario) throws Exception;
}