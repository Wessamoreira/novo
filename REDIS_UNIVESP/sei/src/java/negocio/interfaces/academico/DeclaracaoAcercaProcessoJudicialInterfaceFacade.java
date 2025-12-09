package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.DeclaracaoAcercaProcessoJudicialVO;
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
public interface DeclaracaoAcercaProcessoJudicialInterfaceFacade {

	public void incluir(DeclaracaoAcercaProcessoJudicialVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(DeclaracaoAcercaProcessoJudicialVO obj, UsuarioVO usuario) throws Exception;

	public void excluir(List<DeclaracaoAcercaProcessoJudicialVO> declaracaoAcercaProcessoJudicialVOs, UsuarioVO usuario) throws Exception;
	
	public void excluirPorExpedicaoDiploma(Integer codigoExpedicao, UsuarioVO usuario) throws Exception;

	public List<DeclaracaoAcercaProcessoJudicialVO> consultar(Integer codigoExpedicao) throws Exception;

	public void persistir(List<DeclaracaoAcercaProcessoJudicialVO> declaracaoAcercaProcessoJudicialVOs, UsuarioVO usuario) throws Exception;

}
