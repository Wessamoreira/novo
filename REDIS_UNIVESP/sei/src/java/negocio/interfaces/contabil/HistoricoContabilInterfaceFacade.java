package negocio.interfaces.contabil;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.HistoricoContabilVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface HistoricoContabilInterfaceFacade {
	

    public HistoricoContabilVO novo() throws Exception;
    public void incluir(HistoricoContabilVO obj) throws Exception;
    public void alterar(HistoricoContabilVO obj) throws Exception;
    public void excluir(HistoricoContabilVO obj) throws Exception;
    public HistoricoContabilVO consultarPorChavePrimaria(Integer codigo,UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public List consultarPorNome(String valorConsulta, String ordenacao, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

}