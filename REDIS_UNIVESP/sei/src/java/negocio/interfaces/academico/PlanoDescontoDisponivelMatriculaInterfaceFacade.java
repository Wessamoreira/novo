package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.PlanoDescontoDisponivelMatriculaVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface PlanoDescontoDisponivelMatriculaInterfaceFacade {

    public PlanoDescontoDisponivelMatriculaVO novo() throws Exception;

    public void incluir(PlanoDescontoDisponivelMatriculaVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(PlanoDescontoDisponivelMatriculaVO obj, UsuarioVO usuario) throws Exception;

    public void excluir(PlanoDescontoDisponivelMatriculaVO obj, UsuarioVO usuario) throws Exception;

    public PlanoDescontoDisponivelMatriculaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PlanoDescontoDisponivelMatriculaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public List<PlanoDescontoDisponivelMatriculaVO> consultarPorUnidadeEnsinoDisponivelMatricula(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<PlanoDescontoDisponivelMatriculaVO> consultarPorCodigoPlanoDescontoDisponivelMatricula(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public List<PlanoDescontoVO> consultarPlanoDescontoPorCodigoPlanoDescontoDisponivelMatricula(MatriculaPeriodoVO matriculaPeriodoVO, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public List<PlanoDescontoDisponivelMatriculaVO> consultarPorUnidadeEnsinoDisponivelMatricula_Turma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}
