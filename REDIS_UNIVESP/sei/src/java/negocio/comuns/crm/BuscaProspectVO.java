/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.crm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Philippe
 */
public class BuscaProspectVO {

    //Dados tabela
    private Date dataContato;
    private Date dataCadastro;
    private String nomeProspect;
    private String emailProspect;
    private String nomeConsultor;
    private String observacao;
    private String situacao;
    private String turmaMatriculado;
    private CursoVO cursoNovaInteracao;
    private ProspectsVO prospectNovaInteracao;
    //Dados pesquisa
    private UnidadeEnsinoVO unidadeEnsino;
    private CursoVO curso;
    private FuncionarioVO consultor;
    private FuncionarioVO consultorResponsavel;
    private ProspectsVO prospect;
    private CidadeVO cidade;
    private String situacaoProspect;
    private String ordenacao;
    private String tipoPeriodo;
    private String email;
    private String telefone;
    private String telefoneResidencial;
    private String telefoneComercial;
    private String celular;
    private Date dataInicio;
    private Date dataFim;
    private String iconeDescricaoSituacaoProspect;
    private String descricaoSituacaoProspect;
    private List<CompromissoAgendaPessoaHorarioVO> compromissoAgendaPessoaHorarioVOs;
    private String cpf;
    
    private Boolean possuiMatricula;
    private List<MatriculaVO> matriculaVOs;
    
    public Date getDataContato() {
        if (dataContato == null) {
        	dataContato = new Date(0,0,0);
        }
    	return dataContato;
    }
    
    public Boolean getExisteContatoRegistrado() {
        if (!Uteis.getData(getDataContato()).equals(Uteis.getData(new Date(0,0,0)))) {
            return true;
        }
        return false;
    }

    public String getDataContato_Apresentar() {
        if (!Uteis.getData(getDataContato()).equals(Uteis.getData(new Date(0,0,0)))) {
            return Uteis.getData(dataContato);
        }
        return "Não há contato registrado";
    }

    public void setDataContato(Date dataContato) {
        this.dataContato = dataContato;
    }

    public String getNomeProspect() {
        if (nomeProspect == null) {
            nomeProspect = "";
        }
        return nomeProspect;
    }

    public void setNomeProspect(String nomeProspect) {
        this.nomeProspect = nomeProspect;
    }

    public String getEmailProspect() {
        if (emailProspect == null) {
            emailProspect = "";
        }
        return emailProspect;
    }

    public void setEmailProspect(String emailProspect) {
        this.emailProspect = emailProspect;
    }

    public String getNomeConsultor() {
        if (nomeConsultor == null) {
            nomeConsultor = "";
        }
        return nomeConsultor;
    }

