package negocio.interfaces.recursoshumanos;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.FaixaValorVO;
import negocio.comuns.recursoshumanos.ValorReferenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.enumeradores.ValorFixoEnum;

public interface ValorReferenciaFolhaPagamentoInterfaceFacade {

	public void persistir(ValorReferenciaFolhaPagamentoVO valorReferenciaFolhaPagamento, List<FaixaValorVO> listaAnteriorFaixaValores, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public void excluir(ValorReferenciaFolhaPagamentoVO valorReferenciaFolhaPagamento, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public List<ValorReferenciaFolhaPagamentoVO> consultarPorFiltro(String campoConsulta, String valorConsulta, String situacao, Date inicioVigencia, Date finalVigencia, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void alterarSituacaoDataFinalVigencia(ValorReferenciaFolhaPagamentoVO valorReferenciaFolhaPagamento,  boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;

	public void clonarVigencia(ValorReferenciaFolhaPagamentoVO valorReferenciaFolhaPagamento, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception;
	
	public BigDecimal consultarValorFixo(String identificador, Date dataVigencia);
	
	public BigDecimal consultarValorFixoPorReferencia(ValorFixoEnum referencia, Date dataVigencia);
	
	public String consultarSql(String identificador, Date dataVigencia);

	public FaixaValorVO consultarFaixaValor(String identificador, Date dataVigencia, BigDecimal valor);
	
	public FaixaValorVO consultarFaixaValor(ValorReferenciaFolhaPagamentoVO valorReferencia, BigDecimal valor) ;
	
	public BigDecimal consultarFaixaPercentual(ValorReferenciaFolhaPagamentoVO valorReferencia, BigDecimal valor) ;
	
	public BigDecimal consultarValorDeducao(ValorReferenciaFolhaPagamentoVO valorReferencia, BigDecimal valor) ;

	public ValorReferenciaFolhaPagamentoVO consultarValorReferenciaPorReferencia(String identificador, Date vigencia);
	
	public FaixaValorVO consultarFaixaValor(String identificador, Date vigencia, int valor);

	public List<ValorReferenciaFolhaPagamentoVO> consultarAtivosFinalVigenciaSelecionada();

	public void alterarFinalVigencia(ValorReferenciaFolhaPagamentoVO obj, UsuarioVO usuario);
}