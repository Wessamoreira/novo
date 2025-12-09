package negocio.comuns.basico;
//
//import java.util.ArrayList;
//import java.util.List;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
//import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;

public class LinksUteisVO extends SuperVO {

	private Integer codigo;
	private String descricao;
	private String link;
	private String icone;
	private Boolean aluno;
	private Boolean professor;
	private Boolean coordenador;
	private Boolean administrativo;
	private List<UsuarioLinksUteisVO> usuarioLinksUteisVOs;
	private List<String> listaMensagemErroProcessamento; 

	public static final long serialVersionUID = 1L;

	public LinksUteisVO() {
		super();
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

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getLink() {
		if (link == null) {
			link = "";
		}
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getIcone() {
		if (icone == null) {
			icone = "";
		}
		return icone;
	}

	public void setIcone(String icone) {
		this.icone = icone;
	}

	public Boolean getAluno() {
		if (aluno == null) {
			aluno = false;
		}
		return aluno;
	}

	public void setAluno(Boolean aluno) {
		this.aluno = aluno;
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

	public Boolean getCoordenador() {
		if (coordenador == null) {
			coordenador = false;
		}
		return coordenador;
	}

	public void setCoordenador(Boolean coordenador) {
		this.coordenador = coordenador;
	}

	public Boolean getAdministrativo() {
		if (administrativo == null) {
			administrativo = false;
		}
		return administrativo;
	}

	public void setAdministrativo(Boolean administrativo) {
		this.administrativo = administrativo;
	}
	
	public List<UsuarioLinksUteisVO> getUsuarioLinksUteisVOs() {
		if (usuarioLinksUteisVOs == null) {
			usuarioLinksUteisVOs = new ArrayList<>();
		}
		return usuarioLinksUteisVOs;
	}

	public void setUsuarioLinksUteisVOs(List<UsuarioLinksUteisVO> usuarioLinksUteisVOs) {
		this.usuarioLinksUteisVOs = usuarioLinksUteisVOs; 
	}

	public List<String> getListaMensagemErroProcessamento() {
		if(listaMensagemErroProcessamento ==null) {
			listaMensagemErroProcessamento = new ArrayList<String>(0);
		}
		return listaMensagemErroProcessamento;
	}

	public void setListaMensagemErroProcessamento(List<String> listaMensagemErroProcessamento) {
		this.listaMensagemErroProcessamento = listaMensagemErroProcessamento;
	}
	
	

}