package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.dominios.SituacaoMatriculaPeriodo;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 *
 * @author Carlos
 */
public class AlunosPorUnidadeCursoTurmaRelVOs extends SuperVO {

    private String matricula;
    private String situacao;
    private String situacaoMatriculaPeriodo;
    private String nomeAluno;
    private Integer codigoAluno;
    private String dataNascimento;
    private String cpf;
    private String telefoneRes;
    private String celular;
    private String email;
    private String turmaReposicao;
    private String disciplinaReposicao;
    private Boolean alunoAbandonouCurso;
    
    private String rg;
    private String orgaoEmissor;
    private String estadoEmissaoRg;
    private String dataEmissaoRg;
    private String endereco;
    private String setor;
    private String cep;
    private String cidade;
    private List<FiliacaoVO> listaFiliacao;

    public String getMatricula() {
        if (matricula == null) {
            matricula = "";
        }
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNomeAluno() {
        if (nomeAluno == null) {
            nomeAluno = "";
        }
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
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

    public String getCpf() {
        if (cpf == null) {
            cpf = "";
        }
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    public String getTurmaReposicao() {
        if (turmaReposicao == null) {
            turmaReposicao = "";
        }
        return turmaReposicao;
    }

    public void setTurmaReposicao(String turmaReposicao) {
        this.turmaReposicao = turmaReposicao;
    }

    public String getDisciplinaReposicao() {
        if (disciplinaReposicao == null) {
            disciplinaReposicao = "";
        }
        return disciplinaReposicao;
    }

    public void setDisciplinaReposicao(String disciplinaReposicao) {
        this.disciplinaReposicao = disciplinaReposicao;
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "";
        }
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getSituacao_Apresentar() {
        try {
            if (getSituacao().equals("TU")) {
                return "Transferido Unidade";
            }
            if ((SituacaoVinculoMatricula.getEnum(getSituacao()).equals(SituacaoVinculoMatricula.TRANCADA)) && (this.getAlunoAbandonouCurso())) {
                return SituacaoVinculoMatricula.getDescricao("AC");
            }
            return SituacaoVinculoMatricula.getDescricao(situacao);
        } catch (Exception e) {
            return SituacaoVinculoMatricula.getDescricao("ER");
        }
    }

    public Boolean getAlunoAbandonouCurso() {
        if (alunoAbandonouCurso == null) {
            alunoAbandonouCurso = Boolean.FALSE;
        }
        return alunoAbandonouCurso;
    }

    public void setAlunoAbandonouCurso(Boolean alunoAbandonouCurso) {
        this.alunoAbandonouCurso = alunoAbandonouCurso;
    }

	public List<FiliacaoVO> getListaFiliacao() {
		if (listaFiliacao == null) {
			listaFiliacao = new ArrayList<FiliacaoVO>(0);
		}
		return listaFiliacao;
	}

	public void setListaFiliacao(List<FiliacaoVO> listaFiliacao) {
		this.listaFiliacao = listaFiliacao;
	}
	
	public JRDataSource getListaFiliacaoJR() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaFiliacao().toArray());
        return jr;
    }

	public Integer getCodigoAluno() {
		if (codigoAluno == null) {
			codigoAluno = 0;
		}
		return codigoAluno;
	}

	public void setCodigoAluno(Integer codigoAluno) {
		this.codigoAluno = codigoAluno;
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

	public String getOrgaoEmissor() {
		if (orgaoEmissor == null) {
			orgaoEmissor = "";
		}
		return orgaoEmissor;
	}

	public void setOrgaoEmissor(String orgaoEmissor) {
		this.orgaoEmissor = orgaoEmissor;
	}

	public String getDataEmissaoRg() {
		if (dataEmissaoRg == null) {
			dataEmissaoRg = "";
		}
		return dataEmissaoRg;
	}

	public void setDataEmissaoRg(String dataEmissaoRg) {
		this.dataEmissaoRg = dataEmissaoRg;
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

	public String getSetor() {
		if (setor == null) {
			setor = "";
		}
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
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

	public String getSituacaoMatriculaPeriodo() {
		if(situacaoMatriculaPeriodo == null){
			situacaoMatriculaPeriodo = "";
		}
		return situacaoMatriculaPeriodo;
	}
	
	public String getSituacaoMatriculaPeriodoApresentar() {
		if(situacaoMatriculaPeriodo == null || situacaoMatriculaPeriodo.trim().equals("")){
			return "";
		}
		return SituacaoMatriculaPeriodoEnum.getEnumPorValor(situacaoMatriculaPeriodo).getDescricao();		
	}

	public void setSituacaoMatriculaPeriodo(String situacaoMatriculaPeriodo) {
		this.situacaoMatriculaPeriodo = situacaoMatriculaPeriodo;
	}

	public String getEstadoEmissaoRg() {
		if(estadoEmissaoRg == null) {
			estadoEmissaoRg = "";
		}
		return estadoEmissaoRg;
	}

	public void setEstadoEmissaoRg(String estadoEmissaoRg) {
		this.estadoEmissaoRg = estadoEmissaoRg;
	}
	
	
}
