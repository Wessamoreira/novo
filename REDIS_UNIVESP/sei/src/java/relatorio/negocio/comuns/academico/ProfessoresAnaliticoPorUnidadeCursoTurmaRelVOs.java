package relatorio.negocio.comuns.academico;

import java.util.List;
import negocio.comuns.academico.DisciplinasInteresseVO;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 *
 * @author Alberto
 */
public class ProfessoresAnaliticoPorUnidadeCursoTurmaRelVOs extends SuperVO {

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
    private String filtroSituacao;
    private List<FormacaoAcademicaVO> formacaoAcademicaVOs;
    private Boolean imprimirFormacaoAcademicaVO;
    private List<DisciplinasInteresseVO> disciplinasInteresseVOs;
    private List<FiliacaoVO> filiacaoVOs;
    private Boolean imprimirDisciplinasInteresseVOs;
    private String cep;
    private String endereco;
    private String bairro;
    private String complemento;
    private String numero;
    private String cidade;
    private String estado;
    private String nomePai;
    private String nomeMae;
    private String rg;
    private String cpf;
    private String estadoEmissorRg;
    private String orgaoEmissorRg;
    
    public JRDataSource getFormacaoAcademica() {
        return new JRBeanArrayDataSource(getFormacaoAcademicaVOs().toArray());
    }
    
    public JRDataSource getDisciplinasInteresse() {
        return new JRBeanArrayDataSource(getDisciplinasInteresseVOs().toArray());
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
        if (this.formacaoAcademicaVOs.isEmpty()) {
            this.imprimirFormacaoAcademicaVO = false;
        } else {
            this.imprimirFormacaoAcademicaVO = true;
        }
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

    /**
     * @return the disciplinasInteresseVOs
     */
    public List<DisciplinasInteresseVO> getDisciplinasInteresseVOs() {
        return disciplinasInteresseVOs;
    }

    /**
     * @param disciplinasInteresseVOs the disciplinasInteresseVOs to set
     */
    public void setDisciplinasInteresseVOs(List<DisciplinasInteresseVO> disciplinasInteresseVOs) {
        this.disciplinasInteresseVOs = disciplinasInteresseVOs;
        if (this.disciplinasInteresseVOs.isEmpty()) {
            this.imprimirDisciplinasInteresseVOs = false;
        } else {
            this.imprimirDisciplinasInteresseVOs = true;
        }
    }

    /**
     * @return the filtroSituacao
     */
    public String getFiltroSituacao() {
        if (filtroSituacao != null) {
            filtroSituacao = "";
        }
        return filtroSituacao;
    }

    /**
     * @param filtroSituacao the filtroSituacao to set
     */
    public void setFiltroSituacao(String filtroSituacao) {
        this.filtroSituacao = filtroSituacao;
    }

    /**
     * @return the imprimirFormacaoAcademicaVO
     */
    public Boolean getImprimirFormacaoAcademicaVO() {
        if (imprimirFormacaoAcademicaVO == null) {
            imprimirFormacaoAcademicaVO = Boolean.TRUE;
        }
        return imprimirFormacaoAcademicaVO;
    }

    /**
     * @param imprimirFormacaoAcademicaVO the imprimirFormacaoAcademicaVO to set
     */
    public void setImprimirFormacaoAcademicaVO(Boolean imprimirFormacaoAcademicaVO) {
        this.imprimirFormacaoAcademicaVO = imprimirFormacaoAcademicaVO;
    }

    /**
     * @return the imprimirDisciplinasInteresseVOs
     */
    public Boolean getImprimirDisciplinasInteresseVOs() {
        if (imprimirDisciplinasInteresseVOs == null) {
            imprimirDisciplinasInteresseVOs = Boolean.TRUE;
        }
        return imprimirDisciplinasInteresseVOs;
    }

    /**
     * @param imprimirDisciplinasInteresseVOs the imprimirDisciplinasInteresseVOs to set
     */
    public void setImprimirDisciplinasInteresseVOs(Boolean imprimirDisciplinasInteresseVOs) {
        this.imprimirDisciplinasInteresseVOs = imprimirDisciplinasInteresseVOs;
    }

    /**
     * @return the cep
     */
    public String getCep() {
        if (cep == null) {
           cep  = "";
        }        
        return cep;
    }

    /**
     * @param cep the cep to set
     */
    public void setCep(String cep) {
        this.cep = cep;
    }

    /**
     * @return the endereco
     */
    public String getEndereco() {
        if (endereco == null) {
           endereco  = "";
        }          
        return endereco;
    }

    /**
     * @param endereco the endereco to set
     */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    /**
     * @return the bairro
     */
    public String getBairro() {
        if (bairro == null) {
           bairro  = "";
        }         
        return bairro;
    }

    /**
     * @param bairro the bairro to set
     */
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    /**
     * @return the complemento
     */
    public String getComplemento() {
        if (complemento == null) {
           complemento  = "";
        } 
        return complemento;
    }

    /**
     * @param complemento the complemento to set
     */
    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    /**
     * @return the numero
     */
    public String getNumero() {
        if (numero == null) {
           numero  = "";
        }           
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * @return the cidade
     */
    public String getCidade() {
        if (cidade == null) {
           cidade  = "";
        }       
        return cidade;
    }

    /**
     * @param cidade the cidade to set
     */
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        if (estado == null) {
           estado  = "";
        }        
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the nomePai
     */
    public String getNomePai() {
        if (nomePai == null) {
           nomePai  = "";
        }
        return nomePai;
    }

    /**
     * @param nomePai the nomePai to set
     */
    public void setNomePai(String nomePai) {
        this.nomePai = nomePai;
    }

    /**
     * @return the nomeMae
     */
    public String getNomeMae() {
        if (nomeMae == null) {
          nomeMae  = "";
        }
        return nomeMae;
    }

    /**
     * @param nomeMae the nomeMae to set
     */
    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
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

    /**
     * @return the estadoEmissorRg
     */
    public String getEstadoEmissorRg() {
        if (estadoEmissorRg == null) {
            estadoEmissorRg = "";
        }
        return estadoEmissorRg;
    }

    /**
     * @param estadoEmissorRg the estadoEmissorRg to set
     */
    public void setEstadoEmissorRg(String estadoEmissorRg) {
        this.estadoEmissorRg = estadoEmissorRg;
    }

    /**
     * @return the orgaoEmissorRg
     */
    public String getOrgaoEmissorRg() {
        if (orgaoEmissorRg == null) {
            orgaoEmissorRg = "";
        }
        return orgaoEmissorRg;
    }

    /**
     * @param orgaoEmissorRg the orgaoEmissorRg to set
     */
    public void setOrgaoEmissorRg(String orgaoEmissorRg) {
        this.orgaoEmissorRg = orgaoEmissorRg;
    }

    /**
     * @return the filiacaoVOs
     */
    public List<FiliacaoVO> getFiliacaoVOs() {
        return filiacaoVOs;
    }

    /**
     * @param filiacaoVOs the filiacaoVOs to set
     */
    public void setFiliacaoVOs(List<FiliacaoVO> filiacaoVOs) {
        this.filiacaoVOs = filiacaoVOs;
    }

    /**
     * @return the cpf
     */
    public String getCpf() {
        if (cpf == null) {
            cpf = "";
        }
        return cpf;
    }

    /**
     * @param cpf the cpf to set
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
