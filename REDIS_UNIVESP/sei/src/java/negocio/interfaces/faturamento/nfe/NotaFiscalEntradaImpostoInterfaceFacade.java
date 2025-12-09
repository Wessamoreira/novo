package negocio.interfaces.faturamento.nfe;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaImpostoVO;

public interface NotaFiscalEntradaImpostoInterfaceFacade {

	void persistir(List<NotaFiscalEntradaImpostoVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) ;

	void excluir(NotaFiscalEntradaImpostoVO obj, boolean verificarAcesso, UsuarioVO usuario) ;

	List<NotaFiscalEntradaImpostoVO> consultaRapidaPorImposto(Integer compra, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) ;

	List<NotaFiscalEntradaImpostoVO> consultaRapidaPorNotaFiscalEntrada(Integer notaFiscalEntrada, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) ;

	NotaFiscalEntradaImpostoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) ;

}