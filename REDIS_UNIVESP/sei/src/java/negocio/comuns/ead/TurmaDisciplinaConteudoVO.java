package negocio.comuns.ead;

import java.util.Date;

import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * @author Victor Hugo 08/01/2015
 */
public class TurmaDisciplinaConteudoVO extends SuperVO implements Comparable<TurmaDisciplinaConteudoVO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer codigo;
	private TurmaVO turmaVO;
	private DisciplinaVO disciplinaVO;
	private String ano;
	private String semestre;
	private ConteudoVO conteudoVO;
	private AvaliacaoOnlineVO avaliacaoOnlineVO;
	private UsuarioVO usuarioVO;
	private Date dataInclusao;
	
	/*@transient*/
	private String usuarioPorExtenso;
	private String qtdeAlunoConteudoCorreto;
	private String codigoMatPerTurmaDiscAlunoConteudoCorreto;
	private String qtdeAlunoOutroAnoSemetreUsandoConteudoAntigo;
	private String codigoMatPerTurDisciplinaOutroAnoSemetreUsandoConteudoAntigo;
	private String qtdeAluno ;
    private String logAlteracao;
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}
	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}
	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}
	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
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
	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}
	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}
	public ConteudoVO getConteudoVO() {
		if (conteudoVO == null) {
			conteudoVO = new ConteudoVO();
		}
		return conteudoVO;
	}
	public void setConteudoVO(ConteudoVO conteudoVO) {
		this.conteudoVO = conteudoVO;
	}
	
	@Override
	public int compareTo(TurmaDisciplinaConteudoVO o) {
		if(this.ano.compareTo(o.ano) == 0 && this.semestre.compareTo(o.semestre) == 0 && this.turmaVO.getCodigo().equals(o.turmaVO.getCodigo()) && this.disciplinaVO.getCodigo().equals(o.disciplinaVO.getCodigo())) {
			return 0;
		}
		return 1;
	}
	
	public String getAnoSemestre() {
		if (!getSemestre().equals("")) {
			return getAno() + "/" + getSemestre();
		} else if(!getAno().equals("")){
			return getAno();
		} else {
			return "-";
		}
	}
	public AvaliacaoOnlineVO getAvaliacaoOnlineVO() {
		if (avaliacaoOnlineVO == null) {
			avaliacaoOnlineVO = new AvaliacaoOnlineVO();
		}
		return avaliacaoOnlineVO;
	}
	public void setAvaliacaoOnlineVO(AvaliacaoOnlineVO avaliacaoOnlineVO) {
		this.avaliacaoOnlineVO = avaliacaoOnlineVO;
	}
	
	public String getUsuarioPorExtenso() {
		return usuarioPorExtenso;
	}
	public void setUsuarioPorExtenso(String usuarioPorExtenso) {
		this.usuarioPorExtenso = usuarioPorExtenso;
	}
	
	
	public String getQtdeAlunoConteudoCorreto() {
		if(qtdeAlunoConteudoCorreto == null) {
			qtdeAlunoConteudoCorreto = "";
		}
		return qtdeAlunoConteudoCorreto;
	}
	public void setQtdeAlunoConteudoCorreto(String qtdeAlunoConteudoCorreto) {
		this.qtdeAlunoConteudoCorreto = qtdeAlunoConteudoCorreto;
	}
	public String getCodigoMatPerTurmaDiscAlunoConteudoCorreto() {
		if(codigoMatPerTurmaDiscAlunoConteudoCorreto == null) {
			codigoMatPerTurmaDiscAlunoConteudoCorreto ="" ;
		}
		return codigoMatPerTurmaDiscAlunoConteudoCorreto;
	}
	public void setCodigoMatPerTurmaDiscAlunoConteudoCorreto(String codigoMatPerTurmaDiscAlunoConteudoCorreto) {
		this.codigoMatPerTurmaDiscAlunoConteudoCorreto = codigoMatPerTurmaDiscAlunoConteudoCorreto;
	}
	public String getQtdeAlunoOutroAnoSemetreUsandoConteudoAntigo() {
		if(qtdeAlunoOutroAnoSemetreUsandoConteudoAntigo == null) {
			qtdeAlunoOutroAnoSemetreUsandoConteudoAntigo ="";
		}
		return qtdeAlunoOutroAnoSemetreUsandoConteudoAntigo;
	}
	public void setQtdeAlunoOutroAnoSemetreUsandoConteudoAntigo(String qtdeAlunoOutroAnoSemetreUsandoConteudoAntigo) {
		this.qtdeAlunoOutroAnoSemetreUsandoConteudoAntigo = qtdeAlunoOutroAnoSemetreUsandoConteudoAntigo;
	}
	public String getCodigoMatPerTurDisciplinaOutroAnoSemetreUsandoConteudoAntigo() {
		if(codigoMatPerTurDisciplinaOutroAnoSemetreUsandoConteudoAntigo == null) {
			codigoMatPerTurDisciplinaOutroAnoSemetreUsandoConteudoAntigo = "";
		}
		return codigoMatPerTurDisciplinaOutroAnoSemetreUsandoConteudoAntigo;
	}
	public void setCodigoMatPerTurDisciplinaOutroAnoSemetreUsandoConteudoAntigo(
			String codigoMatPerTurDisciplinaOutroAnoSemetreUsandoConteudoAntigo) {
		this.codigoMatPerTurDisciplinaOutroAnoSemetreUsandoConteudoAntigo = codigoMatPerTurDisciplinaOutroAnoSemetreUsandoConteudoAntigo;
	}
	public String getQtdeAluno() {
		if(qtdeAluno == null ) {
			qtdeAluno = "";
		}
		return qtdeAluno;
	}
	public void setQtdeAluno(String qtdeAluno) {
		this.qtdeAluno = qtdeAluno;
	}	
	
	public String getLogAlteracao() {
		if(logAlteracao == null ) {
			logAlteracao = "";
		}
		return logAlteracao;
	}

	public void setLogAlteracao(String logAlteracao) {
		this.logAlteracao = logAlteracao;
	}
	public Date getDataInclusao() {
		
		return dataInclusao;
	}
	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}
	public UsuarioVO getUsuarioVO() {
		if(usuarioVO == null) {
			usuarioVO = new UsuarioVO();
		}
		return usuarioVO;
	}
	public void setUsuarioVO(UsuarioVO usuarioVO) {
		this.usuarioVO = usuarioVO;
	}
}
