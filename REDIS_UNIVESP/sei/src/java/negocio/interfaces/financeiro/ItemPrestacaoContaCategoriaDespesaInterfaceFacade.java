package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ItemPrestacaoContaCategoriaDespesaVO;
import negocio.comuns.financeiro.ItemPrestacaoContaPagarVO;
import negocio.comuns.financeiro.PrestacaoContaVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;


public interface ItemPrestacaoContaCategoriaDespesaInterfaceFacade {

    void incluirItemPrestacaoContaCategoriaDespesaVOs(List<ItemPrestacaoContaCategoriaDespesaVO> itemPrestacaoContaCategoriaDespesaVOs, PrestacaoContaVO prestacaoContaVO) throws Exception;
    
    void alterarItemPrestacaoContaCategoriaDespesaVOs(List<ItemPrestacaoContaCategoriaDespesaVO> itemPrestacaoContaCategoriaDespesaVOs, PrestacaoContaVO prestacaoContaVO) throws Exception;
    
    void excluirItemPrestacaoContaCategoriaDespesaVOs(List<ItemPrestacaoContaCategoriaDespesaVO> itemPrestacaoContaCategoriaDespesaVOs, PrestacaoContaVO prestacaoContaVO) throws Exception;
    
    List<ItemPrestacaoContaCategoriaDespesaVO> consultarItemPrestacaoContaCategoriaDespesaPorPrestacaoConta(Integer prestacaoConta, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
    
    void adicionarItemPrestacaoContaPagarVO(ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesaVO, ItemPrestacaoContaPagarVO itemPrestacaoContaPagarVO);

    void removerItemPrestacaoContaPagarVO(PrestacaoContaVO prestacaoContaVO, ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesaVO, ItemPrestacaoContaPagarVO itemPrestacaoContaPagarVO);

	ItemPrestacaoContaPagarVO consultarItemPrestacaoContaCategoriaDespesaVO(ItemPrestacaoContaCategoriaDespesaVO ipccd, ContaPagarVO contaPagar);

    
}
