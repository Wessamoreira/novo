package controle.arquitetura;

import java.util.Date;

public class ControleConsultaIntegracaoMestreGR extends DataModelo {

    /**
     *
     */
    private static final long serialVersionUID = -2333143314490871104L;

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
    private Integer codigo;

    private Integer codigoIntegracao;

    private String origem;
    private String nomeLote;
    private Integer codigoLote;
    private String situacao;
    private Integer qtdeRegistros;

    private Integer codigoDisciplina;
    private String siglaDisciplina;
    private String nomeDisciplina;

    private Integer codigoInternoAluno;
    private String codigoAluno;
    private String nomeAluno;
    private String emailAluno;
    private Integer codigoDiaSemanaAluno;
    private String matricula;

    private Integer codigoInternoPolo;
    private String codigoPolo;
    private String descricaoPolo;
    private String tempoEstendidoAluno;
    private String numeroCelularAluno;

    private Integer codigoInternoCurso;
    private String codigoCurso;
    private String descricaoCurso;
    private Integer idItem;
    private String codigosDisciplinas;
    private String abreviaturaDisciplinas;
    
    private String mensagemErro;

    private Date created;
    private Date createdFinal;

    public String getAno() {
        if (ano == null){
            ano = "";
        }
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getSemestre() {
        if (semestre == null){
            semestre = "";
        }
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public Integer getBimestre() {
        if (bimestre == null){
            bimestre = 0;
        }
        return bimestre;
    }

    public void setBimestre(Integer bimestre) {
        this.bimestre = bimestre;
    }

    public String getEnsino() {
        return ensino;
    }

    public void setEnsino(String ensino) {
        this.ensino = ensino;
    }

    public String getTurno() {
        if (turno == null){
            turno = "";
        }
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public Integer getCodigoSerie() {
        return codigoSerie;
    }

    public void setCodigoSerie(Integer codigoSerie) {
        this.codigoSerie = codigoSerie;
    }

    public String getNomeSerie() {
        return nomeSerie;
    }

    public void setNomeSerie(String nomeSerie) {
        this.nomeSerie = nomeSerie;
    }

    public String getCodigoTurma() {
        return codigoTurma;
    }

    public void setCodigoTurma(String codigoTurma) {
        this.codigoTurma = codigoTurma;
    }

    public String getChaveTurma() {
        return chaveTurma;
    }

    public void setChaveTurma(String chaveTurma) {
        this.chaveTurma = chaveTurma;
    }

    public String getCodigoInternoTurma() {
        if (codigoInternoTurma == null){
            codigoInternoTurma = "";
        }
        return codigoInternoTurma;
    }

    public void setCodigoInternoTurma(String codigoInternoTurma) {
        this.codigoInternoTurma = codigoInternoTurma;
    }

    public String getNomeTurma() {
        if (nomeTurma == null){
            nomeTurma = "";
        }
        return nomeTurma;
    }

    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }

    public Integer getCodigo() {
        if (codigo == null){
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getOrigem() {
        if (origem == null){
            origem = "";
        }
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getNomeLote() {
        return nomeLote;
    }

    public void setNomeLote(String nomeLote) {
        this.nomeLote = nomeLote;
    }

    public Integer getCodigoLote() {
        return codigoLote;
    }

    public void setCodigoLote(Integer codigoLote) {
        this.codigoLote = codigoLote;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Integer getQtdeRegistros() {
        return qtdeRegistros;
    }

    public void setQtdeRegistros(Integer qtdeRegistros) {
        this.qtdeRegistros = qtdeRegistros;
    }

    public Integer getCodigoDisciplina() {
        if (codigoDisciplina == null){
            codigoDisciplina = 0;
        }
        return codigoDisciplina;
    }

    public void setCodigoDisciplina(Integer codigoDisciplina) {
        this.codigoDisciplina = codigoDisciplina;
    }

    public String getSiglaDisciplina() {
        if (siglaDisciplina == null){
            siglaDisciplina = "";
        }
        return siglaDisciplina;
    }

    public void setSiglaDisciplina(String siglaDisciplina) {
        this.siglaDisciplina = siglaDisciplina;
    }

    public String getNomeDisciplina() {
        if (nomeDisciplina == null){
            nomeDisciplina = "";
        }
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

    public Integer getCodigoInternoAluno() {
        if (codigoInternoAluno == null){
            codigoInternoAluno = 0;
        }
        return codigoInternoAluno;
    }

    public void setCodigoInternoAluno(Integer codigoInternoAluno) {
        this.codigoInternoAluno = codigoInternoAluno;
    }

    public String getCodigoAluno() {
        if (codigoAluno == null){
            codigoAluno = "";
        }
        return codigoAluno;
    }

    public void setCodigoAluno(String codigoAluno) {
        this.codigoAluno = codigoAluno;
    }

    public String getNomeAluno() {
        if (nomeAluno == null){
            nomeAluno = "";
        }
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public String getEmailAluno() {
        return emailAluno;
    }

    public void setEmailAluno(String emailAluno) {
        if (emailAluno == null){
            emailAluno = "";
        }
        this.emailAluno = emailAluno;
    }

    public Integer getCodigoDiaSemanaAluno() {
        if (codigoDiaSemanaAluno == null){
            codigoDiaSemanaAluno = 0;
        }
        return codigoDiaSemanaAluno;
    }

    public void setCodigoDiaSemanaAluno(Integer codigoDiaSemanaAluno) {
        this.codigoDiaSemanaAluno = codigoDiaSemanaAluno;
    }

    public Integer getCodigoInternoPolo() {
        if (codigoInternoPolo == null){
            codigoInternoPolo = 0;
        }
        return codigoInternoPolo;
    }

    public void setCodigoInternoPolo(Integer codigoInternoPolo) {
        this.codigoInternoPolo = codigoInternoPolo;
    }

    public String getCodigoPolo() {
        if (codigoPolo == null){
            codigoPolo = "";
        }
        return codigoPolo;
    }

    public void setCodigoPolo(String codigoPolo) {
        this.codigoPolo = codigoPolo;
    }

    public String getDescricaoPolo() {
        if (descricaoPolo == null){
            descricaoPolo = "";
        }
        return descricaoPolo;
    }

    public void setDescricaoPolo(String descricaoPolo) {
        this.descricaoPolo = descricaoPolo;
    }

    public Integer getCodigoInternoCurso() {
        if (codigoInternoCurso == null){
            codigoInternoCurso = 0;
        }
        return codigoInternoCurso;
    }

    public void setCodigoInternoCurso(Integer codigoInternoCurso) {
        this.codigoInternoCurso = codigoInternoCurso;
    }

    public String getCodigoCurso() {
        if (codigoCurso == null){
            codigoCurso = "";
        }
        return codigoCurso;
    }

    public void setCodigoCurso(String codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

    public String getDescricaoCurso() {
        if (descricaoCurso == null){
            descricaoCurso = "";
        }
        return descricaoCurso;
    }

    public void setDescricaoCurso(String descricaoCurso) {
        this.descricaoCurso = descricaoCurso;
    }

    public Integer getIdItem() {
        return idItem;
    }

    public void setIdItem(Integer idItem) {
        this.idItem = idItem;
    }

    public Integer getCodigoIntegracao() {
        if (codigoIntegracao == null){
            codigoIntegracao = 0;
        }
        return codigoIntegracao;
    }

    public void setCodigoIntegracao(Integer codigoIntegracao) {
        this.codigoIntegracao = codigoIntegracao;
    }

    public String getTempoEstendidoAluno() {
        if (tempoEstendidoAluno == null){
            tempoEstendidoAluno = "";
        }
        return tempoEstendidoAluno;
    }

    public void setTempoEstendidoAluno(String tempoEstendidoAluno) {
        this.tempoEstendidoAluno = tempoEstendidoAluno;
    }

    public String getMatricula() {
        if (matricula == null){
            matricula = "";
        }
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCodigosDisciplinas() {
        if (codigosDisciplinas == null){
            codigosDisciplinas = "";
        }
        return codigosDisciplinas;
    }

    public void setCodigosDisciplinas(String codigosDisciplinas) {
        this.codigosDisciplinas = codigosDisciplinas;
    }

    public String getAbreviaturaDisciplinas() {
        if (abreviaturaDisciplinas == null){
            abreviaturaDisciplinas = "";
        }
        return abreviaturaDisciplinas;
    }

    public void setAbreviaturaDisciplinas(String abreviaturaDisciplinas) {
        this.abreviaturaDisciplinas = abreviaturaDisciplinas;
    }

    public String getNumeroCelularAluno() {
        if (numeroCelularAluno == null){
            numeroCelularAluno = "";
        }
        return numeroCelularAluno;
    }

    public void setNumeroCelularAluno(String numeroCelularAluno) {
        this.numeroCelularAluno = numeroCelularAluno;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

	public String getMensagemErro() {
		if(mensagemErro == null) {
			mensagemErro =  "";
		}
		return mensagemErro;
	}

	public void setMensagemErro(String mensagemErro) {
		this.mensagemErro = mensagemErro;
	}

	public Date getCreatedFinal() {
		return createdFinal;
	}

	public void setCreatedFinal(Date createdFinal) {
		this.createdFinal = createdFinal;
	}
    
    
}
