package negocio.interfaces.financeiro;
import java.util.Date;
import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ContratosDespesasVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface ContratosDespesasInterfaceFacade {
	

    public ContratosDespesasVO novo() throws Exception;
    public void incluir(ContratosDespesasVO obj, UsuarioVO usuario) throws Exception;
    public void alterar(ContratosDespesasVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(ContratosDespesasVO obj, UsuarioVO usuario) throws Exception;
    public ContratosDespesasVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void verificarPermissaoAutorizarIndeferir(ContratosDespesasVO obj, String situacao, UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorNomeFornecedor(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorTipoContrato(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorDataInicio(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorDataTermino(Date prmIni, Date prmFim,Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorSituacao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorIdentificadorPlanoContaPlanoConta(String valorConsulta,Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorIdentificadorCentroDespesaCentroDespesa(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public List consultarPorNomeFavorecido(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	void realizarGeracaoContratoDespesaEspecificao(ContratosDespesasVO obj) throws Exception;
	void alterarValorDasParcelasPendentes(ContratosDespesasVO contratosDespesasVO, Double valorNovaParcela, UsuarioVO usuarioVO) throws Exception;
	void realizarDefinicaoContaPagarExcluir(ContratosDespesasVO obj, List<ContaPagarVO> contaPagarVOs);
	void realizarCancelamentoContrato(ContratosDespesasVO obj, List<ContaPagarVO> contaPagarVOs, UsuarioVO usuario) throws Exception;
	void consultar(DataModelo dataModelo, String situacaoContrato, String tipoContrato, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
}