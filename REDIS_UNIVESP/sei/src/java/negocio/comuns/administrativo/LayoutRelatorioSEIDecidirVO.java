package negocio.comuns.administrativo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import negocio.comuns.administrativo.enumeradores.RelatorioSEIDecidirModuloEnum;
import negocio.comuns.administrativo.enumeradores.RelatorioSEIDecidirNivelDetalhamentoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

/**
 * 
 * @author Leonardo Riciolle Data 14/11/2014
 */
@XmlRootElement(name = "layout")
public class LayoutRelatorioSEIDecidirVO extends SuperVO {

	private static final long serialVersionUID = -8051696683850355684L;
	@XmlTransient
	private Integer codigo;
	private String descricao;
	private RelatorioSEIDecidirModuloEnum modulo;
	private RelatorioSEIDecidirNivelDetalhamentoEnum nivelDetalhamento;
	private List<LayoutRelatorioSeiDecidirCampoVO> layoutRelatorioSeiDecidirCampoVOs;
	private String agruparRelatorioPor;
	private String condicaoWhereAdicional;
	private String groupByAdicional;
	private String orderByAdicional;
	private String nomePadraoArquivo;
	private TipoRelatorioEnum tipoGeracaoRelatorio;
	private String tagAberturaGeralXml;
	private String tagFechamentoGeralXml;
	private String tagAberturaLinhaXml;
	private String tagFechamentoLinhaXml;
	private Boolean subRelatorio;
	private Boolean subRelatorioCrossTab;
	private String textoCabecalho;
	private Boolean gerarLinhaEmBranco;
	private String condicaoJoinAdicional;
	private String queryLayoutPersonalizado;
	private Boolean layoutPersonalizado;
	private List<FiltroPersonalizadoVO> listaFiltroPersonalizadoVOs;
	
	private Boolean apresentarFiltroFixoUnidadeEnsino;
	private Boolean apresentarFiltroFixoNivelEducacional;
	private Boolean apresentarFiltroFixoCotaIngresso;
	private Boolean apresentarFiltroFixoCurso;
	private Boolean apresentarFiltroFixoTurno;
	private Boolean apresentarFiltroFixoTurma;
	private Boolean apresentarFiltroFixoMatricula;
	private Boolean apresentarFiltroFixoTurmaEstudouDisciplina;
	private Boolean apresentarFiltroFixoDisciplina;
	private Boolean apresentarFiltroFixoFiltroPeriodo;
	private Boolean apresentarFiltroFixoPeriodoLetivo;
	private Boolean apresentarFiltroFixoSituacaoAceiteEletronicoContrato;
	private Boolean apresentarFiltroFixoSituacaoFiltroSituacaoAcademica;
	private Boolean apresentarFiltroFixoSituacaoFinanceiraMatricula;
	
	private Boolean apresentarFiltroFixoCentroReceita;
	private Boolean apresentarFiltroFixoFormaPagamento;
	private Boolean apresentarFiltroFixoPeriodoLetivoCentroResultado;
	private Boolean apresentarFiltroFixoContaCorrente;
	private Boolean apresentarFiltroFixoTipoPessoa;
	private Boolean apresentarFiltroFixoConsiderarUnidadeFinanceira;
	private Boolean apresentarFiltroFixoContaCorrenteRecebimento;
	private Boolean apresentarFiltroFixoSituacaoContaReceber;
	private Boolean apresentarFiltroFixoTipoOrigem;
	
	private Boolean apresentarFiltroFixoCentroResultado;
	private Boolean apresentarFiltroFixoNivelCentroResultado;
	private Boolean apresentarFiltroFixoCategoriaDespesa;
	
	private Boolean apresentarFiltroFixoFavorecido;
	private Boolean apresentarFiltroFixoSituacaoContaPagar;
	private Boolean apresentarFiltroFixoPeriodoFechamentoMesAno;
	
	//REQUERIMENTO
	private Boolean apresentarFiltroFixoTipoRequerimento;
	private Boolean apresentarFiltroFixoResponsavel;
	private Boolean apresentarFiltroFixoTurmaReposicao;
	private Boolean apresentarFiltroFixoRequerente;
	private Boolean apresentarFiltroFixoCoordenador;
	private Boolean apresentarFiltroFixoDepartamentoResponsavel;
	private Boolean apresentarFiltroFixoSituacaoTramite;
	private Boolean apresentarFiltroFixoSituacaoRequerimento;
	private Boolean apresentarFiltroFixoSituacaoFinanceiraRequerimento;
	
	//BIBLIOTECA
	private Boolean apresentarFiltroFixoBiblioteca;
	private Boolean apresentarFiltroFixoTipoCatalogo;
	private Boolean apresentarFiltroFixoClassificacaoBibliografica;
	private Boolean apresentarFiltroFixoTitulo;
	private Boolean apresentarFiltroFixoSecao;
	private Boolean apresentarFiltroFixoAreaConhecimento;
	private Boolean apresentarFiltroFixoFormaEntrada;
	private Boolean apresentarFiltroFixoDataInicioAquisicao;
	private Boolean apresentarFiltroFixoDataFimAquisicao;
	private Boolean apresentarFiltroFixoTipo;
	private Boolean apresentarFiltroFixoCatalogoPeriodico;
	private Boolean apresentarFiltroFixoSituacaoEmprestimo;
	private Boolean apresentarFiltroFixoDataInicioEmprestimo;
	private Boolean apresentarFiltroFixoDataFimEmprestimo;
	
	//PROCESSO SELETIVO
	private Boolean apresentarFiltroFixoProcessoSeletivo;
	private Boolean apresentarFiltroFixoDataProvaInicio;
	private Boolean apresentarFiltroFixoDataProvaFim;
	private Boolean apresentarFiltroFixoSituacaoFinanceiraProcessoSeletivo;
	private Boolean apresentarFiltroFixoSituacaoResultadoProcessoSeletivo;
	private Boolean apresentarFiltroFixoSituacaoInscricao;
	
	//CRM
	private Boolean apresentarFiltroFixoCampanha;
	private Boolean apresentarFiltroFixoConsultor;
	
	//ESTÁGIO
	private Boolean apresentarFiltroFixoComponenteEstagio;
	private Boolean apresentarFiltroFixoSituacaoEstagio;
	private Boolean apresentarFiltroFixoPeriodoEstagio;

	//Transiente
	private List<LayoutRelatorioSEIDecidirArquivoVO> listaRelatorioSEIDecidirArquivo;
	private Boolean telaLayout;

	/**
	 * Transientes
	 */
	@XmlTransient
	private List<PerfilTagSEIDecidirEnum> tagUtilizadaSelectEnums;	
	@XmlTransient
	private Map<String, PerfilTagSEIDecidirEnum> tagAgrupamentoUtilizada;
	@XmlTransient
	private Map<String, PerfilTagSEIDecidirEnum> tagCondicaoWhereUtilizada;	
	@XmlTransient
	private List<PerfilTagSEIDecidirEnum> tagUtilizadas;
	
	

	// CAMPO DO RELATORIO
	@XmlElement(name = "descricao")
	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@XmlElement(name = "modulo")
	public RelatorioSEIDecidirModuloEnum getModulo() {
		if (modulo == null) {
			modulo = RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA;
		}
		return modulo;
	}
	
	public void setModulo(RelatorioSEIDecidirModuloEnum modulo) {
		this.modulo = modulo;
	}

	public String getModuloApresentar() {
		if (getModulo() != null) {
			return getModulo().getValorApresentar();
		}
		return "";
	}

	@XmlElement(name = "nivelDetalhamento")
	public RelatorioSEIDecidirNivelDetalhamentoEnum getNivelDetalhamento() {
		if (nivelDetalhamento == null) {
			nivelDetalhamento = RelatorioSEIDecidirNivelDetalhamentoEnum.UNIDADE_ENSINO;
		}
		return nivelDetalhamento;
	}

