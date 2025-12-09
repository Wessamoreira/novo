package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.TrabalhoConclusaoCursoArtefatoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface TrabalhoConclusaoCursoArtefatoInterfaceFacade {
	
	void persistir(TrabalhoConclusaoCursoArtefatoVO trabalhoConclusaoCursoArtefatoVO) throws Exception;
	
	void realizarRegistroEntregaArtefato(TrabalhoConclusaoCursoArtefatoVO trabalhoConclusaoCursoArtefatoVO, UsuarioVO usuarioVO) throws Exception;
	
	void realizarDevolucaoEntregaArtefato(TrabalhoConclusaoCursoArtefatoVO trabalhoConclusaoCursoArtefatoVO, UsuarioVO usuarioVO) throws Exception;
	
	List<TrabalhoConclusaoCursoArtefatoVO> consultarPorTCC(int tcc) throws Exception;

}
