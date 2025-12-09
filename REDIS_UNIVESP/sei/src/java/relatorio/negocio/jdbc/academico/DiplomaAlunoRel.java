package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.AutorizacaoCursoVO;
import negocio.comuns.academico.ColacaoGrauVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.ExpedicaoDiplomaVO;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrdemHistoricoDisciplina;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.negocio.comuns.academico.DiplomaAlunoHistoricoRelVO;
import relatorio.negocio.comuns.academico.DiplomaAlunoRelVO;
import relatorio.negocio.interfaces.academico.DiplomaAlunoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class DiplomaAlunoRel extends SuperRelatorio implements DiplomaAlunoRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public DiplomaAlunoRel() {
		
	}	

	public void validarDados(DiplomaAlunoRelVO obj) throws ConsistirException {
		if (obj.getMatricula().equals("")) {
			throw new ConsistirException("O Aluno deve ser informado para geração do diploma.");
		}
		if (obj.getNomePrimeiroFuncionario().equals("")) {
			throw new ConsistirException("O Funcionário da primeira assinatura deve ser informado para geração do diploma.");
		}
		if (obj.getCargoPrimeiroFuncionario().equals("")) {
			throw new ConsistirException("O Cargo do Funcionário da primeira assinatura deve ser informado para geração do diploma.");
		}
		if (obj.getNomeSegundoFuncionario().equals("")) {
			throw new ConsistirException("O Funcionário da segunda assinatura deve ser informado para geração do diploma.");
		}
		if (obj.getCargoSegundoFuncionario().equals("")) {
			throw new ConsistirException("O Cargo do Funcionário da segunda assinatura deve ser informado para geração do diploma.");
		}
		if (obj.getCredenciamentoPortaria().equals("") || obj.getDataPublicacaoDoEmpresa().equals("")) {
			throw new ConsistirException("A Unidade de Ensino deve possuir uma Portaria de Credenciamento cadastrada e sua data de publicação.");
		}
		if (obj.getDataNascimentoAluno().equals("")) {
			throw new ConsistirException("O aluno selecionado não possui data de nascimento cadastrada.");
		}
		if (obj.getNacionalidadeAluno().equals("")) {
			throw new ConsistirException("O aluno selecionado não possui nacionalidade cadastrada.");
		}
	}


	public String consultarCidadePorUnidadeEnsinoMatriz(UsuarioVO usuarioVO) throws Exception {
		CidadeVO cidadeVO = getFacadeFactory().getCidadeFacade().consultarPorUnidadeEnsinoMatriz(false, usuarioVO);
		if (cidadeVO != null && cidadeVO.getCodigo() != 0) {
			return cidadeVO.getNome();
		}
		return "";

	}

	public List<DiplomaAlunoRelVO> criarObjeto(Boolean utilizarUnidadeMatriz, ExpedicaoDiplomaVO expDiplomaVO, FuncionarioVO funcionarioPrincipalVO, FuncionarioVO funcionarioSecundarioVO, FuncionarioVO funcionarioTerceiroVO, CargoVO cargoFuncPrincipalVO, CargoVO cargoFuncSecundarioVO, CargoVO cargoFuncTerceiroVO, String tituloFuncionarioPrincipal, String tituloFuncionarioSecundario, UsuarioVO usuario, String tipoLayout, CargoVO cargoFuncQuartoVO, CargoVO cargoFuncQuintoVO, FuncionarioVO funcionarioQuartoVO, FuncionarioVO funcionarioQuintoVO, Boolean xmlDiploma) throws Exception {

		UnidadeEnsinoVO credenciadora = new UnidadeEnsinoVO();
		DiplomaAlunoRelVO diplomaAlunoRelVO = new DiplomaAlunoRelVO();
		diplomaAlunoRelVO.setMatricula(expDiplomaVO.getMatricula().getMatricula());
		UnidadeEnsinoVO cod = getFacadeFactory().getUnidadeEnsinoFacade().consultarSeExisteUnidadeMatriz(true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);

		if (expDiplomaVO.getUnidadeEnsinoCertificadora().getCodigo() != null && expDiplomaVO.getUnidadeEnsinoCertificadora().getCodigo() != 0) {
			credenciadora = expDiplomaVO.getUnidadeEnsinoCertificadora();
			getFacadeFactory().getUnidadeEnsinoFacade().carregarDados(credenciadora, usuario);
			expDiplomaVO.setUnidadeEnsinoCertificadora(credenciadora);
		}

		if (cod.getCodigo() != 0) {
			cod = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(cod.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		}
		expDiplomaVO.getMatricula().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(expDiplomaVO.getMatricula().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));


		if(Uteis.isAtributoPreenchido(expDiplomaVO.getMatricula().getAluno().getNaturalidade())){
			expDiplomaVO.getMatricula().getAluno().getNaturalidade().setEstado(getFacadeFactory().getEstadoFacade().consultarPorCodigoCidade(expDiplomaVO.getMatricula().getAluno().getNaturalidade().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		cargoFuncPrincipalVO = (getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(cargoFuncPrincipalVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
		
		if (Uteis.isAtributoPreenchido(cargoFuncSecundarioVO.getCodigo())) {
			cargoFuncSecundarioVO = (getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(cargoFuncSecundarioVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
		}
		if(Uteis.isAtributoPreenchido(cargoFuncTerceiroVO)){
			cargoFuncTerceiroVO = getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(cargoFuncTerceiroVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		}
		if(Uteis.isAtributoPreenchido(cargoFuncQuartoVO)){
			cargoFuncQuartoVO = getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(cargoFuncQuartoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		}
		if(Uteis.isAtributoPreenchido(cargoFuncQuintoVO)){
			cargoFuncQuintoVO = getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(cargoFuncQuintoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		}
		List<DiplomaAlunoRelVO> lista = new ArrayList<DiplomaAlunoRelVO>(0);
		if (expDiplomaVO.getMatricula().getCurso().getNivelEducacional().equals("PO")) {
			diplomaAlunoRelVO.setDataColacaoGrau(Uteis.getData(expDiplomaVO.getMatricula().getDataConclusaoCurso(), "dd 'de' MMMM 'de' yyyy"));
		} else {
			ColacaoGrauVO colacaoGrauVO = null;
			if (expDiplomaVO.getMatricula().getDataColacaoGrau() != null) {
				diplomaAlunoRelVO.setDataColacaoGrau(Uteis.getData(expDiplomaVO.getMatricula().getDataColacaoGrau(), "dd 'de' MMMM 'de' yyyy"));
			} else {
				colacaoGrauVO = getFacadeFactory().getColacaoGrauFacade().consultarPorMatriculaAluno(expDiplomaVO.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				diplomaAlunoRelVO.setDataColacaoGrau(Uteis.getData(colacaoGrauVO.getData(), "dd 'de' MMMM 'de' yyyy"));
			}
			if (!tipoLayout.equals("DiplomaAlunoSuperior4Rel")) {
				if (Uteis.getSemestreData(expDiplomaVO.getMatricula().getDataConclusaoCurso()).equals("1")) {
					diplomaAlunoRelVO.setDataConclusaoCurso("primeiro semestre de " + Uteis.getData(expDiplomaVO.getMatricula().getDataConclusaoCurso(), "yyyy"));
				} else {
					diplomaAlunoRelVO.setDataConclusaoCurso("segundo semestre de " + Uteis.getData(expDiplomaVO.getMatricula().getDataConclusaoCurso(), "yyyy"));
				}
			} else {
				diplomaAlunoRelVO.setDataConclusaoCurso(Uteis.getData(expDiplomaVO.getMatricula().getDataConclusaoCurso(), "dd/MM/yyyy"));
			}

		}
	//	List<ExpedicaoDiplomaVO> listaExpedicao = getFacadeFactory().getExpedicaoDiplomaFacade().consultarPorMatriculaAluno(expDiplomaVO.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
	//	ExpedicaoDiplomaVO expedicaoDiplomaV = listaExpedicao.get(0);

		/*
		 * Parte responsavel por obter a autorizacao de acordo com o curso e a
		 * data da matricula. Obtem a autorizaçao publicada antes e mais proxima
		 * da data da matricula, ou seja, vigente para aquele aluno
		 */
		expDiplomaVO.getMatricula().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
		getFacadeFactory().getMatriculaFacade().carregarDados(expDiplomaVO.getMatricula(), NivelMontarDados.BASICO, usuario);

		Date dataMatricula = expDiplomaVO.getMatricula().getData();
		if (tipoLayout.equals("DiplomaAlunoSuperior5Rel")) {
			dataMatricula = expDiplomaVO.getMatricula().getDataConclusaoCurso();
		}
		executarMontagemDadosAutorizacaoCurso(expDiplomaVO.getMatricula().getCurso(), diplomaAlunoRelVO, expDiplomaVO, dataMatricula, expDiplomaVO.getMatricula().getAutorizacaoCurso().getCodigo(), expDiplomaVO.getMatricula().getRenovacaoReconhecimentoVO().getCodigo(), tipoLayout);
		
		expDiplomaVO.getMatricula().getCurso().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
		getFacadeFactory().getCursoFacade().carregarDados(expDiplomaVO.getMatricula().getCurso(), NivelMontarDados.BASICO, usuario);
		diplomaAlunoRelVO.setNivelEducacional(expDiplomaVO.getMatricula().getCurso().getNivelEducacional());
		// expedicaoDiplomaVO.getMatricula().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(expDiplomaVO.getMatricula().getCurso().getCodigo(),
		// Uteis.NIVELMONTARDADOS_TODOS, usuario));
		if(Uteis.isAtributoPreenchido(expDiplomaVO.getMatricula().getCurso().getAreaConhecimento())){
			expDiplomaVO.getMatricula().getCurso().setAreaConhecimento(getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimaria(expDiplomaVO.getMatricula().getCurso().getAreaConhecimento().getCodigo(), usuario));
		}
		diplomaAlunoRelVO.setAreaConhecimento(expDiplomaVO.getMatricula().getCurso().getAreaConhecimento().getNome());
		diplomaAlunoRelVO.setSexoAluno(expDiplomaVO.getMatricula().getAluno().getSexo());
		diplomaAlunoRelVO.setAlunoNome(expDiplomaVO.getMatricula().getAluno().getNome());
		if (tipoLayout.equals("DiplomaAlunoSuperior6Rel")) {
			diplomaAlunoRelVO.setAlunoNome(Uteis.alterarpreposicaoParaMinusculo(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(expDiplomaVO.getMatricula().getAluno().getNome().toLowerCase())));
		} else {
			diplomaAlunoRelVO.setAlunoNome(expDiplomaVO.getMatricula().getAluno().getNome());
		}
		if(tipoLayout.equals("DiplomaAlunoSuperior5Rel") || tipoLayout.equals("DiplomaAlunoSuperior6Rel")){ 
			if(diplomaAlunoRelVO.getObservacaoComplementarDiploma1().getObservacaoComplementar().getReapresentarNomeAluno()){
				diplomaAlunoRelVO.getObservacaoComplementarDiploma1().getObservacaoComplementar().setNomeAlunoRepresentar(expDiplomaVO.getMatricula().getAluno().getNome());
			}
			if(diplomaAlunoRelVO.getObservacaoComplementarDiploma2().getObservacaoComplementar().getReapresentarNomeAluno()){
				diplomaAlunoRelVO.getObservacaoComplementarDiploma2().getObservacaoComplementar().setNomeAlunoRepresentar(expDiplomaVO.getMatricula().getAluno().getNome());
			}
		} else if (tipoLayout.equals("TextoPadrao")) {
			if (Uteis.isAtributoPreenchido(diplomaAlunoRelVO.getObservacaoComplementarDiplomaVOs())) {
				diplomaAlunoRelVO.getObservacaoComplementarDiplomaVOs().stream()
					.filter(obs -> obs.getObservacaoComplementar().getReapresentarNomeAluno())
					.forEach(obs -> obs.getObservacaoComplementar().setNomeAlunoRepresentar(Uteis.alterarpreposicaoParaMinusculo(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(expDiplomaVO.getMatricula().getAluno().getNome().toLowerCase()))));
			}
		} else{
			diplomaAlunoRelVO.getObservacaoComplementarDiploma1().getObservacaoComplementar().setNomeAlunoRepresentar(expDiplomaVO.getMatricula().getAluno().getNome());
			diplomaAlunoRelVO.getObservacaoComplementarDiploma2().getObservacaoComplementar().setNomeAlunoRepresentar(expDiplomaVO.getMatricula().getAluno().getNome());
		}
		if (tipoLayout.equals("DiplomaAlunoSuperior4Rel")) {
			diplomaAlunoRelVO.setDataNascimentoAluno(Uteis.getData(expDiplomaVO.getMatricula().getAluno().getDataNasc(), "dd'/'MM'/'yyyy"));
		} else {
			diplomaAlunoRelVO.setDataNascimentoAluno(Uteis.getData(expDiplomaVO.getMatricula().getAluno().getDataNasc(), "dd 'de' MMMM 'de' yyyy"));
		}
		diplomaAlunoRelVO.setDataNascimentoSimples(Uteis.getDataAno4Digitos(expDiplomaVO.getMatricula().getAluno().getDataNasc()));

		if (credenciadora.getCodigo() != null && credenciadora.getCodigo() != 0) {
			diplomaAlunoRelVO.setMantenedora(credenciadora.getMantenedora());
			diplomaAlunoRelVO.setNomeUnidadeEnsino(credenciadora.getNomeExpedicaoDiploma());
			diplomaAlunoRelVO.setLeiCriacao1(credenciadora.getLeiCriacao1());
			diplomaAlunoRelVO.setLeiCriacao2(credenciadora.getLeiCriacao2());
		} else {
			diplomaAlunoRelVO.setMantenedora(expDiplomaVO.getMatricula().getUnidadeEnsino().getMantenedora());
			diplomaAlunoRelVO.setNomeUnidadeEnsino(expDiplomaVO.getMatricula().getUnidadeEnsino().getNomeExpedicaoDiploma());
			diplomaAlunoRelVO.setLeiCriacao1(expDiplomaVO.getMatricula().getUnidadeEnsino().getLeiCriacao1());
			diplomaAlunoRelVO.setLeiCriacao2(expDiplomaVO.getMatricula().getUnidadeEnsino().getLeiCriacao2());
		}

		diplomaAlunoRelVO.setCidadeNome(expDiplomaVO.getMatricula().getUnidadeEnsino().getCidade().getNome());
		diplomaAlunoRelVO.setBairro(expDiplomaVO.getMatricula().getUnidadeEnsino().getSetor());
		diplomaAlunoRelVO.setCep(expDiplomaVO.getMatricula().getUnidadeEnsino().getCEP());
		diplomaAlunoRelVO.setEndereco(expDiplomaVO.getMatricula().getUnidadeEnsino().getEndereco());
		diplomaAlunoRelVO.setEstadoSiglaUnidadeEnsino(expDiplomaVO.getMatricula().getUnidadeEnsino().getCidade().getEstado().getSigla());
		diplomaAlunoRelVO.setNumero(expDiplomaVO.getMatricula().getUnidadeEnsino().getNumero());
		diplomaAlunoRelVO.setEstadoSigla(expDiplomaVO.getMatricula().getAluno().getNaturalidade().getEstado().getSigla());
		diplomaAlunoRelVO.setFoneUnidadeEnsino(expDiplomaVO.getMatricula().getUnidadeEnsino().getTelComercial1());
		diplomaAlunoRelVO.setNomeEstadoNascimento(expDiplomaVO.getMatricula().getAluno().getNaturalidade().getEstado().getNome());
		diplomaAlunoRelVO.setMatricula(expDiplomaVO.getMatricula().getMatricula());
		diplomaAlunoRelVO.setCursoNome(expDiplomaVO.getMatricula().getCurso().getNome());
		if (tipoLayout.equals("DiplomaAlunoSuperior5Rel") || tipoLayout.equals("DiplomaAlunoSuperior6Rel")) {
			diplomaAlunoRelVO.setCursoNome(expDiplomaVO.getMatricula().getCurso().getNomeDocumentacao());
		}
		diplomaAlunoRelVO.setPreposicaoNomeCurso(expDiplomaVO.getMatricula().getCurso().getPreposicaoNomeCurso());
		diplomaAlunoRelVO.setAnoConclusao(expDiplomaVO.getMatricula().getAnoConclusao());
		diplomaAlunoRelVO.setCnpjUnidadeEnsino(expDiplomaVO.getMatricula().getUnidadeEnsino().getCNPJ());
		if (funcionarioPrincipalVO.getPessoa().getSexo().equals("M") || funcionarioPrincipalVO.getPessoa().getSexo().equals("F")) {
			if (tipoLayout.equals("DiplomaAlunoSuperior3Rel") || tipoLayout.equals("TextoPadrao")) {
				diplomaAlunoRelVO.setNomePrimeiroFuncionario(funcionarioPrincipalVO.getPessoa().getNome());
			} else if (tipoLayout.equals("DiplomaAlunoPos3Rel")) {
				diplomaAlunoRelVO.setNomePrimeiroFuncionario(tituloFuncionarioPrincipal + " " + funcionarioPrincipalVO.getPessoa().getNome());
			} else if (tipoLayout.equals("DiplomaAlunoPos4Rel")) {
				diplomaAlunoRelVO.setNomePrimeiroFuncionario(funcionarioPrincipalVO.getPessoa().getNome());
			} else if (tipoLayout.equals("DiplomaAlunoSuperior4Rel")) {
				diplomaAlunoRelVO.setNomePrimeiroFuncionario(funcionarioPrincipalVO.getPessoa().getNome());
			} else if (tipoLayout.equals("DiplomaAlunoSuperior5Rel") || tipoLayout.equals("DiplomaAlunoSuperior6Rel")) {
				diplomaAlunoRelVO.setNomePrimeiroFuncionario(tituloFuncionarioPrincipal + " " + funcionarioPrincipalVO.getPessoa().getNome());
			} else {
				diplomaAlunoRelVO.setNomePrimeiroFuncionario(tituloFuncionarioPrincipal + " " + Uteis.getNomeResumidoPessoa(funcionarioPrincipalVO.getPessoa().getNome()));
			}
		} else {
			diplomaAlunoRelVO.setNomePrimeiroFuncionario(funcionarioPrincipalVO.getPessoa().getNome());
		}
		diplomaAlunoRelVO.setSexoPrimeiroFuncionario(funcionarioPrincipalVO.getPessoa().getSexo());
		diplomaAlunoRelVO.setCargoPrimeiroFuncionario(cargoFuncPrincipalVO.getNome());
		if (funcionarioSecundarioVO.getPessoa().getSexo().equals("M") || funcionarioSecundarioVO.getPessoa().getSexo().equals("F")) {
			if (tipoLayout.equals("DiplomaAlunoSuperior3Rel") || tipoLayout.equals("TextoPadrao")) {
				diplomaAlunoRelVO.setNomeSegundoFuncionario(funcionarioSecundarioVO.getPessoa().getNome());
			} else if (tipoLayout.equals("DiplomaAlunoPos4Rel")) {
				diplomaAlunoRelVO.setNomeSegundoFuncionario(funcionarioSecundarioVO.getPessoa().getNome());
			} else if (tipoLayout.equals("DiplomaAlunoPos3Rel")) {
				diplomaAlunoRelVO.setNomeSegundoFuncionario(tituloFuncionarioSecundario + " " + funcionarioSecundarioVO.getPessoa().getNome());
			} else if (tipoLayout.equals("DiplomaAlunoSuperior4Rel")) {
				diplomaAlunoRelVO.setNomeSegundoFuncionario(funcionarioSecundarioVO.getPessoa().getNome());
			} else if (tipoLayout.equals("DiplomaAlunoSuperior5Rel") || tipoLayout.equals("DiplomaAlunoSuperior6Rel")) {
				diplomaAlunoRelVO.setNomeSegundoFuncionario(tituloFuncionarioSecundario + " " +funcionarioSecundarioVO.getPessoa().getNome());
				diplomaAlunoRelVO.setPortariaCargoPrimeiroFuncionario(cargoFuncPrincipalVO.getPortariaCargo());
			} else {
				diplomaAlunoRelVO.setNomeSegundoFuncionario(tituloFuncionarioSecundario + " " + funcionarioSecundarioVO.getPessoa().getNome());
			}
		} else {
			diplomaAlunoRelVO.setNomeSegundoFuncionario(tituloFuncionarioSecundario + " " + funcionarioSecundarioVO.getPessoa().getNome());
		}
		if (funcionarioTerceiroVO.getPessoa().getCodigo().intValue() != 0) {
			if (tipoLayout.equals("DiplomaAlunoPos3Rel") || tipoLayout.equals("DiplomaAlunoSuperior5Rel") || tipoLayout.equals("DiplomaAlunoSuperior6Rel") || tipoLayout.equals("TextoPadrao")) {
				diplomaAlunoRelVO.setNomeTerceiroFuncionario(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(funcionarioTerceiroVO.getPessoa().getNome().toLowerCase()));
				diplomaAlunoRelVO.setCargoTerceiroFuncionario(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(cargoFuncTerceiroVO.getNome().toLowerCase()));
				diplomaAlunoRelVO.setPortariaCargoTerceiroFuncionario(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(cargoFuncTerceiroVO.getPortariaCargo().toLowerCase()));
			}
		}
		if (funcionarioQuartoVO.getPessoa().getCodigo().intValue() != 0) {
			if (tipoLayout.equals("DiplomaAlunoPos3Rel") || tipoLayout.equals("DiplomaAlunoSuperior5Rel") || tipoLayout.equals("DiplomaAlunoSuperior6Rel") || tipoLayout.equals("TextoPadrao")) {
				diplomaAlunoRelVO.setNomeQuartoFuncionario(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(funcionarioQuartoVO.getPessoa().getNome().toLowerCase()));
				diplomaAlunoRelVO.setCargoQuartoFuncionario(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(cargoFuncQuartoVO.getNome().toLowerCase()));
				diplomaAlunoRelVO.setPortariaCargoQuartoFuncionario(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(cargoFuncQuartoVO.getPortariaCargo().toLowerCase()));
			}
		}
		if (funcionarioQuintoVO.getPessoa().getCodigo().intValue() != 0) {
			if (tipoLayout.equals("DiplomaAlunoPos3Rel") || tipoLayout.equals("DiplomaAlunoSuperior5Rel") || tipoLayout.equals("DiplomaAlunoSuperior6Rel") || tipoLayout.equals("TextoPadrao")) {
				diplomaAlunoRelVO.setNomeQuintoFuncionario(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(funcionarioQuintoVO.getPessoa().getNome().toLowerCase()));
				diplomaAlunoRelVO.setCargoQuintoFuncionario(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(cargoFuncQuintoVO.getNome().toLowerCase()));
				diplomaAlunoRelVO.setPortariaCargoQuintoFuncionario(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(cargoFuncQuintoVO.getPortariaCargo().toLowerCase()));
			}
		}
		diplomaAlunoRelVO.setSexoSegundoFuncionario(funcionarioSecundarioVO.getPessoa().getSexo());
		diplomaAlunoRelVO.setCargoSegundoFuncionario(cargoFuncSecundarioVO.getNome());
		diplomaAlunoRelVO.setPortariaCargoSegundoFuncionario(cargoFuncSecundarioVO.getPortariaCargo());
		if (expDiplomaVO.getMatricula().getCurso().getNivelEducacional().equals("PO")) {
			diplomaAlunoRelVO.setPublicacaoDO(Uteis.getData(expDiplomaVO.getMatricula().getCurso().getDataPublicacaoDO(), "dd 'de' MMMM 'de' yyyy").toLowerCase());
		} else {
			if (!expDiplomaVO.getVia().equals("1")) {
				List<ExpedicaoDiplomaVO> listaExpedicao = getFacadeFactory().getExpedicaoDiplomaFacade().consultarPorMatriculaAluno(expDiplomaVO.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				ExpedicaoDiplomaVO expedicaoDiplomaVO = listaExpedicao.get(0);
				if (tipoLayout.equals("DiplomaAlunoSuperior5Rel") || tipoLayout.equals("DiplomaAlunoSuperior6Rel")) {
					diplomaAlunoRelVO.setNumeroRegistroDiplomaViaAnterior(expDiplomaVO.getNumeroRegistroDiplomaViaAnterior());
					diplomaAlunoRelVO.setNumeroProcessoViaAnterior(expDiplomaVO.getNumeroProcessoViaAnterior());
					diplomaAlunoRelVO.setVia(expDiplomaVO.getVia() + "ª Via");
					diplomaAlunoRelVO.setViaAnterior(expedicaoDiplomaVO.getVia());
					diplomaAlunoRelVO.setDataExpedicaoViaAnterior(Uteis.getDataCidadeDiaMesPorExtensoEAno("", expDiplomaVO.getDataExpedicao(), true));										
					diplomaAlunoRelVO.setReitorRegistroDiplomaViaAnterior(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(expDiplomaVO.getReitorRegistroDiplomaViaAnterior().getPessoa().getNome().toLowerCase()));
					if (expDiplomaVO.getCargoReitorRegistroDiplomaViaAnterior().getCodigo().intValue() > 0) {
						expDiplomaVO.setCargoReitorRegistroDiplomaViaAnterior(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(expDiplomaVO.getCargoReitorRegistroDiplomaViaAnterior().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
					}
					diplomaAlunoRelVO.setCargoReitorRegistroDiplomaViaAnterior(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(expDiplomaVO.getCargoReitorRegistroDiplomaViaAnterior().getNome().toLowerCase()));
					diplomaAlunoRelVO.setSecretariaRegistroDiplomaViaAnterior(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(expDiplomaVO.getSecretariaRegistroDiplomaViaAnterior().getPessoa().getNome().toLowerCase()));
					diplomaAlunoRelVO.setDataRegistroDiplomaViaAnterior(Uteis.getDataCidadeDiaMesPorExtensoEAno("", expDiplomaVO.getDataRegistroDiplomaViaAnterior(), true));					
				} else {
					diplomaAlunoRelVO.setNumeroRegistroDiploma(expDiplomaVO.getNumeroRegistroDiplomaViaAnterior());
					diplomaAlunoRelVO.setNumeroProcesso(expDiplomaVO.getNumeroProcessoViaAnterior());
					diplomaAlunoRelVO.setVia(expDiplomaVO.getVia() + "ª Via");
					diplomaAlunoRelVO.setViaAnterior(expedicaoDiplomaVO.getVia());
					diplomaAlunoRelVO.setDataExpedicaoViaAnterior(Uteis.getDataCidadeDiaMesPorExtensoEAno("", expedicaoDiplomaVO.getDataExpedicao(), true));					
				}
			}
		}
		// getDiplomaAlunoRelAux().setAutorizacaoNrRegistroInterno(expDiplomaVO.getMatricula().getUnidadeEnsino().getCredenciamentoPortaria());
		// getDiplomaAlunoRelAux().setDataPublicacaoDO(Uteis.getData(expDiplomaVO.getMatricula().getUnidadeEnsino().getDataPublicacaoDO(),
		// "dd 'de' MMMM 'de' yyyy"));
		diplomaAlunoRelVO.setAutorizacaoNrRegistroInternoUnidade(expDiplomaVO.getMatricula().getCurso().getNrRegistroInterno());
		diplomaAlunoRelVO.setDataPublicacaoDOUnidade(Uteis.getData(Uteis.isAtributoPreenchido(expDiplomaVO.getMatricula().getCurso().getModalidadeCurso()) && expDiplomaVO.getMatricula().getCurso().getModalidadeCurso().equals(ModalidadeDisciplinaEnum.ON_LINE) ? expDiplomaVO.getMatricula().getUnidadeEnsino().getDataPublicacaoDOEAD() : expDiplomaVO.getMatricula().getUnidadeEnsino().getDataPublicacaoDO(), "dd 'de' MMMM 'de' yyyy"));
		diplomaAlunoRelVO.setReconhecimentoCursoAutorizacaoResolucao(getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoDataMaisRecente(expDiplomaVO.getMatricula().getCurso().getCodigo(), expDiplomaVO.getMatricula().getData(), Uteis.NIVELMONTARDADOS_DADOSBASICOS).getNome());

		if (tipoLayout.equals("DiplomaAlunoSuperior4Rel")) {
			diplomaAlunoRelVO.setReconhecimentoCursoAutorizacaoResolucao(expDiplomaVO.getMatricula().getCurso().getNrRegistroInterno());
		}
		diplomaAlunoRelVO.setHabilitacao(expDiplomaVO.getMatricula().getCurso().getHabilitacao());
		diplomaAlunoRelVO.setNacionalidadeAluno(expDiplomaVO.getMatricula().getAluno().getNacionalidade().getNacionalidade());
		if (tipoLayout.equals("DiplomaAlunoSuperior4Rel")) {
			CidadeVO cidade = new CidadeVO();
			cidade = getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(expDiplomaVO.getMatricula().getAluno().getNaturalidade().getCodigo(), false, usuario);
			diplomaAlunoRelVO.setNaturalidadeAluno(cidade.getEstado().getNome());
		} else 	if (tipoLayout.equals("DiplomaAlunoSuperior5Rel") || tipoLayout.equals("DiplomaAlunoSuperior6Rel") || tipoLayout.equals("TextoPadrao")) {			
			diplomaAlunoRelVO.setNumeroRegistroDiploma(getFacadeFactory().getControleLivroRegistroDiplomaFacade().obterNumeroRegistroMatricula(expDiplomaVO.getMatricula().getMatricula()));
			diplomaAlunoRelVO.setNumeroProcesso(expDiplomaVO.getNumeroProcesso());

			diplomaAlunoRelVO.setNacionalidadeAluno(expDiplomaVO.getMatricula().getAluno().getNacionalidade().getNacionalidade().toLowerCase());
			String estado = expDiplomaVO.getMatricula().getAluno().getNaturalidade().getEstado().getNome().toLowerCase();
			if (expDiplomaVO.getMatricula().getAluno().getNaturalidade().getEstado().getSigla().equals("DF")) {
				estado = "Distrito Federal";
				expDiplomaVO.getMatricula().getAluno().getNaturalidade().getEstado().setNome(estado);
			} else {
				estado = Uteis.generoEstado(expDiplomaVO.getMatricula().getAluno().getNaturalidade().getEstado().getSigla()) + estado;
				expDiplomaVO.getMatricula().getAluno().getNaturalidade().getEstado().setNome(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(estado));				
			}
			String naturalidade = Uteis.gerarUpperCasePrimeiraLetraDasPalavras(expDiplomaVO.getMatricula().getAluno().getNaturalidade().getNome().toLowerCase()) + ", " + Uteis.gerarUpperCasePrimeiraLetraDasPalavras(estado);
			if (!expDiplomaVO.getMatricula().getAluno().getNaturalidade().getEstado().getSigla().trim().equals("")) {
				naturalidade = naturalidade.replace(" Dos ", " dos ");
				naturalidade = naturalidade.replace(" Das ", " das ");
				naturalidade = naturalidade.replace(" Da ", " da ");
				naturalidade = naturalidade.replace(" Do ", " do ");
				diplomaAlunoRelVO.setNaturalidadeAluno(naturalidade);
			} else {
				naturalidade = Uteis.gerarUpperCasePrimeiraLetraDasPalavras(expDiplomaVO.getMatricula().getAluno().getNaturalidade().getNome().toLowerCase());
				naturalidade = naturalidade.replace(" Dos ", " dos ");
				naturalidade = naturalidade.replace(" Das ", " das ");
				naturalidade = naturalidade.replace(" Da ", " da ");
				naturalidade = naturalidade.replace(" Do ", " do ");
				diplomaAlunoRelVO.setNaturalidadeAluno(naturalidade);
			}
			
			diplomaAlunoRelVO.setNomePrimeiroFuncionario(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(diplomaAlunoRelVO.getNomePrimeiroFuncionario().toLowerCase()));
			diplomaAlunoRelVO.setCargoPrimeiroFuncionario(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(diplomaAlunoRelVO.getCargoPrimeiroFuncionario().toLowerCase()));			
			diplomaAlunoRelVO.setPortariaCargoPrimeiroFuncionario(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(diplomaAlunoRelVO.getPortariaCargoPrimeiroFuncionario().toLowerCase()));			
			diplomaAlunoRelVO.setNomeSegundoFuncionario(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(diplomaAlunoRelVO.getNomeSegundoFuncionario().toLowerCase()));
			diplomaAlunoRelVO.setCargoSegundoFuncionario(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(diplomaAlunoRelVO.getCargoSegundoFuncionario().toLowerCase()));
			diplomaAlunoRelVO.setPortariaCargoSegundoFuncionario(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(diplomaAlunoRelVO.getPortariaCargoSegundoFuncionario().toLowerCase()));
			diplomaAlunoRelVO.setNomeTerceiroFuncionario(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(diplomaAlunoRelVO.getNomeTerceiroFuncionario().toLowerCase()));
			diplomaAlunoRelVO.setCargoTerceiroFuncionario(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(diplomaAlunoRelVO.getCargoTerceiroFuncionario().toLowerCase()));
			diplomaAlunoRelVO.setCargoQuartoFuncionario(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(diplomaAlunoRelVO.getCargoQuartoFuncionario().toLowerCase()));
			diplomaAlunoRelVO.setCargoQuintoFuncionario(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(diplomaAlunoRelVO.getCargoQuintoFuncionario().toLowerCase()));
			diplomaAlunoRelVO.setPortariaCargoTerceiroFuncionario(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(diplomaAlunoRelVO.getPortariaCargoTerceiroFuncionario().toLowerCase()));
			diplomaAlunoRelVO.setPortariaCargoQuartoFuncionario(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(diplomaAlunoRelVO.getPortariaCargoQuartoFuncionario().toLowerCase()));
			diplomaAlunoRelVO.setPortariaCargoQuintoFuncionario(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(diplomaAlunoRelVO.getPortariaCargoQuintoFuncionario().toLowerCase()));
			diplomaAlunoRelVO.setAlunoNome(Uteis.alterarpreposicaoParaMinusculo(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(diplomaAlunoRelVO.getAlunoNome().toLowerCase())));
		} else {
			diplomaAlunoRelVO.setNaturalidadeAluno(expDiplomaVO.getMatricula().getAluno().getNaturalidade().getNome());
		}
		diplomaAlunoRelVO.setRgAluno(expDiplomaVO.getMatricula().getAluno().getRG());
		diplomaAlunoRelVO.setOrgaoEmissorAluno(expDiplomaVO.getMatricula().getAluno().getOrgaoEmissor());
		diplomaAlunoRelVO.setEstadoEmissorRgAluno(expDiplomaVO.getMatricula().getAluno().getEstadoEmissaoRG());
		diplomaAlunoRelVO.setTituloCurso(expDiplomaVO.getMatricula().getCurso().getTitulo_Apresentar());
		diplomaAlunoRelVO.setTituloCursoCompleto(expDiplomaVO.getMatricula().getCurso().getTitulacaoDoFormando());
		if (expDiplomaVO.getMatricula().getAluno().getSexo().equals("M")) {
			if (expDiplomaVO.getTitulacaoMasculinoApresentarDiploma().isEmpty()) {
				diplomaAlunoRelVO.setTitulacaoDoFormando(expDiplomaVO.getMatricula().getCurso().getTitulacaoDoFormando());
			} else {
				diplomaAlunoRelVO.setTitulacaoDoFormando(expDiplomaVO.getTitulacaoMasculinoApresentarDiploma());
			}
		} else {
			if (expDiplomaVO.getTitulacaoFemininoApresentarDiploma().isEmpty()) {
				diplomaAlunoRelVO.setTitulacaoDoFormando(expDiplomaVO.getMatricula().getCurso().getTitulacaoDoFormandoFeminino());
			} else {
				diplomaAlunoRelVO.setTitulacaoDoFormando(expDiplomaVO.getTitulacaoFemininoApresentarDiploma());
			}
		}

		// getDiplomaAlunoRelAux()
		UnidadeEnsinoCursoVO unidadeEnsinoCursoVO = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidadeTurno(expDiplomaVO.getMatricula().getCurso().getCodigo(), expDiplomaVO.getMatricula().getUnidadeEnsino().getCodigo(), expDiplomaVO.getMatricula().getTurno().getCodigo(), usuario);
		if (utilizarUnidadeMatriz) {
			diplomaAlunoRelVO.setCidadeDataExpedicaoDiploma(Uteis.getDataCidadeDiaMesPorExtensoEAno(consultarCidadePorUnidadeEnsinoMatriz(usuario), expDiplomaVO.getDataExpedicao(), true));
			if (tipoLayout.equals("DiplomaAlunoSuperior5Rel") || tipoLayout.equals("DiplomaAlunoSuperior6Rel")) {
				CidadeVO cidadeVO = getFacadeFactory().getCidadeFacade().consultarPorUnidadeEnsinoMatriz(false, usuario);
				if (cidadeVO == null || cidadeVO.getCodigo() == 0) {
					cidadeVO = new CidadeVO();
				}				
				String estado = cidadeVO.getEstado().getNome();
				if (cidadeVO.getEstado().getSigla().equals("DF")) {
					estado = "Distrito Federal";
				} else {					
					estado = Uteis.generoEstado(cidadeVO.getEstado().getSigla()) + Uteis.gerarUpperCasePrimeiraLetraDasPalavras(estado);
				}
				diplomaAlunoRelVO.setCidadeDataExpedicaoDiploma(Uteis.getDataCidadeDiaMesPorExtensoEAno(cidadeVO.getNome(), estado, expDiplomaVO.getDataExpedicao(), true));
				diplomaAlunoRelVO.setDataExpedicaoDiploma(Uteis.getData(expDiplomaVO.getDataExpedicao(), "dd 'de' MMMMM 'de' yyyy"));				
			}
			diplomaAlunoRelVO.setRazaoSocial(cod.getRazaoSocial());
			if (unidadeEnsinoCursoVO.getMantenedora().equals("")) {
				diplomaAlunoRelVO.setMantenedora(cod.getMantenedora());
			} else {
				diplomaAlunoRelVO.setMantenedora(unidadeEnsinoCursoVO.getMantenedora());
			}
			if (!unidadeEnsinoCursoVO.getMantida().equals("")) {
				diplomaAlunoRelVO.setNomeUnidadeEnsino(unidadeEnsinoCursoVO.getMantida());
			}
			diplomaAlunoRelVO.setCredenciamentoPortaria(Uteis.isAtributoPreenchido(expDiplomaVO.getMatricula().getCurso().getModalidadeCurso()) && expDiplomaVO.getMatricula().getCurso().getModalidadeCurso().equals(ModalidadeDisciplinaEnum.ON_LINE) ? cod.getCredenciamentoPortariaEAD() : cod.getCredenciamentoPortaria());
			if (tipoLayout.equals("DiplomaAlunoSuperior5Rel") || tipoLayout.equals("DiplomaAlunoSuperior6Rel")) {
				diplomaAlunoRelVO.setCredenciamentoPortaria(Uteis.isAtributoPreenchido(expDiplomaVO.getMatricula().getCurso().getModalidadeCurso()) && expDiplomaVO.getMatricula().getCurso().getModalidadeCurso().equals(ModalidadeDisciplinaEnum.ON_LINE) ? cod.getCredenciamentoEAD() : cod.getCredenciamento());
			}						
			diplomaAlunoRelVO.setDataPublicacaoDoEmpresa(Uteis.getDataAno4Digitos(cod.getDataPublicacaoDO()));

		} else if (!utilizarUnidadeMatriz && expDiplomaVO.getUnidadeEnsinoCertificadora().getCodigo() != null && expDiplomaVO.getUnidadeEnsinoCertificadora().getCodigo() != 0) {

			diplomaAlunoRelVO.setCidadeDataExpedicaoDiploma(Uteis.getDataCidadeDiaMesPorExtensoEAno(credenciadora.getCidade().getNome(), expDiplomaVO.getDataExpedicao(), true));
			if (tipoLayout.equals("DiplomaAlunoSuperior5Rel") || tipoLayout.equals("DiplomaAlunoSuperior6Rel")) {
				String estado = credenciadora.getCidade().getEstado().getNome().toLowerCase();
				if (credenciadora.getCidade().getEstado().getSigla().equals("DF")) {
					estado = "Distrito Federal";
				} else {
					estado = Uteis.generoEstado(credenciadora.getCidade().getEstado().getSigla()) + Uteis.gerarUpperCasePrimeiraLetraDasPalavras(estado);
				}
				diplomaAlunoRelVO.setCidadeDataExpedicaoDiploma(Uteis.getDataCidadeDiaMesPorExtensoEAno(credenciadora.getCidade().getNome(), estado, expDiplomaVO.getDataExpedicao(), true));
				diplomaAlunoRelVO.setDataExpedicaoDiploma(Uteis.getData(expDiplomaVO.getDataExpedicao(), "dd 'de' MMMMM 'de' yyyy"));
			}
			diplomaAlunoRelVO.setRazaoSocial(credenciadora.getRazaoSocial());
			if (unidadeEnsinoCursoVO.getMantenedora().equals("")) {
				diplomaAlunoRelVO.setMantenedora(credenciadora.getMantenedora());
			} else {
				diplomaAlunoRelVO.setMantenedora(unidadeEnsinoCursoVO.getMantenedora());
			}
			if (!unidadeEnsinoCursoVO.getMantida().equals("")) {
				diplomaAlunoRelVO.setNomeUnidadeEnsino(unidadeEnsinoCursoVO.getMantida());
			}
			diplomaAlunoRelVO.setCredenciamentoPortaria(Uteis.isAtributoPreenchido(expDiplomaVO.getMatricula().getCurso().getModalidadeCurso()) && expDiplomaVO.getMatricula().getCurso().getModalidadeCurso().equals(ModalidadeDisciplinaEnum.ON_LINE) ? credenciadora.getCredenciamentoPortariaEAD() : credenciadora.getCredenciamentoPortaria());
			if (tipoLayout.equals("DiplomaAlunoSuperior5Rel") || tipoLayout.equals("DiplomaAlunoSuperior6Rel")) {
				diplomaAlunoRelVO.setCredenciamentoPortaria(Uteis.isAtributoPreenchido(expDiplomaVO.getMatricula().getCurso().getModalidadeCurso()) && expDiplomaVO.getMatricula().getCurso().getModalidadeCurso().equals(ModalidadeDisciplinaEnum.ON_LINE) ? credenciadora.getCredenciamentoEAD() : credenciadora.getCredenciamento());
			}			
			diplomaAlunoRelVO.setDataPublicacaoDoEmpresa(Uteis.getDataAno4Digitos(credenciadora.getDataPublicacaoDO()));

		} else {
			diplomaAlunoRelVO.setCidadeDataExpedicaoDiploma(Uteis.getDataCidadeDiaMesPorExtensoEAno(expDiplomaVO.getMatricula().getUnidadeEnsino().getCidade().getNome(), expDiplomaVO.getDataExpedicao(), true));
			if (tipoLayout.equals("DiplomaAlunoSuperior5Rel") || tipoLayout.equals("DiplomaAlunoSuperior6Rel")) {
				String estado = expDiplomaVO.getMatricula().getUnidadeEnsino().getCidade().getEstado().getNome();
				if (expDiplomaVO.getMatricula().getUnidadeEnsino().getCidade().getEstado().getSigla().equals("DF")) {
					estado = "Distrito Federal";
				} else {
					estado = Uteis.generoEstado(expDiplomaVO.getMatricula().getUnidadeEnsino().getCidade().getEstado().getSigla()) + Uteis.gerarUpperCasePrimeiraLetraDasPalavras(estado);
				}
				diplomaAlunoRelVO.setCidadeDataExpedicaoDiploma(Uteis.getDataCidadeDiaMesPorExtensoEAno(expDiplomaVO.getMatricula().getUnidadeEnsino().getCidade().getNome(), estado, expDiplomaVO.getDataExpedicao(), true));
				diplomaAlunoRelVO.setDataExpedicaoDiploma(Uteis.getData(expDiplomaVO.getDataExpedicao(), "dd 'de' MMMMM 'de' yyyy"));
			}
			diplomaAlunoRelVO.setRazaoSocial(expDiplomaVO.getMatricula().getUnidadeEnsino().getRazaoSocial());
			if (unidadeEnsinoCursoVO.getMantenedora().equals("")) {
				diplomaAlunoRelVO.setMantenedora(expDiplomaVO.getMatricula().getUnidadeEnsino().getMantenedora());
			} else {
				diplomaAlunoRelVO.setMantenedora(unidadeEnsinoCursoVO.getMantenedora());
			}
			if (!unidadeEnsinoCursoVO.getMantida().equals("")) {
				diplomaAlunoRelVO.setNomeUnidadeEnsino(unidadeEnsinoCursoVO.getMantida());
			}
			diplomaAlunoRelVO.setCredenciamentoPortaria(Uteis.isAtributoPreenchido(expDiplomaVO.getMatricula().getCurso().getModalidadeCurso()) && expDiplomaVO.getMatricula().getCurso().getModalidadeCurso().equals(ModalidadeDisciplinaEnum.ON_LINE) ? expDiplomaVO.getMatricula().getUnidadeEnsino().getCredenciamentoPortariaEAD() : expDiplomaVO.getMatricula().getUnidadeEnsino().getCredenciamentoPortaria());
			if (tipoLayout.equals("DiplomaAlunoSuperior5Rel") || tipoLayout.equals("DiplomaAlunoSuperior6Rel")) {
				diplomaAlunoRelVO.setCredenciamentoPortaria(Uteis.isAtributoPreenchido(expDiplomaVO.getMatricula().getCurso().getModalidadeCurso()) && expDiplomaVO.getMatricula().getCurso().getModalidadeCurso().equals(ModalidadeDisciplinaEnum.ON_LINE) ? expDiplomaVO.getMatricula().getUnidadeEnsino().getCredenciamentoEAD() : expDiplomaVO.getMatricula().getUnidadeEnsino().getCredenciamento());
			}
			diplomaAlunoRelVO.setDataPublicacaoDoEmpresa(Uteis.getDataAno4Digitos(expDiplomaVO.getMatricula().getUnidadeEnsino().getDataPublicacaoDO()));
		}
		diplomaAlunoRelVO.setCidadeDataAtual(Uteis.getDataCidadeDiaMesPorExtensoEAno(expDiplomaVO.getMatricula().getUnidadeEnsino().getCidade().getNome(), new Date(), true));

		diplomaAlunoRelVO.setCargaHorariaTotal(executarConsultaCargaHorariaTotal(expDiplomaVO.getMatricula(), tipoLayout, usuario));

		diplomaAlunoRelVO.setCpfAluno(expDiplomaVO.getMatricula().getAluno().getCPF());
		if (tipoLayout.equals("DiplomaAlunoSuperior4Rel")) {
			diplomaAlunoRelVO.setDataAtualExtenso(Uteis.getDataCidadeEstadoDiaMesPorExtensoEAno(expDiplomaVO.getMatricula().getUnidadeEnsino().getCidade().getNome(), expDiplomaVO.getMatricula().getUnidadeEnsino().getCidade().getEstado().getSigla(), expDiplomaVO.getDataExpedicao(), false));
		} else {
			diplomaAlunoRelVO.setDataAtualExtenso(Uteis.getDataCidadeDiaMesPorExtensoEAno(expDiplomaVO.getMatricula().getUnidadeEnsino().getCidade().getNome(), new Date(), false));
		}
		diplomaAlunoRelVO.setPercentualCHIntegralizacaoMatricula(Uteis.getDoubleFormatado(getFacadeFactory().getHistoricoFacade().consultarPercentualCHIntegralizacaoPorMatriculaGradeCurricular(expDiplomaVO.getMatricula().getMatricula(), expDiplomaVO.getMatricula().getGradeCurricularAtual().getCodigo(), usuario)));
		montarDadosFiliacao(expDiplomaVO.getMatricula().getAluno().getCodigo(), diplomaAlunoRelVO, usuario);
		montarDadosCursoEnderecoEstabelecimentoAnterior(expDiplomaVO.getMatricula().getAluno().getCodigo(), diplomaAlunoRelVO, usuario);
		diplomaAlunoRelVO.setListaHistorico(montarHistoricoAluno(expDiplomaVO.getMatricula(), usuario, expDiplomaVO.getGradeCurricularVO().getCodigo(), xmlDiploma));
		Map<String, DiplomaAlunoHistoricoRelVO> listaPeriodo = new HashMap<String, DiplomaAlunoHistoricoRelVO>(0);
		for (DiplomaAlunoHistoricoRelVO diplomaAlunoHistoricoRelVO : diplomaAlunoRelVO.getListaHistorico()) {
			if (!listaPeriodo.containsKey(diplomaAlunoHistoricoRelVO.getAnoSemstre() + diplomaAlunoHistoricoRelVO.getNomePeriodo())) {
				listaPeriodo.put(diplomaAlunoHistoricoRelVO.getAnoSemstre() + diplomaAlunoHistoricoRelVO.getNomePeriodo(), diplomaAlunoHistoricoRelVO);
				diplomaAlunoRelVO.getListaHistoricoEstabelecimento().add(diplomaAlunoHistoricoRelVO);
			}
		}
		Ordenacao.ordenarLista(diplomaAlunoRelVO.getListaHistoricoEstabelecimento(), "anoSemstre");
		try {
			lista.add(diplomaAlunoRelVO);
			return lista;
		} finally {
//			setDiplomaAlunoRelAux(null);
			//listaExpedicao = null;
			//expedicaoDiplomaVO = null;
		}
	}


	public String getDesignIReportRelatorio(String tipoLayout, String tipoDiploma) {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade(tipoLayout, tipoDiploma) + ".jrxml");
	}

	public String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public List<DiplomaAlunoHistoricoRelVO> montarHistoricoAluno(MatriculaVO matriculaVO, UsuarioVO usuarioVO, Integer gradeCurricular, Boolean xmlDiploma) throws Exception {
		List<HistoricoVO> historicoVOs = getFacadeFactory().getHistoricoFacade().consultaRapidaPorMatricula(matriculaVO.getMatricula(), 0, OrdemHistoricoDisciplina.ANO_SEMESTRE.getValor(), true, null, false, NivelMontarDados.BASICO, false, xmlDiploma, usuarioVO);
		List<DiplomaAlunoHistoricoRelVO> diplomaAlunoHistoricoRelVOs = new ArrayList<DiplomaAlunoHistoricoRelVO>(0);
		for (HistoricoVO historicoVO : historicoVOs) {
			DiplomaAlunoHistoricoRelVO obj = new DiplomaAlunoHistoricoRelVO();
			obj.setDisciplinaNome(historicoVO.getDisciplina().getNome());
			obj.setCargaHorariaDisciplina(historicoVO.getGradeDisciplinaVO().getCargaHoraria().toString());
			obj.setNotaDisciplina(Uteis.getDoubleFormatado(historicoVO.getMediaFinal()).toString());
			obj.setNomePeriodo(executarConsultaNomePeriodo(historicoVO, usuarioVO, gradeCurricular));
			obj.setResultadoFinal(historicoVO.getSituacao_Apresentar());
			obj.setFrequencia(historicoVO.getFreguencia());
			obj.setCargaHorariaFrequentada(historicoVO.getCargaHorariaCursada().toString());
			obj.setAnoSemstre(historicoVO.getAnoHistorico());
			obj.setInstituicao(historicoVO.getInstituicao());
			obj.setCidade(historicoVO.getCidade());
			obj.setEstado(historicoVO.getEstado());
			diplomaAlunoHistoricoRelVOs.add(obj);
		}
		return diplomaAlunoHistoricoRelVOs;
	}

	public String executarConsultaNomePeriodo(HistoricoVO historicoVO, UsuarioVO usuarioVO, Integer gradeCurricular) throws Exception {
		PeriodoLetivoVO periodoLetivoVO = new PeriodoLetivoVO();
		if ((historicoVO.getConfiguracaoAcademico().getApresentarPeriodoLetivoMatriculaPeriodoAtualHistorico() && historicoVO.getGradeDisciplinaVO().getTipoDisciplina().equals("OP")) || historicoVO.getHistoricoDisciplinaForaGrade()) {
			periodoLetivoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(historicoVO.getMatriculaPeriodo().getPeridoLetivo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		} else {
			periodoLetivoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorGradeCurricularDisciplina(historicoVO.getDisciplina().getCodigo(), gradeCurricular, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		}
		return periodoLetivoVO.getDescricao();
	}

	public String executarConsultaCargaHorariaTotal(MatriculaVO matricula, String layout, UsuarioVO usuarioVO) throws Exception {
		GradeCurricularVO gradeCurricularVO = getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(matricula.getGradeCurricularAtual().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
		Integer cargaHorariaCumprida = getFacadeFactory().getDisciplinaFacade().consultarCargaHorariaCumpridaNoHistoricoPorGradeCurricularComDisciplinaEquivalente(matricula.getMatricula(), matricula.getGradeCurricularAtual().getCodigo(), true, usuarioVO);
		return cargaHorariaCumprida == null ? "0" : cargaHorariaCumprida.toString();
	}


	public static String getIdEntidade(String tipoLayout, String tipoDiploma) {
		String nivel = "";
		if (tipoDiploma == null || tipoDiploma.equals("") || tipoDiploma.equals("BA")) {
			nivel = "";
		}
		if (tipoDiploma.equals("SU")) {
			nivel = tipoLayout;
		}
		if (tipoDiploma.equals("GT") && ((tipoLayout.equals("DiplomaAlunoSuperior5Rel") || tipoLayout.equals("DiplomaAlunoSuperior5RelVerso")) 
				|| (tipoLayout.equals("DiplomaAlunoSuperior6Rel") || tipoLayout.equals("DiplomaAlunoSuperior6RelVerso")))) {
			nivel = tipoLayout;
		} else if (tipoDiploma.equals("GT") || tipoDiploma.equals("PR")) {
			nivel = "DiplomaAlunoGradTecnologicaRel";
		}
		if (tipoDiploma.equals("PO")) {
			nivel = tipoLayout;
		}
		if (tipoDiploma.equals("ME") || tipoDiploma.equals("MT")) {
			nivel = tipoLayout;
		}
		return nivel;
	}

	public void montarDadosFiliacao(Integer codigoAluno, DiplomaAlunoRelVO obj, UsuarioVO usuarioVO) throws Exception {
		List<FiliacaoVO> filiacaoVOs = getFacadeFactory().getFiliacaoFacade().consultarPorCodigoPessoaTipo(codigoAluno, "", false, usuarioVO);
		for (FiliacaoVO filiacaoVO : filiacaoVOs) {
			if (filiacaoVO.getTipo().equals("PA")) {
				obj.setNomePai(filiacaoVO.getNome());
			} else if (filiacaoVO.getTipo().equals("MA")) {
				obj.setNomeMae(filiacaoVO.getNome());
			}
		}
	}

	public void montarDadosCursoEnderecoEstabelecimentoAnterior(Integer aluno, DiplomaAlunoRelVO obj, UsuarioVO usuarioVO) throws Exception {
		FormacaoAcademicaVO dadosAnterior = getFacadeFactory().getFormacaoAcademicaFacade().consultarPorCodigoPessoa(aluno, false, usuarioVO);
		obj.setCursoAnterior(dadosAnterior.getCurso());
		obj.setEstabelecimentoAnterior(dadosAnterior.getInstituicao());
		obj.setEnderecoEstabelecimentoAnterior("");
	}
	
	public void executarMontagemDadosAutorizacaoCurso(CursoVO cursoVO, DiplomaAlunoRelVO diplomaAlunoRelVO, ExpedicaoDiplomaVO expedicaoDiplomaVO, Date dataMatricula, Integer autorizacaoCurso, Integer codigoRenovacaoReconhecimento, String tipoLayout) throws Exception {
		// DADOS DA RENOVAÇÃO DO RECONHECIMENTO
		String dataReconhecimento = "";
		String dataPrimeiroReconhecimento = "";

		AutorizacaoCursoVO renovacaoReconhecimentoCursoVO = new AutorizacaoCursoVO();
		if (Uteis.isAtributoPreenchido(codigoRenovacaoReconhecimento)) {
			renovacaoReconhecimentoCursoVO = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorChavePrimaria(codigoRenovacaoReconhecimento, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		} else {
			/* Consulta última renovação */
			renovacaoReconhecimentoCursoVO = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoDataVigenteMatricula(cursoVO.getCodigo(), dataMatricula, Uteis.NIVELMONTARDADOS_COMBOBOX);
			if (!Uteis.isAtributoPreenchido(renovacaoReconhecimentoCursoVO)) {
				if ((!tipoLayout.equals("DiplomaAlunoSuperior5Rel") && !tipoLayout.equals("DiplomaAlunoSuperior6Rel")) || (dataMatricula == null && (tipoLayout.equals("DiplomaAlunoSuperior5Rel")))) {
					if (cursoVO.getNivelEducacionalPosGraduacao()) {
						renovacaoReconhecimentoCursoVO = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoPos(cursoVO.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX);
					} else {
						renovacaoReconhecimentoCursoVO = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoDataVigenteMatricula(cursoVO.getCodigo(), new Date(), Uteis.NIVELMONTARDADOS_COMBOBOX);
					}
				}
			}
		}
		if (Uteis.isAtributoPreenchido(renovacaoReconhecimentoCursoVO)) {
			diplomaAlunoRelVO.setReconhecimento(renovacaoReconhecimentoCursoVO.getNome());
			dataReconhecimento = Uteis.getData(renovacaoReconhecimentoCursoVO.getData(), "dd/MM/yyyy");
		}
		renovacaoReconhecimentoCursoVO = null;
		

		// DADOS DO RECONHECIMENTO
		AutorizacaoCursoVO autorizacaoCursoVO = new AutorizacaoCursoVO();
		if (Uteis.isAtributoPreenchido(autorizacaoCurso)) {
			autorizacaoCursoVO = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorChavePrimaria(autorizacaoCurso, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		} else {
			autorizacaoCursoVO = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoDataMaisAntigo(cursoVO.getCodigo(), new Date(), Uteis.NIVELMONTARDADOS_COMBOBOX);
		}
		if (!autorizacaoCursoVO.getCodigo().equals(0)) {
			diplomaAlunoRelVO.setAutorizacaoNrRegistroInterno(autorizacaoCursoVO.getNome());
		} else {
			diplomaAlunoRelVO.setAutorizacaoNrRegistroInterno(cursoVO.getNrRegistroInterno());
		}
		if (cursoVO.getNivelEducacional().equals("PO")) {
			if (!autorizacaoCursoVO.getCodigo().equals(0)) {
				diplomaAlunoRelVO.setDataPublicacaoDO(Uteis.getData(autorizacaoCursoVO.getData(), "dd 'de' MMMM 'de' yyyy"));
			} else {
				diplomaAlunoRelVO.setDataPublicacaoDO(Uteis.getData(expedicaoDiplomaVO.getMatricula().getCurso().getDataPublicacaoDO(), "dd 'de' MMMM 'de' yyyy"));
			}
		} else {
			if (!autorizacaoCursoVO.getCodigo().equals(0)) {
				diplomaAlunoRelVO.setDataPublicacaoDO(Uteis.getDataAno4Digitos(autorizacaoCursoVO.getData()));
			} else {
				diplomaAlunoRelVO.setDataPublicacaoDO(Uteis.getDataAno4Digitos(expedicaoDiplomaVO.getMatricula().getCurso().getDataPublicacaoDO()));
			}
		}
		diplomaAlunoRelVO.setAutorizacaoCurso(autorizacaoCursoVO.getNome());
		if (tipoLayout.equals("DiplomaAlunoSuperior5Rel") || tipoLayout.equals("DiplomaAlunoSuperior6Rel")) {
			if (Uteis.isAtributoPreenchido(autorizacaoCursoVO)) {
				diplomaAlunoRelVO.setPrimeiroReconhecimento(autorizacaoCursoVO.getNome());
				dataPrimeiroReconhecimento = Uteis.getData(autorizacaoCursoVO.getData(), "dd/MM/yyyy");
			}
			if (dataReconhecimento.equals(dataPrimeiroReconhecimento)) {
				diplomaAlunoRelVO.setReconhecimento("");
			}
		} else {
			if (Uteis.isAtributoPreenchido(autorizacaoCurso)) {
				autorizacaoCursoVO = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorChavePrimaria(autorizacaoCurso, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
				if (Uteis.isAtributoPreenchido(autorizacaoCursoVO)) {
					diplomaAlunoRelVO.setPrimeiroReconhecimento(autorizacaoCursoVO.getNome());
				}
			}
		}
		diplomaAlunoRelVO.setObservacaoComplementarDiplomaVOs(expedicaoDiplomaVO.getObservacaoComplementarDiplomaVOs());
		autorizacaoCursoVO = null;
	}

}