/**
 * 
 */
package negocio.comuns.secretaria;

import java.io.Serializable;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.processosel.ResultadoProcessamentoArquivoRespostaProvaPresencialVO;

/**
 * @author Carlos Eugênio
 *
 */
public class MatriculaProvaPresencialNaoLocalizadaVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private ResultadoProcessamentoArquivoRespostaProvaPresencialVO resultadoProcessamentoArquivoRespostaProvaPresencialVO;
	private String matriculaNaoLocalizada;
	private String respostaGabarito;

	public MatriculaProvaPresencialNaoLocalizadaVO() {
		super();
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

	public String getMatriculaNaoLocalizada() {
		if (matriculaNaoLocalizada == null) {
			matriculaNaoLocalizada = "";
		}
		return matriculaNaoLocalizada;
	}

	public void setMatriculaNaoLocalizada(String matriculaNaoLocalizada) {
		this.matriculaNaoLocalizada = matriculaNaoLocalizada;
	}

	public String getRespostaGabarito() {
		if (respostaGabarito == null) {
			respostaGabarito = "";
		}
		return respostaGabarito;
	}

	public void setRespostaGabarito(String respostaGabarito) {
		this.respostaGabarito = respostaGabarito;
	}
	
	public String getSituacao() {
		return "Matrícula Não Encontrada no SEI";
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
