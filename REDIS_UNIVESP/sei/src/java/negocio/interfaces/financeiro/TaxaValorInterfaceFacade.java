package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.TaxaVO;
import negocio.comuns.financeiro.TaxaValorVO;
import negocio.comuns.utilitarias.ConsistirException;


public interface TaxaValorInterfaceFacade {

	public void incluir(TaxaValorVO obj, UsuarioVO usuarioVO) throws Exception;

	public void excluirTaxaValorVOs(TaxaVO taxaVO) throws Exception;

	public void excluirTaxaValor(TaxaValorVO obj, UsuarioVO usuarioVO) throws Exception;

	public List consultarOpcoesTaxaValor(Integer taxa, int nivelMontarDados) throws Exception;

	public void removerTaxaValorVOs(TaxaVO taxaVO, TaxaValorVO taxaValorVO);

	public void incluirTaxaValor(TaxaVO taxaVO, UsuarioVO usuarioVO) throws Exception;

	public void alterarOpcoesTaxaValor(TaxaVO taxaVO, UsuarioVO usuarioVO) throws Exception;

	public void validarDadosTaxaValor(TaxaValorVO taxaValorVO) throws ConsistirException;

}