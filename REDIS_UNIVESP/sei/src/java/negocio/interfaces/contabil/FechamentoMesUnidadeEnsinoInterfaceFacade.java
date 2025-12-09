package negocio.interfaces.contabil;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.FechamentoMesUnidadeEnsinoVO;

public interface FechamentoMesUnidadeEnsinoInterfaceFacade {

    public FechamentoMesUnidadeEnsinoVO novo() throws Exception;
    public void incluir(FechamentoMesUnidadeEnsinoVO obj, UsuarioVO usuario) throws Exception;
    public void incluirListaFechamentoMesUnidadeEnsino(Integer codigoFechamentoMes, List<FechamentoMesUnidadeEnsinoVO> objetos, UsuarioVO usuario) throws Exception;
    public void alterar(FechamentoMesUnidadeEnsinoVO obj, UsuarioVO usuario) throws Exception;
    public void alterarListaFechamentoMesUnidadeEnsino(Integer codigoFechamentoMes, List<FechamentoMesUnidadeEnsinoVO> objetos, UsuarioVO usuario) throws Exception;
    public void excluir(FechamentoMesUnidadeEnsinoVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(Integer codigoFechamento, UsuarioVO usuario) throws Exception;
    public FechamentoMesUnidadeEnsinoVO consultarPorChavePrimaria(Integer codigo,UsuarioVO usuario) throws Exception;
    public List<FechamentoMesUnidadeEnsinoVO> consultarPorFechamentoMes(Integer codigoFechamentoMes, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);

}