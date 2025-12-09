package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.AtividadeComplementarRelVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;

public interface AtividadeComplementarRelInterfaceFacade {

	List<AtividadeComplementarRelVO> criarObjetoLayoutAtividadeComplementarAnalitico(CursoVO curso, TurmaVO turma, String matricula, UnidadeEnsinoVO unidade, String ano, String semestre, String situacao, Boolean ativo, Boolean trancado, Boolean cancelado, Boolean abandonado, Boolean formado, Boolean transferenciaInterna, Boolean transferenciaExterna, Boolean preMatricula, Boolean preMatriculaCancelada, Boolean concluido, Boolean jubilado, String filtro) throws Exception;
	List<AtividadeComplementarRelVO> criarObjetoLayoutAtividadeComplementarSintetico(CursoVO curso, TurmaVO turma, String matricula, UnidadeEnsinoVO unidade, String ano, String semestre, String situacao, Boolean ativo, Boolean trancado, Boolean cancelado, Boolean abandonado, Boolean formado, Boolean transferenciaInterna, Boolean transferenciaExterna, Boolean preMatricula, Boolean preMatriculaCancelada, Boolean concluido, Boolean jubilado, String filtro) throws Exception;
}