	public void setNivelDetalhamento(RelatorioSEIDecidirNivelDetalhamentoEnum nivelDetalhamento) {
		this.nivelDetalhamento = nivelDetalhamento;
	}
	
	public String getNivelDetalhamentoApresentar() {
		if (getNivelDetalhamento() != null) {
			return getNivelDetalhamento().getValorApresentar();
		}
		return "";
	}

	@XmlTransient
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	@XmlElement(name = "layoutCampo")
	public List<LayoutRelatorioSeiDecidirCampoVO> getLayoutRelatorioSeiDecidirCampoVOs() {
		if (layoutRelatorioSeiDecidirCampoVOs == null) {
			layoutRelatorioSeiDecidirCampoVOs = new ArrayList<LayoutRelatorioSeiDecidirCampoVO>(0);
		}
		return layoutRelatorioSeiDecidirCampoVOs;
	}

	public void setLayoutRelatorioSeiDecidirCampoVOs(List<LayoutRelatorioSeiDecidirCampoVO> layoutRelatorioSeiDecidirCampoVOs) {
		this.layoutRelatorioSeiDecidirCampoVOs = layoutRelatorioSeiDecidirCampoVOs;
	}

	/**
	 * Metodo que retorna o tamanho da lista layoutRelatorioSeiDecidirCampoVOs
	 * usado para alternar as posicoes no LayoutRelatorioSEIDecidirControle
	 */
	public Integer getNumeroOpcoesLayoutRelatorioSeiDecidirCampoVO() {
		return getLayoutRelatorioSeiDecidirCampoVOs().size();
	}
		
	
	@XmlTransient
	public Boolean getApresentarFiltrosAcademicos(){
		return (getCodigo() > 0 || getTelaLayout()) && getModulo().equals(RelatorioSEIDecidirModuloEnum.ACADEMICO);
	}
	
	@XmlTransient
	public Boolean getApresentarFiltrosRequerimento(){
		return (getCodigo() > 0 || getTelaLayout()) && getModulo().equals(RelatorioSEIDecidirModuloEnum.REQUERIMENTO);
	}
	
