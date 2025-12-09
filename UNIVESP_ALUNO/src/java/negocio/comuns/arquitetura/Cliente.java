/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.arquitetura;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cliente")
public class Cliente implements Serializable {

	private static final long serialVersionUID = -3120040413073503418L;
	private Integer codigo;
	private String nome;
	private Boolean autorizado;
	private Boolean autorizadoAplicativo;
	private Boolean autorizadoAplicativoExclusivo;
	private Boolean permitirAcessoModuloAdministrativo;
	private Boolean permitirAcessoModuloAcademico;
	private Boolean permitirAcessoModuloFinanceiro;
	private Boolean permitirAcessoModuloCompras;
	private Boolean permitirAcessoModuloSeiDecidir;
	private Boolean permitirAcessoModuloProcessoSeletivo;
	private Boolean permitirAcessoModuloEad;
	private Boolean permitirAcessoModuloBiblioteca;
	private Boolean permitirAcessoModuloCrm;
	private Boolean permitirAcessoModuloAvaliacaoInstitucional;
	private Boolean permitirAcessoModuloBancoDeCurriculo;
	private Boolean permitirAcessoModuloPlanoOrcamentario;
	private Boolean permitirAcessoModuloPatrimonio;
	private Boolean permitirAcessoModuloEstagio;
	private Boolean permitirAcessoModuloMonografia;
	private Boolean permitirAcessoModuloNotaFiscal;
	private Boolean permitirAcessoModuloRH;
	private Boolean permitirAcessoModuloContabil;
	private Boolean permitirDiplomaDigital;
	private Boolean autorizadoAplicativoProfessor;
	private Boolean autorizadoAplicativoProfessorExclusivo;	

	public Cliente() {
	}

	public Cliente(Integer codigo) {
		this.codigo = codigo;
	}

