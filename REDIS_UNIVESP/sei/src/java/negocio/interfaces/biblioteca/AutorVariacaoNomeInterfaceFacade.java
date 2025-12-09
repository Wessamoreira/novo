package negocio.interfaces.biblioteca;

import java.util.List;

import negocio.comuns.biblioteca.AutorVariacaoNomeVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em especial com a classe Façade). Com a
 * utilização desta interface é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes
 * VO).
 */
public interface AutorVariacaoNomeInterfaceFacade {

    AutorVariacaoNomeVO novo() throws Exception;

    void incluir(AutorVariacaoNomeVO obj) throws Exception;

    void alterar(AutorVariacaoNomeVO obj) throws Exception;

    void excluir(AutorVariacaoNomeVO obj) throws Exception;

    List<AutorVariacaoNomeVO> consultarPorVariacaoNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados) throws Exception;

    List<AutorVariacaoNomeVO> consultarPorAutor(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados) throws Exception;

    AutorVariacaoNomeVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados) throws Exception;

    void validarDados(AutorVariacaoNomeVO obj) throws ConsistirException;

}