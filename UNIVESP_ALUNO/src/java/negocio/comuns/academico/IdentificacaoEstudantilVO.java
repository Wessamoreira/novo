package negocio.comuns.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class IdentificacaoEstudantilVO implements Serializable {

    private String nomeAluno;
    private String fotoAluno;
    private String pastaBaseArquivo;
    private String pastaBaseArquivoWeb;
    private String nomeArquivo;
    private Integer codigoArquivo;
    private String nomeCurso;
    private String matricula;
    private String unidadeEnsino;
    private String dataVencimento;
    private String endereco;
    private String cidade;
    private String estado;
    private String cep;
    private String rg;
    private String setor;
    private String turno;
    private String anoLetivo;
    private String turma;
    private String nivelEnsino;
    private String semestre;
    private String periodo;
    private String dataNascimento;
    private String enderecoUnidade;
    private String setorUnidade;
    private String cidadeUnidade;
    private String estadoUnidade;
    private String cepUnidade;
    private String telUnidade;
//    private List<LocalPeriodoVO> listaLocalPeriodo;
    private String orgaoResponsavelEmissaoCarteirinha;
    private Boolean selecionar;
    private String cpf;
    private String tipoPessoa;
    private String pis;
    private String tituloDaIdentificacao;
    private String numero;
    private String complemento;
    
    public static final long serialVersionUID = 1L;

    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getDataVencimento() {
    	if (dataVencimento == null) {
    		dataNascimento = "";
    	}
        return dataVencimento;
    }

    public void setDataVencimento(String dataVencimento) {
        this.dataVencimento = dataVencimento;
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

    public String getEndereco() {
        if (endereco == null) {
            endereco = "";
        }
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
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

    public String getCep() {
        if (cep == null) {
            cep = "";
        }
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
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

    public String getAnoLetivo() {
        if (anoLetivo == null) {
            anoLetivo = "";
        }
        return anoLetivo;
    }

    public void setAnoLetivo(String anoLetivo) {
        this.anoLetivo = anoLetivo;
    }

    public String getTurma() {
        if (turma == null) {
            turma = "";
        }
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public String getNivelEnsino() {
        if (nivelEnsino == null) {
            nivelEnsino = "";
        }
        return nivelEnsino;
    }

    public void setNivelEnsino(String nivelEnsino) {
        this.nivelEnsino = nivelEnsino;
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

    public String getDataNascimento() {
        if (dataNascimento == null) {
            dataNascimento = "";
        }
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEnderecoUnidade() {
        if (enderecoUnidade == null) {
            enderecoUnidade = "";
        }
        return enderecoUnidade;
    }

    public void setEnderecoUnidade(String enderecoUnidade) {
        this.enderecoUnidade = enderecoUnidade;
    }

    public String getCidadeUnidade() {
        if (cidadeUnidade == null) {
            cidadeUnidade = "";
        }
        return cidadeUnidade;
    }

    public void setCidadeUnidade(String cidadeUnidade) {
        this.cidadeUnidade = cidadeUnidade;
    }

    public String getEstadoUnidade() {
        if (estadoUnidade == null) {
            estadoUnidade = "";
        }
        return estadoUnidade;
    }

    public void setEstadoUnidade(String estadoUnidade) {
        this.estadoUnidade = estadoUnidade;
    }

    public String getCepUnidade() {
        if (cepUnidade == null) {
            cepUnidade = "";
        }
        return cepUnidade;
    }

    public void setCepUnidade(String cepUnidade) {
        this.cepUnidade = cepUnidade;
    }

    public String getTelUnidade() {
        if (telUnidade == null) {
            telUnidade = "";
        }
        return telUnidade;
    }

    public void setTelUnidade(String telUnidade) {
        this.telUnidade = telUnidade;
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

    public String getSetorUnidade() {
        if (setorUnidade == null) {
            setorUnidade = "";
        }
        return setorUnidade;
    }

    public void setSetorUnidade(String setorUnidade) {
        this.setorUnidade = setorUnidade;
    }

    public String getFotoAluno() {
        if (fotoAluno == null) {
            fotoAluno = "";
        }
        return fotoAluno;
    }

    public void setFotoAluno(String fotoAluno) {
        this.fotoAluno = fotoAluno;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

//    public List<LocalPeriodoVO> getListaLocalPeriodo() {
//        if (listaLocalPeriodo == null) {
//            listaLocalPeriodo = new ArrayList<LocalPeriodoVO>(0);
//        }
//        return listaLocalPeriodo;
//    }
//
//    public void setListaLocalPeriodo(List<LocalPeriodoVO> listaLocalPeriodo) {
//        this.listaLocalPeriodo = listaLocalPeriodo;
//    }
//
//    public JRDataSource getListaLocalPeriodoJR() {
//        JRDataSource jr = new JRBeanArrayDataSource(getListaLocalPeriodo().toArray());
//        return jr;
//    }

    public String getOrgaoResponsavelEmissaoCarteirinha() {
        if (orgaoResponsavelEmissaoCarteirinha == null) {
            orgaoResponsavelEmissaoCarteirinha = "";
        }
        return orgaoResponsavelEmissaoCarteirinha;
    }

    public void setOrgaoResponsavelEmissaoCarteirinha(String orgaoResponsavelEmissaoCarteirinha) {
        this.orgaoResponsavelEmissaoCarteirinha = orgaoResponsavelEmissaoCarteirinha;
    }

    /**
     * @return the rg
     */
    public String getRg() {
        if (rg == null) {
            rg = "";
        }
        return rg;
    }

    /**
     * @param rg the rg to set
     */
    public void setRg(String rg) {
        this.rg = rg;
    }

	public String getPastaBaseArquivo() {
		if(pastaBaseArquivo == null){
			pastaBaseArquivo = "";
		}
		return pastaBaseArquivo;
	}

	public void setPastaBaseArquivo(String pastaBaseArquivo) {
		this.pastaBaseArquivo = pastaBaseArquivo;
	}

	public String getPastaBaseArquivoWeb() {
		if(pastaBaseArquivoWeb == null){
			pastaBaseArquivoWeb = "";
		}
		return pastaBaseArquivoWeb;
	}

	public void setPastaBaseArquivoWeb(String pastaBaseArquivoWeb) {
		this.pastaBaseArquivoWeb = pastaBaseArquivoWeb;
	}

	public String getNomeArquivo() {
		if(nomeArquivo == null){
			nomeArquivo = "";
		}
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public Integer getCodigoArquivo() {
		if(codigoArquivo == null){
			codigoArquivo = 0;
		}
		return codigoArquivo;
	}

	public void setCodigoArquivo(Integer codigoArquivo) {
		this.codigoArquivo = codigoArquivo;
	}

	public Boolean getSelecionar() {
		if(selecionar == null){
			selecionar = false;
		}
		return selecionar;
	}

	public void setSelecionar(Boolean selecionar) {
		this.selecionar = selecionar;
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
	public String getTipoPessoa() {
		if(tipoPessoa == null){
			tipoPessoa = "";
		}
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}


	public String getPis() {
		if(pis == null){
			pis = "";
		}
		return pis;
	}

	public void setPis(String pis) {
		this.pis = pis;
	}

	public String getTituloDaIdentificacao() {
		if(tituloDaIdentificacao == null){
			tituloDaIdentificacao = "";
		}
		return tituloDaIdentificacao;
	}

	public void setTituloDaIdentificacao(String tituloDaIdentificacao) {
		this.tituloDaIdentificacao = tituloDaIdentificacao;
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

	public String getComplemento() {
		if (complemento == null) {
			complemento = "";
		}
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	
	
	
	
}
