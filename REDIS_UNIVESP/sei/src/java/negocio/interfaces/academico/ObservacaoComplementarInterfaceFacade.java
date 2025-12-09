package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ObservacaoComplementarVO;
import negocio.comuns.arquitetura.UsuarioVO;


public interface ObservacaoComplementarInterfaceFacade {

    public ObservacaoComplementarVO novo() throws Exception;

    public void incluir(ObservacaoComplementarVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(ObservacaoComplementarVO obj, UsuarioVO usuario) throws Exception;

    public void excluir(ObservacaoComplementarVO obj, UsuarioVO usuario) throws Exception;

    public ObservacaoComplementarVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public List<ObservacaoComplementarVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

}
