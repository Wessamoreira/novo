package negocio.interfaces.recursoshumanos;

import java.util.List;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.CompetenciaPeriodoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.ControleLancamentoFolhapagamentoVO;
import negocio.comuns.recursoshumanos.LancamentoFolhaPagamentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface ControleLancamentoFolhapagamentoInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {

	public void excluirPorLancamento(LancamentoFolhaPagamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public boolean consultarDecimoTerceiroJaLancado(FuncionarioCargoVO funcionarioCargoVO, Integer anoCompetencia);

	public boolean validarDecimoTerceiroSegundaParcelaLancada(FuncionarioCargoVO funcionarioCargo, Integer anoCompetencia);

	public ControleLancamentoFolhapagamentoVO consultarControlePrimeiraParcela13(FuncionarioCargoVO funcionarioCargo, Integer anoCompetencia);

	public ControleLancamentoFolhapagamentoVO consultarPorContraCheque(ContraChequeVO contraChequeVO) throws Exception;

	public void cancelarControleLancamentoFolhaPagamento(LancamentoFolhaPagamentoVO lancamentoFolhaPagamento, UsuarioVO usuarioLogado);

	public ControleLancamentoFolhapagamentoVO consultarPorFuncionarioCargoCompetenciaEPeriodo(FuncionarioCargoVO funcionarioCargo, CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, CompetenciaPeriodoFolhaPagamentoVO periodo);

	public Boolean validarRescisaoNaoFoiLancada(FuncionarioCargoVO funcionarioCargo, Integer anoCompetencia);
	
	public List<ControleLancamentoFolhapagamentoVO> consultarPorContraCheque(Integer codigoContraCheque) throws Exception;

	public void atualizarContaPagarDoContraCheque(ControleLancamentoFolhapagamentoVO controleLancamentoFolhapagamentoVO, UsuarioVO usuarioLogado) throws ConsistirException;
	
	public void cancelarContaPagarPorCodigo(int codigoContaPagar, UsuarioVO usuarioLogado) throws ConsistirException;

}