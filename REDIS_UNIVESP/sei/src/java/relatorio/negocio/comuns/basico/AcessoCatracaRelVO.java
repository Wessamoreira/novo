/**
 * 
 */
package relatorio.negocio.comuns.basico;

import java.io.Serializable;
import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import relatorio.negocio.comuns.basico.enumeradores.TipoAcessoCatracaEnum;

/**
 * @author Carlos Eugênio
 *
 */
public class AcessoCatracaRelVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nomePessoa;
	private String matricula;
	private String identificadorTurma;
	private Boolean acessouCatraca;
	private TipoAcessoCatracaEnum tipoAcessoCatraca;
	private String dataAcesso;
	private String horaAcesso;
	private String catraca;
	private String tipoRelatorio;
	private Integer direcao;

	public AcessoCatracaRelVO() {
		super();
	}

	public String getNomePessoa() {
		if (nomePessoa == null) {
			nomePessoa = "";
		}
		return nomePessoa;
	}

	public void setNomePessoa(String nomePessoa) {
		this.nomePessoa = nomePessoa;
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

	public String getIdentificadorTurma() {
		if (identificadorTurma == null) {
			identificadorTurma = "";
		}
		return identificadorTurma;
	}

	public void setIdentificadorTurma(String identificadorTurma) {
		this.identificadorTurma = identificadorTurma;
	}

	public Boolean getAcessouCatraca() {
		if (acessouCatraca == null) {
			acessouCatraca = Boolean.FALSE;
		}
		return acessouCatraca;
	}

	public void setAcessouCatraca(Boolean acessouCatraca) {
		this.acessouCatraca = acessouCatraca;
	}

	public TipoAcessoCatracaEnum getTipoAcessoCatraca() {
		if (tipoAcessoCatraca == null) {
			tipoAcessoCatraca = TipoAcessoCatracaEnum.ENTRADA;
		}
		return tipoAcessoCatraca;
	}

	public void setTipoAcessoCatraca(TipoAcessoCatracaEnum tipoAcessoCatraca) {
		this.tipoAcessoCatraca = tipoAcessoCatraca;
	}

	public String getHoraAcesso() {
		if (horaAcesso == null) {
			horaAcesso = "";
		}
		return horaAcesso;
	}

	public void setHoraAcesso(String horaAcesso) {
		this.horaAcesso = horaAcesso;
	}

	public String getCatraca() {
		if (catraca == null) {
			catraca = "";
		}
		return catraca;
	}

	public void setCatraca(String catraca) {
		this.catraca = catraca;
	}

	public String getDataAcesso() {
		if (dataAcesso == null) {
			dataAcesso = "";
		}
		return dataAcesso;
	}

	public void setDataAcesso(String dataAcesso) {
		this.dataAcesso = dataAcesso;
	}

	public String getTipoRelatorio() {
		if (tipoRelatorio == null) {
			tipoRelatorio = "TODOS";
		}
		return tipoRelatorio;
	}

	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public Integer getDirecao() {
		if (direcao == null) {
			direcao = 0;
		}
		return direcao;
	}

	public void setDirecao(Integer direcao) {
		this.direcao = direcao;
	}

}
