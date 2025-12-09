package negocio.interfaces.contabil;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.IntegracaoContabilVO;
import negocio.comuns.contabil.LayoutIntegracaoTagVO;
import negocio.comuns.contabil.LayoutIntegracaoVO;

public interface LayoutIntegracaoInterfaceFacade {

	void persistir(LayoutIntegracaoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(LayoutIntegracaoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	List<LayoutIntegracaoVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<LayoutIntegracaoVO> consultaRapidaPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	LayoutIntegracaoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void addLayoutIntegracaoTag(LayoutIntegracaoVO obj, LayoutIntegracaoTagVO tag, UsuarioVO usuario) throws Exception;

	void removeLayoutIntegracaoTag(LayoutIntegracaoVO obj, LayoutIntegracaoTagVO tag, UsuarioVO usuario) throws Exception;

	void alterarPosicaoLayoutIntegracaoTag(LayoutIntegracaoVO obj, LayoutIntegracaoTagVO tag, boolean posicaoAcima, UsuarioVO usuario) throws Exception;

	void gerarLayoutXmlParaIntegracaoContabil(IntegracaoContabilVO obj, LayoutIntegracaoVO layoutIntegracaoXmlVO, UsuarioVO usuario) throws Exception;

	void gerarLayoutTxtParaIntegracaoContabil(IntegracaoContabilVO obj, LayoutIntegracaoVO layoutIntegracaoXmlVO, UsuarioVO usuario) throws Exception;

}
