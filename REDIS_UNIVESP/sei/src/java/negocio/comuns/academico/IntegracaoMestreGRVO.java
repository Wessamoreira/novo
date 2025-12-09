package negocio.comuns.academico;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.dominios.OperacaoTempoRealMestreGREnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;

/**
 * @author Rodrigo Ribeiro
 */
@XmlRootElement(name = "integracaoMestreGR")
public class IntegracaoMestreGRVO extends SuperVO {

    private static final long serialVersionUID = 1L;

    private Integer codigo;
    private OperacaoTempoRealMestreGREnum origem;
    private String nomeLote;
    private Integer codigoLote;
    private String situacao;
    private Integer qtdeRegistros;
    private Date created;
    private Date dataTransmissao;
    private String nomeCreated;
    private Integer codigoCreated;
    private String mensagemErro;
    private String dadosEnvio;
    private String dadosRetorno;
    private String urlBase;
    private String token;

    private String ano;
    private String semestre;
    private Integer bimestre;
    private String ensino;
    private String turno;
    private Integer codigoSerie;
    private String nomeSerie;
    private String codigoTurma;
    private String chaveTurma;
    private String codigoInternoTurma;
    private String nomeTurma;

    private Integer codigoDisciplina;
    private String codigosDisciplinas;
    private String siglaDisciplina;
    private String nomeDisciplina;

    private Integer codigoInternoAluno;
    private String codigoAluno;
    private String nomeAluno;
    private String emailAluno;
    private Integer codigoDiaSemanaAluno;
    private String tempoEstendidoAluno;
    private String numeroCelularAluno;

    private Integer codigoInternoPolo;
    private String codigoPolo;
    private String descricaoPolo;

    private Integer codigoInternoCurso;
    private String codigoCurso;
    private String descricaoCurso;

    private String status;
    private String modulo;

    private String dataJson;
    private String statusJson;
    private Integer idItem;
    private String matricula;

    private String abreviaturaDisciplinas;
    
    private Boolean processado;
    
    private Date periodoRequerimentoInicio;
    private Date periodoRequerimentoTermino;
    private String unidadeEnsinos;
    private String cursos;
    private DisciplinaVO disciplinaVO;
    private TipoNivelEducacional nivelEducacional;

