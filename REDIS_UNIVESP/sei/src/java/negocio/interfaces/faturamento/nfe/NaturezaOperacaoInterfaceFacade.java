package negocio.interfaces.faturamento.nfe;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.NaturezaOperacaoVO;
import negocio.comuns.faturamento.nfe.enumeradores.TipoNaturezaOperacaoEnum;

public interface NaturezaOperacaoInterfaceFacade {

	void persistir(NaturezaOperacaoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(NaturezaOperacaoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	List<NaturezaOperacaoVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<NaturezaOperacaoVO> consultaRapidaPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	NaturezaOperacaoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<NaturezaOperacaoVO> consultaRapidaPorTipoNaturezaOperacaoEnum(TipoNaturezaOperacaoEnum tipoNaturezaOperacaoEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<NaturezaOperacaoVO> consultaRapidaPorCodigoNaturezaOperacao(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public NaturezaOperacaoVO consultarPorCodigoNaturezaOperacao(Integer codigoNaturezaOperacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}