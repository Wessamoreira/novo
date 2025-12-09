package negocio.interfaces.financeiro;

import java.util.List;
import java.util.Map;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.PlanoDescontoContaReceberVO;
import relatorio.negocio.comuns.financeiro.BoletoBancarioRelVO;

public interface PlanoDescontoContaReceberInterfaceFacade {

	
	public void incluir(PlanoDescontoContaReceberVO obj, UsuarioVO usuario) throws Exception;

    public void incluir(PlanoDescontoContaReceberVO obj, UsuarioVO usuario, boolean verificarPermissao) throws Exception;
        
	
	public void alterar(PlanoDescontoContaReceberVO obj, UsuarioVO usuario) throws Exception;

	
	public void excluir(PlanoDescontoContaReceberVO obj, UsuarioVO usuario) throws Exception;

	
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	
	public void excluirItensPlanoDescontoContaReceber(Integer provisaoCusto, UsuarioVO usuario) throws Exception;

	
	public void alterarItensPlanoDescontoContaReceber(Integer provisaoCusto, List objetos, UsuarioVO usuario) throws Exception;

	
	public void incluirItensPlanoDescontoContaReceber(Integer provisaoCustoPrm, List objetos, UsuarioVO usuario) throws Exception;
        
    public void incluirItensPlanoDescontoContaReceber(Integer contaReceberPrm, List objetos, UsuarioVO usuario, boolean verificarPermissao) throws Exception;
	
	public PlanoDescontoContaReceberVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	
	public void setIdEntidade(String idEntidade);

    public List consultarPorContaReceber(Integer contaReceber, boolean b, int nivelMontarDados, UsuarioVO usuario) throws Exception;


	public void modificarValorUtilizadoRecebimentoDosPlanos(Integer codigoConta, Double valor) throws Exception;
    
	public List consultarPorPlanoDesconto(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;        
    
    public Map<Integer, List<PlanoDescontoContaReceberVO>> consultarPlanoDescontoContaRecberParaGeracaoBoleto(List<BoletoBancarioRelVO> boletoBancarioRelVOs) throws Exception;

	List<PlanoDescontoContaReceberVO> consultarPlanoDescontoContaReceberParaGeracaoRemessa(Integer codigoContaReceber) throws Exception;

	List<PlanoDescontoContaReceberVO> consultarPorCodigoOrigemDaContaReceberNegociada(Integer contaReceber, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}