    public IntegracaoMestreGRVO() {
        super();
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public OperacaoTempoRealMestreGREnum getOrigem() {
        if (origem == null) {
            origem = OperacaoTempoRealMestreGREnum.TURMA;
        }
        return origem;
    }

    public void setOrigem(OperacaoTempoRealMestreGREnum origem) {
        this.origem = origem;
    }

    public String getNomeLote() {
        if (nomeLote == null) {
            nomeLote = "";
        }
        return nomeLote;
    }

    public void setNomeLote(String nomeLote) {
        this.nomeLote = nomeLote;
    }

    public Integer getCodigoLote() {
        if (codigoLote == null) {
            codigoLote = 0;
        }
        return codigoLote;
    }

    public void setCodigoLote(Integer codigoLote) {
        this.codigoLote = codigoLote;
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

    public Integer getQtdeRegistros() {
        if (qtdeRegistros == null) {
            qtdeRegistros = 0;
        }
        return qtdeRegistros;
    }

    public void setQtdeRegistros(Integer qtdeRegistros) {
        this.qtdeRegistros = qtdeRegistros;
    }

    public Date getDataTransmissao() {
        return dataTransmissao;
    }

    public void setDataTransmissao(Date dataTransmissao) {
        this.dataTransmissao = dataTransmissao;
    }

    public String getMensagemErro() {
        if (mensagemErro == null) {
            mensagemErro = "";
        }
        return mensagemErro;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

    public String getDadosEnvio() {
        if (dadosEnvio == null) {
            dadosEnvio = "";
        }
        return dadosEnvio;
    }

    public void setDadosEnvio(String dadosEnvio) {
        this.dadosEnvio = dadosEnvio;
    }

    public String getDadosRetorno() {
        if (dadosRetorno == null) {
            dadosRetorno = "";
        }
        return dadosRetorno;
    }

    public void setDadosRetorno(String dadosRetorno) {
        this.dadosRetorno = dadosRetorno;
    }

    public String getUrlBase() {
        if (urlBase == null) {
            urlBase = "";
        }
        return urlBase;
    }

    public void setUrlBase(String urlBase) {
        this.urlBase = urlBase;
    }

    public String getToken() {
        if (token == null) {
            token = "";
        }
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getNomeCreated() {
        if (nomeCreated == null) {
            nomeCreated = "";
        }
        return nomeCreated;
    }

    public void setNomeCreated(String nomeCreated) {
        this.nomeCreated = nomeCreated;
    }

    public Integer getCodigoCreated() {
        if (codigoCreated == null) {
            codigoCreated = 0;
        }
        return codigoCreated;
    }

    public void setCodigoCreated(Integer codigoCreated) {
        this.codigoCreated = codigoCreated;
    }

    public String getAno() {
        if (ano == null) {
            ano = Uteis.getAnoDataAtual();
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

    public Integer getBimestre() {
        if (bimestre == null) {
            bimestre = 0;
        }
        return bimestre;
    }

    public void setBimestre(Integer bimestre) {
        this.bimestre = bimestre;
    }

    public String getEnsino() {
        if (ensino == null) {
            ensino = "";
        }
        return ensino;
    }

    public void setEnsino(String ensino) {
        this.ensino = ensino;
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

    public Integer getCodigoSerie() {
        if (codigoSerie == null) {
            codigoSerie = 0;
        }
        return codigoSerie;
    }

    public void setCodigoSerie(Integer codigoSerie) {
        this.codigoSerie = codigoSerie;
    }

    public String getNomeSerie() {
        if (nomeSerie == null) {
            nomeSerie = "";
        }
        return nomeSerie;
    }

    public void setNomeSerie(String nomeSerie) {
        this.nomeSerie = nomeSerie;
    }

    public String getCodigoTurma() {
        if (codigoTurma == null) {
            codigoTurma = "";
        }
        return codigoTurma;
    }

    public void setCodigoTurma(String codigoTurma) {
        this.codigoTurma = codigoTurma;
    }

    public String getChaveTurma() {
        if (chaveTurma == null) {
            chaveTurma = "";
        }
        return chaveTurma;
    }

    public void setChaveTurma(String chaveTurma) {
        this.chaveTurma = chaveTurma;
    }

    public String getCodigoInternoTurma() {
        if (codigoInternoTurma == null) {
            codigoInternoTurma = "";
        }
        return codigoInternoTurma;
    }

    public void setCodigoInternoTurma(String codigoInternoTurma) {
        this.codigoInternoTurma = codigoInternoTurma;
    }

    public String getNomeTurma() {
        if (nomeTurma == null) {
            nomeTurma = "";
        }
        return nomeTurma;
    }

    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }

    public String getStatus() {
        if (status == null) {
            status = "";
        }
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getModulo() {
        if (modulo == null) {
            modulo = "";
        }
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }


    public Integer getCodigoDisciplina() {
        if (codigoDisciplina == null) {
            codigoDisciplina = 0;
        }
        return codigoDisciplina;
    }

    public void setCodigoDisciplina(Integer codigoDisciplina) {
        this.codigoDisciplina = codigoDisciplina;
    }

    public String getSiglaDisciplina() {
        if (siglaDisciplina == null) {
            siglaDisciplina = "";
        }
        return siglaDisciplina;
    }

    public void setSiglaDisciplina(String siglaDisciplina) {
        this.siglaDisciplina = siglaDisciplina;
    }

    public String getNomeDisciplina() {
        if (nomeDisciplina == null) {
            nomeDisciplina = "";
        }
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

    public Integer getCodigoInternoAluno() {
        if (codigoInternoAluno == null) {
            codigoInternoAluno = 0;
        }
        return codigoInternoAluno;
    }

    public void setCodigoInternoAluno(Integer codigoInternoAluno) {
        this.codigoInternoAluno = codigoInternoAluno;
    }

    public String getCodigoAluno() {
        if (codigoAluno == null) {
            codigoAluno = "";
        }
        return codigoAluno;
    }

    public void setCodigoAluno(String codigoAluno) {
        this.codigoAluno = codigoAluno;
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

    public String getEmailAluno() {
        if (emailAluno == null) {
            emailAluno = "";
        }
        return emailAluno;
    }

    public void setEmailAluno(String emailAluno) {
        this.emailAluno = emailAluno;
    }

    public Integer getCodigoDiaSemanaAluno() {
        if (codigoDiaSemanaAluno == null) {
            codigoDiaSemanaAluno = 0;
        }
        return codigoDiaSemanaAluno;
    }

    public void setCodigoDiaSemanaAluno(Integer codigoDiaSemanaAluno) {
        this.codigoDiaSemanaAluno = codigoDiaSemanaAluno;
    }

    public String getTempoEstendidoAluno() {
        if (tempoEstendidoAluno == null) {
            tempoEstendidoAluno = "";
        }
        return tempoEstendidoAluno;
    }

    public void setTempoEstendidoAluno(String tempoEstendidoAluno) {
        this.tempoEstendidoAluno = tempoEstendidoAluno;
    }

    public Integer getCodigoInternoPolo() {
        if (codigoInternoPolo == null) {
            codigoInternoPolo = 0;
        }
        return codigoInternoPolo;
    }

    public void setCodigoInternoPolo(Integer codigoInternoPolo) {
        this.codigoInternoPolo = codigoInternoPolo;
    }

    public String getCodigoPolo() {
        if (codigoPolo == null) {
            codigoPolo = "";
        }
        return codigoPolo;
    }

    public void setCodigoPolo(String codigoPolo) {
        this.codigoPolo = codigoPolo;
    }

    public String getDescricaoPolo() {
        if (descricaoPolo == null) {
            descricaoPolo = "";
        }
        return descricaoPolo;
    }

    public void setDescricaoPolo(String descricaoPolo) {
        this.descricaoPolo = descricaoPolo;
    }

    public Integer getCodigoInternoCurso() {
        if (codigoInternoCurso == null) {
            codigoInternoCurso = 0;
        }
        return codigoInternoCurso;
    }

    public void setCodigoInternoCurso(Integer codigoInternoCurso) {
        this.codigoInternoCurso = codigoInternoCurso;
    }

    public String getCodigoCurso() {
        if (codigoCurso == null) {
            codigoCurso = "";
        }
        return codigoCurso;
    }

    public void setCodigoCurso(String codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

    public String getDescricaoCurso() {
        if (descricaoCurso == null) {
            descricaoCurso = "";
        }
        return descricaoCurso;
    }

    public void setDescricaoCurso(String descricaoCurso) {
        this.descricaoCurso = descricaoCurso;
    }

    public String getDataJson() {
        if (dataJson == null) {
            dataJson = "";
        }
        return dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }

    public String getStatusJson() {
        if (statusJson == null) {
            statusJson = "";
        }
        return statusJson;
    }

    public void setStatusJson(String statusJson) {
        this.statusJson = statusJson;
    }

    public Integer getIdItem() {
        if (idItem == null) {
            idItem = 0;
        }
        return idItem;
    }

    public void setIdItem(Integer idItem) {
        this.idItem = idItem;
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

    public String getCodigosDisciplinas() {
        return codigosDisciplinas;
    }

    public void setCodigosDisciplinas(String codigosDisciplinas) {
        this.codigosDisciplinas = codigosDisciplinas;
    }

    public String getAbreviaturaDisciplinas() {
        if (abreviaturaDisciplinas == null) {
            abreviaturaDisciplinas = "";
        }
        return abreviaturaDisciplinas;
    }

    public void setAbreviaturaDisciplinas(String abreviaturaDisciplinas) {
        this.abreviaturaDisciplinas = abreviaturaDisciplinas;
    }

    public String getNumeroCelularAluno() {
        if (numeroCelularAluno == null) {
            numeroCelularAluno = "";
        }
        return numeroCelularAluno;
    }

    public void setNumeroCelularAluno(String numeroCelularAluno) {
        this.numeroCelularAluno = numeroCelularAluno;
    }

	public Boolean getProcessado() {
		if(processado == null) {
			processado =  false;
		}
		return processado;
	}

	public void setProcessado(Boolean processado) {
		this.processado = processado;
	}

	public String getCodigoDiaSemanaAluno_apresentar() {
		switch (getCodigoDiaSemanaAluno()) {
		case 1:
			return DiaSemana.SEGUNGA.getDescricao();			
		case 2:
			return DiaSemana.TERCA.getDescricao();			
		case 3:
			return DiaSemana.QUARTA.getDescricao();			
		case 4:
			return DiaSemana.QUINTA.getDescricao();			
		case 5:
			return DiaSemana.SEXTA.getDescricao();			
		case 6:
			return DiaSemana.SABADO.getDescricao();			
		case 7:
			return DiaSemana.DOMINGO.getDescricao();			
		case 8:
			return "SABATISTA";			
		default:
			return "";			
		}
	}

	public String getUnidadeEnsinos() {
		if(unidadeEnsinos == null) {
			unidadeEnsinos = "";
		}
		return unidadeEnsinos;
	}

	public void setUnidadeEnsinos(String unidadeEnsinos) {
		this.unidadeEnsinos = unidadeEnsinos;
	}

	public String getCursos() {
		if(cursos == null) {
			cursos = "";
		}
		return cursos;
	}

	public void setCursos(String cursos) {
		this.cursos = cursos;
	}

	public DisciplinaVO getDisciplinaVO() {
		if(disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public Date getPeriodoRequerimentoInicio() {
		return periodoRequerimentoInicio;
	}

	public void setPeriodoRequerimentoInicio(Date periodoRequerimentoInicio) {
		this.periodoRequerimentoInicio = periodoRequerimentoInicio;
	}

	public Date getPeriodoRequerimentoTermino() {
		return periodoRequerimentoTermino;
	}

	public void setPeriodoRequerimentoTermino(Date periodoRequerimentoTermino) {
		this.periodoRequerimentoTermino = periodoRequerimentoTermino;
	}

	public String periodoRequerimento;
	public String getPeriodoRequerimento() {
		if(periodoRequerimento== null) {
			periodoRequerimento =  "";
			if(getPeriodoRequerimentoInicio() != null && getPeriodoRequerimentoTermino() != null) {
				periodoRequerimento = Uteis.getData(getPeriodoRequerimentoInicio())+" à "+Uteis.getData(getPeriodoRequerimentoTermino());
			}
		}
		return periodoRequerimento;
	}

	public TipoNivelEducacional getNivelEducacional() {
		if(nivelEducacional == null) {
			nivelEducacional =  TipoNivelEducacional.SUPERIOR;
		}
		return nivelEducacional;
	}

	public void setNivelEducacional(TipoNivelEducacional nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}
	
    
}
