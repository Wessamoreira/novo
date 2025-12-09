package negocio.interfaces.academico;

import negocio.comuns.academico.ArquivoLogVO;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 *
 * @author Carlos
 */
public interface ArquivoLogInterfaceFacade {
    public void inicializarDadosLogInclusaoArquivo(ArquivoVO arquivo, ArquivoLogVO arquivoLogVO, UsuarioVO responsavelUpload, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
    public void incluir(final ArquivoLogVO obj) throws Exception;
    public void alterarOrigemUpload(Integer arquivo, String origem, UsuarioVO usuario) throws Exception;
}
