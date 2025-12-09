package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

public class RegraEmissaoVO extends SuperVO {
	
	private static final long serialVersionUID = 2301202208203870984L;
	private Integer codigo;
	private String nivelEducacional;
	private Boolean validarMatrizCurricularIntegralizado = false;
	private Boolean validarNotaTCC = false;
	private Boolean validarDocumentosEntregues = false;
	private String tipoContrato;
	private Double notaTCC;
	private List<RegraEmissaoUnidadeEnsinoVO> regraEmissaoUnidadeEnsinoVOs;
	private List<RegraEmissaoVO> listaRegraEmissaoRemovidos;
	
	/* Atributo Transient */
	private String nomeUnidadeEnsisnoSelecionadas;
	private String tipoNivelEducacionalApresentar;
	private String apresentarTipoContrato;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNivelEducacional() {
		if (nivelEducacional == null) {
			nivelEducacional = "";
		}
		return nivelEducacional;
	}

	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}

	public List<RegraEmissaoUnidadeEnsinoVO> getRegraEmissaoUnidadeEnsinoVOs() {
		if (regraEmissaoUnidadeEnsinoVOs == null) {
			regraEmissaoUnidadeEnsinoVOs = new ArrayList<RegraEmissaoUnidadeEnsinoVO>();
		}
		return regraEmissaoUnidadeEnsinoVOs;
	}

	public void setRegraEmissaoUnidadeEnsinoVOs(List<RegraEmissaoUnidadeEnsinoVO> regraEmissaoUnidadeEnsinoVOs) {
		this.regraEmissaoUnidadeEnsinoVOs = regraEmissaoUnidadeEnsinoVOs;
	}

	public Boolean getValidarMatrizCurricularIntegralizado() {
		return validarMatrizCurricularIntegralizado;
	}
	
	public void setValidarMatrizCurricularIntegralizado(Boolean validarMatrizCurricularIntegralizado) {
		this.validarMatrizCurricularIntegralizado = validarMatrizCurricularIntegralizado;
	}
	
	public Boolean getValidarNotaTCC() {
		return validarNotaTCC;
	}
	
	public void setValidarNotaTCC(Boolean validarNotaTCC) {
		this.validarNotaTCC = validarNotaTCC;
	}
	
	public Boolean getValidarDocumentosEntregues() {
		return validarDocumentosEntregues;
	}
	public void setValidarDocumentosEntregues(Boolean validarDocumentosEntregues) {
		this.validarDocumentosEntregues = validarDocumentosEntregues;
	}

	public String getTipoContrato() {
		if (tipoContrato == null) {
			tipoContrato = "";
		}
		return tipoContrato;
	}

	public void setTipoContrato(String tipoContrato) {
		this.tipoContrato = tipoContrato;
	}

	public String getNomeUnidadeEnsisnoSelecionadas() {
		if (nomeUnidadeEnsisnoSelecionadas == null) {
			nomeUnidadeEnsisnoSelecionadas = "";
		}
		return nomeUnidadeEnsisnoSelecionadas;
	}
	
	public Double getNotaTCC() {
		return notaTCC;
	}

	public void setNotaTCC(Double notaTCC) {
		this.notaTCC = notaTCC;
	}

	public void setNomeUnidadeEnsisnoSelecionadas(String nomeUnidadeEnsisnoSelecionadas) {
		this.nomeUnidadeEnsisnoSelecionadas = nomeUnidadeEnsisnoSelecionadas;
	}

	public List<RegraEmissaoVO> getListaRegraEmissaoRemovidos() {
		if (listaRegraEmissaoRemovidos == null) {
			listaRegraEmissaoRemovidos = new ArrayList<RegraEmissaoVO>();
		}
		return listaRegraEmissaoRemovidos;
	}

	public void setListaRegraEmissaoRemovidos(List<RegraEmissaoVO> listaRegraEmissaoRemovidos) {
		this.listaRegraEmissaoRemovidos = listaRegraEmissaoRemovidos;
	}

	public String getTipoNivelEducacionalApresentar() {
		if (tipoNivelEducacionalApresentar == null) {
			tipoNivelEducacionalApresentar = "";
		}
		return tipoNivelEducacionalApresentar;
	}

	public void setTipoNivelEducacionalApresentar(String tipoNivelEducacionalApresentar) {
		this.tipoNivelEducacionalApresentar = tipoNivelEducacionalApresentar;
	}

	public String getApresentarTipoContrato() {
		if (apresentarTipoContrato == null) {
			apresentarTipoContrato = "";
		}
		return apresentarTipoContrato;
	}

	public void setApresentarTipoContrato(String apresentarTipoContrato) {
		this.apresentarTipoContrato = apresentarTipoContrato;
	}
}
