package negocio.interfaces.academico;

import java.io.File;
import java.util.Date;
import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.AssinarDocumentoEntregueVO;
import negocio.comuns.academico.DocumentacaoGEDVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TipoDocumentoGEDVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracaoGEDVO;


public interface DocumentacaoGEDInterfaceFacade {

	public void persistir(DocumentacaoGEDVO obj, Boolean validarAcesso, List<TipoDocumentoGEDVO> listaAnteriorTipoDocumentoGED, UsuarioVO usuario) throws Exception;

	public void persistirComUploadArquivo(DocumentacaoGEDVO obj, List<TipoDocumentoGEDVO> listaAnteriorTipoDocumentoGED, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean validarAcesso, UsuarioVO usuario) throws Exception; 

	public void alterar(DocumentacaoGEDVO obj, Boolean validarAcesso, UsuarioVO usuario) throws Exception;

	public void excluir(DocumentacaoGEDVO obj, Boolean validarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;	

	public List<DocumentacaoGEDVO> consultarPorFiltro(String campoConsulta, String valorConsulta, boolean controlarAcesso, UsuarioVO usuario, String valorConsultaSituacao, DataModelo dataModelo) throws Exception;
	
	public int consultarTotal(String campoConsulta, String valorConsulta, boolean controlarAcesso, UsuarioVO usuario, String valorConsultaSituacao) throws Exception;

	public List<DocumentacaoGEDVO> consultarPorMatricula(String matricula, int todos, UsuarioVO usuarioLogado) throws Exception;

	public void alterarSituacao(DocumentacaoGEDVO obj, Boolean validarAcesso, UsuarioVO usuario) throws Exception;
	
	public void adicionarTipoDocumento(DocumentacaoGEDVO documentacaoGEDVO, TipoDocumentoVO tipoDocumentoVO, UsuarioVO usuarioVO) throws Exception;

	void realizarLocalizacaoDocumentacaoMatriculaVincularDocumentoGED(DocumentacaoGEDVO documentacaoGEDVO) throws Exception;

	DocumentacaoGEDVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados) throws Exception;
	
	void excluirDocumentacaoGEDPorMatricula(MatriculaVO matriculaVO, boolean validarAcesso, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
	
	public void assinarDocumentoGED(DocumentacaoGEDVO obj, UsuarioVO usuario) throws Exception;
	
	public List<DocumentacaoGEDVO> consultarDocumentoGEDEntregue(AssinarDocumentoEntregueVO assinarDocumentoEntregueVO,  Integer nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void realizarAssinaturaDocumentoGEDJOB(DocumentacaoGEDVO documentacaoGEDVO,UnidadeEnsinoVO unidadeEnsino,ConfiguracaoGEDVO configuracaoGEDVO, File fileAssinar, ConfiguracaoGeralSistemaVO configuracaoGeralSistema,UsuarioVO usuario) throws Exception;
	
	public void atualizarStatusDocumentacaoGED(DocumentacaoGEDVO documentacaoGEDVO, String descricao, Boolean statusProcessamento,  final UsuarioVO usuarioVO);
	
	public List<DocumentacaoGEDVO> consultarDocumentoGEDPorStatusProcessamento(Boolean status, int limite, int pagina, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public Integer consultarTotalDocumentosProcessados(Boolean status, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<DocumetacaoMatriculaVO> consultarPorMatriculaParaDocumentacaoXmlDiploma(String matricula, int todos, UsuarioVO usuarioLogado) throws Exception;
	
	/**
	 * Realiza o processo de leitura dos arquivo de texto e processa as informações
	 * para incluir os documentos GED recuperados de acordo com os tipos informados
	 * nos arquivos.
	 * 
	 * @param validarAcesso
	 * @param configuracaoGeralSistemaVO
	 * @param linhaArquivo
	 * @throws Exception
	 */
	DocumentacaoGEDVO realizarOperacoesDocumentacaoGED(Boolean validarAcesso,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, String linhaArquivo,
			List<String> identificadorTipoDocumentoCabecalho, UsuarioVO usuario, String caminhoBaseFinal,
			MatriculaVO matriculaVO, Date hoje, File lote) throws Exception;
}