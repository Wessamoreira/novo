/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.processosel;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author Philippe
 */
public class ResultadoProcessoSeletivoRelVO {

	private String processoSeletivo;
	private Integer inscricao;
	private String nomeCandidato;
	private String dataNasc;
	private String celular;
	private String telefoneComerc;
	private String telefoneRes;
	private String cep;
	private String cidade;
	private String estado;
	private String setor;
	private String numero;
	private String endereco;
	private String complemento;
	private String sexo;	
	private Integer classificacao;
	private String curso;
	private Boolean apresentarNota;
	private String regimeAprovacao;
	private Double notaRedacao;
	private Double mediaNotasProcSeletivo;
	private Integer numeroAcertos;
	private Integer chamada;
	private String turno;
	private String unidadeEnsino;
	private Boolean apresentarNotaPorDisciplina;
	private Boolean apresentarQuantidadeAcerto;
	private Boolean apresentarSituacaoResultadoProcessoSeletivo;
	private List<ResultadoProcessoSeletivoDisciplinaRelVO> listaResultadoProcessoSeletivoDisciplinaRelVOs;
	private String resultadoPrimeiraOpcao;
	private String resultadoSegundaOpcao;
	private String resultadoTerceiraOpcao;
	private Integer nrOpcoesCurso;

	public String getProcessoSeletivo() {
		if (processoSeletivo == null) {
			processoSeletivo = "";
		}
		return processoSeletivo;
	}

	public void setProcessoSeletivo(String processoSeletivo) {
		this.processoSeletivo = processoSeletivo;
	}

	public Integer getInscricao() {
		if (inscricao == null) {
			inscricao = 0000;
		}
		return inscricao;
	}

	public void setInscricao(Integer inscricao) {
		this.inscricao = inscricao;
	}

	public String getNomeCandidato() {
		if (nomeCandidato == null) {
			nomeCandidato = "";
		}
		return nomeCandidato;
	}

	public void setNomeCandidato(String nomeCandidato) {
		this.nomeCandidato = nomeCandidato;
	}

	public Integer getClassificacao() {
		return classificacao;
	}

	public String getClassificacao_Apresentar() {
		return getClassificacao() != null && getClassificacao() > 0 ? getClassificacao().toString() + "°" : "";
	}

	public void setClassificacao(Integer classificacao) {
		this.classificacao = classificacao;
	}

