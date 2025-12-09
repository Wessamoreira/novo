package webservice.boletoonline.itau.comuns;

import java.util.ArrayList;
import java.util.List;

public class ItauBoletoOnlineVO {

	private Integer tipo_ambiente;
	private Integer tipo_registro;
	private Integer tipo_cobranca;
	private String tipo_produto;
	private String subproduto;

	private BeneficiarioVO beneficiario;
	private DebitoVO debito;

	private String identificador_titulo_empresa;
	private String uso_banco;
	private String titulo_aceite;

	private PagadorVO pagador;

	private Integer tipo_carteira_titulo;

	private SacadorAvalistaVO sacador_avalista;

	private MoedaVO moeda;

	private String nosso_numero;

	private String digito_verificador_nosso_numero;
	private String codigo_barras;
	private String data_vencimento;
	private String valor_cobrado;
	private String seu_numero;
	private String especie;
	private String data_emissao;
	private String data_limite_pagamento;
	private Integer tipo_pagamento;
	private Boolean indicador_pagamento_parcial;
	private Integer quantidade_pagamento_parcial;
	private Integer quantidade_parcelas;
	private String instrucao_cobranca_1;
	private Integer quantidade_dias_1;
	private String data_instrucao_1;
	private String instrucao_cobranca_2;
	private Integer quantidade_dias_2;
	private String data_instrucao_2;
	private String instrucao_cobranca_3;
	private Integer quantidade_dias_3;
	private String data_instrucao_3;
	private String valor_abatimento;

	private JurosVO juros;
	private MultaVO multa;
	private List<GrupoDescontoVO> grupo_desconto;
	private RecebimentoDivergenteVO recebimento_divergente;
	private List<GrupoRateioVO> grupo_rateio;

	public Integer getTipo_ambiente() {
		return tipo_ambiente;
	}

	public void setTipo_ambiente(Integer tipo_ambiente) {
		this.tipo_ambiente = tipo_ambiente;
	}

	public Integer getTipo_registro() {
		return tipo_registro;
	}

	public void setTipo_registro(Integer tipo_registro) {
		this.tipo_registro = tipo_registro;
	}

	public Integer getTipo_cobranca() {
		return tipo_cobranca;
	}

	public void setTipo_cobranca(Integer tipo_cobranca) {
		this.tipo_cobranca = tipo_cobranca;
	}

	public String getTipo_produto() {
		return tipo_produto;
	}

	public void setTipo_produto(String tipo_produto) {
		this.tipo_produto = tipo_produto;
	}

	public String getSubproduto() {
		return subproduto;
	}

	public void setSubproduto(String subproduto) {
		this.subproduto = subproduto;
	}

	public BeneficiarioVO getBeneficiario() {
		if (beneficiario == null) {
			beneficiario = new BeneficiarioVO();
		}
		return beneficiario;
	}

	public void setBeneficiario(BeneficiarioVO beneficiario) {
		this.beneficiario = beneficiario;
	}

	public DebitoVO getDebito() {
		if (debito == null) {
			debito = new DebitoVO();
		}
		return debito;
	}

	public void setDebito(DebitoVO debito) {
		this.debito = debito;
	}

	public String getIdentificador_titulo_empresa() {
		if (identificador_titulo_empresa == null) {
			identificador_titulo_empresa = "";
		}
		return identificador_titulo_empresa;
	}

	public void setIdentificador_titulo_empresa(String identificador_titulo_empresa) {
		this.identificador_titulo_empresa = identificador_titulo_empresa;
	}

	public String getUso_banco() {
		if (uso_banco == null) {
			uso_banco = "";
		}
		return uso_banco;
	}

	public void setUso_banco(String uso_banco) {
		this.uso_banco = uso_banco;
	}

	public String getTitulo_aceite() {
		if (titulo_aceite == null) {
			titulo_aceite = "";
		}
		return titulo_aceite;
	}

	public void setTitulo_aceite(String titulo_aceite) {
		this.titulo_aceite = titulo_aceite;
	}

	public PagadorVO getPagador() {
		if (pagador == null) {
			pagador = new PagadorVO();
		}
		return pagador;
	}

	public void setPagador(PagadorVO pagador) {
		this.pagador = pagador;
	}

	public Integer getTipo_carteira_titulo() {
		if (tipo_carteira_titulo == null) {
			tipo_carteira_titulo = 0;
		}
		return tipo_carteira_titulo;
	}

