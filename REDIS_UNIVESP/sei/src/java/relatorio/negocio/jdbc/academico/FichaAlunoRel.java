package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import relatorio.negocio.comuns.academico.FichaAlunoRelVO;

import relatorio.negocio.interfaces.academico.FichaAlunoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class FichaAlunoRel extends SuperRelatorio implements FichaAlunoRelInterfaceFacade {

    private FichaAlunoRelVO fichaAlunoRelVO;
    private MatriculaVO matriculaVO;

    public FichaAlunoRel() {
    }

    public List<FichaAlunoRelVO> criarObjeto(String matricula,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        List<FichaAlunoRelVO> listaRelatorio = new ArrayList<FichaAlunoRelVO>(0);
        setMatriculaVO(new MatriculaVO());
        getMatriculaVO().setMatricula(matricula);
        consultarDadosAcademicosPorMatricula(getMatriculaVO(), usuarioVO);
        consultarDadosPessoaisDoAluno(getMatriculaVO(), usuarioVO);
        setFichaAlunoRelVO(montarDadosFichaAlunoRel(getMatriculaVO(), configuracaoFinanceiroVO, usuarioVO));
        listaRelatorio.add(getFichaAlunoRelVO());
        return listaRelatorio;
    }

    private void consultarDadosAcademicosPorMatricula(MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception {
        getFacadeFactory().getMatriculaFacade().carregarDados(matriculaVO, NivelMontarDados.TODOS, usuarioVO);
        matriculaVO.getUnidadeEnsino().setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(matriculaVO.getUnidadeEnsino().getCidade().getCodigo(), false, usuarioVO));
    }

    private void consultarDadosPessoaisDoAluno(MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception {
        getFacadeFactory().getPessoaFacade().carregarDados(matriculaVO.getAluno(), NivelMontarDados.TODOS, usuarioVO);
    }

    private String consultarTurmaPorMatricula(MatriculaVO matriculaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
    	MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultaUltimaMatriculaPeriodoPorMatriculaConsultaBasica(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, configuracaoFinanceiroVO, usuarioVO);
        if (Uteis.isAtributoPreenchido(matriculaPeriodoVO)) {
        	return matriculaPeriodoVO.getTurma().getIdentificadorTurma();
        }
        return "";
    }

	private void montarResponsavelFinanceiro(FichaAlunoRelVO fichaAlunoRelVO, PessoaVO aluno) {
		fichaAlunoRelVO.setNomeResponsavelFinanceiro("");
		fichaAlunoRelVO.setCpfResponsavelFinanceiro("");
		if (!aluno.getFiliacaoVOs().isEmpty()) {
			for (FiliacaoVO filiacaoVO : matriculaVO.getAluno().getFiliacaoVOs()) {
				if (filiacaoVO.getResponsavelFinanceiro()) {
					fichaAlunoRelVO.setNomeResponsavelFinanceiro(filiacaoVO.getNome());
					fichaAlunoRelVO.setCpfResponsavelFinanceiro(filiacaoVO.getCPF());
					break;
				}
			}
		}
	}
    
    private void montarNomePaiMae(FichaAlunoRelVO fichaAlunoRelVO, PessoaVO aluno) {
        fichaAlunoRelVO.setNomePai("");
        fichaAlunoRelVO.setNomeMae("");
        if (!aluno.getFiliacaoVOs().isEmpty()) {
            for (FiliacaoVO filiacaoVO : matriculaVO.getAluno().getFiliacaoVOs()) {
                if (filiacaoVO.getTipo().equals("PA")) {
                    fichaAlunoRelVO.setNomePai(filiacaoVO.getNome());
                }
                if (filiacaoVO.getTipo().equals("MA")) {
                    fichaAlunoRelVO.setNomeMae(filiacaoVO.getNome());
                }
            }
        }
    }

    private void montarDadosEnsinoMedio(FichaAlunoRelVO fichaAlunoRelVO, PessoaVO aluno) throws Exception {
        if (aluno.getFormacaoAcademicaNivelMedio() != null) {
            fichaAlunoRelVO.setEstabelecimentoEnsinoMedio(aluno.getFormacaoAcademicaNivelMedio().getInstituicao());
			if (aluno.getFormacaoAcademicaNivelMedio().getDesenharDataFim()) {
				fichaAlunoRelVO.setAnoConclusaoEnsinoMedio(aluno.getFormacaoAcademicaNivelMedio().getAnoDataFim());
			} else {
				fichaAlunoRelVO.setAnoConclusaoEnsinoMedio(Uteis.getData(aluno.getFormacaoAcademicaNivelMedio().getDataFim(), "yyyy"));
			}
            fichaAlunoRelVO.setCidadeConclusaoEnsinoMedio(aluno.getFormacaoAcademicaNivelMedio().getCidade().getNome());
            if (aluno.getFormacaoAcademicaNivelMedio().getCidade().getCodigo() != null && !aluno.getFormacaoAcademicaNivelMedio().getCidade().getCodigo().equals(0)) {
            	fichaAlunoRelVO.setEstadoConclusaoEnsinoMedio(getFacadeFactory().getEstadoFacade().consultarPorCodigoCidade(aluno.getFormacaoAcademicaNivelMedio().getCidade().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null).getSigla());
            } else {
            	fichaAlunoRelVO.setEstadoConclusaoEnsinoMedio("");
            }
        } else {
            fichaAlunoRelVO.setEstabelecimentoEnsinoMedio("");
            fichaAlunoRelVO.setAnoConclusaoEnsinoMedio("");
            fichaAlunoRelVO.setCidadeConclusaoEnsinoMedio("");
            fichaAlunoRelVO.setEstadoConclusaoEnsinoMedio("");
        }
    }
    
    private void montarDadosEnsinoFundamental(FichaAlunoRelVO fichaAlunoRelVO, PessoaVO aluno) throws Exception {
        if (aluno.getFormacaoAcademicaNivelFundamental() != null) {
            fichaAlunoRelVO.setEstabelecimentoEnsinoFundamental(aluno.getFormacaoAcademicaNivelFundamental().getInstituicao());
			if (aluno.getFormacaoAcademicaNivelFundamental().getDesenharDataFim()) {
				fichaAlunoRelVO.setAnoConclusaoEnsinoFundamental(aluno.getFormacaoAcademicaNivelFundamental().getAnoDataFim());
			} else {
				fichaAlunoRelVO.setAnoConclusaoEnsinoFundamental(Uteis.getData(aluno.getFormacaoAcademicaNivelFundamental().getDataFim(), "yyyy"));
			}
            fichaAlunoRelVO.setCidadeConclusaoEnsinoFundamental(aluno.getFormacaoAcademicaNivelFundamental().getCidade().getNome());
            if (aluno.getFormacaoAcademicaNivelFundamental().getCidade().getCodigo() != null && !aluno.getFormacaoAcademicaNivelFundamental().getCidade().getCodigo().equals(0)) {
            	fichaAlunoRelVO.setEstadoConclusaoEnsinoFundamental(getFacadeFactory().getEstadoFacade().consultarPorCodigoCidade(aluno.getFormacaoAcademicaNivelFundamental().getCidade().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null).getSigla());
            } else {
            	fichaAlunoRelVO.setEstadoConclusaoEnsinoFundamental("");
            }
        } else {
            fichaAlunoRelVO.setEstabelecimentoEnsinoFundamental("");
            fichaAlunoRelVO.setAnoConclusaoEnsinoFundamental("");
            fichaAlunoRelVO.setCidadeConclusaoEnsinoFundamental("");
            fichaAlunoRelVO.setEstadoConclusaoEnsinoFundamental("");
        }
    }
    
    private void montarDadosGraduacao(FichaAlunoRelVO fichaAlunoRelVO, PessoaVO aluno) throws Exception {
        if (aluno.getFormacaoAcademicaNivelGraduacao() != null) {
            fichaAlunoRelVO.setEstabelecimentoGraduacao(aluno.getFormacaoAcademicaNivelGraduacao().getInstituicao());
			if (aluno.getFormacaoAcademicaNivelGraduacao().getDesenharDataFim()) {
				fichaAlunoRelVO.setAnoConclusaoGraduacao(aluno.getFormacaoAcademicaNivelGraduacao().getAnoDataFim());
			} else {
				fichaAlunoRelVO.setAnoConclusaoGraduacao(Uteis.getData(aluno.getFormacaoAcademicaNivelGraduacao().getDataFim(), "yyyy"));
			}
            fichaAlunoRelVO.setCidadeConclusaoGraduacao(aluno.getFormacaoAcademicaNivelGraduacao().getCidade().getNome());
            if (aluno.getFormacaoAcademicaNivelGraduacao().getCidade().getCodigo() != null && !aluno.getFormacaoAcademicaNivelGraduacao().getCidade().getCodigo().equals(0)) {
            	fichaAlunoRelVO.setEstadoConclusaoGraduacao(getFacadeFactory().getEstadoFacade().consultarPorCodigoCidade(aluno.getFormacaoAcademicaNivelGraduacao().getCidade().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null).getSigla());
            } else {
            	fichaAlunoRelVO.setEstadoConclusaoGraduacao("");
            }
        } else {
            fichaAlunoRelVO.setEstabelecimentoGraduacao("");
            fichaAlunoRelVO.setAnoConclusaoGraduacao("");
            fichaAlunoRelVO.setCidadeConclusaoGraduacao("");
            fichaAlunoRelVO.setEstadoConclusaoGraduacao("");
        }
    }

    private FichaAlunoRelVO montarDadosFichaAlunoRel(MatriculaVO matriculaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        FichaAlunoRelVO fichaAlunoRelVO = new FichaAlunoRelVO();
        fichaAlunoRelVO.setCurso(matriculaVO.getCurso().getNome());
        fichaAlunoRelVO.setNivelEducacionalCurso(matriculaVO.getCurso().getNivelEducacional());
        fichaAlunoRelVO.setTurma(consultarTurmaPorMatricula(matriculaVO, configuracaoFinanceiroVO, usuarioVO));
        fichaAlunoRelVO.setTurno(matriculaVO.getTurno().getNome());
        fichaAlunoRelVO.setUnidadeCurso(matriculaVO.getUnidadeEnsino().getNome());
        fichaAlunoRelVO.setMatricula(matriculaVO.getMatricula());
        fichaAlunoRelVO.setNome(matriculaVO.getAluno().getNome());
        fichaAlunoRelVO.setEndereco(matriculaVO.getAluno().getEndereco() + "  Nº:" + matriculaVO.getAluno().getNumero());
        fichaAlunoRelVO.setSetor(matriculaVO.getAluno().getSetor());
        fichaAlunoRelVO.setCep(matriculaVO.getAluno().getCEP());
        fichaAlunoRelVO.setCidadeEndereco(matriculaVO.getAluno().getCidade().getNome());
        fichaAlunoRelVO.setEmail(matriculaVO.getAluno().getEmail());
        fichaAlunoRelVO.setTelResidencial(matriculaVO.getAluno().getTelefoneRes());
        fichaAlunoRelVO.setTelComercial(matriculaVO.getAluno().getTelefoneComer());
        fichaAlunoRelVO.setNacionalidade(matriculaVO.getAluno().getNacionalidade().getNacionalidade());
        fichaAlunoRelVO.setNaturalidade(matriculaVO.getAluno().getNaturalidade().getNome());
        fichaAlunoRelVO.setNaturalidadeEstado(matriculaVO.getAluno().getNaturalidade().getEstado().getNome());
        fichaAlunoRelVO.setDataNascimento(Uteis.getDataAplicandoFormatacao(matriculaVO.getAluno().getDataNasc(), "dd/MM/yyyy"));
        fichaAlunoRelVO.setSexo(matriculaVO.getAluno().getSexo_Apresentar());
        fichaAlunoRelVO.setEstadoCivil(matriculaVO.getAluno().getEstadoCivil_Apresentar());
        fichaAlunoRelVO.setTituloEleitoral(matriculaVO.getAluno().getTituloEleitoral());
        fichaAlunoRelVO.setRg(matriculaVO.getAluno().getRG());
        fichaAlunoRelVO.setOrgaoEmissorRg(matriculaVO.getAluno().getOrgaoEmissor());
        fichaAlunoRelVO.setEstadoEmissorRg(matriculaVO.getAluno().getEstadoEmissaoRG());
        fichaAlunoRelVO.setCpf(matriculaVO.getAluno().getCPF());
        fichaAlunoRelVO.setCelular(matriculaVO.getAluno().getCelular());
        fichaAlunoRelVO.setComplementoEndereco(matriculaVO.getAluno().getComplemento());
        fichaAlunoRelVO.setEstadoEndereco(matriculaVO.getAluno().getCidade().getEstado().getSigla());
        fichaAlunoRelVO.setNumeroEndereco(matriculaVO.getAluno().getNumero());
        montarNomePaiMae(fichaAlunoRelVO, matriculaVO.getAluno());
        if(matriculaVO.getCurso().getNivelEducacional().equals("ME")) {
        	 montarDadosEnsinoFundamental(fichaAlunoRelVO, matriculaVO.getAluno());
        }
        else if(matriculaVO.getCurso().getNivelEducacional().equals("SU") || matriculaVO.getCurso().getNivelEducacional().equals("GT") || matriculaVO.getCurso().getNivelEducacional().equals("EX") || matriculaVO.getCurso().getNivelEducacional().equals("PR")) {
        	 montarDadosEnsinoMedio(fichaAlunoRelVO, matriculaVO.getAluno());
        }
        else if(matriculaVO.getCurso().getNivelEducacional().equals("PO") || matriculaVO.getCurso().getNivelEducacional().equals("MT")) {
        	 montarDadosGraduacao(fichaAlunoRelVO, matriculaVO.getAluno());
        }
       
        montarResponsavelFinanceiro(fichaAlunoRelVO, matriculaVO.getAluno());
        if (usuarioVO.getUnidadeEnsinoLogado().getCidade().getNome().equals("")) {
            fichaAlunoRelVO.setLocalData(matriculaVO.getUnidadeEnsino().getCidade().getNome() + ", " + Uteis.getDataAplicandoFormatacao(new Date(), "dd/MM/yyyy"));
        } else {
            fichaAlunoRelVO.setLocalData(usuarioVO.getUnidadeEnsinoLogado().getCidade().getNome() + ", " + Uteis.getDataAplicandoFormatacao(new Date(), "dd/MM/yyyy"));
        }
        fichaAlunoRelVO.setDataEmissao(Uteis.getData(new Date(), "dd/MM/yyyy HH:mm:ss"));
        return fichaAlunoRelVO;
    }

    public FichaAlunoRelVO getFichaAlunoRelVO() {
        if (fichaAlunoRelVO == null) {
            fichaAlunoRelVO = new FichaAlunoRelVO();
        }
        return fichaAlunoRelVO;
    }

    public void setFichaAlunoRelVO(FichaAlunoRelVO fichaAlunoRelVO) {
        this.fichaAlunoRelVO = fichaAlunoRelVO;
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

    public static String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
    }

    public static String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
    }

    public static String getIdEntidade() {
        return ("FichaAlunoRel");
    }
}
