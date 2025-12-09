package webservice.servicos;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "pessoa")
public class IntegracaoPessoaVO implements Cloneable {

	private String nome;
	private String endereco;
	private String setor;
	private String numero;
	private String CEP;
	private String complemento;
	private String sexo;
	private String estadoCivil;
	private String telefoneComer;
	private String telefoneRes;
	private String telefoneRecado;
	private String celular;
	private String email;
	private String email2;
	private String paginaPessoal;
	private String dataEmissaoRG;
	private String CPF;
	private String RG;
	private String certificadoMilitar;
	private String pispasep;
	private String dataNasc;
	private String estadoEmissaoRG;
	private String orgaoEmissor;
	private String tituloEleitoral;
	private String necessidadesEspeciais;
	private String corraca;

	private List<IntegracaoFormacaoAcademicaVO> formacaoAcademicaVOs;
	private List<IntegracaoFiliacaoVO> filiacaoVOs;
	// private List<FiliacaoVO> filiacaoVOs;
//	private Integer codigoCidade;
	private String codigoIBGECidade;
	private String nomeCidade;
	private String siglaEstado;
	private String codigoIBGENaturalidade;
	private String nomeNaturalidade;
	private String nomeNacionalidade;
	private String mensagemErro;
	private Integer usuarioResponsavel;

	@XmlElement(name = "mensagemErro")
	public String getMensagemErro() {
		if (mensagemErro == null) {
			mensagemErro = "";
		}
		return mensagemErro;
	}

	public void setMensagemErro(String mensagemErro) {
		this.mensagemErro = mensagemErro;
	}

