package negocio.comuns.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.PeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum;
import negocio.comuns.ead.enumeradores.SituacaoEnum;

/**
 * 
 * @author Manoel
 */
public class PoliticaDivulgacaoMatriculaOnlineVO extends SuperVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String nome;
	private String descricao;
	private Date dataCadastro;
	private UsuarioVO usuario;
	private String caminhoBaseLogo;
	private String nomeArquivoLogo;
	private List<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> politicaDivulgacaoMatriculaOnlineAlunoPublicoAlvoVOs;
	private List<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> politicaDivulgacaoMatriculaOnlineCoordenadorPublicoAlvoVOs;
	private List<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> politicaDivulgacaoMatriculaOnlineFuncionarioPublicoAlvoVOs;
	private List<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> politicaDivulgacaoMatriculaOnlineProfessorPublicoAlvoVOs;
	private Boolean logoInformada;
	private CursoVO cursoVO;
	private Boolean divulgarParaComunidade;
	private Boolean divulgarParaAluno;
	private Boolean divulgarParaProfessor;
	private Boolean divulgarParaCoordenador;
	private Boolean divulgarParaFuncionario;
	private SituacaoEnum situacaoEnum;
	private PeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum periodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum;
	private Date periodoDe;
	private Date periodoAte;
	private Date dataAtivacao;
	private UsuarioVO responsavelAtivacao;
	private Date dataInativacao;
	private UsuarioVO responsavelInativacao;
	
	private String urlImagem;
	private String urlClick;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	public Date getDataCadastro() {
		if (dataCadastro == null) {
			dataCadastro = new Date();
		}
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public UsuarioVO getUsuario() {
		if (usuario == null) {
			usuario = new UsuarioVO();
		}
		return usuario;
	}

	public void setUsuario(UsuarioVO usuario) {
		this.usuario = usuario;
	}

	public String getCaminhoBaseLogo() {
		if (caminhoBaseLogo == null) {
			caminhoBaseLogo = "";
		}
		return caminhoBaseLogo;
	}

	public void setCaminhoBaseLogo(String caminhoBaseLogo) {
		this.caminhoBaseLogo = caminhoBaseLogo;
	}

	public String getNomeArquivoLogo() {
		if (nomeArquivoLogo == null) {
			nomeArquivoLogo = "";
		}
		return nomeArquivoLogo;
	}

	public void setNomeArquivoLogo(String nomeArquivoLogo) {
		this.nomeArquivoLogo = nomeArquivoLogo;
	}

	public List<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> getPoliticaDivulgacaoMatriculaOnlineAlunoPublicoAlvoVOs() {
		if (politicaDivulgacaoMatriculaOnlineAlunoPublicoAlvoVOs == null) {
			politicaDivulgacaoMatriculaOnlineAlunoPublicoAlvoVOs = new ArrayList<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO>();
		}
		return politicaDivulgacaoMatriculaOnlineAlunoPublicoAlvoVOs;
	}

	public void setPoliticaDivulgacaoMatriculaOnlineAlunoPublicoAlvoVOs(List<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> politicaDivulgacaoMatriculaOnlineAlunoPublicoAlvoVOs) {
		this.politicaDivulgacaoMatriculaOnlineAlunoPublicoAlvoVOs = politicaDivulgacaoMatriculaOnlineAlunoPublicoAlvoVOs;
	}
	
	public Boolean getExisteLogo() {
		return !getNomeArquivoLogo().trim().isEmpty();
	}	

	public List<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> getPoliticaDivulgacaoMatriculaOnlineCoordenadorPublicoAlvoVOs() {
		if (politicaDivulgacaoMatriculaOnlineCoordenadorPublicoAlvoVOs == null) {
			politicaDivulgacaoMatriculaOnlineCoordenadorPublicoAlvoVOs = new ArrayList<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO>();
		}
		return politicaDivulgacaoMatriculaOnlineCoordenadorPublicoAlvoVOs;
	}

	public void setPoliticaDivulgacaoMatriculaOnlineCoordenadorPublicoAlvoVOs(List<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> politicaDivulgacaoMatriculaOnlineCoordenadorPublicoAlvoVOs) {
		this.politicaDivulgacaoMatriculaOnlineCoordenadorPublicoAlvoVOs = politicaDivulgacaoMatriculaOnlineCoordenadorPublicoAlvoVOs;
	}

	public List<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> getPoliticaDivulgacaoMatriculaOnlineFuncionarioPublicoAlvoVOs() {
		if (politicaDivulgacaoMatriculaOnlineFuncionarioPublicoAlvoVOs == null) {
			politicaDivulgacaoMatriculaOnlineFuncionarioPublicoAlvoVOs = new ArrayList<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO>();
		}
		return politicaDivulgacaoMatriculaOnlineFuncionarioPublicoAlvoVOs;
	}

	public void setPoliticaDivulgacaoMatriculaOnlineFuncionarioPublicoAlvoVOs(List<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> politicaDivulgacaoMatriculaOnlineFuncionarioPublicoAlvoVOs) {
		this.politicaDivulgacaoMatriculaOnlineFuncionarioPublicoAlvoVOs = politicaDivulgacaoMatriculaOnlineFuncionarioPublicoAlvoVOs;
	}

	public List<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> getPoliticaDivulgacaoMatriculaOnlineProfessorPublicoAlvoVOs() {
		if (politicaDivulgacaoMatriculaOnlineProfessorPublicoAlvoVOs == null) {
			politicaDivulgacaoMatriculaOnlineProfessorPublicoAlvoVOs = new ArrayList<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO>();
		}
		return politicaDivulgacaoMatriculaOnlineProfessorPublicoAlvoVOs;
	}

	public void setPoliticaDivulgacaoMatriculaOnlineProfessorPublicoAlvoVOs(List<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> politicaDivulgacaoMatriculaOnlineProfessorPublicoAlvoVOs) {
		this.politicaDivulgacaoMatriculaOnlineProfessorPublicoAlvoVOs = politicaDivulgacaoMatriculaOnlineProfessorPublicoAlvoVOs;
	}

	public Boolean getLogoInformada() {
		if (logoInformada == null) {
			logoInformada = false;
		}
		return logoInformada;
	}

	public void setLogoInformada(Boolean logoInformada) {
		this.logoInformada = logoInformada;
	}

	public CursoVO getCursoVO() {
		if(cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public Boolean getDivulgarParaComunidade() {
		if(divulgarParaComunidade == null) {
			divulgarParaComunidade = false;
		}
		return divulgarParaComunidade;
	}

	public void setDivulgarParaComunidade(Boolean divulgarParaComunidade) {
		this.divulgarParaComunidade = divulgarParaComunidade;
	}

	public SituacaoEnum getSituacaoEnum() {
		if(situacaoEnum == null) {
			situacaoEnum = SituacaoEnum.EM_CONSTRUCAO;
		}
		return situacaoEnum;
	}

	public void setSituacaoEnum(SituacaoEnum situacaoEnum) {
		this.situacaoEnum = situacaoEnum;
	}

	public PeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum getPeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum() {
		if(periodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum == null) {
			periodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum = PeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum.INDETERMINADO;
		}
		return periodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum;
	}

	public void setPeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum(PeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum periodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum) {
		this.periodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum = periodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum;
	}

	public Date getPeriodoDe() {
		if(periodoDe == null) {
			periodoDe = new Date();
		}
		return periodoDe;
	}

	public void setPeriodoDe(Date periodoDe) {
		this.periodoDe = periodoDe;
	}

	public Date getPeriodoAte() {
		if(periodoAte == null) {
			periodoAte = new Date();
		}
		return periodoAte;
	}

	public void setPeriodoAte(Date periodoAte) {
		this.periodoAte = periodoAte;
	}

	public Date getDataAtivacao() {
		if(dataAtivacao == null) {
			dataAtivacao = new Date();
		}
		return dataAtivacao;
	}

	public void setDataAtivacao(Date dataAtivacao) {
		this.dataAtivacao = dataAtivacao;
	}

	public Date getDataInativacao() {
		if(dataInativacao == null) {
			dataInativacao = new Date();
		}
		return dataInativacao;
	}

	public void setDataInativacao(Date dataInativacao) {
		this.dataInativacao = dataInativacao;
	}

	public UsuarioVO getResponsavelAtivacao() {
		if(responsavelAtivacao == null) {
			responsavelAtivacao = new UsuarioVO();
		}
		return responsavelAtivacao;
	}

	public void setResponsavelAtivacao(UsuarioVO responsavelAtivacao) {
		this.responsavelAtivacao = responsavelAtivacao;
	}

	public UsuarioVO getResponsavelInativacao() {
		if(responsavelInativacao == null) {
			responsavelInativacao = new UsuarioVO();
		}
		return responsavelInativacao;
	}

	public void setResponsavelInativacao(UsuarioVO responsavelInativacao) {
		this.responsavelInativacao = responsavelInativacao;
	}

	public Boolean getDivulgarParaAluno() {
		if(divulgarParaAluno == null) {
			divulgarParaAluno = false;
		}
		return divulgarParaAluno;
	}

	public void setDivulgarParaAluno(Boolean divulgarParaAluno) {
		this.divulgarParaAluno = divulgarParaAluno;
	}

	public Boolean getDivulgarParaProfessor() {
		if(divulgarParaProfessor == null) {
			divulgarParaProfessor = false;
		}
		return divulgarParaProfessor;
	}

	public void setDivulgarParaProfessor(Boolean divulgarParaProfessor) {
		this.divulgarParaProfessor = divulgarParaProfessor;
	}

	public Boolean getDivulgarParaCoordenador() {
		if(divulgarParaCoordenador == null) {
			divulgarParaCoordenador = false;
		}
		return divulgarParaCoordenador;
	}

	public void setDivulgarParaCoordenador(Boolean divulgarParaCoordenador) {
		this.divulgarParaCoordenador = divulgarParaCoordenador;
	}

	public Boolean getDivulgarParaFuncionario() {
		if(divulgarParaFuncionario == null) {
			divulgarParaFuncionario = false;
		}
		return divulgarParaFuncionario;
	}

	public void setDivulgarParaFuncionario(Boolean divulgarParaFuncionario) {
		this.divulgarParaFuncionario = divulgarParaFuncionario;
	}

	public String getUrlImagem() {
		return urlImagem;
	}

	public void setUrlImagem(String urlImagem) {
		this.urlImagem = urlImagem;
	}

	public String getUrlClick() {
		return urlClick;
	}

	public void setUrlClick(String urlClick) {
		this.urlClick = urlClick;
	}	
	
	
}
