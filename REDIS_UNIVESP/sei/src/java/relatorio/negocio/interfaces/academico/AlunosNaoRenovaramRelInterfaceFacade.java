package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import relatorio.negocio.comuns.academico.AlunosNaoRenovaramRelVO;

public interface AlunosNaoRenovaramRelInterfaceFacade {

    void validarDados(UnidadeEnsinoVO unidadeEnsino, PeriodicidadeEnum periodicidadeEnum, String ano, String semestre) throws ConsistirException;

    String designIReportRelatorio();

    String designIReportRelatorioSintetico();

    String caminhoBaseRelatorio();      

    List<AlunosNaoRenovaramRelVO> criarObjeto(UnidadeEnsinoVO unidadeEnsino, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turma, PeriodicidadeEnum periodicidadeEnum, String ano, String semestre, String tipoRelatorio, Boolean desconsiderarAlunoPreMatriculado, Boolean desconsiderarPossivelFormando, UsuarioVO usuarioVO) throws Exception;

	void validarDadosUnidaEnsino(UnidadeEnsinoVO unidadeEnsino) throws ConsistirException;

}