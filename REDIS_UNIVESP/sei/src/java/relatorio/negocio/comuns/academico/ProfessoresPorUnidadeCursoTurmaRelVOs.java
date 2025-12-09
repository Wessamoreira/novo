package relatorio.negocio.comuns.academico;

import java.util.List;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;

/**
 *
 * @author Alberto
 */
public class ProfessoresPorUnidadeCursoTurmaRelVOs extends SuperVO {

    private Integer codigo;
    private String matricula;
    private String situacao;
    private String nomeProfessor;
    private String dataNascimento;
    private String telefoneRes;
    private String celular;
    private String email;
    private String email2;
    private String titulacao;
    private Boolean funcionario;
    private Boolean professor;
    private String sexo;
    private List<FormacaoAcademicaVO> formacaoAcademicaVOs;

    public String getMatricula() {
        if (matricula == null) {
            matricula = "";
        }
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNomeProfessor() {
        if (nomeProfessor == null) {
            nomeProfessor = "";
        }
        return nomeProfessor;
    }

    public void setNomeProfessor(String nomeProfessor) {
        this.nomeProfessor = nomeProfessor;
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

    public String getTelefoneRes() {
        if (telefoneRes == null) {
            telefoneRes = "";
        }
        return telefoneRes;
    }

    public void setTelefoneRes(String telefoneRes) {
        this.telefoneRes = telefoneRes;
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

    public String getEmail2() {
        if (email2 == null) {
            email2 = "";
        }
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
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

    public Boolean getFuncionario() {
        if (funcionario == null) {
            funcionario = false;
        }
        return funcionario;
    }

    public void setFuncionario(Boolean funcionario) {
        this.funcionario = funcionario;
    }

    public List<FormacaoAcademicaVO> getFormacaoAcademicaVOs() {
        return formacaoAcademicaVOs;
    }

    public void setFormacaoAcademicaVOs(List<FormacaoAcademicaVO> formacaoAcademicaVOs) {
        this.formacaoAcademicaVOs = formacaoAcademicaVOs;
    }

    public String getSexo() {
        if (sexo == null) {
            sexo = "";
        }
        return (sexo);
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
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

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Boolean getProfessor() {
        if (professor == null) {
            professor = false;
        }
        return professor;
    }

    public void setProfessor(Boolean professor) {
        this.professor = professor;
    }

    public String getTitulacao() {
        if (titulacao == null) {
            titulacao = "";
        }
        return titulacao;
    }

    public void setTitulacao(String titulacao) {
        this.titulacao = titulacao;
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo com um domínio específico.
     * Com base no valor de armazenamento do atributo esta função é capaz de retornar o
     * de apresentação correspondente. Útil para campos como sexo, escolaridade, etc.
     */
    public String getSexo_Apresentar() {
        if (sexo.equals("F")) {
            return "Feminino";
        }
        if (sexo.equals("M")) {
            return "Masculino";
        }
        return (sexo);
    }

    public String consultarMaiorNivelEscolaridade() {
        String nivelEscolaridade = "";
        int maiorNivel = 0;
        NivelFormacaoAcademica nivelFormacaoAcademica;
        for (FormacaoAcademicaVO formacaoAcademicaVO : getFormacaoAcademicaVOs()) {
            nivelFormacaoAcademica = NivelFormacaoAcademica.getEnum(formacaoAcademicaVO.getEscolaridade());
            if (nivelFormacaoAcademica != null && nivelFormacaoAcademica.getNivel() > 0) {
                if (maiorNivel < nivelFormacaoAcademica.getNivel()) {
                    maiorNivel = nivelFormacaoAcademica.getNivel();
                    nivelEscolaridade = nivelFormacaoAcademica.getSigla();
                    if ((nivelFormacaoAcademica.getValor().equals("DR") || nivelFormacaoAcademica.getValor().equals("GR")) && getSexo().equals("F")) {
                        nivelEscolaridade += "a";
                    }
                }
            }
        }
        return nivelEscolaridade;
    }
}
