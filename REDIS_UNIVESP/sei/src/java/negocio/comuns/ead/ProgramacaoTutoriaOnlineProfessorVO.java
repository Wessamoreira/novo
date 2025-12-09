package negocio.comuns.ead;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.ead.enumeradores.SituacaoProgramacaoTutoriaOnlineEnum;
import negocio.comuns.gsuite.ClassroomGoogleVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * @author Victor Hugo 11/11/2014
 */
public class ProgramacaoTutoriaOnlineProfessorVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private Integer ordemPrioridade;
	private PessoaVO professor;
	private Integer qtdeAlunosTutoria;
	private Date dateInativacao;
	private UsuarioVO responsavelInativacao;
	@ExcluirJsonAnnotation
	@JsonBackReference
	private ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO;
	private SituacaoProgramacaoTutoriaOnlineEnum situacaoProgramacaoTutoriaOnline;
	private List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs;
	private List<MatriculaPeriodoTurmaDisciplinaVO> listaAlunosInativos;
	/*
	 * Transient
	 */
	private Integer qtdeAlunosAtivos;
	private Integer qtdeAlunosInativos;
	private List<ClassroomGoogleVO> listaClassroomGoogleVO;
	@ExcluirJsonAnnotation
	private List<SalaAulaBlackboardVO> listaSalaAulaBlackboardVO;
	@ExcluirJsonAnnotation
	private List<TutoriaOnlineSalaAulaIntegracaoVO> listaTutoriaOnlineSalaAulaIntegracaoVO;
	
	public void carregarTutoriaOnlineSalaAulaIntegracao() {
		getListaTutoriaOnlineSalaAulaIntegracaoVO().clear();
		getListaClassroomGoogleVO().stream().forEach(p ->{
			TutoriaOnlineSalaAulaIntegracaoVO tosai =new TutoriaOnlineSalaAulaIntegracaoVO();
			tosai.setTurmaVO(p.getTurmaVO());
			tosai.setDisciplinaVO(p.getDisciplinaVO());
			tosai.setAno(p.getAno());
			tosai.setSemestre(p.getSemestre());
			tosai.setProfessorEad(p.getProfessorEad());
			tosai.setClassroomGoogleVO(p);
			getListaTutoriaOnlineSalaAulaIntegracaoVO().add(tosai);
			
		});
		getListaSalaAulaBlackboardVO().stream().forEach(p ->{
			if(getListaTutoriaOnlineSalaAulaIntegracaoVO().isEmpty()) {
				carregarTutoriaOnlineSalaAulaIBlackboard(p);
			}else {
				for (TutoriaOnlineSalaAulaIntegracaoVO tosai : getListaTutoriaOnlineSalaAulaIntegracaoVO()) {
					if(tosai.getTurmaVO().getCodigo().equals(p.getTurmaVO().getCodigo()) ||
							tosai.getDisciplinaVO().getCodigo().equals(p.getDisciplinaVO().getCodigo()) ||
							tosai.getAno().equals(p.getAno()) ||
							tosai.getSemestre().equals(p.getSemestre())) {
						tosai.setSalaAulaBlackboardVO(p);
					}else {
						carregarTutoriaOnlineSalaAulaIBlackboard(p);
					}
				}
			}
		});
		
	}

	private void carregarTutoriaOnlineSalaAulaIBlackboard(SalaAulaBlackboardVO p) {
		TutoriaOnlineSalaAulaIntegracaoVO novoTosai =new TutoriaOnlineSalaAulaIntegracaoVO();
		novoTosai.setTurmaVO(p.getTurmaVO());
		novoTosai.setDisciplinaVO(p.getDisciplinaVO());
		novoTosai.setAno(p.getAno());
		novoTosai.setSemestre(p.getSemestre());		
		novoTosai.setSalaAulaBlackboardVO(p);
		getListaTutoriaOnlineSalaAulaIntegracaoVO().add(novoTosai);
	}

	public Integer getOrdemPrioridade() {
		if (ordemPrioridade == null) {
			ordemPrioridade = 0;
		}
		return ordemPrioridade;
	}

	public void setOrdemPrioridade(Integer ordemPrioridade) {
		this.ordemPrioridade = ordemPrioridade;
	}

	public PessoaVO getProfessor() {
		if (professor == null) {
			professor = new PessoaVO();
		}
		return professor;
	}

	public void setProfessor(PessoaVO professor) {
		this.professor = professor;
	}

	public Integer getQtdeAlunosTutoria() {
		if (qtdeAlunosTutoria == null) {
			qtdeAlunosTutoria = 0;
		}
		return qtdeAlunosTutoria;
	}

	public void setQtdeAlunosTutoria(Integer qtdeAlunosTutoria) {
		this.qtdeAlunosTutoria = qtdeAlunosTutoria;
	}

	public Date getDateInativacao() {
		if (dateInativacao == null) {
			dateInativacao = new Date();
		}
		return dateInativacao;
	}

	public void setDateInativacao(Date dateInativacao) {
		this.dateInativacao = dateInativacao;
	}

	public UsuarioVO getResponsavelInativacao() {
		if (responsavelInativacao == null) {
			responsavelInativacao = new UsuarioVO();
		}
		return responsavelInativacao;
	}

	public void setResponsavelInativacao(UsuarioVO responsavelInativacao) {
		this.responsavelInativacao = responsavelInativacao;
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

	public ProgramacaoTutoriaOnlineVO getProgramacaoTutoriaOnlineVO() {
		if (programacaoTutoriaOnlineVO == null) {
			programacaoTutoriaOnlineVO = new ProgramacaoTutoriaOnlineVO();
		}
		return programacaoTutoriaOnlineVO;
	}

	public void setProgramacaoTutoriaOnlineVO(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO) {
		this.programacaoTutoriaOnlineVO = programacaoTutoriaOnlineVO;
	}

	public SituacaoProgramacaoTutoriaOnlineEnum getSituacaoProgramacaoTutoriaOnline() {
		if (situacaoProgramacaoTutoriaOnline == null) {
			situacaoProgramacaoTutoriaOnline = SituacaoProgramacaoTutoriaOnlineEnum.ATIVO;
		}
		return situacaoProgramacaoTutoriaOnline;
	}

	public void setSituacaoProgramacaoTutoriaOnline(SituacaoProgramacaoTutoriaOnlineEnum situacaoProgramacaoTutoriaOnline) {
		this.situacaoProgramacaoTutoriaOnline = situacaoProgramacaoTutoriaOnline;
	}

	public String getOrdenacaoDistribuicaoQuantitativa() {
		return getQtdeAlunosAtivos() + "P" + getOrdemPrioridade();
	}

	public Integer getQtdeAlunosAtivos() {
		if (qtdeAlunosAtivos == null) {
			qtdeAlunosAtivos = 0;
		}
		return qtdeAlunosAtivos;
	}

	public void setQtdeAlunosAtivos(Integer qtdeAlunosAtivos) {
		this.qtdeAlunosAtivos = qtdeAlunosAtivos;
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> getMatriculaPeriodoTurmaDisciplinaVOs() {
		if (matriculaPeriodoTurmaDisciplinaVOs == null) {
			matriculaPeriodoTurmaDisciplinaVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>();
		}
		return matriculaPeriodoTurmaDisciplinaVOs;
	}

	public void setMatriculaPeriodoTurmaDisciplinaVOs(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs) {
		this.matriculaPeriodoTurmaDisciplinaVOs = matriculaPeriodoTurmaDisciplinaVOs;
	}

	public boolean isQtdAlunoTutoriaMaiorQueAlunoAtivos() {
		return getQtdeAlunosAtivos() < getQtdeAlunosTutoria();
	}

	public Integer getQtdeAlunosInativos() {
		if (qtdeAlunosInativos == null) {
			qtdeAlunosInativos = 0;
		}
		return qtdeAlunosInativos;
	}

	public void setQtdeAlunosInativos(Integer qtdeAlunosInativos) {
		this.qtdeAlunosInativos = qtdeAlunosInativos;
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> getListaAlunosInativos() {
		if (listaAlunosInativos == null) {
			listaAlunosInativos = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>();
		}
		return listaAlunosInativos;
	}

	public void setListaAlunosInativos(List<MatriculaPeriodoTurmaDisciplinaVO> listaAlunosInativos) {
		this.listaAlunosInativos = listaAlunosInativos;
	}

	public List<ClassroomGoogleVO> getListaClassroomGoogleVO() {
		if (listaClassroomGoogleVO == null) {
			listaClassroomGoogleVO = new ArrayList<>();
		}
		return listaClassroomGoogleVO;
	}

	public void setListaClassroomGoogleVO(List<ClassroomGoogleVO> listaClassroomGoogleVO) {
		this.listaClassroomGoogleVO = listaClassroomGoogleVO;
	}

	public List<SalaAulaBlackboardVO> getListaSalaAulaBlackboardVO() {
		if (listaSalaAulaBlackboardVO == null) {
			listaSalaAulaBlackboardVO = new ArrayList<>();
		}
		return listaSalaAulaBlackboardVO;
	}

	public void setListaSalaAulaBlackboardVO(List<SalaAulaBlackboardVO> listaSalaAulaBlackboardVO) {
		this.listaSalaAulaBlackboardVO = listaSalaAulaBlackboardVO;
	}
	
	public boolean isExisteTutoriaOnlineIntegracao() {
		return (Uteis.isAtributoPreenchido(getListaClassroomGoogleVO()) || Uteis.isAtributoPreenchido(getListaSalaAulaBlackboardVO()));
	}

	public List<TutoriaOnlineSalaAulaIntegracaoVO> getListaTutoriaOnlineSalaAulaIntegracaoVO() {
		if (listaTutoriaOnlineSalaAulaIntegracaoVO == null) {
			listaTutoriaOnlineSalaAulaIntegracaoVO = new ArrayList<>();
		}
		return listaTutoriaOnlineSalaAulaIntegracaoVO;
	}

	public void setListaTutoriaOnlineSalaAulaIntegracaoVO(List<TutoriaOnlineSalaAulaIntegracaoVO> listaTutoriaOnlineSalaAulaIntegracaoVO) {
		this.listaTutoriaOnlineSalaAulaIntegracaoVO = listaTutoriaOnlineSalaAulaIntegracaoVO;
	}
	
	
	
	
	
	

	
	
	
	
	
	
	
}
