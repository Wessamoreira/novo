/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.crm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author RODRIGO
 */
public class PainelGestorSupervisaoVendaVO {

	private Integer campanha;
	private UsuarioVO vendedor;
	private Integer quantidadeProspect;
	private Integer quantidadeInteracoesProspect;
	private String tempoConsumoMedio;
	private Double taxaConversaoEmSucesso;
	private Double taxaConversaoEmInsucesso;
	private Double taxaConversaoNaoFinalizado;
	private Integer numeroDiaTotalCampanha;
	private Integer numeroDiaAtualCampanha;
	private Date dataInicio;
	private Date dataTermino;

	private List<DadosGeraisCampanhaPorFuncionarioVO> dadosGeraisCampanhaPorFuncionarioVOs;
	private String tempoMedioAtendimentoSucesso;
	private Integer totalAtendimentoSucesso;

	private String tempoMedioAtendimentoInsucesso;
	private Integer totalAtendimentoInsucesso;

	private String tempoMedioAtendimentoNaoFinalizado;
	private Integer totalAtendimentoNaoFinalizado;

	private List<EstatisticaMotivoInsucessoVO> estatisticaMotivoInsucessoVOs;

	private List<EstatisticaPorEtapaCampanhaVO> estatisticaPorEtapaCampanhaSucessoVOs;
	private List<EstatisticaPorEtapaCampanhaVO> estatisticaPorEtapaCampanhaInsucessoVOs;
	private List<EstatisticaPorEtapaCampanhaVO> estatisticaPorEtapaCampanhaNaoFinalizadoVOs;

	private String urlEstatisticaSucesso;
	private String urlEstatisticaInsucesso;
	private String urlEstatisticaNaoFinalizado;

	private String estatisticaPorSexo;
	private String estatisticaPorTipoEmpresa;
	private String estatisticaPorIdade;
	private String estatisticaPorRenda;
	private String estatisticaPorFormacaoAcademica;

	private Map<String, Integer> estatisticaPorSexoData;
	private Map<String, Integer> estatisticaPorTipoEmpresaData;
	private Map<String, Integer> estatisticaPorIdadeData;
	private Map<String, Integer> estatisticaPorRendaData;
	private Map<String, Integer> estatisticaPorFormacaoAcademicaData;

	private List<EstatisticaPipelineVO> estatisticaPipelineVOs;
	private String graficoFunilPipeline;
	private Integer qtdeProspectPipeline;
	


	public String getEstatisticaPorFormacaoAcademica() {
		if (estatisticaPorFormacaoAcademica == null) {
			estatisticaPorFormacaoAcademica = "";
		}
		return estatisticaPorFormacaoAcademica;
	}

	public void setEstatisticaPorFormacaoAcademica(String estatisticaPorFormacaoAcademica) {
		this.estatisticaPorFormacaoAcademica = estatisticaPorFormacaoAcademica;
	}

	public String getEstatisticaPorIdade() {
		if (estatisticaPorIdade == null) {
			estatisticaPorIdade = "";
		}
		return estatisticaPorIdade;
	}

	public void setEstatisticaPorIdade(String estatisticaPorIdade) {
		this.estatisticaPorIdade = estatisticaPorIdade;
	}

	public String getEstatisticaPorRenda() {
		if (estatisticaPorRenda == null) {
			estatisticaPorRenda = "";
		}
		return estatisticaPorRenda;
	}

	public void setEstatisticaPorRenda(String estatisticaPorRenda) {
		this.estatisticaPorRenda = estatisticaPorRenda;
	}

	public String getEstatisticaPorSexo() {
		if (estatisticaPorSexo == null) {
			estatisticaPorSexo = "";
		}
		return estatisticaPorSexo;
	}

	public void setEstatisticaPorSexo(String estatisticaPorSexo) {
		this.estatisticaPorSexo = estatisticaPorSexo;
	}

	public String getEstatisticaPorTipoEmpresa() {
		if (estatisticaPorTipoEmpresa == null) {
			estatisticaPorTipoEmpresa = "";
		}
		return estatisticaPorTipoEmpresa;
	}

