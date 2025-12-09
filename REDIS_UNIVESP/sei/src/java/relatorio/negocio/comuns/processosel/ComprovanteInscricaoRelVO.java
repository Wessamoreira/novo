/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.processosel;

import negocio.comuns.administrativo.ConfiguracaoCandidatoProcessoSeletivoVO;
import negocio.comuns.arquitetura.SuperVO;

/**
 *
 * @author Philippe
 */
public class ComprovanteInscricaoRelVO extends SuperVO {
    
    private Integer inscricao;
    private String situacaoProcSeletivo;
    private String tituloProcessoSeletivo;
    private String nomeCurso;
    private String nomeTurno;
    private Integer nrPeriodoLetivo;
    private String nomeCandidato;
    private String dataNasc;
    private String sexo;
    private String endereco;
    private String numero;
    private String setor;
    private String complemento;
    private String CEP;
    private String cidade;
    private String estado;
    private String telefoneComer;
    private String telefoneRes;
    private String celular;
    private String email;
    private String RG;
    private String cpf;
    private String orgaoEmissor;
    private String dataEmissaoRG;
    private String necessidadesEspeciais;
    private String dataInscricao;
    private String dataProva;
    private Boolean gravida;
    private Boolean canhoto;
    private Boolean portadorNecessidadeEspecial;
    private String instituicaoEnsinoMedio;
    private String sala;
    private String localProva;
    private String enderecoLocalProva;
    private String formaIngresso;
    private Boolean concluiuEnsinoMedio;
    
    private ConfiguracaoCandidatoProcessoSeletivoVO configuracaoCandidatoProcessoSeletivoVO;

    public Integer getInscricao() {
        if (inscricao == null) {
            inscricao = 0000;
        }
        return inscricao;
    }

    public void setInscricao(Integer inscricao) {
        this.inscricao = inscricao;
    }

