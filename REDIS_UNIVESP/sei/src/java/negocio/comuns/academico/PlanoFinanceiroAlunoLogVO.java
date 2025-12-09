package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

public class PlanoFinanceiroAlunoLogVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1870861305426423047L;
	private Integer codigo;
	private UsuarioVO responsavel;
	private Date data;
	private String log;
	private MatriculaPeriodoVO matriculaPeriodo;
	
	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}
	
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public UsuarioVO getResponsavel() {
		if(responsavel == null){
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}
	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}
	public Date getData() {
		if(data == null){
			data = new Date();
		}
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	
	public String getLog() {
		if(log == null){
			log = "";
		}
		return log;
	}
	
	public void setLog(String log) {
		this.log = log;
	}

	public MatriculaPeriodoVO getMatriculaPeriodo() {
		if(matriculaPeriodo == null){
			matriculaPeriodo = new MatriculaPeriodoVO();
		}
		return matriculaPeriodo;
	}

	public void setMatriculaPeriodo(MatriculaPeriodoVO matriculaPeriodo) {
		this.matriculaPeriodo = matriculaPeriodo;
	}
	
	

}
