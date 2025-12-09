package negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.QuestionarioAlunoVO;
import negocio.comuns.processosel.RespostaPerguntaAlunoVO;

public interface RespostaPerguntaAlunoInterfaceFacade {

    public void incluirRespostaPerguntaAluno(QuestionarioAlunoVO questionarioAluno) throws Exception;

    public void incluir(RespostaPerguntaAlunoVO obj) throws Exception;

    public void alterar(RespostaPerguntaAlunoVO obj) throws Exception;

    public void excluir(RespostaPerguntaAlunoVO obj) throws Exception;

    public List<RespostaPerguntaAlunoVO> consultarPorQuestionarioAluno(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

    public RespostaPerguntaAlunoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public void excluirRespostaPerguntaAluno(QuestionarioAlunoVO questionarioAluno) throws Exception;
}
