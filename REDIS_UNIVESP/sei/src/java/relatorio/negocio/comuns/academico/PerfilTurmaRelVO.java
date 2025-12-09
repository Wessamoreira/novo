package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import negocio.comuns.administrativo.FormacaoAcademicaVO;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class PerfilTurmaRelVO {

    private Integer codigoAluno;
    private String aluno;
    private String matricula;
    private String email;
    private String email2;
    private String cursoSuperior;
    private String instituicao;
    private String anoConclusao;
    private String localTrabalho;
    private String cidade;
    private String curso;
    private String turma;
    private String coordenadorCurso;
    private String unidadeEnsino;
    private Date dataNasc;
    private String sexo;
    private Integer idade;
    private String fotoAluno;
    private String pastaBaseArquivo;
    private String pastaBaseArquivoWeb;
    private String nomeArquivo;
    private Integer codigoArquivo;
    private List<FormacaoAcademicaVO> formacaoAcademicaRelVOs;

    public JRDataSource getFormacaoAcademica() {
        return new JRBeanArrayDataSource(getFormacaoAcademicaRelVOs().toArray());
    }

    public PerfilTurmaRelVO() {
    }

    /**
     * @return the aluno
     */
    public String getAluno() {
        if (aluno == null) {
            aluno = "";
        }
        return aluno;
    }

    /**
     * @param aluno the aluno to set
     */
    public void setAluno(String aluno) {
        this.aluno = aluno;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        if (email == null) {
            email = "";
        }
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the email2
     */
    public String getEmail2() {
        if (email2 == null) {
            email2 = "";
        }
        return email2;
    }

    /**
     * @param email2 the email2 to set
     */
    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    /**
     * @return the cursoSuperior
     */
    public String getCursoSuperior() {
        if (cursoSuperior == null) {
            cursoSuperior = "";
        }
        return cursoSuperior;
    }

    /**
     * @param cursoSuperior the cursoSuperior to set
     */
    public void setCursoSuperior(String cursoSuperior) {
        this.cursoSuperior = cursoSuperior;
    }

    /**
     * @return the instituicao
     */
    public String getInstituicao() {
        if (instituicao == null) {
            instituicao = "";
        }
        return instituicao;
    }

    /**
     * @param instituicao the instituicao to set
     */
    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    /**
     * @return the anoConclusao
     */
    public String getAnoConclusao() {
        if (anoConclusao == null) {
            anoConclusao = "";
        }
        return anoConclusao;
    }

    /**
     * @param anoConclusao the anoConclusao to set
     */
    public void setAnoConclusao(String anoConclusao) {
        this.anoConclusao = anoConclusao;
    }

    /**
     * @return the localTrabalho
     */
    public String getLocalTrabalho() {
        if (localTrabalho == null) {
            localTrabalho = "";
        }
        return localTrabalho;
    }

    /**
     * @param localTrabalho the localTrabalho to set
     */
    public void setLocalTrabalho(String localTrabalho) {
        this.localTrabalho = localTrabalho;
    }

    /**
     * @return the curso
     */
    public String getCurso() {
        if (curso == null) {
            curso = "";
        }
        return curso;
    }

    /**
     * @param curso the curso to set
     */
    public void setCurso(String curso) {
        this.curso = curso;
    }

    /**
     * @return the turma
     */
    public String getTurma() {
        if (turma == null) {
            turma = "";
        }
        return turma;
    }

    /**
     * @param turma the turma to set
     */
    public void setTurma(String turma) {
        this.turma = turma;
    }

    /**
     * @return the coordenadorCurso
     */
    public String getCoordenadorCurso() {
        if (coordenadorCurso == null) {
            coordenadorCurso = "";
        }
        return coordenadorCurso;
    }

    /**
     * @param coordenadorCurso the coordenadorCurso to set
     */
    public void setCoordenadorCurso(String coordenadorCurso) {
        this.coordenadorCurso = coordenadorCurso;
    }

    /**
     * @return the unidadeEnsino
     */
    public String getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = "";
        }
        return unidadeEnsino;
    }

    /**
     * @param unidadeEnsino the unidadeEnsino to set
     */
    public void setUnidadeEnsino(String unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public void setFormacaoAcademicaRelVOs(List<FormacaoAcademicaVO> formacaoAcademicaRelVOs) {
        this.formacaoAcademicaRelVOs = formacaoAcademicaRelVOs;
    }

    public List<FormacaoAcademicaVO> getFormacaoAcademicaRelVOs() {
        if (formacaoAcademicaRelVOs == null) {
            formacaoAcademicaRelVOs = new ArrayList<FormacaoAcademicaVO>(0);
        }
        return formacaoAcademicaRelVOs;
    }

    /**
     * @return the codigoAluno
     */
    public Integer getCodigoAluno() {
        if (codigoAluno == null) {
            codigoAluno = 0;
        }
        return codigoAluno;
    }

    /**
     * @param codigoAluno the codigoAluno to set
     */
    public void setCodigoAluno(Integer codigoAluno) {
        this.codigoAluno = codigoAluno;
    }

    /**
     * @return the matricula
     */
    public String getMatricula() {
        if (matricula == null) {
            matricula = "";
        }
        return matricula;
    }

    /**
     * @param matricula the matricula to set
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    /**
     * @return the mediaIdade
     */
    public Integer getIdade() {
        if (idade == null) {
            idade = 0;
        }
        return idade;
    }

    /**
     * @param mediaIdade the mediaIdade to set
     */
    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    /**
     * @return the dataNasc
     */
    public Date getDataNasc() {
        if (dataNasc == null) {
            dataNasc = new Date();
        }
        return dataNasc;
    }

    /**
     * @param dataNasc the dataNasc to set
     */
    public void setDataNasc(Date dataNasc) {
        this.dataNasc = dataNasc;
    }

    /**
     * @return the sexo
     */
    public String getSexo() {
        if (sexo == null) {
            sexo = "";
        }
        return sexo;
    }

    /**
     * @param sexo the sexo to set
     */
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    /**
     * @return the cidade
     */
    public String getCidade() {
        if (cidade == null) {
            cidade = "";
        }
        return cidade;
    }

    /**
     * @param cidade the cidade to set
     */
    public void setCidade(String cidade) {
        this.cidade = cidade;
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

    public String getPastaBaseArquivo() {
        if (pastaBaseArquivo == null) {
            pastaBaseArquivo = "";
        }
        return pastaBaseArquivo;
    }

    public void setPastaBaseArquivo(String pastaBaseArquivo) {
        this.pastaBaseArquivo = pastaBaseArquivo;
    }

    public String getPastaBaseArquivoWeb() {
        if (pastaBaseArquivoWeb == null) {
            pastaBaseArquivoWeb = "";
        }
        return pastaBaseArquivoWeb;
    }

    public void setPastaBaseArquivoWeb(String pastaBaseArquivoWeb) {
        this.pastaBaseArquivoWeb = pastaBaseArquivoWeb;
    }

    public String getNomeArquivo() {
        if (nomeArquivo == null) {
            nomeArquivo = "";
        }
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public Integer getCodigoArquivo() {
        if (codigoArquivo == null) {
            codigoArquivo = 0;
        }
        return codigoArquivo;
    }

    public void setCodigoArquivo(Integer codigoArquivo) {
        this.codigoArquivo = codigoArquivo;
    }
}
