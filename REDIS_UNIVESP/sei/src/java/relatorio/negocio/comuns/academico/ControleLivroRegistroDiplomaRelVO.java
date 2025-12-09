package relatorio.negocio.comuns.academico;

import negocio.comuns.academico.AutorizacaoCursoVO;
import negocio.comuns.academico.ControleLivroFolhaReciboVO;
import negocio.comuns.academico.ExpedicaoDiplomaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FuncionarioVO;

public class ControleLivroRegistroDiplomaRelVO {

	private MatriculaVO matricula;
	private ExpedicaoDiplomaVO expedicaoDiploma;
	private String tituloFuncionario;
	private FuncionarioVO funcionario;
	private CargoVO cargoFuncionario;
	private String semestreAnoConclusao;
	private FuncionarioVO funcionario2;
	private String tituloFuncionario2;
	private CargoVO cargoFuncionario2;
	private String subTitulo;
	private Integer nrLivro;
	private ControleLivroFolhaReciboVO controleLivroFolhaRecibo;
	private String via;
	private String reconhecimento;
	private AutorizacaoCursoVO autorizacaoCurso;
    private AutorizacaoCursoVO renovacaoReconhecimentoVO;
    private String titulacaoCurso;

	public FuncionarioVO getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(FuncionarioVO funcionario) {
		this.funcionario = funcionario;
	}

	public ControleLivroRegistroDiplomaRelVO() {
	}

	public MatriculaVO getMatricula() {
		return matricula;
	}

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}

	public ExpedicaoDiplomaVO getExpedicaoDiploma() {
		return expedicaoDiploma;
	}

	public void setExpedicaoDiploma(ExpedicaoDiplomaVO expedicaoDiploma) {
		this.expedicaoDiploma = expedicaoDiploma;
	}

	public CargoVO getCargoFuncionario() {
		return cargoFuncionario;
	}

	public void setCargoFuncionario(CargoVO cargoFuncionario) {
		this.cargoFuncionario = cargoFuncionario;
	}

	/**
	 * @return the subTitulo
	 */
	public String getSubTitulo() {
		if (subTitulo == null) {
			subTitulo = "REGISTRO DE DIPLOMA";
		}
		return subTitulo;
	}

	/**
	 * @param subTitulo
	 *            the subTitulo to set
	 */
	public void setSubTitulo(String subTitulo) {
		this.subTitulo = subTitulo;
	}

	/**
	 * @return the funcionario2
	 */
	public FuncionarioVO getFuncionario2() {
		if (funcionario2 == null) {
			funcionario2 = new FuncionarioVO();
		}
		return funcionario2;
	}

	/**
	 * @param funcionario2
	 *            the funcionario2 to set
	 */
	public void setFuncionario2(FuncionarioVO funcionario2) {
		this.funcionario2 = funcionario2;
	}

	/**
	 * @return the cargoFuncionario2
	 */
	public CargoVO getCargoFuncionario2() {
		if (cargoFuncionario2 == null) {
			cargoFuncionario2 = new CargoVO();
		}
		return cargoFuncionario2;
	}

	/**
	 * @param cargoFuncionario2
	 *            the cargoFuncionario2 to set
	 */
	public void setCargoFuncionario2(CargoVO cargoFuncionario2) {
		this.cargoFuncionario2 = cargoFuncionario2;
	}

	/**
	 * @return the semestreAnoConclusao
	 */
	public String getSemestreAnoConclusao() {
		if (semestreAnoConclusao == null) {
			if (getMatricula().getMatricula().equals("")) {
				semestreAnoConclusao = "";
			} else {
				if (getMatricula().getCurso().getSemestral()) {
					if ((!getMatricula().getSemestreConclusao().equals(""))
							&& (!getMatricula().getAnoConclusao().equals(""))) {
						semestreAnoConclusao = getMatricula().getSemestreConclusao() + "º Semestre de "
								+ getMatricula().getAnoConclusao();
					} else {
						semestreAnoConclusao = "";
					}
				} else {
					semestreAnoConclusao = getMatricula().getAnoConclusao();
				}
			}
		}
		return semestreAnoConclusao;
	}

	/**
	 * @param semestreAnoConclusao
	 *            the semestreAnoConclusao to set
	 */
	public void setSemestreAnoConclusao(String semestreAnoConclusao) {
		this.semestreAnoConclusao = semestreAnoConclusao;
	}

	/**
	 * @return the controleLivroFolhaRecibo
	 */
	public ControleLivroFolhaReciboVO getControleLivroFolhaRecibo() {
		if (controleLivroFolhaRecibo == null) {
			controleLivroFolhaRecibo = new ControleLivroFolhaReciboVO();
		}
		return controleLivroFolhaRecibo;
	}

	/**
	 * @param controleLivroFolhaRecibo
	 *            the controleLivroFolhaRecibo to set
	 */
	public void setControleLivroFolhaRecibo(ControleLivroFolhaReciboVO controleLivroFolhaRecibo) {
		this.controleLivroFolhaRecibo = controleLivroFolhaRecibo;
	}

	/**
	 * @return the nrLivro
	 */
	public Integer getNrLivro() {
		if (nrLivro == null) {
			nrLivro = 0;
		}
		return nrLivro;
	}

	/**
	 * @param nrLivro
	 *            the nrLivro to set
	 */
	public void setNrLivro(Integer nrLivro) {
		this.nrLivro = nrLivro;
	}

	public String getVia() {
		if (via == null) {
			via = "";
		}
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public String getReconhecimento() {
		if (reconhecimento == null) {
			reconhecimento = "";
		}
		return reconhecimento;
	}

	public void setReconhecimento(String reconhecimento) {
		this.reconhecimento = reconhecimento;
	}

	public String getTituloFuncionario() {
		if (tituloFuncionario == null) {
			tituloFuncionario = "";
		}
		return tituloFuncionario;
	}

	public void setTituloFuncionario(String tituloFuncionario) {
		this.tituloFuncionario = tituloFuncionario;
	}

	public String getTituloFuncionario2() {
		if (tituloFuncionario2 == null) {
			tituloFuncionario = "";
		}
		return tituloFuncionario2;
	}

	public void setTituloFuncionario2(String tituloFuncionario2) {
		this.tituloFuncionario2 = tituloFuncionario2;
	}
	
	public AutorizacaoCursoVO getAutorizacaoCurso() {
		if (autorizacaoCurso == null) {
			autorizacaoCurso = new AutorizacaoCursoVO();
		}
		return autorizacaoCurso;
	}

	public void setAutorizacaoCurso(AutorizacaoCursoVO autorizacaoCurso) {
		this.autorizacaoCurso = autorizacaoCurso;
	}

	public AutorizacaoCursoVO getRenovacaoReconhecimentoVO() {
		if (renovacaoReconhecimentoVO == null) {
			renovacaoReconhecimentoVO = new AutorizacaoCursoVO();
		}
		return renovacaoReconhecimentoVO;
	}

	public void setRenovacaoReconhecimentoVO(AutorizacaoCursoVO renovacaoReconhecimentoVO) {
		this.renovacaoReconhecimentoVO = renovacaoReconhecimentoVO;
	}

	public String getTitulacaoCurso() {
		if (titulacaoCurso == null) {
			titulacaoCurso = "";
		}
		return titulacaoCurso;
	}

	public void setTitulacaoCurso(String titulacaoCurso) {
		this.titulacaoCurso = titulacaoCurso;
	}	
	
	
}
