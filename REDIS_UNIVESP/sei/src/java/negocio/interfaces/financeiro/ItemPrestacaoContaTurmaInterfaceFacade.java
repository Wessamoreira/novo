package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ItemPrestacaoContaTurmaVO;
import negocio.comuns.financeiro.PrestacaoContaVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;


public interface ItemPrestacaoContaTurmaInterfaceFacade {
    
    void incluirItemPrestacaoContaTurmaVOs(List<ItemPrestacaoContaTurmaVO> itemPrestacaoContaTurmaVOs, PrestacaoContaVO prestacaoContaVO) throws Exception;
    
    void alterarItemPrestacaoContaTurmaVOs(List<ItemPrestacaoContaTurmaVO> itemPrestacaoContaTurmaVOs, PrestacaoContaVO prestacaoContaVO) throws Exception;
    
    void excluirItemPrestacaoContaTurmaVOs(List<ItemPrestacaoContaTurmaVO> itemPrestacaoContaTurmaVOs, PrestacaoContaVO prestacaoContaVO) throws Exception;
    
    List<ItemPrestacaoContaTurmaVO> consultarItemPrestacaoContaTurmaPorPrestacaoConta(Integer prestacaoConta, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
}
