package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.MapaDeTurmasEncerradasRelVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
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
public interface MapaDeTurmasEncerradasRelInterfaceFacade {

	List<MapaDeTurmasEncerradasRelVO> consultar(UnidadeEnsinoVO unidadeEnsinoVO, boolean controlarAcesso,int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<MapaDeTurmasEncerradasRelVO> consultarPorSituacaoMatriculaCodigoDaTurma(UnidadeEnsinoVO unidadeEnsinoVO,MapaDeTurmasEncerradasRelVO mapaDeTurmasEncerradasRelVO,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)throws Exception;

  
}
