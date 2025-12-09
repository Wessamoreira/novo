package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ItemPrestacaoContaOrigemContaReceberVO;
import negocio.comuns.financeiro.ItemPrestacaoContaReceberVO;
import negocio.comuns.financeiro.PrestacaoContaVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;


public interface ItemPrestacaoContaOrigemContaReceberInterfaceFacade {
    
     void incluirItemPrestacaoContaOrigemContaReceberVOs(List<ItemPrestacaoContaOrigemContaReceberVO> itemPrestacaoContaOrigemContaReceberVOs, PrestacaoContaVO prestacaoContaVO) throws Exception;
    
    void alterarItemPrestacaoContaOrigemContaReceberVOs(List<ItemPrestacaoContaOrigemContaReceberVO> itemPrestacaoContaOrigemContaReceberVOs, PrestacaoContaVO prestacaoContaVO) throws Exception;
    
    void excluirItemPrestacaoContaOrigemContaReceberVOs(List<ItemPrestacaoContaOrigemContaReceberVO> OrigemContaReceberVOntaReceberVOs, PrestacaoContaVO prestacaoContaVO) throws Exception;
    
    List<ItemPrestacaoContaOrigemContaReceberVO> consultarItemPrestacaoContaOrigemContaReceberPorPrestacaoConta(Integer prestacaoConta, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
    
    void adicionarItemPrestacaoContaReceberVO(ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberVO, ItemPrestacaoContaReceberVO itemPrestacaoContaReceberVO);

    void removerItemPrestacaoContaReceberVO(PrestacaoContaVO prestacaoContaVO,ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberVO, ItemPrestacaoContaReceberVO itemPrestacaoContaReceberVO);

    void adicionarVariasItensPrestacaoContaReceberVOs(List<ItemPrestacaoContaReceberVO> itemPrestacaoContaReceberVOs, PrestacaoContaVO prestacaoContaVO) throws Exception;

}
