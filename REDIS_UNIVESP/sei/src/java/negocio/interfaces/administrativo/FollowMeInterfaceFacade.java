package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FollowMeGrupoDestinatarioVO;
import negocio.comuns.administrativo.FollowMeVO;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.painelGestor.FollowMeRelVO;


public interface FollowMeInterfaceFacade {
    
    void persistir(FollowMeVO followMeVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

    void excluir(FollowMeVO followMeVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;
    
    
    
    List<FollowMeVO> consultar(String descricao, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuarioVO, Integer limite, Integer pagina) throws Exception;
    
    void adicionarFollowMeGrupoDestinatarioVO(FollowMeVO followMeVO, FollowMeGrupoDestinatarioVO followMeGrupoDestinatarioVO) throws Exception;
    
    void removerFollowMeGrupoDestinatarioVO(FollowMeVO followMeVO, FollowMeGrupoDestinatarioVO followMeGrupoDestinatarioVO);
    
    FollowMeVO consultarPorChavePrimaria(Integer followMe, int nivelMontarDados,  boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

    Integer consultarTotalRegistros(String descricao) throws Exception;

    void realizarEnvioDadosFollowMe()   throws Exception;

    void realizarEnvioDadosFollowMeAgora(FollowMeVO followMeVO,ConfiguracaoGeralSistemaVO confUpload) throws Exception;

    List<FollowMeRelVO> consultarFollowMeRelPorFollowMe(Integer followMe, Integer limit, Integer pagina);

    Integer consultarTotalRegistroFollowMeRelPorFollowMe(Integer followMe);

}
