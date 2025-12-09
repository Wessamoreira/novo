package negocio.interfaces.financeiro;

import java.util.List;
import java.util.Set;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;


public interface DescontoChancelaInterfaceFacade {

	List<MatriculaVO> executarConsultaEscolhaDescontoChancela(Integer turmaCodigo, UsuarioVO usuario) throws Exception;

	public void executarGravarListaMatricula(Set<MatriculaVO>  listaMatricula, UsuarioVO usuario) throws Exception;
	
	public void alterar(MatriculaVO  matriculaCodigo, UsuarioVO usuario) throws Exception;

}