	public void setEstatisticaPorTipoEmpresa(String estatisticaPorTipoEmpresa) {
		this.estatisticaPorTipoEmpresa = estatisticaPorTipoEmpresa;
	}

	public Integer getCampanha() {
		return campanha;
	}

	public void setCampanha(Integer campanha) {
		this.campanha = campanha;
	}

	public List<DadosGeraisCampanhaPorFuncionarioVO> getDadosGeraisCampanhaPorFuncionarioVOs() {
		return dadosGeraisCampanhaPorFuncionarioVOs;
	}

	public void setDadosGeraisCampanhaPorFuncionarioVOs(List<DadosGeraisCampanhaPorFuncionarioVO> dadosGeraisCampanhaPorFuncionarioVOs) {
		this.dadosGeraisCampanhaPorFuncionarioVOs = dadosGeraisCampanhaPorFuncionarioVOs;
	}

	public List<EstatisticaMotivoInsucessoVO> getEstatisticaMotivoInsucessoVOs() {
		return estatisticaMotivoInsucessoVOs;
	}

	public void setEstatisticaMotivoInsucessoVOs(List<EstatisticaMotivoInsucessoVO> estatisticaMotivoInsucessoVOs) {
		this.estatisticaMotivoInsucessoVOs = estatisticaMotivoInsucessoVOs;
	}

	public List<EstatisticaPorEtapaCampanhaVO> getEstatisticaPorEtapaCampanhaInsucessoVOs() {
		return estatisticaPorEtapaCampanhaInsucessoVOs;
	}

	public void setEstatisticaPorEtapaCampanhaInsucessoVOs(List<EstatisticaPorEtapaCampanhaVO> estatisticaPorEtapaCampanhaInsucessoVOs) {
		this.estatisticaPorEtapaCampanhaInsucessoVOs = estatisticaPorEtapaCampanhaInsucessoVOs;
	}

	public List<EstatisticaPorEtapaCampanhaVO> getEstatisticaPorEtapaCampanhaNaoFinalizadoVOs() {
		return estatisticaPorEtapaCampanhaNaoFinalizadoVOs;
	}

	public void setEstatisticaPorEtapaCampanhaNaoFinalizadoVOs(List<EstatisticaPorEtapaCampanhaVO> estatisticaPorEtapaCampanhaNaoFinalizadoVOs) {
		this.estatisticaPorEtapaCampanhaNaoFinalizadoVOs = estatisticaPorEtapaCampanhaNaoFinalizadoVOs;
	}

	public List<EstatisticaPorEtapaCampanhaVO> getEstatisticaPorEtapaCampanhaSucessoVOs() {
		return estatisticaPorEtapaCampanhaSucessoVOs;
	}

	public void setEstatisticaPorEtapaCampanhaSucessoVOs(List<EstatisticaPorEtapaCampanhaVO> estatisticaPorEtapaCampanhaSucessoVOs) {
		this.estatisticaPorEtapaCampanhaSucessoVOs = estatisticaPorEtapaCampanhaSucessoVOs;
	}

	public UsuarioVO getVendedor() {
		if (vendedor == null) {
			vendedor = new UsuarioVO();
		}
		return vendedor;
	}

	public void setVendedor(UsuarioVO vendedor) {
		this.vendedor = vendedor;
	}

	public Integer getQuantidadeInteracoesProspect() {
		return quantidadeInteracoesProspect;
	}

	public void setQuantidadeInteracoesProspect(Integer quantidadeInteracoesProspect) {
		this.quantidadeInteracoesProspect = quantidadeInteracoesProspect;
	}

	public Integer getQuantidadeProspect() {
		return quantidadeProspect;
	}

	public void setQuantidadeProspect(Integer quantidadeProspect) {
		this.quantidadeProspect = quantidadeProspect;
	}

	public Double getTaxaConversaoEmSucesso() {
		return taxaConversaoEmSucesso;
	}

	public void setTaxaConversaoEmSucesso(Double taxaConversaoEmSucesso) {
		this.taxaConversaoEmSucesso = taxaConversaoEmSucesso;
	}

