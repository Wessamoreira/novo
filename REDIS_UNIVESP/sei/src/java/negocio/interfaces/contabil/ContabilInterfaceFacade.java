package negocio.interfaces.contabil;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.ContabilVO;
import negocio.comuns.contabil.PlanoContaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface ContabilInterfaceFacade {
	

    public ContabilVO novo() throws Exception;
    public void incluir(ContabilVO obj) throws Exception;
    public void excluir(ContabilVO obj) throws Exception;
    public void gravarComContraPartida(ContabilVO contabil, PlanoContaVO contaDebito, PlanoContaVO contaCredito, PlanoContaVO contaContraPartida, Boolean pagamento,UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiro) throws Exception;
    public List consultar(String campoConsulta, String valorConsulta, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception;
    public ContabilVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception;
    public Integer obterValorNumeroRegistroUltimo() throws Exception;
    public Integer obterMenorAno() throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception;
    public List consultarPorNumeroRegistro(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception;
    public List consultarPorIdentificadorPlanoContaPlanoConta(String valorConsulta, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception;
    public List consultarPorNumeroDocumento(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception;
    public List consultarPorSinal(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception;
    public List consultarPorNomeCliente(String valorConsulta, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception;
    public List consultarPorRequisicaoPagamento(Integer valorConsulta, int nivelMontarDados, boolean verificarPermissao, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception;
    public List consultarPorNomeFornecedor(String valorConsulta, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception;
    public Double consultarSaldoAnterior(Date dataIni, String sinal, Integer conta) throws Exception;
    public List obterContasNoPeriodo(Date dataIni, Date dataFim, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
    public Double obterTotalValorPorConta(Date dataIni, Date dataFim, Integer conta, String sinal, Integer empresa) throws Exception;
    public Double obterTotalValorPorIdentificadorPlanoContaConsiderandoFilhas(Date dataIni, Date dataFim, String identificadorPlanoConta, String sinal, Integer unidadeEnsino) throws Exception;
    public List consultarPorPlanoContaContraPartida(Integer conta, Integer contraPartida, Date dataIni, Date dataFim, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception;
     public List consultarPorContaAnoMesSinal(Integer conta, Integer ano, Integer mes, String sinal,boolean contralarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro,UsuarioVO usuario) throws Exception;
}