package negocio.interfaces.academico;

import java.io.File;
import java.util.List;

import negocio.comuns.academico.MapaAlunoAptoFormarVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface MapaAlunoAptoFormarInterfaceFacade {
	
	public File realizarGeracaoExcel(List<MapaAlunoAptoFormarVO> listaMapaAlunoAptoForma,  String urlLogoPadraoRelatorio, UsuarioVO usuario) throws Exception;

}
