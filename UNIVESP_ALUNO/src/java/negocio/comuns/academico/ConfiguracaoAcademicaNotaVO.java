package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import jakarta.faces.model.SelectItem;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import jobs.enumeradores.TipoUsoNotaEnum;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.academico.enumeradores.TipoNotaConceitoEnum;
import negocio.comuns.arquitetura.SuperVO;

@XmlRootElement(name = "configuracaoAcademicaNotaVO")
public class ConfiguracaoAcademicaNotaVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2005821415964211013L;
	private Integer codigo;
	private ConfiguracaoAcademicoVO configuracaoAcademico;
	private TipoNotaConceitoEnum nota;
	private Boolean notaRecuperacao;
	private String formulaCalculoVerificaNotaRecuperada;
	private Integer qtdeDisciplinaRecuperacao;
	
	private Boolean utilizarNota;
	private Boolean utilizarNotaPorConceito;
	private Boolean apresentarNota;
	private Boolean apresentarNotaBoletim;
	private Boolean utilizarComoSubstitutiva;
	private String politicaSubstitutiva;	
	private String variavel;
	private String titulo;
	private String formulaCalculo;
	private String formulaUso;
	private Boolean utilizarComoMediaFinal;
	private BimestreEnum agrupamentoNota;
	private Double faixaNotaMenor;
	private Double faixaNotaMaior;
	private String regraArredondamentoNota;
	private Boolean permiteRealizarRecuperacaoDisciplinaDependencia;
	private Boolean permiteRealizarRecuperacaoDisciplinaAdaptacao;
	private List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNotaConceitoVOs;
	private Boolean permiteReplicarNotaOutraDisciplina;
	/**
	 * Transient utilizado no MapaNotaPendenciaAlunoRel
	 */
	private Boolean utilizarNotaSegundaChamada;
	private String tituloApresentar;
	private Boolean bloquearLancamentoNota;
	private Boolean filtrarConfiguracaoAcademicaNota;
	private String notaTransiente;
	private TipoUsoNotaEnum tipoUsoNota;
	

	/**
	 * Fim Transient
	 */

	public ConfiguracaoAcademicaNotaVO() {
		super();
	}
		

	public ConfiguracaoAcademicaNotaVO(TipoNotaConceitoEnum nota) {
		super();
		this.nota = nota;
	}


	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	@XmlElement(name = "configuracaoAcademico")
	public ConfiguracaoAcademicoVO getConfiguracaoAcademico() {
		if (configuracaoAcademico == null) {
			configuracaoAcademico = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademico;
	}

	public void setConfiguracaoAcademico(ConfiguracaoAcademicoVO configuracaoAcademico) {
		this.configuracaoAcademico = configuracaoAcademico;
	}

	@XmlElement(name = "nota")
	public TipoNotaConceitoEnum getNota() {
		if (nota == null) {
			nota = TipoNotaConceitoEnum.NOTA_1;
		}
		return nota;
	}

	public void setNota(TipoNotaConceitoEnum nota) {
		this.nota = nota;
	}

	@XmlElement(name = "notaRecuperacao")
	public Boolean getNotaRecuperacao() {
		if (notaRecuperacao == null) {
			notaRecuperacao = false;
		}
		return notaRecuperacao;
	}

	public void setNotaRecuperacao(Boolean notaRecuperacao) {
		this.notaRecuperacao = notaRecuperacao;
	}

	@XmlElement(name = "qtdeDisciplinaRecuperacao")
	public Integer getQtdeDisciplinaRecuperacao() {
		if (qtdeDisciplinaRecuperacao == null) {
			qtdeDisciplinaRecuperacao = 0;
		}
		return qtdeDisciplinaRecuperacao;
	}

	public void setQtdeDisciplinaRecuperacao(Integer qtdeDisciplinaRecuperacao) {
		this.qtdeDisciplinaRecuperacao = qtdeDisciplinaRecuperacao;
	}


	@XmlElement(name = "utilizarNota")
	public Boolean getUtilizarNota() {
		if (utilizarNota == null) {
			utilizarNota = false;
		}
		return utilizarNota;
	}


	public void setUtilizarNota(Boolean utilizarNota) {
		this.utilizarNota = utilizarNota;
	}


	@XmlElement(name = "utilizarNotaPorConceito")
	public Boolean getUtilizarNotaPorConceito() {
		if (utilizarNotaPorConceito == null) {
			utilizarNotaPorConceito = false;
		}
		return utilizarNotaPorConceito;
	}


	public void setUtilizarNotaPorConceito(Boolean utilizarNotaPorConceito) {
		this.utilizarNotaPorConceito = utilizarNotaPorConceito;
	}


	@XmlElement(name = "configuracaoAcademicoNotaConceitoVOs")
	public List<ConfiguracaoAcademicoNotaConceitoVO> getConfiguracaoAcademicoNotaConceitoVOs() {
		if (configuracaoAcademicoNotaConceitoVOs == null) {
			configuracaoAcademicoNotaConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);
		}
		return configuracaoAcademicoNotaConceitoVOs;
	}


	public void setConfiguracaoAcademicoNotaConceitoVOs(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNotaConceitoVOs) {
		this.configuracaoAcademicoNotaConceitoVOs = configuracaoAcademicoNotaConceitoVOs;
	}


	@XmlElement(name = "variavel")
	public String getVariavel() {
		if (variavel == null) {
			variavel = "";
		}
		return variavel;
	}


	public void setVariavel(String variavel) {
		this.variavel = variavel;
	}


	@XmlElement(name = "titulo")
	public String getTitulo() {
		if (titulo == null) {
			titulo = "";
		}
		return titulo;
	}


	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}


	@XmlElement(name = "formulaCalculo")
	public String getFormulaCalculo() {
		if (formulaCalculo == null) {
			formulaCalculo = "";
		}
		return formulaCalculo;
	}


	public void setFormulaCalculo(String formulaCalculo) {
		this.formulaCalculo = formulaCalculo;
	}


	@XmlElement(name = "formulaUso")
	public String getFormulaUso() {
		if (formulaUso == null) {
			formulaUso = "";
		}
		return formulaUso;
	}


	public void setFormulaUso(String formulaUso) {
		this.formulaUso = formulaUso;
	}


	@XmlElement(name = "utilizarComoMediaFinal")
	public Boolean getUtilizarComoMediaFinal() {
		if (utilizarComoMediaFinal == null) {
			utilizarComoMediaFinal = false;
		}
		return utilizarComoMediaFinal;
	}


	public void setUtilizarComoMediaFinal(Boolean utilizarComoMediaFinal) {
		this.utilizarComoMediaFinal = utilizarComoMediaFinal;
	}


	@XmlElement(name = "agrupamentoNota")
	public BimestreEnum getAgrupamentoNota() {	
		if(agrupamentoNota == null){
			agrupamentoNota = BimestreEnum.NAO_CONTROLA;
		}
		return agrupamentoNota;
	}


	public void setAgrupamentoNota(BimestreEnum agrupamentoNota) {
		this.agrupamentoNota = agrupamentoNota;
	}


	@XmlElement(name = "faixaNotaMenor")
	public Double getFaixaNotaMenor() {
		if (faixaNotaMenor == null) {
			faixaNotaMenor = 0.0;
		}
		return faixaNotaMenor;
	}


	public void setFaixaNotaMenor(Double faixaNotaMenor) {
		this.faixaNotaMenor = faixaNotaMenor;
	}

	@XmlElement(name = "faixaNotaMaior")
	public Double getFaixaNotaMaior() {
		if (faixaNotaMaior == null) {
			faixaNotaMaior = 10.0;
		}
		return faixaNotaMaior;
	}


	public void setFaixaNotaMaior(Double faixaNotaMaior) {
		this.faixaNotaMaior = faixaNotaMaior;
	}


	@XmlElement(name = "apresentarNota")
	public Boolean getApresentarNota() {
		if (apresentarNota == null) {
			apresentarNota = false;
		}
		return apresentarNota;
	}


	public void setApresentarNota(Boolean apresentarNota) {
		this.apresentarNota = apresentarNota;
	}


	@XmlElement(name = "utilizarComoSubstitutiva")
	public Boolean getUtilizarComoSubstitutiva() {
		if (utilizarComoSubstitutiva == null) {
			utilizarComoSubstitutiva = false;
		}
		return utilizarComoSubstitutiva;
	}


	public void setUtilizarComoSubstitutiva(Boolean utilizarComoSubstitutiva) {
		this.utilizarComoSubstitutiva = utilizarComoSubstitutiva;
	}


	@XmlElement(name = "politicaSubstitutiva")
	public String getPoliticaSubstitutiva() {
		if (politicaSubstitutiva == null) {
			politicaSubstitutiva = "";
		}
		return politicaSubstitutiva;
	}


	public void setPoliticaSubstitutiva(String politicaSubstitutiva) {
		this.politicaSubstitutiva = politicaSubstitutiva;
	}


	@XmlElement(name = "regraArredondamentoNota")
	public String getRegraArredondamentoNota() {
		if (regraArredondamentoNota == null) {
			regraArredondamentoNota = "";
		}
		return regraArredondamentoNota;
	}


	public void setRegraArredondamentoNota(String regraArredondamentoNota) {
		this.regraArredondamentoNota = regraArredondamentoNota;
	}


	@XmlElement(name = "permiteRealizarRecuperacaoDisciplinaDependencia")
	public Boolean getPermiteRealizarRecuperacaoDisciplinaDependencia() {
		if (permiteRealizarRecuperacaoDisciplinaDependencia == null) {
			permiteRealizarRecuperacaoDisciplinaDependencia = false;
		}
		return permiteRealizarRecuperacaoDisciplinaDependencia;
	}


	public void setPermiteRealizarRecuperacaoDisciplinaDependencia(Boolean permiteRealizarRecuperacaoDisciplinaDependencia) {
		this.permiteRealizarRecuperacaoDisciplinaDependencia = permiteRealizarRecuperacaoDisciplinaDependencia;
	}


	@XmlElement(name = "permiteRealizarRecuperacaoDisciplinaAdaptacao")
	public Boolean getPermiteRealizarRecuperacaoDisciplinaAdaptacao() {
		if (permiteRealizarRecuperacaoDisciplinaAdaptacao == null) {
			permiteRealizarRecuperacaoDisciplinaAdaptacao = false;
		}
		return permiteRealizarRecuperacaoDisciplinaAdaptacao;
	}


	public void setPermiteRealizarRecuperacaoDisciplinaAdaptacao(Boolean permiteRealizarRecuperacaoDisciplinaAdaptacao) {
		this.permiteRealizarRecuperacaoDisciplinaAdaptacao = permiteRealizarRecuperacaoDisciplinaAdaptacao;
	}


	@XmlElement(name = "formulaCalculoVerificaNotaRecuperada")
	public String getFormulaCalculoVerificaNotaRecuperada() {
		if (formulaCalculoVerificaNotaRecuperada == null) {
			formulaCalculoVerificaNotaRecuperada = "";
		}
		return formulaCalculoVerificaNotaRecuperada;
	}


	public void setFormulaCalculoVerificaNotaRecuperada(String formulaCalculoVerificaNotaRecuperada) {
		this.formulaCalculoVerificaNotaRecuperada = formulaCalculoVerificaNotaRecuperada;
	}


	@XmlElement(name = "apresentarNotaBoletim")
	public Boolean getApresentarNotaBoletim() {
		if (apresentarNotaBoletim == null) {
			apresentarNotaBoletim = true;
		}
		return apresentarNotaBoletim;
	}


	public void setApresentarNotaBoletim(Boolean apresentarNotaBoletim) {
		this.apresentarNotaBoletim = apresentarNotaBoletim;
	}


	@XmlElement(name = "utilizarNotaSegundaChamada")
	public Boolean getUtilizarNotaSegundaChamada() {
		if (utilizarNotaSegundaChamada == null) {
			utilizarNotaSegundaChamada = false;
		}
		return utilizarNotaSegundaChamada;
	}


	public void setUtilizarNotaSegundaChamada(Boolean utilizarNotaSegundaChamada) {
		this.utilizarNotaSegundaChamada = utilizarNotaSegundaChamada;
	}
	
	@XmlElement(name = "tituloApresentar")
	public String getTituloApresentar() {
		if (tituloApresentar == null) {
			tituloApresentar = "";
		}
		return tituloApresentar;
	}


	public void setTituloApresentar(String tituloApresentar) {
		this.tituloApresentar = tituloApresentar;
	}
	
	public Boolean getNotaTipoLancamento() {
		if (!getUtilizarNota()) {
			return false;
		}
		if ((getUtilizarComoSubstitutiva()) || (getFormulaCalculo().trim().isEmpty())) {
			return true;
		}
		return false;
	}
	

	@Override
	public ConfiguracaoAcademicaNotaVO clone()  throws CloneNotSupportedException {
		ConfiguracaoAcademicaNotaVO clone  = (ConfiguracaoAcademicaNotaVO)super.clone();
		clone.setCodigo(0);		
		clone.setConfiguracaoAcademicoNotaConceitoVOs(new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0));
		clone.setNovoObj(true);
		for(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO: this.getConfiguracaoAcademicoNotaConceitoVOs()){
			ConfiguracaoAcademicoNotaConceitoVO cloneConceito = configuracaoAcademicoNotaConceitoVO.clone();
			cloneConceito.setCodigo(0);
			cloneConceito.setConfiguracaoAcademico(0);
			cloneConceito.setNovoObj(true);
			clone.getConfiguracaoAcademicoNotaConceitoVOs().add(cloneConceito);
		}
		return clone;
	}
	
	@XmlElement(name = "permiteReplicarNotaOutraDisciplina")
	public Boolean getPermiteReplicarNotaOutraDisciplina() {
		if(permiteReplicarNotaOutraDisciplina == null){
			permiteReplicarNotaOutraDisciplina = false; 
		}
		return permiteReplicarNotaOutraDisciplina;
	}


	public void setPermiteReplicarNotaOutraDisciplina(Boolean permiteReplicarNotaOutraDisciplina) {
		this.permiteReplicarNotaOutraDisciplina = permiteReplicarNotaOutraDisciplina;
	}
	
	private List<SelectItem> listaSelectItemNotaConceito;
	
	@XmlElement(name = "listaSelectItemNotaConceito")
	public List<SelectItem> getListaSelectItemNotaConceito() {
		if (listaSelectItemNotaConceito == null) {
			listaSelectItemNotaConceito = new ArrayList<SelectItem>(0);
			for(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO: this.getConfiguracaoAcademicoNotaConceitoVOs()){				
				listaSelectItemNotaConceito.add(new SelectItem(configuracaoAcademicoNotaConceitoVO.getCodigo(), configuracaoAcademicoNotaConceitoVO.getConceitoNota()));
			}
		}
		return listaSelectItemNotaConceito;
	}


	public void setListaSelectItemNotaConceito(List<SelectItem> listaSelectItemNotaConceito) {
		this.listaSelectItemNotaConceito = listaSelectItemNotaConceito;
	}

	private Double notaDigitada;
	private ConfiguracaoAcademicoNotaConceitoVO notaConceitoSelecionado;
	
	@XmlElement(name = "notaDigitada")
	public Double getNotaDigitada() {		
		return notaDigitada;
	}


	public void setNotaDigitada(Double notaDigitada) {
		this.notaDigitada = notaDigitada;
	}


	@XmlElement(name = "notaConceitoSelecionado")
	public ConfiguracaoAcademicoNotaConceitoVO getNotaConceitoSelecionado() {
		if (notaConceitoSelecionado == null) {
			notaConceitoSelecionado = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return notaConceitoSelecionado;
	}


	public void setNotaConceitoSelecionado(ConfiguracaoAcademicoNotaConceitoVO notaConceitoSelecionado) {
		this.notaConceitoSelecionado = notaConceitoSelecionado;
	}


	/**
	 * @return the bloquearLancamentoNota
	 */
	@XmlElement(name = "bloquearLancamentoNota")
	public Boolean getBloquearLancamentoNota() {
		if(bloquearLancamentoNota == null){
			bloquearLancamentoNota= false;
		}
		return bloquearLancamentoNota;
	}


	/**
	 * @param bloquearLancamentoNota the bloquearLancamentoNota to set
	 */
	public void setBloquearLancamentoNota(Boolean bloquearLancamentoNota) {
		this.bloquearLancamentoNota = bloquearLancamentoNota;
	}

	public TipoUsoNotaEnum getTipoUsoNota() {
		if (tipoUsoNota == null) {
			tipoUsoNota = TipoUsoNotaEnum.NENHUM;
		}
		return tipoUsoNota;
	}


	public void setTipoUsoNota(TipoUsoNotaEnum tipoUsoNota) {
		this.tipoUsoNota = tipoUsoNota;
	}


	public Boolean getFiltrarConfiguracaoAcademicaNota() {
		if (filtrarConfiguracaoAcademicaNota == null) {
			filtrarConfiguracaoAcademicaNota = Boolean.FALSE;
		}
		return filtrarConfiguracaoAcademicaNota;
	}

	public void setFiltrarConfiguracaoAcademicaNota(Boolean filtrarConfiguracaoAcademicaNota) {
		this.filtrarConfiguracaoAcademicaNota = filtrarConfiguracaoAcademicaNota;
	}


	public String getNotaTransiente() {
		if (notaTransiente == null) {
			notaTransiente = "";
		}
		return notaTransiente;
	}


	public void setNotaTransiente(String notaTransiente) {
		this.notaTransiente = notaTransiente;
	}	
}
