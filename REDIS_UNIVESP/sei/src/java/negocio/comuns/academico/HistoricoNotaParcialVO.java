package negocio.comuns.academico;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.enumeradores.TipoNotaConceitoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

@XmlRootElement(name = "historicoNotaParcial")
public class HistoricoNotaParcialVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1255603609628671387L;
	private Integer codigo;
	private HistoricoVO historico;
	private TipoNotaConceitoEnum tipoNota;
	private Double nota;
	private TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcial;
	private Date dataAlteracao;
	private UsuarioVO usuario;

	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public HistoricoVO getHistorico() {
		if (historico == null) {
			historico = new HistoricoVO();
		}
		return historico;
	}
	
	public void setHistorico(HistoricoVO historico) {
		this.historico = historico;
	}
	
	@XmlElement(name = "tipoNota")
	public TipoNotaConceitoEnum getTipoNota() {
		return tipoNota;
	}
	
	public void setTipoNota(TipoNotaConceitoEnum tipoNota) {
		this.tipoNota = tipoNota;
	}
	public Double getNota() {
		return nota;
	}
	public void setNota(Double nota) {
		this.nota = nota;
	}
	public TurmaDisciplinaNotaParcialVO getTurmaDisciplinaNotaParcial() {
		if(turmaDisciplinaNotaParcial == null) {
			turmaDisciplinaNotaParcial = new TurmaDisciplinaNotaParcialVO();
		}
		return turmaDisciplinaNotaParcial;
	}
	public void setTurmaDisciplinaNotaParcial(TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcial) {
		this.turmaDisciplinaNotaParcial = turmaDisciplinaNotaParcial;
	}
	public Date getDataAlteracao() {
		return dataAlteracao;
	}
	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}
	public UsuarioVO getUsuario() {
		if(usuario == null) {
			usuario = new UsuarioVO();
		}
		return usuario;
	}
	public void setUsuario(UsuarioVO usuario) {
		this.usuario = usuario;
	}
	
}
