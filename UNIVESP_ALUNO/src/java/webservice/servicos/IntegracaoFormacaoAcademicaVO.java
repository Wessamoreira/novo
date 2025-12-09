package webservice.servicos;

import java.util.Date;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.CidadeVO;

import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;

@XmlRootElement
public class IntegracaoFormacaoAcademicaVO extends SuperVO {

    private String instituicao;
    private String escolaridade;
    private String curso;
    private String situacao;
    private String tipoInst;
    private String dataInicio;
    private String dataFim;
    private String anoDataFim;
    private Integer pessoa;
    private String codigoCidadeIBGE;
    private String nomeCidade;
    private String descricaoAreaConhecimento;

    /**
     * Construtor padrão da classe <code>FormacaoAcademica</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public IntegracaoFormacaoAcademicaVO() {
        super();
    }

    @XmlElement(name = "instituicao")
	public String getInstituicao() {
		if (instituicao == null) {
			instituicao = "";
		}
		return instituicao;
	}

	public void setInstituicao(String instituicao) {
		this.instituicao = instituicao;
	}

	@XmlElement(name = "escolaridade")
	public String getEscolaridade() {
		if (escolaridade == null) {
			escolaridade = "";
		}
		return escolaridade;
	}

	public void setEscolaridade(String escolaridade) {
		this.escolaridade = escolaridade;
	}

	@XmlElement(name = "curso")
	public String getCurso() {
		if (curso == null) {
			curso = "";
		}
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	@XmlElement(name = "situacao")
	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	@XmlElement(name = "tipoInst")
	public String getTipoInst() {
		if (tipoInst == null) {
			tipoInst = "";
		}
		return tipoInst;
	}

	public void setTipoInst(String tipoInst) {
		this.tipoInst = tipoInst;
	}

	@XmlElement(name = "dataInicio")
	public String getDataInicio() {
		if (dataInicio == null) {
			dataInicio = "";
		}
		return dataInicio;
	}

	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}

	@XmlElement(name = "dataFim")
	public String getDataFim() {
		if (dataFim == null) {
			dataFim = "";
		}
		return dataFim;
	}

	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}

	@XmlElement(name = "anoDataFim")
	public String getAnoDataFim() {
		if (anoDataFim == null) {
			anoDataFim = "";
		}
		return anoDataFim;
	}

	public void setAnoDataFim(String anoDataFim) {
		this.anoDataFim = anoDataFim;
	}

	@XmlElement(name = "pessoa")
	public Integer getPessoa() {
		if (pessoa == null) {
			pessoa = 0;
		}
		return pessoa;
	}

	public void setPessoa(Integer pessoa) {
		this.pessoa = pessoa;
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

	@XmlElement(name = "descricaoAreaConhecimento")
	public String getDescricaoAreaConhecimento() {
		if (descricaoAreaConhecimento == null) {
			descricaoAreaConhecimento = "";
		}
		return descricaoAreaConhecimento;
	}

	public void setDescricaoAreaConhecimento(String descricaoAreaConhecimento) {
		this.descricaoAreaConhecimento = descricaoAreaConhecimento;
	}
}
