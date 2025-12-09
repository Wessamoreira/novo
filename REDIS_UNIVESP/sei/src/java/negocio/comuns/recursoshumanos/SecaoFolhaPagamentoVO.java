package negocio.comuns.recursoshumanos;

import java.util.Date;

import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.recursoshumanos.enumeradores.CategoriaRAISEnum;
import negocio.comuns.recursoshumanos.enumeradores.ControlePontoEnum;
import negocio.comuns.recursoshumanos.enumeradores.MotivoMudancaCNPJEnum;
import negocio.comuns.recursoshumanos.enumeradores.OptanteSimpleEnum;
import negocio.comuns.recursoshumanos.enumeradores.PorteEmpresaEnum;

public class SecaoFolhaPagamentoVO extends SuperVO {

	private static final long serialVersionUID = 3051963860846851506L;

	private Integer codigo;
	private String identificador;
	private String descricao;

	// INSS
	private Integer codigoTerceiros;
	private Integer codigoPagamentoGPS;
	private Integer FPAS;
	private Integer SAT;
	private Integer acidenteTrabalho;
	private OptanteSimpleEnum optanteSimples;

	// RAIS
	private String cnpjAnterior;
	private MotivoMudancaCNPJEnum motivoMudanca;
	private Integer empregados;
	private Integer prefixoRAIS;
	private Integer naturezaJuros;
	private CategoriaRAISEnum categoriaRAIS;
	private MesAnoEnum mesDataBase;
	private Date dataEncerramento;
	private PorteEmpresaEnum porteEmpresa;
	private ControlePontoEnum controlePonto;
	private Integer atividadeEconomica;
	
	public enum EnumCampoConsultaSecaoFolhaPagamento {
		DESCRICAO, 
		IDENTIFICADOR,
		CODIGO;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getIdentificador() {
		if (identificador == null) {
			identificador = "";
		}
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getCodigoTerceiros() {
		if (codigoTerceiros == null) {
			codigoTerceiros = 0;
		}
		return codigoTerceiros;
	}

	public void setCodigoTerceiros(Integer codigoTerceiros) {
		this.codigoTerceiros = codigoTerceiros;
	}

	public Integer getCodigoPagamentoGPS() {
		if (codigoPagamentoGPS == null) {
			codigoPagamentoGPS = 0;
		}
		return codigoPagamentoGPS;
	}

	public void setCodigoPagamentoGPS(Integer codigoPagamentoGPS) {
		this.codigoPagamentoGPS = codigoPagamentoGPS;
	}

	public Integer getFPAS() {
		if (FPAS == null) {
			FPAS = 0;
		}
		return FPAS;
	}

	public void setFPAS(Integer fPAS) {
		FPAS = fPAS;
	}

	public Integer getSAT() {
		if (SAT == null) {
			SAT = 0;
		}
		return SAT;
	}

	public void setSAT(Integer sAT) {
		SAT = sAT;
	}

	public Integer getAcidenteTrabalho() {
		if (acidenteTrabalho == null) {
			acidenteTrabalho = 0;
		}
		return acidenteTrabalho;
	}

	public void setAcidenteTrabalho(Integer acidenteTrabalho) {
		this.acidenteTrabalho = acidenteTrabalho;
	}

	public OptanteSimpleEnum getOptanteSimples() {
		return optanteSimples;
	}

	public void setOptanteSimples(OptanteSimpleEnum optanteSimples) {
		this.optanteSimples = optanteSimples;
	}

	public String getCnpjAnterior() {
		if (cnpjAnterior == null) {
			cnpjAnterior = "";
		}
		return cnpjAnterior;
	}

	public void setCnpjAnterior(String cnpjAnterior) {
		this.cnpjAnterior = cnpjAnterior;
	}

	public MotivoMudancaCNPJEnum getMotivoMudanca() {
		return motivoMudanca;
	}

	public void setMotivoMudanca(MotivoMudancaCNPJEnum motivoMudanca) {
		this.motivoMudanca = motivoMudanca;
	}

	public Integer getEmpregados() {
		if (empregados == null) {
			empregados = 0;
		}
		return empregados;
	}

	public void setEmpregados(Integer empregados) {
		this.empregados = empregados;
	}

	public Integer getPrefixoRAIS() {
		if (prefixoRAIS == null) {
			prefixoRAIS = 0;
		}
		return prefixoRAIS;
	}

	public void setPrefixoRAIS(Integer prefixoRAIS) {
		this.prefixoRAIS = prefixoRAIS;
	}

	public Integer getNaturezaJuros() {
		if (naturezaJuros == null) {
			naturezaJuros = 0;
		}
		return naturezaJuros;
	}

	public void setNaturezaJuros(Integer naturezaJuros) {
		this.naturezaJuros = naturezaJuros;
	}

	public CategoriaRAISEnum getCategoriaRAIS() {
		return categoriaRAIS;
	}

	public void setCategoriaRAIS(CategoriaRAISEnum categoriaRAIS) {
		this.categoriaRAIS = categoriaRAIS;
	}

	public MesAnoEnum getMesDataBase() {
		return mesDataBase;
	}

	public void setMesDataBase(MesAnoEnum mesDataBase) {
		this.mesDataBase = mesDataBase;
	}

	public Date getDataEncerramento() {
		return dataEncerramento;
	}

	public void setDataEncerramento(Date dataEncerramento) {
		this.dataEncerramento = dataEncerramento;
	}

	public PorteEmpresaEnum getPorteEmpresa() {
		return porteEmpresa;
	}

	public void setPorteEmpresa(PorteEmpresaEnum porteEmpresa) {
		this.porteEmpresa = porteEmpresa;
	}

	public ControlePontoEnum getControlePonto() {
		return controlePonto;
	}

	public void setControlePonto(ControlePontoEnum controlePonto) {
		this.controlePonto = controlePonto;
	}

	public Integer getAtividadeEconomica() {
		if (atividadeEconomica == null) {
			atividadeEconomica = 0;
		}
		return atividadeEconomica;
	}

	public void setAtividadeEconomica(Integer atividadeEconomica) {
		this.atividadeEconomica = atividadeEconomica;
	}

}
