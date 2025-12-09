package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.AutorizacaoCursoVO;
import negocio.comuns.academico.ControleLivroFolhaReciboVO;
import negocio.comuns.academico.ControleLivroRegistroDiplomaVO;
import negocio.comuns.academico.ExpedicaoDiplomaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.negocio.comuns.academico.ControleLivroRegistroDiplomaRelVO;
import relatorio.negocio.interfaces.academico.ControleLivroRegistroDiplomaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class ControleLivroRegistroDiplomaRel extends SuperRelatorio implements ControleLivroRegistroDiplomaRelInterfaceFacade {
	
	private static final long serialVersionUID = 1L;

	private ControleLivroRegistroDiplomaRelVO controleLivroRegistroDiplomaRelVo;
	
	private FuncionarioVO funcionario;
	private CargoVO cargoFuncionario;
	private String tituloFuncionario;
	private FuncionarioVO funcionario2;
	private CargoVO cargoFuncionario2;
	private String tituloFuncionario2;

	public ControleLivroRegistroDiplomaRel() {
		inicializarParametros();
	}

	public void inicializarParametros() {
		setControleLivroRegistroDiplomaRelVO(new ControleLivroRegistroDiplomaRelVO());
	}

	public List<ControleLivroRegistroDiplomaRelVO> criarObjeto(ControleLivroRegistroDiplomaVO controleLivroRegistroDiplomaVO, boolean consultarCargo1, boolean consultarCargo2, String reconhecimentoUtilizar, UsuarioVO usuario, String tipoLayout) throws Exception {
		List<ControleLivroRegistroDiplomaRelVO> listaRelatorio = new ArrayList<ControleLivroRegistroDiplomaRelVO>(0);
		List<ControleLivroFolhaReciboVO> lista = getFacadeFactory().getControleLivroFolhaReciboFacade().consultarDadosControleLivroFolhaRecibo(controleLivroRegistroDiplomaVO, false, usuario);
		if (lista.isEmpty()) {
			throw new Exception("Não há alunos neste Livro Registro Diploma.");
		}
		else {
			if (consultarCargo1 && Uteis.isAtributoPreenchido(getCargoFuncionario())) {
        		setCargoFuncionario(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(getCargoFuncionario().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
        	}
        	if (consultarCargo2 && Uteis.isAtributoPreenchido(getCargoFuncionario2())) {
        		setCargoFuncionario2(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(getCargoFuncionario2().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
        	}
	        Iterator<ControleLivroFolhaReciboVO> i = lista.iterator();
	        while (i.hasNext()) {
	        	ControleLivroFolhaReciboVO obj = (ControleLivroFolhaReciboVO)i.next();
	        	ControleLivroRegistroDiplomaRelVO controleLivroRegistroDiplomaRelVO = new ControleLivroRegistroDiplomaRelVO();
	        	getFacadeFactory().getMatriculaFacade().consultarMatriculaParaGeracaoRelatorioLivroRegistro(obj, controleLivroRegistroDiplomaRelVO, usuario);
//	        	montarDadosRenovacaoEReconhecimentoCurso(usuario, obj);
	        	if (!Uteis.isAtributoPreenchido(controleLivroRegistroDiplomaRelVO.getMatricula())) {
	        		controleLivroRegistroDiplomaRelVO.setMatricula(obj.getMatricula());
	        		if (!Uteis.isAtributoPreenchido(controleLivroRegistroDiplomaRelVO.getMatricula().getUnidadeEnsino())) {
	        			controleLivroRegistroDiplomaRelVO.getMatricula().getUnidadeEnsino().setMantenedora(obj.getMatricula().getUnidadeEnsino().getMantenedora());
	        		}
	        	}
	        	if (!Uteis.isAtributoPreenchido(controleLivroRegistroDiplomaRelVO.getExpedicaoDiploma())) {
	        		controleLivroRegistroDiplomaRelVO.setExpedicaoDiploma(obj.getExpedicaoDiplomaVO());
	        	}
	        	controleLivroRegistroDiplomaRelVO.setVia(obj.getVia());
	        	if (Uteis.isAtributoPreenchido(reconhecimentoUtilizar)) {
	        		controleLivroRegistroDiplomaRelVO.setReconhecimento(reconhecimentoUtilizar.equals("RenovacaoReconhecimentoCurso") && Uteis.isAtributoPreenchido(controleLivroRegistroDiplomaRelVO.getMatricula().getRenovacaoReconhecimentoVO()) ? controleLivroRegistroDiplomaRelVO.getMatricula().getRenovacaoReconhecimentoVO().getNome() : controleLivroRegistroDiplomaRelVO.getMatricula().getAutorizacaoCurso().getNome());
	        		controleLivroRegistroDiplomaRelVO.setReconhecimento(controleLivroRegistroDiplomaRelVO.getReconhecimento().replace("\n", " "));
	        		if (controleLivroRegistroDiplomaRelVO.getReconhecimento().length() > 330) {
	        			controleLivroRegistroDiplomaRelVO.setReconhecimento(controleLivroRegistroDiplomaRelVO.getReconhecimento().substring(0, 330).concat("..."));
	        		}
	        	} else {
	        		controleLivroRegistroDiplomaRelVO.setReconhecimento(controleLivroRegistroDiplomaRelVO.getMatricula().getRenovacaoReconhecimentoVO().getNome());
	        	}
	        	if (!obj.getVia().equals("1") && !Uteis.isAtributoPreenchido(controleLivroRegistroDiplomaRelVO.getExpedicaoDiploma())) {
	        		ExpedicaoDiplomaVO exp = getFacadeFactory().getExpedicaoDiplomaFacade().consultarPorMatriculaPrimeiraVia(obj.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		        	if (Uteis.isAtributoPreenchido(exp)) {
		        		obj.setSituacao("Emitido");
		        		if (controleLivroRegistroDiplomaRelVO.getExpedicaoDiploma().getNumeroRegistroDiplomaViaAnterior().equals("") && Uteis.isAtributoPreenchido(controleLivroRegistroDiplomaRelVO.getExpedicaoDiploma().getMatricula())) {
							controleLivroRegistroDiplomaRelVO.getExpedicaoDiploma().setNumeroRegistroDiplomaViaAnterior(getFacadeFactory().getControleLivroRegistroDiplomaFacade().obterNumeroRegistroMatriculaPrimeiraVia(controleLivroRegistroDiplomaRelVO.getExpedicaoDiploma().getMatricula().getMatricula()));
							controleLivroRegistroDiplomaRelVO.getExpedicaoDiploma().setNumeroProcesso(exp.getNumeroProcesso());
						}
		        		if (controleLivroRegistroDiplomaRelVO.getExpedicaoDiploma().getNumeroProcesso().equals("")) {
							controleLivroRegistroDiplomaRelVO.getExpedicaoDiploma().setNumeroProcesso(exp.getNumeroProcesso());
						}
		        		if (controleLivroRegistroDiplomaRelVO.getExpedicaoDiploma().getNumeroProcessoViaAnterior().equals("") && Uteis.isAtributoPreenchido(exp.getNumeroProcesso())) {
							controleLivroRegistroDiplomaRelVO.getExpedicaoDiploma().setNumeroProcessoViaAnterior(exp.getNumeroProcesso());
						}
		        		if (controleLivroRegistroDiplomaRelVO.getVia().equals("")) {
							controleLivroRegistroDiplomaRelVO.setVia(exp.getVia());
						}
		        		if (!Uteis.isAtributoPreenchido(exp.getDataExpedicao())) {
		        			controleLivroRegistroDiplomaRelVO.getExpedicaoDiploma().setDataExpedicao(exp.getDataExpedicao());
		        		}
						controleLivroRegistroDiplomaRelVO.getExpedicaoDiploma().setDataRegistroDiplomaViaAnterior(exp.getDataExpedicao());
						if (!exp.getFuncionarioSecundarioVO().getPessoa().getNome().equals("")) {
							controleLivroRegistroDiplomaRelVO.getExpedicaoDiploma().setReitorRegistroDiplomaViaAnterior(exp.getFuncionarioSecundarioVO());
							controleLivroRegistroDiplomaRelVO.getExpedicaoDiploma().getReitorRegistroDiplomaViaAnterior().getPessoa().setNome(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(controleLivroRegistroDiplomaRelVO.getExpedicaoDiploma().getReitorRegistroDiplomaViaAnterior().getPessoa().getNome().toLowerCase()));
							if (exp.getCargoFuncionarioSecundarioVO().getCodigo().intValue() > 0) {
								exp.setCargoFuncionarioSecundarioVO(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(exp.getCargoFuncionarioSecundarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
								controleLivroRegistroDiplomaRelVO.getExpedicaoDiploma().getCargoReitorRegistroDiplomaViaAnterior().setNome(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(exp.getCargoFuncionarioSecundarioVO().getNome().toLowerCase()));
							}							
						}
						if (!exp.getFuncionarioPrimarioVO().getPessoa().getNome().equals("")) {
							controleLivroRegistroDiplomaRelVO.getExpedicaoDiploma().setSecretariaRegistroDiplomaViaAnterior(exp.getFuncionarioPrimarioVO());
							controleLivroRegistroDiplomaRelVO.getExpedicaoDiploma().getSecretariaRegistroDiplomaViaAnterior().getPessoa().setNome(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(controleLivroRegistroDiplomaRelVO.getExpedicaoDiploma().getSecretariaRegistroDiplomaViaAnterior().getPessoa().getNome().toLowerCase()));
							if (exp.getCargoFuncionarioPrincipalVO().getCodigo().intValue() > 0) {
								exp.setCargoFuncionarioPrincipalVO(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(exp.getCargoFuncionarioPrincipalVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
								controleLivroRegistroDiplomaRelVO.getExpedicaoDiploma().getCargoSecretariaRegistroDiplomaViaAnterior().setNome(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(exp.getCargoFuncionarioPrincipalVO().getNome().toLowerCase()));
							}	
						}					
		        	}
				}
	        	controleLivroRegistroDiplomaRelVO.setCargoFuncionario(getCargoFuncionario());
	        	controleLivroRegistroDiplomaRelVO.setFuncionario(getFuncionario());
	        	controleLivroRegistroDiplomaRelVO.setTituloFuncionario(getTituloFuncionario());
	        	controleLivroRegistroDiplomaRelVO.setCargoFuncionario2(getCargoFuncionario2());
	        	controleLivroRegistroDiplomaRelVO.setFuncionario2(getFuncionario2());
	        	controleLivroRegistroDiplomaRelVO.setTituloFuncionario2(getTituloFuncionario2());
                controleLivroRegistroDiplomaRelVO.setControleLivroFolhaRecibo(obj);
                controleLivroRegistroDiplomaRelVO.setNrLivro(controleLivroRegistroDiplomaVO.getNrLivro());
                if (controleLivroRegistroDiplomaRelVO.getMatricula().getAluno().getSexo().equals("F")) {
                	controleLivroRegistroDiplomaRelVO.setTitulacaoCurso(controleLivroRegistroDiplomaRelVO.getMatricula().getCurso().getTitulacaoDoFormandoFeminino());
                } else {
                	controleLivroRegistroDiplomaRelVO.setTitulacaoCurso(controleLivroRegistroDiplomaRelVO.getMatricula().getCurso().getTitulacaoDoFormando());
                }
                //controleLivroRegistroDiplomaRelVO.getControleLivroFolhaRecibo().setNumeroRegistro(controleLivroRegistroDiplomaVO.getNumeroRegistro());
	    		listaRelatorio.add(controleLivroRegistroDiplomaRelVO);
	        }
        }
		return listaRelatorio;
	}
	
	private void montarDadosRenovacaoEReconhecimentoCurso(UsuarioVO usuario, ControleLivroFolhaReciboVO obj)
			throws Exception {
		Map<String, List<? extends SuperVO>> resultado = getFacadeFactory().getMatriculaFacade().consultarDadosAlteracaoCadastral(obj.getMatricula().getMatricula(), usuario.getUnidadeEnsinoLogado().getCodigo(), 0, 0, "", "", obj.getMatricula().getColacaoGrauVO(), obj.getMatricula().getProgramacaoFormaturaAlunoVO(), usuario);
		List<MatriculaVO> listaMatricula = (List<MatriculaVO>) resultado.get("MATRICULA");
		if (Uteis.isAtributoPreenchido(listaMatricula)) {
			MatriculaVO matricula = listaMatricula.get(0);
			if (Uteis.isAtributoPreenchido(matricula.getAutorizacaoCurso())) {
				obj.getMatricula().setAutorizacaoCurso(getFacadeFactory().getAutorizacaoCursoFacade().consultarPorChavePrimaria(matricula.getAutorizacaoCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS));
			} else {
				obj.getMatricula().setAutorizacaoCurso(getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoDataMaisAntigo(
						obj.getMatricula().getCurso().getCodigo(), new Date(), Uteis.NIVELMONTARDADOS_COMBOBOX));
			}
			
			Date dataPRM = Uteis.isAtributoPreenchido(obj.getMatricula().getDataConclusaoCurso()) ? obj.getMatricula().getDataConclusaoCurso() : obj.getMatricula().getData();

			if (Uteis.isAtributoPreenchido(matricula.getRenovacaoReconhecimentoVO().getCodigo())) {
				obj.getMatricula().setRenovacaoReconhecimentoVO(getFacadeFactory().getAutorizacaoCursoFacade().consultarPorChavePrimaria(matricula.getRenovacaoReconhecimentoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS));
			} else {
				obj.getMatricula().setRenovacaoReconhecimentoVO(getFacadeFactory().getAutorizacaoCursoFacade()
						.consultarPorCursoDataVigenteMatricula(obj.getMatricula().getCurso().getCodigo(), dataPRM,
								Uteis.NIVELMONTARDADOS_COMBOBOX));
				if(obj.getMatricula().getAutorizacaoCurso().getCodigo().equals(obj.getMatricula().getRenovacaoReconhecimentoVO().getCodigo())) {
					obj.getMatricula().setRenovacaoReconhecimentoVO(new AutorizacaoCursoVO());
				}
			}

		}
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}
	
	public static String getDesignIReportRelatorio2() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + "2.jrxml");
	}
	
	public static String getDesignIReportRelatorio3() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + "3.jrxml");
	}
	
	public static String getDesignIReportRelatorio4() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "ControleLivroRegistroDiplomaRelLista" + ".jrxml");
	}
	
	public static String getDesignIReportRelatorio5() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + "5.jrxml");
	}

	public static String getDesignIReportRelatorio5Excel() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + "5.jrxml");
	}
	
	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("ControleLivroRegistroDiplomaRel");
	}

	public ControleLivroRegistroDiplomaRelVO getControleLivroRegistroDiplomaRelVO() {
		if (controleLivroRegistroDiplomaRelVo == null){
			controleLivroRegistroDiplomaRelVo = new ControleLivroRegistroDiplomaRelVO();
		}
		return null;
	}

	public void setControleLivroRegistroDiplomaRelVO(ControleLivroRegistroDiplomaRelVO controleLivroRegistroDiplomaRelVO) {
		this.controleLivroRegistroDiplomaRelVo = controleLivroRegistroDiplomaRelVO;
	}

	public FuncionarioVO getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(FuncionarioVO funcionario) {
		this.funcionario = funcionario;
	}

	public CargoVO getCargoFuncionario() {
		return cargoFuncionario;
	}

	public void setCargoFuncionario(CargoVO cargoFuncionario) {
		this.cargoFuncionario = cargoFuncionario;
	}

    /**
     * @return the funcionario2
     */
    public FuncionarioVO getFuncionario2() {
        return funcionario2;
    }

    /**
     * @param funcionario2 the funcionario2 to set
     */
    public void setFuncionario2(FuncionarioVO funcionario2) {
        this.funcionario2 = funcionario2;
    }

    /**
     * @return the cargoFuncionario2
     */
    public CargoVO getCargoFuncionario2() {
        return cargoFuncionario2;
    }

    /**
     * @param cargoFuncionario2 the cargoFuncionario2 to set
     */
    public void setCargoFuncionario2(CargoVO cargoFuncionario2) {
        this.cargoFuncionario2 = cargoFuncionario2;
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
			tituloFuncionario2 = "";
		}
		return tituloFuncionario2;
	}

	public void setTituloFuncionario2(String tituloFuncionario2) {
		this.tituloFuncionario2 = tituloFuncionario2;
	}

}