	@XmlElement(name = "nome")
	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@XmlElement(name = "endereco")
	public String getEndereco() {
		if (endereco == null) {
			endereco = "";
		}
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	@XmlElement(name = "setor")
	public String getSetor() {
		if (setor == null) {
			setor = "";
		}
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	@XmlElement(name = "numero")
	public String getNumero() {
		if (numero == null) {
			numero = "";
		}
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@XmlElement(name = "CEP")
	public String getCEP() {
		if (CEP == null) {
			CEP = "";
		}
		return CEP;
	}

	public void setCEP(String cEP) {
		CEP = cEP;
	}

	@XmlElement(name = "complemento")
	public String getComplemento() {
		if (complemento == null) {
			complemento = "";
		}
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	@XmlElement(name = "sexo")
	public String getSexo() {
		if (sexo == null) {
			sexo = "";
		}
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	@XmlElement(name = "estadoCivil")
	public String getEstadoCivil() {
		if (estadoCivil == null) {
			estadoCivil = "";
		}
		return estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	@XmlElement(name = "telefoneComer")
	public String getTelefoneComer() {
		if (telefoneComer == null) {
			telefoneComer = "";
		}
		return telefoneComer;
	}

	public void setTelefoneComer(String telefoneComer) {
		this.telefoneComer = telefoneComer;
	}

	@XmlElement(name = "telefoneRes")
	public String getTelefoneRes() {
		if (telefoneRes == null) {
			telefoneRes = "";
		}
		return telefoneRes;
	}

	public void setTelefoneRes(String telefoneRes) {
		this.telefoneRes = telefoneRes;
	}

	@XmlElement(name = "telefoneRecado")
	public String getTelefoneRecado() {
		if (telefoneRecado == null) {
			telefoneRecado = "";
		}
		return telefoneRecado;
	}

	public void setTelefoneRecado(String telefoneRecado) {
		this.telefoneRecado = telefoneRecado;
	}

	@XmlElement(name = "celular")
	public String getCelular() {
		if (celular == null) {
			celular = "";
		}
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	@XmlElement(name = "email")
	public String getEmail() {
		if (email == null) {
			email = "";
		}
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@XmlElement(name = "email2")
	public String getEmail2() {
		if (email2 == null) {
			email2 = "";
		}
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	@XmlElement(name = "paginaPessoal")
	public String getPaginaPessoal() {
		if (paginaPessoal == null) {
			paginaPessoal = "";
		}
		return paginaPessoal;
	}

	public void setPaginaPessoal(String paginaPessoal) {
		this.paginaPessoal = paginaPessoal;
	}

	@XmlElement(name = "dataNasc")
	public String getDataNasc() {
		if (dataNasc == null) {
			dataNasc = "";
		}
		return dataNasc;
	}

	public void setDataNasc(String dataNasc) {
		this.dataNasc = dataNasc;
	}

	@XmlElement(name = "CPF")
	public String getCPF() {
		if (CPF == null) {
			CPF = "";
		}
		return CPF;
	}

	public void setCPF(String cPF) {
		CPF = cPF;
	}

	@XmlElement(name = "RG")
	public String getRG() {
		if (RG == null) {
			RG = "";
		}
		return RG;
	}

	public void setRG(String rG) {
		RG = rG;
	}

	@XmlElement(name = "certificadoMilitar")
	public String getCertificadoMilitar() {
		if (certificadoMilitar == null) {
			certificadoMilitar = "";
		}
		return certificadoMilitar;
	}

	public void setCertificadoMilitar(String certificadoMilitar) {
		this.certificadoMilitar = certificadoMilitar;
	}

	@XmlElement(name = "pispasep")
	public String getPispasep() {
		if (pispasep == null) {
			pispasep = "";
		}
		return pispasep;
	}

	public void setPispasep(String pispasep) {
		this.pispasep = pispasep;
	}

	@XmlElement(name = "dataEmissaoRG")
	public String getDataEmissaoRG() {
		if (dataEmissaoRG == null) {
			dataEmissaoRG = "";
		}
		return dataEmissaoRG;
	}

	public void setDataEmissaoRG(String dataEmissaoRG) {
		this.dataEmissaoRG = dataEmissaoRG;
	}

	@XmlElement(name = "estadoEmissaoRG")
	public String getEstadoEmissaoRG() {
		if (estadoEmissaoRG == null) {
			estadoEmissaoRG = "";
		}
		return estadoEmissaoRG;
	}

	public void setEstadoEmissaoRG(String estadoEmissaoRG) {
		this.estadoEmissaoRG = estadoEmissaoRG;
	}

	@XmlElement(name = "orgaoEmissor")
	public String getOrgaoEmissor() {
		if (orgaoEmissor == null) {
			orgaoEmissor = "";
		}
		return orgaoEmissor;
	}

	public void setOrgaoEmissor(String orgaoEmissor) {
		this.orgaoEmissor = orgaoEmissor;
	}

	@XmlElement(name = "tituloEleitoral")
	public String getTituloEleitoral() {
		if (tituloEleitoral == null) {
			tituloEleitoral = "";
		}
		return tituloEleitoral;
	}

	public void setTituloEleitoral(String tituloEleitoral) {
		this.tituloEleitoral = tituloEleitoral;
	}

	@XmlElement(name = "necessidadesEspeciais")
	public String getNecessidadesEspeciais() {
		if (necessidadesEspeciais == null) {
			necessidadesEspeciais = "";
		}
		return necessidadesEspeciais;
	}

	public void setNecessidadesEspeciais(String necessidadesEspeciais) {
		this.necessidadesEspeciais = necessidadesEspeciais;
	}

	@XmlElement(name = "formacaoAcademicaVOs")
	public List<IntegracaoFormacaoAcademicaVO> getFormacaoAcademicaVOs() {
		if (formacaoAcademicaVOs == null) {
			formacaoAcademicaVOs = new ArrayList<IntegracaoFormacaoAcademicaVO>();
		}
		return formacaoAcademicaVOs;
	}

	public void setFormacaoAcademicaVOs(List<IntegracaoFormacaoAcademicaVO> formacaoAcademicaVOs) {
		this.formacaoAcademicaVOs = formacaoAcademicaVOs;
	}

//	@XmlElement(name = "codigoCidade")
//	public Integer getCodigoCidade() {
//		if (codigoCidade == null) {
//			codigoCidade = 0;
//		}
//		return codigoCidade;
//	}
//
//	public void setCodigoCidade(Integer codigoCidade) {
//		this.codigoCidade = codigoCidade;
//	}

	@XmlElement(name = "nomeCidade")
	public String getNomeCidade() {
		if (nomeCidade == null) {
			nomeCidade = "";
		}
		return nomeCidade;
	}

	public void setNomeCidade(String nomeCidade) {
		this.nomeCidade = nomeCidade;
	}

	@XmlElement(name = "codigoIBGENaturalidade")
	public String getCodigoIBGENaturalidade() {
		if (codigoIBGENaturalidade == null) {
			codigoIBGENaturalidade = "";
		}
		return codigoIBGENaturalidade;
	}

	public void setCodigoIBGENaturalidade(String codigoIBGENaturalidade) {
		this.codigoIBGENaturalidade = codigoIBGENaturalidade;
	}

	@XmlElement(name = "nomeNaturalidade")
	public String getNomeNaturalidade() {
		if (nomeNaturalidade == null) {
			nomeNaturalidade = "";
		}
		return nomeNaturalidade;
	}

	public void setNomeNaturalidade(String nomeNaturalidade) {
		this.nomeNaturalidade = nomeNaturalidade;
	}

	@XmlElement(name = "filiacaoVOs")
	public List<IntegracaoFiliacaoVO> getFiliacaoVOs() {
		if (filiacaoVOs == null) {
			filiacaoVOs = new ArrayList<IntegracaoFiliacaoVO>(0);
		}
		return filiacaoVOs;
	}

	public void setFiliacaoVOs(List<IntegracaoFiliacaoVO> filiacaoVOs) {
		this.filiacaoVOs = filiacaoVOs;
	}

	@XmlElement(name = "codigoIBGECidade")
	public String getCodigoIBGECidade() {
		if (codigoIBGECidade == null) {
			codigoIBGECidade = "";
		}
		
		return codigoIBGECidade;
	}

	public void setCodigoIBGECidade(String codigoIBGECidade) {
		this.codigoIBGECidade = codigoIBGECidade;
	}

	@XmlElement(name = "siglaEstado")
	public String getSiglaEstado() {
		if (siglaEstado == null) {
			siglaEstado = "";
		}
		return siglaEstado;
	}

	
	public void setSiglaEstado(String siglaEstado) {
		this.siglaEstado = siglaEstado;
	}

	@XmlElement(name = "corraca")
	public String getCorraca() {
		if (corraca == null) {
			corraca = "";
		}
		return corraca;
	}

	public void setCorraca(String corraca) {
		this.corraca = corraca;
	}

	@XmlElement(name = "nomeNacionalidade")
	public String getNomeNacionalidade() {
		if (nomeNacionalidade == null) {
			nomeNacionalidade = "";
		}
		return nomeNacionalidade;
	}

	public void setNomeNacionalidade(String nomeNacionalidade) {
		this.nomeNacionalidade = nomeNacionalidade;
	}
	
	@XmlElement(name = "usuarioResponsavel")
	public Integer getUsuarioResponsavel() {
		if (usuarioResponsavel == null) {
			usuarioResponsavel = 0;
		}
		return usuarioResponsavel;
	}

	public void setUsuarioResponsavel(Integer usuarioResponsavel) {
		this.usuarioResponsavel = usuarioResponsavel;
	}
	
}