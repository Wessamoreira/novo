package negocio.comuns.blackboard;

import com.fasterxml.jackson.annotation.JsonBackReference;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardPessoaEnum;

public class SalaAulaBlackboardPessoaVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3819202488359126563L;
	private Integer codigo;
	@ExcluirJsonAnnotation
	@JsonBackReference
	private SalaAulaBlackboardVO salaAulaBlackboardVO;
	private TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum;
	private PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO;	
	private String matricula;
	private Integer matriculaPeriodoTurmaDisciplina;
	private String memberId;
	private String userId;
	/**
	 * Transient
	 */
	@ExcluirJsonAnnotation
	private boolean existeUsuarioLogadoNaSala = false;
	@ExcluirJsonAnnotation
	private String nomeGrupoRemanescente;
	
	@ExcluirJsonAnnotation
	private Integer codigoGrupoOrigem;
	@ExcluirJsonAnnotation
	private String nomeGrupoOrigem;
	private HistoricoNotaBlackboardVO historicoNotaBlackboardVO;


	public SalaAulaBlackboardPessoaVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public SalaAulaBlackboardPessoaVO(SalaAulaBlackboardVO salaAulaBlackboardVO,
			TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum,
			PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO, String matricula,
			Integer matriculaPeriodoTurmaDisciplina) {
		super();
		this.salaAulaBlackboardVO = salaAulaBlackboardVO;
		this.tipoSalaAulaBlackboardPessoaEnum = tipoSalaAulaBlackboardPessoaEnum;
		this.pessoaEmailInstitucionalVO = pessoaEmailInstitucionalVO;
		this.matricula = matricula;
		this.matriculaPeriodoTurmaDisciplina = matriculaPeriodoTurmaDisciplina;
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

	public SalaAulaBlackboardVO getSalaAulaBlackboardVO() {
		if (salaAulaBlackboardVO == null) {
			salaAulaBlackboardVO = new SalaAulaBlackboardVO();
		}
		return salaAulaBlackboardVO;
	}

	public void setSalaAulaBlackboardVO(SalaAulaBlackboardVO salaAulaBlackboardVO) {
		this.salaAulaBlackboardVO = salaAulaBlackboardVO;
	}

	public TipoSalaAulaBlackboardPessoaEnum getTipoSalaAulaBlackboardPessoaEnum() {
		if (tipoSalaAulaBlackboardPessoaEnum == null) {
			tipoSalaAulaBlackboardPessoaEnum = TipoSalaAulaBlackboardPessoaEnum.ALUNO;
		}
		return tipoSalaAulaBlackboardPessoaEnum;
	}

	public void setTipoSalaAulaBlackboardPessoaEnum(TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum) {
		this.tipoSalaAulaBlackboardPessoaEnum = tipoSalaAulaBlackboardPessoaEnum;
	}

	public PessoaEmailInstitucionalVO getPessoaEmailInstitucionalVO() {
		if (pessoaEmailInstitucionalVO == null) {
			pessoaEmailInstitucionalVO = new PessoaEmailInstitucionalVO();
		}
		return pessoaEmailInstitucionalVO;
	}

	public void setPessoaEmailInstitucionalVO(PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO) {
		this.pessoaEmailInstitucionalVO = pessoaEmailInstitucionalVO;
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

	public Integer getMatriculaPeriodoTurmaDisciplina() {
		if (matriculaPeriodoTurmaDisciplina == null) {
			matriculaPeriodoTurmaDisciplina = 0;
		}
		return matriculaPeriodoTurmaDisciplina;
	}

	public void setMatriculaPeriodoTurmaDisciplina(Integer matriculaPeriodoTurmaDisciplina) {
		this.matriculaPeriodoTurmaDisciplina = matriculaPeriodoTurmaDisciplina;
	}


	public String getMemberId() {
		if(memberId == null) {
			memberId = "";
		}
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getUserId() {
		if(userId == null) {
			userId = "";
		}
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNomeGrupoRemanescente() {
		if (nomeGrupoRemanescente == null) {
			nomeGrupoRemanescente = "";
		}
		return nomeGrupoRemanescente;
	}

	public void setNomeGrupoRemanescente(String nomeGrupoRemanescente) {
		this.nomeGrupoRemanescente = nomeGrupoRemanescente;
	}

	public Integer getCodigoGrupoOrigem() {
		if (codigoGrupoOrigem == null) {
			codigoGrupoOrigem = 0;
		}
		return codigoGrupoOrigem;
	}

	public void setCodigoGrupoOrigem(Integer codigoGrupoOrigem) {
		this.codigoGrupoOrigem = codigoGrupoOrigem;
	}

	public String getNomeGrupoOrigem() {
		if (nomeGrupoOrigem == null) {
			nomeGrupoOrigem = "";
		}
		return nomeGrupoOrigem;
	}

	public void setNomeGrupoOrigem(String nomeGrupoOrigem) {
		this.nomeGrupoOrigem = nomeGrupoOrigem;
	}


	public HistoricoNotaBlackboardVO getHistoricoNotaBlackboardVO() {
		if (historicoNotaBlackboardVO == null) {
			historicoNotaBlackboardVO = new HistoricoNotaBlackboardVO();
		}
		return historicoNotaBlackboardVO;
	}

	public void setHistoricoNotaBlackboardVO(HistoricoNotaBlackboardVO historicoNotaBlackboardVO) {
		this.historicoNotaBlackboardVO = historicoNotaBlackboardVO;
	}

	public boolean isExisteUsuarioLogadoNaSala() {
		return existeUsuarioLogadoNaSala;
	}

	public void setExisteUsuarioLogadoNaSala(boolean existeUsuarioLogadoNaSala) {
		this.existeUsuarioLogadoNaSala = existeUsuarioLogadoNaSala;
	}
	
	

}
