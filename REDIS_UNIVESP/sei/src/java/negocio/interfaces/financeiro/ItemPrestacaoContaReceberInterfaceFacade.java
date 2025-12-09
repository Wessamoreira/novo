package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ItemPrestacaoContaOrigemContaReceberVO;
import negocio.comuns.financeiro.ItemPrestacaoContaReceberVO;
import negocio.comuns.financeiro.PrestacaoContaVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;


public interface ItemPrestacaoContaReceberInterfaceFacade {
    
    void incluirItemPrestacaoContaReceberVOs(List<ItemPrestacaoContaReceberVO> itemPrestacaoContaReceberVOs, ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberVO) throws Exception;
    
    void alterarItemPrestacaoContaReceberVOs(List<ItemPrestacaoContaReceberVO> itemPrestacaoContaReceberVOs, ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberVO) throws Exception;
    
    void excluirItemPrestacaoContaReceberVOs(List<ItemPrestacaoContaReceberVO> itemPrestacaoContaReceberVOs, ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberVO) throws Exception;
    
    List<ItemPrestacaoContaReceberVO> consultarItemPrestacaoContaReceberPorItemPrestacaoContaOrigemContaReceber(ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceber, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

    List<ItemPrestacaoContaReceberVO> consultarContaReceberDisponivelPrestacaoConta(String favorecido, String tipoFiltroData, Date dataInicio, Date dataTermino, String tipoOrigem, PrestacaoContaVO prestacaoContaVO);
}
