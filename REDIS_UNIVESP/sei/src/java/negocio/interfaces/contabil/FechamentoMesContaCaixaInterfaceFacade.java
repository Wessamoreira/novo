package negocio.interfaces.contabil;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.FechamentoMesContaCaixaVO;

public interface FechamentoMesContaCaixaInterfaceFacade {

    public FechamentoMesContaCaixaVO novo() throws Exception;
    public void incluir(FechamentoMesContaCaixaVO obj, UsuarioVO usuario) throws Exception;
    public void incluirListaFechamentoMesContaCaixa(Integer codigoFechamentoMes, List<FechamentoMesContaCaixaVO> objetos, UsuarioVO usuario) throws Exception;
    public void alterar(FechamentoMesContaCaixaVO obj, UsuarioVO usuario) throws Exception;
    public void alterarListaFechamentoMesContaCaixa(Integer codigoFechamentoMes, List<FechamentoMesContaCaixaVO> objetos, UsuarioVO usuario) throws Exception;
    public void excluir(FechamentoMesContaCaixaVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(Integer codigoFechamento, UsuarioVO usuario) throws Exception;
    public FechamentoMesContaCaixaVO consultarPorChavePrimaria(Integer codigo,UsuarioVO usuario) throws Exception;
    public List<FechamentoMesContaCaixaVO> consultarPorFechamentoMes(Integer codigoFechamentoMes, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);

}