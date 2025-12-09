package relatorio.negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.LocalAulaVO;
import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
import relatorio.negocio.comuns.academico.CronogramaDeAulasRelVO;

public interface CronogramaDeAulasRelInterfaceFacade {

	/**
	 * @author Wellington Rodrigues - 22/06/2015
	 * @param unidadeEnsino
	 * @param curso
	 * @param turma
	 * @param disciplina
	 * @param dataInicio
	 * @param dataFim
	 * @param tipoLayout
	 * @param visaoAluno
	 * @param usuario
	 * @param ordenacao
	 * @return
	 * @throws Exception
	 */
	List<CronogramaDeAulasRelVO> criarObjeto(Integer unidadeEnsino, Integer curso, Integer turma, String tipoTurma, Integer disciplina, Date dataInicio, Date dataFim, String ano, String semestre, FuncionarioVO funcionario, TurnoVO turno, SalaLocalAulaVO salaLocalAula, LocalAulaVO localAula, String tipoLayout, boolean visaoAluno, UsuarioVO usuario, String ordenacao, PeriodicidadeEnum periodicidadeEnum) throws Exception;

	/**
	 * @author Rodrigo Wind - 11/03/2016
	 * @param layoutEtiqueta
	 * @param unidadeEnsino
	 * @param turma
	 * @param curso
	 * @param disciplina
	 * @param dataInicio
	 * @param dataFim
	 * @param ano
	 * @param semestre
	 * @param funcionario
	 * @param turno
	 * @param salaLocalAula
	 * @param localAula
	 * @param ordenacao
	 * @param usuarioVO
	 * @param numeroCopias
	 * @param linha
	 * @param coluna
	 * @param removerEspacoTAGVazia
	 * @param configuracaoGeralSistemaVO
	 * @return
	 */
	String realizarGeracaoImpressaoEtiquetaPorCronogramaAula(LayoutEtiquetaVO layoutEtiqueta, Integer unidadeEnsino, Integer turma, Integer curso, Integer disciplina, Date dataInicio, Date dataFim, String ano, String semestre, FuncionarioVO funcionario, TurnoVO turno, SalaLocalAulaVO salaLocalAula, LocalAulaVO localAula, String ordenacao, UsuarioVO usuarioVO, Integer numeroCopias, Integer linha, Integer coluna, Boolean removerEspacoTAGVazia, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, PeriodicidadeEnum periodicidade) throws Exception;

	/**
	 * @author Rodrigo Wind - 11/03/2016
	 * @param layoutEtiqueta
	 * @param cronogramaDeAulasRelVOs
	 * @param numeroCopias
	 * @param linha
	 * @param coluna
	 * @param removerEspacoTAGVazia
	 * @param configuracaoGeralSistemaVO
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	String realizarImpressaoEtiquetaCronogramaAula(LayoutEtiquetaVO layoutEtiqueta, List<CronogramaDeAulasRelVO> cronogramaDeAulasRelVOs, Integer numeroCopias, Integer linha, Integer coluna, Boolean removerEspacoTAGVazia, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;
	
	public List<CronogramaDeAulasRelVO> criarObjetoProgramacaoTutoriaOnline(Integer unidadeEnsino, Integer curso, Integer turma, String tipoTurma, Integer disciplina, Date dataInicio, Date dataFim, String ano, String semestre, FuncionarioVO funcionario, TurnoVO turno, SalaLocalAulaVO salaLocalAula, LocalAulaVO localAula, String tipoLayout, boolean visaoAluno, UsuarioVO usuario, String ordenacao, PeriodicidadeEnum periodicidadeEnum) throws Exception;
}