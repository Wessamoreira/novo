package negocio.interfaces.biblioteca;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.MotivoEmprestimoPorHoraVO;


public interface MotivoEmprestimoPorHoraInterfaceFacade {
	

    public void incluir(MotivoEmprestimoPorHoraVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
    public void alterar(MotivoEmprestimoPorHoraVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
    public void excluir(MotivoEmprestimoPorHoraVO obj, UsuarioVO usuarioVO) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)  throws Exception;
    
}