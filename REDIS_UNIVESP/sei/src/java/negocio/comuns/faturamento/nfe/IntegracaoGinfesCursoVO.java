package negocio.comuns.faturamento.nfe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

public class IntegracaoGinfesCursoVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private Date dataImportacao;
	private Boolean importado;
	private UnidadeEnsinoVO unidadeEnsino;
	private List<IntegracaoGinfesCursoItemVO> cursos;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Date getDataImportacao() {
		return dataImportacao;
	}

	public void setDataImportacao(Date dataImportacao) {
		this.dataImportacao = dataImportacao;
	}
	
	public String getDataApresentar() {
		if (dataImportacao == null) {
			return "";
		}
		return (Uteis.getData(dataImportacao));
    }

	public Boolean getImportado() {
		if (importado == null) {
			importado = false;
		}
		return importado;
	}

	public void setImportado(Boolean importado) {
		this.importado = importado;
	}

	public List<IntegracaoGinfesCursoItemVO> getCursos() {
		if (cursos == null) {
			cursos = new ArrayList<IntegracaoGinfesCursoItemVO>();
		}
		return cursos;
	}

	public void setCursos(List<IntegracaoGinfesCursoItemVO> cursos) {
		this.cursos = cursos;
	}

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}
	
}
