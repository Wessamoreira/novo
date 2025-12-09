package negocio.interfaces.financeiro;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ItemCondicaoRenegociacaoVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface ItemCondicaoRenegociacaoInterfaceFacade {
	

    public void validarDados(ItemCondicaoRenegociacaoVO obj) throws ConsistirException;
    public ItemCondicaoRenegociacaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario, int nivelMontarDados ) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    void alterarItemCondicaoRenegociacaos(Integer condicaoRenegociacao, List<ItemCondicaoRenegociacaoVO> objetos) throws Exception;
    void incluirItemCondicaoRenegociacaos(Integer condicaoRenegociacaoPrm, List<ItemCondicaoRenegociacaoVO> objetos) throws Exception;
    List<ItemCondicaoRenegociacaoVO> consultarItemCondicaoRenegociacaoAptaAlunoUtilizar(int condicaoRenegociacao, double valor) throws Exception;
}