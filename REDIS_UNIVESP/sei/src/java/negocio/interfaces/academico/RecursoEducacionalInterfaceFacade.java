package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.RecursoEducacionalVO;
import negocio.comuns.academico.enumeradores.SituacaoRecursoEducacionalEnum;
import negocio.comuns.academico.enumeradores.TipoRecursoEducacionalEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;


public interface RecursoEducacionalInterfaceFacade {

    void persistir(RecursoEducacionalVO recursoEducacionalVO, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    void validarDados(RecursoEducacionalVO recursoEducacionalVO) throws ConsistirException;

    List<RecursoEducacionalVO> consultarRecursoEducacional(String titulo, TipoRecursoEducacionalEnum tipoRecursoEducacional, SituacaoRecursoEducacionalEnum situacaoRecursoEducacional, Integer disciplina, String nomeDisciplina, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario, Integer limit, Integer offset) throws Exception;

    Integer consultarTotalRecursoEducacional(String titulo, TipoRecursoEducacionalEnum tipoRecursoEducacional, SituacaoRecursoEducacionalEnum situacaoRecursoEducacional, Integer disciplina) throws Exception;

    RecursoEducacionalVO consultarRecursoEducacionalPorConteudoUnidadePaginaRecursoEducacional(Integer conteudoUnidadePaginaRecursoEducacional, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    RecursoEducacionalVO consultarRecursoEducacionalPorConteudoUnidadePagina(Integer conteudoUnidadePagina, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

}
