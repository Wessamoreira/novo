package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.RegraEmissaoUnidadeEnsinoVO;
import negocio.comuns.academico.RegraEmissaoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface RegraEmissaoInterfaceFacade<T extends SuperVO> extends SuperFacadeInterface<T>{
	
	public void persistir(List<RegraEmissaoVO> lista, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	public List<RegraEmissaoVO> adicionarRegraEmissaoVOs(List<RegraEmissaoVO> lista, RegraEmissaoVO regraEmissaoVO) throws Exception;
	public void adicionarUnidadeEnsino(RegraEmissaoUnidadeEnsinoVO rue, RegraEmissaoVO re) throws Exception;
	public void removerRegraEmissao(RegraEmissaoUnidadeEnsinoVO rue, RegraEmissaoVO re) throws Exception;
	public List<RegraEmissaoVO> consultarTodasRegrasEmissao(UsuarioVO usuario) throws Exception;
	
}
