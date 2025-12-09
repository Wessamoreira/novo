package negocio.interfaces.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.ExtratoMapaLancamentoFuturoVO;
import negocio.comuns.utilitarias.dominios.TipoMapaLancamentoFuturo;

public interface ExtratoMapaLancamentoFuturoInterfaceFacade {
	
	public void incluir(final ExtratoMapaLancamentoFuturoVO obj, UsuarioVO usuario) throws Exception;
	public void alterarDataFimApresentacaoExtratoPorCodigoChequeSituacao(final Integer cheque, final Date data, final String tipoMapaLancamentoFuturo, UsuarioVO usuario) throws Exception;
	public void excluirPorCodigoCheque(Integer cheque, Boolean verificarAcesso, UsuarioVO usuario) throws Exception;
	public ExtratoMapaLancamentoFuturoVO realizarCriacaoExtratoMapaLancamentoFuturo(ChequeVO chequeVO, Integer contaCorrente, TipoMapaLancamentoFuturo tipoMapaLancamentoFuturo, UsuarioVO usuarioVO);
	public void realizarCriacaoInclusaoExtratoMapaLancamentoFuturo(ChequeVO chequeVO, Date data, Integer contaCorrente, TipoMapaLancamentoFuturo tipoMapaLancamentoFuturo, UsuarioVO usuarioVO) throws Exception;
	public void realizarCriacaoInclusaoExtratoMapaLancamentoFuturoDataReapresentacao(ChequeVO chequeVO, Date dataReapresentacao, Integer contaCorrente, TipoMapaLancamentoFuturo tipoMapaLancamentoFuturo, UsuarioVO usuarioVO) throws Exception;
	public void alterarDataFimApresentacaoSituacaoExtratoPorCodigoChequeSituacao(final Integer cheque, final Date data, final String tipoMapaLancamentoFuturo, final Boolean chequeCompensado, UsuarioVO usuario) throws Exception;

}
