package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;

public interface CentroResultadoOrigemInterfaceFacade {

	void persistir(List<CentroResultadoOrigemVO> lista, String codOrigem, TipoCentroResultadoOrigemEnum tipoCentroResultadoOrigemEnum, boolean verificarAcesso, UsuarioVO usuarioVO, Boolean permitirGravarContaPagarIsenta)throws Exception;

	CentroResultadoOrigemVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);	

	void excluidoRegistroNaoExistenteListaPorCodOrigemPorTipoCentroResultadoOrigemEnum(List<CentroResultadoOrigemVO> lista, String codOrigem, TipoCentroResultadoOrigemEnum tipoCentroResultadoOrigemEnum, UsuarioVO usuario) throws Exception;

	void preencherDadosPorCategoriaDespesa(CentroResultadoOrigemVO obj, UsuarioVO usuario) throws Exception;	

	void atualizarListasDeCentroResultadoOrigem(List<CentroResultadoOrigemVO> listaExistente, List<CentroResultadoOrigemVO> novaLista);

	void adicionarCentroResultadoOrigemAgrupado(CentroResultadoOrigemVO cro, List<CentroResultadoOrigemVO> lista);

	boolean isRemoverCentroResultadoOrigemAgrupado(CentroResultadoOrigemVO objExistente, List<CentroResultadoOrigemVO> novaLista);

	void atualizarCentroResultadoOrigemAgrupado(CentroResultadoOrigemVO novo, List<CentroResultadoOrigemVO> listaCentroResultadoOrigemVOs, Boolean somarValores);
	
	void adicionarCentroResultadoOrigem(List<CentroResultadoOrigemVO> listaCentroResultadoOrigem, CentroResultadoOrigemVO centroResultadoOrigem, Double valoTotalCalculoPercentual,  boolean validarTotal, boolean validarDados, UsuarioVO usuario);

	void removerCentroResultadoOrigem(List<CentroResultadoOrigemVO> listaCentroResultadoOrigem, CentroResultadoOrigemVO centroResultadoOrigem, UsuarioVO usuario);

	void realizarDistribuicaoValoresCentroResultado(List<CentroResultadoOrigemVO> listaCentroResultadoOrigem, Double valoTotalCalculoPercentual, UsuarioVO usuario);

	List<CentroResultadoOrigemVO> consultaRapidaPorCodOrigemPorTipoCentroResultadoOrigemEnum(String codOrigem, TipoCentroResultadoOrigemEnum tipoCentroResultadoOrigemEnum, int nivelMontarDados, UsuarioVO usuario);

	void persistir(CentroResultadoOrigemVO obj, String codOrigem, TipoCentroResultadoOrigemEnum tipoCentroResultadoOrigemEnum, boolean isValidarCentroResultadoExistente, boolean verificarAcesso, UsuarioVO usuarioVO, Boolean permitirGravarContaPagarIsenta) throws Exception;

	CentroResultadoOrigemVO consultarCentroResultadoOrigemExistenteLista(List<CentroResultadoOrigemVO> listaCentroResultadoOrigem, CentroResultadoOrigemVO centroResultadoOrigem);

	void realizarAgrupamentoCentroResultadoOrigemVOSomandoValor(List<CentroResultadoOrigemVO> centroResultadoOrigemFinalVOs, List<CentroResultadoOrigemVO> centroResultadoOrigemAdicionarVOs, boolean adicionarComoClone) throws Exception;

	void validarDadosTotalizadoresAposAlteracao(List<CentroResultadoOrigemVO> listaCentroResultadoOrigem, Double valoTotalCalculoPercentual, boolean validarComparadoComValorTotal, UsuarioVO usuario);

	void removerCentroResultadoOrigemAgrupado(CentroResultadoOrigemVO objExistente, List<CentroResultadoOrigemVO> novaLista);

	void realizarValidacaoPorcentagem(List<CentroResultadoOrigemVO> listaCentroResultadoOrigem, Double valoTotalCalculoPercentual, UsuarioVO usuario);

	void validarDadosCentroResultadoOrigem(CentroResultadoOrigemVO obj, Double valoTotalCalculoPercentual);

	void geracaoCentroResultadoOrigemPadraoPorContaReceber(ContaReceberVO obj, boolean isAtualizarCentroReceita, UsuarioVO usuario) throws Exception;

	void atualizarCentroResultadoOrigemVOCampoValor(CentroResultadoOrigemVO obj, UsuarioVO usuario) throws Exception;
	
	void atualizarCentroResultadoOrigemVOCampoCentroReceita(final CentroResultadoOrigemVO obj,  UsuarioVO usuario) throws Exception;

	void adicionarCentroResultadoOrigem(List<CentroResultadoOrigemVO> listaCentroResultadoOrigem,
			CentroResultadoOrigemVO centroResultadoOrigem, Double valoTotalCalculoPercentual, boolean validarDados,
			UsuarioVO usuario);	

	public void excluirPorContaPagarETipoOrigem(String CodOrigem, String tipoCentroResultadoOrigemEnum, UsuarioVO usuario);

	void adicionarCentroResultadoOrigemPorRateioCategoriaDespesa(
			List<CentroResultadoOrigemVO> listaCentroResultadoOrigem, CentroResultadoOrigemVO centroResultadoOrigem,
			Double valoTotalCalculoPercentual, boolean validarDados, UsuarioVO usuario) throws Exception;
	
	void alterarUnidadeEnsinoCentroResultadoOrigem (TurmaVO turmaVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
}
