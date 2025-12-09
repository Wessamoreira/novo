package negocio.interfaces.financeiro;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ProvisaoCustoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface ProvisaoCustoInterfaceFacade {
	

    public ProvisaoCustoVO novo() throws Exception;
    public void incluir(ProvisaoCustoVO obj, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;
    public void alterar(ProvisaoCustoVO obj, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;
    public void excluir(ProvisaoCustoVO obj, UsuarioVO usuarioVO) throws Exception;
    public ProvisaoCustoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados , UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, String situacao, Date dataInicio, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorNomeUsuario(String valorConsulta, String situacao, Date dataInicio, Date dataFim, int nivelMontarDados , UsuarioVO usuario) throws Exception;
    public List consultarPorCodigoFuncionario(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorNomeFuncionario(String valorConsulta, String situacao, Date dataInicio, Date dataFim, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorDataPrestacaoConta(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public ProvisaoCustoVO consultarPorMapaLancamentoFuturo(Integer codigoMapa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}