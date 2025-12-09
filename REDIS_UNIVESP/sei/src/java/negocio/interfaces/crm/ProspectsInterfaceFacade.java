package negocio.interfaces.crm;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.CursoInteresseVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.TipoProspectVO;
import negocio.comuns.segmentacao.SegmentacaoProspectVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface ProspectsInterfaceFacade {
	

    public void persistir(ProspectsVO obj, Boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
    public void persistirRegistroProspectRapido(ProspectsVO obj, ProspectsVO indicadoPeloProspect, CampanhaVO campanha, UsuarioVO vendedor, Boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
    public void excluir(ProspectsVO obj, Boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
    public List consultar(String valorConsulta, Integer unidadeEnsino, String campoConsulta, boolean controlarAcesso, UsuarioVO usuario, String tipoFiltroConsulta) throws Exception;
    public void validarDados(ProspectsVO obj) throws Exception;
    public void realizarAtualizacaoTelefonePessoa(ProspectsVO prospect, UsuarioVO usuario);
    public void realizarAtualizacaoTelefoneUnidadeEnsino(ProspectsVO prospect, UsuarioVO usuario);
    public void realizarAtualizacaoTelefoneParceiro(ProspectsVO prospect, UsuarioVO usuario);
    public void realizarAtualizacaoTelefoneFornecedor(ProspectsVO prospect, UsuarioVO usuario);
    public Boolean isValido(ProspectsVO obj) throws Exception;
    public ProspectsVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public ProspectsVO consultarPorCPFCNPJUnico(ProspectsVO obj, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorNome(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, String tipoFiltroConsulta) throws Exception;
    public List consultarPorRazaoSocial(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, String tipoFiltroConsulta) throws Exception;
    public List consultarPorCpf(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, String tipoFiltroConsulta) throws Exception;
    public List consultarPorCnpj(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, String tipoFiltroConsulta) throws Exception;
    public List consultarPorNomeUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, String tipoFiltroConsulta) throws Exception;
    public List<ProspectsVO> consultaRapidaPorNomeAutoComplete(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public void adicionarObjCursoInteresseVOs(ProspectsVO objProspectsVO, CursoInteresseVO obj) throws Exception;
    public void excluirObjCursoInteresseVOs(ProspectsVO objProspectsVO, Integer curso) throws Exception;
    public CursoInteresseVO consultarObjCursoInteresseVO(ProspectsVO objProspectsVO, Integer curso) throws Exception;
    public void carregarDados(ProspectsVO obj, UsuarioVO usuario) throws Exception;
    public void carregarDados(ProspectsVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;
    public ProspectsVO realizarPreenchimentoProspect(ProspectsVO obj, UsuarioVO usuario) throws Exception;
    public ProspectsVO realizarPreenchimentoProspectPorUnidade(String cnpj, String nome, ProspectsVO obj, UsuarioVO usuario) throws Exception;
    public ProspectsVO realizarPreenchimentoProspectPorFornecedorCpf(String cpf, ProspectsVO obj, UsuarioVO usuario) throws Exception;
    public PessoaVO realizarPreenchimentoPessoaPorProspect(ProspectsVO obj, UsuarioVO usuario) throws Exception;
    public void realizarPreenchimentoPessoaPorProspect(PessoaVO pessoa, ProspectsVO obj, UsuarioVO usuario) throws Exception;
    public ProspectsVO realizarPreenchimentoProspectPorFornecedorCnpj(String cnpj, ProspectsVO obj, UsuarioVO usuario) throws Exception;
    public ProspectsVO realizarPreenchimentoProspectPorParceiroCpf(String cpf, ProspectsVO obj, UsuarioVO usuario) throws Exception;
    public ProspectsVO realizarPreenchimentoProspectPorParceiroCnpj(String cnpj, ProspectsVO obj, UsuarioVO usuario) throws Exception;
    public ProspectsVO realizarPreenchimentoProspectPorPessoa(String cpf, ProspectsVO obj, UsuarioVO usuario) throws Exception;
    public List<TipoProspectVO> consultarTipoProspect(String cpfConsulta, String cnpjConsulta, Integer prospectIgnorar) throws Exception;
    public void incluirSemValidarDados(final ProspectsVO obj, Boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
    public ProspectsVO consultarPorEmail(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario)throws  Exception;
    public ProspectsVO consultarPorEmailUnico(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public void alterarSemValidarDados(final ProspectsVO obj, Boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean origemPreInscricao) throws Exception;
    public void alterarComValidarDados(final ProspectsVO obj, Boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
    public void alterarProspectTelaFollowUp(final ProspectsVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
    public void validarDadosPreInscricao(ProspectsVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
    public PessoaVO verificaCriandoPessoaProspect(ProspectsVO obj, UsuarioVO usuario) throws Exception;    
    public void montarCursoInteresseVO(SqlRowSet dadosSQL, ProspectsVO obj);
    public void incluirRapidaPorLigacaoReceptia(final ProspectsVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
    public void alterarPessoaProspect(final ProspectsVO obj, UsuarioVO usuario) throws Exception;
    void alterarConsultorProspect(ProspectsVO obj, UsuarioVO usuario) throws Exception;
    public void adicionarObjFormacaoAcademicaVOs(FormacaoAcademicaVO obj, ProspectsVO prospectsVO) throws Exception;
    public void excluirObjFormacaoAcademicaVOs(String curso, ProspectsVO prospectsVO) throws Exception;
    void alterarProspectConformePessoa(PessoaVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;
    public void unificarProspects(final Integer codProspectManter, final Integer codProspectRemover, UsuarioVO usuarioVO) throws Exception;
    public List consultarPorEmailPrincipalSecundario(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, String tipoFiltroConsulta) throws Exception;
	public void realizarCriacaoProspectSegmentacaoOpcao(ProspectsVO obj, List<SegmentacaoProspectVO> segmentacaoProspectVOs, UsuarioVO usuarioVO) throws Exception;
	public void realizarRecuperacaoProspectSegmentacaoOpcao(ProspectsVO obj, List<SegmentacaoProspectVO> segmentacaoProspectVOs, UsuarioVO usuarioVO) throws Exception;
	void alterarUnidadeEnsinoProspect(ProspectsVO obj, UsuarioVO usuario) throws Exception;
	void alterarUnidadeEnsinoEConsultorProspect(ProspectsVO obj, UsuarioVO usuario) throws Exception;
	public List consultarTipoProspectEmail(String email, Integer unidadeEnsino, Integer prospectIgnorar) throws Exception;
	public ProspectsVO consultarPorCodigoPessoa(Integer codigoPessoa, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void obterProspectsDuplicadosUnificando(UsuarioVO usuarioVO) throws Exception;
	void realizarEnvioEmail(ComunicacaoInternaVO comunicacaoInternaVO, List<ProspectsVO> prospectsVOs, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;
	/**
	 * @author Rodrigo Wind - 09/06/2015
	 * @param nome
	 * @param email
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
    List<ProspectsVO> consultarPossivelProspectVincularPessoa(Integer pessoa, String nome, String email, String cpf, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;       
       
	public String consultarAgendaProspect(Integer codigoPessoa) throws Exception;
    
    public void excluirProspectERegistrosReferenciados(ProspectsVO obj, Boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	void realizarVinculoUnidadeEnsinoProspectSemUnidadeEnsinoPorPessoa(Integer pessoa, Integer unidadeEnsino, UsuarioVO usuario)
			throws Exception;
	
	ProspectsVO realizarCriacaoProspectLigacaoReceptivaRS(ProspectsVO prospectsVO, UsuarioVO usuario) throws Exception;
	void alterarResponsavelFinanceiroProspect(ProspectsVO obj, UsuarioVO usuario) throws Exception;

	ProspectsVO consultarPorNomeEmailUnico(String email, String nome, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	
	/**
	 * Consultar todos os dados do Prospects
	 * 
	 * @param codigo - codigo do prospect
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	public ProspectsVO consultarDadosCompletosPorIdProspect(Integer codigo, UsuarioVO usuario) throws Exception;
	
	public void alterarFlagProspectSincronizadoComRDStation(final ProspectsVO obj, UsuarioVO usuario) throws Exception;
	
	public void alterarConsultorProspectUnificacaoFuncionario(Integer codigoConsutorResponsavelManter ,Integer codigoConsutorResponsavelRemover, UsuarioVO usuario) throws Exception;

	public ProspectsVO executarValidarDadosProspectsConformePessoa(ProspectsVO prospectVO);
	
	public ProspectsVO consultarPaiMaeProspectPorCodigo(Integer codigoProspect) throws Exception;
	
	public void alterarApenasDadosPreenchidos(final ProspectsVO obj, Boolean verificarAcesso, UsuarioVO usuario) throws Exception;
}