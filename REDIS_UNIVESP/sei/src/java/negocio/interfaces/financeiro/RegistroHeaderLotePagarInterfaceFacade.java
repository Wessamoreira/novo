package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ControleCobrancaPagarVO;
import negocio.comuns.financeiro.RegistroHeaderLotePagarVO;

public interface RegistroHeaderLotePagarInterfaceFacade {	

	void persistir(List<RegistroHeaderLotePagarVO> lista, UsuarioVO usuarioVO) throws Exception;

	void consultarPorControleCobrancaPagar(ControleCobrancaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}
