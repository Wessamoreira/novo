package relatorio.negocio.comuns.processosel;

import java.util.Date;

public class AlunosMatriculadosPorProcessoSeletivoRelVO {

    private Integer codigoInscricao;
    private String nomePessoa;
    private String nomeUnidadeEnsino;
    private String nomeCurso;
    private String nomeTurno;
    private String telefone;
    private String email;
    private Date dataProva;
    private Date dataMatricula;
    private String descricaoProcSeletivo;
    private String tipoRelatorio;
    private String tipoRelatorioExtenso;
    private String matricula;
    private Boolean matriculado;

    public Integer getCodigoInscricao() {
        return codigoInscricao;
    }

    public void setCodigoInscricao(Integer codigoInscricao) {
        this.codigoInscricao = codigoInscricao;
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    public String getNomeUnidadeEnsino() {
        return nomeUnidadeEnsino;
    }

    public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
        this.nomeUnidadeEnsino = nomeUnidadeEnsino;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public String getNomeTurno() {
        return nomeTurno;
    }

    public void setNomeTurno(String nomeTurno) {
        this.nomeTurno = nomeTurno;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDataProva() {
        return dataProva;
    }

    public void setDataProva(Date dataProva) {
        this.dataProva = dataProva;
    }

    public Date getDataMatricula() {
        return dataMatricula;
    }

    public void setDataMatricula(Date dataMatricula) {
        this.dataMatricula = dataMatricula;
    }

    public String getDescricaoProcSeletivo() {
        return descricaoProcSeletivo;
    }

    public void setDescricaoProcSeletivo(String descricaoProcSeletivo) {
        this.descricaoProcSeletivo = descricaoProcSeletivo;
    }

    public void setTipoRelatorio(String tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

    public String getTipoRelatorio() {
        return tipoRelatorio;
    }

    public void setTipoRelatorioExtenso(String tipoRelatorioExtenso) {
        this.tipoRelatorioExtenso = tipoRelatorioExtenso;
    }

    public String getTipoRelatorioExtenso() {
        return tipoRelatorioExtenso;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Boolean getMatriculado() {
        return matriculado;
    }

    public void setMatriculado(Boolean matriculado) {
        this.matriculado = matriculado;
    }

}