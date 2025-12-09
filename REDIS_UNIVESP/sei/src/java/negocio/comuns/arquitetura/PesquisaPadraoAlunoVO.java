package negocio.comuns.arquitetura;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PesquisaPadraoAlunoVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
    private String foto;
    private Float score;
    private String nome;
    private String matriculas;
    private String registroAcademico;
    private String cpf;
    private String email;
    private List<PesquisaPadraoAlunoResponsavelVO> responsaveis;
    
    /*
     * Campo utilizado para retornar string vazia ao rich:autocomplete ao selecionar uma das sugestões
     */
    private String texto;
    
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public String getMatriculas() {
		if (matriculas == null) {
			matriculas = "";
		}
		return matriculas;
	}
	
	public void setMatriculas(String matriculas) {
		this.matriculas = matriculas;
	}
	
	public String getRegistroAcademico() {
		if (registroAcademico == null) {
			registroAcademico = "";
		}
		return registroAcademico;
	}
	
	public void setRegistroAcademico(String registroAcademico) {
		this.registroAcademico = registroAcademico;
	}
	
	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getCpf() {
		if (cpf == null) {
			cpf = "";
		}
		return cpf;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
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

	public Float getScore() {
		if (score == null) {
			score = 0F;
		}
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public String getFoto() {
		if (foto == null) {
			foto = "";
		}
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public List<PesquisaPadraoAlunoResponsavelVO> getResponsaveis() {
		if (responsaveis == null) {
			responsaveis = new ArrayList<PesquisaPadraoAlunoResponsavelVO>();
		}
		return responsaveis;
	}

	public void setResponsaveis(List<PesquisaPadraoAlunoResponsavelVO> responsaveis) {
		this.responsaveis = responsaveis;
	}

	public String getTexto() {
		if (texto == null) {
			texto = "";
		}
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getHtml() {
		int linhas = 4;
    	if (getResponsaveis().size() > 0) {
    		linhas = 2 + getResponsaveis().size();
    	}
		StringBuilder html = new StringBuilder("<table class=\"ipp\" onclick=\"abrirFichaAluno(")
			.append(getCodigo()).append(")\"><tr><td rowspan=\"").append(linhas).append("\" style=\"width:7%\"><img src=\"")
			.append(getFoto()).append("\" style=\"width:44px;padding:0px;margin:0px\"/></td>\r\n")
			.append("<td style=\"width:54%\"><span class=\"sp\">").append(getNome()).append("</span></td>\r\n")
			.append("<td style=\"width:11%\"><span class=\"ssi\">CPF: </span></td>\r\n")
			.append("<td style=\"width:28%\"><span class=\"ssi\">Email: </span></td></tr>\r\n")
			.append("<tr><td><span class=\"ssi\">Matrícula: </span><span class=\"sc\">").append(getMatriculas())
			.append("</span><span class=\"ssi\">&nbsp;&nbsp;RA: </span><span class=\"sc\">").append(getRegistroAcademico()).append("</span></td>\r\n")
			.append("<td><span class=\"sc\">").append(getCpf()).append("</span></td>\r\n")
			.append("<td><span class=\"sc\">").append(getEmail()).append("</span></td></tr>\r\n");
		if (getResponsaveis().size() > 0) {
			for (PesquisaPadraoAlunoResponsavelVO resp : getResponsaveis()) {
				html.append("<tr class=\"scr\"><td><span class=\"ssi\">").append(resp.getTipo()).append(": </span><span class=\"sc\">")
					.append(resp.getNome()).append("</span></td>\r\n");
				if (resp.getCpf().trim().isEmpty()) {
					html.append("<td></td>");
				} else {
					html.append("<td><span class=\"sc\">").append(resp.getCpf()).append("</span></td>\r\n");
				}
				if (resp.getEmail().trim().isEmpty()) {
	    			html.append("<td></td>");
	    		} else {
					html.append("<td><span class=\"sc\">").append(resp.getEmail()).append("</span></td></tr>\r\n");
	    		}
			}
		} else {
			html.append("<tr><td colspan=\"3\">&nbsp;</td></tr><tr><td colspan=\"3\">&nbsp;</td></tr>");
		}
		html.append("</table>");
		return html.toString();
	}

}