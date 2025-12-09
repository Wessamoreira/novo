package negocio.interfaces;

import java.util.List;


import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.UnidadeConteudoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;


public interface UnidadeConteudoInterfaceFacade {

    void persistir(UnidadeConteudoVO unidadeConteudo, DisciplinaVO disciplinaVO, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    
    List<UnidadeConteudoVO> consultarUnidadeConteudoPorConteudo(Integer conteudo, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    
    void incluirUnidadeConteudo(ConteudoVO conteudoVO, DisciplinaVO disciplinaVO, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    
    void alterarUnidadeConteudo(ConteudoVO conteudoVO, DisciplinaVO disciplinaVO, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    
    void inativarUnidadeConteudo(UnidadeConteudoVO unidadeConteudoVO, Boolean controlarAcesso, UsuarioVO usuario);
    
    UnidadeConteudoVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    void validarDados(UnidadeConteudoVO unidadeConteudoVO) throws ConsistirException;
    
}
