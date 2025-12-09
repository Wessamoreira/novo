package negocio.interfaces.academico;

import negocio.comuns.academico.ProfessorTitularDisciplinaTurmaLogVO;
import negocio.comuns.academico.ProfessorTitularDisciplinaTurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface ProfessorTitularDisciplinaTurmaLogInterfaceFacade {

    public void incluir(ProfessorTitularDisciplinaTurmaLogVO obj) throws Exception;

    public void preencherProfessorTitularDisciplinaTurmaLog(ProfessorTitularDisciplinaTurmaVO obj, String operacao, UsuarioVO usuario) throws Exception;

    public void preencherProfessorTitularDisciplinaTurmaLogOperacaoExclusao(ProfessorTitularDisciplinaTurmaVO contaReceber, String operacao, UsuarioVO usuario) throws Exception;

    public void incluirOperacaoExclusao(final ProfessorTitularDisciplinaTurmaLogVO obj) throws Exception;
}
