package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ControleCobrancaPagarVO;

public interface ContaPagarHistoricoInterfaceFacade {

	void criarContaPagarHistoricoPorBaixaAutomaticas(ContaPagarVO obj, ControleCobrancaPagarVO controleCobrancaPagar, UsuarioVO usuario) throws Exception;

	List consultarPorCodigoControleCobrancaPagar(Integer controleCobrancaPagar, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}
