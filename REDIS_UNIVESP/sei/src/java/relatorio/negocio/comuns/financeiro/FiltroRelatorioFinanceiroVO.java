package relatorio.negocio.comuns.financeiro;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;

public class FiltroRelatorioFinanceiroVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Boolean tipoOrigemInscricaoProcessoSeletivo;
	private Boolean tipoOrigemMatricula;
	private Boolean tipoOrigemRequerimento;
	private Boolean tipoOrigemBiblioteca;
	private Boolean tipoOrigemMensalidade;
	private Boolean tipoOrigemDevolucaoCheque;
	private Boolean tipoOrigemNegociacao;
	private Boolean tipoOrigemBolsaCusteadaConvenio;
	private Boolean tipoOrigemContratoReceita;
	private Boolean tipoOrigemOutros;
	private Boolean tipoOrigemMaterialDidatico;
	private Boolean tipoOrigemInclusaoReposicao;
	private Boolean filtrarPorDataCompetencia;
	
	private Boolean situacaoReceber;
	private Boolean situacaoRecebido;
	private Boolean situacaoCancelado;
	private Boolean situacaoRenegociado;
	private Boolean situacaoExcluida;
	
	private Boolean situacaoPagar;
	private Boolean situacaoPago;
	
	
	public Boolean apenasContasDaBiblioteca;
	
	/**
	 * Dados conta Pagar
	 * 
	 */
	private boolean tipoOrigemPagarCompra = false;
	private boolean tipoOrigemPagarServico = false;
	private boolean tipoOrigemPagarMulta = false;
	private boolean tipoOrigemPagarRequisicao = false;
	private boolean tipoOrigemPagarContratoDespesa = false;
	private boolean tipoOrigemPagarPrevisaoCusto = false;
	private boolean tipoOrigemPagarRestituicaoConvenio = false;
	private boolean tipoOrigemPagarRegistroManual = false;
	private boolean tipoOrigemPagarProcessamentoRetornoParceiro = false;
	private boolean tipoOrigemPagarNotaFiscalEntrada = false;
	private boolean tipoOrigemPagarAdiantamento = false;
	private boolean tipoOrigemPagarRecebimentoCompra = false;
	private boolean tipoOrigemPagarConciliacaoBancaria = false;
	private boolean tipoOrigemPagarTaxaCartao = false;
	private boolean tipoOrigemPagarNegociacaoContaPagar = false;
	private boolean tipoOrigemPagarFolhaPagamento = false;
	
	private boolean desabilitarFiltroOrigem = false;
	private Boolean considerarUnidadeFinanceira;
	
	private Boolean apresentarValorRecebidoComImpostosRetido;
	
	
	public enum FiltroRegistroCobranca {		
		SIMDEACORDOCOMPERIODOFILTRADO("SIMDEACORDOCOMPERIODOFILTRADO", "Sim - De Acordo Com o Período Filtrado")		
		,SIMINDEPENDENTEDOPERIODOFILTRADO("SIMINDEPENDENTEDOPERIODOFILTRADO", "Sim - Independente do Período Filtrado"),
		NAODEACORDOCOMPERIODOFILTRADO("NAODEACORDOCOMPERIODOFILTRADO", "Não - De Acordo Com o Período Filtrado")		
		,NAOINDEPENDENTEDOPERIODOFILTRADO("NAOINDEPENDENTEDOPERIODOFILTRADO", "Não - Independente do Período Filtrado");
		
		String valor;
		String descricao;
		
		private FiltroRegistroCobranca(String valor, String descricao) {
			this.valor = valor;
			this.descricao = descricao;
		}

		public String getValor() {
			return valor;
		}

		public void setValor(String valor) {
			this.valor = valor;
		}

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
	}
	
	public FiltroRelatorioFinanceiroVO (Boolean apenasContasDaBiblioteca) {
		setApenasContasDaBiblioteca(apenasContasDaBiblioteca);
	}
	

	public FiltroRelatorioFinanceiroVO() {
		
	}


	public Boolean getTipoOrigemInscricaoProcessoSeletivo() {
		if (tipoOrigemInscricaoProcessoSeletivo == null) {
			tipoOrigemInscricaoProcessoSeletivo = true;
		}
		if (getApenasContasDaBiblioteca().booleanValue()) {
			return false;
		}
		return tipoOrigemInscricaoProcessoSeletivo;
	}

	public void setTipoOrigemInscricaoProcessoSeletivo(Boolean tipoOrigemInscricaoProcessoSeletivo) {
		this.tipoOrigemInscricaoProcessoSeletivo = tipoOrigemInscricaoProcessoSeletivo;
	}

	public Boolean getTipoOrigemMatricula() {
		if (tipoOrigemMatricula == null) {
			tipoOrigemMatricula = true;
		}
		if (getApenasContasDaBiblioteca().booleanValue()) {
			return false;
		}
		return tipoOrigemMatricula;
	}

	public void setTipoOrigemMatricula(Boolean tipoOrigemMatricula) {
		this.tipoOrigemMatricula = tipoOrigemMatricula;
	}

	public Boolean getTipoOrigemRequerimento() {
		if (tipoOrigemRequerimento == null) {
			tipoOrigemRequerimento = true;
		}
		if (getApenasContasDaBiblioteca().booleanValue()) {
			return false;
		}
		return tipoOrigemRequerimento;
	}

	public void setTipoOrigemRequerimento(Boolean tipoOrigemRequerimento) {
		this.tipoOrigemRequerimento = tipoOrigemRequerimento;
	}

	public Boolean getTipoOrigemBiblioteca() {
		if (tipoOrigemBiblioteca == null) {
			tipoOrigemBiblioteca = true;
		}
		if (getApenasContasDaBiblioteca().booleanValue()) {
			return true;
		}
		return tipoOrigemBiblioteca;
	}

	public void setTipoOrigemBiblioteca(Boolean tipoOrigemBiblioteca) {
		this.tipoOrigemBiblioteca = tipoOrigemBiblioteca;
	}

	public Boolean getTipoOrigemMensalidade() {
		if (tipoOrigemMensalidade == null) {
			tipoOrigemMensalidade = true;
		}
		if (getApenasContasDaBiblioteca().booleanValue()) {
			return false;
		}
		return tipoOrigemMensalidade;
	}

	public void setTipoOrigemMensalidade(Boolean tipoOrigemMensalidade) {
		this.tipoOrigemMensalidade = tipoOrigemMensalidade;
	}

	public Boolean getTipoOrigemDevolucaoCheque() {
		if (tipoOrigemDevolucaoCheque == null) {
			tipoOrigemDevolucaoCheque = true;
		}
		if (getApenasContasDaBiblioteca().booleanValue()) {
			return false;
		}
		return tipoOrigemDevolucaoCheque;
	}

	public void setTipoOrigemDevolucaoCheque(Boolean tipoOrigemDevolucaoCheque) {
		this.tipoOrigemDevolucaoCheque = tipoOrigemDevolucaoCheque;
	}

	public Boolean getTipoOrigemNegociacao() {
		if (tipoOrigemNegociacao == null) {
			tipoOrigemNegociacao = true;
		}
		if (getApenasContasDaBiblioteca().booleanValue()) {
			return false;
		}
		return tipoOrigemNegociacao;
	}

	public void setTipoOrigemNegociacao(Boolean tipoOrigemNegociacao) {
		this.tipoOrigemNegociacao = tipoOrigemNegociacao;
	}

	public Boolean getTipoOrigemBolsaCusteadaConvenio() {
		if (tipoOrigemBolsaCusteadaConvenio == null) {
			tipoOrigemBolsaCusteadaConvenio = true;
		}
		if (getApenasContasDaBiblioteca().booleanValue()) {
			return false;
		}
		return tipoOrigemBolsaCusteadaConvenio;
	}

	public void setTipoOrigemBolsaCusteadaConvenio(Boolean tipoOrigemBolsaCusteadaConvenio) {
		this.tipoOrigemBolsaCusteadaConvenio = tipoOrigemBolsaCusteadaConvenio;
	}

	public Boolean getTipoOrigemContratoReceita() {
		if (tipoOrigemContratoReceita == null) {
			tipoOrigemContratoReceita = true;
		}
		if (getApenasContasDaBiblioteca().booleanValue()) {
			return false;
		}
		return tipoOrigemContratoReceita;
	}

	public void setTipoOrigemContratoReceita(Boolean tipoOrigemContratoReceita) {
		this.tipoOrigemContratoReceita = tipoOrigemContratoReceita;
	}

	public Boolean getTipoOrigemOutros() {
		if (tipoOrigemOutros == null) {
			tipoOrigemOutros = true;
		}
		if (getApenasContasDaBiblioteca().booleanValue()) {
			return false;
		}
		return tipoOrigemOutros;
	}

	public void setTipoOrigemOutros(Boolean tipoOrigemOutros) {
		this.tipoOrigemOutros = tipoOrigemOutros;
	}

	public Boolean getTipoOrigemInclusaoReposicao() {
		if (tipoOrigemInclusaoReposicao == null) {
			tipoOrigemInclusaoReposicao = true;
		}
		if (getApenasContasDaBiblioteca().booleanValue()) {
			return false;
		}
		return tipoOrigemInclusaoReposicao;
	}

	public void setTipoOrigemInclusaoReposicao(Boolean tipoOrigemInclusaoReposicao) {
		this.tipoOrigemInclusaoReposicao = tipoOrigemInclusaoReposicao;
	}

	public Boolean getFiltrarPorDataCompetencia() {
		if (filtrarPorDataCompetencia == null) {
			filtrarPorDataCompetencia = true;
		}
		return filtrarPorDataCompetencia;
	}

	public void setFiltrarPorDataCompetencia(Boolean filtrarPorDataCompetencia) {
		this.filtrarPorDataCompetencia = filtrarPorDataCompetencia;
	}

	public String getItensFiltroSituacao() {
		StringBuilder builder = new StringBuilder();
		if (getSituacaoReceber()) {
			builder.append(builder.length()>0?", ":"").append(SituacaoContaReceber.A_RECEBER.getDescricao());
		}
		if (getSituacaoRecebido()) {
			builder.append(builder.length()>0?", ":"").append(SituacaoContaReceber.RECEBIDO.getDescricao());
		}
		if (getSituacaoRenegociado()) {
			builder.append(builder.length()>0?", ":"").append(SituacaoContaReceber.NEGOCIADO.getDescricao());
		}
		if (getSituacaoCancelado()) {
			builder.append(builder.length()>0?", ":"").append(SituacaoContaReceber.CANCELADO_FINANCEIRO.getDescricao());
		}
		return builder.toString();
	}
	
	public void realizarMarcarTodasOrigens(){
		realizarSelecaoTodasOrigens(true);
	}
	
	public void realizarDesmarcarTodasOrigens(){
		realizarSelecaoTodasOrigens(false);
	}
	
	public void realizarSelecaoTodasOrigens(boolean selecionado){
		setTipoOrigemBiblioteca(selecionado);
		setTipoOrigemBolsaCusteadaConvenio(selecionado);
		setTipoOrigemContratoReceita(selecionado);
		setTipoOrigemDevolucaoCheque(selecionado);
		setTipoOrigemInclusaoReposicao(selecionado);
		setTipoOrigemInscricaoProcessoSeletivo(selecionado);
		setTipoOrigemMatricula(selecionado);
		setTipoOrigemMensalidade(selecionado);
		setTipoOrigemNegociacao(selecionado);
		setTipoOrigemOutros(selecionado);
		setTipoOrigemRequerimento(selecionado);
		setTipoOrigemMaterialDidatico(selecionado);
	}
	
	public String getItensFiltroTipoOrigem() {
		StringBuilder builder = new StringBuilder();
		if (getTipoOrigemBiblioteca()) {
			builder.append(builder.length()>0?", ":"").append(TipoOrigemContaReceber.BIBLIOTECA.getDescricao());
		}
		if (getTipoOrigemBolsaCusteadaConvenio()) {
			builder.append(builder.length()>0?", ":"").append(TipoOrigemContaReceber.BOLSA_CUSTEADA_CONVENIO.getDescricao());
		}
		if (getTipoOrigemContratoReceita()) {
			builder.append(builder.length()>0?", ":"").append(TipoOrigemContaReceber.CONTRATO_RECEITA.getDescricao());
		}
		if (getTipoOrigemDevolucaoCheque()) {
			builder.append(builder.length()>0?", ":"").append(TipoOrigemContaReceber.DEVOLUCAO_CHEQUE.getDescricao());
		}
		if (getTipoOrigemInclusaoReposicao()) {
			builder.append(builder.length()>0?", ":"").append(TipoOrigemContaReceber.INCLUSAOREPOSICAO.getDescricao());
		}
		if (getTipoOrigemInscricaoProcessoSeletivo()) {
			builder.append(builder.length()>0?", ":"").append(TipoOrigemContaReceber.INSCRICAO_PROCESSO_SELETIVO.getDescricao());
		}
		if (getTipoOrigemMatricula()) {
			builder.append(builder.length()>0?", ":"").append(TipoOrigemContaReceber.MATRICULA.getDescricao());
		}
		if (getTipoOrigemMensalidade()) {
			builder.append(builder.length()>0?", ":"").append(TipoOrigemContaReceber.MENSALIDADE.getDescricao());
		}
		if (getTipoOrigemNegociacao()) {
			builder.append(builder.length()>0?", ":"").append(TipoOrigemContaReceber.NEGOCIACAO.getDescricao());
		}
		if (getTipoOrigemOutros()) {
			builder.append(builder.length()>0?", ":"").append(TipoOrigemContaReceber.OUTROS.getDescricao());
		}
		if (getTipoOrigemRequerimento()) {
			builder.append(builder.length()>0?", ":"").append(TipoOrigemContaReceber.REQUERIMENTO.getDescricao());
		}
		if (getTipoOrigemMaterialDidatico()) {
			builder.append(builder.length()>0?", ":"").append(TipoOrigemContaReceber.MATERIAL_DIDATICO.getDescricao());
		}
		return builder.toString();
	}

	public Boolean getSituacaoReceber() {
		if (situacaoReceber == null) {
			situacaoReceber = true;
		}
		return situacaoReceber;
	}

	public void setSituacaoReceber(Boolean situacaoReceber) {
		this.situacaoReceber = situacaoReceber;
	}

	public Boolean getSituacaoRecebido() {
		if (situacaoRecebido == null) {
			situacaoRecebido = true;
		}
		return situacaoRecebido;
	}

	public void setSituacaoRecebido(Boolean situacaoRecebido) {
		this.situacaoRecebido = situacaoRecebido;
	}

	public Boolean getSituacaoCancelado() {
		if (situacaoCancelado == null) {
			situacaoCancelado = false;
		}
		return situacaoCancelado;
	}

	public void setSituacaoCancelado(Boolean situacaoCancelado) {
		this.situacaoCancelado = situacaoCancelado;
	}

	public Boolean getSituacaoRenegociado() {
		if (situacaoRenegociado == null) {
			situacaoRenegociado = true;
		}
		return situacaoRenegociado;
	}

	public void setSituacaoRenegociado(Boolean situacaoRenegociado) {
		this.situacaoRenegociado = situacaoRenegociado;
	}
	
	public void realizarMarcarTodasSituacoes(){
		realizarSelecaoTodasSituacoes(true);
	}
	
	public void realizarDesmarcarTodasSituacoes(){
		realizarSelecaoTodasSituacoes(false);
	}
	
	public void realizarSelecaoTodasSituacoes(boolean selecionado){
		setSituacaoCancelado(selecionado);
		setSituacaoReceber(selecionado);
		setSituacaoRecebido(selecionado);
		setSituacaoRenegociado(selecionado);
		setSituacaoPagar(selecionado);
		setSituacaoPago(selecionado);		
	}

	public Boolean getSituacaoPagar() {
		if (situacaoPagar == null) {
			situacaoPagar = true;
		}
		return situacaoPagar;
	}

	public void setSituacaoPagar(Boolean situacaoPagar) {
		this.situacaoPagar = situacaoPagar;
	}

	public Boolean getSituacaoPago() {
		if (situacaoPago == null) {
			situacaoPago = true;
		}
		return situacaoPago;
	}

	public void setSituacaoPago(Boolean situacaoPago) {
		this.situacaoPago = situacaoPago;
	}


	public void realizarMarcarTodosTipoOrigem(){
		realizarSelecaoTodasTipoOrigem(true);
	}
	
	public void realizarDesmarcarTodosTipoOrigem(){
		realizarSelecaoTodasTipoOrigem(false);
	}

	public void realizarSelecaoTodasTipoOrigem(boolean selecionado){
		setTipoOrigemInscricaoProcessoSeletivo(selecionado);
		setTipoOrigemMatricula(selecionado);
		setTipoOrigemRequerimento(selecionado);
		setTipoOrigemBiblioteca(selecionado);
		setTipoOrigemMensalidade(selecionado);
		setTipoOrigemDevolucaoCheque(selecionado);
		setTipoOrigemNegociacao(selecionado);
		setTipoOrigemBolsaCusteadaConvenio(selecionado);
		setTipoOrigemContratoReceita(selecionado);
		setTipoOrigemOutros(selecionado);
		setTipoOrigemInclusaoReposicao(selecionado);		
		setTipoOrigemMaterialDidatico(selecionado);
	}

	public Boolean getApenasContasDaBiblioteca() {
		if (apenasContasDaBiblioteca == null) {
			apenasContasDaBiblioteca = Boolean.FALSE;
		}
		return apenasContasDaBiblioteca;
	}

	public void setApenasContasDaBiblioteca(Boolean apenasContasDaBiblioteca) {
		this.apenasContasDaBiblioteca = apenasContasDaBiblioteca;
	}
	
	
	public void realizarSelecaoTodasOrigensContaPagar(boolean selecionado){
		setTipoOrigemPagarCompra(selecionado);
		setTipoOrigemPagarServico(selecionado);
		setTipoOrigemPagarMulta(selecionado);
		setTipoOrigemPagarRequisicao(selecionado);
		setTipoOrigemPagarContratoDespesa(selecionado);
		setTipoOrigemPagarPrevisaoCusto(selecionado);
		setTipoOrigemPagarRestituicaoConvenio(selecionado);
		setTipoOrigemPagarRegistroManual(selecionado);
		setTipoOrigemPagarProcessamentoRetornoParceiro(selecionado);
		setTipoOrigemPagarNotaFiscalEntrada(selecionado);
		setTipoOrigemPagarAdiantamento(selecionado);
		setTipoOrigemPagarRecebimentoCompra(selecionado);
		setTipoOrigemPagarConciliacaoBancaria(selecionado);
		setTipoOrigemPagarTaxaCartao(selecionado);
		setTipoOrigemPagarNegociacaoContaPagar(selecionado);
		setTipoOrigemPagarFolhaPagamento(selecionado);
	}


	public boolean isTipoOrigemPagarCompra() {		
		return tipoOrigemPagarCompra;
	}


	public void setTipoOrigemPagarCompra(boolean tipoOrigemPagarCompra) {
		this.tipoOrigemPagarCompra = tipoOrigemPagarCompra;
	}


	public boolean isTipoOrigemPagarServico() {
		return tipoOrigemPagarServico;
	}


	public void setTipoOrigemPagarServico(boolean tipoOrigemPagarServico) {
		this.tipoOrigemPagarServico = tipoOrigemPagarServico;
	}


	public boolean isTipoOrigemPagarMulta() {		
		return tipoOrigemPagarMulta;
	}


	public void setTipoOrigemPagarMulta(boolean tipoOrigemPagarMulta) {
		this.tipoOrigemPagarMulta = tipoOrigemPagarMulta;
	}


	public boolean isTipoOrigemPagarRequisicao() {		
		return tipoOrigemPagarRequisicao;
	}


	public void setTipoOrigemPagarRequisicao(boolean tipoOrigemPagarRequisicao) {
		this.tipoOrigemPagarRequisicao = tipoOrigemPagarRequisicao;
	}


	public boolean isTipoOrigemPagarContratoDespesa() {		
		return tipoOrigemPagarContratoDespesa;
	}


	public void setTipoOrigemPagarContratoDespesa(boolean tipoOrigemPagarContratoDespesa) {
		this.tipoOrigemPagarContratoDespesa = tipoOrigemPagarContratoDespesa;
	}


	public boolean isTipoOrigemPagarPrevisaoCusto() {		
		return tipoOrigemPagarPrevisaoCusto;
	}


	public void setTipoOrigemPagarPrevisaoCusto(boolean tipoOrigemPagarPrevisaoCusto) {
		this.tipoOrigemPagarPrevisaoCusto = tipoOrigemPagarPrevisaoCusto;
	}


	public boolean isTipoOrigemPagarRestituicaoConvenio() {
		return tipoOrigemPagarRestituicaoConvenio;
	}


	public void setTipoOrigemPagarRestituicaoConvenio(boolean tipoOrigemPagarRestituicaoConvenio) {
		this.tipoOrigemPagarRestituicaoConvenio = tipoOrigemPagarRestituicaoConvenio;
	}


	public boolean isTipoOrigemPagarRegistroManual() {
		return tipoOrigemPagarRegistroManual;
	}


	public void setTipoOrigemPagarRegistroManual(boolean tipoOrigemPagarRegistroManual) {
		this.tipoOrigemPagarRegistroManual = tipoOrigemPagarRegistroManual;
	}


	public boolean isTipoOrigemPagarProcessamentoRetornoParceiro() {
		return tipoOrigemPagarProcessamentoRetornoParceiro;
	}


	public void setTipoOrigemPagarProcessamentoRetornoParceiro(boolean tipoOrigemPagarProcessamentoRetornoParceiro) {
		this.tipoOrigemPagarProcessamentoRetornoParceiro = tipoOrigemPagarProcessamentoRetornoParceiro;
	}


	public boolean isTipoOrigemPagarNotaFiscalEntrada() {
		return tipoOrigemPagarNotaFiscalEntrada;
	}


	public void setTipoOrigemPagarNotaFiscalEntrada(boolean tipoOrigemPagarNotaFiscalEntrada) {
		this.tipoOrigemPagarNotaFiscalEntrada = tipoOrigemPagarNotaFiscalEntrada;
	}


	public boolean isTipoOrigemPagarAdiantamento() {
		return tipoOrigemPagarAdiantamento;
	}


	public void setTipoOrigemPagarAdiantamento(boolean tipoOrigemPagarAdiantamento) {
		this.tipoOrigemPagarAdiantamento = tipoOrigemPagarAdiantamento;
	}


	public boolean isTipoOrigemPagarRecebimentoCompra() {
		return tipoOrigemPagarRecebimentoCompra;
	}


	public void setTipoOrigemPagarRecebimentoCompra(boolean tipoOrigemPagarRecebimentoCompra) {
		this.tipoOrigemPagarRecebimentoCompra = tipoOrigemPagarRecebimentoCompra;
	}


	public boolean isTipoOrigemPagarConciliacaoBancaria() {
		return tipoOrigemPagarConciliacaoBancaria;
	}


	public void setTipoOrigemPagarConciliacaoBancaria(boolean tipoOrigemPagarConciliacaoBancaria) {
		this.tipoOrigemPagarConciliacaoBancaria = tipoOrigemPagarConciliacaoBancaria;
	}


	public boolean isTipoOrigemPagarTaxaCartao() {
		return tipoOrigemPagarTaxaCartao;
	}


	public void setTipoOrigemPagarTaxaCartao(boolean tipoOrigemPagarTaxaCartao) {
		this.tipoOrigemPagarTaxaCartao = tipoOrigemPagarTaxaCartao;
	}


	public boolean isTipoOrigemPagarNegociacaoContaPagar() {
		return tipoOrigemPagarNegociacaoContaPagar;
	}


	public void setTipoOrigemPagarNegociacaoContaPagar(boolean tipoOrigemPagarNegociacaoContaPagar) {
		this.tipoOrigemPagarNegociacaoContaPagar = tipoOrigemPagarNegociacaoContaPagar;
	}

	public boolean isTipoOrigemPagarFolhaPagamento() {
		return tipoOrigemPagarFolhaPagamento;
	}


	public void setTipoOrigemPagarFolhaPagamento(boolean tipoOrigemPagarFolhaPagamento) {
		this.tipoOrigemPagarFolhaPagamento = tipoOrigemPagarFolhaPagamento;
	}	

	public Boolean getTipoOrigemMaterialDidatico() {
		if (tipoOrigemMaterialDidatico == null) {
			tipoOrigemMaterialDidatico = false;
		}
		return tipoOrigemMaterialDidatico;
	}


	public void setTipoOrigemMaterialDidatico(Boolean tipoOrigemMaterialDidatico) {
		this.tipoOrigemMaterialDidatico = tipoOrigemMaterialDidatico;
	}
	
	public String adicionarFiltroTipoOrigem() {
		StringBuilder sqlTipoOrigem = new StringBuilder("(''");
		
		if (getTipoOrigemBiblioteca()) {
			sqlTipoOrigem.append(", 'BIB'");
		}
		if (getTipoOrigemBolsaCusteadaConvenio()) {
			sqlTipoOrigem.append(", 'BCC'");
		}
		if (getTipoOrigemContratoReceita()) {
			sqlTipoOrigem.append(", 'CTR'");
		}
		if (getTipoOrigemDevolucaoCheque()) {
			sqlTipoOrigem.append(", 'DCH'");
		}
		if (getTipoOrigemInclusaoReposicao()) {
			sqlTipoOrigem.append(", 'IRE'");
		}
		if (getTipoOrigemInscricaoProcessoSeletivo()) {
			sqlTipoOrigem.append(", 'IPS'");
		}
		if (getTipoOrigemMatricula()) {
			sqlTipoOrigem.append(", 'MAT'");
		}
		if (getTipoOrigemMaterialDidatico()) {
			sqlTipoOrigem.append(", 'MDI'");
		}
		if (getTipoOrigemMensalidade()) {
			sqlTipoOrigem.append(", 'MEN'");
		}
		if (getTipoOrigemNegociacao()) {
			sqlTipoOrigem.append(", 'NCR'");
		}
		if (getTipoOrigemOutros()) {
			sqlTipoOrigem.append(", 'OUT'");
		}
		if (getTipoOrigemRequerimento()) {
			sqlTipoOrigem.append(", 'REQ'");
		}
		
		sqlTipoOrigem.append(")");	
		
		return sqlTipoOrigem.toString();
	}


	public Boolean getSituacaoExcluida() {
		if(situacaoExcluida == null) {
			situacaoExcluida =  false;
		}
		return situacaoExcluida;
	}


	public void setSituacaoExcluida(Boolean situacaoExcluida) {
		this.situacaoExcluida = situacaoExcluida;
	}


	public boolean isDesabilitarFiltroOrigem() {
		return desabilitarFiltroOrigem;
	}


	public void setDesabilitarFiltroOrigem(boolean desabilitarFiltroOrigem) {
		this.desabilitarFiltroOrigem = desabilitarFiltroOrigem;
	}


	public Boolean getConsiderarUnidadeFinanceira() {
		if(considerarUnidadeFinanceira == null) {
			considerarUnidadeFinanceira =  true;
		}
		return considerarUnidadeFinanceira;
	}


	public void setConsiderarUnidadeFinanceira(Boolean considerarUnidadeFinanceira) {
		this.considerarUnidadeFinanceira = considerarUnidadeFinanceira;
	}
	
	public boolean getNenhumFiltroTipoOrigemSelecionado() {
		return !getTipoOrigemInscricaoProcessoSeletivo() && !getTipoOrigemMatricula() && !getTipoOrigemBiblioteca() && !getTipoOrigemMensalidade()
				&& !getTipoOrigemDevolucaoCheque() && !getTipoOrigemNegociacao() && !getTipoOrigemBolsaCusteadaConvenio() && !getTipoOrigemContratoReceita()
				&& !getTipoOrigemOutros() && !getTipoOrigemMaterialDidatico() && !getTipoOrigemInclusaoReposicao() && !getTipoOrigemRequerimento();
	}


	public Boolean getApresentarValorRecebidoComImpostosRetido() {
		if(apresentarValorRecebidoComImpostosRetido == null) {
			apresentarValorRecebidoComImpostosRetido = false;
		}
		return apresentarValorRecebidoComImpostosRetido;
	}


	public void setApresentarValorRecebidoComImpostosRetido(Boolean apresentarValorRecebidoComImpostosRetido) {
		this.apresentarValorRecebidoComImpostosRetido = apresentarValorRecebidoComImpostosRetido;
	}
	
	
}