package negocio.interfaces.secretaria;

import java.util.List;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.ImpressaoDeclaracaoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoDoTextoImpressaoContratoEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.TextoPadraoLayoutVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;

/**
 *
 * @author Carlos
 */
public interface ImpressaoDeclaracaoInterfaceFacade {
	public void consultarAlunoPorMatricula(ImpressaoContratoVO impressaoContratoVO, Integer unidadeEnsino, UsuarioVO usuarioLogado) throws Exception;

	public void validarDadosMatricula(String matricula, String matriculaDigitada) throws Exception;

	public void validarDadosMatriculaPeriodo(MatriculaPeriodoVO matriculaPeriodoVO) throws Exception;

	public List consultarAluno(String valorConsultaAluno, String campoConsultaAluno, Integer unidadeEnsino, UsuarioVO usuarioLogado) throws Exception;

	public String imprimirDeclaracao(Integer textoPadraoDeclaracao, ImpressaoContratoVO impressaoContratoVO, ImpressaoContratoVO impressaoContratoGravarVO, String tipoDeclaracao, TurmaVO turmaVO, DisciplinaVO disciplinaVO, ConfiguracaoGeralSistemaVO config, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioLogado, boolean persistirDocumentoAssinado) throws Exception;

	public ImpressaoContratoVO consultarPorMatriculaTextoPadrao(String matricula, Integer textoPadrao, int codigoProfessor) throws Exception;

	public List<ImpressaoContratoVO> consultarAlunosTurmaVOs(Integer turma, UsuarioVO usuarioVO);

	public void realizarGarantiaMarcacaoUnicoCheck(List<ImpressaoContratoVO> listaAlunosVOs, ImpressaoContratoVO obj);

	public List<ImpressaoDeclaracaoVO> consultarImpressaoDeclaracaoPorTipoDeclaracao(String matricula, Integer professor, Integer textoPadrao, String tipoDeclaracao, UsuarioVO usuarioVO) throws Exception;

	public void alterarImpressaoDeclaracaoSituacaoEntregue(List<ImpressaoDeclaracaoVO> listaImpressaoDeclaracaoVOs, String tipoDeclaracao, UsuarioVO usuarioVO) throws Exception;

	public Boolean imprimirDeclaracaoProcSeletivo(Integer textoPadraoDeclaracao, ImpressaoContratoVO impressaoContratoVO, ImpressaoContratoVO impressaoContratoGravarVO, String tipoDeclaracao, TurmaVO turmaVO, DisciplinaVO disciplinaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioLogado) throws Exception;	

	public String executarConversaoHtmlParaPdf(String htmlRelatorio, TextoPadraoLayoutVO textoPadrao, UsuarioVO usuarioVO) throws Exception;

	String executarConversaoHtmlParaPdfComunicadoInterno(ComunicacaoInternaVO obj, Boolean imprimirPDF, UsuarioVO usuarioVO) throws Exception;

	//String executarAssinaturaParaImpressaoContrato(String nomeArquivoOrigem, TextoPadraoLayoutVO textoPadraoLayout, ImpressaoContratoVO impressaoContratoVO, boolean persistirDocumentoAssinado, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception;

	void gravarImpressaoContrato(ImpressaoContratoVO obj) throws Exception;

	String executarValidacaoImpressaoEmPdf(ImpressaoContratoVO obj, TextoPadraoLayoutVO texto, String textoStr, boolean persistirDocumentoAssinado, ConfiguracaoGeralSistemaVO configGeralSistema,  UsuarioVO usuarioLogado) throws Exception;
	public List<ImpressaoDeclaracaoVO> consultarPorMatriculaPorTextoPadrao(String matricula, Integer textoPadrao, TipoDoTextoImpressaoContratoEnum tipoTextoImpressao,  UsuarioVO usuarioVO) throws Exception;

	void validarExclusaoImpressaoDeclaracaoPorRequerimento(Integer requerimento, UsuarioVO usuario,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
	
	public void validarDadosPermissaoImpressaoContrato(TextoPadraoDeclaracaoVO textoPadraoImprimirVO, UsuarioVO usuario) throws Exception;
	
	void carregarDadosDataInicioFimAula(ImpressaoContratoVO impressaoContratoVO, UsuarioVO usuarioLogado, HistoricoVO hist);
	
	public void excluirImpressaoDeclaracaoPorMatricula(MatriculaVO matriculaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;
	
	public String criarCaminhoPastaAteDiretorioSubRelatorio(TextoPadraoLayoutVO textoLayout, ConfiguracaoGeralSistemaVO config);
	
	void carregarTotalSemestresCurso(GradeCurricularVO gradeCurricularVO, String texto, UsuarioVO usuarioVO) throws Exception;

	String gerarDeclaracaoLGPD(UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuarioVO) throws Exception;

}
