package negocio.interfaces.financeiro;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.PixContaCorrenteVO;
import negocio.comuns.financeiro.enumerador.StatusPixEnum;
import webservice.pix.comuns.PixRSVO;

public interface PixContaCorrenteInterfaceFacade {
	
	PixContaCorrenteVO realizarVisualizacaoPix(ContaReceberVO contaReceberVO,  ConfiguracaoFinanceiroVO config, UsuarioVO usuarioVO);
	
	PixContaCorrenteVO realizarGeracaoPix(PixContaCorrenteVO pixVO, ConfiguracaoFinanceiroVO config, UsuarioVO usuario);
	
	void realizarValidacaoTokenPix(ContaCorrenteVO obj);	
	
	void realizarConfiguracaoWebhookPix(ContaCorrenteVO obj);
	
	String realizarConsultaWebhookPix(ContaCorrenteVO obj);
	
	void realizarCancelamentoWebhookPix(ContaCorrenteVO obj);

	void persistir(PixContaCorrenteVO obj, boolean verificarAcesso, UsuarioVO usuarioVO);

	PixContaCorrenteVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);
	
	void realizarProcessamentoJobCancelamentoPix() throws Exception;
	
	void realizarProcessamentoJobWebhookPix() throws Exception;
	
	String consultarJsonPix(PixContaCorrenteVO pixContaCorrenteVO, UsuarioVO usuario) throws Exception;
	
	void realizarCancelamentoPix(PixContaCorrenteVO pixContaCorrenteVO, UsuarioVO usuario) throws Exception;
	
	PixContaCorrenteVO realizarVerificacaoBaixaPixContaCorrente(PixContaCorrenteVO pixContaCorrenteVO, boolean atualizarPix, UsuarioVO usuario) throws Exception;

	void realizarProcessamentoBaixaPix(PixRSVO pixRSVO, Integer contaCorrente, StatusPixEnum statusPixEnumAtual, UsuarioVO usuario) throws Exception;

	void consultar(DataModelo dataModelo, PixContaCorrenteVO obj) throws Exception;

	List<PixContaCorrenteVO> consultarComBancoIntegradoPixSemWebhookPorSituacao(StatusPixEnum statusPixEnumAtual, int nivelMontarDados, UsuarioVO usuario);	

	

}
