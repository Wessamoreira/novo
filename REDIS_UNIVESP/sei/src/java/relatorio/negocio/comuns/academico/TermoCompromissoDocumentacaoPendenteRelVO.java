package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.DocumetacaoMatriculaVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class TermoCompromissoDocumentacaoPendenteRelVO {

	private String nomeAluno;
	private String matricula;
	private String CEP;
	private String endereco;
	private String complemento;
	private String bairro;
	private String cidade;
	private String estado;
	private String email;
	private String telefoneRes;
	private String celular;
	private String RG;
	private String orgaoExpedidor;
	private String estadoExpedidor;
	private String CPF;
	private String nomePai;
	private String nomeMae;
	private String unidadeEnsino;
	private String curso;
	private String turno;
	private List<DocumetacaoMatriculaVO> documentacaoMatriculaVOs;
	private String localData;
	private String nrDiasLimiteEntregaDocumento;
	private String nomeResponsavelFinanceiro;

	public String getNomeAluno() {
		if (nomeAluno == null) {
			nomeAluno = "";
		}
		return nomeAluno;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}

	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getCEP() {
		if (CEP == null) {
			CEP = "";
		}
		return CEP;
	}

	public void setCEP(String CEP) {
		this.CEP = CEP;
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

	public String getBairro() {
		if (bairro == null) {
			bairro = "";
		}
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
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

	public String getEmail() {
		if (email == null) {
			email = "";
		}
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getCelular() {
		if (celular == null) {
			celular = "";
		}
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getRG() {
		if (RG == null) {
			RG = "";
		}
		return RG;
	}

	public void setRG(String RG) {
		this.RG = RG;
	}

	public String getOrgaoExpedidor() {
		if (orgaoExpedidor == null) {
			orgaoExpedidor = "";
		}
		return orgaoExpedidor;
	}

	public void setOrgaoExpedidor(String orgaoExpedidor) {
		this.orgaoExpedidor = orgaoExpedidor;
	}

	public String getCPF() {
		if (CPF == null) {
			CPF = "";
		}
		return CPF;
	}

	public void setCPF(String CPF) {
		this.CPF = CPF;
	}

	public String getNomePai() {
		if (nomePai == null) {
			nomePai = "";
		}
		return nomePai;
	}

	public void setNomePai(String nomePai) {
		this.nomePai = nomePai;
	}

	public String getNomeMae() {
		if (nomeMae == null) {
			nomeMae = "";
		}
		return nomeMae;
	}

	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
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

	public String getCurso() {
		if (curso == null) {
			curso = "";
		}
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
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

	public List<DocumetacaoMatriculaVO> getDocumentacaoMatriculaVOs() {
		if (documentacaoMatriculaVOs == null) {
			documentacaoMatriculaVOs = new ArrayList<DocumetacaoMatriculaVO>(0);
		}
		return documentacaoMatriculaVOs;
	}

	public void setDocumentacaoMatriculaVOs(List<DocumetacaoMatriculaVO> documentacaoMatriculaVOs) {
		this.documentacaoMatriculaVOs = documentacaoMatriculaVOs;
	}

	public JRDataSource getDocumentacaoMatriculaVOsJRDataSource() {
		return new JRBeanArrayDataSource(getDocumentacaoMatriculaVOs().toArray());
	}

	public String getEstadoExpedidor() {
		if (estadoExpedidor == null) {
			estadoExpedidor = "";
		}
		return estadoExpedidor;
	}

	public void setEstadoExpedidor(String estadoExpedidor) {
		this.estadoExpedidor = estadoExpedidor;
	}

	public String getLocalData() {
		if (localData == null) {
			localData = "";
		}
		return localData;
	}

	public void setLocalData(String localData) {
		this.localData = localData;
	}

	public String getNrDiasLimiteEntregaDocumento() {
		if(nrDiasLimiteEntregaDocumento == null){
			nrDiasLimiteEntregaDocumento = "30";
		}
		return nrDiasLimiteEntregaDocumento;
	}

	public void setNrDiasLimiteEntregaDocumento(String nrDiasLimiteEntregaDocumento) {
		this.nrDiasLimiteEntregaDocumento = nrDiasLimiteEntregaDocumento;
	}
	
	public String getNomeResponsavelFinanceiro() {
		if (nomeResponsavelFinanceiro == null) {
			nomeResponsavelFinanceiro = "";
		}
		return nomeResponsavelFinanceiro;
	}

	public void setNomeResponsavelFinanceiro(String nomeResponsavelFinanceiro) {
		this.nomeResponsavelFinanceiro = nomeResponsavelFinanceiro;
	}		

}
