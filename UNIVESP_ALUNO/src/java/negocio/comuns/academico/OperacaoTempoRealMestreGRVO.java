package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.dominios.OperacaoTempoRealMestreGREnum;

import java.util.Date;

public class OperacaoTempoRealMestreGRVO extends SuperVO {

    private Long codigo;
    private String matricula;
    private OperacaoTempoRealMestreGREnum operacaoTempoRealMestreGREnum;
    private String disciplina;
    private Integer ano;
    private String semestre;
    private int bimestre;
    private String dadosEnvio;
    private String mensagemErro;
    private Date created;
    private Boolean processado;
    private String dadosRetorno;
    private String situacao;

    public Long getCodigo() {
        if (codigo == null) {
            codigo = 0L;
        }
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getMatricula() {
        if (matricula == null) {
            matricula = Constantes.EMPTY;
        }
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public OperacaoTempoRealMestreGREnum getOperacaoTempoRealMestreGREnum() {
        return operacaoTempoRealMestreGREnum;
    }

    public void setOperacaoTempoRealMestreGREnum(OperacaoTempoRealMestreGREnum operacaoTempoRealMestreGREnum) {
        this.operacaoTempoRealMestreGREnum = operacaoTempoRealMestreGREnum;
    }

    public String getDisciplina() {
        if (disciplina == null) {
            disciplina = Constantes.EMPTY;
        }
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public Integer getAno() {
        if (ano == null) {
            ano = 0;
        }
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getSemestre() {
        if (semestre == null) {
            semestre = Constantes.EMPTY;
        }
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public int getBimestre() {
        return bimestre;
    }

    public void setBimestre(int bimestre) {
        this.bimestre = bimestre;
    }

    public String getDadosEnvio() {
        if (dadosEnvio == null) {
            dadosEnvio = Constantes.EMPTY;
        }
        return dadosEnvio;
    }

    public void setDadosEnvio(String dadosEnvio) {
        this.dadosEnvio = dadosEnvio;
    }

    public String getMensagemErro() {
        if (mensagemErro == null) {
            mensagemErro = Constantes.EMPTY;
        }
        return mensagemErro;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

    public Date getCreated() {
        if (created == null) {
            created = new Date();
        }
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Boolean getProcessado() {
        if (processado == null) {
            processado = false;
        }
        return processado;
    }

    public void setProcessado(Boolean processado) {
        this.processado = processado;
    }

    public String getDadosRetorno() {
        if (dadosRetorno == null) {
            dadosRetorno = Constantes.EMPTY;
        }
        return dadosRetorno;
    }

    public void setDadosRetorno(String dadosRetorno) {
        this.dadosRetorno = dadosRetorno;
    }


    public String getSituacao() {
        if (situacao == null) {
            situacao = Constantes.EMPTY;
        }
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}
