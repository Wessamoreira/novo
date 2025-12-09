package negocio.comuns.compras;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Preconditions;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.DepartamentoTramiteCotacaoCompraVO.TipoControleFinanceiroEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

public class CotacaoHistoricoVO extends SuperVO {

	private static final long serialVersionUID = -2478523809320384094L;

	private Integer codigo;
	private Date dataInicio;
	private Date dataFim;
	private boolean retorno;
	private CotacaoVO cotacao;
	private UsuarioVO responsavel;
	private String motivoRetorno;
	private String observacao;
	private DepartamentoTramiteCotacaoCompraVO departamentoTramiteCotacaoCompra;

	private List<MaterialTramiteCotacaoCompraVO> listaMaterialTramiteCotacaoCompra;
	private CotacaoHistoricoVO anteriorHistorico;
	private CotacaoHistoricoVO proximoHistorico;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public CotacaoVO getCotacao() {
		return cotacao;
	}

	public void setCotacao(CotacaoVO cotacao) {
		this.cotacao = cotacao;
	}

	public DepartamentoTramiteCotacaoCompraVO getDepartamentoTramiteCotacaoCompra() {		
		return departamentoTramiteCotacaoCompra;
	}

	public void setDepartamentoTramiteCotacaoCompra(DepartamentoTramiteCotacaoCompraVO departamentoTramiteCotacaoCompra) {
		this.departamentoTramiteCotacaoCompra = departamentoTramiteCotacaoCompra;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataPrevisaoFim() {
		return UteisData.adicionarDiasEmData(getDataInicio(), this.getDepartamentoTramiteCotacaoCompra().getPrazoExecucao());
	}
	
	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	
	public boolean isCampoObservacaoObrigatorio(){
		return getDepartamentoTramiteCotacaoCompra().isObservacaoObrigatoria();
	}
	
	public String getCssCampoObservacao(){
		return isCampoObservacaoObrigatorio() ? "camposObrigatoriosTextArea" : "camposTextArea";
	}

	public String getObservacao() {
		observacao = Optional.ofNullable(observacao).orElse("");
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public UsuarioVO getResponsavel() {
		responsavel = Optional.ofNullable(responsavel).orElse(new UsuarioVO());
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}

	public String getMotivoRetorno() {
		motivoRetorno = Optional.ofNullable(motivoRetorno).orElse("");
		return motivoRetorno;
	}

	public void setMotivoRetorno(String motivoRetorno) {
		this.motivoRetorno = motivoRetorno;
	}
	
	public String getObservacaoOuMotivoRetornoRelatorio() {
		return Uteis.isAtributoPreenchido(getObservacao()) ? getObservacao() : getMotivoRetorno();
	}

	public CotacaoHistoricoVO getAnteriorHistorico() {
		return anteriorHistorico;
	}

	public void setAnteriorHistorico(CotacaoHistoricoVO anteriorHistorico) {
		this.anteriorHistorico = anteriorHistorico;
	}

	public CotacaoHistoricoVO getProximoHistorico() {
		return proximoHistorico;
	}

	public void setProximoHistorico(CotacaoHistoricoVO proximoHistorico) {
		this.proximoHistorico = proximoHistorico;
	}

	public boolean isRetorno() {
		return retorno;
	}

	public void setRetorno(boolean retorno) {
		this.retorno = retorno;
	}

	public List<MaterialTramiteCotacaoCompraVO> getListaMaterialTramiteCotacaoCompra() {
		this.listaMaterialTramiteCotacaoCompra = Optional.ofNullable(this.listaMaterialTramiteCotacaoCompra).orElse(new ArrayList<>());
		return listaMaterialTramiteCotacaoCompra;
	}

	public boolean adicionarMaterialTramiteCotacaoCompra(MaterialTramiteCotacaoCompraVO p) {
		p.setCotacaoHistorico(this);
		return this.getListaMaterialTramiteCotacaoCompra().add(p);
	}

	public boolean removerMaterialTramiteCotacaoCompra(MaterialTramiteCotacaoCompraVO p) {
		return this.getListaMaterialTramiteCotacaoCompra().remove(p);
	}

	public boolean isMostrarBotao() {
		return this.isRetorno() && this.getMotivoRetorno() != null && !this.getMotivoRetorno().trim().isEmpty();
	}

	public boolean isUltimoPasso() {

		List<DepartamentoTramiteCotacaoCompraVO> listaDepartamentoTramite = this.getCotacao().getTramiteCotacaoCompra().getListaDepartamentoTramite();
		Preconditions.checkState(!listaDepartamentoTramite.isEmpty(), "lista de departamentotramite não pode estar vazia!!!");
		Optional<DepartamentoTramiteCotacaoCompraVO> findFirst = listaDepartamentoTramite.stream()
		        .filter(p -> this.departamentoTramiteCotacaoCompra.getOrdem() < p.getOrdem())
		        .filter(p -> this.validaCotacaoPassaDepartamento(this.getCotacao(), p)).findFirst();
		return !findFirst.isPresent();
	}

	public boolean isPrimeiroPasso() {
		List<DepartamentoTramiteCotacaoCompraVO> listaDepartamentoTramite = this.getCotacao().getTramiteCotacaoCompra().getListaDepartamentoTramite();
		Preconditions.checkState(!listaDepartamentoTramite.isEmpty(), "lista de departamentotramite não pode estar vazia!!!");
		Optional<DepartamentoTramiteCotacaoCompraVO> findFirst = listaDepartamentoTramite.stream()
		        .filter(p -> this.departamentoTramiteCotacaoCompra.getOrdem() > p.getOrdem())
		        .filter(p -> this.validaCotacaoPassaDepartamento(this.getCotacao(), p)).findFirst();
		return !findFirst.isPresent();
	}

	public boolean validaCotacaoPassaDepartamento(CotacaoVO cotacao, DepartamentoTramiteCotacaoCompraVO departamentoTramiteCotacaoCompra) {
		BigDecimal valorTotalCompraCotacao = cotacao.getValorTotalCompraCotacao();
		BigDecimal valorMinimo = departamentoTramiteCotacaoCompra.getValorMinimo();
		BigDecimal valorMaximo = departamentoTramiteCotacaoCompra.getValorMaximo();
		TipoControleFinanceiroEnum tipoControleFinanceiro = departamentoTramiteCotacaoCompra.getTipoControleFinanceiro();

		switch (tipoControleFinanceiro) {
		case FAIXA_VALORES:
			return this.bigDecimalBetween(valorTotalCompraCotacao, valorMinimo, valorMaximo);
		case VALOR_MINIMO:
			return valorTotalCompraCotacao.compareTo(valorMinimo) >= 0;
		case NAO_CONTROLA:
			return true;
		default:
			return false;
		}
	}

	public boolean bigDecimalBetween(BigDecimal valor, BigDecimal min, BigDecimal max) {
		return valor.compareTo(min) >= 0 && valor.compareTo(max) <= 0;
	}

	public boolean isTramiteDentroPrazo() {

		return this.calculaDiferencaEmDias(this.getDataInicio(), new Date()) < this.getDepartamentoTramiteCotacaoCompra().getPrazoExecucao();

	}

	private long calculaDiferencaEmDias(Date inicio, Date fim) {

		long diffInMillies = Math.abs(fim.getTime() - inicio.getTime());
		return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

	}

	public void validarDados() {
		Uteis.checkState(!Uteis.isAtributoPreenchido(getResponsavel()), "Não foi encontrado um Responsável para o Trâmite no Departamento " + getDepartamentoTramiteCotacaoCompra().getDepartamentoVO().getNome());
	}

}
