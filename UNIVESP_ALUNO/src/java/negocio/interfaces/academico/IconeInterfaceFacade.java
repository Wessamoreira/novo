package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.IconeVO;
import negocio.comuns.arquitetura.UsuarioVO;


public interface IconeInterfaceFacade {
    
    void perisitir(IconeVO iconeVO, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    
    List<IconeVO> consultarIcones(Integer limite, Integer pagina, String caminhoWebRepositorio) throws Exception;

}
