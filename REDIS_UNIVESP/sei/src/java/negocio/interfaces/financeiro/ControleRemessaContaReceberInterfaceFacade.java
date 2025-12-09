package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaVO;

public interface ControleRemessaContaReceberInterfaceFacade {

	public void incluir(final ControleRemessaContaReceberVO obj, final Integer controleRemessa, UsuarioVO usuario) throws Exception;
	
	public void realizarEstorno(ControleRemessaContaReceberVO controleRemessaContaReceberVO, UsuarioVO usuarioVO) throws Exception;
	
	public List<ControleRemessaContaReceberVO> consultaRapidaContasArquivoRemessaPorCodigoControleRemessa(ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;
	
	public Boolean verificaContaReceberRegistrada(ContaReceberVO obj) throws Exception;	
	
	public void realizarRegistroDadosAceite(ControleRemessaContaReceberVO controleRemessaContaReceberVO, UsuarioVO usuarioVO) throws Exception;
	
	public ControleRemessaContaReceberVO consultaRapidaContaArquivoRemessaPorCodigoContaReceber(Integer contaReceber) throws Exception;
	
	public List<ControleRemessaContaReceberVO> consultaRapidaContasArquivoRemessaPorCodigoMatriculaPeriodo(Integer matriculaPeriodo, UsuarioVO usuario) throws Exception;
	
	public void removerVinculoContaReceber(Integer matriculaPeriodo, UsuarioVO usuarioVO) throws Exception;

	public void realizaVinculoContaReceber(Integer contaReceber, String nossoNumero, UsuarioVO usuarioVO) throws Exception;
	
	public void realizaVinculoContaReceberConvenio(Integer matriculaperiodo,UsuarioVO usuarioVO) throws Exception;

	public ControleRemessaContaReceberVO consultaRapidaContaArquivoRemessaPorNossoNumeroContaReceber(List<ContaCorrenteVO> listaContaCorrente,  String nossoNumero) throws Exception;
	
	public void removeVinculoContaReceberPorCodigoContaReceber(Integer contaReceber, UsuarioVO usuarioVO) throws Exception;
	
	public boolean consultarExisteRemessaContaReceber(Integer codContaReceber) throws Exception;

}