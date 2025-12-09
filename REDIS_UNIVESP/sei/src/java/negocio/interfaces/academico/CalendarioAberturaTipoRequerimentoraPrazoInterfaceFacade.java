package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CalendarioAberturaRequerimentoVO;
import negocio.comuns.academico.CalendarioAberturaTipoRequerimentoraPrazoVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 *
 */
public interface CalendarioAberturaTipoRequerimentoraPrazoInterfaceFacade {

		public void incluir(Integer calendarioAbertura, List<CalendarioAberturaTipoRequerimentoraPrazoVO> calendarioAberturaTipoRequerimentoraPrazoVOs, UsuarioVO usuarioVO) throws Exception;
		public void alterar(Integer calendarioAbertura, List<CalendarioAberturaTipoRequerimentoraPrazoVO> calendarioAberturaTipoRequerimentoraPrazoVOs, UsuarioVO usuarioVO) throws Exception;
	  /* public void excluir(CalendarioAberturaRequerimentoVO obj, UsuarioVO usuarioVO) throws Exception;
	    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	    public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)  throws Exception;*/
		void consultarPorCalendarioAberturaRequerimento(CalendarioAberturaRequerimentoVO calendarioAberturaRequerimentoVO, UsuarioVO usuarioVO) throws Exception;
}