    public void setNomeConsultor(String nomeConsultor) {
        this.nomeConsultor = nomeConsultor;
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

    public String getObservacao_Apresentar() {
        if (getObservacao().length() > 50) {
            return getObservacao().substring(0, 50);
        } else {
            return getObservacao();
        }
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

    public String getTurmaMatriculado() {
        if (turmaMatriculado == null) {
            turmaMatriculado = "";
        }
        return turmaMatriculado;
    }

    public void setTurmaMatriculado(String turmaMatriculado) {
        this.turmaMatriculado = turmaMatriculado;
    }

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return curso;
    }

    public void setCurso(CursoVO curso) {
        this.curso = curso;
    }

    public FuncionarioVO getConsultor() {
        if (consultor == null) {
            consultor = new FuncionarioVO();
        }
        return consultor;
    }

    public void setConsultor(FuncionarioVO consultor) {
        this.consultor = consultor;
    }

    public ProspectsVO getProspect() {
        if (prospect == null) {
            prospect = new ProspectsVO();
        }
        return prospect;
    }

    public void setProspect(ProspectsVO prospect) {
        this.prospect = prospect;
    }

    public CidadeVO getCidade() {
        if (cidade == null) {
            cidade = new CidadeVO();
        }
        return cidade;
    }

    public void setCidade(CidadeVO cidade) {
        this.cidade = cidade;
    }

    public String getSituacaoProspect() {
        if (situacaoProspect == null) {
            situacaoProspect = "";
        }
        return situacaoProspect;
    }

    public void setSituacaoProspect(String situacaoProspect) {
        this.situacaoProspect = situacaoProspect;
    }

    public String getOrdenacao() {
        if (ordenacao == null) {
            ordenacao = "";
        }
        return ordenacao;
    }

    public void setOrdenacao(String ordenacao) {
        this.ordenacao = ordenacao;
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

    public String getTelefone() {
        if (telefone == null) {
            telefone = "";
        }
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public String getTipoPeriodo() {
        if (tipoPeriodo == null) {
            tipoPeriodo = "";
        }
        return tipoPeriodo;
    }

    public void setTipoPeriodo(String tipoPeriodo) {
        this.tipoPeriodo = tipoPeriodo;
    }

    public ProspectsVO getProspectNovaInteracao() {
        if (prospectNovaInteracao == null) {
            prospectNovaInteracao = new ProspectsVO();
        }
        return prospectNovaInteracao;
    }

    public void setProspectNovaInteracao(ProspectsVO prospectNovaInteracao) {
        this.prospectNovaInteracao = prospectNovaInteracao;
    }

    public CursoVO getCursoNovaInteracao() {
        if (cursoNovaInteracao == null) {
            cursoNovaInteracao = new CursoVO();
        }
        return cursoNovaInteracao;
    }

    public void setCursoNovaInteracao(CursoVO cursoNovaInteracao) {
        this.cursoNovaInteracao = cursoNovaInteracao;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getDataCadastro_Apresentar() {
        if (dataCadastro != null) {
            return Uteis.getData(dataCadastro);
        }
        return "";
    }

	public String getIconeDescricaoSituacaoProspect() {
		if (iconeDescricaoSituacaoProspect == null) {
			iconeDescricaoSituacaoProspect = "";
		}
		return iconeDescricaoSituacaoProspect;
	}

	public void setIconeDescricaoSituacaoProspect(
			String iconeDescricaoSituacaoProspect) {
		this.iconeDescricaoSituacaoProspect = iconeDescricaoSituacaoProspect;
	}

	public String getDescricaoSituacaoProspect() {
		if (descricaoSituacaoProspect == null) {
			descricaoSituacaoProspect = "";
		}
		return descricaoSituacaoProspect;
	}

	public void setDescricaoSituacaoProspect(String descricaoSituacaoProspect) {
		this.descricaoSituacaoProspect = descricaoSituacaoProspect;
	}


    /**
     * @return the consultorResponsavel
     */
    public FuncionarioVO getConsultorResponsavel() {
        if (consultorResponsavel == null) {
            consultorResponsavel = new FuncionarioVO();
        }
        return consultorResponsavel;
    }

    /**
     * @param consultorResponsavel the consultorResponsavel to set
     */
    public void setConsultorResponsavel(FuncionarioVO consultorResponsavel) {
        this.consultorResponsavel = consultorResponsavel;
    }

	public List<CompromissoAgendaPessoaHorarioVO> getCompromissoAgendaPessoaHorarioVOs() {
		if(compromissoAgendaPessoaHorarioVOs == null){
			compromissoAgendaPessoaHorarioVOs = new ArrayList<CompromissoAgendaPessoaHorarioVO>(0);
		}
		return compromissoAgendaPessoaHorarioVOs;
	}

	public void setCompromissoAgendaPessoaHorarioVOs(List<CompromissoAgendaPessoaHorarioVO> compromissoAgendaPessoaHorarioVOs) {
		this.compromissoAgendaPessoaHorarioVOs = compromissoAgendaPessoaHorarioVOs;
	}

	public Boolean getPossuiMatricula() {
		if(possuiMatricula == null){
			possuiMatricula = false;
		}
		return possuiMatricula;
	}

	public void setPossuiMatricula(Boolean possuiMatricula) {
		this.possuiMatricula = possuiMatricula;
	}

	public List<MatriculaVO> getMatriculaVOs() {
		if(matriculaVOs == null){
			matriculaVOs = new ArrayList<MatriculaVO>(0);
		}
		return matriculaVOs;
	}

	public void setMatriculaVOs(List<MatriculaVO> matriculaVOs) {
		this.matriculaVOs = matriculaVOs;
	}

	public String getTelefoneResidencial() {
		if (telefoneResidencial == null) {
			telefoneResidencial = "";
		}
		return telefoneResidencial;
	}

	public void setTelefoneResidencial(String telefoneResidencial) {
		this.telefoneResidencial = telefoneResidencial;
	}

	public String getTelefoneComercial() {
		if (telefoneComercial == null) {
			telefoneComercial= "";
		}
		return telefoneComercial;
	}

	public void setTelefoneComercial(String telefoneComercial) {
		this.telefoneComercial = telefoneComercial;
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
    
	public String getCpf() {
		if (cpf == null) {
			cpf = "";
		}
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	private Boolean maximizar;
	private Boolean minimizar;
	
	public Boolean getMaximizar() {
		if (maximizar == null) {
			maximizar = false;
		}
		return maximizar;
	}
	
	public void setMaximizar(Boolean maximizar) {
		this.maximizar = maximizar;
	}
	
	public Boolean getMinimizar() {
		if (minimizar == null) {
			minimizar = false;
		}
		return minimizar;
	}
	
	public void setMinimizar(Boolean minimizar) {
		this.minimizar = minimizar;
	}

}