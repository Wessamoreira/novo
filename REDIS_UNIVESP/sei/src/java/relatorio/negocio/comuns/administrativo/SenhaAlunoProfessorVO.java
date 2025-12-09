package relatorio.negocio.comuns.administrativo;

import java.io.Serializable;

import negocio.comuns.administrativo.UnidadeEnsinoVO;

/**
 *
 * @author Alberto
 */
public class SenhaAlunoProfessorVO implements Serializable{

	private static final long serialVersionUID = 1L;
	private String nome;
    private String data;
    private String cpf;
    private String matricula;
//    private String unidadeEnsino;
    private UnidadeEnsinoVO unidadeEnsino;
    private String caminhoImagem;
    private String username;

    public SenhaAlunoProfessorVO() {
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * @return the username
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * @param username the username to set
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

//    /**
//     * @return the unidadeEnsino
//     */
//    public String getUnidadeEnsino() {
//        return unidadeEnsino;
//    }
//
//    /**
//     * @param unidadeEnsino the unidadeEnsino to set
//     */
//    public void setUnidadeEnsino(String unidadeEnsino) {
//        this.unidadeEnsino = unidadeEnsino;
//    }
    /**
     * @return the caminhoImagem
     */
    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    /**
     * @param caminhoImagem the caminhoImagem to set
     */
    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
