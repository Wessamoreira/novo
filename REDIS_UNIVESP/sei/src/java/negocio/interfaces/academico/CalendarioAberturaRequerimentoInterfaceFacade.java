package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CalendarioAberturaRequerimentoVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 *
 */
public interface CalendarioAberturaRequerimentoInterfaceFacade {

	  	public void incluir(CalendarioAberturaRequerimentoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	    public void alterar(CalendarioAberturaRequerimentoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	    public void excluir(CalendarioAberturaRequerimentoVO obj, UsuarioVO usuarioVO) throws Exception;
	    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	    public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)  throws Exception;
}
