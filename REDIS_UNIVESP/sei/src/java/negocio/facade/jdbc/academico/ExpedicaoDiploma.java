package negocio.facade.jdbc.academico;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.academico.ExpedicaoDiplomaControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.ColacaoGrauVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.ConfiguracaoHistoricoVO;
import negocio.comuns.academico.ConfiguracaoLayoutHistoricoVO;
import negocio.comuns.academico.ControleLivroFolhaReciboVO;
import negocio.comuns.academico.ControleLivroRegistroDiplomaVO;
import negocio.comuns.academico.CursoCoordenadorVO;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.ExpedicaoDiplomaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.ItemTitulacaoCursoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ObservacaoComplementarDiplomaVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.academico.ProgramacaoFormaturaVO;
import negocio.comuns.academico.RegraEmissaoUnidadeEnsinoVO;
import negocio.comuns.academico.RegraEmissaoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.academico.TitulacaoCursoVO;
import negocio.comuns.academico.enumeradores.AlinhamentoAssinaturaDigitalEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.academico.enumeradores.TipoAutorizacaoEnum;
import negocio.comuns.academico.enumeradores.TipoLivroRegistroDiplomaEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.academico.enumeradores.VersaoDiplomaDigitalEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.ConfiguracaoGedOrigemVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ProvedorDeAssinaturaEnum;
import negocio.comuns.diplomaDigital.versao1_05.TMotivoAnulacao;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.ControleConsultaExpedicaoDiploma;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.UnificadorPDF;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.ExpedicaoDiplomaInterfaceFacade;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.DiplomaAlunoRelVO;
import relatorio.negocio.comuns.academico.HistoricoAlunoDisciplinaRelVO;
import relatorio.negocio.comuns.academico.HistoricoAlunoRelVO;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.academico.HistoricoAlunoRel;
import webservice.aws.s3.ServidorArquivoOnlineS3RS;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>ExpedicaoDiplomaVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>ExpedicaoDiplomaVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see ExpedicaoDiplomaVO
 * @see ControleAcesso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class ExpedicaoDiploma extends ControleAcesso implements ExpedicaoDiplomaInterfaceFacade {

	private static final long serialVersionUID = -1511568006114346393L;

	protected static String idEntidade;

	public ExpedicaoDiploma() throws Exception {
		super();
		setIdEntidade("ExpedicaoDiploma");
	}

	public ExpedicaoDiplomaVO novo() throws Exception {
		ExpedicaoDiploma.incluir(getIdEntidade());
		ExpedicaoDiplomaVO obj = new ExpedicaoDiplomaVO();
		return obj;
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>ExpedicaoDiplomaVO</code>. Todos os tipos de consistência de dados
	 * são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 * 
	 * @throws Exception
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public void validarDados(ExpedicaoDiplomaVO obj, Boolean gerarExcessao, UsuarioVO usuario) throws Exception {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getDataExpedicao() == null) {
			if (gerarExcessao) {
				throw new ConsistirException("O campo DATA (Expedição Diploma) deve ser informado.");
			}
			obj.setPossuiErro(Boolean.TRUE);
			obj.setErro("O campo DATA (Expedição Diploma) deve ser informado.");
		}
		if (!obj.getMatricula().getCurso().getNivelEducacional().equals("SU") && !obj.getMatricula().getCurso().getNivelEducacional().equals("GT") && obj.getVia().equals("")) {
			obj.setVia("1");
		}
		if (!Uteis.isAtributoPreenchido(obj.getVia())) {
			if (gerarExcessao) {
				throw new ConsistirException("O campo VIA (Expedição Diploma) deve ser informado.");
			}
			obj.setPossuiErro(Boolean.TRUE);
			obj.setErro("O campo VIA (Expedição Diploma) deve ser informado.");
		}
		if ((obj.getMatricula().getMatricula() == null) || (obj.getMatricula().getMatricula().equals(""))) {
			if (gerarExcessao) {
				throw new ConsistirException("O campo MATRÍCULA (Expedição Diploma) deve ser informado.");
			}
			obj.setPossuiErro(Boolean.TRUE);
			obj.setErro("O campo MATRÍCULA (Expedição Diploma) deve ser informado.");
		}
		List<ExpedicaoDiplomaVO> listaExpedicao = consultarPorMatriculaAluno(obj.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		String via = "0";
		for (ExpedicaoDiplomaVO expedicao : listaExpedicao) {
			if ((!expedicao.getCodigo().equals(obj.getCodigo())) && (Integer.valueOf(expedicao.getVia()) > Integer.valueOf(via)) && (!expedicao.getAnulado())) {
				via = expedicao.getVia();
			}
		}
		if (!via.equals("0")) {
			if (((Integer.valueOf(obj.getVia()) < Integer.valueOf(via)) || (obj.getVia().equals(via))) && (obj.getMatricula().getCurso().getNivelEducacional().equals("SU"))) {
				if (gerarExcessao) {
					throw new ConsistirException("Já foi gerada a " + via + " via do diploma, favor gerar uma via superior.");
				}
				obj.setPossuiErro(Boolean.TRUE);
				obj.setErro("Já foi gerada a " + via + " via do diploma, favor gerar uma via superior.");
			}
		}
		if ((!obj.getVia().equals("") && !obj.getVia().equals("1")) && (obj.getNumeroRegistroDiplomaViaAnterior().equals("")) && (obj.getMatricula().getCurso().getNivelEducacional().equals("SU"))) {
			if (gerarExcessao) {
				throw new ConsistirException("O campo NÚMERO REGISTRO DIPLOMA (Expedição Diploma) deve ser informado.");
			}
			obj.setPossuiErro(Boolean.TRUE);
			obj.setErro("O campo NÚMERO REGISTRO DIPLOMA (Expedição Diploma) deve ser informado.");
		}
		if ((!obj.getVia().equals("") && !obj.getVia().equals("1")) && (obj.getNumeroProcessoViaAnterior().equals("")) && (obj.getMatricula().getCurso().getNivelEducacional().equals("SU"))) {
			if (gerarExcessao) {
				throw new ConsistirException("O campo NÚMERO PROCESSO (Expedição Diploma) deve ser informado.");

			}
			obj.setPossuiErro(Boolean.TRUE);
			obj.setErro("O campo NÚMERO PROCESSO (Expedição Diploma) deve ser informado.");
		}

		if (!Uteis.isAtributoPreenchido(obj.getGradeCurricularVO())) {
			if (gerarExcessao) {
				throw new ConsistirException("O campo GRADE CURRICULAR (Expedição Diploma) deve ser informado.");

			}
			obj.setPossuiErro(Boolean.TRUE);
			obj.setErro("O campo GRADE CURRICULAR (Expedição Diploma) deve ser informado.");
		}
		if (obj.getTituloFuncionarioPrincipal().length() > 100) {
			if (gerarExcessao) {
				throw new ConsistirException("O campo TITULO DO FUNCIONÁRIO 1 (Expedição Diploma) excedeu o limite de 100 caracteres.");

			}
			obj.setPossuiErro(Boolean.TRUE);
			obj.setErro("O campo TITULO DO FUNCIONÁRIO 1 (Expedição Diploma) excedeu o limite de 100 caracteres.");
		}
		if (obj.getTituloFuncionarioSecundario().length() > 100) {
			if (gerarExcessao) {
				throw new ConsistirException("O campo TITULO DO FUNCIONÁRIO 2 (Expedição Diploma) excedeu o limite de 100 caracteres.");

			}
			obj.setPossuiErro(Boolean.TRUE);
			obj.setErro("O campo TITULO DO FUNCIONÁRIO 2 (Expedição Diploma) excedeu o limite de 100 caracteres.");
		}
		if (obj.getTituloFuncionarioTerceiro().length() > 100) {
			if (gerarExcessao) {
				throw new ConsistirException("O campo TITULO DO FUNCIONÁRIO 3 (Expedição Diploma) excedeu o limite de 100 caracteres.");

			}
			obj.setPossuiErro(Boolean.TRUE);
			obj.setErro("O campo TITULO DO FUNCIONÁRIO 3 (Expedição Diploma) excedeu o limite de 100 caracteres.");
		}

		if (obj.getTituloFuncionarioQuarto().length() > 100) {
			if (gerarExcessao) {
				throw new ConsistirException("O campo TITULO DO FUNCIONÁRIO 4 (Expedição Diploma) excedeu o limite de 100 caracteres.");
			}
			obj.setPossuiErro(Boolean.TRUE);
			obj.setErro("O campo TITULO DO FUNCIONÁRIO 4 (Expedição Diploma) excedeu o limite de 100 caracteres.");
		}

		if (obj.getTituloFuncionarioQuinto().length() > 100) {
			if (gerarExcessao) {
				throw new ConsistirException("O campo TITULO DO FUNCIONÁRIO 5 (Expedição Diploma) excedeu o limite de 100 caracteres.");
			}
			obj.setPossuiErro(Boolean.TRUE);
			obj.setErro("O campo TITULO DO FUNCIONÁRIO 5 (Expedição Diploma) excedeu o limite de 100 caracteres.");
		}
	}

    public void validarImprimirDiploma(ExpedicaoDiplomaVO expedicaoDiploma, Integer codFuncPrincipal, Integer codFuncSecundario, Integer codCargoFuncPrincipal, Integer codCargoFuncSecundario, Integer codFuncTerceiro, Integer codCargoFuncTerceiro, String tipoLayout, UsuarioVO usuario, Boolean gerarXMLDiploma) throws Exception {
        try {
            if (expedicaoDiploma.getMatricula().getMatricula() == null || expedicaoDiploma.getMatricula().getMatricula().equals("")) {
                throw new ConsistirException("O campo matrícula deve ser informado.");
            }
//			if (matricula.getDataInicioCurso() == null) {
//				throw new ConsistirException("A matrícula não possui data de início do curso.");
//			}
//			if (matricula.getDataConclusaoCurso() == null) {
//				throw new ConsistirException("A matrícula não possui data de conclusão do curso.");
//			}
            if (codFuncPrincipal == null || codFuncPrincipal == 0) {
                throw new ConsistirException("O campo Assinatura Funcionário 1 deve ser informado.");
            }
            if ((codFuncSecundario == null || codFuncSecundario == 0) && !gerarXMLDiploma) {
                throw new ConsistirException("O campo Assinatura Funcionário 2 deve ser informado.");
            }
/*			if (!tipoLayout.equals("DiplomaAlunoSuperior5Rel")) {
				if (codFuncTerceiro == null || codFuncTerceiro == 0) {
					throw new ConsistirException("O campo Assinatura Funcionário 3 deve ser informado.");
				}
			}*/
            if (codCargoFuncPrincipal == null || codCargoFuncPrincipal == 0) {
                throw new ConsistirException("O campo Cargo do  Funcionário 1 deve ser informado.");
            }
            if ((codCargoFuncSecundario == null || codCargoFuncSecundario == 0) && !gerarXMLDiploma) {
                throw new ConsistirException("O campo Cargo do  Funcionário 2 deve ser informado.");
            }
/*			if (!tipoLayout.equals("DiplomaAlunoSuperior5Rel")) {
				if (codCargoFuncTerceiro == null || codCargoFuncTerceiro == 0) {
					throw new ConsistirException("O campo Cargo do  Funcionário 3 deve ser informado.");
				}
			}*/
            if (!Uteis.isAtributoPreenchido(expedicaoDiploma.getUnidadeEnsinoCertificadora())) {
            	throw new ConsistirException("O campo Unidade Ensino Expedição Diploma deve ser informado.");
            }
            if (Uteis.isAtributoPreenchido(expedicaoDiploma.getMatricula().getCurso().getModalidadeCurso()) && expedicaoDiploma.getMatricula().getCurso().getModalidadeCurso().equals(ModalidadeDisciplinaEnum.ON_LINE)) {
            	if (!expedicaoDiploma.getUnidadeEnsinoCertificadora().getCredenciamentoEadEmTramitacao()) {
            		if (expedicaoDiploma.getUnidadeEnsinoCertificadora().getCredenciamentoPortariaEAD().equals("")) {
            			throw new ConsistirException("A portaria do credenciamento (EAD) da unidade certificadora deve ser informado.");
            		}
            		if (expedicaoDiploma.getUnidadeEnsinoCertificadora().getDataPublicacaoDOEAD() == null) {
            			throw new ConsistirException("A data de publicação diário oficial (EAD) da unidade certificadora deve ser informado.");
            		}
            	}
            } else {
            	if (!expedicaoDiploma.getUnidadeEnsinoCertificadora().getCredenciamentoEmTramitacao()) {
            		if (expedicaoDiploma.getUnidadeEnsinoCertificadora().getCredenciamentoPortaria().equals("")) {
            			throw new ConsistirException("A portaria do credenciamento (PRESENCIAL) da unidade certificadora deve ser informado.");
            		}
            		if (expedicaoDiploma.getUnidadeEnsinoCertificadora().getDataPublicacaoDO() == null) {
            			throw new ConsistirException("A data de publicação diário oficial (PRESENCIAL) da unidade certificadora deve ser informado.");
            		}
            	}
            }
            if (expedicaoDiploma.getMatricula().getAluno().getDataNasc() == null) {
                throw new ConsistirException("O aluno selecionado não possui data de nascimento cadastrada.");
            }
            if (expedicaoDiploma.getMatricula().getAluno().getNacionalidade() == null || expedicaoDiploma.getMatricula().getAluno().getNacionalidade().getCodigo() == 0) {
                throw new ConsistirException("O aluno selecionado não possui nacionalidade cadastrada.");
            }
            if (expedicaoDiploma.getMatricula().getAluno().getNaturalidade() == null || expedicaoDiploma.getMatricula().getAluno().getNaturalidade().getCodigo() == 0) {
                throw new ConsistirException("O aluno selecionado não possui naturalidade cadastrada.");
            }
            if (!expedicaoDiploma.getMatricula().getSituacao().equals("AT") && !expedicaoDiploma.getMatricula().getSituacao().equals("FO")) {
                throw new ConsistirException("O aluno selecionado não está apto (Situação Matrícula) para gerar diploma/certificado.");
            }            
        } catch (Exception e) {
            throw e;
        }
    }
	
        
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ExpedicaoDiplomaVO obj, UsuarioVO usuario) throws Exception {
		incluir(obj, usuario, true, true);
    }

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ExpedicaoDiplomaVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ExpedicaoDiplomaVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ExpedicaoDiplomaVO obj, UsuarioVO usuario, Boolean validarDados, Boolean alterarObsMatricula) throws Exception {
		try {
			if (validarDados) {
				validarDados(obj,true , usuario);
                if (obj.getMatricula().getCurso().getNivelEducacional().equals("PO")) {
					validarConclusaoPos(obj, usuario);
					incluirDataConclusaoCursoMatricula(obj, usuario);
                }
            }
            if (alterarObsMatricula) {
				getFacadeFactory().getMatriculaFacade().alterarObservacaoDiplomaMatricula(obj.getMatricula().getMatricula(), obj.getMatricula().getObservacaoDiploma(), usuario);
            }
			ExpedicaoDiploma.incluir(getIdEntidade(), true, usuario);
			obj.realizarUpperCaseDados();
			obj.setResponsavelCadastro(usuario);
			obj.setDataCadastro(new Date());
			StringBuilder sql = new StringBuilder();
			sql.append(" INSERT INTO ExpedicaoDiploma( dataExpedicao, matricula, via, gradecurricular, numeroprocessoviaanterior, numeroregistrodiplomaviaanterior, unidadeensinocertificadora, utilizarUnidadeMatriz, funcionarioPrimario, funcionarioSecundario, funcionarioTerceiro, layoutDiploma , cargoFuncionarioPrincipal , cargoFuncionarioSecundario , cargoFuncionarioTerceiro, tituloFuncionarioPrincipal, tituloFuncionarioSecundario, tituloFuncionarioTerceiro, numeroProcesso, dataRegistroDiplomaViaAnterior, reitorRegistroDiplomaViaAnterior, secretariaRegistroDiplomaViaAnterior, cargoReitorRegistroDiplomaViaAnterior , titulacaoMasculinoApresentarDiploma, titulacaoFemininoApresentarDiploma, dataPublicacaoDiarioOficial, serial, observacao , numeroRegistroDiploma, funcionarioQuarto, cargoFuncionarioQuarto, tituloFuncionarioQuarto, funcionarioQuinto, cargoFuncionarioQuinto, tituloFuncionarioQuinto, informarCamposLivroRegistradora, nrLivroRegistradora, nrFolhaReciboRegistradora, gerarXMLDiploma, codigoValidacaoDiplomaDigital, anulado, textopadrao, dataRegistroDiploma, emitidoPorProcessoTransferenciaAssistida, nomeIesPTA, cnpjPTA, codigoMecPTA, cepPTA, cidadePTA, logradouroPTA, numeroPTA, complementoPTA, bairroPTA, tipoDescredenciamentoPTA, numeroDescredenciamentoPTA, dataDescredenciamentoPTA, dataPublicacaoDescredenciamentoPTA, veiculoPublicacaoDescredenciamentoPTA, secaoPublicacaoDescredenciamentoPTA, paginaPublicacaoDescredenciamentoPTA, numeroDOUDescredenciamentoPTA, emitidoPorDecisaoJudicial, nomeJuizDecisaoJudicial, numeroProcessoDecisaoJudicial, decisaoJudicial, informacoesAdicionaisDecisaoJudicial, versaoDiploma, dataCadastro, responsavelCadastro)");
			sql.append(" VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
			sql.append(" returning codigo").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getDataExpedicao()));
					if (!obj.getMatricula().getMatricula().equals("")) {
						sqlInserir.setString(2, obj.getMatricula().getMatricula());
					} else {
						sqlInserir.setNull(2, 0);
					}
					sqlInserir.setString(3, obj.getVia());
					sqlInserir.setInt(4, obj.getGradeCurricularVO().getCodigo());
					sqlInserir.setString(5, obj.getNumeroProcessoViaAnterior());
					sqlInserir.setString(6, obj.getNumeroRegistroDiplomaViaAnterior());
					if (obj.getUnidadeEnsinoCertificadora().getCodigo() != null && obj.getUnidadeEnsinoCertificadora().getCodigo() != 0) {
						sqlInserir.setInt(7, obj.getUnidadeEnsinoCertificadora().getCodigo());
					} else {
						sqlInserir.setNull(7, 0);
					}
					sqlInserir.setBoolean(8, obj.getUtilizarUnidadeMatriz());
					if (Uteis.isAtributoPreenchido(obj.getFuncionarioPrimarioVO())) {
						sqlInserir.setInt(9, obj.getFuncionarioPrimarioVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(9, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getFuncionarioSecundarioVO())) {
						sqlInserir.setInt(10, obj.getFuncionarioSecundarioVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(10, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getFuncionarioTerceiroVO())) {
						sqlInserir.setInt(11, obj.getFuncionarioTerceiroVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(11, 0);
					}
					sqlInserir.setString(12, obj.getLayoutDiploma());
					if (Uteis.isAtributoPreenchido(obj.getCargoFuncionarioPrincipalVO())) {
						sqlInserir.setInt(13, obj.getCargoFuncionarioPrincipalVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(13, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getCargoFuncionarioSecundarioVO())) {
						sqlInserir.setInt(14, obj.getCargoFuncionarioSecundarioVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(14, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getCargoFuncionarioTerceiroVO())) {
						sqlInserir.setInt(15, obj.getCargoFuncionarioTerceiroVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(15, 0);
					}
					sqlInserir.setString(16, obj.getTituloFuncionarioPrincipal());
					sqlInserir.setString(17, obj.getTituloFuncionarioSecundario());
					sqlInserir.setString(18, obj.getTituloFuncionarioTerceiro());
					sqlInserir.setString(19, obj.getNumeroProcesso());					
					sqlInserir.setDate(20, Uteis.getDataJDBC(obj.getDataRegistroDiplomaViaAnterior()));
					if (obj.getReitorRegistroDiplomaViaAnterior() != null && obj.getReitorRegistroDiplomaViaAnterior().getCodigo() != 0) {
						sqlInserir.setInt(21, obj.getReitorRegistroDiplomaViaAnterior().getCodigo());
					} else {
						sqlInserir.setNull(21, 0);
					}
					if (obj.getSecretariaRegistroDiplomaViaAnterior() != null && obj.getSecretariaRegistroDiplomaViaAnterior().getCodigo() != 0) {
						sqlInserir.setInt(22, obj.getSecretariaRegistroDiplomaViaAnterior().getCodigo());
					} else {
						sqlInserir.setNull(22, 0);
					}
					if (obj.getCargoReitorRegistroDiplomaViaAnterior() != null && obj.getCargoReitorRegistroDiplomaViaAnterior().getCodigo() != 0) {
						sqlInserir.setInt(23, obj.getCargoReitorRegistroDiplomaViaAnterior().getCodigo());
					} else {
						sqlInserir.setNull(23, 0);
					}		
					sqlInserir.setString(24, obj.getTitulacaoMasculinoApresentarDiploma());
					sqlInserir.setString(25, obj.getTitulacaoFemininoApresentarDiploma());
					Uteis.setValuePreparedStatement(obj.getDataPublicacaoDiarioOficial(), Types.DATE, 26, sqlInserir);
					sqlInserir.setString(27, obj.getSerial());		
					sqlInserir.setString(28, obj.getObservacao());		
					sqlInserir.setString(29, obj.getNumeroRegistroDiploma());
                    if (Uteis.isAtributoPreenchido(obj.getFuncionarioQuartoVO())) {
                    	sqlInserir.setInt(30, obj.getFuncionarioQuartoVO().getCodigo().intValue());
                    } else {
                    	sqlInserir.setNull(30, 0);
                    }
                    if (Uteis.isAtributoPreenchido(obj.getCargoFuncionarioQuartoVO())) {
                    	sqlInserir.setInt(31, obj.getCargoFuncionarioQuartoVO().getCodigo().intValue());
                    } else {
                    	sqlInserir.setNull(31, 0);
                    }
                    sqlInserir.setString(32, obj.getTituloFuncionarioQuarto());
                    if (Uteis.isAtributoPreenchido(obj.getFuncionarioQuintoVO())) {
                    	sqlInserir.setInt(33, obj.getFuncionarioQuintoVO().getCodigo().intValue());
                    } else {
                    	sqlInserir.setNull(33, 0);
                    }
                    if (Uteis.isAtributoPreenchido(obj.getCargoFuncionarioQuintoVO())) {
                    	sqlInserir.setInt(34, obj.getCargoFuncionarioQuintoVO().getCodigo().intValue());
                    } else {
                    	sqlInserir.setNull(34, 0);
                    }
                    sqlInserir.setString(35, obj.getTituloFuncionarioQuinto());
                    if (obj.getInformarCamposLivroRegistradora() != null) {
                        sqlInserir.setBoolean(36, obj.getInformarCamposLivroRegistradora());
                    } else {
                        sqlInserir.setNull(36, 0);
                    }
                    if (obj.getLivroRegistradora() != null) {
                        sqlInserir.setString(37, obj.getLivroRegistradora());
                    } else {
                        sqlInserir.setNull(37, 0);
                    }
                    if (obj.getFolhaReciboRegistradora() != null) {
                        sqlInserir.setString(38, obj.getFolhaReciboRegistradora());
                    } else {
                        sqlInserir.setNull(38, 0);
                    }
                    int i = 38;
                    sqlInserir.setBoolean(++i, obj.getGerarXMLDiploma());
                    if (obj.getCodigoValidacaoDiplomaDigital() != null) {
                        sqlInserir.setString(++i, obj.getCodigoValidacaoDiplomaDigital());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    sqlInserir.setBoolean(++i, obj.getAnulado());
                    if (Uteis.isAtributoPreenchido(obj.getTextoPadrao().getCodigo())) {
                    	sqlInserir.setInt(++i, obj.getTextoPadrao().getCodigo());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    Uteis.setValuePreparedStatement(obj.getDataRegistroDiploma(), Types.DATE, ++i, sqlInserir);
                    sqlInserir.setBoolean(++i, obj.getEmitidoPorProcessoTransferenciaAssistida());
                    if (obj.getEmitidoPorProcessoTransferenciaAssistida()) {
                    	sqlInserir.setString(++i, obj.getNomeIesPTA());
                    	sqlInserir.setString(++i, obj.getCnpjPTA());
                    	sqlInserir.setInt(++i, obj.getCodigoMecPTA());
                    	sqlInserir.setString(++i, obj.getCepPTA());
                    	if (Uteis.isAtributoPreenchido(obj.getCidadePTA())) {
                    		sqlInserir.setInt(++i, obj.getCidadePTA().getCodigo());
                    	} else {
                    		sqlInserir.setNull(++i, 0);	
                    	}
                    	sqlInserir.setString(++i, obj.getLogradouroPTA());
                    	sqlInserir.setString(++i, obj.getNumeroPTA());
                    	sqlInserir.setString(++i, obj.getComplementoPTA());
                    	sqlInserir.setString(++i, obj.getBairroPTA());
                    	sqlInserir.setString(++i, obj.getTipoDescredenciamentoPTA().name());
                    	sqlInserir.setString(++i, obj.getNumeroDescredenciamentoPTA());
                    	if (obj.getDataDescredenciamentoPTA() != null) {
                    		sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataDescredenciamentoPTA()));
                    	} else {
                    		sqlInserir.setNull(++i, 0);
                    	}
                    	if (obj.getDataPublicacaoDescredenciamentoPTA() != null) {
                    		sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataPublicacaoDescredenciamentoPTA()));
                    	} else {
                    		sqlInserir.setNull(++i, 0);
                    	}
                    	sqlInserir.setString(++i, obj.getVeiculoPublicacaoDescredenciamentoPTA());
                    	sqlInserir.setInt(++i, obj.getSecaoPublicacaoDescredenciamentoPTA());
                    	sqlInserir.setInt(++i, obj.getPaginaPublicacaoDescredenciamentoPTA());
                    	sqlInserir.setInt(++i, obj.getNumeroDOUDescredenciamentoPTA());
                    } else {
                    	sqlInserir.setNull(++i, 0);
                    	sqlInserir.setNull(++i, 0);
                    	sqlInserir.setNull(++i, 0);
                    	sqlInserir.setNull(++i, 0);
                    	sqlInserir.setNull(++i, 0);
                    	sqlInserir.setNull(++i, 0);
                    	sqlInserir.setNull(++i, 0);
                    	sqlInserir.setNull(++i, 0);
                    	sqlInserir.setNull(++i, 0);
                    	sqlInserir.setNull(++i, 0);
                    	sqlInserir.setNull(++i, 0);
                    	sqlInserir.setNull(++i, 0);
                    	sqlInserir.setNull(++i, 0);
                    	sqlInserir.setNull(++i, 0);
                    	sqlInserir.setNull(++i, 0);
                    	sqlInserir.setNull(++i, 0);
                    	sqlInserir.setNull(++i, 0);
                    }
                    sqlInserir.setBoolean(++i, obj.getEmitidoPorDecisaoJudicial());
                    if (obj.getEmitidoPorDecisaoJudicial()) {
                    	sqlInserir.setString(++i, obj.getNomeJuizDecisaoJudicial());
                    	sqlInserir.setString(++i, obj.getNumeroProcessoDecisaoJudicial());
                    	sqlInserir.setString(++i, obj.getDecisaoJudicial());
                    	sqlInserir.setString(++i, obj.getInformacoesAdicionaisDecisaoJudicial());
                    } else {
                    	sqlInserir.setNull(++i, 0);
                    	sqlInserir.setNull(++i, 0);
                    	sqlInserir.setNull(++i, 0);
                    	sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getVersaoDiploma().getValor() != null) {
                    	sqlInserir.setString(++i, obj.getVersaoDiploma().getValor());
                    } else {
                    	sqlInserir.setNull(++i, 0);
                    }
                    sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataCadastro()));
                    sqlInserir.setInt(++i, obj.getResponsavelCadastro().getCodigo());
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
            getFacadeFactory().getObservacaoComplementarDiplomaFacade().incluirObservacaoComplementarDiplomas(obj.getCodigo(), obj.getObservacaoComplementarDiplomaVOs());
            getFacadeFactory().getDeclaracaoAcercaProcessoJudicialInterfaceFacade().persistir(obj.getDeclaracaoAcercaProcessoJudicialVOs(), usuario);
  			obj.setNovoObj(Boolean.FALSE);

		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ExpedicaoDiplomaVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ExpedicaoDiplomaVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ExpedicaoDiplomaVO obj, UsuarioVO usuario, Boolean controlarAcesso) throws Exception {
		try {
			validarDados(obj, true ,usuario);
			if (obj.getMatricula().getCurso().getNivelEducacional().equals("PO")) {
				validarConclusaoPos(obj, usuario);
			}
			getFacadeFactory().getMatriculaFacade().alterarObservacaoDiplomaMatricula(obj.getMatricula().getMatricula(), obj.getMatricula().getObservacaoDiploma(), usuario);
			ExpedicaoDiploma.alterar(getIdEntidade(), controlarAcesso, usuario);
			obj.realizarUpperCaseDados();
			final String sql = "UPDATE ExpedicaoDiploma set dataExpedicao=?, matricula=?, via=?, gradecurricular=?, numeroprocessoviaanterior=?, numeroregistrodiplomaviaanterior=?,unidadeensinocertificadora=?,utilizarUnidadeMatriz=?, numeroProcesso=?, dataRegistroDiplomaViaAnterior=?, reitorRegistroDiplomaViaAnterior=?, " +
					"secretariaRegistroDiplomaViaAnterior=?, cargoReitorRegistroDiplomaViaAnterior=? , titulacaoMasculinoApresentarDiploma=?, titulacaoFemininoApresentarDiploma=?, titulofuncionarioprincipal=?, titulofuncionariosecundario=?, titulofuncionarioterceiro=?, "+
					"funcionarioprimario=?, funcionariosecundario=?, funcionarioterceiro=?, cargofuncionarioprincipal=?, cargofuncionariosecundario=?, cargofuncionarioterceiro=?, layoutdiploma=?, dataPublicacaoDiarioOficial=?, serial=?, observacao=? , numeroRegistroDiploma=?, titulofuncionarioquarto=?, funcionarioquarto=?, cargofuncionarioquarto=?, titulofuncionarioquinto=?, funcionarioquinto=?, cargofuncionarioquinto=?, informarCamposLivroRegistradora=?, nrLivroRegistradora=?, nrFolhaReciboRegistradora=?, gerarXMLDiploma=?, codigoValidacaoDiplomaDigital=?, anulado=?, textopadrao=?, dataRegistroDiploma=?, "
					+ "emitidoPorProcessoTransferenciaAssistida=?, nomeIesPTA=?, cnpjPTA=?, codigoMecPTA=?, cepPTA=?, cidadePTA=?, logradouroPTA=?, numeroPTA=?, complementoPTA=?, bairroPTA=?, tipoDescredenciamentoPTA=?, numeroDescredenciamentoPTA=?, dataDescredenciamentoPTA=?, dataPublicacaoDescredenciamentoPTA=?, veiculoPublicacaoDescredenciamentoPTA=?, secaoPublicacaoDescredenciamentoPTA=?, paginaPublicacaoDescredenciamentoPTA=?, numeroDOUDescredenciamentoPTA=?, emitidoPorDecisaoJudicial=?, nomeJuizDecisaoJudicial=?, numeroProcessoDecisaoJudicial=?, decisaoJudicial=?, informacoesAdicionaisDecisaoJudicial=?, versaoDiploma=?" + " WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getDataExpedicao()));
					if (!obj.getMatricula().getMatricula().equals("")) {
						sqlAlterar.setString(2, obj.getMatricula().getMatricula());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					sqlAlterar.setString(3, obj.getVia());
					sqlAlterar.setInt(4, obj.getGradeCurricularVO().getCodigo());
					sqlAlterar.setString(5, obj.getNumeroProcessoViaAnterior());
					sqlAlterar.setString(6, obj.getNumeroRegistroDiplomaViaAnterior());
					if (obj.getUnidadeEnsinoCertificadora().getCodigo() != null && obj.getUnidadeEnsinoCertificadora().getCodigo() != 0) {
						sqlAlterar.setInt(7, obj.getUnidadeEnsinoCertificadora().getCodigo());
					} else {
						sqlAlterar.setNull(7, 0);
					}
					sqlAlterar.setBoolean(8, obj.getUtilizarUnidadeMatriz());
					sqlAlterar.setString(9, obj.getNumeroProcesso());
					sqlAlterar.setDate(10, Uteis.getDataJDBC(obj.getDataRegistroDiplomaViaAnterior()));
					if (obj.getReitorRegistroDiplomaViaAnterior() != null && obj.getReitorRegistroDiplomaViaAnterior().getCodigo() != 0) {
						sqlAlterar.setInt(11, obj.getReitorRegistroDiplomaViaAnterior().getCodigo());
					} else {
						sqlAlterar.setNull(11, 0);
					}
					if (obj.getSecretariaRegistroDiplomaViaAnterior() != null && obj.getSecretariaRegistroDiplomaViaAnterior().getCodigo() != 0) {
						sqlAlterar.setInt(12, obj.getSecretariaRegistroDiplomaViaAnterior().getCodigo());
					} else {
						sqlAlterar.setNull(12, 0);
					}
					if (obj.getCargoReitorRegistroDiplomaViaAnterior() != null && obj.getCargoReitorRegistroDiplomaViaAnterior().getCodigo() != 0) {
						sqlAlterar.setInt(13, obj.getCargoReitorRegistroDiplomaViaAnterior().getCodigo());
					} else {
						sqlAlterar.setNull(13, 0);
					}
					sqlAlterar.setString(14, obj.getTitulacaoMasculinoApresentarDiploma());
					sqlAlterar.setString(15, obj.getTitulacaoFemininoApresentarDiploma());
					sqlAlterar.setString(16, obj.getTituloFuncionarioPrincipal());
					sqlAlterar.setString(17, obj.getTituloFuncionarioSecundario());
					sqlAlterar.setString(18, obj.getTituloFuncionarioTerceiro());
					if (obj.getFuncionarioPrimarioVO().getCodigo().equals(0)) {
						sqlAlterar.setNull(19, 0);
					} else {
						sqlAlterar.setInt(19, obj.getFuncionarioPrimarioVO().getCodigo());
					}
					if (obj.getFuncionarioSecundarioVO().getCodigo().equals(0)) {
						sqlAlterar.setNull(20, 0);
					} else {
						sqlAlterar.setInt(20, obj.getFuncionarioSecundarioVO().getCodigo());
					}
					if (obj.getFuncionarioTerceiroVO().getCodigo().equals(0)) {
						sqlAlterar.setNull(21, 0);
					} else {
						sqlAlterar.setInt(21, obj.getFuncionarioTerceiroVO().getCodigo());
					}
					if (obj.getCargoFuncionarioPrincipalVO().getCodigo().equals(0)) {
						sqlAlterar.setNull(22, 0);
					} else {
						sqlAlterar.setInt(22, obj.getCargoFuncionarioPrincipalVO().getCodigo());
					}
					if (obj.getCargoFuncionarioSecundarioVO().getCodigo().equals(0)) {
						sqlAlterar.setNull(23, 0);
					} else {
						sqlAlterar.setInt(23, obj.getCargoFuncionarioSecundarioVO().getCodigo());
					}
					if (obj.getCargoFuncionarioTerceiroVO().getCodigo().equals(0)) {
						sqlAlterar.setNull(24, 0);
					} else {
						sqlAlterar.setInt(24, obj.getCargoFuncionarioTerceiroVO().getCodigo());
					}
					sqlAlterar.setString(25, obj.getLayoutDiploma());
					Uteis.setValuePreparedStatement(obj.getDataPublicacaoDiarioOficial(), Types.DATE, 26, sqlAlterar);					
					sqlAlterar.setString(27, obj.getSerial());
					sqlAlterar.setString(28, obj.getObservacao());
					sqlAlterar.setString(29, obj.getNumeroRegistroDiploma());
                    sqlAlterar.setString(30, obj.getTituloFuncionarioQuarto());
                    if (Uteis.isAtributoPreenchido(obj.getFuncionarioQuartoVO())) {
                    	sqlAlterar.setInt(31, obj.getFuncionarioQuartoVO().getCodigo().intValue());
                    } else {
                    	sqlAlterar.setNull(31, 0);
                    }
                    if (Uteis.isAtributoPreenchido(obj.getCargoFuncionarioQuartoVO())) {
                    	sqlAlterar.setInt(32, obj.getCargoFuncionarioQuartoVO().getCodigo().intValue());
                    } else {
                    	sqlAlterar.setNull(32, 0);
                    }
                    sqlAlterar.setString(33, obj.getTituloFuncionarioQuinto());
                    if (Uteis.isAtributoPreenchido(obj.getFuncionarioQuintoVO())) {
                    	sqlAlterar.setInt(34, obj.getFuncionarioQuintoVO().getCodigo().intValue());
                    } else {
                    	sqlAlterar.setNull(34, 0);
                    }
                    if (Uteis.isAtributoPreenchido(obj.getCargoFuncionarioQuintoVO())) {
                    	sqlAlterar.setInt(35, obj.getCargoFuncionarioQuintoVO().getCodigo().intValue());
                    } else {
                    	sqlAlterar.setNull(35, 0);
                    }
                    if (obj.getInformarCamposLivroRegistradora() == null) {
                        sqlAlterar.setNull(36, 0);
                    } else {
                        sqlAlterar.setBoolean(36, obj.getInformarCamposLivroRegistradora());
                    }
                    if (obj.getLivroRegistradora() == null) {
                        sqlAlterar.setNull(37, 0);
                    } else {
                        sqlAlterar.setString(37, obj.getLivroRegistradora());
                    }
                    if (obj.getFolhaReciboRegistradora() == null) {
                        sqlAlterar.setNull(38, 0);
                    } else {
                        sqlAlterar.setString(38, obj.getFolhaReciboRegistradora());
                    }      
                    int i = 38;
                    sqlAlterar.setBoolean(++i, obj.getGerarXMLDiploma());
                    if (obj.getCodigoValidacaoDiplomaDigital() == null) {
                        sqlAlterar.setNull(++i, 0);
                    } else {
                        sqlAlterar.setString(++i, obj.getCodigoValidacaoDiplomaDigital());
                    }
                    sqlAlterar.setBoolean(++i, obj.getAnulado());
                    if (Uteis.isAtributoPreenchido(obj.getTextoPadrao().getCodigo())) {
                    	sqlAlterar.setInt(++i, obj.getTextoPadrao().getCodigo());
                    } else {
                    	sqlAlterar.setNull(++i, 0);
                    }
                    Uteis.setValuePreparedStatement(obj.getDataRegistroDiploma(), Types.DATE, ++i, sqlAlterar);
                    sqlAlterar.setBoolean(++i, obj.getEmitidoPorProcessoTransferenciaAssistida());
                    if (obj.getEmitidoPorProcessoTransferenciaAssistida()) {
                    	sqlAlterar.setString(++i, obj.getNomeIesPTA());
                    	sqlAlterar.setString(++i, obj.getCnpjPTA());
                    	sqlAlterar.setInt(++i, obj.getCodigoMecPTA());
                    	sqlAlterar.setString(++i, obj.getCepPTA());
                    	if (Uteis.isAtributoPreenchido(obj.getCidadePTA())) {
                    		sqlAlterar.setInt(++i, obj.getCidadePTA().getCodigo());
                    	} else {
                    		sqlAlterar.setNull(++i, 0);	
                    	}
                    	sqlAlterar.setString(++i, obj.getLogradouroPTA());
                    	sqlAlterar.setString(++i, obj.getNumeroPTA());
                    	sqlAlterar.setString(++i, obj.getComplementoPTA());
                    	sqlAlterar.setString(++i, obj.getBairroPTA());
                    	sqlAlterar.setString(++i, obj.getTipoDescredenciamentoPTA().name());
                    	sqlAlterar.setString(++i, obj.getNumeroDescredenciamentoPTA());
                    	if (obj.getDataDescredenciamentoPTA() != null) {
                    		sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataDescredenciamentoPTA()));
                    	} else {
                    		sqlAlterar.setNull(++i, 0);
                    	}
                    	if (obj.getDataPublicacaoDescredenciamentoPTA() != null) {
                    		sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataPublicacaoDescredenciamentoPTA()));
                    	} else {
                    		sqlAlterar.setNull(++i, 0);
                    	}
                    	sqlAlterar.setString(++i, obj.getVeiculoPublicacaoDescredenciamentoPTA());
                    	sqlAlterar.setInt(++i, obj.getSecaoPublicacaoDescredenciamentoPTA());
                    	sqlAlterar.setInt(++i, obj.getPaginaPublicacaoDescredenciamentoPTA());
                    	sqlAlterar.setInt(++i, obj.getNumeroDOUDescredenciamentoPTA());
                    } else {
                    	sqlAlterar.setNull(++i, 0);
                    	sqlAlterar.setNull(++i, 0);
                    	sqlAlterar.setNull(++i, 0);
                    	sqlAlterar.setNull(++i, 0);
                    	sqlAlterar.setNull(++i, 0);
                    	sqlAlterar.setNull(++i, 0);
                    	sqlAlterar.setNull(++i, 0);
                    	sqlAlterar.setNull(++i, 0);
                    	sqlAlterar.setNull(++i, 0);
                    	sqlAlterar.setNull(++i, 0);
                    	sqlAlterar.setNull(++i, 0);
                    	sqlAlterar.setNull(++i, 0);
                    	sqlAlterar.setNull(++i, 0);
                    	sqlAlterar.setNull(++i, 0);
                    	sqlAlterar.setNull(++i, 0);
                    	sqlAlterar.setNull(++i, 0);
                    	sqlAlterar.setNull(++i, 0);
                    }
                    sqlAlterar.setBoolean(++i, obj.getEmitidoPorDecisaoJudicial());
                    if (obj.getEmitidoPorDecisaoJudicial()) {
                    	sqlAlterar.setString(++i, obj.getNomeJuizDecisaoJudicial());
                    	sqlAlterar.setString(++i, obj.getNumeroProcessoDecisaoJudicial());
                    	sqlAlterar.setString(++i, obj.getDecisaoJudicial());
                    	sqlAlterar.setString(++i, obj.getInformacoesAdicionaisDecisaoJudicial());
                    } else {
                    	sqlAlterar.setNull(++i, 0);
                    	sqlAlterar.setNull(++i, 0);
                    	sqlAlterar.setNull(++i, 0);
                    	sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getVersaoDiploma().getValor() != null) {
                    	sqlAlterar.setString(++i, obj.getVersaoDiploma().getValor());
                    } else {
                    	sqlAlterar.setNull(++i, 0);
                    }
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			});
            getFacadeFactory().getObservacaoComplementarDiplomaFacade().alterarObservacaoComplementarDiplomas(obj.getCodigo(), obj.getObservacaoComplementarDiplomaVOs());
            if (obj.getEmitidoPorDecisaoJudicial()) {
            	getFacadeFactory().getDeclaracaoAcercaProcessoJudicialInterfaceFacade().persistir(obj.getDeclaracaoAcercaProcessoJudicialVOs(), usuario);
            } else {
            	getFacadeFactory().getDeclaracaoAcercaProcessoJudicialInterfaceFacade().excluirPorExpedicaoDiploma(obj.getCodigo(), usuario);
            }
            obj.setNovoObj(Boolean.FALSE);

		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDadosBasicosPrimeiraVia(final ExpedicaoDiplomaVO obj, UsuarioVO usuario, Boolean controlarAcesso) throws Exception {
		try {
			final String sql = "UPDATE ExpedicaoDiploma set numeroprocessoviaanterior=?, numeroregistrodiplomaviaanterior=?, dataRegistroDiplomaViaAnterior=?, reitorRegistroDiplomaViaAnterior=?, secretariaRegistroDiplomaViaAnterior=?, cargoReitorRegistroDiplomaViaAnterior=?,  WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getNumeroProcessoViaAnterior());
					sqlAlterar.setString(2, obj.getNumeroRegistroDiplomaViaAnterior());
					sqlAlterar.setDate(3, Uteis.getDataJDBC(obj.getDataRegistroDiplomaViaAnterior()));
					if (obj.getReitorRegistroDiplomaViaAnterior() != null && obj.getReitorRegistroDiplomaViaAnterior().getCodigo() != 0) {
						sqlAlterar.setInt(4, obj.getReitorRegistroDiplomaViaAnterior().getCodigo());
					} else {
						sqlAlterar.setNull(4, 0);
					}
					if (obj.getSecretariaRegistroDiplomaViaAnterior() != null && obj.getSecretariaRegistroDiplomaViaAnterior().getCodigo() != 0) {
						sqlAlterar.setInt(5, obj.getSecretariaRegistroDiplomaViaAnterior().getCodigo());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					if (obj.getCargoReitorRegistroDiplomaViaAnterior() != null && obj.getCargoReitorRegistroDiplomaViaAnterior().getCodigo() != 0) {
						sqlAlterar.setInt(6, obj.getCargoReitorRegistroDiplomaViaAnterior().getCodigo());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					sqlAlterar.setInt(7, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarFuncionarioResponsavel(final ExpedicaoDiplomaVO obj, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE ExpedicaoDiploma set funcionarioPrimario=?, funcionarioSecundario=?, funcionarioTerceiro=?, layoutDiploma = ?, cargoFuncionarioPrincipal = ?, cargoFuncionarioSecundario = ?, cargoFuncionarioTerceiro = ?, tituloFuncionarioPrincipal=?, tituloFuncionarioSecundario=?, tituloFuncionarioTerceiro=?, serial=?, observacao=?, funcionarioQuarto=?, cargoFuncionarioQuarto = ?, tituloFuncionarioQuarto=?, funcionarioQuinto=?, cargoFuncionarioQuinto = ?, tituloFuncionarioQuinto=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (obj.getFuncionarioPrimarioVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getFuncionarioPrimarioVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					if (obj.getFuncionarioSecundarioVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(2, obj.getFuncionarioSecundarioVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					if (obj.getFuncionarioTerceiroVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(3, obj.getFuncionarioTerceiroVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					sqlAlterar.setString(4, obj.getLayoutDiploma());
					if (obj.getCargoFuncionarioPrincipalVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(5, obj.getCargoFuncionarioPrincipalVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					if (obj.getCargoFuncionarioSecundarioVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(6, obj.getCargoFuncionarioSecundarioVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					if (obj.getCargoFuncionarioTerceiroVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(7, obj.getCargoFuncionarioTerceiroVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(7, 0);
					}
					sqlAlterar.setString(8, obj.getTituloFuncionarioPrincipal());
					sqlAlterar.setString(9, obj.getTituloFuncionarioSecundario());
					sqlAlterar.setString(10, obj.getTituloFuncionarioTerceiro());
					sqlAlterar.setString(11, obj.getSerial());
					sqlAlterar.setString(12, obj.getObservacao());
                    if (Uteis.isAtributoPreenchido(obj.getFuncionarioQuartoVO())) {
                    	sqlAlterar.setInt(13, obj.getFuncionarioQuartoVO().getCodigo().intValue());
                    } else {
                    	sqlAlterar.setNull(13, 0);
                    }
                    if (Uteis.isAtributoPreenchido(obj.getCargoFuncionarioQuartoVO())) {
                    	sqlAlterar.setInt(14, obj.getCargoFuncionarioQuartoVO().getCodigo().intValue());
                    } else {
                    	sqlAlterar.setNull(14, 0);
                    }
                    sqlAlterar.setString(15, obj.getTituloFuncionarioQuarto());
                    if (Uteis.isAtributoPreenchido(obj.getFuncionarioQuintoVO())) {
                    	sqlAlterar.setInt(16, obj.getFuncionarioQuintoVO().getCodigo().intValue());
                    } else {
                    	sqlAlterar.setNull(16, 0);
                    }
                    if (Uteis.isAtributoPreenchido(obj.getCargoFuncionarioQuintoVO())) {
                    	sqlAlterar.setInt(17, obj.getCargoFuncionarioQuintoVO().getCodigo().intValue());
                    } else {
                    	sqlAlterar.setNull(17, 0);
                    }
                    sqlAlterar.setString(18, obj.getTituloFuncionarioQuinto());
					sqlAlterar.setInt(19, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ExpedicaoDiplomaVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ExpedicaoDiplomaVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ExpedicaoDiplomaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			ExpedicaoDiploma.excluir(getIdEntidade(), true, usuario);
			getFacadeFactory().getDocumentoAssinadoFacade().verificarExpedicaoDiplomaContemDocumentoAssinado(obj);
			getFacadeFactory().getDocumentoAssinadoFacade().realizarExclusaoDocumentoAssinadoVinculadoExpedicaoDiploma(obj, usuario, configuracaoGeralSistemaVO);
            getFacadeFactory().getObservacaoComplementarDiplomaFacade().excluirObservacaoComplementarDiplomas(obj.getCodigo());
            getFacadeFactory().getDeclaracaoAcercaProcessoJudicialInterfaceFacade().excluirPorExpedicaoDiploma(obj.getCodigo(), usuario);
//            if (!this.verificarExisteRegistroExpedicaoDiplomaDiferenteAtual(obj)) {
//				 this.executarExclusaoDadosColacaoGrauAluno(obj.getMatricula(),usuario);
//			}
			String sql = "DELETE FROM ExpedicaoDiploma WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Método que verifica se o Aluno de Pos Graduação conclui o curso
	 * 
	 * @return
	 * @throws Exception
	 */
	public void validarConclusaoPos(ExpedicaoDiplomaVO obj, UsuarioVO usuario) throws Exception {
		if (!getFacadeFactory().getMatriculaFacade().isMatriculaIntegralizada(obj.getMatricula(), obj.getGradeCurricularVO().getCodigo(), usuario, null)) {
			throw new ConsistirException("O aluno não concluiu a quantidade de disciplinas mínima para conclusão do curso ( Sendo disciplina OPTATIVA E/OU OBRIGATÓRIA ).");
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>ExpedicaoDiploma</code>
	 * através do valor do atributo <code>String via</code>. Retorna os objetos,
	 * com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ExpedicaoDiplomaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ExpedicaoDiplomaVO> consultarPorVia(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT ExpedicaoDiploma.*, pessoa.nome as nomepessoa  FROM ExpedicaoDiploma INNER JOIN matricula ON matricula.matricula = expedicaodiploma.matricula inner join pessoa on pessoa.codigo = matricula.aluno WHERE upper( via ) like('" + valorConsulta.toUpperCase() 
				+ "%') ";
		if (usuario.getUnidadeEnsinoLogado().getCodigo() > 0) {
			sqlStr +=  " and matricula.unidadeensino = " + usuario.getUnidadeEnsinoLogado().getCodigo();
		}
		sqlStr += " ORDER BY via";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ExpedicaoDiploma</code>
	 * através do valor do atributo <code>matricula</code> da classe
	 * <code>Matricula</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ExpedicaoDiplomaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ExpedicaoDiplomaVO> consultarPorMatriculaMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT ExpedicaoDiploma.*, pessoa.nome as nomepessoa  FROM ExpedicaoDiploma INNER JOIN matricula ON matricula.matricula = expedicaodiploma.matricula inner join pessoa on pessoa.codigo = matricula.aluno  WHERE Matricula.matricula ilike ? ";
		if (usuario.getUnidadeEnsinoLogado().getCodigo() > 0) {
			sqlStr +=  " and matricula.unidadeensino = " + usuario.getUnidadeEnsinoLogado().getCodigo();
		}
		sqlStr += " ORDER BY Matricula.matricula";
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<ExpedicaoDiplomaVO> consultarPorMatriculaAluno(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "select expedicaodiploma.*, pessoa.nome as nomepessoa  from expedicaodiploma INNER JOIN matricula ON matricula.matricula = expedicaodiploma.matricula inner join pessoa on pessoa.codigo = matricula.aluno  where matricula.matricula = '" + matricula + "' ORDER BY expedicaodiploma.via ASC, expedicaodiploma.dataexpedicao ASC, expedicaodiploma.codigo DESC";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public ExpedicaoDiplomaVO consultarPorMatricula(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "select expedicaodiploma.*, pessoa.nome as nomepessoa  from expedicaodiploma INNER JOIN matricula ON matricula.matricula = expedicaodiploma.matricula inner join pessoa on pessoa.codigo = matricula.aluno  where matricula.matricula = '" + matricula + "' ORDER BY expedicaodiploma.via DESC, expedicaodiploma.dataexpedicao DESC, expedicaodiploma.codigo DESC LIMIT 1";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return null;
	}
	
	@Override
	public Date consultarDataExpedicaoDiplomaPorMatricula(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "select dataExpedicao   from expedicaodiploma INNER JOIN matricula ON matricula.matricula = expedicaodiploma.matricula inner join pessoa on pessoa.codigo = matricula.aluno  where matricula.matricula = '" + matricula + "' ORDER BY expedicaodiploma.via DESC, expedicaodiploma.dataexpedicao DESC, expedicaodiploma.codigo DESC LIMIT 1";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);		
		if (rs.next()) {
			return rs.getDate("dataExpedicao");
		}				
		return null;
	}

	public ExpedicaoDiplomaVO consultarPorMatriculaPrimeiraVia(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "select expedicaodiploma.*, pessoa.nome as nomepessoa  from expedicaodiploma INNER JOIN matricula ON matricula.matricula = expedicaodiploma.matricula inner join pessoa on pessoa.codigo = matricula.aluno  where matricula.matricula = '" + matricula + "' and via = '1' ORDER BY expedicaodiploma.via ASC, expedicaodiploma.dataexpedicao ASC, expedicaodiploma.codigo DESC limit 1";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return null;
	}
	
	public ExpedicaoDiplomaVO consultarPorMatriculaMatriculaPrimeiraVia(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT ExpedicaoDiploma.*, pessoa.nome as nomepessoa  FROM ExpedicaoDiploma INNER JOIN matricula ON matricula.matricula = expedicaodiploma.matricula inner join pessoa on pessoa.codigo = matricula.aluno WHERE upper( Matricula.matricula ) like('" + valorConsulta.toUpperCase() + "') and ExpedicaoDiploma.via = '1' ORDER BY Matricula.matricula";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return new ExpedicaoDiplomaVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public ExpedicaoDiplomaVO consultarFuncionarioResponsavelExpedicao(ExpedicaoDiplomaVO expedicaoDiploma, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT ExpedicaoDiploma.funcionarioPrimario, ExpedicaoDiploma.funcionarioSecundario, ");
		sqlStr.append(" ExpedicaoDiploma.funcionarioTerceiro, ExpedicaoDiploma.funcionarioQuarto, ExpedicaoDiploma.funcionarioQuinto, ExpedicaoDiploma.layoutDiploma, ExpedicaoDiploma.cargoFuncionarioPrincipal, "); 
		sqlStr.append(" ExpedicaoDiploma.cargoFuncionarioSecundario, ExpedicaoDiploma.cargoFuncionarioTerceiro, ExpedicaoDiploma.cargoFuncionarioQuarto, ExpedicaoDiploma.cargoFuncionarioQuinto, ExpedicaoDiploma.tituloFuncionarioPrincipal, ExpedicaoDiploma.tituloFuncionarioSecundario, ExpedicaoDiploma.tituloFuncionarioTerceiro, ExpedicaoDiploma.tituloFuncionarioQuarto, ExpedicaoDiploma.tituloFuncionarioQuinto  "); 
		sqlStr.append(" FROM ExpedicaoDiploma ");
		if(expedicaoDiploma.getCodigo() > 0){
			sqlStr.append(" where codigo = ").append(expedicaoDiploma.getCodigo());
		}else{
			sqlStr.append(" where matricula = '").append(expedicaoDiploma.getMatricula().getMatricula()).append("' ");
		}
        sqlStr.append(" and  (ExpedicaoDiploma.funcionarioPrimario > 0 or ExpedicaoDiploma.funcionarioSecundario > 0 or ExpedicaoDiploma.funcionarioTerceiro > 0 or ExpedicaoDiploma.funcionarioQuarto > 0 or ExpedicaoDiploma.funcionarioQuinto > 0)");
		sqlStr.append(" ORDER BY codigo desc limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			if (!expedicaoDiploma.getCodigo().equals(0)) {
				return new ExpedicaoDiplomaVO();
			}
			StringBuilder sqlStr2 = new StringBuilder("SELECT ExpedicaoDiploma.funcionarioPrimario, ExpedicaoDiploma.funcionarioSecundario, ");
			sqlStr2.append(" ExpedicaoDiploma.funcionarioTerceiro, ExpedicaoDiploma.funcionarioQuarto, ExpedicaoDiploma.funcionarioQuinto, ExpedicaoDiploma.layoutDiploma, ExpedicaoDiploma.cargoFuncionarioPrincipal, "); 
			sqlStr2.append(" ExpedicaoDiploma.cargoFuncionarioSecundario, ExpedicaoDiploma.cargoFuncionarioTerceiro, ExpedicaoDiploma.cargoFuncionarioQuarto, ExpedicaoDiploma.cargoFuncionarioQuinto, ExpedicaoDiploma.tituloFuncionarioPrincipal, ExpedicaoDiploma.tituloFuncionarioSecundario, ExpedicaoDiploma.tituloFuncionarioTerceiro, ExpedicaoDiploma.tituloFuncionarioQuarto, ExpedicaoDiploma.tituloFuncionarioQuinto "); 
			sqlStr2.append(" FROM ExpedicaoDiploma ");
            sqlStr2.append(" WHERE (ExpedicaoDiploma.funcionarioPrimario > 0 or ExpedicaoDiploma.funcionarioSecundario > 0 or ExpedicaoDiploma.funcionarioTerceiro > 0 or ExpedicaoDiploma.funcionarioQuarto > 0 or ExpedicaoDiploma.funcionarioQuinto > 0)  ");
			sqlStr2.append(" ORDER BY codigo desc limit 1 ");
			SqlRowSet tabelaResultado2 = getConexao().getJdbcTemplate().queryForRowSet(sqlStr2.toString());
			if (!tabelaResultado2.next()) {
				return expedicaoDiploma;
			}
			return (montarDadosFuncionario(expedicaoDiploma, tabelaResultado2, usuario));
		}
		return (montarDadosFuncionario(expedicaoDiploma, tabelaResultado, usuario));
	}

	public List<ExpedicaoDiplomaVO> consultarPorNomeAluno(String nomeAluno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT expedicaodiploma.*, pessoa.nome as nomepessoa  FROM expedicaodiploma INNER JOIN matricula ON matricula.matricula = expedicaodiploma.matricula " 
		+ "INNER JOIN pessoa ON matricula.aluno = pessoa.codigo WHERE sem_acentos(pessoa.nome) iLIKE sem_acentos(?) ";
		if (usuario.getUnidadeEnsinoLogado().getCodigo() > 0) {
			sqlStr +=  " and  matricula.unidadeensino = " + usuario.getUnidadeEnsinoLogado().getCodigo();
		}
		sqlStr += " ORDER BY pessoa.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, nomeAluno+"%");
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ExpedicaoDiploma</code>
	 * através do valor do atributo <code>Date dataExpedicao</code>. Retorna os
	 * objetos com valores pertecentes ao período informado por parâmetro. Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho
	 * de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ExpedicaoDiplomaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ExpedicaoDiplomaVO> consultarPorDataExpedicao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT ExpedicaoDiploma.*, pessoa.nome as nomepessoa FROM ExpedicaoDiploma INNER JOIN matricula ON matricula.matricula = expedicaodiploma.matricula  inner join pessoa on pessoa.codigo = matricula.aluno WHERE ((dataExpedicao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataExpedicao <= '" + Uteis.getDataJDBC(prmFim) 
				+ "')) ";
		if (usuario.getUnidadeEnsinoLogado().getCodigo() > 0) {
			sqlStr +=  " and matricula.unidadeensino = " + usuario.getUnidadeEnsinoLogado().getCodigo();
		}
		sqlStr += " ORDER BY dataExpedicao";		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ExpedicaoDiploma</code>
	 * através do valor do atributo <code>Integer codigo</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ExpedicaoDiplomaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ExpedicaoDiplomaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT ExpedicaoDiploma.*, pessoa.nome as nomepessoa  FROM ExpedicaoDiploma INNER JOIN matricula ON matricula.matricula = expedicaodiploma.matricula  inner join pessoa on pessoa.codigo = matricula.aluno WHERE ExpedicaoDiploma.codigo = " + valorConsulta.intValue() + " ";
		if (usuario.getUnidadeEnsinoLogado().getCodigo() > 0) {
			sqlStr +=  " and matricula.unidadeensino = " + usuario.getUnidadeEnsinoLogado().getCodigo();
		}
		sqlStr += " ORDER BY ExpedicaoDiploma.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>ExpedicaoDiplomaVO</code>.
	 * 
	 * @return O objeto da classe <code>ExpedicaoDiplomaVO</code> com os dados
	 *         devidamente montados.
	 */
	// public static ExpedicaoDiplomaVO montarDados(SqlRowSet dadosSQL, int
	// nivelMontarDados, UsuarioVO usuario) throws Exception {
	// ExpedicaoDiplomaVO obj = new ExpedicaoDiplomaVO();
	// obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
	// obj.setDataExpedicao(dadosSQL.getDate("dataExpedicao"));
	// obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
	// obj.getGradeCurricularVO().setCodigo(dadosSQL.getInt("gradecurricular"));
	// obj.setVia(dadosSQL.getString("via"));
	// obj.setNumeroProcessoViaAnterior(dadosSQL.getString("numeroprocessoviaanterior"));
	// obj.setNumeroRegistroDiplomaViaAnterior(dadosSQL.getString("numeroregistrodiplomaviaanterior"));
	// obj.setNovoObj(Boolean.FALSE);
	// if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
	// getFacadeFactory().getMatriculaFacade().carregarDados(obj.getMatricula(),
	// NivelMontarDados.BASICO, usuario);
	// //montarDadosMatriculaRapido(obj, nivelMontarDados, usuario);
	// return obj;
	// }
	// montarDadosMatricula(obj, nivelMontarDados, usuario);
	// montarDadosGradeCurricular(obj, nivelMontarDados, usuario);
	// return obj;
	// }

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ExpedicaoDiplomaVO</code> resultantes da consulta.
	 */
	public static List<ExpedicaoDiplomaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ExpedicaoDiplomaVO> vetResultado = new ArrayList<ExpedicaoDiplomaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>ExpedicaoDiplomaVO</code>.
	 * 
	 * @return O objeto da classe <code>ExpedicaoDiplomaVO</code> com os dados
	 *         devidamente montados.
	 */
	public static ExpedicaoDiplomaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ExpedicaoDiplomaVO obj = new ExpedicaoDiplomaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setDataExpedicao(dadosSQL.getDate("dataExpedicao"));
		obj.setDataPublicacaoDiarioOficial(dadosSQL.getDate("dataPublicacaoDiarioOficial"));
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.getGradeCurricularVO().setCodigo(dadosSQL.getInt("gradecurricular"));
		obj.setVia(dadosSQL.getString("via"));
		obj.setNumeroProcessoViaAnterior(dadosSQL.getString("numeroprocessoviaanterior"));
		obj.setNumeroProcesso(dadosSQL.getString("numeroprocesso"));
		obj.setNumeroRegistroDiploma(dadosSQL.getString("numeroRegistroDiploma"));
		obj.setCodigoValidacaoDiplomaDigital(dadosSQL.getString("codigoValidacaoDiplomaDigital"));
		if (dadosSQL.getDate("dataCadastro") != null) {
			obj.setDataCadastro(dadosSQL.getDate("dataCadastro"));
		}
		if (dadosSQL.getObject("responsavelCadastro") != null) {
			obj.getResponsavelCadastro().setCodigo(dadosSQL.getInt("responsavelCadastro"));
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		obj.setNumeroRegistroDiplomaViaAnterior(dadosSQL.getString("numeroregistrodiplomaviaanterior"));
		obj.getUnidadeEnsinoCertificadora().setCodigo(dadosSQL.getInt("unidadeensinocertificadora"));
		obj.setUtilizarUnidadeMatriz(dadosSQL.getBoolean("utilizarUnidadeMatriz"));
		obj.getFuncionarioPrimarioVO().setCodigo(new Integer(dadosSQL.getInt("funcionarioPrimario")));
		obj.getFuncionarioSecundarioVO().setCodigo(new Integer(dadosSQL.getInt("funcionarioSecundario")));
		obj.getFuncionarioTerceiroVO().setCodigo(new Integer(dadosSQL.getInt("funcionarioTerceiro")));
		obj.setLayoutDiploma(dadosSQL.getString("layoutDiploma"));
        obj.getCargoReitorRegistroDiplomaViaAnterior().setCodigo(new Integer(dadosSQL.getInt("cargoReitorRegistroDiplomaViaAnterior")));		
		obj.getCargoFuncionarioPrincipalVO().setCodigo(dadosSQL.getInt("cargoFuncionarioPrincipal"));
		obj.getCargoFuncionarioSecundarioVO().setCodigo(dadosSQL.getInt("cargoFuncionarioSecundario"));
		obj.getCargoFuncionarioTerceiroVO().setCodigo(dadosSQL.getInt("cargoFuncionarioTerceiro"));
		obj.setTituloFuncionarioPrincipal(dadosSQL.getString("tituloFuncionarioPrincipal"));
		obj.setTituloFuncionarioSecundario(dadosSQL.getString("tituloFuncionarioSecundario"));
		obj.setTituloFuncionarioTerceiro(dadosSQL.getString("tituloFuncionarioTerceiro"));
        obj.setObservacaoComplementarDiplomaVOs(getFacadeFactory().getObservacaoComplementarDiplomaFacade().consultarPorExpedicaoDiploma(obj.getCodigo(), false, usuario));
		obj.setTitulacaoFemininoApresentarDiploma(dadosSQL.getString("titulacaoFemininoApresentarDiploma"));
		obj.setTitulacaoMasculinoApresentarDiploma(dadosSQL.getString("titulacaoMasculinoApresentarDiploma"));		
        obj.getReitorRegistroDiplomaViaAnterior().setCodigo(new Integer(dadosSQL.getInt("reitorRegistroDiplomaViaAnterior")));
        obj.getSecretariaRegistroDiplomaViaAnterior().setCodigo(new Integer(dadosSQL.getInt("secretariaRegistroDiplomaViaAnterior")));
        obj.setDataRegistroDiplomaViaAnterior(dadosSQL.getDate("dataRegistroDiplomaViaAnterior"));
        obj.setSerial(dadosSQL.getString("serial"));		
        obj.setObservacao(dadosSQL.getString("observacao"));		
		obj.setObservacaoComplementarDiplomaVOs(getFacadeFactory().getObservacaoComplementarDiplomaFacade().consultarPorExpedicaoDiploma(obj.getCodigo(), false, usuario));
        obj.getFuncionarioQuartoVO().setCodigo(new Integer(dadosSQL.getInt("funcionarioQuarto")));
        obj.getCargoFuncionarioQuartoVO().setCodigo(dadosSQL.getInt("cargoFuncionarioQuarto"));
        obj.setTituloFuncionarioQuarto(dadosSQL.getString("tituloFuncionarioQuarto"));
        obj.getFuncionarioQuintoVO().setCodigo(new Integer(dadosSQL.getInt("funcionarioQuinto")));
        obj.getCargoFuncionarioQuintoVO().setCodigo(dadosSQL.getInt("cargoFuncionarioQuinto"));
        obj.setTituloFuncionarioQuinto(dadosSQL.getString("tituloFuncionarioQuinto"));
        obj.setInformarCamposLivroRegistradora(dadosSQL.getBoolean("informarCamposLivroRegistradora"));
        obj.setFolhaReciboRegistradora(dadosSQL.getString("nrFolhaReciboRegistradora"));
        obj.setLivroRegistradora(dadosSQL.getString("nrLivroRegistradora"));
        obj.setGerarXMLDiploma(dadosSQL.getBoolean("gerarxmldiploma"));
        obj.setAnulado(dadosSQL.getBoolean("anulado"));
        obj.getTextoPadrao().setCodigo(dadosSQL.getInt("textopadrao"));
        obj.setDataRegistroDiploma(dadosSQL.getDate("dataRegistroDiploma"));
        obj.setEmitidoPorProcessoTransferenciaAssistida(dadosSQL.getBoolean("emitidoPorProcessoTransferenciaAssistida"));
        if (obj.getEmitidoPorProcessoTransferenciaAssistida()) {
        	obj.setNomeIesPTA(dadosSQL.getString("nomeIesPTA"));
        	obj.setCnpjPTA(dadosSQL.getString("cnpjPTA"));
        	obj.setCodigoMecPTA(dadosSQL.getInt("codigoMecPTA"));
        	obj.setCepPTA(dadosSQL.getString("cepPTA"));
        	obj.getCidadePTA().setCodigo(dadosSQL.getInt("cidadePTA"));
        	obj.setLogradouroPTA(dadosSQL.getString("logradouroPTA"));
        	obj.setNumeroPTA(dadosSQL.getString("numeroPTA"));
        	obj.setComplementoPTA(dadosSQL.getString("complementoPTA"));
        	obj.setBairroPTA(dadosSQL.getString("bairroPTA"));
        	obj.setTipoDescredenciamentoPTA(TipoAutorizacaoEnum.valueOf(dadosSQL.getString("tipoDescredenciamentoPTA")));
        	obj.setNumeroDescredenciamentoPTA(dadosSQL.getString("numeroDescredenciamentoPTA"));
        	obj.setDataDescredenciamentoPTA(dadosSQL.getDate("dataDescredenciamentoPTA"));
        	obj.setDataPublicacaoDescredenciamentoPTA(dadosSQL.getDate("dataPublicacaoDescredenciamentoPTA"));
        	obj.setVeiculoPublicacaoDescredenciamentoPTA(dadosSQL.getString("veiculoPublicacaoDescredenciamentoPTA"));
        	obj.setSecaoPublicacaoDescredenciamentoPTA(dadosSQL.getInt("secaoPublicacaoDescredenciamentoPTA"));
        	obj.setPaginaPublicacaoDescredenciamentoPTA(dadosSQL.getInt("paginaPublicacaoDescredenciamentoPTA"));
        	obj.setNumeroDOUDescredenciamentoPTA(dadosSQL.getInt("numeroDOUDescredenciamentoPTA"));
        }
        obj.setEmitidoPorDecisaoJudicial(dadosSQL.getBoolean("emitidoPorDecisaoJudicial"));
        if (obj.getEmitidoPorDecisaoJudicial()) {
        	obj.setNomeJuizDecisaoJudicial(dadosSQL.getString("nomeJuizDecisaoJudicial"));
        	obj.setNumeroProcessoDecisaoJudicial(dadosSQL.getString("numeroProcessoDecisaoJudicial"));
        	obj.setDecisaoJudicial(dadosSQL.getString("decisaoJudicial"));
        	obj.setInformacoesAdicionaisDecisaoJudicial(dadosSQL.getString("informacoesAdicionaisDecisaoJudicial"));
        }
        if (dadosSQL.getString("versaoDiploma") != null) {
        	obj.setVersaoDiploma(VersaoDiplomaDigitalEnum.getEnum(dadosSQL.getString("versaoDiploma")));
        }
        if (dadosSQL.getString("motivoAnulacao") != null) {
        	obj.setMotivoAnulacao(TMotivoAnulacao.valueOf(dadosSQL.getString("motivoAnulacao")));
        }
        if (dadosSQL.getDate("dataAnulacao") != null) {
        	obj.setDataAnulacao(dadosSQL.getDate("dataAnulacao"));
        }
        if (dadosSQL.getString("anotacaoAnulacao") != null) {
        	obj.setAnotacaoAnulacao(dadosSQL.getString("anotacaoAnulacao"));
        }
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			montarDadosFuncionarioPrimario(obj, usuario);
			montarDadosFuncionarioSecundario(obj, usuario);
			montarDadosFuncionarioTerceiro(obj, usuario);
            montarDadosFuncionarioQuarto(obj, usuario);
            montarDadosFuncionarioQuinto(obj, usuario);
			if (!obj.getVia().equals("1") && !obj.getVia().equals("")) {
				montarDadosFuncionarioReitor(obj, usuario);	
				montarDadosFuncionarioSecGeral(obj, usuario);	
			}
			obj.getMatricula().getAluno().setNome(dadosSQL.getString("nomePessoa"));
			if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoCertificadora())) {
				obj.setUnidadeEnsinoCertificadora(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsinoCertificadora().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
			}
			if (Uteis.isAtributoPreenchido(obj.getCidadePTA())) {
				obj.setCidadePTA(getFacadeFactory().getCidadeFacade().consultarPorChavePrimariaUnica(obj.getCidadePTA().getCodigo(), false, usuario));
			}
			if (obj.getEmitidoPorDecisaoJudicial()) {
				obj.setDeclaracaoAcercaProcessoJudicialVOs(getFacadeFactory().getDeclaracaoAcercaProcessoJudicialInterfaceFacade().consultar(obj.getCodigo()));
			}
			if (Uteis.isAtributoPreenchido(obj.getResponsavelCadastro())) {
				obj.setResponsavelCadastro(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelCadastro().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
			}
			//montarDadosMatriculaRapido(obj, nivelMontarDados, usuario);
			return obj;
		}
		montarDadosFuncionarioPrimario(obj, usuario);
		montarDadosFuncionarioSecundario(obj, usuario);
		montarDadosFuncionarioTerceiro(obj, usuario);
        montarDadosFuncionarioQuarto(obj, usuario);
        montarDadosFuncionarioQuinto(obj, usuario);
		if (!obj.getVia().equals("1") && !obj.getVia().equals("")) {
			montarDadosFuncionarioReitor(obj, usuario);	
			montarDadosFuncionarioSecGeral(obj, usuario);	
		}
		obj.getMatricula().getAluno().setNome(dadosSQL.getString("nomePessoa"));
		montarDadosUnidadeEnsino(obj, nivelMontarDados);
		montarDadosMatricula(obj, nivelMontarDados, usuario);
		montarDadosGradeCurricular(obj, nivelMontarDados, usuario);
		return obj;
	}

	public static void montarDadosUnidadeEnsino(ExpedicaoDiplomaVO obj, int nivelMontarDados) throws Exception {
		if (obj.getUnidadeEnsinoCertificadora().getCodigo().intValue() == 0) {
			return;
		}
		if (obj.getUnidadeEnsinoCertificadora().getNivelMontarDados().getValor().equals(nivelMontarDados)) {
			return;
		}
		obj.setUnidadeEnsinoCertificadora(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsinoCertificadora().getCodigo(), false, nivelMontarDados, null));
	}

	public static ExpedicaoDiplomaVO montarDadosFuncionario(ExpedicaoDiplomaVO obj, SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		obj.getFuncionarioPrimarioVO().setCodigo(new Integer(dadosSQL.getInt("funcionarioPrimario")));
		obj.getFuncionarioSecundarioVO().setCodigo(new Integer(dadosSQL.getInt("funcionarioSecundario")));
		obj.getFuncionarioTerceiroVO().setCodigo(new Integer(dadosSQL.getInt("funcionarioTerceiro")));
        obj.getFuncionarioQuartoVO().setCodigo(new Integer(dadosSQL.getInt("funcionarioQuarto")));
        obj.getFuncionarioQuintoVO().setCodigo(new Integer(dadosSQL.getInt("funcionarioQuinto")));
		obj.setLayoutDiploma(dadosSQL.getString("layoutDiploma"));
		obj.getCargoFuncionarioPrincipalVO().setCodigo(dadosSQL.getInt("cargoFuncionarioPrincipal"));
		obj.getCargoFuncionarioSecundarioVO().setCodigo(dadosSQL.getInt("cargoFuncionarioSecundario"));
		obj.getCargoFuncionarioTerceiroVO().setCodigo(dadosSQL.getInt("cargoFuncionarioTerceiro"));
        obj.getCargoFuncionarioQuartoVO().setCodigo(dadosSQL.getInt("cargoFuncionarioQuarto"));
        obj.getCargoFuncionarioQuintoVO().setCodigo(dadosSQL.getInt("cargoFuncionarioQuinto"));
		obj.setTituloFuncionarioPrincipal(dadosSQL.getString("tituloFuncionarioPrincipal"));
		obj.setTituloFuncionarioSecundario(dadosSQL.getString("tituloFuncionarioSecundario"));
		obj.setTituloFuncionarioTerceiro(dadosSQL.getString("tituloFuncionarioTerceiro"));
        obj.setTituloFuncionarioQuarto(dadosSQL.getString("tituloFuncionarioQuarto"));
        obj.setTituloFuncionarioQuinto(dadosSQL.getString("tituloFuncionarioQuinto"));
		if (!obj.getFuncionarioPrimarioVO().getArquivoAssinaturaVO().getCodigo().equals(0)) {
			obj.getFuncionarioPrimarioVO().setArquivoAssinaturaVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getFuncionarioPrimarioVO().getArquivoAssinaturaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}
		if (!obj.getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getCodigo().equals(0)) {
			obj.getFuncionarioSecundarioVO().setArquivoAssinaturaVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}
		if (!obj.getFuncionarioTerceiroVO().getArquivoAssinaturaVO().getCodigo().equals(0)) {
			obj.getFuncionarioTerceiroVO().setArquivoAssinaturaVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getFuncionarioTerceiroVO().getArquivoAssinaturaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}
        if (!obj.getFuncionarioQuartoVO().getArquivoAssinaturaVO().getCodigo().equals(0)) {
            obj.getFuncionarioQuartoVO().setArquivoAssinaturaVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getFuncionarioQuartoVO().getArquivoAssinaturaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
        }
        if (!obj.getFuncionarioQuintoVO().getArquivoAssinaturaVO().getCodigo().equals(0)) {
            obj.getFuncionarioQuintoVO().setArquivoAssinaturaVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getFuncionarioQuintoVO().getArquivoAssinaturaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
        }   
		montarDadosFuncionarioPrimario(obj, usuario);
		montarDadosFuncionarioSecundario(obj, usuario);
		montarDadosFuncionarioTerceiro(obj, usuario);
        montarDadosFuncionarioQuarto(obj, usuario);
        montarDadosFuncionarioQuinto(obj, usuario);
		if (!obj.getFuncionarioPrimarioVO().getArquivoAssinaturaVO().getCodigo().equals(0)) {
			obj.getFuncionarioPrimarioVO().setArquivoAssinaturaVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getFuncionarioPrimarioVO().getArquivoAssinaturaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}
		if (!obj.getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getCodigo().equals(0)) {
			obj.getFuncionarioSecundarioVO().setArquivoAssinaturaVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}
		if (!obj.getFuncionarioTerceiroVO().getArquivoAssinaturaVO().getCodigo().equals(0)) {
			obj.getFuncionarioTerceiroVO().setArquivoAssinaturaVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getFuncionarioTerceiroVO().getArquivoAssinaturaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}
        if (!obj.getFuncionarioQuartoVO().getArquivoAssinaturaVO().getCodigo().equals(0)) {
            obj.getFuncionarioQuartoVO().setArquivoAssinaturaVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getFuncionarioQuartoVO().getArquivoAssinaturaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
        }
        if (!obj.getFuncionarioQuintoVO().getArquivoAssinaturaVO().getCodigo().equals(0)) {
            obj.getFuncionarioQuintoVO().setArquivoAssinaturaVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getFuncionarioQuintoVO().getArquivoAssinaturaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
        }
		return obj;
	}

	public static void montarDadosFuncionarioReitor(ExpedicaoDiplomaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getReitorRegistroDiplomaViaAnterior().getCodigo().intValue() == 0) {
			obj.setReitorRegistroDiplomaViaAnterior(new FuncionarioVO());
			return;
		}
		obj.setReitorRegistroDiplomaViaAnterior(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(obj.getReitorRegistroDiplomaViaAnterior().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
	}

	public static void montarDadosFuncionarioSecGeral(ExpedicaoDiplomaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getSecretariaRegistroDiplomaViaAnterior().getCodigo().intValue() == 0) {
			obj.setSecretariaRegistroDiplomaViaAnterior(new FuncionarioVO());
			return;
		}
		obj.setSecretariaRegistroDiplomaViaAnterior(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(obj.getSecretariaRegistroDiplomaViaAnterior().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
	}

	public static void montarDadosFuncionarioPrimario(ExpedicaoDiplomaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getFuncionarioPrimarioVO().getCodigo().intValue() == 0) {
			obj.setFuncionarioPrimarioVO(new FuncionarioVO());
			return;
		}
		obj.setFuncionarioPrimarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(obj.getFuncionarioPrimarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
	}

	public static void montarDadosFuncionarioSecundario(ExpedicaoDiplomaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getFuncionarioSecundarioVO().getCodigo().intValue() == 0) {
			obj.setFuncionarioSecundarioVO(new FuncionarioVO());
			return;
		}
		obj.setFuncionarioSecundarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(obj.getFuncionarioSecundarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
	}

	public static void montarDadosFuncionarioTerceiro(ExpedicaoDiplomaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getFuncionarioTerceiroVO().getCodigo().intValue() == 0) {
			obj.setFuncionarioTerceiroVO(new FuncionarioVO());
			return;
		}
		obj.setFuncionarioTerceiroVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(obj.getFuncionarioTerceiroVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
	}

	public static void montarDadosMatriculaRapido(ExpedicaoDiplomaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if ((obj.getMatricula().getMatricula() == null) || (obj.getMatricula().getMatricula().equals(""))) {
			obj.setMatricula(new MatriculaVO());
			return;
		}
		obj.setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula().getMatricula(), usuario.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>MatriculaVO</code> relacionado ao objeto
	 * <code>ExpedicaoDiplomaVO</code>. Faz uso da chave primária da classe
	 * <code>MatriculaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosMatricula(ExpedicaoDiplomaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if ((obj.getMatricula().getMatricula() == null) || (obj.getMatricula().getMatricula().equals(""))) {
			obj.setMatricula(new MatriculaVO());
			return;
		}
		// obj.setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula().getMatricula(),
		// getUnidadeEnsinoLogado().getCodigo(), nivelMontarDados));
		obj.setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula().getMatricula(), usuario.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.TODOS, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>GradeCurricularVO</code> relacionado ao objeto
	 * <code>ExpedicaoDiplomaVO</code>. Faz uso da chave primária da classe
	 * <code>GradeCurricularVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosGradeCurricular(ExpedicaoDiplomaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if ((obj.getGradeCurricularVO().getCodigo() == null) || (obj.getGradeCurricularVO().getCodigo().intValue() == 0)) {
			obj.setGradeCurricularVO(new GradeCurricularVO());
			return;
		}
		obj.setGradeCurricularVO(getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(obj.getGradeCurricularVO().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>ExpedicaoDiplomaVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public ExpedicaoDiplomaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT ExpedicaoDiploma.*, pessoa.nome as nomepessoa  FROM ExpedicaoDiploma INNER JOIN matricula ON matricula.matricula = expedicaodiploma.matricula  inner join pessoa on pessoa.codigo = matricula.aluno WHERE ExpedicaoDiploma.codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ExpedicaoDiploma ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public void incluirDataConclusaoCursoMatricula(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuario) throws Exception {
		// List<PeriodoLetivoAtivoUnidadeEnsinoCursoVO>
		// listaPeriodoLetivoUnidadeEnsinoCurso =
		// getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().consultarPorMatricula(expedicaoDiplomaVO.getMatricula().getMatricula(),
		// false, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		// if (!listaPeriodoLetivoUnidadeEnsinoCurso.isEmpty()) {
		// expedicaoDiplomaVO.getMatricula().setDataConclusaoCurso(listaPeriodoLetivoUnidadeEnsinoCurso.get(listaPeriodoLetivoUnidadeEnsinoCurso.size()
		// - 1).getDataFimPeriodoLetivo());
		if (expedicaoDiplomaVO.getMatricula().getDataConclusaoCurso() != null) {
			getFacadeFactory().getMatriculaFacade().alterarDataConclusaoCurso(expedicaoDiplomaVO.getMatricula().getMatricula(), expedicaoDiplomaVO.getMatricula().getDataConclusaoCurso(), usuario);
			expedicaoDiplomaVO.getMatricula().setResponsavelAtualizacaoMatriculaFormada(usuario);
			expedicaoDiplomaVO.getMatricula().setDataAtualizacaoMatriculaFormada(new Date());
			getFacadeFactory().getMatriculaFacade().alterarSituacaoMatriculaFormadaAtualizacao(expedicaoDiplomaVO.getMatricula(), usuario);
		}
//		else {
//			throw new ConsistirException("A Matrícula informada não possui uma data de conclusão do curso.");
//		}
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ExpedicaoDiploma.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ExpedicaoDiploma.idEntidade = idEntidade;
	}
        
        public void consultarObservacaoComplementarUltimaExpedicaoMatricula(ExpedicaoDiplomaVO expedicaoDiploma, UsuarioVO usuario) throws Exception {
            StringBuilder sqlStr = new StringBuilder("SELECT ExpedicaoDiploma.codigo as ultimaExpedicao, ExpedicaoDiploma.funcionarioPrimario, ExpedicaoDiploma.funcionarioSecundario, ");
            sqlStr.append(" ExpedicaoDiploma.funcionarioTerceiro, ExpedicaoDiploma.layoutDiploma, ExpedicaoDiploma.cargoFuncionarioPrincipal, "); 
            sqlStr.append(" ExpedicaoDiploma.cargoFuncionarioSecundario, ExpedicaoDiploma.cargoFuncionarioTerceiro, ExpedicaoDiploma.tituloFuncionarioPrincipal, ExpedicaoDiploma.tituloFuncionarioSecundario, ExpedicaoDiploma.tituloFuncionarioTerceiro  "); 
            sqlStr.append(" FROM ExpedicaoDiploma ");
            sqlStr.append(" where matricula = '").append(expedicaoDiploma.getMatricula().getMatricula()).append("' ");
            sqlStr.append(" ORDER BY codigo desc limit 1 ");
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            try {
                if (tabelaResultado.next()) {
                    Integer codigoUltimaExpedicaoDiploma = tabelaResultado.getInt("ultimaExpedicao");
                    if ((codigoUltimaExpedicaoDiploma != null) && (!codigoUltimaExpedicaoDiploma.equals(0))) {
                        List<ObservacaoComplementarDiplomaVO> listaObservacosUltimaExpedicao = getFacadeFactory().getObservacaoComplementarDiplomaFacade().consultarPorExpedicaoDiploma(codigoUltimaExpedicaoDiploma, false, usuario);
                        for (ObservacaoComplementarDiplomaVO obsInicializarOutraExpedicao : listaObservacosUltimaExpedicao) {
                            // removendo vinculo com expedicao anterior, para a nova configuracao seja grava com a
                            // nova expedicao...
                            obsInicializarOutraExpedicao.setCodigo(0);
                            obsInicializarOutraExpedicao.getExpedicaoDiploma().setCodigo(expedicaoDiploma.getCodigo());
                        }
                        expedicaoDiploma.setObservacaoComplementarDiplomaVOs(listaObservacosUltimaExpedicao);
                    }
                }
            } catch (Exception e) {
            }
        }
        
        public boolean verificarExisteRegistroExpedicaoDiplomaDiferenteAtual(ExpedicaoDiplomaVO expedicaoDiplomaVO ) throws Exception {
    		StringBuilder sqlStr = new StringBuilder();
    		sqlStr.append("SELECT expedicaodiploma.codigo FROM expedicaodiploma ");
    		sqlStr.append(" WHERE expedicaodiploma.codigo <> ").append(expedicaoDiplomaVO.getCodigo().intValue());
    		sqlStr.append(" AND expedicaodiploma.matricula = '").append(expedicaoDiplomaVO.getMatricula().getMatricula()).append("';");
    		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()).next();
    	}
    	
    	public void executarExclusaoDadosColacaoGrauAluno(MatriculaVO matriculaVO,UsuarioVO usuarioVO) throws Exception{
    		ColacaoGrauVO colacaoGrauVO = getFacadeFactory().getColacaoGrauFacade().consultarPorMatricula(matriculaVO, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
    		if (Uteis.isAtributoPreenchido(colacaoGrauVO) && !getFacadeFactory().getColacaoGrauFacade().verificarColacaoGrauPertenceMaisDeUmAluno(matriculaVO, colacaoGrauVO)) {
    			getFacadeFactory().getColacaoGrauFacade().excluir(colacaoGrauVO, usuarioVO);
    		}else if(Uteis.isAtributoPreenchido(colacaoGrauVO)) {
    			ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO = getFacadeFactory().getProgramacaoFormaturaAlunoFacade().consultarPorMatriculaColacaoGrau(matriculaVO.getMatricula(), colacaoGrauVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
    			if (Uteis.isAtributoPreenchido(programacaoFormaturaAlunoVO)) {
    				matriculaVO.setDataColacaoGrau(null);
    				getFacadeFactory().getMatriculaFacade().alterarDataColacaoGrauPorMatricula(matriculaVO, usuarioVO);
    				getFacadeFactory().getProgramacaoFormaturaAlunoFacade().excluir(programacaoFormaturaAlunoVO);
    			}
    		}
    	}
    	
    	@Override
    	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    	public void alterarDataExpedicaoDiploma(final Integer codigo, final Date data, UsuarioVO usuario, Boolean controlarAcesso) throws Exception {
    		try {
    			ExpedicaoDiploma.alterar(getIdEntidade(), controlarAcesso, usuario);
    			final String sql = "UPDATE ExpedicaoDiploma set dataExpedicao=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

    			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

    				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
    					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
    					sqlAlterar.setDate(1, Uteis.getDataJDBC(data));
    					sqlAlterar.setInt(2, codigo);
    					return sqlAlterar;
    				}
    			});
    		} catch (Exception e) {
    			throw e;
    		}
    	}

    	@Override
		public void validarRegraEmissao(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO) throws Exception {
			List<RegraEmissaoVO> regraEmissaoVOs = getFacadeFactory().getRegraEmissaoInterfaceFacade().consultarTodasRegrasEmissao(usuarioVO);
			if(regraEmissaoVOs != null) {
				RegraEmissaoVO  regraEmissao = null; 
				if(expedicaoDiplomaVO.getMatricula().getCurso().getNivelEducacional().equals("PO")) {
					 regraEmissao = regraEmissaoVOs.stream().filter(r -> r.getNivelEducacional().equals(expedicaoDiplomaVO.getMatricula().getCurso().getNivelEducacional())
							 && r.getTipoContrato().equals(expedicaoDiplomaVO.getMatricula().getTipoMatricula())).findFirst().orElse(null);
				}else {
					regraEmissao = regraEmissaoVOs.stream().filter(r -> r.getNivelEducacional().equals(expedicaoDiplomaVO.getMatricula().getCurso().getNivelEducacional())).findFirst().orElse(null);
				}	
					if(regraEmissao != null && Uteis.isAtributoPreenchido(regraEmissao.getCodigo())){
						verificarRegraEmissao(regraEmissao, usuarioVO, expedicaoDiplomaVO);
				}
			}	
		}
		
		private void verificarRegraEmissao(RegraEmissaoVO regraEmissao, UsuarioVO usuarioVO, ExpedicaoDiplomaVO expedicaoDiplomaVO) throws Exception {
			MatriculaVO matriculaVO = expedicaoDiplomaVO.getMatricula();
			String documentoPendente = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarExistenciaPendenciaDocumentacaoPorMatricula(matriculaVO.getAluno().getCodigo(), matriculaVO.getMatricula(), new ArrayList<TipoDocumentoVO>(), true);
				if(regraEmissao.getValidarMatrizCurricularIntegralizado() &&
				  !getFacadeFactory().getMatriculaFacade().isMatriculaIntegralizada(matriculaVO,matriculaVO.getGradeCurricularAtual().getCodigo(),usuarioVO, null)) { throw
				  new ConsistirException("O Aluno não possui matrícula integralizada."); 
				}if(regraEmissao.getValidarNotaTCC() && (matriculaVO.getNotaMonografia() == null || matriculaVO.getNotaMonografia().compareTo(regraEmissao.getNotaTCC()) < 0)){
					throw new ConsistirException("O Aluno não possui nota mínima de TCC.");
				}if(regraEmissao.getValidarDocumentosEntregues() && documentoPendente != null){
					throw new ConsistirException("O Aluno possui documentos pedentes para ser entregues.");
				}if(Uteis.isAtributoPreenchido(regraEmissao.getTipoContrato()) && !regraEmissao.getTipoContrato().equals(matriculaVO.getTipoMatricula())){
					throw new ConsistirException("Tipo de Contrato não é válido.");
				}if(!verificarRegraEmissaoUnidadeEnsio(regraEmissao.getRegraEmissaoUnidadeEnsinoVOs(), expedicaoDiplomaVO.getUnidadeEnsinoCertificadora())) {
					throw new ConsistirException(" A Unidade de ensino não é válida para Regra de Emissão.");
				}
			}
		
		private boolean verificarRegraEmissaoUnidadeEnsio(List<RegraEmissaoUnidadeEnsinoVO> regraEmissaoUnidadeEnsinoVOs, UnidadeEnsinoVO unidadeEnsinoVO) {
			boolean isUnidadeEnsino = false;
			if(regraEmissaoUnidadeEnsinoVOs != null) {
				for(RegraEmissaoUnidadeEnsinoVO emissaoUnidadeEnsinoVO : regraEmissaoUnidadeEnsinoVOs){
					if(emissaoUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo().equals(unidadeEnsinoVO.getCodigo())) {
						isUnidadeEnsino = true;
					}
				}
			}
			return isUnidadeEnsino;
		}

		@Override
		public void montarNumeroProcessoERegistroDiplomaVindoMascaraConfiguracaoAcademico(ExpedicaoDiplomaVO expedicaoDiplomaVO, Integer unidadeEnsino, Integer curso, UsuarioVO usuarioLogadoClone) throws Exception {
			ConfiguracaoAcademicoVO confAcademico = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarMascaraNumeroRegistroDiplomaENumeroProcessoExpedicaoDiplomaPorCurso(curso, false, usuarioLogadoClone);
			UnidadeEnsinoCursoVO uec = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorMatriculaAluno(expedicaoDiplomaVO.getMatricula().getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogadoClone);	
			UnidadeEnsinoVO unidade = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(expedicaoDiplomaVO.getMatricula().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogadoClone);
			String mascaraNumeroProcessoExpedicaoDiploma = confAcademico.getMascaraNumeroProcessoExpedicaoDiploma();
			String mascaraNumeroRegistroDiploma = confAcademico.getMascaraNumeroRegistroDiploma();
			if (Uteis.isAtributoPreenchido(mascaraNumeroProcessoExpedicaoDiploma) || Uteis.isAtributoPreenchido(mascaraNumeroRegistroDiploma)) {
				Map<String, String> mapDadosLivro = getFacadeFactory().getControleLivroRegistroDiplomaFacade().obterDadosLivroPorMatricula(expedicaoDiplomaVO.getMatricula().getMatricula(), expedicaoDiplomaVO.getVia());
				expedicaoDiplomaVO.setNumeroRegistroDiploma(Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getNumeroRegistroDiploma()) ? expedicaoDiplomaVO.getNumeroRegistroDiploma() : Constantes.EMPTY);
				expedicaoDiplomaVO.setNumeroProcesso(Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getNumeroProcesso()) ? expedicaoDiplomaVO.getNumeroProcesso() : Constantes.EMPTY);

				if (mascaraNumeroProcessoExpedicaoDiploma.toUpperCase()	.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.MATRICULA.getValor())) {
					mascaraNumeroProcessoExpedicaoDiploma = mascaraNumeroProcessoExpedicaoDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.MATRICULA.getValor(), expedicaoDiplomaVO.getMatricula().getMatricula());
				}
				if (mascaraNumeroRegistroDiploma.toUpperCase().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.MATRICULA.getValor())) {
					mascaraNumeroRegistroDiploma = mascaraNumeroRegistroDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.MATRICULA.getValor(), expedicaoDiplomaVO.getMatricula().getMatricula());
				}
				if (mascaraNumeroProcessoExpedicaoDiploma.toUpperCase().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ABREV_NOME_CURSO.getValor())) {
					mascaraNumeroProcessoExpedicaoDiploma = mascaraNumeroProcessoExpedicaoDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ABREV_NOME_CURSO.getValor(), expedicaoDiplomaVO.getMatricula().getCurso().getAbreviatura());
				}
				if (mascaraNumeroRegistroDiploma.toUpperCase().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ABREV_NOME_CURSO.getValor())) {
					mascaraNumeroRegistroDiploma = mascaraNumeroRegistroDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ABREV_NOME_CURSO.getValor(), expedicaoDiplomaVO.getMatricula().getCurso().getAbreviatura());
				}
				if (mascaraNumeroProcessoExpedicaoDiploma.toUpperCase().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_EXPEDICAO.getValor())
						|| mascaraNumeroRegistroDiploma.toUpperCase().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_EXPEDICAO.getValor())) {
					if (mascaraNumeroProcessoExpedicaoDiploma.toUpperCase()	.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_EXPEDICAO.getValor())) {
						mascaraNumeroProcessoExpedicaoDiploma = mascaraNumeroProcessoExpedicaoDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_EXPEDICAO.getValor(), Uteis.isAtributoPreenchido(mapDadosLivro) ? mapDadosLivro.get(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_EXPEDICAO.getValor()) : Constantes.EMPTY);
					}
					if (mascaraNumeroRegistroDiploma.toUpperCase().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_EXPEDICAO.getValor())) {
						mascaraNumeroRegistroDiploma = mascaraNumeroRegistroDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_EXPEDICAO.getValor(), Uteis.isAtributoPreenchido(mapDadosLivro) ? mapDadosLivro.get(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_EXPEDICAO.getValor()) : Constantes.EMPTY);
					}
					if ((!Uteis.isAtributoPreenchido(mapDadosLivro)) || (Uteis.isAtributoPreenchido(mapDadosLivro) && !Uteis.isAtributoPreenchido(mapDadosLivro.get(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_EXPEDICAO.getValor())))) {
						expedicaoDiplomaVO.setPossuiErro(Boolean.TRUE);
						expedicaoDiplomaVO.setErro("Não foi encontrado um registro no livro do diploma para o aluno  "
								+ expedicaoDiplomaVO.getMatricula().getMatricula() + " -  "
								+ expedicaoDiplomaVO.getMatricula().getAluno().getNome()
								+ ", para a geração do número do registro diploma/processo");
					}
				}
				if (mascaraNumeroProcessoExpedicaoDiploma.toUpperCase()	.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO_ATUAL.getValor())) {
					mascaraNumeroProcessoExpedicaoDiploma = mascaraNumeroProcessoExpedicaoDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO_ATUAL.getValor(), Uteis.getAnoDataAtual4Digitos());
				}
				if (mascaraNumeroRegistroDiploma.toUpperCase()	.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO_ATUAL.getValor())) {
					mascaraNumeroRegistroDiploma = mascaraNumeroRegistroDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO_ATUAL.getValor(), Uteis.getAnoDataAtual4Digitos());
				}
				if (mascaraNumeroProcessoExpedicaoDiploma.toUpperCase()	.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE_ATUAL.getValor())) {
					mascaraNumeroProcessoExpedicaoDiploma = mascaraNumeroProcessoExpedicaoDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE_ATUAL.getValor(), Uteis.getSemestreAtual());
				}
				if (mascaraNumeroRegistroDiploma.toUpperCase()	.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE_ATUAL.getValor())) {
					mascaraNumeroRegistroDiploma = mascaraNumeroRegistroDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE_ATUAL.getValor(), Uteis.getSemestreAtual());
				}
				if (mascaraNumeroProcessoExpedicaoDiploma.toUpperCase().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO.getValor())
						|| mascaraNumeroRegistroDiploma.toUpperCase().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO.getValor())
						|| mascaraNumeroProcessoExpedicaoDiploma.toUpperCase().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE.getValor())
						|| mascaraNumeroRegistroDiploma.toUpperCase().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE.getValor())) {
					String ano = "";
					String semestre = "";
					if (expedicaoDiplomaVO.getMatricula().getCurso().getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL.getValor())) {
						MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultaAnoSemestreUltimaMatriculaPeriodoPorMatriculaConsultaBasica(expedicaoDiplomaVO.getMatricula().getMatricula(), false, usuarioLogadoClone);
						ano = matriculaPeriodoVO.getAno();
						semestre = matriculaPeriodoVO.getSemestre();
					} else {
						int anoConclusao = Uteis.getAnoData(expedicaoDiplomaVO.getDataExpedicao());
						if (anoConclusao > 0) {
							ano = String.valueOf(anoConclusao);
						}
						semestre = Uteis.getSemestreData(expedicaoDiplomaVO.getDataExpedicao());
					}
					if (mascaraNumeroProcessoExpedicaoDiploma.toUpperCase().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO.getValor())) {
						mascaraNumeroProcessoExpedicaoDiploma = mascaraNumeroProcessoExpedicaoDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO.getValor(), ano);
					}
					if (mascaraNumeroRegistroDiploma.toUpperCase().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO.getValor())) {
						mascaraNumeroRegistroDiploma = mascaraNumeroRegistroDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.ANO.getValor(), ano);
					}
					if (mascaraNumeroProcessoExpedicaoDiploma.toUpperCase().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE.getValor())) {
						mascaraNumeroProcessoExpedicaoDiploma = mascaraNumeroProcessoExpedicaoDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE.getValor(), semestre);
					}
					if (mascaraNumeroRegistroDiploma.toUpperCase().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE.getValor())) {
						mascaraNumeroRegistroDiploma = mascaraNumeroRegistroDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.SEMESTRE.getValor(), semestre);
					}
				}
				if (mascaraNumeroRegistroDiploma.toUpperCase().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_PROCESSO.getValor())) {
					if (!Uteis.isAtributoPreenchido(mascaraNumeroProcessoExpedicaoDiploma) && !Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getNumeroProcesso())) {
						expedicaoDiplomaVO.gerarNumeroProcesso(getFacadeFactory().getRequerimentoFacade().consultaRapidaUltimoRequerimentoPorMatriculaTipoDocumentoDiploma(expedicaoDiplomaVO.getMatricula().getMatricula()));
					}
					mascaraNumeroProcessoExpedicaoDiploma = Uteis.coalesceIsAtributoPreenchido(mascaraNumeroProcessoExpedicaoDiploma, expedicaoDiplomaVO.getNumeroProcesso());
					mascaraNumeroRegistroDiploma = mascaraNumeroRegistroDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_PROCESSO.getValor(), mascaraNumeroProcessoExpedicaoDiploma);
				}
				if (mascaraNumeroProcessoExpedicaoDiploma.toUpperCase()	.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_CURSO.getValor())) {
					mascaraNumeroProcessoExpedicaoDiploma = mascaraNumeroProcessoExpedicaoDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_CURSO.getValor(), expedicaoDiplomaVO.getMatricula().getCurso().getCodigo().toString());
				}
				if (mascaraNumeroRegistroDiploma.toUpperCase().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_CURSO.getValor())) {
					mascaraNumeroRegistroDiploma = mascaraNumeroRegistroDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_CURSO.getValor(), expedicaoDiplomaVO.getMatricula().getCurso().getCodigo().toString());
				}
				if (mascaraNumeroProcessoExpedicaoDiploma.toUpperCase().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_IES.getValor())) {
					mascaraNumeroProcessoExpedicaoDiploma = mascaraNumeroProcessoExpedicaoDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_IES.getValor(), expedicaoDiplomaVO.getMatricula().getUnidadeEnsino().getCodigo().toString());
				}
				if (mascaraNumeroRegistroDiploma.toUpperCase().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_IES.getValor())) {
					mascaraNumeroRegistroDiploma = mascaraNumeroRegistroDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_IES.getValor(), expedicaoDiplomaVO.getMatricula().getUnidadeEnsino().getCodigo().toString());
				}
				if (mascaraNumeroProcessoExpedicaoDiploma.toUpperCase().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_CURSO.getValor())) {
					if (Uteis.isAtributoPreenchido(uec.getCurso().getIdCursoInep())) {
						mascaraNumeroProcessoExpedicaoDiploma = mascaraNumeroProcessoExpedicaoDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_CURSO.getValor(), uec.getCurso().getIdCursoInep().toString());					
					} else {
						mascaraNumeroProcessoExpedicaoDiploma = mascaraNumeroProcessoExpedicaoDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_CURSO.getValor(), uec.getCodigoInep() .toString());					
					}
				}
				if (mascaraNumeroRegistroDiploma.toUpperCase().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_CURSO.getValor())) {
					if (Uteis.isAtributoPreenchido(uec.getCurso().getIdCursoInep())) {
						mascaraNumeroRegistroDiploma = mascaraNumeroRegistroDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_CURSO.getValor(), uec.getCurso().getIdCursoInep().toString());					
					} else {
						mascaraNumeroRegistroDiploma = mascaraNumeroRegistroDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_CURSO.getValor(), uec.getCodigoInep().toString());					
					}
				}
				if (mascaraNumeroProcessoExpedicaoDiploma.toUpperCase()	.contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_IES.getValor())) {
					mascaraNumeroProcessoExpedicaoDiploma = mascaraNumeroProcessoExpedicaoDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_IES.getValor(), unidade.getCodigoIES().toString());
				}
				if (mascaraNumeroRegistroDiploma.toUpperCase().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_IES.getValor())) {
					mascaraNumeroRegistroDiploma = mascaraNumeroRegistroDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.CODIGO_EMEC_IES.getValor(), unidade.getCodigoIES().toString());
				}
				if (mascaraNumeroProcessoExpedicaoDiploma.toUpperCase().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_REGISTRO_DIPLOMA.getValor())) {
					mascaraNumeroProcessoExpedicaoDiploma = mascaraNumeroProcessoExpedicaoDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_REGISTRO_DIPLOMA.getValor(), Uteis.isAtributoPreenchido(mapDadosLivro) ? mapDadosLivro.get(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_REGISTRO_DIPLOMA.getValor()) : Constantes.EMPTY);
				}
				if (mascaraNumeroRegistroDiploma.toUpperCase().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_REGISTRO_DIPLOMA.getValor())) {
					mascaraNumeroRegistroDiploma = mascaraNumeroRegistroDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_REGISTRO_DIPLOMA.getValor(), Uteis.isAtributoPreenchido(mapDadosLivro) ? mapDadosLivro.get(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_LIVRO_REGISTRO_DIPLOMA.getValor()) : Constantes.EMPTY);
				}
				if (mascaraNumeroProcessoExpedicaoDiploma.toUpperCase().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_FOLHA_LIVRO.getValor())) {
					mascaraNumeroProcessoExpedicaoDiploma = mascaraNumeroProcessoExpedicaoDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_FOLHA_LIVRO.getValor(), Uteis.isAtributoPreenchido(mapDadosLivro) ? mapDadosLivro.get(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_FOLHA_LIVRO.getValor()) : Constantes.EMPTY);
				}
				if (mascaraNumeroRegistroDiploma.toUpperCase().contains(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_FOLHA_LIVRO.getValor())) {
					mascaraNumeroRegistroDiploma = mascaraNumeroRegistroDiploma.replace(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_FOLHA_LIVRO.getValor(), Uteis.isAtributoPreenchido(mapDadosLivro) ? mapDadosLivro.get(ExpedicaoDiplomaVO.MascaraTagNumeroExpedicaoDiploma.NR_FOLHA_LIVRO.getValor()) : Constantes.EMPTY);
				}
				if (Uteis.isAtributoPreenchido(mascaraNumeroRegistroDiploma)) {
					expedicaoDiplomaVO.setNumeroRegistroDiploma(mascaraNumeroRegistroDiploma.toString());
				}
				if (Uteis.isAtributoPreenchido(mascaraNumeroProcessoExpedicaoDiploma)) {
					expedicaoDiplomaVO.setNumeroProcesso(mascaraNumeroProcessoExpedicaoDiploma.toString());
				}
			}
		}
	
    public ExpedicaoDiplomaVO consultarPorMatriculaPrimeiraViaMontandoFuncionarioCargos(String matricula, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
    	ExpedicaoDiplomaVO expDiplomaVO = consultarPorMatriculaPrimeiraVia(matricula, controlarAcesso, nivelMontarDados, usuarioVO);
    	if (Uteis.isAtributoPreenchido(expDiplomaVO)) {
    		if (Uteis.isAtributoPreenchido(expDiplomaVO.getCargoFuncionarioPrincipalVO())) {
    			expDiplomaVO.setCargoFuncionarioPrincipalVO(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(expDiplomaVO.getCargoFuncionarioPrincipalVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
			}
			if (Uteis.isAtributoPreenchido(expDiplomaVO.getCargoFuncionarioSecundarioVO())) {
				expDiplomaVO.setCargoFuncionarioSecundarioVO(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(expDiplomaVO.getCargoFuncionarioSecundarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
			}
			if (Uteis.isAtributoPreenchido(expDiplomaVO.getCargoFuncionarioTerceiroVO())) {
				expDiplomaVO.setCargoFuncionarioTerceiroVO(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(expDiplomaVO.getCargoFuncionarioTerceiroVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
			}
			if (Uteis.isAtributoPreenchido(expDiplomaVO.getFuncionarioPrimarioVO()) && !Uteis.isAtributoPreenchido(expDiplomaVO.getCargoFuncionarioPrincipalVO())) {
				montarDadosCargoFuncionarioExpedicaoDiploma(expDiplomaVO.getFuncionarioPrimarioVO().getCodigo(), unidadeEnsino, expDiplomaVO::setCargoFuncionarioPrincipalVO, usuarioVO);
			}
			if (Uteis.isAtributoPreenchido(expDiplomaVO.getFuncionarioSecundarioVO()) && !Uteis.isAtributoPreenchido(expDiplomaVO.getCargoFuncionarioSecundarioVO())) {
				montarDadosCargoFuncionarioExpedicaoDiploma(expDiplomaVO.getFuncionarioSecundarioVO().getCodigo(), unidadeEnsino, expDiplomaVO::setCargoFuncionarioSecundarioVO, usuarioVO);
			}
			if (Uteis.isAtributoPreenchido(expDiplomaVO.getFuncionarioTerceiroVO()) && !Uteis.isAtributoPreenchido(expDiplomaVO.getCargoFuncionarioTerceiroVO())) {
				montarDadosCargoFuncionarioExpedicaoDiploma(expDiplomaVO.getFuncionarioTerceiroVO().getCodigo(), unidadeEnsino, expDiplomaVO::setCargoFuncionarioTerceiroVO, usuarioVO);
			}
            if (Uteis.isAtributoPreenchido(expDiplomaVO.getFuncionarioQuartoVO()) && !Uteis.isAtributoPreenchido(expDiplomaVO.getCargoFuncionarioQuartoVO())) {
                montarDadosCargoFuncionarioExpedicaoDiploma(expDiplomaVO.getFuncionarioQuartoVO().getCodigo(), unidadeEnsino, expDiplomaVO::setCargoFuncionarioQuartoVO, usuarioVO);
            }
            if (Uteis.isAtributoPreenchido(expDiplomaVO.getFuncionarioQuintoVO()) && !Uteis.isAtributoPreenchido(expDiplomaVO.getCargoFuncionarioQuintoVO())) {
                montarDadosCargoFuncionarioExpedicaoDiploma(expDiplomaVO.getFuncionarioQuintoVO().getCodigo(), unidadeEnsino, expDiplomaVO::setCargoFuncionarioQuintoVO, usuarioVO);
            }   
			return expDiplomaVO;
    	}
    	return null;
    }
    
    private void montarDadosCargoFuncionarioExpedicaoDiploma(Integer codigoFuncionario, Integer unidadeEnsino, Consumer<CargoVO> consumer, UsuarioVO usuarioVO) throws Exception {
    	List<FuncionarioCargoVO> consultarPorFuncionario = (List<FuncionarioCargoVO>) getFacadeFactory().getFuncionarioCargoFacade().consultarPorFuncionario(codigoFuncionario, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
		if (Uteis.isAtributoPreenchido(consultarPorFuncionario) && Uteis.isAtributoPreenchido(unidadeEnsino) && consumer != null) {
			consultarPorFuncionario.stream().filter(fcvo -> fcvo.getUnidade().getCodigo().equals(unidadeEnsino)).map(FuncionarioCargoVO::getCargo).findFirst().ifPresent(consumer);
		}
    }

	@Override
	public void montarListaExpedicaoDiplomaEmitirPorProgramacaoFormatura(ProgramacaoFormaturaVO programacaoFormaturaVO, List<ExpedicaoDiplomaVO> listaExpedicaoDiplomaVOs, List<ExpedicaoDiplomaVO> listaExpedicaoDiplomaVOsErro, Boolean trazerAlunosComDiplomaEmitido, UsuarioVO usuario) throws Exception {
		listaExpedicaoDiplomaVOs.clear();
		if (!trazerAlunosComDiplomaEmitido) {
			validarFiltroTrazerAlunosComDiplomaEmitido(programacaoFormaturaVO);
		}
		for (ProgramacaoFormaturaAlunoVO obj : programacaoFormaturaVO.getProgramacaoFormaturaAlunoVOs()) {
			ExpedicaoDiplomaVO expedicaoDiplomaVO = new ExpedicaoDiplomaVO();
			if (obj.getPossuiDiploma()) {
				expedicaoDiplomaVO = consultarPorMatricula(obj.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				Date dataexpedicao = consultarDataExpedicaoDiplomaPorMatricula(obj.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				if (Uteis.isAtributoPreenchido(dataexpedicao)) {
					expedicaoDiplomaVO.setDataExpedicao(dataexpedicao);
				}
			}
			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO)) {
				expedicaoDiplomaVO = carregarDadosCompletoExpedicaoDiploma(expedicaoDiplomaVO.getCodigo(), usuario);
			} else {
				expedicaoDiplomaVO.setMatricula((MatriculaVO) Uteis.clonar(obj.getMatricula()));
				getFacadeFactory().getMatriculaFacade().carregarDados(expedicaoDiplomaVO.getMatricula(), NivelMontarDados.TODOS, usuario);
				expedicaoDiplomaVO.setGradeCurricularVO(expedicaoDiplomaVO.getMatricula().getGradeCurricularAtual());
				expedicaoDiplomaVO.setCargaHorariaTotal(getFacadeFactory().getGradeCurricularFacade().consultarCargaHorariaExigidaGrade(expedicaoDiplomaVO.getMatricula().getGradeCurricularAtual().getCodigo(), usuario));
			}
			// Número Processo - Número de Registro do Diploma neste campo o sistema deverá
			// gerar o número de forma automática, usando a formula definida no configuração
			// acadêmica
			getFacadeFactory().getExpedicaoDiplomaFacade().montarNumeroProcessoERegistroDiplomaVindoMascaraConfiguracaoAcademico(expedicaoDiplomaVO, expedicaoDiplomaVO.getMatricula().getUnidadeEnsino().getCodigo(), expedicaoDiplomaVO.getMatricula().getCurso().getCodigo(), usuario);
			if (!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getDataConclusaoCurso()) && Uteis.isAtributoPreenchido(obj.getDataConclusaoCurso())) {
				expedicaoDiplomaVO.getMatricula().setDataConclusaoCurso(obj.getDataConclusaoCurso());
			}
			// Caso os numeros acima acima usarem a tag NR_LIVRO_EXPEDICAO e este aluno não
			// tiver o livro no registro do diploma o mesmo deve ser rejeitado a expedição
			// do
			// diploma com a seguintes mensagem "Não foi encontrado um registro no livro do
			// diploma para o aluno MATRICULA -  NOME,
			// para a geração do número do processo/registro diploma"
			validarViaExpedicaoDiplomaValida(expedicaoDiplomaVO);
			if (expedicaoDiplomaVO.getPossuiErro()) {
				expedicaoDiplomaVO.setConsistirException(new ConsistirException());
				expedicaoDiplomaVO.getConsistirException().getListaMensagemErro().add(expedicaoDiplomaVO.getErro());
				listaExpedicaoDiplomaVOsErro.add(expedicaoDiplomaVO);
			} else {
				expedicaoDiplomaVO.setPossuiDiploma(obj.getPossuiDiploma());
				if (!expedicaoDiplomaVO.getPossuiDiploma()) {
					expedicaoDiplomaVO.setVia("1");
				}
				expedicaoDiplomaVO.setSelecionado(Boolean.TRUE);
				listaExpedicaoDiplomaVOs.add(expedicaoDiplomaVO);
			}
		}
	}
	
	
	
	private  void validarFiltroTrazerAlunosComDiplomaEmitido(ProgramacaoFormaturaVO programacaoFormaturaVO) {	
			Iterator it =  programacaoFormaturaVO.getProgramacaoFormaturaAlunoVOs().iterator();
			while(it.hasNext()) {
				ProgramacaoFormaturaAlunoVO obj =  (ProgramacaoFormaturaAlunoVO) it.next();
				if(obj.getPossuiDiploma()) {
					it.remove();
				}
			}
			
	}

	
	private void validarDataInicioFimCurso(ExpedicaoDiplomaVO expedicaoDiplomaVO) throws Exception {
		try {
			if (expedicaoDiplomaVO.getMatricula().getDataInicioCurso() == null) {
				expedicaoDiplomaVO.getMatricula().setDataInicioCurso(getFacadeFactory().getHorarioTurmaDiaFacade().consultarPrimeiraDataAulaPorMatricula(expedicaoDiplomaVO.getMatricula().getMatricula()));
				 if(expedicaoDiplomaVO.getMatricula().getDataInicioCurso() == null) {
					expedicaoDiplomaVO.setPossuiErro(Boolean.TRUE);
				    expedicaoDiplomaVO.setErro("Não é possível realizar a expedição do diploma pois a matricula do aluno nao possui (Data Inicio do curso )");
				 }
			}
			if (expedicaoDiplomaVO.getMatricula().getDataConclusaoCurso() == null) {
				expedicaoDiplomaVO.getMatricula().setDataConclusaoCurso(getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaDataAulaPorMatricula(expedicaoDiplomaVO.getMatricula().getMatricula()));
			    if(expedicaoDiplomaVO.getMatricula().getDataConclusaoCurso() == null) {			    	
			    	expedicaoDiplomaVO.setPossuiErro(Boolean.TRUE);
			    	expedicaoDiplomaVO.setErro("Não é possível realizar a expedição do diploma pois a matricula do aluno nao possui (Data conclusão do curso )");
			    }
			}			
			
		}catch (Exception e) {
			expedicaoDiplomaVO.setPossuiErro(Boolean.TRUE);
	    	expedicaoDiplomaVO.setErro("Não é possível realizar a expedição do diploma pois a matricula do aluno nao possui (Data Inicio/conclusão do curso )");
		}
		
	}

	private void validarDocumentacaoEntregue(ExpedicaoDiplomaVO expedicaoDiplomaVO , ConfiguracaoGeralSistemaVO confgeral , UsuarioVO usuarioVO) throws Exception {
		if (confgeral.getNaoPermitirExpedicaoDiplomaDocumentacaoPendente()) {
			List<DocumetacaoMatriculaVO> listaDocumentacao = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarPorMatricula(expedicaoDiplomaVO.getMatricula().getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuarioVO);
			for (DocumetacaoMatriculaVO documetacaoMatriculaVO : listaDocumentacao) {
				if (!documetacaoMatriculaVO.getEntregue()) {
					expedicaoDiplomaVO.setPossuiErro(Boolean.TRUE);
					expedicaoDiplomaVO.setErro("Não é possível realizar a expedição do diploma pois o aluno possui documentação obrigatória pendente de entrega.");					
				}
			}
		}
		
	}
	
	
	
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
    public void realizarGeracaoXMLDiplomaDigital(ExpedicaoDiplomaVO expedicaoDiplomaVO, ConfiguracaoGEDVO configGEDVO, ConfiguracaoGeralSistemaVO config, File arquivoVisual, UsuarioVO usuarioVO, File arquivoVisualHistorico, TipoOrigemDocumentoAssinadoEnum tipoXmlGeracao, Boolean persistirDocumentoAssinado, Boolean adicionarSeloQrCode, HistoricoAlunoRelVO histAlunoRelVO) throws Exception {
    	if (!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getFuncionarioTerceiroVO()) && !tipoXmlGeracao.equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL)) {
    		throw new Exception("O Campo Responsável Assinatura IES Emissora é Obrigatório");
    	}
        ConsistirException consistirException = new ConsistirException();
        String nonce = RandomStringUtils.randomNumeric(44);
        if (tipoXmlGeracao.equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL)) {
        	File arquivoDiploma = criarXmlDiploma(expedicaoDiplomaVO, nonce, usuarioVO, consistirException);
        	getFacadeFactory().getDocumentoAssinadoFacade().realizarAssinaturaDiplomaDigital(expedicaoDiplomaVO, arquivoDiploma, arquivoVisual, config, TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL, usuarioVO, persistirDocumentoAssinado, histAlunoRelVO);
    		if (Uteis.isAtributoPreenchido(configGEDVO) && adicionarSeloQrCode && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getCodigoValidacaoDiplomaDigital())) {
    			expedicaoDiplomaVO.getDocumentoAssinadoVO().setCodigoValidacaoDiplomaDigital(expedicaoDiplomaVO.getCodigoValidacaoDiplomaDigital());
    			getFacadeFactory().getDocumentoAssinadoFacade().realizarVerificacaoProvedorDeAssinatura(expedicaoDiplomaVO.getDocumentoAssinadoVO(), expedicaoDiplomaVO.getDocumentoAssinadoVO().getUnidadeEnsinoVO().getCodigo(), true, true, arquivoVisual.getName(), null, "#000000", 80f, 200f, 8f, 380, 10, 0, 0, config, true, usuarioVO, true);
    		} else if (persistirDocumentoAssinado && expedicaoDiplomaVO.getGerarXMLDiploma() && (expedicaoDiplomaVO.getDocumentoAssinadoVO().getArquivo().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3) || expedicaoDiplomaVO.getDocumentoAssinadoVO().getArquivoVisual().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3))) {
    			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getDocumentoAssinadoVO().getArquivo())) {
    				File fileDiretorioCorreto = new File(config.getLocalUploadArquivoFixo() + File.separator + expedicaoDiplomaVO.getDocumentoAssinadoVO().getArquivo().getPastaBaseArquivo() + File.separator + expedicaoDiplomaVO.getDocumentoAssinadoVO().getArquivo().getNome());
    				File fileDiretorioTMP = new File(config.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP.getValue() + File.separator + expedicaoDiplomaVO.getDocumentoAssinadoVO().getArquivo().getNome());
    				if (!fileDiretorioCorreto.exists()) {
    					getFacadeFactory().getArquivoHelper().copiar(fileDiretorioTMP, fileDiretorioCorreto);
    				}
    				getFacadeFactory().getDocumentoAssinadoFacade().realizarUploadArquivoAmazon(expedicaoDiplomaVO.getDocumentoAssinadoVO().getArquivo(), config, true);
    			}
    			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getDocumentoAssinadoVO().getArquivoVisual())) {
    				File fileDiretorioCorreto = new File(config.getLocalUploadArquivoFixo() + File.separator + expedicaoDiplomaVO.getDocumentoAssinadoVO().getArquivoVisual().getPastaBaseArquivo() + File.separator + expedicaoDiplomaVO.getDocumentoAssinadoVO().getArquivoVisual().getNome());
    				File fileDiretorioTMP = new File(config.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP.getValue() + File.separator + expedicaoDiplomaVO.getDocumentoAssinadoVO().getArquivoVisual().getNome());
    				if (!fileDiretorioCorreto.exists()) {
    					getFacadeFactory().getArquivoHelper().copiar(fileDiretorioTMP, fileDiretorioCorreto);
    				}
    				getFacadeFactory().getDocumentoAssinadoFacade().realizarUploadArquivoAmazon(expedicaoDiplomaVO.getDocumentoAssinadoVO().getArquivoVisual(), config, true);
    			}
    		}
    		if (expedicaoDiplomaVO.getCodigoValidacaoDiplomaDigital() != null) {
    			alterarCodigoValidacaoDiploma(expedicaoDiplomaVO.getCodigoValidacaoDiplomaDigital(), expedicaoDiplomaVO.getCodigo(), usuarioVO, false);
    			expedicaoDiplomaVO.getDocumentoAssinadoVO().setCodigoValidacaoDiplomaDigital(expedicaoDiplomaVO.getCodigoValidacaoDiplomaDigital());
    		}
    		if (!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getCodigoValidacaoDiplomaDigital()) && arquivoVisual != null) {
    			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO) && expedicaoDiplomaVO.getGerarXMLDiploma()) {
    				if (!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getCodigoValidacaoDiplomaDigital()) && expedicaoDiplomaVO.getDocumentoAssinadoVO().getArquivoVisual() != null) {
    					if (expedicaoDiplomaVO.getDocumentoAssinadoVO().getArquivoVisual().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
    						ServidorArquivoOnlineS3RS servidorExternoAmazon = new ServidorArquivoOnlineS3RS(config.getUsuarioAutenticacao(), config.getSenhaAutenticacao(), config.getNomeRepositorio());
    						File fileAssinar = new File(getFacadeFactory().getArquivoFacade().realizarDownloadArquivoAmazon(expedicaoDiplomaVO.getDocumentoAssinadoVO().getArquivoVisual(), servidorExternoAmazon, config, false));
    						String arquivoOrigem = fileAssinar.getPath().contains(config.getLocalUploadArquivoFixo()) ? fileAssinar.getPath() : UteisJSF.getCaminhoWeb() + "relatorio/" + fileAssinar.getPath();
							getFacadeFactory().getDocumentoAssinadoFacade().adicionarMarcaDaguaPDF(arquivoOrigem, config, getCaminhoPastaWeb(), expedicaoDiplomaVO.getDocumentoAssinadoVO());
							getFacadeFactory().getDocumentoAssinadoFacade().realizarUploadArquivoAmazon(expedicaoDiplomaVO.getDocumentoAssinadoVO().getArquivoVisual(), config, true);
    					} else {
    						String nomeArquivoOrigem = config.getLocalUploadArquivoFixo() + File.separator + expedicaoDiplomaVO.getDocumentoAssinadoVO().getArquivoVisual().getPastaBaseArquivo() + File.separator + expedicaoDiplomaVO.getDocumentoAssinadoVO().getArquivoVisual().getNome();
    						String arquivoOrigem = nomeArquivoOrigem.contains(config.getLocalUploadArquivoFixo()) ? nomeArquivoOrigem : UteisJSF.getCaminhoWeb() + "relatorio/" + nomeArquivoOrigem;
    						getFacadeFactory().getDocumentoAssinadoFacade().adicionarMarcaDaguaPDF(arquivoOrigem, config, getCaminhoPastaWeb(), expedicaoDiplomaVO.getDocumentoAssinadoVO());	
    					}
    				}
    			}
    			
    		}
        } else if (tipoXmlGeracao.equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL)) {
        	File arquivoDocumentacao = criarXMLDocumentacaoAcademicaRegistro(expedicaoDiplomaVO, nonce, usuarioVO, config, consistirException);; 
        	getFacadeFactory().getDocumentoAssinadoFacade().realizarAssinaturaDiplomaDigital(expedicaoDiplomaVO, arquivoDocumentacao, null, config, TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL, usuarioVO, persistirDocumentoAssinado, histAlunoRelVO);
        } else if (tipoXmlGeracao.equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL)) {
        	File arquivoHistoricoEscolar = criarXMLHistoricoEscolar(expedicaoDiplomaVO, nonce, usuarioVO, histAlunoRelVO, consistirException);;
        	getFacadeFactory().getDocumentoAssinadoFacade().realizarAssinaturaDiplomaDigital(expedicaoDiplomaVO, arquivoHistoricoEscolar, arquivoVisualHistorico, config, TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL, usuarioVO, persistirDocumentoAssinado, histAlunoRelVO);
        	getFacadeFactory().getDocumentoAssinadoFacade().realizarVerificacaoProvedorDeAssinatura(expedicaoDiplomaVO.getDocumentoAssinadoVO(), expedicaoDiplomaVO.getDocumentoAssinadoVO().getUnidadeEnsinoVO().getCodigo(), true, true, arquivoVisualHistorico.getName(), null, "#000000", 80f, 200f, 8f, 380, 10, 0, 0, config, true, usuarioVO, true);
        }

    }

    public static void montarDadosFuncionarioQuarto(ExpedicaoDiplomaVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getFuncionarioQuartoVO().getCodigo().intValue() == 0) {
            obj.setFuncionarioQuartoVO(new FuncionarioVO());
            return;
        }
        obj.setFuncionarioQuartoVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(obj.getFuncionarioQuartoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
    }
    
    public static void montarDadosFuncionarioQuinto(ExpedicaoDiplomaVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getFuncionarioQuintoVO().getCodigo().intValue() == 0) {
            obj.setFuncionarioQuintoVO(new FuncionarioVO());
            return;
        }
        obj.setFuncionarioQuintoVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(obj.getFuncionarioQuintoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
    }
    
    @Override
    public String realizarGeracaoHashCodigoValidacao(ExpedicaoDiplomaVO expedicaoDiplomaVO, String livroRegistro, String numeroFolhaDoDiploma, String numeroSequenciaDoDiploma, UsuarioVO usuarioVO) throws Exception {
    	try {
	    	String hashCodigoValidacao = "";
	    	ControleLivroRegistroDiplomaVO livroRegistroDiplomaVO = new ControleLivroRegistroDiplomaVO();
	    	ControleLivroFolhaReciboVO livroFolhaReciboVO = new ControleLivroFolhaReciboVO();
	    	if (!expedicaoDiplomaVO.getInformarCamposLivroRegistradora()) {
	    		livroFolhaReciboVO = getFacadeFactory().getControleLivroFolhaReciboFacade().consultarPorMatriculaMatriculaPrimeiraEUltimaVia(expedicaoDiplomaVO.getMatricula().getMatricula(), expedicaoDiplomaVO.getMatricula().getCurso().getCodigo(), TipoLivroRegistroDiplomaEnum.DIPLOMA, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
	    		if (Uteis.isAtributoPreenchido(livroFolhaReciboVO)) {
	    			livroRegistro = Uteis.formatarStringXML(livroFolhaReciboVO.getControleLivroRegistroDiploma().getNrLivro().toString());
    				numeroFolhaDoDiploma = Uteis.formatarStringXML(livroFolhaReciboVO.getFolhaReciboAtual().toString());
    				numeroSequenciaDoDiploma = Uteis.formatarStringXML(livroFolhaReciboVO.getNumeroRegistro().toString());
	    		} else {
	    			livroRegistroDiplomaVO = getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarPorCodigoCurso(expedicaoDiplomaVO.getMatricula().getCurso().getCodigo(), null, TipoLivroRegistroDiplomaEnum.DIPLOMA, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
	    			if (Uteis.isAtributoPreenchido(livroRegistroDiplomaVO)) {
	    				livroRegistro = Uteis.formatarStringXML(livroRegistroDiplomaVO.getNrLivro().toString());
	    				numeroFolhaDoDiploma = Uteis.formatarStringXML(livroRegistroDiplomaVO.getNrFolhaRecibo().toString());
	    				numeroSequenciaDoDiploma = Uteis.formatarStringXML(livroRegistroDiplomaVO.getNumeroRegistro().toString());
	    			} else {
	    				livroRegistro = (Uteis.isAtributoPreenchido(Uteis.formatarStringXML(expedicaoDiplomaVO.getLivroRegistradora())) ? Uteis.formatarStringXML(expedicaoDiplomaVO.getLivroRegistradora()) : "");
	    				numeroFolhaDoDiploma = (Uteis.isAtributoPreenchido(Uteis.formatarStringXML(expedicaoDiplomaVO.getFolhaReciboRegistradora())) ? Uteis.formatarStringXML(expedicaoDiplomaVO.getFolhaReciboRegistradora()) : "");
	    				numeroSequenciaDoDiploma = (Uteis.isAtributoPreenchido(Uteis.formatarStringXML(expedicaoDiplomaVO.getNumeroRegistroDiploma())) ? Uteis.formatarStringXML(expedicaoDiplomaVO.getNumeroRegistroDiploma()) : "");
	    			}
	    		}
	    	}
	    	if (Uteis.isAtributoPreenchido(livroRegistro) && Uteis.isAtributoPreenchido(numeroFolhaDoDiploma) && Uteis.isAtributoPreenchido(numeroSequenciaDoDiploma)) {
	    		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getAluno().getCPF().trim()) &&	Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getCurso().getCodigoEMEC()) &&	Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCNPJ().trim()) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCnpjUnidadeCertificadora().trim()) &&	Uteis.isAtributoPreenchido(livroRegistro.toString().trim()) && Uteis.isAtributoPreenchido(numeroFolhaDoDiploma.toString().trim()) &&	Uteis.isAtributoPreenchido(numeroSequenciaDoDiploma.toString().trim())) {
	    			hashCodigoValidacao = Uteis.removerMascara(Uteis.formatarStringXML(expedicaoDiplomaVO.getMatricula().getAluno().getCPF())) + Long.valueOf(expedicaoDiplomaVO.getMatricula().getCurso().getCodigoEMEC()) + Uteis.removerMascara(Uteis.formatarStringXML(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCNPJ())) + Uteis.removerMascara(Uteis.formatarStringXML(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCnpjUnidadeCertificadora()) + Uteis.formatarStringXML(livroRegistro.toString()) + Uteis.formatarStringXML(numeroFolhaDoDiploma.toString()) + Uteis.formatarStringXML(numeroSequenciaDoDiploma.toString()));
	    			hashCodigoValidacao = DigestUtils.sha256Hex(hashCodigoValidacao);
	    			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigoIES()) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigoIESUnidadeCertificadora())) {
	    				hashCodigoValidacao = StringUtils.join(Arrays.asList(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigoIES(), ".", expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigoIESUnidadeCertificadora(), ".", hashCodigoValidacao).toArray());
	    			}
	    		}
	    	} else if (Uteis.isAtributoPreenchido(livroRegistro) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getNumeroRegistroDiploma()) && !Uteis.isAtributoPreenchido(numeroFolhaDoDiploma)) {
	    		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getAluno().getCPF().trim()) &&	Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getCurso().getCodigoEMEC()) &&	Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCNPJ().trim()) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCnpjUnidadeCertificadora().trim()) &&	Uteis.isAtributoPreenchido(livroRegistro.toString().trim()) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getNumeroRegistroDiploma().toString().trim())) {
	    			hashCodigoValidacao = Uteis.removerMascara(Uteis.formatarStringXML(expedicaoDiplomaVO.getMatricula().getAluno().getCPF())) + Long.valueOf(expedicaoDiplomaVO.getMatricula().getCurso().getCodigoEMEC()) + Uteis.removerMascara(Uteis.formatarStringXML(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCNPJ())) + Uteis.removerMascara(Uteis.formatarStringXML(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCnpjUnidadeCertificadora()) + Uteis.formatarStringXML(livroRegistro.toString()) + Uteis.formatarStringXML(expedicaoDiplomaVO.getNumeroRegistroDiploma().toString()));
	    			hashCodigoValidacao = DigestUtils.sha256Hex(hashCodigoValidacao);
	    			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigoIES()) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigoIESUnidadeCertificadora())) {
	    				hashCodigoValidacao = StringUtils.join(Arrays.asList(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigoIES(), ".", expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigoIESUnidadeCertificadora(), ".", hashCodigoValidacao).toArray());
	    			}
	    		}
	    	}
	    	
	    	return hashCodigoValidacao;
    	} catch (Exception e) {
			throw e;
		}
    }
    
    
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarCodigoValidacaoDiploma(final String codigoValidacao, final Integer codigo, UsuarioVO usuario, Boolean controlarAcesso) throws Exception {
		try {
			ExpedicaoDiploma.alterar(getIdEntidade(), controlarAcesso, usuario);
			final String sql = "UPDATE ExpedicaoDiploma set codigoValidacaoDiplomaDigital=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    if (codigoValidacao == null) {
                        sqlAlterar.setNull(1, 0);
                    } else {
                        sqlAlterar.setString(1, codigoValidacao);
                    }
					sqlAlterar.setInt(2, codigo);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarGeracaoRepresentacaoVisualDiplomaDigital(ExpedicaoDiplomaVO expedicaoDiplomaVO) throws Exception {
	    ExpedicaoDiplomaControle expedicaoDiplomaControle = new ExpedicaoDiplomaControle();
	    expedicaoDiplomaControle.setExpedicaoDiplomaVO(expedicaoDiplomaVO);
	    expedicaoDiplomaControle.setFuncionarioPrincipalVO(expedicaoDiplomaVO.getFuncionarioPrimarioVO());
	    expedicaoDiplomaControle.setFuncionarioSecundarioVO(expedicaoDiplomaVO.getFuncionarioSecundarioVO());
	    expedicaoDiplomaControle.setFuncionarioTerceiroVO(expedicaoDiplomaVO.getFuncionarioTerceiroVO());
	    expedicaoDiplomaControle.setFuncionarioQuartoVO(expedicaoDiplomaVO.getFuncionarioQuartoVO());
	    expedicaoDiplomaControle.setFuncionarioQuintoVO(expedicaoDiplomaVO.getFuncionarioQuintoVO());
	    expedicaoDiplomaControle.setCargoFuncionarioPrincipal(expedicaoDiplomaVO.getCargoFuncionarioPrincipalVO());
	    expedicaoDiplomaControle.setCargoFuncionarioSecundario(expedicaoDiplomaVO.getCargoFuncionarioSecundarioVO());
	    expedicaoDiplomaControle.setCargoFuncionarioTerceiro(expedicaoDiplomaVO.getCargoFuncionarioTerceiroVO());
	    expedicaoDiplomaControle.setCargoFuncionarioQuarto(expedicaoDiplomaVO.getCargoFuncionarioQuartoVO());
	    expedicaoDiplomaControle.setCargoFuncionarioQuinto(expedicaoDiplomaVO.getCargoFuncionarioQuintoVO());
	    expedicaoDiplomaControle.setTipoLayout(expedicaoDiplomaVO.getLayoutDiploma());
	    expedicaoDiplomaControle.setTextoPadraoDeclaracao(expedicaoDiplomaVO.getTextoPadrao().getCodigo());
	    expedicaoDiplomaControle.setAssinarDigitalmente(true);
	    expedicaoDiplomaControle.setLancarExcecao(true);
	    expedicaoDiplomaControle.getExpedicaoDiplomaVO().setNovaGeracaoRepresentacaoVisualDiplomaDigital(true);
	    if (!expedicaoDiplomaVO.getGerarXMLDiploma()) {
	    	throw new Exception("Não é possível regerar a REPRESENTAÇÃO VISUAL caso não esteja marcada para (Gerar XML Diploma)");
	    }
	    expedicaoDiplomaControle.imprimirPDF();
	}
	
	private File criarXmlDiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO, String nonce, UsuarioVO usuarioVO, ConsistirException consistirException) throws Exception {
    	switch (expedicaoDiplomaVO.getVersaoDiploma()) {
		case VERSAO_1_05:
			return getFacadeFactory().getExpedicaoDiplomaDigital_1_05_interfaceFacade().criarXMLDiploma(expedicaoDiplomaVO, nonce, usuarioVO, consistirException);
		}
		throw new Exception("Erro ao criar xml de Diploma Digital");
    }
    
    private File criarXMLDocumentacaoAcademicaRegistro(ExpedicaoDiplomaVO expedicaoDiplomaVO, String nonce, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO config, ConsistirException consistirException) throws Exception {
    	switch (expedicaoDiplomaVO.getVersaoDiploma()) {
		case VERSAO_1_05:
			return getFacadeFactory().getExpedicaoDiplomaDigital_1_05_interfaceFacade().criarXMLDocumentacaoAcademicaRegistro(expedicaoDiplomaVO, nonce, config, usuarioVO, consistirException);
		}
		throw new Exception("Erro ao criar xml de Documentação Acadêmica");
    }
    
    private File criarXMLHistoricoEscolar(ExpedicaoDiplomaVO expedicaoDiplomaVO, String nonce, UsuarioVO usuarioVO, HistoricoAlunoRelVO histAlunoRelVO, ConsistirException consistirException) throws Exception {
    	switch (expedicaoDiplomaVO.getVersaoDiploma()) {
		case VERSAO_1_05:
			return getFacadeFactory().getExpedicaoDiplomaDigital_1_05_interfaceFacade().criarXMLHistoricoEscolar(expedicaoDiplomaVO, nonce, usuarioVO, consistirException, histAlunoRelVO);
		}
		throw new Exception("Erro ao criar xml de Histórico Escolar");
    }
    
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String realizarImpressaoExpedicaoDiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO, Boolean assinarDigitalmente, Boolean gerarDiplomaXML, Boolean gerarDiplomaXmlLote, SuperParametroRelVO superParametroRelVO, SuperControleRelatorio superControleRelatorio, String tipoLayout, List<String> listaMensagemErro, String caminhoPastaWeb, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		List<DiplomaAlunoRelVO> listaObjetos = new ArrayList<DiplomaAlunoRelVO>(0);
		try {
			expedicaoDiplomaVO.setImprimirContrato(Boolean.FALSE);
			expedicaoDiplomaVO.setAbrirModalVerso(Constantes.EMPTY);
			expedicaoDiplomaVO.setAbrirModalOK(Boolean.FALSE);
			superControleRelatorio.setCaminhoRelatorio(Constantes.EMPTY);
			superControleRelatorio.setFazerDownload(Boolean.FALSE);
			listaMensagemErro.clear();
			superControleRelatorio.setListaMensagemErro(null);
			limparDadosGerarXmlDiploma(expedicaoDiplomaVO, gerarDiplomaXmlLote);
			superParametroRelVO.setTituloRelatorio("Diploma do Aluno");
			superParametroRelVO.setNomeEmpresa(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getNome());
			if (expedicaoDiplomaVO.getGerarXMLDiploma() && !expedicaoDiplomaVO.getNovaGeracaoRepresentacaoVisualDiplomaDigital()) {
				if (!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMotivoRejeicaoDiplomaDigital()) && expedicaoDiplomaVO.getExisteDiplomaDigitalGerado()) {
					throw new Exception("O motivo da rejeição deve ser informado.");
				}
				montarConfiguracaoDiploma(expedicaoDiplomaVO, usuario);
				montarDadosCoordenadorCursoDiploma(expedicaoDiplomaVO, usuario);
			}
			if (!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora())) {
				throw new Exception("O campo Unidade Ensino Expedição Diploma deve ser informado.");
			} else {
				if (expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getNovoObj()) {
					expedicaoDiplomaVO.setUnidadeEnsinoCertificadora(getAplicacaoControle().getUnidadeEnsinoVO(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigo(), usuario));
				}
			}
			expedicaoDiplomaVO.setAbrirModalVerso(Constantes.EMPTY);
			expedicaoDiplomaVO.setImprimirContrato(Boolean.FALSE);
			validarImprimirDiploma(expedicaoDiplomaVO, expedicaoDiplomaVO.getFuncionarioPrimarioVO().getCodigo(), expedicaoDiplomaVO.getFuncionarioSecundarioVO().getCodigo(), expedicaoDiplomaVO.getCargoFuncionarioPrincipalVO().getCodigo(), expedicaoDiplomaVO.getCargoFuncionarioSecundarioVO().getCodigo(), expedicaoDiplomaVO.getFuncionarioTerceiroVO().getCodigo(), expedicaoDiplomaVO.getCargoFuncionarioTerceiroVO().getCodigo(), tipoLayout, usuario, expedicaoDiplomaVO.getGerarXMLDiploma());
			if (expedicaoDiplomaVO.getMatricula().getDataConclusaoCurso() != null && expedicaoDiplomaVO.getMatricula().getDataInicioCurso() != null) {
				listaObjetos = getFacadeFactory().getDiplomaAlunoRelFacade().criarObjeto(expedicaoDiplomaVO.getUtilizarUnidadeMatriz(), expedicaoDiplomaVO, expedicaoDiplomaVO.getFuncionarioPrimarioVO(), expedicaoDiplomaVO.getFuncionarioSecundarioVO(), expedicaoDiplomaVO.getFuncionarioTerceiroVO(), expedicaoDiplomaVO.getCargoFuncionarioPrincipalVO(), expedicaoDiplomaVO.getCargoFuncionarioSecundarioVO(), expedicaoDiplomaVO.getCargoFuncionarioTerceiroVO(), expedicaoDiplomaVO.getTituloFuncionarioPrincipal(), expedicaoDiplomaVO.getTituloFuncionarioSecundario(), usuario, tipoLayout, expedicaoDiplomaVO.getCargoFuncionarioQuartoVO(), expedicaoDiplomaVO.getCargoFuncionarioQuintoVO(), expedicaoDiplomaVO.getFuncionarioQuartoVO(), expedicaoDiplomaVO.getFuncionarioQuintoVO(), Boolean.TRUE);
				validarRegraEmissao(expedicaoDiplomaVO, usuario);
				expedicaoDiplomaVO.setLayoutDiploma(tipoLayout);
				alterarFuncionarioResponsavel(expedicaoDiplomaVO, usuario);
				if (tipoLayout.equals("TextoPadrao")) {
					if (!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getTextoPadrao())) {
						throw new ConsistirException("Necessário o informar  Tipo Layout (Texto Padrão).");
					}
					ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
					ConfiguracaoGEDVO configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigo(), usuario);
					impressaoContratoVO.setConfiguracaoGEDVO(configGEDVO);
					impressaoContratoVO.setGerarNovoArquivoAssinado(true);
					impressaoContratoVO.getMatriculaVO().setObservacaoDiploma(expedicaoDiplomaVO.getObservacao());
					impressaoContratoVO.setFuncionarioPrincipalVO((FuncionarioVO) Uteis.clonar(expedicaoDiplomaVO.getFuncionarioPrimarioVO()));
					impressaoContratoVO.setFuncionarioSecundarioVO((FuncionarioVO) Uteis.clonar(expedicaoDiplomaVO.getFuncionarioSecundarioVO()));
					impressaoContratoVO.setFuncionarioTerceiroVO((FuncionarioVO) Uteis.clonar(expedicaoDiplomaVO.getFuncionarioTerceiroVO()));
					impressaoContratoVO.setCargoFuncionarioPrincipal((CargoVO) Uteis.clonar(expedicaoDiplomaVO.getCargoFuncionarioPrincipalVO()));
					impressaoContratoVO.setCargoFuncionarioSecundario((CargoVO) Uteis.clonar(expedicaoDiplomaVO.getCargoFuncionarioSecundarioVO()));
					impressaoContratoVO.setCargoFuncionarioTerceiro((CargoVO) Uteis.clonar(expedicaoDiplomaVO.getCargoFuncionarioTerceiroVO()));
					impressaoContratoVO.setObservacaoComplementarDiplomaVOs(listaObjetos.get(0).getObservacaoComplementarDiplomaVOs());
					impressaoContratoVO.setNumeroRegistroDiploma(expedicaoDiplomaVO.getNumeroRegistroDiploma());
					impressaoContratoVO.setExpedicaoDiplomaVO(expedicaoDiplomaVO);
					if (!impressaoContratoVO.getExpedicaoDiplomaVO().getVia().equals("1")) {
						impressaoContratoVO.setPrimeiraViaExpedicaoDiplomaVO(consultarPorMatriculaPrimeiraViaMontandoFuncionarioCargos(expedicaoDiplomaVO.getMatricula().getMatricula(), expedicaoDiplomaVO.getMatricula().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
					}
					TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(expedicaoDiplomaVO.getTextoPadrao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
					impressaoContratoVO.setGerarNovoArquivoAssinado((assinarDigitalmente && textoPadraoDeclaracaoVO.getAssinarDigitalmenteTextoPadrao()) || expedicaoDiplomaVO.getGerarXMLDiploma());
					if (assinarDigitalmente && !textoPadraoDeclaracaoVO.getAssinarDigitalmenteTextoPadrao()) {
						textoPadraoDeclaracaoVO.setAssinarDigitalmenteTextoPadrao(true);
						textoPadraoDeclaracaoVO.setCorAssinaturaDigitalmente("#000000");
						textoPadraoDeclaracaoVO.setAlinhamentoAssinaturaDigitalEnum(AlinhamentoAssinaturaDigitalEnum.RODAPE_DIREITA);
						textoPadraoDeclaracaoVO.setAlturaAssinatura(40f);
						textoPadraoDeclaracaoVO.setLarguraAssinatura(200f);
					}
					impressaoContratoVO.setTipoImpressaoContrato(assinarDigitalmente && expedicaoDiplomaVO.getGerarXMLDiploma() ? "DIPLOMA_DIGITAL" : "EXPEDICAO_DIPLOMA");
					if ((expedicaoDiplomaVO.getGerarXMLDiploma() && !Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getCodigoValidacaoDiplomaDigital())) || (!expedicaoDiplomaVO.getNovaGeracaoRepresentacaoVisualDiplomaDigital() && !gerarDiplomaXmlLote)) {
						String hashCodigoValidacao = getFacadeFactory().getExpedicaoDiplomaFacade().realizarGeracaoHashCodigoValidacao(expedicaoDiplomaVO, expedicaoDiplomaVO.getLivroRegistradora().trim(), expedicaoDiplomaVO.getFolhaReciboRegistradora().trim(), expedicaoDiplomaVO.getNumeroRegistroDiploma().trim(), usuario);
						if (Uteis.isAtributoPreenchido(hashCodigoValidacao)) {
							expedicaoDiplomaVO.setCodigoValidacaoDiplomaDigital(hashCodigoValidacao);
						} else {
							expedicaoDiplomaVO.setCodigoValidacaoDiplomaDigital(Constantes.EMPTY);
						}
					} else if (!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getCodigoValidacaoDiplomaDigital())) {
						expedicaoDiplomaVO.setCodigoValidacaoDiplomaDigital(Constantes.EMPTY);
					}
					String textoStr = getFacadeFactory().getImpressaoContratoFacade().montarDadosContratoTextoPadrao(expedicaoDiplomaVO.getMatricula(), impressaoContratoVO, textoPadraoDeclaracaoVO, configuracaoGeralSistemaVO, usuario);
					if (textoPadraoDeclaracaoVO.getTipoDesigneTextoEnum().isHtml()) {
						HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
						request.getSession().setAttribute("textoRelatorio", textoStr);
						superControleRelatorio.setCaminhoRelatorio(Constantes.EMPTY);
					} else {
						superControleRelatorio.setCaminhoRelatorio(getFacadeFactory().getImpressaoDeclaracaoFacade().executarValidacaoImpressaoEmPdf(impressaoContratoVO, textoPadraoDeclaracaoVO, Constantes.EMPTY, true, configuracaoGeralSistemaVO, usuario));
					}
					if (superControleRelatorio.getCaminhoRelatorio().isEmpty()) {
						expedicaoDiplomaVO.setImprimirContrato(Boolean.TRUE);
						superControleRelatorio.setFazerDownload(Boolean.FALSE);
					} else if (expedicaoDiplomaVO.getGerarXMLDiploma()) {
						superControleRelatorio.setFazerDownload(Boolean.FALSE);
						expedicaoDiplomaVO.setImprimirContrato(Boolean.FALSE);
					} else {
						expedicaoDiplomaVO.setImprimirContrato(Boolean.FALSE);
						superControleRelatorio.setFazerDownload(Boolean.TRUE);
					}
					if (!gerarDiplomaXmlLote) {
						persistirLayoutPadrao(expedicaoDiplomaVO, tipoLayout, usuario);
						expedicaoDiplomaVO.setAbrirModalVerso("RichFaces.$('panelAvisoAssinaturaDigital').hide(); RichFaces.$('panelVersoDiploma').show();");
						expedicaoDiplomaVO.setAbrirModalOK(Boolean.TRUE);
					}
					if (assinarDigitalmente && !gerarDiplomaXmlLote) {
						disponibilizarArquivoExpedicaoDiplomaCertisign(expedicaoDiplomaVO, impressaoContratoVO.getDocumentoAssinado(), superControleRelatorio, configuracaoGeralSistemaVO);
						disponibilizarArquivoExpedicaoDiplomaTechCert(expedicaoDiplomaVO, impressaoContratoVO.getDocumentoAssinado(), superControleRelatorio, configuracaoGeralSistemaVO);
					}
					if (assinarDigitalmente) {
						persistirExpedicaoDiplomaComDocumentoAssinado(expedicaoDiplomaVO, impressaoContratoVO.getDocumentoAssinado(), usuario);
					}
				} else {
					String design = getFacadeFactory().getDiplomaAlunoRelFacade().getDesignIReportRelatorio(tipoLayout, expedicaoDiplomaVO.getMatricula().getCurso().getNivelEducacional());
					superParametroRelVO.setNomeDesignIreport(design);
					superParametroRelVO.setSubReport_Dir(getFacadeFactory().getDiplomaAlunoRelFacade().getCaminhoBaseRelatorio());
					superParametroRelVO.setNomeUsuario(usuario.getNome());
					superParametroRelVO.setListaObjetos(listaObjetos);
					superParametroRelVO.setCaminhoBaseRelatorio(getFacadeFactory().getDiplomaAlunoRelFacade().getCaminhoBaseRelatorio());
					superParametroRelVO.setVersaoSoftware(superControleRelatorio.getVersaoSistema());
					superParametroRelVO.getParametros().put("simboloBandeiraPretoBranco", caminhoPastaWeb + File.separator + "resources" + File.separator + "imagens" + File.separator + "simboloBandeiraPretoBranco.jpg");
					superParametroRelVO.getParametros().put("bordaDiploma", caminhoPastaWeb + File.separator + "resources" + File.separator + "imagens" + File.separator + "bordaDiplom.png");
					UnidadeEnsinoVO unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(expedicaoDiplomaVO.getMatricula().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
					if (unidadeEnsino.getNomeExpedicaoDiploma().trim().isEmpty()) {
						superParametroRelVO.setUnidadeEnsino(unidadeEnsino.getNome());
					} else {
						superParametroRelVO.setUnidadeEnsino(unidadeEnsino.getNomeExpedicaoDiploma());
					}
					superParametroRelVO.adicionarParametro("dataPorExtenso", Uteis.getDataCidadeEstadoDiaMesPorExtensoEAno(unidadeEnsino.getCidade().getNome(), unidadeEnsino.getCidade().getEstado().getSigla(), new Date(), true));
					superParametroRelVO.setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
					superParametroRelVO.adicionarParametro("origemRelatorio", "diploma");
					superParametroRelVO.setMatricula(expedicaoDiplomaVO.getMatricula().getMatricula());
					superParametroRelVO.setAluno(expedicaoDiplomaVO.getMatricula().getAluno().getNome());

					expedicaoDiplomaVO.getMatricula().setObservacaoDiploma(expedicaoDiplomaVO.getObservacao());
					if (expedicaoDiplomaVO.getSegundaVia()) {
						ExpedicaoDiplomaVO exp = consultarPorMatriculaPrimeiraVia(expedicaoDiplomaVO.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
						String operacao = "Alterar";
						if (exp == null) {
							exp = new ExpedicaoDiplomaVO();
							operacao = "Incluir";
							exp.setMatricula(expedicaoDiplomaVO.getMatricula());
							exp.setVia("1");
							exp.setGradeCurricularVO(expedicaoDiplomaVO.getGradeCurricularVO());
							exp.setUnidadeEnsinoCertificadora(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora());
						}
						exp.setNumeroRegistroDiploma(expedicaoDiplomaVO.getNumeroRegistroDiplomaViaAnterior());
						exp.setNumeroProcesso(expedicaoDiplomaVO.getNumeroProcessoViaAnterior());
						exp.setDataExpedicao(expedicaoDiplomaVO.getDataRegistroDiplomaViaAnterior());
						exp.setFuncionarioPrimarioVO(expedicaoDiplomaVO.getSecretariaRegistroDiplomaViaAnterior());
						exp.setFuncionarioSecundarioVO(expedicaoDiplomaVO.getReitorRegistroDiplomaViaAnterior());
						exp.setCargoFuncionarioSecundarioVO(expedicaoDiplomaVO.getCargoReitorRegistroDiplomaViaAnterior());
						if (operacao.equals("Alterar")) {
							alterar(exp, usuario, false);
						} else {
							incluir(exp, usuario, false, false);
						}
						alterarFuncionarioResponsavel(exp, usuario);
					}
					adicionarParametrosAssinaturaDigital(expedicaoDiplomaVO, superParametroRelVO, superControleRelatorio, configuracaoGeralSistemaVO, usuario);
					superControleRelatorio.realizarImpressaoRelatorio();
					persistirLayoutPadrao(expedicaoDiplomaVO, design, usuario);
					if (assinarDigitalmente) {
						ConfiguracaoGEDVO configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigo(), usuario);
						File arquivoVisual = realizarGeracaoVersoDiplomaUnificarComFrenteDiploma(expedicaoDiplomaVO.getMatricula(), superParametroRelVO, expedicaoDiplomaVO, superControleRelatorio, caminhoPastaWeb, usuario, tipoLayout, configuracaoGeralSistemaVO);
						File arquivoVisualHistoricoEscolar = null;
						if ((expedicaoDiplomaVO.getMatricula().getCurso().getNivelEducacionalGraduacaoGraduacaoTecnologica()) && expedicaoDiplomaVO.getGerarXMLDiploma()) {
							getFacadeFactory().getDocumentoAssinadoPessoaFacade().realizarRejeicaoDocumentosAssinados(expedicaoDiplomaVO, expedicaoDiplomaVO.getExisteDiplomaDigitalGerado(), usuario);
							realizarGeracaoXMLDiplomaDigital(expedicaoDiplomaVO, configGEDVO, configuracaoGeralSistemaVO, arquivoVisual, usuario, arquivoVisualHistoricoEscolar, TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL, true, true, null);
							superControleRelatorio.setFazerDownload(Boolean.FALSE);
						} else {
							expedicaoDiplomaVO.setDocumentoAssinadoVO(getFacadeFactory().getDocumentoAssinadoFacade().realizarAssinaturaUnidadeCertificadoraExpedicaoDiploma(expedicaoDiplomaVO.getMatricula(), expedicaoDiplomaVO, configGEDVO, arquivoVisual, expedicaoDiplomaVO.getFuncionarioPrimarioVO(), expedicaoDiplomaVO.getCargoFuncionarioPrincipalVO().getNome(), expedicaoDiplomaVO.getTituloFuncionarioPrincipal(), expedicaoDiplomaVO.getFuncionarioSecundarioVO(), expedicaoDiplomaVO.getCargoFuncionarioSecundarioVO().getNome(), expedicaoDiplomaVO.getTituloFuncionarioSecundario(), expedicaoDiplomaVO.getFuncionarioTerceiroVO(), expedicaoDiplomaVO.getCargoFuncionarioTerceiroVO().getNome(), expedicaoDiplomaVO.getTituloFuncionarioTerceiro(), superControleRelatorio.getCaminhoRelatorio(), configuracaoGeralSistemaVO, tipoLayout, usuario));
							if (!gerarDiplomaXmlLote && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getDocumentoAssinadoVO()) && expedicaoDiplomaVO.getDocumentoAssinadoVO().getArquivo().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
								superControleRelatorio.setCaminhoRelatorio(expedicaoDiplomaVO.getDocumentoAssinadoVO().getCaminhoPreview());
								superControleRelatorio.setUtilizarCaminhoDownloadArquivoAssinadoDigitalmente(Boolean.FALSE);
							}
							if (assinarDigitalmente && !gerarDiplomaXmlLote) {
								disponibilizarArquivoExpedicaoDiplomaCertisign(expedicaoDiplomaVO, expedicaoDiplomaVO.getDocumentoAssinadoVO(), superControleRelatorio, configuracaoGeralSistemaVO);
								disponibilizarArquivoExpedicaoDiplomaTechCert(expedicaoDiplomaVO, expedicaoDiplomaVO.getDocumentoAssinadoVO(), superControleRelatorio, configuracaoGeralSistemaVO);
							}
						}
						persistirExpedicaoDiplomaComDocumentoAssinado(expedicaoDiplomaVO, expedicaoDiplomaVO.getDocumentoAssinadoVO(), usuario);
					}
				}
			}
			return superControleRelatorio.getCaminhoRelatorio();
		} catch (ConsistirException ce) {
			expedicaoDiplomaVO.setImprimirContrato(false);
			superControleRelatorio.setFazerDownload(false);
			expedicaoDiplomaVO.setAbrirModalOK(Boolean.FALSE);
			if (gerarDiplomaXML) {
				if (!ce.getListaMensagemErro().isEmpty()) {
					listaMensagemErro.addAll(ce.getListaMensagemErro());
				} else {
					listaMensagemErro.add(ce.getMessage());
				}
			}
			if (gerarDiplomaXmlLote) {
				if (ce.getListaMensagemErro().isEmpty()) {
					expedicaoDiplomaVO.getConsistirException().getListaMensagemErro().add(ce.getMessage());
				} else {
					expedicaoDiplomaVO.getConsistirException().getListaMensagemErro().addAll(ce.getListaMensagemErro());
				}
			}
			throw ce;
		} catch (Exception e) {
			expedicaoDiplomaVO.setImprimirContrato(false);
			superControleRelatorio.setFazerDownload(false);
			expedicaoDiplomaVO.setAbrirModalOK(Boolean.FALSE);
			if (gerarDiplomaXmlLote) {
				expedicaoDiplomaVO.getConsistirException().getListaMensagemErro().add(e.getMessage());
			}
			throw e;
		} finally {
			listaObjetos = null;
		}
	}
	
	private void disponibilizarArquivoExpedicaoDiplomaCertisign(ExpedicaoDiplomaVO expedicaoDiploma, DocumentoAssinadoVO documentoAssinadoVO, SuperControleRelatorio superControleRelatorio, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		if (Uteis.isAtributoPreenchido(documentoAssinadoVO) && documentoAssinadoVO.getProvedorDeAssinaturaEnum().equals(ProvedorDeAssinaturaEnum.CERTISIGN) && !documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().isXmlMec()) {
			getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorCertisign(documentoAssinadoVO, configuracaoGeralSistemaVO, new UsuarioVO());
			String caminhoArquivoFixo = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + documentoAssinadoVO.getArquivo().getPastaBaseArquivo() + File.separator + documentoAssinadoVO.getArquivo().getNome();
			File fileFixo = new File(caminhoArquivoFixo);
			
			String caminhoArquivoDownload = getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + documentoAssinadoVO.getArquivo().getNome();
			File fileDownload = new File(caminhoArquivoDownload);
			if (Uteis.isAtributoPreenchido(fileDownload)) {
				fileDownload.delete();
			}
			getFacadeFactory().getArquivoHelper().copiar(fileFixo, fileDownload);
			superControleRelatorio.setCaminhoRelatorio(documentoAssinadoVO.getArquivo().getNome());
		}
	}

	private void disponibilizarArquivoExpedicaoDiplomaTechCert(ExpedicaoDiplomaVO expedicaoDiploma, DocumentoAssinadoVO documentoAssinadoVO, SuperControleRelatorio superControleRelatorio, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		if (Uteis.isAtributoPreenchido(documentoAssinadoVO) && documentoAssinadoVO.getProvedorDeAssinaturaEnum().equals(ProvedorDeAssinaturaEnum.TECHCERT) && !documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().isXmlMec()) {
			getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorTechCert(documentoAssinadoVO, configuracaoGeralSistemaVO, new UsuarioVO());
			String caminhoArquivoFixo = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + documentoAssinadoVO.getArquivo().getPastaBaseArquivo() + File.separator + documentoAssinadoVO.getArquivo().getNome();
			File fileFixo = new File(caminhoArquivoFixo);

			String caminhoArquivoDownload = getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + documentoAssinadoVO.getArquivo().getNome();
			File fileDownload = new File(caminhoArquivoDownload);
			if (Uteis.isAtributoPreenchido(fileDownload)) {
				fileDownload.delete();
			}
			getFacadeFactory().getArquivoHelper().copiar(fileFixo, fileDownload);
			superControleRelatorio.setCaminhoRelatorio(documentoAssinadoVO.getArquivo().getNome());
		}
	}
	
	private File realizarGeracaoVersoDiplomaUnificarComFrenteDiploma(MatriculaVO matriculaVO, SuperParametroRelVO superParametroRelVO, ExpedicaoDiplomaVO expedicaoDiplomaVO, SuperControleRelatorio superControleRelatorio, String caminhoPastaWeb, UsuarioVO usuarioVO, String tipoLayout, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		String caminhoFrente = superControleRelatorio.getCaminhoRelatorio();
		imprimirHistoricoPosPaisagem(expedicaoDiplomaVO, superParametroRelVO, superControleRelatorio, tipoLayout, usuarioVO, configuracaoGeralSistema);
		String caminhoVerso = superControleRelatorio.getCaminhoRelatorio();
		String matricula = "";
		if (matriculaVO.getMatricula().contains("/") || matriculaVO.getMatricula().contains("\"")) {
			matricula = matriculaVO.getMatricula().replaceAll("/", "_");
			matricula = matricula.replaceAll("\"", "_");
		} else {
			matricula = matriculaVO.getMatricula();
		}
		String nomeNovoArquivo = "Diploma_" + matricula + "_" + expedicaoDiplomaVO.getVia() + Uteis.removeCaractersEspeciais2(Uteis.getDataComHoraCompleta(new Date()).replaceAll("/", "-").replaceAll(" ", "_")) + ".pdf";
		String caminhoBasePdf = caminhoPastaWeb + File.separator + "relatorio" + File.separator;
		String caminhoPdf = caminhoBasePdf + nomeNovoArquivo;
		UnificadorPDF.realizarUnificacaoPdf(caminhoBasePdf + caminhoFrente, caminhoBasePdf + caminhoVerso, caminhoPdf);
		File frente = new File(caminhoBasePdf + caminhoFrente);
		frente.delete();
		frente = null;
		File verso = new File(caminhoBasePdf + caminhoVerso);
		verso.delete();
		verso = null;
		superControleRelatorio.setCaminhoRelatorio(nomeNovoArquivo);
		return new File(caminhoPdf);
	}
	
	public void imprimirHistoricoPosPaisagem(ExpedicaoDiplomaVO expedicaoDiplomaVO, SuperParametroRelVO superParametroRelVO, SuperControleRelatorio superControleRelatorio, String tipoLayout, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		if (!tipoLayout.equals("DiplomaAlunoPos3Rel") && !tipoLayout.equals("DiplomaAlunoSuperior3Rel")) {
			HistoricoAlunoRelVO histAlunoRelVO = new HistoricoAlunoRelVO();
			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getFuncionarioPrimarioVO())) {
				histAlunoRelVO.setFuncionarioPrincipalVO(expedicaoDiplomaVO.getFuncionarioPrimarioVO());
				histAlunoRelVO.setCargoFuncionarioPrincipal(expedicaoDiplomaVO.getCargoFuncionarioPrincipalVO());
			}
			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getFuncionarioSecundarioVO())) {
				histAlunoRelVO.setFuncionarioSecundarioVO(expedicaoDiplomaVO.getFuncionarioSecundarioVO());
				histAlunoRelVO.setCargoFuncionarioSecundario(expedicaoDiplomaVO.getCargoFuncionarioSecundarioVO());
			}
			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getFuncionarioTerceiroVO())) {
				histAlunoRelVO.setFuncionarioTerciarioVO(expedicaoDiplomaVO.getFuncionarioTerceiroVO());
				histAlunoRelVO.setCargoFuncionarioTerciario(expedicaoDiplomaVO.getCargoFuncionarioTerceiroVO());
			}
			if (expedicaoDiplomaVO.getMatricula().getTituloMonografia() != null) {
				histAlunoRelVO.setTituloMonografia(expedicaoDiplomaVO.getMatricula().getTituloMonografia().replaceAll("(\n|\r)+", " "));
			} else {
				histAlunoRelVO.setTituloMonografia(expedicaoDiplomaVO.getMatricula().getTituloMonografia());
			}
			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getNotaMonografia())) {
				histAlunoRelVO.setNotaMonografia(expedicaoDiplomaVO.getMatricula().getNotaMonografia().toString());
			}
			getFacadeFactory().getMatriculaFacade().alterarObservacaoComplementar(expedicaoDiplomaVO.getMatricula(), expedicaoDiplomaVO.getGradeCurricularVO().getCodigo(), "", usuario);
			getFacadeFactory().getMatriculaFacade().carregarDados(expedicaoDiplomaVO.getMatricula(), NivelMontarDados.TODOS, usuario);
			getFacadeFactory().getHistoricoAlunoRelFacade().setDescricaoFiltros("");
			String design = HistoricoAlunoRel.getDesignIReportRelatorio(TipoNivelEducacional.getEnum(expedicaoDiplomaVO.getMatricula().getCurso().getNivelEducacional()), "HistoricoAlunoPosPaisagemRel", false);
			FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO = new FiltroRelatorioAcademicoVO();
			filtroRelatorioAcademicoVO.realizarMarcarTodasSituacoesHistorico();
			histAlunoRelVO = getFacadeFactory().getHistoricoAlunoRelFacade().criarObjeto(histAlunoRelVO, expedicaoDiplomaVO.getMatricula(), expedicaoDiplomaVO.getGradeCurricularVO(), filtroRelatorioAcademicoVO, 1, Constantes.EMPTY, Constantes.EMPTY, Constantes.EMPTY, "HistoricoAlunoPosPaisagemRel", false, expedicaoDiplomaVO.getDataExpedicao(), false, false, false, usuario, false, false, false, "", false, false, false, "PROFESSOR_APROVEITAMENTO_DISCIPLINA", null, null);
			histAlunoRelVO.setApresentarFrequencia(false);
			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getObservacaoComplementarDiploma1())) {
				histAlunoRelVO.setObservacaoComplementarDiploma1(expedicaoDiplomaVO.getObservacaoComplementarDiploma1());
			}
			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getObservacaoComplementarDiploma2())) {
				histAlunoRelVO.setObservacaoComplementarDiploma2(expedicaoDiplomaVO.getObservacaoComplementarDiploma2());
			}
			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getObservacaoComplementarDiploma3())) {
				histAlunoRelVO.setObservacaoComplementarDiploma3(expedicaoDiplomaVO.getObservacaoComplementarDiploma3());
			}
			for (HistoricoAlunoDisciplinaRelVO histAlunoDiscRelVO : histAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs()) {
				histAlunoDiscRelVO.setApresentarFrequencia(false);
			}
			List<HistoricoAlunoRelVO> historicoAlunoRelVOs = new ArrayList<HistoricoAlunoRelVO>(0);
			historicoAlunoRelVOs.add(histAlunoRelVO);

			superParametroRelVO.setNomeDesignIreport(design);
			superParametroRelVO.setSubReport_Dir(HistoricoAlunoRel.getCaminhoBaseRelatorio());
			superParametroRelVO.setNomeUsuario(usuario.getNome());
			superParametroRelVO.setListaObjetos(historicoAlunoRelVOs);
			superParametroRelVO.setCaminhoBaseRelatorio(HistoricoAlunoRel.getCaminhoBaseRelatorio());
			superParametroRelVO.setVersaoSoftware(superControleRelatorio.getVersaoSistema());
			UnidadeEnsinoVO unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(expedicaoDiplomaVO.getMatricula().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			if (unidadeEnsino.getNomeExpedicaoDiploma().trim().isEmpty()) {
				superParametroRelVO.setUnidadeEnsino(unidadeEnsino.getNome());
			} else {
				superParametroRelVO.setUnidadeEnsino(unidadeEnsino.getNomeExpedicaoDiploma());
			}
			adicionarParametrosAssinaturaDigital(expedicaoDiplomaVO, superParametroRelVO, superControleRelatorio, configuracaoGeralSistema, usuario);
			superParametroRelVO.setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			superControleRelatorio.realizarImpressaoRelatorio();
		} else if (tipoLayout.equals("DiplomaAlunoSuperior3Rel")) {
			imprimirPDFVersoLayout3Superior(expedicaoDiplomaVO, tipoLayout, superParametroRelVO, superControleRelatorio, tipoLayout, usuario, configuracaoGeralSistema);
		} else {
			imprimirPDFVersoLayout3Pos(expedicaoDiplomaVO, superParametroRelVO, tipoLayout, superControleRelatorio, usuario, configuracaoGeralSistema);
		}
	}
	
	public void imprimirPDFVersoLayout3Superior(ExpedicaoDiplomaVO expedicaoDiplomaVO, String tipoLayout, SuperParametroRelVO superParametroRelVO, SuperControleRelatorio superControleRelatorio, String caminhoPastaWeb, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		superParametroRelVO.setTituloRelatorio("Diploma do Aluno");
		superParametroRelVO.setNomeEmpresa(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getNome());
		List<DiplomaAlunoRelVO> listaObjetos = new ArrayList<DiplomaAlunoRelVO>(0);
		validarImprimirDiploma(expedicaoDiplomaVO, expedicaoDiplomaVO.getFuncionarioPrimarioVO().getCodigo(), expedicaoDiplomaVO.getFuncionarioSecundarioVO().getCodigo(), expedicaoDiplomaVO.getCargoFuncionarioPrincipalVO().getCodigo(), expedicaoDiplomaVO.getCargoFuncionarioSecundarioVO().getCodigo(), expedicaoDiplomaVO.getFuncionarioTerceiroVO().getCodigo(), expedicaoDiplomaVO.getCargoFuncionarioTerceiroVO().getCodigo(), tipoLayout, usuario, expedicaoDiplomaVO.getGerarXMLDiploma());
		listaObjetos = getFacadeFactory().getDiplomaAlunoRelFacade().criarObjeto(expedicaoDiplomaVO.getUtilizarUnidadeMatriz(), expedicaoDiplomaVO, expedicaoDiplomaVO.getFuncionarioPrimarioVO(), expedicaoDiplomaVO.getFuncionarioSecundarioVO(), expedicaoDiplomaVO.getFuncionarioTerceiroVO(), expedicaoDiplomaVO.getCargoFuncionarioPrincipalVO(), expedicaoDiplomaVO.getCargoFuncionarioSecundarioVO(), expedicaoDiplomaVO.getCargoFuncionarioTerceiroVO(), expedicaoDiplomaVO.getTituloFuncionarioPrincipal(), expedicaoDiplomaVO.getTituloFuncionarioSecundario(), usuario, tipoLayout, expedicaoDiplomaVO.getCargoFuncionarioQuartoVO(), expedicaoDiplomaVO.getCargoFuncionarioQuartoVO(), expedicaoDiplomaVO.getFuncionarioQuintoVO(), expedicaoDiplomaVO.getFuncionarioQuintoVO(), Boolean.TRUE);
		getFacadeFactory().getExpedicaoDiplomaFacade().alterarFuncionarioResponsavel(expedicaoDiplomaVO, usuario);
		String design = getFacadeFactory().getDiplomaAlunoRelFacade().getDesignIReportRelatorio(tipoLayout + "Verso", expedicaoDiplomaVO.getMatricula().getCurso().getNivelEducacional());
//		if (listaObjetos.get(0).getObservacaoComplementarDiploma0().getCodigo().intValue() > 0) {
//			design = getFacadeFactory().getDiplomaAlunoRelFacade().getDesignIReportRelatorio(tipoLayout + "Verso2", expedicaoDiplomaVO.getMatricula().getCurso().getNivelEducacional());
//		}
		superParametroRelVO.setNomeDesignIreport(design);
		superParametroRelVO.setSubReport_Dir(getFacadeFactory().getDiplomaAlunoRelFacade().getCaminhoBaseRelatorio());
		superParametroRelVO.setNomeUsuario(usuario.getNome());
		superParametroRelVO.setListaObjetos(listaObjetos);
		superParametroRelVO.setCaminhoBaseRelatorio(getFacadeFactory().getDiplomaAlunoRelFacade().getCaminhoBaseRelatorio());
		superParametroRelVO.setVersaoSoftware(superControleRelatorio.getVersaoSistema());
		superParametroRelVO.getParametros().put("simboloBandeiraPretoBranco", caminhoPastaWeb + File.separator + "resources" + File.separator + "imagens" + File.separator + "simboloBandeiraPretoBranco.jpg");
		superParametroRelVO.getParametros().put("bordaDiploma", caminhoPastaWeb + File.separator + "resources" + File.separator + "imagens" + File.separator + "bordaDiplom.png");
		Integer codigoUnidadeEnsino = 0;
		codigoUnidadeEnsino = expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigo();
		UnidadeEnsinoVO unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(codigoUnidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		if (unidadeEnsino.getNomeExpedicaoDiploma().trim().isEmpty()) {
			superParametroRelVO.setUnidadeEnsino(unidadeEnsino.getNome());
		} else {
			superParametroRelVO.setUnidadeEnsino(unidadeEnsino.getNomeExpedicaoDiploma());
		}
		superParametroRelVO.setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
		adicionarParametrosAssinaturaDigital(expedicaoDiplomaVO, superParametroRelVO, superControleRelatorio, configuracaoGeralSistemaVO, usuario);;
		superControleRelatorio.realizarImpressaoRelatorio();
		expedicaoDiplomaVO.setAbrirModalVerso(Constantes.EMPTY);
	}
	
	public void imprimirPDFVersoLayout3Pos(ExpedicaoDiplomaVO expedicaoDiplomaVO, SuperParametroRelVO superParametroRelVO, String tipoLayout, SuperControleRelatorio superControleRelatorio, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		superParametroRelVO.setTituloRelatorio("Verso Diploma do Aluno");
		superParametroRelVO.setNomeEmpresa(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getNome());
		List<DiplomaAlunoRelVO> listaObjetos = new ArrayList<DiplomaAlunoRelVO>(0);
		listaObjetos = getFacadeFactory().getDiplomaAlunoRelFacade().criarObjeto(expedicaoDiplomaVO.getUtilizarUnidadeMatriz(), expedicaoDiplomaVO, expedicaoDiplomaVO.getFuncionarioPrimarioVO(), expedicaoDiplomaVO.getFuncionarioSecundarioVO(), expedicaoDiplomaVO.getFuncionarioTerceiroVO(), expedicaoDiplomaVO.getCargoFuncionarioPrincipalVO(), expedicaoDiplomaVO.getCargoFuncionarioSecundarioVO(), expedicaoDiplomaVO.getCargoFuncionarioTerceiroVO(), expedicaoDiplomaVO.getTituloFuncionarioPrincipal(), expedicaoDiplomaVO.getTituloFuncionarioSecundario(), usuario, tipoLayout, expedicaoDiplomaVO.getCargoFuncionarioQuartoVO(), expedicaoDiplomaVO.getCargoFuncionarioQuintoVO(), expedicaoDiplomaVO.getFuncionarioQuartoVO(), expedicaoDiplomaVO.getFuncionarioQuintoVO(), Boolean.TRUE);
		String design = getFacadeFactory().getDiplomaAlunoRelFacade().getDesignIReportRelatorio("Verso" + tipoLayout, expedicaoDiplomaVO.getMatricula().getCurso().getNivelEducacional());
		superParametroRelVO.setNomeDesignIreport(design);
		superParametroRelVO.setNomeUsuario(usuario.getNome());
		superParametroRelVO.setListaObjetos(listaObjetos);
		superParametroRelVO.setCaminhoBaseRelatorio(getFacadeFactory().getDiplomaAlunoRelFacade().getCaminhoBaseRelatorio());
		superParametroRelVO.setVersaoSoftware(superControleRelatorio.getVersaoSistema());
		Integer codigoUnidadeEnsino = 0;
		codigoUnidadeEnsino = expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigo();
		UnidadeEnsinoVO unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(codigoUnidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		if (unidadeEnsino.getNomeExpedicaoDiploma().trim().isEmpty()) {
			superParametroRelVO.setUnidadeEnsino(unidadeEnsino.getNome());
		} else {
			superParametroRelVO.setUnidadeEnsino(unidadeEnsino.getNomeExpedicaoDiploma());
		}
		superParametroRelVO.setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
		adicionarParametrosAssinaturaDigital(expedicaoDiplomaVO, superParametroRelVO, superControleRelatorio, configuracaoGeralSistemaVO, usuario);
		superControleRelatorio.realizarImpressaoRelatorio();
		expedicaoDiplomaVO.setAbrirModalVerso(Constantes.EMPTY);
	}
	
	private void montarDadosCoordenadorCursoDiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital()) && (!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getCursoCoordenadorVO()))) {
			List<CursoCoordenadorVO> coordenadorVOs = getFacadeFactory().getCursoCoordenadorFacade().consultarPorPessoaUnidadeEnsinoNivelEducacionalCurso(0, expedicaoDiplomaVO.getMatricula().getUnidadeEnsino().getCodigo(), "", expedicaoDiplomaVO.getMatricula().getCurso().getCodigo(), 0, true, true, usuario);
			if (Uteis.isAtributoPreenchido(coordenadorVOs) && coordenadorVOs.stream().anyMatch(coordenador -> Uteis.isAtributoPreenchido(coordenador.getUnidadeEnsino()))) {
				expedicaoDiplomaVO.setCursoCoordenadorVO(coordenadorVOs.stream().filter(coordenador -> Uteis.isAtributoPreenchido(coordenador.getUnidadeEnsino())).findFirst().get());
				List<FormacaoAcademicaVO> formacaoAcademica = getFacadeFactory().getFormacaoAcademicaFacade().consultarFormacaoAcademicaoMaisAtual(expedicaoDiplomaVO.getCursoCoordenadorVO().getFuncionario().getPessoa().getCodigo(), usuario, 1);
				if (!formacaoAcademica.isEmpty()) {
					expedicaoDiplomaVO.setFormacaoAcademicaVO(formacaoAcademica.get(0));
				}
			} else if (Uteis.isAtributoPreenchido(coordenadorVOs)) {
				expedicaoDiplomaVO.setCursoCoordenadorVO(coordenadorVOs.get(0));
				List<FormacaoAcademicaVO> formacaoAcademica = getFacadeFactory().getFormacaoAcademicaFacade().consultarFormacaoAcademicaoMaisAtual(expedicaoDiplomaVO.getCursoCoordenadorVO().getFuncionario().getPessoa().getCodigo(), usuario, 1);
				if (!formacaoAcademica.isEmpty()) {
					expedicaoDiplomaVO.setFormacaoAcademicaVO(formacaoAcademica.get(0));
				}
			}
		}
	}
	
	private void montarConfiguracaoDiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula())) {
			expedicaoDiplomaVO.setConfiguracaoDiplomaDigital(getFacadeFactory().getConfiguracaoDiplomaDigitalInterfaceFacade().consultarConfiguracaoExistente(expedicaoDiplomaVO.getMatricula().getUnidadeEnsino().getCodigo(), false, usuario));
		}
	}
	
	private void limparDadosGerarXmlDiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO, Boolean gerarDiplomaXmlLote) throws Exception {
		if (!expedicaoDiplomaVO.getGerarXMLDiploma() && !gerarDiplomaXmlLote) {
			expedicaoDiplomaVO.setFuncionarioQuartoVO(new FuncionarioVO());
			expedicaoDiplomaVO.setCargoFuncionarioQuartoVO(new CargoVO());
			expedicaoDiplomaVO.setTituloFuncionarioQuarto(Constantes.EMPTY);
			expedicaoDiplomaVO.setFuncionarioQuintoVO(new FuncionarioVO());
			expedicaoDiplomaVO.setCargoFuncionarioQuintoVO(new CargoVO());
			expedicaoDiplomaVO.setTituloFuncionarioQuinto(Constantes.EMPTY);
			expedicaoDiplomaVO.setInformarCamposLivroRegistradora(false);
		}
		expedicaoDiplomaVO.setSituacaoApresentar(null);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void persistirLayoutPadrao(ExpedicaoDiplomaVO expedicaoDiploma, String valor, UsuarioVO usuarioVO) throws Exception {
		if (expedicaoDiploma.getMatricula().getCurso().getNivelEducacionalPosGraduacao()) {
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(valor, "diploma", "designDiplomaPos", usuarioVO);
		} else {
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(valor, "diploma", "designDiplomaGraduacao", expedicaoDiploma.getFuncionarioPrimarioVO().getCodigo(), expedicaoDiploma.getFuncionarioSecundarioVO().getCodigo(), expedicaoDiploma.getFuncionarioTerceiroVO().getCodigo(), expedicaoDiploma.getFuncionarioQuartoVO().getCodigo(), expedicaoDiploma.getFuncionarioQuintoVO().getCodigo(), false, expedicaoDiploma.getTituloFuncionarioPrincipal(), expedicaoDiploma.getTituloFuncionarioSecundario(), expedicaoDiploma.getTituloFuncionarioTerceiro(), expedicaoDiploma.getTituloFuncionarioQuarto(), expedicaoDiploma.getTituloFuncionarioQuinto(), "", "", usuarioVO, "", "", "", "", "");
			if (Uteis.isAtributoPreenchido(expedicaoDiploma.getTextoPadrao())) {
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(expedicaoDiploma.getTextoPadrao().getCodigo().toString(), "diploma", "textoPadrao", usuarioVO);
			}
		}
	}
	
	private void adicionarParametrosAssinaturaDigital(ExpedicaoDiplomaVO expedicaoDiplomaVO, SuperParametroRelVO superParametroRelVO, SuperControleRelatorio superControleRelatorio, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoGEDVO configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(expedicaoDiplomaVO.getMatricula().getUnidadeEnsino().getCodigo(), usuarioVO);
		if (superControleRelatorio.isAssinarDigitalmente()) {
			if (Uteis.isAtributoPreenchido(configGEDVO.getCodigo()) && configGEDVO.getConfiguracaoGedDiplomaVO().getApresentarAssinaturaDigitalizadoFuncionario() && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getFuncionarioPrimarioVO().getArquivoAssinaturaVO().getCodigo())) {
				superParametroRelVO.adicionarParametro("assinaturaDigitalFuncionarioPrimario", configuracaoGeralSistema.getLocalUploadArquivoFixo() + File.separator + expedicaoDiplomaVO.getFuncionarioPrimarioVO().getArquivoAssinaturaVO().getPastaBaseArquivo() + File.separator + expedicaoDiplomaVO.getFuncionarioPrimarioVO().getArquivoAssinaturaVO().getNome());
				superParametroRelVO.adicionarParametro("apresentarAssinaturaDigitalFuncionarioPrimario", configGEDVO.getConfiguracaoGedDiplomaVO().getApresentarAssinaturaDigitalizadoFuncionario());
			} else {
				superParametroRelVO.adicionarParametro("apresentarAssinaturaDigitalFuncionarioPrimario", false);
			}
			if (Uteis.isAtributoPreenchido(configGEDVO.getCodigo()) && configGEDVO.getConfiguracaoGedDiplomaVO().getApresentarAssinaturaDigitalizadoFuncionario() && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getCodigo())) {
				superParametroRelVO.adicionarParametro("assinaturaDigitalFuncionarioSecundario", configuracaoGeralSistema.getLocalUploadArquivoFixo() + File.separator + expedicaoDiplomaVO.getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getPastaBaseArquivo() + File.separator + expedicaoDiplomaVO.getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getNome());
				superParametroRelVO.adicionarParametro("apresentarAssinaturaDigitalFuncionarioSecundario", configGEDVO.getConfiguracaoGedDiplomaVO().getApresentarAssinaturaDigitalizadoFuncionario());
			} else {
				superParametroRelVO.adicionarParametro("apresentarAssinaturaDigitalFuncionarioSecundario", false);
			}
			if (Uteis.isAtributoPreenchido(configGEDVO.getCodigo()) && configGEDVO.getConfiguracaoGedDiplomaVO().getApresentarAssinaturaDigitalizadoFuncionario() && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getFuncionarioTerceiroVO().getArquivoAssinaturaVO().getCodigo())) {
				superParametroRelVO.adicionarParametro("assinaturaDigitalFuncionarioTerciario", configuracaoGeralSistema.getLocalUploadArquivoFixo() + File.separator + expedicaoDiplomaVO.getFuncionarioTerceiroVO().getArquivoAssinaturaVO().getPastaBaseArquivo() + File.separator + expedicaoDiplomaVO.getFuncionarioTerceiroVO().getArquivoAssinaturaVO().getNome());
				superParametroRelVO.adicionarParametro("apresentarAssinaturaDigitalFuncionarioTerciario", configGEDVO.getConfiguracaoGedDiplomaVO().getApresentarAssinaturaDigitalizadoFuncionario());
			} else {
				superParametroRelVO.adicionarParametro("apresentarAssinaturaDigitalFuncionarioTerciario", false);
			}
			superParametroRelVO.adicionarParametro("apresentarSeloAssinaturaDigital", false);
		} else {
			superParametroRelVO.adicionarParametro("apresentarSeloAssinaturaDigital", false);
			superParametroRelVO.adicionarParametro("assinaturaDigitalFuncionarioPrimario", Constantes.EMPTY);
			superParametroRelVO.adicionarParametro("assinaturaDigitalFuncionarioSecundario", Constantes.EMPTY);
			superParametroRelVO.adicionarParametro("assinaturaDigitalFuncionarioTerciario", Constantes.EMPTY);
			superParametroRelVO.adicionarParametro("apresentarAssinaturaDigitalFuncionarioPrimario", false);
			superParametroRelVO.adicionarParametro("apresentarAssinaturaDigitalFuncionarioSecundario", false);
			superParametroRelVO.adicionarParametro("apresentarAssinaturaDigitalFuncionarioTerciario", false);
		}
	}
	
	@Override
	public void carregarDadosExpedicaoDiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO, Boolean expedicaoDiplomaLote, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT ExpedicaoDiploma.*, pessoa.nome as nomepessoa  FROM ExpedicaoDiploma INNER JOIN matricula ON matricula.matricula = expedicaodiploma.matricula  inner join pessoa on pessoa.codigo = matricula.aluno WHERE ExpedicaoDiploma.codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { expedicaoDiplomaVO.getCodigo() });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ExpedicaoDiploma ).");
		}
		montarDadosExpedicaoDiploma(expedicaoDiplomaVO, expedicaoDiplomaLote, tabelaResultado, usuario);
	}
	
	public static void montarDadosExpedicaoDiploma(ExpedicaoDiplomaVO obj, Boolean expedicaoDiplomaLote, SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setDataExpedicao(dadosSQL.getDate("dataExpedicao"));
		obj.setDataPublicacaoDiarioOficial(dadosSQL.getDate("dataPublicacaoDiarioOficial"));
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.getGradeCurricularVO().setCodigo(dadosSQL.getInt("gradecurricular"));
		obj.setVia(dadosSQL.getString("via"));
		obj.setNumeroProcessoViaAnterior(dadosSQL.getString("numeroprocessoviaanterior"));
		if (!expedicaoDiplomaLote) {
	 		obj.setNumeroProcesso(dadosSQL.getString("numeroprocesso"));
	 		obj.setNumeroRegistroDiploma(dadosSQL.getString("numeroRegistroDiploma"));
	 		obj.setFolhaReciboRegistradora(dadosSQL.getString("nrFolhaReciboRegistradora"));
	        obj.setLivroRegistradora(dadosSQL.getString("nrLivroRegistradora"));
	 	}
        obj.setCodigoValidacaoDiplomaDigital(dadosSQL.getString("codigoValidacaoDiplomaDigital"));
		obj.setNumeroRegistroDiplomaViaAnterior(dadosSQL.getString("numeroregistrodiplomaviaanterior"));
		obj.getUnidadeEnsinoCertificadora().setCodigo(dadosSQL.getInt("unidadeensinocertificadora"));
		obj.setUtilizarUnidadeMatriz(dadosSQL.getBoolean("utilizarUnidadeMatriz"));
		obj.getFuncionarioPrimarioVO().setCodigo(new Integer(dadosSQL.getInt("funcionarioPrimario")));
		obj.getFuncionarioSecundarioVO().setCodigo(new Integer(dadosSQL.getInt("funcionarioSecundario")));
		obj.getFuncionarioTerceiroVO().setCodigo(new Integer(dadosSQL.getInt("funcionarioTerceiro")));
		obj.setLayoutDiploma(dadosSQL.getString("layoutDiploma"));
        obj.getCargoReitorRegistroDiplomaViaAnterior().setCodigo(new Integer(dadosSQL.getInt("cargoReitorRegistroDiplomaViaAnterior")));		
		obj.getCargoFuncionarioPrincipalVO().setCodigo(dadosSQL.getInt("cargoFuncionarioPrincipal"));
		obj.getCargoFuncionarioSecundarioVO().setCodigo(dadosSQL.getInt("cargoFuncionarioSecundario"));
		obj.getCargoFuncionarioTerceiroVO().setCodigo(dadosSQL.getInt("cargoFuncionarioTerceiro"));
		obj.setTituloFuncionarioPrincipal(dadosSQL.getString("tituloFuncionarioPrincipal"));
		obj.setTituloFuncionarioSecundario(dadosSQL.getString("tituloFuncionarioSecundario"));
		obj.setTituloFuncionarioTerceiro(dadosSQL.getString("tituloFuncionarioTerceiro"));
        obj.setObservacaoComplementarDiplomaVOs(getFacadeFactory().getObservacaoComplementarDiplomaFacade().consultarPorExpedicaoDiploma(obj.getCodigo(), false, usuario));
		obj.setTitulacaoFemininoApresentarDiploma(dadosSQL.getString("titulacaoFemininoApresentarDiploma"));
		obj.setTitulacaoMasculinoApresentarDiploma(dadosSQL.getString("titulacaoMasculinoApresentarDiploma"));		
        obj.getReitorRegistroDiplomaViaAnterior().setCodigo(new Integer(dadosSQL.getInt("reitorRegistroDiplomaViaAnterior")));
        obj.getSecretariaRegistroDiplomaViaAnterior().setCodigo(new Integer(dadosSQL.getInt("secretariaRegistroDiplomaViaAnterior")));
        obj.setDataRegistroDiplomaViaAnterior(dadosSQL.getDate("dataRegistroDiplomaViaAnterior"));
        obj.setSerial(dadosSQL.getString("serial"));		
        obj.setObservacao(dadosSQL.getString("observacao"));		
		obj.setObservacaoComplementarDiplomaVOs(getFacadeFactory().getObservacaoComplementarDiplomaFacade().consultarPorExpedicaoDiploma(obj.getCodigo(), false, usuario));
        obj.getFuncionarioQuartoVO().setCodigo(new Integer(dadosSQL.getInt("funcionarioQuarto")));
        obj.getCargoFuncionarioQuartoVO().setCodigo(dadosSQL.getInt("cargoFuncionarioQuarto"));
        obj.setTituloFuncionarioQuarto(dadosSQL.getString("tituloFuncionarioQuarto"));
        obj.getFuncionarioQuintoVO().setCodigo(new Integer(dadosSQL.getInt("funcionarioQuinto")));
        obj.getCargoFuncionarioQuintoVO().setCodigo(dadosSQL.getInt("cargoFuncionarioQuinto"));
        obj.setTituloFuncionarioQuinto(dadosSQL.getString("tituloFuncionarioQuinto"));
        obj.setInformarCamposLivroRegistradora(dadosSQL.getBoolean("informarCamposLivroRegistradora"));
        obj.setGerarXMLDiploma(dadosSQL.getBoolean("gerarxmldiploma"));
        obj.setAnulado(dadosSQL.getBoolean("anulado"));
        obj.getTextoPadrao().setCodigo(dadosSQL.getInt("textopadrao"));
        obj.setDataRegistroDiploma(dadosSQL.getDate("dataRegistroDiploma"));
        obj.setEmitidoPorProcessoTransferenciaAssistida(dadosSQL.getBoolean("emitidoPorProcessoTransferenciaAssistida"));
        if (obj.getEmitidoPorProcessoTransferenciaAssistida()) {
        	obj.setNomeIesPTA(dadosSQL.getString("nomeIesPTA"));
        	obj.setCnpjPTA(dadosSQL.getString("cnpjPTA"));
        	obj.setCodigoMecPTA(dadosSQL.getInt("codigoMecPTA"));
        	obj.setCepPTA(dadosSQL.getString("cepPTA"));
        	obj.getCidadePTA().setCodigo(dadosSQL.getInt("cidadePTA"));
        	obj.setLogradouroPTA(dadosSQL.getString("logradouroPTA"));
        	obj.setNumeroPTA(dadosSQL.getString("numeroPTA"));
        	obj.setComplementoPTA(dadosSQL.getString("complementoPTA"));
        	obj.setBairroPTA(dadosSQL.getString("bairroPTA"));
        	obj.setTipoDescredenciamentoPTA(TipoAutorizacaoEnum.valueOf(dadosSQL.getString("tipoDescredenciamentoPTA")));
        	obj.setNumeroDescredenciamentoPTA(dadosSQL.getString("numeroDescredenciamentoPTA"));
        	obj.setDataDescredenciamentoPTA(dadosSQL.getDate("dataDescredenciamentoPTA"));
        	obj.setDataPublicacaoDescredenciamentoPTA(dadosSQL.getDate("dataPublicacaoDescredenciamentoPTA"));
        	obj.setVeiculoPublicacaoDescredenciamentoPTA(dadosSQL.getString("veiculoPublicacaoDescredenciamentoPTA"));
        	obj.setSecaoPublicacaoDescredenciamentoPTA(dadosSQL.getInt("secaoPublicacaoDescredenciamentoPTA"));
        	obj.setPaginaPublicacaoDescredenciamentoPTA(dadosSQL.getInt("paginaPublicacaoDescredenciamentoPTA"));
        	obj.setNumeroDOUDescredenciamentoPTA(dadosSQL.getInt("numeroDOUDescredenciamentoPTA"));
        }
        obj.setEmitidoPorDecisaoJudicial(dadosSQL.getBoolean("emitidoPorDecisaoJudicial"));
        if (obj.getEmitidoPorDecisaoJudicial()) {
        	obj.setNomeJuizDecisaoJudicial(dadosSQL.getString("nomeJuizDecisaoJudicial"));
        	obj.setNumeroProcessoDecisaoJudicial(dadosSQL.getString("numeroProcessoDecisaoJudicial"));
        	obj.setDecisaoJudicial(dadosSQL.getString("decisaoJudicial"));
        	obj.setInformacoesAdicionaisDecisaoJudicial(dadosSQL.getString("informacoesAdicionaisDecisaoJudicial"));
        }
        if (dadosSQL.getString("versaoDiploma") != null) {
        	obj.setVersaoDiploma(VersaoDiplomaDigitalEnum.getEnum(dadosSQL.getString("versaoDiploma")));
        }
        obj.getMatricula().getAluno().setNome(dadosSQL.getString("nomePessoa"));
        if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoCertificadora())) {
        	obj.setUnidadeEnsinoCertificadora(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsinoCertificadora().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
        }
        if (Uteis.isAtributoPreenchido(obj.getCidadePTA())) {
        	obj.setCidadePTA(getFacadeFactory().getCidadeFacade().consultarPorChavePrimariaUnica(obj.getCidadePTA().getCodigo(), false, usuario));
        }
        if (obj.getEmitidoPorDecisaoJudicial()) {
        	obj.setDeclaracaoAcercaProcessoJudicialVOs(getFacadeFactory().getDeclaracaoAcercaProcessoJudicialInterfaceFacade().consultar(obj.getCodigo()));
        }
		obj.setNovoObj(Boolean.FALSE);
		montarDadosFuncionarioPrimario(obj, usuario);
		montarDadosFuncionarioSecundario(obj, usuario);
		montarDadosFuncionarioTerceiro(obj, usuario);
        montarDadosFuncionarioQuarto(obj, usuario);
        montarDadosFuncionarioQuinto(obj, usuario);
		if (!obj.getVia().equals("1") && !obj.getVia().equals("")) {
			montarDadosFuncionarioReitor(obj, usuario);	
			montarDadosFuncionarioSecGeral(obj, usuario);	
		}
		obj.getMatricula().getAluno().setNome(dadosSQL.getString("nomePessoa"));
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		montarDadosMatricula(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosGradeCurricular(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
	}
	
	@Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarAnulacaoDiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO) throws Exception {
		expedicaoDiplomaVO.setResponsavelAnulacao(usuarioVO);
		expedicaoDiplomaVO.setAnulado(Boolean.TRUE);
		expedicaoDiplomaVO.setSituacaoApresentar(null);
    	List<DocumentoAssinadoVO> documentoAssinadoVOs = getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentosDigitaisDiploma(expedicaoDiplomaVO, Arrays.asList(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL), Boolean.TRUE);
		if (Uteis.isAtributoPreenchido(documentoAssinadoVOs)) {
			DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO = new DocumentoAssinadoPessoaVO();
			documentoAssinadoPessoaVO.setDataRejeicao(new Date());
			documentoAssinadoPessoaVO.setMotivoRejeicao(expedicaoDiplomaVO.getMotivoAnulacao().value());
			documentoAssinadoPessoaVO.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.REJEITADO);
			getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosRejeicaoDocumentosAssinados(documentoAssinadoPessoaVO, documentoAssinadoVOs);
			for (DocumentoAssinadoVO documentoAssinadoVO : documentoAssinadoVOs) {
				getFacadeFactory().getDocumentoAssinadoFacade().atualizarDocumentoAssinadoInvalido(documentoAssinadoVO.getCodigo(), Boolean.TRUE, expedicaoDiplomaVO.getMotivoAnulacao().value(), usuarioVO);
			}
		}
		List<ExpedicaoDiplomaVO> expedicaoDiplomaVOs = consultarExpedicaoDiplomaGenerico(new ControleConsultaExpedicaoDiploma(), Arrays.asList("codigo AS expedicaoDiploma_codigo", "matricula AS expedicaoDiploma_matricula", "via AS expedicaoDiploma_via"), Arrays.asList("matricula = '" + expedicaoDiplomaVO.getMatricula().getMatricula() + "'"), new ArrayList<>(0), new ArrayList<>(0), new ArrayList<>(0), Boolean.FALSE);
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVOs)) {
			for (ExpedicaoDiplomaVO obj : expedicaoDiplomaVOs) {
				if (Uteis.isAtributoPreenchido(obj)) {
					alterar(obj, "expedicaoDiploma", new AtributoPersistencia().add("anulado", expedicaoDiplomaVO.getAnulado()).add("motivoAnulacao", expedicaoDiplomaVO.getMotivoAnulacao().name()).add("dataAnulacao", Uteis.getDataJDBC(expedicaoDiplomaVO.getDataAnulacao())).add("anotacaoAnulacao", expedicaoDiplomaVO.getAnotacaoAnulacao()).add("responsavelAnulacao", Uteis.isAtributoPreenchido(usuarioVO) ? usuarioVO.getCodigo() : null), new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
				}
			}
		}
    }
	
	@Override
	@SuppressWarnings("rawtypes")
	public ExpedicaoDiplomaVO consultarUnicaExpedicaoDiplomaGenerico(List<String> fields, List<String> condicaoWhere, List<String> joins, List<Object> valoresFiltros, List<String> orderBY, Boolean retornaException) throws Exception {
		StringBuilder sql = new StringBuilder();
		if (Uteis.isAtributoPreenchido(fields)) {
			sql.append("SELECT " + fields.stream().map(S -> S.toString()).collect(Collectors.joining(", ")) + " FROM expedicaodiploma ");
		} else {
			throw new Exception("Colunas da consulta SELECT deve ser preenchida para consulta.");
		}
		if (Uteis.isAtributoPreenchido(joins)) {
			sql.append(joins.stream().map(j -> j.toString()).collect(Collectors.joining(" ")));
		}
		if (Uteis.isAtributoPreenchido(condicaoWhere)) {
			sql.append(" WHERE ").append(condicaoWhere.stream().map(s -> s.toString()).collect(Collectors.joining(" AND ")));
		} else {
			throw new Exception("Condição WHERE deve ser preenchida para consulta.");
		}
		if (Uteis.isAtributoPreenchido(orderBY)) {
			sql.append(" ORDER BY ").append(orderBY.stream().map(s -> s.toString()).collect(Collectors.joining(", ")));
		}
		sql.append(" LIMIT 1");
		SqlRowSet tabelaResultado = null;
		if (Uteis.isAtributoPreenchido(valoresFiltros)) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valoresFiltros.toArray());
		} else {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		}
		if (!tabelaResultado.next()) {
			if (retornaException) {
				throw new Exception("Dados não encontrado (Expedição Diploma)");
			} else {
				return new ExpedicaoDiplomaVO();
			}
		}
		ExpedicaoDiplomaVO expedicaoDiplomaVO = new ExpedicaoDiplomaVO();
		ControleConsultaExpedicaoDiploma consultaExpedicaoDiploma = new ControleConsultaExpedicaoDiploma();
		Class class1 = ControleConsultaExpedicaoDiploma.class;
		for (String str : fields) {
			String strField = Constantes.EMPTY;
			String classe = str.substring(str.indexOf("AS") + 3, str.lastIndexOf("_") + 1);
			if (classe.equals("personalizado_")) {
				strField = str.substring(str.indexOf("personalizado_"));
				expedicaoDiplomaVO.getFieldsPersonalizados().put(strField, tabelaResultado.getObject(strField));
			} else {
				strField = montarDadosField(classe, str);
			}
			if (!Uteis.isAtributoPreenchido(strField)) {
				throw new Exception("Erro ao montar os dados do Field (" + classe + ").");
			}
			if (!classe.equals("personalizado_") && tabelaResultado.getObject(strField) != null) {
				Field field = class1.getDeclaredField(strField);
				field.set(consultaExpedicaoDiploma, tabelaResultado.getObject(strField));
			}
		}
		consultaExpedicaoDiploma.montarDadosExpedicaoDiploma(expedicaoDiplomaVO);
		return expedicaoDiplomaVO;
	}
	
	private List<ExpedicaoDiplomaVO> consultarExpedicaoDiplomaGenerico(ControleConsultaExpedicaoDiploma controleConsultaOtimizadoExpedicaoDiploma, List<String> fields, List<String> condicaoWhere, List<String> joins, List<Object> valoresFiltros, List<Object> orderBy, Boolean controleConsulta) throws Exception {
		StringBuilder sql = new StringBuilder();
		if (Uteis.isAtributoPreenchido(fields)) {
			sql.append("SELECT count(*) over() as qtde_total_registros, " + fields.stream().map(s -> s.toString()).collect(Collectors.joining(", ")) + " FROM expedicaodiploma ");
		} else {
			throw new Exception("Colunas da consulta SELECT deve ser preenchida para consulta.");
		}
		if (Uteis.isAtributoPreenchido(joins)) {
			sql.append(joins.stream().map(j -> j.toString()).collect(Collectors.joining(" ")));
		}
		if (Uteis.isAtributoPreenchido(condicaoWhere)) {
			sql.append(" WHERE ").append(condicaoWhere.stream().map(s -> s.toString()).collect(Collectors.joining(" AND ")));
		}
		if (Uteis.isAtributoPreenchido(orderBy)) {
			sql.append(" ORDER BY " + orderBy.stream().map(o -> o.toString()).collect(Collectors.joining(", ")));
		}
		if (controleConsulta && controleConsultaOtimizadoExpedicaoDiploma != null) {
			sql.append(" limit ").append(controleConsultaOtimizadoExpedicaoDiploma.getControleConsultaOtimizado().getLimitePorPagina()).append(" offset ").append(controleConsultaOtimizadoExpedicaoDiploma.getControleConsultaOtimizado().getOffset());
		}
		SqlRowSet tabelaResultado = null;
		if (Uteis.isAtributoPreenchido(valoresFiltros)) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valoresFiltros.toArray());
		} else {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		}
		List<ExpedicaoDiplomaVO> expedicaoDiplomaVOs = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			ExpedicaoDiplomaVO expedicaoDiplomaVO = new ExpedicaoDiplomaVO();
			ControleConsultaExpedicaoDiploma consultaExpedicaoDiploma = new ControleConsultaExpedicaoDiploma();
			Class class1 = ControleConsultaExpedicaoDiploma.class;
			if (controleConsulta && controleConsultaOtimizadoExpedicaoDiploma != null && Uteis.isAtributoPreenchido(tabelaResultado.getInt("qtde_total_registros"))) {
				controleConsultaOtimizadoExpedicaoDiploma.getControleConsultaOtimizado().setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
			}
			for (String str : fields) {
				String strField = Constantes.EMPTY;
				String classe = str.substring(str.lastIndexOf("AS") + 3, str.lastIndexOf("_") + 1);
				if (classe.equals("personalizado_")) {
					strField = str.substring(str.indexOf("personalizado_"));
					expedicaoDiplomaVO.getFieldsPersonalizados().put(strField, tabelaResultado.getObject(strField));
				} else {
					strField = montarDadosField(classe, str);
				}
				if (!Uteis.isAtributoPreenchido(strField)) {
					throw new Exception("Erro ao montar os dados do Field (" + classe + ").");
				}
				if (!classe.equals("personalizado_") && tabelaResultado.getObject(strField) != null) {
					Field field = class1.getDeclaredField(strField);
					field.set(consultaExpedicaoDiploma, tabelaResultado.getObject(strField));
				}
			}
			consultaExpedicaoDiploma.montarDadosExpedicaoDiploma(expedicaoDiplomaVO);
			expedicaoDiplomaVOs.add(expedicaoDiplomaVO);
		}
		return expedicaoDiplomaVOs;
	}
	
	@Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarEstornoAnulacaoDiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO) throws Exception {
    	expedicaoDiplomaVO.setAnulado(Boolean.FALSE);
    	expedicaoDiplomaVO.setMotivoAnulacao(null);
    	expedicaoDiplomaVO.setDataAnulacao(null);
    	expedicaoDiplomaVO.setAnotacaoAnulacao(null);
    	expedicaoDiplomaVO.setSituacaoApresentar(null);
    	expedicaoDiplomaVO.setResponsavelAnulacao(null);
    	List<DocumentoAssinadoVO> documentoAssinadoVOs = getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentosDigitaisDiploma(expedicaoDiplomaVO, Arrays.asList(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL), Boolean.FALSE);
		if (Uteis.isAtributoPreenchido(documentoAssinadoVOs)) {
			for (DocumentoAssinadoVO documentoAssinadoVO : documentoAssinadoVOs) {
				getFacadeFactory().getDocumentoAssinadoFacade().atualizarDocumentoAssinadoInvalido(documentoAssinadoVO.getCodigo(), Boolean.FALSE, Constantes.EMPTY, usuarioVO);
			}
		}
		List<ExpedicaoDiplomaVO> expedicaoDiplomaVOs = consultarExpedicaoDiplomaGenerico(new ControleConsultaExpedicaoDiploma(), Arrays.asList("codigo AS expedicaoDiploma_codigo", "matricula AS expedicaoDiploma_matricula", "via AS expedicaoDiploma_via"), Arrays.asList("expedicaoDiploma.matricula = '" + expedicaoDiplomaVO.getMatricula().getMatricula() + "'"), new ArrayList<>(0), new ArrayList<>(0), new ArrayList<>(0), Boolean.FALSE);
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVOs)) {
			for (ExpedicaoDiplomaVO obj : expedicaoDiplomaVOs) {
				if (Uteis.isAtributoPreenchido(obj)) {
					alterar(obj, "expedicaoDiploma", new AtributoPersistencia().add("anulado", expedicaoDiplomaVO.getAnulado()).add("motivoAnulacao", null).add("dataAnulacao", null).add("anotacaoAnulacao", null).add("responsavelAnulacao", null), new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
				}
			}
		}
    }
	
	@Override
	public void consultarExpedicaoDiplomaGenericoOtimizado(ControleConsultaExpedicaoDiploma controleConsultaExpedicaoDiploma) throws Exception {
		List<String> fields = new ArrayList<>();
		fields.add("expedicaoDiploma.codigo AS expedicaoDiploma_codigo");
		fields.add("expedicaoDiploma.dataExpedicao AS expedicaoDiploma_dataexpedicao");
		fields.add("expedicaoDiploma.via AS expedicaoDiploma_via");
		fields.add("matricula.matricula AS matricula_matricula");
		fields.add("matricula.aluno AS matricula_aluno");
		fields.add("pessoa.nome AS matricula_aluno_nome");
		fields.add("diplomaDigital.codigo AS personalizado_codigoDiplomaDigital");
		fields.add("historicoDigital.codigo AS personalizado_codigoHistoricoDigital");
		fields.add("documentacaoAcademicoDigital.codigo AS personalizado_codigoDocumentacaoAcademicoDigital");
		fields.add("curso.codigo AS matricula_curso_codigo");
		fields.add("curso.nome AS matricula_curso_nome");
		fields.add("unidadeEnsino.codigo AS matricula_unidadeensino_codigo");
		fields.add("unidadeEnsino.nome AS matricula_unidadeensino_nome");
		fields.add("(CASE WHEN expedicaoDiploma.anulado THEN 'Diploma Anulado' WHEN documentoassinado IS NULL THEN 'Diploma Emitido Sem XML' WHEN documentoAssinado.pendente THEN 'Diploma Pendente Assinatura' WHEN documentoAssinado.assinado THEN 'Diploma Assinado' ELSE 'Diploma Inválido' END) AS personalizado_situacaoDiploma");
		StringBuilder joinLateral = new StringBuilder();
		joinLateral.append("LEFT JOIN LATERAL (SELECT documentoassinado.codigo, documentoassinado.tipoorigemdocumentoassinado, ");
		joinLateral.append("(CASE WHEN (totalDocumentoPessoa.qtdDocumentoPessoa - totalDocumentoPessoaAssinado.qtdDocumentoPessoaAssinado) = 0 THEN TRUE ELSE FALSE END) AS assinado, ");
		joinLateral.append("(CASE WHEN (totalDocumentoPessoa.qtdDocumentoPessoa - totalDocumentoPessoaPendente.qtdDocumentoPessoaPendente) = 0 THEN TRUE WHEN (totalDocumentoPessoaAssinado.qtdDocumentoPessoaAssinado) = totalDocumentoPessoa.qtdDocumentoPessoa THEN FALSE WHEN (totalDocumentoPessoaPendente.qtdDocumentoPessoaPendente + totalDocumentoPessoaAssinado.qtdDocumentoPessoaAssinado) = totalDocumentoPessoa.qtdDocumentoPessoa THEN TRUE ELSE FALSE END) AS pendente ");
		joinLateral.append("FROM documentoassinado ");
		joinLateral.append("INNER JOIN matricula doc_matricula ON doc_matricula.matricula = documentoAssinado.matricula	INNER JOIN curso doc_curso ON doc_curso.codigo = doc_matricula.curso ");
		joinLateral.append("LEFT JOIN LATERAL (SELECT count(documentoassinadopessoa.codigo) AS qtdDocumentoPessoa FROM documentoassinadopessoa WHERE documentoassinadopessoa.documentoassinado = documentoassinado.codigo) AS totalDocumentoPessoa ON TRUE ");
		joinLateral.append("LEFT JOIN LATERAL (SELECT count(documentoassinadopessoa.codigo) AS qtdDocumentoPessoaPendente FROM documentoassinadopessoa WHERE documentoassinadopessoa.documentoassinado = documentoassinado.codigo AND documentoassinadopessoa.situacaodocumentoassinadopessoa IN ('PENDENTE')) AS totalDocumentoPessoaPendente ON TRUE ");
		joinLateral.append("LEFT JOIN LATERAL (SELECT count(documentoassinadopessoa.codigo) AS qtdDocumentoPessoaAssinado FROM documentoassinadopessoa WHERE documentoassinadopessoa.documentoassinado = documentoassinado.codigo AND documentoassinadopessoa.situacaodocumentoassinadopessoa IN ('ASSINADO')) AS totalDocumentoPessoaAssinado ON TRUE ");
		joinLateral.append("WHERE documentoassinado.matricula = expedicaoDiploma.matricula 	AND documentoassinado.expedicaodiploma = expedicaoDiploma.codigo ");
		joinLateral.append("AND ((doc_curso.niveleducacional IN ('GT', 'SU') AND (documentoassinado.tipoorigemdocumentoassinado IN ('DIPLOMA_DIGITAL') OR documentoassinado.tipoorigemdocumentoassinado IN ('EXPEDICAO_DIPLOMA'))) OR (doc_curso.niveleducacional NOT IN ('GT', 'SU') AND documentoassinado.tipoorigemdocumentoassinado IN ('EXPEDICAO_DIPLOMA'))) ");
		joinLateral.append("ORDER BY documentoassinado.codigo DESC LIMIT 1) AS documentoAssinado ON TRUE ");
		StringBuilder joinDiplomaDigital = new StringBuilder();
		joinDiplomaDigital.append("LEFT JOIN documentoassinado diplomaDigital ON diplomaDigital.codigo = (SELECT d.codigo FROM documentoassinado d ");
		joinDiplomaDigital.append("INNER JOIN matricula d_matricula ON d_matricula.matricula = d.matricula INNER JOIN curso d_curso ON	d_curso.codigo = d_matricula.curso ");
		joinDiplomaDigital.append("WHERE d.expedicaodiploma = expedicaodiploma.codigo AND d.matricula = expedicaodiploma.matricula AND ((d_curso.niveleducacional IN ('GT', 'SU') AND (d.tipoorigemdocumentoassinado IN ('DIPLOMA_DIGITAL') OR d.tipoorigemdocumentoassinado IN ('EXPEDICAO_DIPLOMA'))) OR (d_curso.niveleducacional NOT IN ('GT', 'SU') AND d.tipoorigemdocumentoassinado IN ('EXPEDICAO_DIPLOMA'))) ");
		joinDiplomaDigital.append("ORDER BY d.codigo DESC LIMIT 1) ");
		StringBuilder joinHistoricoDigital = new StringBuilder();
		joinHistoricoDigital.append("LEFT JOIN documentoassinado historicoDigital ON historicoDigital.codigo = (SELECT d.codigo FROM documentoassinado d ");
		joinHistoricoDigital.append("WHERE d.expedicaodiploma = expedicaodiploma.codigo AND d.matricula = expedicaodiploma.matricula AND d.tipoorigemdocumentoassinado IN ('HISTORICO_DIGITAL') ORDER BY d.codigo DESC LIMIT 1) ");
		StringBuilder joinDocumentacaoAcademicoDigital = new StringBuilder();
		joinDocumentacaoAcademicoDigital.append("LEFT JOIN documentoassinado documentacaoAcademicoDigital ON documentacaoAcademicoDigital.codigo = (SELECT d.codigo FROM documentoassinado d ");
		joinDocumentacaoAcademicoDigital.append("WHERE d.expedicaodiploma = expedicaodiploma.codigo AND d.matricula = expedicaodiploma.matricula AND d.tipoorigemdocumentoassinado IN ('DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL') ORDER BY d.codigo DESC LIMIT 1) ");
		List<String> joins = new ArrayList<>();
		joins.add("INNER JOIN matricula ON matricula.matricula = expedicaoDiploma.matricula");
		joins.add("INNER JOIN curso ON curso.codigo = matricula.curso");
		joins.add("INNER JOIN pessoa ON pessoa.codigo = matricula.aluno");
		joins.add("INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = matricula.unidadeEnsino");
		joins.add(joinLateral.toString());
		joins.add(joinDiplomaDigital.toString());
		joins.add(joinHistoricoDigital.toString());
		joins.add(joinDocumentacaoAcademicoDigital.toString());
		List<String> condicaoWhere = new ArrayList<>(0);
		List<Object> valoresFiltros = new ArrayList<>(0);
		if (Uteis.isAtributoPreenchido(controleConsultaExpedicaoDiploma.getMatriculaVO())) {
			condicaoWhere.add("expedicaoDiploma.matricula ilike(?)");
			valoresFiltros.add(controleConsultaExpedicaoDiploma.getMatriculaVO().getMatricula());
		} else {
			if (Uteis.isAtributoPreenchido(controleConsultaExpedicaoDiploma.getCursoVO())) {
				condicaoWhere.add("matricula.curso = ?");
				valoresFiltros.add(controleConsultaExpedicaoDiploma.getCursoVO().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(controleConsultaExpedicaoDiploma.getUnidadeEnsinoVO())) {
				condicaoWhere.add("matricula.unidadeEnsino = ?");
				valoresFiltros.add(controleConsultaExpedicaoDiploma.getUnidadeEnsinoVO().getCodigo());
			}
		}
		if (Uteis.isAtributoPreenchido(controleConsultaExpedicaoDiploma.getCodigoVerificacao())) {
			condicaoWhere.add("expedicaoDiploma.codigovalidacaodiplomadigital ilike(?)");
			valoresFiltros.add(controleConsultaExpedicaoDiploma.getCodigoVerificacao());
		}
		if (Uteis.isAtributoPreenchido(controleConsultaExpedicaoDiploma.getNumeroRegistro())) {
			condicaoWhere.add("expedicaoDiploma.numeroregistrodiploma ilike(?)");
			valoresFiltros.add(controleConsultaExpedicaoDiploma.getNumeroRegistro());
		}
		if (Uteis.isAtributoPreenchido(controleConsultaExpedicaoDiploma.getNumeroProcesso())) {
			condicaoWhere.add("expedicaoDiploma.numeroprocesso ilike(?)");
			valoresFiltros.add(controleConsultaExpedicaoDiploma.getNumeroProcesso());
		}
		if (Uteis.isAtributoPreenchido(controleConsultaExpedicaoDiploma.getVia())) {
			condicaoWhere.add("expedicaoDiploma.via = ?");
			valoresFiltros.add(controleConsultaExpedicaoDiploma.getVia());
		}
		if (Uteis.isAtributoPreenchido(controleConsultaExpedicaoDiploma.getDataPeriodoInicio())) {
			if (controleConsultaExpedicaoDiploma.getTipoData().equals("dataRegistro")) {
				if (Uteis.isAtributoPreenchido(controleConsultaExpedicaoDiploma.getDataPeriodoFim())) {
					if (controleConsultaExpedicaoDiploma.getDataPeriodoInicio().after(controleConsultaExpedicaoDiploma.getDataPeriodoFim())) {
						throw new Exception("O período final não pode ser menor do que o período inicial (DATA REGISTRO).");
					}
					condicaoWhere.add("(expedicaoDiploma.dataRegistroDiploma BETWEEN ? AND ?)");
					valoresFiltros.add(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(controleConsultaExpedicaoDiploma.getDataPeriodoInicio()));
					valoresFiltros.add(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(controleConsultaExpedicaoDiploma.getDataPeriodoFim()));
				} else {
					condicaoWhere.add("(expedicaoDiploma.dataRegistroDiploma >= ?)");
					valoresFiltros.add(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(controleConsultaExpedicaoDiploma.getDataPeriodoInicio()));
				}
			} else if (controleConsultaExpedicaoDiploma.getTipoData().equals("dataCadastro")) {
				if (Uteis.isAtributoPreenchido(controleConsultaExpedicaoDiploma.getDataPeriodoFim())) {
					if (controleConsultaExpedicaoDiploma.getDataPeriodoInicio().after(controleConsultaExpedicaoDiploma.getDataPeriodoFim())) {
						throw new Exception("O período final não pode ser menor do que o período inicial (DATA CADASTRO).");
					}
					condicaoWhere.add("(expedicaoDiploma.dataCadastro BETWEEN ? AND ?)");
					valoresFiltros.add(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(controleConsultaExpedicaoDiploma.getDataPeriodoInicio()));
					valoresFiltros.add(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(controleConsultaExpedicaoDiploma.getDataPeriodoFim()));
				} else {
					condicaoWhere.add("(expedicaoDiploma.dataCadastro >= ?)");
					valoresFiltros.add(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(controleConsultaExpedicaoDiploma.getDataPeriodoInicio()));
				}
			} else {
				if (Uteis.isAtributoPreenchido(controleConsultaExpedicaoDiploma.getDataPeriodoFim())) {
					if (controleConsultaExpedicaoDiploma.getDataPeriodoInicio().after(controleConsultaExpedicaoDiploma.getDataPeriodoFim())) {
						throw new Exception("O período final não pode ser menor do que o período inicial (DATA EXPEDIÇÃO).");
					}
					condicaoWhere.add("(expedicaoDiploma.dataExpedicao BETWEEN ? AND ?)");
					valoresFiltros.add(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(controleConsultaExpedicaoDiploma.getDataPeriodoInicio()));
					valoresFiltros.add(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(controleConsultaExpedicaoDiploma.getDataPeriodoFim()));
				} else {
					condicaoWhere.add("(expedicaoDiploma.dataExpedicao >= ?)");
					valoresFiltros.add(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(controleConsultaExpedicaoDiploma.getDataPeriodoInicio()));
				}
			}
		}
		int qtd = (controleConsultaExpedicaoDiploma.getPendenteAssinatura() ? 1 : 0) + (controleConsultaExpedicaoDiploma.getValido() ? 1 : 0) + (controleConsultaExpedicaoDiploma.getAnulado() ? 1 : 0);
		if (qtd > 1) {
			List<String> situacaoDiploma = new ArrayList<>(0);
			if (controleConsultaExpedicaoDiploma.getPendenteAssinatura()) {
				situacaoDiploma.add("documentoassinado.pendente = TRUE");
			}
			if (controleConsultaExpedicaoDiploma.getValido()) {
				situacaoDiploma.add("documentoassinado.assinado = TRUE");
			}
			if (controleConsultaExpedicaoDiploma.getAnulado()) {
				StringBuilder diplomaAnulado = new StringBuilder();
				diplomaAnulado.append("(expedicaoDiploma.anulado = TRUE ");
				if (Uteis.isAtributoPreenchido(controleConsultaExpedicaoDiploma.getDataPeriodoAnulacaoInicio())) {
					if (Uteis.isAtributoPreenchido(controleConsultaExpedicaoDiploma.getDataPeriodoAnulacaoFim())) {
						if (controleConsultaExpedicaoDiploma.getDataPeriodoAnulacaoInicio().after(controleConsultaExpedicaoDiploma.getDataPeriodoAnulacaoFim())) {
							throw new Exception("O período final não pode ser menor do que o período inicial (DATA ANULAÇÃO).");
						}
						diplomaAnulado.append("AND (expedicaoDiploma.dataanulacao BETWEEN '" + Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(controleConsultaExpedicaoDiploma.getDataPeriodoAnulacaoInicio()) + "' AND '" + Uteis.getDataComHoraSetadaParaUltimoMinutoDia(controleConsultaExpedicaoDiploma.getDataPeriodoAnulacaoFim()) + "' )");
					} else {
						diplomaAnulado.append("AND (expedicaoDiploma.dataanulacao >= '" + Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(controleConsultaExpedicaoDiploma.getDataPeriodoAnulacaoInicio()) + "' )");
					}
				}
				situacaoDiploma.add(diplomaAnulado.append(")").toString());
			}
			condicaoWhere.add("(" + situacaoDiploma.stream().map(s -> s.toString()).collect(Collectors.joining(" OR ")) + ")");
		} else {
			if (controleConsultaExpedicaoDiploma.getPendenteAssinatura()) {
				condicaoWhere.add("documentoassinado.pendente = ?");
				valoresFiltros.add(Boolean.TRUE);
			} else if (controleConsultaExpedicaoDiploma.getValido()) {
				condicaoWhere.add("documentoassinado.assinado = ?");
				valoresFiltros.add(Boolean.TRUE);
			} else if (controleConsultaExpedicaoDiploma.getAnulado()) {
				condicaoWhere.add("expedicaoDiploma.anulado = ?");
				valoresFiltros.add(Boolean.TRUE);
				if (Uteis.isAtributoPreenchido(controleConsultaExpedicaoDiploma.getDataPeriodoAnulacaoInicio())) {
					if (Uteis.isAtributoPreenchido(controleConsultaExpedicaoDiploma.getDataPeriodoAnulacaoFim())) {
						if (controleConsultaExpedicaoDiploma.getDataPeriodoAnulacaoInicio().after(controleConsultaExpedicaoDiploma.getDataPeriodoAnulacaoFim())) {
							throw new Exception("O período final não pode ser menor do que o período inicial (DATA ANULAÇÃO).");
						}
						condicaoWhere.add("(expedicaoDiploma.dataanulacao BETWEEN ? AND ?)");
						valoresFiltros.add(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(controleConsultaExpedicaoDiploma.getDataPeriodoAnulacaoInicio()));
						valoresFiltros.add(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(controleConsultaExpedicaoDiploma.getDataPeriodoAnulacaoFim()));
					} else {
						condicaoWhere.add("(expedicaoDiploma.dataanulacao >= ?)");
						valoresFiltros.add(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(controleConsultaExpedicaoDiploma.getDataPeriodoAnulacaoInicio()));
					}
				}
			}
		}
		controleConsultaExpedicaoDiploma.getControleConsultaOtimizado().setListaConsulta(consultarExpedicaoDiplomaGenerico(controleConsultaExpedicaoDiploma, fields, condicaoWhere, joins, valoresFiltros, Arrays.asList("pessoa.nome", "expedicaoDiploma.via"), Boolean.TRUE));
		if (Uteis.isAtributoPreenchido(controleConsultaExpedicaoDiploma.getControleConsultaOtimizado().getListaConsulta()) && Uteis.isAtributoPreenchido(controleConsultaExpedicaoDiploma.getControleConsultaOtimizado().getListaConsulta())) {
			List<ExpedicaoDiplomaVO> expedicaoDiplomaVOs = (List<ExpedicaoDiplomaVO>) controleConsultaExpedicaoDiploma.getControleConsultaOtimizado().getListaConsulta();
			Map<Integer, ExpedicaoDiplomaVO> map = expedicaoDiplomaVOs.stream().collect(Collectors.toMap(ExpedicaoDiplomaVO::getCodigo, Function.identity()));
			if (map != null && !map.isEmpty()) {
				for (ExpedicaoDiplomaVO obj : (List<ExpedicaoDiplomaVO>) controleConsultaExpedicaoDiploma.getControleConsultaOtimizado().getListaConsulta()) {
					if (Uteis.isAtributoPreenchido(obj) && map.containsKey(obj.getCodigo())) {
						ExpedicaoDiplomaVO expedicao = map.get(obj.getCodigo());
						for (Entry<String, Object> field : expedicao.getFieldsPersonalizados().entrySet()) {
							if ((field.getKey() != null && !field.getKey().trim().isEmpty())) {
								switch (field.getKey()) {
								case "personalizado_codigoDiplomaDigital":
									obj.setDiplomaDigital(new DocumentoAssinadoVO());
									if (field.getValue() != null) {
										obj.getDiplomaDigital().setCodigo(Integer.valueOf(field.getValue().toString()));
									}
									break;
								case "personalizado_codigoHistoricoDigital":
									obj.setHistoricoDigital(new DocumentoAssinadoVO());
									if (field.getValue() != null) {
										obj.getHistoricoDigital().setCodigo(Integer.valueOf(field.getValue().toString()));
									}
									break;
								case "personalizado_codigoDocumentacaoAcademicoDigital":
									obj.setDocumentacaoAcademicaDigital(new DocumentoAssinadoVO());
									if (field.getValue() != null) {
										obj.getDocumentacaoAcademicaDigital().setCodigo(Integer.valueOf(field.getValue().toString()));
									}
									break;
								case "personalizado_situacaoDiploma":
									obj.setSituacaoApresentar(Constantes.EMPTY);
									if (field.getValue() != null) {
										obj.setSituacaoApresentar(field.getValue().toString());
									}
									break;
								}
							}
						}
					}
				}
			}
		}
    }
	
	private List<String> getSqlConsultaExpedicaoDiplomaCompleto() {
		List<String> fields = new ArrayList<>(0);
		fields.add("expedicaodiploma.codigo AS expedicaoDiploma_codigo");
		fields.add("expedicaodiploma.dataexpedicao AS expedicaoDiploma_dataexpedicao");
		fields.add("expedicaodiploma.via AS expedicaoDiploma_via");
		fields.add("expedicaodiploma.matricula AS expedicaoDiploma_matricula");
		fields.add("expedicaodiploma.gradecurricular AS expedicaoDiploma_gradecurricular");
		fields.add("expedicaodiploma.funcionarioprimario AS expedicaoDiploma_funcionarioprimario");
		fields.add("expedicaodiploma.funcionariosecundario AS expedicaoDiploma_funcionariosecundario");
		fields.add("expedicaodiploma.unidadeensinocertificadora AS expedicaoDiploma_unidadeensinocertificadora");
		fields.add("expedicaodiploma.funcionarioterceiro AS expedicaoDiploma_funcionarioterceiro");
		fields.add("expedicaodiploma.utilizarunidadematriz AS expedicaoDiploma_utilizarunidadematriz");
		fields.add("expedicaodiploma.numeroregistrodiplomaviaanterior AS expedicaoDiploma_numeroregistrodiplomaviaanterior");
		fields.add("expedicaodiploma.numeroprocessoviaanterior AS expedicaoDiploma_numeroprocessoviaanterior");
		fields.add("expedicaodiploma.layoutdiploma AS expedicaoDiploma_layoutdiploma");
		fields.add("expedicaodiploma.cargofuncionarioprincipal AS expedicaoDiploma_cargofuncionarioprincipal");
		fields.add("expedicaodiploma.cargofuncionariosecundario AS expedicaoDiploma_cargofuncionariosecundario");
		fields.add("expedicaodiploma.cargofuncionarioterceiro AS expedicaoDiploma_cargofuncionarioterceiro");
		fields.add("expedicaodiploma.titulofuncionarioprincipal AS expedicaoDiploma_titulofuncionarioprincipal");
		fields.add("expedicaodiploma.titulofuncionariosecundario AS expedicaoDiploma_titulofuncionariosecundario");
		fields.add("expedicaodiploma.titulofuncionarioterceiro AS expedicaoDiploma_titulofuncionarioterceiro");
		fields.add("expedicaodiploma.numeroprocesso AS expedicaoDiploma_numeroprocesso");
		fields.add("expedicaodiploma.reitorregistrodiplomaviaanterior AS expedicaoDiploma_reitorregistrodiplomaviaanterior");
		fields.add("expedicaodiploma.secretariaregistrodiplomaviaanterior AS expedicaoDiploma_secretariaregistrodiplomaviaanterior");
		fields.add("expedicaodiploma.dataregistrodiplomaviaanterior AS expedicaoDiploma_dataregistrodiplomaviaanterior");
		fields.add("expedicaodiploma.cargoreitorregistrodiplomaviaanterior AS expedicaoDiploma_cargoreitorregistrodiplomaviaanterior");
		fields.add("expedicaodiploma.titulacaomasculinoapresentardiploma AS expedicaoDiploma_titulacaomasculinoapresentardiploma");
		fields.add("expedicaodiploma.titulacaofemininoapresentardiploma AS expedicaoDiploma_titulacaofemininoapresentardiploma");
		fields.add("expedicaodiploma.datapublicacaodiariooficial AS expedicaoDiploma_datapublicacaodiariooficial");
		fields.add("expedicaodiploma.serial AS expedicaoDiploma_serial");
		fields.add("expedicaodiploma.observacao AS expedicaoDiploma_observacao");
		fields.add("expedicaodiploma.numeroregistrodiploma AS expedicaoDiploma_numeroregistrodiploma");
		fields.add("expedicaodiploma.informarcamposlivroregistradora AS expedicaoDiploma_informarcamposlivroregistradora");
		fields.add("expedicaodiploma.nrlivroregistradora AS expedicaoDiploma_nrlivroregistradora");
		fields.add("expedicaodiploma.nrfolhareciboregistradora AS expedicaoDiploma_nrfolhareciboregistradora");
		fields.add("expedicaodiploma.numeroregistroregistradora AS expedicaoDiploma_numeroregistroregistradora");
		fields.add("expedicaodiploma.gerarxmldiploma AS expedicaoDiploma_gerarxmldiploma");
		fields.add("expedicaodiploma.funcionarioquarto AS expedicaoDiploma_funcionarioquarto");
		fields.add("expedicaodiploma.funcionarioquinto AS expedicaoDiploma_funcionarioquinto");
		fields.add("expedicaodiploma.cargofuncionarioquarto AS expedicaoDiploma_cargofuncionarioquarto");
		fields.add("expedicaodiploma.cargofuncionarioquinto AS expedicaoDiploma_cargofuncionarioquinto");
		fields.add("expedicaodiploma.titulofuncionarioquarto AS expedicaoDiploma_titulofuncionarioquarto");
		fields.add("expedicaodiploma.titulofuncionarioquinto AS expedicaoDiploma_titulofuncionarioquinto");
		fields.add("expedicaodiploma.codigovalidacaodiplomadigital AS expedicaoDiploma_codigovalidacaodiplomadigital");
		fields.add("expedicaodiploma.anulado AS expedicaoDiploma_anulado");
		fields.add("expedicaodiploma.textopadrao AS expedicaoDiploma_textopadrao");
		fields.add("expedicaodiploma.dataregistrodiploma AS expedicaoDiploma_dataregistrodiploma");
		fields.add("expedicaodiploma.emitidoporprocessotransferenciaassistida AS expedicaoDiploma_emitidoporprocessotransferenciaassistida");
		fields.add("expedicaodiploma.nomeiespta AS expedicaoDiploma_nomeiespta");
		fields.add("expedicaodiploma.cnpjpta AS expedicaoDiploma_cnpjpta");
		fields.add("expedicaodiploma.codigomecpta AS expedicaoDiploma_codigomecpta");
		fields.add("expedicaodiploma.ceppta AS expedicaoDiploma_ceppta");
		fields.add("expedicaodiploma.cidadepta AS expedicaoDiploma_cidadepta");
		fields.add("expedicaodiploma.logradouropta AS expedicaoDiploma_logradouropta");
		fields.add("expedicaodiploma.numeropta AS expedicaoDiploma_numeropta");
		fields.add("expedicaodiploma.complementopta AS expedicaoDiploma_complementopta");
		fields.add("expedicaodiploma.bairropta AS expedicaoDiploma_bairropta");
		fields.add("expedicaodiploma.tipodescredenciamentopta AS expedicaoDiploma_tipodescredenciamentopta");
		fields.add("expedicaodiploma.numerodescredenciamentopta AS expedicaoDiploma_numerodescredenciamentopta");
		fields.add("expedicaodiploma.datadescredenciamentopta AS expedicaoDiploma_datadescredenciamentopta");
		fields.add("expedicaodiploma.datapublicacaodescredenciamentopta AS expedicaoDiploma_datapublicacaodescredenciamentopta");
		fields.add("expedicaodiploma.veiculopublicacaodescredenciamentopta AS expedicaoDiploma_veiculopublicacaodescredenciamentopta");
		fields.add("expedicaodiploma.secaopublicacaodescredenciamentopta AS expedicaoDiploma_secaopublicacaodescredenciamentopta");
		fields.add("expedicaodiploma.paginapublicacaodescredenciamentopta AS expedicaoDiploma_paginapublicacaodescredenciamentopta");
		fields.add("expedicaodiploma.numerodoudescredenciamentopta AS expedicaoDiploma_numerodoudescredenciamentopta");
		fields.add("expedicaodiploma.emitidopordecisaojudicial AS expedicaoDiploma_emitidopordecisaojudicial");
		fields.add("expedicaodiploma.nomejuizdecisaojudicial AS expedicaoDiploma_nomejuizdecisaojudicial");
		fields.add("expedicaodiploma.numeroprocessodecisaojudicial AS expedicaoDiploma_numeroprocessodecisaojudicial");
		fields.add("expedicaodiploma.decisaojudicial AS expedicaoDiploma_decisaojudicial");
		fields.add("expedicaodiploma.informacoesadicionaisdecisaojudicial AS expedicaoDiploma_informacoesadicionaisdecisaojudicial");
		fields.add("expedicaodiploma.versaodiploma AS expedicaoDiploma_versaodiploma");
		fields.add("expedicaodiploma.dataanulacao AS expedicaoDiploma_dataanulacao");
		fields.add("expedicaodiploma.motivoanulacao AS expedicaoDiploma_motivoanulacao");
		fields.add("expedicaodiploma.datacadastro AS expedicaoDiploma_datacadastro");
		fields.add("expedicaodiploma.responsavelcadastro AS expedicaoDiploma_responsavelcadastro");
		fields.add("expedicaodiploma.anotacaoanulacao AS expedicaoDiploma_anotacaoanulacao");
//			TEXTO PADRAO
		fields.add("textopadrao.codigo AS textopadrao_codigo");
		fields.add("textopadrao.descricao AS textopadrao_descricao");
//			REITOR VIA ANTERIOR
		fields.add("reitorregistrodiplomaviaanterior.codigo AS reitorregistrodiplomaviaanterior_codigo");
		fields.add("reitorregistrodiplomaviaanterior.matricula AS reitorregistrodiplomaviaanterior_matricula");
		fields.add("reitorregistrodiplomaviaanterior.pessoa AS reitorregistrodiplomaviaanterior_pessoa");
		fields.add("reitorregistrodiplomaviaanterior.escolaridade AS reitorregistrodiplomaviaanterior_escolaridade");
//			PESSOA REITOR VIA ANTERIOR
		fields.add("reitorregistrodiplomaviaanterior_pessoa.codigo AS reitorregistrodiplomaviaanterior_pessoa_codigo");
		fields.add("reitorregistrodiplomaviaanterior_pessoa.cpf AS reitorregistrodiplomaviaanterior_pessoa_cpf");
		fields.add("reitorregistrodiplomaviaanterior_pessoa.nome AS reitorregistrodiplomaviaanterior_pessoa_nome");
//			CARGO REITOR VIA ANTERIOR
		fields.add("cargoreitorregistrodiplomaviaanterior.codigo AS cargoreitorregistrodiplomaviaanterior_codigo");
		fields.add("cargoreitorregistrodiplomaviaanterior.nome AS cargoreitorregistrodiplomaviaanterior_nome");
//			SECRETARIA VIA ANTERIOR
		fields.add("secretariaregistrodiplomaviaanterior.codigo AS secretariaregistrodiplomaviaanterior_codigo");
		fields.add("secretariaregistrodiplomaviaanterior.matricula AS secretariaregistrodiplomaviaanterior_matricula");
		fields.add("secretariaregistrodiplomaviaanterior.pessoa AS secretariaregistrodiplomaviaanterior_pessoa");
		fields.add("secretariaregistrodiplomaviaanterior.escolaridade AS secretariaregistrodiplomaviaanterior_escolaridade");
//			PESSOA SECRETARIA VIA ANTERIOR
		fields.add("secretariaregistrodiplomaviaanterior_pessoa.codigo AS secretariaregistrodiplomaviaanterior_pessoa_codigo");
		fields.add("secretariaregistrodiplomaviaanterior_pessoa.cpf AS secretariaregistrodiplomaviaanterior_pessoa_cpf");
		fields.add("secretariaregistrodiplomaviaanterior_pessoa.nome AS secretariaregistrodiplomaviaanterior_pessoa_nome");
//			UNIDADE CERTIFICADORA
		fields.add("unidadecertificadora.codigo AS unidadecertificadora_codigo");
		fields.add("unidadecertificadora.nome AS unidadecertificadora_nome");
		fields.add("unidadecertificadora.cnpj AS unidadecertificadora_cnpj");
//			FUNCIONARIO PRIMARIO
		fields.add("funcionarioprimario.matricula AS funcionarioprimario_matricula");
		fields.add("funcionarioprimario.pessoa AS funcionarioprimario_pessoa");
		fields.add("funcionarioprimario.codigo AS funcionarioprimario_codigo");
		fields.add("funcionarioprimario.escolaridade AS funcionarioprimario_escolaridade");
		fields.add("funcionarioprimario.arquivoassinatura AS funcionarioprimario_arquivoassinatura");
//			FUNCIONARIO SECUNDARIO
		fields.add("funcionariosecundario.matricula AS funcionariosecundario_matricula");
		fields.add("funcionariosecundario.pessoa AS funcionariosecundario_pessoa");
		fields.add("funcionariosecundario.codigo AS funcionariosecundario_codigo");
		fields.add("funcionariosecundario.escolaridade AS funcionariosecundario_escolaridade");
		fields.add("funcionariosecundario.arquivoassinatura AS funcionariosecundario_arquivoassinatura");
//			FUNCIONARIO TERCEIRO
		fields.add("funcionarioterceiro.matricula AS funcionarioterceiro_matricula");
		fields.add("funcionarioterceiro.pessoa AS funcionarioterceiro_pessoa");
		fields.add("funcionarioterceiro.codigo AS funcionarioterceiro_codigo");
		fields.add("funcionarioterceiro.escolaridade AS funcionarioterceiro_escolaridade");
		fields.add("funcionarioterceiro.arquivoassinatura AS funcionarioterceiro_arquivoassinatura");
//			FUNCIONARIO QUARTO
		fields.add("funcionarioquarto.matricula AS funcionarioquarto_matricula");
		fields.add("funcionarioquarto.pessoa AS funcionarioquarto_pessoa");
		fields.add("funcionarioquarto.codigo AS funcionarioquarto_codigo");
		fields.add("funcionarioquarto.escolaridade AS funcionarioquarto_escolaridade");
		fields.add("funcionarioquarto.arquivoassinatura AS funcionarioquarto_arquivoassinatura");
//			FUNCIONARIO QUINTO
		fields.add("funcionarioquinto.matricula AS funcionarioquinto_matricula");
		fields.add("funcionarioquinto.pessoa AS funcionarioquinto_pessoa");
		fields.add("funcionarioquinto.codigo AS funcionarioquinto_codigo");
		fields.add("funcionarioquinto.escolaridade AS funcionarioquinto_escolaridade");
		fields.add("funcionarioquinto.arquivoassinatura AS funcionarioquinto_escolaridade");
//			PESSOA FUNCIONARIO PRIMARIO
		fields.add("funcionarioprimario_pessoa.codigo AS funcionarioprimario_pessoa_codigo");
		fields.add("funcionarioprimario_pessoa.cpf AS funcionarioprimario_pessoa_cpf");
		fields.add("funcionarioprimario_pessoa.nome AS funcionarioprimario_pessoa_nome");
		fields.add("funcionarioprimario_pessoa.tipoassinaturadocumentoenum AS funcionarioprimario_pessoa_tipoassinaturadocumentoenum");
//			PESSOA FUNCIONARIO SECUNDARIO
		fields.add("funcionariosecundario_pessoa.codigo AS funcionariosecundario_pessoa_codigo");
		fields.add("funcionariosecundario_pessoa.cpf AS funcionariosecundario_pessoa_cpf");
		fields.add("funcionariosecundario_pessoa.nome AS funcionariosecundario_pessoa_nome");
		fields.add("funcionariosecundario_pessoa.tipoassinaturadocumentoenum AS funcionariosecundario_pessoa_tipoassinaturadocumentoenum");
//			PESSOA FUNCIONARIO TERCEIRO
		fields.add("funcionarioterceiro_pessoa.codigo AS funcionariosecundario_pessoa_nome");
		fields.add("funcionarioterceiro_pessoa.cpf AS funcionarioterceiro_pessoa_cpf");
		fields.add("funcionarioterceiro_pessoa.nome AS funcionarioterceiro_pessoa_nome");
		fields.add("funcionarioterceiro_pessoa.tipoassinaturadocumentoenum AS funcionarioterceiro_pessoa_tipoassinaturadocumentoenum");
//			PESSOA FUNCIONARIO QUARTO
		fields.add("funcionarioquarto_pessoa.codigo AS funcionarioquarto_pessoa_codigo");
		fields.add("funcionarioquarto_pessoa.cpf AS funcionarioquarto_pessoa_cpf");
		fields.add("funcionarioquarto_pessoa.nome AS funcionarioquarto_pessoa_nome");
		fields.add("funcionarioquarto_pessoa.tipoassinaturadocumentoenum AS funcionarioquarto_pessoa_tipoassinaturadocumentoenum");
//			PESSOA FUNCIONARIO QUINTO
		fields.add("funcionarioquinto_pessoa.codigo AS funcionarioquinto_pessoa_codigo");
		fields.add("funcionarioquinto_pessoa.cpf AS funcionarioquinto_pessoa_cpf");
		fields.add("funcionarioquinto_pessoa.nome AS funcionarioquinto_pessoa_nome");
		fields.add("funcionarioquinto_pessoa.tipoassinaturadocumentoenum AS funcionarioquinto_pessoa_tipoassinaturadocumentoenum");
//			CARGO FUNCIONARIO PRIMARIO
		fields.add("cargofuncionarioprincipal.codigo AS cargofuncionarioprincipal_codigo");
		fields.add("cargofuncionarioprincipal.nome AS cargofuncionarioprincipal_nome");
//			CARGO FUNCIONARIO SECUNDARIO
		fields.add("cargofuncionariosecundario.codigo AS cargofuncionariosecundario_codigo");
		fields.add("cargofuncionariosecundario.nome AS cargofuncionariosecundario_nome");
//			CARGO FUNCIONARIO TERCEIRO
		fields.add("cargofuncionarioterceiro.codigo AS cargofuncionarioterceiro_codigo");
		fields.add("cargofuncionarioterceiro.nome AS cargofuncionarioterceiro_nome");
//			CARGO FUNCIONARIO QUARTO
		fields.add("cargofuncionarioquarto.codigo AS cargofuncionarioquarto_codigo");
		fields.add("cargofuncionarioquarto.nome AS cargofuncionarioquarto_nome");
//			CARGO FUNCIONARIO QUINTO
		fields.add("cargofuncionarioquinto.codigo AS cargofuncionarioquinto_codigo");
		fields.add("cargofuncionarioquinto.nome AS cargofuncionarioquinto_nome");
//			ARQUIVO ASSINATURA FUNCIONARIO PRIMARIO
		fields.add("funcionarioprimario_arquivoassinatura.codigo AS funcionarioprimario_arquivoassinatura_codigo");
		fields.add("funcionarioprimario_arquivoassinatura.nome AS funcionarioprimario_arquivoassinatura_nome");
		fields.add("funcionarioprimario_arquivoassinatura.descricao AS funcionarioprimario_arquivoassinatura_descricao");
		fields.add("funcionarioprimario_arquivoassinatura.dataupload AS funcionarioprimario_arquivoassinatura_dataupload");
		fields.add("funcionarioprimario_arquivoassinatura.pastabasearquivo AS funcionarioprimario_arquivoassinatura_pastabasearquivo");
		fields.add("funcionarioprimario_arquivoassinatura.servidorarquivoonline AS funcionarioprimario_arquivoassinatura_servidorarquivoonline");
//			ARQUIVO ASSINATURA FUNCIONARIO SECUNDARIO
		fields.add("funcionariosecundario_arquivoassinatura.codigo AS funcionariosecundario_arquivoassinatura_codigo");
		fields.add("funcionariosecundario_arquivoassinatura.nome AS funcionariosecundario_arquivoassinatura_nome");
		fields.add("funcionariosecundario_arquivoassinatura.descricao AS funcionariosecundario_arquivoassinatura_descricao");
		fields.add("funcionariosecundario_arquivoassinatura.dataupload AS funcionariosecundario_arquivoassinatura_dataupload");
		fields.add("funcionariosecundario_arquivoassinatura.pastabasearquivo AS funcionariosecundario_arquivoassinatura_pastabasearquivo");
		fields.add("funcionariosecundario_arquivoassinatura.servidorarquivoonline AS funcionariosecundario_arquivoassinatura_servidorarquivoonline");
//			ARQUIVO ASSINATURA FUNCIONARIO TERCEIRO
		fields.add("funcionarioterceiro_arquivoassinatura.codigo AS funcionarioterceiro_arquivoassinatura_codigo");
		fields.add("funcionarioterceiro_arquivoassinatura.nome AS funcionarioterceiro_arquivoassinatura_nome");
		fields.add("funcionarioterceiro_arquivoassinatura.descricao AS funcionarioterceiro_arquivoassinatura_descricao");
		fields.add("funcionarioterceiro_arquivoassinatura.dataupload AS funcionarioterceiro_arquivoassinatura_dataupload");
		fields.add("funcionarioterceiro_arquivoassinatura.pastabasearquivo AS funcionarioterceiro_arquivoassinatura_pastabasearquivo");
		fields.add("funcionarioterceiro_arquivoassinatura.servidorarquivoonline AS funcionarioterceiro_arquivoassinatura_servidorarquivoonline");
//			ARQUIVO ASSINATURA FUNCIONARIO QUARTO
		fields.add("funcionarioquarto_arquivoassinatura.codigo AS funcionarioquarto_arquivoassinatura_codigo");
		fields.add("funcionarioquarto_arquivoassinatura.nome AS funcionarioquarto_arquivoassinatura_nome");
		fields.add("funcionarioquarto_arquivoassinatura.descricao AS funcionarioquarto_arquivoassinatura_descricao");
		fields.add("funcionarioquarto_arquivoassinatura.dataupload AS funcionarioquarto_arquivoassinatura_dataupload");
		fields.add("funcionarioquarto_arquivoassinatura.pastabasearquivo AS funcionarioquarto_arquivoassinatura_pastabasearquivo");
		fields.add("funcionarioquarto_arquivoassinatura.servidorarquivoonline AS funcionarioquarto_arquivoassinatura_servidorarquivoonline");
//			ARQUIVO ASSINATURA FUNCIONARIO QUINTO
		fields.add("funcionarioquinto_arquivoassinatura.codigo AS funcionarioquinto_arquivoassinatura_codigo");
		fields.add("funcionarioquinto_arquivoassinatura.nome AS funcionarioquinto_arquivoassinatura_nome");
		fields.add("funcionarioquinto_arquivoassinatura.descricao AS funcionarioquinto_arquivoassinatura_descricao");
		fields.add("funcionarioquinto_arquivoassinatura.dataupload AS funcionarioquinto_arquivoassinatura_dataupload");
		fields.add("funcionarioquinto_arquivoassinatura.pastabasearquivo AS funcionarioquinto_arquivoassinatura_pastabasearquivo");
		fields.add("funcionarioquinto_arquivoassinatura.servidorarquivoonline AS funcionarioquinto_arquivoassinatura_servidorarquivoonline");
//			RESPONSAVEL CADASTRO
		fields.add("responsavelcadastro.codigo AS responsavelcadastro_codigo");
		fields.add("responsavelcadastro.nome AS responsavelcadastro_nome");
		fields.add("responsavelcadastro.pessoa AS responsavelcadastro_pessoa");
//			CIDADE TRANSFERENCIA ASSISTIDA
		fields.add("cidadepta.codigo AS cidadepta_codigo");
		fields.add("cidadepta.nome AS cidadepta_nome");
		fields.add("cidadepta.codigoibge AS cidadepta_codigoibge");
		fields.add("cidadepta.estado AS cidadepta_estado");
//			ESTADO CIDADE TRANSFERENCIA ASSISTIDA
		fields.add("cidadepta_estado.codigo AS cidadepta_estado_codigo");
		fields.add("cidadepta_estado.nome AS cidadepta_estado_nome");
		fields.add("cidadepta_estado.codigoibge AS cidadepta_estado_codigoibge");
		fields.add("cidadepta_estado.sigla AS cidadepta_estado_sigla");
		fields.add("cidadepta_estado.paiz AS cidadepta_estado_paiz");
//			RESPONSAVEL ANULAÇÃO
		fields.add("responsavelanulacao.codigo AS responsavelanulacao_codigo");
		fields.add("responsavelanulacao.nome AS responsavelanulacao_nome");
//			PESSOA RESPONSAVEL ANULAÇÃO
		fields.add("responsavelanulacao_pessoa.codigo AS responsavelanulacao_pessoa_codigo");
		fields.add("responsavelanulacao_pessoa.nome AS responsavelanulacao_pessoa_nome");
		return fields;
	}
	
	private List<String> getSqlConsultaMatriculaExpedicaoDiplomaCompleto() {
		List<String> fields = new ArrayList<>(0);
		fields.add("matricula.turno AS matricula_turno");
		fields.add("matricula.usuario AS matricula_usuario");
		fields.add("matricula.inscricao AS matricula_inscricao");
		fields.add("matricula.situacaofinanceira AS matricula_situacaofinanceira");
		fields.add("matricula.situacao AS matricula_situacao");
		fields.add("matricula.data AS matricula_data");
		fields.add("matricula.curso AS matricula_curso");
		fields.add("matricula.unidadeensino AS matricula_unidadeensino");
		fields.add("matricula.aluno AS matricula_aluno");
		fields.add("matricula.matricula AS matricula_matricula");
		fields.add("matricula.tranferenciaentrada AS matricula_tranferenciaentrada");
		fields.add("matricula.formaingresso AS matricula_formaingresso");
		fields.add("matricula.atividadecomplementar AS matricula_atividadecomplementar");
		fields.add("matricula.anoingresso AS matricula_anoingresso");
		fields.add("matricula.semestreingresso AS matricula_semestreingresso");
		fields.add("matricula.anoconclusao AS matricula_anoconclusao");
		fields.add("matricula.semestreconclusao AS matricula_semestreconclusao");
		fields.add("matricula.disciplinasprocseletivo AS matricula_disciplinasprocseletivo");
		fields.add("matricula.fezenade AS matricula_fezenade");
		fields.add("matricula.dataenade AS matricula_dataenade");
		fields.add("matricula.notaenade AS matricula_notaenade");
		fields.add("matricula.enade AS matricula_enade");
		fields.add("matricula.alunoabandonoucurso AS matricula_alunoabandonoucurso");
		fields.add("matricula.observacaocomplementar AS matricula_observacaocomplementar");
		fields.add("matricula.localarmazenamentodocumentosmatricula AS matricula_localarmazenamentodocumentosmatricula");
		fields.add("matricula.datainiciocurso AS matricula_datainiciocurso");
		fields.add("matricula.dataconclusaocurso AS matricula_dataconclusaocurso");
		fields.add("matricula.formacaoacademica AS matricula_formacaoacademica");
		fields.add("matricula.autorizacaocurso AS matricula_autorizacaocurso");
		fields.add("matricula.tipomatricula AS matricula_tipomatricula");
		fields.add("matricula.matriculasuspensa AS matricula_matriculasuspensa");
		fields.add("matricula.databasesuspensao AS matricula_databasesuspensao");
		fields.add("matricula.datacolacaograu AS matricula_datacolacaograu");
		fields.add("matricula.mesingresso AS matricula_mesingresso");
		fields.add("matricula.datacadastro AS matricula_datacadastro");
		fields.add("matricula.gradecurricularatual AS matricula_gradecurricularatual");
		fields.add("matricula.observacaodiploma AS matricula_observacaodiploma");
		fields.add("matricula.classificacaoingresso AS matricula_classificacaoingresso");
		fields.add("matricula.semestreanoingressocenso AS matricula_semestreanoingressocenso");
		fields.add("matricula.renovacaoreconhecimento AS matricula_renovacaoreconhecimento");
		fields.add("matricula.orientadormonografia AS matricula_orientadormonografia");
		fields.add("matricula.cargahorariamonografia AS matricula_cargahorariamonografia");
		fields.add("matricula.titulacaoorientadormonografia AS matricula_titulacaoorientadormonografia");
//		fields.add("matricula.datamonografia AS matricula_datamonografia");
//		fields.add("matricula.cargahorariamatrizcurricular AS matricula_cargahorariamatrizcurricular");
//		fields.add("matricula.areaconcentracaomestrado AS matricula_areaconcentracaomestrado");
//		fields.add("matricula.datarealizacaoprovamestrado AS matricula_datarealizacaoprovamestrado");
//		fields.add("matricula.dataexamequalificacaomestrado AS matricula_dataexamequalificacaomestrado");
		fields.add("matricula.notaenem AS matricula_notaenem");
		fields.add("matricula.matriculaserasa AS matricula_matriculaserasa");
		fields.add("matricula.consultor AS matricula_consultor");
		fields.add("matricula.nomemonografia AS matricula_nomemonografia");
		fields.add("matricula.notamonografia AS matricula_notamonografia");
		fields.add("matricula.tipotrabalhoconclusaocurso AS matricula_tipotrabalhoconclusaocurso");
//		fields.add("matricula.permitirassinarcontratopendenciadocumentacao AS matricula_permitirassinarcontratopendenciadocumentacao");
		fields.add("matricula.naoenviarmensagemcobranca AS matricula_naoenviarmensagemcobranca");
		fields.add("matricula.alunoconcluiudisciplinasregulares AS matricula_alunoconcluiudisciplinasregulares");
		fields.add("matricula.qtddiasadiarbloqueio AS matricula_qtddiasadiarbloqueio");
		fields.add("matricula.canceladofinanceiro AS matricula_canceladofinanceiro");
		fields.add("matricula.dataemissaohistorico AS matricula_dataemissaohistorico");
		fields.add("matricula.codigofinanceiromatricula AS matricula_codigofinanceiromatricula");
		fields.add("matricula.bloqueioporsolicitacaoliberacaomatricula AS matricula_bloqueioporsolicitacaoliberacaomatricula");
//		fields.add("matricula.idcrmeducacional AS matricula_idcrmeducacional");
		fields.add("matricula.dataprocessoseletivo AS matricula_dataprocessoseletivo");
		fields.add("matricula.totalpontoprocseletivo AS matricula_totalpontoprocseletivo");
//		fields.add("matricula.datapublicacaoartigo AS matricula_datapublicacaoartigo");
//		fields.add("matricula.datadefesatcc AS matricula_datadefesatcc");
		fields.add("matricula.localprocessoseletivo AS matricula_localprocessoseletivo");
//		fields.add("matricula.ingressantecota AS matricula_ingressantecota");
		fields.add("matricula.permitirinclusaoexclusaodisciplinasrenovacao AS matricula_permitirinclusaoexclusaodisciplinasrenovacao");
		fields.add("matricula.horascomplementares AS matricula_horascomplementares");
//			ALUNO
		fields.add("matricula_aluno.tituloeleitoral AS matricula_aluno_tituloeleitoral");
		fields.add("matricula_aluno.orgaoemissor AS matricula_aluno_orgaoemissor");
		fields.add("matricula_aluno.estadoemissaorg AS matricula_aluno_estadoemissaorg");
		fields.add("matricula_aluno.dataemissaorg AS matricula_aluno_dataemissaorg");
		fields.add("matricula_aluno.rg AS matricula_aluno_rg");
		fields.add("matricula_aluno.cpf AS matricula_aluno_cpf");
		fields.add("matricula_aluno.nacionalidade AS matricula_aluno_nacionalidade");
		fields.add("matricula_aluno.naturalidade AS matricula_aluno_naturalidade");
		fields.add("matricula_aluno.datanasc AS matricula_aluno_datanasc");
		fields.add("matricula_aluno.email AS matricula_aluno_email");
		fields.add("matricula_aluno.celular AS matricula_aluno_celular");
		fields.add("matricula_aluno.telefonerecado AS matricula_aluno_telefonerecado");
		fields.add("matricula_aluno.telefoneres AS matricula_aluno_telefoneres");
		fields.add("matricula_aluno.telefonecomer AS matricula_aluno_telefonecomer");
		fields.add("matricula_aluno.estadocivil AS matricula_aluno_estadocivil");
		fields.add("matricula_aluno.sexo AS matricula_aluno_sexo");
		fields.add("matricula_aluno.cidade AS matricula_aluno_cidade");
		fields.add("matricula_aluno.complemento AS matricula_aluno_complemento");
		fields.add("matricula_aluno.cep AS matricula_aluno_cep");
		fields.add("matricula_aluno.numero AS matricula_aluno_numero");
		fields.add("matricula_aluno.setor AS matricula_aluno_setor");
		fields.add("matricula_aluno.endereco AS matricula_aluno_endereco");
		fields.add("matricula_aluno.nome AS matricula_aluno_nome");
		fields.add("matricula_aluno.codigo AS matricula_aluno_codigo");
		fields.add("matricula_aluno.aluno AS matricula_aluno_aluno");
		fields.add("matricula_aluno.professor AS matricula_aluno_professor");
		fields.add("matricula_aluno.funcionario AS matricula_aluno_funcionario");
		fields.add("matricula_aluno.candidato AS matricula_aluno_candidato");
		fields.add("matricula_aluno.ativo AS matricula_aluno_ativo");
		fields.add("matricula_aluno.arquivoimagem AS matricula_aluno_arquivoimagem");
		fields.add("matricula_aluno.coordenador AS matricula_aluno_coordenador");
		fields.add("matricula_aluno.nomebatismo AS matricula_aluno_nomebatismo");
//		fields.add("matricula_aluno.utilizardocumentoestrangeiro AS matricula_aluno_utilizardocumentoestrangeiro");
//		fields.add("matricula_aluno.tipodocumentoestrangeiro AS matricula_aluno_tipodocumentoestrangeiro");
//		fields.add("matricula_aluno.numerodocumentoestrangeiro AS matricula_aluno_numerodocumentoestrangeiro");
//			NACIONALIDADE
		fields.add("matricula_aluno_nacionalidade.codigo AS matricula_aluno_nacionalidade_codigo");
		fields.add("matricula_aluno_nacionalidade.nome AS matricula_aluno_nacionalidade_nome");
		fields.add("matricula_aluno_nacionalidade.nacionalidade AS matricula_aluno_nacionalidade_nacionalidade");
//			NATURALIDADE
		fields.add("matricula_aluno_naturalidade.codigo AS matricula_aluno_naturalidade_codigo");
		fields.add("matricula_aluno_naturalidade.nome AS matricula_aluno_naturalidade_nome");
		fields.add("matricula_aluno_naturalidade.codigoibge AS matricula_aluno_naturalidade_codigoibge");
		fields.add("matricula_aluno_naturalidade.estado AS matricula_aluno_naturalidade_estado");
//			ESTADO NATURALIDADE
		fields.add("matricula_aluno_naturalidade_estado.codigo AS matricula_aluno_naturalidade_estado_codigo");
		fields.add("matricula_aluno_naturalidade_estado.nome AS matricula_aluno_naturalidade_estado_nome");
		fields.add("matricula_aluno_naturalidade_estado.codigoibge AS matricula_aluno_naturalidade_estado_codigoibge");
		fields.add("matricula_aluno_naturalidade_estado.sigla AS matricula_aluno_naturalidade_estado_sigla");
		fields.add("matricula_aluno_naturalidade_estado.paiz AS matricula_aluno_naturalidade_estado_paiz");
//			AUTORIZAÇÃO CURSO
		fields.add("matricula_autorizacaocurso.codigo AS matricula_autorizacaocurso_codigo");
		fields.add("matricula_autorizacaocurso.nome AS matricula_autorizacaocurso_nome");
		fields.add("matricula_autorizacaocurso.data AS matricula_autorizacaocurso_data");
		fields.add("matricula_autorizacaocurso.curso AS matricula_autorizacaocurso_curso");
		fields.add("matricula_autorizacaocurso.tipoautorizacaocursoenum AS matricula_autorizacaocurso_tipoautorizacaocursoenum");
		fields.add("matricula_autorizacaocurso.numero AS matricula_autorizacaocurso_numero");
		fields.add("matricula_autorizacaocurso.datacredenciamento AS matricula_autorizacaocurso_datacredenciamento");
		fields.add("matricula_autorizacaocurso.veiculopublicacao AS matricula_autorizacaocurso_veiculopublicacao");
		fields.add("matricula_autorizacaocurso.secaopublicacao AS matricula_autorizacaocurso_secaopublicacao");
		fields.add("matricula_autorizacaocurso.paginapublicacao AS matricula_autorizacaocurso_paginapublicacao");
		fields.add("matricula_autorizacaocurso.numerodou AS matricula_autorizacaocurso_numerodou");
		fields.add("matricula_autorizacaocurso.emtramitacao AS matricula_autorizacaocurso_emtramitacao");
		fields.add("matricula_autorizacaocurso.datacadastro AS matricula_autorizacaocurso_datacadastro");
		fields.add("matricula_autorizacaocurso.dataprotocolo AS matricula_autorizacaocurso_dataprotocolo");
//			RENOVAÇÃO RECONHECIMENTO
		fields.add("matricula_renovacaoreconhecimento.codigo AS matricula_renovacaoreconhecimento_codigo");
		fields.add("matricula_renovacaoreconhecimento.nome AS matricula_renovacaoreconhecimento_nome");
		fields.add("matricula_renovacaoreconhecimento.data AS matricula_renovacaoreconhecimento_data");
		fields.add("matricula_renovacaoreconhecimento.curso AS matricula_renovacaoreconhecimento_curso");
		fields.add("matricula_renovacaoreconhecimento.tipoautorizacaocursoenum AS matricula_renovacaoreconhecimento_tipoautorizacaocursoenum");
		fields.add("matricula_renovacaoreconhecimento.numero AS matricula_renovacaoreconhecimento_numero");
		fields.add("matricula_renovacaoreconhecimento.datacredenciamento AS matricula_renovacaoreconhecimento_datacredenciamento");
		fields.add("matricula_renovacaoreconhecimento.veiculopublicacao AS matricula_renovacaoreconhecimento_veiculopublicacao");
		fields.add("matricula_renovacaoreconhecimento.secaopublicacao AS matricula_renovacaoreconhecimento_secaopublicacao");
		fields.add("matricula_renovacaoreconhecimento.paginapublicacao AS matricula_renovacaoreconhecimento_paginapublicacao");
		fields.add("matricula_renovacaoreconhecimento.numerodou AS matricula_renovacaoreconhecimento_numerodou");
		fields.add("matricula_renovacaoreconhecimento.emtramitacao AS matricula_renovacaoreconhecimento_emtramitacao");
		fields.add("matricula_renovacaoreconhecimento.datacadastro AS matricula_renovacaoreconhecimento_datacadastro");
		fields.add("matricula_renovacaoreconhecimento.dataprotocolo AS matricula_renovacaoreconhecimento_dataprotocolo");
//			UNIDADE ENSINO
		fields.add("matricula_unidadeensino.matriz AS matricula_unidadeensino_matriz");
		fields.add("matricula_unidadeensino.email AS matricula_unidadeensino_email");
		fields.add("matricula_unidadeensino.cnpj AS matricula_unidadeensino_cnpj");
		fields.add("matricula_unidadeensino.tipoempresa AS matricula_unidadeensino_tipoempresa");
		fields.add("matricula_unidadeensino.cep AS matricula_unidadeensino_cep");
		fields.add("matricula_unidadeensino.cidade AS matricula_unidadeensino_cidade");
		fields.add("matricula_unidadeensino.complemento AS matricula_unidadeensino_complemento");
		fields.add("matricula_unidadeensino.numero AS matricula_unidadeensino_numero");
		fields.add("matricula_unidadeensino.setor AS matricula_unidadeensino_setor");
		fields.add("matricula_unidadeensino.endereco AS matricula_unidadeensino_endereco");
		fields.add("matricula_unidadeensino.razaosocial AS matricula_unidadeensino_razaosocial");
		fields.add("matricula_unidadeensino.nome AS matricula_unidadeensino_nome");
		fields.add("matricula_unidadeensino.codigo AS matricula_unidadeensino_codigo");
		fields.add("matricula_unidadeensino.abreviatura AS matricula_unidadeensino_abreviatura");
		fields.add("matricula_unidadeensino.configuracoes AS matricula_unidadeensino_configuracoes");
		fields.add("matricula_unidadeensino.codigoies AS matricula_unidadeensino_codigoies");
		fields.add("matricula_unidadeensino.credenciamentoportaria AS matricula_unidadeensino_credenciamentoportaria");
		fields.add("matricula_unidadeensino.datapublicacaodo AS matricula_unidadeensino_datapublicacaodo");
		fields.add("matricula_unidadeensino.mantenedora AS matricula_unidadeensino_mantenedora");
		fields.add("matricula_unidadeensino.desativada AS matricula_unidadeensino_desativada");
		fields.add("matricula_unidadeensino.credenciamento AS matricula_unidadeensino_credenciamento");
		fields.add("matricula_unidadeensino.nomeexpedicaodiploma AS matricula_unidadeensino_nomeexpedicaodiploma");
		fields.add("matricula_unidadeensino.codigoiesmantenedora AS matricula_unidadeensino_codigoiesmantenedora");
		fields.add("matricula_unidadeensino.cnpjmantenedora AS matricula_unidadeensino_cnpjmantenedora");
		fields.add("matricula_unidadeensino.unidadecertificadora AS matricula_unidadeensino_unidadecertificadora");
		fields.add("matricula_unidadeensino.cnpjunidadecertificadora AS matricula_unidadeensino_cnpjunidadecertificadora");
		fields.add("matricula_unidadeensino.codigoiesunidadecertificadora AS matricula_unidadeensino_codigoiesunidadecertificadora");
		fields.add("matricula_unidadeensino.configuracaoged AS matricula_unidadeensino_configuracaoged");
		fields.add("matricula_unidadeensino.tipoautorizacaoenum AS matricula_unidadeensino_tipoautorizacaoenum");
		fields.add("matricula_unidadeensino.numerocredenciamento AS matricula_unidadeensino_numerocredenciamento");
		fields.add("matricula_unidadeensino.datacredenciamento AS matricula_unidadeensino_datacredenciamento");
		fields.add("matricula_unidadeensino.veiculopublicacaocredenciamento AS matricula_unidadeensino_veiculopublicacaocredenciamento");
		fields.add("matricula_unidadeensino.secaopublicacaocredenciamento AS matricula_unidadeensino_secaopublicacaocredenciamento");
		fields.add("matricula_unidadeensino.paginapublicacaocredenciamento AS matricula_unidadeensino_paginapublicacaocredenciamento");
		fields.add("matricula_unidadeensino.numerodoucredenciamento AS matricula_unidadeensino_numerodoucredenciamento");
		fields.add("matricula_unidadeensino.informardadosregistradora AS matricula_unidadeensino_informardadosregistradora");
		fields.add("matricula_unidadeensino.utilizarenderecounidadeensinoregistradora AS mat_uni_utilizarenderecounidadeensinoregistradora");
		fields.add("matricula_unidadeensino.cepregistradora AS matricula_unidadeensino_cepregistradora");
		fields.add("matricula_unidadeensino.cidaderegistradora AS matricula_unidadeensino_cidaderegistradora");
		fields.add("matricula_unidadeensino.complementoregistradora AS matricula_unidadeensino_complementoregistradora");
		fields.add("matricula_unidadeensino.bairroregistradora AS matricula_unidadeensino_bairroregistradora");
		fields.add("matricula_unidadeensino.enderecoregistradora AS matricula_unidadeensino_enderecoregistradora");
		fields.add("matricula_unidadeensino.numeroregistradora AS matricula_unidadeensino_numeroregistradora");
		fields.add("matricula_unidadeensino.utilizarcredenciamentounidadeensino AS matricula_unidadeensino_utilizarcredenciamentounidadeensino");
		fields.add("matricula_unidadeensino.numerocredenciamentoregistradora AS matricula_unidadeensino_numerocredenciamentoregistradora");
		fields.add("matricula_unidadeensino.datacredenciamentoregistradora AS matricula_unidadeensino_datacredenciamentoregistradora");
		fields.add("matricula_unidadeensino.datapublicacaodoregistradora AS matricula_unidadeensino_datapublicacaodoregistradora");
		fields.add("matricula_unidadeensino.veiculopublicacaocredenciamentoregistradora AS mat_uni_veiculopublicacaocredenciamentoregistradora");
		fields.add("matricula_unidadeensino.secaopublicacaocredenciamentoregistradora AS mat_uni_secaopublicacaocredenciamentoregistradora");
		fields.add("matricula_unidadeensino.paginapublicacaocredenciamentoregistradora AS mat_uni_paginapublicacaocredenciamentoregistradora");
		fields.add("matricula_unidadeensino.numeropublicacaocredenciamentoregistradora AS mat_uni_numeropublicacaocredenciamentoregistradora");
		fields.add("matricula_unidadeensino.utilizarmantenedoraunidadeensino AS matricula_unidadeensino_utilizarmantenedoraunidadeensino");
		fields.add("matricula_unidadeensino.mantenedoraregistradora AS matricula_unidadeensino_mantenedoraregistradora");
		fields.add("matricula_unidadeensino.cnpjmantenedoraregistradora AS matricula_unidadeensino_cnpjmantenedoraregistradora");
		fields.add("matricula_unidadeensino.cnpjmantenedoraregistradora AS matricula_unidadeensino_cepmantenedoraregistradora");
		fields.add("matricula_unidadeensino.enderecomantenedoraregistradora AS matricula_unidadeensino_enderecomantenedoraregistradora");
		fields.add("matricula_unidadeensino.numeromantenedoraregistradora AS matricula_unidadeensino_numeromantenedoraregistradora");
		fields.add("matricula_unidadeensino.cidademantenedoraregistradora AS matricula_unidadeensino_cidademantenedoraregistradora");
		fields.add("matricula_unidadeensino.complementomantenedoraregistradora AS matricula_unidadeensino_complementomantenedoraregistradora");
		fields.add("matricula_unidadeensino.bairromantenedoraregistradora AS matricula_unidadeensino_bairromantenedoraregistradora");
		fields.add("matricula_unidadeensino.utilizarenderecounidadeensinomantenedora AS mat_uni_utilizarenderecounidadeensinomantenedora");
		fields.add("matricula_unidadeensino.cepmantenedora AS matricula_unidadeensino_cepmantenedora");
		fields.add("matricula_unidadeensino.enderecomantenedora AS matricula_unidadeensino_enderecomantenedora");
		fields.add("matricula_unidadeensino.numeromantenedora AS matricula_unidadeensino_numeromantenedora");
		fields.add("matricula_unidadeensino.cidademantenedora AS matricula_unidadeensino_cidademantenedora");
		fields.add("matricula_unidadeensino.complementomantenedora AS matricula_unidadeensino_complementomantenedora");
		fields.add("matricula_unidadeensino.bairromantenedora AS matricula_unidadeensino_bairromantenedora");
		fields.add("matricula_unidadeensino.numerorecredenciamento AS matricula_unidadeensino_numerorecredenciamento");
		fields.add("matricula_unidadeensino.datarecredenciamento AS matricula_unidadeensino_datarecredenciamento");
		fields.add("matricula_unidadeensino.datapublicacaorecredenciamento AS matricula_unidadeensino_datapublicacaorecredenciamento");
		fields.add("matricula_unidadeensino.veiculopublicacaorecredenciamento AS matricula_unidadeensino_veiculopublicacaorecredenciamento");
		fields.add("matricula_unidadeensino.secaopublicacaorecredenciamento AS matricula_unidadeensino_secaopublicacaorecredenciamento");
		fields.add("matricula_unidadeensino.paginapublicacaorecredenciamento AS matricula_unidadeensino_paginapublicacaorecredenciamento");
		fields.add("matricula_unidadeensino.numerodoucredenciamento AS matricula_unidadeensino_numerodourecredenciamento");
		fields.add("matricula_unidadeensino.tipoautorizacaorecredenciamento AS matricula_unidadeensino_tipoautorizacaorecredenciamento");
		fields.add("matricula_unidadeensino.numerorenovacaorecredenciamento AS matricula_unidadeensino_numerorenovacaorecredenciamento");
		fields.add("matricula_unidadeensino.datarenovacaorecredenciamento AS matricula_unidadeensino_datarenovacaorecredenciamento");
		fields.add("matricula_unidadeensino.datapublicacaorenovacaorecredenciamento AS mat_uni_datapublicacaorenovacaorecredenciamento");
		fields.add("matricula_unidadeensino.veiculopublicacaorenovacaorecredenciamento AS mat_uni_veiculopublicacaorenovacaorecredenciamento");
		fields.add("matricula_unidadeensino.secaopublicacaorenovacaorecredenciamento AS mat_uni_secaopublicacaorenovacaorecredenciamento");
		fields.add("matricula_unidadeensino.paginapublicacaorenovacaorecredenciamento AS mat_uni_paginapublicacaorenovacaorecredenciamento");
		fields.add("matricula_unidadeensino.numerodourenovacaorecredenciamento AS matricula_unidadeensino_numerodourenovacaorecredenciamento");
		fields.add("matricula_unidadeensino.tipoautorizacaorenovacaorecredenciamento AS mat_uni_tipoautorizacaorenovacaorecredenciamento");
		fields.add("matricula_unidadeensino.tipoautorizacaocredenciamentoregistradora AS mat_uni_tipoautorizacaocredenciamentoregistradora");
		fields.add("matricula_unidadeensino.configuracaodiplomadigital AS matricula_unidadeensino_configuracaodiplomadigital");
		fields.add("matricula_unidadeensino.numerocredenciamentoead AS matricula_unidadeensino_numerocredenciamentoead");
		fields.add("matricula_unidadeensino.credenciamentoead AS matricula_unidadeensino_credenciamentoead");
		fields.add("matricula_unidadeensino.datacredenciamentoead AS matricula_unidadeensino_datacredenciamentoead");
		fields.add("matricula_unidadeensino.datapublicacaodoead AS matricula_unidadeensino_datapublicacaodoead");
		fields.add("matricula_unidadeensino.credenciamentoportariaead AS matricula_unidadeensino_credenciamentoportariaead");
		fields.add("matricula_unidadeensino.veiculopublicacaocredenciamentoead AS matricula_unidadeensino_veiculopublicacaocredenciamentoead");
		fields.add("matricula_unidadeensino.secaopublicacaocredenciamentoead AS matricula_unidadeensino_secaopublicacaocredenciamentoead");
		fields.add("matricula_unidadeensino.paginapublicacaocredenciamentoead AS matricula_unidadeensino_paginapublicacaocredenciamentoead");
		fields.add("matricula_unidadeensino.numerodoucredenciamentoead AS matricula_unidadeensino_numerodoucredenciamentoead");
		fields.add("matricula_unidadeensino.tipoautorizacaoead AS matricula_unidadeensino_tipoautorizacaoead");
		fields.add("matricula_unidadeensino.numerorecredenciamentoead AS matricula_unidadeensino_numerorecredenciamentoead");
		fields.add("matricula_unidadeensino.datarecredenciamentoead AS matricula_unidadeensino_datarecredenciamentoead");
		fields.add("matricula_unidadeensino.datapublicacaorecredenciamentoead AS matricula_unidadeensino_datapublicacaorecredenciamentoead");
		fields.add("matricula_unidadeensino.veiculopublicacaorecredenciamentoead AS matricula_unidadeensino_veiculopublicacaorecredenciamentoead");
		fields.add("matricula_unidadeensino.secaopublicacaorecredenciamentoead AS matricula_unidadeensino_secaopublicacaorecredenciamentoead");
		fields.add("matricula_unidadeensino.paginapublicacaorecredenciamentoead AS matricula_unidadeensino_paginapublicacaorecredenciamentoead");
		fields.add("matricula_unidadeensino.numerodourecredenciamentoead AS matricula_unidadeensino_numerodourecredenciamentoead");
		fields.add("matricula_unidadeensino.tipoautorizacaorecredenciamentoead AS matricula_unidadeensino_tipoautorizacaorecredenciamentoead");
		fields.add("matricula_unidadeensino.numerorenovacaorecredenciamentoead AS matricula_unidadeensino_numerorenovacaorecredenciamentoead");
		fields.add("matricula_unidadeensino.datarenovacaorecredenciamentoead AS matricula_unidadeensino_datarenovacaorecredenciamentoead");
		fields.add("matricula_unidadeensino.datapublicacaorenovacaorecredenciamentoead AS mat_uni_datapublicacaorenovacaorecredenciamentoead");
		fields.add("matricula_unidadeensino.veiculopublicacaorenovacaorecredenciamentoead AS mat_uni_veiculopublicacaorenovacaorecredenciamentoead");
		fields.add("matricula_unidadeensino.secaopublicacaorenovacaorecredenciamentoead AS mat_uni_secaopublicacaorenovacaorecredenciamentoead");
		fields.add("matricula_unidadeensino.paginapublicacaorenovacaorecredenciamentoead AS mat_uni_paginapublicacaorenovacaorecredenciamentoead");
		fields.add("matricula_unidadeensino.numerodourenovacaorecredenciamentoead AS matricula_unidadeensino_numerodourenovacaorecredenciamentoead");
		fields.add("matricula_unidadeensino.tipoautorizacaorenovacaorecredenciamentoead AS mat_uni_tipoautorizacaorenovacaorecredenciamentoead");
//			TURNO
		fields.add("matricula_turno.codigo AS matricula_turno_codigo");
		fields.add("matricula_turno.duracaoaula AS matricula_turno_duracaoaula");
		fields.add("matricula_turno.nraulas AS matricula_turno_nraulas");
		fields.add("matricula_turno.nome AS matricula_turno_nome");
		fields.add("matricula_turno.tipohorario AS matricula_turno_tipohorario");
//			CURSO
		fields.add("matricula_curso.configuracaoacademico AS matricula_curso_configuracaoacademico");
		fields.add("matricula_curso.areaconhecimento AS matricula_curso_areaconhecimento");
		fields.add("matricula_curso.periodicidade AS matricula_curso_periodicidade");
		fields.add("matricula_curso.datacriacao AS matricula_curso_datacriacao");
		fields.add("matricula_curso.nrregistrointerno AS matricula_curso_nrregistrointerno");
		fields.add("matricula_curso.datapublicacaodo AS matricula_curso_datapublicacaodo");
		fields.add("matricula_curso.nrperiodoletivo AS matricula_curso_nrperiodoletivo");
		fields.add("matricula_curso.niveleducacional AS matricula_curso_niveleducacional");
		fields.add("matricula_curso.publicoalvo AS matricula_curso_publicoalvo");
		fields.add("matricula_curso.descricao AS matricula_curso_descricao");
		fields.add("matricula_curso.objetivos AS matricula_curso_objetivos");
		fields.add("matricula_curso.nome AS matricula_curso_nome");
		fields.add("matricula_curso.codigo AS matricula_curso_codigo");
		fields.add("matricula_curso.titulo AS matricula_curso_titulo");
		fields.add("matricula_curso.abreviatura AS matricula_curso_abreviatura");
		fields.add("matricula_curso.titulacaodoformando AS matricula_curso_titulacaodoformando");
		fields.add("matricula_curso.titulacaodoformandofeminino AS matricula_curso_titulacaodoformandofeminino");
		fields.add("matricula_curso.nomedocumentacao AS matricula_curso_nomedocumentacao");
		fields.add("matricula_curso.titulacaomasculinoapresentardiploma AS matricula_curso_titulacaomasculinoapresentardiploma");
		fields.add("matricula_curso.titulacaofemininoapresentardiploma AS matricula_curso_titulacaofemininoapresentardiploma");
		fields.add("matricula_curso.modalidadecurso AS matricula_curso_modalidadecurso");
		fields.add("matricula_curso.possuicodigoemec AS matricula_curso_possuicodigoemec");
		fields.add("matricula_curso.codigoemec AS matricula_curso_codigoemec");
		fields.add("matricula_curso.numeroprocessoemec AS matricula_curso_numeroprocessoemec");
		fields.add("matricula_curso.tipoprocessoemec AS matricula_curso_tipoprocessoemec");
		fields.add("matricula_curso.datacadastroemec AS matricula_curso_datacadastroemec");
		fields.add("matricula_curso.dataprotocoloemec AS matricula_curso_dataprotocoloemec");
		fields.add("matricula_curso.tipoautorizacaocursoenum AS matricula_curso_tipoautorizacaocursoenum");
		fields.add("matricula_curso.numeroautorizacao AS matricula_curso_numeroautorizacao");
		fields.add("matricula_curso.datacredenciamento AS matricula_curso_datacredenciamento");
		fields.add("matricula_curso.veiculopublicacao AS matricula_curso_veiculopublicacao");
		fields.add("matricula_curso.secaopublicacao AS matricula_curso_secaopublicacao");
		fields.add("matricula_curso.paginapublicacao AS matricula_curso_paginapublicacao");
		fields.add("matricula_curso.numerodou AS matricula_curso_numerodou");
		fields.add("matricula_curso.autorizacaoresolucaoemtramitacao AS matricula_curso_autorizacaoresolucaoemtramitacao");
		fields.add("matricula_curso.numeroprocessoautorizacaoresolucao AS matricula_curso_numeroprocessoautorizacaoresolucao");
		fields.add("matricula_curso.tipoprocessoautorizacaoresolucao AS matricula_curso_tipoprocessoautorizacaoresolucao");
		fields.add("matricula_curso.datacadastroautorizacaoresolucao AS matricula_curso_datacadastroautorizacaoresolucao");
		fields.add("matricula_curso.dataprotocoloautorizacaoresolucao AS matricula_curso_dataprotocoloautorizacaoresolucao");
		fields.add("matricula_curso.habilitacao AS matricula_curso_habilitacao");
		fields.add("matricula_curso.dataHabilitacao AS matricula_curso_datahabilitacao");
//			GRADE CURRICULAR
		fields.add("matricula_gradecurricular.curso AS matricula_gradecurricular_curso");
		fields.add("matricula_gradecurricular.situacao AS matricula_gradecurricular_situacao");
		fields.add("matricula_gradecurricular.datacadastro AS matricula_gradecurricular_datacadastro");
		fields.add("matricula_gradecurricular.nome AS matricula_gradecurricular_nome");
		fields.add("matricula_gradecurricular.codigo AS matricula_gradecurricular_codigo");
		fields.add("matricula_gradecurricular.cargahoraria AS matricula_gradecurricular_cargahoraria");
		fields.add("matricula_gradecurricular.creditos AS matricula_gradecurricular_creditos");
		fields.add("matricula_gradecurricular.totalcargahorariaatividadecomplementar AS mat_gra_totalcargahorariaatividadecomplementar");
		fields.add("matricula_gradecurricular.totalcargahorariaestagio AS matricula_gradecurricular_totalcargahorariaestagio");
//		fields.add("matricula_gradecurricular.considerardisciplinasforagradeintegralizacaochoptativa AS mat_gra_considerardisciplinasforagradeintegralizacaochoptativa");
//		fields.add("matricula_gradecurricular.modalidadematrizcurricular AS matricula_gradecurricular_modalidadematrizcurricular");
//		fields.add("matricula_gradecurricular.periodoletivo AS matricula_gradecurricular_periodoletivo");
//		fields.add("matricula_gradecurricular.totalhorasestagio AS matricula_gradecurricular_totalhorasestagio");
		return fields;
	}
	
	private List<String> getJoinConsultaMatriculaExpedicaoDiplomaCompleto() {
		List<String> joins = new ArrayList<>(0);
		joins.add("INNER JOIN matricula ON	matricula.matricula = expedicaodiploma.matricula");
		joins.add("INNER JOIN pessoa matricula_aluno ON matricula_aluno.codigo = matricula.aluno");
		joins.add("INNER JOIN unidadeensino matricula_unidadeensino ON matricula_unidadeensino.codigo = matricula.unidadeensino");
		joins.add("INNER JOIN curso matricula_curso ON matricula_curso.codigo = matricula.curso");
		joins.add("INNER JOIN gradecurricular matricula_gradecurricular ON matricula_gradecurricular.codigo = matricula.gradecurricularatual");
		joins.add("LEFT JOIN turno matricula_turno ON matricula_turno.codigo = matricula.turno");
		joins.add("LEFT JOIN autorizacaocurso matricula_autorizacaocurso ON matricula_autorizacaocurso.codigo = matricula.autorizacaocurso");
		joins.add("LEFT JOIN autorizacaocurso matricula_renovacaoreconhecimento ON matricula_renovacaoreconhecimento.codigo = matricula.renovacaoreconhecimento");
		joins.add("LEFT JOIN paiz matricula_aluno_nacionalidade ON matricula_aluno_nacionalidade.codigo = matricula_aluno.nacionalidade");
		joins.add("LEFT JOIN cidade matricula_aluno_naturalidade ON matricula_aluno_naturalidade.codigo = matricula_aluno.naturalidade");
		joins.add("LEFT JOIN estado matricula_aluno_naturalidade_estado ON matricula_aluno_naturalidade_estado.codigo = matricula_aluno_naturalidade.estado");
		return joins;
	}
	
	private List<String> getJoinConsultaExpedicaoDiplomaCompleto() {
		List<String> joins = new ArrayList<>(0);
		joins.add("LEFT JOIN unidadeensino unidadecertificadora ON unidadecertificadora.codigo = expedicaodiploma.unidadeensinocertificadora");
		joins.add("LEFT JOIN textopadraodeclaracao textopadrao ON textopadrao.codigo = expedicaodiploma.textopadrao");
		joins.add("LEFT JOIN funcionario reitorregistrodiplomaviaanterior ON reitorregistrodiplomaviaanterior.codigo = expedicaodiploma.reitorregistrodiplomaviaanterior");
		joins.add("LEFT JOIN funcionario secretariaregistrodiplomaviaanterior ON secretariaregistrodiplomaviaanterior.codigo = expedicaodiploma.secretariaregistrodiplomaviaanterior");
		joins.add("LEFT JOIN cargo cargoreitorregistrodiplomaviaanterior ON cargoreitorregistrodiplomaviaanterior.codigo = expedicaodiploma.cargoreitorregistrodiplomaviaanterior");
		joins.add("LEFT JOIN funcionario funcionarioprimario ON funcionarioprimario.codigo = expedicaodiploma.funcionarioprimario");
		joins.add("LEFT JOIN funcionario funcionariosecundario ON funcionariosecundario.codigo = expedicaodiploma.funcionariosecundario");
		joins.add("LEFT JOIN funcionario funcionarioterceiro ON funcionarioterceiro.codigo = expedicaodiploma.funcionarioterceiro");
		joins.add("LEFT JOIN funcionario funcionarioquarto ON funcionarioquarto.codigo = expedicaodiploma.funcionarioquarto");
		joins.add("LEFT JOIN funcionario funcionarioquinto ON funcionarioquinto.codigo = expedicaodiploma.funcionarioquinto");
		joins.add("LEFT JOIN cargo cargofuncionarioprincipal ON cargofuncionarioprincipal.codigo = expedicaodiploma.cargofuncionarioprincipal");
		joins.add("LEFT JOIN cargo cargofuncionariosecundario ON cargofuncionariosecundario.codigo = expedicaodiploma.cargofuncionariosecundario");
		joins.add("LEFT JOIN cargo cargofuncionarioterceiro ON cargofuncionarioterceiro.codigo = expedicaodiploma.cargofuncionarioterceiro");
		joins.add("LEFT JOIN cargo cargofuncionarioquarto ON cargofuncionarioquarto.codigo = expedicaodiploma.cargofuncionarioquarto");
		joins.add("LEFT JOIN cargo cargofuncionarioquinto ON cargofuncionarioquinto.codigo = expedicaodiploma.cargofuncionarioquinto");
		joins.add("LEFT JOIN usuario responsavelcadastro ON responsavelcadastro.codigo = expedicaodiploma.responsavelcadastro");
		joins.add("LEFT JOIN cidade cidadepta ON cidadepta.codigo = expedicaodiploma.cidadepta");
		joins.add("LEFT JOIN estado cidadepta_estado ON cidadepta_estado.codigo = cidadepta.estado");
		joins.add("LEFT JOIN pessoa reitorregistrodiplomaviaanterior_pessoa ON reitorregistrodiplomaviaanterior_pessoa.codigo = reitorregistrodiplomaviaanterior.pessoa");
		joins.add("LEFT JOIN pessoa secretariaregistrodiplomaviaanterior_pessoa ON secretariaregistrodiplomaviaanterior_pessoa.codigo = secretariaregistrodiplomaviaanterior.pessoa");
		joins.add("LEFT JOIN pessoa funcionarioprimario_pessoa ON funcionarioprimario_pessoa.codigo = funcionarioprimario.pessoa");
		joins.add("LEFT JOIN pessoa funcionariosecundario_pessoa ON funcionariosecundario_pessoa.codigo = funcionariosecundario.pessoa");
		joins.add("LEFT JOIN pessoa funcionarioterceiro_pessoa ON funcionarioterceiro_pessoa.codigo = funcionarioterceiro.pessoa");
		joins.add("LEFT JOIN pessoa funcionarioquarto_pessoa ON funcionarioquarto_pessoa.codigo = funcionarioquarto.pessoa");
		joins.add("LEFT JOIN pessoa funcionarioquinto_pessoa ON funcionarioquinto_pessoa.codigo = funcionarioquinto.pessoa");
		joins.add("LEFT JOIN arquivo funcionarioprimario_arquivoassinatura ON funcionarioprimario_arquivoassinatura.codigo = funcionarioprimario.arquivoassinatura");
		joins.add("LEFT JOIN arquivo funcionariosecundario_arquivoassinatura ON funcionariosecundario_arquivoassinatura.codigo = funcionariosecundario.arquivoassinatura");
		joins.add("LEFT JOIN arquivo funcionarioterceiro_arquivoassinatura ON funcionarioterceiro_arquivoassinatura.codigo = funcionarioterceiro.arquivoassinatura");
		joins.add("LEFT JOIN arquivo funcionarioquarto_arquivoassinatura ON funcionarioquarto_arquivoassinatura.codigo = funcionarioquarto.arquivoassinatura");
		joins.add("LEFT JOIN arquivo funcionarioquinto_arquivoassinatura ON funcionarioquinto_arquivoassinatura.codigo = funcionarioquinto.arquivoassinatura");
		joins.add("LEFT JOIN usuario responsavelanulacao ON responsavelanulacao.codigo = expedicaodiploma.responsavelanulacao");
		joins.add("LEFT JOIN pessoa responsavelanulacao_pessoa ON responsavelanulacao_pessoa.codigo = responsavelanulacao.pessoa");
		return joins;
	}
	
	private String montarDadosField(String classe, String field) {
		switch (classe) {
		case "expedicaoDiploma_":
			return field.substring(field.indexOf("expedicaoDiploma_"));
		case "matricula_":
			return field.substring(field.indexOf("matricula_"));
		case "matricula_aluno_":
			return field.substring(field.indexOf("matricula_aluno_"));
		case "matricula_curso_":
			return field.substring(field.indexOf("matricula_curso_"));
		case "matricula_unidadeensino_":
			return field.substring(field.indexOf("matricula_unidadeensino_"));
		case "textopadrao_":
			return field.substring(field.indexOf("textopadrao_"));
		case "reitorregistrodiplomaviaanterior_":
			return field.substring(field.indexOf("reitorregistrodiplomaviaanterior_"));
		case "reitorregistrodiplomaviaanterior_pessoa_":
			return field.substring(field.indexOf("reitorregistrodiplomaviaanterior_pessoa_"));
		case "cargoreitorregistrodiplomaviaanterior_":
			return field.substring(field.indexOf("cargoreitorregistrodiplomaviaanterior_"));
		case "secretariaregistrodiplomaviaanterior_":
			return field.substring(field.indexOf("secretariaregistrodiplomaviaanterior_"));
		case "secretariaregistrodiplomaviaanterior_pessoa_":
			return field.substring(field.indexOf("secretariaregistrodiplomaviaanterior_pessoa_"));
		case "unidadecertificadora_":
			return field.substring(field.indexOf("unidadecertificadora_"));
		case "funcionarioprimario_":
			return field.substring(field.indexOf("funcionarioprimario_"));
		case "funcionariosecundario_":
			return field.substring(field.indexOf("funcionariosecundario_"));
		case "funcionarioterceiro_":
			return field.substring(field.indexOf("funcionarioterceiro_"));
		case "funcionarioquarto_":
			return field.substring(field.indexOf("funcionarioquarto_"));
		case "funcionarioquinto_":
			return field.substring(field.indexOf("funcionarioquinto_"));
		case "funcionarioprimario_pessoa_":
			return field.substring(field.indexOf("funcionarioprimario_pessoa_"));
		case "funcionariosecundario_pessoa_":
			return field.substring(field.indexOf("funcionariosecundario_pessoa_"));
		case "funcionarioterceiro_pessoa_":
			return field.substring(field.indexOf("funcionarioterceiro_pessoa_"));
		case "funcionarioquarto_pessoa_":
			return field.substring(field.indexOf("funcionarioquarto_pessoa_"));
		case "funcionarioquinto_pessoa_":
			return field.substring(field.indexOf("funcionarioquinto_pessoa_"));
		case "cargofuncionarioprincipal_":
			return field.substring(field.indexOf("cargofuncionarioprincipal_"));
		case "cargofuncionariosecundario_":
			return field.substring(field.indexOf("cargofuncionariosecundario_"));
		case "cargofuncionarioterceiro_":
			return field.substring(field.indexOf("cargofuncionarioterceiro_"));
		case "cargofuncionarioquarto_":
			return field.substring(field.indexOf("cargofuncionarioquarto_"));
		case "cargofuncionarioquinto_":
			return field.substring(field.indexOf("cargofuncionarioquinto_"));
		case "funcionarioprimario_arquivoassinatura_":
			return field.substring(field.indexOf("funcionarioprimario_arquivoassinatura_"));
		case "funcionariosecundario_arquivoassinatura_":
			return field.substring(field.indexOf("funcionariosecundario_arquivoassinatura_"));
		case "funcionarioterceiro_arquivoassinatura_":
			return field.substring(field.indexOf("funcionarioterceiro_arquivoassinatura_"));
		case "funcionarioquarto_arquivoassinatura_":
			return field.substring(field.indexOf("funcionarioquarto_arquivoassinatura_"));
		case "funcionarioquinto_arquivoassinatura_":
			return field.substring(field.indexOf("funcionarioquinto_arquivoassinatura_"));
		case "responsavelcadastro_":
			return field.substring(field.indexOf("responsavelcadastro_"));
		case "cidadepta_":
			return field.substring(field.indexOf("cidadepta_"));
		case "matricula_autorizacaocurso_":
			return field.substring(field.indexOf("matricula_autorizacaocurso_"));
		case "matricula_renovacaoreconhecimento_":
			return field.substring(field.indexOf("matricula_renovacaoreconhecimento_"));
		case "matricula_turno_":
			return field.substring(field.indexOf("matricula_turno_"));
		case "matricula_gradecurricular_":
			return field.substring(field.indexOf("matricula_gradecurricular_"));
		case "cidadepta_estado_":
			return field.substring(field.indexOf("cidadepta_estado_"));
		case "matricula_aluno_nacionalidade_":
			return field.substring(field.indexOf("matricula_aluno_nacionalidade_"));
		case "matricula_aluno_naturalidade_":
			return field.substring(field.indexOf("matricula_aluno_naturalidade_"));
		case "matricula_aluno_naturalidade_estado_":
			return field.substring(field.indexOf("matricula_aluno_naturalidade_estado_"));
		case "mat_uni_":
			return field.substring(field.indexOf("mat_uni_"));
		case "mat_gra_":
			return field.substring(field.indexOf("mat_gra_"));
		case "personalizado_":
			return field.substring(field.indexOf("personalizado_"));
		case "responsavelanulacao_":
			return field.substring(field.indexOf("responsavelanulacao_"));
		case "responsavelanulacao_pessoa_":
			return field.substring(field.indexOf("responsavelanulacao_pessoa_"));
		}
		return Constantes.EMPTY;
	}
	
	@Override
	public ExpedicaoDiplomaVO carregarDadosCompletoExpedicaoDiploma(Integer codigoExpedicaoDiploma, UsuarioVO usuario) throws Exception {
		List<String> fields = new ArrayList<>(0);
		List<String> joins = new ArrayList<>(0);
		fields.addAll(getSqlConsultaExpedicaoDiplomaCompleto());
		fields.addAll(getSqlConsultaMatriculaExpedicaoDiplomaCompleto());
		fields.add("cargaHorariaCursada.cargaHorariaCumprido AS personalizado_cargaHorariaCursada");
		fields.add("cargaHorariaCursada.percentualIntegralizado AS personalizado_percentualIntegralizado");
		fields.add("matricula_gradecurricular.cargahoraria AS personalizado_cargaHorariaTotal");
		fields.add("EXISTS(SELECT expedicaoDiplomaSuperior.codigo FROM expedicaodiploma expedicaoDiplomaSuperior WHERE expedicaoDiplomaSuperior.matricula = expedicaodiploma.matricula AND expedicaoDiplomaSuperior.codigo != expedicaodiploma.codigo AND expedicaoDiplomaSuperior.via >= expedicaodiploma.via AND expedicaoDiplomaSuperior.codigo > expedicaodiploma.codigo) AS personalizado_existeexpedicaodiplomasuperior");
		joins.addAll(getJoinConsultaExpedicaoDiplomaCompleto());
		joins.addAll(getJoinConsultaMatriculaExpedicaoDiplomaCompleto());
		joins.add("LEFT JOIN LATERAL( SELECT((CASE WHEN t.cargaHorariaDisciplinaObrigatorioCumprida > t.cargaHorariaDisciplinaObrigatorioExigida THEN t.cargaHorariaDisciplinaObrigatorioExigida ELSE t.cargaHorariaDisciplinaObrigatorioCumprida END) + (CASE WHEN t.cargaHorariaDisciplinaOptativaExigida > 0 THEN CASE WHEN t.quantidadeMinimaDisciplinaOptativaExigida > 0 AND t.quantidadeDisciplinaOptativaCumprida = t.quantidadeMinimaDisciplinaOptativaExigida THEN t.cargaHorariaDisciplinaOptativaExigida ELSE t.cargaHorariaDisciplinaOptativaCumprida END ELSE 0 END) + (CASE WHEN t.cargaHorariaEstagioCumprido > t.cargaHorariaEstagioExigido THEN t.cargaHorariaEstagioExigido ELSE t.cargaHorariaEstagioCumprido END) + (CASE WHEN t.cargaHorariaAtividadeComplementarCumprido > t.cargaHorariaAtividadeComplementarExigido THEN t.cargaHorariaAtividadeComplementarExigido ELSE t.cargaHorariaAtividadeComplementarCumprido END ))::INT AS cargaHorariaCumprido, ((((CASE WHEN t.cargaHorariaDisciplinaObrigatorioCumprida > t.cargaHorariaDisciplinaObrigatorioExigida THEN t.cargaHorariaDisciplinaObrigatorioExigida ELSE t.cargaHorariaDisciplinaObrigatorioCumprida END ) + (CASE WHEN t.cargaHorariaDisciplinaOptativaExigida > 0 THEN CASE WHEN t.quantidadeMinimaDisciplinaOptativaExigida > 0 AND t.quantidadeDisciplinaOptativaCumprida = t.quantidadeMinimaDisciplinaOptativaExigida THEN t.cargaHorariaDisciplinaOptativaExigida ELSE t.cargaHorariaDisciplinaOptativaCumprida END ELSE 0 END) + (CASE WHEN t.cargaHorariaEstagioCumprido > t.cargaHorariaEstagioExigido THEN t.cargaHorariaEstagioExigido ELSE t.cargaHorariaEstagioCumprido END ) + (CASE WHEN t.cargaHorariaAtividadeComplementarCumprido > t.cargaHorariaAtividadeComplementarExigido THEN t.cargaHorariaAtividadeComplementarExigido ELSE t.cargaHorariaAtividadeComplementarCumprido END ))::NUMERIC(20, 2) * 100) / t.cargaHorariaTotal)::NUMERIC(20, 2) AS percentualIntegralizado FROM ( SELECT configuracaodiplomadigital.considerarcargahorariaforagrade, gradecurricularatual.cargahoraria AS cargaHorariaTotal, ( SELECT sum(gd.cargahoraria) FROM gradedisciplina AS gd INNER JOIN periodoletivo pl ON pl.codigo = gd.periodoletivo WHERE pl.gradecurricular = matricula.gradecurricularatual AND gd.tipodisciplina NOT IN ('OP', 'LO')) AS cargaHorariaDisciplinaObrigatorioExigida, ( SELECT sum(gd.cargahoraria) FROM gradedisciplina AS gd INNER JOIN periodoletivo pl ON pl.codigo = gd.periodoletivo WHERE pl.gradecurricular = matricula.gradecurricularatual AND gd.tipodisciplina NOT IN ('OP', 'LO') AND EXISTS ( SELECT his.codigo FROM historico his WHERE his.matricula = matricula.matricula AND his.matrizcurricular = matricula.gradecurricularatual AND his.gradedisciplina = gd.codigo AND his.situacao IN ('AA', 'AP', 'IS', 'CC', 'CH', 'AE'))) AS cargaHorariaDisciplinaObrigatorioCumprida, (gradecurricularatual.cargahoraria - ( SELECT sum(gd.cargahoraria) FROM gradedisciplina AS gd INNER JOIN periodoletivo pl ON pl.codigo = gd.periodoletivo WHERE pl.gradecurricular = matricula.gradecurricularatual AND gd.tipodisciplina NOT IN ('OP', 'LO')) - CASE WHEN gradecurricularatual.totalcargahorariaestagio IS NULL THEN 0 ELSE gradecurricularatual.totalcargahorariaestagio END - CASE WHEN totalcargahorariaatividadecomplementar IS NULL THEN 0 ELSE totalcargahorariaatividadecomplementar END ) AS cargaHorariaDisciplinaOptativaExigida, CASE WHEN gradecurricularatual.quantidadedisciplinasoptativasmatrizcurricular > 0 THEN gradecurricularatual.quantidadedisciplinasoptativasmatrizcurricular ELSE curso.quantidadedisciplinasoptativasexpedicaodiploma END AS quantidadeMinimaDisciplinaOptativaExigida, ( SELECT CASE WHEN sum(cargahoraria) IS NULL THEN 0 ELSE sum(cargahoraria) END FROM ( SELECT gd.cargahoraria FROM historico his INNER JOIN gradedisciplina gd ON gd.codigo = his.gradedisciplina INNER JOIN periodoletivo pl ON pl.codigo = gd.periodoletivo WHERE his.matricula = matricula.matricula AND his.matrizcurricular = matricula.gradecurricularatual AND pl.gradecurricular = matricula.gradecurricularatual AND gd.tipodisciplina IN ('OP', 'LO') AND his.situacao IN ('AA', 'AP', 'IS', 'CC', 'CH', 'AE') UNION ALL SELECT gcgod.cargahoraria FROM historico his INNER JOIN gradecurriculargrupooptativadisciplina gcgod ON gcgod.codigo = his.gradecurriculargrupooptativadisciplina INNER JOIN gradecurriculargrupooptativa AS gcgo ON gcgod.gradecurriculargrupooptativa = gcgo.codigo WHERE his.matricula = matricula.matricula AND his.matrizcurricular = matricula.gradecurricularatual AND gcgo.gradecurricular = matricula.gradecurricularatual AND his.situacao IN ('AA', 'AP', 'IS', 'CC', 'CH', 'AE') UNION ALL SELECT his.cargahorariadisciplina FROM historico his INNER JOIN gradecurricular ON gradecurricular.codigo = his.matrizcurricular WHERE his.matricula = matricula.matricula AND his.matrizcurricular = matricula.gradecurricularatual AND his.situacao IN ('AA', 'AP', 'IS', 'CC', 'CH', 'AE') AND historicodisciplinaforagrade AND (configuracaodiplomadigital.considerarcargahorariaforagrade) ) AS t) AS cargaHorariaDisciplinaOptativaCumprida, ( SELECT count(DISTINCT disciplina) FROM ( SELECT his.disciplina FROM historico his INNER JOIN gradedisciplina gd ON gd.codigo = his.gradedisciplina INNER JOIN periodoletivo pl ON pl.codigo = gd.periodoletivo WHERE his.matricula = matricula.matricula AND his.matrizcurricular = matricula.gradecurricularatual AND pl.gradecurricular = matricula.gradecurricularatual AND gd.tipodisciplina IN ('OP', 'LO') AND his.situacao IN ('AA', 'AP', 'IS', 'CC', 'CH', 'AE') UNION ALL SELECT his.disciplina FROM historico his INNER JOIN gradecurriculargrupooptativadisciplina gcgod ON gcgod.codigo = his.gradecurriculargrupooptativadisciplina INNER JOIN gradecurriculargrupooptativa AS gcgo ON gcgod.gradecurriculargrupooptativa = gcgo.codigo WHERE his.matricula = matricula.matricula AND his.matrizcurricular = matricula.gradecurricularatual AND gcgo.gradecurricular = matricula.gradecurricularatual AND his.situacao IN ('AA', 'AP', 'IS', 'CC', 'CH', 'AE') UNION ALL SELECT his.disciplina FROM historico his INNER JOIN gradecurricular ON gradecurricular.codigo = his.matrizcurricular WHERE his.matricula = matricula.matricula AND his.matrizcurricular = matricula.gradecurricularatual AND his.situacao IN ('AA', 'AP', 'IS', 'CC', 'CH', 'AE') AND historicodisciplinaforagrade AND (configuracaodiplomadigital.considerarcargahorariaforagrade) ) AS t) AS quantidadeDisciplinaOptativaCumprida, COALESCE(gradecurricularatual.totalcargahorariaestagio, 0) AS cargaHorariaEstagioExigido, COALESCE(( (SELECT sum(est.cargaHoraria) AS cargaHoraria FROM estagio est WHERE est.matricula = matricula.matricula ) ), 0) AS cargaHorariaEstagioCumprido, COALESCE(gradecurricularatual.totalcargahorariaatividadecomplementar, 0) AS cargaHorariaAtividadeComplementarExigido, COALESCE((SELECT CASE WHEN cargahorariaconsiderada > cargahorariaexigida THEN cargahorariaexigida ELSE cargahorariaconsiderada END cargaHorariaAtividadeComplementar FROM (SELECT DISTINCT gradecurricularatual.totalCargaHorariaAtividadeComplementar AS cargahorariaexigida, (SELECT sum (CASE WHEN cargaHorariaRealizadaAtividadeComplementar > cargahoraria THEN cargahoraria ELSE cargaHorariaRealizadaAtividadeComplementar END ) AS cargaHorariaRealizadaAtividadeComplementar FROM ( SELECT sum(cargahorariaconsiderada) AS cargaHorariaRealizadaAtividadeComplementar, registroatividadecomplementarmatricula.tipoAtividadeComplementar, gradecurriculartipoatividadecomplementar.cargahoraria FROM registroatividadecomplementarmatricula INNER JOIN gradecurriculartipoatividadecomplementar ON gradecurriculartipoatividadecomplementar.tipoAtividadeComplementar = registroatividadecomplementarmatricula.tipoAtividadeComplementar WHERE registroatividadecomplementarmatricula.matricula = matricula.matricula AND gradecurriculartipoatividadecomplementar.gradecurricular = gradecurricularatual.codigo AND registroatividadecomplementarmatricula.situacaoAtividadeComplementarMatricula = 'DEFERIDO' GROUP BY registroatividadecomplementarmatricula.tipoAtividadeComplementar, gradecurriculartipoatividadecomplementar.cargahoraria ) AS t) AS cargahorariaconsiderada ) AS t), 0) AS cargaHorariaAtividadeComplementarCumprido FROM matricula INNER JOIN curso ON curso.codigo = matricula.curso INNER JOIN gradecurricular gradecurricularatual ON gradecurricularatual.codigo = matricula.gradecurricularatual LEFT JOIN configuracaodiplomadigital ON configuracaodiplomadigital.codigo = ( SELECT cdd.codigo FROM configuracaodiplomadigital cdd LEFT JOIN unidadeensino unidadeensinodiploma ON unidadeensinodiploma.configuracaodiplomadigital = cdd.codigo AND unidadeensinodiploma.codigo = matricula.unidadeensino WHERE (unidadeensinodiploma.codigo = matricula.unidadeensino AND unidadeensinodiploma.configuracaodiplomadigital IS NOT NULL) OR cdd.padrao ORDER BY CASE WHEN unidadeensinodiploma.codigo IS NOT NULL THEN 0 ELSE 1 END LIMIT 1 ) WHERE matricula.matricula = expedicaodiploma.matricula ) AS t ) AS cargaHorariaCursada ON 1 = 1");
		ExpedicaoDiplomaVO expedicaoDiploma = consultarUnicaExpedicaoDiplomaGenerico(fields, Arrays.asList("expedicaodiploma.codigo = ?"), joins, Arrays.asList(codigoExpedicaoDiploma), null, Boolean.TRUE);
		if (Uteis.isAtributoPreenchido(expedicaoDiploma)) {
			expedicaoDiploma.setNovoObj(Boolean.FALSE);
			if (expedicaoDiploma.getEmitidoPorDecisaoJudicial()) {
				expedicaoDiploma.setDeclaracaoAcercaProcessoJudicialVOs(getFacadeFactory().getDeclaracaoAcercaProcessoJudicialInterfaceFacade().consultar(expedicaoDiploma.getCodigo()));
			}
			if (Uteis.isAtributoPreenchido(expedicaoDiploma.getMatricula().getAluno())) {
				expedicaoDiploma.getMatricula().getAluno().setFiliacaoVOs(getFacadeFactory().getFiliacaoFacade().consultarFiliacaos(expedicaoDiploma.getMatricula().getAluno().getCodigo(), Boolean.FALSE, usuario));
			}
			expedicaoDiploma.setObservacaoComplementarDiplomaVOs(getFacadeFactory().getObservacaoComplementarDiplomaFacade().consultarPorExpedicaoDiploma(expedicaoDiploma.getCodigo(), false, usuario));
			if (Uteis.isAtributoPreenchido(expedicaoDiploma.getFieldsPersonalizados())) {
				for (Entry<String, Object> field : expedicaoDiploma.getFieldsPersonalizados().entrySet()) {
					if ((field.getKey() != null && !field.getKey().trim().isEmpty())) {
						switch (field.getKey()) {
						case "personalizado_cargaHorariaCursada":
							if (field.getValue() != null) {
								expedicaoDiploma.setCargaHorariaCursada((Integer) field.getValue());
							}
							break;
						case "personalizado_percentualIntegralizado":
							if (field.getValue() != null) {
								expedicaoDiploma.setPercentualCHIntegralizacaoMatricula(((BigDecimal) field.getValue()).toString());
							}
							break;
						case "personalizado_cargaHorariaTotal":
							if (field.getValue() != null) {
								expedicaoDiploma.setCargaHorariaTotal((Integer) field.getValue());
							}
							break;
						case "personalizado_existeexpedicaodiplomasuperior":
							if (field.getValue() != null) {
								expedicaoDiploma.setExisteExpedicaoDiplomaSuperior((Boolean) field.getValue());
							}
							break;
						}
					}
				}
			}
		}
		return expedicaoDiploma;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarGeracaoHistoricoDigital(SuperParametroRelVO superParametroRelVO, SuperControleRelatorio superControleRelatorio, ExpedicaoDiplomaVO expedicaoDiplomaVO, String tipoLayout, Boolean layoutPersonalizado, Boolean assinarDigitalmente, ConfiguracaoHistoricoVO configuracaoHistoricoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO = carregarDadosLayoutHistorico(tipoLayout, configuracaoHistoricoVO, usuarioVO);
		superParametroRelVO.getParametros().clear();
		superParametroRelVO.setTituloRelatorio("HISTÓRICO ESCOLAR");
		superControleRelatorio.setFazerDownload(Boolean.FALSE);
		ConfiguracaoGEDVO configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigo(), usuarioVO);
		String tipoLayoutComparar = !layoutPersonalizado ? tipoLayout : configuracaoLayoutHistoricoVO.getChave().trim().isEmpty() || configuracaoLayoutHistoricoVO.getChave().equals("0") ? configuracaoLayoutHistoricoVO.getCodigo() + Constantes.EMPTY : configuracaoLayoutHistoricoVO.getChave();
		if (tipoLayoutComparar.equals("HistoricoAlunoEnsinoMedioLayout2Rel") || (tipoLayoutComparar.equals("HistoricoAlunoEnsinoMedioLayout3Rel") && !expedicaoDiplomaVO.getMatricula().getCurso().getNivelEducacional().equals("BA")) || tipoLayoutComparar.equals("HistoricoAlunoEnsinoMedio")) {
			superParametroRelVO.setTituloRelatorio("HISTÓRICO ESCOLAR - ENSINO MÉDIO");
		} else if (tipoLayoutComparar.equals("HistoricoAlunoEnsinoBasicoLayout2Rel") || (tipoLayoutComparar.equals("HistoricoAlunoEnsinoMedioLayout3Rel") && expedicaoDiplomaVO.getMatricula().getCurso().getNivelEducacional().equals("BA"))) {
			superParametroRelVO.setTituloRelatorio("HISTÓRICO ESCOLAR - ENSINO FUNDAMENTAL");
		} else if (tipoLayoutComparar.equals("HistoricoAlunoNivelTecnicoRel") || tipoLayoutComparar.equals("HistoricoAlunoNivelTecnico2Rel")) {
			superParametroRelVO.setTituloRelatorio("HISTÓRICO ESCOLAR - NÍVEL TÉCNICO");
		} else if (tipoLayoutComparar.equals("HistoricoAlunoLayout4Rel") || tipoLayoutComparar.equals("HistoricoAlunoLayout13Rel") || tipoLayoutComparar.equals("HistoricoAlunoLayout12Rel") || tipoLayoutComparar.equals("HistoricoAlunoLayout14Rel") || tipoLayoutComparar.equals("HistoricoAlunoLayout15Rel") || tipoLayoutComparar.equals("HistoricoAlunoLayout15PortariaMECRel") || tipoLayoutComparar.equals("HistoricoAlunoLayout4PortariaMECRel") || tipoLayoutComparar.equals("HistoricoAlunoLayout21PortariaMECRel") || tipoLayoutComparar.equals("HistoricoAlunoLayout22PortariaMECRel") || tipoLayoutComparar.equals("HistoricoAlunoLayout24Graduacao")) {
			superParametroRelVO.setTituloRelatorio("HISTÓRICO ACADÊMICO");
		} else if (tipoLayoutComparar.equals("HistoricoAlunoResidenciaMedicaRel")) {
			superParametroRelVO.setTituloRelatorio("PROGRAMA DE RESIDÊNCIA MÉDICA");
		} else {
			superParametroRelVO.setTituloRelatorio("HISTÓRICO ESCOLAR");
		}
		if (!tipoLayoutComparar.equals("HistoricoAlunoLayout15PortariaMECRel")) {
			superParametroRelVO.setNomeEmpresa(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getNome());
		}
		String design = Constantes.EMPTY;
		montarConfiguracaoDiploma(expedicaoDiplomaVO, usuarioVO);
		montarDadosCoordenadorCursoDiploma(expedicaoDiplomaVO, usuarioVO);
		FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO = new FiltroRelatorioAcademicoVO();
		filtroRelatorioAcademicoVO.realizarMarcarSituacoesConfiguracaoDiploma(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital());
		getFacadeFactory().getHistoricoAlunoRelFacade().validarDados(expedicaoDiplomaVO.getMatricula(), null, "aluno", null, expedicaoDiplomaVO.getUnidadeEnsinoCertificadora());
		HistoricoAlunoRelVO histAlunoRelVO = new HistoricoAlunoRelVO();
		HistoricoAlunoRelVO historicoTemp = null;
		histAlunoRelVO.setFuncionarioPrincipalVO(expedicaoDiplomaVO.getFuncionarioPrimarioVO());
		histAlunoRelVO.setFuncionarioSecundarioVO(expedicaoDiplomaVO.getFuncionarioSecundarioVO());
		histAlunoRelVO.setFuncionarioTerciarioVO(expedicaoDiplomaVO.getFuncionarioTerceiroVO());
		histAlunoRelVO.setCargoFuncionarioPrincipal(expedicaoDiplomaVO.getCargoFuncionarioPrincipalVO());
		histAlunoRelVO.setCargoFuncionarioSecundario(expedicaoDiplomaVO.getCargoFuncionarioSecundarioVO());
		histAlunoRelVO.setCargoFuncionarioTerciario(expedicaoDiplomaVO.getCargoFuncionarioTerceiroVO());
		histAlunoRelVO.setTituloFuncionarioPrincipal(expedicaoDiplomaVO.getTituloFuncionarioPrincipal());
		histAlunoRelVO.setTituloFuncionarioSecundario(expedicaoDiplomaVO.getTituloFuncionarioSecundario());
		histAlunoRelVO.setTituloFuncionarioTerciario(expedicaoDiplomaVO.getTituloFuncionarioTerceiro());
		MatriculaVO matricula = (MatriculaVO) expedicaoDiplomaVO.getMatricula().clone();
		getFacadeFactory().getHistoricoAlunoRelFacade().validarDados(expedicaoDiplomaVO.getMatricula(), null, "aluno", null, expedicaoDiplomaVO.getUnidadeEnsinoCertificadora());
		getFacadeFactory().getMatriculaFacade().carregarDados(matricula, NivelMontarDados.TODOS, usuarioVO);
		getFacadeFactory().getHistoricoAlunoRelFacade().setDescricaoFiltros(Constantes.EMPTY);
		if (Uteis.getIsValorNumerico(tipoLayout)) {
			if (configuracaoLayoutHistoricoVO.getPastaBaseArquivoPdfPrincipal() == null) {
				throw new Exception("Não foi possível encontrar o modelo do layout principal selecionado (" + tipoLayout + ").");
			}
			design = configuracaoLayoutHistoricoVO.getPastaBaseArquivoPdfPrincipal() + File.separator + configuracaoLayoutHistoricoVO.getNomeArquivoPdfPrincipal();
		} else {
			design = HistoricoAlunoRel.getDesignIReportRelatorio(TipoNivelEducacional.getEnum(matricula.getCurso().getNivelEducacional()), tipoLayout, Boolean.FALSE);
		}
		histAlunoRelVO.setTrazerTodosProfessoresDisciplinas(Boolean.TRUE);
		historicoTemp = getFacadeFactory().getHistoricoAlunoRelFacade().criarObjeto(histAlunoRelVO, matricula, expedicaoDiplomaVO.getGradeCurricularVO(), filtroRelatorioAcademicoVO, 1, Constantes.EMPTY, Constantes.EMPTY, Constantes.EMPTY, tipoLayoutComparar, false, expedicaoDiplomaVO.getDataExpedicao(), false, false, false, usuarioVO, false, false, false, "", false, false, false, "PROFESSOR_APROVEITAMENTO_DISCIPLINA", null, null);
		historicoTemp.setApresentarFrequencia(Boolean.TRUE);
		for (HistoricoAlunoDisciplinaRelVO histAlunoDiscRelVO : historicoTemp.getListaHistoricoAlunoDisciplinaRelVOs()) {
			if (Uteis.isAtributoPreenchido(histAlunoDiscRelVO)) {
				histAlunoDiscRelVO.setApresentarFrequencia(Boolean.TRUE);
			}
		}
		histAlunoRelVO.setApresentarTopoRelatorio(Boolean.TRUE);
		if (Uteis.isAtributoPreenchido(historicoTemp.getListaHistoricoAlunoDisciplinaRelVOs())) {
			historicoTemp.setLegendaSituacaoHistorico(getFacadeFactory().getHistoricoAlunoRelFacade().realizarCriacaoLegendaSituacaoDisciplinaHistorico(historicoTemp, tipoLayoutComparar));
			if (getFacadeFactory().getHistoricoAlunoRelFacade().validarTipoLayoutGraduacao(tipoLayoutComparar, configuracaoLayoutHistoricoVO)) {
				historicoTemp.setLegendaTitulacaoProfessor((getFacadeFactory().getHistoricoAlunoRelFacade().realizarCriacaoLegendaTitulacaoProfessorDisciplinaHistorico(historicoTemp, tipoLayoutComparar)));
			}
		}
		Date dataEmissaoHistorico = expedicaoDiplomaVO.getMatricula().getDataEmissaoHistorico();
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora())) {
			String cidade = Constantes.EMPTY;
			CidadeVO cidadeVO = getFacadeFactory().getCidadeFacade().consultarCidadePorUnidadeEnsino(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigo(), false, 0, usuarioVO);
			if (cidadeVO != null && cidadeVO.getCodigo() != 0) {
				cidade = cidadeVO.getNome();
			} else {
				cidade = Constantes.EMPTY;
			}
			if (cidade.equals(Constantes.EMPTY)) {
				cidade = matricula.getUnidadeEnsino().getCidade().getNome();
			}
			historicoTemp.setDataEmissaoHistorico(tipoLayoutComparar.equals("HistoricoAlunoLayout14Rel") ? Uteis.getDataCidadeDiaMesPorExtensoEAno(Constantes.EMPTY, dataEmissaoHistorico, true) : Uteis.getDataCidadeDiaMesPorExtensoEAno(cidade, dataEmissaoHistorico, true));
			historicoTemp.setDataEmissaoHistorico(tipoLayoutComparar.equals("HistoricoAlunoLayout18PortariaMECRel") ? Uteis.getDataCidadeEstadoDiaMesPorExtensoEAno(cidade, cidadeVO.getEstado().getSigla(), dataEmissaoHistorico, true) : Uteis.getDataCidadeDiaMesPorExtensoEAno(cidade, dataEmissaoHistorico, true));
			if (tipoLayout.equals("HistoricoAlunoPos2Rel")) {
				GradeCurricularVO grade = getFacadeFactory().getGradeCurricularFacade().consultarGradeCurricularAtualMatricula(matricula.getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
				superParametroRelVO.adicionarParametro("reconhecimento", historicoTemp.getReconhecimento());
				superParametroRelVO.adicionarParametro("emisaoHistorico", historicoTemp.getDataEmissaoHistorico());
				superParametroRelVO.adicionarParametro("cargaHorariaCurso", grade.getCargaHoraria());
			}
		} else {
			historicoTemp.setDataEmissaoHistorico(tipoLayoutComparar.equals("HistoricoAlunoLayout14Rel") ? Uteis.getDataCidadeDiaMesPorExtensoEAno(Constantes.EMPTY, dataEmissaoHistorico, true) : Uteis.getDataCidadeDiaMesPorExtensoEAno(matricula.getUnidadeEnsino().getCidade().getNome(), dataEmissaoHistorico, true));
		}
		String enderecoUnidadeEnsino = null;
		if (tipoLayoutComparar.equals("HistoricoAlunoLayout11Rel")) {
			enderecoUnidadeEnsino = matricula.getUnidadeEnsino().getEnderecoCompleto() + " " + matricula.getUnidadeEnsino().getCidade().getNome() + " - " + matricula.getUnidadeEnsino().getCidade().getEstado().getSigla();
		} else {
			enderecoUnidadeEnsino = matricula.getUnidadeEnsino().getEnderecoCompleto();
		}
		superParametroRelVO.adicionarParametro("enderecoUnidadeEnsino", enderecoUnidadeEnsino);
		if (getFacadeFactory().getHistoricoAlunoRelFacade().validarTipoLayoutGraduacao(tipoLayoutComparar, configuracaoLayoutHistoricoVO)) {
			Matricula.montarDadosUnidadeEnsino(matricula, NivelMontarDados.TODOS);
			String cnpjEmpresa = "CNPJ: ";
			historicoTemp.setNomeUnidadeEnsino(matricula.getUnidadeEnsino().getNome());
			historicoTemp.setCodigoIES(matricula.getUnidadeEnsino().getCodigoIES());
			historicoTemp.setCodigoIESMantenedora(matricula.getUnidadeEnsino().getCodigoIESMantenedora());
			historicoTemp.setMantenedora(matricula.getUnidadeEnsino().getMantenedora());
			historicoTemp.setCnpj(matricula.getUnidadeEnsino().getCNPJ());
			if (Uteis.isAtributoPreenchido(historicoTemp.getMantenedora())) {
				cnpjEmpresa += historicoTemp.getMantenedora();
			} else {
				cnpjEmpresa += historicoTemp.getCnpj();
			}
			superParametroRelVO.adicionarParametro("cnpjEmpresa", cnpjEmpresa);
		}
		TitulacaoCursoVO tc = getFacadeFactory().getTitulacaoCursoFacade().consultarPorCodigoCursoUnico(matricula.getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuarioVO);
		if (Uteis.isAtributoPreenchido(tc)) {
			for (ItemTitulacaoCursoVO itc : tc.getItemTitulacaoCursoVOs()) {
				Map<String, List<HistoricoAlunoDisciplinaRelVO>> mapaProfessoresTitulares = historicoTemp.getListaHistoricoAlunoDisciplinaRelVOs().stream().collect(Collectors.groupingBy(HistoricoAlunoDisciplinaRelVO::getProfessor));
				Long qtdProfessor = mapaProfessoresTitulares.entrySet().stream().count();
				if (Uteis.isAtributoPreenchido(qtdProfessor)) {
					Long qtdProfessorDoutorOuMestre = mapaProfessoresTitulares.entrySet().stream().filter(p -> p.getValue().get(0).getTitulacaoProfessor().equals(itc.getTitulacao()) || p.getValue().get(0).getTitulacaoProfessor().equals(itc.getSegundaTitulacao()) || p.getValue().get(0).getSiglaTitulacaoProfessor().equals(itc.getTitulacao()) || p.getValue().get(0).getSiglaTitulacaoProfessor().equals(itc.getSegundaTitulacao()) || ((itc.getTitulacao().equals("DR") || itc.getSegundaTitulacao().equals("DR")) && p.getValue().get(0).getTitulacaoProfessor().equals("DRA")) || ((itc.getTitulacao().equals("DR") || itc.getSegundaTitulacao().equals("DR")) && p.getValue().get(0).getSiglaTitulacaoProfessor().equalsIgnoreCase("DRA")) || ((itc.getTitulacao().equals("GR") || itc.getSegundaTitulacao().equals("GR")) && p.getValue().get(0).getTitulacaoProfessor().equals("GRA"))
							|| ((itc.getTitulacao().equals("MS") || itc.getSegundaTitulacao().equals("MS")) && p.getValue().get(0).getSiglaTitulacaoProfessor().equalsIgnoreCase("ME")) || ((itc.getTitulacao().equals("EP") || itc.getSegundaTitulacao().equals("EP")) && p.getValue().get(0).getTitulacaoProfessor().equals("ESP"))).count();
					Double porcentagemProfessoresTitulacao = Uteis.arrendondarForcando2CadasDecimais(((qtdProfessorDoutorOuMestre * 100) / qtdProfessor));
					if (itc.getQuantidade() > porcentagemProfessoresTitulacao) {
						throw new Exception("A Titulação no histórico do aluno tem apenas " + Uteis.arrendondarForcando2CadasDecimaisStrComSepador(porcentagemProfessoresTitulacao, ",") + "% de " + itc.getTitulacao_Apresentar() + ". Não respeitando a regra definida na Titulação de Curso de " + Uteis.arrendondarForcando2CadasDecimaisStrComSepador(itc.getQuantidade(), ",") + "%.");
					}
				}
			}
		}
		if ((tipoLayoutComparar.equals("HistoricoAlunoLayout10Rel") || (Uteis.isAtributoPreenchido(configuracaoLayoutHistoricoVO) && (configuracaoLayoutHistoricoVO.getConfiguracaoHistoricoVO().getNivelEducacional().equals(TipoNivelEducacional.SUPERIOR) || configuracaoLayoutHistoricoVO.getConfiguracaoHistoricoVO().getNivelEducacional().equals(TipoNivelEducacional.GRADUACAO_TECNOLOGICA)))) && Boolean.TRUE) {
			int indexPenultimoSemestre = 0;
			matricula.getUltimoMatriculaPeriodoVO();
			if (matricula.getMatriculaPeriodoVOs().size() >= 2) {
				indexPenultimoSemestre = matricula.getMatriculaPeriodoVOs().size() - 2;
				MatriculaPeriodoVO penultimoMatriculaPeriodoVO = matricula.getMatriculaPeriodoVOs().get(indexPenultimoSemestre);
				superParametroRelVO.adicionarParametro("ultimoPeriodo", penultimoMatriculaPeriodoVO.getAno() + "/" + penultimoMatriculaPeriodoVO.getSemestre());
				superParametroRelVO.adicionarParametro("mediaNotasUltimoSemestre", getFacadeFactory().getHistoricoAlunoRelFacade().calcularMediaNotasSemestreAluno(historicoTemp.getListaHistoricoAlunoDisciplinaRelVOs(), penultimoMatriculaPeriodoVO));
			} else {
				superParametroRelVO.adicionarParametro("ultimoPeriodo", Constantes.EMPTY);
				superParametroRelVO.adicionarParametro("mediaNotasUltimoSemestre", 0);
			}
		} else {
			superParametroRelVO.adicionarParametro("ultimoPeriodo", Constantes.EMPTY);
			superParametroRelVO.adicionarParametro("mediaNotasUltimoSemestre", 0);
		}
		if (tipoLayoutComparar.equals("HistoricoAlunoPos3Rel") || tipoLayoutComparar.equals("HistoricoAlunoPos17Rel") || tipoLayoutComparar.equals("HistoricoAlunoPos3PercentualIntegralizacaoRel") || (Uteis.isAtributoPreenchido(configuracaoLayoutHistoricoVO) && configuracaoLayoutHistoricoVO.getConfiguracaoHistoricoVO().getNivelEducacional().equals(TipoNivelEducacional.POS_GRADUACAO))) {
			String turmaUltimoMatriculaPeriodo = getFacadeFactory().getTurmaFacade().consultaRapidaPorMatriculaUltimaMatriculaPeriodo(matricula.getMatricula(), usuarioVO).getIdentificadorTurma();
			superParametroRelVO.adicionarParametro("turmaUltimoMatriculaPeriodo", turmaUltimoMatriculaPeriodo);
		}
		Integer totalizadorCreditoDisciplina = 0;
		Integer totalizadorCreditoEstagio = 0;
		Integer totalizadorCreditoObrigatorioEstagio = 0;
		if (tipoLayoutComparar.equals("HistoricoAlunoLayout15Rel") || tipoLayoutComparar.equals("HistoricoAlunoLayout15PortariaMECRel")) {
			for (HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO : historicoTemp.getListaHistoricoAlunoDisciplinaRelVOs()) {
				if (!historicoAlunoDisciplinaRelVO.getDisciplinaEstagio()) {
					if (Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getCrDisciplina())) {
						totalizadorCreditoDisciplina += Integer.parseInt(historicoAlunoDisciplinaRelVO.getCrDisciplina());
					}
				} else {
					if (Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getCrDisciplina())) {
						totalizadorCreditoEstagio += Integer.parseInt(historicoAlunoDisciplinaRelVO.getCrDisciplina());
					}
					totalizadorCreditoObrigatorioEstagio += historicoAlunoDisciplinaRelVO.getNrCreditos();
				}
			}
		}
		superParametroRelVO.setNomeDesignIreport(design);
		if (Uteis.getIsValorNumerico(tipoLayout)) {
			superParametroRelVO.setSubReport_Dir(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + configuracaoLayoutHistoricoVO.getPastaBaseArquivoPdfPrincipal() + File.separator);
			superParametroRelVO.setCaminhoBaseRelatorio(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + configuracaoLayoutHistoricoVO.getPastaBaseArquivoPdfPrincipal() + File.separator);
			superParametroRelVO.adicionarParametro("configuracaoLayoutHistoricoVO", configuracaoLayoutHistoricoVO);
		} else {
			superParametroRelVO.setSubReport_Dir(HistoricoAlunoRel.getCaminhoBaseRelatorio());
			superParametroRelVO.setCaminhoBaseRelatorio(HistoricoAlunoRel.getCaminhoBaseRelatorio());
		}
		superParametroRelVO.setNomeUsuario(usuarioVO.getNome());
		superParametroRelVO.setListaObjetos(Collections.singletonList(historicoTemp));
		superParametroRelVO.setQuantidade(1);
		superParametroRelVO.setVersaoSoftware(superControleRelatorio.getVersaoSistema());
		if (expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getNomeExpedicaoDiploma().trim().isEmpty()) {
			superParametroRelVO.setUnidadeEnsino(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getNome());
		} else {
			superParametroRelVO.setUnidadeEnsino(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getNomeExpedicaoDiploma());
		}
		if (tipoLayoutComparar.equals("HistoricoAlunoLayout9Rel") || tipoLayoutComparar.equals("HistoricoAlunoLayout9PortariaMECRel") || tipoLayoutComparar.equals("HistoricoAlunoLayout15Rel") || tipoLayoutComparar.equals("HistoricoAlunoLayout15PortariaMECRel") || Uteis.getIsValorNumerico(tipoLayout)) {
			superParametroRelVO.getParametros().put("logoMunicipio", configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCaminhoBaseLogoMunicipio().replaceAll("\\\\", "/") + File.separator + expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getNomeArquivoLogoMunicipio());
		}
		if (tipoLayoutComparar.equals("HistoricoAlunoLayout15Rel") || tipoLayoutComparar.equals("HistoricoAlunoLayout15PortariaMECRel")) {
			superParametroRelVO.setNomeEmpresa("MUNICÍPIO DE " + expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCidade().getNome().toUpperCase() + " - ESTADO DO " + expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCidade().getEstado().getNome().toUpperCase());
			String informacoesAdicionaisEndereco = expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getInformacoesAdicionaisEndereco().replace("|", "\n");
			superParametroRelVO.getParametros().put("informacoesCabecalho", informacoesAdicionaisEndereco);
		}
		superParametroRelVO.setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
		superParametroRelVO.adicionarParametro("assinarDigitalmente", assinarDigitalmente);
		if (Uteis.isAtributoPreenchido(getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigo(), usuarioVO)) && getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigo(), usuarioVO).getConfiguracaoGedHistoricoVO().getApresentarAssinaturaDigitalizadoFuncionario()) {
			superParametroRelVO.adicionarParametro("assinaturaFuncionarioPrincipal", expedicaoDiplomaVO.getUrlAssinaturaFuncionarioPrincipal(configuracaoGeralSistemaVO));
			superParametroRelVO.adicionarParametro("assinaturaFuncionarioSecundario", expedicaoDiplomaVO.getUrlAssinaturaFuncionarioSecundario(configuracaoGeralSistemaVO));
			superParametroRelVO.adicionarParametro("assinaturaFuncionarioTerciario", expedicaoDiplomaVO.getUrlAssinaturaFuncionarioTerciario(configuracaoGeralSistemaVO));
		} else {
			superParametroRelVO.adicionarParametro("assinaturaFuncionarioPrincipal", Constantes.EMPTY);
			superParametroRelVO.adicionarParametro("assinaturaFuncionarioSecundario", Constantes.EMPTY);
			superParametroRelVO.adicionarParametro("assinaturaFuncionarioTerciario", Constantes.EMPTY);
		}
		superParametroRelVO.adicionarParametro("apresentarInstituicaoDisciplinaAproveitada", Boolean.TRUE);
		superParametroRelVO.adicionarParametro("apresentarDisciplinaPeriodoTrancadoCanceladoTransferido", Boolean.TRUE);
		superParametroRelVO.adicionarParametro("apresentarCargaHorariaDisciplina", Boolean.TRUE);
		superParametroRelVO.adicionarLogoUnidadeEnsinoSelecionada(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora());
		if (tipoLayout.equals("HistoricoAlunoPos18Rel")) {
			superParametroRelVO.adicionarParametro("logoPadraoMunicipio", configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCaminhoBaseLogoMunicipio() + File.separator + expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getNomeArquivoLogoMunicipio());
			superParametroRelVO.adicionarParametro("simboloBandeiraPretoBranco", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "simboloBandeiraPretoBranco.jpg");
		}
		if (tipoLayout.equals("HistoricoAlunoPos18Rel")) {
			superParametroRelVO.adicionarParametro("trazerTodosProfessoresDisciplinas", Boolean.TRUE);
		}
		if (totalizadorCreditoDisciplina > 0) {
			superParametroRelVO.adicionarParametro("totalizadorCreditoDisciplina", totalizadorCreditoDisciplina.toString());
		}
		if (totalizadorCreditoEstagio > 0) {
			superParametroRelVO.adicionarParametro("totalizadorCreditoEstagio", totalizadorCreditoEstagio.toString());
		}
		if (totalizadorCreditoObrigatorioEstagio > 0) {
			superParametroRelVO.adicionarParametro("totalizadorCreditoObrigatorioEstagio", totalizadorCreditoObrigatorioEstagio.toString());
		}
		superParametroRelVO.adicionarParametro("filtrocancelado", filtroRelatorioAcademicoVO.getCanceladoHistorico());
		if (assinarDigitalmente && configGEDVO.getConfiguracaoGedHistoricoVO().getApresentarAssinaturaDigitalizadoFuncionario()) {
			montarDadosAssinaturaDigitalFuncionario(configGEDVO, historicoTemp, superParametroRelVO, configuracaoGeralSistemaVO, usuarioVO);
		} else {
			superParametroRelVO.adicionarParametro("apresentarAssinaturaDigitalFuncionarioPrimario", Boolean.FALSE);
			superParametroRelVO.adicionarParametro("apresentarAssinaturaDigitalFuncionarioSecundario", Boolean.FALSE);
			superParametroRelVO.adicionarParametro("apresentarAssinaturaDigitalFuncionarioTerciario", Boolean.FALSE);
		}
		superParametroRelVO.adicionarParametro("matriculaAluno", historicoTemp.getMatriculaVO());
		superControleRelatorio.realizarImpressaoRelatorio();
		String caminhoBasePdf = getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator;
		String caminhoPdf = caminhoBasePdf + superControleRelatorio.getCaminhoRelatorio();
		superControleRelatorio.setFazerDownload(false);
		realizarGeracaoXmlHistorico(historicoTemp, expedicaoDiplomaVO, new File(caminhoPdf), superControleRelatorio, usuarioVO);
	}
	
	public void realizarGeracaoXmlHistorico(HistoricoAlunoRelVO histAlunoRelVO, ExpedicaoDiplomaVO expedicaoDiplomaVO, File arquivoVisualHistoricoEscolar, SuperControleRelatorio superControleRelatorio, UsuarioVO usuarioVO) throws Exception {
		try {
			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora())) {
				getFacadeFactory().getUnidadeEnsinoFacade().carregarDados(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora(), usuarioVO);
			} else {
				throw new Exception("A UNIDADE CERTIFICADORA deve ser preenchida.");
			}
			ConfiguracaoGEDVO configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigo(), usuarioVO);
			if ((expedicaoDiplomaVO.getMatricula().getCurso().getNivelEducacionalGraduacaoGraduacaoTecnologica())) {
				getFacadeFactory().getExpedicaoDiplomaFacade().realizarGeracaoXMLDiplomaDigital(expedicaoDiplomaVO, configGEDVO, superControleRelatorio.getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(expedicaoDiplomaVO.getMatricula().getUnidadeEnsino().getCodigo()), null, usuarioVO, arquivoVisualHistoricoEscolar, TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL, true, true, histAlunoRelVO);
			}
		} catch (ConsistirException ce) {
			throw ce;
		} catch (Exception e) {
			throw e;
		}
	}
	
	private void montarDadosAssinaturaDigitalFuncionario(ConfiguracaoGEDVO configGEDVO, HistoricoAlunoRelVO historicoAlunoRelVO, SuperParametroRelVO superParametroRelVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		if (!Uteis.isAtributoPreenchido(configGEDVO.getCodigo())) {
			return;
		}
//		FUNCIONÁRIO PRIMÁRIO
		if (Uteis.isAtributoPreenchido(historicoAlunoRelVO.getFuncionarioPrincipalVO().getCodigo())) {
			historicoAlunoRelVO.getFuncionarioPrincipalVO().setArquivoAssinaturaVO(getFacadeFactory().getArquivoFacade().consultarAssinaturaDigitalFuncionarioPorCodigoFuncionario(historicoAlunoRelVO.getFuncionarioPrincipalVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, usuarioVO));
			if (Uteis.isAtributoPreenchido(historicoAlunoRelVO.getFuncionarioPrincipalVO().getArquivoAssinaturaVO().getCodigo())) {
				superParametroRelVO.adicionarParametro("assinaturaDigitalFuncionarioPrimario", configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + historicoAlunoRelVO.getFuncionarioPrincipalVO().getArquivoAssinaturaVO().getPastaBaseArquivoEnum().getValue() + File.separator + historicoAlunoRelVO.getFuncionarioPrincipalVO().getArquivoAssinaturaVO().getNome());
				superParametroRelVO.adicionarParametro("apresentarAssinaturaDigitalFuncionarioPrimario", configGEDVO.getConfiguracaoGedHistoricoVO().getApresentarAssinaturaDigitalizadoFuncionario());
			} else {
				superParametroRelVO.adicionarParametro("apresentarAssinaturaDigitalFuncionarioPrimario", false);
			}
		} else {
			superParametroRelVO.adicionarParametro("apresentarAssinaturaDigitalFuncionarioPrimario", false);
		}
//		FUNCIONÁRIO SECUNDÁRIO
		if (Uteis.isAtributoPreenchido(historicoAlunoRelVO.getFuncionarioSecundarioVO().getCodigo())) {
			historicoAlunoRelVO.getFuncionarioSecundarioVO().setArquivoAssinaturaVO(getFacadeFactory().getArquivoFacade().consultarAssinaturaDigitalFuncionarioPorCodigoFuncionario(historicoAlunoRelVO.getFuncionarioSecundarioVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, usuarioVO));
			if (Uteis.isAtributoPreenchido(historicoAlunoRelVO.getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getCodigo())) {
				superParametroRelVO.adicionarParametro("assinaturaDigitalFuncionarioSecundario", configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + historicoAlunoRelVO.getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getPastaBaseArquivoEnum().getValue() + File.separator + historicoAlunoRelVO.getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getNome());
				superParametroRelVO.adicionarParametro("apresentarAssinaturaDigitalFuncionarioSecundario", configGEDVO.getConfiguracaoGedHistoricoVO().getApresentarAssinaturaDigitalizadoFuncionario());
			} else {
				superParametroRelVO.adicionarParametro("apresentarAssinaturaDigitalFuncionarioSecundario", false);
			}
		} else {
			superParametroRelVO.adicionarParametro("apresentarAssinaturaDigitalFuncionarioSecundario", false);
		}
//		FUNCIONÁRIO TERCIÁRIO
		if (Uteis.isAtributoPreenchido(historicoAlunoRelVO.getFuncionarioTerciarioVO().getCodigo())) {
			historicoAlunoRelVO.getFuncionarioTerciarioVO().setArquivoAssinaturaVO(getFacadeFactory().getArquivoFacade().consultarAssinaturaDigitalFuncionarioPorCodigoFuncionario(historicoAlunoRelVO.getFuncionarioTerciarioVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, usuarioVO));
			if (Uteis.isAtributoPreenchido(historicoAlunoRelVO.getFuncionarioTerciarioVO().getArquivoAssinaturaVO().getCodigo())) {
				superParametroRelVO.adicionarParametro("assinaturaDigitalFuncionarioTerciario", configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + historicoAlunoRelVO.getFuncionarioTerciarioVO().getArquivoAssinaturaVO().getPastaBaseArquivoEnum().getValue() + File.separator + historicoAlunoRelVO.getFuncionarioTerciarioVO().getArquivoAssinaturaVO().getNome());
				superParametroRelVO.adicionarParametro("apresentarAssinaturaDigitalFuncionarioTerciario", configGEDVO.getConfiguracaoGedHistoricoVO().getApresentarAssinaturaDigitalizadoFuncionario());
			} else {
				superParametroRelVO.adicionarParametro("apresentarAssinaturaDigitalFuncionarioTerciario", false);
			}
		} else {
			superParametroRelVO.adicionarParametro("apresentarAssinaturaDigitalFuncionarioTerciario", false);
		}
	}
	
	private ConfiguracaoLayoutHistoricoVO carregarDadosLayoutHistorico(String tipoLayout, ConfiguracaoHistoricoVO configuracaoHistoricoVO, UsuarioVO usuarioVO) throws Exception {
		try {
			if (Uteis.getIsValorNumerico(tipoLayout)) {
				if (Uteis.isAtributoPreenchido(configuracaoHistoricoVO) && configuracaoHistoricoVO.getConfiguracaoLayoutHistoricoVOs().stream().anyMatch(l -> l.getCodigo().toString().equals(tipoLayout))) {
					return configuracaoHistoricoVO.getConfiguracaoLayoutHistoricoVOs().stream().filter(l -> l.getCodigo().toString().equals(tipoLayout)).findFirst().get();
				} else {
					return getFacadeFactory().getConfiguracaoLayoutHistoricoFacade().consultarPorChavePrimaria(Integer.parseInt(tipoLayout), usuarioVO);
				}
			} else {
				return new ConfiguracaoLayoutHistoricoVO();
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public void realizarGeracaoDocumentacaoAcademica(ExpedicaoDiplomaVO expedicaoDiplomaVO, SuperControleRelatorio superControleRelatorio, UsuarioVO usuarioVO) throws Exception {
		expedicaoDiplomaVO.setConsistirException(new ConsistirException());
		if (expedicaoDiplomaVO.getGerarXMLDiploma()) {
			montarConfiguracaoDiploma(expedicaoDiplomaVO, usuarioVO);
			montarDadosCoordenadorCursoDiploma(expedicaoDiplomaVO, usuarioVO);
		}
		getFacadeFactory().getExpedicaoDiplomaFacade().validarImprimirDiploma(expedicaoDiplomaVO, expedicaoDiplomaVO.getFuncionarioPrimarioVO().getCodigo(), expedicaoDiplomaVO.getFuncionarioSecundarioVO().getCodigo(), expedicaoDiplomaVO.getCargoFuncionarioPrincipalVO().getCodigo(), expedicaoDiplomaVO.getCargoFuncionarioSecundarioVO().getCodigo(), expedicaoDiplomaVO.getFuncionarioTerceiroVO().getCodigo(), expedicaoDiplomaVO.getCargoFuncionarioTerceiroVO().getCodigo(), expedicaoDiplomaVO.getLayoutDiploma(), usuarioVO, expedicaoDiplomaVO.getGerarXMLDiploma());
		getFacadeFactory().getDiplomaAlunoRelFacade().criarObjeto(expedicaoDiplomaVO.getUtilizarUnidadeMatriz(), expedicaoDiplomaVO, expedicaoDiplomaVO.getFuncionarioPrimarioVO(), expedicaoDiplomaVO.getFuncionarioSecundarioVO(), expedicaoDiplomaVO.getFuncionarioTerceiroVO(), expedicaoDiplomaVO.getCargoFuncionarioPrincipalVO(), expedicaoDiplomaVO.getCargoFuncionarioSecundarioVO(), expedicaoDiplomaVO.getCargoFuncionarioTerceiroVO(), expedicaoDiplomaVO.getTituloFuncionarioPrincipal(), expedicaoDiplomaVO.getTituloFuncionarioSecundario(), usuarioVO, expedicaoDiplomaVO.getLayoutDiploma(), expedicaoDiplomaVO.getCargoFuncionarioQuartoVO(), expedicaoDiplomaVO.getCargoFuncionarioQuintoVO(), expedicaoDiplomaVO.getFuncionarioQuartoVO(), expedicaoDiplomaVO.getFuncionarioQuintoVO(), Boolean.TRUE);
		getFacadeFactory().getExpedicaoDiplomaFacade().validarRegraEmissao(expedicaoDiplomaVO, usuarioVO);
		ConfiguracaoGEDVO configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigo(), usuarioVO);
		File arquivoVisualHistoricoEscolar = null;
		if ((expedicaoDiplomaVO.getMatricula().getCurso().getNivelEducacionalGraduacaoGraduacaoTecnologica()) && expedicaoDiplomaVO.getGerarXMLDiploma()) {
			getFacadeFactory().getExpedicaoDiplomaFacade().realizarGeracaoXMLDiplomaDigital(expedicaoDiplomaVO, configGEDVO, superControleRelatorio.getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(expedicaoDiplomaVO.getMatricula().getUnidadeEnsino().getCodigo()), null, usuarioVO, arquivoVisualHistoricoEscolar, TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL, true, true, null);
		}
	}
    
	@Override
	public String realizarGeracaoNomeArquivoExpedicaoDiploma(String origemUtilizar, MatriculaVO matriculaVO, UsuarioVO usuarioVO) {
		String origem = "DIPLOMA_";
		String nomeCurso = Uteis.removeCaractersEspeciais(matriculaVO.getCurso().getAbreviatura().trim()).replace("/", "_").replace(" ", "_").toUpperCase();
		String nomeAluno = Uteis.removeCaractersEspeciais(matriculaVO.getAluno().getNome().trim()).replace("/", "_").replace(" ", "_").toUpperCase();
		String codigoUsuario = Uteis.isAtributoPreenchido(usuarioVO) ? usuarioVO.getCodigo().toString() : "0";
		String horario = String.valueOf(new Date().getTime());
		if (Uteis.isAtributoPreenchido(matriculaVO.getCurso().getNivelEducacional()) && matriculaVO.getCurso().getNivelEducacionalPosGraduacao()) {
			origem = "CERTIFICADO_";
		}
		origem = Uteis.isAtributoPreenchido(origemUtilizar) ? origemUtilizar : origem;
		if (!Uteis.isAtributoPreenchido(nomeCurso)) {
			nomeCurso = Uteis.removeCaractersEspeciais(matriculaVO.getCurso().getNome().trim()).replace("/", "_").replace(" ", "_").toUpperCase();
		}
		int comprimentoDisponivelCurso = 240 - (origem.length() + nomeAluno.length() + codigoUsuario.length() + horario.length());
		if (nomeCurso.length() > comprimentoDisponivelCurso && comprimentoDisponivelCurso > 0) {
			nomeCurso = nomeCurso.substring(0, comprimentoDisponivelCurso);
		}
		return origem + (Uteis.isAtributoPreenchido(nomeCurso) && comprimentoDisponivelCurso > 0 ? nomeCurso + "_" : "") + nomeAluno + "_" + codigoUsuario + "_" + horario;
	}
	
	@Override
	public void carregarViaAnteriorExpedicaoDiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO) {
		if (expedicaoDiplomaVO != null && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula())) {
			StringBuilder sql = new  StringBuilder();
			sql.append("SELECT via, anulado FROM expedicaoDiploma WHERE matricula = ? ORDER BY via DESC, codigo DESC LIMIT 1");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), expedicaoDiplomaVO.getMatricula().getMatricula());
			if (tabelaResultado.next()) {
				String via = tabelaResultado.getString("via");
				if (Uteis.getIsValorNumerico(via)) {
					Boolean anulado = tabelaResultado.getBoolean("anulado");
					if (anulado) {
						expedicaoDiplomaVO.setVia(via);
					} else {
						expedicaoDiplomaVO.setVia(Uteis.isAtributoPreenchido(via) ? String.valueOf(Integer.valueOf(via) + 1) : "1");
					}
				} else {
					expedicaoDiplomaVO.setVia("1");
				}
			} else {
				expedicaoDiplomaVO.setVia("1");
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirExpedicaoDiplomaComDocumentoAssinado(ExpedicaoDiplomaVO expedicaoDiplomaVO, DocumentoAssinadoVO documentoAssinadoVO, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO)) {
			alterar(expedicaoDiplomaVO, usuario, Boolean.FALSE);
		} else {
			incluir(expedicaoDiplomaVO, usuario, Boolean.FALSE, Boolean.FALSE);
			if (Uteis.isAtributoPreenchido(documentoAssinadoVO)) {
				getFacadeFactory().getDocumentoAssinadoFacade().alterarExpedicaoDiplomaDocumentoAssinado(documentoAssinadoVO.getCodigo(), expedicaoDiplomaVO.getCodigo());
			}
		}
	}
	
	@Override
	public Integer consultarExpedicaoDiplomaUtilizarHistoricoDigital(String matricula) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT codigo FROM expedicaoDiploma WHERE matricula = ? ORDER BY via DESC, dataexpedicao DESC, codigo DESC LIMIT 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula);
		return tabelaResultado.next() ? tabelaResultado.getInt("codigo") : 0;
	}
	
	private static final String XML_NAO_GERADO = "xmlPendenteGeracao";
	private static final String XML_ASSINADO = "xmlAssinado";
	private static final String XML_PENDENTE = "xmlPendente";
	private static final String XML_REJEITADO = "xmlRejeitado";
	
	/**
	 * Método utilizado para validar se a expedição de diploma que está sendo
	 * verificada apresentará para o usuário, e estará no processo de geração de
	 * diploma/documentação em lote de acordo com a situação informada
	 * 
	 * @param expedicaoDiploma
	 * @param situacao
	 * @param tipoOrigemDocumentoAssinadoEnums
	 * @author Felipi Alves
	 */
	private void consultarEValidarSituacaoUltimoXmlMecGerado(ExpedicaoDiplomaVO expedicaoDiploma, String situacao, List<TipoOrigemDocumentoAssinadoEnum> tipoOrigemDocumentoAssinadoEnums) {
		if (Objects.nonNull(expedicaoDiploma) && Uteis.isAtributoPreenchido(expedicaoDiploma.getMatricula()) && Uteis.isAtributoPreenchido(situacao) && Uteis.isAtributoPreenchido(tipoOrigemDocumentoAssinadoEnums)) {
			List<Object> filters = new ArrayList<>();
			filters.add(expedicaoDiploma.getMatricula().getMatricula());
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT x.codigo, x.situacaoXML FROM ( ");
			sql.append("SELECT documentoassinado.codigo, CASE WHEN documentoassinado.documentoassinadoinvalido THEN 'REJEITADO' WHEN (totalDocumentoPessoaAssinado.qtdDocumentoPessoaAssinado - totalDocumentoPessoa.qtdDocumentoPessoa) = 0 THEN 'ASSINADO' WHEN totalDocumentoPessoaPendente.qtdDocumentoPessoaPendente = totalDocumentoPessoa.qtdDocumentoPessoa THEN 'PENDENTE' WHEN (totalDocumentoPessoaPendente.qtdDocumentoPessoaPendente + totalDocumentoPessoaAssinado.qtdDocumentoPessoaAssinado) = totalDocumentoPessoa.qtdDocumentoPessoa THEN 'PENDENTE' ELSE 'REJEITADO' END situacaoXML ");
			sql.append("FROM documentoassinado ");
			sql.append("INNER JOIN matricula ON matricula.matricula = documentoassinado.matricula ");
			sql.append("INNER JOIN curso ON curso.codigo = matricula.curso ");
			sql.append("LEFT JOIN LATERAL ( SELECT count(documentoassinadopessoa.codigo) AS qtdDocumentoPessoa FROM documentoassinadopessoa WHERE documentoassinadopessoa.documentoassinado = documentoassinado.codigo) AS totalDocumentoPessoa ON TRUE ");
			sql.append("LEFT JOIN LATERAL ( SELECT count(documentoassinadopessoa.codigo) AS qtdDocumentoPessoaPendente FROM documentoassinadopessoa WHERE documentoassinadopessoa.documentoassinado = documentoassinado.codigo AND documentoassinadopessoa.situacaodocumentoassinadopessoa IN ('PENDENTE')) AS totalDocumentoPessoaPendente ON TRUE ");
			sql.append("LEFT JOIN LATERAL ( SELECT count(documentoassinadopessoa.codigo) AS qtdDocumentoPessoaAssinado FROM documentoassinadopessoa WHERE documentoassinadopessoa.documentoassinado = documentoassinado.codigo AND documentoassinadopessoa.situacaodocumentoassinadopessoa IN ('ASSINADO')) AS totalDocumentoPessoaAssinado ON TRUE ");
			sql.append("WHERE matricula.matricula = ? ");
			sql.append("AND curso.niveleducacional IN ('GT', 'SU') ");
			if (Uteis.isAtributoPreenchido(expedicaoDiploma)) {
				sql.append("AND documentoassinado.expedicaodiploma = ? ");
				filters.add(expedicaoDiploma.getCodigo());
			}
			sql.append(tipoOrigemDocumentoAssinadoEnums.stream().map(tipoOrigem -> tipoOrigem.name()).collect(Collectors.joining("', '", " AND documentoassinado.tipoorigemdocumentoassinado IN ('",  "') "))).append(" ");
			sql.append("ORDER BY documentoassinado.codigo DESC LIMIT 1 ");
			sql.append(") AS x ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filters.toArray());
			if (!tabelaResultado.next()) {
				expedicaoDiploma.setSelecionado(Objects.equals(situacao, XML_NAO_GERADO));
				return;
			}
			if (!Uteis.isAtributoPreenchido(tabelaResultado.getInt("codigo")) || !Uteis.isAtributoPreenchido(tabelaResultado.getString("situacaoXML"))) {
				expedicaoDiploma.setSelecionado(Objects.equals(situacao, XML_NAO_GERADO));
				return;
			} else {
				String situacaoXml = tabelaResultado.getString("situacaoXML");
				if (Uteis.isAtributoPreenchido(situacaoXml)) {
					if (Objects.equals(situacao, XML_NAO_GERADO)) {
						expedicaoDiploma.setSelecionado(Boolean.FALSE);
					} else if (Objects.equals(situacao, XML_ASSINADO)) {
						expedicaoDiploma.setSelecionado(Objects.equals("ASSINADO", situacaoXml));
					} else if (Objects.equals(situacao, XML_PENDENTE)) {
						expedicaoDiploma.setSelecionado(Objects.equals("PENDENTE", situacaoXml));
					} else if (Objects.equals(situacao, XML_REJEITADO)) {
						expedicaoDiploma.setSelecionado(Objects.equals("REJEITADO", situacaoXml));
					}
					return;
				}
			}
		}
	}
	
	private void validarXmlMecAluno(ExpedicaoDiplomaVO expedicaoDiploma, String filtroSituacao, List<TipoOrigemDocumentoAssinadoEnum> tipoOrigemDocumentoAssinadoEnums) {
		if (Objects.nonNull(expedicaoDiploma) && expedicaoDiploma.getSelecionado() && Uteis.isAtributoPreenchido(expedicaoDiploma.getMatricula()) && Uteis.isAtributoPreenchido(filtroSituacao)) {
			consultarEValidarSituacaoUltimoXmlMecGerado(expedicaoDiploma, filtroSituacao, tipoOrigemDocumentoAssinadoEnums);
		}
	}
	
	/**
	 * Método utilizado pela tela Expedição Diploma Lote, com o intuito de filtrar
	 * para o usuário os alunos de acordo com a situação do diploma digital
	 * selecionado e a documentação acadêmica selecionada
	 * 
	 * @param expedicaoDiplomaVOs
	 * @param filtroSituacaoDiplomaDigital
	 * @param filtroSituacaoDocumentacaoAcademica
	 * @author Felipi Alves
	 */
	@Override
	public void realizarFiltragemAlunosPermitirGerarXML(List<ExpedicaoDiplomaVO> expedicaoDiplomaVOs, String filtroSituacaoDiplomaDigital, String filtroSituacaoDocumentacaoAcademica) {
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVOs)) {
			expedicaoDiplomaVOs.stream().forEach(expedicaoDiploma -> expedicaoDiploma.setSelecionado(Boolean.TRUE));
			if (Uteis.isAtributoPreenchido(filtroSituacaoDiplomaDigital) || Uteis.isAtributoPreenchido(filtroSituacaoDocumentacaoAcademica)) {
				for (ExpedicaoDiplomaVO expedicaoDiploma : expedicaoDiplomaVOs) {
					if (Objects.nonNull(expedicaoDiploma) && Uteis.isAtributoPreenchido(expedicaoDiploma.getMatricula())) {
						validarXmlMecAluno(expedicaoDiploma, filtroSituacaoDiplomaDigital, new ArrayList<>(Arrays.asList(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL, TipoOrigemDocumentoAssinadoEnum.EXPEDICAO_DIPLOMA)));
						validarXmlMecAluno(expedicaoDiploma, filtroSituacaoDocumentacaoAcademica, new ArrayList<>(Arrays.asList(TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL)));
					}
				}
			}
		}
	}
	
	private Boolean viaIgualFormatoNumericoInvalido(String via) {
		if (Uteis.isAtributoPreenchido(via)) {
			if (Objects.equals(via, "01")) {
				return Boolean.TRUE;
			} else if (Objects.equals(via, "02")) {
				return Boolean.TRUE;
			} else if (Objects.equals(via, "03")) {
				return Boolean.TRUE;
			} else if (Objects.equals(via, "04")) {
				return Boolean.TRUE;
			} else if (Objects.equals(via, "05")) {
				return Boolean.TRUE;
			} else if (Objects.equals(via, "06")) {
				return Boolean.TRUE;
			} else if (Objects.equals(via, "07")) {
				return Boolean.TRUE;
			} else if (Objects.equals(via, "08")) {
				return Boolean.TRUE;
			} else if (Objects.equals(via, "09")) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	}
	
	@Override
	public void validarViaExpedicaoDiplomaValida(ExpedicaoDiplomaVO expedicaoDiplomaVO) {
		if (Objects.nonNull(expedicaoDiplomaVO) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getVia())) {
			String MENSAGEM_ERRO = "Identificamos um erro no formato da via. O erro ocorreu porque a via contém caracteres inválidos, como letras. Por favor, insira apenas números, antiga via: \"" + expedicaoDiplomaVO.getVia() + "\"";
			if (!Uteis.getIsValorNumerico2(expedicaoDiplomaVO.getVia())) {
				expedicaoDiplomaVO.setPossuiErro(Boolean.TRUE);
				expedicaoDiplomaVO.setErro(MENSAGEM_ERRO);
				expedicaoDiplomaVO.setVia(Constantes.EMPTY);
			} else if (expedicaoDiplomaVO.getVia().length() > 2) {
				expedicaoDiplomaVO.setPossuiErro(Boolean.TRUE);
				expedicaoDiplomaVO.setErro(MENSAGEM_ERRO);
				expedicaoDiplomaVO.setVia(Constantes.EMPTY);
			} else if (Uteis.getIsValorNumerico2(expedicaoDiplomaVO.getVia()) && viaIgualFormatoNumericoInvalido(expedicaoDiplomaVO.getVia())) {
				expedicaoDiplomaVO.setPossuiErro(Boolean.TRUE);
				expedicaoDiplomaVO.setErro(MENSAGEM_ERRO);
				expedicaoDiplomaVO.setVia(Constantes.EMPTY);
			}
		}
	}
	
	@Override
	public void realizarCorrecaoDocumentacaoMatriculaPorExpedicaoDiploma(ExpedicaoDiplomaVO expedicaoDiploma, ProgressBarVO progressBar) throws Exception {
		if (Objects.nonNull(expedicaoDiploma)) {
			progressBar.setProgresso(progressBar.getProgresso() + 1);
			progressBar.setStatus("(" + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + ") matrícula: " + expedicaoDiploma.getMatricula().getMatricula() + ", aluno: " + expedicaoDiploma.getMatricula().getAluno().getNome());
			expedicaoDiploma.setConsistirException(new ConsistirException());
			List<DocumetacaoMatriculaVO> listaDocumentosAssinados = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarDocumentacoesMatriculasAssinadosParaCorrecaoPdfA(expedicaoDiploma.getMatricula().getMatricula());
			if (Uteis.isAtributoPreenchido(listaDocumentosAssinados)) {
				ConfiguracaoGEDVO configuracaoGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(expedicaoDiploma.getMatricula().getUnidadeEnsino().getCodigo(), progressBar.getUsuarioVO());
				List<String> listaCodigoArquivoSucesso = new ArrayList<>(0);
				for (DocumetacaoMatriculaVO documetacaoMatricula : listaDocumentosAssinados) {
					if (Uteis.isAtributoPreenchido(documetacaoMatricula) && Uteis.isAtributoPreenchido(documetacaoMatricula.getArquivoAssinado())) {
						try {
							PessoaVO responsavelAprovacao = Uteis.isAtributoPreenchido(documetacaoMatricula.getRespAprovacaoDocDep().getPessoa()) ? documetacaoMatricula.getRespAprovacaoDocDep().getPessoa() : (PessoaVO) (Uteis.clonar(progressBar.getUsuarioVO().getPessoa()));
							Date dataAprovacao = Uteis.isAtributoPreenchido(documetacaoMatricula.getDataAprovacaoDocDep()) ? documetacaoMatricula.getDataAprovacaoDocDep() : new Date();
							ConfiguracaoGeralSistemaVO configuracaoGeralSistema = progressBar.getConfiguracaoGeralSistemaVO();
							UnidadeEnsinoVO unidadeEnsino = expedicaoDiploma.getMatricula().getUnidadeEnsino();
							ArquivoVO arquivoAssinado = documetacaoMatricula.getArquivoAssinado();
							UsuarioVO usuarioLogado = progressBar.getUsuarioVO();
							
							String pastaBaseArquivoTemporario = progressBar.getCaminhoWebRelatorio() + File.separator + Constantes.relatorio;
							String nomeArquivoTemporario = (new Date().getTime()) + arquivoAssinado.getNome();
							File arquivoTemporario = new File(pastaBaseArquivoTemporario + File.separator + nomeArquivoTemporario);
							File arquivo = new File(configuracaoGeralSistema.getLocalUploadArquivoFixo() + File.separator + arquivoAssinado.getPastaBaseArquivo() + File.separator + arquivoAssinado.getNome());
							Uteis.checkState(!Uteis.isAtributoPreenchido(arquivo), "O arquivo não existe no servidor de arquivo on-line");
							getFacadeFactory().getArquivoHelper().copiar(arquivo, arquivoTemporario);
							Boolean isPdfA = getFacadeFactory().getArquivoHelper().verificarPDFIsPDFA(arquivoTemporario.getPath());
							if (isPdfA) {
								continue;
							}
							try {
								getFacadeFactory().getArquivoHelper().realizarConversaoPdfPdfA(arquivoTemporario.getPath(), configuracaoGeralSistema, arquivoAssinado, Boolean.FALSE, false, Boolean.FALSE);
							} catch (Exception e) {
								if (!Uteis.isAtributoPreenchido(arquivoTemporario)) {
									getFacadeFactory().getArquivoHelper().copiar(arquivo, arquivoTemporario);
								}
								try {
									getFacadeFactory().getArquivoHelper().realizarConversaoPdfPdfA(arquivoTemporario.getPath(), configuracaoGeralSistema, arquivoAssinado, Boolean.TRUE, false, Boolean.FALSE);
								} catch (Exception e2) {
									throw e2;
								}
							}
							Uteis.checkState(!Uteis.isAtributoPreenchido(arquivoTemporario), "Não foi possivel realizar a conversão do arquivo assinado para PDF/A");
							if (arquivoAssinado.getArquivoAssinadoFuncionario()) {
								ArquivoVO certificadoParaDocumento = getFacadeFactory().getDocumentoAssinadoFacade().validarCertificadoParaDocumento(unidadeEnsino, configuracaoGEDVO, configuracaoGeralSistema, usuarioLogado, responsavelAprovacao);
								Uteis.checkState(!Uteis.isAtributoPreenchido(certificadoParaDocumento), "Não foi possível realizar a montagem do certificado do funcionário responsável pela aprovação do documento");
								arquivoTemporario = getFacadeFactory().getDocumentoAssinadoFacade().preencherAssinadorDigitalDocumentoPdfParaDocumentoMatriculaV2(arquivoTemporario.getAbsolutePath(), certificadoParaDocumento, responsavelAprovacao.getSenhaCertificadoParaDocumento(), pastaBaseArquivoTemporario, nomeArquivoTemporario, AlinhamentoAssinaturaDigitalEnum.RODAPE_ESQUERDA, "#000000", 40f, 200f, 6f, 400, 20, 400, 2, configuracaoGeralSistema, configuracaoGEDVO, Constantes.EMPTY, dataAprovacao, Boolean.FALSE, documetacaoMatricula.getIdDocumetacao());
								Uteis.checkState(!Uteis.isAtributoPreenchido(arquivoTemporario), "Não foi possível realizar a assinatura da documentação pelo responsável pela aprovação");
							}
							if (arquivoAssinado.getArquivoAssinadoUnidadeEnsino()) {
								String urlValidacao = configuracaoGeralSistema.getUrlAcessoExternoAplicacao() + "/visaoAdministrativo/academico/documentoAssinado.xhtml?tipoDoc=" + TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_ALUNO + "&dados=0";
								arquivoTemporario = getFacadeFactory().getDocumentoAssinadoFacade().preencherAssinadorDigitalDocumentoPdfParaDocumentoMatriculaV2(arquivoTemporario.getAbsolutePath(), configuracaoGEDVO.getCertificadoDigitalUnidadeEnsinoVO(), configuracaoGEDVO.getSenhaCertificadoDigitalUnidadeEnsino(), pastaBaseArquivoTemporario, nomeArquivoTemporario, AlinhamentoAssinaturaDigitalEnum.RODAPE_ESQUERDA, "#000000", 40f, 200f, 6f, 60, 20, 60, 2, configuracaoGeralSistema, configuracaoGEDVO, urlValidacao, dataAprovacao, Boolean.FALSE, documetacaoMatricula.getIdDocumetacao());
								Uteis.checkState(!Uteis.isAtributoPreenchido(arquivoTemporario), "Não foi possível realizar a assinatura da documentação por unidade de ensino");
							}
							getFacadeFactory().getArquivoHelper().escreverArquivo(new FileInputStream(arquivoTemporario), arquivo.getPath());
							listaCodigoArquivoSucesso.add(arquivoAssinado.getCodigo().toString());
						} catch (Exception e) {
							String erro = "Erro: " + e.getMessage() + ", tipo de documento: " + documetacaoMatricula.getTipoDeDocumentoVO().getNome().toUpperCase();
							expedicaoDiploma.getConsistirException().getListaMensagemErro().add(erro);
						}
					}
				}
				if (Uteis.isAtributoPreenchido(listaCodigoArquivoSucesso)) {
					getConexao().getJdbcTemplate().update("UPDATE arquivo SET arquivoconvertidopdfa = TRUE WHERE codigo IN (" + listaCodigoArquivoSucesso.stream().collect(Collectors.joining(", ")) +")");
				}
				if (Uteis.isAtributoPreenchido(expedicaoDiploma.getConsistirException().getListaMensagemErro())) {
					throw expedicaoDiploma.getConsistirException();
				}
			}
		}
	}
}
