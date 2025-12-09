package negocio.interfaces.administrativo;

import java.io.File;
import java.util.List;

import org.primefaces.event.FileUploadEvent;

import controle.academico.RenovarMatriculaControle;
import negocio.comuns.academico.ProgramacaoFormaturaUnidadeEnsinoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoCentroResultadoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import webservice.servicos.UnidadeEnsinoRSVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em especial com a classe
 * Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais. Além de
 * padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio de sua classe Façade (responsável por persistir
 * os dados das classes VO).
 */
public interface UnidadeEnsinoInterfaceFacade {

	UnidadeEnsinoVO novo() throws Exception;

	void incluir(UnidadeEnsinoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

	void alterar(UnidadeEnsinoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

	void alterarAnoNumeroDocumento(UnidadeEnsinoVO obj) throws Exception;

	void alterarCoordenadoresTCC(final UnidadeEnsinoVO obj, UsuarioVO usuario) throws Exception;

	void excluir(UnidadeEnsinoVO obj, UsuarioVO usuarioVO) throws Exception;
	
	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoConfiguracoesPorGestaoEnvioMensagemAutomatica();

	UnidadeEnsinoVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	UnidadeEnsinoVO consultarPorChavePrimaria(Integer codigo, String tipoUsuario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultarPorNome(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultarPorRazaoSocial(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultarPorNomeCidade(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultarPorCNPJ(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultarPorInscEstadual(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultarPorRG(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultarPorCPF(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	UnidadeEnsinoVO consultarPorCPFUnico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	UnidadeEnsinoVO consultaBasicaPorFuncionario(FuncionarioVO funcionario, UsuarioVO usuario) throws Exception;

	UnidadeEnsinoVO consultarPorCNPJUnico(String valorConsulta, String nomeConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;	

	UnidadeEnsinoVO consultarSeExisteUnidadeEnsinoCurso(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	UnidadeEnsinoVO consultarSeExisteUnidadeMatriz(boolean somenteDadosBasicos, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	UnidadeEnsinoVO consultarSeExisteUnidade(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void setIdEntidade(String aIdEntidade);

	List<UnidadeEnsinoVO> consultaRapidaPorNome(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<UnidadeEnsinoVO> consultaRapidaPorNomePorApresentarHomePreInscricao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultaRapidaPorRazaoSocial(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultaRapidaPorNomeCidade(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultaRapidaPorCnpj(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultaRapidaPorInscEstatual(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultaRapidaPorRg(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDado, UsuarioVO usuarios) throws Exception;

	List<UnidadeEnsinoVO> consultaRapidaPorCpf(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void carregarDados(UnidadeEnsinoVO obj, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultarPorCodigoCurso(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultarPorUsuarioUnidadeEnsinoVinculadaAoUsuario(Integer codUsuario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	UnidadeEnsinoVO consultaRapidaPorCodigo(Integer codUnidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultarPorProcessoSeletivo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultarUnidadeEnsinoFaltandoLista(List<UnidadeEnsinoVO> unidadeEnsinoVOs, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	UnidadeEnsinoVO consultarPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	UnidadeEnsinoVO obterUnidadeMatriz(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultarUnidadeEnsinoOndeCursoDiferenteDePosGraduacao(int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultarPorProfessor(Integer professor, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultaRapidaPorNomeAutoComplete(String valorConsulta, int limit, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void validarDadosUnidadeEnsino(Integer unidadeEnsino) throws Exception;

	void carregarDados(UnidadeEnsinoVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultarPorCoordenador(Integer coordenador, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultarTodasUnidades(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultarUnidadeEnsinoComboBox(Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	UnidadeEnsinoVO consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultarUnidadeEnsinoProfessorPorCodigoPessoa(Integer pessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void upLoadLogoUnidadeEnsino(FileUploadEvent upload, UnidadeEnsinoVO unidadeEnsinoVO, String tipoLogo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

	Integer consultarCodigoPessoaCoordenadorTCC(Integer unidadeEnsino, Integer curso) throws Exception;

	UnidadeEnsinoVO consultarPorContaReceberDadosLogoRelatorio(Integer contaReceber, UsuarioVO usuario) throws Exception;
	
	UnidadeEnsinoVO consultarPorUnidadeEnsinoDadosLogoRelatorio(Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

	UnidadeEnsinoVO consultarPorTurma(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultarPorUsuarioNomeEntidadePermissao(String nomeEntidade, String permissao, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	UnidadeEnsinoVO consultaRapidaResponsavelCobrancaUnidadePorCodigo(Integer codMatriculaPeriodoTurmaDisciplina) throws Exception;

	List<UnidadeEnsinoVO> consultarPorUsuario(UsuarioVO usuarioVO) throws Exception;

	UnidadeEnsinoVO consultaRapidaPorChavePrimariaDadosBasicosBoleto(Integer codUnidadeEnsino, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultarUnidadeEnsinoDoProcessoMatriculaPorCodigoBanner(Integer codigoBanner, UsuarioVO usuarioVO) throws Exception;
	
//	public List<UnidadeEnsinoVO> consultarPorProcSeletivoComboBox(List<ProcSeletivoVO> listaProcSeletivoVOs, Boolean possuiUnidadeEnsinoLogada, UsuarioVO usuarioVO);
	
	public List<UnidadeEnsinoVO> consultarPorNomeVinculadoBiblioteca(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;	
	
	public String renderizarLogoAplicativo(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, String pastaBase, String nomeImagem);

	List<UnidadeEnsinoVO> consultarPorUnidadeEnsinoPorBiblioteca(Integer biblioteca, Integer unidadeEnsino,
			boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	List<UnidadeEnsinoVO> consultarPorConfiguracoes(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<UnidadeEnsinoVO> consultarPorConfiguracaoAtendimento(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorProfessor(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public UnidadeEnsinoVO consultarUnidadeDeEnsinoPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultarRapidaPorConfiguracaoContabil(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<UnidadeEnsinoVO> consultarRapidaTodosCodigoContabilUnidadeEnsino(boolean controlarAcesso, UsuarioVO usuario) throws Exception;

//	void atualizarUnidadeEnsinoDadosConfiguracaoContabil( UsuarioVO usuario) throws Exception;	

	void addUnidadeEnsinoCursoCentroResultado(UnidadeEnsinoVO obj, UnidadeEnsinoCursoCentroResultadoVO unidadeEnsinoCentroResultadoVO, UsuarioVO usuario);
	
	void removerUnidadeEnsinoCursoCentroResultado(UnidadeEnsinoVO obj, UnidadeEnsinoCursoCentroResultadoVO unidadeEnsinoCentroResultadoVO, UsuarioVO usuario);
	
	public List<UnidadeEnsinoVO> consultarPorArtefatoEntregaAluno(Integer artefato, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultarUnidadeEnsinoPessoaEBiblioteca(Integer pessoa, Integer biblioteca,
			boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultarUnidadeEnsinoAcademicaEFinanceiraConformeUnidadeEnsinoLogada(
			Integer unidadeEnsinoLogada, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public  UnidadeEnsinoVO consultaRapidaResponsavelNotificacaoAlteracaoCronogramaAulaPorCodigoUnidadeEnsino(Integer codigo) throws Exception;

	UnidadeEnsinoVO consultarPorNome(String nome, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	UnidadeEnsinoVO consultarPorCodigo(Integer codigo, UsuarioVO usuarioVO);

	UnidadeEnsinoVO consultarPorChavePrimariaUnica(Integer codigoPrm, UsuarioVO usuario) throws Exception;
	
	UnidadeEnsinoVO consultarUnidadesVinculadaConfiguracaoGed(List<ProgramacaoFormaturaUnidadeEnsinoVO> listaProgramacao, int nivelMontardados, UsuarioVO usuarioLogado) throws Exception;


	UnidadeEnsinoVO consultarSeExisteUnidadeMatrizParaExpedicaoDiploma( Boolean controlarAcesso,UsuarioVO usuario) throws Exception;

	UnidadeEnsinoVO consultarCodigoUnicaUnidadeEnsinoPorNome(String unidadeEnsino, int nivelMontarDados,
			UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultarUnidadeEnsinoComboBox(Integer unidadeEnsino, boolean controlarAcesso,
			boolean apenasAtivas, UsuarioVO usuario) throws Exception;

	List<UnidadeEnsinoVO> consultarPorCursoAtivo(Integer unidadeEnsino, Integer curso, Integer turno,
			Boolean validarVagasPorComputador, Boolean considerarVagaCurso, String ano, String semestre, DiaSemana diaSemana, boolean controlarAcesso,
			int nivelMontarDados, UsuarioVO usuario) throws Exception;

	UnidadeEnsinoRSVO consultarUnidadeEnsinoMatriculaOnlineProcessoSeletivo(Integer codUnidadeEnsino, UsuarioVO usuario,
			RenovarMatriculaControle renovarMatriculaControle);
	
	List<UnidadeEnsinoVO> consultarTodasUnidadeEnsinoVagaOfertada() throws Exception;
	
	public File carregarExcelVagaOfertadaUnidadeEnsino(List<UnidadeEnsinoVO> unidadeEnsinoVOs, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO ) throws Exception;
    
	public List<UnidadeEnsinoVO> importarPlanilhaUnidadeEnsinoVagaOfertada(FileUploadEvent uploadEvent, UsuarioVO usuario) throws Exception;
    
	void alterarNumeroVagaOfertadaUnidadeEnsino(UnidadeEnsinoVO obj) throws Exception;

}