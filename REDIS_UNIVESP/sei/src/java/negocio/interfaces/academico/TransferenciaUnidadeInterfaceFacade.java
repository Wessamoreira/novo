package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.MotivoCancelamentoTrancamentoVO;
import negocio.comuns.academico.TransferenciaUnidadeVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface TransferenciaUnidadeInterfaceFacade {

	public TransferenciaUnidadeVO persistir(MatriculaPeriodoVO matriculaPeriodoOrigem, MatriculaPeriodoVO matriculaPeriodoDestino, MatriculaVO matriculaVoNova, Boolean transferirDescontos, List listaSelectItemReconhecimentoCurso, ConfiguracaoFinanceiroVO configuracaoFinanceiroPadraoSistema, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, MotivoCancelamentoTrancamentoVO motivoCancelamentoTrancamentoVO, UsuarioVO usuario) throws Exception;

	public void gerarDataMatriculaDestino(MatriculaVO matriculaVoNova, MatriculaPeriodoVO matriculaPeriodoVoOrigem, MatriculaPeriodoVO matriculaPeriodoVoDestino) throws Exception;

	public List<TransferenciaUnidadeVO> consultaRapidaPorMatriculaOrigem(String matriculaOrigem, Integer unidadeEnsinoOrigem, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<TransferenciaUnidadeVO> consultaRapidaPorMatriculaDestino(String matriculaDestino, Integer unidadeEnsinoDestino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<TransferenciaUnidadeVO> consultaRapidaPorNomeAluno(String nomeAluno, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<TransferenciaUnidadeVO> consultaRapidaPorData(Date prmIni, Date prmFim, Integer unidadeEnsinoOrigem, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<TransferenciaUnidadeVO> consultaRapidaPorNomeCurso(String nomeCurso, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	Boolean imprimirDeclaracaoTransferenciaUnidade(Integer transferenciaUnidade, Integer textoPadraoDeclaracao, UsuarioVO usuario) throws Exception;

	Boolean imprimirContratoTransferenciaUnidade(Integer transferenciaUnidade, Integer codigoTexto, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuarioLogado) throws Exception;

	/**
	 * @author Rodrigo Wind - 25/01/2016
	 * @param matriculaorigem
	 * @return
	 */
	Boolean verificarExistenciaTransferenciaInterna(String matriculaorigem);
	
	public void gerarDataMatriculaDestinoDeAcordoProcessoMatricula(MatriculaVO matriculaVoNova, MatriculaPeriodoVO matriculaPeriodoVoOrigem, MatriculaPeriodoVO matriculaPeriodoVoDestino) throws Exception;
}
