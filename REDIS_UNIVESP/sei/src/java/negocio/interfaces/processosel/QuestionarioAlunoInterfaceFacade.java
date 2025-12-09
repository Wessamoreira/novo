package negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.processosel.QuestionarioAlunoVO;
import negocio.comuns.processosel.QuestionarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface QuestionarioAlunoInterfaceFacade {

    public void incluir(QuestionarioAlunoVO obj) throws Exception;

    public void alterar(QuestionarioAlunoVO obj) throws Exception;

    public void excluir(QuestionarioAlunoVO obj) throws Exception;

    public void montarRespostasQuestionarioAluno(QuestionarioVO obj, QuestionarioAlunoVO questionarioAluno, PessoaVO aluno) throws Exception;

    public QuestionarioAlunoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

   // public void alterarQuestionarioAluno(Integer perfilSocioEconomico, List objetos) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

//    public List consultarPorRespostaQuestionarioAluno(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
//
//    public List consultarPorTexto(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
//
//    public List consultarPorTipoResposta(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

    public void setIdEntidade(String aIdEntidade);

  //  public List consultarQuestionarioAluno(Integer perguntaQuestionario, int nivelMontarDados, UsuarioVO usuario) throws Exception;

//    public List consultarQuestionarioAluno(Integer perguntaQuestionario, Integer perfilSocioEconomico, int nivelMontarDados, UsuarioVO usuario) throws Exception;

 //   public void excluirQuestionarioAluno(Integer codigo) throws Exception;

   public QuestionarioAlunoVO consultarPorQuestionarioAluno(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

 //   public void montarListaRespostaQuestionarioAlunoBancoCurriculum(QuestionarioVO questionario, UsuarioVO usuarioVO) throws Exception;


}
