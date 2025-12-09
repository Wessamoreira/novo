package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.MotivoCancelamentoTrancamentoVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface MotivoCancelamentoTrancamentoInterfaceFacade {

    public MotivoCancelamentoTrancamentoVO novo() throws Exception;

    public void incluir(MotivoCancelamentoTrancamentoVO obj, UsuarioVO usuarioVO) throws Exception;

    public void alterar(MotivoCancelamentoTrancamentoVO obj, UsuarioVO usuarioVO) throws Exception;

    public void excluir(MotivoCancelamentoTrancamentoVO obj, UsuarioVO usuarioVO) throws Exception;

    public MotivoCancelamentoTrancamentoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<MotivoCancelamentoTrancamentoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<MotivoCancelamentoTrancamentoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<MotivoCancelamentoTrancamentoVO> consultarPorNome(String valorConsulta, Integer codigoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<MotivoCancelamentoTrancamentoVO> consultarPorNomeAtivo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public void setIdEntidade(String aIdEntidade);

    public MotivoCancelamentoTrancamentoVO consultarPorTipoJustificativa(String tipoJustificativa) throws Exception;

	List<MotivoCancelamentoTrancamentoVO> consultarPorTipoJustificativaSituacao(String tipoJustificativa, String situacao) throws Exception;

}
