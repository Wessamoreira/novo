package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class AtaResultadosFinaisRelVO {

	private String turma;
	private String ano;
	private String semestre;
	private String cidade;
	private String endereco;
	private String numero;
	private String bairro;
	private String complemento;
	private String cep;
	private String estado;
	private String email;
	private String fone;
	private String site;
	private String turno;
	private String funcionarioPrincipal;
	private String funcionarioSecundario;
	private String descricaoAta;
	private String periodoLetivo;
	private Date dataApuracao;

	private Boolean anual;
	private Boolean semestral;
	private String nivelEducacional;
	private String nomeCertificadoPeriodoLetivo;

	private List<AtaResultadosFinaisDisciplinasRelVO> ataResultadosFinaisDisciplinasRelVOs;
	private String nomeDocumentacaoCurso;

	public String getTurma() {
		if (turma == null) {
			turma = "";
		}
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public JRDataSource getAtaResultadosFinaisDisciplinasRelVOsJRDataSource() {
		return new JRBeanArrayDataSource(getAtaResultadosFinaisDisciplinasRelVOs().toArray());
	}

	public List<AtaResultadosFinaisDisciplinasRelVO> getAtaResultadosFinaisDisciplinasRelVOs() {
		if (ataResultadosFinaisDisciplinasRelVOs == null) {
			ataResultadosFinaisDisciplinasRelVOs = new ArrayList<AtaResultadosFinaisDisciplinasRelVO>(0);
		}
		return ataResultadosFinaisDisciplinasRelVOs;
	}

	public void setAtaResultadosFinaisDisciplinasRelVOs(
			List<AtaResultadosFinaisDisciplinasRelVO> ataResultadosFinaisDisciplinasRelVOs) {
		this.ataResultadosFinaisDisciplinasRelVOs = ataResultadosFinaisDisciplinasRelVOs;
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

	public String getEndereco() {
		if (endereco == null) {
			endereco = "";
		}
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
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

	public String getBairro() {
		if (bairro == null) {
			bairro = "";
		}
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
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

	public String getCep() {
		if (cep == null) {
			cep = "";
		}
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
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

	public String getEmail() {
		if (email == null) {
			email = "";
		}
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFone() {
		if (fone == null) {
			fone = "";
		}
		return fone;
	}

	public void setFone(String fone) {
		this.fone = fone;
	}

	public String getSite() {
		if (site == null) {
			site = "";
		}
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getFuncionarioPrincipal() {
		if (funcionarioPrincipal == null) {
			funcionarioPrincipal = "";
		}
		return funcionarioPrincipal;
	}

	public void setFuncionarioPrincipal(String funcionarioPrincipal) {
		this.funcionarioPrincipal = funcionarioPrincipal;
	}

	public String getFuncionarioSecundario() {
		if (funcionarioSecundario == null) {
			funcionarioSecundario = "";
		}
		return funcionarioSecundario;
	}

	public void setFuncionarioSecundario(String funcionarioSecundario) {
		this.funcionarioSecundario = funcionarioSecundario;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public String getDescricaoAta() {
		if (descricaoAta == null) {
			descricaoAta = "";
		}
		return descricaoAta;
	}

	public void setDescricaoAta(String descricaoAta) {
		this.descricaoAta = descricaoAta;
	}

	public String getPeriodoLetivo() {
		if (periodoLetivo == null) {
			periodoLetivo = "";
		}
		return periodoLetivo;
	}

	public void setPeriodoLetivo(String periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}

	public Date getDataApuracao() {
		if (dataApuracao == null) {
			dataApuracao = new Date();
		}
		return dataApuracao;
	}

	public void setDataApuracao(Date dataApuracao) {
		this.dataApuracao = dataApuracao;
	}

	public Boolean getAnual() {
		if(anual == null) {
			anual = Boolean.FALSE;
		}
		return anual;
	}

	public void setAnual(Boolean anual) {
		this.anual = anual;
	}

	public Boolean getSemestral() {
		if(semestral == null) {
			semestral = Boolean.FALSE;
		}
		return semestral;
	}

	public void setSemestral(Boolean semestral) {
		this.semestral = semestral;
	}

	public String getNivelEducacional() {
		if(nivelEducacional == null) {
			nivelEducacional = "";
		}
		return nivelEducacional;
	}

	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}

	public String getNomeCertificadoPeriodoLetivo() {
		if(nomeCertificadoPeriodoLetivo == null) {
			nomeCertificadoPeriodoLetivo = "";
		}
		return nomeCertificadoPeriodoLetivo;
	}

	public void setNomeCertificadoPeriodoLetivo(String nomeCertificadoPeriodoLetivo) {
		this.nomeCertificadoPeriodoLetivo = nomeCertificadoPeriodoLetivo;
	}
	
	public String getNomeDocumentacaoCurso() {
		return nomeDocumentacaoCurso;
	}

	public void setNomeDocumentacaoCurso(String nomeDocumentacaoCurso) {
		this.nomeDocumentacaoCurso = nomeDocumentacaoCurso;
	}	
		
}