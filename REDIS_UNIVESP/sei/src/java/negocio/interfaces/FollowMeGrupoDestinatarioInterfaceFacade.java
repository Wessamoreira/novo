package negocio.interfaces;

import java.util.List;

import negocio.comuns.administrativo.FollowMeGrupoDestinatarioVO;
import negocio.comuns.administrativo.FollowMeVO;
import negocio.comuns.utilitarias.ConsistirException;


public interface FollowMeGrupoDestinatarioInterfaceFacade {
    
    void persistir(FollowMeGrupoDestinatarioVO followMeGrupoDestinatarioVO, FollowMeVO followMeVO) throws Exception;
    
    List<FollowMeGrupoDestinatarioVO> consultarPorFollowMe(Integer followMe, int nivelMontarDados);

    void validarDados(FollowMeGrupoDestinatarioVO followMeGrupoDestinatarioVO) throws ConsistirException;

    void excluirFollowMeGrupoDestinatario(FollowMeVO followMeVO) throws Exception;
    
}
