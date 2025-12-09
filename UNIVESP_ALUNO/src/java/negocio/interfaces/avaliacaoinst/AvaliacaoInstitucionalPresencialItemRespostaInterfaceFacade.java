package negocio.interfaces.avaliacaoinst;

import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalPresencialRespostaVO;
import negocio.comuns.processosel.RespostaPerguntaVO;

/**
 * Interface reponsvel por criar uma estrutura padro de comunidao entre a camada de controle e camada de negcio (em
 * especial com a classe Faade). Com a utilizao desta interface  possvel substituir tecnologias de uma camada da
 * aplicao com mnimo de impacto nas demais. Alm de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negcio, por intermdio de sua classe Faade (responsvel por persistir os dados das classes VO).
 */
public interface AvaliacaoInstitucionalPresencialItemRespostaInterfaceFacade {

    public AvaliacaoInstitucionalPresencialRespostaVO novo() throws Exception;

    public void incluir(final AvaliacaoInstitucionalPresencialRespostaVO obj, final RespostaPerguntaVO respostaPergunta) throws Exception;

    public void alterar(final AvaliacaoInstitucionalPresencialRespostaVO obj, final RespostaPerguntaVO respostaPergunta) throws Exception;

    public void excluirPorAvaliacaoInstitucionalPresencialResposta(AvaliacaoInstitucionalPresencialRespostaVO obj) throws Exception;

    public void setIdEntidade(String aIdEntidade);
}
