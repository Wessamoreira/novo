package negocio.interfaces.faturamento.nfe;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import controle.arquitetura.AplicacaoControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.ConfiguracaoNotaFiscalVO;
import negocio.comuns.faturamento.nfe.NfeVO;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaServicoVO;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.faturamento.nfe.AmbienteNfeEnum;
import negocio.comuns.utilitarias.faturamento.nfe.TipoCertificadoEnum;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;


/**
 * Interface responsável por criar uma estrutura padrão de comunidaï¿½ï¿½o entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.		
 * Alï¿½m de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermï¿½dio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface NotaFiscalSaidaInterfaceFacade {

	NotaFiscalSaidaVO novo(UsuarioVO usuarioLogado) throws Exception;

	void incluir(NotaFiscalSaidaVO obj, UsuarioVO usuarioLogado) throws Exception;

	public List consultar(String campoConsulta, String valorConsulta, Date dataInicio, Date dataTermino, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, int limite, int pagina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado, String tipoPessoa) throws Exception;
	void alterar(NotaFiscalSaidaVO obj, UsuarioVO usuarioLogado) throws Exception;

	void gravarSituacaoInutilizado(Integer codigo, String situacao, String protocolo, String motivo, UsuarioVO usuarioLogado) throws Exception;

	void gravarSituacaoCancelamento(Integer codigo, String situacao, String protocolo, String xmlCancelamento, String motivo, UsuarioVO usuarioLogado) throws Exception;

	void gravarXmlCancelamento(Integer codigo, String xmlCancelamento, UsuarioVO usuarioLogado) throws Exception;
	
	void gravarXmlEnvio(Integer codigo, String xmlEnvio, UsuarioVO usuarioLogado) throws Exception;

	void gravarRecibo(Integer codigo, String recibo, UsuarioVO usuarioLogado) throws Exception;

	String getTipoIcms(Double aliquotaICMS, Double baseICMS, Double precoItem, Double aliquotaSubTrib, Double baseSubTrib, Double totalSubTrib, Boolean baseReduzida, Double valorIPI);

	List<File> getArquivosEnvioPorData(Date date) throws Exception;

	void gravarSituacaoNotaComBaseNoStatus(NotaFiscalSaidaVO notaFiscalSaidaVO, String status, String xmlRetorno, UsuarioVO usuarioLogado) throws Exception;

	String consultarStatusServicoNFe(UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuarioLogado) throws Exception;
	
	String consultarStatusServicoNFeWebservice(UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuarioLogado) throws Exception;

	void executarValidacaoPossibilidadeInutilizarNota(NotaFiscalSaidaVO notaFiscalSaidaVO) throws Exception;

	void excluir(NotaFiscalSaidaVO obj, UsuarioVO usuarioLogado) throws Exception;

	void excluirSemCommit(NotaFiscalSaidaVO obj, UsuarioVO usuarioLogado) throws Exception;

	Long consultarNumeroNotaFiscal(Integer codigo) throws Exception;

	List<String> consultarXMLEnvioPorData(Date data) throws Exception;

	List<String> consultarXMLCancelamentoPorData(Date data) throws Exception;

	List<NotaFiscalSaidaVO> consultarNotaFiscalSaidaEnviadasPorData(Date data, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<NotaFiscalSaidaVO> consultarNotaFiscalSaidaCanceladasPorData(Date data, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List consultarPorNotaOrigem(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	public List consultarPorSituacao(String valorConsulta, Date prmIni, Date prmFim, String situacao, int limite, int pagina, boolean controleAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
	
	List consultarPorLote(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	public List consultarPorNumero(Long valorConsulta, Date prmIni, Date prmFim, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, int limite, int pagina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
	
	NotaFiscalSaidaVO consultarPorNumeroUnico(Long valorConsulta, String transferencia, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	NotaFiscalSaidaVO consultarClienteNotaFiscal(Long valorConsulta, String identificador, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	public List consultarPorNomeCliente(String valorConsulta, Date prmIni, Date prmFim, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, int limite, int pagina, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
	
	public List consultarPorDataEmissao(Date prmIni, Date prmFim, String situacao, int limite, int pagina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;	
	
	public List consultarPorCodigo(Integer valorConsulta, Date prmIni, Date prmFim, String situacao, int limite, int pagina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List consultarPorDataEmissaoCliente(Date prmIni, Date prmFim, Integer cliente, int limite, int pagina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	Integer obterTotalRegistroConsultarDataEmissaoCliente(Integer cliente, Date prmIni, Date prmFim) throws Exception;

	NotaFiscalSaidaVO consultarPorNumeroUnicoCliente(Long valorConsulta, Integer cliente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	Integer obterTotalRegistroConsultarPorNumeroUnicoCliente(Long valorConsulta, Integer cliente) throws Exception;

	NotaFiscalSaidaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	NotaFiscalSaidaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void setIdEntidade(String idEntidade);

	Integer consultarTotalRegistros(String campoConsulta, String valorConsulta, Integer codigoEmpresa, String transferencia, Date dataInicio, Date dataTermino, String identificador, UsuarioVO usuarioLogado) throws Exception;

	Integer obterTotalRegistroConsultarPorCodigo(Integer valorConsulta, Integer codigoEmpresa, Date prmIni, Date prmFim) throws Exception;

	Integer obterTotalRegistroConsultarPorDataEmissao(Integer codigoEmpresa, String transferencia, Date prmIni, Date prmFim) throws Exception;

	Integer obterTotalRegistroConsultarPorNomeCliente(String valorConsulta, Integer codigoEmpresa, String transferencia, Date prmIni, Date prmFim, UsuarioVO usuarioLogado) throws Exception;

	Integer obterTotalRegistroConsultarPorSituacao(String valorConsulta, Integer codigoEmpresa, String transferencia, Date prmIni, Date prmFim) throws Exception;

	Integer obterTotalRegistroConsultarPorNumero(Long valorConsulta, String transferencia, Integer codigoEmpresa, Date prmIni, Date prmFim) throws Exception;

	Integer obterTotalRegistroConsultarPorModelo(String valorConsulta, Date prmIni, Date prmFim, Integer codigoEmpresa) throws Exception;

	void executarValidacaoTipoCertificado(TipoCertificadoEnum tipoCertificadoEnum) throws Exception;

	String consultarLoteEnviar(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO conGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception;
	
	String consultarLoteEnviarWebservice(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO conGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception;

	String consultarNota(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO conGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception;

	void cancelarNFeWebservice(List<NotaFiscalSaidaVO> notaSaidaVOs, List<NotaFiscalSaidaVO> notaFiscalSaidaErroVOs, ConfiguracaoGeralSistemaVO conSistemaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, UsuarioVO usuarioLogado) throws Exception;

	String inutilizarNota(NotaFiscalSaidaVO notaFiscalSaidaVO, String caminhoNFe, AmbienteNfeEnum ambienteNfeEnum, TipoCertificadoEnum tipoCertificadoEnum, String caminhoCertificado, UsuarioVO usuarioLogado) throws Exception;

	String corrigirNotaFiscal(NotaFiscalSaidaVO notaFiscalSaidaVO, String caminhoNFe, AmbienteNfeEnum ambienteNfeEnum, TipoCertificadoEnum tipoCertificadoEnum, String caminhoCertificado, UsuarioVO usuarioLogado) throws Exception;

	void preencherTodosListaContasRecebidas(List<NotaFiscalSaidaVO> notaFiscalSaidaVOs);

	void desmarcarTodosListaContasRecebidas(List<NotaFiscalSaidaVO> notaFiscalSaidaVOs);

	public String getDesignIReportRelatorio();

	public String getCaminhoBaseRelatorio();
	
	public List consultarPorMatricula(String valorConsulta, Date prmIni, Date prmFim, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, int limite, int pagina, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
	
	public File[] executarCompactarNotasEnviadas(List<NotaFiscalSaidaVO> notaFiscalSaidaVOs, ConfiguracaoGeralSistemaVO conSistemaVO, UsuarioVO usuarioVO, ProgressBarVO ProgressBarNotasCompactadas) throws Exception;

	String consultarXmlEnvioNotaFiscal(Integer numeroNota) throws Exception;
	
	String consultarXmlCancelamentoNotaFiscal(Integer numeroNota) throws Exception;

	void realizarEnvioEmailNotaFiscal(NotaFiscalSaidaVO notaSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRepositorioArquivo, ConfiguracaoGeralSistemaVO configuracaoEmail, Boolean notificarAlunoNotaFiscalGerada , UsuarioVO usuarioLogado) throws Exception;
	

	Integer consultarTotalRegistroPorNomeCliente(String valorConsulta, Date prmIni, Date prmFim, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	Integer consultarTotalRegistroPorMatricula(String valorConsulta, Date prmIni, Date prmFim, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	HashMap<String, Double> consultarTotalNotaTotalISSQNPorNomeCliente(String valorConsulta, Date prmIni, Date prmFim, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	HashMap<String, Double> consultarTotalNotaTotalISSQNPorMatricula(String valorConsulta, Date prmIni, Date prmFim, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	/**
	 * Resposável por executar a consulta das notas fiscais de saídas para geração do arquivo zip.
	 * 
	 * @author Wellington - 10 de dez de 2015
	 * @param campoConsulta
	 * @param valorConsulta
	 * @param dataInicio
	 * @param dataTermino
	 * @param situacao
	 * @param unidadeEnsinoVO
	 * @param cursoVO
	 * @param turmaVO
	 * @param limite
	 * @param pagina
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuarioLogado
	 * @return
	 * @throws Exception
	 */
	List<NotaFiscalSaidaVO> consultarDadosParaCompactacaoNotasEnviadas(String campoConsulta, String valorConsulta, Date dataInicio, Date dataTermino, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, int limite, int pagina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void consultarNFSE(AplicacaoControle aplicacaoControle, NotaFiscalSaidaVO notaFiscalSaidaVO, boolean persistirObservacaoContribuinte, String observacaoContribuinte, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, ProgressBarVO progressBar, String autorizado, UsuarioVO usuarioVO) throws Exception;

	void gravarNumeroNotaFiscal(NotaFiscalSaidaVO notaFiscal, long numeroNota) throws Exception;

	void gravarSituacaoEnvio(NotaFiscalSaidaVO notaFiscal, String situacao, String protocolo, Date dataSituacao, String identificadorReceita, ConfiguracaoNotaFiscalVO configuracaoNotaFiscalVO, UsuarioVO usuarioLogado) throws Exception;

	void alterarMensagemRetorno(NotaFiscalSaidaVO obj, UsuarioVO usuarioLogado) throws Exception;

	void montarDadosGeracaoNotaFiscalSaida(List<NotaFiscalSaidaVO> notaFiscalSaidaVOs, List<ContaReceberRecebimentoVO> contaReceberRecebimentoVOs, List<ContaReceberVO> contasReceberVOs ,Integer unidadeEnsino, List<NotaFiscalSaidaVO> notaFiscalSaidaEnviadasVOs, UsuarioVO usuarioVO) throws Exception;

	void gravarLinkAcesso(NotaFiscalSaidaVO notaFiscal, UsuarioVO usuarioLogado) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 2 de set de 2016 
	 * @param nfeVO
	 * @param nota
	 * @param conNotaFiscalVO
	 * @param usuarioLogado
	 * @throws Exception 
	 */
	void inicializarDadosObjetoEnvio(NfeVO nfeVO, NotaFiscalSaidaVO nota, ConfiguracaoNotaFiscalVO conNotaFiscalVO,ConfiguracaoGeralSistemaVO configuracaoRespositorioArquivo, UsuarioVO usuarioLogado) throws Exception;
	
	void calcularImpostos(NotaFiscalSaidaVO nota, ConfiguracaoNotaFiscalVO configuracaoNotaFiscalVO);
	
	List<ContaReceberVO> consultarContasReceberNotaFiscalSaida(MatriculaVO matriculaVO, Date dataInicio, Date dataFim, CursoVO cursoVO, TurmaVO turmaVO, String tipoValorConsultaContasAReceber,
			FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, String situacaoContaReceber,
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer unidadeEnsino, UsuarioVO usuarioVO )throws Exception;

	void cancelarNFSE(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO conSistemaVO, UsuarioVO usuarioLogado) throws Exception;

	void gravarLogContasVinculadas(NotaFiscalSaidaVO notaFiscalSaidaVO, UsuarioVO usuarioLogado) throws Exception;
	
	String consultarXmlProcNFENotaFiscal(Integer numero) throws Exception;
	
	List<ContaReceberVO> consultarContasRecebidasValorCheioNotaFiscal(MatriculaVO matriculaVO, Date dataInicio, Date dataFim, CursoVO cursoVO, TurmaVO turmaVO, String tipoValorConsultaContasAReceber, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, String situacaoContaReceber, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;
	
	public List consultarPorTipoPessoa(String valorConsulta, Date prmIni, Date prmFim, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, int limite, int pagina, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado, String tipoPessoa) throws Exception;
	
	public Integer consultarTotalRegistroPorTipoPessoa(String valorConsulta, Date prmIni, Date prmFim, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado, String tipoPessoa) throws Exception;
	
	public HashMap<String, Double> consultarTotalNotaTotalISSQNPorTipoPessoa(String valorConsulta, Date prmIni, Date prmFim, String situacao, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, TurmaVO turmaVO, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado, String tipoPessoa) throws Exception;
	
	void gravarEnvioNFSeWebservice(NotaFiscalSaidaVO notaFiscal, UsuarioVO usuarioLogado) throws Exception;
	
	String concatenarDatasCompetenciasContaReceberNotaFiscal(List<NotaFiscalSaidaServicoVO> notaFiscalSaidaServicoVOs) throws Exception;

	void gravarReciboJsonEnvioNotaFiscalSaida(String recibo ,Integer codigo, String jsonEnvio, String jsonRetornoEnvio, UsuarioVO usuarioLogado) throws Exception;

	void persistirNotaFiscalRegistrandoNumeroNotaNumeroRPS(NotaFiscalSaidaVO notaSaidaVO, ProgressBarVO progressBar,UsuarioVO usuarioLogado) throws Exception;

	void realizarInclusaoNotaFiscalSaidaEnviandoNFeWebservice(NotaFiscalSaidaVO notaSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo,
			ConfiguracaoGeralSistemaVO conGeralSistemaVO, ConfiguracaoGeralSistemaVO configuracaoEmail,
			ProgressBarVO progressBar, Integer unidadeEnsino, UsuarioVO usuarioLogado) throws Exception;

	void enviarNFE(NfeVO nfeVO, NotaFiscalSaidaVO notaSaidaVO, ConfiguracaoGeralSistemaVO conGeralSistemaVO,
			ProgressBarVO progressBar, UsuarioVO usuarioLogado) throws Exception;

	void realizarEnvioNotaFiscalEletronica(NotaFiscalSaidaVO notaSaidaVO,ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, ConfiguracaoGeralSistemaVO conGeralSistemaVO,
			ConfiguracaoGeralSistemaVO configuracaoEmail, ProgressBarVO progressBar, Integer unidadeEnsino,	UsuarioVO usuarioLogado) throws Exception;

	void realizarEscritaTextoDebugNFE_LOG(String mensagem);

	

	void realizarConsultaReciboNotaFiscalNFEWebService(NfeVO nfeVO, NotaFiscalSaidaVO notaSaidaVO, String urlWebserviceNFe, UsuarioVO usuarioLogado);

	void realizarEnvioNFEWebService(NfeVO nfeVO, NotaFiscalSaidaVO notaSaidaVO, String urlWebserviceNFe, ProgressBarVO progressBar,
			UsuarioVO usuarioLogado) throws Exception;

	String enviarNFeWebservice(AplicacaoControle aplicacaoControle, NotaFiscalSaidaVO notaSaidaVO,
			boolean persistirObservacaoContribuinte, String observacaoContribuinte,
			ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, ConfiguracaoGeralSistemaVO configuracaoEmail,
			ProgressBarVO progressBar, Integer unidadeEnsino, UsuarioVO usuarioLogado) throws Exception;

	void consultarPorReciboEnvioNFE(NotaFiscalSaidaVO notaSaidaVO, ConfiguracaoGeralSistemaVO conGeralSistemaVO, ConfiguracaoGeralSistemaVO configuracaoRespositorioArquivo, UsuarioVO usuarioLogado) throws Exception;

	void gravarNumeroNotaFiscalViaJobComTransacaoIndependente(NotaFiscalSaidaVO notaFiscal,	ConfiguracaoNotaFiscalVO configuracao, long numeroNota, UsuarioVO usuarioVO) throws Exception;

	
	
}
