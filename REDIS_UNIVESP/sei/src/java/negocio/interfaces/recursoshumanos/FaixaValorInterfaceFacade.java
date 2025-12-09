package negocio.interfaces.recursoshumanos;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.FaixaValorVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface FaixaValorInterfaceFacade {

	public void persistir(FaixaValorVO faixaValor, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void excluir(FaixaValorVO faixaValor, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	@SuppressWarnings("rawtypes")
	public List consultarPorTabelaReferenciaFolhaPagamento(int codigo, Boolean validarAcesso, UsuarioVO usuarioVO);

	public void validarDadosLimiteInferiorMaiorLimiteSuperior(FaixaValorVO obj) throws ConsistirException;

	public FaixaValorVO consultarPorChavePrimaria(long codigo) throws Exception;
	
	public Integer consultarValorEntreLimiteInferiorELimiteSuperior(Integer valor);
}
