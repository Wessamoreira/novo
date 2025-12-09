package negocio.interfaces.arquitetura;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.ConfiguracaoLdapVO;
import negocio.comuns.arquitetura.RegistroLdapVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.facade.jdbc.arquitetura.Ldap.SituacaoRetornoLdapEnum;

public interface LdapInterfaceFacade {

	boolean consultarSeUsuarioExisteLdap(ConfiguracaoLdapVO conf, UsuarioVO usuario) throws Exception;
	
	void executarSincronismoComLdapAoIncluirUsuario(ConfiguracaoLdapVO conf, UsuarioVO usuario, String senha, MatriculaVO matricula ,PessoaEmailInstitucionalVO pessoaEmailInstitucional ,UsuarioVO usuarioLogado );

    void executarSincronismoComLdapAoAlterarSenha(ConfiguracaoLdapVO conf, UsuarioVO usuario, String senha);

    void consultar(DataModelo dataModelo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    void reexecutarSincronismoComLdap(RegistroLdapVO obj, UsuarioVO usuarioLogado) throws Exception;

    boolean testarConexaoLdap(ConfiguracaoLdapVO configuracaoLdapVO, UsuarioVO usuario) throws Exception;

	void executarCriacaoContaTeste(ConfiguracaoLdapVO conf, UsuarioVO usuario, String senha) throws Exception;
	
	void executarExclusaoContaTeste(ConfiguracaoLdapVO conf, UsuarioVO usuario, String senha) throws Exception;

	void executarSincronismoComLdapAoCancelarTransferirMatricula(ConfiguracaoLdapVO conf, UsuarioVO usuario, MatriculaVO matricula, Integer registroLdap , Boolean estorno,UsuarioVO usuarioVO);

	void realizarAlterarDadosCadastraisPessoaLDAP(PessoaVO pessoaVO, PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO, Integer registroLdap,UsuarioVO usuarioVO);

	

	void executarSincronismoComLdapAoInativarContaPorPessoaEmailInstitucional(ConfiguracaoLdapVO conf,	UsuarioVO usuario, MatriculaVO matricula, PessoaVO pessoa, Boolean estorno,		Integer registroLdap,PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO,UsuarioVO usuarioLogado);

	SituacaoRetornoLdapEnum verificarExistenciaContaLdapPorRegistroAcademicoEmailInstitucional(ConfiguracaoLdapVO conf, UsuarioVO usuario,
			String emailInstitucional , String numeroRegistroAcademico) throws Exception;

	void realizarCriacaoUsuarioLDAP_AlterandoDadosUsuarioVeteranoParaPrimeiroAcesso(
			ConfiguracaoLdapVO configuracaoLdapPorCurso, UsuarioVO usuarioAlteracao, MatriculaVO matriculaVO,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistema,Boolean criarNovoPessoaEmailInstitucional , PessoaEmailInstitucionalVO emailInstitucional ,UsuarioVO usuario) throws Exception;

	void executarProcessamentoAtivacaoRegistroLdapBlackBoard(MatriculaVO matriculaVO,
			 ConfiguracaoLdapVO configuracaoLdapPorCurso,
			UsuarioVO usuarioAlteracao, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuarioLogado)
			throws Exception;

	void executarSincronismoComLdapAoAlterarSenha(ConfiguracaoLdapVO conf, UsuarioVO usuario, String senha,
			Integer registroLdap);

}
