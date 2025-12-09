package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.RenovacaoMatriculaTurmaMatriculaPeriodoVO;
import negocio.comuns.academico.RenovacaoMatriculaTurmaVO;
import negocio.comuns.academico.enumeradores.SituacaoRenovacaoMatriculaPeriodoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ProgressBarVO;

public interface RenovacaoMatriculaTurmaMatriculaPeriodoInterfaceFacade {

	void excluir(RenovacaoMatriculaTurmaMatriculaPeriodoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void persistir(RenovacaoMatriculaTurmaMatriculaPeriodoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<RenovacaoMatriculaTurmaMatriculaPeriodoVO> consultarPorRenovacaoMatriculaTurma(Integer renovacaoMatriculaTurma, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<RenovacaoMatriculaTurmaMatriculaPeriodoVO> consultarPorRenovacaoMatriculaTurmaESituacao(Integer renovacaoMatriculaTurma, SituacaoRenovacaoMatriculaPeriodoEnum situacao, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO, Integer limite, Integer offset) throws Exception;

	void alterar(RenovacaoMatriculaTurmaMatriculaPeriodoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void realizarConsultarMatriculaPeriodoAptaRenovar(RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean permitirRealizarMatriculaDisciplinaPreRequisito, UsuarioVO usuarioVO, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) throws Exception;
	
	void realizarConsultarMatriculaPeriodoAptaRenovarPorTurma(RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean permitirRealizarMatriculaDisciplinaPreRequisito, UsuarioVO usuarioVO, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs, ProgressBarVO progressBarVO) throws Exception;

	void realizarInicializacaoDadosPertinentesProcessamento(RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO, UsuarioVO usuario) throws Exception;

	MatriculaPeriodoVO realizarRenovacaoAutomaticaAtravesRenovacaPorTurma(RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO, RenovacaoMatriculaTurmaMatriculaPeriodoVO renovacaoMatriculaTurmaMatriculaPeriodoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean permitirRealizarMatriculaDisciplinaPreRequisito, UsuarioVO usuarioVO, boolean simularRenovacao, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) throws Exception;

	void realizarDefinicaoDadosFinanceiroMatriculaPeriodo(RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO, RenovacaoMatriculaTurmaMatriculaPeriodoVO renovacaoMatriculaTurmaMatriculaPeriodoVO, MatriculaVO matriculaVO, MatriculaPeriodoVO novaMatriculaPeriodo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean permitirRealizarMatriculaDisciplinaPreRequisito, UsuarioVO usuarioVO, boolean simularRenovacao, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) throws Exception;

	void incluirRenovacaoMatriculaTurmaMatriculaPeriodoVOs(RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void alterarRenovacaoMatriculaTurmaMatriculaPeriodoVOs(RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

}
