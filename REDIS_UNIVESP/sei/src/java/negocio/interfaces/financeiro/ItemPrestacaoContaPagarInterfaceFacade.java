package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ItemPrestacaoContaCategoriaDespesaVO;
import negocio.comuns.financeiro.ItemPrestacaoContaPagarVO;
import negocio.comuns.financeiro.PrestacaoContaVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;


public interface ItemPrestacaoContaPagarInterfaceFacade {
    
    void incluirItemPrestacaoContaPagarVOs(List<ItemPrestacaoContaPagarVO> itemPrestacaoContaPagarVOs, ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesaVO) throws Exception;
    
    void alterarItemPrestacaoContaPagarVOs(List<ItemPrestacaoContaPagarVO> itemPrestacaoContaPagarVOs, ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesaVO) throws Exception;
    
    void excluirItemPrestacaoContaPagarVOs(List<ItemPrestacaoContaPagarVO> itemPrestacaoContaPagarVOs, ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesaVO) throws Exception;
    
    List<ItemPrestacaoContaPagarVO> consultarItemPrestacaoContaPagarPorItemPrestacaoContaCategoriaDespesa(ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesa, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

    List<ItemPrestacaoContaPagarVO> consultarContaPagarDisponivelPrestacaoConta(String favorecido, String tipoFiltroData, Date dataInicio, Date dataTermino, PrestacaoContaVO prestacaoContaVO);

}
