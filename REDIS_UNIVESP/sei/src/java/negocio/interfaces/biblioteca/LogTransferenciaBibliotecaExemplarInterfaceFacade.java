package negocio.interfaces.biblioteca;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.LogTransferenciaBibliotecaExemplarVO;

public interface LogTransferenciaBibliotecaExemplarInterfaceFacade {

	public void incluir(LogTransferenciaBibliotecaExemplarVO obj, UsuarioVO usuario) throws Exception;
	public List consultarPorExemplar(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
}