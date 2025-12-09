/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.academico;

/**
 * 
 * @author Otimize-TI
 */
public class DeclaracaoPasseEstudantilVO {

    protected String nome;
    protected String rg;
    protected String orgaoExpedidor;
    protected String dataExpedicao;
    protected String cpf;
    protected String periodoLetivo;
    protected String curso;
    protected String turno;
    protected String observacao;
    protected String filiacao;
    protected String sexo;
    protected String naturalidade;
    protected String data;
    protected String dataNascimento;
    protected String matricula;
    protected String endereco;
    protected String bairro;
    protected String cidade;
    protected String cep;
    protected String cidadeUnid;
    protected Integer textoPadrao;

    public DeclaracaoPasseEstudantilVO() {
    }

    public String getNome() {
        if (nome == null) {
            nome = "";
        }
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRg() {
        if (rg == null) {
            rg = "";
        }
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        if (cpf == null) {
            cpf = "";
        }
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    public String getCurso() {
        if (curso == null) {
            curso = "";
        }
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getData() {
        if (data == null) {
            data = "";
        }
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataNascimento() {
        if (dataNascimento == null) {
            dataNascimento = "";
        }
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
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

    public String getDataExpedicao() {
        if (dataExpedicao == null) {
            dataExpedicao = "";
        }
        return dataExpedicao;
    }

    public void setDataExpedicao(String dataExpedicao) {
        this.dataExpedicao = dataExpedicao;
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

    public String getObservacao() {
        if (observacao == null) {
            observacao = "";
        }
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getFiliacao() {
        if (filiacao == null) {
            filiacao = "";
        }
        return filiacao;
    }

    public void setFiliacao(String filiacao) {
        this.filiacao = filiacao;
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

    public String getNaturalidade() {
        if (naturalidade == null) {
            naturalidade = "";
        }
        return naturalidade;
    }

    public void setNaturalidade(String naturalidade) {
        this.naturalidade = naturalidade;
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

    public String getCep() {
        if (cep == null) {
            cep = "";
        }
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidadeUnid() {
        if (cidadeUnid == null) {
            cidadeUnid = "";
        }
        return cidadeUnid;
    }

    public void setCidadeUnid(String cidadeUnid) {
        this.cidadeUnid = cidadeUnid;
    }

    /**
     * @return the orgaoExpedidor
     */
    public String getOrgaoExpedidor() {
        if (orgaoExpedidor == null) {
            orgaoExpedidor = "";
        }
        return orgaoExpedidor;
    }

    /**
     * @param orgaoExpedidor the orgaoExpedidor to set
     */
    public void setOrgaoExpedidor(String orgaoExpedidor) {
        this.orgaoExpedidor = orgaoExpedidor;
    }
    
    public Integer getTextoPadrao() {
		if(textoPadrao == null){
			textoPadrao = 0;
		}
		return textoPadrao;
	}

	public void setTextoPadrao(Integer textoPadrao) {
		this.textoPadrao = textoPadrao;
	}
}
