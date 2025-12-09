package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

public class AdvertenciaVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private MatriculaVO matriculaVO;
	private Date dataAdvertencia;
	private TipoAdvertenciaVO tipoAdvertenciaVO;
	private BimestreEnum bimestre;
	private String semestre;
	private String ano;
	private String advertencia;
	private UsuarioVO responsavel;
	private Date dataCadastro;
	private FuncionarioVO professor;
	private String observacao;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public Date getDataAdvertencia() {
		if (dataAdvertencia == null) {
			dataAdvertencia = new Date();
		}
		return dataAdvertencia;
	}

	public String getDataAdvertencia_Apresentar() {
		return (Uteis.getData(dataAdvertencia));
	}

	public void setDataAdvertencia(Date dataAdvertencia) {
		this.dataAdvertencia = dataAdvertencia;
	}

	public TipoAdvertenciaVO getTipoAdvertenciaVO() {
		if (tipoAdvertenciaVO == null) {
			tipoAdvertenciaVO = new TipoAdvertenciaVO();
		}
		return tipoAdvertenciaVO;
	}

	public void setTipoAdvertenciaVO(TipoAdvertenciaVO tipoAdvertenciaVO) {
		this.tipoAdvertenciaVO = tipoAdvertenciaVO;
	}

	public BimestreEnum getBimestre() {
		return bimestre;
	}

	public String getBimestre_Apresentar() {
		return BimestreEnum.getEnum(bimestre.getValor()).getValorApresentar();
	}

	public void setBimestre(BimestreEnum bimestre) {
		this.bimestre = bimestre;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public String getAdvertencia() {
		if (advertencia == null) {
			advertencia = "";
		}
		return advertencia;
	}

	public void setAdvertencia(String advertencia) {
		this.advertencia = advertencia;
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

	public Date getDataCadastro() {
		if (dataCadastro == null) {
			dataCadastro = new Date();
		}
		return dataCadastro;
	}

	public String getDataCadastro_Apresentar() {
		return (Uteis.getData(getDataCadastro()));
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public FuncionarioVO getProfessor() {
		if (professor == null) {
			professor = new FuncionarioVO();
		}
		return professor;
	}

	public void setProfessor(FuncionarioVO professor) {
		this.professor = professor;
	}

	public String getObservacao() {
		if (observacao == null) {
			observacao = "";
		}
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

}
