package negocio.interfaces.blackboard;

import java.util.List;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.blackboard.BlackboardFechamentoNotaOperacaoVO;
import negocio.comuns.blackboard.BlackboardGestaoFechamentoNotaVO;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.utilitarias.ProgressBarVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface BlackboardFechamentoNotaOperacaoInterfaceFacade {

	public Boolean validarExisteOperacaoFechamentoNotaPendente();
	
	public BlackboardGestaoFechamentoNotaVO consultarConfguracaoAcademicaParaFechamentoNota(String idSalaAulaBlackboard, List<UnidadeEnsinoVO> unidadeEnsinoVOs, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, Integer bimestre, Integer curso, Integer turma, Integer disciplina, String matricula, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioVO, DataModelo controleConsulta, String nivelEducacional) throws Exception;

	public void realizarOperacaoDeFechamentoNota(BlackboardGestaoFechamentoNotaVO salaAulaBlackboardGestaoFechamentoNotaVO, String idSalaAulaBlackboard, List<UnidadeEnsinoVO> unidadeEnsinoVOs, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, Integer bimestre, Integer curso, Integer turma, Integer disciplina, String matricula, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioLogado) throws Exception;

	public List<BlackboardFechamentoNotaOperacaoVO> consultarFechamentoNotaOperacaoNaoExecutado() throws Exception;
	
	public void executarOperacaoFechamentoNotaBlackboard(List<BlackboardFechamentoNotaOperacaoVO> operacoes, ProgressBarVO progressBarFechamentoNota, SuperControle superControle) throws Exception;

	public List<BlackboardFechamentoNotaOperacaoVO> consultarFechamentoNotaErro();
}
