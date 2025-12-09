package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.ExpedicaoDiplomaVO;
//import negocio.comuns.academico.GestaoXmlGradeCurricularVO;
import negocio.comuns.academico.enumeradores.DocumentoAssinadoOrigemEnum;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ProvedorDeAssinaturaEnum;


public interface DocumentoAssinadoPessoaInterfaceFacade {

	void persistir(List<DocumentoAssinadoPessoaVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	DocumentoAssinadoPessoaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<DocumentoAssinadoPessoaVO> consultarDocumentosAssinadoPessoaPorDocumentoAssinado(DocumentoAssinadoVO obj, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
	
	Boolean consultaSeExisteDocumentoAssinadoPessoaPorCodigoPorSituacao(Integer codigo,SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum) throws Exception;

	void atualizarDadosAssinatura(final DocumentoAssinadoPessoaVO obj, Boolean assinarPorCNPJ, Integer ordemAssinatura, String provedorAssinatura) throws Exception;

	void atualizarSituacaoPendenteDocumentoAssinadoPessoaParaRejeitado(final DocumentoAssinadoVO obj, final String motivo, UsuarioVO usuario) throws Exception;
	
	void atualizarSituacaoRejeitadoDocumentoAssinadoPessoaParaPendente(final DocumentoAssinadoVO obj,  UsuarioVO usuario) throws Exception;
	
	void atualizarDadosRejeicao(DocumentoAssinadoPessoaVO obj) throws Exception;
	
	public DocumentoAssinadoPessoaVO consultarDocumentosAssinadoPessoaPorArquivoEPessoaDocumentoAssinado(PessoaVO pessoa, ArquivoVO arquivoVO , boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;
	
	public void excluir(DocumentoAssinadoPessoaVO obj, UsuarioVO usuario) throws Exception;

	void excluirPendente(DocumentoAssinadoPessoaVO obj, UsuarioVO usuario) throws Exception;

	public void excluirPorCodigoDocumentoAssinado(Integer codigo, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	Integer atualizarDadosAssinaturaPorProvedorCertisign(String codigoProvedorAssinatura, String nome, String cpf, SituacaoDocumentoAssinadoPessoaEnum situacao, String motivoRejeicao , Date dataAssinaturaOrRejeicao, DocumentoAssinadoOrigemEnum documentoAssinadoOrigemEnum, String jsonAssinatura) throws Exception;
	
	DocumentoAssinadoPessoaVO consultarDocumentosAssinadoPessoaPorMatricula(String matriculaAluno) throws Exception;
	
	void rejeitarContratoAssinadoPendenteAutomaticamente(DocumentoAssinadoPessoaVO obj, UsuarioVO usuarioLogado);
	
	public DocumentoAssinadoPessoaVO consultarDocumentosAssinadoPessoaPorMatriculaMatriculaPeriodo(String matriculaAluno , Integer matriculaPeriodo) throws Exception;

	public void atualizarEmailSignatarioConcedente(DocumentoAssinadoPessoaVO obj) throws Exception;

	public void atualizarSituacaoPendenteDocumentoAssinadoAlunoParaRejeitado(DocumentoAssinadoVO obj, String motivo, UsuarioVO usuario) throws Exception;

	public void atualizarDadosRejeicaoDocumentosAssinados(DocumentoAssinadoPessoaVO obj, List<DocumentoAssinadoVO> documentoAssinadoVOs) throws Exception;

	public void realizarRejeicaoDocumentosAssinados(ExpedicaoDiplomaVO expedicaoDiplomaVO, Boolean apresentarMensagemAssinaturaDigital, UsuarioVO usuarioVO) throws Exception;

	public void realizarRejeicaoDocumentosAssinados( UsuarioVO usuarioVO) throws Exception;

	Integer atualizarDadosAssinaturaPorProvedorTechCert(String codigoProvedorAssinatura, String nome, String cpf, SituacaoDocumentoAssinadoPessoaEnum situacao, String motivoRejeicao, Date dataAssinaturaOrRejeicao, DocumentoAssinadoOrigemEnum documentoAssinadoOrigemEnum, String jsonAssinatura, String urlAssinatura) throws Exception;

	Integer atualizarDadosAssinaturaPorProvedorTechCertUrl(String chaveProvedordeAssinatura, String nome, String cpf, SituacaoDocumentoAssinadoPessoaEnum situacao, DocumentoAssinadoOrigemEnum documentoAssinadoOrigemEnum, String jsonAssinatura, String urlAssinatura, String urlProvedorDeAssinatura) throws Exception;

	void realizarTrocaResponsavelAssinaturaDocumento(DocumentoAssinadoVO obj, PessoaVO novaPessoa, Integer codigoDocumentoAssinadoPessoaAlteracao, ConfiguracaoGeralSistemaVO configSistemaVO, UsuarioVO usuarioVO);

	void reordenarAssinatesDocumentoAssinado(DocumentoAssinadoVO documentoAssinadoVO, UsuarioVO usuarioVO, ConfiguracaoGEDVO configuracaoGEDVO);

	void processarTrocaResponsavelAssinaturaDocumentoAssinado(DocumentoAssinadoPessoaVO obj, ProvedorDeAssinaturaEnum provedorAssintura, PessoaVO novaPessoa, Integer codigoUnidadeEnsino, DocumentoAssinadoVO documentoAssinadoVO, UsuarioVO usuarioVO) throws Exception;

	void realizarTrocaDeAssinanteTechCert(DocumentoAssinadoPessoaVO documentoAssinadoPessoa, DocumentoAssinadoVO documentoAssinadoVO, PessoaVO novaPessoa, ConfiguracaoGEDVO configGEDVO, UsuarioVO usuarioVO) throws Exception;

}
