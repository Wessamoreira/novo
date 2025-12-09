package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;

import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.utilitarias.ProgressBarVO;

/**
 *
 * @author Carlos
 */
public interface ImpressaoContratoInterfaceFacade {
	public void consultarAlunoPorMatricula(ImpressaoContratoVO impressaoContratoVO, Integer unidadeEnsino, String ano, String semestre, UsuarioVO usuarioLogado) throws Exception;

	public List<ImpressaoContratoVO> consultarDadosGeracaoContrato(MatriculaVO matricula, String campoFiltroPor, TurmaVO turma, String semestre, String ano, Integer unidadeEnsino, Optional<Date> dataInicio, Optional<Date> dataFim, UsuarioVO usuarioLogado, CursoVO cursoVO, String tipoAluno,String situacaoContratoDigital ) throws Exception;

	public void validarDadosMatricula(String matricula, String matriculaDigitada) throws Exception;

	public void validarDadosMatriculaPeriodo(MatriculaPeriodoVO matriculaPeriodoVO) throws Exception;

	public List<MatriculaVO> consultarAluno(String valorConsultaAluno, String campoConsultaAluno, Integer unidadeEnsino, UsuarioVO usuarioLogado) throws Exception;

	public String imprimirContrato(String tipoContrato, ImpressaoContratoVO impressaoContratoVO,  ConfiguracaoGeralSistemaVO config,  UsuarioVO usuarioLogado) throws Exception;

	public String imprimirContratoInclusaoReposicao(ImpressaoContratoVO impressaoContratoVO,  TextoPadraoVO textoPadraoContratoInclusao, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO config) throws Exception;

	String montarDadosContratoTextoPadrao(MatriculaVO matriculaVO, ImpressaoContratoVO impressaoContratoVO, TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

	String imprimirContratoVisaoAluno(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodoVO,  ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioLogado) throws Exception;

	String imprimirContratoRenovarMatricula(String tipoContrato, MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodoVO,  ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioLogado) throws Exception;
	
	String executarValidacaoTipoImpressaoContrato(TextoPadraoVO texto, String contratoPronto, ImpressaoContratoVO impressaoContratoVO, ConfiguracaoGeralSistemaVO config,Boolean regerarDocumentoAssinado, UsuarioVO usuarioLogado) throws Exception;
	
	public List<HistoricoVO> realizarObtencaoDatasAulaAluno(List<HistoricoVO> historicoVOs, ImpressaoContratoVO impressaoContratoVO, UsuarioVO usuario, Boolean considerarSituacaoCursando) throws Exception;
	
	public String realizarCriacaoLegendaSituacaoDisciplinaHistorico(ImpressaoContratoVO impressaoContratoVO, MatriculaVO matriculaVO);
	
	public void realizarNotificacaoPendenciaAssinaturaContrato(DocumentoAssinadoPessoaVO documentoAssinadoPessoa , PersonalizacaoMensagemAutomaticaVO msg, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean enviarPorEmail) throws Exception;
	
}
