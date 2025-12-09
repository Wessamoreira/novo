package webservice.servicos;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;

@XmlRootElement
public class IntegracaoFiliacaoVO extends SuperVO {
    
    private String tipo;    
    private String nome;
    private String CPF;
    private String dataNasc;
    private String RG;
    private String CEP;
    private String endereco;
    private String setor;
    private String complemento;
    private String telComercial;
    private String telResidencial;
    private String telRecado;
    private String celular;
    private String codigoCidadeIBGE;

    /**
     * Construtor padrão da classe <code>FormacaoAcademica</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public IntegracaoFiliacaoVO() {
        super();
    }

    @XmlElement(name = "tipo")    
	public String getTipo() {
		if (tipo == null) {
			tipo = "";
		}
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
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

	@XmlElement(name = "telComercial")
	public String getTelComercial() {
		if (telComercial == null) {
			telComercial = "";
		}
		return telComercial;
	}

	public void setTelComercial(String telComercial) {
		this.telComercial = telComercial;
	}

	@XmlElement(name = "telResidencial")
	public String getTelResidencial() {
		if (telResidencial == null) {
			telResidencial = "";
		}
		return telResidencial;
	}

	public void setTelResidencial(String telResidencial) {
		this.telResidencial = telResidencial;
	}

	@XmlElement(name = "telRecado")
	public String getTelRecado() {
		if (telRecado == null) {
			telRecado = "";
		}
		return telRecado;
	}

	public void setTelRecado(String telRecado) {
		this.telRecado = telRecado;
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

	@XmlElement(name = "codigoCidadeIBGE")
	public String getCodigoCidadeIBGE() {
		if (codigoCidadeIBGE == null) {
			codigoCidadeIBGE = "";
		}
		return codigoCidadeIBGE;
	}

	public void setCodigoCidadeIBGE(String codigoCidadeIBGE) {
		this.codigoCidadeIBGE = codigoCidadeIBGE;
	}


}
