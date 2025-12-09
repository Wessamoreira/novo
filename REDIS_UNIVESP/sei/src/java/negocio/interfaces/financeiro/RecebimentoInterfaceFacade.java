package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.RecebimentoVO;
import relatorio.negocio.comuns.financeiro.ComprovanteRecebimentoRelVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface RecebimentoInterfaceFacade {

    public RecebimentoVO novo() throws Exception;

    public void incluir(RecebimentoVO obj, UsuarioVO usuario,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

    public void alterar(RecebimentoVO obj,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    public void excluir(RecebimentoVO obj, UsuarioVO usuario) throws Exception;

    public RecebimentoVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List consultarPorNrDocumento(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados ,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List consultarPorIdentificadorCentroReceitaCentroReceita(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados ,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigoBarra(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados ,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List consultarPorCandidato(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados ,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List consultarPorAluno(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados ,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List consultarPorFuncionario(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados ,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigoContaReceber(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados ,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

	Date consultarDataVencimentoPrevistaCartaoCredito(
			Integer codigoContaReceber,
			Integer codigoFormaPagamentoNegociacaoRecebimento) throws Exception;

	List<FormaPagamentoNegociacaoRecebimentoVO> consultarFormaPagamentoNegociacaoRecebimentoCartaoRelatorio(
			FormaPagamentoNegociacaoRecebimentoVO formaPgto,
			ComprovanteRecebimentoRelVO comprovante) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 4 de jul de 2016 
	 * @param formaPgto
	 * @param comprovante
	 * @return
	 * @throws Exception 
	 */
	List<FormaPagamentoNegociacaoRecebimentoVO> consultarFormaPagamentoNegociacaoRecebimentoCartaoRelatorioDCC(FormaPagamentoNegociacaoRecebimentoVO formaPgto, ComprovanteRecebimentoRelVO comprovante) throws Exception;
}
