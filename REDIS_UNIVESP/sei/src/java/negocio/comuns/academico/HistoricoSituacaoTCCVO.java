package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.academico.enumeradores.EtapaTCCEnum;
import negocio.comuns.academico.enumeradores.SituacaoTCCEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

public class HistoricoSituacaoTCCVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -196183886189080595L;
	private Integer codigo;
	private Date dataSituacao;
	private UsuarioVO usuario;
	private String tipoUsuario;
	private SituacaoTCCEnum situacaoTCC;
	private EtapaTCCEnum etapaTCC;
	private TrabalhoConclusaoCursoVO trabalhoConclusaoCurso;

	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public String getDataSituacaoApresentar() {
		if(dataSituacao == null){
			return "";
		}
		return Uteis.getDataComHora(getDataSituacao());
	}

	public Date getDataSituacao() {
		if(dataSituacao == null){
			dataSituacao = new Date();
		}
		return dataSituacao;
	}

	public void setDataSituacao(Date dataSituacao) {
		this.dataSituacao = dataSituacao;
	}
	
	public String getUsuarioApresentar() {
		if (getUsuario().getCodigo().intValue() != 0) {
			return getTipoUsuario() + ":" + getUsuario().getNome();
		} else {
			return getTipoUsuario();
		}
	}

	public UsuarioVO getUsuario() {
		if(usuario == null){
			usuario = new UsuarioVO();
		}
		return usuario;
	}

	public void setUsuario(UsuarioVO usuario) {
		this.usuario = usuario;
	}

	public SituacaoTCCEnum getSituacaoTCC() {
		return situacaoTCC;
	}

	public void setSituacaoTCC(SituacaoTCCEnum situacaoTCC) {
		this.situacaoTCC = situacaoTCC;
	}

	public EtapaTCCEnum getEtapaTCC() {		
		return etapaTCC;
	}

	public void setEtapaTCC(EtapaTCCEnum etapaTCC) {
		this.etapaTCC = etapaTCC;
	}

	public TrabalhoConclusaoCursoVO getTrabalhoConclusaoCurso() {
		if(trabalhoConclusaoCurso == null){
			trabalhoConclusaoCurso = new TrabalhoConclusaoCursoVO();
		}
		return trabalhoConclusaoCurso;
	}

	public void setTrabalhoConclusaoCurso(TrabalhoConclusaoCursoVO trabalhoConclusaoCurso) {
		this.trabalhoConclusaoCurso = trabalhoConclusaoCurso;
	}

	public String getTipoUsuario() {
		if (tipoUsuario == null) {
			tipoUsuario = "";
		}
		return tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

}