	public String getTempoConsumoMedio() {
		return tempoConsumoMedio;
	}

	public void setTempoConsumoMedio(String tempoConsumoMedio) {
		this.tempoConsumoMedio = tempoConsumoMedio;
	}

	public String getTempoMedioAtendimentoInsucesso() {
		return tempoMedioAtendimentoInsucesso;
	}

	public void setTempoMedioAtendimentoInsucesso(String tempoMedioAtendimentoInsucesso) {
		this.tempoMedioAtendimentoInsucesso = tempoMedioAtendimentoInsucesso;
	}

	public String getTempoMedioAtendimentoNaoFinalizado() {
		return tempoMedioAtendimentoNaoFinalizado;
	}

	public void setTempoMedioAtendimentoNaoFinalizado(String tempoMedioAtendimentoNaoFinalizado) {
		this.tempoMedioAtendimentoNaoFinalizado = tempoMedioAtendimentoNaoFinalizado;
	}

	public String getTempoMedioAtendimentoSucesso() {
		return tempoMedioAtendimentoSucesso;
	}

	public void setTempoMedioAtendimentoSucesso(String tempoMedioAtendimentoSucesso) {
		this.tempoMedioAtendimentoSucesso = tempoMedioAtendimentoSucesso;
	}

	public Integer getTotalAtendimentoInsucesso() {
		return totalAtendimentoInsucesso;
	}

	public void setTotalAtendimentoInsucesso(Integer totalAtendimentoInsucesso) {
		this.totalAtendimentoInsucesso = totalAtendimentoInsucesso;
	}

	public Integer getTotalAtendimentoNaoFinalizado() {
		return totalAtendimentoNaoFinalizado;
	}

	public void setTotalAtendimentoNaoFinalizado(Integer totalAtendimentoNaoFinalizado) {
		this.totalAtendimentoNaoFinalizado = totalAtendimentoNaoFinalizado;
	}

	public Integer getTotalAtendimentoSucesso() {
		return totalAtendimentoSucesso;
	}

	public void setTotalAtendimentoSucesso(Integer totalAtendimentoSucesso) {
		this.totalAtendimentoSucesso = totalAtendimentoSucesso;
	}

	public String getUrlEstatisticaInsucesso() {
		return urlEstatisticaInsucesso;
	}

	public void setUrlEstatisticaInsucesso(String urlEstatisticaInsucesso) {
		this.urlEstatisticaInsucesso = urlEstatisticaInsucesso;
	}

	public String getUrlEstatisticaNaoFinalizado() {
		return urlEstatisticaNaoFinalizado;
	}

	public void setUrlEstatisticaNaoFinalizado(String urlEstatisticaNaoFinalizado) {
		this.urlEstatisticaNaoFinalizado = urlEstatisticaNaoFinalizado;
	}

	public String getUrlEstatisticaSucesso() {
		return urlEstatisticaSucesso;
	}

	public void setUrlEstatisticaSucesso(String urlEstatisticaSucesso) {
		this.urlEstatisticaSucesso = urlEstatisticaSucesso;
	}

	public String getDataInicio_Apresentar() {
		if (dataInicio == null) {
			return "00/00/00";
		}
		return Uteis.getData(dataInicio);
	}

	public String getDataTermino_Apresentar() {
		if (dataTermino == null) {
			return "00/00/00";
		}
		return Uteis.getData(dataTermino);
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataTermino() {
		return dataTermino;
	}

	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}

	public Integer getNumeroDiaAtualCampanha() {
		return numeroDiaAtualCampanha;
	}

	public void setNumeroDiaAtualCampanha(Integer numeroDiaAtualCampanha) {
		this.numeroDiaAtualCampanha = numeroDiaAtualCampanha;
	}

	public Integer getNumeroDiaTotalCampanha() {
		return numeroDiaTotalCampanha;
	}

	public void setNumeroDiaTotalCampanha(Integer numeroDiaTotalCampanha) {
		this.numeroDiaTotalCampanha = numeroDiaTotalCampanha;
	}