    public String getNomeCurso() {
        if (nomeCurso == null) {
            nomeCurso = "";
        }
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public String getNomeTurno() {
        if (nomeTurno == null) {
            nomeTurno = "";
        }
        return nomeTurno;
    }

    public void setNomeTurno(String nomeTurno) {
        this.nomeTurno = nomeTurno;
    }

    public Integer getNrPeriodoLetivo() {
        if (nrPeriodoLetivo == null) {
            nrPeriodoLetivo = 1;
        }
        return nrPeriodoLetivo;
    }

    public void setNrPeriodoLetivo(Integer nrPeriodoLetivo) {
        this.nrPeriodoLetivo = nrPeriodoLetivo;
    }

    public String getNomeCandidato() {
        if (nomeCandidato == null) {
            nomeCandidato =  "";
        }
        return nomeCandidato;
    }

    public void setNomeCandidato(String nomeCandidato) {
        this.nomeCandidato = nomeCandidato;
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

    public String getSexo() {
        if (sexo == null) {
            sexo = "";
        }
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
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

    public String getSetor() {
        if (setor == null) {
            setor = "";
        }
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
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

    public String getCEP() {
        if (CEP == null) {
            CEP = "";
        }
        return CEP;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
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

    public String getTelefoneComer() {
        if (telefoneComer == null) {
            telefoneComer = "";
        }
        return telefoneComer;
    }

    public void setTelefoneComer(String telefoneComer) {
        this.telefoneComer = telefoneComer;
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

    public String getEmail() {
        if (email == null) {
            email = "";
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getOrgaoEmissor() {
        if (orgaoEmissor == null) {
            orgaoEmissor = "";
        }
        return orgaoEmissor;
    }

    public void setOrgaoEmissor(String orgaoEmissor) {
        this.orgaoEmissor = orgaoEmissor;
    }

    public String getDataEmissaoRG() {
        if (dataEmissaoRG == null) {
            dataEmissaoRG = "";
        }
        return dataEmissaoRG;
    }

    public void setDataEmissaoRG(String dataEmissaoRG) {
        this.dataEmissaoRG = dataEmissaoRG;
    }

    public String getNecessidadesEspeciais() {
        if (necessidadesEspeciais == null) {
            necessidadesEspeciais = "";
        }
        return necessidadesEspeciais;
    }

    public void setNecessidadesEspeciais(String necessidadesEspeciais) {
        this.necessidadesEspeciais = necessidadesEspeciais;
    }

    public String getDataInscricao() {
        if (dataInscricao == null) {
            dataInscricao = "";
        }
        return dataInscricao;
    }

    public void setDataInscricao(String dataInscricao) {
        this.dataInscricao = dataInscricao;
    }

    public String getTituloProcessoSeletivo() {
        if (tituloProcessoSeletivo == null) {
            tituloProcessoSeletivo = "";
        }
        return tituloProcessoSeletivo;
    }

    public void setTituloProcessoSeletivo(String tituloProcessoSeletivo) {
        this.tituloProcessoSeletivo = tituloProcessoSeletivo;
    }

	public String getDataProva() {
		if (dataProva == null) {
			dataProva = "";
		}
		return dataProva;
	}

	public void setDataProva(String dataProva) {
		this.dataProva = dataProva;
	}
	
	
	public Boolean getPortadorNecessidadeEspecial() {
		if (portadorNecessidadeEspecial == null) {
			portadorNecessidadeEspecial = Boolean.FALSE;
		}
		return portadorNecessidadeEspecial;
	}

	public void setPortadorNecessidadeEspecial(Boolean portadorNecessidadeEspecial) {
		this.portadorNecessidadeEspecial = portadorNecessidadeEspecial;
	}

	public Boolean getGravida() {
		if (gravida == null) {
			gravida = Boolean.FALSE;
		}
		return gravida;
	}

	public void setGravida(Boolean gravida) {
		this.gravida = gravida;
	}

	public Boolean getCanhoto() {
		if (canhoto == null) {
			canhoto = Boolean.FALSE;
		}
		return canhoto;
	}

	public void setCanhoto(Boolean canhoto) {
		this.canhoto = canhoto;
	}

	public String getInstituicaoEnsinoMedio() {
		if(instituicaoEnsinoMedio == null){
			instituicaoEnsinoMedio = "";
		}
		return instituicaoEnsinoMedio;
	}

	public void setInstituicaoEnsinoMedio(String instituicaoEnsinoMedio) {
		this.instituicaoEnsinoMedio = instituicaoEnsinoMedio;
	}

	public String getCpf() {
		if(cpf == null){
			cpf = "";
		}
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getSala() {
		if (sala == null) {
			sala = "";
		}
		return sala;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}

	public String getLocalProva() {
		if (localProva == null) {
			localProva = "";
		}
		return localProva;
	}

	public void setLocalProva(String localProva) {
		this.localProva = localProva;
	}

	public String getEnderecoLocalProva() {
		if (enderecoLocalProva == null) {
			enderecoLocalProva = "";
		}
		return enderecoLocalProva;
	}

	public void setEnderecoLocalProva(String enderecoLocalProva) {
		this.enderecoLocalProva = enderecoLocalProva;
	}

	public String getSituacaoProcSeletivo() {
		if (situacaoProcSeletivo == null) {
			situacaoProcSeletivo = "";
		}
		return situacaoProcSeletivo;
	}

	public void setSituacaoProcSeletivo(String situacaoProcSeletivo) {
		this.situacaoProcSeletivo = situacaoProcSeletivo;
	}

	public String getSituacaoProcSeletivo_Apresentar() {
		if (getSituacaoProcSeletivo().equals("PF")) {
			return "Pendente Financeiramente";
		}
		if (getSituacaoProcSeletivo().equals("CO")) {
			return "Confirmada";
		}
		return (getSituacaoProcSeletivo());
	}    
	
	public String getFormaIngresso() {
		if (formaIngresso == null) {
			formaIngresso = "";
		}
		return formaIngresso;
	}

	public void setFormaIngresso(String formaIngresso) {
		this.formaIngresso = formaIngresso;
	}

	public ConfiguracaoCandidatoProcessoSeletivoVO getConfiguracaoCandidatoProcessoSeletivoVO() {
		if(configuracaoCandidatoProcessoSeletivoVO == null) {
			configuracaoCandidatoProcessoSeletivoVO = new ConfiguracaoCandidatoProcessoSeletivoVO();
		}
		return configuracaoCandidatoProcessoSeletivoVO;
	}

	public void setConfiguracaoCandidatoProcessoSeletivoVO(
			ConfiguracaoCandidatoProcessoSeletivoVO configuracaoCandidatoProcessoSeletivoVO) {
		this.configuracaoCandidatoProcessoSeletivoVO = configuracaoCandidatoProcessoSeletivoVO;
	}

	public Boolean getConcluiuEnsinoMedio() {
		if(concluiuEnsinoMedio == null) {
			concluiuEnsinoMedio = Boolean.FALSE;
		}
		return concluiuEnsinoMedio;
	}

	public void setConcluiuEnsinoMedio(Boolean concluiuEnsinoMedio) {
		this.concluiuEnsinoMedio = concluiuEnsinoMedio;
	}

	
	
}
