package negocio.comuns.recursoshumanos;

import java.util.Date;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoHoraAtividadeExtraClasseEnum;
import negocio.comuns.utilitarias.Uteis;

public class AtividadeExtraClasseProfessorPostadoVO extends SuperVO {

	private static final long serialVersionUID = -6626163883737977139L;

	private Integer codigo;
	private Date dataAtividade;
	private Date dataCadastro;
	private String descricao;
	private Integer horasRealizada;
	private ArquivoVO arquivo;
	private SituacaoHoraAtividadeExtraClasseEnum situacaoHoraAtividadeExtraClasseEnum;
	private FuncionarioCargoVO funcionarioCargo;
	private AtividadeExtraClasseProfessorVO atividadeExtraClasseProfessorVO;
	private String motivoIndeferimento;
	private Date dataAprovacao;
	private Date dataIndeferimento;
	private UsuarioVO usuarioResponsavel;
	private Boolean realizadoDownloadArquivo;
	private String log;
	private CursoVO cursoVO;

	//Atibutos Transiente
	private Integer horasAguardandoAprovacao;
	private Integer horasAprovada;
	private Integer horasIndeferidas; 

	public enum EnumCampoConsultaAtividadeExtraClasseProfessorPostado {
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

	public Date getDataAtividade() {
		if (dataAtividade == null) {
			dataAtividade = new Date();
		}
		return dataAtividade;
	}

	public void setDataAtividade(Date dataAtividade) {
		this.dataAtividade = dataAtividade;
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

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getHorasRealizada() {
		if (horasRealizada == null) {
			horasRealizada = 0;
		}
		return horasRealizada;
	}

	public void setHorasRealizada(Integer horasRealizada) {
		this.horasRealizada = horasRealizada;
	}

	public ArquivoVO getArquivo() {
		if (arquivo == null) {
			arquivo = new ArquivoVO();
		}
		return arquivo;
	}

	public void setArquivo(ArquivoVO arquivo) {
		this.arquivo = arquivo;
	}

	public SituacaoHoraAtividadeExtraClasseEnum getSituacaoHoraAtividadeExtraClasseEnum() {
		if (situacaoHoraAtividadeExtraClasseEnum == null) {
			situacaoHoraAtividadeExtraClasseEnum = SituacaoHoraAtividadeExtraClasseEnum.AGUARDANDO_APROVACAO;
		}
		return situacaoHoraAtividadeExtraClasseEnum;
	}

	public void setSituacaoHoraAtividadeExtraClasseEnum(
			SituacaoHoraAtividadeExtraClasseEnum situacaoHoraAtividadeExtraClasseEnum) {
		this.situacaoHoraAtividadeExtraClasseEnum = situacaoHoraAtividadeExtraClasseEnum;
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

	public AtividadeExtraClasseProfessorVO getAtividadeExtraClasseProfessorVO() {
		if (atividadeExtraClasseProfessorVO == null) {
			atividadeExtraClasseProfessorVO = new AtividadeExtraClasseProfessorVO();
		}
		return atividadeExtraClasseProfessorVO;
	}

	public void setAtividadeExtraClasseProfessorVO(
			AtividadeExtraClasseProfessorVO atividadeExtraClasseProfessorVO) {
		this.atividadeExtraClasseProfessorVO = atividadeExtraClasseProfessorVO;
	}

	public Integer getHorasAguardandoAprovacao() {
		if (horasAguardandoAprovacao == null) {
			horasAguardandoAprovacao = 0;
		}
		return horasAguardandoAprovacao;
	}

	public void setHorasAguardandoAprovacao(Integer horasAguardandoAprovacao) {
		this.horasAguardandoAprovacao = horasAguardandoAprovacao;
	}

	public Integer getHorasAprovada() {
		if (horasAprovada == null) {
			horasAprovada = 0;
		}
		return horasAprovada;
	}

	public void setHorasAprovada(Integer horasAprovada) {
		this.horasAprovada = horasAprovada;
	}

	public Integer getHorasIndeferidas() {
		if (horasIndeferidas == null) {
			horasIndeferidas = 0;
		}
		return horasIndeferidas;
	}

	public void setHorasIndeferidas(Integer horasIndeferidas) {
		this.horasIndeferidas = horasIndeferidas;
	}

	public String getMotivoIndeferimento() {
		if (motivoIndeferimento == null) {
			motivoIndeferimento = "";
		}
		return motivoIndeferimento;
	}

	public void setMotivoIndeferimento(String motivoIndeferimento) {
		this.motivoIndeferimento = motivoIndeferimento;
	}

	public Date getDataAprovacao() {
		return dataAprovacao;
	}

	public void setDataAprovacao(Date dataAprovacao) {
		this.dataAprovacao = dataAprovacao;
	}

	public String getSituacaoHoraAtividadeExtraClasseValor() {
		if (Uteis.isAtributoPreenchido(getSituacaoHoraAtividadeExtraClasseEnum())) {
			return getSituacaoHoraAtividadeExtraClasseEnum().name();
		}
		return "";
	}

	public String getSituacaoHoraAtividadeExtraClasseApresentar() {
		if (Uteis.isAtributoPreenchido(getSituacaoHoraAtividadeExtraClasseEnum())) {
			return getSituacaoHoraAtividadeExtraClasseEnum().getDescricao();
		}
		return "";
	}

	public Date getDataIndeferimento() {
		return dataIndeferimento;
	}

	public void setDataIndeferimento(Date dataIndeferimento) {
		this.dataIndeferimento = dataIndeferimento;
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

	public boolean getRealizadoDownloadArquivo() {
		if (realizadoDownloadArquivo == null) {
			realizadoDownloadArquivo = Boolean.FALSE;
		}
		return realizadoDownloadArquivo;
	}

	public void setRealizadoDownloadArquivo(boolean realizadoDownloadArquivo) {
		this.realizadoDownloadArquivo = realizadoDownloadArquivo;
	}

	public String getLog() {
		if (log == null) {
			log = "";
		}
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}
}