	public void setTipo_carteira_titulo(Integer tipo_carteira_titulo) {
		this.tipo_carteira_titulo = tipo_carteira_titulo;
	}

	public SacadorAvalistaVO getSacador_avalista() {
		return sacador_avalista;
	}

	public void setSacador_avalista(SacadorAvalistaVO sacador_avalista) {
		this.sacador_avalista = sacador_avalista;
	}

	public MoedaVO getMoeda() {
		if (moeda == null) {
			moeda = new MoedaVO();
		}
		return moeda;
	}

	public void setMoeda(MoedaVO moeda) {
		this.moeda = moeda;
	}

	public String getNosso_numero() {
		if (nosso_numero == null) {
			nosso_numero = "";
		}
		return nosso_numero;
	}

	public void setNosso_numero(String nosso_numero) {
		this.nosso_numero = nosso_numero;
	}

	public String getDigito_verificador_nosso_numero() {
		if (digito_verificador_nosso_numero == null) {
			digito_verificador_nosso_numero = "";
		}
		return digito_verificador_nosso_numero;
	}

	public void setDigito_verificador_nosso_numero(String digito_verificador_nosso_numero) {
		this.digito_verificador_nosso_numero = digito_verificador_nosso_numero;
	}

	public String getCodigo_barras() {
		if (codigo_barras == null) {
			codigo_barras = "";
		}
		return codigo_barras;
	}

	public void setCodigo_barras(String codigo_barras) {
		this.codigo_barras = codigo_barras;
	}

	public String getData_vencimento() {
		if (data_vencimento == null) {
			data_vencimento = "";
		}
		return data_vencimento;
	}

	public void setData_vencimento(String data_vencimento) {
		this.data_vencimento = data_vencimento;
	}

	public String getValor_cobrado() {
		if (valor_cobrado == null) {
			valor_cobrado = "";
		}
		return valor_cobrado;
	}

	public void setValor_cobrado(String valor_cobrado) {
		this.valor_cobrado = valor_cobrado;
	}

	public String getSeu_numero() {
		if (seu_numero == null) {
			seu_numero = "";
		}
		return seu_numero;
	}

	public void setSeu_numero(String seu_numero) {
		this.seu_numero = seu_numero;
	}

	public String getEspecie() {
		if (especie == null) {
			especie = "";
		}
		return especie;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
	}

	public String getData_emissao() {
		if (data_emissao == null) {
			data_emissao = "";
		}
		return data_emissao;
	}

	public void setData_emissao(String data_emissao) {
		this.data_emissao = data_emissao;
	}

	public String getData_limite_pagamento() {
		if (data_limite_pagamento == null) {
			data_limite_pagamento = "";
		}
		return data_limite_pagamento;
	}

	public void setData_limite_pagamento(String data_limite_pagamento) {
		this.data_limite_pagamento = data_limite_pagamento;
	}

	public Integer getTipo_pagamento() {
		if (tipo_pagamento == null) {
			tipo_pagamento = 0;
		}
		return tipo_pagamento;
	}

	public void setTipo_pagamento(Integer tipo_pagamento) {
		this.tipo_pagamento = tipo_pagamento;
	}

	public Boolean getIndicador_pagamento_parcial() {
		if (indicador_pagamento_parcial == null) {
			indicador_pagamento_parcial = Boolean.FALSE;
		}
		return indicador_pagamento_parcial;
	}

	public void setIndicador_pagamento_parcial(Boolean indicador_pagamento_parcial) {
		this.indicador_pagamento_parcial = indicador_pagamento_parcial;
	}

	public Integer getQuantidade_pagamento_parcial() {
		if (quantidade_pagamento_parcial == null) {
			quantidade_pagamento_parcial = 0;
		}
		return quantidade_pagamento_parcial;
	}

	public void setQuantidade_pagamento_parcial(Integer quantidade_pagamento_parcial) {
		this.quantidade_pagamento_parcial = quantidade_pagamento_parcial;
	}

	public Integer getQuantidade_parcelas() {
		if (quantidade_parcelas == null) {
			quantidade_parcelas = 0;
		}
		return quantidade_parcelas;
	}

	public void setQuantidade_parcelas(Integer quantidade_parcelas) {
		this.quantidade_parcelas = quantidade_parcelas;
	}

	public String getInstrucao_cobranca_1() {
		if (instrucao_cobranca_1 == null) {
			instrucao_cobranca_1 = "";
		}
		return instrucao_cobranca_1;
	}