	public Cliente(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Boolean getAutorizado() {
		return autorizado;
	}

	public void setAutorizado(Boolean autorizado) {
		this.autorizado = autorizado;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Boolean getAutorizadoAplicativo() {
		if (autorizadoAplicativo == null) {
			autorizadoAplicativo = false;
		}
		return autorizadoAplicativo;
	}

	public void setAutorizadoAplicativo(Boolean autorizadoAplicativo) {
		this.autorizadoAplicativo = autorizadoAplicativo;
	}

	public Boolean getPermitirAcessoModuloAdministrativo() {
		if (permitirAcessoModuloAdministrativo == null) {
			permitirAcessoModuloAdministrativo = Boolean.FALSE;
		}
		return permitirAcessoModuloAdministrativo;
	}

	public void setPermitirAcessoModuloAdministrativo(Boolean permitirAcessoModuloAdministrativo) {
		this.permitirAcessoModuloAdministrativo = permitirAcessoModuloAdministrativo;
	}

	public Boolean getPermitirAcessoModuloAcademico() {
		if (permitirAcessoModuloAcademico == null) {
			permitirAcessoModuloAcademico = Boolean.FALSE;
		}
		return permitirAcessoModuloAcademico;
	}

	public void setPermitirAcessoModuloAcademico(Boolean permitirAcessoModuloAcademico) {
		this.permitirAcessoModuloAcademico = permitirAcessoModuloAcademico;
	}

	public Boolean getPermitirAcessoModuloFinanceiro() {
		if (permitirAcessoModuloFinanceiro == null) {
			permitirAcessoModuloFinanceiro = Boolean.FALSE;
		}
		return permitirAcessoModuloFinanceiro;
	}

	public void setPermitirAcessoModuloFinanceiro(Boolean permitirAcessoModuloFinanceiro) {
		this.permitirAcessoModuloFinanceiro = permitirAcessoModuloFinanceiro;
	}

	public Boolean getPermitirAcessoModuloCompras() {
		if (permitirAcessoModuloCompras == null) {
			permitirAcessoModuloCompras = Boolean.FALSE;
		}
		return permitirAcessoModuloCompras;
	}

	public void setPermitirAcessoModuloCompras(Boolean permitirAcessoModuloCompras) {
		this.permitirAcessoModuloCompras = permitirAcessoModuloCompras;
	}

	public Boolean getPermitirAcessoModuloSeiDecidir() {
		if (permitirAcessoModuloSeiDecidir == null) {
			permitirAcessoModuloSeiDecidir = Boolean.FALSE;
		}
		return permitirAcessoModuloSeiDecidir;
	}

	public void setPermitirAcessoModuloSeiDecidir(Boolean permitirAcessoModuloSeiDecidir) {
		this.permitirAcessoModuloSeiDecidir = permitirAcessoModuloSeiDecidir;
	}

	public Boolean getPermitirAcessoModuloProcessoSeletivo() {
		if (permitirAcessoModuloProcessoSeletivo == null) {
			permitirAcessoModuloProcessoSeletivo = Boolean.FALSE;
		}
		return permitirAcessoModuloProcessoSeletivo;
	}

	public void setPermitirAcessoModuloProcessoSeletivo(Boolean permitirAcessoModuloProcessoSeletivo) {
		this.permitirAcessoModuloProcessoSeletivo = permitirAcessoModuloProcessoSeletivo;
	}

	public Boolean getPermitirAcessoModuloEad() {
		if (permitirAcessoModuloEad == null) {
			permitirAcessoModuloEad = Boolean.FALSE;
		}
		return permitirAcessoModuloEad;
	}

	public void setPermitirAcessoModuloEad(Boolean permitirAcessoModuloEad) {
		this.permitirAcessoModuloEad = permitirAcessoModuloEad;
	}

	public Boolean getPermitirAcessoModuloBiblioteca() {
		if (permitirAcessoModuloBiblioteca == null) {
			permitirAcessoModuloBiblioteca = Boolean.FALSE;
		}
		return permitirAcessoModuloBiblioteca;
	}

	public void setPermitirAcessoModuloBiblioteca(Boolean permitirAcessoModuloBiblioteca) {
		this.permitirAcessoModuloBiblioteca = permitirAcessoModuloBiblioteca;
	}

	public Boolean getPermitirAcessoModuloCrm() {
		if (permitirAcessoModuloCrm == null) {
			permitirAcessoModuloCrm = Boolean.FALSE;
		}
		return permitirAcessoModuloCrm;
	}

	public void setPermitirAcessoModuloCrm(Boolean permitirAcessoModuloCrm) {
		this.permitirAcessoModuloCrm = permitirAcessoModuloCrm;
	}

	public Boolean getPermitirAcessoModuloAvaliacaoInstitucional() {
		if (permitirAcessoModuloAvaliacaoInstitucional == null) {
			permitirAcessoModuloAvaliacaoInstitucional = Boolean.FALSE;

		}
		return permitirAcessoModuloAvaliacaoInstitucional;
	}

	public void setPermitirAcessoModuloAvaliacaoInstitucional(Boolean permitirAcessoModuloAvaliacaoInstitucional) {
		this.permitirAcessoModuloAvaliacaoInstitucional = permitirAcessoModuloAvaliacaoInstitucional;
	}

	public Boolean getPermitirAcessoModuloBancoDeCurriculo() {
		if (permitirAcessoModuloBancoDeCurriculo == null) {
			permitirAcessoModuloBancoDeCurriculo = Boolean.FALSE;

		}
		return permitirAcessoModuloBancoDeCurriculo;
	}

	public void setPermitirAcessoModuloBancoDeCurriculo(Boolean permitirAcessoModuloBancoDeCurriculo) {
		this.permitirAcessoModuloBancoDeCurriculo = permitirAcessoModuloBancoDeCurriculo;
	}

	public Boolean getPermitirAcessoModuloPlanoOrcamentario() {
		if (permitirAcessoModuloPlanoOrcamentario == null) {
			permitirAcessoModuloPlanoOrcamentario = Boolean.FALSE;
		}
		return permitirAcessoModuloPlanoOrcamentario;
	}

	public void setPermitirAcessoModuloPlanoOrcamentario(Boolean permitirAcessoModuloPlanoOrcamentario) {
		this.permitirAcessoModuloPlanoOrcamentario = permitirAcessoModuloPlanoOrcamentario;
	}

	public Boolean getPermitirAcessoModuloPatrimonio() {
		if (permitirAcessoModuloPatrimonio == null) {
			permitirAcessoModuloPatrimonio = Boolean.FALSE;
		}
		return permitirAcessoModuloPatrimonio;
	}

	public void setPermitirAcessoModuloPatrimonio(Boolean permitirAcessoModuloPatrimonio) {
		this.permitirAcessoModuloPatrimonio = permitirAcessoModuloPatrimonio;
	}

	public Boolean getPermitirAcessoModuloEstagio() {
		if (permitirAcessoModuloEstagio == null) {
			permitirAcessoModuloEstagio = Boolean.FALSE;
		}
		return permitirAcessoModuloEstagio;
	}

	public void setPermitirAcessoModuloEstagio(Boolean permitirAcessoModuloEstagio) {
		this.permitirAcessoModuloEstagio = permitirAcessoModuloEstagio;
	}

	public Boolean getPermitirAcessoModuloMonografia() {
		if (permitirAcessoModuloMonografia == null) {
			permitirAcessoModuloMonografia = Boolean.FALSE;
		}
		return permitirAcessoModuloMonografia;
	}

	public void setPermitirAcessoModuloMonografia(Boolean permitirAcessoModuloMonografia) {
		this.permitirAcessoModuloMonografia = permitirAcessoModuloMonografia;
	}

	public Boolean getPermitirAcessoModuloNotaFiscal() {
		if (permitirAcessoModuloNotaFiscal == null) {
			permitirAcessoModuloNotaFiscal = Boolean.FALSE;
		}
		return permitirAcessoModuloNotaFiscal;
	}

	public void setPermitirAcessoModuloNotaFiscal(Boolean permitirAcessoModuloNotaFiscal) {
		this.permitirAcessoModuloNotaFiscal = permitirAcessoModuloNotaFiscal;
	}

	public Boolean getAutorizadoAplicativoExclusivo() {
		if(autorizadoAplicativoExclusivo == null){
			autorizadoAplicativoExclusivo = false;
		}
		return autorizadoAplicativoExclusivo;
	}

	public void setAutorizadoAplicativoExclusivo(Boolean autorizadoAplicativoExclusivo) {
		this.autorizadoAplicativoExclusivo = autorizadoAplicativoExclusivo;
	}
	
	public Boolean getPermitirAcessoModuloRH() {
		if(permitirAcessoModuloRH == null) {
			permitirAcessoModuloRH = false;
		}
		return permitirAcessoModuloRH;
	}

	public void setPermitirAcessoModuloRH(Boolean permitirAcessoModuloRH) {
		this.permitirAcessoModuloRH = permitirAcessoModuloRH;
	}

	public Boolean getAutorizadoAplicativoProfessor() {
		if(autorizadoAplicativoProfessor == null) {
			autorizadoAplicativoProfessor = false;
		}
		return autorizadoAplicativoProfessor;
	}

	public void setAutorizadoAplicativoProfessor(Boolean autorizadoAplicativoProfessor) {
		this.autorizadoAplicativoProfessor = autorizadoAplicativoProfessor;
	}

	public Boolean getAutorizadoAplicativoProfessorExclusivo() {
		if(autorizadoAplicativoProfessorExclusivo == null) {
			autorizadoAplicativoProfessorExclusivo = false;
		}
		return autorizadoAplicativoProfessorExclusivo;
	}

	public void setAutorizadoAplicativoProfessorExclusivo(Boolean autorizadoAplicativoProfessorExclusivo) {
		this.autorizadoAplicativoProfessorExclusivo = autorizadoAplicativoProfessorExclusivo;
	}

	public Boolean getPermitirAcessoModuloContabil() {
		if(permitirAcessoModuloContabil == null) {
			permitirAcessoModuloContabil =  false;
		}
		return permitirAcessoModuloContabil;
	}

	public void setPermitirAcessoModuloContabil(Boolean permitirAcessoModuloContabil) {
		this.permitirAcessoModuloContabil = permitirAcessoModuloContabil;
	}

	public Boolean getPermitirDiplomaDigital() {
		if(permitirDiplomaDigital == null) {
			permitirDiplomaDigital =  false;
		}
		return permitirDiplomaDigital;
	}

	public void setPermitirDiplomaDigital(Boolean permitirDiplomaDigital) {
		this.permitirDiplomaDigital = permitirDiplomaDigital;
	}
	
	
}
