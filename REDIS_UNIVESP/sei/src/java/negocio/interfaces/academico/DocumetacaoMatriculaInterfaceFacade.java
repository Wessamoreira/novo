package negocio.interfaces.academico;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.richfaces.event.FileUploadEvent;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.AssinarDocumentoEntregueVO;
import negocio.comuns.academico.DocumentacaoGEDVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em especial com a classe
 * Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais. Além de
 * padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio de sua classe Façade (responsável por persistir
 * os dados das classes VO).
 */
public interface DocumetacaoMatriculaInterfaceFacade {

	public DocumetacaoMatriculaVO novo() throws Exception;

	public void incluir(DocumetacaoMatriculaVO obj, PessoaVO aluno, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

	public void alterar(DocumetacaoMatriculaVO obj, PessoaVO aluno, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

	public void excluir(DocumetacaoMatriculaVO obj, UsuarioVO usuario) throws Exception;

	public List<DocumetacaoMatriculaVO> consultarPorSituacao(String valorConsulta, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List<DocumetacaoMatriculaVO> consultarPorProcessadoComErro(Boolean status, String matricula, String aluno, String nomeDocumento, Date dataInicio, Date dataFim, int nivelMontarDados, int limite, int pagina, boolean controlarAcesso, DataModelo dataModelo,  UsuarioVO usuario) throws Exception;

	public List<DocumetacaoMatriculaVO> consultarPorTipoDeDocumento(Integer valorConsulta, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<DocumetacaoMatriculaVO> consultarPorCodigo(Integer valorConsulta, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void excluirDocumetacaoMatriculas(List<DocumetacaoMatriculaVO> documetacaoMatriculaVOs, String matricula, UsuarioVO usuario) throws Exception;

	public void alterarDocumetacaoMatriculas(MatriculaVO matriculaVO, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean validarExclusaoDocumentos) throws Exception;

	public void incluirDocumetacaoMatriculas(MatriculaVO matriculaVO, String matriculaPrm, List<DocumetacaoMatriculaVO> objetos, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

	public List<DocumetacaoMatriculaVO> consultarDocumetacaoMatriculas(String matricula, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String idEntidade);

	public List<DocumetacaoMatriculaVO> consultarPorNomeDoAluno(String valorConsulta, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<DocumetacaoMatriculaVO> consultarPorSituacaoMatricula(String string, String matricula, int nivelMontarDados, boolean b, UsuarioVO usuario) throws Exception;

	public List<DocumetacaoMatriculaVO> consultarDocumetacaoMatriculaPorMatriculaAluno(String matricula, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<DocumetacaoMatriculaVO> consultarPorMatricula(String valorConsulta, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<DocumetacaoMatriculaVO> consultarDocumetacaoMatriculaPorMatriculaAlunoEntregue(String matricula, int nivelMontarDados, boolean entregue, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<DocumetacaoMatriculaVO> consultaRapidaPorMatricula(String matricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void alterarCodigoArquivo(DocumetacaoMatriculaVO obj, Integer codArquivo, UsuarioVO usuario) throws Exception;

	public void alterarCodigoArquivoVerso(DocumetacaoMatriculaVO obj, Integer codArquivo, UsuarioVO usuario) throws Exception;

	public Boolean verificaAlunoDevendoDocumentoQueSuspendeMatricula(String matriculaAluno) throws Exception;

	public List<DocumetacaoMatriculaVO> consultarDocumetacaoMatriculaPorMatriculaAlunoPendenteEntregaSuspendeMatricula(String matricula, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	DocumetacaoMatriculaVO consultarDocumentacaoMatriculaExigenciaAlunoASerReaproveitado(Integer aluno, Integer tipoDocumento, UsuarioVO usuario) throws Exception;

	void alteraDocumentoAluno(final String situacao, final Date dataEntrega, final Integer arquivo, final Integer arquivoVerso, final Integer arquivoAssinado, final Integer aluno, final Integer tipoDeDocumento, final Integer responsavel, final String matricula, final Boolean entregue, final Boolean possuiArquivo);

	public void alterarDocumentoMatriculaAprovaPeloDep(DocumetacaoMatriculaVO obj, UsuarioVO usuario) throws Exception;

	public List<DocumetacaoMatriculaVO> consultarDocumentoPendenteAprovacao(String matricula, Boolean trazerDocumentosIndeferidos, Boolean trazerDocumentosDeferidos, Boolean trazerDocumentosPendentes) throws Exception;

	public void alterarDocumentoMatriculaNegadoPeloDep(DocumetacaoMatriculaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

	public List<DocumetacaoMatriculaVO> consultarDocumentoAprovadoPendenteNotificar(String matricula) throws Exception;

	public DocumetacaoMatriculaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void alterarDocumentoMatriculaNotificado(DocumetacaoMatriculaVO obj, UsuarioVO usuario) throws Exception;

	public void alterarDocumentoMatriculaAlunoPostagemNotificado(DocumetacaoMatriculaVO obj, UsuarioVO usuario) throws Exception;

	public List<DocumetacaoMatriculaVO> consultarDocumentoAprovadoPendenteNotificarAlunoDocPostado(String matricula) throws Exception;

	String consultarExistenciaPendenciaDocumentacaoPorMatricula(Integer pessoa, String matricula, List<TipoDocumentoVO> documentosValidarPendenciaVOs, boolean validarTodosDocumentosEntregues) throws Exception;

	/** 
	 * @author Wellington - 19 de fev de 2016 
	 * @param matriculaVO
	 * @param usuario
	 * @throws Exception 
	 */
	void executarGeracaoSituacaoDocumentacaoMatricula(MatriculaVO matriculaVO, UsuarioVO usuario) throws Exception;

	public DocumetacaoMatriculaVO consultarPorArquivoGED(ArquivoVO arquivo);	

	public DocumetacaoMatriculaVO consultarPorTipoDeDocumentoMatricula(Integer codigo, String matricula,
			int nivelmontardadosTodos, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void alterarDocumentacaoGed(DocumetacaoMatriculaVO obj, UsuarioVO usuario) throws Exception;

	public void excluirDocumentacaoMatricula(DocumetacaoMatriculaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean verificarAcesso, UsuarioVO usuario) throws Exception;
	
	Boolean realizarVerificacaoDocumentacaoMatriculaVinculadoArquivoGed(Integer arquivo, Integer documentacaoMatricula) throws Exception;

	void removerVinculoDocumentacaoGed(DocumentacaoGEDVO obj, UsuarioVO usuario) throws Exception;

	void removerVinculoDocumentacaoGed(DocumentacaoGEDVO obj, DocumetacaoMatriculaVO documetacaoMatriculaVO, UsuarioVO usuario) throws Exception;

	public Boolean consultarContratoAssinadoPorMatriculaAlunoEntregue(String matricula, UsuarioVO usuario) throws Exception;

	String consultarExistenciaPendenciaDocumentacaoPorMatriculaPorGerarSuspensao(String matricula,boolean isGerarSuspensaoMatricula) throws Exception;
	
	public Boolean consultarSeExistemDocumentosPendentesAluno(String matricula, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public Boolean validaAlunoAssinouContratoMatricula(String matricula) throws Exception;
	
	List<DocumetacaoMatriculaVO> consultarDocumentosPendentesPorMatricula(String matricula, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List<DocumetacaoMatriculaVO> consultarDocumentoMatriculaEntregue(AssinarDocumentoEntregueVO assinarDocumentoEntregueVO,  Integer nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public File unificarFrenteVersoDocumentoMatricula(DocumetacaoMatriculaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO , UsuarioVO usuarioVO) throws Exception;
	
	public void assinarDocumentacaoMatricula(DocumetacaoMatriculaVO obj, UsuarioVO usuario) throws Exception;
	
	void verificarDocumentosObrigatoriosACadaRenovacao(MatriculaVO matriculaVO , String ano , String semestre) throws Exception;
	
	public void atualizarStatusDocumentacaoMatricula(DocumetacaoMatriculaVO documetacaoMatriculaVO, String texto, Boolean statusProcessamento, final UsuarioVO usuarioVO);
	
	public void realizarAssinaturaDocumentoJOB(DocumetacaoMatriculaVO documetacaoMatriculaVO, final UsuarioVO usuarioVO) throws Exception;
	

	public void validarExtensaoArquivoFrente(String fileName, DocumetacaoMatriculaVO documetacaoMatriculaVO) throws Exception;
	public void validarExtensaoArquivoVerso(String fileName, DocumetacaoMatriculaVO documetacaoMatriculaVO) throws Exception;
	
	public void gravarDocumentacaoMatriculaVisaoAluno(List<DocumetacaoMatriculaVO> documetacaoMatriculaVOs, MatriculaVO matriculaVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean validarExclusaoDocumentos) throws Exception;
	
	public Integer consultarTotalDocumentosProcessados(Boolean status, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public void removerProfessorListaAssinaturaDigital(ArquivoVO arquivoVO,FuncionarioVO obj, UsuarioVO usuarioLogado) throws Exception;
	
	public void alterarDocumentoMatriculaAprovaPeloDep(DocumetacaoMatriculaVO obj, MatriculaVO matricula, ConfiguracaoFinanceiroVO config, UsuarioVO usuario) throws Exception;

	void realizarPreencherCaminhoAnexoImagemDocumentacaoMatricula(List<DocumetacaoMatriculaVO> listaDocumentacaoMatriculaVO,ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO2) throws Exception;

	public boolean verificarAlunoEntregouTodosDocumentosQueSuspendeMatriculaParaAtivacaoMatricula(String matriculaAluno) throws Exception;

	void realizarUploadArquivo(FileUploadEvent uploadEvent, FileItem fileItem,
			DocumetacaoMatriculaVO documetacaoMatriculaVO, PessoaVO aluno, boolean frente, boolean ged,
			DocumentacaoGEDVO documentacaoGEDVO, UsuarioVO usuarioVO, String ambiente) throws Exception;

	void removerVinculoMotivoIndeferimentoDocumentoAluno(DocumetacaoMatriculaVO documetacaoMatriculaVO,	UsuarioVO usuario) throws Exception;

	Boolean verificarBloqueioAprovacaoDocumentoMatriculaCalouroDeAcordoComPeriodoChamadaDoProcessoSeletivo(String matricula, Boolean documentoIndeferido ,Boolean validarVisaoAluno , UsuarioVO usuario) throws Exception;

	void validarPermissaoPermiteUploadDocumentoIndeferidoForaPrazoParaMatriculaProcessoSeletivo(MatriculaVO matricula,DocumetacaoMatriculaVO documetacaoMatriculaVO,
			UsuarioVO usuario) throws Exception;
	
	List<DocumetacaoMatriculaVO> consultarPorTipoDeDocumentoAluno(Integer codigoTipoDocumento, Integer aluno, int nivelmontardadosTodos, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List<DocumetacaoMatriculaVO> consultarDocumetacaoMatriculaPorMatriculaAluno(String matricula, int nivelMontarDados, Boolean enviarDocumentoXml, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<DocumetacaoMatriculaVO> consultarDocumentacoesMatriculasAssinadosParaCorrecaoPdfA(String matricula);
}
