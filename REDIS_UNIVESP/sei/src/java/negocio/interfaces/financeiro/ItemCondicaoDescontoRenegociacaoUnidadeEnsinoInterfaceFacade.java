package negocio.interfaces.financeiro;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO;
import negocio.comuns.financeiro.ItemCondicaoDescontoRenegociacaoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface ItemCondicaoDescontoRenegociacaoUnidadeEnsinoInterfaceFacade {
	

    public void validarDados(ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO obj);
    public ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public void setIdEntidade(String aIdEntidade);
	List<ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO> consultarItemCondicaoDescontoRenegociacaoUnidadeEnsinoPoritemCondicaoDescontoRenegociacao(Integer itemCondicaoDescontoRenegociacao, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	void persistir(ItemCondicaoDescontoRenegociacaoVO obj, List<ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	
    
}