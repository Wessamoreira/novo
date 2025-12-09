package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.FollowMeCategoriaDespesaVO;
import negocio.comuns.administrativo.FollowMeVO;


public interface FollowMeCategoriaDespesaInterfaceFacade {
    
    void persistir(FollowMeCategoriaDespesaVO followMeCategoriaDespesaVO, FollowMeVO followMeVO) throws Exception;
    
    List<FollowMeCategoriaDespesaVO> consultarPorFollowMe(Integer followMe) throws Exception;
    
    List<FollowMeCategoriaDespesaVO> consultarPorFollowMeTrazendoTodasCategoriaDespesa(Integer followMe) throws Exception;
    
    void excluirFollowMeCategoriaDespesa(FollowMeVO followMeVO) throws Exception;

}
