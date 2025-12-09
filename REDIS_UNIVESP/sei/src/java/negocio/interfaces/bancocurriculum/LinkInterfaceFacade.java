package negocio.interfaces.bancocurriculum;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.LinkVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;


public interface LinkInterfaceFacade {    
    
    void persistir(LinkVO linkVO, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
    
    void realizarAtivacao(LinkVO linkVO, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

    public List<LinkVO> consultar(Date dataInicio, Date dataFim, String link, String escopo, StatusAtivoInativoEnum situacao, Integer unidadeEnsino, boolean validarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO conf) throws Exception;
    
    void realizarInativacao(LinkVO linkVO, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

    public LinkVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO conf) throws Exception;

    public List<LinkVO> consultarLinkApresentarVisao(String escopo, StatusAtivoInativoEnum situacao, Integer unidadeEnsino, ConfiguracaoGeralSistemaVO conf) throws Exception;
}
