package negocio.interfaces.crm;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.segmentacao.ProspectSegmentacaoOpcaoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface ProspectSegmentacaoOpcaoInterfaceFacade {

	public void incluir(ProspectSegmentacaoOpcaoVO obj, UsuarioVO usuario) throws Exception;

	public void incluirProspectSegmentacaoOpcoes(ProspectsVO prospect, UsuarioVO usuario) throws Exception;

	public void alterarProspectsSegmentacaoOpcao(Integer prospects, List<ProspectSegmentacaoOpcaoVO> objetos, UsuarioVO usuario) throws Exception;

}