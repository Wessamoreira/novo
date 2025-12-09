package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface ConfiguracaoGeralSistemaInterfaceFacade {

	public ConfiguracaoGeralSistemaVO novo() throws Exception;

	public void incluir(ConfiguracaoGeralSistemaVO obj, UsuarioVO usuarioLogado) throws Exception;

	public void alterar(ConfiguracaoGeralSistemaVO obj, UsuarioVO usuarioLogado) throws Exception;

	public void excluir(ConfiguracaoGeralSistemaVO obj, UsuarioVO usuarioLogado) throws Exception;

	public ConfiguracaoGeralSistemaVO consultarPorCodigoConfiguracoes(Integer codigoConfiguracoes, boolean controlarAcesso, UsuarioVO usuario, int nivelMontarDados) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorSmptPadrao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorNomeVisao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorNomePerfilAcesso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public ConfiguracaoGeralSistemaVO consultarPorCodigoUnidadeEnsino(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public ConfiguracaoGeralSistemaVO consultarConfiguracaoASerUsada(int nivelMontarDados, UsuarioVO usuario,Integer unidadeEnsino) throws Exception;
	
	Integer consultarCodigoConfiguracaoReferenteUnidadeEnsino(Integer unidadeEnsino) throws Exception;

    public PessoaVO consultarResponsavelPadraoComunicadoInternoPorCodigoConfiguracoes(Integer codigoPessoa) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public void validarConfiguracaoGeralSistema(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	/**
	 * Método responsável por verificar se sistema está configurado para validar
	 * cpf.
	 * 
	 * @param controlarAcesso
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean realizarVerificacaoValidarCpf(boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public ConfiguracaoGeralSistemaVO consultarConfiguracaoASerUsadaUnidadEnsino(Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public ConfiguracaoGeralSistemaVO consultarPorMensagemTelaLoginConfiguracaoPadraoSistema() throws Exception;

    public ConfiguracaoGeralSistemaVO consultarPorApresentarHomeCandidato() throws Exception;

    public String consultarMensagemErroSenha() throws Exception;

    public ConfiguracaoGeralSistemaVO consultarPorCodigoConfiguracaoGeralSistema(Integer codigoConfiguracoes, boolean controlarAcesso, UsuarioVO usuario, int nivelMontarDados) throws Exception;

    public ConfiguracaoGeralSistemaVO consultarConfiguracaoPadraoSistemaTextoBancoCurriculum() throws Exception;
    
    public ConfiguracaoGeralSistemaVO consultarConfiguraoesEnvioEmail() throws Exception;
    
    public ConfiguracaoGeralSistemaVO consultarConfiguraoesLocalUploadArquivoFixo() throws Exception;
    
    ConfiguracaoGeralSistemaVO consultarConfiguraoesWebserviceNFe() throws Exception;

		ConfiguracaoGeralSistemaVO consultarConfiguraoesEnvioEmailMaisResponsavelPadrao() throws Exception;		

    public ConfiguracaoGeralSistemaVO consultarPorApresentarEsqueceuMinhaSenha() throws Exception;

	String consultarRegraComissionamentoRanking(Integer unidadeEnsino);

		ConfiguracaoGeralSistemaVO consultarConfiguraoesQtdeDiasNotificacaoDataProcessoSeletivo() throws Exception;

	/**
	 * Responsável por executar a verificação se é para apresentar aluno
	 * pendente financeiramente seguindo a regra definida na configuração geral
	 * do sistema. Caso o usuário logado seja professor, é validado o boleano
	 * apresentarAlunoPendenteFinanceiroVisaoProfessor, caso o usuário logado
	 * seja coordenador, é validado o boleano
	 * apresentarAlunoPendenteFinanceiroVisaoCoordenador, caso seja visão da
	 * secretaria e retornado verdadeiro.
	 * 
	 * @author Wellington Rodrigues - 16/04/2015
	 * @param unidadeEnsinoVO
	 * @param usuarioLogado
	 * @return
	 * @throws Exception
	 */
	boolean executarVerificacaoApresentarAlunoPendenteFinanceiramente(Integer unidadeEnsino, UsuarioVO usuarioLogado) throws Exception;
	
	/**
	 * Consulta ConfiguracaoGeralSistemaVO com o configuracoesVO padrao
	 * 
	 * @return ConfiguracaoGeralSistemaVO padrao
	 * @throws Exception
	 */
	public ConfiguracaoGeralSistemaVO consultarConfiguracaoPadraoSistema() throws Exception;
	
	String consultarUrlExternoDownloadArquivoPadraoSistema() throws Exception;
	
	public String consultarTokenWebServicePadraoSistema() throws Exception;

	public ConfiguracaoGeralSistemaVO consultarConfiguraoesLocalUploadArquivoGED() throws Exception;
	
	public ConfiguracaoGeralSistemaVO consultarConfiguraoesDiretorioUpload() throws Exception;
	
	ConfiguracaoGeralSistemaVO consultarConfiguraoesMinhaBiblioteca() throws Exception;
	
	Integer consultarConfiguraoesQtdeCaractereLimiteDownloadMaterial() throws Exception;

	void excluirCertificado(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;
	
	boolean consultarConfiguracaoGeralSeGerarSenhaCpfAluno() throws Exception;
	
	String consultarUrlAcessoExternoAplicacaoPadraoSistema() throws Exception;

	String consultarVesaoSeiSignature() throws Exception;

	void carrregarDadosConfiguracaoPadraoCancelamentoMatriculaPorOutraMatriculaPorCodigoConfiguracao(
			ConfiguracaoGeralSistemaVO obj, UsuarioVO usuario) throws Exception;
	
    public void incluirLogJobSymplicty(String erro, Boolean sucesso) throws Exception;
}