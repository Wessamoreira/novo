package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MapaRegistroEvasaoCursoMatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TrancamentoVO;
import negocio.comuns.academico.enumeradores.OrigemFechamentoMatriculaPeriodoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;

import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.secretaria.MapaRegistroEvasaoCursoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProgressBarVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em especial com a classe
 * Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais. Além de
 * padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio de sua classe Façade (responsável por persistir
 * os dados das classes VO).
 */
public interface TrancamentoInterfaceFacade {

	public TrancamentoVO novo() throws Exception;

	public void incluir(TrancamentoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,  UsuarioVO usuarioVO, Boolean realizandoTrancamentoAutomatico) throws Exception;

	public void alterar(TrancamentoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

	public void excluir(TrancamentoVO obj, UsuarioVO usuarioVO) throws Exception;

	public TrancamentoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

//	public List<TrancamentoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ) throws Exception;

//	public List<TrancamentoVO> consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

//	public List<TrancamentoVO> consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

//	public List<TrancamentoVO> consultarPorMatriculaMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

//	public List<TrancamentoVO> consultarPorCodigoRequerimento(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

//	public List<TrancamentoVO> consultarPorTipoJustificativa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

//	public List<TrancamentoVO> consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public List<TrancamentoVO> consultarPorMatriculaSituacaoFinanceira(String matricula, String string, Integer i, boolean b, int nivelmontardadosDadosbasicos,  UsuarioVO usuario) throws Exception;

//	public List<TrancamentoVO> consultarPorDataUnidadeEnsino(Date datainicio, Date dataFim, String unidadeEnsino, boolean b, int nivelmontardadosDadosbasicos,  UsuarioVO usuario) throws Exception;

	public TrancamentoVO consultarPorCodigoRequerimento(Integer codigoPrm, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

	public void alterarSituacao(final Integer trancamento, final String situacao) throws Exception;

//	public List<TrancamentoVO> consultarPorNomeAluno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

//	public List<TrancamentoVO> consultarPorTurma(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception;

//	public List<TrancamentoVO> consultaRapidaPorNomeAluno(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

//	public List<TrancamentoVO> consultaRapidaPorTurma(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

//	public List<TrancamentoVO> consultaRapidaPorTipoJustificativa(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

//	public List<TrancamentoVO> consultaRapidaPorCodigoRequerimento(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

//	public List<TrancamentoVO> consultaRapidaPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<TrancamentoVO> consultaRapidaPorMatricula(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

//	public List<TrancamentoVO> consultaRapidaPorSituacao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

//	public List<TrancamentoVO> consultaRapidaPorData(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public String imprimirDeclaracaoTrancamento(TrancamentoVO trancamentoVO, TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO, ConfiguracaoGeralSistemaVO config,  UsuarioVO usuario) throws Exception;
	
	public void validarDadosAntesImpressao(TrancamentoVO obj, Integer textoPadrao) throws Exception;

	void incluirEDeferirRequerimento(TrancamentoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,  UsuarioVO usuarioVO, Boolean realizandoTrancamentoAutomatico) throws Exception;

	Date consultarDataTrancamentoPorMatricula(String matricula, UsuarioVO usuarioVO, Boolean filtrarTipoRetorno) throws Exception;

	/**
	 * Responsável por executar a alteração da situação do requerimento para FD (Finalizado Deferido), realizar a alteração da situação da matrícula e
	 * por fim realizar a alteração da situação acadêmica da matrícula período.
	 * 
	 * @author Wellington Rodrigues - 01/04/2015
	 * @param matriculaPeriodoVO
	 * @param historicoVOs
	 * @param requerimentoVO
	 * @param data
	 * @param origem
	 * @param codigoOrigemFechamentoMatriculaPeriodo
	 * @param alunoTransferidoUnidade
	 * @param relativoAbandonoDeCurso
	 * @param configuracaoFinanceiroVO
	 * @param usuario
	 * @throws Exception
	 */
	void alterarSituacaoAcademicaMatricula(MatriculaPeriodoVO matriculaPeriodoVO, List<HistoricoVO> historicoVOs, RequerimentoVO requerimentoVO, Date data, OrigemFechamentoMatriculaPeriodoEnum origem, Integer codigoOrigemFechamentoMatriculaPeriodo, boolean alunoTransferidoUnidade, boolean relativoAbandonoDeCurso,  Boolean inativarUsuarioLdap , Boolean inativarUsuarioBlackBoard  ,UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar a validação da situação do requerimento.
	 * 
	 * @author Wellington Rodrigues - 01/04/2015
	 * @param obj
	 * @throws ConsistirException
	 */
	void validarSituacaoRequerimento(RequerimentoVO obj) throws ConsistirException;	

	/**
	 * Responsável por executar a montagem dos históricos para realizar alteração da situação de acordo com a ultima matrícula período cuja situação
	 * seja AT ou PR.
	 * 
	 * @author Wellington Rodrigues - 01/04/2015
	 * @param matriculaVO
	 * @param configuracaoFinanceiroVO
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	List<HistoricoVO> executarMontagemHistoricosParaRealizarAlteracaoSituacao(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO,   UsuarioVO usuarioVO) throws Exception;

	/**
	 * Responsável por executar a persistência dos dados pertinentes a TrancamentoVO.
	 * 
	 * @author Wellington Rodrigues - 01/04/2015
	 * @param obj
	 * @param configuracaoGeralSistemaVO
	 * @param configuracaoFinanceiroVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	void persistir(TrancamentoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,  UsuarioVO usuarioVO) throws Exception;
	
	/**
	 * Responsável por executar a alteração da situação da matrícula período, alteração de todos os históricos, retirar a reserva da turma e realizar
	 * criação da MatriculaDW.
	 * 
	 * @author Wellington Rodrigues - 01/04/2015
	 * @param matriculaPeriodoVO
	 * @param historicoVOs
	 * @param data
	 * @param origem
	 * @param codigoOrigemFechamentoMatriculaPeriodo
	 * @param configuracaoFinanceiroVO
	 * @param usuario
	 * @throws Exception
	 */
	public void alterarSituacaoAcademicaMatriculaPeriodo(MatriculaPeriodoVO matriculaPeriodoVO, List<HistoricoVO> historicoVOs, Date data, OrigemFechamentoMatriculaPeriodoEnum origem, Integer codigoOrigemFechamentoMatriculaPeriodo,  UsuarioVO usuario) throws Exception;

	/**
	 * @author Wellington - 12 de jan de 2016
	 * @param matriculaVOs
	 * @param tipoTrancamento
	 * @param configuracaoFinanceiroVO
	 * @param configuracaoGeralSistemaVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	void persistirTrancamentoMapaRegistroAbandonoCursoTrancamento(List<MatriculaPeriodoVO> matriculaPeriodoVOs, String tipoTrancamento, ProgressBarVO progressBarVO, UsuarioVO usuarioVO) throws Exception;
	
	void persistirTrancamentoMapaRegistroAbandonoCursoTrancamentoIndividualmente(MapaRegistroEvasaoCursoMatriculaPeriodoVO mrecmp,  ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,  UsuarioVO usuarioVO) throws Exception;
	
	void executarEstorno(TrancamentoVO trancamentoVO, UsuarioVO usuarioVO) throws Exception;

	void realizarDefinicaoMatriculaPeriodoRealizarTrancamento(TrancamentoVO trancamentoVO,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, 
			UsuarioVO usuarioVO) throws Exception;

//	List<TrancamentoVO> consultaRapidaPorRegistroAcademicoAluno(String valorConsulta, Integer unidadeEnsino,boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void executarVerificacaoEGeracaoMatriculaPeriodoSemestrePosteriorMapaRegistroAbandonoCursoTrancamento(
			TrancamentoVO trancamentoVO, MatriculaPeriodoVO matriculaPeriodoVO,
			 UsuarioVO usuarioVO) throws Exception;

	void consultaOtimizada(DataModelo controleConsulta, String tipoJustificativa, String situacao, String tipo,
			Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

}