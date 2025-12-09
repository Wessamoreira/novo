package negocio.interfaces.biblioteca;

import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.ImpressoraVO;

public interface ImpressoraInterfaceFacade {
	
	void persistir(ImpressoraVO impressoraVO, UsuarioVO usuarioVO) throws Exception;
	void excluir(ImpressoraVO impressoraVO, UsuarioVO usuarioVO) throws Exception;
	List<ImpressoraVO> consultar(String consultarPor, String valorConsulta, UnidadeEnsinoVO unidadeEnsinoVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	void realizarImpressaoTeste(ImpressoraVO impressoraVO, UsuarioVO usuarioVO) throws Exception;	

}
