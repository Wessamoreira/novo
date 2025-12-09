package negocio.interfaces.avaliacaoinst;

import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalPresencialRespostaVO;
import negocio.comuns.processosel.RespostaPerguntaVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface AvaliacaoInstitucionalPresencialItemRespostaInterfaceFacade {

    public AvaliacaoInstitucionalPresencialRespostaVO novo() throws Exception;

    public void incluir(final AvaliacaoInstitucionalPresencialRespostaVO obj, final RespostaPerguntaVO respostaPergunta) throws Exception;

    public void alterar(final AvaliacaoInstitucionalPresencialRespostaVO obj, final RespostaPerguntaVO respostaPergunta) throws Exception;

    public void excluirPorAvaliacaoInstitucionalPresencialResposta(AvaliacaoInstitucionalPresencialRespostaVO obj) throws Exception;

    public void setIdEntidade(String aIdEntidade);
}
