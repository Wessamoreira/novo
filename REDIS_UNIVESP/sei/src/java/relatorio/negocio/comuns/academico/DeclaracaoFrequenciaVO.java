/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.comuns.academico;

import negocio.comuns.administrativo.FuncionarioVO;



/**
 * 
 * @author Otimize-TI
 */
public class DeclaracaoFrequenciaVO {

    private String nome;
    private String rg;
    private String cpf;
    private String periodoLetivo;
    private String curso;
    private String data;
    private String matricula;
    private String informacoesAdicionais;
    private String disciplina;
    private String nomeUnidadeEnsino;
    private String cidadeUnidadeEnsino;
    private String frequencia;
    private Integer tipoDeclaracao;
    private FuncionarioVO funcionarioPrincipalVO;
    



	public DeclaracaoFrequenciaVO() {
		setNome("");
		setRg("");
		setCpf("");
		setData("");
		setMatricula("");
	}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPeriodoLetivo() {
        if (periodoLetivo == null){
            periodoLetivo = "____________________________";
        }
        return periodoLetivo;
    }

    public void setPeriodoLetivo(String periodoLetivo) {
        this.periodoLetivo = periodoLetivo;
    }

    public String getCurso() {
        if (curso == null){
            curso = "_____________________________";
        }
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getInformacoesAdicionais() {
        return informacoesAdicionais;
    }

    public void setInformacoesAdicionais(String informacoesAdicionais) {
        this.informacoesAdicionais = informacoesAdicionais;
    }

    public String getDisciplina() {
        if (disciplina == null){
            disciplina = "___________________________________________";
        }
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getNomeUnidadeEnsino() {
        if (nomeUnidadeEnsino == null){
            nomeUnidadeEnsino = "____________________________________";
        }
        return nomeUnidadeEnsino;
    }

    public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
        this.nomeUnidadeEnsino = nomeUnidadeEnsino;
    }

    public String getCidadeUnidadeEnsino() {
        if (cidadeUnidadeEnsino == null){
            cidadeUnidadeEnsino = "_______________________";
        }

        return cidadeUnidadeEnsino;
    }

    public void setCidadeUnidadeEnsino(String cidadeUnidadeEnsino) {
        this.cidadeUnidadeEnsino = cidadeUnidadeEnsino;
    }

    public String getFrequencia() {
        if (frequencia == null){
            frequencia = "___";
        }
        return frequencia;
    }

    public void setFrequencia(String frequencia) {
        this.frequencia = frequencia;
    }

    public Integer getTipoDeclaracao() {
        if (tipoDeclaracao == null){
            tipoDeclaracao = 0;
        }
        return tipoDeclaracao;
    }

    public void setTipoDeclaracao(Integer tipoDeclaracao) {
        this.tipoDeclaracao = tipoDeclaracao;
    }


    public Boolean getApresentarInformacoesAdicionais() {
        if (getTipoDeclaracao().intValue() == 1){
            return true;
        }
        return false;
    }


    public FuncionarioVO getFuncionarioPrincipalVO() {
        if (funcionarioPrincipalVO == null){
            funcionarioPrincipalVO = new FuncionarioVO();
        }
        return funcionarioPrincipalVO;
    }

    public void setFuncionarioPrincipalVO(FuncionarioVO funcionarioPrincipalVO) {
        this.funcionarioPrincipalVO = funcionarioPrincipalVO;
    }


}
