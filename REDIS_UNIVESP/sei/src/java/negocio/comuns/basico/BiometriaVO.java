package negocio.comuns.basico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

public class BiometriaVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private PessoaVO pessoaVO;
	private UsuarioVO responsavel;
	private Date data;
	private Integer idDigitalPolegarEsquerdo;
	private Integer idDigitalIndicadorEsquerdo;
	private Integer idDigitalMedioEsquerdo;
	private Integer idDigitalAnularEsquerdo;
	private Integer idDigitalMinimoEsquerdo;
	private Integer idDigitalPolegarDireito;
	private Integer idDigitalIndicadorDireito;
	private Integer idDigitalMedioDireito;
	private Integer idDigitalAnularDireito;
	private Integer idDigitalMinimoDireito;
	private Integer idDigital;
	private Integer idDedo;
	private String estadoBio;
	private String matricula;
	private Boolean ativo;
	private Boolean sincronizar;
	private Date dataSincronismo;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public PessoaVO getPessoaVO() {
		if (pessoaVO == null) {
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}

	public Date getData() {
		if (data == null) {
			data = new Date();
		}
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getData_Apresentar() {
		return Uteis.getDataAno4Digitos(getData());
	}

	public Integer getIdDigitalPolegarEsquerdo() {
		if (idDigitalPolegarEsquerdo == null) {
			idDigitalPolegarEsquerdo = 0;
		}
		return idDigitalPolegarEsquerdo;
	}

	public void setIdDigitalPolegarEsquerdo(Integer idDigitalPolegarEsquerdo) {
		this.idDigitalPolegarEsquerdo = idDigitalPolegarEsquerdo;
	}

	public Integer getIdDigitalIndicadorEsquerdo() {
		if (idDigitalIndicadorEsquerdo == null) {
			idDigitalIndicadorEsquerdo = 0;
		}
		return idDigitalIndicadorEsquerdo;
	}

	public void setIdDigitalIndicadorEsquerdo(Integer idDigitalIndicadorEsquerdo) {
		this.idDigitalIndicadorEsquerdo = idDigitalIndicadorEsquerdo;
	}

	public Integer getIdDigitalMedioEsquerdo() {
		if (idDigitalMedioEsquerdo == null) {
			idDigitalMedioEsquerdo = 0;
		}
		return idDigitalMedioEsquerdo;
	}

	public void setIdDigitalMedioEsquerdo(Integer idDigitalMedioEsquerdo) {
		this.idDigitalMedioEsquerdo = idDigitalMedioEsquerdo;
	}

	public Integer getIdDigitalAnularEsquerdo() {
		if (idDigitalAnularEsquerdo == null) {
			idDigitalAnularEsquerdo = 0;
		}
		return idDigitalAnularEsquerdo;
	}

	public void setIdDigitalAnularEsquerdo(Integer idDigitalAnularEsquerdo) {
		this.idDigitalAnularEsquerdo = idDigitalAnularEsquerdo;
	}

	public Integer getIdDigitalMinimoEsquerdo() {
		if (idDigitalMinimoEsquerdo == null) {
			idDigitalMinimoEsquerdo = 0;
		}
		return idDigitalMinimoEsquerdo;
	}

	public void setIdDigitalMinimoEsquerdo(Integer idDigitalMinimoEsquerdo) {
		this.idDigitalMinimoEsquerdo = idDigitalMinimoEsquerdo;
	}

	public Integer getIdDigitalPolegarDireito() {
		if (idDigitalPolegarDireito == null) {
			idDigitalPolegarDireito = 0;
		}
		return idDigitalPolegarDireito;
	}

	public void setIdDigitalPolegarDireito(Integer idDigitalPolegarDireito) {
		this.idDigitalPolegarDireito = idDigitalPolegarDireito;
	}

	public Integer getIdDigitalIndicadorDireito() {
		if (idDigitalIndicadorDireito == null) {
			idDigitalIndicadorDireito = 0;
		}
		return idDigitalIndicadorDireito;
	}

	public void setIdDigitalIndicadorDireito(Integer idDigitalIndicadorDireito) {
		this.idDigitalIndicadorDireito = idDigitalIndicadorDireito;
	}

	public Integer getIdDigitalMedioDireito() {
		if (idDigitalMedioDireito == null) {
			idDigitalMedioDireito = 0;
		}
		return idDigitalMedioDireito;
	}

	public void setIdDigitalMedioDireito(Integer idDigitalMedioDireito) {
		this.idDigitalMedioDireito = idDigitalMedioDireito;
	}

	public Integer getIdDigitalAnularDireito() {
		if (idDigitalAnularDireito == null) {
			idDigitalAnularDireito = 0;
		}
		return idDigitalAnularDireito;
	}

	public void setIdDigitalAnularDireito(Integer idDigitalAnularDireito) {
		this.idDigitalAnularDireito = idDigitalAnularDireito;
	}

	public Integer getIdDigitalMinimoDireito() {
		if (idDigitalMinimoDireito == null) {
			idDigitalMinimoDireito = 0;
		}
		return idDigitalMinimoDireito;
	}

	public void setIdDigitalMinimoDireito(Integer idDigitalMinimoDireito) {
		this.idDigitalMinimoDireito = idDigitalMinimoDireito;
	}

	public Integer getIdDigital() {
		if (idDigital == null) {
			idDigital = 0;
		}
		return idDigital;
	}

	public void setIdDigital(Integer idDigital) {
		this.idDigital = idDigital;
	}

	public Integer getIdDedo() {
		if (idDedo == null) {
			idDedo = 0;
		}
		return idDedo;
	}

	public void setIdDedo(Integer idDedo) {
		this.idDedo = idDedo;
	}

	public String getEstadoBio() {
		if (estadoBio == null) {
			estadoBio = "";
		}
		return estadoBio;
	}

	public void setEstadoBio(String estadoBio) {
		this.estadoBio = estadoBio;
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
	 * @return the ativo
	 */
	public Boolean getAtivo() {
		if (ativo == null) {
			ativo = true;
		}
		return ativo;
	}

	/**
	 * @param ativo the ativo to set
	 */
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	/**
	 * @return the sincronizar
	 */
	public Boolean getSincronizar() {
		if (sincronizar == null) {
			sincronizar = true;
		}
		return sincronizar;
	}

	/**
	 * @param sincronizar the sincronizar to set
	 */
	public void setSincronizar(Boolean sincronizar) {
		this.sincronizar = sincronizar;
	}

	/**
	 * @return the dataSincronismo
	 */
	public Date getDataSincronismo() {		
		return dataSincronismo;
	}

	/**
	 * @param dataSincronismo the dataSincronismo to set
	 */
	public void setDataSincronismo(Date dataSincronismo) {
		this.dataSincronismo = dataSincronismo;
	}
	
	

}