	@XmlTransient
	public Boolean getApresentarFiltrosFinanceirosReceita(){
		return (getCodigo() > 0 || getTelaLayout()) && (getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA) || getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA));
	}
	
	@XmlTransient
	public Boolean getApresentarFiltrosCentroReceita(){
		return (getCodigo() > 0 || getTelaLayout()) && (getApresentarFiltrosFinanceirosReceita() || getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_CENTRO_RESULTADO));
	}
	
	@XmlTransient
	public Boolean getApresentarFiltrosFechamentoMesReceita(){
		return (getCodigo() > 0 || getTelaLayout()) && getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA);
	}
	
	@XmlTransient
	public Boolean getApresentarFiltrosFinanceirosDespesa(){
		return (getCodigo() > 0 || getTelaLayout()) && (getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA));
	}
	
	@XmlTransient
	public Boolean getApresentarFiltrosCategoriaDespesa(){
		return (getCodigo() > 0 || getTelaLayout()) && (getApresentarFiltrosFinanceirosDespesa() || getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_CENTRO_RESULTADO));
	}
	
	@XmlTransient
	public Boolean getApresentarFiltrosCrm(){
		return (getCodigo() > 0 || getTelaLayout()) && getModulo().equals(RelatorioSEIDecidirModuloEnum.CRM);
	}
	
	@XmlTransient
	public Boolean getApresentarFiltrosCentroResultado(){
		return (getCodigo() > 0 || getTelaLayout()) && getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_CENTRO_RESULTADO);
	}
	
	@XmlTransient
	public Boolean getApresentarFiltrosMatricula(){
		return (getCodigo() > 0 || getTelaLayout()) && (getModulo().equals(RelatorioSEIDecidirModuloEnum.ACADEMICO) || (getModulo().equals(RelatorioSEIDecidirModuloEnum.CRM) && getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.MATRICULA) || getModulo().equals(RelatorioSEIDecidirModuloEnum.ESTAGIO)));
	}
	
	@XmlTransient
	public Boolean getApresentarFiltrosNivelEducacional(){
		return (getCodigo() > 0 || getTelaLayout()) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.CRM) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_CENTRO_RESULTADO) 
				&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.REQUERIMENTO) 
				&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.ADMINISTRATIVO) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.AVALIACAO_INSTITUCIONAL)
				&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.BIBLIOTECA) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.COMPRA) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.CRM)
				&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.NOTA_FISCAL) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.PATRIMONIO) 
				&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.PLANO_ORCAMENTARIO) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.RECURSOS_HUMANOS) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.REQUERIMENTO)
				&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.PROCESSO_SELETIVO);
	}
	
	@XmlTransient
	public Boolean getApresentarFiltrosCurso(){
		return (getCodigo() > 0 || getTelaLayout()) &&  (getModulo().equals(RelatorioSEIDecidirModuloEnum.ACADEMICO) || getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA) 
				|| getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_CENTRO_RESULTADO) || getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA) || getModulo().equals(RelatorioSEIDecidirModuloEnum.PROCESSO_SELETIVO) || getModulo().equals(RelatorioSEIDecidirModuloEnum.ESTAGIO));
	}
	
	@XmlTransient
	public Boolean getApresentarFiltrosTurma(){
		return ((getCodigo() > 0 || getTelaLayout()) || getTelaLayout()) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.CRM) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.REQUERIMENTO) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.ADMINISTRATIVO) 
				&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.AVALIACAO_INSTITUCIONAL) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.BIBLIOTECA) 
				&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.COMPRA) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.CRM)
				&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.NOTA_FISCAL) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.PATRIMONIO) 
				&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.PLANO_ORCAMENTARIO) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.RECURSOS_HUMANOS) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.REQUERIMENTO)
				&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.PROCESSO_SELETIVO);
	}
	
	@XmlTransient
	public Boolean getApresentarFiltrosHistorico(){
		return (getCodigo() > 0 || getTelaLayout()) && getModulo().equals(RelatorioSEIDecidirModuloEnum.ACADEMICO) && getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.HISTORICO);
	}
	
	@XmlTransient
	public Boolean getApresentarFiltrosTurno(){
		return (getCodigo() > 0 || getTelaLayout()) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.CRM)  && !getModulo().equals(RelatorioSEIDecidirModuloEnum.REQUERIMENTO) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.ADMINISTRATIVO) 
				&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.AVALIACAO_INSTITUCIONAL) 
				&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.COMPRA) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.CRM) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.BIBLIOTECA)
				&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.NOTA_FISCAL) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.PATRIMONIO) 
				&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.PLANO_ORCAMENTARIO) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.RECURSOS_HUMANOS) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.REQUERIMENTO)
				&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.PROCESSO_SELETIVO);
	}
	
	@XmlTransient
	public Boolean getApresentarFiltrosSituacaoCompromisso(){
		return ((getCodigo() > 0 || getTelaLayout()) || getTelaLayout()) && getModulo().equals(RelatorioSEIDecidirModuloEnum.CRM) && (getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.INTERACOES_WORKFLOW) || getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.COMPROMISSO));
	}
	
	@XmlTransient
	public Boolean getApresentarFiltrosSituacaoAcademica(){
		return (getCodigo() > 0 || getTelaLayout()) 
				&& ((!getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.CRM) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_CENTRO_RESULTADO) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.BIBLIOTECA)
						&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.ADMINISTRATIVO)
						&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.AVALIACAO_INSTITUCIONAL)
						&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.COMPRA) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.CRM)
						&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.NOTA_FISCAL) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.PATRIMONIO) 
						&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.PLANO_ORCAMENTARIO) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.RECURSOS_HUMANOS) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.REQUERIMENTO)
						&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.PROCESSO_SELETIVO)
						) 
				|| (getModulo().equals(RelatorioSEIDecidirModuloEnum.CRM) && getNivelDetalhamento().equals(RelatorioSEIDecidirNivelDetalhamentoEnum.MATRICULA)));
	}
	
	@XmlTransient
	public Boolean getApresentarFiltrosSituacaoFinanceiraMatricula(){
		return (getCodigo() > 0 || getTelaLayout()) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.CRM) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.FINANCEIRO_CENTRO_RESULTADO)
				&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.ADMINISTRATIVO) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.AVALIACAO_INSTITUCIONAL)
		&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.COMPRA) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.CRM) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.BIBLIOTECA)
		&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.ESTAGIO) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.NOTA_FISCAL) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.PATRIMONIO) 
		&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.PLANO_ORCAMENTARIO) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.RECURSOS_HUMANOS) && !getModulo().equals(RelatorioSEIDecidirModuloEnum.REQUERIMENTO)
		&& !getModulo().equals(RelatorioSEIDecidirModuloEnum.PROCESSO_SELETIVO)  && !getModulo().equals(RelatorioSEIDecidirModuloEnum.ESTAGIO);
	}
	
	@XmlTransient
	public Boolean getApresentarFiltrosBiblioteca(){
		return (getCodigo() > 0 || getTelaLayout()) && getModulo().equals(RelatorioSEIDecidirModuloEnum.BIBLIOTECA);
	}
	
	@XmlTransient
	public Boolean getApresentarFiltrosProcessoSeletivo(){
		return (getCodigo() > 0 || getTelaLayout()) && getModulo().equals(RelatorioSEIDecidirModuloEnum.PROCESSO_SELETIVO);
	}

	@XmlElement(name = "agruparRelatorioPor")
	public String getAgruparRelatorioPor() {
		if (agruparRelatorioPor == null) {
			agruparRelatorioPor = "";
		}
		return agruparRelatorioPor;
	}

	public void setAgruparRelatorioPor(String agruparRelatorioPor) {
		this.agruparRelatorioPor = agruparRelatorioPor;
	}

	@XmlTransient
	public Map<String, PerfilTagSEIDecidirEnum> getTagAgrupamentoUtilizada() {
		if (tagAgrupamentoUtilizada == null) {
			tagAgrupamentoUtilizada = new HashMap<String, PerfilTagSEIDecidirEnum>(0);
		}
		return tagAgrupamentoUtilizada;
	}

	public void setTagAgrupamentoUtilizada(Map<String, PerfilTagSEIDecidirEnum> tagAgrupamento) {
		this.tagAgrupamentoUtilizada = tagAgrupamento;
	}

	/**
	 * @return the condicaoWhereAdicional
	 */
	@XmlElement(name = "condicaoWhereAdicional")
	public String getCondicaoWhereAdicional() {
		if (condicaoWhereAdicional == null) {
			condicaoWhereAdicional = "";
		}
		return condicaoWhereAdicional;
	}

	/**
	 * @param condicaoWhereAdicional the condicaoWhereAdicional to set
	 */
	public void setCondicaoWhereAdicional(String condicaoWhereAdicional) {
		this.condicaoWhereAdicional = condicaoWhereAdicional;
	}

	/**
	 * @return the tagCondicaoWhereUtilizada
	 */
	public Map<String, PerfilTagSEIDecidirEnum> getTagCondicaoWhereUtilizada() {
		if (tagCondicaoWhereUtilizada == null) {
			tagCondicaoWhereUtilizada = new HashMap<String, PerfilTagSEIDecidirEnum>(0);
		}
		return tagCondicaoWhereUtilizada;
	}

	/**
	 * @param tagCondicaoWhereUtilizada the tagCondicaoWhereUtilizada to set
	 */
	public void setTagCondicaoWhereUtilizada(Map<String, PerfilTagSEIDecidirEnum> tagCondicaoWhereUtilizada) {
		this.tagCondicaoWhereUtilizada = tagCondicaoWhereUtilizada;
	}

	/**
	 * @return the tagUtilizadaSelectEnums
	 */
	public List<PerfilTagSEIDecidirEnum> getTagUtilizadaSelectEnums() {
		if (tagUtilizadaSelectEnums == null) {
			tagUtilizadaSelectEnums = new ArrayList<PerfilTagSEIDecidirEnum>(0);
		}
		return tagUtilizadaSelectEnums;
	}

	/**
	 * @param tagUtilizadaSelectEnums the tagUtilizadaSelectEnums to set
	 */
	public void setTagUtilizadaSelectEnums(List<PerfilTagSEIDecidirEnum> tagUtilizadaSelectEnums) {
		this.tagUtilizadaSelectEnums = tagUtilizadaSelectEnums;
	}

	/**
	 * @return the tagUtilizadas
	 */
	public List<PerfilTagSEIDecidirEnum> getTagUtilizadas() {
		if (tagUtilizadas == null) {
			tagUtilizadas = new ArrayList<PerfilTagSEIDecidirEnum>(0);
		}
		return tagUtilizadas;
	}

	/**
	 * @param tagUtilizadas the tagUtilizadas to set
	 */
	public void setTagUtilizadas(List<PerfilTagSEIDecidirEnum> tagUtilizadas) {
		this.tagUtilizadas = tagUtilizadas;
	}

	@XmlElement(name = "groupByAdicional")
	public String getGroupByAdicional() {
		if(groupByAdicional == null){
			groupByAdicional = "";
		}
		return groupByAdicional;
	}

	public void setGroupByAdicional(String groupByAdicional) {
		this.groupByAdicional = groupByAdicional;
	}

	@XmlElement(name = "nomePadraoArquivo")
	public String getNomePadraoArquivo() {
		if(nomePadraoArquivo == null){
			nomePadraoArquivo = "";
		}
		return nomePadraoArquivo;
	}

	public void setNomePadraoArquivo(String nomePadraoArquivo) {
		this.nomePadraoArquivo = nomePadraoArquivo;
	}

	@XmlElement(name = "tipoGeracaoRelatorio")
	public TipoRelatorioEnum getTipoGeracaoRelatorio() {
		if(tipoGeracaoRelatorio == null){
			tipoGeracaoRelatorio = TipoRelatorioEnum.EXCEL;
		}
		return tipoGeracaoRelatorio;
	}

	public void setTipoGeracaoRelatorio(TipoRelatorioEnum tipoGeracaoRelatorio) {
		this.tipoGeracaoRelatorio = tipoGeracaoRelatorio;
	}

	@XmlElement(name = "tagAberturaGeralXml")
	public String getTagAberturaGeralXml() {
		if(tagAberturaGeralXml == null){
			tagAberturaGeralXml = "";
		}
		return tagAberturaGeralXml;
	}

	public void setTagAberturaGeralXml(String tagAberturaGeralXml) {
		this.tagAberturaGeralXml = tagAberturaGeralXml;
	}

	@XmlElement(name = "tagFechamentoGeralXml")
	public String getTagFechamentoGeralXml() {
		if(tagFechamentoGeralXml == null){
			tagFechamentoGeralXml = "";
		}
		return tagFechamentoGeralXml;
	}

	public void setTagFechamentoGeralXml(String tagFechamentoGeralXml) {
		this.tagFechamentoGeralXml = tagFechamentoGeralXml;
	}

	@XmlElement(name = "tagAberturaLinhaXml")
	public String getTagAberturaLinhaXml() {
		if(tagAberturaLinhaXml == null){
			tagAberturaLinhaXml = "";
		}
		return tagAberturaLinhaXml;
	}

	public void setTagAberturaLinhaXml(String tagAberturaLinhaXml) {
		this.tagAberturaLinhaXml = tagAberturaLinhaXml;
	}

	@XmlElement(name = "tagFechamentoLinhaXml")
	public String getTagFechamentoLinhaXml() {
		if(tagFechamentoLinhaXml == null){
			tagFechamentoLinhaXml = "";
		}
		return tagFechamentoLinhaXml;
	}

	public void setTagFechamentoLinhaXml(String tagFechamentoLinhaXml) {
		this.tagFechamentoLinhaXml = tagFechamentoLinhaXml;
	}

	public List<LayoutRelatorioSEIDecidirArquivoVO> getListaRelatorioSEIDecidirArquivo() {
		if (listaRelatorioSEIDecidirArquivo == null) {
			listaRelatorioSEIDecidirArquivo = new ArrayList<>();
		}
		return listaRelatorioSEIDecidirArquivo;
	}

	public void setListaRelatorioSEIDecidirArquivo(List<LayoutRelatorioSEIDecidirArquivoVO> listaRelatorioSEIDecidirArquivo) {
		this.listaRelatorioSEIDecidirArquivo = listaRelatorioSEIDecidirArquivo;
	}

	@XmlElement(name = "subRelatorio")
	public Boolean getSubRelatorio() {
		if (subRelatorio == null) {
			subRelatorio = Boolean.FALSE;
		}
		return subRelatorio;
	}

	public void setSubRelatorio(Boolean subRelatorio) {
		this.subRelatorio = subRelatorio;
	}

	@XmlElement(name = "subRelatorioCrossTab")
	public Boolean getSubRelatorioCrossTab() {
		if (subRelatorioCrossTab == null) {
			subRelatorioCrossTab = Boolean.FALSE;
		}
		return subRelatorioCrossTab;
	}

	public void setSubRelatorioCrossTab(Boolean subRelatorioCrossTab) {
		this.subRelatorioCrossTab = subRelatorioCrossTab;
	}
	@XmlElement(name = "textoCabecalho")
	public String getTextoCabecalho() {
		if (textoCabecalho == null) {
			textoCabecalho = "";
		}
		return textoCabecalho;
	}

	public void setTextoCabecalho(String textoCabecalho) {
		this.textoCabecalho = textoCabecalho;
	}
	
	@XmlElement(name = "gerarLinhaEmBranco")
	public Boolean getGerarLinhaEmBranco() {
		if (gerarLinhaEmBranco == null) {
			gerarLinhaEmBranco = Boolean.FALSE;
		}
		return gerarLinhaEmBranco;
	}
	
	public void setGerarLinhaEmBranco(Boolean gerarLinhaEmBranco) {
		this.gerarLinhaEmBranco = gerarLinhaEmBranco;
	}
	
	public Boolean getApresentarFiltroPeriodoPor() {
		return getApresentarFiltrosAcademicos() || getApresentarFiltrosFinanceirosReceita() || getApresentarFiltrosFinanceirosDespesa() || getApresentarFiltrosFechamentoMesReceita();
	}
	
	
	@XmlElement(name = "condicaoJoinAdicional")
	public String getCondicaoJoinAdicional() {
		if (condicaoJoinAdicional == null) {
			condicaoJoinAdicional = "";
		}
		return condicaoJoinAdicional;
	}
	
	
	@XmlElement(name = "orderByAdicional")
	public String getOrderByAdicional() {
		return orderByAdicional;
	}

	public void setOrderByAdicional(String orderByAdicional) {
		this.orderByAdicional = orderByAdicional;
	}
	
	public Boolean getApresentarFiltroDocumentoAssinado() {
		return getApresentarFiltrosAcademicos();
	}
	
	public void setCondicaoJoinAdicional(String condicaoJoinAdicional) {
		this.condicaoJoinAdicional = condicaoJoinAdicional;
	}
	

	@XmlTransient
	public Boolean getApresentarFiltroComponenteEstagio(){
		return (getCodigo() > 0 || getTelaLayout()) && getModulo().equals(RelatorioSEIDecidirModuloEnum.ESTAGIO);
	}

	@XmlTransient
	public Boolean getApresentarFiltroSituacaoEstagio(){
		return (getCodigo() > 0 || getTelaLayout()) && getModulo().equals(RelatorioSEIDecidirModuloEnum.ESTAGIO);
	}

	@XmlTransient
	public Boolean getApresentarFiltroPeriodosEstagio(){
		return (getCodigo() > 0 || getTelaLayout()) && getModulo().equals(RelatorioSEIDecidirModuloEnum.ESTAGIO);
	}

	public List<FiltroPersonalizadoVO> getListaFiltroPersonalizadoVOs() {
		if (listaFiltroPersonalizadoVOs == null) {
			listaFiltroPersonalizadoVOs = new ArrayList<FiltroPersonalizadoVO>(0);
		}
		return listaFiltroPersonalizadoVOs;
	}

	public void setListaFiltroPersonalizadoVOs(List<FiltroPersonalizadoVO> listaFiltroPersonalizadoVOs) {
		this.listaFiltroPersonalizadoVOs = listaFiltroPersonalizadoVOs;
	}
	
	public Integer getNumeroOpcoes(){
		return getListaFiltroPersonalizadoVOs().size();
	}

	public Boolean getLayoutPersonalizado() {
		if (layoutPersonalizado == null) {
			layoutPersonalizado = false;
		}
		return layoutPersonalizado;
	}

	public void setLayoutPersonalizado(Boolean layoutPersonalizado) {
		this.layoutPersonalizado = layoutPersonalizado;
	}
	
	public Boolean getApresentarLayoutPersonalizado() {
		return getLayoutPersonalizado();
	}

	public String getQueryLayoutPersonalizado() {
		if (queryLayoutPersonalizado == null) {
			queryLayoutPersonalizado = "";
		}
		return queryLayoutPersonalizado;
	}

	public void setQueryLayoutPersonalizado(String queryLayoutPersonalizado) {
		this.queryLayoutPersonalizado = queryLayoutPersonalizado;
	}

	public Boolean getApresentarFiltroFixoUnidadeEnsino() {
		if (apresentarFiltroFixoUnidadeEnsino == null) {
			apresentarFiltroFixoUnidadeEnsino = true;
		}
		return apresentarFiltroFixoUnidadeEnsino;
	}

	public void setApresentarFiltroFixoUnidadeEnsino(Boolean apresentarFiltroFixoUnidadeEnsino) {
		this.apresentarFiltroFixoUnidadeEnsino = apresentarFiltroFixoUnidadeEnsino;
	}

	public Boolean getApresentarFiltroFixoNivelEducacional() {
		if (apresentarFiltroFixoNivelEducacional == null) {
			apresentarFiltroFixoNivelEducacional = true;
		}
		return apresentarFiltroFixoNivelEducacional;
	}

	public void setApresentarFiltroFixoNivelEducacional(Boolean apresentarFiltroFixoNivelEducacional) {
		this.apresentarFiltroFixoNivelEducacional = apresentarFiltroFixoNivelEducacional;
	}

	public Boolean getApresentarFiltroFixoCotaIngresso() {
		if (apresentarFiltroFixoCotaIngresso == null) {
			apresentarFiltroFixoCotaIngresso = true;
		}
		return apresentarFiltroFixoCotaIngresso;
	}

	public void setApresentarFiltroFixoCotaIngresso(Boolean apresentarFiltroFixoCotaIngresso) {
		this.apresentarFiltroFixoCotaIngresso = apresentarFiltroFixoCotaIngresso;
	}

	public Boolean getApresentarFiltroFixoCurso() {
		if (apresentarFiltroFixoCurso == null) {
			apresentarFiltroFixoCurso = true;
		}
		return apresentarFiltroFixoCurso;
	}

	public void setApresentarFiltroFixoCurso(Boolean apresentarFiltroFixoCurso) {
		this.apresentarFiltroFixoCurso = apresentarFiltroFixoCurso;
	}

	public Boolean getApresentarFiltroFixoTurno() {
		if (apresentarFiltroFixoTurno == null) {
			apresentarFiltroFixoTurno = true;
		}
		return apresentarFiltroFixoTurno;
	}

	public void setApresentarFiltroFixoTurno(Boolean apresentarFiltroFixoTurno) {
		this.apresentarFiltroFixoTurno = apresentarFiltroFixoTurno;
	}

	public Boolean getApresentarFiltroFixoTurma() {
		if (apresentarFiltroFixoTurma == null) {
			apresentarFiltroFixoTurma = true;
		}
		return apresentarFiltroFixoTurma;
	}

	public void setApresentarFiltroFixoTurma(Boolean apresentarFiltroFixoTurma) {
		this.apresentarFiltroFixoTurma = apresentarFiltroFixoTurma;
	}

	public Boolean getApresentarFiltroFixoMatricula() {
		if (apresentarFiltroFixoMatricula == null) {
			apresentarFiltroFixoMatricula = true;
		}
		return apresentarFiltroFixoMatricula;
	}

	public void setApresentarFiltroFixoMatricula(Boolean apresentarFiltroFixoMatricula) {
		this.apresentarFiltroFixoMatricula = apresentarFiltroFixoMatricula;
	}

	public Boolean getApresentarFiltroFixoTurmaEstudouDisciplina() {
		if (apresentarFiltroFixoTurmaEstudouDisciplina == null) {
			apresentarFiltroFixoTurmaEstudouDisciplina = true;
		}
		return apresentarFiltroFixoTurmaEstudouDisciplina;
	}

	public void setApresentarFiltroFixoTurmaEstudouDisciplina(Boolean apresentarFiltroFixoTurmaEstudouDisciplina) {
		this.apresentarFiltroFixoTurmaEstudouDisciplina = apresentarFiltroFixoTurmaEstudouDisciplina;
	}

	public Boolean getApresentarFiltroFixoDisciplina() {
		if (apresentarFiltroFixoDisciplina == null) {
			apresentarFiltroFixoDisciplina = true;
		}
		return apresentarFiltroFixoDisciplina;
	}

	public void setApresentarFiltroFixoDisciplina(Boolean apresentarFiltroFixoDisciplina) {
		this.apresentarFiltroFixoDisciplina = apresentarFiltroFixoDisciplina;
	}

	public Boolean getApresentarFiltroFixoFiltroPeriodo() {
		if (apresentarFiltroFixoFiltroPeriodo == null) {
			apresentarFiltroFixoFiltroPeriodo = true;
		}
		return apresentarFiltroFixoFiltroPeriodo;
	}

	public void setApresentarFiltroFixoFiltroPeriodo(Boolean apresentarFiltroFixoFiltroPeriodo) {
		this.apresentarFiltroFixoFiltroPeriodo = apresentarFiltroFixoFiltroPeriodo;
	}

	public Boolean getApresentarFiltroFixoPeriodoLetivo() {
		if (apresentarFiltroFixoPeriodoLetivo == null) {
			apresentarFiltroFixoPeriodoLetivo = true;
		}
		return apresentarFiltroFixoPeriodoLetivo;
	}

	public void setApresentarFiltroFixoPeriodoLetivo(Boolean apresentarFiltroFixoPeriodoLetivo) {
		this.apresentarFiltroFixoPeriodoLetivo = apresentarFiltroFixoPeriodoLetivo;
	}

	public Boolean getApresentarFiltroFixoSituacaoAceiteEletronicoContrato() {
		if (apresentarFiltroFixoSituacaoAceiteEletronicoContrato == null) {
			apresentarFiltroFixoSituacaoAceiteEletronicoContrato = true;
		}
		return apresentarFiltroFixoSituacaoAceiteEletronicoContrato;
	}

	public void setApresentarFiltroFixoSituacaoAceiteEletronicoContrato(
			Boolean apresentarFiltroFixoSituacaoAceiteEletronicoContrato) {
		this.apresentarFiltroFixoSituacaoAceiteEletronicoContrato = apresentarFiltroFixoSituacaoAceiteEletronicoContrato;
	}

	public Boolean getApresentarFiltroFixoSituacaoFiltroSituacaoAcademica() {
		if (apresentarFiltroFixoSituacaoFiltroSituacaoAcademica == null) {
			apresentarFiltroFixoSituacaoFiltroSituacaoAcademica = true;
		}
		return apresentarFiltroFixoSituacaoFiltroSituacaoAcademica;
	}

	public void setApresentarFiltroFixoSituacaoFiltroSituacaoAcademica(
			Boolean apresentarFiltroFixoSituacaoFiltroSituacaoAcademica) {
		this.apresentarFiltroFixoSituacaoFiltroSituacaoAcademica = apresentarFiltroFixoSituacaoFiltroSituacaoAcademica;
	}


	public Boolean getApresentarFiltroFixoSituacaoFinanceiraMatricula() {
		if (apresentarFiltroFixoSituacaoFinanceiraMatricula == null) {
			apresentarFiltroFixoSituacaoFinanceiraMatricula = true;
		}
		return apresentarFiltroFixoSituacaoFinanceiraMatricula;
	}

	public void setApresentarFiltroFixoSituacaoFinanceiraMatricula(
			Boolean apresentarFiltroFixoSituacaoFinanceiraMatricula) {
		this.apresentarFiltroFixoSituacaoFinanceiraMatricula = apresentarFiltroFixoSituacaoFinanceiraMatricula;
	}

	public Boolean getApresentarFiltroFixoCentroReceita() {
		if (apresentarFiltroFixoCentroReceita == null) {
			apresentarFiltroFixoCentroReceita = true;
		}
		return apresentarFiltroFixoCentroReceita;
	}

	public void setApresentarFiltroFixoCentroReceita(Boolean apresentarFiltroFixoCentroReceita) {
		this.apresentarFiltroFixoCentroReceita = apresentarFiltroFixoCentroReceita;
	}

	public Boolean getApresentarFiltroFixoFormaPagamento() {
		if (apresentarFiltroFixoFormaPagamento == null) {
			apresentarFiltroFixoFormaPagamento = true;
		}
		return apresentarFiltroFixoFormaPagamento;
	}

	public void setApresentarFiltroFixoFormaPagamento(Boolean apresentarFiltroFixoFormaPagamento) {
		this.apresentarFiltroFixoFormaPagamento = apresentarFiltroFixoFormaPagamento;
	}

	public Boolean getApresentarFiltroFixoContaCorrente() {
		if (apresentarFiltroFixoContaCorrente == null) {
			apresentarFiltroFixoContaCorrente = true;
		}
		return apresentarFiltroFixoContaCorrente;
	}

	public void setApresentarFiltroFixoContaCorrente(Boolean apresentarFiltroFixoContaCorrente) {
		this.apresentarFiltroFixoContaCorrente = apresentarFiltroFixoContaCorrente;
	}

	public Boolean getApresentarFiltroFixoTipoPessoa() {
		if (apresentarFiltroFixoTipoPessoa == null) {
			apresentarFiltroFixoTipoPessoa = true;
		}
		return apresentarFiltroFixoTipoPessoa;
	}

	public void setApresentarFiltroFixoTipoPessoa(Boolean apresentarFiltroFixoTipoPessoa) {
		this.apresentarFiltroFixoTipoPessoa = apresentarFiltroFixoTipoPessoa;
	}

	public Boolean getApresentarFiltroFixoConsiderarUnidadeFinanceira() {
		if (apresentarFiltroFixoConsiderarUnidadeFinanceira == null) {
			apresentarFiltroFixoConsiderarUnidadeFinanceira = true;
		}
		return apresentarFiltroFixoConsiderarUnidadeFinanceira;
	}

	public void setApresentarFiltroFixoConsiderarUnidadeFinanceira(
			Boolean apresentarFiltroFixoConsiderarUnidadeFinanceira) {
		this.apresentarFiltroFixoConsiderarUnidadeFinanceira = apresentarFiltroFixoConsiderarUnidadeFinanceira;
	}

	public Boolean getApresentarFiltroFixoContaCorrenteRecebimento() {
		if (apresentarFiltroFixoContaCorrenteRecebimento == null) {
			apresentarFiltroFixoContaCorrenteRecebimento = true;
		}
		return apresentarFiltroFixoContaCorrenteRecebimento;
	}

	public void setApresentarFiltroFixoContaCorrenteRecebimento(Boolean apresentarFiltroFixoContaCorrenteRecebimento) {
		this.apresentarFiltroFixoContaCorrenteRecebimento = apresentarFiltroFixoContaCorrenteRecebimento;
	}

	public Boolean getApresentarFiltroFixoSituacaoContaReceber() {
		if (apresentarFiltroFixoSituacaoContaReceber == null) {
			apresentarFiltroFixoSituacaoContaReceber = true;
		}
		return apresentarFiltroFixoSituacaoContaReceber;
	}

	public void setApresentarFiltroFixoSituacaoContaReceber(Boolean apresentarFiltroFixoSituacaoContaReceber) {
		this.apresentarFiltroFixoSituacaoContaReceber = apresentarFiltroFixoSituacaoContaReceber;
	}

	public Boolean getApresentarFiltroFixoTipoOrigem() {
		if (apresentarFiltroFixoTipoOrigem == null) {
			apresentarFiltroFixoTipoOrigem = true;
		}
		return apresentarFiltroFixoTipoOrigem;
	}

	public void setApresentarFiltroFixoTipoOrigem(Boolean apresentarFiltroFixoTipoOrigem) {
		this.apresentarFiltroFixoTipoOrigem = apresentarFiltroFixoTipoOrigem;
	}

	public Boolean getApresentarFiltroFixoPeriodoLetivoCentroResultado() {
		if (apresentarFiltroFixoPeriodoLetivoCentroResultado == null) {
			apresentarFiltroFixoPeriodoLetivoCentroResultado = true;
		}
		return apresentarFiltroFixoPeriodoLetivoCentroResultado;
	}

	public void setApresentarFiltroFixoPeriodoLetivoCentroResultado(
			Boolean apresentarFiltroFixoPeriodoLetivoCentroResultado) {
		this.apresentarFiltroFixoPeriodoLetivoCentroResultado = apresentarFiltroFixoPeriodoLetivoCentroResultado;
	}

	public Boolean getTelaLayout() {
		if (telaLayout == null) {
			telaLayout = false;
		}
		return telaLayout;
	}

	public void setTelaLayout(Boolean telaLayout) {
		this.telaLayout = telaLayout;
	}

	public Boolean getApresentarFiltroFixoCentroResultado() {
		if (apresentarFiltroFixoCentroResultado == null) {
			apresentarFiltroFixoCentroResultado = true;
		}
		return apresentarFiltroFixoCentroResultado;
	}

	public void setApresentarFiltroFixoCentroResultado(Boolean apresentarFiltroFixoCentroResultado) {
		this.apresentarFiltroFixoCentroResultado = apresentarFiltroFixoCentroResultado;
	}

	public Boolean getApresentarFiltroFixoNivelCentroResultado() {
		if (apresentarFiltroFixoNivelCentroResultado == null) {
			apresentarFiltroFixoNivelCentroResultado = true;
		}
		return apresentarFiltroFixoNivelCentroResultado;
	}

	public void setApresentarFiltroFixoNivelCentroResultado(Boolean apresentarFiltroFixoNivelCentroResultado) {
		this.apresentarFiltroFixoNivelCentroResultado = apresentarFiltroFixoNivelCentroResultado;
	}

	public Boolean getApresentarFiltroFixoCategoriaDespesa() {
		if (apresentarFiltroFixoCategoriaDespesa == null) {
			apresentarFiltroFixoCategoriaDespesa = true;
		}
		return apresentarFiltroFixoCategoriaDespesa;
	}

	public void setApresentarFiltroFixoCategoriaDespesa(Boolean apresentarFiltroFixoCategoriaDespesa) {
		this.apresentarFiltroFixoCategoriaDespesa = apresentarFiltroFixoCategoriaDespesa;
	}

	public Boolean getApresentarFiltroFixoFavorecido() {
		if (apresentarFiltroFixoFavorecido == null) {
			apresentarFiltroFixoFavorecido = true;
		}
		return apresentarFiltroFixoFavorecido;
	}

	public void setApresentarFiltroFixoFavorecido(Boolean apresentarFiltroFixoFavorecido) {
		this.apresentarFiltroFixoFavorecido = apresentarFiltroFixoFavorecido;
	}

	public Boolean getApresentarFiltroFixoSituacaoContaPagar() {
		if (apresentarFiltroFixoSituacaoContaPagar == null) {
			apresentarFiltroFixoSituacaoContaPagar = true;
		}
		return apresentarFiltroFixoSituacaoContaPagar;
	}

	public void setApresentarFiltroFixoSituacaoContaPagar(Boolean apresentarFiltroFixoSituacaoContaPagar) {
		this.apresentarFiltroFixoSituacaoContaPagar = apresentarFiltroFixoSituacaoContaPagar;
	}

	public Boolean getApresentarFiltroFixoPeriodoFechamentoMesAno() {
		if (apresentarFiltroFixoPeriodoFechamentoMesAno == null) {
			apresentarFiltroFixoPeriodoFechamentoMesAno = true;
		}
		return apresentarFiltroFixoPeriodoFechamentoMesAno;
	}

	public void setApresentarFiltroFixoPeriodoFechamentoMesAno(Boolean apresentarFiltroFixoPeriodoFechamentoMesAno) {
		this.apresentarFiltroFixoPeriodoFechamentoMesAno = apresentarFiltroFixoPeriodoFechamentoMesAno;
	}

	public Boolean getApresentarFiltroFixoTipoRequerimento() {
		if (apresentarFiltroFixoTipoRequerimento == null) {
			apresentarFiltroFixoTipoRequerimento = true;
		}
		return apresentarFiltroFixoTipoRequerimento;
	}

	public void setApresentarFiltroFixoTipoRequerimento(Boolean apresentarFiltroFixoTipoRequerimento) {
		this.apresentarFiltroFixoTipoRequerimento = apresentarFiltroFixoTipoRequerimento;
	}

	public Boolean getApresentarFiltroFixoResponsavel() {
		if (apresentarFiltroFixoResponsavel == null) {
			apresentarFiltroFixoResponsavel = true;
		}
		return apresentarFiltroFixoResponsavel;
	}

	public void setApresentarFiltroFixoResponsavel(Boolean apresentarFiltroFixoResponsavel) {
		this.apresentarFiltroFixoResponsavel = apresentarFiltroFixoResponsavel;
	}

	public Boolean getApresentarFiltroFixoTurmaReposicao() {
		if (apresentarFiltroFixoTurmaReposicao == null) {
			apresentarFiltroFixoTurmaReposicao = true;
		}
		return apresentarFiltroFixoTurmaReposicao;
	}

	public void setApresentarFiltroFixoTurmaReposicao(Boolean apresentarFiltroFixoTurmaReposicao) {
		this.apresentarFiltroFixoTurmaReposicao = apresentarFiltroFixoTurmaReposicao;
	}

	public Boolean getApresentarFiltroFixoRequerente() {
		if (apresentarFiltroFixoRequerente == null) {
			apresentarFiltroFixoRequerente = true;
		}
		return apresentarFiltroFixoRequerente;
	}

	public void setApresentarFiltroFixoRequerente(Boolean apresentarFiltroFixoRequerente) {
		this.apresentarFiltroFixoRequerente = apresentarFiltroFixoRequerente;
	}

	public Boolean getApresentarFiltroFixoCoordenador() {
		if (apresentarFiltroFixoCoordenador == null) {
			apresentarFiltroFixoCoordenador = true;
		}
		return apresentarFiltroFixoCoordenador;
	}

	public void setApresentarFiltroFixoCoordenador(Boolean apresentarFiltroFixoCoordenador) {
		this.apresentarFiltroFixoCoordenador = apresentarFiltroFixoCoordenador;
	}

	public Boolean getApresentarFiltroFixoDepartamentoResponsavel() {
		if (apresentarFiltroFixoDepartamentoResponsavel == null) {
			apresentarFiltroFixoDepartamentoResponsavel = true;
		}
		return apresentarFiltroFixoDepartamentoResponsavel;
	}

	public void setApresentarFiltroFixoDepartamentoResponsavel(Boolean apresentarFiltroFixoDepartamentoResponsavel) {
		this.apresentarFiltroFixoDepartamentoResponsavel = apresentarFiltroFixoDepartamentoResponsavel;
	}

	public Boolean getApresentarFiltroFixoSituacaoTramite() {
		if (apresentarFiltroFixoSituacaoTramite == null) {
			apresentarFiltroFixoSituacaoTramite = true;
		}
		return apresentarFiltroFixoSituacaoTramite;
	}

	public void setApresentarFiltroFixoSituacaoTramite(Boolean apresentarFiltroFixoSituacaoTramite) {
		this.apresentarFiltroFixoSituacaoTramite = apresentarFiltroFixoSituacaoTramite;
	}

	public Boolean getApresentarFiltroFixoSituacaoRequerimento() {
		if (apresentarFiltroFixoSituacaoRequerimento == null) {
			apresentarFiltroFixoSituacaoRequerimento = true;
		}
		return apresentarFiltroFixoSituacaoRequerimento;
	}

	public void setApresentarFiltroFixoSituacaoRequerimento(Boolean apresentarFiltroFixoSituacaoRequerimento) {
		this.apresentarFiltroFixoSituacaoRequerimento = apresentarFiltroFixoSituacaoRequerimento;
	}

	public Boolean getApresentarFiltroFixoSituacaoFinanceiraRequerimento() {
		if (apresentarFiltroFixoSituacaoFinanceiraRequerimento == null) {
			apresentarFiltroFixoSituacaoFinanceiraRequerimento = true;
		}
		return apresentarFiltroFixoSituacaoFinanceiraRequerimento;
	}

	public void setApresentarFiltroFixoSituacaoFinanceiraRequerimento(
			Boolean apresentarFiltroFixoSituacaoFinanceiraRequerimento) {
		this.apresentarFiltroFixoSituacaoFinanceiraRequerimento = apresentarFiltroFixoSituacaoFinanceiraRequerimento;
	}

	public Boolean getApresentarFiltroFixoBiblioteca() {
		if (apresentarFiltroFixoBiblioteca == null) {
			apresentarFiltroFixoBiblioteca = true;
		}
		return apresentarFiltroFixoBiblioteca;
	}

	public void setApresentarFiltroFixoBiblioteca(Boolean apresentarFiltroFixoBiblioteca) {
		this.apresentarFiltroFixoBiblioteca = apresentarFiltroFixoBiblioteca;
	}

	public Boolean getApresentarFiltroFixoTipoCatalogo() {
		if (apresentarFiltroFixoTipoCatalogo == null) {
			apresentarFiltroFixoTipoCatalogo = true;
		}
		return apresentarFiltroFixoTipoCatalogo;
	}

	public void setApresentarFiltroFixoTipoCatalogo(Boolean apresentarFiltroFixoTipoCatalogo) {
		this.apresentarFiltroFixoTipoCatalogo = apresentarFiltroFixoTipoCatalogo;
	}

	public Boolean getApresentarFiltroFixoClassificacaoBibliografica() {
		if (apresentarFiltroFixoClassificacaoBibliografica == null) {
			apresentarFiltroFixoClassificacaoBibliografica = true;
		}
		return apresentarFiltroFixoClassificacaoBibliografica;
	}

	public void setApresentarFiltroFixoClassificacaoBibliografica(Boolean apresentarFiltroFixoClassificacaoBibliografica) {
		this.apresentarFiltroFixoClassificacaoBibliografica = apresentarFiltroFixoClassificacaoBibliografica;
	}

	public Boolean getApresentarFiltroFixoTitulo() {
		if (apresentarFiltroFixoTitulo == null) {
			apresentarFiltroFixoTitulo = true;
		}
		return apresentarFiltroFixoTitulo;
	}

	public void setApresentarFiltroFixoTitulo(Boolean apresentarFiltroFixoTitulo) {
		this.apresentarFiltroFixoTitulo = apresentarFiltroFixoTitulo;
	}

	public Boolean getApresentarFiltroFixoSecao() {
		if (apresentarFiltroFixoSecao == null) {
			apresentarFiltroFixoSecao = true;
		}
		return apresentarFiltroFixoSecao;
	}

	public void setApresentarFiltroFixoSecao(Boolean apresentarFiltroFixoSecao) {
		this.apresentarFiltroFixoSecao = apresentarFiltroFixoSecao;
	}

	public Boolean getApresentarFiltroFixoAreaConhecimento() {
		if (apresentarFiltroFixoAreaConhecimento == null) {
			apresentarFiltroFixoAreaConhecimento = true;
		}
		return apresentarFiltroFixoAreaConhecimento;
	}

	public void setApresentarFiltroFixoAreaConhecimento(Boolean apresentarFiltroFixoAreaConhecimento) {
		this.apresentarFiltroFixoAreaConhecimento = apresentarFiltroFixoAreaConhecimento;
	}

	public Boolean getApresentarFiltroFixoFormaEntrada() {
		if (apresentarFiltroFixoFormaEntrada == null) {
			apresentarFiltroFixoFormaEntrada = true;
		}
		return apresentarFiltroFixoFormaEntrada;
	}

	public void setApresentarFiltroFixoFormaEntrada(Boolean apresentarFiltroFixoFormaEntrada) {
		this.apresentarFiltroFixoFormaEntrada = apresentarFiltroFixoFormaEntrada;
	}

	public Boolean getApresentarFiltroFixoDataInicioAquisicao() {
		if (apresentarFiltroFixoDataInicioAquisicao == null) {
			apresentarFiltroFixoDataInicioAquisicao = true;
		}
		return apresentarFiltroFixoDataInicioAquisicao;
	}

	public void setApresentarFiltroFixoDataInicioAquisicao(Boolean apresentarFiltroFixoDataInicioAquisicao) {
		this.apresentarFiltroFixoDataInicioAquisicao = apresentarFiltroFixoDataInicioAquisicao;
	}

	public Boolean getApresentarFiltroFixoDataFimAquisicao() {
		if (apresentarFiltroFixoDataFimAquisicao == null) {
			apresentarFiltroFixoDataFimAquisicao = true;
		}
		return apresentarFiltroFixoDataFimAquisicao;
	}

	public void setApresentarFiltroFixoDataFimAquisicao(Boolean apresentarFiltroFixoDataFimAquisicao) {
		this.apresentarFiltroFixoDataFimAquisicao = apresentarFiltroFixoDataFimAquisicao;
	}

	public Boolean getApresentarFiltroFixoTipo() {
		if (apresentarFiltroFixoTipo == null) {
			apresentarFiltroFixoTipo = true;
		}
		return apresentarFiltroFixoTipo;
	}

	public void setApresentarFiltroFixoTipo(Boolean apresentarFiltroFixoTipo) {
		this.apresentarFiltroFixoTipo = apresentarFiltroFixoTipo;
	}

	public Boolean getApresentarFiltroFixoCatalogoPeriodico() {
		if (apresentarFiltroFixoCatalogoPeriodico == null) {
			apresentarFiltroFixoCatalogoPeriodico = true;
		}
		return apresentarFiltroFixoCatalogoPeriodico;
	}

	public void setApresentarFiltroFixoCatalogoPeriodico(Boolean apresentarFiltroFixoCatalogoPeriodico) {
		this.apresentarFiltroFixoCatalogoPeriodico = apresentarFiltroFixoCatalogoPeriodico;
	}

	public Boolean getApresentarFiltroFixoDataInicioEmprestimo() {
		if (apresentarFiltroFixoDataInicioEmprestimo == null) {
			apresentarFiltroFixoDataInicioEmprestimo = true;
		}
		return apresentarFiltroFixoDataInicioEmprestimo;
	}

	public void setApresentarFiltroFixoDataInicioEmprestimo(Boolean apresentarFiltroFixoDataInicioEmprestimo) {
		this.apresentarFiltroFixoDataInicioEmprestimo = apresentarFiltroFixoDataInicioEmprestimo;
	}

	public Boolean getApresentarFiltroFixoDataFimEmprestimo() {
		if (apresentarFiltroFixoDataFimEmprestimo == null) {
			apresentarFiltroFixoDataFimEmprestimo = true;
		}
		return apresentarFiltroFixoDataFimEmprestimo;
	}

	public void setApresentarFiltroFixoDataFimEmprestimo(Boolean apresentarFiltroFixoDataFimEmprestimo) {
		this.apresentarFiltroFixoDataFimEmprestimo = apresentarFiltroFixoDataFimEmprestimo;
	}

	public Boolean getApresentarFiltroFixoSituacaoEmprestimo() {
		if (apresentarFiltroFixoSituacaoEmprestimo == null) {
			apresentarFiltroFixoSituacaoEmprestimo = true;
		}
		return apresentarFiltroFixoSituacaoEmprestimo;
	}

	public void setApresentarFiltroFixoSituacaoEmprestimo(Boolean apresentarFiltroFixoSituacaoEmprestimo) {
		this.apresentarFiltroFixoSituacaoEmprestimo = apresentarFiltroFixoSituacaoEmprestimo;
	}

	public Boolean getApresentarFiltroFixoDataProvaInicio() {
		if (apresentarFiltroFixoDataProvaInicio == null) {
			apresentarFiltroFixoDataProvaInicio = true;
		}
		return apresentarFiltroFixoDataProvaInicio;
	}

	public void setApresentarFiltroFixoDataProvaInicio(Boolean apresentarFiltroFixoDataProvaInicio) {
		this.apresentarFiltroFixoDataProvaInicio = apresentarFiltroFixoDataProvaInicio;
	}

	public Boolean getApresentarFiltroFixoDataProvaFim() {
		if (apresentarFiltroFixoDataProvaFim == null) {
			apresentarFiltroFixoDataProvaFim = true;
		}
		return apresentarFiltroFixoDataProvaFim;
	}

	public void setApresentarFiltroFixoDataProvaFim(Boolean apresentarFiltroFixoDataProvaFim) {
		this.apresentarFiltroFixoDataProvaFim = apresentarFiltroFixoDataProvaFim;
	}

	public Boolean getApresentarFiltroFixoSituacaoResultadoProcessoSeletivo() {
		if (apresentarFiltroFixoSituacaoResultadoProcessoSeletivo == null) {
			apresentarFiltroFixoSituacaoResultadoProcessoSeletivo = true;
		}
		return apresentarFiltroFixoSituacaoResultadoProcessoSeletivo;
	}

	public void setApresentarFiltroFixoSituacaoResultadoProcessoSeletivo(
			Boolean apresentarFiltroFixoSituacaoResultadoProcessoSeletivo) {
		this.apresentarFiltroFixoSituacaoResultadoProcessoSeletivo = apresentarFiltroFixoSituacaoResultadoProcessoSeletivo;
	}

	public Boolean getApresentarFiltroFixoSituacaoInscricao() {
		if (apresentarFiltroFixoSituacaoInscricao == null) {
			apresentarFiltroFixoSituacaoInscricao = true;
		}
		return apresentarFiltroFixoSituacaoInscricao;
	}

	public void setApresentarFiltroFixoSituacaoInscricao(Boolean apresentarFiltroFixoSituacaoInscricao) {
		this.apresentarFiltroFixoSituacaoInscricao = apresentarFiltroFixoSituacaoInscricao;
	}

	public Boolean getApresentarFiltroFixoProcessoSeletivo() {
		if (apresentarFiltroFixoProcessoSeletivo == null) {
			apresentarFiltroFixoProcessoSeletivo = true;
		}
		return apresentarFiltroFixoProcessoSeletivo;
	}

	public void setApresentarFiltroFixoProcessoSeletivo(Boolean apresentarFiltroFixoProcessoSeletivo) {
		this.apresentarFiltroFixoProcessoSeletivo = apresentarFiltroFixoProcessoSeletivo;
	}

	public Boolean getApresentarFiltroFixoSituacaoFinanceiraProcessoSeletivo() {
		if (apresentarFiltroFixoSituacaoFinanceiraProcessoSeletivo == null) {
			apresentarFiltroFixoSituacaoFinanceiraProcessoSeletivo = true;
		}
		return apresentarFiltroFixoSituacaoFinanceiraProcessoSeletivo;
	}

	public void setApresentarFiltroFixoSituacaoFinanceiraProcessoSeletivo(
			Boolean apresentarFiltroFixoSituacaoFinanceiraProcessoSeletivo) {
		this.apresentarFiltroFixoSituacaoFinanceiraProcessoSeletivo = apresentarFiltroFixoSituacaoFinanceiraProcessoSeletivo;
	}

	public Boolean getApresentarFiltroFixoCampanha() {
		if (apresentarFiltroFixoCampanha == null) {
			apresentarFiltroFixoCampanha = true;
		}
		return apresentarFiltroFixoCampanha;
	}

	public void setApresentarFiltroFixoCampanha(Boolean apresentarFiltroFixoCampanha) {
		this.apresentarFiltroFixoCampanha = apresentarFiltroFixoCampanha;
	}

	public Boolean getApresentarFiltroFixoConsultor() {
		if (apresentarFiltroFixoConsultor == null) {
			apresentarFiltroFixoConsultor = true;
		}
		return apresentarFiltroFixoConsultor;
	}

	public void setApresentarFiltroFixoConsultor(Boolean apresentarFiltroFixoConsultor) {
		this.apresentarFiltroFixoConsultor = apresentarFiltroFixoConsultor;
	}

	public Boolean getApresentarFiltroFixoComponenteEstagio() {
		if (apresentarFiltroFixoComponenteEstagio == null) {
			apresentarFiltroFixoComponenteEstagio = true;
		}
		return apresentarFiltroFixoComponenteEstagio;
	}

	public void setApresentarFiltroFixoComponenteEstagio(Boolean apresentarFiltroFixoComponenteEstagio) {
		this.apresentarFiltroFixoComponenteEstagio = apresentarFiltroFixoComponenteEstagio;
	}

	public Boolean getApresentarFiltroFixoSituacaoEstagio() {
		if (apresentarFiltroFixoSituacaoEstagio == null) {
			apresentarFiltroFixoSituacaoEstagio = true;
		}
		return apresentarFiltroFixoSituacaoEstagio;
	}

	public void setApresentarFiltroFixoSituacaoEstagio(Boolean apresentarFiltroFixoSituacaoEstagio) {
		this.apresentarFiltroFixoSituacaoEstagio = apresentarFiltroFixoSituacaoEstagio;
	}

	public Boolean getApresentarFiltroFixoPeriodoEstagio() {
		if (apresentarFiltroFixoPeriodoEstagio == null) {
			apresentarFiltroFixoPeriodoEstagio = true;
		}
		return apresentarFiltroFixoPeriodoEstagio;
	}

	public void setApresentarFiltroFixoPeriodoEstagio(Boolean apresentarFiltroFixoPeriodoEstagio) {
		this.apresentarFiltroFixoPeriodoEstagio = apresentarFiltroFixoPeriodoEstagio;
	}

	
}