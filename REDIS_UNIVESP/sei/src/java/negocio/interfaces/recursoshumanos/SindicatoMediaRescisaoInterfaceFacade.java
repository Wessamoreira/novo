package negocio.interfaces.recursoshumanos;

import java.util.List;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.SindicatoMediaRescisaoVO;
import negocio.comuns.recursoshumanos.SindicatoVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoEventoMediaRescisaoEnum;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface SindicatoMediaRescisaoInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<SindicatoMediaRescisaoVO> {

	public void persistirTodos(List<SindicatoMediaRescisaoVO> sindicatoMediaRescisaoVOs, SindicatoVO obj, UsuarioVO usuarioVO) throws Exception;

	List<SindicatoMediaRescisaoVO> consultarPorSindicatoVO(SindicatoVO obj, boolean validarAcesso, UsuarioVO usuario) throws Exception;

	public void consultarEventosDeMediaDeRescisao(FuncionarioCargoVO funcionarioCargo, Integer anoCompetencia, List<EventoFolhaPagamentoVO> listaDeEventos, TipoEventoMediaRescisaoEnum tipoEventoMediaRescisao);

}
