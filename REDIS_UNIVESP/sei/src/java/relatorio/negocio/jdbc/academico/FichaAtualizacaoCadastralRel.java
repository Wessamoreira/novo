package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.negocio.comuns.academico.FichaAlunoRelVO;
import relatorio.negocio.interfaces.academico.FichaAtualizacaoCadastralRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
public class FichaAtualizacaoCadastralRel extends SuperRelatorio implements FichaAtualizacaoCadastralRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public void validarDados(MatriculaVO matriculaVO, TurmaVO turmaVO, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, String filtro) throws Exception {
		if (filtro.equals("")) {
			throw new Exception("O campo FILTRO deve ser informado");
		}
		if (filtro.equals("aluno") && matriculaVO.getMatricula().equals("")) {
			throw new Exception("O campo ALUNO deve ser informado");
		}
		// if(filtro.equals("outros") && unidadeEnsinoCursoVO.getCodigo() == 0){
		// throw new Exception("O campo CURSO deve ser informado");
		// }
		if (filtro.equals("outros") && turmaVO.getCodigo() == 0) {
			throw new Exception("O campo TURMA deve ser informado");
		}
	}

	public List<FichaAlunoRelVO> criarObjeto(MatriculaVO matriculaVO, TurmaVO turmaVO, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, String filtro, String anoConsulta , String semestreConsulta) throws Exception {
		validarDados(matriculaVO, turmaVO, unidadeEnsinoCursoVO, filtro);
		List<FichaAlunoRelVO> listaRelatorio = new ArrayList<FichaAlunoRelVO>(0);
		if (matriculaVO.getMatricula().equals("")) {
			List<MatriculaVO> listaMatriculaAlunos = consultarAlunosDoCurso(unidadeEnsinoCursoVO.getCurso(), turmaVO, configuracaoFinanceiroVO, usuarioVO ,anoConsulta,semestreConsulta);
			for (MatriculaVO matriculaAluno : listaMatriculaAlunos) {
				consultarDadosAcademicosPorMatricula(matriculaAluno, usuarioVO);
				consultarDadosPessoaisDoAluno(matriculaAluno, usuarioVO);
				listaRelatorio.add(montarDadosFichaAlunoRel(matriculaAluno, usuarioVO));
			}
		} else {
			consultarDadosAcademicosPorMatricula(matriculaVO, usuarioVO);
			consultarDadosPessoaisDoAluno(matriculaVO, usuarioVO);
			listaRelatorio.add(montarDadosFichaAlunoRel(matriculaVO, usuarioVO));
		}
		return listaRelatorio;
	}

	@SuppressWarnings("unchecked")
	public List<MatriculaVO> consultarAlunosDoCurso(CursoVO curso, TurmaVO turma, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO ,String anoConsulta, String semestreConsulta) throws Exception {
		List<MatriculaVO> lista = new ArrayList<MatriculaVO>(0);
		if (turma.getIntegral()) {
			lista = getFacadeFactory().getMatriculaFacade().consultarPorCursoTurmaAnoSemestreSituacao(curso.getCodigo(), turma.getCodigo(), "", "", SituacaoVinculoMatricula.ATIVA.getValor(), "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, configuracaoFinanceiroVO, usuarioVO);
		} else if (turma.getSemestral()) {
			lista = getFacadeFactory().getMatriculaFacade().consultarPorCursoTurmaAnoSemestreSituacao(curso.getCodigo(), turma.getCodigo(), anoConsulta, semestreConsulta, SituacaoVinculoMatricula.ATIVA.getValor(), "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, configuracaoFinanceiroVO, usuarioVO);
		} else if (turma.getAnual()) {
			lista = getFacadeFactory().getMatriculaFacade().consultarPorCursoTurmaAnoSemestreSituacao(curso.getCodigo(), turma.getCodigo(), anoConsulta, "", SituacaoVinculoMatricula.ATIVA.getValor(), "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, configuracaoFinanceiroVO, usuarioVO);
		}
		if (lista.isEmpty()) {
			throw new Exception("Não há alunos matriculados nas condições propostas acima.");
		}
		return lista;
	}

	private void consultarDadosAcademicosPorMatricula(MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getMatriculaFacade().carregarDados(matriculaVO, NivelMontarDados.BASICO, usuarioVO);
		matriculaVO.getUnidadeEnsino().setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(matriculaVO.getUnidadeEnsino().getCidade().getCodigo(), false, usuarioVO));
	}

	private void consultarDadosPessoaisDoAluno(MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception {
		matriculaVO.setAluno(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(matriculaVO.getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
	}

	private String consultarTurmaPorMatricula(MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception {
		MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
		return matriculaPeriodoVO.getTurma().getIdentificadorTurma();
	}

	private void montarNomePaiMae(FichaAlunoRelVO fichaAlunoRelVO, MatriculaVO matriculaVO) {
		fichaAlunoRelVO.setNomePai("");
		fichaAlunoRelVO.setNomeMae("");
		if (!matriculaVO.getAluno().getFiliacaoVOs().isEmpty()) {
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

	private FichaAlunoRelVO montarDadosFichaAlunoRel(MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception {
		FichaAlunoRelVO fichaAlunoRelVO = new FichaAlunoRelVO();
		fichaAlunoRelVO.setCurso(matriculaVO.getCurso().getNome());
		fichaAlunoRelVO.setTurma(consultarTurmaPorMatricula(matriculaVO, usuarioVO));
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
		fichaAlunoRelVO.setCelular(matriculaVO.getAluno().getCelular());
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
		montarNomePaiMae(fichaAlunoRelVO, matriculaVO);
		if (usuarioVO.getUnidadeEnsinoLogado().getCidade().getNome().equals("")) {
			fichaAlunoRelVO.setLocalData(matriculaVO.getUnidadeEnsino().getCidade().getNome() + ", " + Uteis.getDataAplicandoFormatacao(new Date(), "dd/MM/yyyy"));
		} else {
			fichaAlunoRelVO.setLocalData(usuarioVO.getUnidadeEnsinoLogado().getCidade().getNome() + ", " + Uteis.getDataAplicandoFormatacao(new Date(), "dd/MM/yyyy"));
		}
		fichaAlunoRelVO.setDataEmissao(Uteis.getData(new Date(), "dd/MM/yyyy HH:mm:ss"));
		fichaAlunoRelVO.setComplementoEndereco(matriculaVO.getAluno().getComplemento());
		return fichaAlunoRelVO;
	}

	public static String getDesignIReportRelatorio() {
		return getCaminhoBaseIReportRelatorio() + File.separator + getIdEntidade() + ".jrxml";
	}

	public static String getCaminhoBaseIReportRelatorio() {
		return "relatorio" + File.separator + "designRelatorio" + File.separator + "academico";
	}

	public static String getIdEntidade() {
		return "FichaAtualizacaoCadastralRel";
	}
}