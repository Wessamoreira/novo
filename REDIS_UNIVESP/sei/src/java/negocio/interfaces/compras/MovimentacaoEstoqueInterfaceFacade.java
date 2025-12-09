package negocio.interfaces.compras;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.MovimentacaoEstoqueVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface MovimentacaoEstoqueInterfaceFacade {
	

    
    public void incluir(MovimentacaoEstoqueVO obj,UsuarioVO usuario) throws Exception;
    public void alterar(MovimentacaoEstoqueVO obj,UsuarioVO usuarioVO) throws Exception;
    public MovimentacaoEstoqueVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List<MovimentacaoEstoqueVO> consultarPorCodigo(Integer valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List<MovimentacaoEstoqueVO> consultarPorTipoMovimentacao(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List<MovimentacaoEstoqueVO> consultarPorNomeProduto(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
}