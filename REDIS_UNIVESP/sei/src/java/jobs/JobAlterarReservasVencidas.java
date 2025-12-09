package jobs;

import java.util.HashMap;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.ReservaVO;
import negocio.comuns.biblioteca.enumeradores.SituacaoReservaEnum;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class JobAlterarReservasVencidas extends SuperFacadeJDBC implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3284372592641289519L;

	
	@Override
	public void run() {
		try {
			HashMap<String, ConfiguracaoBibliotecaVO> mapConfiguracaoBiblioteca = new HashMap<String, ConfiguracaoBibliotecaVO>(0);
			ConfiguracaoBibliotecaVO conBibliotecaVO = null;
			List<ReservaVO> reservaVOs = getFacadeFactory().getReservaFacade().consultarReservasVencidasPorCatalogoPessoa(null, null, null, new UsuarioVO());
			for (ReservaVO reservaVO : reservaVOs) {
				String keyMapConfiguracaoBiblioteca = reservaVO.getBibliotecaVO().getCodigo().toString();
				if (mapConfiguracaoBiblioteca.containsKey(keyMapConfiguracaoBiblioteca)) {
					conBibliotecaVO = mapConfiguracaoBiblioteca.get(keyMapConfiguracaoBiblioteca);
				} else {
					conBibliotecaVO = getFacadeFactory().getConfiguracaoBibliotecaFacade().executarObterConfiguracaoBibliotecaComBaseTipoPessoa(reservaVO.getBibliotecaVO().getCodigo(), reservaVO.getTipoPessoa(), reservaVO.getMatricula(), new UsuarioVO());
					mapConfiguracaoBiblioteca.put(keyMapConfiguracaoBiblioteca, conBibliotecaVO);
				}
				getFacadeFactory().getReservaFacade().alterarSituacaoReserva(reservaVO, SituacaoReservaEnum.FINALIZADO.getKey(), new UsuarioVO());
				if (getFacadeFactory().getReservaFacade().consultarNumeroDeExemplaresDisponiveisPorCatalogo(reservaVO.getCatalogo(), reservaVO.getBibliotecaVO(), conBibliotecaVO, false) > 0) {
					getFacadeFactory().getReservaFacade().executarAlterarDataTerminoReservaDataReservaMaisAntigaPorCatalogoEEnviaMensagemReservaDisponivel(reservaVO.getCatalogo(), reservaVO, conBibliotecaVO, new UsuarioVO());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
