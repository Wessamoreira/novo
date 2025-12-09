package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.FollowMeDepartamentoVO;
import negocio.comuns.administrativo.FollowMeVO;


public interface FollowMeDepartamentoInterfaceFacade {
    
    void persistir(FollowMeDepartamentoVO followMeDepartamentoVO, FollowMeVO followMeVO) throws Exception;
    
    List<FollowMeDepartamentoVO> consultarPorFollowMe(Integer followMe) throws Exception;
    
    List<FollowMeDepartamentoVO> consultarPorFollowMeTrazendoTodosDepartamento(Integer followMe) throws Exception;
    
    void excluirFollowMeDepartamento(FollowMeVO followMeVO) throws Exception;

}
