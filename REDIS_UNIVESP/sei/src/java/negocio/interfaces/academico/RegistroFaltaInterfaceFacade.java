package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.RegistroFaltaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface RegistroFaltaInterfaceFacade {

	public void persistir(RegistroFaltaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void excluir(RegistroFaltaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public List<RegistroFaltaVO> consultar(Integer unidadeEnsino, String matricula, Integer curso, Date dataInicio, Date dataFim, boolean verificarAcesso, UsuarioVO usuarioVO, int nivelMontarDados) throws Exception;

	public RegistroFaltaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, boolean verificarAcesso, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;

}
