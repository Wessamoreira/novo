package negocio.interfaces.basico;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.BairroVO;
import negocio.comuns.basico.CidadeVO;

public interface BairroInterfaceFacade {

    public BairroVO novo() throws Exception;

    public void incluir(BairroVO obj) throws Exception;

    public void alterar(BairroVO obj) throws Exception;

    public void excluir(BairroVO obj) throws Exception;

    public List consultarPorDescricao(String valorConsulta, CidadeVO cidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomeCidade(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public BairroVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String idEntidade);
}
