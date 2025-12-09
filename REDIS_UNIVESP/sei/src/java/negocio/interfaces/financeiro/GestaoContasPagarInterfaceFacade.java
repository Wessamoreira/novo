package negocio.interfaces.financeiro;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.GestaoContasPagarVO;
import negocio.comuns.utilitarias.ProgressBarVO;

public interface GestaoContasPagarInterfaceFacade {

	List<ContaPagarVO> consultar(GestaoContasPagarVO obj, boolean controlarAcesso, UsuarioVO usuairo) throws Exception ;

	void persitir(GestaoContasPagarVO obj, List<ContaPagarVO> lista, ProgressBarVO progressBarVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void executarAplicacaoDaAlteracaoDataVencimento(GestaoContasPagarVO obj, List<ContaPagarVO> lista, UsuarioVO usuario);

	void executarAplicacaoDaAlteracaoDataOperacao(GestaoContasPagarVO obj, List<ContaPagarVO> lista, UsuarioVO usuario);
	
	void executarAplicacaoDaAlteracaoValor(GestaoContasPagarVO obj, List<ContaPagarVO> lista, UsuarioVO usuario);

	void consultar(DataModelo dataModelo, GestaoContasPagarVO obj);

	void incluir(GestaoContasPagarVO obj, boolean verificarAcesso, UsuarioVO usuario);

}
