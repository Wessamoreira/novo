package negocio.comuns.administrativo;

import java.util.Date;

import negocio.comuns.administrativo.enumeradores.TipoOcorrenciaEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;

public class OcorrenciaLGPDVO extends SuperVO{

	private static final long serialVersionUID = 1L;
	
	private Integer codigo;
	private Date dataCadastro;
	private PessoaVO pessoaVO;
	private TipoOcorrenciaEnum tipoOcorrenciaEnum;
	private Date created;
	private Date updated;
	private Integer codigoCreated;
	private Integer codigoUpdated;
	private String nomeCreated;
	private String nomeUpdated;
	
	public OcorrenciaLGPDVO() {
	}

	public OcorrenciaLGPDVO(Integer codigo, Date dataCadastro, PessoaVO pessoaVO) {
		this.codigo = codigo;
		this.dataCadastro = dataCadastro;
		this.pessoaVO = pessoaVO;
	}

	public Integer getCodigo() {
		if(codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Date getDataCadastro() {
		if(dataCadastro == null) {
			dataCadastro = new Date();
		}
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public PessoaVO getPessoaVO() {
		if(pessoaVO == null) {
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	public TipoOcorrenciaEnum getTipoOcorrenciaEnum() {
		if(tipoOcorrenciaEnum == null) {
			tipoOcorrenciaEnum = TipoOcorrenciaEnum.ACESSO_DADOS;
		}
		return tipoOcorrenciaEnum;
	}

	public void setTipoOcorrenciaEnum(TipoOcorrenciaEnum tipoOcorrenciaEnum) {
		this.tipoOcorrenciaEnum = tipoOcorrenciaEnum;
	}

	public Date getCreated() {
		if(created == null) {
			created = new Date();
		}
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		if(updated == null) {
			updated = new Date();
		}
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Integer getCodigoCreated() {
		if(codigoCreated == null) {
			codigoCreated = 0;
		}
		return codigoCreated;
	}

	public void setCodigoCreated(Integer codigoCreated) {
		this.codigoCreated = codigoCreated;
	}

	public Integer getCodigoUpdated() {
		if(codigoUpdated == null) {
			codigoUpdated = 0;
		}
		return codigoUpdated;
	}

	public void setCodigoUpdated(Integer codigoUpdated) {
		this.codigoUpdated = codigoUpdated;
	}

	public String getNomeCreated() {
		if(nomeCreated == null) {
			nomeCreated = "";
		}
		return nomeCreated;
	}

	public void setNomeCreated(String nomeCreated) {
		this.nomeCreated = nomeCreated;
	}

	public String getNomeUpdated() {
		if(nomeUpdated == null) {
			nomeUpdated = "";
		}
		return nomeUpdated;
	}

	public void setNomeUpdated(String nomeUpdated) {
		this.nomeUpdated = nomeUpdated;
	}
}
