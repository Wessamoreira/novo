package negocio.interfaces.arquitetura;

import java.util.List;

import controle.arquitetura.LoginControle;
import jakarta.faces.context.FacesContext;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoEnumInterface;
import negocio.comuns.utilitarias.AcessoException;

public interface ControleAcessoInterfaceFacade {


//	public PerfilAcessoVO getPerfilAcesso();
//
//	public void setPerfilAcesso(PerfilAcessoVO aPerfilAcesso);
	

	/**
	 * Operação padrão para realizar o CONSULTAR de dados de uma entidade no BD. Verifica e inicializa (se necessário) a conexão com o BD. Verifica se o usuário logado possui permissão de acesso a operação CONSULTAR.
	 * 
	 * @param idEntidade
	 *            Nome da entidade para a qual se deseja realizar a operação.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso a esta operação.
	 */
	void consultar(String idEntidade, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	void incluir(String idEntidade, UsuarioVO usuario) throws Exception;

	void incluir(String idEntidade, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	boolean verificarPermissaoFuncionalidadePorUsernameSenhaUnidadeEnsino(String funcionalidade, String username,
			String senha, Integer unidadeEnsino, Boolean retornarExcexao) throws Exception;

	void validarSeRegistroForamExcluidoDasListaSubordinadas(List<? extends SuperVO> lista, String tabelaSubordinada,
			String nomeEntidade, Integer codigoEntidade, UsuarioVO usuario) throws Exception;

	/**
	 * Operação padrão para realizar o ALTERAR de dados de uma entidade no BD. Verifica e inicializa (se necessário) a conexão com o BD. Verifica se o usuário logado possui permissão de acesso a operação ALTERAR.
	 * 
	 * @param idEntidade
	 *            Nome da entidade para a qual se deseja realizar a operação.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso a esta operação.
	 */
	void alterar(String idEntidade, UsuarioVO usuario) throws Exception;

	void alterar(String idEntidade, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	void verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(String funcionalidade, UsuarioVO usuario)
			throws Exception;

	/**
	 * Operação responsável por verificar se um usuário existe (através de ser Username e Senha) e retornar os dados deste usuário para registro na sessão da aplicação. Caso o usuário não exista é retornado um Exception. da operação </code>verificarPermissaoOperacao(PermissaoVO permissao, int operacao)</code>.
	 * 
	 * @param username
	 *            Username do usuário.
	 * @param senha
	 *            Senha do usuário.
	 * @return UsuarioVO Dados do usuário localizado.
	 * @exception Exception
	 *                Erro alertando que o usuário não existe.
	 */
	UsuarioVO verificarLoginUsuario(String username, String senha, boolean encriptar, int nivelMontarDados)
			throws Exception;

	/**
	 * Operação padrão para verificar acesso do usuário a determinada funcionalidade registrada no Perfil de Acesso do Usuário. Pode ser, por exemplo, alterar um valor de um campo. Neste caso deverá existir no PerfilAcesso do usuário o key (apelido) identificando esta funcionalidade. Esta funcinalidade deverá estar gravada com a opção TOTAL ou ALTERAR para garantir que o usuário tenha acesso à mesma.
	 * 
	 * @param funcionalidade
	 *            Nome da funcionalidade para a qual se deseja realizar a verificação de permissão do usuário.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso a esta operação.
	 */	
	Boolean verificarPermissaoUsuarioFuncionalidade(String funcionalidade, UsuarioVO usuario) throws Exception;

	void verificarPermissaoUsuarioFuncionalidadeComUsuarioVO(String funcionalidade, UsuarioVO usuario) throws Exception;

	void verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(String funcionalidade, UsuarioVO usuario)
			throws Exception;

	/**
	 * Operação padrão para realizar o EXCLUIR de dados de uma entidade no BD. Verifica e inicializa (se necessário) a conexão com o BD. Verifica se o usuário logado possui permissão de acesso a operação EXCLUIR.
	 * 
	 * @param idEntidade
	 *            Nome da entidade para a qual se deseja realizar a operação.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso a esta operação.
	 */
	void excluir(String idEntidade, UsuarioVO usuario) throws Exception;

	void excluir(String idEntidade, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	void verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(
			PerfilAcessoPermissaoEnumInterface perfilAcessoEnum, UsuarioVO usuario) throws AcessoException;

	Boolean verificarPermissaoFuncionalidadeUsuario(String funcionalidade, UsuarioVO usuario) throws Exception;	

	boolean verificarPermissaoOperacao(String nomeEntidade, Integer operacao, UsuarioVO usuarioVO);

	UsuarioVO verificarLoginUsuarioSimulacaoVisaoAluno(Integer codigoPessoa, Boolean exceptionUsuarioNaoExistente)
			throws Exception;


//	Boolean incluirOuAlterar(String idEntidade, UsuarioVO usuario) throws Exception;

	/**
	 * Operação padrão para verificar acesso do usuário a determinada funcionalidade registrada no Perfil de Acesso do Usuário. Pode ser, por exemplo, alterar um valor de um campo. Neste caso deverá existir no PerfilAcesso do usuário o key (apelido) identificando esta funcionalidade. Esta funcinalidade deverá estar gravada com a opção TOTAL ou ALTERAR para garantir que o usuário tenha acesso à mesma.
	 * 
	 * @param funcionalidade
	 *            Nome da funcionalidade para a qual se deseja realizar a verificação de permissão do usuário.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso a esta operação.
	 */
	void verificarPermissaoUsuarioFuncionalidade(UsuarioVO usuario, String funcionalidade) throws Exception;

	boolean verificarPermissaoOperacaoUnificacaoPerfil(Integer usuario, String nomeEntidade, Integer operacao);

	UsuarioVO verificarLoginUsuarioParaControleFalhaLogin(String username) throws Exception;

	UsuarioVO verificarLoginUsuarioSimulacaoSuporte(String username, String senha, boolean encriptar,
			int nivelMontarDados) throws Exception;

//	UsuarioVO verificarLoginUsuarioEmailInstitucional(String emailInstitucional, int nivelMontarDados) throws Exception;

//	UsuarioVO verificarLoginUsuarioEmailInstitucional(String emailInstitucional, int nivelMontarDados, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	UsuarioVO verificarLoginAdministrador(String username, String senha, boolean encriptar) throws Exception;

	
	void emitirRelatorio(String idEntidade, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

//	UsuarioVO realizarVerificacaoSeLoginSeraRealizadoPeloLDAP(String username, int nivelMontarDados) throws Exception;

	public boolean verificarPermissaoOperacao(Integer perfilAcesso, String nomeEntidade, Integer operacao);

//	public void validarLoginBlackboardViaSei(LoginControle loginControle, FacesContext facesContext);

//	void validarLoginSistemaViaToken(LoginControle loginControle, FacesContext facesContext, String tokenLogin,
//			String ambienteLogin);

//	public PerfilAcessoVO getPerfilAcesso();
//
//	public void setPerfilAcesso(PerfilAcessoVO aPerfilAcesso);




}