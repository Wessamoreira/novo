package negocio.interfaces.gsuite;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.gsuite.AdminSdkIntegracaoVO;
import negocio.comuns.gsuite.PessoaGsuiteVO;

public interface AdminSdkIntegracaoInterfaceFacade {

	AdminSdkIntegracaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados);

	void consultar(DataModelo dataModelo, AdminSdkIntegracaoVO obj);

	void consultarTotalUsuarioAdminSdkGoogle(AdminSdkIntegracaoVO obj);

	AdminSdkIntegracaoVO consultarAdminSdkGoogleEmProcessamentoPorUsuario(Integer usuario, int nivelMontarDados);

	void consultarAtualizacaoAdminSdkIntegracao(AdminSdkIntegracaoVO obj);

	void realizarVerificacaoPessoaGsuiteVOValida(PessoaGsuiteVO obj, UsuarioVO usuarioVO);

	void executarAlteracaoSenhaContaGsuite(PessoaVO pessoa, String senha, String confirmacaoSenha, UsuarioVO usuario) throws Exception;

}