	public Double getTaxaConversaoEmInsucesso() {
		return taxaConversaoEmInsucesso;
	}

	public void setTaxaConversaoEmInsucesso(Double taxaConversaoEmInsucesso) {
		this.taxaConversaoEmInsucesso = taxaConversaoEmInsucesso;
	}

	public Double getTaxaConversaoNaoFinalizado() {
		return taxaConversaoNaoFinalizado;
	}

	public void setTaxaConversaoNaoFinalizado(Double taxaConversaoNaoFinalizado) {
		this.taxaConversaoNaoFinalizado = taxaConversaoNaoFinalizado;
	}

	public List<EstatisticaPipelineVO> getEstatisticaPipelineVOs() {
		if (estatisticaPipelineVOs == null) {
			estatisticaPipelineVOs = new ArrayList<EstatisticaPipelineVO>(0);
		}
		return estatisticaPipelineVOs;
	}

	public void setEstatisticaPipelineVOs(List<EstatisticaPipelineVO> estatisticaPipelineVOs) {
		this.estatisticaPipelineVOs = estatisticaPipelineVOs;
	}

	public String getGraficoFunilPipeline() {
		return graficoFunilPipeline;
	}

	public void setGraficoFunilPipeline(String graficoFunilPipeline) {
		this.graficoFunilPipeline = graficoFunilPipeline;
	}

	public Integer getQtdeProspectPipeline() {
		if (qtdeProspectPipeline == null) {
			qtdeProspectPipeline = 0;
		}
		return qtdeProspectPipeline;
	}

	public void setQtdeProspectPipeline(Integer qtdeProspectPipeline) {
		this.qtdeProspectPipeline = qtdeProspectPipeline;
	}

	public Map<String, Integer> getEstatisticaPorSexoData() {
		if (estatisticaPorSexoData == null) {
			estatisticaPorSexoData = new HashMap<String, Integer>();
		}
		return estatisticaPorSexoData;
	}

	public void setEstatisticaPorSexoData(Map<String, Integer> estatisticaPorSexoData) {
		this.estatisticaPorSexoData = estatisticaPorSexoData;
	}

	public Map<String, Integer> getEstatisticaPorTipoEmpresaData() {
		if (estatisticaPorTipoEmpresaData == null) {
			estatisticaPorTipoEmpresaData = new HashMap<String, Integer>();
		}
		return estatisticaPorTipoEmpresaData;
	}

	public void setEstatisticaPorTipoEmpresaData(Map<String, Integer> estatisticaPorTipoEmpresaData) {
		this.estatisticaPorTipoEmpresaData = estatisticaPorTipoEmpresaData;
	}

	public Map<String, Integer> getEstatisticaPorIdadeData() {
		if (estatisticaPorIdadeData == null) {
			estatisticaPorIdadeData = new HashMap<String, Integer>();
		}
		return estatisticaPorIdadeData;
	}

	public void setEstatisticaPorIdadeData(Map<String, Integer> estatisticaPorIdadeData) {
		this.estatisticaPorIdadeData = estatisticaPorIdadeData;
	}

	public Map<String, Integer> getEstatisticaPorRendaData() {
		if (estatisticaPorRendaData == null) {
			estatisticaPorRendaData = new HashMap<String, Integer>();
		}
		return estatisticaPorRendaData;
	}

	public void setEstatisticaPorRendaData(Map<String, Integer> estatisticaPorRendaData) {
		this.estatisticaPorRendaData = estatisticaPorRendaData;
	}

	public Map<String, Integer> getEstatisticaPorFormacaoAcademicaData() {
		if (estatisticaPorFormacaoAcademicaData == null) {
			estatisticaPorFormacaoAcademicaData = new HashMap<String, Integer>();
		}
		return estatisticaPorFormacaoAcademicaData;
	}

	public void setEstatisticaPorFormacaoAcademicaData(Map<String, Integer> estatisticaPorFormacaoAcademicaData) {
		this.estatisticaPorFormacaoAcademicaData = estatisticaPorFormacaoAcademicaData;
	}


}
