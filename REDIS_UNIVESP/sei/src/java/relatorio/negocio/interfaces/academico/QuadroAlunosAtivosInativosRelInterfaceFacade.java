package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import relatorio.negocio.comuns.academico.AlunosAtivosInativosVO;
import relatorio.negocio.comuns.academico.QuadroAlunosAtivosInativosRelVO;

public interface QuadroAlunosAtivosInativosRelInterfaceFacade {

    public List<QuadroAlunosAtivosInativosRelVO> criarObjeto(String ano, String semestre, UnidadeEnsinoVO unidadeEnsinoVO, UnidadeEnsinoCursoVO unidadeEnsinoCurso, TurmaVO turma) throws Exception;

    public List<AlunosAtivosInativosVO> montarListaNrAlunosMatriculados(String ano, String semestre, UnidadeEnsinoVO unidadeEnsinoVO, UnidadeEnsinoCursoVO unidadeEnsinoCurso, TurmaVO turma) throws Exception;

    public String getDescricaoFiltros();

    public void setDescricaoFiltros(String string);
}
