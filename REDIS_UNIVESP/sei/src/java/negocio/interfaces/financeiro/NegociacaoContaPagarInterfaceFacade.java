package negocio.interfaces.financeiro;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaPagarNegociadoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.NegociacaoContaPagarVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface NegociacaoContaPagarInterfaceFacade {
	
    public void incluir(NegociacaoContaPagarVO obj, Boolean verificarPermissao, UsuarioVO usuario) throws Exception;    
    public void excluir(NegociacaoContaPagarVO obj, Boolean verificarPermissao, UsuarioVO usuario) throws Exception;
    public NegociacaoContaPagarVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);       
    public Double calcularValorTotalConfirmacaoNegociacao(NegociacaoContaPagarVO obj) throws Exception ;
    public void validarDadosCalculoValorTotalConfirmacaoNegociacao(NegociacaoContaPagarVO obj) throws Exception;
    List<NegociacaoContaPagarVO> consultar(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    Integer consultarTotalRegistro(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino) throws Exception;
	Double realizarCalculoValorBaseParcela(NegociacaoContaPagarVO negociacaoContaPagarVO) throws Exception;
	void gerarParcelas(NegociacaoContaPagarVO obj, UsuarioVO usuarioVO) throws Exception;		
	void validarDados(NegociacaoContaPagarVO obj) throws ConsistirException;
	void validarDadosBasicos(NegociacaoContaPagarVO obj) throws ConsistirException;
	void adicionarObjContaPagarNegociadoVOs(NegociacaoContaPagarVO negociacaoContaPagarVO, ContaPagarVO contaPagarVO, UsuarioVO usuario) throws Exception;
	void removerObjContaPagarNegociadoVOs(NegociacaoContaPagarVO negociacaoContaPagarVO, ContaPagarNegociadoVO obj);
	void calcularValorTotal(NegociacaoContaPagarVO negociacaoContaPagarVO);
	void alterarJustificativa(NegociacaoContaPagarVO obj, Boolean verificarPermissao, UsuarioVO usuario) throws Exception;
	void realizarGeracaoLancamentoContabilJuroMultaDesconto(NegociacaoContaPagarVO negociacaoContaPagarVO, Boolean forcarRecarregamentoConfiguracaoContabil, UsuarioVO usuarioVO) throws Exception;
	
}