package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.CampanhaMidiaVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface CampanhaMidiaInterfaceFacade {

    public void adicionarObjCampanhaMidiaVOs(CampanhaVO campanhaVO, CampanhaMidiaVO obj) throws Exception;

    public void excluirObjCampanhaMidiaVOs(CampanhaVO campanhaVO, CampanhaMidiaVO campanhaItemVO) throws Exception;

    public void incluirCampanhaMidia(Integer campanha, List objetos) throws Exception;
    
    public void alterarCampanhaMidia(Integer campanha, List objetos) throws Exception;

    public void excluirCampanhaMidia(Integer campanha) throws Exception;
    
    public List<CampanhaMidiaVO> consultarCampanhaMidiaPorCampanha(Integer campanha, UsuarioVO usuarioVO);
}
