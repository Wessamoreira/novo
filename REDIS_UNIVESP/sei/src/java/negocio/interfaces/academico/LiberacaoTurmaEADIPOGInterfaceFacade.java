package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.LiberacaoTurmaEADIPOGVO;
import negocio.comuns.academico.TurmaEADIPOGVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface LiberacaoTurmaEADIPOGInterfaceFacade {


	public void incluir(final TurmaEADIPOGVO turmaEAD, UsuarioVO usuario) throws Exception;

    public void excluir(TurmaEADIPOGVO turmaEADVO ,ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

	public TurmaEADIPOGVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);
	
	public List<LiberacaoTurmaEADIPOGVO> consultarTurmas() throws Exception;
	
	public List<TurmaEADIPOGVO> consultarTurmasPorModulo(Integer modulo) throws Exception;

	public TurmaEADIPOGVO consultarPorTurma(String matricula, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}