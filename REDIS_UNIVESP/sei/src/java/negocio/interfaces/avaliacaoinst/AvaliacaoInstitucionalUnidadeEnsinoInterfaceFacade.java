package negocio.interfaces.avaliacaoinst;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalUnidadeEnsinoVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;

import java.util.List;

public interface AvaliacaoInstitucionalUnidadeEnsinoInterfaceFacade {

    public void incluirAvaliacaoInstitucionalUnidadeEnsino(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuarioVO) throws Exception;
    public void alterarAvaliacaoInstitucionalUnidadeEnsino(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuarioVO) throws Exception;
    public void excluirAvaliacaoInstitucionalUnidadeEnsino(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuarioVO) throws Exception;
    public List<AvaliacaoInstitucionalUnidadeEnsinoVO> consultarPorAvaliacaoInstitucional(Integer avaliacaoInstitucional, UsuarioVO usuarioVO) throws Exception;

}
