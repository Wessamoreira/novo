package negocio.interfaces.biblioteca;

import java.util.List;

import org.springframework.dao.DataAccessException;

import controle.academico.VisaoAlunoControle;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.ArquivoMarc21CatalogoVO;
import negocio.comuns.biblioteca.ArquivoMarc21VO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.UteisFTP;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em especial com a classe
 * Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais. Além de
 * padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio de sua classe Façade (responsável por persistir
 * os dados das classes VO).
 */
public interface ConfiguracaoBibliotecaInterfaceFacade {

	ConfiguracaoBibliotecaVO novo() throws Exception;

	void incluir(ConfiguracaoBibliotecaVO obj, UsuarioVO usuario) throws Exception;

	void alterar(ConfiguracaoBibliotecaVO obj, UsuarioVO usuario) throws Exception;

	void excluir(ConfiguracaoBibliotecaVO obj, UsuarioVO usuario) throws Exception;

	ConfiguracaoBibliotecaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<ConfiguracaoBibliotecaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void setIdEntidade(String aIdEntidade);

	ConfiguracaoBibliotecaVO consultarConfiguracaoPadrao(int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	
	ConfiguracaoBibliotecaVO consultarConfiguracaoMinhaBiblioteca() throws Exception;
	
	ConfiguracaoBibliotecaVO consultarConfiguracaoBibliotecaLexMagister() throws Exception;

	List<ConfiguracaoBibliotecaVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void validarDados(ConfiguracaoBibliotecaVO configuracaoBibliotecaVO) throws ConsistirException;

	FuncionarioVO consultarFuncionarioPadraoEnvioNotificacaoEmail(UsuarioVO usuarioLogado) throws DataAccessException, Exception;

	ConfiguracaoBibliotecaVO consultarConfiguracaoPadraoFuncionario(Boolean padrao, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;

	ConfiguracaoBibliotecaVO consultarConfiguracaoPorBiblioteca(Integer biblioteca, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	ConfiguracaoBibliotecaVO consultarConfiguracaoBibliotecaPorBibliotecaUnidadeEnsinoENivelEducacional(Integer biblioteca, String matricula, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	ConfiguracaoBibliotecaVO consultarConfiguracaoBibliotecaPorUnidadeEnsinoENivelEducacional(String matricula, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	ConfiguracaoBibliotecaVO executarObterConfiguracaoBibliotecaComBaseTipoPessoa(Integer biblioteca, String tipoPessoa, String matricula, UsuarioVO usuarioVO) throws Exception;

	Boolean realizarValidacaoBibliotecaLexMagisterHabilitado(Boolean bibliotecaLexMagister, UsuarioVO usuarioLogado,
			VisaoAlunoControle visaoAlunoControle);
	
	void realizarNavegacaoParaBibliotecaLexMagister(UsuarioVO usuarioLogadoVO);

	void verificarConexaoBibliotecaEbsco(String host , String usuario , String senha)throws Exception  ;

	ConfiguracaoBibliotecaVO consultarConfiguracaoBibliotecaEbsco() throws Exception;

	void validarDadosEbsco(String hostEbsco, String usuarioEbsco, String senhaEbsco) throws Exception;
	
	
	public void realizarEnvioCatalogoIntegracaoEbsco(List<ArquivoMarc21VO> arquivoMarc21VOs ,ProgressBarVO progressBarVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO ,boolean validarIntegracaoEbsco,String realizarEnvioCatalogoIntegracaoEbsco) throws Exception;

	public void realizarEnvioCatalogoIntegracaoEbsco(ArquivoMarc21VO arquivoMarc21VO, UteisFTP ftpClient,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaPrivilegiandoACfgDaUnidade,
			boolean validarIntegracaoEbsco, ProgressBarVO progressBarVO ) throws Exception;	



	Boolean realizarValidacaoBibliotecaBVPearsonHabilitado(Boolean bibliotecaBvPearson, UsuarioVO usuarioLogado,
			VisaoAlunoControle visaoAlunoControle);

	public String realizarCriacaoTokenPearson(String loginPearson);

	ConfiguracaoBibliotecaVO consultarConfiguracaoBibliotecaBVPearson() ;

}