package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.SetranspVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface SetranspInterfaceFacade {

	public SetranspVO novo() throws Exception;

	public void incluir(SetranspVO obj, UsuarioVO usuarioVO) throws Exception;

	public void alterar(SetranspVO obj, UsuarioVO usuarioVO) throws Exception;

	public void excluir(SetranspVO obj, UsuarioVO usuarioVO) throws Exception;

	public SetranspVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorDataGeracao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorResponsavel(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public SetranspVO criarObjetoSetranspVO(SetranspVO setranspVO, Integer unidadeEnsino, Integer curso, Integer turma, String aluno, Date dataInicio, Date dataFim, String ano, String semestre, String tipoFiltroPeriodicidade) throws Exception;

	public SetranspVO gerarArquivo(SetranspVO setranspVO, String caminhoPastaArquivo, UsuarioVO usuario) throws Exception;
}