package negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.PerguntaQuestionarioVO;

public interface PerguntaQuestionarioInterfaceFacade {

    public PerguntaQuestionarioVO novo() throws Exception;

    public void incluir(PerguntaQuestionarioVO obj, UsuarioVO usuarioVO) throws Exception;

    public void alterar(PerguntaQuestionarioVO obj, UsuarioVO usuarioVO) throws Exception;

    public void excluir(PerguntaQuestionarioVO obj, UsuarioVO usuarioVO) throws Exception;

    public List consultarPorDescricaoPergunta(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuarioLogado) throws Exception;

    public List consultarPorDescricaoQuestionario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuarioLogado) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuarioLogado) throws Exception;

    public void excluirPerguntaQuestionarios(Integer questionario, UsuarioVO usuarioVO) throws Exception;

    public void excluirPerguntaQuestionarios(Integer questionario, List objetos, UsuarioVO usuarioVO) throws Exception;

    public void incluirPerguntaQuestionarios(Integer questionarioPrm, List objetos, UsuarioVO usuarioVO) throws Exception;

    public PerguntaQuestionarioVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,UsuarioVO usuarioLogado) throws Exception;

    public void setIdEntidade(String idEntidade);

    public void alterarPerguntaQuestionarios(Integer questionario, List objetos, UsuarioVO usuarioVO) throws Exception;
    
    public List<PerguntaQuestionarioVO> consultarPorCodigoQuestionario(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
}
