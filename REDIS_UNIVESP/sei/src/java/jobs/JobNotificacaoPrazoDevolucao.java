package jobs;

import java.util.HashMap;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.EmprestimoVO;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class JobNotificacaoPrazoDevolucao extends SuperFacadeJDBC implements Runnable {

	private static final long serialVersionUID = 1L;

	@Override
	public void run() {
		try {
			ConfiguracaoGeralSistemaVO conGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			HashMap<String, ConfiguracaoBibliotecaVO> mapConfiguracaoBiblioteca = new HashMap<String, ConfiguracaoBibliotecaVO>(0);
			ConfiguracaoBibliotecaVO conBibliotecaVO = null;
			List<EmprestimoVO> emprestimoVOs = getFacadeFactory().getEmprestimoFacade().consultarEmprestimoNotificacaoPrazoDevolucao();
			for (EmprestimoVO emprestimoVO : emprestimoVOs) {
				String keyMapConfiguracaoBiblioteca = emprestimoVO.getBiblioteca().getCodigo().toString() + emprestimoVO.getUnidadeEnsinoVO().getCodigo().toString();
				if (mapConfiguracaoBiblioteca.containsKey(keyMapConfiguracaoBiblioteca)) {
					conBibliotecaVO = mapConfiguracaoBiblioteca.get(keyMapConfiguracaoBiblioteca);
				} else {
					conBibliotecaVO = getFacadeFactory().getConfiguracaoBibliotecaFacade().executarObterConfiguracaoBibliotecaComBaseTipoPessoa(emprestimoVO.getBiblioteca().getCodigo(), emprestimoVO.getTipoPessoa(), emprestimoVO.getMatricula().getMatricula(), new UsuarioVO());
					mapConfiguracaoBiblioteca.put(keyMapConfiguracaoBiblioteca, conBibliotecaVO);
				}
				if (!conBibliotecaVO.getQuantidadeDiasAntesNotificarPrazoDevolucao().equals(0)) {
					getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executaEnvioMensagemNotificacaoPrazoDevolucao(emprestimoVO.getItemEmprestimoVOs(), emprestimoVO.getPessoa(), emprestimoVO.getTipoPessoa(), emprestimoVO.getBiblioteca().getNome(), new UsuarioVO(), conGeralSistemaVO, conBibliotecaVO.getFuncionarioPadraoEnvioMensagem().getPessoa(), emprestimoVO.getUnidadeEnsinoVO().getCodigo());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
