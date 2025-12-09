package negocio.interfaces.arquitetura;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.ConfiguracaoLdapVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.SolicitarAlterarSenhaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoEnumInterface;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.protocolo.enumeradores.TipoPoliticaDistribuicaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.dominios.TipoUsuario;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface UsuarioInterfaceFacade {

	public UsuarioVO novo() throws Exception;
	
	public UsuarioVO validarUsuarioExistente(PessoaVO pessoaVO, String matricula, UsuarioVO usuarioLogado) throws Exception;
	
	public String executarVerificacaoDeUsernameValida(PessoaVO pessoa, Integer tentativa, Integer qtdeLimiteCaracteres) throws Exception;

	public void registrarUltimoAcesso(final UsuarioVO obj) throws Exception;

	public void incluir(UsuarioVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(UsuarioVO obj, UsuarioVO usuario, PerfilAcessoPermissaoEnumInterface... permissoes) throws Exception;

	public void excluir(UsuarioVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(final UsuarioVO obj, boolean verificarAcesso, UsuarioVO usuario, PerfilAcessoPermissaoEnumInterface... permissoes) throws Exception;

	public void incluir(final UsuarioVO obj, final boolean verificarPermissao, UsuarioVO usuario) throws Exception;

	public UsuarioVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<UsuarioVO> consultarPorCodigoUnico(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorNome(String valorConsulta, Integer codUnidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<UsuarioVO> consultarPorNomeTipoEspecificoUsuarioAlunoProfessorCoordenador(String valorConsulta, Integer codUnidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, DataModelo dataModelo, UsuarioVO usuario) throws Exception;

	public Integer consultarTotalDeGegistroPorNome(String valorConsulta, Integer codUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List consultarPorUsername(String valorConsulta, Integer codUnidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<UsuarioVO> consultarPorUsernameTipoEspecificoUsuarioAlunoProfessorCoordenador(String valorConsulta, Integer codUnidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, DataModelo dataModelo, UsuarioVO usuario) throws Exception;
	
	public Integer consultarTotalDeRegistroPorUsername(String valorConsulta, Integer codUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorNomePerfilAcesso(String valorConsulta, Integer codUnidadeEnsino, Integer limite, Integer offset, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public Integer consultarTotalDeRegistroPorNomePerfilAcesso(String valorConsulta, Integer codUnidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigoPessoa(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public UsuarioVO consultarPorPessoa(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public void alterarSenha(UsuarioVO usuarioLogado, String loginAnterior, String senhaAnterior, String login, String senha, boolean alterarSenhaContaGsuite) throws Exception;

	public UsuarioVO consultarPorNomePessoa(String aluno, boolean b, int nivelmontardadosDadosminimos, UsuarioVO usuario) throws Exception;

	public UsuarioVO consultarPorUsernameCPFUnico(String username, String cpf, boolean b, int nivelmontardadosTodos, UsuarioVO usuario) throws Exception;

	public UsuarioVO consultarPorUsernameUnico(String username, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void executarAtualizarUsernameSenha(String tipoUsuario, UsuarioVO usuario) throws Exception;

	public List consultarUsuariosFuncionarios(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void alterarSenha(Boolean lembrarDeSincronizarComLdapNoFinalDaTransacao, UsuarioVO usuario) throws Exception;

	public String definirVisaoLogarDiretamente(UsuarioVO usuarioVO) throws Exception;

	public String definirVisaoLogarComEscolha(UsuarioVO usuarioVO) throws Exception;

	public void definirOpcoesVisao(UsuarioVO usuarioVO) throws Exception;

	public Integer consultarQuantidadeDeUnidadesUsuarioEstaVinculado(UsuarioVO usuarioVO);

	public UsuarioVO consultarUsuarioUnicoDMParaMatriculaCRM(int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public Boolean validarUnicidadeUsername(String username, Integer codigoPessoa) throws Exception;

	public List consultarPorCodigoParceiro(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void realizarValidacaoPersitirUsuario( ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuarioLogado) throws Exception;

	public void executarInclusaoNovoUsuario(ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuarioLogado) throws Exception;

	public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorUsername(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<UsuarioVO> consultaRapidaPorNomeAlunoAlteracaoSenha(String nomePessoa, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<UsuarioVO> consultaRapidaPorMatriculaAlunoAlteracaoSenha(String matricula, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public Integer consultarTotalDeGegistroPorNomeAlunoAlteracaoSenha(String valorConsulta, Integer codUnidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public Integer consultarTotalDeGegistroPorMatriculaAlunoAlteracaoSenha(String valorConsulta, Integer codUnidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void alterarUserNameSenhaAlteracaoSenhaAluno(  final UsuarioVO obj, final String usernameNovo, final String senhaNova ,Boolean validarUnicidadeSenhaJaUtilizada,UsuarioVO usuarioLogado) throws Exception;

	public List<UsuarioVO> consultaRapidaPorResponsavelLegal(Integer codigoResponsavelLegal, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void executarVerificacaoParaInclusaoNovoUsuarioPorFiliacao(PessoaVO pessoa, UsuarioVO usuarioLogado) throws Exception;

	void realizarValidacaoSenhaPessoa(Integer pessoa, String senha) throws Exception;

	public void alterarTipoUsuario(final Integer pessoa, final String tipoUsuario, final UsuarioVO usuario) throws Exception;

	Boolean consultarPorCodigoPessoaSeUsuarioExiste(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void registrarPrimeiroAcesso(final UsuarioVO obj) throws Exception;

	public Boolean consultarPorCpfPessoaSeUsuarioExiste(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public Integer consultarTotalDeRegistroPorTipoUsuario(String valorConsulta, String nome, Integer codUnidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorTipoUsuario(String valorConsulta, String nome, Integer codUnidadeEnsino, Integer limite, Integer offset, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UsuarioVO> consultarUsuarioPorCodigoDepartamentoESemDepartamento(Integer codigo, Integer unidadeEnsino, Integer unidadeEnsinoLogado, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UsuarioVO> consultaRapidaPorNomeUsuarioAutoComplete(String valorConsulta, int limit, Integer unidadeEnsino, Integer unidadeEnsinoLogado, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UsuarioVO> consultaRapidaPorNomeFuncionario(String valorConsulta, Integer departamento, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UsuarioVO> consultaRapidaPorMatriculaFuncionario(String valorConsulta, Integer departamento, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception;

	List<UsuarioVO> consultaRapidaPorCPFFuncionario(String valorConsulta, Integer departamento, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UsuarioVO> consultaRapidaPorCargoFuncionario(String valorConsulta, Integer departamento, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UsuarioVO> consultaRapidaFuncionarioPorUnidadeEnsino(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	UsuarioVO consultarPorCodigoUsuario(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void alterarBoleanoUsuarioPerfilAdministrador(Integer pessoa, boolean perfilAdministrador, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	SqlRowSet consultarAlunosSemLogarSistema() throws Exception;

	/**
	 * @author Wellington Rodrigues - 21/05/2015
	 * @param pessoaAntigo
	 * @param pessoaNova
	 * @throws Exception
	 */
	void alterarPessoaUnificacaoFuncionario(Integer pessoaAntigo, Integer pessoaNova, UsuarioVO usuarioVO) throws Exception;

	public void executarValidacaoSenhasCompativeis(UsuarioVO usuarioVO, String novaSenha, String novaSenhaDois) throws Exception;

	public void alterarBooleanoSolicitarNovaSenha(Boolean solicitarNovaSenha, UsuarioVO usuarioAlterarSenha, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void alterarNomeUsuario(final Integer codPessoa, final String nome, UsuarioVO usuario) throws Exception;

	/**
	 * @author Victor Hugo de Paula Costa - 21 de nov de 2016
	 * @param usuario
	 * @throws Exception
	 */
	void alterarTokenEPlataformaAplicativo(UsuarioVO usuario) throws Exception;

	/**
	 * @author Victor Hugo de Paula Costa - 21 de nov de 2016
	 * @param comunicadoInternoDestinatarioVOs
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	List<UsuarioVO> consultaRapidaUsuariosEnvioPushAplicativo(List<ComunicadoInternoDestinatarioVO> comunicadoInternoDestinatarioVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void alterarTokenRedefinirSenha(final UsuarioVO obj) throws Exception;

	public UsuarioVO consultarPorTokenRedefinirSenha(String token) throws Exception;

	public void alterarSenhaPrimeiroAcesso(Boolean lembrarDeSincronizarComLdapNoFinalDaTransacao, final UsuarioVO obj, ConfiguracaoGeralSistemaVO config) throws Exception;

	List<UsuarioVO> consultarResponsavelInteracaoWorkflowPorAlunoFichaAluno(Integer aluno, UsuarioVO usuarioVO);

	UsuarioVO consultarPorDepartamentoCargoEPoliticaDistribuicao(DepartamentoVO departamento, CargoVO cargo, TipoPoliticaDistribuicaoEnum tipoPoliticaDistribuicao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UsuarioVO> consultaUsuarioPorDepartamento(DepartamentoVO departamento, CargoVO cargo, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	List<UsuarioVO> consultaUsuarioCoordenadorPorDepartamento(DepartamentoVO departamento, CargoVO cargo, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	

	public List<UsuarioVO> consultarUsuarioPorUnidadeEnsino(String valorConsulta, int unidadeEnsino, int limit, int offset, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	int consultarUsuarioPorUnidadeEnsinoContador(String valorConsulta, int unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorTipoUsuario(TipoUsuario tipoUsuario) throws Exception;

	List<UsuarioVO> consultaUsuarioGerentePorDepartamento(DepartamentoVO departamento, CargoVO cargo, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	void realizarNavegacaoParaMinhaBiblioteca(ConfiguracaoBibliotecaVO configuracaoBiblioteca, UsuarioVO usuarioLogado) throws Exception;

	void alterarPossuiCadastroLdap(UsuarioVO usuarioVO ,UsuarioVO usuarioLogado) throws Exception;
	
	public Boolean validarTokenAplicativo(String token);
	
	public void alterarSenhaShaSenhaMSCHAPV2(String senha, final UsuarioVO obj) throws Exception;

	public void registrarFalhaTentativaLogin(String username, int qtdTentativas) throws Exception;
	Map<String, Integer> removerUsuarioMinhaBiblioteca(List<UsuarioVO> usuarioVOs, ConsistirException consistirException) throws Exception;

    public List<UsuarioVO> consultarUsuarioPossuiIntegracaoMinhaBiblioteca(Date dataEvasao, String valorConsulta, String campoConsulta) throws Exception;

	
	public StringBuilder consultarUsuarioAlunoPorUnidadeEnsinoCurso(Integer codigoUnidadeEnsino,  Integer codigoCurso, Boolean solicitarNovaSenha) throws Exception;
	
	public StringBuilder consultarUsuarioProfessorPorUnidadeEnsinoCurso(Integer codigoUnidadeEnsino,  Integer codigoCurso, Boolean solicitarNovaSenha) throws Exception;
	
	public StringBuilder consultarUsuarioCoordenadorPorUnidadeEnsinoCurso(Integer codigoUnidadeEnsino,  Integer codigoCurso, Boolean solicitarNovaSenha) throws Exception;
	
	public StringBuilder consultarUsuarioFuncionarioPorUnidadeEnsinoCurso(Integer codigoUnidadeEnsino,  Integer codigoDepartamento, Boolean solicitarNovaSenha) throws Exception;
	
	public Integer consultarTotalUsuarioAlunoPorUnidadeEnsinoCurso(Integer codigoUnidadeEnsino,  Integer codigoCurso, Boolean solicitarNovaSenha) throws Exception;
	
	public Integer consultarTotalUsuarioProfessorPorUnidadeEnsinoCurso(Integer codigoUnidadeEnsino,  Integer codigoCurso, Boolean solicitarNovaSenha) throws Exception;
	
	public Integer consultarTotalUsuarioCoordenadorPorUnidadeEnsinoCurso(Integer codigoUnidadeEnsino,  Integer codigoCurso, Boolean solicitarNovaSenha) throws Exception;
	
	public Integer consultarTotalUsuarioFuncionarioPorUnidadeEnsinoCurso(Integer codigoUnidadeEnsino,  Integer codigoDepartamento, Boolean solicitarNovaSenha) throws Exception;
	
	public UsuarioVO obterUsuarioResponsavelOperacoesExternas() throws Exception;
	
	List<UsuarioVO> consultarUsuarioUnificacao(UsuarioVO usuarioManterVO, String campoUnificar, String valorUnificar, UsuarioVO usuarioVO) throws Exception;

	void realizarUnificacaoUsuario(List<UsuarioVO> listaUsuarioUnificarVOs, UsuarioVO usuarioManterVO, UsuarioVO usuarioLogadoVO) throws Exception;
	
	public void alterarPoliticaPrivacidade(final UsuarioVO obj) throws Exception;

	public List<UsuarioVO> consultarPorMatriculaTipoEspecificoUsuarioAlunoProfessorCoordenador(String valorConsulta,	Integer codUnidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados,
			DataModelo dataModelo, UsuarioVO usuario) throws Exception;

	public List<UsuarioVO> consultarPorRegistroAcademicoTipoEspecificoUsuarioAlunoProfessorCoordenador(String valorConsulta, Integer codUnidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados,
			DataModelo dataModelo, UsuarioVO usuario) throws Exception;

	public List consultarPorRegistroAcademico(String valorConsulta, Integer codUnidadeEnsino, int nivelMontarDados,	Integer limite, Integer offset , DataModelo dataModelo, UsuarioVO usuario) throws Exception;

	public List consultarPorMatricula(String valorConsulta, Integer codUnidadeEnsino, int nivelMontarDados,	Integer limite, Integer offset , DataModelo dataModelo, UsuarioVO usuario) throws Exception;

	void alterarBooleanoResetouSenhaPrimeiroAcesso(Boolean resetouSenhaPrimeiroAcesso, UsuarioVO usuarioResetouSenha,
			boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	public List<UsuarioVO> consultarPorCPF(String valorConsulta,  Integer limite,  Integer offset, boolean controlarAcesso, DataModelo dataModelo, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<UsuarioVO> consultarPorEmailInstitucional(String valorConsulta, Integer limite, Integer offset, boolean controlarAcesso,   DataModelo dataModelo, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public UsuarioVO consultarPorCPF(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public UsuarioVO consultarPorEmailInstitucional(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public UsuarioVO consultaRapidaPorMatriculaAluno(String matricula, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<TipoOrigemDocumentoAssinadoEnum> realizarVerificacaoPermissaoTipoOrigemDocumentoAluno(UsuarioVO usuarioVO);

	void alterarBooleanoAtivoLdap(Boolean ativoLdap, UsuarioVO usuarioResetouSenha, UsuarioVO usuarioVO)
			throws Exception;

	public List<UsuarioVO> consultarUsuariosPorPessoa(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<UsuarioVO> consultaRapidaPorNomeFuncionario(String valorConsulta, Integer departamento, String tipoPessoa, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<UsuarioVO> consultaRapidaPorMatriculaFuncionario(String valorConsulta, Integer departamento, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Boolean exerceCargoAdministrativo, Boolean ativo, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception;

	public List<UsuarioVO> consultaRapidaPorCPFFuncionario(String valorConsulta, Integer departamento, String tipoPessoa, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<UsuarioVO> consultaRapidaPorCargoFuncionario(String valorConsulta, Integer departamento, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<UsuarioVO> consultaRapidaPorNomeUsuarioAutoComplete(String valorConsulta, int limit, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer departamento, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	public UsuarioVO consultarUsuarioNaoVinculadoFuncionario(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public String realizarCarregamentoListaUsuarioAlunoAtivos();
	public void realizarCarregamentoDadosUsuarioAlunoAtivo(Integer codigo, ProgressBarVO progressBarVO) throws Exception;
}
