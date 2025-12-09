package negocio.interfaces.academico;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.SetranspVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import webservice.aws.s3.ServidorArquivoOnlineS3RS;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface ArquivoInterfaceFacade {

	public ArquivoVO novo() throws Exception;

	public void incluir(ArquivoVO obj, boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public void incluir(ArquivoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public void alterar(ArquivoVO obj, boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public void alterar(ArquivoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public void excluir(ArquivoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public ArquivoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public ArquivoVO consultarPorCodOrigemInscricao(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public ArquivoVO consultarPorCodigo(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<ArquivoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ArquivoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ArquivoVO> consultarPorDataUpload(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ArquivoVO> consultarPorCodigoUsuario(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ArquivoVO> consultarPorDataDisponibilizacao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ArquivoVO> consultarPorNomeDisciplina(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ArquivoVO> consultarPorIdentificadorTurmaTurma(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ArquivoVO> consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ArquivoVO> consultarArquivoPorTurmaDisciplinaOrigemAtivos(Integer turma, Integer disciplina, OrigemArquivo origemArquivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public byte[] consultarArquivoPorCodigoArquivo(Integer codigo, UsuarioVO usuario) throws Exception;

	public List<ArquivoVO> consultarArquivoPorTurmaDisciplinaOrigemInativos(Integer turma, Integer disciplina, OrigemArquivo origemArquivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ArquivoVO> consultarArquivoPorResponsavelUploadTurmaDisciplinaOrigemInativos(Integer responsavelUpload, Integer turma, Integer disciplina, OrigemArquivo origemArquivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ArquivoVO> consultarArquivoPorResponsavelUploadTurmaDisciplinaOrigemAtivos(Integer responsavelUpload, Integer turma, Integer disciplina, OrigemArquivo origemArquivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public void validarDownloadArquivoAluno(UsuarioVO usuario) throws Exception;

	// public List consultarArquivoPorDisciplinaTurma(Integer codigo, Integer
	// codigo2, boolean b, int nivelmontardadosTodos, UsuarioVO usuario) throws
	// Exception;
	public List<ArquivoVO> consultarArquivoPorDisciplinaTurma(Integer disciplina, Integer turma, Integer professor, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, String campoConsultaSituacao, String anoPeriodoUpload, String semestrePeriodoUpload, String anoPeriodoDisponibilizacao, String semestrePeriodoDisponibilizacao) throws Exception;
	
	public List<ArquivoVO> consultarArquivosRespostaDosAlunos(OrigemArquivo aluno, Integer codigo, boolean b, int nivelmontardadosTodos, UsuarioVO usuario) throws Exception;

	public void incluirArquivoSetransp(SetranspVO setranspVO, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public void excluirArquivoSetransp(SetranspVO setranspVO, UsuarioVO usuario) throws Exception;

	public String criarNomeArquivoSetransp(String valorConsultaFiltros, UsuarioVO usuario);

	public ArquivoVO montarArquivo(byte[] byteArray, String nome, String extensao, String origem, UsuarioVO usuario);

	public List<ArquivoVO> consultarPorCodOrigemTipoOrigem(Integer codigo, String tipoOrigem, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void incluirArquivos(List<ArquivoVO> arquivoVOs, int codigo, OrigemArquivo origemArquivo, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public void alterarManterDisponibilizacao(ArquivoVO obj, UsuarioVO usuario) throws Exception;

	public ArquivoVO consultarDadosDoArquivoASerRespondido(Integer codigoArquivo, UsuarioVO usuario) throws Exception;

	public void excluirArquivoDoDiretorioEspecifico(ArquivoVO arquivoVO, String caminhoPasta) throws Exception;

	public void validarDadosDisponibilizarMaterialAcademico(ArquivoVO obj) throws Exception;

	public void excluir(ArquivoVO obj, boolean verificarAcesso, String nomeEntidade, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public void alterarManterDisponibilizacao(ArquivoVO obj, boolean verificarAcesso, String nomeEntidade, UsuarioVO usuario) throws Exception;

	public String executarDefinicaoUrlAcessoArquivo(ArquivoVO arquivoVO, PastaBaseArquivoEnum pastaBaseArquivoEnum, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO);

	public List<ArquivoVO> consultarArquivoAtivosPorMatriculaDisciplinaAnoSemestreOrigem(String matricula, Integer disciplina, String ano, String semestre, String origem) throws Exception;
	                                                                                                                             
	public List<ArquivoVO> consultarArquivoAtivosPorProfessorDisciplinaTurmaVisaoProfessor(Integer professor, Integer turma, Integer disciplina, String campoConsultaSituacao, String anoPeriodoUpload, String semestrePeriodoUpload, String anoPeriodoDisponibilizacao, String semestrePeriodoDisponibilizacao) throws Exception;

	public List<ArquivoVO> consultarArquivosAtivosPorDisciplinaTurmaInstituicaoVisaoProfessor(Integer professor, Integer turma, Integer disciplina) throws Exception;

	public Boolean verificarDataDownloadDentroQuantidadeDiasMaximoLimite(String matricula, CursoVO curso, TurmaVO turma, DisciplinaVO disciplina) throws Exception;

	List<ArquivoVO> consultarArquivosPorSituacaoPorNivelEducacional(String nivelEducacional, SituacaoArquivo situacaoArquivo, UsuarioVO usuarioVO) throws Exception;

	public List<ArquivoVO> consultarArquivoInstituicionalParaProfessor(UsuarioVO usuarioVO, Integer limit) throws Exception;

	public List<String> consultarNomeArquivoPorCodigoDisciplina(Integer codigoExcluir) throws Exception;

	public void verificarArquivosExistentesHD(ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

	public void excluirPorDocumentacaoMatriculaRequerimento(ArquivoVO obj, boolean verificarAcesso, String nomeEntidade, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public ArquivoVO consultarPorChavePrimariaConsultaCompleta(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ArquivoVO> consultarPorVariosCodigo(String listaCodigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ArquivoVO> consultarArquivoInstituicionalParaCoordenador(UsuarioVO usuarioVO, Integer limit) throws Exception;

	SqlRowSet consultarAlunoNotificarDownloadMaterial();

	void alterarArquivos(Integer codigo, List<ArquivoVO> objetos, OrigemArquivo origemArquivo, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public void removerAcentosCaracteresEspeciaisArquivos(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

	public List<ArquivoVO> consultarArquivoAtivosPorDisciplinaTurmaVisaoCoordenador(Integer turma, Integer disciplina) throws Exception;

	public List<ArquivoVO> consultarArquivosAtivosPorDisciplinaTurmaInstituicaoVisaoCoordenador(Integer turma, Integer disciplina) throws Exception;

	public List<ArquivoVO> consultarArquivoAtivosPorDisciplinaTurmaVisaoCoordenador(Integer turma, Integer disciplina, Integer unidadeEnsino  , String campoConsultaSituacao, String anoPeriodoUpload, String semestrePeriodoUpload, String anoPeriodoDisponibilizacao, String semestrePeriodoDisponibilizacao) throws Exception;

	List<ArquivoVO> consultarArquivosPorSituacaoInstitucional(ArquivoVO arquivoVO, SituacaoArquivo situacaoArquivo, boolean apresentarVisaoAluno, boolean apresentarVisaoProfessor, boolean apresentarVisaoCoordenador) throws Exception;

	List<ArquivoVO> consultarArquivoInstituicionalParaAluno(UsuarioVO usuarioVO, String matricula, Integer limit) throws Exception;

	public Integer consultarQuantidadeMaterialPostado(Integer professor, Integer disciplina, Integer turma) throws Exception;

	void incluirBackUp(ArquivoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	List<ArquivoVO> consultarArquivoPorDisciplinaTurmaBackUp(Integer disciplina, Integer turma, Integer professor, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void excluirBackUp(ArquivoVO obj, boolean verificarAcesso, String nomeEntidade, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public List<ArquivoVO> consultarArquivosPorPastaBaseArquivo(String pastaBaseArquivo) throws Exception;

	void alterarDisponibilidadeMaterial(ArquivoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	SqlRowSet consultarAlunoEmPeriodoAulaNotificarDownloadMaterial(String dataAtual, Integer disciplina, Integer turma, Integer professor);

	/**
	 * @author Wellington Rodrigues - 10 de ago de 2015
	 * @param arquivoVOs
	 * @param arquivoVO
	 * @param arquivoVO2
	 * @param usuario
	 * @param configuracaoGeralSistemaVO
	 * @throws Exception
	 */
	void alterarOrdemArquivo(List<ArquivoVO> arquivoVOs, ArquivoVO arquivo1, ArquivoVO arquivo2, UsuarioVO usuario) throws Exception;

	/**
	 * @author Wellington Rodrigues - 11 de ago de 2015
	 * @param arquivoVOs
	 * @param arquivoVO
	 * @throws Exception
	 */
	void adicionarArquivoIndice(List<ArquivoVO> arquivoVOs, ArquivoVO arquivoVO) throws Exception;

	/**
	 * @author Wellington Rodrigues - 11 de ago de 2015
	 * @param arquivoVOs
	 * @param arquivo1
	 * @param arquivo2
	 * @param usuario
	 * @throws Exception
	 */
	void alterarOrdemArquivoDragDrop(List<ArquivoVO> arquivoVOs, List<ArquivoVO> arquivoFilhoVOs, ArquivoVO arquivo1, ArquivoVO arquivo2, UsuarioVO usuario) throws Exception;

	/**
	 * @author Wellington Rodrigues - 12 de ago de 2015
	 * @param arquivoVOs
	 * @param listaArquivoConsultado
	 * @throws Exception
	 */
	Map<String, ArquivoVO> montarDadosArquivoIndice(List<ArquivoVO> listaArquivoConsultado, UsuarioVO usuario) throws Exception;

	/**
	 * @author Wellington Rodrigues - 12 de ago de 2015
	 * @param obj
	 * @param verificarAcesso
	 * @param usuario
	 * @param configuracaoGeralSistemaVO
	 * @throws Exception
	 */
	void persistir(ArquivoVO obj, boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	/**
	 * @author Wellington Rodrigues - 12 de ago de 2015
	 * @param arquivoVOs
	 * @param arquivo1
	 * @param arquivo2
	 * @param usuario
	 * @throws Exception
	 */
	void alterarOrdemArquivoFilho(List<ArquivoVO> arquivoVOs, ArquivoVO arquivo1, ArquivoVO arquivo2, UsuarioVO usuario) throws Exception;

	/**
	 * @author Wellington Rodrigues - 13 de ago de 2015
	 * @param arquivoVO
	 * @param pastaBaseArquivoEnum
	 * @param configuracaoGeralSistemaVO
	 * @return
	 */
	String executarDefinicaoUrlFisicoAcessoArquivo(ArquivoVO arquivoVO, PastaBaseArquivoEnum pastaBaseArquivoEnum, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO);

	void carregarArquivoDigitalmenteAssinado(ArquivoVO obj, ConfiguracaoGeralSistemaVO confg, UsuarioVO usuarioLogado) throws Exception;

	List<File> consultarArquivosNaoVinculadosSistema(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean filtrarPastaArquivo, boolean filtrarPastaImagem, Integer loteFile) throws Exception;

	/*List<ArquivoVO> consultarArquivoAssinado(String campoConsulta, String valorConsulta, int limite, int pagina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	Integer consultarTotalRegistroArquivoAssinados(String campoConsulta, String valorConsulta, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;*/


	/**
	 * @author Rodrigo Wind - 26/02/2016
	 * @param arquivoVOs
	 * @return
	 * @throws Exception
	 */
//	Map<String, ArquivoVO> realizarSeparacaoArquivosPorDisciplinaTurmaProfessor(List<ArquivoVO> arquivoVOs) throws Exception;

	public List<ArquivoVO> consultarBackupPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public ArquivoVO consultarBackupPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void excluirArquivoQueNaoEstaNaListaComCodigoOrigemComOrigemArquivo(List<ArquivoVO> listaArquivo,
			String codigoOrigem, OrigemArquivo origemArquivo, PastaBaseArquivoEnum pastaBaseArquivoEnum,
			int nivelMontarDados, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema)
			throws Exception;
	
	public List<ArquivoVO> consultarMateriaisPendentesAluno(String matricula, String ano, String semestre, String origem, String periodicidade, Integer matriculaPeriodoTurmaDisciplina) throws Exception;

	public SqlRowSet consultarMateriaisAlunosQueSofreramAlteracao(Integer horas);
	public void alterarCodigoOrigemArquivo(ArquivoVO arquivoVO, UsuarioVO usuario) throws Exception;
	
	public void incluirArquivoNoServidorOnline(final ArquivoVO obj, boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
	
	public List<ArquivoVO> consultarArquivosQuestaoEadPorDisciplinaPastaBaseArquivo(String pastaBaseArquivo, Integer codigoDisciplina) throws Exception;
	
	public ArquivoVO consultarPorCodigoIntegracaoMoodle(Long codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public void atualizarDataDisponibilizacao(final Integer turma, UsuarioVO usuario) throws Exception;
	
	public SqlRowSet consultarArquivosQueForamExcluidos(Integer hora) ;
	
	public Boolean verificarExisteArquivoMesmoNomeAnoUpload(String valorConsulta, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception;


	public ArquivoVO consultarPorCodOrigemRequisicao(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<ArquivoVO> consultarArquivosBibliotecaExterna(Integer codigoCatalogo) throws Exception;
	
	public String consultarCapaCatalogo(Integer codigoCatalogo) throws Exception;

	
	public void excluirRegistroArquivoAmazonPorDocumentacaoMatricula(ArquivoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;
	
	ArquivoVO consultarAssinaturaDigitalFuncionarioPorCodigoFuncionario(Integer codigoFuncionario, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	void alterarNomeArquivo(Integer arquivo, String nome, UsuarioVO usuario) throws Exception;

	void alterarDescricaoArquivo(Integer arquivo, String descricao, UsuarioVO usuario) throws Exception;

	void alterarApresentacaoArquivoInstitucionalProfessorCoordenadorAluno(ArquivoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	void alterarSituacaoArquivo(ArquivoVO obj, SituacaoArquivo situacaoArquivo, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	List<ArquivoVO> consultarArquivosQuestaoProcessoSeletivoPorDisciplina(String pastaBaseArquivo,
			Integer codigoDisciplina) throws Exception;

	Integer consultarQtdeMateriaisPendentesAluno(String matricula, String ano, String semestre, String origem,
			String periodicidade, Integer matriculaPeriodoTurmaDisciplina) throws Exception;

	void validarDadosExtensaoArquivoTipoMarketing(ArquivoVO arquivoVO, UsuarioVO usuarioVO) throws Exception;

	List<ArquivoVO> consultarPorCodOrigemTipoOrigemTipoRelatorio(Integer codigo, String tipoOrigem,
			TipoRelatorioEnum tipoRelatorio, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void excluirArquivoPastaBaseArquivo(ArquivoVO arquivoVO, UsuarioVO usuario,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	String realizarDownloadArquivoAmazon(ArquivoVO obj, ServidorArquivoOnlineS3RS servidorArquivoOnlineS3RS,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean disponibilizarArquivoAssinadoParaDowload)
			throws Exception;

	String realizarVisualizacaoPreview(ArquivoVO arquivoVO) throws Exception;

	public void realizarUploadServidorAmazon(ServidorArquivoOnlineS3RS servidorExternoAmazon, ArquivoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean isDeletarAqruivoExistente) throws ConsistirException;

	public void atualizarArquivoIsPdfA(ArquivoVO arquivoVO);
}