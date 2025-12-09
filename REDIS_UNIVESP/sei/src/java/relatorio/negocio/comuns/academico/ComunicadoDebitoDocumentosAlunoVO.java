package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.TipoDocumentoVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class ComunicadoDebitoDocumentosAlunoVO {

	private String unidadeEnsino;
	private String nome;
	private String matricula;
	private String turma;
	private String dataHoje;
	private List<TipoDocumentoVO> documentacaoMatriculaVOs;

	public ComunicadoDebitoDocumentosAlunoVO() {

	}

	public String getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = "";
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
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

	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getTurma() {
		if (turma == null) {
			turma = "";
		}
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

	public String getDataHoje() {
		if (dataHoje == null) {
			dataHoje = "";
		}
		return dataHoje;
	}

	public void setDataHoje(String dataHoje) {
		this.dataHoje = dataHoje;
	}

	public List<TipoDocumentoVO> getDocumentacaoMatriculaVOs() {
		if (documentacaoMatriculaVOs == null) {
			documentacaoMatriculaVOs = new ArrayList<TipoDocumentoVO>(0);
		}
		return documentacaoMatriculaVOs;
	}

	public void setDocumentacaoMatriculaVOs(List<TipoDocumentoVO> documentacaoMatriculaVOs) {
		this.documentacaoMatriculaVOs = documentacaoMatriculaVOs;
	}

	public JRDataSource getListaDocumentacaoMatriculaVOs() {
		JRDataSource jr = new JRBeanArrayDataSource(getDocumentacaoMatriculaVOs().toArray());
		return jr;
	}

}
