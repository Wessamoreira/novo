package relatorio.negocio.interfaces.biblioteca;

import java.util.List;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ExemplarVO;

/**
 *
 * @author Carlos
 */
public interface EtiquetaCodigoBarraRelInterfaceFacade {

    public List<CatalogoVO> consultarCatalogo(String campoConsultarCatalogo, String valorConsultarCatalogo, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
    public List<ExemplarVO> consultarExemplar(String campoConsultarExemplar, String valorConsultarExemplar, Integer unidadeEnsino, UsuarioVO usuarioLogado) throws Exception;
}
