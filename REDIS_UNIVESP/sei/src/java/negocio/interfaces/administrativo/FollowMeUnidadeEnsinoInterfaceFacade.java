package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.FollowMeUnidadeEnsinoVO;
import negocio.comuns.administrativo.FollowMeVO;


public interface FollowMeUnidadeEnsinoInterfaceFacade {
    
    void persistir(FollowMeUnidadeEnsinoVO followMeUnidadeEnsinoVO, FollowMeVO followMeVO) throws Exception;
    
    List<FollowMeUnidadeEnsinoVO> consultarPorFollowMe(Integer followMe) throws Exception;
    
    List<FollowMeUnidadeEnsinoVO> consultarPorFollowMeTrazendoTodasUnidadesEnsino(Integer followMe) throws Exception;
    
    void excluirFollowMeUnidadeEnsino(FollowMeVO followMeVO) throws Exception;

}
