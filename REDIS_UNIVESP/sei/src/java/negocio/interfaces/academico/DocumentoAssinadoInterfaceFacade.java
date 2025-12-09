package negocio.interfaces.academico;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipFile;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.EstagioVO;
import negocio.comuns.academico.ExpedicaoDiplomaVO;
import negocio.comuns.academico.GestaoXmlGradeCurricularVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.academico.ProgramacaoFormaturaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.AlinhamentoAssinaturaDigitalEnum;
import negocio.comuns.academico.enumeradores.DocumentoAssinadoOrigemEnum;
import negocio.comuns.academico.enumeradores.OperacaoDeVinculoEstagioEnum;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.*;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.ConfiguracaoGedOrigemVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ProvedorDeAssinaturaEnum;
import negocio.comuns.estagio.ConfiguracaoEstagioObrigatorioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProgressBarVO;
import relatorio.negocio.comuns.academico.HistoricoAlunoRelVO;
import webservice.certisign.comuns.CertiSignCallBackRSVO;
import webservice.certisign.comuns.CertiSignCallBackSignatureValidateRSVO;
import webservice.certisign.comuns.CertiSignRSVO;
import webservice.techcert.comuns.*;


public interface DocumentoAssinadoInterfaceFacade {

	

	DocumentoAssinadoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void incluir(DocumentoAssinadoVO obj, boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
	
	void atualizarDocumentoAssinadoInvalido(final Integer codigo, final boolean documentoAssinadoInvalido, final String motivo, UsuarioVO usuario) throws Exception;

	List<DocumentoAssinadoVO> consultarArquivoAssinado(String campoConsulta, String valorConsulta, int limite, int pagina, boolean controlarAcesso, int nivelMontarDados, boolean arquivoAssinadoDigitalmente, List<TipoOrigemDocumentoAssinadoEnum> listaTipoOrigemDocumentoAssinadoEnum, SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum, UsuarioVO usuarioLogado, Boolean trazerDocumentosDigitais, Boolean trazerDocumentosAssinadosInvalidos) throws Exception;

	Integer consultarTotalRegistroArquivoAssinados(String campoConsulta, String valorConsulta, Boolean controlarAcesso, int nivelMontarDados, boolean arquivoAssinadoDigitalmente, List<TipoOrigemDocumentoAssinadoEnum> listaTipoOrigemDocumentoAssinadoEnum, SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum, UsuarioVO usuarioLogado, Boolean trazerDocumentosDigitais, Boolean trazerDocumentosAssinadosInvalidos) throws Exception;	

	String realizarInclusaoDocumentoAssinadoPorImpostoRenda(String nomeArquivoOrigem, MatriculaVO matricula, GradeCurricularVO gradeCurricular, TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinado, ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum, String origemArquivo, String descricaoArquivo, String corAssinatura, Float alturaAssinatura, Float larguraAssinatura, ConfiguracaoGeralSistemaVO config, Integer codigoRequerimento, UsuarioVO usuarioVO) throws Exception;

	List<DocumentoAssinadoVO> consultarDocumentosAssinadoPorRelatorio(DocumentoAssinadoVO obj, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado, List<TipoOrigemDocumentoAssinadoEnum> tipoOrigemDocumentoAssinadoEnums, Integer limit) throws Exception;

	void excluir(DocumentoAssinadoVO obj, boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	DocumentoAssinadoVO consultarDocumentoAssinadoPorTurmaPorDisciplinaPorAnoPorSemestrePorTipoOrigemDocumentoAssinadoEnum(Integer turma, Integer disciplina, String ano, String semestre, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	String excutarVerificacaoPessoasParaAssinarDocumento(DocumentoAssinadoVO obj,  File fileAssinar, ConfiguracaoGeralSistemaVO config, boolean pemitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso, Boolean retornarExcecaoAssinaturaCoordenadorPrimeiro, UsuarioVO usuarioVO) throws Exception;

	void consultarDocumentos(DataModelo dataModelo, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO,
			DisciplinaVO disciplinaVO, String ano, String semestre, MatriculaVO matriculaVO,
			TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinado, Date dataInicio, Date dataTermino,
			SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum, PessoaVO pessoaAssinatura,
			UsuarioVO usuarioVO, Integer limit, Integer offset) throws Exception;
	
	public void consultarDocumentos(DataModelo dataModelo, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, String semestre, MatriculaVO matriculaVO, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinado, Date dataInicio, Date dataTermino, SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum, PessoaVO pessoaAssinatura, UsuarioVO usuarioVO, Integer limit, Integer offset, Integer ordemAssinaturaFiltrar) throws Exception;

	Boolean consultarSeExisteDocumentoAssinado(UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, String semestre, MatriculaVO matriculaVO, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinado, Date dataInicio, Date dataTermino, SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum, PessoaVO pessoaAssinatura, UsuarioVO usuarioVO);

	Integer consultarTotalDocumentoPendenteUsuarioLogado(UsuarioVO usuarioVO);
	
	DocumentoAssinadoVO consultarDocumentoAssinadoPorAlunoTipoOrigemCodigoOrigem(String matricula, Integer codigoOrigem, String origem, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	public String realizarInclusaoDocumentoAssinadoPorAtaResultadosFinais(String nomeArquivoOrigem, TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinado, ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum, String corAssinatura, Float alturaAssinatura, Float larguraAssinatura, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO, String alinhamentoAssinaturaDigital, UnidadeEnsinoVO unidadeEnsinoVO,  FuncionarioVO funcionarioPrincipal, String cargoFuncionario1, String tituloFuncionario1, FuncionarioVO funcionarioSecundario, String cargoFuncionario2, String tituloFuncionario2) throws Exception;
	
	DocumentoAssinadoVO realizarAssinaturaUnidadeCertificadoraExpedicaoDiploma(MatriculaVO matriculaVO, ExpedicaoDiplomaVO expedicaoDiplomaVO, ConfiguracaoGEDVO configGEDVO, File fileAssinar, FuncionarioVO funcionario1, String cargoFuncionario1, String tituloFuncionario1, FuncionarioVO funcionario2, String cargoFuncionario2, String tituloFuncionario2, FuncionarioVO funcionario3, String cargoFuncionario3, String tituloFuncionario3, String nomeArquivo, ConfiguracaoGeralSistemaVO config, String tipoLayout, UsuarioVO usuarioVO) throws Exception;
	
	public String realizarInclusaoDocumentoAssinadoPorEstagio(EstagioVO estagioVO,  TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum,  String nomeArquivoOrigem, ConfiguracaoEstagioObrigatorioVO configEstagio, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception;
	
	public ArquivoVO realizarAssinaturaDocumentacaoAluno(UnidadeEnsinoVO unidadeEnsinoVO, ArquivoVO arquivoVO,  ConfiguracaoGEDVO configGEDVO, File fileAssinar, String idDocumentacao, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception;

	DocumentoAssinadoVO realizarAssinaturaUploadArquivoInstitucional(ArquivoVO arquivoVO, List<FuncionarioVO> listaConsultaFuncionarioVOs, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception;

	String realizarInclusaoDocumentoAssinadoPorBoletimAcademico(String nomeArquivoOrigem, MatriculaVO matricula, GradeCurricularVO gradeCurricular, TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinado, ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum,String corAssinatura, Float alturaAssinatura, Float larguraAssinatura, FuncionarioVO funcionarioPrincipalVO, String cargoFuncionario1, String tituloFuncionario1, FuncionarioVO funcionarioSecundarioVO, String cargoFuncionario2, String tituloFuncionario2, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception;

	String realizarInclusaoDocumentoAssinadoPorHistoricoAluno(String nomeArquivoOrigem, MatriculaVO matricula, GradeCurricularVO gradeCurricular, TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinado, ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum, String corAssinatura, Float alturaAssinatura, Float larguraAssinatura, FuncionarioVO funcionarioPrincipalVO, String cargoFuncionario1, String tituloFuncionario1, FuncionarioVO funcionarioSecundarioVO, String cargoFuncionario2, String tituloFuncionario2, 
			FuncionarioVO funcionarioTerciarioVO, String cargoFuncionario3, String tituloFuncionario3, ConfiguracaoGeralSistemaVO config, Integer codOrigemRequerimento, UsuarioVO usuarioVO) throws Exception;

	String realizarInclusaoDocumentoAssinadoPorEmissaoCertificado(String nomeArquivoOrigem, MatriculaVO matricula,UnidadeEnsinoVO unidadeEnsinoVO , TurmaVO turma, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinado,ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum, String corAssinatura, Float alturaAssinatura, Float larguraAssinatura, FuncionarioVO funcionarioPrincipalVO, String cargoFuncionario1, String tituloFuncionario1, FuncionarioVO funcionarioSecundarioVO, String cargoFuncionario2, String tituloFuncionario2, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO, Boolean apresentarNomeCustomizado , String nomeCustomizado) throws Exception;
	
	void executarExclusaoTodosDocumentoAssinadoSelecionados(DocumentoAssinadoVO obj, OperacaoDeVinculoEstagioEnum operacaoDeVinculoEstagioEnum, String motivo, UsuarioVO usuario, ConfiguracaoEstagioObrigatorioVO configEstagio) throws Exception; 
	
	public String realizarAssinaturaAgendaProfessor(String nomeArquivoOrigem, AlinhamentoAssinaturaDigitalEnum alinhamentoAssinaturaDigitalEnum,  String corAssinatura, Float alturaAssinatura, Float larguraAssinatura, ConfiguracaoGeralSistemaVO config, FuncionarioVO funcionarioVO, UsuarioVO usuarioVO) throws Exception ;
	
	public ArquivoVO realizarAssinaturaDocumentacaoProfessor(UnidadeEnsinoVO unidadeEnsinoVO, ArquivoVO arquivoVO,  ConfiguracaoGEDVO configGEDVO, File fileAssinar, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception;
	
	public DocumentoAssinadoVO consultarDocumentoAssinadoPorArquivo(Integer codigo, UsuarioVO usuarioLogado);
	
	public void excluirDocumentoAssinadoVinculadoDocumentacaoGED(Integer codigo, UsuarioVO usuario) throws Exception;
	
	List<DocumentoAssinadoVO> consultarDocumentosAssinadoPorSeiSignature(int nivelMontarDados, UsuarioVO usuarioLogado, String tipoDocumentoAssinado, Integer ordemAssinaturaFiltrar) throws Exception;
	
	public DocumentoAssinadoVO executarAtualizacaoDadosAssinaturaPorProvedorCertisign(CertiSignCallBackRSVO obj, Date dataAssinaturaOrRejeicao, DocumentoAssinadoOrigemEnum documentoAssinadoOrigemEnum,UsuarioVO usuarioOperacoesExternas ) throws Exception;
	
	String executarAssinaturaProvedorCertiSign(DocumentoAssinadoVO obj, String arquivoOrigem, ConfiguracaoGEDVO configGEDVO, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception;
	
	public void realizarVisualizacaoArquivoProvedorCertisign(DocumentoAssinadoVO doc, ConfiguracaoGeralSistemaVO configGeral, boolean includeOriginal, boolean includeManifest, boolean zipado, UsuarioVO usuario) throws Exception;
	
	void realizarDownloadArquivoProvedorCertisign(DocumentoAssinadoVO doc, ConfiguracaoGeralSistemaVO configGeral, UsuarioVO usuario) throws Exception;

	public Integer executarAtualizacaoDocumentoAssinadoPorSeiSignature(DocumentoAssinadoVO obj, InputStream uploadedInputStream, ByteArrayOutputStream byteArquivo, Boolean xmlUpadoMapa, String tamanhoArquivoXML, boolean pemitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso, Boolean retornarExcecaoAssinaturaCoordenadorPrimeiro, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO, Boolean assinarPorCNPJ, Integer ordemAssinatura, String provedorAssinatura, Boolean escreverArquivo, Boolean realizarEscritaDebug) throws Exception;

	void adicionarQRCodePDF(DocumentoAssinadoVO documentoAssinadoVO, String arquivoOrigem,
			ConfiguracaoGeralSistemaVO config, ConfiguracaoGEDVO configuracaoGEDVO) throws Exception;

	void adicionarSeloPDF(DocumentoAssinadoVO documentoAssinadoVO, String arquivoOrigem,
			ConfiguracaoGeralSistemaVO config, ConfiguracaoGEDVO configuracaoGEDVO) throws Exception;
	
	void adicionarSeloPDF(TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum, String arquivoOrigem,
			ConfiguracaoGeralSistemaVO config, ConfiguracaoGEDVO configuracaoGEDVO) throws Exception;

	void adicionarQRCodePDF(TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum, String arquivoOrigem,
			String urlQrCode, ConfiguracaoGeralSistemaVO config, ConfiguracaoGEDVO configuracaoGEDVO) throws Exception;

	void preencherAssinadorDigitalDocumentoPdf(String arquivoOrigem, ArquivoVO certificadoDigitalVO,
			String senhaCertificadoDigital, DocumentoAssinadoVO documentoAssinado,
			AlinhamentoAssinaturaDigitalEnum alinhamentoAssinaturaDigital, String corAssinatura, Float alturaAssinatura,
			Float larguraAssinatura, float tamanhoFonte, int coordenadaLLX, int coordenadaLLY, int coordenadaURX,
			int coordenadaURY,  boolean apresentarAssinaturaUltimaPagina, boolean apresentarAssinaturaDigital, ConfiguracaoGeralSistemaVO config, Boolean excluirArquivoOrigem,
			Boolean disponibilizarArquivoAssinadoParaDowload) throws Exception;

	void realizarAssinaturaFuncionarioPreVisualizacao(DocumentoAssinadoVO documentoAssinadoVO,
			ConfiguracaoGEDVO configuracaoGEDVO, ConfiguracaoGedOrigemVO configuracaoGedOrigemVO,
			FuncionarioVO funcionarioVO, CargoVO cargoVO, String titulo, Integer nrFuncionario) throws Exception;

	String realizarVerificacaoProvedorDeAssinatura(DocumentoAssinadoVO obj, Integer unidadeEnsino, boolean isAdicionarSelo, boolean isAdicionarQRCode, String nomeArquivoOrigem, AlinhamentoAssinaturaDigitalEnum alinhamentoAssinaturaDigital, String corAssinatura, Float alturaAssinatura, Float larguraAssinatura,float tamnhoFonte, int coordenadaLLX, int coordenadaLLY, int coordenadaURX, int coordenadaURY, ConfiguracaoGeralSistemaVO config, Boolean excluirArquivoOrigem, UsuarioVO usuarioVO, Boolean disponibilizarArquivoAssinadoParaDowload) throws Exception;
	String executarAssinaturaParaDocumento(DocumentoAssinadoVO obj, ConfiguracaoGEDVO configuracaoGEDVO,
			String arquivoOrigem, String corAssinatura, ConfiguracaoGeralSistemaVO config, Boolean excluirArquivoOrigem,
			UsuarioVO usuarioVO, Boolean disponibilizarArquivoAssinadoParaDowload, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum) throws Exception;

	


	public String excutarVerificacaoPessoasParaAssinarContrato(DocumentoAssinadoVO obj, File fileAssinar, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception;

	public List<DocumentoAssinadoVO> consultarPorMatriculaPeriodo(Integer matriculaPeriodo, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public DocumentoAssinadoVO consultarPorMatriculaPeriodoContratoAssinado(Integer matriculaPeriodo, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<DocumentoAssinadoVO> consultarPorMatriculaAlunoContratoPendenteAssinatura(String matricula, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<Integer> consultarDocumentosPendenteRejeitadoMatricula(String matricula, String tipoPessoa) throws Exception;

	public void realizarBloqueioDocument(DocumentoAssinadoVO doc, ConfiguracaoGEDVO configGedVO, UsuarioVO usuario) throws Exception;
	
	public void realizarEnvioNotificacaoLembreteDocumentoPendenteAssinatura(DocumentoAssinadoVO obj, ConfiguracaoGEDVO configGedVO) throws Exception  ;

	public CertiSignRSVO realizarEnvioParticipantAdd(CertiSignRSVO csRSVO ,DocumentoAssinadoPessoaVO doc, ConfiguracaoGEDVO configGedVO) throws Exception;

	void realizarExclusaoParticipantDiscard(DocumentoAssinadoPessoaVO doc, ConfiguracaoGEDVO configGedVO) throws Exception;

	public void realizarProcessoAlteracaoEmailNotificacaoAssinaturaCertSign(DocumentoAssinadoPessoaVO documentoAssinadoConcedente, EstagioVO estagioConcedenteAlteracaoEmailNotificacaoPendente, String emailEnviarNotificacaoAssinaturaConcedente , UsuarioVO usuarioLogado) throws Exception;

	public CertiSignCallBackSignatureValidateRSVO realizarConsultarDocumentsValidateSignatures(DocumentoAssinadoVO doc,ConfiguracaoGEDVO configGedVO) throws Exception;


	public void executarProcessamentoDocumentosAssinadoEletronicaMenteValidandoSituacaoAssinaturaPorProvedorCertiSign(List<DocumentoAssinadoVO> docs, UsuarioVO usuarioLogado) throws Exception;

	public List<DocumentoAssinadoVO> consultarDocumentosAssinadoPendentePorPeriodo(int nivelMontarDados, UsuarioVO usuarioLogado,	Date periodoInicial, Date periodoFinal) throws ConsistirException, Exception;

	public void realizarProcessamentoJobValidacaoDocumentoAssinadoEnviadosPorProvedorCertiSign(Date periodoInicial, Date periodoFinal) throws Exception;
	
	public String realizarInclusaoDocumentoAssinadoPorAtaColacaoGrau(String nomeArquivoOrigem, ProgramacaoFormaturaVO programacaoFormatura, CursoVO curso, List<ProgramacaoFormaturaAlunoVO> listaProgramacaoFormaturaAlunoVO, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinado, ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum, String corAssinatura, Float alturaAssinatura, Float larguraAssinatura, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO, UnidadeEnsinoVO unidadeEnsinoVO, PessoaVO funcionarioPrincipal, String cargoFuncionario1, String tituloFuncionario1, PessoaVO funcionarioSecundario, String cargoFuncionario2, String tituloFuncionario2) throws Exception;

	List<DocumentoAssinadoVO> consultarPorCodigoAlunoPendenciaAssinaturaContratoColacaoGrau(Integer codigoPessoa, String matricula, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void alterDocumentoAssinadoInvalido(DocumentoAssinadoVO documentoAssinadoVO, UsuarioVO usuarioLogado);

	public List<DocumentoAssinadoVO> consultarPorCodigoProgramacaoCodigoCurso(Integer codigoProgramacaoFormatura, Integer codigoCurso, Boolean filtrarAlunosPendenteColacaoGrau, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public DocumentoAssinadoVO consultarPorCodigoProvedorAssinatura(String codigoprovedordeassinatura, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<DocumentoAssinadoVO> consultarPorCodigoProgramacaoFormatura(Integer codigoProgramacaoFormatura, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	DocumentoAssinadoVO consultarPorMatriculaAlunoUltimoContratoGeradoSemAssinaturaRejeitada(String matricula,TipoOrigemDocumentoAssinadoEnum tipoorigemdocumentoassinado, int nivelMontarDados, UsuarioVO usuario)	throws Exception;

	List<DocumentoAssinadoVO> verificarGeracaoDocumentoAssinado(Integer codigoPessoa, MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UnidadeEnsinoVO unidadeEnsinoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void realizarAssinaturaRejeicaoDocumentoAppAluno(DocumentoAssinadoVO documentoAssinadoVO, UsuarioVO usuarioVO,
			String latitude, String longitude, boolean assinar) throws Exception;

	void realizarVerificacaoDocumentoAssinadoPendenteUsuarioLogado(UsuarioVO usuarioVO, MatriculaVO matriculaVO,
			MatriculaPeriodoVO matriculaPeriodoVO, Boolean verificarAtaColacao) throws Exception;

	Map<String, DocumentoAssinadoPessoaVO> realizarGeracaoPreviewDocumentoContratoAtaColacao(
			DocumentoAssinadoVO documentoAssinado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema,
			UsuarioVO usuarioVO) throws Exception;

	Boolean verificarAlunoAssinouContratoMatriculaParaAtivacaoMatricula(String matriculaAluno) throws Exception;
	
	public String realizarInclusaoDocumentoAssinadoPorPlanoDeEnsino(String nomeArquivoOrigem, PlanoEnsinoVO planoEnsinoVO, String ano, String semestre, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception;
	
	public void consultarDocumentoAssinadoPlanoEnsino(DataModelo dataModelo, PlanoEnsinoVO planoEnsinoVO,  DisciplinaVO disciplinaVO, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinado,	SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum, GradeCurricularVO gradeCurricularVO,  UsuarioVO usuarioVO, Integer limit, Integer offset) throws Exception;
	
	void realizarExclusaoDocument(DocumentoAssinadoVO doc, ConfiguracaoGEDVO configGedVO) throws Exception;

	public String realizarGeracaoPreviewRepresentacaoVisual(DocumentoAssinadoVO documentoAssinado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public void verificarExpedicaoDiplomaContemDocumentoAssinado(ExpedicaoDiplomaVO expedicaoDiplomaVO) throws Exception;

	public void realizarExclusaoDocumentoAssinadoVinculadoExpedicaoDiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public void adicionarMarcaDaguaPDF(String arquivoOrigem, ConfiguracaoGeralSistemaVO config, String caminhoWeb, DocumentoAssinadoVO documentoAssinadoVO) throws Exception;

	public void realizarAssinaturaDiplomaDigital(ExpedicaoDiplomaVO expedicaoDiplomaVO, File arquivoXML, File arquivoVisual, ConfiguracaoGeralSistemaVO config, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum, UsuarioVO usuarioVO, Boolean persistirDocumentoAssinado, HistoricoAlunoRelVO histAlunoRelVO) throws Exception;

	public String realizarGeracaoPreviewDocumento(DocumentoAssinadoVO documentoAssinado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

	public Map<List<String>, List<String>> realizarImportacaoXmlDiplomaRegistradoraLote(ZipFile arquivoZizado, ProgressBarVO progressBarVO) throws Exception;

	public void realizarLeituraDiplomaDigital(DocumentoAssinadoVO documentoAssinadoVO, ConfiguracaoGeralSistemaVO config, InputStream inputStream, UsuarioVO usuarioVO) throws Exception;

	public void alterarArquivoVisualDocumentoAssinado(DocumentoAssinadoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	public void realizarUploadArquivoAmazon(ArquivoVO arquivo, ConfiguracaoGeralSistemaVO config, Boolean deletarAqruivoExistente) throws Exception;

	public List<DocumentoAssinadoVO> consultarDocumentosDigitaisDiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO, List<TipoOrigemDocumentoAssinadoEnum> tipoOrigemDocumentoAssinadoEnums, Boolean documentoAssinadoValido) throws Exception;

	public List<DocumentoAssinadoVO> consultarDocumentosAssinadoPorRelatorio(DocumentoAssinadoVO obj, boolean apresentarApenasDocumentoValido, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado, List<TipoOrigemDocumentoAssinadoEnum> tipoOrigemDocumentoAssinadoEnums, Integer limit) throws Exception;

	public void realizarAssinaturaCurriculoEscolar(GestaoXmlGradeCurricularVO gestaoXmlGradeCurricularVO, File arquivoXML, File arquivoVisual, ConfiguracaoGeralSistemaVO config, TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum, UsuarioVO usuarioVO, Boolean persistirDocumentoAssinado) throws Exception;

	public void alterarExpedicaoDiplomaDocumentoAssinado(Integer documentoAssinado, Integer expedicaoDiploma);

	public void rejeitarDocumentoAssinadoProvedorAssinatura(DocumentoAssinadoVO documentoAssinadoExcluir, String motivoRejeicaoDocumentoAssinadoProvedorAssinatura, UsuarioVO usuarioLogado) throws Exception;

	public File realizarAssinaturaDocumentacaoAlunoV2(UnidadeEnsinoVO unidadeEnsino, ArquivoVO arquivoVO, ConfiguracaoGEDVO configGEDVO, File fileAssinar, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO, Boolean realizandoCorrecaoPDFA, Boolean realizarConversaoPDFPDFAImagem, Boolean persistirArquivo, String idDocumentacao) throws Exception;

	public ArquivoVO validarCertificadoParaDocumento(UnidadeEnsinoVO unidadeEnsinoVO, ConfiguracaoGEDVO configuracaoGEDVO, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO, PessoaVO pessoaVO) throws Exception;

	public File preencherAssinadorDigitalDocumentoPdfParaDocumentoMatriculaV2(String arquivoOrigem, ArquivoVO certificadoDigitalVO, String senhaCertificadoDigital, String caminhoPastaBase, String nomeArquivo, AlinhamentoAssinaturaDigitalEnum alinhamentoAssinaturaDigital, String corAssinatura, Float alturaAssinatura, Float larguraAssinatura, float tamanhoFonte, int coordenadaLLX, int coordenadaLLY, int coordenadaURX, int coordenadaURY, ConfiguracaoGeralSistemaVO config, ConfiguracaoGEDVO configGed, String urlValidacao, Date dataAssinatura, Boolean pastaBaseArquivoFixo, String idDocumentacao) throws Exception;

	public void executarProcessamentoDocumentosAssinadoEletronicaMenteValidandoSituacaoAssinaturaPorProvedorCertiSign(DocumentoAssinadoVO doc, UsuarioVO usuarioLogado) throws Exception;

	public void atualizarSituacaoDocumentoAssinadoErro(DocumentoAssinadoVO documentoAssinado);

	public void realizarDownloadArquivoProvedorTechCert(DocumentoAssinadoVO doc, ConfiguracaoGeralSistemaVO configGeral, UsuarioVO usuario) throws Exception;

	void executarProcessamentoDocumentosAssinadoEletronicaMenteValidandoSituacaoAssinaturaPorProvedorTechCert(DocumentoAssinadoVO doc, UsuarioVO usuarioLogado) throws Exception;

	void executarProcessamentoDocumentosAssinadoEletronicaMenteValidandoSituacaoAssinaturaPorProvedorTechCert(List<DocumentoAssinadoVO> docs, UsuarioVO usuarioLogado) throws Exception;

	DocumentKeyValidationTechCertVO realizarConsultarDocumentsValidateSignaturesTechcert(DocumentoAssinadoVO doc, ConfiguracaoGEDVO configGedVO, DocumentsTechCertVO documentsTechCertVO);

	DocumentoAssinadoVO consultarPorChaveProvedordeAssinatura(String chaveProvedorDeAssinatura, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void realizarVisualizacaoArquivoProvedorTechCert(DocumentoAssinadoVO doc, ConfiguracaoGeralSistemaVO configGeral, UsuarioVO usuario) throws Exception;

	void realizarProcessamentoJobValidacaoDocumentoAssinadoEnviadosPorProvedorTechCert(Date periodoInicial,	Date periodoFinal) throws ConsistirException;

	void realizarProcessoAlteracaoEmailNotificacaoAssinaturaTechCert(DocumentoAssinadoPessoaVO doc, EstagioVO estagioConcedenteAlteracaoEmailNotificacaoPendente , String emailEnviarNotificacaoAssinaturaConcedente , UsuarioVO usuarioLogado) throws Exception;

	Boolean isHabilitadoTipoDocumentoTechCert(TipoOrigemDocumentoAssinadoEnum tipoDocumentoAtual);

	void integrarDocumentoTechCert(IntegracaoTechCertVO integracaoTechCertVO, String arquivoOrigem, DocumentoAssinadoVO documentoAssinadoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoGEDVO configuracaoGEDVO, UsuarioVO usuarioLogado);

	void realizarBloqueioDocumentTechCert(DocumentoAssinadoVO doc, ConfiguracaoGEDVO configGedVO, UsuarioVO usuario) throws Exception;

	void executarProcessamentoDocumentosAssinadoEletronicaMenteValidandoSituacaoAssinaturaPorProvedorTechCertApi(DocumentoAssinadoVO doc, UsuarioVO usuarioLogado) throws Exception;

	List<DocumentoAssinadoVO> consultarDocumentosAssinadoPendentePorPeriodoTechCert(int nivelMontarDados, UsuarioVO usuarioLogado , Date periodoInicial , Date periodoFinal) throws Exception;

	void realizarAlteracaoParticipanteTechCert(DocumentoAssinadoPessoaVO pessoaDelete, String chaveProvedorAssinatura, AddedFlowActionTechCertVO addedFlowActionTechCertVO, ConfiguracaoGEDVO configuracaoGEDVO) throws Exception;

	void realizarExclusaoParticipantDiscardTechCert(DocumentoAssinadoPessoaVO doc, ConfiguracaoGEDVO configGedVO, DocumentoAssinadoVO documentoAssinadoVO) throws Exception;

	AddedFlowActionTechCertVO permissoesDocumentsFlowsTechCert(AddedFlowActionTechCertVO addedFlowActionTechCertVO);

	void validarCpfEmailParaAssinatura(String email, String cpf, String nome) throws Exception;

	DocumentsTechCertVO consultarDocumet(ConfiguracaoGEDVO configuracaoGEDVO, String documentId);

	void executarProcessamentoTechCertAntigoPosTrocaAssinante(String chaveProvedorAssinatura, UsuarioVO usuarioLogado, ConfiguracaoGEDVO configGedVO, DocumentoAssinadoOrigemEnum documentoAssinadoOrigemEnum) throws Exception;

	void realizarAlteracaoStepFlowActionTechCert(ConfiguracaoGEDVO configGedVO, String chaveProvedorDeAssinatura, Map<Integer, String> flowActionEdited) throws Exception;
	
	public List<DocumentoAssinadoVO> consultarAtaColacaoGrauDataLimiteAssinaturaVencido();
}
