package negocio.comuns.recursoshumanos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

public class AtividadeExtraClasseProfessorVO extends SuperVO {

	private static final long serialVersionUID = -6626163883737977139L;

	private Integer codigo;
	private FuncionarioCargoVO funcionarioCargo;
	private Integer horaPrevista;
	private Date data;
	private Date dataCadastro;
	private UsuarioVO usuarioResponsavel;
	private Date dataLimiteRegistro;
	private Date dataLimiteAprovacao;

	//Transiente
	private Integer totalHorasIndeferidas;
	private Integer totalHorasAprovadas;
	private Integer totalHorasAguardandoAprovacao;
	
	private List<AtividadeExtraClasseProfessorCursoVO> atividadeExtraClasseProfessorCursoVOs;

	public enum EnumCampoConsultaHoraAtividadeExtraClasseProfessor {
		FUNCIONARIO, MATRICULA_CARGO, MATRICULA_FUNCIONARIO;
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

	public FuncionarioCargoVO getFuncionarioCargo() {
		if (funcionarioCargo == null) {
			funcionarioCargo = new FuncionarioCargoVO();
		}
		return funcionarioCargo;
	}

	public void setFuncionarioCargo(FuncionarioCargoVO funcionarioCargo) {
		this.funcionarioCargo = funcionarioCargo;
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

	public Date getDataCadastro() {
		if (dataCadastro == null) {
			dataCadastro = new Date();
		}
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Integer getHoraPrevista() {
		if (horaPrevista == null) {
			horaPrevista = 0;
		}
		return horaPrevista;
	}

	public void setHoraPrevista(Integer horaPrevista) {
		this.horaPrevista = horaPrevista;
	}

	public UsuarioVO getUsuarioResponsavel() {
		if (usuarioResponsavel == null) {
			usuarioResponsavel = new UsuarioVO();
		}
		return usuarioResponsavel;
	}

	public void setUsuarioResponsavel(UsuarioVO usuarioResponsavel) {
		this.usuarioResponsavel = usuarioResponsavel;
	}

	public Integer getTotalHorasIndeferidas() {
		if (totalHorasIndeferidas == null) {
			totalHorasIndeferidas = 0;
		}
		return totalHorasIndeferidas;
	}

	public void setTotalHorasIndeferidas(Integer totalHorasIndeferidas) {
		this.totalHorasIndeferidas = totalHorasIndeferidas;
	}

	public Integer getTotalHorasAprovadas() {
		if (totalHorasAprovadas == null) {
			totalHorasAprovadas = 0;
		}
		return totalHorasAprovadas;
	}

	public void setTotalHorasAprovadas(Integer totalHorasAprovadas) {
		this.totalHorasAprovadas = totalHorasAprovadas;
	}

	public Integer getTotalHorasAguardandoAprovacao() {
		if (totalHorasAguardandoAprovacao == null) {
			totalHorasAguardandoAprovacao = 0;
		}
		return totalHorasAguardandoAprovacao;
	}

	public void setTotalHorasAguardandoAprovacao(Integer totalHorasAguardandoAprovacao) {
		this.totalHorasAguardandoAprovacao = totalHorasAguardandoAprovacao;
	}

	public Date getDataLimiteRegistro() {
		if (dataLimiteRegistro == null) {
			dataLimiteRegistro = new Date();
		}
		return dataLimiteRegistro;
	}

	public void setDataLimiteRegistro(Date dataLimiteRegistro) {
		this.dataLimiteRegistro = dataLimiteRegistro;
	}

	public Date getDataLimiteAprovacao() {
		if (dataLimiteAprovacao == null) {
			dataLimiteAprovacao = new Date();
		}
		return dataLimiteAprovacao;
	}

	public void setDataLimiteAprovacao(Date dataLimiteAprovacao) {
		this.dataLimiteAprovacao = dataLimiteAprovacao;
	}

	public List<AtividadeExtraClasseProfessorCursoVO> getAtividadeExtraClasseProfessorCursoVOs() {
		if (atividadeExtraClasseProfessorCursoVOs == null) {
			atividadeExtraClasseProfessorCursoVOs = new ArrayList<>(0);
		}
		return atividadeExtraClasseProfessorCursoVOs;
	}

	public void setAtividadeExtraClasseProfessorCursoVOs(
			List<AtividadeExtraClasseProfessorCursoVO> atividadeExtraClasseProfessorCursoVOs) {
		this.atividadeExtraClasseProfessorCursoVOs = atividadeExtraClasseProfessorCursoVOs;
	}
	
	public int getTotalHoraPrevistaDefinirHorasCurso() {
		return getAtividadeExtraClasseProfessorCursoVOs().stream().mapToInt(AtividadeExtraClasseProfessorCursoVO::getHoraPrevista).sum();
	}

	public int getTotalHoraAprovadaDefinirHorasCurso() {
		return getAtividadeExtraClasseProfessorCursoVOs().stream().mapToInt(AtividadeExtraClasseProfessorCursoVO::getTotalHorasAprovadas).sum();
	}

	public int getTotalHoraAguardandoAprovacaoDefinirHorasCurso() {
		return getAtividadeExtraClasseProfessorCursoVOs().stream().mapToInt(AtividadeExtraClasseProfessorCursoVO::getTotalHorasAguardandoAprovacao).sum();
	}

	public int getTotalHoraIndeferidaDefinirHorasCurso() {
		return getAtividadeExtraClasseProfessorCursoVOs().stream().mapToInt(AtividadeExtraClasseProfessorCursoVO::getTotalHorasIndeferidas).sum();
	}
}