package negocio.interfaces.arquitetura;

import java.util.List;

import negocio.comuns.arquitetura.NovidadeSeiVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.TipoNovidadeEnum;


public interface NovidadeSeiInterfaceFacade {

    List<NovidadeSeiVO> consultarNovidades(Integer limit, Integer pagina, boolean trazerApenasNaoVisualizadas, TipoNovidadeEnum tipoNovidadeEnum, String palavraChave, boolean trazerApenasDestaque, UsuarioVO usuarioVO);
    
    Integer consultarTotalRegistroNovidades(boolean trazerApenasNaoVisualizadas, TipoNovidadeEnum tipoNovidadeEnum, String palavraChave, UsuarioVO usuarioVO);
    
    Boolean realizarValidacaoNovidadeSemVisualizacaoUsuario(Integer usuario);
    
    NovidadeSeiVO consultarNovidadeUsuarioApresentar(UsuarioVO usuario);
    
    void registrarVisualizacao(NovidadeSeiVO novidadeSeiVO, UsuarioVO usuarioVO);
    
    Integer realizarContagemNovidadeSemVisualizacaoUsuario(Integer usuario);

	void persistir(NovidadeSeiVO novidadeSeiVO) throws Exception;

	void excluir(NovidadeSeiVO novidadeSeiVO) throws Exception;
    
}