	public void setInstrucao_cobranca_1(String instrucao_cobranca_1) {
		this.instrucao_cobranca_1 = instrucao_cobranca_1;
	}

	public Integer getQuantidade_dias_1() {
		if (quantidade_dias_1 == null) {
			quantidade_dias_1 =0;
		}
		return quantidade_dias_1;
	}

	public void setQuantidade_dias_1(Integer quantidade_dias_1) {
		this.quantidade_dias_1 = quantidade_dias_1;
	}

	public String getData_instrucao_1() {
		if (data_instrucao_1 == null) {
			data_instrucao_1 = "";
		}
		return data_instrucao_1;
	}

	public void setData_instrucao_1(String data_instrucao_1) {
		this.data_instrucao_1 = data_instrucao_1;
	}

	public String getInstrucao_cobranca_2() {
		if (instrucao_cobranca_2 == null) {
			instrucao_cobranca_2 = "";
		}
		return instrucao_cobranca_2;
	}

	public void setInstrucao_cobranca_2(String instrucao_cobranca_2) {
		this.instrucao_cobranca_2 = instrucao_cobranca_2;
	}

	public Integer getQuantidade_dias_2() {
		if (quantidade_dias_2 == null) {
			quantidade_dias_2 = 0;
		}
		return quantidade_dias_2;
	}

	public void setQuantidade_dias_2(Integer quantidade_dias_2) {
		this.quantidade_dias_2 = quantidade_dias_2;
	}

	public String getData_instrucao_2() {
		if (data_instrucao_2 == null) {
			data_instrucao_2 = "";
		}
		return data_instrucao_2;
	}

	public void setData_instrucao_2(String data_instrucao_2) {
		this.data_instrucao_2 = data_instrucao_2;
	}

	public String getInstrucao_cobranca_3() {
		if (instrucao_cobranca_3 == null) {
			instrucao_cobranca_3 = "";
		}
		return instrucao_cobranca_3;
	}

	public void setInstrucao_cobranca_3(String instrucao_cobranca_3) {
		this.instrucao_cobranca_3 = instrucao_cobranca_3;
	}

	public Integer getQuantidade_dias_3() {
		if (quantidade_dias_3 == null) {
			quantidade_dias_3 = 0;
		}
		return quantidade_dias_3;
	}

	public void setQuantidade_dias_3(Integer quantidade_dias_3) {
		this.quantidade_dias_3 = quantidade_dias_3;
	}

	public String getData_instrucao_3() {
		if (data_instrucao_3 == null) {
			data_instrucao_3 = "";
		}
		return data_instrucao_3;
	}

	public void setData_instrucao_3(String data_instrucao_3) {
		this.data_instrucao_3 = data_instrucao_3;
	}

	public String getValor_abatimento() {
		if (valor_abatimento == null) {
			valor_abatimento = "";
		}
		return valor_abatimento;
	}

	public void setValor_abatimento(String valor_abatimento) {
		this.valor_abatimento = valor_abatimento;
	}

	public JurosVO getJuros() {
		if (juros == null) {
			juros = new JurosVO();
		}
		return juros;
	}

	public void setJuros(JurosVO juros) {
		this.juros = juros;
	}

	public MultaVO getMulta() {
		if (multa == null) {
			multa = new MultaVO();
		}
		return multa;
	}

	public void setMulta(MultaVO multa) {
		this.multa = multa;
	}

	public List<GrupoDescontoVO> getGrupo_desconto() {
		if (grupo_desconto == null) {
			new ArrayList<GrupoDescontoVO>();
		}
		return grupo_desconto;
	}

	public void setGrupo_desconto(List<GrupoDescontoVO> grupo_desconto) {
		this.grupo_desconto = grupo_desconto;
	}

	public RecebimentoDivergenteVO getRecebimento_divergente() {
		if (recebimento_divergente == null) {
			recebimento_divergente = new RecebimentoDivergenteVO();
		}
		return recebimento_divergente;
	}

	public void setRecebimento_divergente(RecebimentoDivergenteVO recebimento_divergente) {
		this.recebimento_divergente = recebimento_divergente;
	}

	public List<GrupoRateioVO> getGrupo_rateio() {
		if (grupo_rateio == null) {
			grupo_rateio = new ArrayList<GrupoRateioVO>();
		}
		return grupo_rateio;
	}

	public void setGrupo_rateio(List<GrupoRateioVO> grupo_rateio) {
		this.grupo_rateio = grupo_rateio;
	}

}
