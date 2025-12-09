package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.RegraEmissaoUnidadeEnsinoVO;
import negocio.comuns.academico.RegraEmissaoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface RegraEmissaoUnidadeEnsinoInterfaceFacade<T extends SuperVO> extends SuperFacadeInterface<T>{
	
	public void persistir(List<RegraEmissaoUnidadeEnsinoVO> regraEmissaoUnidadeEnsinoVOs, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	public List<RegraEmissaoUnidadeEnsinoVO> consultarRegraEmissaoUnidadeEnsinoPorRegraEmissaoCodigo(Integer codigo) throws Exception;
	public List<RegraEmissaoUnidadeEnsinoVO> consultarRegraEmissaoUnidadePorRegraEmissao(RegraEmissaoVO regraEmissaoVO, boolean validarAcesso,UsuarioVO usuario) throws Exception;
	public void excluirRegraEmissaoUnidadeEnsino(List<RegraEmissaoUnidadeEnsinoVO> emissaoUnidadeEnsinoVOs,boolean validarAcesso,UsuarioVO usuario) throws Exception;
	
}
