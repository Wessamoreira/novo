package negocio.interfaces.contabil;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.FechamentoMesHistoricoModificacaoVO;
import negocio.comuns.contabil.FechamentoMesVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;

public interface FechamentoMesHistoricoModificacaoInterfaceFacade {

    public FechamentoMesHistoricoModificacaoVO novo() throws Exception;
    public void incluir(FechamentoMesHistoricoModificacaoVO obj, UsuarioVO usuario) throws Exception;
    public void incluirListaFechamentoMesHistoricoModificacao(Integer codigoFechamentoMes, List<FechamentoMesHistoricoModificacaoVO> objetos, UsuarioVO usuario) throws Exception;
    public void alterar(FechamentoMesHistoricoModificacaoVO obj, UsuarioVO usuario) throws Exception;
    public void alterarListaFechamentoMesHistoricoModificacao(Integer codigoFechamentoMes, List<FechamentoMesHistoricoModificacaoVO> objetos, UsuarioVO usuario) throws Exception;
    public void excluir(FechamentoMesHistoricoModificacaoVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(Integer codigoFechamento, UsuarioVO usuario) throws Exception;
    public FechamentoMesHistoricoModificacaoVO consultarPorChavePrimaria(Integer codigo,UsuarioVO usuario) throws Exception;
    public List<FechamentoMesHistoricoModificacaoVO> consultarPorFechamentoMes(Integer codigoFechamentoMes, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public FechamentoMesHistoricoModificacaoVO gerarNovoHistoricoModificacao(FechamentoMesVO fechamentoMes, UsuarioVO usuario, TipoOrigemHistoricoBloqueioEnum tipoOrigem, String descricao, String detalhe);

}