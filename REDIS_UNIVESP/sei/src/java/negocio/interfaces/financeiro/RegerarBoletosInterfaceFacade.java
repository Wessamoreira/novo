package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface RegerarBoletosInterfaceFacade {

	public List consultarContaReceber(int offset, int codUnidadeEnsino, int codUnidadeEnsinoCurso, int codTurma, String ano, String semestre) throws Exception;

	public void regerarBoletos(List<ContaReceberVO> listaContaReceberVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, ControleRemessaContaReceberVO crcr) throws Exception;

        public void gerarDadosBoleto(ContaReceberVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, ControleRemessaContaReceberVO crcr) throws Exception;

        public void alterar(final ContaReceberVO obj) throws Exception;

        public void executarGeracaoNossoNumeroContaReceber(final ContaReceberVO obj) throws Exception;

        public void gravarNumeroDoc(final ContaReceberVO obj) throws Exception;

        public String gerarNumeroDocumento(UnidadeEnsinoVO unidadeEnsino) throws Exception;

        public int getQtdeContas();
}