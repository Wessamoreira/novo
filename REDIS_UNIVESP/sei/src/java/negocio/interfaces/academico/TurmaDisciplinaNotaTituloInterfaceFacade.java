package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.TurmaDisciplinaNotaTituloVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoNotaConceitoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface TurmaDisciplinaNotaTituloInterfaceFacade {
	
	void persistir(List<TurmaDisciplinaNotaTituloVO> turmaDisciplinaNotaTituloVOs, List<HistoricoVO> listaHistoricoVOs, TurmaVO turmaLancamentoNotaVO, UsuarioVO usuarioVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, boolean trazerAlunosTransferenciaMatriz, boolean permiteLancarNotaDisciplinaComposta) throws Exception;
	void excluir(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO, UsuarioVO usuarioVO) throws Exception;
	void incluir(final TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO, List<HistoricoVO> listaHistoricoVOs,  TurmaVO turmaLancamentoNotaVO, final UsuarioVO usuarioVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, boolean trazerAlunosTransferenciaMatriz, boolean permiteLancarNotaDisciplinaComposta) throws Exception;
	void alterar(final TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO, List<HistoricoVO> listaHistoricoVOs, TurmaVO turmaLancamentoNotaVO, final UsuarioVO usuarioVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, boolean trazerAlunosTransferenciaMatriz, boolean permiteLancarNotaDisciplinaComposta) throws Exception;		
	List<TurmaDisciplinaNotaTituloVO> consultarPorTurmaDisciplinaAnoSemestreConfiguracaoAcademica(TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, String semestre, ConfiguracaoAcademicoVO configuracaoAcademicoVO, UsuarioVO usuarioVO) throws Exception;
	TurmaDisciplinaNotaTituloVO consultarPorTurmaDisciplinaAnoSemestreConfiguracaoAcademicaTipoNota(TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, String semestre, ConfiguracaoAcademicoVO configuracaoAcademicoVO, TipoNotaConceitoEnum tipoNotaConceitoEnum, UsuarioVO usuarioVO) throws Exception;
	List<TurmaDisciplinaNotaTituloVO> consultarPorMatriculaPeriodoTurmaDisciplina(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception;
	List<TurmaDisciplinaNotaTituloVO> consultarPorHistorico(HistoricoVO historicoVO, UsuarioVO usuarioVO) throws Exception;
	void realizarCriacaoTurmaDisciplinaNotaTituloComBaseTipoTurma(TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, String semestre,
			ConfiguracaoAcademicoVO configuracaoAcademicoVO, UsuarioVO usuarioVO) throws Exception;
	public TurmaDisciplinaNotaTituloVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception;
	
	public String setarTituloNotaApresentar(String tipoNota, String tituloNotaApresentarConfiguracaoAcademico);

}