	public String getCurso() {
		if (curso == null) {
			curso = "";
		}
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public Boolean getApresentarNota() {
		if (apresentarNota == null) {
			apresentarNota = Boolean.FALSE;
		}
		return apresentarNota;
	}

	public void setApresentarNota(Boolean apresentarNota) {
		this.apresentarNota = apresentarNota;
	}

	public boolean getIsApresentarNotaRedacao() {
		return getRegimeAprovacao().equals("quantidadeAcertosRedacao");
	}

	public String getRegimeAprovacao() {
		if (regimeAprovacao == null) {
			regimeAprovacao = "";
		}
		return regimeAprovacao;
	}

	public void setRegimeAprovacao(String regimeAprovacao) {
		this.regimeAprovacao = regimeAprovacao;
	}

	public Double getNotaRedacao() {
		if (notaRedacao == null) {
			notaRedacao = 0.0;
		}
		return notaRedacao;
	}

	public void setNotaRedacao(Double notaRedacao) {
		this.notaRedacao = notaRedacao;
	}

	public Double getMediaNotasProcSeletivo() {
		if (mediaNotasProcSeletivo == null) {
			mediaNotasProcSeletivo = 0.0;
		}
		return mediaNotasProcSeletivo;
	}

	public void setMediaNotasProcSeletivo(Double mediaNotasProcSeletivo) {
		this.mediaNotasProcSeletivo = mediaNotasProcSeletivo;
	}

	public Integer getNumeroAcertos() {
		if (numeroAcertos == null) {
			numeroAcertos = 0;
		}
		return numeroAcertos;
	}

	public void setNumeroAcertos(Integer numeroAcertos) {
		this.numeroAcertos = numeroAcertos;
	}

	public Integer getChamada() {
		if (chamada == null) {
			chamada = 0;
		}
		return chamada;
	}

	public void setChamada(Integer chamada) {
		this.chamada = chamada;
	}

	public String getTurno() {
		if (turno == null) {
			turno = "";
		}
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public String getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = "";
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public List<ResultadoProcessoSeletivoDisciplinaRelVO> getListaResultadoProcessoSeletivoDisciplinaRelVOs() {
		if (listaResultadoProcessoSeletivoDisciplinaRelVOs == null) {
			listaResultadoProcessoSeletivoDisciplinaRelVOs = new java.util.ArrayList<ResultadoProcessoSeletivoDisciplinaRelVO>(0);
		}
		return listaResultadoProcessoSeletivoDisciplinaRelVOs;
	}

	public void setListaResultadoProcessoSeletivoDisciplinaRelVOs(List<ResultadoProcessoSeletivoDisciplinaRelVO> listaResultadoProcessoSeletivoDisciplinaRelVOs) {
		this.listaResultadoProcessoSeletivoDisciplinaRelVOs = listaResultadoProcessoSeletivoDisciplinaRelVOs;
	}
	
	public JRDataSource getListaResultadoProcessoSeletivoDisciplinaJRDataSourceVOs() {
		JRBeanCollectionDataSource jr = new JRBeanCollectionDataSource(getListaResultadoProcessoSeletivoDisciplinaRelVOs());
		return jr;
	}

	public Boolean getApresentarNotaPorDisciplina() {
		if (apresentarNotaPorDisciplina == null) {
			apresentarNotaPorDisciplina = Boolean.TRUE;
		}
		return apresentarNotaPorDisciplina;
	}

	public void setApresentarNotaPorDisciplina(Boolean apresentarNotaPorDisciplina) {
		this.apresentarNotaPorDisciplina = apresentarNotaPorDisciplina;
	}
	
	public Boolean getApresentarQuantidadeAcerto() {
		if (apresentarQuantidadeAcerto == null) {
			apresentarQuantidadeAcerto = Boolean.FALSE;
		}
		return apresentarQuantidadeAcerto;
	}

	public void setApresentarQuantidadeAcerto(Boolean apresentarQuantidadeAcerto) {
		this.apresentarQuantidadeAcerto = apresentarQuantidadeAcerto;
	}

	public Boolean getApresentarSituacaoResultadoProcessoSeletivo() {
		if (apresentarSituacaoResultadoProcessoSeletivo == null) {
			apresentarSituacaoResultadoProcessoSeletivo = Boolean.FALSE;
		}
		return apresentarSituacaoResultadoProcessoSeletivo;
	}

	public void setApresentarSituacaoResultadoProcessoSeletivo(Boolean apresentarSituacaoResultadoProcessoSeletivo) {
		this.apresentarSituacaoResultadoProcessoSeletivo = apresentarSituacaoResultadoProcessoSeletivo;
	}

	public String getResultadoPrimeiraOpcao() {
		if (resultadoPrimeiraOpcao == null) {
			resultadoPrimeiraOpcao = "";
		}
		return resultadoPrimeiraOpcao;
	}

	public void setResultadoPrimeiraOpcao(String resultadoPrimeiraOpcao) {
		this.resultadoPrimeiraOpcao = resultadoPrimeiraOpcao;
	}

	public String getResultadoSegundaOpcao() {
		if (resultadoSegundaOpcao == null) {
			resultadoSegundaOpcao = "";
		}
		return resultadoSegundaOpcao;
	}

	public void setResultadoSegundaOpcao(String resultadoSegundaOpcao) {
		this.resultadoSegundaOpcao = resultadoSegundaOpcao;
	}

	public String getResultadoTerceiraOpcao() {
		if (resultadoTerceiraOpcao == null) {
			resultadoTerceiraOpcao = "";
		}
		return resultadoTerceiraOpcao;
	}

	public void setResultadoTerceiraOpcao(String resultadoTerceiraOpcao) {
		this.resultadoTerceiraOpcao = resultadoTerceiraOpcao;
	}

	public Integer getNrOpcoesCurso() {
		if (nrOpcoesCurso == null) {
			nrOpcoesCurso = 0;
		}
		return nrOpcoesCurso;
	}

	public void setNrOpcoesCurso(Integer nrOpcoesCurso) {
		this.nrOpcoesCurso = nrOpcoesCurso;
	}
	
	public String getSituacaoResultadoProcessoSeletivo() {
		if (getNrOpcoesCurso().equals(1)) {
			if (getResultadoPrimeiraOpcao().equals("AP")) {
				return "Aprovado 1º Opção";
			} else {
				return "Reprovado";
			}
		}
		if (getNrOpcoesCurso().equals(2)) {
			if (getResultadoPrimeiraOpcao().equals("AP")) {
				return "Aprovado 1º Opção";
			} else if (getResultadoSegundaOpcao().equals("AP")){
				return "Aprovado 2º Opção";
			} 
			return "Reprovado";
		}
		if (getNrOpcoesCurso().equals(3)) {
			if (getResultadoPrimeiraOpcao().equals("AP")) {
				return "Aprovado 1º Opção";
			} else if (getResultadoSegundaOpcao().equals("AP")){
				return "Aprovado 2º Opção";
			} else if (getResultadoTerceiraOpcao().equals("AP")){
				return "Aprovado 3º Opção";
			}
			return "Reprovado";
		}
		return "";
	}

	public String getCelular() {
		if (celular == null) {
			celular = "";
		}
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getTelefoneComerc() {
		if (telefoneComerc == null) {
			telefoneComerc = "";
		}
		return telefoneComerc;
	}

	public void setTelefoneComerc(String telefoneComerc) {
		this.telefoneComerc = telefoneComerc;
	}

	public String getTelefoneRes() {
		if (telefoneRes == null) {
			telefoneRes = "";
		}
		return telefoneRes;
	}

	public void setTelefoneRes(String telefoneRes) {
		this.telefoneRes = telefoneRes;
	}

	public String getCep() {
		if (cep == null) {
			cep = "";
		}
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCidade() {
		if (cidade == null) {
			cidade = "";
		}
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		if (estado == null) {
			estado = "";
		}
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getSetor() {
		if (setor == null) {
			setor = "";
		}
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	public String getNumero() {
		if (numero == null) {
			numero = "";
		}		
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getEndereco() {
		if (endereco == null) {
			endereco = "";
		}
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getComplemento() {
		if (complemento == null) {
			complemento = "";
		}
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getSexo() {
		if (sexo == null) {
			sexo = "";
		}
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getDataNasc() {
		if (dataNasc == null) {
			dataNasc = "";
		}
		return dataNasc;
	}

	public void setDataNasc(String dataNasc) {
		this.dataNasc = dataNasc;
	}
}
