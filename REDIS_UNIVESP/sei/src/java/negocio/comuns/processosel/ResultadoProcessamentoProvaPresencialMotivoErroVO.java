/**
 * 
 */
package negocio.comuns.processosel;

import java.io.Serializable;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.SuperVO;

/**
 * @author Carlos Eugênio
 *
 */
public class ResultadoProcessamentoProvaPresencialMotivoErroVO extends SuperVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private ResultadoProcessamentoArquivoRespostaProvaPresencialVO resultadoProcessamentoArquivoRespostaProvaPresencialVO;
	private MatriculaVO matriculaVO;
	private DisciplinaVO disciplinaVO;
	private String mensagemErro;

	public ResultadoProcessamentoProvaPresencialMotivoErroVO() {
		super();
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
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

	public String getMensagemErro() {
		if (mensagemErro == null) {
			mensagemErro = "";
		}
		return mensagemErro;
	}

	public void setMensagemErro(String mensagemErro) {
		this.mensagemErro = mensagemErro;
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

	public ResultadoProcessamentoArquivoRespostaProvaPresencialVO getResultadoProcessamentoArquivoRespostaProvaPresencialVO() {
		if (resultadoProcessamentoArquivoRespostaProvaPresencialVO == null) {
			resultadoProcessamentoArquivoRespostaProvaPresencialVO = new ResultadoProcessamentoArquivoRespostaProvaPresencialVO();
		}
		return resultadoProcessamentoArquivoRespostaProvaPresencialVO;
	}

	public void setResultadoProcessamentoArquivoRespostaProvaPresencialVO(ResultadoProcessamentoArquivoRespostaProvaPresencialVO resultadoProcessamentoArquivoRespostaProvaPresencialVO) {
		this.resultadoProcessamentoArquivoRespostaProvaPresencialVO = resultadoProcessamentoArquivoRespostaProvaPresencialVO;
	}
	
}
