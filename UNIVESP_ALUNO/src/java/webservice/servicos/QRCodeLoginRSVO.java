package webservice.servicos;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * @author Victor Hugo de Paula Costa - 26 de set de 2016
 *
 */
@XmlRootElement(name = "qrCodeLogin")
public class QRCodeLoginRSVO {
	/**
	 * @author Victor Hugo de Paula Costa - 26 de set de 2016
	 */
	private Integer codigoUnidadeEnsino;
	private String nomeUnidadeEnsino;
	private String matricula;
	private String nomeAluno;
	private String urlFotoPerfilAluno;
	private String urlBaseWS;
	private String nomeCurso;
	private Integer codigoCurso;
	private String tipoRecursoEducacional;
	private String login;
	private String senha;

	public Integer getCodigoUnidadeEnsino() {
		if (codigoUnidadeEnsino == null) {
			codigoUnidadeEnsino = 0;
		}
		return codigoUnidadeEnsino;
	}

	public void setCodigoUnidadeEnsino(Integer codigoUnidadeEnsino) {
		this.codigoUnidadeEnsino = codigoUnidadeEnsino;
	}

	public String getNomeUnidadeEnsino() {
		if (nomeUnidadeEnsino == null) {
			nomeUnidadeEnsino = "";
		}
		return nomeUnidadeEnsino;
	}

	public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
		this.nomeUnidadeEnsino = nomeUnidadeEnsino;
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

	public String getNomeAluno() {
		if (nomeAluno == null) {
			nomeAluno = "";
		}
		return nomeAluno;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}

	public String getUrlFotoPerfilAluno() {
		if (urlFotoPerfilAluno == null) {
			urlFotoPerfilAluno = "";
		}
		return urlFotoPerfilAluno;
	}

	public void setUrlFotoPerfilAluno(String urlFotoPerfilAluno) {
		this.urlFotoPerfilAluno = urlFotoPerfilAluno;
	}

	public String getUrlBaseWS() {
		if (urlBaseWS == null) {
			urlBaseWS = "";
		}
		return urlBaseWS;
	}

	public void setUrlBaseWS(String urlBaseWS) {
		this.urlBaseWS = urlBaseWS;
	}

	public String getNomeCurso() {
		if (nomeCurso == null) {
			nomeCurso = "";
		}
		return nomeCurso;
	}

	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}

	public Integer getCodigoCurso() {
		if (codigoCurso == null) {
			codigoCurso = 0;
		}
		return codigoCurso;
	}

	public void setCodigoCurso(Integer codigoCurso) {
		this.codigoCurso = codigoCurso;
	}

	public String getTipoRecursoEducacional() {
		if (tipoRecursoEducacional == null) {
			tipoRecursoEducacional = "";
		}
		return tipoRecursoEducacional;
	}

	public void setTipoRecursoEducacional(String tipoRecursoEducacional) {
		this.tipoRecursoEducacional = tipoRecursoEducacional;
	}

	public String getLogin() {
		if (login == null) {
			login = "";
		}
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		if (senha == null) {
			senha = "";
		}
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
}
