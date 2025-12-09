package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

public class TrabalhoConclusaoCursoArtefatoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -356246938786832194L;
	private Integer codigo;
	private ConfiguracaoTCCArtefatoVO configuracaoTCCArtefato;
	private Boolean entregue;
	private Date dataEntrega;
	private UsuarioVO responsavelRegistro;
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

	public ConfiguracaoTCCArtefatoVO getConfiguracaoTCCArtefato() {
		if(configuracaoTCCArtefato == null){
			configuracaoTCCArtefato = new ConfiguracaoTCCArtefatoVO();
		}
		return configuracaoTCCArtefato;
	}

	public void setConfiguracaoTCCArtefato(ConfiguracaoTCCArtefatoVO configuracaoTCCArtefato) {
		this.configuracaoTCCArtefato = configuracaoTCCArtefato;
	}

	public Boolean getEntregue() {
		if(entregue == null){
			entregue = false;
		}
		return entregue;
	}

	public void setEntregue(Boolean entregue) {
		this.entregue = entregue;
	}

	public Date getDataEntrega() {		
		return dataEntrega;
	}

	public void setDataEntrega(Date dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public UsuarioVO getResponsavelRegistro() {
		if(responsavelRegistro == null){
			responsavelRegistro = new UsuarioVO();
		}
		return responsavelRegistro;
	}

	public void setResponsavelRegistro(UsuarioVO responsavelRegistro) {
		this.responsavelRegistro = responsavelRegistro;
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

}
