/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.comuns.academico;

import java.util.Date;

/**
 *
 * @author Carlos
 */
public class AlunosAprovadosProcessoSeletivoRelVO {

    private String nome;
    private String telefone;
    private String email;
    private String curso;
    private Date dataNascimento;
    private String cpf;
    private String procSeletivo;
    private String unidadeEnsino;

    /**
     * @return the nome
     */
    public String getNome() {
        if(nome == null){
            nome = "";
        }
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the telefone
     */
    public String getTelefone() {
        if(telefone == null){
            telefone = "";
        }
        return telefone;
    }

    /**
     * @param telefone the telefone to set
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        if(email == null){
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
     * @return the curso
     */
    public String getCurso() {
        if(curso == null){
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
     * @return the dataNascimento
     */
    public Date getDataNascimento() {
        if(dataNascimento == null){
            dataNascimento = new Date();
        }
        return dataNascimento;
    }

    /**
     * @param dataNascimento the dataNascimento to set
     */
    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    /**
     * @return the cpf
     */
    public String getCpf() {
        if(cpf == null){
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

    /**
     * @return the procSeletivo
     */
    public String getProcSeletivo() {
        if(procSeletivo == null){
            procSeletivo = "";
        }
        return procSeletivo;
    }

    /**
     * @param procSeletivo the procSeletivo to set
     */
    public void setProcSeletivo(String procSeletivo) {
        this.procSeletivo = procSeletivo;
    }

    /**
     * @return the unidadeEnsino
     */
    public String getUnidadeEnsino() {
        if(unidadeEnsino == null){
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

}
